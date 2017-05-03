package sy.util;

import java.sql.*;
import java.util.*;

/**
 * Created by heyh on 2016/10/29.
 */
public class SqliteHelper {

    public static Connection getConnection(String dbPath) {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
//            connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static List<Map<String, Object>> query(String dbPath, String sql) {
        Connection conn = getConnection(dbPath);
        List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = conn.createStatement();
            statement.setQueryTimeout(100);
            rs = statement.executeQuery(sql);
            rtnList = convertList(rs);
            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rtnList;
    }

    public static boolean insert(String dbPath, String sql) {
        Connection conn = getConnection(dbPath);
        List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
        Statement statement = null;
        boolean rtn = false;
        try {
            statement = conn.createStatement();
//            statement.set
            rtn = statement.execute(sql);
            statement.close();
//            conn.commit();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rtn;
    }

    public static boolean delete(String dbPath, String sql) {
        Connection conn = getConnection(dbPath);
        List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
        Statement statement = null;
        boolean rtn = false;
        try {
            statement = conn.createStatement();
            statement.setQueryTimeout(100);
            rtn = statement.execute(sql);
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rtn;
    }

    private static List<Map<String, Object>> convertList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        while (rs.next()) {
            Map<String, Object> rowData = new HashMap<String, Object>();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            rtnList.add(rowData);

        }
        return rtnList;
    }

    public static void main(String[] args) {
        String dbPath = "//Users/heyh/Desktop/111.mdb";
        String sql = "select * from 工程数据表";
        List<Map<String, Object>> list = query(dbPath, sql);
        System.out.println(list);
    }
}
