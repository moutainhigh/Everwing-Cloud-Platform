package com.everwing.coreservice.wy.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 操作Excel工具类
 * @author shaozheng
 *	2015-9-11
 */
public class ExcelUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExcelUtils.class);
	
	private POIFSFileSystem fs;
	private HSSFWorkbook wb;
	private HSSFSheet sheet;
	private HSSFRow row;
	
	private XSSFWorkbook xb;
	private XSSFSheet xsheet;
	private XSSFRow xrow;
	
	/**
     * 读取Excel表格表头的内容
     * @param InputStream
     * @return String 表头内容的数组
     */
    public String[] readExcelTitle(InputStream is) {
        try {
            fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = wb.getSheetAt(0);
        row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        System.out.println("colNum:" + colNum);
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            //title[i] = getStringCellValue(row.getCell((short) i));
            title[i] = getCellFormatValue(row.getCell((short) i));
        }
        return title;
    }
    
    /**
     * 读取excel数据的内容
     * @param filePath 文件的相对路径
     * @return
     * @throws FileNotFoundException 
     * @throws IOException
     * @throws URISyntaxException 
     */
    public List<Map<Short,String>> readExcel(String filePath) throws FileNotFoundException, IOException, URISyntaxException{
    	String str = getClass().getClassLoader().getResource("").toURI().getPath();
    	
    	
//    	String classPath ="/" + str.substring(str.indexOf("/", 0)+1, str.lastIndexOf("WEB-INF/")) + filePath;  //获取文件服务器下上传的文件,读取文件并获取Excel
    	String classPath = "/" + str.substring(str.indexOf("/", 0)+1, str.lastIndexOf("classes/")) + "classes/upload/wypt_20170216_Monkong7/2017_03_22/aaa.xls";
    	

    	List<Map<Short, String>> list = new ArrayList<Map<Short, String>>();
    	Map<Short, String> content = null;
    	LOG.info("=============== old path = " + classPath.replace("\\", "/"));
    	classPath = classPath.replace("\\", "/").replaceAll("//", "/");
    	LOG.info("=============== replace path = " + classPath);
    	//1.得到Excel常用对象 
    	FileInputStream fis = new FileInputStream(classPath);
    	POIFSFileSystem fs = new POIFSFileSystem(fis);
		//2.得到Excel工作簿对象 
    	HSSFWorkbook  wb = new HSSFWorkbook(fs);
        //3.得到Excel工作表对象 
        HSSFSheet sheet = wb.getSheetAt(0); 
        //总行数 
        int trLength = sheet.getLastRowNum(); 
        //4.得到Excel工作表的行 
        HSSFRow row = sheet.getRow(0); 
        //总列数 
        int tdLength = row.getLastCellNum(); 
        for(int i=1;i<=trLength;i++){
        	content = new HashMap<Short, String>();
        	//得到Excel工作表的行 
        	HSSFRow row1 = sheet.getRow(i);
        	short j = 0;
        	HSSFCell tmpCell = null;
	      	try{
		      	tmpCell = row1.getCell((short) 0);
	      	}catch(Exception e){
	      		continue;
	      	}
	      	if(tmpCell==null){
	      		continue;
	      	}
        	String validData = getCellFormatValue(tmpCell);
        	if(StringUtils.isEmpty(validData.trim())){
        		continue;
        	}else{
        		while (j < tdLength) {
            		HSSFCell cell1 = row1.getCell(j);
            		String value = getCellFormatValue(cell1);
            		content.put(j, value);
                    j++;
                }
        	}
        	list.add(content);
        }
    	return list;
    }
    
    /**
     * 
     * @param filePath  绝对路径
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws URISyntaxException
     */
    public List<Map<Short,String>> readExcelXlsAbsPath(String absPath) throws FileNotFoundException, IOException, URISyntaxException{
    	
    	List<Map<Short, String>> list = new ArrayList<Map<Short, String>>();
    	Map<Short, String> content = null;
    	LOG.info("=============== old path = " + absPath.replace("\\", "/"));
    	absPath = absPath.replace("\\", "/").replaceAll("//", "/");
    	LOG.info("=============== replace path = " + absPath);
    	//1.得到Excel常用对象 
    	FileInputStream fis = new FileInputStream(absPath);
    	POIFSFileSystem fs = new POIFSFileSystem(fis);
    	//2.得到Excel工作簿对象 
    	HSSFWorkbook  wb = new HSSFWorkbook(fs);
    	//3.得到Excel工作表对象 
    	HSSFSheet sheet = wb.getSheetAt(0); 
    	//总行数 
    	int trLength = sheet.getLastRowNum(); 
    	//4.得到Excel工作表的行 
    	HSSFRow row = sheet.getRow(0); 
    	//总列数 
    	int tdLength = row.getLastCellNum(); 
    	for(int i=1;i<=trLength;i++){
    		content = new HashMap<Short, String>();
    		//得到Excel工作表的行 
    		HSSFRow row1 = sheet.getRow(i);
    		short j = 0;
    		HSSFCell tmpCell = null;
    		try{
    			tmpCell = row1.getCell((short) 0);
    		}catch(Exception e){
    			continue;
    		}
    		if(tmpCell==null){
    			continue;
    		}
    		String validData = getCellFormatValue(tmpCell);
    		if(StringUtils.isEmpty(validData.trim())){
    			continue;
    		}else{
    			while (j < tdLength) {
    				HSSFCell cell1 = row1.getCell(j);
    				String value = getCellFormatValue(cell1);
    				content.put(j, value);
    				j++;
    			}
    		}
    		list.add(content);
    	}
    	return list;
    }
    
    
    
    
    
    /**
     * 读取excel数据的内容
     * @param filePath 绝对路径
     * @return
     * @throws FileNotFoundException 
     * @throws IOException
     * @throws URISyntaxException 
     */
    public List<Map<Short,String>> readExcelByFilePath(InputStream input) throws FileNotFoundException, IOException, URISyntaxException{
//    	String str = getClass().getClassLoader().getResource("").toURI().getPath();
    	

    	List<Map<Short, String>> list = new ArrayList<Map<Short, String>>();
    	Map<Short, String> content = null;
    	//1.得到Excel常用对象 
//    	File f = new File(classPath);
    	XSSFWorkbook xwb = new XSSFWorkbook(input);  
        //3.得到Excel工作表对象 
        XSSFSheet xsheet = xwb.getSheetAt(0); 
        //总行数 
        int trLength = xsheet.getLastRowNum(); 
        //4.得到Excel工作表的行 
        XSSFRow row = xsheet.getRow(0); 
        //总列数 
        int tdLength = row.getLastCellNum(); 
        for(int i=1;i<=trLength;i++){
        	boolean fFlag = false;
        	content = new HashMap<Short, String>();
        	//得到Excel工作表的行 
        	XSSFRow row1 = xsheet.getRow(i);
        	XSSFCell tmpCell = null;
	      	try{
		      	tmpCell = row1.getCell((short) 0);
	      	}catch(Exception e){
	      		continue;
	      	}
	      	if(tmpCell==null){
	      		continue;
	      	}
        	short j = 0;
        	while (j < tdLength) {
        		XSSFCell cell1 = row1.getCell(j);
        		if(StringUtils.isEmpty(getCellFormatValueXlsx(row1.getCell(0)))){
        			fFlag = true;
        			break;
            	}
        		String value = getCellFormatValueXlsx(cell1);
        		content.put(j, value);
                j++;
            }
        	if(fFlag){
        		continue;
        	}
        	list.add(content);
        }
    	return list;
    }
    
    
    
    /**
     * 读取excel数据的内容
     * @param filePath 文件的相对路径,sheetPage 读取第几个sheet页。
     * @return
     * @throws FileNotFoundException 
     * @throws IOException
     * @throws URISyntaxException 
     */
	  public List<Map<Short,String>> readExcel(String filePath,int sheetPage) throws FileNotFoundException, IOException, URISyntaxException{
	  	
		  if(sheetPage<0){
			  return new ArrayList<Map<Short, String>>(); 
		  }
		String str = getClass().getClassLoader().getResource("").toURI().getPath();
	  	String classPath ="/" + str.substring(str.indexOf("/", 0)+1, str.lastIndexOf("WEB-INF/")) + filePath;
	//  	System.out.println(classPath.replace("\\", "/"));
	  	List<Map<Short, String>> list = new ArrayList<Map<Short, String>>();
	  	Map<Short, String> content = null;
	  	//1.得到Excel常用对象 
	      POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(classPath.replace("\\", "/"))); 
	      //2.得到Excel工作簿对象 
	      HSSFWorkbook wb = new HSSFWorkbook(fs); 
	      //3.得到Excel工作表对象 
	      HSSFSheet sheet = wb.getSheetAt(sheetPage); 
	      //总行数 
	      int trLength = sheet.getLastRowNum(); 
	      //4.得到Excel工作表的行 
	      HSSFRow row = sheet.getRow(0); 
	      //总列数 
	      int tdLength = row.getLastCellNum(); 
	      for(int i=1;i<=trLength;i++){
	      	content = new HashMap<Short, String>();
	      	//得到Excel工作表的行 
	      	HSSFRow row1 = sheet.getRow(i);
	      	short j = 0;
	      	HSSFCell tmpCell = null;
	      	try{
		      	tmpCell = row1.getCell((short) 0);
	      	}catch(Exception e){
	      		continue;
	      	}
	      	if(tmpCell==null){
	      		continue;
	      	}
	      	String validData = getCellFormatValue(tmpCell);
	      	if(StringUtils.isEmpty(validData.trim())){
	      		continue;
	      	}else{
	      		while (j < tdLength) {
	          		HSSFCell cell1 = row1.getCell(j);
	          		String value = getCellFormatValue(cell1);
	          		content.put(j, value);
	                  j++;
	              }
	      	}
	      	list.add(content);
	      }
	  	return list;
	  }
    
    public List<Map<Short,String>> readExcelXlsx(String filePath) throws FileNotFoundException, IOException, URISyntaxException, EncryptedDocumentException, InvalidFormatException{
    	String str = getClass().getClassLoader().getResource("").toURI().getPath();
    	String classPath = str.substring(str.indexOf("/", 0)+1, str.lastIndexOf("WEB-INF/")) + filePath;
//    	System.out.println(classPath.replace("\\", "/"));
    	List<Map<Short, String>> list = new ArrayList<Map<Short, String>>();
    	Map<Short, String> content = null;
    	//1.得到Excel常用对象 
    	File f = new File(classPath.replace("\\", "/"));
    	XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(f));  
        //3.得到Excel工作表对象 
        XSSFSheet xsheet = xwb.getSheetAt(0); 
        //总行数 
        int trLength = xsheet.getLastRowNum(); 
        //4.得到Excel工作表的行 
        XSSFRow row = xsheet.getRow(0); 
        //总列数 
        int tdLength = row.getLastCellNum(); 
        for(int i=1;i<=trLength;i++){
        	boolean fFlag = false;
        	content = new HashMap<Short, String>();
        	//得到Excel工作表的行 
        	XSSFRow row1 = xsheet.getRow(i);
        	XSSFCell tmpCell = null;
	      	try{
		      	tmpCell = row1.getCell((short) 0);
	      	}catch(Exception e){
	      		continue;
	      	}
	      	if(tmpCell==null){
	      		continue;
	      	}
        	short j = 0;
        	while (j < tdLength) {
        		XSSFCell cell1 = row1.getCell(j);
        		if(StringUtils.isEmpty(getCellFormatValueXlsx(row1.getCell(0)))){
        			fFlag = true;
        			break;
            	}
        		String value = getCellFormatValueXlsx(cell1);
        		content.put(j, value);
                j++;
            }
        	if(fFlag){
        		continue;
        	}
        	list.add(content);
        }
    	return list;
    }
    
    /**
     * 读取excel数据的内容
     * @param filePath 文件的相对路径,sheetPage 读取第几个sheet页。
     * @return
     * @throws FileNotFoundException 
     * @throws IOException
     * @throws URISyntaxException 
     */
    public List<Map<Short,String>> readExcelXlsx(String filePath,int sheetIndex) throws FileNotFoundException, IOException, URISyntaxException, EncryptedDocumentException, InvalidFormatException{
    	if(sheetIndex<0){
			  return new ArrayList<Map<Short, String>>(); 
		}
    	String str = getClass().getClassLoader().getResource("").toURI().getPath();
    	String classPath = str.substring(str.indexOf("/", 0)+1, str.lastIndexOf("WEB-INF/")) + filePath;
//    	System.out.println(classPath.replace("\\", "/"));
    	List<Map<Short, String>> list = new ArrayList<Map<Short, String>>();
    	Map<Short, String> content = null;
    	//1.得到Excel常用对象 
    	File f = new File(classPath.replace("\\", "/"));
    	XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(f));  
        //3.得到Excel工作表对象 
        XSSFSheet xsheet = xwb.getSheetAt(sheetIndex); 
        //总行数 
        int trLength = xsheet.getLastRowNum(); 
        //4.得到Excel工作表的行 
        XSSFRow row = xsheet.getRow(0); 
        //总列数 
        int tdLength = row.getLastCellNum(); 
        for(int i=1;i<=trLength;i++){
        	boolean fFlag = false;
        	content = new HashMap<Short, String>();
        	//得到Excel工作表的行 
        	XSSFRow row1 = xsheet.getRow(i);
        	XSSFCell tmpCell = null;
	      	try{
		      	tmpCell = row1.getCell((short) 0);
	      	}catch(Exception e){
	      		continue;
	      	}
	      	if(tmpCell==null){
	      		continue;
	      	}
        	short j = 0;
        	while (j < tdLength) {
        		XSSFCell cell1 = row1.getCell(j);
        		if(StringUtils.isEmpty(getCellFormatValueXlsx(row1.getCell(0)))){
        			fFlag = true;
        			break;
            	}
        		String value = getCellFormatValueXlsx(cell1);
        		content.put(j, value);
                j++;
            }
        	if(fFlag){
        		continue;
        	}
        	list.add(content);
        }
    	return list;
    }
    
    private String getCellFormatValueXlsx(XSSFCell cell1) {
    	DecimalFormat df = new DecimalFormat("0");  
        String cellvalue = "";
        if (cell1 != null) {
            // 判断当前Cell的Type
            switch (cell1.getCellType()) {
            // 如果当前Cell的Type为NUMERIC
            case XSSFCell.CELL_TYPE_NUMERIC:
        		cellvalue = df.format(cell1.getNumericCellValue());
            	break;
            case XSSFCell.CELL_TYPE_FORMULA: {
               /* // 判断当前的cell是否为Date
                if (DateUtil.isCellDateFormatted(cell1)) {
                    // 如果是Date类型则，转化为Data格式
                    
                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    //cellvalue = cell.getDateCellValue().toLocaleString();
                    
                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell1.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellvalue = sdf.format(date);
                    
                }
                // 如果是纯数字
                else {
                    // 取得当前Cell的数值
                    cellvalue = String.valueOf((int)cell1.getNumericCellValue());
                }*/
            	BigDecimal bigula = new BigDecimal(cell1 
            		      .getCachedFormulaResultType()); 
            		    cellvalue = bigula.toString(); 
                break;
            }
            // 如果当前Cell的Type为STRIN
            case XSSFCell.CELL_TYPE_STRING:
                // 取得当前的Cell字符串
                cellvalue = cell1.getRichStringCellValue().getString();
                break;
            // 默认的Cell值
            default:
                cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
	}

	/**
     * 获取单元格数据内容为字符串类型的数据
     * 
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private static String getStringCellValue(HSSFCell cell) {
        String strCell = "";
        DecimalFormat df = new DecimalFormat("0");
        switch (cell.getCellType()) {
        case HSSFCell.CELL_TYPE_STRING:
            strCell = cell.getStringCellValue();
            break;
        case HSSFCell.CELL_TYPE_NUMERIC:
            strCell = String.valueOf(cell.getNumericCellValue());
            break;
        case HSSFCell.CELL_TYPE_BOOLEAN:
            strCell = String.valueOf(cell.getBooleanCellValue());
            break;
        case HSSFCell.CELL_TYPE_BLANK:
            strCell = "";
            break;
        default:
            strCell = "";
            break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        if (cell == null) {
            return "";
        }
        return strCell;
    }
    
    /**
     * 获取单元格数据内容为日期类型的数据
     * 
     * @param cell
     *            Excel单元格
     * @return String 单元格数据内容
     */
    private String getDateCellValue(HSSFCell cell) {
        String result = "";
        try {
            int cellType = cell.getCellType();
            if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
                Date date = cell.getDateCellValue();
                result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1)
                        + "-" + date.getDate();
            } else if (cellType == HSSFCell.CELL_TYPE_STRING) {
                String date = getStringCellValue(cell);
                result = date.replaceAll("[年月]", "-").replace("日", "").trim();
            } else if (cellType == HSSFCell.CELL_TYPE_BLANK) {
                result = "";
            }
        } catch (Exception e) {
            System.out.println("日期格式不正确!");
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 根据HSSFCell类型设置数据
     * @param cell
     * @return
     */
    private static String getCellFormatValue(HSSFCell cell) {
    	DecimalFormat dformat = new DecimalFormat("0.00"); 
    	DecimalFormat iformat = new DecimalFormat("0");
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
            // 如果当前Cell的Type为NUMERIC
            case HSSFCell.CELL_TYPE_FORMULA: {
                // 判断当前的cell是否为Date
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 如果是Date类型则，转化为Data格式
                    
                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    //cellvalue = cell.getDateCellValue().toLocaleString();
                    
                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellvalue = sdf.format(date);
                    
                }
                // 如果是纯数字
                else {
                    // 取得当前Cell的数值
                    cellvalue = String.valueOf((int)cell.getNumericCellValue());
                }
                break;
            }
            case HSSFCell.CELL_TYPE_NUMERIC:
            	if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 如果是Date类型则，转化为Data格式
                    
                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    //cellvalue = cell.getDateCellValue().toLocaleString();
                    
                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellvalue = sdf.format(date);
                    
                }else{
                	try{
                		
                		String tmp = cell.getNumericCellValue()+"";
                		
                		String ints[] = tmp.split("\\.");
                		
                		if(ints.length==2){
                			
                			/*if(Long.parseLong(ints[1])==0L){
            					cellvalue = iformat.format(cell.getNumericCellValue());
            				}else{
            					cellvalue = dformat.format(cell.getNumericCellValue());
            				}*/
                			
      					  
        					if(ints[1].contains("E")){
        						cellvalue = iformat.format(cell.getNumericCellValue());
        					}else{
        						cellvalue = dformat.format(cell.getNumericCellValue());
        					}
                		}else if(ints.length==1){
                			cellvalue = dformat.format(cell.getNumericCellValue());
                		}
                	}catch(Exception ex){
                		ex.printStackTrace();
                	}
                	
                }
            	break;
            // 如果当前Cell的Type为STRIN
            case HSSFCell.CELL_TYPE_STRING:
                // 取得当前的Cell字符串
//            	cellvalue=cell.getStringCellValue();
                cellvalue = cell.getRichStringCellValue().getString();
                break;
            // 默认的Cell值
            default:
                cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }
    
    
    /**
	 * 
	 * @param in
	 * @return
	 */
	public String[] getExcelTitleXlsx(InputStream in) {
		try {
			xb = new XSSFWorkbook(in);
			xsheet = xb.getSheetAt(0);
			//获取行数
			xrow = xsheet.getRow(0);
			//获取列数
			int columnNum = 0;
			columnNum = xrow.getPhysicalNumberOfCells();
			String[] title = new String[columnNum];
			for (int i = 0; i < columnNum; i++) {
				title[i] = getCellFormatValueXlsx(xrow.getCell(i));
			}
			return title;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void addRow(Sheet sheet, int rowIndex, Object... data) {
		Row row = sheet.createRow(rowIndex);
		for (int i = 0; i < data.length; i++) {
			row.createCell(i).setCellValue(data[i] + "");
		}
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	public String[] getExcelTitleXlsx(String path) {
		try {
			xb = new XSSFWorkbook(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



	/**
	 * 
	 * @param in 输入字节流
	 * @param clazz  类对象
	 * @param map  类中字段和标题对应保存的map  value为方法名
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Object> readExcelContentXlsx(InputStream in,Class clazz,Map<String,String> map){
		List<Object> list = new ArrayList<Object>();
		//Field[] fields = clazz.getDeclaredFields();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Method[] methods = clazz.getDeclaredMethods();
		try {
			xb = new XSSFWorkbook(in);
			xsheet = xb.getSheetAt(0);
			int totalRows = xsheet.getLastRowNum();
			xrow = xsheet.getRow(0);
			int totalColumn = xrow.getPhysicalNumberOfCells();
			for (int i = 1; i < totalRows; i++) {
				Object t = clazz.newInstance();
				xrow = xsheet.getRow(i);
				for (int j = 0; j < totalColumn; j++) {
					XSSFCell cell = xrow.getCell(j);
					String cellValue = getCellFormatValueXlsx(cell);
					for (Method method : methods) {
						if(map.get(j).equals(method.getName())){
							Class[] ctype = method.getParameterTypes();
							if(ctype!= null && ctype.length >0){
								Class thisType = ctype[0];
								if(thisType == String.class){
									method.invoke(t, cellValue);
								}
								if(thisType == Integer.class){
									method.invoke(t, Integer.valueOf(cellValue));
								}
								if(thisType == Double.class){
									method.invoke(t, Double.valueOf(cellValue));								
								}
								if(thisType == Float.class){
									method.invoke(t, Float.valueOf(cellValue));
								}
								if(thisType == Date.class){
									method.invoke(t, sdf.parse(cellValue));
								}
							}
							break;
						}
					}
				}
				list.add(t);
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public HSSFWorkbook getWb() {
		return wb;
	}

	public void setWb(HSSFWorkbook wb) {
		this.wb = wb;
	}

	public HSSFSheet getSheet() {
		return sheet;
	}

	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}

	public HSSFRow getRow() {
		return row;
	}

	public void setRow(HSSFRow row) {
		this.row = row;
	}

	public XSSFWorkbook getXb() {
		return xb;
	}

	public void setXb(XSSFWorkbook xb) {
		this.xb = xb;
	}

	public XSSFSheet getXsheet() {
		return xsheet;
	}

	public void setXsheet(XSSFSheet xsheet) {
		this.xsheet = xsheet;
	}

	public XSSFRow getXrow() {
		return xrow;
	}

	public void setXrow(XSSFRow xrow) {
		this.xrow = xrow;
	}
}
