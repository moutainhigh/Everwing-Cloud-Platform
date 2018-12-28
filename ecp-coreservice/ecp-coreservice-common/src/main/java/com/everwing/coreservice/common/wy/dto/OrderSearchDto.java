package com.everwing.coreservice.common.wy.dto;

import com.everwing.coreservice.common.wy.entity.order.TcOrder;

import java.io.Serializable;

/**
 * 工单搜索dto
 *
 * @author DELL shiny
 * @create 2018/1/2
 */
public class OrderSearchDto extends TcOrder implements Serializable{

    /**
     * 客户电话
     */
    private String registerPhone;

    /**
     * 房屋编码
     */
    private String houseCode;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 项目id
     */
    private String projectId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getRegisterPhone() {
        return registerPhone;
    }

    public void setRegisterPhone(String registerPhone) {
        this.registerPhone = registerPhone;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
}
