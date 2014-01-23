package logParsers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import types.Packet;
import types.TCPConnection;

//import logParsers.ConnectionFeatureExtractor;

public class runner {
  public static void main(String[] args) {
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;

    String url = "jdbc:postgresql://localhost/sid_clone";
    String user = "xiaohua";
    String password = "onefish";

//    String splitter = "\",\"";
    
    String connSplitter = "\t";
//    String packetSplitter = "\",\"";
    

    try {
      con = DriverManager.getConnection(url, user, password);
      // get the conversation list of bprd network
      CSVFileReader connFile = new CSVFileReader("data/bprd1/conn.log", connSplitter, "bprd");
      CSVFileReader fileAccess = new CSVFileReader("data/bprd1/files.log", connSplitter, null);
      
      List<TCPConnection> bprdConns = connFile.getConnectionList();
      
      
      Collections.sort(bprdConns);
//      List<ArrayList<Packet>> coveredPackets = new ArrayList<ArrayList<Packet>>();
      
//      int index = 1;
      long t1 = System.nanoTime();
      for (int i = 0; i < bprdConns.size(); i++) {
        TCPConnection targetConn = bprdConns.get(i);
        targetConn.getCoveredPackets(con);
        List<TCPConnection> connWindow = getConnectionWindow(bprdConns, targetConn, i);
        ConnectionFeatureExtractor featureExtractor = new ConnectionFeatureExtractor(targetConn, connWindow);
        featureExtractor.getUrgentCount();
        featureExtractor.getSameHostFeatures();
        featureExtractor.getSameServiceFeatures();
      }      
      long t2 = System.nanoTime();
      System.out.println((t2-t1) * 1E-9);
      
      
      
//      // get the conversation list of colo network
//      packetFile = new CSVFileReader("data/colo-3728-packets.csv", splitter, null, null);
//      connFile = new CSVFileReader("data/colo-3728-conversations.csv", splitter,
//              packetFile.startTime, "colo");
//      List<TCPConnection> coloConns_1 = connFile.getConnectionList();
//
//      packetFile = new CSVFileReader("data/colo-3728-packets1.csv", splitter, null, null);
//      connFile = new CSVFileReader("data/colo-3728-conversations1.csv", splitter,
//              packetFile.startTime, "colo");
//      List<TCPConnection> coloConns_2 = connFile.getConnectionList();
//      List<TCPConnection> coloConns = new ArrayList<TCPConnection>();
//      coloConns.addAll(coloConns_1);
//      coloConns.addAll(coloConns_2);
//
//      // get the conversation list of trunk network
//      packetFile = new CSVFileReader("data/trunk-3728-packets.csv", splitter, null, null);
//      connFile = new CSVFileReader("data/trunk-3728-conversations.csv", splitter,
//              packetFile.startTime, "trunk");
//      List<TCPConnection> trunkConns_1 = connFile.getConnectionList();
//
//      packetFile = new CSVFileReader("data/trunk-3728-packets1.csv", splitter, null, null);
//      connFile = new CSVFileReader("data/trunk-3728-conversations1.csv", splitter,
//              packetFile.startTime, "trunk");
//      List<TCPConnection> trunkConns_2 = connFile.getConnectionList();
//      List<TCPConnection> trunkConns = new ArrayList<TCPConnection>();
//      trunkConns.addAll(trunkConns_1);
//      trunkConns.addAll(trunkConns_2);
//
//      // sort the conversation lists
//      Collections.sort(trunkConns);
//      Collections.sort(coloConns);
//      Collections.sort(bprdConns);
//
//      // get the overall conversation list
//      List<TCPConnection> allConns = new ArrayList<TCPConnection>();
//      allConns.addAll(bprdConns);
//      allConns.addAll(coloConns);
//      allConns.addAll(trunkConns);
//      Collections.sort(allConns);
//
//      System.out.println("size of bprd conns:" + bprdConns.size());
//      System.out.println("size of colo conns:" + coloConns.size());
//      System.out.println("size of trunk conns:" + trunkConns.size());
//      System.out.println("number of all connections:"
//              + (bprdConns.size() + coloConns.size() + trunkConns.size()));

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
        if (con != null) {
          con.close();
        }

      } catch (SQLException ex) {
        Logger lgr = Logger.getLogger(runner.class.getName());
        lgr.log(Level.WARNING, ex.getMessage(), ex);
      }
    }
  }

  private static List<TCPConnection> getConnectionWindow(List<TCPConnection> allConns,
          TCPConnection conn, int index) {
    double startTime = conn.startTime;
    List<TCPConnection> result = new ArrayList<TCPConnection>();
    for (int i = 0; i< index;i++){
      if (allConns.get(i).startTime >= startTime-2){
        result.add(allConns.get(i));
      }
    }
    return result;
  }
}
