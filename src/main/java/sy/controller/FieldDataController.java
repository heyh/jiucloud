package sy.controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sy.model.po.Cost;
import sy.model.po.GCPo;
import sy.model.po.Project;
import sy.model.po.TFieldData;
import sy.pageModel.*;
import sy.service.*;
import sy.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
@RequestMapping("/fieldDataController")
public class FieldDataController extends BaseController {

	@Autowired
	private FieldDataServiceI fieldDataServiceI;

	@Autowired
	private ProjectServiceI projectServiceI;

	@Autowired
	private GCPoServiceI gcpoServiceI;

	@Autowired
	private CostServiceI costServiceI;

    @Autowired
    private DepartmentServiceI departmentService;

	/**
	 * 跳转管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/fieldDataShow")
	public String manager(HttpServletRequest req) {
		req.setAttribute("first", UtilDate.getshortFirst());
		req.setAttribute("last", UtilDate.getshortLast());
		return "/app/fielddata/fielddataShow";
	}
	/**
	 * 跳转费用管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/docDataShow")
	public String docManager(HttpServletRequest req) {
		req.setAttribute("first", UtilDate.getshortFirst());
		req.setAttribute("last", UtilDate.getshortLast());
		return "/app/fielddata/docdataShow";
	}

	/**
	 * 获取管理页面数据
	 */
	@RequestMapping("/dataGrid")
	@ResponseBody
	public DataGrid dataGrid(FieldData fieldData, PageHelper ph,
			HttpServletRequest request, HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil
				.getSessionInfoName());
		List<Integer> ugroup = sessionInfo.getUgroup();
		String source = request.getParameter("source");
        if (request.getParameter("id") != null) {
            Integer id = Integer.parseInt(request.getParameter("id"));
            fieldData.setId(id);
        }
		if(null==fieldData.getStartTime()&&null==fieldData.getEndTime()){
			fieldData.setStartTime(UtilDate.getshortFirst()+" 00:00:00");
			fieldData.setEndTime(UtilDate.getshortLast()+" 23:59:59");
		}
        if (null != request.getParameter("needApproved")) {
            fieldData.setNeedApproved(request.getParameter("needApproved"));
        }

		DataGrid dataGrid = fieldDataServiceI.dataGrid(fieldData, ph, ugroup,source);

        // add by heyh begin 审批数据
        if (null != request.getParameter("needApproved")) {
            List<FieldData> fieldDatas = dataGrid.getRows();
            for (int i = fieldDatas.size()-1; i >= 0; i--) {
                String currentApprovedUser = fieldDatas.get(i).getCurrentApprovedUser() == null ? "" : fieldDatas.get(i).getCurrentApprovedUser();
                if (!currentApprovedUser.equals(sessionInfo.getId())) {
                    fieldDatas.remove(i);
                }
            }
            dataGrid.setTotal((long) fieldDatas.size());
        }

        // add by heyh end
		session.setAttribute("analusisInfo", fieldData);
		return dataGrid;
	}

	/**
	 * 获取附件管理页面数据
	 */
	@RequestMapping("/securi_filedataGrid")
	@ResponseBody
	public DataGrid dataGrid(HttpServletRequest request) {
		String filename = request.getParameter("filename");
		String filetype = request.getParameter("filetype");
		String id = request.getParameter("id");
		return gcpoServiceI.dataGrid(id, filename, filetype);
	}

	/**
	 * 跳转工程选择弹出框
	 */
	@RequestMapping("/securi_selectp")
	public String selectp(HttpServletRequest request, String proid,
			String proName) {
		return "/app/fielddata/project_select";
	}

	/**
	 * 获取费用类型选择弹出框
	 */
	@RequestMapping("/securi_selectc")
	public String selectc() {
		return "/app/fielddata/cost_select";
	}
	/**
	 * 获取费用类型选择弹出框
	 */
	@RequestMapping("/securi_selectcc")
	public String selectcc() {
		return "/app/fielddata/cost_selectcc";
	}

	/**
	 * 跳转附件预览
	 */
	@RequestMapping("/securi_showfile")
	public String showfile(HttpServletRequest request) {
		String id = request.getParameter("id");
		GCPo file = gcpoServiceI.findOneView(Integer.parseInt(id));
		if (file.getExt().equals("png") || file.getExt().equals("jpg")) {
			request.setAttribute("v", file.getSourceFilePath());
			return "/app/fielddata/pic";
		} else if (file.getExt().equals("mp3")) {
			request.setAttribute("v", file.getSourceFilePath());
			return "/app/fielddata/music";
		} else if (file.getExt().equals("mp4")) {
			request.setAttribute("v", file.getSourceFilePath());
			return "/app/fielddata/video";
		} else {
			request.setAttribute("v", file.getSwfFilePath());
			return "/app/fielddata/view";
		}
	}

	/**
	 * 跳转到附件管理
	 * 
	 * @param request
	 */
	@RequestMapping("/securi_fieldDataFile")
	public String addFilePage(String id, HttpServletRequest request) {
		request.setAttribute("id", id);
		return "/app/fielddata/fieldDataFile";
	}

	/**
	 * 跳转到修改
	 * 
	 * @param request
	 * @return
	 * @throws java.io.IOException
	 */
	@RequestMapping("/upfieldData")
	public String updatePage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String id = request.getParameter("id");
		TFieldData tFieldData = fieldDataServiceI.detail(id);
		if (tFieldData == null) {
			response.getWriter().write("1");
			return null;
		}
		Project project = projectServiceI.findOneView(Integer
				.parseInt(tFieldData.getProjectName()));
		Cost cost = costServiceI.findById(tFieldData.getCostType());

		request.setAttribute("tfielddata", tFieldData);
		request.setAttribute("project", project);
		request.setAttribute("cost", cost);
		return "/app/fielddata/updatefieldData";
	}

	/**
	 * 跳转到添加
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/addfieldData")
	public String addPage(HttpServletRequest request, String proid) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession()
				.getAttribute(ConfigUtil.getSessionInfoName());
		String cost_id = sessionInfo.getLast_cost_id();
		Cost cost = null;
		String proName = "";

		if (cost_id != null && cost_id.length() > 0) {
			cost = costServiceI.findById(cost_id);
		}

		if (proid != null && proid.length() > 0) {
			Project project = projectServiceI.findOneView(Integer
					.parseInt(proid));
			proName = project.getProName();
		} else {
			proid = sessionInfo.getLast_project_id();
			if (proid != null && proid.length() > 0) {
				proName = projectServiceI.findOneView(Integer.parseInt(proid))
						.getProName();
			}
		}

		request.setAttribute("cost", cost);
		request.setAttribute("proid", proid);
		request.setAttribute("proName", proName);
		return "/app/fielddata/addfieldData";
	}
	/**
	 * 跳转到添加
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/addDocData")
	public String addDocPage(HttpServletRequest request, String proid) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
		String cost_id = sessionInfo.getLast_cost_id();
		Cost cost = null;
		String proName = "";

		if (cost_id != null && cost_id.length() > 0) {
			cost = costServiceI.findById(cost_id);
		}

		if (proid != null && proid.length() > 0) {
			Project project = projectServiceI.findOneView(Integer
					.parseInt(proid));
			proName = project.getProName();
		} else {
			proid = sessionInfo.getLast_project_id();
			if (proid != null && proid.length() > 0) {
				proName = projectServiceI.findOneView(Integer.parseInt(proid))
						.getProName();
			}
		}

		request.setAttribute("cost", cost);
		request.setAttribute("proid", proid);
		request.setAttribute("proName", proName);
		return "/app/fielddata/addDocData";
	}

	/**
	 * 保存
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/savefieldData")
	public Json saveFieldData(TFieldData fieldData, HttpServletRequest request) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		Json j = new Json();
		SessionInfo sessionInfo = (SessionInfo) request.getSession()
				.getAttribute(ConfigUtil.getSessionInfoName());
		fieldData.setUid(sessionInfo.getId());
		fieldData.setCid(sessionInfo.getCompid());
        // modify by heyh
		fieldData.setUname((sessionInfo.getName() != null && !sessionInfo.getName().equals("")) ? sessionInfo.getName() : sessionInfo.getUsername());
		fieldData.setCompany(sessionInfo.getCompName());

        // add by heyh begin
        List<Integer> approvedUserList = new ArrayList<Integer>();
        if (fieldData.getNeedApproved().equals("1")) {
            approvedUserList = departmentService.getAllParents(fieldData.getCid(), Integer.parseInt(fieldData.getUid()));
            if (approvedUserList == null) {
                approvedUserList.add(Integer.parseInt(fieldData.getUid())); // 如果为空说明是超级管理员，自己审批
            }
            fieldData.setApprovedUser(StringUtils.join(approvedUserList, ",")); // 所有审批人
            fieldData.setCurrentApprovedUser(String.valueOf(approvedUserList.get(0))); // 当前审批人

        }
        // add by heyh end

		try {
			Cost tem = costServiceI.findById(fieldData.getCostType());
            String fj = tem.getItemCode().substring(0, 3);
//			if (!"纯附件".equals(tem.getCostType())) {
            if (!fj.equals("000") && Integer.parseInt(fj) <=900 ) {
				Double.parseDouble(fieldData.getPrice());
				Integer.parseInt(fieldData.getCount());
			}
			fieldDataServiceI.add(fieldData);
			j.setObj(fieldDataServiceI.getId(fieldData));
			sessionInfo.setLast_cost_id(fieldData.getCostType());
			sessionInfo.setLast_project_id(fieldData.getProjectName());
			j.setSuccess(true);
			j.setMsg("现场数据添加成功！");
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}

    /**
     * 修改
     * @param fieldData
     * @param session
     * @return
     */
	@RequestMapping("/updatefieldData")
	@ResponseBody
	public Json updateFieldData(TFieldData fieldData, HttpSession session) {
		Json j = new Json();
		try {
			Cost tem = costServiceI.findById(fieldData.getCostType());

			if (!Constant.isSameDate(fieldData.getCreatTime(), new Date())) {
				j.setMsg("只能修改当天录入的信息!!");
				return j;
			}
            String itemCode = tem.getItemCode();
            if (!itemCode.substring(0, 3).equals("000") && Integer.parseInt(itemCode.substring(0, 3)) <= 900) {
                Double.parseDouble(fieldData.getPrice());
                Integer.parseInt(fieldData.getCount());
            }
//			if (!"纯附件".equals(tem.getCostType())) {
//				Double.parseDouble(fieldData.getPrice());
//				Integer.parseInt(fieldData.getCount());
//			}

            // add by heyh begin 修改后重新设置审批状态和当前审批人
            if (fieldData.getNeedApproved() != null && !fieldData.getNeedApproved().equals("0")) {
                fieldData.setNeedApproved("1");
                List<Integer> approvedUserList = departmentService.getAllParents(fieldData.getCid(), Integer.parseInt(fieldData.getUid()));
                if (approvedUserList == null) {
                    approvedUserList.add(Integer.parseInt(fieldData.getUid())); // 如果为空说明是超级管理员，自己审批
                }
                fieldData.setApprovedUser(StringUtils.join(approvedUserList, ",")); // 所有审批人
                fieldData.setCurrentApprovedUser(String.valueOf(approvedUserList.get(0))); // 当前审批人
            }
            // add by heyh end

			fieldDataServiceI.update(fieldData);
			j.setSuccess(true);
			j.setMsg("操作成功！");
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}

    /**
     * 删除
     * @param id
     * @return
     */
	@RequestMapping("/delfieldData")
	@ResponseBody
	public Json delCost(String id) {
		Json j = new Json();
		try {
			fieldDataServiceI.delete(id);
			j.setSuccess(true);
			j.setMsg("操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 批量删除
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/securi_bunchdelete")
	@ResponseBody
	public Json bunchdelete(HttpServletRequest request) {
		Json j = new Json();
		String ids = request.getParameter("ids");
		try {
			String[] id = ids.split(",");
			for (String tem : id) {
				fieldDataServiceI.delete(tem);
			}
			j.setSuccess(true);
			j.setMsg("操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/securi_filedelete")
	@ResponseBody
	public Json filedelete(String id) {
		Json j = new Json();
		try {
			gcpoServiceI.deleteOne(Integer.parseInt(id));
			j.setSuccess(true);
			j.setMsg("操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 跳转至批量下载附件
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/securi_downloadfile")
	public String downloadfile(String mpid, HttpServletRequest request) {
		System.out.println(mpid);
		request.setAttribute("mpid", mpid);
		return "/app/fielddata/download";
	}

    /**
     * 文件上传
     * @param session
     * @param req
     * @param rt
     * @return
     */
	@RequestMapping("/upload")
	@ResponseBody
	public Json uploadhb(HttpSession session, HttpServletRequest req,
			MultipartHttpServletRequest rt) {
		Json j = new Json();
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil
				.getSessionInfoName());
		String userPath = sessionInfo.getId() + "/";

		String mid = rt.getParameter("id");// 关联ID;
		if (mid == null || mid.length() == 0 || mid.equals("undefined")) {
			j.setCode(000);
			j.setSuccess(false);
			j.setMsg("mid is need not null");
			return j;
		}
		long count = gcpoServiceI.getFieldCount(mid);
		if (count >= 10) {
			j.setSuccess(false);
			j.setMsg("该条现场记录的附件数量过多,限制上传");
			return j;
		}
		try {
//			GetRealPath grp = new GetRealPath(req.getSession()
//					.getServletContext());
            // modify by heyh begin
			String file_path = PropertyUtil.getFileRealPath() + "/upload/" + Constant.SOURCE + userPath;
            // end
			MultipartFile patch = rt.getFile(req.getParameter("name"));// 获取文件
			String fileName = patch.getOriginalFilename();// 得到文件名
			if (!patch.isEmpty()) {
				File saveDir = new File(file_path);
				if (!saveDir.exists())
					saveDir.mkdirs();
				String reg = fileName.substring(
						patch.getOriginalFilename().lastIndexOf(".") + 1)
						.toLowerCase();
				int status = Constant.fileStatus(reg);
				if (status == -1) {
					j.setSuccess(false);
					j.setMsg("上传的文件格式不支持");
					return j;
				}
                // modify by heyh begin
				String finalname = fileName.substring(0, patch.getOriginalFilename().lastIndexOf(".")) + "-" + DateKit.getCurrentDate("yyyyMMddHHmmssSSS");
//                String finalname = UUID.randomUUID().toString();
                // end
				File f = new File(file_path + finalname + "." + reg);
                    patch.transferTo(f);

				GCPo gcpo = new GCPo();
				gcpo.setMpid(mid);
				gcpo.setFileName(patch.getOriginalFilename());
				gcpo.setPdfFilePath("");
				gcpo.setSwfFilePath("");
				gcpo.setSourceFilePath(userPath + finalname + "." + reg);
				gcpo.setExt(reg);
				gcpo.setStatus(status);

				// 如果已经是pdf直接设置从pdf > swf状态开始
				if (reg.equals("pdf")) {
                    String pdfFilePath = PropertyUtil.getFileRealPath() + "/upload/" + Constant.PDFSOURCE + userPath;
//					Constant.copyFile(f, new File(pdfFilePath + finalname + "." + reg));
                    FileUtils.copyFileToDirectory(f, new File(pdfFilePath));
					gcpo.setStatus(Constant.PDF2SWF_STATUS);
					gcpo.setPdfFilePath(userPath + finalname + "." + reg);
				}
				gcpoServiceI.add(gcpo);
				j.setMsg(fileName + "上传成功");
				j.setObj(gcpo.getId());
				j.setCode(2000);
				j.setSuccess(true);
				return j;
			} else {
				j.setCode(1004);
				j.setObj(null);
				j.setSuccess(false);
				j.setMsg("上传的文件不存在");
				return j;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			j.setCode(1005);
			j.setObj(null);
			j.setSuccess(false);
			j.setMsg("上传异常:" + ex.getMessage());
			return j;
		}
	}

	/* 后台导出到excel */
	@RequestMapping("/securi_execl")
	public void OutputToExcel(FieldData fieldData, PageHelper ph,
			HttpServletResponse response, HttpServletRequest request) {
		response.setContentType("text/html;charset=utf8");
		String source = request.getParameter("source");
		HttpSession session = request.getSession();

		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil
				.getSessionInfoName());
		List<Integer> ugroup = sessionInfo.getUgroup();
		ph.setRows(999999999);

//		try {
//			fieldData.setUname(new String(request.getParameter("uname")
//					.getBytes("iso-8859-1"), "utf-8"));
//			fieldData.setProjectName(new String(request.getParameter(
//					"projectName").getBytes("iso-8859-1"), "utf-8"));
//			fieldData.setCostType(new String(request.getParameter("costType")
//					.getBytes("iso-8859-1"), "utf-8"));
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//		}

        fieldData.setUname(request.getParameter("uname"));
        fieldData.setProjectName(request.getParameter("projectName"));
        fieldData.setCostType(request.getParameter("costType"));

		List<FieldData> datas = fieldDataServiceI.dataGrid(fieldData, ph,
				ugroup,source).getRows();

		List<Map<String, Object>> map = createExcelRecord(datas);

		// 填充projects数据
		String[] columnNames = { "工程名称", "费用类型", "现场数据名称", "单价", "数量", "金额",
				"单位", "规格", "操作人", "入库时间" };// 列名
		String[] keys = { "project_name", "costType_name", "name", "price",
				"count", "money", "unit", "specifications", "uname",
				"createTime" };// map中的key

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			ExcelExportUtil.createWorkBook(map, keys, columnNames).write(os);
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=data.xls");
			ServletOutputStream out;
			out = response.getOutputStream();
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;
			// Simple read/write loop.
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	// 格式化execl表格数据
	private List<Map<String, Object>> createExcelRecord(List<FieldData> list) {
		List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sheetName", "data.xls");
		listmap.add(map);
		double count = 0;

		for (int i = 0; i < list.size(); i++) {
			FieldData tem = list.get(i);
			Map<String, Object> mapValue = new HashMap<String, Object>();
			mapValue.put("project_name", tem.getProjectName());
			mapValue.put("costType_name", tem.getCostType());
			mapValue.put("name", tem.getDataName());
			mapValue.put("price", tem.getPrice());
			mapValue.put("count", tem.getCount());
			double result = Integer.parseInt(tem.getCount()) * Double.parseDouble(tem.getPrice());
			count += result;
			int result2 = (int) (result * 100);
			mapValue.put("money", result2 / 100.0);
			mapValue.put("unit", tem.getUnit());
			mapValue.put("specifications", tem.getSpecifications());
			mapValue.put("uname", tem.getUname());
			mapValue.put("createTime", tem.getCreatTime());
			listmap.add(mapValue);
		}
		Map<String, Object> mapValue = new HashMap<String, Object>();
		int result2 = (int) (count * 100);
		mapValue.put("project_name", "合计");
		mapValue.put("money", result2 / 100.0);
		listmap.add(mapValue);
		return listmap;
	}

    @RequestMapping("/securi_approvedField")
    @ResponseBody
    public Json approvedField(Integer id, String approvedState,HttpServletResponse response, HttpServletRequest request) {
        Json j = new Json();
        if (id != null) {
            fieldDataServiceI.approvedField(id, approvedState);
        }
        j.setMsg("审批成功！");
        j.setSuccess(true);
        return j;
    }
}
