package sy.service.impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.h2.mvstore.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.JswZjsprojectDaoI;
import sy.model.ZjsProject;
import sy.model.po.JswZjsproject;
import sy.model.po.TFieldData;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.pageModel.User;
import sy.service.JswZjsprojectServiceI;
import sy.service.UserServiceI;
import sy.util.DateKit;
import sy.util.StringEscapeEditor;

import java.util.*;

/**
 * Created by heyh on 2016/10/21.
 */

@Service("jswZjsprojectService")
public class JswZjsprojectServiceImpl implements JswZjsprojectServiceI {

    @Autowired
    private JswZjsprojectDaoI jswZjsprojectDao;

    @Autowired
    private UserServiceI userService;

    @Override
    public List<JswZjsproject> getZjsprojects(PageHelper pageHelper, List<Integer> ugroup, String keyword, String startTime, String endTime) {
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = "from JswZjsproject where isOnLine = '0' and is_show = '1' and level='1'";
        hql += " and user_id in (" + StringUtils.join(ugroup, ",") + ")";
        if (!keyword.equals("")) {
            hql += " and name like :name ";
            params.put("name", "%%" + keyword + "%%");
        }
        if (startTime != null && startTime.length() > 0) {
            hql += " and update_time >= :startTime";
            params.put("startTime", DateKit.strDateToTimeStemp(startTime));
        }
        if (endTime != null && endTime.length() > 0) {
            hql += " and update_time <= :endTime";
            params.put("endTime", DateKit.strDateToTimeStemp(endTime));
        }
        hql += " order by update_time desc";
        List<JswZjsproject> zjsprojectList = jswZjsprojectDao.find(hql, params, pageHelper.getPage(), pageHelper.getRows());
        return zjsprojectList;
    }

    @Override
    public List<JswZjsproject> getChildZjsprojects(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        String hql = "from JswZjsproject where isOnLine = '0' and is_show = '1' and parent_id = :id order by update_time desc ";
        List<JswZjsproject> childZjsprojects = jswZjsprojectDao.find(hql, params);
        return childZjsprojects;
    }

    @Override
    public DataGrid dataGrid(PageHelper pageHelper, List<Integer> ugroup, String keyword, String startTime, String endTime, String pid) {
        DataGrid dg = new DataGrid();
        List<ZjsProject> zjsProjectList = new ArrayList<ZjsProject>();
        ZjsProject zjsProject = new ZjsProject();

        List<JswZjsproject> jswZjsprojectList = new ArrayList<JswZjsproject>();
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = "";
        if (pid.equals("0")) {
            params = new HashMap<String, Object>();
            hql = "from JswZjsproject where isOnLine = '0' and is_show = '1' and level='1'";
            hql += " and user_id in (" + StringUtils.join(ugroup, ",") + ")";
            if (!keyword.equals("")) {
                hql += " and name like :name ";
                params.put("name", "%%" + keyword + "%%");
            }
            if (startTime != null && startTime.length() > 0) {
                hql += " and add_time >= :startTime";
                params.put("startTime", DateKit.strDateToTimeStemp(startTime));
            }
            if (endTime != null && endTime.length() > 0) {
                hql += " and add_time <= :endTime";
                params.put("endTime", DateKit.strDateToTimeStemp(endTime));
            }
            hql += " order by update_time desc";
            jswZjsprojectList = new ArrayList<JswZjsproject>();
            jswZjsprojectList = jswZjsprojectDao.find(hql, params, pageHelper.getPage(), pageHelper.getRows());
            zjsProjectList = new ArrayList<ZjsProject>();
            for (JswZjsproject jswZjsproject : jswZjsprojectList) {
                zjsProject = new ZjsProject();

                zjsProject.setId(jswZjsproject.getId());
                zjsProject.set_parentId(jswZjsproject.getParentId());
                zjsProject.setFilePath(jswZjsproject.getFilePath());
                zjsProject.setLevel(jswZjsproject.getLevel());
                zjsProject.setName(jswZjsproject.getName());
                zjsProject.setUserId(jswZjsproject.getUserId());

                User user = userService.getUser(jswZjsproject.getUserId());
                String realName = user.getRealname();
                if (realName == null || realName.equals("")) {
                    realName = user.getUsername();
                }
                zjsProject.setUserName(realName);

                zjsProject.setZjsdwgcBh(jswZjsproject.getZjsdwgcBh());
//                zjsProject.setState("closed");
                zjsProject.setState(hasChild(jswZjsproject.getId()) ? "closed" : "open");

                zjsProject.setIconCls("iconCls");

                zjsProjectList.add(zjsProject);
            }
        } else {
            params = new HashMap<String, Object>();
            params.put("parent_id", pid);
            hql = "from JswZjsproject where isOnLine = '0' and is_show = '1' and parent_id = :parent_id order by update_time desc ";
            jswZjsprojectList = new ArrayList<JswZjsproject>();
            jswZjsprojectList = jswZjsprojectDao.find(hql, params);

            zjsProjectList = new ArrayList<ZjsProject>();
            for (JswZjsproject jswZjsproject : jswZjsprojectList) {
                zjsProject = new ZjsProject();

                zjsProject.setId(jswZjsproject.getId());
                zjsProject.set_parentId(jswZjsproject.getParentId());
                zjsProject.setFilePath(jswZjsproject.getFilePath());
                zjsProject.setLevel(jswZjsproject.getLevel());
                zjsProject.setName(jswZjsproject.getName());
                zjsProject.setUserId(jswZjsproject.getUserId());
                zjsProject.setZjsdwgcBh(jswZjsproject.getZjsdwgcBh());
//                zjsProject.setState("");
                zjsProject.setState(hasChild(jswZjsproject.getId()) ? "closed" : "open");
                zjsProject.setIconCls("iconCls");

                zjsProjectList.add(zjsProject);
            }
        }

        dg.setRows(zjsProjectList);
        dg.setTotal(jswZjsprojectDao.count("select count(*) " + hql, params));
        return dg;
    }

    private boolean hasChild(int id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", String.valueOf(id));
        String hql = "from JswZjsproject where isOnLine = '0' and is_show = '1' and parent_id = :id ";
        List<JswZjsproject> jswZjsprojectList = jswZjsprojectDao.find(hql, params);
        return ( jswZjsprojectList == null || jswZjsprojectList.size() == 0 ) ? false : true;
    }

}
