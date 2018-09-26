package com.motorcli.springboot.web.view;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

public abstract class ExcelView extends AbstractView {

    private static final String CONTENT_TYPE = "application/vnd.ms-excel;charset=UTF-8";
    protected final String XLS = ".xls";

    protected final String XLSX = ".xlsx";
    private File file;
    private String fileName;

    public ExcelView() {
        setContentType("application/vnd.ms-excel;charset=UTF-8");
    }

    public void setFile(File file) {
        this.file = file;
    }

    protected boolean generatesDownloadContent() {
        return true;
    }

    protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Workbook workbook = null;
        String extension = getExtension();
        if (extension.equals(".xls")) {
            if (this.file != null)
                workbook = new HSSFWorkbook(new FileInputStream(this.file));
            else
                workbook = new HSSFWorkbook();
        } else if (extension.equals(".xlsx")) {
            if (this.file != null)
                workbook = new XSSFWorkbook(new FileInputStream(this.file));
            else {
                workbook = new XSSFWorkbook();
            }
        }

        buildExcelDocument(model, workbook, request, response);

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=" + getFileName() + getExtension());
        response.setContentType(getContentType());

        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        if (this.file != null)
            this.file.delete();
    }

    protected String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    protected abstract void buildExcelDocument(Map<String, Object> params, Workbook workbook, HttpServletRequest request, HttpServletResponse response);

    protected Cell getCell(Sheet sheet, int row, int col) {
        Row sheetRow = sheet.getRow(row);
        if (sheetRow == null) {
            sheetRow = sheet.createRow(row);
        }
        Cell cell = sheetRow.getCell(col);
        if (cell == null) {
            cell = sheetRow.createCell(col);
        }
        return cell;
    }

    protected void setText(Cell cell, String text) {
        cell.setCellType(CellType.STRING);
        cell.setCellValue(text);
    }

    protected String getExtension() {
        if (this.file == null) {
            return XLSX;
        }
        String extension = "." + this.file.getName().substring(this.file.getName().lastIndexOf(".") + 1);
        return extension.toLowerCase();
    }
}
