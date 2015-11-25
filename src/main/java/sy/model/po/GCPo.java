package sy.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * GCPo 附件表
 * 
 * @author william.shao
 *
 */
@Entity
@Table(name = "tgc_file")
public class GCPo {

	/**
	 * ID
	 */
	@Id
	@Column(name = "ID", nullable = false, length = 36)
	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
	// "GENERATOR_SEQ_BANK")
	// @SequenceGenerator(name = "GENERATOR_SEQ_BANK", sequenceName =
	// "PGMGRSYS_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	/**
	 * 关联ID 通过关联ID可以查询多个文件
	 */
	@Column(name = "mpid")
	private String mpid;

	@Column(name = "updateError")
	private String updateError;

	@Column(name = "updateswferror")
	private String updateswferror;

	public String getUpdateswferror() {
		return updateswferror;
	}

	public void setUpdateswferror(String updateswferror) {
		this.updateswferror = updateswferror;
	}

	public String getUpdateError() {
		return updateError;
	}

	public void setUpdateError(String updateError) {
		this.updateError = updateError;
	}

	/**
	 * 文件名
	 */
	@Column(name = "fileName")
	private String fileName;

	/**
	 * 保存目录中的文件名
	 */
	@Column(name = "sourceFilePath")
	private String sourceFilePath;

	/**
	 * 转换后的pdf文件
	 */
	@Column(name = "pdfFilePath")
	private String pdfFilePath;

	/**
	 * 转换后的swf文件
	 */
	@Column(name = "swfFilePath")
	private String swfFilePath;

	/**
	 * 扩展名 文档类:doc/docx/xls/xlsx/txt/ppt/pptx 图片：jpg 视频:
	 */
	@Column(name = "ext")
	private String ext;

	/**
	 * 文件转换状态 0:doc > pdf 1:pdf > swf 2: >转换完成 3: >转换失败 4: >不需要转 5: >正在处理
	 */
	@Column(name = "status")
	private int status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMpid() {
		return mpid;
	}

	public void setMpid(String mpid) {
		this.mpid = mpid;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSourceFilePath() {
		return sourceFilePath;
	}

	public void setSourceFilePath(String sourceFilePath) {
		this.sourceFilePath = sourceFilePath;
	}

	public String getPdfFilePath() {
		return pdfFilePath;
	}

	public void setPdfFilePath(String pdfFilePath) {
		this.pdfFilePath = pdfFilePath;
	}

	public String getSwfFilePath() {
		return swfFilePath;
	}

	public void setSwfFilePath(String swfFilePath) {
		this.swfFilePath = swfFilePath;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "GCPo [id=" + id + ", mpid=" + mpid + ", updateError="
				+ updateError + ", updateswferror=" + updateswferror
				+ ", fileName=" + fileName + ", sourceFilePath="
				+ sourceFilePath + ", pdfFilePath=" + pdfFilePath
				+ ", swfFilePath=" + swfFilePath + ", ext=" + ext + ", status="
				+ status + "]";
	}
}
