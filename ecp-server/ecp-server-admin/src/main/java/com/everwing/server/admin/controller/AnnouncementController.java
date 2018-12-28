package com.everwing.server.admin.controller;

import com.everwing.coreservice.common.admin.util.PageBean;
import com.everwing.coreservice.common.platform.entity.generated.Announcement;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/announcement")
public class AnnouncementController extends BaseController {

	@GetMapping("/main")
	public String index() {
		List<Company> companyList = getModel(commonQueryApi.selectListByExample(Company.class, null));
		getCurrRequest().setAttribute("companyList", companyList);
		return toAdminView("announcement");
	}

	@PostMapping("/list")
	public @ResponseBody String list(PageBean pageBean) {
		addParamToPageBean(pageBean, "query_content");
		String status = getCurrRequest().getParameter("status");
		if (!"all".equals(status)) {
			pageBean.addParam("status", status);
		}
		return renderAdminJson(commonAdminApi.listAnnouncementByPage(pageBean));
	}

	@GetMapping("/release")
	public @ResponseBody String release(String announcementIds) {
		String[] ids = announcementIds.split(",");
		for (String id : ids) {
			if (StringUtils.isNoneBlank(id)) {
				Announcement announcement = new Announcement();
				announcement.setAnnouncementId(id);
				announcement.setStatus(1);
				handleResult(commonQueryApi.updateByPrimaryKeySelective(announcement));
			}
		}

		return renderSuccess();
	}

	@PostMapping("/save")
	public @ResponseBody String save(Announcement announcement) {
		announcement.setCreateAccountId(getCurrUser().getAccountId());
		announcement.setAnnouncementId(randomUUID());
		announcement.setCreateDate(new Date());
		announcement.setIsReaded(false);
		if (announcement.getStatus() == null) {
			announcement.setStatus(0);
		}
		handleResult(commonQueryApi.insertSelective(announcement));
		return renderSuccess();
	}
}