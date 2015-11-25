package sy.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

//import sy.model.PUsers;
import sy.pageModel.Json;

import com.alibaba.fastjson.JSONObject;

public class Constant {

	public static final String SERVERHOST = "http://115.29.246.117:90";
	/**
	 * 登录
	 */
	public static final String includePatternURL = "/costController/addCostPage,/costController/updateCostPage,/costController/delCost,/costController/upSort,/costController/downSort";

	public static String JPUSHMESSAGETYPE = "jpush_message_type";

	public interface JPUSHMESSAGE {
		public int MESG = 0; // 系统通知
		public int USRMSG = 1;// 用户数据推送
		public int FENXIANG = 2;// 分享
	}

	/**
	 * @ClassName: VisitEnum
	 * @Description: 访问类型
	 * @author william shao
	 * @date 2014年4月3日 下午12:24:30
	 *
	 */
	public interface VisitEnum {
		public int IOS = 0;
		public int ANDROID = 1;
	}

	/**
	 * @ClassName: ContentTypeEnum
	 * @Description: 文章发布类型,0图文，1，图片列表，2文字列表 3、微视、4、静态栏目 如（便民服务） 5、调查、6、留言、7、微发布
	 * @author william shao
	 * @date 2014年4月3日 下午4:07:15
	 *
	 */
	public interface EditionEnum {

		// 资讯类
		public int PICANDCONTENT = 0;
		public int PICLIST = 1;
		public int CONTENTLIST = 2;

		// 功能类
		public int CONTENTTV = 3;
		public int STILL = 4;
		public int SURVEY = 5;
		public int LEMSG = 6;
		public int WSEND = 7;
	}

	/**
	 * 状态 1开始 0结束 ,2待开始 值参考
	 */
	public interface OnLineInteractiveStatus {
		public int BEGIN = 1;
		public int END = 0;
		public int WAIT = 2;
	}

	/**
	 * 
	 * @ClassName: OpTypeEnum
	 * @Description: TODO(用于定义日志来操作类型 0：登录 1:访问 2:评论 3:收藏 4:调查 )
	 * @author william shao
	 * @date 2014年4月3日 下午4:27:59
	 *
	 */
	public interface OpTypeEnum {
		public int LOGIN = 0;
		public int VISIT = 1;
		public int COMMENT = 2;
		public int ADDCOLLECT = 3;
		public int SURVEY = 4;
	}

	/**
	 * 0 未置顶,1置顶
	 * 
	 * @ClassName: IsTopEnum
	 * @Description: TODO(文章是否置顶值)
	 * @author william shao
	 * @date 2014年4月5日 上午11:04:44
	 *
	 */
	public interface IsTopEnum {
		public int NOTOP = 0;
		public int TOP = 1;
	}

	/**
	 * @ClassName: CommentEnum
	 * @Description: TODO(是否允许被评论,0可以不被评论，1评论)
	 * @author william shao
	 * @date 2014年4月5日 上午11:18:50
	 *
	 */
	public interface CommentEnum {
		public int YES = 1;
		public int NO = 0;
	}

	/**
	 * 
	 * @ClassName: IsDelEnum
	 * @Description: TODO(通用删除标识 ， 1正常,0删除)
	 * @author william shao
	 * @date 2014年4月5日 下午12:47:45
	 *
	 */
	public interface IsDelEnum {
		public int YES = 1;
		public int NO = 0;
	}

	/**
	 *
	 * @ClassName: IsLoginVote
	 * @Description: TODO( 是否允许未登录投票 ,1：允许未登录投票，0不允许未登录投票)
	 * @author william shao
	 * @date 2014年4月5日 下午12:49:57
	 *
	 */
	public interface IsLoginVote {
		public int YES = 1;
		public int NO = 0;
	}

	/**
	 *
	 * @ClassName: IsLoginVote
	 * @Description: TODO( 是否允许重复投票 ,1：允许重复投票，0不允许重复投票)
	 * @author william shao
	 * @date 2014年4月5日 下午12:49:57
	 *
	 */
	public interface AllowDuplicateVote {
		public int YES = 1;
		public int NO = 0;
	}

	/**
	 * @ClassName: OptionsEnum
	 * @Description: TODO(是否多选 1多选，0单选)
	 * @author william shao
	 * @date 2014年4月5日 下午12:55:27
	 *
	 */
	public interface OptionsEnum {
		public int YES = 1;
		public int NO = 0;
	}

	/**
	 * 
	 * @ClassName: OitypeEnum
	 * @Description: TODO(问卷类型0热点、1娱乐、2生活、3其他)
	 * @author william shao
	 * @date 2014年4月5日 下午12:59:10
	 *
	 */
	public interface OitypeEnum {
		public int RD = 0;
		public int YL = 1;
		public int SH = 2;
		public int QT = 3;
	}

	/**
	 * 
	 * @ClassName: IsAudit
	 * @Description: TODO(是否审核,Y:1审核,0未审核,包括文章、评论需要审)
	 * @author william shao
	 * @date 2014年4月5日 下午2:22:19
	 *
	 */
	public interface IsAuditEnum {
		public int Y = 1;
		public int N = 0;
	}

	/**
	 * 
	 * @ClassName: IsClick
	 * @Description: TODO(是否可点击,Y:1可点,0不可点,包括文章图文版式 置顶大图 需要)
	 * @author william shao
	 * @date 2014年4月5日 下午2:22:19
	 *
	 */
	public interface IsClickEnum {
		public int Y = 1;
		public int N = 0;
	}

	/**
	 * 
	 * @ClassName: IsHot
	 * @Description: TODO(是否热点,Y:1是,0否,包括文章图文版式需要)
	 * @author william shao
	 * @date 2014年4月5日 下午2:22:19
	 *
	 */
	public interface IsHotEnum {
		public int Y = 1;
		public int N = 0;
	}

	/**
	 * 
	 * @ClassName: IsTj
	 * @Description: TODO(是否推荐,Y:1推荐,0不推荐,包括文章 图文版式需要)
	 * @author william shao
	 * @date 2014年4月5日 下午2:22:19
	 *
	 */
	public interface IsTjEnum {
		public int Y = 1;
		public int N = 0;
	}

	/**
	 * 
	 * @ClassName: IsContact
	 * @Description: TODO(是否关联,Y:1已关联,0未关联,包括文章需要)
	 * @author william shao
	 * @date 2014年4月5日 下午2:22:19
	 *
	 */
	public interface IsContactEnum {
		public int Y = 1;
		public int N = 0;
	}

	/**
	 * 地市session 获取id key
	 */
	public static final String CITYENUMVALUE = "CITYENUMVALUE";

	/**
	 * 消息推送内容id
	 */
	public static final String contentId = "contentId";
	/**
	 * 消息推送 地市编号
	 */
	public static final String cityNo = "cityNo";

	/**
	 * 
	 * @ClassName: IsAudit
	 * @Description: TODO(栏目类别,功能类、资讯类 ,0功能类，1资讯类 2资讯类其他来源)
	 * @author william shao
	 * @date 2014年4月5日 下午2:22:19
	 *
	 */
	public interface FunctionEnum {
		public int FUN = 0;
		public int CONTENT = 1;
		public int OTHERSOURCE = 2;
	}

	public static String getimageId(int len) {
		Calendar calendar = Calendar.getInstance();
		Long time = new Date().getTime()
				+ (calendar.get(Calendar.ZONE_OFFSET) + calendar
						.get(Calendar.DST_OFFSET)) / (60 * 1000);
		Constant c = new Constant();
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

	/**
	 * 将传递过来的base64参数转jsonObject
	 * 
	 * @param req
	 * @return
	 */
	public static Json convertJson(HttpServletRequest req) {
		Json json = new Json();
		BaseCode64 base64 = new BaseCode64();
		try {
			String sign = req.getParameter("sign");
			// System.out.println(sign+"  .......................aa");
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

	/**
	 * PDF 路径
	 */
	public static final String PDFSOURCE = "pdfsource/";

	/**
	 * 源 路径
	 */
	public static final String SOURCE = "source/";

	/**
	 * SWF 路径
	 */
	public static final String SWFSOURCE = "swfsource/";

	/**
	 * 0 swf > pdf
	 */
	public static final int DOC2PDF_STATUS = 0;

	/**
	 * 1 pdf > swf
	 */
	public static final int PDF2SWF_STATUS = 1;

	/**
	 * 2 转换完成
	 */
	public static final int CONVERTSUCCESS_STATUS = 2;

	/**
	 * 3 转换失败
	 */
	public static final int CONVERTERROR_STATUS = 3;

	/**
	 * 4: >不需要转
	 */
	public static final int CONVERTNO_STATUS = 4;

	/**
	 * 查询条数
	 */
	public static final int ROWNUMDATA = 1;

	public static final String EXTLIST = "doc,docx,xls,xlsx,txt,ppt,pptx,pdf";
	public static final String IMAGELIST = "jpg,png,mp3,mp4";
	// public static final String

	/**
	 * 更新处理中状态
	 */
	public static final int HANDLER_STATUS = 5;

	public static int regex_filetype(String ext) {
		int t = 0;

		return t;
	}

	/**
	 * 判断文件是否在转换范围内
	 * 
	 * @param ext
	 * @return boolean flg true ,status = 0 else 4
	 */
	public static boolean regex_ext(String ext) {
		boolean f = false;
		String dox_ext = EXTLIST;
		String[] slist = dox_ext.split(",");
		for (String sext : slist) {
			if (sext.equals(ext)) {
				f = true;
				break;
			}
		}
		return f;
	}

	public static int fileStatus(String ext) {
		String[] slist = EXTLIST.split(",");
		String[] vlist = IMAGELIST.split(",");
		for (String tem : slist) {
			if (tem.equals(ext)) {
				return 0;
			}
		}
		for (String tem : vlist) {
			if (tem.equals(ext)) {
				return 4;
			}
		}
		return -1;
	}

	public static boolean copyFile(File s, File t) throws Exception {
		InputStream fis = null;
		OutputStream fos = null;
		try {
			fis = new BufferedInputStream(new FileInputStream(s));
			fos = new BufferedOutputStream(new FileOutputStream(t));
			byte[] buf = new byte[2048];
			int i;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			try {
				fis.close();
				fos.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return true;
	}

	public static boolean isSameDate(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
		boolean isSameMonth = isSameYear
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
		boolean isSameDate = isSameMonth
				&& cal1.get(Calendar.DAY_OF_MONTH) == cal2
						.get(Calendar.DAY_OF_MONTH);
		return isSameDate;
	}

	public static Date string2date(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
