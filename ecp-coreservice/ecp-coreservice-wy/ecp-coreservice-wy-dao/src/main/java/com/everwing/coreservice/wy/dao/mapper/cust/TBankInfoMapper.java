package com.everwing.coreservice.wy.dao.mapper.cust;

import com.everwing.coreservice.common.wy.entity.cust.TBankInfo;
import com.everwing.coreservice.common.wy.entity.cust.TBankInfoExample;

import java.util.List;

public interface TBankInfoMapper {
    long countByExample(TBankInfoExample example);

    int deleteByExample(TBankInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TBankInfo record);

    int insertSelective(TBankInfo record);

    List<TBankInfo> selectByExample(TBankInfoExample example);

    TBankInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TBankInfo record);

    int updateByPrimaryKey(TBankInfo record);
}