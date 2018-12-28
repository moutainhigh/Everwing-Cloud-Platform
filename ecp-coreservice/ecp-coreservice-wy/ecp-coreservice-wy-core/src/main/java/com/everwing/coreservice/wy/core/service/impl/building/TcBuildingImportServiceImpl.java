package com.everwing.coreservice.wy.core.service.impl.building;/**
 * Created by wust on 2017/7/24.
 */

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import com.everwing.coreservice.common.utils.cache.DataDictionaryUtil;
import com.everwing.coreservice.common.utils.generator.WyCodeGenerator;
import com.everwing.coreservice.common.wy.common.enums.*;
import com.everwing.coreservice.common.wy.entity.common.synchrodata.TSynchrodata;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingImportList;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingList;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExport;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectList;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectSearch;
import com.everwing.coreservice.common.wy.service.building.TcBuildingImportService;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.core.task.TcBuildingImportTask;
import com.everwing.coreservice.wy.core.utils.ImportExportUtils;
import com.everwing.coreservice.wy.dao.mapper.common.ImportExportMapper;
import com.everwing.coreservice.wy.dao.mapper.common.TSynchrodataMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcBuildingMapper;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysProjectMapper;
import com.everwing.myexcel.definition.ExcelDefinitionReader;
import com.everwing.myexcel.factory.DefinitionFactory;
import com.everwing.myexcel.factory.xml.XMLDefinitionFactory4commonImport;
import com.everwing.myexcel.resolver.poi.POIExcelResolver4commonImport;
import com.everwing.myexcel.result.ExcelImportResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Function:导入建筑
 * Reason:导入建筑和普通的导入有点不一样，就是该导入实现的是一棵树结构的导入
 * Date:2017/7/24
 * @author wusongti@lii.com.cn
 */
@Service("tcBuildingImportServiceImpl")
public class TcBuildingImportServiceImpl extends POIExcelResolver4commonImport implements TcBuildingImportService {

    static Logger logger = LogManager.getLogger(TcBuildingImportServiceImpl.class);

    @Autowired
    private TcBuildingMapper tcBuildingMapper;

    @Autowired
    private ImportExportMapper importExportMapper;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private FastDFSApi fastDFSApi;

    @Autowired
    private TSysProjectMapper tSysProjectMapper;

    @Autowired
    private TSynchrodataMapper tSynchrodataMapper;

    @Value("${queue.wy2platform.building.key}")
    private String queue_wy2platform_building_key;

    /**
     * 导入入口(API层调用入口)
     * @param ctx
     * @param tSysImportExportRequest
     */
    @Override
    public void doImport(WyBusinessContext ctx, TSysImportExportSearch tSysImportExportRequest) {
        if(StringUtils.isBlank(tSysImportExportRequest.getProjectCode())){
            throw new ECPBusinessException("参数[projectCode]不能空");
        }

        if(StringUtils.isBlank(tSysImportExportRequest.getBatchNo())){
            throw new ECPBusinessException("参数[batchNo]不能空");
        }

        TSysImportExportSearch condition = new TSysImportExportSearch();
        condition.setBatchNo(tSysImportExportRequest.getBatchNo());
        List<TSysImportExportList> tSysImportExportListList = importExportMapper.findByCondtion(condition);
        if(CollectionUtils.isEmpty(tSysImportExportListList)){
            throw new ECPBusinessException("没有文件上传记录，请先上传文件");
        }
        TSysImportExportList tSysImportExportListExist = tSysImportExportListList.get(0);
        try {
            RemoteModelResult<UploadFile> remoteModelResult = fastDFSApi.loadFilePathById(tSysImportExportListExist.getUploadFileId());
            if(remoteModelResult.isSuccess()){
                UploadFile uploadFile = remoteModelResult.getModel();
                logger.info("上传的文件{}",uploadFile);
                URL url = new URL(uploadFile.getPath());
                HttpURLConnection uc = (HttpURLConnection) url.openConnection();
                uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true
                uc.connect();
                super.excelInputStream = uc.getInputStream();
            }
        } catch (Exception e) {
            logger.error(e);
            throw new ECPBusinessException("导入失败，读取文件失败："+e.getMessage());
        }


        TSysProjectSearch tSysProjectSearch = new TSysProjectSearch();
        tSysProjectSearch.setCode(tSysImportExportRequest.getProjectCode());
        List<TSysProjectList>  tSysProjectLists = tSysProjectMapper.findByCondition(tSysProjectSearch);
        if(CollectionUtils.isNotEmpty(tSysProjectLists)){
            ctx.setProjectId(tSysProjectLists.get(0).getProjectId());
            ctx.setProjectCode(tSysProjectLists.get(0).getCode());
            ctx.setProjectName(tSysProjectLists.get(0).getName());
        }


        /**
         * 耗时多的业务丢到线程池
         */
        try {
            Runnable task = new TcBuildingImportTask(ctx,this,tSysImportExportRequest.getBatchNo());
            threadPoolTaskExecutor.execute(task);
        } catch (Exception e) {
            throw new ECPBusinessException("导入失败："+e.getMessage());
        }
    }

    /**
     * 导入回调方法
     * @param ctx
     * @param batchNo
     */
    @Override
    public MessageMap importCallback(WyBusinessContext ctx, String batchNo) {
        // 切换数据源
        WyBusinessContext.getContext().setCompanyId(ctx.getCompanyId());

        MessageMap mm = new MessageMap();

        int allCount = 0;   // 导入且有数据的sheet数量

        ExcelImportResult excelImportResult = null;

        try {
            // 1.读取excel数据
            excelImportResult = super.readExcel();

            // 2.处理业务数据
            Map<String, List<?>> listMap = excelImportResult.getListMap();

            List<TcBuildingImportList> tcBuildingImportLists = (List<TcBuildingImportList>)listMap.get("0"); // 获取第1个sheet里面的数据


            MessageMap messageMap = null;
            if(!CollectionUtils.isEmpty(tcBuildingImportLists)){
                allCount ++;
                messageMap = importBuilding(ctx, tcBuildingImportLists);
            }


            int successCount = 0;
            int errorCount = 0;
            int partialSuccessCount = 0;
            if(messageMap != null){
                if(MessageMap.INFOR_ERROR.equals(messageMap.getFlag())){
                    errorCount ++;
                }else if(MessageMap.INFOR_WARNING.equals(messageMap.getFlag())){
                    partialSuccessCount ++;
                }else{
                    successCount ++;
                }
            }



            if(partialSuccessCount > 0){
                mm.setObj(ImportExportEnum.partial_success.name());
            }else{
                if(allCount > 0 && (allCount == successCount)){
                    mm.setObj(ImportExportEnum.succeed.name());
                }else if(allCount == errorCount){
                    mm.setObj(ImportExportEnum.failed.name());
                }else {
                    mm.setObj(ImportExportEnum.partial_success.name());
                }
            }

            mm.setMessage(messageMap.getMessage());
        } catch (Exception e){
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(e.getMessage());
        }
        return mm;
    }

    /**
     * 导入完成后的操作
     * @param ctx
     * @param mm
     * @param batchNo
     */
    @Override
    public void after(WyBusinessContext ctx, MessageMap mm, String batchNo){
        TSysImportExportSearch condition = new TSysImportExportSearch();
        condition.setBatchNo(batchNo);
        List<TSysImportExportList> tSysImportExportListList = importExportMapper.findByCondtion(condition);
        TSysImportExportList tSysImportExportListExist = tSysImportExportListList.get(0);

        String stauts = "";
        if(MessageMap.INFOR_SUCCESS.equals(mm.getFlag())){
            if(mm.getObj() != null){
                stauts = mm.getObj().toString();
            }else{
                stauts = ImportExportEnum.succeed.name();
            }
            TSysImportExport tSysImportExport = new TSysImportExport();
            BeanUtils.copyProperties(tSysImportExportListExist,tSysImportExport);
            tSysImportExport.setStatus(stauts);
            importExportMapper.modify(tSysImportExport);
        }else{
            if(mm.getObj() != null){
                stauts = mm.getObj().toString();
            }else{
                stauts = ImportExportEnum.failed.name();
            }
            TSysImportExport tSysImportExport = new TSysImportExport();
            BeanUtils.copyProperties(tSysImportExportListExist,tSysImportExport);
            tSysImportExport.setStatus(stauts);
            tSysImportExport.setEndTime(new Date());
            importExportMapper.modify(tSysImportExport);
        }

        // 上传导入结果到文件服务器
        ImportExportUtils.uploadMessage(importExportMapper,fastDFSApi,tSysImportExportListExist.getBatchNo(),mm.getMessage());


        /**
         * 将数据推送到平台
         */
        int commitSize = 1000;//默认每次提交数量
        TcBuildingSearch tcBuildingSearch = new TcBuildingSearch();
        List<TcBuildingList> tcBuildingLists = tcBuildingMapper.findByCondition(tcBuildingSearch);
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(tcBuildingLists)){
            int size = tcBuildingLists.size();
            if (size <= commitSize) {
                List<TSynchrodata> tSynchrodatas = new ArrayList<>(commitSize);
                for (TcBuildingList tcBuildingList : tcBuildingLists) {
                    TSynchrodata tSynchrodata = new TSynchrodata();
                    tSynchrodata.setCode(WyCodeGenerator.genSynchrodataCode());
                    tSynchrodata.setDescription("同步建筑tc_building表");
                    tSynchrodata.setTableName(SynchrodataEnum.table_tc_building.getStringValue());
                    tSynchrodata.setTableFieldName("building_code");
                    tSynchrodata.setTableFieldValue(tcBuildingList.getBuildingCode());
                    tSynchrodata.setDestinationQueue(queue_wy2platform_building_key);
                    tSynchrodata.setOperation(RabbitMQEnum.insertOrModify.name());
                    tSynchrodata.setPriorityLevel(SynchrodataEnum.priorityLevel_middle.getIntValue());
                    tSynchrodata.setState(SynchrodataEnum.state_draft.getStringValue());
                    tSynchrodata.setCreaterId(ctx.getUserId());
                    tSynchrodata.setCreaterName(ctx.getStaffName());
                    tSynchrodatas.add(tSynchrodata);
                }
                tSynchrodataMapper.batchInsert(tSynchrodatas);
            } else {
                int partSize = size / commitSize;
                for(int i = 0; i < partSize; i++) {
                    List<TSynchrodata> tSynchrodatas = new ArrayList<>(tcBuildingLists.size());
                    List<TcBuildingList> subList = tcBuildingLists.subList(0, commitSize);
                    for (TcBuildingList tcBuildingList : subList) {
                        TSynchrodata tSynchrodata = new TSynchrodata();
                        tSynchrodata.setCode(WyCodeGenerator.genSynchrodataCode());
                        tSynchrodata.setDescription("同步建筑tc_building表");
                        tSynchrodata.setTableName(SynchrodataEnum.table_tc_building.getStringValue());
                        tSynchrodata.setTableFieldName("building_code");
                        tSynchrodata.setTableFieldValue(tcBuildingList.getBuildingCode());
                        tSynchrodata.setDestinationQueue(queue_wy2platform_building_key);
                        tSynchrodata.setOperation(RabbitMQEnum.insertOrModify.name());
                        tSynchrodata.setPriorityLevel(SynchrodataEnum.priorityLevel_middle.getIntValue());
                        tSynchrodata.setState(SynchrodataEnum.state_draft.getStringValue());
                        tSynchrodata.setCreaterId(ctx.getUserId());
                        tSynchrodata.setCreaterName(ctx.getStaffName());
                        tSynchrodatas.add(tSynchrodata);
                    }

                    tcBuildingLists.subList(0, commitSize).clear();

                    tSynchrodataMapper.batchInsert(tSynchrodatas);
                }
                if(!tcBuildingLists.isEmpty()){
                    List<TSynchrodata> tSynchrodatas = new ArrayList<>(tcBuildingLists.size());
                    for (TcBuildingList tcBuildingList : tcBuildingLists) {
                        TSynchrodata tSynchrodata = new TSynchrodata();
                        tSynchrodata.setCode(WyCodeGenerator.genSynchrodataCode());
                        tSynchrodata.setDescription("同步建筑tc_building表");
                        tSynchrodata.setTableName(SynchrodataEnum.table_tc_building.getStringValue());
                        tSynchrodata.setTableFieldName("building_code");
                        tSynchrodata.setTableFieldValue(tcBuildingList.getBuildingCode());
                        tSynchrodata.setDestinationQueue(queue_wy2platform_building_key);
                        tSynchrodata.setOperation(RabbitMQEnum.insertOrModify.name());
                        tSynchrodata.setPriorityLevel(SynchrodataEnum.priorityLevel_middle.getIntValue());
                        tSynchrodata.setState(SynchrodataEnum.state_draft.getStringValue());
                        tSynchrodata.setCreaterId(ctx.getUserId());
                        tSynchrodata.setCreaterName(ctx.getStaffName());
                        tSynchrodatas.add(tSynchrodata);
                    }

                    tSynchrodataMapper.batchInsert(tSynchrodatas);
                }
            }
        }
    }

    /**
     * 这个方法返回xml定义对象
     */
    @Override
    protected ExcelDefinitionReader getExcelDefinition() {
        String xmlFullPath = "importExport/import/xml/basedata_building.xml";
        DefinitionFactory definitionReaderFactory = new XMLDefinitionFactory4commonImport(xmlFullPath);
        return definitionReaderFactory.createExcelDefinitionReader();
    }


    @Override
    protected String getLookupItemCodeByName(String lookupCode, String parentCode, String name) {
        WyBusinessContext ctx = WyBusinessContext.getContext();
        return DataDictionaryUtil.getLookupItemCodeByParentCodeAndName(ctx.getCompanyId(),parentCode,name);
    }

    /**
     * 导入资产
     * @param ctx
     * @param tcBuildingImportLists
     * @return
     */
    private MessageMap importBuilding(WyBusinessContext ctx, List<TcBuildingImportList> tcBuildingImportLists) {
        MessageMap mm = new MessageMap();

        int commitSize = 1000;//默认每次提交数量

        // 错误信息
        StringBuffer errorMsg = new StringBuffer();

        int successCount = 0;

        final List<TcBuilding> tcBuildingListNew = new ArrayList<>(1000);
        final List<TcBuilding> tcBuildingListOld = new ArrayList<>(1000);
        final List<TcBuilding> unbindAssociatedParkingSpacesList = new ArrayList<>(200);
        final List<TcBuilding> bindAssociatedParkingSpacesList = new ArrayList<>(200);
        final Set<String> boundSet = new HashSet<>(100);
        final HashMap<String,String> addedMap = new HashMap(1000); // 添加过的数据
        setBuildingFullNameByDatabase(ctx.getProjectCode(),addedMap);

        /**
         * 手工管理事务
         */
        DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) SpringContextHolder.getBean("transactionManager");
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务，这样会比较安全些。
        TransactionStatus status = transactionManager.getTransaction(def); // 获得事务

        int len = tcBuildingImportLists.size();
        try {
            for(int i=0;i < len;i++){
                TcBuildingImportList tcBuildingImportList = tcBuildingImportLists.get(i);
                if(tcBuildingImportList.getSuccessFlag()){
                    // 解析建筑
                    getBuildingTreeByRow(ctx,
                            tcBuildingImportList,
                            tcBuildingListNew,
                            tcBuildingListOld,
                            unbindAssociatedParkingSpacesList,
                            bindAssociatedParkingSpacesList,
                            boundSet,
                            addedMap);
                    successCount ++;
                }else{
                    errorMsg.append(tcBuildingImportList.getErrorMessage()).append("\n");
                }
            }

            // 新增
            if (CollectionUtils.isNotEmpty(tcBuildingListNew)) {
                int size = tcBuildingListNew.size();
                if (size <= commitSize) {
                    tcBuildingMapper.batchInsert(tcBuildingListNew);
                } else {
                    int partSize = size / commitSize;
                    for(int i = 0; i < partSize; i++) {
                        List<TcBuilding> subList = tcBuildingListNew.subList(0, commitSize);
                        tcBuildingMapper.batchInsert(subList);
                        logger.info("导入-->新增建筑：分批提交" + commitSize + "条数据");

                        tcBuildingListNew.subList(0, commitSize).clear();
                        logger.info("导入-->新增建筑：剔除已经提交的数据后还剩余" + tcBuildingListNew.size() + "条数据");
                    }
                    if(!tcBuildingListNew.isEmpty()){
                        tcBuildingMapper.batchInsert(tcBuildingListNew);
                        logger.info("导入-->新增建筑：分批提交剩余" + tcBuildingListNew.size() + "条数据");
                    }
                }
            }

            // 修改
            if (CollectionUtils.isNotEmpty(tcBuildingListOld)) {
                int size = tcBuildingListOld.size();
                if (size <= commitSize) {
                    tcBuildingMapper.batchModify(tcBuildingListOld);
                } else {
                    int partSize = size / commitSize;
                    for(int i = 0; i < partSize; i++) {
                        List<TcBuilding> subList = tcBuildingListOld.subList(0, commitSize);
                        tcBuildingMapper.batchModify(subList);
                        logger.info("导入-->修改建筑：分批提交" + commitSize + "条数据");

                        tcBuildingListOld.subList(0, commitSize).clear();
                        logger.info("导入-->修改建筑：剔除已经提交的数据后还剩余" + tcBuildingListOld.size() + "条数据");
                    }
                    if(!tcBuildingListOld.isEmpty()){
                        tcBuildingMapper.batchModify(tcBuildingListOld);
                        logger.info("导入-->修改建筑：分批提交剩余" + tcBuildingListOld.size() + "条数据");
                    }
                }
            }


            /**
             * 解绑车位
             */
            if(CollectionUtils.isNotEmpty(unbindAssociatedParkingSpacesList)){
                List<TcBuilding> modifyList = new ArrayList<>(unbindAssociatedParkingSpacesList.size());
                TcBuildingSearch tcBuildingSearch = new TcBuildingSearch();
                for (TcBuilding associatedParkingSpaces : unbindAssociatedParkingSpacesList) {
                    tcBuildingSearch.setHouseCodeNew(associatedParkingSpaces.getHouseCodeNew());
                    List<TcBuildingList> tcBuildingLists = tcBuildingMapper.findByCondition(tcBuildingSearch);
                    if(CollectionUtils.isEmpty(tcBuildingLists)){
                        continue;
                    }
                    TcBuilding tcBuilding = tcBuildingLists.get(0);
                    tcBuilding.setAssociatedParkingSpaces(associatedParkingSpaces.getAssociatedParkingSpaces());
                    tcBuilding.setModifyId(ctx.getUserId());
                    tcBuilding.setModifyName(ctx.getStaffName());
                    modifyList.add(tcBuilding);
                }
                if(CollectionUtils.isNotEmpty(modifyList)){
                    int size = modifyList.size();
                    if (size <= commitSize) {
                        tcBuildingMapper.batchModify(modifyList);
                    } else {
                        int partSize = size / commitSize;
                        for(int i = 0; i < partSize; i++) {
                            List<TcBuilding> subList = modifyList.subList(0, commitSize);
                            tcBuildingMapper.batchModify(subList);
                            logger.info("导入-->设置关联车位：分批提交" + commitSize + "条数据");

                            modifyList.subList(0, commitSize).clear();
                            logger.info("导入-->设置关联车位：剔除已经提交的数据后还剩余" + modifyList.size() + "条数据");
                        }
                        if(!modifyList.isEmpty()){
                            tcBuildingMapper.batchModify(modifyList);
                            logger.info("导入-->设置关联车位：分批提交剩余" + modifyList.size() + "条数据");
                        }
                    }
                }
            }

            /**
             * 关联车位
             */
            if(CollectionUtils.isNotEmpty(bindAssociatedParkingSpacesList)){
                List<TcBuilding> modifyList = new ArrayList<>(bindAssociatedParkingSpacesList.size());
                TcBuildingSearch tcBuildingSearch = new TcBuildingSearch();
                for (TcBuilding associatedParkingSpaces : bindAssociatedParkingSpacesList) {
                    tcBuildingSearch.setHouseCodeNew(associatedParkingSpaces.getHouseCodeNew());
                    List<TcBuildingList> tcBuildingLists = tcBuildingMapper.findByCondition(tcBuildingSearch);
                    if(CollectionUtils.isEmpty(tcBuildingLists)){
                        continue;
                    }
                    TcBuilding tcBuilding = tcBuildingLists.get(0);
                    tcBuilding.setAssociatedParkingSpaces(associatedParkingSpaces.getAssociatedParkingSpaces());
                    tcBuilding.setModifyId(ctx.getUserId());
                    tcBuilding.setModifyName(ctx.getStaffName());
                    modifyList.add(tcBuilding);
                }
                if(CollectionUtils.isNotEmpty(modifyList)){
                    int size = modifyList.size();
                    if (size <= commitSize) {
                        tcBuildingMapper.batchModify(modifyList);
                    } else {
                        int partSize = size / commitSize;
                        for(int i = 0; i < partSize; i++) {
                            List<TcBuilding> subList = modifyList.subList(0, commitSize);
                            tcBuildingMapper.batchModify(subList);
                            logger.info("导入-->设置关联车位：分批提交" + commitSize + "条数据");

                            modifyList.subList(0, commitSize).clear();
                            logger.info("导入-->设置关联车位：剔除已经提交的数据后还剩余" + modifyList.size() + "条数据");
                        }
                        if(!modifyList.isEmpty()){
                            tcBuildingMapper.batchModify(modifyList);
                            logger.info("导入-->设置关联车位：分批提交剩余" + modifyList.size() + "条数据");
                        }
                    }
                }
            }

            // 手工提交事务
            transactionManager.commit(status);
        }catch (Exception e){
            logger.error(e);
            successCount = 0;
            errorMsg.append(e.getMessage());
            //手工回滚事务
            transactionManager.rollback(status);
        }finally {
            if(successCount == 0){
                StringBuffer firstMsg = new StringBuffer("导入信息失败，共["+len+"]条记录\n");
                mm.setFlag(MessageMap.INFOR_ERROR);
                mm.setMessage(firstMsg.append(errorMsg).toString());
            }else if(successCount == len){
                String msg = "导入信息成功，共["+len+"]条记录\n";
                mm.setFlag(MessageMap.INFOR_SUCCESS);
                mm.setMessage(msg);
                mm.getMapMessage().put("buildingListInsert",tcBuildingListNew);
                mm.getMapMessage().put("buildingListModify",tcBuildingListOld);
            }else{
                StringBuffer firstMsg = new StringBuffer("导入信息部分成功，共["+successCount+"]条记录导入成功，["+(len - successCount)+"]条记录导入失败\n");
                mm.setFlag(MessageMap.INFOR_WARNING);
                mm.setMessage(firstMsg.append(errorMsg).toString());
                mm.getMapMessage().put("buildingListInsert",tcBuildingListNew);
                mm.getMapMessage().put("buildingListModify",tcBuildingListOld);
            }
        }
        return mm;
    }


    /**
     * 防止数据库已经存在的节点名称和导入的名称重复，因此将已经存在的名称加入集合，在导入的时候用来判断
     * @param projectCode
     * @param addedMap
     */
    private void setBuildingFullNameByDatabase(String projectCode,HashMap<String,String> addedMap){
        TcBuildingSearch tcBuildingSearch = new TcBuildingSearch();
        tcBuildingSearch.setProjectId(projectCode);
        List<TcBuildingList> tcBuildingLists = tcBuildingMapper.findByCondition(tcBuildingSearch);
        if(CollectionUtils.isNotEmpty(tcBuildingLists)) {
            for (TcBuildingList tcBuildingList : tcBuildingLists) {
                String key = tcBuildingList.getProjectName() + tcBuildingList.getBuildingFullName();
                String value = tcBuildingList.getBuildingCode();
                addedMap.put(key,value);
            }
        }
    }


    /**
     * 解析excel里面的一行数据，注意，excel里面的一行数据就是一棵树，因此解析到的集合也是一棵树的数据
     * @param ctx
     * @param tcBuildingImportList
     * @param tcBuildingListNew
     * @param tcBuildingListOld
     * @param unbindAssociatedParkingSpacesList
     * @param bindAssociatedParkingSpacesList
     * @param boundSet
     * @param addedMap
     */
    private void getBuildingTreeByRow(WyBusinessContext ctx,
                                      final TcBuildingImportList tcBuildingImportList,
                                      final List<TcBuilding> tcBuildingListNew,
                                      final List<TcBuilding> tcBuildingListOld,
                                      final List<TcBuilding> unbindAssociatedParkingSpacesList,
                                      final List<TcBuilding> bindAssociatedParkingSpacesList,
                                      final Set<String> boundSet,
                                      final HashMap<String,String> addedMap){

        /**
         * 验证并转换从excel读取到的节点名称
         */
        String buildingName4qi = validateBuildingStructure(LookupItemEnum.buildingType_qi.getStringValue(), tcBuildingImportList.getFirstNode());
        String buildingName4qu = validateBuildingStructure(LookupItemEnum.buildingType_qu.getStringValue(), tcBuildingImportList.getSecondNode());
        String buildingName4dongzuo = validateBuildingStructure(LookupItemEnum.buildingType_dongzuo.getStringValue(), tcBuildingImportList.getThirdNode());
        String buildingName4danyuanrukou = validateBuildingStructure(LookupItemEnum.buildingType_danyuanrukou.getStringValue(), tcBuildingImportList.getFourthNode());
        String buildingName4ceng = validateBuildingStructure(LookupItemEnum.buildingType_ceng.getStringValue(), tcBuildingImportList.getFifthNode());
        String buildingName4leaf = validateBuildingStructure("leaf", tcBuildingImportList.getSixthNode());

        /**
         * 拼接节点全名称
         */
        String buildingFullName4qi = ctx.getProjectName() + buildingName4qi;
        String buildingFullName4qu = buildingFullName4qi + buildingName4qu;
        String buildingFullName4dongzuo = buildingFullName4qu + buildingName4dongzuo;
        String buildingFullName4danyuanrukou = buildingFullName4dongzuo + buildingName4danyuanrukou;
        String buildingFullName4ceng = buildingFullName4danyuanrukou + buildingName4ceng;
        String buildingFullName4leaf = buildingFullName4ceng + buildingName4leaf;

        boolean isNew = false;
        String houseCode = CommonUtils.null2String(tcBuildingImportList.getHouseCode());
        String houseCodeNew = CommonUtils.null2String(tcBuildingImportList.getHouseCodeNew());
        if(StringUtils.isBlank(houseCode) && StringUtils.isBlank(houseCodeNew)){ // 新增
            isNew = true;
        }else{
            if(StringUtils.isNotBlank(houseCode)){ // 旧资产编码不为空，则根据实际情况判断是否是新数据
                TcBuildingSearch tcBuildingSearch = new TcBuildingSearch();
                tcBuildingSearch.setProjectId(ctx.getProjectCode());
                tcBuildingSearch.setHouseCode(houseCode);
                List<TcBuildingList> tcBuildingListsByHouseCode = tcBuildingMapper.findByCondition(tcBuildingSearch);
                if(CollectionUtils.isNotEmpty(tcBuildingListsByHouseCode)){ // 更新
                    isNew = false;
                }else{ // 新增
                    isNew = true;
                }
            }

            if(StringUtils.isNotBlank(houseCodeNew)){ // 新资产编码不为空，则根据实际情况判断是否是新数据
                TcBuildingSearch tcBuildingSearch = new TcBuildingSearch();
                tcBuildingSearch.setProjectId(ctx.getProjectCode());
                tcBuildingSearch.setHouseCodeNew(houseCodeNew);
                List<TcBuildingList> tcBuildingListsByHouseCodeNew = tcBuildingMapper.findByCondition(tcBuildingSearch);
                if(CollectionUtils.isNotEmpty(tcBuildingListsByHouseCodeNew)){ // 更新
                    isNew = false;
                }else{ // 新增
                    isNew = true;
                }
            }
        }

        if(isNew){
            /**
             * 组装【期】节点
             */
            String buildingCode4qi = "";
            if(addedMap.containsKey(buildingFullName4qi)){
                buildingCode4qi = addedMap.get(buildingFullName4qi);
            }else{
                TcBuilding qi = new TcBuilding();
                qi.setId(UUID.randomUUID().toString());
                qi.setCompanyId(ctx.getCompanyId());
                qi.setProjectId(ctx.getProjectCode());
                String buildingCode = WyCodeGenerator.genBuildingCode();
                qi.setBuildingCode(buildingCode);
                qi.setPid("jzjg");
                qi.setBuildingName(buildingName4qi);
                qi.setBuildingFullName(buildingFullName4qi);
                qi.setBuildingType(LookupItemEnum.buildingType_qi.getStringValue());
                qi.setCreaterId(ctx.getUserId());
                qi.setCreaterName(ctx.getStaffName());
                buildingCode4qi = qi.getBuildingCode();
                tcBuildingListNew.add(qi);
                addedMap.put(buildingFullName4qi,qi.getBuildingCode());
            }


            /**
             * 组装【区】节点
             */
            String buildingCode4qu = "";
            if(addedMap.containsKey(buildingFullName4qu)){
                buildingCode4qu = addedMap.get(buildingFullName4qu);
            }else{
                TcBuilding qu = new TcBuilding();
                qu.setId(UUID.randomUUID().toString());
                qu.setCompanyId(ctx.getCompanyId());
                qu.setProjectId(ctx.getProjectCode());
                String buildingCode = WyCodeGenerator.genBuildingCode();
                qu.setBuildingCode(buildingCode);
                qu.setPid(buildingCode4qi);
                qu.setBuildingName(buildingName4qu);
                qu.setBuildingFullName(buildingFullName4qu);
                qu.setBuildingType(LookupItemEnum.buildingType_qu.getStringValue());
                qu.setCreaterId(ctx.getUserId());
                qu.setCreaterName(ctx.getStaffName());
                buildingCode4qu = qu.getBuildingCode();
                tcBuildingListNew.add(qu);
                addedMap.put(buildingFullName4qu,qu.getBuildingCode());
            }


            /**
             * 组装【栋/座】节点
             */
            String buildingCode4dongzuo = "";
            if(addedMap.containsKey(buildingFullName4dongzuo)){
                buildingCode4dongzuo = addedMap.get(buildingFullName4dongzuo);
            }else{
                TcBuilding dongzuo = new TcBuilding();
                dongzuo.setId(UUID.randomUUID().toString());
                dongzuo.setCompanyId(ctx.getCompanyId());
                dongzuo.setProjectId(ctx.getProjectCode());
                String buildingCode = WyCodeGenerator.genBuildingCode();
                dongzuo.setBuildingCode(buildingCode);
                dongzuo.setPid(buildingCode4qu);
                dongzuo.setBuildingName(buildingName4dongzuo);
                dongzuo.setBuildingFullName(buildingFullName4dongzuo);
                dongzuo.setBuildingType(LookupItemEnum.buildingType_dongzuo.getStringValue());
                dongzuo.setCreaterId(ctx.getUserId());
                dongzuo.setCreaterName(ctx.getStaffName());
                buildingCode4dongzuo = dongzuo.getBuildingCode();
                tcBuildingListNew.add(dongzuo);
                addedMap.put(buildingFullName4dongzuo,dongzuo.getBuildingCode());
            }


            /**
             * 组装【单元】节点
             */
            String buildingCode4danyuanrukou = "";
            if(addedMap.containsKey(buildingFullName4danyuanrukou)){
                buildingCode4danyuanrukou = addedMap.get(buildingFullName4danyuanrukou);
            }else{
                TcBuilding danyuanrukou = new TcBuilding();
                danyuanrukou.setId(UUID.randomUUID().toString());
                danyuanrukou.setCompanyId(ctx.getCompanyId());
                danyuanrukou.setProjectId(ctx.getProjectCode());
                String buildingCode = WyCodeGenerator.genBuildingCode();
                danyuanrukou.setBuildingCode(buildingCode);
                danyuanrukou.setPid(buildingCode4dongzuo);
                danyuanrukou.setBuildingName(buildingName4danyuanrukou);
                danyuanrukou.setBuildingFullName(buildingFullName4danyuanrukou);
                danyuanrukou.setBuildingType(LookupItemEnum.buildingType_danyuanrukou.getStringValue());
                danyuanrukou.setCreaterId(ctx.getUserId());
                danyuanrukou.setCreaterName(ctx.getStaffName());
                buildingCode4danyuanrukou = danyuanrukou.getBuildingCode();
                tcBuildingListNew.add(danyuanrukou);
                addedMap.put(buildingFullName4danyuanrukou,danyuanrukou.getBuildingCode());
            }


            /**
             * 组装【层】节点
             */
            String buildingCode4ceng = "";
            if(addedMap.containsKey(buildingFullName4ceng)){
                buildingCode4ceng = addedMap.get(buildingFullName4ceng);
            }else{
                TcBuilding ceng = new TcBuilding();
                ceng.setId(UUID.randomUUID().toString());
                ceng.setCompanyId(ctx.getCompanyId());
                ceng.setProjectId(ctx.getProjectCode());
                String buildingCode = WyCodeGenerator.genBuildingCode();
                ceng.setBuildingCode(buildingCode);
                ceng.setPid(buildingCode4danyuanrukou);
                ceng.setBuildingName(buildingName4ceng);
                ceng.setBuildingFullName(buildingFullName4ceng);
                ceng.setBuildingType(LookupItemEnum.buildingType_ceng.getStringValue());
                ceng.setCreaterId(ctx.getUserId());
                ceng.setCreaterName(ctx.getStaffName());
                buildingCode4ceng = ceng.getBuildingCode();
                tcBuildingListNew.add(ceng);
                addedMap.put(buildingFullName4ceng,ceng.getBuildingCode());
            }



            /**
             * 组装叶子节点
             */
            if(addedMap.containsKey(buildingFullName4leaf)){
                throw new ECPBusinessException("存在重复的数据：" + buildingFullName4leaf);
            }else{
                /**
                 * 将label转换为code
                 */
                String assetAttributes = "";
                String buildingType = tcBuildingImportList.getBuildingType();
                if(LookupItemEnum.buildingType_house.getStringValue().equalsIgnoreCase(buildingType)){
                    assetAttributes = DataDictionaryUtil.getLookupItemCodeByParentCodeAndName(ctx.getCompanyId(),"houseType",tcBuildingImportList.getAssetAttributes());
                }else if(LookupItemEnum.buildingType_store.getStringValue().equalsIgnoreCase(buildingType)){
                    assetAttributes = DataDictionaryUtil.getLookupItemCodeByParentCodeAndName(ctx.getCompanyId(),"storeType",tcBuildingImportList.getAssetAttributes());
                }else if(LookupItemEnum.buildingType_parkspace.getStringValue().equalsIgnoreCase(buildingType)){
                    assetAttributes = DataDictionaryUtil.getLookupItemCodeByParentCodeAndName(ctx.getCompanyId(),"stationProperty",tcBuildingImportList.getAssetAttributes());
                }



                TcBuilding leaf = new TcBuilding();
                BeanUtils.copyProperties(tcBuildingImportList,leaf);
                leaf.setId(UUID.randomUUID().toString());
                leaf.setCompanyId(ctx.getCompanyId());
                leaf.setProjectId(ctx.getProjectCode());
                String buildingCode = WyCodeGenerator.genBuildingCode();
                leaf.setBuildingCode(buildingCode);
                leaf.setPid(buildingCode4ceng);
                leaf.setHouseCodeNew(genHouseCodeNew(ctx,tcBuildingImportList.getBuildingType(),tcBuildingImportList.getHouseCodeNew(),buildingFullName4leaf));
                leaf.setBuildingName(buildingName4leaf);
                leaf.setBuildingFullName(buildingFullName4leaf);
                leaf.setBuildingType(tcBuildingImportList.getBuildingType());
                leaf.setAssetAttributes(assetAttributes);
                leaf.setCreaterId(ctx.getUserId());
                leaf.setCreaterName(ctx.getStaffName());
                setAssociatedParkingSpaces(ctx,true,leaf,unbindAssociatedParkingSpacesList,bindAssociatedParkingSpacesList,boundSet);
                tcBuildingListNew.add(leaf);
                addedMap.put(buildingFullName4leaf,leaf.getBuildingCode());
            }
        }else{
            /**
             * 找到数据库数据的叶子节点
             */
            TcBuilding leaf = null;
            TcBuildingSearch tcBuildingSearch = new TcBuildingSearch();
            tcBuildingSearch.setProjectId(ctx.getProjectCode());

            if(StringUtils.isNotBlank(houseCode)){
                tcBuildingSearch.setHouseCode(houseCode);
                List<TcBuildingList> tcBuildingListsByHouseCode = tcBuildingMapper.findByCondition(tcBuildingSearch);
                if(CollectionUtils.isNotEmpty(tcBuildingListsByHouseCode)){
                    leaf = tcBuildingListsByHouseCode.get(0);
                }
            }
            if(leaf == null && StringUtils.isNotBlank(houseCodeNew)){
                tcBuildingSearch.setHouseCodeNew(houseCodeNew);
                List<TcBuildingList> tcBuildingListsByHouseCodeNew = tcBuildingMapper.findByCondition(tcBuildingSearch);
                if(CollectionUtils.isNotEmpty(tcBuildingListsByHouseCodeNew)){
                    leaf = tcBuildingListsByHouseCodeNew.get(0);
                }
            }



            /**
             * 将label转换为code
             */
            String assetAttributes = "";
            String buildingType = leaf.getBuildingType();
            if(LookupItemEnum.buildingType_house.getStringValue().equalsIgnoreCase(buildingType)){
                assetAttributes = DataDictionaryUtil.getLookupItemCodeByParentCodeAndName(ctx.getCompanyId(),"houseType",tcBuildingImportList.getAssetAttributes());
            }else if(LookupItemEnum.buildingType_store.getStringValue().equalsIgnoreCase(buildingType)){
                assetAttributes = DataDictionaryUtil.getLookupItemCodeByParentCodeAndName(ctx.getCompanyId(),"storeType",tcBuildingImportList.getAssetAttributes());
            }else if(LookupItemEnum.buildingType_parkspace.getStringValue().equalsIgnoreCase(buildingType)){
                assetAttributes = DataDictionaryUtil.getLookupItemCodeByParentCodeAndName(ctx.getCompanyId(),"stationProperty",tcBuildingImportList.getAssetAttributes());
            }


            leaf.setBuildingName(buildingName4leaf);
            leaf.setBuildingFullName(buildingFullName4leaf);
            leaf.setAssetAttributes(assetAttributes);
            leaf.setPropertyAttributes(tcBuildingImportList.getPropertyAttributes());
            leaf.setBuildingCertificate(tcBuildingImportList.getBuildingCertificate());
            leaf.setMarketState(tcBuildingImportList.getMarketState());
            leaf.setFinishDate(tcBuildingImportList.getFinishDate());
            leaf.setBuildingArea(tcBuildingImportList.getBuildingArea());
            leaf.setUsableArea(tcBuildingImportList.getUsableArea());
            leaf.setShareArea(tcBuildingImportList.getShareArea());
            leaf.setAssetUsage(tcBuildingImportList.getAssetUsage());
            leaf.setBuildingHeight(tcBuildingImportList.getBuildingHeight());
            leaf.setJoinFlag(tcBuildingImportList.getJoinFlag());
            leaf.setJoinDate(tcBuildingImportList.getJoinDate());
            leaf.setPropertyName(tcBuildingImportList.getPropertyName());
            leaf.setPropertyAddr(tcBuildingImportList.getPropertyAddr());
            leaf.setPropertyRightFlag(tcBuildingImportList.getPropertyRightFlag());
            leaf.setPropertyRightType(tcBuildingImportList.getPropertyRightType());
            leaf.setParkingSpaceType(tcBuildingImportList.getParkingSpaceType());
            leaf.setAssociatedParkingSpaces(tcBuildingImportList.getAssociatedParkingSpaces());
            leaf.setDiscounts(tcBuildingImportList.getDiscounts());
            leaf.setUnitWyPrice(tcBuildingImportList.getUnitWyPrice());
            leaf.setUnitBtPrice(tcBuildingImportList.getUnitBtPrice());
            leaf.setIsChargeObj(tcBuildingImportList.getIsChargeObj());
            leaf.setBillAddress(tcBuildingImportList.getBillAddress());
            leaf.setModifyId(ctx.getUserId());
            leaf.setModifyName(ctx.getStaffName());
            setAssociatedParkingSpaces(ctx,false,leaf,unbindAssociatedParkingSpacesList,bindAssociatedParkingSpacesList,boundSet);
            tcBuildingListOld.add(leaf);


            TcBuilding ceng = getBuildingByBuildingCode(leaf.getPid());
            ceng.setBuildingName(buildingName4ceng);
            ceng.setBuildingFullName(buildingFullName4ceng);
            ceng.setModifyId(ctx.getUserId());
            ceng.setModifyName(ctx.getStaffName());
            tcBuildingListOld.add(ceng);


            TcBuilding danyuanrukou = getBuildingByBuildingCode(ceng.getPid());
            danyuanrukou.setBuildingName(buildingName4danyuanrukou);
            danyuanrukou.setBuildingFullName(buildingFullName4danyuanrukou);
            danyuanrukou.setModifyId(ctx.getUserId());
            danyuanrukou.setModifyName(ctx.getStaffName());
            tcBuildingListOld.add(danyuanrukou);


            TcBuilding dongzuo = getBuildingByBuildingCode(danyuanrukou.getPid());
            dongzuo.setBuildingName(buildingName4dongzuo);
            dongzuo.setBuildingFullName(buildingFullName4dongzuo);
            dongzuo.setModifyId(ctx.getUserId());
            dongzuo.setModifyName(ctx.getStaffName());
            tcBuildingListOld.add(dongzuo);


            TcBuilding qu = getBuildingByBuildingCode(dongzuo.getPid());
            qu.setBuildingName(buildingName4qu);
            qu.setBuildingFullName(buildingFullName4qu);
            qu.setModifyId(ctx.getUserId());
            qu.setModifyName(ctx.getStaffName());
            tcBuildingListOld.add(qu);


            TcBuilding qi = getBuildingByBuildingCode(qu.getPid());
            qi.setBuildingName(buildingName4qi);
            qi.setBuildingFullName(buildingName4qi);
            qi.setModifyId(ctx.getUserId());
            qi.setModifyName(ctx.getStaffName());
            tcBuildingListOld.add(qi);
        }
    }


    /**
     * 如果houseCodeNew字段值为空，则根据buildingFullName生成资产编码，否则直接返回houseCodeNew
     * @param ctx
     * @param buildingType
     * @param houseCodeNew
     * @param buildingFullName
     * @return
     */
    private String genHouseCodeNew(WyBusinessContext ctx, String buildingType, String houseCodeNew, String buildingFullName){
        String houseCode = houseCodeNew;

        String regEx = "[^0-9a-bA-B]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(buildingFullName);

        String numberStr = m.replaceAll("").trim();

        if(StringUtils.isBlank(CommonUtils.null2String(houseCode))){
            houseCode = ctx.getProjectCode().substring(ctx.getProjectCode().length() - 2) + numberStr;
            if(LookupItemEnum.buildingType_house.getStringValue().equalsIgnoreCase(buildingType)){
                houseCode = houseCode + WyEnum.CODE_TYPE_1.getStringValue();
            }else if(LookupItemEnum.buildingType_store.getStringValue().equalsIgnoreCase(buildingType)){
                houseCode = houseCode + WyEnum.CODE_TYPE_2.getStringValue();
            }else if(LookupItemEnum.buildingType_parkspace.getStringValue().equalsIgnoreCase(buildingType)){
                houseCode = houseCode + WyEnum.CODE_TYPE_3.getStringValue();
            }
        }
        return houseCode;
    }

    /**
     * 根据buildingCode获取建筑
     * @param buildingCode
     * @return
     */
    private TcBuilding getBuildingByBuildingCode(String buildingCode){
        TcBuildingSearch tcBuildingSearch = new TcBuildingSearch();
        tcBuildingSearch.setBuildingCode(buildingCode);
        List<TcBuildingList> tcBuildingLists = tcBuildingMapper.findByCondition(tcBuildingSearch);
        if(CollectionUtils.isNotEmpty(tcBuildingLists)){
            return tcBuildingLists.get(0);
        }
        return null;
    }


    /**
     * 设置关联车位
     * @param ctx
     * @param isNew
     * @param building
     * @param unbindAssociatedParkingSpacesList
     * @param bindAssociatedParkingSpacesList
     * @param boundSet
     */
    private void setAssociatedParkingSpaces(WyBusinessContext ctx,
                                            boolean isNew,
                                            final TcBuilding building,
                                            final List<TcBuilding> unbindAssociatedParkingSpacesList,
                                            final List<TcBuilding> bindAssociatedParkingSpacesList,
                                            final Set<String> boundSet){
        if(!LookupItemEnum.buildingType_parkspace.getStringValue().equalsIgnoreCase(building.getBuildingType())){
            return;
        }

        if(!"009_003".equals(building.getAssetAttributes())){ // 是否子母车位
            return;
        }

        String houseCodeNew = building.getHouseCodeNew();                       // 下面注释遇到houseCodeNew，我们称之为“当前车位”
        String associatedParkingSpaces = building.getAssociatedParkingSpaces(); // 下面注释遇到associatedParkingSpaces，我们称之为“被关联车位”

        if(boundSet.contains(houseCodeNew)){
            // 已经处理过的绑定则忽略
            return;
        }
        if(StringUtils.isNotBlank(associatedParkingSpaces)){
            // 加入已处理列表
            boundSet.add(associatedParkingSpaces);
        }
        if(isNew){
            if(StringUtils.isNotBlank(associatedParkingSpaces)){
                /**
                 * 解绑原来使用associatedParkingSpaces作为关联的所有车位
                 */
                TcBuildingSearch tcBuildingSearch = new TcBuildingSearch();
                tcBuildingSearch.setAssociatedParkingSpaces(associatedParkingSpaces);
                List<TcBuildingList> tcBuildingLists = tcBuildingMapper.findByCondition(tcBuildingSearch);
                if(org.apache.commons.collections.CollectionUtils.isNotEmpty(tcBuildingLists)){
                    for (TcBuildingList tcBuildingList : tcBuildingLists) {
                        tcBuildingList.setAssociatedParkingSpaces("");
                        tcBuildingList.setModifyId(ctx.getUserId());
                        tcBuildingList.setModifyName(ctx.getStaffName());
                        unbindAssociatedParkingSpacesList.add(tcBuildingList);
                    }
                }

                /**
                 * 把当前车位设置为被关联车位的关联车位：
                 * 先用associatedParkingSpaces作为条件查询出所有使用他作为关联车位的数据
                 * 再将查询出来的结果的关联车位修改为当前车位
                 */
                TcBuilding associatedParkingSpacesBuilding = new TcBuilding();
                associatedParkingSpacesBuilding.setHouseCodeNew(associatedParkingSpaces);
                associatedParkingSpacesBuilding.setAssociatedParkingSpaces(houseCodeNew);
                bindAssociatedParkingSpacesList.add(associatedParkingSpacesBuilding);
            }
        }else{
            if(StringUtils.isNotBlank(houseCodeNew)) {
                /**
                 * 解绑原来使用houseCodeNew作为关联的所有车位
                 */
                TcBuildingSearch tcBuildingSearch = new TcBuildingSearch();
                tcBuildingSearch.setAssociatedParkingSpaces(houseCodeNew);
                List<TcBuildingList> tcBuildingLists = tcBuildingMapper.findByCondition(tcBuildingSearch);
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(tcBuildingLists)) {
                    for (TcBuildingList tcBuildingList : tcBuildingLists) {
                        tcBuildingList.setAssociatedParkingSpaces("");
                        tcBuildingList.setModifyId(ctx.getUserId());
                        tcBuildingList.setModifyName(ctx.getStaffName());
                        unbindAssociatedParkingSpacesList.add(tcBuildingList);
                    }
                }
            }

            if(StringUtils.isNotBlank(associatedParkingSpaces)) {
                /**
                 * 解绑原来使用associatedParkingSpaces作为关联的所有车位
                 */
                TcBuildingSearch tcBuildingSearch = new TcBuildingSearch();
                tcBuildingSearch.setAssociatedParkingSpaces(associatedParkingSpaces);
                List<TcBuildingList> tcBuildingLists = tcBuildingMapper.findByCondition(tcBuildingSearch);
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(tcBuildingLists)) {
                    for (TcBuildingList tcBuildingList : tcBuildingLists) {
                        tcBuildingList.setAssociatedParkingSpaces("");
                        tcBuildingList.setModifyId(ctx.getUserId());
                        tcBuildingList.setModifyName(ctx.getStaffName());
                        unbindAssociatedParkingSpacesList.add(tcBuildingList);
                    }
                }


                /**
                 * 把当前车位设置为被关联车位的关联车位：
                 * 先用associatedParkingSpaces作为条件查询出所有使用他作为关联车位的数据
                 * 再将查询出来的结果的关联车位修改为当前车位
                 */
                tcBuildingSearch = new TcBuildingSearch();
                tcBuildingSearch.setHouseCodeNew(associatedParkingSpaces);
                tcBuildingLists = tcBuildingMapper.findByCondition(tcBuildingSearch);
                if(org.apache.commons.collections.CollectionUtils.isNotEmpty(tcBuildingLists)){
                    for (TcBuildingList tcBuildingList : tcBuildingLists) {
                        tcBuildingList.setAssociatedParkingSpaces(houseCodeNew);
                        tcBuildingList.setModifyId(ctx.getUserId());
                        tcBuildingList.setModifyName(ctx.getStaffName());
                        bindAssociatedParkingSpacesList.add(tcBuildingList);
                    }
                }


                /**
                 * 设置当前车位的关联车位
                 */
                TcBuilding current = new TcBuilding();
                current.setId(building.getId());
                current.setHouseCodeNew(building.getHouseCodeNew());
                current.setBuildingCode(building.getBuildingCode());
                current.setAssociatedParkingSpaces(building.getAssociatedParkingSpaces());
                current.setModifyId(ctx.getUserId());
                current.setModifyName(ctx.getStaffName());
                bindAssociatedParkingSpacesList.add(current);
            }
        }
    }


    /**
     * 标准建筑，则严格校验格式
     * 资产全名格式：XX期 + XX区 + XXX栋X座 + X单元/入口 + XX层 + XX（流水码）
     * 新资产编码则根据全名称提取XX数字来充当：XX（两位项目编码） + XX（期） + XX（区） + XXX栋X座 + X（单元/入口） + XX（流水码）
     * @param buildingType  节点类型
     * @param buildingName  节点名称
     * @return  新节点名称
     */
    private String validateBuildingStructure(String buildingType,String buildingName){
        String buildingNameNew = buildingName;
        String regEx = "[^0-9a-bA-B]";
        Pattern p = Pattern.compile(regEx);

        if(LookupItemEnum.buildingType_qi.getStringValue().equalsIgnoreCase(buildingType)){
            Matcher m = p.matcher(buildingName);
            String numberStr = m.replaceAll("").trim();

            if(numberStr.length() == 1){
                buildingNameNew = "0" + numberStr + "期";
            }else if(numberStr.length() == 2){
                buildingNameNew = numberStr + "期";
            }else{
                throw new ECPBusinessException("期格式有误，正确的格式如：XX期或XX");
            }
        }else if(LookupItemEnum.buildingType_qu.getStringValue().equalsIgnoreCase(buildingType)){
            Matcher m = p.matcher(buildingName);
            String numberStr = m.replaceAll("").trim();

            if(numberStr.length() == 0){
                buildingNameNew = StringUtils.leftPad("",2,"0") + "区";
            }else if(numberStr.length() == 1){
                buildingNameNew = StringUtils.leftPad(numberStr,2,"0") + "区";
            }else if(numberStr.length() == 2){
                buildingNameNew = numberStr + "区";
            }else{
                throw new ECPBusinessException("区格式有误，正确的格式如：XX区或XX");
            }
        }else if(LookupItemEnum.buildingType_dongzuo.getStringValue().equalsIgnoreCase(buildingType)){
            /**
             * 验证栋座格式的栋部分
             */
            if(StringUtils.isBlank(CommonUtils.null2String(buildingName))){
                throw new ECPBusinessException("栋格式有误，正确的格式如：XXX栋或XXX");
            }

            String dongStr = buildingName;
            if(CommonUtils.null2String(buildingName).length() > 2){
                dongStr = buildingName.substring(0,3);
            }

            Matcher dongMatcher = p.matcher(dongStr);
            String dongNumberStr = dongMatcher.replaceAll("").trim();
            if(dongNumberStr.length() == 1){ // 只填一位数字
                buildingNameNew = StringUtils.leftPad(dongNumberStr,3,"0") + "栋";
            }else if(dongNumberStr.length() == 2){
                buildingNameNew = StringUtils.leftPad(dongNumberStr,3,"0") + "栋";
            }else if(dongNumberStr.length() == 3){
                buildingNameNew = dongNumberStr + "栋";
            }else{
                throw new ECPBusinessException("栋格式有误，正确的格式如：XXX栋或XXX");
            }

            /**
             * 验证栋座格式的座部分
             */
            String zuoStr = buildingName.substring(3);
            Matcher zuoMatcher = p.matcher(zuoStr);
            String zuoNumberStr = zuoMatcher.replaceAll("").trim();
            if(zuoNumberStr.length() == 0){
                buildingNameNew = buildingNameNew + "0座";
            }else if(zuoNumberStr.length() == 1){
                buildingNameNew = buildingNameNew + zuoNumberStr + "座";
            }else{
                throw new ECPBusinessException("座格式有误，正确的格式如：X座");
            }
        }else if(LookupItemEnum.buildingType_danyuanrukou.getStringValue().equalsIgnoreCase(buildingType)){
            Matcher m = p.matcher(buildingName);
            String numberStr = m.replaceAll("").trim();
            if(numberStr.length() == 0){
                buildingNameNew = "0单元";
            }else if(numberStr.length() == 1){
                buildingNameNew = numberStr + (numberStr.equalsIgnoreCase(buildingName) ? "单元" : buildingName.substring(1));
            }else{
                throw new ECPBusinessException("单元/入口格式有误，正确的格式如：X单元/入口或X");
            }
        }else if(LookupItemEnum.buildingType_ceng.getStringValue().equalsIgnoreCase(buildingType)){
            Matcher m = p.matcher(buildingName);
            String numberStr = m.replaceAll("").trim();
            if(numberStr.length() == 0){
                buildingNameNew = "00层";
            }else if(numberStr.length() == 1){
                buildingNameNew = "0" + numberStr + "层";
            }else if(numberStr.length() == 2){
                buildingNameNew = numberStr + "层";
            }else{
                throw new ECPBusinessException("层格式有误，正确的格式如：XX层或XX");
            }
        }else{
            Matcher m = p.matcher(buildingName.trim());
            String numberStr = m.replaceAll("").trim();
            if(numberStr.length() == 0){
                throw new ECPBusinessException("流水码格式有误，正确的格式如：XX");
            }else if(numberStr.length() == 1){
                buildingNameNew = StringUtils.leftPad(numberStr,2,"0");
            }else if(numberStr.length() == 2){
                buildingNameNew = StringUtils.leftPad(numberStr,2,"0");
            }else{
                buildingNameNew = numberStr;
            }
        }
        return buildingNameNew;
    }
}
