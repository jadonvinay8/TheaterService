package com.capgemini.TheaterService.beans;

public class ErrorResponse {

	private String errorMsg;
	private String code;
	private String cause;
	
	public ErrorResponse() {
		// Default Constructor
	}

	public ErrorResponse(String errorMsg, String cause, String code) {
		this.errorMsg = errorMsg;
		this.code = code;
		this.cause = cause;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}
	
}
