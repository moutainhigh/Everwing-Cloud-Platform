package com.everwing.coreservice.wy.core.service.impl.personbuilding;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.Page;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.constant.MqConstants;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.generator.WyCodeGenerator;
import com.everwing.coreservice.common.wy.common.enums.RabbitMQEnum;
import com.everwing.coreservice.common.wy.common.enums.SynchrodataEnum;
import com.everwing.coreservice.common.wy.dto.BuildingAndCustDTO;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.common.synchrodata.TSynchrodata;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.EnterpriseCustNew;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNew;
import com.everwing.coreservice.common.wy.entity.personbuilding.BindBuilding;
import com.everwing.coreservice.common.wy.entity.personbuilding.PersonBuildingImportBean;
import com.everwing.coreservice.common.wy.entity.personbuilding.PersonBuildingInfo;
import com.everwing.coreservice.common.wy.entity.personbuilding.PersonBuildingNew;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingList;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.common.wy.service.personbuilding.PersonbuildingService;
import com.everwing.coreservice.common.wy.service.sys.TSysLookupService;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.core.task.PersonBuildingImportTask;
import com.everwing.coreservice.wy.core.utils.ExcelUtils;
import com.everwing.coreservice.wy.dao.mapper.common.ImportExportMapper;
import com.everwing.coreservice.wy.dao.mapper.common.TSynchrodataMapper;
import com.everwing.coreservice.wy.dao.mapper.cust.enterprisecust.EnterpriseCustNewMapper;
import com.everwing.coreservice.wy.dao.mapper.cust.person.PersonCustNewMapper;
import com.everwing.coreservice.wy.dao.mapper.personbuilding.PersonBuildingNewMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcBuildingMapper;
import com.everwing.myexcel.definition.ExcelDefinitionReader;
import com.everwing.myexcel.factory.DefinitionFactory;
import com.everwing.myexcel.factory.xml.XMLDefinitionFactory4commonImport;
import com.everwing.myexcel.resolver.poi.POIExcelResolver4commonImport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service("personbuildingService")
public class PersonbuildingServiceImp extends POIExcelResolver4commonImport implements PersonbuildingService {
	
	private static final Logger logger = LogManager.getLogger(PersonbuildingServiceImp.class);

	@Autowired
	private PersonBuildingNewMapper personBuildingNewMapper;  
	@Autowired
	private PersonCustNewMapper personCustNewMapper;
	@Autowired
	private EnterpriseCustNewMapper enterpriseCustNewMapper; 
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Autowired
	private TSysLookupService tSysLookupService;
	
	@Autowired
	private TcBuildingMapper tcBuildingMapper;
	
	private WyBusinessContext ctx;
	
	@Autowired
	private FastDFSApi fastDFSApi;
	
	@Autowired
	private ImportExportMapper importExportMapper;

	@Autowired
	private TSynchrodataMapper tSynchrodataMapper;


	@Value("${queue.personBuilding.key}")
	private String ROUTE_KEY;
	
	
	@Override
	public MessageMap addPersonBuildingNewRestful(String companyId, PersonBuildingNew personBuildingNew) {
		List<PersonBuildingNew> personBuildingNewss=personBuildingNewMapper.getPersonBuildingNewByCustIdAndBsId_one(personBuildingNew.getCustId(), personBuildingNew.getBuildingId());
		for( PersonBuildingNew personBuildingNews : personBuildingNewss ){
			if ( personBuildingNews != null) {
				//客户与建筑已绑定
				return new MessageMap(MessageMap.INFOR_ERROR,"客户与建筑已绑定");
			}
		}    
		personBuildingNew.setPersonBuildingId(CommonUtils.getUUID());
		personBuildingNewMapper.insertPersonBuildingNew(personBuildingNew); //添加客户信息与资产关系
		return new MessageMap(null,"绑定成功！");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listAllPersonAndHouseByHouseId(String companyId, String houseId) {
		return new BaseDto(this.personBuildingNewMapper.listAllPersonAndHouseByHouseId(houseId),new Page());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listAll(String companyId) {
		return new BaseDto(this.personBuildingNewMapper.listAll());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPagePersonBuildingNew(String companyId, PersonBuildingNew personBuildingNew) {
		List<PersonBuildingNew> a = new ArrayList<PersonBuildingNew>();
		if( CommonUtils.isNotEmpty( personBuildingNew.getBuildingId() ) ){
			String[] ids = personBuildingNew.getBuildingId().split(",");
			for (String s : ids) {
				PersonBuildingNew personBuildingNew1=new PersonBuildingNew();
				personBuildingNew1.setBuildingId(s);
				List<PersonBuildingNew> personBuildingNews = personBuildingNewMapper.listPagePersonBuilding(personBuildingNew1);				
				a.addAll(personBuildingNews);
			}
		}else{
			a = personBuildingNewMapper.listPagePersonBuildingNew(personBuildingNew);
		}
		return new BaseDto(a,new Page());
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPersonBuildingNewone(String companyId, String buildingStructureId) {
		return new BaseDto(this.personBuildingNewMapper.listPersonBuildingNewByBuildingStructureIdone(buildingStructureId));
	}

	@Override
	public MessageMap addPersonBuildingEnterpriseNewRestful(String companyId, PersonBuildingNew personBuildingNew) {
//		PersonBuildingNew personBuildingNews=personBuildingNewMapper.getPersonBuildingNewByEnterpriseIdAndBsId(personBuildingNew.getEnterpriseId(), personBuildingNew.getBuildingId());
//	    if (personBuildingNews!=null&&!personBuildingNews.equals("")) {  //客户与建筑已绑定
//	    	return "";
//		}else{
//			//客户与建筑没有绑定时	
//			personBuildingNewMapper.insertPersonBuildingNew(personBuildingNew); //添加客户信息与资产关系
//			BuildingStructureNew buildingStructure_check=buildingStructureNewMapper.getBuildingStructureNewbyId(personBuildingNew.getBuildingId());
//			if(buildingStructure_check.getIsBindingAssets()==0){
//				buildingStructureNewMapper.updateBuildingStructureById(personBuildingNew.getBuildingId());
//			}
//			return "true";
//		}
		return null;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto getRelationOfemplers(String companyId, PersonBuildingNew personBuildingNew) {
		List<PersonBuildingNew> personBuildingNewone = new ArrayList<PersonBuildingNew>();
		if(CommonUtils.isEmpty(personBuildingNew.getEnterpriseId())){
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR, "传入企业客户id为空"));
		}
		String[] ids = personBuildingNew.getEnterpriseId().split(",");
		if(CommonUtils.isNotEmpty(ids)){
			for(String cr : ids ){
				personBuildingNew.setEnterpriseId(cr);
				List<PersonBuildingNew> personBuildingNews =personBuildingNewMapper.getRelationOfemplers(personBuildingNew);
				if(personBuildingNews != null && personBuildingNews.size() > 0){
					for(PersonBuildingNew personBuilding:personBuildingNews){
						if (personBuilding.getCustType().equals("员工")) {
							personBuildingNewone.add(personBuilding);
						}
					}
				}
			}
		}
		return new BaseDto(personBuildingNewone);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto selectPersonBuildingNewByCustId(String companyId, PersonBuildingNew personBuildingNew) {
		List<PersonBuildingNew> list=personBuildingNewMapper.selectPersonBuildingNewByCustId(personBuildingNew);
		return new BaseDto(list);
	}
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public BaseDto getRelationOfemplersByCustId(String companyId, String custId) {/*
		PersonBuildingNew personBuildingNew=new PersonBuildingNew();
		
		personBuildingNew.setCustId(custId);
		
		List<PersonBuildingNew> personBuildingNewAll=new ArrayList<PersonBuildingNew>();
		
		List<PersonBuildingNew> personBuildingNews=personBuildingNewMapper.listAllPersonBuildingNewone(personBuildingNew);
		
		for(PersonBuildingNew personBuilding : personBuildingNews){
		
			String buildingStructureId = personBuilding.getBuildingId();
			
			String custType = personBuilding.getCustType();
			
			if( buildingStructureId != null && custType != null ){
				
				if( custType.equals("业主") ){
					PersonBuildingNew personBuildingNewone=new PersonBuildingNew();
					 
					personBuildingNewone.setBuildingId(buildingStructureId);
					
					List<PersonBuildingNew> personBuildingNewsone=personBuildingNewMapper.getRelationOfemplersByStructs(personBuildingNewone);
					
					for(PersonBuildingNew personBuildingone:personBuildingNewsone){
						if( personBuildingone.getCustType().equals("业主") && personBuildingone.getCustType().equals("员工") ){
							personBuildingNewAll.add(personBuildingone);
						}
					}	
				}
				else if( custType.equals("员工") ){
					List<PersonBuildingNew> personBuildingNewsone=personBuildingNewMapper.getRelationOfemplersByStructs(personBuilding);	
					
					for(PersonBuildingNew personBuildingone:personBuildingNewsone){
						if( personBuildingone.getCustType().equals("员工") ){
							personBuildingNewAll.add(personBuildingone);
						}
					}	
				}
				else{
					PersonBuildingNew personBuildingNewone=new PersonBuildingNew();
					personBuildingNewone.setPersonBuildingId(personBuilding.getPersonBuildingId());
					List<PersonBuildingNew> personBuildingNewsone=personBuildingNewMapper.getRelationOfemplersByStructs(personBuildingNewone);
					personBuildingNewone.setCustType("业主");
					personBuildingNewone.setBuildingId(buildingStructureId);
					personBuildingNewone.setPersonBuildingId("");
					List<PersonBuildingNew> ownerName=personBuildingNewMapper.getRelationOfemplersByStructs(personBuildingNewone);
					
					for( PersonBuildingNew personBuildingone:personBuildingNewsone ){
						for(PersonBuildingNew personBuildingtwo:ownerName){
//							PersonBuildingNew a=new PersonBuildingNew();
							PersonBuildingNew person_building= (PersonBuildingNew)personBuildingone.clone();
							System.out.print(person_building==personBuildingone);
							person_building.setName(personBuildingtwo.getName());
							person_building.setPersonId(personBuildingtwo.getPersonId());
							personBuildingNewAll.add(person_building);
						}
					}
				}
			}
		}
		return new BaseDto(personBuildingNewAll);*/
		return new BaseDto();
	}

	@Override
	public MessageMap updataPersonBuildingById(String companyId, PersonBuildingNew personBuildingNew) {
		int num=personBuildingNewMapper.updataPersonBuildingById(personBuildingNew);
		if(num>0) return new MessageMap(MessageMap.INFOR_SUCCESS,"修改成功");
		else return new MessageMap(MessageMap.INFOR_ERROR,"修改失败");
	}

	@Override
	public MessageMap deletePersonBuildingById(String companyId, List<PersonBuildingNew> personBuildingNews) {
		if(CommonUtils.isEmpty(personBuildingNews)){
			return new MessageMap(MessageMap.INFOR_ERROR, "参数为空,删除失败.");
		}
		for( PersonBuildingNew entity : personBuildingNews){
			personBuildingNewMapper.updataPersonBuildingById(entity);
		}
		return new MessageMap(MessageMap.INFOR_SUCCESS,"删除成功");
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listBudildingByCustId(String companyId,String custId) {
		return new BaseDto(personBuildingNewMapper.listBudildingByCustId(custId),null);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto getRelationBycustId(String companyId, String custId) {
		return new BaseDto(this.personBuildingNewMapper.getRelationBycustId(custId));
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPageHouseByenterpriseId(String companyId, PersonBuildingNew personBuildingNew) {
		List<PersonBuildingNew> personBuildingNews =personBuildingNewMapper.listPageHouseByenterpriseId(personBuildingNew);
		return new BaseDto(personBuildingNews,personBuildingNew.getPage());
	}

	@Override
	public MessageMap deletePersonBuilding(String companyId, PersonBuildingNew personBuildingNew) {
		if(CommonUtils.isEmpty(personBuildingNew.getPersonBuildingId())){
			return new MessageMap(MessageMap.INFOR_ERROR,"传入参数为空,删除失败.");
		}
			
		String[] ids = personBuildingNew.getPersonBuildingId().split(Constants.STR_COMMA);
		
		for(String personBuildingId : ids){
			personBuildingNewMapper.deletePersonBuilding(personBuildingId);
		}
		return new MessageMap(MessageMap.INFOR_SUCCESS,"操作成功");
	}

	
	@Override
	public MessageMap importFile(String companyId, Annex annex) {
		String filePath = annex.getAnnexAddress();
		int insertNum = 0;
		int repeatNum = 0;
		if(CommonUtils.isNotEmpty(filePath))
			try {
					if(filePath.startsWith(File.separator)){
						filePath=filePath.substring(1);
					}
					List<Map<Short, String>> dataList=null;
					if(filePath.endsWith(".xlsx")){
						dataList = new ExcelUtils().readExcelXlsx(filePath);
					}else if(filePath.endsWith(".xls")){
						dataList = new ExcelUtils().readExcel(filePath);
					}
				//测试 0表示之间id 1表示客户类型  2表示证件号   3表示姓名
				Map<String,String> personBuildingMap = new HashMap<String,String>();//客户资产绑定Map，用于去重
				Map<String,String> custMap = new HashMap<String,String>();//个人客户与企业客户Map，用于判断是否存在该记录
				
				//项目下的所有 业主-建筑 关系
				List<PersonBuildingNew> oldPersonBuildingList = personBuildingNewMapper.getPersonBuildingHasMoreByProjectId(annex.getProjectId());
				//所有个人客户数据
				List<PersonCustNew> oldPersonCustList = personCustNewMapper.listAllPersonCustNew();
				//所有企业客户数据
				List<EnterpriseCustNew> oldEnterpriseCustList = enterpriseCustNewMapper.listAllEnterpriseCustNew();
				
				//获取数据库中的客户建筑关联信息和客户的信息，设置key、value并保存到map中
				for (PersonBuildingNew personBuildingNew : oldPersonBuildingList) {
					PersonCustNew personCustNew = personBuildingNew.getPersonCustNew();
					EnterpriseCustNew enterpriseCustNew = personBuildingNew.getEnterpriseCustNew();
					String custKey = "";
					String pbKey = "";
					//String custId="";
					if(CommonUtils.isEmpty(personBuildingNew.getCustId())){
//					if(StringUtils.isEmpty(personBuildingNew.getCustId())){
						custKey = "企业客户"+enterpriseCustNew.getTradingNumber();
					}else{
						custKey = "个人客户"+personCustNew.getCardNum();
					}
					pbKey = personBuildingNew.getBuildingId()+custKey;
					personBuildingMap.put(pbKey, personBuildingNew.getBuildingId());
				}
				
				for (PersonCustNew personCustNew : oldPersonCustList) {
					String pcKey = "个人客户"+personCustNew.getCardNum();
					custMap.put(pcKey, personCustNew.getCustId());
				}
				for (EnterpriseCustNew enterpriseCustNew : oldEnterpriseCustList) {
					if(CommonUtils.isNotEmpty(enterpriseCustNew.getTradingNumber())){
//					if(StringUtils.isNotEmpty(enterpriseCustNew.getTradingNumber())){
						String pcKey = "企业客户"+enterpriseCustNew.getTradingNumber();
						custMap.put(pcKey, enterpriseCustNew.getEnterpriseId());
					}
				}
				List<PersonBuildingNew> insertList = new ArrayList<PersonBuildingNew>();
				if(dataList!=null && dataList.size()>0){
					for (Map<Short, String> t : dataList) {
						Map<String,String> singleMap = new HashMap<String,String>();
						
						String isBuilding = t.get((short)4);//是否为建筑
						if(!"false".equals(isBuilding)){
							continue;
						}
						
						//获取excel列的基本信息
						String buildingStructureId = t.get((short)0);										//建筑结构编号
						String custType = t.get((short)8);													//户主类型
						String cardNum = t.get((short)9).length() == 18?t.get((short)9)+"S":t.get((short)9);//户主证件号码或者公司营业执照号
						
						String appendCustType = t.get((short)10);//追加客户类型
						String appendCardNum = t.get((short)11);//追加客户证件号码
						//判断excel里面的数据是否重复
						if(CommonUtils.isNotEmpty(appendCustType) && CommonUtils.isNotEmpty(appendCardNum)){
//							buildingStructureNewMapper.updateBuildingStructureById(buildingStructureId);   //修改is_binding_assets标识 为1   表示已经绑定
							String[] appendCustTypeArr = appendCustType.split(",");   // 
							String[] appendCardNumArr = appendCardNum.split(",");
							//判断追加的客户信息和户主的是否有冲突
							for (int i = 0; i < appendCardNumArr.length; i++) {
								String custypeArrStr = appendCustTypeArr.length==1?appendCustTypeArr[0]:appendCustTypeArr[i];
								String cardNumArrStr = appendCardNumArr[i].length() == 18?appendCardNumArr[i]+"S":appendCardNumArr[i];
								if(custType.equals(custypeArrStr) && cardNum.equals(cardNumArrStr)){
									continue;
								}else{
									//判断后面的追加客户信息是否有重复，只取一条
									String sKey = custypeArrStr+"-"+cardNumArrStr;
									if(!singleMap.containsKey(sKey)){
										singleMap.put(sKey,cardNumArrStr);
									}
								}
							}
						}
						
						if(singleMap.size() > 0){
							for (String sKey : singleMap.keySet()) {
								String[] sKeyArr = sKey.split("-");
								String custKey = sKeyArr[0]+sKeyArr[1];
								String existKey = buildingStructureId+custKey;
								//不存在该建筑,且该客户存在
								if(!personBuildingMap.containsKey(existKey) && custMap.containsKey(custKey)){
									PersonBuildingNew personBuildingNew = new PersonBuildingNew();
									String custId = custMap.get(custKey);
									String personBuildingId = UUID.randomUUID().toString();
									
									personBuildingNew.setPersonBuildingId(personBuildingId);
									personBuildingNew.setBuildingId(buildingStructureId);
									personBuildingNew.setCustType("业主");
									if("个人客户".equals(sKeyArr[0])){
										personBuildingNew.setCustId(custId);
										
										//判断是否有个人账户和对应的房屋、车位资产账户
										
									}else{
										personBuildingNew.setEnterpriseId(custId);
									}
									insertList.add(personBuildingNew);
								}else{
									repeatNum++;
								}
							}
						}
					}
				}
				if(insertList.size() > 0){
					insertNum = personBuildingNewMapper.insertList(insertList);
				}
				return new MessageMap(MessageMap.INFOR_SUCCESS,"成功导入" +insertNum + "条记录，不合法记录"+repeatNum+"条");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new MessageMap(MessageMap.INFOR_ERROR,"数据导入失败!");
		}

	
	@Override
	public MessageMap inportOtherExcel(String companyId, Annex annex) {
		String filePath = annex.getAnnexAddress();
		if(CommonUtils.isNotEmpty(filePath))
			try {
				//1. 获取excel导入的数据
				if(filePath.startsWith(File.separator)){
					filePath=filePath.substring(1);
				}
				List<Map<Short, String>> dataList=null;
				if(filePath.endsWith(".xlsx")){
					dataList = new ExcelUtils().readExcelXlsx(filePath);
				}else if(filePath.endsWith(".xls")){
					dataList = new ExcelUtils().readExcel(filePath);
				}
				
				//2. 加载所有的个人客户数据, 项目下所有的  业主建筑绑定关系 , 以及建筑数据
				
				//项目下的所有 业主-建筑 关系   用以去除已经绑定过的关系
				List<PersonBuildingNew> oldPersonBuildingList = personBuildingNewMapper.getPersonBuildingHasMoreByProjectId(annex.getProjectId());
				
				//企业客户也是由个人客户数据而来 , 所以此处只绑定个人客户-建筑关系即可
				//所有个人客户数据
				List<PersonCustNew> oldPersonCustList = personCustNewMapper.listAllPersonCustNew();
				
				//建筑数据 , 该项目下
//				List<BuildingStructureNew> buildings = this.buildingStructureNewMapper.getBuildingStructureNewbyProjectId(annex.getProjectId());
						
				
				//对个人客户数据进行去重
				Map<String,String> noRepeatPerson = new HashMap<String, String>();
				if(CommonUtils.isNotEmpty(oldPersonCustList)){
					for(PersonCustNew cust : oldPersonCustList){
						noRepeatPerson.put(cust.getName() + "_" + cust.getCardNum(), cust.getCustId());
					}
				}
				
				//去重后的建筑关系
				Map<String,String> noRepeatBuilding = new HashMap<String, String>();
//				if(CommonUtils.isNotEmpty(buildings)){
//					for(BuildingStructureNew b : buildings){
//						noRepeatBuilding.put(b.getFullName(), b.getId());
//					}
//				}
				
				//已存在的资产绑定关系
				Map<String,String> existsPersonBuildingMap = new HashMap<String, String>();
				if(CommonUtils.isNotEmpty(oldPersonCustList)){
					for(PersonBuildingNew pb : oldPersonBuildingList){
						existsPersonBuildingMap.put(pb.getCustId() + "_" + pb.getBuildingId(), null);
					}
				}
				
				
				//3.1 对Excel内读取的数据进行循环比对
					//当前数据格式  :     客户名		客户身份证号	房屋节点名		房屋全名
				int insertNum = 0;	//可插入数据
				int repeatNum = 0;	//重复数据
				List<PersonBuildingNew> insertBuildingNews = new ArrayList<PersonBuildingNew>();
				
				if(CommonUtils.isNotEmpty(dataList)){
					for(Map<Short, String> t : dataList){
						//获取数据
						String custName = t.get((short)0);
						String custCardNum = t.get((short)1);
						if(!custCardNum.endsWith("S") || !custCardNum.endsWith("s")){
							custCardNum = CommonUtils.null2String(custCardNum).concat("S");
						}
//						String nodeName = t.get((short)2);//从未使用到的参数
						String fullName = t.get((short)3);
						
						//比对个人客户集合,是否存在,不存在则为非法记录 repeatNum++
						if(!noRepeatPerson.containsKey(custName  + "_" + custCardNum)){
							repeatNum ++;
							continue;
						}
						String custId = noRepeatPerson.get(custName  + "_" + custCardNum);	//获取客户id
						
						//比对建筑集合,是否存在,不存在则视为非法记录  repeatNum++
						if(!noRepeatBuilding.containsKey(fullName)){
							repeatNum ++;
							continue;
						}
						String buildingStructureId = noRepeatBuilding.get(fullName);	//获取建筑id
						
						//个人客户中存在, 建筑集合中也存在,  比对是否已经含有当前的业主建筑绑定关系 , 若有,则视为非法记录 ,repeatNum++
						if(existsPersonBuildingMap.containsKey(custId + "_" + buildingStructureId)){
							repeatNum ++ ;
							continue;
						}
						
						//满足上述三个条件之后,构建集合,进行插入
						PersonBuildingNew insertObj = new PersonBuildingNew();
						insertObj.setPersonBuildingId(CommonUtils.getUUID());
						insertObj.setCustId(custId);
						insertObj.setBuildingId(buildingStructureId);
						insertObj.setCustType("业主");
						insertObj.setState((byte)0);
						insertObj.setRelationDate(new Date());
						
						insertBuildingNews.add(insertObj);
						
					}
				}
					
				if(CommonUtils.isNotEmpty(insertBuildingNews)){
					insertNum = this.personBuildingNewMapper.insertList(insertBuildingNews);
				}
				
				return new MessageMap(MessageMap.INFOR_SUCCESS,"成功导入" +insertNum + "条记录，不合法记录"+repeatNum+"条");
			} catch (Exception e) {
				e.printStackTrace();
			}
		return new MessageMap(MessageMap.INFOR_ERROR,"数据导入失败");
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto getInfosByBuildingId(String companyId, String buildingId) {
		return new BaseDto(this.personBuildingNewMapper.getInfosByBuildingId(buildingId));
	}

	@Override
	public MessageMap assetBinding(WyBusinessContext ctx, List<PersonBuildingNew> personBuildingNews) {
		if(CommonUtils.isEmpty(personBuildingNews)){
			return new MessageMap(null, "绑定数据为空.");
		}
		
		List<PersonBuildingNew> existsPbs = this.personBuildingNewMapper.isExists(personBuildingNews.get(0));
		List<PersonBuildingNew> addList = new ArrayList<PersonBuildingNew>();
		
		for(PersonBuildingNew pb : personBuildingNews){
			boolean flag = true;
			for(PersonBuildingNew existsPb : existsPbs){
				if((CommonUtils.isEquals(pb.getCustId(), existsPb.getCustId()) || CommonUtils.isEquals(pb.getEnterpriseId(), existsPb.getEnterpriseId()))
						&& CommonUtils.isEquals(pb.getBuildingId(), existsPb.getBuildingId())){
					flag = false;
					continue;
				}
			}
			if(flag)
				addList.add(pb);
		}

		//1. 数据入库
		if(CommonUtils.isEmpty(personBuildingNews.get(0).getEnterpriseId())){
			//个人用户
			for(PersonBuildingNew pb : addList){
				//1.1  判断是否已存在绑定关系
				if(null != pb){
					//1.2  未绑定的需要绑定到tc_person_building
					String personBuildingId = CommonUtils.getUUID();
					pb.setPersonBuildingId(personBuildingId);
					pb.setState((byte) 0);
					pb.setRelationDate(new Date());
					this.personBuildingNewMapper.insertPersonBuildingNew(pb);
					/**
					 * 推送到平台
					 */
					List<TSynchrodata> tSynchrodatas = new ArrayList<>(1);
					TSynchrodata tSynchrodata = new TSynchrodata();
					tSynchrodata.setCode(WyCodeGenerator.genSynchrodataCode());
					tSynchrodata.setDescription("同步person_building表");
					tSynchrodata.setTableName(SynchrodataEnum.table_tc_person_building.getStringValue());
					tSynchrodata.setTableFieldName("person_building_id");
					tSynchrodata.setTableFieldValue(personBuildingId);
					tSynchrodata.setDestinationQueue(ROUTE_KEY);
					tSynchrodata.setOperation(RabbitMQEnum.insert.name());
					tSynchrodata.setPriorityLevel(SynchrodataEnum.priorityLevel_middle.getIntValue());
					tSynchrodata.setState(SynchrodataEnum.state_draft.getStringValue());
					tSynchrodata.setCreaterId(ctx.getUserId());
					tSynchrodata.setCreaterName(ctx.getStaffName());
					tSynchrodatas.add(tSynchrodata);
					tSynchrodataMapper.batchInsert(tSynchrodatas);
				}
			}
		}else{
			//企业用户
			//直接检测,然后保存
			for(PersonBuildingNew pb : addList){
				if(null != pb){
					String personBuildingId = CommonUtils.getUUID();
					pb.setPersonBuildingId(personBuildingId);
					this.personBuildingNewMapper.insertPersonBuildingNew(pb);

					/**
					 * 推送到平台
					 */
					List<TSynchrodata> tSynchrodatas = new ArrayList<>(1);
					TSynchrodata tSynchrodata = new TSynchrodata();
					tSynchrodata.setCode(WyCodeGenerator.genSynchrodataCode());
					tSynchrodata.setDescription("同步person_building表");
					tSynchrodata.setTableName(SynchrodataEnum.table_tc_person_building.getStringValue());
					tSynchrodata.setTableFieldName("person_building_id");
					tSynchrodata.setTableFieldValue(personBuildingId);
					tSynchrodata.setDestinationQueue(ROUTE_KEY);
					tSynchrodata.setOperation(RabbitMQEnum.insert.name());
					tSynchrodata.setPriorityLevel(SynchrodataEnum.priorityLevel_middle.getIntValue());
					tSynchrodata.setState(SynchrodataEnum.state_draft.getStringValue());
					tSynchrodata.setCreaterId(ctx.getUserId());
					tSynchrodata.setCreaterName(ctx.getStaffName());
					tSynchrodatas.add(tSynchrodata);
					tSynchrodataMapper.batchInsert(tSynchrodatas);
				}
			}
		}




//		JSONObject jsonObj = new JSONObject();
//		jsonObj.put("opr", MqConstants.PERSON_BUILDING_BATCH_ADD);
//		jsonObj.put("content",addList);
//		this.amqpTemplate.convertAndSend(ROUTE_KEY, jsonObj);
//		logger.info("消息队列: 批量绑定房屋/业主关联关系, 发送消息到消息队列完成 . 数据: {}. " , jsonObj.toJSONString());
		return new MessageMap(MessageMap.INFOR_SUCCESS,"绑定成功");

	}

	@Override
	public MessageMap relieveAssetBinding(String companyId, List<PersonBuildingNew> personBuildingNews) {
		if(CommonUtils.isEmpty(personBuildingNews)){
			return new MessageMap(null, "解绑数据为空.");
		}
		
		String buildingId = personBuildingNews.get(0).getBuildingId();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("buildingId", buildingId);
		paramMap.put("buildingList", personBuildingNews);
		if(CommonUtils.isEmpty(personBuildingNews.get(0).getEnterpriseId())){
			//解绑个人客户
			//1. 删除tc_person_building
			paramMap.put("flag", "1");
			this.personBuildingNewMapper.deleteBatch(paramMap);
		}else{
			//解绑企业客户
			//1. 删除tc_person_building
			paramMap.put("flag", "2");
			this.personBuildingNewMapper.deleteBatch(paramMap);
		}
		JSONObject obj = new JSONObject();
		obj.put("opr", MqConstants.PERSON_BUILDING_BATCH_DEL);
		obj.put("content", paramMap);
		this.amqpTemplate.convertAndSend(ROUTE_KEY, obj);
		logger.info("消息队列: 批量删除房屋/业主关联关系, 发送消息到消息队列完成 . 数据: {}. " , obj.toJSONString());

		
		return new MessageMap(MessageMap.INFOR_SUCCESS, "解绑成功");
	}

	
	
	
	@Override
	public MessageMap exportBuildingInfos(String companyId, String isBindingAssets, BindBuilding bindBuilding) {
		//isBindingAssets  =  isBindingAssets + ","  +num;
		/*String [] params=isBindingAssets.split(",");
		if(CommonUtils.isEmpty(params)){
			return new MessageMap(MessageMap.INFOR_ERROR,"参数异常");
		}
		String isBindingAsset=params[0];
		String num=params[1];
		
		if("3".equals(num)){
//			return exportOtherEnvTabTop();
		}
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("projectId", bindBuilding.getProjectId());
		paramMap.put("fullName", bindBuilding.getBuildingFullName());//全名
		paramMap.put("name", bindBuilding.getCustName());//业主姓名
		paramMap.put("enterpriseName", bindBuilding.getEnterpriseName());
		paramMap = CommonUtils.changeValues(paramMap);
		
		List<BindBuilding> results = null;
		if ("2".equals(isBindingAsset)) {
			//查询未绑定
			results = this.personBuildingNewMapper.getNotBindBuildingInfos(paramMap);
			
		}else if("1".equals(isBindingAsset)){
			//查询已绑定
			results = this.personBuildingNewMapper.getBindedBuildingInfos(paramMap);
			
		}else if("0".equals(isBindingAsset)){
			//查询全部
			results = new ArrayList<BindBuilding>();
//			if(CommonUtils.isEmpty(paramMap.get("name")) && CommonUtils.isEmpty(paramMap.get("enterpriseName"))){
				results.addAll(this.personBuildingNewMapper.getNotBindBuildingInfos(paramMap));
				results.addAll(this.personBuildingNewMapper.getBindedBuildingInfos(paramMap));
//			}else{
//				//同时存在个人用户姓名与企业名称
//				results.addAll(this.personBuildingNewMapper.getBindedBuildingAndCustInfos(paramMap));
//			}
		}else{
			return new MessageMap(MessageMap.INFOR_ERROR,"参数异常");
		}
		
		//对查询出的数据注入到List<Node>中
		List<Node> nodes = new ArrayList<Node>();
		boolean flag ;
		StringBuffer cardNumsBuffer = new StringBuffer();
		StringBuffer cardTypeBuffer = new StringBuffer();
		StringBuffer namesBuffer = new StringBuffer();
//		StringBuffer fulleNameBuffer = new StringBuffer();
		for(BindBuilding building : results){
			flag = false;
			cardNumsBuffer.delete(0, cardNumsBuffer.length());
			cardTypeBuffer.delete(0, cardTypeBuffer.length());
			namesBuffer.delete(0, namesBuffer.length());
			for(Node node : nodes){
				if(node.getId().equals(building.getBuildingId())){
					flag = true;
					if(node.getCardNums().endsWith("S")){
						node.setCardNums(node.getCardNums().substring(0,node.getCardNums().length()-1));
					}
					cardNumsBuffer.append(node.getCardNums()).append(",");
					cardTypeBuffer.append(node.getCardTypes()).append(",");
					namesBuffer.append(node.getNames());
					//当前为企业用户  企业用户的证件号怎么拿到 原逻辑是不需要
					if(null != building.getPersonCust()){
						//当前为个人用户
						String cardNum = "";
						if(CommonUtils.null2String(building.getPersonCust().getCardNum()).endsWith("S")){
							cardNum = building.getPersonCust().getCardNum().substring(0,building.getPersonCust().getCardNum().length()-1);
						}
						cardNumsBuffer.append(cardNum);
						if("0".equals(building.getPersonCust().getCardType()))
							cardTypeBuffer.append("身份证");
						else
							cardTypeBuffer.append("护照");
						namesBuffer.append(",").append(building.getPersonCust().getName());
					}else{
						//当前为企业用户
						EnterpriseCustNew cust = building.getEnterpriseCust();
						if(CommonUtils.isNotEmpty(cust)){
							namesBuffer.append(",").append(cust.getEnterpriseName());
						}
					}
					
					//TODO
					node.setCardNums(cardNumsBuffer.toString());
					node.setCardTypes(cardTypeBuffer.toString());
					node.setNames(namesBuffer.toString());
					break;
				}
			}
			
			if(!flag){
				//不存在,直接创建新Node
				Node node = new Node();
				node.setId(building.getBuildingId());
				node.setBuildingCode(building.getBuildingCode());
				node.setUuid(CommonUtils.getUUID());
				node.setBuildingArea(CommonUtils.null2String(building.getBuildingArea()));	//建筑面积
				node.setBuildingCertificate(null); 	//房产证号
				node.setBuildingType(building.getBuildingType()); //建筑类型
				if(null != building.getPersonCust()){
					node.setCardNums(building.getPersonCust().getCardNum());
					if("0".equals(building.getPersonCust().getCardType()))
						node.setCardTypes("身份证");
					else
						node.setCardTypes("护照");
					node.setNames(building.getPersonCust().getName());
					node.setCustType("个人客户");
				}else if(null != building.getEnterpriseCust()){
					
					EnterpriseCustNew cust = building.getEnterpriseCust();
					node.setNames(CommonUtils.isEmpty(cust)? "" : cust.getEnterpriseName());
					node.setCustType("企业客户");
				}
				
				node.setNodeName(building.getBuildingName());
				node.setProjectId(building.getProjectId());
				node.setParentId(building.getpId());
				node.setIsParent("false");
				
				for(BindBuilding obj : results){
					if(node.getBuildingCode().equals(obj.getpId())){
						node.setIsParent("true");
						break;
					}
				}
				
				node.setFullName(building.getBuildingFullName());
				nodes.add(node);
			}
		}
		
		Excel ex = new Excel();
		List<ExcelInfo> list = new ArrayList<ExcelInfo>();
		ExcelInfo exl = new ExcelInfo();
		String[] headers;
		String[] fields = null;

		if ("2".equals(num)) {
			fields = new String[]{ "id", "fullName", "nodeName", "buildingType", "isParent", "buildingArea",
					"buildingCertificate", "names","custType","cardNums" };
			headers = new String[] { "建筑结构编号", "建筑全名称", "建筑节点名称", "建筑类型", "是否为单位建筑(true:是 ,false:不是)", "建筑面积", "房产证号",
					"户主", "户主类型", "户主证件号", "追加客户类型(*个人客户,企业客户)", "证件号(*请注意改成文本)", "追加客户姓名(*)" };
			exl.setTitles(headers);
		}

		if ("1".equals(num)) {
			fields = new String[]{ "id", "fullName", "nodeName", "buildingType", "isParent", "buildingArea",
					"buildingCertificate", "names" };
			headers = new String[] { "建筑结构编号", "建筑全名称", "建筑节点名称", "建筑类型", "是否为单位建筑", "建筑面积", "房产证号", "户主" };
			exl.setTitles(headers);
		}
		if (CommonUtils.isNotEmpty(nodes)) {
			exl.setFields(fields);
			exl.setSheetName("建筑导表导出文件");
			exl.setList(nodes);
			list.add(exl);
		}
		byte [] is = new byte[10];
		*//*try {
			is = ex.createExcelByte(list);
		} catch (exception e) {
			e.printStackTrace();
			is = null;
		}*//*
		
		File file=new File("D:\\abc.xlsx");
		if (!file.exists()) {
		    try {
				file.createNewFile();
				FileOutputStream  fop = new FileOutputStream(file);
				fop.write(is);
				fop.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   }*/
		
		
		return new MessageMap(MessageMap.INFOR_SUCCESS,"");
//		return com.flf.util.JsonUtil.success(com.flf.entity.ExcelEntity.addValue(is), true);
	}
	

	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto gteBuildingByPersonId(String companyId, String personId) {
		return null;
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto getHouseNewByHouseId(String companyId, String houseId) {
		return null;
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto getSipByStuctureId(String companyId, String buildingStructureId) {
		return null;
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto getPersonIdByBuildingStru(String companyId, String buildingStruId) {
		return null;
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto getPersonBuildingByCustId(String companyId, String custId) {
		return null;
	}

	@Override
	public int getPersonBuildingCountByCustId(String companyId, String custId) {
		return 0;
	}

	@Override
	public String getSipsByStuctureId(String companyId, String buildingStructureId) {
		return null;
	}

	@Override
	public String getPersonBuildingByCustIdList(String companyId, String ids) {
		return null;
	}

	@Override
	public String gteBuildingByPersonIdList(String companyId, String ids) {
		return null;
	}

	@Override
	public int getPersonBuildingCountByCustIdList(String companyId, String ids) {
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseDto loadBuildingInfos(String companyId,String projectId) {
		long startTime = System.currentTimeMillis();
		List<PersonBuildingInfo> infos = this.personBuildingNewMapper.loadBuildingInfos(projectId); 
		BaseDto returnDto = new BaseDto();
		if(CommonUtils.isEmpty(infos)){
			returnDto.setMessageMap(new MessageMap(MessageMap.INFOR_WARNING,"未找到建筑."));
		}else{
			returnDto.setLstDto(infos);
			returnDto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功"));
		}
		logger.info("时间: = " + (System.currentTimeMillis() - startTime));
		return returnDto;
	}

	@Override
	public MessageMap importFile(WyBusinessContext ctx, String batchNo,String excelPath, String projectId) {
		

        MessageMap mm = new MessageMap();

        this.ctx = ctx;
        //采用分布式文件服务器方式来做
        //通过batchNo查询uploadFileId信息作为参数传递给文件服务器
        TSysImportExportList tSysImportExportListExist = null;
        TSysImportExportSearch condition = new TSysImportExportSearch();
        condition.setBatchNo(batchNo);
        List<TSysImportExportList> tSysImportExportListList = importExportMapper.findByCondtion(condition);
        if(CommonUtils.isNotEmpty(tSysImportExportListList)){
            tSysImportExportListExist = tSysImportExportListList.get(0);
        }else{
            throw new ECPBusinessException("没有文件上传记录，请先上传文件");
        }
        
        try {
            RemoteModelResult<UploadFile> remoteModelResult = fastDFSApi.loadFilePathById(tSysImportExportListExist.getUploadFileId());
            if(remoteModelResult.isSuccess()){
                UploadFile uploadFile = remoteModelResult.getModel();
                URL url = new URL(uploadFile.getPath());
                HttpURLConnection uc = (HttpURLConnection) url.openConnection();
                uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true
                uc.connect();
                super.excelInputStream = uc.getInputStream();
            }
        } catch (Exception e) {
            throw new ECPBusinessException("导入失败，读取文件失败："+e.getMessage());
        }

        ExecutorService executorService = Executors.newFixedThreadPool(1);

        try {
            Callable task = new PersonBuildingImportTask(this,ctx,batchNo,excelPath,projectId);
            Future<MessageMap> messageMapFuture = executorService.submit(task);
            mm = messageMapFuture.get();
        }  catch (Exception e) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(e.getMessage());
        }
        executorService.shutdown();

        while (true) {
            if (executorService.isTerminated()) {
                break;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {

            }
        }
        executorService.shutdown();
        return mm;
	}


	@Override
	public Integer getBuildingCountByCustomerId(WyBusinessContext ctx, String custId) {
		return personBuildingNewMapper.getBuildingCountByCustomerId(custId);
	}

	@Override
	public List<PersonBuildingNew> findByCondition(WyBusinessContext ctx,PersonBuildingNew personBuildingNew) {
		return personBuildingNewMapper.findByCondition(personBuildingNew);
	}

	@Override
	protected ExcelDefinitionReader getExcelDefinition() {
		 DefinitionFactory definitionReaderFactory = new XMLDefinitionFactory4commonImport("importExport/import/xml/person_building.xml");
	     return definitionReaderFactory.createExcelDefinitionReader();
	}

	@Override
	protected String getLookupItemCodeByName(String lookupCode, String parentCode, String name) {
		return null;
	}

	protected String getLookupCodeByName(String parentCode, String name) {
		 return tSysLookupService.getLookupCodeByName(ctx,parentCode,name);
	}

	protected String getLookupItemCodeByName(String parentCode, String name) {
		return tSysLookupService.getLookupItemCodeByName(ctx,parentCode,name);
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public MessageMap importData(WyBusinessContext ctx, String batchNo,List<PersonBuildingImportBean> pbImportBeans, String projectId) {
			try {
				
				//当前数据格式  :  房屋全名      房屋节点名         客户名		客户身份证号	
				int insertNum = 0;	//可插入数据
				int repeatNum = 0;	//重复数据
				int notExistsNum = 0; //不存在数据
				List<PersonBuildingNew> insertBuildingNews = new ArrayList<PersonBuildingNew>();
				StringBuffer sb = new StringBuffer();
				int rowCount = 1;
				if(CommonUtils.isNotEmpty(pbImportBeans)){
					String buildingFullName = null,
					       isEc = null,
					       name = null,
					       numStr = null,
					       houseCode = null,
					       buildingId = null,
					       buildingCode = null,
					       custId = null;
					PersonBuildingNew existsPb = null;
					TcBuildingSearch condition = new TcBuildingSearch();
					condition.setProjectId(projectId);
					for(PersonBuildingImportBean pb : pbImportBeans){
						//获取数据
						buildingFullName = pb.getFullName();
						name = pb.getCustName();
						numStr = pb.getCardnum();
						houseCode = CommonUtils.null2String(pb.getHouseCode());
						isEc = pb.getIsEc();
						
						//判断该建筑是否存在
						List<TcBuildingList> buildings = this.tcBuildingMapper.findByCode(houseCode,null);
						if(CommonUtils.isEmpty(buildings) || null == buildings.get(0)){
							notExistsNum ++;
							rowCount ++;
							
							logger.warn("资产绑定关系导入: [建筑节点 {} 不存在], 本条数据导入失败 .", buildingFullName);
							sb.append("第").append(rowCount).append("行建筑节点不存在,本条数据导入失败, 房屋全名:[").append(buildingFullName).append("]. \n");
							continue;
						}
						buildingId = buildings.get(0).getId();
						buildingCode = buildings.get(0).getBuildingCode();
						//判断该客户是否存在
						if(CommonUtils.isEquals(Constants.STR_YES_CN, isEc)){
							//企业客户
							EnterpriseCustNew ec = this.enterpriseCustNewMapper.findBySomeParams(name, numStr);
						    if(null == ec){ //企业客户不存在
						    	notExistsNum ++;
						    	rowCount ++;
						    	
						    	logger.warn("资产绑定关系导入: [企业客户 : {} , 资质证号 :{} 不存在], 本条数据导入失败 .", name , numStr );
								sb.append("第").append(rowCount).append("行企业客户资质证号不存在,本条数据导入失败, 企业客户名称:[").append(name).append("], 资质证号: [").append(numStr).append("]. \n");
						    	continue;
						    }
						    custId = ec.getEnterpriseId();
						    //判断是否已经存在绑定关系
						    existsPb = this.personBuildingNewMapper.getPersonBuildingNewByEnterpriseIdAndBsId(ec.getEnterpriseId(), buildingId);
						    
						}else{
							//个人客户
							PersonCustNew pc = this.personCustNewMapper.findBySomeParams(name, numStr);
							if(pc == null){  //个人客户不存在
								notExistsNum ++;
								rowCount ++;
								
								logger.warn("资产绑定关系导入: [个人客户 : {} , 身份证号 :{} 不存在], 本条数据导入失败 .", name , numStr );
								sb.append("第").append(rowCount).append("行个人客户身份证号不存在,本条数据导入失败, 个人客户名称:[").append(name).append("], 身份证号: [").append(numStr).append("]. \n");

								continue;
							}
							custId = pc.getCustId();
							//判断是否已经存在绑定关系
							existsPb = this.personBuildingNewMapper.getPersonBuildingNewByCustIdAndBsId(pc.getCustId(), buildingId);
						}
						if(existsPb != null){
					    	repeatNum ++;
					    	rowCount++;

					    	logger.warn("资产绑定关系导入: [客户名: {}, 证件号 : {}, 房屋名: {} 绑定关系已存在 ], 本条数据导入失败 .", name , numStr ,buildingFullName);
							sb.append("第").append(rowCount).append("行资产客户绑定信息已存在,本条数据导入失败, 客户名称:[")
								.append(name).append("], 证件号: [").append(numStr).append("], 房屋名: [").append(buildingFullName).append("]. \n");

					    	continue;
					    }
						
						//满足上述三个条件之后,构建集合,进行插入
						PersonBuildingNew insertObj = new PersonBuildingNew();
						insertObj.setPersonBuildingId(CommonUtils.getUUID());
						if(CommonUtils.isEquals(Constants.STR_YES_CN, isEc))
							insertObj.setEnterpriseId(custId);
						else
							insertObj.setCustId(custId);
						
						insertObj.setBuildingId(buildingId);
						insertObj.setBuildingCode(buildingCode);
						insertObj.setCustType("业主");
						insertObj.setState((byte)0);
						insertObj.setRelationDate(new Date());
						insertBuildingNews.add(insertObj);
					}
				}
				if(CommonUtils.isNotEmpty(insertBuildingNews)){
					insertNum = this.personBuildingNewMapper.insertList(insertBuildingNews);
					//发送到消息队列
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("opr", MqConstants.PERSON_BUILDING_BATCH_ADD);
					jsonObj.put("content",insertBuildingNews);
					this.amqpTemplate.convertAndSend(ROUTE_KEY, jsonObj);
					logger.info("消息队列: 批量删除房屋/业主关联关系, 发送消息到消息队列完成 . 数据: {}. " , jsonObj.toJSONString());
				}
				
				//日志上传
				try {
					ByteArrayInputStream ais = new ByteArrayInputStream(sb.toString().getBytes());
					RemoteModelResult<UploadFile> rslt = this.fastDFSApi.uploadFile(ais, batchNo + ".txt");
					if(rslt.isSuccess() && rslt.getModel() != null){
						TSysImportExportSearch tSysImportExportSearch = new TSysImportExportSearch();
		                tSysImportExportSearch.setBatchNo(batchNo);
		                List<TSysImportExportList> tSysImportExportLists =  importExportMapper.findByCondtion(tSysImportExportSearch);
		                if(CommonUtils.isNotEmpty(tSysImportExportLists)) {
		                    TSysImportExportList tSysImportExportList = tSysImportExportLists.get(0);
		                    tSysImportExportList.setUploadMessageId(rslt.getModel().getUploadFileId());
		                    tSysImportExportList.setEndTime(new Date());
		                    importExportMapper.modify(tSysImportExportList);
		                }
					}
				} catch (Exception e) {
					e.printStackTrace();
					return new MessageMap(MessageMap.INFOR_ERROR,"日志文件上传时失败. ");
				}
				return new MessageMap(MessageMap.INFOR_SUCCESS,"成功导入数据[" +insertNum + "]条，重复绑定数据["+repeatNum+"]条, 不存在数据["+notExistsNum+"]条. ");
			} catch (Exception e) {
				e.printStackTrace();
			}
		return new MessageMap(MessageMap.INFOR_ERROR,"数据导入失败");
	}


	@Override
	public List<Map> findGreenLightItemParametersByBuildingId(WyBusinessContext ctx, String buildingId) {
		return personBuildingNewMapper.findGreenLightItemParametersByBuildingId(buildingId);
	}






	@Override
	public BaseDto getBuildingDataByCustPhone(String companyId,String phone){

		BaseDto bto = new BaseDto();
		MessageMap messageMap = new MessageMap();

 		if(CommonUtils.isEmpty(phone)){
			messageMap.setFlag(MessageMap.INFOR_ERROR);
			messageMap.setMessage("传入参数为空");
			bto.setMessageMap(messageMap);
			return bto;
		}
		if(CommonUtils.isEmpty(companyId)){
			messageMap.setFlag(MessageMap.INFOR_ERROR);
			messageMap.setMessage("传入公司id为空");
			bto.setMessageMap(messageMap);
			return bto;
		}
		List returnList = new ArrayList();
		List<PersonBuildingNew> list = personBuildingNewMapper.getBuildingDataByCustPhone(phone);

 		if(CommonUtils.isEmpty(list)){
			messageMap.setFlag(MessageMap.INFOR_ERROR);
			messageMap.setMessage("该用户没有对应的资产");
			bto.setMessageMap(messageMap);
			return bto;
		}
		for(PersonBuildingNew personBuildingNew: list ){
			Map map = new HashMap();
			if(CommonUtils.isEmpty(personBuildingNew.getBuildingId())){
				messageMap.setFlag(MessageMap.INFOR_ERROR);
				messageMap.setMessage("传入参数为空");
				bto.setMessageMap(messageMap);
				return bto;
			}
			map.put("buildingId",personBuildingNew.getBuildingId());
			map.put("buildingFullName",(personBuildingNew.getBuildingFullName())==null?"": personBuildingNew.getBuildingFullName());
			map.put("projectId",(personBuildingNew.getProjectId())==null?"":personBuildingNew.getProjectId());
			map.put("projectName",(personBuildingNew.getProjectName())==null?"":personBuildingNew.getProjectName());
			List<Map> owerList = personBuildingNewMapper.getOwerDataByBuildingId(personBuildingNew.getBuildingId());
			map.put("ower",CommonUtils.isEmpty(owerList)==true?"":owerList);
			returnList.add(map);
		}
		messageMap.setFlag(MessageMap.INFOR_SUCCESS);
		messageMap.setMessage("查询成功");
		bto.setMessageMap(messageMap);
		bto.setLstDto(returnList);
		return bto;
	}

	@Override
	public List<BuildingAndCustDTO> getBuindingAndCustByMobile(String companyId, String mobile) {
		return personBuildingNewMapper.queryByMobile(mobile);
	}


}
