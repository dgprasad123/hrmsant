/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.view;

import hrms.model.WaterRent;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.web.servlet.view.document.AbstractExcelView;

/**
 *
 * @author Manas
 */
public class WRRScheduleExcel extends AbstractExcelView{
    @Override
    protected void buildExcelDocument(Map<String, Object> model,
            HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        WaterRent waterRent[] = (WaterRent[])model.get("waterRent");
        response.setHeader("Content-Disposition", "attachment; filename=\"employeeList_waterrent.xls\"");
        HSSFSheet sheet = workbook.createSheet("Water Rent");
        sheet.setDefaultColumnWidth(20);

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");        
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        
        
        // create header row
        HSSFRow header = sheet.createRow(1);

        header.createCell(0).setCellValue("Sl No");
        header.getCell(0).setCellStyle(style);
        
        header.createCell(1).setCellValue("Consumer No");
        header.getCell(1).setCellStyle(style);
        
        header.createCell(2).setCellValue("HRMS ID");
        header.getCell(2).setCellStyle(style);
        
        header.createCell(3).setCellValue("Month");
        header.getCell(3).setCellStyle(style);
        
        header.createCell(4).setCellValue("Year");
        header.getCell(4).setCellStyle(style);
        
        header.createCell(5).setCellValue("Employee Name");
        header.getCell(5).setCellStyle(style);               
        
        header.createCell(6).setCellValue("QRS No");
        header.getCell(6).setCellStyle(style);
        
        header.createCell(7).setCellValue("QRS Type");
        header.getCell(7).setCellStyle(style);
        
        header.createCell(8).setCellValue("Unit/Area");
        header.getCell(8).setCellStyle(style);
        
        header.createCell(9).setCellValue("DDO Code");
        header.getCell(9).setCellStyle(style);
        
        header.createCell(10).setCellValue("Office Name");
        header.getCell(10).setCellStyle(style);
        
        header.createCell(11).setCellValue("TV No");
        header.getCell(11).setCellStyle(style);
        
        header.createCell(12).setCellValue("TV Date");
        header.getCell(12).setCellStyle(style);
        
        header.createCell(13).setCellValue("Water Tax");
        header.getCell(13).setCellStyle(style);
        
        header.createCell(14).setCellValue("Swerage Tax");
        header.getCell(14).setCellStyle(style);
        
        header.createCell(15).setCellValue("Date of Allotment");
        header.getCell(15).setCellStyle(style);
        
        
        
        // create header row
        
        int rowNum = 2;
        for (int i = 0; i < waterRent.length; i++) {
            WaterRent wrr = waterRent[i];
            HSSFRow row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(i+1);
            row.createCell(1).setCellValue(wrr.getConsumerNo());
            row.createCell(2).setCellValue(wrr.getHrmsid());
            row.createCell(3).setCellValue(wrr.getRecoverymonth());
            row.createCell(4).setCellValue(wrr.getRecoveryyear());            
            row.createCell(5).setCellValue(wrr.getFullName());
            row.createCell(6).setCellValue(wrr.getQuarterNo());
            row.createCell(7).setCellValue(wrr.getQrtrtype());
            row.createCell(8).setCellValue(wrr.getQrtrunit());
            row.createCell(9).setCellValue(wrr.getDdocode());
            row.createCell(10).setCellValue(wrr.getOfficename());
            row.createCell(11).setCellValue(wrr.getTvno());
            row.createCell(12).setCellValue(wrr.getTvdate());            
            row.createCell(13).setCellValue(wrr.getWtax());
            row.createCell(14).setCellValue(wrr.getSwtax());
            row.createCell(15).setCellValue(wrr.getDateofallotment());            
        }
        sheet.autoSizeColumn(0);
    }
}
