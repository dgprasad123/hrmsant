/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.schedule;

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
import hrms.model.payroll.billbrowser.BillChartOfAccount;
import hrms.model.payroll.billbrowser.BillObjectHead;
import hrms.model.payroll.schedule.BillFrontPageBean;
import hrms.model.payroll.schedule.Schedule;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Surendra
 */
public class BillFrontpageDAOImpl implements BillFrontpageDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String getDPHead(String billNo, int aqMonth, int aqYear) {

        Statement st = null;
        ResultSet rs = null;
        String dphead = "";
        Connection con = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();

            String sql = "Select BT_ID from( (Select AQ_MAST.AQSL_NO from AQ_MAST where AQ_MAST.BILL_NO = '" + billNo + "' AND AQ_MAST.aq_month=" + aqMonth + " AND AQ_MAST.aq_year=" + aqYear + " )"
                    + " AQ_MAST inner join"
                    + " (Select AQ_DTLS.AQSL_NO,AQ_DTLS.AD_AMT,BT_ID from AQ_DTLS where  AD_CODE ='DP' AND AQ_DTLS.aq_month=" + aqMonth + " AND AQ_DTLS.aq_year=" + aqYear + " AND AD_AMT > 0) AQ_DTLS"
                    + " on AQ_MAST.AQSL_NO = AQ_DTLS.AQSL_NO ) GROUP BY BT_ID";
            rs = st.executeQuery(sql);
            if (rs.next()) {
                dphead = rs.getString("BT_ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return dphead;
    }

    public ArrayList getScheduleListWithADCode(String billNo, int aqMonth, int aqYear) {

        Connection con = null;
        ResultSet rs = null;
        ArrayList al = new ArrayList();
        Schedule sc = null;
        Statement st = null;
        String aqDtlsTbl = "";
        try {
            aqDtlsTbl = getAqDtlsTableName(billNo + "");
            con = dataSource.getConnection();
            st = con.createStatement();
            String dedQry = " SELECT AQ_DTLS.SCHEDULE,NOW_DEDN,AD_AMT,BT_ID "
                    + "FROM (SELECT AD_CODE,SCHEDULE,DED_TYPE,NOW_DEDN,SUM(AD_AMT) AD_AMT,BT_ID FROM " + aqDtlsTbl + "  AQ_DTLS INNER JOIN  ("
                    + "SELECT AQ_MAST.AQSL_NO FROM AQ_MAST WHERE  AQ_MAST.BILL_NO=" + billNo + " AND AQ_MAST.aq_month=" + aqMonth + " AND aq_year=" + aqYear + ")AQ_MAST "
                    + " ON AQ_DTLS.AQSL_NO = AQ_MAST.AQSL_NO WHERE AD_TYPE='D' and SCHEDULE != 'PVTL' and schedule != 'PVTD'"
                    + " GROUP BY AQ_DTLS.SCHEDULE,DED_TYPE,NOW_DEDN,AD_CODE,BT_ID ORDER BY AQ_DTLS.SCHEDULE)AQ_DTLS WHERE AD_AMT >0";
            rs = st.executeQuery(dedQry);
            while (rs.next()) {
                String schedule = rs.getString("SCHEDULE");

                if (schedule.equals("GPF")) {
                    sc = new Schedule();
                    sc.setScheduleName("GPF");
                    sc.setObjectHead(StringUtils.defaultString(rs.getString("BT_ID")));
                    sc.setSchAmount(rs.getString("AD_AMT"));
                } else if (schedule.equals("GA")) {
                    sc = new Schedule();
                    sc.setScheduleName("GA");
                    sc.setObjectHead(StringUtils.defaultString(rs.getString("BT_ID")));
                    sc.setSchAmount(rs.getString("AD_AMT"));
                } else if (schedule.equals("TPF")) {
                    sc = new Schedule();
                    sc.setScheduleName("TPF");
                    sc.setObjectHead(StringUtils.defaultString(rs.getString("BT_ID")));
                    sc.addSchAmount(rs.getInt("AD_AMT"));
                } else if (schedule.equals("TPFGA")) {
                    sc = new Schedule();
                    sc.setScheduleName("TPF (P)");
                    sc.setObjectHead(StringUtils.defaultString(rs.getString("BT_ID")));
                    sc.addSchAmount(rs.getInt("AD_AMT"));
                } else if (schedule.equals("CPF")) {
                    sc = new Schedule();
                    sc.setScheduleName("NPS");
                    sc.setObjectHead(StringUtils.defaultString(rs.getString("BT_ID")));
                    sc.addSchAmount(rs.getInt("AD_AMT"));
                } else if (schedule.equals("NPSL")) {
                    sc = new Schedule();
                    sc.setScheduleName("NPS (P)");
                    sc.setObjectHead(StringUtils.defaultString(rs.getString("BT_ID")));
                    sc.addSchAmount(rs.getInt("AD_AMT"));
                } else {
                    sc = new Schedule();
                    sc.setObjectHead(StringUtils.defaultString(rs.getString("BT_ID")));
                    if (rs.getString("NOW_DEDN") != null && !rs.getString("NOW_DEDN").equals("")) {
                        sc.setScheduleName(rs.getString("SCHEDULE") + " (" + rs.getString("NOW_DEDN") + ")");
                    } else {
                        sc.setScheduleName(rs.getString("SCHEDULE"));
                    }
                    sc.setSchAmount(rs.getString("AD_AMT"));
                }
                al.add(sc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    public String getPayHead(int billNo) {

        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String payHead = "";
        try {
            con = dataSource.getConnection();
            String spQry = "select pay_head from bill_mast where bill_no=?";
            pstmt = con.prepareStatement(spQry);
            pstmt.setInt(1, billNo);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                payHead = rs.getString("pay_head");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return payHead;
    }

    public int getSpecialPayAmount(int billNo, int aqMonth, int aqYear) {

        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String aqDtlsTbl = "";
        int amount = 0;
        try {
            con = dataSource.getConnection();
            aqDtlsTbl = getAqDtlsTableName(billNo + "");
            String spQry = "Select sum(AQ_DTLS.AD_AMT) AD_AMT from( (Select AQ_MAST.AQSL_NO from AQ_MAST where AQ_MAST.BILL_NO = ? and aq_month= ? and aq_year=? )AQ_MAST "
                    + "inner join (Select AQSL_NO,AD_AMT from " + aqDtlsTbl + " where SCHEDULE = 'OA'  AND AD_CODE ='SP') AQ_DTLS "
                    + "on AQ_MAST.AQSL_NO = AQ_DTLS.AQSL_NO )";
            pstmt = con.prepareStatement(spQry);
            pstmt.setInt(1, billNo);
            pstmt.setInt(2, aqMonth);
            pstmt.setInt(3, aqYear);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                amount = rs.getInt("AD_AMT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return amount;
    }

    public int getIrAmount(int billNo, int aqMonth, int aqYear) {

        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String aqDtlsTbl = "";
        int amount = 0;
        try {
            aqDtlsTbl = getAqDtlsTableName(billNo + "");

            con = dataSource.getConnection();
            String irQry = "Select sum(AQ_DTLS.AD_AMT) AD_AMT from( (Select AQ_MAST.AQSL_NO from AQ_MAST where AQ_MAST.BILL_NO = ? and aq_month= ? and aq_year=? )AQ_MAST "
                    + "inner join (Select AQSL_NO,AD_AMT from " + aqDtlsTbl + " where SCHEDULE = 'OA'  AND AD_CODE ='IR') AQ_DTLS "
                    + "on AQ_MAST.AQSL_NO = AQ_DTLS.AQSL_NO )";
            pstmt = con.prepareStatement(irQry);
            pstmt.setInt(1, billNo);
            pstmt.setInt(2, aqMonth);
            pstmt.setInt(3, aqYear);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                amount = rs.getInt("AD_AMT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return amount;
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

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(aqMonth, aqYear);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aqDTLS;
    }

    @Override
    public int getEmployeeCount(String offcode, String postgrptype, int billId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int totalCount = 0;
        try {
            con = this.dataSource.getConnection();

            /*String sql = " SELECT COUNT(emp_id) cnt FROM AQ_MAST "
             + " INNER JOIN EMP_MAST ON AQ_MAST.EMP_CODE=EMP_MAST.EMP_ID"
             + " WHERE BILL_NO=? AND post_grp_type=? AND IS_REGULAR='Y'";*/
            String sql = "SELECT COUNT(emp_id) cnt FROM AQ_MAST\n"
                    + "INNER JOIN EMP_MAST ON AQ_MAST.EMP_CODE=EMP_MAST.EMP_ID\n"
                    + "LEFT OUTER JOIN G_SPC ON AQ_MAST.CUR_SPC=G_SPC.SPC\n"
                    + "WHERE BILL_NO=? AND post_grp_type=? \n"
                    + "AND (IS_REGULAR='Y' OR IS_REGULAR='W' OR IS_REGULAR='G' OR IS_REGULAR='A')";
            pst = con.prepareStatement(sql);
            pst.setInt(1, billId);
            pst.setString(2, postgrptype);
            rs = pst.executeQuery();
            if (rs.next()) {
                totalCount = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return totalCount;
    }

    @Override
    public double getAmtCreditedToBeneficiary(int billNo, int aqMonth, int aqYear) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        double amt = 0.0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select sum(ad_amt) amt from aq_dtls"
                    + " inner join aq_mast on aq_dtls.aqsl_no=aq_mast.aqsl_no where bill_no=? and aq_mast.aq_year=? and aq_mast.aq_month=? and (schedule != 'PVTL' or schedule != 'PVTD')";
            pst = con.prepareStatement(sql);
            pst.setInt(1, billNo);
            pst.setInt(2, aqYear);
            pst.setInt(3, aqMonth);
            rs = pst.executeQuery();
            if (rs.next()) {
                amt = rs.getInt("amt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return amt;
    }

    @Override
    public double getAmtCreditedToDDO(int billNo, int aqMonth, int aqYear) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        double amt = 0.0;
        try {
            con = this.dataSource.getConnection();
            String aqdtlsTbl = AqFunctionalities.getAQBillDtlsTable(aqMonth, aqYear);
            String sql = "select sum(ad_amt) amt from " + aqdtlsTbl + " aq_dtls"
                    + " inner join aq_mast on aq_dtls.aqsl_no=aq_mast.aqsl_no where bill_no=? and aq_mast.aq_year=? and aq_mast.aq_month=? and (schedule = 'PVTL' or schedule = 'PVTD')";
            pst = con.prepareStatement(sql);
            pst.setInt(1, billNo);
            pst.setInt(2, aqYear);
            pst.setInt(3, aqMonth);
            rs = pst.executeQuery();
            if (rs.next()) {
                amt = rs.getInt("amt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return amt;
    }

    @Override
    public int getContractualEmployeeCount(String offcode, String type, int billId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int totalCount = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = " SELECT COUNT(emp_id) cnt FROM AQ_MAST "
                    + " INNER JOIN EMP_MAST ON AQ_MAST.EMP_CODE=EMP_MAST.EMP_ID"
                    + " WHERE BILL_NO=? AND IS_REGULAR=?";;
            pst = con.prepareStatement(sql);
            pst.setInt(1, billId);
            pst.setString(2, type);
            rs = pst.executeQuery();
            if (rs.next()) {
                totalCount = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return totalCount;

    }

    @Override
    public void singlebillFrontPagePDF(Document document, String billMonth, String billNo, BillChartOfAccount billChartOfAccount, BillObjectHead boha, List scheduleList, List scheduleListTR, List oaList, int spAmt, int irAmt, int payAmt, String payHead, String offCode, int aqMonth, int aqYear) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        BillFrontPageBean bfpbean = new BillFrontPageBean();

        try {
            con = this.dataSource.getConnection();
            getBenRefNo(bfpbean, billNo);
            getDescriptionDetails(bfpbean, billNo);

            Font f1 = new Font();
            f1.setSize(8.5f);
            f1.setFamily("Times New Roman");

            Font f1small = new Font();
            f1small.setSize(7);
            f1small.setFamily("Times New Roman");

            Font f2 = new Font();
            f2.setSize(9);

            Font f3 = new Font();
            f3.setSize(10);
            f3.setFamily("Times New Roman");
            f3.setStyle(Font.BOLD);

            //start of first table
            PdfPTable maintable = new PdfPTable(4);
            maintable.setWidths(new int[]{3, 2, 2, 1});
            maintable.setWidthPercentage(100);

            PdfPCell maincell = null;

            //START OF FIRST MAIN ROW
            maincell = new PdfPCell(new Phrase("Schedule LIII - Form No. 188", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase(StringUtils.defaultString(billChartOfAccount.getDdoName()), f1));
            //maincell.setBorder(Rectangle.NO_BORDER);
            maincell.setHorizontalAlignment(Element.ALIGN_CENTER);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("Bill No: " + StringUtils.defaultString(billChartOfAccount.getBilldesc()), new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            maincell.setBorder(Rectangle.NO_BORDER);
            maincell.setHorizontalAlignment(Element.ALIGN_CENTER);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("P", new Font(Font.FontFamily.TIMES_ROMAN, 30, Font.NORMAL)));
            maincell.setRowspan(3);
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("Detailed Pay Bill of Permanent/Temporary Establishment of the " + StringUtils.defaultString(billChartOfAccount.getOffName()), new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.NORMAL)));
            maincell.setColspan(2);
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("(O.T.C.Form No.22)", new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD)));
            maincell.setBorder(Rectangle.NO_BORDER);
            maincell.setHorizontalAlignment(Element.ALIGN_CENTER);
            maintable.addCell(maincell);

            /*maincell = new PdfPCell(new Phrase("",f1));
             maincell.setBorder(Rectangle.NO_BORDER);
             maintable.addCell(maincell);*/
            maincell = new PdfPCell(new Phrase("for the month of " + billMonth, new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.NORMAL)));
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("", f1));
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("District : " + StringUtils.defaultString(billChartOfAccount.getDistrict()), new Font(Font.FontFamily.TIMES_ROMAN, 6, Font.NORMAL)));
            maincell.setColspan(2);
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("Ben Ref No : " + StringUtils.defaultString(billChartOfAccount.getBenRefNo()), f2));
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("Token No : " + StringUtils.defaultString(billChartOfAccount.getTokenNo()), f2));
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("HRMS Bill Id : " + StringUtils.defaultString(billNo), f2));
            maincell.setColspan(2);
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell();
            maincell.setColspan(2);
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            if (billChartOfAccount.getBillType() != null && !billChartOfAccount.getBillType().equals("22")) {
                if (billChartOfAccount.getTokenNo() == null || billChartOfAccount.getTokenNo().equals("")) {
                    //maincell = new PdfPCell(new Phrase("DRAFT", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                    maincell = new PdfPCell();
                    maincell.setBorder(Rectangle.NO_BORDER);
                    maintable.addCell(maincell);
                } else {
                    maincell = new PdfPCell();
                    maincell.setBorder(Rectangle.NO_BORDER);
                    maintable.addCell(maincell);
                }
            } else {
                maincell = new PdfPCell();
                maincell.setBorder(Rectangle.NO_BORDER);
                maintable.addCell(maincell);
            }

            maincell = new PdfPCell();
            maincell.setColspan(2);
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            //end of first table
            document.add(maintable);
            //END OF FIRST MAIN ROW

            //START OF SECOND MAIN ROW
            //start of second table
            maintable = new PdfPTable(2);
            maintable.setWidths(new float[]{6.5f, 4.5f});
            maintable.setWidthPercentage(100);

            //PdfPCell maincell = null;
            //start of Creating Space
            maincell = new PdfPCell();
            maincell.setColspan(2);
            maincell.setFixedHeight(10);
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            //end of Creating Space

            //start of first row under SECOND MAIN ROW
            maincell = new PdfPCell(new Phrase("Name of detailed heads filled in by the Drawing Officer", f1));
            maincell.setBorder(Rectangle.TOP | Rectangle.RIGHT);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase(StringUtils.leftPad("Unit-wise Expenditure & Deduction Details", 50), f1));
            maincell.setBorder(Rectangle.TOP);
            maincell.setHorizontalAlignment(Rectangle.ALIGN_LEFT);
            maintable.addCell(maincell);
            //end of first row under SECOND MAIN ROW
            //start of Creating Space
            maincell = new PdfPCell();
            maincell.setFixedHeight(10);
            maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.BOTTOM);
            maintable.addCell(maincell);
            //end of Creating Space

            //start of second row under SECOND MAIN ROW
            //start of 1st inner table under second table for column 1
            PdfPTable innertable1 = new PdfPTable(2);
            innertable1.setWidths(new int[]{1, 1});
            innertable1.setWidthPercentage(100);

            PdfPCell innercell = null;

            innercell = new PdfPCell(new Phrase("Demand No", f1));
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable1.addCell(innercell);
            innercell = new PdfPCell(new Phrase("- " + StringUtils.defaultString(billChartOfAccount.getDemandNo()), f1));
            innercell.setBorder(Rectangle.RIGHT);
            innertable1.addCell(innercell);

            innercell = new PdfPCell(new Phrase("Major Head", f1));
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable1.addCell(innercell);
            innercell = new PdfPCell(new Phrase("- " + StringUtils.defaultString(billChartOfAccount.getMajorHead()), f1));
            innercell.setBorder(Rectangle.RIGHT);
            innertable1.addCell(innercell);

            innercell = new PdfPCell(new Phrase("Sub Major Head", f1));
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable1.addCell(innercell);
            innercell = new PdfPCell(new Phrase("- " + StringUtils.defaultString(billChartOfAccount.getSubMajorHead()), f1));
            innercell.setBorder(Rectangle.RIGHT);
            innertable1.addCell(innercell);

            innercell = new PdfPCell(new Phrase("Minor Head", f1));
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable1.addCell(innercell);
            innercell = new PdfPCell(new Phrase("- " + StringUtils.defaultString(billChartOfAccount.getMinorHead()), f1));
            innercell.setBorder(Rectangle.RIGHT);
            innertable1.addCell(innercell);

            innercell = new PdfPCell(new Phrase("Sub head", f1));
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable1.addCell(innercell);
            innercell = new PdfPCell(new Phrase("- " + StringUtils.defaultString(billChartOfAccount.getSubMinorHead1()), f1));
            innercell.setBorder(Rectangle.RIGHT);
            innertable1.addCell(innercell);

            innercell = new PdfPCell(new Phrase("Detail Head", f1));
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable1.addCell(innercell);
            innercell = new PdfPCell(new Phrase("- " + StringUtils.defaultString(billChartOfAccount.getSubMinorHead2()), f1));
            innercell.setBorder(Rectangle.RIGHT);
            innertable1.addCell(innercell);

            innercell = new PdfPCell(new Phrase("Plan Status", f1));
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable1.addCell(innercell);
            innercell = new PdfPCell(new Phrase("- " + StringUtils.defaultString(billChartOfAccount.getPlanName()), f1));
            innercell.setBorder(Rectangle.RIGHT);
            innertable1.addCell(innercell);

            innercell = new PdfPCell(new Phrase("Charge/Voted", f1));
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable1.addCell(innercell);
            innercell = new PdfPCell(new Phrase("- " + StringUtils.defaultString(billChartOfAccount.getSubMinorHead3()), f1));
            innercell.setBorder(Rectangle.RIGHT);
            innertable1.addCell(innercell);

            innercell = new PdfPCell(new Phrase("Sector", f1));
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable1.addCell(innercell);
            innercell = new PdfPCell(new Phrase("- " + StringUtils.defaultString(billChartOfAccount.getSectorName()), f1));
            innercell.setBorder(Rectangle.RIGHT);
            innertable1.addCell(innercell);
            //end of 1st inner table under second table for column1    
            maincell = new PdfPCell(innertable1);
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            //start of second inner table under second table for column 2
            innertable1 = new PdfPTable(3);
            innertable1.setWidths(new float[]{3, 1.5f, 0.5f});
            innertable1.setWidthPercentage(100);

            //PdfPCell innercell = null;
            innercell = new PdfPCell();
            innercell.setBorder(Rectangle.TOP);
            innertable1.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Rs", f1));
            innercell.setBorder(Rectangle.TOP | Rectangle.BOTTOM | Rectangle.LEFT);
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innertable1.addCell(innercell);
            innercell = new PdfPCell(new Phrase("P", f1));
            innercell.setBorder(Rectangle.TOP | Rectangle.BOTTOM | Rectangle.LEFT);
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innertable1.addCell(innercell);

            innercell = new PdfPCell(new Phrase("Pay of Permanent Establishment", f1));
            innercell.setBorder(Rectangle.RIGHT);
            innertable1.addCell(innercell);
            innercell = new PdfPCell();
            innercell.setBorder(Rectangle.RIGHT);
            innertable1.addCell(innercell);
            innercell = new PdfPCell();
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable1.addCell(innercell);

            innercell = new PdfPCell(new Phrase("Pay of temporary Establishment", f1));
            innercell.setBorder(Rectangle.RIGHT);
            innertable1.addCell(innercell);
            innercell = new PdfPCell();
            innercell.setBorder(Rectangle.RIGHT);
            innertable1.addCell(innercell);
            innercell = new PdfPCell();
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable1.addCell(innercell);

            Schedule obj1 = null;
            int amt = 0;
            double oaAmt = 0.0;
            if (oaList != null && oaList.size() > 0) {
                for (int i = 0; i < oaList.size(); i++) {

                    obj1 = (Schedule) oaList.get(i);
                    if (obj1.getObjectHead() != null && obj1.getObjectHead().equals("136")) {
                        amt = amt + Integer.parseInt(obj1.getSchAmount());
                    }
                    if (obj1.getObjectHead() != null && obj1.getObjectHead().equals("000")) {
                        amt = amt + Integer.parseInt(obj1.getSchAmount());
                    }
                    oaAmt = obj1.getAlowanceTotal();
                }
            }
            payAmt = payAmt + amt;
            String amtTobeDisplay = String.valueOf(payAmt);
            oaAmt = payAmt + spAmt + irAmt + (oaAmt - amt);

            Schedule objD = null;
            double deductAmt = 0.0;
            if (scheduleList != null && scheduleList.size() > 0) {
                for (int i = 0; i < scheduleList.size(); i++) {
                    objD = (Schedule) scheduleList.get(i);
                    double schAmt = Double.parseDouble(objD.getSchAmount());
                    deductAmt = deductAmt + schAmt;
                }
            }

            String totDeductAmt[] = CommonFunctions.getRupessAndPaise(String.valueOf(deductAmt));
            double netAmt = oaAmt - deductAmt;
            String netAmount[] = CommonFunctions.getRupessAndPaise(String.valueOf(netAmt));
            String totOaAmtInString = Double.valueOf(oaAmt + "").longValue() + "";
            String totNetAmtInString = Double.valueOf(netAmt + "").longValue() + "";
            String benCreditedAmount = Double.valueOf(netAmt - getAmtCreditedToDDO(Integer.parseInt(billNo), aqMonth, aqYear)).longValue() + "";
            if (payHead == null) {
                payHead = "";
            }

            if (payAmt > 0) {
                innercell = new PdfPCell(new Phrase(payHead + StringUtils.repeat(" ", 30) + StringUtils.defaultString("- PAY"), f2));
                innercell.setBorder(Rectangle.RIGHT);
                innertable1.addCell(innercell);
                innercell = new PdfPCell(new Phrase(payAmt + "", f2));
                innercell.setBorder(Rectangle.RIGHT);
                innercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                innertable1.addCell(innercell);
                innercell = new PdfPCell();
                innercell.setBorder(Rectangle.NO_BORDER);
                innertable1.addCell(innercell);
            }

            if (spAmt > 0) {
                innercell = new PdfPCell(new Phrase("136" + StringUtils.repeat(" ", 30) + StringUtils.defaultString("- SP"), f2));
                innercell.setBorder(Rectangle.RIGHT);
                innertable1.addCell(innercell);
                innercell = new PdfPCell(new Phrase(spAmt + "", f2));
                innercell.setBorder(Rectangle.RIGHT);
                innercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                innertable1.addCell(innercell);
                innercell = new PdfPCell();
                innercell.setBorder(Rectangle.NO_BORDER);
                innertable1.addCell(innercell);
            }

            if (irAmt > 0) {
                innercell = new PdfPCell(new Phrase("136" + StringUtils.repeat(" ", 30) + StringUtils.defaultString("- IR"), f2));
                innercell.setBorder(Rectangle.RIGHT);
                innertable1.addCell(innercell);
                innercell = new PdfPCell(new Phrase(irAmt + "", f2));
                innercell.setBorder(Rectangle.RIGHT);
                innercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                innertable1.addCell(innercell);
                innercell = new PdfPCell();
                innercell.setBorder(Rectangle.NO_BORDER);
                innertable1.addCell(innercell);
            }

            Schedule obj = null;
            if (oaList != null && oaList.size() > 0) {
                for (int i = 0; i < oaList.size(); i++) {
                    obj = (Schedule) oaList.get(i);

                    if (!obj.getObjectHead().equals("136") && !obj.getObjectHead().equals("000")) {

                        innercell = new PdfPCell(new Phrase(obj.getObjectHead() + StringUtils.repeat(" ", 30) + StringUtils.defaultString(obj.getScheduleName()), f2));
                        innercell.setBorder(Rectangle.RIGHT);
                        innertable1.addCell(innercell);

                        innercell = new PdfPCell(new Phrase(obj.getSchAmount(), f2));
                        innercell.setBorder(Rectangle.RIGHT);
                        innercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        innertable1.addCell(innercell);

                        innercell = new PdfPCell();
                        innercell.setBorder(Rectangle.NO_BORDER);
                        innertable1.addCell(innercell);
                    }
                }
            }

            innercell = new PdfPCell(new Phrase("Total", f1));
            innercell.setBorder(Rectangle.TOP | Rectangle.RIGHT);
            innercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            innertable1.addCell(innercell);
            innercell = new PdfPCell(new Phrase(StringUtils.defaultString(totOaAmtInString + ""), f1));
            innercell.setBorder(Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
            innercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            innertable1.addCell(innercell);
            //innercell = new PdfPCell(new Phrase(StringUtils.defaultString(billFrForm.getTotalPaice()), f1));
            innercell = new PdfPCell();
            innercell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            innercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            innertable1.addCell(innercell);

            maincell = new PdfPCell(innertable1);
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            //end of second row under SECOND MAIN ROW

            //start of third row under SECOND MAIN ROW
            maincell = new PdfPCell(new Phrase("\nN.B", f1));
            maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);

            //start of inner table
            innertable1 = new PdfPTable(3);
            innertable1.setWidths(new float[]{3, 1.5f, 0.5f});
            innertable1.setWidthPercentage(100);

            innercell = new PdfPCell(new Phrase("\nDeduct-", f1));
            innercell.setBorder(Rectangle.RIGHT);
            innertable1.addCell(innercell);

            innercell = new PdfPCell();
            innercell.setBorder(Rectangle.RIGHT);
            innertable1.addCell(innercell);

            innercell = new PdfPCell();
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable1.addCell(innercell);
            //end of inner table
            maincell = new PdfPCell(innertable1);
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            //start of third row under SECOND MAIN ROW

            //start of fourth row under SECOND MAIN ROW
            maincell = new PdfPCell(new Phrase("", f1));
            maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);

            //start of third inner table under second inner table under second table for column 2
            innertable1 = new PdfPTable(4);
            innertable1.setWidths(new float[]{2, 1, 1.5f, 0.5f});
            innertable1.setWidthPercentage(100);
            //ArrayList 
            if (scheduleList != null && scheduleList.size() > 0) {
                Iterator itr = scheduleList.iterator();
                while (itr.hasNext()) {
                    obj = (Schedule) itr.next();
                    innercell = new PdfPCell(new Phrase(StringUtils.defaultString(obj.getObjectHead()), f2));
                    innercell.setBorder(Rectangle.NO_BORDER);
                    innertable1.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(StringUtils.defaultString(obj.getScheduleName()), f2));
                    innercell.setBorder(Rectangle.RIGHT);
                    innercell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    innertable1.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(StringUtils.defaultString(obj.getSchAmount()), f2));
                    innercell.setBorder(Rectangle.RIGHT);
                    innercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    innertable1.addCell(innercell);
                    innercell = new PdfPCell();
                    innercell.setBorder(Rectangle.NO_BORDER);
                    innertable1.addCell(innercell);
                }
            }

            innercell = new PdfPCell(new Phrase("\n\nTotal deductions", f1));
            innercell.setColspan(2);
            innercell.setBorder(Rectangle.RIGHT);
            innertable1.addCell(innercell);
            //innercell = new PdfPCell(new Phrase("\n\n" + StringUtils.defaultString(Double.valueOf(totDeductAmt[0]).longValue() + ""), f1));
            innercell = new PdfPCell(new Phrase("\n\n" + Double.valueOf(deductAmt + "").longValue() + "", f1));
            innercell.setBorder(Rectangle.RIGHT | Rectangle.TOP | Rectangle.BOTTOM);
            innercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            innertable1.addCell(innercell);
            innercell = new PdfPCell();
            innercell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            innercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            innertable1.addCell(innercell);

            innercell = new PdfPCell(new Phrase("\nNet Total", f1));
            innercell.setColspan(2);
            innercell.setBorder(Rectangle.RIGHT);
            innertable1.addCell(innercell);
            innercell = new PdfPCell(new Phrase("\n" + StringUtils.defaultString(totNetAmtInString + ""), f1));
            innercell.setBorder(Rectangle.RIGHT | Rectangle.TOP);
            innercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            innertable1.addCell(innercell);
            //innercell = new PdfPCell(new Phrase("\n" + StringUtils.defaultString(billFrForm.getNetPaice()), f1));
            innercell = new PdfPCell();
            innercell.setBorder(Rectangle.TOP);
            innercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            innertable1.addCell(innercell);
            //end of inner table for column 2 for fifth row under SECOND MAIN ROW

            maincell = new PdfPCell(innertable1);
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            //end of fifth row under SECOND MAIN ROW

            document.add(maintable);

            maintable = new PdfPTable(5);
            maintable.setWidths(new float[]{1, 0.5f, 0.5f, 1, 0.5f});
            maintable.setWidthPercentage(100);

            maincell = new PdfPCell();
            maincell.setColspan(5);
            maincell.setBorder(Rectangle.NO_BORDER);
            maincell.setFixedHeight(30);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("Category of employees", f3));
            //maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase("Number", f3));
            //maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase("Amount Credited to Beneficiary Account", f1));
            //maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase(benCreditedAmount + "", f1));
            //maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("Group - A", f1));
            //maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase(getEmployeeCount(offCode, "A", Integer.parseInt(billNo)) + "", f1));
            //maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase("Amount Credited to DDO Account", f1));
            //maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase(Double.valueOf(getAmtCreditedToDDO(Integer.parseInt(billNo), aqMonth, aqYear)).longValue() + "", f1));
            //maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("Group - B", f1));
            //maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase(getEmployeeCount(offCode, "B", Integer.parseInt(billNo)) + "", f1));
            //maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase("TOTAL(Net Amount)", f1));
            //maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase(StringUtils.defaultString(totNetAmtInString + ""), f1));
            //maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("Group - C", f1));
            //maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase(getEmployeeCount(offCode, "C", Integer.parseInt(billNo)) + "", f1));
            //maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("Group - D", f1));
            //maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase(getEmployeeCount(offCode, "D", Integer.parseInt(billNo)) + "", f1));
            //maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("Contractual", f1));
            //maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase(getContractualEmployeeCount(offCode, "C", Integer.parseInt(billNo)) + "", f1));
            //maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("Consolidated", f1));
            //maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase(getContractualEmployeeCount(offCode, "N", Integer.parseInt(billNo)) + "", f1));
            //maincell.setBorder(Rectangle.RIGHT);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            document.add(maintable);
            /*
             maintable = new PdfPTable(4);
             maintable.setWidths(new float[]{1, 1, 1, 1});
             maintable.setWidthPercentage(80);

             maincell = new PdfPCell();
             maincell.setColspan(4);
             maincell.setFixedHeight(20);
             maincell.setBorder(Rectangle.NO_BORDER);
             maintable.addCell(maincell);

             maincell = new PdfPCell(new Phrase("DETAIL OF PAY OF ABSENTEES REFUNDED", f1));
             maincell.setColspan(4);
             maincell.setHorizontalAlignment(Element.ALIGN_CENTER);
             maintable.addCell(maincell);

             maincell = new PdfPCell(new Phrase("Transaction of Establishment", f1));
             maincell.setHorizontalAlignment(Element.ALIGN_CENTER);
             maintable.addCell(maincell);
             maincell = new PdfPCell(new Phrase("Name of the Incumbent", f1));
             maincell.setHorizontalAlignment(Element.ALIGN_CENTER);
             maintable.addCell(maincell);
             maincell = new PdfPCell(new Phrase("Period", f1));
             maincell.setHorizontalAlignment(Element.ALIGN_CENTER);
             maintable.addCell(maincell);
             maincell = new PdfPCell(new Phrase("Amount", f1));
             maincell.setHorizontalAlignment(Element.ALIGN_CENTER);
             maintable.addCell(maincell);

             document.add(maintable);
             */
            maintable = new PdfPTable(3);
            maintable.setWidths(new float[]{0.1f, 0.3f, 1});
            maintable.setWidthPercentage(80);

            maincell = new PdfPCell();
            maincell.setColspan(3);
            maincell.setFixedHeight(20);
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase("Rupees(in words): ", f1));
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase(Numtowordconvertion.convertNumber(new Double(netAmt).intValue()).toUpperCase(), f1small));
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            double netAmtInWord = netAmt + 1;

            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase("Under Rupees: ", f1));
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase(Numtowordconvertion.convertNumber(new Double(netAmtInWord).intValue()).toUpperCase(), f1small));
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            document.add(maintable);

            maintable = new PdfPTable(3);
            maintable.setWidths(new float[]{0.3f, 0.8f, 0.5f});
            maintable.setWidthPercentage(80);

            maincell = new PdfPCell();
            maincell.setColspan(3);
            maincell.setFixedHeight(20);
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("Received content", f1));
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase(billChartOfAccount.getDdoEmpName() + ", " + billChartOfAccount.getDdoPostName(), f1small));
            maincell.setColspan(2);
            maincell.setBorder(Rectangle.NO_BORDER);
            maincell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            maintable.addCell(maincell);

            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase("Signature of the DDO", f1));
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("Received payment", f1));
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            document.add(maintable);
            document.newPage();

            maintable = new PdfPTable(4);
            maintable.setWidths(new float[]{0.3f, 1, 1, 1});
            maintable.setWidthPercentage(80);

            maincell = new PdfPCell(new Phrase("Pay Order of the Treasury Officer", f3));
            maincell.setColspan(4);
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 20) + "Net payment credited to Bank Account Rs " + Double.valueOf(netAmt + "").longValue() + "", f1));
            maincell.setColspan(3);
            maincell.setFixedHeight(20);
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 20) + "Deductions(Treasury by transfers)", f1));
            maincell.setColspan(3);
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            document.add(maintable);

            maintable = new PdfPTable(4);
            maintable.setWidths(new float[]{0.3f, 0.5f, 0.5f, 0.5f});
            maintable.setWidthPercentage(80);

            maincell = new PdfPCell();
            maincell.setColspan(4);
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase("Nature of Deductions", f1));
            //maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase("Amount", f1));
            //maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            double deductAmtTR = 0.0;
            if (scheduleListTR != null && scheduleListTR.size() > 0) {
                for (int i = 0; i < scheduleListTR.size(); i++) {
                    objD = (Schedule) scheduleListTR.get(i);
                    double schAmt = Double.parseDouble(objD.getSchAmount());
                    deductAmtTR = deductAmtTR + schAmt;
                }
            }

            if (scheduleListTR != null && scheduleListTR.size() > 0) {
                Iterator itr = scheduleListTR.iterator();
                while (itr.hasNext()) {
                    obj = (Schedule) itr.next();
                    maincell = new PdfPCell();
                    maincell.setBorder(Rectangle.NO_BORDER);
                    maintable.addCell(maincell);
                    maincell = new PdfPCell(new Phrase(StringUtils.defaultString(obj.getScheduleName()), f1));
                    maincell.setBorder(Rectangle.NO_BORDER);
                    maintable.addCell(maincell);
                    maincell = new PdfPCell(new Phrase(StringUtils.defaultString(obj.getSchAmount()), f1));
                    maincell.setBorder(Rectangle.NO_BORDER);
                    maintable.addCell(maincell);
                    maincell = new PdfPCell();
                    maincell.setBorder(Rectangle.NO_BORDER);
                    maintable.addCell(maincell);
                }
            }

            //deductAmtTR = deductAmtTR + netAmt;
            //Double benCreditedAmountTemp = Double.parseDouble(benCreditedAmount);
            //deductAmtTR = deductAmtTR + benCreditedAmountTemp;
            deductAmtTR = deductAmtTR + netAmt;

            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase("TOTAL(Treasury Gross)", f1));
            //maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase(Double.valueOf(deductAmtTR + "").longValue() + "", f1));
            //maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);
            maincell = new PdfPCell();
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            document.add(maintable);

            maintable = new PdfPTable(3);
            maintable.setWidths(new float[]{1, 1, 1});
            maintable.setWidthPercentage(80);

            maincell = new PdfPCell();
            maincell.setColspan(3);
            maincell.setFixedHeight(10);
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("Rupees in Words: " + Numtowordconvertion.convertNumber(new Double(deductAmtTR).intValue()).toUpperCase(), f1));
            maincell.setBorder(Rectangle.NO_BORDER);
            maincell.setColspan(3);
            maincell.setHorizontalAlignment(Element.ALIGN_CENTER);
            maintable.addCell(maincell);

            maincell = new PdfPCell();
            maincell.setColspan(3);
            maincell.setFixedHeight(50);
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("Examined and Entered", f1));
            maincell.setBorder(Rectangle.NO_BORDER);
            maincell.setHorizontalAlignment(Element.ALIGN_CENTER);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase("Treasury Accountant", f1));
            maincell.setBorder(Rectangle.NO_BORDER);
            maincell.setHorizontalAlignment(Element.ALIGN_CENTER);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase("Treasury Officer", f1));
            maincell.setBorder(Rectangle.NO_BORDER);
            maincell.setHorizontalAlignment(Element.ALIGN_CENTER);
            maintable.addCell(maincell);

            maincell = new PdfPCell();
            maincell.setColspan(3);
            maincell.setFixedHeight(20);
            maincell.setBorder(Rectangle.NO_BORDER);
            maintable.addCell(maincell);

            //START OF THIRD MAIN ROW
            maincell = new PdfPCell(new Phrase("FOR THE USE OF THE ACCOUNTANT GENERAL'S OFFICE", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            maincell.setColspan(3);
            maincell.setBorder(Rectangle.NO_BORDER);
            maincell.setHorizontalAlignment(Element.ALIGN_CENTER);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("Admitted Rs.", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            maincell.setColspan(3);
            maincell.setBorder(Rectangle.NO_BORDER);
            maincell.setHorizontalAlignment(Element.ALIGN_LEFT);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("Object Rs.", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            maincell.setColspan(3);
            maincell.setBorder(Rectangle.NO_BORDER);
            maincell.setHorizontalAlignment(Element.ALIGN_LEFT);
            maintable.addCell(maincell);

            maincell = new PdfPCell(new Phrase("Auditor", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            maincell.setBorder(Rectangle.NO_BORDER);
            maincell.setHorizontalAlignment(Element.ALIGN_LEFT);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase("Superitendent", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            maincell.setBorder(Rectangle.NO_BORDER);
            maincell.setHorizontalAlignment(Element.ALIGN_LEFT);
            maintable.addCell(maincell);
            maincell = new PdfPCell(new Phrase("Gazetted Officer", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            maincell.setBorder(Rectangle.NO_BORDER);
            maincell.setHorizontalAlignment(Element.ALIGN_LEFT);
            maintable.addCell(maincell);

            //END OF THIRD MAIN ROW
            document.add(maintable);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    private BillFrontPageBean getBenRefNo(BillFrontPageBean bfpbean, String billNo) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT BEN_REF_NO,TOKEN_NO,BILL_TYPE FROM BILL_MAST WHERE BILL_NO=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billNo));
            rs = pst.executeQuery();
            if (rs.next()) {
                bfpbean.setRefno(rs.getString("BEN_REF_NO"));
                bfpbean.setTokenNo(rs.getString("TOKEN_NO"));
                bfpbean.setBillType(rs.getString("BILL_TYPE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return bfpbean;
    }

    private BillFrontPageBean getDescriptionDetails(BillFrontPageBean bfpbean, String billNo) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT OFF_EN,DIST_NAME,DDO_CODE,BILL_MAST.MAJOR_HEAD, BILL_MAST.MAJOR_HEAD_DESC, BILL_MAST.PLAN, POST_TYPE, BILL_MAST.SECTOR,SECTOR_DESC, BILL_MAST.SUB_MAJOR_HEAD,"
                    + " BILL_MAST.SUB_MAJOR_HEAD_DESC, BILL_MAST.MINOR_HEAD, BILL_MAST.MINOR_HEAD_DESC, BILL_MAST.SUB_MINOR_HEAD1, BILL_MAST.SUB_MINOR_HEAD1_DESC,"
                    + " BILL_MAST.SUB_MINOR_HEAD2, BILL_MAST.SUB_MINOR_HEAD2_DESC, BILL_MAST.SUB_MINOR_HEAD3, BILL_MAST.BILL_TYPE,DEMAND_NO FROM("
                    + " SELECT G_OFFICE.OFF_EN,G_OFFICE.DIST_CODE,BILL_MAST.MAJOR_HEAD, BILL_MAST.MAJOR_HEAD_DESC, BILL_MAST.PLAN, BILL_MAST.SECTOR, BILL_MAST.SUB_MAJOR_HEAD,"
                    + " BILL_MAST.SUB_MAJOR_HEAD_DESC, BILL_MAST.MINOR_HEAD, BILL_MAST.MINOR_HEAD_DESC, BILL_MAST.SUB_MINOR_HEAD1, BILL_MAST.SUB_MINOR_HEAD1_DESC,"
                    + " BILL_MAST.SUB_MINOR_HEAD2, BILL_MAST.SUB_MINOR_HEAD2_DESC, BILL_MAST.SUB_MINOR_HEAD3, BILL_MAST.BILL_TYPE,G_OFFICE.DDO_CODE,DEMAND_NO FROM ("
                    + " SELECT BILL_MAST.MAJOR_HEAD, BILL_MAST.MAJOR_HEAD_DESC, BILL_MAST.PLAN, BILL_MAST.SECTOR, BILL_MAST.SUB_MAJOR_HEAD,"
                    + " BILL_MAST.SUB_MAJOR_HEAD_DESC, BILL_MAST.MINOR_HEAD, BILL_MAST.MINOR_HEAD_DESC, BILL_MAST.SUB_MINOR_HEAD1, BILL_MAST.SUB_MINOR_HEAD1_DESC,"
                    + " BILL_MAST.SUB_MINOR_HEAD2, BILL_MAST.SUB_MINOR_HEAD2_DESC, BILL_MAST.SUB_MINOR_HEAD3, BILL_MAST.BILL_TYPE,DDO_CODE,DEMAND_NO,OFF_CODE FROM BILL_MAST WHERE BILL_NO=?) BILL_MAST"
                    + " LEFT OUTER JOIN G_OFFICE ON BILL_MAST.OFF_CODE=G_OFFICE.OFF_CODE) BILL_MAST"
                    + " LEFT OUTER JOIN G_DISTRICT ON BILL_MAST.DIST_CODE=G_DISTRICT.DIST_CODE"
                    + " LEFT OUTER JOIN G_SECTOR ON BILL_MAST.SECTOR=G_SECTOR.SECTOR_CODE"
                    + " LEFT OUTER JOIN G_POST_TYPE ON BILL_MAST.PLAN=G_POST_TYPE.POST_CODE";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billNo));
            rs = pst.executeQuery();
            if (rs.next()) {
                bfpbean.setDdoName(rs.getString("DDO_CODE"));
                bfpbean.setOffName(rs.getString("OFF_EN"));
                bfpbean.setDistrictName(rs.getString("DIST_NAME"));
                bfpbean.setMajorHead(rs.getString("MAJOR_HEAD"));
                bfpbean.setMajorHeadDesc(rs.getString("MAJOR_HEAD_DESC"));
                bfpbean.setPlan(rs.getString("PLAN"));
                bfpbean.setSector(rs.getString("SECTOR"));
                bfpbean.setPlanName(rs.getString("POST_TYPE"));
                bfpbean.setSectorName(rs.getString("SECTOR_DESC"));
                bfpbean.setSubMajorHead(rs.getString("SUB_MAJOR_HEAD"));
                bfpbean.setSubMajorHeadDesc(rs.getString("SUB_MAJOR_HEAD_DESC"));
                bfpbean.setMinorHead(rs.getString("MINOR_HEAD"));
                bfpbean.setMinorHeadDesc(rs.getString("MINOR_HEAD_DESC"));
                bfpbean.setSubMinorHead1(rs.getString("SUB_MINOR_HEAD1"));
                bfpbean.setSubMinorHeadDesc1(rs.getString("SUB_MINOR_HEAD1_DESC"));
                bfpbean.setSubMinorHead2(rs.getString("SUB_MINOR_HEAD2"));
                bfpbean.setSubMinorHeadDesc2(rs.getString("SUB_MINOR_HEAD2_DESC"));
                if (rs.getString("SUB_MINOR_HEAD3") != null && !rs.getString("SUB_MINOR_HEAD3").equals("")) {
                    if (rs.getString("SUB_MINOR_HEAD3").equals("2")) {
                        bfpbean.setSubMinorHead3("Charge");
                    } else {
                        bfpbean.setSubMinorHead3("Voted");
                    }
                } else {
                    bfpbean.setSubMinorHead3("");
                }

                bfpbean.setBillType(rs.getString("BILL_TYPE"));
                bfpbean.setDemandNo(rs.getString("DEMAND_NO"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return bfpbean;
    }

    @Override
    public ArrayList getScheduleListWithADCodeTR(String billNo, int aqMonth, int aqYear) {

        Connection con = null;
        ResultSet rs = null;
        ArrayList al = new ArrayList();
        Schedule sc = null;
        Statement st = null;
        String aqDtlsTbl = "";
        try {
            aqDtlsTbl = getAqDtlsTableName(billNo + "");
            con = dataSource.getConnection();
            st = con.createStatement();
            String dedQry = " SELECT AQ_DTLS.SCHEDULE,NOW_DEDN,AD_AMT,BT_ID "
                    + "FROM (SELECT AD_CODE,SCHEDULE,DED_TYPE,NOW_DEDN,SUM(AD_AMT) AD_AMT,BT_ID FROM " + aqDtlsTbl + "  AQ_DTLS INNER JOIN  ("
                    + "SELECT AQ_MAST.AQSL_NO FROM AQ_MAST WHERE  AQ_MAST.BILL_NO=" + billNo + " AND AQ_MAST.aq_month=" + aqMonth + " AND aq_year=" + aqYear + ")AQ_MAST "
                    + " ON AQ_DTLS.AQSL_NO = AQ_MAST.AQSL_NO WHERE AD_TYPE='D' and SCHEDULE != 'PVTL' and schedule != 'PVTD'"
                    + " GROUP BY AQ_DTLS.SCHEDULE,DED_TYPE,NOW_DEDN,AD_CODE,BT_ID ORDER BY AQ_DTLS.SCHEDULE)AQ_DTLS LEFT OUTER JOIN G_SCHEDULE ON AQ_DTLS.SCHEDULE=G_SCHEDULE.SCHEDULE WHERE AD_AMT > 0 and (bt_from='TRBT' or AQ_DTLS.schedule='NPSL' or AQ_DTLS.schedule='TPFGA') ";
            rs = st.executeQuery(dedQry);
            while (rs.next()) {
                String schedule = rs.getString("SCHEDULE");

                if (schedule.equals("GPF")) {
                    sc = new Schedule();
                    sc.setScheduleName("GPF");
                    sc.setObjectHead(StringUtils.defaultString(rs.getString("BT_ID")));
                    sc.setSchAmount(rs.getString("AD_AMT"));
                } else if (schedule.equals("GA")) {
                    sc = new Schedule();
                    sc.setScheduleName("GA");
                    sc.setObjectHead(StringUtils.defaultString(rs.getString("BT_ID")));
                    sc.setSchAmount(rs.getString("AD_AMT"));
                } else if (schedule.equals("TPF")) {
                    sc = new Schedule();
                    sc.setScheduleName("TPF");
                    sc.setObjectHead(StringUtils.defaultString(rs.getString("BT_ID")));
                    sc.addSchAmount(rs.getInt("AD_AMT"));
                } else if (schedule.equals("TPFGA")) {
                    sc = new Schedule();
                    sc.setScheduleName("TPF (P)");
                    sc.setObjectHead(StringUtils.defaultString(rs.getString("BT_ID")));
                    sc.addSchAmount(rs.getInt("AD_AMT"));
                } else if (schedule.equals("CPF")) {
                    sc = new Schedule();
                    sc.setScheduleName("NPS");
                    sc.setObjectHead(StringUtils.defaultString(rs.getString("BT_ID")));
                    sc.addSchAmount(rs.getInt("AD_AMT"));
                } else if (schedule.equals("NPSL")) {
                    sc = new Schedule();
                    sc.setScheduleName("NPS (P)");
                    sc.setObjectHead(StringUtils.defaultString(rs.getString("BT_ID")));
                    sc.addSchAmount(rs.getInt("AD_AMT"));
                } else {
                    sc = new Schedule();
                    sc.setObjectHead(StringUtils.defaultString(rs.getString("BT_ID")));
                    if (rs.getString("NOW_DEDN") != null && !rs.getString("NOW_DEDN").equals("")) {
                        sc.setScheduleName(rs.getString("SCHEDULE") + " (" + rs.getString("NOW_DEDN") + ")");
                    } else {
                        sc.setScheduleName(rs.getString("SCHEDULE"));
                    }
                    sc.setSchAmount(rs.getString("AD_AMT"));
                }
                al.add(sc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;

    }

}
