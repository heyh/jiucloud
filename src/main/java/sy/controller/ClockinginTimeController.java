package sy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.Clockingin;
import sy.model.po.ClockinginTime;
import sy.pageModel.DataGrid;
import sy.pageModel.Json;
import sy.pageModel.PageHelper;
import sy.pageModel.SessionInfo;
import sy.service.ClockinginTimeServiceI;
import sy.util.ConfigUtil;
import sy.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Time;
import java.util.Date;

/**
 * Created by heyh on 2017/10/9.
 */
@Controller
@RequestMapping("/clockinginTimeController")
public class ClockinginTimeController extends BaseController {

    @Autowired
    private ClockinginTimeServiceI clockinginTimeService;

    @RequestMapping("/ClockinginTimeSet")
    public String ClockinginTimeSet(HttpServletRequest request) {
        return "/app/clockingin/ClockinginTimeSet";
    }

    @RequestMapping("/securi_dataGrid")
    @ResponseBody
    public DataGrid dataGrid(PageHelper pageHelper, HttpServletRequest request, HttpSession session) {
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();

        DataGrid dataGrid = clockinginTimeService.dataGrid(cid, pageHelper);
        return dataGrid;
    }

    @RequestMapping("/securi_updateClockinginTimePage")
    public String updateClockinginTimePage(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        ClockinginTime clockinginTime = clockinginTimeService.getClockinginTime(id);
        request.setAttribute("clockinginTime", clockinginTime);
        return "/app/clockingin/updateClockinginTime";
    }

    @RequestMapping("/securi_updateClockinginTime")
    @ResponseBody
    public Json updateClockinginTime(HttpServletRequest request, HttpSession session) {
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
        int id = Integer.parseInt(StringUtil.trimToEmpty(request.getParameter("id")));
        String clockinginStartTime = request.getParameter("clockinginStartTime");
        String clockinginEndTime = request.getParameter("clockinginEndTime");

        Json j = new Json();
        try {
            ClockinginTime c = clockinginTimeService.getClockinginTime(id);
            c.setClockinginStartTime(Time.valueOf(clockinginStartTime));
            c.setClockinginEndTime(Time.valueOf(clockinginEndTime));
            clockinginTimeService.update(c);

            j.setSuccess(true);
            j.setMsg("操作成功！");
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg(e.getMessage());
        }
        return j;
    }
}
