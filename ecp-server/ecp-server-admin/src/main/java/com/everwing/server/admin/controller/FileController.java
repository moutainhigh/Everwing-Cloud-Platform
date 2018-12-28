package com.everwing.server.admin.controller;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController extends BaseController {

	@PostMapping("/upload")
	public String upload(MultipartFile uploadFile) throws Exception {
		UploadFile saveFile = getModel(fastDFSApi.uploadFile(uploadFile));
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("id", saveFile.getUploadFileId());
		return renderAdminJson(new RemoteModelResult<Map>(data));
		
//		UploadFile saveFile = fileService.upload(uploadFile, null);
//		ResponseResult responseResult = new ResponseResult();
//		HashMap<String, Object> data = new HashMap<String, Object>();
//		data.put("id", saveFile.getUploadFileId());
//		return renderApiJson(responseResult);
	}

	@GetMapping("/{fileId}")
	public void download(@PathVariable String fileId, HttpServletResponse response)
			throws FileNotFoundException, IOException {
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
		logger.info("文件重定向：{}",uploadFile.getPath());
	}
}