package sy.service.impl;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.ResourceDaoI;
import sy.dao.RoleDaoI;
import sy.dao.UserDaoI;
import sy.model.Tresource;
import sy.model.Tuser;
import sy.pageModel.User;
import sy.service.UserServiceI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserServiceI {

	@Autowired
	private UserDaoI userDao;

	@Autowired
	private RoleDaoI roleDao;

	@Autowired
	private ResourceDaoI resourceDao;

	@Override
	public User login(String name, String pwd) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("pwd", pwd);
		User user = new User();
//		Tuser t = userDao.get("from Tuser t where t.username=:name and password=md5(concat(md5(:pwd),safecode))", params);
        Tuser t = userDao.get("from Tuser t where (t.username=:name or t.mobile_phone=:name or t.email=:name or t.qq=:name) and password=md5(concat(md5(:pwd),safecode))", params);
		if (t != null) {
			BeanUtils.copyProperties(t, user);
			return user;
		}
		return null;
	}

	@Override
	public User get(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Tuser t = userDao.get("from Tuser where id = :id", params);
		User u = new User();
		BeanUtils.copyProperties(t, u);
		// if (t.getTroles() != null && !t.getTroles().isEmpty()) {
		// String roleIds = "";
		// String roleNames = "";
		// boolean b = false;
		// for (Trole role : t.getTroles()) {
		// if (b) {
		// roleIds += ",";
		// roleNames += ",";
		// } else {
		// b = true;
		// }
		// roleIds += role.getId();
		// roleNames += role.getName();
		// }
		// u.setRoleIds(roleIds);
		// u.setRoleNames(roleNames);
		// }
		return u;
	}

	@Override
	public User getUser(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Tuser t = userDao.get(" from Tuser t  where t.id = :id", params);
		User u = new User();
		BeanUtils.copyProperties(t, u);
		return u;
	}

	@Override
	public List<String> resourceList(String id, int isadmin) {
		List<String> resourceList = new ArrayList<String>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		String hql = "from Tresource";
		if (isadmin == 0) {
			hql += " where id!='item_jiucloud_menu_data'";
		}
		List<Tresource> list = resourceDao.find(hql);
		for (Tresource tem : list) {
			resourceList.add(tem.getUrl());
		}
		System.out.println(resourceList.size());
		return resourceList;
	}

	public List<User> findallUser(String cid, String uid) {
		List<User> ul = new ArrayList<User>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", cid);
		List<Integer> uuu = (List<Integer>) userDao
				.getList("select user_id from jsw_corporation_department where endnode=1 and  company_id="
						+ cid + " and user_id!=" + uid);
		StringBuffer hql = new StringBuffer();
		hql.append("from Tuser where id in(");
		if (uuu != null && uuu.size() > 0) {
			hql.append(uuu.get(0).toString());
			for (int i = 1; i < uuu.size(); i++) {
				hql.append("," + uuu.get(i).toString());
			}
			hql.append(")");
		}
		List<Tuser> tl = userDao.find(hql.toString());
		System.out.println(tl.size() + "   ===================");
		if (tl != null && tl.size() > 0) {
			for (Tuser t : tl) {
				User u = new User();
				u.setId(t.getId());
				u.setUsername((t.getUsername()));
				// u.setCreatedatetime(t.getCreatedatetime());
				// u.setModifydatetime(t.getModifydatetime());
				// u.setDescription(t.getDescription());
				// u.setUariva(t.getUariva());// 头像地址
				// u.setRealname(t.getRealname());// 名称
				ul.add(u);
			}
		}
		return ul;
	}

    public String findUnderlingUsers(List<Integer> uids) {
        StringBuffer hql = new StringBuffer();
        if (uids!=null && uids.size()>0) {
            hql.append("from Tuser where id in (");
            hql.append(StringUtils.join(uids, ","));
            hql.append(")");
        } else {
            return "";
        }
        List<Tuser> users = userDao.find(hql.toString());
        List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>();
        Map<String, Object> tmpMap = new HashMap<String, Object>();
        if (users != null && users.size() >0) {
            for (Tuser user : users) {
                tmpMap = new HashMap<String, Object>();
                String name = user.getRealname()!=null && !user.getRealname().equals("") ? user.getRealname() : user.getUsername();
                tmpMap.put("id", name);
                tmpMap.put("text", name);
                tmpList.add(tmpMap);
            }
        }
        return JSON.toJSONString(tmpList);
    }
}
