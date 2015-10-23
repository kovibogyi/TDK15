/*
TODO:
elemi PT:
- sorozatszámítás, összegzés
- eldöntés
- kiválasztás
- (lineáris) keresés
- megszámolás
- szélsőérték kiválasztás

összetett PT:
- másolás
- kiválogatás
- szétválogatás
- metszet
- unió
- összefésülés

*/



package adatbazis.adatbazis3;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

interface AdatbazisKapcsolat {
  String URL = "jdbc:oracle:thin:@localhost:1521:xe";
  String USER = "HR";
  String PASSWORD = "hr";
  String DRIVER = "oracle.jdbc.driver.OracleDriver";
}

class Adatbazis implements AdatbazisKapcsolat {
  private static Connection kapcsolat;

  public static void kapcsolatNyit() {
    try {
      Class.forName(DRIVER);
      kapcsolat = DriverManager.getConnection(URL, USER, PASSWORD);
    }
    catch (ClassNotFoundException e) {
      System.out.println("Hiba! Hiányzik a JDBC driver.");
    }
    catch (SQLException e) {
      System.out.println("Hiba! Nem sikerült megnyitni a kapcsolatot az adatbázis-szerverrel.");
    }
  }

  public static void kapcsolatZár() {
    try {
      kapcsolat.close();
    }
    catch (SQLException e) {
      System.out.println("Hiba! Nem sikerült lezárni a kapcsolatot az adatbázis-szerverrel.");
    }
  }

  public static ResultSet lekérdezRészlegDolgozó() {
    ResultSet rs = null;
    try {
      Statement s = kapcsolat.createStatement();
      rs = s.executeQuery(
              "SELECT DEPARTMENT_NAME AS RÉSZLEG, FIRST_NAME || ' ' || LAST_NAME AS NÉV "
              + "FROM DEPARTMENTS, EMPLOYEES "
              + "WHERE EMPLOYEES.DEPARTMENT_ID=DEPARTMENTS.DEPARTMENT_ID "
              + "ORDER BY DEPARTMENT_NAME || FIRST_NAME || LAST_NAME ");
    }
    catch (SQLException e) {
      ;
    }
    return rs;
  }

  public static int lekérdezDolgozóDb() {
    int db=0;
    try {
      Statement s=kapcsolat.createStatement();
      ResultSet rs=s.executeQuery(
              "SELECT COUNT(EMPLOYEE_ID) "+
              "FROM EMPLOYEES");
      rs.next();
      db=rs.getInt(1);
    }
    catch(SQLException e) {
      ;
    }
    return db;
  }

  public static ArrayList<String> lekérdezRészleg() {
    ArrayList<String> lista = new ArrayList<String>();
    try {
      Statement s = kapcsolat.createStatement();
      ResultSet rs = s.executeQuery(
              "SELECT DEPARTMENT_NAME "
              + "FROM DEPARTMENTS "
              + "ORDER BY 1");
      while (rs.next())
        lista.add(rs.getString("DEPARTMENT_NAME"));
    }
    catch (SQLException e) {
      ;
    }
    return lista;
  }

  public static int lekérdezRészlegDolgozóDb(String részleg) {
    int db = 0;
    try {
      PreparedStatement ps = kapcsolat.prepareStatement(
              "SELECT COUNT(EMPLOYEE_ID) "
              + "FROM EMPLOYEES, DEPARTMENTS "
              + "WHERE EMPLOYEES.DEPARTMENT_ID=DEPARTMENTS.DEPARTMENT_ID AND "
              + "DEPARTMENT_NAME=?");
      ps.setString(1, részleg);
      ResultSet rs = ps.executeQuery();
      rs.next();
      db = rs.getInt(1);
    }
    catch (SQLException e) {
      ;
    }
    return db;
  }

  public static ArrayList<String> lekérdezRészlegDolgozó(String részleg) {
    ArrayList<String> lista = new ArrayList<String>();
    try {
      PreparedStatement ps = kapcsolat.prepareStatement(
              "SELECT FIRST_NAME || ' ' || LAST_NAME AS NÉV "
              + "FROM EMPLOYEES, DEPARTMENTS "
              + "WHERE EMPLOYEES.DEPARTMENT_ID=DEPARTMENTS.DEPARTMENT_ID AND DEPARTMENT_NAME=? "
              + "ORDER BY NÉV");
      ps.setString(1, részleg);
      ResultSet rs = ps.executeQuery();
      while (rs.next())
        lista.add(rs.getString("NÉV"));
    }
    catch (SQLException e) {
      ;
    }
    return lista;
  }
}

public class Adatbazis3 extends JFrame implements ActionListener {
  private DefaultListModel dlm1 = new DefaultListModel();
  private DefaultListModel dlm2 = new DefaultListModel();
  private DefaultTreeModel dtm3 = new DefaultTreeModel(null);
  private JTree tFa3 = new JTree(dtm3);
  private DefaultTreeModel dtm4 = new DefaultTreeModel(null);
  private JTree tFa4 = new JTree(dtm4);
  private DefaultTreeModel dtm5 = new DefaultTreeModel(null);
  private JTree tFa5 = new JTree(dtm5);
  private DefaultTreeModel dtm6 = new DefaultTreeModel(null);
  private JTree tFa6 = new JTree(dtm6);
  private JButton btSzűr1 = new JButton("Egyszerű lista");
  private JButton btSzűr2 = new JButton("Összegző lista");
  private JButton btSzűr3 = new JButton("Egyszerű fa");
  private JButton btSzűr4 = new JButton("Összegző fa");
  private JButton btSzűr5 = new JButton("Összegző fa (2)");
  private JButton btSzűr6 = new JButton("Összegző fa (3)");
  
  public Adatbazis3() {
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setTitle("Oracle adatbázis-lekérdező program 3.0");
    setSize(400, 500);
    JTabbedPane tp = new JTabbedPane();
    JPanel pn1=panelGyártó(btSzűr1, new JList(dlm1));
    tp.addTab("Egyszerű lista", pn1);
    tp.addTab("Összegző lista", panelGyártó(btSzűr2, new JList(dlm2)));
    tp.addTab("Egyszerű fa", panelGyártó(btSzűr3, tFa3));
    tp.addTab("Összegző fa (1)", panelGyártó(btSzűr4, tFa4));
    tp.addTab("Összegző fa (2) !!!", panelGyártó(btSzűr5, tFa5));
    tp.addTab("Összegző fa (3)", panelGyártó(btSzűr6, tFa6));
    add(tp);
    setVisible(true);
  }

  private JPanel panelGyártó(JButton bt, JComponent c) {
    JPanel pn = new JPanel(new BorderLayout());
    JPanel pnVezérlő = new JPanel(new FlowLayout());
    pnVezérlő.add(new JLabel("Dolgozók listája részlegenként"));
    pnVezérlő.add(bt);
    bt.addActionListener(this);
    pn.add(pnVezérlő, BorderLayout.NORTH);
    pn.add(new JScrollPane(c));
    return pn;
  }
  
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == btSzűr1)
      lista1();
    if (e.getSource() == btSzűr2)
      lista2();
    if (e.getSource() == btSzűr3)
      lista3();
    if (e.getSource() == btSzűr4)
      lista4();
    if (e.getSource() == btSzűr5)
      lista5();
    if (e.getSource() == btSzűr6)
      lista6();
  }

  private void lista1() {
    dlm1.clear();
    try {
      Adatbazis.kapcsolatNyit();
      ResultSet rs = Adatbazis.lekérdezRészlegDolgozó();
      rs.next();
      while (true) {
        String aktuálisRészleg = rs.getString("RÉSZLEG");
        dlm1.addElement(aktuálisRészleg);
        while (rs.getString("RÉSZLEG").equals(aktuálisRészleg)) {
          dlm1.addElement("    " + rs.getString("NÉV"));
          rs.next();
        }
      }
    }
    catch (SQLException e) {
      Adatbazis.kapcsolatZár();
    }
  }

  private void lista2() {
    dlm2.clear();
    Adatbazis.kapcsolatNyit();
    ArrayList<String> részlegLista = Adatbazis.lekérdezRészleg();
    for (String részleg : részlegLista) {
      int részlegDolgozóDb = Adatbazis.lekérdezRészlegDolgozóDb(részleg);
      if (részlegDolgozóDb > 0) {
        dlm2.addElement(részleg + " (" + részlegDolgozóDb + " fő)");
        ArrayList<String> részlegDolgozóLista = Adatbazis.lekérdezRészlegDolgozó(részleg);
        for (String részlegDolgozó : részlegDolgozóLista)
          dlm2.addElement("    " + részlegDolgozó);
        dlm2.addElement(" ");
      }
    }
    dlm2.addElement("Összesen: " + Adatbazis.lekérdezDolgozóDb() + " fő");
    Adatbazis.kapcsolatZár();
  }

  private void lista3() {
    dtm3.setRoot(null);
    DefaultMutableTreeNode faGyökér=new DefaultMutableTreeNode("Részlegek");
    dtm3.setRoot(faGyökér);
    try {
      Adatbazis.kapcsolatNyit();
      ResultSet rs = Adatbazis.lekérdezRészlegDolgozó();
      rs.next();
      while (true) {
        String aktuálisRészleg=rs.getString("RÉSZLEG");
        DefaultMutableTreeNode faCsomópont=new DefaultMutableTreeNode(aktuálisRészleg);
        faGyökér.add(faCsomópont);
        while (rs.getString("RÉSZLEG").equals(aktuálisRészleg)) {
          DefaultMutableTreeNode faLevél=new DefaultMutableTreeNode(rs.getString("NÉV"));
          faCsomópont.add(faLevél);
          rs.next();
        }
      }
    }
    catch (SQLException e) {
      Adatbazis.kapcsolatZár();
    }
    faCsomópontKinyit(tFa3, tFa3.getPathForRow(0));
  }
  
  private void lista4() {
    dtm4.setRoot(null);
    Adatbazis.kapcsolatNyit();
    DefaultMutableTreeNode faGyökér=
      new DefaultMutableTreeNode("Részlegek ("+Adatbazis.lekérdezDolgozóDb()+" fő)");
    dtm4.setRoot(faGyökér);
    ArrayList<String> részlegLista = Adatbazis.lekérdezRészleg();
    for (String részleg : részlegLista) {
      int részlegDolgozóDb = Adatbazis.lekérdezRészlegDolgozóDb(részleg);
      if (részlegDolgozóDb > 0) {
        DefaultMutableTreeNode faCsomópont =
                new DefaultMutableTreeNode(részleg + " (" + részlegDolgozóDb + " fő)");
        faGyökér.add(faCsomópont);
        ArrayList<String> részlegDolgozóLista = Adatbazis.lekérdezRészlegDolgozó(részleg);
        for (String részlegDolgozó : részlegDolgozóLista) {
          DefaultMutableTreeNode faLevél=new DefaultMutableTreeNode(részlegDolgozó);
          faCsomópont.add(faLevél);
        }
      }
    }
    Adatbazis.kapcsolatZár();
    faCsomópontKinyit(tFa4, tFa4.getPathForRow(0));    
  }

  private void lista5() {
    dtm5.setRoot(null);
    DefaultMutableTreeNode faGyökér=
      new DefaultMutableTreeNode("Részlegek");
    dtm5.setRoot(faGyökér);
    int összesDolgozóDb=0;
    try {
      Adatbazis.kapcsolatNyit();
      ResultSet rs = Adatbazis.lekérdezRészlegDolgozó();
      rs.next();
      while (true) {
        String aktuálisRészleg = rs.getString("RÉSZLEG");
        DefaultMutableTreeNode faCsomópont = new DefaultMutableTreeNode(aktuálisRészleg);
        faGyökér.add(faCsomópont);
        int részlegDolgozóDb=0;
        while (rs.getString("RÉSZLEG").equals(aktuálisRészleg)) {
          DefaultMutableTreeNode faLevél =
            new DefaultMutableTreeNode(rs.getString("NÉV"));
          faCsomópont.add(faLevél);
          részlegDolgozóDb++;
          rs.next();
        }
        összesDolgozóDb+=részlegDolgozóDb;
        faCsomópont.setUserObject(faCsomópont.getUserObject()+" ("+részlegDolgozóDb+" fő)");
      }
    }
    catch (SQLException e) {
      faGyökér.setUserObject(faGyökér.getUserObject()+" ("+összesDolgozóDb+" fő)");
      Adatbazis.kapcsolatZár();
    }
    faCsomópontKinyit(tFa5, tFa5.getPathForRow(0));
  }
  
  private void lista6() {
    dtm6.setRoot(null);
    DefaultMutableTreeNode faGyökér=
      new DefaultMutableTreeNode("Részlegek");
    dtm6.setRoot(faGyökér);
    try {
      Adatbazis.kapcsolatNyit();
      ResultSet rs = Adatbazis.lekérdezRészlegDolgozó();
      rs.next();
      while (true) {
        String aktuálisRészleg = rs.getString("RÉSZLEG");
        DefaultMutableTreeNode faCsomópont = new DefaultMutableTreeNode(aktuálisRészleg);
        faGyökér.add(faCsomópont);
        while (rs.getString("RÉSZLEG").equals(aktuálisRészleg)) {
          DefaultMutableTreeNode faLevél =
            new DefaultMutableTreeNode(rs.getString("NÉV"));
          faCsomópont.add(faLevél);
          rs.next();
        }
      }
    }
    catch (SQLException e) {
      Adatbazis.kapcsolatZár();
    }
    int összesDolgozóDb=0;
    Enumeration részleg=faGyökér.children();
    while(részleg.hasMoreElements()) {
      DefaultMutableTreeNode aktuálisRészleg=(DefaultMutableTreeNode)részleg.nextElement();
      int részlegDolgozó=aktuálisRészleg.getChildCount();
      aktuálisRészleg.setUserObject(aktuálisRészleg.getUserObject()+" ("+részlegDolgozó+" fő)");
      összesDolgozóDb+=részlegDolgozó;
    }
    faGyökér.setUserObject(faGyökér.getUserObject()+" ("+összesDolgozóDb+" fő)");        
    faCsomópontKinyit(tFa6, tFa6.getPathForRow(0));
  }

  private void faCsomópontKinyit(JTree fa, TreePath útvonal) {
    Enumeration e=((DefaultMutableTreeNode)útvonal.getLastPathComponent()).children();
    while(e.hasMoreElements())
      faCsomópontKinyit(fa, útvonal.pathByAddingChild((DefaultMutableTreeNode)e.nextElement()));
    fa.expandPath(útvonal);
  }

  public static void main(String[] args) {
    new Adatbazis3();
  }
}