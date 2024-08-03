/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.view;

/**
 *
 * @author Satya
 */
import hrms.model.updateleave.UpdateLeave;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Satya
 */
public class EmpAttendanceData extends AbstractExcelView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
            HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<UpdateLeave> empAttendance = (List<UpdateLeave>) model.get("empAttendanceList");
        HSSFSheet sheet = workbook.createSheet("Attendance Report");
        HSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("Sl No");
        header.createCell(1).setCellValue("Biometric Emp Id");
        header.createCell(2).setCellValue("HRMS Id");
        header.createCell(3).setCellValue("Employee Name");
        header.createCell(4).setCellValue("Total CL");
        header.createCell(5).setCellValue("Available CL");
        header.createCell(6).setCellValue("Total Absent Current Month");
        header.createCell(7).setCellValue("Total Late In-Punch");
        header.createCell(8).setCellValue("Total Missing Out-Punch");
        header.createCell(9).setCellValue("Leave Balance As on Date ");
        header.createCell(10).setCellValue("No. Of days (Every 3 days Late) ");
        int rowNum = 1;
        for (UpdateLeave updateLeave : empAttendance) {
            HSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(rowNum);
            row.createCell(1).setCellValue(updateLeave.getBiometricId());
            row.createCell(2).setCellValue(updateLeave.getEmpid());
            row.createCell(3).setCellValue(updateLeave.getEmpName());
            row.createCell(4).setCellValue(updateLeave.getTotalCl());
            row.createCell(5).setCellValue(updateLeave.getTotalClAvail());
            row.createCell(6).setCellValue(updateLeave.getTotAbsentCurMonth());
            row.createCell(7).setCellValue(updateLeave.getTotLateInpunch());
            row.createCell(8).setCellValue(updateLeave.getTotMissingOutPunch());
            row.createCell(9).setCellValue(updateLeave.getTotBalanceAsOnDate());
            row.createCell(10).setCellValue(updateLeave.getTotDaysForThreeDaysLate());
            rowNum++;
        }
    }
}
