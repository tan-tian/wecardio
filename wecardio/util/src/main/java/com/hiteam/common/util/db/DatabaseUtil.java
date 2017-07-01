package com.hiteam.common.util.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.hiteam.common.util.excel.ExcelBean;
import com.hiteam.common.util.excel.ExcelUtils;
import com.hiteam.common.util.excel.SheetBean;
import com.hiteam.common.util.xml.XstreamUtil;

/**
 * 数据库工具类
 *
 */
public class DatabaseUtil {

	/**
	 * 保留字段名称列表
	 */
	private static List<String> includeAttrs = Arrays.asList(new String[] {
			"Name", "Code", "Comment", "DataType", "Length", "Precision" });

	/**
	 * 解析Excel文件为表定义
	 * 
	 * @param inputStream
	 *            文件流
	 * @param tableName
	 *            数据库表名
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static TableDef parseTableDefFromExcel(InputStream inputStream,
			String tableName) throws InvalidFormatException, IOException {
		TableDef tableDef = new TableDef();
		tableDef.setCode(tableName);

		ExcelBean excelBean;
		excelBean = ExcelBean.create(inputStream);
		SheetBean sheetBean = excelBean.getSheetBean(0);
		tableDef.setName(sheetBean.getSheet().getSheetName());

		List<List<Object>> content = sheetBean.getContent();
		List<Map<Object, Object>> columnDefines = ExcelUtils
				.getListMapByListList(content);

		for (Map<Object, Object> columnDefine : columnDefines) {
			ColumnDef column = new ColumnDef();
			column.setCode((String) columnDefine.get("Code"));
			column.setName((String) columnDefine.get("Name"));
			column.setComment((String) columnDefine.get("Comment"));
			column.setDataType((String) columnDefine.get("Data Type"));
			column.setLength((Integer) columnDefine.get("Length"));
			column.setPrecision((Integer) columnDefine.get("Precision"));

			tableDef.getColumns().add(column);
		}

		return tableDef;
	}

	/**
	 * 解析pdm文件为表定义
	 * 
	 * @param inputStream
	 *            文件流
	 * @param tableName
	 *            数据库表名
	 * @return
	 * @throws IOException
	 */
	public static TableDef parseTableDefFromPdm(InputStream inputStream,
			String tableName) throws IOException {
		String pdmContent = IOUtils.toString(inputStream, "UTF-8").replaceAll(
				"\n|\r", "");// 正则表达式默认是单行模式

		TableDef tableDef = null;
		Pattern pattern = Pattern.compile("<o:Table.*?</o:Table>");
		Matcher m = pattern.matcher(pdmContent);
		while (m.find()) {
			String token = m.group();
			int tableIndex = token.lastIndexOf("<o:Table");
			token = token.substring(tableIndex);// 排除<o:Table*/>情况

			int codeIndex = token.indexOf("<a:Code>" + tableName + "</a:Code>");
			if (codeIndex != -1) {// 过滤出指定数据库表部分
				int columnsIndex = token.indexOf("<c:Columns>");
				if (columnsIndex == -1 || columnsIndex > codeIndex) {// 防止列名和指定表名一致的情况
					tableDef = XstreamUtil
							.toBean(handleMarkerA(handleMarkerC(token)),
									TableDef.class);
					break;
				}
			}
		}

		if (tableDef == null) {
			throw new RuntimeException("找不到表【" + tableName + "】的定义");
		}

		return tableDef;
	}

	/**
	 * 去除pdm中多余的a类型标记属性
	 * 
	 * @param xml
	 * @return
	 */
	private static String handleMarkerA(String xml) {
		StringBuffer sb = new StringBuffer();

		Pattern pattern = Pattern.compile("<a:.*?/.*?>");
		Matcher m = pattern.matcher(xml);
		while (m.find()) {
			String token = m.group();
			int index = token.indexOf(">");
			String name = token.substring(3, index);
			// 仅保留需要的属性，因为xstream遇到不可识别属性会报错
			if (includeAttrs.contains(name)) {
				m.appendReplacement(sb, token);
			} else {
				m.appendReplacement(sb, "");
			}
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 去除pdm中多余的c类型标记属性
	 * 
	 * @param xml
	 * @return
	 */
	private static String handleMarkerC(String xml) {
		StringBuffer sb = new StringBuffer();

		Pattern pattern = Pattern
				.compile("<c:(Columns|Keys|PrimaryKey|ClusterObject).*?</c:(Columns|Keys|PrimaryKey|ClusterObject)>");
		Matcher m = pattern.matcher(xml);
		while (m.find()) {
			String token = m.group();
			int index = token.indexOf(">");
			String name = token.substring(3, index);
			// 过滤掉非列定义部分
			if ("Columns".equals(name)) {
				m.appendReplacement(sb, token);
			} else {
				m.appendReplacement(sb, "");
			}
		}
		m.appendTail(sb);
		return sb.toString();
	}

}
