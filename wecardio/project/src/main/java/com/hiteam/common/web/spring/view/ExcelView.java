package com.hiteam.common.web.spring.view;

import com.hiteam.common.util.Multimedia;
import com.hiteam.common.util.json.JsonUtils;
import com.hiteam.common.web.WebUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Excel视图
 */
public class ExcelView extends AbstractExcelView {

	private Logger logger = LoggerFactory.getLogger(ExcelView.class);
	
    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	logger.info("springmvc - AbstractExcelView" + model);
    	HSSFSheet sheet = workbook.createSheet(MapUtils.getString(model, "sheets")); // 创建sheet页
        sheet.setDefaultColumnWidth(12);// 设置默认列宽

        // 创建表头行
        String[] heads = (String[]) model.get("heads");
        this.createHeadRow(sheet, heads);

        // 写入数据行
        String[] columns = (String[]) model.get("columns");
        String data = (String) model.get("datas");
        List dataList = JsonUtils.toObject(data, List.class);
        this.createDataRow(sheet, columns, dataList);

        // 导出文件名称
        String fileName = "data.xls";
        if (model.get("fileName") != null) {
            fileName = (String) model.get("fileName");
        }

        // 设置MIME类型
        response.setContentType(Multimedia.MIME_TYPES.get("xls") + "; charset=UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + WebUtil.encodingFileName(fileName, request));

        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        //outputStream.close();
    }



    /**
     * 创建Excel表头行
     * @param sheet sheet页
     * @param heads 表头名称数组
     */
    private void createHeadRow(HSSFSheet sheet, String[] heads) {
        Row headRow = sheet.createRow(0);
        headRow.setHeight((short) (18 * 20));
        int colNum = 0; // 列序号
        CellStyle headStyle = createHeadStyle(sheet.getWorkbook());
        for (String head : heads) {
            Cell cell = headRow.createCell(colNum);
            cell.setCellValue(head);
            cell.setCellStyle(headStyle);
            colNum++;
        }
    }

    /**
     * 创建数据行
     * @param sheet    sheet页
     * @param columns  字段数组
     * @param dataList 数据列表
     */
    private void createDataRow(HSSFSheet sheet, String[] columns, List dataList) {
        int rowNum = 1;
        CellStyle dateStyle = this.createDateStyle(sheet.getWorkbook());
        for (Object o : dataList) {
            Map<String, Object> data = (Map<String, Object>) o;
            Row dataRow = sheet.createRow(rowNum);

            int colNum = 0;
            for (String column : columns) {
                Cell cell = dataRow.createCell(colNum);
                Object value;
                if ("rowid".equals(column)) {
                    value = rowNum;
                } else {
                    value = data.get(column);
                }
                if (value != null) {
                    if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else if (value instanceof Date) {
                        // 日期格式未验证
                        cell.setCellValue(((Date) value));
                        cell.setCellStyle(dateStyle);
                    } else {
                        cell.setCellValue(String.valueOf(value));
                    }
                } else {
                    cell.setCellValue("");
                }
                colNum++;
            }
            rowNum++;
        }
    }

    /**
     * 创建Excel表头样式
     * @param workbook 工作表
     * @return Excel单元格样式
     */
    private CellStyle createHeadStyle(HSSFWorkbook workbook) {
        Font warnfont = workbook.createFont();
        warnfont.setFontHeightInPoints((short) 12);
        warnfont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        CellStyle headStyle = workbook.createCellStyle();
        headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
        headStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        headStyle.setBorderRight(CellStyle.BORDER_THIN);
        headStyle.setFont(warnfont);
        headStyle.setWrapText(true);
        return headStyle;
    }

    /**
     * 创建Excel日期样式
     * @param workbook 工作表
     * @return Excel单元格样式
     */
    private CellStyle createDateStyle(HSSFWorkbook workbook) {
        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
        return dateStyle;
    }
}
