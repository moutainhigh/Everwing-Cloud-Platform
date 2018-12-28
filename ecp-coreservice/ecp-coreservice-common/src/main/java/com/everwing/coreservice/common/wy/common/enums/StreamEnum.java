package com.everwing.coreservice.common.wy.common.enums;

public enum StreamEnum {

	//扣费
	purpose_billing("当月扣费"),
	
	//违约金扣费
	purpose_billing_by_latefee("扣除违约金"),
	
	//抵扣
	purpose_dk_to_wy("抵扣物业管理费"),
	purpose_dk_to_bt("抵扣本体基金"),
	purpose_dk_to_water("抵扣水费"),
	purpose_dk_to_elect("抵扣电费"),
	
	//充值
	purpose_pay_by_person("人为充值"),
	
	//托收充值
	purpose_pay_by_collection("托收充值"),
	
	//通用账户充值
	purpose_pay_by_common("通用账户充值"),
	
	//退款
	purpose_back("退款"),
	
	//减免
	purpose_jm("减免"),
	
	//回滚
	purpose_rollback("回滚");
	
	
	private String v;
	
	StreamEnum(){}
	StreamEnum(String v){this.v = v;}
	
	public String getV(){
		return v;
	}
}
