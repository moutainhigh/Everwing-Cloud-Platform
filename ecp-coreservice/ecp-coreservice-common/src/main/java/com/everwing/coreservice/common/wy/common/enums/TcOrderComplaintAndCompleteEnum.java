package com.everwing.coreservice.common.wy.common.enums;

public enum TcOrderComplaintAndCompleteEnum {

	//普通是否
	COMMON_YES(0), //是
	COMMON_NO(1), //否
	
	//工单类型
	ORDER_TYPE_CHANGE_ASSETS(1), //:资产变更抄表
	ORDER_TYPE_METER_READING(0), //水电抄表
	//预留字段类型一
	ORDER_TYPE_ONE_WATER(0), //水表
	ORDER_TYPE_ONE_ELECT(1), //电表
	
	//预留字段类型二
	ORDER_TYPE_TWO_ERRORBILL(0), //计费错误
	ORDER_TYPE_TWO_READING(1), //抄表
	
	//工单来源
	ORDER_SOURCE_CUSTOMER_SERVICE(0), //客服
	ORDER_SOURCE_CC(1), //CC
	
	IS_BILLING_YES(0), //已经计费
	IS_BILLING_NO(1), //未计费
	
	//处理状态
	ORDER_STATUS_NOACCEPT(0), //未接受
	ORDER_STATUS_HANDLING(1), //处理中
	ORDER_STATUS_COMPLETE(2), //已完成
	ORDER_STATUS_DESPOSE(3); //作废
	private Integer value;
	
	private TcOrderComplaintAndCompleteEnum() {
	}

	private TcOrderComplaintAndCompleteEnum(Integer value) {
		this.value = value;
	}

	public Integer getIntV(){
		return value;
	}
}
