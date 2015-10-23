/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author don_felegyhazy
 */
public class EmpListTuple {
  final ArrayList<Employee> list1,list2;

  public EmpListTuple() {
    list1 = new ArrayList<>();
    list2 = new ArrayList<>();
  }

  
  
  public EmpListTuple(ArrayList<Employee> list1, ArrayList<Employee> list2) {
    this.list1 = list1;
    this.list2 = list2;
  }

  public ArrayList<Employee> getList1() {
    return list1;
  }

  public ArrayList<Employee> getList2() {
    return list2;
  }
  
  public ObservableList<Employee> getList1O() {
    return FXCollections.observableArrayList(list1);
  }

  public ObservableList<Employee> getList2O() {
    return FXCollections.observableArrayList(list2);
  }
  
}
