package model;

public class Employee {
  private int empId;
  private String empName;
  private int empSalary;
  private int depId;
  private int managerId;

  public Employee(int empId, String empName, int empSalary, int depId, int managerId) {
    //tfh. minden OK
    this.empId = empId;
    this.empName = empName;
    this.empSalary = empSalary;
    this.depId = depId;
    this.managerId = managerId;
  }

  public static String getIdentifiers() {
    return "empId;empName;empSalary;depId;managerId";
  }
  
  
  
  public String toString() {
    //return empName;
    return toCSVString();
  }
  
  public String toCSVString() {
    return empId+";"+empName+";"+empSalary+";"+depId+";"+managerId;
  }

  public int getEmpId() {
    return empId;
  }

  public String getEmpName() {
    return empName;
  }

  public int getEmpSalary() {
    return empSalary;
  }

  public int getDepId() {
    return depId;
  }

  public int getManagerId() {
    return managerId;
  }
  
  /**
   * megnöveli a fizetest
   * (masolas tetelhez)
   * 
   * @param e a forrás empl.
   * @return egy új megemelt fiz.-ű dolgozó
   */
  
  public static Employee raiseSalary(Employee e) {
    return new Employee(e.empId, e.empName, (int)(e.empSalary*1.1), e.depId, e.managerId);
  }
  
  
}
