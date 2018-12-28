package com.everwing.server.platform.controller.api;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController extends BaseApiController {

	@PostMapping("/upload")
	public String upload(MultipartFile uploadFile) throws Exception {
//		UploadFile saveFile = fileService.upload(uploadFile, null,null);
		UploadFile saveFile = getModel(fastDFSApi.uploadFile(uploadFile));
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("id", saveFile.getUploadFileId());
		return renderJson(new RemoteModelResult<Map>(data));
	}

	@PostMapping("/batchUpload")
	public String batchUpload(MultipartFile[] files)throws Exception{
		return renderJson(fastDFSApi.batchUploadFile(files));
	}

	@GetMapping("/loadPathByIds")
	public String loadPathByIds(String fileIds){
		if(StringUtils.isNotEmpty(fileIds)) {
			return renderJson(fastDFSApi.loadFilesByIds(fileIds.split(",")));
		}
		return renderJson(new RemoteModelResult<>(null));
	}

	@GetMapping("/{fileId}")
	public void download(@PathVariable String fileId,HttpServletResponse response) throws IOException {
//		UploadFile uploadFile = new UploadFile();
//		uploadFile.setUploadFileId(fileId);
//		UploadFile file = getModel(
//				commonQueryApi.queryFrom(UploadFile.class, uploadFile).singleResult());
//
//		File localFile = fileService.getFileAbsPath(file);
//		// 临时使用流，后面重定向到Nginx地址
//		int size = IOUtils.copy(new FileInputStream(localFile), response.getOutputStream());
//		response.setHeader("Content-Length", "" + size);
		
		UploadFile uploadFile = fastDFSApi.loadFilePathById(fileId).getModel();
		response.setHeader("Content-Length", ""+uploadFile.getSize());
		response.sendRedirect(uploadFile.getPath());
	}
}