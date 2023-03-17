package com.wfraser.ukgov.apis.exceptions;

public class BankHolidayException extends Exception {

	public BankHolidayException(Exception e) {
		this.setStackTrace(e.getStackTrace());
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7298907424218245513L;

}
