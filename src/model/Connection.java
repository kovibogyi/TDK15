package model;

import java.sql.ResultSet;

public interface Connection {
  String URL="jdbc:oracle:thin:@localhost:1521:xe";
  String USER="HR";
  String PASSWORD="hr";
  String DRIVER="oracle.jdbc.driver.OracleDriver";
  String SQLEMPLOYEES=
    "SELECT EMPLOYEE_ID AS empId, FIRST_NAME || ' ' || LAST_NAME AS empName, salary AS empSalary, DEPARTMENT_ID AS depId, MANAGER_ID AS managerId\n" +
    "FROM EMPLOYEES";
  
  
  void open();

  ResultSet query(String sql);
  
  void close();
}
