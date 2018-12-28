package com.everwing.autotask.core.service.bill;

import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;

/**
 * 基类
 *
 * @author DELL shiny
 * @create 2018/5/30
 */
public interface BaseCmacService {

    void setNext(BaseCmacService cmacService);

    void invoke(String companyId, TBsProject project);

}
