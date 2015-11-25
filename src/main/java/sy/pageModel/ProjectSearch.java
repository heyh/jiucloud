package sy.pageModel;

import java.util.Date;

public class ProjectSearch {

	private String manager;
	private String gczt;
	private String proName;// 根据项目名称查询
	private String projetcId;
	private Date kgrq;// 开工日期
	private Date jgrq;// 结束日期

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getGczt() {
		return gczt;
	}

	public void setGczt(String gczt) {
		this.gczt = gczt;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public Date getKgrq() {
		return kgrq;
	}

	public void setKgrq(Date kgrq) {
		this.kgrq = kgrq;
	}

	public Date getJgrq() {
		return jgrq;
	}

	public void setJgrq(Date jgrq) {
		this.jgrq = jgrq;
	}

	public String getProjetcId() {
		return projetcId;
	}

	public void setProjetcId(String projetcId) {
		this.projetcId = projetcId;
	}

	@Override
	public String toString() {
		return "ProjectSearch [manager=" + manager + ", gczt=" + gczt
				+ ", proName=" + proName + ", projetcId=" + projetcId
				+ ", kgrq=" + kgrq + ", jgrq=" + jgrq + "]";
	}
}
