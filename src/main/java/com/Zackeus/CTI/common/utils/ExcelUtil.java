package com.Zackeus.CTI.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.Zackeus.CTI.common.annotation.excel.ExcelField;
import com.Zackeus.CTI.common.utils.exception.MyException;
import com.Zackeus.CTI.common.utils.httpClient.HttpStatus;
import com.Zackeus.CTI.modules.agent.entity.AgentCallData;

/**
 * 
 * @Title:ExcelUtil
 * @Description:TODO(excel导出封装类)
 * @Company:
 * @author zhou.zhang
 * @date 2018年7月17日 上午10:55:24
 */
public class ExcelUtil {

	// 2007 版本以上 最大支持1048576行
	public final static String EXCEl_FILE_2007 = "2007";
	// 2003 版本 最大支持65536 行
	public final static String EXCEL_FILE_2003 = "2003";

	/**
	 * 
	 * @Title：exportExcel
	 * @Description: TODO()
	 * @see：
	 * @param list
	 * @param cls
	 * @param sumData
	 * @param version 2003 或者 2007，不传时默认生成2003版本
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public static <Q> Workbook exportExcel(List<Q> list, Class<Q> cls, Q sumData, String version) {
		if (StringUtils.isEmpty(version) || EXCEL_FILE_2003.equals(version.trim())) {
			return exportExcel2003(list, cls, sumData);
		} else {
			return exportExcel2007(list, cls, sumData);
		}
	}

	/**
	 * 
	 * @param <Q>
	 * @Title:exportExcel2003
	 * @Description: TODO(生成excel2003)
	 * @param list
	 * @param cls
	 * @param sumData
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public static <Q> HSSFWorkbook exportExcel2003(List<Q> list, Class<Q> cls, Q sumData) {
		HSSFWorkbook wb = new HSSFWorkbook();
		Field[] fields = cls.getDeclaredFields();
		ArrayList<String> headList = new ArrayList<String>();
		
		try {
			// 添加合计数据
			if (sumData != null) {
				list.add(sumData);
			}

			for (Field f : fields) {
				ExcelField field = f.getAnnotation(ExcelField.class);
				if (field != null) {
					headList.add(field.title());
				}
			}

			HSSFCellStyle titleStyle = getTitleStyle2003(wb);
			HSSFCellStyle cellStyle = getCellStyle2003(wb);
			
			HSSFSheet sheet = wb.createSheet();
			// 设置表格默认列宽度为15个字节
			sheet.setDefaultColumnWidth(20);
			
			// 设置Excel表的第一行即表头
			HSSFRow row = sheet.createRow(0);
			for (int i = 0; i < headList.size(); i++) {
				HSSFCell headCell = row.createCell(i);
				// 设置表头样式
				headCell.setCellStyle(titleStyle);
				headCell.setCellValue(new HSSFRichTextString(headList.get(i)));
			}

			for (int i = 0; i < list.size(); i++) {
				HSSFRow rowdata = sheet.createRow(i + 1);// 创建数据行
				Q q = list.get(i);
				Field[] ff = q.getClass().getDeclaredFields();
				int j = 0;
				for (Field f : ff) {
					ExcelField field = f.getAnnotation(ExcelField.class);
					if (field == null) {
						continue;
					}
					f.setAccessible(true);
					Object obj = f.get(q);
					HSSFCell cell = rowdata.createCell(j);
					cell.setCellStyle(cellStyle);

					// 当数字时
					if (obj instanceof Integer) {
						cell.setCellValue((Integer) obj);
						// 将序号替换为123456
						if (j == 0)
							cell.setCellValue(i + 1);
					}
					// 当为Long时
					else if (obj instanceof Long)
						cell.setCellValue((Long) obj);
					// 当为字符串时
					else if (obj instanceof String)
						cell.setCellValue((String) obj);
					// 当为布尔时
					else if (obj instanceof Boolean)
						cell.setCellValue((Boolean) obj);
					// 当为时间时
					else if (obj instanceof Date)
						cell.setCellValue(DateUtils.formatDate((Date) obj, DateUtils.parsePatterns[2]));
					// 当为时间时
					else if (obj instanceof Calendar)
						cell.setCellValue((Calendar) obj);
					// 当为小数时
					else if (obj instanceof Double)
						cell.setCellValue((Double) obj);
					// 其它数据类型都当作字符串简单处理
					else {
						if (obj != null) {
							cell.setCellValue(ObjectUtils.toString(obj));
						}
					}
					j++;
				}
			}

			if (sumData != null) {
				int rowIndex = list.size();
				HSSFRow sumRow = sheet.getRow(rowIndex);
				HSSFCell sumCell = sumRow.getCell(0);
				sumCell.setCellStyle(titleStyle);
				sumCell.setCellValue("合计");
			}
			return wb;
		} catch (Exception e) {
			Logs.error("生成excel2003异常：" + Logs.toLog(e));
			throw new MyException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "生成excel2003异常：" + e.getMessage());
		}
	}

	/**
	 * 
	 * @Title:createExcel
	 * @Description: TODO(生成excel)
	 * @param list 导出的数据
	 * @param cls list里的实体class
	 * @param sumData sumData合计数据
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <Q> XSSFWorkbook exportExcel2007(List<Q> list, Class<Q> cls, Q sumData) {
		XSSFWorkbook wb = new XSSFWorkbook();
		Field[] fields = cls.getDeclaredFields();
		ArrayList<String> headList = new ArrayList<String>();

		try {
			// 添加合计数据
			if (sumData != null) {
				list.add(sumData);
			}

			for (Field f : fields) {
				ExcelField field = f.getAnnotation(ExcelField.class);
				if (field != null) {
					headList.add(field.title());
				}
			}

			XSSFCellStyle titleStyle = getTitleStyle2007(wb);
			XSSFCellStyle cellStyle = getCellStyle2007(wb);
			XSSFSheet sheet = wb.createSheet();
			// 设置Excel表的第一行即表头
			XSSFRow row = sheet.createRow(0);
			for (int i = 0; i < headList.size(); i++) {
				XSSFCell headCell = row.createCell(i);
				// 设置表头样式
				headCell.setCellStyle(titleStyle);
				headCell.setCellValue(new XSSFRichTextString(headList.get(i)));
				// 设置单元格自适应
				sheet.autoSizeColumn((short) i);
				sheet.setColumnWidth(0, 15 * 256);
			}

			for (int i = 0; i < list.size(); i++) {
				XSSFRow rowdata = sheet.createRow(i + 1);// 创建数据行
				Q q = list.get(i);
				Field[] ff = q.getClass().getDeclaredFields();
				int j = 0;
				for (Field f : ff) {
					ExcelField field = f.getAnnotation(ExcelField.class);
					if (field == null) {
						continue;
					}
					f.setAccessible(true);
					Object obj = f.get(q);
					XSSFCell cell = rowdata.createCell(j);
					cell.setCellStyle(cellStyle);

					// 当数字时
					if (obj instanceof Integer) {
						cell.setCellValue((Integer) obj);
						// 将序号替换为123456
						if (j == 0)
							cell.setCellValue(i + 1);
					}
					// 当为Long时
					else if (obj instanceof Long)
						cell.setCellValue((Long) obj);
					// 当为字符串时
					else if (obj instanceof String)
						cell.setCellValue((String) obj);
					// 当为布尔时
					else if (obj instanceof Boolean)
						cell.setCellValue((Boolean) obj);
					// 当为时间时
					else if (obj instanceof Date)
						cell.setCellValue(DateUtils.formatDate((Date) obj, DateUtils.parsePatterns[2]));
					// 当为时间时
					else if (obj instanceof Calendar)
						cell.setCellValue((Calendar) obj);
					// 当为小数时
					else if (obj instanceof Double)
						cell.setCellValue((Double) obj);
					// 其它数据类型都当作字符串简单处理
					else {
						if (obj != null) {
							cell.setCellValue(ObjectUtils.toString(obj));
						}
					}
					j++;
				}
			}

			if (sumData != null) {
				int rowIndex = list.size();
				XSSFRow sumRow = sheet.getRow(rowIndex);
				XSSFCell sumCell = sumRow.getCell(0);
				sumCell.setCellStyle(titleStyle);
				sumCell.setCellValue("合计");
			}
			return wb;
		} catch (Exception e) {
			Logs.error("生成excel2007异常：" + Logs.toLog(e));
			throw new MyException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "生成excel2007异常：" + e.getMessage());
		}
	}

	/**
	 * 
	 * @Title:getCellStyle
	 * @Description: TODO(表头样式)
	 * @param wb
	 * @return
	 */
	private static XSSFCellStyle getTitleStyle2007(XSSFWorkbook wb) {
		XSSFCellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(new XSSFColor(java.awt.Color.gray));
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.SOLID_FOREGROUND); // 让单元格居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 左右居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 上下居中
		style.setWrapText(true); // 设置自动换行
		XSSFFont font = wb.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setColor(new XSSFColor(java.awt.Color.BLACK));
		font.setFontHeightInPoints((short) 12);
		style.setFont(font);
		return style;
	}

	/**
	 * 
	 * @Title:getCellStyle
	 * @Description: TODO(单元格样式)
	 * @param wb
	 * @return
	 */
	private static XSSFCellStyle getCellStyle2007(XSSFWorkbook wb) {
		XSSFCellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(new XSSFColor(java.awt.Color.WHITE));
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		XSSFFont font = wb.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
		style.setFont(font);
		return style;
	}
	
	/**
	 * 
	 * @Title:getTitleStyle2003
	 * @Description: TODO(表头样式)
	 * @param wb
	 * @return
	 */
	private static HSSFCellStyle getTitleStyle2003(HSSFWorkbook wb) {
		HSSFCellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setColor(HSSFColor.WHITE.index);
		font.setFontHeightInPoints((short) 11);
		style.setFont(font);
		return style;
	}
	
	/**
	 * 
	 * @Title:getCellStyle2003
	 * @Description: TODO(单元格样式)
	 * @param wb
	 * @return
	 */
	private static HSSFCellStyle getCellStyle2003(HSSFWorkbook wb) {
		HSSFCellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(HSSFColor.WHITE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		style.setFont(font);
		return style;
	}

	/**
	 * 
	 * @Title:getExcelName
	 * @Description: TODO(转化为excel名称)
	 * @param filename
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getExcelName(String filename) throws UnsupportedEncodingException {
		String excelName = StringUtils.join(filename, ".xlsx");
		return URLEncoder.encode(excelName, WebUtils.UTF_ENCODING);
	}
	
	/**
	 * 
	 * @Title:writeExcel
	 * @Description: TODO(导出)
	 * @param response
	 * @param fileName
	 * @param wb
	 * @throws IOException
	 */
	public static void writeExcel(String fileName, HttpServletResponse response, Workbook wb) {
		response.setContentType("application/vnd.ms-excel");
		OutputStream ouputStream = null;
		try {
			response.setHeader("Content-disposition", "attachment; filename=" + 
					URLEncoder.encode(fileName, WebUtils.UTF_ENCODING));
			ouputStream = response.getOutputStream();
			wb.write(ouputStream);
		} catch (Exception e) {
			Logs.error("导出报表异常：" + Logs.toLog(e));
			throw new MyException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "导出报表异常：" + e.getMessage());
		} finally {
			try {
				ouputStream.close();
			} catch (Exception e2) {
				Logs.error("关闭文件流异常：" + Logs.toLog(e2));
			}
		}
	}
	
	/**
	 * 
	 * @Title：toBase64
	 * @Description: TODO(转base64)
	 * @see：
	 * @param workbook
	 * @return
	 * @throws IOException 
	 */
	public static String toBase64(Workbook workbook) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			workbook.write(os);
			byte[] bytes = os.toByteArray();
			return FileUtils.byte2Base64StringFun(bytes);
		} catch (Exception e) {
			Logs.error("字节流转base64异常：" + Logs.toLog(e));
			throw new MyException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "字节流转base64异常：" + Logs.toLog(e));
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				Logs.error("关闭字节输出流异常：" + Logs.toLog(e));
			}
		}
	}
	
	/**
	 * 
	 * @Title:main
	 * @Description: TODO(测试)
	 * @param args
	 * @throws FileNotFoundException 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<AgentCallData> agentCallDatas = new ArrayList<>();
		agentCallDatas.add(new AgentCallData("123", "1", "张舟", "152", "", "5117", "15058041631", "main", true, 
				new Date(), null));
		
		HSSFWorkbook firstWb = (HSSFWorkbook) ExcelUtil.exportExcel(agentCallDatas, AgentCallData.class, null, EXCEL_FILE_2003);
		firstWb.write(new FileOutputStream("D:/TEST.xls"));
		
		XSSFWorkbook secondWb = (XSSFWorkbook) ExcelUtil.exportExcel(agentCallDatas, AgentCallData.class, null, EXCEl_FILE_2007);
		secondWb.write(new FileOutputStream("D:/TEST.xlsx"));
		
	}
}
