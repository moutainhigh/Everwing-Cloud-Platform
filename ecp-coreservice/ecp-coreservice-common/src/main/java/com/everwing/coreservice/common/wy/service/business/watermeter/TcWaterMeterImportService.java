package com.everwing.coreservice.common.wy.service.business.watermeter;/**
 * Created by wust on 2017/7/24.
 */

import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/7/24
 * @author wusongti@lii.com.cn
 */
public interface TcWaterMeterImportService {
	
    MessageMap importWaterMeterInfo(WyBusinessContext ctx, String batchNo, List<TcWaterMeter> electMeterImport);

    MessageMap importWaterMeter(WyBusinessContext ctx, String batchNo, String excelPath);
}
