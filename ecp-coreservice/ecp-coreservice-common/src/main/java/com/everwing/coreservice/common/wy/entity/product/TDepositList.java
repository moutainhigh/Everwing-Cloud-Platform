package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2017/11/14.
 */

import com.everwing.coreservice.common.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * Function:
 * Reason:
 * Date:2017/11/14
 * @author wusongti@lii.com.cn
 */
public class TDepositList extends TDeposit{
    private static final long serialVersionUID = 4536299706659329942L;

    private String statusName;

    private Double returnedPrice;   // 已退金额

    private Double deductAmount;    // 折扣金额

    private String personCustName;

    private String personCustPhone;

    private String enterpriseCustName;

    private String enterpriseCustPhone;

    private String depositPersonInfo;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Double getReturnedPrice() {
        return returnedPrice;
    }

    public void setReturnedPrice(Double returnedPrice) {
        this.returnedPrice = returnedPrice;
    }

    public Double getDeductAmount() {
        return deductAmount;
    }

    public void setDeductAmount(Double deductAmount) {
        this.deductAmount = deductAmount;
    }

    public String getPersonCustName() {
        return personCustName;
    }

    public void setPersonCustName(String personCustName) {
        this.personCustName = personCustName;
    }

    public String getPersonCustPhone() {
        return personCustPhone;
    }

    public void setPersonCustPhone(String personCustPhone) {
        this.personCustPhone = personCustPhone;
    }

    public String getEnterpriseCustName() {
        return enterpriseCustName;
    }

    public void setEnterpriseCustName(String enterpriseCustName) {
        this.enterpriseCustName = enterpriseCustName;
    }

    public String getEnterpriseCustPhone() {
        return enterpriseCustPhone;
    }

    public void setEnterpriseCustPhone(String enterpriseCustPhone) {
        this.enterpriseCustPhone = enterpriseCustPhone;
    }

    public String getDepositPersonInfo() {
        String format = "客户名称：%s<br/>联系方式：%s";
        if(StringUtils.isNotBlank(CommonUtils.null2String(this.personCustName))
                || StringUtils.isNotBlank(CommonUtils.null2String(this.personCustPhone))){
           this.depositPersonInfo =  String.format(format,this.personCustName,this.personCustPhone);
        }else {
            this.depositPersonInfo =   String.format(format,this.enterpriseCustName,this.enterpriseCustPhone);
        }
       return this.depositPersonInfo;
    }
}
