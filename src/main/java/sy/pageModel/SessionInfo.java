package sy.pageModel;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import sy.model.Param;
import sy.model.S_department;

import java.util.List;
import java.util.Map;

/**
 * session信息模型
 * 
 * @author 孙宇
 * 
 */
public class SessionInfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1848239681643733253L;

	private String id;// 用户ID
	private String name;// 用户登录名
	private String compid; // 公司ID
	private String compName; // 公司名
	private String depid; // 职位ID
	private String depName; // 职位名
	private String department_id; // 部门id
	private String department_name; // 部门名
	private List<Integer> dgroup;
	private List<Integer> ugroup;
	private String ip;
	private String last_cost_id;
	private String last_project_id;
	private int isadmin;
    // 热线号码
    private String cno;

    // add by heyh
    private int parentId;

    private List<S_department> departmentIds;
    private List<Param> unitParams;
    private List<String> rightList;
	private JSONArray costTree;
	private JSONArray docCostTree;
	private JSONArray billCostTree;
	private JSONArray materialCostTree;

	public JSONArray getBillCostTree() {
		return billCostTree;
	}

	public void setBillCostTree(JSONArray billCostTree) {
		this.billCostTree = billCostTree;
	}

	public JSONArray getMaterialCostTree() {
		return materialCostTree;
	}

	public void setMaterialCostTree(JSONArray materialCostTree) {
		this.materialCostTree = materialCostTree;
	}

	public JSONArray getDocCostTree() {
		return docCostTree;
	}

	public void setDocCostTree(JSONArray docCostTree) {
		this.docCostTree = docCostTree;
	}

	public JSONArray getCostTree() {
		return costTree;
	}

	public void setCostTree(JSONArray costTree) {
		this.costTree = costTree;
	}

	public List<String> getRightList() {
        return rightList;
    }

    public void setRightList(List<String> rightList) {
        this.rightList = rightList;
    }

    public List<S_department> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<S_department> departmentIds) {
        this.departmentIds = departmentIds;
    }

    public String getProjectInfos() {
        return projectInfos;
    }

    public void setProjectInfos(String projectInfos) {
        this.projectInfos = projectInfos;
    }

    private String projectInfos;
    private String underlingUsers;

    public String getUnderlingUsers() {
        return underlingUsers;
    }

    public void setUnderlingUsers(String underlingUsers) {
        this.underlingUsers = underlingUsers;
    }

    private Map<String, List<Map<String, Object>>> costTypeInfos;

    public Map<String, List<Map<String, Object>>> getCostTypeInfos() {
        return costTypeInfos;
    }

    public void setCostTypeInfos(Map<String, List<Map<String, Object>>> costTypeInfos) {
        this.costTypeInfos = costTypeInfos;
    }

    // add by heyh
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getCompid() {
		return compid;
	}

	public void setCompid(String compid) {
		this.compid = compid;
	}



	private List<String> resourceList;// 用户可以访问的资源地址列表

	public List<String> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<String> resourceList) {
		this.resourceList = resourceList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getDepid() {
		return depid;
	}

	public void setDepid(String depid) {
		this.depid = depid;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public List<Integer> getDgroup() {
		return dgroup;
	}

	public void setDgroup(List<Integer> dgroup) {
		this.dgroup = dgroup;
	}

	public List<Integer> getUgroup() {
		return ugroup;
	}

	public void setUgroup(List<Integer> ugroup) {
		this.ugroup = ugroup;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getIsadmin() {
		return isadmin;
	}

	public void setIsadmin(int isadmin) {
		this.isadmin = isadmin;
	}

	@Override
	public String toString() {
		return "SessionInfo [id=" + id + ", name=" + name + ", compid="
				+ compid + ", compName=" + compName + ", depid=" + depid
				+ ", depName=" + depName + ", dgroup=" + dgroup + ", ugroup="
				+ ugroup + ", ip=" + ip + ", isadmin=" + isadmin + ", cno="
				+ cno + "]";
	}

	public String getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}

	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}

	public String getLast_cost_id() {
		return last_cost_id;
	}

	public void setLast_cost_id(String last_cost_id) {
		this.last_cost_id = last_cost_id;
	}

	public String getLast_project_id() {
		return last_project_id;
	}

	public void setLast_project_id(String last_project_id) {
		this.last_project_id = last_project_id;
	}

    public void setUnitParams(List<Param> unitParams) {
        this.unitParams = unitParams;
    }

    public List<Param> getUnitParams() {
        return unitParams;
    }
}
