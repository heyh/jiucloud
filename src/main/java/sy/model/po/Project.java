package sy.model.po;

import javax.persistence.*;
import java.util.Date;

/**
 * 工程名称管理
 * 
 * @author apple
 */
@Entity
@Table(name = "tgc_Project")
public class Project {

	@Id
	@Column(name = "ID", nullable = false, length = 36)
	private int id;
	@Column(name = "isdel", length = 2)
	private int isdel; // 0删除，1未删除
	@Column(name = "compId")
	private String compId;// 公司ID
	@Column(name = "uids")
	private String uid;// 用户ID

	// 合同信息
	@Column(name = "proName")
	private String proName;// 工程合同名称
	@Column(name = "projectId", nullable = false, length = 36)
	private String projectId;// 项目编号
	@Column(name = "provice")
	private String provice;// 省
	@Column(name = "city")
	private String city;// 市
	@Temporal(TemporalType.DATE)
	private Date zbtzsrq; // 中标通知书日期
	@Temporal(TemporalType.DATE)
	private Date htqdrq;// 合同签订日期
	@Temporal(TemporalType.DATE)
	@Column(name = "kgrq", length = 10)
	private Date kgrq;// 开工日期
	@Temporal(TemporalType.DATE)
	@Column(name = "jgrq", length = 10)
	private Date jgrq;// 竣工日期
	private String gczbyd;// 工程质保约定
	private String gcfkyd;// 工程付款约定
	@Column(name = "manager2")
	private String manager2;// 投标项目经理
	@Column(name = "gchtj")
	private String gchtj;// 工程合同价
	@Column(name = "shortname")
	private String shortname;// 工程简称
	// 建筑面积或规模值
	@Column(name = "jzmjorgm")
	private String jzmjorgm;
	private String money_state;
	// 工程预算 工程结算 工程报价 工程标底 设计概算 投资估算
	@Column(name = "zjlx")
	private String zjlx;// 造价类型
	// 建筑工程 市政工程 安装工程 装饰工程 仿古园林 修缮工程 人防工程 电力工程 水利工程 交通工程 冶金工程 内河航运 沿海港口 铁路工程
	// 通讯工程 其他
	@Column(name = "gclx")
	private String gclx;// 工程类型
	// 在建工程 已验收工程 审计完工程 计算完工程 养护结算工程 尾款已付清工程
	@Column(name = "gczt")
	private String gczt;// 工程状态

	// 施工信息
	@Column(name = "manager")
	private String manager;// 实际项目经理
	@Temporal(TemporalType.DATE)
	private Date gckgrq;// 工程正式开工日期
	@Temporal(TemporalType.DATE)
	private Date gcjgrq;// 工程正式竣工日期
	private String gcdqyjqk;// 工程到期移交情况
	private String gczlhjqk;// 工程质量获奖情况
	private String jgjss;// 竣工结算书
	private String gcaqwmqk;// 工程安全文明情况
	private String jgbb;// 竣工报告
	private String jgzl;// 竣工资料
	private String zjy;// 造价员
	private String zly;// 资料员
	@Temporal(TemporalType.DATE)
	private Date jgysrq;// 竣工验收日期

	// 工程维护
	private String gcwhq;// 工程维护期
	private String yhjb;// 养护级别
	private String yhcbr;// 养护承包人
	@Temporal(TemporalType.DATE)
	private Date whkssj;// 工程内部维护开始时间
	@Temporal(TemporalType.DATE)
	private Date whjssj;// 工程内部维护结束时间
	@Temporal(TemporalType.DATE)
	private Date htwhjzr;// 合同维护截止日
	@Temporal(TemporalType.DATE)
	private Date gcyjrq;// 工程移交日期

    @Column(name = "isLock")
    private String isLock;// 工程锁定标志

    public String getIsLock() {
        return isLock;
    }

    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIsdel() {
		return isdel;
	}

	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getProvice() {
		return provice;
	}

	public void setProvice(String provice) {
		this.provice = provice;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getJzmjorgm() {
		return jzmjorgm;
	}

	public void setJzmjorgm(String jzmjorgm) {
		this.jzmjorgm = jzmjorgm;
	}

	public String getGchtj() {
		return gchtj;
	}

	public void setGchtj(String gchtj) {
		this.gchtj = gchtj;
	}

	public String getZjlx() {
		return zjlx;
	}

	public void setZjlx(String zjlx) {
		this.zjlx = zjlx;
	}

	public String getGclx() {
		return gclx;
	}

	public void setGclx(String gclx) {
		this.gclx = gclx;
	}

	public String getGczt() {
		return gczt;
	}

	public void setGczt(String gczt) {
		this.gczt = gczt;
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

	public String getMoney_state() {
		return money_state;
	}

	public void setMoney_state(String money_state) {
		this.money_state = money_state;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getManager2() {
		return manager2;
	}

	public void setManager2(String manager2) {
		this.manager2 = manager2;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public Date getZbtzsrq() {
		return zbtzsrq;
	}

	public void setZbtzsrq(Date zbtzsrq) {
		this.zbtzsrq = zbtzsrq;
	}

	public Date getHtqdrq() {
		return htqdrq;
	}

	public void setHtqdrq(Date htqdrq) {
		this.htqdrq = htqdrq;
	}

	public String getGczbyd() {
		return gczbyd;
	}

	public void setGczbyd(String gczbyd) {
		this.gczbyd = gczbyd;
	}

	public String getGcfkyd() {
		return gcfkyd;
	}

	public void setGcfkyd(String gcfkyd) {
		this.gcfkyd = gcfkyd;
	}

	public Date getGckgrq() {
		return gckgrq;
	}

	public void setGckgrq(Date gckgrq) {
		this.gckgrq = gckgrq;
	}

	public Date getGcjgrq() {
		return gcjgrq;
	}

	public void setGcjgrq(Date gcjgrq) {
		this.gcjgrq = gcjgrq;
	}

	public String getGcdqyjqk() {
		return gcdqyjqk;
	}

	public void setGcdqyjqk(String gcdqyjqk) {
		this.gcdqyjqk = gcdqyjqk;
	}

	public String getGczlhjqk() {
		return gczlhjqk;
	}

	public void setGczlhjqk(String gczlhjqk) {
		this.gczlhjqk = gczlhjqk;
	}

	public String getJgjss() {
		return jgjss;
	}

	public void setJgjss(String jgjss) {
		this.jgjss = jgjss;
	}

	public String getGcaqwmqk() {
		return gcaqwmqk;
	}

	public void setGcaqwmqk(String gcaqwmqk) {
		this.gcaqwmqk = gcaqwmqk;
	}

	public String getJgbb() {
		return jgbb;
	}

	public void setJgbb(String jgbb) {
		this.jgbb = jgbb;
	}

	public String getJgzl() {
		return jgzl;
	}

	public void setJgzl(String jgzl) {
		this.jgzl = jgzl;
	}

	public String getZjy() {
		return zjy;
	}

	public void setZjy(String zjy) {
		this.zjy = zjy;
	}

	public String getZly() {
		return zly;
	}

	public void setZly(String zly) {
		this.zly = zly;
	}

	public Date getJgysrq() {
		return jgysrq;
	}

	public void setJgysrq(Date jgysrq) {
		this.jgysrq = jgysrq;
	}

	public String getGcwhq() {
		return gcwhq;
	}

	public void setGcwhq(String gcwhq) {
		this.gcwhq = gcwhq;
	}

	public String getYhjb() {
		return yhjb;
	}

	public void setYhjb(String yhjb) {
		this.yhjb = yhjb;
	}

	public String getYhcbr() {
		return yhcbr;
	}

	public void setYhcbr(String yhcbr) {
		this.yhcbr = yhcbr;
	}

	public Date getWhkssj() {
		return whkssj;
	}

	public void setWhkssj(Date whkssj) {
		this.whkssj = whkssj;
	}

	public Date getWhjssj() {
		return whjssj;
	}

	public void setWhjssj(Date whjssj) {
		this.whjssj = whjssj;
	}

	public Date getHtwhjzr() {
		return htwhjzr;
	}

	public void setHtwhjzr(Date htwhjzr) {
		this.htwhjzr = htwhjzr;
	}

	public Date getGcyjrq() {
		return gcyjrq;
	}

	public void setGcyjrq(Date gcyjrq) {
		this.gcyjrq = gcyjrq;
	}

}
