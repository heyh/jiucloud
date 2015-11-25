package sy.util;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;

import sy.pageModel.Json;

public class Constants {


	public static String getId() {
		Date data = new Date();
		String timestr = data.toString();
		timestr = timestr.substring(4);// 将最前面的关于星期几的字符去掉
		String str1 = new String(" CST ");
		timestr = timestr.replaceAll(str1, " ");// 去掉“CST”字符和与它相邻的一个空格
		return timestr;
	}

	public static String getimageId(int len) {
		Calendar calendar = Calendar.getInstance();
		Long time = new Date().getTime()
				+ (calendar.get(Calendar.ZONE_OFFSET) + calendar
						.get(Calendar.DST_OFFSET)) / (60 * 1000);
		Constants c = new Constants();
		return time + c.randomString(len);
	}

	public String randomString(int length) {
		Random randGen = null;
		char[] numbersAndLetters = null;
		if (length < 1) {
			return null;
		}
		if (randGen == null) {
			randGen = new Random();
			numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
					+ "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
		}
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
			// randBuffer[i] = numbersAndLetters[randGen.nextInt(35)];
		}
		return new String(randBuffer);
	}

	public Long getimageIds() {
		Calendar calendar = Calendar.getInstance();
		Long time = new Date().getTime()
				+ (calendar.get(Calendar.ZONE_OFFSET) + calendar
						.get(Calendar.DST_OFFSET)) / (60 * 1000);
		return time;
	}

	public static long getToday(String time, String endtime) {
		long day = 0;
		try {
			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
					"yyyy-MM-dd");
			java.util.Date beginDate = format.parse(time);
			java.util.Date endDate = format.parse(endtime);
			day = (endDate.getTime() - beginDate.getTime())
					/ (24 * 60 * 60 * 1000);
		} catch (Exception ex) {
		}
		return day;
	}

	// 实现日期加一天的方法
	public static String addDay(String s, int n) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cd = Calendar.getInstance();
			cd.setTime(sdf.parse(s));
			cd.add(Calendar.DATE, n);// 增加一天
			// cd.add(Calendar.MONTH, n);//增加一个月
			return sdf.format(cd.getTime());
		} catch (Exception e) {
			return null;
		}
	}

	/*   *//**
	 * 产品分类数据
	 * 
	 * @return
	 */
	/*
	 * public static List<RecordModle> productDataType(){ List<RecordModle> m =
	 * new ArrayList<RecordModle>(); m.add(new RecordModle("tel","手机","1"));
	 * m.add(new RecordModle("pei","配件","2")); return m; }
	 */

	public static Date convertDateByStr(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = sdf.parse(str);
		} catch (Exception ex) {
		}
		return d;
	}

	/**
	 * 保留小数点位数
	 * 
	 * @param ws
	 * @return
	 */
	public static double setscale(int ws, double m) {
		BigDecimal b = new BigDecimal(m);
		double df = b.setScale(ws, BigDecimal.ROUND_HALF_UP).doubleValue();
		return df;
	}

	// 七大地区华北地区 华中地区 华东地区 华南地区 西北地区 东北地区 西南地区

	public static Json getSign(HttpServletRequest req) {
		Json json = new Json();
		BaseCode64 base64 = new BaseCode64();
		try {
			String sign = req.getParameter("sign");
			byte[] decodeStr = base64.decode(sign);
			sign = URLDecoder.decode(new String(decodeStr), "UTF-8");
			JSONObject o = JSONObject.parseObject(sign);
			json.setObj(o);
			json.setSuccess(true);
		} catch (Exception ex) {
			json.setMsg("非法参数..." + ex.getMessage());
			json.setSuccess(false);
			json.setCode(1002);
			ex.printStackTrace();
		}
		return json;
	}
	
	public static Json getSign(MultipartHttpServletRequest req) {
		Json json = new Json();
		BaseCode64 base64 = new BaseCode64();
		try {
			String sign = req.getParameter("sign");
			byte[] decodeStr = base64.decode(sign);
			sign = URLDecoder.decode(new String(decodeStr), "UTF-8");
			JSONObject o = JSONObject.parseObject(sign);
			json.setObj(o);
			json.setSuccess(true);
		} catch (Exception ex) {
			json.setMsg("非法参数..." + ex.getMessage());
			json.setSuccess(false);
			json.setCode(1002);
			ex.printStackTrace();
		}
		return json;
	}
	
	
}
