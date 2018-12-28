package com.everwing.coreservice.wy.core.service.impl.business.watermeter;/**
 * Created by wust on 2017/7/24.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.cache.DataDictionaryUtil;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProject;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectList;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectSearch;
import com.everwing.coreservice.common.wy.service.business.watermeter.TcWaterMeterImportService;
import com.everwing.coreservice.common.wy.service.sys.TSysLookupService;
import com.everwing.coreservice.common.wy.service.sys.TSysProjectService;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.core.task.TcWaterMeterImportTask;
import com.everwing.coreservice.wy.dao.mapper.business.watermeter.TcWaterMeterMapper;
import com.everwing.coreservice.wy.dao.mapper.common.ImportExportMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcBuildingMapper;
import com.everwing.myexcel.definition.ExcelDefinitionReader;
import com.everwing.myexcel.factory.DefinitionFactory;
import com.everwing.myexcel.factory.xml.XMLDefinitionFactory4commonImport;
import com.everwing.myexcel.resolver.poi.POIExcelResolver4commonImport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * Function:
 * Reason:
 * Date:2017/7/24
 * @author wusongti@lii.com.cn
 */
@Service("tcWaterMeterImportServiceImpl")
public class TcWaterMeterImportServiceImpl extends POIExcelResolver4commonImport implements TcWaterMeterImportService {
    private static final Logger log = Logger.getLogger(TcWaterMeterImportServiceImpl.class);



    private WyBusinessContext ctx;

    @Autowired
    private TcWaterMeterMapper tcWaterMeterMapper;

    @Autowired
    private TcBuildingMapper tcBuildingMapper;
    @Autowired
    private TSysProjectService tSysProjectService;

    @Autowired
    private TSysLookupService tSysLookupService;
    
    @Autowired
    private FastDFSApi fastDFSApi;
    
    @Autowired
    private ImportExportMapper importExportMapper;

    /**
     * 对导入数据不符合要求的行进行过滤
     */
    private List FilteImportList(List<TcWaterMeter> list,StringBuffer erro){
        List<TcWaterMeter> filteList=new ArrayList<>();
        //添加对关联收费对象的一对一过滤，防止导入时造成关联收费对象的重复
        Map<String, String> fileMap=new HashMap<>();
        for ( int i=1; i<=list.size(); i++) {
            TcWaterMeter tcWaterMeter=list.get(i-1);
            //非空校验
            if(CommonUtils.isEmpty(tcWaterMeter.getCode())){
                erro.append("第"+i+"行数据缺少水表编号，无法导入");
                continue;
            }else if(CommonUtils.isEmpty(String.valueOf(tcWaterMeter.getIsCircle()))){
                erro.append("第"+i+"行数据缺少是否循环使用值，无法导入");
                continue;
            }else if(CommonUtils.isEmpty(tcWaterMeter.getMaxAmount())){
                erro.append("第"+i+"行数据缺少最大值，无法导入");
                continue;
            }else if(CommonUtils.isEmpty(String.valueOf(tcWaterMeter.getIsPublic()))){
                erro.append("第"+i+"行数据缺少是否公用值，无法导入");
                continue;
            }else if(CommonUtils.isEmpty(tcWaterMeter.getMeterLevel())){
                erro.append("第"+i+"行数据缺少水表等级，无法导入");
                continue;
            }else if(CommonUtils.isEmpty(tcWaterMeter.getPosition())){
                erro.append("第"+i+"行数据缺少安装位置，无法导入");
                continue;
            }else if(CommonUtils.isEmpty(tcWaterMeter.getRelationBuilding())){
                erro.append("第"+i+"行数据缺少关联收费对象，无法导入");
                continue;
            }else if(CommonUtils.isEmpty(tcWaterMeter.getWaterMeterName())){
                erro.append("第"+i+"行数据缺少水表名称，无法导入");
                continue;
            }else if(CommonUtils.isEmpty(tcWaterMeter.getProjectId())){
                erro.append("第"+i+"行数据缺少项目id，无法导入");
                continue;
            }else if(CommonUtils.isEmpty(String.valueOf(tcWaterMeter.getIsBilling()))){
                erro.append("第"+i+"行数据缺少是否计费，无法导入");
                continue;
            }else if(CommonUtils.isEmpty(String.valueOf(tcWaterMeter.getUserType()))){
                erro.append("第"+i+"行数据缺少使用类型，无法导入");
                continue;
            }else if(CommonUtils.isEmpty(String.valueOf(tcWaterMeter.getType()))){
                erro.append("第"+i+"行数据缺少使用类型，无法导入");
                continue;
            }else if(CommonUtils.isEmpty(String.valueOf(tcWaterMeter.getState()))){
                erro.append("第"+i+"行数据缺少状态，无法导入");
                continue;
            }else if(CommonUtils.isEmpty(tcWaterMeter.getRate())){
                erro.append("第"+i+"行数据缺少倍率，无法导入");
                continue;
            }
            
            if(CommonUtils.isEmpty(String.valueOf(tcWaterMeter.getMinAmount()))){
                tcWaterMeter.setMinAmount(0.0);
            }
            
            if(CommonUtils.isEmpty(String.valueOf(tcWaterMeter.getInitAmount()))){
                tcWaterMeter.setInitAmount(0.0);
            }
            
            if(fileMap.containsKey(tcWaterMeter.getRelationBuilding())) {
            	//本次导入已经包含有此关联建筑信息的
            	 erro.append("第"+i+"行关联建筑信息重复");
            	 continue;
            }
            
            filteList.add(tcWaterMeter);
            fileMap.put(tcWaterMeter.getRelationBuilding(), "fileMap");
        }
        return filteList;
    }

    @Override
    public MessageMap importWaterMeter(WyBusinessContext ctx, String batchNo, String excelPath) {
        this.ctx = ctx;
        // TODO Auto-generated method stub
        MessageMap mm = new MessageMap();

        //采用分布式文件服务器方式来做
        //通过batchNo查询uploadFileId信息作为参数传递给文件服务器
        TSysImportExportList tSysImportExportListExist = null;
        TSysImportExportSearch condition = new TSysImportExportSearch();
        condition.setBatchNo(batchNo);
        List<TSysImportExportList> tSysImportExportListList = importExportMapper.findByCondtion(condition);
        if(CollectionUtils.isNotEmpty(tSysImportExportListList)){
            tSysImportExportListExist = tSysImportExportListList.get(0);
        }else{
            throw new ECPBusinessException("没有文件上传记录，请先上传文件");
        }
        
        try {
            RemoteModelResult<UploadFile> remoteModelResult = fastDFSApi.loadFilePathById(tSysImportExportListExist.getUploadFileId());
            if(remoteModelResult.isSuccess()){
                UploadFile uploadFile = remoteModelResult.getModel();
                URL url = new URL(uploadFile.getPath());
                HttpURLConnection uc = (HttpURLConnection) url.openConnection();
                uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true
                uc.connect();
                super.excelInputStream = uc.getInputStream();
            }
        } catch (Exception e) {
            throw new ECPBusinessException("导入失败，读取文件失败："+e.getMessage());
        }

        ExecutorService executorService = Executors.newFixedThreadPool(1);

        try {
            Callable task = new TcWaterMeterImportTask(this,ctx,batchNo,excelPath);
            Future<MessageMap> messageMapFuture = executorService.submit(task);
            mm = messageMapFuture.get();
        }  catch (Exception e) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(e.getMessage());
        }
        executorService.shutdown();

        while (true) {
            if (executorService.isTerminated()) {
                break;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {

            }
        }
 
        executorService.shutdown();
        return mm;
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String,Object> getImportWaterMeter(String companyId, List<TcWaterMeter> waterMeterImportlist){

        Map<String,Object> map =new HashMap<String,Object>();
        List<TcWaterMeter> existWaterMeterList = new ArrayList<TcWaterMeter>();
        List<TcWaterMeter> newWaterMeterList = new ArrayList<TcWaterMeter>();
        StringBuffer error = new StringBuffer();
        //获取所有需要导入数据的code集合，用户删选数据库中已存在的code（做update操作），以及导入需要新增的code（做insert操作）
        Set<String> codeSet=new HashSet<>();//水表code
        Set<String> codeSetBuildingCode=new HashSet<>();//关联收费对象
        Set<String> codeProject=new HashSet<>();//项目名称
        Set<String> codeMeterPosition=new HashSet<>();//水表安装位置，允许多表公用

        waterMeterImportlist=FilteImportList(waterMeterImportlist,error);

        String projectName="";
        for (TcWaterMeter tcWaterMeter : waterMeterImportlist) {
        	projectName=tcWaterMeter.getProjectId();
            if(CommonUtils.isNotEmpty(tcWaterMeter.getCode())){
                codeSet.add(tcWaterMeter.getCode());
            }
            if(CommonUtils.isNotEmpty(tcWaterMeter.getRelationBuilding())){
                codeSetBuildingCode.add(tcWaterMeter.getRelationBuilding());
            }
            if(CommonUtils.isNotEmpty(tcWaterMeter.getProjectId())){
                codeProject.add(tcWaterMeter.getProjectId().trim());
            }
            if(CommonUtils.isNotEmpty(tcWaterMeter.getPosition())){
                codeMeterPosition.add(tcWaterMeter.getPosition());
            }

        }

        List<String> codeList=new ArrayList<>(codeSet);
        List<String> codeListBuilding=new ArrayList<>(codeSetBuildingCode);
        List<String> codeListProject=new ArrayList<>(codeProject);
        List<String> codePositionList=new ArrayList<>(codeMeterPosition);
        
        if(CommonUtils.isEmpty(codeList) 
        		|| CommonUtils.isEmpty(codeListBuilding)
        		|| CommonUtils.isEmpty(codeListProject)
        		|| CommonUtils.isEmpty(codePositionList)) {
        	//四个必填的字段如果有一个为空，导入失败，数据不完整
        	Map resultMap=new HashMap<>();
        	resultMap.put("msg", new MessageMap(MessageMap.INFOR_ERROR,"请检查水表编号，关联建筑，安装位置，项目信息是否完整"));
            return resultMap;
        }
        
        if(codeListProject.size()>1){
            Map resultMap=new HashMap<>();
            resultMap.put("msg", new MessageMap(MessageMap.INFOR_WARNING,"请按照单个项目倒入水表信息"));
            return resultMap;
        }

        //查询出所有存在的code的水表集合
        //这里需要考虑数据很多的时候，使用 in 语句 () 会有问题
        List<TcWaterMeter> waterMeterList=new ArrayList<>();
        if(codeList.size()>500){
            int num=codeList.size()/500;
            for(int i=0;i<=num;i++){
                List<TcWaterMeter> existList=new ArrayList<>();
                if(i == num){
                    existList= tcWaterMeterMapper.findMetersByCodes(codeList.subList(i*500, codeList.size()));
                }else{
                    existList= tcWaterMeterMapper.findMetersByCodes(codeList.subList(i*500, (i+1)*500));
                }
                waterMeterList.addAll(existList);
            }
        }else{
            waterMeterList= tcWaterMeterMapper.findMetersByCodes(codeList);
        }

        Map<String, Object> paramMap=new HashMap<String, Object>();
        paramMap.put("projectName",projectName);
        List<TcBuilding> buildingList=new ArrayList<>();
        //通过关联建筑名称查询关联建筑code(写入数据库用)
        if(codeListBuilding.size()>500){
            int num=codeListBuilding.size()/500;
            for(int i=0;i<=num;i++){
                List<TcBuilding> buildings=new ArrayList<>();
                if(i == num){
                	paramMap.put("list", codeListBuilding.subList(i*500, codeListBuilding.size()));
                    buildings= tcBuildingMapper.getTcBulidCodeList(paramMap);
                }else{
                	paramMap.put("list", codeListBuilding.subList(i*500, (i+1)*500));
                    buildings= tcBuildingMapper.getTcBulidCodeList(paramMap);
                }
                buildingList.addAll(buildings);
            }
        }else{
        	paramMap.put("list", codeListBuilding);
            buildingList=tcBuildingMapper.getTcBulidCodeList(paramMap);
        }

        //水表安装位置
        List<TcBuilding> positionList=new ArrayList<>();
        //通过关联建筑名称查询关联建筑code(写入数据库用)
        if(positionList.size()>500){
            int num=codePositionList.size()/500;
            for(int i=0;i<=num;i++){
                List<TcBuilding> positions=new ArrayList<>();
                if(i == num){
                	paramMap.put("list", codeListBuilding.subList(i*500, codeListBuilding.size()));
                    positions= tcBuildingMapper.getTcBulidCodeList(paramMap);
                }else{
                	paramMap.put("list",  codeListBuilding.subList(i*500, (i+1)*500));
                    positions= tcBuildingMapper.getTcBulidCodeList(paramMap);
                }
                positionList.addAll(positions);
            }
        }else{
        	paramMap.put("list", codePositionList);
            positionList=tcBuildingMapper.getTcBulidCodeList(paramMap);
        }

//		List<TcBuilding> buildingList= tcBuildingMapper.getTcBulidCodeList(codeListBuilding);

        // TODO 查t_sys_user表，或者后期会提供一个接口大家共用
        //通过安装人名称查询安装人员工编号信息(写入数据库用)
        //List<Staff> staffList= staffMapper.getstaffByName(codeListStaff);
        //项目id
        TSysProjectSearch tSysProjectSearch = new TSysProjectSearch();
        tSysProjectSearch.setName(codeListProject.get(0));
        BaseDto baseDto  = tSysProjectService.findByCondition(ctx,tSysProjectSearch);
        List<TSysProjectList> tSysProjectLists = baseDto.getLstDto();



        for (TcWaterMeter tcWaterMeter : waterMeterImportlist) {
            //code不允许空
            if(CommonUtils.isEmpty(tcWaterMeter.getCode())){
                continue;
            }
            //将关联收费对象修改为code
            int num=0;//标识项目，关联收费对象，水表位置是否被替换
            if(CommonUtils.isNotEmpty(tcWaterMeter.getRelationBuilding())){
                for (TcBuilding building : buildingList) {
                    if(building.getBuildingFullName().equals(tcWaterMeter.getRelationBuilding())){
                        tcWaterMeter.setRelationBuilding(building.getBuildingCode());
                        num++;
                        break;
                    }
                }
                if(num==0){
                    error.append("收费对象["+tcWaterMeter.getRelationBuilding()+"]不符合要求").append("\n");
                    continue;
                }

            }else{
                error.append("水表编号["+tcWaterMeter.getCode()+"]对应的关联收费对象为空，不允许导入").append("\n");
                continue;
            }

            num=0;
            //将水表安装的位置修改为code(后面抄水表要用到)
            if(CommonUtils.isNotEmpty(tcWaterMeter.getPosition())){
                for (TcBuilding building : positionList) {
                    if(building.getBuildingFullName().equals(tcWaterMeter.getPosition())){
                        tcWaterMeter.setPosition(building.getBuildingCode());
                        num++;
                        break;
                    }
                }
                if(num==0){
                    error.append("收费对象["+tcWaterMeter.getPosition()+"]在系统中不存在").append("\n");
                    continue;
                }

            }else{
                error.append("水表编号["+tcWaterMeter.getCode()+"]对应水表安装位置为空，不允许导入").append("\n");
                continue;
            }

            //项目信息
            num=0;
            // TODO 项目遍历问题
            if(CommonUtils.isNotEmpty(tcWaterMeter.getProjectId())){
                for (TSysProject project : tSysProjectLists) {
                    if(project.getName().trim().equals(tcWaterMeter.getProjectId().trim())){
                        tcWaterMeter.setProjectId(project.getCode());
                        num++;
                        break;
                    }
                }
                if(num==0){
                    error.append("水表编号["+tcWaterMeter.getCode()+"]对应的项目名称系统不存在").append("\n");
                    continue;
                }
            }else{
                error.append("水表编号["+tcWaterMeter.getCode()+"]对应项目名称为空，不允许导入").append("\n");
                continue;
            }


            //是否为已存在水表信息
            int isAdd=0;
            for (TcWaterMeter tcW : waterMeterList) {
                if(tcW.getCode().equals(tcWaterMeter.getCode())){
                    existWaterMeterList.add(tcWaterMeter);//已存在
                    isAdd++;
                    continue;
                }
            }
            if(isAdd > 0) {
            	continue;
            }
            newWaterMeterList.add(tcWaterMeter);
            
        }

        map.put("error", error);
        map.put("existWaterMeterList", existWaterMeterList);
        map.put("newWaterMeterList", newWaterMeterList);

        return map;
    }

    /**
     * 导入
     * @param ctx
     * @param batchNo
     * @param tcHouseImportListList
     * @return
     */
    /**
     * 通过excel批量导入水表基础数据信息
     * @param ctx
     * @param batchNo
     * @return MessageMap
     */
    public MessageMap importWaterMeterInfo(WyBusinessContext ctx, String batchNo, List<TcWaterMeter> waterMeterImportlist){
        MessageMap mm = new MessageMap();
        try {
            Map<String,Object> map = getImportWaterMeter(ctx.getCompanyId(),waterMeterImportlist);
            if( null != map.get("msg")){
                return (MessageMap) map.get("msg");
            }
            List<TcWaterMeter> existWaterList = (List<TcWaterMeter>)map.get("existWaterMeterList");
            List<TcWaterMeter> newWaterList = (List<TcWaterMeter>)map.get("newWaterMeterList");
            int commitSize = 500;//默认每次提交数量
            int insertNum=0;
            int updateNum=0;
            if(CollectionUtils.isNotEmpty(newWaterList)){
                int size = newWaterList.size();
                if(size <= commitSize){
                    insertNum=this.tcWaterMeterMapper.batchInsert(newWaterList);//   .batchadd(newElectList);
                }else{
                    if(size % commitSize == 0){
                        int count = size / commitSize;
                        for(int i=0;i<=count;i++){
                            int fromIndex = i * commitSize;
                            int toIndex = (i+1) * commitSize;
                            insertNum+= this.tcWaterMeterMapper.batchInsert(newWaterList.subList(fromIndex,toIndex));
                        }
                    }else{
                        int endIndex = 0;
                        int count = size / commitSize;
                        for(int i=0;i<count;i++){
                            int fromIndex = i * commitSize;
                            int toIndex = (i+1) * commitSize;
                            endIndex = toIndex;
                            insertNum+=  this.tcWaterMeterMapper.batchInsert(newWaterList.subList(fromIndex,toIndex));
                        }
                        insertNum+=  this.tcWaterMeterMapper.batchInsert(newWaterList.subList(endIndex,size));
                    }
                }
            }
            //修改
            if(CollectionUtils.isNotEmpty(existWaterList)){
                int size = existWaterList.size();
                if(size <= commitSize){
                    updateNum=tcWaterMeterMapper.bachUpdateForImport(existWaterList);
                }else{
                    if(size % commitSize == 0){
                        int count = size / commitSize;
                        for(int i=0;i<=count;i++){
                            int fromIndex = i * commitSize;
                            int toIndex = (i+1) * commitSize;
                            updateNum += tcWaterMeterMapper.bachUpdateForImport(existWaterList.subList(fromIndex,toIndex));
                        }
                    }else{
                        int endIndex = 0;
                        int count = size / commitSize;
                        for(int i=0;i<count;i++){
                            int fromIndex = i * commitSize;
                            int toIndex = (i+1) * commitSize;
                            endIndex = toIndex;
                            updateNum +=  tcWaterMeterMapper.bachUpdateForImport(existWaterList.subList(fromIndex,toIndex));
                        }
                        updateNum +=  tcWaterMeterMapper.bachUpdateForImport(existWaterList.subList(endIndex,size));
                    }
                }
            }
            StringBuffer error =	(StringBuffer)map.get("error");
            if(error.length()==0){
                mm.setFlag(MessageMap.INFOR_SUCCESS);
                mm.setMessage("全部导入成功");
            }else{
                mm.setFlag(MessageMap.INFOR_WARNING);
                mm.setMessage("成功插入"+insertNum+"条水表数据,成功修改"+updateNum+"条水表数据"+error);
            }

        } catch (Exception e) {
            log.error("=================="+e+"==================");
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage("导入出现异常,失败"+e.getMessage());
        }
        return mm;
    }

    
    @Override
    protected ExcelDefinitionReader getExcelDefinition() {
        DefinitionFactory definitionReaderFactory = new XMLDefinitionFactory4commonImport("importExport/import/xml/water_meter.xml");
        return definitionReaderFactory.createExcelDefinitionReader();
    }

    @Override
    protected String getLookupItemCodeByName(String lookupCode, String parentCode, String name) {
        WyBusinessContext ctx = WyBusinessContext.getContext();
        return DataDictionaryUtil.getLookupItemCodeByParentCodeAndName(ctx.getCompanyId(),parentCode,name);
    }
}
