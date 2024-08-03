/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.billbrowser;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import hrms.common.AqFunctionalities;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.Numtowordconvertion;
import hrms.common.PayrollCommonFunction;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.BytransferDetails;
import hrms.model.payroll.aqreport.AqreportBean;
import hrms.model.payroll.arrear.ArrAqMastModel;
import hrms.model.payroll.billbrowser.AcquaintanceBean;
import hrms.model.payroll.billbrowser.AcquaintanceDtlBean;
import hrms.model.payroll.billbrowser.AllowanceDetailsObjClass;
import hrms.model.payroll.billbrowser.BillBean;
import hrms.model.payroll.billbrowser.BillChartOfAccount;
import hrms.model.payroll.billbrowser.BillConfigObj;
import hrms.model.payroll.billbrowser.BillObjectHead;
import hrms.model.payroll.billbrowser.DeductionDetailsObjClass;
import hrms.model.payroll.billbrowser.LtaBeanForAG;
import hrms.model.payroll.schedule.ADDetailsHealperBean;
import hrms.model.payroll.schedule.AqreportHelperBean;
import hrms.model.payroll.schedule.Schedule;
import hrms.model.payroll.schedule.SectionWiseAqBean;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 * @author Manas Jena
 */
public class AqReportDAOImpl implements AqReportDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    protected AqFunctionalities aqfunctionalities;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    public void setAqfunctionalities(AqFunctionalities aqfunctionalities) {
        this.aqfunctionalities = aqfunctionalities;
    }

    @Override
    public Map<String, List> getAllColumnNameList(String billno, String month, String year) {
        Map<String, List> colNameObj = new HashMap<String, List>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            String tabName = AqFunctionalities.getAQBillDtlsTable(Integer.parseInt(month), Integer.parseInt(year));
            ps = con.prepareStatement("select ad_code, rep_col, ad_desc from " + tabName + " aq_dtls "
                    + " inner join aq_mast on aq_dtls.aqsl_no=aq_mast.aqsl_no "
                    + " where bill_no=?  and aq_mast.aq_year=? and aq_mast.aq_month=? and ad_amt>0 "
                    + " group by ad_code, rep_col, ad_desc order by rep_col, ad_desc");
            ps.setInt(1, Integer.parseInt(billno));
            ps.setInt(2, Integer.parseInt(year));
            ps.setInt(3, Integer.parseInt(month));

            rs = ps.executeQuery();

            List colList = new ArrayList();
            String repCol = "";
            boolean added = true;
            int firsttime = 0;
            while (rs.next()) {

                if (repCol.equalsIgnoreCase(rs.getString("rep_col"))) {
                    colList.add(rs.getString("ad_desc"));
                } else if (!repCol.equalsIgnoreCase(rs.getString("rep_col"))) {
                    if (firsttime == 0) {
                        firsttime = 1;
                        colList = new ArrayList();
                    } else {
                        added = true;
                        colNameObj.put(repCol, colList);
                        colList = new ArrayList();
                        added = false;
                    }
                    added = false;
                    repCol = rs.getString("rep_col");
                    colList.add(rs.getString("ad_desc"));

                }

            }

            if (added == false) {
                colNameObj.put(repCol, colList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return colNameObj;
    }

    @Override
    public List getDeductionGrandAbstract(String billNo, String month, String year) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        BytransferDetails bt = new BytransferDetails();
        try {
            con = dataSource.getConnection();
            String tabName = AqFunctionalities.getAQBillDtlsTable(Integer.parseInt(month), Integer.parseInt(year));
            ps = con.prepareStatement("select ad_desc, sum(ad_amt) ad_amt from aq_mast \n"
                    + "inner join " + tabName + " aq_dtls on aq_mast.aqsl_no=aq_dtls.aqsl_no\n"
                    + "where bill_no=? and aq_mast.aq_year=? and aq_mast.aq_month=? and ad_amt>0 and ad_type='D'\n"
                    + "group by ad_desc order by ad_desc");
            ps.setInt(1, Integer.parseInt(billNo));
            ps.setInt(2, Integer.parseInt(year));
            ps.setInt(3, Integer.parseInt(month));
            rs = ps.executeQuery();
            while (rs.next()) {
                bt = new BytransferDetails();
                bt.setScheduleName(rs.getString("ad_desc"));
                bt.setAmount(rs.getDouble("ad_amt"));
                li.add(bt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public String getADCodeDesc(String adcode) {
        String ad_desc = "";
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        Statement stmt1 = null;
        ResultSet rs1 = null;
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            stmt1 = con.createStatement();

            String sql = "SELECT AD_DESC from g_ad_list where ad_code_name='" + adcode + "'";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                ad_desc = rs.getString("AD_DESC");
            } else {
                String sql1 = "select loan_name from g_loan where loan_tp='" + adcode + "'";
                rs1 = stmt1.executeQuery(sql1);
                if (rs1.next()) {
                    ad_desc = rs1.getString("loan_name");
                } else {
                    ad_desc = adcode;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(rs1, stmt1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ad_desc;

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
    public ArrayList<AcquaintanceBean> getAcquaintance1(String billNo, String aqDtlsTableName) {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        ArrayList<AcquaintanceBean> acquaintanceList = new ArrayList();
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();

            /*res = stmt.executeQuery("SELECT AQ_MAST.AQSL_NO,AQ_MAST.EMP_CODE,AQ_MAST.EMP_NAME,AQ_MAST.CUR_DESG,AQ_MAST.PAY_SCALE,AQ_MAST.BANK_ACC_NO,AQ_MAST.CUR_BASIC,AQ_MAST.GPF_ACC_NO FROM"
             + " (SELECT * FROM AQ_MAST WHERE AQ_MAST.BILL_NO='" + billNo + "')AQ_MAST"
             + " INNER JOIN  (SELECT " + aqDtlsTableName + ".EMP_CODE FROM " + aqDtlsTableName + " WHERE (" + aqDtlsTableName + ".SCHEDULE='PVTL'"
             + " OR " + aqDtlsTableName + ".SCHEDULE='PVTD') AND " + aqDtlsTableName + ".AD_TYPE='D' AND AD_AMT>0 group by EMP_CODE) AQDTLS  ON"
             + " AQDTLS.EMP_CODE=AQ_MAST.EMP_CODE ORDER BY POST_SL_NO");*/
            res = stmt.executeQuery("SELECT AQ_MAST.AQSL_NO,AQ_MAST.EMP_CODE,AQ_MAST.EMP_NAME,AQ_MAST.CUR_DESG,AQ_MAST.PAY_SCALE,AQ_MAST.BANK_ACC_NO,AQ_MAST.CUR_BASIC,AQ_MAST.GPF_ACC_NO FROM"
                    + " (SELECT * FROM AQ_MAST WHERE AQ_MAST.BILL_NO='" + billNo + "')AQ_MAST"
                    + " INNER JOIN "
                    + " (SELECT EMP_CODE FROM("
                    + " (SELECT AQSL_NO FROM AQ_MAST WHERE AQ_MAST.BILL_NO='" + billNo + "')AQ_MAST"
                    + " INNER JOIN " + aqDtlsTableName + " ON AQ_MAST.AQSL_NO = " + aqDtlsTableName + ".AQSL_NO)AQDTLS WHERE (SCHEDULE='PVTL' OR SCHEDULE='PVTD') AND AD_TYPE='D' AND AD_AMT>0 group by EMP_CODE)T1"
                    + " ON AQ_MAST.EMP_CODE=T1.EMP_CODE ORDER BY POST_SL_NO");
            while (res.next()) {
                AcquaintanceBean aqBean = new AcquaintanceBean();
                aqBean.setAqslno(res.getString("AQSL_NO"));
                aqBean.setEmpname(res.getString("EMP_NAME"));
                aqBean.setEmpcode(res.getString("EMP_CODE"));
                aqBean.setGpfaccno(res.getString("GPF_ACC_NO"));
                aqBean.setCurdesg(res.getString("CUR_DESG"));
                aqBean.setBankaccno(res.getString("BANK_ACC_NO"));
                aqBean.setPayscale(res.getString("PAY_SCALE"));
                aqBean.setCurbasic(res.getInt("CUR_BASIC"));
                acquaintanceList.add(aqBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return acquaintanceList;
    }

    @Override
    public ArrayList<AcquaintanceBean> getAcquaintance(String billNo) {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        ArrayList<AcquaintanceBean> acquaintanceList = new ArrayList();
        try {
            con = repodataSource.getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery("SELECT AQSL_NO, emp_code, gpf_acc_no, EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,GPF_ACC_NO,gross_amt,ded_amt,pvt_ded_amt FROM AQ_MAST WHERE  BILL_NO='" + billNo + "' AND EMP_NAME IS NOT NULL order by post_sl_no");
            while (res.next()) {
                AcquaintanceBean aqBean = new AcquaintanceBean();
                if (res.getString("emp_code") != null && !res.getString("emp_code").equals("")) {
                    boolean depstatusBoole = false;
                    //depstatusBoole=stopSalaryForPayHeldUp(res.getString("emp_code"));
                    if (depstatusBoole) {
                        aqBean.setEmployeeDepStatus("HELDUP");
                    } else {
                        aqBean.setEmployeeDepStatus("NA");
                    }
                } else {
                    aqBean.setEmployeeDepStatus("NA");
                }

                aqBean.setAqslno(res.getString("AQSL_NO"));
                aqBean.setEmpcode(res.getString("emp_code"));
                aqBean.setGpfaccno(res.getString("gpf_acc_no"));
                aqBean.setEmpname(res.getString("EMP_NAME"));
                aqBean.setGpfaccno(res.getString("GPF_ACC_NO"));
                aqBean.setCurdesg(res.getString("CUR_DESG"));
                aqBean.setBankaccno(res.getString("BANK_ACC_NO"));
                aqBean.setPayscale(res.getString("PAY_SCALE"));
                aqBean.setCurbasic(res.getInt("CUR_BASIC"));
                aqBean.setGrossamt(res.getInt("gross_amt"));
                aqBean.setDedamt(res.getInt("ded_amt"));
                aqBean.setPrvdedamt(res.getInt("pvt_ded_amt"));
                acquaintanceList.add(aqBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return acquaintanceList;
    }

    public boolean stopSalaryForPayHeldUp(String empId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean stopSalary = false;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select * from emp_pay_heldup where emp_id=? and to_date is null ");
            ps.setString(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {
                stopSalary = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return stopSalary;
    }

    @Override
    public ArrayList getAcquaintanceDtlDed(String empcode, String aqslno, String aqDtlsTableName) {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        ArrayList aqDtl = new ArrayList();
        try {
            con = repodataSource.getConnection();
            stmt = con.createStatement();
            String queryClump = "SELECT AD_AMT,AD_DESC,AD_CODE,SCHEDULE,REP_COL FROM " + aqDtlsTableName + " WHERE (" + aqDtlsTableName + ".SCHEDULE='PVTL' OR"
                    + " " + aqDtlsTableName + ".SCHEDULE='PVTD') AND EMP_CODE='" + empcode + "' AND " + aqDtlsTableName + ".AQSL_NO = '" + aqslno + "'"
                    + " AND AD_AMT>0";
            res = stmt.executeQuery(queryClump);
            while (res.next()) {
                AcquaintanceDtlBean aqdtl = new AcquaintanceDtlBean();
                aqdtl.setAdcode(res.getString("AD_CODE"));
                aqdtl.setRepcol(res.getInt("REP_COL"));
                aqdtl.setAdamt(res.getInt("AD_AMT"));
                aqDtl.add(aqDtl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aqDtl;
    }

    @Override
    public ArrayList getAcquaintanceDtl(String aqslno, String aqDtlsTableName) {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        ArrayList aqDtllist = new ArrayList();
        try {
            con = repodataSource.getConnection();
            stmt = con.createStatement();
            String queryClump = "select * from (select cnt,coalesce(REP_COL,0) rep_col,tmep4.AD_CODE,AD_AMT,SCHEDULE,tmep4.NOW_DEDN,AD_TYPE,coalesce(sl_no,0) slno,REF_DESC from(select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no from "
                    + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDtlsTableName + " WHERE AQSL_NO='" + aqslno + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt = 1)tmep4 "
                    + "left outer join (SELECT AD_CODE,REF_DESC,NOW_DEDN FROM " + aqDtlsTableName + " WHERE AQSL_NO='" + aqslno + "') temp2 on "
                    + "tmep4.ad_code = temp2.ad_code and tmep4.now_dedn = temp2.now_dedn "
                    + "union "
                    + "select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no,''::text from "
                    + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDtlsTableName + " WHERE AQSL_NO='" + aqslno + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt > 1 ) view3 order by SLNO ";
            res = stmt.executeQuery(queryClump);
            while (res.next()) {
                AcquaintanceDtlBean aqdtl = new AcquaintanceDtlBean();
                aqdtl.setAdcode(res.getString("AD_CODE"));
                aqdtl.setRepcol(res.getInt("REP_COL"));
                aqdtl.setAdamt(res.getInt("AD_AMT"));
                aqDtllist.add(aqdtl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aqDtllist;
    }

    @Override
    public BillConfigObj getBillConfig(String billno) {
        BillConfigObj objBill = new BillConfigObj();
        ResultSet rs = null;
        Statement st = null;
        String col6Desc[] = null;
        String col7Desc[] = null;
        String col16Desc[] = null;
        String col17Desc[] = null;
        String col18Desc[] = null;
        String offCode = "";
        String pOffCode = "";
        String billgroupId = "";
        Connection con = null;
        try {
            /*
             *  get aqreport configuration from database
             * 
             */
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("select p_off_code,off_code, bill_group_id from bill_mast where bill_no='" + billno + "'");
            if (rs.next()) {
                offCode = rs.getString("off_code");
                billgroupId = rs.getString("bill_group_id");
                pOffCode = rs.getString("p_off_code");
            }

            DataBaseFunctions.closeSqlObjects(rs, st);
            st = con.createStatement();
            rs = st.executeQuery("SELECT CONFIGURED_LVL,show_in_aquitance FROM BILL_GROUP_MASTER WHERE BILL_GROUP_ID='" + billgroupId + "'");
            if (rs.next()) {
                String level = rs.getString("CONFIGURED_LVL");
                objBill.setCol6List(getColConfiguredDtata(con, offCode, billgroupId, 6, level));
                objBill.setCol7List(getColConfiguredDtata(con, offCode, billgroupId, 7, level));
                objBill.setCol16List(getColConfiguredDtata(con, offCode, billgroupId, 16, level));
                objBill.setCol17List(getColConfiguredDtata(con, offCode, billgroupId, 17, level));
                objBill.setCol18List(getColConfiguredDtata(con, offCode, billgroupId, 18, level));
                objBill.setNotToPrintInAquitance(rs.getString("show_in_aquitance"));
                if (pOffCode != null && !pOffCode.equals("")) {
                    objBill.setCol7List(getColConfiguredDtata(con, pOffCode, billgroupId, 7, level));
                }
            }
            DataBaseFunctions.closeSqlObjects(rs, st);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return objBill;

    }

    public BillConfigObj getBillConfigForAQReportExcel(String billno) {
        BillConfigObj objBill = new BillConfigObj();
        ResultSet rs = null;
        Statement st = null;
        String col6Desc[] = null;
        String col7Desc[] = null;
        String col16Desc[] = null;
        String col17Desc[] = null;
        String col18Desc[] = null;
        String offCode = "";
        String billgroupId = "";
        Connection con = null;
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
                objBill.setCol6List(getColConfiguredDtataForAqReportExcel(con, offCode, billgroupId, 6, level));
                objBill.setCol7List(getColConfiguredDtataForAqReportExcel(con, offCode, billgroupId, 7, level));
                objBill.setCol16List(getColConfiguredDtataForAqReportExcel(con, offCode, billgroupId, 16, level));
                objBill.setCol17List(getColConfiguredDtataForAqReportExcel(con, offCode, billgroupId, 17, level));
                objBill.setCol18List(getColConfiguredDtataForAqReportExcel(con, offCode, billgroupId, 18, level));
            }
            DataBaseFunctions.closeSqlObjects(rs, st);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return objBill;

    }

    public static String[] getColConfiguredDtata(Connection con, String offCode, String billgroupId, int colNumber, String configLevel) {
        ResultSet rs = null;
        Statement st = null;
        String colDesc[] = null;
        int coli = 0;
        String sql = "";
        try {
            st = con.createStatement();

            if (configLevel != null && !configLevel.equals("")) {
                if (configLevel.equalsIgnoreCase("B")) {
                    sql = "SELECT AD_CODE_NAME,ROW_NUMBER FROM (SELECT AD_CODE,ROW_NUMBER FROM BILL_CONFIGURATION WHERE OFF_CODE='" + offCode + "' AND BILL_GROUP_ID=" + billgroupId + " AND COL_NUMBER=" + colNumber + ") BILL_CONFIGURATION "
                            + "INNER JOIN G_AD_LIST ON "
                            + "BILL_CONFIGURATION.AD_CODE=G_AD_LIST.AD_CODE ORDER BY ROW_NUMBER";
                } else {
                    sql = "SELECT AD_CODE_NAME,ROW_NUMBER FROM (SELECT AD_CODE,ROW_NUMBER FROM BILL_CONFIGURATION WHERE OFF_CODE='" + offCode + "' AND COL_NUMBER=" + colNumber + " AND BILL_GROUP_ID IS NULL) BILL_CONFIGURATION "
                            + "INNER JOIN G_AD_LIST ON "
                            + "BILL_CONFIGURATION.AD_CODE=G_AD_LIST.AD_CODE ORDER BY ROW_NUMBER";
                }
            }
            if (sql != null && !sql.equals("")) {
                rs = st.executeQuery(sql);

                while (rs.next()) {
                    if (coli == 0) {
                        colDesc = new String[11];
                    }
                    colDesc[coli] = rs.getString("AD_CODE_NAME");
                    coli++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
        }
        return colDesc;
    }

    public static String[] getColConfiguredDtataForAqReportExcel(Connection con, String offCode, String billgroupId, int colNumber, String configLevel) {
        ResultSet rs = null;
        Statement st = null;
        String colDesc[] = null;
        int coli = 0;
        String sql = "";
        try {
            st = con.createStatement();

            if (configLevel != null && !configLevel.equals("")) {
                if (configLevel.equalsIgnoreCase("B")) {
                    sql = "SELECT AD_CODE_NAME,ROW_NUMBER FROM (SELECT AD_CODE,ROW_NUMBER FROM BILL_CONFIGURATION WHERE OFF_CODE='" + offCode + "' AND BILL_GROUP_ID=" + billgroupId + " AND COL_NUMBER=" + colNumber + ") BILL_CONFIGURATION "
                            + "INNER JOIN G_AD_LIST ON "
                            + "BILL_CONFIGURATION.AD_CODE=G_AD_LIST.AD_CODE ORDER BY AD_CODE_NAME";
                } else {
                    sql = "SELECT AD_CODE_NAME,ROW_NUMBER FROM (SELECT AD_CODE,ROW_NUMBER FROM BILL_CONFIGURATION WHERE OFF_CODE='" + offCode + "' AND COL_NUMBER=" + colNumber + " AND BILL_GROUP_ID IS NULL) BILL_CONFIGURATION "
                            + "INNER JOIN G_AD_LIST ON "
                            + "BILL_CONFIGURATION.AD_CODE=G_AD_LIST.AD_CODE ORDER BY AD_CODE_NAME";
                }
            }
            if (sql != null && !sql.equals("")) {
                rs = st.executeQuery(sql);

                while (rs.next()) {
                    if (coli == 0) {
                        colDesc = new String[11];
                    }
                    colDesc[coli] = rs.getString("AD_CODE_NAME");
                    coli++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
        }
        return colDesc;
    }

    @Override
    public BillChartOfAccount getBillChartOfAccount(String billNo) {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        BillChartOfAccount bcacc = new BillChartOfAccount();
        try {
            con = repodataSource.getConnection();
            stmt = con.createStatement();
            String qry = " SELECT vch_no,vch_date,EMPNAME,post, bill_mast.bill_no,BEN_REF_NO,TOKEN_NO,OFF_EN,DIST_NAME,DDO_CODE,BILL_MAST.MAJOR_HEAD, BILL_MAST.MAJOR_HEAD_DESC,BILL_MAST.BILL_DESC, BILL_MAST.PLAN, POST_TYPE, BILL_MAST.SECTOR,SECTOR_DESC, BILL_MAST.SUB_MAJOR_HEAD, "
                    + " BILL_MAST.SUB_MAJOR_HEAD_DESC, BILL_MAST.MINOR_HEAD, BILL_MAST.MINOR_HEAD_DESC, BILL_MAST.SUB_MINOR_HEAD1, BILL_MAST.SUB_MINOR_HEAD1_DESC, "
                    + " BILL_MAST.SUB_MINOR_HEAD2, BILL_MAST.SUB_MINOR_HEAD2_DESC, BILL_MAST.SUB_MINOR_HEAD3, BILL_MAST.BILL_TYPE,DEMAND_NO FROM( "
                    + " SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,post, vch_no,vch_date,bill_no,BEN_REF_NO,TOKEN_NO,G_OFFICE.OFF_EN,G_OFFICE.DIST_CODE,BILL_MAST.MAJOR_HEAD, BILL_MAST.MAJOR_HEAD_DESC, BILL_DESC,BILL_MAST.PLAN, BILL_MAST.SECTOR, BILL_MAST.SUB_MAJOR_HEAD, "
                    + " BILL_MAST.SUB_MAJOR_HEAD_DESC, BILL_MAST.MINOR_HEAD, BILL_MAST.MINOR_HEAD_DESC, BILL_MAST.SUB_MINOR_HEAD1, BILL_MAST.SUB_MINOR_HEAD1_DESC, "
                    + " BILL_MAST.SUB_MINOR_HEAD2, BILL_MAST.SUB_MINOR_HEAD2_DESC, BILL_MAST.SUB_MINOR_HEAD3, BILL_MAST.BILL_TYPE,BILL_MAST.DDO_CODE,DEMAND_NO FROM ( "
                    + " SELECT vch_no,vch_date,bill_no,BEN_REF_NO,TOKEN_NO,BILL_MAST.MAJOR_HEAD, BILL_MAST.MAJOR_HEAD_DESC, BILL_DESC,BILL_MAST.PLAN, BILL_MAST.SECTOR, BILL_MAST.SUB_MAJOR_HEAD, "
                    + " BILL_MAST.SUB_MAJOR_HEAD_DESC, BILL_MAST.MINOR_HEAD, BILL_MAST.MINOR_HEAD_DESC, BILL_MAST.SUB_MINOR_HEAD1, BILL_MAST.SUB_MINOR_HEAD1_DESC, "
                    + " BILL_MAST.SUB_MINOR_HEAD2, BILL_MAST.SUB_MINOR_HEAD2_DESC, BILL_MAST.SUB_MINOR_HEAD3, BILL_MAST.BILL_TYPE,DDO_CODE,DEMAND_NO,OFF_CODE FROM BILL_MAST WHERE BILL_NO='" + billNo + "') BILL_MAST "
                    + " LEFT OUTER JOIN G_OFFICE ON BILL_MAST.OFF_CODE=G_OFFICE.OFF_CODE "
                    + " LEFT OUTER JOIN G_POST ON G_OFFICE.ddo_post=G_POST.POST_CODE "
                    + " LEFT OUTER JOIN EMP_MAST ON G_OFFICE.ddo_hrmsid=EMP_MAST.EMP_ID) BILL_MAST  "
                    + " LEFT OUTER JOIN G_DISTRICT ON BILL_MAST.DIST_CODE=G_DISTRICT.DIST_CODE "
                    + " LEFT OUTER JOIN G_SECTOR ON BILL_MAST.SECTOR=G_SECTOR.SECTOR_CODE "
                    + " LEFT OUTER JOIN G_POST_TYPE ON BILL_MAST.PLAN=G_POST_TYPE.POST_CODE";

            res = stmt.executeQuery(qry);
            if (res.next()) {

                bcacc.setBenRefNo(res.getString("BEN_REF_NO"));
                bcacc.setTokenNo(res.getString("TOKEN_NO"));

                bcacc.setBilldesc(res.getString("BILL_DESC"));
                bcacc.setDdoName(res.getString("DDO_CODE"));
                bcacc.setDdoEmpName(res.getString("EMPNAME"));
                bcacc.setDdoPostName(res.getString("post"));
                bcacc.setOffName(res.getString("OFF_EN"));
                bcacc.setDistrict(res.getString("DIST_NAME"));
                bcacc.setMajorHead(res.getString("MAJOR_HEAD"));
                bcacc.setMajorHeadDesc(res.getString("MAJOR_HEAD_DESC"));
                bcacc.setPlan(res.getString("PLAN"));
                bcacc.setSector(res.getString("SECTOR"));
                bcacc.setPlanName(res.getString("POST_TYPE"));
                bcacc.setSectorName(res.getString("SECTOR_DESC"));
                bcacc.setSubMajorHead(res.getString("SUB_MAJOR_HEAD"));
                bcacc.setSubMajorHeadDesc(res.getString("SUB_MAJOR_HEAD_DESC"));
                bcacc.setMinorHead(res.getString("MINOR_HEAD"));
                bcacc.setMinorHeadDesc(res.getString("MINOR_HEAD_DESC"));
                bcacc.setSubMinorHead1(res.getString("SUB_MINOR_HEAD1"));
                bcacc.setSubMinorHeadDesc1(res.getString("SUB_MINOR_HEAD1_DESC"));
                bcacc.setSubMinorHead2(res.getString("SUB_MINOR_HEAD2"));
                bcacc.setSubMinorHeadDesc2(res.getString("SUB_MINOR_HEAD2_DESC"));
                if (res.getString("SUB_MINOR_HEAD3") != null && !res.getString("SUB_MINOR_HEAD3").equals("")) {
                    if (res.getString("SUB_MINOR_HEAD3").equals("2")) {
                        bcacc.setSubMinorHead3("Charge");
                    } else {
                        bcacc.setSubMinorHead3("Voted");
                    }
                } else {
                    bcacc.setSubMinorHead3("");
                }

                bcacc.setBillType(res.getString("BILL_TYPE"));
                bcacc.setDemandNo(res.getString("DEMAND_NO"));

                bcacc.setBillid(res.getString("bill_no"));
                bcacc.setVoucherNo(res.getString("vch_no"));
                bcacc.setVoucherDate(CommonFunctions.getFormattedOutputDate1(res.getDate("vch_date")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return bcacc;
    }

    @Override
    public BillChartOfAccount getBillChartOfAccountDHE(String billNo) {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        BillChartOfAccount bcacc = new BillChartOfAccount();
        try {
            con = repodataSource.getConnection();
            stmt = con.createStatement();
            String qry = " SELECT vch_no,vch_date,EMPNAME,post, bill_mast.bill_no,BEN_REF_NO,TOKEN_NO,OFF_EN,DIST_NAME,DDO_CODE,BILL_MAST.MAJOR_HEAD, BILL_MAST.MAJOR_HEAD_DESC,BILL_MAST.BILL_DESC, BILL_MAST.PLAN, POST_TYPE, BILL_MAST.SECTOR,SECTOR_DESC, BILL_MAST.SUB_MAJOR_HEAD,\n"
                    + " BILL_MAST.SUB_MAJOR_HEAD_DESC, BILL_MAST.MINOR_HEAD, BILL_MAST.MINOR_HEAD_DESC, BILL_MAST.SUB_MINOR_HEAD1, BILL_MAST.SUB_MINOR_HEAD1_DESC,\n"
                    + " BILL_MAST.SUB_MINOR_HEAD2, BILL_MAST.SUB_MINOR_HEAD2_DESC, BILL_MAST.SUB_MINOR_HEAD3, BILL_MAST.BILL_TYPE,DEMAND_NO FROM(\n"
                    + " SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,post, vch_no,vch_date,bill_no,BEN_REF_NO,TOKEN_NO,G_OFFICE.OFF_EN,G_OFFICE.DIST_CODE,BILL_MAST.MAJOR_HEAD, BILL_MAST.MAJOR_HEAD_DESC, BILL_DESC,BILL_MAST.PLAN, BILL_MAST.SECTOR, BILL_MAST.SUB_MAJOR_HEAD,\n"
                    + " BILL_MAST.SUB_MAJOR_HEAD_DESC, BILL_MAST.MINOR_HEAD, BILL_MAST.MINOR_HEAD_DESC, BILL_MAST.SUB_MINOR_HEAD1, BILL_MAST.SUB_MINOR_HEAD1_DESC,\n"
                    + " BILL_MAST.SUB_MINOR_HEAD2, BILL_MAST.SUB_MINOR_HEAD2_DESC, BILL_MAST.SUB_MINOR_HEAD3, BILL_MAST.BILL_TYPE,BILL_MAST.DDO_CODE,DEMAND_NO FROM (\n"
                    + " SELECT vch_no,vch_date,bill_no,BEN_REF_NO,TOKEN_NO,BILL_MAST.MAJOR_HEAD, BILL_MAST.MAJOR_HEAD_DESC, BILL_DESC,BILL_MAST.PLAN, BILL_MAST.SECTOR, BILL_MAST.SUB_MAJOR_HEAD,\n"
                    + " BILL_MAST.SUB_MAJOR_HEAD_DESC, BILL_MAST.MINOR_HEAD, BILL_MAST.MINOR_HEAD_DESC, BILL_MAST.SUB_MINOR_HEAD1, BILL_MAST.SUB_MINOR_HEAD1_DESC,\n"
                    + " BILL_MAST.SUB_MINOR_HEAD2, BILL_MAST.SUB_MINOR_HEAD2_DESC, BILL_MAST.SUB_MINOR_HEAD3, BILL_MAST.BILL_TYPE,DDO_CODE,DEMAND_NO,P_OFF_CODE FROM BILL_MAST WHERE BILL_NO='" + billNo + "') BILL_MAST\n"
                    + " LEFT OUTER JOIN G_OFFICE ON BILL_MAST.P_OFF_CODE=G_OFFICE.OFF_CODE\n"
                    + " LEFT OUTER JOIN G_POST ON G_OFFICE.ddo_post=G_POST.POST_CODE\n"
                    + " LEFT OUTER JOIN EMP_MAST ON G_OFFICE.ddo_hrmsid=EMP_MAST.EMP_ID) BILL_MAST \n"
                    + " LEFT OUTER JOIN G_DISTRICT ON BILL_MAST.DIST_CODE=G_DISTRICT.DIST_CODE\n"
                    + " LEFT OUTER JOIN G_SECTOR ON BILL_MAST.SECTOR=G_SECTOR.SECTOR_CODE\n"
                    + " LEFT OUTER JOIN G_POST_TYPE ON BILL_MAST.PLAN=G_POST_TYPE.POST_CODE";

            res = stmt.executeQuery(qry);
            if (res.next()) {

                bcacc.setBenRefNo(res.getString("BEN_REF_NO"));
                bcacc.setTokenNo(res.getString("TOKEN_NO"));

                bcacc.setBilldesc(res.getString("BILL_DESC"));
                bcacc.setDdoName(res.getString("DDO_CODE"));
                bcacc.setDdoEmpName(res.getString("EMPNAME"));
                bcacc.setDdoPostName(res.getString("post"));
                bcacc.setOffName(res.getString("OFF_EN"));
                bcacc.setDistrict(res.getString("DIST_NAME"));
                bcacc.setMajorHead(res.getString("MAJOR_HEAD"));
                bcacc.setMajorHeadDesc(res.getString("MAJOR_HEAD_DESC"));
                bcacc.setPlan(res.getString("PLAN"));
                bcacc.setSector(res.getString("SECTOR"));
                bcacc.setPlanName(res.getString("POST_TYPE"));
                bcacc.setSectorName(res.getString("SECTOR_DESC"));
                bcacc.setSubMajorHead(res.getString("SUB_MAJOR_HEAD"));
                bcacc.setSubMajorHeadDesc(res.getString("SUB_MAJOR_HEAD_DESC"));
                bcacc.setMinorHead(res.getString("MINOR_HEAD"));
                bcacc.setMinorHeadDesc(res.getString("MINOR_HEAD_DESC"));
                bcacc.setSubMinorHead1(res.getString("SUB_MINOR_HEAD1"));
                bcacc.setSubMinorHeadDesc1(res.getString("SUB_MINOR_HEAD1_DESC"));
                bcacc.setSubMinorHead2(res.getString("SUB_MINOR_HEAD2"));
                bcacc.setSubMinorHeadDesc2(res.getString("SUB_MINOR_HEAD2_DESC"));
                if (res.getString("SUB_MINOR_HEAD3") != null && !res.getString("SUB_MINOR_HEAD3").equals("")) {
                    if (res.getString("SUB_MINOR_HEAD3").equals("2")) {
                        bcacc.setSubMinorHead3("Charge");
                    } else {
                        bcacc.setSubMinorHead3("Voted");
                    }
                } else {
                    bcacc.setSubMinorHead3("");
                }

                bcacc.setBillType(res.getString("BILL_TYPE"));
                bcacc.setDemandNo(res.getString("DEMAND_NO"));

                bcacc.setBillid(res.getString("bill_no"));
                bcacc.setVoucherNo(res.getString("vch_no"));
                bcacc.setVoucherDate(CommonFunctions.getFormattedOutputDate1(res.getDate("vch_date")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return bcacc;
    }

    @Override
    public ArrayList getScheduleListWithADCode(String billNo, int aqYear, int aqMonth) {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        ArrayList scheduleList = new ArrayList();
        Schedule sc = null;
        String aqDTLS = "AQ_DTLS";
        try {
            con = repodataSource.getConnection();
            stmt = con.createStatement();

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(aqMonth, aqYear);
            String bfQry = "Select SCHEDULE,NOW_DEDN,sum(AD_AMT) AD_AMT,BT_ID from " + aqDTLS + " a, aq_mast b where a.aqsl_no = b.aqsl_no "
                    + "and a.aq_month = b.aq_month and a.aq_year = b.aq_year and b.bill_no = '" + billNo + "' and a.aq_month = " + aqMonth + " "
                    + "and a.aq_year = " + aqYear + " and a.ad_type='D' and a.ad_amt >0 and a.AD_CODE !='PVTL' and a.AD_CODE !='PVTD' "
                    + "group by a.SCHEDULE, NOW_DEDN,BT_ID ORDER BY a.SCHEDULE";
            res = stmt.executeQuery(bfQry);
            while (res.next()) {
                String schedule = res.getString("SCHEDULE");
                if (schedule.equals("GPF")) {
                    sc = new Schedule();
                    sc.setScheduleName("GPF");
                    sc.setObjectHead(StringUtils.defaultString(res.getString("BT_ID")));
                    sc.setSchAmount(res.getString("AD_AMT"));

                } else if (schedule.equals("GA")) {
                    sc = new Schedule();
                    sc.setScheduleName("GA");
                    sc.setObjectHead(StringUtils.defaultString(res.getString("BT_ID")));
                    sc.setSchAmount(res.getString("AD_AMT"));

                } else if (schedule.equals("TPF") || schedule.equals("TPFGA")) {
                    sc = new Schedule();
                    sc.setScheduleName("TPF");
                    sc.setObjectHead(StringUtils.defaultString(res.getString("BT_ID")));
                    sc.addSchAmount(res.getInt("AD_AMT"));

                } else if (schedule.equals("CPF") || schedule.equals("NPSL")) {
                    sc = new Schedule();
                    sc.setScheduleName("CPF");
                    sc.setObjectHead(StringUtils.defaultString(res.getString("BT_ID")));
                    sc.addSchAmount(res.getInt("AD_AMT"));

                } else {
                    sc = new Schedule();
                    sc.setObjectHead(StringUtils.defaultString(res.getString("BT_ID")));
                    if (res.getString("NOW_DEDN") != null && !res.getString("NOW_DEDN").equals("")) {
                        sc.setScheduleName(res.getString("SCHEDULE") + " (" + res.getString("NOW_DEDN") + ")");
                    } else {
                        sc.setScheduleName(res.getString("SCHEDULE"));
                    }
                    sc.setSchAmount(res.getString("AD_AMT"));

                }
                scheduleList.add(sc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return scheduleList;
    }

    public Schedule getArrScheduleListWithADCode(String billNo) {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        Schedule sc = null;

        ResultSet rs2 = null;
        ResultSet rs3 = null;
        PreparedStatement pst2 = null;

        PreparedStatement pst1 = null;

        try {
            con = repodataSource.getConnection();
            stmt = con.createStatement();
            String bfQry = "select sum(inctax) inctax,sum(pt) pt,sum(other_recovery) otherrecovery,sum(ddo_recovery) ddorecovery from arr_mast where bill_no='" + billNo + "'";
            res = stmt.executeQuery(bfQry);
            if (res.next()) {
                sc = new Schedule();
                sc.setItObjHead("58816");
                sc.setItAmt(res.getString("inctax"));

                sc.setPtObjHead("3043");
                sc.setPtAmt(res.getString("pt"));

                //sc.setOrObjHead("59546");
                //sc.setOrAmt(res.getString("otherrecovery"));
                if (res.getInt("otherrecovery") > 0) {
                    pst1 = con.prepareStatement("select  btid_or,sum(other_recovery) as OTHER_RECOVERY from arr_mast where bill_no='" + billNo + "' "
                            + "group by btid_or having sum(other_recovery)>0");
                    rs3 = pst1.executeQuery();
                    if (rs3.next()) {
                        if ((rs3.getString("btid_or") != null && !rs3.getString("btid_or").equals(""))) {
                            sc.setOrObjHead(rs3.getString("btid_or"));
                        } else {
                            sc.setOrObjHead("59546");
                        }
                        sc.setOrAmt(res.getString("otherrecovery"));
                    }
                }

                //sc.setDrObjHead("12345");
                //sc.setDrAmt(res.getString("ddorecovery"));
                if (res.getInt("ddorecovery") > 0) {
                    pst1 = con.prepareStatement("select  btid_dr,sum(ddo_recovery) as DDO_RECOVERY from arr_mast where bill_no='" + billNo + "' "
                            + "group by btid_dr having sum(ddo_recovery)>0");
                    rs3 = pst1.executeQuery();
                    if (rs3.next()) {
                        if ((rs3.getString("btid_dr") != null && !rs3.getString("btid_dr").equals(""))) {
                            sc.setDrObjHead(rs3.getString("btid_dr"));
                        }
                        sc.setDrAmt(res.getString("ddorecovery"));
                    }
                }
            }

            String gpfType = "";

            pst2 = con.prepareStatement("select gpf_type from arr_mast where bill_no=? and gpf_type='ISO'");
            pst2.setInt(1, Integer.parseInt(billNo));
            rs2 = pst2.executeQuery();
            if (rs2.next()) {
                gpfType = "ISO";
            }

            bfQry = "select sum(cpf_head) cpf_head, acct_type from arr_mast where bill_no='" + billNo + "'  group by acct_type ";
            res = stmt.executeQuery(bfQry);
            while (res.next()) {
                if (res.getInt("cpf_head") > 0) {
                    if (res.getString("acct_type") != null && !res.getString("acct_type").equals("") && res.getString("acct_type").equals("GPF")) {
                        if (gpfType != null && !gpfType.equals("")) {
                            sc.setCpfObjHead("57649");
                            sc.setAcctType("GPF");
                        } else {
                            sc.setCpfObjHead("55545");
                            sc.setAcctType("GPF");
                        }

                    } else if (res.getString("acct_type") != null && !res.getString("acct_type").equals("") && res.getString("acct_type").equals("TPF")) {
                        sc.setCpfObjHead("55550");
                        sc.setAcctType("TPF");
                    } else {
                        sc.setCpfObjHead("57740");
                        sc.setAcctType("CPF");
                    }

                    sc.setCpfAmt(res.getString("cpf_head"));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return sc;
    }

    @Override
    public BillObjectHead getBillObjectHeadAndAmt(String billNo, int aqYear, int aqMonth) {
        BillObjectHead billObjectHead = new BillObjectHead();
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        String aqDTLS = "AQ_DTLS";
        try {
            con = repodataSource.getConnection();
            stmt = con.createStatement();
            String adCode = "GP";

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(aqMonth, aqYear);
            String bObjHeadQry = "Select sum(AQ_DTLS.AD_AMT) AD_AMT,BT_ID from( (Select AQ_MAST.AQSL_NO from AQ_MAST "
                    + "where AQ_MAST.BILL_NO = '" + billNo + "' and AQ_MAST.aq_month=" + aqMonth + " and AQ_MAST.aq_year=" + aqYear + " )AQ_MAST inner join "
                    + "(Select AQSL_NO,AD_AMT,BT_ID from " + aqDTLS + " where SCHEDULE = '" + adCode + "') AQ_DTLS "
                    + " on AQ_MAST.AQSL_NO = AQ_DTLS.AQSL_NO )GROUP BY BT_ID";
            res = stmt.executeQuery(bObjHeadQry);
            if (res.next()) {
                billObjectHead.setPayhead(res.getString("BT_ID"));
                billObjectHead.setPayamt(res.getLong("AD_AMT"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billObjectHead;
    }

    public BillObjectHead getArrBillObjectHeadAndAmt(String billNo, String typeofbill) {
        BillObjectHead billObjectHead = new BillObjectHead();

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet res = null;
        try {
            con = repodataSource.getConnection();

            if (typeofbill != null && typeofbill.equalsIgnoreCase("ARREAR_6_J")) {
                String bObjHeadQry = "select sum(arrear_pay)arrear_pay, pay_head from arr_mast where bill_no = ? group by pay_head";
                pst = con.prepareStatement(bObjHeadQry);
                pst.setInt(1, Integer.parseInt(billNo));
                res = pst.executeQuery();
                if (res.next()) {
                    billObjectHead.setPayhead(res.getString("pay_head"));
                    /*Double doublearrearpay = res.getInt("full_arrear_pay")*0.5;
                     int intarrearpay = doublearrearpay.intValue();
                     billObjectHead.setPayamt(Long.parseLong(intarrearpay+""));*/
                    billObjectHead.setPayamt(res.getLong("arrear_pay"));
                }
            } else if (typeofbill != null && typeofbill.equalsIgnoreCase("ARREAR_NPS")) {
                String bObjHeadQry = "select sum(cpf_head)tot_cpf, pay_head from arr_mast where bill_no = ? group by pay_head";
                pst = con.prepareStatement(bObjHeadQry);
                pst.setInt(1, Integer.parseInt(billNo));
                res = pst.executeQuery();
                if (res.next()) {
                    billObjectHead.setPayhead(res.getString("pay_head"));
                    billObjectHead.setPayamt(res.getLong("tot_cpf"));
                    System.out.println("billObjectHead 5%===" + billObjectHead.getPayamt());
                }
            } else {
                String bObjHeadQry = "select sum(arrear_pay)arrear_pay, pay_head from arr_mast where bill_no = ? group by pay_head";
                pst = con.prepareStatement(bObjHeadQry);
                pst.setInt(1, Integer.parseInt(billNo));
                res = pst.executeQuery();
                if (res.next()) {
                    billObjectHead.setPayhead(res.getString("pay_head"));
                    billObjectHead.setPayamt(res.getLong("arrear_pay"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billObjectHead;
    }

    @Override
    public List getLtaAdcodeWiseList(BillBean bb, String aqDtlsTableName) {
        Connection con = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;
        List longtermdtls = new ArrayList();
        String loanwiseSum = "";
        String adcode = "";
        Schedule sc = null;
        try {

            con = repodataSource.getConnection();

            String loangroup = "SELECT SUM(AD_AMT) SUM_AMT,DTLS.AD_CODE FROM   AQ_MAST MAST, " + aqDtlsTableName + " DTLS\n"
                    + " WHERE MAST.AQSL_NO=DTLS.AQSL_NO\n"
                    + " AND (DTLS.AD_CODE='HBA' OR DTLS.AD_CODE='MCA'     "
                    + " OR DTLS.AD_CODE='VE' OR DTLS.AD_CODE='MOPA' OR DTLS.AD_CODE='CMPA' ) AND DTLS.AD_AMT>0 AND "
                    + " MAST.BILL_NO=? AND MAST.AQ_MONTH=? AND MAST.AQ_YEAR=? GROUP BY AD_CODE";

            pstmt2 = con.prepareStatement(loangroup);

            pstmt2.setInt(1, Integer.parseInt(bb.getBillno()));
            pstmt2.setInt(2, bb.getBillMonth());
            pstmt2.setInt(3, bb.getBillYear());
            rs = pstmt2.executeQuery();
            while (rs.next()) {
                sc = new Schedule();
                sc.setSchAmount(rs.getString("SUM_AMT"));
                sc.setScheduleName(rs.getString("AD_CODE"));
                sc.setBillno(Integer.parseInt(bb.getBillno()));
                if (rs.getInt("SUM_AMT") > 0) {
                    longtermdtls.add(sc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            DataBaseFunctions.closeSqlObjects(rs, pstmt2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return longtermdtls;
    }

    @Override
    public ArrayList getLongTermLoanDtls(BillBean bb, String aqDtlsTableName) {
        Connection con = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet res = null;
        ArrayList longtermdtls = new ArrayList();
        List li = new ArrayList();
        try {
            con = repodataSource.getConnection();
            stmt = con.createStatement();
            String sql = "SELECT GPF_ACC_NO,MAST.AQ_MONTH,MAST.AQ_YEAR,DTLS.NOW_DEDN,REF_COUNT,AD_AMT,DTLS.BT_ID,emp_loan.IS_VERIFIED,  "
                    + "noti.ordno,noti.orddt,R_MJH_CD,R_SMJH_CD,R_MIH_CD,R_SBH_CD,"
                    + "R_DTLH_CD,R_SDTLH_CD,ad_ref_id,loan_subtyp,ordno,orddt,LOAN_SUBTYP FROM AQ_MAST MAST "
                    + "INNER JOIN " + aqDtlsTableName + " DTLS ON MAST.AQSL_NO=DTLS.AQSL_NO "
                    + "inner join emp_loan_sanc emp_loan on dtls.ad_ref_id=emp_loan.loanid "
                    + "inner join emp_notification noti on emp_loan.not_id=noti.not_id "
                    + "INNER JOIN AG_BT_ID BTID ON DTLS.BT_ID=BTID.BT_ID "
                    + "WHERE MAST.BILL_NO=?  AND MAST.AQ_YEAR=? AND MAST.AQ_MONTH=?  AND DTLS.AD_CODE=? AND DTLS.AD_AMT>0 ";

            li = getLtaAdcodeWiseList(bb, aqDtlsTableName);
            for (int i = 0; i < li.size(); i++) {

                Schedule sc = (Schedule) li.get(i);

                pstmt = con.prepareStatement(sql);
                pstmt.setInt(1, sc.getBillno());
                pstmt.setInt(2, bb.getBillYear());
                pstmt.setInt(3, bb.getBillMonth());
                pstmt.setString(4, sc.getScheduleName());
                res = pstmt.executeQuery();
                int kp = 1;
                while (res.next()) {

                    kp++;
                    LtaBeanForAG ltaBeanForAG = new LtaBeanForAG();
                    ltaBeanForAG.setLN_IDNO(res.getString("GPF_ACC_NO"));
                    ltaBeanForAG.setLOAN_TYP(sc.getScheduleName());
                    ltaBeanForAG.setLOAN_STYP(res.getString("LOAN_SUBTYP"));
                    ltaBeanForAG.setSNC_NO(res.getString("ordno"));
                    ltaBeanForAG.setSNC_DT(res.getString("orddt"));
                    ltaBeanForAG.setAD_REF_ID(res.getString("ad_ref_id"));
                    ltaBeanForAG.setIS_VERIFIED(res.getString("IS_VERIFIED"));
                    ltaBeanForAG.setFIN_YR(bb.getFinyear());
                    ltaBeanForAG.setACC_MN(bb.getVouchermonth());
                    ltaBeanForAG.setACC_TYP("NEW");
                    ltaBeanForAG.setSEC_NM("TA");
                    ltaBeanForAG.setSAL_MN("1-" + (res.getInt("AQ_MONTH") + 1) + "-" + res.getString("AQ_YEAR"));
                    String s = bb.getVouchermonth();
                    double d = Double.parseDouble(s);
                    int vmonth = (int) d;

                    ltaBeanForAG.setADJ_MN("1-" + vmonth + "-" + bb.getAdjYear());

                    if (res.getString("NOW_DEDN").equalsIgnoreCase("P")) {
                        ltaBeanForAG.setPI_FLG("PRN");
                    } else {
                        ltaBeanForAG.setPI_FLG("INT");
                    }

                    ltaBeanForAG.setRECV_ORD_NO(null);
                    ltaBeanForAG.setRECV_ORD_DT(null);
                    ltaBeanForAG.setRECV_TYP("NORMAL");
                    ltaBeanForAG.setPAY_MODE("SALARY_OR_PAY_BILL");
                    ltaBeanForAG.setTR_FIN_YR(bb.getFinyear());
                    ltaBeanForAG.setTR_MN(bb.getVouchermonth());
                    if (bb.getVoucherDay() <= 18) {
                        ltaBeanForAG.setUNITNO("1");
                    } else {
                        ltaBeanForAG.setUNITNO("2");
                    }
                    ltaBeanForAG.setTR_CD(bb.getTreasurycode());

                    ltaBeanForAG.setRMJHCD(res.getString("R_MJH_CD"));
                    ltaBeanForAG.setRSMJHCD(res.getString("R_SMJH_CD"));
                    ltaBeanForAG.setRMIHCD(res.getString("R_MIH_CD"));
                    ltaBeanForAG.setRSBHCD(res.getString("R_SBH_CD"));
                    ltaBeanForAG.setRDTLHCD(res.getString("R_DTLH_CD"));
                    ltaBeanForAG.setRSDTLHCD(res.getString("R_SDTLH_CD"));

                    ltaBeanForAG.setTVTC_NO(bb.getVoucherno());
                    ltaBeanForAG.setTVTC_DT(bb.getVoucherdate());
                    ltaBeanForAG.setDDO_CD(bb.getDdocode());
                    ltaBeanForAG.setINST_NO(res.getInt("REF_COUNT"));
                    ltaBeanForAG.setAMT_PAID(res.getString("AD_AMT"));
                    ltaBeanForAG.setBASE_HD_CD(bb.getMajorhead());
                    ltaBeanForAG.setREF_UP_TE_NO(null);
                    ltaBeanForAG.setREF_ROWID(null);
                    ltaBeanForAG.setWHO_USER("HRMS");
                    ltaBeanForAG.setAMT_TYP(res.getString("NOW_DEDN"));
                    ltaBeanForAG.setLOANWISESUM(sc.getSchAmount());
                    longtermdtls.add(ltaBeanForAG);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return longtermdtls;
    }

    @Override
    public Schedule getScheduleList(String scheduleName) {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        Schedule sc = new Schedule();
        try {
            con = repodataSource.getConnection();
            stmt = con.createStatement();

            res = stmt.executeQuery("SELECT SCHEDULE,SCHEDULE_DESC,DEMAND_NO from G_SCHEDULE where SCHEDULE='" + scheduleName + "'");
            if (res.next()) {
                sc = new Schedule();
                sc.setScheduleName(res.getString("SCHEDULE"));
                sc.setScheduleDesc(res.getString("SCHEDULE_DESC"));
                sc.setDemandNo(res.getString("DEMAND_NO"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return sc;
    }

    @Override
    public AcquaintanceBean getAqMastDtl(String aqslno) {
        AcquaintanceBean aqBean = new AcquaintanceBean();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            con = repodataSource.getConnection();
            pstmt = con.prepareStatement("SELECT EMP_TYPE,EMP_CODE,EMP_NAME,OFF_CODE,GPF_TYPE,GPF_ACC_NO,OFF_DDO,MON_BASIC,CUR_BASIC,PAY_SCALE,OFF_DDO,GPF_TYPE,CUR_DESG,DECODE(PLAN,null,' ',PLAN)PLAN,DECODE(SECTOR,null,' ',SECTOR)SECTOR,AQ_DAY, PAY_DAY, dep_code FROM AQ_MAST WHERE AQSL_NO=?");
            pstmt.setString(1, aqslno);
            res = pstmt.executeQuery();
            if (res.next()) {
                aqBean.setEmptype(res.getString("EMP_TYPE"));
                aqBean.setEmpcode(res.getString("EMP_CODE"));
                aqBean.setEmpname(res.getString("EMP_NAME"));
                aqBean.setCurdesg(res.getString("CUR_DESG"));
                aqBean.setPayscale(res.getString("PAY_SCALE"));
                aqBean.setCurbasic(res.getInt("CUR_BASIC"));
                aqBean.setMonbasic(res.getInt("MON_BASIC"));
                aqBean.setGpfaccno(res.getString("GPF_ACC_NO"));
                aqBean.setAqday(res.getInt("AQ_DAY"));
                aqBean.setPayday(res.getInt("PAY_DAY"));
                aqBean.setDepcode(res.getString("dep_code"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aqBean;
    }

    @Override
    public ArrayList getAcquaintanceDtlDed(String aqslno, String aqDtlsTableName) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        ArrayList deductionobjList = new ArrayList();
        try {
            con = repodataSource.getConnection();
            String queryClump = "SELECT AQSL_NO,AD_CODE,ACC_NO,REF_DESC,AD_AMT,DED_TYPE,NOW_DEDN,AD_REF_ID,INSTALMENT_COUNT,AD_DESC FROM " + aqDtlsTableName + " WHERE " + aqDtlsTableName + ".AQSL_NO = ? AND AD_TYPE='D' ORDER BY AD_CODE,AD_AMT DESC ";
            pstmt = con.prepareStatement(queryClump);
            pstmt.setString(1, aqslno);
            res = pstmt.executeQuery();
            while (res.next()) {
                DeductionDetailsObjClass deductionobjclass = new DeductionDetailsObjClass();
                deductionobjclass.setHidDeductionAqSlno(res.getString("AQSL_NO"));
                deductionobjclass.setDeduction(res.getString("AD_CODE"));
                deductionobjclass.setAccNo(res.getString("ACC_NO"));
                deductionobjclass.setDeductionFor(res.getString("REF_DESC"));
                deductionobjclass.setAmount(res.getString("AD_AMT"));
                deductionobjclass.setNowdedn(res.getString("NOW_DEDN"));
                deductionobjclass.setDedType(res.getString("DED_TYPE"));
                deductionobjclass.setDeductionName(res.getString("AD_DESC"));
                if (res.getString("AD_REF_ID") != null && !res.getString("AD_REF_ID").equals("")) {
                    deductionobjclass.setAdrefId(res.getString("AD_REF_ID"));
                } else {
                    deductionobjclass.setAdrefId("");
                }
                deductionobjclass.setNoofInstal(res.getInt("INSTALMENT_COUNT"));
                deductionobjList.add(deductionobjclass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deductionobjList;
    }

    @Override
    public ArrayList getAcquaintanceDtlAll(String aqslno, String aqDtlsTableName) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        ArrayList allowanceObjList = new ArrayList();
        try {
            con = repodataSource.getConnection();
            pstmt = con.prepareStatement("SELECT AQSL_NO,AD_CODE,AD_AMT,AD_DESC FROM " + aqDtlsTableName + " WHERE AD_TYPE='A' AND AQSL_NO=? ORDER BY AD_CODE,AD_AMT DESC");
            pstmt.setString(1, aqslno);
            res = pstmt.executeQuery();
            while (res.next()) {
                AllowanceDetailsObjClass adobjclass = new AllowanceDetailsObjClass();
                adobjclass.setAllowance(res.getString("AD_CODE"));
                adobjclass.setAmount(res.getString("AD_AMT"));
                adobjclass.setAllowanceName(res.getString("AD_DESC"));
                allowanceObjList.add(adobjclass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allowanceObjList;
    }

    public CommonReportParamBean getBillDetails(String billNo) {
        CommonReportParamBean commonBean = new CommonReportParamBean();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = repodataSource.getConnection();
            st = con.createStatement();
            if (billNo != null && !billNo.equals("")) {
                rs = st.executeQuery("SELECT SUFFIX,DESCRIPTION,BILL_DESC,BILL_DATE,AQ_YEAR,AQ_MONTH,G_OFFICE.OFF_EN,DIST_NAME,STATE_NAME, G_POST.POST,G_OFFICE.OFF_EN,G_OFFICE.DDO_REG_NO,G_OFFICE.DTO_REG_NO,G_DEPARTMENT.DEPARTMENT_NAME,TAN_NO,FA_BTID,PA_CODE,BILL_NO.OFF_CODE,BILL_NO.TR_CODE,TR_NAME, TYPE_OF_BILL,vch_no,vch_date FROM "
                        + "(SELECT BILL_GROUP_ID,OFF_CODE,BILL_DESC,AQ_YEAR,AQ_MONTH,BILL_DATE,TR_CODE,TYPE_OF_BILL,vch_no,vch_date FROM BILL_MAST WHERE BILL_NO='" + billNo + "') BILL_NO  "
                        + "INNER JOIN G_OFFICE ON BILL_NO.OFF_CODE = G_OFFICE.OFF_CODE "
                        + "LEFT OUTER JOIN G_POST ON G_OFFICE.DDO_POST=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN G_DEPARTMENT ON G_OFFICE.DEPARTMENT_CODE =G_DEPARTMENT.DEPARTMENT_CODE "
                        + "LEFT OUTER JOIN G_DISTRICT ON G_OFFICE.DIST_CODE=G_DISTRICT.DIST_CODE "
                        + "LEFT OUTER JOIN G_STATE ON G_OFFICE.STATE_CODE=G_STATE.STATE_CODE "
                        + "LEFT OUTER JOIN BILL_GROUP_MASTER ON BILL_NO.BILL_GROUP_ID=BILL_GROUP_MASTER.BILL_GROUP_ID"
                        + " LEFT OUTER JOIN G_TREASURY ON BILL_NO.TR_CODE=G_TREASURY.TR_CODE");

                if (rs.next()) {
                    commonBean.setSuffix(rs.getString("SUFFIX"));
                    commonBean.setBillgroupDesc(rs.getString("DESCRIPTION"));
                    commonBean.setBilldesc(rs.getString("BILL_DESC"));
                    commonBean.setBilldate(CommonFunctions.getFormattedOutputDate1(rs.getDate("BILL_DATE")));
                    commonBean.setAqyear(rs.getInt("AQ_YEAR"));
                    commonBean.setAqmonth(rs.getInt("AQ_MONTH"));
                    commonBean.setDdoname(rs.getString("POST"));
                    commonBean.setOfficename(rs.getString("OFF_EN"));
                    commonBean.setDeptname(rs.getString("DEPARTMENT_NAME"));
                    commonBean.setDistrict(rs.getString("DIST_NAME"));
                    commonBean.setStatename(rs.getString("STATE_NAME"));
                    commonBean.setDdoregno(rs.getString("DDO_REG_NO"));
                    commonBean.setDtoregno(rs.getString("DTO_REG_NO"));
                    commonBean.setOfficeen(rs.getString("OFF_EN"));
                    commonBean.setTanno(rs.getString("TAN_NO"));
                    commonBean.setPacode(rs.getString("PA_CODE"));
                    commonBean.setOffcode(rs.getString("OFF_CODE"));
                    commonBean.setTypeofBill(rs.getString("TYPE_OF_BILL"));
                    if (rs.getString("FA_BTID") != null && !rs.getString("FA_BTID").equals("")) {
                        commonBean.setFabtid(rs.getString("FA_BTID"));
                    } else {
                        commonBean.setFabtid("55032");
                    }
                    commonBean.setTreasuryname(rs.getString("TR_NAME"));
                    commonBean.setVchNo(rs.getString("vch_no"));
                    commonBean.setVchDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("vch_date")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return commonBean;
    }
    /*------for DHE(Start)-------*/

    public CommonReportParamBean getBillDetailsDHE(String billNo) {
        CommonReportParamBean commonBean = new CommonReportParamBean();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = repodataSource.getConnection();
            st = con.createStatement();
            if (billNo != null && !billNo.equals("")) {
                rs = st.executeQuery("SELECT SUFFIX,DESCRIPTION,BILL_DESC,BILL_DATE,AQ_YEAR,AQ_MONTH,G_OFFICE.OFF_EN,DIST_NAME,STATE_NAME, G_POST.POST,G_OFFICE.OFF_EN,G_OFFICE.DDO_REG_NO,G_OFFICE.DTO_REG_NO,\n"
                        + "G_DEPARTMENT.DEPARTMENT_NAME,TAN_NO,FA_BTID,PA_CODE,BILL_NO.P_OFF_CODE,BILL_NO.TR_CODE,TR_NAME, TYPE_OF_BILL,vch_no,vch_date FROM\n"
                        + "(SELECT BILL_GROUP_ID,P_OFF_CODE,BILL_DESC,AQ_YEAR,AQ_MONTH,BILL_DATE,TR_CODE,TYPE_OF_BILL,vch_no,vch_date FROM BILL_MAST WHERE BILL_NO='" + billNo + "') BILL_NO\n"
                        + "INNER JOIN G_OFFICE ON BILL_NO.P_OFF_CODE = G_OFFICE.OFF_CODE\n"
                        + "LEFT OUTER JOIN G_POST ON G_OFFICE.DDO_POST=G_POST.POST_CODE\n"
                        + "LEFT OUTER JOIN G_DEPARTMENT ON G_OFFICE.DEPARTMENT_CODE =G_DEPARTMENT.DEPARTMENT_CODE\n"
                        + "LEFT OUTER JOIN G_DISTRICT ON G_OFFICE.DIST_CODE=G_DISTRICT.DIST_CODE\n"
                        + "LEFT OUTER JOIN G_STATE ON G_OFFICE.STATE_CODE=G_STATE.STATE_CODE\n"
                        + "LEFT OUTER JOIN BILL_GROUP_MASTER ON BILL_NO.BILL_GROUP_ID=BILL_GROUP_MASTER.BILL_GROUP_ID\n"
                        + "LEFT OUTER JOIN G_TREASURY ON BILL_NO.TR_CODE=G_TREASURY.TR_CODE");

                if (rs.next()) {
                    commonBean.setSuffix(rs.getString("SUFFIX"));
                    commonBean.setBillgroupDesc(rs.getString("DESCRIPTION"));
                    commonBean.setBilldesc(rs.getString("BILL_DESC"));
                    commonBean.setBilldate(CommonFunctions.getFormattedOutputDate1(rs.getDate("BILL_DATE")));
                    commonBean.setAqyear(rs.getInt("AQ_YEAR"));
                    commonBean.setAqmonth(rs.getInt("AQ_MONTH"));
                    commonBean.setDdoname(rs.getString("POST"));
                    commonBean.setOfficename(rs.getString("OFF_EN"));
                    commonBean.setDeptname(rs.getString("DEPARTMENT_NAME"));
                    commonBean.setDistrict(rs.getString("DIST_NAME"));
                    commonBean.setStatename(rs.getString("STATE_NAME"));
                    commonBean.setDdoregno(rs.getString("DDO_REG_NO"));
                    commonBean.setDtoregno(rs.getString("DTO_REG_NO"));
                    commonBean.setOfficeen(rs.getString("OFF_EN"));
                    commonBean.setTanno(rs.getString("TAN_NO"));
                    commonBean.setPacode(rs.getString("PA_CODE"));
                    commonBean.setOffcode(rs.getString("P_OFF_CODE"));
                    commonBean.setTypeofBill(rs.getString("TYPE_OF_BILL"));
                    if (rs.getString("FA_BTID") != null && !rs.getString("FA_BTID").equals("")) {
                        commonBean.setFabtid(rs.getString("FA_BTID"));
                    } else {
                        commonBean.setFabtid("55032");
                    }
                    commonBean.setTreasuryname(rs.getString("TR_NAME"));
                    commonBean.setVchNo(rs.getString("vch_no"));
                    commonBean.setVchDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("vch_date")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return commonBean;
    }
    /*----End-----*/

    @Override
    public String getEmpType(String billNo, String mon, String year) {
        Connection con = null;
        Statement st = null;
        String empType = "";
        ResultSet rs = null;;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            String sqlqryforsection = "SELECT SEC_SL_NO,SECTION,EMP_TYPE FROM AQ_MAST WHERE BILL_NO='" + billNo + "'  GROUP BY SECTION,SEC_SL_NO,EMP_TYPE ORDER BY SEC_SL_NO";
            rs = st.executeQuery(sqlqryforsection);
            if (rs.next()) {
                empType = rs.getString("EMP_TYPE");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empType;
    }

    public ArrayList getSectionWiseBillDtls(String billno, String mon, String year, String format, BillConfigObj billConfigObj, String empType, String column9NameList, String column10NameList, String column11NameList, String column12NameList, String column13NameList, String column14NameList, String column15NameList, String column16NameList, String column17NameList, String column18NameList) {
        String section = "";
        Connection con = null;
        int secslno = 0;
        String offCode = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        SectionWiseAqBean objBill = null;;
        ArrayList a1 = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT SEC_SL_NO, SECTION, EMP_TYPE FROM AQ_MAST WHERE AQ_YEAR=? AND AQ_MONTH=? AND BILL_NO=?  AND EMP_TYPE=? GROUP BY SECTION, SEC_SL_NO, EMP_TYPE ORDER BY SEC_SL_NO");
            pstmt.setInt(1, Integer.parseInt(year));
            pstmt.setInt(2, Integer.parseInt(mon));
            pstmt.setInt(3, Integer.parseInt(billno));
            pstmt.setString(4, empType);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                objBill = new SectionWiseAqBean();
                section = rs.getString("SECTION");
                secslno = rs.getInt("SEC_SL_NO");
                empType = rs.getString("EMP_TYPE");
                //offCode = rs.getString("OFF_CODE");
                if (format.equals("f2") && empType.equals("R")) {
                    objBill = getAqBillDetailsF2(billno, mon, year, section, secslno, billConfigObj, column9NameList, column10NameList, column11NameList, column12NameList, column13NameList, column14NameList, column15NameList, column16NameList, column17NameList, column18NameList);
                } else if (format.equals("f1") && (empType.equals("R") || empType.equals("A"))) {
                    objBill = getAqBillDetails(billno, mon, year, section, secslno, billConfigObj, column9NameList, column10NameList, column11NameList, column12NameList, column13NameList, column14NameList, column15NameList, column16NameList, column17NameList, column18NameList);
                } else if (format.equals("f1") && (empType.equals("C") || empType.equals("B"))) {
                    objBill = getAqBillDetailsContractual(billno, mon, year, section, secslno, billConfigObj);
                } else if (format.equals("f1") && empType.equals("S")) {
                    objBill = getAqBillDetailsContractual(billno, mon, year, section, secslno, billConfigObj);
                } else if (format.equals("f2") && (empType.equals("C") || empType.equals("B"))) {
                    objBill = getAqBillDetailsContractualF2(billno, mon, year, section, secslno, billConfigObj);
                } else if (format.equals("f2") && empType.equals("S")) {
                    objBill = getAqBillDetailsContractualF2(billno, mon, year, section, secslno, billConfigObj);
                }
                //objBill.setSectionwiseOffEn(rs.getString("off_en"));
                objBill.setSectionname(section);
                a1.add(objBill);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return a1;
    }

    public SectionWiseAqBean getAqBillDetailsF2(String billno, String mon, String year, String section, int secslno, BillConfigObj billConfigObj, String column9NameList, String column10NameList, String column11NameList, String column12NameList, String column13NameList, String column14NameList, String column15NameList, String column16NameList, String column17NameList, String column18NameList) {
        Connection con = null;
        Statement st = null;
        Statement st1 = null;
        Statement st2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        int pageno = 1;
        AqreportHelperBean aqbean = null;
        ArrayList aqlist = new ArrayList();
        int colNo = 0;
        section = StringUtils.replace(section, "'", "''");
        SectionWiseAqBean secWiseAqBean = new SectionWiseAqBean();
        String aqDTLS = "AQ_DTLS";
        /*String col6Desc[]=null;
         String col7Desc[]=null;
         String col16Desc[]=null;
         String col17Desc[]=null;
         String col18Desc[]=null;  */
        String sqlQuery = "";
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            st1 = con.createStatement();
            st2 = con.createStatement();
            st = con.createStatement();

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(Integer.parseInt(mon), Integer.parseInt(year));

            PayrollCommonFunction prcf = new PayrollCommonFunction();
            CommonReportParamBean bean = prcf.getCommonReportParameter(con, StringUtils.defaultString(billno));

            if (billConfigObj.getNotToPrintInAquitance() != null && billConfigObj.getNotToPrintInAquitance().equals("N")) {
                sqlQuery = "SELECT * FROM (SELECT AQSL_NO,EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,SPC_ORD_NO,SPC_ORD_DATE,GPF_ACC_NO,ACCT_TYPE,EMP_CODE,post_sl_no,SEC_SL_NO "
                        + "FROM AQ_MAST WHERE AQ_MONTH=" + mon + " AND AQ_YEAR=" + year + " AND BILL_NO=" + billno + " AND SECTION='" + section + "' AND SEC_SL_NO=" + secslno + " AND DEP_CODE != '11' AND EMP_CODE IS NOT NULL) AQ_MAST "
                        + "LEFT OUTER JOIN (SELECT EMP_ID,BRASS_NO,CUR_CADRE_CODE,IF_GPF_ASSUMED FROM EMP_MAST)EMP_MAST ON AQ_MAST.EMP_CODE = EMP_MAST.EMP_ID "
                        + "LEFT OUTER JOIN (SELECT CADRE_CODE,CADRE_ABBR FROM G_CADRE)G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE order by POST_SL_NO";
            } else {
                sqlQuery = "SELECT * FROM (SELECT AQSL_NO,EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,SPC_ORD_NO,SPC_ORD_DATE,GPF_ACC_NO,ACCT_TYPE,EMP_CODE,post_sl_no,SEC_SL_NO "
                        + "FROM AQ_MAST WHERE AQ_MONTH=" + mon + " AND AQ_YEAR=" + year + " AND BILL_NO=" + billno + " AND SECTION='" + section + "' AND SEC_SL_NO=" + secslno + " AND DEP_CODE != '11') AQ_MAST "
                        + "LEFT OUTER JOIN (SELECT EMP_ID,BRASS_NO,CUR_CADRE_CODE,IF_GPF_ASSUMED FROM EMP_MAST)EMP_MAST ON AQ_MAST.EMP_CODE = EMP_MAST.EMP_ID "
                        + "LEFT OUTER JOIN (SELECT CADRE_CODE,CADRE_ABBR FROM G_CADRE)G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE order by POST_SL_NO";
            }

            //"SELECT rownum slno,AQSL_NO,EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,SPC_ORD_NO,SPC_ORD_DATE,GPF_ACC_NO,ACCT_TYPE FROM AQ_MAST WHERE AQ_MONTH='"+mon+"' AND AQ_YEAR='"+year+"' AND BILL_NO='"+billno+"' order by post_sl_no"
            rs = st.executeQuery(sqlQuery);
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
                if (i % 5 == 0) {
                    pageno++;
                    aqbean.setPagebreakLA("<input type=\"button\" name=\"pagebreak1\" style=\"page-break-before: always;width: 0;height: 0\"/>");
                    aqbean.setPageHeaderLA(reportPageHeaderF2(bean, pageno, column9NameList, column10NameList, column11NameList, column12NameList, column13NameList, column14NameList, column15NameList, column16NameList, column17NameList, column18NameList));
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
                String queryClump = "select * from (select cnt,REP_COL,tmep4.AD_CODE,AD_AMT,SCHEDULE,tmep4.NOW_DEDN,AD_TYPE,sl_no,REF_DESC from(select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt = 1)tmep4 "
                        + "left outer join (SELECT AD_CODE,REF_DESC,NOW_DEDN FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp2 on "
                        + "tmep4.ad_code = temp2.ad_code and tmep4.now_dedn = temp2.now_dedn "
                        + "union "
                        + "select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no,''::TEXT from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp "
                        + " group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt > 1 ) AS TAB order by ad_code ";
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
                                refdesc = aqfunctionalities.getRefDesc(adcode, nowdedn, aqslno, Integer.parseInt(mon), Integer.parseInt(year));
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
                int privatededuction = aqfunctionalities.getPrivateDeduction(rs.getString("AQSL_NO"), Integer.parseInt(mon), Integer.parseInt(year));
                int privateloan = aqfunctionalities.getPrivateLoan(rs.getString("AQSL_NO"), Integer.parseInt(mon), Integer.parseInt(year));
                int grosspay = aqfunctionalities.getGrossPay(rs.getString("AQSL_NO"), Integer.parseInt(mon), Integer.parseInt(year));
                int totaldedn = aqfunctionalities.getTotalDedn(rs.getString("AQSL_NO"), Integer.parseInt(mon), Integer.parseInt(year));
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

    public SectionWiseAqBean getAqBillDetails(String billno, String mon, String year, String section, int secslno, BillConfigObj billConfigObj, String column9NameList, String column10NameList, String column11NameList, String column12NameList, String column13NameList, String column14NameList, String column15NameList, String column16NameList, String column17NameList, String column18NameList) throws Exception {
        Connection con = null;
        int pageno = 1;
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
            st = con.createStatement();
            st1 = con.createStatement();
            st2 = con.createStatement();

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(Integer.parseInt(mon), Integer.parseInt(year));
            PayrollCommonFunction prcf = new PayrollCommonFunction();
            CommonReportParamBean bean = prcf.getCommonReportParameter(con, StringUtils.defaultString(billno));

            if (billConfigObj.getNotToPrintInAquitance() != null && billConfigObj.getNotToPrintInAquitance().equals("N")) {
                pstamt = con.prepareStatement("SELECT * FROM (SELECT OFF_CODE,AQSL_NO,EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,SPC_ORD_NO,SPC_ORD_DATE,GPF_ACC_NO,ACCT_TYPE,EMP_CODE,post_sl_no,SEC_SL_NO "
                        + "FROM AQ_MAST WHERE AQ_MONTH='" + mon + "' AND AQ_YEAR='" + year + "' AND BILL_NO='" + billno + "' AND SECTION='" + section + "' AND SEC_SL_NO='" + secslno + "' AND DEP_CODE != '11' AND EMP_CODE IS NOT NULL) AQ_MAST "
                        + "LEFT OUTER JOIN (SELECT EMP_ID,BRASS_NO,CUR_CADRE_CODE,IF_GPF_ASSUMED FROM EMP_MAST)EMP_MAST ON AQ_MAST.EMP_CODE = EMP_MAST.EMP_ID "
                        + "LEFT OUTER JOIN (SELECT CADRE_CODE,CADRE_ABBR FROM G_CADRE)G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE order by POST_SL_NO");
            } else {
                pstamt = con.prepareStatement("SELECT * FROM (SELECT OFF_CODE,AQSL_NO,EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,SPC_ORD_NO,SPC_ORD_DATE,GPF_ACC_NO,ACCT_TYPE,EMP_CODE,post_sl_no,SEC_SL_NO "
                        + "FROM AQ_MAST WHERE AQ_MONTH='" + mon + "' AND AQ_YEAR='" + year + "' AND BILL_NO='" + billno + "' AND SECTION='" + section + "' AND SEC_SL_NO='" + secslno + "' AND DEP_CODE != '11') AQ_MAST "
                        + "LEFT OUTER JOIN (SELECT EMP_ID,BRASS_NO,CUR_CADRE_CODE,IF_GPF_ASSUMED FROM EMP_MAST)EMP_MAST ON AQ_MAST.EMP_CODE = EMP_MAST.EMP_ID "
                        + "LEFT OUTER JOIN (SELECT CADRE_CODE,CADRE_ABBR FROM G_CADRE)G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE order by POST_SL_NO");
            }
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
                if (i % 5 == 0) {
                    pageno++;
                    aqbean.setPagebreakLA("<input type=\"button\" name=\"pagebreak1\" style=\"page-break-before: always;width: 0;height: 0\"/>");
                    aqbean.setPageHeaderLA(reportPageHeader(bean, pageno, column9NameList, column10NameList, column11NameList, column12NameList, column13NameList, column14NameList, column15NameList, column16NameList, column17NameList, column18NameList));
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
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 "
                        + " where cnt > 1 ) AS TAB order by AD_CODE ";
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
                            if (rs1.getInt("AD_AMT") > 0) {
                                aqbean.setCol7(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol7List());
                            }
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
                            } else if (rs.getString("OFF_CODE") != null && (rs.getString("OFF_CODE").equals("OLSGAD0010001") || rs.getString("OFF_CODE").equals("OLSEDN0010003") || rs.getString("OFF_CODE").equals("OLSIND0010002")) && (aqbean.getAcctype().equals("EPF") && (rs1.getString("AD_CODE").equals("EPF")))) {
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
                                refdesc = aqfunctionalities.getRefDesc(adcode, nowdedn, aqslno, Integer.parseInt(mon), Integer.parseInt(year));
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
                                refdesc = aqfunctionalities.getRefDesc(adcode, nowdedn, aqslno, Integer.parseInt(mon), Integer.parseInt(year));
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
                int grosspay = aqfunctionalities.getGrossPay(rs.getString("AQSL_NO"), Integer.parseInt(mon), Integer.parseInt(year));
                int totaldedn = aqfunctionalities.getTotalDedn(rs.getString("AQSL_NO"), Integer.parseInt(mon), Integer.parseInt(year));
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

    public SectionWiseAqBean getAqBillDetailsContractual(String billno, String mon, String year, String section, int secslno, BillConfigObj billConfigObj) throws Exception {
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

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(Integer.parseInt(mon), Integer.parseInt(year));
            con = dataSource.getConnection();
            st = con.createStatement();
            pstamt = con.prepareStatement("SELECT * FROM (SELECT OFF_CODE,AQSL_NO,EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,SPC_ORD_NO,SPC_ORD_DATE,GPF_ACC_NO,ACCT_TYPE,EMP_CODE,post_sl_no,SEC_SL_NO "
                    + "FROM AQ_MAST WHERE AQ_MONTH=? AND AQ_YEAR=? AND BILL_NO=? AND SECTION=? AND SEC_SL_NO=? AND DEP_CODE != '11') AQ_MAST "
                    + "LEFT OUTER JOIN (SELECT EMP_ID,CUR_CADRE_CODE,IF_GPF_ASSUMED FROM EMP_MAST)EMP_MAST ON AQ_MAST.EMP_CODE = EMP_MAST.EMP_ID "
                    + "LEFT OUTER JOIN (SELECT CADRE_CODE,CADRE_ABBR FROM G_CADRE)G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE order by POST_SL_NO");

            pstamt.setInt(1, Integer.parseInt(mon));
            pstamt.setInt(2, Integer.parseInt(year));
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
                aqbean.setGpfacct(rs.getString("GPF_ACC_NO"));
                aqbean.setAccNo(rs.getString("BANK_ACC_NO"));
                aqbean.setAcctype(rs.getString("ACCT_TYPE"));
                aqbean.setBasic(rs.getString("CUR_BASIC"));
                aqslno = rs.getString("AQSL_NO");

                String queryClump = "select * from (select cnt,REP_COL,tmep4.AD_CODE,AD_AMT,SCHEDULE,tmep4.NOW_DEDN,AD_TYPE,sl_no,REF_DESC from(select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt = 1)tmep4 "
                        + "left outer join (SELECT AD_CODE,REF_DESC,NOW_DEDN FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp2 on "
                        + "tmep4.ad_code = temp2.ad_code and tmep4.now_dedn = temp2.now_dedn "
                        + "union "
                        + "select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no,cast('' as text) from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt > 1 )t1 order by SL_NO ";

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
                        //                            case 4:
                        //                                aqbean.setCol4(rs1.getString("AD_CODE"),rs1.getInt("AD_AMT"),rs1.getString("SCHEDULE"),rs1.getString("NOW_DEDN"),rs1.getString("REF_DESC"));
                        //                                break;
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
                            } else if (rs.getString("OFF_CODE") != null && (rs.getString("OFF_CODE").equals("OLSGAD0010001") || rs.getString("OFF_CODE").equals("OLSEDN0010003") || rs.getString("OFF_CODE").equals("OLSIND0010002")) && (aqbean.getAcctype().equals("EPF") && (rs1.getString("AD_CODE").equals("EPF")))) {
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
                                refdesc = aqfunctionalities.getRefDesc(adcode, nowdedn, aqslno, Integer.parseInt(mon), Integer.parseInt(year));
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
                                refdesc = aqfunctionalities.getRefDesc(adcode, nowdedn, aqslno, Integer.parseInt(mon), Integer.parseInt(year));
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
                int grosspay = aqfunctionalities.getGrossPay(rs.getString("AQSL_NO"), Integer.parseInt(mon), Integer.parseInt(year));
                int totaldedn = aqfunctionalities.getTotalDedn(rs.getString("AQSL_NO"), Integer.parseInt(mon), Integer.parseInt(year));
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
            //objBill.setAqlistSectionWise(aqlist);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return objBill;
    }

    public SectionWiseAqBean getAqBillDetailsContractualF2(String billno, String mon, String year, String section, int secslno, BillConfigObj billConfigObj) throws Exception {
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

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(Integer.parseInt(mon), Integer.parseInt(year));
            con = dataSource.getConnection();
            st = con.createStatement();
            pstamt = con.prepareStatement("SELECT * FROM (SELECT AQSL_NO,EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,SPC_ORD_NO,SPC_ORD_DATE,GPF_ACC_NO,ACCT_TYPE,EMP_CODE,post_sl_no,SEC_SL_NO "
                    + "FROM AQ_MAST WHERE AQ_MONTH=? AND AQ_YEAR=? AND BILL_NO=? AND SECTION=? AND SEC_SL_NO=? AND DEP_CODE != '11') AQ_MAST "
                    + "LEFT OUTER JOIN (SELECT EMP_ID,CUR_CADRE_CODE,IF_GPF_ASSUMED FROM EMP_MAST)EMP_MAST ON AQ_MAST.EMP_CODE = EMP_MAST.EMP_ID "
                    + "LEFT OUTER JOIN (SELECT CADRE_CODE,CADRE_ABBR FROM G_CADRE)G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE order by POST_SL_NO");

            pstamt.setInt(1, Integer.parseInt(mon));
            pstamt.setInt(2, Integer.parseInt(year));
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
                aqbean.setGpfacct(rs.getString("GPF_ACC_NO"));
                aqbean.setAccNo(rs.getString("BANK_ACC_NO"));
                aqbean.setAcctype(rs.getString("ACCT_TYPE"));
                aqbean.setBasic(rs.getString("CUR_BASIC"));
                aqslno = rs.getString("AQSL_NO");

                String queryClump = "select * from (select cnt,REP_COL,tmep4.AD_CODE,AD_AMT,SCHEDULE,tmep4.NOW_DEDN,AD_TYPE,sl_no,REF_DESC from(select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt = 1)tmep4 "
                        + "left outer join (SELECT AD_CODE,REF_DESC,NOW_DEDN FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp2 on "
                        + "tmep4.ad_code = temp2.ad_code and tmep4.now_dedn = temp2.now_dedn "
                        + "union "
                        + "select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no,cast('' as text) from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt > 1 )t1 order by SL_NO ";

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
                        //                            case 4:
                        //                                aqbean.setCol4(rs1.getString("AD_CODE"),rs1.getInt("AD_AMT"),rs1.getString("SCHEDULE"),rs1.getString("NOW_DEDN"),rs1.getString("REF_DESC"));
                        //                                break;
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
                                refdesc = aqfunctionalities.getRefDesc(adcode, nowdedn, aqslno, Integer.parseInt(mon), Integer.parseInt(year));
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
                                refdesc = aqfunctionalities.getRefDesc(adcode, nowdedn, aqslno, Integer.parseInt(mon), Integer.parseInt(year));
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
                int privatededuction = aqfunctionalities.getPrivateDeduction(rs.getString("AQSL_NO"), Integer.parseInt(mon), Integer.parseInt(year));
                int privateloan = aqfunctionalities.getPrivateLoan(rs.getString("AQSL_NO"), Integer.parseInt(mon), Integer.parseInt(year));
                int grosspay = aqfunctionalities.getGrossPay(rs.getString("AQSL_NO"), Integer.parseInt(mon), Integer.parseInt(year));
                int totaldedn = aqfunctionalities.getTotalDedn(rs.getString("AQSL_NO"), Integer.parseInt(mon), Integer.parseInt(year));

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
            objBill.setAqlistSectionWise(aqlist);
            //objBill.setAqlistSectionWise(aqlist);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return objBill;
    }

    public String getMonth(int mon) {
        String month = null;
        switch (mon) {
            case 0:
                month = "JANUARY";
                break;
            case 1:
                month = "FEBRUARY";
                break;
            case 2:
                month = "MARCH";
                break;
            case 3:
                month = "APRIL";
                break;
            case 4:
                month = "MAY";
                break;
            case 5:
                month = "JUNE";
                break;
            case 6:
                month = "JULY";
                break;
            case 7:
                month = "AUGUST";
                break;
            case 8:
                month = "SEPTEMBER";
                break;
            case 9:
                month = "OCTOBER";
                break;
            case 10:
                month = "NOVEMBER";
                break;
            case 11:
                month = "DECEMBER";
                break;
        }
        return month;

    }

    public int getOtherAllowance(String billNo, String mon, String year) {
        Connection con = null;
        Statement st = null;
        int oa = 0;
        ResultSet rs = null;;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            String sqloa = "SELECT SUM(ad_amt)oa_amt FROM (select ad_amt,SCHEDULE,AD_TYPE,ad_code from aq_dtls inner join (select aqsl_no from aq_mast where bill_no=" + billNo + " and aq_month='" + mon + "' and aq_year='" + year + "' )aq_mast "
                    + "on aq_dtls.aqsl_no = aq_mast.aqsl_no) where SCHEDULE='OA' AND AD_TYPE='A' AND ad_code != 'SP'";
            rs = st.executeQuery(sqloa);
            if (rs.next()) {
                oa = rs.getInt("oa_amt");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return oa;
    }

    public int getColGrandTotal(ArrayList aqlist, String col) {
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

                for (int j = 0; j < column.size(); j++) {
                    addbean = (ADDetailsHealperBean) column.get(j);

                    total = total + addbean.getAdamt();

                    /*if(addbean.getAdcode().equalsIgnoreCase("LFQ")){
                     }*/
                }
            }
        }
        return total;
    }

    public int getColGrandTotal(ArrayList aqlist, String col, String colName, String nowdedn) {
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

    public int getColGrandTotal(ArrayList aqlist, String col, int rowNo, String nowdedn) {
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

    public HashMap getPayAbstract(ArrayList aqlist) {
        HashMap payAbstract = new HashMap();
        int pay = 0;
        int da = 0;
        int hra = 0;
        int oa = 0;
        int dp = 0;
        AqreportHelperBean aqbean = null;
        ArrayList column3 = null;
        ArrayList column4 = null;
        ArrayList column5 = null;
        ArrayList column6 = null;
        ArrayList column7 = null;
        ADDetailsHealperBean addbean = null;

        for (int i = 0; i < aqlist.size(); i++) {
            SectionWiseAqBean sectionwiseAq = (SectionWiseAqBean) aqlist.get(i);
            ArrayList arrBill = sectionwiseAq.getAqlistSectionWise();
            for (int k = 0; k < arrBill.size(); k++) {
                if (arrBill.get(k) != null) {
                    aqbean = (AqreportHelperBean) arrBill.get(k);
                    if (aqlist.get(i) != null) {
                        column3 = (ArrayList) aqbean.getCol3();
                        column4 = (ArrayList) aqbean.getCol4();
                        column5 = (ArrayList) aqbean.getCol5();
                        column6 = (ArrayList) aqbean.getCol6();
                        column7 = (ArrayList) aqbean.getCol7();

                        pay = pay + Integer.parseInt(aqbean.getBasic());

                        for (int j = 0; j < column3.size(); j++) {
                            addbean = (ADDetailsHealperBean) column3.get(j);
                            pay = pay + addbean.getAdamt();
                        }
                        for (int j = 0; j < column4.size(); j++) {
                            addbean = (ADDetailsHealperBean) column4.get(j);
                            dp = dp + addbean.getAdamt();
                        }
                        for (int j = 0; j < column5.size(); j++) {
                            addbean = (ADDetailsHealperBean) column5.get(j);
                            if (addbean.getAdcode().equalsIgnoreCase("DA")) {
                                da = da + addbean.getAdamt();
                            }
                        }

                        for (int j = 0; j < column6.size(); j++) {
                            addbean = (ADDetailsHealperBean) column6.get(j);
                            if (addbean.getAdcode().equalsIgnoreCase("HRA") || addbean.getAdcode().equalsIgnoreCase("LFQ")) {
                                hra = hra + addbean.getAdamt();
                            } else {
                                oa = oa + addbean.getAdamt();
                            }
                            // hra = hra + addbean.getAdamt();
                        }
                        for (int j = 0; j < column7.size(); j++) {
                            addbean = (ADDetailsHealperBean) column7.get(j);
                            if (addbean.getAdcode().equalsIgnoreCase("HRA") || addbean.getAdcode().equalsIgnoreCase("LFQ")) {
                                hra = hra + addbean.getAdamt();
                            } else {
                                oa = oa + addbean.getAdamt();
                            }
                        }
                    }
                }
            }
        }
        payAbstract.put("pay", pay + "");
        payAbstract.put("dp", dp + "");
        payAbstract.put("da", da + "");
        payAbstract.put("hra", hra + "");
        payAbstract.put("oa", oa + "");

        return payAbstract;
    }

    @Override
    public List getAllowanceDetails(String billNo, int aqYear, int aqMonth) {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        List oaList = new ArrayList();
        Schedule sc = null;
        int oaTotal = 0;
        String aqDTLS = "AQ_DTLS";
        String amt = "";
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(aqMonth, aqYear);
            String oaSql = "Select SCHEDULE, sum(ad_amt) ad_amt, bt_id from " + aqDTLS + " a, aq_mast b where a.aqsl_no = b.aqsl_no "
                    + "and b.bill_no = '" + billNo + "' and b.aq_month = " + aqMonth + " "
                    + "and b.aq_year = " + aqYear + " and a.ad_type='A' and a.ad_amt >0 and a.AD_CODE !='SP' AND a.AD_CODE !='IR' "
                    + "group by a.SCHEDULE, bt_id";
            res = stmt.executeQuery(oaSql);
            while (res.next()) {

                sc = new Schedule();

                String schName = res.getString("SCHEDULE");
                amt = res.getString("AD_AMT");
                sc.setScheduleName("- " + schName);
                sc.setSchAmount(amt);

                oaTotal = oaTotal + Integer.parseInt(amt);
                sc.setAlowanceTotal(oaTotal);
                sc.setObjectHead(res.getString("bt_id"));

                oaList.add(sc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return oaList;
    }

    @Override
    public int getPayAmt(int billNo) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int amount = 0;

        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT sum(CUR_BASIC) AD_AMT from AQ_MAST WHERE BILL_NO=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, billNo);
            rs = pst.executeQuery();
            if (rs.next()) {
                amount = rs.getInt("AD_AMT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return amount;
    }

    //@Override
    public int getPayAmtArrear(int billNo) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int amount = 0;

        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT sum(CUR_BASIC) AD_AMT from ARR_MAST WHERE BILL_NO=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, billNo);
            rs = pst.executeQuery();
            if (rs.next()) {
                amount = rs.getInt("AD_AMT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return amount;
    }

    private StringBuffer reportPageHeader(CommonReportParamBean bean, int pageno, String column9NameList, String column10NameList, String column11NameList, String column12NameList, String column13NameList, String column14NameList, String column15NameList, String column16NameList, String column17NameList, String column18NameList) {
        StringBuffer header = null;
        try {
            PayrollCommonFunction prcf = new PayrollCommonFunction();
            //CommonReportParamBean bean = prcf.getCommonReportParameter(con, StringUtils.defaultString(billNo));

            header = new StringBuffer(" <table width=\"100%\" border=\"0\">\n"
                    + "                    <tr>\n"
                    + "                        <td width=\"8%\">STATE-" + bean.getStatename() + "</td>\n"
                    + "                        <td width=\"10%\" align=\"center\">DIST-" + bean.getDistrict() + "</td>        \n"
                    + "                        <td width=\"50%\" align=\"center\">SCHEDULE-A STATE HEAD QUATERS FORM NO-58 <br />\n"
                    + "                            PAY BILL FOR " + bean.getOfficename() + "<br/>\n"
                    + "                            MONTHLY PAY BILL FOR " + getMonth(bean.getAqmonth()) + "-" + bean.getAqyear() + "</td>\n"
                    + "                        <td width=\"15%\" align=\"center\">BILL NO:" + bean.getBilldesc() + "<br> BILL DT:" + bean.getBilldate() + "</td>\n"
                    + "                        <td width=\"7%\">PAGE:" + (pageno++) + "</td>\n"
                    + "                    </tr>\n"
                    + "\n"
                    + "                </table>\n"
                    + "                <table width=\"100%\" style=\"border-top: 0;border-bottom: 0\">\n"
                    + "                    <tr>\n"
                    + "                        <td width=\"2%\">SL NO</td>\n"
                    + "                        <td width=\"11%\">NAME/<br />DESG/<br />PAY SCALE </td>\n"
                    + "                        <td width=\"4%\">BASIC<br />SPL PAY<br/>GP<br/>IR</td>\n"
                    + "                        <td width=\"4%\">DP<br/>P.PAY</td>\n"
                    + "                        <td width=\"2%\">DA</td>\n"
                    + "                        <td width=\"3%\">HRA</td>\n"
                    + "                        <td width=\"4%\">OTHER<br />ALLOWANCE</td>\n"
                    + "                        <td width=\"5%\">GROSS<br />PAY</td>\n"
                    + "                        <td width=\"6%\">" + column9NameList + "</td>\n"
                    + "                        <td width=\"6%\">" + column10NameList + "</td>\n"
                    + "                        <td width=\"4%\">" + column11NameList + "</td>\n"
                    + "                        <td width=\"5%\">" + column12NameList + " </td>\n"
                    + "                        <td width=\"4%\">" + column13NameList + "</td>\n"
                    + "                        <td width=\"8%\">" + column14NameList + " </td>\n"
                    + "                        <td width=\"5%\">" + column15NameList + "</td>\n"
                    + "                        <td width=\"5%\">" + column16NameList + "</td>\n"
                    + "                        <td width=\"4%\">" + column17NameList + "</td>\n"
                    + "                        <td width=\"5%\">" + column18NameList + "</td>\n"
                    + "                        <td width=\"5%\">TOTAL<br />DEDN</td>\n"
                    + "                        <td width=\"4%\">NET PAY </td>\n"
                    + "                        <td width=\"7%\">REMARKS<br /> A/C NO </td>\n"
                    + "                    </tr>\n"
                    + "                    <tr>\n"
                    + "                        <td>(1)</td>\n"
                    + "                        <td>(2)</td>\n"
                    + "                        <td>(3)</td>\n"
                    + "                        <td>(4)</td>\n"
                    + "                        <td>(5)</td>\n"
                    + "                        <td>(6)</td>\n"
                    + "                        <td>(7)</td>\n"
                    + "                        <td>(8)</td>\n"
                    + "                        <td>(9)</td>\n"
                    + "                        <td>(10)</td>\n"
                    + "                        <td>(11)</td>\n"
                    + "                        <td>(12)</td>\n"
                    + "                        <td>(13)</td>\n"
                    + "                        <td>(14)</td>\n"
                    + "                        <td>(15)</td>\n"
                    + "                        <td>(16)</td>\n"
                    + "                        <td>(17)</td>\n"
                    + "                        <td>(18)</td>\n"
                    + "                        <td>(19)</td>\n"
                    + "                        <td>(20)</td>\n"
                    + "                        <td>(21)</td>\n"
                    + "                    </tr>");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return header;
    }

    private StringBuffer reportPageHeaderF2(CommonReportParamBean bean, int pageno, String column9NameList, String column10NameList, String column11NameList, String column12NameList, String column13NameList, String column14NameList, String column15NameList, String column16NameList, String column17NameList, String column18NameList) {
        StringBuffer header = null;
        try {

            header = new StringBuffer(" <table width=\"100%\" border=\"0\">\n"
                    + "                    <tr>\n"
                    + "                        <td width=\"8%\">STATE-" + bean.getStatename() + "</td>\n"
                    + "                        <td width=\"10%\" align=\"center\">DIST-" + bean.getDistrict() + "</td>        \n"
                    + "                        <td width=\"50%\" align=\"center\">SCHEDULE-A STATE HEAD QUATERS FORM NO-58 <br />\n"
                    + "                            PAY BILL FOR " + bean.getOfficename() + "<br/>\n"
                    + "                            MONTHLY PAY BILL FOR " + getMonth(bean.getAqmonth()) + "-" + bean.getAqyear() + "</td>\n"
                    + "                        <td width=\"15%\" align=\"center\">BILL NO:" + bean.getBilldesc() + "<br> BILL DT:" + bean.getBilldate() + "</td>\n"
                    + "                        <td width=\"7%\">PAGE:" + (pageno++) + "</td>\n"
                    + "                    </tr>\n"
                    + "\n"
                    + "                </table>\n"
                    + "                <table width=\"100%\" style=\"border-top: 0;border-bottom: 0\">\n"
                    + "                    <tr>\n"
                    + "                        <td width=\"2%\">SL NO</td>\n"
                    + "                        <td width=\"11%\">NAME/<br />DESG/<br />PAY SCALE </td>\n"
                    + "                        <td width=\"4%\">BASIC<br />SPL PAY<br/>GP<br/>IR</td>\n"
                    + "                        <td width=\"4%\">DP<br/>P.PAY</td>\n"
                    + "                        <td width=\"2%\">DA</td>\n"
                    + "                        <td width=\"3%\">HRA</td>\n"
                    + "                        <td width=\"4%\">OTHER<br />ALLOWANCE</td>\n"
                    + "                        <td width=\"5%\">GROSS<br />PAY</td>\n"
                    + "                        <td width=\"6%\">" + column9NameList + "</td>\n"
                    + "                        <td width=\"6%\">" + column10NameList + "</td>\n"
                    + "                        <td width=\"4%\">" + column11NameList + "</td>\n"
                    + "                        <td width=\"5%\">" + column12NameList + " </td>\n"
                    + "                        <td width=\"4%\">" + column13NameList + "</td>\n"
                    + "                        <td width=\"8%\">" + column14NameList + " </td>\n"
                    + "                        <td width=\"5%\">" + column15NameList + "</td>\n"
                    + "                        <td width=\"5%\">" + column16NameList + "</td>\n"
                    + "                        <td width=\"4%\">" + column17NameList + "</td>\n"
                    + "                        <td width=\"5%\">" + column18NameList + "</td>\n"
                    + "                        <td width=\"5%\">TOTAL<br />DEDN<br/>NET-PAY</td>\n"
                    + "                        <td width=\"4%\">PVTDED<br/>BANK LOAN<br/>NET BALANCE </td>\n"
                    + "                        <td width=\"7%\">REMARKS<br /> A/C NO </td>\n"
                    + "                    </tr>\n"
                    + "                    <tr>\n"
                    + "                        <td>(1)</td>\n"
                    + "                        <td>(2)</td>\n"
                    + "                        <td>(3)</td>\n"
                    + "                        <td>(4)</td>\n"
                    + "                        <td>(5)</td>\n"
                    + "                        <td>(6)</td>\n"
                    + "                        <td>(7)</td>\n"
                    + "                        <td>(8)</td>\n"
                    + "                        <td>(9)</td>\n"
                    + "                        <td>(10)</td>\n"
                    + "                        <td>(11)</td>\n"
                    + "                        <td>(12)</td>\n"
                    + "                        <td>(13)</td>\n"
                    + "                        <td>(14)</td>\n"
                    + "                        <td>(15)</td>\n"
                    + "                        <td>(16)</td>\n"
                    + "                        <td>(17)</td>\n"
                    + "                        <td>(18)</td>\n"
                    + "                        <td>(19)</td>\n"
                    + "                        <td>(20)</td>\n"
                    + "                        <td>(21)</td>\n"
                    + "                    </tr>");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return header;
    }

    @Override
    public double getTotalAllowance(String aqSlNo, String aqDtlsTableName) {
        Connection con = null;
        String query = null;
        ResultSet rs = null;
        double totalAllowance = 0;
        PreparedStatement pst = null;

        try {
            con = this.repodataSource.getConnection();
            query = "SELECT SUM(AD_AMT)ADAMT FROM " + aqDtlsTableName + " WHERE AD_TYPE='A' AND AQSL_NO=?";
            pst = con.prepareStatement(query);
            pst.setString(1, aqSlNo);
            rs = pst.executeQuery();
            if (rs.next()) {
                totalAllowance = rs.getDouble("ADAMT");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return totalAllowance;
    }

    @Override
    public double getTotalDeduction(String aqSlNo, String aqDtlsTableName) {
        Connection con = null;
        String query = null;
        ResultSet rs = null;
        double totalDeduction = 0;
        PreparedStatement pst = null;

        try {
            con = this.repodataSource.getConnection();
            query = "SELECT SUM(AD_AMT)ADAMT FROM " + aqDtlsTableName + " WHERE AD_TYPE='D' AND AQSL_NO=?";
            pst = con.prepareStatement(query);
            pst.setString(1, aqSlNo);
            rs = pst.executeQuery();
            if (rs.next()) {
                totalDeduction = rs.getDouble("ADAMT");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return totalDeduction;
    }

    @Override
    public void saveAquitanceBasic(String aqslNo, int aqbasic) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE AQ_MAST SET MON_BASIC=?, CUR_BASIC=? WHERE AQSL_NO=?");
            pstmt.setInt(1, aqbasic);
            pstmt.setInt(2, aqbasic);
            pstmt.setString(3, aqslNo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public int getAquitanceBasic(String aqslNo) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        int basic = 0;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT MON_BASIC FROM AQ_MAST WHERE AQSL_NO=?");
            pstmt.setString(1, aqslNo);
            res = pstmt.executeQuery();
            if (res.next()) {
                basic = res.getInt("MON_BASIC");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return basic;
    }

    @Override
    public void downloadAqReportExcel(OutputStream out, String fileName, WritableWorkbook workbook, String billNo) {
        Connection con = null;
        Statement st = null;
        Statement st1 = null;
        ResultSet res = null;
        ResultSet res1 = null;

        int gross = 0;
        int grosstotal = 0;
        int totalded = 0;
        int netpay = 0;
        int totalpvtded = 0;
        int balance = 0;

        String aqDTLS = "AQ_DTLS";

        int col = 0;
        int repcol = 0;

        PreparedStatement pst = null;
        ResultSet rs = null;

        /*String queryClump5 = "select * from (select cnt,REP_COL,tmep4.AD_CODE,AD_AMT,SCHEDULE,tmep4.NOW_DEDN,AD_TYPE,sl_no,REF_DESC from(select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no from "
         + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM AQ_DTLS WHERE AQSL_NO=?) temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt = 1)tmep4 "
         + "left outer join (SELECT AD_CODE,REF_DESC,NOW_DEDN FROM AQ_DTLS WHERE AQSL_NO=?) temp2 on "
         + "tmep4.ad_code = temp2.ad_code and tmep4.now_dedn = temp2.now_dedn "
         + "union "
         + "select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no,''::text from "
         + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM AQ_DTLS WHERE AQSL_NO=?) temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt > 1 ) S1 order by SL_NO ";*/
        try {
            con = this.dataSource.getConnection();

            String sql = "select aq_year,aq_month from bill_mast where bill_no=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billNo));
            rs = pst.executeQuery();
            if (rs.next()) {
                aqDTLS = AqFunctionalities.getAQBillDtlsTable(rs.getInt("aq_month"), rs.getInt("aq_year"));
            }

            //pst = con.prepareStatement(queryClump5);
            BillConfigObj billConfig = getBillConfigForAQReportExcel(billNo);
            String[] col6 = billConfig.getCol6List();
            String[] col7 = billConfig.getCol7List();

            st = con.createStatement();

            WritableSheet sheet = workbook.createSheet("Aquitance", 0);
            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            WritableCellFormat headcell = new WritableCellFormat(headformat);
            WritableFont cellformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
            WritableCellFormat innercell = new WritableCellFormat(cellformat);

            WritableCellFormat colorheadercell = new WritableCellFormat(headformat);
            colorheadercell.setBorder(Border.RIGHT, BorderLineStyle.MEDIUM_DASHED, Colour.RED);

            WritableCellFormat colorcell = new WritableCellFormat(cellformat);
            colorcell.setBorder(Border.RIGHT, BorderLineStyle.MEDIUM_DASHED, Colour.RED);

            /*Print Bill Header*/
            sheet.mergeCells(0, 0, 0, 1);
            Label label = new Label(0, 0, "EMP NAME", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(1, 0, 1, 1);
            label = new Label(1, 0, "HRMS ID/GPF NO", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(2, 0, 2, 1);
            label = new Label(2, 0, "POST", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(3, 0, 3, 1);
            label = new Label(3, 0, "BANK_ACC_NO", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(4, 0, 4, 1);
            label = new Label(4, 0, "PAY SCALE", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(5, 0, 5, 1);
            label = new Label(5, 0, "BASIC", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(6, 0, 6, 1);
            label = new Label(6, 0, "SPECIAL PAY", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(7, 0, 7, 1);
            label = new Label(7, 0, "GRADE PAY", headcell);//column,row
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
            //sheet.mergeCells(10,0,10,1);
            if (col6 == null) {
                sheet.mergeCells(11, 0, 11, 1);
                label = new Label(11, 0, "HRA", headcell);//column,row
                sheet.addCell(label);
                col = 11;
            } else if (col6 != null && col6.length > 0) {
                col = 10;
                for (int i = 0; i < col6.length; i++) {
                    col = col + 1;
                    label = new Label(col, 1, getADCodeDesc(col6[i]), headcell);
                    sheet.addCell(label);
                }
            }
            if (col7 != null && col7.length > 0) {
                col++;
                sheet.mergeCells(col, 0, (col + col7.length), 0);
                label = new Label(col, 0, "OTHER ALLOWANCE", headcell);//column,row
                sheet.addCell(label);
                for (int i = 0; i < col7.length; i++) {

                    label = new Label(col, 1, getADCodeDesc(col7[i]), headcell);
                    sheet.addCell(label);
                    col = col + 1;
                }
            } else {
                //col = 11;
            }
            //col = col + 1;
            label = new Label(col, 1, "GROSS", headcell);
            sheet.addCell(label);

            String queryClump = "select * from (select cnt,REP_COL,tmep4.AD_CODE,AD_AMT,SCHEDULE,tmep4.NOW_DEDN,AD_TYPE,sl_no,REF_DESC from(select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no from "
                    + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO=?) temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt = 1)tmep4 "
                    + "left outer join (SELECT AD_CODE,REF_DESC,NOW_DEDN FROM " + aqDTLS + " WHERE AQSL_NO=?) temp2 on "
                    + "tmep4.ad_code = temp2.ad_code and tmep4.now_dedn = temp2.now_dedn "
                    + "union "
                    + "select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no,''::text from "
                    + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO=?) temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt > 1 ) S1 order by SL_NO ";
            pst = con.prepareStatement(queryClump);

            res = st.executeQuery("SELECT  AQSL_NO,EMP_CODE,EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,GPF_ACC_NO FROM AQ_MAST WHERE  BILL_NO='" + billNo + "' AND EMP_NAME IS NOT NULL order by post_sl_no");
            int row = 1;
            int row1 = 1;
            while (res.next()) {
                //int netPay = AqFunctionalities.getGrossPay(con,rs.getString("AQSL_NO"))-AqFunctionalities.getTotalDedn(con,rs.getString("AQSL_NO"));
                row++;
                row1++;
                label = new Label(0, row, res.getString("EMP_NAME"), innercell);
                sheet.addCell(label);
                label = new Label(1, row, res.getString("EMP_CODE") + " / " + res.getString("GPF_ACC_NO"), innercell);
                sheet.addCell(label);
                label = new Label(2, row, res.getString("CUR_DESG"), innercell);
                sheet.addCell(label);
                label = new Label(3, row, res.getString("BANK_ACC_NO"), innercell);
                sheet.addCell(label);
                label = new Label(4, row, res.getString("PAY_SCALE"), innercell);
                sheet.addCell(label);
                label = new Label(5, row, res.getString("CUR_BASIC"), innercell);
                sheet.addCell(label);

                pst.setString(1, res.getString("AQSL_NO"));
                pst.setString(2, res.getString("AQSL_NO"));
                pst.setString(3, res.getString("AQSL_NO"));
                res1 = pst.executeQuery();
                //rs1 = st1.executeQuery("SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,REF_DESC,NOW_DEDN,AD_TYPE FROM AQ_DTLS WHERE AQSL_NO='"+rs.getString("AQSL_NO")+"' order by sl_no");
                boolean inWhile = false;

                while (res1.next()) {
                    inWhile = true;
                    String adcode = res1.getString("AD_CODE");
                    repcol = res1.getInt("REP_COL");
                    if (adcode.equals("SP")) {
                        gross = gross + res1.getInt("AD_AMT");
                        label = new Label(6, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("GP")) {
                        gross = gross + res1.getInt("AD_AMT");
                        label = new Label(7, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("DP")) {
                        gross = gross + res1.getInt("AD_AMT");
                        label = new Label(8, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }

                    if (adcode.equals("DA")) {
                        gross = gross + res1.getInt("AD_AMT");
                        label = new Label(9, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("IR")) {
                        gross = gross + res1.getInt("AD_AMT");
                        label = new Label(10, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (col6 == null) {
                        if (adcode.equals("HRA")) {
                            gross = gross + res1.getInt("AD_AMT");
                            label = new Label(11, row, res1.getString("AD_AMT"), innercell);
                            sheet.addCell(label);
                        }
                        col = 11;
                    } else if (col6 != null && col6.length > 0) {
                        col = 10;
                        for (int i = 0; i < col6.length; i++) {
                            col = col + 1;
                            if (repcol == 6) {
                                if (adcode.equals(col6[i])) {
                                    gross = gross + res1.getInt("AD_AMT");
                                    label = new Label(col, row, res1.getString("AD_AMT"), innercell);
                                    sheet.addCell(label);
                                }
                            }
                        }
                    }
                    //col = col + 1;

                    if (col7 != null && col7.length > 0) {
                        for (int j = 0; j < col7.length; j++) {
                            col = col + 1;
                            if (repcol == 7) {
                                if (adcode.equals(col7[j])) {
                                    gross = gross + res1.getInt("AD_AMT");
                                    label = new Label(col, row, res1.getString("AD_AMT"), innercell);
                                    sheet.addCell(label);
                                }
                            }
                        }
                    } else {
                        col = 11;
                    }
                }
                if (inWhile == false) {
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
                }
                col = col + 1;
                gross = gross + res.getInt("CUR_BASIC");
                label = new Label(col, row, "" + gross, innercell);
                sheet.addCell(label);
                gross = 0;
            }

            DataBaseFunctions.closeSqlObjects(res, st1);
            DataBaseFunctions.closeSqlObjects(res, st);

            row = row + 2;
            label = new Label(6, row, "DEDUCTIONS", headcell);
            sheet.addCell(label);

            row++;

            label = new Label(0, row, "EMP NAME", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(1,0,1,1);
            label = new Label(1, row, "HRMS ID/GPF NO", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(2,0,2,1);
            label = new Label(2, row, "POST", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(3,0,3,1);
            label = new Label(3, row, "PAY SCALE", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(4,0,4,1);
            label = new Label(4, row, "BASIC", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(5,0,5,1);
            label = new Label(5, row, getADCodeDesc("GPF"), headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(6,0,6,1);
            label = new Label(6, row, getADCodeDesc("CPF"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(7, row, getADCodeDesc("TPF"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(8, row, getADCodeDesc("HRR"), headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(7,0,7,1);
            label = new Label(9, row, getADCodeDesc("FA"), headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(8,0,8,1);
            label = new Label(10, row, getADCodeDesc("MCA"), headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(9,0,9,1);
            label = new Label(11, row, getADCodeDesc("OR"), headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(10,0,10,1);
            label = new Label(12, row, getADCodeDesc("HBA"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(13, row, getADCodeDesc("PT"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(14, row, getADCodeDesc("GA"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(15, row, getADCodeDesc("VE"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(16, row, getADCodeDesc("SHBA"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(17, row, getADCodeDesc("MOPA"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(18, row, getADCodeDesc("GPDD"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(19, row, getADCodeDesc("HC"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(20, row, getADCodeDesc("LIC"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(21, row, getADCodeDesc("PA"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(22, row, getADCodeDesc("HIGL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(23, row, getADCodeDesc("LIGL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(24, row, getADCodeDesc("MIGL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(25, row, getADCodeDesc("MAL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(26, row, getADCodeDesc("GPIR"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(27, row, getADCodeDesc("GIS"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(28, row, getADCodeDesc("IT"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(29, row, getADCodeDesc("WRR"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(30, row, getADCodeDesc("GISA"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(31, row, getADCodeDesc("VE"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(32, row, getADCodeDesc("EP"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(33, row, getADCodeDesc("SWR"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(34, row, getADCodeDesc("CMPA"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(35, row, getADCodeDesc("EV"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(36, row, "Total Deductions", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(11,0,15,0);

            queryClump = "select * from (select cnt,REP_COL,tmep4.AD_CODE,AD_AMT,SCHEDULE,tmep4.NOW_DEDN,AD_TYPE,sl_no,REF_DESC from(select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no from "
                    + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO=?) temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt = 1)tmep4 "
                    + "left outer join (SELECT AD_CODE,REF_DESC,NOW_DEDN FROM " + aqDTLS + " WHERE AQSL_NO=?) temp2 on "
                    + "tmep4.ad_code = temp2.ad_code and tmep4.now_dedn = temp2.now_dedn "
                    + "union "
                    + "select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no,''::text from "
                    + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO=?) temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt > 1 ) S1 order by SL_NO ";
            pst = con.prepareStatement(queryClump);

            st = con.createStatement();
            res = st.executeQuery("SELECT  AQSL_NO,EMP_CODE,EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,GPF_ACC_NO FROM AQ_MAST WHERE  BILL_NO='" + billNo + "' AND EMP_NAME IS NOT NULL order by post_sl_no");
            while (res.next()) {
                row++;
                row1++;

                label = new Label(0, row, res.getString("EMP_NAME"), innercell);
                sheet.addCell(label);
                label = new Label(1, row, res.getString("EMP_CODE") + " / " + res.getString("GPF_ACC_NO"), innercell);
                sheet.addCell(label);
                label = new Label(2, row, res.getString("CUR_DESG"), innercell);
                sheet.addCell(label);
                label = new Label(3, row, res.getString("PAY_SCALE"), innercell);
                sheet.addCell(label);
                label = new Label(4, row, res.getString("CUR_BASIC"), innercell);
                sheet.addCell(label);

                pst.setString(1, res.getString("AQSL_NO"));
                pst.setString(2, res.getString("AQSL_NO"));
                pst.setString(3, res.getString("AQSL_NO"));
                res1 = pst.executeQuery();
                while (res1.next()) {
                    String adcode = res1.getString("AD_CODE");
                    if (adcode.equals("GPF")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(5, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("CPF")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(6, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("TPF")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(7, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("HRR")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(8, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("FA")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(9, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("MCA")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(10, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("OR")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(11, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("HBA")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(12, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("PT")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(13, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("GA")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(14, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("VE")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(15, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("SHBA")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(16, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("MOPA")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(17, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("GPDD")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(18, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("HC")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(19, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("LIC")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(20, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("PA")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(21, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("HIGL")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(22, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("LIGL")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(23, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("MIGL")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(24, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("MAL")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(25, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("GPIR")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(26, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("GIS")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(27, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("IT")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(28, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("WRR")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(29, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("GISA")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(30, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("VE")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(31, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("EP")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(32, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("SWR")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(33, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("CMPA")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(34, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("EV")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label = new Label(35, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                }
                label = new Label(36, row, totalded + "", innercell);
                sheet.addCell(label);
                totalded = 0;
            }

            DataBaseFunctions.closeSqlObjects(res, st1);
            DataBaseFunctions.closeSqlObjects(res, st);

            row = row + 2;
            label = new Label(6, row, "PRIVATE DEDUCTIONS", headcell);
            sheet.addCell(label);

            row++;

            label = new Label(0, row, "EMP NAME", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(1,0,1,1);
            label = new Label(1, row, "HRMS ID/GPF NO", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(2,0,2,1);
            label = new Label(2, row, "POST", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(3,0,3,1);
            label = new Label(3, row, "PAY SCALE", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(4,0,4,1);
            label = new Label(4, row, "BASIC", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(5,0,5,1);
            label = new Label(5, row, getADCodeDesc("ALBDL"), headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(6,0,6,1);
            label = new Label(6, row, getADCodeDesc("ASSCN"), headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(7,0,7,1);
            label = new Label(7, row, getADCodeDesc("BADN"), headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(8,0,8,1);
            label = new Label(8, row, getADCodeDesc("BANKLN"), headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(9,0,9,1);
            label = new Label(9, row, getADCodeDesc("CLUB"), headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(10,0,10,1);
            label = new Label(10, row, getADCodeDesc("CMRF"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(11, row, getADCodeDesc("CRFL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(12, row, getADCodeDesc("DHE"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(13, row, getADCodeDesc("DTHREL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(14, row, getADCodeDesc("ELCTRC"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(15, row, getADCodeDesc("FARD"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(16, row, getADCodeDesc("FC"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(17, row, getADCodeDesc("FL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(18, row, getADCodeDesc("GOVTRC"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(19, row, getADCodeDesc("HFY"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(20, row, getADCodeDesc("MISDED"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(21, row, getADCodeDesc("NGBL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(22, row, getADCodeDesc("OPW"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(23, row, getADCodeDesc("OPWLN"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(24, row, getADCodeDesc("OSCBL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(25, row, getADCodeDesc("PAYSLP"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(26, row, getADCodeDesc("SBIKL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(27, row, getADCodeDesc("SBIL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(28, row, getADCodeDesc("SBILN"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(29, row, getADCodeDesc("SOCIL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(30, row, getADCodeDesc("SPRTS"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(31, row, getADCodeDesc("SYNDC"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(32, row, getADCodeDesc("SYNDLN"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(33, row, getADCodeDesc("TEMPFND"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(34, row, getADCodeDesc("TEMPHRR"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(35, row, getADCodeDesc("UBI"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(36, row, getADCodeDesc("UCB"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(37, row, getADCodeDesc("UCOLN"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(38, row, getADCodeDesc("UWFND"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(39, row, getADCodeDesc("UWLN"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(40, row, getADCodeDesc("WC"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(41, row, getADCodeDesc("DHOBI"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(42, row, getADCodeDesc("CRPWF"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(43, row, "Total Pvt Deductions", headcell);//column,row
            sheet.addCell(label);

            queryClump = "SELECT AD_AMT,AD_DESC,AD_CODE,SCHEDULE FROM " + aqDTLS + " AQ_DTLS WHERE (AQ_DTLS.SCHEDULE='PVTL' OR"
                    + " AQ_DTLS.SCHEDULE='PVTD') AND EMP_CODE=? AND AQ_DTLS.AQSL_NO = ?"
                    + " AND AD_AMT>0";
            pst = con.prepareStatement(queryClump);

            st = con.createStatement();
            /*res = st.executeQuery("SELECT AQ_MAST.AQSL_NO,AQ_MAST.EMP_CODE,AQ_MAST.EMP_NAME,AQ_MAST.CUR_DESG,AQ_MAST.PAY_SCALE,AQ_MAST.BANK_ACC_NO,AQ_MAST.CUR_BASIC,AQ_MAST.GPF_ACC_NO FROM"
             + " (SELECT * FROM AQ_MAST WHERE AQ_MAST.BILL_NO='" + billNo + "')AQ_MAST"
             + " INNER JOIN  (SELECT AQ_DTLS.EMP_CODE FROM AQ_DTLS WHERE (AQ_DTLS.SCHEDULE='PVTL'"
             + " OR AQ_DTLS.SCHEDULE='PVTD') AND AQ_DTLS.AD_TYPE='D' AND AD_AMT>0 group by EMP_CODE)AQ_DTLS ON"
             + " AQ_DTLS.EMP_CODE=AQ_MAST.EMP_CODE ORDER BY POST_SL_NO");*/
            res = st.executeQuery("SELECT AQ_MAST.AQSL_NO,AQ_MAST.EMP_CODE,AQ_MAST.EMP_NAME,AQ_MAST.CUR_DESG,AQ_MAST.PAY_SCALE,AQ_MAST.BANK_ACC_NO,AQ_MAST.CUR_BASIC,AQ_MAST.GPF_ACC_NO FROM AQ_MAST WHERE AQ_MAST.BILL_NO='" + billNo + "' ORDER BY POST_SL_NO");
            while (res.next()) {
                row++;
                row1++;

                label = new Label(0, row, res.getString("EMP_NAME"), innercell);
                sheet.addCell(label);
                label = new Label(1, row, res.getString("EMP_CODE") + " / " + res.getString("GPF_ACC_NO"), innercell);
                sheet.addCell(label);
                label = new Label(2, row, res.getString("CUR_DESG"), innercell);
                sheet.addCell(label);
                label = new Label(3, row, res.getString("PAY_SCALE"), innercell);
                sheet.addCell(label);
                label = new Label(4, row, res.getString("CUR_BASIC"), innercell);
                sheet.addCell(label);

                pst.setString(1, res.getString("EMP_CODE"));
                pst.setString(2, res.getString("AQSL_NO"));

                res1 = pst.executeQuery();
                while (res1.next()) {
                    String adcode = res1.getString("AD_CODE");
                    if (adcode.equals("ALBDL")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(5, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("ASSCN")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(6, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("BADN")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(7, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("BANKLN")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(8, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("CLUB")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(9, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("CMRF")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(10, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("CRFL")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(11, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("DHE")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(12, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("DTHREL")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(13, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("ELCTRC")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(14, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("FARD")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(15, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("FC")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(16, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("FL")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(17, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("GOVTRC")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(18, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("HFY")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(19, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("MISDED")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(20, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("NGBL")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(21, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("OPW")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(22, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("OPWLN")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(23, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("OSCBL")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(24, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("PAYSLP")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(25, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("SBIKL")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(26, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("SBIL")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(27, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("SBILN")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(28, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("SOCIL")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(29, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("SPRTS")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(30, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("SYNDC")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(31, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("SYNDLN")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(32, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("TEMPFND")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(33, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("TEMPHRR")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(34, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("UBI")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(35, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("UCB")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(36, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("UCOLN")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(37, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("UWFND")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(38, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("UWLN")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(39, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("WC")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(40, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("DHOBI")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(41, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("CRPWF")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label = new Label(42, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label);
                    }
                }
                label = new Label(42, row, totalpvtded + "", innercell);
                sheet.addCell(label);
                totalpvtded = 0;
            }

            //Creating Second Sheet
            sheet = workbook.createSheet("Aquitance2", 1);

            sheet.mergeCells(0, 0, 0, 1);
            Label label2 = new Label(0, 0, "EMP NAME", headcell);//column,row
            sheet.addCell(label2);
            sheet.mergeCells(1, 0, 1, 1);
            label2 = new Label(1, 0, "GPF NO", headcell);//column,row
            sheet.addCell(label2);
            sheet.mergeCells(2, 0, 2, 1);
            label2 = new Label(2, 0, "POST", headcell);//column,row
            sheet.addCell(label2);
            sheet.mergeCells(3, 0, 3, 1);
            label2 = new Label(3, 0, "BANK_ACC_NO", headcell);//column,row
            sheet.addCell(label2);
            sheet.mergeCells(4, 0, 4, 1);
            label2 = new Label(4, 0, "PAY SCALE", headcell);//column,row
            sheet.addCell(label2);
            sheet.mergeCells(5, 0, 5, 1);
            label2 = new Label(5, 0, "BASIC", headcell);//column,row
            sheet.addCell(label2);
            sheet.mergeCells(6, 0, 6, 1);
            label2 = new Label(6, 0, "SPECIAL PAY", headcell);//column,row
            sheet.addCell(label2);
            sheet.mergeCells(7, 0, 7, 1);
            label2 = new Label(7, 0, "GRADE PAY", headcell);//column,row
            sheet.addCell(label2);
            sheet.mergeCells(8, 0, 8, 1);
            label2 = new Label(8, 0, "DP", headcell);//column,row
            sheet.addCell(label2);
            sheet.mergeCells(9, 0, 9, 1);
            label2 = new Label(9, 0, "DA", headcell);//column,row
            sheet.addCell(label2);
            sheet.mergeCells(10, 0, 10, 1);
            label2 = new Label(10, 0, "IR", headcell);//column,row
            sheet.addCell(label2);
            if (col6 == null) {
                sheet.mergeCells(11, 0, 11, 1);
                label2 = new Label(11, 0, "HRA", headcell);//column,row
                sheet.addCell(label2);
                col = 11;
            } else if (col6 != null && col6.length > 0) {
                col = 10;
                for (int i = 0; i < col6.length; i++) {
                    col = col + 1;
                    label2 = new Label(col, 1, getADCodeDesc(col6[i]), headcell);
                    sheet.addCell(label2);
                }
            }
            if (col7 != null && col7.length > 0) {
                //sheet.mergeCells(11,0,18,0);
                label2 = new Label(11, 0, "OTHER ALLOWANCE", headcell);//column,row
                sheet.addCell(label2);
                for (int i = 0; i < col7.length; i++) {
                    col = col + 1;
                    label2 = new Label(col, 1, getADCodeDesc(col7[i]), headcell);
                    sheet.addCell(label2);
                }
            } else {
                col = 11;
            }
            col = col + 1;
            label2 = new Label(col, 1, "GROSS", colorheadercell);//column,row
            sheet.addCell(label2);

            row = 0;

            //sheet.mergeCells(28,0,35,0);
            label2 = new Label(32, row, "DEDUCTIONS", headcell);
            sheet.addCell(label2);

            row = 1;

            col = col + 2;
            label2 = new Label(col, row, getADCodeDesc("GPF"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("CPF"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("TPF"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("HRR"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("FA"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("MCA"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("OR"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("HBA"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("PT"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("GA"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("VE"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("SHBA"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("MOPA"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("GPDD"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("HC"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("LIC"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("PA"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("HIGL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("LIGL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("MIGL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("MAL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("GPIR"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("GIS"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("IT"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("WRR"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("GISA"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("VE"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("EP"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("SWR"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("CMPA"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, "Total Deductions", colorheadercell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, "Net Pay", colorheadercell);//column,row
            sheet.addCell(label2);
            //sheet.mergeCells(11,0,15,0);

            row = 0;
            col = col + 3;
            label2 = new Label(col, row, "PRIVATE DEDUCTIONS", headcell);
            sheet.addCell(label2);

            row = 1;

            //col = col;
            label2 = new Label(col, row, getADCodeDesc("ALBDL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("ASSCN"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("BADN"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("BANKLN"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("CLUB"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("CMRF"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("CRFL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("DHE"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("DTHREL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("ELCTRC"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("FARD"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("FC"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("FL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("GOVTRC"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("HFY"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("MISDED"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("NGBL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("OPW"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("OPWLN"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("OSCBL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("PAYSLP"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("SBIKL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("SBIL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("SBILN"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("SOCIL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("SPRTS"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("SYNDC"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("SYNDLN"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("TEMPFND"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("TEMPHRR"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("UBI"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("UCB"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("UCOLN"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("UWFND"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("UWLN"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("WC"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("DHOBI"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, getADCodeDesc("CRPWF"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, "Total Pvt Deductions", headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, "Net Balance", headcell);//column,row
            sheet.addCell(label2);

            DataBaseFunctions.closeSqlObjects(res, st);

            String queryClump1 = "select * from (select cnt,REP_COL,tmep4.AD_CODE,AD_AMT,SCHEDULE,tmep4.NOW_DEDN,AD_TYPE,sl_no,REF_DESC from(select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no from "
                    + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO=?) temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt = 1)tmep4 "
                    + "left outer join (SELECT AD_CODE,REF_DESC,NOW_DEDN FROM " + aqDTLS + " WHERE AQSL_NO=?) temp2 on "
                    + "tmep4.ad_code = temp2.ad_code and tmep4.now_dedn = temp2.now_dedn "
                    + "union "
                    + "select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no,''::text from "
                    + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO=?) temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt > 1 ) S1 order by SL_NO ";
            pst = con.prepareStatement(queryClump1);

            st = con.createStatement();
            res = st.executeQuery("SELECT AQSL_NO,EMP_NAME,EMP_CODE,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,GPF_ACC_NO FROM AQ_MAST WHERE  BILL_NO='" + billNo + "' AND EMP_NAME IS NOT NULL order by post_sl_no");

            row = 1;
            row1 = 1;

            while (res.next()) {
                //int netPay = AqFunctionalities.getGrossPay(con,rs.getString("AQSL_NO"))-AqFunctionalities.getTotalDedn(con,rs.getString("AQSL_NO"));
                row++;
                row1++;
                //col = 10;

                label2 = new Label(0, row, res.getString("EMP_NAME"), innercell);
                sheet.addCell(label2);
                label2 = new Label(1, row, res.getString("GPF_ACC_NO"), innercell);
                sheet.addCell(label2);
                label2 = new Label(2, row, res.getString("CUR_DESG"), innercell);
                sheet.addCell(label2);
                label2 = new Label(3, row, res.getString("BANK_ACC_NO"), innercell);
                sheet.addCell(label2);
                label2 = new Label(4, row, res.getString("PAY_SCALE"), innercell);
                sheet.addCell(label2);
                label2 = new Label(5, row, res.getString("CUR_BASIC"), innercell);
                sheet.addCell(label2);
                gross = gross + res.getInt("CUR_BASIC");

                /*String queryClump = "select * from (select cnt,REP_COL,tmep4.AD_CODE,AD_AMT,SCHEDULE,tmep4.NOW_DEDN,AD_TYPE,sl_no,REF_DESC from(select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no from " + 
                 "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM AQ_DTLS WHERE AQSL_NO='"+res.getString("AQSL_NO")+"') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt = 1)tmep4 " + 
                 "left outer join (SELECT AD_CODE,REF_DESC,NOW_DEDN FROM AQ_DTLS WHERE AQSL_NO='"+res.getString("AQSL_NO")+"') temp2 on " + 
                 "tmep4.ad_code = temp2.ad_code and tmep4.now_dedn = temp2.now_dedn " + 
                 "union " + 
                 "select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no,'' from " + 
                 "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM AQ_DTLS WHERE AQSL_NO='"+res.getString("AQSL_NO")+"') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt > 1 ) order by SL_NO ";
                
                 st1 = con.createStatement();     */
                String queryClump5 = "select * from (select cnt,REP_COL,tmep4.AD_CODE,AD_AMT,SCHEDULE,tmep4.NOW_DEDN,AD_TYPE,sl_no,REF_DESC from(select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO=?) temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt = 1)tmep4 "
                        + "left outer join (SELECT AD_CODE,REF_DESC,NOW_DEDN FROM " + aqDTLS + " WHERE AQSL_NO=?) temp2 on "
                        + "tmep4.ad_code = temp2.ad_code and tmep4.now_dedn = temp2.now_dedn "
                        + "union "
                        + "select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no,''::text from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO=?) temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt > 1 ) S1 order by SL_NO ";
                pst = con.prepareStatement(queryClump5);
                pst.setString(1, res.getString("AQSL_NO"));
                pst.setString(2, res.getString("AQSL_NO"));
                pst.setString(3, res.getString("AQSL_NO"));
                res1 = pst.executeQuery();
                //rs1 = st1.executeQuery("SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,REF_DESC,NOW_DEDN,AD_TYPE FROM AQ_DTLS WHERE AQSL_NO='"+rs.getString("AQSL_NO")+"' order by sl_no");
                boolean insideWhile = false;
                while (res1.next()) {
                    insideWhile = true;
                    String adcode = res1.getString("AD_CODE");
                    repcol = res1.getInt("REP_COL");
                    if (adcode.equals("SP")) {
                        gross = gross + res1.getInt("AD_AMT");
                        label2 = new Label(6, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    if (adcode.equals("GP")) {
                        gross = gross + res1.getInt("AD_AMT");
                        label2 = new Label(7, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    if (adcode.equals("DP")) {
                        gross = gross + res1.getInt("AD_AMT");
                        label2 = new Label(8, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }

                    if (adcode.equals("DA")) {
                        gross = gross + res1.getInt("AD_AMT");
                        label2 = new Label(9, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    if (adcode.equals("IR")) {
                        gross = gross + res1.getInt("AD_AMT");
                        label2 = new Label(10, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    if (col6 == null) {
                        if (adcode.equals("HRA")) {
                            gross = gross + res1.getInt("AD_AMT");
                            label2 = new Label(11, row, res1.getString("AD_AMT"), innercell);
                            sheet.addCell(label2);
                        }
                        col = 11;
                    } else if (col6 != null && col6.length > 0) {
                        col = 10;
                        for (int i = 0; i < col6.length; i++) {
                            col = col + 1;
                            if (repcol == 6) {
                                if (adcode.equals(col6[i])) {
                                    gross = gross + res1.getInt("AD_AMT");
                                    label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                                    sheet.addCell(label2);
                                }
                            }
                        }
                    }
                    //col = col + 1;

                    if (col7 != null && col7.length > 0) {
                        for (int j = 0; j < col7.length; j++) {
                            col = col + 1;
                            if (repcol == 7) {
                                if (adcode.equals(col7[j])) {
                                    gross = gross + res1.getInt("AD_AMT");
                                    label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                                    sheet.addCell(label2);
                                }
                            }
                        }
                    } else {
                        col = 11;
                    }
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
                }
                col = col + 1;
                grosstotal = gross;
                label2 = new Label(col, row, "" + gross, colorcell);
                sheet.addCell(label2);
                gross = 0;

                DataBaseFunctions.closeSqlObjects(res1, st1);

                pst.setString(1, res.getString("AQSL_NO"));
                pst.setString(2, res.getString("AQSL_NO"));
                pst.setString(3, res.getString("AQSL_NO"));

                res1 = pst.executeQuery();
                int prevcol = col + 1;
                col = col + 1;
                boolean insideDedWhile = false;
                while (res1.next()) {
                    insideDedWhile = true;
                    String adcode = res1.getString("AD_CODE");
                    col = prevcol;
                    col = col + 1;
                    if (adcode.equals("GPF")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("CPF")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("TPF")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("HRR")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("FA")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("MCA")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("OR")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("HBA")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("PT")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("GA")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("VE")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("SHBA")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("MOPA")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("GPDD")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("HC")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("LIC")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("PA")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("HIGL")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("LIGL")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("MIGL")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("MAL")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("GPIR")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("GIS")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("IT")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("WRR")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("GISA")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("VE")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("EP")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("SWR")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("CMPA")) {
                        totalded = totalded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                }
                if (insideDedWhile == false) {
                    col = col + 29;
                }
                netpay = grosstotal - totalded;
                col = col + 1;
                label2 = new Label(col, row, totalded + "", colorcell);
                sheet.addCell(label2);
                totalded = 0;
                col = col + 1;
                label2 = new Label(col, row, netpay + "", colorcell);
                sheet.addCell(label2);

                DataBaseFunctions.closeSqlObjects(res1, st1);

                String queryClump2 = "SELECT AD_AMT,AD_DESC,AD_CODE,SCHEDULE FROM " + aqDTLS + " AQ_DTLS WHERE (AQ_DTLS.SCHEDULE='PVTL' OR"
                        + " AQ_DTLS.SCHEDULE='PVTD') AND EMP_CODE='" + res.getString("EMP_CODE") + "' AND AQ_DTLS.AQSL_NO = '" + res.getString("AQSL_NO") + "'"
                        + " AND AD_AMT>0";

                st1 = con.createStatement();
                res1 = st1.executeQuery(queryClump2);

                boolean insidePvtDedWhile = false;
                int prevpvtcol = col + 2;
                //col = prevpvtcol;
                while (res1.next()) {
                    insidePvtDedWhile = true;
                    String adcode = res1.getString("AD_CODE");
                    col = prevpvtcol;
                    col = col + 1;
                    if (adcode.equals("ALBDL")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("ASSCN")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("BADN")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("BANKLN")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("CLUB")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("CMRF")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("CRFL")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("DHE")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("DTHREL")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("ELCTRC")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("FARD")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("FC")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("FL")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("GOVTRC")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("HFY")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("MISDED")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("NGBL")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("OPW")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("OPWLN")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("OSCBL")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("PAYSLP")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("SBIKL")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("SBIL")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("SBILN")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("SOCIL")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("SPRTS")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("SYNDC")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("SYNDLN")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("TEMPFND")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("TEMPHRR")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("UBI")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("UCB")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("UCOLN")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("UWFND")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("UWLN")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("WC")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("DHOBI")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("CRPWF")) {
                        totalpvtded = totalpvtded + res1.getInt("AD_AMT");
                        label2 = new Label(col, row, res1.getString("AD_AMT"), innercell);
                        sheet.addCell(label2);
                    }
                }
                if (insidePvtDedWhile == false) {
                    col = col + 39;
                }
                col = col + 1;
                balance = netpay - totalpvtded;
                label2 = new Label(col, row, totalpvtded + "", innercell);
                sheet.addCell(label2);
                col = col + 1;
                label2 = new Label(col, row, balance + "", innercell);
                sheet.addCell(label2);
                totalpvtded = 0;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res1, st1);
            DataBaseFunctions.closeSqlObjects(res, st);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void stopPay(String aqslno) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("UPDATE AQ_MAST SET CUR_BASIC=?  WHERE AQSL_NO=?");
            ps.setInt(1, 0);
            ps.setString(2, aqslno);
            ps.executeUpdate();

            DataBaseFunctions.closeSqlObjects(ps);

            ps = con.prepareStatement("UPDATE AQ_DTLS SET AD_AMT=? WHERE AQSL_NO=?");
            ps.setInt(1, 0);
            ps.setString(2, aqslno);
            ps.executeUpdate();

            DataBaseFunctions.closeSqlObjects(ps);

            ps = con.prepareStatement("UPDATE AQ_DTLS SET TOT_REC_AMT=? WHERE AQSL_NO=? AND AD_CODE='CPF'");
            ps.setInt(1, 0);
            ps.setString(2, aqslno);
            ps.executeUpdate();

        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void generateAqReportPDF(Document document, String billNo, CommonReportParamBean crb, AqreportBean aqreportFormBean) {
        Connection con = null;
        PreparedStatement pstmtBtId = null;

        PreparedStatement pstmtQPool = null;

        try {

            con = this.dataSource.getConnection();

            Font textFont = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
            Font bigTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL, BaseColor.BLACK);
            Font boldTextFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);

            PdfPTable table = new PdfPTable(5);
            table.setWidths(new float[]{10, 10, 60, 10, 10});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("STATE-" + aqreportFormBean.getState() + "\n" + "VCH NO: " + crb.getVchNo(), textFont));
            cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DIST-" + aqreportFormBean.getDistrict() + "\n" + crb.getVchDate(), textFont));
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SCHEDULE-A STATE HEAD QUATERS FORM NO-58\n"
                    + "PAY BILL FOR " + aqreportFormBean.getOffen()
                    + "\n"
                    + "MONTHLY PAY BILL FOR " + aqreportFormBean.getMonth() + "-" + aqreportFormBean.getYear(), textFont));
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("BILL NO:" + aqreportFormBean.getBillNo() + "\nBILL DT:" + aqreportFormBean.getBilldate(), textFont));
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("PAGE:1", textFont));
            cell.setBorder(Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
            table.addCell(cell);

            document.add(table);

            PdfPTable table1 = new PdfPTable(21);
            table1.setWidths(new float[]{2, 11, 4, 4, 2, 3, 4, 5, 6, 6, 4, 5, 4, 8, 5, 5, 4, 5, 5, 4, 7});
            table1.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("SL NO", textFont));
            cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("NAME/\nDESG/\nPAY SCALE", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("BASIC\nSPL PAY\nGP\nIR", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("DP\nP.PAY", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("DA", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("HRA", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("OTHER\nALLOWANCE", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("GROSS\nPAY", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("LIC/\nPLI", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("GPF/CPF/TPF\nDA-GPF\nRECOVERY", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("P.TAX\nI.TAX", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("HRR\nWATER TAX\nSWG\nHIRE CHG", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("HB\n INT HB\nSPL HB\nINT SPL HB", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("MC\nINT MC\nMC/MOP ADV\nINT MC/MOPED", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("CAR ADV\nINT CAR\nBI-CYCLE\nINT CYCL", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("PAY ADV\nMED ADV\nTRADE ADV\nOVDL", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("FEST\nNPS ARR.\nEX. PAY\nRTI\nAUDR", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("OTHER\nRECOVERY\nGIS ADV\nAIS GIS\nCOMP ADV", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("TOTAL\nDEDN", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("NET PAY", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("REMARKS\n A/C NO", textFont));
            cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("(1)", textFont));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("(2)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("(3)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("(4)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("(5)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("(6)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("(7)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("(8)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("(9)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("(10)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("(11)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("(12)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("(13)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("(14)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("(15)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("(16)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("(17)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("(18)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("(19)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("(20)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("(21)", textFont));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            for (int i = 0; i < aqreportFormBean.getAqlist().size(); i++) {
                SectionWiseAqBean objBill = (SectionWiseAqBean) aqreportFormBean.getAqlist().get(i);
                cell = new PdfPCell(new Phrase(objBill.getAqlistSectionWise().size() + " " + objBill.getSectionname(), hdrTextFont));
                cell.setBorder(Rectangle.BOTTOM);
                cell.setColspan(21);
                table1.addCell(cell);
                for (int j = 0; j < objBill.getAqlistSectionWise().size(); j++) {
                    String adAmt = "";
                    AqreportHelperBean aqBean = (AqreportHelperBean) objBill.getAqlistSectionWise().get(j);
                    cell = new PdfPCell(new Phrase(aqBean.getSlno(), textFont));
                    cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(new Phrase(aqBean.getEmpname() + "\n" + aqBean.getDesg() + "\n" + aqBean.getPayscale() + "\n" + aqBean.getGpfacct(), textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(cell);
                    for (int k = 0; k < aqBean.getCol3().size(); k++) {
                        ADDetailsHealperBean adBean = (ADDetailsHealperBean) aqBean.getCol3().get(k);
                        adAmt += "\n" + adBean.getAdamt();
                    }
                    cell = new PdfPCell(new Phrase(aqBean.getBasic() + adAmt, textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);
                    adAmt = "";
                    for (int k = 0; k < aqBean.getCol4().size(); k++) {
                        ADDetailsHealperBean adBean = (ADDetailsHealperBean) aqBean.getCol4().get(k);
                        adAmt += "\n" + adBean.getAdamt();
                    }
                    cell = new PdfPCell(new Phrase(adAmt, textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);
                    adAmt = "";
                    for (int k = 0; k < aqBean.getCol5().size(); k++) {
                        ADDetailsHealperBean adBean = (ADDetailsHealperBean) aqBean.getCol5().get(k);
                        adAmt += "\n" + adBean.getAdamt();
                    }
                    cell = new PdfPCell(new Phrase(adAmt, textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);
                    adAmt = "";
                    for (int k = 0; k < aqBean.getCol6().size(); k++) {
                        ADDetailsHealperBean adBean = (ADDetailsHealperBean) aqBean.getCol6().get(k);
                        adAmt += "\n" + adBean.getAdamt();
                    }
                    cell = new PdfPCell(new Phrase(adAmt, textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);
                    adAmt = "";
                    for (int k = 0; k < aqBean.getCol7().size(); k++) {
                        ADDetailsHealperBean adBean = (ADDetailsHealperBean) aqBean.getCol7().get(k);
                        adAmt += "\n" + adBean.getAdamt();
                    }
                    cell = new PdfPCell(new Phrase(adAmt, textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);
                    adAmt = "";
                    for (int k = 0; k < aqBean.getCol8().size(); k++) {
                        ADDetailsHealperBean adBean = (ADDetailsHealperBean) aqBean.getCol8().get(k);
                        adAmt += "\n" + adBean.getAdamt();
                    }
                    cell = new PdfPCell(new Phrase(adAmt, textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);
                    adAmt = "";
                    for (int k = 0; k < aqBean.getCol9().size(); k++) {
                        ADDetailsHealperBean adBean = (ADDetailsHealperBean) aqBean.getCol9().get(k);
                        adAmt += "\n" + adBean.getAdamt();
                    }
                    cell = new PdfPCell(new Phrase(adAmt, textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);
                    adAmt = "";
                    for (int k = 0; k < aqBean.getCol10().size(); k++) {
                        ADDetailsHealperBean adBean = (ADDetailsHealperBean) aqBean.getCol10().get(k);
                        adAmt += "\n" + adBean.getAdamt() + adBean.getRefdesc();
                    }
                    cell = new PdfPCell(new Phrase(adAmt, textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);
                    adAmt = "";
                    for (int k = 0; k < aqBean.getCol11().size(); k++) {
                        ADDetailsHealperBean adBean = (ADDetailsHealperBean) aqBean.getCol11().get(k);
                        adAmt += "\n" + adBean.getAdamt();
                    }
                    cell = new PdfPCell(new Phrase(adAmt, textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);
                    adAmt = "";
                    for (int k = 0; k < aqBean.getCol12().size(); k++) {
                        ADDetailsHealperBean adBean = (ADDetailsHealperBean) aqBean.getCol12().get(k);
                        adAmt += "\n" + adBean.getAdamt();
                    }
                    cell = new PdfPCell(new Phrase(adAmt, textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);
                    adAmt = "";
                    for (int k = 0; k < aqBean.getCol13().size(); k++) {
                        ADDetailsHealperBean adBean = (ADDetailsHealperBean) aqBean.getCol13().get(k);
                        adAmt += "\n" + adBean.getAdamt() + adBean.getRefdesc();
                    }
                    cell = new PdfPCell(new Phrase(adAmt, textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);
                    adAmt = "";
                    for (int k = 0; k < aqBean.getCol14().size(); k++) {
                        ADDetailsHealperBean adBean = (ADDetailsHealperBean) aqBean.getCol14().get(k);
                        adAmt += "\n" + adBean.getAdamt() + adBean.getRefdesc();
                    }
                    cell = new PdfPCell(new Phrase(adAmt, textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);
                    adAmt = "";
                    for (int k = 0; k < aqBean.getCol15().size(); k++) {
                        ADDetailsHealperBean adBean = (ADDetailsHealperBean) aqBean.getCol15().get(k);
                        adAmt += "\n" + adBean.getAdamt() + adBean.getRefdesc();
                    }
                    cell = new PdfPCell(new Phrase(adAmt, textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);
                    adAmt = "";
                    for (int k = 0; k < aqBean.getCol16().size(); k++) {
                        ADDetailsHealperBean adBean = (ADDetailsHealperBean) aqBean.getCol16().get(k);
                        adAmt += "\n" + adBean.getAdamt() + adBean.getRefdesc();
                    }
                    cell = new PdfPCell(new Phrase(adAmt, textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);
                    adAmt = "";
                    for (int k = 0; k < aqBean.getCol17().size(); k++) {
                        ADDetailsHealperBean adBean = (ADDetailsHealperBean) aqBean.getCol17().get(k);
                        adAmt += "\n" + adBean.getAdamt() + adBean.getRefdesc();
                    }
                    cell = new PdfPCell(new Phrase(adAmt, textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);
                    adAmt = "";
                    for (int k = 0; k < aqBean.getCol18().size(); k++) {
                        ADDetailsHealperBean adBean = (ADDetailsHealperBean) aqBean.getCol18().get(k);
                        adAmt += "\n" + adBean.getAdamt() + adBean.getRefdesc();
                    }
                    cell = new PdfPCell(new Phrase(adAmt, textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);
                    adAmt = "";
                    for (int k = 0; k < aqBean.getCol19().size(); k++) {
                        ADDetailsHealperBean adBean = (ADDetailsHealperBean) aqBean.getCol19().get(k);
                        adAmt += "\n" + adBean.getAdamt();
                    }
                    cell = new PdfPCell(new Phrase(adAmt, textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);
                    adAmt = "";
                    for (int k = 0; k < aqBean.getCol20().size(); k++) {
                        ADDetailsHealperBean adBean = (ADDetailsHealperBean) aqBean.getCol20().get(k);
                        adAmt += "\n" + adBean.getAdamt();
                    }
                    cell = new PdfPCell(new Phrase(adAmt, textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(new Phrase(aqBean.getAccNo(), textFont));
                    cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);
                }
            }
            cell = new PdfPCell(new Phrase("Grand Total", boldTextFont));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT);
            cell.setColspan(2);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(aqreportFormBean.getCol3Tot() + "", boldTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getCol4Tot() + "", boldTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getCol5Tot() + "", boldTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getCol6Tot() + "", boldTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getCol7Tot() + "", boldTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getCol8Tot() + "", boldTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getCol9Tot() + "", boldTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getCol10Tot() + "", boldTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getCol11Tot() + "", boldTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getCol12Tot() + "", boldTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getCol13Tot() + "", boldTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getCol14Tot() + "", boldTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getCol15Tot() + "", boldTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getCol16Tot() + "", boldTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getCol17Tot() + "", boldTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getCol18Tot() + "", boldTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getCol19Tot() + "", boldTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getCol20Tot() + "", boldTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("", textFont));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Rupees " + aqreportFormBean.getNetPay() + " only", hdrTextFont));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.LEFT);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setColspan(21);
            table1.addCell(cell);
            document.add(table1);

            PdfPTable table2 = new PdfPTable(4);
            table2.setWidths(new float[]{25, 25, 25, 25});
            table2.setWidthPercentage(20);
            table2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.setSpacingBefore(20);

            cell = new PdfPCell(new Phrase("Pay", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase("=", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getPay(), textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase("", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase("Dp+P Pay", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase("=", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getDp(), textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase("", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase("DA", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase("=", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getDa(), textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase("", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase("HRA", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase("=", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getHra(), textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase("", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase("OA", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase("=", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getOa(), textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase("", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase(" ", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(4);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase("Total", textFont));
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            cell.setBorderWidth(1);
            cell.setPaddingTop(10);
            cell.setPaddingBottom(10);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase("=", textFont));
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            cell.setBorderWidth(1);
            cell.setPaddingTop(10);
            cell.setPaddingBottom(10);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotAbstract() + "", textFont));
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            cell.setBorderWidth(1);
            cell.setPaddingTop(10);
            cell.setPaddingBottom(10);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase("", textFont));
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            cell.setBorderWidth(1);
            cell.setPaddingTop(10);
            cell.setPaddingBottom(10);
            table2.addCell(cell);
            document.add(table2);

            PdfPTable table3 = new PdfPTable(4);
            table3.setWidths(new float[]{25, 25, 25, 25});
            table3.setWidthPercentage(50);
            table3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.setSpacingBefore(20);

            cell = new PdfPCell(new Phrase("LIC", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getCol9Tot() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase("FESTIVAL ADV", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getCol17Tot() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);

            cell = new PdfPCell(new Phrase("HRR", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotHrr() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase("GEN HBA ADV", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotHbaPri() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);

            cell = new PdfPCell(new Phrase("HUDCO", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotShbaPri() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase("MC/MOPED ADV", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotMcaPri() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);

            cell = new PdfPCell(new Phrase("INT ON HUDCO", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotShbaInt() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase("BI-CYCLES ADV", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotBicycPri() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);

            cell = new PdfPCell(new Phrase("PROF. TAX", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotPt() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase("GIS ADVANCE", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotGisaPri() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);

            cell = new PdfPCell(new Phrase("INCOME TAX", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotIt() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase("GPF", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotGpf() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);

            cell = new PdfPCell(new Phrase("0075 MISC", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase("", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase("INT MC/MOPED", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotMopInt() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);

            cell = new PdfPCell(new Phrase("HICHG", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotHc() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase("INT HBA", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotHbaInt() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);

            cell = new PdfPCell(new Phrase("AIS GIS", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotGis() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase("INT CAR ADV", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotVehicleInt() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);

            cell = new PdfPCell(new Phrase("CPF", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotCpf() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase("PAY ADV", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotPa() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);

            cell = new PdfPCell(new Phrase("CGEGIS", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotCgegis() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase("CAR ADV", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotVehiclePri() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);

            cell = new PdfPCell(new Phrase("TPF", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotTpf() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase("TPGA", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotTfga() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);

            cell = new PdfPCell(new Phrase("TLIC", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotTlci() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase("WRR", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotWrr() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);

            cell = new PdfPCell(new Phrase("SWR", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotSwr() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase("CC", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotCc() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);

            cell = new PdfPCell(new Phrase("COMP ADV", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotCmpa() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase("OVDL(P)", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(aqreportFormBean.getTotOvdl() + "", textFont));
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);

            document.add(table3);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            DataBaseFunctions.closeSqlObjects(rsBtId, pstmtBtId);
//            DataBaseFunctions.closeSqlObjects(rsQPool, pstmtQPool);
            DataBaseFunctions.closeSqlObjects(pstmtBtId, pstmtQPool);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList<AcquaintanceBean> getAcquaintanceDHE(String billNo, String offCode) {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        ArrayList<AcquaintanceBean> acquaintanceList = new ArrayList();
        try {
            con = repodataSource.getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery("SELECT AQSL_NO, emp_code, gpf_acc_no, EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,GPF_ACC_NO,gross_amt,"
                    + "ded_amt,pvt_ded_amt FROM AQ_MAST WHERE  BILL_NO='" + billNo + "' AND OFF_CODE='" + offCode + "' AND EMP_NAME IS NOT NULL order by post_sl_no");
            while (res.next()) {
                AcquaintanceBean aqBean = new AcquaintanceBean();
                if (res.getString("emp_code") != null && !res.getString("emp_code").equals("")) {
                    boolean depstatusBoole = false;
                    //depstatusBoole=stopSalaryForPayHeldUp(res.getString("emp_code"));
                    if (depstatusBoole) {
                        aqBean.setEmployeeDepStatus("HELDUP");
                    } else {
                        aqBean.setEmployeeDepStatus("NA");
                    }
                } else {
                    aqBean.setEmployeeDepStatus("NA");
                }

                aqBean.setAqslno(res.getString("AQSL_NO"));
                aqBean.setEmpcode(res.getString("emp_code"));
                aqBean.setGpfaccno(res.getString("gpf_acc_no"));
                aqBean.setEmpname(res.getString("EMP_NAME"));
                aqBean.setGpfaccno(res.getString("GPF_ACC_NO"));
                aqBean.setCurdesg(res.getString("CUR_DESG"));
                aqBean.setBankaccno(res.getString("BANK_ACC_NO"));
                aqBean.setPayscale(res.getString("PAY_SCALE"));
                aqBean.setCurbasic(res.getInt("CUR_BASIC"));
                aqBean.setGrossamt(res.getInt("gross_amt"));
                aqBean.setDedamt(res.getInt("ded_amt"));
                aqBean.setPrvdedamt(res.getInt("pvt_ded_amt"));
                acquaintanceList.add(aqBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return acquaintanceList;
    }

    //--------for DHE---------------------------------------------
    @Override
    public ArrayList getSectionWiseBillDtlsDHE(String billno, String mon, String year, String format, String offCode, BillConfigObj billConfigObj, String empType, String column9NameList, String column10NameList, String column11NameList, String column12NameList, String column13NameList, String column14NameList, String column15NameList, String column16NameList, String column17NameList, String column18NameList) {
        String section = "";
        Connection con = null;
        int secslno = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        SectionWiseAqBean objBill = null;;
        ArrayList a1 = new ArrayList();
        String offLvl = null;
        String isddo = null;
        String officeName = null;
        String officeCode = null;
        try {
            con = dataSource.getConnection();
            //System.out.println("dheaq::" + offCode);
            pstmt = con.prepareStatement("Select is_ddo,lvl from g_office where off_code=?");
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                offLvl = rs.getString("lvl");
                isddo = rs.getString("is_ddo");
                if ((isddo != null && isddo.equals("B")) && (offLvl != null && offLvl.equals("20"))) {
                    pstmt = con.prepareStatement("SELECT SEC_SL_NO,cur_spc,off_code,getofficeen(off_code)offName, SECTION, EMP_TYPE FROM AQ_MAST WHERE AQ_YEAR=? AND AQ_MONTH=? AND BILL_NO=?  AND EMP_TYPE=?\n"
                            + "GROUP BY SECTION,cur_spc,off_code, SEC_SL_NO, EMP_TYPE having off_code=? ORDER BY SEC_SL_NO");
                    pstmt.setInt(1, Integer.parseInt(year));
                    pstmt.setInt(2, Integer.parseInt(mon));
                    pstmt.setInt(3, Integer.parseInt(billno));
                    pstmt.setString(4, empType);
                    pstmt.setString(5, offCode);
                    rs1 = pstmt.executeQuery();

                    while (rs1.next()) {
                        objBill = new SectionWiseAqBean();
                        section = rs1.getString("SECTION");
                        secslno = rs1.getInt("SEC_SL_NO");
                        empType = rs1.getString("EMP_TYPE");
                        officeName = rs1.getString("offName");
                        officeCode = rs1.getString("off_code");
                        if (format.equals("f1") && empType.equals("D")) {
                            objBill = getAqBillDetailsDHESubOfficeF2(billno, mon, year, section, officeName, secslno, billConfigObj, offCode);
                        } else if (format.equals("f2") && empType.equals("D")) {
                            objBill = getAqBillDetailsDHESubOfficeF2(billno, mon, year, section, officeName, secslno, billConfigObj, offCode);
                        }
                        objBill.setSectionwiseOffEn(officeName);
                        //System.out.println("setSectionwiseOffEn->" + objBill.getSectionwiseOffEn());
                        objBill.setSectionname(section);
                        objBill.setOffcode(officeCode);

                        a1.add(objBill);
                    }
                } else if ((isddo != null && isddo.equals("Y")) && (offLvl != null && offLvl.equals("03"))) {
                    pstmt = con.prepareStatement("SELECT off_code,getofficeen(off_code)offName, EMP_TYPE FROM AQ_MAST WHERE \n"
                            + " AQ_YEAR=? AND AQ_MONTH=? AND BILL_NO=?  AND EMP_TYPE=?\n"
                            + "GROUP BY off_code, EMP_TYPE ");
                    pstmt.setInt(1, Integer.parseInt(year));
                    pstmt.setInt(2, Integer.parseInt(mon));
                    pstmt.setInt(3, Integer.parseInt(billno));
                    pstmt.setString(4, empType);

                    rs1 = pstmt.executeQuery();
                    while (rs1.next()) {
                        objBill = new SectionWiseAqBean();
                        //section = rs1.getString("SECTION");
                        //secslno = rs1.getInt("SEC_SL_NO");
                        empType = rs1.getString("EMP_TYPE");
                        officeName = rs1.getString("offName");
                        officeCode = rs1.getString("off_code");
                        if (format.equals("f1") && empType.equals("D")) {
                            objBill = getAqBillDetailsDHEF2(billno, mon, year, "", officeName, officeCode, 0, billConfigObj);
                        } else if (format.equals("f2") && empType.equals("D")) {
                            objBill = getAqBillDetailsDHEF2(billno, mon, year, "", officeName, officeCode, 0, billConfigObj);
                        }
                        objBill.setSectionwiseOffEn(officeName);
                        //System.out.println("setSectionwiseOffEn->" + objBill.getSectionwiseOffEn());
                        objBill.setSectionname("");
                        objBill.setOffcode(officeCode);

                        a1.add(objBill);
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(rs1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return a1;
    }

    //----------------------------------For DHE------------------------------------------
    public SectionWiseAqBean getAqBillDetailsDHEF2(String billno, String mon, String year, String section, String officeName, String offCode, int secslno, BillConfigObj billConfigObj) throws Exception {
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
            //System.out.println("offcode:" + offCode);
            aqDTLS = AqFunctionalities.getAQBillDtlsTable(Integer.parseInt(mon), Integer.parseInt(year));
            con = dataSource.getConnection();
            st = con.createStatement();
            pstamt = con.prepareStatement("SELECT * FROM (SELECT AQSL_NO,OFF_CODE,EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,SPC_ORD_NO,SPC_ORD_DATE,GPF_ACC_NO,ACCT_TYPE,\n"
                    + "EMP_CODE,post_sl_no,SEC_SL_NO\n"
                    + "FROM AQ_MAST WHERE AQ_MONTH=? AND AQ_YEAR=? AND BILL_NO=? \n"
                    + "AND OFF_CODE=?  AND DEP_CODE != '11') AQ_MAST\n"
                    + "LEFT OUTER JOIN (SELECT EMP_ID,CUR_CADRE_CODE,IF_GPF_ASSUMED FROM EMP_MAST)EMP_MAST ON AQ_MAST.EMP_CODE = EMP_MAST.EMP_ID\n"
                    + "LEFT OUTER JOIN (SELECT CADRE_CODE,CADRE_ABBR FROM G_CADRE)G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE\n"
                    + "order by POST_SL_NO");

            pstamt.setInt(1, Integer.parseInt(mon));
            pstamt.setInt(2, Integer.parseInt(year));
            pstamt.setInt(3, Integer.parseInt(billno));
            pstamt.setString(4, offCode);
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
                aqbean.setGpfacct(rs.getString("GPF_ACC_NO"));
                aqbean.setAccNo(rs.getString("BANK_ACC_NO"));
                aqbean.setAcctype(rs.getString("ACCT_TYPE"));
                aqbean.setBasic(rs.getString("CUR_BASIC"));
                aqslno = rs.getString("AQSL_NO");

                String queryClump = "select * from (select cnt,REP_COL,tmep4.AD_CODE,AD_AMT,SCHEDULE,tmep4.NOW_DEDN,AD_TYPE,sl_no,REF_DESC from(select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt = 1)tmep4 "
                        + "left outer join (SELECT AD_CODE,REF_DESC,NOW_DEDN FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp2 on "
                        + "tmep4.ad_code = temp2.ad_code and tmep4.now_dedn = temp2.now_dedn "
                        + "union "
                        + "select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no,cast('' as text) from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt > 1 )t1 order by SL_NO ";

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
                        //                            case 4:
                        //                                aqbean.setCol4(rs1.getString("AD_CODE"),rs1.getInt("AD_AMT"),rs1.getString("SCHEDULE"),rs1.getString("NOW_DEDN"),rs1.getString("REF_DESC"));
                        //                                break;
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
                                refdesc = aqfunctionalities.getRefDesc(adcode, nowdedn, aqslno, Integer.parseInt(mon), Integer.parseInt(year));
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
                                refdesc = aqfunctionalities.getRefDesc(adcode, nowdedn, aqslno, Integer.parseInt(mon), Integer.parseInt(year));
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
                int privatededuction = aqfunctionalities.getPrivateDeduction(rs.getString("AQSL_NO"), Integer.parseInt(mon), Integer.parseInt(year));
                int privateloan = aqfunctionalities.getPrivateLoan(rs.getString("AQSL_NO"), Integer.parseInt(mon), Integer.parseInt(year));
                int grosspay = aqfunctionalities.getGrossPay(rs.getString("AQSL_NO"), Integer.parseInt(mon), Integer.parseInt(year));
                int totaldedn = aqfunctionalities.getTotalDedn(rs.getString("AQSL_NO"), Integer.parseInt(mon), Integer.parseInt(year));

                aqbean.setCol8("GROSS PAY", grosspay, null, null, null);
                //aqbean.setCol19("TOTDEN", totaldedn, null, null, null);
                //aqbean.setCol19("NETPAY", grosspay - totaldedn, null, null, null);

                //aqbean.setCol20("PVTDED", privatededuction, null, null, null);
                //aqbean.setCol20("BANKLOAN", privateloan, null, null, null);
                //aqbean.setCol20("NETBALANCE", grosspay - (totaldedn + privatededuction + privateloan), null, null, null);
                aqbean.setCol19("TOTDEN", totaldedn, null, null, null);
                aqbean.setCol19("PVTDED", privatededuction, null, null, null);
                aqbean.setCol19("BANKLOAN", privateloan, null, null, null);

                aqbean.setCol20("NETBALANCE", grosspay - (totaldedn + privatededuction + privateloan), null, null, null);

                aqlist.add(aqbean);
                Collections.sort(aqbean.getCol6());
                Collections.sort(aqbean.getCol7());
                Collections.sort(aqbean.getCol16());
                Collections.sort(aqbean.getCol17());
                Collections.sort(aqbean.getCol18());
            }
            objBill.setAqlistSectionWise(aqlist);
            objBill.setSectionwiseOffEn(officeName);
            //objBill.setAqlistSectionWise(aqlist);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return objBill;
    }

    public SectionWiseAqBean getAqBillDetailsDHESubOfficeF2(String billno, String mon, String year, String section, String officeName, int secslno, BillConfigObj billConfigObj, String offCode) throws Exception {
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
            //System.out.println("offcode:" + offCode);
            aqDTLS = AqFunctionalities.getAQBillDtlsTable(Integer.parseInt(mon), Integer.parseInt(year));
            con = dataSource.getConnection();
            st = con.createStatement();
            pstamt = con.prepareStatement("SELECT * FROM (SELECT AQSL_NO,OFF_CODE,EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,SPC_ORD_NO,SPC_ORD_DATE,GPF_ACC_NO,ACCT_TYPE,EMP_CODE,post_sl_no,SEC_SL_NO "
                    + "FROM AQ_MAST WHERE AQ_MONTH=? AND AQ_YEAR=? AND BILL_NO=? AND SECTION=? AND OFF_CODE=? AND SEC_SL_NO=? AND DEP_CODE != '11') AQ_MAST "
                    + "LEFT OUTER JOIN (SELECT EMP_ID,CUR_CADRE_CODE,IF_GPF_ASSUMED FROM EMP_MAST)EMP_MAST ON AQ_MAST.EMP_CODE = EMP_MAST.EMP_ID "
                    + "LEFT OUTER JOIN (SELECT CADRE_CODE,CADRE_ABBR FROM G_CADRE)G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE "
                    + "order by POST_SL_NO");

            pstamt.setInt(1, Integer.parseInt(mon));
            pstamt.setInt(2, Integer.parseInt(year));
            pstamt.setInt(3, Integer.parseInt(billno));
            pstamt.setString(4, section);
            pstamt.setString(5, offCode);
            pstamt.setInt(6, secslno);
            //pstamt.setString(6, offCode);

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
                aqbean.setGpfacct(rs.getString("GPF_ACC_NO"));
                aqbean.setAccNo(rs.getString("BANK_ACC_NO"));
                aqbean.setAcctype(rs.getString("ACCT_TYPE"));
                aqbean.setBasic(rs.getString("CUR_BASIC"));
                aqslno = rs.getString("AQSL_NO");

                String queryClump = "select * from (select cnt,REP_COL,tmep4.AD_CODE,AD_AMT,SCHEDULE,tmep4.NOW_DEDN,AD_TYPE,sl_no,REF_DESC from(select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt = 1)tmep4 "
                        + "left outer join (SELECT AD_CODE,REF_DESC,NOW_DEDN FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp2 on "
                        + "tmep4.ad_code = temp2.ad_code and tmep4.now_dedn = temp2.now_dedn "
                        + "union "
                        + "select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no,cast('' as text) from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt > 1 )t1 order by SL_NO ";

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
                        //                            case 4:
                        //                                aqbean.setCol4(rs1.getString("AD_CODE"),rs1.getInt("AD_AMT"),rs1.getString("SCHEDULE"),rs1.getString("NOW_DEDN"),rs1.getString("REF_DESC"));
                        //                                break;
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
                                refdesc = aqfunctionalities.getRefDesc(adcode, nowdedn, aqslno, Integer.parseInt(mon), Integer.parseInt(year));
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
                                refdesc = aqfunctionalities.getRefDesc(adcode, nowdedn, aqslno, Integer.parseInt(mon), Integer.parseInt(year));
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
                int privatededuction = aqfunctionalities.getPrivateDeduction(rs.getString("AQSL_NO"), Integer.parseInt(mon), Integer.parseInt(year));
                int privateloan = aqfunctionalities.getPrivateLoan(rs.getString("AQSL_NO"), Integer.parseInt(mon), Integer.parseInt(year));
                int grosspay = aqfunctionalities.getGrossPay(rs.getString("AQSL_NO"), Integer.parseInt(mon), Integer.parseInt(year));
                int totaldedn = aqfunctionalities.getTotalDedn(rs.getString("AQSL_NO"), Integer.parseInt(mon), Integer.parseInt(year));

                aqbean.setCol8("GROSS PAY", grosspay, null, null, null);
                //aqbean.setCol19("TOTDEN", totaldedn, null, null, null);
                //aqbean.setCol19("NETPAY", grosspay - totaldedn, null, null, null);

                //aqbean.setCol20("PVTDED", privatededuction, null, null, null);
                //aqbean.setCol20("BANKLOAN", privateloan, null, null, null);
                //aqbean.setCol20("NETBALANCE", grosspay - (totaldedn + privatededuction + privateloan), null, null, null);
                aqbean.setCol19("TOTDEN", totaldedn, null, null, null);
                aqbean.setCol19("PVTDED", privatededuction, null, null, null);
                aqbean.setCol19("BANKLOAN", privateloan, null, null, null);

                aqbean.setCol20("NETBALANCE", grosspay - (totaldedn + privatededuction + privateloan), null, null, null);

                aqlist.add(aqbean);
                Collections.sort(aqbean.getCol6());
                Collections.sort(aqbean.getCol7());
                Collections.sort(aqbean.getCol16());
                Collections.sort(aqbean.getCol17());
                Collections.sort(aqbean.getCol18());
            }
            objBill.setAqlistSectionWise(aqlist);
            objBill.setSectionwiseOffEn(officeName);
            //objBill.setAqlistSectionWise(aqlist);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return objBill;
    }

    @Override
    public void GenerateOtherArrearAquitancePDFReport(Document document, String billNo, CommonReportParamBean crb, List arrAqList, AqreportBean aqreportFormBean) {

        int totalGrossArrear = 0;

        int totalCPF = 0;
        int totalPT = 0;
        int totalIT = 0;
        int totalOR = 0;
        int totalDDORecovery = 0;

        int dedTotal = 0;
        int netTotal = 0;

        try {
            Font textFont = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
            Font bigTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL, BaseColor.BLACK);
            Font boldTextFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);

            PdfPTable table = new PdfPTable(5);
            table.setWidths(new float[]{10, 10, 60, 10, 10});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("STATE-" + aqreportFormBean.getState() + "\n" + "VCH NO: " + crb.getVchNo(), bigTextFont));
            cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DIST-" + aqreportFormBean.getDistrict() + "\n" + crb.getVchDate(), bigTextFont));
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SCHEDULE-A STATE HEAD QUATERS FORM NO-58\n"
                    + "PAY BILL FOR " + aqreportFormBean.getOffen()
                    + "\n"
                    + "MONTHLY PAY BILL FOR " + aqreportFormBean.getMonth() + "-" + aqreportFormBean.getYear(), bigTextFont));
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("BILL NO:" + aqreportFormBean.getBillNo() + "\nBILL DT:" + aqreportFormBean.getBilldate(), bigTextFont));
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("PAGE:1", bigTextFont));
            cell.setBorder(Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(15);
            table.setWidths(new float[]{2, 11, 4, 4, 2, 3, 4, 5, 6, 6, 4, 5, 4, 8, 5});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("SL NO", bigTextFont));
            cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("NAME/\nDESG/\nPAY SCALE", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("PAY\nGP\nIR", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("DA", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("HRA", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("OA", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("GROSS Arrear", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("GPF/CPF/TPF", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("P.TAX", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("I.TAX", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("OTHER\nRECOVERY", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("DDO\nRECOVERY", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("TOTAL\nDEDN", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("NET PAY", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("REMARKS\nA/C NO.", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(1)", textFont));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(2)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(3)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(4)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(5)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(6)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(7)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(8)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(9)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(10)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(11)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(12)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(13)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(14)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(15)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            if (arrAqList != null && arrAqList.size() > 0) {
                int slno = 0;
                int deductions = 0;
                int netamount = 0;
                for (int i = 0; i < arrAqList.size(); i++) {
                    ArrAqMastModel arrAqBean = (ArrAqMastModel) arrAqList.get(i);

                    slno++;

                    totalGrossArrear = totalGrossArrear + arrAqBean.getGrandTotArr100();
                    totalCPF = totalCPF + NumberUtils.toInt(arrAqBean.getCpfHead());
                    totalPT = totalPT + arrAqBean.getProfessionalTax();
                    totalIT = totalIT + NumberUtils.toInt(arrAqBean.getIncomeTaxAmt());
                    totalOR = totalOR + NumberUtils.toInt(arrAqBean.getOtherRecovery());
                    totalDDORecovery = totalDDORecovery + NumberUtils.toInt(arrAqBean.getDdoRecovery());

                    if (arrAqBean.getCpfHead() != null && !arrAqBean.getCpfHead().equals("")) {
                        deductions = deductions + Integer.parseInt(arrAqBean.getCpfHead());
                    }

                    deductions = deductions + arrAqBean.getProfessionalTax();
                    if (arrAqBean.getIncomeTaxAmt() != null && !arrAqBean.getIncomeTaxAmt().equals("")) {
                        deductions = deductions + Integer.parseInt(arrAqBean.getIncomeTaxAmt());
                    }
                    if (arrAqBean.getOtherRecovery() != null && !arrAqBean.getOtherRecovery().equals("")) {
                        deductions = deductions + Integer.parseInt(arrAqBean.getOtherRecovery());
                    }
                    if (arrAqBean.getDdoRecovery() != null && !arrAqBean.getDdoRecovery().equals("")) {
                        deductions = deductions + Integer.parseInt(arrAqBean.getDdoRecovery());
                    }

                    netamount = arrAqBean.getGrandTotArr100() - deductions;

                    cell = new PdfPCell(new Phrase(slno + "", textFont));
                    cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getEmpName() + "\n" + arrAqBean.getCurDesg() + "\n" + arrAqBean.getGpfAccNo(), textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getPayTotal() + "\n" + arrAqBean.getGpTotal() + "\n" + arrAqBean.getIrTotal(), textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getDaTotal() + "", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getHraTotal() + "", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getOaTotal() + "", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getGrandTotArr100() + "", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getCpfHead(), textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getProfessionalTax() + "", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getIncomeTaxAmt(), textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getOtherRecovery(), textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getDdoRecovery(), textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(deductions + "", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(netamount + "", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    dedTotal = dedTotal + deductions;
                    netTotal = netTotal + netamount;
                }
            }

            cell = new PdfPCell(new Phrase("GRAND TOTAL", textFont));
            cell.setColspan(6);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalGrossArrear + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalCPF + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalPT + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalIT + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalOR + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalDDORecovery + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dedTotal + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(netTotal + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            Long netamountinword = 0L;
            if (netTotal > 0) {
                netamountinword = Long.parseLong(netTotal + "");
            }

            cell = new PdfPCell();
            cell.setColspan(6);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(Numtowordconvertion.convertNumber(netamountinword.intValue()) + " only", textFont));
            cell.setColspan(9);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void GenerateArrear4Arrear6AquitancePDFReport(Document document, String billNo, CommonReportParamBean crb, List arrAqList, AqreportBean aqreportFormBean) {

        int totalArrear100 = 0;
        int totalArrear40 = 0;

        int totalCPF = 0;
        int totalPT = 0;
        int totalIT = 0;
        int totalOR = 0;
        int totalDDORecovery = 0;

        int dedTotal = 0;
        double netTotal = 0;

        try {
            Font textFont = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
            Font bigTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL, BaseColor.BLACK);
            Font boldTextFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);

            PdfPTable table = new PdfPTable(5);
            table.setWidths(new float[]{10, 10, 60, 10, 10});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("STATE-" + aqreportFormBean.getState() + "\n" + "VCH NO: " + StringUtils.defaultString(crb.getVchNo()), bigTextFont));
            cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DIST-" + aqreportFormBean.getDistrict() + "\n" + StringUtils.defaultString(crb.getVchDate()), bigTextFont));
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SCHEDULE-A STATE HEAD QUATERS FORM NO-58\n"
                    + "PAY BILL FOR " + aqreportFormBean.getOffen()
                    + "\n"
                    + "MONTHLY PAY BILL FOR " + aqreportFormBean.getMonth() + "-" + aqreportFormBean.getYear(), bigTextFont));
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("BILL NO:" + aqreportFormBean.getBillNo() + "\nBILL DT:" + aqreportFormBean.getBilldate(), bigTextFont));
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("PAGE:1", bigTextFont));
            cell.setBorder(Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(11);
            table.setWidths(new float[]{2, 11, 4, 4, 2, 3, 4, 5, 6, 6, 4});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("SL NO", bigTextFont));
            cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("NAME AND DESIGNATION", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("ARREAR 100%", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            String arrearpercentagetext = "";
            if (crb.getTypeofBill() != null && crb.getTypeofBill().equals("ARREAR")) {
                arrearpercentagetext = "Arrear 40%";
            } else if (crb.getTypeofBill() != null && crb.getTypeofBill().equals("ARREAR_6")) {
                if (crb.getPercentageArraer() == 15) {
                    arrearpercentagetext = "Arrear 15/20%";
                } else if (crb.getPercentageArraer() != 15) {
                    arrearpercentagetext = "Arrear " + crb.getPercentageArraer() + "%";
                }
            }
            cell = new PdfPCell(new Phrase(arrearpercentagetext, bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("GPF/CPF/TPF", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("P.TAX", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("I.TAX", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("OTHER\nRECOVERY", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("TOTAL\nDEDN", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("NET PAY", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("REMARKS", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(1)", textFont));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(2)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(3)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(4)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(5)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(6)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(7)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(8)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(9)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(10)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(11)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            if (arrAqList != null && arrAqList.size() > 0) {
                int slno = 0;
                int deductions = 0;
                double netamount = 0;
                for (int i = 0; i < arrAqList.size(); i++) {
                    ArrAqMastModel arrAqBean = (ArrAqMastModel) arrAqList.get(i);

                    slno++;

                    totalArrear100 = totalArrear100 + arrAqBean.getGrandTotArr100();
                    totalArrear40 = totalArrear40 + (int)arrAqBean.getGrandTotArr40();
                    totalCPF = totalCPF + NumberUtils.toInt(arrAqBean.getCpfHead());
                    totalPT = totalPT + arrAqBean.getProfessionalTax();
                    totalIT = totalIT + NumberUtils.toInt(arrAqBean.getIncomeTaxAmt());
                    totalOR = totalOR + NumberUtils.toInt(arrAqBean.getOtherRecovery());
                    totalDDORecovery = totalDDORecovery + NumberUtils.toInt(arrAqBean.getDdoRecovery());

                    if (arrAqBean.getCpfHead() != null && !arrAqBean.getCpfHead().equals("")) {
                        deductions = deductions + Integer.parseInt(arrAqBean.getCpfHead());
                    }

                    deductions = deductions + arrAqBean.getProfessionalTax();
                    if (arrAqBean.getIncomeTaxAmt() != null && !arrAqBean.getIncomeTaxAmt().equals("")) {
                        deductions = deductions + Integer.parseInt(arrAqBean.getIncomeTaxAmt());
                    }
                    if (arrAqBean.getOtherRecovery() != null && !arrAqBean.getOtherRecovery().equals("")) {
                        deductions = deductions + Integer.parseInt(arrAqBean.getOtherRecovery());
                    }

                    netamount = arrAqBean.getGrandTotArr40() - deductions;

                    cell = new PdfPCell(new Phrase(slno + "", textFont));
                    cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getEmpName() + "\n" + arrAqBean.getCurDesg() + "\n" + arrAqBean.getGpfAccNo(), textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getGrandTotArr100() + "", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getGrandTotArr40() + "", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getCpfHead(), textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getProfessionalTax() + "", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getIncomeTaxAmt(), textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getOtherRecovery(), textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(deductions + "", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(netamount + "", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getRemark(), textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    dedTotal = dedTotal + deductions;
                    netTotal = netTotal + netamount;
                }
            }

            cell = new PdfPCell(new Phrase("GRAND TOTAL", textFont));
            cell.setColspan(2);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalArrear100 + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalArrear40 + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalCPF + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalPT + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalIT + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalOR + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dedTotal + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(netTotal + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            Long netamountinword = 0L;
            if (netTotal > 0) {
                netamountinword = (long)netTotal;
            }

            cell = new PdfPCell();
            cell.setColspan(6);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(Numtowordconvertion.convertNumber(netamountinword.intValue()) + " only", textFont));
            cell.setColspan(5);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void GenerateNPSArrearAquitancePDFReport(Document document, String billNo, CommonReportParamBean crb, List arrAqList, AqreportBean aqreportFormBean) {
        
        int totalArrear100 = 0;
        int totalArrear40 = 0;

        int totalCPF = 0;
        int totalPT = 0;
        int totalIT = 0;
        int totalOR = 0;
        int totalDDORecovery = 0;

        int dedTotal = 0;
        double netTotal = 0;

        try {
            Font textFont = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
            Font bigTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL, BaseColor.BLACK);
            Font boldTextFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);

            PdfPTable table = new PdfPTable(5);
            table.setWidths(new float[]{10, 10, 60, 10, 10});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("STATE-" + aqreportFormBean.getState() + "\n" + "VCH NO: " + StringUtils.defaultString(crb.getVchNo()), bigTextFont));
            cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DIST-" + aqreportFormBean.getDistrict() + "\n" + StringUtils.defaultString(crb.getVchDate()), bigTextFont));
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SCHEDULE-A STATE HEAD QUATERS FORM NO-58\n"
                    + "PAY BILL FOR " + aqreportFormBean.getOffen()
                    + "\n"
                    + "MONTHLY PAY BILL FOR " + aqreportFormBean.getMonth() + "-" + aqreportFormBean.getYear(), bigTextFont));
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("BILL NO:" + aqreportFormBean.getBillNo() + "\nBILL DT:" + aqreportFormBean.getBilldate(), bigTextFont));
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("PAGE:1", bigTextFont));
            cell.setBorder(Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(8);
            table.setWidths(new float[]{2, 11, 4, 4, 2, 3, 4, 5});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("SL NO", bigTextFont));
            cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("NAME AND DESIGNATION", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("ARREAR 100%", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);            
            cell = new PdfPCell(new Phrase("ARREAR 5%\n(10% of Balance 50%)", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("TOTAL CPF", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("TOTAL\nDEDN", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("NET PAY", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("REMARKS", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);            

            cell = new PdfPCell(new Phrase("(1)", textFont));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(2)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(3)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(4)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(5)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(6)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(7)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(8)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            if (arrAqList != null && arrAqList.size() > 0) {
                int slno = 0;
                int deductions = 0;
                double netamount = 0;
                for (int i = 0; i < arrAqList.size(); i++) {
                    ArrAqMastModel arrAqBean = (ArrAqMastModel) arrAqList.get(i);

                    slno++;

                    totalArrear100 = totalArrear100 + arrAqBean.getGrandTotArr100();
                    totalArrear40 = totalArrear40 + (int)arrAqBean.getGrandTotArr40();
                    totalCPF = totalCPF + NumberUtils.toInt(arrAqBean.getCpfHead());

                    cell = new PdfPCell(new Phrase(slno + "", textFont));
                    cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getEmpName() + "\n" + arrAqBean.getCurDesg() + "\n" + arrAqBean.getGpfAccNo(), textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getGrandTotArr100() + "", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getGrandTotArr40()+"", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getCpfHead(), textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getCpfHead(), textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(0 + "", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getRemark(), textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    dedTotal = dedTotal + deductions;
                    netTotal = netTotal + netamount;
                }
            }

            cell = new PdfPCell(new Phrase("GRAND TOTAL", textFont));
            cell.setColspan(2);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalArrear100 + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalArrear40 + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalCPF + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalCPF + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            Long netamountinword = 0L;
            if (netTotal > 0) {
                netamountinword = (long)totalCPF;
            }

            cell = new PdfPCell();
            cell.setColspan(6);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(Numtowordconvertion.convertNumber(netamountinword.intValue()) + " only", textFont));
            cell.setColspan(2);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }

    @Override
    public void GenerateJudiciaryArrearAquitancePDFReport(Document document, String billNo, CommonReportParamBean crb, List arrAqList, AqreportBean aqreportFormBean) {
        
        int totalArrear100 = 0;
        int totalArrear40 = 0;

        int totalCPF = 0;
        int totalPT = 0;
        int totalIT = 0;
        int totalOR = 0;
        int totalDDORecovery = 0;

        int dedTotal = 0;
        double netTotal = 0;

        try {
            Font textFont = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
            Font bigTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL, BaseColor.BLACK);
            Font boldTextFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);

            PdfPTable table = new PdfPTable(5);
            table.setWidths(new float[]{10, 10, 60, 10, 10});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("STATE-" + aqreportFormBean.getState() + "\n" + "VCH NO: " + StringUtils.defaultString(crb.getVchNo()), bigTextFont));
            cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DIST-" + aqreportFormBean.getDistrict() + "\n" + StringUtils.defaultString(crb.getVchDate()), bigTextFont));
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SCHEDULE-A STATE HEAD QUATERS FORM NO-58\n"
                    + "PAY BILL FOR " + aqreportFormBean.getOffen()
                    + "\n"
                    + "MONTHLY PAY BILL FOR " + aqreportFormBean.getMonth() + "-" + aqreportFormBean.getYear(), bigTextFont));
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("BILL NO:" + aqreportFormBean.getBillNo() + "\nBILL DT:" + aqreportFormBean.getBilldate(), bigTextFont));
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("PAGE:1", bigTextFont));
            cell.setBorder(Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(10);
            table.setWidths(new float[]{2, 11, 4, 4, 2, 3, 4, 5, 6, 6});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("SL NO", bigTextFont));
            cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("NAME AND DESIGNATION", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("ARREAR 100%", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            String arrearpercentagetext = "";
            if (crb.getTypeofBill() != null && (crb.getTypeofBill().equals("ARREAR_J") || crb.getTypeofBill().equals("ARREAR_6_J"))) {
                arrearpercentagetext = "Arrear " + crb.getPercentageArraer() + "%";
            }
            cell = new PdfPCell(new Phrase(arrearpercentagetext, bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("GPF/CPF/TPF", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("P.TAX", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("I.TAX", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);            
            cell = new PdfPCell(new Phrase("TOTAL\nDEDN", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("NET PAY", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("REMARKS", bigTextFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(1)", textFont));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(2)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(3)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(4)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(5)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(6)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(7)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(8)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(9)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(10)", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            if (arrAqList != null && arrAqList.size() > 0) {
                int slno = 0;
                int deductions = 0;
                double netamount = 0;
                for (int i = 0; i < arrAqList.size(); i++) {
                    ArrAqMastModel arrAqBean = (ArrAqMastModel) arrAqList.get(i);

                    slno++;

                    totalArrear100 = totalArrear100 + arrAqBean.getGrandTotArr100();
                    totalArrear40 = totalArrear40 + (int)arrAqBean.getGrandTotArr40();
                    totalCPF = totalCPF + NumberUtils.toInt(arrAqBean.getCpfHead());
                    totalPT = totalPT + arrAqBean.getProfessionalTax();
                    totalIT = totalIT + NumberUtils.toInt(arrAqBean.getIncomeTaxAmt());                    

                    if (arrAqBean.getCpfHead() != null && !arrAqBean.getCpfHead().equals("")) {
                        deductions = deductions + Integer.parseInt(arrAqBean.getCpfHead());
                    }

                    deductions = deductions + arrAqBean.getProfessionalTax();
                    if (arrAqBean.getIncomeTaxAmt() != null && !arrAqBean.getIncomeTaxAmt().equals("")) {
                        deductions = deductions + Integer.parseInt(arrAqBean.getIncomeTaxAmt());
                    }
                    if (arrAqBean.getOtherRecovery() != null && !arrAqBean.getOtherRecovery().equals("")) {
                        deductions = deductions + Integer.parseInt(arrAqBean.getOtherRecovery());
                    }

                    netamount = arrAqBean.getGrandTotArr40() - deductions;

                    cell = new PdfPCell(new Phrase(slno + "", textFont));
                    cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getEmpName() + "\n" + arrAqBean.getCurDesg() + "\n" + arrAqBean.getGpfAccNo(), textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getGrandTotArr100() + "", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getGrandTotArr40() + "", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getCpfHead(), textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getProfessionalTax() + "", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getIncomeTaxAmt(), textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);                    
                    cell = new PdfPCell(new Phrase(deductions + "", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(netamount + "", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arrAqBean.getRemark(), textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    dedTotal = dedTotal + deductions;
                    netTotal = netTotal + netamount;
                }
            }

            cell = new PdfPCell(new Phrase("GRAND TOTAL", textFont));
            cell.setColspan(2);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalArrear100 + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalArrear40 + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalCPF + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalPT + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalIT + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);            
            cell = new PdfPCell(new Phrase(dedTotal + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(netTotal + "", textFont));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            Long netamountinword = 0L;
            if (netTotal > 0) {
                netamountinword = (long)netTotal;
            }

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(Numtowordconvertion.convertNumber(netamountinword.intValue()) + " only", textFont));
            cell.setColspan(5);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
