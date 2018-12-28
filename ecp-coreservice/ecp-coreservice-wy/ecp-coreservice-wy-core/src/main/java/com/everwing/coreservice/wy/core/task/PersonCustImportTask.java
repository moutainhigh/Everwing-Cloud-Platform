package com.everwing.coreservice.wy.core.task;


import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import com.everwing.coreservice.common.wy.common.enums.ImportExportEnum;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeterImport;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNewImportList;
import com.everwing.coreservice.common.wy.service.building.TcBuildingImportService;
import com.everwing.coreservice.common.wy.service.cust.person.PersonCustImportService;
import com.everwing.myexcel.resolver.ExcelResolver;
import com.everwing.myexcel.result.ExcelImportResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class PersonCustImportTask implements Callable {
    static Logger logger = LogManager.getLogger(TcBuildingImportTask.class);

    private PersonCustImportService personCustImportService;

    private WyBusinessContext ctx;

    private String batchNo;

    private String excelPath;

    private ExcelResolver abstractExcel;


    public PersonCustImportTask(ExcelResolver abstractExcel, WyBusinessContext ctx, String batchNo, String excelPath) throws Exception {
        this.ctx = ctx;
        this.excelPath = excelPath;
        this.batchNo = batchNo;
        personCustImportService = SpringContextHolder.getBean("personCustImportServiceImpl");

        this.abstractExcel = abstractExcel;
    }



    @Override
    public MessageMap call() throws ECPBusinessException {

        MessageMap mm = new MessageMap();

        /**
         * 1.从输入流中得到业务数据
         */
        ExcelImportResult excelImportResult = null;
        try {
            excelImportResult = this.abstractExcel.readExcel();
        } catch (ECPBusinessException e) {
            logger.info(CommonUtils.log(e.getMessage()));
            throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
        }

        /**
         * 2.處理业务数据
         */
        Map<String, List<?>> listMap = excelImportResult.getListMap();
        List<PersonCustNewImportList> personCustNewImportLists = (List<PersonCustNewImportList>)listMap.get("0"); // 获取第1个sheet里面的数据
        if(CollectionUtils.isEmpty(personCustNewImportLists)){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("找不到Excel数据呢，请检查一下模板数据是否按照规定格式设置");
        }else{
            try {
                MessageMap returnMM = personCustImportService.importPerson(ctx,personCustNewImportLists);
                if(returnMM.getFlag().equals(MessageMap.INFOR_WARNING)){
                    mm.setFlag(MessageMap.INFOR_WARNING);
                    mm.setMessage(returnMM.getMessage());
                    mm.setObj(ImportExportEnum.partial_success.name());
                }
                if(returnMM.getFlag().equals(MessageMap.INFOR_SUCCESS)){
                    mm.setFlag(MessageMap.INFOR_SUCCESS);
                    mm.setMessage(returnMM.getMessage());
                    mm.setObj(ImportExportEnum.succeed.name());
                }
                if(returnMM.getFlag().equals(MessageMap.INFOR_ERROR)){
                    mm.setFlag(MessageMap.INFOR_ERROR);
                    mm.setMessage(returnMM.getMessage());
                    mm.setObj(ImportExportEnum.failed.name());
                }
            } catch (Exception e) {
                logger.info(CommonUtils.log(e.getMessage()));
                throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
            }

        }
        return mm;
    }

}
