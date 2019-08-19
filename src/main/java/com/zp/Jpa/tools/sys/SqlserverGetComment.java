package com.zp.Jpa.tools.sys;
 
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
 
/**
 * 获得数据库表的元数据
 */
public class SqlserverGetComment{
 
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
 
        HashMap<String, Object> computer_mall = (HashMap<String, Object>) getAllTableColumnMySql();
 
        Set<String> tableNames = computer_mall.keySet();
        System.out.println("一共有表：" + tableNames.size() + "张");
        for (String tableName : tableNames) { 
            System.out.println("===========================\r\n");  
            System.out.println("表名：" + tableName);
 
            // <字段名,类型>  HashMap 集合
            HashMap<String, String> columnNames = (HashMap<String, String>) computer_mall.get(tableName);
 
            Set<String> columns = columnNames.keySet();
 
            for (String column : columns) {
                System.out.println("字段名：" + column + " 类型：" + columnNames.get(column));
            }
 
            System.out.println("===========================\r\n");
        }
 
 
    }
    
    public static Map getAllTableColumnMySql () throws SQLException, ClassNotFoundException {
    	 
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/springbootjpadb";
        String username = "root";
        String password = "root";
        Map<String, Object> tableNameMap = new HashMap<>();
        //加载驱动
        Class.forName(driver);
        //获得数据库连接
        Connection connection = DriverManager.getConnection(url, username, password);
        //获得元数据
        DatabaseMetaData metaData = connection.getMetaData();
        //获得表信息
        ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
 
        while (tables.next()) {
            Map<String, String> columnNameMap = new HashMap<>(); //保存字段名
            Map<String, String> remarkMap = new HashMap<>(); //保存注释
            //获得表名
            String table_name = tables.getString("TABLE_NAME");
            //通过表名获得所有字段名
            ResultSet columns = metaData.getColumns(null, null, table_name, "%");
            System.out.println("==>"+columns.toString());
            //获得所有字段名
            while (columns.next()) {
                //获得字段名
                String column_name = columns.getString("COLUMN_NAME");
                //获得字段类型
                String type_name = columns.getString("TYPE_NAME");
//获得字段注释   注意： 对于此列，SQL Server 始终会返回 Null。
                String remarks = columns.getString("Comment");
//   https://docs.microsoft.com/zh-cn/sql/connect/jdbc/reference/getcolumns-method-sqlserverdatabasemetadata?view=sql-server-2017
                System.out.println(table_name+":"+column_name+":"+remarks);
 
                columnNameMap.put(column_name, type_name+remarks);
//                remarkMap.put(column_name, remarks);
            }
 
            tableNameMap.put(table_name, columnNameMap);
//            tableNameMap.put(table_name, remarkMap);
        }
 
        return tableNameMap;
    }

 
    public static Map getAllTableColumn () throws SQLException, ClassNotFoundException {
 
        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String url = "jdbc:sqlserver://localhost:1433;databaseName=kj201507150001";
        String username = "sa";
        String password = "sa@xinyour1cn";
        Map<String, Object> tableNameMap = new HashMap<>();
        //加载驱动
        Class.forName(driver);
        //获得数据库连接
        Connection connection = DriverManager.getConnection(url, username, password);
        //获得元数据
        DatabaseMetaData metaData = connection.getMetaData();
        //获得表信息
        ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
 
        while (tables.next()) {
            Map<String, String> columnNameMap = new HashMap<>(); //保存字段名
            Map<String, String> remarkMap = new HashMap<>(); //保存注释
            //获得表名
            String table_name = tables.getString("TABLE_NAME");
            //通过表名获得所有字段名
            ResultSet columns = metaData.getColumns(null, null, table_name, "%");
            System.out.println("==>"+columns.toString());
            //获得所有字段名
            while (columns.next()) {
                //获得字段名
                String column_name = columns.getString("COLUMN_NAME");
                //获得字段类型
                String type_name = columns.getString("TYPE_NAME");
//获得字段注释   注意： 对于此列，SQL Server 始终会返回 Null。
//                String remarks = columns.getString("REMARKS");
//   https://docs.microsoft.com/zh-cn/sql/connect/jdbc/reference/getcolumns-method-sqlserverdatabasemetadata?view=sql-server-2017
//                System.out.println(table_name+":"+column_name+":"+remarks);
 
                columnNameMap.put(column_name, type_name);
//                remarkMap.put(column_name, remarks);
            }
 
            tableNameMap.put(table_name, columnNameMap);
//            tableNameMap.put(table_name, remarkMap);
        }
 
        return tableNameMap;
    }
 
 
}