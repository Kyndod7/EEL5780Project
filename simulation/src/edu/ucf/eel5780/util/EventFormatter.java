package edu.ucf.eel5780.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class EventFormatter extends Formatter {

	public static final String SEPARATOR = ",";
	
	@Override
	public String format(LogRecord arg0) {
		StringBuffer sb = new StringBuffer();
		
		sb.append(getTimestamp(arg0.getMillis()));
		sb.append(SEPARATOR);
		sb.append(arg0.getLevel());
		sb.append(SEPARATOR);
		sb.append(arg0.getMessage());
		sb.append("\n");
		
		return sb.toString();
	}
	
	private String getTimestamp(long millisecs) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(millisecs);
		return sdf.format(date);
	}
	
}
