package sy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class StringUtil {
	/**
	 * 锟斤拷锟斤拷锟斤拷址锟�
	 * 
	 * @param str
	 * @return String
	 */
	public static String doEmpty(String str) {
		return doEmpty(str, "");
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷址锟�
	 * 
	 * @param str
	 * @param defaultValue
	 * @return String
	 */
	public static String doEmpty(String str, String defaultValue) {
		if (str == null || str.equalsIgnoreCase("null")
				|| str.trim().equals("") || str.trim().equals("锟斤拷锟斤拷选锟斤拷")) {
			str = defaultValue;
		} else if (str.startsWith("null")) {
			str = str.substring(4, str.length());
		}
		return str.trim();
	}

	/**
	 * 锟斤拷选锟斤拷
	 */
	final static String PLEASE_SELECT = "锟斤拷选锟斤拷...";

	public static boolean notEmpty(Object o) {
		return o != null && !"".equals(o.toString().trim())
				&& !"null".equalsIgnoreCase(o.toString().trim())
				&& !"undefined".equalsIgnoreCase(o.toString().trim())
				&& !PLEASE_SELECT.equals(o.toString().trim());
	}

	public static boolean empty(Object o) {
		return o == null || "".equals(o.toString().trim())
				|| "null".equalsIgnoreCase(o.toString().trim())
				|| "undefined".equalsIgnoreCase(o.toString().trim())
				|| PLEASE_SELECT.equals(o.toString().trim());
	}

	public static boolean num(Object o) {
		int n = 0;
		try {
			n = Integer.parseInt(o.toString().trim());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean decimal(Object o) {
		double n = 0;
		try {
			n = Double.parseDouble(o.toString().trim());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (n > 0.0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 锟斤拷JID锟斤拷锟斤拷锟矫伙拷锟斤拷
	 * 
	 * @param Jid
	 * @return
	 */
	public static String getUserNameByJid(String Jid) {
		if (empty(Jid)) {
			return null;
		}
		if (!Jid.contains("@")) {
			return Jid;
		}
		return Jid.split("@")[0];
	}

	/**
	 * 
	 * 
	 * @param jidFor
	 *             
	 * @param userName
	 * @return
	 */
	public static String getJidByName(String userName, String jidFor) {
		if (empty(jidFor) || empty(jidFor)) {
			return null;
		}
		return userName + "@" + jidFor;
	}

	/**
	 * 锟斤拷锟矫伙拷锟斤拷锟絁ID,使锟斤拷默锟斤拷锟斤拷锟斤拷ahic.com.cn
	 * 
	 * @param userName
	 * @return
	 */
	public static String getJidByName(String userName) {
		String jidFor = "ahic.com.cn";
		return getJidByName(userName, jidFor);
	}

	/**
	 * 锟斤拷莞锟斤拷时锟斤拷锟街凤拷锟斤拷锟斤拷锟斤拷 锟斤拷 时 锟斤拷 锟斤拷
	 * 
	 * @param allDate
	 *            like "yyyy-MM-dd hh:mm:ss SSS"
	 * @return
	 */
	public static String getMonthTomTime(String allDate) {
		return allDate.substring(5, 19);
	}

	/**
	 * 锟斤拷莞锟斤拷时锟斤拷锟街凤拷锟斤拷锟斤拷锟斤拷 锟斤拷 时 锟斤拷 锟铰碉拷锟斤拷锟斤拷
	 * 
	 * @param allDate
	 *            like "yyyy-MM-dd hh:mm:ss SSS"
	 * @return
	 */
	public static String getMonthTime(String allDate) {
		return allDate.substring(5, 16);
	}
	
	 /** 
	  * 
	  * @param fileName 
	  * @return 
	  */  
	 public static String readTxtFile(File fileName)throws Exception{  
		  String result="";  
		  FileReader fileReader=null;  
		  BufferedReader bufferedReader=null;  
	  try{  
		   fileReader=new FileReader(fileName);  
		   
		  // BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file_url),"gbk"))
		   bufferedReader=new BufferedReader(fileReader);  
	  try{  
		     String read=null;  
		     while((read=bufferedReader.readLine())!=null){  
		       result=result+read+"\r\n";  
		      }  
		    }catch(Exception e){  
		      e.printStackTrace();  
		    }  
		  }catch(Exception e){  
		   e.printStackTrace();  
		  }finally{  
		   if(bufferedReader!=null){  
		    bufferedReader.close();  
		   }  
		   if(fileReader!=null){  
		    fileReader.close();  
		   }  
		  }  
		  return result;  
	  }

    public static boolean isNum(String str){
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }
}
