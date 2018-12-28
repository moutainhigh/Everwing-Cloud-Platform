package com.everwing.coreservice.wy.core.service.impl.TcSurfaceWater;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.WyEnum;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeterSearch;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterlog;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.service.TcSurfaceWater.TcSurfaceWaterService;
import com.everwing.coreservice.wy.dao.mapper.annex.AnnexMapper;
import com.everwing.coreservice.wy.dao.mapper.materlog.WaterlogMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcSurfaceWaterMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.everwing.coreservice.wy.dao.mapper.property.TcBuildingMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("tcSurfaceWaterSericeImpl")
public class TcSurfaceWaterSericeImpl implements TcSurfaceWaterService {

    @Autowired
    private TcSurfaceWaterMapper  tcSurfaceWaterMapper;

    @Autowired
    private TcBuildingMapper TcBuildingMapper;

    @Autowired
    private WaterlogMapper  waterlogMapper;


    @Autowired
    private AnnexMapper annexMapper;
    static Logger logger = LogManager.getLogger(TcSurfaceWaterSericeImpl.class);

    /**
     * 分页查询水表信息
     *
     * @param
     * @return
     */
    @Override
    public BaseDto listPageWaterMeterInfos(WyBusinessContext ctx, TcWaterMeterSearch tcWaterMeter) {
        List<TcWaterMeter> list =tcSurfaceWaterMapper.listPageWaterMeterInfos(tcWaterMeter);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(tcWaterMeter.getPage());
        return baseDto ;
    }

    /**
     * 水表结构图
     *
     * @param
     * @return
     */
    @Override
    public BaseDto listPageWaterTree(WyBusinessContext ctx, TcWaterMeterSearch tcWaterMeter) {
        List<TcWaterMeter> list =tcSurfaceWaterMapper.listPageWaterTree(tcWaterMeter);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(tcWaterMeter.getPage());
        return baseDto ;
    }
    /**
     * 查询父表编码为非终端表
     *
     * @param
     * @return
     */
    @Override
    public BaseDto listPageParentCodeInfos(WyBusinessContext ctx, TcWaterMeterSearch tcWaterMeter) {
        List<TcWaterMeter> list =tcSurfaceWaterMapper.listPageParentCodeInfos(tcWaterMeter);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(tcWaterMeter.getPage());
        return baseDto ;
    }


    /**
     * 删除水表信息
     *
     * @param id
     * @return
     */
    @Override
    public MessageMap delete(WyBusinessContext ctx, String id) {
        MessageMap messageMap = new MessageMap();
        TcWaterMeter   tcWaterMeter=this.tcSurfaceWaterMapper.SeleteByID(id);
        this.tcSurfaceWaterMapper.deleteSurface(id);

        //添加操作日志
        TcWaterlog  tcWaterlog  =  new TcWaterlog();
        tcWaterlog.setCode(tcWaterMeter.getCode());
        tcWaterlog.setWaterId(id);
        tcWaterlog.setModifyType("delete");
        tcWaterlog.setOperator(ctx.getStaffName());
        this.waterlogMapper.insert(tcWaterlog);
        return messageMap;
    }


    /**
     * 修改水表信息
     *
     * @param ctx
     * @param
     * @return
     */
    @Override
    public MessageMap modify(WyBusinessContext ctx, TcWaterMeter tcWaterMeter) {
        MessageMap messageMap = new MessageMap();
        if (tcWaterMeter.getCode() != null && !"".equals(tcWaterMeter.getCode())) {
            tcWaterMeter.setModifyId(tcWaterMeter.getCode());
        }
        this.tcSurfaceWaterMapper.modify(tcWaterMeter);


        //添加操作日志
        TcWaterlog  tcWaterlog  =  new TcWaterlog();
        tcWaterlog.setWaterId(tcWaterMeter.getId());
        String dirtyinfo =  StringUtils.join(tcWaterMeter.getDirtyinfo(),",");
        tcWaterlog.setModifyMatter(dirtyinfo);
        tcWaterlog.setModifyType("edit");
        tcWaterlog.setOperator(ctx.getStaffName());
        this.waterlogMapper.insert(tcWaterlog);
        return messageMap;
    }


    /**
     * 新增水表信息
     *
     * @param ctx
     * @param
     * @return
     */
    @Override
    public MessageMap add(WyBusinessContext ctx, TcWaterMeter  tcWaterMeter) {
        MessageMap messageMap = new MessageMap();
        tcWaterMeter.setCompanyCode(ctx.getCompanyCode());
        // 生成建筑编码 : 规则系统自动生成，项目流水码（2位）+04+流水码（从00001开始，不限位数），如110400001
        String projectId = tcWaterMeter.getProjectId();
        String newCoding = projectId.substring(projectId.length() - 2) + WyEnum.CODE_TYPE_07.getStringValue();
        // 获取流水码 取最大加1
        String MaxCode = this.tcSurfaceWaterMapper.getCode(projectId);
        //自动补位
        MaxCode = StringUtils.leftPad(MaxCode, 5, "0");
        newCoding = newCoding + MaxCode;
        //UUID 在JAVA 中生成 下面还要用到
        String uuid = UUID.randomUUID().toString();
        tcWaterMeter.setId(uuid);
        tcWaterMeter.setCode(newCoding);


        String id = CommonUtils.getUUID();
        if (CommonUtils.isNotEmpty(tcWaterMeter.getAnnexs())) {
            for (Annex annex : tcWaterMeter.getAnnexs()) {
                annex.setRelationId(id);
                annex.setAnnexTime(CommonUtils.getDateStr());
                annexMapper.insertAnnex(annex);
            }
        }

        TcBuilding tcBuilding  =  new TcBuilding();
        tcBuilding.setId(uuid);
        tcBuilding.setBuildingType("WaterMeter");
        List<TcBuilding>  list  =  new ArrayList<>();
        list.add(tcBuilding);
        this.TcBuildingMapper.batchInsert(list);
        this.tcSurfaceWaterMapper.insert(tcWaterMeter);

        //添加操作日志
        TcWaterlog  tcWaterlog  =  new TcWaterlog();
        tcWaterlog.setWaterId(tcWaterMeter.getId());
        tcWaterlog.setModifyType("insert");
        tcWaterlog.setOperator(ctx.getStaffName());
        this.waterlogMapper.insert(tcWaterlog);
        return  messageMap;
    }
}
