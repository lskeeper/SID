package logParsers;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import types.FTPCommand;
import types.FileAccess;
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

    // String splitter = "\",\"";

    String connSplitter = "\t";
    // String packetSplitter = "\",\"";

    try {
      con = DriverManager.getConnection(url, user, password);
      // get the conversation list of bprd network
      CSVFileReader connFile = new CSVFileReader("data/bprd1/conn.log", connSplitter, "bprd");
//      List<TCPConnection> bprdConns = connFile.getConnectionList();

      CSVFileReader fileAccess = new CSVFileReader("data/bprd1/files.log", connSplitter, null);
      CSVFileReader ftpCmds = new CSVFileReader("data/bprd1/ftp.log", connSplitter, null);

      List<FileAccess> fileAccList = fileAccess.getFileAccessList();
      List<FTPCommand> ftpCmdList = ftpCmds.getFTPCmdList();

//      Collections.sort(bprdConns);

//      ConnectionFeatureExtractor featureExtractor = new ConnectionFeatureExtractor(bprdConns, con);
//      featureExtractor.getTrafficBasedFeatures();
//      featureExtractor.getContentBasedFeatures(fileAccList, ftpCmdList);
//
//      features2CSV(bprdConns, "data/bprd1/features.csv");

       // get the conversation list of colo network
      
//      fileAccList = new ArrayList<FileAccess>();
//      ftpCmdList = new ArrayList<FTPCommand>();
//      List<TCPConnection> coloConns = new ArrayList<TCPConnection>();
//      
//      connFile = new CSVFileReader("data/colo1/conn.log", connSplitter, "colo");
//      List<TCPConnection> coloConns1 = connFile.getConnectionList();
//      CSVFileReader fileAccess1 = new CSVFileReader("data/colo1/files.log", connSplitter, null);
//      CSVFileReader ftpCmds1 = new CSVFileReader("data/colo1/ftp.log", connSplitter, null);
//      List<FileAccess> fileAccList1 = fileAccess1.getFileAccessList();
//      List<FTPCommand> ftpCmdList1 = ftpCmds1.getFTPCmdList();
//      
//      connFile = new CSVFileReader("data/colo2/conn.log", connSplitter, "colo");
//      List<TCPConnection> coloConns2 = connFile.getConnectionList();
//      CSVFileReader fileAccess2 = new CSVFileReader("data/colo2/files.log", connSplitter, null);
//      CSVFileReader ftpCmds2 = new CSVFileReader("data/colo2/ftp.log", connSplitter, null);
//      List<FileAccess> fileAccList2 = fileAccess2.getFileAccessList();
//      List<FTPCommand> ftpCmdList2 = ftpCmds2.getFTPCmdList();
//      
//      coloConns.addAll(coloConns1);
//      coloConns.addAll(coloConns2);
//      Collections.sort(coloConns);
//      
//      fileAccList.addAll(fileAccList1);
//      fileAccList.addAll(fileAccList2);
//      ftpCmdList.addAll(ftpCmdList1);
//      ftpCmdList.addAll(ftpCmdList2);
//      
//    ConnectionFeatureExtractor featureExtractor = new ConnectionFeatureExtractor(coloConns, con);
//    featureExtractor.getTrafficBasedFeatures();
//    featureExtractor.getContentBasedFeatures(fileAccList, ftpCmdList);
//    
//    features2CSV(coloConns, "data/clusteringData/coloFeatures.csv");
      
    // get the conversation list of trunk network
      fileAccList = new ArrayList<FileAccess>();
      ftpCmdList = new ArrayList<FTPCommand>();
      List<TCPConnection> trunkConns = new ArrayList<TCPConnection>();
      
      connFile = new CSVFileReader("data/trunk1/conn.log", connSplitter, "trunk");
      List<TCPConnection> trunkConns1 = connFile.getConnectionList();
      CSVFileReader fileAccess1 = new CSVFileReader("data/trunk1/files.log", connSplitter, null);
      CSVFileReader ftpCmds1 = new CSVFileReader("data/trunk1/ftp.log", connSplitter, null);
      List<FileAccess> fileAccList1 = fileAccess1.getFileAccessList();
      List<FTPCommand> ftpCmdList1 = ftpCmds1.getFTPCmdList();
      
      connFile = new CSVFileReader("data/trunk2/conn.log", connSplitter, "trunk");
      List<TCPConnection> trunkConns2 = connFile.getConnectionList();
      CSVFileReader fileAccess2 = new CSVFileReader("data/trunk2/files.log", connSplitter, null);
      CSVFileReader ftpCmds2 = new CSVFileReader("data/trunk2/ftp.log", connSplitter, null);
      List<FileAccess> fileAccList2 = fileAccess2.getFileAccessList();
      List<FTPCommand> ftpCmdList2 = ftpCmds2.getFTPCmdList();
      
      trunkConns.addAll(trunkConns1);
      trunkConns.addAll(trunkConns2);
      Collections.sort(trunkConns);
      
      fileAccList.addAll(fileAccList1);
      fileAccList.addAll(fileAccList2);
      ftpCmdList.addAll(ftpCmdList1);
      ftpCmdList.addAll(ftpCmdList2);
      
      ConnectionFeatureExtractor featureExtractor = new ConnectionFeatureExtractor(trunkConns, con);
      featureExtractor.getTrafficBasedFeatures();
      featureExtractor.getContentBasedFeatures(fileAccList, ftpCmdList);
      
      features2CSV(trunkConns, "data/clusteringData/trunkFeatures.csv");
      //
      // // get the overall conversation list
      // List<TCPConnection> allConns = new ArrayList<TCPConnection>();
      // allConns.addAll(bprdConns);
      // allConns.addAll(coloConns);
      // allConns.addAll(trunkConns);
      // Collections.sort(allConns);
      //
      // System.out.println("size of bprd conns:" + bprdConns.size());
      // System.out.println("size of colo conns:" + coloConns.size());
      // System.out.println("size of trunk conns:" + trunkConns.size());
      // System.out.println("number of all connections:"
      // + (bprdConns.size() + coloConns.size() + trunkConns.size()));

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

  private static void features2CSV(List<TCPConnection> bprdConns, String fileName) {
    try {
      FileWriter writer = new FileWriter(fileName);
      for (TCPConnection conn : bprdConns) {
        writer.append(conn.protocol + ",");
        writer.append(conn.service + ",");
        writer.append(Integer.toString(conn.missedBytes) + ",");
        writer.append(Integer.toString(conn.byteA2B) + ",");
        writer.append(Integer.toString(conn.byteB2A) + ",");
        writer.append(Integer.toString(conn.packetA2B) + ",");
        writer.append(Integer.toString(conn.packetB2A) + ",");
        writer.append(Double.toString(conn.duration) + ",");
        writer.append(conn.state + ",");
        writer.append(conn.history + ",");
        writer.append(Integer.toString(conn.urgentNum) + ",");
        writer.append(Integer.toString(conn.sameHostCount) + ",");
        writer.append(Integer.toString(conn.sameSrvCount) + ",");
        writer.append(Double.toString(conn.sameHostRErrorRate) + ",");
        writer.append(Double.toString(conn.sameHostSErrorRate) + ",");
        writer.append(Double.toString(conn.sameHostSameSrvRate) + ",");
        writer.append(Double.toString(conn.sameSrvRErrorRate) + ",");
        writer.append(Double.toString(conn.sameSrvSErrorRate) + ",");
        writer.append(Double.toString(conn.sameSrvDiffHostRate) + ",");
        writer.append(Integer.toString(conn.nFileAcc) + ",");
        writer.append(Integer.toString(conn.nFTPCmd) + ",");
        writer.append(Integer.toString(conn.imageAcc) + ",");
        writer.append(Integer.toString(conn.textAcc) + ",");
        writer.append(Integer.toString(conn.appAcc) + ",");
        writer.append(Integer.toString(conn.binAcc) + ",");
        
        writer.append(conn.label);
        writer.append('\n');
      }
      writer.flush();
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
