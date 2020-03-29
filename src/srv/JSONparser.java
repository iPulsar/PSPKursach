/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srv;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONValue;
/**
 *
 * @author Orlovskiy
 */
public class JSONparser {
        public static HashMap PharseJsonConfig(String path) throws ParseException
    {
        HashMap config = new HashMap();
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
         
        try (FileReader reader = new FileReader(path))
        {
            
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONObject object = (JSONObject) obj;
            object.keySet().forEach(keyStr -> /*there is pharse magic begins */
            {
               Object keyvalue = object.get(keyStr);
               config.put(keyStr.toString(), keyvalue.toString());
            });
            return config;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return config;
    }
    public static void PharseJsonRequest(String pathToJson)
    {
        
    }
    public static String JsonToString(JSONObject json)
    {
        String result = json.toString();
        return result;
    }
    public static JSONObject StringToJson(String str) throws ParseException
    {   JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(str);
    }
    public static String getJSONFromResultSet(ResultSet rs) {
        JSONObject json = new JSONObject(); 
        if(rs!=null)
        {
            try {
                ResultSetMetaData metaData = rs.getMetaData();
                int counter = 1;
                while(rs.next())
                {
                    for (int i = 1; i <= metaData.getColumnCount(); i++)
                    {
                        json.put(counter+"_"+metaData.getColumnName(i),rs.getString(i));
                        System.out.println(counter+"_"+metaData.getCatalogName(i)+" "+rs.getString(i));
                    }
                    counter++;
                }
                json.put("numberOfRow",counter-1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
         }
        System.out.println(json.toJSONString());
         return json.toString();
        }
    }
