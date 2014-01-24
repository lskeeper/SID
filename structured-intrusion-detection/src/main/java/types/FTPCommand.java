package types;

public class FTPCommand {

  public double timeStamp;

  public String conn_uid;

  public String command;

  public String user;

  public FTPCommand(String elements[]) {
    try {
      this.timeStamp = Double.parseDouble(elements[0]);
      this.conn_uid = elements[1];
      this.user = elements[6];
      this.command = elements[8];

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
