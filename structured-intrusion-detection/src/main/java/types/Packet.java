package types;

import java.sql.ResultSet;

public class Packet {
  public double raw_time;

  public int source_ip;

  public int dest_ip;

  public int source_port;
  
  public int dest_port;

  public String network;

  public boolean syn;

  public boolean fin;

  public boolean reset;

  public boolean push;

  public boolean urgent;

  public boolean ack;

  public Packet(ResultSet tcpdumpRow) {
    try {
      this.raw_time = tcpdumpRow.getDouble("raw_time");
      this.source_ip = tcpdumpRow.getInt("source_ip");
      this.dest_ip = tcpdumpRow.getInt("dest_ip");
      this.source_port = tcpdumpRow.getInt("source_port");
      this.dest_port = tcpdumpRow.getInt("dest_port");
      this.network = tcpdumpRow.getString("network");
      this.fin = tcpdumpRow.getBoolean("fin");
      this.syn = tcpdumpRow.getBoolean("syn");
      this.reset = tcpdumpRow.getBoolean("reset");
      this.push = tcpdumpRow.getBoolean("push");
      this.urgent = tcpdumpRow.getBoolean("urgent");
      this.ack = tcpdumpRow.getBoolean("ack");
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
