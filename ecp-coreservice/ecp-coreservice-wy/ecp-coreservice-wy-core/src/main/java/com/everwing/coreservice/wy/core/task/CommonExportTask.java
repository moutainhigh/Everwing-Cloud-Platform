package com.everwing.coreservice.wy.core.task;/**
 * Created by wust on 2017/7/28.
 */

import com.everwing.coreservice.common.utils.SpringContextHolder;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.service.sys.TSysImportExportService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * Function:公共导出任务
 * Reason:可以使用内部类编写此类
 * Date:2017/7/28
 * @author wusongti@lii.com.cn
 */
public class CommonExportTask  implements Runnable{
    static Logger logger = LogManager.getLogger(CommonExportTask.class);

    private TSysImportExportService tSysImportExportService;

    private WyBusinessContext ctx;

    public CommonExportTask(WyBusinessContext ctx) {
        this.tSysImportExportService = ((TSysImportExportService) SpringContextHolder.getBean("tSysImportExportServiceImpl"));
        this.ctx = ctx;
    }

    @Override
    public void run() {
        logger.info("-----------------开始导出");
        tSysImportExportService.exportExcelCallback(ctx);
        logger.info("-----------------导出完成");
    }
}
