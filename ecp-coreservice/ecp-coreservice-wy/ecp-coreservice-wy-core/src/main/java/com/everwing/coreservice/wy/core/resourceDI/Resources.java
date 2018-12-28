package com.everwing.coreservice.wy.core.resourceDI;

import com.everwing.coreservice.common.wy.service.configuration.cmac.service.ICmacService;
import com.everwing.coreservice.common.wy.service.configuration.cmac.single.servie.ISingleCmacService;
import com.everwing.coreservice.platform.api.CompanyApi;
import com.everwing.coreservice.wy.dao.mapper.account.pay.TBsPayInfoMapper;
import com.everwing.coreservice.wy.dao.mapper.annex.AnnexMapper;
import com.everwing.coreservice.wy.dao.mapper.business.electmeter.TcElectMeterMapper;
import com.everwing.coreservice.wy.dao.mapper.business.meterdata.TcMeterDataMapper;
import com.everwing.coreservice.wy.dao.mapper.business.meterrelation.TcMeterRelationMapper;
import com.everwing.coreservice.wy.dao.mapper.business.readingschedule.TcReadingScheduleMapper;
import com.everwing.coreservice.wy.dao.mapper.business.readingtask.TcReadingTaskMapper;
import com.everwing.coreservice.wy.dao.mapper.business.watermeter.TcWaterMeterMapper;
import com.everwing.coreservice.wy.dao.mapper.business.watermeter.TcWaterMeterOperRecordMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.TBsProjectMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.TBcCollectionMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.jrl.TBcJrlBodyMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.jrl.TBcJrlHeadMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.union.TBcUnionCollectionBodyMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.union.TBcUnionCollectionHeadMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.union.back.TBcUnionBackBodyMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.union.back.TBcUnionBackHeadMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bc.project.TBcProjectMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bill.TBsChargeBillHistoryMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bill.TBsChargeBillTotalMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.owed.TBsOwedHistoryMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsChargingSchemeMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsShareBasicsInfoMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsShareRelatedTaskMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.rebilling.TBsRebillingInfoMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.tbs.assetsaccount.TBsAssetAccountMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.tbs.assetsaccount.stream.TBsAssetAccountStreamMapper;
import com.everwing.coreservice.wy.dao.mapper.cust.CollectionMapper;
import com.everwing.coreservice.wy.dao.mapper.cust.TBankInfoMapper;
import com.everwing.coreservice.wy.dao.mapper.cust.enterprisecust.EnterpriseCustNewMapper;
import com.everwing.coreservice.wy.dao.mapper.cust.person.PersonCustNewMapper;
import com.everwing.coreservice.wy.dao.mapper.delivery.TJgAccountReceivableMapper;
import com.everwing.coreservice.wy.dao.mapper.delivery.TJgStaffGropMapper;
import com.everwing.coreservice.wy.dao.mapper.delivery.TJgTotalCalculationMapper;
import com.everwing.coreservice.wy.dao.mapper.personbuilding.PersonBuildingNewMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcBuildingMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcHouseMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcStallMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcStoreMapper;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysLookupMapper;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysProjectMapper;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysUserMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 
 * 提供统一的依赖注入
 *
 */
@Component
public class Resources {
	
	protected static Map<String,List<ICmacService>> handlers = new ConcurrentHashMap<String,List<ICmacService>>();
	protected static Map<String,List<ISingleCmacService>> signleHandlers = new ConcurrentHashMap<String,List<ISingleCmacService>>();
	
	protected static final String AUTO_GENER = "system";
	
	/**
	 * 账单生成消息队列 
	 */
	@Value("${queue.wy2Wy.bill.gen.write.key}")
	protected String route_key_bill_gen;
	
	/**
	 * 批量最大插入次数
	 */
	@Value("${batch.add.count}")
	protected Integer BATCH_ADD_COUNT;
	
	/**
	 * 批量最大更新次数
	 */
	@Value("${batch.update.count}")
	protected Integer BATCH_UPDATE_COUNT;
	
	/**-------------------- basedata start ------------------------*/
	@Autowired
	protected TcHouseMapper tcHouseMapper;
	
	@Autowired
	protected TcBuildingMapper tcBuildingMapper;
	
	@Autowired
	protected TcStoreMapper tcStoreMapper;
	
	@Autowired
	protected TcStallMapper tcStallMapper;
	
	@Autowired
	protected TSysUserMapper tSysUserMapper;
	
	@Autowired
	protected TSysLookupMapper tSysLookupMapper;
	
	/**-------------------- basedata end ------------------------*/
	
	/**-------------------- business start ------------------------*/
	@Autowired
	protected TcReadingScheduleMapper tcReadingScheduleMapper;
	
	@Autowired
	protected TcReadingTaskMapper tcReadingTaskMapper;
	
	@Autowired
	protected TcWaterMeterMapper tcWaterMeterMapper;
	
	@Autowired
	protected TcElectMeterMapper tcElectMeterMapper;
	
	@Autowired
	protected TcMeterDataMapper tcMeterDataMapper;
	
	@Autowired
	protected TcMeterRelationMapper tcMeterRelationMapper;
	
	@Autowired
	protected TcWaterMeterOperRecordMapper tcWaterMeterOperRecordMapper;
	
	@Autowired
	protected TBsPayInfoMapper tBsPayInfoMapper;
	
	/**-------------------- business end ------------------------*/
	
	
	/**-------------------- baseSetting start ------------------------*/
	
		/**-------------------- baseSetting - billing  start ------------------------*/
		@Autowired
		protected TBsProjectMapper tBsProjectMapper;
	
		@Autowired
		protected TBsChargeBillHistoryMapper tBsChargeBillHistoryMapper;	//每期计费详情
		
		@Autowired
		protected TBsChargeBillTotalMapper tBsChargeBillTotalMapper;		//每期计费总额
		
		@Autowired
		protected TBsChargingSchemeMapper tBsChargingSchemeMapper;		//方案表

			/**-------------------- share - billing  start ------------------------*/
			@Autowired
			protected TBsShareBasicsInfoMapper tBsShareBasicsInfoMapper;
			@Autowired
			protected TBsShareRelatedTaskMapper tBsShareRelatedTaskMapper;
		
			/**-------------------- share - billing  end ------------------------*/
		
		
		@Autowired
		protected TBsAssetAccountMapper tBsAssetAccountMapper;			//账户表
		
		@Autowired
		protected TBsAssetAccountStreamMapper tBsAssetAccountStreamMapper;	//账户流水表
		
		@Autowired
		protected TBsOwedHistoryMapper tBsOwedHistoryMapper;	//欠费历史表
		
		@Autowired
		protected TBsRebillingInfoMapper tBsRebillingInfoMapper;	//重新计费历史数据
		
		/**-------------------- baseSetting - billing  end ------------------------*/
		
		
		
		/**-------------------- baseSetting - collection  start ------------------------*/
		@Autowired
		protected TBcProjectMapper tBcProjectMapper;	//托收项目
		@Autowired
		protected TBcCollectionMapper tBcCollectionMapper;	//托收总单
			//银联托收母子表
			@Autowired
			protected TBcUnionCollectionHeadMapper tBcUnionCollectionHeadMapper;
			@Autowired
			protected TBcUnionCollectionBodyMapper tBcUnionCollectionBodyMapper;
			//银联回盘母子表
			@Autowired
			protected TBcUnionBackHeadMapper tBcUnionBackHeadMapper;
			@Autowired
			protected TBcUnionBackBodyMapper tBcUnionBackBodyMapper;
			//金融联托收母子表
			@Autowired
			protected TBcJrlHeadMapper tBcJrlHeadMapper;
			@Autowired
			protected TBcJrlBodyMapper tBcJrlBodyMapper;
			
			//托收基础数据
			@Autowired
			protected CollectionMapper collectionMapper;
			
			//银行基础数据
			@Autowired
			protected TBankInfoMapper tBankInfoMapper;
		/**-------------------- baseSetting - collection  end ------------------------*/
		
		/**-------------------- baseSetting - jg  start ------------------------*/
			@Autowired
			protected TJgStaffGropMapper tJgStaffGropMapper;
			
			@Autowired
			protected TJgAccountReceivableMapper tJgAccountReceivableMapper;

			@Autowired
			protected TJgTotalCalculationMapper tJgTotalCalculationMapper;
			
		/**-------------------- baseSetting - jg  end ------------------------*/
		
	
	/**-------------------- baseSetting end ------------------------*/
	
	
	@Autowired
	protected CompanyApi companyApi;
		
	@Autowired
	protected AmqpTemplate amqpTemplate;
	
	@Autowired
	protected PersonCustNewMapper personCustNewMapper;
	
	@Autowired
	protected EnterpriseCustNewMapper enterpriseCustNewMapper;
	
	@Autowired
	protected AnnexMapper annexMapper;
	
	@Autowired
	protected TSysProjectMapper tSysProjectMapper;
	
	@Autowired
	protected PersonBuildingNewMapper personBuildingNewMapper;
	
}
