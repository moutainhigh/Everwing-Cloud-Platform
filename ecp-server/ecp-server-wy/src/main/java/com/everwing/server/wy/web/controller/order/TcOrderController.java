package com.everwing.server.wy.web.controller.order;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.dto.OrderSearchDto;
import com.everwing.coreservice.common.wy.entity.order.TcOrder;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingList;
import com.everwing.coreservice.common.wy.entity.property.property.CustomerSearch;
import com.everwing.coreservice.wy.api.building.PropertyApi;
import com.everwing.coreservice.wy.api.building.TcBuildingApi;
import com.everwing.coreservice.wy.api.common.WyCommonApi;
import com.everwing.coreservice.wy.api.configuration.tbcassetacount.TBsAssetAccountApi;
import com.everwing.coreservice.wy.api.order.TcOrderCompleteApi;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 工单（服务请求）
 */
@Controller
@RequestMapping(value="/order")
@SuppressWarnings("rawtypes")
public class TcOrderController {
	@Autowired
	private TcOrderCompleteApi tcOrderApi;
	@Autowired
	private TBsAssetAccountApi tBsAssetAccountApi;
	@Autowired
	private PropertyApi propertyApi;
	@Autowired
	private TcBuildingApi tcBuildingApi;
	@Autowired
	private WyCommonApi wyCommonApi;

	@GetMapping("/custAndBuildingInfo")
	public @ResponseBody BaseDto custAndBuildingInfo(String custId, String custType,String buildingCode) {
		return BaseDtoUtils.getDto(wyCommonApi.custAndBuildingInfo(custId, custType, buildingCode));
	}

	@PostMapping("/listPageDatas")
	public @ResponseBody BaseDto listPageDatas(@RequestBody OrderSearchDto orderSearchDto) {
		RemoteModelResult<BaseDto> listPageDatas = tcOrderApi.listPageDatas(orderSearchDto);
		return BaseDtoUtils.getDto(listPageDatas);
	}

	@PostMapping("/updateOrder")
	public @ResponseBody BaseDto updateOrder(@RequestBody TcOrder order) {
		order.setUpdateBy(WyBusinessContext.getContext().getUserId());
		return BaseDtoUtils.getDto(tcOrderApi.updateOrder(order));
	}

	@PostMapping("/newOrders")
	public @ResponseBody BaseDto newOrders(@RequestBody TcOrder order) {
		if (StringUtils.isAnyBlank(order.getCustId(), order.getCustType())) {
			BaseDto baseDto = new BaseDto();
			MessageMap mm = new MessageMap();
			mm.setFlag(MessageMap.INFOR_ERROR);
			mm.setMessage("缺少必要参数");
			baseDto.setMessageMap(mm);
			return baseDto;
		}

		ArrayList<TcOrder> orderList = new ArrayList<TcOrder>();
		orderList.add(order);
		return BaseDtoUtils.getDto(tcOrderApi.newOrders(orderList));
	}

	@RequestMapping("/delete")
	public @ResponseBody BaseDto deleteOrders(String ids){
		return BaseDtoUtils.getDto(tcOrderApi.deleteOrders(ids));
	}

	@RequestMapping("/finish")
	public @ResponseBody BaseDto finishOrders(String ids){
		String updateBy= WyBusinessContext.getContext().getUserId();
		return BaseDtoUtils.getDto(tcOrderApi.finishOrders(ids,updateBy));
	}


	/**
	 * @description 客户分页条件查询
	 */
	@RequestMapping(value = "/listPageCustomerInEntery", method = RequestMethod.POST)
	public @ResponseBody
	BaseDto listPageCustomerInEntery(@RequestBody CustomerSearch customerSearch){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		RemoteModelResult<BaseDto> result = propertyApi.listPageCustomerInEntery(ctx.getCompanyId(),customerSearch);
		if(result.isSuccess()){
			baseDto = result.getModel();
		} else {
			mm.setFlag(MessageMap.INFOR_WARNING);
			mm.setMessage(result.getMsg());
		}
		baseDto.setMessageMap(mm);
		return baseDto;
	}


	/**
	 * @description 资产分页条件查询
	 */
	@RequestMapping(value = "/listPageBuildingInEntery",method = RequestMethod.POST)
	public @ResponseBody
	BaseDto listPageBuildingInEntery(@RequestBody CustomerSearch customerSearch){
		//查出客户列表
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		RemoteModelResult<BaseDto> result = propertyApi.listPageCustomerInEntery(ctx.getCompanyId(),customerSearch);
		if (result.isSuccess()) {
			baseDto.setMessageMap(mm);
		} else {
			mm.setFlag(MessageMap.INFOR_WARNING);
			mm.setMessage(result.getMsg());
			baseDto.setMessageMap(mm);
			return baseDto;
		}

		ArrayList<String> custIds = new ArrayList<String>();
		for(Object obj:result.getModel().getLstDto()){
			Map<String,Object> custMap = (Map<String, Object>) obj;
			custIds.add((String) custMap.get("id"));
		}
		// custIds.add("0278979a-931a-4027-9159-c0ecba7da419");
		customerSearch.setCustIds(custIds);

		//根据客户的id查出对应building
		RemoteModelResult<BaseDto> buildingResult = tcBuildingApi.listPageBuildingInEntery(ctx.getCompanyId(),customerSearch);

		List<TcBuildingList> buildingList = buildingResult.getModel().getLstDto();

		if (StringUtils.isNotBlank(customerSearch.getHouseNum())
				|| StringUtils.isNotBlank(customerSearch.getBuildingAddress())){//只显示该房屋编码的资产
			ArrayList<TcBuildingList> filteredBuildingList = new ArrayList<TcBuildingList>();
			for(TcBuildingList b : buildingList){
				if(b == null || CommonUtils.isEmpty(b.getId())) continue;
				if(StringUtils.isNotBlank(customerSearch.getHouseNum()) && customerSearch.getHouseNum().equalsIgnoreCase(b.getHouseCode())){
					filteredBuildingList.add(b);
				}else if(StringUtils.isNotBlank(customerSearch.getBuildingAddress()) && b.getBuildingFullName().contains(customerSearch.getBuildingAddress())){
					filteredBuildingList.add(b);
				}
			}
			buildingList = filteredBuildingList;
		}

		// 调用欠费计算API
		RemoteModelResult<BaseDto> arrearsResult = tBsAssetAccountApi.queryTotalArrears(buildingList);
		if(buildingResult.isSuccess() && arrearsResult.isSuccess()){
			//赋值新的LstDto
			buildingResult.getModel().setLstDto(arrearsResult.getModel().getLstDto());
			baseDto = buildingResult.getModel();
		}else{
			mm.setFlag(MessageMap.INFOR_WARNING);
			mm.setMessage(buildingResult.getMsg());
		}


		baseDto.setMessageMap(mm);
		return baseDto;
	}
}