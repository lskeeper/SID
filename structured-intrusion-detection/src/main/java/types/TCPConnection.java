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

  public String label;

  public double startTime;

  public double endTime;

  public String uid;

  public String addressA;

  public int srcIP;

  public int destIP;

  public String addressB;

  public String portA;

  public String portB;

  public String protocol;

  public String service;

  public int missedBytes;

  public int packetNum;

  public int byteNum;

  public int packetA2B;

  public int packetB2A;

  public int byteA2B;

  public int byteB2A;

  public String network;

  public double duration;

  public String state;

  public String history;

  public int urgentNum;

  // Traffic features computed using a 2-second time window

  public int sameHostCount;

  public int sameSrvCount;

  public double sameHostSErrorRate;

  public double sameHostRErrorRate;

  public double sameHostSameSrvRate;

  public double sameSrvSErrorRate;

  public double sameSrvRErrorRate;

  public double sameSrvDiffHostRate;

  // File-based content features

  public int nFileAcc;

  public int nFTPCmd;

  public int imageAcc;

  public int textAcc;

  public int appAcc;

  public int binAcc;

  // The packets each connection covers

  public ArrayList<Packet> coveredPackets;

  public TCPConnection(String[] elements, String network) {
    try {
      this.startTime = Double.parseDouble(elements[0]);
      this.uid = elements[1];
      this.addressA = elements[2];
      this.portA = elements[3];
      this.addressB = elements[4];
      this.portB = elements[5];
      this.protocol = elements[6];
      this.service = elements[7];
      this.duration = Double.parseDouble(elements[8]);
      this.endTime = this.startTime + this.duration;
      this.byteA2B = Integer.parseInt(elements[9]);
      this.byteB2A = Integer.parseInt(elements[10]);
      this.state = elements[11];
      this.missedBytes = Integer.parseInt(elements[13]);
      this.history = elements[14];
      this.packetA2B = Integer.parseInt(elements[15]);
      this.packetB2A = Integer.parseInt(elements[17]);
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

  public void getCoveredPackets(Connection con) throws SQLException {
    Statement st = con.createStatement();
    this.coveredPackets = new ArrayList<Packet>();
    ResultSet rs = null;
    double startTime = this.startTime;
    double endTime = this.endTime;
    String source_addr = this.addressA;
    String dest_addr = this.addressB;
    String network = this.network;
    try {
      rs = st.executeQuery(String.format("SELECT * FROM ip WHERE ip='%s'", source_addr));
      rs.next();
      int source_ip = rs.getInt("id");
      Boolean sourceBgAttacker = rs.getBoolean("bg_attacker");
      Boolean sourceBgScanner = rs.getBoolean("bg_scanner");
      Boolean sourceAttacker = rs.getBoolean("attacker");
      Boolean sourceDecoy = rs.getBoolean("decoy");
      Boolean sourceSteppingStone = rs.getBoolean("stepping_stone");
      this.srcIP = source_ip;

      rs = st.executeQuery(String.format("SELECT * FROM ip WHERE ip='%s'", dest_addr));
      rs.next();
      int dest_ip = rs.getInt("id");
      this.destIP = dest_ip;
      Boolean destBgAttacker = rs.getBoolean("bg_attacker");
      Boolean destBgScanner = rs.getBoolean("bg_scanner");
      Boolean destAttacker = rs.getBoolean("attacker");
      Boolean destDecoy = rs.getBoolean("decoy");
      Boolean destSteppingStone = rs.getBoolean("stepping_stone");

      // conditions for labeling
      if (sourceBgAttacker || destBgAttacker) {
        this.label = "bg_attack";
      } else if (sourceBgScanner || destBgScanner) {
        this.label = "bg_scan";
      } else if (sourceAttacker || destAttacker || sourceDecoy || destDecoy || sourceSteppingStone
              || destSteppingStone) {
        this.label = "attack";
      } else {
        this.label = "normal";
      }

      String finalQuery = String
              .format("SELECT * from tcpdump WHERE raw_time BETWEEN %1$f AND %2$f AND network='%3$s' AND ((source_ip=%4$d AND dest_ip=%5$d) OR (source_ip=%5$d AND dest_ip=%4$d))",
                      startTime, endTime, network, source_ip, dest_ip);
      rs = st.executeQuery(finalQuery);
      while (rs.next()) {
        this.coveredPackets.add(new Packet(rs));
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
  }
}
