package logParsers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import types.FTPCommand;
import types.FileAccess;
import types.TCPConnection;

public class CSVFileReader {
  public String csvFile;

  public String splitter;

  public Double startTime;

  public String network;

  public List<String[]> lines;

  public CSVFileReader(String csvFileName, String splitter, String network) {
    this.csvFile = csvFileName;
    this.splitter = splitter;
    if (null != network) {
      this.network = network;
    }
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
  }

  public List<TCPConnection> getConnectionList() {
    List<TCPConnection> connectionList = new ArrayList<TCPConnection>();
    for (String[] elements : this.lines) {
      if (elements[0].startsWith("#") || elements[8].equals("-")) {
        continue;
      } else {
        try {
          TCPConnection connection = new TCPConnection(elements, this.network);
          connectionList.add(connection);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return connectionList;
  }

  public List<FileAccess> getFileAccessList() {
    List<FileAccess> result = new ArrayList<FileAccess>();
    for (String[] elements : this.lines) {
      if (elements[0].startsWith("#")) {
        continue;
      } else {
        try {
          FileAccess fileAccess = new FileAccess(elements);
          result.add(fileAccess);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return result;
  }

  public List<FTPCommand> getFTPCmdList() {
    List<FTPCommand> result = new ArrayList<FTPCommand>();
    for (String[] elements : this.lines) {
      if (elements[0].startsWith("#")) {
        continue;
      } else {
        try {
          FTPCommand ftpCmd = new FTPCommand(elements);
          result.add(ftpCmd);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return result;
  }
}
