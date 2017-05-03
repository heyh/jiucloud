package sy.service;

import sy.model.po.JswZjsproject;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;

import java.util.List;

/**
 * Created by heyh on 2016/10/21.
 */
public interface JswZjsprojectServiceI {
    public List<JswZjsproject> getZjsprojects(PageHelper pageHelper, List<Integer> ugroup, String keyword, String startTime, String endTime);
    public List<JswZjsproject> getChildZjsprojects(String id);
    public DataGrid dataGrid(PageHelper pageHelper, List<Integer> ugroup, String keyword, String startTime, String endTime, String pid);
}
