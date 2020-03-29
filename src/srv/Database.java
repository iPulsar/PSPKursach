/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srv;
import java.sql.*;
/**
 *
 * @author Orlovskiy
 */
public class Database {
    protected static Connection con = null;
    protected static String User = "root";
    protected static String Password = "root";
    protected static String dbname = "db";
    protected static Statement stmt = null;
    public static void DBConnect()
    {
        try{  
        Class.forName("org.mariadb.jdbc.Driver");  
        Database.con =DriverManager.getConnection(  
        "jdbc:mariadb://192.168.211.8:3306/"+dbname,User,Password);  
        //here sonoo is database name, root is username and password  
        Database.stmt=Database.con.createStatement();
        }catch(Exception e){ System.out.println(e);}  
    }  
    public static ResultSet qerry_s(String q) throws SQLException
    {
       DBConnect();
       System.out.println(q);
       ResultSet r = stmt.executeQuery(q);
       return r;
    }
        public static void qerry_i(String q) throws SQLException
    {
       DBConnect();
       System.out.println(q);
       stmt.executeQuery(q);
    }
    public static void DBClose() throws SQLException
    {
        Database.con.close();
    }
    
}
