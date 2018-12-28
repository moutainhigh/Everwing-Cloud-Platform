package com.everwing.coreservice.wy.core.task;/**
 * Created by wust on 2017/5/22.
 */

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.service.building.TcBuildingImportService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * Function:实现导入业务的一个预备线程
 * Reason:处理导入业务，并在导入完成后更新导入导出表的状态
 * Date:2017/5/22
 * @author wusongti@lii.com.cn
 */
public class TcBuildingImportTask implements Runnable{

    static Logger logger = LogManager.getLogger(TcBuildingImportTask.class);

    private TcBuildingImportService tcBuildingImportService;

    private WyBusinessContext ctx;

    private String batchNo;


    public TcBuildingImportTask(WyBusinessContext ctx, TcBuildingImportService tcBuildingImportService, String batchNo) throws Exception {
        this.tcBuildingImportService = tcBuildingImportService;
        this.ctx = ctx;
        this.batchNo = batchNo;
    }



    @Override
    public void run(){
        logger.info("-----------------开始导入建筑");
        MessageMap mm = new MessageMap();
        try {
            mm = tcBuildingImportService.importCallback(ctx, batchNo);
        }finally {
            tcBuildingImportService.after(ctx,mm,batchNo);
        }
        logger.info("-----------------导入建筑完成");
    }
}
