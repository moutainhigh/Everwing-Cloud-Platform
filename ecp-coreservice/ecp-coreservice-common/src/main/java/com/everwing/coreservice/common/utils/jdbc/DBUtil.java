package com.everwing.coreservice.common.utils.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: DBUtil <br/>
 * Function: DB工具. <br/>
 * Reason: 使用JDBC时需要的DB辅助工具. <br/>
 * Date: 2016年3月1日 下午1:28:28 <br/>
 *
 * @author wust
 * @version 1.5.1.2
 * @since JDK 1.7.0_75
 */
public class DBUtil {
    static Logger logger = LogManager.getLogger(DBUtil.class);

    public static Connection getConn(final String driver, final String url, final String userName, final String password) {
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            logger.error(e);
        }
        return conn;
    }


    public static void rollBack(Connection con) {
        try {
            con.rollback();
        } catch (SQLException e) {
            logger.error("回滚失败", e);
        }
    }

    /**
     * executeQuery:根据sql查询数据. <br/>
     *
     * @param con 当前连接
     * @param sql 查询sql
     * @return 结果列表
     * @throws SQLException
     * @author PeterWu
     * @since JDK 1.7.0_75
     */
    public static List<Map<String, Object>> executeQuery(Connection con, String sql) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            return getListFromRs(rs);
        } finally {
            closeRs(rs);
            closePst(pst);
        }
    }

    /**
     * executeQuery:执行查询. <br/>
     * 返回查询结果集合，封装结果成entity的列表。
     *
     * @param con
     * @param sql
     * @param c
     * @return
     * @throws SQLException
     * @author PeterWu
     * @since JDK 1.7.0_75
     */
    public static List<Object> executeQuery(Connection con, String sql, Class<?> c) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            return getListFromRs(rs, c);
        } finally {
            closeRs(rs);
            closePst(pst);
        }
    }

    /**
     * getListFromRs:从结果集里面抽取结果，每个键值对为一个List元素，列名为键，列值为值. <br/>
     *
     * @param rs
     * @return
     * @throws SQLException
     * @author PeterWu
     * @since JDK 1.7.0_75
     */
    public static List<Map<String, Object>> getListFromRs(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        int i;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (i = 0; i < columns; i++) {
                map.put(md.getColumnName(i + 1), getValueByType(rs, md.getColumnType(i + 1), md.getColumnName(i + 1)));
            }
            list.add(map);
        }
        return list;
    }

    /**
     * getListFromRsLowerCase:根据结果集得到查询结果. <br/>
     * 以列名作键，以列值作值，列名转换成小写。封装每一个键值对成List
     *
     * @param rs
     * @return
     * @throws SQLException
     * @author PeterWu
     * @since JDK 1.7.0_75
     */
    public static List<Map<String, Object>> getListFromRsLowerCase(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        int i;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (i = 0; i < columns; i++) {
                map.put(md.getColumnName(i + 1).toLowerCase(), getValueByType(rs, md.getColumnType(i + 1), md.getColumnName(i + 1)));
            }
            list.add(map);
        }
        return list;
    }

    /**
     * getListFromRsUpperCase:根据结果集得到查询结果. <br/>
     * 以列名作键，以列值作值，列名转换成大写。封装每一个键值对成List
     *
     * @param rs
     * @return
     * @throws SQLException
     * @author PeterWu
     * @since JDK 1.7.0_75
     */
    public static List<Map<String, Object>> getListFromRsUpperCase(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        int i;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (i = 0; i < columns; i++) {
                map.put(md.getColumnName(i + 1).toUpperCase(), getValueByType(rs, md.getColumnType(i + 1), md.getColumnName(i + 1)));
            }
            list.add(map);
        }
        return list;
    }

    /**
     * getListFromRs:得到转换成entity后的查询集合. <br/>
     *
     * @param rs
     * @param c
     * @return
     * @throws SQLException
     * @author PeterWu
     * @since JDK 1.7.0_75
     */
    public static List<Object> getListFromRs(ResultSet rs, Class<?> c) throws SQLException {
        List<Object> list = new ArrayList<Object>();
        try {
            while (rs.next()) {
                Object o = initObjectFromRs(rs, c);
                list.add(o);
            }
        } catch (IllegalAccessException e) {
            logger.error("", e);
        } catch (InstantiationException e) {
            logger.error("", e);
        }
        return list;
    }

    /**
     * getFirstObjectFromRs:得到结果集里面的第一条记录，且封装成指定的类对象. <br/>
     * 如果要得到对象列表，可遍历该方法。
     *
     * @param rs          结果集
     * @param entityClass 实体类
     * @return 实体对象
     * @throws SQLException
     * @author PeterWu
     * @since JDK 1.7.0_75
     */
    public static Object getFirstObjectFromRs(ResultSet rs, Class<?> entityClass) throws SQLException {
        Object o = null;
        try {
            o = initObjectFromRsIfExist(rs, entityClass);
        } catch (InstantiationException e) {
            logger.error("", e);
        } catch (IllegalAccessException e) {
            logger.error("", e);
        }
        return o;
    }

    /**
     * getValueByType:得到相应数据类型的查询值. <br/>
     *
     * @param rs         结果集
     * @param columnType 列类型
     * @param columnName 列名称
     * @return 相应列的查询结果
     * @throws SQLException
     * @author PeterWu
     * @since JDK 1.7.0_75
     */
    private static Object getValueByType(ResultSet rs, int columnType, String columnName) throws SQLException {
        switch (columnType) {
            case Types.NUMERIC:
                return rs.getLong(columnName);
            case Types.VARCHAR:
                return rs.getString(columnName);
            case Types.DATE:
                return rs.getDate(columnName);
            case Types.TIMESTAMP:
                return rs.getTimestamp(columnName);
            case Types.INTEGER:
                return rs.getInt(columnName);
            case Types.DOUBLE:
                return rs.getDouble(columnName);
            case Types.FLOAT:
                return rs.getFloat(columnName);
            case Types.BIGINT:
                return rs.getLong(columnName);
            default:
                return rs.getObject(columnName);
        }
    }

    /**
     * rsContainsFields:检测结果集里面是否包含entity里面的字段. <br/>
     * 在查询SQL里面可以指定需要的字段，如SELECT user_name AS userName FROM table;
     *
     * @param rs
     * @param fieldName
     * @return
     * @throws SQLException
     * @author PeterWu
     * @since JDK 1.7.0_75
     */
    private static boolean rsContainsFields(ResultSet rs, String fieldName) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        for (int i = 0; i < md.getColumnCount(); i++) {
            if (md.getColumnName(i + 1).equalsIgnoreCase(fieldName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * initObjectFromRs:将查询结果转换成entity. <br/>
     * 类似mybatis或hibernate框架查询返回的entity，此处自己实现。
     * 该方法不会检验字段是否存在。
     *
     * @param rs
     * @return
     * @throws InstantiationException
     * @throws SQLException
     * @throws IllegalAccessException
     * @author PeterWu
     * @since JDK 1.7.0_75
     */
    private static Object initObjectFromRs(ResultSet rs, Class<?> entityClass) throws InstantiationException, SQLException, IllegalAccessException {
        Object obj = entityClass.newInstance();
        Method[] methods = obj.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("set")) {
                try {
                    method.invoke(obj, getParamValueFromRs(rs, method));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("IllegalArgumentException:" + e + "\nMethods:" + method.getName());
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("InvocationTargetException:" + e + "\nMethods:" + method.getName());
                }
            }
        }
        return obj;
    }

    /**
     * initObjectFromRsIfExist:将查询结果转换成entity. <br/>
     * 类似mybatis或hibernate框架查询返回的entity，此处自己实现。
     * 该方法会检验字段是否存在。
     *
     * @param rs          结果集
     * @param entityClass 实体类，里面必须要有get方法和set方法
     * @return
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @author PeterWu
     * @since JDK 1.7.0_75
     */
    private static Object initObjectFromRsIfExist(ResultSet rs, Class<?> entityClass) throws SQLException, IllegalAccessException, InstantiationException {
        Object obj = entityClass.newInstance();
        Method[] methods = obj.getClass().getMethods();
        String field;
        for (Method method : methods) {
            field = method.getName().substring(3);

            // 是set方法，且该方法对应的字段必须存在，
            // 如setUserName方法，userName必须要被包含在结果集中
            // 那么查询SQL必须要返回这个列名，使用AS userName。
            if (method.getName().startsWith("set") && rsContainsFields(rs, field)) {
                try {
                    // 将对应set方法的值封装到类对象里面
                    method.invoke(obj, getParamValueFromRs(rs, method));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("IllegalArgumentException:" + e + "\nMethods:" + method.getName());
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("InvocationTargetException:" + e + "\nMethods:" + method.getName());
                }
            }
        }
        return obj;
    }

    /**
     * getParamValueFromRs:从结果集里面得到指定字段的值. <br/>
     *
     * @param rs     结果集
     * @param method 方法对象
     * @return 相应字段的值
     * @throws SQLException
     * @author PeterWu
     * @since JDK 1.7.0_75
     */
    private static Object getParamValueFromRs(ResultSet rs, Method method) throws SQLException {
        String fieldName = method.getName().substring(3);
        Type type = method.getGenericParameterTypes()[0];
        return getValueFromRs(rs, fieldName, type);
    }

    /**
     * getValueFromRs:根据字段名称和字段类型得到字段值. <br/>
     *
     * @param rs        结果集
     * @param fieldName 字段名称
     * @param fieldType 字段类型
     * @return
     * @throws SQLException
     * @author PeterWu
     * @since JDK 1.7.0_75
     */
    private static Object getValueFromRs(ResultSet rs, String fieldName, Type fieldType) throws SQLException {
        String type = fieldType.toString();
        try {
            if (type.equals("int") || type.equals("class java.lang.Integer")) {
                return rs.getInt(fieldName);
            } else if (type.equals("float") || type.equals("class java.lang.Float")) {
                return rs.getFloat(fieldName);
            } else if (type.equals("double") || type.equals("class java.lang.Double")) {
                return rs.getDouble(fieldName);
            } else if (type.equals("long") || type.equals("class java.lang.Long")) {
                return rs.getLong(fieldName);
            } else if (type.equals("class java.lang.String")) {
                return rs.getString(fieldName);
            } else if (type.equals("class java.sql.Timestamp")) {
                return rs.getTimestamp(fieldName);
            } else if (type.equals("class java.sql.Date")) {
                return rs.getDate(fieldName);
            } else if (type.equals("class java.sql.Time")) {
                return rs.getTime(fieldName);
            }
        } catch (SQLException e) {
            throw new SQLException("SQLException when get field:" + fieldName + "\n" + e);
        }
        throw new RuntimeException("getValueFromRsByField fail, field type is:" + type + ",field name is:" + fieldName);
    }

    /**
     * closeRs:关闭ResultSet. <br/>
     *
     * @param rss
     * @author PeterWu
     * @since JDK 1.7.0_75
     */
    public static void closeRs(ResultSet... rss) {
        for (ResultSet rs : rss) {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {

                }
            }
        }
    }

    /**
     * closePst:关闭Statement. <br/>
     *
     * @param psts
     * @author PeterWu
     * @since JDK 1.7.0_75
     */
    public static void closePst(Statement... psts) {
        for (Statement pst : psts) {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {

                }
            }
        }
    }

    /**
     * closeCon:关闭Connection. <br/>
     *
     * @param cons
     * @author PeterWu
     * @since JDK 1.7.0_75
     */
    public static void closeCon(Connection... cons) {
        for (Connection con : cons) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {

                }
            }
        }
    }
}