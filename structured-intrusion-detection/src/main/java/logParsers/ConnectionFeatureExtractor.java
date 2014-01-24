package logParsers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import types.FTPCommand;
import types.FileAccess;
import types.Packet;
import types.TCPConnection;

public class ConnectionFeatureExtractor {
  public TCPConnection conn;

  public List<TCPConnection> allConns;

  public Connection sqlCon;

  public HashMap<String, TCPConnection> connMap;
  
  public static final List<String> SYN_ERROR_Flags = Arrays.asList(new String[] { "RSTOS0",
      "RSTRH", "SH", "SHR", "OTH" });

  public static final List<String> MIME_TYPES = Arrays.asList(new String[] { "image", "text",
      "application", "binary" });

  public ConnectionFeatureExtractor(List<TCPConnection> connList, Connection sqlCon) {
    this.allConns = connList;
    this.sqlCon = sqlCon;
    this.connMap = new HashMap<String, TCPConnection>();
  }

  public void getTrafficBasedFeatures() throws SQLException {
    long t1 = System.nanoTime();
    for (int i = 0; i < this.allConns.size(); i++) {
      
      TCPConnection targetConn = this.allConns.get(i);
      String targetUID = targetConn.uid;
      this.connMap.put(targetUID, targetConn);

      targetConn.getCoveredPackets(sqlCon);
      List<TCPConnection> connWindow = getConnectionWindow(this.allConns, targetConn, i);
      getUrgentCount(targetConn);
      getSameHostFeatures(targetConn, connWindow);
      getSameServiceFeatures(targetConn, connWindow);
    }
    long t2 = System.nanoTime();
    System.out.println((t2 - t1) * 1E-9);
  }

  private void getUrgentCount(TCPConnection targetConn) {
    int count = 0;
    for (Packet packet : targetConn.coveredPackets) {
      if (packet.urgent) {
        count++;
      }
    }
    targetConn.urgentNum = count;
  }

  private void getSameHostFeatures(TCPConnection targetConn, List<TCPConnection> connWindow) {
    int hostIP = targetConn.destIP;
    String service = targetConn.service;
    int count = 0;
    int sErrorCount = 0;
    int rErrorCount = 0;
    int sameSrvCount = 0;
    for (TCPConnection prevConn : connWindow) {
      if (prevConn.destIP == hostIP) {
        count++;
        if (prevConn.service.equals(service)) {
          sameSrvCount++;
        }

        if (SYN_ERROR_Flags.contains(prevConn.state)) {
          sErrorCount++;
        }
        if ("REJ".equals(prevConn.state)) {
          rErrorCount++;
        }
      }
    }
    targetConn.sameHostCount = count;
    if (count == 0) {
      targetConn.sameHostSameSrvRate = 0;
      targetConn.sameHostSErrorRate = 0;
      targetConn.sameHostRErrorRate = 0;
    } else {
      targetConn.sameHostSameSrvRate = (double) sameSrvCount / count;
      targetConn.sameHostSErrorRate = (double) sErrorCount / count;
      targetConn.sameHostRErrorRate = (double) rErrorCount / count;
    }

  }

  private void getSameServiceFeatures(TCPConnection targetConn, List<TCPConnection> connWindow) {
    String service = targetConn.service;
    int hostIP = targetConn.destIP;
    int count = 0;
    int sErrorCount = 0;
    int rErrorCount = 0;
    int diffHostCount = 0;
    for (TCPConnection prevConn : connWindow) {
      if (prevConn.service.equals(service)) {
        count++;
        if (prevConn.destIP != hostIP) {
          diffHostCount++;
        }
        if (SYN_ERROR_Flags.contains(prevConn.state)) {
          sErrorCount++;
        }
        if ("REJ".equals(prevConn.state)) {
          rErrorCount++;
        }
      }
    }
    targetConn.sameSrvCount = count;
    if (count == 0) {
      targetConn.sameSrvDiffHostRate = 0;
      targetConn.sameSrvSErrorRate = 0;
      targetConn.sameSrvRErrorRate = 0;
    } else {
      targetConn.sameSrvDiffHostRate = (double) diffHostCount / count;
      targetConn.sameSrvSErrorRate = (double) sErrorCount / count;
      targetConn.sameSrvRErrorRate = (double) rErrorCount / count;
    }
  }

  private static List<TCPConnection> getConnectionWindow(List<TCPConnection> allConns,
          TCPConnection conn, int index) {
    double startTime = conn.startTime;
    List<TCPConnection> result = new ArrayList<TCPConnection>();
    for (int i = 0; i < index; i++) {
      if (allConns.get(i).startTime >= startTime - 2) {
        result.add(allConns.get(i));
      }
    }
    return result;
  }

  public void getContentBasedFeatures(List<FileAccess> fileAccList, List<FTPCommand> ftpCmdList) {
    for (FileAccess fileAcc: fileAccList){
      String connUID = fileAcc.conn_uid;
      String mimeType = fileAcc.mime_type;
      TCPConnection targetConn = this.connMap.get(connUID);
      targetConn.nFileAcc++;
      if (mimeType.equals("image")){
        targetConn.imageAcc++;
      }
      if (mimeType.equals("text")){
        targetConn.textAcc++;
      }
      if (mimeType.equals("application")){
        targetConn.appAcc++;
      }
      if (mimeType.equals("binary")){
        targetConn.binAcc++;
      }
    }
    for (FTPCommand ftpCmd: ftpCmdList){
      String connUID = ftpCmd.conn_uid;
      TCPConnection targetConn = this.connMap.get(connUID);
      targetConn.nFTPCmd++;
    }
  }


}
