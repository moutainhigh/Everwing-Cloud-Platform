
package com.everwing.coreservice.common.wy.common.enums;

/**
 * @describe 银账交割枚举类
 * @author DELL
 * @date 2017-09-05
 */
public enum SettlementEnum {

	BUSINESS_ACCOUNT_RECEIVABLE(101),	//收账
	BUSINESS_ACCOUNT_GIVE(102),	//交账
	BUSINESS_ACCOUNT_TOTAL(103),//收银员结算
	BUSINESS_ACCOUNT_TOTAL_ZZ(104),//收银组长交账到出纳
	BUSINESS_ACCOUNT_TOTAL_PRODUCT(105),//产品
	BUSINESS_ACCOUNT_TOTAL_DEPOSIT_BACK(106),//退押金
	BUSINESS_PAY_INFO(107),//前台收款
	BUSINESS_ACCOUNT_Finance_CN(108),
	
	ROLE_LEVEL_KJ(1),	//会计
	ROLE_LEVEL_CN(2),	//出纳
	ROLE_LEVEL_SYZZ(3),	//收银组长
	ROLE_LEVEL_SYY(4),	//收银员
	
	status_completed(1),	//已结算
	status_waiting(2),		//待结算
	status_backed(3);		//已退款
	


	private Integer intValue;
	
	SettlementEnum(){}
	
	SettlementEnum(Integer v){
		this.intValue=v;
	}
	
	public Integer getIntValue(){
		return intValue;
	}
	
}
