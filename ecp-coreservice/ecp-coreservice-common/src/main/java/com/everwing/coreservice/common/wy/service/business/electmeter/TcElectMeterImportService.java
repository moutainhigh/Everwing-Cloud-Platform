package com.everwing.coreservice.common.wy.service.business.electmeter;/**
 * Created by wust on 2017/7/24.
 */

import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeterImport;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/7/24
 * @author wusongti@lii.com.cn
 */
public interface TcElectMeterImportService {
    /**
     * 导入
     * @param ctx
     * @param batchNo
     * @return
     */
    MessageMap importElectMeter(WyBusinessContext ctx, String batchNo, String excelPath);

     MessageMap importElect(WyBusinessContext ctx,List<ElectMeterImport> electMeterImportlist);
}
