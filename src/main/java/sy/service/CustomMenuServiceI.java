package sy.service;

import java.util.HashMap;
import java.util.List;

import sy.model.po.Button;
import sy.model.po.CustomMenu;
import sy.pageModel.AppSearch;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;


/**
 * ****************************************************************
 * 文件名称 : CustomMenuServiceI.java
 * 作 者 :   tcp
 * 创建时间 : 2015年1月9日 下午3:00:51
 * 文件描述 : 服务号自定义菜单Service
 * 版权声明 : 
 * 修改历史 : 2015年1月9日 1.00 初始版本
 *****************************************************************
 */
public interface CustomMenuServiceI {
	
	/*
	 * 新增一级菜单
	 */
	public Integer addPrimaryMenu(String PrimaryName);
	
	/*
	 * 编辑一级菜单
	 */
	public void updatePrimaryMenu(Button b);
	
	/*
	 * 删除一级菜单
	 */
	public void delPrimaryMenu(String id);
	
	
	/**
	 * 根据id查询button
	 * 方法表述
	 * @param id
	 * @return
	 * Button
	 */
	public Button getMenuById(String id);
	
	/**
	 * 根据id获得CustomMenu菜单
	 */
	public CustomMenu getCustomMenu(String id);
	
	/**
	 * 按服务号名查询菜单
	 * 方法表述
	 * @param fwName
	 * @return
	 * CustomMenu
	 */
	public CustomMenu getCustomMenuByFw(String fwName);
	/**
	 * 保存菜单
	 * 方法表述
	 * @param menu
	 * void
	 */
	public void saveMenu(CustomMenu menu);
	
	
	/*
	 * 查询自定义菜单
	 */
	public String findAllCustomMenu(String id);
	
	/**
	 * 根据 服务号获取服务号菜单信息   
	 * 方法表述
	 * @param id
	 * void
	 */
	public CustomMenu getMenu(String id);
	
	/**
	 * 获得所有服务号菜单信息
	 * 方法表述
	 * @param app
	 * @param ph
	 * @return
	 * DataGrid
	 */
	public DataGrid dataGrid(AppSearch app, PageHelper ph);
}
