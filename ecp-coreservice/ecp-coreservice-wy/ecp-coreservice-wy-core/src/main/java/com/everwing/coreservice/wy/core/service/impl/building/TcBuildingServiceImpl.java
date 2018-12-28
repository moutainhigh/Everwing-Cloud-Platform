package com.everwing.coreservice.wy.core.service.impl.building;/**
 * Created by wust on 2017/4/18.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.ThreadPool.ThreadPoolUtils;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.cache.DataDictionaryUtil;
import com.everwing.coreservice.common.utils.generator.WyCodeGenerator;
import com.everwing.coreservice.common.wy.common.enums.*;
import com.everwing.coreservice.common.wy.entity.common.synchrodata.TSynchrodata;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccount;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingList;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.common.wy.entity.property.property.CustomerSearch;
import com.everwing.coreservice.common.wy.initialization.AbstractInitializeData;
import com.everwing.coreservice.common.wy.service.building.TcBuildingService;
import com.everwing.coreservice.platform.api.AccountApi;
import com.everwing.coreservice.platform.api.CompanyApi;
import com.everwing.coreservice.wy.core.task.Wy2platform4buildingTask;
import com.everwing.coreservice.wy.dao.mapper.common.ImportExportMapper;
import com.everwing.coreservice.wy.dao.mapper.common.TSynchrodataMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.tbs.assetsaccount.TBsAssetAccountMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcBuildingMapper;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysLookupMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Function:
 * Reason:
 * Date:2017-4-18 08:42:30
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
@Service("tcBuildingServiceImpl")
public class TcBuildingServiceImpl implements TcBuildingService{

    static Logger logger = LogManager.getLogger(TcBuildingServiceImpl.class);

    @Autowired
    private TcBuildingMapper tcBuildingMapper;

    @Autowired
    private AccountApi accountApi;

    @Autowired
    private TSysLookupMapper tSysLookupMapper;

    @Autowired
    private ImportExportMapper importExportMapper;

    @Autowired
    private  TBsAssetAccountMapper tBsAssetAccountMapper;


    @Autowired
    private CompanyApi companyApi;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private AbstractInitializeData initializeBuildingData;

    @Autowired
    private TSynchrodataMapper tSynchrodataMapper;

    //用来存放初始化项目时的项目编号，避免连续点击初始化按钮。
    private static final Map<String,String> projectMap = new HashMap<String,String>();

    @Value("${queue.wy2platform.building.key}")
    private String queue_wy2platform_building_key;


    @Override
    public BaseDto listPage(WyBusinessContext ctx, TcBuildingSearch condition) {
        List<TcBuildingList> list = tcBuildingMapper.listPage(condition);
        /**
         * 不用表去管理查询而在这里设置名称的好处是：
         * 当表的记录达到几百万时，关联查询的性能很差，而此次只需要循环十条分页数据，性能比sql关联查询性能要好非常多
         */
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(list)){
            for (TcBuildingList tcBuildingList : list) {
                tcBuildingList.setBuildingTypeName(
                        DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(),
                                LookupEnum.buildingType.name(),
                                tcBuildingList.getBuildingType()));
                tcBuildingList.setMarketStateName(
                        DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(),
                                LookupEnum.marketState.name(),
                                tcBuildingList.getMarketState()));
                if(LookupItemEnum.buildingType_house.getStringValue().equalsIgnoreCase(tcBuildingList.getBuildingType())){
                    tcBuildingList.setAssetAttributesName(DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(),
                            "houseType",
                            tcBuildingList.getAssetAttributes()));
                }else if(LookupItemEnum.buildingType_store.getStringValue().equalsIgnoreCase(tcBuildingList.getBuildingType())){
                    tcBuildingList.setAssetAttributesName( DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(),
                            "storeType",
                            tcBuildingList.getAssetAttributes()));
                }else if(LookupItemEnum.buildingType_parkspace.getStringValue().equalsIgnoreCase(tcBuildingList.getBuildingType())){
                    tcBuildingList.setAssetAttributesName( DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(),
                            "stationProperty",
                            tcBuildingList.getAssetAttributes()));
                }

            }
        }

        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(condition.getPage());
        return baseDto ;
    }

    @Override
    public BaseDto listPageUnrelated(WyBusinessContext ctx, TcBuildingSearch condition) {
        List<TcBuildingList> list = tcBuildingMapper.listPageUnrelated(condition);
        /**
         * 不用表去管理查询而在这里设置名称的好处是：
         * 当表的记录达到几百万时，关联查询的性能很差，而此次只需要循环十条分页数据，性能比sql关联查询性能要好非常多
         */
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(list)){
            for (TcBuildingList tcBuildingList : list) {
                tcBuildingList.setBuildingTypeName(
                        DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(),
                                LookupEnum.buildingType.name(),
                                tcBuildingList.getBuildingType()));
                tcBuildingList.setMarketStateName(
                        DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(),
                                LookupEnum.marketState.name(),
                                tcBuildingList.getMarketState()));
                if(LookupItemEnum.buildingType_house.getStringValue().equalsIgnoreCase(tcBuildingList.getBuildingType())){
                    tcBuildingList.setAssetAttributesName(DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(),
                            "houseType",
                            tcBuildingList.getAssetAttributes()));
                }else if(LookupItemEnum.buildingType_store.getStringValue().equalsIgnoreCase(tcBuildingList.getBuildingType())){
                    tcBuildingList.setAssetAttributesName( DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(),
                            "storeType",
                            tcBuildingList.getAssetAttributes()));
                }else if(LookupItemEnum.buildingType_parkspace.getStringValue().equalsIgnoreCase(tcBuildingList.getBuildingType())){
                    tcBuildingList.setAssetAttributesName( DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(),
                            "stationProperty",
                            tcBuildingList.getAssetAttributes()));
                }

            }
        }

        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(condition.getPage());
        return baseDto ;
    }
    @Override
    public List<TcBuildingList> findAllBuildingNodeByCondition(WyBusinessContext ctx, TcBuildingSearch condition) {
        return tcBuildingMapper.findAllBuildingNodeByCondition(condition);
    }

    @Override
    public List<TcBuildingList> findByCondition(WyBusinessContext ctx, TcBuildingSearch condition) {
        return tcBuildingMapper.findByCondition(condition);
    }



    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap add(WyBusinessContext ctx, TcBuilding entity) {
        MessageMap messageMap = new MessageMap();

        String regEx="[^0-9a-bA-B]";
        Pattern p = Pattern.compile(regEx);

        List<TcBuilding> addList = new ArrayList<>();
        String projectId = entity.getProjectId();
        String buildingFullName = "";

        /**
         * 如果是标准建筑，则严格校验格式
         * 资产全名格式：01期 + 01区 + 001栋0座 + 1单元/1入口 + 01层 + 01（流水码）
         * 新资产编码则根据全名称提取XX数字来充当：10（两位项目编码） + 01（期） + 01（区） + 001栋0座 + 1（单元/入口） + 01（流水码）
         */
        if("Yes".equalsIgnoreCase(entity.getIsStandardBuilding())){
            if(LookupItemEnum.buildingType_qi.getStringValue().equalsIgnoreCase(entity.getBuildingType())){
                Matcher m = p.matcher(entity.getBuildingName());

                String numberStr = m.replaceAll("").trim();
                if(numberStr.length() == 1){
                    entity.setBuildingName("0" + numberStr + "期");
                }else if(numberStr.length() == 2){
                    entity.setBuildingName(numberStr + "期");
                }else{
                    throw new ECPBusinessException("期格式有误，正确的格式如：01期或01");
                }

                buildingFullName = getParentBuildingFullNameByPid(projectId,entity.getPid(),entity.getBuildingName()) + entity.getBuildingName();
            }else if(LookupItemEnum.buildingType_qu.getStringValue().equalsIgnoreCase(entity.getBuildingType())){
                Matcher m = p.matcher(entity.getBuildingName());

                String numberStr = m.replaceAll("").trim();
                if(numberStr.length() == 0){
                    entity.setBuildingName("00" + numberStr + "区");
                }else if(numberStr.length() == 1){
                    entity.setBuildingName("0" + numberStr + "区");
                }else if(numberStr.length() == 2){
                    entity.setBuildingName(numberStr + "区");
                }else{
                    throw new ECPBusinessException("区格式有误，正确的格式如：XX区或XX");
                }

                buildingFullName = getParentBuildingFullNameByPid(projectId,entity.getPid(),entity.getBuildingName()) + entity.getBuildingName();
            }else if(LookupItemEnum.buildingType_dongzuo.getStringValue().equalsIgnoreCase(entity.getBuildingType())){
                String buildingNameNew = "";
                /**
                 * 验证栋座格式的栋部分
                 */
                String dongStr = "";
                if(entity.getBuildingName().contains("栋")){
                    dongStr = entity.getBuildingName().substring(0,entity.getBuildingName().indexOf("栋"));
                }else{
                    if(entity.getBuildingName().contains("座")){
                        // 跨过栋，直接填写座，不合格
                        throw new ECPBusinessException("栋格式有误，正确的格式如：001栋或001");
                    }
                    dongStr = entity.getBuildingName();
                }

                Matcher dongMatcher = p.matcher(dongStr);
                String dongNumberStr = dongMatcher.replaceAll("").trim();
                if(dongNumberStr.length() == 1){ // 只填一位数字
                    buildingNameNew = StringUtils.leftPad(dongNumberStr,3,"0") + "栋";
                }else if(dongNumberStr.length() == 2){
                    buildingNameNew = StringUtils.leftPad(dongNumberStr,3,"0") + "栋";
                }else if(dongNumberStr.length() == 3){
                    buildingNameNew = dongNumberStr + "栋";
                }else{
                    throw new ECPBusinessException("栋格式有误，正确的格式如：001栋或001");
                }

                /**
                 * 验证栋座格式的座部分
                 */
                String zuoStr = "";
                if(entity.getBuildingName().contains("栋")){
                    zuoStr = entity.getBuildingName().substring(entity.getBuildingName().indexOf("栋"));
                }


                Matcher zuoMatcher = p.matcher(zuoStr);
                String zuoNumberStr = zuoMatcher.replaceAll("").trim();
                if(zuoNumberStr.length() == 0){
                    buildingNameNew = buildingNameNew + "0座";
                }else if(zuoNumberStr.length() == 1){
                    buildingNameNew = buildingNameNew + zuoNumberStr + "座";
                }else{
                    throw new ECPBusinessException("座格式有误，正确的格式如：0座");
                }

                entity.setBuildingName(buildingNameNew);

                buildingFullName = getParentBuildingFullNameByPid(projectId,entity.getPid(),entity.getBuildingName()) + entity.getBuildingName();
            }else if(LookupItemEnum.buildingType_danyuanrukou.getStringValue().equalsIgnoreCase(entity.getBuildingType())){
                Matcher m = p.matcher(entity.getBuildingName());
                String numberStr = m.replaceAll("").trim();
                if(numberStr.length() != 1){
                    throw new ECPBusinessException("单元/入口格式有误，正确的格式如：1单元/1入口或1");
                }

                if(numberStr.equals(entity.getBuildingName())){
                    entity.setBuildingName(numberStr + "单元");
                }

                buildingFullName = getParentBuildingFullNameByPid(projectId,entity.getPid(),entity.getBuildingName()) + entity.getBuildingName();
            }else if(LookupItemEnum.buildingType_ceng.getStringValue().equalsIgnoreCase(entity.getBuildingType())){
                Matcher m = p.matcher(entity.getBuildingName());
                String numberStr = m.replaceAll("").trim();
                if(numberStr.length() == 0){
                    entity.setBuildingName("00层");
                }else if(numberStr.length() == 1){
                    entity.setBuildingName("0" + numberStr + "层");
                }else if(numberStr.length() == 2){
                    entity.setBuildingName(numberStr + "层");
                }else{
                    throw new ECPBusinessException("层格式有误，正确的格式如：01层或01");
                }

                buildingFullName = getParentBuildingFullNameByPid(projectId,entity.getPid(),entity.getBuildingName()) + entity.getBuildingName();
            }else{
                Matcher m = p.matcher(entity.getBuildingName().trim());
                String numberStr = m.replaceAll("").trim();
                if(numberStr.length() == 1){
                    entity.setBuildingName(StringUtils.leftPad(numberStr,2,"0"));
                }else if(numberStr.length() == 2){
                    entity.setBuildingName(StringUtils.leftPad(numberStr,2,"0"));
                }else{
                    entity.setBuildingName(numberStr);
                }

                /**
                 * 根据全名称解析出数字
                 */
                buildingFullName = getParentBuildingFullNameByPid(projectId,entity.getPid(),entity.getBuildingName()) + entity.getBuildingName();
                Matcher buildingFullNameMatcher = p.matcher(buildingFullName);
                String buildingFullnameNumberStr = buildingFullNameMatcher.replaceAll("").trim();

                // 用户在没有自定义资产编码的情况下，系统自动生成
                if(StringUtils.isBlank(entity.getHouseCodeNew())){
                    String houseCodeNew = projectId.substring(projectId.length() - 2) + buildingFullnameNumberStr;
                    if(LookupItemEnum.buildingType_house.getStringValue().equalsIgnoreCase(entity.getBuildingType())){
                        houseCodeNew = houseCodeNew + WyEnum.CODE_TYPE_1.getStringValue();
                    }else if(LookupItemEnum.buildingType_store.getStringValue().equalsIgnoreCase(entity.getBuildingType())){
                        houseCodeNew = houseCodeNew + WyEnum.CODE_TYPE_2.getStringValue();
                    }else if(LookupItemEnum.buildingType_parkspace.getStringValue().equalsIgnoreCase(entity.getBuildingType())){
                        houseCodeNew = houseCodeNew + WyEnum.CODE_TYPE_3.getStringValue();
                    }
                    entity.setHouseCodeNew(houseCodeNew);
                }
            }
        }else{
            if(LookupItemEnum.buildingType_publicbuilding.getStringValue().equalsIgnoreCase(entity.getBuildingType())){
                Matcher m = p.matcher(entity.getBuildingName());
                String numberStr = m.replaceAll("").trim();
                if(numberStr.length() < 1){
                    throw new ECPBusinessException("流水码格式有误，流水码必须大于等于5位数字，如：00001");
                }else if(numberStr.length() < 5){
                    entity.setBuildingName(StringUtils.leftPad(numberStr,5,"0"));
                }

                // 公共资产
                String houseCodeNew = projectId.substring(projectId.length() - 2) + WyEnum.CODE_TYPE_05.getStringValue() + numberStr;
                entity.setHouseCodeNew(houseCodeNew);
            }

            buildingFullName = getParentBuildingFullNameByPid(projectId,entity.getPid(),entity.getBuildingName()) + entity.getBuildingName();
        }

        // 建筑编码
        String codeStr = WyCodeGenerator.genBuildingCode();
        

        TcBuildingSearch condition = new TcBuildingSearch();
        condition.setBuildingFullName(buildingFullName);
        condition.setProjectId(entity.getProjectId());
        List<TcBuildingList> exsisList = tcBuildingMapper.findByCondition(condition);
        if(!CollectionUtils.isEmpty(exsisList)){
            throw new ECPBusinessException("该名称已经存在，请重新输入");
        }else{
            String id = UUID.randomUUID().toString();
            entity.setId(id);
            entity.setBuildingFullName(buildingFullName);
            entity.setBuildingCode(codeStr);
            entity.setProjectId(entity.getProjectId());
            entity.setPassword(((Integer)((int)((Math.random() * 9 + 1) * 100000))).toString());
            entity.setCompanyId(ctx.getCompanyId());
            addList.add(entity);
            tcBuildingMapper.batchInsert(addList);
            messageMap.setObj(entity);


            /**
             * 推送到平台
             */
            List<TSynchrodata> tSynchrodatas = new ArrayList<>(1);
            TSynchrodata tSynchrodata = new TSynchrodata();
            tSynchrodata.setCode(WyCodeGenerator.genSynchrodataCode());
            tSynchrodata.setDescription("同步建筑tc_building表");
            tSynchrodata.setTableName(SynchrodataEnum.table_tc_building.getStringValue());
            tSynchrodata.setTableFieldName("building_code");
            tSynchrodata.setTableFieldValue(entity.getBuildingCode());
            tSynchrodata.setDestinationQueue(queue_wy2platform_building_key);
            tSynchrodata.setOperation(RabbitMQEnum.insert.name());
            tSynchrodata.setPriorityLevel(SynchrodataEnum.priorityLevel_middle.getIntValue());
            tSynchrodata.setState(SynchrodataEnum.state_draft.getStringValue());
            tSynchrodata.setCreaterId(ctx.getUserId());
            tSynchrodata.setCreaterName(ctx.getStaffName());
            tSynchrodatas.add(tSynchrodata);
            tSynchrodataMapper.batchInsert(tSynchrodatas);
        }

        // 重新初始化树到缓存
        initializeBuildingData.init();
        return messageMap;
    }

    /**
     * 获取父节点的全名称
     * @param projectId
     * @param pid
     * @return
     */
    private String getParentBuildingFullNameByPid(String projectId,String pid,String defaultName){
        if(StringUtils.isBlank(pid)){
            return defaultName;
        }else{
            TcBuildingSearch condition = new TcBuildingSearch();
            condition.setBuildingCode(pid);
            condition.setProjectId(projectId);
            List<TcBuildingList> tcBuildingLists = tcBuildingMapper.findByCondition(condition);
            if(org.apache.commons.collections.CollectionUtils.isNotEmpty(tcBuildingLists)){
                return tcBuildingLists.get(0).getBuildingFullName();
            }
        }
        return "";
    }


    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap modify(WyBusinessContext ctx, TcBuilding entity) {
        MessageMap messageMap = new MessageMap();
        List<TcBuilding> buildingListModify = new ArrayList<>();
        TcBuildingSearch tcBuildingSearch = new TcBuildingSearch();
        tcBuildingSearch.setBuildingCode(entity.getBuildingCode());
        List<TcBuildingList> list = tcBuildingMapper.findByCondition(tcBuildingSearch);
        if(!CollectionUtils.isEmpty(list)){
            TcBuildingList tcBuildingList = list.get(0);

            /**
             * 是否需要级联修改子节点名称
             */
            String newBuildingName = entity.getBuildingName().trim();
            String oldBuildingName = tcBuildingList.getBuildingName();
            if(!oldBuildingName.equals(newBuildingName)){
                // 修改当前节点的全名称
                String newParentBuildingFullName = tcBuildingList.getBuildingFullName().replace(oldBuildingName,newBuildingName);
                tcBuildingMapper.renameBuildingFullName(newParentBuildingFullName,tcBuildingList.getBuildingCode());

                // 递归修改子节点的全名称
                List<TcBuilding> tcBuildingsByPid = tcBuildingMapper.findBuildingByPid(tcBuildingList.getBuildingCode());
                if(org.apache.commons.collections.CollectionUtils.isNotEmpty(tcBuildingsByPid)){
                    for (TcBuilding child : tcBuildingsByPid) {
                        String newChildBuildingFullName = child.getBuildingFullName().replace(oldBuildingName,newBuildingName);

                        child.setBuildingFullName(newChildBuildingFullName);
                        buildingListModify.add(child);

                        tcBuildingMapper.renameBuildingFullName(newChildBuildingFullName,child.getBuildingCode());
                        renameChildrenBuildingFullName(buildingListModify,child,oldBuildingName,newBuildingName);
                    }
                }
            }

            /**
             * 1.如果车位A把车位B当作自己的子母关联车位，那么车位B应该也要把车位A当作自己的关联车位，以下代码就是干这种事
             * 2.如果突然由子母车位变为非子母车位了，也得解除两边的关系
             */
            if("009_003".equals(entity.getAssetAttributes())) { // 子母车位，则设置关联车位
                /**
                 * 先清空以当前车作为关联车位的车位的关联车位
                 */
                tcBuildingSearch = new TcBuildingSearch();
                tcBuildingSearch.setAssociatedParkingSpaces(entity.getHouseCodeNew());
                List<TcBuildingList> tcBuildingLists2 = tcBuildingMapper.findByCondition(tcBuildingSearch);
                if(org.apache.commons.collections.CollectionUtils.isNotEmpty(tcBuildingLists2)){
                    for (TcBuildingList tcBuildingList2 : tcBuildingLists2) {
                        tcBuildingList2.setAssociatedParkingSpaces("");
                        tcBuildingMapper.modify(tcBuildingList2);
                        buildingListModify.add(tcBuildingList2);
                    }
                }


                /**
                 * 把当前车位设置为被关联车位的关联车位
                 */
                tcBuildingSearch = new TcBuildingSearch();
                tcBuildingSearch.setHouseCodeNew(entity.getAssociatedParkingSpaces());
                List<TcBuildingList> tcBuildingLists4 = tcBuildingMapper.findByCondition(tcBuildingSearch);
                if(org.apache.commons.collections.CollectionUtils.isNotEmpty(tcBuildingLists4)){
                    for (TcBuildingList tcBuildingList4 : tcBuildingLists4) {
                        tcBuildingList4.setAssociatedParkingSpaces(entity.getHouseCodeNew());
                        tcBuildingMapper.modify(tcBuildingList4);
                        buildingListModify.add(tcBuildingList4);
                    }
                }
            }else{ // 由子母车位改为非子母车位，则清空关联车位
                /**
                 * 清空使用当前车位作为关联的车位的AssociatedParkingSpaces值
                 */
                tcBuildingSearch = new TcBuildingSearch();
                tcBuildingSearch.setAssociatedParkingSpaces(entity.getHouseCodeNew());
                List<TcBuildingList> tcBuildingLists4 = tcBuildingMapper.findByCondition(tcBuildingSearch);
                if(org.apache.commons.collections.CollectionUtils.isNotEmpty(tcBuildingLists4)){
                    for (TcBuildingList tcBuildingList4 : tcBuildingLists4) {
                        tcBuildingList4.setAssociatedParkingSpaces("");
                        tcBuildingMapper.modify(tcBuildingList4);
                        buildingListModify.add(tcBuildingList4);
                    }
                }
            }
            tcBuildingMapper.modify(entity);
            buildingListModify.add(entity);
        }


        /**
         * 推送到平台
         */
        if(!CollectionUtils.isEmpty(buildingListModify)){
            List<TSynchrodata> tSynchrodatas = new ArrayList<>(buildingListModify.size());
            for (TcBuilding tcBuilding : buildingListModify) {
                TSynchrodata tSynchrodata = new TSynchrodata();
                tSynchrodata.setCode(WyCodeGenerator.genSynchrodataCode());
                tSynchrodata.setDescription("同步建筑tc_building表");
                tSynchrodata.setTableName(SynchrodataEnum.table_tc_building.getStringValue());
                tSynchrodata.setTableFieldName("building_code");
                tSynchrodata.setTableFieldValue(tcBuilding.getBuildingCode());
                tSynchrodata.setDestinationQueue(queue_wy2platform_building_key);
                tSynchrodata.setOperation(RabbitMQEnum.modify.name());
                tSynchrodata.setPriorityLevel(SynchrodataEnum.priorityLevel_middle.getIntValue());
                tSynchrodata.setState(SynchrodataEnum.state_draft.getStringValue());
                tSynchrodata.setCreaterId(ctx.getUserId());
                tSynchrodata.setCreaterName(ctx.getStaffName());
                tSynchrodatas.add(tSynchrodata);
            }
            tSynchrodataMapper.batchInsert(tSynchrodatas);
        }
        return messageMap;
    }

    /**
     * 递归修改子节点全名称
     * @param tcBuilding
     */
    private void renameChildrenBuildingFullName(List<TcBuilding> buildingListModify,TcBuilding tcBuilding,String oldBuildingName,String newBuildingName){
        List<TcBuilding> tcBuildingsByPid = tcBuildingMapper.findBuildingByPid(tcBuilding.getBuildingCode());
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(tcBuildingsByPid)){
            for (TcBuilding child : tcBuildingsByPid) {
                String newChildBuildingFullName = child.getBuildingFullName().replace(oldBuildingName,newBuildingName);

                child.setBuildingFullName(newChildBuildingFullName);
                buildingListModify.add(child);

                tcBuildingMapper.renameBuildingFullName(newChildBuildingFullName,child.getBuildingCode());

                renameChildrenBuildingFullName(buildingListModify,child,oldBuildingName,newBuildingName);
            }
        }
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap delete(String compnayId,String guids) {
        MessageMap messageMap = new MessageMap();
        if(CommonUtils.isEmpty(CommonUtils.null2String(guids))){
            throw new ECPBusinessException("请重新选择数据");
        }else{
            TcBuildingSearch tcBuildingSearch = new TcBuildingSearch();
            tcBuildingSearch.setPid(guids);
            List<TcBuildingList> tcBuildingListList = tcBuildingMapper.findByCondition(tcBuildingSearch);
            if(!CollectionUtils.isEmpty(tcBuildingListList)){
                throw new ECPBusinessException("请先删除所有子节点");
            }else{
                List<String> guidList = CommonUtils.str2List(guids,",");
                if(!CollectionUtils.isEmpty(guidList)){
                    // 建筑
                    tcBuildingMapper.batchDelete(guidList);

                    /**
                     * 推送到平台
                     */
                    Runnable runnable = new Wy2platform4buildingTask(guidList, RabbitMQEnum.batchDel.name(),compnayId);
                    threadPoolTaskExecutor.execute(runnable);
                }
            }
        }
        return messageMap;
    }


    @Override
    public List<Map<String,String>> collectAssetInfo(WyBusinessContext ctx, TcBuildingSearch tcBuildingSearch) {
        return tcBuildingMapper.collectAssetInfo(tcBuildingSearch);
    }


    /**
     * 同步最新树
     * @param ctx
     * @return
     */
    @Override
    public MessageMap syncBuildingTree(WyBusinessContext ctx) {
        MessageMap mm = new MessageMap();
        initializeBuildingData.init(ctx.getCompanyId());
        return mm;
    }



    /*************************感情分割线***********************************************************************************/




















































    @Override
    public BaseDto listPageBuildingInEntery(String companyId,CustomerSearch customerSearch) {
        List<TcBuildingList> list = tcBuildingMapper.listPageBuildingInEntery(customerSearch);

        BaseDto baseDto = new BaseDto();
        baseDto.setLstDto(list);
        baseDto.setPage(customerSearch.getPage());
        return baseDto ;
    }


    private    List<TBsAssetAccount> createTBsList(TcBuilding entity,String projectName){
        List<TBsAssetAccount> tBAccountlist = new ArrayList<TBsAssetAccount>();
        TBsAssetAccount tbs = null;

        //根据类型和建筑编号查询；如果账户存在则跳出循环
        //通用账户
        tbs = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(entity.getBuildingCode(), WyTbEnum.TBS_ACCOUNT_TYPE_COMMON.getIntValue());
        if(CommonUtils.isEmpty(tbs)){
            TBsAssetAccount tBsAssetAccount = createTBsAssetAccount(entity,projectName);
            tBsAssetAccount.setType(WyTbEnum.TBS_ACCOUNT_TYPE_COMMON.getIntValue()); //通用账户
            tBAccountlist.add(tBsAssetAccount);
        }
        //物业账户
        tbs = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(entity.getBuildingCode(), WyTbEnum.TBS_ACCOUNT_TYPE_WY.getIntValue());
        if(CommonUtils.isEmpty(tbs)){
            TBsAssetAccount tBsAssetAccount = createTBsAssetAccount(entity,projectName);
            tBsAssetAccount.setType(WyTbEnum.TBS_ACCOUNT_TYPE_WY.getIntValue()); //物业管理费账户
            tBAccountlist.add(tBsAssetAccount);
        }
        //水表账户
        tbs = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(entity.getBuildingCode(), WyTbEnum.TBS_ACCOUNT_TYPE_WATER.getIntValue());
        if(CommonUtils.isEmpty(tbs)){
            TBsAssetAccount tBsAssetAccount = createTBsAssetAccount(entity,projectName);
            tBsAssetAccount.setType(WyTbEnum.TBS_ACCOUNT_TYPE_WATER.getIntValue()); //水费账户
            tBAccountlist.add(tBsAssetAccount);
        }
        //电表账户
        tbs = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(entity.getBuildingCode(), WyTbEnum.TBS_ACCOUNT_TYPE_ELECT.getIntValue());
        if(CommonUtils.isEmpty(tbs)){
            TBsAssetAccount tBsAssetAccount = createTBsAssetAccount(entity,projectName);
            tBsAssetAccount.setType(WyTbEnum.TBS_ACCOUNT_TYPE_ELECT.getIntValue()); //电费账户
            tBAccountlist.add(tBsAssetAccount);
        }
        //本体基金账户
        tbs = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(entity.getBuildingCode(), WyTbEnum.TBS_ACCOUNT_TYPE_BT.getIntValue());
        if(CommonUtils.isEmpty(tbs)){
            TBsAssetAccount tBsAssetAccount = createTBsAssetAccount(entity,projectName);
            tBsAssetAccount.setType(WyTbEnum.TBS_ACCOUNT_TYPE_BT.getIntValue()); //本体基金账户
            tBAccountlist.add(tBsAssetAccount);
        }
        return tBAccountlist;
    }

    private TBsAssetAccount createTBsAssetAccount(TcBuilding entity,String projectName){
        TBsAssetAccount tBsAssetAccount = new TBsAssetAccount();
        tBsAssetAccount.setBuildingCode(entity.getBuildingCode());
        tBsAssetAccount.setAccountBalance(0.00); //创建账户时，余额为0
        tBsAssetAccount.setFullName(entity.getBuildingFullName());
        tBsAssetAccount.setProjectId(entity.getProjectId());
        tBsAssetAccount.setProjectName(projectName);
        tBsAssetAccount.setUseStatus(WyTbEnum.TBS_ACCOUNT_STATUS_START.getIntValue());  //默认创建账户时启用
        tBsAssetAccount.setCreateId(entity.getCreaterId());
        tBsAssetAccount.setCreateName(entity.getCreaterName());
        return tBsAssetAccount;
    }















    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public BaseDto loadBuildingByPickUpTree(String companyId, String projectId,String buildingCode, String custId) {
        if(!CommonUtils.onlyOneExist(projectId,buildingCode,custId)){
            return new BaseDto(new ArrayList<TcBuilding>(),null);
        }
        return new BaseDto(tcBuildingMapper.loadBuildingByPickUpTree(projectId, buildingCode, custId),null);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public BaseDto loadBuildingByProjectIdWithoutTree(String companyId, String projectId) {
        return new BaseDto(tcBuildingMapper.loadBuildingByProjectIdWithoutTree(projectId),null);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public List<TcBuilding> loadBuildingByProjectId(String companyId, String projectId) {
        return this.tcBuildingMapper.loadBuildingByProjectId(projectId);
    }






    /* (non-Javadoc)
     * @see com.everwing.coreservice.common.wy.service.project.TcBuildingService#initaccount(com.everwing.coreservice.common.wy.common.BusinessContext, java.lang.String, java.lang.String)
     */
    @Override
    public BaseDto initaccount(final WyBusinessContext ctx, final String projectId,
                               final String projectName) throws ECPBusinessException{
        MessageMap msgMap = new MessageMap();
        BaseDto baseDto = new BaseDto();
        final String initFlag=projectId+"_"+ctx.getCompanyId();
        if(projectMap.containsKey(initFlag)){
            msgMap.setFlag(MessageMap.INFOR_ERROR);
            msgMap.setMessage("项目["+projectName+"]正在初始化账户,请稍后再操作!");
            logger.info(String.format("当前时间 : %s ,异常  -> %s" ,CommonUtils.getDateStr(),msgMap.getMessage()));
            baseDto.setMessageMap(msgMap);
            return baseDto;
        }

        //由于产生的账号可能较多；所以这里才去异步的方式
        try {
            //将项目编号存在在
            projectMap.put(initFlag, initFlag);
            if(StringUtils.isBlank(projectId)){
                projectMap.remove(initFlag);
                msgMap.setFlag(MessageMap.INFOR_ERROR);
                msgMap.setMessage("传入的项目编号为空!");
                logger.info(String.format("当前时间 : %s ,异常  -> %s" ,CommonUtils.getDateStr(),msgMap.getMessage()));
                baseDto.setMessageMap(msgMap);
                return baseDto;
            }
            final List<TcBuilding> list = this.tcBuildingMapper.loadBuildingByProjectIdAndhousestore(projectId);
            if(org.apache.commons.collections.CollectionUtils.isEmpty(list)){
                projectMap.remove(initFlag);
                msgMap.setFlag(MessageMap.INFOR_ERROR);
                logger.info(String.format("当前时间 : %s ,异常  -> %s" ,CommonUtils.getDateStr(),msgMap.getMessage()));
                msgMap.setMessage("项目:["+projectName+"]下没有建筑;请检查");
                baseDto.setMessageMap(msgMap);
                return baseDto;
            }
            RemoteModelResult<Company> rslt =  this.companyApi.queryCompany(ctx.getCompanyId());
            if(!rslt.isSuccess() || rslt.getModel() == null){
                logger.warn("切换数据源失败。");
                return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"切换数据源失败。"));
            }
            final String companyId = rslt.getModel().getCompanyId();
            //这里启动一个线程防止建筑过多导致超时
            ThreadPoolUtils.getInstance().executeThread(new Runnable() {

                @Override
                public void run() {
                    // 切换数据源
                    WyBusinessContext.getContext().setCompanyId(companyId);
                    List<TBsAssetAccount> tBAccountlist = new ArrayList<>();
                    try {
                        for(TcBuilding tcBuilding:list){
                            tcBuilding.setCreaterId(ctx.getUserId());
                            tcBuilding.setCreaterName(ctx.getLoginName());
                            List<TBsAssetAccount> tblist = createTBsList(tcBuilding,projectName);
                            if(tblist.size()>0){
                                tBAccountlist.addAll(tblist);
                            }
                        }
                        sumbitTBsAssetAccount(tBAccountlist);
                        projectMap.remove(initFlag);
                    } catch (Exception e) {
                        projectMap.remove(initFlag);
                        logger.info(String.format("当前时间 : %s ,异常  -> %s" ,CommonUtils.getDateStr(),e.getMessage()));
                    }
                }
            });
            msgMap.setFlag(MessageMap.INFOR_SUCCESS);
            msgMap.setMessage("异步初始化账户开始,稍后请查看详情!");
            baseDto.setMessageMap(msgMap);
            return baseDto;

        } catch (Exception e) {
            projectMap.remove(initFlag);
            logger.info(String.format("当前时间 : %s ,异常  -> %s" ,CommonUtils.getDateStr(),e.getMessage()));
            throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);

        }

    }

    //提交新增账户
    private void sumbitTBsAssetAccount(List<TBsAssetAccount> tBAccountlist){
        int commitSize = 1000;//默认每次提交数量
        // 新增账户
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(tBAccountlist)) {
            int size = tBAccountlist.size();
            if (size <= commitSize) {
                this.tBsAssetAccountMapper.batchInsert(tBAccountlist);
            } else {
                int partSize = size / commitSize;
                for(int i = 0; i < partSize; i++) {
                    List<TBsAssetAccount> subList = tBAccountlist.subList(0, commitSize);
                    this.tBsAssetAccountMapper.batchInsert(subList);
                    logger.info(String.format("当前时间 : %s ,新增账户:分批提交-> %s" ,CommonUtils.getDateStr(),commitSize + "条数据"));

                    tBAccountlist.subList(0, commitSize).clear();
                    logger.info(String.format("当前时间 : %s ,剔除已经提交的数据后还剩余 -> %s" ,CommonUtils.getDateStr(),tBAccountlist.size() + "条数据"));
                }
                if(!tBAccountlist.isEmpty()){
                    this.tBsAssetAccountMapper.batchInsert(tBAccountlist);
                    logger.info(String.format("当前时间 : %s ,新增建筑:分批提交剩余 -> %s" ,CommonUtils.getDateStr(),tBAccountlist.size() + "条数据"));
                }
            }
        }
    }

    /**
     * 根据项目编号统计收费对象
     */
    @Override
    public BaseDto countIsChargeObjByProject(WyBusinessContext ctx,
                                             String projectId,String isChargerObj) throws ECPBusinessException{
        BaseDto baseDto = new BaseDto();
        MessageMap msgMap = new MessageMap();
        try {
//			String isChargeObj="Yes";
            Integer count = this.tcBuildingMapper.countIsChargeObjByProject(projectId, isChargerObj);
            msgMap.setFlag(MessageMap.INFOR_SUCCESS);
            baseDto.setMessageMap(msgMap);
            baseDto.setObj(count);
            return baseDto;
        } catch (Exception e) {
            logger.info(getLogStr(e.getMessage()));
            throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
        }
    }

    private String getLogStr(String error){
        return String.format("当前时间 : %s , 异常  -> %s" ,CommonUtils.getDateStr(),error);
    }

}

