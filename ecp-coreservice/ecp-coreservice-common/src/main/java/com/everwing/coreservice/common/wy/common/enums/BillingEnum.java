package com.everwing.coreservice.common.wy.common.enums;


/**
 * @TODO 收费计费模块用枚举
 *
 */
public enum BillingEnum {

	//启用停用
	STATUS_START(0),	//启用
	STATUS_STOP(1),		//停用
	
	//生效失效
	IS_USED_USING(0),			//方案启用
	IS_USED_STOP(1),			//方案停用
	IS_EFFECTIVE_YES(0),		//方案生效
	IS_EFFECTIVE_NO(1),			//方案失效
	
	//计费状态
	BILL_STATUS_NO(0), //未计费
	BILL_STATUS_WHOLE(1), //全部计费
	BILL_STATUS_APART(2), //部分计费
	
	//计费方式
	TYPE_AUTO(0),	//自动
	TYPE_MANUAL(1),	//手动
	TYPE_AGAIN(2),	//重新
	
	//是否重新计费
	IS_REBILLING_YES(0),	//是
	IS_REBILLING_NO(1),		//不是
	
	//审核状态
	AUDIT_STATUS_WAITING(0),	//待审核
	AUDIT_STATUS_REFUSED(2),	//拒绝
	AUDIT_STATUS_COMPELTE(1),	//通过
	AUDIT_STATUS_APART_COMPLETE(3), //部分审核通过
	
	
	//账户类型
	ACCOUNT_TYPE_COMMON(0),		//通用账户
	ACCOUNT_TYPE_WY(1),			//物业管理费账户
	ACCOUNT_TYPE_BT(2),			//本体基金账户
	ACCOUNT_TYPE_WATER(3),		//水费账户
	ACCOUNT_TYPE_ELECT(4),		//电费账户
	
	//计费方案类型
	SCHEME_TYPE_WY(1),			//物业方案
	SCHEME_TYPE_BT(2),			//本体方案
	SCHEME_TYPE_WATER(3),		//水费方案
	SCHEME_TYPE_ELECT(4),		//电费方案
	
	
	//违约金计算方式
	SCHEME_CLAC_TYPE_SIMPLE(0),			//单利
	SCHEME_CLAC_TYPE_CPX(1),			//复利
	
	//计费面积
	SCHEME_BILLING_AREA_JZ(0),		//建筑面积
	SCHEME_BILLING_AREA_TN(1),		//套内面积
	
	//常量类型
	CONSTANTS_WATER(0),		//水费常量
	CONSTANTS_ELECT(1),		//电费常量
	
	//计费状态
	PROJECT_BILLING_STATUS_STOP(0),				//未开启
	PROJECT_BILLING_STATUS_NOT_BILLING(1),		//未计费
	PROJECT_BILLING_STATUS_COMPLETE(2),			//计费完成
	PROJECT_BILLING_START(3),				//计费中
	PROJECT_BILLING_START_APARTCOMPLETE(4), //部分计费，主要针对产权变更的部分计费
	
	//分摊类型
	SHARE_TYPE_WY(0),		//物业
	SHARE_TYPE_WATER(1),	//水费
	SHARE_TYPE_ELECT(2),	//电费
	
	//分摊对象
	SHARE_USER_HU(0),		//户
	SHARE_USER_USED(1),		//用量
	
	//MQ用枚举
	manaul_billing(0),		//手动计费
	auto_billing(1),		//自动计费
	company_request(2),		//计费按公司分发请求
	share_company_request(3), //分摊计费按公司发请求
	
	data_insert(0),			//插入
	data_update(1),			//更新
	

	//水电费的分摊在一起,区分水电费
	SHARE_WATER_TYPE(0),     //水费
	SHARE_ELECT_TYPE(1),		//电费
	
	common_account_is_not_bill(0),  //通用账户未抵扣
	common_account_is_billed(1),	//通用账户已抵扣
	rebilling_deduct(0),	//扣取
	rebilling_back(1),		//退回
	rebilling_common(2),	//正常

	//账单是否生成
	bill_is_gen_no(0),		//账单未生成
	bill_is_gen_yes(1),		//账单已生成
	
	
	//账单是否打包完成
	bill_is_zip_no(0),		//账单未打包
	bill_is_zip_yes(1),		//账单已打包
	
	//水电表分摊类型
	SHARE_BY_HOUSEHOLD(0),     //按户分摊
	SHARE_BY_CONSUMPTION(1),  //用量
	
	
	//mq申明处
	mq_gen_bill_auto(0),					//自动生成账单
	mq_gen_bill_manaul_first(1),			//第一次生成账单
	mq_gen_bill_manaul_regen(2);			//重新生成账单
	
	
	
	
	
	private Integer value;
	BillingEnum(){}
	BillingEnum(Integer v){this.value = v;}
	
	public Integer getIntV(){
		return value;
	}
	
	
}
