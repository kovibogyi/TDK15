/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.*;

/**
 *
 * @author don_felegyhazy
 */
public class View extends Application {

  final boolean FULLSCREEN = false;
  final String TITLE = "title";
  
  ObservableList<Tab> tabs = FXCollections.observableArrayList();
  
  Rectangle bgRect = new Rectangle();
  
  
  
  TabPane center = new TabPane();
  
  Group main = new Group(bgRect,center);
  BorderPane root = new BorderPane(main);
  Scene scene = new Scene(root, 600, 600);
    
  Stage stageRef;
  
//debug
  final Model model = new Model();

//  public View(Model model) {
//    super();
//    this.model = model;
//  }

  
  
  
  // Debug-hoz
  public static void main(String[] args) {
    launch(args);
  }    
  
  @Override
  public void start(Stage primaryStage) throws Exception {
    stageRef = primaryStage;
    stageRef.setTitle(TITLE);
    
    bgRect.widthProperty().bind(scene.widthProperty());
    bgRect.heightProperty().bind(scene.heightProperty());
    bgRect.setFill(Color.TRANSPARENT);
    
    initTabs();
    
    
    stageRef.setScene(scene);
    stageRef.setFullScreen(FULLSCREEN);
    stageRef.show();
  }
  


  private void initTabs() {
    center.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    tabs.addListener((ListChangeListener.Change<? extends Tab> c) -> {      
      if (c.next()) {
        for (Tab tab : c.getAddedSubList()) 
          center.getTabs().add(tab);
        for (Tab tab : c.getRemoved()) 
          center.getTabs().remove(tab);
      }          
    });
    
    
    ListView<Employee> lvEmps = new ListView<>(model.getEmployeeList());            
    
    
    center.prefWidthProperty().bind(scene.widthProperty());
    Tab tab1 = new Tab("Tab1",lvEmps);       
    
        
    int lines = 12;
    
    Text [][] texts = new Text[2][lines];
    GridPane gp = new GridPane();
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < lines; j++) {
        texts[i][j] = new Text();
        
        gp.add(texts[i][j], i, j);
      }
      
    }
    
    texts[0][0].setText("megszamol i");
    texts[1][0].setText("" + model.pt1i());
    
    texts[0][1].setText("megszamol r");
    texts[1][1].setText("" + model.pt1r());
    
    texts[0][2].setText("eldont i");
    texts[1][2].setText("" + model.pt2i(150000));
    
    texts[0][3].setText("eldont r");
    texts[1][3].setText("" + model.pt2r(100000));
    
    texts[0][4].setText("kivalaszt i");
    texts[1][4].setText("" + model.pt3i(4800));
    
    texts[0][5].setText("kivalaszt r");
    texts[1][5].setText("" + model.pt3r(4800));
    
    texts[0][6].setText("kereses i");
    texts[1][6].setText(""+model.pt4i(100));
    
    texts[0][7].setText("kereses r");
    texts[1][7].setText(""+model.pt4r(100));
    
    texts[0][8].setText("megszamolas i");
    texts[1][8].setText(""+model.pt5i(100));
    
    texts[0][9].setText("megszamolas r");
    texts[1][9].setText(""+model.pt5r(100));
    
    texts[0][10].setText("max kiv i");
    texts[1][10].setText("" + model.pt6i());
    
    texts[0][11].setText("max kiv r");
    texts[1][11].setText("" + model.pt6r());    
    
    Tab tab2 = new Tab("Tab2",gp);
    
    TilePane tp = new TilePane();
    Tab tab3 = new Tab("Tab3",tp);
    try {
      ListView lvEmpsRSI = new ListView(model.pt7i(Employee.class.getDeclaredMethod("raiseSalary", Employee.class))); //listview empls raised salary iterative
      ListView lvEmpsRSR = new ListView(model.pt7i(Employee.class.getDeclaredMethod("raiseSalary", Employee.class))); // -||-                        recursive   
      tp.getChildren().addAll(lvEmpsRSI,lvEmpsRSR);
      
    } catch (Exception e) {}
    
    
    
    ListView<Employee> lvEmpSelI = new ListView<>(model.pt8i(100));
    ListView<Employee> lvEmpSelR = new ListView<>(model.pt8r(100));
    TilePane tp2 = new TilePane(lvEmpSelI,lvEmpSelR);
    Tab tab4 = new Tab("Tab4",tp2);
    
    int avg = model.pt1i()/model.getEmployeeList().size();
    
    EmpListTuple el1 = model.pt9i(avg);
    EmpListTuple el2 = model.pt9r(avg);        
    
    GridPane gp2 = new GridPane();
    
    ListView<Employee> lvEmppt9i1 = new ListView<>(el1.getList1O());
    ListView<Employee> lvEmppt9i2 = new ListView<>(el1.getList2O());
    
    lvEmppt9i1.prefHeightProperty().bind(gp2.heightProperty().divide(2.0D));
    lvEmppt9i2.prefHeightProperty().bind(gp2.heightProperty().divide(2.0D));
    
    ListView<Employee> lvEmppt9r1 = new ListView<>(el2.getList1O());
    ListView<Employee> lvEmppt9r2 = new ListView<>(el2.getList2O());
    
    lvEmppt9r1.prefHeightProperty().bind(gp2.heightProperty().divide(2.0D));
    lvEmppt9r2.prefHeightProperty().bind(gp2.heightProperty().divide(2.0D));
                
    gp2.add(lvEmppt9i1, 0, 0);
    gp2.add(lvEmppt9i2, 0, 1);
    gp2.add(lvEmppt9r1, 1, 0);
    gp2.add(lvEmppt9r2, 1, 1);        
    
    Tab tab5 = new Tab("Tab5",gp2);
    
    //8-as es 9-es tetellel kepzett listak metszete (10-es)
    ListView lvAvgAlattEsShippingI = new ListView(model.pt10i(model.pt8i(model.getDeptsAndIDs().get("Shipping")),el1.getList1O()));
    ListView lvAvgAlattEsShippingR = new ListView(model.pt10r(model.pt8r(model.getDeptsAndIDs().get("Shipping")),el2.getList1O()));
    TilePane tp3 = new TilePane(lvAvgAlattEsShippingI,lvAvgAlattEsShippingR);
    
    Tab tab6 = new Tab("Tab6",tp3);
    
    center.getTabs().addAll(tab1,tab2,tab3,tab4,tab5,tab6);
  }
  
}
