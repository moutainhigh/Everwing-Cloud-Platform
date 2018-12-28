package com.everwing.coreservice.wy.core.task;

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.service.cust.enterprise.EnterpriseCustImportService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TcEnterpriseImportTask implements Runnable {

    static Logger logger = LogManager.getLogger(TcEnterpriseImportTask.class);

    private EnterpriseCustImportService enterpriseCustImportService;

    private WyBusinessContext ctx;

    private String batchNo;

    public TcEnterpriseImportTask(WyBusinessContext ctx, EnterpriseCustImportService enterpriseCustImportService, String batchNo) throws Exception {
        this.enterpriseCustImportService = enterpriseCustImportService;
        this.ctx = ctx;
        this.batchNo = batchNo;
    }

    @Override
    public void run() {
        logger.info("-----------------开始导入建筑");
        MessageMap mm = new MessageMap();
        try {
            mm = enterpriseCustImportService.importCallback(ctx, batchNo);
        }finally {
            enterpriseCustImportService.after(ctx,mm,batchNo);
        }
        logger.info("-----------------导入建筑完成");
    }

    }
