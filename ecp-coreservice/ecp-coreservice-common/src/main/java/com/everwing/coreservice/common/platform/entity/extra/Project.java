package com.everwing.coreservice.common.platform.entity.extra;

import com.everwing.coreservice.common.wy.entity.system.project.TSysProject;

import java.io.Serializable;

/**
 * 项目类
 *
 * @author DELL shiny
 * @create 2018/4/3
 */
public class Project extends TSysProject implements Serializable{

    private String companyId;

    private String zipCode;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

}
