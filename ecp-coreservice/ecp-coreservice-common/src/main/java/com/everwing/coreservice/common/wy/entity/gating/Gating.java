package com.everwing.coreservice.common.wy.entity.gating;

import com.everwing.coreservice.common.Page;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Gate
 * <p>Description: 门控机设备信息表 </p>
 * @date 2017年4月21日10:56:58
 */
@XmlRootElement(name = "Gating")  //门控机表
public class Gating implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6746066734962447813L;
	//主键id
	private String id;
	//	设备码
    private String equipmentNum;
    //	门控机code
    private String gatingCode;
    //	设备名称
    private String equipmentName;
    //	设备型号
    private String equipmentModel;
    //	设备sn
    private String equipmentSn;
    //	批次编号
    private String batchNummer;
    //	出厂日期
    private Date manufactureDate;
    //	出厂日期(输出用)
    private String manufactureTime;
    //	报废日期
    private Date scrapTime;
    //	报废原因
    private String scrapCause;
    //	质保年限
    private Integer qualityTerm;
    //	使用年限
    private Integer employTerm;
    //	当前门控机的状态（未销售，已销售，正在使用，维修中，报废）
    private String facilityState;
    //	质保开始日期
    private Date qualityTimeStart;
    //	质保结束日期
    private Date qualityTimeEnd;
    //	质保开始日期（输出用）
    private String qualityStartTime;
    //	质保结束日期（输出用）
    private String qualityEndTime;
    //	销售合同ID
    private String marketId;
    //	采购订单ID
    private String purchaseId;
    //	生产厂商
    private String productionFirm;
    //	生产地址
    private String productionSite;
    //	位置
    private String district;
    //	二维码
    private String twoDimensionCode;
    //	门控机开门状态（1开门0关门）
    private int openGatingState;
    //	是否为围墙机(0不是1是)
    private int isWallGating;
    //	所属项目名
    private String employProject;
    
    private String companyId;
    
    //	后台列表用序号
    private int num;
    
    //	后台批量录入数量
    private int countequipmentnum;
    
    //	后台状态查询用字段
    private String statenum;
    
    private String projectId;// 项目id
    
    private String accountNum;//门控机账号
    
    private String password;	//密码

	private String accountName; //账号名
	
	private String gateAddress; //安装地址

	private Integer onlineState; //在线状态 0在线1离线

	private Integer videosState; //视频开启状态 0开启1关闭

	private String version;//当前门控机版本号

	public String getPassword() {
		return password;
	}
	public String getGateAddress() {
		return gateAddress;
	}
	public void setGateAddress(String gateAddress) {
		this.gateAddress = gateAddress;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public int getIsWallGating() {
		return isWallGating;
	}
	public void setIsWallGating(int isWallGating) {
		this.isWallGating = isWallGating;
	}
	public int getOpenGatingState() {
		return openGatingState;
	}
	public void setOpenGatingState(int openGatingState) {
		this.openGatingState = openGatingState;
	}
	public Date getQualityTimeStart() {
		return qualityTimeStart;
	}
	public void setQualityTimeStart(Date qualityTimeStart) {
		this.qualityTimeStart = qualityTimeStart;
	}

	private Page page;

    public Page getPage() {
		if(page==null)
			page = new Page();
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}

    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEquipmentNum() {
		return equipmentNum;
	}
	public void setEquipmentNum(String equipmentNum) {
		this.equipmentNum = equipmentNum;
	}
	
	public String getGatingCode() {
		return gatingCode;
	}
	public void setGatingCode(String gatingCode) {
		this.gatingCode = gatingCode;
	}
	public String getBatchNummer() {
		return batchNummer;
	}
	public void setBatchNummer(String batchNummer) {
		this.batchNummer = batchNummer;
	}
	public String getMarketId() {
		return marketId;
	}
	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}
	public String getPurchaseId() {
		return purchaseId;
	}
	public void setPurchaseId(String purchaseId) {
		this.purchaseId = purchaseId;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getTwoDimensionCode() {
		return twoDimensionCode;
	}
	public void setTwoDimensionCode(String twoDimensionCode) {
		this.twoDimensionCode = twoDimensionCode;
	}
	public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentModel() {
        return equipmentModel;
    }

    public void setEquipmentModel(String equipmentModel) {
        this.equipmentModel = equipmentModel;
    }


    public Date getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Date manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

	public Date getScrapTime() {
        return scrapTime;
    }

    public void setScrapTime(Date scrapTime) {
        this.scrapTime = scrapTime;
    }

    public String getScrapCause() {
        return scrapCause;
    }

    public void setScrapCause(String scrapCause) {
        this.scrapCause = scrapCause;
    }

    public Integer getQualityTerm() {
        return qualityTerm;
    }

    public void setQualityTerm(Integer qualityTerm) {
        this.qualityTerm = qualityTerm;
    }

    public Integer getEmployTerm() {
        return employTerm;
    }

    public void setEmployTerm(Integer employTerm) {
        this.employTerm = employTerm;
    }

    public String getFacilityState() {
        return facilityState;
    }

    public void setFacilityState(String facilityState) {
		this.facilityState = facilityState;
	}
	public String getProductionFirm() {
        return productionFirm;
    }

    public void setProductionFirm(String productionFirm) {
        this.productionFirm = productionFirm;
    }

    public String getProductionSite() {
        return productionSite;
    }

    public void setProductionSite(String productionSite) {
        this.productionSite = productionSite;
    }
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public Date getQualityTimeEnd() {
		return qualityTimeEnd;
	}
	public void setQualityTimeEnd(Date qualityTimeEnd) {
		this.qualityTimeEnd = qualityTimeEnd;
	}
	public String getQualityStartTime() {
		return qualityStartTime;
	}
	public void setQualityStartTime(String qualityStartTime) {
		this.qualityStartTime = qualityStartTime;
	}
	public String getQualityEndTime() {
		return qualityEndTime;
	}
	public void setQualityEndTime(String qualityEndTime) {
		this.qualityEndTime = qualityEndTime;
	}
	public String getEmployProject() {
		return employProject;
	}
	public void setEmployProject(String employProject) {
		this.employProject = employProject;
	}
	public String getManufactureTime() {
		return manufactureTime;
	}
	public void setManufactureTime(String manufactureTime) {
		this.manufactureTime = manufactureTime;
	}
	public String getStatenum() {
		return statenum;
	}
	public void setStatenum(String statenum) {
		this.statenum = statenum;
	}
	public int getCountequipmentnum() {
		return countequipmentnum;
	}
	public void setCountequipmentnum(int countequipmentnum) {
		this.countequipmentnum = countequipmentnum;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getEquipmentSn() {
		return equipmentSn;
	}
	public void setEquipmentSn(String equipmentSn) {
		this.equipmentSn = equipmentSn;
	}
	public String getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Integer getOnlineState() {
		return onlineState;
	}

	public void setOnlineState(Integer onlineState) {
		this.onlineState = onlineState;
	}

	public Integer getVideosState() {
		return videosState;
	}

	public void setVideosState(Integer videosState) {
		this.videosState = videosState;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Gating [id=" + id + ", equipmentNum=" + equipmentNum
				+ ", gatingCode=" + gatingCode + ", equipmentName="
				+ equipmentName + ", equipmentModel=" + equipmentModel
				+ ", equipmentSn=" + equipmentSn + ", batchNummer="
				+ batchNummer + ", manufactureDate=" + manufactureDate
				+ ", manufactureTime=" + manufactureTime + ", scrapTime="
				+ scrapTime + ", scrapCause=" + scrapCause + ", qualityTerm="
				+ qualityTerm + ", employTerm=" + employTerm
				+ ", facilityState=" + facilityState + ", qualityTimeStart="
				+ qualityTimeStart + ", qualityTimeEnd=" + qualityTimeEnd
				+ ", qualityStartTime=" + qualityStartTime
				+ ", qualityEndTime=" + qualityEndTime + ", marketId="
				+ marketId + ", purchaseId=" + purchaseId + ", productionFirm="
				+ productionFirm + ", productionSite=" + productionSite
				+ ", district=" + district + ", twoDimensionCode="
				+ twoDimensionCode + ", openGatingState=" + openGatingState
				+ ", isWallGating=" + isWallGating + ", employProject="
				+ employProject + ", companyId=" + companyId + ", num=" + num
				+ ", countequipmentnum=" + countequipmentnum + ", statenum="
				+ statenum + ", projectId=" + projectId + ", accountNum="
				+ accountNum + ", password=" + password + ", accountName="
				+ accountName + ", gateAddress=" + gateAddress + "onlineState="+onlineState+"videosState="+videosState+"version="+version+", page="
				+ page + "]";
	}
    
}