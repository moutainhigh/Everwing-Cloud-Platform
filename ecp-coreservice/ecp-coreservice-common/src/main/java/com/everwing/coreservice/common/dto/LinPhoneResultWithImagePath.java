package com.everwing.coreservice.common.dto;

/**
 * 带图片路径的返回
 *
 * @author DELL shiny
 * @create 2017/7/14
 */
public class LinPhoneResultWithImagePath extends LinphoneResult{

    public LinPhoneResultWithImagePath(Object data,String imgBasePath){
        super(data);
        this.imgBasePath=imgBasePath;
    }

    private String imgBasePath;

    public String getImgBasePath() {
        return imgBasePath;
    }

    public void setImgBasePath(String imgBasePath) {
        this.imgBasePath = imgBasePath;
    }
}
