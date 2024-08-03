/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.Budget;

import hrms.common.DataBaseFunctions;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.Workbook;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 *
 * @author Madhusmita
 */
public class BudgetSpecialStatementDAOImpl implements BudgetSpecialStatementDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void downloadSpecialStatementExcel(OutputStream out, String fileName, WritableWorkbook workbook, String deptCode, String month, String year) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement selectpst = null;
        ResultSet rs = null;
        ResultSet selectrs = null;
        String deptName = null;
        int row = 0;
        double rowTotal = 0;

        double pay = 0;
        double da = 0;
        double hra = 0;

        int totalSancStrength = 0;
        int totalMeninPosition = 0;
        double totalPay = 0;
        double totalDA = 0;
        double totalHRA = 0;
        double grandTotal = 0;

        try {
            con = this.dataSource.getConnection();

            //out = new FileOutputStream(new File("D:/Special_Statement/FEB/Special_Statement_Excel_" + deptName + ".xls"));
            // WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("Special Statement", 0);

            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

            WritableCellFormat headcell = new WritableCellFormat(headformat);
            //headcell.setAlignment(Alignment.CENTRE);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);

            WritableFont innerformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);

            WritableCellFormat innercell = new WritableCellFormat(innerformat);

            jxl.write.Number num = null;

            row = row + 1;

            Label label = new Label(1, row, "FORM-IV", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(1, row, 10, row);

            row = row + 1;

            label = new Label(1, row, "(See Rule 7)", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(1, row, 10, row);

            row = row + 1;

            label = new Label(1, row, "SPECIAL STATEMENT ON NUMBER OF EMPLOYEES AND RELATED SALARIES", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(1, row, 10, row);

            row = row + 1;

            label = new Label(0, row, "", headcell);
            sheet.addCell(label);
            label = new Label(1, row, "", headcell);
            sheet.addCell(label);
            label = new Label(2, row, "No. of Employees", headcell);
            sheet.addCell(label);
            sheet.mergeCells(2, row, 3, row);
            label = new Label(4, row, "Expenditure     (Rupees in Lakh)", headcell);
            sheet.addCell(label);
            sheet.mergeCells(4, row, 9, row);

            row = row + 1;

            label = new Label(0, row, "Major Head", headcell);
            sheet.addCell(label);
            label = new Label(1, row, "Scales of Pay", headcell);
            sheet.addCell(label);
            label = new Label(2, row, "Sanctioned Strength", headcell);
            sheet.addCell(label);
            label = new Label(3, row, "Men in Position", headcell);
            sheet.addCell(label);
            label = new Label(4, row, "Pay including\nSpl Pay,DP and\nGP", headcell);
            sheet.addCell(label);
            label = new Label(5, row, "Dearness\nAllowance", headcell);
            sheet.addCell(label);
            label = new Label(6, row, "Allowances\n(HRA,RCM,\nOA)", headcell);
            sheet.addCell(label);
            label = new Label(7, row, "TOTAL", headcell);
            sheet.addCell(label);

            System.out.println("");
            String majorhead = "";
            String sql = "select bill_mast.major_head,g_spc.pay_scale,"
                    + " special_statement_7thlevel('" + deptCode + "',g_spc.pay_scale) level_7thpay,"
                    + " special_statement_sanctioned_strength_budget(bill_mast.major_head,'" + deptCode + "', " + year + " ,g_spc.pay_scale) sanctioned_strength,"
                    + " special_statement_men_in_position(bill_mast.major_head,'" + deptCode + "'," + year + " ," + month + " ,g_spc.pay_scale) men_in_position,"
                    + " special_statement_get_pay(bill_mast.major_head,'" + deptCode + "'," + year + " ," + month + " ,g_spc.pay_scale) pay,"
                    + " special_statement_get_da_amt(bill_mast.major_head,'" + deptCode + "'," + year + " , " + month + " ,g_spc.pay_scale) da,"
                    + " special_statement_get_hra_amt(bill_mast.major_head,'" + deptCode + "'," + year + " ," + month + " ,g_spc.pay_scale) hra"
                    + " from bill_mast"
                    + " inner join aq_mast on bill_mast.bill_no=aq_mast.bill_no"
                    + " inner join g_spc on aq_mast.cur_spc=g_spc.spc"
                    + " where bill_mast.off_code in (select off_code from g_office where g_office.department_code=? and g_office.is_ddo='Y') and bill_mast.aq_year=?"
                    + " and bill_mast.aq_month=? and (g_spc.pay_scale is not null and g_spc.pay_scale <> '') group by bill_mast.major_head,g_spc.pay_scale"
                    + " order by bill_mast.major_head,level_7thpay desc,g_spc.pay_scale";
            selectpst = con.prepareStatement(sql);
            selectpst.setString(1, deptCode);
            selectpst.setInt(2, Integer.parseInt(year));
            selectpst.setInt(3, Integer.parseInt(month));
            selectrs = selectpst.executeQuery();
            while (selectrs.next()) {
                row = row + 1;

                /*int sanctionedStrength = getSanctionedStrength(con,"OLSFIN0010000",aqyear,aqmonth,selectrs.getString("major_head"),selectrs.getString("pay_scale"),selectrs.getInt("gp"));
                 int menInPosition = getMenInPosition(con,"OLSFIN0010000",aqyear,aqmonth,selectrs.getString("major_head"),selectrs.getString("pay_scale"),selectrs.getInt("gp"));
                 int payAmt = getPayAmount(con,"OLSFIN0010000",aqyear,aqmonth,selectrs.getString("major_head"),selectrs.getString("pay_scale"),selectrs.getInt("gp"));
                 int daAmt = getDAAmount(con,"OLSFIN0010000",aqyear,aqmonth,selectrs.getString("major_head"),selectrs.getString("pay_scale"),selectrs.getInt("gp"));*/
                if (majorhead.equals("")) {
                    majorhead = selectrs.getString("major_head");
                } else if (selectrs.getString("major_head") != null && !majorhead.equals(selectrs.getString("major_head"))) {
                    majorhead = selectrs.getString("major_head");

                    label = new Label(0, row, "", headcell);
                    sheet.addCell(label);
                    label = new Label(1, row, "TOTAL", headcell);
                    sheet.addCell(label);
                    num = new jxl.write.Number(2, row, totalSancStrength, headcell);
                    sheet.addCell(num);
                    num = new jxl.write.Number(3, row, totalMeninPosition, headcell);
                    sheet.addCell(num);
                    num = new jxl.write.Number(4, row, totalPay, headcell);
                    sheet.addCell(num);
                    num = new jxl.write.Number(5, row, totalDA, headcell);
                    sheet.addCell(num);
                    num = new jxl.write.Number(6, row, totalHRA, headcell);
                    sheet.addCell(num);
                    num = new jxl.write.Number(7, row, grandTotal, headcell);
                    sheet.addCell(num);

                    totalSancStrength = 0;
                    totalMeninPosition = 0;
                    totalPay = 0;
                    totalDA = 0;
                    totalHRA = 0;
                    grandTotal = 0;

                    row = row + 1;
                }
                //System.out.println("Major Head is: " + majorhead);

                totalSancStrength = totalSancStrength + selectrs.getInt("sanctioned_strength");
                totalMeninPosition = totalMeninPosition + selectrs.getInt("men_in_position");
                totalPay = totalPay + selectrs.getDouble("pay");
                totalDA = totalDA + selectrs.getDouble("da");
                totalHRA = totalHRA + selectrs.getDouble("hra");
                grandTotal = totalPay + totalDA + totalHRA;

                pay = selectrs.getDouble("pay");
                da = selectrs.getDouble("da");
                hra = selectrs.getDouble("hra");
                rowTotal = pay + da + hra;

                String payStr = Double.valueOf(pay + "").longValue() + "";
                String daStr = Double.valueOf(da + "").longValue() + "";
                String hraStr = Double.valueOf(hra + "").longValue() + "";
                String rowTotalStr = Double.valueOf(rowTotal + "").longValue() + "";

                label = new Label(0, row, selectrs.getString("major_head"), innercell);
                sheet.addCell(label);
                label = new Label(1, row, selectrs.getString("pay_scale"), innercell);
                sheet.addCell(label);
                num = new jxl.write.Number(2, row, selectrs.getInt("sanctioned_strength"), innercell);
                sheet.addCell(num);
                num = new jxl.write.Number(3, row, selectrs.getInt("men_in_position"), innercell);
                sheet.addCell(num);
                label = new Label(4, row, payStr, innercell);
                sheet.addCell(label);
                label = new Label(5, row, daStr, innercell);
                sheet.addCell(label);
                label = new Label(6, row, hraStr, innercell);
                sheet.addCell(label);
                label = new Label(7, row, rowTotalStr, innercell);
                sheet.addCell(label);

                rowTotal = 0;
            }
            row = row + 1;

            label = new Label(0, row, "", headcell);
            sheet.addCell(label);
            label = new Label(1, row, "TOTAL", headcell);
            sheet.addCell(label);
            num = new jxl.write.Number(2, row, totalSancStrength, headcell);
            sheet.addCell(num);
            num = new jxl.write.Number(3, row, totalMeninPosition, headcell);
            sheet.addCell(num);
            num = new jxl.write.Number(4, row, totalPay, headcell);
            sheet.addCell(num);
            num = new jxl.write.Number(5, row, totalDA, headcell);
            sheet.addCell(num);
            num = new jxl.write.Number(6, row, totalHRA, headcell);
            sheet.addCell(num);
            num = new jxl.write.Number(7, row, grandTotal, headcell);
            sheet.addCell(num);
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(selectrs, selectpst);
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

}
