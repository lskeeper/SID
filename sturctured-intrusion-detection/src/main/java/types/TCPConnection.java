package types;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import logParsers.runner;

public class TCPConnection implements Comparable<TCPConnection> {
  public double startTime;

  public double endTime;

  public String addressA;

  public String addressB;

  public String portA;

  public String portB;

  public int packetNum;

  public int byteNum;

  public int packetA2B;

  public int packetB2A;

  public int byteA2B;

  public int byteB2A;

  public String network;

  public double relStart;

  public double duration;

  public TCPConnection(String[] elements, double firstStartTime, String network) {
    try {
      this.addressA = elements[0].replace("\"", "");
      this.portA = elements[1];
      this.addressB = elements[2];
      this.portB = elements[3];
      this.packetNum = Integer.parseInt(elements[4]);
      this.byteNum = Integer.parseInt(elements[5]);
      this.packetA2B = Integer.parseInt(elements[6]);
      this.packetB2A = Integer.parseInt(elements[7]);
      this.byteA2B = Integer.parseInt(elements[8]);
      this.byteB2A = Integer.parseInt(elements[9]);
      this.relStart = Double.parseDouble(elements[10]);
      this.duration = Double.parseDouble(elements[11]);
      this.startTime = firstStartTime + this.relStart;
      this.endTime = this.startTime + this.duration;
      this.network = network;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public int compareTo(TCPConnection compareConn) {
    if (this.startTime - compareConn.startTime > 0) {
      return 1;
    } else if (this.startTime == compareConn.startTime) {
      if (this.endTime - compareConn.endTime > 0) {
        return 1;
      } else {
        return -1;
      }
    } else {
      return -1;
    }

  }

  public ArrayList<Packet> getCoveredPackets(Connection con) throws SQLException {
    Statement st = con.createStatement();
    ArrayList<Packet> result = new ArrayList<Packet>();
    ResultSet rs = null;
    double startTime = this.startTime;
    double endTime = this.endTime;
    String source_addr = this.addressA;
    String dest_addr = this.addressB;
    String network = this.network;
    try {
      rs = st.executeQuery(String.format("SELECT id FROM ip WHERE ip='%s'", source_addr));
      rs.next();
      int source_ip = rs.getInt("id");
      rs = st.executeQuery(String.format("SELECT id FROM ip WHERE ip='%s'", dest_addr));
      rs.next();
      int dest_ip = rs.getInt("id");

      // System.out.println(startTime + "\t" + endTime);
      String finalQuery = String
              .format("SELECT * from tcpdump WHERE raw_time BETWEEN %1$f AND %2$f AND network='%3$s' AND ((source_ip=%4$d AND dest_ip=%5$d) OR (source_ip=%5$d AND dest_ip=%4$d))",
                      startTime, endTime, network, source_ip, dest_ip);
//      long t1 = System.nanoTime();
      rs = st.executeQuery(finalQuery);
//      long t2 = System.nanoTime();
//      System.out.println((t2 - t1) * 1E-9);
      while (rs.next()) {
        result.add(new Packet(rs));
      }

    } catch (SQLException ex) {
      Logger lgr = Logger.getLogger(runner.class.getName());
      lgr.log(Level.SEVERE, ex.getMessage(), ex);

    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (st != null) {
          st.close();
        }
      } catch (SQLException ex) {
        Logger lgr = Logger.getLogger(runner.class.getName());
        lgr.log(Level.WARNING, ex.getMessage(), ex);
      }
    }
    return result;
  }
}
