package com.kingbosky.commons.util;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
				Object data = rowData.get(k);
				if(data == null) data = "";
				if(data instanceof Date){
					cell.setCellValue(DateUtil.formatDate((Date)data));
				}else if(data instanceof Number){
					cell.setCellValue(((Number)data).doubleValue());
				}else if(data instanceof Calendar){
					cell.setCellValue(DateUtil.formatDate(((Calendar)data).getTime()));
				}else if(data instanceof Boolean){
					cell.setCellValue((Boolean)data);
				}else{
					cell.setCellValue(String.valueOf(data));
				} 
			}
		}
	}
	
	/**
	 * 给workbook添加一个sheet
	 * @param workbook
	 * @param sheetName
	 * @param headers
	 * @param datas
	 */
	public static void addSheetToWorkbook(HSSFWorkbook workbook, String sheetName, String[] headers, List<List<Object>> datas) {
		addSheetToWorkbook(workbook, sheetName, Arrays.asList(headers), datas);
	}
}
