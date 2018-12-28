package com.everwing.coreservice.common.wy.common.enums;

public enum CollectionEnum {

	//通用账户扣取
	common_account_stop(0),		//关闭通用账户
	common_account_using(1),	//开启通用账户	
	
	//状态 
	status_on(0),			//开启
	status_off(1),			//关闭
	
	//托收收费项
	collection_item_wy(1),	//物业管理费
	collection_item_bt(2),	//本体基金
	collection_item_water(3),	//水费
	collection_item_elect(4),	//电费
	
	
	//托收状态
	type_union(0),	//银联
	type_jrl(1),	//金融联
	type_off(2),	//未开启
	
	//卡类型
	type_is_card(0), 		//卡
	type_is_not_card(1),	//折
	
	//托收状态
	colleciton_status_wait(0),		//待托收
	collection_status_wait_back(1),	//待回盘
	collection_status_complete(2),	//完成
	
	//金融联 的 银行卡异地标识
	jrl_is_local(0),		//本地
	jrl_is_not_local(1),	//异地
	
	//金融联头文件类型
	jrl_is_collection(0),	//金融联托收
	jrl_is_back(1),			//金融联回盘
	
	//金融联业务类型
	jrl_business_type("304"),				//批量代收,固定值
	
	//金融联费项代码 固定值 703
	jrl_fee_no("00703"),
	
	
	//金融联币种  固定值 CNY
	jrl_fee_type_RMB("CNY"),
	
	//金融联业务编码 固定值 30401
	jrl_business_no_RMB("30401"),
	
	//回盘文件响应码
	union_back_file_resp_success("00"),		//完成

	//回盘文件交易状态码
	union_back_file_trade_status_success("1001"),		//交易成功
	
	//金融联回盘文件 扣款成功 固定值 FSBR0000
	jrl_back_file_trade_status_success("FSBR0000"),
	
	//货币类型
	fee_type_rmb("CNY"),		//人民币
	fee_type_gb("HKD"),			//港币
	fee_type_dollar("USD"),		//美元
	
	//持卡人证件类型
	id_card_type_is_id("01"),		//身份证
	id_card_type_is_jg("02"),		//军官证
	id_card_type_is_port("03"),		//护照
	id_card_type_is_hk("04"),		//户口簿
	id_card_type_is_hx("05"),		//回乡证
	id_card_type_is_other("06"),	//其他
	
	
	//银联阈值
	threshold_union_code_in_lookup("unionMaxAmount"),
	//金融联阈值在数据字典中的code
	threshold_jrl_code_in_lookup("jrlMaxAmount");
	
	
	private Integer v;
	private String stringV;
	CollectionEnum(){}
	
	CollectionEnum(Integer v){this.v = v;}
	CollectionEnum(String v){this.stringV = v;}
	
	
	public Integer getV(){
		return this.v;
	}
	
	public String getStringV(){
		return this.stringV;
	}
}
