package com.everwing.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FormulaCalculationUtil {
	// 创建脚本引擎管理器  
	private static  ScriptEngineManager  sm = new ScriptEngineManager();
	// 创建一个处理JavaScript的脚本引擎 
    private static ScriptEngine engine = sm.getEngineByExtension("js");
    
    private static final Logger log = LogManager.getLogger(FormulaCalculationUtil.class);
    
    /**
     * 纯数值计算,公式不含任何变量
     * @param formulaValue
     * @return
     * @throws ScriptException 
     * 这里计算四舍五入保留6位小数
     */
	public static Object numericalCalculation(String formulaValue){
		Object obj=null;
		try {
			obj= engine.eval("Math.round(("+formulaValue+")*1000000)/1000000");
			return obj;
		}catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	private static String getLogStr(String error){
		return String.format("当前时间 : %s , 异常  -> %s" ,new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()),error);
	}
	
	/**
	 * 水电费公式计算,传用量等变量
	 * @param formulaValue
	 * @return
	 * 这里计算四舍五入保留6位小数
	 */
	public static Object waterElectCalculation(String formulaValue,Double $Count,Double $PeakCount,Double $VallCount,Double $CommCount){
		Object obj =null;
		try {
			engine.put("$Count", $Count);
			engine.put("$PeakCount", $PeakCount);
			engine.put("$VallCount", $VallCount);
			engine.put("$CommCount", $CommCount);
			obj= engine.eval("Math.round(("+formulaValue+")*1000000)/1000000");
			return obj;
		}catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		
		return obj;
	}
	
	
	//关于JS的MATH函数
	/**
	 * 返回算术常量 e，即自然对数的底数（约等于2.718）。
	 * @return
	 */
	public static Object getE(){
		Object obj =null;
		try {
			obj= engine.eval("Math.E");
			return obj;
		} catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 返回 2 的自然对数（约等于0.693）。
	 * @return
	 */
	public static Object getLN2(){
		Object obj =null;
		try {
			obj= engine.eval("Math.LN2");
			return obj;
		} catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 返回 10 的自然对数（约等于2.302）。
	 * @return
	 */
	public static Object getLN10(){
		Object obj =null;
		try {
			obj= engine.eval("Math.LN10");
			return obj;
		} catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 返回以 2 为底的 e 的对数（约等于 1.414）。
	 * @return
	 */
	public static Object getLOG2E(){
		Object obj =null;
		try {
			obj= engine.eval("Math.LOG2E");
			return obj;
		}catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 返回以 10 为底的 e 的对数（约等于0.434）
	 * @return
	 */
	public static Object getLOG10E(){
		Object obj =null;
		try {
			obj= engine.eval("Math.LOG10E");
			return obj;
		} catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 返回圆周率（约等于3.14159）
	 * @return
	 */
	public static Object getPI(){
		Object obj =null;
		try {
			obj= engine.eval("Math.PI");
			return obj;
		} catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 	返回返回 2 的平方根的倒数（约等于 0.707）
	 * @return
	 */
	public static Object getSQRT1_2(){
		Object obj =null;
		try {
			obj= engine.eval("Math.SQRT1_2");
			return obj;
		} catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 返回 2 的平方根（约等于 1.414）。
	 * @return
	 */
	public static Object getSQRT2(){
		Object obj =null;
		try {
			obj= engine.eval("Math.SQRT2");
			return obj;
		} catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 求绝对值
	 * @param parm
	 * @return
	 */
	public static Object getABS(Double parm){
		Object obj =null;
		try {
			engine.put("parm", parm);
			obj= engine.eval("Math.abs(parm)");
			return obj;
		}catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 返回数的反余弦值。
	 * @param parm
	 * @return
	 */
	public static Object getAcos(Double parm){
		Object obj =null;
		try {
			engine.put("parm", parm);
			obj= engine.eval("Math.acos(parm)");
			return obj;
		}catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 返回数的反正弦值
	 * @param parm
	 * @return
	 */
	public static Object getAsin(Double parm){
		Object obj =null;
		try {
			engine.put("parm", parm);
			obj= engine.eval("Math.asin(parm)");
			return obj;
		}catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}	
	
	/**
	 * 以介于 -PI/2 与 PI/2 弧度之间的数值来返回 x 的反正切值。
	 * @param parm
	 * @return
	 */
	public static Object getAtan(Double parm){
		Object obj =null;
		try {
			engine.put("parm", parm);
			obj= engine.eval("Math.atan(parm)");
			return obj;
		}catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 返回从 x 轴到点 (x,y) 的角度（介于 -PI/2 与 PI/2 弧度之间）。
	 * @param y
	 * @param x
	 * @return
	 */
	public static Object getAtan2(Double y,Double x){
		Object obj =null;
		try {
			engine.put("y", y);
			engine.put("x", x);
			obj= engine.eval("Math.atan2(y,x)");
			return obj;
		}catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 对数进行上舍入。
	 * @param parm
	 * @return
	 */
	public static Object getCeil(Double parm){
		Object obj =null;
		try {
			engine.put("parm", parm);
			obj= engine.eval("Math.ceil(parm)");
			return obj;
		}catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 返回数的余弦。
	 * @param parm
	 * @return
	 */
	public static Object getCos(Double parm){
		Object obj =null;
		try {
			engine.put("parm", parm);
			obj= engine.eval("Math.cos(parm)");
			return obj;
		}catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 返回 e 的指数。
	 * @param parm
	 * @return
	 */
	public static Object getExp(Double parm){
		Object obj =null;
		try {
			engine.put("parm", parm);
			obj= engine.eval("Math.exp(parm)");
			return obj;
		}catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 对数进行下舍入。
	 * @param parm
	 * @return
	 */
	public static Object getFloor(Double parm){
		Object obj =null;
		try {
			engine.put("parm", parm);
			obj= engine.eval("Math.floor(parm)");
			return obj;
		}catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 返回数的自然对数（底为e)
	 * @param parm
	 * @return
	 */
	public static Object getLog(Double parm){
		Object obj =null;
		try {
			engine.put("parm", parm);
			obj= engine.eval("Math.log(parm)");
			return obj;
		}catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 返回 x 的 y 次幂。
	 * @param x
	 * @param y
	 * @return
	 */
	public static Object getPow(Double x, Double y){
		Object obj =null;
		try {
			engine.put("x", x);
			engine.put("y", y);
			obj= engine.eval("Math.pow(x,y)");
			return obj;
		}catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 返回 x 和 y 中的最高值。
	 * @param x
	 * @param y
	 * @return
	 */
	public static Object getMax(Double x,Double y){
		Object obj =null;
		try {
			engine.put("x", x);
			engine.put("y", y);
			obj= engine.eval("Math.max(x,y)");
			return obj;
		}catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 返回 x 和 y 中的最低值。
	 * @param x
	 * @param y
	 * @return
	 */
	public static Object getMin(Double x,Double y){
		Object obj =null;
		try {
			engine.put("x", x);
			engine.put("y", y);
			obj= engine.eval("Math.min(x,y)");
			return obj;
		}catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 返回 0 ~ 1 之间的随机数。
	 * @return
	 */
	public static Object getRandom(){
		Object obj =null;
		try {
			obj= engine.eval("Math.random()");
			return obj;
		}catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 把数四舍五入为最接近的整数。
	 * @param parm
	 * @return
	 */
	public static Object getRound(Double parm){
		Object obj =null;
		try {
			engine.put("parm", parm);
			obj= engine.eval("Math.round(parm)");
			return obj;
		} catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 返回数的正弦
	 * @param x
	 * @return
	 */
	public static Object getSin(Double x){
		Object obj =null;
		try {
			engine.put("x", x);
			obj= engine.eval("Math.sin(x)");
			return obj;
		}catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 返回数的平方根。
	 * @param x
	 * @return
	 */
	public static Object getSqrt(Double x){
		Object obj =null;
		try {
			engine.put("x", x);
			obj= engine.eval("Math.sqrt(x)");
			return obj;
		}catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	
	/**
	 * 返回角的正切。
	 * @param x
	 * @return
	 */
	public static Object getTan(Double x){
		Object obj =null;
		try {
			engine.put("x", x);
			obj= engine.eval("Math.tan(x)");
			return obj;
		}catch(ScriptException e){
			log.info(getLogStr(e.getMessage()));
		}catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
		}
		return obj;
	}
	


//	@Test
//	public void testJs() {
//		try {
//			String str = "1/0";
//	     	Object obj= engine.eval(str);
//	     	System.out.println(obj.toString());
//	      String st12 =	new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
//	     	System.out.println(st12);
//			Object obj = numericalCalculation("1*9)");
//			System.out.println(obj);
//			
//			String str ="(1 + $a) * 2";
//			String str ="Math.abs(a)";
//			
//			String str ="Math.round(5*0.9*0.59*100)/100";
//			String str ="Math.round((1/3)*1000000)/1000000";
//			engine.put("$a", -3);
//			engine.put("b", 3);
//			engine.put("a", 4);
//			engine.put("c", 5);
			
//			Object obj= engine.eval(str);
//			System.out.println(obj.toString());
//			Object obj= numericalCalculation("5*0.9*0.59");
//			String formulaValue,Double $Count,Double $PeakCount,Double $VallCount,Double $CommCount
//			Object obj = waterElectCalculation("$Count*0.9*0.59",5.0,0.0,0.0,0.0);
//			System.out.println(obj.toString());
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//		
//	}
	
	
	
	
}
