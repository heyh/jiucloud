package sy.service.impl;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;
import sy.service.ProjectOverviewServiceI;
import sy.util.SqliteHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heyh on 2017/3/4.
 */

@Service("projectOverviewService")
public class ProjectOverviewServiceImpl implements ProjectOverviewServiceI {

    /**
     * 分部分项工程量清单与计价表 	工程数据表
     *
     * @param mdbPath
     * @return
     */
    @Override
    public List<Map<String, Object>> getProjectDataInfo(String mdbPath) {
        List<Map<String, Object>> projectDatas = new ArrayList<Map<String, Object>>();
        Map<String, Object> projectData = new HashMap<String, Object>();
        List<Map<String, Object>> _projectDatas = SqliteHelper.query(mdbPath, "select * from 工程数据表");
        for(Map<String, Object> _projectData : _projectDatas) {
            projectData = new HashMap<String, Object>();
            projectData.put("deh", _projectData.get("定额号"));
            projectData.put("zmmc", _projectData.get("子目名称"));
            projectData.put("mshxmtz", _projectData.get("描述后的项目特征"));
            projectData.put("zmdw", _projectData.get("子目单位"));
            projectData.put("gclz", _projectData.get("工程量值"));
            projectData.put("jjdj", _projectData.get("基价单价"));
            projectData.put("rgfdj", _projectData.get("人工费单价"));
            projectData.put("clfdj", _projectData.get("材料费单价"));
            projectData.put("jxfdj", _projectData.get("机械费单价"));
            projectData.put("glf", _projectData.get("管理费"));
            projectData.put("jhlrz", _projectData.get("计划利润值"));
            projectData.put("jj", _projectData.get("基价"));
            projectData.put("pointNo", _projectData.get("POINTNO") == null ? _projectData.get("pointno") : _projectData.get("POINTNO"));

            projectDatas.add(projectData);
        }
        return projectDatas;
    }

    /**
     * 措施项目清单与计价表（一）	cs1
     *
     * @param mdbPath
     * @return
     */
    @Override
    public List<Map<String, Object>> getCS1Info(String mdbPath) {
        List<Map<String, Object>> csInfos = new ArrayList<Map<String, Object>>();
        Map<String, Object> csInfo = new HashMap<String, Object>();
        List<Map<String, Object>> _cs1Infos = SqliteHelper.query(mdbPath, "select * from CS1");
        for (Map<String, Object> _csInfo : _cs1Infos) {
            csInfo = new HashMap<String, Object>();
            csInfo.put("measureXh", _csInfo.get("MEASUREXH"));
            csInfo.put("measureName", _csInfo.get("MEASURENAME"));
            csInfo.put("measureDw", _csInfo.get("MEASUREDW"));
            csInfo.put("measureJsgs", _csInfo.get("MEASUREJSGS"));
            csInfo.put("measureBase", _csInfo.get("MEASUREBASE"));
            csInfo.put("measureFee", _csInfo.get("MEASUREFEE"));
            csInfo.put("flfw", _csInfo.get("FLFW"));
            csInfo.put("measureSum", _csInfo.get("MEASURESUM"));
            csInfo.put("measureBh", _csInfo.get("MEASUREBH"));
            csInfo.put("measureJg", _csInfo.get("MEASUREJG"));
            csInfo.put("measureWmf", _csInfo.get("MEASUREWMF"));

            csInfos.add(csInfo);
        }
        return csInfos;
    }

    /**
     * 措施项目清单与计价表（二）	cs2
     *
     * @param mdbPath
     * @return
     */
    @Override
    public List<Map<String, Object>> getCS2Info(String mdbPath) {
        List<Map<String, Object>> cs2Infos = new ArrayList<Map<String, Object>>();
        Map<String, Object> cs2Info = new HashMap<String, Object>();
        List<Map<String, Object>> _cs2Infos = SqliteHelper.query(mdbPath, "select * from CS2");
        for(Map<String, Object> _cs2Info : _cs2Infos) {
            cs2Info = new HashMap<String, Object>();
            cs2Info.put("deh", _cs2Info.get("定额号"));
            cs2Info.put("zmmc", _cs2Info.get("子目名称"));
            cs2Info.put("mshxmtz", _cs2Info.get("描述后的项目特征"));
            cs2Info.put("zmdw", _cs2Info.get("子目单位"));
            cs2Info.put("gclz", _cs2Info.get("工程量值"));
            cs2Info.put("jjdj", _cs2Info.get("基价单价"));
            cs2Info.put("rgfdj", _cs2Info.get("人工费单价"));
            cs2Info.put("clfdj", _cs2Info.get("材料费单价"));
            cs2Info.put("jxfdj", _cs2Info.get("机械费单价"));
            cs2Info.put("glf", _cs2Info.get("管理费"));
            cs2Info.put("jhlrz", _cs2Info.get("计划利润值"));
            cs2Info.put("jj", _cs2Info.get("基价"));

            cs2Infos.add(cs2Info);
        }
        return cs2Infos;
    }

    /**
     * 其它项目清单与计价汇总表	qt
     *
     * @param mdbPath
     * @return
     */
    @Override
    public List<Map<String, Object>> getQTInfo(String mdbPath) {
        List<Map<String, Object>> qtInfos = new ArrayList<Map<String, Object>>();
        Map<String, Object> qtInfo = new HashMap<String, Object>();
        List<Map<String, Object>> _qtInfos = SqliteHelper.query(mdbPath, "select * from QT");
        for (Map<String, Object> _qtInfo : _qtInfos) {
            qtInfo = new HashMap<String, Object>();
            qtInfo.put("id", _qtInfo.get("ID"));
            qtInfo.put("name", _qtInfo.get("QTNAME"));
            qtInfo.put("dw", _qtInfo.get("QTDW"));
            qtInfo.put("jsgs", _qtInfo.get("QTJSGS"));
            qtInfo.put("js", _qtInfo.get("QTJS"));
            qtInfo.put("fl", _qtInfo.get("QTFL"));
            qtInfo.put("je", _qtInfo.get("QTJE"));
            qtInfo.put("jg", _qtInfo.get("QTJG"));
            qtInfo.put("bz", _qtInfo.get("QTBZ"));
            qtInfos.add(qtInfo);
        }
        return qtInfos;
    }

    /**
     * 材料暂估价格表、发包人供应材料一览表、 承包人供应主要材料一览表	gljsummay
     *
     * @param mdbPath
     * @return
     */
    @Override
    public List<Map<String, Object>> getGljSummary(String mdbPath, String pointNo) {
        List<Map<String, Object>> projectMachineInfos = new ArrayList<Map<String, Object>>();
        Map<String, Object> projectMachineInfo = new HashMap<String, Object>();

        List<Map<String, Object>> _projectMachineInfos = SqliteHelper.query(mdbPath, "select * from 工程工料机表 where pointno=" + '\'' + pointNo + '\'');
        for (Map<String, Object> _projectMachineInfo : _projectMachineInfos) {
            projectMachineInfo = new HashMap<String, Object>();
            projectMachineInfo.put("clbm", _projectMachineInfo.get("材料编码"));
            projectMachineInfo.put("clmc", _projectMachineInfo.get("材料名称"));
            projectMachineInfo.put("ggxh", _projectMachineInfo.get("规格型号"));
            projectMachineInfo.put("dw", _projectMachineInfo.get("单位"));
            projectMachineInfo.put("je", _projectMachineInfo.get("金额"));
            projectMachineInfo.put("dehl", _projectMachineInfo.get("定额数量0"));
            projectMachineInfo.put("yshl", _projectMachineInfo.get("定额数量1"));
            projectMachineInfo.put("hl", _projectMachineInfo.get("定额数量"));
            projectMachineInfo.put("xs", _projectMachineInfo.get("系数"));
            projectMachineInfo.put("sl", _projectMachineInfo.get("数量"));

            if (projectMachineInfo.get("材料编码") != null) {
                List<Map<String, Object>> _gljSummarys = SqliteHelper.query(mdbPath, "select scj,csscj,taxrate,cgbgf  from GLJSUMMARY where bm=" + '\'' + projectMachineInfo.get("材料编码") + '\'');
                if (_gljSummarys != null && _gljSummarys.size() > 0) {
                    projectMachineInfo.put("hsj", _gljSummarys.get(0).get("SCJ"));
                    projectMachineInfo.put("cb", _gljSummarys.get(0).get("cgbgf"));
                    projectMachineInfo.put("taxrate", _gljSummarys.get(0).get("taxrate"));
                    projectMachineInfo.put("csj", _gljSummarys.get(0).get("csscj"));
                }
            }

            projectMachineInfos.add(projectMachineInfo);
        }

        return projectMachineInfos;
    }

    /**
     * 取费表	summary
     *
     * @param mdbPath
     * @return
     */
    @Override
    public List<Map<String, Object>> getSummary(String mdbPath) {
        List<Map<String, Object>> summaryInfos = new ArrayList<Map<String, Object>>();
        Map<String, Object> summaryInfo = new HashMap<String, Object>();
        List<Map<String, Object>> _summaryInfos = SqliteHelper.query(mdbPath, "select * from SUMMARY");
        for (Map<String, Object> _summaryInfo : _summaryInfos) {
            summaryInfo = new HashMap<String, Object>();
            summaryInfo.put("FEEXH", _summaryInfo.get("FEEXH"));
            summaryInfo.put("FEENAME", _summaryInfo.get("FEENAME"));
            summaryInfo.put("FEECALC", _summaryInfo.get("FEECALC"));
            summaryInfo.put("FEEBASE", _summaryInfo.get("FEEBASE"));
            summaryInfo.put("FEERATE", _summaryInfo.get("FEERATE"));
            summaryInfo.put("FEERESULT", _summaryInfo.get("FEERESULT"));
            summaryInfo.put("ISSHOW", _summaryInfo.get("ISSHOW"));
            summaryInfo.put("FEEBH", _summaryInfo.get("FEEBH"));
            summaryInfo.put("FEEJG", _summaryInfo.get("FEEJG"));
            summaryInfo.put("FEEREMARK", _summaryInfo.get("FEEREMARK"));
            summaryInfos.add(summaryInfo);
        }

        return summaryInfos;
    }
}
