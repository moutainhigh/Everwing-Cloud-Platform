package com.everwing.coreservice.platform.dao.mapper.extra.provider;

import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.platform.dao.mapper.extra.FastDFSExtraMapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class FastDFSProvider {

    public String batchInsert(Map<String,Object> param){
        List<UploadFile> uploadFileList= (List<UploadFile>) param.get("uploadFileList");
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("insert into upload_file("+ FastDFSExtraMapper.COLUMNS+")values");
        if(!uploadFileList.isEmpty()) {
            for (UploadFile uploadFile:uploadFileList) {
                 stringBuilder.append ("('"+ uploadFile.getUploadFileId()+"','"+new Timestamp(uploadFile.getUploadTime().getTime())+"','"+uploadFile.getFileName()+"',"+uploadFile.getSize()+",'"+uploadFile.getPath()+"','"+uploadFile.getSuffix()+"','"+uploadFile.getMd5()+"'),");
            }
        }
        return stringBuilder.subSequence(0,stringBuilder.length()-1).toString();
    }

    public String getFilesByIds(Map<String,Object> param){
        String[] fileIds= (String[]) param.get("fileIds");
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("select ");
        stringBuilder.append(FastDFSExtraMapper.COLUMNS_TO_BEAN);
        stringBuilder.append(" from upload_file ");
        stringBuilder.append(" where upload_file_id in (");
        for (String fileId:fileIds) {
            stringBuilder.append(" '");
            stringBuilder.append(fileId);
            stringBuilder.append("',");
        }
        stringBuilder=new StringBuilder(stringBuilder.subSequence(0,stringBuilder.length()-1));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
