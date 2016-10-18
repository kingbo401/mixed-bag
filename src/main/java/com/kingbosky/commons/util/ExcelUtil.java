package com.kingbosky.commons.util;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelUtil {
	
	/**
	 * 给workbook添加一个sheet
	 * @param workbook
	 * @param sheetName
	 * @param headers
	 * @param datas
	 */
	public static void addSheetToWorkbook(HSSFWorkbook workbook, String sheetName, List<String> headers, List<List<Object>> datas) {
		if(workbook == null || StringUtil.isEmpty(sheetName) || CollectionUtil.isEmpty(headers) || CollectionUtil.isEmpty(datas)) return;
		HSSFSheet sheet = workbook.createSheet(sheetName);
		HSSFRow head = sheet.createRow(0);
		for (int i = 0; i < headers.size(); i++) {
			HSSFCell headCell = head.createCell(i);
			headCell.setCellValue(StringUtil.getString(headers.get(i)));
		}
		
		for (int j = 0, rowIdx = 1; j < datas.size(); j++, rowIdx++) {
			HSSFRow row = sheet.createRow(rowIdx);
			List<Object> rowData = datas.get(j);
			if (CollectionUtil.isEmpty(rowData)) continue;
			for (int k = 0; k < rowData.size(); k++) {
				HSSFCell cell = row.createCell(k);
				cell.setCellValue(StringUtil.getString(rowData.get(k)));
			}
		}
	}
}
