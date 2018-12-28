package com.everwing.coreservice.common.wy.fee.constant;

/**
 *资产滞纳金（违约金）业务类型，枚举
 *
 * @author qhc
 * @create 2018/5/22
 */
public enum LatefeeBusinessTypeEnum {

	PAY_LATE_FEE(1,"交费违约金"),  //前台交费，交了违约金

	SPECIAL_ACCOUNT_DEDUCTIBLE_LATE_FEE(2,"专项账户进行了违约金的抵扣"),

	COMMON_ACCOUNT_DEDUCTIBLE_LATE_FEE(3,"通用账户进行违约金的抵扣"),
	
	PRODUCE_LATE_FEE(4,"产生违约金"),
	
	ROLLBACK_LATE_FEE(5,"回退交费的违约金");
	

    private int code;

    private String desc;

    LatefeeBusinessTypeEnum(int code,String desc){
        this.code=code;
        this.desc=desc;
    }

    public int getCode(){
        return code;
    }
    
    public String getDesc(){
        return desc;
    }
}
