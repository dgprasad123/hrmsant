package hrms.dao.payroll.tpschedule;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.AqFunctionalities;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.Numtowordconvertion;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.billbrowser.BillConfigObj;
import hrms.model.payroll.tpfschedule.TPFEmployeeScheduleBean;
import hrms.model.payroll.tpfschedule.TPFScheduleBean;
import hrms.model.payroll.tpfschedule.TpfTypeBean;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.lang.StringUtils;

public class TPFScheduleDAOImpl implements TPFScheduleDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    @Override
    public List getEmployeeWiseTPFList(String billNo, int aqMonth, int aqYear) {

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList tpfList = new ArrayList();
        TpfTypeBean tpfBean = null;

        try {
            con = dataSource.getConnection();

            stmt = con.createStatement();
            String sql = "SELECT GPF_TYPE FROM AQ_MAST WHERE BILL_NO ='" + billNo + "' and aq_month = " + aqMonth + " and aq_year = " + aqYear + " "
                    + "and ACCT_TYPE='TPF' AND GPF_TYPE IS NOT NULL GROUP BY GPF_TYPE ORDER BY GPF_TYPE";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tpfBean = new TpfTypeBean();
                tpfBean.setGpfType(rs.getString("GPF_TYPE"));
                tpfBean.setEmpGpfList(getEmpTPFDetails(tpfBean.getGpfType(), billNo, con, aqMonth, aqYear));
                tpfBean.setEmpno(tpfBean.getEmpGpfList().size());
                tpfList.add(tpfBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return tpfList;
    }

    public ArrayList getEmpTPFDetails(String gpfType, String billNo, Connection con, int aqMonth, int aqYear) throws SQLException {

        Statement stmt = null;
        ResultSet rs = null;
        Statement stmt1 = null;
        ResultSet rs1 = null;
        Statement stmt2 = null;
        ResultSet rs2 = null;
        Statement stmt3 = null;
        ResultSet rs3 = null;

        TPFEmployeeScheduleBean empSchBean = null;
        ArrayList empGpfList = new ArrayList();

        String noofinst = "";
        int releasedAmount = 0;
        int total = 0;
        String dob = null;
        String dob1 = null;
        String dob2 = null;
        String dob3 = null;
        String dob4 = null;
        String dos = null;
        String dos1 = null;
        String dos2 = null;
        String dos3 = null;
        String dos4 = null;
        String doe = null;
        String doe1 = null;
        String doe2 = null;
        String doe3 = null;
        String doe4 = null;

        String aqdtlsTblName = getAqDtlsTableName(billNo);
        try {
            stmt = con.createStatement();
            String query = "SELECT * FROM ( "
                    + "SELECT GPF_ACC_NO,regexp_replace(EMP_MAST.GPF_NO, '\\d', '', 'g') SERIES,EMP_MAST.DOE_GOV,EMP_MAST.DOB,EMP_MAST.DOS,"
                    + "AQ_MAST.EMP_CODE,AQ_MAST.EMP_NAME, AQ_MAST.CUR_DESG,AQ_MAST.BANK_ACC_NO,AQ_MAST.CUR_BASIC,AQ_MAST.PAY_SCALE,"
                    + "AQ_MAST.AQSL_NO,POST_SL_NO from (SELECT EMP_CODE,EMP_NAME,CUR_DESG,BANK_ACC_NO,CUR_BASIC,PAY_SCALE,AQSL_NO,POST_SL_NO,"
                    + "GPF_ACC_NO from AQ_MAST WHERE GPF_TYPE = '" + gpfType + "' AND BILL_NO = '" + billNo + "' and aq_month = " + aqMonth + " "
                    + "and aq_year = " + aqYear + " AND ACCT_TYPE='TPF') AQ_MAST "
                    + "left outer join EMP_MAST on AQ_MAST.EMP_CODE =EMP_MAST.EMP_ID ) TPF_DETAILS ORDER BY EMP_NAME,SERIES";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                empSchBean = new TPFEmployeeScheduleBean();
                if (rs.getString("EMP_NAME") != null && !rs.getString("EMP_NAME").equals("")) {

                    empSchBean.setName(rs.getString("EMP_NAME"));
                    empSchBean.setDesignation(rs.getString("CUR_DESG"));
                    empSchBean.setAccNo(rs.getString("GPF_ACC_NO"));
                    empSchBean.setBasicPay(rs.getString("CUR_BASIC"));
                    empSchBean.setScaleOfPay(rs.getString("PAY_SCALE"));
                    noofinst = getNoOfInst(rs.getString("AQSL_NO"), aqdtlsTblName, con);
                    empSchBean.setNoOfInst(noofinst);
                    if (rs.getString("DOE_GOV") != null && !rs.getString("DOE_GOV").trim().equals("")) {
                        doe = rs.getString("DOE_GOV");
                        doe1 = doe.substring(0, 4);
                        doe2 = doe.substring(5, 7);
                        doe3 = doe.substring(8, 10);
                        doe4 = doe3 + "/" + doe2 + "/" + doe1;
                        empSchBean.setDateOfEntry(doe4);
                    }
                    if (rs.getString("DOB") != null && !rs.getString("DOB").trim().equals("")) {
                        dob = rs.getString("DOB");

                        dob1 = dob.substring(0, 4);
                        dob2 = dob.substring(5, 7);
                        dob3 = dob.substring(8, 10);
                        dob4 = dob3 + "/" + dob2 + "/" + dob1;
                        empSchBean.setDob(dob4);
                    }
                    if (rs.getString("DOS") != null && !rs.getString("DOS").trim().equals("")) {
                        dos = rs.getString("DOS");
                        dos1 = dos.substring(0, 4);
                        dos2 = dos.substring(5, 7);
                        dos3 = dos.substring(8, 10);
                        dos4 = dos3 + "/" + dos2 + "/" + dos1;
                        empSchBean.setDor(dos4);
                    }

                    stmt1 = con.createStatement();
                    query = "SELECT " + aqdtlsTblName + ".AD_AMT MONTHLYSUB FROM " + aqdtlsTblName + " WHERE " + aqdtlsTblName + ".AD_TYPE = 'D' AND EMP_CODE='" + rs.getString("EMP_CODE") + "' AND " + aqdtlsTblName + ".DED_TYPE = 'S' AND " + aqdtlsTblName + ".SCHEDULE = 'TPF' AND AQSL_NO = '" + rs.getString("AQSL_NO") + "' order by ad_amt desc";
                    rs1 = stmt1.executeQuery(query);
                    if (rs1.next()) {
                        empSchBean.setMonthlySub(rs1.getInt("MONTHLYSUB"));
                    }

                    stmt2 = con.createStatement();
                    query = "SELECT SUM(" + aqdtlsTblName + ".AD_AMT) TOWARDSLOAN FROM " + aqdtlsTblName + " WHERE " + aqdtlsTblName + ".AD_TYPE = 'D' AND EMP_CODE='" + rs.getString("EMP_CODE") + "' AND " + aqdtlsTblName + ".DED_TYPE = 'L' AND " + aqdtlsTblName + ".SCHEDULE = 'TPFGA' AND AQSL_NO = '" + rs.getString("AQSL_NO") + "'";
                    rs2 = stmt2.executeQuery(query);
                    if (rs2.next()) {
                        empSchBean.setTowardsLoan(rs2.getInt("TOWARDSLOAN"));
                    }

                    stmt3 = con.createStatement();
                    query = "SELECT SUM(" + aqdtlsTblName + ".AD_AMT) TOWARDSOTHER FROM " + aqdtlsTblName + " WHERE " + aqdtlsTblName + ".AD_TYPE = 'D' AND EMP_CODE='" + rs.getString("EMP_CODE") + "' AND (" + aqdtlsTblName + ".AD_CODE = 'GPDD' OR " + aqdtlsTblName + ".AD_CODE = 'GPIR') AND AQSL_NO = '" + rs.getString("AQSL_NO") + "'";
                    rs3 = stmt3.executeQuery(query);
                    if (rs3.next()) {
                        empSchBean.setOtherdeposits(rs3.getInt("TOWARDSOTHER"));
                    }

                    releasedAmount = empSchBean.getMonthlySub() + empSchBean.getTowardsLoan() + empSchBean.getOtherdeposits();
                    empSchBean.setTotalReleased(releasedAmount);
                    total += releasedAmount;

                    empSchBean.setCarryForward(total + "");
                    //if (total > 0) {
                    empGpfList.add(empSchBean);
                    //}
                }
                if (total > 0) {
                    empSchBean.setAmountInWords(Numtowordconvertion.convertNumber((int) total).toUpperCase());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, rs2, rs3);
            DataBaseFunctions.closeSqlObjects(rs, stmt);
        }
        return empGpfList;
    }

    public static String getNoOfInst(String aqslno, String aqdtlsTblName, Connection con) throws Exception {

        Statement stmt = null;
        ResultSet rs = null;

        String inst = "";
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT REF_DESC FROM " + aqdtlsTblName + " WHERE AQSL_NO='" + aqslno + "' AND SCHEDULE='TPFGA'");
            if (rs.next()) {
                inst = rs.getString("REF_DESC");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
        }
        return inst;
    }

    public String getAqDtlsTableName(String billNo) {

        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        String aqDTLS = "AQ_DTLS";
        try {
            con = dataSource.getConnection();

            stmt = con.createStatement();
            res = stmt.executeQuery("SELECT aq_month,aq_year FROM BILL_MAST WHERE bill_no=" + billNo);
            int aqMonth = 0;
            int aqYear = 0;
            if (res.next()) {
                aqMonth = res.getInt("aq_month");
                aqYear = res.getInt("aq_year");
            }

            aqDTLS = hrms.common.AqFunctionalities.getAQBillDtlsTable(aqMonth, aqYear);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aqDTLS;
    }

    @Override
    public List getTPFAbstract(String billNo, int aqMonth, int aqYear) {

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList tpfabstractList = new ArrayList();
        TPFScheduleBean tpfTypeBean = null;

        double sal = 0.0;
        String empCode = "";
        double amt = 0.0;
        double amt1 = 0.0;
        String tot = null;
        String tot1 = null;

        String aqdtlsTblName = getAqDtlsTableName(billNo);
        try {
            con = dataSource.getConnection();

            stmt = con.createStatement();
            String sql = "SELECT GPF_TYPE,SUM(AD_AMT) AMT FROM (SELECT AQ_MAST.AQSL_NO,GPF_TYPES.GPF_TYPE,AD_AMT FROM (SELECT AQ_MAST.AQSL_NO,GPF_TYPE "
                    + "FROM AQ_MAST where BILL_NO = '" + billNo + "' and aq_month = " + aqMonth + " and aq_year = " + aqYear + ")AQ_MAST"
                    + " INNER JOIN (SELECT GPF_TYPE from AQ_MAST where BILL_NO = '" + billNo + "'  and aq_month = " + aqMonth + " and aq_year = " + aqYear + " "
                    + "group by GPF_TYPE) GPF_TYPES ON GPF_TYPES.GPF_TYPE = AQ_MAST.GPF_TYPE "
                    + "INNER JOIN (SELECT AQSL_NO,AD_AMT FROM " + aqdtlsTblName + " WHERE (AD_CODE = 'TPF' and DED_TYPE ='S') "
                    + "or (AD_CODE='TPFGA' AND DED_TYPE='L') or (AD_CODE='GPDD' AND DED_TYPE='S') or (AD_CODE='GPIR' AND DED_TYPE='S'))AQ_DTLS "
                    + "ON AQ_MAST.AQSL_NO = AQ_DTLS.AQSL_NO)TEMP GROUP BY GPF_TYPE ORDER BY GPF_TYPE";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tpfTypeBean = new TPFScheduleBean();
                tpfTypeBean.setPfcode(rs.getString("GPF_TYPE"));
                tpfTypeBean.setTotalamt(rs.getString("AMT"));
                sal = rs.getDouble("AMT");
                amt = amt + sal;
                amt1 = amt + 1;

                tpfTypeBean.setCarryForward((int) amt + "");
                tpfTypeBean.setCarryForward1((int) amt1 + "");

                tot = Numtowordconvertion.convertNumber((int) amt);
                tpfTypeBean.setAmountInWords(tot.toUpperCase());
                tot1 = Numtowordconvertion.convertNumber((int) amt1);
                tpfTypeBean.setAmountInWords1(tot1.toUpperCase());

                if (sal > 0) {
                    tpfabstractList.add(tpfTypeBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return tpfabstractList;
    }

    @Override
    public void downloadQuarterITExcel(OutputStream out, String fileName, WritableWorkbook workbook, String billNo, BillConfigObj billConfig) {
        DataSource ds = null;
        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;
        Statement stmt1 = null;
        ResultSet rs1 = null;

        int gross = 0;
        int repcol = 0;
        String adtype = "";
        String pvtdl = "";
        int grosspay = 0;
        int netded = 0;
        int netpay = 0;

        BufferedOutputStream output = null;
        try {
            con = dataSource.getConnection();

            String filename = "QuarterIT_" + billNo + ".xls";

            WritableSheet sheet = workbook.createSheet("Aquitance", 0);

            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            WritableCellFormat headcell = new WritableCellFormat(headformat);
            WritableFont cellformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
            WritableCellFormat innercell = new WritableCellFormat(cellformat);

            sheet.mergeCells(0, 0, 0, 1);
            Label label = new Label(0, 0, "SL No", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(1, 0, 1, 1);
            label = new Label(1, 0, "EMP NAME", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(2, 0, 2, 1);
            label = new Label(2, 0, "POST", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(3, 0, 3, 1);
            label = new Label(3, 0, "PAN No", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(4, 0, 4, 1);
            label = new Label(4, 0, "DATE OF PAYMENT", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(5, 0, 5, 1);
            label = new Label(5, 0, "BASIC", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(6, 0, 6, 1);
            label = new Label(6, 0, "SP", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(7, 0, 7, 1);
            label = new Label(7, 0, "GP", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(8, 0, 8, 1);
            label = new Label(8, 0, "DP", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(9, 0, 9, 1);
            label = new Label(9, 0, "DA", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(10, 0, 10, 1);
            label = new Label(10, 0, "IR", headcell);//column,row
            sheet.addCell(label);

            int col = 10;
            String[] col6 = billConfig.getCol6List();
            String[] col7 = billConfig.getCol7List();

            if (col6 == null) {
                sheet.mergeCells(11, 0, 11, 1);
                label = new Label(11, 0, "HRA", headcell);//column,row
                sheet.addCell(label);
                col = 11;
            } else if (col6 != null && col6.length > 0) {

                col = 10;
                for (int i = 0; i < col6.length; i++) {
                    col = col + 1;
                    label = new Label(col, 1, col6[i], headcell);
                    sheet.addCell(label);
                }
            }
            if (col7 != null && col7.length > 0) {
                sheet.mergeCells(12, 0, 18, 0);
                label = new Label(12, 0, "OTHER ALLOWANCE", headcell);//column,row
                sheet.addCell(label);
                for (int i = 0; i < col7.length; i++) {
                    col = col + 1;

                    label = new Label(col, 1, col7[i], headcell);
                    sheet.addCell(label);
                }
            } else {
                col = 11;
            }
            col = col + 1;

            label = new Label(col, 1, "GROSS", headcell);
            sheet.addCell(label);
            col = col + 1;
            label = new Label(col, 1, "INCOME TAX", headcell);
            sheet.addCell(label);
            col = col + 1;
            label = new Label(col, 1, "EXCESS PAY", headcell);
            sheet.addCell(label);
            col = col + 1;
            label = new Label(col, 1, "HRMS ID", headcell);
            sheet.addCell(label);
            col = col + 1;
            label = new Label(col, 1, "NET PAY", headcell);
            sheet.addCell(label);

            //rs = stmt.executeQuery("SELECT AQSL_NO,EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,GPF_ACC_NO FROM AQ_MAST WHERE  BILL_NO='"+billNo+"' AND EMP_NAME IS NOT NULL order by post_sl_no");
            String firstsql = "SELECT ID_NO,AQ_MAST.*,VCH_DATE FROM (SELECT post_sl_no,BILL_NO,AQSL_NO,EMP_NAME,EMP_CODE,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,GPF_ACC_NO FROM AQ_MAST WHERE"
                    + " BILL_NO='" + billNo + "' AND EMP_NAME IS NOT NULL)AQ_MAST"
                    + " left outer join (SELECT EMP_ID,ID_DESCRIPTION,ID_NO FROM EMP_ID_DOC"
                    + " WHERE ID_DESCRIPTION='PAN')EMP_ID_DOC ON AQ_MAST.EMP_CODE=EMP_ID_DOC.EMP_ID"
                    + " left outer join bill_mast on aq_mast.bill_no=bill_mast.bill_no order by post_sl_no";
            stmt = con.createStatement();
            rs = stmt.executeQuery(firstsql);

            int row = 1;
            int row1 = 1;
            while (rs.next()) {
                //int netPay = AqFunctionalities.getGrossPay(con,rs.getString("AQSL_NO"))-AqFunctionalities.getTotalDedn(con,rs.getString("AQSL_NO"));
                row++;
                row1++;
                col = 11;
                //gross = 0;

                label = new Label(0, row, (row - 1) + "", innercell);
                sheet.addCell(label);
                label = new Label(1, row, rs.getString("EMP_NAME"), innercell);
                sheet.addCell(label);
                label = new Label(2, row, rs.getString("CUR_DESG"), innercell);
                sheet.addCell(label);
                label = new Label(3, row, rs.getString("ID_NO"), innercell);
                sheet.addCell(label);
                label = new Label(4, row, CommonFunctions.getFormattedOutputDate1(rs.getDate("VCH_DATE")), innercell);
                sheet.addCell(label);
                label = new Label(5, row, rs.getString("CUR_BASIC"), innercell);
                sheet.addCell(label);
                gross = gross + rs.getInt("CUR_BASIC");

                String secsql = "select S1.* from (select cnt,REP_COL,tmep4.AD_CODE,AD_AMT,SCHEDULE,tmep4.NOW_DEDN,AD_TYPE,sl_no,REF_DESC from(select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM AQ_DTLS WHERE AQSL_NO='" + rs.getString("AQSL_NO") + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt = 1)tmep4 "
                        + "left outer join (SELECT AD_CODE,REF_DESC,NOW_DEDN FROM AQ_DTLS WHERE AQSL_NO='" + rs.getString("AQSL_NO") + "') temp2 on "
                        + "tmep4.ad_code = temp2.ad_code and tmep4.now_dedn = temp2.now_dedn "
                        + "union "
                        + "select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no,''::text from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM AQ_DTLS WHERE AQSL_NO='" + rs.getString("AQSL_NO") + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt > 1 ) AS S1 order by SL_NO ";

                DataBaseFunctions.closeSqlObjects(rs1, stmt1);

                stmt1 = con.createStatement();
                rs1 = stmt1.executeQuery(secsql);
                //rs1 = stmt1.executeQuery("SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,REF_DESC,NOW_DEDN,AD_TYPE FROM AQ_DTLS WHERE AQSL_NO='"+rs.getString("AQSL_NO")+"' order by sl_no");
                boolean insideWhile = false;

                while (rs1.next()) {

                    insideWhile = true;
                    String adcode = rs1.getString("AD_CODE");
                    repcol = rs1.getInt("REP_COL");
                    adtype = rs1.getString("AD_TYPE");
                    pvtdl = rs1.getString("SCHEDULE");

                    if (adcode.equals("SP")) {
                        //gross = gross + rs1.getInt("AD_AMT");
                        label = new Label(6, row, rs1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("GP")) {
                        //gross = gross + rs1.getInt("AD_AMT");

                        label = new Label(7, row, rs1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("DP")) {
                        //gross = gross + rs1.getInt("AD_AMT");
                        label = new Label(8, row, rs1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("DA")) {

                        //gross = gross + rs1.getInt("AD_AMT");
                        label = new Label(9, row, rs1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("IR")) {
                        //gross = gross + rs1.getInt("AD_AMT");
                        label = new Label(10, row, rs1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (col6 == null) {
                        if (adcode.equals("HRA")) {
                            //gross = gross + rs1.getInt("AD_AMT");
                            label = new Label(11, row, rs1.getString("AD_AMT"), innercell);
                            sheet.addCell(label);
                        }
                        col = 11;
                    } else if (col6 != null && col6.length > 0) {
                        col = 10;
                        for (int i = 0; i < col6.length; i++) {
                            col = col + 1;
                            if (repcol == 6) {
                                if (adcode.equals(col6[i])) {
                                    //gross = gross + rs1.getInt("AD_AMT");
                                    label = new Label(col, row, rs1.getString("AD_AMT"), innercell);
                                    sheet.addCell(label);
                                }
                            }
                        }
                    }
                    if (col7 != null && col7.length > 0) {
                        for (int j = 0; j < col7.length; j++) {
                            col = col + 1;
                            if (repcol == 7) {
                                if (adcode.equals(col7[j])) {
                                    //gross = gross + rs1.getInt("AD_AMT");
                                    label = new Label(col, row, rs1.getString("AD_AMT"), innercell);
                                    sheet.addCell(label);
                                }
                            }
                        }
                    } else {
                        col = 11;
                    }

                    if (adtype != null && adtype.equals("A")) {
                        gross = gross + rs1.getInt("AD_AMT");
                    }

                    col = col + 1;
                    label = new Label(col, row, "" + gross, innercell);
                    sheet.addCell(label);
                    col = col + 1;
                    if (adcode.equals("IT")) {
                        label = new Label(col, row, rs1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    col = col + 1;
                    if (adcode.equals("EP")) {
                        label = new Label(col, row, rs1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }

                    if (adtype != null && adtype.equals("D")) {
                        if (pvtdl != null && !pvtdl.equals("PVTD") && !pvtdl.equals("PVTL")) {
                            netded = netded + rs1.getInt("AD_AMT");
                        }
                    }
                    col = col + 1;
                    label = new Label(col, row, rs.getString("EMP_CODE"), innercell);
                    sheet.addCell(label);
                    col = col + 1;
                    netpay = gross - netded;
                    label = new Label(col, row, netpay + "", innercell);
                    sheet.addCell(label);
                }
                if (insideWhile == false) {
                    col = 10;
                    if (col6 != null && col6.length > 0) {
                        for (int j = 0; j < col6.length; j++) {
                            col = col + 1;
                        }
                    }
                    if (col7 != null && col7.length > 0) {
                        for (int j = 0; j < col7.length; j++) {
                            col = col + 1;
                        }
                    }
                    //gross = gross + rs.getInt("CUR_BASIC");
                    col = col + 1;
                    label = new Label(col, row, "" + gross, innercell);
                    sheet.addCell(label);
                    //gross = 0;
                    col = col + 1;
                    label = new Label(col, row, "", innercell);
                    sheet.addCell(label);
                    col = col + 1;
                    label = new Label(col, row, "", innercell);
                    sheet.addCell(label);
                    col = col + 1;
                    label = new Label(col, row, "", innercell);
                    sheet.addCell(label);
                    col = col + 1;
                    label = new Label(col, row, "", innercell);
                    sheet.addCell(label);
                }

                gross = 0;
                netpay = 0;
                netded = 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, stmt1);
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void generateTPFSchedulePDF(String filename, Document document, String billno, List tpfEmpList, List tpfAbstractList, CommonReportParamBean crb) {

        int pageNo = 0;
        int slno = 0;
        int empcnt = 0;
        int tot = 0;

        int amount = 0;
        String amountString = "";

        try {
            document.open();

            Font f1 = new Font();
            f1.setSize(9);
            f1.setFamily("Times New Roman");

            PdfPTable table = null;
            table = new PdfPTable(2);
            table.setWidths(new int[]{5, 2});
            table.setWidthPercentage(100);
            PdfPCell cell = null;

            PdfPTable innertable = null;
            innertable = new PdfPTable(8);
            innertable.setWidths(new float[]{1, 1.5f, 3f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f});
            innertable.setWidthPercentage(100);
            PdfPCell innercell = null;

            TPFScheduleBean obj = null;
            if (tpfAbstractList != null && tpfAbstractList.size() > 0) {
                obj = new TPFScheduleBean();
                for (int i = 0; i < tpfAbstractList.size(); i++) {
                    obj = (TPFScheduleBean) tpfAbstractList.get(i);

                    amount = Integer.parseInt(obj.getCarryForward1()) - 1;
                    amountString = Numtowordconvertion.convertNumber((int) amount);
                }
            }

            if (tpfEmpList != null && tpfEmpList.size() > 0) {
                Iterator itr = tpfEmpList.iterator();
                TpfTypeBean tpfTypeBean = null;

                while (itr.hasNext()) {
                    tpfTypeBean = (TpfTypeBean) itr.next();
                    slno++;
                    boolean empPrinted = false;

                    pageNo++;
                    printHeader(innertable, innercell, crb.getBilldesc(), tpfTypeBean.getGpfType(), pageNo, f1);
                    printHeader1(innertable, innercell, crb.getAqmonth() + "", crb.getAqyear(), f1);

                    List tpfDtlsList = tpfTypeBean.getEmpGpfList();
                    if (tpfDtlsList != null && tpfDtlsList.size() > 0) {
                        Iterator itr1 = tpfDtlsList.iterator();
                        TPFEmployeeScheduleBean tpfSchBean = null;
                        while (itr1.hasNext()) {
                            tpfSchBean = (TPFEmployeeScheduleBean) itr1.next();
                            tot = tot + tpfSchBean.getTotalReleased();

                            empcnt++;
                            //1st row inside while
                            innercell = new PdfPCell(new Phrase(empcnt + "", f1));
                            innercell.setBorder(Rectangle.TOP);
                            innertable.addCell(innercell);
                            innercell = new PdfPCell(new Phrase(tpfSchBean.getAccNo(), f1));
                            innercell.setBorder(Rectangle.TOP);
                            innertable.addCell(innercell);
                            innercell = new PdfPCell(new Phrase(tpfSchBean.getName() + "\n" + tpfSchBean.getDesignation(), f1));
                            innercell.setBorder(Rectangle.TOP);
                            innertable.addCell(innercell);
                            innercell = new PdfPCell(new Phrase(tpfSchBean.getBasicPay() + "\n" + tpfSchBean.getScaleOfPay(), f1));
                            innercell.setBorder(Rectangle.TOP);
                            innertable.addCell(innercell);
                            innercell = new PdfPCell(new Phrase("" + tpfSchBean.getMonthlySub(), f1));
                            innercell.setBorder(Rectangle.TOP);
                            innertable.addCell(innercell);
                            innercell = new PdfPCell(new Phrase(tpfSchBean.getNoOfInst(), f1));
                            innercell.setBorder(Rectangle.TOP);
                            innertable.addCell(innercell);
                            innercell = new PdfPCell(new Phrase("" + tpfSchBean.getTotalReleased(), f1));
                            innercell.setBorder(Rectangle.TOP);
                            innertable.addCell(innercell);
                            innercell = new PdfPCell(new Phrase(tpfSchBean.getDob() + "\n" + tpfSchBean.getDor(), f1));
                            innercell.setBorder(Rectangle.TOP);
                            innertable.addCell(innercell);
                            if (empcnt == tpfDtlsList.size()) {
                                printGrandTotal(innertable, innercell, tot, amountString, f1);
                            }
                            if (empcnt == 20 && empcnt <= tpfDtlsList.size()) {
                                pageNo++;
                                // printHeader(innertable, innercell, billDesc, tpfTypeBean.getGpfType(), pageNo, f1);
                                printHeader1(innertable, innercell, crb.getAqmonth() + "", crb.getAqyear(), f1);
                            }
                        }
                    }
                    empcnt = 0;
                    tot = 0;
                }
                printAbstract(innertable, innercell, crb.getBilldesc(), crb.getAqmonth() + "", tpfAbstractList, amount, amountString, crb.getOfficename(), pageNo, f1);
            }

            document.add(innertable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printHeader(PdfPTable table, PdfPCell cell, String billDesc, String gpfType, int pagno, Font f1) throws Exception {

        cell = new PdfPCell(new Phrase("Page: " + pagno, f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(StringUtils.defaultString("TEACHER PROVIDENT FUND"), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("BILL NO: " + StringUtils.defaultString(billDesc), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("SCHEDULE OF " + StringUtils.defaultString(gpfType), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Demand No-8009- State G.P.F Withdrawals", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("1. This form should not be used for transactions of Teacher Provident Fund for which Form No. O.T.C. 63 has been provided. The account Nos. should be arranged in serial order.", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("2. Col. 1 quote account number unfailingly. The guide letters e.g. I.C.S.(ICS Provident Fund) etc., should be unvariably, prefixed to account numbers. ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("3. In the remark column give reasons for discontinuance of subscription such as 'Proceeded on leave', 'Transfered to ........................... office ................... District ....................', 'Quitted service', 'Died or Discontinued under Rule II'. ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("4. In the remark column write description against every new name such as 'New Subscriber', 'came on transfer from ........................... office ................... District ..................... resumed subscription'. ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("5. Separate Schedule should be prepared in respect of person whose accounts are kept by different Accountant-General. ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
    }

    private void printHeader1(PdfPTable table, PdfPCell cell, String billMon, int billYear, Font f1) throws Exception {
        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.defaultString("DEDUCTION MADE FROM THE SALARY FOR " + billMon + " " + billYear), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Sl No", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("ACCOUNT NO\nDATE OF ENTRY\nINTO GOVT. SER.", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("NAME OF THE SUBSCRIBER\nDESIGNATION", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("BASIC PAY\nSCALE OF PAY", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("MONTHLY SUBSCRIPTION", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("REFUND OF\nWITHDRAWALS\nAMT/NO. OF INST.", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("TOTAL RELEASED", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("REMARKS\nD.O.B and D.O.R.", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("1", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("2", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("3", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("4", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("5", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("6", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("7", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("8", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

    }

    private void printGrandTotal(PdfPTable table, PdfPCell cell, int total, String totalFig, Font f1) throws Exception {

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("*Total * :", f1));
        //cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(total + "", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("RUPEES " + StringUtils.defaultString(totalFig) + " ONLY", f1));
        // cell = new PdfPCell(new Phrase("In Words (Ruppes "+StringUtils.upperCase(totalFig+" ) Only");
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setFixedHeight(20);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Certified that all particulars of recovery have been correctly furnished as per the instruction issued in respect of preparation of G.P.F. Schedules. ", f1));
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Voucher No.................................... ", f1));
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("FOR USE IN AUDIT OFFICE", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Certified that the name and account No. of individual deduction and total shown in column - 6 have been checked with ref. to the bill vide page 224 of the Audit Manual. ", f1));
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Certified that the rates of pay shown in column - 4 have been verified with amount drawn in this bill. ", f1));
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("AUDITOR", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
    }

    private void printAbstract(PdfPTable table, PdfPCell cell, String billDesc, String billMonth, List tpfAbstractList, int grandTotal, String totalFig, String offname, int pagno, Font f1) throws Exception {
        int total = 0;
        cell = new PdfPCell(new Phrase("Page: " + pagno, f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(StringUtils.defaultString("TPF ABSTRACT"), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("BILL NO: " + StringUtils.defaultString(billDesc), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("PF CODE ", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(4);
        cell.setBorder(Rectangle.BOX);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("TOTAL AMOUNT ", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(4);
        cell.setBorder(Rectangle.BOX);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        if (tpfAbstractList != null && tpfAbstractList.size() > 0) {
            Iterator itr = tpfAbstractList.iterator();
            TPFScheduleBean tpfSchBean = null;

            while (itr.hasNext()) {
                tpfSchBean = (TPFScheduleBean) itr.next();
                total = total + Integer.parseInt(tpfSchBean.getTotalamt());
                cell = new PdfPCell(new Phrase(tpfSchBean.getPfcode(), new Font(Font.FontFamily.TIMES_ROMAN, 10)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + total, new Font(Font.FontFamily.TIMES_ROMAN, 10)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
        }
        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("*Total * :", f1));
        //cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(grandTotal + "", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("RUPEES " + StringUtils.defaultString(totalFig) + " ONLY", f1));
        // cell = new PdfPCell(new Phrase("In Words (Ruppes "+StringUtils.upperCase(totalFig+" ) Only");
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(30);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Signature of the D.D.O. with Seal ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(offname, new Font(Font.FontFamily.TIMES_ROMAN, 10)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Date:", new Font(Font.FontFamily.TIMES_ROMAN, 10)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Certified that all particulars of recovery have been correctly furnished as per the instruction issued in respect of preparation of G.P.F. Schedules. ", f1));
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Voucher No.................................... ", f1));
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("FOR USE IN AUDIT OFFICE", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Certified that the name and account No. of individual deduction and total shown in column - 6 have been checked with ref. to the bill vide page 224 of the Audit Manual. ", f1));
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Certified that the rates of pay shown in column - 4 have been verified with amount drawn in this bill. ", f1));
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("AUDITOR", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Under Rs " + grandTotal + " " + StringUtils.defaultString(totalFig) + " ONLY", f1));
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Schedule LIII - Form No. 186", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("ORIGINAL/DUPLICATE/TRIPLICATE ", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("(See S.R.s 52)" + total, new Font(Font.FontFamily.TIMES_ROMAN, 10)));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("CHALLAN NO. " + total, new Font(Font.FontFamily.TIMES_ROMAN, 10)));
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("(O.T.C-06)Challan of Cash paid into the Treasury/Sub-Treasury at ", new Font(Font.FontFamily.TIMES_ROMAN, 10)));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", new Font(Font.FontFamily.TIMES_ROMAN, 10)));
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("State/Reserve Bank of India", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", new Font(Font.FontFamily.TIMES_ROMAN, 10)));
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("To be filled in by the remitter ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setColspan(5);
        cell.setBorder(Rectangle.BOX);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("To be filled by the Departmental Officer of the Treasury ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setColspan(3);
        cell.setBorder(Rectangle.BOX);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("By Whom tendered ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Name [Designation] and address of the person \n on whose behalf money is paid  ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Full particulars of the remittance and of authority [if any]   ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Amount    ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Head of account ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Order of the Bank ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Name: \n \n Signature: ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setRotation(90);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(offname, new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Monthly Subscription of TPF for the month of " + billMonth + " of no of \n employees in Bill No -" + billDesc + " \n \n \n Total:", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Rs. " + grandTotal + "\n \n \n  Rs." + grandTotal, new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("8009 State Provident Fund \n & Other Provident Fund \n Misc Provident Fund of the\n employees of the Aided \n Educational Institutions", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setRotation(90);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Signature and full \n designation of the officer\nordering the money to be paid in.\n\nDate- ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setRotation(90);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("1) To be used only in the case of remittances to Bank through an Officer of the Government\n"
                + "(in words) Rs " + grandTotal + "/- (RUPEES " + totalFig + ") ONLY\n"
                + "Rs " + grandTotal + "/- (RUPEES " + totalFig + ") ONLY by T.C to 8009 GPF of AEI ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Received payment\nTreasurer ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Accountant", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Date..............................................Treasury Officer/Agent\n(See Instructions on overleaf) ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
}
