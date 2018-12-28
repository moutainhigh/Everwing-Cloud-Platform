package com.everwing.myexcel.exception;

/**
 * 异常处理
 */
public class MyExcelException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;

    public MyExcelException() {
        super();
    }

    public MyExcelException(String message) {
        super(message);
    }

    public MyExcelException(String code, String message) {
        super(message);
        this.code = code;
    }

    public MyExcelException(Throwable cause) {
        super(cause);
    }

    public MyExcelException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyExcelException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
