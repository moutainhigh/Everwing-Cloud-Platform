package com.everwing.coreservice.admin.core.service.impl;

import com.everwing.coreservice.common.admin.service.UploadFileService;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.platform.util.MapperResources;
import org.springframework.stereotype.Component;

@Component
public class UploadFileServiceImpl extends MapperResources implements UploadFileService {


	@Override
	public RemoteModelResult<Void> insertSelective(UploadFile uploadFile) {
		uploadFileMapper.insertSelective(uploadFile);
		return new RemoteModelResult<Void>();
	}
}
