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

public class runner {
  public static void main(String[] args) {
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;

    String url = "jdbc:postgresql://localhost/sid_clone";
    String user = "xiaohua";
    String password = "onefish";

    String splitter = "\",\"";

    try {
      con = DriverManager.getConnection(url, user, password);
      // get the conversation list of bprd network
      CSVFileReader packetFile = new CSVFileReader("data/bprd-7315-packets.csv", splitter,
              null, null);
      CSVFileReader connFile = new CSVFileReader("data/bprd-7315-conversations.csv", splitter,
              packetFile.startTime, "bprd");

      List<TCPConnection> bprdConns = connFile.getConnectionList();

      // get the conversation list of bprd network
      packetFile = new CSVFileReader("data/colo-3728-packets.csv", splitter, null, null);
      connFile = new CSVFileReader("data/colo-3728-conversations.csv", splitter,
              packetFile.startTime, "colo");
      List<TCPConnection> coloConns_1 = connFile.getConnectionList();

      packetFile = new CSVFileReader("data/colo-3728-packets1.csv", splitter, null, null);
      connFile = new CSVFileReader("data/colo-3728-conversations1.csv", splitter,
              packetFile.startTime, "colo");
      List<TCPConnection> coloConns_2 = connFile.getConnectionList();
      List<TCPConnection> coloConns = new ArrayList<TCPConnection>();
      coloConns.addAll(coloConns_1);
      coloConns.addAll(coloConns_2);
      
      
      // get the conversation list of trunk network
      packetFile = new CSVFileReader("data/trunk-3728-packets.csv", splitter, null, null);
      connFile = new CSVFileReader("data/trunk-3728-conversations.csv", splitter,
              packetFile.startTime, "trunk");
      List<TCPConnection> trunkConns_1 = connFile.getConnectionList();

      packetFile = new CSVFileReader("data/trunk-3728-packets1.csv", splitter, null, null);
      connFile = new CSVFileReader("data/trunk-3728-conversations1.csv", splitter,
              packetFile.startTime, "trunk");
      List<TCPConnection> trunkConns_2 = connFile.getConnectionList();
      Collections.sort(trunkConns_2);
//      List<ArrayList<Packet>> coveredPackets = new ArrayList<ArrayList<Packet>>();
//      long t1 = System.nanoTime();
//      for (int i = 0; i < 10; i++){
//        ArrayList<Packet> packetsIncluded = trunkConns_2.get(i).getCoveredPackets(con);
//        coveredPackets.add(packetsIncluded);
//      }
//      long t2 = System.nanoTime();
//      System.out.println((t2 - t1) * 1E-9);
//      System.out.println(coveredPackets.get(1).size());
//      int index = 1;
//      for (TCPConnection conn: trunkConns_2){
//        ArrayList<Packet> packetsIncluded = conn.getCoveredPackets(con);
//        System.out.println(index++);
//        coveredPackets.add(packetsIncluded);
//      }
      List<TCPConnection> trunkConns = new ArrayList<TCPConnection>();
      trunkConns.addAll(trunkConns_1);
      trunkConns.addAll(trunkConns_2);
      
      // sort the conversation lists
      Collections.sort(trunkConns);
      Collections.sort(coloConns);
      Collections.sort(bprdConns);
      
      // get the overall conversation list
      List<TCPConnection> allConns = new ArrayList<TCPConnection>();
      allConns.addAll(bprdConns);
      allConns.addAll(coloConns);
      allConns.addAll(trunkConns);
      Collections.sort(allConns);
      
      System.out.println("size of bprd conns:" + bprdConns.size());
      System.out.println("size of colo conns:" + coloConns.size());
      System.out.println("size of trunk conns:" + trunkConns.size());
      System.out.println("number of all connections:" + (bprdConns.size()+coloConns.size()+trunkConns.size()));
      
//      for (TCPConnection conn: allConns){
//        System.out.println(conn.startTime + "\t" + conn.network);
//      }
      
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
    // for (Map.Entry<String, String> entry: ipMap.entrySet()) {
    // System.out.println(entry.getKey() + "\t" + entry.getValue());
    // }
  }
}
