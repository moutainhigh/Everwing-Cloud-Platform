package com.everwing.coreservice.common.wy.fee.constant;

/**
 * 资产收费结果明细表  》》 业务类型枚举
 *
 * @author qhc
 * @create 2018/5/22
 */
public enum AcChargeDetailBusinessTypeEnum {

	CHARGE(1,"计费"),
	
	SPECIAL_DK(2,"专项账户抵扣"),
	
    COMMON_DK(3,"通用账户抵扣"),

    PAY(4,"交费"),
    
    ROLLBACK(5,"回退");

    private int code;

    private String desc;

    AcChargeDetailBusinessTypeEnum(int code,String desc){
        this.code=code;
        this.setDesc(desc);
    }

    public static AcChargeDetailBusinessTypeEnum getAcBusinessTypeEnumByCode(int code){
    	if( 1 == code ) return AcChargeDetailBusinessTypeEnum.CHARGE;
    	else if ( 2 == code ) return AcChargeDetailBusinessTypeEnum.SPECIAL_DK;
    	else if ( 3 == code ) return AcChargeDetailBusinessTypeEnum.COMMON_DK;
    	else if ( 4 == code ) return AcChargeDetailBusinessTypeEnum.PAY;
    	else return AcChargeDetailBusinessTypeEnum.ROLLBACK;
    }

    public int getCode(){
        return code;
    }

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
