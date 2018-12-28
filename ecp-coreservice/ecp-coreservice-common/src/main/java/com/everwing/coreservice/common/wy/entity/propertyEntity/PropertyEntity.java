/**
 * @Title: propertyService.java
 * @Package com.flf.request
 * @Description: TODO
 * Copyright: Copyright (c) 2011 
 * Company:武汉闻风多奇软件开发有限公司
 * 
 * @author wangtao
 * @date 2015-6-12 上午11:12:40
 * @version V1.0
 */

package com.everwing.coreservice.common.wy.entity.propertyEntity;

import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNew;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @ClassName: propertyService
 * @Description: TODO
 * @author wangyang
 * @date 2015-6-12 上午11:12:40
 *
 */
//物业服务
@XmlRootElement(name = "propertyService") 
public class PropertyEntity {
	private List<PersonCustNew> personCusts;//客户信息
//	private List<CarInfo> carInfos;//车辆信息
//	private List<HouseNew> houses;//房屋信息
//	private List<StallNew> stalls;//车位信息
//	private List<StoreNew> stores;//商铺信息
//	private List<Entrance> entrances;//门禁卡信息
//	private List<Caraccesscard> caraccesscards;//停车卡信息
//	private List<NewLease> leases;//停车卡信息
//	private List<PetRegistration> pets;//宠物信息
	/*public List<NewLease> getLeases() {
		return leases;
	}

	public void setLeases(List<NewLease> leases) {
		this.leases = leases;
	}

	public List<PetRegistration> getPets() {
		return pets;
	}

	public void setPets(List<PetRegistration> pets) {
		this.pets = pets;
	}

	
	public List<Entrance> getEntrances() {
		return entrances;
	}

	public void setEntrances(List<Entrance> entrances) {
		this.entrances = entrances;
	}

	public List<Caraccesscard> getCaraccesscards() {
		return caraccesscards;
	}

	public void setCaraccesscards(List<Caraccesscard> caraccesscards) {
		this.caraccesscards = caraccesscards;
	}

	
	public List<StoreNew> getStores() {
		return stores;
	}

	public void setStores(List<StoreNew> stores) {
		this.stores = stores;
	}

	public List<StallNew> getStalls() {
		return stalls;
	}

	public void setStalls(List<StallNew> stalls) {
		this.stalls = stalls;
	}

	public List<HouseNew> getHouses() {
		return houses;
	}

	public void setHouses(List<HouseNew> houses) {
		this.houses = houses;
	}

	public List<CarInfo> getCarInfos() {
		return carInfos;
	}

	public void setCarInfos(List<CarInfo> carInfos) {
		this.carInfos = carInfos;
	}*/

	public List<PersonCustNew> getPersonCusts() {
		return personCusts;
	}

	public void setPersonCusts(List<PersonCustNew> personCusts) {
		this.personCusts = personCusts;
	}
	
}
