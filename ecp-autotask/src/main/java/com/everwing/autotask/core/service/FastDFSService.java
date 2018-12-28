package com.everwing.autotask.core.service;

import com.everwing.coreservice.common.platform.entity.generated.UploadFile;

import java.io.File;

/**
 * @author DELL shiny
 * @create 2018/6/4
 */
public interface FastDFSService {

    UploadFile uploadFile(File file);
}
