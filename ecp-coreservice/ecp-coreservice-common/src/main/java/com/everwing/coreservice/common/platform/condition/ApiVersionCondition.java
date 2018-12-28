package com.everwing.coreservice.common.platform.condition;

import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

    private final static Pattern VERSION_PREFIX_PATTERN=Pattern.compile("v(\\d\\.\\d)/");

    private double apiVersion;

    public ApiVersionCondition(double apiVersion){
        this.apiVersion=apiVersion;
    }

    @Override
    public ApiVersionCondition combine(ApiVersionCondition apiVersionCondition) {
        return new ApiVersionCondition(apiVersionCondition.getApiVersion());
    }

    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest httpServletRequest) {
        Matcher matcher=VERSION_PREFIX_PATTERN.matcher(httpServletRequest.getPathInfo());
        if(matcher.find()){
            Integer version=Integer.valueOf(matcher.group(1));
            if(version>=this.apiVersion){
                return this;
            }
        }
        return null;
    }

    @Override
    public int compareTo(ApiVersionCondition apiVersionCondition, HttpServletRequest httpServletRequest) {
        //优先匹配最新的版本号
        return (int) (apiVersionCondition.getApiVersion()-this.apiVersion);
    }

    public double getApiVersion() {
        return apiVersion;
    }
}
