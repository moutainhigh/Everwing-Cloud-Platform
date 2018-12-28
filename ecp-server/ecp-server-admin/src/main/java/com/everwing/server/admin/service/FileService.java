package com.everwing.server.admin.service;

import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.server.admin.controller.BaseController;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@Component
public class FileService extends BaseController {
	@Value("${admin.file.save.path}")
	private String fileBasePath;

	/**
	 * @param fileId
	 *            指定文件ID，null则表示不指定
	 * @description 上传文件到本地
	 */
	public UploadFile upload(MultipartFile multipartFile, String fileId) {
		// 插入文件数据
		UploadFile saveFile = new UploadFile();
		String originalFilename = multipartFile.getOriginalFilename();
		int endIndex = originalFilename.lastIndexOf(".") + 1;
		String fileName = originalFilename.substring(0, endIndex - 1);
		String fileSuffix = originalFilename.substring(endIndex);

		saveFile.setUploadFileId(StringUtils.isBlank(fileId) ? randomUUID() : fileId);
		saveFile.setUploadTime(new Date());
		saveFile.setFileName(fileName);
		saveFile.setSize(multipartFile.getSize());
		saveFile.setPath(generatePath());
		saveFile.setSuffix(fileSuffix);
		saveFile.setAccountId(getCurrUser().getAccountId());
		handleResult(uploadFileApi.insertSelective(saveFile));

		// 保存到本地
		File file = new File(new File(fileBasePath, saveFile.getPath()),
				saveFile.getUploadFileId() + "." + saveFile.getSuffix());
		if (!file.getParentFile().exists()) {// 创建目录
			file.getParentFile().mkdirs();
		}
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(file);
			IOUtils.write(multipartFile.getBytes(), output);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		return saveFile;
	}

	private String generatePath() {
		Calendar now = Calendar.getInstance();
		return now.get(Calendar.YEAR) + File.separator + (now.get(Calendar.MONTH) + 1)
				+ File.separator + now.get(Calendar.DATE);
	}

	public File getFileAbsPath(UploadFile file) {
		return new File(new File(fileBasePath, file.getPath()),
				file.getUploadFileId() + "." + file.getSuffix());
	}

	public String getFileBasePath() {
		return fileBasePath;
	}
}
