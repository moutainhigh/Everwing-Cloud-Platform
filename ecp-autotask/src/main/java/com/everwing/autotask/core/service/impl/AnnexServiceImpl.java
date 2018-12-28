package com.everwing.autotask.core.service.impl;

import com.everwing.autotask.core.dao.AnnexMapper;
import com.everwing.autotask.core.service.AnnexService;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author DELL shiny
 * @create 2018/6/4
 */
@Service
public class AnnexServiceImpl implements AnnexService {

    @Autowired
    private AnnexMapper annexMapper;


    @Override
    public void addAnnex(String companyId,Annex annex) {
        annexMapper.insert(annex);
    }
}
