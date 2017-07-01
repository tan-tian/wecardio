package com.hiteam.common.util.excel;

import java.io.InputStream;

public class ExcelBean{

    private InputStream inputStream;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    private ExcelBean(InputStream inputStream){
        this.inputStream=inputStream;
    }
    public static ExcelBean create(InputStream inputStream) {
        return new ExcelBean(inputStream);
    }

    public SheetBean getSheetBean(int i) {
        return new SheetBean();
    }
}