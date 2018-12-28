package com.everwing.coreservice.wy.core.service.impl.TcConstruction;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.common.enums.WyEnum;
import com.everwing.coreservice.common.wy.entity.property.Engineering.Engineeringlog;
import com.everwing.coreservice.common.wy.entity.property.Engineering.TcConstruction;
import com.everwing.coreservice.common.wy.entity.property.Engineering.TcConstructionSearch;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.service.TcConstruction.TcConstructionService;
import com.everwing.coreservice.wy.dao.mapper.engineeringlog.EngineeringlogMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcBuildingMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcConstructionMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTimeComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName: TcConstructionServiceImpl
 * @Author:Ck
 * @Date: 2018/7/31
 **/

@Service("tcConstructionServiceImpl")
public class TcConstructionServiceImpl implements TcConstructionService {


    @Autowired
    private TcBuildingMapper TcBuildingMapper;

    @Autowired
    private TcConstructionMapper TcConstructionMapper;

    @Autowired
    private EngineeringlogMapper engineeringlogMapper;

    static Logger logger = LogManager.getLogger(TcConstructionServiceImpl.class);

    /**
     * 查询工程施工
     *
     * @param ctx
     * @param tcConstruction
     * @return
     */
    @Override
    public BaseDto loadbyConstructionlistPage(WyBusinessContext ctx, TcConstructionSearch tcConstruction) {
        List<TcConstruction> list = TcConstructionMapper.loadbyConstructionlistPage(tcConstruction);
   /*    if(org.apache.commons.collections.CollectionUtils.isNotEmpty(list)){
            for (TcConstruction  tcConstruction1 : list) {
                Date startDate = tcConstruction1.getStartDate();
                Date nowDate = new Date();
                int result = DateTimeComparator.getInstance().compare(startDate,nowDate);

                if(result <= 0){// 开工日期小于等于当前日期，施工中
                    tcConstruction1.setConstructionState("施工中");
                }else{
                    tcConstruction1.setConstructionState("未施工");
                }
            }
        }*/
        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(tcConstruction.getPage());
        return baseDto;
    }
    /**
     * 查询未关联水电工程施工
     *
     * @param ctx
     * @param tcConstruction
     * @return
     */
    @Override
    public BaseDto loadbyWaterElectlistPage(WyBusinessContext ctx, TcConstructionSearch tcConstruction) {
        List<TcConstruction> list = TcConstructionMapper.loadbyWaterElectlistPage(tcConstruction);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(tcConstruction.getPage());
        return baseDto;
    }


    /**
     * 查询历史施工
     *
     * @param ctx
     * @param tcConstruction
     * @return
     */
    @Override
    public BaseDto loadbyEhistorylistPage(WyBusinessContext ctx, TcConstructionSearch tcConstruction) {
        List<TcConstruction> list = TcConstructionMapper.loadbyEhistorylistPage(tcConstruction);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(tcConstruction.getPage());
        return baseDto;
    }

    /**
     * 删除工程施工
     *
     * @param id
     * @return
     */
    @Override
    public MessageMap delete(WyBusinessContext ctx, String id) {
        MessageMap messageMap = new MessageMap();
        TcConstruction  tcConstruction  = this.TcConstructionMapper.SeleteByID(id);
        this.TcConstructionMapper.deleteConstruction(id);

        //添加操作日志
        Engineeringlog engineeringlog = new Engineeringlog();
        engineeringlog.setEngineeringName(tcConstruction.getEngineeringName());
        engineeringlog.setConstructionAddr(tcConstruction.getConstructionAddr());
        engineeringlog.setEngineeringUnit(tcConstruction.getEngineeringUnit());
        engineeringlog.setEngineeringDirector(tcConstruction.getEngineeringDirector());
        engineeringlog.setHouseCodeNew(tcConstruction.getHouseCodeNew());
        engineeringlog.setBuildingid(id);
        engineeringlog.setModifyType("delete");
        engineeringlog.setOperator(ctx.getStaffName());
        this.engineeringlogMapper.insert(engineeringlog);
        return messageMap;
    }



    /**
     * 修改工程施工
     *
     * @param ctx
     * @param tcConstruction
     * @return
     */
    @Override
    public MessageMap modify(WyBusinessContext ctx, TcConstruction tcConstruction) {

        MessageMap messageMap = new MessageMap();
        if (tcConstruction.getHouseCodeNew() != null && !"".equals(tcConstruction.getHouseCodeNew())) {
            tcConstruction.setModifyId(tcConstruction.getHouseCodeNew());
        }
        this.TcConstructionMapper.modify(tcConstruction);

        // 判断是否在施工中
        Date startDate = tcConstruction.getStartDate();
        Date nowDate = new Date();
        int result = DateTimeComparator.getInstance().compare(startDate,nowDate);

        if(result <= 0){// 开工日期小于等于当前日期，施工中
            tcConstruction.setConstructionState("施工中");
        }else{
            tcConstruction.setConstructionState("未施工");
        }

        this.TcConstructionMapper.modify(tcConstruction);

        //添加操作日志
        Engineeringlog engineeringlog = new Engineeringlog();
        engineeringlog.setEngineeringName(tcConstruction.getEngineeringName());
        engineeringlog.setConstructionAddr(tcConstruction.getConstructionAddr());
        engineeringlog.setEngineeringUnit(tcConstruction.getEngineeringUnit());
        engineeringlog.setEngineeringDirector(tcConstruction.getEngineeringDirector());
        engineeringlog.setHouseCodeNew(tcConstruction.getHouseCodeNew());
        engineeringlog.setBuildingid(tcConstruction.getId());
        String dirtyinfo =  StringUtils.join(tcConstruction.getDirtyinfo(),",");
        engineeringlog.setModifyMatter(dirtyinfo);
        engineeringlog.setModifyType("edit");
        engineeringlog.setOperator(ctx.getStaffName());
        this.engineeringlogMapper.insert(engineeringlog);
        return messageMap;
    }


    /**
     * 修改状态为完工
     *
     * @param ctx
     * @param tcConstruction
     * @return
     */
    @Override
    public MessageMap reviseComplete(WyBusinessContext ctx, TcConstruction tcConstruction) {
        MessageMap messageMap = new MessageMap();
        if (tcConstruction.getHouseCodeNew() != null && !"".equals(tcConstruction.getHouseCodeNew())) {
            tcConstruction.setModifyId(tcConstruction.getHouseCodeNew());
        }
        this.TcConstructionMapper.reviseComplete(tcConstruction);
        return messageMap;
    }

    /**
     * 修改状态为暂停
     *
     * @param ctx
     * @param tcConstruction
     * @return
     */
    @Override
    public MessageMap suspend(WyBusinessContext ctx, TcConstruction tcConstruction) {
        MessageMap messageMap = new MessageMap();
        if (tcConstruction.getHouseCodeNew() != null && !"".equals(tcConstruction.getHouseCodeNew())) {
            tcConstruction.setModifyId(tcConstruction.getHouseCodeNew());
        }
        this.TcConstructionMapper.suspend(tcConstruction);
        return messageMap;
    }

    /**
     * 修改状态为施工中
     *
     * @param ctx
     * @param tcConstruction
     * @return
     */
    @Override
    public MessageMap startUp(WyBusinessContext ctx, TcConstruction tcConstruction) {
        MessageMap messageMap = new MessageMap();
        if (tcConstruction.getHouseCodeNew() != null && !"".equals(tcConstruction.getHouseCodeNew())) {
            tcConstruction.setModifyId(tcConstruction.getHouseCodeNew());
        }
        this.TcConstructionMapper.startUp(tcConstruction);
        return messageMap;
    }

    /**
     * 修改延期时间
     *
     * @param ctx
     * @param tcConstruction
     * @return
     */
    @Override
    public MessageMap editDelay(WyBusinessContext ctx, TcConstruction tcConstruction) {
        MessageMap messageMap = new MessageMap();
        this.TcConstructionMapper.editDelay(tcConstruction);
        return messageMap;
    }
    /**
     * 新增工程施工
     *
     * @param ctx
     * @param tcConstruction
     * @return
     */
    @Override
    public MessageMap add(WyBusinessContext ctx, TcConstruction tcConstruction) {
        MessageMap messageMap = new MessageMap();
        tcConstruction.setCompanyId(ctx.getCompanyId());
        // 生成建筑编码 : 规则系统自动生成，项目流水码（2位）+04+流水码（从00001开始，不限位数），如110400001
        String projectId = tcConstruction.getProjectId();
        String HouseCodeNew = projectId.substring(projectId.length() - 2) + WyEnum.CODE_TYPE_06.getStringValue();
        // 获取流水码 取最大加1
        String MaxCode = this.TcConstructionMapper.getCode(projectId);
        //自动补位
        MaxCode = StringUtils.leftPad(MaxCode, 5, "0");
        HouseCodeNew = HouseCodeNew + MaxCode;
        //UUID 在JAVA 中生成 下面还要用到
        String uuid = UUID.randomUUID().toString();
        tcConstruction.setId(uuid);
        tcConstruction.setHouseCodeNew(HouseCodeNew);
        // 判断是否在施工中
        Date startDate = tcConstruction.getStartDate();
        Date nowDate = new Date();
        int result = DateTimeComparator.getInstance().compare(startDate,nowDate);

                if(result <= 0){// 开工日期小于等于当前日期，施工中
                    tcConstruction.setConstructionState("施工中");
                }else{
                    tcConstruction.setConstructionState("未施工");
                }

        TcBuilding tcBuilding  =  new TcBuilding();
        tcBuilding.setId(uuid);
        tcBuilding.setBuildingType("Engineering");
        List<TcBuilding>  list  =  new ArrayList<>();
        list.add(tcBuilding);
        this.TcBuildingMapper.batchInsert(list);


        this.TcConstructionMapper.insert(tcConstruction);

        //添加操作日志
        Engineeringlog engineeringlog = new Engineeringlog();
        engineeringlog.setBuildingid(tcConstruction.getId());
        engineeringlog.setEngineeringName(tcConstruction.getEngineeringName());
        engineeringlog.setConstructionAddr(tcConstruction.getConstructionAddr());
        engineeringlog.setEngineeringUnit(tcConstruction.getEngineeringUnit());
        engineeringlog.setEngineeringDirector(tcConstruction.getEngineeringDirector());
        engineeringlog.setHouseCodeNew(tcConstruction.getHouseCodeNew());
        engineeringlog.setModifyType("insert");
        engineeringlog.setOperator(ctx.getStaffName());
        this.engineeringlogMapper.insert(engineeringlog);
        return  messageMap;
    }
}
