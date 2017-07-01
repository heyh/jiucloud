package sy.service;

import java.util.List;
import java.util.Map;

import sy.model.po.TFieldData;
import sy.pageModel.DataGrid;
import sy.pageModel.FieldData;
import sy.pageModel.PageHelper;

public interface FieldDataServiceI {

	/**
	 * 获得列表
	 */
	public DataGrid dataGrid(FieldData fieldData, PageHelper ph,
                             List<Integer> ugroup, String source, String keyword);
	public DataGrid dataGridForMobile(FieldData fieldData, PageHelper ph,
							 List<Integer> ugroup, String source, String keyword);
	/**
	 * 
	 * 删除
	 * 
	 * @param id
	 *            void
	 */
	public void delete(String id);

	/**
	 * 
	 * 添加
	 * 
	 * @param TFieldData
	 *            void
	 */
	public TFieldData add(TFieldData tFieldData);

	/**
	 * 
	 * 修改
	 * 
	 * @param TFieldData
	 *            void
	 */
	public void update(TFieldData tFieldData);

	/**
	 * 
	 * 详情
	 * 
	 * @param id
	 *            void
	 */
	public TFieldData detail(String id);

	List<TFieldData> getfindList(List<Integer> ugroup);

	public Object getId(TFieldData fieldData);

//    public void approvedField(Integer id, String approvedState, String approvedOption);

	public void approvedField(Integer id, String approvedState, String approvedOption, String currentApprovedUser);

    public List<TFieldData> getNeedApproveList(String currentApprovedUser);

    public boolean hasSameFieldData(TFieldData fieldData);

    /**
     * 获得审批列表
     */
    public DataGrid approveDataGrid(PageHelper ph, String currentApprovedUser);

    /**
     * 我的需要审批记录信息
     * @param ph
     * @param uid
     * @return
     */
    public DataGrid myApproveDataGrid(PageHelper ph, String uid);

	public TFieldData getMaxFieldByCidUid(String cid, String uid);

    public TFieldData getFieldByMaxId(String cid, String uid, String projectId);

    public List<TFieldData> getOutFieldByRelId(String relId);

    public List<Map<String, Object>> getMaterialDatas(String cid, String statDate, List<Integer> ugroup, Integer selDepartmentId);

    public List<FieldData> getBoq(String cid, String startDate, String endDate, List<Integer> ugroup, String type);

	public void backFill(Integer id, String price, String feeType);

	public List<TFieldData> getDefaultField(String cid);
}
