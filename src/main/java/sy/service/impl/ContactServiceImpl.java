package sy.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.ContactDaoI;
import sy.model.Contact;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.service.ContactServiceI;

@Service
public class ContactServiceImpl implements ContactServiceI {

	@Autowired
	ContactDaoI contactDao;

	@Override
	public DataGrid dataGrid(int pid, PageHelper ph) {
		DataGrid dg = new DataGrid();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pid", pid);
		String hql = " from Contact where pid=:pid";
		List<Contact> list = contactDao.find(hql, params, ph.getPage(),
				ph.getRows());
		dg.setRows(list);
		dg.setTotal(contactDao.count("select count(*) " + hql, params));
		return dg;
	}

	@Override
	public void delete(Contact tem) {
		contactDao.delete(tem);
	}

	@Override
	public Contact update(Contact tem) {
		contactDao.update(tem);
		return tem;
	}

	@Override
	public Contact findoneview(int id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Contact tem = contactDao
				.get(" from Contact where id=:id", params);
		return tem;
	}

	@Override
	public Contact add(Contact tem) {
		contactDao.save(tem);
		return tem;
	}

}
