/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srv;

import java.security.Timestamp;
import java.sql.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Orlovskiy
 */
public class Functions {
    public static String Response(String req) throws ParseException, SQLException 
    {
        JSONObject request = JSONparser.StringToJson(req);
        switch (request.get("request").toString()){
            case "auth":
            {
                try {
                    String Login = request.get("login").toString();                
                    String Password = request.get("password").toString();
                    String jstring = JSONparser.getJSONFromResultSet(Database.qerry_s("Select `username` FROM `users` WHERE `username` = '"+Login+"' AND `password` = '"+Password+"'"));
                    JSONObject data = new JSONObject();
                    data = JSONparser.StringToJson(jstring);
                    if (data.get("1_username").equals(Login))
                    {
                        UpdateToken(GetToken(data.get("1_username").toString()));
                        String token = GetToken(data.get("1_username").toString());
                        System.out.println(token);
                        return "token:"+token;
                    }
                    else return "login not found debug info: "+data.get("username").toString();
                }catch(SQLException | NullPointerException e){
                    if (e.toString().contains("SQL"))
                            {
                               return "Auth Failed. SQL Error: "+e.toString(); 
                            }
                    else return "Auth failed incorrect username or password";
                }
            }
            case "checkconnection": return "Connection established";
            
            case "registration":
            {
                try {
                    String username = request.get("username").toString();
                    String password = request.get("password").toString();
                    String FirstName = request.get("FirstName").toString();
                    String LastName = request.get("LastName").toString();
                    String country = request.get("country").toString();
                    String token = TokenGenerator.generateToken(username).replaceAll(username+":", "");
                    Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date currentdate = new Date();
                    String lastlogin = formatter.format(currentdate);
                    String tokenexpire = formatter.format(currentdate);
                    Database.qerry_i("INSERT INTO `users` (`id`, `username`, `password`, `token`, `lastlogin`, `tokenexpire`, `FirstName`, `LastName`, `country`) VALUES (NULL, '"+username+"', '"+password+"', '"+token+"', '"+lastlogin+"', '"+tokenexpire+"', '"+FirstName+"', '"+LastName+"', '"+country+"');");
                    return "registration succsess";
                    
                } catch (SQLException ex) {
                    Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println(ex);
                    return "SQL Querry error: "+ex.toString();
                }
            }

            case "update":
            {   
                if (!IsAuth(request.get("token").toString())) return "Unauthorized";
                switch(request.get("updateType").toString())
                {
                    case "messages": {
                        String jstring = JSONparser.getJSONFromResultSet(Database.qerry_s("select e.date as date, e.message as message, e.anon as anon, d.username as sender\n" +
                                                                                            "from messages e  \n" +
                                                                                            "inner join users d on e.senderid = d.id\n" +
                                                                                            "where e.reciverid = "+GetUserId(GetUserName(request.get("token").toString()))));
                        return jstring;
                    }
                    case "board":
                    {
                                               String jstring = JSONparser.getJSONFromResultSet(Database.qerry_s("select e.date as date, e.bmessage as bmessage, d.username as user\n" +
                                                                                            "from board e  \n" +
                                                                                            "inner join users d on e.userid = d.id"));
                                               return jstring;
                    }
                    case "advertise":
                    {
                        String jstring = JSONparser.getJSONFromResultSet(Database.qerry_s("SELECT a.advertise as advertise, a.date as date, b.username as owner FROM advertise a\n" +
                                                                                            "INNER JOIN users b on a.ownerid = b.id"));
                        return jstring;
                    }
                }
                
            }
            case "send":
            {
                if (!IsAuth(request.get("token").toString())) return "Unauthorized";
                String username = GetUserName(request.get("token").toString());
                try{
                    GetUserId(request.get("recivername").toString());
                }catch(NullPointerException e)
                {
                    System.out.println("username not found exception: "+e);
                    return "Reciver was not found";
                }
                try{
                    boolean b = Boolean.parseBoolean(request.get("anon").toString());
                    int anon = b ? 1 : 0;
                    Database.qerry_i("INSERT INTO `messages` (`senderid`, `reciverid`, `message`, `date`, `anon`) VALUES ('"+GetUserId(username)+"', '"+GetUserId(request.get("recivername").toString())+"', '"+request.get("message").toString()+"', current_timestamp(), '"+anon+"');");
                    return "Message sended";
                }catch (SQLException e)
                {
                    System.out.println(e.toString());
                    return "Something wrong on server side. Contact to your system administrator.";
                }
            }
            case "board":
            {
                if (!IsAuth(request.get("token").toString())) return "Unauthorized";
                String username = GetUserName(request.get("token").toString());
                try{
                    Database.qerry_i("INSERT INTO `board` (`userid`, `bmessage`, `date`) VALUES ('"+GetUserId(GetUserName(request.get("token").toString()))+"', '"+request.get("message")+"', current_timestamp());");
                    return "Message sended";
                }catch (SQLException e)
                {
                    System.out.println(e.toString());
                    return "Something wrong on server side. Contact to your system administrator.";
                }
            }
            case "search":
            {
                if (!IsAuth(request.get("token").toString())) return "Unauthorized";
                String jstring = JSONparser.getJSONFromResultSet(Database.qerry_s("SELECT `FirstName`, `LastName`, `Country` FROM `users` WHERE `FirstName` LIKE '%"+request.get("querry").toString()+"%' OR `LastName` LIKE '%"+request.get("querry").toString()+"%' OR `country` LIKE '%"+request.get("querry").toString()+"%'"));
                return jstring;
            } 
            default: return "unknown request";
        }
    }
    private static boolean IsAuth(String token) 
    {
        try {
                String jstring = JSONparser.getJSONFromResultSet(Database.qerry_s("Select `tokenexpire` FROM `users` WHERE `token` = '"+token+"'"));
                JSONObject data = JSONparser.StringToJson(jstring);
                Date expiredate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.get("1_tokenexpire").toString());
                Date currentdate = new Date();
                if(expiredate.before(currentdate))
                {
                    System.out.println("token: "+token+" expire. token date: "+expiredate);
                    return false;
                }
                else return true;
            }catch(SQLException | NullPointerException | ParseException e){
                System.out.println("Exception from IsAuth: "+e);
                return false;
            } catch (java.text.ParseException ex) {
                Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
          
    }
    private static void UpdateToken(String token)
    {
       String jstring;
        try {
            jstring = JSONparser.getJSONFromResultSet(Database.qerry_s("Select `username` FROM `users` WHERE `token` = '"+token+"'"));
            JSONObject data = JSONparser.StringToJson(jstring);
            String username = data.get("1_username").toString();
            String tokennew = TokenGenerator.generateToken(username).replaceAll(username+":", "");
            Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currentdate = new Date();
            Date expiredate = new Date();
            expiredate.setTime(expiredate.getTime() + TimeUnit.HOURS.toMillis(4));
            Database.qerry_i("UPDATE `users` SET `token` = '"+tokennew+"', `tokenexpire` = '"+formatter.format(expiredate)+"', `lastlogin` = '"+formatter.format(currentdate)+"' WHERE `token` = '"+token+"'");
        } catch (SQLException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        } catch (ParseException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static String GetToken(String username)
    {
        try {
            String jstring = JSONparser.getJSONFromResultSet(Database.qerry_s("Select `token` FROM `users` WHERE `username` = '"+username+"'"));
            JSONObject data = JSONparser.StringToJson(jstring);
            String token = data.get("1_token").toString();
            return token;

        } catch (ParseException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "error";
    }
    private static String GetUserName(String token) throws ParseException, SQLException
    {
        String jstring = JSONparser.getJSONFromResultSet(Database.qerry_s("Select `username` FROM `users` WHERE `token` = '"+token+"'"));
        JSONObject data = JSONparser.StringToJson(jstring);
        return data.get("1_username").toString();
    }
        private static String GetUserId(String username) throws ParseException, SQLException
    {
        String jstring = JSONparser.getJSONFromResultSet(Database.qerry_s("Select `id` FROM `users` WHERE `username` = '"+username+"'"));
        JSONObject data = JSONparser.StringToJson(jstring);
        return data.get("1_id").toString();
    }
}

