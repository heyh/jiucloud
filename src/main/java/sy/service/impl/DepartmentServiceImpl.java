package sy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.DepartmentDaoI;
import sy.model.S_department;
import sy.model.po.Department;
import sy.service.DepartmentServiceI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			objects = departmentDaoI
					.findBySql("select job_id from jsw_corporation_department where user_id="
							+ uid +" and company_id = "+cid);
		}else{
			objects = departmentDaoI
					.findBySql("select job_id from jsw_corporation_department where user_id="
							+ uid );
		}
		
		params.put("id", objects.get(0));
		Department department = departmentDaoI.get(
				"from Department where id=:id", params);
		//查询超级管理员
		if(cid!=null){
			objectByid = departmentDaoI
					.findBySql("select user_id from jsw_corporation_department where user_id <> 0 and parent_id=0 and company_id="+cid);
		}else{
			objectByid = departmentDaoI
					.findBySql("select user_id from jsw_corporation_department where user_id <> 0 and parent_id=0 ");
		}
		//遍历超级管理员
		for(int i=0;i<objectByid.size();i++){
			//如果是超级原理员设置parent_id=0
			if(uid.equals(String.valueOf(objectByid.get(i)))){
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
			objects = departmentDaoI
					.findBySql("select parent_id from jsw_corporation_department where user_id="
							+ uid +" and company_id= " +cid);
		}else{
			objects = departmentDaoI
					.findBySql("select parent_id from jsw_corporation_department where user_id="
							+ uid );
		}
		
		List<Integer> list = null;
		if(d!=null){
			if (d.getParent_id() == 0&&"0".equals(String.valueOf(objects.get(0)))) {
				list = (List<Integer>) departmentDaoI
						.getList("select distinct(user_id) from jsw_corporation_department where company_id="
								+ cid + " order by user_id desc");
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
		sql.append("select distinct(user_id) from jsw_corporation_department where parent_id="
				+ did );
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
					.getObject("select parent_id from S_department where user_id=" + uid + " and company_id = "+cid);
		}else{
			id = (Integer) departmentDaoI
					.getObject("select parent_id from S_department where user_id=" + uid );
		}
		S_department department = new S_department();
			//部门信息也是查询jsw_corporation_department,先通过用户id和公司id查询部门id,用部门id(parent_id)查询jsw_corporation_department,通过id查询部门信息
			String sql = "select id,name from jsw_corporation_department where id="
					+ id;
			List<Object[]> deps = departmentDaoI.findBySql(sql);
			for (Object[] tem : deps) {
				department.setId((Integer) tem[0]);
				department.setName((String) tem[1]);
			}
		
		return department;
	}
}
