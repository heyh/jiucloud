package sy.model.po;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * ****************************************************************
 * 文件名称 : Button.java
 * 作 者 :   Administrator
 * 创建时间 : 2015年1月13日 上午9:19:20
 * 文件描述 : 菜单内容
 * 版权声明 : 
 * 修改历史 : 2015年1月13日 1.00 初始版本
 *****************************************************************
 */
@Entity
@Table(name = "tbutton")
public class Button {
	
	//菜单编号
	@Id
	@Column(name = "ID", nullable = false, length = 36)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERATOR_SEQ_BANK")
	//@SequenceGenerator(name = "GENERATOR_SEQ_BANK", sequenceName = "PGMGRSYS_SEQ", initialValue = 1, allocationSize = 1)
	
	@GeneratedValue(strategy=GenerationType.AUTO)  
	private int id;
	
	@Column(name = "type" , length = 2)
	private int type;//菜单的响应动作类型    1:文本 2:图文 3:链接
	
	@Column(name = "name")
	private String name; //菜单标题
	
	@Column(name = "key",columnDefinition="CLOB", nullable=true)
	private String key;//用于消息接口推送    类型 1
	
	@Column(name = "informId")
	private String informId;//图文  类型2
	
	@Column(name = "url")
	private String url;//网页链接  类型3
	
	//子菜单
	@Column(name = "subMenu")
	private String subMenu;//二级菜单id ,,,
	
	@Transient
	private List<Button> sub_button = new ArrayList<Button>();//二级菜单
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSubMenu() {
		return subMenu;
	}
	public void setSubMenu(String subMenu) {
		this.subMenu = subMenu;
	}
	public List<Button> getSub_button() {
		return sub_button;
	}
	public void setSub_button(List<Button> sub_button) {
		this.sub_button = sub_button;
	}
	public String getInformId() {
		return informId;
	}
	public void setInformId(String informId) {
		this.informId = informId;
	}
	
	
}
