package com.everwing.coreservice.common.platform.constant;

/**
 * @description 数据库数据字典
 * @author MonKong
 * @date 2016年12月20日
 */
public enum Dict {
	/*----- Platform START -----*/
	SMS_TYPE_VERIFY_CODE(0), // 通用验证码
	
	ROLE_STATE_ABLE(1), // 可用
	ROLE_STATE_UNABLE(0), // 不可用

	ACCOUNT_STATE_NORMAL(1), // 正常
	ACCOUNT_STATE_CANCEL(0), // 注销
	
	ACCOUNT_LOG_TYPE_ADD(1), // 增加
	ACCOUNT_LOG_TYPE_DEL(2), // 删除
	ACCOUNT_LOG_TYPE_UPDATE(3), // 更新
	ACCOUNT_LOG_TYPE_QUERY(4), // 查询

	ACCOUNT_TYPE_STAFF(0), // 员工
	ACCOUNT_TYPE_HOUSE(1), // 房屋
	ACCOUNT_TYPE_LY_USER(2), // 铃音用户
	ACCOUNT_TYPE_ANDROID_MKJ(3), // 安卓门控机
	ACCOUNT_TYPE_IOS_MKJ(4), // IOS门控机
	ACCOUNT_TYPE_CUSTOMER(5), // 客户
	ACCOUNT_TYPE_ADMIN(6), // 后台
//	ACCOUNT_TYPE_COMPANY_SUPER_ADMIN(7), // 公司超级管理员

	EXDATA_ATTR_KEY_IOS_PRO_APP_TOKEN("ios_pro_app_token"), // 账号exdata中的属性字段名
	EXDATA_ATTR_KEY_IOS_ENT_APP_TOKEN("ios_ent_app_token"), // 账号exdata中的属性字段名
	EXDATA_ATTR_KEY_CURR_IOS_TYPE("curr_ios_type"), // 当前IOS使用的推送类型
	EXDATA_ATTR_CURR_PUSH_TYPE_ENT("ent"), // 企业版
	EXDATA_ATTR_CURR_PUSH_TYPE_DEV("dev"), // 开发版
	EXDATA_ATTR_CURR_PUSH_TYPE_PRO("pro"), // 生产版
	/*----- Platform END -----*/
	
	
	/*----- Admin START -----*/
	COMPANY_APPROVAL_STATUS_FAIL(0),
	COMPANY_APPROVAL_STATUS_SUCCESS(1),
	COMPANY_APPROVAL_STATUS_PROCESSING(2),//审核中
	/*----- Admin END -----*/

	ACCOUNT_DEFAULT_PSW("");// 账号默认密码

	Dict(Object value) {
		this.value = value;
	}

	private Object value;

	public String getStringValue() {
		return (String) value;
	}

	public int getIntValue() {
		return (int) value;
	}
	
	public Object getValue() {
		return value;
	}

	/**
	 * @warning 此方法需要根据ACCOUNT_TYPE的值域变化跟着改动。
	 * 检查传入type在系统中是否合理
	 * @param type 传入的账号type
	 * @return
	 */
	public static boolean checkAccountType(int type){
		if(type>-1&&type<9){
			return true;
		}
		return false;
	}
}
