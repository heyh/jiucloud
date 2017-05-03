package sy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sy.model.po.TFieldData;
import sy.pageModel.FieldData;
import sy.pageModel.Json;
import sy.pageModel.SessionInfo;
import sy.service.FieldDataServiceI;
import sy.service.ItemServiceI;
import sy.service.ProjectOverviewServiceI;
import sy.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by heyh on 2017/2/27.
 */

@Controller
@RequestMapping("/projectOverviewController")
public class ProjectOverviewController {

    @Autowired
    private ProjectOverviewServiceI projectOverviewService;

    @Autowired
    private FieldDataServiceI fieldDataService;

    @Autowired
    private ItemServiceI itemService;

    @RequestMapping("/securi_projectOverview")
    public String projectOverview(@RequestParam(value = "id", required = true) String id,
                                  HttpServletRequest request, HttpServletResponse response) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String cid = sessionInfo.getCompid();

        TFieldData tFieldData = fieldDataService.detail(id);
        String projectId = tFieldData.getProjectName();
        String remark = tFieldData.getRemark();

        String mdbPath = "";
        if (remark.indexOf("氿上云导入") != -1) {
            mdbPath = PropertyUtil.getFileRealPath() + "/jsw/" + projectId + "/" + remark.substring("氿上云导入".length() + 1) + ".mdb";
        } else if (remark.indexOf("execl导入") != -1) {
            mdbPath = PropertyUtil.getFileRealPath() + "/execl/" + projectId + "/" + remark.substring("execl导入".length() + 1) + ".mdb";
        }

        List<Map<String, Object>> projectDataInfos = projectOverviewService.getProjectDataInfo(mdbPath);
        List<Map<String, Object>> cs1Infos = projectOverviewService.getCS1Info(mdbPath);
        List<Map<String, Object>> cs2Infos = projectOverviewService.getCS2Info(mdbPath);
        List<Map<String, Object>> qtInfos = projectOverviewService.getQTInfo(mdbPath);
        List<Map<String, Object>> summaryInfos = projectOverviewService.getSummary(mdbPath);

        String[] names = remark.split("-");
        String name = itemService.getSingleItem(cid, projectId, names[2]).getName();
        String importDate = String.valueOf(DateKit.StringToDate(names[3]));

        Map<String, Object> mdbInfo = new HashMap<String, Object>();
        mdbInfo.put("mdbName", name + " - " + importDate);
        mdbInfo.put("mdbPath", mdbPath);
        mdbInfo.put("projectDataInfos", projectDataInfos);
        mdbInfo.put("cs1Infos", cs1Infos);
        mdbInfo.put("cs2Infos", cs2Infos);
        mdbInfo.put("qtInfos", qtInfos);
        mdbInfo.put("summaryInfos", summaryInfos);

        request.setAttribute("mdbInfo", mdbInfo);
        return "/app/projectOverview/projectOverview";
    }


//    @RequestMapping("/securi_projectsOverview")
//    public String projectsOverview(@RequestParam(value = "id", required = true) String projectId,
//                                  HttpServletRequest request, HttpServletResponse response) {
//        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
//        String cid = sessionInfo.getCompid();
//
//        File[] jswFiles = getFileList(PropertyUtil.getFileRealPath() + "/jsw/" + projectId);
//        File[] excelFiles = getFileList(PropertyUtil.getFileRealPath() + "/excel/" + projectId);
//        List<File> fileList = new ArrayList<File>();
//        if (jswFiles != null) {
//            fileList = new ArrayList(Arrays.asList(jswFiles));
//        }
//        if (excelFiles != null) {
//            fileList.addAll(Arrays.asList(excelFiles));
//        }
//
//        if (fileList == null || fileList.size() < 1) {
//            return "/app/projectOverview/projectOverview";
//        }
//
//        List<Map<String, Object>> mdbInfos = new ArrayList<Map<String, Object>>();
//        Map<String, Object> mdbInfo = new HashMap<String, Object>();
//        for (File temp : fileList) {
//            String mdbName = temp.getName();
//            if (FileUtil.getExtName(mdbName, '.').equals(".mdb")) {
//                String mdbPath = temp.getPath();
//                List<Map<String, Object>> projectDataInfos = projectOverviewService.getProjectDataInfo(mdbPath);
//                List<Map<String, Object>> cs1Infos = projectOverviewService.getCS1Info(mdbPath);
//                List<Map<String, Object>> cs2Infos = projectOverviewService.getCS2Info(mdbPath);
//                List<Map<String, Object>> qtInfos = projectOverviewService.getQTInfo(mdbPath);
////                List<Map<String, Object>> gljSummaryInfos = projectOverviewService.getGljSummary(mdbPath);
//                List<Map<String, Object>> summaryInfos = projectOverviewService.getSummary(mdbPath);
//
//                String[] names = mdbName.split("-");
//                String name = itemService.getSingleItem(cid, projectId, names[1]).getName();
//                String importDate = String.valueOf(DateKit.StringToDate(names[2].substring(0, names[2].length()-4)));
//
//                mdbInfo = new HashMap<String, Object>();
//                mdbInfo.put("mdbName", name + " - " + importDate);
//                mdbInfo.put("mdbPath", mdbPath);
//                mdbInfo.put("projectDataInfos", projectDataInfos);
//                mdbInfo.put("cs1Infos", cs1Infos);
//                mdbInfo.put("cs2Infos", cs2Infos);
//                mdbInfo.put("qtInfos", qtInfos);
////                mdbInfo.put("gljSummaryInfos", gljSummaryInfos);
//                mdbInfo.put("summaryInfos", summaryInfos);
//
//                mdbInfos.add(mdbInfo);
//
//            }
//        }
//
//        request.setAttribute("mdbInfos", mdbInfos);
//        return "/app/projectOverview/projectOverview";
//    }

    @RequestMapping("/securi_getProjectMachineInfo")
    @ResponseBody
    public Json getProjectMachineInfo(@RequestParam(value = "mdbPath", required = true) String mdbPath,
                                      @RequestParam(value = "pointNo", required = true) String pointNo,
                                      HttpServletRequest request, HttpServletResponse response) {

        Json json = new Json();
        List<Map<String, Object>> projectMachineInfos = projectOverviewService.getGljSummary(mdbPath, pointNo);

        json.setObj(projectMachineInfos);
        json.setSuccess(true);
        return json;
    }

    private File[] getFileList(String filesPath) {
        return new File(filesPath).listFiles();
    }

}
