package sy.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.GCPoDaoI;
import sy.model.po.GCPo;
import sy.pageModel.DataGrid;
import sy.service.GCPoServiceI;

@Service(value = "gcpoServiceImpl")
public class GCPoServiceImpl implements GCPoServiceI {

	@Autowired
	GCPoDaoI gcpoDao;

	@Override
	public void add(GCPo gc) {
		gcpoDao.save(gc);
	}

	@Override
	public void deleteOne(Integer id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		GCPo tem = gcpoDao.get("from GCPo where id=:id", params);
		gcpoDao.delete(tem);
	}

	@Override
	public GCPo findOneView(Integer id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		GCPo tem = gcpoDao.get("from GCPo where id=:id", params);
		return tem;
	}

	@Override
	public void update(GCPo gc) {
		gcpoDao.update(gc);
	}

	@Override
	public DataGrid dataGrid(String id, String filename, String filetype) {
		DataGrid dg = new DataGrid();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		String hql = " from GCPo where mpid=:id ";
		if (filename != null && filename.length() > 0) {
			hql += " and fileName like :filename";
			params.put("filename", "%" + filename + "%");
		}
		if (filetype != null && filetype.length() > 0) {
			hql += " and ext like :filetype ";
			params.put("filetype", "%" + filetype + "%");
		}
		List<GCPo> l = gcpoDao.find(hql, params);
		System.out.println(l);
		dg.setRows(l);
		dg.setTotal(gcpoDao.count("select count(*) " + hql, params));
		return dg;
	}

	@Override
	public long getFieldCount(String mid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mid", mid);
		return gcpoDao.count("select count(*) from GCPo where mpid=:mid",
				params);
	}

	@Override
	public List<GCPo> getGcpoListFile(String type, String mpid) {
		Map<String, Object> params = new HashMap<String, Object>();

		String hql = "from GCPo where ";
		if (type.equals("1")) {
			hql += "mpid=:mpid";
			params.put("mpid", mpid);
		} else {
			hql += " id in (" + mpid + ")";
		}
		List<GCPo> list = gcpoDao.find(hql, params);
		System.out.println(list);
		return list;
	}
}
