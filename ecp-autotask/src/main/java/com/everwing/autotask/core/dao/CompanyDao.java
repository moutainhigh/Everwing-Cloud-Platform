package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.platform.entity.generated.Company;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 公司
 *
 * @author DELL shiny
 * @create 2018/5/7
 */
@Repository
public interface CompanyDao {

    List<Company> selectAllCompany();

    Company selectById(String companyId);
}
