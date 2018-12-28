package com.everwing.coreservice.common.wy.entity.property.property;/**
 * Created by wust on 2017/8/14.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/14
 * @author wusongti@lii.com.cn
 */
public class CustomerList implements java.io.Serializable{
    private static final long serialVersionUID = 5524873505005513617L;

    private String id;
    private String name;
    private String custFrom;
    private String cardTypeName;
    private String cardNum;
    private String phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustFrom() {
        return custFrom;
    }

    public void setCustFrom(String custFrom) {
        this.custFrom = custFrom;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
