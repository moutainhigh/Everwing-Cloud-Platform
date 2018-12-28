package com.everwing.coreservice.wy.core.service.impl.cust.person;

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.cache.DataDictionaryUtil;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNew;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNewImportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.common.wy.service.cust.person.PersonCustImportService;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.core.task.PersonCustImportTask;
import com.everwing.coreservice.wy.dao.mapper.common.ImportExportMapper;
import com.everwing.coreservice.wy.dao.mapper.cust.person.PersonCustNewMapper;
import com.everwing.myexcel.definition.ExcelDefinitionReader;
import com.everwing.myexcel.factory.DefinitionFactory;
import com.everwing.myexcel.factory.xml.XMLDefinitionFactory4commonImport;
import com.everwing.myexcel.resolver.poi.POIExcelResolver4commonImport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

@Service("personCustImportServiceImpl")
public class PersonCustImportServiceImpl extends POIExcelResolver4commonImport implements PersonCustImportService{
    static Logger logger = LogManager.getLogger(PersonCustImportServiceImpl.class);

    @Autowired
    private ImportExportMapper importExportMapper;


    @Autowired
    private PersonCustNewMapper personCustNewMapper;

    @Autowired
    private FastDFSApi fastDFSApi;


    private WyBusinessContext ctx;


    /**
     * 导入入口
     * @param ctx
     * @param tSysImportExportRequest
     */
    @Override
    public MessageMap doImport(WyBusinessContext ctx, TSysImportExportSearch tSysImportExportRequest) {
        String batchNo = tSysImportExportRequest.getBatchNo();
        String excelPath = null;
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
            Callable task = new PersonCustImportTask(this,ctx,batchNo,excelPath);
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

//    @Override
//    public MessageMap importPerson(WyBusinessContext ctx, List<PersonCustNewImportList> personCustNewImportLists) {
//        return null;
//    }

    @Override
    public MessageMap importPerson(WyBusinessContext ctx, List<PersonCustNewImportList> personCustNewImportLists) {
        this.ctx = ctx;
        MessageMap mm = new MessageMap();
        Map<String,Object> map = getImportPerson(ctx,personCustNewImportLists);
        List<PersonCustNewImportList> newList =(List<PersonCustNewImportList>)map.get("ImportList");

        int commitSize = 1000;//默认每次提交数量
        // 新增
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(newList)){
            int size = newList.size();
            if(size <= commitSize){
                personCustNewMapper.batchInsert(newList);
            }else{
                if(size % commitSize == 0){
                    int count = size / commitSize;
                    for(int i=0;i<count;i++){
                        int fromIndex = i * commitSize;
                        int toIndex = (i+1) * commitSize;
                        personCustNewMapper.batchInsert(newList.subList(fromIndex,toIndex));
                    }
                }else{
                    int endIndex = 0;
                    int count = size / commitSize;
                    for(int i=0;i<count;i++){
                        int fromIndex = i * commitSize;
                        int toIndex = (i+1) * commitSize;
                        endIndex = toIndex;
                        personCustNewMapper.batchInsert(newList.subList(fromIndex,toIndex));
                    }
                    personCustNewMapper.batchInsert(newList.subList(endIndex,size));
                }
            }
        }

        Integer successCount = (Integer)map.get("successCount");
        Integer failCount = (Integer)map.get("failCount");
        StringBuffer error = (StringBuffer)map.get("error");
        if(failCount>0 && successCount>0){ //部分导入成功
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("个人客户导入部分成功;成功了"+successCount+"条;失败"+failCount+"条;"+error.toString());
        }
        if(failCount==0&&successCount==personCustNewImportLists.size()){//全部导入成功
            mm.setFlag(MessageMap.INFOR_SUCCESS);
            mm.setMessage("全部导入成功");
        }
        if(successCount==0&&failCount==personCustNewImportLists.size()){//全部导入失败
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage("个人客户导入全部失败;失败了"+failCount+"条;"+error.toString());
        }
        return mm;
    }

    private Map<String,Object> getImportPerson(WyBusinessContext ctx, List<PersonCustNewImportList> personCustNewImportLists) {
        this.ctx = ctx;
        Map<String,Object> map =new HashMap<String,Object>();
        List<PersonCustNewImportList> newImportLists = new ArrayList<PersonCustNewImportList>();
        StringBuffer error = new StringBuffer();
        Integer successCount=0;
        Integer failCount =0;
        try {
            //新增的校验
            String num = this.personCustNewMapper.selectCustCode();
            for (int i = 0; i < personCustNewImportLists.size(); i++) {
                PersonCustNewImportList personCustImport = personCustNewImportLists.get(i);
                PersonCustNew personCustNew = new PersonCustNew();
                personCustNew.setCardNum(personCustImport.getCardNum());
                personCustNew.setRegisterPhone(personCustImport.getRegisterPhone());
                //校验计费和收费对象的关系
                if(personCustNewMapper.getCountByNoOrPhoneById(personCustNew) > 0){
                        error.append("个人客户["+personCustNew.getName()+"]身份证或注册号码重复").append("\n");
                        failCount++;
                        continue;
                }
                num = String.valueOf(Integer.parseInt(num) + 1);
                personCustImport.setCustCode('P' + num);
                personCustImport.setCompanyId(ctx.getCompanyId());
                personCustNew.setCardNum(personCustImport.getCardNum());
                personCustNew.setRegisterPhone(personCustImport.getRegisterPhone());
                newImportLists.add(personCustImport);
                successCount++;
            }


            map.put("error", error);
            map.put("ImportList", newImportLists);
            map.put("successCount", successCount);
            map.put("failCount", failCount);
            return map;

        }catch (Exception e) {
            map.put("ImportList", new ArrayList<PersonCustNewImportList>());
            logger.info(CommonUtils.log(e.getMessage()));
            map.put("successCount", 0);
            map.put("failCount", personCustNewImportLists.size());
            map.put("error", error.append(e.getMessage()));
        }


        return map;
    }


    @Override
    protected ExcelDefinitionReader getExcelDefinition() {
        DefinitionFactory definitionReaderFactory = new XMLDefinitionFactory4commonImport("importExport/import/xml/person_cust.xml");
        return definitionReaderFactory.createExcelDefinitionReader();
    }

    @Override
    protected String getLookupItemCodeByName(String lookupCode, String parentCode, String name) {
        WyBusinessContext ctx = WyBusinessContext.getContext();
        return DataDictionaryUtil.getLookupItemCodeByParentCodeAndName(ctx.getCompanyId(),parentCode,name);
    }
}
