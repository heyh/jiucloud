package sy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.S_city;
import sy.model.S_province;
import sy.model.po.Project;
import sy.pageModel.*;
import sy.service.CityServiceI;
import sy.service.ProjectServiceI;
import sy.service.ProvinceServiceI;
import sy.util.StringUtil;
import sy.util.UtilDate;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

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
	public List<S_city> getCities(HttpServletRequest reqs) {
		String pidVar = reqs.getParameter("provincename");
//		try {
//			pidVar = new String(pidVar.getBytes("ISO-8859-1"), "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		S_province tem = provinceService.getProvinceByName(pidVar);
		List<S_city> list = cityService.getCities(tem.getProvinceid());
		return list;
	}

	/**
	 * 跳转新增页面
	 * 
	 * @return
	 */
	@RequestMapping("/toAddPage")
	public String toAddPage(HttpServletRequest request) {
		List<S_province> provinces = provinceService.getProvinces();
		request.setAttribute("provinces", provinces);
		return "/app/pro/saveprolis";
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
	 * 
	 * @param id
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
			j.setMsg("请输入项目编号！");
			return j;
		}
		if (pro.getProName() == null || "".equals(pro.getProName())) {
			j.setSuccess(false);
			j.setMsg("请输入项目名称！");
			return j;
		}
		if (pro.getShortname() == null || "".equals(pro.getShortname())) {
			j.setSuccess(false);
			j.setMsg("请输入项目简称！");
			return j;
		}
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
		return "/app/pro/findOneView";
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
		List<S_province> provinces = provinceService.getProvinces();
		S_province tem = provinceService.getProvinceByName(pro.getProvice());
		List<S_city> cities = cityService.getCities(tem.getProvinceid());
		request.setAttribute("provinces", provinces);
		request.setAttribute("cities", cities);
		request.setAttribute("pro", pro);
		return "/app/pro/edit";
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
	 * 
	 * @param user
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

    @RequestMapping("/lockProject")
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

    @RequestMapping("/unLockProject")
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
}
