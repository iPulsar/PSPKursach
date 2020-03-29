/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srv;
import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;
/**
 *
 * @author Orlovskiy
 */
public class Main {
    public static void main(String[] args) throws SQLException {
        HashMap config;
        try {
            config = JSONparser.PharseJsonConfig("./config.json");

        Database.User = config.get("username").toString();
        Database.Password = config.get("password").toString();
        Database.dbname = config.get("database").toString();
                } catch (ParseException ex) {
                    Database.User = "root";
                    Database.Password = "root";
                    Database.dbname = "db";    
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true)
        {
            Database.DBConnect();
            MultiThreadedServer server = new MultiThreadedServer(9000);
            new Thread(server).start();
            try {
                Thread.sleep(300 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Database.DBClose();
            System.out.println("Stopping Server");
            server.stop();
        }
        
    }
}