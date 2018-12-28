package com.everwing.coreservice.common.wy.entity.system.importExport;/**
 * Created by wust on 2017/5/5.
 */

import com.everwing.coreservice.common.Page;

/**
 *
 * Function:
 * Reason:
 * Date:2017/5/5
 * @author wusongti@lii.com.cn
 */
public class TSysImportExportSearch extends TSysImportExport {
    private static final long serialVersionUID = 8563213375486416854L;
    private Page page;
    private String companyId;
    private String projectCode;
    private String projectName;
    private String operationUserId;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getOperationUserId() {
        return operationUserId;
    }

    public void setOperationUserId(String operationUserId) {
        this.operationUserId = operationUserId;
    }
}
