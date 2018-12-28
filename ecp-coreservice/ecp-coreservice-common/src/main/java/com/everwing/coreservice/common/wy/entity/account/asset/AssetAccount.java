package com.everwing.coreservice.common.wy.entity.account.asset;

import com.everwing.coreservice.common.BaseEntity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AssetAccount") //资产账户表
public class AssetAccount extends BaseEntity {

	private static final long serialVersionUID = -3669459202713611418L;

	private String assetAccountId; //资产账户id

    private String assetAccountName; //资产账户名称

    private String assetNum; //资产编号

    private String buildingId; //建筑结构id

    private String fullName; //房屋全名
    
    private String areaTypeId; //面积类型

    private String areaTypeName; //面积类型名称
    
    private String houseAddress;//房屋地址

    private String assetAccountNum; //资产账户编号

    private Byte isBankCard; //是否绑定银行卡
    
    private String bankAccount; //银行卡

    private String projectId; //项目Id

    private Double assetAccountBalance; //资产账户余额

    private Byte assetAccountType; //账户类型（0资产账户1押金账户）
    
    private double totalDeposits;//押金总额
    
    private double totalRefundableDeposit;//可退还押金总额
    
//    private BuildingStructureNew buildingStructureNew;//建筑结构
    
//    private ChargeItemNew chargeItem;//**收费项目
    
//    private List<AdvancePaymentsDetails> advancePaymentsDetails;//支付明细
    
    private String name;//账户名称
    
    private String custId;//账户id
    
    private double generalBalance;//通用余额
    
    private String companyName;	//物业公司名称
    
    private Double addPrice;//充值金额 王洲 2015.12.31
    
    private Double disSum;//账户详情可抵扣余额总和
    
    private String paymentType;//支付方式0现金1刷卡2微信
    
//    private List<PaymentDetails> paymentDetails; //收支记录
    
    private String depositNum;//押金单号
    
    private String ciName;//收费项目名
    
    private String chargeTypeId;//收费类型id
    
    private Double disposableBalance;//账户明细可抵扣余额
    
    private String assetAccountDetailId;//账户明细id
    
    private String chargeTypeName;//收费类型名称
    
    private String type;//缴费类型的标记  "05"表示从物业服务传过来的充值
    
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAssetAccountDetailId() {
		return assetAccountDetailId;
	}

	public void setAssetAccountDetailId(String assetAccountDetailId) {
		this.assetAccountDetailId = assetAccountDetailId;
	}

	public Double getDisposableBalance() {
		return disposableBalance;
	}

	public void setDisposableBalance(Double disposableBalance) {
		this.disposableBalance = disposableBalance;
	}

	public String getChargeTypeName() {
		return chargeTypeName;
	}

	public void setChargeTypeName(String chargeTypeName) {
		this.chargeTypeName=chargeTypeName;
	}

	public String getChargeTypeId() {
		return chargeTypeId;
	}

	public void setChargeTypeId(String chargeTypeId) {
		this.chargeTypeId = chargeTypeId;
	}

	public String getDepositNum() {
		return depositNum;
	}

	public void setDepositNum(String depositNum) {
		this.depositNum = depositNum;
	}

	public double getGeneralBalance() {
		return generalBalance;
	}

	public void setGeneralBalance(double generalBalance) {
		this.generalBalance = generalBalance;
	}

/*	public ChargeItemNew getChargeItem() {
		return chargeItem;
	}

	public void setChargeItem(ChargeItemNew chargeItem) {
		this.chargeItem = chargeItem;
	}*/

	public double getTotalDeposits() {
		return totalDeposits;
	}

	public void setTotalDeposits(double totalDeposits) {
		this.totalDeposits = totalDeposits;
	}

	public double getTotalRefundableDeposit() {
		return totalRefundableDeposit;
	}

	public void setTotalRefundableDeposit(double totalRefundableDeposit) {
		this.totalRefundableDeposit = totalRefundableDeposit;
	}

	/*public List<AdvancePaymentsDetails> getAdvancePaymentsDetails() {
		return advancePaymentsDetails;
	}

	public void setAdvancePaymentsDetails(
			List<AdvancePaymentsDetails> advancePaymentsDetails) {
		this.advancePaymentsDetails = advancePaymentsDetails;
	}

	public BuildingStructureNew getBuildingStructureNew() {
		return buildingStructureNew;
	}

	public void setBuildingStructureNew(BuildingStructureNew buildingStructureNew) {
		this.buildingStructureNew = buildingStructureNew;
	}*/

	public String getAssetAccountId() {
        return assetAccountId;
    }

    public void setAssetAccountId(String assetAccountId) {
        this.assetAccountId = assetAccountId;
    }

    public String getAssetAccountName() {
        return assetAccountName;
    }

    public void setAssetAccountName(String assetAccountName) {
        this.assetAccountName = assetAccountName;
    }

    public String getAssetNum() {
        return assetNum;
    }

    public void setAssetNum(String assetNum) {
        this.assetNum = assetNum;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAssetAccountNum() {
        return assetAccountNum;
    }

    public void setAssetAccountNum(String assetAccountNum) {
        this.assetAccountNum = assetAccountNum;
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

	public Double getAssetAccountBalance() {
        return assetAccountBalance;
    }

    public void setAssetAccountBalance(Double assetAccountBalance) {
        this.assetAccountBalance = assetAccountBalance;
    }

    public Byte getAssetAccountType() {
        return assetAccountType;
    }

    public void setAssetAccountType(Byte assetAccountType) {
        this.assetAccountType = assetAccountType;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Double getAddPrice() {
		return addPrice;
	}

	public void setAddPrice(Double addPrice) {
		this.addPrice = addPrice;
	}

	public Double getDisSum() {
		return disSum;
	}

	public void setDisSum(Double disSum) {
		this.disSum = disSum;
	}

	public String getHouseAddress() {
		return houseAddress;
	}

	public void setHouseAddress(String houseAddress) {
		this.houseAddress = houseAddress;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getAreaTypeId() {
		return areaTypeId;
	}

	public String getAreaTypeName() {
		return areaTypeName;
	}

	public void setAreaTypeId(String areaTypeId) {
		this.areaTypeId = areaTypeId;
	}

	public void setAreaTypeName(String areaTypeName) {
		this.areaTypeName = areaTypeName;
	}

	public String getCiName() {
		return ciName;
	}

	public void setCiName(String ciName) {
		this.ciName = ciName;
	}

	/*public List<PaymentDetails> getPaymentDetails() {
		return paymentDetails;
	}

	public void setPaymentDetails(List<PaymentDetails> paymentDetails) {
		this.paymentDetails = paymentDetails;
	}*/

}