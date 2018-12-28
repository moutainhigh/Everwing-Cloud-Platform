package com.everwing.server.wy.web.controller.cust.person;

import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.*;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNew;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNewSearch;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.wy.api.cust.person.PersonCustApi;
import com.everwing.coreservice.wy.api.sys.TSysImportExportApi;
import com.everwing.server.wy.web.controller.sys.TSysImportExportController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings({ "rawtypes", "deprecation" })
@Controller
@RequestMapping(value="/PersonCustNew")
public class PersonCustController {

	@Autowired
	@Qualifier("personCustApi")
	private PersonCustApi personCustApi;
	
	@Autowired
    private TSysImportExportApi tSysImportExportApi;

	@Autowired
	private SpringRedisTools springRedisTools;
	
//	@PostMapping("/loadPersonPickUpList")
//	public @ResponseBody BaseDto loadPersonPickUpList(@RequestBody PersonCustNew cust) throws Exception{
//		return BaseDtoUtils.getDto(this.personCustApi.loadPersonPickUpList(cust));
//	}
//
//	@GetMapping("/loadPersonByProjectId")
//	public @ResponseBody BaseDto getPersonByProjectId(String projectId){
//		return BaseDtoUtils.getDto(this.personCustApi.getPersonByProjectId(projectId));
//	}
//
//	@RequestMapping("/listPersonCustByBuildingCode")
//	@ResponseBody
//	public BaseDto listPersonCustByBuildingCode(@RequestBody PersonCustNew cust){
//		return BaseDtoUtils.getDto(this.personCustApi.listPersonCustByBuildingCode(cust));
//	}
//
//
//	@RequestMapping(value="/findPersonCustNewByhouseId/{houseid}",method=RequestMethod.GET)
//	@ResponseBody
//	public BaseDto findPersonCustNewByhouseId(HttpServletRequest req,@PathVariable("houseid") String houseid){
//		return BaseDtoUtils.getDto(this.personCustApi.findPersonCustNewByhouseId(CommonUtils.getCompanyId(req), houseid));
//	}
//
//	//查询所有客户信息
//	@RequestMapping(value="/listAllPersonCustNewRestful",method=RequestMethod.GET)
//	@ResponseBody
//	public BaseDto listAllPersonCustNewRestful(HttpServletRequest req){
//		return BaseDtoUtils.getDto(this.personCustApi.listAllPersonCustNewRestful(CommonUtils.getCompanyId(req)));
//	}
//

//
//
//
//	@RequestMapping(value="/findPersonCustNewRestful",method=RequestMethod.POST)
//    @ResponseBody
//    public BaseDto findPersonCustNewRestful(HttpServletRequest req ,@RequestBody PersonCustNew personCustNew){
//		return BaseDtoUtils.getDto(this.personCustApi.findPersonCustNewRestful(CommonUtils.getCompanyId(req),personCustNew));
//	}
//
//	@RequestMapping(value="/getPersonCustNewsByIds",method=RequestMethod.POST)
//	public @ResponseBody BaseDto getPersonCustNewsByIds(HttpServletRequest req ,@RequestBody String custIds){
//		return BaseDtoUtils.getDto(this.personCustApi.getPersonCustNewsByIds(CommonUtils.getCompanyId(req),custIds));
//	}
//
//	/**
//     * 添加客户基本信息
//     * @param PersonCustNew
//     */
//    @RequestMapping(value="/insertPersonCustNewRestful1/{ids}",method=RequestMethod.POST)
//    public @ResponseBody BaseDto insertPersonCustNewRestful1(HttpServletRequest req , @PathVariable("ids") String ids,@RequestBody PersonCustNew PersonCustNew){
//    	return BaseDtoUtils.getDto(this.personCustApi.insertPersonCustNewRestful1(CommonUtils.getCompanyId(req),ids,PersonCustNew));
//    }
//
//
//
//
//
//    /**
//     * 把logo图片添加到附件表中
//     */
//    @RequestMapping(value="/insertLogoNewRestful",method=RequestMethod.POST)
//    public @ResponseBody BaseDto insertLogo(HttpServletRequest req , @RequestBody PersonCustNew personCustNew){
//    	return BaseDtoUtils.getDto(this.personCustApi.insertLogo(CommonUtils.getCompanyId(req),personCustNew));
//    }
//
//    /**
//     * 根据id查询附件，在前台可以取出图片
//     * @return
//     */
//     @RequestMapping(value="/getLogoByIdRestful/{companyId}",method=RequestMethod.GET)
//     public @ResponseBody BaseDto getLogoById(HttpServletRequest req , @PathVariable String companyId){
//    	 return BaseDtoUtils.getDto(this.personCustApi.getLogoById(CommonUtils.getCompanyId(req),companyId));
//     }
//
//
//
//
//
//
//     //分页显示所有个人客户信息
//     @RequestMapping(value="/listPagePersonCustRestful",method=RequestMethod.POST)
//     public @ResponseBody BaseDto listPagePersonCust(HttpServletRequest req , @RequestBody PersonCustNew personCustNew){
//    	 return BaseDtoUtils.getDto(this.personCustApi.listPagePersonCust(CommonUtils.getCompanyId(req),personCustNew));
//     }
//
//     @RequestMapping(value="/listPageSearchPersonCust",method=RequestMethod.POST)
//     public @ResponseBody BaseDto listPageSearchPersonCust(HttpServletRequest req , @RequestBody Search search){
//    	 return BaseDtoUtils.getDto(this.personCustApi.listPageSearchPersonCust(CommonUtils.getCompanyId(req),search));
//     }
//
//     @RequestMapping(value="/propertyServiceSearch",method=RequestMethod.POST)
//     public @ResponseBody BaseDto propertyServiceSearch(HttpServletRequest req , @RequestBody Search search){
//    	 return BaseDtoUtils.getDto(this.personCustApi.propertyServiceSearch(CommonUtils.getCompanyId(req) , search));
//     }
//
//     @RequestMapping(value="/findEnterpriseCustByIdRestful/{enterpriseCustId}",method=RequestMethod.GET)
//     public @ResponseBody BaseDto findEnterpriseCustById(HttpServletRequest req,@PathVariable String enterpriseCustId){
//    	 return BaseDtoUtils.getDto(this.personCustApi.findEnterpriseCustById(CommonUtils.getCompanyId(req),enterpriseCustId));
//     }
//
//     @RequestMapping(value="/propertyServiceSearchCommon",method=RequestMethod.POST)
//     public @ResponseBody BaseDto propertyEntitySearch(HttpServletRequest req , @RequestBody Search search){
//    	 return BaseDtoUtils.getDto(this.personCustApi.propertyEntitySearch(CommonUtils.getCompanyId(req), search));
//     }
//
//     /**
//      * 根据条件查询出办理人及授权人车位，车辆，房屋
//      * @param search
//      * @return
//      */
//     @RequestMapping(value="/propertyServiceAuSearch",method=RequestMethod.POST)
//     public @ResponseBody BaseDto propertyEntityAuSearch(HttpServletRequest req , @RequestBody Search search){
//    	 return BaseDtoUtils.getDto(this.personCustApi.propertyEntityAuSearch(CommonUtils.getCompanyId(req),search));
//     }
//
//     @RequestMapping(value="/listAllPersonBySearch",method=RequestMethod.POST)
//     public @ResponseBody BaseDto listAllPersonBySearch(HttpServletRequest req , @RequestBody Search search){
//    	 return BaseDtoUtils.getDto(this.personCustApi.listAllPersonBySearch(CommonUtils.getCompanyId(req),search));
//     }
//
//     @RequestMapping(value="/listPagePersonCustByCondition",method=RequestMethod.POST)
//   	 public @ResponseBody BaseDto listPagePersonCustByCondition(HttpServletRequest req, @RequestBody PersonCustNew personCustNew){
//    	 return BaseDtoUtils.getDto(this.personCustApi.listPagePersonCustByCondition(CommonUtils.getCompanyId(req),personCustNew));
//     }
//
//
//    @RequestMapping(value="/importCqExcelFile/{projectId}",method=RequestMethod.POST)
//  	public @ResponseBody BaseDto importCqExcelFile(HttpServletRequest req , @RequestBody Annex annex,@PathVariable String projectId){
//    	return BaseDtoUtils.getDto(this.personCustApi.importCqExcelFile(CommonUtils.getCompanyId(req),annex,projectId));
//    }
//
//    @RequestMapping(value="/listPagelistSearchPersonCust",method=RequestMethod.POST)
//  	public @ResponseBody BaseDto listPagelistSearchPersonCust(HttpServletRequest req , @RequestBody PersonCustNew personCustNew){
//    	return BaseDtoUtils.getDto(this.personCustApi.listPageListSearchPersonCust(CommonUtils.getCompanyId(req),personCustNew));
//    }
//
//    /**
//	 * 根据建筑id查询该房屋关系的所有人
//	 * @return
//	 */
//    @RequestMapping(value="/listPersonCustNewByBuildingStructureId/{buildingStructureId}",method=RequestMethod.GET)
//	public @ResponseBody BaseDto findCustByBsId(HttpServletRequest req , @PathVariable String buildingStructureId){
//    	return BaseDtoUtils.getDto(this.personCustApi.findCustByBsId(CommonUtils.getCompanyId(req),buildingStructureId));
//    }
//
//    /**
//	 * 根据建筑结构id获取客户信息
//	 * @param buildingStructureid
//	 * @return
//	 */
//    @RequestMapping(value="/getSelectPersonNew/{buildingStructureid}",method=RequestMethod.GET)
//    public @ResponseBody BaseDto getSelectPersonNew(HttpServletRequest req , @PathVariable String buildingStructureid){
//    	return BaseDtoUtils.getDto(this.personCustApi.getSelectPerson(CommonUtils.getCompanyId(req),buildingStructureid));
//    }
//
//
//    /**
//  	 * 根据身份证等 查询
//  	 */
//    @RequestMapping(value="/listPagePersonCustNewByAllSearch",method=RequestMethod.POST)
//    public @ResponseBody BaseDto listPagePersonCustNewByAllSearch(HttpServletRequest req , @RequestBody PersonCustNew personCustNew){
//    	return BaseDtoUtils.getDto(this.personCustApi.listPagePersonCustNewByAllSearch(CommonUtils.getCompanyId(req),personCustNew));
//    }
//
//    /**
//	 * 根据选中的房屋获取与房屋相关联的人员
//	 */
//    @RequestMapping(value="/getPersonCustNew",method=RequestMethod.POST)
//    public @ResponseBody BaseDto getPersonCustNew(HttpServletRequest req , @RequestBody Search search){
//    	return BaseDtoUtils.getDto(this.personCustApi.getPersonCustNew(CommonUtils.getCompanyId(req),search));
//    }
//
//    /**
//	 *个人客户导出
//	 */
//	@RequestMapping(value="/exportPersonCustNewFile",method=RequestMethod.POST)
//	public @ResponseBody BaseDto exportPersonCustNewFile(HttpServletRequest req , @RequestBody PersonCustNew personCustNew){
//		return BaseDtoUtils.getDto(this.personCustApi.exportPersonCustNewFile(CommonUtils.getCompanyId(req),personCustNew));
//	}
//
//	@RequestMapping(value="/getCountByNoOrPhone",method=RequestMethod.POST)
//	public @ResponseBody BaseDto getCountByNoOrPhone(HttpServletRequest req , @RequestBody PersonCustNew personCustNew){
//		return BaseDtoUtils.getDto(this.personCustApi.getCountByNoOrPhone(CommonUtils.getCompanyId(req),personCustNew));
//	}
//
//	/**
//	 * 分页显示所有个人客户信息
//	 */
//	@RequestMapping(value="/listPagePersonCustInfos",method=RequestMethod.POST)
//	public @ResponseBody BaseDto listPagePersonCustInfos(HttpServletRequest req , @RequestBody PersonCustNew PersonCustNew){
//		return BaseDtoUtils.getDto(this.personCustApi.listPagePersonCustInfos(CommonUtils.getCompanyId(req),PersonCustNew));
//	}
//
//	@RequestMapping(value="/getAllInfosByBuildingId/{buildingId}",method=RequestMethod.GET)
//	public @ResponseBody BaseDto getAllInfosByBuildingId(HttpServletRequest req , @PathVariable String buildingId){
//		return BaseDtoUtils.getDto(this.personCustApi.getAllInfosByBuildingId(CommonUtils.getCompanyId(req),buildingId));
//	}
//
//

//
//
//	@RequestMapping(value="/testImport",method=RequestMethod.GET)
//	public @ResponseBody BaseDto testImport(HttpServletRequest req){
//
//		BaseDto returnBaseDto = new BaseDto();
//
//		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		final Map<String, PersonCustNew> codeMap = new HashMap<String, PersonCustNew>();
//		List<PersonCustNew> allCustomerList = new ArrayList<PersonCustNew>();
//		String filePath = "C:\\Users\\DELL\\Desktop\\门禁对讲业主数据0508\\门禁对讲业主数据0508\\批量转换\\1号楼.xls";
//		if (CommonUtils.isNotEmpty(filePath)) {
//			try{
//				final List<Map<Short, String>> dataList = new ExcelUtils().readExcel(filePath);
//
//				if (dataList != null && dataList.size() > 0) {
//
//					for (int i = 0; i < allCustomerList.size(); i++) {
//						codeMap.put(allCustomerList.get(i).getCardNum(), allCustomerList.get(i));  //身份证号为Key , 客户对象为value
//					}
//
//					final String key = UUID.randomUUID().toString();
//					Runnable th = new Runnable() {
//						@Override
//						public void run() {
//							final Map<String, String> importIdCard = new HashMap<String, String>();
//							int totalNum = dataList.size();
//							int bsnum = 0;
//							int countNum = 0;
//							String num = "aaaaa"; //客户编码最大值  10005
//							for (Map<Short, String> t : dataList) {
//								boolean check = true;
//								final PersonCustNew personCustNew = new PersonCustNew();
//								// 姓名
//								if (!StringUtils.isEmpty(t.get((short) 0).trim())) {
//									personCustNew.setName(t.get((short) 0));
//								} else {
//									check = false;
//								}
//								// 性别
//								if (check && !StringUtils.isEmpty(t.get((short) 1).trim())) {
//									if (t.get((short) 1).equals("男")) {
//										personCustNew.setSex("0");
//									} else {
//										personCustNew.setSex("1");
//									}
//								} else {
//									check = false;
//								}
//								// 民族
//								if (check && !StringUtils.isEmpty(t.get((short) 2).trim())) {
//									personCustNew.setNation(t.get((short) 2));
//								}
//								// 证件类型
//								if (check && !StringUtils.isEmpty(t.get((short) 3).trim())) {
//									if ("身份证".equals(t.get((short) 3))) {
//										personCustNew.setCardType("0");
//									} else {
//										personCustNew.setCardType("1");// 护照
//									}
//
//								} else {
//									check = false;
//								}
//								if ("0".equals(personCustNew.getCardType())) {
//									// 证件号码
//									String idCard_tmp = CommonUtils.null2String(t.get((short) 4).trim());
//									if(idCard_tmp.endsWith("S") || idCard_tmp.endsWith("s")){
//										idCard_tmp = idCard_tmp.substring(0,idCard_tmp.length()-1);
//									}
//									if (ValidatorUtil.isIdcard(idCard_tmp)) {
//
//										if (importIdCard.containsKey(idCard_tmp)) {
//											check = false;
//										} else {
//											importIdCard.put(idCard_tmp, idCard_tmp);
//										}
//
//										if (idCard_tmp.length() >= 18) {
//											if (codeMap.containsKey(idCard_tmp + "S")) {
//												check = false;
//											} else {
//												personCustNew.setCardNum(idCard_tmp + "S");
//											}
//										} else if (t.get((short) 4).length() >= 15) {
//											if (codeMap.containsKey(idCard_tmp)) {
//												check = false;
//											} else {
//												personCustNew.setCardNum(idCard_tmp);
//											}
//										}
//									} else {
//										check = false;
//									}
//								} else {
//									if (codeMap.containsKey(t.get((short) 4).trim())) {
//										continue;
//									} else {
//										personCustNew.setCardNum(t.get((short) 4));
//									}
//								}
//
//								if (check && !StringUtils.isEmpty(t.get((short) 5).trim())) {
//
//									String birthday = CommonUtils.null2String(t.get((short) 5));
//									if(!Pattern.compile("yyyy-MM-dd").matcher(birthday).matches()){
//										//check = false;
//									}else{
//										try {
//											// 出生日期
//											personCustNew.setBirthday(sdf.parse(t.get((short) 5)));
//										} catch (ParseException e) {
//											e.printStackTrace();
//										}
//									}
//								}
//								if (check && !StringUtils.isEmpty(t.get((short) 6).trim())) {
//									if ("已婚".equals(t.get((short) 6))) {// 婚姻状况
//										personCustNew.setMarrieState((byte) 0);
//									} else {
//										personCustNew.setMarrieState((byte) 1);// 婚否
//									}
//								}
//								if (check && !StringUtils.isEmpty(t.get((short) 7).trim())) {
//									personCustNew.setRegisterPhone(t.get((short) 7));// 注册号码
//								} else {
//									check = false;
//								}
//
//								String nativePlace = t.get((short) 8).trim();
//								if (check && !StringUtils.isEmpty(nativePlace)) {
//									personCustNew.setNativePlace(nativePlace);// 籍贯
//								}
//
//								// 紧急联系人电话
//								if (check && !StringUtils.isEmpty(t.get((short) 10).trim().trim())) {
//									personCustNew.setUrgentContactPhone(t.get((short) 10));
//								}
//
//								if (check && !StringUtils.isEmpty(t.get((short) 11).trim())) {
//									personCustNew.setCensus(t.get((short) 11));// 户口所在地
//								}
//								if (check && !StringUtils.isEmpty(t.get((short) 12).trim())) {
//									personCustNew.setEmail(t.get((short) 12));// 电子邮箱
//								}
//								if (check && !StringUtils.isEmpty(t.get((short) 13).trim())) {
//									personCustNew.setWorkUnits(t.get((short) 13));// 工作单位
//								}
//								if (check && !StringUtils.isEmpty(t.get((short) 14).trim())) {
//									personCustNew.setPhoneNum(t.get((short) 14));
//								}
//
//								if (check && !StringUtils.isEmpty(t.get((short) 15).trim().trim())) {
//									personCustNew.setPhoneNum1(t.get((short) 15));
//								}
//								if (check && !StringUtils.isEmpty(t.get((short) 16).trim())) {
//									personCustNew.setPhoneNum2(t.get((short) 16));
//								}
//
//								String urgentContactPerson = t.get((short) 9).trim();
//								if (check && !StringUtils.isEmpty(urgentContactPerson)) {
//									personCustNew.setUrgentContactPerson(urgentContactPerson);// 紧急联系人
//								}
//
//								personCustNew.setCustId(UUID.randomUUID().toString());
//								if (num == null || num.equals("")) {
//									num = "10000";
//								} else {
//									num = String.valueOf(Integer.parseInt(num) + 1);
//								}
//								personCustNew.setCustCode(num);// 客户编号
//								countNum++;
//								if (check) {
//									JSONObject obj = new JSONObject();
//									obj.put("opr", MqConstants.PERSON_CUST_ADD);
//									obj.put("cust", personCustNew);
//
//									bsnum++;
//
//									// 添加个人账户表、押金账户表、个人账户和押金账户的管理 王洲 2016.04.15
//									Account account = new Account();// 个人账户
//									AssetAccount assetAccount = new AssetAccount();// 押金账户
//									AccountRelation accountRelation = new AccountRelation();// 关系表
//
//									// 个人账户对象添加数据
//									account.setAccountId(UUID.randomUUID().toString());
//									account.setAccountNum(personCustNew.getCustCode());
//									account.setIsBankCard((byte) 0);
//									account.setProjectId(personCustNew.getProjectId());
//									account.setDisposableBalance((double) 0);
//									account.setTotalBalance((double) 0);
//									account.setCustId(personCustNew.getCustId());
//
//									// 押金账户对象添加数据
//									assetAccount.setAssetAccountId(UUID.randomUUID().toString());
//									assetAccount.setAssetAccountNum("cust" + personCustNew.getCustCode());
//									assetAccount.setIsBankCard((byte) 1);
//									assetAccount.setProjectId(personCustNew.getProjectId());
//									assetAccount.setAssetAccountBalance((double) 0);
//									assetAccount.setAssetAccountType((byte) 1);
//									assetAccount.setTotalDeposits((double) 0);
//									assetAccount.setTotalRefundableDeposit((double) 0);
//									assetAccount.setGeneralBalance((double) 0);
//
//									// 关联表
//									accountRelation.setAccountRelationId(UUID.randomUUID().toString());
//									accountRelation.setAccountId(account.getAccountId());
//									accountRelation.setAssetAccount(assetAccount.getAssetAccountId());
//								}
//								MapUtils.getMap().put(key, bsnum + "|" + totalNum + "|" + countNum);
//							}
//						}
//					};
//					Thread t = new Thread(th);
//					t.start();
//					returnBaseDto.setObj(key);
//					returnBaseDto.setMessageMap(new MessageMap(null, MessageMap.INFOR_SUCCESS));
//					return returnBaseDto;
//
//				}
//			}  catch (Exception e1){
//				throw new ECPBusinessException(e1);
//			}
//		}
//		returnBaseDto.setMessageMap(new MessageMap(MessageMap.INFOR_ERROR,"数据导入失败!"));
//		return returnBaseDto;
//	}



	/** ------------------------------------------------- 华丽丽分割线 --------------------------------------------------------------------- **/
	/**
	 * 根据条件分页查询客户信息
	 * @param req
	 * @param personCustNew
	 * @return
	 */
	@RequestMapping(value="/listPagePersonCustNewBySearch",method=RequestMethod.POST)
	public @ResponseBody BaseDto listPagePersonCustNewBySearch(HttpServletRequest req , @RequestBody PersonCustNewSearch personCustNew){
		return BaseDtoUtils.getDto(this.personCustApi.listPagePersonCustNewBySearch(CommonUtils.getCompanyId(req),personCustNew));
	}


	/**
	 * 根据id获取客户的资产绑定关系
	 * @param req
	 * @param custId
	 * @return
	 */
	@RequestMapping(value="/getPersonBuildingByCustId/{custId}",method=RequestMethod.GET)
	public @ResponseBody BaseDto getPersonBuildingByCustId(HttpServletRequest req , @PathVariable String custId){
		return BaseDtoUtils.getDto(this.personCustApi.getPersonBuildingByCustId(CommonUtils.getCompanyId(req),custId));
	}


	/**
	 * 修改客户基本信息
	 * @param req
	 * @param personCustNew
	 * @return
	 */
	@RequestMapping(value="/updatePersonCustNewRestful",method=RequestMethod.POST)
	public @ResponseBody BaseDto updatePersonCustNewRestful(HttpServletRequest req , @RequestBody PersonCustNew personCustNew){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.personCustApi.updatePersonCustNewRestful(ctx, personCustNew));
	}

	/**
	 * 添加客户基本信息不关联建筑信息
	 * @param req
	 * @param personCustNew
	 * @return
	 */
	@RequestMapping(value="/insertPersonCustNewRestful",method=RequestMethod.POST)
	public @ResponseBody BaseDto insertPersonCustNewRestful(HttpServletRequest req , @RequestBody PersonCustNew personCustNew){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.personCustApi.insertPersonCustNewRestful(ctx,personCustNew));
	}

	/**
	 * 根据id删除客户基本信息
	 * @param req
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/deletePersonCustNewRestful/{id}",method=RequestMethod.GET)
	@ResponseBody
	public MessageMap deletePersonCust(HttpServletRequest req , @PathVariable String id){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		this.personCustApi.deletePersonCust(ctx,id);
		return new MessageMap("true","删除成功！");
	}

	/**
	 * web端导入入口
	 * 这部分代码，你只需要修改方法名和访问地址value的值即可
	 * @param projectId
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/importPersonCustInfo/{projectId}", method = RequestMethod.POST)
	public @ResponseBody MessageMap importPersonCust(@PathVariable String projectId) {

		WyBusinessContext ctx = WyBusinessContext.getContext();
		MessageMap mm = new MessageMap();
		Object obj = springRedisTools.getByKey(String.format(TSysImportExportController.UPLOAD_FILE_KEY,ctx.getLoginName()));
		if(obj != null){
			springRedisTools.deleteByKey(String.format(TSysImportExportController.UPLOAD_FILE_KEY,ctx.getLoginName()));
			String batchNo = obj.toString();
			TSysImportExportSearch tSysImportExportRequest = new TSysImportExportSearch();
			tSysImportExportRequest.setProjectCode(projectId);
			tSysImportExportRequest.setBatchNo(batchNo);
			String CompanyId = ctx.getCompanyId();
			ctx.setProjectId(projectId);
			RemoteModelResult remoteModelResult = personCustApi.importPersonCust(ctx, tSysImportExportRequest);
			if (!remoteModelResult.isSuccess()) {
				mm.setFlag(MessageMap.INFOR_ERROR);
				mm.setMessage(remoteModelResult.getMsg());
			}
		}

		return mm;

	}

	/**
	 * 根据cust_id加载客户和账户的信息
	 */
	@RequestMapping(value = "loadCustAccountInfo/{id}",method = RequestMethod.GET)
	public @ResponseBody BaseDto loadCustAccountInfo(@PathVariable String id){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		RemoteModelResult remoteModelResult=personCustApi.queryCustAccountInfo(ctx,id);
		return BaseDtoUtils.getDto(remoteModelResult);
	}

}
