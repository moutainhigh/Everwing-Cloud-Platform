package com.everwing.coreservice.platform.api;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.NoExceptionProxy;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.platform.api.util.ServiceResources;
import com.everwing.utils.FastDFS.FastDFSUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.everwing.coreservice.common.utils.CommonUtils.throwECPException;

@Service
public class FastDFSApi extends ServiceResources{

    @Value("${imgBaseUri}")
    private String imgBaseUri;

    @NoExceptionProxy
    public String getImgBaseUri() {
        return imgBaseUri;
    }

    /**
     * 批量下载时把文件服务器地址设置进去
     * @param fileIds
     * @return
     */
    public RemoteModelResult<List<UploadFile>> loadFilesByIds(String... fileIds){
        if(fileIds!=null&&fileIds.length>0){
        	List<UploadFile> list = new ArrayList<UploadFile>();
            List<UploadFile> uploadFileList=fastDFSService.queryBatchFile(fileIds);
            if(uploadFileList.size()>0){
            	for(UploadFile upFile:uploadFileList){
            		upFile.setPath(imgBaseUri+upFile.getPath());
            		list.add(upFile);
            	}
            }
            return new RemoteModelResult<>(list);
        }
        return new RemoteModelResult<>(null);
    }


    public RemoteModelResult<List<UploadFile>> batchUploadFile(List<File> files){
        List<UploadFile> uploadFileList=new ArrayList<>(files.size());
        try {
            for (File file:files){
                InputStream inputStream=new FileInputStream(file);
                String fileName=file.getName();
                long fileLength=inputStream.available();
                String path= FastDFSUtils.uploadFile(inputStream,fileName,fileLength);
                if(StringUtils.isNotEmpty(path)) {
                    UploadFile uploadFile = new UploadFile();
                    uploadFile.setUploadFileId(UUID.randomUUID().toString());
                    uploadFile.setFileName(fileName);
                    uploadFile.setMd5(DigestUtils.md5Hex(inputStream));
                    uploadFile.setPath(path);
                    uploadFile.setSize(fileLength);
                    uploadFile.setSuffix(FilenameUtils.getExtension(fileName));
                    uploadFile.setUploadTime(new Date());
                    uploadFileList.add(uploadFile);
                }
            }
        } catch (Exception e) {
            throwECPException(ReturnCode.SYSTEM_ERROR);
        }
        boolean flag=fastDFSService.batchInsertFile(uploadFileList);
        if(flag){
            return new RemoteModelResult<>(uploadFileList);
        }
        return new RemoteModelResult<>(null);
    }

    public RemoteModelResult<UploadFile> uploadFile(File file){
        try {
            InputStream inputStream=new FileInputStream(file);
            String fileName=file.getName();
            long fileLength=inputStream.available();
            String path=FastDFSUtils.uploadFile(inputStream,fileName,fileLength);
            if(StringUtils.isNotEmpty(path)){
                UploadFile uploadFile=new UploadFile();
                uploadFile.setUploadFileId(UUID.randomUUID().toString());
                uploadFile.setFileName(fileName);
                uploadFile.setMd5(DigestUtils.md5Hex(inputStream));
                uploadFile.setPath(path);
                uploadFile.setSize(fileLength);
                uploadFile.setSuffix(FilenameUtils.getExtension(fileName));
                uploadFile.setUploadTime(new Date());
                // 获取请求人的信息 暂时用-1测试
                //uploadFile.setAccountId("-1");
                boolean flag=fastDFSService.uploadFile(uploadFile);
                if(flag)
                    return new RemoteModelResult<>(uploadFile);
            }
        } catch (Exception e) {
            throwECPException(ReturnCode.SYSTEM_ERROR);
        }
        return new RemoteModelResult<>(null);
    }

    public RemoteModelResult<List<UploadFile>> batchUploadFile(MultipartFile... multipartFiles)throws Exception{
        List<UploadFile> uploadFileList=new ArrayList<>(multipartFiles.length);
        for (MultipartFile multipartFile:multipartFiles){
            InputStream inputStream=multipartFile.getInputStream();
            String fileName=multipartFile.getOriginalFilename();
            long fileLength=inputStream.available();
            String path= FastDFSUtils.uploadFile(inputStream,fileName,fileLength);
            if(StringUtils.isNotEmpty(path)) {
                UploadFile uploadFile = new UploadFile();
                uploadFile.setUploadFileId(UUID.randomUUID().toString());
                uploadFile.setFileName(multipartFile.getOriginalFilename());
                uploadFile.setMd5(DigestUtils.md5Hex(inputStream));
                uploadFile.setPath(path);
                uploadFile.setSize(fileLength);
                uploadFile.setSuffix(FilenameUtils.getExtension(fileName));
                uploadFile.setUploadTime(new Date());
                uploadFileList.add(uploadFile);
            }
        }
        boolean flag=fastDFSService.batchInsertFile(uploadFileList);
        if(flag){
            return new RemoteModelResult<>(uploadFileList);
        }
        return new RemoteModelResult<>(null);
    }

    public RemoteModelResult<UploadFile> uploadFile(MultipartFile multipartFile){
        try {
            InputStream inputStream=multipartFile.getInputStream();
            String fileName=multipartFile.getOriginalFilename();
            long fileLength=inputStream.available();
            String path=FastDFSUtils.uploadFile(inputStream,fileName,fileLength);
            if(StringUtils.isNotEmpty(path)){
                UploadFile uploadFile=new UploadFile();
                uploadFile.setUploadFileId(UUID.randomUUID().toString());
                uploadFile.setFileName(multipartFile.getOriginalFilename());
                uploadFile.setPath(path);
                uploadFile.setMd5(DigestUtils.md5Hex(inputStream));
                uploadFile.setSize(fileLength);
                uploadFile.setSuffix(FilenameUtils.getExtension(fileName));
                uploadFile.setUploadTime(new Date());
                // 获取请求人的信息 暂时用-1测试
                //uploadFile.setAccountId("-1");
                boolean flag=fastDFSService.uploadFile(uploadFile);
                if(flag)
                    inputStream.close();
                    return new RemoteModelResult<>(uploadFile);
            }
            inputStream.close();
        } catch (Exception e) {
            throwECPException(ReturnCode.SYSTEM_ERROR);
        }
        return new RemoteModelResult<>(null);
    }

    public RemoteModelResult<UploadFile> uploadFile(InputStream inputStream,String fileName){
        try {
            long fileLength=inputStream.available();
            String path=FastDFSUtils.uploadFile(inputStream,fileName,fileLength);
            if(StringUtils.isNotEmpty(path)){
                UploadFile uploadFile=new UploadFile();
                uploadFile.setUploadFileId(UUID.randomUUID().toString());
                uploadFile.setFileName(fileName);
                uploadFile.setPath(path);
                uploadFile.setMd5(DigestUtils.md5Hex(inputStream));
                uploadFile.setSize(fileLength);
                uploadFile.setSuffix(FilenameUtils.getExtension(fileName));
                uploadFile.setUploadTime(new Date());
                // 获取请求人的信息 暂时用-1测试
                //uploadFile.setAccountId("-1");
                boolean flag=fastDFSService.uploadFile(uploadFile);
                if(flag)
                    return new RemoteModelResult<>(uploadFile);
            }
            inputStream.close();
        } catch (Exception e) {
            throwECPException(ReturnCode.SYSTEM_ERROR);
        }
        return new RemoteModelResult<>(null);
    }

    public RemoteModelResult<UploadFile> loadFilePathById(String fileId) {
        UploadFile uploadFile=fastDFSService.queryFileById(fileId);
        if(uploadFile != null){
            uploadFile.setPath(imgBaseUri+uploadFile.getPath());
        }
        return new RemoteModelResult<>(uploadFile);
    }

    public RemoteModelResult deleteFile(String fileId){
        /**
         * 是否存在和删除是否成功不做处理
         */
        UploadFile uploadFile=fastDFSService.queryFileById(fileId);
        if(uploadFile!=null) {
            boolean fdfsDel = FastDFSUtils.deleteFile(uploadFile.getPath());
            if (fdfsDel) {
                fastDFSService.deleteByFileId(fileId);
            }
        }
        return new RemoteModelResult(new BaseDto<>(new MessageMap(MessageMap.INFOR_SUCCESS,"删除成功!")));
    }

    public RemoteModelResult<byte[]> downloadFile(String path){
        return new RemoteModelResult<>(FastDFSUtils.downloadFile(path));
    }
}
