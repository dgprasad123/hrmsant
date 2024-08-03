package hrms.dao.payroll.payslip;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import hrms.common.AqFunctionalities;
import hrms.common.CalendarCommonMethods;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.Numtowordconvertion;
import hrms.common.PayrollCommonFunction;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.payslip.ADDetails;
import hrms.model.payroll.payslip.PaySlipDetailBean;
import hrms.model.payroll.payslip.PaySlipListBean;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

public class PaySlipDAOImpl implements PaySlipDAO {

    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    @Override
    public List getPaySlip(String empid, int year, int month) {

        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;
        PaySlipListBean pbean = null;

        double basic = 0.0;
        double totallow = 0.0;
        double grosspay = 0.0;
        double totdeduct = 0.0;
        double netpay = 0.0;

        ArrayList list = new ArrayList();
        try {
            con = dataSource.getConnection();

            String aqdtlsTbl = AqFunctionalities.getAQBillDtlsTable(month, year);

            stmt = con.createStatement();
            String sql = "select * from ("
                    + " SELECT AQSL_NO,AQ_MONTH,AQ_YEAR,CUR_BASIC,TOTALALLOWANCE,TOTALDEDUCTION FROM ("
                    + " select AQSL_NO,aq_mast.AQ_MONTH,aq_mast.AQ_YEAR,CUR_BASIC,TOTALALLOWANCE,TOTALDEDUCTION FROM ("
                    + " select BILL_NO,AQSL_NO,AQ_MONTH,AQ_YEAR,CUR_BASIC,"
                    + " GETADTOTAL_NEW(AQSL_NO,'A','" + aqdtlsTbl + "','" + year + "','" + month + "')TOTALALLOWANCE,GETADTOTAL_NEW(AQSL_NO,'D','" + aqdtlsTbl + "','" + year + "','" + month + "')TOTALDEDUCTION from AQ_MAST where"
                    + " AQ_MAST.EMP_CODE='" + empid + "' AND AQ_YEAR=" + year + " AND AQ_MONTH=" + month + ")AQ_MAST"
                    + " inner join"
                    + " bill_mast on aq_mast.bill_no=bill_mast.bill_no where bill_status_id >= 5 ORDER BY AQ_YEAR DESC, AQ_MONTH DESC)tab)tab2";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                pbean = new PaySlipListBean();
                pbean.setEmpId(empid);
                pbean.setAqslno(rs.getString("AQSL_NO"));
                pbean.setMonth(CalendarCommonMethods.getFullNameMonthAsString(rs.getInt("AQ_MONTH")));
                pbean.setYear(rs.getString("AQ_YEAR"));
                pbean.setMonth_year(pbean.getMonth() + "-" + pbean.getYear());
                pbean.setBasic(rs.getString("CUR_BASIC"));
                pbean.setTotallowance(rs.getString("TOTALALLOWANCE"));
                pbean.setTotdeduction(rs.getString("TOTALDEDUCTION"));

                basic = rs.getDouble("CUR_BASIC");
                totallow = rs.getDouble("TOTALALLOWANCE");
                grosspay = basic + totallow;
                pbean.setGross(grosspay + "");

                totdeduct = rs.getDouble("TOTALDEDUCTION");
                netpay = grosspay - totdeduct;
                pbean.setNetpay(netpay + "");

                list.add(pbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public String getAQSLNo(String empid, int year, int month) {
        Connection con = null;
        String aqslno = "";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT AQSL_NO FROM AQ_MAST WHERE EMP_CODE=? AND AQ_MONTH=? AND AQ_YEAR=?");
            pst.setString(1, empid);
            pst.setInt(2, month);
            pst.setInt(3, year);
            rs = pst.executeQuery();

            if (rs.next()) {
                aqslno = rs.getString("aqsl_no");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aqslno;
    }

    @Override
    public PaySlipDetailBean getEmployeeData(String aqslno, int year, int month) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PaySlipDetailBean pbeandetail = new PaySlipDetailBean();

        PayrollCommonFunction prcf = new PayrollCommonFunction();
        CommonReportParamBean bean = null;

        try {
            con = repodataSource.getConnection();

            String sql = "SELECT ARRAY_TO_STRING(ARRAY[E1.INITIALS, E1.F_NAME, E1.M_NAME, E1.L_NAME], ' ') DDO_NAME,g_office.off_en,emp_type,DA_RATE,EMP_MAST.doe_gov,EMP_MAST.USERTYPE,BILL_DESC,G_BANK.BANK_NAME,EMP_CODE,EMP_NAME,CUR_DESG,GPF_ACC_NO,PAY_SCALE,\n"
                    + "  CUR_BASIC,MON_BASIC,AQ_MAST.AQ_MONTH,AQ_MAST.AQ_YEAR,PAY_DAY,AQ_MAST.BILL_NO,AQ_MAST.BANK_ACC_NO,EMP_MAST.BRASS_NO,VCH_NO,VCH_DATE,\n"
                    + "  BILL_MAST.BILL_DATE,\n"
                    + "  (SELECT ID_NO FROM EMP_ID_DOC WHERE EMP_ID=AQ_MAST.EMP_CODE AND ID_DESCRIPTION='PAN')PAN FROM\n"
                    + "  (select DA_RATE,EMP_CODE,EMP_NAME,CUR_DESG,GPF_ACC_NO,PAY_SCALE,\n"
                    + "  CUR_BASIC,MON_BASIC,AQ_MONTH,AQ_YEAR,PAY_DAY,BILL_NO,BANK_ACC_NO,BANK_NAME,off_code,emp_type from aq_mast where\n"
                    + "  aqsl_no=?)AQ_MAST\n"
                    + "  LEFT OUTER JOIN G_BANK ON AQ_MAST.BANK_NAME=G_BANK.BANK_CODE\n"
                    + "  LEFT OUTER JOIN EMP_MAST ON AQ_MAST.EMP_CODE=EMP_MAST.EMP_ID\n"
                    + "  LEFT OUTER JOIN BILL_MAST ON AQ_MAST.BILL_NO=BILL_MAST.BILL_NO\n"
                    + "  left outer join g_office on AQ_MAST.off_code=g_office.off_code"
                    + " LEFT OUTER JOIN EMP_MAST E1 ON G_OFFICE.DDO_HRMSID=E1.EMP_ID";
            pst = con.prepareStatement(sql);
            pst.setString(1, aqslno);
            rs = pst.executeQuery();
            if (rs.next()) {

                if (rs.getString("EMP_NAME") != null && !rs.getString("EMP_NAME").equals("")) {
                    if (rs.getString("BRASS_NO") != null && !rs.getString("BRASS_NO").equals("")) {
                        pbeandetail.setEmpName(rs.getString("BRASS_NO") + " " + rs.getString("EMP_NAME"));
                    } else {
                        pbeandetail.setEmpName(rs.getString("EMP_NAME"));
                    }
                }
                pbeandetail.setEmpCode(rs.getString("emp_code"));
                if (rs.getString("CUR_DESG") != null && !rs.getString("CUR_DESG").equals("")) {
                    pbeandetail.setCurDesig(rs.getString("CUR_DESG"));
                }
                if (rs.getString("GPF_ACC_NO") != null && !rs.getString("GPF_ACC_NO").equals("")) {
                    pbeandetail.setGpfno(rs.getString("GPF_ACC_NO"));
                }
                pbeandetail.setDoj(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe_gov")));
                if (rs.getString("PAY_SCALE") != null && !rs.getString("PAY_SCALE").equals("")) {
                    pbeandetail.setScalePay(rs.getString("PAY_SCALE"));
                }
                if (rs.getString("CUR_BASIC") != null && !rs.getString("CUR_BASIC").equals("")) {
                    pbeandetail.setCurBasic(rs.getString("CUR_BASIC"));
                }
                if (rs.getString("MON_BASIC") != null && !rs.getString("MON_BASIC").equals("")) {
                    pbeandetail.setEmpBasic(rs.getString("MON_BASIC"));
                }
                if (rs.getString("AQ_MONTH") != null && !rs.getString("AQ_MONTH").equals("")) {
                    pbeandetail.setForMonth(CalendarCommonMethods.getFullNameMonthAsString((rs.getInt("AQ_MONTH"))));
                }
                if (rs.getString("AQ_YEAR") != null && !rs.getString("AQ_YEAR").equals("")) {
                    pbeandetail.setForYear(rs.getString("AQ_YEAR"));
                }
                if (rs.getString("PAY_DAY") != null && !rs.getString("PAY_DAY").equals("")) {
                    pbeandetail.setDaysWork(rs.getString("PAY_DAY"));
                }
                if (rs.getString("BILL_DESC") != null && !rs.getString("BILL_DESC").equals("")) {
                    pbeandetail.setBillNo(rs.getString("BILL_DESC"));
                }
                if (rs.getString("BANK_ACC_NO") != null && !rs.getString("BANK_ACC_NO").equals("")) {
                    pbeandetail.setBankAcno(rs.getString("BANK_ACC_NO"));
                }
                if (rs.getString("BANK_NAME") != null && !rs.getString("BANK_NAME").equals("")) {
                    pbeandetail.setBank(rs.getString("BANK_NAME"));
                }
                if (rs.getString("VCH_NO") != null && !rs.getString("VCH_NO").equals("")) {
                    pbeandetail.setVchno(rs.getString("VCH_NO"));
                }
                if (rs.getString("VCH_DATE") != null && !rs.getString("VCH_DATE").equals("")) {
                    pbeandetail.setVchdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("VCH_DATE")));
                }
                if (rs.getString("BILL_DATE") != null && !rs.getString("BILL_DATE").equals("")) {
                    pbeandetail.setBilldate(CommonFunctions.getFormattedOutputDate1(rs.getDate("BILL_DATE")));
                }
                if (rs.getString("DA_RATE") != null && !rs.getString("DA_RATE").equals("")) {
                    pbeandetail.setDarate(rs.getString("DA_RATE"));
                }
                pbeandetail.setPanno(rs.getString("PAN"));
                pbeandetail.setOffName(rs.getString("OFF_EN"));
                if (rs.getString("emp_type") != null && !rs.getString("emp_type").equals("")) {
                    if (rs.getString("emp_type").equals("D")) {
                        pbeandetail.setTypeOfEmp("Non Government Aided Employee");
                    } else if (rs.getString("emp_type").equals("B")) {
                        pbeandetail.setTypeOfEmp("Level-V(Ex-cadre) Employee");
                    } else if (rs.getString("emp_type").equals("A")) {
                        if (rs.getString("USERTYPE") != null && !rs.getString("USERTYPE").equals("")) {
                            pbeandetail.setTypeOfEmp(rs.getString("USERTYPE").concat(" Employee(Special Category)"));
                        } else {
                            pbeandetail.setTypeOfEmp("Non Govt.Special Category Employee");
                        }
                    } else {
                        pbeandetail.setTypeOfEmp("");
                    }
                }
                pbeandetail.setDdoName(rs.getString("DDO_NAME"));
            }
            //bean = prcf.getCommonReportParameter(con, getBillNo(aqslno, year, month));
            //pbeandetail.setOffName(bean.getOfficename());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pbeandetail;
    }

    @Override
    public ADDetails[] getPrivateDeductionList(String aqslno, int year, int month) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<ADDetails> list = new ArrayList();
        ADDetails ad = null;
        int sum = 0;
        try {
            con = dataSource.getConnection();
            String aqdtlsTbl = AqFunctionalities.getAQBillDtlsTable(month, year);

            String sql = "SELECT AQ_DTLS.AD_DESC,AD_AMT,REF_DESC,AQ_DTLS.AD_CODE,NOW_DEDN,AD_ABBR,ACC_NO FROM (SELECT ACC_NO,AD_DESC,AD_AMT,REF_DESC,AD_CODE,NOW_DEDN"
                    + " FROM " + aqdtlsTbl + " WHERE AD_TYPE='D' AND DED_TYPE = 'L' AND AD_AMT > 0 AND AQSL_NO=? AND SCHEDULE ='PVTD' AND AQ_YEAR=? AND AQ_MONTH=?)AQ_DTLS"
                    + " LEFT OUTER JOIN G_AD_LIST ON AQ_DTLS.AD_CODE=G_AD_LIST.AD_CODE_NAME";
            pst = con.prepareStatement(sql);
            pst.setString(1, aqslno);
            pst.setInt(2, year);
            pst.setInt(3, month);
            rs = pst.executeQuery();
            while (rs.next()) {
                ad = new ADDetails();
                if ((rs.getString("AD_CODE").equals("LIC") || rs.getString("AD_CODE").equals("TLIC")) && (rs.getString("ACC_NO") != null && !rs.getString("ACC_NO").equals(""))) {
                    ad.setAdCode(rs.getString("AD_ABBR") + " (" + rs.getString("ACC_NO") + ")");
                    ad.setAdCodeDesc(rs.getString("AD_ABBR"));
                } else {
                    ad.setAdCode(rs.getString("AD_ABBR"));
                    ad.setAdCodeDesc(rs.getString("AD_ABBR"));
                }
                if (rs.getString("REF_DESC") != null) {
                    ad.setAdDesc(rs.getString("REF_DESC"));
                }
                ad.setAdAmt(rs.getInt("AD_AMT"));
                ad.setAccNumber(rs.getString("ACC_NO"));

                Double amt = Double.parseDouble(rs.getString("AD_AMT"));
                sum += amt.intValue();
                ad.setAdAmount(sum);
                list.add(ad);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        ADDetails idarray[] = list.toArray(new ADDetails[list.size()]);
        return idarray;
    }

    @Override
    public ADDetails[] getAllowanceDeductionList(String aqslno, String adType, int year, int month) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String sql = "";
        ADDetails ad = null;

        int sum = 0;
        int sum1 = 0;

        ArrayList<ADDetails> list = new ArrayList();

        try {
            con = dataSource.getConnection();

            String aqdtlsTbl = AqFunctionalities.getAQBillDtlsTable(month, year);

            if (adType.equals("A")) {
                sql = "SELECT G_AD_LIST.AD_DESC,AD_AMT,REF_DESC,G_AD_LIST.AD_CODE,AD_ABBR,ACC_NO FROM (SELECT AD_DESC,AD_AMT,REF_DESC,AD_CODE,ACC_NO FROM " + aqdtlsTbl + " WHERE AD_TYPE='A' AND AD_AMT > 0 AND AQSL_NO= ? AND AQ_YEAR=? AND AQ_MONTH=? ORDER BY REP_COL,SL_NO )AQ_DTLS "
                        + " LEFT OUTER JOIN G_AD_LIST ON AQ_DTLS.AD_CODE=G_AD_LIST.AD_CODE_NAME";
                pst = con.prepareStatement(sql);
                pst.setString(1, aqslno);
                pst.setInt(2, year);
                pst.setInt(3, month);
            } else {
                /*sql = "SELECT AQ_DTLS.AD_DESC,AD_AMT,REF_DESC,AQ_DTLS.AD_CODE,NOW_DEDN,AD_ABBR,ACC_NO FROM (SELECT ACC_NO,AD_DESC,AD_AMT,REF_DESC,AD_CODE,NOW_DEDN"
                 + " FROM " + aqdtlsTbl + " WHERE AD_TYPE='D' AND DED_TYPE != 'L' AND AD_AMT > 0 AND AQSL_NO=? AND SCHEDULE !='PVTL' AND SCHEDULE !='PVTD' AND AQ_YEAR=? AND AQ_MONTH=?)AQ_DTLS"
                 + " LEFT OUTER JOIN G_AD_LIST ON AQ_DTLS.AD_CODE=G_AD_LIST.AD_CODE_NAME"
                 + " UNION"
                 + " SELECT ACC_NO,AD_DESC,AD_AMT,REF_DESC,AD_CODE,NOW_DEDN"
                 + " FROM " + aqdtlsTbl + " WHERE AQSL_NO=? AND AD_AMT > 0"
                 + " AND AQ_YEAR=? AND AQ_MONTH=? AND AD_TYPE='D' AND ad_code='EPF'";*/
                sql = "SELECT aqdtlsTbl.AD_DESC,AD_AMT,REF_DESC,aqdtlsTbl.AD_CODE,NOW_DEDN,AD_ABBR,ACC_NO FROM  \n"
                        + " (SELECT ACC_NO,AD_DESC,AD_AMT,REF_DESC,AD_CODE,NOW_DEDN  \n"
                        + " FROM " + aqdtlsTbl + " WHERE AD_TYPE='D' AND DED_TYPE != 'L' AND AD_AMT > 0 AND AQSL_NO=? AND SCHEDULE !='PVTL' AND SCHEDULE !='PVTD' \n"
                        + "  AND AQ_YEAR=? AND AQ_MONTH=?)as  aqdtlsTbl  \n"
                        + " LEFT OUTER JOIN G_AD_LIST ON aqdtlsTbl.AD_CODE=G_AD_LIST.AD_CODE_NAME  \n"
                        + " UNION  \n"
                        + " SELECT aqdtlsTbl1.AD_DESC,AD_AMT,REF_DESC,aqdtlsTbl1.AD_CODE,NOW_DEDN,AD_ABBR,ACC_NO FROM " + aqdtlsTbl + " as aqdtlsTbl1\n"
                        + " LEFT OUTER JOIN G_AD_LIST ON aqdtlsTbl1.AD_CODE=G_AD_LIST.AD_CODE_NAME  \n"
                        + " WHERE AQSL_NO=? AND AD_AMT > 0  \n"
                        + " AND AQ_YEAR=? AND AQ_MONTH=? AND aqdtlsTbl1.AD_TYPE='D'  AND aqdtlsTbl1.SCHEDULE IS NULL";
                pst = con.prepareStatement(sql);
                pst.setString(1, aqslno);
                pst.setInt(2, year);
                pst.setInt(3, month);
                pst.setString(4, aqslno);
                pst.setInt(5, year);
                pst.setInt(6, month);
            }

            rs = pst.executeQuery();
            while (rs.next()) {
                ad = new ADDetails();
                if ((rs.getString("AD_CODE").equals("LIC") || rs.getString("AD_CODE").equals("TLIC")) && (rs.getString("ACC_NO") != null && !rs.getString("ACC_NO").equals(""))) {
                    ad.setAdCode(rs.getString("AD_ABBR") + " (" + rs.getString("ACC_NO") + ")");
                    ad.setAdCodeDesc(rs.getString("AD_ABBR"));
                } else {
                    ad.setAdCode(rs.getString("AD_ABBR"));
                    ad.setAdCodeDesc(rs.getString("AD_ABBR"));
                }
                if (rs.getString("REF_DESC") != null) {
                    ad.setAdDesc(rs.getString("REF_DESC"));
                }
                ad.setAdAmt(rs.getInt("AD_AMT"));
                ad.setAccNumber(rs.getString("ACC_NO"));
                if (adType.equalsIgnoreCase("A") && rs.getString("AD_AMT") != null) {
                    Double amt = Double.parseDouble(rs.getString("AD_AMT"));
                    sum += amt.intValue();
                    ad.setAdAmount(sum);
                }
                if (adType.equalsIgnoreCase("D") && rs.getString("AD_AMT") != null) {
                    Double amt = Double.parseDouble(rs.getString("AD_AMT"));
                    sum1 += amt.intValue();
                    ad.setAdAmount(sum1);
                }
                list.add(ad);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        ADDetails idarray[] = list.toArray(new ADDetails[list.size()]);
        return idarray;
    }

    @Override
    public List getPrivateDedeuctionList(String aqslno, int year, int month) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet pvtdedRs = null;

        ADDetails ad = null;

        int sum = 0;
        int sum1 = 0;

        ArrayList list = new ArrayList();
        try {
            con = dataSource.getConnection();

            String aqdtlsTbl = AqFunctionalities.getAQBillDtlsTable(month, year);

            String sql = "SELECT PVT_DESC,AD_DESC,AD_CODE,AD_AMT,REF_DESC,AQ_DTLS.NOW_DEDN,AD_REF_ID,TOT_REC_AMT,P_ORG_AMT,I_ORG_AMT,LOAN_ABBR FROM"
                    + " (SELECT PVT_DESC,AD_DESC,AD_CODE,AD_AMT,REF_DESC,AQ_DTLS.NOW_DEDN,AD_REF_ID,TOT_REC_AMT,P_ORG_AMT,I_ORG_AMT FROM(SELECT AD_DESC,AD_CODE,AD_AMT,"
                    + " REF_DESC,NOW_DEDN,AD_REF_ID,TOT_REC_AMT FROM " + aqdtlsTbl + " WHERE AD_TYPE='D' AND AD_AMT > 0 AND (SCHEDULE ='PVTL' OR SCHEDULE"
                    + " ='PVTD') AND AQSL_NO='" + aqslno + "' AND AQ_YEAR='" + year + "' AND AQ_MONTH='" + month + "')AQ_DTLS"
                    + " LEFT OUTER JOIN (SELECT LOANID,NOW_DEDN,P_ORG_AMT,I_ORG_AMT,PVT_DESC FROM EMP_LOAN_SANC)EMP_LOAN_SANC ON AQ_DTLS.AD_REF_ID ="
                    + " EMP_LOAN_SANC.LOANID )AQ_DTLS"
                    + " LEFT OUTER JOIN G_LOAN ON AQ_DTLS.AD_CODE=G_LOAN.LOAN_TP";
            pst = con.prepareStatement(sql);
            pvtdedRs = pst.executeQuery();
            while (pvtdedRs.next()) {
                ad = new ADDetails();
                if (pvtdedRs.getString("LOAN_ABBR") != null && !pvtdedRs.getString("LOAN_ABBR").equals("")) {
                    ad.setAdCode(pvtdedRs.getString("LOAN_ABBR"));
                } else if (pvtdedRs.getString("PVT_DESC") != null && !pvtdedRs.getString("PVT_DESC").equals("")) {
                    ad.setAdCode(pvtdedRs.getString("PVT_DESC").toUpperCase());
                } else if (pvtdedRs.getString("AD_DESC") != null && !pvtdedRs.getString("AD_DESC").equals("")) {
                    ad.setAdCode(pvtdedRs.getString("AD_DESC").toUpperCase());
                }
                if (pvtdedRs.getString("REF_DESC") != null) {
                    ad.setAdDesc(pvtdedRs.getString("AD_AMT") + " (" + pvtdedRs.getString("REF_DESC") + ")");
                } else {
                    ad.setAdDesc(pvtdedRs.getString("AD_AMT"));
                }
                sum1 += Integer.parseInt(pvtdedRs.getString("AD_AMT"));
                ad.setAdAmount(sum1);
                list.add(ad);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pvtdedRs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public List getPrivateDedeuctionListForPaySlip(String aqslno, int year, int month) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet pvtdedRs = null;

        ADDetails ad = null;

        int sum = 0;
        int sum1 = 0;

        ArrayList list = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String aqdtlsTbl = AqFunctionalities.getAQBillDtlsTable(month, year);

            String sql = "SELECT AQ_DTLS.AD_DESC,AD_AMT,REF_DESC,AQ_DTLS.AD_CODE,NOW_DEDN,AD_ABBR FROM (SELECT AD_DESC,AD_AMT,REF_DESC,AD_CODE,NOW_DEDN"
                    + " FROM " + aqdtlsTbl + " WHERE AD_TYPE='D' AND AD_AMT > 0 AND AQSL_NO=? AND (SCHEDULE ='PVTL' OR SCHEDULE ='PVTD')"
                    + " AND AQ_YEAR=? AND AQ_MONTH=?)AQ_DTLS"
                    + " LEFT OUTER JOIN G_AD_LIST ON AQ_DTLS.AD_CODE=G_AD_LIST.AD_CODE_NAME";
            pst = con.prepareStatement(sql);
            pst.setString(1, aqslno);
            pst.setInt(2, year);
            pst.setInt(3, month);
            pvtdedRs = pst.executeQuery();
            while (pvtdedRs.next()) {
                ad = new ADDetails();
                if (pvtdedRs.getString("AD_ABBR") != null && !pvtdedRs.getString("AD_ABBR").equals("")) {
                    ad.setAdCodeDesc(pvtdedRs.getString("AD_ABBR"));
                }
                ad.setAdAmt(pvtdedRs.getInt("AD_AMT"));
                Double amt = Double.parseDouble(pvtdedRs.getString("AD_AMT"));
                //sum1 += Integer.parseInt(pvtdedRs.getString("AD_AMT"));
                sum1 += amt.intValue();
                ad.setAdAmount(sum1);
                list.add(ad);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pvtdedRs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public List getLoanList(String aqslno, int year, int month) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet loanRs = null;

        ADDetails ad = null;

        ArrayList list = new ArrayList();

        int sum = 0;
        int sum1 = 0;
        try {
            con = dataSource.getConnection();

            String aqdtlsTbl = AqFunctionalities.getAQBillDtlsTable(month, year);

            String sql = "SELECT PVT_DESC,AD_DESC,AD_CODE,AD_AMT,REF_DESC,AQ_DTLS.NOW_DEDN,AD_REF_ID,TOT_REC_AMT,P_ORG_AMT,I_ORG_AMT,LOAN_ABBR FROM (SELECT AD_DESC,AD_CODE,AD_AMT,REF_DESC,AQ_DTLS.NOW_DEDN,AD_REF_ID,TOT_REC_AMT,P_ORG_AMT,I_ORG_AMT,PVT_DESC FROM(SELECT AD_DESC,AD_CODE,AD_AMT,REF_DESC,NOW_DEDN,AD_REF_ID,TOT_REC_AMT FROM " + aqdtlsTbl + " WHERE AD_TYPE='D' AND DED_TYPE = 'L' AND AD_AMT > 0 AND AQSL_NO='" + aqslno + "' AND SCHEDULE !='PVTL' AND SCHEDULE !='PVTD' AND AQ_YEAR='" + year + "' AND AQ_MONTH='" + month + "')AQ_DTLS"
                    + " INNER JOIN (SELECT LOANID,NOW_DEDN,P_ORG_AMT,I_ORG_AMT,PVT_DESC FROM EMP_LOAN_SANC)EMP_LOAN_SANC ON AQ_DTLS.AD_REF_ID = EMP_LOAN_SANC.LOANID )AQ_DTLS"
                    + " LEFT OUTER JOIN G_LOAN ON AQ_DTLS.AD_CODE=G_LOAN.LOAN_TP";

            pst = con.prepareStatement(sql);
            loanRs = pst.executeQuery();
            while (loanRs.next()) {
                ad = new ADDetails();
                if (loanRs.getString("LOAN_ABBR") != null && !loanRs.getString("LOAN_ABBR").equals("")) {
                    ad.setAdCode(loanRs.getString("LOAN_ABBR"));
                } else {
                    ad.setAdCode(loanRs.getString("PVT_DESC").toUpperCase());
                }
                if (loanRs.getString("REF_DESC") != null) {
                    ad.setAdDesc(loanRs.getString("AD_AMT") + " (" + loanRs.getString("REF_DESC") + ")");
                } else {
                    ad.setAdDesc(loanRs.getString("AD_AMT"));
                }

                ad.setPrincipalAmt(loanRs.getInt("P_ORG_AMT"));
                ad.setInterestAmt(loanRs.getInt("I_ORG_AMT"));
                ad.setTotRecAmt(loanRs.getInt("TOT_REC_AMT"));
                ad.setNowDedn(loanRs.getString("NOW_DEDN"));
                ad.setRefDesc(loanRs.getString("REF_DESC"));

                ad.setAdAmt(loanRs.getInt("AD_AMT"));

                sum1 += loanRs.getDouble("AD_AMT");
                ad.setAdAmount(sum1);
                list.add(ad);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(loanRs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public void getPaySlipPDF(Document document, PaySlipDetailBean pbeandetail, ArrayList allownacelist, ArrayList deductionlist, ArrayList privatedeductionlist, ArrayList loanlist) {

        try {
            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            PdfPTable table = null;
            PdfPTable innertable = null;

            PdfPCell cell = null;
            PdfPCell innercell = null;

            table = new PdfPTable(6);
            table.setWidths(new int[]{2, 2, 2, 2, 2, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("PAY SLIP", f1));
            cell.setColspan(6);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(6);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(" + pbeandetail.getOffName() + ")", f1));
            cell.setColspan(6);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(6);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Name and Designation of the Incumbent: " + pbeandetail.getEmpName() + "," + pbeandetail.getCurDesig(), f1));
            cell.setColspan(6);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 150)));
            cell.setColspan(6);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("GPF Ac No:", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(pbeandetail.getGpfno(), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("For the Month of:", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(pbeandetail.getForMonth() + "-" + pbeandetail.getForYear(), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Bank:", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(pbeandetail.getBank(), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Scale of Pay:", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(pbeandetail.getScalePay(), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("No of days worked:", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(pbeandetail.getDaysWork(), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Bank A/c No:", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(pbeandetail.getBankAcno(), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Current Basic:", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(pbeandetail.getCurBasic(), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Pay of the Month:", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(pbeandetail.getCurBasic(), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Bill No:", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(pbeandetail.getBillNo(), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("TV No:", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(pbeandetail.getVchno(), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("TV Date:", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(pbeandetail.getVchdate(), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Bill Date:", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(pbeandetail.getBilldate(), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            innertable = new PdfPTable(3);
            innertable.setWidths(new int[]{2, 2, 2});
            innertable.setWidthPercentage(100);

            innercell = new PdfPCell(new Phrase("SL", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            innertable.addCell(cell);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public String getBillNo(String aqslno, int year, int month) throws Exception {

        Connection con = null;

        PreparedStatement stmt = null;
        ResultSet rs = null;

        String billNO = "";
        try {
            con = dataSource.getConnection();
            stmt = con.prepareStatement("select AQ_MAST.BILL_NO from AQ_MAST where aqsl_no=? AND AQ_YEAR=? AND AQ_MONTH=?");
            stmt.setString(1, aqslno);
            stmt.setInt(2, year);
            stmt.setInt(3, month);
            rs = stmt.executeQuery();
            if (rs.next()) {
                billNO = rs.getString("BILL_NO");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billNO;
    }

    public String getBillNo1(String empid, int year, int month) throws Exception {

        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;

        String billNO = "";
        try {
            con = dataSource.getConnection();

            stmt = con.createStatement();
            String sql = "select bill_no from aq_mast where emp_code='" + empid + "' and aq_year='" + year + "' and aq_month='" + month + "'";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                billNO = rs.getString("bill_no");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billNO;
    }

    public String getAqDtlsTableName(String billNo) {

        Connection con = null;

        Statement stmt = null;
        ResultSet res = null;

        String aqDTLS = "AQ_DTLS";
        try {
            con = dataSource.getConnection();

            stmt = con.createStatement();
            res = stmt.executeQuery("SELECT aq_month,aq_year FROM BILL_MAST WHERE bill_no='" + billNo + "'");
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
    public String getTokenGeneratedBillNo(String empid, int year, int month) {

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String billNO = "";
        String deputationSpc = "";
        String foreignBody = "";

        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();

            String deputationSpcQuery = "select deputation_spc,deputation_offcode,is_foreign_body from\n"
                    + "emp_Mast inner join g_office on emp_Mast.deputation_offcode=g_office.off_code\n"
                    + "WHERE emp_id=? AND dep_code='07'";

            PreparedStatement pstmt = con.prepareStatement(deputationSpcQuery);
            pstmt.setString(1, empid);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                deputationSpc = rs.getString("deputation_spc");
                foreignBody = rs.getString("is_foreign_body");
            }

            if ((deputationSpc != null && !deputationSpc.equals("")) && foreignBody.equals("Y")) {
               // System.out.println("deputationSpc is:" + deputationSpc);
                String query = "select bill_mast.bill_no from (select bill_no from aq_mast where emp_code='" + empid + "' and aq_year=" + year + " and aq_month=" + month + " and cur_spc='" + deputationSpc + "')aq_mast\n"
                        + "inner join (select bill_no,bill_status_id from bill_mast where bill_status_id >= 5)bill_mast on aq_mast.bill_no=bill_mast.bill_no";
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    billNO = rs.getString("bill_no");
                }
            } else {

                String sql = "select bill_mast.bill_no from (select bill_no from aq_mast where emp_code='" + empid + "' and aq_year=" + year + " and aq_month=" + month + ")aq_mast"
                        + " inner join (select bill_no,bill_status_id from bill_mast where bill_status_id >= 5)bill_mast on aq_mast.bill_no=bill_mast.bill_no";
                rs = stmt.executeQuery(sql);

                if (rs.next()) {
                    billNO = rs.getString("bill_no");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billNO;
    }

    @Override
    public List getPayBillData(int billNo, int aqMonth, int aqYear) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PaySlipListBean pBean = null;
        List payList = new ArrayList();
        try {
            con = dataSource.getConnection();

            String sql1 = "SELECT DOS,BRASS_NO,BILL_NO,AQSL_NO,AQ_MAST.EMP_CODE,AQ_MAST.EMP_NAME,CUR_DESG,AQ_MAST.PAY_SCALE,"
                    + "AQ_MAST.BANK_ACC_NO,CUR_BASIC,SPC_ORD_NO,SPC_ORD_DATE,GPF_ACC_NO,ID_NO,PAY_DAY,AQ_MAST.pay_commission, da_rate FROM "
                    + "(SELECT * FROM AQ_MAST WHERE BILL_NO =? and aq_month=? and aq_year=? order by POST_SL_NO) AQ_MAST "
                    + "INNER JOIN (select dos,emp_id,BRASS_NO from emp_mast)EMP_MAST ON AQ_MAST.EMP_CODE = EMP_MAST.EMP_ID "
                    + "LEFT OUTER JOIN (SELECT * FROM EMP_ID_DOC WHERE ID_DESCRIPTION='PAN')EMP_ID_DOC "
                    + "ON AQ_MAST.EMP_CODE = EMP_ID_DOC.EMP_ID order by POST_SL_NO";
            pstmt = con.prepareStatement(sql1);
            pstmt.setInt(1, billNo);
            pstmt.setInt(2, aqMonth);
            pstmt.setInt(3, aqYear);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                if (rs.getString("EMP_NAME") != null && !rs.getString("EMP_NAME").equals("")) {
                    pBean = new PaySlipListBean();

                    if (rs.getString("BRASS_NO") != null && !rs.getString("BRASS_NO").equals("")) {
                        pBean.setEmpName(rs.getString("BRASS_NO") + " " + rs.getString("EMP_NAME"));
                    } else {
                        pBean.setEmpName(rs.getString("EMP_NAME"));
                    }
                    pBean.setEmpId(rs.getString("EMP_CODE"));
                    pBean.setCurDesg(rs.getString("CUR_DESG"));
                    pBean.setGpfNo(rs.getString("GPF_ACC_NO"));
                    //pBean.setPayScale(rs.getString("PAY_SCALE"));
                    pBean.setPayDay(rs.getInt("PAY_DAY"));
                    pBean.setPanNo(rs.getString("ID_NO"));
                    pBean.setAqslno(rs.getString("AQSL_NO"));
                    pBean.setBasic(rs.getString("CUR_BASIC"));
                    pBean.setBankaccno(rs.getString("BANK_ACC_NO"));
                    pBean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOS")));
                    pBean.setPayCommission(rs.getString("pay_commission"));
                    pBean.setPayScale(rs.getString("PAY_SCALE"));
                    pBean.setDaformula(rs.getString("da_rate"));

                    payList.add(pBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return payList;
    }

    public void printHeader(PdfPTable table, PdfPCell cell, CommonReportParamBean crb, Font f1) throws Exception {

        cell = new PdfPCell(new Phrase(crb.getOfficename(), f1));
        cell.setColspan(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Payslip for the month of : " + CalendarCommonMethods.getMonthAsString(crb.getAqmonth()) + " - " + crb.getAqyear(), f1));
        cell.setColspan(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        String billDesc = "";
        if (crb.getBilldesc() != null && !crb.getBilldesc().equals("")) {
            billDesc = crb.getBilldesc();
        } else {
            billDesc = "";
        }

        cell = new PdfPCell(new Phrase("Bill No: " + billDesc, f1));
        cell.setColspan(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("DDO Name: " + crb.getDdoName(), f1));
        cell.setColspan(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    @Override
    public void getPaySlipBillWise(Document document, List dataList, int billNo, CommonReportParamBean crb, String qrcodepath) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        PreparedStatement pstmtA = null;
        ResultSet allowanceRs = null;

        PreparedStatement pstmtD = null;
        ResultSet deductionRs = null;

        PreparedStatement pstmtL = null;
        ResultSet loanRs = null;

        String aqDtlsTbl = "";
        String year = "";
        String month = "";
        String billdesc = "";
        String billdate = "";
        String vchno = "";

        double billGrossAmt = 0;
        double billDeductionAmt = 0;
        double billPvtDeduction = 0;
        double billNetAmt = 0;

        String prevDaRate = "";
        String daRate = "";

        try {
            aqDtlsTbl = getAqDtlsTableName(billNo + "");
            con = dataSource.getConnection();

            Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 8.4f, Font.NORMAL, BaseColor.BLACK);
            Font bigTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 8.6f, Font.NORMAL, BaseColor.BLACK);
            Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 9f, Font.BOLD, BaseColor.BLACK);

            String sql1 = "SELECT AQ_MONTH,AQ_YEAR,BILL_DESC,BILL_DATE,VCH_NO,VCH_DATE,GROSS_AMT,DED_AMT FROM BILL_MAST WHERE BILL_NO=? and aq_month=? and aq_year=?";
            pstmt = con.prepareStatement(sql1);
            pstmt.setInt(1, billNo);
            pstmt.setInt(2, crb.getAqmonth());
            pstmt.setInt(3, crb.getAqyear());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                year = rs.getString("AQ_YEAR");
                month = rs.getString("AQ_MONTH");
                billdesc = rs.getString("BILL_DESC");
                vchno = StringUtils.defaultString(rs.getString("VCH_NO"));
                if (rs.getString("VCH_DATE") != null && !rs.getString("VCH_DATE").equals("")) {
                    vchno = vchno + " dtd " + CommonFunctions.getFormattedOutputDate1(rs.getDate("VCH_DATE"));

                }
                if (rs.getString("BILL_DATE") != null) {
                    billdate = "/" + CommonFunctions.getFormattedOutputDate(rs.getDate("BILL_DATE"));
                }
            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            billGrossAmt = crb.getBillGrossAmt();
            billDeductionAmt = crb.getBillDedAmt();
            billPvtDeduction = crb.getBillPvtDedAmt();

            billNetAmt = billGrossAmt - billDeductionAmt;

            //DA Verification Commented as per Manas Sir on dated 09-05-2018 01:32 PM
//            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
//            Date prevDaDate = null;
//            String sql6 = "SELECT WEF_FROM,AD_FORMULA FROM G_AD_LIST_HISTORY WHERE AD_CODE_NAME=?";
//            pstmt = con.prepareStatement(sql6);
//            pstmt.setString(1, "DA");
//            rs = pstmt.executeQuery();
//            if(rs.next()){
//                //prevDaDate = rs.getDate("WEF_FROM");
//                prevDaDate = CommonFunctions.getFormattedOutputDate(rs.getDate("WEF_FROM"));
//                prevDaRate = rs.getString("AD_FORMULA");
//            }
//            DataBaseFunctions.closeSqlObjects(rs,pstmt);
//            
//            String billDateString = "25-"+CalendarCommonMethods.getMonthAsString(Integer.parseInt(month))+"-"+year;
//            String newDaDate = "15-OCT-2014";
//            Date curbillDate = sdf.parse(billDateString);
//            Date newdadate = sdf.parse(newDaDate);
//            if(curbillDate.compareTo(newdadate) < 0 ){
//                if(curbillDate.compareTo(prevDaDate) > 0){
//                    daRate = prevDaRate;
//                }
//            }else{
//                String sql7 = "SELECT AD_FORMULA FROM G_AD_LIST WHERE AD_CODE_NAME=?";
//                pstmt = con.prepareStatement(sql7);
//                pstmt.setString(1, "DA");
//                rs = pstmt.executeQuery();
//                if(rs.next()){
//                    daRate = rs.getString("AD_FORMULA");
//                }
//            }
//            DataBaseFunctions.closeSqlObjects(rs,pstmt);
            PdfPTable table = new PdfPTable(5);
            table.setWidths(new float[]{1, 2, 1, 1, 2});
            table.setWidthPercentage(100);

            PdfPCell cell = null;
            int slno = 0;

            if (dataList != null && dataList.size() > 0) {
                PaySlipListBean psbean = null;
                for (int i = 0; i < dataList.size(); i++) {
                    psbean = (PaySlipListBean) dataList.get(i);

                    table = new PdfPTable(5);
                    table.setWidths(new float[]{1, 3, 1, 1, 2});
                    table.setWidthPercentage(100);

                    printHeader(table, cell, crb, boldTextFont);

                    cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 185), textFont));
                    cell.setColspan(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("HRMS ID", textFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(": " + StringUtils.defaultString(psbean.getEmpId()), boldTextFont));
                    cell.setColspan(2);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Bill No", textFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(": " + StringUtils.defaultString(billdesc), boldTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Name", textFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(": " + StringUtils.defaultString(psbean.getEmpName()), boldTextFont));
                    cell.setColspan(2);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Bill Gross/Net", textFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(": " + billGrossAmt + " / " + billNetAmt, boldTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Designation", textFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(": " + StringUtils.defaultString(psbean.getCurDesg()), boldTextFont));
                    cell.setColspan(2);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    String payCommission = "Scale of Pay";
                    if (psbean.getPayCommission() != null && !psbean.getPayCommission().equals("")) {
                        if (psbean.getPayCommission().equals("6")) {
                            payCommission = "Scale of Pay";
                        } else if (psbean.getPayCommission().equals("7")) {
                            payCommission = "Pay Matrix";
                        }
                    }

                    cell = new PdfPCell(new Phrase(payCommission, textFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(": " + StringUtils.defaultString(psbean.getPayScale()), boldTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("GPF A/C", textFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(": " + StringUtils.defaultString(psbean.getGpfNo()), boldTextFont));
                    cell.setColspan(2);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("TV No & Dt", textFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(": " + StringUtils.defaultString(vchno), boldTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("PAN No", textFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(": " + StringUtils.defaultString(psbean.getPanNo()), boldTextFont));
                    cell.setColspan(2);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("TDays Attn.", textFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(": " + psbean.getPayDay(), boldTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Bank A/c No", textFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(": " + StringUtils.defaultString(psbean.getBankaccno()), boldTextFont));
                    cell.setColspan(2);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("DA Rate", textFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(": " + StringUtils.defaultString(psbean.getDaformula()), boldTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 185), textFont));
                    cell.setColspan(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    document.add(table);

                    table = new PdfPTable(5);
                    table.setWidths(new float[]{2, 2, 1, 0.7f, 1.2f});
                    table.setWidthPercentage(100);

                    cell = new PdfPCell(new Phrase("Earnings...", textFont));
                    //cell.setColspan(2);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Deductions...", textFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Loan/Advance Recovery...", textFont));
                    cell.setColspan(3);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 185), textFont));
                    cell.setColspan(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    //psbean.setAqslno("21910000000865_3_2017_7");
                    String alowanceQry = "SELECT G_AD_LIST.AD_DESC,AD_AMT,REF_DESC,G_AD_LIST.AD_CODE,AD_ABBR FROM "
                            + "(SELECT AD_DESC,AD_AMT,REF_DESC,AD_CODE FROM " + aqDtlsTbl + " WHERE AD_TYPE='A' AND AD_AMT > 0 "
                            + "AND AQSL_NO= ? ORDER BY REP_COL,SL_NO )AQ_DTLS"
                            + " LEFT OUTER JOIN G_AD_LIST ON AQ_DTLS.AD_CODE=G_AD_LIST.AD_CODE_NAME";
                    pstmtA = con.prepareStatement(alowanceQry);
                    pstmtA.setString(1, psbean.getAqslno());
                    allowanceRs = pstmtA.executeQuery();

                    String deductionQry = "SELECT AQ_DTLS.AD_DESC,AD_AMT,REF_DESC,AQ_DTLS.AD_CODE,NOW_DEDN,AD_ABBR FROM "
                            + "(SELECT AD_DESC,AD_AMT,REF_DESC,AD_CODE,NOW_DEDN FROM " + aqDtlsTbl + " WHERE AD_TYPE='D' AND DED_TYPE != 'L' "
                            + "AND AD_AMT > 0 AND AQSL_NO= ? AND SCHEDULE !='PVTL' AND SCHEDULE !='PVTD')AQ_DTLS"
                            + " LEFT OUTER JOIN G_AD_LIST ON AQ_DTLS.AD_CODE=G_AD_LIST.AD_CODE_NAME order by AD_ABBR";

                    pstmtD = con.prepareStatement(deductionQry);
                    pstmtD.setString(1, psbean.getAqslno());
                    deductionRs = pstmtD.executeQuery();

                    String loanSql = "SELECT PVT_DESC,AD_DESC,AD_CODE,AD_AMT,REF_DESC,AQ_DTLS.NOW_DEDN,AD_REF_ID,TOT_REC_AMT,P_ORG_AMT,I_ORG_AMT,LOAN_ABBR FROM"
                            + " (SELECT PVT_DESC,AD_DESC,AD_CODE,AD_AMT,REF_DESC,AQ_DTLS.NOW_DEDN,AD_REF_ID,TOT_REC_AMT,P_ORG_AMT,I_ORG_AMT FROM(SELECT AD_DESC,AD_CODE,AD_AMT,"
                            + " REF_DESC,NOW_DEDN,AD_REF_ID,TOT_REC_AMT FROM " + aqDtlsTbl + " WHERE AD_TYPE='D' AND DED_TYPE = 'L' AND AD_AMT > 0 AND AQSL_NO=?)AQ_DTLS"
                            + " INNER JOIN (SELECT LOANID,NOW_DEDN,P_ORG_AMT,I_ORG_AMT,PVT_DESC FROM EMP_LOAN_SANC)EMP_LOAN_SANC ON AQ_DTLS.AD_REF_ID ="
                            + " EMP_LOAN_SANC.LOANID )AQ_DTLS"
                            + " LEFT OUTER JOIN G_LOAN ON AQ_DTLS.AD_CODE=G_LOAN.LOAN_TP"
                            + " UNION"
                            + " SELECT PVT_DESC,AD_DESC,AD_CODE,AD_AMT,REF_DESC,AQ_DTLS.NOW_DEDN,AD_REF_ID,TOT_REC_AMT,P_ORG_AMT,I_ORG_AMT,LOAN_ABBR FROM"
                            + " (SELECT PVT_DESC,AD_DESC,AD_CODE,AD_AMT,REF_DESC,AQ_DTLS.NOW_DEDN,AD_REF_ID,TOT_REC_AMT,P_ORG_AMT,I_ORG_AMT FROM(SELECT AD_DESC,AD_CODE,AD_AMT,"
                            + " REF_DESC,NOW_DEDN,AD_REF_ID,TOT_REC_AMT FROM " + aqDtlsTbl + " WHERE AD_TYPE='D' AND DED_TYPE = 'L' AND AD_AMT > 0 AND (SCHEDULE ='PVTL' OR SCHEDULE"
                            + " ='PVTD') AND AQSL_NO=?)AQ_DTLS"
                            + " INNER JOIN (SELECT LOANID,NOW_DEDN,P_ORG_AMT,I_ORG_AMT,PVT_DESC FROM EMP_LOAN_SANC)EMP_LOAN_SANC ON AQ_DTLS.AD_REF_ID ="
                            + " EMP_LOAN_SANC.LOANID )AQ_DTLS"
                            + " LEFT OUTER JOIN G_LOAN ON AQ_DTLS.AD_CODE=G_LOAN.LOAN_TP";
                    pstmtL = con.prepareStatement(loanSql);
                    pstmtL.setString(1, psbean.getAqslno());
                    pstmtL.setString(2, psbean.getAqslno());
                    loanRs = pstmtL.executeQuery();

                    int grosspay = 0;
                    int deduction = 0;

                    PdfPTable allowancetable = null;
                    PdfPCell allowanceCell = null;

                    PdfPTable dedtable = null;
                    PdfPCell dedCell = null;

                    PdfPTable loantable = null;
                    PdfPCell loanCell = null;
                    //int k = 0;
                    for (int j = 0; j < 18; j++) {

                        //Allowances
                        allowancetable = new PdfPTable(2);
                        allowancetable.setWidths(new float[]{0.5f, 0.5f});
                        allowancetable.setWidthPercentage(100);

                        String allowanceAbr = "";
                        String allowanceAmt = "";
                        if (j == 0) {
                            allowanceAbr = "Basic";
                            allowanceAmt = psbean.getBasic() + "";
                            grosspay = grosspay + Integer.parseInt(psbean.getBasic());

                        } else {
                            if (allowanceRs.next()) {
                                allowanceAbr = allowanceRs.getString("AD_ABBR");
                                allowanceAmt = allowanceRs.getInt("AD_AMT") + "";
                                grosspay = grosspay + allowanceRs.getInt("AD_AMT");
                            }
                        }
                        allowanceCell = new PdfPCell(new Phrase(allowanceAbr, boldTextFont));
                        allowanceCell.setBorder(Rectangle.NO_BORDER);
                        allowancetable.addCell(allowanceCell);
                        allowanceCell = new PdfPCell(new Phrase(allowanceAmt, boldTextFont));
                        allowanceCell.setBorder(Rectangle.RIGHT);
                        allowanceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        allowancetable.addCell(allowanceCell);

                        cell = new PdfPCell(allowancetable);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        //Deductions
                        dedtable = new PdfPTable(2);
                        dedtable.setWidths(new float[]{0.5f, 0.5f});
                        dedtable.setWidthPercentage(100);

                        String deductionAbr = "";
                        String deductionAmt = "";

                        if (deductionRs.next()) {

                            deductionAbr = deductionRs.getString("AD_ABBR");
                            deductionAmt = deductionRs.getInt("AD_AMT") + "";
                            deduction = deduction + deductionRs.getInt("AD_AMT");
                        }

                        dedCell = new PdfPCell(new Phrase(deductionAbr, boldTextFont));
                        dedCell.setBorder(Rectangle.NO_BORDER);
                        dedCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        dedtable.addCell(dedCell);

                        dedCell = new PdfPCell(new Phrase(deductionAmt, boldTextFont));
                        dedCell.setBorder(Rectangle.RIGHT);
                        dedCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        dedtable.addCell(dedCell);

                        cell = new PdfPCell(dedtable);
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);

                        //Loans
                        loantable = new PdfPTable(4);
                        loantable.setWidths(new float[]{0.8f, 0.4f, 0.3f, 0.4f});
                        loantable.setWidthPercentage(100);

                        String loanAbr = "";
                        int loanAmt = 0;
                        if (j <= 2) {
                            if (j == 0) {
                                //loanAbr = StringUtils.repeat("-",50);
                                loanCell = new PdfPCell(new Phrase(StringUtils.repeat("-", 76), textFont));
                                loanCell.setColspan(4);
                                loanCell.setBorder(Rectangle.LEFT);
                                loantable.addCell(loanCell);
                            } else if (j == 1) {
                                //loanAbr = "Description     Amount   Instl. Balance";
                                loanCell = new PdfPCell(new Phrase("Description", textFont));
                                loanCell.setBorder(Rectangle.LEFT);
                                loanCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                loantable.addCell(loanCell);

                                loanCell = new PdfPCell(new Phrase("Amount", textFont));
                                loanCell.setBorder(Rectangle.NO_BORDER);
                                loantable.addCell(loanCell);

                                loanCell = new PdfPCell(new Phrase("Instl.", textFont));
                                loanCell.setBorder(Rectangle.NO_BORDER);
                                loantable.addCell(loanCell);

                                loanCell = new PdfPCell(new Phrase("Balance", textFont));
                                loanCell.setBorder(Rectangle.NO_BORDER);
                                loantable.addCell(loanCell);
                            } else if (j == 2) {
                                //loanAbr = StringUtils.repeat("-",50);
                                loanCell = new PdfPCell(new Phrase(StringUtils.repeat("-", 76), textFont));
                                loanCell.setColspan(4);
                                loanCell.setBorder(Rectangle.LEFT);
                                loantable.addCell(loanCell);
                            }
                        } else {
                            int balanceAmt = 0;
                            if (loanRs.next()) {
                                if (loanRs.getString("LOAN_ABBR") != null && !loanRs.getString("LOAN_ABBR").equals("")) {
                                    loanAbr = loanRs.getString("LOAN_ABBR");
                                } else {
                                    loanAbr = loanRs.getString("PVT_DESC");
                                }
                                deduction = deduction + loanRs.getInt("AD_AMT");
                                if (loanRs.getString("NOW_DEDN").equals("P")) {
                                    balanceAmt = loanRs.getInt("P_ORG_AMT") - loanRs.getInt("TOT_REC_AMT");
                                    loanAbr = loanAbr + "(P)";
                                } else if (loanRs.getString("NOW_DEDN").equals("I")) {
                                    balanceAmt = loanRs.getInt("I_ORG_AMT") - loanRs.getInt("TOT_REC_AMT");
                                    loanAbr = loanAbr + "(I)";
                                }
                                loanCell = new PdfPCell(new Phrase(StringUtils.defaultString(loanAbr), boldTextFont));
                                loanCell.setBorder(Rectangle.LEFT);
                                loanCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                loantable.addCell(loanCell);

                                loanCell = new PdfPCell(new Phrase(loanRs.getString("AD_AMT"), boldTextFont));
                                loanCell.setBorder(Rectangle.NO_BORDER);
                                loanCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                loantable.addCell(loanCell);

                                loanCell = new PdfPCell(new Phrase(StringUtils.defaultString(loanRs.getString("REF_DESC")), boldTextFont));
                                loanCell.setBorder(Rectangle.NO_BORDER);
                                loanCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                loantable.addCell(loanCell);

                                loanCell = new PdfPCell(new Phrase(balanceAmt + "", boldTextFont));
                                loanCell.setBorder(Rectangle.NO_BORDER);
                                loanCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                loantable.addCell(loanCell);
                            }
                        }
                        //cell = new PdfPCell(new Phrase(StringUtils.defaultString(loanRs.getString("LOAN_ABBR")+"   "+loanRs.getString("AD_AMT")+"   "+StringUtils.defaultString(loanRs.getString("REF_DESC"))+"  "+balanceAmt),f1));
                        cell = new PdfPCell(loantable);
                        cell.setColspan(3);
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);
                    }
                    int netpay = grosspay - deduction;
                    if (netpay < 0) {
                        netpay = Math.abs(netpay);
                    }

                    cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 185), textFont));
                    cell.setColspan(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    allowancetable = new PdfPTable(2);
                    allowancetable.setWidths(new float[]{0.5f, 0.5f});
                    allowancetable.setWidthPercentage(100);

                    allowanceCell = null;

                    allowanceCell = new PdfPCell(new Phrase("Gross Pay", textFont));
                    allowanceCell.setBorder(Rectangle.NO_BORDER);
                    allowancetable.addCell(allowanceCell);

                    allowanceCell = new PdfPCell(new Phrase(": " + grosspay, boldTextFont));
                    allowanceCell.setBorder(Rectangle.NO_BORDER);
                    allowanceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    allowancetable.addCell(allowanceCell);

                    cell = new PdfPCell(allowancetable);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    dedtable = new PdfPTable(2);
                    dedtable.setWidths(new float[]{0.5f, 0.5f});
                    dedtable.setWidthPercentage(100);

                    dedCell = null;

                    dedCell = new PdfPCell(new Phrase("Total Dedn.", textFont));
                    dedCell.setBorder(Rectangle.NO_BORDER);
                    dedCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    dedtable.addCell(dedCell);

                    dedCell = new PdfPCell(new Phrase(": " + deduction, boldTextFont));
                    dedCell.setBorder(Rectangle.NO_BORDER);
                    dedCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    dedtable.addCell(dedCell);

                    cell = new PdfPCell(dedtable);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    loantable = new PdfPTable(4);
                    loantable.setWidths(new float[]{0.8f, 0.4f, 0.3f, 0.4f});
                    loantable.setWidthPercentage(100);

                    loanCell = null;

                    loanCell = new PdfPCell(new Phrase("Net Pay", textFont));
                    loanCell.setBorder(Rectangle.NO_BORDER);
                    loanCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    loantable.addCell(loanCell);

                    loanCell = new PdfPCell(new Phrase(":" + netpay, boldTextFont));
                    loanCell.setBorder(Rectangle.NO_BORDER);
                    loanCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    loantable.addCell(loanCell);

                    loanCell = new PdfPCell();
                    loanCell.setBorder(Rectangle.NO_BORDER);
                    loantable.addCell(loanCell);

                    loanCell = new PdfPCell();
                    loanCell.setBorder(Rectangle.NO_BORDER);
                    loantable.addCell(loanCell);

                    cell = new PdfPCell(loantable);
                    cell.setColspan(3);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Rupees (" + Numtowordconvertion.convertNumber((int) netpay).toUpperCase() + ") Only", boldTextFont));
                    cell.setColspan(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 185), textFont));
                    cell.setColspan(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setColspan(5);
                    cell.setFixedHeight(20);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    
                    Image qrImage = generateQRCodeForPaySlip(document, psbean.getEmpId(), psbean.getEmpName(), grosspay + "", netpay + "");
                    
                    if (qrImage != null) {
                        qrImage.scaleToFit(40f, 40f);
                        cell = new PdfPCell(qrImage);
                        cell.setColspan(2);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                    }else{                    
                        cell = new PdfPCell();
                        cell.setColspan(2);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                    }
                    cell = new PdfPCell(new Phrase("Authorised Signatory/Bill Assistant", textFont));
                    cell.setColspan(3);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(" ", boldTextFont));
                    cell.setColspan(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("N.B YOU MAY ALSO VIEW THIS PAYSLIP BY USING HRMS ODISHA USING FOLLOWING URL", boldTextFont));
                    cell.setColspan(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("(http://hrmsodisha.gov.in)", boldTextFont));
                    cell.setColspan(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    
                    slno++;
                    if (slno % 2 == 0) {
                        document.add(table);
                        document.newPage();

                        table = new PdfPTable(5);
                        table.setWidths(new float[]{1, 2, 1, 0.7f, 1.2f});
                        table.setWidthPercentage(100);
                    }

                    if (slno % 2 != 0) {
                        cell = new PdfPCell();
                        cell.setColspan(5);
                        cell.setFixedHeight(30);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                    }
                    document.add(table);

                    //generateQRCodeForPreviousYear(psbean.getEmpId(), psbean.getEmpName(), grosspay + "", netpay + "", qrcodepath, crb.getAqyear(), crb.getAqmonth());                    
                }
            }
            if (slno == 0) {
                Paragraph p1 = new Paragraph("No Record");
                //p1.setAlignment();
                document.add(p1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(allowanceRs, pstmtA);
            DataBaseFunctions.closeSqlObjects(deductionRs, pstmtD);
            DataBaseFunctions.closeSqlObjects(loanRs, pstmtL);
            DataBaseFunctions.closeSqlObjects(con);
            document.close();
        }
    }

    @Override
    public void getPaySlipBillWise2(Document document, List dataList, int billNo, CommonReportParamBean crb) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        PreparedStatement pstmtA = null;
        ResultSet allowanceRs = null;

        PreparedStatement pstmtD = null;
        ResultSet deductionRs = null;

        PreparedStatement pstmtL = null;
        ResultSet loanRs = null;

        PreparedStatement pstmtPD = null;
        ResultSet pvtDedRs = null;

        String aqDtlsTbl = "";
        String year = "";
        String month = "";
        String billdesc = "";
        String billdate = "";
        String vchno = "";

        double billGrossAmt = 0;
        double billDeductionAmt = 0;
        double billPvtDeduction = 0;
        double billNetAmt = 0;

        String prevDaRate = "";
        String daRate = "";

        try {
            aqDtlsTbl = getAqDtlsTableName(billNo + "");
            con = dataSource.getConnection();

            Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 8f, Font.NORMAL, BaseColor.BLACK);
            Font bigTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 8.2f, Font.NORMAL, BaseColor.BLACK);
            Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 8.5f, Font.BOLD, BaseColor.BLACK);
            Font boldTextFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 7f, Font.BOLD, BaseColor.BLACK);

            String sql1 = "SELECT AQ_MONTH,AQ_YEAR,BILL_DESC,BILL_DATE,VCH_NO,VCH_DATE,GROSS_AMT,DED_AMT FROM BILL_MAST WHERE BILL_NO=? and aq_month=? and aq_year=?";
            pstmt = con.prepareStatement(sql1);
            pstmt.setInt(1, billNo);
            pstmt.setInt(2, crb.getAqmonth());
            pstmt.setInt(3, crb.getAqyear());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                year = rs.getString("AQ_YEAR");
                month = rs.getString("AQ_MONTH");
                billdesc = rs.getString("BILL_DESC");
                vchno = StringUtils.defaultString(rs.getString("VCH_NO"));
                if (rs.getString("VCH_DATE") != null && !rs.getString("VCH_DATE").equals("")) {
                    vchno = vchno + " dtd " + CommonFunctions.getFormattedOutputDate1(rs.getDate("VCH_DATE"));

                }
                if (rs.getString("BILL_DATE") != null) {
                    billdate = "/" + CommonFunctions.getFormattedOutputDate(rs.getDate("BILL_DATE"));
                }
            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            billGrossAmt = crb.getBillGrossAmt();
            billDeductionAmt = crb.getBillDedAmt();
            billPvtDeduction = crb.getBillPvtDedAmt();
            billNetAmt = billGrossAmt - billDeductionAmt;

            PdfPTable table = new PdfPTable(5);
            table.setWidths(new float[]{1, 2, 1, 1, 2});
            table.setWidthPercentage(100);

            PdfPCell cell = null;
            int slno = 0;

            if (dataList != null && dataList.size() > 0) {
                PaySlipListBean psbean = null;
                for (int i = 0; i < dataList.size(); i++) {
                    psbean = (PaySlipListBean) dataList.get(i);

                    table = new PdfPTable(5);
                    table.setWidths(new float[]{1, 3, 1, 1, 2});
                    table.setWidthPercentage(100);

                    printHeader(table, cell, crb, boldTextFont);

                    cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 193), textFont));
                    cell.setColspan(5);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    //1st Row    
                    cell = new PdfPCell(new Phrase("Name", boldTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(": " + StringUtils.defaultString(psbean.getEmpName()), bigTextFont));
                    cell.setColspan(2);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("HRMS ID", boldTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(": " + StringUtils.defaultString(psbean.getEmpId()), bigTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    //2nd Row        
                    cell = new PdfPCell(new Phrase("Designation", boldTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(": " + StringUtils.defaultString(psbean.getCurDesg()), bigTextFont));
                    cell.setColspan(2);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("GPF A/c", boldTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(": " + StringUtils.defaultString(psbean.getGpfNo()), bigTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    //3rd Row            
                    cell = new PdfPCell(new Phrase("PAN No", boldTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(": " + StringUtils.defaultString(psbean.getPanNo()), bigTextFont));
                    cell.setColspan(2);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Days Attn.", boldTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(": " + StringUtils.defaultString(psbean.getPayDay() + ""), bigTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    //4th Row        
                    cell = new PdfPCell(new Phrase("Bank A/c No", boldTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(": " + StringUtils.defaultString(psbean.getBankaccno()), bigTextFont));
                    cell.setColspan(2);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("DOS", boldTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(": " + StringUtils.defaultString(psbean.getDos()), bigTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    //5th Row            
                    cell = new PdfPCell(new Phrase("TV No & Dt", boldTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(": " + StringUtils.defaultString(vchno), bigTextFont));
                    cell.setColspan(2);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 193), textFont));
                    cell.setColspan(5);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    document.add(table);

                    table = new PdfPTable(5);
                    table.setWidths(new float[]{1, 2.5f, 1, 1, 2});
                    table.setWidthPercentage(100);

                    cell = new PdfPCell(new Phrase("Earnings...", boldTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Deductions...", boldTextFont));
                    cell.setColspan(3);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);

                    cell = new PdfPCell(new Phrase("Pvt. Ded....", boldTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 193), textFont));
                    cell.setColspan(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    String allowanceQry = "SELECT G_AD_LIST.AD_DESC,AD_AMT,REF_DESC,G_AD_LIST.AD_CODE,AD_ABBR,ad_code_name FROM (SELECT AD_DESC,AD_AMT,REF_DESC,AD_CODE FROM " + aqDtlsTbl + " WHERE AD_TYPE='A' AND AD_AMT > 0 "
                            + "AND AQSL_NO= ? ORDER BY REP_COL,SL_NO )AQ_DTLS LEFT OUTER JOIN G_AD_LIST ON AQ_DTLS.AD_CODE=G_AD_LIST.AD_CODE_NAME";
                    pstmtA = con.prepareStatement(allowanceQry);
                    pstmtA.setString(1, psbean.getAqslno());
                    allowanceRs = pstmtA.executeQuery();

                    String deductionQry = "SELECT AQ_DTLS.AD_DESC,AD_AMT,REF_DESC,AQ_DTLS.AD_CODE,NOW_DEDN,AD_ABBR,ACC_NO FROM (SELECT AD_DESC,AD_AMT,REF_DESC,AD_CODE,NOW_DEDN,ACC_NO "
                            + "FROM " + aqDtlsTbl + " WHERE AD_TYPE='D' AND DED_TYPE != 'L' AND AD_AMT > 0 AND AQSL_NO = ? AND SCHEDULE !='PVTL' AND SCHEDULE !='PVTD')AQ_DTLS "
                            + "LEFT OUTER JOIN G_AD_LIST ON AQ_DTLS.AD_CODE=G_AD_LIST.AD_CODE_NAME order by AD_ABBR";
                    pstmtD = con.prepareStatement(deductionQry);
                    pstmtD.setString(1, psbean.getAqslno());
                    deductionRs = pstmtD.executeQuery();

                    String loanQry = "SELECT AD_DESC,AD_CODE,AD_AMT,REF_DESC,AQ_DTLS.NOW_DEDN,AD_REF_ID,TOT_REC_AMT,P_ORG_AMT,I_ORG_AMT,LOAN_ABBR FROM (SELECT AD_DESC,AD_CODE,AD_AMT,REF_DESC,AQ_DTLS.NOW_DEDN,"
                            + "AD_REF_ID,TOT_REC_AMT,P_ORG_AMT,I_ORG_AMT FROM(SELECT AD_DESC,AD_CODE,AD_AMT,REF_DESC,NOW_DEDN,AD_REF_ID,TOT_REC_AMT FROM " + aqDtlsTbl + " WHERE AD_TYPE='D' AND DED_TYPE = 'L' "
                            + "AND AD_AMT > 0 AND SCHEDULE !='PVTL' AND SCHEDULE !='PVTD' AND AQSL_NO = ? )AQ_DTLS "
                            + "INNER JOIN (SELECT LOANID,NOW_DEDN,P_ORG_AMT,I_ORG_AMT FROM EMP_LOAN_SANC)EMP_LOAN_SANC ON AQ_DTLS.AD_REF_ID = EMP_LOAN_SANC.LOANID )AQ_DTLS "
                            + "LEFT OUTER JOIN G_LOAN ON AQ_DTLS.AD_CODE=G_LOAN.LOAN_TP";
                    pstmtL = con.prepareStatement(loanQry);
                    pstmtL.setString(1, psbean.getAqslno());
                    loanRs = pstmtL.executeQuery();

                    String pvtDedQry = "SELECT AQ_DTLS.AD_DESC,AD_AMT,REF_DESC,AQ_DTLS.AD_CODE,EMP_LOAN_SANC.NOW_DEDN,AD_ABBR,PVT_DESC FROM (SELECT AD_REF_ID,AD_DESC,AD_AMT,REF_DESC,AD_CODE,NOW_DEDN "
                            + "FROM " + aqDtlsTbl + " WHERE AD_TYPE='D' AND AD_AMT > 0 AND AQSL_NO = ? AND (SCHEDULE ='PVTL' OR SCHEDULE ='PVTD'))AQ_DTLS "
                            + "LEFT OUTER JOIN EMP_LOAN_SANC ON AQ_DTLS.AD_REF_ID=EMP_LOAN_SANC.LOANID "
                            + "LEFT OUTER JOIN G_AD_LIST ON AQ_DTLS.AD_CODE=G_AD_LIST.AD_CODE_NAME";
                    pstmtPD = con.prepareStatement(pvtDedQry);
                    pstmtPD.setString(1, psbean.getAqslno());
                    pvtDedRs = pstmtPD.executeQuery();

                    int grosspay = 0;
                    int deduction = 0;
                    int pvtded = 0;
                    boolean deductionAvl = false;

                    PdfPTable allowancetable = null;
                    PdfPCell allowanceCell = null;

                    PdfPTable dedcumLoantable = null;
                    PdfPCell dedcumLoanCell = null;

                    PdfPTable loantable = null;
                    PdfPCell loanCell = null;

                    PdfPTable pvtdedtable = null;
                    PdfPCell pvtdedcell = null;
                    int k = -1;

                    for (int j = 0; j < 18; j++) {

                        //Allowances
                        allowancetable = new PdfPTable(2);
                        allowancetable.setWidths(new float[]{0.5f, 0.5f});
                        allowancetable.setWidthPercentage(100);

                        String allowanceAbr = "";
                        String allowanceAmt = "";
                        if (j == 0) {

                            grosspay = grosspay + Integer.parseInt(psbean.getBasic());

                            allowanceCell = new PdfPCell(new Phrase("Basic", bigTextFont));
                            allowanceCell.setBorder(Rectangle.NO_BORDER);
                            allowancetable.addCell(allowanceCell);

                            allowanceCell = new PdfPCell(new Phrase(psbean.getBasic() + "", boldTextFont));
                            allowanceCell.setBorder(Rectangle.RIGHT);
                            allowanceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            allowancetable.addCell(allowanceCell);

                        } else {
                            if (allowanceRs.next()) {
                                //allowanceAbr = allowanceRs.getString("AD_ABBR");
                                allowanceAbr = allowanceRs.getString("ad_code_name");
                                allowanceAmt = allowanceRs.getInt("AD_AMT") + "";
                                grosspay = grosspay + allowanceRs.getInt("AD_AMT");

                                allowanceCell = new PdfPCell(new Phrase(StringUtils.defaultString(allowanceAbr), bigTextFont));
                                allowanceCell.setBorder(Rectangle.NO_BORDER);
                                allowancetable.addCell(allowanceCell);

                                allowanceCell = new PdfPCell(new Phrase(StringUtils.defaultString(allowanceAmt), boldTextFont));
                                allowanceCell.setBorder(Rectangle.NO_BORDER);
                                allowanceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                allowancetable.addCell(allowanceCell);
                            }
                        }

                        cell = new PdfPCell(allowancetable);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        //Deductions
                        String deductionAbr = "";
                        String deductionAmt = "";

                        if (deductionRs.next()) {

                            dedcumLoantable = new PdfPTable(2);
                            dedcumLoantable.setWidths(new float[]{0.5f, 0.5f});
                            dedcumLoantable.setWidthPercentage(100);

                            deductionAbr = deductionRs.getString("AD_ABBR");
                            deductionAmt = deductionRs.getInt("AD_AMT") + "";
                            deduction = deduction + deductionRs.getInt("AD_AMT");
                            deductionAvl = true;

                            dedcumLoanCell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 5) + StringUtils.defaultString(deductionAbr), bigTextFont));
                            dedcumLoanCell.setBorder(Rectangle.LEFT);
                            dedcumLoanCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            dedcumLoantable.addCell(dedcumLoanCell);

                            dedcumLoanCell = new PdfPCell(new Phrase(StringUtils.defaultString(deductionAmt), boldTextFont));
                            dedcumLoanCell.setBorder(Rectangle.RIGHT);
                            dedcumLoanCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            dedcumLoantable.addCell(dedcumLoanCell);

                        } else {
                            k++;
                            String loanAbr = "";
                            dedcumLoantable = new PdfPTable(4);
                            dedcumLoantable.setWidths(new float[]{0.4f, 0.2f, 0.2f, 0.2f});
                            dedcumLoantable.setWidthPercentage(100);

                            if (k <= 2) {
                                if (k == 0) {
                                    dedcumLoanCell = new PdfPCell(new Phrase(StringUtils.repeat("-", 107), boldTextFont));
                                    dedcumLoanCell.setColspan(4);
                                    dedcumLoanCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                                    dedcumLoantable.addCell(dedcumLoanCell);

                                } else if (k == 1) {

                                    dedcumLoanCell = new PdfPCell(new Phrase("Description", boldTextFont));
                                    dedcumLoanCell.setBorder(Rectangle.LEFT);
                                    dedcumLoanCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    dedcumLoantable.addCell(dedcumLoanCell);

                                    dedcumLoanCell = new PdfPCell(new Phrase("Amount", boldTextFont));
                                    dedcumLoanCell.setBorder(Rectangle.NO_BORDER);
                                    dedcumLoanCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    dedcumLoantable.addCell(dedcumLoanCell);

                                    dedcumLoanCell = new PdfPCell(new Phrase("Instl.", boldTextFont));
                                    dedcumLoanCell.setBorder(Rectangle.NO_BORDER);
                                    dedcumLoanCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    dedcumLoantable.addCell(dedcumLoanCell);

                                    dedcumLoanCell = new PdfPCell(new Phrase("Balance", boldTextFont));
                                    dedcumLoanCell.setBorder(Rectangle.RIGHT);
                                    dedcumLoanCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    dedcumLoantable.addCell(dedcumLoanCell);

                                } else if (k == 2) {
                                    dedcumLoanCell = new PdfPCell(new Phrase(StringUtils.repeat("-", 107), boldTextFont));
                                    dedcumLoanCell.setColspan(4);
                                    dedcumLoanCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                                    dedcumLoantable.addCell(dedcumLoanCell);
                                }
                            } else {
                                if (loanRs.next()) {
                                    loanAbr = loanRs.getString("LOAN_ABBR");
                                    deduction = deduction + loanRs.getInt("AD_AMT");
                                    int balanceAmt = 0;
                                    if (loanRs.getString("NOW_DEDN").equals("P")) {
                                        balanceAmt = loanRs.getInt("P_ORG_AMT") - loanRs.getInt("TOT_REC_AMT");
                                        loanAbr = loanAbr + "(P)";
                                    } else if (loanRs.getString("NOW_DEDN").equals("I")) {
                                        balanceAmt = loanRs.getInt("I_ORG_AMT") - loanRs.getInt("TOT_REC_AMT");
                                        loanAbr = loanAbr + "(I)";
                                    }
                                    dedcumLoanCell = new PdfPCell(new Phrase(StringUtils.defaultString(loanAbr), bigTextFont));
                                    dedcumLoanCell.setBorder(Rectangle.LEFT);
                                    dedcumLoanCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    dedcumLoantable.addCell(dedcumLoanCell);

                                    dedcumLoanCell = new PdfPCell(new Phrase(StringUtils.defaultString(loanRs.getString("AD_AMT")), boldTextFont));
                                    dedcumLoanCell.setBorder(Rectangle.NO_BORDER);
                                    dedcumLoanCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    dedcumLoantable.addCell(dedcumLoanCell);

                                    dedcumLoanCell = new PdfPCell(new Phrase(StringUtils.defaultString(loanRs.getString("REF_DESC")), boldTextFont));
                                    dedcumLoanCell.setBorder(Rectangle.NO_BORDER);
                                    dedcumLoanCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    dedcumLoantable.addCell(dedcumLoanCell);

                                    dedcumLoanCell = new PdfPCell(new Phrase(balanceAmt + "", boldTextFont));
                                    dedcumLoanCell.setBorder(Rectangle.RIGHT);
                                    dedcumLoanCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    dedcumLoantable.addCell(dedcumLoanCell);
                                }
                            }
                        }

                        cell = new PdfPCell(dedcumLoantable);
                        cell.setColspan(3);
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);

                        //Private Deductions
                        pvtdedtable = new PdfPTable(2);
                        pvtdedtable.setWidths(new float[]{0.8f, 0.5f});
                        pvtdedtable.setWidthPercentage(100);

                        String pvtdedAbr = "";
                        String pvtdedAmt = "";
                        if (pvtDedRs.next()) {
                            if (pvtDedRs.getString("AD_ABBR") != null && !pvtDedRs.getString("AD_ABBR").equals("")) {
                                pvtdedAbr = pvtDedRs.getString("AD_ABBR");
                            } else {
                                if (pvtDedRs.getString("PVT_DESC") != null && !pvtDedRs.getString("PVT_DESC").equals("")) {
                                    pvtdedAbr = pvtDedRs.getString("PVT_DESC").toUpperCase();
                                }
                            }
                            pvtdedAmt = pvtDedRs.getInt("AD_AMT") + "";
                            pvtded = pvtded + pvtDedRs.getInt("AD_AMT");

                            pvtdedcell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 3) + StringUtils.defaultString(pvtdedAbr), bigTextFont));
                            pvtdedcell.setBorder(Rectangle.NO_BORDER);
                            pvtdedtable.addCell(pvtdedcell);
                            pvtdedcell = new PdfPCell(new Phrase(StringUtils.defaultString(pvtdedAmt), boldTextFont));
                            pvtdedcell.setBorder(Rectangle.NO_BORDER);
                            pvtdedcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            pvtdedtable.addCell(pvtdedcell);
                        }

                        cell = new PdfPCell(pvtdedtable);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                    }

                    int netpay = grosspay - deduction;
                    int netbal = netpay - pvtded;
                    if (netpay < 0) {
                        netpay = Math.abs(netpay);
                    }
                    cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 193), textFont));
                    cell.setColspan(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    allowancetable = new PdfPTable(2);
                    allowancetable.setWidths(new float[]{0.5f, 0.5f});
                    allowancetable.setWidthPercentage(100);

                    allowanceCell = null;

                    allowanceCell = new PdfPCell(new Phrase("Gross Pay : ", boldTextFont));
                    allowanceCell.setBorder(Rectangle.NO_BORDER);
                    allowanceCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    allowancetable.addCell(allowanceCell);

                    allowanceCell = new PdfPCell(new Phrase(grosspay + "", textFont));
                    allowanceCell.setBorder(Rectangle.NO_BORDER);
                    allowanceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    allowancetable.addCell(allowanceCell);

                    cell = new PdfPCell(allowancetable);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    dedcumLoantable = new PdfPTable(2);
                    dedcumLoantable.setWidths(new float[]{0.3f, 0.3f});
                    dedcumLoantable.setWidthPercentage(100);

                    dedcumLoanCell = new PdfPCell(new Phrase("Total Dedn.", boldTextFont));
                    dedcumLoanCell.setBorder(Rectangle.NO_BORDER);
                    dedcumLoanCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    dedcumLoantable.addCell(dedcumLoanCell);
                    dedcumLoanCell = new PdfPCell(new Phrase(": " + deduction, boldTextFont));
                    dedcumLoanCell.setBorder(Rectangle.NO_BORDER);
                    dedcumLoanCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    dedcumLoantable.addCell(dedcumLoanCell);

                    cell = new PdfPCell(dedcumLoantable);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    loantable = new PdfPTable(4);
                    loantable.setWidths(new float[]{0.4f, 0.4f, 0.4f, 0.4f});
                    loantable.setWidthPercentage(100);

                    loanCell = null;

                    loanCell = new PdfPCell(new Phrase("Net Pay", boldTextFont));
                    loanCell.setBorder(Rectangle.NO_BORDER);
                    dedcumLoanCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    loantable.addCell(loanCell);
                    loanCell = new PdfPCell(new Phrase(":" + netpay, boldTextFont));
                    loanCell.setBorder(Rectangle.NO_BORDER);
                    loanCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    loantable.addCell(loanCell);
                    loanCell = new PdfPCell(new Phrase("Pvt Dedn.", boldTextFont));
                    loanCell.setBorder(Rectangle.NO_BORDER);
                    dedcumLoanCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    loantable.addCell(loanCell);
                    loanCell = new PdfPCell(new Phrase(":" + pvtded, boldTextFont));
                    loanCell.setBorder(Rectangle.NO_BORDER);
                    loanCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    loantable.addCell(loanCell);

                    cell = new PdfPCell(loantable);
                    cell.setColspan(3);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("(Rupees " + StringUtils.upperCase(Numtowordconvertion.convertNumber(netbal) + " ) Only"), boldTextFont));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Net Bal:" + StringUtils.repeat(" ", 15) + netbal, boldTextFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 183), boldTextFont));
                    cell.setColspan(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setColspan(5);
                    cell.setFixedHeight(10);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setColspan(2);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Authorised Signatory/Bill Assistant", boldTextFont));
                    cell.setColspan(3);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("N.B YOU MAY ALSO VIEW THIS PAYSLIP BY USING HRMS ODISHA USING THE GIVEN URL(http://hrmsorissa.gov.in)", boldTextFont1));
                    cell.setColspan(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    slno++;
                    if (slno % 2 == 0) {
                        document.add(table);
                        document.newPage();

                        table = new PdfPTable(5);
                        table.setWidths(new float[]{1, 2, 1, 0.7f, 1.2f});
                        table.setWidthPercentage(100);
                    }

                    if (slno % 2 != 0) {
                        cell = new PdfPCell(new Phrase("Cut Here" + StringUtils.repeat("-", 70) + "Cut Here" + StringUtils.repeat("-", 70) + "Cut Here", boldTextFont));
                        cell.setColspan(5);
                        cell.setFixedHeight(30);
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table.addCell(cell);
                    }

                    document.add(table);
                }
            }

            if (slno == 0) {

                cell = new PdfPCell(new Phrase(" There is no record ", bigTextFont));
                cell.setBorder(Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(5);
                table.addCell(cell);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(allowanceRs, pstmtA);
            DataBaseFunctions.closeSqlObjects(deductionRs, pstmtD);
            DataBaseFunctions.closeSqlObjects(loanRs, pstmtL);
            DataBaseFunctions.closeSqlObjects(pvtDedRs, pstmtPD);
            DataBaseFunctions.closeSqlObjects(con);
            document.close();
        }
    }

    @Override
    public List getBillWiseEmployeeList(String billNo, int year, int month) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List emplist = new ArrayList();
        PaySlipListBean list = null;

        try {
            con = this.dataSource.getConnection();
            String sql = "SELECT da_rate,BRASS_NO,BILL_NO,AQSL_NO,AQ_MAST.EMP_CODE,AQ_MAST.EMP_NAME,CUR_DESG,AQ_MAST.PAY_SCALE,"
                    + " AQ_MAST.BANK_ACC_NO,CUR_BASIC,SPC_ORD_NO,SPC_ORD_DATE,GPF_ACC_NO,ID_NO,PAY_DAY,AQ_MAST.pay_commission FROM (SELECT * FROM AQ_MAST WHERE"
                    + " BILL_NO=? AND AQ_YEAR=? AND AQ_MONTH=? order by POST_SL_NO) AQ_MAST"
                    + " INNER JOIN EMP_MAST ON AQ_MAST.EMP_CODE = EMP_MAST.EMP_ID LEFT OUTER JOIN (SELECT * FROM EMP_ID_DOC WHERE"
                    + " ID_DESCRIPTION='PAN')EMP_ID_DOC ON AQ_MAST.EMP_CODE = EMP_ID_DOC.EMP_ID order by POST_SL_NO";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billNo));
            pst.setInt(2, year);
            pst.setInt(3, month);
            rs = pst.executeQuery();
            while (rs.next()) {
                list = new PaySlipListBean();
                list.setEmpId(rs.getString("EMP_CODE"));
                list.setEmpName(rs.getString("EMP_NAME"));
                list.setBasic(rs.getString("CUR_BASIC"));
                list.setCurDesg(rs.getString("CUR_DESG"));
                list.setGpfNo(rs.getString("GPF_ACC_NO"));
                list.setPanNo(rs.getString("ID_NO"));
                //list.setPayScale(rs.getString("PAY_SCALE"));
                list.setBankaccno(rs.getString("BANK_ACC_NO"));
                list.setBrassNo(rs.getString("BRASS_NO"));
                list.setPayDay(rs.getInt("PAY_DAY"));
                list.setAqslno(rs.getString("AQSL_NO"));
                list.setPayCommission(rs.getString("pay_commission"));
                if (rs.getString("pay_commission") != null && !rs.getString("pay_commission").equals("")) {
                    if (rs.getInt("pay_commission") == 6) {
                        list.setPayScale(rs.getString("PAY_SCALE"));
                    } else if (rs.getInt("pay_commission") == 7) {
                        if (rs.getString("PAY_SCALE") != null && !rs.getString("PAY_SCALE").equals("")) {
                            /*String[] temppayscale = rs.getString("PAY_SCALE").split("-");
                             list.setPayScale("Level-" + temppayscale[0] + " and Cell-" + temppayscale[1]);*/
                            list.setPayScale(rs.getString("PAY_SCALE"));
                        }
                    }
                } else {
                    list.setPayScale("");
                }
                list.setDaformula(rs.getString("da_rate"));

                emplist.add(list);
            }
        } catch (Exception e) {
            e.printStackTrace();;
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public PaySlipDetailBean billAmt(String billNo, int year, int month) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        double grossPay = 0;
        double deduction = 0;
        double pvtdeduction = 0;

        PaySlipDetailBean billAmtDetails = new PaySlipDetailBean();
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT VCH_NO,VCH_DATE FROM BILL_MAST WHERE BILL_NO=? AND AQ_YEAR=? AND AQ_MONTH=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billNo));
            pst.setInt(2, year);
            pst.setInt(3, month);
            rs = pst.executeQuery();
            if (rs.next()) {
                billAmtDetails.setVchno(rs.getString("VCH_NO"));
                billAmtDetails.setVchdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("VCH_DATE")));
            }

            sql = "Select sum(CUR_BASIC) AD_AMT from AQ_MAST WHERE BILL_NO=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billNo));
            rs = pst.executeQuery();
            if (rs.next()) {
                grossPay = rs.getDouble("AD_AMT");
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            sql = "SELECT sum(AQ_DTLS.AD_AMT) AD_AMT from((Select AQ_MAST.AQSL_NO FROM AQ_MAST where AQ_MAST.BILL_NO = ? AND AQ_YEAR=? AND AQ_MONTH=?)AQ_MAST inner join "
                    + " (SELECT AQ_DTLS.AQSL_NO,AQ_DTLS.AD_AMT from AQ_DTLS where AQ_DTLS.AD_TYPE = 'A') AQ_DTLS ON AQ_MAST.AQSL_NO = AQ_DTLS.AQSL_NO)";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billNo));
            pst.setInt(2, year);
            pst.setInt(3, month);
            rs = pst.executeQuery();
            if (rs.next()) {
                grossPay = grossPay + rs.getDouble("AD_AMT");
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            sql = "SELECT sum(AQ_DTLS.AD_AMT) AD_AMT from((Select AQ_MAST.AQSL_NO FROM AQ_MAST where AQ_MAST.BILL_NO = ? AND AQ_YEAR=? AND AQ_MONTH=?)AQ_MAST inner join "
                    + " (SELECT AQ_DTLS.AQSL_NO,AQ_DTLS.AD_AMT from AQ_DTLS where AQ_DTLS.AD_TYPE = 'D' AND SCHEDULE != 'PVTL' and SCHEDULE != 'PVTD' AND AD_AMT>0) AQ_DTLS ON AQ_MAST.AQSL_NO = AQ_DTLS.AQSL_NO) ";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billNo));
            pst.setInt(2, year);
            pst.setInt(3, month);
            rs = pst.executeQuery();
            if (rs.next()) {
                deduction = rs.getDouble("AD_AMT");
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            sql = "SELECT sum(AQ_DTLS.AD_AMT) AD_AMT from( (Select AQ_MAST.AQSL_NO FROM AQ_MAST where AQ_MAST.BILL_NO = ? AND AQ_YEAR=? AND AQ_MONTH=?)AQ_MAST inner join "
                    + " (SELECT AQ_DTLS.AQSL_NO,AQ_DTLS.AD_AMT from AQ_DTLS where AQ_DTLS.AD_TYPE = 'D' AND (SCHEDULE = 'PVTL' OR SCHEDULE = 'PVTD')) AQ_DTLS ON AQ_MAST.AQSL_NO = AQ_DTLS.AQSL_NO) ";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billNo));
            pst.setInt(2, year);
            pst.setInt(3, month);
            rs = pst.executeQuery();
            if (rs.next()) {
                pvtdeduction = rs.getDouble("AD_AMT");
            }
            billAmtDetails.setBillGrossAmt(grossPay);
            billAmtDetails.setBillDeductionAmt(deduction);//Govt Deductions
            billAmtDetails.setBillPrivateDeductionAmt(pvtdeduction);//Private Loan and Deductions
            billAmtDetails.setBillNetAmt(grossPay - deduction);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billAmtDetails;
    }

    public static void generateQRcode(String data, String path, String charset, Map map, int h, int w) throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, w, h);
        MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File(path));
    }

    public void generateQRCode(String empid, String empname, String gross, String net, String qrCodePath, String aqyear, String aqmonth) {

        try {
            String str = "HRMS Id =" + empid + ",Employee Name=" + empname + ",Gross Pay=" + gross + " ,Net Pay = " + net;

            //data that we want to store in the QR code  
            String storedpath = qrCodePath + empid + CommonFunctions.getResourcePath();
            //String storedpath = qrCodePath + str2;
            String path = storedpath + aqyear + "-" + aqmonth + ".png";
            //String path = "D:\\Propertystatement\\" + str2 + "-" +  "(" +str1 + "-" + str7 + ")"  + ".png";
            //Encoding charset to be used  
            String charset = "UTF-8";
            Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            //generates QR code with Low level(L) error correction capability  
            hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            //invoking the user-defined method that creates the QR code  
            generateQRcode(str, path, charset, hashMap, 200, 200);//increase or decrease height and width accodingly   
            //prints if the QR code is generated   

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Image generateQRCodeForPaySlip(Document document, String empid, String empname, String gross, String net) {
        Image qrImage = null;
        try {
            String str = "HRMS Id =" + empid + ",Employee Name=" + empname + ",Gross Pay=" + gross + " ,Net Pay = " + net;
            BarcodeQRCode qrcode = new BarcodeQRCode(str, 1, 1, null);
            qrImage = qrcode.getImage();
            //document.add(qrImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
       return qrImage; 
    }

    @Override
    public List getPaySlipForeignBody(String empid, int year, int month, int billNo) {

        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;
        PaySlipListBean pbean = null;

        double basic = 0.0;
        double totallow = 0.0;
        double grosspay = 0.0;
        double totdeduct = 0.0;
        double netpay = 0.0;

        ArrayList list = new ArrayList();
        try {
            con = dataSource.getConnection();

            String aqdtlsTbl = AqFunctionalities.getAQBillDtlsTable(month, year);

            stmt = con.createStatement();
            String sql = "select * from ("
                    + " SELECT AQSL_NO,AQ_MONTH,AQ_YEAR,CUR_BASIC,TOTALALLOWANCE,TOTALDEDUCTION FROM ("
                    + " select AQSL_NO,aq_mast.AQ_MONTH,aq_mast.AQ_YEAR,CUR_BASIC,TOTALALLOWANCE,TOTALDEDUCTION FROM ("
                    + " select BILL_NO,AQSL_NO,AQ_MONTH,AQ_YEAR,CUR_BASIC,"
                    + " GETADTOTAL_NEW(AQSL_NO,'A','" + aqdtlsTbl + "','" + year + "','" + month + "')TOTALALLOWANCE,GETADTOTAL_NEW(AQSL_NO,'D','" + aqdtlsTbl + "','" + year + "','" + month + "')TOTALDEDUCTION from AQ_MAST where"
                    + " AQ_MAST.EMP_CODE='" + empid + "' AND AQ_YEAR=" + year + " AND AQ_MONTH=" + month + ")AQ_MAST"
                    + " inner join"
                    + " bill_mast on aq_mast.bill_no=bill_mast.bill_no where bill_status_id >= 5 and AQ_MAST.bill_no='" + billNo + "' ORDER BY AQ_YEAR DESC, AQ_MONTH DESC)tab)tab2";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                pbean = new PaySlipListBean();
                pbean.setEmpId(empid);
                pbean.setAqslno(rs.getString("AQSL_NO"));
                pbean.setMonth(CalendarCommonMethods.getFullNameMonthAsString(rs.getInt("AQ_MONTH")));
                pbean.setYear(rs.getString("AQ_YEAR"));
                pbean.setMonth_year(pbean.getMonth() + "-" + pbean.getYear());
                pbean.setBasic(rs.getString("CUR_BASIC"));
                pbean.setTotallowance(rs.getString("TOTALALLOWANCE"));
                pbean.setTotdeduction(rs.getString("TOTALDEDUCTION"));

                basic = rs.getDouble("CUR_BASIC");
                totallow = rs.getDouble("TOTALALLOWANCE");
                grosspay = basic + totallow;
                pbean.setGross(grosspay + "");

                totdeduct = rs.getDouble("TOTALDEDUCTION");
                netpay = grosspay - totdeduct;
                pbean.setNetpay(netpay + "");

                list.add(pbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }
    
    @Override
    public PaySlipDetailBean getContributionDetails(String empid, int year, int month) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PaySlipDetailBean pbean = new PaySlipDetailBean();
        String deputationSpc = "";
        String foreignBody = "";
        int totalContribution = 0;
        try {
            con = dataSource.getConnection();

            String deputationSpcQuery = "SELECT deputation_spc, deputation_offcode, is_foreign_body FROM "
                    + "emp_Mast INNER JOIN g_office ON emp_Mast.deputation_offcode = g_office.off_code "
                    + "WHERE emp_id=? AND dep_code='07'";
            pstmt = con.prepareStatement(deputationSpcQuery);
            pstmt.setString(1, empid);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                deputationSpc = rs.getString("deputation_spc");
                foreignBody = rs.getString("is_foreign_body");
            }

            if ((deputationSpc != null && !deputationSpc.equals("")) && foreignBody.equals("Y")) {

                String contributionData = "SELECT aq_year, aq_month, if_gpf_assumed, cur_basic, mon_basic, gross_amt, ded_amt, "
                        + "pvt_ded_amt, aqmast.acct_type, aqmast.gpf_type aqmast_gpftype, gpf_acc_no, g_gpf_type.gpf_type g_gpf_type "
                        + "FROM (SELECT * FROM aq_mast WHERE emp_code=? AND aq_month=? AND aq_year=? and cur_spc=?) aqmast "
                        + "INNER JOIN EMP_MAST ON aqmast.emp_code = emp_mast.emp_id "
                        + "LEFT OUTER JOIN g_gpf_type ON aqmast.gpf_type = g_gpf_type.gpf_type";
                pstmt = con.prepareStatement(contributionData);
                pstmt.setString(1, empid);
                pstmt.setInt(2, month);
                pstmt.setInt(3, year);
                pstmt.setString(4, deputationSpc);
                rs = pstmt.executeQuery();

                if (rs.next()) {

                    pbean.setAccType(rs.getString("acct_type"));
                    pbean.setAqgpfType(rs.getString("aqmast_gpftype"));
                    pbean.setGpfAssumed(rs.getString("if_gpf_assumed"));
                    pbean.setGgpfType(rs.getString("g_gpf_type"));
                    pbean.setMonth(CalendarCommonMethods.getFullNameMonthAsString(rs.getInt("AQ_MONTH")));
                    pbean.setYear(rs.getString("AQ_YEAR"));
                    pbean.setMonth_year(pbean.getMonth() + "-" + pbean.getYear());
                    pbean.setCurBasic(rs.getString("cur_basic"));
                    double curBasic = rs.getDouble("cur_basic");

                    if ("GPF".equals(pbean.getAccType()) && pbean.getAqgpfType().equals(pbean.getGgpfType())) {
                        int pensionContribution = (int) (0.4 * curBasic);
                        totalContribution = totalContribution + pensionContribution;
                        pbean.setTotalPensionContribution(pensionContribution);

                    }

                    if ("PRAN".equals(pbean.getAccType()) && "N".equals(pbean.getGpfAssumed())) {
                        int employeeContribution = (int) (0.10 * curBasic);
                        int govtContribution = (int) (0.14 * curBasic);
                        int totalPensionContribution = employeeContribution + govtContribution;
                        pbean.setEmployeeContribution(employeeContribution);
                        pbean.setGovtContribution(govtContribution);
                        pbean.setTotalPensionContribution(totalPensionContribution);
                    }

                    int leaveContribution = (int) (0.11 * curBasic);
                    totalContribution = totalContribution + leaveContribution;
                    pbean.setLeaveContribution(leaveContribution);
                    pbean.setTotalContribution(totalContribution+"");

                }
            } else {
                System.out.println("No contribution data found for the specified criteria.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pbean;
    }

}
