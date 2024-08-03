/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.view;

import hrms.model.employee.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.web.servlet.view.document.AbstractExcelView;

/**
 *
 * @author Manas
 */
public class EmployeeExcelBuilder extends AbstractExcelView {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
            HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        //Connection con = null;
        //con = this.dataSource.getConnection();
        // PreparedStatement ps1 = null;
        String offCode = (String) model.get("offCode");
         String deptName = (String) model.get("deptName");
          String offname = (String) model.get("offname");
           String dist = (String) model.get("dist");
        
        WritableFont titleFont4 = new WritableFont(WritableFont.COURIER, 13, WritableFont.BOLD, true);
            int col = 2;
            int widthInChars = 5;
            WritableCellFormat titleformat4 = new WritableCellFormat(titleFont4);
            titleformat4.setAlignment(Alignment.CENTRE);
            titleformat4.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat4.setVerticalAlignment(VerticalAlignment.CENTRE);
       // ResultSet rs, rs1 = null;

        //ps1 = con.prepareStatement("select off_code,off_en, department_name,dist_name from g_office inner join g_department on g_office.department_code=g_department.department_code inner join g_district on g_office.dist_code=g_district.dist_code where off_code=?");
        // ps1.setString(1, offCode);
        // rs1 = ps1.executeQuery();
        List<Employee> listEmployees = (List<Employee>) model.get("listEmployees");
        response.setHeader("Content-Disposition", "attachment; filename=\"employeeList_" + offCode + ".xls\"");
        HSSFSheet sheet = workbook.createSheet("Employee List");
        sheet.setDefaultColumnWidth(30);

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
       // font.setFontHeight(short);
        style.setFont(font);
        HSSFRow addressHeadersept = sheet.createRow(0);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 18));
        addressHeadersept.createCell(0).setCellValue("Department Name: "+deptName +" DEPARTMENT");
        addressHeadersept.getCell(0).setCellStyle(style);

        HSSFRow addressHeaderoffname = sheet.createRow(1);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 18));
        addressHeaderoffname.createCell(0).setCellValue("Office Name: "+offname);
        addressHeaderoffname.getCell(0).setCellStyle(style);

        HSSFRow addressHeaderdistname = sheet.createRow(2);
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 18));
        addressHeaderdistname.createCell(0).setCellValue("Dist Name: "+dist);
        addressHeaderdistname.getCell(0).setCellStyle(style);

        HSSFRow addressHeader = sheet.createRow(3);
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 14, 18));
        //sheet.addMergedRegion(new CellRangeAddress(3, 3, 19, 22));
        addressHeader.createCell(14).setCellValue("PRESENT ADDRESS");
        addressHeader.getCell(14).setCellStyle(style);
       // addressHeader.createCell(19).setCellValue("PRESENT ADDRESS");
       // addressHeader.getCell(19).setCellStyle(style);

        // create header row
        HSSFRow header = sheet.createRow(4);

        header.createCell(0).setCellValue("Sl No");
        header.getCell(0).setCellStyle(style);

        header.createCell(1).setCellValue("HRMS ID");
        header.getCell(1).setCellStyle(style);

        header.createCell(2).setCellValue("GPF No");
        header.getCell(2).setCellStyle(style);

        header.createCell(3).setCellValue("Employee Name");
        header.getCell(3).setCellStyle(style);

        header.createCell(4).setCellValue("POST");
        header.getCell(4).setCellStyle(style);

        header.createCell(5).setCellValue("DOB");
        header.getCell(5).setCellStyle(style);

        header.createCell(6).setCellValue("MOBILE NO");
        header.getCell(6).setCellStyle(style);

        header.createCell(7).setCellValue("CURRENT BASIC PAY");
        header.getCell(7).setCellStyle(style);

        header.createCell(8).setCellValue("DOJ");
        header.getCell(8).setCellStyle(style);

        header.createCell(9).setCellValue("DOS");
        header.getCell(9).setCellStyle(style);

        header.createCell(10).setCellValue("POST GROUP");
        header.getCell(10).setCellStyle(style);

        header.createCell(11).setCellValue("GENDER");
        header.getCell(11).setCellStyle(style);

        header.createCell(12).setCellValue("Email");
        header.getCell(12).setCellStyle(style);

        header.createCell(13).setCellValue("CADRE");
        header.getCell(13).setCellStyle(style);

        header.createCell(14).setCellValue("VILLAGE");
        header.getCell(14).setCellStyle(style);

        header.createCell(15).setCellValue("POST OFFICE");
        header.getCell(15).setCellStyle(style);

        header.createCell(16).setCellValue("POLICE STATION");
        header.getCell(16).setCellStyle(style);

        header.createCell(17).setCellValue("BLOCK");
        header.getCell(17).setCellStyle(style);

        header.createCell(18).setCellValue("DISTRICT");
        header.getCell(18).setCellStyle(style);

//        header.createCell(19).setCellValue("VILLAGE");
//        header.getCell(19).setCellStyle(style);
//
//        header.createCell(20).setCellValue("POST OFFICE");
//        header.getCell(20).setCellStyle(style);
//
//        header.createCell(21).setCellValue("POLICE STATION");
//        header.getCell(21).setCellStyle(style);
//
//        header.createCell(22).setCellValue("BLOCK");
//        header.getCell(22).setCellStyle(style);
//
//        header.createCell(23).setCellValue("DISTRICT");
//        header.getCell(23).setCellStyle(style);

        int rowNum = 5;
        for (int i = 0; i < listEmployees.size(); i++) {
            Employee employee = (Employee) listEmployees.get(i);
            HSSFRow row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(employee.getEmpid());
            row.createCell(2).setCellValue(employee.getGpfno());
            row.createCell(3).setCellValue(employee.getFullname());
            row.createCell(4).setCellValue(employee.getPost());
            row.createCell(5).setCellValue(employee.getDob());
            row.createCell(6).setCellValue(employee.getMobile());
            row.createCell(7).setCellValue(employee.getBasic());
            row.createCell(8).setCellValue(employee.getJoindategoo());
            row.createCell(9).setCellValue(employee.getDor());
            row.createCell(10).setCellValue(employee.getPostGrpType());
            row.createCell(11).setCellValue(employee.getGender());
            row.createCell(12).setCellValue(employee.getEmail());
            row.createCell(13).setCellValue(employee.getCadreCode());
            row.createCell(14).setCellValue(employee.getVillName());
            row.createCell(15).setCellValue(employee.getPoName());
            row.createCell(16).setCellValue(employee.getPsName());
            row.createCell(17).setCellValue(employee.getBlName());
            row.createCell(18).setCellValue(employee.getDistName());
  
            
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

        sheet.autoSizeColumn(12);
        sheet.autoSizeColumn(13);
        sheet.autoSizeColumn(14);
        sheet.autoSizeColumn(15);
        sheet.autoSizeColumn(16);

        sheet.autoSizeColumn(17);
        sheet.autoSizeColumn(18);
        sheet.autoSizeColumn(19);
        sheet.autoSizeColumn(20);
        sheet.autoSizeColumn(21);
        sheet.autoSizeColumn(22);
        sheet.autoSizeColumn(23);
    }
}
