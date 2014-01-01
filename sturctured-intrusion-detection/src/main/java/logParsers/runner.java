package logParsers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class runner {
  public static void main(String[] args) {
//    CSVFileReader fileReader = new CSVFileReader("data/bprd-7315-conversations.csv", "\",\"");
    CSVFileReader fileReader = new CSVFileReader("data/ip-key.csv", ",");
    List<String[]> lines = fileReader.parseCSV();
//    List<TCPConnection> result = fileReader.getConnectionList(lines);
//    System.out.println(result.get(0).addressA);
//    System.out.println(result.get(0).duration);
    HashMap<String, String> ipMap = fileReader.readIpKeyMap(lines);
    for (Map.Entry<String, String> entry: ipMap.entrySet()) {
      System.out.println(entry.getKey() + "\t" + entry.getValue());
    }
  }
}
