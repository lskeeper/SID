package logParsers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TCPConnectionParser {
  public String csvFile;

  public TCPConnectionParser(String csvFileName) {
    this.csvFile = csvFileName;
  }

  public List<TCPConnection> parse() {
    List<TCPConnection> result = new ArrayList<TCPConnection>();
    BufferedReader br = null;
    String line = "";
    try {
      br = new BufferedReader(new FileReader(this.csvFile));
      while ((line = br.readLine()) != null) {
        TCPConnection connection = new TCPConnection(line);
        result.add(connection);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return result;
  }
}
