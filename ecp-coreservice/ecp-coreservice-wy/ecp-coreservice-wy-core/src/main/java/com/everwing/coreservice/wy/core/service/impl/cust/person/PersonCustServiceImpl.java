package com.everwing.coreservice.wy.core.service.impl.cust.person;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.ThreadPool.ThreadPoolUtils;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.constant.MqConstants;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.ExcelUtils;
import com.everwing.coreservice.common.utils.MapUtils;
import com.everwing.coreservice.common.utils.ValidatorUtil;
import com.everwing.coreservice.common.utils.generator.WyCodeGenerator;
import com.everwing.coreservice.common.wy.common.enums.RabbitMQEnum;
import com.everwing.coreservice.common.wy.common.enums.SynchrodataEnum;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.common.synchrodata.TSynchrodata;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNew;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNewSearch;
import com.everwing.coreservice.common.wy.entity.cust.person.relation.PersonCustRelation;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.common.wy.service.cust.person.PersonCustService;
import com.everwing.coreservice.platform.api.CompanyApi;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.core.utils.MqPropertiesUtil;
import com.everwing.coreservice.wy.dao.mapper.annex.AnnexMapper;
import com.everwing.coreservice.wy.dao.mapper.common.ImportExportMapper;
import com.everwing.coreservice.wy.dao.mapper.common.TSynchrodataMapper;
import com.everwing.coreservice.wy.dao.mapper.cust.person.PersonCustNewMapper;
import com.everwing.coreservice.wy.dao.mapper.cust.person.relation.PersonCustRelationMapper;
import com.everwing.coreservice.wy.dao.mapper.personbuilding.PersonBuildingNewMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


@Service("personCustService")
public class PersonCustServiceImpl implements PersonCustService{

	private static final Logger LOG = Logger.getLogger(PersonCustServiceImpl.class);

	private static final String REGEX_BIRTHDAY = "[0-9]{4}-[0-9]{2}-[0-9]{2}";

	private static final Pattern BIRTHDAY_PATTERN = Pattern.compile(REGEX_BIRTHDAY);

	@Value("${queue.personCust.key}")
	private String queue_wy2platform_personCust_key;

	@Autowired
	protected CompanyApi companyApi;

	@Autowired
	private PersonCustNewMapper personCustNewMapper;

	@Autowired
	private PersonCustRelationMapper personCustRelationMapper;

	@Autowired
	private FastDFSApi fastDFSApi;

	@Autowired
	private AnnexMapper annexMapper;

	@Autowired
	private PersonBuildingNewMapper personBuildingNewMapper;

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Autowired
	private ImportExportMapper importExportMapper;

	@Autowired
	private TSynchrodataMapper tSynchrodataMapper;



	private void addPath(List<PersonCustNew> custs){
		if(CommonUtils.isNotEmpty(custs)){
			for(PersonCustNew cust : custs){
				if(CommonUtils.isNotEmpty(cust.getAnnexs())){
					for(Annex annex : cust.getAnnexs()){
						annex.setAnnexAddress(this.fastDFSApi.getImgBaseUri() + annex.getAnnexAddress());
					}
				}
				if(CommonUtils.isNotEmpty(cust.getUploadImage())){
					cust.setUploadImage(this.fastDFSApi.getImgBaseUri() + cust.getUploadImage());
				}
			}
		}
	}

	/**
	 * 根据条件分页查询客户信息
	 * @param companyId
	 * @param personCustNew
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPagePersonCustNewBySearch(String companyId,PersonCustNewSearch personCustNew) {
		List<PersonCustNew> custs = this.personCustNewMapper.listPagePersonCustNewBySearch(personCustNew);
		addPath(custs);
		return new BaseDto(custs,personCustNew.getPage());
	}

	/**
	 * 根据id获取客户的资产绑定关系
	 * @param companyId
	 * @param custId
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto getPersonBuildingByCustId(String companyId, String custId) {
		BaseDto returnDto = new BaseDto<>();
		returnDto.setLstDto(this.personBuildingNewMapper.getPersonBuildingByCustId(custId));
		return returnDto;
	}


	/**
	 * 修改客户基本信息
	 * @param ctx
	 * @param personCustNew
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(rollbackFor=Exception.class)
	@Override
	public BaseDto updatePersonCustNewRestful(WyBusinessContext ctx,PersonCustNew personCustNew) {
		BaseDto dto = new BaseDto();
		MessageMap msgMap = new MessageMap();

		String message = personCustNew.getRegisterPhone();
		String msg = Constants.STR_EMPTY;
		String cardNum = personCustNew.getCardNum();
		boolean check = true;
		String id = personCustNew.getCustId();
		PersonCustNew param = new PersonCustNew();
		param.setCustId(personCustNew.getCustId());
		param.setCardNum(cardNum);
		int num = personCustNewMapper.getCountByNoOrPhoneById(param);
		if (num != 0) {
			msg = "证件号码已存在";
			check = false;
		} else {
			param.setCardNum(null);
			param.setRegisterPhone(personCustNew.getRegisterPhone());
			num = personCustNewMapper.getCountByNoOrPhoneById(param);
			if (num != 0) {
				check = false;
				if (CommonUtils.isNotEmpty(msg)) {
					msg = msg.concat(Constants.STR_COMMA);
				}
				msg = msg.concat("注册电话号码已存在");
			}
		}


		//如果有关系人就添加关系人

		if(personCustNew.getPersonCustRelation() != null && !"".equals(personCustNew.getPersonCustRelation())){

			// 本人与关系人一致

			if(personCustNew.getCustId().equals(personCustNew.getPersonCustRelation().getCustId())){
				msgMap = new MessageMap(MessageMap.INFOR_WARNING, "关系人添加失败,关系人就是本人");
				dto.setMessageMap(msgMap);
				msgMap.setFlag(MessageMap.INFOR_WARNING);
				return dto;
			}


			PersonCustRelation personCustRelation = personCustNew.getPersonCustRelation();
			personCustRelation.setPersonRelationid(CommonUtils.getUUID());
			personCustRelation.setRelatetionId(personCustNew.getCustId());
			personCustRelation.setCreateName(personCustNew.getCreateName());
			personCustRelation.setCreateId(personCustNew.getCreateId());
			this.personCustRelationMapper.insert(personCustRelation);
		}

		if (check) {
			personCustNew.setCardNum(cardNum);
			Integer n = personCustNewMapper.updatePersonCustNew(personCustNew);
			msg = "编辑成功";
			if (n > 0) {

				if(CommonUtils.isNotEmpty(personCustNew.getAnnexs())){
					//直接删除原文件
					annexMapper.deleteAnnexByRelationId(id);
					//然后插入
					for(Annex annex : personCustNew.getAnnexs()){
						annex.setRelationId(id);
						annex.setAnnexTime(CommonUtils.getDateStr());
						annexMapper.insertAnnex(annex);
					}
				}
				msgMap.setFlag(MessageMap.INFOR_SUCCESS);

				/**
				 * 推送到平台
				 */
				List<TSynchrodata> tSynchrodatas = new ArrayList<>(1);
				TSynchrodata tSynchrodata = new TSynchrodata();
				tSynchrodata.setCode(WyCodeGenerator.genSynchrodataCode());
				tSynchrodata.setDescription("同步tc_person_cust表");
				tSynchrodata.setTableName(SynchrodataEnum.table_tc_person_cust.getStringValue());
				tSynchrodata.setTableFieldName("cust_id");
				tSynchrodata.setTableFieldValue(personCustNew.getCustId());
				tSynchrodata.setDestinationQueue(queue_wy2platform_personCust_key);
				tSynchrodata.setOperation(RabbitMQEnum.modify.name());
				tSynchrodata.setPriorityLevel(SynchrodataEnum.priorityLevel_middle.getIntValue());
				tSynchrodata.setState(SynchrodataEnum.state_draft.getStringValue());
				tSynchrodata.setCreaterId(ctx.getUserId());
				tSynchrodata.setCreaterName(ctx.getStaffName());
				tSynchrodatas.add(tSynchrodata);
				tSynchrodataMapper.batchInsert(tSynchrodatas);

			}
		}else{
			msgMap.setFlag(MessageMap.INFOR_ERROR);
		}
		msgMap.setMessage(msg);
		dto.setMessageMap(msgMap);
		return dto;
	}


	/**
	 * 添加客户基本信息不关联建筑信息
	 * @param ctx
	 * @param personCustNew
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(rollbackFor=Exception.class)
	@Override
	public BaseDto insertPersonCustNewRestful(WyBusinessContext ctx,PersonCustNew personCustNew){
		BaseDto dto = new BaseDto();
		MessageMap msgMap = null;

		PersonCustNew pcn = new PersonCustNew();
		personCustNew.setCompanyId(ctx.getCompanyId());
		pcn.setCardNum(personCustNew.getCardNum());
		pcn.setRegisterPhone(null);
		int a = personCustNewMapper.getCountByNoOrPhoneById(pcn);

		if(a == 0){

			pcn.setCardNum(null);
			pcn.setRegisterPhone(personCustNew.getRegisterPhone());
			a = personCustNewMapper.getCountByNoOrPhoneById(pcn);

			if(a == 0){

				String id = CommonUtils.getUUID();
				personCustNew.setCustId(id);
				String num = personCustNewMapper.selectCustCode();
				if (num.equals("0")) {
					num = "P10000";
				} else {
					num = String.valueOf(Integer.parseInt(num) + 1);
					num = 'P' + num  ;
				}
				personCustNew.setCustCode(num);// 客户编号
				personCustNewMapper.insertPersonCustNew(personCustNew);




				//如果有关系人就添加关系人
				if(CommonUtils.isNotEmpty(personCustNew.getPersonCustRelation())){




					PersonCustRelation personCustRelation = personCustNew.getPersonCustRelation();
					personCustRelation.setPersonRelationid(CommonUtils.getUUID());
					personCustRelation.setRelatetionId(personCustNew.getCustId());
					personCustRelation.setCreateName(ctx.getStaffName());
					personCustRelation.setCreateId(ctx.getUserId());
					this.personCustRelationMapper.insert(personCustRelation);
				}


				if (CommonUtils.isNotEmpty(personCustNew.getAnnexs())) {
					for (Annex annex : personCustNew.getAnnexs()) {
						annex.setRelationId(id);
						annex.setAnnexTime(CommonUtils.getDateStr());
						annexMapper.insertAnnex(annex);
					}
				}

				/**
				 * 推送到平台
				 */
				List<TSynchrodata> tSynchrodatas = new ArrayList<>(1);
				TSynchrodata tSynchrodata = new TSynchrodata();
				tSynchrodata.setCode(WyCodeGenerator.genSynchrodataCode());
				tSynchrodata.setDescription("同步tc_person_cust表");
				tSynchrodata.setTableName(SynchrodataEnum.table_tc_person_cust.getStringValue());
				tSynchrodata.setTableFieldName("cust_id");
				tSynchrodata.setTableFieldValue(personCustNew.getCustId());
				tSynchrodata.setDestinationQueue(queue_wy2platform_personCust_key);
				tSynchrodata.setOperation(RabbitMQEnum.insert.name());
				tSynchrodata.setPriorityLevel(SynchrodataEnum.priorityLevel_middle.getIntValue());
				tSynchrodata.setState(SynchrodataEnum.state_draft.getStringValue());
				tSynchrodata.setCreaterId(ctx.getUserId());
				tSynchrodata.setCreaterName(ctx.getStaffName());
				tSynchrodatas.add(tSynchrodata);
				tSynchrodataMapper.batchInsert(tSynchrodatas);




				msgMap = new MessageMap(null, "添加成功");
			}else{
				msgMap = new MessageMap(MessageMap.INFOR_WARNING, "手机号码已经存在.");
			}
		}else{
			msgMap = new MessageMap(MessageMap.INFOR_WARNING,"证件号码已经被使用, 无法重复使用. ");
		}

		dto.setMessageMap(msgMap);
		return dto;
	}

	/**
	 * 根据姓名,身份证号,注册号码查询
	 * @param companyId
	 * @param personCustNew
	 * @return
	 */
	public BaseDto getPersonCustNewByName(String companyId, PersonCustNew personCustNew) {
		// 因为可能是多项查询 用, 隔开 1是姓名 2是身份证 3 是注册号码
		return new BaseDto(this.personCustNewMapper.getPersonCustNewByName(personCustNew));
	}

	@Override
	public void importPersonCust(final WyBusinessContext ctx, TSysImportExportSearch tSysImportExportRequest) {

		String companyId = ctx.getCompanyId();
		final String companyIdnew = companyId;
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		String filePath = annex.getAnnexAddress();
		final Map<String, PersonCustNew> codeMap = new HashMap<String, PersonCustNew>();
		List<PersonCustNew> allCustomerList = new ArrayList<PersonCustNew>();

		//采用分布式文件服务器的方式来进行文件的处理
		TSysImportExportList tSysImportExportListExist = null;
		TSysImportExportSearch condition = new TSysImportExportSearch();
		String batchNo = tSysImportExportRequest.getBatchNo();
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


		if (CommonUtils.isNotEmpty(dataList) && dataList.size() > 0) {

			final List<Map<Short, String>> importList=dataList;

			try {
				if (dataList != null && dataList.size() > 0) {
					allCustomerList = personCustNewMapper.listAllPersonCustNew();// 获取所有个人客户

					for (int i = 0; i < allCustomerList.size(); i++) {
						//处理身份证号和护照号不可以重复
						codeMap.put(allCustomerList.get(i).getCardNum(), allCustomerList.get(i));  //身份证号为Key , 客户对象为value
					}
					String num = personCustNewMapper.selectCustCode(); //客户编码最大值  10005
					final String key = UUID.randomUUID().toString();


					final  String cid = companyId;
					ThreadPoolUtils.getInstance().executeThread(new Runnable() {

						@Override
						public void run() {
							// 切换数据源
							WyBusinessContext.getContext().setCompanyId(cid);
							final Map<String, String> importIdCard = new HashMap<String, String>();
							int totalNum = importList.size();
							int bsnum = 0;
							int countNum = 0;
							String num = personCustNewMapper.selectCustCode(); //客户编码最大值  10005
							for (Map<Short, String> t : importList) {
								boolean check = true;
								final PersonCustNew personCustNew = new PersonCustNew();
								personCustNew.setCreateId(ctx.getUserId());
								personCustNew.setCreateName(ctx.getStaffName());
								personCustNew.setCompanyId(companyIdnew);
								// 姓名
								if (!StringUtils.isEmpty(t.get((short) 0).trim())) {
									personCustNew.setName(t.get((short) 0));
								} else {
									check = false;
									LOG.info("********************姓名为空，无法导入!***********************");
								}
								// 性别
								if (check && !StringUtils.isEmpty(t.get((short) 1).trim())) {
									if (t.get((short) 1).equals("男")) {
										personCustNew.setSex("0");
									} else {
										personCustNew.setSex("1");
									}
								} else {
									check = false;
									LOG.info("********************姓名为"+t.get((short) 0)+"  姓名为空无法导入!***********************");
								}
								// 民族
								if (check && !StringUtils.isEmpty(t.get((short) 2).trim())) {
									personCustNew.setNation(t.get((short) 2));
								}
								// 证件类型
								if (check && !StringUtils.isEmpty(t.get((short) 3).trim())) {
									if ("身份证".equals(t.get((short) 3))) {
										personCustNew.setCardType("0");
									} else {
										personCustNew.setCardType("1");// 护照
									}

								} else {
									personCustNew.setCardType("0");
//									check = false;
									LOG.info("********************姓名为"+t.get((short) 0)+"  证件为空, 默认设置为身份证!***********************");
								}

								String idCard_tmp = CommonUtils.null2String(t.get((short) 4).trim());
								if(CommonUtils.isEmpty(idCard_tmp)){
									//证件号码为空, 直接设置为UUID
									personCustNew.setCardNum(CommonUtils.getUUID());

								}else{
									if ("0".equals(personCustNew.getCardType())) {
										// 证件号码
										idCard_tmp = idCard_tmp.substring(0,idCard_tmp.length()-1);
										if (ValidatorUtil.isIdcard(idCard_tmp)) {

											if (importIdCard.containsKey(idCard_tmp)) {
												idCard_tmp = CommonUtils.getUUID();       //该身份证已经被使用, 设置为UUID()
											} else {
												importIdCard.put(idCard_tmp, idCard_tmp);
											}

											if (idCard_tmp.length() >= 18) {
												if (codeMap.containsKey(idCard_tmp)) {
													idCard_tmp = CommonUtils.getUUID();    //该身份证已经被使用, 设置为UUID
												}else {
													idCard_tmp += "";
												}
											} else if (t.get((short) 4).length() >= 15) {
												if (codeMap.containsKey(idCard_tmp)) {
													idCard_tmp = CommonUtils.getUUID();   //该身份证已经被使用, 设置为UUID
												}
											}
										} else {
											if (importIdCard.containsKey(idCard_tmp) || codeMap.containsKey(idCard_tmp) || codeMap.containsKey(idCard_tmp + "S") ){
												idCard_tmp = CommonUtils.getUUID();    //该身份证被导入或被使用, 且不符合标准格式, 设置为UUID
											}
											//身份证不符合格式, 但未被使用,直接设置
											LOG.info("********************姓名为"+t.get((short) 0)+"  证件不正确, 设置为UUID!***********************");
										}
										personCustNew.setCardNum(idCard_tmp);
									} else {
										if (codeMap.containsKey(t.get((short) 4).trim())) {
											personCustNew.setCardNum(CommonUtils.getUUID());  //该护照已经被使用, 设置为UUID
											personCustNew.setCardNum(t.get((short) 4));
										}
									}
								}

								if (check && !StringUtils.isEmpty(t.get((short) 5).trim())) {

									String birthday = CommonUtils.null2String(t.get((short) 5));
									if(!BIRTHDAY_PATTERN.matcher(birthday).matches()){
										personCustNew.setBirthday(new Date());  //生日设置为当前
									}else{
										try {
											// 出生日期
											personCustNew.setBirthday(sdf.parse(t.get((short) 5)));
										} catch (ParseException e) {
											e.printStackTrace();
										}
									}
								}
								if (check && !StringUtils.isEmpty(t.get((short) 6).trim())) {
									if ("已婚".equals(t.get((short) 6))) {// 婚姻状况
										personCustNew.setMarrieState((byte) 0);
									} else {
										personCustNew.setMarrieState((byte) 1);// 婚否
									}
								}
								if (check && !StringUtils.isEmpty(t.get((short) 7).trim())) {
									String registerPhone = t.get((short) 7).trim();
									registerPhone = registerPhone.replaceAll("\\+", "");
									if(registerPhone.startsWith("86")){
										registerPhone = registerPhone.substring(2);
									}
									personCustNew.setRegisterPhone(registerPhone);// 注册号码
								} else {
									check = false;
									LOG.info("********************姓名为"+t.get((short) 0)+"  手机号码为空无法导入!***********************");
								}

								String nativePlace = t.get((short) 8).trim();
								if (check && !StringUtils.isEmpty(nativePlace)) {
									personCustNew.setNativePlace(nativePlace);// 籍贯
								}

								// 紧急联系人电话
								if (check && !StringUtils.isEmpty(t.get((short) 10).trim().trim())) {
									personCustNew.setUrgentContactPhone(t.get((short) 10));
								}

								if (check && !StringUtils.isEmpty(t.get((short) 11).trim())) {
									personCustNew.setCensus(t.get((short) 11));// 户口所在地
								}
								if (check && !StringUtils.isEmpty(t.get((short) 12).trim())) {
									personCustNew.setEmail(t.get((short) 12));// 电子邮箱
								}
								if (check && !StringUtils.isEmpty(t.get((short) 13).trim())) {
									personCustNew.setWorkUnits(t.get((short) 13));// 工作单位
								}
								if (check && !StringUtils.isEmpty(t.get((short) 14).trim())) {
									personCustNew.setPhoneNum(t.get((short) 14));
								}

								String urgentContactPerson = t.get((short) 9).trim();
								if (check && !StringUtils.isEmpty(urgentContactPerson)) {
									personCustNew.setUrgentContactPerson(urgentContactPerson);// 紧急联系人
								}

								personCustNew.setCustId(UUID.randomUUID().toString());
								if (num == null || num.equals("")) {
									num = "P10000";
								} else {
									num = String.valueOf(Integer.parseInt(num) + 1);
									num = 'P' + num  ;
								}
								personCustNew.setCustCode(num);// 客户编号
								countNum++;
								if (check) {
									personCustNew.setCompanyId(companyIdnew);
									personCustNewMapper.insertPersonCustNew(personCustNew);
									JSONObject obj = new JSONObject();
									obj.put("opr", MqConstants.PERSON_CUST_ADD);
									obj.put("cust", personCustNew);
//									amqpTemplate.convertAndSend(ROUTE_KEY, obj);
									LOG.info(CommonUtils.log("消息队列: 导入时新增个人客户数据  , 发送消息到消息队列完成 . 数据: " + obj.toJSONString()));
									bsnum++;

								}

								MapUtils.getMap().put(key, bsnum + "|" + totalNum + "|" + countNum);
							}

						}
					});
				}
			} catch (Exception e) {
				LOG.error(e);
				LOG.info("导入个人客户异常");
				e.printStackTrace();
			}

		}


	}

	@Override
	public BaseDto queryCustAccountInfoById(String companyId, String id) {
		if(org.apache.commons.lang.StringUtils.isNotEmpty(id)){
			return new BaseDto(personCustNewMapper.queryCustAccountInfoById(id));
		}
		return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"参数错误！"));
	}

	/**
	 * 根据id删除客户基本信息
	 * @param ctx
	 * @param id
	 */
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void deletePersonCust(WyBusinessContext ctx, String id){
		/*//删除个人客户资产
		this.assetAccountMapper.deleteAssetAccountByCustId(id);

		this.accountRelationMapper.deleteAccountRelationByCustId(id);

		this.accountMapper.deleteAccountByCustId(id);
		*/


		//删除绑定资产表
		this.personBuildingNewMapper.deleteByCustId(id);

		//删除个人客户
		this.personCustNewMapper.deletePersonCustNew(id);

		/**
		 * 推送到平台
		 */
		List<TSynchrodata> tSynchrodatas = new ArrayList<>(1);
		TSynchrodata tSynchrodata = new TSynchrodata();
		tSynchrodata.setCode(WyCodeGenerator.genSynchrodataCode());
		tSynchrodata.setDescription("同步tc_person_cust表");
		tSynchrodata.setTableName(SynchrodataEnum.table_tc_person_cust.getStringValue());
		tSynchrodata.setTableFieldName("cust_id");
		tSynchrodata.setTableFieldValue(id);
		tSynchrodata.setDestinationQueue(queue_wy2platform_personCust_key);
		tSynchrodata.setOperation(RabbitMQEnum.insert.name());
		tSynchrodata.setPriorityLevel(SynchrodataEnum.priorityLevel_middle.getIntValue());
		tSynchrodata.setState(SynchrodataEnum.state_draft.getStringValue());
		tSynchrodata.setCreaterId(ctx.getUserId());
		tSynchrodata.setCreaterName(ctx.getStaffName());
		tSynchrodatas.add(tSynchrodata);
		tSynchrodataMapper.batchInsert(tSynchrodatas);


	}

}
