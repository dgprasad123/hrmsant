/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.qtrallotment;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.AqFunctionalities;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.qtrallotment.QuarterAllotment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author lenovo pc
 */
public class QuarterAllotmentDAOImpl implements QuarterAllotmentDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List QuaterAllotSt(String empId) {
        List list = null;
        ResultSet rs = null;
        Statement st = null;
        Connection con = null;
        QuarterAllotment qas = new QuarterAllotment();
        list = new ArrayList();
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT QA_ID,NOT_ID,EMP_ID,QUARTER_NO,ALLOTMENT_DATE,POSSESSION_DATE,QUARTER_RENT,ADDRESS,IF_SURRENDERED,WATER_RENT,CONSUMER_NO,SEWERAGE_RENT,POOL_NAME,EMP_QTR_ALLOT.Q_ID,IS_GET_HRA FROM (SELECT QA_ID,NOT_ID,EMP_ID,QUARTER_NO,ALLOTMENT_DATE,POSSESSION_DATE,QUARTER_RENT,ADDRESS,IF_SURRENDERED,WATER_RENT,CONSUMER_NO,SEWERAGE_RENT,Q_ID,IS_GET_HRA "
                    + " FROM EMP_QTR_ALLOT where EMP_ID='" + empId + "') EMP_QTR_ALLOT "
                    + " LEFT OUTER JOIN G_QTR_POOL ON EMP_QTR_ALLOT.Q_ID=G_QTR_POOL.Q_ID");
            while (rs.next()) {
                qas.setQtrId(rs.getString("QA_ID"));
                qas.setNotId(rs.getString("NOT_ID"));
                qas.setQtrNo(rs.getString("QUARTER_NO"));
                if (rs.getDate("ALLOTMENT_DATE") != null) {
                    qas.setQtrAllotdate(CommonFunctions.getFormattedOutputDate3(rs.getDate("ALLOTMENT_DATE")));
                }
                if (rs.getDate("POSSESSION_DATE") != null) {
                    qas.setPossesdate(CommonFunctions.getFormattedOutputDate3(rs.getDate("POSSESSION_DATE")));
                }
                qas.setQtrRent(rs.getString("QUARTER_RENT"));
                qas.setWtrRent(rs.getString("WATER_RENT"));
                qas.setConsumerNo(rs.getString("CONSUMER_NO"));
                qas.setSewerageRent(rs.getString("SEWERAGE_RENT"));
                qas.setAddress(rs.getString("ADDRESS"));
                qas.setIfSurrendered(rs.getString("IF_SURRENDERED"));
                qas.setQtrIdforAllotFrom(rs.getString("Q_ID"));
                qas.setQtrForAllotName(rs.getString("POOL_NAME"));
                qas.setIsgetHra(rs.getString("IS_GET_HRA"));
                list.add(qas);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st, con);
        }
        return list;
    }

    @Override
    public QuarterAllotment editQuarterAllotment(String qtrAllotId) {
        ResultSet rs = null;
        Statement st = null;
        Connection con = null;
        QuarterAllotment qas = new QuarterAllotment();
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT QA_ID,NOT_ID,EMP_ID,QUARTER_NO,ALLOTMENT_DATE,POSSESSION_DATE,QUARTER_RENT,ADDRESS,IF_SURRENDERED,WATER_RENT,CONSUMER_NO,SEWERAGE_RENT,POOL_NAME,EMP_QTR_ALLOT.Q_ID,IS_GET_HRA FROM (SELECT QA_ID,NOT_ID,EMP_ID,QUARTER_NO,ALLOTMENT_DATE,POSSESSION_DATE,QUARTER_RENT,ADDRESS,IF_SURRENDERED,WATER_RENT,CONSUMER_NO,SEWERAGE_RENT,Q_ID,IS_GET_HRA "
                    + " FROM EMP_QTR_ALLOT where QA_ID=" + qtrAllotId + ") EMP_QTR_ALLOT "
                    + " LEFT OUTER JOIN G_QTR_POOL ON EMP_QTR_ALLOT.Q_ID=G_QTR_POOL.Q_ID");
            while (rs.next()) {
                qas.setQtrId(rs.getString("QA_ID"));
                qas.setNotId(rs.getString("NOT_ID"));
                qas.setQtrNo(rs.getString("QUARTER_NO"));
                if (rs.getDate("ALLOTMENT_DATE") != null) {
                    qas.setQtrAllotdate(CommonFunctions.getFormattedOutputDate3(rs.getDate("ALLOTMENT_DATE")));
                }
                if (rs.getDate("POSSESSION_DATE") != null) {
                    qas.setPossesdate(CommonFunctions.getFormattedOutputDate3(rs.getDate("POSSESSION_DATE")));
                }
                qas.setQtrRent(rs.getString("QUARTER_RENT"));
                qas.setWtrRent(rs.getString("WATER_RENT"));
                qas.setConsumerNo(rs.getString("CONSUMER_NO"));
                qas.setSewerageRent(rs.getString("SEWERAGE_RENT"));
                qas.setAddress(rs.getString("ADDRESS"));
                qas.setIfSurrendered(rs.getString("IF_SURRENDERED"));
                qas.setQtrIdforAllotFrom(rs.getString("Q_ID"));
                qas.setQtrForAllotName(rs.getString("POOL_NAME"));
                qas.setIsgetHra(rs.getString("IS_GET_HRA"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return qas;
    }

    @Override
    public int saveQuaterAllotmentRecord(QuarterAllotment quarterAllot) {
        PreparedStatement pst = null;
        Statement st = null;
        int mcode = 0;
        Connection con = null;
        int n = 0;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            if (quarterAllot.getNotId() != null && !quarterAllot.getNotId().equals("")) {
                st.executeUpdate("delete from EMP_QTR_ALLOT where not_id='" + quarterAllot.getNotId() + "'");
            }
            mcode = CommonFunctions.getMaxCodeInteger("EMP_QTR_ALLOT", "QA_ID", con);
            pst = con.prepareStatement("INSERT INTO EMP_QTR_ALLOT(QA_ID,NOT_ID,NOT_TYPE,EMP_ID,QUARTER_NO,ALLOTMENT_DATE,POSSESSION_DATE,QUARTER_RENT,SEWERAGE_RENT,ADDRESS,IF_SURRENDERED,WATER_RENT,Q_ID,CONSUMER_NO,IS_GET_HRA) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setInt(1, mcode);
            pst.setString(2, quarterAllot.getNotId());
            pst.setString(3, quarterAllot.getNotType());
            pst.setString(4, quarterAllot.getEmpId());
            pst.setString(5, quarterAllot.getQtrNo());
            pst.setString(6, quarterAllot.getQtrAllotdate());
            pst.setString(7, quarterAllot.getPossesdate());
            pst.setString(8, quarterAllot.getQtrRent());
            pst.setString(9, quarterAllot.getSewerageRent());
            pst.setString(10, quarterAllot.getAddress());
            pst.setString(11, null);
            pst.setString(12, quarterAllot.getWtrRent());
            pst.setString(13, quarterAllot.getQtrIdforAllotFrom());
            pst.setString(14, quarterAllot.getConsumerNo());
            pst.setString(15, quarterAllot.getIsgetHra());
            n = pst.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public int saveQuaterSurrenderRecord(QuarterAllotment quarterAllot) {
        PreparedStatement pst = null;
        Statement st = null;
        Statement st1 = null;
        int mcode = 0;
        Connection con = null;
        int n = 0;
        try {
            con = dataSource.getConnection();
            st1 = con.createStatement();
            if (quarterAllot.getNotId() != null && !quarterAllot.getNotId().equals("")) {
                st1.executeUpdate("delete from EMP_QTR_SURRENDER where not_id='" + quarterAllot.getNotId() + "'");
            }
            mcode = CommonFunctions.getMaxCodeInteger("EMP_QTR_SURRENDER", "QS_ID", con);
            pst = con.prepareStatement("INSERT INTO EMP_QTR_SURRENDER(QS_ID,QA_ID,NOT_ID,NOT_TYPE,EMP_ID,SURRENDER_DATE) VALUES(?,?,?,?,?,?)");
            pst.setInt(1, mcode);
            pst.setInt(2, Integer.parseInt(quarterAllot.getQtrId()));
            pst.setString(3, quarterAllot.getNotId());
            pst.setString(4, quarterAllot.getNotType());
            pst.setString(5, quarterAllot.getEmpId());
            pst.setString(6, quarterAllot.getSurrendate());
            n = pst.executeUpdate();
            st.executeUpdate("UPDATE EMP_QTR_ALLOT SET IF_SURRENDERED='Y' WHERE QA_ID=" + quarterAllot.getQtrId());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, st, st1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public QuarterAllotment getSurrenderEditRecords(String qtrSurId) {
        QuarterAllotment qas = new QuarterAllotment();
        String surdate = null;
        ResultSet rs = null;
        Statement st = null;
        Statement st1 = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            st1 = con.createStatement();
            rs = st1.executeQuery("SELECT * FROM (SELECT * FROM EMP_QTR_SURRENDER WHERE QS_ID=" + qtrSurId + ") EMP_QTR_SURRENDER INNER JOIN EMP_QTR_ALLOT ON EMP_QTR_SURRENDER.QA_ID = EMP_QTR_ALLOT.QA_ID");
            while (rs.next()) {
                qas.setQtrId(rs.getString("QA_ID"));
                qas.setQtrNo(rs.getString("QUARTER_NO"));
                if (rs.getDate("ALLOTMENT_DATE") != null) {
                    qas.setQtrAllotdate(CommonFunctions.getFormattedOutputDate3(rs.getDate("ALLOTMENT_DATE")));
                }
                if (rs.getDate("POSSESSION_DATE") != null) {
                    qas.setPossesdate(CommonFunctions.getFormattedOutputDate3(rs.getDate("POSSESSION_DATE")));
                }
                qas.setQtrRent(rs.getString("QUARTER_RENT"));
                qas.setAddress(rs.getString("ADDRESS"));
                if (rs.getDate("SURRENDER_DATE") != null) {
                    qas.setSurrendate(CommonFunctions.getFormattedOutputDate3(rs.getDate("SURRENDER_DATE")));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return qas;
    }

    @Override
    public void deleteQtrAllot(String qtrid, String empid) {
        Statement st = null;
        Statement st1 = null;
        ResultSet rs = null;
        Statement st2 = null;
        String qtrNo = null;
        String ifsuendered = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            st1 = con.createStatement();
            st2 = con.createStatement();
            st.executeUpdate("Delete EMP_QTR_ALLOT where QA_ID='" + qtrid + "'");
            rs = st1.executeQuery("select QUARTER_NO,IF_SURRENDERED from EMP_QTR_ALLOT where emp_id='" + empid + "' order by QA_ID DESC");
            if (rs.next()) {
                qtrNo = rs.getString("QUARTER_NO");
                ifsuendered = rs.getString("IF_SURRENDERED");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(st, st1, st2);
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteQtrSurrendRecords(String qtrid, String surid, String empid) {
        Statement st = null;
        Statement st1 = null;
        ResultSet rs = null;
        String qtrNo = null;
        String ifsuendered = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            st1 = con.createStatement();
            st1.executeUpdate("Delete from EMP_QTR_SURRENDER where qs_id='" + surid + "'");
            st.executeUpdate("UPDATE EMP_QTR_ALLOT SET IF_SURRENDERED=" + null + " where QA_ID='" + qtrid + "'");
            rs = st1.executeQuery("select QUARTER_NO,IF_SURRENDERED from EMP_QTR_ALLOT where emp_id='" + empid + "' order by QA_ID DESC");
            if (rs.next()) {
                qtrNo = rs.getString("QUARTER_NO");
                ifsuendered = rs.getString("IF_SURRENDERED");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(st, st1);
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List quarterempdetails(String empId, int year) {
        List list = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Connection con = null;
        QuarterAllotment qas = null;
        list = new ArrayList();
        try {
            String aqDTLS = "AQ_DTLS";

            con = this.dataSource.getConnection();

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(0, year);

            String sql = "select QUARTER_NO,QUARTER_RENT,ADDRESS,vch_no,vch_date,aqmast.bill_no,bill_mast.bill_date,aq_dtls.ad_amt HRR,aq_dtls2.AD_AMT SWR,aq_dtls1.AD_AMT WRR,aq_dtls3.AD_AMT MRR,aqmast.aq_year,aqmast.aq_month FROM "
                    + "(select aqsl_no,aq_year,aq_month,emp_code ,bill_no ,BILL_DATE from aq_mast where emp_code=? and aq_year=?) aqmast "
                    + "left outer join "
                    + "(select aqsl_no,aq_year,aq_month,ad_amt,ad_code,emp_code from " + aqDTLS + " where emp_code=? and aq_year=? and ad_code='HRR')aq_dtls "
                    + "on aq_dtls.aqsl_no=aqmast.aqsl_no "
                    + "left outer join "
                    + "(select aqsl_no,aq_year,aq_month,ad_amt,ad_code,emp_code from " + aqDTLS + " where emp_code=? and aq_year=? and ad_code='WRR' )aq_dtls1 "
                    + "on aq_dtls1.aqsl_no=aqmast.aqsl_no "
                    + "left outer join "
                    + "(select DISTINCT aqsl_no,aq_year,aq_month,ad_amt,ad_code,emp_code from " + aqDTLS + " where emp_code=? and aq_year=? and ad_code='SWR' and ad_amt>0 )aq_dtls2 "
                    + "on aq_dtls2.aqsl_no=aqmast.aqsl_no "
                    + "left outer join"
                    + "(select DISTINCT aqsl_no,aq_year,aq_month,ad_amt,ad_code,emp_code from " + aqDTLS + " where emp_code=? and aq_year=? and ad_code='MRR' and ad_amt>0)aq_dtls3 "
                    + "on aq_dtls3.aqsl_no=aqmast.aqsl_no "
                    + "left outer join "
                    + "bill_mast on aqmast.bill_no=bill_mast.bill_no "
                    + "inner join (SELECT DISTINCT QUARTER_NO,QUARTER_RENT,ADDRESS,emp_id,qa_id,possession_date,if_surrendered from emp_qtr_allot)EQ on EQ.emp_id=aqmast.emp_code \n"
                    + "LEFT OUTER JOIN emp_qtr_surrender ES ON EQ.qa_id = ES.qa_id  \n"
                    + "WHERE EQ.emp_id=? and aqmast.aq_year = ?\n"
                    + "and (TO_DATE(concat(aqmast.aq_year, lpad((aqmast.aq_month+1)::varchar, 2, '0'), '01'),'YYYYMMDD') between possession_date and surrender_date or\n"
                    + "	 (if_surrendered is NULL and TO_DATE(concat(aqmast.aq_year, lpad((aqmast.aq_month+1)::varchar, 2, '0'), '01'),'YYYYMMDD') >= possession_date)) order by aqmast.aq_month";

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, empId);
            pstmt.setInt(2, year);
            pstmt.setString(3, empId);
            pstmt.setInt(4, year);
            pstmt.setString(5, empId);
            pstmt.setInt(6, year);
            pstmt.setString(7, empId);
            pstmt.setInt(8, year);
            pstmt.setString(9, empId);
            pstmt.setInt(10, year);
            pstmt.setString(11, empId);
            pstmt.setInt(12, year);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                qas = new QuarterAllotment();
                qas.setMonth(rs.getString("aq_month"));
                qas.setYear(rs.getString("aq_year"));
                qas.setStringmonth(CommonFunctions.getMonthAsString(rs.getInt("aq_month")));
                qas.setQtrno(rs.getString("QUARTER_NO"));
                qas.setQtr_rent(rs.getString("QUARTER_RENT"));
                qas.setQtraddrs(rs.getString("ADDRESS"));
                qas.setBill_no(rs.getString("bill_no"));
                qas.setBill_date(CommonFunctions.getFormattedOutputDate1(rs.getDate("bill_date")));
                qas.setVch_no(rs.getString("vch_no"));
                qas.setVch_date(CommonFunctions.getFormattedOutputDate1(rs.getDate("vch_date")));
                qas.setHrr(rs.getInt("HRR"));
                qas.setWrr(rs.getInt("WRR"));
                if (rs.getString("SWR") != null && !rs.getString("SWR").equals("")) {
                    qas.setSwr(rs.getInt("SWR"));
                } else {
                    qas.setSwr(0);
                }
                qas.setMrr(rs.getInt("MRR"));
                list.add(qas);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public void downloadApplicationPdf(PdfWriter writer, Document document, String empId, int year, String empGpfNo, String empName) {
        List list = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Connection con = null;
        QuarterAllotment qas = null;
        list = new ArrayList();
        try {
            String aqDTLS = "AQ_DTLS";
            Font f1 = new Font();
            f1.setSize(6.9f);
            f1.setFamily("Times New Roman");
            
            PdfPTable dataTable = new PdfPTable(13);
            PdfPCell dataCell = null;
            dataTable.setWidths(new int[]{1, 2, 2, 2, 2, 1, 1, 1, 1, 2, 2, 2, 2});
            dataTable.setWidthPercentage(100);

            Font bold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            Font bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
            Font bold2 = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
            Paragraph p4 = new Paragraph("MY QUARTER DETAILS", bold);
            p4.setAlignment(Element.ALIGN_CENTER);
            p4.setSpacingBefore(15f);
            p4.setSpacingAfter(15f);
            document.add(p4);
            Paragraph p1 = new Paragraph("EMPLOYEE NAME: " + empName, bold1);
            document.add(p1);
            Paragraph p2 = new Paragraph("GPF NO: " + empGpfNo, bold1);
            document.add(p2);
            Paragraph p3 = new Paragraph("HRMS ID: " + empId, bold1);
            document.add(p3);
            PdfPCell blankRow = new PdfPCell(new Phrase(""));
            blankRow.setFixedHeight(15f);
            blankRow.setColspan(13);
            blankRow.setBorder(Rectangle.NO_BORDER);
            dataTable.addCell(blankRow);
            

            dataCell = new PdfPCell(new Phrase("S.N.", bold2));
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataCell.setBorderWidth(0.5f);
            dataCell.setFixedHeight(30f);
            dataCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase("Month", bold2));
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataCell.setBorderWidth(0.5f);
            dataCell.setFixedHeight(30f);
            dataCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase("Quarter No", bold2));
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataCell.setBorderWidth(0.5f);
            dataCell.setFixedHeight(30f);
            dataCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase("Quarter Address", bold2));
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataCell.setBorderWidth(0.5f);
            dataCell.setFixedHeight(30f);
            dataCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase("Quarter Rent", bold2));
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataCell.setBorderWidth(0.5f);
            dataCell.setFixedHeight(30f);
            dataCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase("HRR", bold2));
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataCell.setBorderWidth(0.5f);
            dataCell.setFixedHeight(30f);
            dataCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase("WRR", bold2));
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataCell.setBorderWidth(0.5f);
            dataCell.setFixedHeight(30f);
            dataCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase("SWR", bold2));
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataCell.setBorderWidth(0.5f);
            dataCell.setFixedHeight(30f);
            dataCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase("MRR", bold2));
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataCell.setBorderWidth(0.5f);
            dataCell.setFixedHeight(30f);
            dataCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase("Bill No", bold2));
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataCell.setBorderWidth(0.5f);
            dataCell.setFixedHeight(30f);
            dataCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase("Bill Date", bold2));
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataCell.setBorderWidth(0.5f);
            dataCell.setFixedHeight(30f);
            dataCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase("Voucher No", bold2));
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataCell.setBorderWidth(0.5f);
            dataCell.setFixedHeight(30f);
            dataCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase("Voucher Date", bold2));
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataCell.setBorderWidth(0.5f);
            dataCell.setFixedHeight(30f);
            dataCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            dataTable.addCell(dataCell);
            //document.add(dataTable);

            con = this.dataSource.getConnection();

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(0, year);

            String sql = "select QUARTER_NO,QUARTER_RENT,ADDRESS,vch_no,vch_date,aqmast.bill_no,bill_mast.bill_date,aq_dtls.ad_amt HRR,aq_dtls2.AD_AMT SWR,aq_dtls1.AD_AMT WRR,aq_dtls3.AD_AMT MRR,aqmast.aq_year,aqmast.aq_month FROM "
                    + "(select aqsl_no,aq_year,aq_month,emp_code ,bill_no ,BILL_DATE from aq_mast where emp_code=? and aq_year=?) aqmast "
                    + "left outer join "
                    + "(select aqsl_no,aq_year,aq_month,ad_amt,ad_code,emp_code from " + aqDTLS + " where emp_code=? and aq_year=? and ad_code='HRR')aq_dtls "
                    + "on aq_dtls.aqsl_no=aqmast.aqsl_no "
                    + "left outer join "
                    + "(select aqsl_no,aq_year,aq_month,ad_amt,ad_code,emp_code from " + aqDTLS + " where emp_code=? and aq_year=? and ad_code='WRR' )aq_dtls1 "
                    + "on aq_dtls1.aqsl_no=aqmast.aqsl_no "
                    + "left outer join "
                    + "(select DISTINCT aqsl_no,aq_year,aq_month,ad_amt,ad_code,emp_code from " + aqDTLS + " where emp_code=? and aq_year=? and ad_code='SWR' and ad_amt>0 )aq_dtls2 "
                    + "on aq_dtls2.aqsl_no=aqmast.aqsl_no "
                    + "left outer join"
                    + "(select DISTINCT aqsl_no,aq_year,aq_month,ad_amt,ad_code,emp_code from " + aqDTLS + " where emp_code=? and aq_year=? and ad_code='MRR' and ad_amt>0)aq_dtls3 "
                    + "on aq_dtls3.aqsl_no=aqmast.aqsl_no "
                    + "left outer join "
                    + "bill_mast on aqmast.bill_no=bill_mast.bill_no "
                    + "inner join (SELECT DISTINCT QUARTER_NO,QUARTER_RENT,ADDRESS,emp_id,qa_id,possession_date,if_surrendered from emp_qtr_allot)EQ on EQ.emp_id=aqmast.emp_code \n"
                    + "LEFT OUTER JOIN emp_qtr_surrender ES ON EQ.qa_id = ES.qa_id  \n"
                    + "WHERE EQ.emp_id=? and aqmast.aq_year = ?\n"
                    + "and (TO_DATE(concat(aqmast.aq_year, lpad((aqmast.aq_month+1)::varchar, 2, '0'), '01'),'YYYYMMDD') between possession_date and surrender_date or\n"
                    + "	 (if_surrendered is NULL and TO_DATE(concat(aqmast.aq_year, lpad((aqmast.aq_month+1)::varchar, 2, '0'), '01'),'YYYYMMDD') >= possession_date)) order by aqmast.aq_month";

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, empId);
            pstmt.setInt(2, year);
            pstmt.setString(3, empId);
            pstmt.setInt(4, year);
            pstmt.setString(5, empId);
            pstmt.setInt(6, year);
            pstmt.setString(7, empId);
            pstmt.setInt(8, year);
            pstmt.setString(9, empId);
            pstmt.setInt(10, year);
            pstmt.setString(11, empId);
            pstmt.setInt(12, year);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                qas = new QuarterAllotment();
                qas.setMonth(rs.getString("aq_month"));
                qas.setYear(rs.getString("aq_year"));
                qas.setStringmonth(CommonFunctions.getMonthAsString(rs.getInt("aq_month")));
                qas.setQtrno(rs.getString("QUARTER_NO"));
                qas.setQtr_rent(rs.getString("QUARTER_RENT"));
                qas.setQtraddrs(rs.getString("ADDRESS"));
                qas.setBill_no(rs.getString("bill_no"));
                qas.setBill_date(CommonFunctions.getFormattedOutputDate1(rs.getDate("bill_date")));
                qas.setVch_no(rs.getString("vch_no"));
                qas.setVch_date(CommonFunctions.getFormattedOutputDate1(rs.getDate("vch_date")));
                qas.setHrr(rs.getInt("HRR"));
                qas.setWrr(rs.getInt("WRR"));
                if (rs.getString("SWR") != null && !rs.getString("SWR").equals("")) {
                    qas.setSwr(rs.getInt("SWR"));
                } else {
                    qas.setSwr(0);
                }
                qas.setMrr(rs.getInt("MRR"));
                list.add(qas);
            }
            if (list != null && list.size() > 0) {
                QuarterAllotment qarBean = null;

                for (int j = 0; j < list.size(); j++) {
                    qarBean = (QuarterAllotment) list.get(j);

                    dataCell = new PdfPCell(new Phrase(Integer.toString(j + 1), new Font(Font.FontFamily.HELVETICA, 6)));
                    //dataCell.setBorder(Rectangle.NO_BORDER);
                    //dataCell.setBorder(Rectangle.BOTTOM | Rectangle.TOP | Rectangle.LEFT | Rectangle.RIGHT);
                    dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    dataCell.setBorderWidth(0.5f);
                    dataCell.setFixedHeight(25f);
                    dataTable.addCell(dataCell);                         
                    dataCell = new PdfPCell(new Phrase(qarBean.getStringmonth(), new Font(Font.FontFamily.HELVETICA, 6.9f)));
                    //dataCell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                    dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    dataCell.setBorderWidth(0.5f);
                    dataCell.setFixedHeight(25f);
                    dataTable.addCell(dataCell);
                    dataCell = new PdfPCell(new Phrase(qarBean.getQtrno(), new Font(Font.FontFamily.HELVETICA, 6.9f)));
                    //dataCell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                    dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    dataCell.setBorderWidth(0.5f);
                    dataCell.setFixedHeight(25f);
                    dataTable.addCell(dataCell);
                    dataCell = new PdfPCell(new Phrase(qarBean.getQtraddrs(), new Font(Font.FontFamily.HELVETICA, 6.9f)));
                    //dataCell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                    dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    dataCell.setBorderWidth(0.5f);
                    dataCell.setFixedHeight(25f);
                    dataTable.addCell(dataCell);
                    dataCell = new PdfPCell(new Phrase(qarBean.getQtr_rent(), new Font(Font.FontFamily.HELVETICA, 6.9f)));
                    //dataCell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                    dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    dataCell.setBorderWidth(0.5f);
                    dataCell.setFixedHeight(25f);
                    dataTable.addCell(dataCell);
                    dataCell = new PdfPCell(new Phrase(Integer.toString(qarBean.getHrr()), new Font(Font.FontFamily.HELVETICA, 6.9f)));
                    //dataCell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                    dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    dataCell.setBorderWidth(0.5f);
                    dataCell.setFixedHeight(25f);
                    dataTable.addCell(dataCell);
                    dataCell = new PdfPCell(new Phrase(Integer.toString(qarBean.getWrr()), new Font(Font.FontFamily.HELVETICA, 6.9f)));
                    //dataCell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                    dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    dataCell.setBorderWidth(0.5f);
                    dataCell.setFixedHeight(25f);
                    dataTable.addCell(dataCell);
                    dataCell = new PdfPCell(new Phrase(Integer.toString(qarBean.getSwr()), new Font(Font.FontFamily.HELVETICA, 6.9f)));
                    //dataCell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                    dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    dataCell.setBorderWidth(0.5f);
                    dataCell.setFixedHeight(25f);
                    dataTable.addCell(dataCell);
                    dataCell = new PdfPCell(new Phrase(Integer.toString(qarBean.getMrr()), new Font(Font.FontFamily.HELVETICA, 6.9f)));
                    //dataCell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                    dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    dataCell.setBorderWidth(0.5f);
                    dataCell.setFixedHeight(25f);
                    dataTable.addCell(dataCell);
                    dataCell = new PdfPCell(new Phrase(qarBean.getBill_no(), new Font(Font.FontFamily.HELVETICA, 6.9f)));
                    //dataCell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                    dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    dataCell.setBorderWidth(0.5f);
                    dataCell.setFixedHeight(25f);
                    dataTable.addCell(dataCell);
                    dataCell = new PdfPCell(new Phrase(qarBean.getBill_date(), new Font(Font.FontFamily.HELVETICA, 6.9f)));
                    //dataCell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                    dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    dataCell.setBorderWidth(0.5f);
                    dataCell.setFixedHeight(25f);
                    dataTable.addCell(dataCell);
                    dataCell = new PdfPCell(new Phrase(qarBean.getVch_no(), new Font(Font.FontFamily.HELVETICA, 6.9f)));
                    //dataCell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                    dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    dataCell.setBorderWidth(0.5f);
                    dataCell.setFixedHeight(25f);
                    dataTable.addCell(dataCell);
                    dataCell = new PdfPCell(new Phrase(qarBean.getVch_date(), new Font(Font.FontFamily.HELVETICA, 6.9f)));
                    //dataCell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                    dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    dataCell.setBorderWidth(0.5f);
                    dataCell.setFixedHeight(25f);
                    dataTable.addCell(dataCell);

                }

            }

            document.add(dataTable);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);

        }

    }
}
