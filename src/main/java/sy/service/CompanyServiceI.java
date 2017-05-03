package sy.service;

import org.springframework.stereotype.Repository;

import sy.model.po.Company;

import java.util.List;

/**
 * **************************************************************** 文件名称 :
 * ApplicationServiceI.java 作 者 : Administrator 创建时间 : 2015年6月4日11:18:23 文件描述 :
 * project接口，管理项目名称的增删改查等功能 版权声明 : 修改历史 : 2015年6月4日 初始版本
 *****************************************************************
 */
public interface CompanyServiceI {

	/**
	 * 查询一条纪录
	 * 
	 * @param id
	 */
	public Company findOneView(String id,String cid);

	public List<Company> getCompanyInfos(String uid, String cid);

}
