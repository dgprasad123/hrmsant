package hrms.dao.tpfreport;

import hrms.SelectOption;
import hrms.common.AqFunctionalities;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.tpfreport.TPFReportBean;
import hrms.model.tpfreport.TPFReportList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

public class TPFReportsDAOImpl implements TPFReportsDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getTPFEmployeeList(int year, int month, String billNo) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        TPFReportBean tbean = null;

        List li = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT A.bill_no,B.aqsl_no,B.gpf_acc_no,B.emp_name,C.ad_amt FROM bill_mast A"
                    + " INNER JOIN aq_mast B ON A.bill_no=B.bill_no"
                    + " INNER JOIN aq_dtls C ON B.aqsl_no=C.aqsl_no WHERE A.bill_no=? and A.aq_month=? and A.aq_year=? and B.acct_type='TPF' and (C.ad_code='TPF' OR C.ad_code='TPFGA') and C.ad_amt > 0";
            pst = con.prepareStatement(sql);
            pst.setString(1, billNo);
            pst.setInt(2, month);
            pst.setInt(3, year);
            rs = pst.executeQuery();
            while (rs.next()) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getDDOList(String parentTrCode, String subTrCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SelectOption so = null;

        List li = new ArrayList();
        String sql = "";
        try {
            con = this.dataSource.getConnection();

            //String sql = "SELECT OFF_CODE,DDO_CODE,OFF_EN FROM G_OFFICE WHERE IS_DDO='Y' AND TR_CODE=? AND OFF_CODE LIKE '%EDN%' ORDER BY OFF_EN ASC";
            if (subTrCode != null && !subTrCode.equals("")) {
                sql = "SELECT G_OFFICE.OFF_CODE,DDO_CODE,OFF_EN FROM G_OFFICE"
                        + " INNER JOIN G_TREASURY ON G_OFFICE.TR_CODE=G_TREASURY.TR_CODE WHERE IS_DDO='Y' AND G_TREASURY.TR_CODE=? AND G_OFFICE.OFF_CODE LIKE '%EDN%' ORDER BY OFF_EN ASC";
                pst = con.prepareStatement(sql);
                pst.setString(1, subTrCode);
            } else {
                String parentTrCodeTemp = parentTrCode.substring(0, 2);
                sql = "SELECT G_OFFICE.OFF_CODE,DDO_CODE,OFF_EN FROM G_OFFICE"
                        + " INNER JOIN G_TREASURY ON G_OFFICE.TR_CODE=G_TREASURY.TR_CODE WHERE IS_DDO='Y' AND G_OFFICE.TR_CODE LIKE ? AND G_OFFICE.OFF_CODE LIKE '%EDN%' ORDER BY OFF_EN ASC";
                pst = con.prepareStatement(sql);
                pst.setString(1, parentTrCodeTemp + "%");
            }

            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("OFF_CODE"));
                so.setLabel(rs.getString("OFF_EN") + "(" + rs.getString("DDO_CODE") + ")");
                li.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getBillAmt(String ddocode, int month, int year) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SelectOption so = null;

        List li = new ArrayList();

        String challanNoDate = "";
        try {
            con = this.dataSource.getConnection();
            
            /*String sql = "select bill_no, SUM(ad_amt) AS total_deduction from aq_mast AM INNER JOIN aq_dtls AD ON AM.aqsl_no = AD.aqsl_no" +
             " where off_code=? and AM.aq_year=? and AM.aq_month=? and acct_type=? and (AD.schedule=? OR AD.schedule=? or AD.ad_code = ? or AD.ad_code = ?)" +
             " group by bill_no";*/
            /*String sql = "select bill_mast.bill_no, total_deduction,vch_no,vch_date,challan_no,challan_date from("
             + " select bill_no, SUM(ad_amt) AS total_deduction from aq_mast AM INNER JOIN aq_dtls AD ON AM.aqsl_no = AD.aqsl_no"
             + " where off_code=? and AM.aq_year=? and AM.aq_month=? and acct_type=? and (AD.schedule=? OR AD.schedule=? or AD.ad_code = ? or AD.ad_code = ?)"
             + " group by bill_no)temp"
             + " inner join bill_mast on temp.bill_no=bill_mast.bill_no";*/
            String sql = "select bill_mast.bill_no, total_deduction,vch_no,vch_date,challan_no,challan_date from("
                    + " select bill_no, SUM(ad_amt) AS total_deduction from aq_mast AM INNER JOIN aq_dtls AD ON AM.aqsl_no = AD.aqsl_no"
                    + " where off_code=? and acct_type=? and (AD.schedule=? OR AD.schedule=? or AD.ad_code = ? or AD.ad_code = ?)"
                    + " group by bill_no)temp"
                    + " inner join bill_mast on temp.bill_no=bill_mast.bill_no where extract (year from challan_date)=? AND extract (month from challan_date)=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, ddocode);
            pst.setString(2, "TPF");
            pst.setString(3, "TPF");
            pst.setString(4, "TPFGA");
            pst.setString(5, "GPDD");
            pst.setString(6, "GPIR");
            pst.setInt(7, year);
            pst.setInt(8, (month + 1));
            rs = pst.executeQuery();
            while (rs.next()) {
                String vchNo1 = "";
                String vchDate = "";

                so = new SelectOption();
                so.setValue(rs.getString("bill_no"));

                if (rs.getString("vch_no") != null && !rs.getString("vch_no").equals("")) {

                    String vchNo = rs.getString("vch_no");
                    if (vchNo.length() > 4) {
                        vchNo1 = vchNo.substring(4);
                    }

                    if (rs.getString("vch_date") != null && !rs.getString("vch_date").equals("")) {
                        vchDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("vch_date"));
                    }
                }
                if (rs.getString("challan_no") != null && !rs.getString("challan_no").equals("")) {
                    challanNoDate = rs.getString("challan_no");
                    if (rs.getString("challan_date") != null && !rs.getString("challan_date").equals("")) {
                        challanNoDate = challanNoDate + "/" + CommonFunctions.getFormattedOutputDate1(rs.getDate("challan_date"));
                    }
                }
                if (challanNoDate != null && !challanNoDate.equals("")) {
                    so.setLabel(rs.getString("total_deduction") + " (" + vchNo1 + "/" + vchDate + ") and {" + challanNoDate + "}");
                } else {
                    so.setLabel(rs.getString("total_deduction") + " (" + vchNo1 + "/" + vchDate + ")");
                }

                li.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getYearList() {

        SelectOption so = null;
        List li = new ArrayList();
        try {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            for (int i = 5; i > 0; i--) {
                so = new SelectOption();
                so.setValue(year + "");
                so.setLabel(year + "");
                li.add(so);
                year = year - 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return li;
    }

    @Override
    public List getPaymentList(String billNo) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List li = new ArrayList();

        TPFReportList tlist = null;
        int i = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select emp_name,cur_desg,mon_basic,gpf_type,gpf_acc_no,p_org_amt,ORDNO,ORDDT from (select emp_code,emp_name,cur_desg,mon_basic,gpf_type,gpf_acc_no from aq_mast where bill_no=?)aq_mast"
                    + " left outer join (select emp_id,not_id,p_org_amt from emp_loan_sanc where loan_tp=?)emp_loan_sanc on aq_mast.emp_code=emp_loan_sanc.emp_id"
                    + " inner join emp_notification on emp_loan_sanc.not_id=emp_notification.not_id order by emp_name asc";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billNo));
            pst.setString(2, "TPF");
            rs = pst.executeQuery();
            while (rs.next()) {
                i = i + 1;
                tlist = new TPFReportList();
                tlist.setSlno(i);
                tlist.setEmpName(rs.getString("emp_name"));
                tlist.setEmpDesg(rs.getString("cur_desg"));
                tlist.setBasic(rs.getString("mon_basic"));
                tlist.setGpfseries(rs.getString("gpf_type"));
                tlist.setGpfNo(rs.getString("gpf_acc_no"));
                tlist.setOrdNo(rs.getString("ORDNO"));
                tlist.setOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                tlist.setAmount(rs.getInt("p_org_amt"));
                li.add(tlist);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public TPFReportBean getBillDtls(String billNo) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        TPFReportBean tbean = new TPFReportBean();
        try {
            con = this.dataSource.getConnection();

            String sql = "select department_name,g_office.ddo_code,token_no,token_date,vch_no,vch_date,bill_mast.bill_date,aq_mast.off_code,bill_desc from aq_mast"
                    + " inner join bill_mast on aq_mast.bill_no=bill_mast.bill_no"
                    + " inner join g_office on aq_mast.off_code=g_office.off_code"
                    + " inner join g_department on g_office.department_code=g_department.department_code where aq_mast.bill_no=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billNo));
            rs = pst.executeQuery();
            if (rs.next()) {
                tbean.setDeptName(rs.getString("department_name"));
                tbean.setDdoCode(rs.getString("ddo_code"));
                tbean.setTokenNo(rs.getString("token_no"));
                tbean.setTokenDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("token_no")));
                tbean.setBillDesc(rs.getString("bill_desc"));
                tbean.setVoucherNo(rs.getString("vch_no"));
                tbean.setVoucherDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("vch_date")));
                tbean.setBillDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("bill_date")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return tbean;
    }

    @Override
    public ArrayList getTPFListFromChallanNo(String challanNo, String challanDate, String parentTRCode, String subTRCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement innerpst = null;
        ResultSet innerrs = null;

        Statement stmt1 = null;
        Statement stmt2 = null;
        Statement stmt3 = null;

        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;

        TPFReportBean tpfbean = null;

        ArrayList arList = new ArrayList();

        int billNo = 0;
        String typeOfBill = "";
        int aqMonth = 0;
        int aqYear = 0;
        try {
            con = this.dataSource.getConnection();

            challanNo = "8009" + challanNo;

            if (subTRCode != null && !subTRCode.equals("")) {
                pst = con.prepareStatement("select bill_no,type_of_bill,aq_year,aq_month from bill_mast where CHALLAN_NO=? AND challan_date=? and BILL_MAST.TR_CODE = ?");
                pst.setString(1, challanNo);
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(challanDate).getTime()));
                pst.setString(3, subTRCode);
                rs = pst.executeQuery();
                if (rs.next()) {
                    billNo = rs.getInt("bill_no");
                    typeOfBill = rs.getString("type_of_bill");
                    aqMonth = rs.getInt("aq_month");
                    aqYear = rs.getInt("aq_year");

                    if (typeOfBill != null && !typeOfBill.equals("")) {
                        if (typeOfBill.contains("ARREAR")) {
                            innerpst = con.prepareStatement("select GPF_ACC_NO,EMP_MAST.DOE_GOV,EMP_MAST.DOB,EMP_MAST.DOS,"
                                    + " AQ_MAST.emp_id,AQ_MAST.OFF_CODE,AQ_MAST.EMP_NAME, AQ_MAST.CUR_DESG,AQ_MAST.BANK_ACC_NO,AQ_MAST.CUR_BASIC,"
                                    + " AQ_MAST.AQSL_NO,full_arrear_pay,arrear_pay,cpf_head from arr_mast AQ_MAST"
                                    + " left outer join"
                                    + " EMP_MAST on AQ_MAST.emp_id = EMP_MAST.EMP_ID"
                                    + " where bill_no=? and cpf_head > 0 ORDER BY GPF_TYPE,GPF_ACC_NO");
                            innerpst.setInt(1, billNo);
                            innerrs = innerpst.executeQuery();
                            while (innerrs.next()) {
                                tpfbean = new TPFReportBean();
                                tpfbean.setDdoCode(innerrs.getString("off_code"));
                                tpfbean.setEmpname(innerrs.getString("emp_name"));
                                tpfbean.setEmpId(innerrs.getString("emp_id"));
                                tpfbean.setCurDesg(innerrs.getString("cur_desg"));
                                tpfbean.setGpfNo(innerrs.getString("gpf_acc_no"));

                                tpfbean.setMonthlySubAmt(innerrs.getInt("cpf_head"));
                                tpfbean.setTotalReleased(innerrs.getInt("cpf_head"));

                                if (innerrs.getString("emp_id") != null && !innerrs.getString("emp_id").equals("") && tpfbean.getTotalReleased() > 0) {
                                    arList.add(tpfbean);
                                }
                            }
                        } else {
                            String aqDtlsTbl = AqFunctionalities.getAQBillDtlsTable(aqMonth,aqYear);
                            innerpst = con.prepareStatement("select GPF_ACC_NO,EMP_MAST.DOE_GOV,EMP_MAST.DOB,EMP_MAST.DOS,"
                                    + " AQ_MAST.EMP_CODE,AQ_MAST.OFF_CODE,AQ_MAST.EMP_NAME, AQ_MAST.CUR_DESG,AQ_MAST.BANK_ACC_NO,AQ_MAST.CUR_BASIC,AQ_MAST.PAY_SCALE,"
                                    + " AQ_MAST.AQSL_NO,POST_SL_NO from aq_mast"
                                    + " left outer join"
                                    + " EMP_MAST on AQ_MAST.EMP_CODE = EMP_MAST.EMP_ID"
                                    + " where bill_no=? and AQ_MAST.aq_year=? and AQ_MAST.aq_month=?");
                            innerpst.setInt(1, billNo);
                            innerpst.setInt(2, aqYear);
                            innerpst.setInt(3, aqMonth);
                            innerrs = innerpst.executeQuery();
                            while (innerrs.next()) {
                                tpfbean = new TPFReportBean();
                                tpfbean.setDdoCode(innerrs.getString("off_code"));
                                tpfbean.setEmpname(innerrs.getString("emp_name"));
                                tpfbean.setEmpId(innerrs.getString("emp_code"));
                                tpfbean.setCurDesg(innerrs.getString("cur_desg"));
                                tpfbean.setAqslno(innerrs.getString("aqsl_no"));
                                tpfbean.setGpfNo(innerrs.getString("gpf_acc_no"));
                                if (innerrs.getString("aqsl_no") != null && !innerrs.getString("aqsl_no").equals("")) {
                                    String empAqslNo = innerrs.getString("aqsl_no");
                                    stmt1 = con.createStatement();
                                    String Sql2 = "SELECT AD_AMT FROM "+aqDtlsTbl+" WHERE AD_TYPE = 'D' AND AQSL_NO= '" + empAqslNo + "'  AND  DED_TYPE = 'S' and SCHEDULE ='TPF'";
                                    rs1 = stmt1.executeQuery(Sql2);
                                    if (rs1.next()) {
                                        tpfbean.setMonthlySubAmt(rs1.getInt("AD_AMT"));
                                    }
                                    stmt2 = con.createStatement();
                                    String Sql3 = "SELECT AD_AMT REFUND_OF_WITHDRAWAL, REF_DESC FROM "+aqDtlsTbl+" WHERE AD_TYPE = 'D'  AND DED_TYPE = 'L' and SCHEDULE='TPFGA'\n"
                                            + " AND AQSL_NO = '" + empAqslNo + "'";
                                    rs2 = stmt2.executeQuery(Sql3);
                                    if (rs2.next()) {
                                        tpfbean.setTowardsLoan(rs2.getInt("REFUND_OF_WITHDRAWAL"));
                                        tpfbean.setInstCnt(StringUtils.defaultString(rs2.getString("REF_DESC")));
                                    }
                                    stmt3 = con.createStatement();
                                    String Sql4 = "SELECT SUM(AD_AMT)TOTALRELEASED FROM "+aqDtlsTbl+" WHERE AQSL_NO ='" + empAqslNo + "' AND (AD_CODE='TPF' OR ad_code='TPFGA') GROUP BY AQSL_NO";
                                    rs3 = stmt3.executeQuery(Sql4);
                                    if (rs3.next()) {
                                        tpfbean.setTotalReleased(rs3.getInt("TOTALRELEASED"));
                                    }
                                }
                                if (innerrs.getString("EMP_CODE") != null && !innerrs.getString("EMP_CODE").equals("") && tpfbean.getTotalReleased() > 0) {
                                    arList.add(tpfbean);
                                }
                            }
                        }
                    }
                }
            } else {
                String allTreasury = "";
                pst = con.prepareStatement("select tr_code from g_treasury where parent_tr_code=? or tr_code=?");
                pst.setString(1, parentTRCode);
                pst.setString(2, parentTRCode);
                rs = pst.executeQuery();
                while (rs.next()) {
                    if (allTreasury.equals("")) {
                        allTreasury = "'" + rs.getString("tr_code") + "'";
                    } else {
                        allTreasury = allTreasury + ",'" + rs.getString("tr_code") + "'";
                    }
                }
                DataBaseFunctions.closeSqlObjects(rs, pst);

                pst = con.prepareStatement("select bill_no,type_of_bill,aq_year,aq_month from bill_mast where CHALLAN_NO=? AND challan_date=? and BILL_MAST.TR_CODE IN (" + allTreasury + ")");
                pst.setString(1, challanNo);
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(challanDate).getTime()));
                rs = pst.executeQuery();
                if (rs.next()) {
                    billNo = rs.getInt("bill_no");
                    typeOfBill = rs.getString("type_of_bill");
                    aqMonth = rs.getInt("aq_month");
                    aqYear = rs.getInt("aq_year");

                    if (typeOfBill != null && !typeOfBill.equals("")) {
                        if (typeOfBill.contains("ARREAR")) {
                            innerpst = con.prepareStatement("select GPF_ACC_NO,EMP_MAST.DOE_GOV,EMP_MAST.DOB,EMP_MAST.DOS,"
                                    + " AQ_MAST.emp_id,AQ_MAST.OFF_CODE,AQ_MAST.EMP_NAME, AQ_MAST.CUR_DESG,AQ_MAST.BANK_ACC_NO,AQ_MAST.CUR_BASIC,"
                                    + " AQ_MAST.AQSL_NO,full_arrear_pay,arrear_pay,cpf_head from arr_mast AQ_MAST"
                                    + " left outer join"
                                    + " EMP_MAST on AQ_MAST.emp_id = EMP_MAST.EMP_ID"
                                    + " where bill_no=? and cpf_head > 0 ORDER BY GPF_TYPE,GPF_ACC_NO");
                            innerpst.setInt(1, billNo);
                            innerrs = innerpst.executeQuery();
                            while (innerrs.next()) {
                                tpfbean = new TPFReportBean();
                                tpfbean.setDdoCode(innerrs.getString("off_code"));
                                tpfbean.setEmpname(innerrs.getString("emp_name"));
                                tpfbean.setEmpId(innerrs.getString("emp_id"));
                                tpfbean.setCurDesg(innerrs.getString("cur_desg"));
                                tpfbean.setGpfNo(innerrs.getString("gpf_acc_no"));

                                tpfbean.setMonthlySubAmt(innerrs.getInt("cpf_head"));
                                tpfbean.setTotalReleased(innerrs.getInt("cpf_head"));

                                if (innerrs.getString("emp_id") != null && !innerrs.getString("emp_id").equals("") && tpfbean.getTotalReleased() > 0) {
                                    arList.add(tpfbean);
                                }
                            }
                        } else {
                            String aqDtlsTbl = AqFunctionalities.getAQBillDtlsTable(aqMonth,aqYear);
                            innerpst = con.prepareStatement("select GPF_ACC_NO,EMP_MAST.DOE_GOV,EMP_MAST.DOB,EMP_MAST.DOS,"
                                    + " AQ_MAST.EMP_CODE,AQ_MAST.OFF_CODE,AQ_MAST.EMP_NAME, AQ_MAST.CUR_DESG,AQ_MAST.BANK_ACC_NO,AQ_MAST.CUR_BASIC,AQ_MAST.PAY_SCALE,"
                                    + " AQ_MAST.AQSL_NO,POST_SL_NO from aq_mast"
                                    + " left outer join"
                                    + " EMP_MAST on AQ_MAST.EMP_CODE = EMP_MAST.EMP_ID"
                                    + " where bill_no=? and AQ_MAST.aq_year=? and AQ_MAST.aq_month=? ORDER BY GPF_TYPE,GPF_ACC_NO");
                            innerpst.setInt(1, billNo);
                            innerpst.setInt(2, aqYear);
                            innerpst.setInt(3, aqMonth);
                            innerrs = innerpst.executeQuery();
                            while (innerrs.next()) {
                                tpfbean = new TPFReportBean();
                                tpfbean.setDdoCode(innerrs.getString("off_code"));
                                tpfbean.setEmpname(innerrs.getString("emp_name"));
                                tpfbean.setEmpId(innerrs.getString("emp_code"));
                                tpfbean.setCurDesg(innerrs.getString("cur_desg"));
                                tpfbean.setAqslno(innerrs.getString("aqsl_no"));
                                tpfbean.setGpfNo(innerrs.getString("gpf_acc_no"));
                                if (innerrs.getString("aqsl_no") != null && !innerrs.getString("aqsl_no").equals("")) {
                                    String empAqslNo = innerrs.getString("aqsl_no");
                                    stmt1 = con.createStatement();
                                    String Sql2 = "SELECT AD_AMT FROM "+aqDtlsTbl+" WHERE AD_TYPE = 'D' AND AQSL_NO= '" + empAqslNo + "'  AND  DED_TYPE = 'S' and SCHEDULE ='TPF'";
                                    rs1 = stmt1.executeQuery(Sql2);
                                    if (rs1.next()) {
                                        tpfbean.setMonthlySubAmt(rs1.getInt("AD_AMT"));
                                    }
                                    stmt2 = con.createStatement();
                                    String Sql3 = "SELECT AD_AMT REFUND_OF_WITHDRAWAL, REF_DESC FROM "+aqDtlsTbl+" WHERE AD_TYPE = 'D'  AND DED_TYPE = 'L' and SCHEDULE='TPFGA'\n"
                                            + " AND AQSL_NO = '" + empAqslNo + "'";
                                    rs2 = stmt2.executeQuery(Sql3);
                                    if (rs2.next()) {
                                        tpfbean.setTowardsLoan(rs2.getInt("REFUND_OF_WITHDRAWAL"));
                                        tpfbean.setInstCnt(StringUtils.defaultString(rs2.getString("REF_DESC")));
                                    }
                                    stmt3 = con.createStatement();
                                    String Sql4 = "SELECT SUM(AD_AMT)TOTALRELEASED FROM "+aqDtlsTbl+" WHERE AQSL_NO ='" + empAqslNo + "' AND (AD_CODE='TPF' OR ad_code='TPFGA') GROUP BY AQSL_NO";
                                    rs3 = stmt3.executeQuery(Sql4);
                                    if (rs3.next()) {
                                        tpfbean.setTotalReleased(rs3.getInt("TOTALRELEASED"));
                                    }
                                }
                                if (innerrs.getString("EMP_CODE") != null && !innerrs.getString("EMP_CODE").equals("") && tpfbean.getTotalReleased() > 0) {
                                    arList.add(tpfbean);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arList;
    }
    
    @Override
    public String getOfficeNameFromChallanNo(String challanNo, String challanDate, String parentTrCode,String subTRCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String offname = "";
        
        try {
            con = this.dataSource.getConnection();
            
            challanNo = "8009" + challanNo;
            
            if (subTRCode != null && !subTRCode.equals("")) {
                String sql = "select off_en from bill_mast"
                        + " inner join g_office on bill_mast.off_code=g_office.off_code"
                        + " where challan_no=? and challan_date=? and BILL_MAST.TR_CODE = ?";
                pst = con.prepareStatement(sql);
                pst.setString(1, challanNo);
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(challanDate).getTime()));
                pst.setString(3, subTRCode);
                rs = pst.executeQuery();
                if (rs.next()) {
                    offname = rs.getString("off_en");
                }
            } else {
                String allTreasury = "";
                pst = con.prepareStatement("select tr_code from g_treasury where parent_tr_code=? or tr_code=?");
                pst.setString(1, parentTrCode);
                pst.setString(2, parentTrCode);
                rs = pst.executeQuery();
                while (rs.next()) {
                    if (allTreasury.equals("")) {
                        allTreasury = "'" + rs.getString("tr_code") + "'";
                    } else {
                        allTreasury = allTreasury + ",'" + rs.getString("tr_code") + "'";
                    }
                }
                DataBaseFunctions.closeSqlObjects(rs, pst);

                String sql = "select off_en from bill_mast"
                        + " inner join g_office on bill_mast.off_code=g_office.off_code"
                        + " where challan_no=? and challan_date=? and BILL_MAST.TR_CODE IN (" + allTreasury + ")";
                pst = con.prepareStatement(sql);
                pst.setString(1, challanNo);
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(challanDate).getTime()));
                rs = pst.executeQuery();
                if (rs.next()) {
                    offname = rs.getString("off_en");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return offname;
    }
}
