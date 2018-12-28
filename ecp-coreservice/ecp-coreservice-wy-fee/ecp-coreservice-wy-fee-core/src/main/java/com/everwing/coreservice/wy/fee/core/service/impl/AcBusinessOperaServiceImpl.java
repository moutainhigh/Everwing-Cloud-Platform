package com.everwing.coreservice.wy.fee.core.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.everwing.coreservice.common.wy.fee.dto.AcBusinessOperaDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.BusinessOperaDetailDto;
import com.everwing.coreservice.common.wy.fee.entity.AcBusinessOperaDetail;
import com.everwing.coreservice.common.wy.fee.service.AcBusinessOperaService;
import com.everwing.coreservice.wy.fee.dao.mapper.AcBusinessOperaDetailMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author DELL shiny
 * @create 2018/8/13
 */
@Service
@Component
public class AcBusinessOperaServiceImpl implements AcBusinessOperaService {

    private Logger logger= LogManager.getLogger(AcBusinessOperaServiceImpl.class);

    @Autowired
    private AcBusinessOperaDetailMapper acBusinessOperaDetailMapper;

    @Override
    public String addOperaDetail(String companyId, AcBusinessOperaDetailDto acBusinessOperaDetailDto) {
        acBusinessOperaDetailMapper.insertOperaDetail(acBusinessOperaDetailDto);
        return acBusinessOperaDetailDto.getId();
    }

    @Override
    public List<AcBusinessOperaDetail> listPageBusinessOperaDetail(String companyId, BusinessOperaDetailDto businessOperaDetailDto) {
        return acBusinessOperaDetailMapper.selectByCondition(businessOperaDetailDto);
    }
}
