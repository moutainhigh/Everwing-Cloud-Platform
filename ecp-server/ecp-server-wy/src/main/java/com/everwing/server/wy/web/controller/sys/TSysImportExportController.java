package com.everwing.server.wy.web.controller.sys;/**
 * Created by wust on 2017/5/12.
 */

import com.esotericsoftware.minlog.Log;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import com.everwing.coreservice.common.utils.generator.WyCodeGenerator;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.ImportExportEnum;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.system.importExport.Excel;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExport;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.api.annex.AnnexApi;
import com.everwing.coreservice.wy.api.sys.TSysImportExportApi;
import com.everwing.server.wy.util.ImportExportHelper;
import com.everwing.server.wy.util.ServerSysConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/5/12
 * @author wusongti@lii.com.cn
 */
@Controller
@RequestMapping("/system/ImportExportController")
public class TSysImportExportController {
    static Logger logger = LogManager.getLogger(TSysImportExportController.class);

    @Autowired
    private SpringRedisTools springRedisTools;

    @Autowired
    private TSysImportExportApi tSysImportExportApi;
    
    @Autowired
    private AnnexApi annexApi;

    @Autowired
    protected FastDFSApi fastDFSApi;

    @Autowired
	private ServerSysConfig sysConfig;


    // 格式为uploadFileKey_${loginName}
    public static final String UPLOAD_FILE_KEY = "uploadFileKey_%s";
    /**
     * 导入导出结果列表查询
     * @param request
     * @param tSysImportExportSearch
     * @return
     */
    @WyOperationLogAnnotation(moduleName=OperationEnum.Module_ImportExport,businessName="查询导入导出列表",operationType= OperationEnum.Search)
    @RequestMapping(value = "/listPage",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPage(HttpServletRequest request, @RequestBody TSysImportExportSearch tSysImportExportSearch){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        if(!ctx.isSuAdmin()){
            // 非超级管理员只能看自己导入导出的数据
            tSysImportExportSearch.setOperationUserId(ctx.getUserId());
        }
        RemoteModelResult<BaseDto> result = tSysImportExportApi.listPage(ctx,tSysImportExportSearch);
        if(result.isSuccess()){
            mm.setFlag(MessageMap.INFOR_SUCCESS);
            baseDto = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
            logger.error(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }


    /**
     * 已经改用文件服务器做，该方法作废，参考CommonController.downloadFastFile：在导入导出结果列表点击下载文件
     * @param request
     * @param response
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/download",method=RequestMethod.GET)
    public @ResponseBody
    MessageMap download (HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        MessageMap mm = new MessageMap();

        // 全名称，如：d://import/xxx.xls
        String fileNameStr = request.getParameter("fileName") == null ? "": CommonUtils.null2String(request.getParameter("fileName"));

        if (StringUtils.isBlank(fileNameStr)) {

            mm.setFlag(MessageMap.INFOR_WARNING);

            mm.setMessage("文件名不能少噢。");

            return mm;
        }


        String fileName = fileNameStr.substring(fileNameStr.lastIndexOf("/")+1);

        try {
            response.setHeader("Content-Disposition","attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
        } catch (UnsupportedEncodingException e) {

        }
        OutputStream outputStream = null;
        InputStream in = null;
        try {
            outputStream = response.getOutputStream();

            in = new FileInputStream(new File(fileNameStr));

            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }

        } catch (IOException e) {
            logger.error(e);
        }finally {
            try {
                if(in != null){
                    in.close();
                }

                if(outputStream != null){
                    outputStream.close();
                }
            } catch (IOException e) {
                logger.error(e);
            }
        }
        return mm;
    }
    
    /**
     * 查询附件
     */
    @RequestMapping(value="/queryEnclosureFile",method=RequestMethod.POST)
    public @ResponseBody BaseDto queryEnclosureFile(HttpServletRequest req,@RequestBody Annex annex){
    	return BaseDtoUtils.getDto(this.annexApi.queryByRelationId(CommonUtils.getCompanyId(req), annex));
    }
    
    /**
     * 删除附件
     */
    @RequestMapping(value="/deleteEnclosureFile",method=RequestMethod.POST)
    public @ResponseBody BaseDto deleteEnclosureFile(HttpServletRequest req,@RequestBody Annex annex){
    	return  BaseDtoUtils.getDto(this.annexApi.deleteEnclosureFile(CommonUtils.getCompanyId(req), annex));
    }
    
    
    /**
     * 导入前需要上传的文件   此方法用于之前上传图片附件允许多个，后来对接fastdfs重写的方法
     *       附件上传后一样传给fastDfs处理
     *       然后也一样需要写入给
     * @param request
     * @param file
     * @return
     */
    @SuppressWarnings("unused")
	@RequestMapping(value = "/uploadEnclosureFile",method=RequestMethod.POST)
    public @ResponseBody
    MessageMap uploadEnclosureFile (HttpServletRequest request, MultipartFile file) {
    	
    	CommonsMultipartFile fileItem=null;
    	if(null != file) {
    		fileItem=(CommonsMultipartFile)file;
    	}else {
    		return new MessageMap(MessageMap.INFOR_ERROR, "附件信息为空"); 
    	}
    	
    	MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        //  模块名称，必填，只能用英文名
        String moduleDescription = CommonUtils.null2String(request.getParameter("moduleDescription"));
        if(StringUtils.isBlank(moduleDescription)){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("上传文件失败，模块描述必须填。");
            return mm;
        }else if(!moduleDescription.matches("[A-Za-z0-9_]+")){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("上传文件失败，模块描述只能是字母、数字、下划线或三者的组合。");
            return mm;
        }
        //编号，由各个模块的ID组成
        String moduleCode = CommonUtils.null2String(request.getParameter("moduleCode"));
        if(StringUtils.isBlank(moduleCode)){
        	 mm.setFlag(MessageMap.INFOR_WARNING);
             mm.setMessage("上传文件失败，模块ID必须填。");
             return mm;
        }
    	//公司编码
    	String companyId = CommonUtils.null2String(request.getParameter("companyId"));
    	
    	String fileName =fileItem.getFileItem().getName().substring(0,fileItem.getFileItem().getName().lastIndexOf("."));
		String fileType =fileItem.getFileItem().getName().substring(fileItem.getFileItem().getName().lastIndexOf(".") + 1);
		//查询附件是否已经存在
		mm= ImportExportHelper.existAnnex(companyId, moduleCode, fileName,fileType);
    	
		if(mm.getFlag().equals(MessageMap.INFOR_ERROR)){
			return new MessageMap(MessageMap.INFOR_ERROR, "附件信息已存在，请重新上传");
		}else{
			//附件可以上传
			try {
				//将文件传给fastDFS处理
				RemoteModelResult<UploadFile> remoteModelResult = fastDFSApi.uploadFile(file);
				if(remoteModelResult.isSuccess()) {
	                UploadFile uploadFile = remoteModelResult.getModel();
	                if (uploadFile != null) {
	                	
	                	Annex annex = installAnnex(moduleCode,null,fileName,fileType);
	                	annex.setUploadFileId(uploadFile.getUploadFileId());
                        RemoteModelResult<BaseDto> result = ((AnnexApi) SpringContextHolder.getBean("annexApi")).addAnner(companyId, annex);
                        if(result.getModel().getMessageMap().getFlag().equals(MessageMap.INFOR_SUCCESS)){
                        	mm.setFlag(MessageMap.INFOR_SUCCESS);
                        	mm.setMessage("上传成功");
                        }else{
                        	mm.setFlag(MessageMap.INFOR_ERROR);
                        	mm.setMessage("上传失败");
                        }
	                }
		          }
			} catch (Exception e) {
				logger.error(e);
	            mm.setFlag(MessageMap.INFOR_ERROR);
	            mm.setMessage("上传失败：" + e.getMessage());
			}
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
     * 精拍仪图片上传
     * @throws IOException 
     */
    @RequestMapping(value = "/uploadPhoto",method=RequestMethod.POST)
    public @ResponseBody MessageMap uploadPhoto(HttpServletRequest request,MultipartFile file) throws IOException{
    	
    	CommonsMultipartFile fileItem=null;
    	if(null != file) {
    		fileItem=(CommonsMultipartFile)file;
    	}else {
    		return new MessageMap(MessageMap.INFOR_ERROR, "附件信息为空"); 
    	}
    	String companyId = CommonUtils.null2String(request.getParameter("companyId"));
    	String projectId = CommonUtils.null2String(request.getParameter("projectId"));
    	String buildCode=CommonUtils.null2String(request.getParameter("buildCode"));
    	if(StringUtils.isBlank(companyId) || StringUtils.isBlank(projectId) || StringUtils.isBlank(buildCode)){
    		return new MessageMap(MessageMap.INFOR_ERROR,"公司编号或者项目编号或者建筑编号不能为空!");
    	}
    	MessageMap msgMap = new MessageMap();
    	String photoTempRootPath = sysConfig.getPhotoPath();
    	String photoTempPath=photoTempRootPath+companyId+File.separator+projectId+File.separator+buildCode+File.separator+createNowStringTime();
    	//判断目录是否存在，不存在则创建
    	 File dirTempFile = new File(photoTempPath);
    	 if (!dirTempFile.exists()) {
             dirTempFile.mkdirs();
         }
    	 //判断文件是否存在，若存在，则删除
    	 
    	OutputStream os = null;
        InputStream is = null;
        
        try {
        	String fileName =fileItem.getOriginalFilename();
        	//判断文件是否存在，若存在，则删除
        	File isExistFile = new File(photoTempPath+File.separator+fileName);
        	if(isExistFile.exists()){
        		isExistFile.delete();
        	}
    		File uploadedFile = new File(photoTempPath,fileName);
    		os = new FileOutputStream(uploadedFile);
    		is = fileItem.getInputStream();

             byte buf[] = new byte[10240];//可以修改 1024 以提高读取速度

             int length = 0;

             while( (length = is.read(buf)) > 0 ){
                 os.write(buf, 0, length);
             }
             os.flush();
        	
		} catch (FileNotFoundException e) {
			Log.info(CommonUtils.log(e.getMessage()));
		}catch(IOException e1){
			Log.info(CommonUtils.log(e1.getMessage()));
		}catch(Exception e2){
			Log.info(CommonUtils.log(e2.getMessage()));
		}finally{
			if(os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error(e);
                }
            }
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error(e);
                }
            }
		}
    	return msgMap;
    }
    
    
    /**
     * 得到当前日期
     */
    
    private  String createNowStringTime(){
    	
    	return new DateTime().toString("yyyy-MM-dd");
    }
    

    /**
     * 导入前需要上传的文件
     * @param request
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadFile",method=RequestMethod.POST)
    public @ResponseBody
    MessageMap uploadFile (HttpServletRequest request, @RequestParam(value = "file" , required = true) MultipartFile file) {
        MessageMap mm = new MessageMap();

        //  模块名称，必填，只能用英文名
        String moduleDescription = CommonUtils.null2String(request.getParameter("moduleDescription"));
        if(StringUtils.isBlank(moduleDescription)){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("上传文件失败，模块描述必须填。");
            return mm;
        }else if(!moduleDescription.matches("[A-Za-z0-9_]+")){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("上传文件失败，模块描述只能是字母、数字、下划线或三者的组合。");
            return mm;
        }

        WyBusinessContext ctx = WyBusinessContext.getContext();

        String batchNo = WyCodeGenerator.genImportExportCode(ImportExportEnum.Import.name().toUpperCase());


        // 检测是否有导入任务在执行
        mm = ImportExportHelper.checkDoingTask(ctx.getCompanyId(),moduleDescription);
        if(!MessageMap.INFOR_SUCCESS.equals(mm.getFlag())){
            return mm;
        }



        try{
            RemoteModelResult<UploadFile> remoteModelResult = fastDFSApi.uploadFile(file);
            if(remoteModelResult.isSuccess()) {
                UploadFile uploadFile = remoteModelResult.getModel();
                if (uploadFile != null) {
                    TSysImportExport tSysImportExport = new TSysImportExport();
                    tSysImportExport.setUploadFileId(uploadFile.getUploadFileId());
                    tSysImportExport.setBatchNo(batchNo);
                    tSysImportExport.setFileName(file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf(".")));
                    tSysImportExport.setModuleDescription(moduleDescription);
                    tSysImportExport.setFileSize((file.getSize() / 1024) + "KB");
                    tSysImportExport.setFileType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
                    tSysImportExport.setStatus(ImportExportEnum.doing.name());
                    tSysImportExport.setCreaterId(ctx.getUserId());
                    tSysImportExport.setCreaterName(ctx.getStaffName());
                    tSysImportExport.setOperationType(ImportExportEnum.Import.name());
                    List<TSysImportExport> list = new ArrayList<>(1);
                    list.add(tSysImportExport);

                    RemoteModelResult<MessageMap> messageMapRemoteModelResult = tSysImportExportApi.batchInsert(ctx.getCompanyId(),list);
                    if(messageMapRemoteModelResult.isSuccess()){
                        springRedisTools.deleteByKey(String.format(UPLOAD_FILE_KEY,ctx.getLoginName()));
                        springRedisTools.addData(String.format(UPLOAD_FILE_KEY,ctx.getLoginName()), batchNo);
                    }else{
                        mm.setFlag(MessageMap.INFOR_ERROR);
                        mm.setMessage("上传失败：" + messageMapRemoteModelResult.getMsg());
                    }
                }
            }
        }catch (Exception e){
            logger.error(e);
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage("上传失败：" + e.getMessage());
        }
        return mm;
    }


    /**
     * 导出入口，将数据库记录写入工作薄，并存储excel文件到文件服务器
     * 等待下载
     * @param request
     * @param excel
     * @return
     */
    @WyOperationLogAnnotation(moduleName=OperationEnum.Module_Common,businessName="导出Excel",operationType= OperationEnum.Export)
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public @ResponseBody
    MessageMap exportMain(HttpServletRequest request, @RequestBody Excel excel) {

        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        if (excel == null) {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("导出参数[XML Name, Excel Version, Module Description]不能为空。");
            return mm;
        } else if (StringUtils.isBlank(CommonUtils.null2String(excel.getXmlName()))) {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("XML Name参数不能少噢。");
            return mm;
        } else if (StringUtils.isBlank(CommonUtils.null2String(excel.getExcelVersion()))) {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("Excel Version参数不能少噢。");
            return mm;
        } else if (!excel.getModuleDescription().matches("[A-Za-z0-9_]+")) {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("上传文件失败，模块描述只能是字母、数字、下划线或三者的组合。");
            return mm;
        }

        String batchNo = WyCodeGenerator.genImportExportCode(ImportExportEnum.Export.name().toUpperCase());
        TSysImportExport tSysImportExport = new TSysImportExport();
        tSysImportExport.setBatchNo(batchNo);
        tSysImportExport.setFileName(batchNo + "." + excel.getExcelVersion());
        tSysImportExport.setModuleDescription(excel.getModuleDescription());
        tSysImportExport.setFileSize("");
        tSysImportExport.setFileType(excel.getExcelVersion());
        //tSysImportExport.setFileDir(dirConfig);
        tSysImportExport.setCreaterId(ctx.getUserId());
        tSysImportExport.setCreaterName(ctx.getStaffName());
        tSysImportExport.setStartTime(new Date());
        tSysImportExport.setOperationType(ImportExportEnum.Export.name());
        tSysImportExport.setStatus(ImportExportEnum.doing.name());
        List<TSysImportExport> listInsert = new ArrayList<>(1);
        listInsert.add(tSysImportExport);
        tSysImportExportApi.batchInsert(ctx.getCompanyId(),listInsert);

        excel.setBatchNo(batchNo);

        RemoteModelResult<MessageMap> messageMapRemoteModelResult = tSysImportExportApi.exportExcel(ctx, excel);
        if (messageMapRemoteModelResult.isSuccess()) {
            mm = messageMapRemoteModelResult.getModel();
        } else {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(messageMapRemoteModelResult.getMsg());
        }
        return mm;
    }
}
