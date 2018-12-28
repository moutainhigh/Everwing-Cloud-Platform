package com.everwing.coreservice.common.wy.fee.dto;

/**
 * 前台操作封装类
 *
 * @author DELL shiny
 * @create 2018/8/23
 */
public class FrontOperaDto {

    private String companyId;

    private AcOrderDto acOrderDto;

    private AcBusinessOperaDetailDto businessOperaDetailDto;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public AcOrderDto getAcOrderDto() {
        return acOrderDto;
    }

    public void setAcOrderDto(AcOrderDto acOrderDto) {
        this.acOrderDto = acOrderDto;
    }

    public AcBusinessOperaDetailDto getBusinessOperaDetailDto() {
        return businessOperaDetailDto;
    }

    public void setBusinessOperaDetailDto(AcBusinessOperaDetailDto businessOperaDetailDto) {
        this.businessOperaDetailDto = businessOperaDetailDto;
    }
}
