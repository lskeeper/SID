package logParsers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CSVFileReader {
  public String csvFile;

  public String splitter;

  public CSVFileReader(String csvFileName, String splitter) {
    this.csvFile = csvFileName;
    this.splitter = splitter;
  }

  public List<String[]> parseCSV() {
    List<String[]> result = new ArrayList<String[]>();
    BufferedReader br = null;
    String line = "";
    try {
      br = new BufferedReader(new FileReader(this.csvFile));
      while ((line = br.readLine()) != null) {
        String[] elements = line.trim().split(this.splitter);
        result.add(elements);
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

  public HashMap<String, String> readIpKeyMap(List<String[]> lines) {
    HashMap<String, String> ipMap = new HashMap<String, String>();
    for (String[] elements : lines) {
      try {
        ipMap.put(elements[0], elements[1]);
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
    return ipMap;
  }
  
  public List<TCPConnection> getConnectionList(List<String[]> lines){
    List<TCPConnection> connectionList = new ArrayList<TCPConnection>();
    for (String[] elements: lines) {
      try {
        TCPConnection connection = new TCPConnection(elements);
        connectionList.add(connection);
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
    return connectionList; 
  }
}
