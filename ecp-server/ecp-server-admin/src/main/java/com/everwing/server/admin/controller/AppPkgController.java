package com.everwing.server.admin.controller;

import com.everwing.coreservice.common.admin.util.PageBean;
import com.everwing.coreservice.common.platform.entity.generated.AppPkg;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Controller
@RequestMapping("/appPkg")
public class AppPkgController extends BaseController {
	
	@GetMapping("/main")
	public String index() {
		return toAdminView("app_pkg");
	}
	
	@GetMapping("/list")
	public @ResponseBody String list(PageBean pageBean) {
		addParamToPageBean(pageBean, "version", "timeStart", "type", "timeEnd");
		return renderAdminJson(commonAdminApi.listAppPkgByPage(pageBean));
	}

	@GetMapping("/delete")
	public @ResponseBody String delete(String id) {
		return renderApiJson(commonAdminApi.deleteAppPkgByPrimaryKey(id));
	}

	@GetMapping("/update")
	public @ResponseBody String update(AppPkg appPkg) {
		if (appPkg.getStatus() !=null && appPkg.getStatus() == 1) {
			//先禁用同类型所有数据
			commonAdminApi.banAllPkgByType(appPkg.getType());
			
			appPkg.setAvailableTime(new Date());
			appPkg.setAvailableAccountId(getCurrUser().getAccountId());
		}
		return renderApiJson(commonAdminApi.updateAppPkgByPrimaryKeySelective(appPkg));
	}

	@PostMapping("/save")
	public @ResponseBody String save(AppPkg appPkg, MultipartFile uploadFile) throws Exception {
		
		//保存到文件系统
		UploadFile file = getModel(fastDFSApi.uploadFile(uploadFile));
		String fileId = file.getUploadFileId();
		
		
		// 插入升级包数据
		appPkg.setMd5(DigestUtils.md5Hex(uploadFile.getInputStream()));
		appPkg.setStatus(0);
		appPkg.setAppPkgId(fileId);
		appPkg.setUploadTime(new Date());
		appPkg.setUploadAccountId(getCurrUser().getAccountId());
		appPkg.setPkgFileId(fileId);
		handleResult(commonAdminApi.addAppPkg(appPkg));
		
		 return renderSuccess();
	}

}