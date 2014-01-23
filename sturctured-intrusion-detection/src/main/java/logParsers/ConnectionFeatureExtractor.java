package logParsers;

import java.util.Arrays;
import java.util.List;
import types.Packet;
import types.TCPConnection;

public class ConnectionFeatureExtractor {
  public TCPConnection conn;

  public List<TCPConnection> connWindow;

  public static final List<String> SYN_ERROR_Flags = Arrays.asList(new String[] { "RSTOS0",
      "RSTRH", "SH", "SHR", "OTH" });

  public ConnectionFeatureExtractor(TCPConnection conn, List<TCPConnection> connWindow) {
    this.conn = conn;
    this.connWindow = connWindow;
  }

  public void getUrgentCount() {
    int count = 0;
    for (Packet packet : this.conn.coveredPackets) {
      if (packet.urgent) {
        count++;
      }
    }
    conn.urgentNum = count;
  }

  public void getSameHostFeatures() {
    int hostIP = conn.destIP;
    String service = this.conn.service;
    int count = 0;
    int sErrorCount = 0;
    int rErrorCount = 0;
    int sameSrvCount = 0;
    for (TCPConnection prevConn : this.connWindow) {
      if (prevConn.destIP == hostIP) {
        count++;
        if (prevConn.service.equals(service)){
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
    this.conn.sameHostCount = count;
    if (count == 0){
      this.conn.sameHostSameSrvRate = 0;
      this.conn.sameHostSErrorRate = 0;
      this.conn.sameHostRErrorRate = 0;
    } else{
      this.conn.sameHostSameSrvRate = (double) sameSrvCount / count;
      this.conn.sameHostSErrorRate = (double) sErrorCount / count;
      this.conn.sameHostRErrorRate = (double) rErrorCount / count;
    }
    
  }
  
  public void getSameServiceFeatures(){
    String service = this.conn.service;
    int hostIP = this.conn.destIP;
    int count = 0;
    int sErrorCount = 0;
    int rErrorCount = 0;
    int diffHostCount = 0;
    for (TCPConnection prevConn: this.connWindow){
      if (prevConn.service.equals(service)){
        count++;
        if (prevConn.destIP != hostIP){
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
    this.conn.sameSrvCount = count;
    if (count == 0){
      this.conn.sameSrvDiffHostRate = 0;
      this.conn.sameSrvSErrorRate = 0;
      this.conn.sameSrvRErrorRate = 0;
    } else{
      this.conn.sameSrvDiffHostRate = (double) diffHostCount / count;
      this.conn.sameSrvSErrorRate = (double) sErrorCount / count;
      this.conn.sameSrvRErrorRate = (double) rErrorCount / count;
    }
  }

}
