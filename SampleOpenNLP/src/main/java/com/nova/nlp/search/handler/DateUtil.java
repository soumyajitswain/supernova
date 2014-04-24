package com.nova.nlp.search.handler;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private static final String[] PATTERNS = 
			new String[]{"yyyy-MM-dd","yyyyMMdd"};
	public static String parseDate(String dateStr, 
			        String[] patterns) {
		Date date = null; 
		SimpleDateFormat parser = new SimpleDateFormat();
		parser.setLenient(false);
		ParsePosition pos = new ParsePosition(0);
		for(String pattern: patterns) {
			parser.applyPattern(pattern);
			pos.setIndex(0);
			String str = dateStr;
			date = parser.parse(str,pos);
			if (date != null && pos.getIndex() == str.length()) {
				break;
			}

		}
		return date==null?dateStr:new java.sql.Date(date.getTime()).toString();
	}

	public static String parseDate(String  dateStr) {
		return parseDate(dateStr,PATTERNS);
	}
}
