package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

/**
 * Created by DELL on 2018/6/4.
 */
@Repository
public interface UploadFileMapper {

    String COLUMNS="upload_file_id,upload_time,file_name,size,path,suffix,md5";

    @Insert("insert into upload_file("+COLUMNS+")values(#{uploadFileId},#{uploadTime},#{fileName},#{size},#{path},#{suffix},#{md5})")
    void insert(UploadFile uploadFile);
}
