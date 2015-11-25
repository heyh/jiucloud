package sy.service;

import org.springframework.stereotype.Repository;

import sy.model.po.Tpcost;

/**
 * **************************************************************** 文件名称 :
 * ApplicationServiceI.java 作 者 : Administrator 创建时间 : 2015年6月4日11:18:23 文件描述 :
 * project接口，管理项目名称的增删改查等功能 版权声明 : 修改历史 : 2015年6月4日 初始版本
 *****************************************************************
 */
@Repository
public interface TpcostServiceI {

	void add(Tpcost tpcost);

	/**
	 * 查询一个（当执行编辑功能，预览功能调用此方法返回一个Project对象）
	 * 
	 * @param proId
	 * @return
	 */
	Tpcost findOneView(Integer id);

	Tpcost findOneView(Tpcost tpcost);

	void delete(Tpcost tpcost);

}
