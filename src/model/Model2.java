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

public class Model2 implements Connection {
  private ArrayList<Employee> empList=new ArrayList<>();
  private ArrayList<Department> depList=new ArrayList<>();
  private java.sql.Connection connection=null;
  private File empFile=new File("./data/employees.csv");
  private File depFile=new File("./data/departments.csv");
  
  public Model2() {
    open();
    employeesWrite();       //Kaczuré
    departmentsWrite();   //Ákosé
    close();
    empList=employeesRead();
   // depList=departmentsRead();

    //x();      //1000x meghívja az iteratívot
    //PT        
    //pt1();    //PT1 iteratív
    pt1r();     //PT1 rekurzív
  }
  //Töbszöri futtatás (PT1 iteratív
  private void x() {
    long startTime=System.currentTimeMillis();
    for (int i = 1; i <= 1000; i++) {
      pt1i();
      Collections.shuffle(empList);
    }
    long endTime=System.currentTimeMillis();
    System.out.println("Elapsed time: "+(endTime-startTime));
  }
  //PT1 rekurzív meghívó
  private void pt1r() {
    int sumSalary=pt11r(empList.size()-1);
    int countEmp=empList.size();
    double avgSalary=(double)sumSalary/countEmp;    
    System.out.println(avgSalary);
  }
  //PT1 rekurzív 
  private int pt11r(int n) {
    if(n==0)
      return empList.get(0).getEmpSalary();
    return empList.get(n).getEmpSalary()+pt11r(n-1);
  }
  //PT1 iteratív
  private void pt1i() {
    int sumSalary=0, countEmp=0;
    for (Employee emp : empList) {
      sumSalary+=emp.getEmpSalary();
      countEmp++;
    }
    double avgSalary=(double)sumSalary/countEmp;    
    System.out.println(avgSalary);
  }
  //Employees beolvasás fájlból
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
  //Fájl megnyit
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
  //Lekérdezés
  @Override
  public ResultSet query(String sql) {
    ResultSet rs=null;
    try {
      rs=connection.createStatement().executeQuery(sql);
    }
    catch (SQLException e) {
        System.out.println("Hiba");
    }
    return rs;
  }
  //Bezárás
  @Override
  public void close() {
    if(connection!=null) 
      try {
        connection.close();
      }
      catch (SQLException e) {
        
      }
  }
  //Employees fájlba írása
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
//      BufferedWriter bwEmp=new BufferedWriter(new FileWriter(empFile));
//      bwEmp.write(Employee.getIdentifiers());
//      bwEmp.newLine();
//      for (Employee emp : empList) {
//        bwEmp.write(emp.toString());
//        bwEmp.newLine();
//      }              
//      bwEmp.close();
    }
    catch(Exception e) {
      
    }
  }
  
  
  //Departmen beolvasása Ákos------------
  
  
    //Departments fájlba írás
   private void departmentsWrite() {
    ResultSet rsDep=query(SQLDEPARTMENT);
    try {
      while(rsDep.next()) {
        int depId=rsDep.getInt("depId");
        String depName=rsDep.getString("depName");
        int depManagerId=rsDep.getInt("depManagerId");       
        depList.add(new Department(depId, depName, depManagerId));
      }
        System.out.println(depList);
        
      BufferedWriter bwDep=new BufferedWriter(new FileWriter(depFile));
      bwDep.write(Department.getIdentifiers());
      bwDep.newLine();
      for (Department dep : depList) {
        bwDep.write(dep.toString());
        bwDep.newLine();
      }              
      bwDep.close();
    }
    catch(Exception e) {
      
    }
//  
//  
//  
  }
  
  //Departmen beolvasása Ákos VÉGE-----------
  public static void main(String[] args) {
    Model2 model = new Model2();
    for (Department d : model.depList)
      System.out.println(d);
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