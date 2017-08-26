package sy.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class ObjectExcelViewSpecial extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
									  HSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
//		Date date = new Date();
		String filename = UtilDate.getOrderNums();
		HSSFSheet sheet;
		HSSFCell cell;
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename="+filename+".xls");
		sheet = workbook.createSheet("sheet1");
		
		HSSFCellStyle headerStyle = workbook.createCellStyle(); //标题样式
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont headerFont = workbook.createFont();	//标题字体
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setFontHeightInPoints((short)20);
		headerStyle.setFont(headerFont);
		short width = 15, headerHeight=25*20, contentHeight=25*15;
		sheet.setDefaultColumnWidth(width);

		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);

		// 标题
		Map<String, Object> largeTitle = (Map<String, Object>) model.get("largeTitle");
		if (largeTitle != null) {
			String largeTitleContent = StringUtil.trimToEmpty(largeTitle.get("largeTitleContent"));
			int cellCount = Integer.parseInt(StringUtil.trimToEmpty(largeTitle.get("cellCount")));
			cell = getCell(sheet, 0, 0);
			cell.setCellStyle(headerStyle);
			setText(cell, largeTitleContent);
			sheet.addMergedRegion(new Region(0, (short) (0), 0, (short) (cellCount - 1)));
		}

		// 表头
		List<String> titles = (List<String>) model.get("titles");
		if (titles != null && titles.size()>0) {
			for (int i = 0; i < titles.size(); i++) { //表头
				String title = titles.get(i);
				cell = getCell(sheet, 1, i);
				cell.setCellStyle(headerStyle);
				setText(cell, title);
			}
			sheet.getRow(0).setHeight(headerHeight);
		}

		// 内容
		HSSFCellStyle contentStyle = workbook.createCellStyle(); //内容样式
		HSSFFont contentFont = workbook.createFont();	//标题字体
		contentFont.setBoldweight((short)200);
		contentFont.setFontHeightInPoints((short)10);
		contentStyle.setFont(contentFont);
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		contentStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		contentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		contentStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		contentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);

		// 合计
		HSSFCellStyle totalStyle = workbook.createCellStyle(); //内容样式
		HSSFFont totalFont = workbook.createFont();	//标题字体
		totalFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		totalFont.setFontHeightInPoints((short)15);
		totalStyle.setFont(totalFont);
		totalStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		totalStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		totalStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		totalStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		totalStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);

		List<PageData> varList = (List<PageData>) model.get("varList");
		int varCount = varList.size();
		for(int i=0; i<varCount; i++){
			PageData vpd = varList.get(i);
			for(int j=0;j<vpd.size();j++){
				String varstr = StringUtil.trimToEmpty(vpd.get("var"+(j+1)));
				if (varstr.equals("total")) {
					cell = getCell(sheet, i+1, 0);
					cell.setCellStyle(totalStyle);
					setText(cell,"合计");
					sheet.addMergedRegion(new Region(i+1, (short) (0), i+1, (short) (2)));
				} else {
					cell = getCell(sheet, i+1, j);
					cell.setCellStyle(contentStyle);
					setText(cell,varstr);
				}

			}
			sheet.getRow(i+1).setHeight(contentHeight);
		}
		
	}

}
