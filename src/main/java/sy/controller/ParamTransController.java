package sy.controller;

import freemarker.ext.beans.HashAdapter;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sy.model.Param;
import sy.model.ParamTrans;
import sy.model.po.Cost;
import sy.model.po.Project;
import sy.model.po.TFieldData;
import sy.pageModel.*;
import sy.service.ParamService;
import sy.service.ParamTransServiceI;
import sy.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by heyh on 2017/1/29.
 */

@Controller
@RequestMapping("/paramTransController")
public class ParamTransController {

    @Autowired
    private ParamTransServiceI paramTransService;

    @Autowired
    private ParamService paramService;

    @RequestMapping("/ParamTransList")
    public String ParamTransList(HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        List<ParamTrans> paramTransList = paramTransService.getParamTransListByCid(cid, "costTrans");
        if (paramTransList.size() <= 0) {
            List<ParamTrans> defaultParamTransList = paramTransService.getParamTransListByCid("-1", "costTrans");
            for (ParamTrans defaultParamTrans : defaultParamTransList) {
                defaultParamTrans.setCid(cid);
                paramTransService.add(defaultParamTrans);
            }
        }
        return "/app/fielddata/ParamTransList";
    }

    @RequestMapping("/securi_dataGrid")
    @ResponseBody
    public DataGrid dataGrid(PageHelper pageHelper, HttpServletRequest request, HttpSession session) {
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();

        DataGrid dataGrid = paramTransService.dataGrid(cid, "costTrans", pageHelper);

        return dataGrid;
    }

    /**
     * 跳转新增页
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/securi_addParamTransPage")
    public String addParamTransPage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());

        request.setAttribute("jsyfee", paramService.getParams("jsyfee", ""));

        List<Map<String, Object>> dataCostInfos = sessionInfo.getCostTypeInfos().get("dataCostInfos");
        List<Map<String, Object>> glxtfee = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> dataCostInfo : dataCostInfos) {
            String itemCode = StringUtil.trimToEmpty(dataCostInfo.get("itemCode"));
            String isSend = StringUtil.trimToEmpty(dataCostInfo.get("isSend"));
            if (StringUtils.startsWith(itemCode, "003") && isSend.equals("1")) {
                glxtfee.add(dataCostInfo);
            }
        }

        // 把名字前缀除去
        for (Map<String, Object> _fee : glxtfee) {
            String[] costTypes = StringUtil.trimToEmpty(_fee.get("costType")).split("-");
            _fee.put("costType", costTypes[costTypes.length-1]);
        }

        request.setAttribute("glxtfee", glxtfee);

        return "/app/fielddata/addParamTrans";
    }

    @RequestMapping("/securi_addParamTrans")
    @ResponseBody
    public Json addParamTrans(ParamTrans paramTrans, HttpSession session) {
        Json j = new Json();
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        paramTrans.setCid(cid);
        paramTrans.setParamType("costTrans");

        try {
            paramTransService.add(paramTrans);
            j.setSuccess(true);
            j.setMsg("添加成功！");
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 跳转修改页
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/securi_updateParamTransPage")
    public String updateParamTransPage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String id = request.getParameter("id");
        ParamTrans paramTrans = paramTransService.detail(id);
        if (paramTrans == null) {
            response.getWriter().write("1");
            return null;
        }

        request.setAttribute("jsyfee", paramService.getParams("jsyfee", ""));
        request.setAttribute("paramTrans", paramTrans);

        List<Map<String, Object>> dataCostInfos = sessionInfo.getCostTypeInfos().get("dataCostInfos");
        List<Map<String, Object>> glxtfee = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> dataCostInfo : dataCostInfos) {
            String itemCode = StringUtil.trimToEmpty(dataCostInfo.get("itemCode"));
            String isSend = StringUtil.trimToEmpty(dataCostInfo.get("isSend"));
            if (StringUtils.startsWith(itemCode, "003") && isSend.equals("1")) {
                glxtfee.add(dataCostInfo);
            }
        }

        // 把名字前缀除去
        for (Map<String, Object> _fee : glxtfee) {
            String[] costTypes = StringUtil.trimToEmpty(_fee.get("costType")).split("-");
            _fee.put("costType", costTypes[costTypes.length-1]);
        }

        request.setAttribute("glxtfee", glxtfee);

        return "/app/fielddata/updateParamTrans";
    }

    /**
     * 修改
     *
     * @param paramTrans
     * @param session
     * @return
     */
    @RequestMapping("/securi_updateParamTrans")
    @ResponseBody
    public Json updateParamTrans(ParamTrans paramTrans, HttpSession session) {
        Json j = new Json();
        try {
            ParamTrans info = paramTransService.detail(StringUtil.trimToEmpty(paramTrans.getId()));
            info.setParamName(paramTrans.getParamName());
            info.setTransParamCode(paramTrans.getTransParamCode());
            info.setTransParamName(paramTrans.getTransParamName());
            paramTransService.update(info);
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

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping("/securi_delParamTrans")
    @ResponseBody
    public Json delParamTrans(String id) {
        Json j = new Json();
        try {
            paramTransService.delete(id);
            j.setSuccess(true);
            j.setMsg("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg(e.getMessage());
        }
        return j;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @RequestMapping("/ExeclParamTransList")
    public String ExeclParamTransList(HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        String uid = sessionInfo.getId();

        String defaultTemplate = PropertyUtil.getFileRealPath() + "/template" + "/projectTemplate.xls";
        String template = PropertyUtil.getFileRealPath() + "/template/" + uid + "/projectTemplate.xls";
        File file = new File(template);
        if (!file.exists()) {
            File dir = new File(file.getParent());
            dir.mkdir();
            FileUtil.copyFile(defaultTemplate, template);
        }

        List<ParamTrans> paramTransList = paramTransService.getParamTransListByCid(cid, "execlCostTrans");
        if (paramTransList.size() <= 0) {
            List<ParamTrans> defaultParamTransList = paramTransService.getParamTransListByCid("-1", "execlCostTrans");
            for (ParamTrans defaultParamTrans : defaultParamTransList) {
                defaultParamTrans.setCid(cid);
                paramTransService.add(defaultParamTrans);
            }
        }

        return "/app/fielddata/ExeclParamTransList";
    }

    @RequestMapping("/securi_execlDataGrid")
    @ResponseBody
    public DataGrid execlDataGrid(PageHelper pageHelper, HttpServletRequest request, HttpSession session) {
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();

        DataGrid dataGrid = paramTransService.dataGrid(cid, "execlCostTrans", pageHelper);

        return dataGrid;
    }

    @RequestMapping("/securi_addExeclParamTransPage")
    public String addExeclParamTransPage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String uid = sessionInfo.getId();

        String templatePate = PropertyUtil.getFileRealPath() + "/template/" + uid;
        List<Map<String, Object>> excelInfos1 = ObjectExcelRead.readExcel(templatePate, "projectTemplate.xls", 3, 0, 0); //执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
        List<Map<String, Object>> execlFees = new ArrayList<Map<String, Object>>();
        Map<String, Object> execlFee = new HashMap<String, Object>();
        for (Map<String, Object> excelInfo : excelInfos1) {
            String feeCode = StringUtil.trimToEmpty(excelInfo.get("var0"));
            String feeName = StringUtil.trimToEmpty(excelInfo.get("var1"));
            if (feeCode.equals("") || feeName.equals("")) {
                continue;
            }
            execlFee = new HashMap<String, Object>();
            execlFee.put("feeCode", feeCode);
            execlFee.put("feeName", feeName);
            execlFees.add(execlFee);

        }
        request.setAttribute("execlFees", execlFees);

        List<Map<String, Object>> dataCostInfos = sessionInfo.getCostTypeInfos().get("dataCostInfos");
        List<Map<String, Object>> glxtfee = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> dataCostInfo : dataCostInfos) {
            String itemCode = StringUtil.trimToEmpty(dataCostInfo.get("itemCode"));
            String isSend = StringUtil.trimToEmpty(dataCostInfo.get("isSend"));
            if (StringUtils.startsWith(itemCode, "003") && isSend.equals("1")) {
                glxtfee.add(dataCostInfo);
            }
        }
        request.setAttribute("glxtfee", glxtfee);

        return "/app/fielddata/addExeclParamTrans";
    }

    @RequestMapping("/securi_addExeclParamTrans")
    @ResponseBody
    public Json securi_addExeclParamTrans(ParamTrans paramTrans, HttpSession session) {
        Json j = new Json();
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        paramTrans.setCid(cid);
        paramTrans.setParamType("execlCostTrans");

        try {
            paramTransService.add(paramTrans);
            j.setSuccess(true);
            j.setMsg("添加成功！");
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @RequestMapping("/securi_updateExeclParamTransPage")
    public String updateExeclParamTransPage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String uid = sessionInfo.getId();

        String id = request.getParameter("id");
        ParamTrans paramTrans = paramTransService.detail(id);
        if (paramTrans == null) {
            response.getWriter().write("1");
            return null;
        }
        request.setAttribute("paramTrans", paramTrans);

        String templatePate = PropertyUtil.getFileRealPath() + "/template/" + uid;
        List<Map<String, Object>> excelInfos1 = ObjectExcelRead.readExcel(templatePate, "projectTemplate.xls", 3, 0, 0); //执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
        List<Map<String, Object>> execlFees = new ArrayList<Map<String, Object>>();
        Map<String, Object> execlFee = new HashMap<String, Object>();
        for (Map<String, Object> excelInfo : excelInfos1) {
            String feeCode = StringUtil.trimToEmpty(excelInfo.get("var0"));
            String feeName = StringUtil.trimToEmpty(excelInfo.get("var1"));
            if (feeCode.equals("") || feeName.equals("")) {
                continue;
            }
            execlFee = new HashMap<String, Object>();
            execlFee.put("feeCode", feeCode);
            execlFee.put("feeName", feeName);
            execlFees.add(execlFee);

        }
        request.setAttribute("execlFees", execlFees);

        List<Map<String, Object>> dataCostInfos = sessionInfo.getCostTypeInfos().get("dataCostInfos");
        List<Map<String, Object>> glxtfee = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> dataCostInfo : dataCostInfos) {
            String itemCode = StringUtil.trimToEmpty(dataCostInfo.get("itemCode"));
            String isSend = StringUtil.trimToEmpty(dataCostInfo.get("isSend"));
            if (StringUtils.startsWith(itemCode, "003") && isSend.equals("1")) {
                glxtfee.add(dataCostInfo);
            }
        }
        request.setAttribute("glxtfee", glxtfee);

        return "/app/fielddata/updateExeclParamTrans";
    }

    @RequestMapping("/securi_updateEeclParamTrans")
    @ResponseBody
    public Json updateEeclParamTrans(ParamTrans paramTrans, HttpSession session) {
        Json j = new Json();
        try {
            ParamTrans info = paramTransService.detail(StringUtil.trimToEmpty(paramTrans.getId()));
            info.setParamCode(paramTrans.getParamCode());
            info.setParamName(paramTrans.getParamName());
            info.setTransParamCode(paramTrans.getTransParamCode());
            info.setTransParamName(paramTrans.getTransParamName());
            paramTransService.update(info);
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

    @RequestMapping("/securi_uploadExeclTemplatePage")
    public String uploadExeclTemplatePage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String uid = sessionInfo.getId();
        return "/app/fielddata/uploadExeclTemplate";
    }

    @RequestMapping("securi_uploadExeclTemplate")
    @ResponseBody
    public Json uploadExeclTemplate(@RequestParam(value = "excel", required = false) MultipartFile file,
                                    HttpServletRequest request) {
        Json j = new Json();
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String uid = sessionInfo.getId();

        if (null == file && file.isEmpty()) {
            return j;
        }

        String filePath = PropertyUtil.getFileRealPath() + "/template/" + uid;    //文件上传路径
        FileUpload.fileUp(file, filePath, "projectTemplate");

        j.setSuccess(true);
        j.setMsg("导入成功！");
        return j;
    }
}

