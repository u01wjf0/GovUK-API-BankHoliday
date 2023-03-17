package com.wfraser.ukgov.apis;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wfraser.ukgov.apis.constants.Regions;
import com.wfraser.ukgov.apis.exceptions.BankHolidayException;
import com.wfraser.ukgov.apis.models.Event;
import com.wfraser.ukgov.apis.models.Events;
import com.wfraser.ukgov.apis.models.Root;


public class BankHolidays  {
	
	
	private Root allHols = null;
	private GregorianCalendar nextLoadTime = new GregorianCalendar();
	private int daysBeforeReload = 0;
	private static BankHolidays instance = null;
	private Set<Regions> regions;
	
	/**
	 * Gets the current or a new instance of {@link #BankHolidays()} 
	 * 
	 * Because bank holidays do not change often, 
	 * a copy of the return is held for the given number of days 
	 * 
	 * @param refreshIntervalDays - Int value for refresh intervals
	 * @return
	 */
	public static BankHolidays getInstance(int refreshIntervalDays, Set<Regions> regions) {
		if(instance == null)
		{
			instance = new BankHolidays(refreshIntervalDays, regions);
			return instance;
		}
		if(instance.daysBeforeReload != refreshIntervalDays)
		{
			instance.daysBeforeReload = refreshIntervalDays;
			instance.regions = regions;
		}
		return instance;
	}
	
	/**
	 * 
	 * @param daysBeforeReload
	 */
	private BankHolidays(int daysBeforeReload, Set<Regions> regions) {
		this.daysBeforeReload = daysBeforeReload;
		this.regions = regions;
	}
	
	/**
	 * Blocking constructor
	 */
	private BankHolidays() {
		
	}

	/**
	 * 
	 * @throws IOException
	 */
	private void getData() throws IOException {
		
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
		    HttpGet httpGet = new HttpGet("https://www.gov.uk/bank-holidays.json");
		    HttpResponse response = httpClient.execute(httpGet);
		    
		    
			BufferedReader in = new BufferedReader(
					  new InputStreamReader(response.getEntity().getContent()));
					String inputLine;
					StringBuffer content = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
					    content.append(inputLine);
					}
					in.close();
			
			ObjectMapper om = new ObjectMapper();
			allHols = om.readValue(content.toString(), Root.class);
			nextLoadTime = new GregorianCalendar();
			nextLoadTime.add(Calendar.DATE, daysBeforeReload);
		}
		

	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private Events getAllDates() throws IOException {
		if(allHols == null || new GregorianCalendar().after(nextLoadTime)) {
			getData();
		}
		return allHols.getAllEvents((Regions[]) regions.toArray());
	}
	
	
	/**
	 * Checks if a provided date is a bank holiday 
	 * 
	 * @param date
	 * @return
	 * @throws BankHolidayException
	 */
	public boolean isDateABankHoliday(GregorianCalendar date) throws BankHolidayException {
		try {
		Events events = getAllDates();
		return events.isDateABankHoliday(date);
		} catch (Exception e ) {
			// convert all in to one type of exception
			throw new BankHolidayException(e);
		}
	}
	
	/**
	 * Checks if today is a bank holiday
	 * 
	 * @return
	 * @throws BankHolidayException
	 */
	public boolean isTodayABankHoliday() throws BankHolidayException {
		try {
		Events events = getAllDates();
		return events.isTodayABankHoliday();
		} catch (Exception e ) {
			// convert all in to one type of exception
			throw new BankHolidayException(e);
		}
	}
	
	/**
	 * Get's the details from a bank holiday in @type {@link com.wfraser.ukgov.apis.models.Event}
	 * 
	 * @param date - {@link java.util.GregorianCalendar}
	 * @return {@link com.wfraser.ukgov.apis.models.Event} with event details
	 * @throws BankHolidayException
	 */
	public Event getBankHolidayDetails(GregorianCalendar date) throws BankHolidayException {
		try {
		Events events = getAllDates();
		return events.getBankHolidayInfo(date);
		} catch (Exception e ) {
			// convert all in to one type of exception
			throw new BankHolidayException(e);
		}
	}

//	public static void main(String args[]) throws BankHolidayException 
//	{
//		BankHolidays bh = BankHolidays.getInstance(1);
//		System.out.println(bh.isDateABankHoliday( new GregorianCalendar( 2018, 0, 2) ));
////		System.out.println(events.isTodayABankHoliday());
//		System.out.println(bh.isTodayABankHoliday());
//		System.out.println(bh.getBankHolidayDetails(new GregorianCalendar( 2018, 0, 2)));
//	}

}