package com.everwing.coreservice.common.utils.jdbc;

import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: DBParams <br/>
 * Function: JDBC参数对象. <br/>
 * Reason: 用于设置参数的对象. <br/>
 * Date: 2016年2月29日 下午5:25:54 <br/>
 *
 * @author wust
 * @version 1.5.1.2
 * @since JDK 1.7.0_75
 */
public class DBParams 
{
	/**
	 * 数据库的基本类型，包含的jdbc封装的基本类型
	 */
	private static final int[] SQLTYPES = {Types.INTEGER, Types.BIGINT, Types.VARCHAR,Types.DATE, Types.TIMESTAMP, Types.DOUBLE, Types.TIME};
	
	/**
	 * Java数据基本类型，SQLTYPES与CLASSTYPES类型是一一对应的
	 */
	private static final Class<?>[] CLASSTYPES = {Integer.class, Long.class, String.class,Date.class, Timestamp.class, Double.class, Time.class};

	/**
	 * 访问数据库，增删改查的sql语句预先定义的语句参数  
	 */
    private List<ParamEntity> paramList = new ArrayList<ParamEntity>(); 
    
	/**
	 * 
	 * copy:复制一份参数集合. <br/>
	 *
	 * @author PeterWu
	 * @return
	 * @since JDK 1.7.0_75
	 */
	public DBParams copy(){
		DBParams copy = new DBParams();
		for(ParamEntity entity: paramList){
			copy.paramList.add(entity);
		}
		return copy;
	}
	
	/**
	 * 
	 * addParam:为SQL语句添加参数值. <br/>
	 * 
	 * @author PeterWu
	 * @param obj  参数值
	 * @since JDK 1.7.0_75
	 */
	public void addParam(Object obj){
		addParam(obj, getSqlTypeByClassType(obj.getClass()));
	}
	/**
	 * 
	 * getSqlTypeByClassType:根据Java数据类型得到SQL基本数据类型. <br/>
	 *
	 * @author PeterWu
	 * @param type Java数据类型
	 * @return     SQL数据类型
	 * @since JDK 1.7.0_75
	 */
	private int getSqlTypeByClassType(Type type){
		for(int i = 0; i < CLASSTYPES.length; i++){
			if(type == CLASSTYPES[i]){
				return SQLTYPES[i];
			}
		}
		throw new RuntimeException("The paremeter of type not be found:" + type);
	}
	
	private int checkSupportType(int sqlType){
		for(int i = 0; i < SQLTYPES.length; i++){
			if(sqlType == SQLTYPES[i]){
				return i;
			}
		}
		throw new RuntimeException("The paremeter of type not be found:" + sqlType);
	}
	
	public void addNullParam(int sqlType){
		checkSupportType(sqlType);
		addParam(null, sqlType);
	}
	
	public void addNullParam(Type t){
		addParam(null, getSqlTypeByClassType(t));
	}
	
	/**
	 * 
	 * addParam:添加参数. <br/>
	 *
	 * @author PeterWu
	 * @param obj  参数值
	 * @param type 参数值数据类型
	 * @since JDK 1.7.0_75
	 */
	private void addParam(Object obj, int type){
		ParamEntity entity = new ParamEntity(obj, type);
		paramList.add(entity);
	}
	
	public void removeParam(int index){
		paramList.remove(index);
	}
	
	public void clearParams(){
		paramList.clear();
	}
	
	/**
	 * 
	 * prepareStatement:设置参数值. <br/>
	 *
	 * @author PeterWu
	 * @param pst
	 * @param startIndex
	 * @throws SQLException
	 * @since JDK 1.7.0_75
	 */
	public void prepareStatement(PreparedStatement pst,int startIndex) throws SQLException{
		for(ParamEntity e: paramList){
			int dataType = e.getValueType();
			Object dataObj = e.getValue();
			if(dataObj == null){//如果为空就设置为null
				pst.setNull(startIndex++, dataType);
				continue;
			}		
			if(dataType == SQLTYPES[0]){				
				pst.setInt(startIndex++, (Integer) dataObj);
			}else if(dataType == SQLTYPES[1]){
				pst.setLong(startIndex++, (Long) dataObj);
			}else if(dataType == SQLTYPES[2]){
				pst.setString(startIndex++, (String) dataObj);
			}else if(dataType == SQLTYPES[3]){
				pst.setDate(startIndex++, (Date) dataObj);
			}else if(dataType == SQLTYPES[4]){
				pst.setTimestamp(startIndex++, (Timestamp) dataObj);
			}else if(dataType == SQLTYPES[5]){
				pst.setDouble(startIndex++, (Double) dataObj);
			}else if(dataType == SQLTYPES[6]){
				pst.setTime(startIndex++, (Time) dataObj);
			}
		}
	}
	
	/**
	 * 
	 * prepareStatement:为执行数据库的sql语句设置参数值. <br/>
	 *
	 * @author PeterWu
	 * @param pst
	 * @throws SQLException
	 * @since JDK 1.7.0_75
	 */
	public void prepareStatement(PreparedStatement pst) throws SQLException{
		prepareStatement(pst, 1);
	}
}
