/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.view;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.model.parmast.ParAdminProperties;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.web.servlet.view.document.AbstractExcelView;

/**
 *
 * @author Manisha
 */
public class PendingPARAtAuthorityExcel extends AbstractExcelView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
            HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ArrayList pendingparList = (ArrayList) model.get("pendingparList");

        response.setHeader("Content-Disposition", "attachment; filename=\"PARList.xls\"");
        HSSFSheet sheet = workbook.createSheet("PAR List");
        sheet.setDefaultColumnWidth(30);

        CellStyle style = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);

        HSSFRow header = sheet.createRow(1);

        header.createCell(0).setCellValue("Sl No");
        header.getCell(0).setCellStyle(style);

        header.createCell(1).setCellValue("Employee ID");
        header.getCell(1).setCellStyle(style);

        header.createCell(2).setCellValue("GPF No");
        header.getCell(2).setCellStyle(style);

        header.createCell(3).setCellValue("Employee Name");
        header.getCell(3).setCellStyle(style);

        header.createCell(4).setCellValue("Designation");
        header.getCell(4).setCellStyle(style);

        header.createCell(5).setCellValue("Date Of Birth");
        header.getCell(5).setCellStyle(style);

        header.createCell(6).setCellValue("Group");
        header.getCell(6).setCellStyle(style);

        header.createCell(7).setCellValue("Cadre");
        header.getCell(7).setCellStyle(style);

        header.createCell(8).setCellValue("Current Office");
        header.getCell(8).setCellStyle(style);
        
        header.createCell(9).setCellValue("Par Period From");
        header.getCell(9).setCellStyle(style);
        
        header.createCell(10).setCellValue("Par Period To");
        header.getCell(10).setCellStyle(style);

        header.createCell(11).setCellValue("Status");
        header.getCell(11).setCellStyle(style);
        
        header.createCell(12).setCellValue("Authority Name");
        header.getCell(12).setCellStyle(style);
        
        header.createCell(13).setCellValue("Authority Designation");
        header.getCell(13).setCellStyle(style);

        int rowNum = 2;
        for (int i = 0; i < pendingparList.size(); i++) {
            ParAdminProperties parAdminProperties = (ParAdminProperties) pendingparList.get(i);
            HSSFRow row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(parAdminProperties.getEmpId());
            row.createCell(2).setCellValue(parAdminProperties.getGpfno());
            row.createCell(3).setCellValue(parAdminProperties.getEmpName());
            row.createCell(4).setCellValue(parAdminProperties.getPostName());
            row.createCell(5).setCellValue(parAdminProperties.getDob());
            row.createCell(6).setCellValue(parAdminProperties.getGroupName());
            row.createCell(7).setCellValue(parAdminProperties.getCadreName());
            row.createCell(8).setCellValue(parAdminProperties.getCurrentOfficeName());
            row.createCell(9).setCellValue(parAdminProperties.getPrdFrmDate());
            row.createCell(10).setCellValue(parAdminProperties.getPrdToDate());
            row.createCell(11).setCellValue(parAdminProperties.getParstatus());
            row.createCell(12).setCellValue(parAdminProperties.getPendingAtAuthName()); 
            row.createCell(13).setCellValue(parAdminProperties.getPendingAtSpc());
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
        sheet.autoSizeColumn(9);
        sheet.autoSizeColumn(10);
        sheet.autoSizeColumn(11);
        

    }

}
