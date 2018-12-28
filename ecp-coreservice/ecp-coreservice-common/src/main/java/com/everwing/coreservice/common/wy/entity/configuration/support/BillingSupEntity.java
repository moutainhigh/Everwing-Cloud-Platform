package com.everwing.coreservice.common.wy.entity.configuration.support;

import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccount;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.owed.TBsOwedHistory;
import com.everwing.coreservice.common.wy.entity.configuration.rebilling.TBsRebillingInfo;

import java.io.Serializable;
import java.util.List;

public class BillingSupEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -558992248181960672L;

	private List<TBsChargeBillHistory> insertList;
	private List<TBsChargeBillHistory> updateList;
	
	private List<TBsAssetAccount> updateAccountList;
	private List<TBsAssetAccount> insertAccountList;
	
	private List<TBsAssetAccountStream> updateStreamList;
	private List<TBsAssetAccountStream> insertStreamList;
	
	private List<TBsOwedHistory> insertOhList;
	private List<TBsOwedHistory> updateOhList;
	
	private List<TBsRebillingInfo> insertInfoList;
		

	public List<TBsChargeBillHistory> getInsertList() {
		return insertList;
	}

	public List<TBsAssetAccount> getUpdateAccountList() {
		return updateAccountList;
	}

	public void setUpdateAccountList(List<TBsAssetAccount> updateAccountList) {
		this.updateAccountList = updateAccountList;
	}

	public List<TBsAssetAccount> getInsertAccountList() {
		return insertAccountList;
	}

	public void setInsertAccountList(List<TBsAssetAccount> insertAccountList) {
		this.insertAccountList = insertAccountList;
	}

	public List<TBsAssetAccountStream> getUpdateStreamList() {
		return updateStreamList;
	}

	public void setUpdateStreamList(List<TBsAssetAccountStream> updateStreamList) {
		this.updateStreamList = updateStreamList;
	}

	public List<TBsAssetAccountStream> getInsertStreamList() {
		return insertStreamList;
	}

	public void setInsertStreamList(List<TBsAssetAccountStream> insertStreamList) {
		this.insertStreamList = insertStreamList;
	}

	public void setInsertList(List<TBsChargeBillHistory> insertList) {
		this.insertList = insertList;
	}

	public List<TBsChargeBillHistory> getUpdateList() {
		return updateList;
	}

	public void setUpdateList(List<TBsChargeBillHistory> updateList) {
		this.updateList = updateList;
	}

	public BillingSupEntity() {
		super();
	}

	public BillingSupEntity(List<TBsChargeBillHistory> insertList,
			List<TBsChargeBillHistory> updateList) {
		super();
		this.insertList = insertList;
		this.updateList = updateList;
	}

	public List<TBsOwedHistory> getInsertOhList() {
		return insertOhList;
	}

	public void setInsertOhList(List<TBsOwedHistory> insertOhList) {
		this.insertOhList = insertOhList;
	}

	public List<TBsOwedHistory> getUpdateOhList() {
		return updateOhList;
	}

	public void setUpdateOhList(List<TBsOwedHistory> updateOhList) {
		this.updateOhList = updateOhList;
	}

	public List<TBsRebillingInfo> getInsertInfoList() {
		return insertInfoList;
	}

	public void setInsertInfoList(List<TBsRebillingInfo> insertInfoList) {
		this.insertInfoList = insertInfoList;
	}
	
	

	
	
	
	
	
}
