package sy.pageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * EasyUI DataGrid模型
 * 
 * @author 孙宇
 * 
 */
public class DataGrid implements java.io.Serializable {

	private Long total = 0L;
	private List rows = new ArrayList();
    private List<FieldData> footer = new ArrayList<FieldData>();

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

    public List<FieldData> getFooter() {
        return footer;
    }

    public void setFooter(List<FieldData> footer) {
        this.footer = footer;
    }

    @Override
	public String toString() {
		return "DataGrid [total=" + total + ", rows=" + rows + "]";
	}

}
