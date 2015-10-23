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

    public int getDepId() {
        return depId;
    }

    public String getDepName() {
        return depName;
    }

    public int getDepManagerId() {
        return depManagerId;
    }
  
  public static String getIdentifiers() {
    return "depId;depName;depManagerId;";
  }
  @Override
  public String toString() {
    return depId+";"+depName+";"+depManagerId;
  }
}
