package logParsers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import types.Packet;
import types.TCPConnection;

public class ConnectionFeatureExtractor {
  public Connection dbConnection = null;

  public ConnectionFeatureExtractor(Connection con) {
    this.dbConnection = con;
  }

  public List<Packet> getCoveredPackets() {

    List<Packet> result = new ArrayList<Packet>();
    Statement st = null;
    ResultSet rs = null;
    Float startTime = null;
    String splitter = "\",\"";
    
    try {
      // con = DriverManager.getConnection(url, user, password);
      st = this.dbConnection.createStatement();
      rs = st.executeQuery("SELECT * FROM tcpdump LIMIT 1 OFFSET 0 WHERE network='br'");
      startTime = rs.getFloat(1);
//      CSVFileReader connFile = new CSVFileReader("data/bprd-7315-conversations.csv", splitter, startTime);
//      CSVFileReader packetFile = new CSVFileReader("data/bprd-7315-packets.csv", splitter, startTime);
//      connFile.parseCSV();
//      packetFile.parseCSV();
//      List<TCPConnection> connList = connFile.getConnectionList();
//      for (TCPConnection tcpConn : connList) {
//        
//      }
      // rs = st.executeQuery("SELECT * FROM ip");

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
        if (this.dbConnection != null) {
          this.dbConnection.close();
        }

      } catch (SQLException ex) {
        Logger lgr = Logger.getLogger(runner.class.getName());
        lgr.log(Level.WARNING, ex.getMessage(), ex);
      }
    }

    

    return result;
  }
}
