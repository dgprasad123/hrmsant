/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.schedule;

import hrms.common.CalendarCommonMethods;
import hrms.common.DataBaseFunctions;
import hrms.common.Numtowordconvertion;
import hrms.model.payroll.billbrowser.AllowDeductDetails;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommonScheduleMethods {

    public double getTotalValue(Statement st, String accNo, String empCode, String schedule) throws Exception {
        ResultSet rs = null;
        double total = 0.0;
        try {
            String strQry = "select sum(AD_AMT) AD_AMT from AQ_DTLS where ACC_NO='" + accNo + "' AND EMP_CODE='" + empCode + "' AND SCHEDULE='" + schedule + "'";
            rs = st.executeQuery(strQry);
            while (rs.next()) {
                total = rs.getDouble("AD_AMT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
        }
        return total;
    }

    public static String getNoOfInst(String aqslno, int year, int month, String aqdtlsTblName, Connection con, String advanceType) throws Exception {

        PreparedStatement pst = null;
        ResultSet rs = null;

        String inst = "";
        try {
            String sql = "SELECT REF_DESC FROM " + aqdtlsTblName + " WHERE AQSL_NO=? AND AQ_YEAR=? AND AQ_MONTH=? AND SCHEDULE=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, aqslno);
            pst.setInt(2, year);
            pst.setInt(3, month);
            pst.setString(4, advanceType);
            rs = pst.executeQuery();
            if (rs.next()) {
                inst = rs.getString("REF_DESC");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
        }
        return inst;
    }

    public static String getNoOfInst2(String aqslno, String aqdtlsTblName, Connection con) throws Exception {

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

    public String getNoOfInst1(String aqslno, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String inst = "";
        try {
            st = con.createStatement();
            String queryString = "SELECT REF_DESC FROM AQ_DTLS WHERE AQSL_NO='" + aqslno + "' AND SCHEDULE='GA'";
            rs = st.executeQuery(queryString);
            if (rs.next()) {
                inst = rs.getString("REF_DESC");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
        }
        return inst;
    }

    public String[] getGrossTotalAmount(Connection con, String billNo) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String totamt = "0";
        double totBankAmt = 0;
        String arr[] = new String[2];
        try {
            st = con.createStatement();
            String otc84Qry = "SELECT BANK_NAME from AQ_MAST  WHERE BILL_NO ='" + billNo + "' and DEFAULT_BANK = 1 OR DEFAULT_BANK = 0 GROUP BY BANK_NAME";
            rs = st.executeQuery(otc84Qry);
            while (rs.next()) {
                if (rs.getString("BANK_NAME") != null && !rs.getString("BANK_NAME").equals("")) {
                    totamt = getGrossTotal1(con, rs.getString("BANK_NAME"), billNo, true);
                } else {
                    totamt = getGrossTotal1(con, null, billNo, false);
                }
                totBankAmt = Double.parseDouble(totamt) + totBankAmt;
            }
            arr[0] = Double.toString(totBankAmt);
            arr[1] = Numtowordconvertion.convertNumber((int) totBankAmt);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
        }
        return arr;
    }

    public List getAllowanceList(Connection con, String adtype, String billno, double basicPay, String accType) {
        AllowDeductDetails allowdeduct = null;
        List al = new ArrayList();
        ResultSet rs = null;
        Statement st = null;
        boolean ispayadded = false;
        String gphead = "";

        try {
            st = con.createStatement();

            //String alowanceQry = "select a.ad_code,a.bt_id,sum(a.ad_amt) adamt from hrmis2.AQ_DTLS a,hrmis2.AQ_MAST b where a.AQSL_NO =b.AQSL_NO and a.aq_month=b.aq_month "
            //        + "and a.aq_year=b.aq_year and a.aq_month= "++" and a.aq_year= "++" and a.ad_type = '"+ adtype +"' and a.ad_amt>0 and b.BILL_NO = "+billno+" group by a.ad_code,a.bt_id";
            String alowanceQry = "SELECT AD_CODE,BT_ID,SUM(AD_AMT) AD_AMT FROM AQ_DTLS INNER JOIN  (SELECT AQ_MAST.AQSL_NO FROM AQ_MAST WHERE "
                    + "AQ_MAST.BILL_NO='" + billno + "')AQ_MAST ON AQ_DTLS.AQSL_NO = AQ_MAST.AQSL_NO WHERE AD_TYPE='" + adtype + "' AND AD_AMT>0 GROUP BY "
                    + "AQ_DTLS.AD_CODE,BT_ID,NOW_DEDN ORDER BY BT_ID";
            rs = st.executeQuery(alowanceQry);
            while (rs.next()) {
                allowdeduct = new AllowDeductDetails();

                allowdeduct.setAdname(rs.getString("AD_CODE"));
                if (accType != null && accType.equalsIgnoreCase("TPF") && allowdeduct.getAdname().equalsIgnoreCase("LIC")) {
                    allowdeduct.setObjecthead("7129");
                } else if (accType != null && accType.equalsIgnoreCase("TPF") && (allowdeduct.getAdname().equalsIgnoreCase("GPF") || allowdeduct.getAdname().equalsIgnoreCase("GA"))) {
                    allowdeduct.setObjecthead("7058");
                } else {
                    if (allowdeduct.getAdname().equalsIgnoreCase("GP")) {
                        gphead = rs.getString("BT_ID");
                    }
                    allowdeduct.setObjecthead(rs.getString("BT_ID"));
                }

                if (allowdeduct.getObjecthead() != null && !allowdeduct.getObjecthead().equals("")) {
                    if (allowdeduct.getObjecthead().equals("136")) {
                        ispayadded = true;
                        allowdeduct.setAdname("PAY + " + allowdeduct.getAdname());
                        allowdeduct.setAdamount(Double.valueOf(basicPay + rs.getDouble("AD_AMT") + "").longValue() + "");
                    } else {
                        allowdeduct.setAdamount(Double.valueOf(rs.getDouble("AD_AMT") + "").longValue() + "");
                    }
                } else {
                    allowdeduct.setAdamount(Double.valueOf(rs.getDouble("AD_AMT") + "").longValue() + "");
                }
                al.add(allowdeduct);
            }
            if (ispayadded == false) {
                allowdeduct = new AllowDeductDetails();
                allowdeduct.setAdname("PAY  ");
                allowdeduct.setObjecthead(gphead);
                allowdeduct.setAdamount(Double.valueOf(basicPay + "").longValue() + "");
                al.add(allowdeduct);
            }
            Collections.sort(al);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
        }
        return al;
    }

    public ArrayList getDeductionList(Connection con, String adtype, String billno, String accType) {

        Statement stmt = null;
        ResultSet rs = null;
        AllowDeductDetails allowdeduct = null;
        ArrayList dList = new ArrayList();
        try {
            stmt = con.createStatement();
            String deductQry = "SELECT AQ_DTLS.AD_CODE,AQ_DTLS.BT_ID,sum(AQ_DTLS.AD_AMT) AD_AMT from( (Select AQ_MAST.AQSL_NO FROM AQ_MAST where AQ_MAST.BILL_NO = '" + billno + "')AQ_MAST inner join"
                    + " (SELECT AQ_DTLS.AQSL_NO,AQ_DTLS.AD_AMT,AQ_DTLS.AD_CODE,AQ_DTLS.BT_ID from AQ_DTLS where AQ_DTLS.AD_TYPE = '" + adtype + "' AND AD_AMT>0 AND SCHEDULE != 'PVTL' and SCHEDULE != 'PVTD') AQ_DTLS ON"
                    + " AQ_MAST.AQSL_NO = AQ_DTLS.AQSL_NO) GROUP BY AQ_DTLS.AD_CODE,BT_ID ORDER BY BT_ID";
            rs = stmt.executeQuery(deductQry);
            while (rs.next()) {
                allowdeduct = new AllowDeductDetails();

                allowdeduct.setAdname(rs.getString("AD_CODE"));
                if (accType != null && accType.equalsIgnoreCase("TPF") && allowdeduct.getAdname().equalsIgnoreCase("LIC")) {
                    allowdeduct.setObjecthead("7129");
                } else if (accType != null && accType.equalsIgnoreCase("TPF") && (allowdeduct.getAdname().equalsIgnoreCase("GPF") || allowdeduct.getAdname().equalsIgnoreCase("GA"))) {
                    allowdeduct.setObjecthead("7058");
                } else {
                    allowdeduct.setObjecthead(rs.getString("BT_ID"));
                }

                allowdeduct.setAdamount(rs.getDouble("AD_AMT") + "");
                dList.add(allowdeduct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
        }
        return dList;
    }

    public String getADCodeHead(Connection con, String billNo, String ad, String aqDTLS, int aqMonth, int aqYear) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String head = "";
        try {
            st = con.createStatement();
            String sql = "select BT_ID from " + aqDTLS + " a, AQ_MAST b where a.aqsl_no = b.aqsl_no "
                    + "and b.aq_month = '" + aqMonth + "' and b.aq_year = '" + aqYear + "' and b.BILL_NO = '" + billNo + "' and a.AD_CODE = '" + ad + "' GROUP BY BT_ID";
            rs = st.executeQuery(sql);
            if (rs.next()) {
                head = rs.getString("BT_ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
        }
        return head;
    }

    public static ArrayList allowanceDeductionList(Connection con, int month, String year, String dedtype, String billNo, String aqDtls) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        OtcPlanForm40Bean otc40 = null;
        ArrayList alowanceDedList = new ArrayList();
        int gpamt = 0;
        try {
            st = con.createStatement();
            String query = "select a.AD_CODE,SUM(a.AD_AMT)AD_AMT,a.NOW_DEDN,a.BT_ID from " + aqDtls + " a, AQ_MAST b where a.aqsl_no = b.aqsl_no "
                    + "and b.aq_month = " + month + " AND b.aq_year = " + year + " AND AD_AMT>0 "
                    + "AND AD_TYPE='" + dedtype + "' AND (SCHEDULE !='PVTL' AND SCHEDULE!='PVTD') and b.bill_no = '" + billNo + "' GROUP BY AD_CODE,NOW_DEDN,BT_ID "
                    + "ORDER BY AD_CODE";
            rs = st.executeQuery(query);
            while (rs.next()) {
                if (dedtype.equals("A")) {
                    if (rs.getString("AD_CODE") != null) {
                        otc40 = new OtcPlanForm40Bean();
                        gpamt = rs.getInt("AD_AMT");
                        otc40.setAdAmt(gpamt);
                        otc40.setBtId(rs.getString("BT_ID"));
                        otc40.setNowDedn(rs.getString("NOW_DEDN"));
                        otc40.setAdCode(rs.getString("AD_CODE"));
                        alowanceDedList.add(otc40);
                    }
                }else {
                    otc40 = new OtcPlanForm40Bean();
                    otc40.setAdAmt(rs.getInt("AD_AMT"));
                    otc40.setBtId(rs.getString("BT_ID"));
                    otc40.setNowDedn(rs.getString("NOW_DEDN"));
                    otc40.setAdCode(rs.getString("AD_CODE"));
                    alowanceDedList.add(otc40);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
        }
        return alowanceDedList;
    }

    public int getAllowanceOrDeduction(Statement st, String sql) throws Exception {
        int allOrDeduc = 0;
        ResultSet rs = null;
        try {
            rs = st.executeQuery(sql);
            while (rs.next()) {
                if (rs.getString(1) != null && !rs.getString(1).equals("")) {
                    allOrDeduc = Integer.parseInt(rs.getString(1));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
        }
        return allOrDeduc;
    }

    public static String getArrInstalment(String aqslno, Connection con, String aqDtls, int aqYear, int aqMonth) throws Exception {
        ResultSet rs = null;
        Statement stmt = null;
        String arrinst = null;
        try {
            stmt = con.createStatement();
            String arrIntQry = "SELECT REF_DESC FROM "+aqDtls+" WHERE AQSL_NO='" + aqslno + "' and aq_month='" + aqMonth + "' and aq_year='" + aqYear + "'"
                    + " AND AD_CODE='NPSL'";
            rs = stmt.executeQuery(arrIntQry);
            while (rs.next()) {
                if (rs.getString("REF_DESC") != null) {
                    arrinst = rs.getString("REF_DESC");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
        }
        return arrinst;
    }

    public static String getNpsl(String aqslno, Connection con, String aqDtls, int aqYear, int aqMonth) throws Exception {
        ResultSet rs = null;
        Statement stmt = null;
        String npsl = null;
        int amt = 0;
        try {
            stmt = con.createStatement();
            String npsQry = "SELECT AD_AMT FROM "+aqDtls+" WHERE AQSL_NO='" + aqslno + "' and aq_month='" + aqMonth + "' and aq_year='" + aqYear + "'"
                    + " AND AD_CODE='NPSL' AND AD_AMT>0";
            rs = stmt.executeQuery(npsQry);
            while (rs.next()) {
                if (rs.getString("AD_AMT") != null) {
                    amt = amt + rs.getInt("AD_AMT");
                }
            }
            npsl = amt + "";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
        }
        return npsl;
    }

    public static String getCpf(String aqslno, Connection con, String aqDtls, int aqYear, int aqMonth) throws Exception {
        ResultSet rs = null;
        Statement stmt = null;
        String cpf = null;
        try {
            stmt = con.createStatement();
            String cpfQry = "SELECT AD_AMT FROM "+aqDtls+" WHERE AQSL_NO='" + aqslno + "' and aq_month='" + aqMonth + "' and aq_year='" + aqYear + "' AND AD_CODE='CPF'";
            rs = stmt.executeQuery(cpfQry);
            if (rs.next()) {
                cpf = rs.getString("AD_AMT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
        }
        return cpf;
    }

    public static String getPpay(String aqslno, Connection con, String aqDtls, int aqYear, int aqMonth) throws Exception {
        ResultSet rs = null;
        Statement stmt = null;
        String ppay = null;
        try {
            stmt = con.createStatement();
            String pPayQry = "SELECT AD_AMT FROM " + aqDtls + " WHERE AQSL_NO='" + aqslno + "' and aq_month='" + aqMonth + "' and aq_year='" + aqYear + "' AND AD_CODE='PPAY'";
            rs = stmt.executeQuery(pPayQry);
            while (rs.next()) {
                ppay = rs.getString("AD_AMT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
        }
        return ppay;
    }

    public static String getDearnessAllowence(String aqslno, Connection con, String aqDtls, int aqYear, int aqMonth) throws Exception {
        ResultSet rs = null;
        Statement stmt = null;
        String dearness = null;
        try {
            stmt = con.createStatement();
            String daQry = "SELECT AD_AMT FROM "+aqDtls+" WHERE AQSL_NO='" + aqslno + "' and aq_month='" + aqMonth + "' and aq_year='" + aqYear + "' AND AD_CODE='DA'";
            rs = stmt.executeQuery(daQry);
            if (rs.next()) {
                dearness = rs.getString("AD_AMT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
        }
        return dearness;
    }

    public static String getGradePay(String aqslno, Connection con, String aqDtls, int aqYear, int aqMonth) throws Exception {
        ResultSet rs = null;
        Statement stmt = null;
        String gradepay = null;
        try {
            stmt = con.createStatement();
            String gpQry = "SELECT AD_AMT FROM " + aqDtls + " WHERE AQSL_NO='" + aqslno + "' and aq_month='" + aqMonth + "' and aq_year='" + aqYear + "' AND AD_CODE='GP'";
            rs = stmt.executeQuery(gpQry);
            if (rs.next()) {
                gradepay = rs.getString("AD_AMT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
        }
        return gradepay;
    }

    public String getAdAmount(Connection con, String billNo, String adType, int aqMonth, int aqYear) throws Exception {

        Statement st = null;
        ResultSet rs = null;
        String amount = "";
        try {
            st = con.createStatement();
            String backPageQry = "Select sum(AQ_DTLS.AD_AMT) AD_AMT from( (Select AQ_MAST.AQSL_NO from AQ_MAST where AQ_MAST.BILL_NO='" + billNo + "' AND AQ_MAST.AQ_MONTH=" + aqMonth + " AND AQ_MAST.AQ_YEAR=" + aqYear + " )AQ_MAST "
                    + "inner join(Select AQ_DTLS.AQSL_NO,AQ_DTLS.AD_AMT from AQ_DTLS where AQ_DTLS.SCHEDULE = '" + adType + "') AQ_DTLS "
                    + "on AQ_MAST.AQSL_NO = AQ_DTLS.AQSL_NO)";
            rs = st.executeQuery(backPageQry);
            while (rs.next()) {
                amount = rs.getString("AD_AMT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
        }
        return amount;
    }

    public String getPayAmt(Connection con, String billNo, int aqMonth, int aqYear) throws Exception {

        Statement st = null;
        ResultSet rs = null;
        String amount = "";
        try {
            st = con.createStatement();
            String payAmtQry = "Select sum(CUR_BASIC) AD_AMT from AQ_MAST WHERE BILL_NO='" + billNo + "' and aq_month =" +aqMonth+ " and aq_year=" +aqYear+ "";
            rs = st.executeQuery(payAmtQry);
            while (rs.next()) {
                amount = rs.getString("AD_AMT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
        }
        return amount;
    }

    public String getDearnessPay(Connection con, String billNo, int aqMonth, int aqYear, String aqDtlsTbl) throws Exception {

        Statement st = null;
        ResultSet rs = null;
        String amount = "";
        try {
            st = con.createStatement();
           String backPageQry = "select sum(a.AD_AMT) AD_AMT from "+aqDtlsTbl+" a, AQ_MAST b where a.aqsl_no = b.aqsl_no and b.aq_month = "+aqMonth+" "
                    + "AND b.aq_year = "+aqYear+" AND AD_CODE ='DP' and b.bill_no = '"+billNo+"' ";
            rs = st.executeQuery(backPageQry);
            while (rs.next()) {
                amount = rs.getString("AD_AMT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
        }
        return amount;
    }

    public String[] getTotalAmount(Connection con, String billNo, String aqDtls, int aqYear, int aqMonth) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String totamt = "0";
        double totBankAmt = 0;
        String arr[] = new String[2];
        try {

            st = con.createStatement();
            rs = st.executeQuery("SELECT BANK_NAME from AQ_MAST WHERE BILL_NO ='" + billNo + "' and DEFAULT_BANK= 1 OR DEFAULT_BANK = 0 GROUP BY BANK_NAME");

            while (rs.next()) {
                if (rs.getString("BANK_NAME") != null && !rs.getString("BANK_NAME").equals("")) {

                    totamt = getEmpSalDetails(con, rs.getString("BANK_NAME"), billNo, true, aqDtls, aqYear, aqMonth);
                } else {
                    totamt = getEmpSalDetails(con, null, billNo, false, aqDtls, aqYear, aqMonth);
                }
                totBankAmt = Double.parseDouble(totamt) + totBankAmt;
            }
            arr[0] = Double.toString(totBankAmt);
            arr[1] = Numtowordconvertion.convertNumber((int) totBankAmt);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
        }
        return arr;
    }

    public String getEmpSalDetails(Connection con, String bankName, String billNo, boolean hasAccount, String aqDtls, int aqYear, int aqMonth) throws Exception {
        ResultSet rs = null;
        ResultSet rs1 = null;
        Statement stmt = null;
        Statement stmt1 = null;
        String query = "";
        double netAmount = 0.0;
        double dedAmount = 0.0;
        double allAmount = 0.0;
        double total = 0.0;

        try {
            stmt = con.createStatement();
            stmt1 = con.createStatement();

            if (hasAccount) {
                query = "SELECT EMP_NAME,CUR_DESG,BANK_ACC_NO,CUR_BASIC,EMP_CODE,AQSL_NO from AQ_MAST WHERE BANK_NAME='" + bankName + "' "
                        + "AND BILL_NO='" + billNo + "' AND DEFAULT_BANK = 1 ORDER BY POST_SL_NO";
            } else {
                query = "SELECT EMP_NAME,CUR_DESG,BANK_ACC_NO,CUR_BASIC,EMP_CODE,AQSL_NO from AQ_MAST WHERE BILL_NO='" + billNo + "' "
                        + "AND DEFAULT_BANK=0 ORDER BY POST_SL_NO";
            }

            rs = stmt.executeQuery(query);
            while (rs.next()) {

                String dedQry = "SELECT SUM(AQ_DTLS.AD_AMT) DEDUCTION FROM " + aqDtls + " WHERE AQ_DTLS.AD_TYPE = 'D' AND EMP_CODE='" + rs.getString("EMP_CODE") + "'"
                        + " AND AQSL_NO = '" + rs.getString("AQSL_NO") + "' and aq_month='" + aqMonth + "' and aq_year='" + aqYear + "' ";
                rs1 = stmt1.executeQuery(dedQry);
                if (rs1.next()) {
                    dedAmount = rs1.getDouble("DEDUCTION");
                }
                String alowQry = "SELECT SUM(AQ_DTLS.AD_AMT) ALLOWANCE FROM " + aqDtls + " WHERE AQ_DTLS.AD_TYPE = 'A' and aq_month='" + aqMonth + "' "
                        + "AND EMP_CODE='" + rs.getString("EMP_CODE") + "' AND AQSL_NO = '" + rs.getString("AQSL_NO") + "'  and aq_year='" + aqYear + "'";
                rs1 = stmt1.executeQuery(alowQry);
                if (rs1.next()) {
                    allAmount = rs1.getDouble("ALLOWANCE");
                }
                netAmount = rs.getDouble("CUR_BASIC") + allAmount - dedAmount;
                total += netAmount;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, rs1);
            DataBaseFunctions.closeSqlObjects(stmt, stmt1);
        }
        return Double.toString(total);
    }

    public String getDDODesignationList(Statement st2, String billNo) {
        String query = "";
        ResultSet rs = null;
        String ddoDesg = "";
        query = "SELECT POST FROM BILL_MAST INNER JOIN G_POST ON BILL_MAST.OFF_DDO=G_POST.POST_CODE WHERE BILL_MAST.BILL_NO='" + billNo + "'";
        try {
            rs = st2.executeQuery(query);
            while (rs.next()) {
                ddoDesg = rs.getString("POST");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ddoDesg;
    }

    public String getMonthAndYear(Connection con, String billNo) throws Exception {

        Statement st = null;
        ResultSet rs = null;
        String query1 = "";

        String yearVal = "";
        int monthVal = 0;
        String monthStr = "";
        String result = "";
        try {
            st = con.createStatement();
            query1 = "SELECT AQ_MONTH,AQ_YEAR FROM BILL_MAST WHERE BILL_NO='" + billNo + "'";
            rs = st.executeQuery(query1);
            if (rs.next()) {
                yearVal = rs.getString("AQ_YEAR");
                monthVal = rs.getInt("AQ_MONTH");
                monthStr = CalendarCommonMethods.getFullMonthAsString(monthVal);
                result = monthStr + "-" + yearVal;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
        }
        return result;
    }

    public String getOfficeName(Connection con, String billNo) {

        String stQuery1 = "";
        ResultSet rs = null;
        Statement st = null;
        String offName = null;
        try {
            st = con.createStatement();
            stQuery1 = "SELECT OFF_EN FROM (SELECT OFF_CODE FROM BILL_MAST WHERE BILL_NO='" + billNo + "' )TAB1 INNER JOIN G_OFFICE ON G_OFFICE.OFF_CODE=TAB1.OFF_CODE ";
            rs = st.executeQuery(stQuery1);
            if (rs.next()) {
                offName = rs.getString("OFF_EN");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
        }
        return offName;
    }

    public String getGrossTotal1(Connection con, String bankName, String billNo, boolean hasAccount) throws Exception {
        ResultSet rs = null;
        ResultSet rs2 = null;

        double grossAmount = 0.0;
        double dedAmount = 0.0;
        double totdedAmount = 0.0;
        double allAmount = 0.0;
        double totbasic = 0.0;
        double totallowance = 0.0;
        double total = 0.0;
        Statement stmt = null;
        Statement stmt2 = null;
        try {
            stmt = con.createStatement();
            stmt2 = con.createStatement();
            String query1 = "";
            String query2 = "";
            String query3 = "";

            if (hasAccount) {
                query1 = "SELECT EMP_NAME,CUR_DESG,BANK_ACC_NO,CUR_BASIC,EMP_CODE,AQSL_NO from AQ_MAST WHERE BANK_NAME='" + bankName + "' "
                        + "AND BILL_NO = '" + billNo + "' AND DEFAULT_BANK = 1 ORDER BY POST_SL_NO";
            } else {
                query1 = "SELECT EMP_NAME,CUR_DESG,BANK_ACC_NO,CUR_BASIC,EMP_CODE,AQSL_NO from AQ_MAST WHERE BILL_NO='" + billNo + "' "
                        + "AND DEFAULT_BANK = 0 ORDER BY POST_SL_NO";
            }
            rs = stmt.executeQuery(query1);
            while (rs.next()) {
                query2 = "SELECT SUM(AQ_DTLS.AD_AMT) DEDUCTION FROM AQ_DTLS WHERE AQ_DTLS.AD_TYPE = 'D' AND EMP_CODE='" + rs.getString("EMP_CODE") + "' "
                        + "AND AQSL_NO = '" + rs.getString("AQSL_NO") + "'";
                rs2 = stmt2.executeQuery(query2);
                if (rs2.next()) {
                    dedAmount = rs2.getDouble("DEDUCTION");
                }
                DataBaseFunctions.closeSqlObjects(rs2, stmt2);

                query3 = "SELECT SUM(AQ_DTLS.AD_AMT) ALLOWANCE FROM AQ_DTLS WHERE AQ_DTLS.AD_TYPE = 'A' AND "
                        + "EMP_CODE='" + rs.getString("EMP_CODE") + "' AND AQSL_NO = '" + rs.getString("AQSL_NO") + "'";
                stmt2 = con.createStatement();
                rs2 = stmt2.executeQuery(query3);
                if (rs2.next()) {
                    allAmount = rs2.getDouble("ALLOWANCE");
                }
                totbasic = rs.getDouble("CUR_BASIC") + totbasic;
                totdedAmount = dedAmount + totdedAmount;
                totallowance = allAmount + totallowance;

            }
            total = totbasic + totallowance;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(rs2, stmt2);
        }
        return Double.toString(total);
    }

    public static int getPrivateDeductionLoanForEmp(Connection con, String aqslno, String aqDtls, int aqYear, int aqMonth) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int total = 0;
        try {
            st = con.createStatement();
            String pdQuery = "SELECT SUM(AD_AMT) AD_AMT FROM " + aqDtls + " WHERE AQSL_NO='" + aqslno + "' and aq_month='" + aqMonth + "' and aq_year='" + aqYear + "' "
                    + "AND (SCHEDULE='PVTD' OR SCHEDULE='PVTL') AND AD_AMT>0";
            rs = st.executeQuery(pdQuery);
            if (rs.next()) {
                total = total + rs.getInt("AD_AMT");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
        }
        return total;
    }

    public static int getPrivateLoan(Connection con, String aqslno, String aqDtls, int aqYear, int aqMonth) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int total = 0;
        try {
            st = con.createStatement();
            String plQuery = "SELECT SUM(AD_AMT) AD_AMT FROM " + aqDtls + " WHERE AQSL_NO='" + aqslno + "' and aq_month='" + aqMonth + "' "
                    + "and aq_year='" + aqYear + "' AND SCHEDULE='PVTL'";
            rs = st.executeQuery(plQuery);
            if (rs.next()) {
                total = total + rs.getInt("AD_AMT");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
        }
        return total;
    }

    public int getGrossPay(Connection con, String aqslno, String aqDtls, int aqYear, int aqMonth) throws Exception {
        Statement st = null;
        Statement st1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        int total = 0;

        try {
            st = con.createStatement();
            st1 = con.createStatement();
            rs = st.executeQuery("SELECT CUR_BASIC FROM AQ_MAST WHERE AQSL_NO='" + aqslno + "'");
            if (rs.next()) {
                total = rs.getInt("CUR_BASIC");
                String grosQry = "SELECT SUM(AD_AMT) AD_AMT FROM " + aqDtls + " WHERE AQSL_NO='" + aqslno + "' and aq_month='" + aqMonth + "' "
                        + "and aq_year='" + aqYear + "' AND AD_TYPE='A'";
                rs1 = st1.executeQuery(grosQry);
                if (rs1.next()) {
                    total = total + rs1.getInt("AD_AMT");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(rs1, st1);
        }
        return total;
    }

    public static int getTotalDedn(Connection con, String aqslno, String aqDtls, int aqYear, int aqMonth) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int total = 0;
        try {
            st = con.createStatement();
            String degnQuery = "SELECT SUM(AD_AMT) AD_AMT FROM " + aqDtls + " WHERE AQSL_NO='" + aqslno + "' and aq_month='" + aqMonth + "' "
                    + "and aq_year='" + aqYear + "' AND AD_TYPE='D' AND SCHEDULE !='PVTL' AND SCHEDULE !='PVTD'";
            rs = st.executeQuery(degnQuery);
            if (rs.next()) {
                total = rs.getInt("AD_AMT");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
        }
        return total;
    }

}
