package com.wfraser.ukgov.apis.models;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 
 * @author u01wjf0
 *
 */
public class Event {
	public String title;
	public GregorianCalendar date;
	public String notes;
	public boolean bunting;
	
	@Override
	public String toString() {
		return "Bank Holiday: " + title + " - " + date.get(Calendar.DATE) + "\\" + (date.get(Calendar.MONTH)+1) + "\\" + date.get(Calendar.YEAR) + '\n'
				+ (bunting ? "Bunting is required" : "Bunting is not required") + (notes.length() > 0 ? " - " + notes : "");
	}
}
