package com.zp.Jpa.tools.sys;

import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;  
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
import java.util.Set;

import com.zp.Jpa.tools.StringUtils;  
  
/** 
 * 读取mysql某数据库下表的注释信息 
 *  
 * @author xxx 
 */  
public class MySqlGetComment {  
    public static Connection getMySQLConnection() throws Exception {  
        Class.forName("com.mysql.jdbc.Driver");  
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/springbootjpadb", "root", "root");  
        return conn;  
    }  
      
  
    /** 
     * 获取当前数据库下的所有表名称 
     * @return 
     * @throws Exception 
     */  
    public static List getAllTableName() throws Exception {  
        List tables = new ArrayList();  
        Connection conn = getMySQLConnection();  
        Statement stmt = conn.createStatement();  
        ResultSet rs = stmt.executeQuery("SHOW TABLES ");  
        while (rs.next()) {  
            String tableName = rs.getString(1);  
            tables.add(tableName);  
        }  
        rs.close();  
        stmt.close();  
        conn.close();  
        return tables;  
    }  
      
  
    /** 
     * 获得某表的建表语句 
     * @param tableName 
     * @return 
     * @throws Exception 
     */  
    public static Map getCommentByTableName(List tableName) throws Exception {  
        Map map = new HashMap();  
        Connection conn = getMySQLConnection();  
        Statement stmt = conn.createStatement();  
        for (int i = 0; i < tableName.size(); i++) {  
            String table = (String) tableName.get(i);  
            ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + table);  
            if (rs != null && rs.next()) {  
                String createDDL = rs.getString(2);  
                String comment = parse(createDDL);  
                map.put(table, comment);  
            }  
            rs.close();  
        }  
        stmt.close();  
        conn.close();  
        return map;  
    }  
    /** 
     * 获得某表中所有字段的注释 
     * @param tableName 
     * @return 
     * @throws Exception 
     */  
    public static void getColumnCommentByTableName(List tableName) throws Exception {  
        Map map = new HashMap();  
        Connection conn = getMySQLConnection();  
        Statement stmt = conn.createStatement();  
        for (int i = 0; i < tableName.size(); i++) {  
            String table = (String) tableName.get(i);  
            ResultSet rs = stmt.executeQuery("show full columns from " + table);  
            System.out.println("【"+table+"】");  
//          if (rs != null && rs.next()) {  
                //map.put(rs.getString("Field"), rs.getString("Comment"));  
            while (rs.next()) {     
//              System.out.println("字段名称：" + rs.getString("Field") + "\t"+ "字段注释：" + rs.getString("Comment") );  
                System.out.println(rs.getString("Field") + "\t:\t"+  rs.getString("Comment") );  
            }   
//          }  
            rs.close();  
        }  
        stmt.close();  
        conn.close();  
//      return map;  
    }  
  public static String[] getCommentByTableName(String tableName) throws Exception {
	  List<String> comments=new ArrayList<>();
	  Map map = new HashMap();  
      Connection conn = getMySQLConnection();  
      Statement stmt = conn.createStatement();  
      
           
          ResultSet rs = stmt.executeQuery("show full columns from " + tableName);  
          System.out.println("【"+tableName+"】");  
//        if (rs != null && rs.next()) {  
              //map.put(rs.getString("Field"), rs.getString("Comment"));  
          while (rs.next()) {     
//            System.out.println("字段名称：" + rs.getString("Field") + "\t"+ "字段注释：" + rs.getString("Comment") );  
              System.out.println(rs.getString("Field") + "\t:\t"+  rs.getString("Comment") );  
              if(StringUtils.isEmpty(rs.getString("Comment")) ) {
            	  comments.add(rs.getString("Field"));//如果没有备注,就用字段名来充当表头
              }else {
            	  comments.add(rs.getString("Comment"));
              }
              
          }   
//        }  
          rs.close();  
      
      stmt.close();  
      conn.close();  
      //创建一个数组,
      int num=comments.size();
      String[] comms=new String[num];
      for(int i=0;i<num;i++) {
    	  comms[i]=comments.get(i);
      }
      return comms;
  }
      
  
    /** 
     * 返回注释信息 
     * @param all 
     * @return 
     */  
      
    public static String parse(String all) {  
        String comment = null;  
        int index = all.indexOf("COMMENT='");  
        if (index < 0) {  
            return "";  
        }  
        comment = all.substring(index + 9);  
        comment = comment.substring(0, comment.length() - 1);  
        return comment;  
    }  
  
    public static void main(String[] args) throws Exception {  
//        List tables = getAllTableName();  
//        Map tablesComment = getCommentByTableName(tables);  
//        Set names = tablesComment.keySet();  
//        Iterator iter = names.iterator();  
//        while (iter.hasNext()) {  
//            String name = (String) iter.next();  
//            System.out.println("Table Name: " + name + ", Comment: " + tablesComment.get(name));  
//        }  
//          
//        getColumnCommentByTableName(tables);  
//    }
    	System.err.println("objective_tb");
    	String[] object=getCommentByTableName("objective_tb");
    	for(String obj:object) {
    		System.out.println(obj.toString());
    	}
    
}
}