package com.hiteam.common.util.convert;

import com.hiteam.common.util.spring.SpringUtils;
import org.apache.commons.io.FilenameUtils;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.document.DocumentFamily;
import org.artofsolving.jodconverter.document.DocumentFormat;
import org.artofsolving.jodconverter.document.SimpleDocumentFormatRegistry;
import org.artofsolving.jodconverter.office.OfficeManager;

import java.io.File;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 文档转换工具类
 * Created by tantian on 2014/11/1.
 */
public class JodConverterUtils {

    private JodConverterUtils() {}

    /**
     * 文档转换
     * @param inputPath  源路径
     * @param outputPath 目标路径
     * @throws Exception
     */
    public static void fileConverter(String inputPath, String outputPath) throws Exception {
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        fileConverter(inputFile, outputFile);
    }

    /**
     * 文档转换
     * @param inputFile  源路径
     * @param outputFile 目标路径
     * @throws Exception
     */
    public static void fileConverter(File inputFile, File outputFile) throws Exception {
        OfficeManagerService officeManagerService = SpringUtils.getBean("officeManagerService");
        OfficeManager officeManager = officeManagerService.getOfficeManager();
        OfficeDocumentConverter converter;

        String inputExt = FilenameUtils.getExtension(inputFile.getName());
        if ("txt".equals(inputExt.toLowerCase())) {
            SimpleDocumentFormatRegistry sdfr = new SimpleDocumentFormatRegistry();

            DocumentFormat txt = new DocumentFormat("Plain Text", "txt", "text/plain");
            txt.setInputFamily(DocumentFamily.TEXT);
            Map<String, Object> txtLoadProperties = new LinkedHashMap<String, Object>();
            txtLoadProperties.put("Hidden", true);
            txtLoadProperties.put("ReadOnly", true);
            txtLoadProperties.put("FilterName", "Text (encoded)");
            txtLoadProperties.put("FilterOptions", "gb_2312");
            txt.setLoadProperties(txtLoadProperties);
            txt.setStoreProperties(DocumentFamily.TEXT, txtLoadProperties);
            sdfr.addFormat(txt);

            DocumentFormat pdf = new DocumentFormat("Portable Document Format", "pdf", "application/pdf");
            pdf.setStoreProperties(DocumentFamily.TEXT, Collections.singletonMap("FilterName", "writer_pdf_Export"));
            pdf.setStoreProperties(DocumentFamily.SPREADSHEET, Collections.singletonMap("FilterName", "calc_pdf_Export"));
            pdf.setStoreProperties(DocumentFamily.PRESENTATION, Collections.singletonMap("FilterName", "impress_pdf_Export"));
            pdf.setStoreProperties(DocumentFamily.DRAWING, Collections.singletonMap("FilterName", "draw_pdf_Export"));
            sdfr.addFormat(pdf);

            converter = new OfficeDocumentConverter(officeManager, sdfr);
        } else {
            converter = new OfficeDocumentConverter(officeManager);
        }

        converter.convert(inputFile, outputFile);
    }
}
