package sy.pageModel;

public class AnalysisSearch {

	private String startTime;
	private String endTime;
	private String project_id;
	private String cost_id;
	private String pcost_id;
	private String pName;
	private String costTypeName;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getCost_id() {
		return cost_id;
	}

	public void setCost_id(String cost_id) {
		this.cost_id = cost_id;
	}

	public String getPcost_id() {
		return pcost_id;
	}

	public void setPcost_id(String pcost_id) {
		this.pcost_id = pcost_id;
	}

	@Override
	public String toString() {
		return "AnalysisSearch [startTime=" + startTime + ", endTime="
				+ endTime + ", project_id=" + project_id + ", cost_id="
				+ cost_id + ", pcost_id=" + pcost_id + "]";
	}

	public boolean isNull() {
		if ((startTime == null || "".equals(startTime))
				&& (endTime == null || "".equals(startTime))
				&& (project_id == null || "".equals(startTime))
				&& (cost_id == null || "".equals(startTime))
				&& (pcost_id == null || "".equals(startTime))) {
			return true;
		}
		return false;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getCostTypeName() {
		return costTypeName;
	}

	public void setCostTypeName(String costTypeName) {
		this.costTypeName = costTypeName;
	}
}
