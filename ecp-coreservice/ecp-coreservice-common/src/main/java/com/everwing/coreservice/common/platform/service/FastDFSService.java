package com.everwing.coreservice.common.platform.service;

import com.everwing.coreservice.common.platform.entity.generated.UploadFile;

import java.util.List;

public interface FastDFSService {
    /**
     * 上传springmvc提交的文件到文件服务器
     * @param uploadFile
     * @return 存库是否成功
     */
    boolean uploadFile(UploadFile uploadFile) throws Exception;

    /**
     * 根据id获取文件信息
     * @param fileId
     * @return
     */
    UploadFile queryFileById(String fileId);

    /**
     * 批量上传文件
     * @param uploadFileList 文件list
     * @return 上传是否成功
     */
    boolean batchInsertFile(List<UploadFile> uploadFileList);

    /**
     * 通过多个id获取多个文件信息
     * @param fileIds 文件id数组
     * @return 文件信息list
     */
    List<UploadFile> queryBatchFile(String... fileIds);

    /**
     * 通过fileId删除文件信息
     * @param fileId fileId
     * @return 是否删除成功
     */
    boolean deleteByFileId(String fileId);
}
