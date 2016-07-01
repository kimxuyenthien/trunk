package de.gimik.app.allpresanapp.Utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtility {
	public static final SimpleDateFormat dateTimeParamFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//private static final SimpleDateFormat shortTimeParamFormat = new SimpleDateFormat("hh:mm a");
	public static final SimpleDateFormat shortTimeParamFormat = new SimpleDateFormat("HH:mm");
	public static final SimpleDateFormat longTimeParamFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	public static final SimpleDateFormat dateParamFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat headerDateParamFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
	public static final SimpleDateFormat GermanDateParamFormat = new SimpleDateFormat("dd.MM.yy HH:mm");
	public static final SimpleDateFormat GermanShortDateParamFormat = new SimpleDateFormat("dd.MM.yy");
	public static final SimpleDateFormat DBTimestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat Format_yyyyMMdd_HHmmss = new SimpleDateFormat("yyyyMMdd_HHmmss");

	public static Date parseDateTime(String text){
		if (text==null)
			return null;
		try {
			return dateTimeParamFormat.parse(text);
		} catch (ParseException pe) {
			return null;
		}
	}

    public static Date parseDBTimestamp(String timestamp) {
        try{
            return DBTimestampFormat.parse(timestamp);
        } catch(Exception ex){
			ex.printStackTrace();
            return null;
        }
    }
	
	public static String formatDateTime(Date dateTime){
		try{
			return dateTimeParamFormat.format(dateTime);
		} catch(Exception ex){
			ex.printStackTrace();
			return "";
		}
	}
	
	public static String formatShortTime(Date dateTime){
		try{
			return shortTimeParamFormat.format(dateTime);
		} catch(Exception ex){
			return "";
		}
	}
	
	public static String formatGermanTime(Date dateTime){
		try{
			return GermanDateParamFormat.format(dateTime);
		} catch(Exception ex){
			return "";
		}
	}
	
	public static String formatGermanShortDate(Date dateTime){
		try{
			return GermanShortDateParamFormat.format(dateTime);
		} catch(Exception ex){
			return "";
		}
	}
	
	public static Date parseDate(String text){
		try {
			return dateParamFormat.parse(text);
		} catch (ParseException pe) {
			return null;
		}
	}

	public static Date parseGermanShortDate(String text){
		try {
			return GermanShortDateParamFormat.parse(text);
		} catch (ParseException pe) {
			return null;
		}
	}

    public static Date parseDate(String text, String pattern){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            return formatter.parse(text);
        } catch (ParseException pe) {
            return null;
        }
    }
	
	public static Date parseDateFromTick(int tickInSecond){
		if (tickInSecond > 0) {
			Calendar calendar = Calendar.getInstance();
			long tick = (long) tickInSecond * 1000;

			calendar.setTimeInMillis(tick);
			return calendar.getTime();
		}

		return null;
	}
	
	public static String formatDate(Date dateTime){
		try{
			return dateParamFormat.format(dateTime);
		} catch(Exception ex){
			return "";
		}
	}
	
	public static String formatHeaderDate(Date dateTime){
		try{
			return headerDateParamFormat.format(dateTime);
		} catch(Exception ex){
			return "";
		}
	}
	
	public static long getTickOfDayStart(Date date){
		return parseDateTime(formatDate(date) + " 00:00:00").getTime();
	}


	public static  String formatFullDate(Date date, Locale locale){
		if (date==null)
			return "";
		if (locale==null)
			locale = Locale.US;
		DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, locale);
		return df.format(date);
	}

	public static String PrintStartTime(Date startTime, Date endTime){
		String time = "";
		if(startTime!=null){
			if (endTime != null){
                if (GermanShortDateParamFormat.format(startTime).equalsIgnoreCase(GermanShortDateParamFormat.format(endTime))){
                    time = String.format("%s - %s", longTimeParamFormat.format(startTime), shortTimeParamFormat.format(endTime));
                }
                else{
                    time = String.format("%s - %s", longTimeParamFormat.format(startTime), longTimeParamFormat.format(endTime));
                }
			}
			else{
				time = longTimeParamFormat.format(startTime);
			}
		}
		else {
			if (endTime !=null){
				time = longTimeParamFormat.format(endTime);
			}
		}

		if (time!=null && time.contains("00:00 - 00:00"))
			return "";
		return time;
	}
}
