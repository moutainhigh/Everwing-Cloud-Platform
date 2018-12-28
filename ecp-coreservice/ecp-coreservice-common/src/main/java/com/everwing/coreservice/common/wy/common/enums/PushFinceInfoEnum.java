
package com.everwing.coreservice.common.wy.common.enums;

/**
 * @describe 推送财务接口会用到的一些枚举信息
 * @author qhc
 * @date 2017-09-05
 */
public enum PushFinceInfoEnum {
	
	//付款类型（物业和产品是两套所以这里配置两套）
	TOLL_TYPE_CASH("cash","现金"),
	TOLL_TYPE_CHARGE("charge","刷卡"),
	TOLL_TYPE_ALIPAY("alipay","支付宝"),
	TOLL_TYPE_WEIXINPAY("weixinpay","微信支付"),
	TOLL_TYPE_BANK("bank","银行收款"),
	TOLL_TYPE_RETURNMONEY("returnmoney","找零"),
	
//	 productOrderPayType_cash("cash"),                   // 订单支付类型，现金支付
//	    productOrderPayType_charge("charge"),               // 订单支付类型，刷卡
//	    productOrderPayType_bank("bank"),                   // 订单支付类型，银行收款
//	    productOrderPayType_alipay("alipay"),               // 订单支付类型，支付宝
//	    productOrderPayType_weixinpay("weixinpay"),         // 订单支付类型，微信
//	    productOrderPayType_returnmoney("returnmoney"),     // 订单支付类型，找零
//	
	
	
	//支付类型: 1 现金 2 微信 3 银联 4 混合支付
	
	TOLL_TYPE_1("1","现金"),
	TOLL_TYPE_2("2","微信支付"),
	TOLL_TYPE_3("3","刷卡"),
	TOLL_TYPE_4("4","混合支付"),
	TOLL_TYPE_5("5","银行托收"),
	TOLL_TYPE_6("6","银行收款"),
	TOLL_TYPE_7("7","支付宝"),

	
	
	//业务类型
	PROJECT_INFO_WY("wy","管理费"),
	PROJECT_INFO_BT("bt","本体基金"),
	PROJECT_INFO_WATER("water","水费"),
	PROJECT_INFO_ELECT("elect","电费"),
	PROJECT_INFO_COMMON("common","通用账户"),
	PROJECT_INFO_PRODUCT("product","产品购买"),
	PROJECT_INFO_REFUND("refund","退费"),
	
	//业务类型--查询用
	TYTPE_WY("wy","1"),
	TYTPE_BT("bt","2"),
	TYTPE_WATER("water","3"),
	TYTPE_ELECT("elect","4"),
	TYTPE_PAY("pay","6"),
	TYTPE_PRODUCT("product","5"),
	
	//公司对应关系，因为我们系统和财务那边对应的公司实体存在差异，这里做一个中转  
	
//	503	深圳市桃源物业管理有限公司桃源峰景园管理处
//	504	桃源居管理处
//	505	深圳市桃源物业管理有限公司中澳实验学校管理处
//	506	深圳市世外桃源物业管理有限公司重庆分公司
//	507	重庆市澳亚商业管理有限公司
//	509	深圳市世外桃源物业管理有限公司天津滨海新区分公司

//	桃源居国际花园： 506
//	十一区商业街：507
//	桃源居管理处：504
//	桃源峰景园：503
//	天津景湖轩：509

	COMPANY_503("桃源峰景园","深圳市桃源物业管理有限公司桃源峰景园管理处"),
	COMPANY_504("桃源居管理处","桃源居管理处"),
	COMPANY_506("桃源居国际花园","深圳市世外桃源物业管理有限公司重庆分公司"),
	COMPANY_507("十一区商业街","重庆市澳亚商业管理有限公司"),
	COMPANY_509("天津景湖轩","深圳市世外桃源物业管理有限公司天津滨海新区分公司");
	
	
	
	private String index;
	
	private String value;
	
	//构造方法，直接初始化
	PushFinceInfoEnum(  String index , String value ){
        this.index = index; 
        this.value = value;
	}
	
	
	 // 获取值
    public static String getName(String index) {  
        for (PushFinceInfoEnum c : PushFinceInfoEnum.values()) {  
            if (c.getIndex().equals( index ) ) {  
                return c.value;  
            }  
        }  
        return null;  
    }  
	
	
    public String getIndex() {  
        return index;  
    }  
    public void setIndex(String index) {  
        this.index = index;  
    } 
    public String getValue() {  
        return value;  
    }  
    public void setValue(String value) {  
        this.value = value;  
    }
    
	
}
