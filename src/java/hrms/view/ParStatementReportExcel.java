/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.view;

import hrms.model.parmast.DepartmentPromotionBean;
import hrms.model.parmast.DepartmentPromotionDetail;
import hrms.model.parmast.FiscalYearWiseParData;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.springframework.web.servlet.view.document.AbstractExcelView;

/**
 *
 * @author Manisha
 */
public class ParStatementReportExcel extends AbstractExcelView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
            HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ArrayList cadrewisenrcReport = (ArrayList) model.get("cadrewisenrcReport");
        DepartmentPromotionBean dpcBean = (DepartmentPromotionBean) model.get("dpcBean");

        response.setHeader("Content-Disposition", "attachment; filename=\"PARStatementReport.xls\"");
        HSSFSheet sheet = workbook.createSheet("PAR List");
        sheet.setDefaultColumnWidth(30);
        CreationHelper helper = workbook.getCreationHelper();
        
        CellStyle style = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);

        CellStyle mergeCellStyle = workbook.createCellStyle();
        mergeCellStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);

        HSSFRow header = sheet.createRow(1);

        header.createCell(0).setCellValue("Sl No");
        header.getCell(0).setCellStyle(style);

        header.createCell(1).setCellValue("Employee Name");
        header.getCell(1).setCellStyle(style);

        header.createCell(2).setCellValue("Designation");
        header.getCell(2).setCellStyle(style);

        header.createCell(3).setCellValue("Date of birth");
        header.getCell(3).setCellStyle(style);

        header.createCell(4).setCellValue("Year");
        header.getCell(4).setCellStyle(style);

        header.createCell(5).setCellValue("Period From");
        header.getCell(5).setCellStyle(style);

        header.createCell(6).setCellValue("Period To");
        header.getCell(6).setCellStyle(style);

        header.createCell(7).setCellValue("Post Group");
        header.getCell(7).setCellStyle(style);

        header.createCell(8).setCellValue("Status");
        header.getCell(8).setCellStyle(style);

        header.createCell(9).setCellValue("Grade");
        header.getCell(9).setCellStyle(style);

        header.createCell(10).setCellValue("Remarks");
        header.getCell(10).setCellStyle(style);

        int rowNum = 2;

        for (int i = 0; i < cadrewisenrcReport.size(); i++) {
            DepartmentPromotionDetail departmentPromotionDetail = (DepartmentPromotionDetail) cadrewisenrcReport.get(i);
            ArrayList<FiscalYearWiseParData> fiscalYearList = departmentPromotionDetail.getFiscalYearList();
            int nooffiscalyear = fiscalYearList.size();
            int noofperiod = 0;
            int additionalrowspan = 0;
            for (int j = 0; j < nooffiscalyear; j++) {
                FiscalYearWiseParData fyear = fiscalYearList.get(j);
                noofperiod = noofperiod + fyear.getNoofperiod();
            }
            if (noofperiod > nooffiscalyear) {
                additionalrowspan = noofperiod - nooffiscalyear;
            }

            HSSFRow row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(i + 1);
            row.getCell(0).setCellStyle(mergeCellStyle);
            row.createCell(1).setCellValue(departmentPromotionDetail.getEmpName());
            row.getCell(1).setCellStyle(mergeCellStyle);
            row.createCell(2).setCellValue(departmentPromotionDetail.getEmpPost());
            row.getCell(2).setCellStyle(mergeCellStyle);
            row.createCell(3).setCellValue(departmentPromotionDetail.getDob());
            row.getCell(3).setCellStyle(mergeCellStyle);
            int firstRow = rowNum;
            for (int j = 0; j < nooffiscalyear; j++) {
                FiscalYearWiseParData fyear = fiscalYearList.get(j);
                ArrayList<FiscalYearWiseParData.Parabstractdata> yearwisedata = fyear.getYearwisedata();
                noofperiod = fyear.getNoofperiod();
                row.createCell(4).setCellValue(fyear.getFy());                
                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum()+(noofperiod-1), 4, 4));
                for (int k = 0; k < noofperiod; k++) {
                    FiscalYearWiseParData.Parabstractdata parabstractdata = yearwisedata.get(k);
                    row.createCell(5).setCellValue(parabstractdata.getPeriodfrom());
                    row.createCell(6).setCellValue(parabstractdata.getPeriodto());
                    row.createCell(7).setCellValue(parabstractdata.getPostGroup());
                    
                    if (parabstractdata.getParstatus() == 17) {
                        HSSFHyperlink link = (HSSFHyperlink)helper.createHyperlink(Hyperlink.LINK_URL);
                        link.setAddress("https://apps.hrmsodisha.gov.in/getviewPARAdmindetail.htm?parId=" + parabstractdata.getParid() + "&taskId=");
                        HSSFCell cell = row.createCell(8);
                        cell.setCellValue("NRC");
                        cell.setHyperlink(link);
                    } else if ((parabstractdata.getParstatus() != 17 && parabstractdata.getParid() != 0) && (parabstractdata.getIsreviewed().equals("Y") && parabstractdata.getPostGroup() != null && parabstractdata.getPostGroup().equals("A")) || parabstractdata.getPostGroup() != null && parabstractdata.getPostGroup().equals("B")) {                        
                        HSSFHyperlink link = (HSSFHyperlink)helper.createHyperlink(Hyperlink.LINK_URL);
                        link.setAddress("https://apps.hrmsodisha.gov.in/getviewPARAdmindetail.htm?parId=" + parabstractdata.getParid() + "&taskId=");
                        HSSFCell cell = row.createCell(8);
                        cell.setCellValue("PAR");
                        cell.setHyperlink(link);                        
                    } else if (parabstractdata.getParid() == 0) {
                        row.createCell(8).setCellValue("Not Initiated");                        
                    } else if ((parabstractdata.getIsadversed() == null || parabstractdata.getIsadversed().equals("")) && parabstractdata.getPostGroup() != null && parabstractdata.getPostGroup().equals("A") && parabstractdata.getIsreviewed().equals("N")) {
                        row.createCell(8).setCellValue("Not Reviewed");                        
                    } else if (parabstractdata.getPostGroup() != null && parabstractdata.getPostGroup().equals("A") && parabstractdata.getIsadversed() != null && parabstractdata.getIsadversed().equals("Y")) {
                        row.createCell(8).setCellValue("Adverse");                        
                    }
                    
                    if ((parabstractdata.getPostGroup() != null && parabstractdata.getPostGroup().equals("A") && parabstractdata.getIsreviewed().equals("Y")) || parabstractdata.getPostGroup() != null && parabstractdata.getPostGroup().equals("B") && (parabstractdata.getParstatus() != 17)) {
                        row.createCell(9).setCellValue(parabstractdata.getGradeName()); 
                    }else{
                        row.createCell(9).setCellValue(""); 
                    }
                    row.createCell(10).setCellValue(parabstractdata.getRemark());
                    if ((k + 1) < noofperiod) {
                        row = sheet.createRow(rowNum++);
                    }
                }
                /*Logic to do not add extra row*/
                if ((j + 1) < nooffiscalyear) {
                    row = sheet.createRow(rowNum++);
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(firstRow - 1, rowNum - 1, 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(firstRow - 1, rowNum - 1, 1, 1));
            sheet.addMergedRegion(new CellRangeAddress(firstRow - 1, rowNum - 1, 2, 2));
            sheet.addMergedRegion(new CellRangeAddress(firstRow - 1, rowNum - 1, 3, 3));

            /*row.createCell(4).setCellValue(departmentPromotionDetail.getEmpName());
             row.createCell(5).setCellValue(departmentPromotionDetail.getDob());
             row.createCell(6).setCellValue(departmentPromotionDetail.getEmpName());
             row.createCell(7).setCellValue(departmentPromotionDetail.getEmpName());
             row.createCell(8).setCellValue(departmentPromotionDetail.getEmpName());
             row.createCell(9).setCellValue(departmentPromotionDetail.getEmpName());
             row.createCell(10).setCellValue(departmentPromotionDetail.getEmpName());*/
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

    }

}
