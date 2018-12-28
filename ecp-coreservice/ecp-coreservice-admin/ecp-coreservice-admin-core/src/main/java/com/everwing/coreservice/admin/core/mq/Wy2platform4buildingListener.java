package com.everwing.coreservice.admin.core.mq;/**
 * Created by wust on 2017/5/27.
 */

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.constant.Dict;
import com.everwing.coreservice.common.platform.entity.extra.IdGen;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.platform.entity.generated.AccountExample;
import com.everwing.coreservice.common.utils.RC4;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import com.everwing.coreservice.common.wy.common.enums.RabbitMQEnum;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.platform.api.AccountApi;
import com.everwing.coreservice.platform.api.CommonQueryApi;
import com.everwing.coreservice.platform.api.IdGenApi;
import com.everwing.coreservice.platform.util.MapperResources;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Function:物业平台推送过来的建筑数据
 * Reason:保存建筑数据，注册室内机账号
 * Date:2017/5/27
 * @author wusongti@lii.com.cn
 */
@Service("wy2platform4buildingListener")
public class Wy2platform4buildingListener extends MapperResources implements MessageListener {
    private static final Logger LOG = LoggerFactory.getLogger(BuildingGateListener.class);

    private static final int commitSize = 1000;

    @Autowired
    private AccountApi accountApi;

    @Autowired
    private CommonQueryApi commonQueryApi;

    @Autowired
    private IdGenApi idGenApi;

    static final String systemProp = "jZ5$x!6yeAo1Qe^r";

    private Map<String,TcBuilding> groupByBuildingCodeMaps = null;

    @Override
    public void onMessage(Message message) {
        try {
            JSONObject jsonObject = (JSONObject) JSONObject.parse(new String(message.getBody(), "UTF-8"));

            if (null == jsonObject) {
                LOG.info("消息队列: 传入建筑数据为空: {}", jsonObject);
                return;
            }

            String companyId = jsonObject.getString(RabbitMQEnum.companyId.name());
            this.groupByBuildingCode(companyId);

            String op = jsonObject.get(RabbitMQEnum.opt.name()).toString();
            if (op.equals(RabbitMQEnum.insert.name())) {
                process(jsonObject);
            } else if (op.equals(RabbitMQEnum.modify.name())) {
                process(jsonObject);
            } else if (op.equals(RabbitMQEnum.del.name())) {
                String buildingCode =  jsonObject.getString(RabbitMQEnum.data.name());
                List<String> buildingCodeList = new ArrayList<>(1);
                buildingCodeList.add(buildingCode);
                accountApi.cancelAccountByAccountCode(buildingCode);
                tcBuildingExtraMapper.batchDelete(buildingCodeList);
                LOG.info("删除建筑成功{}",buildingCode);
            }else if (op.equals(RabbitMQEnum.insertOrModify.name())) {
                process(jsonObject);
            }
        } catch (Exception e) {
            LOG.error("出现异常:{}",e);
        }
    }

    private void process(JSONObject jsonObject){
        JSONObject dataJsonObject = jsonObject.getJSONObject(RabbitMQEnum.data.name());
        TcBuilding source =  JSONObjectToTcBuilding(dataJsonObject);
        String buildingCode = source.getBuildingCode();
        if(this.groupByBuildingCodeMaps != null){
            if(this.groupByBuildingCodeMaps.containsKey(buildingCode)){
                List<TcBuilding> tcBuildingsModify = new ArrayList<>(1);
                tcBuildingsModify.add(source);
                tcBuildingExtraMapper.batchModify(tcBuildingsModify);
                LOG.info("更新建筑成功{}",buildingCode);
            }else{
                registerSNAccount(source);   // 注册室内机账号

                List<TcBuilding> tcBuildingsNew = new ArrayList<>(1);
                tcBuildingsNew.add(source);
                tcBuildingExtraMapper.batchInsert(tcBuildingsNew);

                LOG.info("新建建筑成功{}",buildingCode);
            }
        }else{
            registerSNAccount(source);   // 注册室内机账号

            List<TcBuilding> tcBuildingsNew = new ArrayList<>(1);
            tcBuildingsNew.add(source);
            tcBuildingExtraMapper.batchInsert(tcBuildingsNew);

            LOG.info("新建建筑成功{}",buildingCode);
        }
    }

    private TcBuilding JSONObjectToTcBuilding(JSONObject jsonObject){
        if(jsonObject == null){
            return null;
        }
        return jsonObject.toJavaObject(TcBuilding.class);
    }

    /**
     * 根据建筑编码分组建筑
     * @return
     */
    private Map<String,TcBuilding> groupByBuildingCode(String companyId){

        TcBuildingSearch tcBuildingSearch = new TcBuildingSearch();
        tcBuildingSearch.setCompanyId(companyId);
        List<TcBuilding> tcBuildingLists = tcBuildingExtraMapper.findByCondition(tcBuildingSearch);
        if(CollectionUtils.isNotEmpty(tcBuildingLists)){
            int len = tcBuildingLists.size();
            groupByBuildingCodeMaps = new HashMap<>(len);
            for(int i = 0;i<len;i++){
                TcBuilding tcBuilding = tcBuildingLists.get(i);
                String key = tcBuilding.getBuildingCode();
                groupByBuildingCodeMaps.put(key,tcBuilding);
            }
        }
        return groupByBuildingCodeMaps;
    }




    /**
     * 注册室内机账号
     * @param tcBuilding
     */
    private void registerSNAccount(TcBuilding tcBuilding){
        if (LookupItemEnum.buildingType_house.getStringValue().equals(tcBuilding.getBuildingType())) {
            RemoteModelResult<IdGen> remoteModelResult = idGenApi.queryMaxId(Dict.ACCOUNT_TYPE_HOUSE.getIntValue());
            if (remoteModelResult.isSuccess()) {
                IdGen idGen = remoteModelResult.getModel();
                Account account = new Account();
                account.setType(Dict.ACCOUNT_TYPE_HOUSE.getIntValue());
                account.setSourceId(tcBuilding.getId());
                account.setAccountCode(tcBuilding.getHouseCodeNew());
                account.setAccountName("sn" + idGen.getId());
                account.setPassword(Base64.encodeBase64String(RC4.encry_RC4_string("XH@341", systemProp).getBytes()));
                account.setCompanyId(tcBuilding.getCompanyId());

                AccountExample example = new AccountExample();
                example.createCriteria().andAccountCodeEqualTo(tcBuilding.getBuildingCode());
                RemoteModelResult<Account> accountRemoteModelResult = commonQueryApi.selectOneByExample(Account.class, example);
                if (accountRemoteModelResult.isSuccess()) {
                    Account existAccount = accountRemoteModelResult.getModel();
                    if (existAccount != null) {
                        return;
                    }

                    RemoteModelResult<Account> accountRemoteModelResult1 = accountApi.register(account, "", "");
                    if (!accountRemoteModelResult1.isSuccess()) {
                        LOG.info("注册室内机账号失败，buildingCode{}，平台返回的消息{}",tcBuilding.getBuildingCode(),accountRemoteModelResult1.getMsg());
                    }else{
                        LOG.info("注册室内机账号成功，buildingCode{}，室内机账号{}",tcBuilding.getBuildingCode(),account.getAccountName());
                    }
                }else{
                    LOG.info("注册室内机账号时查询平台账号接口出现异常，buildingCode{}，平台返回的消息{}",tcBuilding.getBuildingCode(),accountRemoteModelResult.getMsg());
                }
            } else {
                LOG.info("注册室内机账号时获取账号名称失败，buildingCode{}，平台返回的消息{}",tcBuilding.getBuildingCode(),remoteModelResult.getMsg());
            }
        }
    }
}