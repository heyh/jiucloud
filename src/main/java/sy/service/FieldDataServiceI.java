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

    public void approvedField(Integer id, String approvedState);

    public List<TFieldData> getNeedApproveList(String currentApprovedUser);

    public boolean hasSameFieldData(TFieldData fieldData);

    /**
     * 获得审批列表
     */
    public DataGrid approveDataGrid(PageHelper ph, String currentApprovedUser);
}
