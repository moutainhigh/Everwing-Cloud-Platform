package com.everwing.server.wy.web.controller.project;/**
 * Created by wust on 2017/8/4.
 */

import com.esotericsoftware.minlog.Log;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;
import com.everwing.coreservice.common.wy.entity.order.TcOrderChangeAssetComplaint;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.common.wy.entity.property.property.ProprietorInfo;
import com.everwing.coreservice.common.wy.entity.property.property.TPropertyChangingHistory;
import com.everwing.coreservice.common.wy.entity.property.property.TPropertyChangingHistorySearch;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserList;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserSearch;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.api.annex.AnnexApi;
import com.everwing.coreservice.wy.api.building.PropertyApi;
import com.everwing.coreservice.wy.api.building.TcBuildingApi;
import com.everwing.coreservice.wy.api.configuration.tbcassetacount.TBsAssetAccountApi;
import com.everwing.coreservice.wy.api.personbuilding.PersonbuildingApi;
import com.everwing.coreservice.wy.api.sys.TSysUserApi;
import com.everwing.server.wy.util.ServerSysConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * Function:资产管理
 * Reason:
 * Date:2017/8/4
 * @author wusongti@lii.com.cn
 */
@Controller
@RequestMapping("/PropertyController")
public class PropertyController {
    @Autowired
    private PropertyApi propertyApi;

    @Autowired
    private TSysUserApi tSysUserApi;

    @Autowired
    private TBsAssetAccountApi tBsAssetAccountApi;

    @Autowired
    private PersonbuildingApi personbuildingApi;
    
    @Autowired
    private ServerSysConfig sysConfig;

    @Autowired
    private FastDFSApi fastDFSApi;

    @Autowired
    private TcBuildingApi tcBuildingApi;

    /**
     * 以项目维度查询资产
     * @param tcBuildingSearch
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Property,businessName="分页查询资产列表",operationType= OperationEnum.Search)
    @RequestMapping(value = "/listPage",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPage(@RequestBody TcBuildingSearch tcBuildingSearch){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<BaseDto> result = tcBuildingApi.listPage(ctx,tcBuildingSearch);
        if(result.isSuccess()){
            baseDto = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }

    /**
     * 以客户维度查询资产
     * @param tcBuildingSearch
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Property,businessName="分页查询资产列表",operationType= OperationEnum.Search)
    @RequestMapping(value = "/listProperty4customerPage",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listProperty4customerPage(@RequestBody TcBuildingSearch tcBuildingSearch){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();



        RemoteModelResult<BaseDto> remoteModelResult = personbuildingApi.listBudildingByCustId(tcBuildingSearch.getCustomerId());
        if(remoteModelResult.isSuccess()){
            List<TcBuilding> tcBuildings = remoteModelResult.getModel().getLstDto();
            if(CollectionUtils.isNotEmpty(tcBuildings)){
                List<String> buildingCodeList = new ArrayList<>(tcBuildings.size());
                for (TcBuilding tcBuilding : tcBuildings) {
                    buildingCodeList.add(tcBuilding.getBuildingCode());
                }

                tcBuildingSearch.setBuildingCodeList(buildingCodeList);
                RemoteModelResult<BaseDto> result = tcBuildingApi.listPage(ctx,tcBuildingSearch);
                if(result.isSuccess()){
                    baseDto = result.getModel();
                }else{
                    mm.setFlag(MessageMap.INFOR_WARNING);
                    mm.setMessage(result.getMsg());
                }
            }
        }else {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(remoteModelResult.getMsg());
        }


        baseDto.setMessageMap(mm);
        return baseDto;
    }
    
    
    @RequestMapping(value="/getBillByBuildCode/{buildCode}/{projectId}/{projectName}",method =RequestMethod.POST)
    public @ResponseBody BaseDto getBillByBuildCode(HttpServletRequest req,@PathVariable String buildCode,
    		                                    @PathVariable String projectId,@PathVariable String projectName){
    	
    	BaseDto baseDto =new BaseDto();
    	MessageMap msgMap = new MessageMap();
    	WyBusinessContext ctx = WyBusinessContext.getContext();
    	try {
    		ctx.setProjectId(projectId);
    		ctx.setProjectName(projectName);
		RemoteModelResult<BaseDto> result =	 this.propertyApi.getBillByBuildCode(ctx, buildCode);
			if(result.isSuccess()){
				baseDto = result.getModel();
				msgMap= baseDto.getMessageMap();
			}else{
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage(result.getMsg());
			}
		} catch (Exception e) {
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("系统错误!");
			Log.info(CommonUtils.log(e.getMessage()));
		}
    	baseDto.setMessageMap(msgMap);
    	return baseDto;
    }
    


    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Property,businessName="分页查询产权变更记录",operationType= OperationEnum.Search)
    @RequestMapping(value = "/listPageChangingHistory",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPageChangingHistory(HttpServletRequest request, @RequestBody TPropertyChangingHistorySearch tPropertyChangingHistorySearch){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<BaseDto> result = propertyApi.listPageChangingHistory(ctx.getCompanyId(),tPropertyChangingHistorySearch);
        if(result.isSuccess()){
            baseDto = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }


    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Property,businessName="新增产权变更信息",operationType= OperationEnum.Insert)
    @RequestMapping(value="/insertChangingHistory",method =RequestMethod.POST)
    public @ResponseBody  MessageMap insertChangingHistory(HttpServletRequest request,@RequestBody TPropertyChangingHistory entity){
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();
        //这里把文件夹里的所有文件打包成一个zip包上传到FDS文件服务器上
        String companyId = ctx.getCompanyId();
        String projectId = entity.getProjectId();
        String buildCode = entity.getBuildingCode();
        Log.info(CommonUtils.log(companyId+","+projectId+","+buildCode));
        if(StringUtils.isBlank(companyId) || StringUtils.isBlank(projectId) || StringUtils.isBlank(buildCode)){
        	mm.setFlag(MessageMap.INFOR_ERROR);
        	mm.setMessage("公司编号或者项目编号或者建筑编号不能为空!");
        	return mm;
        }
        entity.setId(CommonUtils.getUUID()); //设置
        String photoTempPath = sysConfig.getPhotoPath()+companyId+File.separator+projectId+File.separator+buildCode+File.separator+createNowStringTime();
        File tempFile = new File(photoTempPath);
        String zipPath = sysConfig.getPhotoPath()+companyId+File.separator+projectId+File.separator+buildCode;
        String zipFileName = createNowStringTime();
        File zipFile = new File(zipPath+File.separator+zipFileName+".zip");
        if(zipFile.exists()) zipFile.delete();
        if(tempFile.exists()){
        	File[] tempFiles = tempFile.listFiles();//得到所有文件,将所有文件打包成一个zip包存放到fds服务器，删除文件和zip包
        	if(tempFiles.length>0){//该文件夹内有文件
        		//将该目录下的所有文件打包成zip文件并且上传到FDS
//        		String zipPath = "";
        		boolean flag = fileToZip(photoTempPath,zipPath,zipFileName);
        		if(flag){//打包成功上传FDS，
        			File uploadZipFile = new File(zipPath+File.separator+zipFileName+".zip");
        			try {
        				 RemoteModelResult<UploadFile>  result= this.fastDFSApi.uploadFile(uploadZipFile);
						 if(result.isSuccess()){
							  UploadFile uploadFile = result.getModel();
				                if (uploadFile != null) {
				                	Annex annex = installAnnex(entity.getId(),null,zipFileName,"zip");
				                	annex.setUploadFileId(uploadFile.getUploadFileId());
			                        RemoteModelResult<BaseDto> remoteModelResult = ((AnnexApi) SpringContextHolder.getBean("annexApi")).addAnner(companyId, annex);
			                        if(remoteModelResult.isSuccess()){
			                        	uploadZipFile.getAbsoluteFile().delete(); //删除这个zip包
			                        	//删除临时文件
//			                        	File[] deleteFile = new File(photoTempPath).listFiles();
			                        	for(int i=0;i<tempFiles.length;i++){
			                        		tempFiles[i].delete();
			                        	}
			                        }
				                }
						 }
					} catch (Exception e) {
						Log.info(CommonUtils.log(e.getMessage()));
						mm.setFlag(MessageMap.INFOR_ERROR);
						mm.setMessage("文件上传出错！");
						return mm;
					}
        			
        		}
        	}
        }
        entity.setCreaterId(ctx.getUserId());
        entity.setCreaterName(ctx.getLoginName());
        RemoteModelResult<MessageMap> result = propertyApi.insertChangingHistory(ctx.getCompanyId(),entity);
        if(result.isSuccess()){
            mm = result.getModel() == null ? null : result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }
    
    /**
     * 组装插入附件对象
     */
    private Annex installAnnex(String moduleCode,String path,String fileName,String fileType){
    	Annex annex = new Annex();
    	annex.setAnnexName(fileName);
    	annex.setAnnexType(1); //图片  目前暂时只能上传图片
    	annex.setFileType(fileType);
    	annex.setRelationId(moduleCode);
    	annex.setAnnexAddress(path);
    	annex.setAnnexTime(CommonUtils.getDateStr());
    	annex.setIsMain("1"); //辅图
    	return annex;
    }
    /**
     * 测试ZIP打包
     * @return
     * @throws IOException 
     */
//    @Test
//    public void TestZip() throws IOException{
//    	ZipOutputStream zos = null;
//    	FileInputStream fis = null;
//    	String path ="D:\\photo\\09841dc0-204a-41f2-a175-20a6dcee0187\\1001\\1001_20170727172034178_10000_10008_10000_10000_10001_10002\\2017-11-02";
//    	File file =new File(path);
//    	
//    	String zipPath = "D:\\photo\\09841dc0-204a-41f2-a175-20a6dcee0187\\1001\\1001_20170727172034178_10000_10008_10000_10000_10001_10002";
//    	String zipFileName = createNowStringTime();
//    	File zipFile = new File(zipPath+File.separator+createNowStringTime()+".zip");
//    	if(zipFile.exists()) zipFile.delete();
//    	fileToZip(path,zipPath,"2017-11-02");
//    	
//    }
    
    private  String createNowStringTime(){
    	
    	return new DateTime().toString("yyyy-MM-dd");
    }
    
    
    public  boolean fileToZip(String sourceFilePath,String zipFilePath,String fileName){  
        boolean flag = false;  
        File sourceFile = new File(sourceFilePath);  
        FileInputStream fis = null;  
        BufferedInputStream bis = null;  
        FileOutputStream fos = null;  
        ZipOutputStream zos = null;  
          
        if(sourceFile.exists() == false){  
        	Log.info(CommonUtils.log("待压缩的文件目录："+sourceFilePath+"不存在."));
        }else{  
            try {  
                File zipFile = new File(zipFilePath + "/" + fileName +".zip");  
                if(zipFile.exists()){
                	Log.info(CommonUtils.log(zipFilePath + "目录下存在名字为:" + fileName +".zip" +"打包文件!"));
                }else{  
                    File[] sourceFiles = sourceFile.listFiles();  
                    if(null == sourceFiles || sourceFiles.length<1){
                    	Log.info(CommonUtils.log("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩。"));
                    }else{  
                        fos = new FileOutputStream(zipFile);  
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));  
                        byte[] bufs = new byte[1024*10];  
                        for(int i=0;i<sourceFiles.length;i++){  
                            //创建ZIP实体，并添加进压缩包  
                            ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());  
                            zos.putNextEntry(zipEntry);  
                            //读取待压缩的文件并写进压缩包里  
                            fis = new FileInputStream(sourceFiles[i]);  
                            bis = new BufferedInputStream(fis, 1024*10);  
                            int read = 0;  
                            while((read=bis.read(bufs, 0, 1024*10)) != -1){  
                                zos.write(bufs,0,read);  
                            }  
                        }  
                        flag = true;  
                    }  
                }  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
                throw new RuntimeException(e);  
            } catch (IOException e) {  
                e.printStackTrace();  
                throw new RuntimeException(e);  
            } finally{  
                //关闭流  
                try {  
                    if(null != bis) bis.close();  
                    if(null != zos) zos.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                    throw new RuntimeException(e);  
                }  
            }  
        }  
        return flag;  
    }  


    @RequestMapping(value = "/getProprietorInfoByBuildingCode/{buildingCode}",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap getProprietorInfoByBuildingCode(HttpServletRequest request, @PathVariable String buildingCode){
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<List<ProprietorInfo>> result = propertyApi.getProprietorInfoByBuildingCode(ctx.getCompanyId(),buildingCode);
        if(result.isSuccess()){
            StringBuffer oldHolderStr = new StringBuffer();
            StringBuffer oldHolderStrName = new StringBuffer();

            List<ProprietorInfo> list = result.getModel();
            if(CollectionUtils.isNotEmpty(list)){
                for(;;){
                    if(CollectionUtils.isNotEmpty(list)){
                        ProprietorInfo proprietorInfo = list.subList(0,1).get(0);
                        list.subList(0,1).clear();
                        oldHolderStr.append(proprietorInfo.getOldHolder()).append(CollectionUtils.isEmpty(list)?"":",");
                        oldHolderStrName.append(proprietorInfo.getOldHolderName()).append(CollectionUtils.isEmpty(list)?"":",");
                        continue;
                    }
                    break;
                }
            }
            mm.setObj(oldHolderStr + ";" + oldHolderStrName);
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }
    
    /**
     * 检查该资产账号余额情况，欠费不能做产权变更
     * @param req
     * @return
     */
    @RequestMapping(value="/queryTbsAssetAccount/{buildingCode}/{buildName}/{projectId}/{projectName}",method=RequestMethod.POST)
    public @ResponseBody BaseDto queryTbsAssetAccount(HttpServletRequest req,@PathVariable String buildingCode,@PathVariable String buildName,@PathVariable String projectId,@PathVariable String projectName){
    	BaseDto baseDto = new BaseDto();
    	MessageMap msgMap = new MessageMap();
    	WyBusinessContext ctx = WyBusinessContext.getContext();
    	ctx.setProjectId(projectId);
    	ctx.setProjectName(projectName);
    	try {
    		RemoteModelResult<BaseDto> rslt = this.propertyApi.checkCollingDatas(ctx,buildingCode,projectId);
    		if(rslt.isSuccess() && null != rslt.getModel()){
    			String fullNames = CommonUtils.null2String(rslt.getModel().getObj());
    			if(CommonUtils.isNotEmpty(fullNames)){
    				msgMap.setFlag(MessageMap.INFOR_ERROR);
    				msgMap.setMessage(fullNames + ": 处于  [托收待回盘] 状态 , 为防止对老业主产生不必要的扣费, 当前无法办理过户操作. 请等待回盘后再进行此操作. ");
    				baseDto.setMessageMap(msgMap);
    				return baseDto;
    			}
    		}
    		
    		
    		RemoteModelResult<BaseDto> result = tBsAssetAccountApi.queryAccountSituationByBuildCode(ctx, buildingCode,buildName);
    		if(result.isSuccess()){
    			baseDto = result.getModel();
    			msgMap = baseDto.getMessageMap();
    		}else{
    			msgMap.setFlag(MessageMap.INFOR_ERROR);
    			msgMap.setMessage(result.getMsg());
    		}
		} catch (Exception e) {
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("系统错误!");
		}
    	baseDto.setMessageMap(msgMap);
    	return baseDto;
    }




    /**
     * 申请抄表
     * @param request
     * @param type
     * @param tcReadingTask
     * @return
     */
    @RequestMapping(value="/requestReadingMeter/{projectId}/{type}",method =RequestMethod.POST)
    public @ResponseBody  MessageMap requestReadingMeter(HttpServletRequest request,@PathVariable String projectId,@PathVariable int type,@RequestBody TcReadingTask tcReadingTask){
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();


        TSysUserSearch tSysUserSearch = new TSysUserSearch();
        tSysUserSearch.setLoginName(tcReadingTask.getReadingPersonId());
        RemoteModelResult<List<TSysUserList>> userListRemoteModelResult = tSysUserApi.findByCondition(ctx,tSysUserSearch);
        if(userListRemoteModelResult.isSuccess()){
            List<TSysUserList> tSysUserLists = userListRemoteModelResult.getModel();
            if(CollectionUtils.isEmpty(tSysUserLists)){
                mm.setFlag(MessageMap.INFOR_WARNING);
                mm.setMessage("没有该抄表员，请重新输入");
            }else {
                tcReadingTask.setReadingPersonId(tSysUserLists.get(0).getUserId());
                RemoteModelResult<MessageMap> result = propertyApi.requestReadingMeter(ctx.getCompanyId(),projectId,type,tcReadingTask);
                if(result.isSuccess()){
                    mm = result.getModel() == null ? mm : result.getModel();
                }else{
                    mm.setFlag(MessageMap.INFOR_WARNING);
                    mm.setMessage(StringUtils.isBlank(result.getMsg())?"申请失败":result.getMsg());
                }
            }
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(StringUtils.isBlank(userListRemoteModelResult.getMsg())?"查找抄表员失败":userListRemoteModelResult.getMsg());
        }
        return mm;
    }
    
    /**
     * 抄表申请重写，
     * 现在的逻辑，是直接创建工单，不和抄表任务关联
     */
    @RequestMapping(value="/requestReadingMeterNew",method=RequestMethod.POST)
    public @ResponseBody BaseDto requestReadingMeterNew(HttpServletRequest req,@RequestBody TcOrderChangeAssetComplaint tcOrderChangeAssetComplaint){
    	BaseDto baseDto = new BaseDto();
    	MessageMap msgMap = new MessageMap();
    	try {
    		  WyBusinessContext ctx = WyBusinessContext.getContext();
    		  if(CommonUtils.isEmpty(tcOrderChangeAssetComplaint) || StringUtils.isBlank(tcOrderChangeAssetComplaint.getReadingPersonId())){
    			  msgMap.setFlag(MessageMap.INFOR_ERROR);
    			  msgMap.setMessage("传入的参数为空或者负责人为空!");
    			  baseDto.setMessageMap(msgMap);
    			  return baseDto;
    		  }
        	RemoteModelResult<BaseDto> result =	this.propertyApi.requestReadingMeterNew(ctx, tcOrderChangeAssetComplaint);
            if(result.isSuccess()){
            	baseDto = result.getModel();
            	msgMap=baseDto.getMessageMap();
            }else{
            	msgMap.setFlag(MessageMap.INFOR_ERROR);
            	msgMap.setMessage(result.getMsg());
            } 
    	           
		} catch (Exception e) {
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("系统异常!");
			Log.info(CommonUtils.log(e.getMessage()));
		}
    	baseDto.setMessageMap(msgMap);
    	return baseDto;
    }
    
    
    /**
     * 手动计费
     */
    @RequestMapping(value="/manualBill/{buildCode}/{buildName}/{projectId}/{projectName}",method=RequestMethod.POST)
    public  @ResponseBody BaseDto manualBill(HttpServletRequest req,@PathVariable String buildCode,
    		                              @PathVariable String buildName,@PathVariable String projectId,@PathVariable String projectName){
    	BaseDto baseDto = new BaseDto();
    	MessageMap msgMap = new MessageMap();
    	try {
    		WyBusinessContext ctx = WyBusinessContext.getContext();
    		ctx.setProjectId(projectId);
    		ctx.setProjectName(projectName);
			RemoteModelResult<BaseDto> result = this.propertyApi.manualBill(ctx, buildCode, buildName);
			if(result.isSuccess()){
				baseDto = result.getModel();
				msgMap = baseDto.getMessageMap();
			}else{
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage(result.getMsg());
			}
		} catch (Exception e) {
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("系统错误!");
		}
    	baseDto.setMessageMap(msgMap);
    	return baseDto;
    }
    
}
