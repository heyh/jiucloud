package sy.controller;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.Clockingin;
import sy.model.Item;
import sy.model.po.TFieldData;
import sy.pageModel.*;
import sy.service.ClockinginServiceI;
import sy.util.ConfigUtil;
import sy.util.ExcelExportUtil;
import sy.util.StringUtil;
import sy.util.UtilDate;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

/**
 * Created by heyh on 2017/9/17.
 */

@Controller
@RequestMapping("/clockinginController")
public class ClockinginController extends BaseController  {

    @Autowired
    private ClockinginServiceI clockinginService;

    @RequestMapping("/ClockinginList")
    public String ClockinginList(HttpServletRequest request) {
        return "/app/clockingin/ClockinginList";
    }

    @RequestMapping("/securi_dataGrid")
    @ResponseBody
    public DataGrid dataGrid(PageHelper pageHelper, HttpServletRequest request, HttpSession session) {
        String keyword = StringUtil.trimToEmpty(request.getParameter("keyword"));
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        List<Integer> ugroup = sessionInfo.getUgroup();
        Clockingin clockingin = new Clockingin();
        clockingin.setCid(cid);

        String startDate = StringUtil.trimToEmpty(request.getParameter("startDate"));
        startDate = startDate.equals("") ? UtilDate.getshortFirst() + " 00:00:00" : startDate + " 00:00:00";
        clockingin.setStartDate(startDate);

        String endDate = StringUtil.trimToEmpty(request.getParameter("endDate"));
        endDate = endDate.equals("") ? UtilDate.getshortLast() + " 23:59:59" : endDate + " 23:59:59";
        clockingin.setEndDate(endDate);

        String searchUid = StringUtil.trimToEmpty(request.getParameter("searchUid"));
        clockingin.setUid(searchUid);

        String clockinginFlag = StringUtil.trimToEmpty(request.getParameter("clockinginFlag"));
        clockingin.setClockinginFlag(clockinginFlag);

        String approveState = StringUtil.trimToEmpty(request.getParameter("approveState"));
        clockingin.setApproveState(approveState);

        DataGrid dataGrid = clockinginService.dataGrid(keyword, cid, ugroup, clockingin, pageHelper);
        return dataGrid;
    }

    @RequestMapping("/securi_updateClockinginPage")
    public String updateClockinginPage(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        Clockingin clockingin = clockinginService.getClockingin(id);
        request.setAttribute("clockingin", clockingin);
        return "/app/clockingin/updateClockingin";
    }

    @RequestMapping("/securi_updateClockingin")
    @ResponseBody
    public Json updateClockingin(Clockingin clockingin, HttpSession session) {
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
        Json j = new Json();
        try {
            Clockingin c = clockinginService.getClockingin(clockingin.getId());
            c.setApproveState(clockingin.getApproveState());
            c.setApproveDesc(clockingin.getApproveDesc());
            c.setApproveTime(new Date());
            c.setApproveUser(sessionInfo.getUsername());
            clockinginService.update(c);

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

    @RequestMapping("/securi_delClockingin")
    @ResponseBody
    public Json delClockingin(int id) {
        Json j = new Json();
        try {
            clockinginService.delete(id);
            j.setSuccess(true);
            j.setMsg("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @RequestMapping("/securi_execl")
    public void OutputToExcel(Clockingin c, PageHelper pageHelper, HttpServletResponse response, HttpServletRequest request) {
        response.setContentType("text/html;charset=utf8");
        HttpSession session = request.getSession();
        String keyword = StringUtil.trimToEmpty(request.getParameter("keyword"));
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        List<Integer> ugroup = sessionInfo.getUgroup();
        Clockingin clockingin = new Clockingin();
        clockingin.setCid(cid);

        String startDate = StringUtil.trimToEmpty(request.getParameter("startDate"));
        startDate = startDate.equals("") ? UtilDate.getshortFirst() + " 00:00:00" : startDate + " 00:00:00";
        clockingin.setStartDate(startDate);

        String endDate = StringUtil.trimToEmpty(request.getParameter("endDate"));
        endDate = endDate.equals("") ? UtilDate.getshortLast() + " 23:59:59" : endDate + " 23:59:59";
        clockingin.setEndDate(endDate);

        String searchUid = StringUtil.trimToEmpty(request.getParameter("searchUid"));
        clockingin.setUid(searchUid);

        String clockinginFlag = StringUtil.trimToEmpty(request.getParameter("clockinginFlag"));
        clockingin.setClockinginFlag(clockinginFlag);

        String approveState = StringUtil.trimToEmpty(request.getParameter("approveState"));
        clockingin.setApproveState(approveState);

        pageHelper.setRows(999999999);
        List<Clockingin> datas = clockinginService.dataGrid(keyword, cid, ugroup, clockingin, pageHelper).getRows();

        List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();

        String[] columnNames = {};
        String[] keys = {};
        map = createExcelRecord(datas);
        columnNames = new String[]{"日期", "人员", "上/下班", "时间", "上班地点", "状态", "事由", "审核意见", "审核人", "审核时间"};  // 列名
        keys = new String[]{"clockinginDate", "uname", "clockinginFlag", "clockinginTime", "address", "approveState", "reasonDesc", "approveDesc",
                "approveUser", "approveTime"};// map中的key


        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            ExcelExportUtil.createWorkBook(map, keys, columnNames).write(os);
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=data.xls");
            ServletOutputStream out;
            out = response.getOutputStream();
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    // 格式化execl表格数据
    private List<Map<String, Object>> createExcelRecord(List<Clockingin> list) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "data.xls");
        listmap.add(map);
        double count = 0;

        for (int i = 0; i < list.size(); i++) {
            Clockingin tem = list.get(i);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("clockinginDate", StringUtil.trimToEmpty(tem.getClockinginDate()).substring(0, 10));
            mapValue.put("uname", tem.getUname());
            mapValue.put("clockinginFlag", tem.getClockinginFlag().equals('0') ? "上班" : "下班");
            mapValue.put("clockinginTime", StringUtil.trimToEmpty(tem.getClockinginTime()).substring(0, 19));
            mapValue.put("address", tem.getAddress());

            mapValue.put("approveState", tem.getApproveState());
            mapValue.put("reasonDesc", tem.getReasonDesc());
            mapValue.put("approveUser", tem.getApproveUser());
            mapValue.put("approveTime", tem.getApproveTime());
            listmap.add(mapValue);
        }
        return listmap;
    }
}
