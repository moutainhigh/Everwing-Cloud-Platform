package com.everwing.coreservice.common.wy.fee.service;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.fee.dto.AcBusinessOperaDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.BusinessOperaDetailDto;
import com.everwing.coreservice.common.wy.fee.entity.AcBusinessOperaDetail;

import java.util.List;

/**
 * @author DELL shiny
 * @create 2018/8/13
 */
public interface AcBusinessOperaService {

    /**
     * 创建资金操作明细
     * @param companyId
     * @param acBusinessOperaDetailDto
     * @return 操作明细ID
     */
    String addOperaDetail(String companyId,AcBusinessOperaDetailDto acBusinessOperaDetailDto);

    /**
     * 分页加载businessOperaDetail
     * @param companyId 切库id
     * @param businessOperaDetailDto businessOperaDetail搜索条件
     * @return list
     */
    List<AcBusinessOperaDetail> listPageBusinessOperaDetail(String companyId, BusinessOperaDetailDto businessOperaDetailDto);
}
