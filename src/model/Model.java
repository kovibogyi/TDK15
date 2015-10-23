package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model implements Connection {
  private ArrayList<Employee> empList=new ArrayList<>();
  private ArrayList<Department> depList=new ArrayList<>();
  private java.sql.Connection connection=null;
  private File empFile=new File("./data/employees.csv");
  private File depFile=new File("./data/departments.csv");
  private Map<String,Integer> deptsAndIDs = new HashMap<>();
  
  public Model() {
    open();
    employeesWrite();       //Kaczuré
    departmentsWrite();   //Ákosé
    close();
    
    empList=employeesRead();
    depList=departmentsRead();
    
    for (Department d : depList)
      deptsAndIDs.put(d.getDepName(), d.getDepId());

    //x();      //1000x meghívja az iteratívot
    //PT        
    //pt1();    //PT1 iteratív
//    pt1r();     //PT1 rekurzív
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
  
//  private void pt1r() {
//    int sumSalary=pt11r(empList.size()-1);
//    int countEmp=empList.size();
//    double avgSalary=(double)sumSalary/countEmp;    
//    System.out.println(avgSalary);
//  }
//  
//  private int pt11r(int n) {
//    if(n==0)
//      return empList.get(0).getEmpSalary();
//    return empList.get(n).getEmpSalary()+pt11r(n-1);
//  }
  
//  private void pt1i() {
//    int sumSalary=0, countEmp=0;
//    for (Employee emp : empList) {
//      sumSalary+=emp.getEmpSalary();
//      countEmp++;
//    }
//    double avgSalary=(double)sumSalary/countEmp;    
//    System.out.println(avgSalary);
//  }
  
  
  
  
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
        bwEmp.write(emp.toCSVString());
        bwEmp.newLine();
      }              
      bwEmp.close();
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
  }
   
    //Fájlból beolvasás
    private ArrayList<Department> departmentsRead() {
    ArrayList<Department> depList=new ArrayList<>();
    try {
      BufferedReader brDep=new BufferedReader(new FileReader(depFile));
      brDep.readLine();
      String line="";
      while((line=brDep.readLine())!=null) {
        String[] splittedLine=line.split(";");
        int depId=Integer.parseInt(splittedLine[0]);
        String depName=splittedLine[1];        
        int depManagerId=Integer.parseInt(splittedLine[2]);
        depList.add(new Department(depId, depName, depManagerId));
      }
      brDep.close();
    }
    catch (Exception e) {
    }
    
    return depList;
  }  
  
  public ObservableList<Employee> getEmployeeList() {
    return FXCollections.observableArrayList(empList);
  }
  
  public ObservableList<Department> getDeptList() {
    return FXCollections.observableArrayList(depList);
  }

  public Map<String, Integer> getDeptsAndIDs() {
    return deptsAndIDs;
  }
  
  
  
  /**
   * Összes fizetés megszámolása
   * @return 
   */
  
  public int pt1i() {
    int sum = 0;
    for (Employee emp : empList) 
      sum += emp.getEmpSalary();
    return sum;
  }

   /**
   * Összes fizetés megszámolása
   * @return 
   */  
  public int pt1r() {    
    return pt1r(0);
  }
  
  private int pt1r(int n) {    
    return n==empList.size()-1 ? 
            empList.get(n).getEmpSalary() : empList.get(n).getEmpSalary()+pt1r(n+1);
  }
  
  
  /**
   * eldöntés: van e fizetés > x?
   * 
   * @param x
   * @return 
   */
  
  public boolean pt2i(int x) {    
    int i = 0;    
    while (i<empList.size() && empList.get(i).getEmpSalary()<x) 
      i++;      
    return i<empList.size();
  }
  /**
   * eldöntés: van e fizetés > x?
   * 
   * @param x
   * @return 
   */  
  public boolean pt2r(int x) {
    return pt2r(x,0);
  }
  
  public boolean pt2r(int x, int n) {
    return n!=empList.size() ? 
            (empList.get(n).getEmpSalary() > x ? true : pt2r(x,n+1) ) 
            : false;
  }  
   
  /**
   * kiválasztás: ki keres x-et?
   * 
   * @param x
   * @return 
   */
  public Employee pt3i(int x) {
    int i = 0;
    while (i<empList.size() && empList.get(i).getEmpSalary() != x) 
      i++;
    return i==empList.size() ? null : empList.get(i);
  }  
  
  /**
   * kiválasztás: ki keres x-et?
   * 
   * @param x
   * @return 
   */  
  public Employee pt3r(int x) {
    return pt3r(x,0);        
  }
  
  private Employee pt3r(int x, int n) {
    return n < empList.size() ? 
            empList.get(n).getEmpSalary() == x ? empList.get(n) : pt3r(x,n+1) 
            : null;
  }  
  
  
  
  /**
   * keresés : első alkalmazott, ahol dept code = x
   * 
   * @param x
   * @return 
   */
  public Employee pt4i(int x) {
    int i = 0;
    while (i<empList.size() && empList.get(i).getDepId() != x)
      i++;
    return i<empList.size() ? empList.get(i) : null;
  }
  /**
   * keresés : első alkalmazott, ahol dept code = x
   * 
   * @param x
   * @return 
   */  
  public Employee pt4r(int x) {
    return pt4r(x,0);
  }
  
  private Employee pt4r(int x, int n) {
    return n < empList.size() ? 
            empList.get(n).getDepId() == x ? empList.get(n) : pt4r(x,n+1) 
            : null;    
  }
  
  
  
  /**
   * Megszámolás: ahol depId = x
   * @param x
   * @return 
   */
  public int pt5i(int x) {
    int members = 0;
    for (Employee e : empList)
      if (e.getDepId() == x) members++;
    return members;    
  }

  /**
   * Megszámolás: ahol depId = x
   * @param x
   * @return 
   */  
  public int pt5r(int x) {
    return pt5r(x,0);
  }
  
  private int pt5r(int x, int n) {
    return n==empList.size() ? 0 : (empList.get(n).getDepId() == x ? 1 : 0) + pt5r(x,n+1);
  }
  
  /**
   * 
   * @return legmagasabb fizetes indexe
   */
  public int pt6i() {
    int max = 0;
    for (int i = 1; i < empList.size(); i++) 
      if (empList.get(i).getEmpSalary()>empList.get(max).getEmpSalary()) max = i;
    return max;
  }
  /**
   * 
   * @return legmagasabb fizetes indexe
   */
  
  public int pt6r() {
    return pt6r(0,0);
  }
  
  private int pt6r(int n, int max) {
    return n == empList.size() ? max : (empList.get(n).getEmpSalary() > empList.get(max).getEmpSalary() ? pt6r(n+1,n) : pt6r(n+1,max) );
  }
  

  
  /**
   * le masolja az emplistet es vegrehatja m metodust, minden elemen
   * 
   * @param m a statikus metódus amit minden elemen végrehajt, returnType = Employee.class, args.length = 1 és arg[0].class = Employee.class 
   * @return teljesül e m feltétel
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException 
   */
  
  public ObservableList<Employee> pt7i(Method m) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    if (!m.getReturnType().equals(Employee.class) || 
            m.getParameters().length != 1 || 
            !m.getParameterTypes()[0].equals(Employee.class)) 
      throw new RuntimeException("Hiba");    
    List<Employee> raisedSalaryEmps = new ArrayList<>();
    for (Employee e : empList)
      raisedSalaryEmps.add((Employee)m.invoke(null, e));
    return FXCollections.observableArrayList(raisedSalaryEmps);
  }
  
  /**
   * le masolja az emplistet es vegrehatja m metodust, minden elemen
   * 
   * @param m a statikus metódus amit minden elemen végrehajt, returnType = Employee.class, args.length = 1 és arg[0].class = Employee.class 
   * @return teljesül e m feltétel
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException 
   */  
  public ObservableList<Employee> pt7r(Method m) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    if (!m.getReturnType().equals(Employee.class) || 
            m.getParameters().length != 1 || 
            !m.getParameterTypes()[0].equals(Employee.class)) 
      throw new RuntimeException("Hiba");    
    List<Employee> raisedSalaryEmps = new ArrayList<>();
    return pt7r(m,0,raisedSalaryEmps);
    
  }

  private ObservableList<Employee> pt7r(Method m, int n, List <Employee> raisedSalaryEmps) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    if (n==empList.size()) return FXCollections.observableArrayList(raisedSalaryEmps);
    Employee emp = (Employee)m.invoke(null, empList.get(n));
    raisedSalaryEmps.add(emp);
    return pt7r(m,n+1,raisedSalaryEmps);    
  }
  
  /**
   * kivalogatas: x dept koduak uj listaba
   * 
   * @param x dept kod
   */
  public ObservableList<Employee> pt8i(int x) {
    List <Employee> deptEmps = new ArrayList<>();
    for (Employee e : empList) 
      if (e.getDepId() == x) deptEmps.add(e);
    return FXCollections.observableArrayList(deptEmps);    
  }
  
/**
   * kivalogatas: x dept koduak uj listaba
   * 
   * @param x dept kod
   */
  
  public ObservableList<Employee> pt8r(int x) {
    List <Employee> deptEmps = new ArrayList<>();
    return pt8r(x,0,deptEmps);
  }

  private ObservableList<Employee> pt8r(int x, int n, List<Employee> deptEmps) {
    if (n == empList.size()) return FXCollections.observableArrayList(deptEmps);
    if (empList.get(n).getDepId() == x) deptEmps.add(empList.get(n));
    return pt8r(x,n+1,deptEmps);
  }
  
  /**
   * szétválogatás: pivot fizetés alattiakat és felettieket külön
   * @param x pivot
   * @return EmplistTuple (list1 - alatta, list2 felette)
   */
  
  public EmpListTuple pt9i(int x) {
    ArrayList<Employee> 
            list1 = new ArrayList<>(),
            list2 = new ArrayList<>();
    for (Employee e : empList) 
      if (e.getEmpSalary() <= x) list1.add(e);
      else list2.add(e);      
    return new EmpListTuple(list1, list2);          
  }
  
  /**
   * szétválogatás: pivot fizetés alattiakat és felettieket külön
   * @param x pivot
   * @return EmplistTuple (list1 - alatta, list2 felette)
   */
  
  public EmpListTuple pt9r(int x) {
    EmpListTuple elt = new EmpListTuple();
    return pt9r(x,0, elt);
  }

  private EmpListTuple pt9r(int x, int n, EmpListTuple elt) {
    if (n==empList.size()) return elt;
    if (empList.get(n).getEmpSalary() <= x) elt.getList1().add(empList.get(n));
    else elt.getList2().add(empList.get(n));
    return pt9r(x, n+1, elt);
  }
  
  /**
   * metszet
   * 
   * @param list1
   * @param list2
   * @return list1 és 2 metszete
   */
  public ObservableList<Employee> pt10i(ObservableList<Employee> list1, ObservableList<Employee> list2) {
    Set<Employee> temp = new HashSet<>();
    for (int i = 0; i < list1.size(); i++) {
      int j = 0;
      while (j<list2.size() && list1.get(i) != list2.get(j))
        j++;
      if (j<list2.size())
        temp.add(list1.get(i));
    }
    return FXCollections.observableArrayList(temp);    
  }
  
  /**
   * metszet
   * 
   * @param list1
   * @param list2
   * @return list1 és 2 metszete
   */  
  public ObservableList<Employee> pt10r(ObservableList<Employee> list1, ObservableList<Employee> list2) {
    Set<Employee> temp = new HashSet<>();
    return pt10r(list1,list2,0,0,temp);
  }

  /**
   * 
   * @param list1
   * @param list2
   * @param n egyik listában i-nél
   * @param o másikban o-nál tart
   * @param temp a metszet
   * @return 
   */
  private ObservableList<Employee> pt10r(ObservableList<Employee> list1, ObservableList<Employee> list2, int n, int o, Set<Employee> temp) {
    if (n==list1.size()) return FXCollections.observableArrayList(temp);
    if (o==list2.size()) return pt10r(list1,list2,n+1,0,temp);
    if (list1.get(n).equals(list2.get(o))) {
      temp.add(list1.get(n));
      return pt10r(list1,list2,n+1,0,temp);
    } else return pt10r(list1,list2,n,o+1,temp);
    
    
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