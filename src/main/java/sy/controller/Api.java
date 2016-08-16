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
                                       @RequestParam(value = "type", required = false) String type,
                                       @RequestParam(value = "keyword", required=false) String keyword,
                                       @RequestParam(value = "currentPage", required = true) int currentPage,
                                       @RequestParam(value = "limitSize", required = true) int limitSize,
                                       HttpServletRequest request, HttpServletResponse response) {

        DataGrid dataGrid = null;
        FieldData fieldData = new FieldData();
        fieldData.setUid(uid);
        keyword = keyword == null ? "" : keyword;
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(currentPage);
        pageHelper.setRows(limitSize);
        try {
            List<Integer> ugroup = departmentService.getUsers(cid, Integer.parseInt(uid));
            dataGrid = fieldDataService.dataGrid(fieldData, pageHelper, ugroup, type, keyword);

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

    /**
     * 获取附件
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/securi_fileList")
    @ResponseBody
    public JSONObject fileList(@RequestParam(value = "mid", required = true) String mid,
                         HttpServletRequest request, HttpServletResponse response) {
        List<GCPo> fileList = null;
        try {
            String ip = IPUtility.getLocalIP();
            String servAddr = "http://" + ip + ":8080/fileserver";
            fileList = gcPoService.dataGrid(mid, null, null).getRows();
            if (fileList != null && fileList.size()>0) {
                for (GCPo _file : fileList) {
                    _file.setSourceFilePath(servAddr + "/upload/source/" + _file.getSourceFilePath());
                }
            }
        } catch (Exception e) {
            return new WebResult().fail().setMessage("网络异常,请稍后再试");
        }
        return new WebResult().ok().set("fileList", fileList);
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
                                   @RequestParam(value = "type", required = true) String type,
                                   HttpServletRequest request, HttpServletResponse response) {
        List<S_department> s = departmentService.getDepartmentsByUid(uid, cid);
        List<Integer> departmentIds = new ArrayList<Integer>();
        if (s != null && s.size() > 0) {
            for (S_department department : s) {
                departmentIds.add(department.getId());
            }
        }
        Map<String, List<Map<String, Object>>> costInfos = costService.getCostTypeInfos(departmentIds, cid);
        List<Map<String, Object>> costList = new ArrayList<Map<String, Object>>();
        if (type.equals("data")) {
            costList = costInfos.get("dataCostInfos");
        } else if (type.equals("doc")) {
            costList = costInfos.get("docCostInfos");
        }
        List<Map<String, Object>> _costList = new ArrayList<Map<String, Object>>();
        Map<String, Object> _cost = new HashMap<String, Object>();
        for (Map<String, Object> cost : costList) {
            _cost = new HashMap<String, Object>();
            _cost.put("name", cost.get("costType"));
            _cost.put("id", cost.get("nid"));
            _cost.put("pid", cost.get("pid"));
            _cost.put("itemCode", cost.get("itemCode"));
            _costList.add(_cost);
        }
        return new WebResult().ok().set("costInfos", Utility.treeList(_costList, "-1"));
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

    @RequestMapping("/securi_editFieldData")
    @ResponseBody
    public JSONObject editFieldData(@RequestParam(value = "id", required = true) String id,
                                      @RequestParam(value = "projectName", required = false) String projectName,
                                      @RequestParam(value = "nid", required = false) String nid,
                                      @RequestParam(value = "dataName", required = false) String dataName,
                                      @RequestParam(value = "unit", required = false) String unit,
                                      @RequestParam(value = "price", required = false) String price,
                                      @RequestParam(value = "count", required = false) String count,
                                      @RequestParam(value = "specifications", required = false) String specifications,
                                      @RequestParam(value = "remark", required = false) String remark,
                                      @RequestParam(value = "needApproved", required = false) String needApproved,
                                      HttpServletRequest request, HttpServletResponse response) {

        TFieldData fieldData = fieldDataService.detail(id);
        String uid = fieldData.getUid();
        String cid = fieldData.getCid();

        if (projectName != null && !projectName.equals("") && !projectName.equals(fieldData.getProjectName())) {
            fieldData.setProjectName(projectName);
        }
        if (nid != null && !nid.equals("")) {
            Cost cost = costService.findOneView(nid, cid);
            if (!fieldData.getItemCode().equals(cost.getItemCode())) {
                fieldData.setCostType(String.valueOf(cost.getId()));
                fieldData.setItemCode(cost.getItemCode());
            }
        }
        if (dataName != null && !dataName.equals("") && !dataName.equals(fieldData.getDataName())) {
            fieldData.setDataName(dataName);
        }
        if (unit != null && !unit.equals("") && !unit.equals(fieldData.getUnit())) {
            fieldData.setUnit(unit);
        }
        if (price != null && !price.equals("") && !price.equals(fieldData.getPrice())) {
            fieldData.setPrice(price);
        }
        if (count != null && !count.equals("") && !count.equals(fieldData.getCount())) {
            fieldData.setCount(count);
        }
        if (specifications != null && !specifications.equals("")  && !specifications.equals(fieldData.getSpecifications())) {
            fieldData.setSpecifications(specifications);
        }
        if (remark != null && !remark.equals("") && !remark.equals(fieldData.getRemark())) {
            fieldData.setRemark(remark);
        }
        if (needApproved != null && !needApproved.equals("") && !needApproved.equals(fieldData.getNeedApproved())) {
            fieldData.setNeedApproved(needApproved);
        }

        String approvedUser = "";
        String currentApprovedUser = "";
        List<Integer> approvedUserList = new ArrayList<Integer>();
        if (needApproved != null && needApproved.equals("1")) {
            approvedUserList = departmentService.getAllParents(cid, Integer.parseInt(uid));
            if (approvedUserList == null || approvedUserList.size() == 0) {
                approvedUserList.add(Integer.parseInt(uid)); // 如果为空说明是超级管理员，自己审批
            }
            approvedUser = StringUtils.join(approvedUserList, ","); // 所有审批人
            currentApprovedUser = String.valueOf(approvedUserList.get(0)); // 当前审批人

            fieldData.setNeedApproved("1");
            fieldData.setApprovedUser(approvedUser);
            fieldData.setCurrentApprovedUser(currentApprovedUser);
        } else {
            fieldData.setNeedApproved("0");
            fieldData.setApprovedUser("");
            fieldData.setCurrentApprovedUser("");
        }

        try {
            fieldDataService.update(fieldData);
            return new WebResult().ok().set("mid", fieldDataService.getId(fieldData));
        } catch (Exception e) {
            e.printStackTrace();
            return new WebResult().fail().setMessage("网络异常,修改失败");
        }
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
            return new WebResult().fail().setMessage("网络异常,上传失败");
        }
    }

    @RequestMapping("/securi_delFieldData")
    @ResponseBody
    public JSONObject delFieldData(@RequestParam(value = "id", required = true) String id,
                                   HttpServletResponse response) {
        try {
            fieldDataService.delete(id);
            return new WebResult().ok().setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new WebResult().fail().setMessage("网络异常,删除失败");
        }
    }

    /**
     * 审批数据列表
     * @param uid
     * @param cid
     * @param currentPage
     * @param limitSize
     * @param request
     * @param response
     * @return
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

    /**
     * 我的填报
     * @param uid
     * @param cid
     * @param currentPage
     * @param limitSize
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/securi_myApproveFielddataList")
    @ResponseBody
    public JSONObject myApproveFielddataList(@RequestParam(value = "uid", required = true) String uid,
                                             @RequestParam(value = "cid", required = true) String cid,
                                             @RequestParam(value = "currentPage", required = true) int currentPage,
                                             @RequestParam(value = "limitSize", required = true) int limitSize,
                                             HttpServletRequest request, HttpServletResponse response) {
        DataGrid dataGrid = null;
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(currentPage);
        pageHelper.setRows(limitSize);
        try {
            List<Integer> ugroup = departmentService.getUsers(String.valueOf(cid), Integer.parseInt(uid));
            dataGrid = fieldDataService.myApproveDataGrid(pageHelper, uid);
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
        return new WebResult().ok().set("myApprovalDataList", dataGrid.getRows());
    }

    /**
     * 需要审批的列表
     * @param currentApprovedUser
     * @param response
     * @param request
     * @return
     */
    @RequestMapping("securi_getNeedApproveList")
    @ResponseBody
    public JSONObject getNeedApproveList(@RequestParam(value = "uid", required = true) String currentApprovedUser,
                                         HttpServletResponse response, HttpServletRequest request) {
        List<TFieldData> needApproveList = fieldDataService.getNeedApproveList(currentApprovedUser);
        for (TFieldData needApprove : needApproveList) {
            String uid = needApprove.getUid();
            User user = userService.getUser(uid);
            String realName = user.getRealname();
            if (realName == null || realName.equals("")) {
                realName = user.getUsername();
            }
            needApprove.setUname(realName);

            String itemcode = needApprove.getItemCode();
            boolean isData = itemcode != null && !itemcode.equals("") && !itemcode.substring(0, 3).equals("000") && Integer.parseInt(itemcode.substring(0, 3)) <= 900;
            if (isData) {
                needApprove.setItemCode("0");
            } else {
                needApprove.setItemCode("1");
            }
        }

        return new WebResult().ok().set("needApproveList", needApproveList);
    }

    @RequestMapping("/securi_approvedField")
    @ResponseBody
    public JSONObject approvedField(@RequestParam(value = "id", required = true) int id,
                              @RequestParam(value = "approvedState", required = true) String approvedState,
                              @RequestParam(value = "approvedOption", required = true) String approvedOption,
                              HttpServletResponse response, HttpServletRequest request) {

        fieldDataService.approvedField(id, approvedState, approvedOption);
        return new WebResult().ok().setMessage("审批成功");
    }
}
