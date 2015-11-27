package sy.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Repository;

import sy.model.po.Project;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.pageModel.ProjectSearch;

/**
 * **************************************************************** 文件名称 :
 * ApplicationServiceI.java 作 者 : Administrator 创建时间 : 2015年6月4日11:18:23 文件描述 :
 * project接口，管理项目名称的增删改查等功能 版权声明 : 修改历史 : 2015年6月4日 初始版本
 *****************************************************************
 */
@Repository
public interface ProjectServiceI {

	/**
	 * 获取项目名称列表
	 */
	DataGrid dataGrid(ProjectSearch app, PageHelper ph, List<Integer> ugroup);

    // add by heyh
    DataGrid dataGrid(ProjectSearch app, PageHelper ph, String compId);

	/**
	 * 获取项目预览，查询一个
	 */

	/**
	 * 新增
	 */
	void add(Project pro);

	/**
	 * 删除一个，软删除
	 * 
	 * @param id
	 */
	void deleteOne(Integer id);

	/**
	 * 查询一个（当执行编辑功能，预览功能调用此方法返回一个Project对象）
	 * 
	 * @param proId
	 * @return
	 */
	Project findOneView(Integer id);

	public List<Project> findListView(String id,String cid);
	
	/**
	 * 修改
	 * 
	 * @param pro
	 * @param uid
	 */
	void update(Project pro);

	List<Project> getProjectsAfterNow(String cid);

	List<Project> getProjects(List<Integer> ugroup);

    public String getProjectInfos(String cid);
}
