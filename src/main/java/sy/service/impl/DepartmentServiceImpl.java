package sy.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.DepartmentDaoI;
import sy.model.S_department;
import sy.model.po.Department;
import sy.service.DepartmentServiceI;
import sy.util.Node;
import sy.util.NodeUtil;

import java.util.*;

@Service
public class DepartmentServiceImpl implements DepartmentServiceI {

	@Autowired
	DepartmentDaoI departmentDaoI;

	@Override
	public Department findOneView(String uid,String cid) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<Object[]> objects = null;
		List<Object[]> objectByid = null;
		if(cid!=null){
//			objects = departmentDaoI
//					.findBySql("select job_id from jsw_corporation_department where user_id="
//							+ uid +" and company_id = "+cid);
            objects = departmentDaoI
                    .findBySql("select job_id from jsw_corporation_department where FIND_IN_SET(" + uid + ", user_id) and company_id = "+cid);
		}else{
//			objects = departmentDaoI
//					.findBySql("select job_id from jsw_corporation_department where user_id=" + uid );
            objects = departmentDaoI
                    .findBySql("select job_id from jsw_corporation_department where FIND_IN_SET(" + uid + ", user_id)");
		}
		
		params.put("id", objects.get(0));
		Department department = departmentDaoI.get("from Department where id=:id", params);
		//查询超级管理员
		if(cid!=null){
			objectByid = departmentDaoI.findBySql("select user_id from jsw_corporation_department where user_id <> 0 and parent_id=0 and company_id=" + cid);
		}else{
			objectByid = departmentDaoI.findBySql("select user_id from jsw_corporation_department where user_id <> 0 and parent_id=0 ");
		}
		//遍历超级管理员
		for(int i=0;i<objectByid.size();i++){
			//如果是超级原理员设置parent_id=0
//			if(uid.equals(String.valueOf(objectByid.get(i)))){
            if(String.valueOf(objectByid.get(i)).contains(uid)) {
				department  = new Department();
				department.setParent_id(0);
			}
		}
		return department;
	}

	@Override
	public List<Integer> getListByDepartmentId(int id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		List<Integer> list = new ArrayList<Integer>();
		// list.add(id);
		Department d = departmentDaoI.get("from Department where id=:id",
				params);
		if(d!=null){
			getGroup(d, list);
		}
		return list;
	}

	public void getGroup(Department d, List<Integer> list) {
		if (d.getIsend() == 1) {
			list.add(d.getId());
			return;
		}
		List<Department> dlist = departmentDaoI
				.find("from Department where parent_id=" + d.getId());
		for (int i = 0; i < dlist.size(); i++) {
			list.add(dlist.get(i).getId());
			getGroup(dlist.get(i), list);
		}
	}

	@Override
	public List<Integer> getUserGroup(Department d, String uid,String cid) {
		List<Object[]> objects = null;
		if(cid!=null){
//			objects = departmentDaoI.findBySql("select parent_id from jsw_corporation_department where user_id=" + uid +" and company_id= " +cid);
            objects = departmentDaoI.findBySql("select parent_id from jsw_corporation_department where FIND_IN_SET(" + uid + ", user_id) and company_id= " +cid);
        }else{
//			objects = departmentDaoI.findBySql("select parent_id from jsw_corporation_department where user_id=" + uid );
            objects = departmentDaoI.findBySql("select parent_id from jsw_corporation_department where  FIND_IN_SET(" + uid + ", user_id)" );
		}
		
		List<Integer> list = new ArrayList<Integer>();
		if(d!=null){
			if (d.getParent_id() == 0 && "0".equals(String.valueOf(objects.get(0)))) {
//                list = (List<Integer>) departmentDaoI
//                        .getList("select distinct(user_id) from jsw_corporation_department where company_id="
//								+ cid + " order by user_id desc");
                List<String> tmpList = new ArrayList<String>();
                tmpList = (List<String>) departmentDaoI
                        .getList("select distinct(user_id) from jsw_corporation_department where company_id="
                                + cid + " order by user_id desc");
                for (String o : tmpList) {
                    String [] arr = o.split(",");
                    for (String str : arr) {
                        list.add(Integer.parseInt(str));
                    }
                }

				return list;
			}
		}
		
		
		//根据用户的公司id部门id查询有没有job表中没有的普通员工
		List<Object[]> notFindJob = null;
		List<Object[]> notFindJobByid = null;
		String jobsql = "select user_id,job_id from jsw_corporation_department where company_id="+cid+" and parent_id ="+objects.get(0);
		notFindJob = departmentDaoI.findBySql(jobsql);
		
		String str = null;
		String notjobsql = null;
		for(int i=0;i<notFindJob.size();i++){
			notjobsql = "select id from jsw_corporation_job where parent_id ="+objects.get(0)+" and  id = "+notFindJob.get(i)[1];
			notFindJobByid = departmentDaoI.findBySql(notjobsql);
			if(notFindJobByid == null){
				str = ","+(String) notFindJob.get(i)[0];
			}
		}
		
		
		Object did = objects.get(0);
		List<Integer> dgroup =null;
		if(d!=null){
			dgroup = this.getListByDepartmentId(d.getId());
		}
		System.out.println(did);
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct(user_id) from jsw_corporation_department where parent_id=" + did );
		if(dgroup!=null&&dgroup.size()>0){
			sql.append(" and job_id in(2,");
			sql.append(dgroup.get(0).toString());
			for (int i = 1; i < dgroup.size(); i++) {
				sql.append("," + dgroup.get(i).toString());
			}
			if(str!=null){
				sql.append(str);
			}
			sql.append(")");
		}
		list = (List<Integer>) departmentDaoI.getList(sql.toString());
		list.add(Integer.parseInt(uid));
		return list;
	}

	@Override
	public List<Department> getDepartments(String cid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", Integer.parseInt(cid));
		List<Department> departments = departmentDaoI.find(
				"from Department where company_id=:cid", params);
		return departments;
	}

	@Override
	public S_department getDepartmentByUid(String uid,String cid) {
		Integer id = null;
		if(cid!=null){
			id = (Integer) departmentDaoI
					.getObject("select parent_id from S_department where user_id=" + uid + " and company_id = " + cid);
		}else{
			id = (Integer) departmentDaoI
					.getObject("select parent_id from S_department where user_id=" + uid);
		}
		S_department department = new S_department();
			//部门信息也是查询jsw_corporation_department,先通过用户id和公司id查询部门id,用部门id(parent_id)查询jsw_corporation_department,通过id查询部门信息
			String sql = "select id,name from jsw_corporation_department where id=" + id;
			List<Object[]> deps = departmentDaoI.findBySql(sql);
			for (Object[] tem : deps) {
				department.setId((Integer) tem[0]);
				department.setName((String) tem[1]);
			}
		
		return department;
	}


    @Override
    public List<Integer> getUsers(String cid, int uid) {
        List<Integer> uids = new ArrayList<Integer>();
        List<Object[]> objects = null;
        int id = -1;
        objects = departmentDaoI.findBySql("select id, parent_id, user_id, company_id from jsw_corporation_department where company_id= " +cid);
        List<Node> departments = new ArrayList<Node>();
        for (Object[] object : objects) {
            List<String> tmpUid = Arrays.asList(String.valueOf(object[2]).split(","));
            for (String tmp : tmpUid) {
                Node department = new Node();
                department.setId(Integer.parseInt(String.valueOf(object[0])));
                department.setParentId(Integer.parseInt(String.valueOf(object[1])));
                department.setUserId(Integer.parseInt(tmp));
//                department.setCompanyId(Integer.parseInt(String.valueOf(object[3])));
                if (uid == Integer.parseInt(tmp)) {
                    id = Integer.parseInt(String.valueOf(object[0]));
                }
                departments.add(department);
            }

        }
        if (id != -1) {
//            String strUids = getChildNodes(id, departments);
            NodeUtil nodeUtil = new NodeUtil();
            uids = nodeUtil.getChildNodes(departments, id);
//            if (strUids != null && !strUids.equals("")) {
//                CollectionUtils.collect(Arrays.asList(strUids.split(",")),
//                        new Transformer() {
//                            public java.lang.Object transform(java.lang.Object input) {
//                                return new Integer((String) input);
//                            }
//                        }, uids);
//            }
        }

        return uids;
    }

    @Override
    public List<S_department> getDepartmentsByUid(String uid,String cid) {
        List<Integer> ids = null;
        if(cid!=null){
            ids= (List<Integer>) departmentDaoI.getList("select parent_id from jsw_corporation_department where FIND_IN_SET(" + uid + ", user_id)" + " and company_id = " + cid);
        }else{
            ids = (List<Integer>) departmentDaoI.getList("select parent_id from jsw_corporation_department where FIND_IN_SET(" + uid + ", user_id)");
        }
        List<S_department> departments = new ArrayList<S_department>();
        for (Integer id : ids) {
            S_department department = new S_department();
            //部门信息也是查询jsw_corporation_department,先通过用户id和公司id查询部门id,用部门id(parent_id)查询jsw_corporation_department,通过id查询部门信息
            String sql = "select id,name,parent_id from jsw_corporation_department where id=" + id;
            List<Object[]> deps = departmentDaoI.findBySql(sql);
            for (Object[] tem : deps) {
                department.setId((Integer) tem[0]);
                department.setName((String) tem[1]);
//                department.setParent_id((Integer) tem[2]);
            }
            departments.add(department);
        }

        return departments;
    }

    @Override
    public boolean hasRight(String cid, int uid, String rightCode) {
        boolean hasRight = false;
        List<String> rightsList = new ArrayList<String>();
        if (null != cid) {
            rightsList = (List<String>) departmentDaoI.getList("select cbgl_power from jsw_corporation_department where FIND_IN_SET(" + uid + ", user_id)" + " and company_id = " + cid);
        } else {
            rightsList = (List<String>) departmentDaoI.getList("select cbgl_power from jsw_corporation_department where FIND_IN_SET(" + uid + ", user_id)");
        }

        if (null != rightsList && rightsList.size() > 0) {
            for (String rights : rightsList) {
                if (hasRight) {
                    break;
                }

                if (StringUtils.isNotEmpty(rights)) {
                    String[] rightArr = rights.split(",");
                    if (rightArr.length > 0) {
                        for (String right : rightArr) {
                            if (right.equals(rightCode)) {
                                hasRight = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        return hasRight;
    }

    @Override
    public List<String> getAllRight(String cid, int uid) {
        List<String> rightList = new ArrayList<String>();
        List<String> rightsList = new ArrayList<String>();
        if (null != cid) {
            rightsList = (List<String>) departmentDaoI.getList("select cbgl_power from jsw_corporation_department where FIND_IN_SET(" + uid + ", user_id)" + " and company_id = " + cid);
        } else {
            rightsList = (List<String>) departmentDaoI.getList("select cbgl_power from jsw_corporation_department where FIND_IN_SET(" + uid + ", user_id)");
        }

        if (null != rightsList && rightsList.size() > 0) {
            for (String rights : rightsList) {
                if (StringUtils.isNotEmpty(rights)) {
                    String[] rightArr = rights.split(",");
                    if (rightArr.length > 0) {
                        for (String right : rightArr) {
                            rightList.add(right);
                        }
                    }
                }
            }
        }
        if (null != rightList && rightList.size() > 1) {
            Set<String> set = new HashSet<String>(rightList);
            rightList = new ArrayList<String>(set);
        }
        return rightList;
    }

    @Override
    public int getParentId(String cid, int uid) {
        int parentId = 1;
        List<Integer> ids = null;
        if(cid!=null){
            ids= (List<Integer>) departmentDaoI.getList("select parent_id from jsw_corporation_department where FIND_IN_SET(" + uid + ", user_id)" + " and company_id = " + cid);
        }else{
            ids = (List<Integer>) departmentDaoI.getList("select parent_id from jsw_corporation_department where FIND_IN_SET(" + uid + ", user_id)");
        }
        for (Integer id : ids) {
            if (0 == id) {
                parentId = 0;
                break;
            } else {
                parentId = id;
            }
        }
        return parentId;
    }

}
