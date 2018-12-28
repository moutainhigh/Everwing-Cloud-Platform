package com.everwing.coreservice.common.wy.fee.constant;

/**
 * @author DELL shiny
 * @create 2018/8/22
 */
public enum PayChannel {

    CASH(1, "现金"),

	WE_CHAT(2, "微信"),

    UNION_PAY(3, "银联"),

	MIX_PAY(4, "混合支付"),

	COLLECTION(5, "托收"),

	BANK(6, "银行"),

	ALIPAY(7, "支付宝"),

    SMALL_ROUTINE(8,"小程序");

    private int code;

    private String desc;

    PayChannel(int code,String desc){
        this.code=code;
        this.desc=desc;
    }

    public static PayChannel getPayChannalByCode(int code){
        for(PayChannel payChannel:PayChannel.values()){
            if(code==payChannel.getCode()){
                return payChannel;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
