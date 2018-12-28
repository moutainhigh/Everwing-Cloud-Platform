package com.everwing.autotask.core.service;

import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;

import java.util.List;

/**
 * Created by DELL on 2018/5/11.
 */
public interface ProjectService {

    List<TBsProject> findCanBillingCmacProject(String companyId);

    List<TBsProject> findByObj(String companyId,TBsProject project);

    List<TBsProject> findCanGenBillProject(String companyId);

    TBsProject findById(String companyId,String projectId);
}
