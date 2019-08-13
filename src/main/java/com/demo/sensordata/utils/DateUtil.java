package com.demo.sensordata.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Slf4j
public class DateUtil {
	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

	public static Date convertToDate(String dateStr) {
		Date date = null;
		try {
			dateStr = dateStr.split("\\+")[0].replace("T", " ");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = format.parse(dateStr);
		} catch (Exception e) {
			logger.error("Cannot parse date : " + dateStr);
		}
		return date;
	}

	public static String getDateStr(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDate = dateFormat.format(date);
		return strDate.replace(" ", "T");
	}
}
