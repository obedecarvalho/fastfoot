package com.fastfoot.service.util;

import java.text.DecimalFormat;
import java.util.Date;

import com.ibm.icu.text.SimpleDateFormat;

public class FormatadorUtil {
	
	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");
	
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	public static String formatarDecimal(Double number) {
		if (number == null) return null;
		return DECIMAL_FORMAT.format(number);
	}
	
	public static String formatarData(Date date) {
		if (date == null) return null;
		return DATE_FORMAT.format(date);
	}

}
