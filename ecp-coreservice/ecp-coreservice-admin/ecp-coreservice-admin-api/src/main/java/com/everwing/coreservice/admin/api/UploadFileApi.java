package com.everwing.coreservice.admin.api;

import com.everwing.coreservice.common.admin.service.UploadFileService;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UploadFileApi {

	@Autowired
	private UploadFileService uploadFileService;

	
	public RemoteModelResult<Void> insertSelective(UploadFile uploadFile){
		return uploadFileService.insertSelective(uploadFile);
	}

}