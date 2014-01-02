package logParsers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import types.Packet;
import types.TCPConnection;

public class CSVFileReader {
  public String csvFile;

  public String splitter;

  public Double startTime;

  public String network;

  public List<String[]> lines;

  public CSVFileReader(String csvFileName, String splitter, Double startTime, String network) {
    this.csvFile = csvFileName;
    this.splitter = splitter;
    this.startTime = startTime;
    this.network = network;
    this.parseCSV();
  }

  public void parseCSV() {
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
    this.lines = result;
    String[] firstLine = result.get(0);
    if (firstLine.length == 7) {
      this.startTime = Double.parseDouble(firstLine[1]);
    }

  }

  public HashMap<String, String> readIpKeyMap(List<String[]> lines) {
    HashMap<String, String> ipMap = new HashMap<String, String>();
    for (String[] elements : lines) {
      try {
        if (elements.length == 2) {
          ipMap.put(elements[0], elements[1]);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return ipMap;
  }

  public List<TCPConnection> getConnectionList() {
    List<TCPConnection> connectionList = new ArrayList<TCPConnection>();
    for (String[] elements : this.lines) {
      try {
        TCPConnection connection = new TCPConnection(elements, this.startTime, this.network);
        connectionList.add(connection);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return connectionList;
  }

}
