package sy.controller;

import com.alibaba.fastjson.JSONObject;
import com.sun.xml.internal.rngom.parse.host.Base;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sy.model.Param;
import sy.model.S_department;
import sy.model.po.Company;
import sy.model.po.Cost;
import sy.model.po.GCPo;
import sy.model.po.TFieldData;
import sy.pageModel.*;
import sy.service.*;
import sy.service.impl.CompanyServiceImpl;
import sy.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by heyh on 16/8/1.
 */
@Controller
@RequestMapping("/api")
public class Api extends BaseController {

    @Autowired
    private UserServiceI userService;

    @Autowired
    private FieldDataServiceI fieldDataService;

    @Autowired
    private CompanyServiceI companyService;

    @Autowired
    private DepartmentServiceI departmentService;

    @Autowired
    private ProjectServiceI projectService;

    @Autowired
    private ParamService paramService;

    @Autowired
    private CostServiceI costService;

    @Autowired
    private GCPoServiceI gcPoService;

    @RequestMapping("/securi_login")
    @ResponseBody
    public JSONObject login(@RequestParam(value = "name", required = true) String name,
                            @RequestParam(value = "pwd", required = true) String pwd,
                            HttpServletRequest request) {

        User user = userService.login(name, pwd);
        if (user != null) {
            Company company = companyService.findOneView(user.getId(), null);
            return new WebResult().ok().set("user", user).set("company", company);
        } else {
            return new WebResult().fail().setMessage("用户名或密码错误");
        }
    }

    /**
     * 现场数据列表
     */
    @RequestMapping("/securi_fieldDataList")
    @ResponseBody
    public JSONObject getFieldDataList(@RequestParam(value = "uid", required = true) String uid,
                                       @RequestParam(value = "cid", required = true) String cid,
                                       @RequestParam(value = "currentPage", required = true) int currentPage,
                                       @RequestParam(value = "limitSize", required = true) int limitSize,
                                       HttpServletRequest request, HttpServletResponse response) {

        DataGrid dataGrid = null;
        FieldData fieldData = new FieldData();
        fieldData.setUid(uid);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(currentPage);
        pageHelper.setRows(limitSize);
        try {
            List<Integer> ugroup = departmentService.getUsers(cid, Integer.parseInt(uid));
            dataGrid = fieldDataService.dataGrid(fieldData, pageHelper, ugroup, "", "");

            List<FieldData> fieldDatas = dataGrid.getRows();
            if (fieldDatas != null && fieldDatas.size() > 0) {
                for (int i = fieldDatas.size() - 1; i >= 0; i--) {
                    String currentApprovedUser = fieldDatas.get(i).getCurrentApprovedUser() == null ? "" : fieldDatas.get(i).getCurrentApprovedUser();
                    if (!currentApprovedUser.equals("")) {
                        User user = userService.getUser(currentApprovedUser);
                        String realName = user.getRealname();
                        if (realName == null || realName.equals("")) {
                            realName = user.getUsername();
                        }
                        fieldDatas.get(i).setCurrentApprovedUser(realName);
                    }
                }
            }
        } catch (Exception e) {
            return new WebResult().fail().setMessage("网络异常,请稍后再试");
        }
        return new WebResult().ok().set("fieldDataList", dataGrid.getRows());
    }

    @RequestMapping("/securi_getProjects")
    @ResponseBody
    public JSONObject getProjects(@RequestParam(value = "cid", required = true) String cid,
                                  HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> projectInfos = projectService.getProjects(cid);
        return new WebResult().ok().set("projectInfos", projectInfos);

    }

    @RequestMapping("/securi_getParams")
    @ResponseBody
    public JSONObject getParams(@RequestParam(value = "paramType", required = true) String paramType,
                                HttpServletRequest request, HttpServletResponse response) {
        List<Param> params = (List<Param>) paramService.getParams(paramType, "");
        return new WebResult().ok().set("params", params);
    }

    @RequestMapping("/securi_getCostInfos")
    @ResponseBody
    public JSONObject getCostInfos(@RequestParam(value = "uid", required = true) String uid,
                                   @RequestParam(value = "cid", required = true) String cid,
                                   HttpServletRequest request, HttpServletResponse response) {
        List<S_department> s = departmentService.getDepartmentsByUid(uid, cid);
        List<Integer> departmentIds = new ArrayList<Integer>();
        if (s != null && s.size() > 0) {
            for (S_department department : s) {
                departmentIds.add(department.getId());
            }
        }
        Map<String, List<Map<String, Object>>> costInfos = costService.getCostTypeInfos(departmentIds, cid);
        List<Map<String, Object>> dataCostList = costInfos.get("dataCostInfos");
        List<Map<String, Object>> _dataCostList = new ArrayList<Map<String, Object>>();
        Map<String, Object> _dataCost = new HashMap<String, Object>();
        for (Map<String, Object> dataCost : dataCostList) {
            _dataCost = new HashMap<String, Object>();
            _dataCost.put("name", dataCost.get("costType"));
            _dataCost.put("id", dataCost.get("nid"));
            _dataCost.put("pid", dataCost.get("pid"));
            _dataCost.put("itemCode", dataCost.get("itemCode"));
            _dataCostList.add(_dataCost);
        }
        return new WebResult().ok().set("costInfos", Utility.treeList(_dataCostList, "-1"));
    }

    @ResponseBody
    @RequestMapping("/securi_savefieldData")
    public JSONObject saveFieldData(@RequestParam(value = "projectName", required = true) String projectName,
                                    @RequestParam(value = "nid", required = true) String nid,
                                    @RequestParam(value = "dataName", required = true) String dataName,
                                    @RequestParam(value = "uid", required = true) String uid,
                                    @RequestParam(value = "uname", required = true) String uname,
                                    @RequestParam(value = "unit", required = false) String unit,
                                    @RequestParam(value = "price", required = false) String price,
                                    @RequestParam(value = "count", required = false) String count,
                                    @RequestParam(value = "specifications", required = false) String specifications,
                                    @RequestParam(value = "remark", required = false) String remark,
                                    @RequestParam(value = "needApproved", required = false) String needApproved,
                                    @RequestParam(value = "cid", required = false) String cid,
                                    @RequestParam(value = "companyName", required = false) String companyName,
                                    @RequestParam(value = "approvedUser", required = false) String approvedUser,
                                    @RequestParam(value = "currentApprovedUser", required = false) String currentApprovedUser,
                                    HttpServletRequest request) {

        if (companyName == null || companyName.equals("")) {
            Company c = companyService.findOneView(uid, cid);
            cid = String.valueOf(c.getId());
            companyName = c.getName();
        }

        Cost cost = costService.findOneView(nid, cid);
        String costType = String.valueOf(cost.getId());
        String itemCode = cost.getItemCode();

        if (approvedUser == null || approvedUser.equals("")) {
            List<Integer> approvedUserList = new ArrayList<Integer>();
            if (needApproved.equals("1")) {
                approvedUserList = departmentService.getAllParents(cid, Integer.parseInt(uid));
                if (approvedUserList == null || approvedUserList.size()==0) {
                    approvedUserList.add(Integer.parseInt(uid)); // 如果为空说明是超级管理员，自己审批
                }
                approvedUser = StringUtils.join(approvedUserList, ","); // 所有审批人
                currentApprovedUser = String.valueOf(approvedUserList.get(0)); // 当前审批人

            }
        } else {
            List<String> approvedUsers = Arrays.asList(approvedUser.split(","));
            if (approvedUsers != null && approvedUsers.size() > 0) {
                currentApprovedUser = approvedUsers.get(0); // 当前审批人
            }
        }

        TFieldData fieldData = new TFieldData(projectName, uid, new Date(),
                costType, dataName, price, companyName, count, specifications,
                remark, cid, uname, unit, needApproved, approvedUser, currentApprovedUser, itemCode, "");
        try {
            fieldDataService.add(fieldData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new WebResult().ok().set("mid", fieldDataService.getId(fieldData));
    }

    /**
     * 附件上传
     *
     * @param mid
     * @param request
     * @param response
     * @param rt
     * @return
     */
    @RequestMapping("/securi_upload")
    @ResponseBody
    public JSONObject upload(@RequestParam(value = "mid", required = true) String mid,
                             HttpServletRequest request, HttpServletResponse response, MultipartHttpServletRequest rt) {
        if (mid == null || mid.length() == 0 || mid.equals("undefined")) {
            return new WebResult().fail().setMessage("mid is need not null");
        }
        try {
            TFieldData tem = fieldDataService.detail(mid);
            String userPath = tem.getUid() + "/";
            String file_path = PropertyUtil.getFileRealPath() + "/upload/" + Constant.SOURCE + userPath;

            MultipartFile patch = rt.getFile("file");// 获取文件

            String fileName = patch.getOriginalFilename();// 得到文件名
            if (!patch.isEmpty()) {
                File saveDir = new File(file_path);
                if (!saveDir.exists())
                    saveDir.mkdirs();
                String reg = fileName.substring(patch.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
                int status = Constant.fileStatus(reg);
                if (status == -1) {
                    return new WebResult().fail().setMessage("上传的文件格式不支持");
                }
                String finalname = UUID.randomUUID().toString();
                File f = new File(file_path + finalname + "." + reg);
                patch.transferTo(f);
                GCPo gcpo = new GCPo();
                gcpo.setMpid(mid);
                gcpo.setFileName(patch.getOriginalFilename());
                gcpo.setPdfFilePath("");
                gcpo.setSwfFilePath("");
                gcpo.setSourceFilePath(userPath + finalname + "." + reg);
                gcpo.setExt(reg);
                gcpo.setStatus(status);

                // 如果已经是pdf直接设置从pdf > swf状态开始
                if (reg.equals("pdf")) {
                    String pdfFilePath = PropertyUtil.getFileRealPath() + "/upload/" + Constant.PDFSOURCE + userPath;
                    FileUtils.copyFileToDirectory(f, new File(pdfFilePath));
                    gcpo.setStatus(Constant.PDF2SWF_STATUS);
                    gcpo.setPdfFilePath(userPath + finalname + "." + reg);
                }
                gcPoService.add(gcpo);
                return new WebResult().ok().setMessage("附件上传成功");
            } else {
                return new WebResult().fail().setMessage("上传的附件不存在");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new WebResult().fail().setMessage("上传异常:" + ex.getMessage());
        }
    }

    /**
     * 审批数据列表
     */
    @RequestMapping("/securi_approvalFielddataList")
    @ResponseBody
    public JSONObject approvalFielddataList(@RequestParam(value = "uid", required = true) String uid,
                                     @RequestParam(value = "cid", required = true) String cid,
                                     @RequestParam(value = "currentPage", required = true) int currentPage,
                                     @RequestParam(value = "limitSize", required = true) int limitSize,
                                     HttpServletRequest request, HttpServletResponse response) {
        DataGrid dataGrid = null;
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(currentPage);
        pageHelper.setRows(limitSize);
        try {
            List<Integer> ugroup = departmentService.getUsers(cid, Integer.parseInt(uid));
            dataGrid = fieldDataService.approveDataGrid(pageHelper, uid);

            // add by heyh begin
            List<FieldData> fieldDatas = dataGrid.getRows();
            if (fieldDatas != null && fieldDatas.size()>0) {
                for (int i = fieldDatas.size()-1; i >= 0; i--) {
                    String currentApprovedUser = fieldDatas.get(i).getCurrentApprovedUser() == null ? "" : fieldDatas.get(i).getCurrentApprovedUser();
                    if (!currentApprovedUser.equals("")) {
                        User user = userService.getUser(currentApprovedUser);
                        String realName = user.getRealname();
                        if (realName == null || realName.equals("")) {
                            realName = user.getUsername();
                        }
                        fieldDatas.get(i).setCurrentApprovedUser(realName);
                    }
                }
            }
        } catch (Exception e) {
            return new WebResult().fail().setMessage("网络异常,请稍后再试");
        }
        return new WebResult().ok().set("approvalFieldDataList", dataGrid.getRows());
    }
}
