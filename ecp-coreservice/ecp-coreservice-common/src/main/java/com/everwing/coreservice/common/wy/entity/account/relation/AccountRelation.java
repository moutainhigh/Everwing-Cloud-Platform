package com.everwing.coreservice.common.wy.entity.account.relation;

import com.everwing.coreservice.common.BaseEntity;
import com.everwing.coreservice.common.wy.entity.account.asset.AssetAccount;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AccountRelation") //账户关系表
public class AccountRelation extends BaseEntity {

	private static final long serialVersionUID = -5015536241674541517L;

	private String accountRelationId; //关系id

    private String accountId; //账户id

    private String assetAccount; //资产账户id
    
    private AssetAccount account;//资产账户
    

    public AssetAccount getAccount() {
		return account;
	}

	public void setAccount(AssetAccount account) {
		this.account = account;
	}

	public String getAccountRelationId() {
        return accountRelationId;
    }

    public void setAccountRelationId(String accountRelationId) {
        this.accountRelationId = accountRelationId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAssetAccount() {
        return assetAccount;
    }

    public void setAssetAccount(String assetAccount) {
        this.assetAccount = assetAccount;
    }
}