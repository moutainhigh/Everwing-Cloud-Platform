package com.everwing.coreservice.wy.core.request;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="Info")
public class Info {
	private String state;
	private String code;
	private Object info;
	private List<?> permissions;
	
    public String getState()
    {
        return state;
    }
    public void setState(String state)
    {
        this.state = state;
    }
    public String getCode()
    {
        return code;
    }
    public void setCode(String code)
    {
        this.code = code;
    }
    public Object getInfo()
    {
        return info;
    }
    public void setInfo(Object info)
    {
        this.info = info;
    }
    public List<?> getPermissions()
    {
        return permissions;
    }
    public void setPermissions(List<?> permissions)
    {
        this.permissions = permissions;
    }
}
