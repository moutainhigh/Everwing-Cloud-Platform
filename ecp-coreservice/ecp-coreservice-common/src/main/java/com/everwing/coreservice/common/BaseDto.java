package com.everwing.coreservice.common;

import com.everwing.coreservice.common.utils.CommonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @param <T>
 * @param <E>
 */
public class BaseDto<T,E> implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<T> lstDto;
	private List<E> lstDetailDto;
    private Page page;
    private MessageMap messageMap;
    private T t;
    private E e;
    private Object obj;

    public List<T> getLstDto() {
        return lstDto;
    }

    public void setLstDto(List<T> lstDto) {
        this.lstDto = lstDto;
    }

    public List<E> getLstDetailDto() {
        return lstDetailDto;
    }

    public void setLstDetailDto(List<E> lstDetailDto) {
        this.lstDetailDto = lstDetailDto;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public MessageMap getMessageMap() {
        return messageMap;
    }

    public void setMessageMap(MessageMap messageMap) {
        this.messageMap = messageMap;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public E getE() {
        return e;
    }

    public void setE(E e) {
        this.e = e;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
    
    /**
     * 无参构造方法
     */
    public BaseDto(){}
    
    /**
     * 构造方法: 传入集合,集合分页
     */
    public BaseDto(List<T> lstDto,Page page){
    	this.lstDto = lstDto;
    	this.page = page;
    }
    
    /**
     * 构造方法: 传入单个对象,构造集合
     */
    public BaseDto(T t){
    	if(null == this.lstDto){
    		this.lstDto = new ArrayList<T>();
    	}
    	lstDto.add(t);
    }
    
    public BaseDto(MessageMap msgMap){
    	if(CommonUtils.isEmpty(msgMap))
    		this.messageMap = new MessageMap(null, MessageMap.INFOR_SUCCESS);
    	this.messageMap = msgMap;
    }
}
