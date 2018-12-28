package com.everwing.coreservice.wy.core.service.impl.business.electmeter;/**
 * Created by wust on 2017/7/24.
 */

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.MeterEnum;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeter;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeterImport;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserList;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserSearch;
import com.everwing.coreservice.common.wy.service.business.electmeter.TcElectMeterImportService;
import com.everwing.coreservice.common.wy.service.sys.TSysLookupService;
import com.everwing.coreservice.common.wy.service.sys.TSysProjectService;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.core.task.TcElectMeterImportTask;
import com.everwing.coreservice.wy.dao.mapper.business.electmeter.TcElectMeterMapper;
import com.everwing.coreservice.wy.dao.mapper.business.meterdata.TcMeterDataMapper;
import com.everwing.coreservice.wy.dao.mapper.business.meterrelation.TcMeterRelationMapper;
import com.everwing.coreservice.wy.dao.mapper.business.watermeter.TcWaterMeterOperRecordMapper;
import com.everwing.coreservice.wy.dao.mapper.common.ImportExportMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcBuildingMapper;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysLookupMapper;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysUserMapper;
import com.everwing.myexcel.definition.ExcelDefinitionReader;
import com.everwing.myexcel.factory.DefinitionFactory;
import com.everwing.myexcel.factory.xml.XMLDefinitionFactory4commonImport;
import com.everwing.myexcel.resolver.poi.POIExcelResolver4commonImport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

/**
 *
 * Function:
 * Reason:
 * Date:2017/7/24
 * @author wusongti@lii.com.cn
 */
@Service("tcElectMeterImportServiceImpl")
public class TcElectMeterImportServiceImpl extends POIExcelResolver4commonImport implements TcElectMeterImportService {
    private static final Logger log = Logger.getLogger(TcElectMeterImportServiceImpl.class);

    @Autowired
    private TSysLookupMapper tSysLookupMapper;

    @Autowired
    TcElectMeterMapper tcElectMeterMapper;
    @Autowired
    TcWaterMeterOperRecordMapper tcWaterMeterOperRecordMapper;
    @Autowired
    TcMeterRelationMapper tcMeterRelationMapper;
    @Autowired
    TcMeterDataMapper tcMeterDataMapper;
    @Autowired
    TSysProjectService tSysProjectService;
    @Autowired
    TcBuildingMapper tcBuildingMapper;
    @Autowired
    private TSysUserMapper tSysUserMapper;
    
    @Autowired
    private FastDFSApi fastDFSApi;
    
    @Autowired
    private ImportExportMapper importExportMapper;

    @Autowired
    private TSysLookupService tSysLookupService;

    private WyBusinessContext ctx;

    /**
     * 导入
     * @param ctx
     * @param batchNo
     * @return
     */
    public MessageMap importElectMeter(WyBusinessContext ctx, String batchNo, String excelPath){
        this.ctx = ctx;
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
            Callable task = new TcElectMeterImportTask(this,ctx,batchNo,excelPath);
            Future<MessageMap> messageMapFuture = executorService.submit(task);
            mm = messageMapFuture.get();
        } catch (InterruptedException e) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(e.getMessage());
        } catch (ExecutionException e) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(e.getMessage());
        } catch (Exception e) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(e.getMessage());
        }finally{
            executorService.shutdown();
        }

        return mm;
    }

    private Map<String,Object> getImportElectMeter(WyBusinessContext ctx, List<ElectMeterImport> electMeterImportlist){
        this.ctx = ctx;
        Map<String,Object> map =new HashMap<String,Object>();
        List<ElectMeterImport> oldElectMeterList = new ArrayList<ElectMeterImport>();
        List<ElectMeterImport> newElectMeterList = new ArrayList<ElectMeterImport>();
        StringBuffer error = new StringBuffer();
        Integer successCount=0;
        Integer failCount =0;
        try {
            //存放本次excel导入的验证
            List<Map<String,String>> validateMap = new ArrayList<Map<String,String>>();
            //存放在数据库存在的验证
            List<Map<String,String>> existMap = new ArrayList<Map<String,String>>();
            List<ElectMeterImport> oldValidateList = new ArrayList<ElectMeterImport>();
            //这里再次校验，如果表等级不为1级表时，校验父表编号在数据库或者本次导入的Excel中是否存在,若存在，表等级是否满足(若为2级表，则父级表只能为1级表)
            List<String> noEditCode  = new ArrayList<String>(); //用来存放不能修改的表编码
            //新增的校验
            List<ElectMeterImport> newValidateList = new ArrayList<ElectMeterImport>();
            
            //检验收费对象重复的问题，表和收费对象只能一对一
            Map<String,String> realtionMap = new HashMap<String,String>();

            for(int i=0;i<electMeterImportlist.size();i++){
                ElectMeterImport electMeterImport = electMeterImportlist.get(i);
                ElectMeter electmeter = this.tcElectMeterMapper.getElectMeterByCode(electMeterImport.getCode(),ctx.getProjectId());
                electMeterImport.setCompanyId(ctx.getCompanyId());

                TcBuilding feeobj = tcBuildingMapper.getTcBulidByFullName(ctx.getProjectId(),electMeterImport.getRelationbuildingName());
                if(CommonUtils.isEmpty(feeobj)){
                    error.append("收费对象["+electMeterImport.getRelationbuildingName()+"]在系统中不存在").append("\n");
                    failCount++;
                    continue;
                }else{
                    electMeterImport.setRelationbuilding(feeobj.getBuildingCode());
                }

                //校验电表编号
                if(StringUtils.isBlank(electMeterImport.getCode())){
                    error.append("电表编号不能为空,请检查电表编号是否按照规则填写!").append("\n");
                    failCount++;
                    continue;
                }

                //校验电表名称长度
                String eletName = electMeterImport.getElectricitymetername();
                //校验电表名称
                if(StringUtils.isBlank(eletName)){
                    error.append("电表名称不能为空,请检查电表名称是否按照规则填写!").append("\n");
                    failCount++;
                    continue;
                }

                if(StringUtils.isNotBlank(eletName) && eletName.length()>30){
                    error.append("电表名称["+eletName+"]需在30个字符以内!").append("\n");
                    failCount++;
                    continue;
                }

                //校验电表位置
                if(StringUtils.isBlank(electMeterImport.getLocationName())){
                    error.append("电表位置不能为空,请检查电表位置是否按照规则填写!").append("\n");
                    failCount++;
                    continue;
                }

                //校验电表等级
                if(electMeterImport.getMeterLevel() ==null || StringUtils.isBlank(electMeterImport.getMeterLevel().toString())){
                    error.append("电表等级不能为空,请检查表等级是否按照规则填写!").append("\n");
                    failCount++;
                    continue;
                }
                //
                //校验品牌
                String brand = electMeterImport.getBrand();
                if(StringUtils.isNotBlank(brand) && brand.length()>12){
                    error.append("电表名称["+eletName+"]的品牌需在12个字符以内!").append("\n");
                    failCount++;
                    continue;
                }
                //校验规格
                String spec = electMeterImport.getSpecs();
                if(StringUtils.isNotBlank(spec) && spec.length()>15){
                    error.append("电表名称["+eletName+"]的规格需在15个字符以内!").append("\n");
                    failCount++;
                    continue;
                }
                //校验供应商
                String provider = electMeterImport.getProvider();
                if(StringUtils.isNotBlank(provider) && provider.length()>30){
                    error.append("电表名称["+eletName+"]的供应商需在30个字符以内!").append("\n");
                    failCount++;
                    continue;
                }
                //校验供应商联系方式
                String providerPhone = electMeterImport.getProviderphone();
                if(StringUtils.isNotBlank(providerPhone) && providerPhone.length()>15){
                    error.append("电表名称["+eletName+"]的供应商联系方式需在15个字符以内!").append("\n");
                    failCount++;
                    continue;
                }
                //校验计费和收费对象的关系
                Integer isBilling = electMeterImport.getIsbilling();
                if(isBilling.equals(MeterEnum.METER_YES.getIntValue())){
                    if(StringUtils.isEmpty(electMeterImport.getRelationbuildingName())){
                        error.append("电表名称["+eletName+"]需要计费时,收费对象不能为空!").append("\n");
                        failCount++;
                        continue;
                    }
                }

                //校验表等级和父表编号的关系
                Integer meterLevel = electMeterImport.getMeterLevel();
                String parentCode = electMeterImport.getParentcode();
                if(meterLevel.equals(1) || meterLevel.equals(11)){
                    if(StringUtils.isNotEmpty(parentCode)){
                        error.append("电表名称["+eletName+"]的表等级为一级表或者为M级表时,不能有父表编码").append("\n");
                        failCount++;
                        continue;
                    }
                }else{//这里校验，当电表等级为非一级表时，其父表编号不能为空
                    if(StringUtils.isEmpty(parentCode)){
                        error.append("电表名称["+eletName+"]的表等级不为一级表时,父表编码不能为空").append("\n");
                        failCount++;
                        continue;
                    }
                }
                /**
                 * TODO 此处的参数应该是一个登陆账号或者员工号，这样才能查出一条数据。否则参数是员工姓名的话，可能查出多个员工，该用哪个员工呢？
                 */
                TSysUserSearch tSysUserSearch = new TSysUserSearch();
                tSysUserSearch.setStaffNumber(electMeterImport.getAssembleperson());
                List<TSysUserList> userList = tSysUserMapper.findByCondition(tSysUserSearch);
                if(CommonUtils.isEmpty(userList)){
                    error.append("安装人员["+electMeterImport.getAssembleperson()+"]在系统中不存在").append("\n");
                    failCount++;
                    continue;
                }else{
                    electMeterImport.setAssembleperson(userList.get(0).getStaffName());
                    electMeterImport.setAssemblepersonId(userList.get(0).getStaffNumber());
                }
                TcBuilding location = tcBuildingMapper.getTcBulidByFullName(ctx.getProjectId(),electMeterImport.getLocationName());
                if(CommonUtils.isEmpty(location)){
                    error.append("位置["+electMeterImport.getLocationName()+"]在系统中不存在").append("\n");
                    failCount++;
                    continue;
                }else{
                    electMeterImport.setLocation(location.getBuildingCode());
                }
                Map<String,String> valiMap = new HashMap<String,String>();
                Map<String,String> exVliMap = new HashMap<String,String>();
                if(CommonUtils.isEmpty(electmeter)){
                    electMeterImport.setCreaterId(ctx.getUserId());
                    electMeterImport.setCreateTime(new Date());
                    electMeterImport.setProjectId(ctx.getProjectId());
                    electMeterImport.setProjectCode(ctx.getProjectId());
                    electMeterImport.setProjectName(ctx.getProjectName());
                    String reationId = electMeterImport.getRelationbuilding();
                    if(StringUtils.isNotBlank(reationId) && !realtionMap.containsKey(reationId)){ //校验本次导入的excel是否有重复的收费对象
                    	realtionMap.put(reationId, reationId);
                    	//然后去数据库查询是否有存在的收费对象，如果没有才可以保存
                    	ElectMeter reElect = this.tcElectMeterMapper.getElectMeterByReationId(ctx.getProjectId(), reationId);
                    	if(CommonUtils.isEmpty(reElect)){ //收费对象在数据库中不存在，可以保存
                    		newElectMeterList.add(electMeterImport);
                            valiMap.put("code", electMeterImport.getCode());
                            valiMap.put("meterLevel", electMeterImport.getMeterLevel().toString());
                            validateMap.add(valiMap);
                    	}else{
                    		error.append("电表["+electMeterImport.getElectricitymetername()+"]的收费对象["+electMeterImport.getRelationbuildingName()+"]已经被表名["+reElect.getRelationbuildingName()+"]占用。");
                    	}
                    	
                    }else{
                    	error.append("电表["+electMeterImport.getElectricitymetername()+"]的收费对象重复或者收费对象编号不存在!");
                    }
                    
                }else{
                	 electMeterImport.setModifyId(ctx.getUserId());
                     electMeterImport.setModifyTime(new Date());
                     electMeterImport.setProjectId(ctx.getProjectId());
                     electMeterImport.setProjectCode(ctx.getProjectId());
                     electMeterImport.setProjectName(ctx.getProjectName());
                	String reationId = electMeterImport.getRelationbuilding();
                    if(StringUtils.isNotBlank(reationId) && !realtionMap.containsKey(reationId)){ //校验本次导入的excel是否有重复的收费对象
                    	realtionMap.put(reationId, reationId);
                    	//然后去数据库查询是否有存在的收费对象，如果没有才可以保存
                    	ElectMeter reElect = this.tcElectMeterMapper.getElectMeterByReationId(ctx.getProjectId(), reationId);
                    	if(CommonUtils.isEmpty(reElect)){ //收费对象在数据库中不存在，可以保存
                    		 exVliMap.put("code", electmeter.getCode());
                             exVliMap.put("meterLevel", String.valueOf(electmeter.getMeterLevel()));
                             existMap.add(exVliMap);
                             valiMap.put("code", electMeterImport.getCode());
                             valiMap.put("meterLevel", electMeterImport.getMeterLevel().toString());
                             validateMap.add(valiMap);
                             oldElectMeterList.add(electMeterImport);
                    	}else{
                    		error.append("电表["+electMeterImport.getElectricitymetername()+"]的收费对象["+electMeterImport.getRelationbuildingName()+"]已经被表名["+reElect.getRelationbuildingName()+"]占用。");
                    	}
                    	
                    }else{
                    	error.append("电表["+electMeterImport.getElectricitymetername()+"]的收费对象重复或者收费对象编号不存在!");
                    }
                }
            }
            for(int p=0;p<newElectMeterList.size();p++){
                ElectMeterImport electMeterImport =newElectMeterList.get(p);
                //构建比较对象
                //如果是一级表和11的M级表则不需要查看父表情况
                if(electMeterImport.getMeterLevel().equals(1) || electMeterImport.getMeterLevel().equals(11)){
                    newValidateList.add(electMeterImport);
                    successCount++;
                }else{
                    Map<String,String> importMap = new HashMap<String,String>();
                    importMap.put("code", electMeterImport.getParentcode());
                    importMap.put("meterLevel", String.valueOf(electMeterImport.getMeterLevel()-1));
                    if(validateMap.contains(importMap)){
                        newValidateList.add(electMeterImport);
                        successCount++;
                    }else{
                        if(existMap.contains(importMap)){
                            newValidateList.add(electMeterImport);
                            //将如果从数据库查询出来的表作为父表则，该父表在本次导入时不能做修改。
                            noEditCode.add(importMap.get("code"));

                        }else{
                            //根据父表编号查看 父表是否在数据库存在
                            ElectMeter eletm = this.tcElectMeterMapper.getElectMeterByCode(electMeterImport.getParentcode(),ctx.getProjectId());
                            if(CommonUtils.isEmpty(eletm)){
                                error.append("电表名称为["+electMeterImport.getElectricitymetername()+"]的父表编号不存在!");
                                failCount++;
                                continue;
                            }else{
                                if(StringUtils.isNotBlank(eletm.getMeterLevel().toString()) && String.valueOf(electMeterImport.getMeterLevel()-1).equals(eletm.getMeterLevel().toString())){
                                    newValidateList.add(electMeterImport);
                                    successCount++;
                                }else{
                                    error.append("电表名称为["+electMeterImport.getElectricitymetername()+"]的父表编号不存在或者父表的表等级和该表的表等级不匹配!");
                                    failCount++;
                                    continue;
                                }
                            }
                        }
                    }
                }
            }
            //编辑的校验
            for(int j=0;j<oldElectMeterList.size();j++){
                ElectMeterImport electMeterImport =oldElectMeterList.get(j);
                //如果是一级表则不需要查看父表的情况
                if(electMeterImport.getMeterLevel().equals(1)){
                    oldValidateList.add(electMeterImport);
                    successCount++;
                }else{
                    //构建比较对象
                    Map<String,String> importMap = new HashMap<String,String>();
                    importMap.put("code", electMeterImport.getParentcode());
                    importMap.put("meterLevel", String.valueOf(electMeterImport.getMeterLevel()-1));
                    if(validateMap.contains(importMap)){
                        oldValidateList.add(electMeterImport);
                        successCount++;
                    }else{
                        //如果存在
                        if(noEditCode.contains(electMeterImport.getCode())){
                            failCount++;
                            continue; //如果这条导入记录被作为父表则不能做修改；故要移出修改的集合里

                        }else{
                            //如果在本次导入的Excel里不存在，则转到数据库去查找，如果数据库查找到后且能匹配上表的等级则该条导入记录不做更新操作
                            if(existMap.contains(importMap)){
                                oldValidateList.add(electMeterImport);
                                //
                                noEditCode.add(importMap.get("code"));
                                successCount++;
                            }else{
                                //根据父表编号查看 父表是否在数据库存在
                                ElectMeter eletm = this.tcElectMeterMapper.getElectMeterByCode(electMeterImport.getParentcode(),ctx.getProjectId());
                                if(CommonUtils.isEmpty(eletm)){
                                    error.append("电表名称为["+electMeterImport.getElectricitymetername()+"]的父表编号不存在!");
                                    failCount++;
                                    continue;
                                }else{
                                    if(StringUtils.isNotBlank(eletm.getMeterLevel().toString()) && String.valueOf(electMeterImport.getMeterLevel()-1).equals(eletm.getMeterLevel().toString())){
                                        newValidateList.add(electMeterImport);
                                        successCount++;
                                    }else{
                                        error.append("电表名称为["+electMeterImport.getElectricitymetername()+"]的父表编号不存在或者父表的表等级和该表的表等级不匹配!");
                                        failCount++;
                                        continue;
                                    }
                                }
                            }
                        }
                    }
                }

            }
            map.put("error", error);
            map.put("oldElectMeterList", oldValidateList);
            map.put("newElectMeterList", newValidateList);
            map.put("successCount", successCount);
            map.put("failCount", failCount);
            return map;
        } catch (Exception e) {
            map.put("oldElectMeterList", new ArrayList<ElectMeterImport>());
            map.put("newElectMeterList", new ArrayList<ElectMeterImport>());
            log.info(CommonUtils.log(e.getMessage()));
            map.put("successCount", 0);
            map.put("failCount", electMeterImportlist.size());
            map.put("error", error.append(e.getMessage()));
        }


        return map;
    }


    @Transactional(rollbackFor=Exception.class)
    public MessageMap importElect(WyBusinessContext ctx,List<ElectMeterImport> electMeterImportlist){
        this.ctx = ctx;
        MessageMap mm = new MessageMap();
        Map<String,Object> map = getImportElectMeter(ctx,electMeterImportlist);
        List<ElectMeterImport> oldList = (List<ElectMeterImport>)map.get("oldElectMeterList");
        List<ElectMeterImport> newList =(List<ElectMeterImport>)map.get("newElectMeterList");

        int commitSize = 1000;//默认每次提交数量
        // 新增
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(newList)){
            int size = newList.size();
            if(size <= commitSize){
                tcElectMeterMapper.batchadd(newList);
            }else{
                if(size % commitSize == 0){
                    int count = size / commitSize;
                    for(int i=0;i<count;i++){
                        int fromIndex = i * commitSize;
                        int toIndex = (i+1) * commitSize;
                        tcElectMeterMapper.batchadd(newList.subList(fromIndex,toIndex));
                    }
                }else{
                    int endIndex = 0;
                    int count = size / commitSize;
                    for(int i=0;i<count;i++){
                        int fromIndex = i * commitSize;
                        int toIndex = (i+1) * commitSize;
                        endIndex = toIndex;
                        tcElectMeterMapper.batchadd(newList.subList(fromIndex,toIndex));
                    }
                    tcElectMeterMapper.batchadd(newList.subList(endIndex,size));
                }
            }
        }

        //修改
        // 修改
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(oldList)){
            int size = oldList.size();
            if(size <= commitSize){
                tcElectMeterMapper.batchupdate(oldList);
            }else {
                if(size % commitSize == 0){
                    int count = size / commitSize;
                    for(int i=0;i<count;i++){
                        int fromIndex = i * commitSize;
                        int toIndex = (i+1) * commitSize;
                        tcElectMeterMapper.batchupdate(oldList.subList(fromIndex,toIndex));
                    }
                }else{
                    int endIndex = 0;
                    int count = size / commitSize;
                    for(int i=0;i<count;i++){
                        int fromIndex = i * commitSize;
                        int toIndex = (i+1) * commitSize;
                        endIndex = toIndex;
                        tcElectMeterMapper.batchupdate(oldList.subList(fromIndex,toIndex));
                    }
                    tcElectMeterMapper.batchupdate(oldList.subList(endIndex,size));
                }
            }
        }
        Integer successCount = (Integer)map.get("successCount");
        Integer failCount = (Integer)map.get("failCount");
        StringBuffer error = (StringBuffer)map.get("error");
        if(failCount>0 && successCount>0){ //部分导入成功
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("电表导入部分成功;成功了"+successCount+"条;失败"+failCount+"条;"+error.toString());
        }
        if(failCount==0&&successCount==electMeterImportlist.size()){//全部导入成功
            mm.setFlag(MessageMap.INFOR_SUCCESS);
            mm.setMessage("全部导入成功");
        }
        if(successCount==0&&failCount==electMeterImportlist.size()){//全部导入失败
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage("电表导入全部失败;失败了"+failCount+"条;"+error.toString());
        }
        return mm;
    }

    @Override
    protected ExcelDefinitionReader getExcelDefinition() {
        DefinitionFactory definitionReaderFactory = new XMLDefinitionFactory4commonImport("importExport/import/xml/elect_meter.xml");
        return definitionReaderFactory.createExcelDefinitionReader();
    }

    @Override
    protected String getLookupItemCodeByName(String lookupCode, String parentCode, String name) {
        return null;
    }

    protected String getLookupCodeByName(String parentCode, String name) {
        return tSysLookupService.getLookupCodeByName(ctx,parentCode,name);
    }

    protected String getLookupItemCodeByName(String parentCode, String name) {
        return tSysLookupService.getLookupItemCodeByName(ctx,parentCode,name);
    }
}
