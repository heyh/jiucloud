package sy.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.ProjectDaoI;
import sy.model.po.Project;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.pageModel.ProjectSearch;
import sy.service.ProjectServiceI;
import sy.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * **************************************************************** 文件名称 :
 * IMUserServiceImpl.java 作 者 : Administrator 创建时间 : 2015年6月4日11:23:24 文件描述 :
 * 轻应用Service 版权声明 : 修改历史 : 2015年6月4日 1.00 初始版本
 *****************************************************************
 */

@Service
public class ProjectServiceImpl implements ProjectServiceI {

	@Autowired
	private ProjectDaoI projectDao;

	// 加载页面list
	@Override
	public DataGrid dataGrid(ProjectSearch app, PageHelper ph,
			List<Integer> ugroup, List<Integer> departmentIds) {
		DataGrid dg = new DataGrid();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from Project p  where 1=1 ";

		System.out.println(hql + whereHql(app, params, ugroup));
		// 暂时不传入uid
		List<Project> l = projectDao.find(hql + whereHql(app, params, ugroup),
				params, ph.getPage(), ph.getRows());
		dg.setRows(l);
		dg.setTotal(projectDao.count(
				"select count(*) " + hql + whereHql(app, params, ugroup),
				params));
		return dg;
	}

	@Override
	public List<Project> getProjects(List<Integer> ugroup) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from Project p  where 1=1 ";
		List<Project> l = projectDao.find(hql + whereHql(null, params, ugroup),
				params);
		return l;
	}

	// 查询时使用的动态添加where条件
	private String whereHql(ProjectSearch app, Map<String, Object> params,
			List<Integer> ugroup) {
		StringBuffer hql = new StringBuffer();
		// 如果app中有值，则代表需要模糊查询
		if (null != app) {
			// 判断项目ID是否为空，否则填充查询参数
			if (null != app.getProjetcId() && !("".equals(app.getProjetcId()))) {
				hql.append(" and p.projectId like '%'||:projectId||'%' ");
				params.put("projectId", app.getProjetcId());
			}
			if (null != app.getGczt() && !("".equals(app.getGczt()))) {
				hql.append(" and p.gczt like '%'||:gczt||'%' ");
				params.put("gczt", app.getGczt());
			}
			if (null != app.getManager() && !("".equals(app.getManager()))) {
				hql.append(" and p.manager like '%'||:manager||'%' ");
				params.put("manager", app.getManager());
			}
			// 判断项目名称是否为空，否则填充查询参数
			if (null != app.getProName() && !("".equals(app.getProName()))) {
				hql.append(" and p.proName like '%'||:proName||'%' ");
				params.put("proName", app.getProName());
			}
			// 判断项目开工日期是否为空，否则填充查询参数
			if (null != app.getKgrq() && !("".equals(app.getKgrq().toString()))) {
				hql.append(" and p.kgrq >= date(:kgrq) ");
				params.put("kgrq", app.getKgrq());
			}
			// 判断项目竣工日期是否为空，否则填充查询参数
			if (null != app.getJgrq() && !("".equals(app.getJgrq().toString()))) {
				hql.append(" and p.kgrq <= date(:jgrq) ");
				params.put("jgrq", app.getJgrq());
			}
		}
		if (ugroup != null && ugroup.size() > 0) {
			hql.append(" and uids in(" + ugroup.get(0).toString());
			for (int i = 1; i < ugroup.size(); i++) {
				hql.append("," + ugroup.get(i).toString());
			}
			hql.append(") ");
		}
		hql.append(" and p.isdel = 0 order by p.id desc");
		return hql.toString();

	}

	// 新增
	@Override
	public void add(Project pro) {
		try {
			this.projectDao.save(pro);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除一个
	@Override
	public void deleteOne(Integer id) {
		try {
			// this.projectDao.delete(this.projectDao.get("from Project p where p.id = "+id));
			Project p = this.projectDao
					.get("from Project p where p.id = " + id);
			p.setIsdel(1);
			this.projectDao.update(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 查询一个，预览
	@Override
	public Project findOneView(Integer id) {
		Project p = new Project();
		try {
			p = this.projectDao.get("from Project p where p.id = " + id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	
	// 查询多个
		@Override
		public List<Project> findListView(String id,String cid) {
			List<Project> p = null;
			try {
				p = this.projectDao.find("from Project p where p.proName like '%%" + id+"%%' and compId="+cid);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return p;
		}

	@Override
	public void update(Project pro) {
		try {
			this.projectDao.update(pro);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Project> getProjectsAfterNow(String cid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", cid);
		String hql = " from Project where isdel=0 and jgrq>now() and compId=:cid";
		List<Project> l = projectDao.find(hql, params);
		return l;
	}

    @Override
    public String getProjectInfos(String cid, List<Integer> departmentIds) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        String hql = " from Project where isdel=0 and compId=:cid";
        List<Project> proList = projectDao.find(hql, params);
        List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>();
        Map<String, Object> tmpMap = new HashMap<String, Object>();
        boolean belongFlag = false;
        for (Project pro : proList) {
            tmpMap = new HashMap<String, Object>();
            if (pro.getBelongDeparts() != null && pro.getBelongDeparts().split(",").length>0) {
                for (String belongDepart : pro.getBelongDeparts().split(",")) {
                    for (Integer departmentId : departmentIds) {
                        if (belongDepart.equals(StringUtil.trimToEmpty(departmentId))) {
                            belongFlag = true;
                        }
                    }
                }
                if (!belongFlag) {
                    continue;
                }
            }

            tmpMap.put("id", pro.getProName());
            tmpMap.put("text", pro.getProName());
            tmpList.add(tmpMap);
        }
        return JSON.toJSONString(tmpList);
    }

    @Override
    public List<Map<String, Object>> getProjects(String cid,List<Integer> departmentIds) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        String hql = " from Project where isdel=0 and compId=:cid and isLock = '0' order by id desc ";
        List<Project> proList = projectDao.find(hql, params);
        List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>();
        Map<String, Object> tmpMap = new HashMap<String, Object>();
        boolean belongFlag = false;
        for (Project pro : proList) {
            tmpMap = new HashMap<String, Object>();
            if (pro.getBelongDeparts() != null && pro.getBelongDeparts().split(",").length>0) {
                for (String belongDepart : pro.getBelongDeparts().split(",")) {
                    for (Integer departmentId : departmentIds) {
                        if (belongDepart.equals(StringUtil.trimToEmpty(departmentId))) {
                            belongFlag = true;
                        }
                    }
                }
                if (!belongFlag) {
                    continue;
                }
            }
            tmpMap.put("id", pro.getId());
            tmpMap.put("text", pro.getProName());
            tmpList.add(tmpMap);
        }
        return tmpList;
    }

    /**
     * 锁定
     * @param id
     */
    @Override
    public void lockProject(Integer id) {
        try {
            Project p = this.projectDao.get("from Project p where p.id = " + id);
            p.setIsLock(1);
            this.projectDao.update(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解锁
     * @param id
     */
    @Override
    public void unLockProject(Integer id) {
        try {
            Project p = this.projectDao.get("from Project p where p.id = " + id);
            p.setIsLock(0);
            this.projectDao.update(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Project> getProjectByProjectId(String cid, String projectId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        params.put("projectId", projectId);
        String hql = " from Project where isdel=0 and compId=:cid and projectId = :projectId";
        List<Project> proList = projectDao.find(hql, params);
        return proList;
    }

    @Override
    public List<Project> getProjectByProjectName(String cid, String projectName) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        params.put("proName", projectName);
        String hql = " from Project where isdel=0 and compId=:cid and proName = :proName";
        List<Project> proList = projectDao.find(hql, params);
        return proList;
    }

    // add by heyh
    @Override
    public DataGrid dataGrid(ProjectSearch app, PageHelper ph, String compId, String source) {
        DataGrid dg = new DataGrid();
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = " from Project p  where 1=1 ";

        // 暂时不传入uid
        List<Project> l = projectDao.find(hql + whereHql(app, params, compId, source), params, ph.getPage(), ph.getRows());
        dg.setRows(l);
        dg.setTotal(projectDao.count( "select count(*) " + hql + whereHql(app, params, compId, source), params));
        return dg;
    }

    // 查询时使用的动态添加where条件
    private String whereHql(ProjectSearch app, Map<String, Object> params, String compId, String source) {
        StringBuffer hql = new StringBuffer();
        // 如果app中有值，则代表需要模糊查询
        if (null != app) {
            // 判断项目ID是否为空，否则填充查询参数
            if (null != app.getProjetcId() && !("".equals(app.getProjetcId()))) {
                hql.append(" and p.projectId like '%'||:projectId||'%' ");
                params.put("projectId", app.getProjetcId());
            }
            if (null != app.getGczt() && !("".equals(app.getGczt()))) {
                hql.append(" and p.gczt like '%'||:gczt||'%' ");
                params.put("gczt", app.getGczt());
            }
            if (null != app.getManager() && !("".equals(app.getManager()))) {
                hql.append(" and p.manager like '%'||:manager||'%' ");
                params.put("manager", app.getManager());
            }
            // 判断项目名称是否为空，否则填充查询参数
            if (null != app.getProName() && !("".equals(app.getProName()))) {
                hql.append(" and p.proName like '%'||:proName||'%' ");
                params.put("proName", app.getProName());
            }
            // 判断项目开工日期是否为空，否则填充查询参数
            if (null != app.getKgrq() && !("".equals(app.getKgrq().toString()))) {
                hql.append(" and p.kgrq >= date(:kgrq) ");
                params.put("kgrq", app.getKgrq());
            }
            // 判断项目竣工日期是否为空，否则填充查询参数
            if (null != app.getJgrq() && !("".equals(app.getJgrq().toString()))) {
                hql.append(" and p.kgrq <= date(:jgrq) ");
                params.put("jgrq", app.getJgrq());
            }

            // add by heyh begin keyword
            if (null != app.getKeyword() && !app.getKeyword().equals("")) {
                String keyword = app.getKeyword();
                hql.append(" and ( projectId like :projectId ");
                params.put("projectId", "%%" + keyword + "%%");

                hql.append(" or proName like :proName ");
                params.put("proName", "%%" + keyword + "%%");

                hql.append(" or financeCode like :financeCode ");
                params.put("financeCode", "%%" + keyword + "%%");

                hql.append(" or shortname like :shortname ");
                params.put("shortname", "%%" + keyword + "%%");

                hql.append(" or gchtj like :gchtj ");
                params.put("gchtj", "%%" + keyword + "%%");

                hql.append(" or gchtj like :gchtj ");
                params.put("gchtj", "%%" + keyword + "%%");

                hql.append(" or gczbyd like :gczbyd ");
                params.put("gczbyd", "%%" + keyword + "%%");

                hql.append(" or provice like :provice ");
                params.put("provice", "%%" + keyword + "%%");

                hql.append(" or area like :area ");
                params.put("area", "%%" + keyword + "%%");

                hql.append(" or manager2 like :manager2 ");
                params.put("manager2", "%%" + keyword + "%%");

                hql.append(" or city like :city ");
                params.put("city", "%%" + keyword + "%%");

                hql.append(" or gczt like :gczt ");
                params.put("gczt", "%%" + keyword + "%%");

                hql.append(" or manager like :manager ");
                params.put("manager", "%%" + keyword + "%%");

                hql.append(" or zjy like :zjy ");
                params.put("zjy", "%%" + keyword + "%%");

                hql.append(" or zly like :zly ");
                params.put("zly", "%%" + keyword + "%%");

                hql.append(" or maintenanceManager like :maintenanceManager ");
                params.put("maintenanceManager", "%%" + keyword + "%%");

                hql.append(" or yhcbr like :yhcbr )");
                params.put("yhcbr", "%%" + keyword + "%%");

            }
            // add by heyh end
        }
        if (compId != null && !compId.equals("")) {
            hql.append(" and compId = :compId");
            params.put("compId", compId);
        }
        if (source.equals("field")) {
            hql.append(" and p.isdel = 0 and p.isLock = '0' order by p.id desc"); // 增加field时，需要过滤掉isLock为1的工程
        } else {
            hql.append(" and p.isdel = 0 order by p.id desc");
        }

        return hql.toString();

    }

    @Override
    public List<Project> initDefaultProject(String cid, String uid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        List<Project> projectList = projectDao.find("from Project where compId=:cid", params);
        if (projectList == null || projectList.size() == 0) {
            Project project = new Project();
            project.setCompId(cid);
            project.setProName("样本工程");
            project.setShortname("工程");
            project.setProjectId("001");
            project.setUid(uid);
            project.setProvice("全国");
            project.setIsdel(0);
            project.setIsLock(0);
            projectDao.save(project);
        }

        return projectList;
    }

    @Override
    public int initExperienceProjects(String cid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        String sql = "INSERT INTO tgc_Project \n" +
                "  SELECT null, city, :cid, gclx, isdel, jgrq, jzmjorgm, kgrq, proName,\n" +
                "  projectId, provice, uids, zjlx, gchtj, gczt, money_state, manager,\n" +
                "  manager2, shortname, gcaqwmqk, gcdqyjqk, gcfkyd, gcjgrq, gckgrq,\n" +
                "  gcwhq, gcyjrq, gczbyd, gczlhjqk, htqdrq, htwhjzr, jgbb, jgjss,\n" +
                "  jgysrq, jgzl, whjssj, whkssj, yhcbr, yhjb, zbtzsrq, zjy, zly, isLock,\n" +
                "  cse, djdw, djdwlink, financeCode, htyhq, jldw, jldwlink, jsdw,\n" +
                "  jsdwlink, maintenanceCost, maintenanceManager, managerConfirm,\n" +
                "  remark, sbe, sje, area, belongDeparts \n" +
                "  FROM tgc_Project b \n" +
                "  WHERE compId = '195'";

        return projectDao.executeSql(sql, params);
    }

    @Override
    public List<Project> getAllProjects(String cid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cid", cid);
        String hql = " from Project where isdel=0 and compId=:cid ";

        return projectDao.find(hql, params);
    }

}
