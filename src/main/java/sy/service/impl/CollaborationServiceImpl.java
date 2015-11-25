package sy.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.CollaborationDaoI;
import sy.model.Collaboration;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.service.CollaborationServiceI;

@Service
public class CollaborationServiceImpl implements CollaborationServiceI {

	@Autowired
	CollaborationDaoI collaborationDao;

	@Override
	public DataGrid dataGrid(String pid, PageHelper ph) {
		DataGrid dg = new DataGrid();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pid", Integer.parseInt(pid));
		String hql = " from Collaboration where pid=:pid";
		List<Collaboration> list = collaborationDao.find(hql, params,
				ph.getPage(), ph.getRows());
		dg.setRows(list);
		dg.setTotal(collaborationDao.count("select count(*) " + hql, params));
		return dg;
	}

	@Override
	public void delete(Collaboration tem) {
		collaborationDao.delete(tem);
	}

	@Override
	public Collaboration update(Collaboration tem) {
		collaborationDao.update(tem);
		return tem;
	}

	@Override
	public Collaboration findoneview(int id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Collaboration tem = collaborationDao
				.get(" from Collaboration where id=:id",params);
		return tem;
	}

	@Override
	public Collaboration add(Collaboration tem) {
		collaborationDao.save(tem);
		return tem;
	}

}
