package com.everwing.coreservice.common.exception;

public interface ReturnCodeAware {
	public String getErrorCode();
	public String getErrorDescription();
	public Object[] getArgs();
}
