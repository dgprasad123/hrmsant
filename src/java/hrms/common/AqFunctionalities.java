/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.common;

import static hrms.dao.payroll.billbrowser.AqReportDAOImpl.getColConfiguredDtata;
import hrms.model.payroll.billbrowser.BillConfigObj;
import hrms.model.payroll.schedule.ADDetailsHealperBean;
import hrms.model.payroll.schedule.AqreportHelperBean;
import hrms.model.payroll.schedule.PrivateDeduction;
import hrms.model.payroll.schedule.SectionWiseAqBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Manas
 */
public class AqFunctionalities {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static String getAQBillDtlsTable(int mon, int year) {
        String aqDTLS = "AQ_DTLS";
        if (year < 2021) {
            aqDTLS = "hrmis.AQ_DTLS1";
        }
        return aqDTLS;
    }

    public ArrayList getSectionWiseBillDtls(String billno, int mon, int year, String format, BillConfigObj billConfigObj, String empType) throws Exception {
        String section = "";

        int secslno = 0;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        SectionWiseAqBean objBill = null;;
        ArrayList a1 = new ArrayList();

        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            String sqlqryforsection = "SELECT SEC_SL_NO,SECTION,EMP_TYPE FROM AQ_MAST WHERE  BILL_NO='" + billno + "' AND AQ_MONTH=" + mon + " AND AQ_YEAR=" + year + "  GROUP BY SECTION,SEC_SL_NO,EMP_TYPE ORDER BY SEC_SL_NO";
            rs = st.executeQuery(sqlqryforsection);
            while (rs.next()) {
                objBill = null;
                section = rs.getString("SECTION");
                secslno = rs.getInt("SEC_SL_NO");
                empType = rs.getString("EMP_TYPE");
                if (format.equals("f2") && empType.equals("R")) {
                    objBill = getAqBillDetailsF2(billno, mon, year, section, secslno, billConfigObj);
                } else if (format.equals("f1") && empType.equals("R")) {
                    objBill = getAqBillDetails(billno, mon, year, section, secslno, billConfigObj);
                } else if (format.equals("f1") && empType.equals("C")) {
                    objBill = getAqBillDetailsContractual(billno, mon, year, section, secslno, billConfigObj);
                }
                objBill.setSectionname(section);
                a1.add(objBill);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return a1;
    }

    public BillConfigObj getBillConfig(String billno) {
        BillConfigObj objBill = new BillConfigObj();
        Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        String col6Desc[] = null;
        String col7Desc[] = null;
        String col16Desc[] = null;
        String col17Desc[] = null;
        String col18Desc[] = null;
        String offCode = "";
        String billgroupId = "";

        try {
            /*
             *  get aqreport configuration from database
             * 
             */
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("select off_code, bill_group_id from bill_mast where bill_no='" + billno + "'");
            if (rs.next()) {
                offCode = rs.getString("off_code");
                billgroupId = rs.getString("bill_group_id");
            }

            DataBaseFunctions.closeSqlObjects(rs, st);
            st = con.createStatement();
            rs = st.executeQuery("SELECT CONFIGURED_LVL FROM BILL_GROUP_MASTER WHERE BILL_GROUP_ID='" + billgroupId + "'");
            if (rs.next()) {
                String level = rs.getString("CONFIGURED_LVL");
                objBill.setCol6List(getColConfiguredDtata(con, offCode, billgroupId, 6, level));
                objBill.setCol7List(getColConfiguredDtata(con, offCode, billgroupId, 7, level));
                objBill.setCol16List(getColConfiguredDtata(con, offCode, billgroupId, 16, level));
                objBill.setCol17List(getColConfiguredDtata(con, offCode, billgroupId, 17, level));
                objBill.setCol18List(getColConfiguredDtata(con, offCode, billgroupId, 18, level));
            }
            DataBaseFunctions.closeSqlObjects(rs, st);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st, con);
        }
        return objBill;

    }

    public SectionWiseAqBean getAqBillDetailsF2(String billno, int mon, int year, String section, int secslno, BillConfigObj billConfigObj) throws Exception {
        Connection con = null;
        Statement st = null;
        Statement st1 = null;
        Statement st2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        AqreportHelperBean aqbean = null;
        ArrayList aqlist = new ArrayList();
        int colNo = 0;
        section = StringUtils.replace(section, "'", "''");
        SectionWiseAqBean secWiseAqBean = new SectionWiseAqBean();
        String aqDTLS = "AQ_DTLS";
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            st1 = con.createStatement();
            st2 = con.createStatement();
            st = con.createStatement();

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(mon, year);

            String sqlQuery = "SELECT * FROM (SELECT AQSL_NO,EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,SPC_ORD_NO,SPC_ORD_DATE,GPF_ACC_NO,ACCT_TYPE,EMP_CODE,post_sl_no,SEC_SL_NO "
                    + "FROM AQ_MAST WHERE AQ_MONTH=" + mon + " AND AQ_YEAR=" + year + " AND BILL_NO=" + billno + " AND SECTION='" + section + "' AND SEC_SL_NO=" + secslno + " AND DEP_CODE != '11') AQ_MAST "
                    + "LEFT OUTER JOIN (SELECT EMP_ID,BRASS_NO,CUR_CADRE_CODE,IF_GPF_ASSUMED FROM EMP_MAST)EMP_MAST ON AQ_MAST.EMP_CODE = EMP_MAST.EMP_ID "
                    + "LEFT OUTER JOIN (SELECT CADRE_CODE,CADRE_ABBR FROM G_CADRE)G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE order by POST_SL_NO";
            //"SELECT rownum slno,AQSL_NO,EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,SPC_ORD_NO,SPC_ORD_DATE,GPF_ACC_NO,ACCT_TYPE FROM AQ_MAST WHERE AQ_MONTH='"+mon+"' AND AQ_YEAR='"+year+"' AND BILL_NO='"+billno+"' order by post_sl_no"

            rs = st.executeQuery(sqlQuery);
            int i = 0;
            while (rs.next()) {
                i++;
                String aqslno = rs.getString("AQSL_NO");
                aqbean = new AqreportHelperBean();
                aqbean.setSlno("" + i);
                if (rs.getString("EMP_NAME") != null && !rs.getString("EMP_NAME").equals("")) {
                    if (rs.getString("BRASS_NO") != null && !rs.getString("BRASS_NO").equals("")) {
                        aqbean.setEmpname(rs.getString("BRASS_NO") + " " + rs.getString("EMP_NAME"));
                    } else {
                        aqbean.setEmpname(rs.getString("EMP_NAME"));
                    }
                } else {
                    aqbean.setEmpname("VACANT");
                }

                aqbean.setCadreabbr(rs.getString("CADRE_ABBR"));
                aqbean.setDesg(rs.getString("CUR_DESG"));
                aqbean.setPayscale(rs.getString("PAY_SCALE"));
                aqbean.setAccNo(rs.getString("BANK_ACC_NO"));
                aqbean.setAcctype(rs.getString("ACCT_TYPE"));
                aqbean.setBasic(rs.getString("CUR_BASIC"));
                if (rs.getString("SPC_ORD_NO") != null && !rs.getString("SPC_ORD_NO").equals("")) {
                    aqbean.setOrdno(rs.getString("SPC_ORD_NO"));
                } else {
                    aqbean.setOrdno("");
                }
                aqbean.setOrddate(CommonFunctions.getFormattedOutputDate4(rs.getDate("SPC_ORD_DATE")));
                aqbean.setGpfacct(rs.getString("GPF_ACC_NO"));
                if (rs.getString("IF_GPF_ASSUMED") != null && !rs.getString("IF_GPF_ASSUMED").equals("")) {
                    if (rs.getString("IF_GPF_ASSUMED").equalsIgnoreCase("Y")) {
                        aqbean.setGpfacct("");
                    }
                } else {
                    aqbean.setGpfacct("");
                }
                aqbean.setGpfNoAssumed(rs.getString("IF_GPF_ASSUMED"));

                String queryClump = "select * from (select cnt,REP_COL,tmep4.AD_CODE,AD_AMT,SCHEDULE,tmep4.NOW_DEDN,AD_TYPE,sl_no,REF_DESC from(select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM AQ_DTLS WHERE AQSL_NO='" + rs.getString("AQSL_NO") + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt = 1)tmep4 "
                        + "left outer join (SELECT AD_CODE,REF_DESC,NOW_DEDN FROM AQ_DTLS WHERE AQSL_NO='" + rs.getString("AQSL_NO") + "') temp2 on "
                        + "tmep4.ad_code = temp2.ad_code and tmep4.now_dedn = temp2.now_dedn "
                        + "union "
                        + "select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no,''::TEXT from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM AQ_DTLS WHERE AQSL_NO='" + rs.getString("AQSL_NO") + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt > 1 ) final_tab order by SL_NO ";
                rs1 = st1.executeQuery(queryClump);

                while (rs1.next()) {
                    colNo = rs1.getInt("REP_COL");
                    String refdesc = "";
                    String adcode = "";
                    String nowdedn = "";

                    switch (colNo) {
                        case 3:
                            aqbean.setCol3(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 4:
                            aqbean.setCol4(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 5:
                            aqbean.setCol5(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 6:
                            aqbean.setCol6(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol6List());
                            break;
                        case 7:
                            aqbean.setCol7(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol7List());
                            break;
                        case 9:
                            aqbean.setCol9(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 10:
                            if ((aqbean.getAcctype()).equals("GPF") && (rs1.getString("AD_CODE").equals("GPF") || rs1.getString("AD_CODE").equals("GA") || rs1.getString("AD_CODE").equals("GPDD") || rs1.getString("AD_CODE").equals("GPIR"))) {
                                aqbean.setCol10(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            } else if ((aqbean.getAcctype()).equals("PRAN") && rs1.getString("AD_CODE").equals("CPF")) {
                                aqbean.setCol10(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            } else if ((aqbean.getAcctype()).equals("TPF") && rs1.getString("AD_CODE").equals("TPF")) {
                                aqbean.setCol10(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            }
                            break;
                        case 11:
                            aqbean.setCol11(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 12:
                            aqbean.setCol12(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 13:
                            refdesc = rs1.getString("REF_DESC");
                            adcode = rs1.getString("AD_CODE");
                            nowdedn = rs1.getString("NOW_DEDN");
                            if ((refdesc == null || refdesc.equals("")) && rs1.getInt("AD_AMT") > 0) {
                                refdesc = getRefDesc(adcode, nowdedn, aqslno, mon, year);
                                aqbean.setCol13(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), refdesc);
                            } else {
                                aqbean.setCol13(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            }
                            break;
                        case 14:
                            aqbean.setCol14(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 15:
                            aqbean.setCol15(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 16:
                            aqbean.setCol16(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol16List());
                            break;
                        case 17:
                            aqbean.setCol17(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol17List());
                            break;
                        case 18:
                            aqbean.setCol18(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol18List());
                            break;
                    }
                }
                int privatededuction = getPrivateDeduction(aqslno, mon, year);
                int privateloan = getPrivateLoan(aqslno, mon, year);
                int grosspay = getGrossPay(rs.getString("AQSL_NO"), mon, year);
                int totaldedn = getTotalDedn(rs.getString("AQSL_NO"), mon, year);
                aqbean.setCol8("GROSS PAY", grosspay, null, null, null);
                aqbean.setCol19("TOTDEN", totaldedn, null, null, null);
                aqbean.setCol19("NETPAY", grosspay - totaldedn, null, null, null);
                aqbean.setCol20("PVTDED", privatededuction, null, null, null);

                aqbean.setCol20("BANKLOAN", privateloan, null, null, null);
                aqbean.setCol20("NETBALANCE", grosspay - (totaldedn + privatededuction + privateloan), null, null, null);

                aqlist.add(aqbean);
                Collections.sort(aqbean.getCol6());
                Collections.sort(aqbean.getCol7());
                Collections.sort(aqbean.getCol16());
                Collections.sort(aqbean.getCol17());
                Collections.sort(aqbean.getCol18());

            }
            secWiseAqBean.setAqlistSectionWise(aqlist);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(rs2, st2);
            DataBaseFunctions.closeSqlObjects(rs1, st1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return secWiseAqBean;
    }

    public int getPrivateDeduction(String aqslno, int mon, int year) throws Exception {
        Connection con = null;
        Statement st = null;
        Statement st1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        int total = 0;

        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            st1 = con.createStatement();

            rs1 = st1.executeQuery("SELECT SUM(AD_AMT) AD_AMT FROM " + getAQBillDtlsTable(mon, year) + " WHERE AQSL_NO='" + aqslno + "' AND SCHEDULE='PVTD' AND AD_AMT>0");
            if (rs1.next()) {
                total = total + rs1.getInt("AD_AMT");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(rs1, st1);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return total;
    }

    public Vector getPrivateDeductionLoan(String aqslno) throws Exception {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        Vector privateDeduction = new Vector();
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT T2.ACC_NO,AD_AMT,IFSC_CODE,MICR_CODE FROM(SELECT T1.ACC_NO,AD_AMT,BRANCH_CODE FROM (SELECT ACC_NO,AD_AMT AD_AMT,AD_REF_ID FROM AQ_DTLS WHERE AQSL_NO='" + aqslno + "' AND (SCHEDULE='PVTD' OR SCHEDULE='PVTL') AND ACC_NO IS NOT NULL)T1 "
                    + "INNER JOIN EMP_LOAN_SANC ON T1.AD_REF_ID = EMP_LOAN_SANC.LOANID)T2 "
                    + "INNER JOIN G_BRANCH ON T2.BRANCH_CODE = G_BRANCH.BRANCH_CODE");
            while (rs.next()) {
                PrivateDeduction pvd = new PrivateDeduction();
                pvd.setLoanaccno(rs.getString("ACC_NO"));
                pvd.setAcctype("Loan (Cash Credit)");
                pvd.setBankifsccode(rs.getString("IFSC_CODE"));
                pvd.setBankmicrcode(rs.getString("MICR_CODE"));
                pvd.setLoanamt(rs.getInt("AD_AMT"));
                privateDeduction.add(pvd);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st, con);
        }

        return privateDeduction;
    }

    public static int getPrivateDeductionLoan(String aqslno, boolean isTransferToDDOAccount) throws Exception {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        int total = 0;

        try {
            st = con.createStatement();
            if (isTransferToDDOAccount == true) {
                rs = st.executeQuery("SELECT SUM(AD_AMT) AD_AMT FROM AQ_DTLS WHERE AQSL_NO='" + aqslno + "' AND (SCHEDULE='PVTD' OR SCHEDULE='PVTL') AND ACC_NO IS NULL");
            } else {
                rs = st.executeQuery("SELECT SUM(AD_AMT) AD_AMT FROM AQ_DTLS WHERE AQSL_NO='" + aqslno + "' AND (SCHEDULE='PVTD' OR SCHEDULE='PVTL') AND ACC_NO IS NOT NULL");
            }
            if (rs.next()) {
                total = total + rs.getInt("AD_AMT");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return total;
    }

    public String getRefDesc(String adCode, String nowdedn, String aqslno, int mon, int year) {
        String refdesc = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT REF_DESC FROM " + getAQBillDtlsTable(mon, year) + " WHERE AQ_YEAR=? AND AQ_MONTH=? AND AQSL_NO=? AND NOW_DEDN=? AND AD_CODE=? AND AD_AMT > 0");
            pstmt.setInt(1, year);
            pstmt.setInt(2, mon);
            pstmt.setString(3, aqslno);
            pstmt.setString(4, nowdedn);
            pstmt.setString(5, adCode);
            res = pstmt.executeQuery();
            int i = 0;
            while (res.next()) {
                i++;
                refdesc = res.getString("REF_DESC");
            }
            if (i > 1) {
                refdesc = null;
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return refdesc;
    }

    public int getPrivateLoan(String aqslno, int mon, int year) throws Exception {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total = 0;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT SUM(AD_AMT) AD_AMT FROM " + getAQBillDtlsTable(mon, year) + " WHERE AQSL_NO=? AND SCHEDULE='PVTL' AND AD_AMT>0");            
            pstmt.setString(1, aqslno);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                total = total + rs.getInt("AD_AMT");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return total;
    }

    public int getGrossPay(String aqslno, int mon, int year) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        int total = 0;
        boolean dataFound = false;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT CUR_BASIC FROM AQ_MAST WHERE AQ_YEAR=? AND AQ_MONTH=? AND AQSL_NO=?");
            pstmt.setInt(1, year);
            pstmt.setInt(2, mon);
            pstmt.setString(3, aqslno);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                total = rs.getInt("CUR_BASIC");
                dataFound = true;
            }
            if (dataFound) {
                pstmt = con.prepareStatement("SELECT SUM(AD_AMT) AD_AMT FROM " + getAQBillDtlsTable(mon, year) + " WHERE AQSL_NO=? AND AD_TYPE='A' AND AD_AMT>0");                
                pstmt.setString(1, aqslno);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    total = total + rs.getInt("AD_AMT");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return total;
    }

    public int getTotalDedn(String aqslno, int mon, int year) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        int total = 0;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT SUM(AD_AMT) AD_AMT FROM " + getAQBillDtlsTable(mon, year) + " WHERE AQSL_NO=? AND AD_TYPE='D' AND SCHEDULE !='PVTL' AND SCHEDULE !='PVTD' AND AD_AMT>0");            
            pstmt.setString(1, aqslno);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                total = rs.getInt("AD_AMT");
            }
            
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            
            pstmt = con.prepareStatement("SELECT SUM(AD_AMT) AD_AMT FROM " + getAQBillDtlsTable(mon, year) + " WHERE AQSL_NO=? AND AD_TYPE='D' AND ad_code='EPF' AND AD_AMT>0");            
            pstmt.setString(1, aqslno);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                total = total + rs.getInt("AD_AMT");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return total;
    }

    public SectionWiseAqBean getAqBillDetailsContractual(String billno, int mon, int year, String section, int secslno, BillConfigObj billConfigObj) throws Exception {
        Connection con = null;
        Statement st = null;
        Statement st1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String aqslno = null;
        int colNo = 0;
        PreparedStatement pstamt = null;
        AqreportHelperBean aqbean = null;
        ArrayList aqlist = new ArrayList();
        SectionWiseAqBean objBill = new SectionWiseAqBean();
        String aqDTLS = "AQ_DTLS";
        try {
            con = dataSource.getConnection();
            aqDTLS = AqFunctionalities.getAQBillDtlsTable(mon, year);
            st = con.createStatement();
            pstamt = con.prepareStatement("SELECT * FROM (SELECT rownum slno,AQSL_NO,EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,SPC_ORD_NO,SPC_ORD_DATE,GPF_ACC_NO,ACCT_TYPE,EMP_CODE,post_sl_no,SEC_SL_NO "
                    + "FROM AQ_MAST WHERE AQ_MONTH=? AND AQ_YEAR=? AND BILL_NO=? AND SECTION=? AND SEC_SL_NO=? AND DEP_CODE != '11') AQ_MAST "
                    + "LEFT OUTER JOIN (SELECT EMP_ID,CUR_CADRE_CODE,IF_GPF_ASSUMED FROM EMP_MAST)EMP_MAST ON AQ_MAST.EMP_CODE = EMP_MAST.EMP_ID "
                    + "LEFT OUTER JOIN (SELECT CADRE_CODE,CADRE_ABBR FROM G_CADRE)G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE order by POST_SL_NO");

            pstamt.setInt(1, mon);
            pstamt.setInt(2, year);
            pstamt.setInt(3, Integer.parseInt(billno));
            pstamt.setString(4, section);
            pstamt.setInt(5, secslno);
            rs = pstamt.executeQuery();
            int i = 0;
            while (rs.next()) {
                st1 = con.createStatement();
                i++;

                aqbean = new AqreportHelperBean();
                aqbean.setSlno("" + i);
                if (rs.getString("EMP_NAME") != null && !rs.getString("EMP_NAME").equals("")) {
                    aqbean.setEmpname(rs.getString("EMP_NAME"));
                } else {
                    aqbean.setEmpname("VACANT");
                }
                aqbean.setCadreabbr(rs.getString("CADRE_ABBR"));
                aqbean.setDesg(rs.getString("CUR_DESG"));
                aqbean.setPayscale(rs.getString("PAY_SCALE"));
                aqbean.setAccNo(rs.getString("BANK_ACC_NO"));
                aqbean.setAcctype(rs.getString("ACCT_TYPE"));
                aqbean.setBasic(rs.getString("CUR_BASIC"));
                aqslno = rs.getString("AQSL_NO");
                String queryClump = "select * from (select cnt,REP_COL,tmep4.AD_CODE,AD_AMT,SCHEDULE,tmep4.NOW_DEDN,AD_TYPE,sl_no,REF_DESC from(select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt = 1)tmep4 "
                        + "left outer join (SELECT AD_CODE,REF_DESC,NOW_DEDN FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp2 on "
                        + "tmep4.ad_code = temp2.ad_code and tmep4.now_dedn = temp2.now_dedn "
                        + "union "
                        + "select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no,''::TEXT from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt > 1 ) FINAL_TAB order by SL_NO ";
                rs1 = st1.executeQuery(queryClump);
                while (rs1.next()) {
                    colNo = rs1.getInt("REP_COL");
                    String refdesc = "";
                    String adcode = "";
                    String nowdedn = "";
                    switch (colNo) {
                        case 3:
                            aqbean.setCol3(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 5:
                            aqbean.setCol5(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 6:
                            aqbean.setCol6(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol6List());
                            break;
                        case 7:
                            aqbean.setCol7(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol7List());
                            break;
                        case 9:
                            aqbean.setCol9(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 10:
                            if ((aqbean.getAcctype()).equals("GPF") && (rs1.getString("AD_CODE").equals("GPF") || rs1.getString("AD_CODE").equals("GA") || rs1.getString("AD_CODE").equals("GPDD") || rs1.getString("AD_CODE").equals("GPIR"))) {
                                aqbean.setCol10(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            } else if ((aqbean.getAcctype()).equals("PRAN") && rs1.getString("AD_CODE").equals("CPF")) {
                                aqbean.setCol10(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            } else if ((aqbean.getAcctype()).equals("TPF") && (rs1.getString("AD_CODE").equals("TPF") || rs1.getString("AD_CODE").equals("TPFGA"))) {
                                aqbean.setCol10(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            }
                            break;
                        case 11:
                            aqbean.setCol11(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 12:
                            aqbean.setCol12(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 13:
                            refdesc = rs1.getString("REF_DESC");
                            adcode = rs1.getString("AD_CODE");
                            nowdedn = rs1.getString("NOW_DEDN");
                            if ((refdesc == null || refdesc.equals("")) && rs1.getInt("AD_AMT") > 0) {
                                refdesc = getRefDesc(adcode, nowdedn, aqslno, mon, year);
                                aqbean.setCol13(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), refdesc);
                            } else {
                                aqbean.setCol13(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            }
                            break;
                        case 14:
                            refdesc = rs1.getString("REF_DESC");
                            adcode = rs1.getString("AD_CODE");
                            nowdedn = rs1.getString("NOW_DEDN");
                            if ((refdesc == null || refdesc.equals("")) && rs1.getInt("AD_AMT") > 0) {
                                refdesc = getRefDesc(adcode, nowdedn, aqslno, mon, year);
                                aqbean.setCol14(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), refdesc);
                            } else {
                                aqbean.setCol14(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            }
                            break;
                        case 15:
                            aqbean.setCol15(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 16:
                            aqbean.setCol16(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol16List());
                            break;
                        case 17:
                            aqbean.setCol17(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol17List());
                            break;
                        case 18:
                            aqbean.setCol18(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol18List());
                            break;
                    }
                }
                int grosspay = getGrossPay(rs.getString("AQSL_NO"), mon, year);
                int totaldedn = getTotalDedn(rs.getString("AQSL_NO"), mon, year);
                aqbean.setCol8("GROSS PAY", grosspay, null, null, null);
                aqbean.setCol19("TOTDEN", totaldedn, null, null, null);
                aqbean.setCol20("NETPAY", grosspay - totaldedn, null, null, null);
                aqlist.add(aqbean);
                Collections.sort(aqbean.getCol6());
                Collections.sort(aqbean.getCol7());
                Collections.sort(aqbean.getCol16());
                Collections.sort(aqbean.getCol17());
                Collections.sort(aqbean.getCol18());
            }
            objBill.setAqlistSectionWise(aqlist);
            objBill.setAqlistSectionWise(aqlist);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return objBill;
    }

    public SectionWiseAqBean getAqBillDetails(String billno, int mon, int year, String section, int secslno, BillConfigObj billConfigObj) throws Exception {
        Connection con = null;
        Statement st = null;
        PreparedStatement pstamt = null;
        Statement st1 = null;
        Statement st2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        AqreportHelperBean aqbean = null;
        ArrayList aqlist = new ArrayList();
        int colNo = 0;
        section = StringUtils.replace(section, "'", "''");
        SectionWiseAqBean objBill = new SectionWiseAqBean();
        int col7sum = 0;
        String aqDTLS = "AQ_DTLS";
        try {
            con = dataSource.getConnection();
            aqDTLS = AqFunctionalities.getAQBillDtlsTable(mon, year);
            st = con.createStatement();
            st1 = con.createStatement();
            st2 = con.createStatement();

            pstamt = con.prepareStatement("SELECT * FROM (SELECT rownum slno,AQSL_NO,EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,SPC_ORD_NO,SPC_ORD_DATE,GPF_ACC_NO,ACCT_TYPE,EMP_CODE,post_sl_no,SEC_SL_NO "
                    + "FROM AQ_MAST WHERE AQ_MONTH='" + mon + "' AND AQ_YEAR='" + year + "' AND BILL_NO='" + billno + "' AND SECTION='" + section + "' AND SEC_SL_NO='" + secslno + "' AND DEP_CODE != '11') AQ_MAST "
                    + "LEFT OUTER JOIN (SELECT EMP_ID,BRASS_NO,CUR_CADRE_CODE,IF_GPF_ASSUMED FROM EMP_MAST)EMP_MAST ON AQ_MAST.EMP_CODE = EMP_MAST.EMP_ID "
                    + "LEFT OUTER JOIN (SELECT CADRE_CODE,CADRE_ABBR FROM G_CADRE)G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE order by POST_SL_NO");
            //pstamt.setInt(1,Integer.parseInt(mon));
            // pstamt.setInt(2,Integer.parseInt(year));
            // pstamt.setInt(3,Integer.parseInt(billno));
            //section = StringUtils.replace(section,"'","''");
            // pstamt.setString(4,section);
            // pstamt.setInt(5,secslno);
            rs = pstamt.executeQuery();
            int i = 0;

            while (rs.next()) {
                i++;
                aqbean = new AqreportHelperBean();
                aqbean.setSlno("" + i);
                if (rs.getString("EMP_NAME") != null && !rs.getString("EMP_NAME").equals("")) {
                    if (rs.getString("BRASS_NO") != null && !rs.getString("BRASS_NO").equals("")) {
                        aqbean.setEmpname(rs.getString("BRASS_NO") + " " + rs.getString("EMP_NAME"));
                    } else {
                        aqbean.setEmpname(rs.getString("EMP_NAME"));
                    }
                } else {
                    aqbean.setEmpname("VACANT");
                }
                aqbean.setCadreabbr(rs.getString("CADRE_ABBR"));
                aqbean.setDesg(rs.getString("CUR_DESG"));
                aqbean.setPayscale(rs.getString("PAY_SCALE"));
                aqbean.setAccNo(rs.getString("BANK_ACC_NO"));
                aqbean.setAcctype(rs.getString("ACCT_TYPE"));
                aqbean.setBasic(rs.getString("CUR_BASIC"));
                if (rs.getString("SPC_ORD_NO") != null && !rs.getString("SPC_ORD_NO").equals("")) {
                    aqbean.setOrdno(rs.getString("SPC_ORD_NO"));
                } else {
                    aqbean.setOrdno("");
                }
                aqbean.setOrddate(CommonFunctions.getFormattedOutputDate4(rs.getDate("SPC_ORD_DATE")));
                aqbean.setGpfacct(rs.getString("GPF_ACC_NO"));
                if (rs.getString("IF_GPF_ASSUMED") != null && !rs.getString("IF_GPF_ASSUMED").equals("")) {
                    if (rs.getString("IF_GPF_ASSUMED").equalsIgnoreCase("Y")) {
                        aqbean.setGpfacct("");
                    }
                } else {
                    aqbean.setGpfacct("");
                }
                aqbean.setGpfNoAssumed(rs.getString("IF_GPF_ASSUMED"));
                String aqslno = rs.getString("AQSL_NO");
                //String aqslno = "21910000006590_9_2014_3";
                String queryClump = "select * from (select cnt,REP_COL,tmep4.AD_CODE,AD_AMT,SCHEDULE,tmep4.NOW_DEDN,AD_TYPE,sl_no,REF_DESC from(select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt = 1)tmep4 "
                        + "left outer join (SELECT AD_CODE,REF_DESC,NOW_DEDN FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp2 on "
                        + "tmep4.ad_code = temp2.ad_code and tmep4.now_dedn = temp2.now_dedn "
                        + "union "
                        + "select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no,''::TEXT from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt > 1 ) FINAL_TAB order by SL_NO ";

                rs1 = st1.executeQuery(queryClump);

                while (rs1.next()) {
                    colNo = rs1.getInt("REP_COL");
                    String refdesc = "";
                    String adcode = "";
                    String nowdedn = "";
                    switch (colNo) {
                        case 3:
                            aqbean.setCol3(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 4:
                            aqbean.setCol4(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 5:
                            aqbean.setCol5(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 6:
                            if (rs1.getString("AD_CODE").equalsIgnoreCase("LFQ")) {

                            }
                            aqbean.setCol6(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol6List());
                            break;
                        case 7:
                            if (rs1.getString("AD_CODE").equalsIgnoreCase("LFQ")) {

                            }
                            /*col7sum = col7sum + rs1.getInt("AD_AMT");
                             if(rs1.getString("SCHEDULE").equals("OA") && rs1.getInt("AD_AMT") > 0){
                             
                             }*/
                            aqbean.setCol7(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol7List());
                            break;
                        case 9:
                            aqbean.setCol9(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 10:
                            if ((aqbean.getAcctype()).equals("GPF") && (rs1.getString("AD_CODE").equals("GPF") || rs1.getString("AD_CODE").equals("GA") || rs1.getString("AD_CODE").equals("GPDD") || rs1.getString("AD_CODE").equals("GPIR"))) {
                                aqbean.setCol10(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            } else if ((aqbean.getAcctype()).equals("PRAN") && rs1.getString("AD_CODE").equals("CPF")) {
                                aqbean.setCol10(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            } else if ((aqbean.getAcctype()).equals("TPF") && (rs1.getString("AD_CODE").equals("TPF") || rs1.getString("AD_CODE").equals("TPFGA"))) {
                                aqbean.setCol10(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            }
                            break;
                        case 11:
                            aqbean.setCol11(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 12:
                            aqbean.setCol12(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 13:
                            refdesc = rs1.getString("REF_DESC");
                            adcode = rs1.getString("AD_CODE");
                            nowdedn = rs1.getString("NOW_DEDN");
                            if ((refdesc == null || refdesc.equals("")) && rs1.getInt("AD_AMT") > 0) {
                                refdesc = getRefDesc(adcode, nowdedn, aqslno, mon, year);
                                aqbean.setCol13(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), refdesc);
                            } else {
                                aqbean.setCol13(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            }
                            break;
                        case 14:
                            refdesc = rs1.getString("REF_DESC");
                            adcode = rs1.getString("AD_CODE");
                            nowdedn = rs1.getString("NOW_DEDN");
                            if ((refdesc == null || refdesc.equals("")) && rs1.getInt("AD_AMT") > 0) {
                                refdesc = getRefDesc(adcode, nowdedn, aqslno, mon, year);
                                aqbean.setCol14(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), refdesc);
                            } else {
                                aqbean.setCol14(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            }
                            break;
                        case 15:
                            aqbean.setCol15(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 16:
                            aqbean.setCol16(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol16List());
                            break;
                        case 17:
                            if (aqslno.equals("21910000008647_2_2015_2")) {

                            }
                            aqbean.setCol17(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol17List());
                            break;
                        case 18:
                            aqbean.setCol18(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol18List());
                            break;
                    }
                }
                int grosspay = getGrossPay(rs.getString("AQSL_NO"), mon, year);
                int totaldedn = getTotalDedn(rs.getString("AQSL_NO"), mon, year);
                aqbean.setCol8("GROSS PAY", grosspay, null, null, null);
                aqbean.setCol19("TOTDEN", totaldedn, null, null, null);
                aqbean.setCol20("NETPAY", grosspay - totaldedn, null, null, null);
                aqlist.add(aqbean);
                Collections.sort(aqbean.getCol6());
                Collections.sort(aqbean.getCol7());
                Collections.sort(aqbean.getCol16());
                Collections.sort(aqbean.getCol17());
                Collections.sort(aqbean.getCol18());
            }

            objBill.setAqlistSectionWise(aqlist);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(rs2, st2);
            DataBaseFunctions.closeSqlObjects(rs1, st1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return objBill;
    }

    public static int getColGrandTotal(ArrayList aqlist, String col, int rowNo, String nowdedn) {
        AqreportHelperBean aqbean = new AqreportHelperBean();
        ArrayList column = null;
        String subCol = col.substring(3);
        int val = Integer.parseInt(subCol);
        int total = 0;
        ADDetailsHealperBean addbean = null;
        for (int i = 0; i < aqlist.size(); i++) {
            SectionWiseAqBean sectionwiseAq = (SectionWiseAqBean) aqlist.get(i);
            ArrayList arrBill = sectionwiseAq.getAqlistSectionWise();
            for (int k = 0; k < arrBill.size(); k++) {
                if (arrBill.get(k) != null) {
                    aqbean = (AqreportHelperBean) arrBill.get(k);
                    switch (val) {
                        case 3:
                            column = (ArrayList) aqbean.getCol3();
                            break;
                        case 4:
                            column = (ArrayList) aqbean.getCol4();
                            break;
                        case 5:
                            column = (ArrayList) aqbean.getCol5();
                            break;
                        case 6:
                            column = (ArrayList) aqbean.getCol6();
                            break;
                        case 7:
                            column = (ArrayList) aqbean.getCol7();
                            break;
                        case 8:
                            column = (ArrayList) aqbean.getCol8();
                            break;
                        case 9:
                            column = (ArrayList) aqbean.getCol9();
                            break;
                        case 10:
                            column = (ArrayList) aqbean.getCol10();
                            break;
                        case 11:
                            column = (ArrayList) aqbean.getCol11();
                            break;
                        case 12:
                            column = (ArrayList) aqbean.getCol12();
                            break;
                        case 13:
                            column = (ArrayList) aqbean.getCol13();
                            break;
                        case 14:
                            column = (ArrayList) aqbean.getCol14();
                            break;
                        case 15:
                            column = (ArrayList) aqbean.getCol15();
                            break;
                        case 16:
                            column = (ArrayList) aqbean.getCol16();
                            break;
                        case 17:
                            column = (ArrayList) aqbean.getCol17();
                            break;
                        case 18:
                            column = (ArrayList) aqbean.getCol18();
                            break;
                        case 19:
                            column = (ArrayList) aqbean.getCol19();
                            break;
                        case 20:
                            column = (ArrayList) aqbean.getCol20();
                            break;
                    }

                }
                if (column.size() > rowNo) {
                    addbean = (ADDetailsHealperBean) column.get(rowNo);
                    total = total + addbean.getAdamt();
                }
            }
            /*if(col != null && col.equals("col7")){
             
             }*/
        }

        return total;
    }

    public static int getColGrandTotal(ArrayList aqlist, String col, String colName, String nowdedn) {
        AqreportHelperBean aqbean = new AqreportHelperBean();
        ArrayList column = null;
        String subCol = col.substring(3);
        int val = Integer.parseInt(subCol);
        int total = 0;
        ADDetailsHealperBean addbean = null;
        for (int i = 0; i < aqlist.size(); i++) {
            SectionWiseAqBean sectionwiseAq = (SectionWiseAqBean) aqlist.get(i);
            ArrayList arrBill = sectionwiseAq.getAqlistSectionWise();
            for (int k = 0; k < arrBill.size(); k++) {
                if (arrBill.get(k) != null) {
                    aqbean = (AqreportHelperBean) arrBill.get(k);
                    switch (val) {
                        case 3:
                            column = (ArrayList) aqbean.getCol3();
                            break;
                        case 4:
                            column = (ArrayList) aqbean.getCol4();
                            break;
                        case 5:
                            column = (ArrayList) aqbean.getCol5();
                            break;
                        case 6:
                            column = (ArrayList) aqbean.getCol6();
                            break;
                        case 7:
                            column = (ArrayList) aqbean.getCol7();
                            break;
                        case 8:
                            column = (ArrayList) aqbean.getCol8();
                            break;
                        case 9:
                            column = (ArrayList) aqbean.getCol9();
                            break;
                        case 10:
                            column = (ArrayList) aqbean.getCol10();
                            break;
                        case 11:
                            column = (ArrayList) aqbean.getCol11();
                            break;
                        case 12:
                            column = (ArrayList) aqbean.getCol12();
                            break;
                        case 13:
                            column = (ArrayList) aqbean.getCol13();
                            break;
                        case 14:
                            column = (ArrayList) aqbean.getCol14();
                            break;
                        case 15:
                            column = (ArrayList) aqbean.getCol15();
                            break;
                        case 16:
                            column = (ArrayList) aqbean.getCol16();
                            break;
                        case 17:
                            column = (ArrayList) aqbean.getCol17();
                            break;
                        case 18:
                            column = (ArrayList) aqbean.getCol18();
                            break;
                        case 19:
                            column = (ArrayList) aqbean.getCol19();
                            break;
                        case 20:
                            column = (ArrayList) aqbean.getCol20();
                            break;
                    }

                }
                if (colName.equals("BASIC")) {

                    int basic = 0;
                    if (aqbean.getBasic() != null) {
                        basic = Integer.parseInt(aqbean.getBasic());
                    }
                    total = total + basic;

                }

                for (int j = 0; j < column.size(); j++) {
                    addbean = (ADDetailsHealperBean) column.get(j);
                    if (addbean.getAdcode() != null && addbean.getAdcode().equalsIgnoreCase(colName)) {

                        if (nowdedn == null) {
                            total = total + addbean.getAdamt();
                        } else if (addbean.getNowdedn() != null && addbean.getNowdedn().equalsIgnoreCase(nowdedn)) {
                            total = total + addbean.getAdamt();
                        }
                        /*if(addbean.getAdcode().equalsIgnoreCase("LFQ")){
                         
                         }*/
                    }
                }
            }
        }
        return total;
    }

    public static int getColTotal(Object objBill, int end, String col, String colName, String nowdedn) {
        ArrayList arrBill = (ArrayList) objBill;
        AqreportHelperBean aqbean = new AqreportHelperBean();
        ArrayList column = null;
        String subCol = col.substring(3);
        int val = Integer.parseInt(subCol);
        int total = 0;
        ADDetailsHealperBean addbean = null;
        for (int i = 0; i <= end && i < arrBill.size(); i++) {
            if (arrBill.get(i) != null) {
                aqbean = (AqreportHelperBean) arrBill.get(i);
                switch (val) {
                    case 3:
                        column = (ArrayList) aqbean.getCol3();
                        break;
                    case 4:
                        column = (ArrayList) aqbean.getCol4();
                        break;
                    case 5:
                        column = (ArrayList) aqbean.getCol5();
                        break;
                    case 6:
                        column = (ArrayList) aqbean.getCol6();
                        break;
                    case 7:
                        column = (ArrayList) aqbean.getCol7();
                        break;
                    case 8:
                        column = (ArrayList) aqbean.getCol8();
                        break;
                    case 9:
                        column = (ArrayList) aqbean.getCol9();
                        break;
                    case 10:
                        column = (ArrayList) aqbean.getCol10();
                        break;
                    case 11:
                        column = (ArrayList) aqbean.getCol11();
                        break;
                    case 12:
                        column = (ArrayList) aqbean.getCol12();
                        break;
                    case 13:
                        column = (ArrayList) aqbean.getCol13();
                        break;
                    case 14:
                        column = (ArrayList) aqbean.getCol14();
                        break;
                    case 15:
                        column = (ArrayList) aqbean.getCol15();
                        break;
                    case 16:
                        column = (ArrayList) aqbean.getCol16();
                        break;
                    case 17:
                        column = (ArrayList) aqbean.getCol17();
                        break;
                    case 18:
                        column = (ArrayList) aqbean.getCol18();
                        break;
                    case 19:
                        column = (ArrayList) aqbean.getCol19();
                        break;
                    case 20:
                        column = (ArrayList) aqbean.getCol20();
                        break;
                }

            }
            if (colName.equals("BASIC")) {

                int basic = 0;
                if (aqbean.getBasic() != null) {
                    basic = Integer.parseInt(aqbean.getBasic());
                }
                total = total + basic;
            }

            for (int j = 0; j < column.size(); j++) {
                addbean = (ADDetailsHealperBean) column.get(j);
                if (addbean.getAdcode() != null && addbean.getAdcode().equalsIgnoreCase(colName)) {
                    if (nowdedn == null) {
                        total = total + addbean.getAdamt();
                    } else if (addbean.getNowdedn() != null && addbean.getNowdedn().equalsIgnoreCase(nowdedn)) {
                        total = total + addbean.getAdamt();
                    }

                }
            }
        }
        return total;
    }

    public static int getColTotal(Object objBill, int end, String col, int rowNo, String nowdedn) {
        ArrayList arrBill = (ArrayList) objBill;
        AqreportHelperBean aqbean = new AqreportHelperBean();
        ArrayList column = null;
        String subCol = col.substring(3);
        int val = Integer.parseInt(subCol);
        int total = 0;
        ADDetailsHealperBean addbean = null;

        for (int i = 0; i <= end && i < arrBill.size(); i++) {
            if (arrBill.get(i) != null) {
                aqbean = (AqreportHelperBean) arrBill.get(i);
                switch (val) {
                    case 3:
                        column = (ArrayList) aqbean.getCol3();
                        break;
                    case 4:
                        column = (ArrayList) aqbean.getCol4();
                        break;
                    case 5:
                        column = (ArrayList) aqbean.getCol5();
                        break;
                    case 6:
                        column = (ArrayList) aqbean.getCol6();
                        break;
                    case 7:
                        column = (ArrayList) aqbean.getCol7();
                        break;
                    case 8:
                        column = (ArrayList) aqbean.getCol8();
                        break;
                    case 9:
                        column = (ArrayList) aqbean.getCol9();
                        break;
                    case 10:
                        column = (ArrayList) aqbean.getCol10();
                        break;
                    case 11:
                        column = (ArrayList) aqbean.getCol11();
                        break;
                    case 12:
                        column = (ArrayList) aqbean.getCol12();
                        break;
                    case 13:
                        column = (ArrayList) aqbean.getCol13();
                        break;
                    case 14:
                        column = (ArrayList) aqbean.getCol14();
                        break;
                    case 15:
                        column = (ArrayList) aqbean.getCol15();
                        break;
                    case 16:
                        column = (ArrayList) aqbean.getCol16();
                        break;
                    case 17:
                        column = (ArrayList) aqbean.getCol17();
                        break;
                    case 18:
                        column = (ArrayList) aqbean.getCol18();
                        break;
                    case 19:
                        column = (ArrayList) aqbean.getCol19();
                        break;
                    case 20:
                        column = (ArrayList) aqbean.getCol20();
                        break;
                }

            }
            if (column.size() > rowNo) {
                addbean = (ADDetailsHealperBean) column.get(rowNo);
                total = total + addbean.getAdamt();
            }
        }
        return total;
    }

    public String getADCodeHead(String billNo, String ad) throws Exception {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String head = "";

        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            String sql = "Select BT_ID from( (Select AQ_MAST.AQSL_NO from AQ_MAST where AQ_MAST.BILL_NO = '" + billNo + "')"
                    + " AQ_MAST inner join"
                    + " (Select AQ_DTLS.AQSL_NO,AQ_DTLS.AD_AMT,BT_ID from AQ_DTLS where  AD_CODE ='" + ad + "') AQ_DTLS"
                    + " on AQ_MAST.AQSL_NO = AQ_DTLS.AQSL_NO ) GROUP BY BT_ID";
            rs = st.executeQuery(sql);
            if (rs.next()) {
                head = rs.getString("BT_ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return head;
    }

    public static int getColFinalTotal(Object objBill, int end, String col) {
        ArrayList arrBill = (ArrayList) objBill;
        AqreportHelperBean aqbean = new AqreportHelperBean();
        ArrayList column = null;
        String subCol = col.substring(3);
        int val = Integer.parseInt(subCol);
        int total = 0;
        ADDetailsHealperBean addbean = null;

        for (int i = 0; i <= end && i < arrBill.size(); i++) {
            aqbean = (AqreportHelperBean) arrBill.get(i);
            if (arrBill.get(i) != null) {
                switch (val) {
                    case 3:
                        column = (ArrayList) aqbean.getCol3();
                        break;
                    case 4:
                        column = (ArrayList) aqbean.getCol4();
                        break;
                    case 5:
                        column = (ArrayList) aqbean.getCol5();
                        break;
                    case 6:
                        column = (ArrayList) aqbean.getCol6();
                        break;
                    case 7:
                        column = (ArrayList) aqbean.getCol7();
                        break;
                    case 8:
                        column = (ArrayList) aqbean.getCol8();
                        break;
                    case 9:
                        column = (ArrayList) aqbean.getCol9();
                        break;
                    case 10:
                        column = (ArrayList) aqbean.getCol10();
                        break;
                    case 11:
                        column = (ArrayList) aqbean.getCol11();
                        break;
                    case 12:
                        column = (ArrayList) aqbean.getCol12();
                        break;
                    case 13:
                        column = (ArrayList) aqbean.getCol13();
                        break;
                    case 14:
                        column = (ArrayList) aqbean.getCol14();
                        break;
                    case 15:
                        column = (ArrayList) aqbean.getCol15();
                        break;
                    case 16:
                        column = (ArrayList) aqbean.getCol16();
                        break;
                    case 17:
                        column = (ArrayList) aqbean.getCol17();
                        break;
                    case 18:
                        column = (ArrayList) aqbean.getCol18();
                        break;
                    case 19:
                        column = (ArrayList) aqbean.getCol19();
                        break;
                    case 20:
                        column = (ArrayList) aqbean.getCol20();
                        break;

                }
            }
            if (val == 3) {
                int basic = 0;
                if (aqbean.getBasic() != null) {
                    basic = Integer.parseInt(aqbean.getBasic());
                }
                total = total + basic;

            }

            for (int j = 0; j < column.size(); j++) {
                addbean = (ADDetailsHealperBean) column.get(j);
                if (val == 19) {
                    if (addbean.getAdcode().equals("TOTDEN") || addbean.getAdcode().equals("NETPAY")) {
                        total = total + addbean.getAdamt();
                    }
                } else if (val == 20) {
                    if (addbean.getAdcode().equals("NETBALANCE") || addbean.getAdcode().equals("NETPAY")) {
                        total = total + addbean.getAdamt();
                    }
                } else {
                    total = total + addbean.getAdamt();
                }
            }
        }

        return total;
    }

    public String getDPHead(String billNo, int aqMonth, int aqYear) throws Exception {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String dphead = "";

        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            String sql = "Select BT_ID from( (Select AQ_MAST.AQSL_NO from AQ_MAST where AQ_MAST.BILL_NO = '" + billNo + "'AND AQ_MAST.aq_month=" + aqMonth + " AND AQ_MAST.aq_year=" + aqYear + " )"
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
            DataBaseFunctions.closeSqlObjects(rs, st, con);
        }
        return dphead;
    }

    public int getGPFAmount(String billId, String btId) {
        Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        int sumOfAmt = 0;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT SUM(AD_AMT) amt FROM (SELECT AD_AMT,AD_CODE,BT_ID FROM(SELECT AQSL_NO FROM AQ_MAST WHERE BILL_NO=" + billId + ")AQ_MAST "
                    + "INNER JOIN AQ_DTLS ON AQ_MAST.AQSL_NO = AQ_DTLS.AQSL_NO) T1 WHERE (AD_CODE='GPF' OR AD_CODE='GA') AND BT_ID='" + btId + "'");
            if (rs.next()) {
                sumOfAmt = rs.getInt("amt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st, con);
        }
        return sumOfAmt;
    }

    public String isEmployeeRegular(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isRegular = "N";
        Date dateofentryingov = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            con = this.dataSource.getConnection();

            Date payRevDt = sdf.parse("2016-01-01");

            String sql = "SELECT IS_REGULAR,DOE_GOV FROM EMP_MAST WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("DOE_GOV") != null && !rs.getString("DOE_GOV").equals("")) {
                    dateofentryingov = sdf.parse(rs.getString("DOE_GOV"));
                }
                if (rs.getString("IS_REGULAR") != null && (rs.getString("IS_REGULAR").equals("Y") || rs.getString("IS_REGULAR").equals("W") || rs.getString("IS_REGULAR").equals("G"))) {
                    isRegular = "Y";
                } else {
                    isRegular = rs.getString("IS_REGULAR");
                }
            }

            /*if(isRegular.equals("Y")){
             DataBaseFunctions.closeSqlObjects(rs, pst);
                
             sql = "SELECT count(*) cnt FROM emp_regularization_contractual WHERE EMP_ID=?";
             pst = con.prepareStatement(sql);
             pst.setString(1,empid);
             rs = pst.executeQuery();
             if(rs.next()){
             if(rs.getInt("cnt") > 0){
             isRegular = "N";
             }
             }
             }*/
            if (isRegular.equals("Y")) {
                DataBaseFunctions.closeSqlObjects(rs, pst);

                sql = "SELECT count(*) cnt FROM emp_regularization_contractual WHERE EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, empid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    if (rs.getInt("cnt") > 0) {
                        if (dateofentryingov != null && !dateofentryingov.equals("")) {
                            if (dateofentryingov.compareTo(payRevDt) > 0) {
                                isRegular = "Y";
                            } else {
                                isRegular = "N";
                            }
                        } else {
                            isRegular = "N";
                        }
                    }
                }
            }

            if (isRegular.equals("Y")) {
                DataBaseFunctions.closeSqlObjects(rs, pst);

                sql = "SELECT pay_revision_option.emp_id,is_previous_cont6 FROM pay_revision_option" +
                     " inner join emp_regularization_contractual on pay_revision_option.emp_id=emp_regularization_contractual.emp_id" +
                     " WHERE pay_revision_option.EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, empid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    if (rs.getString("is_previous_cont6") != null && rs.getString("is_previous_cont6").equals("Y")) {
                        isRegular = "Y";
                    }else{
                        isRegular = "N";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isRegular;
    }
}
