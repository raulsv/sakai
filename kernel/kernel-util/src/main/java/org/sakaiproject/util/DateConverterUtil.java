/**********************************************************************************
 * $URL$
 * $Id$
 **********************************************************************************
 *
 * Copyright (c) 2017 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.opensource.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/
package org.sakaiproject.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;


import lombok.extern.slf4j.Slf4j;

//REVISAR COMENTARIO!!!!
/**
 * Performs date conversion respecting l10n.<br>
 */
@Slf4j
public final class DateConverterUtil {

	private DateConverterUtil() {
	}

	public static Date convertToUserZone(Date dateIn, TimeZone timeZoneServer) {
		if(dateIn == null || timeZoneServer == null) {
			return dateIn;
		}
		
		LocalDateTime ldt = LocalDateTime.ofInstant(dateIn.toInstant(), timeZoneServer.toZoneId());
		Calendar calendar = new GregorianCalendar(ldt.getYear(), ldt.getMonthValue()-1, ldt.getDayOfMonth(), ldt.getHour(), ldt.getMinute(), ldt.getSecond()); 
				
		return calendar.getTime();
	}
	
	public static Date convertToServerZone(String pattern,Date dateIn, TimeZone timeZoneOrig) {
		if(dateIn == null || timeZoneOrig == null || StringUtils.isBlank(pattern)) {
			return dateIn;
		}

		DateFormat orig = new SimpleDateFormat(pattern, Locale.ROOT);
		orig.setTimeZone(timeZoneOrig);
		DateFormat serverF = new SimpleDateFormat(pattern, Locale.ROOT);
		serverF.setTimeZone(TimeZone.getDefault());

		Date serverDate = null;
		try {
			serverDate = serverF.parse(serverF.format(orig.parse(DateFormatterUtil.format(dateIn, pattern, Locale.ROOT))));
		} catch (ParseException e) {
			log.warn("Error parsing the date {} to the server time zone", dateIn);
		}
	    		
		return serverDate;
	}
	
	/**
	 * Formats the date input to String using the format given.
	 * 
	 * @param inputDate
	 *            The date that needs to be formatted.
	 * @param format
	 *            The given date-time format.
	 * @param locale
	 *            The given locale.
	 * @throws ParseException
	 * 			If throws a parse exception then returns the SHORT format by default (MM/dd/yyyy hh:mm a)
	 */
	/*public static String format(Date inputDate, String format, Locale locale) {
		if(inputDate == null){
			return null;
		}

		try {
			return new SimpleDateFormat(format, locale)
					.format(inputDate);
		} catch(Exception ex) {
			return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US)
					.format(inputDate)
					.replace(",",""); // FIX JDK8 -> JDK9
		}
	}*/
}
