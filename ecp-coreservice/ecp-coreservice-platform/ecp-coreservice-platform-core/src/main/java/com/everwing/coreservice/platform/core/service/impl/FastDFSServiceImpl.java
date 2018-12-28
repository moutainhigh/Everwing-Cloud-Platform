package com.everwing.coreservice.platform.core.service.impl;

import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.platform.service.FastDFSService;
import com.everwing.coreservice.platform.core.util.Resources;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FastDFSServiceImpl extends Resources implements FastDFSService {


    @Override
    public boolean uploadFile(UploadFile uploadFile) {
        return fastDFSExtraMapper.insert(uploadFile)>0;
    }

    @Override
    public UploadFile queryFileById(String fileId) {
        return fastDFSExtraMapper.getById(fileId);
    }

    @Override
    public boolean batchInsertFile(List<UploadFile> uploadFileList) {
        return fastDFSExtraMapper.batchInsert(uploadFileList)==uploadFileList.size();
    }

    @Override
    public List<UploadFile> queryBatchFile(String... fileIds) {
        return fastDFSExtraMapper.getFilesByIds(fileIds);
    }

    @Override
    public boolean deleteByFileId(String fileId) {
        return fastDFSExtraMapper.deleteById(fileId)>0;
    }
}
