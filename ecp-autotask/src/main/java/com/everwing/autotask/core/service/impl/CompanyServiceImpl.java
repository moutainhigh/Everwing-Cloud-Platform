package com.everwing.autotask.core.service.impl;

import com.everwing.autotask.core.dao.CompanyDao;
import com.everwing.autotask.core.service.CompanyService;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author DELL shiny
 * @create 2018/5/7
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDao companyDao;

    @Override
    public List<Company> queryAllCompany() {
        return companyDao.selectAllCompany();
    }

    @Override
    public Company queryCompany(String companyId) {
        return companyDao.selectById(companyId);
    }

}
