package com.company;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    public static void main(String[] args)throws SQLException {
        input();
    }

    private static String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12));
    }

    private static void checkPass(String plainPassword, String hashedPassword) {
        if (BCrypt.checkpw(plainPassword, hashedPassword))
            System.out.println("The password matches.");
        else
            System.out.println("The password does not match.");
    }

    public static void input() throws SQLException {

        Scanner scan = new Scanner(System.in);
        System.out.println("1 for Register Data;2 for Login; 3 for Output Data from table;");
        System.out.println("4 for Output Data from Items table; 5 for buy an Item; any other exit");
        int k = scan.nextInt();

        if (k == 1) {
            DBReg();

        } else if (k == 2) {
            DBLog();

        } else if(k==3){
            DBOutput();
        }
        else {
            System.out.println("System Exit");
            System.exit(0);
        }
    }
    public static void DBReg() throws SQLException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter username:");
        String username = scan.nextLine();
        System.out.println("Enter password:");
        String password = scan.nextLine();

        PreparedStatement stmt = null;
        DBConnection db = new DBConnection();
        int money=0;
        try{
            Statement st = db.getConnection().createStatement();
            String sql="insert into shoplogin(username,password,money) values(?, ?, ?)";
            stmt = db.getConnection().prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hashPassword(password));
            stmt.setInt(3, money);
            stmt.execute();
            System.out.println("Register Successfully");
            input();
        } catch (SQLException se){
            System.out.println(se.getMessage());
        }
    }


    public static void DBLog() throws SQLException {
        Data s = new Data();
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter login: ");
        String username = scan.nextLine();
        s.setUsername(username);
        System.out.println("Enter password: ");
        String pass = scan.nextLine();
        s.setPassword(pass);
        DBConnection db = new DBConnection();
        String userquery = "select password from shoplogin where username ='" + s.getUsername() + "' ";
        String userquery1 = "select * from shoplogin where username ='" + s.getUsername() + "' ";
        Statement st = db.getConnection().createStatement();
        ResultSet rs = st.executeQuery(userquery);
        Statement st1 = db.getConnection().createStatement();
        ResultSet rs1 = st1.executeQuery(userquery1);
        if (rs.next()) {
            String password = rs.getString("password");
            checkPass(pass, password);

        }
            else {
            System.out.print("Check your login or password");
            input();
        }
        while(rs1.next()){
            s.setId(rs1.getInt("id"));
            s.setUsername(rs1.getString("username"));
            s.setPassword(rs1.getString("password"));
            s.setMoney(rs1.getInt("money"));
            System.out.println(s);
            System.out.println("1 for OutPut Items;2 for Buy;any other exit");
            int sf = scan.nextInt();
            if(sf==1){
                DBItemsOutput(s);
            }
            else if(sf==2){
                DBItemsBuy(s);
            }
            else{
                System.out.println("System Exit");
                System.exit(0);
            }
        }
    }

    public static void DBOutput() throws SQLException {
        DBConnection db = new DBConnection();
        try {
            Statement st = db.getConnection().createStatement();
            String query = "SELECT * FROM shoplogin";
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                Data s = new Data();
                s.setId(rs.getInt("id"));
                s.setUsername(rs.getString("username"));
                s.setPassword(rs.getString("password"));
                s.setMoney(rs.getInt("money"));
                System.out.println(s);
            }
            input();
        } catch (SQLException ex) {
            System.out.println("Failed");
        }
    }
    public static void DBItemsOutput(Data data) throws SQLException {
        DBConnection db = new DBConnection();
        Scanner scan = new Scanner(System.in);
        try {
            Statement st = db.getConnection().createStatement();
            String query = "SELECT * FROM items";
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                Items s = new Items();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setPrice(rs.getInt("price"));
                s.setQty(rs.getInt("qty"));
                System.out.println(s);
            }
            System.out.println("1 for OutPut Items;2 for Buy;any other exit");
            int sf = scan.nextInt();
            if(sf==1){
                DBItemsOutput(data);
            }
            else if(sf==2){
                DBItemsBuy(data);
            }
            else{
                System.out.println("System Exit");
                System.exit(0);
            }
        } catch (SQLException ex) {
            System.out.println("Failed");
        }
    }
    public static void DBItemsBuy(Data data) throws SQLException {
        DBConnection db = new DBConnection();
        try {
            Scanner scan = new Scanner(System.in);
            int idd = scan.nextInt();
            Statement st = db.getConnection().createStatement();
            String query = "SELECT * FROM items where id='" + idd + "' ";
            ResultSet rs = st.executeQuery(query);
            Items s = new Items();
            PreparedStatement ps = db.getConnection().prepareStatement(
                    "UPDATE shoplogin SET money = ? WHERE id = ? ");
            PreparedStatement ps1 = db.getConnection().prepareStatement(
                    "UPDATE items SET qty = ? WHERE id = ? ");
            while(rs.next()) {
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setPrice(rs.getInt("price"));
                s.setQty(rs.getInt("qty"));
            }
            if(data.getMoney()>0 && s.getQty()>0) {
                ps.setInt(1, data.setMoney(data.getMoney() - s.getPrice()));
                ps.setInt(2, data.getId());
                ps.executeUpdate();
                ps.close();
                ps1.setInt(1,s.setQty(s.getQty())-1);
                ps1.setInt(2,s.getId());
                ps1.executeUpdate();
                ps1.close();
            }

            else{
                System.out.println("No money or qty");
            }


            System.out.println(s);


            System.out.println(data);
            System.out.println("1 for OutPut Items;2 for Buy;any other exit");
            int sf = scan.nextInt();
            if(sf==1){
                DBItemsOutput(data);
            }
            else if(sf==2){
                DBItemsBuy(data);
            }
            else{
                System.out.println("System Exit");
                System.exit(0);
            }
        } catch (SQLException ex) {
            System.out.println("Failed");
        }
    }




}
