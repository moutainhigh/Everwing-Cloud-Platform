package com.everwing.coreservice.wy.core.service.impl.cust.enterprise;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.ExcelUtils;
import com.everwing.coreservice.common.utils.MapUtils;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.EnterpriseCustNew;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.EnterpriseCustNewSearch;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNew;
import com.everwing.coreservice.common.wy.entity.cust.person.relation.PersonCustRelation;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.common.wy.service.cust.enterprise.EnterpriseCustService;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.core.request.ExcelInfo;
import com.everwing.coreservice.wy.core.utils.Excel;
import com.everwing.coreservice.wy.dao.mapper.annex.AnnexMapper;
import com.everwing.coreservice.wy.dao.mapper.common.ImportExportMapper;
import com.everwing.coreservice.wy.dao.mapper.cust.enterprisecust.EnterpriseCustNewMapper;
import com.everwing.coreservice.wy.dao.mapper.cust.person.PersonCustNewMapper;
import com.everwing.coreservice.wy.dao.mapper.cust.person.relation.PersonCustRelationMapper;
import com.everwing.coreservice.wy.utils.ExcelEntity;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("enterpriseCustService")
public class EnterpriseCustServiceImpl implements EnterpriseCustService {
	
	@Autowired
	private FastDFSApi fastDFSApi;

	@Autowired
	private EnterpriseCustNewMapper enterpriseCustNewMapper;
	
	@Autowired
	private PersonCustNewMapper personCustNewMapper;
	
	@Autowired
	private AnnexMapper annexMapper;
	
	@Autowired
	private ImportExportMapper importExportMapper;

	@Autowired
	private PersonCustRelationMapper personCustRelationMapper;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listAllEnterpriseCust(String companyId) {
		BaseDto dto = new BaseDto();
		dto.setLstDto(this.enterpriseCustNewMapper.listAllEnterpriseCustNew());
		return dto;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto getEnterpiseById(String companyId, String enterpriseId) {
		BaseDto baseDto = new BaseDto();
		
		EnterpriseCustNew ec = this.enterpriseCustNewMapper.getEnterpriseCustById(enterpriseId);
		if(null != ec && CommonUtils.isNotEmpty(ec.getAnnexs())){
			for(Annex annex : ec.getAnnexs()){
				annex.setAnnexAddress(this.fastDFSApi.getImgBaseUri() + annex.getAnnexAddress());
			}
		}
		
		baseDto.setObj(ec);
		return baseDto;
	}







	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPageEnterpriseCust(String companyId,EnterpriseCustNew enterpriseCustNew) {
		return new BaseDto(addPath2AnnexAddress(this.enterpriseCustNewMapper.listPageEnterpriseCustNew(enterpriseCustNew)), enterpriseCustNew.getPage());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPageParams(String companyId,EnterpriseCustNew enterpriseCustNew) {
		return new BaseDto(addPath2AnnexAddress(this.enterpriseCustNewMapper.listPageEnterpriseByParams(enterpriseCustNew)), enterpriseCustNew.getPage());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto findEnterpriseCust(String companyId,EnterpriseCustNew enterpriseCustNew) {
		return new BaseDto(this.enterpriseCustNewMapper.findEnterpriseCustNew(enterpriseCustNew),null);
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public BaseDto importExcelFiles(String companyId, Annex annex) {
		return null;
	}
	
	
	
	private EnterpriseCustNew setEnterpriseCustNew(Map<Short, String> t){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		EnterpriseCustNew e=new EnterpriseCustNew();
		try{			
			e.setEnterpriseName(t.get((short) 0));//企业姓名
			e.setManageType(t.get((short) 1));//经营类型
			e.setAddress(t.get((short) 2));//地址
			e.setRepresentative(t.get((short) 3));//法人代表
			e.setOfficePhone(t.get((short) 4));//办公电话
			e.setEmail(t.get((short)5));//电子邮件
			
			//添加验证，电子邮件/注册日期/企业类型/紧急联系人/紧急联系电话/企业委托人/企业地址 等等，均可能为空。
			
			if(t.get((short)6)!=null&&!"".equals(t.get((short)6))){
				e.setRegisterDate(sdf.parse(t.get((short)6)));//注册日期
			}else{
				e.setRegisterDate(null);//注册日期
			}
			e.setEnterpriseProperty(t.get((short) 7));//企业类型
			e.setEmergencyContact(t.get((short) 8));//紧急联系人
			e.setEmergencyContactPhone(t.get((short) 9));//紧急联系电话
			e.setPrincipal(t.get((short) 10));//企业委托人
			e.setBusinessAddress(t.get((short) 11));//企业地址	
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return e;
	}





	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPageEnterpriseCustNewHelp(String companyId,EnterpriseCustNew enterpriseCustNew) {
		return new BaseDto(this.enterpriseCustNewMapper.listPageEnterpriseCustNewHelp(enterpriseCustNew), enterpriseCustNew.getPage());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPageEnterpriseByName(String companyId,EnterpriseCustNew enterpriseCustNew) {
		return new BaseDto(this.enterpriseCustNewMapper.listPageEnterpriseByName(enterpriseCustNew), enterpriseCustNew.getPage());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPageEnterprise(String companyId,EnterpriseCustNew enterpriseCustNew) {
		return new BaseDto(this.enterpriseCustNewMapper.listPageEnterprise(enterpriseCustNew), enterpriseCustNew.getPage());
	}


	
	private List<EnterpriseCustNew> addPath2AnnexAddress(List<EnterpriseCustNew> ecs){
		if(CommonUtils.isNotEmpty(ecs)){
			for(EnterpriseCustNew ec : ecs){
				if(CommonUtils.isNotEmpty(ec.getAnnexs())){
					for(Annex annex : ec.getAnnexs()){
						annex.setAnnexAddress(this.fastDFSApi.getImgBaseUri() + annex.getAnnexAddress());
					}
				}
			}
		}
		return ecs;
	}


	public BaseDto getEnterpriseCustNewByName(String companyId, EnterpriseCustNew enterpriseCustNew) {
		return new BaseDto(this.enterpriseCustNewMapper.getEnterpriseCustNewByName(enterpriseCustNew));
	}


	/**  ------------------------------------------ 分割线 -----------------------------------------------------**/


	/**
	 * 通过企业名称 ，编号地址等条件查询业主的信息 分页
	 * @param enterpriseCustNewSearch
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPageEnterpriseCustNewByCondition(String companyId,EnterpriseCustNewSearch enterpriseCustNewSearch) {
		return new BaseDto(addPath2AnnexAddress(this.enterpriseCustNewMapper.listPageEnterpriseCustNewByCondition(enterpriseCustNewSearch)), enterpriseCustNewSearch.getPage());
	}

	/**
	 * 新的导入
	 * @param companyId
	 * @param batchNo
	 * @return
	 */
	@Override
	public MessageMap importEnterpriseCust(final String companyId, String batchNo) {
		MessageMap msgMap = new MessageMap();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<EnterpriseCustNew>  allCustomerList=new ArrayList<EnterpriseCustNew>();

		List<PersonCustNew> allPersonCustNew=new ArrayList<PersonCustNew>();
		final Map<String,PersonCustNew> nameValue=new HashMap<String,PersonCustNew>();

		//采用分布式文件服务器的方式来进行文件的处理
		TSysImportExportList tSysImportExportListExist = null;
		TSysImportExportSearch condition = new TSysImportExportSearch();
		condition.setBatchNo(batchNo);
		List<TSysImportExportList> tSysImportExportListList = importExportMapper.findByCondtion(condition);
		if(CollectionUtils.isNotEmpty(tSysImportExportListList)){
			tSysImportExportListExist = tSysImportExportListList.get(0);
		}else{
			throw new ECPBusinessException("没有文件上传记录，请先上传文件");
		}

		List<Map<Short, String>> dataList = null;

		try {
			RemoteModelResult<UploadFile> remoteModelResult = fastDFSApi.loadFilePathById(tSysImportExportListExist.getUploadFileId());
			if(remoteModelResult.isSuccess()){
				UploadFile uploadFile = remoteModelResult.getModel();
				URL url = new URL(uploadFile.getPath());
				HttpURLConnection uc = (HttpURLConnection) url.openConnection();
				uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true
				uc.connect();
				dataList=new ExcelUtils().readExcelByFilePath(uc.getInputStream());
			}
		} catch (Exception e) {
			throw new ECPBusinessException("导入失败，读取文件失败："+e.getMessage());
		}


		//不符条件放入list
		final List<EnterpriseCustNew> noLoad=new ArrayList<EnterpriseCustNew>();
		if (CommonUtils.isNotEmpty(dataList) && dataList.size() > 0) {
			try {
				final List<Map<Short, String>> importList = dataList;
				if (dataList != null && dataList.size() > 0) {
					List<String> nameList=new ArrayList<String>();
					List<String> officeList=new ArrayList<String>();
					allCustomerList=enterpriseCustNewMapper.listAllEnterpriseCustNew();//获取所有企业客户
					allPersonCustNew=this.personCustNewMapper.searchAllPersonCustNewByCompanyId(companyId);
					for(PersonCustNew pn:allPersonCustNew){
						if(!nameValue.containsKey(pn.getName())){
							nameValue.put(pn.getName(), pn);
						}else{
							nameValue.remove(pn.getName());
						}
					}

					for(int i=0;i<allCustomerList.size();i++){
						nameList.add(allCustomerList.get(i).getEnterpriseName());
						officeList.add(allCustomerList.get(i).getOfficePhone());
					}
					List<Map<Short, String>>  noContains=new ArrayList<Map<Short, String>> ();
					for (int n=0;n<dataList.size();n++) {
						if(!nameList.contains(dataList.get(n).get((short) 0))
								||!officeList.contains(dataList.get(n).get((short) 4))){
							noContains.add(dataList.get(n));
						}
					}
					allCustomerList.clear();
					nameList.clear();
					officeList.clear();
					dataList.clear();
					for(int n=0;n<noContains.size();n++){
						if(!nameList.contains(noContains.get(n).get((short) 0))
								||!officeList.contains(noContains.get(n).get((short) 4))){
							nameList.add(noContains.get(n).get((short)0));
							officeList.add(noContains.get(n).get((short) 4));
							dataList.add(noContains.get(n));
						}
					}
					final String key = UUID.randomUUID().toString();
					if (dataList.size() != 0) {
						Runnable th = new Runnable() {
							@Override
							public void run() {
								int totalNum = importList.size();
								int bsnum = 0;
//									DataSourceUtil.changeDataSource(companyId);
//									System.out.println(DBContextHolder.getDBType());

								String num = enterpriseCustNewMapper.selectCustCode();// 获取最大编号
								for (Map<Short, String> t : importList) {
									final EnterpriseCustNew enterpriseCustNew = new EnterpriseCustNew();
									enterpriseCustNew.setCompanyId(companyId);
									// 企业名称
									if (!StringUtils.isEmpty(t.get((short) 0))) {
										enterpriseCustNew.setEnterpriseName(t.get((short) 0));

									} else {
										noLoad.add(setEnterpriseCustNew(t));
										continue;
									}
									// 经营类型
									if (!StringUtils.isEmpty(t.get((short) 1))) {
										enterpriseCustNew.setManageType(t.get((short) 1));
									} else {
										noLoad.add(setEnterpriseCustNew(t));
										continue;
									}
									// 注册地址
									if (!StringUtils.isEmpty(t.get((short) 2))) {
										enterpriseCustNew.setAddress(t.get((short) 2));
									}
									// 法人代表
									String representative = t.get((short) 3);
									if (!StringUtils.isEmpty(representative)) {
										if(nameValue.containsKey(representative)){
											PersonCustNew pcn=nameValue.get(representative);
											enterpriseCustNew.setRepresentative(pcn.getCustId());
										}
									} else {
										noLoad.add(setEnterpriseCustNew(t));
										continue;
									}
									// 办公电话
									if (!StringUtils.isEmpty(t.get((short) 4))) {
										enterpriseCustNew.setOfficePhone(t.get((short) 4));

									} else {
										noLoad.add(setEnterpriseCustNew(t));
										continue;
									}
									// 电子邮件
									if (!StringUtils.isEmpty(t.get((short) 5))) {
										enterpriseCustNew.setEmail(t.get((short) 5));
									}
									// 注册日期
									if (!StringUtils.isEmpty(t.get((short) 6).trim())) {
										try {
											enterpriseCustNew.setRegisterDate(sdf.parse(t.get((short) 6)));
										} catch (ParseException e) {
											e.printStackTrace();
										}
									} else {
										enterpriseCustNew.setRegisterDate(null);// 注册日期
									}
									// 企业类型
									if (!StringUtils.isEmpty(t.get((short) 7))) {
										enterpriseCustNew.setEnterpriseProperty(t.get((short) 7));
									}
									// 紧急联系人
									if (!StringUtils.isEmpty(t.get((short) 8))) {
										enterpriseCustNew.setEmergencyContact(t.get((short) 8));
									}
									// 紧急联系电话
									if (!StringUtils.isEmpty(t.get((short) 9))) {
										enterpriseCustNew.setEmergencyContactPhone(t.get((short) 9));
									}
									// 企业委托人
									if (!StringUtils.isEmpty(t.get((short) 10).trim())) {
										enterpriseCustNew.setPrincipal(t.get((short) 10));
									}
									// 企业地址
									if (!StringUtils.isEmpty(t.get((short) 11))) {
										enterpriseCustNew.setBusinessAddress(t.get((short) 11));
									}

									enterpriseCustNew.setEnterpriseId(UUID.randomUUID().toString());
									if (num == null || num.equals("")) {
										num = "10000";
									} else {
										num = String.valueOf(Integer.parseInt(num) + 1);
									}
									enterpriseCustNew.setUnitNumber(num);// 企业编号
									enterpriseCustNew.setCompanyId(companyId);// 公司id
									enterpriseCustNewMapper.insertEnterpriseCustNew(enterpriseCustNew);
									bsnum++;
									MapUtils.getMap().put(key, bsnum + "|" + totalNum);
								}
							}
						};
						Thread t = new Thread(th);
						t.start();
						msgMap.setFlag(MessageMap.INFOR_SUCCESS);
						msgMap.setMessage("正在执行企业客户的导入操作！");
						return msgMap;
					}else{
						msgMap.setFlag(MessageMap.INFOR_ERROR);
						msgMap.setMessage("导入文档未找到数据");
						return msgMap;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		msgMap.setFlag(MessageMap.INFOR_ERROR);
		msgMap.setMessage("导入失败");
		return msgMap;
	}

	/**
	 * 根据id删除企业客户基本信息
	 * @param enterpriseId
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public BaseDto deleteEnterpriseCust(String companyId, String enterpriseId) {
		MessageMap msgMap = new MessageMap();
		this.enterpriseCustNewMapper.deleteEnterpriseCustNew(enterpriseId);
		msgMap.setMessage(MessageMap.INFOR_SUCCESS);
		return new BaseDto(msgMap);
	}

	/**
	 * 修改企业客户基本信息
	 * @param enterpriseCustNew
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public BaseDto updateEnterpriseCust(WyBusinessContext ctx, EnterpriseCustNew enterpriseCustNew) {
		MessageMap msgMap = new MessageMap();
		String date = CommonUtils.getDateStr();
		String id=enterpriseCustNew.getEnterpriseId();

		String enterpriseName = enterpriseCustNew.getEnterpriseName();
		List<EnterpriseCustNew> custs = this.enterpriseCustNewMapper.findByNameAndId(enterpriseCustNew);

		if(CommonUtils.isNotEmpty(custs)){
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "该企业名已存在,无法使用"));
		}

		Integer n=enterpriseCustNewMapper.updateEnterpriseCustNew(enterpriseCustNew);
		if(n>0){

			List<Annex> annexList = enterpriseCustNew.getAnnexs();//得到传过来的文件列表
			if(CommonUtils.isNotEmpty(annexList)){
				//先删除当前用户的所有文件信息,再上传新的
				annexMapper.deleteAnnexByRelationId(id);	//删除该用户所有关联文件信息

				//新增
				for(Annex annex : annexList){
					annex.setRelationId(id);
					annex.setAnnexTime(date);
					this.annexMapper.insertAnnex(annex);
				}
			}
		}

		//如果有关系人就添加关系人
		if(enterpriseCustNew.getPersonCustRelation() != null && !"".equals(enterpriseCustNew.getPersonCustRelation())){
			PersonCustRelation personCustRelation = enterpriseCustNew.getPersonCustRelation();
			personCustRelation.setPersonRelationid(CommonUtils.getUUID());
			personCustRelation.setRelatetionId(enterpriseCustNew.getEnterpriseId());
			personCustRelation.setCreateName(ctx.getStaffName());
			personCustRelation.setCreateId(ctx.getUserId());
			this.personCustRelationMapper.insert(personCustRelation);
		}
		msgMap.setFlag(MessageMap.INFOR_SUCCESS);
		msgMap.setMessage(MessageMap.INFOR_SUCCESS);
		return new BaseDto(msgMap);
	}


	/**
	 * 添加企业客户基本信息
	 * @param enterpriseCustNew
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public BaseDto insertEnterprise(WyBusinessContext ctx, EnterpriseCustNew enterpriseCustNew){
		MessageMap msgMap = null;
		if(enterpriseCustNew!=null){

			List<EnterpriseCustNew> custs = this.enterpriseCustNewMapper.findByNameAndId(enterpriseCustNew);
			if(CommonUtils.isNotEmpty(custs)){
				return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"该企业名已经存在,请更换."));
			}

			//判断资质证编号是否被使用
			if(CommonUtils.isNotEmpty(enterpriseCustNew.getTradingNumber())){
				EnterpriseCustNew existsEc = this.enterpriseCustNewMapper.findBySomeParams(null, enterpriseCustNew.getTradingNumber());
				if(existsEc != null){
					return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "该资质证号 [" + enterpriseCustNew.getTradingNumber()  + "] 在本项目下已被 企业[ " + existsEc.getEnterpriseName() + " ]使用, 无法重复创建. "));
				}
			}


			String uuid= CommonUtils.getUUID();
			String date = CommonUtils.getDateStr();
			enterpriseCustNew.setEnterpriseId(uuid);
			// 企业编号生成
			String UnitNumber = enterpriseCustNewMapper.selectCustCode();
			if (CommonUtils.isEmpty(UnitNumber)) {
				UnitNumber = "C10000";
			} else {
				UnitNumber = String.valueOf(Integer.parseInt(UnitNumber) + 1);
				UnitNumber = 'C' + UnitNumber  ;
			}
			enterpriseCustNew.setUnitNumber(UnitNumber);//企业编号

			Integer result = enterpriseCustNewMapper.insertEnterpriseCustNew(enterpriseCustNew);
			//添加成功
			if(result>0){
				//上传文件
				List<Annex> annexs = enterpriseCustNew.getAnnexs();
				if(CommonUtils.isNotEmpty(annexs)){
					for(Annex annex :annexs){
						annex.setRelationId(uuid);
						annex.setAnnexTime(date);
						annexMapper.insertAnnex(annex);
					}
				}
			}

			//如果有关系人就添加关系人
			if(enterpriseCustNew.getPersonCustRelation() != null && !"".equals(enterpriseCustNew.getPersonCustRelation())){
				PersonCustRelation personCustRelation = enterpriseCustNew.getPersonCustRelation();
				personCustRelation.setPersonRelationid(CommonUtils.getUUID());
				personCustRelation.setRelatetionId(enterpriseCustNew.getEnterpriseId());
				personCustRelation.setCreateName(ctx.getStaffName());
				personCustRelation.setCreateId(ctx.getUserId());
				this.personCustRelationMapper.insert(personCustRelation);
			}


			msgMap = new MessageMap(MessageMap.INFOR_SUCCESS, "添加企业客户成功.");
		}else{
			throw new ECPBusinessException(ReturnCode.WY_PARAM_IS_NULL);
		}
		return new BaseDto(msgMap);
	}

}
