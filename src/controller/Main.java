package controller;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import model.Department;
import model.Employee;
import model.Model;
import view.View;

public class Main {
  public static void main(String[] args) {
    
    Model model=new Model();        
    for (Employee e : model.getEmployeeList()) {
      System.out.println(e);
    }
    
    for (Department d : model.getDeptList()) {
      System.out.println(d);
    }
    
    
//    try {
//      Method toDo = Employee.class.getDeclaredMethod("raiseSalary", Employee.class);    
//      System.out.println(model.pt7i(toDo));
//      System.out.println(model.pt7r(toDo));
//    } catch (Exception e) {}
//    
//    Application.launch(View.class,args);
    
    
  }
}
