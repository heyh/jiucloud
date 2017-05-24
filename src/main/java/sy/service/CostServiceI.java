package sy.service;

import net.sf.json.JSONArray;
import sy.model.po.Cost;
import sy.model.po.Department_Cost;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;

import java.util.List;
import java.util.Map;

public interface CostServiceI {
	/**
	 * 
	 * 获得列表
	 */
//	DataGrid dataGrid(int department, String cid, String source);
    public DataGrid dataGrid(List<Integer> departmentIds, String cid,String source); // 多部门
	/**
	 * 
	 * 删除
	 * 
	 * @param id
	 *            void
	 */

	/**
	 * 根据Id查询
	 * 
	 * @return
	 */
	public Cost findById(String id);

	/**
	 * 添加
	 */
	public void add(Cost info);

	/**
	 * 修改
	 */
	public void update(Cost info);

	public int getMaxSortByPid(String pid, String cid);

	// /**
	// * 获取同pid下sort值仅次于传入sort的一条纪录，两条记录sort互换
	// */
	// boolean upSort(String id);
	//
	// /**
	// * 同上,反
	// */
	// boolean downSort(String id);

	/**
	 * 如果当前公司是第一次进入费用模块，则导入蓝本
	 */
	public void init(String cid);

	JSONArray getCostList(List<Integer> departmentIds, String cid, String source);

	public DataGrid dataGrid(String title, String code, PageHelper ph,
							 String cid);

	public List<Cost> getFamily(String nid, String cid);

	public String getMaxNidByCid(String cid);

	public List<Cost> getprices(String cid);

	Cost findOneView(String nid, String cid);

	public void treedelete(Cost cost);

	void delete(Cost cost);

	DataGrid dataGridWithPrice(String title, String code, PageHelper ph,
                               String cid, int price_id);

	DataGrid dataGridInPrice(String title, String code, PageHelper ph,
                             String cid, int price_id);

	public List<Cost> getEndCosts(String title, String code, PageHelper ph,
                                  String cid, String departmentIds);

	public Cost getParentByCode(String itemCode, String cid);

	Cost getCostByCode(String itemCode, String cid);

	List<Cost> getLikeCostByCode(String itemCode, String cid);

	public DataGrid departmentGrid(String cid);

	void add2(Department_Cost tem);

	void delete2(Department_Cost tem);

	Department_Cost findoneview2(int cost_id, int department_id);

	DataGrid dataGridWithDepartment(String title, String code, PageHelper ph,
                                    String cid, int department_id);

	DataGrid dataGridInDepartment(String title, String code, PageHelper ph,
                                  String cid, int price_id);
    public Map<String, List<Map<String, Object>>> getCostTypeInfos(List<Integer> departmentIds, String cid);
	public Map<String, List<Map<String, Object>>> getCostTypeInfosForMobile(List<Integer> departmentIds, String cid);
    public void initDefaultCost(String cid);
    public int insertDefaultCost(String cid);

    public List<Cost> getMatrialsCostList(String cid, Integer selDepartmentId);

}
