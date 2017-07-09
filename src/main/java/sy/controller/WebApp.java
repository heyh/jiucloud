package sy.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import sy.model.S_department;
import sy.model.po.Company;
import sy.model.po.Cost;
import sy.model.po.Department;
import sy.model.po.GCPo;
import sy.model.po.Project;
import sy.model.po.TFieldData;
import sy.pageModel.DataGrid;
import sy.pageModel.FieldData;
import sy.pageModel.Json;
import sy.pageModel.PageHelper;
import sy.pageModel.ProjectSearch;
import sy.pageModel.SessionInfo;
import sy.pageModel.User;
import sy.service.CompanyServiceI;
import sy.service.CostServiceI;
import sy.service.DepartmentServiceI;
import sy.service.FieldDataServiceI;
import sy.service.GCPoServiceI;
import sy.service.ProjectServiceI;
import sy.service.UserServiceI;
import sy.util.*;

@Controller
@RequestMapping("/webApp")
public class WebApp extends BaseController {

	@Autowired
	private UserServiceI userService;

	@Autowired
	private CompanyServiceI companyService;

	@Autowired
	private DepartmentServiceI departmentService;

	@Autowired
	private ProjectServiceI projectService;

	@Autowired
	private FieldDataServiceI fieldDataServiceI;

	@Autowired
	private CostServiceI costService;

	@Autowired
	private GCPoServiceI gcpoServiceI;

	/**
	 * 用户登录
	 * 
	 * @param user
	 *            用户对象
	 * @param session
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/securi_login")
	public Json login(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf8");
		Json j = new Json();
		System.out.println(request.getParameter("name"));
		System.out.println(request.getParameter("pwd"));
		User u = null;
		String cid = null;
		try {
			u = userService.login(request.getParameter("name"),
					request.getParameter("pwd"));
			if (u != null) {
				Company c = companyService.findOneView(u.getId(),cid);
				if (c == null) {
					j.setSuccess(false);
					j.setMsg("您好，您没有登录权限，请联系管理员订购该业务");
					return j;
				}
//				S_department s = departmentService.getDepartmentByUid(u.getId(),cid);
//				Department d = departmentService.findOneView(u.getId(),cid);
                List<S_department> s = departmentService.getDepartmentsByUid(u.getId(), cid);
				SessionInfo sessionInfo = new SessionInfo();
				sessionInfo.setName(u.getRealname());
				sessionInfo.setId(u.getId());
				sessionInfo.setCompid(String.valueOf(c.getId()));
				sessionInfo.setCompName(c.getName());
                sessionInfo.setDepartmentIds(s);
//				sessionInfo.setDepid(String.valueOf(d.getId()));
//				sessionInfo.setDepName(d.getName());
//				sessionInfo.setDepartment_id(String.valueOf(s.getId()));
//				sessionInfo.setDepartment_name(s.getName());
                if (null != s && s.size() > 0) {
                    for (S_department d : s) {
                        sessionInfo.setDepid(String.valueOf(d.getId()));
                        sessionInfo.setDepName(d.getName());
                        sessionInfo.setDepartment_id(String.valueOf(d.getId()));
                        sessionInfo.setDepartment_name(d.getName());
                    }
                } else {
                    sessionInfo.setDepid(null);
                    sessionInfo.setDepName(null);
                    sessionInfo.setDepartment_id(null);
                    sessionInfo.setDepartment_name(null);
                }
				j.setObj(sessionInfo);
			} else if (u == null) {
				j.setMsg("用户名或密码错误！");
				return j;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			j.setMsg("服务器异常！！！");
			return j;
		}
		j.setSuccess(true);
		j.setMsg("登陆成功！");
		return j;
	}

	/**
	 * 工程列表
	 * 
	 */
	@ResponseBody
	@RequestMapping("/securi_projectList")
	public Json projectList(ProjectSearch app, PageHelper ph,
			HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf8");
		Json json = new Json();
		DataGrid dataGrid = null;
		System.out.println(app);
		String uid = request.getParameter("uid");
		String cid = null;
		User u = userService.getUser(uid);
		try {
//			Department d = departmentService.findOneView(uid,cid);
//			List<Integer> ugroup = departmentService.getUserGroup(d, uid,cid);
            Company c = companyService.findOneView(u.getId(),cid);
//            List<Integer> ugroup = departmentService.getUsers(String.valueOf(c.getId()), Integer.parseInt(u.getId()));
//			dataGrid = projectService.dataGrid(app, ph, ugroup);
            dataGrid = projectService.dataGrid(app, ph, StringUtil.trimToEmpty(c.getId()), "field");
			json.setObj(dataGrid.getRows());
		} catch (Exception e) {
			json.setMsg("服务器错误,请稍后再试");
			return json;
		}
		json.setSuccess(true);
		return json;
	}

	/**
	 * 工程详细
	 */
	@ResponseBody
	@RequestMapping("/securi_projectDetail")
	public Json projectDetail(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf8");
		Json json = new Json();
		String proId = request.getParameter("proId");
		try {
			Project pro = this.projectService.findOneView(Integer
					.parseInt(proId));
			json.setObj(pro);
		} catch (Exception e) {
			json.setMsg("服务器错误,请稍后再试");
			return json;
		}
		json.setSuccess(true);
		return json;
	}

	/**
	 * 现场数据列表
	 */
	@RequestMapping("/securi_fielddataList")
	@ResponseBody
	public Json dataGrid(FieldData fieldData, PageHelper ph,
			HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf8");
		Json json = new Json();
		DataGrid dataGrid = null;
		String uid = request.getParameter("uid");
		String cid = null;
		User u = userService.getUser(uid);
		try {
//			Department d = departmentService.findOneView(uid,cid);
//			List<Integer> ugroup = departmentService.getUserGroup(d, uid,cid);
            Company c = companyService.findOneView(u.getId(),cid);
            List<Integer> ugroup = departmentService.getUsers(String.valueOf(c.getId()), Integer.parseInt(u.getId()));
			dataGrid = fieldDataServiceI.dataGrid(fieldData, ph, ugroup, "", "");

			// add by heyh begin
			List<FieldData> fieldDatas = dataGrid.getRows();
			if (fieldDatas != null && fieldDatas.size()>0) {
				for (int i = fieldDatas.size()-1; i >= 0; i--) {
					String currentApprovedUser = fieldDatas.get(i).getCurrentApprovedUser() == null ? "" : fieldDatas.get(i).getCurrentApprovedUser();
					if (!currentApprovedUser.equals("")) {
						User user = userService.getUser(currentApprovedUser);
						String realName = user.getRealname();
						if (realName == null || realName.equals("")) {
							realName = user.getUsername();
						}
						fieldDatas.get(i).setCurrentApprovedUser(realName);
					}
				}
			}
			// add by heyh end
			json.setObj(dataGrid.getRows());
		} catch (Exception e) {
			json.setMsg("服务器错误,请稍后再试");
			return json;
		}
		json.setSuccess(true);
		return json;
	}

	/**
	 * 现场详细
	 */
	@ResponseBody
	@RequestMapping("/securi_fielddataDetail")
	public Json fielddataDetail(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf8");
		Json json = new Json();
		String field_id = request.getParameter("field_id");
		if (field_id == null) {
			json.setMsg("传入数据不能为空");
			return json;
		}
		try {
			TFieldData tem = fieldDataServiceI.detail(field_id);

			FieldData f = new FieldData();
			f.setCost_id(Integer.parseInt(tem.getCostType()));
			f.setId(tem.getId());
			f.setUid(tem.getUid());
			f.setCreatTime(tem.getCreatTime());
			f.setDataName(tem.getDataName());
			f.setPrice(tem.getPrice());
			f.setCompany(tem.getCompany());
			f.setUnit(tem.getUnit());
			f.setCount(tem.getCount());
			f.setSpecifications(tem.getSpecifications());
			f.setRemark(tem.getRemark());
			f.setUname(tem.getUname());

			f.setCost_id(Integer.parseInt(tem.getCostType()));
			Cost cost = costService.findById(tem.getCostType());
			if (cost == null) {
				f.setCostType("该费用类型可能已经被删除");
			} else {
				f.setCostType(cost.getCostType());
			}

			f.setProject_id(Integer.parseInt(tem.getProjectName()));
			Project project = projectService.findOneView(Integer.parseInt(tem
					.getProjectName()));
			if (project == null) {
				f.setProjectName("该工程可能已经被删除");
			} else {
				f.setProjectName(project.getProName());
			}

			json.setObj(f);
		} catch (Exception e) {
			json.setMsg("服务器错误,请稍后再试");
			return json;
		}
		json.setSuccess(true);
		return json;
	}

	/**
	 * 保存
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/securi_savefieldData")
	@ResponseBody
	public Json saveFieldData(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf8");
		Json j = new Json();
		String count = "";
		String price = "";
		String costType = request.getParameter("costType");
		if (costType == null || "".equals(costType)) {
			j.setMsg("请选择费用类型!!");
			return j;
		}

		String costName = costService.findById(costType).getCostType();
        String itemCode = costService.findById(costType).getItemCode();
        if (!itemCode.substring(0, 3).equals("000") && Integer.parseInt(itemCode.substring(0,3)) <= 900) {
            count = request.getParameter("count");
            if (count == null || "".equals(count)) {
                j.setMsg("请输入数量!!");
                return j;
            }
            price = request.getParameter("price");
            if (price == null || "".equals(price)) {
                j.setMsg("请输入单价!!");
                return j;
            }
        }
//		if (!"纯附件".equals(costName)) {
//			count = request.getParameter("count");
//			if (count == null || "".equals(count)) {
//				j.setMsg("请输入数量!!");
//				return j;
//			}
//			price = request.getParameter("price");
//			if (price == null || "".equals(price)) {
//				j.setMsg("请输入单价!!");
//				return j;
//			}
//		}

		String dataName = request.getParameter("dataName");
		if (dataName == null || "".equals(dataName)) {
			j.setMsg("请输入现场数据名称!!");
			return j;
		}

		String projectName = request.getParameter("projectName");
		if (projectName == null || "".equals(projectName)) {
			j.setMsg("请选择工程!!");
			return j;
		}
		String uid = request.getParameter("uid");
		if (uid == null || "".equals(uid)) {
			j.setMsg("请登录后再试!!");
			return j;
		}
		String specifications = request.getParameter("specifications");
		String unit = request.getParameter("unit");
		String remark = request.getParameter("remark");
		String uname = request.getParameter("uname");
		String cid = request.getParameter("cid");
		String company = request.getParameter("company");
        String needApproved = request.getParameter("needApproved");
        String approvedUser = "";
        String currentApprovedUser = "";
        String section = StringUtil.trimToEmpty(request.getParameter("section"));
		String supplier = StringUtil.trimToEmpty(request.getParameter("supplier"));
        // add by heyh begin
        List<Integer> approvedUserList = new ArrayList<Integer>();
        if (needApproved != null && needApproved.equals("1")) {
            approvedUserList = departmentService.getAllParents(cid, Integer.parseInt(uid));
            if (approvedUserList == null) {
                approvedUserList.add(Integer.parseInt(uid)); // 如果为空说明是超级管理员，自己审批
            }
            approvedUser = StringUtils.join(approvedUserList, ","); // 所有审批人
            currentApprovedUser = String.valueOf(approvedUserList.get(0)); // 当前审批人
        } else {
			needApproved = "0";
		}
        // add by heyh end
		Cost cost = costService.findById(costType);
//		fieldData.setItemCode(cost.getItemCode());
		TFieldData fieldData = new TFieldData(projectName, uid, new Date(),
				costType, dataName, price, company, count, specifications,
				remark, cid, uname, unit, needApproved, approvedUser, cost.getItemCode(), currentApprovedUser, "", section, supplier, "", "","");

		System.out.println(fieldData);
		try {
			fieldDataServiceI.add(fieldData);
			j.setObj(fieldDataServiceI.getId(fieldData));
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
			return j;
		}
		j.setSuccess(true);
		j.setMsg("添加成功！");
		return j;
	}

	/**
	 * 修改
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/securi_updatefieldData")
	@ResponseBody
	public Json updateFieldData(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf8");
		Json j = new Json();
		String id = request.getParameter("id");
		if (id == null || "".equals(id)) {
			j.setMsg("现场数据id不能为空!!");
			return j;
		}
		TFieldData fieldData = fieldDataServiceI.detail(id);
		if (!Constant.isSameDate(fieldData.getCreatTime(), new Date())) {
			j.setMsg("只能修改当天录入的信息!!");
			return j;
		}

		String costType = request.getParameter("costType");
		if (costType == null || "".equals(costType)) {
			j.setMsg("请选择费用类型!!");
			return j;
		}
		fieldData.setCostType(costType);

        String itemCode = costService.findById(costType).getItemCode();
        if (!itemCode.substring(0, 3).equals("000") && Integer.parseInt(itemCode.substring(0,3)) <= 900) {
            String count = request.getParameter("count");
            if (count == null || "".equals(count)) {
                j.setMsg("请输入数量!!");
                return j;
            }
            fieldData.setCount(count);

            String price = request.getParameter("price");
            if (price == null || "".equals(price)) {
                j.setMsg("请输入单价!!");
                return j;
            }
            fieldData.setPrice(price);
        }

		String dataName = request.getParameter("dataName");
		if (dataName == null || "".equals(dataName)) {
			j.setMsg("请输入现场数据名称!!");
			return j;
		}
		fieldData.setDataName(dataName);

		String projectName = request.getParameter("projectName");
		if (projectName == null || "".equals(projectName)) {
			j.setMsg("请选择工程!!");
			return j;
		}
		fieldData.setProjectName(projectName);
		fieldData.setUnit(request.getParameter("unit"));
		fieldData.setSpecifications(request.getParameter("specifications"));
		fieldData.setRemark(request.getParameter("remark"));

        // add by heyh begin
        String cid = request.getParameter("cid");
        String uid = request.getParameter("uid");
        if (uid == null || "".equals(uid)) {
            j.setMsg("请登录后再试!!");
            return j;
        }
        String needApproved = request.getParameter("needApproved");
        String approvedUser = "";
        String currentApprovedUser = "";
        List<Integer> approvedUserList = new ArrayList<Integer>();
		if (needApproved != null && needApproved.equals("1")) {
			approvedUserList = departmentService.getAllParents(cid, Integer.parseInt(uid));
			if (approvedUserList == null) {
				approvedUserList.add(Integer.parseInt(uid)); // 如果为空说明是超级管理员，自己审批
			}
			approvedUser = StringUtils.join(approvedUserList, ","); // 所有审批人
			currentApprovedUser = String.valueOf(approvedUserList.get(0)); // 当前审批人

			fieldData.setNeedApproved("1");
			fieldData.setApprovedUser(approvedUser);
			fieldData.setCurrentApprovedUser(currentApprovedUser);
		} else {
			fieldData.setNeedApproved("0");
			fieldData.setApprovedUser("");
			fieldData.setCurrentApprovedUser("");
		}
        // add by heyh end

		System.out.println(fieldData);
		try {
			fieldDataServiceI.update(fieldData);
			j.setObj(fieldDataServiceI.getId(fieldData));
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
			return j;
		}
		j.setSuccess(true);
		j.setMsg("修改成功！");
		return j;
	}

	/**
	 * 费用类型列表
	 * 
	 */
	@ResponseBody
	@RequestMapping("/securi_costList")
	public Json costList(PageHelper ph, HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf8");
		Json json = new Json();
		String cid = request.getParameter("cid");
		String title = request.getParameter("title");
		String code = request.getParameter("code");
        String departmentIds = request.getParameter("departmentIds");
		if (cid == null || "".equals(cid)) {
			json.setMsg("公司id不能为空!!");
			return json;
		}
		try {
			List<Cost> list = costService.getEndCosts(title, code, ph, cid, departmentIds);
			json.setObj(list);
		} catch (Exception e) {
			json.setMsg("服务器错误,请稍后再试");
			return json;
		}
		json.setSuccess(true);
		return json;
	}

	@RequestMapping("/securi_fileList")
	@ResponseBody
	public Json fileList(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf8");
		Json json = new Json();
		String id = request.getParameter("mid");
		List<GCPo> list = null;
		try {
			list = gcpoServiceI.dataGrid(id, null, null).getRows();
			System.out.println(list);
		} catch (Exception e) {
			json.setMsg("服务器错误,请稍后再试");
			return json;
		}
		json.setObj(list);
		json.setSuccess(true);
		return json;

	}

	/**
	 * 文件上传
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/securi_upload")
	@ResponseBody
	public Json uploadhb(HttpSession session, HttpServletRequest req,
			HttpServletResponse response, MultipartHttpServletRequest rt) {
		response.setContentType("text/html;charset=utf8");
		Json j = new Json();

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
			TFieldData tem = fieldDataServiceI.detail(mid);
			String userPath = tem.getUid() + "/";

//			GetRealPath grp = new GetRealPath(req.getSession().getServletContext());
//			String file_path = grp.getRealPath() + "upload/" + Constant.SOURCE + userPath;
            String file_path = PropertyUtil.getFileRealPath() + "/upload/" + Constant.SOURCE + userPath;

			MultipartFile patch = rt.getFile("file");// 获取文件

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
				String finalname = UUID.randomUUID().toString();
//                String finalname = fileName.substring(0, patch.getOriginalFilename().lastIndexOf(".")) + "-" + DateKit.getCurrentDate("yyyyMMddHHmmssSSS");

                // boolean regxFlg = Constant.regex_ext(reg);
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
                    FileUtils.copyFileToDirectory(f, new File(pdfFilePath));
                    gcpo.setStatus(Constant.PDF2SWF_STATUS);
                    gcpo.setPdfFilePath(userPath + finalname + "." + reg);

//					Constant.copyFile(f, new File(file_path + Constant.PDFSOURCE + finalname + "." + reg));
//					gcpo.setStatus(Constant.PDF2SWF_STATUS);
//					gcpo.setPdfFilePath(finalname + "." + reg);
				}
				gcpoServiceI.add(gcpo);
				j.setMsg("上传成功");
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

	@RequestMapping("/securi_filedelete")
	@ResponseBody
	public Json filedelete(String id, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf8");
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
     * 审批数据列表
     */
    @RequestMapping("/securi_approveFielddataList")
    @ResponseBody
    public Json approveFielddataList(FieldData fieldData, PageHelper ph,
                         HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf8");
        Json json = new Json();
        DataGrid dataGrid = null;
        String uid = request.getParameter("uid");
        String cid = null;
        User u = userService.getUser(uid);
        try {
//			Department d = departmentService.findOneView(uid,cid);
//			List<Integer> ugroup = departmentService.getUserGroup(d, uid,cid);
            Company c = companyService.findOneView(u.getId(),cid);
            List<Integer> ugroup = departmentService.getUsers(String.valueOf(c.getId()), Integer.parseInt(u.getId()));
            dataGrid = fieldDataServiceI.approveDataGrid(ph, uid);

            // add by heyh begin
            List<FieldData> fieldDatas = dataGrid.getRows();
            if (fieldDatas != null && fieldDatas.size()>0) {
                for (int i = fieldDatas.size()-1; i >= 0; i--) {
                    String currentApprovedUser = fieldDatas.get(i).getCurrentApprovedUser() == null ? "" : fieldDatas.get(i).getCurrentApprovedUser();
                    if (!currentApprovedUser.equals("")) {
                        User user = userService.getUser(currentApprovedUser);
                        String realName = user.getRealname();
                        if (realName == null || realName.equals("")) {
                            realName = user.getUsername();
                        }
                        fieldDatas.get(i).setCurrentApprovedUser(realName);
                    }
                }
            }
            // add by heyh end
            json.setObj(dataGrid.getRows());
        } catch (Exception e) {
            json.setMsg("服务器错误,请稍后再试");
            return json;
        }
        json.setSuccess(true);
        return json;
    }

    /**
     * 我提交的审批数据列表
     */
    @RequestMapping("/securi_myApproveFielddataList")
    @ResponseBody
    public Json myApproveFielddataList(FieldData fieldData, PageHelper ph,
                                     HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf8");
        Json json = new Json();
        DataGrid dataGrid = null;
        String uid = request.getParameter("uid");
        String cid = null;
        User u = userService.getUser(uid);
        try {
//			Department d = departmentService.findOneView(uid,cid);
//			List<Integer> ugroup = departmentService.getUserGroup(d, uid,cid);
            Company c = companyService.findOneView(u.getId(),cid);
            List<Integer> ugroup = departmentService.getUsers(String.valueOf(c.getId()), Integer.parseInt(u.getId()));
			dataGrid = fieldDataServiceI.myApproveDataGrid(ph, uid, "", fieldData, "");

            // add by heyh begin
            List<FieldData> fieldDatas = dataGrid.getRows();
            if (fieldDatas != null && fieldDatas.size()>0) {
                for (int i = fieldDatas.size()-1; i >= 0; i--) {
                    String currentApprovedUser = fieldDatas.get(i).getCurrentApprovedUser() == null ? "" : fieldDatas.get(i).getCurrentApprovedUser();
                    if (!currentApprovedUser.equals("")) {
                        User user = userService.getUser(currentApprovedUser);
                        String realName = user.getRealname();
                        if (realName == null || realName.equals("")) {
                            realName = user.getUsername();
                        }
                        fieldDatas.get(i).setCurrentApprovedUser(realName);
                    }
                }
            }
            // add by heyh end
            json.setObj(dataGrid.getRows());
        } catch (Exception e) {
            json.setMsg("服务器错误,请稍后再试");
            return json;
        }
        json.setSuccess(true);
        return json;
    }

    @RequestMapping("/securi_approvedField")
    @ResponseBody
    public Json approvedField(Integer id, String approvedState, String approvedOption, String currentApprovedUser, HttpServletResponse response, HttpServletRequest request) {
        Json j = new Json();
        if (id != null) {
            fieldDataServiceI.approvedField(id, approvedState, approvedOption, currentApprovedUser);
        }
        j.setMsg("审批成功！");
        j.setSuccess(true);
        return j;
    }

	@RequestMapping("/securi_update")
	@ResponseBody
	public Json filedelete(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf8");

		String version_now = request.getParameter("version");
		Json j = new Json();
		try {
			GetRealPath grp = new GetRealPath(request.getSession()
					.getServletContext());
			String file_path = grp.getRealPath()
					+ "upload/android-version/update.txt";
			File file = new File(file_path);
			FileReader fr = new FileReader(file);
			BufferedReader reader = new BufferedReader(fr);
			String version_max = reader.readLine();

			if (Double.parseDouble(version_max) <= Double
					.parseDouble(version_now)) {
				j.setMsg("已经是最新版本");
				return j;
			}

			String file_name = reader.readLine();
			j.setObj(request.getContextPath() + "/upload/android-version/"
					+ file_name);

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
			return j;
		}
		j.setMsg("发现新版本");
		j.setSuccess(true);
		return j;
	}

}
