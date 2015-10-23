package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class Model implements Connection {
  private ArrayList<Employee> empList=new ArrayList<>();
  private ArrayList<Department> depList=new ArrayList<>();
  private java.sql.Connection connection=null;
  private File empFile=new File("./data/employees.csv");
  
  public Model() {
    //open();
    //employeesWrite();
    //close();
    empList=employeesRead();

    //x();
    //PT
    //pt1();
    pt1r();
  }
  
  private void x() {
    long startTime=System.currentTimeMillis();
    for (int i = 1; i <= 1000; i++) {
      pt1i();
      Collections.shuffle(empList);
    }
    long endTime=System.currentTimeMillis();
    System.out.println("Elapsed time: "+(endTime-startTime));
  }
  
  private void pt1r() {
    int sumSalary=pt11r(empList.size()-1);
    int countEmp=empList.size();
    double avgSalary=(double)sumSalary/countEmp;    
    System.out.println(avgSalary);
  }
  
  private int pt11r(int n) {
    if(n==0)
      return empList.get(0).getEmpSalary();
    return empList.get(n).getEmpSalary()+pt11r(n-1);
  }
  
  private void pt1i() {
    int sumSalary=0, countEmp=0;
    for (Employee emp : empList) {
      sumSalary+=emp.getEmpSalary();
      countEmp++;
    }
    double avgSalary=(double)sumSalary/countEmp;    
    System.out.println(avgSalary);
  }
  
  private ArrayList<Employee> employeesRead() {
    ArrayList<Employee> empList=new ArrayList<>();
    try {
      BufferedReader brEmp=new BufferedReader(new FileReader(empFile));
      brEmp.readLine();
      String line="";
      while((line=brEmp.readLine())!=null) {
        String[] splittedLine=line.split(";");
        int empId=Integer.parseInt(splittedLine[0]);
        String empName=splittedLine[1];
        int empSalary=Integer.parseInt(splittedLine[2]);
        int depId=Integer.parseInt(splittedLine[3]);
        int managerId=Integer.parseInt(splittedLine[4]);
        empList.add(new Employee(empId, empName, empSalary, depId, managerId));
      }
      brEmp.close();
    }
    catch (Exception e) {
    }
    
    return empList;
  }

  @Override
  public void open() {
    try {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      connection=DriverManager.getConnection(URL, USER, PASSWORD);
    }
    catch(ClassNotFoundException e) {
      
    }
    catch(SQLException e) {
      
    }
  }

  @Override
  public ResultSet query(String sql) {
    ResultSet rs=null;
    try {
      rs=connection.createStatement().executeQuery(sql);
    }
    catch (SQLException e) {
      
    }
    return rs;
  }

  @Override
  public void close() {
    if(connection!=null) 
      try {
        connection.close();
      }
      catch (SQLException e) {
        
      }
  }

  private void employeesWrite() {
    ResultSet rsEmp=query(SQLEMPLOYEES);
    try {
      while(rsEmp.next()) {
        int empId=rsEmp.getInt("empId");
        String empName=rsEmp.getString("empName");
        int empSalary=rsEmp.getInt("empSalary");
        int depId=rsEmp.getInt("depId");
        int managerId=rsEmp.getInt("managerId");
        empList.add(new Employee(empId, empName, empSalary, depId, managerId));
      }
      BufferedWriter bwEmp=new BufferedWriter(new FileWriter(empFile));
      bwEmp.write(Employee.getIdentifiers());
      bwEmp.newLine();
      for (Employee emp : empList) {
        bwEmp.write(emp.toString());
        bwEmp.newLine();
      }              
      bwEmp.close();
    }
    catch(Exception e) {
      
    }
  }
  
  
}


/*
    String url="jdbc:oracle:thin:@localhost:1521:xe";
    String user="HR";
    String password="hr";
    Class.forName("oracle.jdbc.driver.OracleDriver");
    Connection kapcsolat=
      DriverManager.getConnection(url, user, password);
    System.out.print("Dolgozók átlagfizetése: ");
    Statement stat=kapcsolat.createStatement();
    ResultSet rs=
      stat.executeQuery(
        "SELECT AVG(SALARY) FROM EMPLOYEES");
    rs.next();
    System.out.println(rs.getString(1));
    kapcsolat.close();

*/