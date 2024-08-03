/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.view;

import hrms.common.CalendarCommonMethods;
import hrms.model.payroll.AnnualIncomeTax;
import hrms.model.payroll.tpfschedule.AnnualIncomeTaxDetail;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.web.servlet.view.document.AbstractExcelView;

/**
 *
 * @author Manas
 */
public class AnnualIncomeTaxReport extends AbstractExcelView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        int row = 0;
        AnnualIncomeTax annualIncomeTaxData = (AnnualIncomeTax) model.get("annualIncomeTaxData");
        String empId = request.getParameter("empid");
        String fiscalYear = request.getParameter("fyear");
        String year1 = "";
        String year2 = "";
        if (fiscalYear != null) {
            String str[] = fiscalYear.split("-");
            year1 = str[1];
            year2 = str[0];
        }
        String fileName = "INCOME_TAX_DATA_" + empId + "_" + fiscalYear + ".xls";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        HSSFSheet sheet = workbook.createSheet("Annual Income");

        /**
         * ************Cell Formatting**************
         */
        CellStyle headformat = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 10);
        headformat.setFont(font);
        headformat.setAlignment(CellStyle.ALIGN_CENTER);

        CellStyle headcell2 = workbook.createCellStyle();
        headcell2.setFont(font);

        CellStyle headcell = workbook.createCellStyle();
        headcell.setFont(font);
        headcell.setAlignment(CellStyle.ALIGN_CENTER);
        headcell.setWrapText(true);
        headcell.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headcell.setBorderBottom(CellStyle.BORDER_THIN);
        headcell.setBorderTop(CellStyle.BORDER_THIN);
        headcell.setBorderLeft(CellStyle.BORDER_THIN);
        headcell.setBorderRight(CellStyle.BORDER_THIN);

        CellStyle innercell = workbook.createCellStyle();

        /**
         * ************Cell Formatting**************
         */
        HSSFRow header = sheet.createRow(row);
        header.createCell(1).setCellValue("ANNUAL INCOME TAX REPORT");
        header.getCell(1).setCellStyle(headformat);
        sheet.addMergedRegion(new CellRangeAddress(row, row, 1, 15));

        row++;
        header = sheet.createRow(row);
        header.createCell(1).setCellValue("For the Year " + year2 + "-" + year1);
        header.getCell(1).setCellStyle(headformat);
        sheet.addMergedRegion(new CellRangeAddress(row, row, 1, 15));

        row++;
        header = sheet.createRow(row);
        header.createCell(1).setCellValue("(1)  Account Number  :");
        header.getCell(1).setCellStyle(headcell2);
        sheet.addMergedRegion(new CellRangeAddress(row, row, 1, 3));

        header.createCell(4).setCellValue(annualIncomeTaxData.getAccountnumber());
        header.getCell(4).setCellStyle(headcell2);
        sheet.addMergedRegion(new CellRangeAddress(row, row, 4, 15));

        row++;
        header = sheet.createRow(row);
        header.createCell(1).setCellValue("(2)  Name            :");
        header.getCell(1).setCellStyle(headcell2);
        sheet.addMergedRegion(new CellRangeAddress(row, row, 1, 3));

        header.createCell(4).setCellValue(annualIncomeTaxData.getName());
        header.getCell(4).setCellStyle(headcell2);
        sheet.addMergedRegion(new CellRangeAddress(row, row, 4, 15));

        row++;
        header = sheet.createRow(row);
        header.createCell(1).setCellValue("(3)  Designation     :");
        header.getCell(1).setCellStyle(headcell2);
        sheet.addMergedRegion(new CellRangeAddress(row, row, 1, 3));

        header.createCell(4).setCellValue(annualIncomeTaxData.getDesignation());
        header.getCell(4).setCellStyle(headcell2);
        sheet.addMergedRegion(new CellRangeAddress(row, row, 4, 15));

        row++;
        header = sheet.createRow(row);
        header.createCell(1).setCellValue("(4)  Pan card Number :");
        header.getCell(1).setCellStyle(headcell2);
        sheet.addMergedRegion(new CellRangeAddress(row, row, 1, 3));

        header.createCell(4).setCellValue(annualIncomeTaxData.getPancard());
        header.getCell(4).setCellStyle(headcell2);
        sheet.addMergedRegion(new CellRangeAddress(row, row, 4, 15));

        /**
         * ********************************************************************
         **********************Detail Header Start*****************************
         * *******************************************************************
         */
        row++;
        HSSFRow slnoheader = sheet.createRow(row + 1);
        header = sheet.createRow(row);
        header.createCell(1).setCellValue("Month");
        header.getCell(1).setCellStyle(headcell);
        slnoheader.createCell(1).setCellValue(1);
        slnoheader.getCell(1).setCellStyle(headcell);

        header.createCell(2).setCellValue("Bill No");
        header.getCell(2).setCellStyle(headcell);
        slnoheader.createCell(2).setCellValue(2);
        slnoheader.getCell(2).setCellStyle(headcell);

        header.createCell(3).setCellValue("Basic Pay");
        header.getCell(3).setCellStyle(headcell);
        slnoheader.createCell(3).setCellValue(3);
        slnoheader.getCell(3).setCellStyle(headcell);

        header.createCell(4).setCellValue("DP/GP");
        header.getCell(4).setCellStyle(headcell);
        slnoheader.createCell(4).setCellValue(4);
        slnoheader.getCell(4).setCellStyle(headcell);

        header.createCell(5).setCellValue("IR");
        header.getCell(5).setCellStyle(headcell);
        slnoheader.createCell(5).setCellValue(5);
        slnoheader.getCell(5).setCellStyle(headcell);

        header.createCell(6).setCellValue("DA");
        header.getCell(6).setCellStyle(headcell);
        slnoheader.createCell(6).setCellValue(6);
        slnoheader.getCell(6).setCellStyle(headcell);

        header.createCell(7).setCellValue("HRA");
        header.getCell(7).setCellStyle(headcell);
        slnoheader.createCell(7).setCellValue(7);
        slnoheader.getCell(7).setCellStyle(headcell);

        header.createCell(8).setCellValue("OA");
        header.getCell(8).setCellStyle(headcell);
        slnoheader.createCell(8).setCellValue(8);
        slnoheader.getCell(8).setCellStyle(headcell);

        header.createCell(9).setCellValue("OT");
        header.getCell(9).setCellStyle(headcell);
        slnoheader.createCell(9).setCellValue(9);
        slnoheader.getCell(9).setCellStyle(headcell);

        header.createCell(10).setCellValue("GROSS TOTAL");
        header.getCell(10).setCellStyle(headcell);
        slnoheader.createCell(10).setCellValue(10);
        slnoheader.getCell(10).setCellStyle(headcell);

        header.createCell(11).setCellValue("GPF/TPF /CPF");
        header.getCell(11).setCellStyle(headcell);
        slnoheader.createCell(11).setCellValue(11);
        slnoheader.getCell(11).setCellStyle(headcell);

        header.createCell(12).setCellValue("PT");
        header.getCell(12).setCellStyle(headcell);
        slnoheader.createCell(12).setCellValue(12);
        slnoheader.getCell(12).setCellStyle(headcell);

        header.createCell(13).setCellValue("IT");
        header.getCell(13).setCellStyle(headcell);
        slnoheader.createCell(13).setCellValue(13);
        slnoheader.getCell(13).setCellStyle(headcell);

        header.createCell(14).setCellValue("LIC");
        header.getCell(14).setCellStyle(headcell);
        slnoheader.createCell(14).setCellValue(14);
        slnoheader.getCell(14).setCellStyle(headcell);

        header.createCell(15).setCellValue("HRR");
        header.getCell(15).setCellStyle(headcell);
        slnoheader.createCell(15).setCellValue(15);
        slnoheader.getCell(15).setCellStyle(headcell);

        header.createCell(16).setCellValue("WRR");
        header.getCell(16).setCellStyle(headcell);
        slnoheader.createCell(16).setCellValue(16);
        slnoheader.getCell(16).setCellStyle(headcell);

        header.createCell(17).setCellValue("MOPA(P)");
        header.getCell(17).setCellStyle(headcell);
        slnoheader.createCell(17).setCellValue(17);
        slnoheader.getCell(17).setCellStyle(headcell);

        header.createCell(18).setCellValue("MOPA(I)");
        header.getCell(18).setCellStyle(headcell);
        slnoheader.createCell(18).setCellValue(18);
        slnoheader.getCell(18).setCellStyle(headcell);

        header.createCell(19).setCellValue("MCY(P)");
        header.getCell(19).setCellStyle(headcell);
        slnoheader.createCell(19).setCellValue(19);
        slnoheader.getCell(19).setCellStyle(headcell);

        header.createCell(20).setCellValue("MCY(I)");
        header.getCell(20).setCellStyle(headcell);
        slnoheader.createCell(20).setCellValue(20);
        slnoheader.getCell(20).setCellStyle(headcell);

        header.createCell(21).setCellValue("Hiring Charges");
        header.getCell(21).setCellStyle(headcell);
        slnoheader.createCell(21).setCellValue(21);
        slnoheader.getCell(21).setCellStyle(headcell);

        header.createCell(22).setCellValue("Computer Advance");
        header.getCell(22).setCellStyle(headcell);
        slnoheader.createCell(22).setCellValue(22);
        slnoheader.getCell(22).setCellStyle(headcell);

        header.createCell(23).setCellValue("GIS");
        header.getCell(23).setCellStyle(headcell);
        slnoheader.createCell(23).setCellValue(23);
        slnoheader.getCell(23).setCellStyle(headcell);

        header.createCell(24).setCellValue("GPF Advance");
        header.getCell(24).setCellStyle(headcell);
        slnoheader.createCell(24).setCellValue(24);
        slnoheader.getCell(24).setCellStyle(headcell);

        header.createCell(25).setCellValue("FA");
        header.getCell(25).setCellStyle(headcell);
        slnoheader.createCell(25).setCellValue(25);
        slnoheader.getCell(25).setCellStyle(headcell);

        header.createCell(26).setCellValue("HBA(P)");
        header.getCell(26).setCellStyle(headcell);
        slnoheader.createCell(26).setCellValue(26);
        slnoheader.getCell(26).setCellStyle(headcell);

        header.createCell(27).setCellValue("HBA(I)");
        header.getCell(27).setCellStyle(headcell);
        slnoheader.createCell(27).setCellValue(27);
        slnoheader.getCell(27).setCellStyle(headcell);

        header.createCell(28).setCellValue("Loan");
        header.getCell(28).setCellStyle(headcell);
        slnoheader.createCell(28).setCellValue(28);
        slnoheader.getCell(28).setCellStyle(headcell);

        header.createCell(29).setCellValue("Total Deduction");
        header.getCell(29).setCellStyle(headcell);
        slnoheader.createCell(29).setCellValue(29);
        slnoheader.getCell(29).setCellStyle(headcell);

        header.createCell(30).setCellValue("Net");
        header.getCell(30).setCellStyle(headcell);
        slnoheader.createCell(30).setCellValue(30);
        slnoheader.getCell(30).setCellStyle(headcell);

        /**
         * ********************************************************************
         */
        row++;
        ArrayList annualIncomeTaxDetailList = annualIncomeTaxData.getAnnualIncomeTaxDetailList();
        int basicPay = 0;
        for (int i = 0; i < annualIncomeTaxDetailList.size(); i++) {
            row++;
            AnnualIncomeTaxDetail annualIncomeTaxDetail = (AnnualIncomeTaxDetail) annualIncomeTaxDetailList.get(i);
            HSSFRow data = sheet.createRow(row);
            data.createCell(1).setCellValue(CalendarCommonMethods.getFullNameMonthAsString(annualIncomeTaxDetail.getMonth()) + "-" + annualIncomeTaxDetail.getYear());
            data.getCell(1).setCellStyle(innercell);

            data.createCell(2).setCellValue(annualIncomeTaxDetail.getBillDesc());
            data.getCell(2).setCellStyle(innercell);

            data.createCell(3).setCellValue(annualIncomeTaxDetail.getCurbasic());
            data.getCell(3).setCellStyle(innercell);

            data.createCell(4).setCellValue(annualIncomeTaxDetail.getGp());
            data.getCell(4).setCellStyle(innercell);

            data.createCell(5).setCellValue(annualIncomeTaxDetail.getIr());
            data.getCell(5).setCellStyle(innercell);

            data.createCell(6).setCellValue(annualIncomeTaxDetail.getDa());
            data.getCell(6).setCellStyle(innercell);

            data.createCell(7).setCellValue(annualIncomeTaxDetail.getHra());
            data.getCell(7).setCellStyle(innercell);

            data.createCell(8).setCellValue(annualIncomeTaxDetail.getOa());
            data.getCell(8).setCellStyle(innercell);

            data.createCell(9).setCellValue(annualIncomeTaxDetail.getOt());
            data.getCell(9).setCellStyle(innercell);
            
            /*Calculate Gross total of each row*/
            data.createCell(10).setCellType(HSSFCell.CELL_TYPE_FORMULA);
            data.getCell(10).setCellFormula("SUM(D"+(row+1)+":J"+(row+1)+")");
            data.getCell(10).setCellStyle(innercell);
            /*Calculate Gross total of each row*/

            //data.createCell(10).setCellValue(annualIncomeTaxDetail.getGross());
            //data.getCell(10).setCellStyle(innercell);
            data.createCell(11).setCellValue(annualIncomeTaxDetail.getGpf());
            data.getCell(11).setCellStyle(innercell);

            data.createCell(12).setCellValue(annualIncomeTaxDetail.getPt());
            data.getCell(12).setCellStyle(innercell);

            data.createCell(13).setCellValue(annualIncomeTaxDetail.getIt());
            data.getCell(13).setCellStyle(innercell);

            data.createCell(14).setCellValue(annualIncomeTaxDetail.getLic());
            data.getCell(14).setCellStyle(innercell);

            data.createCell(15).setCellValue(annualIncomeTaxDetail.getHrr());
            data.getCell(15).setCellStyle(innercell);

            data.createCell(16).setCellValue(annualIncomeTaxDetail.getWrr());
            data.getCell(16).setCellStyle(innercell);

            data.createCell(17).setCellValue(annualIncomeTaxDetail.getMopaap());
            data.getCell(17).setCellStyle(innercell);

            data.createCell(18).setCellValue(annualIncomeTaxDetail.getMopaai());
            data.getCell(18).setCellStyle(innercell);

            data.createCell(19).setCellValue(annualIncomeTaxDetail.getMcyp());
            data.getCell(19).setCellStyle(innercell);

            data.createCell(20).setCellValue(annualIncomeTaxDetail.getMcyi());
            data.getCell(20).setCellStyle(innercell);

            data.createCell(21).setCellValue(annualIncomeTaxDetail.getHc());
            data.getCell(21).setCellStyle(innercell);

            data.createCell(22).setCellValue(annualIncomeTaxDetail.getCompadv());
            data.getCell(22).setCellStyle(innercell);

            data.createCell(23).setCellValue(annualIncomeTaxDetail.getGis());
            data.getCell(23).setCellStyle(innercell);

            data.createCell(24).setCellValue(annualIncomeTaxDetail.getGpfadv());
            data.getCell(24).setCellStyle(innercell);

            data.createCell(25).setCellValue(annualIncomeTaxDetail.getFa());
            data.getCell(25).setCellStyle(innercell);

            data.createCell(26).setCellValue(annualIncomeTaxDetail.getHbap());
            data.getCell(26).setCellStyle(innercell);

            data.createCell(27).setCellValue(annualIncomeTaxDetail.getHbai());
            data.getCell(27).setCellStyle(innercell);

            data.createCell(28).setCellValue(annualIncomeTaxDetail.getLoan());
            data.getCell(28).setCellStyle(innercell);
            
            /*Calculate Total Deduction of each row*/
            data.createCell(29).setCellType(HSSFCell.CELL_TYPE_FORMULA);
            data.getCell(29).setCellFormula("SUM(L"+(row+1)+":AC" + (row+1) + ")");
            data.getCell(29).setCellStyle(innercell);
            /*Calculate Total Deduction of each row*/
            
            /*Calculate Net pay of each row*/
            data.createCell(30).setCellType(HSSFCell.CELL_TYPE_FORMULA);
            data.getCell(30).setCellFormula("k"+(row+1)+"-AD" + (row+1));
            data.getCell(30).setCellStyle(innercell);
            /*Calculate Net pay of each row*/
        }

        /**
         * ******************Totaling Row***************
         */
        row++;
        HSSFRow data = sheet.createRow(row);
        data.createCell(1).setCellValue("Total");
        data.getCell(1).setCellStyle(innercell);

        data.createCell(3).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(3).setCellFormula("SUM(D9:D" + row + ")");
        data.getCell(3).setCellStyle(innercell);

        data.createCell(4).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(4).setCellFormula("SUM(E9:E" + row + ")");
        data.getCell(4).setCellStyle(innercell);

        data.createCell(5).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(5).setCellFormula("SUM(F9:F" + row + ")");
        data.getCell(5).setCellStyle(innercell);

        data.createCell(6).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(6).setCellFormula("SUM(G9:G" + row + ")");
        data.getCell(6).setCellStyle(innercell);

        data.createCell(7).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(7).setCellFormula("SUM(H9:H" + row + ")");
        data.getCell(7).setCellStyle(innercell);

        data.createCell(8).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(8).setCellFormula("SUM(I9:I" + row + ")");
        data.getCell(8).setCellStyle(innercell);

        data.createCell(9).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(9).setCellFormula("SUM(J9:J" + row + ")");
        data.getCell(9).setCellStyle(innercell);

        data.createCell(10).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(10).setCellFormula("SUM(K9:K" + row + ")");
        data.getCell(10).setCellStyle(innercell);
        
        data.createCell(11).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(11).setCellFormula("SUM(L9:L" + row + ")");
        data.getCell(11).setCellStyle(innercell);
        
        data.createCell(12).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(12).setCellFormula("SUM(M9:M" + row + ")");
        data.getCell(12).setCellStyle(innercell);
        
        data.createCell(13).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(13).setCellFormula("SUM(N9:N" + row + ")");
        data.getCell(13).setCellStyle(innercell);
        
        data.createCell(14).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(14).setCellFormula("SUM(O9:O" + row + ")");
        data.getCell(14).setCellStyle(innercell);
        
        data.createCell(15).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(15).setCellFormula("SUM(P9:P" + row + ")");
        data.getCell(15).setCellStyle(innercell);
        
        data.createCell(16).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(16).setCellFormula("SUM(Q9:Q" + row + ")");
        data.getCell(16).setCellStyle(innercell);
        
        data.createCell(17).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(17).setCellFormula("SUM(R9:R" + row + ")");
        data.getCell(17).setCellStyle(innercell);
        
        data.createCell(18).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(18).setCellFormula("SUM(S9:S" + row + ")");
        data.getCell(18).setCellStyle(innercell);
        
        data.createCell(19).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(19).setCellFormula("SUM(T9:T" + row + ")");
        data.getCell(19).setCellStyle(innercell);
        
        data.createCell(20).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(20).setCellFormula("SUM(U9:U" + row + ")");
        data.getCell(20).setCellStyle(innercell);
        
        data.createCell(21).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(21).setCellFormula("SUM(V9:V" + row + ")");
        data.getCell(21).setCellStyle(innercell);
        
        data.createCell(22).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(22).setCellFormula("SUM(W9:W" + row + ")");
        data.getCell(22).setCellStyle(innercell);
        
        data.createCell(23).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(23).setCellFormula("SUM(X9:X" + row + ")");
        data.getCell(23).setCellStyle(innercell);
        
        data.createCell(24).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(24).setCellFormula("SUM(Y9:Y" + row + ")");
        data.getCell(24).setCellStyle(innercell);
        
        data.createCell(25).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(25).setCellFormula("SUM(Z9:Z" + row + ")");
        data.getCell(25).setCellStyle(innercell);
        
        data.createCell(26).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(26).setCellFormula("SUM(AA9:AA" + row + ")");
        data.getCell(26).setCellStyle(innercell);
        
        data.createCell(27).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(27).setCellFormula("SUM(AB9:AB" + row + ")");
        data.getCell(27).setCellStyle(innercell);

        data.createCell(28).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(28).setCellFormula("SUM(AC9:AC" + row + ")");
        data.getCell(28).setCellStyle(innercell);
        
        data.createCell(29).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(29).setCellFormula("SUM(AD9:AD" + row + ")");
        data.getCell(29).setCellStyle(innercell);
        
        data.createCell(30).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        data.getCell(30).setCellFormula("SUM(AE9:AE" + row + ")");
        data.getCell(30).setCellStyle(innercell);
        /**
         * ******************Totaling Row***************
         */
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
    }

}
