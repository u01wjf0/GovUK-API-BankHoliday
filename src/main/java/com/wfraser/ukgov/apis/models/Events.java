package com.wfraser.ukgov.apis.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 
 * @author u01wjf0
 *
 */
public class Events extends ArrayList<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5718483620802485950L;

	public boolean isTodayABankHoliday() {
		GregorianCalendar today = new GregorianCalendar();
		return isDateABankHoliday(today);
	}
	
	public boolean isDateABankHoliday(GregorianCalendar date) {
		for(Event e : this)
		{
			if(e.date.get(Calendar.YEAR) == date.get((Calendar.YEAR)) &&
					e.date.get(Calendar.MONTH) == date.get((Calendar.MONTH)) &&
					e.date.get(Calendar.DATE) == date.get((Calendar.DATE))) {
				return true;
			}
		}
		return false;
	}
	
	public Event getBankHolidayInfo(GregorianCalendar date) {
		for(Event e : this)
		{
			if(e.date.get(Calendar.YEAR) == date.get((Calendar.YEAR)) &&
					e.date.get(Calendar.MONTH) == date.get((Calendar.MONTH)) &&
					e.date.get(Calendar.DATE) == date.get((Calendar.DATE))) {
				return e;
			}
		}
		return null;
	}

}
