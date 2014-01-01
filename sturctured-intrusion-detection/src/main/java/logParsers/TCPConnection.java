package logParsers;

public class TCPConnection {
  public String addressA;

  public String addressB;

  public String portA;

  public String portB;

  public int packetNum;

  public int byteNum;

  public int packetA2B;

  public int packetB2A;

  public int byteA2B;

  public int byteB2A;

  public float relStart;

  public float duration;
  
  public TCPConnection(String[] elements) {
    try {
//      System.out.println(inputLine.replace("\"",""));
      this.addressA = elements[0].replace("\"", "");
      this.portA = elements[1];
      this.addressB = elements[2];
      this.portB = elements[3];
      this.packetNum = Integer.parseInt(elements[4]);
      this.byteNum= Integer.parseInt(elements[5]);
      this.packetA2B = Integer.parseInt(elements[6]);
      this.packetB2A = Integer.parseInt(elements[7]);
      this.byteA2B = Integer.parseInt(elements[8]);
      this.byteB2A = Integer.parseInt(elements[9]);
      this.relStart = Float.parseFloat(elements[10]);
      this.duration = Float.parseFloat(elements[11]);
    } catch(Exception e) {
      e.printStackTrace();
    }
    
  }
}
