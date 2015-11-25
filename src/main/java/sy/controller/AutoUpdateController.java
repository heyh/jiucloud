package sy.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.jws.HandlerChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import sy.pageModel.Json;
/**
 * ****************************************************************
 * 文件名称 : AutoUpdateController.java
 * 作 者 :   Administrator
 * 创建时间 : 2015年1月4日 下午1:43:03
 * 文件描述 : Android版本更新
 * 版权声明 : 
 * 修改历史 : 2015年1月4日 1.00 初始版本
 *****************************************************************
 */
@Controller
@RequestMapping("/autoUpdateController")
public class AutoUpdateController extends BaseController {

	
	/**
	 * 跳转到android更新页面
	 * 
	 * @return
	 */
	@RequestMapping("/toAutoUpdateAndroid")
	public String toAutoUpdateAndroid(HttpServletRequest request) {
		 sy.util.GetRealPath grp = new sy.util.GetRealPath(request.getSession().getServletContext());
			String sysPath=grp.getRealPath()+"/upload/";
			SAXReader reader = new SAXReader();
			Json json = new Json();
			   //通过reader来解析指定路径下的xml文件
			   Document doc = null;
			try {
				doc = reader.read(new File(sysPath+"android-version\\update.xml"));
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   //通过doc获取xml文件中的元素赋值Element对象

			   Element rootElement = doc.getRootElement();
			   String version="";
			   String description="";
			   String apkName="";
			   String url="";
			   for (Iterator i = rootElement.elementIterator(); i.hasNext();)
			   {
			    Element element = (Element) i.next();
//			    System.out.println(element.getText());//显示首级信息
//			    System.out.println(element.getName());//显示首级元素名称

			    if(element.getName().equals("version"))
			    {
			    	version=element.getText();
			    	System.out.println(element.getText());
			    }//end block if
			    if(element.getName().equals("description"))
			    {
			    	description=element.getText();
			    	System.out.println(element.getText());
			    }
			    if(element.getName().equals("url"))
			    {
			    	url=element.getText();
			    	System.out.println(element.getText());
			    }
			    if(element.getName().equals("apkName"))
			    {
			    	apkName=element.getText();
			    	System.out.println(element.getText());
			    }//end block if
			   
			   }
			   request.setAttribute("version", version);
			   request.setAttribute("description", description);
			   request.setAttribute("apkName", apkName);
			   request.setAttribute("url", url);
		return "/admin/autoUpdates";
	}

	/**
	 * 到上传页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/securi_update")
	@ResponseBody
	public Json update(HttpServletRequest request, HttpServletResponse response) {
		sy.util.GetRealPath grp = new sy.util.GetRealPath(request.getSession()
				.getServletContext());
		String sysPath = grp.getRealPath() + "/upload/";
		SAXReader reader = new SAXReader();
		Json json = new Json();
		// 通过reader来解析指定路径下的xml文件
		Document doc = null;
		try {
			doc = reader
					.read(new File(sysPath + "android-version\\update.xml"));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 通过doc获取xml文件中的元素赋值Element对象

		Element rootElement = doc.getRootElement();
		String version = "";
		String description = "";
		String apkName = "";
		String url = "";
		for (Iterator i = rootElement.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			// System.out.println(element.getText());//显示首级信息
			// System.out.println(element.getName());//显示首级元素名称

			if (element.getName().equals("version")) {
				version = element.getText();
				System.out.println(element.getText());
			}// end block if
			if (element.getName().equals("description")) {
				description = element.getText();
				System.out.println(element.getText());
			}
			if (element.getName().equals("url")) {
				url = element.getText();
				System.out.println(element.getText());
			}
			if (element.getName().equals("apkName")) {
				apkName = element.getText();
				System.out.println(element.getText());
			}// end block if

		}

		Map<String, String> m = new HashMap<String, String>();
		m.put("version", version);
		m.put("apkName", apkName);
		m.put("description", description);
		m.put("url", url);
		Json j = new Json();
		j.setObj(m);
		j.setSuccess(true);
		j.setCode(null);
		j.setMsg(null);
		return j;

	}

	public Json doAutoUpdatexml(HttpServletRequest request,
			HttpServletResponse response, String apkNames, String descriptions,
			String versions, String addr) {
		Json j = new Json();

		sy.util.GetRealPath grp = new sy.util.GetRealPath(request.getSession()
				.getServletContext());
		String sysPath = grp.getRealPath() + "/upload/";
		File files1 = new File(sysPath + "android-version\\update.xml");
		if (files1.isFile() && files1.exists()) {
			// boolean bb=files1.delete();
			// System.out.println(bb);
			// System.out.println("===========已经删除update.xml文件===============");
		}
		OutputFormat format = OutputFormat.createPrettyPrint(); // 设置XML文档输出格式
		format.setEncoding("UTF-8"); // 设置XML文档的编码类型
		format.setSuppressDeclaration(true);
		format.setIndent(true); // 设置是否缩进
		format.setIndent(" "); // 以空格方式实现缩进
		format.setNewlines(true); // 设置是否换行

		String version1 = versions;
		String url1 = addr;
		String description1 = descriptions;
		String apkName1 = apkNames;

		Document document = DocumentHelper.createDocument(); // 创建文档
		document.setXMLEncoding("UTF-8");
		Element first = document.addElement("first");
		Element version = first.addElement("version"); // 添加子节点
		version.setText(version1); // 添加Text值；例：<a>abc</a>
		Element url = first.addElement("url");
		url.setText(url1);
		Element description = first.addElement("description");
		description.setText(description1);
		Element apkName = first.addElement("apkName");
		apkName.setText(apkName1);
		try {
			File fileWriter = new File(sysPath + "android-version\\update.xml");
			XMLWriter xmlWriter = new XMLWriter(
					new FileOutputStream(fileWriter), format);
			xmlWriter.write(document); // 写入文件中
			xmlWriter.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return j;

	}

	@RequestMapping("/douploadfile")
	@ResponseBody
	public Json douploadfile(HttpSession session, HttpServletRequest request,
			HttpServletResponse response, MultipartHttpServletRequest rt) {
		Json j = new Json();
		MultipartFile patch = rt.getFile("proPicFiled"); // 获取缩略图
		if (patch.isEmpty()) {
			j.setSuccess(false);
			j.setMsg("请选择上传文件!");
			return j;
		}
		sy.util.GetRealPath grp = new sy.util.GetRealPath(request.getSession()
				.getServletContext());
		String sysPath = grp.getRealPath() + "/upload/";
		String fileTrueName = "";
		File files1 = new File(sysPath + "android-version\\");
		if (files1.isFile() && files1.exists()) {
			boolean bb = files1.delete();
			// System.out.println("===========已经删除apk文件===============");

			// 实现apk删除
		}
		String dizhi = "android-version";
		String fileDir = sysPath + dizhi;
		if (!patch.isEmpty()) {
			File picSaveDir = new File(fileDir);
			if (!picSaveDir.exists())
				picSaveDir.mkdirs();
			try {
				String reg = patch
						.getOriginalFilename()
						.substring(
								patch.getOriginalFilename().lastIndexOf(".") + 1)
						.toLowerCase();
				if (!reg.equals("apk")) {
					j.setMsg("上传的文件格式错误,请上传apk...");
					j.setSuccess(false);
					return j;
				} else {
					fileTrueName = patch.getOriginalFilename();
					patch.transferTo(new File(picSaveDir + "/"
							+ patch.getOriginalFilename())); // 原图
					j.setSuccess(true);
					j.setMsg("版本更新成功!");
				}
			} catch (Exception ex) {
				j.setSuccess(false);
				j.setMsg("请检查文件,文件名尽量使用英文,请勿使用空格...");
				return j;
			}
		}

		ResourceBundle bundle = ResourceBundle.getBundle("config");
		String serverip = bundle.getString("SERVERIP");
		String serverport = bundle.getString("SERVERPORT");
		String PROJ = bundle.getString("PROJECT");
		String addr = "http://" + serverip + ":" + serverport + "" + PROJ
				+ "/upload/android-version" + "/" + fileTrueName;

		doAutoUpdatexml(request, response, rt.getParameter("apkName"),
				rt.getParameter("description"), rt.getParameter("version"),
				addr);
		return j;
	}

	@RequestMapping("/securi_encosys")
	@ResponseBody
	public Json encodesys(HttpServletRequest req) {
		ResourceBundle bundle = ResourceBundle.getBundle("config");
		Json j = new Json();
		j.setMsg(bundle.getString("encodes"));
		return j;
	}

}
