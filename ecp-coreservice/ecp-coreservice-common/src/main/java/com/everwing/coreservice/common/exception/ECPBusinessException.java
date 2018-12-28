package com.everwing.coreservice.common.exception;

import com.everwing.coreservice.common.constant.ReturnCode;

public class ECPBusinessException extends RuntimeException implements ReturnCodeAware {

	private static final long serialVersionUID = -4762976047220373113L;

	private String returnCode;
	private String returnDescription;
	private Object[] args;

	public ECPBusinessException(ReturnCode returnCode) {
		super(returnCode.getCode() + " : " + returnCode.getDescription());
		this.returnCode = returnCode.getCode();
		this.returnDescription = returnCode.getDescription();
	}

	public ECPBusinessException(ReturnCode returnCode, Object... args) {
		super(returnCode.getCode() + getArgsStr(args));
		this.returnCode = returnCode.getCode();
		this.returnDescription = returnCode.getDescription();
		this.args = args;
	}

	public ECPBusinessException(Throwable cause, ReturnCode returnCode, Object... args) {
		super(returnCode.getCode() + getArgsStr(args));
		this.returnCode = returnCode.getCode();
		this.returnDescription = returnCode.getDescription();
		this.args = args;
	}

	public ECPBusinessException(Throwable cause, ReturnCode returnCode) {
		super(returnCode.getCode(), cause);
		this.returnCode = returnCode.getCode();
		this.returnDescription = returnCode.getDescription();
	}

	public ECPBusinessException(Throwable cause) {
		super(cause);
		if (cause instanceof ReturnCodeAware) {
			ReturnCodeAware exception = (ReturnCodeAware) cause;
			this.returnCode = exception.getErrorCode();
			this.returnDescription = exception.getErrorDescription();
			this.args = exception.getArgs();
		}
	}


	/**
	 * 支持异常链模式
	 * @param message
	 */
	public ECPBusinessException(String message) {
		super(message);
		this.returnCode = "EXCEPTION_RETURN_CODE_DEFAULT";
		this.returnDescription = message;
	}

	/**
	 * 支持异常链模式
	 * @param message
	 * @param cause
	 */
	public ECPBusinessException(String message,Throwable cause) {
		super(message,cause);
		if (cause instanceof ReturnCodeAware) {
			ReturnCodeAware exception = (ReturnCodeAware) cause;
			this.returnCode = exception.getErrorCode();
			this.returnDescription = exception.getErrorDescription();
			this.args = exception.getArgs();
		}
	}

	@Override
	public String getErrorCode() {
		return returnCode;
	}

	@Override
	public Object[] getArgs() {
		return args;
	}

	@Override
	public String getErrorDescription() {
		return returnDescription;
	}

	private static String getArgsStr(Object[] args) {
		String result;
		if (args != null) {
			StringBuffer sb = new StringBuffer(", args: ");
			for (Object arg : args) {
				sb.append(arg).append(", ");
			}
			result = sb.toString();
		} else {
			result = "";
		}
		return result;
	}

}
