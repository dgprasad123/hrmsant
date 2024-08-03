package hrms.dao.report.budgetRequirement;

import hrms.common.DataBaseFunctions;
import hrms.model.report.budgetrequirement.BudgetRequirementListBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class BudgetRequirementDAOImpl implements BudgetRequirementDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getEmployeeList(String offCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList emplist = new ArrayList();

        BudgetRequirementListBean brlbean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,EMP_MAST.EMP_ID,GPF_NO,F_NAME,M_NAME,L_NAME,CUR_SALARY,CUR_BASIC_SALARY,EMP_MAST.GP,POST,bank_acc_no,"
                    + " CUR_SPC,OFF_CODE FROM EMP_MAST"
                    + " LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC"
                    + " LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE"
                    + " WHERE CUR_OFF_CODE=? AND DEP_CODE='02' ORDER BY F_NAME,M_NAME,L_NAME";
            pst = con.prepareStatement(sql);
            pst.setString(1, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                brlbean = new BudgetRequirementListBean();
                brlbean.setEmpname(rs.getString("EMP_NAME"));
                brlbean.setGpfno(rs.getString("GPF_NO"));
                brlbean.setPost(rs.getString("POST"));
                brlbean.setBankAccNo(rs.getString("bank_acc_no"));
                brlbean.setEmpid(rs.getString("EMP_ID"));
                brlbean.setPayScale(rs.getString("CUR_SALARY"));
                brlbean.setCurBasic(rs.getString("CUR_BASIC_SALARY"));
                String[] curLevelCell = getCurrentLevelCell(rs.getString("CUR_SALARY"), rs.getInt("CUR_BASIC_SALARY"));
                brlbean.setCurLevel(curLevelCell[0]);
                brlbean.setCurCell(curLevelCell[1]);
                String[] nextBasicLevelCell = getNextBasicLevelCell(rs.getString("CUR_SALARY"), rs.getInt("CUR_BASIC_SALARY"));
                brlbean.setNewBasic(nextBasicLevelCell[0]);
                brlbean.setNewLevel(nextBasicLevelCell[1]);
                brlbean.setNewCell(nextBasicLevelCell[2]);
                emplist.add(brlbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    private String[] getCurrentLevelCell(String payscale, int curbasic) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String[] levelCell = new String[2];
        try {
            con = this.dataSource.getConnection();

            String sql = "select gp_level,level_slno,payscale,amt from pay_matrix_2017 where payscale = ? and amt = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, payscale);
            pst.setInt(2, curbasic);
            rs = pst.executeQuery();
            if (rs.next()) {
                levelCell[0] = rs.getInt("gp_level") + "";
                levelCell[1] = rs.getInt("level_slno") + "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return levelCell;
    }

    private String[] getNextBasicLevelCell(String payscale, int curbasic) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String[] nextBasicLevelCell = new String[3];
        try {
            con = this.dataSource.getConnection();

            String sql = "select gp_level,level_slno,payscale,amt from pay_matrix_2017 where payscale = ? and amt > ? limit 1";
            pst = con.prepareStatement(sql);
            pst.setString(1, payscale);
            pst.setInt(2, curbasic);
            rs = pst.executeQuery();
            if (rs.next()) {
                nextBasicLevelCell[0] = rs.getInt("amt") + "";
                nextBasicLevelCell[1] = rs.getInt("gp_level") + "";
                nextBasicLevelCell[2] = rs.getInt("level_slno") + "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return nextBasicLevelCell;
    }

    @Override
    public void downloadBudgetRequirementDataExcel(HttpServletResponse response, String offCode, ArrayList emplist) {

        try {

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Employee List");

            Font headerFont = workbook.createFont();
            //headerFont.setBoldweight(true);
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setColor(IndexedColors.RED.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);

            Cell cell = headerRow.createCell(0);
            cell.setCellValue("Sl No");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(1);
            cell.setCellValue("EMP NAME");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(2);
            cell.setCellValue("GPF No");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(3);
            cell.setCellValue("POST");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(4);
            cell.setCellValue("BANk ACC No");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(5);
            cell.setCellValue("HRMS ID");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(6);
            cell.setCellValue("PAY SCALE");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(7);
            cell.setCellValue("CURRENT BASIC");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(8);
            cell.setCellValue("CURRENT LEVEL");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(9);
            cell.setCellValue("CURRENT CELL");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(10);
            cell.setCellValue("NEW BASIC AFTER INCREMENT");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(11);
            cell.setCellValue("NEW LEVEL");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(12);
            cell.setCellValue("NEW CELL");
            cell.setCellStyle(headerCellStyle);

            BudgetRequirementListBean brlbean = null;
            int rowNum = 1;
            if (emplist != null && emplist.size() > 0) {
                for (int i = 0; i < emplist.size(); i++) {
                    brlbean = (BudgetRequirementListBean) emplist.get(i);

                    Row dataRow = sheet.createRow(rowNum);

                    dataRow.createCell(0).setCellValue(i + 1);

                    dataRow.createCell(1).setCellValue(brlbean.getEmpname());

                    dataRow.createCell(2).setCellValue(brlbean.getGpfno());

                    dataRow.createCell(3).setCellValue(brlbean.getPost());

                    dataRow.createCell(4).setCellValue(brlbean.getBankAccNo());

                    dataRow.createCell(5).setCellValue(brlbean.getEmpid());

                    dataRow.createCell(6).setCellValue(brlbean.getPayScale());

                    dataRow.createCell(7).setCellValue(brlbean.getCurBasic());

                    dataRow.createCell(8).setCellValue(brlbean.getCurLevel());

                    dataRow.createCell(9).setCellValue(brlbean.getCurCell());

                    dataRow.createCell(10).setCellValue(brlbean.getNewBasic());

                    dataRow.createCell(11).setCellValue(brlbean.getNewLevel());

                    dataRow.createCell(12).setCellValue(brlbean.getNewCell());

                    rowNum += 1;
                }
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

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=\"BudgetRequirementData_" + offCode + ".xls\"");

            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
