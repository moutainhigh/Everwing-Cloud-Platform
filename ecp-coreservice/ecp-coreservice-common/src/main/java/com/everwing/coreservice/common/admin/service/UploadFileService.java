package com.everwing.coreservice.common.admin.service;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;

public interface UploadFileService {

	RemoteModelResult<Void> insertSelective(UploadFile uploadFile);

}
