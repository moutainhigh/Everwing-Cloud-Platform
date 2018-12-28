package com.everwing.coreservice.platform.dao.mapper.extra;

import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.platform.dao.mapper.extra.provider.FastDFSProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FastDFSExtraMapper {

    String COLUMNS="upload_file_id,upload_time,file_name,size,path,suffix,md5";

    String COLUMNS_TO_BEAN="upload_file_id uploadFileId,upload_time uploadTime,file_name fileName,size,path,suffix,account_id accountId,md5";

    @Insert("insert into upload_file("+COLUMNS+")values(#{uploadFileId},#{uploadTime},#{fileName},#{size},#{path},#{suffix},#{md5})")
    int insert(UploadFile uploadFile);

    @Select("select "+COLUMNS_TO_BEAN+" from upload_file where upload_file_id=#{fileId}")
    UploadFile getById(String fileId);

    @InsertProvider(type = FastDFSProvider.class,method = "batchInsert")
    int batchInsert(@Param("uploadFileList") List<UploadFile> uploadFileList);

    @SelectProvider(type = FastDFSProvider.class,method = "getFilesByIds")
    List<UploadFile> getFilesByIds(@Param("fileIds") String[] fileIds);

    @Delete("delete from upload_file where upload_file_id=#{fileId}")
    int deleteById(String fileId);
}
