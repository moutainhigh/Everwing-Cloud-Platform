package com.everwing.autotask.core.service;

import com.everwing.coreservice.common.platform.entity.generated.Company;

import java.util.List;

/**
 * 公司信息
 *
 * @author DELL shiny
 * @create 2018/5/7
 */
public interface CompanyService {

    /**
     * 查询所有公司信息
     * @return 公司集合
     */
    List<Company> queryAllCompany();

    Company queryCompany(String companyId);
}
