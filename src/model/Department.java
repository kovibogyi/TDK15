package model;

public class Department {
  private int depId;
  private String depName;
  private int depManagerId;

  public Department(int depId, String depName, int depManagerId) {
    this.depId = depId;
    this.depName = depName;
    this.depManagerId = depManagerId;
  }
  
  
  public static String getIdentifiers() {return "Meh!";}
  
}
