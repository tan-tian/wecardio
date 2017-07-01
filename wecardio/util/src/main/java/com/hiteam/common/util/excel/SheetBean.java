package com.hiteam.common.util.excel;

import org.apache.poi.hssf.model.InternalWorkbook;

import java.util.List;

public class SheetBean{

    private String sheetName;

    public List<List<Object>> getContent() {
        return null;
    }

    public SheetBean getSheet() {
        return new SheetBean();
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }
}