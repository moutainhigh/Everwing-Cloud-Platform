package com.everwing.coreservice.wy.api.sys;/**
 * Created by wust on 2017/4/26.
 */

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.system.importExport.Excel;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExport;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.common.wy.service.sys.TSysImportExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * Function:excel导入导出API
 * Reason:
 * Date:2017/4/26
 * @author wusongti@lii.com.cn
 */
@Component("tSysImportExportApi")
public class TSysImportExportApi {
    @Autowired
    private TSysImportExportService tSysImportExportService;

    public RemoteModelResult<BaseDto> listPage(WyBusinessContext ctx, TSysImportExportSearch condition){
        BaseDto pageResultDto = tSysImportExportService.listPage(ctx,condition);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(pageResultDto);
        return result;
    }

    public RemoteModelResult<List<TSysImportExportList>> findByCondtion(String companyId, TSysImportExportSearch condition){
        List<TSysImportExportList> list = tSysImportExportService.findByCondtion(companyId,condition);
        RemoteModelResult<List<TSysImportExportList>> result = new RemoteModelResult<>();
        result.setModel(list);
        return result;
    }

    /**
     * 新增一条导入导出记录
     * @param list
     * @return
     */
    public RemoteModelResult<MessageMap> batchInsert(String companyId, List<TSysImportExport> list){
        RemoteModelResult<MessageMap> messageMapRemoteModelResult = new RemoteModelResult<>();
        messageMapRemoteModelResult.setModel(tSysImportExportService.batchInsert(companyId,list));
        return messageMapRemoteModelResult;
    }


    public RemoteModelResult<MessageMap> modify(TSysImportExport entity){
        RemoteModelResult<MessageMap> messageMapRemoteModelResult = new RemoteModelResult<>();
        messageMapRemoteModelResult.setModel(tSysImportExportService.modify(entity));
        return messageMapRemoteModelResult;
    }



    /**
     * 导出excel
     * @param ctx
     * @param excel
     * @return
     */
    public RemoteModelResult<MessageMap> exportExcel(WyBusinessContext ctx, Excel excel){
        RemoteModelResult<MessageMap> messageMapRemoteModelResult = new RemoteModelResult<>();
        messageMapRemoteModelResult.setModel(tSysImportExportService.exportExcel(ctx,excel));
        return messageMapRemoteModelResult;
    }
    
    public RemoteModelResult<MessageMap> modify(String companyId,TSysImportExport entity){
        RemoteModelResult<MessageMap> messageMapRemoteModelResult = new RemoteModelResult<>();
        messageMapRemoteModelResult.setModel(tSysImportExportService.modify(companyId,entity));
        return messageMapRemoteModelResult;
    }
    
    
}
