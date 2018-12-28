package com.everwing.coreservice.common.utils;

/**
 * @describe 数据字典枚举
 * @author QHC
 * @date 2017-05-20
 */
public enum DataDictionaryEnum {

	
	/**
	 * 常用的选择项：0：是， 1： 不是（否）
	 */
	CHOOSE_STATE_Y("是",0),
	CHOOSE_STATE_N("不是",1),
  
	/**
	 * 水电表状态
	 */
	METER_STATE_Q("启用",0),
	METER_STATE_T("停用",1),
  
	/**
	 * 使用性质0 商用, 1 民用 , 2 管理处
	 */
	USER_TYPE_S("商用",0),
	USER_TYPE_M("民用",1),
	USER_TYPE_G("管理处",2),
	
	/**
	 * 抄表方式: 0 室内 , 1 室外 , 2 远程
	 */
	READING_METER_TYPE_IN("室内",0),
	READING_METER_TYPE_OUT("室外",1),
	READING_METER_TYPE_FAR("远程",2);
	
	public static Integer getIndexByName(String name){
		if(CommonUtils.isEmpty(name)){
			return null;
		}
        for (DataDictionaryEnum enums : DataDictionaryEnum.values()){  
            if (enums.name.equals(name)){  
                return enums.getIndex();  
            }  
        }  
        
        return null;
	 } 
   	
   
    private final String name;  //字典描述
    private final int index;    //标识
  
    DataDictionaryEnum(String name, int index) {  
        this.name = name;  
        this.index = index;  
    }  
  
    public String getName() {  
        return name;  
    }  
  
    public int getIndex() {  
        return index;  
    }  
     

}
