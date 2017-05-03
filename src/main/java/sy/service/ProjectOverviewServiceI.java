package sy.service;

import java.util.List;
import java.util.Map;

/**
 * Created by heyh on 2017/3/4.
 */
public interface ProjectOverviewServiceI {

    List<Map<String, Object>> getProjectDataInfo(String mdbPath);
    List<Map<String, Object>> getCS1Info(String mdbPath);
    List<Map<String, Object>> getCS2Info(String mdbPath);
    List<Map<String, Object>> getQTInfo(String mdbPath);
    List<Map<String, Object>> getGljSummary(String mdbPath, String pointNo);
    List<Map<String, Object>> getSummary(String mdbPath);

}
