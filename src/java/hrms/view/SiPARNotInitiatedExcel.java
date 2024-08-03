/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.view;

import hrms.model.parmast.ParAdminProperties;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractExcelView;

/**
 *
 * @author Manisha
 */
public class SiPARNotInitiatedExcel extends AbstractExcelView {

    protected void buildExcelDocument(Map<String, Object> model,
            HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ArrayList employeeListNotInitiated = (ArrayList) model.get("employeeListNotInitiated");
        System.out.println("Inside view excel __________________");
        response.setHeader("Content-Disposition", "attachment; filename=\"SIPARNotInitiatedList.xls\"");
        HSSFSheet sheet = workbook.createSheet("SI PAR Not Initiated List");
        sheet.setDefaultColumnWidth(30);
        
        CellStyle style1 = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font font1 = workbook.createFont();
        font1.setFontName("Arial");
        font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font1.setFontHeightInPoints((short) 20);
        style1.setFont(font1);
        style1.setAlignment(CellStyle.ALIGN_CENTER);
        style1.setWrapText(true);
        style1.setBorderTop(CellStyle.BORDER_THIN);
        style1.setBorderRight(CellStyle.BORDER_THIN);
        style1.setBorderBottom(CellStyle.BORDER_THIN);
        style1.setBorderLeft(CellStyle.BORDER_THIN);

        CellStyle style = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 9);
        style.setFont(font);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);

        int row = 0;

        HSSFRow header = sheet.createRow(row);

        header.createCell(0).setCellValue("SI PAR Not Initiated List");
        header.getCell(0).setCellStyle(style1);

        row++;
        HSSFRow header1 = sheet.createRow(row);
        header1.createCell(0).setCellValue("Sl No");
        header1.getCell(0).setCellStyle(style);

        header1.createCell(1).setCellValue("Employee ID");
        header1.getCell(1).setCellStyle(style);

        header1.createCell(2).setCellValue("GPF No");
        header1.getCell(2).setCellStyle(style);

        header1.createCell(3).setCellValue("Employee Name");
        header1.getCell(3).setCellStyle(style);

        header1.createCell(4).setCellValue("Designation");
        header1.getCell(4).setCellStyle(style);

        header1.createCell(5).setCellValue("Date Of Birth");
        header1.getCell(5).setCellStyle(style);

        header1.createCell(6).setCellValue("Group");
        header1.getCell(6).setCellStyle(style);

        header1.createCell(7).setCellValue("Cadre");
        header1.getCell(7).setCellStyle(style);

        header1.createCell(8).setCellValue("Current Office");
        header1.getCell(8).setCellStyle(style);

        row++;
        for (int i = 0; i < employeeListNotInitiated.size(); i++) {
            ParAdminProperties parAdminProperties = (ParAdminProperties) employeeListNotInitiated.get(i);
            HSSFRow row1 = sheet.createRow(row++);
            row1.createCell(0).setCellValue(i + 1);
            row1.getCell(0).setCellStyle(style);
            row1.createCell(1).setCellValue(parAdminProperties.getEmpId());
            row1.getCell(1).setCellStyle(style);
            row1.createCell(2).setCellValue(parAdminProperties.getGpfno());
            row1.getCell(2).setCellStyle(style);
            row1.createCell(3).setCellValue(parAdminProperties.getEmpName());
            row1.getCell(3).setCellStyle(style);
            row1.createCell(4).setCellValue(parAdminProperties.getPostName());
            row1.getCell(4).setCellStyle(style);
            row1.createCell(5).setCellValue(parAdminProperties.getDob());
            row1.getCell(5).setCellStyle(style);
            row1.createCell(6).setCellValue(parAdminProperties.getGroupName());
            row1.getCell(6).setCellStyle(style);
            row1.createCell(7).setCellValue(parAdminProperties.getCadreName());
            row1.getCell(7).setCellStyle(style);
            row1.createCell(8).setCellValue(parAdminProperties.getCurrentOfficeName());
            row1.getCell(8).setCellStyle(style);
            

        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
        sheet.autoSizeColumn(7);
        sheet.autoSizeColumn(8);

        int firstRow = 0;
        int lastRow = 0;
        int firstCol = 0;
        int lastCol = 10;

        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 10));

    }
}
