package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2017/11/14.
 */

import com.everwing.coreservice.common.Page;

/**
 *
 * Function:
 * Reason:
 * Date:2017/11/14
 * @author wusongti@lii.com.cn
 */
public class TDepositSearch extends TDeposit{
    private static final long serialVersionUID = -3282960913900832248L;

    private Page page;

    // 客户姓名
    private String custName;

    // 注册号码
    private String regPhone;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getRegPhone() {
        return regPhone;
    }

    public void setRegPhone(String regPhone) {
        this.regPhone = regPhone;
    }
}
