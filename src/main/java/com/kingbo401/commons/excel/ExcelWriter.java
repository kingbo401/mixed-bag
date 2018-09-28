package com.kingbo401.commons.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.commons.util.DateUtil;
import com.kingbo401.commons.util.StringUtil;
/**
 * ExcelWriter
 * @author tianqiongbo
 *
 */
public class ExcelWriter {
	private Workbook workbook = null;
	
	public ExcelWriter(ExcelVersion version){
		if(ExcelVersion.XLS.equals(version)){
			workbook = new HSSFWorkbook();
		}else{
			workbook = new XSSFWorkbook();
		}
	}
	
	/**
	 * 添加一个sheet
	 * @param sheetName
	 * @param headers
	 * @param datas
	 */
	public void addSheet(String sheetName, List<String> headers, List<List<Object>> datas) {
		if(StringUtil.isEmpty(sheetName) || CollectionUtil.isEmpty(headers) || CollectionUtil.isEmpty(datas)) return;
		Sheet sheet = workbook.createSheet(sheetName);
		Row head = sheet.createRow(0);
		for (int i = 0; i < headers.size(); i++) {
			Cell headCell = head.createCell(i);
			headCell.setCellValue(StringUtil.getString(headers.get(i)));
		}
		
		for (int j = 0, rowIdx = 1; j < datas.size(); j++, rowIdx++) {
			Row row = sheet.createRow(rowIdx);
			List<Object> rowData = datas.get(j);
			if (CollectionUtil.isEmpty(rowData)) continue;
			for (int k = 0; k < rowData.size(); k++) {
				Cell cell = row.createCell(k);
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
	 * @param sheetName
	 * @param headers
	 * @param datas
	 */
	public void addSheet(String sheetName, String[] headers, List<List<Object>> datas) {
		addSheet(sheetName, Arrays.asList(headers), datas);
	}
	
	public void write(OutputStream stream) throws IOException{
		workbook.write(stream);
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}
}
