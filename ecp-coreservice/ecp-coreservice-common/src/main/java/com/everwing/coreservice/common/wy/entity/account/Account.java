package com.everwing.coreservice.common.wy.entity.account;

import com.everwing.coreservice.common.BaseEntity;
import com.everwing.coreservice.common.wy.entity.account.asset.AssetAccount;
import com.everwing.coreservice.common.wy.entity.account.relation.AccountRelation;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Account") //账户表
public class Account extends BaseEntity {

	private static final long serialVersionUID = -1406048694290338656L;

	private String accountId;  //账户id

    private String accountNum; //账户编号

    private Byte isBankCard; //是否绑定银行卡

    private String projectId; //项目Id
    
    private String projectName;//**项目名称

    private Double disposableBalance; //可支配余额

    private Double totalBalance; // 总余额

    private String custId; //客户id

    private String enterpriseId; //企业客户id
    
    private Double totalBalanceOfDelinquentAccounts;//**欠费账户总余额（用于前台显示）
    
    private List<AccountRelation> accountRelations;//包含资产信息的账户关系
    
//    private List<AdvancePaymentsDetails> advancePaymentsDetails;//支付明细
    
    private List<AssetAccount> assetAccountList;//资产账户/押金账户集合
    
    
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Double getTotalBalanceOfDelinquentAccounts() {
		return totalBalanceOfDelinquentAccounts;
	}

	public void setTotalBalanceOfDelinquentAccounts(
			Double totalBalanceOfDelinquentAccounts) {
		this.totalBalanceOfDelinquentAccounts = totalBalanceOfDelinquentAccounts;
	}

	/*public List<AdvancePaymentsDetails> getAdvancePaymentsDetails() {
		return advancePaymentsDetails;
	}

	public void setAdvancePaymentsDetails(
			List<AdvancePaymentsDetails> advancePaymentsDetails) {
		this.advancePaymentsDetails = advancePaymentsDetails;
	}*/

	public List<AccountRelation> getAccountRelations() {
		return accountRelations;
	}

	public void setAccountRelations(List<AccountRelation> accountRelations) {
		this.accountRelations = accountRelations;
	}

	public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public Byte getIsBankCard() {
        return isBankCard;
    }

    public void setIsBankCard(Byte isBankCard) {
        this.isBankCard = isBankCard;
    }

    

    public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public Double getDisposableBalance() {
        return disposableBalance;
    }

    public void setDisposableBalance(Double disposableBalance) {
        this.disposableBalance = disposableBalance;
    }

    public Double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(Double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

	public List<AssetAccount> getAssetAccountList() {
		return assetAccountList;
	}

	public void setAssetAccountList(List<AssetAccount> assetAccountList) {
		this.assetAccountList = assetAccountList;
	}
}