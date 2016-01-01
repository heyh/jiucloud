package sy.service;

import org.springframework.stereotype.Repository;
import sy.model.po.Price;
import sy.model.po.Project;
import sy.pageModel.AnalysisData;
import sy.pageModel.AnalysisSearch;

import java.util.List;

/**
 * **************************************************************** 文件名称 :
 * ApplicationServiceI.java 作 者 : Administrator 创建时间 : 2015年6月4日11:18:23 文件描述 :
 * AnalysisData接口，管理项目名称的增删改查等功能 版权声明 : 修改历史 : 2015年6月4日 初始版本
 *****************************************************************
 */
@Repository
public interface AnalysisServiceI {

	public List<AnalysisData> getList(String date, String date2, int tpcost_id,
			int project_id, List<Integer> ugroup);

	public List<AnalysisData> getTable(AnalysisSearch model,
			List<Integer> ugroup, List<Project> projects, List<Price> prices,String cid);

	public List<AnalysisData> getList(String date, String date2, String price_id,
			String project_id, List<Integer> ugroup,String cid);

    public String getFeeStatList(int projectId, String cid, List<Integer> ugroup);

    public List<Object[]> getAllFee(String cid, List<Integer> ugroup);
}
