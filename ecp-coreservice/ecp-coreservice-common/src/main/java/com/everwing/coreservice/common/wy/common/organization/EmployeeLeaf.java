package com.everwing.coreservice.common.wy.common.organization;/**
 * Created by wust on 2017/8/17.
 */

import com.everwing.coreservice.common.wy.entity.system.user.TSysUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.List;

/**
 *
 * Function:员工构件
 * Reason:
 * Date:2017/8/17
 * @author wusongti@lii.com.cn
 */
public class EmployeeLeaf implements OrganizationComponent,Serializable {

    private static final long serialVersionUID = 163584340770851956L;
    static Logger logger = LogManager.getLogger(EmployeeLeaf.class);
    private TSysUser tSysUser;

    public TSysUser gettSysUser() {
        return this.tSysUser;
    }

    public void settSysUser(TSysUser tSysUser) {
        this.tSysUser = tSysUser;
    }

    @Override
    public void add(OrganizationComponent organizationComponent) {
        logger.error("员工节点不支持新增操作");
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public List<OrganizationComponent> getChildren() {
        logger.error("员工没有子节点");
        return null;
    }
}
