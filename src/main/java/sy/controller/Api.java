package sy.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sy.model.Item;
import sy.model.Param;
import sy.model.S_department;
import sy.model.po.*;
import sy.pageModel.*;
import sy.service.*;
import sy.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

import static java.lang.Thread.sleep;

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

    @Autowired
    private JswZjsprojectServiceI jswZjsprojectService;

    @Autowired
    private ItemServiceI itemService;

    @Autowired
    private DiscussServiceI discussService;

    @RequestMapping("/securi_login")
    @ResponseBody
    public JSONObject login(@RequestParam(value = "name", required = true) String name,
                            @RequestParam(value = "pwd", required = true) String pwd,
                            @RequestParam(value = "cid", required = false) String cid,
                            HttpServletRequest request) {

        User user = userService.login(name, pwd);
        if (user != null) {
            List<Company> companyList = companyService.getCompanyInfos(user.getId(), null);
            List<String> rightList = new ArrayList<String>();
            int parentId = -1;

            if (companyList != null && companyList.size()>0) {
                rightList = departmentService.getAllRight(cid, Integer.parseInt(user.getId()));
                parentId = departmentService.getParentId(cid, Integer.parseInt(user.getId()));
            }
            return new WebResult().ok().set("user", user).set("companyList", companyList).set("rightList", rightList).set("parentId", parentId);
        } else {
            return new WebResult().fail().setMessage("用户名或密码错误");
        }
    }

    /**
     * 现场数据列表
     */
    @RequestMapping("/securi_fieldDataList")
    @ResponseBody
    public JSONObject getFieldDataList(@RequestParam(value = "rightList", required = false) String rightList,
                                       @RequestParam(value = "uid", required = true) String uid,
                                       @RequestParam(value = "cid", required = true) String cid,
                                       @RequestParam(value = "type", required = false) String type,
                                       @RequestParam(value = "keyword", required = false) String keyword,
                                       @RequestParam(value = "currentPage", required = true) int currentPage,
                                       @RequestParam(value = "limitSize", required = true) int limitSize,
                                       @RequestParam(value = "advancedSearchProjectName", required = false) String advancedSearchProjectName,
                                       @RequestParam(value = "advancedSearchCostType", required = false) String advancedSearchCostType,
                                       @RequestParam(value = "advancedSearchStartTime", required = false) String advancedSearchStartTime,
                                       @RequestParam(value = "advancedSearchEndTime", required = false) String advancedSearchEndTime,
                                       HttpServletRequest request, HttpServletResponse response) {

        DataGrid dataGrid = null;
        FieldData fieldData = new FieldData();
        fieldData.setUid(uid);
//        if (rightList != null && !rightList.equals("") && (rightList.contains("15") || rightList.contains("16") || rightList.contains("17")) ) {
            fieldData.setCid(cid);
//        }

        if (advancedSearchProjectName != null && !advancedSearchProjectName.equals("")) {
            fieldData.setProjectName(advancedSearchProjectName);
        }
        if (advancedSearchCostType != null && !advancedSearchCostType.equals("")) {
            fieldData.setCostType(advancedSearchCostType);
        }

        if (advancedSearchStartTime != null && !advancedSearchStartTime.equals("")) {
            fieldData.setStartTime(advancedSearchStartTime + " 00:00:00");
        }
        if (advancedSearchEndTime != null && !advancedSearchEndTime.equals("")) {
            fieldData.setEndTime(advancedSearchEndTime + " 23:59:59");
        }

//        if (null == fieldData.getStartTime() && null == fieldData.getEndTime()) {
//            fieldData.setStartTime(UtilDate.getshortFirst() + " 00:00:00");
//            fieldData.setEndTime(UtilDate.getshortLast() + " 23:59:59");
//        }

        keyword = keyword == null ? "" : keyword;
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(currentPage);
        pageHelper.setRows(limitSize);
        try {
            List<Integer> ugroup = departmentService.getUsers(cid, Integer.parseInt(uid));
            dataGrid = fieldDataService.dataGridForMobile(fieldData, pageHelper, ugroup, type, keyword);

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
     *
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
            if (fileList != null && fileList.size() > 0) {
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
                                  @RequestParam(value = "uid", required = true) String uid,
                                  HttpServletRequest request, HttpServletResponse response) {
        List<S_department> s = departmentService.getDepartmentsByUid(uid, cid);
        List<Integer> departmentIdList = new ArrayList<Integer>();
        if (s != null && s.size() > 0) {
            for (S_department department : s) {
                departmentIdList.add(department.getId());
            }
        }
        List<Map<String, Object>> projectInfos = projectService.getProjects(cid, departmentIdList);
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
        Map<String, List<Map<String, Object>>> costInfos = costService.getCostTypeInfosForMobile(departmentIds, cid);
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

    @RequestMapping("/securi_getSelectItems")
    @ResponseBody
    public JSONObject getSelectItems(@RequestParam(value = "cid", required = true) String cid,
                                     @RequestParam(value = "uid", required = true) String uid,
                                     @RequestParam(value = "projectId", required = true) String projectId,
                                  HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> itemList = itemService.getSelectItems(cid, projectId);
        if (itemList.size()<=0) {
            List<Item> defaultItemList = itemService.getDefaultItems();
            for (Item defaultItem : defaultItemList) {
                defaultItem.setCid(cid);
                defaultItem.setProjectId(projectId);
                defaultItem.setOperator(uid);
                itemService.add(defaultItem);
            }
            itemList = itemService.getSelectItems(cid, projectId);
        }

        String section = null;
        TFieldData tFieldData = fieldDataService.getFieldByMaxId(cid,uid, projectId);
        if (tFieldData != null) {
            section = tFieldData.getSection();
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("itemList", itemList);
        data.put("section", section);
        return new WebResult().ok().set("itemList", itemList).set("section", section);
    }

    @RequestMapping("/securi_getMaxFieldData")
    @ResponseBody
    public JSONObject getMaxFieldData(@RequestParam(value = "cid", required = true) String cid,
                                     @RequestParam(value = "uid", required = true) String uid,
                                     HttpServletRequest request, HttpServletResponse response) {
        TFieldData maxFieldData = fieldDataService.getMaxFieldByCidUid(cid, uid);
        Project project = projectService.findOneView(Integer.parseInt(maxFieldData.getProjectName()));
        String proName = "";
        String sectionName = "";
        if (project != null) {
            proName = project.getProName();
        }
        Item sectionItem = itemService.getSingleItem(cid, maxFieldData.getProjectName(), maxFieldData.getSection());
        if (sectionItem == null) {
            sectionName = "标段1";
        } else {
            sectionName = sectionItem.getName();
        }
        return new WebResult().ok().set("maxFieldData", maxFieldData).set("proName", proName).set("sectionName", sectionName);
    }

    @RequestMapping("/securi_getSupInfo")
    @ResponseBody
    public JSONObject getSupInfo(@RequestParam(value = "cid", required = true) String cid,
                                 @RequestParam(value = "uid", required = true) String uid,
                                 @RequestParam(value = "projectId", required = true) String projectId,
                                 @RequestParam(value = "section", required = true) String section,
                                 HttpServletRequest request, HttpServletResponse response) {
        List<String> supInfos = itemService.getSupInfos(cid, projectId, section);
        return new WebResult().ok().set("supInfos", supInfos);
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
                                    @RequestParam(value = "section", required = false) String section,
                                    @RequestParam(value = "supplier", required = false) String supplier,
                                    @RequestParam(value = "relId", required = false) String relId,
                                    HttpServletRequest request) {

        if (companyName == null || companyName.equals("")) {
            Company c = companyService.findOneView(uid, cid);
            cid = String.valueOf(c.getId());
            companyName = c.getName();
        }

        Cost cost = costService.findOneView(nid, cid);
        String costType = String.valueOf(cost.getId());
        String itemCode = cost.getItemCode();

//        if (approvedUser == null || approvedUser.equals("")) {
//            List<Integer> approvedUserList = new ArrayList<Integer>();
//            if (needApproved.equals("1")) {
//                approvedUserList = departmentService.getAllParents(cid, Integer.parseInt(uid));
//                if (approvedUserList == null || approvedUserList.size() == 0) {
//                    approvedUserList.add(Integer.parseInt(uid)); // 如果为空说明是超级管理员，自己审批
//                }
//                approvedUser = StringUtils.join(approvedUserList, ","); // 所有审批人
//                currentApprovedUser = String.valueOf(approvedUserList.get(0)); // 当前审批人
//
//            }
//        } else {
//            List<String> approvedUsers = Arrays.asList(approvedUser.split(","));
//            if (approvedUsers != null && approvedUsers.size() > 0) {
//                currentApprovedUser = approvedUsers.get(0); // 当前审批人
//            }
//        }
        if (approvedUser == null || approvedUser.equals("")) {
            approvedUser = currentApprovedUser;
        }

        TFieldData fieldData = new TFieldData(projectName, uid, new Date(),
                costType, dataName, price, companyName, count, specifications,
                remark, cid, uname, unit, needApproved, approvedUser, currentApprovedUser, itemCode, "", section, supplier, relId, "", "");
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
                                    @RequestParam(value = "section", required = false) String section,
                                    @RequestParam(value = "supplier", required = false) String supplier,
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
        if (specifications != null && !specifications.equals("") && !specifications.equals(fieldData.getSpecifications())) {
            fieldData.setSpecifications(specifications);
        }
        if (remark != null && !remark.equals("") && !remark.equals(fieldData.getRemark())) {
            fieldData.setRemark(remark);
        }
        if (needApproved != null && !needApproved.equals("") && !needApproved.equals(fieldData.getNeedApproved())) {
            fieldData.setNeedApproved(needApproved);
        }
        if (section != null && !section.equals("") && !section.equals(fieldData.getSection())) {
            fieldData.setSection(section);
        }
        if (supplier != null && !supplier.equals("") && !supplier.equals(fieldData.getSupplier())) {
            fieldData.setSupplier(supplier);
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
            return new WebResult().fail().setMessage("网络异常,删除失败");
        }
    }

    @RequestMapping("/securi_delFile")
    @ResponseBody
    public JSONObject delFile(String id, HttpServletResponse response) {
        try {
            gcPoService.deleteOne(Integer.parseInt(id));
            return new WebResult().ok().setMessage("删除成功");
        } catch (Exception e) {
            return new WebResult().fail().setMessage("网络异常,删除失败");
        }
    }

    /**
     * 审批数据列表
     *
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
        return new WebResult().ok().set("approvalFieldDataList", dataGrid.getRows());
    }

    /**
     * 我的填报
     *
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
        return new WebResult().ok().set("myApprovalDataList", dataGrid.getRows());
    }

    /**
     * 需要审批的列表
     *
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
                                    @RequestParam(value = "currentApprovedUser", required = false) String currentApprovedUser,
                                    HttpServletResponse response, HttpServletRequest request) {

        fieldDataService.approvedField(id, approvedState, approvedOption, currentApprovedUser);
        return new WebResult().ok().setMessage("审批成功");
    }

    @RequestMapping("/securi_chooseApprove")
    @ResponseBody
    public JSONObject chooseApprove(@RequestParam(value = "cid", required = true) String cid,
                                    @RequestParam(value = "uid", required = true) String uid,
                                    HttpServletResponse response, HttpServletRequest request) {

        List<User> users = departmentService.getFirstLevelParentDepByUid(cid, uid);
        List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
        Map<String, Object> userMap = new HashMap<String, Object>();
        if (users != null && users.size()>0) {
            for (User user : users) {
                userMap = new HashMap<String, Object>();
                userMap.put("id", user.getId());
                userMap.put("username", !user.getRealname().equals("") ? user.getRealname() : user.getUsername());
                userList.add(userMap);
            }
        }
        return new WebResult().ok().set("approveUserList", userList);
    }

    @RequestMapping("/securi_getAd")
    @ResponseBody
    public JSONObject getAd(HttpServletResponse response, HttpServletRequest request) {

        List<Map<String, Object>> adList = new ArrayList<Map<String, Object>>();
//        String ip = IPUtility.getLocalIP();
//        String servAddr = "http://" + ip + ":8080/fileserver";
        String servAddr = "http://gcgl.9393915.com:8080/fileserver";

        Map<String, Object> ad = new HashMap<String, Object>();
        for (int i = 1; i <= 3; i++) {
            ad = new HashMap<String, Object>();
            ad.put("_orderNo", i);
            ad.put("_fileUrl", servAddr + "/upload/ad/banner" + i + ".png");
            adList.add(ad);
        }
        return new WebResult().ok().set("adList", adList);
    }

    /**
     * 获取工程项目
     *
     * @param uid
     * @param cid
     * @param keyword
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/securi_getZjsprojects")
    @ResponseBody
    public JSONObject getZjsprojects(@RequestParam(value = "uid", required = true) String uid,
                                     @RequestParam(value = "cid", required = true) String cid,
                                     @RequestParam(value = "keyword", required = false) String keyword,
                                     @RequestParam(value = "currentPage", required = true) int currentPage,
                                     @RequestParam(value = "limitSize", required = true) int limitSize,
                                     @RequestParam(value = "startTime", required = false) String startTime,
                                     @RequestParam(value = "endTime", required = false) String endTime,
                                     HttpServletRequest request, HttpServletResponse response) {
        List<JswZjsproject> zjsprojectList = new ArrayList<JswZjsproject>();
        keyword = keyword == null ? "" : keyword;
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(currentPage);
        pageHelper.setRows(limitSize);
        List<Integer> ugroup = new ArrayList<Integer>();
        if (cid == null || cid.equals("")) {
            ugroup.add(Integer.parseInt(uid));
        } else {
            ugroup = departmentService.getUsers(cid, Integer.parseInt(uid));
        }

        zjsprojectList = jswZjsprojectService.getZjsprojects(pageHelper, ugroup, keyword, startTime, endTime);
        List<Map<String, Object>> _zjsprojectList = new ArrayList<Map<String, Object>>();
        Map<String, Object> _zjsproject = new HashMap<String, Object>();
        for (JswZjsproject jswZjsproject : zjsprojectList) {
            _zjsproject = new HashMap<String, Object>();
            _zjsproject.put("userId", jswZjsproject.getUserId());
            _zjsproject.put("name", jswZjsproject.getName());
            _zjsproject.put("filePath", jswZjsproject.getFilePath());
            _zjsproject.put("level", jswZjsproject.getLevel());
            _zjsproject.put("id", jswZjsproject.getId());
            _zjsproject.put("pid", jswZjsproject.getParentId());
            _zjsprojectList.add(_zjsproject);
        }
        return new WebResult().ok().set("zjsprojects", _zjsprojectList);
    }

    @RequestMapping("/securi_getChildZjsprojects")
    @ResponseBody
    public JSONObject getChildZjsprojects(@RequestParam(value = "id", required = true) String id,
                                          HttpServletRequest request, HttpServletResponse response) {
        List<JswZjsproject> childZjsprojectList = new ArrayList<JswZjsproject>();
        childZjsprojectList = jswZjsprojectService.getChildZjsprojects(id);
        List<Map<String, Object>> _childZjsprojectList = new ArrayList<Map<String, Object>>();
        Map<String, Object> _childZjsproject = new HashMap<String, Object>();
        for (JswZjsproject childZjsproject : childZjsprojectList) {
            _childZjsproject = new HashMap<String, Object>();
            _childZjsproject.put("userId", childZjsproject.getUserId());
            _childZjsproject.put("name", childZjsproject.getName());
            _childZjsproject.put("filePath", childZjsproject.getFilePath());
            _childZjsproject.put("level", childZjsproject.getLevel());
            _childZjsproject.put("id", childZjsproject.getId());
            _childZjsproject.put("pid", childZjsproject.getParentId());
            _childZjsprojectList.add(_childZjsproject);
        }
        return new WebResult().ok().set("childZjsprojects", _childZjsprojectList);
    }

    @RequestMapping("/securi_getProjectMdbPath")
    @ResponseBody
    public JSONObject getProjectMdbPath(@RequestParam(value = "userId", required = true) String userId,
                                        @RequestParam(value = "fileName", required = true) String fileName,
                                        HttpServletRequest request, HttpServletResponse response) {

        String path = PropertyUtil.getFileRealPath() + "/download/" + userId;
        File file = new File(path);
        File[] tempList = file.listFiles();
        if (tempList != null && tempList.length>0) {
            for (File tmpFile : tempList) {
                tmpFile.delete();
            }
        }

        String remoteBaseUrl = "http://114.55.55.167:8080/project/";
        String baseDest = PropertyUtil.getFileRealPath() + "/download/";
        String filePath = ZipUtil.downloadRemoteFile(remoteBaseUrl, baseDest, userId, fileName);
        File zipFile = new File(filePath);
        ZipUtil.unzip(zipFile, baseDest + userId, "njrxy_+7804");

        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        file = new File(path);
        tempList = file.listFiles();
        String mdbPath = path + "/new" + ".mdb";
        for (File temp : tempList) {
            if (temp.getName().endsWith("mdb")) {
                temp.renameTo(new File(mdbPath));
                break;
            }
        }

        return new WebResult().ok().set("mdbPath", mdbPath);
    }

    @RequestMapping("/securi_getProjectInfo")
    @ResponseBody
    public JSONObject getProjectInfo(@RequestParam(value = "mdbPath", required = true) String mdbPath,
                                     HttpServletRequest request, HttpServletResponse response) {

        List<Map<String, Object>> projectInfos = SqliteHelper.query(mdbPath, "select * from PROJECTINFO");
        Map<String, Object> _projectInfo = new HashMap<String, Object>();
        List<Map<String, Object>> _projectInfos = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> projectInfo : projectInfos) {
            _projectInfo = new HashMap<String, Object>();
            String _key =  StringUtil.trimToEmpty(projectInfo.get("PROJECT_NAME"));
            String _value = StringUtil.trimToEmpty(projectInfo.get("PROJECT_VALUE"));

            if (_key.equals("工程类型") || _key.equals("是否保存在空间") || _key.equals("定额库编码") || _key.equals("工程ID")) {
                continue;
            }

            _projectInfo.put("_key", _key);
            if (_key != null && (_key.equals("单方造价") || _key.equals("工程造价") || _key.equals("招标控制价") || _key.equals("中标价"))) {
                _projectInfo.put("_value", (_value == null || _value.equals("") ? "0.00" : new java.text.DecimalFormat("#.00").format(Double.parseDouble(_value))) + "元");
            } else if (_key.equals("定额库编号")) {
                _projectInfo.put("_value", _value == null || _value.equals("") ? "" : _value.substring(0, _value.lastIndexOf(".mdb")));
            } else {
                _projectInfo.put("_value", _value);
            }
            _projectInfo.put("_isCategory", projectInfo.get("PROJECT_CATEGORY"));
            _projectInfos.add(_projectInfo);
        }
        return new WebResult().ok().set("projectInfos", _projectInfos);
    }

    @RequestMapping("/securi_getProjectDataInfo")
    @ResponseBody
    public JSONObject getProjectDataInfo(@RequestParam(value = "mdbPath", required = true) String mdbPath,
                                         HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> projectDatas = SqliteHelper.query(mdbPath, "select 节点位置,定额号,子目名称,专业,类别,子目单位,工程量,工程量值,基价单价,pointno,描述后的项目特征,计算规则,工作内容,工程量公式 from 工程数据表");
        Map<String, Object> _projectData = new HashMap<String, Object>();
        List<Map<String, Object>> _projectDatas = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> projectData : projectDatas) {
            _projectData = new HashMap<String, Object>();
            _projectData.put("jdwz", projectData.get("节点位置"));
            _projectData.put("deh", projectData.get("定额号"));
            _projectData.put("zmmc", projectData.get("子目名称"));
            _projectData.put("zy", projectData.get("专业"));
            _projectData.put("lb", projectData.get("类别"));
            _projectData.put("zmdw", projectData.get("子目单位"));
            _projectData.put("gclbds", projectData.get("工程量"));
            _projectData.put("gcl", projectData.get("工程量值"));
            _projectData.put("dj", projectData.get("基价单价"));
            _projectData.put("pointNo", projectData.get("POINTNO") == null ? projectData.get("pointno") : projectData.get("POINTNO"));
            _projectData.put("gcnr", projectData.get("工作内容"));
            _projectData.put("jsgz", projectData.get("计算规则"));
            _projectData.put("mshxmtz", projectData.get("描述后的项目特征"));
            _projectData.put("gclgs", projectData.get("工程量公式"));
            _projectDatas.add(_projectData);
        }
        return new WebResult().ok().set("projectDatas", _projectDatas);
    }

    /**
     * 工程工料机表
     *
     * @param mdbPath
     * @param pointNo
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/securi_getProjectMachineInfo")
    @ResponseBody
    public JSONObject getProjectMachineInfo(@RequestParam(value = "mdbPath", required = true) String mdbPath,
                                            @RequestParam(value = "pointNo", required = true) String pointNo,
                                            HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> projectMachineInfoList = SqliteHelper.query(mdbPath, "select * from 工程工料机表 where pointno=" + '\'' + pointNo + '\'');
        List<Map<String, Object>> _projectMachineInfoList = new ArrayList<Map<String, Object>>();
        Map<String, Object> _projectMachineInfo = new HashMap<String, Object>();
        for (Map<String, Object> projectMachineInfo : projectMachineInfoList) {
            _projectMachineInfo = new HashMap<String, Object>();
            _projectMachineInfo.put("clbm", projectMachineInfo.get("材料编码"));
            _projectMachineInfo.put("clmc", projectMachineInfo.get("材料名称"));
            _projectMachineInfo.put("ggxh", projectMachineInfo.get("规格型号"));
            _projectMachineInfo.put("dw", projectMachineInfo.get("单位"));
            _projectMachineInfo.put("je", projectMachineInfo.get("金额"));
            _projectMachineInfo.put("dehl", projectMachineInfo.get("定额数量0"));
            _projectMachineInfo.put("yshl", projectMachineInfo.get("定额数量1"));
            _projectMachineInfo.put("hl", projectMachineInfo.get("定额数量"));
            _projectMachineInfo.put("xs", projectMachineInfo.get("系数"));
            _projectMachineInfo.put("sl", projectMachineInfo.get("数量"));

            List<Map<String, Object>> gljSummarys = SqliteHelper.query(mdbPath, "select scj,csscj,taxrate,cgbgf  from GLJSUMMARY where bm=" + '\'' + projectMachineInfo.get("材料编码") + '\'');
            if (gljSummarys != null && gljSummarys.size() > 0) {
                _projectMachineInfo.put("hsj", gljSummarys.get(0).get("SCJ"));
                _projectMachineInfo.put("cb", gljSummarys.get(0).get("cgbgf"));
                _projectMachineInfo.put("taxrate", gljSummarys.get(0).get("taxrate"));
                _projectMachineInfo.put("csj", gljSummarys.get(0).get("csscj"));
            }

            _projectMachineInfoList.add(_projectMachineInfo);
        }

        return new WebResult().ok().set("projectMachineInfos", _projectMachineInfoList);
    }

    /**
     * 计价程序 QFGS
     *
     * @param mdbPath
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/securi_getQFGSInfo")
    @ResponseBody
    public JSONObject getQFGSInfo(@RequestParam(value = "mdbPath", required = true) String mdbPath,
                                  HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> QFGSInfos = SqliteHelper.query(mdbPath, "select * from QFGS");
        Map<String, Object> _QFGSInfo = new HashMap<String, Object>();
        List<Map<String, Object>> _QFGSInfos = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> QFGSInfo : QFGSInfos) {
            _QFGSInfo = new HashMap<String, Object>();
            _QFGSInfo.put("ID", QFGSInfo.get("ID"));
            _QFGSInfo.put("FYMC", QFGSInfo.get("FYMC"));
            _QFGSInfo.put("JZJSGS", QFGSInfo.get("JZJSGS"));
            _QFGSInfo.put("AZJSGS", QFGSInfo.get("AZJSGS"));
            _QFGSInfo.put("ISHJ", QFGSInfo.get("ISHJ"));
            _QFGSInfo.put("TYPE", QFGSInfo.get("TYPE"));
            _QFGSInfos.add(_QFGSInfo);
        }

        return new WebResult().ok().set("QFGSInfos", _QFGSInfos);
    }

    /**
     * 措施项目 CS1
     *
     * @param mdbPath
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/securi_getCSInfo")
    @ResponseBody
    public JSONObject getCSInfo(@RequestParam(value = "mdbPath", required = true) String mdbPath,
                                HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> csInfos = SqliteHelper.query(mdbPath, "select MEASUREXH,MEASURENAME,MEASUREDW,MEASUREJSGS,MEASUREBASE,MEASUREFEE,FLFW,MEASURESUM,MEASUREBH,MEASUREJG,MEASUREWMF  from CS1");
        Map<String, Object> _csInfo = new HashMap<String, Object>();
        List<Map<String, Object>> _csInfos = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> csInfo : csInfos) {
            _csInfo = new HashMap<String, Object>();
            _csInfo.put("measureXh", csInfo.get("MEASUREXH"));
            _csInfo.put("measureName", csInfo.get("MEASURENAME"));
            _csInfo.put("measureDw", csInfo.get("MEASUREDW"));
            _csInfo.put("measureJsgs", csInfo.get("MEASUREJSGS"));
            _csInfo.put("measureBase", csInfo.get("MEASUREBASE"));
            _csInfo.put("measureFee", csInfo.get("MEASUREFEE"));
            _csInfo.put("flfw", csInfo.get("FLFW"));
            _csInfo.put("measureSum", csInfo.get("MEASURESUM"));
            _csInfo.put("measureBh", csInfo.get("MEASUREBH"));
            _csInfo.put("measureJg", csInfo.get("MEASUREJG"));
            _csInfo.put("measureWmf", csInfo.get("MEASUREWMF"));

            _csInfos.add(_csInfo);
        }
        return new WebResult().ok().set("csInfos", _csInfos);
    }

    /**
     * 措施项目 CS1BL
     *
     * @param mdbPath
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/securi_getCS1BLInfo")
    @ResponseBody
    public JSONObject getCS1BLInfo(@RequestParam(value = "mdbPath", required = true) String mdbPath,
                                   HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> cs1blInfos = SqliteHelper.query(mdbPath, "select * from CS1BL");
        Map<String, Object> _cs1blInfo = new HashMap<String, Object>();
        List<Map<String, Object>> _cs1blInfos = new ArrayList<Map<String, Object>>();
        int i = 1;
        for (Map<String, Object> csInfo : cs1blInfos) {
            _cs1blInfo = new HashMap<String, Object>();
            _cs1blInfo.put("recNo", i++);
            _cs1blInfo.put("varName", csInfo.get("VARNAME"));
            _cs1blInfo.put("varResult", csInfo.get("VARRESULT"));
            _cs1blInfos.add(_cs1blInfo);
        }
        return new WebResult().ok().set("cs1blInfos", _cs1blInfos);
    }

    /**
     * 其它项目
     *
     * @param mdbPath
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/securi_getQTInfo")
    @ResponseBody
    public JSONObject getQTInfo(@RequestParam(value = "mdbPath", required = true) String mdbPath,
                                HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> qtInfos = SqliteHelper.query(mdbPath, "select * from QT");
        Map<String, Object> _qtInfo = new HashMap<String, Object>();
        List<Map<String, Object>> _qtInfos = new ArrayList<Map<String, Object>>();
        int i = 0;
        for (Map<String, Object> qtInfo : qtInfos) {
            _qtInfo = new HashMap<String, Object>();
            _qtInfo.put("id", qtInfo.get("ID"));
            _qtInfo.put("name", qtInfo.get("QTNAME"));
            _qtInfo.put("dw", qtInfo.get("QTDW"));
            _qtInfo.put("jsgs", qtInfo.get("QTJSGS"));
            _qtInfo.put("js", qtInfo.get("QTJS"));
            _qtInfo.put("fl", qtInfo.get("QTFL"));
            _qtInfo.put("je", qtInfo.get("QTJE"));
            _qtInfo.put("jg", qtInfo.get("QTJG"));
            _qtInfo.put("bz", qtInfo.get("QTBZ"));
            _qtInfo.put("pointNo", i++);
            _qtInfos.add(_qtInfo);
        }
        return new WebResult().ok().set("qtInfos", _qtInfos);
    }

    /**
     * 其它项目详情
     *
     * @param pointNo
     * @param jsgs
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/securi_getQTDetailInfo")
    @ResponseBody
    public JSONObject getQTDetailInfo(@RequestParam(value = "mdbPath", required = true) String mdbPath,
                                      @RequestParam(value = "pointNo", required = true) String pointNo,
                                      @RequestParam(value = "jsgs", required = true) String jsgs,
                                      HttpServletRequest request, HttpServletResponse response) {

        List<Map<String, Object>> _zljemxbInfos = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> _zygczgjInfos = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> _jrgInfos = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> _zcbfwfInfos = new ArrayList<Map<String, Object>>();

        if (jsgs.equals("暂列金额")) {
            List<Map<String, Object>> zljemxbInfos = SqliteHelper.query(mdbPath, "select * from ZLJEMXB where 指针号= " + '\'' + pointNo + '\'');
            if (zljemxbInfos != null && zljemxbInfos.size() > 0) {
                Map<String, Object> _zljemxbInfo = new HashMap<String, Object>();
                for (Map<String, Object> zljemxbInfo : zljemxbInfos) {
                    _zljemxbInfo = new HashMap<String, Object>();
                    _zljemxbInfo.put("xh", zljemxbInfo.get("XH"));
                    _zljemxbInfo.put("mc", zljemxbInfo.get("MC"));
                    _zljemxbInfo.put("dw", zljemxbInfo.get("DW"));
                    _zljemxbInfo.put("sl", zljemxbInfo.get("SL"));
                    _zljemxbInfo.put("jsgs", zljemxbInfo.get("jsjc"));
                    _zljemxbInfo.put("dj", zljemxbInfo.get("DJ"));
                    _zljemxbInfo.put("je", zljemxbInfo.get("JE"));
                    _zljemxbInfo.put("bz", zljemxbInfo.get("BZ"));
                    _zljemxbInfos.add(_zljemxbInfo);
                }
            }
        } else if (jsgs.equals("专业工程暂估价")) {
            List<Map<String, Object>> zygczgjInfos = SqliteHelper.query(mdbPath, "select * from ZYGCZGJ where 指针号= " + '\'' + pointNo + '\'');
            if (zygczgjInfos != null && zygczgjInfos.size() > 0) {
                Map<String, Object> _zygczgjInfo = new HashMap<String, Object>();
                for (Map<String, Object> zygczgjInfo : zygczgjInfos) {
                    _zygczgjInfo = new HashMap<String, Object>();
                    _zygczgjInfo.put("xh", zygczgjInfo.get("XH"));
                    _zygczgjInfo.put("mc", zygczgjInfo.get("MC"));
                    _zygczgjInfo.put("dw", zygczgjInfo.get("DW"));
                    _zygczgjInfo.put("sl", zygczgjInfo.get("SL"));
                    _zygczgjInfo.put("dj", zygczgjInfo.get("DJ"));
                    _zygczgjInfo.put("je", zygczgjInfo.get("JE"));
                    _zygczgjInfo.put("gcnr", zygczgjInfo.get("GCNR"));
                    _zygczgjInfo.put("bz", zygczgjInfo.get("BZ"));
                    _zygczgjInfos.add(_zygczgjInfo);
                }
            }
        } else if (jsgs.equals("计日工")) {
            List<Map<String, Object>> jrgInfos = SqliteHelper.query(mdbPath, "select * from JRG");
            if (jrgInfos != null && jrgInfos.size() > 0) {
                Map<String, Object> _jrgInfo = new HashMap<String, Object>();
                for (Map<String, Object> jrgInfo : jrgInfos) {
                    _jrgInfo = new HashMap<String, Object>();
                    _jrgInfo.put("bh", jrgInfo.get("BH"));
                    _jrgInfo.put("mc", jrgInfo.get("MC"));
                    _jrgInfo.put("dw", jrgInfo.get("DW"));
                    _jrgInfo.put("sl", jrgInfo.get("SL"));
                    _jrgInfo.put("dj", jrgInfo.get("DJ"));
                    _jrgInfo.put("hj", jrgInfo.get("HJ"));

                    //lb=1,人工，lb=2，材料，lb=3，机械，lb=4，管理费利润
                    if (jrgInfo.get("LB").equals("1")) {
                        _jrgInfo.put("type", "人工");
                    } else if (jrgInfo.get("LB").equals("2")) {
                        _jrgInfo.put("type", "材料");
                    } else if (jrgInfo.get("LB").equals("3")) {
                        _jrgInfo.put("type", "施工机械");
                    } else if (jrgInfo.get("LB").equals("4")) {
                        _jrgInfo.put("type", "管理费利润");
                    }
                    _jrgInfos.add(_jrgInfo);
                }
            }

        } else if (jsgs.equals("总承包服务费")) {
            List<Map<String, Object>> zcbfwfInfos = SqliteHelper.query(mdbPath, "select * from ZCBFWF where 指针号= " + '\'' + pointNo + '\'');
            if (zcbfwfInfos != null && zcbfwfInfos.size() > 0) {
                Map<String, Object> _zcbfwfInfo = new HashMap<String, Object>();
                for (Map<String, Object> zcbfwfInfo : zcbfwfInfos) {
                    _zcbfwfInfo = new HashMap<String, Object>();
                    _zcbfwfInfo.put("xh", zcbfwfInfo.get("XH"));
                    _zcbfwfInfo.put("mc", zcbfwfInfo.get("MC"));
                    _zcbfwfInfo.put("nr", zcbfwfInfo.get("NR"));
                    _zcbfwfInfo.put("jsjc", zcbfwfInfo.get("jsjc"));
                    _zcbfwfInfo.put("xmjz", zcbfwfInfo.get("XMJZ"));
                    _zcbfwfInfo.put("fl", zcbfwfInfo.get("FL"));
                    _zcbfwfInfo.put("je", zcbfwfInfo.get("JE"));
                    _zcbfwfInfos.add(_zcbfwfInfo);
                }
            }
        }
        return new WebResult().ok()
                .set("zljemxbInfos", _zljemxbInfos)
                .set("zygczgjInfos", _zygczgjInfos)
                .set("jrgInfos", _jrgInfos)
                .set("zcbfwfInfos", _zcbfwfInfos);
    }

    /**
     * 取费
     *
     * @param mdbPath
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/securi_getSummaryInfo")
    @ResponseBody
    public JSONObject getSummaryInfo(@RequestParam(value = "mdbPath", required = true) String mdbPath,
                                     HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> summaryInfos = SqliteHelper.query(mdbPath, "select * from SUMMARY");
        Map<String, Object> _summaryInfo = new HashMap<String, Object>();
        List<Map<String, Object>> _summaryInfos = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> summaryInfo : summaryInfos) {
            _summaryInfo = new HashMap<String, Object>();
            _summaryInfo.put("FEEXH", summaryInfo.get("FEEXH"));
            _summaryInfo.put("FEENAME", summaryInfo.get("FEENAME"));
            _summaryInfo.put("FEECALC", summaryInfo.get("FEECALC"));
            _summaryInfo.put("FEEBASE", summaryInfo.get("FEEBASE"));
            _summaryInfo.put("FEERATE", summaryInfo.get("FEERATE"));
            _summaryInfo.put("FEERESULT", summaryInfo.get("FEERESULT"));
            _summaryInfo.put("ISSHOW", summaryInfo.get("ISSHOW"));
            _summaryInfo.put("FEEBH", summaryInfo.get("FEEBH"));
            _summaryInfo.put("FEEJG", summaryInfo.get("FEEJG"));
            _summaryInfo.put("FEEREMARK", summaryInfo.get("FEEREMARK"));
            _summaryInfos.add(_summaryInfo);
        }
        return new WebResult().ok().set("summaryInfos", _summaryInfos);
    }

    /**
     * 取费 SUMMARYBL
     *
     * @param mdbPath
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/securi_getSummaryBLInfo")
    @ResponseBody
    public JSONObject getSummaryBLInfo(@RequestParam(value = "mdbPath", required = true) String mdbPath,
                                       HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> summaryBLInfos = SqliteHelper.query(mdbPath, "select * from SUMMARYBL");
        Map<String, Object> _summaryBLInfo = new HashMap<String, Object>();
        List<Map<String, Object>> _summaryBLInfos = new ArrayList<Map<String, Object>>();
        int i = 1;
        for (Map<String, Object> summaryBLInfo : summaryBLInfos) {
            _summaryBLInfo = new HashMap<String, Object>();
            _summaryBLInfo.put("recNo", i++);
            _summaryBLInfo.put("varName", summaryBLInfo.get("VARNAME"));
            _summaryBLInfo.put("varResult", summaryBLInfo.get("VARRESULT"));
            _summaryBLInfos.add(_summaryBLInfo);
        }
        return new WebResult().ok().set("summaryBLInfos", _summaryBLInfos);
    }

    @RequestMapping("/securi_getGLJFJB")
    @ResponseBody
    public JSONObject getGLJFJB(@RequestParam(value = "mdbPath", required = true) String mdbPath,
                                HttpServletRequest request, HttpServletResponse response) {

        List<Map<String, Object>> _gljfjbInfos = new ArrayList<Map<String, Object>>();
        Map<String, Object> _gljfjbInfo = new HashMap<String, Object>();

        String sql = "select g.ZGCL, g.ZYCL, g.JG, g.BM, g.GLJMC, g.DW, d.sl, g.DJ, g.SCJ, g.cgbgf, g.taxrate, g.csscj, g.JE, g.fxxs, g.jzdj, g.GGXH, g.GYCS, g.CD, g.ZJYY  from\n" +
                "(select clbm, sum(tmpsl) as sl from (\n" +
                "select 材料编码 clbm, 定额数量 * 工程量值 as tmpsl from GLJFJB a, 工程数据表 b where a.pointno = b.pointno\n" +
                "union all\n" +
                "select 材料编码 clbm, 定额数量 * 工程量值 as tmpsl from CS2GLJFJB a, CS2 b where a.pointno = b.pointno) group by clbm) d,\n" +
                "GLJSUMMARY g\n" +
                "where g.BM = d.clbm\n" +
                "order by clbm asc";
        List<Map<String, Object>> gljfjbInfos = SqliteHelper.query(mdbPath, sql);

        if (gljfjbInfos != null && gljfjbInfos.size() > 0) {
            for (Map<String, Object> gljfjbInfo : gljfjbInfos) {
                _gljfjbInfo = new HashMap<String, Object>();
                _gljfjbInfo.put("zg", gljfjbInfo.get("ZGCL"));
                _gljfjbInfo.put("dy", gljfjbInfo.get("ZYCL"));
                _gljfjbInfo.put("jg", gljfjbInfo.get("JG"));
                _gljfjbInfo.put("bm", gljfjbInfo.get("BM"));
                _gljfjbInfo.put("gljmc", gljfjbInfo.get("GLJMC"));
                _gljfjbInfo.put("dw", gljfjbInfo.get("DW"));
                _gljfjbInfo.put("sl", gljfjbInfo.get("sl"));
                _gljfjbInfo.put("dj", gljfjbInfo.get("DJ"));
                _gljfjbInfo.put("hsj", gljfjbInfo.get("SCJ"));
                _gljfjbInfo.put("cb", gljfjbInfo.get("cgbgf"));
                _gljfjbInfo.put("taxrate", gljfjbInfo.get("taxrate"));
                _gljfjbInfo.put("csj", gljfjbInfo.get("csscj"));
                _gljfjbInfo.put("je", gljfjbInfo.get("JE"));
                _gljfjbInfo.put("fxxs", gljfjbInfo.get("fxxs"));
                _gljfjbInfo.put("jzdj", gljfjbInfo.get("jzdj"));
                _gljfjbInfo.put("ggxh", gljfjbInfo.get("GGXH"));
                _gljfjbInfo.put("gycs", gljfjbInfo.get("GYCS"));
                _gljfjbInfo.put("shdd", gljfjbInfo.get("CD"));
                _gljfjbInfo.put("jhfs", gljfjbInfo.get("ZJYY"));
                _gljfjbInfos.add(_gljfjbInfo);
            }
        }

        return new WebResult().ok().set("gljfjbInfos", _gljfjbInfos);
    }

    @RequestMapping("/securi_getRatetableInfo")
    @ResponseBody
    public JSONObject getRatetableInfo(@RequestParam(value = "mdbPath", required = true) String mdbPath,
                                       HttpServletRequest request, HttpServletResponse response) {


        List<Map<String, Object>> ratetableInfos = SqliteHelper.query(mdbPath,
                "select 类别 AS 'lb', " +
                        "费率1 AS 'a1',\n" +
                        "费率2 AS 'a2',\n" +
                        "费率3 AS 'a3',\n" +
                        "费率4 AS 'a4',\n" +
                        "费率5 AS 'a5',\n" +
                        "费率6 AS 'a6',\n" +
                        "费率7 AS 'a7',\n" +
                        "费率8 AS 'a8',\n" +
                        "费率9 AS 'a9',\n" +
                        "费率10 AS 'b1',\n" +
                        "费率11 AS 'b2',\n" +
                        "费率12 AS 'b3',\n" +
                        "费率13 AS 'b4',\n" +
                        "费率14 AS 'b5',\n" +
                        "费率15 AS 'b6',\n" +
                        "费率16 AS 'b7',\n" +
                        "费率17 AS 'b8',\n" +
                        "费率18 AS 'b9',\n" +
                        "费率19 AS 'c1',\n" +
                        "费率20 AS 'c2',\n" +
                        "费率21 AS 'c3',\n" +
                        "费率22 AS 'c4',\n" +
                        "费率23 AS 'c5',\n" +
                        "费率24 AS 'c6',\n" +
                        "费率25 AS 'c7',\n" +
                        "费率26 AS 'c8',\n" +
                        "费率27 AS 'c9',\n" +
                        "费率28 AS 'd1',\n" +
                        "费率29 AS 'd2',\n" +
                        "费率30 AS 'd3',\n" +
                        "费率31 AS 'd4',\n" +
                        "费率32 AS 'd5',\n" +
                        "费率33 AS 'd6',\n" +
                        "费率34 AS 'd7',\n" +
                        "费率35 AS 'd8',\n" +
                        "费率36 AS 'd9',\n" +
                        "费率37 AS 'e1',\n" +
                        "费率38 AS 'e2',\n" +
                        "费率39 AS 'e3',\n" +
                        "费率40 AS 'e4',\n" +
                        "费率41 AS 'e5',\n" +
                        "费率42 AS 'e6',\n" +
                        "费率43 AS 'e7',\n" +
                        "费率44 AS 'e8',\n" +
                        "费率45 AS 'e9',\n" +
                        "费率46 AS 'f1',\n" +
                        "费率47 AS 'f2',\n" +
                        "费率48 AS 'f3',\n" +
                        "费率49 AS 'f4',\n" +
                        "费率50 AS 'f5',\n" +
                        "费率51 AS 'f6',\n" +
                        "费率52 AS 'f7',\n" +
                        "费率53 AS 'f8',\n" +
                        "费率54 AS 'f9',\n" +
                        "费率55 AS 'g1',\n" +
                        "费率56 AS 'g2',\n" +
                        "费率57 AS 'g3',\n" +
                        "费率58 AS 'g4',\n" +
                        "费率59 AS 'g5',\n" +
                        "费率60 AS 'g6',\n" +
                        "费率61 AS 'g7',\n" +
                        "费率62 AS 'g8',\n" +
                        "费率63 AS 'g9',\n" +
                        "费率64 AS 'e1',\n" +
                        "费率65 AS 'e2',\n" +
                        "费率66 AS 'e3',\n" +
                        "费率67 AS 'e4',\n" +
                        "费率68 AS 'e5',\n" +
                        "费率69 AS 'e6',\n" +
                        "费率70 AS 'e7',\n" +
                        "费率71 AS 'e8',\n" +
                        "费率72 AS 'e9' from RATETABLE where 费率去处 = '1'");

        List<String> lbs = new ArrayList<String>();
        List<Map<String, Object>> sortRatetableInfos = new ArrayList<Map<String, Object>>();
        List<Integer> xCount = new ArrayList<Integer>();
        int i=0;
        Map<String, Object> xTmp = new HashMap<String, Object>();
        for (Map<String, Object> ratetableInfo : ratetableInfos) {
            lbs.add(StringUtil.trimToEmpty(ratetableInfo.get("lb")));
            ratetableInfo.remove("lb");
            sortRatetableInfos.add(new TreeMap<String, Object>(ratetableInfo));

            xCount.add(i++);
        }

        List<Map<String, Object>> horizontalRatetableInfos = landscapeToPortrait(sortRatetableInfos);

        List<Map<String, Object>> jsqdeDbqInfos = SqliteHelper.query(mdbPath, "select * from JSQDDE_DBQ");

        List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
        Map<String, Object> rtnMap = new HashMap<String, Object>();

        for (Map<String, Object> jsqdeDbqInfo : jsqdeDbqInfos) {
            for (Map<String, Object> horizontalRatetableInfo : horizontalRatetableInfos) {
                if (horizontalRatetableInfo.get("xh").equals(jsqdeDbqInfo.get("序号"))) {
                    rtnMap = new HashMap<String, Object>();
                    rtnMap.put("lbmc", jsqdeDbqInfo.get("费用名称"));
                    rtnMap.put("jzl", jsqdeDbqInfo.get("取费基数"));
                    rtnMap.putAll(horizontalRatetableInfo);
                    break;
                }
            }
            rtnList.add(rtnMap);
        }

        return new WebResult().ok().set("ratetableInfos", rtnList).set("lbs", lbs).set("xCount", xCount);
    }

    private List<Map<String, Object>> landscapeToPortrait(List<Map<String, Object>> lList) {
        if (lList == null || lList.size() == 0) {
            return lList;
        }
        List<Map<String, Object>> pList = new ArrayList<Map<String, Object>>();
        Map<String, Object> pMap = new HashMap<String, Object>();

        Map<String, Object> tmp = lList.get(0);
        Iterator it = tmp.entrySet().iterator();
        int j = 1;
        while (it.hasNext()) {
            Map.Entry<String, Object> enter = (Map.Entry<String, Object>) it.next();
            String _key = enter.getKey();
            pMap = new HashMap<String, Object>();
            pMap.put("xh", j++);
            for (int i = 0; i < lList.size(); i++) {
                Iterator it1 = lList.get(i).entrySet().iterator();
                while (it1.hasNext()) {
                    Map.Entry<String, Object> enter1 = (Map.Entry<String, Object>) it1.next();
                    if (enter1.getKey().equals(_key)) {
                        pMap.put("x" + i, lList.get(i).get(_key));
                        break;
                    }
                }

            }
            pList.add(pMap);
        }


        return pList;
    }

    @RequestMapping("/securi_discussShow")
    @ResponseBody
    public JSONObject discussShow(@RequestParam(value = "discussType", required = true) String discussType,
                                  @RequestParam(value = "discussId", required = true) String discussId,
                                  @RequestParam(value = "uid", required = true) String uid,
                                  HttpServletRequest request, HttpServletResponse response) {

        List<Discuss> discussList = discussService.getDiscussList(discussId);
        for (Discuss discuss : discussList) {
            User user = userService.getUser(discuss.getCreateUser());
            if (user != null) {
                String isMyself = discuss.getCreateUser().equals(uid) ? "1" : "0";
                discuss.setIsMyself(isMyself);
                String name = user.getRealname() != null && !user.getRealname().equals("") ? user.getRealname() : user.getUsername();
                discuss.setCreateUser(name);
            }
        }

        String varTitle = "";
        String varName = "";
        Date varDate = new Date();
        if(discussType.equals("0")) {
            TFieldData tempFieldData = fieldDataService.detail(discussId);

            String projectId = tempFieldData.getProjectName();

            Project project = projectService.findOneView(Integer.parseInt(projectId));
            varTitle = project.getProName() + ":    " + tempFieldData.getDataName();

            User user = userService.getUser(StringUtil.trimToEmpty(tempFieldData.getId()));
            varName = user.getRealname() != null && !user.getRealname().equals("") ? user.getRealname() : user.getUsername();

            varDate = tempFieldData.getCreatTime();
        } else if(discussType.equals("1")) {
            Project project = projectService.findOneView(Integer.parseInt(discussId));

            varTitle = project.getProName();

            User user = userService.getUser(StringUtil.trimToEmpty(project.getUid()));
            varName = user.getRealname() != null && !user.getRealname().equals("") ? user.getRealname() : user.getUsername();
        }

        return new WebResult().ok().set("varTitle", varTitle).set("varName", varName).set("varDate", varDate)
                .set("discussType", discussType)
                .set("discussId", discussId).set("discussList", discussList).set("discussCount", discussList.size());
    }

    @RequestMapping("/securi_getStorageCount")
    @ResponseBody
    public JSONObject getStorageCount (@RequestParam(value = "id", required = true) String id,
                                       HttpServletRequest request, HttpServletResponse response) {
        double outCount = 0.00;
        TFieldData tFieldData = fieldDataService.detail(id);
        List<TFieldData> outFieldDataList = fieldDataService.getOutFieldByRelId(id);
        if (outFieldDataList != null && outFieldDataList.size()>0) {
            for (TFieldData out : outFieldDataList) {
                outCount += (out.getCount() != null && !out.getCount().equals("")) ? Double.parseDouble(out.getCount()) : 0.00;
            }
        }

        double storageCount = Double.parseDouble(tFieldData.getCount()) + outCount;

        return new WebResult().ok().set("storageCount", storageCount);
    }

    @ResponseBody
    @RequestMapping("/securi_saveOutFieldData")
    public JSONObject saveOutFieldData(@RequestParam(value = "outProId", required = true) String outProId,
                                       @RequestParam(value = "outCount", required = true) String outCount,
                                       @RequestParam(value = "id", required = true) String id,
                                       @RequestParam(value = "uid", required = true) String uid,
                                       HttpServletRequest request) {
        TFieldData tFieldData = fieldDataService.detail(id);

        TFieldData outFieldData = new TFieldData();
        outFieldData.setSection(tFieldData.getSection());
        outFieldData.setCid(tFieldData.getCid());
        outFieldData.setUid(uid);
        outFieldData.setSupplier(tFieldData.getSupplier());
        outFieldData.setCostType(tFieldData.getCostType());
        outFieldData.setDataName(tFieldData.getDataName());
        outFieldData.setIsDelete(0);
        outFieldData.setProjectName(outProId);
        outFieldData.setPrice(tFieldData.getPrice());
        outFieldData.setCompany(tFieldData.getCompany());
        outFieldData.setItemCode(tFieldData.getItemCode());
        outFieldData.setSpecifications(tFieldData.getSpecifications());
        outFieldData.setRemark("出库");
        outFieldData.setNeedApproved("0");
        User user = userService.getUser(uid);

        String realName = user.getRealname();
        if (realName == null || realName.equals("")) {
            realName = user.getUsername();
        }
        outFieldData.setUname(realName);

        outFieldData.setUnit(tFieldData.getUnit());

        Boolean isNumber = outCount.matches("-?[0-9]+.*[0-9]*");
        outFieldData.setCount(StringUtil.trimToEmpty(-Double.parseDouble(outCount)));
        outFieldData.setRelId(id);

        fieldDataService.add(outFieldData);

        return new WebResult().ok();
    }
}
