package sy.service.impl;

import org.springframework.stereotype.Service;

import sy.model.po.Button;
import sy.model.po.CustomMenu;
import sy.pageModel.AppSearch;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.service.CustomMenuServiceI;
@Service
public class CustomMenuServiceImpl implements CustomMenuServiceI{

	@Override
	public Integer addPrimaryMenu(String PrimaryName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePrimaryMenu(Button b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delPrimaryMenu(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Button getMenuById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomMenu getCustomMenu(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomMenu getCustomMenuByFw(String fwName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveMenu(CustomMenu menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String findAllCustomMenu(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomMenu getMenu(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataGrid dataGrid(AppSearch app, PageHelper ph) {
		// TODO Auto-generated method stub
		return null;
	}

}
