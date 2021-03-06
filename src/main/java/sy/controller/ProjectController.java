package sy.controller;

import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import sy.model.Area;
import sy.model.City;
import sy.model.Province;
import sy.model.S_department;
import sy.model.po.Project;
import sy.pageModel.*;
import sy.service.*;
import sy.util.ConfigUtil;
import sy.util.StringUtil;
import sy.util.UtilDate;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * **************************************************************** 文件名称 :
 * ApplicationController.java 作 者 : Administrator 创建时间 : 2015年6月4日11:14:06 文件描述
 * : 通过ProjectController将数据库中查询出的值使用Json格式传递给前台页面，绑定在dataGrid上 版权声明 : 修改历史 :
 * 2015年6月4日 1.00 初始版本
 *****************************************************************
 */

@Controller
@RequestMapping("/projectController")
public class ProjectController extends BaseController {

	@Autowired
	private ProjectServiceI projectService;
	@Autowired
	private ProvinceServiceI provinceService;
	@Autowired
	private CityServiceI cityService;

    @Autowired
    private AreaServiceI areaService;

	@Autowired
	private DepartmentServiceI departmentService;

	/**
	 * 跳转管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/recProList")
	public String managerrec(HttpServletRequest req) {
		req.setAttribute("first", UtilDate.getshortFirst());
		req.setAttribute("last", UtilDate.getshortLast());
		return "/app/pro/showprolist";
	}

	/**
	 * 获取城市数据
	 * 
	 * @return
	 */
	@RequestMapping("/getCities")
	@ResponseBody
	public List<City> getCities(HttpServletRequest reqs) {
		String provincename = reqs.getParameter("provincename");
//		try {
//			provincename = new String(provincename.getBytes("ISO-8859-1"), "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		if (provincename.equals("")) {
			return null;
		}
		Province tem = provinceService.getProvinceByName(provincename);
		List<City> list = cityService.getCities(tem.getCode());
		return list;
	}

    @RequestMapping("/securi_getAreas")
    @ResponseBody
    public List<Area> getAreas(HttpServletRequest reqs) {
        String cityname = reqs.getParameter("cityname");
//		try {
//			cityname = new String(cityname.getBytes("ISO-8859-1"), "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
        if (cityname.equals("")) {
            return null;
        }
        City tem = cityService.getCityByName(cityname);
        List<Area> list = areaService.getAreas(tem.getCode());
        return list;
    }

	/**
	 * 跳转新增页面
	 * 
	 * @return
	 */
	@RequestMapping("/toAddPage")
	public String toAddPage(HttpServletRequest request) {
		List<Province> provinces = provinceService.getProvinces();
		request.setAttribute("provinces", provinces);
		return "/app/pro/addProject";
	}

	/**
	 * 删除该项目(注意，此处为软删除，并非执行delete，而是执行update，将数据库中isdel列设置为1即不显示)
	 */
	@RequestMapping("/deleteProject")
	@ResponseBody
	public Json delete(Integer id) {
		Json j = new Json();
		if (id != null) {
			this.projectService.deleteOne(id);
		}
		j.setMsg("删除成功！");
		j.setSuccess(true);
		return j;
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 *            ('0','1','2')
	 */
	@RequestMapping("/batchDeleteProject")
	@ResponseBody
	public Json batchDelete(String ids) {
		Json j = new Json();
		if (ids != null && ids.length() > 0) {
			for (String id : ids.split(",")) {
				if (id != null) {
					this.delete(id);
				}
			}
		}
		j.setMsg("批量删除成功！");
		j.setSuccess(true);
		return j;
	}

	/**
	 * 删除工程接口
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteSend")
	@ResponseBody
	public Json delete(String id) {
		Json j = new Json();
		if (id != null) {
			this.projectService.deleteOne(Integer.parseInt(id));
		}
		j.setMsg("删除成功！");
		j.setSuccess(true);
		return j;
	}

    /**
     * 保存新增数据
     * @param pro
     * @param request
     * @return
     */
	@RequestMapping("/save")
	@ResponseBody
	public Json save(Project pro, HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession()
				.getAttribute(sy.util.ConfigUtil.getSessionInfoName());
		String uid = sessionInfo.getId();
		String compid = sessionInfo.getCompid();
		Json j = new Json();
		if (pro.getProjectId() == null || "".equals(pro.getProjectId())) {
			j.setSuccess(false);
			j.setMsg("请输入项目编码！");
			return j;
		}
		if (pro.getProName() == null || "".equals(pro.getProName())) {
			j.setSuccess(false);
			j.setMsg("请输入工程合同名称！");
			return j;
		}
//		if (pro.getShortname() == null || "".equals(pro.getShortname())) {
//			j.setSuccess(false);
//			j.setMsg("请输入项目简称！");
//			return j;
//		}
		// 执行新增sql命令
		try {
			pro.setCompId(compid);
			pro.setUid(uid);
			this.projectService.add(pro);
			j.setMsg("新增成功！");
			j.setSuccess(true);
		} catch (Exception ex) {
			j.setMsg("新增失败");
			j.setSuccess(false);
		}
		return j;
	}

	/**
	 * 详情,预览，查询一个显示在一个jsp上（不可修改）
	 * 
	 * @return
	 */
	@RequestMapping("/findOneView")
	public String findOneView(HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession()
				.getAttribute(sy.util.ConfigUtil.getSessionInfoName());
		String proId = request.getParameter("proId");
		Project pro = this.projectService.findOneView(Integer.parseInt(proId));
		pro.setCompId(sessionInfo.getCompName());
		request.setAttribute("pro", pro);
//		return "/app/pro/findOneView";
        return "/app/pro/viewProject";
	}

	/**
	 * 编辑，查询一个显示在一个jsp上（可修改）
	 * 
	 * @return
	 */
	@RequestMapping("/edit")
	public String edit(HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession()
				.getAttribute(sy.util.ConfigUtil.getSessionInfoName());
		String proId = request.getParameter("proId");
		Project pro = this.projectService.findOneView(Integer.parseInt(proId));
		pro.setCompId(sessionInfo.getCompName());
		List<Province> provinces = provinceService.getProvinces();

        Province tem = provinceService.getProvinceByName(pro.getProvice());
		List<City> cities = new ArrayList<City>();
		if (tem != null) {
			cities = cityService.getCities(tem.getCode());
		}


        List<Area> areas = new ArrayList<Area>();
        if (pro.getCity() != null && !pro.getCity().equals("")) {
            City city = cityService.getCityByName(pro.getCity());
            if (city != null) {
                areas = areaService.getAreas(city.getCode());
            }

        }


		request.setAttribute("provinces", provinces);
		request.setAttribute("cities", cities);
        request.setAttribute("areas", areas);
		request.setAttribute("pro", pro);
//		return "/app/pro/edit";
        return "/app/pro/editProject";
	}

	/**
	 * 修改
	 * 
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public Json update(Project pro, HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession()
				.getAttribute(sy.util.ConfigUtil.getSessionInfoName());
		String uid = sessionInfo.getId();
		String cid = sessionInfo.getCompid();
		Json j = new Json();
		if (pro.getProjectId() == null || "".equals(pro.getProjectId())) {
			j.setSuccess(false);
			j.setMsg("请输入项目编号！");
			return j;
		}
		if (pro.getProName() == null || "".equals(pro.getProName())) {
			j.setSuccess(false);
			j.setMsg("请输入公司名称！");
			return j;
		}
		if (pro.getShortname() == null || "".equals(pro.getShortname())) {
			j.setSuccess(false);
			j.setMsg("请输入项目简称！");
			return j;
		}
		// 执行新增sql命令
		try {
			pro.setUid(uid);
			pro.setCompId(cid);
			this.projectService.update(pro);
			j.setMsg("修改成功！");
			j.setSuccess(true);
		} catch (Exception ex) {
			j.setMsg("修改失败");
			j.setSuccess(false);
		}
		return j;
	}

    /**
     * 获取数据表格（注意：此处查询的为数据库中列isdel为0，即未删除状态的数据）
     * @param app
     * @param ph
     * @param request
     * @return
     */
	@RequestMapping("/dataGrid")
	@ResponseBody
	public DataGrid dataGrid(ProjectSearch app, PageHelper ph,
			HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession()
				.getAttribute(sy.util.ConfigUtil.getSessionInfoName());
        // modify by heyh
        // 修改为按compid，查询公司所有有效工程
//		List<Integer> ugroup = sessionInfo.getUgroup();
        String compId = sessionInfo.getCompid();

		System.out.println(app);

//		DataGrid dataGrid = projectService.dataGrid(app, ph, ugroup);
        String source = StringUtil.trimToEmpty(request.getParameter("source"));

        DataGrid dataGrid = projectService.dataGrid(app, ph, compId, source);
		return dataGrid;
	}

	@RequestMapping("/securi_getProjects")
	@ResponseBody
	public Json getProjects(ProjectSearch app, PageHelper ph,
							 HttpServletRequest request) {
		Json j = new Json();
		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(sy.util.ConfigUtil.getSessionInfoName());
		String compId = sessionInfo.getCompid();
		List<S_department> departmentList  = sessionInfo.getDepartmentIds();
		List<Integer> departmentIds = new ArrayList<Integer>();
		if (departmentList != null && departmentList.size() > 0) {
			for (S_department department : departmentList) {
				departmentIds.add(department.getId());
			}
		}
		List<Map<String, Object>> projects = projectService.getProjects(compId, departmentIds);
		j.setObj(projects);
		j.setSuccess(true);
		return j;
	}

    @RequestMapping("/securi_lockProject")
    @ResponseBody
    public Json lockProject(Integer id) {
        Json j = new Json();
        if (id != null) {
            this.projectService.lockProject(id);
        }
        j.setMsg("工程锁定成功！");
        j.setSuccess(true);
        return j;
    }

    @RequestMapping("/securi_unLockProject")
    @ResponseBody
    public Json unLockProject(Integer id) {
        Json j = new Json();
        if (id != null) {
            this.projectService.unLockProject(id);
        }
        j.setMsg("工程解锁成功！");
        j.setSuccess(true);
        return j;
    }

	@RequestMapping("/securi_goAssigned")
	public ModelAndView goAssigned(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
		String cid = sessionInfo.getCompid();
		String proId = request.getParameter("proId");
		Project pro = this.projectService.findOneView(Integer.parseInt(proId));
		List<Object[]> departmentList = departmentService.getAllDepartmentList(cid);
		List<Map<String, Object>> _departmentList = new ArrayList<Map<String, Object>>();
		Map<String,Object> _department = new HashMap<String, Object>();
		for (Object[] department : departmentList) {
			_department = new HashMap<String, Object>();
			_department.put("id", department[0]);
			_department.put("pId", department[1]);
			_department.put("name", department[2]);
			if (pro.getBelongDeparts() != null && !pro.getBelongDeparts().equals("") && pro.getBelongDeparts().contains(StringUtil.trimToEmpty(department[0]))) {
				_department.put("checked", true);
			}
			_departmentList.add(_department);
		}

		mv.addObject("pro", pro);
		mv.addObject("departmentList", JSONArray.fromObject(_departmentList));
		mv.setViewName("/app/pro/assignedProject");
		return mv;
	}

	@RequestMapping("/securi_assignedProject")
	@ResponseBody
	public Json assignedProject(Project pro, HttpServletRequest request) {
		Json j = new Json();

		Project project = this.projectService.findOneView(pro.getId());
		project.setBelongDeparts(pro.getBelongDeparts());
		// 执行新增sql命令
		try {
			this.projectService.update(project);
			j.setMsg("修改成功！");
			j.setSuccess(true);
		} catch (Exception ex) {
			j.setMsg("修改失败");
			j.setSuccess(false);
		}
		return j;
	}

	@RequestMapping("/securi_getAllProjects")
	@ResponseBody
	public List<Project> getAllProjects(HttpServletRequest request) {
		Json j = new Json();
		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(sy.util.ConfigUtil.getSessionInfoName());
		String compId = sessionInfo.getCompid();
		String uid = sessionInfo.getId();
		List<Project> projectList = projectService.initDefaultProject(compId, uid);
//		j.setObj(projectList);
//		j.setSuccess(true);
		return projectList;
	}
}
