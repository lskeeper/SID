package types;

public class FileAccess {
  public double timeStamp;

  public String fuid;

  public String conn_uid;

  public String source;

  public int depth;

  public String mime_type;

  public FileAccess(String[] elements) {
    try {
      this.timeStamp = Double.parseDouble(elements[0]);
      this.fuid = elements[1];
      this.conn_uid = elements[4];
      this.source = elements[5];
      this.depth = Integer.parseInt(elements[6]);
      this.mime_type = elements[8].split("/")[0];

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
