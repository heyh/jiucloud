package sy.controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sy.model.Item;
import sy.model.ParamTrans;
import sy.model.S_department;
import sy.model.po.*;
import sy.pageModel.DataGrid;
import sy.pageModel.FieldData;
import sy.pageModel.Json;
import sy.pageModel.PageHelper;
import sy.pageModel.SessionInfo;
import sy.pageModel.User;
import sy.service.*;
import sy.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Thread.sleep;

@Controller
@RequestMapping("/fieldDataController")
public class FieldDataController extends BaseController {

    @Autowired
    private FieldDataServiceI fieldDataServiceI;

    @Autowired
    private ProjectServiceI projectServiceI;

    @Autowired
    private GCPoServiceI gcpoServiceI;

    @Autowired
    private CostServiceI costServiceI;

    @Autowired
    private DepartmentServiceI departmentService;

    @Autowired
    private UserServiceI userService;

    @Autowired
    private JswZjsprojectServiceI jswZjsprojectService;

    @Autowired
    private ParamTransServiceI paramTransService;

    @Autowired
    private ItemServiceI itemService;

    @Autowired
    private CompanyServiceI companyService;

    /**
     * 跳转管理页面
     *
     * @return
     */
    @RequestMapping("/fieldDataShow")
    public String fieldDataShow(HttpServletRequest req) {
        req.setAttribute("first", UtilDate.getshortFirst());
        req.setAttribute("last", UtilDate.getshortLast());
        return "/app/fielddata/fielddataShow";
    }

    /**
     * 跳转费用管理页面
     *
     * @return
     */
    @RequestMapping("/docDataShow")
    public String docDataShow(HttpServletRequest req) {
        req.setAttribute("first", UtilDate.getshortFirst());
        req.setAttribute("last", UtilDate.getshortLast());
        return "/app/fielddata/docdataShow";
    }

    @RequestMapping("/billDataShow")
    public String billDataShow(HttpServletRequest req) {
        req.setAttribute("first", UtilDate.getshortFirst());
        req.setAttribute("last", UtilDate.getshortLast());
        return "/app/fielddata/billdataShow";
    }

    @RequestMapping("/materialDataShow")
    public String materialDataShow(HttpServletRequest req) {
        req.setAttribute("first", UtilDate.getshortFirst());
        req.setAttribute("last", UtilDate.getshortLast());
        return "/app/fielddata/materialdataShow";
    }

    /**
     * 获取管理页面数据
     */
    @RequestMapping("/dataGrid")
    @ResponseBody
    public DataGrid dataGrid(FieldData fieldData, PageHelper ph, HttpServletRequest request, HttpSession session) {
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
//        if (sessionInfo.getRightList().contains("15") || sessionInfo.getRightList().contains("16") || sessionInfo.getRightList().contains("17") ) {
            fieldData.setCid(cid);
//        }
        List<Integer> ugroup = sessionInfo.getUgroup();
        String source = request.getParameter("source");
        String keyword = StringUtil.trimToEmpty(request.getParameter("keyword"));
        if (request.getParameter("id") != null) {
            Integer id = Integer.parseInt(request.getParameter("id"));
            fieldData.setId(id);
        }
        if (null == fieldData.getStartTime() && null == fieldData.getEndTime()) {
            fieldData.setStartTime(UtilDate.getshortFirst() + " 00:00:00");
            fieldData.setEndTime(UtilDate.getshortLast() + " 23:59:59");
        }
        if (null != request.getParameter("needApproved")) {
            fieldData.setNeedApproved(request.getParameter("needApproved"));
        }
        DataGrid dataGrid = fieldDataServiceI.dataGrid(fieldData, ph, ugroup, source, keyword);

        // add by heyh begin 审批数据
        List<FieldData> fieldDatas = dataGrid.getRows();
        if (fieldDatas != null && fieldDatas.size() > 0) {
            for (int i = fieldDatas.size() - 1; i >= 0; i--) {
                String currentApprovedUser = fieldDatas.get(i).getCurrentApprovedUser() == null ? "" : fieldDatas.get(i).getCurrentApprovedUser();

                if (null != request.getParameter("needApproved")) {
                    if (!currentApprovedUser.equals(sessionInfo.getId())) {
                        fieldDatas.remove(i);
                        dataGrid.setTotal(dataGrid.getTotal() - 1);
                        continue;
                    }
                }

                if (!currentApprovedUser.equals("")) {
                    User user = userService.getUser(currentApprovedUser);
                    String realName = user.getRealname();
                    if (realName == null || realName.equals("")) {
                        realName = user.getUsername();
                    }
                    fieldDatas.get(i).setCurrentApprovedUser(realName);
                }

                String uids = fieldDatas.get(i).getApprovedUser();
                StringBuffer approvedUser = new StringBuffer();
                if (uids != null && !uids.equals("")) {
                    String[] uidArr = uids.split(",");
                    for (String uid : uidArr) {
                        if (!uid.equals("")) {
                            User user = userService.getUser(uid);
                            if (user == null) {
                                continue;
                            }
                            String realName = user.getRealname();
                            if (realName == null || realName.equals("")) {
                                realName = user.getUsername();
                            }
                            approvedUser.append(realName).append(",");
                        }
                    }
                }
                fieldDatas.get(i).setApprovedUser(approvedUser == null || approvedUser.toString().equals("") ? "" : approvedUser.substring(0, approvedUser.length() - 1));
            }
        }
//        dataGrid.setTotal((long) fieldDatas.size());
        // add by heyh end
        session.setAttribute("analusisInfo", fieldData);
        return dataGrid;
    }

    /**
     * 获取附件管理页面数据
     */
    @RequestMapping("/securi_filedataGrid")
    @ResponseBody
    public DataGrid dataGrid(HttpServletRequest request) {
        String filename = request.getParameter("filename");
        String filetype = request.getParameter("filetype");
        String id = request.getParameter("id");
        return gcpoServiceI.dataGrid(id, filename, filetype);
    }

    /**
     * 跳转工程选择弹出框
     */
    @RequestMapping("/securi_selectp")
    public String selectp(HttpServletRequest request, String proid,
                          String proName) {
        return "/app/fielddata/project_select";
    }

    /**
     * 获取费用类型选择弹出框
     */
    @RequestMapping("/securi_selectc")
    public String selectc() {
        return "/app/fielddata/cost_select";
    }

    /**
     * 获取费用类型选择弹出框
     */
    @RequestMapping("/securi_selectcc")
    public String selectcc() {
        return "/app/fielddata/cost_selectcc";
    }

    /**
     * 跳转附件预览
     */
    @RequestMapping("/securi_showfile")
    public String showfile(HttpServletRequest request) {
        String id = request.getParameter("id");
        GCPo file = gcpoServiceI.findOneView(Integer.parseInt(id));
        if (file.getExt().equals("png") || file.getExt().equals("jpg")) {
            request.setAttribute("v", file.getSourceFilePath());
            return "/app/fielddata/pic";
        } else if (file.getExt().equals("mp3")) {
            request.setAttribute("v", file.getSourceFilePath());
            return "/app/fielddata/music";
        } else if (file.getExt().equals("mp4")) {
            request.setAttribute("v", file.getSourceFilePath());
            return "/app/fielddata/video";
        } else {
            request.setAttribute("v", file.getSwfFilePath());
            request.setAttribute("pdfPath", file.getPdfFilePath());
            return "/app/fielddata/view";
        }
    }

    /**
     * 跳转到附件管理
     *
     * @param request
     */
    @RequestMapping("/securi_fieldDataFile")
    public String addFilePage(String id, HttpServletRequest request) {
        request.setAttribute("id", id);
        return "/app/fielddata/fieldDataFile";
    }

    /**
     * 跳转到修改
     *
     * @param request
     * @return
     * @throws java.io.IOException
     */
    @RequestMapping("/upfieldData")
    public String updatePage(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        String preview = request.getParameter("preview");
        TFieldData tFieldData = fieldDataServiceI.detail(id);
        if (tFieldData == null) {
            response.getWriter().write("1");
            return null;
        }
        Project project = projectServiceI.findOneView(Integer
                .parseInt(tFieldData.getProjectName()));
        Cost cost = costServiceI.findById(tFieldData.getCostType());

        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        String uid = sessionInfo.getId();
        List<Map<String, Object>> selectItems = itemService.getSelectItems(cid, tFieldData.getProjectName());
        if (selectItems.size()<=0) {
            List<Item> defaultItemList = itemService.getDefaultItems();
            for (Item defaultItem : defaultItemList) {
                defaultItem.setCid(cid);
                defaultItem.setProjectId(tFieldData.getProjectName());
                defaultItem.setOperator(uid);
                itemService.add(defaultItem);
            }
            selectItems = itemService.getSelectItems(cid, tFieldData.getProjectName());
        }

        request.setAttribute("tfielddata", tFieldData);
        request.setAttribute("project", project);
        request.setAttribute("cost", cost);
        request.setAttribute("preview", preview == null ? false : true);
        request.setAttribute("selectItems", selectItems);

        // modify by heyh begin
        String fj = tFieldData.getItemCode().substring(0, 3);
        if (fj.equals("000") || Integer.parseInt(fj) > 900) {
            return "/app/fielddata/updatefieldData4Doc";
        } else {
            return "/app/fielddata/updatefieldData";
        }

    }

    @RequestMapping("/outStorage")
    public String outStoragePage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        String preview = request.getParameter("preview");
        TFieldData tFieldData = fieldDataServiceI.detail(id);
        if (tFieldData == null) {
            response.getWriter().write("1");
            return null;
        }

        double outCount = 0.00;
        List<TFieldData> outFieldDataList = fieldDataServiceI.getOutFieldByRelId(id);
        if (outFieldDataList != null && outFieldDataList.size()>0) {
            for (TFieldData out : outFieldDataList) {
                outCount += (out.getCount() != null && !out.getCount().equals("")) ? Double.parseDouble(out.getCount()) : 0.00;
            }
        }

        double storageCount = Double.parseDouble(tFieldData.getCount()) + outCount;

        Project project = projectServiceI.findOneView(Integer.parseInt(tFieldData.getProjectName()));
        Cost cost = costServiceI.findById(tFieldData.getCostType());

        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        String uid = sessionInfo.getId();
        List<Map<String, Object>> selectItems = itemService.getSelectItems(cid, tFieldData.getProjectName());
        if (selectItems.size()<=0) {
            List<Item> defaultItemList = itemService.getDefaultItems();
            for (Item defaultItem : defaultItemList) {
                defaultItem.setCid(cid);
                defaultItem.setProjectId(tFieldData.getProjectName());
                defaultItem.setOperator(uid);
                itemService.add(defaultItem);
            }
            selectItems = itemService.getSelectItems(cid, tFieldData.getProjectName());
        }

        request.setAttribute("storageCount", storageCount != 0 ? String.format("%.4f", storageCount) : 0.00);
        request.setAttribute("tfielddata", tFieldData);
        request.setAttribute("project", project);
        request.setAttribute("cost", cost);
        request.setAttribute("preview", preview == null ? false : true);
        request.setAttribute("selectItems", selectItems);


        return "/app/fielddata/outStorage";

    }

    @ResponseBody
    @RequestMapping("/securi_saveOutFieldData")
    public com.alibaba.fastjson.JSONObject saveFieldData(@RequestParam(value = "outProId", required = true) String outProId,
                                                         @RequestParam(value = "outCount", required = true) String outCount,
                                                         @RequestParam(value = "id", required = true) String id,
                                                         @RequestParam(value = "currentApprovedUser", required = true) String currentApprovedUser,
                                                         HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        TFieldData tFieldData = fieldDataServiceI.detail(id);

        TFieldData outFieldData = new TFieldData();
        outFieldData.setSection(tFieldData.getSection());
        outFieldData.setCid(tFieldData.getCid());
        outFieldData.setUid(sessionInfo.getId());
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
        outFieldData.setNeedApproved("1");
        outFieldData.setApprovedUser(currentApprovedUser);
        outFieldData.setCurrentApprovedUser(currentApprovedUser);
        outFieldData.setUname((sessionInfo.getName() != null && !sessionInfo.getName().equals("")) ? sessionInfo.getName() : sessionInfo.getUsername());
        outFieldData.setUnit(tFieldData.getUnit());

        Boolean isNumber = outCount.matches("-?[0-9]+.*[0-9]*");
        outFieldData.setCount(StringUtil.trimToEmpty(-Double.parseDouble(outCount)));
        outFieldData.setRelId(id);

        fieldDataServiceI.add(outFieldData);

        return new WebResult().ok();
    }
    /**
     * 跳转到添加
     *
     * @param request
     * @return
     */
    @RequestMapping("/addfieldData")
    public String addPage(HttpServletRequest request, String proid) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession()
                .getAttribute(ConfigUtil.getSessionInfoName());
        String cost_id = sessionInfo.getLast_cost_id();
        Cost cost = null;
        String proName = "";

        if (cost_id != null && cost_id.length() > 0) {
            cost = costServiceI.findById(cost_id);
        }

        if (proid != null && proid.length() > 0) {
            Project project = projectServiceI.findOneView(Integer
                    .parseInt(proid));
            proName = project.getProName();
        } else {
            proid = sessionInfo.getLast_project_id();
            if (proid != null && proid.length() > 0) {
                proName = projectServiceI.findOneView(Integer.parseInt(proid))
                        .getProName();
            }
        }

        String cid = sessionInfo.getCompid();
        String uid = sessionInfo.getId();
        TFieldData tFieldData = fieldDataServiceI.getMaxFieldByCidUid(cid, uid);
        String maxProjectId = null;
        if (tFieldData != null) {
            maxProjectId = tFieldData.getProjectName();
        }
        request.setAttribute("maxProjectId", maxProjectId);

        request.setAttribute("cost", cost);
        request.setAttribute("proid", proid);
        request.setAttribute("proName", proName);
        return "/app/fielddata/addfieldData";
    }

    /**
     * 快速添加项目费用数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/quickAddFieldData")
    public String quickAddFieldData(HttpServletRequest request, HttpServletResponse response) {
        return "app/fielddata/quickAddFieldData";
    }

    /**
     * 跳转到添加
     *
     * @param request
     * @return
     */
    @RequestMapping("/addDocData")
    public String addDocDataPage(HttpServletRequest request, String proid) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String cost_id = sessionInfo.getLast_cost_id();
        Cost cost = null;
        String proName = "";

        if (cost_id != null && cost_id.length() > 0) {
            cost = costServiceI.findById(cost_id);
        }

        if (proid != null && proid.length() > 0) {
            Project project = projectServiceI.findOneView(Integer
                    .parseInt(proid));
            proName = project.getProName();
        } else {
            proid = sessionInfo.getLast_project_id();
            if (proid != null && proid.length() > 0) {
                proName = projectServiceI.findOneView(Integer.parseInt(proid))
                        .getProName();
            }
        }

        String cid = sessionInfo.getCompid();
        String uid = sessionInfo.getId();
        TFieldData tFieldData = fieldDataServiceI.getMaxFieldByCidUid(cid, uid);
        String maxProjectId = null;
        if (tFieldData != null) {
            maxProjectId = tFieldData.getProjectName();
        }
        request.setAttribute("maxProjectId", maxProjectId);

        request.setAttribute("cost", cost);
        request.setAttribute("proid", proid);
        request.setAttribute("proName", proName);
        return "/app/fielddata/addDocData";
    }

    @RequestMapping("/securi_addBillData")
    public String addBillDataPage(HttpServletRequest request, String proid) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String cost_id = sessionInfo.getLast_cost_id();
        Cost cost = null;
        String proName = "";

        if (cost_id != null && cost_id.length() > 0) {
            cost = costServiceI.findById(cost_id);
        }

        if (proid != null && proid.length() > 0) {
            Project project = projectServiceI.findOneView(Integer
                    .parseInt(proid));
            proName = project.getProName();
        } else {
            proid = sessionInfo.getLast_project_id();
            if (proid != null && proid.length() > 0) {
                proName = projectServiceI.findOneView(Integer.parseInt(proid))
                        .getProName();
            }
        }

        String cid = sessionInfo.getCompid();
        String uid = sessionInfo.getId();
        TFieldData tFieldData = fieldDataServiceI.getMaxFieldByCidUid(cid, uid);
        String maxProjectId = null;
        if (tFieldData != null) {
            maxProjectId = tFieldData.getProjectName();
        }
        request.setAttribute("maxProjectId", maxProjectId);

        request.setAttribute("cost", cost);
        request.setAttribute("proid", proid);
        request.setAttribute("proName", proName);
        return "/app/fielddata/addBillData";
    }

    @RequestMapping("/securi_addMaterialData")
    public String addMaterialDataPage(HttpServletRequest request, String proid) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String cost_id = sessionInfo.getLast_cost_id();
        Cost cost = null;
        String proName = "";

        if (cost_id != null && cost_id.length() > 0) {
            cost = costServiceI.findById(cost_id);
        }

        if (proid != null && proid.length() > 0) {
            Project project = projectServiceI.findOneView(Integer
                    .parseInt(proid));
            proName = project.getProName();
        } else {
            proid = sessionInfo.getLast_project_id();
            if (proid != null && proid.length() > 0) {
                proName = projectServiceI.findOneView(Integer.parseInt(proid))
                        .getProName();
            }
        }

        String cid = sessionInfo.getCompid();
        String uid = sessionInfo.getId();
        TFieldData tFieldData = fieldDataServiceI.getMaxFieldByCidUid(cid, uid);
        String maxProjectId = null;
        if (tFieldData != null) {
            maxProjectId = tFieldData.getProjectName();
        }
        request.setAttribute("maxProjectId", maxProjectId);

        request.setAttribute("cost", cost);
        request.setAttribute("proid", proid);
        request.setAttribute("proName", proName);
        return "/app/fielddata/addMaterialData";
    }

    @RequestMapping("securi_getCostInfo")
    @ResponseBody
    public Json getCostInfo(String nid, HttpServletResponse response, HttpServletRequest request) {
        Json json = new Json();
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        Cost cost = costServiceI.findOneView(nid, sessionInfo.getCompid());
        json.setObj(cost);
        json.setSuccess(true);
        return json;

    }
    /**
     * 保存
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/savefieldData")
    public Json saveFieldData(TFieldData fieldData, HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        Json j = new Json();
        SessionInfo sessionInfo = (SessionInfo) request.getSession()
                .getAttribute(ConfigUtil.getSessionInfoName());
        fieldData.setUid(sessionInfo.getId());
        fieldData.setCid(sessionInfo.getCompid());
        // modify by heyh
        fieldData.setUname((sessionInfo.getName() != null && !sessionInfo.getName().equals("")) ? sessionInfo.getName() : sessionInfo.getUsername());
        fieldData.setCompany(sessionInfo.getCompName());

        if (fieldDataServiceI.hasSameFieldData(fieldData)) {
            j.setMsg("系统已有相同数据，不能重复填加!");
            return j;
        }

        // costType 代替 nid
        if (fieldData.getItemCode() == null || fieldData.getItemCode().equals("")) {
            Cost cost = costServiceI.findOneView(fieldData.getCostType(), sessionInfo.getCompid());
            fieldData.setCostType(String.valueOf(cost.getId()));
            fieldData.setItemCode(cost.getItemCode());
        }

        // add by heyh begin
//        if (fieldData.getApprovedUser() == null || fieldData.getApprovedUser().equals("")) {
//            List<Integer> approvedUserList = new ArrayList<Integer>();
//            if (fieldData.getNeedApproved().equals("1")) {
//                approvedUserList = departmentService.getAllParents(fieldData.getCid(), Integer.parseInt(fieldData.getUid()));
//                if (approvedUserList == null || approvedUserList.size() == 0) {
//                    approvedUserList.add(Integer.parseInt(fieldData.getUid())); // 如果为空说明是超级管理员，自己审批
//                }
//                fieldData.setApprovedUser(StringUtils.join(approvedUserList, ",")); // 所有审批人
//                fieldData.setCurrentApprovedUser(String.valueOf(approvedUserList.get(0))); // 当前审批人
//
//            }
//        } else {
//            List<String> approvedUsers = Arrays.asList(fieldData.getApprovedUser().split(","));
//            if (approvedUsers != null && approvedUsers.size() > 0) {
//                fieldData.setCurrentApprovedUser(approvedUsers.get(0)); // 当前审批人
//            }
//        }

        if (fieldData.getNeedApproved().equals("1")) {
            fieldData.setApprovedUser(fieldData.getCurrentApprovedUser());
        }
        // add by heyh end

        try {
            Cost tem = costServiceI.findById(fieldData.getCostType());
            String fj = tem.getItemCode().substring(0, 3);

            fieldDataServiceI.add(fieldData);
            j.setObj(fieldDataServiceI.getId(fieldData));
            sessionInfo.setLast_cost_id(fieldData.getCostType());
            sessionInfo.setLast_project_id(fieldData.getProjectName());
            j.setSuccess(true);
            j.setMsg("现场数据添加成功！");
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 修改
     *
     * @param fieldData
     * @param session
     * @return
     */
    @RequestMapping("/updatefieldData")
    @ResponseBody
    public Json updateFieldData(TFieldData fieldData, HttpSession session) {
        Json j = new Json();
        try {
            Cost tem = costServiceI.findById(fieldData.getCostType());

            if (!Constant.isSameDate(fieldData.getCreatTime(), new Date())) {
                j.setMsg("只能修改当天录入的信息!!");
                return j;
            }
//            String itemCode = tem.getItemCode();
//            if (!itemCode.substring(0, 3).equals("000") && Integer.parseInt(itemCode.substring(0, 3)) <= 900) {
//                Double.parseDouble(fieldData.getPrice());
//                Integer.parseInt(fieldData.getCount());
//            }
//			if (!"纯附件".equals(tem.getCostType())) {
//				Double.parseDouble(fieldData.getPrice());
//				Integer.parseInt(fieldData.getCount());
//			}

            // add by heyh begin 修改后重新设置审批状态和当前审批人
            if (fieldData.getNeedApproved() != null && !fieldData.getNeedApproved().equals("0")) {
                fieldData.setNeedApproved("1");
                List<Integer> approvedUserList = departmentService.getAllParents(fieldData.getCid(), Integer.parseInt(fieldData.getUid()));
                if (approvedUserList == null) {
                    approvedUserList.add(Integer.parseInt(fieldData.getUid())); // 如果为空说明是超级管理员，自己审批
                }
                fieldData.setApprovedUser(StringUtils.join(approvedUserList, ",")); // 所有审批人
                if (approvedUserList != null && approvedUserList.size()>0) {
                    fieldData.setCurrentApprovedUser(String.valueOf(approvedUserList.get(0))); // 当前审批人
                }
            }
            // add by heyh end

            fieldDataServiceI.update(fieldData);
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
    @RequestMapping("/delfieldData")
    @ResponseBody
    public Json delCost(String id) {
        Json j = new Json();
        try {
            fieldDataServiceI.delete(id);
            j.setSuccess(true);
            j.setMsg("操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除
     *
     * @param request
     * @return
     */
    @RequestMapping("/securi_bunchdelete")
    @ResponseBody
    public Json bunchdelete(HttpServletRequest request) {
        Json j = new Json();
        String ids = request.getParameter("ids");
        try {
            String[] id = ids.split(",");
            for (String tem : id) {
                fieldDataServiceI.delete(tem);
            }
            j.setSuccess(true);
            j.setMsg("操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @RequestMapping("/securi_filedelete")
    @ResponseBody
    public Json filedelete(String id) {
        Json j = new Json();
        try {
            gcpoServiceI.deleteOne(Integer.parseInt(id));
            j.setSuccess(true);
            j.setMsg("操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 跳转至批量下载附件
     *
     * @param request
     * @return
     */
    @RequestMapping("/securi_downloadfile")
    public String downloadfile(String mpid, HttpServletRequest request) {
        System.out.println(mpid);
        request.setAttribute("mpid", mpid);
        return "/app/fielddata/download";
    }

    /**
     * 文件上传
     *
     * @param session
     * @param req
     * @param rt
     * @return
     */
    @RequestMapping("/upload")
    @ResponseBody
    public Json uploadhb(HttpSession session, HttpServletRequest req,
                         MultipartHttpServletRequest rt) {
        Json j = new Json();
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil
                .getSessionInfoName());
        String userPath = sessionInfo.getId() + "/";
//		String mid = rt.getParameter("id");// 关联ID;
        boolean isWebUploader = rt.getParameter("updateType") != null && rt.getParameter("updateType").equals("webuploader");
        String mid = isWebUploader ? rt.getParameter("mid") : rt.getParameter("id");
        if (mid == null || mid.length() == 0 || mid.equals("undefined")) {
            j.setCode(000);
            j.setSuccess(false);
            j.setMsg("mid is need not null");
            return j;
        }
        long count = gcpoServiceI.getFieldCount(mid);
        if (count >= 10) {
            j.setSuccess(false);
            j.setMsg("该条现场记录的附件数量过多,限制上传");
            return j;
        }
        try {
//			GetRealPath grp = new GetRealPath(req.getSession()
//					.getServletContext());
            // modify by heyh begin
            String file_path = PropertyUtil.getFileRealPath() + "/upload/" + Constant.SOURCE + userPath;
            // end
            MultipartFile patch = isWebUploader ? rt.getFile("file") : rt.getFile(req.getParameter("name"));// 获取文件
            String fileName = patch.getOriginalFilename();// 得到文件名
            if (!patch.isEmpty()) {
                File saveDir = new File(file_path);
                if (!saveDir.exists())
                    saveDir.mkdirs();
                String reg = fileName.substring(
                        patch.getOriginalFilename().lastIndexOf(".") + 1)
                        .toLowerCase();
                int status = Constant.fileStatus(reg);
                if (status == -1) {
                    j.setSuccess(false);
                    j.setMsg("上传的文件格式不支持");
                    return j;
                }
                // modify by heyh begin
//				String finalname = fileName.substring(0, patch.getOriginalFilename().lastIndexOf(".")) + "-" + DateKit.getCurrentDate("yyyyMMddHHmmssSSS");
                String finalname = UUID.randomUUID().toString();
                // end
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
//					Constant.copyFile(f, new File(pdfFilePath + finalname + "." + reg));
                    FileUtils.copyFileToDirectory(f, new File(pdfFilePath));
                    gcpo.setStatus(Constant.PDF2SWF_STATUS);
                    gcpo.setPdfFilePath(userPath + finalname + "." + reg);
                }
                gcpoServiceI.add(gcpo);
                j.setMsg(fileName + "上传成功");
                j.setObj(gcpo.getId());
                j.setCode(2000);
                j.setSuccess(true);
                return j;
            } else {
                j.setCode(1004);
                j.setObj(null);
                j.setSuccess(false);
                j.setMsg("上传的文件不存在");
                return j;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            j.setCode(1005);
            j.setObj(null);
            j.setSuccess(false);
            j.setMsg("上传异常:" + ex.getMessage());
            return j;
        }
    }

    /* 后台导出到excel */
    @RequestMapping("/securi_execl")
    public void OutputToExcel(FieldData fieldData, PageHelper ph,
                              HttpServletResponse response, HttpServletRequest request) {
        response.setContentType("text/html;charset=utf8");
        String source = request.getParameter("source");
        String keyword = request.getParameter("keyword");
        HttpSession session = request.getSession();

        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil
                .getSessionInfoName());
        List<Integer> ugroup = sessionInfo.getUgroup();
        ph.setRows(999999999);

//		try {
//			fieldData.setUname(new String(request.getParameter("uname")
//					.getBytes("iso-8859-1"), "utf-8"));
//			fieldData.setProjectName(new String(request.getParameter(
//					"projectName").getBytes("iso-8859-1"), "utf-8"));
//			fieldData.setCostType(new String(request.getParameter("costType")
//					.getBytes("iso-8859-1"), "utf-8"));
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//		}

        fieldData.setUname(request.getParameter("uname"));
        fieldData.setProjectName(request.getParameter("projectName"));
        fieldData.setCostType(request.getParameter("costType"));

        List<FieldData> datas = fieldDataServiceI.dataGrid(fieldData, ph, ugroup, source, keyword).getRows();

        List<Map<String, Object>> map = createExcelRecord(datas);

        // 填充projects数据
        String[] columnNames = {"工程名称", "费用类型", "现场数据名称", "单价", "数量", "金额",
                "单位", "规格", "操作人", "入库时间"};// 列名
        String[] keys = {"project_name", "costType_name", "name", "price",
                "count", "money", "unit", "specifications", "uname",
                "createTime"};// map中的key

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
    private List<Map<String, Object>> createExcelRecord(List<FieldData> list) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "data.xls");
        listmap.add(map);
        double count = 0;

        for (int i = 0; i < list.size(); i++) {
            FieldData tem = list.get(i);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("project_name", tem.getProjectName());
            mapValue.put("costType_name", tem.getCostType());
            mapValue.put("name", tem.getDataName());
            mapValue.put("price", tem.getPrice());
            mapValue.put("count", tem.getCount());
            double result = (tem.getCount() == null || tem.getCount().equals("") || tem.getPrice() == null || tem.getPrice().equals("") ) ? 0.00 : Double.parseDouble(tem.getCount()) * Double.parseDouble(tem.getPrice());
            count += result;
            int result2 = (int) (result * 100);
            mapValue.put("money", result2 / 100.0);
            mapValue.put("unit", tem.getUnit());
            mapValue.put("specifications", tem.getSpecifications());
            mapValue.put("uname", tem.getUname());
            mapValue.put("createTime", tem.getCreatTime());
            listmap.add(mapValue);
        }
        Map<String, Object> mapValue = new HashMap<String, Object>();
        int result2 = (int) (count * 100);
        mapValue.put("project_name", "合计");
        mapValue.put("money", result2 / 100.0);
        listmap.add(mapValue);
        return listmap;
    }

    @RequestMapping("/securi_approvedField")
    @ResponseBody
    public Json approvedField(Integer id, String approvedState, String approvedOption, String currentApprovedUser, HttpServletResponse response, HttpServletRequest request) {
        Json j = new Json();
        if (id != null) {
            fieldDataServiceI.approvedField(id, approvedState, approvedOption, currentApprovedUser);
        }
        j.setMsg("审批成功！");
        j.setSuccess(true);
        return j;
    }

    @RequestMapping("securi_getNeedApproveList")
    @ResponseBody
    public Json getNeedApproveList(@RequestParam(value = "currentApprovedUser", required = true) String currentApprovedUser,
                                   @RequestParam(value = "cid", required = true) String cid,
                                   HttpServletResponse response, HttpServletRequest request) {

        Json json = new Json();
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        List<TFieldData> needApproveList = new ArrayList<TFieldData>();
        try {
            needApproveList = fieldDataServiceI.getNeedApproveList(currentApprovedUser);
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
        } catch (Exception e) {

        }

        // 后加的功能
        Company c = companyService.findOneView(currentApprovedUser,cid);
        User u = userService.getUser(currentApprovedUser);
        String compName = "";
        String userName = "";
        if (c != null && u != null) {
            compName = c.getName();
            userName = u.getRealname() == null || u.getRealname().equals("") ? u.getUsername() : u.getRealname();
        }

        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("compName", compName);
        rtnMap.put("userName", userName);

        rtnMap.put("needApproveList", needApproveList);
        json.setObj(rtnMap);
        json.setSuccess(true);
        return json;
    }

//    @RequestMapping("securi_chooseApprove")
//    public String chooseApprove() {
//        return "/app/fielddata/chooseApprove";
//    }

    @RequestMapping("securi_chooseApprove")
    @ResponseBody
    public Json chooseApprove(HttpServletResponse response, HttpServletRequest request) {
        Json json = new Json();
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String uid = sessionInfo.getId();
        String cid = sessionInfo.getCompid();
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
        json.setObj(userList);
        json.setSuccess(true);
        return json;
    }

    @RequestMapping("/securi_chooseApprovePage")
    public String chooseApprovePage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String uid = sessionInfo.getId();
        String cid = sessionInfo.getCompid();
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
        request.setAttribute("id", request.getParameter("id"));
        request.setAttribute("userList", userList);
        return "/app/pro/chooseApprove";
    }

    @RequestMapping("securi_getParentNodes")
    @ResponseBody
    public Json getParentNodes(HttpServletResponse response, HttpServletRequest request) {
        Json json = new Json();
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String uid = sessionInfo.getId();
        String cid = sessionInfo.getCompid();
        List<Node> parentNodeList = departmentService.getParentNodes(cid, Integer.parseInt(uid));
        json.setObj(NodeUtil.transJson(NodeUtil.buildListToTree(parentNodeList)));
        json.setSuccess(true);
        return json;

    }

    @RequestMapping("/myApproveShow")
    public String myApproveShow(HttpServletRequest req) {
        req.setAttribute("first", UtilDate.getshortFirst());
        req.setAttribute("last", UtilDate.getshortLast());
        return "/app/fielddata/myApproveShow";
    }

    @RequestMapping("/securi_myApproveDataGrid")
    @ResponseBody
    public DataGrid securi_myApproveDataGrid(PageHelper ph, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
        String uid = sessionInfo.getId();
        DataGrid dataGrid = fieldDataServiceI.myApproveDataGrid(ph, uid);

        // add by heyh begin
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

        return dataGrid;
    }

    @RequestMapping("/securi_cloudProjects")
    public String cloudProjects(HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession()
                .getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();
        String uid = sessionInfo.getId();
        TFieldData tFieldData = fieldDataServiceI.getMaxFieldByCidUid(cid, uid);
        String maxProjectId = null;
        if (tFieldData != null) {
            maxProjectId = tFieldData.getProjectName();
        }
        request.setAttribute("maxProjectId", maxProjectId);

        request.setAttribute("first", UtilDate.getshortFirst());
        request.setAttribute("last", UtilDate.getshortLast());
        return "/app/fielddata/zjsproject_select";
    }

    @RequestMapping("/securi_zjsprojectTreeGrid")
    @ResponseBody
    public DataGrid zjsprojectTreeGrid(PageHelper ph, HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession()
                .getAttribute(ConfigUtil.getSessionInfoName());
        String keyword = StringUtil.trimToEmpty(request.getParameter("keyword"));

        List<Integer> ugroup = sessionInfo.getUgroup();

        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        if (null == startTime && null == startTime) {
            startTime = UtilDate.getshortFirst();
            endTime = UtilDate.getshortLast();
        }

        String pid = StringUtil.trimToEmpty(request.getParameter("id")).equals("") ? "0" : StringUtil.trimToEmpty(request.getParameter("id"));

        DataGrid jswZjsprojectList =  jswZjsprojectService.dataGrid(ph, ugroup, keyword, startTime, endTime, pid);

        return jswZjsprojectList;
    }

    @RequestMapping("/securi_getZjsprojectTreeGridChild")
    @ResponseBody
    public List<JswZjsproject> getZjsprojectTreeGridChild(PageHelper ph, HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession()
                .getAttribute(ConfigUtil.getSessionInfoName());
        String keyword = StringUtil.trimToEmpty(request.getParameter("keyword"));

        List<Integer> ugroup = sessionInfo.getUgroup();

        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        if (null == startTime && null == startTime) {
            startTime = UtilDate.getshortFirst();
            endTime = UtilDate.getshortLast();
        }

        String pid = StringUtil.trimToEmpty(request.getParameter("id")).equals("") ? "0" : StringUtil.trimToEmpty(request.getParameter("id"));

        JSONArray _zjsprojectList = new JSONArray();
        JSONObject _zjsproject = new JSONObject();
        DataGrid jswZjsprojectList =  jswZjsprojectService.dataGrid(ph, ugroup, keyword, startTime, endTime, pid);

        return jswZjsprojectList.getRows();
    }


    @RequestMapping("/securi_cloudImport")
    @ResponseBody
    public Json cloudImport(@RequestParam(value = "proId", required = false) String proId,
                            @RequestParam(value = "fileName", required = false) String fileName,
                            @RequestParam(value = "largeCostType", required = false) String largeCostType,
                            @RequestParam(value = "section", required = false) String section,
                            @RequestParam(value = "userId", required = false) String userId,
                            @RequestParam(value = "dataName", required = false) String dataName,
                            HttpServletRequest request) {
        Json j = new Json();
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String uid = sessionInfo.getId();
        String cid = sessionInfo.getCompid();
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMddHHmmss");
        String strDate = sdf.format(new Date());

        try {
            // 路径: jsw/projectId/uid-section-yyyyMMddHHmmss
            String remoteBaseUrl = "http://114.55.55.167:8080/project/";
            String localBaseDir = PropertyUtil.getFileRealPath() + "/jsw/";
            String downloadDir = localBaseDir + "download/";
            String unzipDir = localBaseDir + "/" + proId + "/temp/";
            String strFileName = uid + "-" + section + "-" + strDate;
            String mdbPath = localBaseDir + "/" + proId + "/" + strFileName + ".mdb";

            File file = new File(unzipDir);
            File[] tempList = file.listFiles();
            if (tempList != null && tempList.length>0) {
                for (File tmpFile : tempList) {
                    tmpFile.delete();
                }
            }

            String filePath = ZipUtil.downloadRemoteFile(remoteBaseUrl, downloadDir , userId, fileName);
            File zipFile = new File(filePath);
            ZipUtil.unzip(zipFile, unzipDir, "njrxy_+7804");

            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            file = new File(unzipDir);
            tempList = file.listFiles();

            if (tempList == null) {
                j.setSuccess(true);
                j.setMsg("无氿上云工程数据,请联系管理员！");
                return j;
            }

            for (File temp : tempList) {
                if (temp.getName().endsWith("mdb")) {
//                if (temp.getName().substring(temp.getName().lastIndexOf(".") + 1).equals("mdb")) {
                    temp.renameTo(new File(mdbPath));
                    break;
                }
            }

            List<Map<String, Object>> summaryBLInfos = SqliteHelper.query(mdbPath, "select ID,VARNAME,VARRESULT from SUMMARYBL");
            List<Map<String, Object>> summaryInfos = SqliteHelper.query(mdbPath, "select ID,FEENAME VARNAME,FEERESULT VARRESULT, ISSHOW from SUMMARY");
            summaryBLInfos.addAll(summaryInfos);
            Collections.reverse(summaryBLInfos);

            for (Map<String, Object> summaryBLInfo1 : summaryBLInfos) {
                // 人工费=人工费+人差
                if (summaryBLInfo1.get("VARNAME").equals("人工费")) {
                    for (Map<String, Object> summaryBLInfo2 : summaryBLInfos){
                        if (summaryBLInfo2.get("VARNAME").equals("人差")) {
                            String varResult1 = StringUtil.trimToEmpty(summaryBLInfo1.get("VARRESULT"));
                            String varResult2 = StringUtil.trimToEmpty(summaryBLInfo2.get("VARRESULT"));
                            summaryBLInfo1.put("VARRESULT", Double.parseDouble(varResult1) + Double.parseDouble(varResult2));
                        }
                    }
                }

                // 材料费=材料费+材差+商品砼
                if (summaryBLInfo1.get("VARNAME").equals("材料费")) {
                    for (Map<String, Object> summaryBLInfo2 : summaryBLInfos){
                        if (summaryBLInfo2.get("VARNAME").equals("材差") || summaryBLInfo1.get("VARNAME").equals("商品砼")) {
                            String varResult1 = StringUtil.trimToEmpty(summaryBLInfo1.get("VARRESULT"));
                            String varResult2 = StringUtil.trimToEmpty(summaryBLInfo2.get("VARRESULT"));
                            summaryBLInfo1.put("VARRESULT", Double.parseDouble(varResult1) + Double.parseDouble(varResult2));
                        }

                    }
                }

                // 机械费=机械费+机差
                if (summaryBLInfo1.get("VARNAME").equals("机械费")) {
                    for (Map<String, Object> summaryBLInfo2 : summaryBLInfos){
                        if (summaryBLInfo2.get("VARNAME").equals("机差")) {
                            String varResult1 = StringUtil.trimToEmpty(summaryBLInfo1.get("VARRESULT"));
                            String varResult2 = StringUtil.trimToEmpty(summaryBLInfo2.get("VARRESULT"));
                            summaryBLInfo1.put("VARRESULT", Double.parseDouble(varResult1) + Double.parseDouble(varResult2));
                        }
                    }
                }
            }


            List<ParamTrans> costTransInfos = paramTransService.getParamTransListByCid(cid, "costTrans");
            if (costTransInfos.size() <= 0) {
                costTransInfos = paramTransService.getParamTransListByCid("-1", "costTrans");
            }
            TFieldData fieldData = new TFieldData();
            for (Map<String, Object> summaryBLInfo : summaryBLInfos) {
                String price = StringUtil.trimToEmpty(summaryBLInfo.get("VARRESULT"));
                if (price == null || price.equals("") || price.equals("0")) {
                    continue;
                }

                String jswCostName = StringUtil.trimToEmpty(summaryBLInfo.get("VARNAME"));
                String isShow = StringUtil.trimToEmpty(summaryBLInfo.get("ISSHOW"));
                for (ParamTrans costTransInfo : costTransInfos) {
                    if (costTransInfo.getParamName().equals(jswCostName) || (costTransInfo.getParamName().equals("合计") && isShow.equals("1")) ) {

                        String itemCode = costTransInfo.getTransParamCode();
                        itemCode = largeCostType + itemCode.substring(3); // 替换费用类别
                        Cost cost = costServiceI.getCostByCode(itemCode, sessionInfo.getCompid());
                        if (cost == null) {
                            continue;
                        }

                        int costId = cost.getId();
//                        if (dataName == null || dataName.equals("")) {
//                            dataName = cost.getCostType();
//                        }

                        fieldData.setUid(uid);
                        fieldData.setCid(cid);
                        fieldData.setUname((sessionInfo.getName() != null && !sessionInfo.getName().equals("")) ? sessionInfo.getName() : sessionInfo.getUsername());
                        fieldData.setCompany(sessionInfo.getCompName());
                        fieldData.setProjectName(proId);
                        fieldData.setDataName(dataName + "-" + cost.getCostType());
                        fieldData.setPrice(StringUtil.trimToEmpty(summaryBLInfo.get("VARRESULT")));
                        fieldData.setCount("1");
                        fieldData.setUnit("元");
                        fieldData.setSpecifications("");
                        fieldData.setRemark("氿上云导入-" + strFileName);
                        fieldData.setItemCode(itemCode);
                        fieldData.setCostType(StringUtil.trimToEmpty(costId));
                        fieldData.setNeedApproved("0");
                        fieldData.setApprovedUser("");
                        fieldData.setCurrentApprovedUser("");
                        fieldData.setApprovedOption("");
                        fieldData.setSection(section);
                        if (fieldDataServiceI.hasSameFieldData(fieldData)) {
                            continue;
                        }
                        fieldDataServiceI.add(fieldData);
                    }
                }
            }

            j.setSuccess(true);
            j.setMsg("导入成功！");
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @RequestMapping("/securi_execlProjects")
    public String execlProjects(HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String uid = sessionInfo.getId();

        String defaultTemplatePath = PropertyUtil.getFileRealPath() + "/template" + "/projectTemplate.xls";
        String templatePath = PropertyUtil.getFileRealPath() + "/template/" + uid + "/projectTemplate.xls";
        File file = new File(templatePath);
        if (!file.exists()) {
            File dir = new File(file.getParent());
            dir.mkdir();
            FileUtil.copyFile(defaultTemplatePath, templatePath);
        }

        String cid = sessionInfo.getCompid();
        TFieldData tFieldData = fieldDataServiceI.getMaxFieldByCidUid(cid, uid);
        String maxProjectId = null;
        if (tFieldData != null) {
            maxProjectId = tFieldData.getProjectName();
        }
        request.setAttribute("maxProjectId", maxProjectId);

        return "/app/fielddata/execl_select";
    }

    /**
     * 下载模版
     */
    @RequestMapping(value="/securi_downExcel")
    public void downExcel(HttpServletResponse response)throws Exception{
        FileDownload.fileDownload(response, PathUtil.getClasspath() + Constant.EXCEL_TEMPLATE + "projectTemplate.xls", "projectTemplate.xls");
//        FileDownload.fileDownload(response, PropertyUtil.getFileRealPath() + Constant.EXCEL_TEMPLATE + "projectTemplate.xls", "projectTemplate.xls");

    }

    @RequestMapping("/securi_execlImport")
    @ResponseBody
    public Json execlImport(@RequestParam(value = "excel", required = false) MultipartFile file,
                            @RequestParam(value = "projectName", required = false) String projectName,
                            @RequestParam(value = "largeCostType", required = false) String largeCostType,
                            @RequestParam(value = "section", required = false) String section,
                            @RequestParam(value = "dataName", required = false) String dataName,
                            HttpServletRequest request) {
        Json j = new Json();
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String uid = sessionInfo.getId();
        String cid = sessionInfo.getCompid();

        if (null == file && file.isEmpty()) {
            return j;
        }
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMddHHmmss");
        String strDate = sdf.format(new Date());

        String strFileName = uid + "-" + section + "-" + strDate;
        String filePath = PropertyUtil.getFileRealPath() + "/excel/" + projectName + "/"; //文件上传路径
        String fileName = FileUpload.fileUp(file, filePath, strFileName); //执行上传

        List<Map<String, Object>> excelInfos1 =  ObjectExcelRead.readExcel(filePath, fileName, 1, 0, 0); //执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet

        List<ParamTrans> costTransInfos = paramTransService.getParamTransListByCid(cid, "execlCostTrans");
        if (costTransInfos.size() <= 0) {
            costTransInfos = paramTransService.getParamTransListByCid("-1", "execlCostTrans");
        }

        TFieldData fieldData = new TFieldData();

        for (Map<String, Object> excelInfo1 : excelInfos1) {
            String price = StringUtil.trimToEmpty(excelInfo1.get("var2"));
            if (price == null || price.equals("") || price.equals("0")) {
                continue;
            }

            // 插入 mysql
            String aliasCostCode = StringUtil.trimToEmpty(excelInfo1.get("var0"));
            for (ParamTrans costTransInfo : costTransInfos) {
                if (costTransInfo.getParamCode().equals(aliasCostCode)) {

                    String itemCode = costTransInfo.getTransParamCode();
                    itemCode = largeCostType + itemCode.substring(3); // 替换费用类别
                    Cost cost = costServiceI.getCostByCode(itemCode, sessionInfo.getCompid());
                    if (cost == null) {
                        continue;
                    }

                    int costId = cost.getId();
//                    if (dataName == null || dataName.equals("")) {
//                        dataName = cost.getCostType();
//                    }

                    fieldData.setUid(uid);
                    fieldData.setCid(cid);
                    fieldData.setUname((sessionInfo.getName() != null && !sessionInfo.getName().equals("")) ? sessionInfo.getName() : sessionInfo.getUsername());
                    fieldData.setCompany(sessionInfo.getCompName());
                    fieldData.setProjectName(projectName);
                    fieldData.setDataName(dataName + "-" + cost.getCostType());
                    fieldData.setPrice(price);
                    fieldData.setCount("1");
                    fieldData.setUnit("元");
                    fieldData.setSpecifications("");
                    fieldData.setRemark("execl导入-" + strFileName);
                    fieldData.setItemCode(itemCode);
                    fieldData.setCostType(StringUtil.trimToEmpty(costId));
                    fieldData.setNeedApproved("0");
                    fieldData.setApprovedUser("");
                    fieldData.setCurrentApprovedUser("");
                    fieldData.setApprovedOption("");
                    fieldData.setSection(section);
                    if (fieldDataServiceI.hasSameFieldData(fieldData)) {
                        continue;
                    }
                    fieldDataServiceI.add(fieldData);
                }
            }
        }
        String mdbPath = PropertyUtil.getFileRealPath() + "/excel/" + "template.mdb";
//        File templateFile = new File(mdbPath);
//        if (!templateFile.exists()) {
//            String newPath = PropertyUtil.getFileRealPath() + "/excel/" + "new.mdb";
//            File newFile = new File(newPath);
//            newFile.renameTo(templateFile);
//        }
//
//        String sql = "select name from sqlite_master where type='table' order by name";
//        List<Map<String, Object>> tables = SqliteHelper.query(mdbPath, sql);
//
//        for (Map<String, Object> table : tables) {
//            String delSql = "delete from " + table.get("name");
//            SqliteHelper.delete(mdbPath, delSql);
//
//            String qrySql = "select * from "+ table.get("name");
//            List<Map<String, Object>> list = SqliteHelper.query(mdbPath, qrySql);
//            System.out.println("list:" + list.size());
//            ;
//        }


        String templateDir = PropertyUtil.getFileRealPath() + "/excel/";
        String destDir = PropertyUtil.getFileRealPath() + "/excel/" + projectName + "/";
        FileUtil.createDir(destDir);
        FileUtil.copyFile(templateDir + "template.mdb", destDir + strFileName + ".mdb");

        // 插入 SUMMARYBL
        int id = 1;
        for (int i=2; i<excelInfos1.size(); i++) {
//            String id = StringUtil.trimToEmpty(excelInfos1.get(i).get("var0"));
            id++;
            String varName = StringUtil.trimToEmpty(excelInfos1.get(i).get("var1"));
            String varResult = StringUtil.trimToEmpty(excelInfos1.get(i).get("var2"));
            if (varResult == null || varResult.equals("") || varResult.equals("0")) {
                continue;
            }
            String insertSql = "insert into SUMMARYBL values (" + id + ",'" + varName + "'," + varResult + ",null,null,null,null,null)";
            SqliteHelper.insert(destDir + "/" + strFileName + ".mdb", insertSql);
        }

        // 工程数据表
        List<Map<String, Object>> excelInfos2 =  ObjectExcelRead.readExcel(filePath, fileName, 1, 0, 1); //执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
        for (int i=5; i<excelInfos2.size(); i++) {
            Map<String, Object> excelInfo = excelInfos2.get(i);
            String xh = StringUtil.trimToEmpty(excelInfo.get("var0"));
            // 标题的节点号=1，清单的节点号=5，子目的节点号=0
            // 清单的计价节点=true，标题和子目的基价节点=false
            String jdh = "";
            String jjjd = "";
            if (xh == null || xh.equals("")) { // 标题

                jdh = "1";
                jjjd = "1";

            } else {
                if (xh.contains("(") || xh.contains("<")) { // 清单
                    jdh = "5";
                } else { // 子目
                    jdh = "0";
                }
                jjjd = "0";
            }
            String deh = StringUtil.trimToEmpty(excelInfo.get("var1")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var1"));
            String zmmc = StringUtil.trimToEmpty(excelInfo.get("var2")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var2"));
            String mshxmtz = StringUtil.trimToEmpty(excelInfo.get("var3")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var3"));
            String zmdw = StringUtil.trimToEmpty(excelInfo.get("var4")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var4"));
            String gclz = StringUtil.trimToEmpty(excelInfo.get("var5")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var5"));
            String jjdj = StringUtil.trimToEmpty(excelInfo.get("var6")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var6"));
            String rgfdj = StringUtil.trimToEmpty(excelInfo.get("var7")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var7"));
            String clfdj = StringUtil.trimToEmpty(excelInfo.get("var8")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var8"));
            String jxfdj = StringUtil.trimToEmpty(excelInfo.get("var9")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var9"));
            String glf = StringUtil.trimToEmpty(excelInfo.get("var10")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var10"));
            String jhlrz = StringUtil.trimToEmpty(excelInfo.get("var11")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var11"));
            String jj = StringUtil.trimToEmpty(excelInfo.get("var12")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var12"));
            String insertSql = "insert into 工程数据表 values " +
                    "(null," + jdh + "," + jjjd +",null,null,null,null,null,null,\'" + deh + "\',\'" + zmmc + "\',\'" +
                    zmdw + "\',null,null,null,null," + gclz + ",null," + jjdj + "," + jj + ",null," + rgfdj + ",null," +
                    clfdj + ",null," + jxfdj + ",null,null,null,null,null,null," +
                    "null,null," + glf + "," + jhlrz + ",null,null,null,null,null,null," +
                    "null,null,null,null,null,null,null,null,null,null,null,null," +
                    "null,null,null,null,null,null,null,null,null,null,null," +
                    "null,null,null,null,null,null,null,null,null,null,null,null,\'" +
                    mshxmtz  + "\',null,null,null,null,null,null,null,null,null,null," +
                    "null,null,null,null,null,null,null,null,null,null," +
                    "null,null,null,null,null,null,null,null,null,null,null,null,null," +
                    "null,null,null,null,null,null,null,null,null,null,null,null," +
                    "null,null,null,null,null,null,null,null,null,null)";

            SqliteHelper.insert(destDir + "/" + strFileName + ".mdb", insertSql);
        }

        // 单价措施 CS2
        List<Map<String, Object>> excelInfos3 =  ObjectExcelRead.readExcel(filePath, fileName, 1, 0, 2); //执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
        for (int i=5; i<excelInfos3.size(); i++) {
            Map<String, Object> excelInfo = excelInfos3.get(i);
            String xh = StringUtil.trimToEmpty(excelInfo.get("var0"));
            // 标题的节点号=1，清单的节点号=5，子目的节点号=0
            // 清单的计价节点=true，标题和子目的基价节点=false
            String jdh = "";
            String jjjd = "";
            if (xh == null || xh.equals("")) { // 标题

                jdh = "1";
                jjjd = "1";

            } else {
                if (xh.contains("(") || xh.contains("<")) { // 清单
                    jdh = "5";
                } else { // 子目
                    jdh = "0";
                }
                jjjd = "0";
            }
            String deh = StringUtil.trimToEmpty(excelInfo.get("var1")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var1"));
            String zmmc = StringUtil.trimToEmpty(excelInfo.get("var2")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var2"));
            String mshxmtz = StringUtil.trimToEmpty(excelInfo.get("var3")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var3"));
            String zmdw = StringUtil.trimToEmpty(excelInfo.get("var4")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var4"));
            String gclz = StringUtil.trimToEmpty(excelInfo.get("var5")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var5"));
            String jjdj = StringUtil.trimToEmpty(excelInfo.get("var6")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var6"));
            String rgfdj = StringUtil.trimToEmpty(excelInfo.get("var7")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var7"));
            String clfdj = StringUtil.trimToEmpty(excelInfo.get("var8")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var8"));
            String jxfdj = StringUtil.trimToEmpty(excelInfo.get("var9")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var9"));
            String glf = StringUtil.trimToEmpty(excelInfo.get("var10")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var10"));
            String jhlrz = StringUtil.trimToEmpty(excelInfo.get("var11")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var11"));
            String jj = StringUtil.trimToEmpty(excelInfo.get("var12")).equals("") ? null : StringUtil.trimToEmpty(excelInfo.get("var12"));
            String insertSql = "insert into CS2 values " +
                    "(null," + jdh + "," + jjjd +",null,null,null,null,null,null,\'" + deh + "\',\'" + zmmc + "\',\'" +
                    zmdw + "\',null,null,null,null," + gclz + ",null," + jjdj + "," + jj + ",null," + rgfdj + ",null," +
                    clfdj + ",null," + jxfdj + ",null,null,null,null,null,null," +
                    "null,null," + glf + "," + jhlrz + ",null,null,null,null,null,null," +
                    "null,null,null,null,null,null,null,null,null,null,null,null," +
                    "null,null,null,null,null,null,null,null,null,null,null," +
                    "null,null,null,null,null,null,null,null,null,null,null,null,\'" +
                    mshxmtz  + "\',null,null,null,null,null,null,null,null,null,null," +
                    "null,null,null,null,null,null,null,null,null,null," +
                    "null,null,null,null,null,null,null,null,null,null,null,null,null," +
                    "null,null,null,null,null,null,null,null,null,null,null,null," +
                    "null,null,null,null,null,null,null,null,null,null)";

            SqliteHelper.insert(destDir + "/" + strFileName + ".mdb", insertSql);
        }

        // 总价措施 CS1
        List<Map<String, Object>> excelInfos4 =  ObjectExcelRead.readExcel(filePath, fileName, 1, 0, 3); //执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
        id = 1;
        for (int i=2; i<excelInfos4.size(); i++) {
            id++;
            String xh = StringUtil.trimToEmpty(excelInfos4.get(i).get("var0"));
            String nodeId = xh.matches("[0-9]+") ? "5" : "0";
            String nodeState = "1";
            String nodeCellOffSet = xh.matches("[0-9]+") ? "0" : "15";
            String nodeCalculate = xh.matches("[0-9]+") ? "1" : "0";
//            String bh = StringUtil.trimToEmpty(excelInfos4.get(i).get("var1"));
            String name = StringUtil.trimToEmpty(excelInfos4.get(i).get("var2"));
            String dw = null;
            String jsgs = null;
            String base = StringUtil.trimToEmpty(excelInfos4.get(i).get("var3"));
            String fl = StringUtil.trimToEmpty(excelInfos4.get(i).get("var4"));
            String je = StringUtil.trimToEmpty(excelInfos4.get(i).get("var5"));
            String flfw = null;
            String isShow = "0";
            String type = null;
            String bh = "0";
            String jg = "0";
            String wmf = "0";
            String isVisible = "0";
            String insertSql = "insert into CS1 values (" + id + "," + nodeId + "," + nodeState + "," +
                    nodeCellOffSet + "," + nodeCalculate + "," + xh + ",\'" + name + "\'," + dw + "," +
                    jsgs + ",\'" + base + "\',\'" + fl + "\',\'" + je + "\'," + flfw + "," + isShow + "," +
                    type + "," + bh + "," + jg + "," + wmf + "," + isVisible + ")";
            SqliteHelper.insert(destDir + "/" + strFileName + ".mdb", insertSql);
        }

        // 其他项目
        List<Map<String, Object>> excelInfos5 =  ObjectExcelRead.readExcel(filePath, fileName, 1, 0, 4); //执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
        int guid = 1;
        for (int i=2; i<excelInfos5.size();i++) {
            guid++;
            String strId = StringUtil.trimToEmpty(excelInfos5.get(i).get("var0"));
            String name = StringUtil.trimToEmpty(excelInfos5.get(i).get("var1"));
            String dw = StringUtil.trimToEmpty(excelInfos5.get(i).get("var2"));
            String je = StringUtil.trimToEmpty(excelInfos5.get(i).get("var3"));
            String bz = StringUtil.trimToEmpty(excelInfos5.get(i).get("var4"));
            String nodeId = strId.matches("[0-9]+") ? "5" : "0";
            String nodeState = "1";
            String nodeCellOffSet = strId.matches("[0-9]+") ? "0" : "15";
            String nodeCalculate = strId.matches("[0-9]+") ? "1" : "0";
            String jsgs = strId.matches("[0-9]+") ? name : "";
            String js = "";
            String fl = "100";
            String type = "1";
            String jg = "1";
            String jjjd = strId.matches("[0-9]+") ? "1" : "0";
            String lb = strId.matches("[0-9]+") ? "1" : "4";
            String insertSql = "insert into QT values (" + guid + "," + strId +  "," +
                    nodeId + "," + nodeState + "," +  nodeCellOffSet + "," + nodeCalculate + ",\'" +
                    name + "','" + dw + "','" + jsgs + "','" + js + "','" + fl + "','" + je + "','" + bz + "','" +
                    type + "','" + jg + "','" + jjjd + "','" + lb + "')";
            SqliteHelper.insert(destDir + "/" + strFileName + ".mdb", insertSql);
        }

        // 工料机汇总表
        List<Map<String, Object>> excelInfos6 =  ObjectExcelRead.readExcel(filePath, fileName, 1, 0, 5); //执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
        for (int i=2; i<excelInfos6.size(); i++) {
            String xh = StringUtil.trimToEmpty(excelInfos6.get(i).get("var0"));
            String bm = StringUtil.trimToEmpty(excelInfos6.get(i).get("var1"));
            String name = StringUtil.trimToEmpty(excelInfos6.get(i).get("var2"));
            String dw = StringUtil.trimToEmpty(excelInfos6.get(i).get("var3"));
            String sl = StringUtil.trimToEmpty(excelInfos6.get(i).get("var4"));
            String dj = StringUtil.trimToEmpty(excelInfos6.get(i).get("var5"));
            String je = StringUtil.trimToEmpty(excelInfos6.get(i).get("var6"));
            String insertSql = "insert into 工程工料机表 values(" + xh + ",null,null,'" + bm + "','" + name + "','" +
                    dw + "'," + "null,null,null,null,null,null,null,null,null,'" + sl + "','" + dj + "',null,null,'" + je + "',null,null,null,null,null,null,null,null,null,null,null,null,null)";
            SqliteHelper.insert(destDir + "/" + strFileName + ".mdb", insertSql);

        }

        j.setSuccess(true);
        j.setMsg("导入成功！");
        return j;
    }


    @RequestMapping("/securi_backFillPage")
    public String backFillPage(HttpServletRequest request) {
        request.setAttribute("first", UtilDate.getshortFirst());
        request.setAttribute("last", UtilDate.getshortLast());
        return "/app/fielddata/backFill";
    }

    @RequestMapping("/securi_backFill")
    @ResponseBody
    public Json backFill(@RequestParam(value = "projectId", required = false) String projectId,
                         @RequestParam(value = "fileName", required = false) String fileName,
                         @RequestParam(value = "userId", required = false) String userId,
                         @RequestParam(value = "feeType", required = false) String feeType,
                         HttpServletRequest request) {
        Json j = new Json();
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String uid = sessionInfo.getId();
        String cid = sessionInfo.getCompid();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String strDate = sdf.format(new Date());

        try {
            // 路径: jsw/projectId/uid-section-yyyyMMddHHmmss
            String remoteBaseUrl = "http://114.55.55.167:8080/project/" ;
            String localBaseDir = PropertyUtil.getFileRealPath() + "/jsw/" ;
            String downloadDir = localBaseDir + "download/" ;
            String unzipDir = localBaseDir + "/" + projectId + "/temp/" ;
            String strFileName = uid + "-" + strDate;
            String mdbPath = localBaseDir + "/" + projectId + "/" + strFileName + ".mdb" ;

            File file = new File(unzipDir);
            File[] tempList = file.listFiles();
            if (tempList != null && tempList.length > 0) {
                for (File tmpFile : tempList) {
                    tmpFile.delete();
                }
            }

            String filePath = ZipUtil.downloadRemoteFile(remoteBaseUrl, downloadDir, userId, fileName);
            File zipFile = new File(filePath);
            ZipUtil.unzip(zipFile, unzipDir, "njrxy_+7804");

            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            file = new File(unzipDir);
            tempList = file.listFiles();

            if (tempList == null) {
                j.setSuccess(true);
                j.setMsg("无氿上云工程数据,请联系管理员！");
                return j;
            }

            for (File temp : tempList) {
                if (temp.getName().endsWith("mdb")) {
                    temp.renameTo(new File(mdbPath));
                    break;
                }
            }

            List<Map<String, Object>> datas = SqliteHelper.query(mdbPath, "select bz, zhdj from qianfeiyongbiao where nodeid=5");

            if (datas != null && datas.size() > 0) {
                for (Map<String, Object> data : datas) {
                    String id = StringUtil.trimToEmpty(data.get("bz"));
                    String price = StringUtil.trimToEmpty(data.get("zhdj"));
                    fieldDataServiceI.backFill(Integer.parseInt(id), price, feeType);
                }
            }

            j.setSuccess(true);
            j.setMsg("回填成功！");
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg(e.getMessage());
        }
        return j;
    }
}
