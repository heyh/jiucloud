package sy.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateKit {

	/** 日期格式 */
	private final static ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>(){
	    protected SimpleDateFormat initialValue() {
		return new SimpleDateFormat("yyyy-MM-dd");
	    }
	};
	
	/** 时间格式 */
	private final static ThreadLocal<SimpleDateFormat> timeFormat = new ThreadLocal<SimpleDateFormat>(){
	    protected SimpleDateFormat initialValue() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    }
	};

	private final static ThreadLocal<SimpleDateFormat> monthlyFormat = new ThreadLocal<SimpleDateFormat>(){
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM");
		}
	};
	/**  
	 * 获取当前时间:Date
	 */
	public static Date getDate(){
		return new Date();
	}
	
	/**  
	 * 获取当前时间:Calendar
	 */
	public static Calendar getCal(){
		return Calendar.getInstance();
	}

	public static String monthlyToStr(Date date){
		if(date != null)
			return monthlyFormat.get().format(date);
		return null;
	}

	/**  
	 * 日期转换为字符串:yyyy-MM-dd
	 */
	public static String dateToStr(Date date){
		if(date != null)
			return dateFormat.get().format(date);
		return null;
	}
	
	/**  
	 * 时间转换为字符串:yyyy-MM-dd HH:mm:ss
	 */
	public static String timeToStr(Date date){
		if(date != null)
			return timeFormat.get().format(date);
		return null;
	}
	
	/**  
	 * 字符串转换为日期:yyyy-MM-dd
	 */
	public static Date strToDate(String str){
		Date date = null;
		try {
			date = dateFormat.get().parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**  
	 * 字符串转换为时间:yyyy-MM-dd HH:mm:ss
	 */
	public static Date strToTime(String str){
		Date date = null;
		try {
			date = timeFormat.get().parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**  
	 * 友好的方式显示时间
	 */
	public static String friendlyFormat(String str){
		Date date = strToTime(str);
		if(date == null)
			return ":)";
		Calendar now = getCal();
		String time = new SimpleDateFormat("HH:mm").format(date);
		
		// 第一种情况，日期在同一天
		String curDate = dateFormat.get().format(now.getTime());
		String paramDate = dateFormat.get().format(date);
		if(curDate.equals(paramDate)){
			int hour = (int) ((now.getTimeInMillis() - date.getTime()) / 3600000);
			if(hour > 0)
				return time;
			int minute = (int) ((now.getTimeInMillis() - date.getTime()) / 60000);
			if (minute < 2)
				return "刚刚";
			if (minute > 30)
				return "半个小时以前";
			return minute + "分钟前";
		}
		
		// 第二种情况，不在同一天
		int days = (int) ((getBegin(getDate()).getTime() - getBegin(date).getTime()) / 86400000 );
		if(days == 1 )
			return "昨天 "+time;
		if(days == 2)
			return "前天 "+time;
		if(days <= 7)
			return days + "天前";
		return dateToStr(date);
	}
	
	/**  
	 * 返回日期的0点:2012-07-07 20:20:20 --> 2012-07-07 00:00:00
	 */
	public static Date getBegin(Date date){
		return strToTime(dateToStr(date)+" 00:00:00");
	}

    public static String getCurrentDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    public static Date strToDateOrTime(String str) {
        int strLength = str.trim().length();
        SimpleDateFormat strSdf = null;
        switch (strLength) {
            case 8:
                strSdf = new SimpleDateFormat("yyyyMMdd");
                break;
            case 10:
                strSdf = new SimpleDateFormat("yyyy-MM-dd");
                break;
            case 14:
                strSdf = new SimpleDateFormat("yyyyMMddHHmmss");
                break;
            case 19:
                strSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
            default:
                try {
                    throw new Exception("时间字符串错误：" + str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        try {
            return strSdf.parse(str.trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

	public static String StringToDate(String str) {

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			str = sdf1.format(sdf2.parse(str));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static long strDateToTimeStemp(String strDate) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		long timeStemp = -1;
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
			timeStemp = date.getTime()/1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timeStemp;

	}

	public static String lastMonth() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM");
		return format.format(c.getTime());
	}

	public static String currentMonth() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM");
		return format.format(c.getTime());
	}


	public static void main(String[] args) {
		System.out.println(currentMonth());
	}

}
