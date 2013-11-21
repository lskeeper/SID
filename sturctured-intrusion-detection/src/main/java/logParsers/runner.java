package logParsers;

import java.util.List;

public class runner {
  public static void main(String[] args) {
    TCPConnectionParser obj = new TCPConnectionParser("data/bprd-7315-conversations.csv");
    List<TCPConnection> result = obj.parse();
    System.out.println(result.get(0).addressA);
    System.out.println(result.get(0).duration);
  }
}
