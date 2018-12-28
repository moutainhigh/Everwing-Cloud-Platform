package com.everwing.coreservice.wy.fee.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.fee.dto.AcBusinessOperaDetailDto;
import com.everwing.coreservice.common.wy.fee.service.AcBusinessOperaService;
import org.springframework.stereotype.Component;

/**
 * @Author: zgrf
 * @Description: 资金操作明细api
 * @Date: Create in 2018-08-14 09:52
 **/
@Component
public class AcBusinessOperaDetailApi {

    @Reference(check = false)
    private AcBusinessOperaService acBusinessOperaService;

    public RemoteModelResult<String> createOperaDetail(String companyId, AcBusinessOperaDetailDto dto){
        String operaId = acBusinessOperaService.addOperaDetail(companyId,dto);
        return new RemoteModelResult<String>(operaId);
    }

}
