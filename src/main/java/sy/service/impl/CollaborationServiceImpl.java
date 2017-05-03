package sy.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.CollaborationDaoI;
import sy.model.Collaboration;
import sy.model.Item;
import sy.pageModel.DataGrid;
import sy.pageModel.FieldData;
import sy.pageModel.PageHelper;
import sy.pageModel.PmCollaboration;
import sy.service.CollaborationServiceI;
import sy.service.ItemServiceI;

@Service
public class CollaborationServiceImpl implements CollaborationServiceI {

	@Autowired
	CollaborationDaoI collaborationDao;

	@Autowired
	private ItemServiceI itemService;

	@Override
	public DataGrid dataGrid(String cid, String pid, PageHelper ph) {
		DataGrid dg = new DataGrid();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pid", Integer.parseInt(pid));
		String hql = " from Collaboration where pid=:pid";
		List<Collaboration> list = collaborationDao.find(hql, params, ph.getPage(), ph.getRows());
		List<PmCollaboration> pmCollaborationList = new ArrayList<PmCollaboration>();
		for (Collaboration collaboration : list) {
			PmCollaboration pmCollaboration = new PmCollaboration();
			pmCollaboration.setId(collaboration.getId());
			pmCollaboration.setSection(collaboration.getSection());
			Item sectionItem = itemService.getSingleItem(cid, pid, collaboration.getSection());
			if (sectionItem == null) {
				pmCollaboration.setSectionName("标段1");
			} else {
				pmCollaboration.setSectionName(sectionItem.getName());
			}
			pmCollaboration.setName(collaboration.getName());
			pmCollaboration.setAddress(collaboration.getAddress());
			pmCollaboration.setEmail(collaboration.getEmail());
			pmCollaboration.setIndustry(collaboration.getIndustry());
			pmCollaboration.setPower(collaboration.getPower());
			pmCollaboration.setRemark(collaboration.getRemark());
			pmCollaboration.setTel(collaboration.getTel());
			pmCollaborationList.add(pmCollaboration);
		}
		dg.setRows(pmCollaborationList);
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
		Collaboration tem = collaborationDao.get(" from Collaboration where id=:id",params);
		return tem;
	}

	@Override
	public Collaboration add(Collaboration tem) {
		collaborationDao.save(tem);
		return tem;
	}

}
