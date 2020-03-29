/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srv;
import java.io.*;
import java.net.*;
import java.sql.SQLException;
/**
 *
 * @author Orlovskiy
 */
public class Main {
    public static void main(String[] args) throws SQLException {
        
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