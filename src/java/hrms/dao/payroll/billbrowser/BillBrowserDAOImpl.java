/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.billbrowser;

import hrms.SelectOption;
import hrms.common.AqFunctionalities;
import hrms.common.CalendarCommonMethods;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.HrmsBillConfig;
import hrms.dao.billvouchingTreasury.VouchingServicesDAOImpl;
import hrms.model.billvouchingTreasury.BillDetail;
import hrms.model.billvouchingTreasury.ObjectBreakup;
import hrms.model.employee.Employee;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Office;
import hrms.model.master.OfficeBean;
import hrms.model.payroll.BytransferDetails;
import hrms.model.payroll.GpfTpfDetails;
import hrms.model.payroll.LtaLoanList;
import hrms.model.payroll.NPSDetails;
import hrms.model.payroll.SalaryBenefitiaryDetails;
import hrms.model.payroll.aqmast.AqmastModel;
import hrms.model.payroll.billbrowser.AllowDeductDetails;
import hrms.model.payroll.billbrowser.BillAttr;
import hrms.model.payroll.billbrowser.BillBean;
import hrms.model.payroll.billbrowser.BillBrowserbean;
import hrms.model.payroll.billbrowser.BillHistoryAttr;
import hrms.model.payroll.billbrowser.FailedTransactionBean;
import hrms.model.payroll.billbrowser.GetBillStatusBean;
import hrms.model.payroll.billbrowser.GlobalBillStatus;
import hrms.model.payroll.schedule.BeneficiaryListBean;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Manas Jena
 */
public class BillBrowserDAOImpl implements BillBrowserDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean grantUploadBill(String offCode, String loginOffCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        boolean uploadRights = true;
        String poffCode = "";
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select off_code, is_ddo, p_off_code from g_office where off_code=?");
            ps.setString(1, offCode);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("is_ddo") != null && !rs.getString("is_ddo").equals("") && rs.getString("is_ddo").equalsIgnoreCase("N")) {
                    uploadRights = false;
                }
                poffCode = rs.getString("p_off_code");
            }

            if (uploadRights == false) {
                if (poffCode.equalsIgnoreCase(loginOffCode)) {
                    uploadRights = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return uploadRights;
    }

    @Override
    public BillBrowserbean getArrearBillPeriod(String offcode, int prepareMonth, int preparedYear) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        BillBrowserbean bb = new BillBrowserbean();
        try {

            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT from_month, from_year, to_month, to_year  FROM BILL_MAST WHERE OFF_CODE=? AND AQ_MONTH=? AND AQ_YEAR=? AND TYPE_OF_BILL=? and from_year >0 and to_year>0");
            pstmt.setString(1, offcode);
            pstmt.setInt(2, prepareMonth);
            pstmt.setInt(3, preparedYear);
            pstmt.setString(4, "ARREAR");
            pstmt.executeQuery();
            rs = pstmt.executeQuery();
            if (rs.next()) {
                bb.setSltFromMonth(rs.getInt("from_month"));
                bb.setSltFromYear(rs.getInt("from_year"));
                bb.setSltToMonth(rs.getInt("to_month"));
                bb.setSltToYear(rs.getInt("to_year"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return bb;
    }

    @Override
    public ArrayList getBillPrepareYear(String offCode) {
        Connection con = null;
        ResultSet rs = null;
        ArrayList al = new ArrayList();
        SelectOption so;
        PreparedStatement pstmt = null;
        try {

            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT DISTINCT AQ_YEAR FROM BILL_MAST WHERE OFF_CODE=?  ORDER  BY AQ_YEAR DESC");
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("AQ_YEAR"));
                so.setValue(rs.getString("AQ_YEAR"));
                al.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;

    }

    @Override
    public ArrayList getMajorHeadListTreasuryWise(String trcode, int aqyear, int aqmonth) {
        Connection con = null;
        ResultSet rs = null;
        ArrayList al = new ArrayList();
        SelectOption so;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select distinct major_head from bill_mast where extract (year from vch_date)=? AND extract (month from vch_date)=? and tr_code in (select tr_code from g_treasury where ag_treasury_code=?) order by major_head");
            pstmt.setInt(1, aqyear);
            pstmt.setInt(2, aqmonth + 1);
            pstmt.setString(3, trcode);
            pstmt.execute();
            rs = pstmt.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("major_head"));
                so.setValue(rs.getString("major_head"));
                al.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;

    }

    @Override
    public ArrayList getVoucherListTreasuryWise(String trcode, int aqyear, int aqmonth, String majorhead) {
        Connection con = null;
        ResultSet rs = null;
        ArrayList al = new ArrayList();
        PreparedStatement pstmt = null;
        LtaLoanList vchlist = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select bill_no,vch_no from bill_mast where major_head=? and  extract (year from vch_date)=? and extract (month from vch_date)=? and tr_code in (select tr_code from g_treasury where ag_treasury_code=?) order by vch_no::integer");
            pstmt.setString(1, majorhead);
            pstmt.setInt(2, aqyear);
            pstmt.setInt(3, aqmonth);
            pstmt.setString(4, trcode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                vchlist = new LtaLoanList();
                vchlist.setBillid(rs.getInt("bill_no"));
                vchlist.setHbabillId(rs.getInt("bill_no"));
                vchlist.setMcaBillId(rs.getInt("bill_no"));
                vchlist.setCompaBillId(rs.getInt("bill_no"));
                vchlist.setVeBillId(rs.getInt("bill_no"));
                vchlist.setGpfBillId(rs.getInt("bill_no"));
                String tvtcno = "";
                if (StringUtils.contains(rs.getString("vch_no"), "/")) {
                    tvtcno = StringUtils.leftPad(StringUtils.substring(rs.getString("vch_no"), (StringUtils.indexOf(rs.getString("vch_no"), "/") + 1)), 4, "0");
                } else if (StringUtils.contains(rs.getString("vch_no"), "-")) {
                    tvtcno = StringUtils.leftPad(StringUtils.substring(rs.getString("vch_no"), (StringUtils.indexOf(rs.getString("vch_no"), "-") + 1)), 4, "0");
                } else {
                    if (rs.getString("vch_no").length() > 4) {
                        tvtcno = StringUtils.leftPad(StringUtils.substring(rs.getString("vch_no"), 4), 4, "0");
                    } else {
                        tvtcno = StringUtils.leftPad(rs.getString("vch_no"), 4, "0");
                    }
                }
                vchlist.setVchno(tvtcno);
                al.add(vchlist);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return al;
    }

    @Override
    public ArrayList getMonthFromSelectedYear(String offCode, int year, String txtBillType) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList month = new ArrayList();
        SelectOption so;
        try {

            if (year != 0) {
                con = dataSource.getConnection();
                if (txtBillType != null && txtBillType.contains("ARREAR")) {
                    pstmt = con.prepareStatement("SELECT DISTINCT AQ_MONTH FROM BILL_MAST WHERE OFF_CODE=? AND AQ_YEAR=? AND TYPE_OF_BILL like '%ARREAR%' ORDER BY AQ_MONTH DESC");
                    pstmt.setString(1, offCode);
                    pstmt.setInt(2, year);
                    rs = pstmt.executeQuery();
                    while (rs.next()) {
                        so = new SelectOption();
                        so.setValue(rs.getString("AQ_MONTH"));
                        so.setLabel(CalendarCommonMethods.getFullMonthAsString(rs.getInt("AQ_MONTH")));
                        month.add(so);
                    }
                } else {
                    pstmt = con.prepareStatement("SELECT DISTINCT AQ_MONTH FROM BILL_MAST WHERE OFF_CODE=? AND AQ_YEAR=? AND TYPE_OF_BILL=? ORDER BY AQ_MONTH DESC");
                    pstmt.setString(1, offCode);
                    pstmt.setInt(2, year);
                    pstmt.setString(3, txtBillType);
                    rs = pstmt.executeQuery();
                    while (rs.next()) {
                        so = new SelectOption();
                        so.setValue(rs.getString("AQ_MONTH"));
                        so.setLabel(CalendarCommonMethods.getFullMonthAsString(rs.getInt("AQ_MONTH")));
                        month.add(so);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return month;

    }

    @Override
    public ArrayList getPayBillList(int year, int month, String offCode, String billType, String spc) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<BillBean> billList = new ArrayList<>();
        try {
            //if (billType != null && billType.equalsIgnoreCase("PAY")) {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT BILL_GRP_ID FROM BILL_GROUP_PRIVILAGE WHERE SPC=?");
            pstmt.setString(1, spc);
            rs = pstmt.executeQuery();
            boolean separateBillMapped = false;
            if (rs.next()) {
                separateBillMapped = true;
                pstmt = con.prepareStatement(" SELECT BILL_NO, BILL_DESC, BILL_DATE, BILL_MAST.BILL_TYPE,TYPE_OF_BILL, BILL_GROUP_DESC DESCRIPTION, BILL_MAST.BILL_STATUS_ID, ONLINE_BILL_SUBMISSION, PAYBILL_TASK.BILL_ID IS_BILL_PREPARED, BILL_MAST.BILL_GROUP_ID, arrear_percent, (select count(*) from bill_beneficiary where bill_id=BILL_MAST.bill_no) noof_emp  FROM ( "
                        + " SELECT BILL_NO,BILL_DESC,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_GROUP_DESC,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION, IS_BILL_PREPARED, BILL_GROUP_ID, arrear_percent  FROM ( "
                        + " SELECT BILL_NO,BILL_DESC,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_GROUP_DESC,BILL_STATUS_ID,OFF_CODE, IS_BILL_PREPARED, BILL_GROUP_ID ,arrear_percent FROM ( "
                        + " SELECT BILL_GRP_ID FROM BILL_GROUP_PRIVILAGE WHERE SPC=? ) BILL_GROUP_PRIVILAGE "
                        + " LEFT OUTER JOIN ( "
                        + " SELECT BILL_NO,BILL_DESC,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_GROUP_DESC,BILL_STATUS_ID,OFF_CODE, IS_BILL_PREPARED, BILL_GROUP_ID, arrear_percent FROM BILL_MAST WHERE OFF_CODE=? AND AQ_MONTH=? AND AQ_YEAR=? AND TYPE_OF_BILL=? ORDER BY BILL_GROUP_DESC)BILL_MAST "
                        + " ON BILL_GROUP_PRIVILAGE.BILL_GRP_ID = BILL_MAST.BILL_GROUP_ID "
                        + " ) BILL_MAST "
                        + " LEFT OUTER JOIN G_OFFICE ON BILL_MAST.OFF_CODE=G_OFFICE.OFF_CODE ORDER BY BILL_DESC) BILL_MAST "
                        + " LEFT OUTER JOIN PAYBILL_TASK ON BILL_MAST.BILL_NO=PAYBILL_TASK.BILL_ID order by BILL_NO");
                pstmt.setString(1, spc);
                pstmt.setString(2, offCode);
                pstmt.setInt(3, month);
                pstmt.setInt(4, year);
                pstmt.setString(5, billType);

            } else {
                pstmt = con.prepareStatement("SELECT BILL_NO,BILL_DATE,BILL_MAST.BILL_TYPE,BILL_MAST.TYPE_OF_BILL,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION , PAYBILL_TASK.BILL_ID IS_BILL_PREPARED,arrear_percent, BILL_MAST.BILL_GROUP_ID,DESCRIPTION, BILL_DESC,IS_DELETED, (select count(*) from bill_beneficiary where bill_id=BILL_MAST.bill_no) noof_emp   FROM ( "
                        + " SELECT BILL_NO,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION , IS_BILL_PREPARED,arrear_percent, BILL_GROUP_ID,DESCRIPTION, BILL_DESC,IS_DELETED FROM ( "
                        + " SELECT BILL_NO,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION , IS_BILL_PREPARED,arrear_percent, BILL_GROUP_ID,DESCRIPTION, BILL_DESC,IS_DELETED FROM ( "
                        + " SELECT BILL_NO,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION , IS_BILL_PREPARED,arrear_percent, BILL_GROUP_MASTER.BILL_GROUP_ID,DESCRIPTION, BILL_DESC,IS_DELETED FROM ( "
                        + " SELECT BILL_GROUP_ID, DESCRIPTION,IS_DELETED FROM BILL_GROUP_MASTER WHERE OFF_CODE=?) BILL_GROUP_MASTER "
                        + " LEFT OUTER JOIN "
                        + " (SELECT BILL_NO,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION , IS_BILL_PREPARED, BILL_GROUP_ID, BILL_DESC, arrear_percent FROM ( "
                        + " SELECT BILL_NO,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_STATUS_ID,IS_BILL_PREPARED, BILL_GROUP_ID,OFF_CODE, BILL_DESC, arrear_percent FROM BILL_MAST WHERE OFF_CODE=? AND AQ_MONTH=? AND AQ_YEAR=? AND TYPE_OF_BILL=?) BILL_MAST "
                        + " INNER JOIN G_OFFICE ON BILL_MAST.OFF_CODE=G_OFFICE.OFF_CODE) BILL_MAST "
                        + " ON BILL_GROUP_MASTER.BILL_GROUP_ID=BILL_MAST.BILL_GROUP_ID  WHERE (IS_DELETED='N' OR IS_DELETED IS NULL) OR (IS_DELETED='Y' AND IS_BILL_PREPARED ='Y') ORDER BY DESCRIPTION) BILL_MAST ) BILL_MAST ) BILL_MAST "
                        + " LEFT OUTER JOIN PAYBILL_TASK ON BILL_MAST.BILL_NO=PAYBILL_TASK.BILL_ID  order by BILL_NO");
                pstmt.setString(1, offCode);
                pstmt.setString(2, offCode);
                pstmt.setInt(3, month);
                pstmt.setInt(4, year);
                pstmt.setString(5, billType);

            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                BillBean bb = new BillBean();
                bb.setBillgroupId(rs.getString("BILL_GROUP_ID"));
                bb.setBillno(rs.getString("BILL_NO"));
                bb.setBilldesc(rs.getString("BILL_DESC"));
                bb.setBilldate(CommonFunctions.getFormattedOutputDate1(rs.getDate("BILL_DATE")));
                bb.setBilltype(rs.getString("TYPE_OF_BILL"));
                bb.setBillGroupDesc(rs.getString("DESCRIPTION"));
                if (rs.getInt("BILL_STATUS_ID") == 0) {
                    bb.setShowLink("");
                } else if (rs.getInt("BILL_STATUS_ID") == 2 || rs.getInt("BILL_STATUS_ID") == 4 || rs.getInt("BILL_STATUS_ID") == 8) {
                    bb.setShowLink("Y");
                } else if (rs.getInt("BILL_STATUS_ID") > 2 && rs.getInt("BILL_STATUS_ID") != 4 && rs.getInt("BILL_STATUS_ID") != 8) {
                    bb.setShowLink("N");
                } else {
                    bb.setShowLink("");
                }
                bb.setLockBill(rs.getInt("BILL_STATUS_ID"));
                bb.setOnlinebillapproved(rs.getString("ONLINE_BILL_SUBMISSION"));
                bb.setNooofEmployee(rs.getInt("noof_emp"));
                if (rs.getString("IS_BILL_PREPARED") != null && !rs.getString("IS_BILL_PREPARED").equals("")) {
                    bb.setIsbillPrepared("N");
                } else {
                    bb.setIsbillPrepared("Y");
                }
                bb.setPercentageArraer(rs.getInt("arrear_percent") + "");

                if (bb.getBillno() != null && !bb.getBillno().equals("")) {
                    bb.setFtcount(getFailedTransactionCount(Integer.parseInt(bb.getBillno())));
                } else {
                    bb.setFtcount(0);
                }
                if (bb.getBillno() != null && !bb.getBillno().equals("")) {
                    bb.setCheckESignStatus(checkEsign(offCode, bb.getBillno()));
                }
                bb.setFrontpageslno(getBillFrontPageSlNo(bb.getBilltype()) + "");
                bb.setSelectedBilltype(billType);
                billList.add(bb);
            }
            //}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billList;
    }

    private String checkEsign(String offCode, String billno) {
        String result = "N";
        String ddo_hrmsid = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("SELECT id_esign_log FROM esign_log WHERE office_code = ? AND bill_no=? AND report_ref_slno=? AND signed_pdf_file IS NOT NULL");
            ps.setString(1, offCode);
            ps.setInt(2, Integer.parseInt(billno));
            ps.setInt(3, 2);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = "Y";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return result;
    }

    private String checkArrearEsign(String offCode, String billno) {
        String result = "N";
        String ddo_hrmsid = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("SELECT id_esign_log FROM esign_log WHERE office_code = ? AND bill_no=?\n"
                    + "AND report_ref_slno in (79,93,111,137,156,177) AND signed_pdf_file IS NOT NULL");
            ps.setString(1, offCode);
            ps.setInt(2, Integer.parseInt(billno));
            rs = ps.executeQuery();
            if (rs.next()) {
                result = "Y";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return result;
    }

    @Override
    public List getDdoCodeList(String offCode) {
        Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        List treasuryList = new ArrayList();
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("select g_office.ddo_code from g_office "
                    + " where g_office.off_code='" + offCode + "'");
            while (rs.next()) {
                SelectOption so = new SelectOption();

                so.setValue(rs.getString("ddo_code"));
                so.setLabel(rs.getString("ddo_code"));
                treasuryList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return treasuryList;
    }

    public ArrayList getBillDetails(String offcode, int month, int year) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList billList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select billmast.bill_no,billmast.bill_desc,billmast.bill_date,billmast.aq_month,billmast.aq_year,billmast.off_code, billmast.bill_group_desc, "
                    + "billmast.token_no,billmast.token_date,billmast.bill_status_id,billstatus.bill_status from "
                    + "(select * from bill_mast where off_code=? and aq_month=? and aq_year=?)billmast left outer join (select * from g_bill_status)billstatus on billmast.bill_status_id=billstatus.id");
            pstmt.setString(1, offcode);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                BillDetail billDetail = new BillDetail();
                billDetail.setOffcode(rs.getString("off_code"));
                billDetail.setBillnumber(rs.getString("bill_no"));
                billDetail.setBilldesc(rs.getString("bill_desc"));
                billDetail.setBilldesc(rs.getString("bill_group_desc"));
                billDetail.setBillStatus(rs.getString("bill_status"));
                billDetail.setBillStatusId(rs.getInt("bill_status_id"));
                billDetail.setBillDate(CommonFunctions.getFormattedInputDate(rs.getDate("bill_date")));
                billDetail.setBillmonth(rs.getInt("aq_month"));
                billDetail.setBillyear(rs.getInt("aq_year"));
                billDetail.setTokenNumber(StringUtils.defaultString(rs.getString("token_no")));
                billDetail.setTokendate(CommonFunctions.getFormattedInputDate(rs.getDate("token_date")));
                billDetail.setPrevTokenNumber(StringUtils.defaultString(rs.getString("previous_token_no")));
                billDetail.setPrevTokendate(CommonFunctions.getFormattedInputDate(rs.getDate("previous_token_date")));
                billList.add(billDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billList;
    }

    @Override
    public BillDetail getBillDetails(int billno) {
        BillDetail bill = null;
        VouchingServicesDAOImpl vserv = new VouchingServicesDAOImpl();
        Calendar cal = Calendar.getInstance();

        String month = "";
        String billdate = "";
        String periodFrom = "";
        String periodTo = "";
        String ddocode = "";
        String treasuryCode = "";

        int year = 0;
        int billmonth = 0;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        try {
            con = this.dataSource.getConnection();

            sql = "SELECT ddo_hrmsid,ddo_post,BILL_MAST.OFF_CODE,OFF_NAME,OFF_DDO,BILL_MAST.DDO_CODE,BILL_NO,BILL_DATE,BILL_DESC,PLAN,SECTOR,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD, "
                    + " SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,BILL_TYPE,BILL_MAST.TR_CODE,AQ_MONTH,AQ_YEAR,DEMAND_NO,BEN_REF_NO,PREVIOUS_TOKEN_NO,PREVIOUS_TOKEN_DATE,"
                    + " IS_RESUBMITTED,type_of_bill,bill_status_id,bill_group_id, vch_no, vch_date, gross_amt , "
                    + " ded_amt , pvt_ded_amt, FROM_MONTH, FROM_YEAR, TO_MONTH, TO_YEAR, FROM_DATE, TO_DATE, bill_mast.co_code,allow_esign FROM ( "
                    + " SELECT bill_mast.OFF_CODE,BILL_NO,BILL_DATE,BILL_DESC,OFF_DDO,bill_mast.DDO_CODE,PLAN,SECTOR,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD, "
                    + " SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,BILL_TYPE,bill_mast.TR_CODE,AQ_MONTH,AQ_YEAR,DEMAND_NO,BEN_REF_NO,PREVIOUS_TOKEN_NO,PREVIOUS_TOKEN_DATE,IS_RESUBMITTED, type_of_bill, "
                    + " bill_status_id, bill_group_id, vch_no, vch_date, gross_amt ,ded_amt , pvt_ded_amt, FROM_MONTH, FROM_YEAR, TO_MONTH, TO_YEAR, FROM_DATE,TO_DATE, bill_mast.co_code  "
                    + " FROM BILL_MAST "
                    + " WHERE BILL_NO=? "
                    + " ) BILL_MAST "
                    + " INNER JOIN G_OFFICE ON BILL_MAST.OFF_CODE=G_OFFICE.OFF_CODE ";

            ps = con.prepareStatement(sql);
            ps.setInt(1, billno);
            rs = ps.executeQuery();
            if (rs.next()) {

                bill = new BillDetail();
                bill.setDdohrmisd(rs.getString("ddo_hrmsid"));
                bill.setDdodesgn(rs.getString("ddo_post"));
                bill.setBillgroupId(rs.getString("bill_group_id"));
                bill.setHrmsgeneratedRefno(rs.getString("BILL_NO"));
                billdate = CommonFunctions.getFormattedOutputDate3(rs.getDate("BILL_DATE"));
                bill.setHrmsgeneratedRefdate(billdate);
                bill.setBillType("N");
                bill.setBillnumber(rs.getString("BILL_DESC"));
                bill.setBillDate(billdate);
                bill.setBilldesc(rs.getString("BILL_DESC"));
                year = rs.getInt("AQ_YEAR");
                billmonth = rs.getInt("AQ_MONTH");
                bill.setBillyear(year);
                bill.setBillmonth(billmonth + 1);
                bill.setProcessFromDate(rs.getInt("FROM_DATE") + "");
                bill.setProcessToDate(rs.getInt("TO_DATE") + "");
                bill.setToMonth(rs.getInt("TO_MONTH"));
                bill.setToYear(rs.getInt("TO_YEAR"));
                bill.setTypeOfBill(rs.getString("type_of_bill"));
                bill.setAllowEsign(rs.getString("allow_esign"));
                if (rs.getString("AQ_MONTH") != null) {

                    month = CalendarCommonMethods.getFullMonthAsString((rs.getInt("AQ_MONTH")));
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, rs.getInt("AQ_MONTH"));
                    cal.set(Calendar.DATE, 1);
                    bill.setAq_month(rs.getString("AQ_MONTH"));
                }
                SimpleDateFormat date_format = new SimpleDateFormat("dd-MMM-yyyy");
                if (rs.getString("AQ_YEAR") != null) {
                    bill.setAq_year(rs.getString("AQ_YEAR"));
                }

                bill.setAgbillTypeId(rs.getString("BILL_TYPE"));
                if (rs.getString("type_of_bill").equals("ARREAR") || rs.getString("type_of_bill").equals("ARREAR_6")) {
                    bill.setSalFromdate("01-JAN-2016");
                } else if (rs.getString("type_of_bill").equals("OTHER_ARREAR") || rs.getString("type_of_bill").equals("ARREAR_J") || rs.getString("type_of_bill").equals("ARREAR_6_J")) {
                    Calendar cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("FROM_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("FROM_YEAR"));
                    cal2.set(Calendar.DATE, rs.getInt("FROM_DATE"));
                    bill.setSalFromdate(date_format.format(cal2.getTime()));

                } else if (rs.getString("type_of_bill").equals("ARREAR_NPS")) {
                    Calendar cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("FROM_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("FROM_YEAR"));
                    cal2.set(Calendar.DATE, rs.getInt("FROM_DATE"));
                    bill.setSalFromdate(date_format.format(cal2.getTime()));

                } else if (rs.getString("type_of_bill").equals("OA_ARREAR")) {
                    Calendar cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("FROM_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("FROM_YEAR"));
                    cal2.set(Calendar.DATE, rs.getInt("FROM_DATE"));
                    bill.setSalFromdate(date_format.format(cal2.getTime()));
                } else if (rs.getString("type_of_bill").equals("LEAVE_ARREAR")) {
                    Calendar cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("FROM_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("FROM_YEAR"));
                    cal2.set(Calendar.DATE, rs.getInt("FROM_DATE"));
                    bill.setSalFromdate(date_format.format(cal2.getTime()));
                } else if (rs.getString("type_of_bill").equals("UNUTILISED_LEAVE_ARREAR")) {
                    Calendar cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("FROM_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("FROM_YEAR"));
                    cal2.set(Calendar.DATE, rs.getInt("FROM_DATE"));
                    bill.setSalFromdate(date_format.format(cal2.getTime()));
                } else if (rs.getString("type_of_bill").equals("PAY")) {
                    bill.setSalFromdate(date_format.format(cal.getTime()));
                }

                periodFrom = bill.getSalFromdate();
                cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                if (rs.getString("type_of_bill").equals("ARREAR") || rs.getString("type_of_bill").equals("ARREAR_6")) {
                    bill.setSalTodate("31-AUG-2017");
                } else if (rs.getString("type_of_bill").equals("OTHER_ARREAR") || rs.getString("type_of_bill").equals("ARREAR_J") || rs.getString("type_of_bill").equals("ARREAR_6_J")) {
                    Calendar cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("TO_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("TO_YEAR"));
                    cal2.set(Calendar.DATE, rs.getInt("TO_DATE"));

                    bill.setSalTodate(date_format.format(cal2.getTime()));

                } else if (rs.getString("type_of_bill").equals("ARREAR_NPS")) {
                    Calendar cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("TO_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("TO_YEAR"));
                    cal2.set(Calendar.DATE, rs.getInt("TO_DATE"));

                    bill.setSalTodate(date_format.format(cal2.getTime()));

                } else if (rs.getString("type_of_bill").equals("OA_ARREAR")) {
                    Calendar cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("TO_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("TO_YEAR"));
                    cal2.set(Calendar.DATE, rs.getInt("TO_DATE"));

                    bill.setSalTodate(date_format.format(cal2.getTime()));

                } else if (rs.getString("type_of_bill").equals("LEAVE_ARREAR")) {
                    Calendar cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("TO_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("TO_YEAR"));
                    cal2.set(Calendar.DATE, rs.getInt("TO_DATE"));

                    bill.setSalTodate(date_format.format(cal2.getTime()));

                } else if (rs.getString("type_of_bill").equals("UNUTILISED_LEAVE_ARREAR")) {
                    Calendar cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("TO_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("TO_YEAR"));
                    cal2.set(Calendar.DATE, rs.getInt("TO_DATE"));

                    bill.setSalTodate(date_format.format(cal2.getTime()));

                } else if (rs.getString("type_of_bill").equals("PAY")) {
                    bill.setSalTodate(date_format.format(cal.getTime()));
                }

                periodTo = bill.getSalTodate();
                ddocode = rs.getString("DDO_CODE");
                bill.setOffcode(rs.getString("off_code"));
                bill.setDdoccode(ddocode);
                bill.setDemandNumber(rs.getString("DEMAND_NO"));
                bill.setMajorHead(rs.getString("MAJOR_HEAD"));
                bill.setSubMajorHead(rs.getString("SUB_MAJOR_HEAD"));
                bill.setMinorHead(rs.getString("MINOR_HEAD"));
                bill.setSubHead(rs.getString("SUB_MINOR_HEAD1"));
                bill.setDetailHead(rs.getString("SUB_MINOR_HEAD2"));
                bill.setPlanStatus(rs.getString("PLAN"));
                bill.setChargedVoted(rs.getString("SUB_MINOR_HEAD3"));
                bill.setSectorCode(rs.getString("SECTOR"));
                bill.setTypeofBillString(rs.getString("type_of_bill"));
                bill.setVchDt(CommonFunctions.getFormattedOutputDate3(rs.getDate("vch_date")));
                bill.setVchNo(rs.getString("vch_no"));
                bill.setPrevTokenNumber(null);
                bill.setPrevTokendate(null);
                treasuryCode = rs.getString("TR_CODE");
                bill.setTreasuryCode(treasuryCode);
                //bill.setBeneficiaryrefno(rs.getString("BEN_REF_NO"));
                if (rs.getString("IS_RESUBMITTED") != null && rs.getString("IS_RESUBMITTED").equals("Y")) {
                    bill.setBillType("R");
                    bill.setPrevTokenNumber(rs.getString("PREVIOUS_TOKEN_NO"));
                    bill.setPrevTokendate(CommonFunctions.getFormattedOutputDate3(rs.getDate("PREVIOUS_TOKEN_DATE")));
                }
                if (rs.getString("type_of_bill").equals("OTHER_ARREAR")) {
                    String billgross = getGrossForArrear(billno);
                    if (billgross != null && !billgross.equals("")) {
                        bill.setGrossAmount(Double.valueOf(billgross).longValue() + "");

                        int net = Integer.parseInt(billgross);
                        String totded = getTotalDeductionForArrear(billno);
                        int pvtdedAmt = getDDORecoveryList(billno);
                        if (pvtdedAmt > 0) {
                            bill.setPvtDedamt(Integer.toString(pvtdedAmt));
                        }

                        if (totded != null && !totded.equals("")) {
                            net = Integer.parseInt(billgross) - Integer.parseInt(totded);
                        }
                        bill.setNetAmount(Double.valueOf(net + "").longValue() + "");
                    }
                } else if (rs.getString("type_of_bill").equals("OA_ARREAR")) {
                    String billgross = getGrossForArrear(billno);
                    if (billgross != null && !billgross.equals("")) {
                        bill.setGrossAmount(Double.valueOf(billgross).longValue() + "");

                        int net = Integer.parseInt(billgross);
                        String totded = getTotalDeductionForArrear(billno);
                        if (totded != null && !totded.equals("")) {
                            net = Integer.parseInt(billgross) - Integer.parseInt(totded);
                        }
                        bill.setNetAmount(Double.valueOf(net + "").longValue() + "");
                    }
                } else if (rs.getString("type_of_bill").equals("LEAVE_ARREAR")) {
                    String billgross = getGrossForArrear(billno);
                    if (billgross != null && !billgross.equals("")) {
                        bill.setGrossAmount(Double.valueOf(billgross).longValue() + "");

                        int net = Integer.parseInt(billgross);
                        String totded = getTotalDeductionForArrear(billno);
                        if (totded != null && !totded.equals("")) {
                            net = Integer.parseInt(billgross) - Integer.parseInt(totded);
                        }
                        bill.setNetAmount(Double.valueOf(net + "").longValue() + "");
                    }
                } else if (rs.getString("type_of_bill").equals("UNUTILISED_LEAVE_ARREAR")) {
                    String billgross = getGrossForArrear(billno);
                    if (billgross != null && !billgross.equals("")) {
                        bill.setGrossAmount(Double.valueOf(billgross).longValue() + "");

                        int net = Integer.parseInt(billgross);
                        String totded = getTotalDeductionForArrear(billno);
                        if (totded != null && !totded.equals("")) {
                            net = Integer.parseInt(billgross) - Integer.parseInt(totded);
                        }
                        bill.setNetAmount(Double.valueOf(net + "").longValue() + "");
                    }
                } else if (rs.getString("type_of_bill").equals("ARREAR_NPS")) {
                    String billgross = getGrossForNPSArrear(billno);
                    if (billgross != null && !billgross.equals("")) {
                        bill.setGrossAmount(Double.valueOf(billgross).longValue() + "");

                        int net = Integer.parseInt(billgross);
                        String totded = getTotalDeductionForArrear(billno);
                        if (totded != null && !totded.equals("")) {
                            net = Integer.parseInt(billgross) - Integer.parseInt(totded);
                        }
                        bill.setNetAmount("0");
                    }
                } else if (rs.getString("type_of_bill") != null && rs.getString("type_of_bill").equals("ARREAR_6")) {
                    bill.setGrossAmount(Double.valueOf(rs.getInt("gross_amt") + "").longValue() + "");
                    String totded = getTotalDeductionForArrear(billno);
                    int net = 0;
                    if (totded != null && !totded.equals("")) {
                        net = rs.getInt("gross_amt") - Integer.parseInt(totded);
                    } else {
                        net = rs.getInt("gross_amt") - rs.getInt("ded_amt");
                    }
                    bill.setNetAmount(Double.valueOf(net + "").longValue() + "");
                } else {
                    bill.setGrossAmount(Double.valueOf(rs.getInt("gross_amt") + "").longValue() + "");
                    int net = rs.getInt("gross_amt") - rs.getInt("ded_amt");
                    bill.setNetAmount(Double.valueOf(net + "").longValue() + "");
                }

                bill.setBillStatusId(rs.getInt("bill_status_id"));
                bill.setCoCode(rs.getString("co_code"));

            }

            DataBaseFunctions.closeSqlObjects(rs, ps);
            if (bill.getCoCode() != null && !bill.getCoCode().equals("")) {

                ps = con.prepareStatement("select off_en from g_office where co_code=?");
                ps.setString(1, bill.getCoCode());
                rs = ps.executeQuery();
                if (rs.next()) {
                    bill.setCoName(rs.getString("off_en"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return bill;
    }

    @Override
    public String getBillSubminorHead3(int billId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String head = "";
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT sub_minor_head2 FROM BILL_MAST WHERE BILL_NO=?");
            ps.setInt(1, billId);
            rs = ps.executeQuery();
            if (rs.next()) {
                head = rs.getString("sub_minor_head2");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return head;
    }

    @Override
    public ArrayList getOBJXMLData(int billId, String treasuryCode, double basicPay, String billdate, String typeofbill, String head, int aqMonth, int aqYear) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList objlist = new ArrayList();
        String aqDTLS = "AQ_DTLS";
        try {
            con = this.dataSource.getConnection();

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(aqMonth, aqYear);
            String sql = "";
            if (typeofbill != null) {
                if (typeofbill.equalsIgnoreCase("PAY")) {
                    sql = "select BT_ID,sum(ad_amt) AMT, emp_type from " + aqDTLS + " aq_dtls inner join (select * from AQ_MAST where bill_no=? and aq_month=? "
                            + " and aq_year=? )AQ_MAST on AQ_MAST.aqsl_no = aq_dtls.aqsl_no "
                            + " where ad_type = 'A' and ad_amt > 0 GROUP BY BT_ID, emp_type";
                    pst = con.prepareStatement(sql);
                    pst.setInt(1, billId);
                    pst.setInt(2, aqMonth);
                    pst.setInt(3, aqYear);

                    rs = pst.executeQuery();

                    boolean payheadadded = false;
                    ObjectBreakup object = null;
                    String empType = "";

                    while (rs.next()) {
                        object = new ObjectBreakup();
                        object.setHrmsgeneratedRefno(billId);
                        object.setHrmsgeneratedRefdate(billdate);
                        object.setObjectHead(rs.getString("BT_ID"));

                        if (object.getObjectHead().equals(head)) {
                            payheadadded = true;
                            //bill.setGrossAmount(Double.valueOf(rs.getInt("gross_amt") + "").longValue() + "");

                            String val = Double.valueOf(basicPay + rs.getDouble("AMT")).longValue() + "";
                            object.setObjectHeadwiseAmount(val);

                        } else {
                            String val = Double.valueOf(rs.getDouble("AMT")).longValue() + "";
                            object.setObjectHeadwiseAmount(val);
                        }

                        object.setTreasuryCode(treasuryCode);
                        objlist.add(object);

                        /*
                         if (billtype.equals("21")) {
                         if (object.getObjectHead().equals("921")) {
                         head136added = true;
                         object.setObjectHeadwiseAmount(basicPay + rs.getDouble("AMT"));
                         } else {
                         object.setObjectHeadwiseAmount(rs.getDouble("AMT"));
                         }
                         } else {
                         if (rs.getString("emp_type").equals("R")) {
                         if (object.getObjectHead().equals("000")) {
                         head136added = true;
                         object.setObjectHeadwiseAmount(basicPay + rs.getDouble("AMT"));
                         } else if (object.getObjectHead().equals("136")) {
                         head136added = true;
                         object.setObjectHeadwiseAmount(basicPay + rs.getDouble("AMT"));
                         } else {
                         object.setObjectHeadwiseAmount(rs.getDouble("AMT"));
                         }
                         } else {
                         if (object.getObjectHead().equals("000")) {
                         head136added = true;
                         object.setObjectHeadwiseAmount(basicPay + rs.getDouble("AMT"));
                         } else if (object.getObjectHead().equals("136")) {
                         head136added = true;
                         object.setObjectHeadwiseAmount(basicPay + rs.getDouble("AMT"));
                         } else {
                         object.setObjectHeadwiseAmount(rs.getDouble("AMT"));
                         }
                         }
                        
                        
                         }

                         object.setTreasuryCode(treasuryCode);
                         objlist.add(object);
                         */
                    }
                    if (payheadadded == false) {

                        object = new ObjectBreakup();
                        object.setHrmsgeneratedRefno(billId);
                        object.setHrmsgeneratedRefdate(billdate);

                        object.setObjectHead(head);
                        String val = Double.valueOf(basicPay).longValue() + "";
                        object.setObjectHeadwiseAmount(val);
                        object.setTreasuryCode(treasuryCode);
                        if (basicPay > 0) {
                            objlist.add(object);
                        }
                    }

                } else if (typeofbill.equalsIgnoreCase("OT")) {
                    sql = "SELECT sum(TOTAL) AMT,sum(DA) DA FROM AQ_MAST_OT WHERE bill_no=?";
                    pst = con.prepareStatement(sql);
                    pst.setInt(1, billId);
                    rs = pst.executeQuery();
                    ObjectBreakup object = null;

                    if (rs.next()) {
                        object = new ObjectBreakup();
                        object.setHrmsgeneratedRefno(billId);
                        object.setHrmsgeneratedRefdate(billdate);
                        object.setObjectHead("156");
                        String val = Double.valueOf(rs.getDouble("DA")).longValue() + "";
                        object.setObjectHeadwiseAmount(val);
                        object.setTreasuryCode(treasuryCode);
                        objlist.add(object);

                        object = new ObjectBreakup();
                        object.setHrmsgeneratedRefno(billId);
                        object.setHrmsgeneratedRefdate(billdate);
                        object.setObjectHead("136");
                        val = Double.valueOf(rs.getInt("AMT") - rs.getDouble("DA")).longValue() + "";
                        object.setObjectHeadwiseAmount(val);
                        object.setTreasuryCode(treasuryCode);
                        objlist.add(object);
                    }

                } else if (typeofbill.equalsIgnoreCase("ARREAR") || typeofbill.equalsIgnoreCase("ARREAR_6") || typeofbill.equalsIgnoreCase("ARREAR_J") || typeofbill.equalsIgnoreCase("ARREAR_6_J")) {
                    sql = "select sum(arrear_pay) arrear_pay from arr_mast mast where bill_no=?";
                    pst = con.prepareStatement(sql);
                    pst.setInt(1, billId);
                    rs = pst.executeQuery();
                    ObjectBreakup object = null;

                    if (rs.next()) {
                        /*
                         object = new ObjectBreakup();
                         object.setHrmsgeneratedRefno(billId);
                         object.setHrmsgeneratedRefdate(billdate);
                         object.setObjectHead("156");
                         object.setObjectHeadwiseAmount(rs.getDouble("DA"));
                         object.setTreasuryCode(treasuryCode);
                         objlist.add(object);
                         */
                        object = new ObjectBreakup();
                        object.setHrmsgeneratedRefno(billId);
                        object.setHrmsgeneratedRefdate(billdate);
                        object.setObjectHead(getObjectHead(billId));
                        String val = Double.valueOf(rs.getInt("arrear_pay")).longValue() + "";
                        object.setObjectHeadwiseAmount(val);
                        object.setTreasuryCode(treasuryCode);
                        objlist.add(object);
                    }

                } else if (typeofbill.equalsIgnoreCase("ARREAR_NPS")) {
                    ObjectBreakup object = new ObjectBreakup();
                    object.setHrmsgeneratedRefno(billId);
                    object.setHrmsgeneratedRefdate(billdate);
                    object.setObjectHead(getObjectHead(billId));
                    String val = getGrossForNPSArrear(billId);
                    object.setObjectHeadwiseAmount(val);
                    object.setTreasuryCode(treasuryCode);
                    objlist.add(object);

                } else {
                    pst = con.prepareStatement("SELECT * FROM(SELECT BT_ID,sum(to_be_paid-already_paid) adamt FROM ARR_DTLS INNER JOIN ARR_MAST ON  ARR_DTLS.AQSL_NO = ARR_MAST.AQSL_NO WHERE BILL_NO = ? group by bt_id) T1 WHERE adamt != 0 order by adamt");
                    pst.setInt(1, billId);
                    rs = pst.executeQuery();
                    ObjectBreakup object = null;
                    int tAmt = 0;
                    while (rs.next()) {
                        object = new ObjectBreakup();
                        object.setHrmsgeneratedRefno(billId);
                        object.setHrmsgeneratedRefdate(billdate);
                        object.setObjectHead(rs.getString("BT_ID"));

                        if (rs.getInt("adamt") > 0) {

                            if (object.getObjectHead() != null && object.getObjectHead().equals("136")) {
                                if (rs.getInt("adamt") > tAmt) {
                                    object.setObjectHeadwiseAmount((rs.getInt("adamt") + tAmt) + "");
                                    tAmt = 0;
                                } else {
                                    object.setObjectHeadwiseAmount(rs.getInt("adamt") + "");
                                }
                            } else if (object.getObjectHead() != null && object.getObjectHead().equals("156")) {
                                if (rs.getInt("adamt") > tAmt) {
                                    object.setObjectHeadwiseAmount((rs.getInt("adamt") + tAmt) + "");
                                    tAmt = 0;
                                } else {
                                    object.setObjectHeadwiseAmount(rs.getInt("adamt") + "");
                                }
                            } else {
                                object.setObjectHeadwiseAmount(rs.getInt("adamt") + "");
                            }

                            //tAmt = 0;
                            object.setTreasuryCode(treasuryCode);
                            if (object.getObjectHeadwiseAmount() != null && Integer.parseInt(object.getObjectHeadwiseAmount()) < 0) {
                                tAmt = Integer.parseInt(object.getObjectHeadwiseAmount());
                            } else {
                                objlist.add(object);
                            }

                        } else {
                            tAmt = rs.getInt("adamt") + tAmt;

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
        return objlist;
    }

    private String getObjectHead(int billId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String obejctHead = "855";
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select PAY_HEAD from bill_mast where BILL_NO=?");
            pstmt.setInt(1, billId);
            rs = pstmt.executeQuery();
            if (rs.next()) {

                if (rs.getString("PAY_HEAD") != null && !rs.getString("PAY_HEAD").equals("")) {
                    if (rs.getString("PAY_HEAD").equals("136")) {
                        obejctHead = "855";
                    } else if (rs.getString("PAY_HEAD").equals("921")) {
                        obejctHead = "921";
                    } else if (rs.getString("PAY_HEAD").equals("000")) {
                        obejctHead = "000";
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return obejctHead;
    }

    @Override
    public String getScheduleSlNo(String btId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String schSlNo = "";

        try {
            con = this.dataSource.getConnection();
            String schSlNoQry = "select ga.sl_no schSlNo, gal.schedule from g_ad_list gal, g_schedule ga where gal.schedule = ga.schedule and bt_id = ?";
            pstmt = con.prepareStatement(schSlNoQry);
            pstmt.setString(1, btId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                schSlNo = rs.getString("schSlNo");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return schSlNo;
    }

    @Override
    public String getFixedScheduleSlNo(String billType) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String fixedSchSlNo = "";

        try {
            con = this.dataSource.getConnection();
            String schSlNoQry = "select sl_no from g_schedule_fixed_link where bill_type = ?  order by sl_no";
            pstmt = con.prepareStatement(schSlNoQry);
            pstmt.setString(1, billType);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String tempSchSlNo = rs.getString("sl_no");
                if (fixedSchSlNo.equals("")) {
                    fixedSchSlNo = tempSchSlNo;
                } else {
                    fixedSchSlNo = fixedSchSlNo + "," + tempSchSlNo;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fixedSchSlNo;
    }

    @Override
    public int updateRequiredReportsColumn(String schSlNO, int billno) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int updRes = 0;
        try {
            con = this.dataSource.getConnection();

            pstmt = con.prepareStatement("UPDATE BILL_MAST SET required_reports = ? WHERE BILL_NO = ?");
            pstmt.setString(1, schSlNO);
            pstmt.setInt(2, billno);
            updRes = pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return updRes;
    }

    @Override
    public ArrayList getBTXMLData(int billId, String treasuryCode, String billdate, String typeofbill, int aqMonth, int aqYear) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        PreparedStatement ps2 = null;
        ArrayList bytranlist = new ArrayList();
        String aqDTLS = "AQ_DTLS";
        int ddoRecovery = 0;
        try {
            con = this.dataSource.getConnection();

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(aqMonth, aqYear);
            st = con.createStatement();
            if (typeofbill.equalsIgnoreCase("PAY")) {

                rs = st.executeQuery("SELECT BT_ID,SUM(AD_AMT) AD_AMT FROM (SELECT AQSL_NO FROM AQ_MAST WHERE BILL_NO=" + billId + " and aq_month=" + aqMonth + " "
                        + "and aq_year=" + aqYear + " )AQ_MAST INNER JOIN (SELECT * FROM " + aqDTLS + " AQ_DTLS WHERE AD_TYPE='D'  AND AD_AMT >0 and SCHEDULE != 'PVTL' and SCHEDULE != 'PVTD' "
                        + "and aq_month=" + aqMonth + " and aq_year=" + aqYear + " )AQ_DTLS ON AQ_MAST.AQSL_NO = AQ_DTLS.AQSL_NO GROUP BY BT_ID ");

                BytransferDetails bytran = null;
                while (rs.next()) {
                    bytran = new BytransferDetails();
                    bytran.setHrmsgeneratedRefno(billId);
                    bytran.setHrmsgeneratedRefdate(billdate);
                    bytran.setBtserialno(rs.getString("BT_ID"));
                    bytran.setBytransferType("Ag Bt");

                    bytran.setAmount(rs.getDouble("AD_AMT"));
                    bytran.setTreasuryCode(treasuryCode);

                    bytranlist.add(bytran);
                }
            } else if (typeofbill.equalsIgnoreCase("ARREAR") || typeofbill.equalsIgnoreCase("ARREAR_6") || typeofbill.equalsIgnoreCase("ARREAR_J") || typeofbill.equalsIgnoreCase("ARREAR_6_J")) {
                rs = st.executeQuery("select sum(inctax) inctax, sum(cpf_head) cpf_head, sum(pt) pt from arr_mast where bill_no=" + billId);

                BytransferDetails bytran = null;
                if (rs.next()) {
                    bytran = new BytransferDetails();
                    bytran.setHrmsgeneratedRefno(billId);
                    bytran.setHrmsgeneratedRefdate(billdate);
                    bytran.setBtserialno("58816");
                    bytran.setBytransferType("Ag Bt");
                    bytran.setAmount(rs.getInt("inctax"));
                    bytran.setTreasuryCode(treasuryCode);

                    bytranlist.add(bytran);

                    bytran = new BytransferDetails();
                    bytran.setHrmsgeneratedRefno(billId);
                    bytran.setHrmsgeneratedRefdate(billdate);
                    bytran.setBtserialno("57740");
                    bytran.setBytransferType("Ag Bt");
                    bytran.setAmount(rs.getInt("cpf_head"));
                    bytran.setTreasuryCode(treasuryCode);

                    bytranlist.add(bytran);

                    bytran = new BytransferDetails();
                    bytran.setHrmsgeneratedRefno(billId);
                    bytran.setHrmsgeneratedRefdate(billdate);
                    bytran.setBtserialno("3043");
                    bytran.setBytransferType("Ag Bt");
                    bytran.setAmount(rs.getInt("pt"));
                    bytran.setTreasuryCode(treasuryCode);

                    bytranlist.add(bytran);

                }
            } else if (typeofbill.equalsIgnoreCase("ARREAR_NPS")) {
                rs = st.executeQuery("select sum(cpf_head) cpf_head from arr_mast where bill_no=" + billId);

                BytransferDetails bytran = null;
                if (rs.next()) {
                    bytran = new BytransferDetails();
                    bytran.setHrmsgeneratedRefno(billId);
                    bytran.setHrmsgeneratedRefdate(billdate);
                    bytran.setBtserialno("57740");
                    bytran.setBytransferType("Ag Bt");
                    bytran.setAmount(rs.getInt("cpf_head"));
                    bytran.setTreasuryCode(treasuryCode);

                    bytranlist.add(bytran);
                }
            } else if (typeofbill.equals("OTHER_ARREAR")) {
                rs = st.executeQuery("select sum(inctax) inctax, sum(pt) pt,sum(other_recovery) otherrecovery,\n"
                        + "sum(ddo_recovery) ddorecovery,(select distinct btid_or from arr_mast where bill_no='" + billId + "'  \n"
                        + "and (btid_or is not null and btid_or<>'') )btidor\n"
                        + "from (select a.emp_id,EMP_NAME,GPF_NO,DOB,DOS,\n"
                        + "cpf_head,inctax, pt, other_recovery,ddo_recovery,\n"
                        + "a.gpf_type ,a.ACCT_TYPE,g.bt_id,cur_desg, arrear_pay, from_month, from_year,\n"
                        + "to_month, to_year from arr_mast a\n"
                        + "inner join emp_mast b on a.emp_id=b.emp_id\n"
                        + "left outer join g_gpf_type g on a.gpf_type=g.gpf_type\n"
                        + "where bill_no='" + billId + "')btdet");

                BytransferDetails bytran = null;
                if (rs.next()) {
                    bytran = new BytransferDetails();
                    bytran.setHrmsgeneratedRefno(billId);
                    bytran.setHrmsgeneratedRefdate(billdate);
                    bytran.setBtserialno("58816");
                    bytran.setBytransferType("Ag Bt");
                    bytran.setAmount(rs.getInt("inctax"));
                    bytran.setTreasuryCode(treasuryCode);

                    if (rs.getInt("inctax") > 0) {
                        bytranlist.add(bytran);
                    }

                    bytran = new BytransferDetails();
                    bytran.setHrmsgeneratedRefno(billId);
                    bytran.setHrmsgeneratedRefdate(billdate);
                    bytran.setBtserialno("3043");
                    bytran.setBytransferType("Ag Bt");
                    bytran.setAmount(rs.getInt("pt"));
                    bytran.setTreasuryCode(treasuryCode);

                    if (rs.getInt("pt") > 0) {
                        bytranlist.add(bytran);
                    }

                    bytran = new BytransferDetails();
                    bytran.setHrmsgeneratedRefno(billId);
                    bytran.setHrmsgeneratedRefdate(billdate);
                    bytran.setBtserialno(rs.getString("btidor"));
                    bytran.setBytransferType("Ag Bt");
                    bytran.setAmount(rs.getInt("otherrecovery"));
                    bytran.setTreasuryCode(treasuryCode);

                    if (rs.getInt("otherrecovery") > 0) {
                        bytranlist.add(bytran);
                    }

                    /*ddoRecovery = rs.getInt("ddorecovery");
                     if (ddoRecovery > 0) {
                     String btidDr = null;
                     rs2 = st.executeQuery("select distinct btid_dr from arr_mast where ddo_recovery>0 and bill_no=" + billId + "");
                     if (rs2.next()) {
                     btidDr = rs2.getString("btid_dr");
                     }
                     bytran = new BytransferDetails();
                     bytran.setHrmsgeneratedRefno(billId);
                     bytran.setHrmsgeneratedRefdate(billdate);
                     bytran.setBtserialno(btidDr);
                     bytran.setBytransferType("Ag Bt");
                     bytran.setAmount(ddoRecovery);
                     bytran.setTreasuryCode(treasuryCode);

                     bytranlist.add(bytran);
                     }*/
                }
                String gpfType = "";
                ps2 = con.prepareStatement("select gpf_type from arr_mast where bill_no=? limit 1");
                ps2.setInt(1, billId);
                rs2 = ps2.executeQuery();
                if (rs2.next()) {
                    gpfType = rs2.getString("gpf_type");
                }
                DataBaseFunctions.closeSqlObjects(rs);
                rs = st.executeQuery("select sum(cpf_head) cpf_head, ACCT_TYPE from (   "
                        + " select a.emp_id,EMP_NAME,GPF_NO,DOB,DOS,cpf_head,inctax, pt, a.gpf_type ,a.ACCT_TYPE,g.bt_id,cur_desg, arrear_pay, from_month, from_year, to_month, to_year from arr_mast a "
                        + " inner join emp_mast b on a.emp_id=b.emp_id "
                        + " left outer join g_gpf_type g on a.gpf_type=g.gpf_type "
                        + "where bill_no=" + billId + " )btdet group by ACCT_TYPE");

                while (rs.next()) {
                    if (rs.getString("ACCT_TYPE") != null && !rs.getString("ACCT_TYPE").equals("")) {

                        if (rs.getString("ACCT_TYPE").equalsIgnoreCase("GPF")) {

                            bytran = new BytransferDetails();
                            bytran.setHrmsgeneratedRefno(billId);
                            bytran.setHrmsgeneratedRefdate(billdate);
                            if (gpfType != null && !gpfType.equals("")) {
                                if (gpfType.equalsIgnoreCase("ISO")) {
                                    bytran.setBtserialno("57649");
                                } else {
                                    bytran.setBtserialno("55545");
                                }
                            }

                            bytran.setBytransferType("Ag Bt");
                            bytran.setAmount(rs.getInt("cpf_head"));
                            bytran.setTreasuryCode(treasuryCode);

                            if (rs.getInt("cpf_head") > 0) {
                                bytranlist.add(bytran);
                            }
                        } else if (rs.getString("ACCT_TYPE").equalsIgnoreCase("TPF")) {
                            bytran = new BytransferDetails();
                            bytran.setHrmsgeneratedRefno(billId);
                            bytran.setHrmsgeneratedRefdate(billdate);
                            bytran.setBtserialno("55550");
                            bytran.setBytransferType("Ag Bt");
                            bytran.setAmount(rs.getInt("cpf_head"));
                            bytran.setTreasuryCode(treasuryCode);

                            if (rs.getInt("cpf_head") > 0) {
                                bytranlist.add(bytran);
                            }
                        } else if (rs.getString("ACCT_TYPE").equalsIgnoreCase("PRAN")) {
                            bytran = new BytransferDetails();
                            bytran.setHrmsgeneratedRefno(billId);
                            bytran.setHrmsgeneratedRefdate(billdate);
                            bytran.setBtserialno("57740");
                            bytran.setBytransferType("Ag Bt");
                            bytran.setAmount(rs.getInt("cpf_head"));
                            bytran.setTreasuryCode(treasuryCode);

                            if (rs.getInt("cpf_head") > 0) {
                                bytranlist.add(bytran);
                            }
                        }
                    }

                }

            } else if (typeofbill.equals("OA_ARREAR")) {
                rs = st.executeQuery("select sum(inctax) inctax, sum(cpf_head) cpf_head, sum(pt) pt from arr_mast where bill_no=" + billId);

                BytransferDetails bytran = null;
                if (rs.next()) {
                    bytran = new BytransferDetails();
                    bytran.setHrmsgeneratedRefno(billId);
                    bytran.setHrmsgeneratedRefdate(billdate);
                    bytran.setBtserialno("58816");
                    bytran.setBytransferType("Ag Bt");
                    bytran.setAmount(rs.getInt("inctax"));
                    bytran.setTreasuryCode(treasuryCode);

                    bytranlist.add(bytran);

                    bytran = new BytransferDetails();
                    bytran.setHrmsgeneratedRefno(billId);
                    bytran.setHrmsgeneratedRefdate(billdate);
                    bytran.setBtserialno("57740");
                    bytran.setBytransferType("Ag Bt");
                    bytran.setAmount(rs.getInt("cpf_head"));
                    bytran.setTreasuryCode(treasuryCode);

                    bytranlist.add(bytran);

                    bytran = new BytransferDetails();
                    bytran.setHrmsgeneratedRefno(billId);
                    bytran.setHrmsgeneratedRefdate(billdate);
                    bytran.setBtserialno("3043");
                    bytran.setBytransferType("Ag Bt");
                    bytran.setAmount(rs.getInt("pt"));
                    bytran.setTreasuryCode(treasuryCode);

                    bytranlist.add(bytran);

                }

            } else if (typeofbill.equals("LEAVE_ARREAR")) {
                rs = st.executeQuery("select sum(inctax) inctax, sum(cpf_head) cpf_head, sum(pt) pt from arr_mast where bill_no=" + billId);

                BytransferDetails bytran = null;
                if (rs.next()) {
                    bytran = new BytransferDetails();
                    bytran.setHrmsgeneratedRefno(billId);
                    bytran.setHrmsgeneratedRefdate(billdate);
                    bytran.setBtserialno("58816");
                    bytran.setBytransferType("Ag Bt");
                    bytran.setAmount(rs.getInt("inctax"));
                    bytran.setTreasuryCode(treasuryCode);

                    bytranlist.add(bytran);

                    bytran = new BytransferDetails();
                    bytran.setHrmsgeneratedRefno(billId);
                    bytran.setHrmsgeneratedRefdate(billdate);
                    bytran.setBtserialno("57740");
                    bytran.setBytransferType("Ag Bt");
                    bytran.setAmount(rs.getInt("cpf_head"));
                    bytran.setTreasuryCode(treasuryCode);

                    bytranlist.add(bytran);

                    bytran = new BytransferDetails();
                    bytran.setHrmsgeneratedRefno(billId);
                    bytran.setHrmsgeneratedRefdate(billdate);
                    bytran.setBtserialno("3043");
                    bytran.setBytransferType("Ag Bt");
                    bytran.setAmount(rs.getInt("pt"));
                    bytran.setTreasuryCode(treasuryCode);

                    bytranlist.add(bytran);

                }

            } else if (typeofbill.equals("UNUTILISED_LEAVE_ARREAR")) {
                rs = st.executeQuery("select sum(inctax) inctax, sum(cpf_head) cpf_head, sum(pt) pt from arr_mast where bill_no=" + billId);

                BytransferDetails bytran = null;
                if (rs.next()) {
                    bytran = new BytransferDetails();
                    bytran.setHrmsgeneratedRefno(billId);
                    bytran.setHrmsgeneratedRefdate(billdate);
                    bytran.setBtserialno("58816");
                    bytran.setBytransferType("Ag Bt");
                    bytran.setAmount(rs.getInt("inctax"));
                    bytran.setTreasuryCode(treasuryCode);

                    bytranlist.add(bytran);

                    bytran = new BytransferDetails();
                    bytran.setHrmsgeneratedRefno(billId);
                    bytran.setHrmsgeneratedRefdate(billdate);
                    bytran.setBtserialno("57740");
                    bytran.setBytransferType("Ag Bt");
                    bytran.setAmount(rs.getInt("cpf_head"));
                    bytran.setTreasuryCode(treasuryCode);

                    bytranlist.add(bytran);

                    bytran = new BytransferDetails();
                    bytran.setHrmsgeneratedRefno(billId);
                    bytran.setHrmsgeneratedRefdate(billdate);
                    bytran.setBtserialno("3043");
                    bytran.setBytransferType("Ag Bt");
                    bytran.setAmount(rs.getInt("pt"));
                    bytran.setTreasuryCode(treasuryCode);

                    bytranlist.add(bytran);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return bytranlist;
    }

    public String getGPFSeries(String gpfaccno) {
        String gpfseries = "";
        if (gpfaccno != null) {
            gpfseries = gpfaccno.replaceAll("[0-9]", "");
        }
        if (gpfseries.lastIndexOf("-") > 0) {
            gpfseries = gpfseries.substring(0, gpfseries.length() - 1);
        }/*
         if (gpfaccno != null) {
         gpfseries = gpfaccno.replaceAll("[^A-Z]", "");
         }*/

        return gpfseries;
    }

    public String getBTIDforCPF() throws Exception {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String acc = "";
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT BT_ID FROM G_AD_LIST WHERE AD_CODE_NAME='CPF'");
            if (rs.next()) {
                acc = rs.getString("BT_ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return acc;
    }

    @Override
    public ArrayList getGPFXMLData(int billId, String billdate, int monthasNumber, int year, String periodFrom, String periodTo, String typeofbill, int aqMonth, int aqYear) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        ArrayList gpflist = new ArrayList();
        Connection con = null;
        String aqDTLS = "AQ_DTLS";
        try {
            /*
             * GPF data is collected here
             * */
            con = dataSource.getConnection();
            st = con.createStatement();
            if (typeofbill.equalsIgnoreCase("PAY")) {

                aqDTLS = AqFunctionalities.getAQBillDtlsTable(aqMonth, aqYear);
                rs = st.executeQuery("SELECT EMP_ID,CUR_DESG,EMP_NAME,GPF_NO,DOB,DOS,REF_DESC,AD_AMT,DED_TYPE,AQ_MAST.GPF_TYPE,AD_CODE,AQ_DTLS.BT_ID FROM ( "
                        + "SELECT EMP_CODE,AQSL_NO,CUR_DESG,EMP_NAME,GPF_TYPE FROM AQ_MAST WHERE ACCT_TYPE='GPF'  AND BILL_NO=" + billId + " and aq_month=" + aqMonth + " and aq_year=" + aqYear + " ) AQ_MAST "
                        + "INNER JOIN (SELECT * FROM " + aqDTLS + " AQ_DTLS WHERE (AD_CODE='GPF' or AD_CODE='GA' OR AD_CODE='GPDD' OR AD_CODE='GPIR' ) AND AQ_MONTH=" + (monthasNumber - 1) + " AND AQ_YEAR=" + year + ") AQ_DTLS ON AQ_MAST.AQSL_NO=AQ_DTLS.AQSL_NO "
                        + "INNER JOIN EMP_MAST ON AQ_MAST.EMP_CODE=EMP_MAST.EMP_ID LEFT OUTER JOIN G_GPF_TYPE ON AQ_MAST.GPF_TYPE = G_GPF_TYPE.GPF_TYPE ORDER BY EMP_ID,DED_TYPE");

                GpfTpfDetails gpf = null;

                String empId = "";
                double tot = 0;
                double subscription = 0.0;
                double gpfadv = 0.0;
                double gpfother = 0.0;

                boolean gpfbeanAlreadyAdded = true;
                int firstTime = 0;
                while (rs.next()) {
                    if (empId.equals(rs.getString("EMP_ID"))) {
                        if (rs.getString("AD_CODE").equals("GPF")) {
                            tot = tot + rs.getDouble("AD_AMT");

                            subscription = subscription + rs.getDouble("AD_AMT");
                            gpf.setMonthlySubscrip(subscription + "");
                        } else if (rs.getString("AD_CODE").equals("GA")) {
                            tot = tot + rs.getDouble("AD_AMT");
                            gpfadv = gpfadv + rs.getDouble("AD_AMT");
                            if (rs.getDouble("AD_AMT") > 0.0) {
                                gpf.setInstNumber(rs.getString("REF_DESC"));
                            }
                            gpf.setRefundWithdrawl(gpfadv + "");
                        } else if (rs.getString("AD_CODE").equals("GPDD") || rs.getString("AD_CODE").equals("GPIR")) {
                            tot = tot + rs.getDouble("AD_AMT");
                            gpf.setOtherDeposit(gpf.getOtherDeposit() + rs.getDouble("AD_AMT"));
                        }
                        gpf.setTotRealised(tot);
                    } else if (!empId.equals(rs.getString("EMP_ID"))) {
                        if (firstTime == 0) {
                            firstTime = 1;
                            gpf = new GpfTpfDetails();
                            tot = 0;
                            subscription = 0;
                            gpfadv = 0.0;
                            gpfother = 0.0;
                        } else {
                            if (gpf.getTotRealised() > 0) {

                                gpflist.add(gpf);
                            }
                            gpfbeanAlreadyAdded = true;
                            gpf = new GpfTpfDetails();
                            tot = 0;
                            subscription = 0;
                            gpfadv = 0.0;
                            gpfother = 0.0;
                            gpfbeanAlreadyAdded = false;
                        }

                        gpfbeanAlreadyAdded = false;
                        String btidofgpf = rs.getString("BT_ID");
                        empId = rs.getString("EMP_ID");
                        gpf.setHrmsgeneratedRefno(billId + "");
                        gpf.setHrmsgeneratedRefdate(billdate);
                        gpf.setGpfnumber(StringUtils.trim(getNumberFromGPF(rs.getString("GPF_NO"))));
                        gpf.setGpfSeries(StringUtils.trim(rs.getString("GPF_TYPE")));
                        gpf.setSubscribName(rs.getString("EMP_NAME"));
                        gpf.setDesig(rs.getString("CUR_DESG"));

                        gpf.setDob(CommonFunctions.getFormattedOutputDate3(rs.getDate("DOB")));
                        gpf.setDos(CommonFunctions.getFormattedOutputDate3(rs.getDate("DOS")));
                        gpf.setBtserialno(btidofgpf);
                        gpf.setPeriodFrom(periodFrom);
                        gpf.setPeriodTo(periodTo);
                        if (rs.getString("AD_CODE").equals("GPF")) {
                            tot = tot + rs.getDouble("AD_AMT");
                            subscription = subscription + rs.getDouble("AD_AMT");
                            gpf.setMonthlySubscrip(subscription + "");
                        } else if (rs.getString("AD_CODE").equals("GA")) {
                            tot = tot + rs.getDouble("AD_AMT");
                            gpfadv = gpfadv + rs.getDouble("AD_AMT");
                            if (rs.getDouble("AD_AMT") > 0.0) {
                                gpf.setInstNumber(rs.getString("REF_DESC"));
                            }
                            gpf.setRefundWithdrawl(gpfadv + "");
                        } else if (rs.getString("AD_CODE").equals("GPDD") || rs.getString("AD_CODE").equals("GPIR")) {
                            tot = tot + rs.getDouble("AD_AMT");
                            gpf.setOtherDeposit(gpf.getOtherDeposit() + rs.getDouble("AD_AMT"));
                        }
                        gpf.setTotRealised(tot);
                    }
                }
                if (gpfbeanAlreadyAdded == false && gpf.getTotRealised() > 0) {

                    gpflist.add(gpf);
                }
            } else if (typeofbill.equalsIgnoreCase("OTHER_ARREAR")) {
                rs = st.executeQuery("select a.emp_id,EMP_NAME,GPF_NO,DOB,DOS,cpf_head,a.gpf_type ,g.bt_id,cur_desg, arrear_pay, from_month, from_year, to_month, to_year from arr_mast a, emp_mast b, g_gpf_type g "
                        + " where a.emp_id=b.emp_id and a.gpf_type=g.gpf_type and bill_no=" + billId + " and cpf_head>0 AND a.ACCT_TYPE = 'GPF'");

                SimpleDateFormat date_format = new SimpleDateFormat("dd-MMM-yyyy");
                while (rs.next()) {
                    GpfTpfDetails gpf = new GpfTpfDetails();

                    Calendar cal2 = null;
                    cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("FROM_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("FROM_YEAR"));
                    cal2.set(Calendar.DATE, 1);
                    periodFrom = date_format.format(cal2.getTime());

                    cal2 = Calendar.getInstance();
                    cal2.set(Calendar.MONTH, (rs.getInt("TO_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("TO_YEAR"));
                    cal2.set(Calendar.DATE, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));

                    periodTo = date_format.format(cal2.getTime());

                    gpf.setHrmsgeneratedRefno(billId + "");
                    gpf.setHrmsgeneratedRefdate(billdate);
                    gpf.setBtserialno(rs.getString("bt_id"));
                    if (rs.getString("GPF_NO") == null || rs.getString("GPF_NO").equals("")) {
                        gpf.setGpfSeries(rs.getString("GPF_NO"));
                    } else {
                        gpf.setGpfSeries(rs.getString("GPF_NO").replaceAll("[^A-Z ]", ""));
                    }

                    gpf.setGpfnumber(StringUtils.trim(getNumberFromGPF(rs.getString("GPF_NO"))));
                    gpf.setSubscribName(rs.getString("EMP_NAME"));
                    if (rs.getString("cur_desg") != null && !rs.getString("cur_desg").equals("")) {
                        gpf.setDesig(rs.getString("cur_desg"));
                    } else {
                        gpf.setDesig("NA");
                    }

                    gpf.setMonthlySubscrip(rs.getString("cpf_head"));

                    gpf.setDob(CommonFunctions.getFormattedOutputDate3(rs.getDate("DOB")));
                    gpf.setDos(CommonFunctions.getFormattedOutputDate3(rs.getDate("DOS")));
                    gpf.setPeriodFrom(periodFrom);
                    gpf.setPeriodTo(periodTo);

                    gpf.setTotRealised(rs.getInt("cpf_head"));

                    if (gpf.getTotRealised() > 0) {

                        gpflist.add(gpf);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gpflist;
    }

    @Override
    public ArrayList getTPFXMLData(int billId, String billdate, int monthasNumber, int year, String periodFrom, String periodTo, String typeofbill, int aqMonth, int aqYear) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        ArrayList gpflist = new ArrayList();
        Connection con = null;
        String aqDTLS = "AQ_DTLS";
        try {
            /*
             * GPF data is collected here
             * */

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(aqMonth, aqYear);
            con = dataSource.getConnection();
            st = con.createStatement();
            if (typeofbill.equalsIgnoreCase("PAY")) {
                rs = st.executeQuery("SELECT EMP_ID,CUR_DESG,EMP_NAME,GPF_NO,DOB,DOS,REF_DESC,AD_AMT,DED_TYPE,AQ_MAST.GPF_TYPE,AD_CODE,AQ_DTLS.BT_ID FROM ( "
                        + "SELECT EMP_CODE,AQSL_NO,CUR_DESG,EMP_NAME,GPF_TYPE FROM AQ_MAST WHERE ACCT_TYPE='TPF' AND BILL_NO=" + billId + " and aq_month=" + aqMonth + " and aq_year=" + aqYear + " ) AQ_MAST "
                        + "INNER JOIN (SELECT * FROM " + aqDTLS + " AQ_DTLS WHERE ( AD_CODE='TPF' or AD_CODE='TPFGA' OR AD_CODE='GPDD' OR AD_CODE='GPIR' ) AND AQ_MONTH=" + (monthasNumber - 1) + " AND AQ_YEAR=" + year + ") AQ_DTLS ON AQ_MAST.AQSL_NO=AQ_DTLS.AQSL_NO "
                        + "INNER JOIN EMP_MAST ON AQ_MAST.EMP_CODE=EMP_MAST.EMP_ID LEFT OUTER JOIN G_GPF_TYPE ON AQ_MAST.GPF_TYPE = G_GPF_TYPE.GPF_TYPE ORDER BY EMP_ID,DED_TYPE");

                GpfTpfDetails gpf = null;

                String empId = "";
                double tot = 0;
                double subscription = 0.0;
                double gpfadv = 0.0;
                double gpfother = 0.0;

                boolean gpfbeanAlreadyAdded = true;
                int firstTime = 0;
                while (rs.next()) {
                    if (empId.equals(rs.getString("EMP_ID"))) {
                        if (rs.getString("AD_CODE").equals("GPDD") || rs.getString("AD_CODE").equals("GPIR")) {
                            tot = tot + rs.getDouble("AD_AMT");
                            gpf.setOtherDeposit(gpf.getOtherDeposit() + rs.getDouble("AD_AMT"));
                        } else if (rs.getString("AD_CODE").equals("TPF")) {
                            tot = tot + rs.getDouble("AD_AMT");
                            subscription = subscription + rs.getDouble("AD_AMT");
                            gpf.setMonthlySubscrip(subscription + "");
                        } else if (rs.getString("AD_CODE").equals("TPFGA")) {
                            tot = tot + rs.getDouble("AD_AMT");
                            gpfadv = gpfadv + rs.getDouble("AD_AMT");
                            if (rs.getDouble("AD_AMT") > 0.0) {
                                gpf.setInstNumber(rs.getString("REF_DESC"));
                            }
                            gpf.setRefundWithdrawl(gpfadv + "");
                        }
                        gpf.setTotRealised(tot);
                    } else if (!empId.equals(rs.getString("EMP_ID"))) {
                        if (firstTime == 0) {
                            firstTime = 1;
                            gpf = new GpfTpfDetails();
                            tot = 0;
                            subscription = 0;
                            gpfadv = 0.0;
                            gpfother = 0.0;
                        } else {
                            if (gpf.getTotRealised() > 0) {
                                gpflist.add(gpf);
                            }
                            gpfbeanAlreadyAdded = true;
                            gpf = new GpfTpfDetails();
                            tot = 0;
                            subscription = 0;
                            gpfadv = 0.0;
                            gpfother = 0.0;
                            gpfbeanAlreadyAdded = false;
                        }

                        gpfbeanAlreadyAdded = false;
                        String btidofgpf = rs.getString("BT_ID");
                        empId = rs.getString("EMP_ID");
                        gpf.setHrmsgeneratedRefno(billId + "");
                        gpf.setHrmsgeneratedRefdate(billdate);
                        gpf.setGpfnumber(StringUtils.trim(getNumberFromGPF(rs.getString("GPF_NO"))));
                        gpf.setGpfSeries(StringUtils.trim(rs.getString("GPF_TYPE")));
                        gpf.setSubscribName(rs.getString("EMP_NAME"));
                        gpf.setDesig(rs.getString("CUR_DESG"));
                        gpf.setDob(CommonFunctions.getFormattedOutputDate3(rs.getDate("DOB")));
                        gpf.setDos(CommonFunctions.getFormattedOutputDate3(rs.getDate("DOS")));
                        gpf.setBtserialno(btidofgpf);
                        gpf.setPeriodFrom(periodFrom);
                        gpf.setPeriodTo(periodTo);

                        if (rs.getString("AD_CODE").equals("GPDD") || rs.getString("AD_CODE").equals("GPIR")) {
                            tot = tot + rs.getDouble("AD_AMT");
                            gpf.setOtherDeposit(gpf.getOtherDeposit() + rs.getDouble("AD_AMT"));
                        } else if (rs.getString("AD_CODE").equals("TPF")) {
                            tot = tot + rs.getDouble("AD_AMT");
                            subscription = subscription + rs.getDouble("AD_AMT");
                            gpf.setMonthlySubscrip(subscription + "");
                        } else if (rs.getString("AD_CODE").equals("TPFGA")) {
                            tot = tot + rs.getDouble("AD_AMT");
                            gpfadv = gpfadv + rs.getDouble("AD_AMT");
                            if (rs.getDouble("AD_AMT") > 0.0) {
                                gpf.setInstNumber(rs.getString("REF_DESC"));
                            }
                            gpf.setRefundWithdrawl(gpfadv + "");
                        }
                        gpf.setTotRealised(tot);
                    }
                }
                if (gpfbeanAlreadyAdded == false && gpf.getTotRealised() > 0) {
                    gpflist.add(gpf);
                }

            } else if (typeofbill.equalsIgnoreCase("OTHER_ARREAR")) {
                rs = st.executeQuery("select a.emp_id,EMP_NAME,GPF_NO,DOB,DOS,cpf_head,a.gpf_type ,g.bt_id,cur_desg, arrear_pay, from_month, from_year, to_month, to_year from arr_mast a, emp_mast b, g_gpf_type g "
                        + " where a.emp_id=b.emp_id and a.gpf_type=g.gpf_type and bill_no=" + billId + " and cpf_head>0 AND a.ACCT_TYPE = 'TPF'");

                SimpleDateFormat date_format = new SimpleDateFormat("dd-MMM-yyyy");
                while (rs.next()) {
                    GpfTpfDetails gpf = new GpfTpfDetails();

                    Calendar cal2 = null;
                    cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("FROM_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("FROM_YEAR"));
                    cal2.set(Calendar.DATE, 1);
                    periodFrom = date_format.format(cal2.getTime());

                    cal2 = Calendar.getInstance();
                    cal2.set(Calendar.MONTH, (rs.getInt("TO_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("TO_YEAR"));
                    cal2.set(Calendar.DATE, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));

                    periodTo = date_format.format(cal2.getTime());

                    gpf.setHrmsgeneratedRefno(billId + "");
                    gpf.setHrmsgeneratedRefdate(billdate);
                    gpf.setBtserialno(rs.getString("bt_id"));
                    if (rs.getString("GPF_NO") == null || rs.getString("GPF_NO").equals("")) {
                        gpf.setGpfSeries(rs.getString("GPF_NO"));
                    } else {
                        gpf.setGpfSeries(rs.getString("GPF_NO").replaceAll("[^A-Z ]", ""));
                    }

                    gpf.setGpfnumber(StringUtils.trim(getNumberFromGPF(rs.getString("GPF_NO"))));
                    gpf.setSubscribName(rs.getString("EMP_NAME"));
                    if (rs.getString("cur_desg") != null && !rs.getString("cur_desg").equals("")) {
                        gpf.setDesig(rs.getString("cur_desg"));
                    } else {
                        gpf.setDesig("NA");
                    }
                    gpf.setMonthlySubscrip(rs.getString("cpf_head"));

                    gpf.setDob(CommonFunctions.getFormattedOutputDate3(rs.getDate("DOB")));
                    gpf.setDos(CommonFunctions.getFormattedOutputDate3(rs.getDate("DOS")));
                    gpf.setPeriodFrom(periodFrom);
                    gpf.setPeriodTo(periodTo);

                    gpf.setTotRealised(rs.getInt("cpf_head"));

                    if (gpf.getTotRealised() > 0) {

                        gpflist.add(gpf);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gpflist;
    }

    public static String getNumberFromGPF(String gpfno) {
        String numgpfno = "";
        for (int i = 0; i < gpfno.length(); i++) {
            if (isNumeric(gpfno.substring(i, i + 1)) == true) {
                numgpfno = numgpfno + gpfno.substring(i, i + 1);
            }
        }
        return numgpfno;
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public int getcountNoofTimesBillPreparedForNps(int billMonth, int billYear, String empId, int billId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int ctr = 1;

        try {

            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select token_date from aq_mast "
                    + " left outer join bill_mast on aq_mast.bill_no=bill_mast.bill_no "
                    + " where ACCT_TYPE='PRAN' AND aq_mast.aq_month=? and aq_mast.aq_year=?  "
                    + " and aq_day>pay_day and emp_code=? and aq_mast.bill_no <>?");

            ps.setInt(1, billMonth);
            ps.setInt(2, billYear);
            ps.setString(3, empId);
            ps.setInt(4, billId);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("token_date") != null && !rs.getString("token_date").equals("")) {
                    ctr++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();;
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return ctr;
    }

    @Override
    public ArrayList getNPSXMLData(int billId, String billdate, int monthasNumber, int year, String typeofbill, int aqMonth, int aqYear) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String empId = "";
        ArrayList npslist = new ArrayList();
        int tot = 0;
        int firstTime = 0;
        //VouchingServices vserv= new VouchingServices();    
        String aqDTLS = "AQ_DTLS";
        NPSDetails nps = null;

        PreparedStatement pst = null;
        try {

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(aqMonth, aqYear);
            String query = "SELECT DDO_REG_NO FROM G_OFFICE INNER JOIN (SELECT OFF_CODE FROM BILL_MAST WHERE BILL_NO=" + billId + ")BILL_MAST ON G_OFFICE.OFF_CODE=BILL_MAST.OFF_CODE";
            con = this.dataSource.getConnection();
            st = con.createStatement();
            String ddoregno = "";
            rs = st.executeQuery(query);
            if (rs.next()) {
                ddoregno = rs.getString("DDO_REG_NO");
            }

            st = con.createStatement();
            String btidforcpf = getBTIDforCPF();
            System.out.println("billId:" + billId + ";" + aqMonth + ":" + aqYear + "::" + monthasNumber + ":::" + year);
            if (typeofbill.equalsIgnoreCase("PAY")) {

                rs = st.executeQuery("SELECT emp_mast.stop_pay_nps,emp_type,emp_mast.dos,emp_mast.if_gpf_assumed,AQ_DTLS.TOT_REC_AMT,EMP_ID, CUR_BASIC, EMP_NAME, GPF_NO, DOB,DOS, REF_DESC, AD_AMT, DED_TYPE, AD_CODE , aq_day, pay_day FROM ( "
                        + "SELECT EMP_CODE, AQSL_NO, EMP_NAME, CUR_BASIC, aq_day,emp_type, pay_day FROM AQ_MAST WHERE ACCT_TYPE='PRAN' AND BILL_NO=" + billId + " and aq_month=" + aqMonth + " and aq_year=" + aqYear + " ) AQ_MAST "
                        + "INNER JOIN (SELECT * FROM " + aqDTLS + " AQ_DTLS WHERE (AD_CODE='CPF' OR AD_CODE='NPSL' OR AD_CODE='DA' OR AD_CODE='GP' OR AD_CODE='PPAY') AND "
                        + "AQ_MONTH=" + (monthasNumber - 1) + " AND AQ_YEAR=" + year + " AND AD_AMT>0) AQ_DTLS ON AQ_MAST.AQSL_NO=AQ_DTLS.AQSL_NO "
                        + "INNER JOIN EMP_MAST ON AQ_MAST.EMP_CODE=EMP_MAST.EMP_ID ORDER BY EMP_ID,DED_TYPE");

                empId = "";
                tot = 0;
                int ctr = 1;
                boolean npsbeanAlreadyAdded = true;
                firstTime = 0;
                while (rs.next()) {
                    //nps = new NPSDetails();
                    //nps.setNpsEmpid(rs.getString("EMP_ID"));
                    //System.out.println("NPSEMPID:" + nps.getNpsEmpid());

                    if (empId.equals(rs.getString("EMP_ID"))) {
                        String ded_type = rs.getString("DED_TYPE");
                        if (rs.getString("AD_CODE").equals("CPF")) {
                            if (nps.getSc() != null && !nps.getSc().equals("")) {
                                nps.setSc(Integer.parseInt(nps.getSc()) + rs.getInt("AD_AMT") + "");
                            } else {
                                nps.setSc(rs.getString("AD_AMT"));
                            }
                            if ((aqMonth >= 9 && aqYear == 2021) || aqYear > 2021) {
                                if (nps.getGc() != null && !nps.getGc().equals("")) {
                                    nps.setGc(Integer.parseInt(nps.getGc()) + rs.getInt("TOT_REC_AMT") + "");
                                } else {
                                    nps.setGc(rs.getString("TOT_REC_AMT"));
                                }
                            } else {
                                if (nps.getGc() != null && !nps.getGc().equals("")) {
                                    nps.setGc(Integer.parseInt(nps.getGc()) + rs.getInt("AD_AMT") + "");
                                } else {
                                    nps.setGc(rs.getString("AD_AMT"));
                                }
                            }
                        } else if (rs.getString("AD_CODE").equals("DA")) {
                            nps.setDa(rs.getDouble("AD_AMT"));
                        } else if (rs.getString("AD_CODE").equals("GP")) {
                            nps.setGp(rs.getDouble("AD_AMT"));
                        } else if (rs.getString("AD_CODE").equals("PPAY")) {
                            nps.setPpay(rs.getDouble("AD_AMT"));
                        }
                        if (ded_type != null && !ded_type.equals("") && ded_type.equals("L")) {
                            nps.setInstAmt(rs.getDouble("AD_AMT"));
                        }
                        nps.setIfGPFAssumed(rs.getString("if_gpf_assumed"));
                        nps.setDosDate(rs.getDate("DOS"));
                        nps.setNpsEmpid(rs.getString("EMP_ID"));
                    } else if (!empId.equals(rs.getString("EMP_ID"))) {
                        if (firstTime == 0) {
                            firstTime = 1;
                            nps = new NPSDetails();
                            npsbeanAlreadyAdded = false;
                        } else {
                            if (nps.getGc() != null && !nps.getGc().equals("")) {
                                if (Integer.parseInt(nps.getGc()) > 0 || nps.getInstAmt() > 0) {
                                    npslist.add(nps);
                                }
                            }
                            npsbeanAlreadyAdded = true;
                            nps = new NPSDetails();
                            tot = 0;
                            npsbeanAlreadyAdded = false;
                        }

                        empId = rs.getString("EMP_ID");
                        nps.setNpsEmpid(empId);

                        if (rs.getInt("aq_day") > rs.getInt("pay_day")) {
                            ctr = getcountNoofTimesBillPreparedForNps((monthasNumber - 1), year, empId, billId);
                        }

                        if (ctr == 1) {
                            nps.setContType("R");
                        } else if (ctr == 2) {
                            nps.setContType("A");
                        }

                        nps.setHrmsgeneratedRefno(billId);
                        nps.setHrmsgeneratedRefdate(billdate);
                        nps.setPran(StringUtils.trim(rs.getString("GPF_NO")));
                        nps.setNameofSubscrib(rs.getString("EMP_NAME"));
                        nps.setBasic(rs.getDouble("CUR_BASIC"));
                        nps.setBtserialno(btidforcpf);
                        nps.setPaymonth(monthasNumber + "");
                        nps.setPayYear(year + "");
                        nps.setDdoRegno(ddoregno);
                        if (rs.getString("AD_CODE").equals("CPF")) {
                            nps.setSc(rs.getString("AD_AMT"));
                            if ((aqMonth >= 9 && aqYear == 2021) || aqYear > 2021) {
                                nps.setGc(rs.getString("TOT_REC_AMT"));
                            } else {
                                nps.setGc(rs.getString("AD_AMT"));
                            }
                        } else if (rs.getString("AD_CODE").equals("DA")) {
                            nps.setDa(rs.getDouble("AD_AMT"));
                        } else if (rs.getString("AD_CODE").equals("PPAY")) {
                            nps.setPpay(rs.getDouble("AD_AMT"));
                        } else if (rs.getString("AD_CODE").equals("GP")) {
                            nps.setGp(rs.getDouble("AD_AMT"));
                        } else if (rs.getString("DED_TYPE") != null && !rs.getString("DED_TYPE").equals("") && rs.getString("DED_TYPE").equals("L")) {
                            nps.setInstAmt(rs.getDouble("AD_AMT"));
                        }
                        nps.setIfGPFAssumed(rs.getString("if_gpf_assumed"));
                        nps.setIfStopPayNps(rs.getString("stop_pay_nps"));
                        nps.setUserType(rs.getString("emp_type"));
                        nps.setDosDate(rs.getDate("DOS"));

                    }
                }
                if (nps != null && nps.getGc() != null && !nps.getGc().equals("")) {
                    if (npsbeanAlreadyAdded == false && (Integer.parseInt(nps.getGc()) > 0 || nps.getInstAmt() > 0)) {
                        if (nps.getIfGPFAssumed() != null && nps.getIfGPFAssumed().equals("N")) {
                            npslist.add(nps);
                        }
                    }
                }

            } else if (typeofbill.equalsIgnoreCase("ARREAR")) {

                rs = st.executeQuery("select a.emp_id,EMP_NAME,GPF_NO,DOB,DOS,cpf_head, "
                        + "arrear_pay from arr_mast a, emp_mast b where a.emp_id=b.emp_id and bill_no=" + billId + " and cpf_head>0 AND a.ACCT_TYPE = 'PRAN'");
                while (rs.next()) {
                    nps = new NPSDetails();
                    nps.setContType("A");
                    empId = rs.getString("emp_id");
                    nps.setHrmsgeneratedRefno(billId);
                    nps.setHrmsgeneratedRefdate(billdate);
                    nps.setPran(StringUtils.trim(rs.getString("GPF_NO")));
                    nps.setNameofSubscrib(rs.getString("EMP_NAME"));
                    nps.setBasic(rs.getInt("arrear_pay"));
                    nps.setBtserialno(btidforcpf);
                    nps.setPaymonth(monthasNumber + "");
                    nps.setPayYear(year + "");
                    nps.setDdoRegno(ddoregno);
                    nps.setSc(rs.getString("cpf_head"));
                    nps.setGc(rs.getString("cpf_head"));

                    npslist.add(nps);
                }
            } else if (typeofbill.equalsIgnoreCase("ARREAR_6")) {

                rs = st.executeQuery("select a.emp_id,EMP_NAME,GPF_NO,DOB,DOS,cpf_head, "
                        + "arrear_pay from arr_mast a, emp_mast b where a.emp_id=b.emp_id and bill_no=" + billId + " and cpf_head>0 AND a.ACCT_TYPE = 'PRAN'");
                while (rs.next()) {
                    nps = new NPSDetails();
                    nps.setContType("A");
                    empId = rs.getString("emp_id");
                    nps.setHrmsgeneratedRefno(billId);
                    nps.setHrmsgeneratedRefdate(billdate);
                    nps.setPran(StringUtils.trim(rs.getString("GPF_NO")));
                    nps.setNameofSubscrib(rs.getString("EMP_NAME"));
                    nps.setBasic(rs.getInt("arrear_pay"));
                    nps.setBtserialno(btidforcpf);
                    nps.setPaymonth(monthasNumber + "");
                    nps.setPayYear(year + "");
                    nps.setDdoRegno(ddoregno);
                    nps.setSc(rs.getString("cpf_head"));
                    nps.setGc(rs.getString("cpf_head"));

                    npslist.add(nps);
                }
            } else if (typeofbill.equalsIgnoreCase("ARREAR_NPS")) {

                rs = st.executeQuery("select a.emp_id,EMP_NAME,GPF_NO,DOB,DOS,cpf_head, "
                        + "arrear_pay from arr_mast a, emp_mast b where a.emp_id=b.emp_id and bill_no=" + billId + " and cpf_head>0 AND a.ACCT_TYPE = 'PRAN'");
                while (rs.next()) {
                    nps = new NPSDetails();
                    nps.setContType("A");
                    empId = rs.getString("emp_id");
                    nps.setHrmsgeneratedRefno(billId);
                    nps.setHrmsgeneratedRefdate(billdate);
                    nps.setPran(StringUtils.trim(rs.getString("GPF_NO")));
                    nps.setNameofSubscrib(rs.getString("EMP_NAME"));
                    nps.setBasic(rs.getInt("arrear_pay"));
                    nps.setBtserialno(btidforcpf);
                    nps.setPaymonth(monthasNumber + "");
                    nps.setPayYear(year + "");
                    nps.setDdoRegno(ddoregno);
                    nps.setSc(rs.getString("cpf_head"));
                    nps.setGc(rs.getString("cpf_head"));

                    npslist.add(nps);
                }
            } else if (typeofbill.equalsIgnoreCase("OTHER_ARREAR")) {
                int frommonth = 0;
                int fromyear = 0;
                pst = con.prepareStatement("SELECT from_month,from_year FROM BILL_MAST WHERE BILL_NO=?");
                pst.setInt(1, billId);
                rs = pst.executeQuery();
                if (rs.next()) {
                    frommonth = rs.getInt("from_month");
                    fromyear = rs.getInt("from_year");
                }

                DataBaseFunctions.closeSqlObjects(rs, pst);

                rs = st.executeQuery("select aqsl_no,a.emp_id,EMP_NAME,GPF_NO,DOB,DOS,cpf_head,cpf_head_gc, "
                        + " arrear_pay from arr_mast a, emp_mast b where a.emp_id=b.emp_id and bill_no=" + billId + " and cpf_head>0 AND cpf_head_gc >= 0 AND a.ACCT_TYPE = 'PRAN' and b.if_gpf_assumed='N'");
                while (rs.next()) {
                    nps = new NPSDetails();
                    nps.setContType("A");
                    empId = rs.getString("emp_id");
                    nps.setHrmsgeneratedRefno(billId);
                    nps.setHrmsgeneratedRefdate(billdate);
                    nps.setPran(StringUtils.trim(rs.getString("GPF_NO")));
                    nps.setNameofSubscrib(rs.getString("EMP_NAME"));

                    int basicGp = 0;
                    int daAmt = 0;
                    List li = getAllowanceListForArrearAQSLNOWISE(rs.getString("aqsl_no"));
                    for (int i = 0; i < li.size(); i++) {
                        AllowDeductDetails ad = (AllowDeductDetails) li.get(i);
                        if (ad.getAdname().equalsIgnoreCase("PAY") || ad.getAdname().equalsIgnoreCase("GP")) {
                            if (ad.getAdamount() != null && !ad.getAdamount().equals("")) {
                                basicGp = basicGp + Integer.parseInt(ad.getAdamount());
                            }
                        } else if (ad.getAdname().equalsIgnoreCase("DA")) {
                            if (ad.getAdamount() != null && !ad.getAdamount().equals("")) {
                                daAmt = daAmt + Integer.parseInt(ad.getAdamount());
                            }
                        }
                    }

                    nps.setBasic(basicGp);
                    nps.setBtserialno(btidforcpf);
                    nps.setPaymonth(monthasNumber + "");
                    nps.setPayYear(year + "");
                    nps.setDdoRegno(ddoregno);
                    nps.setSc(rs.getString("cpf_head"));
                    nps.setDa(daAmt);
                    if ((frommonth >= 4 && fromyear == 2019) || fromyear >= 2020) {
                        if (rs.getString("cpf_head_gc") != null && !rs.getString("cpf_head_gc").equals("")) {
                            nps.setGc(rs.getString("cpf_head_gc"));
                        } else {
                            nps.setGc("0");
                        }
                    } else {
                        nps.setGc(rs.getString("cpf_head"));
                    }

                    npslist.add(nps);
                }
            } else if (typeofbill.equalsIgnoreCase("ARREAR_J") || typeofbill.equalsIgnoreCase("ARREAR_6_J")) {
                int frommonth = 0;
                int fromyear = 0;
                pst = con.prepareStatement("SELECT from_month,from_year FROM BILL_MAST WHERE BILL_NO=?");
                pst.setInt(1, billId);
                rs = pst.executeQuery();
                if (rs.next()) {
                    frommonth = rs.getInt("from_month");
                    fromyear = rs.getInt("from_year");
                }

                DataBaseFunctions.closeSqlObjects(rs, pst);

                rs = st.executeQuery("select aqsl_no,a.emp_id,EMP_NAME,GPF_NO,DOB,DOS,cpf_head,cpf_head_gc, "
                        + " arrear_pay from arr_mast a, emp_mast b where a.emp_id=b.emp_id and bill_no=" + billId + " and cpf_head>0 AND cpf_head_gc >= 0 AND a.ACCT_TYPE = 'PRAN'");
                while (rs.next()) {
                    nps = new NPSDetails();
                    nps.setContType("A");
                    empId = rs.getString("emp_id");
                    nps.setHrmsgeneratedRefno(billId);
                    nps.setHrmsgeneratedRefdate(billdate);
                    nps.setPran(StringUtils.trim(rs.getString("GPF_NO")));
                    nps.setNameofSubscrib(rs.getString("EMP_NAME"));

                    nps.setBasic(rs.getInt("arrear_pay"));
                    nps.setBtserialno(btidforcpf);
                    nps.setPaymonth(monthasNumber + "");
                    nps.setPayYear(year + "");
                    nps.setDdoRegno(ddoregno);
                    nps.setSc(rs.getString("cpf_head"));
                    //nps.setDa(daAmt);
                    if ((frommonth >= 4 && fromyear == 2019) || fromyear >= 2020) {
                        if (rs.getString("cpf_head_gc") != null && !rs.getString("cpf_head_gc").equals("")) {
                            nps.setGc(rs.getString("cpf_head_gc"));
                        } else {
                            nps.setGc("0");
                        }
                    } else {
                        nps.setGc(rs.getString("cpf_head"));
                    }

                    npslist.add(nps);
                }
            } else if (typeofbill.equalsIgnoreCase("OA_ARREAR")) {

                rs = st.executeQuery("select a.emp_id,EMP_NAME,GPF_NO,DOB,DOS,cpf_head, "
                        + "arrear_pay from arr_mast a, emp_mast b where a.emp_id=b.emp_id and bill_no=" + billId + " and cpf_head>0 AND a.ACCT_TYPE = 'PRAN'");
                while (rs.next()) {
                    nps = new NPSDetails();
                    nps.setContType("A");
                    empId = rs.getString("emp_id");
                    nps.setHrmsgeneratedRefno(billId);
                    nps.setHrmsgeneratedRefdate(billdate);
                    nps.setPran(StringUtils.trim(rs.getString("GPF_NO")));
                    nps.setNameofSubscrib(rs.getString("EMP_NAME"));
                    nps.setBasic(rs.getInt("arrear_pay"));
                    nps.setBtserialno(btidforcpf);
                    nps.setPaymonth(monthasNumber + "");
                    nps.setPayYear(year + "");
                    nps.setDdoRegno(ddoregno);
                    nps.setSc(rs.getString("cpf_head"));
                    nps.setGc(rs.getString("cpf_head"));

                    npslist.add(nps);
                }
            } else if (typeofbill.equalsIgnoreCase("LEAVE_ARREAR")) {

                rs = st.executeQuery("select a.emp_id,EMP_NAME,GPF_NO,DOB,DOS,cpf_head, "
                        + "arrear_pay from arr_mast a, emp_mast b where a.emp_id=b.emp_id and bill_no=" + billId + " and cpf_head>0 AND a.ACCT_TYPE = 'PRAN'");
                while (rs.next()) {
                    nps = new NPSDetails();
                    nps.setContType("A");
                    empId = rs.getString("emp_id");
                    nps.setHrmsgeneratedRefno(billId);
                    nps.setHrmsgeneratedRefdate(billdate);
                    nps.setPran(StringUtils.trim(rs.getString("GPF_NO")));
                    nps.setNameofSubscrib(rs.getString("EMP_NAME"));
                    nps.setBasic(rs.getInt("arrear_pay"));
                    nps.setBtserialno(btidforcpf);
                    nps.setPaymonth(monthasNumber + "");
                    nps.setPayYear(year + "");
                    nps.setDdoRegno(ddoregno);
                    nps.setSc(rs.getString("cpf_head"));
                    nps.setGc(rs.getString("cpf_head"));

                    npslist.add(nps);
                }
            } else if (typeofbill.equalsIgnoreCase("UNUTILISED_LEAVE_ARREAR")) {

                rs = st.executeQuery("select a.emp_id,EMP_NAME,GPF_NO,DOB,DOS,cpf_head, "
                        + "arrear_pay from arr_mast a, emp_mast b where a.emp_id=b.emp_id and bill_no=" + billId + " and cpf_head>0 AND a.ACCT_TYPE = 'PRAN'");
                while (rs.next()) {
                    nps = new NPSDetails();
                    nps.setContType("A");
                    empId = rs.getString("emp_id");
                    nps.setHrmsgeneratedRefno(billId);
                    nps.setHrmsgeneratedRefdate(billdate);
                    nps.setPran(StringUtils.trim(rs.getString("GPF_NO")));
                    nps.setNameofSubscrib(rs.getString("EMP_NAME"));
                    nps.setBasic(rs.getInt("arrear_pay"));
                    nps.setBtserialno(btidforcpf);
                    nps.setPaymonth(monthasNumber + "");
                    nps.setPayYear(year + "");
                    nps.setDdoRegno(ddoregno);
                    nps.setSc(rs.getString("cpf_head"));
                    nps.setGc(rs.getString("cpf_head"));

                    npslist.add(nps);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return npslist;

    }

    private int getBasicforNPS(String aqsl_no) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        int basicamt = 0;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT sum(to_be_paid-already_paid) adamt FROM ARR_DTLS where AQSL_NO = ? and (ad_type='PAY' or ad_type='GP') ");
            ps.setString(1, aqsl_no);
            rs = ps.executeQuery();
            if (rs.next()) {
                basicamt = rs.getInt("adamt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return basicamt;
    }

    public static String getBillType(Connection con, int billId) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String billtype = "";
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT BILL_TYPE FROM BILL_MAST WHERE BILL_NO=" + billId);
            if (rs.next()) {
                billtype = rs.getString("BILL_TYPE");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
        }
        return billtype;
    }

    @Override
    public int getbillsubmissionCount(int billno) {
        int count = 0;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            String sqlQuery = "SELECT COUNT(*) cnt FROM BILL_STATUS_HISTORY WHERE STATUS_ID=2 AND BILL_ID=" + billno;
            st = con.createStatement();
            rs = st.executeQuery(sqlQuery);
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return count;
    }

    @Override
    public void updateBillStatus(int billno, int billStatusId) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("UPDATE BILL_MAST SET BILL_STATUS_ID=? WHERE BILL_NO=?");
            ps.setInt(1, billStatusId);
            ps.setInt(2, billno);
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateBillHistory(int billno, String submissionDate, String billgrossNet) {
        Connection con = null;
        PreparedStatement ps = null;
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        try {

            con = this.dataSource.getConnection();
            ps = con.prepareStatement("INSERT INTO BILL_STATUS_HISTORY (BILL_ID,HISTORY_DATE,STATUS_ID,REMARK) VALUES (?,?,?,?)");
            ps.setInt(1, billno);
            ps.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a").parse(submissionDate).getTime()));

            ps.setInt(3, 2);
            ps.setString(4, "ONLINE BILL SUBMITTED - " + billgrossNet);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getPayBillList(int year, int month, String treasuryCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList billList = new ArrayList();

        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT aq_month,aq_year,BILL_NO,major_head,sub_major_head,minor_head,sub_minor_head1,sub_minor_head2,sub_minor_head3,VCH_NO,VCH_DATE,extract (year from vch_date) vch_year, EXTRACT(MONTH FROM vch_date) vch_month,EXTRACT(DAY FROM vch_date) vch_day,ag_treasury_code,DDO_CODE FROM BILL_MAST bill "
                    + "inner join g_treasury treasury on bill.tr_code=treasury.tr_code WHERE extract (year from vch_date)=? AND extract (month from vch_date)=? AND bill.TR_CODE=?  AND bill.BILL_STATUS_ID=7");
            pstmt.setInt(1, year);
            pstmt.setInt(2, (month + 1));
            pstmt.setString(3, treasuryCode);

            rs = pstmt.executeQuery();

            String financialYear = "";
            if (month > 2) {
                financialYear = year + "-" + (year + 1);
            } else {
                financialYear = (year - 1) + "-" + year;
            }
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
            while (rs.next()) {
                BillBean bb = new BillBean();
                bb.setBillMonth(rs.getInt("aq_month"));
                bb.setBillYear(rs.getInt("aq_year"));
                bb.setAdjYear(rs.getInt("vch_year"));
                bb.setBillno(rs.getString("BILL_NO"));
                bb.setVoucherno(rs.getString("VCH_NO"));

                if (rs.getDate("VCH_DATE") != null) {
                    bb.setVoucherdate(DATE_FORMAT.format(rs.getDate("VCH_DATE")));
                }
                bb.setVouchermonth(rs.getString("vch_month"));
                bb.setFinyear(financialYear);
                bb.setTreasurycode(rs.getString("ag_treasury_code"));
                bb.setDdocode(rs.getString("DDO_CODE"));
                bb.setMajorhead(rs.getString("major_head"));
                bb.setVoucherDay(rs.getInt("vch_day"));
                billList.add(bb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billList;
    }

    @Override
    public int getNewBillYear(String offCode, String typeOfBill) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        int newYear = 0;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select max(aq_year) maxyear,count(*) cnt from BILL_MAST WHERE OFF_CODE=? and type_of_bill LIKE '%" + typeOfBill + "%' ");
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    newYear = rs.getInt("maxyear");
                } else {
                    newYear = Calendar.getInstance().get(Calendar.YEAR);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return newYear;
    }

    @Override
    public int getNewBillMonth(String offCode, int year, String typeOfBill) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        int newMonth = 0;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT MAX(AQ_MONTH) maxmonth FROM BILL_MAST WHERE OFF_CODE=? AND AQ_YEAR=? and TYPE_OF_BILL=?");
            pstmt.setString(1, offCode);
            pstmt.setInt(2, year);
            pstmt.setString(3, typeOfBill);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getString("maxmonth") == null) {
                    newMonth = -1;
                } else {
                    newMonth = rs.getInt("maxmonth");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return newMonth;
    }

    @Override
    public GlobalBillStatus getBillProcessStatus(String globalVariablename) {
        //String GLOBAL_VARIABLE_NAME = "STOP_BILL_PROCESS";
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        GlobalBillStatus gbs = new GlobalBillStatus();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT GLOBAL_VAR_VALUE, MESSAGE FROM HRMS_CONFIG WHERE GLOBAL_VAR_NAME=?");
            pstmt.setString(1, globalVariablename);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                gbs.setGlobalVarValue(rs.getString("GLOBAL_VAR_VALUE"));
                gbs.setMessage(rs.getString("MESSAGE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gbs;
    }

    @Override
    public String getMonthName(int month) {
        String monthString = "";
        if (month == 0) {
            monthString = "January";
        } else if (month == 1) {
            monthString = "February";
        } else if (month == 2) {
            monthString = "March";
        } else if (month == 3) {
            monthString = "April";
        } else if (month == 4) {
            monthString = "May";
        } else if (month == 5) {
            monthString = "June";
        } else if (month == 6) {
            monthString = "July";
        } else if (month == 7) {
            monthString = "August";
        } else if (month == 8) {
            monthString = "September";
        } else if (month == 9) {
            monthString = "October";
        } else if (month == 10) {
            monthString = "November";
        } else if (month == 11) {
            monthString = "December";
        }
        return monthString;
    }

    @Override
    public ArrayList getBillGroupList(String offCode, String curSpc) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList empBillGrpList = new ArrayList();
        boolean privFound = false;
        try {
            con = dataSource.getConnection();
            /*pstmt = con.prepareStatement("SELECT BILL_GROUP_ID, DESCRIPTION, PLAN, SECTOR, MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,POST_CLASS,DEMAND_NO from ("
             + "SELECT BILL_GRP_ID FROM BILL_GROUP_PRIVILAGE WHERE SPC=?) BILL_GROUP_PRIVILAGE "
             + "INNER JOIN ( "
             + "SELECT BILL_GROUP_ID, DESCRIPTION, PLAN, SECTOR, MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,POST_CLASS,DEMAND_NO from BILL_GROUP_MASTER "
             + "WHERE OFF_CODE=? AND (IS_DELETED IS  NULL OR IS_DELETED='N')) BILL_GROUP_MASTER ON BILL_GROUP_PRIVILAGE.BILL_GRP_ID = BILL_GROUP_MASTER.BILL_GROUP_ID::TEXT ORDER BY DESCRIPTION");*/
            pstmt = con.prepareStatement("SELECT BILL_GROUP_ID, DESCRIPTION, PLAN, SECTOR, MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,POST_CLASS,DEMAND_NO from"
                    + " (SELECT BILL_GROUP_ID, DESCRIPTION, PLAN, SECTOR, MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,POST_CLASS,DEMAND_NO from BILL_GROUP_MASTER"
                    + " WHERE OFF_CODE=? AND (IS_DELETED IS  NULL OR IS_DELETED='N'))BILL_GROUP_MASTER"
                    + " LEFT OUTER JOIN"
                    + " (SELECT BILL_GRP_ID FROM BILL_GROUP_PRIVILAGE WHERE SPC=?) BILL_GROUP_PRIVILAGE"
                    + " ON BILL_GROUP_PRIVILAGE.BILL_GRP_ID = BILL_GROUP_MASTER.BILL_GROUP_ID::BIGINT"
                    + " ORDER BY DESCRIPTION");

            pstmt.setString(1, offCode);
            pstmt.setString(2, curSpc);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                BillAttr billattr = new BillAttr();
                billattr.setBillgroupId(rs.getString("BILL_GROUP_ID"));
                billattr.setBillDesc(rs.getString("DESCRIPTION"));
                billattr.setChartofAcc(StringUtils.defaultString(rs.getString("DEMAND_NO")) + "-" + StringUtils.defaultString(rs.getString("MAJOR_HEAD")) + "-" + StringUtils.defaultString(rs.getString("SUB_MAJOR_HEAD")) + "-" + StringUtils.defaultString(rs.getString("MINOR_HEAD")) + "-" + StringUtils.defaultString(rs.getString("SUB_MINOR_HEAD1")) + "-" + StringUtils.defaultString(rs.getString("SUB_MINOR_HEAD2")) + "-" + StringUtils.defaultString(rs.getString("PLAN")) + "-" + StringUtils.defaultString(rs.getString("SUB_MINOR_HEAD3")) + "-" + StringUtils.defaultString(rs.getString("SECTOR")));
                privFound = true;
                empBillGrpList.add(billattr);
            }
            if (privFound == false) {
                pstmt = con.prepareStatement("SELECT BILL_GROUP_ID, DESCRIPTION, PLAN, SECTOR, MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,POST_CLASS,DEMAND_NO from BILL_GROUP_MASTER "
                        + "WHERE OFF_CODE=? AND (IS_DELETED IS  NULL OR IS_DELETED='N') ORDER BY DESCRIPTION");
                pstmt.setString(1, offCode);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    BillAttr billattr = new BillAttr();
                    billattr.setBillgroupId(rs.getString("BILL_GROUP_ID"));
                    billattr.setBillDesc(rs.getString("DESCRIPTION"));
                    billattr.setChartofAcc(StringUtils.defaultString(rs.getString("DEMAND_NO")) + "-" + StringUtils.defaultString(rs.getString("MAJOR_HEAD")) + "-" + StringUtils.defaultString(rs.getString("SUB_MAJOR_HEAD")) + "-" + StringUtils.defaultString(rs.getString("MINOR_HEAD")) + "-" + StringUtils.defaultString(rs.getString("SUB_MINOR_HEAD1")) + "-" + StringUtils.defaultString(rs.getString("SUB_MINOR_HEAD2")) + "-" + StringUtils.defaultString(rs.getString("PLAN")) + "-" + StringUtils.defaultString(rs.getString("SUB_MINOR_HEAD3")) + "-" + StringUtils.defaultString(rs.getString("SECTOR")));
                    empBillGrpList.add(billattr);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empBillGrpList;
    }

    @Override
    public BillAttr[] createBillFromBillGroup(int mAqMonth, int mAqYear, String[] billGroupId, String processDt, int priority, String billType, String moffcode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        Date processDate = null;
        Date fyStatrtDate = null;
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int task[] = null;
        int taskid = 0;
        String regularOrcontractualBill = "";
        BillAttr[] billStatus = null;
        ArrayList<BillAttr> billStatusA = new ArrayList<>();
        try {
            con = dataSource.getConnection();
            if ((year >= mAqYear + 1) || (year == mAqYear && mAqMonth <= month)) {

                task = new int[billGroupId.length];

                for (int i = 0; i < billGroupId.length; i++) {

                    BillAttr billAttr = new BillAttr();
                    billAttr.setBillgroupId(billGroupId[i]);

                    pstmt = con.prepareStatement("SELECT * FROM BILL_MAST WHERE BILL_GROUP_ID=? AND AQ_MONTH=? AND AQ_YEAR=? and type_of_bill=?");
                    pstmt.setBigDecimal(1, new BigDecimal(billGroupId[i]));
                    pstmt.setInt(2, mAqMonth);
                    pstmt.setInt(3, mAqYear);

                    pstmt.setString(4, billType);
                    rs = pstmt.executeQuery();
                    System.out.println("billGroupId[i]):" + billGroupId[i]);
                    boolean isDuplicateProcess = true;
                    if (rs.next()) {
                        isDuplicateProcess = true;
                    } else {
                        isDuplicateProcess = false;
                    }
                    if (!isDuplicateProcess) {
                        processDate = (Date) formatter.parse(processDt);
                        fyStatrtDate = (Date) formatter.parse("1-APR-" + mAqYear);
                        pstmt = con.prepareStatement("SELECT BILL_TYPE FROM G_SECTION INNER JOIN (SELECT DISTINCT SECTION_ID FROM BILL_SECTION_MAPPING WHERE BILL_GROUP_ID=?)B ON G_SECTION.SECTION_ID = B.SECTION_ID");
                        pstmt.setBigDecimal(1, new BigDecimal(billGroupId[i]));
                        rs = pstmt.executeQuery();

                        if (rs.next()) {
                            regularOrcontractualBill = rs.getString("BILL_TYPE");
                            System.out.println("billtype:" + rs.getString("BILL_TYPE"));
                        }
                        if (regularOrcontractualBill != null && !regularOrcontractualBill.equals("")) {
                            taskid = isContainsKey(moffcode, mAqMonth, mAqYear, billGroupId[i], processDt, billType, 0, regularOrcontractualBill, priority);
                            task[i] = taskid;
                        }
                        if (taskid == 0) {
                            billAttr.setMsg("Bill Cannot be Processed. Because you have not assigned section to bill group list or you have not mapped any employee into section or bill type is blank.  ");
                        } else {
                            billAttr.setMsg("Bill is under Process");
                        }
                        System.out.println("taskid:" + taskid);

                    }
                    billStatusA.add(billAttr);
                }
            }
            billStatus = billStatusA.toArray(new BillAttr[billStatusA.size()]);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billStatus;
    }

    @Override
    public BillAttr[] createBillForExtraMonthIncentive(int mAqMonth, int mAqYear, String[] billGroupId, String processDt, int priority, String billType, String moffcode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        Date processDate = null;
        Date fyStatrtDate = null;
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int task[] = null;
        int taskid = 0;
        String regularOrcontractualBill = "";
        BillAttr[] billStatus = null;
        ArrayList<BillAttr> billStatusA = new ArrayList<>();
        try {
            con = dataSource.getConnection();

            task = new int[billGroupId.length];

            for (int i = 0; i < billGroupId.length; i++) {

                BillAttr billAttr = new BillAttr();
                billAttr.setBillgroupId(billGroupId[i]);
                pstmt = con.prepareStatement("SELECT * FROM BILL_MAST WHERE BILL_GROUP_ID=? AND AQ_MONTH=? AND AQ_YEAR=? and type_of_bill=?");
                pstmt.setBigDecimal(1, new BigDecimal(billGroupId[i]));
                pstmt.setInt(2, mAqMonth);
                pstmt.setInt(3, mAqYear);
                pstmt.setString(4, billType);
                rs = pstmt.executeQuery();
                boolean isDuplicateProcess = true;
                if (rs.next()) {
                    isDuplicateProcess = true;
                } else {
                    isDuplicateProcess = false;
                }
                if (!isDuplicateProcess) {
                    processDate = (Date) formatter.parse(processDt);
                    fyStatrtDate = (Date) formatter.parse("1-APR-" + mAqYear);
                    pstmt = con.prepareStatement("SELECT BILL_TYPE FROM G_SECTION INNER JOIN (SELECT DISTINCT SECTION_ID FROM BILL_SECTION_MAPPING WHERE BILL_GROUP_ID=?)B ON G_SECTION.SECTION_ID = B.SECTION_ID");
                    pstmt.setBigDecimal(1, new BigDecimal(billGroupId[i]));
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        regularOrcontractualBill = rs.getString("BILL_TYPE");
                    }
                    taskid = isContainsKey(moffcode, mAqMonth, mAqYear, billGroupId[i], processDt, billType, 0, regularOrcontractualBill, priority);
                    task[i] = taskid;
                    if (taskid == 0) {
                        billAttr.setMsg("Bill Cannot be Processed. Because you have not assigned section to bill group list or you have not mapped any employee into section. ");
                    } else {
                        billAttr.setMsg("Bill is under Process");
                    }

                }
                billStatusA.add(billAttr);
            }

            billStatus = billStatusA.toArray(new BillAttr[billStatusA.size()]);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billStatus;
    }

    @Override

    public BillAttr[] createBillFromBillGroupForAdvancePay(String fromDate, String toDate, String[] billGroupId, String processDt, int priority, String billType, String moffcode, int arrearPercent) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        Date processDate = null;
        Date fyStatrtDate = null;
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int task[] = null;
        int taskid = 0;
        BillAttr[] billStatus = null;
        ArrayList billStatusA = new ArrayList();
        try {
            con = dataSource.getConnection();

            task = new int[billGroupId.length];

            if (billGroupId != null && billGroupId.length > 0) {
                for (int i = 0; i < billGroupId.length; i++) {
                    BillAttr billAttr = new BillAttr();
                    billAttr.setBillgroupId(billGroupId[i]);

                    /*
                     pstmt = con.prepareStatement("SELECT * FROM BILL_MAST WHERE BILL_GROUP_ID=? AND from_month=? AND from_year=? AND to_month=? AND to_year=?");
                     pstmt.setBigDecimal(1, new BigDecimal(billGroupId[i]));
                     pstmt.setInt(2, fromMonth);
                     pstmt.setInt(3, fromYear);
                     pstmt.setInt(4, toMonth);
                     pstmt.setInt(5, toYear);
                     rs = pstmt.executeQuery();
                     boolean isDuplicateProcess = true;
                     if (rs.next()) {
                     isDuplicateProcess = true;
                     } else {
                     isDuplicateProcess = false;
                     }
                     */
                    boolean isDuplicateProcess = false;
                    if (!isDuplicateProcess) {

                        processDate = (Date) formatter.parse(processDt);
                        taskid = isContainsKeyForAdvancePay(moffcode, billGroupId[i], processDt, billType, 0, priority, fromDate, toDate, arrearPercent);
                        task[i] = taskid;

                        if (taskid == 0) {
                            billAttr.setMsg("Bill Cannot be Processed. Because you have not assigned section to bill group list or you have not mapped any employee into section. ");
                        } else {
                            billAttr.setMsg("Bill is under Process");
                        }

                    }
                    billStatusA.add(billAttr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billStatus;
    }

    @Override
    public BillAttr[] createBillFromBillGroupForArrear(String fromDate, String toDate, String[] billGroupId, String processDt, int priority, String billType, String moffcode, int arrearPercent) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        Date processDate = null;
        Date fyStatrtDate = null;
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int task[] = null;
        int taskid = 0;
        BillAttr[] billStatus = null;
        ArrayList billStatusA = new ArrayList();
        try {
            con = dataSource.getConnection();

            task = new int[billGroupId.length];

            if (billGroupId != null && billGroupId.length > 0) {
                for (int i = 0; i < billGroupId.length; i++) {
                    BillAttr billAttr = new BillAttr();
                    billAttr.setBillgroupId(billGroupId[i]);

                    /*
                     pstmt = con.prepareStatement("SELECT * FROM BILL_MAST WHERE BILL_GROUP_ID=? AND from_month=? AND from_year=? AND to_month=? AND to_year=?");
                     pstmt.setBigDecimal(1, new BigDecimal(billGroupId[i]));
                     pstmt.setInt(2, fromMonth);
                     pstmt.setInt(3, fromYear);
                     pstmt.setInt(4, toMonth);
                     pstmt.setInt(5, toYear);
                     rs = pstmt.executeQuery();
                     boolean isDuplicateProcess = true;
                     if (rs.next()) {
                     isDuplicateProcess = true;
                     } else {
                     isDuplicateProcess = false;
                     }
                     */
                    boolean isDuplicateProcess = false;
                    if (!isDuplicateProcess) {

                        processDate = (Date) formatter.parse(processDt);
                        taskid = isContainsKeyForArrear(moffcode, billGroupId[i], processDt, billType, 0, priority, fromDate, toDate, arrearPercent);
                        task[i] = taskid;

                        if (taskid == 0) {
                            billAttr.setMsg("Bill Cannot be Processed. Because you have not assigned section to bill group list or you have not mapped any employee into section. ");
                        } else {
                            billAttr.setMsg("Bill is under Process");
                        }

                    }
                    billStatusA.add(billAttr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billStatus;
    }

    public int getBillPriority(String offCode) {
        int priority = 0;
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT PAYBILL_PRIORITY FROM G_OFFICE WHERE OFF_CODE=?");
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                priority = rs.getInt("PAYBILL_PRIORITY");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return priority;
    }

    public int isContainsKeyForAdvancePay(String moffcode, String bgId, String processDate, String billType, int billId, int priority, String fromDate, String toDate, int arrearPercent) throws Exception {
        Connection con = null;
        PreparedStatement stamt = null;
        ResultSet res = null;
        ResultSet res1 = null;
        int containsKey = 0;
        int taskid = 0;
        try {
            Calendar c = Calendar.getInstance();
            int aqyear = c.get(Calendar.YEAR);
            int aqmonth = c.get(Calendar.MONTH);
            con = dataSource.getConnection();
            /**
             * ******************THIS QUERY RETRIEVES ALL EMPLOYEES OF AN
             * OFFICE INCLUDING EMPLOYEES RELIEVED IN CURRENT
             * MONTH******************
             */
            String query = "";

            query = "SELECT TASK_ID FROM PAYBILL_TASK where OFF_CODE=?  AND BILL_GROUP_ID=? AND bill_type=?";
            stamt = con.prepareStatement(query);
            stamt.setString(1, moffcode);
            stamt.setBigDecimal(2, new BigDecimal(bgId));
            stamt.setString(3, billType);

            res = stamt.executeQuery();
            if (res.next()) {
                taskid = res.getInt("TASK_ID");
                containsKey = taskid;
            } else {
                containsKey = 0;
            }

            if (containsKey == 0) {
                if (billId == 0) {
                    billId = createBillIdForAdvancePay(moffcode, bgId, processDate, billType, aqmonth, aqyear, fromDate, toDate, arrearPercent);
                }
                stamt = con.prepareStatement("select COALESCE(max(task_id),0)+1 taskid FROM paybill_task");
                res = stamt.executeQuery();
                taskid = 0;
                if (res.next()) {
                    taskid = res.getInt("TASKID");
                }

                stamt = con.prepareStatement("INSERT INTO PAYBILL_TASK (TASK_ID, OFF_CODE, BILL_ID, BILL_GROUP_ID, PRIORITY, bill_type, aq_month, aq_year) VALUES (?,?,?,?,?,?,?,?)");
                stamt.setInt(1, taskid);
                stamt.setString(2, moffcode);
                stamt.setInt(3, billId);
                stamt.setBigDecimal(4, new BigDecimal(bgId));
                stamt.setInt(5, priority);
                stamt.setString(6, billType);
                stamt.setInt(7, aqmonth);
                stamt.setInt(8, aqyear);
                stamt.executeUpdate();

            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res1);
            DataBaseFunctions.closeSqlObjects(res, stamt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return taskid;
    }

    public int isContainsKeyForArrear(String moffcode, String bgId, String processDate, String billType, int billId, int priority, String fromDate, String toDate, int arrearPercent) throws Exception {
        Connection con = null;
        PreparedStatement stamt = null;
        ResultSet res = null;
        ResultSet res1 = null;
        int containsKey = 0;
        int taskid = 0;
        try {
            Calendar c = Calendar.getInstance();
            int aqyear = c.get(Calendar.YEAR);
            int aqmonth = c.get(Calendar.MONTH);
            con = dataSource.getConnection();
            /**
             * ******************THIS QUERY RETRIEVES ALL EMPLOYEES OF AN
             * OFFICE INCLUDING EMPLOYEES RELIEVED IN CURRENT
             * MONTH******************
             */
            String query = "";

            query = "SELECT TASK_ID FROM PAYBILL_TASK where OFF_CODE=?  AND BILL_GROUP_ID=? AND bill_type=?";
            stamt = con.prepareStatement(query);
            stamt.setString(1, moffcode);
            stamt.setBigDecimal(2, new BigDecimal(bgId));
            stamt.setString(3, billType);

            res = stamt.executeQuery();
            if (res.next()) {
                taskid = res.getInt("TASK_ID");
                containsKey = taskid;
            } else {
                containsKey = 0;
            }

            if (containsKey == 0) {
                if (billId == 0) {
                    billId = createBillIdForArrear(moffcode, bgId, processDate, billType, aqmonth, aqyear, fromDate, toDate, arrearPercent);
                }
                stamt = con.prepareStatement("select COALESCE(max(task_id),0)+1 taskid FROM paybill_task");
                res = stamt.executeQuery();
                taskid = 0;
                if (res.next()) {
                    taskid = res.getInt("TASKID");
                }

                stamt = con.prepareStatement("INSERT INTO PAYBILL_TASK (TASK_ID, OFF_CODE, BILL_ID, BILL_GROUP_ID, PRIORITY, bill_type, aq_month, aq_year) VALUES (?,?,?,?,?,?,?,?)");
                stamt.setInt(1, taskid);
                stamt.setString(2, moffcode);
                stamt.setInt(3, billId);
                stamt.setBigDecimal(4, new BigDecimal(bgId));
                stamt.setInt(5, priority);
                stamt.setString(6, billType);
                stamt.setInt(7, aqmonth);
                stamt.setInt(8, aqyear);
                stamt.executeUpdate();

            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res1);
            DataBaseFunctions.closeSqlObjects(res, stamt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return taskid;
    }

    public int isContainsKey(String moffcode, int aqMonth, int aqYear, String bgId, String processDate, String billType, int billId, String regularOrcontractualBill, int priority) throws Exception {
        Connection con = null;
        Statement stamt = null;
        ResultSet res = null;
        ResultSet res1 = null;
        int containsKey = 0;
        int taskid = 0;
        String employeeQuery = "";
        try {
            con = dataSource.getConnection();
            /**
             * ******************THIS QUERY RETRIEVES ALL EMPLOYEES OF AN
             * OFFICE INCLUDING EMPLOYEES RELIEVED IN CURRENT
             * MONTH******************
             */

            if (regularOrcontractualBill.equalsIgnoreCase("REGULAR") || regularOrcontractualBill.equalsIgnoreCase("CONT6_REG") || regularOrcontractualBill.equalsIgnoreCase("SP_CATGORY")) {
                employeeQuery = "SELECT COUNT(*) CNT FROM( "
                        + "SELECT OFF_CODE, SECTION_POST_MAPPING.SPC,EMP_ID, NAME,SECTION_ID FROM ( "
                        + "SELECT OFF_CODE, SPC,EMP_ID, NAME    FROM "
                        + " (SELECT '" + moffcode + "'::TEXT OFF_CODE ,G_SPC.SPC,EMP_MAST.EMP_ID,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ')||' / '||G_SPC.SPN AS NAME "
                        + " FROM (SELECT * FROM G_SPC WHERE OFF_CODE ='" + moffcode + "' AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL))G_SPC "
                        + " LEFT OUTER JOIN "
                        + " (SELECT EMP_ID,CUR_SPC,GPF_NO,INITIALS,F_NAME,M_NAME,L_NAME FROM EMP_MAST WHERE CUR_OFF_CODE ='" + moffcode + "' AND (DEP_CODE='02' OR DEP_CODE='05') AND CUR_SPC IS NOT NULL) EMP_MAST "
                        + " ON  G_SPC.SPC = EMP_MAST.CUR_SPC "
                        + " INNER JOIN SECTION_POST_MAPPING ON G_SPC.SPC = SECTION_POST_MAPPING.SPC ORDER BY F_NAME) AS TAB1"
                        + " UNION "
                        + " (SELECT '" + moffcode + "'::TEXT OFF_CODE ,G_SPC.SPC,EMP_MAST.EMP_ID,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ')||' / '||G_SPC.SPN AS NAME "
                        + " FROM"
                        + " (SELECT SPC,EMP_ID FROM EMP_RELIEVE WHERE SUBSTR(SPC,0,13)='" + moffcode + "' AND TO_CHAR(RLV_DATE,'mm')='" + aqMonth + "' AND TO_CHAR(RLV_DATE,'yyyy')='" + aqYear + "' AND ((TO_CHAR(RLV_DATE,'dd') || RLV_TIME) != '01FN')) TMPRLV"
                        + " INNER JOIN "
                        + " (SELECT EMP_ID,CUR_SPC,GPF_NO,INITIALS,F_NAME,M_NAME,L_NAME FROM EMP_MAST) EMP_MAST "
                        + " ON TMPRLV.EMP_ID=EMP_MAST.EMP_ID"
                        + " LEFT OUTER JOIN"
                        + " (SELECT * FROM G_SPC WHERE OFF_CODE ='" + moffcode + "' AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL)) G_SPC "
                        + " ON TMPRLV.SPC = G_SPC.SPC) ) AS COMPLETE_QUERY "
                        + " LEFT OUTER JOIN SECTION_POST_MAPPING ON COMPLETE_QUERY.SPC=SECTION_POST_MAPPING.SPC ) TEMP "
                        + " INNER JOIN (SELECT * FROM BILL_SECTION_MAPPING WHERE BILL_GROUP_ID IN (" + bgId + ")) BILL_SECTION_MAPPING ON TEMP.SECTION_ID=BILL_SECTION_MAPPING.SECTION_ID ";
            } else if (regularOrcontractualBill.equalsIgnoreCase("CONTRACTUAL") || regularOrcontractualBill.equalsIgnoreCase("DEPUTATION")) {
                employeeQuery = " SELECT COUNT(SPC)CNT FROM ( "
                        + " SELECT SECTION_ID,BILL_GROUP_ID FROM BILL_SECTION_MAPPING WHERE BILL_GROUP_ID IN (" + bgId + ")) AS TEMP "
                        + " INNER JOIN SECTION_POST_MAPPING ON TEMP.SECTION_ID=SECTION_POST_MAPPING.SECTION_ID";
            }

            stamt = con.createStatement();
            String query = "";

            query = "SELECT TASK_ID FROM PAYBILL_TASK where OFF_CODE='" + moffcode + "' AND AQ_MONTH=" + aqMonth + " AND AQ_YEAR=" + aqYear + " AND BILL_GROUP_ID='" + bgId + "' AND BILL_TYPE='" + billType + "'";

            res = stamt.executeQuery(query);
            if (res.next()) {
                taskid = res.getInt("TASK_ID");
                containsKey = taskid;
            } else {
                containsKey = 0;
            }

            System.out.println("containsKey:" + containsKey);

            if (containsKey == 0) {
                if (billId == 0) {
                    billId = createBillId(moffcode, bgId, processDate, billType, aqMonth, aqYear);
                }
                System.out.println("billid:" + billId);
                stamt = con.createStatement();

                res = stamt.executeQuery(employeeQuery);
                int totalNoOfEmployee = 0;
                if (res.next()) {
                    totalNoOfEmployee = res.getInt("CNT");
                }
                if (totalNoOfEmployee > 0) {
                    stamt = con.createStatement();
                    res = stamt.executeQuery("select COALESCE(max(task_id),0)+1 taskid FROM paybill_task");
                    taskid = 0;
                    if (res.next()) {
                        taskid = res.getInt("TASKID");
                    }
                    stamt = con.createStatement();
                    stamt.execute("INSERT INTO PAYBILL_TASK (TASK_ID,OFF_CODE,AQ_MONTH,AQ_YEAR,TOTAL_AQ,BILL_ID,BILL_GROUP_ID,PRIORITY, bill_type) VALUES (" + taskid + ",'" + moffcode + "'," + aqMonth + "," + aqYear + "," + totalNoOfEmployee + "," + billId + ",'" + bgId + "'," + priority + ", '" + billType + "')");
                } else {
                    taskid = 0;
                }
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res1);
            DataBaseFunctions.closeSqlObjects(res, stamt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return taskid;
    }

    public int isContainsKeyForLeftOut(String moffcode, int aqMonth, int aqYear, String bgId, String processDate, String billType, int billId, String regularOrcontractualBill, int priority) throws Exception {
        Connection con = null;
        Statement stamt = null;
        ResultSet res = null;
        ResultSet res1 = null;
        int containsKey = 0;
        int taskid = 0;
        String employeeQuery = "";
        try {
            con = dataSource.getConnection();
            /**
             * ******************THIS QUERY RETRIEVES ALL EMPLOYEES OF AN
             * OFFICE INCLUDING EMPLOYEES RELIEVED IN CURRENT
             * MONTH******************
             */

            stamt = con.createStatement();
            String query = "";

            query = "SELECT TASK_ID FROM PAYBILL_TASK where OFF_CODE='" + moffcode + "' AND AQ_MONTH=" + aqMonth + " AND AQ_YEAR=" + aqYear + " AND BILL_GROUP_ID='" + bgId + "' AND BILL_TYPE='" + billType + "'";

            res = stamt.executeQuery(query);
            if (res.next()) {
                taskid = res.getInt("TASK_ID");
                containsKey = taskid;
            } else {
                containsKey = 0;
            }

            if (containsKey == 0) {
                if (billId == 0) {
                    billId = createBillId(moffcode, bgId, processDate, billType, aqMonth, aqYear);
                }
                stamt = con.createStatement();

                res = stamt.executeQuery(employeeQuery);
                int totalNoOfEmployee = 0;
                if (res.next()) {
                    totalNoOfEmployee = res.getInt("CNT");
                }
                if (totalNoOfEmployee > 0) {
                    stamt = con.createStatement();
                    res = stamt.executeQuery("select COALESCE(max(task_id),0)+1 taskid FROM paybill_task");
                    taskid = 0;
                    if (res.next()) {
                        taskid = res.getInt("TASKID");
                    }
                    stamt = con.createStatement();
                    stamt.execute("INSERT INTO PAYBILL_TASK (TASK_ID,OFF_CODE,AQ_MONTH,AQ_YEAR,TOTAL_AQ,BILL_ID,BILL_GROUP_ID,PRIORITY, bill_type) VALUES (" + taskid + ",'" + moffcode + "'," + aqMonth + "," + aqYear + "," + totalNoOfEmployee + "," + billId + ",'" + bgId + "'," + priority + ", '" + billType + "')");
                } else {
                    taskid = 0;
                }
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res1);
            DataBaseFunctions.closeSqlObjects(res, stamt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return taskid;
    }

    public int createBillId(String offCode, String bgrId, String processDate, String billType, int aqmonth, int aqyear) throws Exception {
        Connection con = null;
        ResultSet rs2 = null;
        Statement st2 = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int mBillNo = 0;

        try {
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            con = dataSource.getConnection();
            st2 = con.createStatement();
            rs2 = st2.executeQuery("SELECT G_OFFICE.OFF_CODE,BILL_GROUP_ID, DESCRIPTION,DEMAND_NO,PLAN,SECTOR,MAJOR_HEAD, MAJOR_HEAD_DESC, SUB_MAJOR_HEAD, SUB_MAJOR_HEAD_DESC, MINOR_HEAD, MINOR_HEAD_DESC, \n"
                    + "  SUB_MINOR_HEAD1, SUB_MINOR_HEAD1_DESC, SUB_MINOR_HEAD2, SUB_MINOR_HEAD2_DESC, SUB_MINOR_HEAD3, SUB_MINOR_HEAD3_DESC,g_office.DDO_CODE,"
                    + "  g_office.BANK_CODE,g_office.BRANCH_CODE,g_office.TR_CODE,BILL_TYPE, PAY_HEAD, BILL_GROUP_MASTER.co_code, ddo_hrmsid,"
                    + " ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, DDO_POST, post  FROM ("
                    + "  SELECT * FROM BILL_GROUP_MASTER WHERE OFF_CODE='" + offCode + "' AND (IS_DELETED IS  NULL OR IS_DELETED='N') AND BILL_GROUP_ID =" + bgrId
                    + " )BILL_GROUP_MASTER \n"
                    + "  LEFT OUTER JOIN G_OFFICE ON BILL_GROUP_MASTER.OFF_CODE = G_OFFICE.OFF_CODE"
                    + "  left outer join g_post on g_office.ddo_post=g_post.post_code "
                    + "  left outer join emp_mast on g_office.ddo_hrmsid=emp_mast.emp_id");

            if (rs2.next()) {

                pst = con.prepareStatement("INSERT INTO BILL_MAST (BILL_DESC,BILL_DATE,BILL_TYPE,AQ_GROUP_DESC,AQ_MONTH,AQ_YEAR,OFF_CODE,DEMAND_NO,MAJOR_HEAD,MAJOR_HEAD_DESC,SUB_MAJOR_HEAD,SUB_MAJOR_HEAD_DESC,"
                        + "MINOR_HEAD,MINOR_HEAD_DESC,SUB_MINOR_HEAD1,SUB_MINOR_HEAD1_DESC,SUB_MINOR_HEAD2,SUB_MINOR_HEAD2_DESC,SUB_MINOR_HEAD3,BILL_GROUP_DESC,PLAN,SECTOR,DDO_CODE,BANK_CODE,BRANCH_CODE,"
                        + "TR_CODE,BILL_GROUP_ID,TYPE_OF_BILL, PAY_HEAD, CO_CODE, DDO_POST, ddo_empid, ddo_name, ddo_post_name)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                //pst.setInt(1, mBillNo);
                pst.setString(1, null);
                pst.setTimestamp(2, new Timestamp(dateFormat.parse(processDate).getTime()));
                pst.setString(3, rs2.getString("BILL_TYPE"));
                pst.setString(4, null);
                pst.setInt(5, aqmonth);
                pst.setInt(6, aqyear);
                pst.setString(7, offCode);
                pst.setString(8, rs2.getString("DEMAND_NO"));
                pst.setString(9, rs2.getString("MAJOR_HEAD"));
                pst.setString(10, rs2.getString("MAJOR_HEAD_DESC"));
                pst.setString(11, rs2.getString("SUB_MAJOR_HEAD"));
                pst.setString(12, rs2.getString("SUB_MAJOR_HEAD_DESC"));
                pst.setString(13, rs2.getString("MINOR_HEAD"));
                pst.setString(14, rs2.getString("MINOR_HEAD_DESC"));
                pst.setString(15, rs2.getString("SUB_MINOR_HEAD1"));
                pst.setString(16, rs2.getString("SUB_MINOR_HEAD1_DESC"));
                pst.setString(17, rs2.getString("SUB_MINOR_HEAD2"));
                pst.setString(18, rs2.getString("SUB_MINOR_HEAD2_DESC"));
                pst.setString(19, rs2.getString("SUB_MINOR_HEAD3"));
                pst.setString(20, rs2.getString("DESCRIPTION"));
                pst.setString(21, rs2.getString("PLAN"));
                pst.setString(22, rs2.getString("SECTOR"));
                pst.setString(23, rs2.getString("DDO_CODE"));
                pst.setString(24, rs2.getString("BANK_CODE"));
                pst.setString(25, rs2.getString("BRANCH_CODE"));
                pst.setString(26, rs2.getString("TR_CODE"));
                pst.setBigDecimal(27, new BigDecimal(bgrId));
                pst.setString(28, billType);
                pst.setString(29, rs2.getString("PAY_HEAD"));
                pst.setString(30, rs2.getString("co_code"));
                pst.setString(31, rs2.getString("DDO_POST"));
                pst.setString(32, rs2.getString("ddo_hrmsid"));
                pst.setString(33, rs2.getString("EMPNAME"));
                pst.setString(34, rs2.getString("post"));

                int ret = pst.executeUpdate();
                rs = pst.getGeneratedKeys();
                rs.next();
                mBillNo = rs.getInt("bill_no");

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs2, pst);
            DataBaseFunctions.closeSqlObjects(st2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return mBillNo;
    }

    public int createBillIdForAdvancePay(String offCode, String bgrId, String processDate, String billType, int aqmonth, int aqyear, String fromDate, String toDate, int arrearPercent) throws Exception {
        Connection con = null;
        ResultSet rs2 = null;
        ResultSet rs = null;
        Statement st2 = null;
        PreparedStatement pst = null;
        int mBillNo = 0;
        int fromYear = 0;
        int fromMonth = 0;
        int frmDateAsNumber = 0;
        int toYear = 0;
        int toMonth = 0;
        int toDateAsNumber = 0;

        try {
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            con = dataSource.getConnection();
            st2 = con.createStatement();

            Date dt = new Date();
            Calendar cal = Calendar.getInstance();

            if (fromDate != null && !fromDate.equals("")) {
                dt = new Date(fromDate);
                cal.setTime(dt);
                frmDateAsNumber = cal.get(Calendar.DATE);
                fromMonth = cal.get(Calendar.MONTH) + 1;
                fromYear = cal.get(Calendar.YEAR);
            }

            if (toDate != null && !toDate.equals("")) {
                dt = new Date(toDate);
                cal.setTime(dt);
                toDateAsNumber = cal.get(Calendar.DATE);
                toMonth = cal.get(Calendar.MONTH) + 1;
                toYear = cal.get(Calendar.YEAR);
            }

            rs2 = st2.executeQuery("SELECT G_OFFICE.OFF_CODE,BILL_GROUP_ID, DESCRIPTION,DEMAND_NO,PLAN,SECTOR,MAJOR_HEAD, MAJOR_HEAD_DESC, "
                    + "SUB_MAJOR_HEAD, SUB_MAJOR_HEAD_DESC, MINOR_HEAD, MINOR_HEAD_DESC, SUB_MINOR_HEAD1, SUB_MINOR_HEAD1_DESC, SUB_MINOR_HEAD2, "
                    + "SUB_MINOR_HEAD2_DESC, SUB_MINOR_HEAD3, SUB_MINOR_HEAD3_DESC,DDO_CODE,DDO_POST,BANK_CODE,BRANCH_CODE,TR_CODE,"
                    + "BILL_TYPE, PAY_HEAD, BILL_GROUP_MASTER.co_code "
                    + " FROM (SELECT * FROM BILL_GROUP_MASTER WHERE OFF_CODE='" + offCode + "' AND (IS_DELETED IS  NULL OR IS_DELETED='N') "
                    + " AND BILL_GROUP_ID ='" + bgrId + "' )BILL_GROUP_MASTER "
                    + "LEFT OUTER JOIN G_OFFICE ON BILL_GROUP_MASTER.OFF_CODE = G_OFFICE.OFF_CODE");

            if (rs2.next()) {

                pst = con.prepareStatement("INSERT INTO BILL_MAST (BILL_DESC,BILL_DATE,BILL_TYPE,AQ_GROUP_DESC,AQ_MONTH,AQ_YEAR,OFF_CODE,DEMAND_NO,MAJOR_HEAD,MAJOR_HEAD_DESC,"
                        + " SUB_MAJOR_HEAD,SUB_MAJOR_HEAD_DESC,MINOR_HEAD,MINOR_HEAD_DESC,"
                        + " SUB_MINOR_HEAD1,SUB_MINOR_HEAD1_DESC,SUB_MINOR_HEAD2,SUB_MINOR_HEAD2_DESC,SUB_MINOR_HEAD3,BILL_GROUP_DESC,PLAN,SECTOR,DDO_CODE,BANK_CODE,"
                        + " BRANCH_CODE,TR_CODE,BILL_GROUP_ID,TYPE_OF_BILL, from_month ,from_year ,to_month ,to_year, pay_head, from_date, to_date, arrear_percent, co_code ) "
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, null);
                pst.setTimestamp(2, new Timestamp(dateFormat.parse(processDate).getTime()));
                if (rs2.getString("BILL_TYPE") != null && rs2.getString("BILL_TYPE").equals("69")) {
                    pst.setString(3, rs2.getString("BILL_TYPE"));
                } else {
                    pst.setString(3, "42");
                }

                pst.setString(4, null);
                pst.setInt(5, aqmonth);
                pst.setInt(6, aqyear);
                pst.setString(7, offCode);
                pst.setString(8, rs2.getString("DEMAND_NO"));
                pst.setString(9, rs2.getString("MAJOR_HEAD"));
                pst.setString(10, rs2.getString("MAJOR_HEAD_DESC"));
                pst.setString(11, rs2.getString("SUB_MAJOR_HEAD"));
                pst.setString(12, rs2.getString("SUB_MAJOR_HEAD_DESC"));
                pst.setString(13, rs2.getString("MINOR_HEAD"));
                pst.setString(14, rs2.getString("MINOR_HEAD_DESC"));
                pst.setString(15, rs2.getString("SUB_MINOR_HEAD1"));
                pst.setString(16, rs2.getString("SUB_MINOR_HEAD1_DESC"));
                pst.setString(17, rs2.getString("SUB_MINOR_HEAD2"));
                pst.setString(18, rs2.getString("SUB_MINOR_HEAD2_DESC"));
                pst.setString(19, rs2.getString("SUB_MINOR_HEAD3"));
                pst.setString(20, rs2.getString("DESCRIPTION"));
                pst.setString(21, rs2.getString("PLAN"));
                pst.setString(22, rs2.getString("SECTOR"));
                pst.setString(23, rs2.getString("DDO_CODE"));
                pst.setString(24, rs2.getString("BANK_CODE"));
                pst.setString(25, rs2.getString("BRANCH_CODE"));
                pst.setString(26, rs2.getString("TR_CODE"));
                pst.setBigDecimal(27, new BigDecimal(bgrId));
                pst.setString(28, billType);
                pst.setInt(29, fromMonth);
                pst.setInt(30, fromYear);
                pst.setInt(31, toMonth);
                pst.setInt(32, toYear);
                pst.setString(33, rs2.getString("PAY_HEAD"));
                pst.setInt(34, frmDateAsNumber);
                pst.setInt(35, toDateAsNumber);
                pst.setInt(36, arrearPercent);
                pst.setString(37, rs2.getString("co_code"));
                int ret = pst.executeUpdate();
                rs = pst.getGeneratedKeys();
                rs.next();
                mBillNo = rs.getInt("bill_no");

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs2, pst);
            DataBaseFunctions.closeSqlObjects(st2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return mBillNo;
    }

    public int createBillIdForArrear(String offCode, String bgrId, String processDate, String billType, int aqmonth, int aqyear, String fromDate, String toDate, int arrearPercent) throws Exception {
        Connection con = null;
        ResultSet rs2 = null;
        ResultSet rs = null;
        Statement st2 = null;
        PreparedStatement pst = null;
        int mBillNo = 0;
        int fromYear = 0;
        int fromMonth = 0;
        int frmDateAsNumber = 0;
        int toYear = 0;
        int toMonth = 0;
        int toDateAsNumber = 0;

        try {
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            con = dataSource.getConnection();
            st2 = con.createStatement();

            Date dt = new Date();
            Calendar cal = Calendar.getInstance();

            if (fromDate != null && !fromDate.equals("")) {
                dt = new Date(fromDate);
                cal.setTime(dt);
                frmDateAsNumber = cal.get(Calendar.DATE);
                fromMonth = cal.get(Calendar.MONTH) + 1;
                fromYear = cal.get(Calendar.YEAR);
            }

            if (toDate != null && !toDate.equals("")) {
                dt = new Date(toDate);
                cal.setTime(dt);
                toDateAsNumber = cal.get(Calendar.DATE);
                toMonth = cal.get(Calendar.MONTH) + 1;
                toYear = cal.get(Calendar.YEAR);
            }

            rs2 = st2.executeQuery("SELECT G_OFFICE.OFF_CODE,BILL_GROUP_ID, DESCRIPTION,DEMAND_NO,PLAN,SECTOR,MAJOR_HEAD, "
                    + " MAJOR_HEAD_DESC, SUB_MAJOR_HEAD, SUB_MAJOR_HEAD_DESC, MINOR_HEAD, MINOR_HEAD_DESC, SUB_MINOR_HEAD1, "
                    + " SUB_MINOR_HEAD1_DESC, SUB_MINOR_HEAD2, "
                    + " SUB_MINOR_HEAD2_DESC, SUB_MINOR_HEAD3, SUB_MINOR_HEAD3_DESC,DDO_CODE,DDO_POST,BANK_CODE,"
                    + " BRANCH_CODE,TR_CODE,BILL_TYPE, PAY_HEAD, BILL_GROUP_MASTER.co_code FROM (SELECT * FROM BILL_GROUP_MASTER "
                    + " WHERE OFF_CODE='" + offCode + "' AND (IS_DELETED IS  NULL OR IS_DELETED='N') "
                    + " AND BILL_GROUP_ID ='" + bgrId + "' )BILL_GROUP_MASTER "
                    + " LEFT OUTER JOIN G_OFFICE ON BILL_GROUP_MASTER.OFF_CODE = G_OFFICE.OFF_CODE");

            if (rs2.next()) {

                pst = con.prepareStatement("INSERT INTO BILL_MAST (BILL_DESC,BILL_DATE,BILL_TYPE,AQ_GROUP_DESC,AQ_MONTH,"
                        + " AQ_YEAR,OFF_CODE,DEMAND_NO,MAJOR_HEAD,MAJOR_HEAD_DESC,SUB_MAJOR_HEAD,SUB_MAJOR_HEAD_DESC,"
                        + " MINOR_HEAD,MINOR_HEAD_DESC,"
                        + " SUB_MINOR_HEAD1,SUB_MINOR_HEAD1_DESC,SUB_MINOR_HEAD2,SUB_MINOR_HEAD2_DESC,SUB_MINOR_HEAD3,"
                        + " BILL_GROUP_DESC,PLAN,SECTOR,DDO_CODE,BANK_CODE,BRANCH_CODE,TR_CODE,BILL_GROUP_ID,TYPE_OF_BILL,"
                        + " from_month ,from_year ,to_month ,to_year, pay_head, from_date, to_date, arrear_percent, co_code )"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

                pst.setString(1, null);
                pst.setTimestamp(2, new Timestamp(dateFormat.parse(processDate).getTime()));
                if (rs2.getString("BILL_TYPE") != null && rs2.getString("BILL_TYPE").equals("69")) {
                    pst.setString(3, rs2.getString("BILL_TYPE"));
                } else {
                    pst.setString(3, "43");
                }

                pst.setString(4, null);
                pst.setInt(5, aqmonth);
                pst.setInt(6, aqyear);
                pst.setString(7, offCode);
                pst.setString(8, rs2.getString("DEMAND_NO"));
                pst.setString(9, rs2.getString("MAJOR_HEAD"));
                pst.setString(10, rs2.getString("MAJOR_HEAD_DESC"));
                pst.setString(11, rs2.getString("SUB_MAJOR_HEAD"));
                pst.setString(12, rs2.getString("SUB_MAJOR_HEAD_DESC"));
                pst.setString(13, rs2.getString("MINOR_HEAD"));
                pst.setString(14, rs2.getString("MINOR_HEAD_DESC"));
                pst.setString(15, rs2.getString("SUB_MINOR_HEAD1"));
                pst.setString(16, rs2.getString("SUB_MINOR_HEAD1_DESC"));
                pst.setString(17, rs2.getString("SUB_MINOR_HEAD2"));
                pst.setString(18, rs2.getString("SUB_MINOR_HEAD2_DESC"));
                pst.setString(19, rs2.getString("SUB_MINOR_HEAD3"));
                pst.setString(20, rs2.getString("DESCRIPTION"));
                pst.setString(21, rs2.getString("PLAN"));
                pst.setString(22, rs2.getString("SECTOR"));
                pst.setString(23, rs2.getString("DDO_CODE"));
                pst.setString(24, rs2.getString("BANK_CODE"));
                pst.setString(25, rs2.getString("BRANCH_CODE"));
                pst.setString(26, rs2.getString("TR_CODE"));
                pst.setBigDecimal(27, new BigDecimal(bgrId));
                pst.setString(28, billType);
                pst.setInt(29, fromMonth);
                pst.setInt(30, fromYear);
                pst.setInt(31, toMonth);
                pst.setInt(32, toYear);
                pst.setString(33, rs2.getString("PAY_HEAD"));
                pst.setInt(34, frmDateAsNumber);
                pst.setInt(35, toDateAsNumber);
                pst.setInt(36, arrearPercent);
                pst.setString(37, rs2.getString("co_code"));
                int ret = pst.executeUpdate();
                rs = pst.getGeneratedKeys();
                rs.next();
                mBillNo = rs.getInt("bill_no");

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs2, pst);
            DataBaseFunctions.closeSqlObjects(st2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return mBillNo;
    }

    @Override
    public AllowDeductDetails getPay(int billno, String offCode) {
        AllowDeductDetails allowdeduct = new AllowDeductDetails();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT BILL_MAST.PAY_HEAD,SUM(CUR_BASIC) AS PAY_AMOUNT FROM BILL_MAST INNER JOIN AQ_MAST ON BILL_MAST.BILL_NO = AQ_MAST.BILL_NO "
                    + "WHERE BILL_MAST.BILL_NO=? AND BILL_MAST.OFF_CODE=? GROUP BY BILL_MAST.PAY_HEAD");
            pst.setInt(1, billno);
            pst.setString(2, offCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                allowdeduct.setAdname("PAY");
                allowdeduct.setObjecthead(rs.getString("PAY_HEAD"));
                allowdeduct.setAdamount(rs.getString("PAY_AMOUNT"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allowdeduct;
    }

    @Override
    public String getGrossForArrear(int billno) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String grossAmt = "";
        try {
            con = dataSource.getConnection();
            String oaQry = "SELECT sum(to_be_paid-already_paid) adamt FROM ARR_DTLS INNER JOIN ARR_MAST ON  ARR_DTLS.AQSL_NO = ARR_MAST.AQSL_NO WHERE BILL_NO =?";
            pst = con.prepareStatement(oaQry);
            pst.setInt(1, billno);
            rs = pst.executeQuery();
            if (rs.next()) {
                grossAmt = rs.getString("adamt");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return grossAmt;
    }

    public String getGrossForNPSArrear(int billno) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String grossAmt = "";
        try {
            con = dataSource.getConnection();
            String oaQry = "SELECT sum(cpf_head) adamt FROM ARR_MAST WHERE BILL_NO=?";
            pst = con.prepareStatement(oaQry);
            pst.setInt(1, billno);
            rs = pst.executeQuery();
            if (rs.next()) {
                grossAmt = rs.getString("adamt");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return grossAmt;
    }

    @Override
    public String getTotalDeductionForArrear(int billno) {

        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String dedamt = "";
        try {
            con = dataSource.getConnection();
            //String oaQry = "select sum(COALESCE(cpf_head,0)+COALESCE(pt,0)+COALESCE(inctax,0)+COALESCE(other_recovery,0)+COALESCE(ddo_recovery,0)) ded_amt from arr_mast where bill_no=?";
            String oaQry = "select sum(COALESCE(cpf_head,0)+COALESCE(pt,0)+COALESCE(inctax,0)+COALESCE(other_recovery,0)) ded_amt from arr_mast where bill_no=?";
            pst = con.prepareStatement(oaQry);
            pst.setInt(1, billno);
            rs = pst.executeQuery();
            if (rs.next()) {
                dedamt = rs.getString("ded_amt");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return dedamt;
    }

    public ArrayList getAllowanceListForArrearAQSLNOWISE(String aqslno) {
        ArrayList allowanceList = new ArrayList();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            String oaQry = "SELECT AD_TYPE,BT_ID,sum(to_be_paid-already_paid) adamt FROM ARR_DTLS  WHERE AQSL_NO = ? group by ad_TYPE,bt_id";
            pst = con.prepareStatement(oaQry);
            pst.setString(1, aqslno);
            rs = pst.executeQuery();
            while (rs.next()) {
                AllowDeductDetails allowdeduct = new AllowDeductDetails();
                allowdeduct.setAdname(rs.getString("AD_TYPE"));
                allowdeduct.setObjecthead(rs.getString("BT_ID"));
                allowdeduct.setAdamount(rs.getString("adamt"));
                allowanceList.add(allowdeduct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allowanceList;
    }

    @Override
    public ArrayList getAllowanceListForArrear(int billno, int aqMonth, int aqyear) {
        ArrayList allowanceList = new ArrayList();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            String oaQry = "select * from ( SELECT AD_TYPE,BT_ID,sum(to_be_paid-already_paid) adamt FROM ARR_DTLS INNER JOIN ARR_MAST ON  ARR_DTLS.AQSL_NO = ARR_MAST.AQSL_NO WHERE BILL_NO = ? group by ad_TYPE,bt_id) allw_ta where adamt!=0";
            pst = con.prepareStatement(oaQry);
            pst.setInt(1, billno);
            rs = pst.executeQuery();
            while (rs.next()) {
                AllowDeductDetails allowdeduct = new AllowDeductDetails();
                allowdeduct.setAdname(rs.getString("AD_TYPE"));
                allowdeduct.setObjecthead(rs.getString("BT_ID"));
                allowdeduct.setAdamount(rs.getString("adamt"));
                allowanceList.add(allowdeduct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allowanceList;
    }

    @Override
    public ArrayList getAllowanceListForComputerTokenReportArrear(int billno, int aqMonth, int aqyear) {
        ArrayList allowanceList = new ArrayList();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;

        int adamt = 0;
        int payamt = 0;
        try {
            con = dataSource.getConnection();
            String oaQry = "select * from ( SELECT AD_TYPE,BT_ID,sum(to_be_paid-already_paid) adamt FROM ARR_DTLS INNER JOIN ARR_MAST ON  ARR_DTLS.AQSL_NO = ARR_MAST.AQSL_NO WHERE BILL_NO = ? group by ad_TYPE,bt_id) allw_ta where adamt!=0";
            pst = con.prepareStatement(oaQry);
            pst.setInt(1, billno);
            rs = pst.executeQuery();
            while (rs.next()) {
                AllowDeductDetails allowdeduct = new AllowDeductDetails();
                adamt = rs.getInt("adamt");
                allowdeduct.setAdname(rs.getString("AD_TYPE"));
                allowdeduct.setObjecthead(rs.getString("BT_ID"));
                /*if (allowdeduct.getObjecthead() != null && allowdeduct.getObjecthead().equals("136")) {
                 adamt = payamt + adamt;
                 allowdeduct.setAdamount(adamt + "");
                 } else {
                 if(allowdeduct.getAdname() != null && allowdeduct.getAdname().equals("PAY")){
                 adamt = payamt + adamt;
                 allowdeduct.setAdamount(adamt + "");
                 }else{
                 allowdeduct.setAdamount(rs.getString("adamt"));
                 }
                 }*/
                if (allowdeduct.getAdname() != null && allowdeduct.getAdname().equals("PAY")) {
                    adamt = payamt + adamt;
                    allowdeduct.setAdamount(adamt + "");
                } else {
                    allowdeduct.setAdamount(rs.getString("adamt"));
                }
                if (adamt != 0) {
                    allowanceList.add(allowdeduct);
                } else {
                    payamt = payamt + adamt;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allowanceList;
    }

    @Override
    public ArrayList getDeductionListForArrear(int billno, int aqMonth, int aqYear) {
        ArrayList deductionList = new ArrayList();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;

        ResultSet rs2 = null;
        ResultSet rs3 = null;
        PreparedStatement pst2 = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select  sum(pt) as PT, sum(inctax) as IT, sum(other_recovery) as OTHER_RECOVERY_AMT,sum(ddo_recovery) as DDO_RECOVERY_AMT from arr_mast where bill_no=?");
            pst.setInt(1, billno);
            rs = pst.executeQuery();
            if (rs.next()) {
                AllowDeductDetails allowdeduct = new AllowDeductDetails();

                allowdeduct = new AllowDeductDetails();
                allowdeduct.setAdname("PT");
                allowdeduct.setObjecthead("3043");
                allowdeduct.setAdamount(rs.getString("PT"));
                deductionList.add(allowdeduct);

                allowdeduct = new AllowDeductDetails();
                allowdeduct.setAdname("IT");
                allowdeduct.setObjecthead("58816");
                allowdeduct.setAdamount(rs.getString("IT"));
                deductionList.add(allowdeduct);

                //allowdeduct.setObjecthead("59546");
                if (rs.getInt("OTHER_RECOVERY_AMT") > 0) {
                    allowdeduct = new AllowDeductDetails();
                    allowdeduct.setAdname("OR");
                    pst1 = con.prepareStatement("select  btid_or,sum(other_recovery) as OTHER_RECOVERY from arr_mast where bill_no=? group by btid_or having sum(other_recovery)>0");
                    pst1.setInt(1, billno);
                    rs3 = pst1.executeQuery();
                    if (rs3.next()) {
                        if ((rs3.getString("btid_or") != null && !rs3.getString("btid_or").equals(""))) {
                            allowdeduct.setObjecthead(rs3.getString("btid_or"));
                        } else {
                            allowdeduct.setObjecthead("59546");
                        }
                    }
                    allowdeduct.setAdamount(rs.getString("OTHER_RECOVERY_AMT"));
                    deductionList.add(allowdeduct);
                }

                /*if (rs.getInt("DDO_RECOVERY_AMT") > 0) {
                 allowdeduct = new AllowDeductDetails();
                 allowdeduct.setAdname("DR");
                 pst1 = con.prepareStatement("select  btid_dr,sum(ddo_recovery) as DDO_RECOVERY from arr_mast where bill_no=? group by btid_dr having sum(ddo_recovery)>0");
                 pst1.setInt(1, billno);
                 rs3 = pst1.executeQuery();
                 if (rs3.next()) {
                 if ((rs3.getString("btid_dr") != null && !rs3.getString("btid_dr").equals("")) && rs3.getInt("DDO_RECOVERY") > 0) {
                 allowdeduct.setObjecthead(rs3.getString("btid_dr"));
                 }
                 }
                 allowdeduct.setAdamount(rs.getString("DDO_RECOVERY_AMT"));
                 deductionList.add(allowdeduct);
                 //allowdeduct.setObjecthead("12345");
                 }*/
            }

            String gpfType = "";

            pst2 = con.prepareStatement("select gpf_type from arr_mast where bill_no=? and gpf_type='ISO'");
            pst2.setInt(1, billno);
            rs2 = pst2.executeQuery();
            if (rs2.next()) {
                gpfType = "ISO";
            }

            pst = con.prepareStatement("select sum(cpf_head) as CPF, ACCT_TYPE from arr_mast where bill_no=? GROUP BY ACCT_TYPE");
            pst.setInt(1, billno);
            rs = pst.executeQuery();
            while (rs.next()) {
                AllowDeductDetails allowdeduct = new AllowDeductDetails();
                String acctType = rs.getString("ACCT_TYPE");
                if (acctType != null && !acctType.equals("")) {
                    if (acctType.equals("PRAN")) {
                        allowdeduct.setAdname("CPF");
                        allowdeduct.setObjecthead("57740");
                    } else if (acctType.equals("TPF")) {
                        allowdeduct.setAdname("TPF");
                        allowdeduct.setObjecthead("55550");
                    } else {
                        if (gpfType != null && !gpfType.equals("")) {
                            allowdeduct.setAdname("GPF");
                            allowdeduct.setObjecthead("57649");
                        } else {
                            allowdeduct.setAdname("GPF");
                            allowdeduct.setObjecthead("55545");
                        }
                    }
                }

                allowdeduct.setAdamount(rs.getString("CPF"));
                deductionList.add(allowdeduct);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(rs2);
            DataBaseFunctions.closeSqlObjects(rs3, pst2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deductionList;
    }

    @Override
    public ArrayList getAllowanceList(int billno, int aqMonth, int aqYear) {
        AllowDeductDetails allowdeduct = null;
        ArrayList allowanceList = new ArrayList();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            String aqDTLS = "hrmis2.AQ_DTLS";

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(aqMonth, aqYear);
            // OLD QRY = "SELECT AD_CODE,BT_ID,SUM(AD_AMT) AD_AMT FROM AQ_DTLS INNER JOIN  (SELECT AQ_MAST.AQSL_NO FROM AQ_MAST WHERE  AQ_MAST.BILL_NO=? )AQ_MAST ON AQ_DTLS.AQSL_NO = AQ_MAST.AQSL_NO "
            //        + "WHERE AD_TYPE='A' AND AD_AMT > 0 GROUP BY AQ_DTLS.AD_CODE,BT_ID,NOW_DEDN ORDER BY AQ_DTLS.AD_CODE"
            String oaQry = "select a.ad_code,a.bt_id,sum(a.ad_amt) adamt from " + aqDTLS + " a,hrmis2.AQ_MAST b where a.AQSL_NO =b.AQSL_NO and b.aq_month= ? and b.aq_year= ? and a.ad_type = 'A' and a.ad_amt>0 and b.BILL_NO = ? group by a.ad_code,a.bt_id";
            pst = con.prepareStatement(oaQry);
            pst.setInt(1, aqMonth);
            pst.setInt(2, aqYear);
            pst.setInt(3, billno);
            rs = pst.executeQuery();
            while (rs.next()) {
                allowdeduct = new AllowDeductDetails();
                allowdeduct.setAdname(rs.getString("AD_CODE"));
                allowdeduct.setObjecthead(rs.getString("BT_ID"));
                allowdeduct.setAdamount(rs.getString("adamt"));
                allowanceList.add(allowdeduct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allowanceList;
    }

    @Override
    public ArrayList getDeductionList(int billno, int aqMonth, int aqYear) {
        AllowDeductDetails allowdeduct = null;
        ArrayList deductionList = new ArrayList();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String nowdedn = "";
        try {
            con = dataSource.getConnection();
            String aqDTLS = "hrmis2.AQ_DTLS";

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(aqMonth, aqYear);
            pst = con.prepareStatement("SELECT AD_CODE,SUM(AD_AMT) AD_AMT,NOW_DEDN,BT_ID FROM " + aqDTLS + " a INNER JOIN  (SELECT AQ_MAST.AQSL_NO FROM AQ_MAST WHERE  AQ_MONTH=? AND AQ_YEAR=? AND AQ_MAST.BILL_NO=?)AQ_MAST ON a.AQSL_NO = AQ_MAST.AQSL_NO "
                    + "WHERE AD_TYPE='D' AND  (SCHEDULE !='PVTL' AND SCHEDULE!='PVTD')  AND AD_AMT>0 GROUP BY a.AD_CODE,BT_ID,NOW_DEDN ORDER BY a.AD_CODE");
            pst.setInt(1, aqMonth);
            pst.setInt(2, aqYear);
            pst.setInt(3, billno);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getString("AD_AMT") != null && !rs.getString("AD_AMT").equals("")) {
                    nowdedn = rs.getString("NOW_DEDN");
                    allowdeduct = new AllowDeductDetails();
                    if (nowdedn == null) {
                        allowdeduct.setAdname(rs.getString("AD_CODE"));
                    } else {
                        allowdeduct.setAdname(rs.getString("AD_CODE") + "-" + nowdedn);
                    }
                    allowdeduct.setObjecthead(rs.getString("BT_ID"));
                    allowdeduct.setAdamount(rs.getString("AD_AMT"));
                    deductionList.add(allowdeduct);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deductionList;
    }

    @Override
    public ArrayList getPvtLoanList(int billno, int aqMonth, int aqYear) {
        AllowDeductDetails allowdeduct = null;
        ArrayList pvtDeductionList = new ArrayList();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try {
            String aqdtlsTbl = AqFunctionalities.getAQBillDtlsTable(aqMonth, aqYear);
            con = dataSource.getConnection();
            /*pst = con.prepareStatement("SELECT AD_CODE,SUM(AD_AMT) AD_AMT FROM AQ_DTLS INNER JOIN  (SELECT AQ_MAST.AQSL_NO FROM AQ_MAST WHERE  AQ_MAST.BILL_NO=?)AQ_MAST ON AQ_DTLS.AQSL_NO = AQ_MAST.AQSL_NO "
             + "WHERE (SCHEDULE ='PVTL' OR SCHEDULE ='PVTD') AND AD_AMT>0 GROUP BY AQ_DTLS.AD_CODE,BT_ID,NOW_DEDN ORDER BY AQ_DTLS.AD_CODE");*/
            pst = con.prepareStatement("SELECT AD_CODE,SUM(AD_AMT) AS AD_AMT FROM AQ_MAST"
                    + " INNER JOIN " + aqdtlsTbl + " AQ_DTLS ON AQ_MAST.AQSL_NO=AQ_DTLS.AQSL_NO"
                    + " WHERE AQ_MAST.BILL_NO=? AND AQ_MAST.AQ_YEAR=? AND AQ_MAST.AQ_MONTH=? AND (SCHEDULE ='PVTL' OR SCHEDULE ='PVTD') AND AD_AMT>0 GROUP BY AQ_DTLS.AD_CODE,BT_ID,NOW_DEDN ORDER BY AQ_DTLS.AD_CODE");
            pst.setInt(1, billno);
            pst.setInt(2, aqYear);
            pst.setInt(3, aqMonth);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getString("AD_AMT") != null && !rs.getString("AD_AMT").equals("")) {
                    allowdeduct = new AllowDeductDetails();
                    allowdeduct.setAdname(rs.getString("AD_CODE"));
                    allowdeduct.setAdamount(rs.getString("AD_AMT"));
                    pvtDeductionList.add(allowdeduct);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pvtDeductionList;
    }

    @Override
    public void reprocessSingleBill(BillBrowserbean bbbean, Office off) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            boolean ifexist = false;
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT TASK_ID FROM PAYBILL_TASK WHERE BILL_ID=? and off_code=?");
            pstmt.setInt(1, Integer.parseInt(bbbean.getBillNo()));
            pstmt.setString(2, bbbean.getOffCode());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                ifexist = true;
            }
            if (!ifexist) {

                pstmt = con.prepareStatement("select COALESCE(max(task_id),0)+1 taskid FROM paybill_task");
                rs = pstmt.executeQuery();
                int taskid = 0;
                if (rs.next()) {
                    taskid = rs.getInt("TASKID");
                }
                int totalNoOfEmployee = 0;
                pstmt = con.prepareStatement("INSERT INTO PAYBILL_TASK (TASK_ID,OFF_CODE,AQ_MONTH,AQ_YEAR,TOTAL_AQ,BILL_ID,BILL_GROUP_ID,PRIORITY,bill_type) VALUES (" + taskid + ",'" + bbbean.getOffCode() + "'," + bbbean.getSltMonth() + "," + bbbean.getSltYear() + "," + totalNoOfEmployee + "," + bbbean.getBillNo() + ",'" + bbbean.getBgid() + "'," + bbbean.getPriority() + ", '" + bbbean.getTxtbilltype() + "')");
                pstmt.executeUpdate();

                pstmt = con.prepareStatement("UPDATE BILL_MAST SET IS_BILL_PREPARED = 'N', tr_code=?, ddo_code=?,  ddo_empid=?,  ddo_name=?, ddo_post_name=?  WHERE BILL_NO=?");
                pstmt.setString(1, off.getTrCode());
                pstmt.setString(2, off.getDdoCode());
                pstmt.setString(3, off.getDdoHrmsid());
                pstmt.setString(4, off.getDdoName());
                pstmt.setString(5, off.getDdoPostName());
                pstmt.setInt(6, Integer.parseInt(bbbean.getBillNo()));
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateBillData(BillBrowserbean bbbean, String typeOfBill) {
        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pst2 = null;

        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;

        ResultSet resultSet = null;
        ResultSet rs2 = null;

        ResultSet rs = null;
        int res = 0;
        int retVal = 1;
        boolean ret = false;
        String isError = "N";
        String status = "";
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        PreparedStatement pstmtselect = null;
        ResultSet rsselect = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        String acctType = "GPF";
        try {
            Date outputDate = dateFormat.parse(bbbean.getBillDate());
            con = dataSource.getConnection();

            status = verifyBillNoandBenRefNo(bbbean.getOffCode(), bbbean.getBillNo(), bbbean.getSltYear(), bbbean.getSltMonth());

            String aqDtlsTable = AqFunctionalities.getAQBillDtlsTable(bbbean.getSltMonth(), bbbean.getSltYear());

            if (status != null && status.equals("Y")) {
                if (typeOfBill != null && typeOfBill.equals("PAY")) {
                    pst = con.prepareStatement("UPDATE BILL_MAST SET GROSS_AMT = getbillgrossamt_1(BILL_NO) WHERE BILL_NO = ? ");
                    pst.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    pst.executeUpdate();

                    pst = con.prepareStatement("UPDATE BILL_MAST SET ded_amt = getbilldedamt(BILL_NO) WHERE BILL_NO = ? ");
                    pst.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    pst.executeUpdate();

                    pst2 = con.prepareStatement("SELECT emp_mast.emp_id, aq_MAST.aqsl_no, emp_mast.if_maintenance_deduct FROM aq_MAST "
                            + " inner join  emp_mast on  aq_MAST.emp_code=emp_mast.emp_id "
                            + " WHERE BILL_NO=? and emp_mast.if_maintenance_deduct='Y' ");
                    pst2.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    rs = pst2.executeQuery();
                    while (rs.next()) {
                        AqmastModel aq = new AqmastModel();
                        aq.setBillNo(Integer.parseInt(bbbean.getBillNo()));
                        aq.setAqSlNo(rs.getString("aqsl_no"));
                        aq.setEmpCode(rs.getString("emp_id"));
                        addMaintenanceData(aq, typeOfBill, aqDtlsTable);
                    }

                } else if (typeOfBill.equals("OTHER_ARREAR")) {
                    pstmt1 = con.prepareStatement("update arr_mast set full_arrear_pay = getgross_arrear(aqsl_no) WHERE bill_no = ? ");
                    pstmt1.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    pstmt1.executeUpdate();

                    pstmt1 = con.prepareStatement("update arr_mast set arrear_pay = full_arrear_pay WHERE bill_no = ? ");
                    pstmt1.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    pstmt1.executeUpdate();

                    /*pstmt2 = con.prepareStatement("UPDATE ARR_MAST SET cpf_head=? WHERE AQSL_NO=?");

                     pstmt1 = con.prepareStatement("SELECT AQSL_NO,ROUND(arrear_pay*0.1) AS CPFAMT FROM ARR_MAST "
                     + "INNER JOIN EMP_MAST ON ARR_MAST.emp_id=EMP_MAST.emp_id "
                     + "LEFT OUTER JOIN (SELECT * FROM update_ad_info WHERE REF_AD_CODE='76') AS update_ad_info ON ARR_MAST.emp_id = update_ad_info.updation_ref_code "
                     + "WHERE BILL_NO=? AND EMP_MAST.acct_type='PRAN' AND FIXEDVALUE IS NULL");
                     pstmt1.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                     resultSet = pstmt1.executeQuery();
                     while (resultSet.next()) {
                     pstmt2.setInt(1, resultSet.getInt("CPFAMT"));
                     pstmt2.setString(2, resultSet.getString("AQSL_NO"));
                     pstmt2.executeUpdate();

                     }*/
                    pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET GROSS_AMT = getbilltotgross_arrear(BILL_NO) WHERE BILL_NO = ? ");
                    pstmt1.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    pstmt1.executeUpdate();

                    pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET DED_AMT = getbilltotded_arrear(BILL_NO) WHERE BILL_NO = ? ");
                    pstmt1.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    res = pstmt1.executeUpdate();

                    pst = con.prepareStatement("UPDATE arr_mast SET gross_amt = getempgrosstotal_arrear(aqsl_no) WHERE bill_no=?");
                    pst.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    pst.executeUpdate();

                    pst = con.prepareStatement("UPDATE arr_mast SET ded_amt = getempdedtotal_arrear(aqsl_no) WHERE bill_no=?");
                    pst.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    pst.executeUpdate();

                    pst2 = con.prepareStatement("SELECT emp_mast.emp_id, arr_mast.aqsl_no, emp_mast.if_maintenance_deduct FROM ARR_MAST "
                            + " inner join  emp_mast on  arr_mast.emp_id=emp_mast.emp_id "
                            + " WHERE BILL_NO=? and emp_mast.if_maintenance_deduct='Y' ");
                    pst2.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    rs = pst2.executeQuery();
                    while (rs.next()) {
                        AqmastModel aq = new AqmastModel();
                        aq.setBillNo(Integer.parseInt(bbbean.getBillNo()));
                        aq.setAqSlNo(rs.getString("aqsl_no"));
                        aq.setEmpCode(rs.getString("emp_id"));
                        addMaintenanceData(aq, typeOfBill, aqDtlsTable);
                    }

                    DataBaseFunctions.closeSqlObjects(rs, pst2);
                    pst = con.prepareStatement("select emp_mast.acct_type, emp_mast.bank_acc_no, aqsl_no, g_branch.ifsc_code, g_branch.branch_code, g_branch.bank_code  from arr_mast "
                            + " inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id "
                            + " left outer join g_branch on emp_mast.branch_code=g_branch.branch_code "
                            + " where bill_no=? ");
                    pst.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    rs = pst.executeQuery();

                    pst2 = con.prepareStatement("update arr_mast set acct_type=?, bank_acc_no=?, ifsc_code=?, branch_name=?, bank_name=? where aqsl_no=?");

                    while (rs.next()) {
                        pst2.setString(1, rs.getString("acct_type"));
                        pst2.setString(2, rs.getString("bank_acc_no"));
                        pst2.setString(3, rs.getString("ifsc_code"));
                        pst2.setString(4, rs.getString("branch_code"));
                        pst2.setString(5, rs.getString("bank_code"));
                        pst2.setString(6, rs.getString("aqsl_no"));
                        pst2.executeUpdate();
                    }

                } else if (typeOfBill.equals("ARREAR")) {
                    pstmt1 = con.prepareStatement("UPDATE arr_mast set full_arrear_pay = getgross_arrear(aqsl_no) WHERE bill_no = ? ");
                    pstmt1.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    pstmt1.executeUpdate();

                    String sql = "select full_arrear_pay,arrear_pay,aqsl_no,emp_mast.acct_type from arr_mast "
                            + " inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id"
                            + " where bill_no=? and full_arrear_pay <> arrear_pay";
                    pstmtselect = con.prepareStatement(sql);
                    pstmtselect.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    rsselect = pstmtselect.executeQuery();
                    while (rsselect.next()) {
                        String aqslno = rsselect.getString("aqsl_no");

                        pstmt1 = con.prepareStatement("UPDATE arr_mast set acct_type=?,arrear_pay = round(full_arrear_pay*0.4) WHERE aqsl_no = ? ");
                        pstmt1.setString(1, rsselect.getString("acct_type"));
                        pstmt1.setString(2, aqslno);
                        pstmt1.executeUpdate();

                        pst = con.prepareStatement("UPDATE arr_mast SET gross_amt = getempgrosstotal_arrear(aqsl_no) WHERE aqsl_no=?");
                        pst.setString(1, aqslno);
                        pst.executeUpdate();

                        pst = con.prepareStatement("UPDATE arr_mast SET ded_amt = getempdedtotal_arrear(aqsl_no) WHERE aqsl_no=?");
                        pst.setString(1, aqslno);
                        pst.executeUpdate();
                    }

                    sql = "select full_arrear_pay,arrear_pay,aqsl_no,emp_mast.acct_type from arr_mast "
                            + " inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id"
                            + " where bill_no=? and full_arrear_pay = arrear_pay";
                    pstmtselect = con.prepareStatement(sql);
                    pstmtselect.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    rsselect = pstmtselect.executeQuery();
                    while (rsselect.next()) {
                        String aqslno = rsselect.getString("aqsl_no");

                        pst = con.prepareStatement("UPDATE arr_mast SET acct_type=?,gross_amt = getempgrosstotal_arrear(aqsl_no) WHERE aqsl_no=?");
                        pst.setString(1, rsselect.getString("acct_type"));
                        pst.setString(2, aqslno);
                        pst.executeUpdate();

                        pst = con.prepareStatement("UPDATE arr_mast SET ded_amt = getempdedtotal_arrear(aqsl_no) WHERE aqsl_no=?");
                        pst.setString(1, aqslno);
                        pst.executeUpdate();
                    }

                    pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET GROSS_AMT = getbilltotgross_arrear(BILL_NO) WHERE BILL_NO = ? ");
                    pstmt1.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    pstmt1.executeUpdate();

                    pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET DED_AMT = getbilltotded_arrear(BILL_NO) WHERE BILL_NO = ? ");
                    pstmt1.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    pstmt1.executeUpdate();

                    pst2 = con.prepareStatement("SELECT emp_mast.emp_id, arr_mast.aqsl_no, emp_mast.if_maintenance_deduct FROM ARR_MAST "
                            + " inner join  emp_mast on  arr_mast.emp_id=emp_mast.emp_id "
                            + " WHERE BILL_NO=? and emp_mast.if_maintenance_deduct='Y' ");
                    pst2.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    rs = pst2.executeQuery();
                    while (rs.next()) {
                        AqmastModel aq = new AqmastModel();
                        aq.setBillNo(Integer.parseInt(bbbean.getBillNo()));
                        aq.setAqSlNo(rs.getString("aqsl_no"));
                        aq.setEmpCode(rs.getString("emp_id"));
                        addMaintenanceData(aq, typeOfBill, aqDtlsTable);
                    }

                } else if (typeOfBill.equals("ARREAR_J")) {
                    pstmt1 = con.prepareStatement("UPDATE arr_mast set full_arrear_pay = getgross_arrear(aqsl_no) WHERE bill_no = ? ");
                    pstmt1.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    pstmt1.executeUpdate();

                    String sql = "select full_arrear_pay,arrear_pay,aqsl_no,emp_mast.acct_type from arr_mast "
                            + " inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id"
                            + " where bill_no=? and full_arrear_pay <> arrear_pay";
                    pstmtselect = con.prepareStatement(sql);
                    pstmtselect.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    rsselect = pstmtselect.executeQuery();
                    while (rsselect.next()) {
                        String aqslno = rsselect.getString("aqsl_no");

                        pstmt1 = con.prepareStatement("UPDATE arr_mast set acct_type=?,arrear_pay = round(full_arrear_pay*0.25) WHERE aqsl_no = ? ");
                        pstmt1.setString(1, rsselect.getString("acct_type"));
                        pstmt1.setString(2, aqslno);
                        pstmt1.executeUpdate();

                        pst = con.prepareStatement("UPDATE arr_mast SET gross_amt = getempgrosstotal_arrear(aqsl_no) WHERE aqsl_no=?");
                        pst.setString(1, aqslno);
                        pst.executeUpdate();

                        pst = con.prepareStatement("UPDATE arr_mast SET ded_amt = getempdedtotal_arrear(aqsl_no) WHERE aqsl_no=?");
                        pst.setString(1, aqslno);
                        pst.executeUpdate();
                    }

                    sql = "select full_arrear_pay,arrear_pay,aqsl_no,emp_mast.acct_type from arr_mast "
                            + " inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id"
                            + " where bill_no=? and full_arrear_pay = arrear_pay";
                    pstmtselect = con.prepareStatement(sql);
                    pstmtselect.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    rsselect = pstmtselect.executeQuery();
                    while (rsselect.next()) {
                        String aqslno = rsselect.getString("aqsl_no");

                        pst = con.prepareStatement("UPDATE arr_mast SET acct_type=?,gross_amt = getempgrosstotal_arrear(aqsl_no) WHERE aqsl_no=?");
                        pst.setString(1, rsselect.getString("acct_type"));
                        pst.setString(2, aqslno);
                        pst.executeUpdate();

                        pst = con.prepareStatement("UPDATE arr_mast SET ded_amt = getempdedtotal_arrear(aqsl_no) WHERE aqsl_no=?");
                        pst.setString(1, aqslno);
                        pst.executeUpdate();
                    }

                    pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET GROSS_AMT = getbilltotgross_arrear(BILL_NO) WHERE BILL_NO = ? ");
                    pstmt1.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    pstmt1.executeUpdate();

                    pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET DED_AMT = getbilltotded_arrear(BILL_NO) WHERE BILL_NO = ? ");
                    pstmt1.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    pstmt1.executeUpdate();

                    pst2 = con.prepareStatement("SELECT emp_mast.emp_id, arr_mast.aqsl_no, emp_mast.if_maintenance_deduct FROM ARR_MAST "
                            + " inner join  emp_mast on  arr_mast.emp_id=emp_mast.emp_id "
                            + " WHERE BILL_NO=? and emp_mast.if_maintenance_deduct='Y' ");
                    pst2.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    rs = pst2.executeQuery();
                    while (rs.next()) {
                        AqmastModel aq = new AqmastModel();
                        aq.setBillNo(Integer.parseInt(bbbean.getBillNo()));
                        aq.setAqSlNo(rs.getString("aqsl_no"));
                        aq.setEmpCode(rs.getString("emp_id"));
                        addMaintenanceData(aq, typeOfBill, aqDtlsTable);
                    }

                } else if (typeOfBill.equals("ARREAR_6_J")) {
                    String sql = "select full_arrear_pay,arrear_pay,aqsl_no,emp_mast.acct_type from arr_mast "
                            + " inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id"
                            + " where bill_no=? and full_arrear_pay <> arrear_pay";
                    pstmtselect = con.prepareStatement(sql);
                    pstmtselect.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    rsselect = pstmtselect.executeQuery();
                    while (rsselect.next()) {
                        String aqslno = rsselect.getString("aqsl_no");

                        pstmt1 = con.prepareStatement("UPDATE arr_mast set acct_type=? WHERE aqsl_no = ? ");
                        pstmt1.setString(1, rsselect.getString("acct_type"));
                        pstmt1.setString(2, aqslno);
                        pstmt1.executeUpdate();

                        pst = con.prepareStatement("UPDATE arr_mast SET gross_amt = getempgrosstotal_arrear(aqsl_no) WHERE aqsl_no=?");
                        pst.setString(1, aqslno);
                        pst.executeUpdate();

                        pst = con.prepareStatement("UPDATE arr_mast SET ded_amt = getempdedtotal_arrear(aqsl_no) WHERE aqsl_no=?");
                        pst.setString(1, aqslno);
                        pst.executeUpdate();
                    }

                    sql = "select full_arrear_pay,arrear_pay,aqsl_no,emp_mast.acct_type from arr_mast "
                            + " inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id"
                            + " where bill_no=? and full_arrear_pay = arrear_pay";
                    pstmtselect = con.prepareStatement(sql);
                    pstmtselect.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    rsselect = pstmtselect.executeQuery();
                    while (rsselect.next()) {
                        String aqslno = rsselect.getString("aqsl_no");

                        pst = con.prepareStatement("UPDATE arr_mast SET acct_type=?,gross_amt = getempgrosstotal_arrear(aqsl_no) WHERE aqsl_no=?");
                        pst.setString(1, rsselect.getString("acct_type"));
                        pst.setString(2, aqslno);
                        pst.executeUpdate();

                        pst = con.prepareStatement("UPDATE arr_mast SET ded_amt = getempdedtotal_arrear(aqsl_no) WHERE aqsl_no=?");
                        pst.setString(1, aqslno);
                        pst.executeUpdate();
                    }

                    pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET GROSS_AMT = getbilltotgross_arrear(BILL_NO) WHERE BILL_NO = ? ");
                    pstmt1.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    pstmt1.executeUpdate();

                    pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET DED_AMT = getbilltotded_arrear(BILL_NO) WHERE BILL_NO = ? ");
                    pstmt1.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    pstmt1.executeUpdate();

                    pst2 = con.prepareStatement("SELECT emp_mast.emp_id, arr_mast.aqsl_no, emp_mast.if_maintenance_deduct FROM ARR_MAST "
                            + " inner join  emp_mast on  arr_mast.emp_id=emp_mast.emp_id "
                            + " WHERE BILL_NO=? and emp_mast.if_maintenance_deduct='Y' ");
                    pst2.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    rs = pst2.executeQuery();
                    while (rs.next()) {
                        AqmastModel aq = new AqmastModel();
                        aq.setBillNo(Integer.parseInt(bbbean.getBillNo()));
                        aq.setAqSlNo(rs.getString("aqsl_no"));
                        aq.setEmpCode(rs.getString("emp_id"));
                        addMaintenanceData(aq, typeOfBill, aqDtlsTable);
                    }

                } else if (typeOfBill.equals("ARREAR_6")) {
                    int arrPercent = 0;
                    pst2 = con.prepareStatement("SELECT arrear_percent from bill_mast WHERE bill_no = ?");
                    pst2.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    rs = pst2.executeQuery();
                    if (rs.next()) {
                        arrPercent = rs.getInt("arrear_percent");
                    }

                    pstmt1 = con.prepareStatement("UPDATE arr_mast set full_arrear_pay = getgross_arrear(aqsl_no) WHERE bill_no = ? ");
                    pstmt1.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    pstmt1.executeUpdate();

                    double percent = 0.0;
                    if (arrPercent == 10) {
                        percent = 0.1;
                    } else if (arrPercent == 50) {
                        percent = 0.5;
                    } else if (arrPercent == 60) {
                        percent = 0.6;
                    } else if (arrPercent == 30) {
                        percent = 0.3;
                    } else if (arrPercent == 20) {
                        pst1 = con.prepareStatement("select ACCT_TYPE from arr_mast where bill_no=? GROUP BY ACCT_TYPE");
                        pst1.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                        rs1 = pst1.executeQuery();
                        if (rs1.next()) {
                            acctType = rs1.getString("ACCT_TYPE");
                        }
                        if (acctType.equals("PRAN")) {
                            percent = 0.15;

                            pstmt1 = con.prepareStatement("UPDATE arr_mast set arrear_pay = full_arrear_pay*" + percent + " from emp_mast WHERE bill_no = ? and arr_mast.emp_id=emp_mast.emp_id and emp_mast.if_gpf_assumed='N'");
                            pstmt1.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                            pstmt1.executeUpdate();

                            percent = 0.2;

                            pstmt1 = con.prepareStatement("UPDATE arr_mast set arrear_pay = full_arrear_pay*" + percent + " from emp_mast WHERE bill_no = ? and arr_mast.emp_id=emp_mast.emp_id and emp_mast.if_gpf_assumed='Y'");
                            pstmt1.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                            pstmt1.executeUpdate();

                        } else {
                            percent = 0.2;
                            if (bbbean.getBillNo().equals("54449582")) {
                                percent = 0.05;
                            }
                            pstmt1 = con.prepareStatement("UPDATE arr_mast set arrear_pay = full_arrear_pay*" + percent + " WHERE bill_no = ? ");
                            pstmt1.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                            pstmt1.executeUpdate();
                        }
                    }

                    if (arrPercent != 20) {
                        pstmt1 = con.prepareStatement("UPDATE arr_mast set arrear_pay = full_arrear_pay*" + percent + " WHERE bill_no = ? ");
                        pstmt1.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                        pstmt1.executeUpdate();
                    }

                    pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET GROSS_AMT = getbilltotgross_arrear(BILL_NO) WHERE BILL_NO = ? ");
                    pstmt1.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    pstmt1.executeUpdate();

                    pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET DED_AMT = getbilltotded_arrear(BILL_NO) WHERE BILL_NO = ? ");
                    pstmt1.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    res = pstmt1.executeUpdate();

                    pst = con.prepareStatement("UPDATE arr_mast SET gross_amt = getempgrosstotal_arrear(aqsl_no) WHERE bill_no=?");
                    pst.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    pst.executeUpdate();

                    pst = con.prepareStatement("UPDATE arr_mast SET ded_amt = getempdedtotal_arrear(aqsl_no) WHERE bill_no=?");
                    pst.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    pst.executeUpdate();

                    pst2 = con.prepareStatement("SELECT emp_mast.emp_id, arr_mast.aqsl_no, emp_mast.if_maintenance_deduct FROM ARR_MAST "
                            + " inner join  emp_mast on  arr_mast.emp_id=emp_mast.emp_id "
                            + " WHERE BILL_NO=? and emp_mast.if_maintenance_deduct='Y' ");
                    pst2.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                    rs = pst2.executeQuery();
                    while (rs.next()) {
                        AqmastModel aq = new AqmastModel();
                        aq.setBillNo(Integer.parseInt(bbbean.getBillNo()));
                        aq.setAqSlNo(rs.getString("aqsl_no"));
                        aq.setEmpCode(rs.getString("emp_id"));
                        addMaintenanceData(aq, typeOfBill, aqDtlsTable);
                    }

                }

                /*
                
                 else if (typeOfBill != null && typeOfBill.contains("ARREAR")) {
                 pst = con.prepareStatement("UPDATE BILL_MAST SET GROSS_AMT = getbilltotgross_arrear(BILL_NO) WHERE BILL_NO = ? ");
                 pst.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                 pst.executeUpdate();

                 pst = con.prepareStatement("UPDATE BILL_MAST SET DED_AMT = getbilltotded_arrear(BILL_NO) WHERE BILL_NO = ? ");
                 pst.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                 pst.executeUpdate();

                 pst = con.prepareStatement("UPDATE arr_mast SET gross_amt = getempgrosstotal_arrear(aqsl_no) WHERE bill_no=?");
                 pst.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                 pst.executeUpdate();

                 pst = con.prepareStatement("UPDATE arr_mast SET ded_amt = getempdedtotal_arrear(aqsl_no) WHERE bill_no=?");
                 pst.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                 pst.executeUpdate();

                 pst2 = con.prepareStatement("SELECT emp_mast.emp_id, arr_mast.aqsl_no, emp_mast.if_maintenance_deduct FROM ARR_MAST "
                 + " inner join  emp_mast on  arr_mast.emp_id=emp_mast.emp_id "
                 + " WHERE BILL_NO=? and emp_mast.if_maintenance_deduct='Y' ");
                 pst2.setInt(1, Integer.parseInt(bbbean.getBillNo()));
                 rs = pst2.executeQuery();
                 while (rs.next()) {
                 AqmastModel aq = new AqmastModel();
                 aq.setBillNo(Integer.parseInt(bbbean.getBillNo()));
                 aq.setAqSlNo(rs.getString("aqsl_no"));
                 aq.setEmpCode(rs.getString("emp_id"));
                 
                 addMaintenanceData(aq, typeOfBill);
                 }

                 }
                 */
                pst = con.prepareStatement("UPDATE BILL_MAST SET BILL_DESC=?,TR_CODE=?,BEN_REF_NO=?, BILL_DATE=?,VCH_NO=?,VCH_DATE=?, co_code=?, ddo_code=? WHERE BILL_NO=?");
                pst.setString(1, bbbean.getBilldesc());
                pst.setString(2, bbbean.getTreasury());
                pst.setString(3, bbbean.getBenificiaryNumber());
                pst.setTimestamp(4, new Timestamp(outputDate.getTime()));
                pst.setString(5, bbbean.getVchNo());
                if (bbbean.getVchDt() != null && !bbbean.getVchDt().equals("")) {
                    outputDate = dateFormat.parse(bbbean.getVchDt());
                    pst.setTimestamp(6, new Timestamp(outputDate.getTime()));
                } else {
                    pst.setTimestamp(6, null);
                }
                /*if (bbbean.getStatus() == 5) {
                 if (bbbean.getVchNo() != null && !bbbean.getVchNo().equals("")) {
                 pst.setInt(7, 7);
                 } else {
                 pst.setInt(7, bbbean.getStatus());
                 }
                 } else {
                 pst.setInt(7, bbbean.getStatus());
                 }*/
                pst.setString(7, bbbean.getSltCOList());
                pst.setString(8, bbbean.getSltddoCode());
                pst.setInt(9, Integer.parseInt(bbbean.getBillNo()));
                retVal = pst.executeUpdate();
            } else if (status != null && status.equals("DBN")) {
                isError = "DBN";
            } else if (status != null && status.equals("DBR")) {
                isError = "DBR";
            }

            insertBeneficiaryDataForBill(Integer.parseInt(bbbean.getBillNo()), typeOfBill);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rsselect, pstmtselect);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void addMaintenanceData(AqmastModel aqMastModel, String typeOfBill, String aqDtlsTable) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pst = null;
        PreparedStatement pst2 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        int netAmt = 0;
        int benfAmt = 0;
        int totAmt = 0;
        try {
            con = dataSource.getConnection();

            pst2 = con.prepareStatement("delete from other_beneficiary where ref_emp_id=? and bill_ID=? ");
            pst2.setString(1, aqMastModel.getEmpCode());
            pst2.setInt(2, aqMastModel.getBillNo());
            pst2.executeUpdate();

            pst2 = con.prepareStatement("insert into other_beneficiary (bill_id, ref_emp_id, beneficiary_name, aq_month, aq_year, acct_type, acct_no ,ifsc_code, bank_name, branch_name, net_amt, ref_off_code, aqsl_no, rel_slno) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
            if (typeOfBill.equals("PAY")) {
                pst = con.prepareStatement("select getempgrosstotal('" + aqMastModel.getAqSlNo() + "','" + aqDtlsTable + "') gross, getempdedtotal('" + aqMastModel.getAqSlNo() + "','" + aqDtlsTable + "') dedn, getemppvtdedtotal('" + aqMastModel.getAqSlNo() + "','" + aqDtlsTable + "') pvtdedn ");
                rs2 = pst.executeQuery();
                if (rs2.next()) {
                    netAmt = rs2.getInt("gross") - rs2.getInt("dedn");
                }
            } else if (typeOfBill != null && typeOfBill.contains("ARREAR")) {
                pst = con.prepareStatement("select getempgrosstotal_arrear('" + aqMastModel.getAqSlNo() + "') gross, getempdedtotal_arrear('" + aqMastModel.getAqSlNo() + "') dedn");
                rs2 = pst.executeQuery();
                if (rs2.next()) {
                    netAmt = rs2.getInt("gross") - rs2.getInt("dedn");
                }
            }

            pstmt = con.prepareStatement("SELECT BANK_ACC_NO, G_BRANCH.BANK_CODE, G_BRANCH.BRANCH_CODE, G_BRANCH.IFSC_CODE, AMOUNT_TYPE, FIXED_AMOUNT, FORMULA, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, SL_NO  FROM EMP_RELATION "
                    + " INNER JOIN G_BRANCH ON EMP_RELATION.BRANCH_CODE=G_BRANCH.BRANCH_CODE "
                    + " WHERE EMP_ID=? AND AMOUNT_TYPE IS NOT NULL");

            pstmt.setString(1, aqMastModel.getEmpCode());
            rs1 = pstmt.executeQuery();
            while (rs1.next()) {
                if (rs1.getString("AMOUNT_TYPE").equals("1")) {
                    int percentage = Integer.parseInt(rs1.getString("FORMULA"));
                    if (percentage == 10) {
                        benfAmt = new Long(Math.round(netAmt * 0.10)).intValue();
                    } else if (percentage == 15) {
                        benfAmt = new Long(Math.round(netAmt * 0.15)).intValue();
                    } else if (percentage == 20) {
                        benfAmt = new Long(Math.round(netAmt * 0.20)).intValue();
                    } else if (percentage == 25) {
                        benfAmt = new Long(Math.round(netAmt * 0.25)).intValue();
                    } else if (percentage == 30) {
                        benfAmt = new Long(Math.round(netAmt * 0.30)).intValue();
                    } else if (percentage == 35) {
                        benfAmt = new Long(Math.round(netAmt * 0.35)).intValue();
                    } else if (percentage == 40) {
                        benfAmt = new Long(Math.round(netAmt * 0.40)).intValue();
                    } else if (percentage == 45) {
                        benfAmt = new Long(Math.round(netAmt * 0.45)).intValue();
                    } else if (percentage == 50) {
                        benfAmt = new Long(Math.round(netAmt * 0.50)).intValue();
                    }

                } else if (rs1.getString("AMOUNT_TYPE").equals("0")) {
                    benfAmt = rs1.getInt("FIXED_AMOUNT");
                }

                totAmt = totAmt + benfAmt;
                pst2.setInt(1, aqMastModel.getBillNo());
                pst2.setString(2, aqMastModel.getEmpCode());
                pst2.setString(3, rs1.getString("EMPNAME"));
                pst2.setInt(4, aqMastModel.getAqMonth());
                pst2.setInt(5, aqMastModel.getAqYear());
                pst2.setString(6, "S");
                pst2.setString(7, rs1.getString("BANK_ACC_NO"));
                pst2.setString(8, rs1.getString("ifsc_code"));
                pst2.setString(9, rs1.getString("bank_code"));
                pst2.setString(10, rs1.getString("BRANCH_CODE"));
                pst2.setInt(11, benfAmt);
                pst2.setString(12, aqMastModel.getOffCode());
                pst2.setString(13, aqMastModel.getAqSlNo());
                pst2.setInt(14, rs1.getInt("SL_NO"));

                if (totAmt <= netAmt) {
                    pst2.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String verifyBillNoandBenRefNo(String offCode, String billId, int year, int month) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = null;

        String isSaving = "Y";
        try {
            con = dataSource.getConnection();
            String sql = "SELECT * FROM BILL_MAST WHERE AQ_YEAR=? AND AQ_MONTH=? AND OFF_CODE=?  AND BILL_NO!=?";

            stmt = con.prepareStatement(sql);
            stmt.setInt(1, year);
            stmt.setInt(2, month);
            stmt.setString(3, offCode);
            stmt.setInt(4, Integer.parseInt(billId));
            rs = stmt.executeQuery();
            if (rs.next()) {
                isSaving = "DBN";
            } else {

                sql = "SELECT * FROM BILL_MAST WHERE AQ_YEAR=? AND AQ_MONTH=? AND OFF_CODE=? AND BILL_NO!=?";
                stmt = con.prepareStatement(sql);
                stmt.setInt(1, year);
                stmt.setInt(2, month);
                stmt.setString(3, offCode);
                stmt.setInt(4, Integer.parseInt(billId));
                rs = stmt.executeQuery();
                if (rs.next()) {
                    isSaving = "DBR";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isSaving;
    }

    @Override
    public void updateBillChartofAccount(int billno, BillBrowserbean bean) {
        String query = "";
        String chartAcc = "";
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {

            con = dataSource.getConnection();
            query = "UPDATE BILL_MAST SET DEMAND_NO=?, MAJOR_HEAD=?, SUB_MAJOR_HEAD=?, MINOR_HEAD=?, SUB_MINOR_HEAD1=?, SUB_MINOR_HEAD2=?, SUB_MINOR_HEAD3=?, PLAN=?, SECTOR=? WHERE BILL_NO=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, bean.getTxtDemandno());
            pstmt.setString(2, bean.getTxtmajcode());
            pstmt.setString(3, bean.getSubmajcode());
            pstmt.setString(4, bean.getTxtmincode());
            pstmt.setString(5, bean.getSubmincode1());
            pstmt.setString(6, bean.getSubmincode2());
            pstmt.setString(7, bean.getSubmincode3());
            pstmt.setString(8, bean.getPlanCode());
            pstmt.setString(9, bean.getSectorCode());
            pstmt.setInt(10, billno);
            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public String getBillChartofAccount(int billno) {
        String query = "";
        String chartAcc = "";
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            query = "SELECT DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR FROM BILL_MAST WHERE BILL_NO=?";
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, billno);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                chartAcc = StringUtils.defaultString(rs.getString("DEMAND_NO")) + "-" + StringUtils.defaultString(rs.getString("MAJOR_HEAD")) + "-" + StringUtils.defaultString(rs.getString("SUB_MAJOR_HEAD")) + "-" + StringUtils.defaultString(rs.getString("MINOR_HEAD")) + "-" + StringUtils.defaultString(rs.getString("SUB_MINOR_HEAD1")) + "-" + StringUtils.defaultString(rs.getString("SUB_MINOR_HEAD2")) + "-" + StringUtils.defaultString(rs.getString("PLAN")) + "-" + StringUtils.defaultString(rs.getString("SUB_MINOR_HEAD3")) + "-" + StringUtils.defaultString(rs.getString("SECTOR"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return chartAcc;
    }

    @Override
    public GetBillStatusBean getUploadBillStatus(int billId) {
        Connection con = null;
        ResultSet rs2 = null;
        PreparedStatement pstmt = null;
        GetBillStatusBean gbillStatus = new GetBillStatusBean();
        int billStatusId = 0;
        ArrayList al = null;
        int prevBillId = 0;

        BillHistoryAttr billhistr = null;
        ArrayList billhistrs = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT BILL_STATUS_ID,BILL_DESC,BILL_DATE,PREVIOUS_BILL_NO FROM BILL_MAST WHERE BILL_NO=?");
            pstmt.setInt(1, billId);
            rs2 = pstmt.executeQuery();
            if (rs2.next()) {
                billStatusId = rs2.getInt("BILL_STATUS_ID");
                gbillStatus.setBillno(rs2.getString("BILL_DESC"));
                gbillStatus.setBilldate(CommonFunctions.getFormattedOutputDate3(rs2.getDate("BILL_DATE")));
                prevBillId = rs2.getInt("PREVIOUS_BILL_NO");
            }

            DataBaseFunctions.closeSqlObjects(rs2, pstmt);

            String query = "SELECT BILL_STATUS,REMARK,STATUS_ID,HISTORY_DATE FROM ( "
                    + "SELECT REMARK,STATUS_ID,HISTORY_DATE FROM BILL_STATUS_HISTORY WHERE BILL_ID IN (?, ? ) ORDER BY HISTORY_DATE DESC ) BILL_STATUS_HISTORY "
                    + "INNER JOIN G_BILL_STATUS ON BILL_STATUS_HISTORY.STATUS_ID=G_BILL_STATUS.ID ORDER BY HISTORY_DATE DESC";

            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, billId);
            pstmt.setInt(2, prevBillId);
            rs2 = pstmt.executeQuery();
            while (rs2.next()) {

                Timestamp originalString = rs2.getTimestamp("HISTORY_DATE");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");

                String newString = simpleDateFormat.format(originalString);
                billhistr = new BillHistoryAttr();
                billhistr.setBillStatus(rs2.getString("BILL_STATUS") + " (" + newString + ")");
                al = new ArrayList();
                if (rs2.getInt("STATUS_ID") == 4) {
                    String str[] = rs2.getString("REMARK").split("@");
                    if (str != null) {
                        for (int i = 0; i < str.length; i++) {
                            al.add(str[i]);
                        }
                    }
                } else {
                    al.add(rs2.getString("REMARK"));
                }
                billhistr.setMessage(al);
                billhistrs.add(billhistr);

            }
            gbillStatus.setErrMsg(billhistrs);

            DataBaseFunctions.closeSqlObjects(rs2, pstmt);

            String query2 = "SELECT * FROM (SELECT BILL_NO, BILL_DESC, BILL_DATE, VCH_NO, VCH_DATE, TOKEN_NO, TOKEN_DATE, BILL_STATUS_ID FROM BILL_MAST WHERE BILL_NO=?) BILL_MAST "
                    + "LEFT OUTER JOIN G_BILL_STATUS ON BILL_MAST.BILL_STATUS_ID = G_BILL_STATUS.ID";
            pstmt = con.prepareStatement(query2);
            pstmt.setInt(1, billId);
            rs2 = pstmt.executeQuery();
            if (rs2.next()) {
                gbillStatus.setBillno(rs2.getString("BILL_DESC"));
                gbillStatus.setBilldate(CommonFunctions.getFormattedOutputDate3(rs2.getDate("BILL_DATE")));
                gbillStatus.setTokenno(rs2.getString("TOKEN_NO"));
                gbillStatus.setTokendate(CommonFunctions.getFormattedOutputDate3(rs2.getDate("TOKEN_DATE")));
                gbillStatus.setVoucherno(rs2.getString("VCH_NO"));
                gbillStatus.setVoucherdate(CommonFunctions.getFormattedOutputDate3(rs2.getDate("VCH_DATE")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs2, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gbillStatus;
    }

    @Override
    public void changeBillStatus(int billId, int statusId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("update bill_mast set bill_status_id=? where bill_no=?");//0
            pstmt.setInt(1, statusId);
            pstmt.setInt(2, billId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getBillData(BillDetail billDetail) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList ar = new ArrayList();
        String sql = "";
        String sql1 = "";
        String sql2 = "";

        String offCode = billDetail.getOffcode();
        String bill_no = billDetail.getBillnumber();
        try {
            String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            con = this.dataSource.getConnection();
            stmt = con.createStatement();
            if (offCode != null && !offCode.equals("")) {
                sql1 = "Select max(aq_year) \"year\" from bill_mast where off_code='" + offCode + "'";
                rs = stmt.executeQuery(sql1);
                if (rs.next()) {
                    billDetail = new BillDetail();
                    billDetail.setAq_year(rs.getString("year"));
                }
                int aqYear = Integer.parseInt(billDetail.getAq_year());
                if (aqYear > 0) {
                    sql2 = "select max(aq_month) \"amonth\" from bill_mast where off_code='" + offCode + "' and aq_year=(Select max(aq_year) from bill_mast where off_code='" + offCode + "')";
                    rs = stmt.executeQuery(sql2);
                    if (rs.next()) {
                        billDetail = new BillDetail();
                        billDetail.setAq_month((rs.getString("amonth")));
                    }
                }
                int aqMonth = Integer.parseInt(billDetail.getAq_month());
                sql = "select billmast.bill_no,billmast.bill_desc,billmast.bill_date,billmast.aq_month,billmast.aq_year,billmast.off_code, billmast.bill_group_desc,\n"
                        + "billmast.token_no,billmast.token_date,billmast.previous_token_no,billmast.previous_token_date,billmast.bill_status_id,billstatus.bill_status,billmast.type_of_bill from \n"
                        + "(select * from bill_mast where off_code='" + offCode + "' and aq_year='" + aqYear + "' and aq_month='" + aqMonth + "')billmast\n"
                        + "left outer join\n"
                        + "(select * from g_bill_status)billstatus\n"
                        + "on\n"
                        + "billmast.bill_status_id=billstatus.id";
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    billDetail = new BillDetail();
                    billDetail.setOffcode(rs.getString("off_code"));
                    billDetail.setBillnumber(rs.getString("bill_no"));
                    billDetail.setBilldesc(rs.getString("bill_desc"));
                    //billDetail.setBilldesc(rs.getString("bill_group_desc"));
                    billDetail.setBillStatus(rs.getString("bill_status"));
                    billDetail.setBillStatusId(rs.getInt("bill_status_id"));
                    billDetail.setBillgrpname(rs.getString("bill_group_desc"));
                    billDetail.setBillDate(CommonFunctions.getFormattedInputDate(rs.getDate("bill_date")));
                    billDetail.setAq_month(monthNames[(rs.getInt("aq_month"))]);
                    billDetail.setAq_year(rs.getString("aq_year"));
                    billDetail.setTokenNumber(StringUtils.defaultString(rs.getString("token_no")));
                    billDetail.setTokendate(CommonFunctions.getFormattedInputDate(rs.getDate("token_date")));
                    billDetail.setPrevTokenNumber(StringUtils.defaultString(rs.getString("previous_token_no")));
                    billDetail.setPrevTokendate(CommonFunctions.getFormattedInputDate(rs.getDate("previous_token_date")));
                    billDetail.setEncryBillId(CommonFunctions.encodedTxt(rs.getString("bill_no")));
                    billDetail.setBillType(rs.getString("type_of_bill"));

                    ar.add(billDetail);
                }

            } else if (bill_no != null && !bill_no.equals("")) {
                sql = "select billmast.bill_no,billmast.bill_desc,billmast.bill_date,billmast.aq_month,billmast.aq_year,billmast.off_code, billmast.bill_group_desc,\n"
                        + "billmast.token_no,billmast.token_date,billmast.previous_token_no,billmast.previous_token_date,billmast.bill_status_id,billstatus.bill_status,billmast.type_of_bill,\n"
                        + "(select arrear_percent from arr_mast where bill_no=billmast.bill_no limit 1)arrear_percent \n"
                        + "from \n"
                        + "(select * from bill_mast where BILL_no='" + billDetail.getBillnumber() + "')billmast\n"
                        + "left outer join\n"
                        + "(select * from g_bill_status)billstatus\n"
                        + "on\n"
                        + "billmast.bill_status_id=billstatus.id";
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    billDetail = new BillDetail();
                    billDetail.setOffcode(rs.getString("off_code"));
                    billDetail.setBillnumber(rs.getString("bill_no"));
                    billDetail.setBilldesc(rs.getString("bill_desc"));
                    //billDetail.setBilldesc(rs.getString("bill_group_desc"));
                    billDetail.setBillStatus(rs.getString("bill_status"));
                    billDetail.setBillStatusId(rs.getInt("bill_status_id"));
                    billDetail.setBillgrpname(rs.getString("bill_group_desc"));
                    billDetail.setBillDate(CommonFunctions.getFormattedInputDate(rs.getDate("bill_date")));
                    billDetail.setAq_month(monthNames[(rs.getInt("aq_month"))]);
                    billDetail.setAq_year(rs.getString("aq_year"));
                    billDetail.setTokenNumber(StringUtils.defaultString(rs.getString("token_no")));
                    billDetail.setTokendate(CommonFunctions.getFormattedInputDate(rs.getDate("token_date")));
                    billDetail.setPrevTokenNumber(StringUtils.defaultString(rs.getString("previous_token_no")));
                    billDetail.setPrevTokendate(CommonFunctions.getFormattedInputDate(rs.getDate("previous_token_date")));
                    billDetail.setEncryBillId(CommonFunctions.encodedTxt(rs.getString("bill_no")));
                    billDetail.setBillType(rs.getString("type_of_bill"));
                    billDetail.setArrearpercent(rs.getString("arrear_percent"));
                    ar.add(billDetail);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    // rs.close();                    
                    //  stmt.close();
                    con.close();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return ar;
    }

    @Override
    public void unlockBill(BillDetail billDetail, String dcLoginId, String ipaddress) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pst1 = null;
        try {
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            String Cdate = ft.format(dNow);

            con = this.dataSource.getConnection();
            ps = con.prepareStatement("update bill_mast set bill_status_id=0, ben_ref_no=? where bill_no=? and (bill_status_id<>3 and bill_status_id<>5 and bill_status_id<>7)");
            ps.setString(1, "");
            ps.setInt(2, Integer.parseInt(billDetail.getBillnumber()));
            ps.executeUpdate();

            String sqlString = dcLoginId + " has unlocked bill no " + billDetail.getBillnumber() + " on " + new Date();
            pst1 = con.prepareStatement("INSERT INTO monitor_emp_log (login_by,login_date,emp_id,sql_text_query,IP_ADDRESS) values(?,?,?,?,?)");
            pst1.setString(1, dcLoginId);
            pst1.setTimestamp(2, new Timestamp(new Date().getTime()));
            pst1.setString(3, billDetail.getBillnumber());
            pst1.setString(4, sqlString);
            pst1.setString(5, ipaddress);
            pst1.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public BillDetail unlockBillToResubmit(BillDetail billDetail, String ipaddress, String dcLoginId) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pst1 = null;
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("update bill_mast set bill_status_id=1, ben_ref_no=? where bill_status_id=2 and bill_no=?");
            ps.setString(1, "");
            ps.setInt(2, Integer.parseInt(billDetail.getBillnumber()));
            ps.executeUpdate();

            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            String Cdate = ft.format(dNow);

            String sqlString = dcLoginId + " has unlocked bill no " + billDetail.getBillnumber() + " on " + new Date();
            pst1 = con.prepareStatement("INSERT INTO monitor_emp_log (login_by,login_date,emp_id,sql_text_query,IP_ADDRESS) values(?,?,?,?,?)");
            pst1.setString(1, dcLoginId);
            pst1.setTimestamp(2, new Timestamp(new Date().getTime()));
            pst1.setString(3, billDetail.getBillnumber());
            pst1.setString(4, sqlString);
            pst1.setString(5, ipaddress);
            pst1.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, pst1);
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void objectBill(BillDetail billDetail) {
        Connection con = null;
        PreparedStatement ps = null;
        Statement stmt = null;
        ResultSet rs = null;
        String prvtoken_no = "";
        Date prvtoken_date = null;
        String tokenno = "";
        Date tokendate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String objBill = billDetail.getObjectbill();
        ArrayList ar = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            stmt = con.createStatement();

            String sql = "UPDATE BILL_MAST SET BILL_STATUS_ID=8,IS_RESUBMITTED='Y',PREVIOUS_TOKEN_NO=?,PREVIOUS_TOKEN_DATE=?,TOKEN_NO='',TOKEN_DATE=NULL, ben_ref_no=? WHERE BILL_NO=?";
            ps = con.prepareStatement(sql);
            String[] objArr = objBill.split(",");

            for (int i = 0; i < objArr.length; i++) {
                String sql1 = "SELECT * FROM BILL_MAST WHERE BILL_NO='" + Integer.parseInt(objArr[i]) + "'";
                rs = stmt.executeQuery(sql1);
                if (rs.next()) {
                    prvtoken_no = rs.getString("TOKEN_NO");
                    prvtoken_date = rs.getDate("TOKEN_DATE");
                }
                ps.setString(1, prvtoken_no);
                ps.setTimestamp(2, new Timestamp(prvtoken_date.getTime()));
                ps.setString(3, "");
                ps.setInt(4, Integer.parseInt(objArr[i]));
                ps.executeUpdate();
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public double getBasicAmountBillWise(int billno) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        double basic = 0;

        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("select SUM(CUR_BASIC) AMT from AQ_MAST WHERE BILL_NO=?");
            ps.setInt(1, billno);

            rs = ps.executeQuery();
            if (rs.next()) {
                basic = rs.getDouble("amt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return basic;
    }

    @Override
    public HrmsBillConfig getHrmsBillRestrictionStatus() {
        HrmsBillConfig config = new HrmsBillConfig();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("SELECT global_var_name, global_var_value, message FROM hrms_config ");
            rs = ps.executeQuery();
            while (rs.next()) {

                if (rs.getString("global_var_name").equalsIgnoreCase("STOP_BILL_PROCESS")) {
                    config.setStop_BILL_PROCESS(rs.getString("global_var_value"));
                    config.setStop_BILL_PROCESS_MESSAGE(rs.getString("message"));
                } else if (rs.getString("global_var_name").equalsIgnoreCase("STOP_BILL_SUBMISSION")) {
                    config.setStop_BILL_SUBMISSION(rs.getString("global_var_value"));
                    config.setStop_BILL_SUBMISSION_MESSAGE(rs.getString("message"));
                } else if (rs.getString("global_var_name").equalsIgnoreCase("STOP_SUBMIT_BILL_FOR_PROFILE")) {
                    config.setStop_SUBMIT_BILL_FOR_PROFILE(rs.getString("global_var_value"));
                    config.setStop_SUBMIT_BILL_FOR_PROFILE_MESSAGE(rs.getString("message"));
                } else if (rs.getString("global_var_name").equalsIgnoreCase("STOP_SUBMIT_BILL_FOR_AER")) {
                    config.setStop_SUBMIT_BILL_FOR_AER(rs.getString("global_var_value"));
                    config.setStop_SUBMIT_BILL_FOR_AER_MESSAGE(rs.getString("message"));
                } else if (rs.getString("global_var_name").equalsIgnoreCase("STOP_SUBMIT_BILL_FOR_AER_CO")) {
                    config.setStop_SUBMIT_BILL_FOR_AER_CO(rs.getString("global_var_value"));
                    config.setStop_SUBMIT_BILL_FOR_AER_CO_MESSAGE(rs.getString("message"));
                } else if (rs.getString("global_var_name").equalsIgnoreCase("STOP_SUBMIT_BILL_FOR_AER_AO")) {
                    config.setStop_SUBMIT_BILL_FOR_AER_AO(rs.getString("global_var_value"));
                    config.setStop_SUBMIT_BILL_FOR_AER_AO_MESSAGE(rs.getString("message"));
                } else if (rs.getString("global_var_name").equalsIgnoreCase("STOP_SUBMIT_BILL_FOR_POST_TERM")) {
                    config.setStop_SUBMIT_BILL_FOR_POST_TERMINATION_SUBMITTED(rs.getString("global_var_value"));
                    config.setStop_SUBMIT_BILL_FOR_POST_TERMINATION_SUBMITTED_MESSAGE(rs.getString("message"));
                } else if (rs.getString("global_var_name").equalsIgnoreCase("SUBMISSION_DATE")) {
                    config.setSubmission_DATE(rs.getString("global_var_value"));
                    config.setSubmission_DATE_MESSAGE(rs.getString("message"));
                } else if (rs.getString("global_var_name").equalsIgnoreCase("CLOSING_DATE")) {
                    config.setClosing_DATE(rs.getString("global_var_value"));
                    config.setClosing_DATE_MESSAGE(rs.getString("message"));
                } else if (rs.getString("global_var_name").equalsIgnoreCase("CLOSING_MONTH")) {
                    config.setClosing_MONTH(rs.getString("global_var_value"));
                    config.setClosing_MONTH_MESSAGE(rs.getString("message"));
                } else if (rs.getString("global_var_name").equalsIgnoreCase("CLOSING_YEAR")) {
                    config.setClosing_YEAR(rs.getString("global_var_value"));
                    config.setClosing_YEAR_MESSAGE(rs.getString("message"));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return config;
    }

    @Override
    public boolean isprofileVerifiedBillWise(int billId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean verified = true;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select count(*) cnt from aq_mast "
                    + " INNER JOIN emp_mast ON emp_mast.emp_id=aq_mast.emp_code where bill_no =? "
                    + " AND ((emp_mast.if_profile_verified IS NULL OR emp_mast.if_profile_verified='N') and (emp_mast.if_reengaged='N' or emp_mast.if_reengaged is null) ) and is_regular <> 'D'");
            ps.setInt(1, billId);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    verified = false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return verified;
    }

    @Override
    public boolean isaersubmitted(String offCode, String fy) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean verified = false;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select off_code  from aer_report_submit where fy=? and off_code=? and is_approver_submitted='Y' ");

            ps.setString(1, fy);
            ps.setString(2, offCode);

            rs = ps.executeQuery();
            if (rs.next()) {

                verified = true;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return verified;
    }

    @Override
    public boolean isaersubmittedAsCO(String offCode, String fy) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean verified = false;
        String level = "";
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("select lvl from g_office where off_code=?");
            ps.setString(1, offCode);
            rs = ps.executeQuery();
            if (rs.next()) {
                level = rs.getString("lvl");
            }

            if (level != null && !level.equals("") && (level.equals("01") || level.equals("02"))) {
                ps = con.prepareStatement("select co_off_code  from aer_report_submit_at_co where fy=? and co_off_code=? and verifier_submitted_on is not null ");

                ps.setString(1, fy);
                ps.setString(2, offCode);

                rs = ps.executeQuery();
                if (rs.next()) {

                    verified = true;

                }
            } else {
                verified = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return verified;
    }

    @Override
    public boolean isaersubmittedAsAO(String offCode, String fy) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean verified = false;
        String level = "";
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("select lvl from g_office where off_code=?");
            ps.setString(1, offCode);
            rs = ps.executeQuery();
            if (rs.next()) {
                level = rs.getString("lvl");
            }

            if (level != null && !level.equals("") && (level.equals("01"))) {
                ps = con.prepareStatement("select ao_off_code  from aer_report_submit_at_ao where fy=? and ao_off_code=? and ao_off_code is not null");

                ps.setString(1, fy);
                ps.setString(2, offCode);

                rs = ps.executeQuery();
                if (rs.next()) {
                    verified = true;
                }
            } else {
                verified = true;
            }

            ps = con.prepareStatement("select ao_off_code  from aer_report_submit_at_ao where fy=? and ao_off_code=? and ao_off_code is not null");

            ps.setString(1, fy);
            ps.setString(2, offCode);

            rs = ps.executeQuery();
            if (rs.next()) {

                verified = true;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return verified;
    }

    @Override
    public boolean ispostTerminationDataSubmitted(String offCode, String fy) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean verified = false;
        String level = "";
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("select lvl from g_office where off_code=?");
            ps.setString(1, offCode);
            rs = ps.executeQuery();
            if (rs.next()) {
                level = rs.getString("lvl");
            }

            if (level != null && !level.equals("") && (level.equals("01") || level.equals("02"))) {
                ps = con.prepareStatement("select co_off_code, lvl,is_hod_submitted  from post_termination_master_co ptm "
                        + " left outer join g_office on ptm.co_off_code=g_office.off_code where  co_off_code=? and ptm.fy=?");

                ps.setString(1, offCode);
                ps.setString(2, fy);

                rs = ps.executeQuery();
                if (rs.next()) {
                    if (rs.getString("is_hod_submitted").equalsIgnoreCase("Y")) {
                        verified = true;
                    }
                }
            } else {
                verified = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return verified;
    }

    @Override
    public boolean aerSubmitted(String offcode, String fy) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean submiited = false;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select status from aer_report_submit where off_code=? and fy=? ");
            ps.setString(1, offcode);
            ps.setString(2, fy);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("status") != null && !rs.getString("status").equals("")) {
                    submiited = true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return submiited;
    }

    @Override
    public String changePayHeadOfBill(BillBrowserbean bbbean) {
        Connection con = null;
        PreparedStatement ps = null;
        String changed = "S";
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT BILL_GROUP_ID FROM BILL_MAST WHERE BILL_NO=? AND OFF_CODE=?");
            ps.setBigDecimal(1, new BigDecimal(bbbean.getBillNo()));
            ps.setString(2, bbbean.getOffCode());
            rs = ps.executeQuery();
            BigDecimal billgroupid = new BigDecimal(0);
            if (rs.next()) {
                billgroupid = rs.getBigDecimal("BILL_GROUP_ID");
            }
            ps = con.prepareStatement("UPDATE BILL_GROUP_MASTER SET PAY_HEAD = ? WHERE BILL_GROUP_ID=?");
            ps.setString(1, bbbean.getObjectbthead());
            ps.setBigDecimal(2, billgroupid);
            ps.executeUpdate();

            ps = con.prepareStatement("UPDATE BILL_MAST SET PAY_HEAD = ? WHERE BILL_NO=?");
            ps.setString(1, bbbean.getObjectbthead());
            ps.setBigDecimal(2, new BigDecimal(bbbean.getBillNo()));
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return changed;
    }

    @Override
    public String changeObjectBtHead(BillBrowserbean bbbean) {
        Connection con = null;
        PreparedStatement ps = null;
        Statement stmt = null;
        String changed = "S";
        try {
            con = this.dataSource.getConnection();
            String adcode = bbbean.getAdcodename();
            if (adcode.contains("-")) {
                adcode = adcode.substring(0, adcode.indexOf("-"));

            }
            String aqDTLSTbl = AqFunctionalities.getAQBillDtlsTable(bbbean.getSltMonth(), bbbean.getSltYear());

            stmt = con.createStatement();
            stmt.execute("UPDATE " + aqDTLSTbl + " A "
                    + "SET BT_ID='" + bbbean.getObjectbthead().trim() + "' FROM (SELECT AQSL_NO,AQ_MONTH,AQ_YEAR FROM AQ_MAST WHERE aq_month=" + bbbean.getSltMonth() + " and aq_year=" + bbbean.getSltYear() + " AND BILL_NO=" + bbbean.getBillNo() + ") B "
                    + "WHERE A.AQSL_NO = B.AQSL_NO AND A.AQ_MONTH=B.AQ_MONTH AND A.AQ_YEAR=B.AQ_YEAR "
                    + "AND A.AD_CODE='" + adcode + "' AND A.AQ_MONTH=" + bbbean.getSltMonth() + " AND A.AQ_YEAR=" + bbbean.getSltYear() + " AND AD_AMT > 0");
            //}
        } catch (SQLException e) {
            changed = "F";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return changed;
    }

    @Override
    public String changeObjectBtHeadOfArrear(BillBrowserbean bbbean) {
        Connection con = null;
        PreparedStatement ps = null;
        Statement stmt = null;
        String changed = "S";
        try {
            con = this.dataSource.getConnection();
            String adcode = bbbean.getAdcodename();
            if (adcode.contains("-")) {
                adcode = adcode.substring(0, adcode.indexOf("-"));

            }

            if (adcode.equals("PAY")) {
                stmt = con.createStatement();
                stmt.execute("UPDATE ARR_MAST SET pay_head='" + bbbean.getObjectbthead().trim() + "' WHERE BILL_NO=" + bbbean.getBillNo());
            }
            stmt = con.createStatement();
            stmt.execute("UPDATE ARR_DTLS A SET BT_ID='" + bbbean.getObjectbthead().trim() + "' FROM (SELECT AQSL_NO,P_MONTH,P_YEAR FROM ARR_MAST WHERE BILL_NO=" + bbbean.getBillNo() + ") B "
                    + "WHERE A.AQSL_NO = B.AQSL_NO AND A.ad_type='" + adcode + "'");
        } catch (SQLException e) {
            changed = "F";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return changed;
    }

    @Override
    public String changeBtHeadOfArrear(BillBrowserbean bbbean) {
        Connection con = null;
        PreparedStatement ps = null;
        Statement stmt = null;
        String changed = "S";
        try {
            con = this.dataSource.getConnection();
            String adcode = bbbean.getAdcodename();
            if (adcode.contains("-")) {
                adcode = adcode.substring(0, adcode.indexOf("-"));
            }
            stmt = con.createStatement();
            if (bbbean.getAdcodename() != null && bbbean.getAdcodename().equals("OR")) {
                stmt.execute("UPDATE ARR_MAST SET BTID_OR='" + bbbean.getObjectbthead() + "' WHERE  BILL_NO=" + bbbean.getBillNo() + " AND OTHER_RECOVERY>0");
            } else if (bbbean.getAdcodename() != null && bbbean.getAdcodename().equals("DR")) {
                stmt.execute("UPDATE ARR_MAST SET BTID_DR='" + bbbean.getObjectbthead() + "' WHERE  BILL_NO=" + bbbean.getBillNo() + " AND DDO_RECOVERY>0");
            }

        } catch (SQLException e) {
            changed = "F";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return changed;
    }

    @Override
    public int assignNewBillNo(int billNo) {
        Connection con = null;
        PreparedStatement ps = null;
        Statement stmt = null;
        int newBillNo = 0;

        PreparedStatement pst = null;
        ResultSet rs = null;
        String typeofbill = "";
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("select type_of_bill from bill_mast where bill_no=?");
            pst.setInt(1, billNo);
            rs = pst.executeQuery();
            if (rs.next()) {
                typeofbill = rs.getString("type_of_bill");
            }

            newBillNo = CommonFunctions.getMaxCode(con, "bill_mast", "bill_no");
            ps = con.prepareStatement("UPDATE bill_mast SET previous_bill_no = ?,  bill_no = ?"
                    + ",is_resubmitted='N',previous_token_no=null,previous_token_date=null WHERE bill_no = ?");
            ps.setInt(1, billNo);
            ps.setInt(2, newBillNo);
            ps.setInt(3, billNo);
            ps.executeUpdate();

            if (typeofbill.equals("PAY")) {
                ps = con.prepareStatement("UPDATE aq_mast SET bill_no = ? WHERE bill_no = ?");
                ps.setInt(1, newBillNo);
                ps.setInt(2, billNo);
                ps.executeUpdate();
            } else if (typeofbill.contains("ARREAR")) {
                ps = con.prepareStatement("UPDATE arr_mast SET bill_no = ? WHERE bill_no = ?");
                ps.setInt(1, newBillNo);
                ps.setInt(2, billNo);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(stmt);
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return newBillNo;
    }

    @Override
    public boolean checkBillNo(int billNo) {
        boolean isExisting = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int totalRows = 0;

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT COALESCE(COUNT(*),0) AS total_rows FROM bill_mast WHERE bill_no = ?");
            ps.setInt(1, billNo);
            rs = ps.executeQuery();
            while (rs.next()) {
                totalRows = rs.getInt("total_rows");
            }
            if (totalRows > 0) {
                isExisting = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isExisting;
    }

    @Override
    public ArrayList getPayBillListForAG(int year, int month, String treasuryCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList billList = new ArrayList();

        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT aq_month,aq_year,BILL_NO,major_head,sub_major_head,minor_head,sub_minor_head1,sub_minor_head2,sub_minor_head3,VCH_NO,VCH_DATE,extract (year from vch_date) vch_year, EXTRACT(MONTH FROM vch_date) vch_month,EXTRACT(DAY FROM vch_date) vch_day,ag_treasury_code,DDO_CODE FROM BILL_MAST bill "
                    + "inner join g_treasury treasury on bill.tr_code=treasury.tr_code WHERE extract (year from vch_date)=? AND extract (month from vch_date)=? AND bill.TR_CODE=? AND bill.BILL_STATUS_ID=7 order by major_head,substring(VCH_NO,5)");
            pstmt.setInt(1, year);
            pstmt.setInt(2, (month + 1));
            pstmt.setString(3, treasuryCode);

            rs = pstmt.executeQuery();

            String financialYear = "";
            if (month > 2) {
                financialYear = year + "-" + (year + 1);
            } else {
                financialYear = (year - 1) + "-" + year;
            }
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
            while (rs.next()) {
                BillBean bb = new BillBean();
                bb.setBillMonth(rs.getInt("aq_month"));
                bb.setBillYear(rs.getInt("aq_year"));
                bb.setAdjYear(rs.getInt("vch_year"));
                bb.setBillno(rs.getString("BILL_NO"));
                if (rs.getString("VCH_NO").length() > 4) {
                    bb.setVoucherno(StringUtils.leftPad(StringUtils.substring(rs.getString("VCH_NO"), 4), 5, "0"));
                } else {
                    bb.setVoucherno(StringUtils.leftPad(rs.getString("VCH_NO"), 5, "0"));
                }

                if (rs.getDate("VCH_DATE") != null) {
                    bb.setVoucherdate(DATE_FORMAT.format(rs.getDate("VCH_DATE")));
                }
                bb.setVouchermonth(rs.getString("vch_month"));
                bb.setFinyear(financialYear);
                bb.setTreasurycode(rs.getString("ag_treasury_code"));
                bb.setDdocode(rs.getString("DDO_CODE"));
                bb.setMajorhead(rs.getString("major_head"));
                bb.setVoucherDay(rs.getInt("vch_day"));
                billList.add(bb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billList;
    }

    @Override
    public void updateBillInfo(BillDetail billDetail) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        //BillDetail billDetail = null;
        int result = 0;
        try {
            con = this.dataSource.getConnection();
            //billDetail= new BillDetail();
            //String reportList= billDetail.getChkReports();
            String billno = billDetail.getHidbillNo();
            String updateQry = "UPDATE BILL_MAST SET FROM_MONTH=?,FROM_YEAR=?,TO_MONTH=?,TO_YEAR=?,PAY_HEAD=?,BILL_TYPE=?,DDO_CODE=?, TOKEN_NO=?,TOKEN_DATE=?,BILL_STATUS_ID=?, REQUIRED_REPORTS=?,previous_token_no=?,previous_token_date=?,is_resubmitted=?,from_date=?,to_date=?,BILL_DATE=?,BILL_DESC=? WHERE BILL_NO=?";
            pst = con.prepareStatement(updateQry);
            pst.setInt(1, billDetail.getFromMonth());
            pst.setInt(2, billDetail.getFromYear());
            pst.setInt(3, billDetail.getToMonth());
            pst.setInt(4, billDetail.getToYear());
            pst.setString(5, billDetail.getPayHead());
            pst.setString(6, billDetail.getBillType());
            pst.setString(7, billDetail.getDdoCode());
            if (billDetail.getTokenNumber() != null && !billDetail.getTokenNumber().equals("")) {
                pst.setString(8, billDetail.getTokenNumber());

            } else {
                pst.setString(8, null);
            }
            if (billDetail.getTokendate() != null && !billDetail.getTokendate().equals("")) {
                pst.setTimestamp(9, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(billDetail.getTokendate()).getTime()));
            } else {
                pst.setTimestamp(9, null);
            }
            if (billDetail.getTokenNumber() != null && !billDetail.getTokenNumber().equals("")) {
                pst.setInt(10, 5);
            } else {
                pst.setInt(10, billDetail.getBillStatusId());
            }

            if (billDetail.getChkReports() != null && !billDetail.getChkReports().equals("")) {
                pst.setString(11, null);
            } else {
                pst.setString(11, billDetail.getRqdReports());
            }
            pst.setString(12, billDetail.getPrevTokenNumber());
            if (billDetail.getPrevTokendate() != null && !billDetail.getPrevTokendate().equals("")) {
                pst.setTimestamp(13, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(billDetail.getPrevTokendate()).getTime()));
            } else {
                pst.setTimestamp(13, null);
            }
            pst.setString(14, billDetail.getIsresubmitted());
            pst.setInt(15, Integer.parseInt(billDetail.getProcessFromDate()));
            pst.setInt(16, Integer.parseInt(billDetail.getProcessToDate()));
            if (billDetail.getBillDate() != null && !billDetail.getBillDate().equals("")) {
                pst.setTimestamp(17, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(billDetail.getBillDate()).getTime()));
            } else {
                pst.setTimestamp(17, null);
            }
            pst.setString(18, billDetail.getBilldesc());
            if (billno != null && !billno.equals("")) {
                pst.setInt(19, Integer.parseInt(billno));
                result = pst.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public BillDetail getEditBillInfo(String billnumber) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Statement stmt = null;
        BillDetail billDetail = null;
        ArrayList billDetailsList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            String sql1 = "SELECT BILL_STATUS_ID,BILL_DATE,FROM_MONTH,FROM_YEAR,TO_MONTH,TO_YEAR,DDO_CODE,BILL_TYPE,PAY_HEAD,REQUIRED_REPORTS,TOKEN_NO,TOKEN_DATE,previous_TOKEN_NO,previous_TOKEN_DATE,is_resubmitted,from_date,to_date,BILL_DESC FROM BILL_MAST WHERE BILL_NO= '" + billnumber + "'";
            ps = con.prepareStatement(sql1);
            rs = ps.executeQuery();
            if (rs.next()) {
                billDetail = new BillDetail();
                billDetail.setFromMonth(rs.getInt("from_month"));
                billDetail.setFromYear(rs.getInt("from_year"));
                billDetail.setToMonth(rs.getInt("to_month"));
                billDetail.setToYear(rs.getInt("to_year"));
                billDetail.setBillType(rs.getString("bill_type"));
                billDetail.setPayHead(rs.getString("pay_head"));
                billDetail.setDdoCode(rs.getString("ddo_code"));
                billDetail.setRqdReports(rs.getString("required_reports"));
                billDetail.setTokenNumber(rs.getString("token_no"));
                billDetail.setTokendate(CommonFunctions.getFormattedOutputDate1(rs.getDate("token_date")));
                billDetail.setPrevTokenNumber(rs.getString("previous_token_no"));
                billDetail.setPrevTokendate(CommonFunctions.getFormattedOutputDate1(rs.getDate("previous_token_date")));
                billDetail.setIsresubmitted(rs.getString("is_resubmitted"));
                billDetail.setProcessFromDate(rs.getInt("from_date") + "");
                billDetail.setProcessToDate(rs.getInt("to_date") + "");
                billDetail.setBillDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("BILL_DATE")));
                billDetail.setBillStatusId(rs.getInt("BILL_STATUS_ID"));
                billDetail.setBilldesc(rs.getString("BILL_DESC"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billDetail;

    }

    @Override
    public boolean isbillPrepared(String billId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean billstatus = false;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT BILL_NO, PAYBILL_TASK.BILL_ID IS_BILL_PREPARED FROM BILL_MAST "
                    + " LEFT OUTER JOIN PAYBILL_TASK ON "
                    + " BILL_MAST.BILL_NO=PAYBILL_TASK.BILL_ID"
                    + " WHERE BILL_NO=? ");
            ps.setInt(1, Integer.parseInt(billId));
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("IS_BILL_PREPARED") != null && !rs.getString("IS_BILL_PREPARED").equals("")) {
                    billstatus = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billstatus;
    }

    @Override
    public ArrayList verifyforLockBill(int billId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String verifydata = "";

        ArrayList lockbillerrorlist = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select bill_date, aq_month, aq_year, bill_desc, ben_ref_no, tr_code, bill_type from bill_mast where BILL_NO =? ");
            ps.setInt(1, billId);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getDate("bill_date") == null || rs.getDate("bill_date").equals("")) {
                    //verifydata = "Bill date cannot be blank.";
                    lockbillerrorlist.add("Bill date cannot be blank.");
                }

                if (rs.getString("bill_desc") == null || rs.getString("bill_desc").equals("")) {
                    //verifydata = verifydata + "<br />Bill Number cannot be blank.";
                    lockbillerrorlist.add("Bill Number cannot be blank.");
                }

                if (rs.getString("tr_code") == null || rs.getString("tr_code").equals("")) {
                    //verifydata = verifydata + "<br />Treasury Name cannot be blank.";
                    lockbillerrorlist.add("Treasury Name cannot be blank.");
                }

                if (rs.getString("bill_type") == null || rs.getString("bill_type").equals("")) {
                    //verifydata = verifydata + "<br />Bill Type cannot be blank.";
                    lockbillerrorlist.add("Bill Type cannot be blank.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lockbillerrorlist;
    }

    @Override
    public ArrayList getBeneficiaryList(int billId, String typeOfBill) {
        ArrayList li = new ArrayList();
        SalaryBenefitiaryDetails bd = new SalaryBenefitiaryDetails();
        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pst2 = null;

        ResultSet rs = null;
        ResultSet rs2 = null;

        ArrayList objlist = new ArrayList();

        boolean foundBeneficiary = false;
        try {
            con = this.dataSource.getConnection();

            pst2 = con.prepareStatement("select count(*) cnt from bill_beneficiary where bill_id=?");
            pst2.setInt(1, billId);
            rs2 = pst2.executeQuery();
            if (rs2.next()) {
                if (rs2.getInt("cnt") > 0) {
                    foundBeneficiary = true;
                }
            }

            if (foundBeneficiary == false) {
                //insertBeneficiaryDataForBill(billId, typeOfBill);
            }

            String sql = "select bill_id, bill_date, benf_name, benf_address, bank_acct_type, ifsc_code, bank_acc_no, net_amt, mobile, gpf_acc_no, benf_type   from bill_beneficiary where bill_id=? and benf_type='DDO' and net_amt>0 ";
            pst = con.prepareStatement(sql);
            pst.setInt(1, billId);
            rs = pst.executeQuery();
            while (rs.next()) {
                bd = new SalaryBenefitiaryDetails();
                bd.setBeneficiaryType(rs.getString("benf_type"));
                bd.setHrms_no(rs.getString("bill_id"));
                bd.setHrms_date(CommonFunctions.getFormattedOutputDate1(rs.getDate("bill_date")));
                bd.setBenf_name(rs.getString("benf_name"));
                bd.setBank_ifsc_code(rs.getString("ifsc_code"));
                bd.setAccount_type(rs.getString("bank_acct_type"));
                String benfaddress = "";
                if (rs.getString("benf_address") != null && !rs.getString("benf_address").equals("")) {
                    if (rs.getString("benf_address").length() > 200) {
                        benfaddress = rs.getString("benf_address").substring(0, 200);
                    } else {
                        benfaddress = rs.getString("benf_address");
                    }
                    bd.setBenf_address(benfaddress);
                    //System.out.println("address" + bd.getBenf_address());
                } else {
                    bd.setBenf_address("NA");
                }
                bd.setEmployeeId("");
                bd.setBen_acct_no(rs.getString("bank_acc_no"));
                bd.setMobile_number("");
                bd.setAmount(rs.getInt("net_amt"));
                if (bd.getAmount() > 0) {
                    objlist.add(bd);
                }
            }

            /*pst = con.prepareStatement("select bill_id, bill_date, benf_name, benf_address, bank_acct_type, ifsc_code, bank_acc_no, net_amt, emp_id, "
             + " mobile, acct_type, if_gpf_assumed, gpf_series, gpf_acc_no, gpf_number, benf_type from bill_beneficiary where bill_id=? and benf_type='EMP' and net_amt>0 order by benf_name");*/
            if (typeOfBill.equals("PAY")) {
                pst = con.prepareStatement("select aq_mast.pay_scale,aq_mast.emp_type,a1.* from\n"
                        + "(select emp_non_pran,usertype,matrix_cell,matrix_level,emp_mast.pay_commission,law_level,if_reengaged,bill_beneficiary.is_regular,stop_pay_nps,bill_id, \n"
                        + "bill_beneficiary.bill_date, benf_name, benf_address, \n"
                        + "bank_acct_type, ifsc_code, bill_beneficiary.bank_acc_no, net_amt, bill_beneficiary.emp_id,\n"
                        + "bill_beneficiary.mobile, bill_beneficiary.acct_type, bill_beneficiary.if_gpf_assumed, bill_beneficiary.gpf_series, gpf_acc_no, \n"
                        + "gpf_number, benf_type from bill_beneficiary\n"
                        + "inner join emp_mast on bill_beneficiary.emp_id=emp_mast.emp_id\n"
                        + "where bill_id=? and benf_type='EMP' and net_amt>0)a1 \n"
                        + "inner join aq_mast on a1.bill_id=aq_mast.bill_no and a1.emp_id=aq_mast.emp_code\n"
                        + "order by a1.benf_name");
            } else {
                pst = con.prepareStatement("select emp_non_pran,usertype,matrix_cell,matrix_level,emp_mast.pay_commission,law_level,if_reengaged,bill_beneficiary.is_regular,stop_pay_nps,bill_id, \n"
                        + "bill_beneficiary.bill_date, benf_name, benf_address, \n"
                        + "bank_acct_type, ifsc_code, bill_beneficiary.bank_acc_no, net_amt, bill_beneficiary.emp_id,\n"
                        + "bill_beneficiary.mobile, bill_beneficiary.acct_type, bill_beneficiary.if_gpf_assumed, bill_beneficiary.gpf_series, gpf_acc_no, \n"
                        + "gpf_number, benf_type from bill_beneficiary\n"
                        + "inner join emp_mast on bill_beneficiary.emp_id=emp_mast.emp_id\n"
                        + "where bill_id=? and benf_type='EMP' and net_amt>0\n"
                        + "order by benf_name");
            }

            pst.setInt(1, billId);
            rs = pst.executeQuery();
            double totAmt = 0;
            while (rs.next()) {

                bd = new SalaryBenefitiaryDetails();
                bd.setHrms_no(rs.getString("bill_id"));
                bd.setHrms_date(CommonFunctions.getFormattedOutputDate1(rs.getDate("bill_date")));
                bd.setBenf_name(rs.getString("benf_name"));
                bd.setBeneficiaryType(rs.getString("benf_type"));
                String benfaddress = "";
                if (rs.getString("benf_address") != null && !rs.getString("benf_address").equals("")) {
                    if (rs.getString("benf_address").length() > 200) {
                        benfaddress = rs.getString("benf_address").substring(0, 200);
                    } else {
                        benfaddress = rs.getString("benf_address");
                    }
                    bd.setBenf_address(benfaddress);
                    //System.out.println("address" + bd.getBenf_address());
                } else {
                    bd.setBenf_address("NA");
                }
                if (rs.getString("bank_acc_no") != null && !rs.getString("bank_acc_no").equals("")) {
                    bd.setBen_acct_no(StringUtils.trim(rs.getString("bank_acc_no")));
                }
                bd.setAccount_type(rs.getString("bank_acct_type"));
                bd.setBank_ifsc_code(rs.getString("ifsc_code"));
                bd.setAmount(rs.getInt("net_amt"));
                bd.setEmployeeId(rs.getString("emp_id"));
                //bd.setEmail_id(rs.getString("BILL_NO"));
                if (rs.getString("mobile") != null && !rs.getString("mobile").equals("") && rs.getString("mobile").trim().length() == 10) {
                    bd.setMobile_number(StringUtils.defaultString(rs.getString("mobile"), ""));
                } else {
                    bd.setMobile_number("");
                }

                bd.setIfGpfAssumed(rs.getString("if_gpf_assumed"));
                bd.setIfStopPayNps(rs.getString("stop_pay_nps"));
                //if(rs.getString("gpf_acc_no") != null && !rs.getString("gpf_acc_no").equals("")){
                bd.setIsRegular(rs.getString("is_regular"));
                /*}else{
                 bd.setIsRegular("N");
                 }*/
                bd.setLawLevel(rs.getString("law_level"));
                bd.setEmpNonPran(rs.getString("emp_non_pran"));
                bd.setIfReengaged(rs.getString("if_reengaged"));
                bd.setGpfAcctNo(rs.getString("gpf_acc_no"));
                bd.setEmpAcctType(rs.getString("acct_type"));
                if (rs.getString("acct_type").equalsIgnoreCase("GPF") || rs.getString("acct_type").equalsIgnoreCase("TPF")) {
                    if (rs.getString("if_gpf_assumed") != null && !rs.getString("if_gpf_assumed").equals("") && rs.getString("if_gpf_assumed").equals("N")) {
                        bd.setGpf_series(StringUtils.trim(rs.getString("gpf_series")));
                        bd.setGpf_number(StringUtils.trim(getNumberFromGPF(rs.getString("gpf_acc_no"))));
                    } else {
                        bd.setGpf_series("");
                        bd.setGpf_number("");
                    }
                } else if (rs.getString("acct_type").equalsIgnoreCase("PRAN")) {
                    if (rs.getString("if_gpf_assumed") != null && !rs.getString("if_gpf_assumed").equals("") && rs.getString("if_gpf_assumed").equals("N")) {
                        bd.setPran(StringUtils.defaultString(rs.getString("gpf_number"), ""));
                    }
                } else {
                    bd.setPran("");
                }
                if (typeOfBill.equals("PAY")) {
                    bd.setPaycomm(rs.getString("pay_commission"));
                    bd.setPaycell(rs.getString("matrix_cell"));
                    bd.setPaylevel(rs.getString("matrix_cell"));
                    bd.setPayscale(rs.getString("pay_scale"));
                    bd.setEmpType(rs.getString("emp_type"));
                } else {
                    bd.setPaycomm(null);
                    bd.setPaycell(null);
                    bd.setPaylevel(null);
                    bd.setPayscale(null);
                    bd.setEmpType(null);
                }

                bd.setUsertype(rs.getString("usertype"));

                if (bd.getAmount() > totAmt) {
                    if(bd.getEmployeeId() != null && !isEmployeeExistInBeneficiary(bd.getEmployeeId(), bd.getBenf_name(),objlist)){
                        objlist.add(bd);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return objlist;
    }
    
    private boolean isEmployeeExistInBeneficiary(String empid, String empname, List objlist) {
        boolean duplicate = false;
        SalaryBenefitiaryDetails sbd = null;
        for (int i = 0; i < objlist.size(); i++) {
            sbd = (SalaryBenefitiaryDetails) objlist.get(i);
            if (sbd.getEmployeeId() != null && sbd.getEmployeeId().equals(empid)) {
                if (sbd.getBenf_name() != null && sbd.getBenf_name().equals(empname)) {
                    duplicate = true;
                }
            }
        }
        return duplicate;
    }
    
    private void insertBeneficiaryDataForBill(int billId, String typeOfBill) {

        ArrayList li = new ArrayList();

        SalaryBenefitiaryDetails bd = new SalaryBenefitiaryDetails();

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst2 = null;
        PreparedStatement pst3 = null;

        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;

        ArrayList objlist = new ArrayList();

        try {
            con = this.dataSource.getConnection();

            String sql2 = " SELECT BANK_ACC_NO, other_beneficiary.IFSC_CODE, MOBILE, net_amt, beneficiary_name FROM EMP_RELATION "
                    + " inner join other_beneficiary on EMP_RELATION.emp_id=other_beneficiary.ref_emp_id "
                    + " and EMP_RELATION.sl_no=other_beneficiary.rel_slno"
                    + " WHERE EMP_ID=? and bill_id=? AND AMOUNT_TYPE IS NOT NULL ";

            if (typeOfBill.contains("ARREAR")) {
                String sql = "SELECT DDOGOFC.*,DDORECOVERY FROM\n"
                        + "(SELECT G_OFFICE.OFF_NAME, G_OFFICE.DDO_CODE, BILL_MAST.OFF_CODE,PVT_DED_AMT,G_OFFICE.BANK_CODE,G_OFFICE.BRANCH_CODE,IFSC_CODE,\n"
                        + "MICR_CODE,DDO_CUR_ACC_NO,OFF_EN,BILL_MAST.BILL_NO,BILL_DATE, SUBSTRING(BILL_MAST.OFF_CODE, 1,3) EMP_ADDRESS FROM BILL_MAST\n"
                        + "INNER JOIN G_OFFICE ON BILL_MAST.OFF_CODE = G_OFFICE.OFF_CODE\n"
                        + "LEFT OUTER JOIN G_BRANCH ON G_OFFICE.BRANCH_CODE = G_BRANCH.BRANCH_CODE WHERE BILL_NO=?)DDOGOFC\n"
                        + "LEFT OUTER JOIN \n"
                        + "(SELECT BILL_NO,SUM(DDO_RECOVERY) DDORECOVERY FROM ARR_MAST WHERE BILL_NO=? GROUP BY BILL_NO)ARRMAST \n"
                        + "ON DDOGOFC.BILL_NO=ARRMAST.BILL_NO \n"
                        + "WHERE (PVT_DED_AMT>0 OR DDORECOVERY>0)";
                pst = con.prepareStatement(sql);
                pst.setInt(1, billId);
                pst.setInt(2, billId);
                rs = pst.executeQuery();
                if (rs.next()) {
                    bd = new SalaryBenefitiaryDetails();
                    bd.setHrms_no(rs.getString("BILL_NO"));
                    bd.setHrms_date(CommonFunctions.getFormattedOutputDate1(rs.getDate("bill_date")));
                    if (rs.getString("DDO_CODE").equals("OLSWAT001")) {
                        bd.setBenf_name(rs.getString("OFF_NAME"));
                    } else {
                        bd.setBenf_name(rs.getString("OFF_EN"));
                    }

                    bd.setDdoCode(rs.getString("OFF_CODE"));
                    bd.setBank_ifsc_code(rs.getString("ifsc_code"));
                    bd.setAccount_type("C");
                    bd.setBeneficiaryType("DDO");
                    bd.setBenf_address(rs.getString("emp_address"));

                    bd.setEmployeeId("");
                    //bd.setMicrNo(rs.getString("micr_code"));
                    //bd.setBankAccountType("Current");
                    bd.setBen_acct_no(rs.getString("DDO_CUR_ACC_NO"));
                    bd.setMobile_number("");
                    if (rs.getInt("DDORECOVERY") > 0) {
                        bd.setAmount(rs.getInt("DDORECOVERY"));
                    } else {
                        bd.setAmount(rs.getInt("PVT_DED_AMT"));
                    }

                    if (bd.getAmount() > 0) {
                        objlist.add(bd);
                    }
                }

                sql = "select emp_mast.is_regular,getempaddress(emp_mast.emp_id) emp_address, bill_mast.BILL_NO,emp_name, aq_mast.bank_acc_no, aq_mast.emp_id, aq_mast.branch_name, gpf_type, aq_mast.acct_type ,bill_date, "
                        + " gpf_acc_no,emp_mast.if_gpf_assumed,emp_mast.if_maintenance_deduct, aq_mast.gross_amt,aq_mast.ded_amt,aq_mast.gross_amt-(coalesce(aq_mast.ded_amt,0)+coalesce(aq_mast.other_recovery,0)+coalesce(aq_mast.ddo_recovery,0)+coalesce(aq_mast.pvt_ded_amt, 0)) net_amt, mobile, aq_mast.ifsc_code from arr_mast aq_mast "
                        + " inner join bill_mast on aq_mast.bill_no=bill_mast.bill_no "
                        + " inner join emp_mast on aq_mast.emp_id=emp_mast.emp_id "
                        + " where aq_mast.BILL_NO=? and aq_mast.emp_id is not null order by emp_name";
                /*sql = "select emp_mast.is_regular,getempaddress(emp_mast.emp_id) emp_address, bill_mast.BILL_NO,emp_name, aq_mast.bank_acc_no, aq_mast.emp_id, aq_mast.branch_name, gpf_type, aq_mast.acct_type ,bill_date, "
                 + " gpf_acc_no,emp_mast.if_gpf_assumed,emp_mast.if_maintenance_deduct, aq_mast.gross_amt,aq_mast.ded_amt,aq_mast.gross_amt-(coalesce(aq_mast.ded_amt,0)+coalesce(aq_mast.other_recovery,0)+coalesce(aq_mast.pvt_ded_amt, 0)) net_amt, mobile, aq_mast.ifsc_code from arr_mast aq_mast "
                 + " inner join bill_mast on aq_mast.bill_no=bill_mast.bill_no "
                 + " inner join emp_mast on aq_mast.emp_id=emp_mast.emp_id "
                 + " where aq_mast.BILL_NO=? and aq_mast.emp_id is not null order by emp_name";*/
                pst = con.prepareStatement(sql);
                pst.setInt(1, billId);
                rs = pst.executeQuery();
                double totAmt = 0;
                while (rs.next()) {
                    totAmt = 0;

                    if (rs.getString("if_maintenance_deduct") != null && !rs.getString("if_maintenance_deduct").equals("") && rs.getString("if_maintenance_deduct").equals("Y")) {

                        pst2 = con.prepareStatement(sql2);
                        pst2.setString(1, rs.getString("emp_id"));
                        pst2.setInt(2, billId);
                        rs2 = pst2.executeQuery();
                        while (rs2.next()) {
                            bd = new SalaryBenefitiaryDetails();

                            bd.setHrms_no(rs.getString("BILL_NO"));
                            bd.setEmpAcctType(rs.getString("acct_type"));
                            bd.setHrms_date(CommonFunctions.getFormattedOutputDate1(rs.getDate("bill_date")));
                            bd.setBenf_name(rs2.getString("beneficiary_name"));
                            bd.setBenf_address(rs.getString("emp_address").replaceAll("[^a-zA-Z0-9\\s+]", ""));
                            if (rs2.getString("bank_acc_no") != null && !rs2.getString("bank_acc_no").equals("")) {
                                bd.setBen_acct_no(StringUtils.trim(rs2.getString("bank_acc_no")));
                            }
                            bd.setAccount_type("S");
                            bd.setBeneficiaryType("EMP");
                            bd.setBank_ifsc_code(rs2.getString("ifsc_code"));
                            bd.setAmount(rs2.getInt("net_amt"));
                            //netamt = rs2.getInt("net_amt");
                            bd.setEmployeeId(rs.getString("emp_id"));
                            bd.setIfGpfAssumed("Y");
                            bd.setIsRegular("N");

                            //bd.setEmail_id(rs.getString("BILL_NO"));
                            if (rs.getString("mobile") != null && !rs.getString("mobile").equals("") && rs.getString("mobile").length() == 10) {
                                bd.setMobile_number(StringUtils.defaultString(rs.getString("mobile"), ""));
                            } else {
                                bd.setMobile_number("");
                            }
                            if (rs.getString("acct_type").equalsIgnoreCase("GPF") || rs.getString("acct_type").equalsIgnoreCase("TPF")) {
                                if (rs.getString("if_gpf_assumed") != null && !rs.getString("if_gpf_assumed").equals("") && rs.getString("if_gpf_assumed").equals("N")) {
                                    bd.setGpf_series(StringUtils.trim(rs.getString("gpf_type")));
                                    bd.setGpf_number(StringUtils.trim(getNumberFromGPF(rs.getString("gpf_acc_no"))));
                                    bd.setGpfAcctNo(rs.getString("gpf_acc_no"));
                                } else {
                                    bd.setGpf_series("");
                                    bd.setGpf_number("");
                                    bd.setGpfAcctNo("");
                                }
                            } else if (rs.getString("acct_type").equalsIgnoreCase("PRAN")) {

                                if (rs.getString("if_gpf_assumed") != null && !rs.getString("if_gpf_assumed").equals("") && rs.getString("if_gpf_assumed").equals("N")) {
                                    bd.setPran(StringUtils.defaultString(rs.getString("gpf_acc_no"), ""));
                                }
                            } else {
                                bd.setPran("");
                            }
                            totAmt = totAmt + bd.getAmount();
                            if (bd.getAmount() > 0) {
                                if (totAmt <= rs.getInt("net_amt")) {
                                    objlist.add(bd);
                                }

                            }
                        }
                    }

                    bd = new SalaryBenefitiaryDetails();
                    bd.setEmpAcctType(rs.getString("acct_type"));
                    bd.setIfMaintenanceDeduct(rs.getString("if_maintenance_deduct"));
                    bd.setHrms_no(rs.getString("BILL_NO"));
                    bd.setHrms_date(CommonFunctions.getFormattedOutputDate1(rs.getDate("bill_date")));
                    bd.setBenf_name(rs.getString("emp_name"));
                    bd.setBenf_address(StringUtils.replace(rs.getString("emp_address").replaceAll("[^a-zA-Z0-9\\s+]", ""), "&#13;", ""));

                    if (rs.getString("bank_acc_no") != null && !rs.getString("bank_acc_no").equals("")) {
                        bd.setBen_acct_no(StringUtils.trim(StringUtils.defaultString(rs.getString("bank_acc_no"))));
                    }
                    bd.setAccount_type("S");
                    bd.setBeneficiaryType("EMP");
                    bd.setBank_ifsc_code(rs.getString("ifsc_code"));
                    //Insert Gross data in benificiary
                    bd.setGross(rs.getInt("gross_amt"));
                    bd.setAmount(rs.getInt("net_amt") - totAmt);
                    bd.setEmployeeId(StringUtils.defaultString(rs.getString("emp_id")));
                    //bd.setEmail_id(rs.getString("BILL_NO"));
                    if (rs.getString("mobile") != null && !rs.getString("mobile").equals("") && rs.getString("mobile").length() == 10) {
                        bd.setMobile_number(StringUtils.defaultString(rs.getString("mobile"), ""));
                    } else {
                        bd.setMobile_number("");
                    }
                    bd.setIfGpfAssumed(rs.getString("if_gpf_assumed"));
                    bd.setIsRegular(rs.getString("is_regular"));
                    if (rs.getString("acct_type").equalsIgnoreCase("GPF") || rs.getString("acct_type").equalsIgnoreCase("TPF")) {
                        if (rs.getString("if_gpf_assumed") != null && !rs.getString("if_gpf_assumed").equals("") && rs.getString("if_gpf_assumed").equals("N")) {
                            bd.setGpf_series(StringUtils.trim(rs.getString("gpf_type")));
                            bd.setGpf_number(StringUtils.trim(getNumberFromGPF(rs.getString("gpf_acc_no"))));
                            bd.setGpfAcctNo(rs.getString("gpf_acc_no"));
                        } else {
                            bd.setGpf_series("");
                            bd.setGpf_number("");
                            bd.setGpfAcctNo("");
                        }
                    } else if (rs.getString("acct_type").equalsIgnoreCase("PRAN")) {
                        if (rs.getString("if_gpf_assumed") != null && !rs.getString("if_gpf_assumed").equals("") && rs.getString("if_gpf_assumed").equals("N")) {
                            bd.setPran(StringUtils.defaultString(rs.getString("gpf_acc_no"), ""));
                            bd.setGpfAcctNo(rs.getString("gpf_acc_no"));
                        }
                    } else {
                        bd.setPran("");
                    }
                    //if (bd.getAmount() > totAmt) {
                    if (bd.getAmount() > 0) {
                        objlist.add(bd);
                    }
                }
            } else {

                int otherDdoAmt = 0;
                String sql = "SELECT SUBSTRING(G_OFFICE.off_code, 1,3) emp_address, manage_pvt_deduction.BILL_NO, OFF_EN, IFS_CODE, BANK_ACC_NO, AMOUNT, bill_date FROM manage_pvt_deduction "
                        + " INNER JOIN G_OFFICE ON manage_pvt_deduction.OFF_CODE=G_OFFICE.OFF_CODE "
                        + " INNER JOIN BILL_MAST ON manage_pvt_deduction.BILL_NO=BILL_MAST.BILL_NO "
                        + " WHERE manage_pvt_deduction.BILL_NO=? AND AMOUNT>0 ";
                pst = con.prepareStatement(sql);
                pst.setInt(1, billId);
                rs = pst.executeQuery();
                while (rs.next()) {
                    bd = new SalaryBenefitiaryDetails();
                    bd.setHrms_no(rs.getString("BILL_NO"));
                    bd.setHrms_date(CommonFunctions.getFormattedOutputDate1(rs.getDate("bill_date")));
                    bd.setBenf_name(rs.getString("OFF_EN"));
                    bd.setBank_ifsc_code(rs.getString("ifs_code"));
                    bd.setAccount_type("C");
                    bd.setBeneficiaryType("DDO");
                    bd.setBenf_address(rs.getString("emp_address"));
                    bd.setEmployeeId("");
                    //bd.setMicrNo(rs.getString("micr_code"));
                    //bd.setBankAccountType("Current");
                    bd.setBen_acct_no(rs.getString("BANK_ACC_NO"));
                    bd.setMobile_number("");
                    otherDdoAmt = otherDdoAmt + rs.getInt("AMOUNT");

                    bd.setAmount(rs.getInt("AMOUNT"));
                    if (bd.getAmount() > 0) {
                        objlist.add(bd);
                    }
                }

                pst3 = con.prepareStatement("select sum(aq_dtls.ad_amt) epfamt from aq_mast"
                        + " inner join aq_dtls on aq_mast.aqsl_no=aq_dtls.aqsl_no"
                        + " where bill_no=? and acct_type='EPF' and ad_code='EPF'");

                sql = "SELECT G_OFFICE.ddo_code, G_OFFICE.OFF_NAME, BILL_MAST.OFF_CODE,SUBSTRING(BILL_MAST.off_code, 1,3) emp_address,ddo_hrmsid, BILL_MAST.BILL_NO,bill_date, PVT_DED_AMT,G_OFFICE.BANK_CODE,G_OFFICE.BRANCH_CODE,IFSC_CODE,MICR_CODE,DDO_CUR_ACC_NO,OFF_EN FROM BILL_MAST "
                        + "INNER JOIN G_OFFICE ON BILL_MAST.OFF_CODE = G_OFFICE.OFF_CODE "
                        + "LEFT OUTER JOIN G_BRANCH ON G_OFFICE.BRANCH_CODE = G_BRANCH.BRANCH_CODE WHERE BILL_NO=? and PVT_DED_AMT>0";
                pst = con.prepareStatement(sql);
                pst.setInt(1, billId);
                rs = pst.executeQuery();
                if (rs.next()) {
                    bd = new SalaryBenefitiaryDetails();
                    bd.setHrms_no(rs.getString("BILL_NO"));
                    bd.setHrms_date(CommonFunctions.getFormattedOutputDate1(rs.getDate("bill_date")));
                    if (rs.getString("DDO_CODE").equals("OLSWAT001")) {
                        bd.setBenf_name(rs.getString("OFF_NAME"));
                    } else {
                        bd.setBenf_name(rs.getString("OFF_EN"));
                    }

                    bd.setBank_ifsc_code(rs.getString("ifsc_code"));
                    bd.setDdoCode(rs.getString("OFF_CODE"));
                    bd.setAccount_type("C");
                    bd.setBeneficiaryType("DDO");
                    bd.setBenf_address(rs.getString("emp_address"));
                    bd.setEmployeeId("");
                    //bd.setMicrNo(rs.getString("micr_code"));
                    //bd.setBankAccountType("Current");
                    bd.setBen_acct_no(rs.getString("DDO_CUR_ACC_NO"));
                    bd.setMobile_number("");

                    int epfamt = 0;

                    pst3.setInt(1, billId);
                    rs3 = pst3.executeQuery();
                    if (rs3.next()) {
                        epfamt = rs3.getInt("epfamt");
                    }

                    double db = rs.getInt("PVT_DED_AMT") + epfamt - otherDdoAmt;
                    bd.setAmount(db);
                    if (bd.getAmount() > 0) {
                        objlist.add(bd);
                    }
                } else {
                    sql = "SELECT (select sum(ad_amt) from aq_mast inner join aq_dtls on aq_mast.aqsl_no=aq_dtls.aqsl_no where aq_dtls.aqsl_no=aq_mast.aqsl_no and ad_code='EPF' and bill_no=bill_mast.bill_no) epfsum,"
                            + "G_OFFICE.ddo_code, G_OFFICE.OFF_NAME, BILL_MAST.OFF_CODE,SUBSTRING(BILL_MAST.off_code, 1,3) emp_address,ddo_hrmsid, BILL_MAST.BILL_NO,bill_date,G_OFFICE.BANK_CODE,G_OFFICE.BRANCH_CODE,IFSC_CODE,MICR_CODE,DDO_CUR_ACC_NO,OFF_EN"
                            + " FROM BILL_MAST"
                            + " INNER JOIN G_OFFICE ON BILL_MAST.OFF_CODE = G_OFFICE.OFF_CODE"
                            + " LEFT OUTER JOIN G_BRANCH ON G_OFFICE.BRANCH_CODE = G_BRANCH.BRANCH_CODE"
                            + " WHERE BILL_MAST.BILL_NO=?";
                    pst = con.prepareStatement(sql);
                    pst.setInt(1, billId);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        bd = new SalaryBenefitiaryDetails();
                        bd.setHrms_no(rs.getString("BILL_NO"));
                        bd.setHrms_date(CommonFunctions.getFormattedOutputDate1(rs.getDate("bill_date")));
                        if (rs.getString("DDO_CODE").equals("OLSWAT001")) {
                            bd.setBenf_name(rs.getString("OFF_NAME"));
                        } else {
                            bd.setBenf_name(rs.getString("OFF_EN"));
                        }

                        bd.setBank_ifsc_code(rs.getString("ifsc_code"));
                        bd.setDdoCode(rs.getString("OFF_CODE"));
                        bd.setAccount_type("C");
                        bd.setBeneficiaryType("DDO");
                        bd.setBenf_address(rs.getString("emp_address"));
                        bd.setEmployeeId("");

                        bd.setBen_acct_no(rs.getString("DDO_CUR_ACC_NO"));
                        bd.setMobile_number("");

                        int epfamt = rs.getInt("epfsum");

                        double db = epfamt - otherDdoAmt;
                        bd.setAmount(db);
                        if (bd.getAmount() > 0) {
                            objlist.add(bd);
                        }
                    }
                }

                pst = con.prepareStatement("SELECT emp_mast.is_regular,ifsc_code,getempaddress(emp_mast.emp_id) emp_address,emp_mast.emp_id,bill_mast.BILL_NO,emp_name,bill_mast.bill_date,NULLIF(regexp_replace(gpf_no, '[^0-9]', '', 'g'), '')::numeric  gpfNumber, "
                        + " gpf_acc_no,emp_mast.if_gpf_assumed,mobile, emp_mast.if_maintenance_deduct,aq_mast.gross_amt,aq_mast.ded_amt,AQ_MAST.pvt_ded_amt,aq_mast.bank_acc_no, aq_mast.branch_name, gpf_type, aq_mast.acct_type,   "
                        + " gpf_acc_no,aq_mast.gross_amt-(coalesce(aq_mast.ded_amt,0)+coalesce(aq_mast.pvt_ded_amt, 0)) net_amt from "
                        + " (SELECT BILL_NO,aq_mast.bank_acc_no, aq_mast.branch_name, gpf_type, aq_mast.acct_type,   "
                        + " gpf_acc_no, SUM(aq_mast.gross_amt) AS gross_amt,SUM(aq_mast.ded_amt) AS ded_amt, SUM(aq_mast.pvt_ded_amt) AS pvt_ded_amt, "
                        + " aq_mast.ifsc_code,EMP_CODE,EMP_NAME FROM AQ_MAST WHERE aq_mast.BILL_NO=? and emp_code is not null "
                        + " GROUP BY BILL_NO,aq_mast.bank_acc_no, aq_mast.branch_name, gpf_type, aq_mast.acct_type,    "
                        + "  gpf_acc_no,aq_mast.ifsc_code,EMP_CODE,EMP_NAME ORDER BY EMP_CODE)aq_mast "
                        + "  inner join emp_mast on aq_mast.emp_code=emp_mast.emp_id       "
                        + "   inner join bill_mast on aq_mast.bill_no=bill_mast.bill_no order by emp_name");

                pst.setInt(1, billId);
                rs = pst.executeQuery();
                double totAmt = 0;
                while (rs.next()) {
                    totAmt = 0;
                    if (rs.getString("if_maintenance_deduct") != null && !rs.getString("if_maintenance_deduct").equals("") && rs.getString("if_maintenance_deduct").equals("Y")) {
                        pst2 = con.prepareStatement(sql2);
                        pst2.setString(1, rs.getString("emp_id"));
                        pst2.setInt(2, billId);
                        rs2 = pst2.executeQuery();
                        while (rs2.next()) {
                            bd = new SalaryBenefitiaryDetails();
                            bd.setEmpAcctType(rs.getString("acct_type"));
                            bd.setHrms_no(rs.getString("BILL_NO"));
                            bd.setHrms_date(CommonFunctions.getFormattedOutputDate1(rs.getDate("bill_date")));
                            bd.setBenf_name(rs2.getString("beneficiary_name"));
                            if (rs.getString("emp_address") != null && !rs.getString("emp_address").equals("")) {
                                bd.setBenf_address(rs.getString("emp_address").trim());
                            }
                            if (rs2.getString("bank_acc_no") != null && !rs2.getString("bank_acc_no").equals("")) {
                                bd.setBen_acct_no(StringUtils.trim(rs2.getString("bank_acc_no")));
                            }
                            bd.setAccount_type("S");
                            bd.setBeneficiaryType("EMP");
                            bd.setBank_ifsc_code(rs2.getString("ifsc_code"));
                            bd.setAmount(rs2.getInt("net_amt"));
                            bd.setEmployeeId(rs.getString("emp_id"));
                            //bd.setEmail_id(rs.getString("BILL_NO"));
                            if (rs.getString("mobile") != null && !rs.getString("mobile").equals("") && rs.getString("mobile").length() == 10) {
                                bd.setMobile_number(StringUtils.defaultString(rs.getString("mobile"), ""));
                            } else {
                                bd.setMobile_number("");
                            }
                            //bd.setIfGpfAssumed(rs.getString("if_gpf_assumed"));
                            bd.setIfGpfAssumed("Y");
                            bd.setIsRegular("N");
                            if (rs.getString("acct_type").equalsIgnoreCase("GPF") || rs.getString("acct_type").equalsIgnoreCase("TPF")) {
                                if (rs.getString("if_gpf_assumed") != null && !rs.getString("if_gpf_assumed").equals("") && rs.getString("if_gpf_assumed").equals("N")) {
                                    bd.setGpf_series(StringUtils.trim(rs.getString("gpf_type")));
                                    bd.setGpf_number(StringUtils.trim(getNumberFromGPF(rs.getString("gpf_acc_no"))));
                                    bd.setGpfAcctNo(rs.getString("gpf_acc_no"));
                                } else {
                                    bd.setGpf_series("");
                                    bd.setGpf_number("");
                                    bd.setGpfAcctNo("");
                                }
                            } else if (rs.getString("acct_type").equalsIgnoreCase("PRAN")) {
                                if (rs.getString("if_gpf_assumed") != null && !rs.getString("if_gpf_assumed").equals("") && rs.getString("if_gpf_assumed").equals("N")) {
                                    bd.setPran(StringUtils.defaultString(rs.getString("gpf_acc_no"), ""));
                                }
                            } else {
                                bd.setPran("");
                            }
                            totAmt = totAmt + bd.getAmount();
                            if (bd.getAmount() > 0) {
                                if (totAmt <= rs.getInt("net_amt")) {
                                    objlist.add(bd);
                                }

                            }
                        }
                    }

                    bd = new SalaryBenefitiaryDetails();
                    bd.setEmpAcctType(rs.getString("acct_type"));
                    bd.setIfMaintenanceDeduct(rs.getString("if_maintenance_deduct"));
                    bd.setHrms_no(rs.getString("BILL_NO"));
                    bd.setHrms_date(CommonFunctions.getFormattedOutputDate1(rs.getDate("bill_date")));
                    bd.setBenf_name(rs.getString("emp_name"));
                    if (rs.getString("emp_address") != null && !rs.getString("emp_address").equals("")) {
                        bd.setBenf_address(rs.getString("emp_address").trim());
                    }
                    if (rs.getString("bank_acc_no") != null && !rs.getString("bank_acc_no").equals("")) {
                        bd.setBen_acct_no(StringUtils.trim(rs.getString("bank_acc_no")));
                    }
                    bd.setAccount_type("S");
                    bd.setBeneficiaryType("EMP");
                    bd.setBank_ifsc_code(rs.getString("ifsc_code"));
                    bd.setAmount(rs.getInt("net_amt") - totAmt);
                    bd.setEmployeeId(rs.getString("emp_id"));
                    //bd.setEmail_id(rs.getString("BILL_NO"));
                    if (rs.getString("mobile") != null && !rs.getString("mobile").equals("") && rs.getString("mobile").length() == 10) {
                        bd.setMobile_number(StringUtils.defaultString(rs.getString("mobile"), ""));
                    } else {
                        bd.setMobile_number("");
                    }
                    bd.setIfGpfAssumed(rs.getString("if_gpf_assumed"));
                    bd.setIsRegular(rs.getString("is_regular"));
                    if (rs.getString("acct_type").equalsIgnoreCase("GPF") || rs.getString("acct_type").equalsIgnoreCase("TPF")) {
                        if (rs.getString("if_gpf_assumed") != null && !rs.getString("if_gpf_assumed").equals("") && rs.getString("if_gpf_assumed").equals("N")) {
                            bd.setGpf_series(StringUtils.trim(rs.getString("gpf_type")));
                            bd.setGpf_number(StringUtils.trim(getNumberFromGPF(rs.getString("gpf_acc_no"))));
                            bd.setGpfAcctNo(rs.getString("gpf_acc_no"));
                        } else {
                            bd.setGpf_series("");
                            bd.setGpf_number("");
                            bd.setGpfAcctNo("");
                        }
                    } else if (rs.getString("acct_type").equalsIgnoreCase("PRAN")) {
                        if (rs.getString("if_gpf_assumed") != null && !rs.getString("if_gpf_assumed").equals("") && rs.getString("if_gpf_assumed").equals("N")) {
                            bd.setPran(StringUtils.defaultString(rs.getString("gpfNumber"), ""));
                            bd.setGpfAcctNo(rs.getString("gpf_acc_no"));
                        }
                    } else {
                        bd.setPran("");
                    }
                    if (bd.getAmount() > 0) {
                        objlist.add(bd);
                    }
                }
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);
            if (objlist.size() > 0) {

                String sql = "delete from bill_beneficiary where bill_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, billId);
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(rs, pst);

                SalaryBenefitiaryDetails salarybd = null;
                sql = "insert into bill_beneficiary(bill_id ,emp_id ,benf_name ,gpf_acc_no ,gpf_series ,gpf_number ,acct_type ,if_gpf_assumed , mobile ,"
                        + " if_maintenance_deduct ,bill_date , benf_address, bank_acc_no, ifsc_code, gross_amt, deduction,"
                        + " pvt_deduction, net_amt, ddo_code, bank_acct_type,benf_type,is_regular)"
                        + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql);
                for (int i = 0; i < objlist.size(); i++) {
                    salarybd = (SalaryBenefitiaryDetails) objlist.get(i);
                    pst.setInt(1, billId);
                    pst.setString(2, salarybd.getEmployeeId());
                    if (salarybd.getBenf_name().length() > 100) {
                        pst.setString(3, salarybd.getBenf_name().substring(0, 100));
                    } else {
                        pst.setString(3, salarybd.getBenf_name());
                    }
                    pst.setString(4, salarybd.getGpfAcctNo());
                    pst.setString(5, salarybd.getGpf_series());
                    //pst.setString(6, salarybd.getGpf_number());
                    if (salarybd.getEmpAcctType() != null && (salarybd.getEmpAcctType().equalsIgnoreCase("GPF") || salarybd.getEmpAcctType().equalsIgnoreCase("TPF"))) {
                        pst.setString(6, salarybd.getGpf_number());
                    } else if (salarybd.getEmpAcctType() != null && salarybd.getEmpAcctType().equalsIgnoreCase("PRAN")) {
                        pst.setString(6, salarybd.getPran());
                    } else {
                        pst.setString(6, "");
                    }
                    pst.setString(7, salarybd.getEmpAcctType());
                    pst.setString(8, salarybd.getIfGpfAssumed());
                    pst.setString(9, salarybd.getMobile_number());
                    pst.setString(10, salarybd.getIfMaintenanceDeduct());
                    pst.setTimestamp(11, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(salarybd.getHrms_date()).getTime()));
                    String address = salarybd.getBenf_address();
                    if (address != null && !address.equals("") && !address.trim().equals("")) {
                        if (address.length() > 100) {
                            address = address.substring(0, 100);
                        }
                    } else {
                        address = "NA";
                    }
                    pst.setString(12, address);
                    pst.setString(13, salarybd.getBen_acct_no());
                    pst.setString(14, salarybd.getBank_ifsc_code());
                    pst.setInt(15, salarybd.getGross());
                    pst.setInt(16, 0);
                    pst.setInt(17, 0);
                    pst.setDouble(18, salarybd.getAmount());
                    pst.setString(19, salarybd.getDdoCode());
                    pst.setString(20, salarybd.getAccount_type());
                    pst.setString(21, salarybd.getBeneficiaryType());
                    pst.setString(22, salarybd.getIsRegular());
                    pst.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public ArrayList getVoucherListForAG(int year, int month, String parentTrCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList billList = new ArrayList();

        String allTreasury = "";
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("select tr_code from g_treasury where ag_treasury_code=?");
            pstmt.setString(1, parentTrCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (allTreasury.equals("")) {
                    allTreasury = "'" + rs.getString("tr_code") + "'";
                } else {
                    allTreasury = allTreasury + ",'" + rs.getString("tr_code") + "'";
                }
            }

            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            pstmt = con.prepareStatement("SELECT aq_month,aq_year,BILL_NO,major_head,sub_major_head,minor_head,sub_minor_head1,sub_minor_head2,sub_minor_head3,VCH_NO,VCH_DATE,extract (year from vch_date) vch_year, EXTRACT(MONTH FROM vch_date) vch_month,EXTRACT(DAY FROM vch_date) vch_day,ag_treasury_code,DDO_CODE FROM BILL_MAST bill "
                    + "inner join g_treasury treasury on bill.tr_code=treasury.tr_code WHERE extract (year from vch_date)=? AND extract (month from vch_date)=? AND bill.TR_CODE IN (" + allTreasury + ") AND bill.BILL_STATUS_ID=7 order by major_head,substring(VCH_NO,5)::integer");
            pstmt.setInt(1, year);
            pstmt.setInt(2, (month + 1));
            //pstmt.setString(3, allTreasury);

            rs = pstmt.executeQuery();

            String financialYear = "";
            if (month > 2) {
                financialYear = year + "-" + (year + 1);
            } else {
                financialYear = (year - 1) + "-" + year;
            }
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
            while (rs.next()) {
                BillBean bb = new BillBean();
                bb.setBillMonth(rs.getInt("aq_month"));
                bb.setBillYear(rs.getInt("aq_year"));
                bb.setAdjYear(rs.getInt("vch_year"));
                bb.setBillno(rs.getString("BILL_NO"));
                if (rs.getString("VCH_NO").length() > 4) {
                    bb.setVoucherno(StringUtils.leftPad(StringUtils.substring(rs.getString("VCH_NO"), 4), 5, "0"));
                } else {
                    bb.setVoucherno(StringUtils.leftPad(rs.getString("VCH_NO"), 5, "0"));
                }

                if (rs.getDate("VCH_DATE") != null) {
                    bb.setVoucherdate(DATE_FORMAT.format(rs.getDate("VCH_DATE")));
                }
                bb.setVouchermonth(rs.getString("vch_month"));
                bb.setFinyear(financialYear);
                bb.setTreasurycode(rs.getString("ag_treasury_code"));
                bb.setDdocode(rs.getString("DDO_CODE"));
                bb.setMajorhead(rs.getString("major_head"));
                bb.setVoucherDay(rs.getInt("vch_day"));
                billList.add(bb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billList;
    }

    @Override
    public ArrayList getAquitanceVoucherListForAG(int year, int month, String parentTrCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList billList = new ArrayList();

        String allTreasury = "";
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("select tr_code from g_treasury where ag_treasury_code=?");
            pstmt.setString(1, parentTrCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (allTreasury.equals("")) {
                    allTreasury = "'" + rs.getString("tr_code") + "'";
                } else {
                    allTreasury = allTreasury + ",'" + rs.getString("tr_code") + "'";
                }
            }

            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            pstmt = con.prepareStatement("SELECT aq_month,aq_year,BILL_NO,major_head,sub_major_head,minor_head,sub_minor_head1,sub_minor_head2,sub_minor_head3,VCH_NO,VCH_DATE,extract (year from vch_date) vch_year, EXTRACT(MONTH FROM vch_date) vch_month,EXTRACT(DAY FROM vch_date) vch_day,ag_treasury_code,DDO_CODE FROM BILL_MAST bill "
                    + "inner join g_treasury treasury on bill.tr_code=treasury.tr_code WHERE extract (year from vch_date)=? AND extract (month from vch_date)=? AND bill.TR_CODE IN (" + allTreasury + ") AND bill.BILL_STATUS_ID=7 and bill.type_of_bill='PAY' and length(VCH_NO) > 4 order by major_head,substring(VCH_NO,5)::integer");
            pstmt.setInt(1, year);
            pstmt.setInt(2, (month + 1));
            //pstmt.setString(3, allTreasury);

            rs = pstmt.executeQuery();

            String financialYear = "";
            if (month > 2) {
                financialYear = year + "-" + (year + 1);
            } else {
                financialYear = (year - 1) + "-" + year;
            }
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
            while (rs.next()) {
                BillBean bb = new BillBean();
                bb.setBillMonth(rs.getInt("aq_month"));
                bb.setBillYear(rs.getInt("aq_year"));
                bb.setAdjYear(rs.getInt("vch_year"));
                bb.setBillno(rs.getString("BILL_NO"));
                if (rs.getString("VCH_NO").length() > 4) {
                    bb.setVoucherno(StringUtils.leftPad(StringUtils.substring(rs.getString("VCH_NO"), 4), 5, "0"));
                } else {
                    bb.setVoucherno(StringUtils.leftPad(rs.getString("VCH_NO"), 5, "0"));
                }

                if (rs.getDate("VCH_DATE") != null) {
                    bb.setVoucherdate(DATE_FORMAT.format(rs.getDate("VCH_DATE")));
                }
                bb.setVouchermonth(rs.getString("vch_month"));
                bb.setFinyear(financialYear);
                bb.setTreasurycode(rs.getString("ag_treasury_code"));
                bb.setDdocode(rs.getString("DDO_CODE"));
                bb.setMajorhead(rs.getString("major_head"));
                bb.setVoucherDay(rs.getInt("vch_day"));
                billList.add(bb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billList;
    }

    @Override
    public double getBillGrossAndNet(int billId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        double netAmt = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select sum(net_amt) netAmt from bill_beneficiary where bill_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, billId);
            rs = pst.executeQuery();
            if (rs.next()) {
                netAmt = rs.getDouble("netAmt");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return netAmt;
    }

    @Override
    public boolean verifySalaryBillofIAS(int billId) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean stop = false;
        try {

            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select cadre_type, emp_mast.cur_allotment_year, emp_mast.acct_type, emp_mast.if_retired  from aq_mast "
                    + " inner join emp_mast on aq_mast.emp_code=emp_mast.emp_id "
                    + " inner join g_cadre on emp_mast.cur_cadre_code=g_cadre.cadre_code "
                    + " where bill_no=? and emp_code is not null and cadre_type='AIS' group by cadre_type,emp_mast.cur_allotment_year,emp_mast.acct_type, emp_mast.if_retired ");

            pst.setInt(1, billId);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("if_retired") != null && !rs.getString("if_retired").equals("") && rs.getString("if_retired").equalsIgnoreCase("Y")) {
                    stop = false;
                } else if (rs.getString("acct_type").equalsIgnoreCase("PRAN")) {
                    if (rs.getString("cur_allotment_year") != null && rs.getInt("cur_allotment_year") < 2004) {
                        stop = true;
                    }
                } else {
                    stop = true;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return stop;
    }

    @Override
    public boolean foundFiftyPercentGrossForIASCorrect(String empId, int billId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean stop = true;
        try {

            int currentBasic = 0;
            int halfBasic = 0;

            con = this.dataSource.getConnection();

            pst = con.prepareStatement("SELECT cur_basic_salary FROM emp_mast WHERE EMP_id=? ");

            pst.setString(1, empId);
            rs = pst.executeQuery();
            if (rs.next()) {
                currentBasic = rs.getInt("cur_basic_salary");
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            pst = con.prepareStatement("SELECT mon_basic FROM AQ_MAST WHERE EMP_CODE=? AND bill_no=? ");

            pst.setString(1, empId);
            pst.setInt(2, billId);
            rs = pst.executeQuery();
            if (rs.next()) {
                halfBasic = rs.getInt("mon_basic");
            }

            if (currentBasic >= halfBasic * 2) {
                stop = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return stop;
    }

    @Override
    public String verifyUnlockBillDistrict(String logindistcode, String offcode, String billno) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean distCodeStatus = false;

        String sql = "";
        String errMsg = "N";
        try {
            con = this.dataSource.getConnection();

            if (offcode != null && !offcode.equals("")) {
                sql = "select dist_code from bill_mast"
                        + " inner join g_office on bill_mast.off_code=g_office.off_code where bill_mast.off_code=? group by dist_code";
                pst = con.prepareStatement(sql);
                pst.setString(1, offcode);
                rs = pst.executeQuery();
                if (rs.next()) {
                    if (rs.getString("dist_code") != null && !rs.getString("dist_code").equals("")) {
                        if (logindistcode != null && logindistcode.equals(rs.getString("dist_code"))) {
                            distCodeStatus = true;
                        }
                    }
                }

                if (distCodeStatus == false) {
                    errMsg = "This Office does not belong to your District";
                }
            } else if (billno != null && !billno.equals("")) {
                sql = "select dist_code from bill_mast"
                        + " inner join g_office on bill_mast.off_code=g_office.off_code where bill_mast.bill_no=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(billno));
                rs = pst.executeQuery();
                if (rs.next()) {
                    if (rs.getString("dist_code") != null && !rs.getString("dist_code").equals("")) {
                        if (logindistcode != null && logindistcode.equals(rs.getString("dist_code"))) {
                            distCodeStatus = true;
                        }
                    }
                }

                if (distCodeStatus == false) {
                    errMsg = "This Bill does not belong to your District";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return errMsg;
    }

    @Override
    public ArrayList getFailedTransactionData(int billno) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        FailedTransactionBean ftbean = new FailedTransactionBean();
        ArrayList ftlist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,bill_fail_transaction.* from bill_fail_transaction"
                    + " left outer join emp_mast on bill_fail_transaction.emp_id=emp_mast.emp_id where bill_id=? order by f_name";
            pst = con.prepareStatement(sql);
            pst.setInt(1, billno);
            rs = pst.executeQuery();
            while (rs.next()) {
                ftbean = new FailedTransactionBean();
                ftbean.setEmpid(rs.getString("emp_id"));
                ftbean.setEmpname(rs.getString("EMP_NAME"));
                ftbean.setBillid(rs.getString("bill_id"));
                ftbean.setBilldesc(rs.getString("bill_number"));
                ftbean.setBilldate(CommonFunctions.getFormattedOutputDate1(rs.getDate("bill_date")));
                ftbean.setTokenNo(rs.getString("token_number"));
                ftbean.setTokenDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("token_date")));
                ftbean.setBenfAcctNo(rs.getString("benf_acct_number"));
                ftbean.setBenfAmount(rs.getString("benf_amount"));
                ftbean.setIfsCode(rs.getString("ifsc_code"));
                ftbean.setRemarks(rs.getString("remarks"));
                ftlist.add(ftbean);
            }
            //System.out.println("ftlist size is: "+ftlist.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ftlist;
    }

    @Override
    public int getFailedTransactionCount(int billNo) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int failedcount = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select count(*) cnt from bill_fail_transaction where bill_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, billNo);
            rs = pst.executeQuery();
            if (rs.next()) {
                failedcount = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return failedcount;
    }

    @Override
    public SelectOption getBeneficiaryNet(int billNo) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SelectOption so = new SelectOption();
        try {
            con = this.dataSource.getConnection();

            String sql = "select count(*) empcount,sum(net_amt) netamt from bill_beneficiary where bill_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, billNo);
            rs = pst.executeQuery();
            if (rs.next()) {
                so.setLabel(rs.getString("empcount"));
                so.setValue(rs.getString("netamt"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return so;
    }

    @Override
    public List getMismatchBeneficiaryDetailsFromPreviousMonth(int billNo) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        AqmastModel aq = new AqmastModel();
        List<AqmastModel> li1 = new ArrayList();
        List<AqmastModel> li2 = new ArrayList();
        List<AqmastModel> li3 = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            int aqmonth = 0;
            int aqyear = 0;
            String billGrpId = "";

            String sql = " select aq_month, aq_year, bill_group_id, benf_name, gpf_number, emp_id, bank_acc_no, ifsc_code from bill_beneficiary "
                    + " inner join bill_mast on bill_beneficiary.bill_id=bill_mast.bill_no "
                    + " where bill_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, billNo);
            rs = pst.executeQuery();
            while (rs.next()) {
                aq = new AqmastModel();
                aq.setEmpCode(rs.getString("emp_id"));
                aq.setEmpName(rs.getString("benf_name"));
                aq.setGpfAccNo(rs.getString("gpf_number"));
                aq.setBankAccNo(rs.getString("bank_acc_no"));
                aq.setIfscCode(rs.getString("ifsc_code"));
                aqyear = rs.getInt("aq_year");
                aqmonth = rs.getInt("aq_month");
                billGrpId = rs.getString("bill_group_id");

                li1.add(aq);
            }
            if (aqmonth == 0) {
                aqmonth = aqmonth - 1;
                aqyear = aqyear - 1;
            } else {
                aqmonth = aqmonth - 1;
            }
            if (billGrpId != null && !billGrpId.equals("")) {
                sql = " select aq_month, aq_year, bill_group_id, benf_name, gpf_number, emp_id, bank_acc_no, ifsc_code from bill_beneficiary "
                        + " inner join bill_mast on bill_beneficiary.bill_id=bill_mast.bill_no "
                        + " where type_of_bill='PAY' and aq_month=? and aq_year=? and bill_group_id=" + billGrpId;

                pst = con.prepareStatement(sql);
                pst.setInt(1, aqmonth);
                pst.setInt(2, aqyear);
                rs = pst.executeQuery();
                while (rs.next()) {
                    aq = new AqmastModel();
                    aq.setEmpCode(rs.getString("emp_id"));
                    aq.setEmpName(rs.getString("benf_name"));
                    aq.setGpfAccNo(rs.getString("gpf_number"));
                    aq.setBankAccNo(rs.getString("bank_acc_no"));
                    aq.setIfscCode(rs.getString("ifsc_code"));
                    li2.add(aq);
                }
            }
            for (AqmastModel user1 : li1) {
                System.out.println("list 1");
                for (AqmastModel user2 : li2) {
                    System.out.println("list 2");
                    if ((user1.getEmpCode().equals(user2.getEmpCode())) && (!user1.getBankAccNo().equals(user2.getBankAccNo()) || !user1.getIfscCode().equals(user2.getIfscCode()))) {
                        li3.add(user1);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li3;
    }

    @Override
    public List getFortyPercentVoucheredBillData(BillDetail billDetail) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList ar = new ArrayList();
        String billno = null;
        try {
            String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            con = this.dataSource.getConnection();

            if (billDetail.getBillnumber() != null && !billDetail.getBillnumber().equals("")) {
                billno = billDetail.getBillnumber();
            }

            String sql = "select billmast.bill_no,billmast.bill_desc,billmast.bill_date,billmast.aq_month,billmast.aq_year,billmast.off_code, \n"
                    + "billmast.bill_group_desc,billmast.token_no,billmast.token_date,billmast.previous_token_no,billmast.previous_token_date,\n"
                    + "vch_no,vch_date,billmast.bill_status_id,billstatus.bill_status,billmast.type_of_bill from \n"
                    + "(select * from bill_mast where BILL_no=? and type_of_bill='ARREAR')billmast\n"
                    + "left outer join (select * from g_bill_status)billstatus on billmast.bill_status_id=billstatus.id";
            ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(billno));
            rs = ps.executeQuery();
            if (rs.next()) {
                billDetail = new BillDetail();
                billDetail.setOffcode(rs.getString("off_code"));
                billDetail.setBillnumber(rs.getString("bill_no"));
                billDetail.setBilldesc(rs.getString("bill_desc"));
                billDetail.setBillStatus(rs.getString("bill_status"));
                billDetail.setBillStatusId(rs.getInt("bill_status_id"));
                billDetail.setBillgrpname(rs.getString("bill_group_desc"));
                billDetail.setBillDate(CommonFunctions.getFormattedInputDate(rs.getDate("bill_date")));
                billDetail.setAq_month(monthNames[(rs.getInt("aq_month"))]);
                billDetail.setAq_year(rs.getString("aq_year"));
                billDetail.setTokenNumber(StringUtils.defaultString(rs.getString("token_no")));
                billDetail.setTokendate(CommonFunctions.getFormattedInputDate(rs.getDate("token_date")));
                billDetail.setVchNo(rs.getString("vch_no"));
                billDetail.setVchDt(CommonFunctions.getFormattedInputDate(rs.getDate("vch_date")));
                billDetail.setBillType(rs.getString("type_of_bill"));
                ar.add(billDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return ar;
    }

    @Override
    public boolean unlockFortyPercentVoucheredBill(int billNo) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        boolean updateStatus = false;

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("UPDATE BILL_MAST SET BILL_STATUS_ID=0 WHERE BILL_NO=? AND BILL_STATUS_ID=7 AND TYPE_OF_BILL='ARREAR' ");
            ps.setInt(1, billNo);
            int retVal = ps.executeUpdate();
            if (retVal > 0) {
                updateStatus = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return updateStatus;
    }

    @Override
    public boolean updateFortyPercentVoucheredBillStatus(int billNo) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        boolean updateStatus = false;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("UPDATE BILL_MAST SET BILL_STATUS_ID=7 WHERE BILL_NO=? AND BILL_STATUS_ID IN ('0','1','2') AND TYPE_OF_BILL='ARREAR' AND VCH_NO IS NOT NULL ");
            ps.setInt(1, billNo);
            int retVal = ps.executeUpdate();
            if (retVal > 0) {
                updateStatus = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return updateStatus;
    }

    public ArrayList getsubOfficeBillGroupList(String offCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList empBillGrpList = new ArrayList();
        boolean privFound = false;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select distinct t1.BILL_GROUP_ID,* from\n"
                    + "(SELECT  BILL_GROUP_MASTER.BILL_GROUP_ID, DESCRIPTION, PLAN, SECTOR, MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,\n"
                    + "SUB_MINOR_HEAD3,POST_CLASS,DEMAND_NO from\n"
                    + "(SELECT BILL_GROUP_ID, DESCRIPTION, PLAN, SECTOR, MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,POST_CLASS,DEMAND_NO \n"
                    + "from BILL_GROUP_MASTER WHERE OFF_CODE=(SELECT P_OFF_CODE FROM G_OFFICE WHERE IS_DDO='B' AND LVL='20' AND OFF_CODE=?) \n"
                    + "AND (IS_DELETED IS  NULL OR IS_DELETED='N'))BILL_GROUP_MASTER\n"
                    + "LEFT OUTER JOIN\n"
                    + "(SELECT BILL_GRP_ID FROM BILL_GROUP_PRIVILAGE WHERE SPC='') BILL_GROUP_PRIVILAGE\n"
                    + "ON BILL_GROUP_PRIVILAGE.BILL_GRP_ID = BILL_GROUP_MASTER.BILL_GROUP_ID::BIGINT\n"
                    + "INNER JOIN bill_section_mapping BSM ON BSM.BILL_GROUP_ID=BILL_GROUP_MASTER.BILL_GROUP_ID\n"
                    + "INNER JOIN g_section on g_section.section_id=BSM.section_id and g_section.off_code=?\n"
                    + "ORDER BY DESCRIPTION)t1");

            pstmt.setString(1, offCode);
            pstmt.setString(2, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                BillAttr billattr = new BillAttr();
                billattr.setBillgroupId(rs.getString("BILL_GROUP_ID"));
                billattr.setBillDesc(rs.getString("DESCRIPTION"));
                billattr.setChartofAcc(StringUtils.defaultString(rs.getString("DEMAND_NO")) + "-" + StringUtils.defaultString(rs.getString("MAJOR_HEAD")) + "-" + StringUtils.defaultString(rs.getString("SUB_MAJOR_HEAD")) + "-" + StringUtils.defaultString(rs.getString("MINOR_HEAD")) + "-" + StringUtils.defaultString(rs.getString("SUB_MINOR_HEAD1")) + "-" + StringUtils.defaultString(rs.getString("SUB_MINOR_HEAD2")) + "-" + StringUtils.defaultString(rs.getString("PLAN")) + "-" + StringUtils.defaultString(rs.getString("SUB_MINOR_HEAD3")) + "-" + StringUtils.defaultString(rs.getString("SECTOR")));
                privFound = true;
                empBillGrpList.add(billattr);
            }
            if (privFound == false) {
                pstmt = con.prepareStatement("SELECT BILL_GROUP_ID, DESCRIPTION, PLAN, SECTOR, MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,POST_CLASS,DEMAND_NO from BILL_GROUP_MASTER "
                        + "WHERE OFF_CODE=? AND (IS_DELETED IS  NULL OR IS_DELETED='N') ORDER BY DESCRIPTION");
                pstmt.setString(1, offCode);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    BillAttr billattr = new BillAttr();
                    billattr.setBillgroupId(rs.getString("BILL_GROUP_ID"));
                    billattr.setBillDesc(rs.getString("DESCRIPTION"));
                    billattr.setChartofAcc(StringUtils.defaultString(rs.getString("DEMAND_NO")) + "-" + StringUtils.defaultString(rs.getString("MAJOR_HEAD")) + "-" + StringUtils.defaultString(rs.getString("SUB_MAJOR_HEAD")) + "-" + StringUtils.defaultString(rs.getString("MINOR_HEAD")) + "-" + StringUtils.defaultString(rs.getString("SUB_MINOR_HEAD1")) + "-" + StringUtils.defaultString(rs.getString("SUB_MINOR_HEAD2")) + "-" + StringUtils.defaultString(rs.getString("PLAN")) + "-" + StringUtils.defaultString(rs.getString("SUB_MINOR_HEAD3")) + "-" + StringUtils.defaultString(rs.getString("SECTOR")));
                    empBillGrpList.add(billattr);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empBillGrpList;
    }

    @Override
    public int isContainingKeyDHE(String moffcode, int aqMonth, int aqYear, String bgId, String processDate, String billType, int billId, String regularOrcontractualBill, int priority) throws Exception {
        Connection con = null;

        PreparedStatement pst = null;

        Statement stamt = null;
        ResultSet res = null;
        ResultSet res1 = null;

        int containsKey = 0;
        int taskid = 0;
        String employeeQuery = "";
        try {
            con = this.dataSource.getConnection();
            /**
             * ******************THIS QUERY RETRIEVES ALL EMPLOYEES OF AN
             * OFFICE INCLUDING EMPLOYEES RELIEVED IN CURRENT
             * MONTH******************
             */

            if (regularOrcontractualBill.equalsIgnoreCase("REGULAR") || regularOrcontractualBill.equalsIgnoreCase("CONT6_REG")) {
                employeeQuery = "SELECT COUNT(*) CNT FROM( "
                        + "SELECT OFF_CODE, SECTION_POST_MAPPING.SPC,EMP_ID, NAME,SECTION_ID FROM ( "
                        + "SELECT OFF_CODE, SPC,EMP_ID, NAME    FROM "
                        + " (SELECT '" + moffcode + "'::TEXT OFF_CODE ,G_SPC.SPC,EMP_MAST.EMP_ID,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ')||' / '||G_SPC.SPN AS NAME "
                        + " FROM (SELECT * FROM G_SPC WHERE OFF_CODE ='" + moffcode + "' AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL))G_SPC "
                        + " LEFT OUTER JOIN "
                        + " (SELECT EMP_ID,CUR_SPC,GPF_NO,INITIALS,F_NAME,M_NAME,L_NAME FROM EMP_MAST WHERE CUR_OFF_CODE ='" + moffcode + "' AND (DEP_CODE='02' OR DEP_CODE='05') AND CUR_SPC IS NOT NULL) EMP_MAST "
                        + " ON  G_SPC.SPC = EMP_MAST.CUR_SPC "
                        + " INNER JOIN SECTION_POST_MAPPING ON G_SPC.SPC = SECTION_POST_MAPPING.SPC ORDER BY F_NAME) AS TAB1"
                        + " UNION "
                        + " (SELECT '" + moffcode + "'::TEXT OFF_CODE ,G_SPC.SPC,EMP_MAST.EMP_ID,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ')||' / '||G_SPC.SPN AS NAME "
                        + " FROM"
                        + " (SELECT SPC,EMP_ID FROM EMP_RELIEVE WHERE SUBSTR(SPC,0,13)='" + moffcode + "' AND TO_CHAR(RLV_DATE,'mm')='" + aqMonth + "' AND TO_CHAR(RLV_DATE,'yyyy')='" + aqYear + "' AND ((TO_CHAR(RLV_DATE,'dd') || RLV_TIME) != '01FN')) TMPRLV"
                        + " INNER JOIN "
                        + " (SELECT EMP_ID,CUR_SPC,GPF_NO,INITIALS,F_NAME,M_NAME,L_NAME FROM EMP_MAST) EMP_MAST "
                        + " ON TMPRLV.EMP_ID=EMP_MAST.EMP_ID"
                        + " LEFT OUTER JOIN"
                        + " (SELECT * FROM G_SPC WHERE OFF_CODE ='" + moffcode + "' AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL)) G_SPC "
                        + " ON TMPRLV.SPC = G_SPC.SPC) ) AS COMPLETE_QUERY "
                        + " LEFT OUTER JOIN SECTION_POST_MAPPING ON COMPLETE_QUERY.SPC=SECTION_POST_MAPPING.SPC ) TEMP "
                        + " INNER JOIN (SELECT * FROM BILL_SECTION_MAPPING WHERE BILL_GROUP_ID IN (" + bgId + ")) BILL_SECTION_MAPPING ON TEMP.SECTION_ID=BILL_SECTION_MAPPING.SECTION_ID ";
            } else if (regularOrcontractualBill.equalsIgnoreCase("CONTRACTUAL")) {
                employeeQuery = "SELECT COUNT(SPC)CNT FROM ( "
                        + " SELECT SECTION_ID,BILL_GROUP_ID FROM BILL_SECTION_MAPPING WHERE BILL_GROUP_ID IN (" + bgId + ")) AS TEMP "
                        + " INNER JOIN SECTION_POST_MAPPING ON TEMP.SECTION_ID=SECTION_POST_MAPPING.SECTION_ID";
            } else if (regularOrcontractualBill.equalsIgnoreCase("NONGOVTAID")) {
                employeeQuery = "SELECT COUNT(SPC)CNT FROM (\n"
                        + "SELECT SECTION_ID,BILL_GROUP_ID FROM BILL_SECTION_MAPPING WHERE BILL_GROUP_ID IN (" + bgId + ")) AS TEMP \n"
                        + "INNER JOIN SECTION_POST_MAPPING ON TEMP.SECTION_ID=SECTION_POST_MAPPING.SECTION_ID\n"
                        + "INNER JOIN G_SECTION ON SECTION_POST_MAPPING.SECTION_ID=G_SECTION.SECTION_ID AND OFF_CODE='" + moffcode + "'";
            }

            stamt = con.createStatement();
            String query = "";

            query = "SELECT TASK_ID FROM PAYBILL_TASK where OFF_CODE='" + moffcode + "' AND AQ_MONTH=" + aqMonth + " AND AQ_YEAR=" + aqYear + " AND BILL_GROUP_ID='" + bgId + "' AND BILL_TYPE='" + billType + "'";
            pst = con.prepareStatement(query);
            res = pst.executeQuery();
            if (res.next()) {
                taskid = res.getInt("TASK_ID");
                containsKey = taskid;
            } else {
                containsKey = 0;
            }
            System.out.println("containsKey:" + containsKey + "::" + billId);

            if (containsKey == 0) {
                if (billId == 0) {
                    //billId = createBillId(moffcode, bgId, processDate, billType, aqMonth, aqYear);
                    billId = getDHEBillDetails(moffcode, bgId, processDate, billType, aqMonth, aqYear);
                }
                stamt = con.createStatement();

                res = stamt.executeQuery(employeeQuery);
                int totalNoOfEmployee = 0;
                if (res.next()) {
                    totalNoOfEmployee = res.getInt("CNT");
                }
                if (totalNoOfEmployee > 0) {
                    stamt = con.createStatement();
                    res = stamt.executeQuery("select COALESCE(max(task_id),0)+1 taskid FROM paybill_task");
                    taskid = 0;
                    if (res.next()) {
                        taskid = res.getInt("TASKID");
                    }
                    stamt = con.createStatement();
                    stamt.execute("INSERT INTO PAYBILL_TASK (TASK_ID,OFF_CODE,AQ_MONTH,AQ_YEAR,TOTAL_AQ,BILL_ID,BILL_GROUP_ID,PRIORITY, bill_type) VALUES (" + taskid + ",'" + moffcode + "'," + aqMonth + "," + aqYear + "," + totalNoOfEmployee + "," + billId + ",'" + bgId + "'," + priority + ", '" + billType + "')");
                } else {
                    taskid = 0;
                }
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res1);
            DataBaseFunctions.closeSqlObjects(res, stamt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return taskid;
    }

    @Override
    public boolean isCollegeUnderDHE(String offcode) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean found = false;

        try {
            con = this.dataSource.getConnection();

            String sql = "select lvl,is_ddo,p_off_code,off_code from g_office where off_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            rs = pst.executeQuery();
            if (rs.next()) {
                if ((rs.getString("lvl") != null && rs.getString("lvl").equals("20")) && (rs.getString("is_ddo") != null && rs.getString("is_ddo").equals("B"))
                        && (rs.getString("p_off_code") != null && (rs.getString("p_off_code").equals("BBSEDN0240000") || rs.getString("p_off_code").equals("BBSEDU0010000")))) {
                    found = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return found;
    }

    @Override
    public BillAttr[] createBillFromBillGroupDHE(int mAqMonth, int mAqYear, String[] billGroupId, String processDt, int priority, String billType, String moffcode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        Date processDate = null;
        Date fyStatrtDate = null;
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int task[] = null;
        int taskid = 0;
        String regularOrcontractualBill = "";
        BillAttr[] billStatus = null;
        ArrayList<BillAttr> billStatusA = new ArrayList<>();
        int billNo = 0;
        try {
            con = dataSource.getConnection();
            if ((year >= mAqYear + 1) || (year == mAqYear && mAqMonth <= month)) {

                task = new int[billGroupId.length];

                for (int i = 0; i < billGroupId.length; i++) {

                    BillAttr billAttr = new BillAttr();
                    billAttr.setBillgroupId(billGroupId[i]);

                    pstmt = con.prepareStatement("SELECT * FROM BILL_MAST WHERE BILL_GROUP_ID=? AND AQ_MONTH=? AND AQ_YEAR=? and type_of_bill=?");
                    pstmt.setBigDecimal(1, new BigDecimal(billGroupId[i]));
                    pstmt.setInt(2, mAqMonth);
                    pstmt.setInt(3, mAqYear);

                    pstmt.setString(4, billType);
                    rs = pstmt.executeQuery();
                    boolean isDuplicateProcess = true;
                    if (rs.next()) {
                        System.out.println("BillNo:::::" + rs.getString("bill_no"));
                        billNo = rs.getInt("bill_no");
                        isDuplicateProcess = true;
                    } else {
                        isDuplicateProcess = false;
                    }
                    if (!isDuplicateProcess) {
                        processDate = (Date) formatter.parse(processDt);
                        fyStatrtDate = (Date) formatter.parse("1-APR-" + mAqYear);
                        pstmt = con.prepareStatement("SELECT BILL_TYPE FROM G_SECTION INNER JOIN (SELECT DISTINCT SECTION_ID FROM BILL_SECTION_MAPPING WHERE BILL_GROUP_ID=?)B "
                                + "ON G_SECTION.SECTION_ID = B.SECTION_ID AND OFF_CODE=?");
                        pstmt.setBigDecimal(1, new BigDecimal(billGroupId[i]));
                        pstmt.setString(2, moffcode);
                        rs = pstmt.executeQuery();

                        if (rs.next()) {
                            regularOrcontractualBill = rs.getString("BILL_TYPE");
                            System.out.println("billtype:" + rs.getString("BILL_TYPE"));
                        }
                        if (regularOrcontractualBill != null && !regularOrcontractualBill.equals("")) {
                            taskid = isContainingKeyDHE(moffcode, mAqMonth, mAqYear, billGroupId[i], processDt, billType, billNo, regularOrcontractualBill, priority);
                            task[i] = taskid;
                        }
                        if (taskid == 0) {
                            billAttr.setMsg("Bill Cannot be Processed. Because you have not assigned section to bill group list or you have not mapped any employee into section or bill type is blank.  ");
                        } else {
                            billAttr.setMsg("Bill is under Process");
                        }

                    } else {
                        processDate = (Date) formatter.parse(processDt);
                        fyStatrtDate = (Date) formatter.parse("1-APR-" + mAqYear);
                        pstmt = con.prepareStatement("SELECT distinct BILL_TYPE FROM G_SECTION INNER JOIN (SELECT DISTINCT SECTION_ID FROM BILL_SECTION_MAPPING WHERE BILL_GROUP_ID=?)B "
                                + "ON G_SECTION.SECTION_ID = B.SECTION_ID AND OFF_CODE=?");
                        pstmt.setBigDecimal(1, new BigDecimal(billGroupId[i]));
                        pstmt.setString(2, moffcode);
                        rs = pstmt.executeQuery();

                        if (rs.next()) {
                            regularOrcontractualBill = rs.getString("BILL_TYPE");
                            System.out.println("billtype:" + rs.getString("BILL_TYPE"));
                        }
                        if (regularOrcontractualBill != null && !regularOrcontractualBill.equals("")) {
                            taskid = isContainingKeyDHE(moffcode, mAqMonth, mAqYear, billGroupId[i], processDt, billType, billNo, regularOrcontractualBill, priority);
                            task[i] = taskid;
                        }
                        if (taskid == 0) {
                            billAttr.setMsg("Bill Cannot be Processed. Because you have not assigned section to bill group list or you have not mapped any employee into section or bill type is blank.  ");
                        } else {
                            billAttr.setMsg("Bill is under Process.........................");
                        }

                    }
                    billStatusA.add(billAttr);
                }
            }
            billStatus = billStatusA.toArray(new BillAttr[billStatusA.size()]);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billStatus;

    }

    @Override
    public int getDHEBillDetails(String offCode, String bgrId, String processDate, String billType, int aqmonth, int aqyear) throws Exception {
        Connection con = null;
        ResultSet rs2 = null;
        Statement st2 = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int mBillNo = 0;

        try {
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            con = dataSource.getConnection();
            st2 = con.createStatement();

            rs2 = st2.executeQuery("SELECT G_OFFICE.OFF_CODE pOffCode,BILL_GROUP_ID, DESCRIPTION,DEMAND_NO,PLAN,SECTOR,MAJOR_HEAD, MAJOR_HEAD_DESC, SUB_MAJOR_HEAD, SUB_MAJOR_HEAD_DESC, MINOR_HEAD, MINOR_HEAD_DESC, \n"
                    + "SUB_MINOR_HEAD1, SUB_MINOR_HEAD1_DESC, SUB_MINOR_HEAD2, SUB_MINOR_HEAD2_DESC, SUB_MINOR_HEAD3, SUB_MINOR_HEAD3_DESC,g_office.DDO_CODE,\n"
                    + "g_office.BANK_CODE,g_office.BRANCH_CODE,g_office.TR_CODE,BILL_TYPE, PAY_HEAD, BILL_GROUP_MASTER.co_code, ddo_hrmsid,\n"
                    + "ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, DDO_POST, post  FROM (\n"
                    + "SELECT * FROM BILL_GROUP_MASTER WHERE OFF_CODE=(SELECT P_OFF_CODE FROM G_OFFICE WHERE OFF_CODE='" + offCode + "') \n"
                    + "AND (IS_DELETED IS  NULL OR IS_DELETED='N') AND BILL_GROUP_ID =" + bgrId + "\n"
                    + ")BILL_GROUP_MASTER\n"
                    + "LEFT OUTER JOIN G_OFFICE ON BILL_GROUP_MASTER.OFF_CODE = G_OFFICE.OFF_CODE\n"
                    + "left outer join g_post on g_office.ddo_post=g_post.post_code\n"
                    + "left outer join emp_mast on g_office.ddo_hrmsid=emp_mast.emp_id");

            if (rs2.next()) {

                pst = con.prepareStatement("INSERT INTO BILL_MAST (BILL_DESC,BILL_DATE,BILL_TYPE,AQ_GROUP_DESC,AQ_MONTH,AQ_YEAR,OFF_CODE,DEMAND_NO,MAJOR_HEAD,MAJOR_HEAD_DESC,SUB_MAJOR_HEAD,SUB_MAJOR_HEAD_DESC,"
                        + "MINOR_HEAD,MINOR_HEAD_DESC,SUB_MINOR_HEAD1,SUB_MINOR_HEAD1_DESC,SUB_MINOR_HEAD2,SUB_MINOR_HEAD2_DESC,SUB_MINOR_HEAD3,BILL_GROUP_DESC,PLAN,SECTOR,DDO_CODE,BANK_CODE,BRANCH_CODE,"
                        + "TR_CODE,BILL_GROUP_ID,TYPE_OF_BILL, PAY_HEAD, CO_CODE, DDO_POST, ddo_empid, ddo_name, ddo_post_name,p_off_code)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                //pst.setInt(1, mBillNo);
                pst.setString(1, null);
                pst.setTimestamp(2, new Timestamp(dateFormat.parse(processDate).getTime()));
                pst.setString(3, rs2.getString("BILL_TYPE"));
                pst.setString(4, null);
                pst.setInt(5, aqmonth);
                pst.setInt(6, aqyear);
                pst.setString(7, offCode);
                pst.setString(8, rs2.getString("DEMAND_NO"));
                pst.setString(9, rs2.getString("MAJOR_HEAD"));
                pst.setString(10, rs2.getString("MAJOR_HEAD_DESC"));
                pst.setString(11, rs2.getString("SUB_MAJOR_HEAD"));
                pst.setString(12, rs2.getString("SUB_MAJOR_HEAD_DESC"));
                pst.setString(13, rs2.getString("MINOR_HEAD"));
                pst.setString(14, rs2.getString("MINOR_HEAD_DESC"));
                pst.setString(15, rs2.getString("SUB_MINOR_HEAD1"));
                pst.setString(16, rs2.getString("SUB_MINOR_HEAD1_DESC"));
                pst.setString(17, rs2.getString("SUB_MINOR_HEAD2"));
                pst.setString(18, rs2.getString("SUB_MINOR_HEAD2_DESC"));
                pst.setString(19, rs2.getString("SUB_MINOR_HEAD3"));
                pst.setString(20, rs2.getString("DESCRIPTION"));
                pst.setString(21, rs2.getString("PLAN"));
                pst.setString(22, rs2.getString("SECTOR"));
                pst.setString(23, rs2.getString("DDO_CODE"));
                pst.setString(24, rs2.getString("BANK_CODE"));
                pst.setString(25, rs2.getString("BRANCH_CODE"));
                pst.setString(26, rs2.getString("TR_CODE"));
                pst.setBigDecimal(27, new BigDecimal(bgrId));
                pst.setString(28, billType);
                pst.setString(29, rs2.getString("PAY_HEAD"));
                pst.setString(30, rs2.getString("co_code"));
                pst.setString(31, rs2.getString("DDO_POST"));
                pst.setString(32, rs2.getString("ddo_hrmsid"));
                pst.setString(33, rs2.getString("EMPNAME"));
                pst.setString(34, rs2.getString("post"));
                pst.setString(35, rs2.getString("pOffCode"));

                int ret = pst.executeUpdate();
                rs = pst.getGeneratedKeys();
                rs.next();
                mBillNo = rs.getInt("bill_no");
                System.out.println("mBillNo:" + mBillNo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs2, pst);
            DataBaseFunctions.closeSqlObjects(st2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return mBillNo;
    }

    @Override
    public ArrayList getVoucherListForCOA(int year, int month, String parentTrCode) {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        ArrayList billList = new ArrayList();

        String allTreasury = "";
        try {
            con = this.dataSource.getConnection();

            pstmt = con.prepareStatement("select tr_code from g_treasury where ag_treasury_code=?");
            pstmt.setString(1, parentTrCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (allTreasury.equals("")) {
                    allTreasury = "'" + rs.getString("tr_code") + "'";
                } else {
                    allTreasury = allTreasury + ",'" + rs.getString("tr_code") + "'";
                }
            }

            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            pstmt = con.prepareStatement("SELECT aq_month,aq_year,BILL_NO,major_head,sub_major_head,minor_head,sub_minor_head1,sub_minor_head2,sub_minor_head3,VCH_NO,VCH_DATE,extract (year from vch_date) vch_year, EXTRACT(MONTH FROM vch_date) vch_month,EXTRACT(DAY FROM vch_date) vch_day,ag_treasury_code,DDO_CODE FROM BILL_MAST bill "
                    + "inner join g_treasury treasury on bill.tr_code=treasury.tr_code WHERE extract (year from vch_date)=? AND extract (month from vch_date)=? AND bill.TR_CODE IN (" + allTreasury + ") AND bill.BILL_STATUS_ID=7 and bill.type_of_bill='PAY' and length(VCH_NO) > 4 and (challan_no is not null and challan_no <> '') order by major_head,substring(VCH_NO,5)::integer");
            pstmt.setInt(1, year);
            pstmt.setInt(2, (month + 1));
            //pstmt.setString(3, allTreasury);

            rs = pstmt.executeQuery();

            String financialYear = "";
            if (month > 2) {
                financialYear = year + "-" + (year + 1);
            } else {
                financialYear = (year - 1) + "-" + year;
            }
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
            while (rs.next()) {
                BillBean bb = new BillBean();
                bb.setBillMonth(rs.getInt("aq_month"));
                bb.setBillYear(rs.getInt("aq_year"));
                bb.setAdjYear(rs.getInt("vch_year"));
                bb.setBillno(rs.getString("BILL_NO"));
                if (rs.getString("VCH_NO").length() > 4) {
                    bb.setVoucherno(StringUtils.leftPad(StringUtils.substring(rs.getString("VCH_NO"), 4), 5, "0"));
                } else {
                    bb.setVoucherno(StringUtils.leftPad(rs.getString("VCH_NO"), 5, "0"));
                }

                if (rs.getDate("VCH_DATE") != null) {
                    bb.setVoucherdate(DATE_FORMAT.format(rs.getDate("VCH_DATE")));
                }
                bb.setVouchermonth(rs.getString("vch_month"));
                bb.setFinyear(financialYear);
                bb.setTreasurycode(rs.getString("ag_treasury_code"));
                bb.setDdocode(rs.getString("DDO_CODE"));
                bb.setMajorhead(rs.getString("major_head"));
                bb.setVoucherDay(rs.getInt("vch_day"));
                billList.add(bb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billList;
    }

    @Override
    public ArrayList getPayBillListDHE(int year, int month, String offCode, String billType, String spc) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<BillBean> billList = new ArrayList<>();
        try {
            //if (billType != null && billType.equalsIgnoreCase("PAY")) {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT BILL_NO,BILL_DATE,BILL_MAST.BILL_TYPE,BILL_MAST.TYPE_OF_BILL,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION , PAYBILL_TASK.BILL_ID IS_BILL_PREPARED,arrear_percent, BILL_MAST.BILL_GROUP_ID, \n"
                    + " DESCRIPTION, BILL_DESC,IS_DELETED, (select count(*) from bill_beneficiary where bill_id=BILL_MAST.bill_no) noof_emp   FROM ( \n"
                    + " SELECT BILL_NO,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION , IS_BILL_PREPARED,arrear_percent, BILL_GROUP_ID,DESCRIPTION, BILL_DESC,IS_DELETED FROM ( \n"
                    + " SELECT BILL_NO,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION , IS_BILL_PREPARED,arrear_percent, BILL_GROUP_ID,DESCRIPTION, BILL_DESC,IS_DELETED FROM ( \n"
                    + " SELECT BILL_NO,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION , IS_BILL_PREPARED,arrear_percent, BILL_GROUP_MASTER.BILL_GROUP_ID,DESCRIPTION, \n"
                    + " BILL_DESC,IS_DELETED FROM ( \n"
                    + " SELECT BILL_GROUP_ID, DESCRIPTION,IS_DELETED FROM BILL_GROUP_MASTER WHERE OFF_CODE=(SELECT P_OFF_CODE FROM G_OFFICE WHERE OFF_CODE=?)) BILL_GROUP_MASTER \n"
                    + " LEFT OUTER JOIN \n"
                    + " (SELECT BILL_NO,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION , IS_BILL_PREPARED, BILL_GROUP_ID, BILL_DESC, arrear_percent FROM ( \n"
                    + " SELECT BILL_NO,P_OFF_CODE,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_STATUS_ID,IS_BILL_PREPARED, BILL_GROUP_ID,OFF_CODE, BILL_DESC, arrear_percent FROM BILL_MAST WHERE  \n"
                    + " P_OFF_CODE=(SELECT P_OFF_CODE FROM G_OFFICE WHERE OFF_CODE=?) AND AQ_MONTH=? AND AQ_YEAR=? AND TYPE_OF_BILL=?) BILL_MAST \n"
                    + " INNER JOIN G_OFFICE ON BILL_MAST.P_OFF_CODE=G_OFFICE.OFF_CODE) BILL_MAST \n"
                    + " ON BILL_GROUP_MASTER.BILL_GROUP_ID=BILL_MAST.BILL_GROUP_ID  WHERE (IS_DELETED='N' OR IS_DELETED IS NULL) OR (IS_DELETED='Y' AND IS_BILL_PREPARED ='Y')  \n"
                    + " ORDER BY DESCRIPTION) BILL_MAST ) BILL_MAST ) BILL_MAST \n"
                    + " LEFT OUTER JOIN PAYBILL_TASK ON BILL_MAST.BILL_NO=PAYBILL_TASK.BILL_ID  order by BILL_NO");
            pstmt.setString(1, offCode);
            pstmt.setString(2, offCode);
            pstmt.setInt(3, month);
            pstmt.setInt(4, year);
            pstmt.setString(5, billType);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                BillBean bb = new BillBean();
                bb.setBillgroupId(rs.getString("BILL_GROUP_ID"));
                bb.setBillno(rs.getString("BILL_NO"));
                bb.setBilldesc(rs.getString("BILL_DESC"));
                bb.setBilldate(CommonFunctions.getFormattedOutputDate1(rs.getDate("BILL_DATE")));
                bb.setBilltype(rs.getString("TYPE_OF_BILL"));
                bb.setBillGroupDesc(rs.getString("DESCRIPTION"));
                if (rs.getInt("BILL_STATUS_ID") == 0) {
                    bb.setShowLink("");
                } else if (rs.getInt("BILL_STATUS_ID") == 2 || rs.getInt("BILL_STATUS_ID") == 4 || rs.getInt("BILL_STATUS_ID") == 8) {
                    bb.setShowLink("Y");
                } else if (rs.getInt("BILL_STATUS_ID") > 2 && rs.getInt("BILL_STATUS_ID") != 4 && rs.getInt("BILL_STATUS_ID") != 8) {
                    bb.setShowLink("N");
                } else {
                    bb.setShowLink("");
                }
                bb.setLockBill(rs.getInt("BILL_STATUS_ID"));
                bb.setOnlinebillapproved(rs.getString("ONLINE_BILL_SUBMISSION"));
                bb.setNooofEmployee(rs.getInt("noof_emp"));
                if (rs.getString("IS_BILL_PREPARED") != null && !rs.getString("IS_BILL_PREPARED").equals("")) {
                    bb.setIsbillPrepared("N");
                } else {
                    bb.setIsbillPrepared("Y");
                }
                bb.setPercentageArraer(rs.getInt("arrear_percent") + "");

                if (bb.getBillno() != null && !bb.getBillno().equals("")) {
                    bb.setFtcount(getFailedTransactionCount(Integer.parseInt(bb.getBillno())));
                } else {
                    bb.setFtcount(0);
                }
                if (bb.getBillno() != null && !bb.getBillno().equals("")) {
                    bb.setCheckESignStatus(checkEsign(offCode, bb.getBillno()));
                }
                bb.setFrontpageslno(getBillFrontPageSlNo(bb.getBilltype()) + "");

                billList.add(bb);
            }

            //}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return billList;
    }

    @Override
    public BillDetail getBillDetailsDHE(int billno, String offCode) {
        BillDetail bill = null;
        VouchingServicesDAOImpl vserv = new VouchingServicesDAOImpl();
        Calendar cal = Calendar.getInstance();

        String month = "";
        String billdate = "";
        String periodFrom = "";
        String periodTo = "";
        String ddocode = "";
        String treasuryCode = "";

        int year = 0;
        int billmonth = 0;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        try {
            con = this.dataSource.getConnection();

            sql = "SELECT ddo_hrmsid,ddo_post,BILL_MAST.P_OFF_CODE,OFF_NAME,OFF_DDO,BILL_MAST.DDO_CODE,BILL_NO,BILL_DATE,BILL_DESC,PLAN,SECTOR,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,\n"
                    + "SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,BILL_TYPE,BILL_MAST.TR_CODE,AQ_MONTH,AQ_YEAR,DEMAND_NO,BEN_REF_NO,PREVIOUS_TOKEN_NO,PREVIOUS_TOKEN_DATE,\n"
                    + "IS_RESUBMITTED,type_of_bill,bill_status_id,bill_group_id, vch_no, vch_date, gross_amt ,\n"
                    + "ded_amt , pvt_ded_amt, FROM_MONTH, FROM_YEAR, TO_MONTH, TO_YEAR, FROM_DATE, TO_DATE, bill_mast.co_code FROM (\n"
                    + "SELECT bill_mast.OFF_CODE,P_OFF_CODE,BILL_NO,BILL_DATE,BILL_DESC,OFF_DDO,bill_mast.DDO_CODE,PLAN,SECTOR,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,\n"
                    + "SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,BILL_TYPE,bill_mast.TR_CODE,AQ_MONTH,AQ_YEAR,DEMAND_NO,BEN_REF_NO,PREVIOUS_TOKEN_NO,PREVIOUS_TOKEN_DATE,IS_RESUBMITTED, type_of_bill,\n"
                    + "bill_status_id, bill_group_id, vch_no, vch_date, gross_amt ,ded_amt , pvt_ded_amt, FROM_MONTH, FROM_YEAR, TO_MONTH, TO_YEAR, FROM_DATE,TO_DATE, bill_mast.co_code\n"
                    + "FROM BILL_MAST WHERE BILL_NO=? AND P_OFF_CODE=(SELECT P_OFF_CODE FROM G_OFFICE WHERE OFF_CODE=?)) BILL_MAST\n"
                    + "INNER JOIN G_OFFICE ON BILL_MAST.P_OFF_CODE=G_OFFICE.OFF_CODE ";
            ps = con.prepareStatement(sql);
            ps.setInt(1, billno);
            ps.setString(2, offCode);
            rs = ps.executeQuery();
            if (rs.next()) {

                bill = new BillDetail();
                bill.setDdohrmisd(rs.getString("ddo_hrmsid"));
                bill.setDdodesgn(rs.getString("ddo_post"));
                bill.setBillgroupId(rs.getString("bill_group_id"));
                bill.setHrmsgeneratedRefno(rs.getString("BILL_NO"));
                billdate = CommonFunctions.getFormattedOutputDate3(rs.getDate("BILL_DATE"));
                bill.setHrmsgeneratedRefdate(billdate);
                bill.setBillType("N");
                bill.setBillnumber(rs.getString("BILL_DESC"));
                bill.setBillDate(billdate);
                bill.setBilldesc(rs.getString("BILL_DESC"));
                year = rs.getInt("AQ_YEAR");
                billmonth = rs.getInt("AQ_MONTH");
                bill.setBillyear(year);
                bill.setBillmonth(billmonth + 1);
                bill.setProcessFromDate(rs.getInt("FROM_DATE") + "");
                bill.setProcessToDate(rs.getInt("TO_DATE") + "");
                bill.setToMonth(rs.getInt("TO_MONTH"));
                bill.setToYear(rs.getInt("TO_YEAR"));
                if (rs.getString("AQ_MONTH") != null) {

                    month = CalendarCommonMethods.getFullMonthAsString((rs.getInt("AQ_MONTH")));
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, rs.getInt("AQ_MONTH"));
                    cal.set(Calendar.DATE, 1);
                }
                SimpleDateFormat date_format = new SimpleDateFormat("dd-MMM-yyyy");

                bill.setAgbillTypeId(rs.getString("BILL_TYPE"));
                if (rs.getString("type_of_bill").equals("ARREAR") || rs.getString("type_of_bill").equals("ARREAR_6")) {
                    bill.setSalFromdate("01-JAN-2016");
                } else if (rs.getString("type_of_bill").equals("OTHER_ARREAR") || rs.getString("type_of_bill").equals("ARREAR_J") || rs.getString("type_of_bill").equals("ARREAR_6_J")) {
                    Calendar cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("FROM_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("FROM_YEAR"));
                    cal2.set(Calendar.DATE, rs.getInt("FROM_DATE"));
                    bill.setSalFromdate(date_format.format(cal2.getTime()));

                } else if (rs.getString("type_of_bill").equals("ARREAR_NPS")) {
                    Calendar cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("FROM_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("FROM_YEAR"));
                    cal2.set(Calendar.DATE, rs.getInt("FROM_DATE"));
                    bill.setSalFromdate(date_format.format(cal2.getTime()));

                } else if (rs.getString("type_of_bill").equals("OA_ARREAR")) {
                    Calendar cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("FROM_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("FROM_YEAR"));
                    cal2.set(Calendar.DATE, rs.getInt("FROM_DATE"));
                    bill.setSalFromdate(date_format.format(cal2.getTime()));
                } else if (rs.getString("type_of_bill").equals("LEAVE_ARREAR")) {
                    Calendar cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("FROM_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("FROM_YEAR"));
                    cal2.set(Calendar.DATE, rs.getInt("FROM_DATE"));
                    bill.setSalFromdate(date_format.format(cal2.getTime()));
                } else if (rs.getString("type_of_bill").equals("UNUTILISED_LEAVE_ARREAR")) {
                    Calendar cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("FROM_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("FROM_YEAR"));
                    cal2.set(Calendar.DATE, rs.getInt("FROM_DATE"));
                    bill.setSalFromdate(date_format.format(cal2.getTime()));
                } else if (rs.getString("type_of_bill").equals("PAY")) {
                    bill.setSalFromdate(date_format.format(cal.getTime()));
                }

                periodFrom = bill.getSalFromdate();
                cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                if (rs.getString("type_of_bill").equals("ARREAR") || rs.getString("type_of_bill").equals("ARREAR_6")) {
                    bill.setSalTodate("31-AUG-2017");
                } else if (rs.getString("type_of_bill").equals("OTHER_ARREAR") || rs.getString("type_of_bill").equals("ARREAR_J") || rs.getString("type_of_bill").equals("ARREAR_6_J")) {
                    Calendar cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("TO_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("TO_YEAR"));
                    cal2.set(Calendar.DATE, rs.getInt("TO_DATE"));

                    bill.setSalTodate(date_format.format(cal2.getTime()));

                } else if (rs.getString("type_of_bill").equals("ARREAR_NPS")) {
                    Calendar cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("TO_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("TO_YEAR"));
                    cal2.set(Calendar.DATE, rs.getInt("TO_DATE"));

                    bill.setSalTodate(date_format.format(cal2.getTime()));

                } else if (rs.getString("type_of_bill").equals("OA_ARREAR")) {
                    Calendar cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("TO_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("TO_YEAR"));
                    cal2.set(Calendar.DATE, rs.getInt("TO_DATE"));

                    bill.setSalTodate(date_format.format(cal2.getTime()));

                } else if (rs.getString("type_of_bill").equals("LEAVE_ARREAR")) {
                    Calendar cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("TO_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("TO_YEAR"));
                    cal2.set(Calendar.DATE, rs.getInt("TO_DATE"));

                    bill.setSalTodate(date_format.format(cal2.getTime()));

                } else if (rs.getString("type_of_bill").equals("UNUTILISED_LEAVE_ARREAR")) {
                    Calendar cal2 = Calendar.getInstance();

                    cal2.set(Calendar.MONTH, (rs.getInt("TO_MONTH") - 1));
                    cal2.set(Calendar.YEAR, rs.getInt("TO_YEAR"));
                    cal2.set(Calendar.DATE, rs.getInt("TO_DATE"));

                    bill.setSalTodate(date_format.format(cal2.getTime()));

                } else if (rs.getString("type_of_bill").equals("PAY")) {
                    bill.setSalTodate(date_format.format(cal.getTime()));
                }

                periodTo = bill.getSalTodate();
                ddocode = rs.getString("DDO_CODE");
                bill.setOffcode(rs.getString("p_off_code"));
                bill.setDdoccode(ddocode);
                bill.setDemandNumber(rs.getString("DEMAND_NO"));
                bill.setMajorHead(rs.getString("MAJOR_HEAD"));
                bill.setSubMajorHead(rs.getString("SUB_MAJOR_HEAD"));
                bill.setMinorHead(rs.getString("MINOR_HEAD"));
                bill.setSubHead(rs.getString("SUB_MINOR_HEAD1"));
                bill.setDetailHead(rs.getString("SUB_MINOR_HEAD2"));
                bill.setPlanStatus(rs.getString("PLAN"));
                bill.setChargedVoted(rs.getString("SUB_MINOR_HEAD3"));
                bill.setSectorCode(rs.getString("SECTOR"));
                bill.setTypeofBillString(rs.getString("type_of_bill"));
                bill.setVchDt(CommonFunctions.getFormattedOutputDate3(rs.getDate("vch_date")));
                bill.setVchNo(rs.getString("vch_no"));
                bill.setPrevTokenNumber(null);
                bill.setPrevTokendate(null);
                treasuryCode = rs.getString("TR_CODE");
                bill.setTreasuryCode(treasuryCode);
                //bill.setBeneficiaryrefno(rs.getString("BEN_REF_NO"));
                if (rs.getString("IS_RESUBMITTED") != null && rs.getString("IS_RESUBMITTED").equals("Y")) {
                    bill.setBillType("R");
                    bill.setPrevTokenNumber(rs.getString("PREVIOUS_TOKEN_NO"));
                    bill.setPrevTokendate(CommonFunctions.getFormattedOutputDate3(rs.getDate("PREVIOUS_TOKEN_DATE")));
                }
                if (rs.getString("type_of_bill").equals("OTHER_ARREAR")) {
                    String billgross = getGrossForArrear(billno);
                    if (billgross != null && !billgross.equals("")) {
                        bill.setGrossAmount(Double.valueOf(billgross).longValue() + "");

                        int net = Integer.parseInt(billgross);
                        String totded = getTotalDeductionForArrear(billno);
                        if (totded != null && !totded.equals("")) {
                            net = Integer.parseInt(billgross) - Integer.parseInt(totded);
                        }
                        bill.setNetAmount(Double.valueOf(net + "").longValue() + "");
                    }
                } else if (rs.getString("type_of_bill").equals("OA_ARREAR")) {
                    String billgross = getGrossForArrear(billno);
                    if (billgross != null && !billgross.equals("")) {
                        bill.setGrossAmount(Double.valueOf(billgross).longValue() + "");

                        int net = Integer.parseInt(billgross);
                        String totded = getTotalDeductionForArrear(billno);
                        if (totded != null && !totded.equals("")) {
                            net = Integer.parseInt(billgross) - Integer.parseInt(totded);
                        }
                        bill.setNetAmount(Double.valueOf(net + "").longValue() + "");
                    }
                } else if (rs.getString("type_of_bill").equals("LEAVE_ARREAR")) {
                    String billgross = getGrossForArrear(billno);
                    if (billgross != null && !billgross.equals("")) {
                        bill.setGrossAmount(Double.valueOf(billgross).longValue() + "");

                        int net = Integer.parseInt(billgross);
                        String totded = getTotalDeductionForArrear(billno);
                        if (totded != null && !totded.equals("")) {
                            net = Integer.parseInt(billgross) - Integer.parseInt(totded);
                        }
                        bill.setNetAmount(Double.valueOf(net + "").longValue() + "");
                    }
                } else if (rs.getString("type_of_bill").equals("UNUTILISED_LEAVE_ARREAR")) {
                    String billgross = getGrossForArrear(billno);
                    if (billgross != null && !billgross.equals("")) {
                        bill.setGrossAmount(Double.valueOf(billgross).longValue() + "");

                        int net = Integer.parseInt(billgross);
                        String totded = getTotalDeductionForArrear(billno);
                        if (totded != null && !totded.equals("")) {
                            net = Integer.parseInt(billgross) - Integer.parseInt(totded);
                        }
                        bill.setNetAmount(Double.valueOf(net + "").longValue() + "");
                    }
                } else if (rs.getString("type_of_bill").equals("ARREAR_NPS")) {
                    String billgross = getGrossForNPSArrear(billno);
                    if (billgross != null && !billgross.equals("")) {
                        bill.setGrossAmount(Double.valueOf(billgross).longValue() + "");

                        int net = Integer.parseInt(billgross);
                        String totded = getTotalDeductionForArrear(billno);
                        if (totded != null && !totded.equals("")) {
                            net = Integer.parseInt(billgross) - Integer.parseInt(totded);
                        }
                        bill.setNetAmount("0");
                    }
                } else {
                    bill.setGrossAmount(Double.valueOf(rs.getInt("gross_amt") + "").longValue() + "");
                    int net = rs.getInt("gross_amt") - rs.getInt("ded_amt");
                    bill.setNetAmount(Double.valueOf(net + "").longValue() + "");
                }

                bill.setBillStatusId(rs.getInt("bill_status_id"));
                bill.setCoCode(rs.getString("co_code"));

            }
            System.out.println("coco:" + bill.getCoCode());

            DataBaseFunctions.closeSqlObjects(rs, ps);
            if (bill.getCoCode() != null && !bill.getCoCode().equals("")) {

                ps = con.prepareStatement("select off_en from g_office where co_code=?");
                ps.setString(1, bill.getCoCode());
                rs = ps.executeQuery();
                if (rs.next()) {
                    bill.setCoName(rs.getString("off_en"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return bill;
    }

    @Override
    public ArrayList getBillPrepareYearDHE(String offCode) {
        Connection con = null;
        ResultSet rs = null;
        ArrayList al = new ArrayList();
        SelectOption so;
        PreparedStatement pstmt = null;
        try {

            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT DISTINCT AQ_YEAR FROM BILL_MAST WHERE P_OFF_CODE=(SELECT P_OFF_CODE FROM G_OFFICE WHERE OFF_CODE=?) ORDER  BY AQ_YEAR DESC");
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("AQ_YEAR"));
                so.setValue(rs.getString("AQ_YEAR"));
                al.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public ArrayList getBillPrepareYearDDODHE(String offCode) {
        Connection con = null;
        ResultSet rs = null;
        ArrayList al = new ArrayList();
        SelectOption so;
        PreparedStatement pstmt = null;
        try {

            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT DISTINCT AQ_YEAR FROM BILL_MAST WHERE P_OFF_CODE=? ORDER  BY AQ_YEAR DESC");
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("AQ_YEAR"));
                so.setValue(rs.getString("AQ_YEAR"));
                al.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public ArrayList getMonthFromSelectedYearDHE(String offCode, int year, String txtBillType) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList month = new ArrayList();
        SelectOption so;
        try {

            if (year != 0) {
                con = dataSource.getConnection();
                if (txtBillType != null && txtBillType.contains("ARREAR")) {
                    pstmt = con.prepareStatement("SELECT DISTINCT AQ_MONTH FROM BILL_MAST WHERE P_OFF_CODE=(SELECT P_OFF_CODE FROM G_OFFICE WHERE OFF_CODE=?) AND AQ_YEAR=? AND TYPE_OF_BILL like '%ARREAR%' ORDER BY AQ_MONTH DESC");
                    pstmt.setString(1, offCode);
                    pstmt.setInt(2, year);
                    rs = pstmt.executeQuery();
                    while (rs.next()) {
                        so = new SelectOption();
                        so.setValue(rs.getString("AQ_MONTH"));
                        so.setLabel(CalendarCommonMethods.getFullMonthAsString(rs.getInt("AQ_MONTH")));
                        month.add(so);
                    }
                } else {
                    pstmt = con.prepareStatement("SELECT DISTINCT AQ_MONTH FROM BILL_MAST WHERE P_OFF_CODE=(SELECT P_OFF_CODE FROM G_OFFICE WHERE OFF_CODE=?) AND AQ_YEAR=? AND TYPE_OF_BILL=? ORDER BY AQ_MONTH DESC");
                    pstmt.setString(1, offCode);
                    pstmt.setInt(2, year);
                    pstmt.setString(3, txtBillType);
                    rs = pstmt.executeQuery();
                    while (rs.next()) {
                        so = new SelectOption();
                        so.setValue(rs.getString("AQ_MONTH"));
                        so.setLabel(CalendarCommonMethods.getFullMonthAsString(rs.getInt("AQ_MONTH")));
                        month.add(so);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return month;

    }

    @Override
    public boolean isDDODHE(String offcode) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean found = false;

        try {
            con = this.dataSource.getConnection();

            String sql = "select lvl,is_ddo,p_off_code,off_code from g_office where off_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("lvl") != null && rs.getString("lvl").equals("03") && (rs.getString("is_ddo") != null && rs.getString("is_ddo").equals("Y"))
                        && (rs.getString("off_code").equals("BBSEDN0240000") || rs.getString("off_code").equals("BBSEDU0010000"))) {
                    found = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return found;
    }

    @Override
    public ArrayList getPayBillListDHEColleges(int year, int month, String offCode, String billType, String spc) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<BillBean> billList = new ArrayList<>();
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("select distinct t1.bill_group_id bgid,t1.*,is_bill_verified from \n"
                    + " (SELECT BILL_NO,BILL_DATE,BILL_MAST.BILL_TYPE,BILL_MAST.TYPE_OF_BILL,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION , PAYBILL_TASK.BILL_ID IS_BILL_PREPARED,arrear_percent,  \n"
                    + " BILL_MAST.BILL_GROUP_ID, \n"
                    + " DESCRIPTION, BILL_DESC,IS_DELETED, (select count(*) from bill_beneficiary where bill_id=BILL_MAST.bill_no) noof_emp   FROM ( \n"
                    + " SELECT BILL_NO,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION , IS_BILL_PREPARED,arrear_percent, BILL_GROUP_ID,DESCRIPTION, BILL_DESC,IS_DELETED FROM ( \n"
                    + " SELECT BILL_NO,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION , IS_BILL_PREPARED,arrear_percent, BILL_GROUP_ID,DESCRIPTION, BILL_DESC,IS_DELETED FROM ( \n"
                    + " SELECT BILL_NO,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION , IS_BILL_PREPARED,arrear_percent, BILL_GROUP_MASTER.BILL_GROUP_ID,DESCRIPTION, \n"
                    + " BILL_DESC,IS_DELETED FROM ( \n"
                    + " SELECT BILL_GROUP_ID, DESCRIPTION,IS_DELETED FROM BILL_GROUP_MASTER WHERE OFF_CODE=(SELECT P_OFF_CODE FROM G_OFFICE WHERE OFF_CODE=?)) BILL_GROUP_MASTER \n"
                    + " LEFT OUTER JOIN \n"
                    + " (SELECT BILL_NO,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION , IS_BILL_PREPARED, BILL_GROUP_ID, BILL_DESC, arrear_percent FROM ( \n"
                    + " SELECT BILL_NO,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_STATUS_ID,IS_BILL_PREPARED, BILL_GROUP_ID,OFF_CODE, BILL_DESC, arrear_percent FROM BILL_MAST WHERE  \n"
                    + " P_OFF_CODE=(SELECT P_OFF_CODE FROM G_OFFICE WHERE OFF_CODE=?) AND AQ_MONTH=? AND AQ_YEAR=? AND TYPE_OF_BILL=?) BILL_MAST \n"
                    + " INNER JOIN G_OFFICE ON BILL_MAST.OFF_CODE=G_OFFICE.OFF_CODE) BILL_MAST \n"
                    + " ON BILL_GROUP_MASTER.BILL_GROUP_ID=BILL_MAST.BILL_GROUP_ID  WHERE (IS_DELETED='N' OR IS_DELETED IS NULL) OR (IS_DELETED='Y' AND IS_BILL_PREPARED ='Y') \n"
                    + " ORDER BY DESCRIPTION) BILL_MAST ) BILL_MAST ) BILL_MAST \n"
                    + " LEFT OUTER JOIN PAYBILL_TASK ON BILL_MAST.BILL_NO=PAYBILL_TASK.BILL_ID and PAYBILL_TASK.off_code=?  order by BILL_NO)t1 \n"
                    + " INNER JOIN \n"
                    + " (SELECT SEC_SL_NO,GS.SECTION_ID,SECTION_NAME,GOFF.OFF_CODE,BILL_GROUP_ID,OFF_EN,LVL FROM \n"
                    + " (SELECT SEC_SL_NO,G_SECTION.SECTION_ID,BILL_GROUP_ID,SECTION_NAME,OFF_CODE FROM (SELECT SECTION_ID,BILL_GROUP_ID,SEC_SL_NO FROM BILL_SECTION_MAPPING \n"
                    + " ) BILL_SECTION_MAPPING \n"
                    + " LEFT OUTER JOIN  G_SECTION  ON BILL_SECTION_MAPPING.SECTION_ID=G_SECTION.SECTION_ID ORDER BY SEC_SL_NO)GS \n"
                    + " LEFT OUTER JOIN (SELECT * FROM G_OFFICE WHERE LVL='20' and IS_DDO='B')GOFF ON GS.OFF_CODE=GOFF.OFF_CODE \n"
                    + " where GOFF.off_code=? ORDER BY OFF_EN)t2 on \n"
                    + " t1.bill_group_id=t2.bill_group_id\n"
                    + " LEFT OUTER JOIN  (SELECT  DISTINCT BILL_NO,is_bill_verified FROM AQ_MAST WHERE OFF_CODE=? AND AQ_MONTH=? AND AQ_YEAR=?) AQM\n"
                    + " ON AQM.BILL_NO=T1.BILL_NO");
            pstmt.setString(1, offCode);
            pstmt.setString(2, offCode);
            pstmt.setInt(3, month);
            pstmt.setInt(4, year);
            pstmt.setString(5, billType);
            pstmt.setString(6, offCode);
            pstmt.setString(7, offCode);
            pstmt.setString(8, offCode);
            pstmt.setInt(9, month);
            pstmt.setInt(10, year);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                BillBean bb = new BillBean();
                bb.setBillgroupId(rs.getString("BILL_GROUP_ID"));
                bb.setBillno(rs.getString("BILL_NO"));
                bb.setBilldesc(rs.getString("BILL_DESC"));
                bb.setBilldate(CommonFunctions.getFormattedOutputDate1(rs.getDate("BILL_DATE")));
                bb.setBilltype(rs.getString("TYPE_OF_BILL"));
                bb.setBillGroupDesc(rs.getString("DESCRIPTION"));
                if (rs.getInt("BILL_STATUS_ID") == 0) {
                    bb.setShowLink("");
                } else if (rs.getInt("BILL_STATUS_ID") == 2 || rs.getInt("BILL_STATUS_ID") == 4 || rs.getInt("BILL_STATUS_ID") == 8) {
                    bb.setShowLink("Y");
                } else if (rs.getInt("BILL_STATUS_ID") > 2 && rs.getInt("BILL_STATUS_ID") != 4 && rs.getInt("BILL_STATUS_ID") != 8) {
                    bb.setShowLink("N");
                } else {
                    bb.setShowLink("");
                }
                bb.setLockBill(rs.getInt("BILL_STATUS_ID"));
                bb.setOnlinebillapproved(rs.getString("ONLINE_BILL_SUBMISSION"));
                bb.setNooofEmployee(rs.getInt("noof_emp"));
                if (rs.getString("IS_BILL_PREPARED") != null && !rs.getString("IS_BILL_PREPARED").equals("")) {
                    bb.setIsbillPrepared("N");
                } else {
                    bb.setIsbillPrepared("Y");
                }
                bb.setPercentageArraer(rs.getInt("arrear_percent") + "");

                if (bb.getBillno() != null && !bb.getBillno().equals("")) {
                    bb.setFtcount(getFailedTransactionCount(Integer.parseInt(bb.getBillno())));
                } else {
                    bb.setFtcount(0);
                }

                bb.setIsbillVerified(rs.getString("is_bill_verified"));

                billList.add(bb);
            }

            //}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return billList;
    }

    @Override
    public ArrayList getSinglePageBillPrepareYear(String ddoCode) {
        Connection con = null;
        ResultSet rs = null;
        ArrayList al = new ArrayList();
        SelectOption so;
        PreparedStatement pstmt = null;
        try {

            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT DISTINCT AQ_YEAR FROM BILL_MAST WHERE DDO_CODE=?  ORDER  BY AQ_YEAR DESC");
            pstmt.setString(1, ddoCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("AQ_YEAR"));
                so.setValue(rs.getString("AQ_YEAR"));
                al.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public ArrayList getSinglePageMonthFromSelectedYear(String ddoCode, int year, String txtBillType) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList month = new ArrayList();
        SelectOption so;
        try {

            if (year != 0) {
                con = dataSource.getConnection();
                if (txtBillType != null && txtBillType.contains("ARREAR")) {
                    pstmt = con.prepareStatement("SELECT DISTINCT AQ_MONTH FROM BILL_MAST WHERE DDO_CODE=? AND AQ_YEAR=? AND TYPE_OF_BILL like '%ARREAR%' ORDER BY AQ_MONTH DESC");
                    pstmt.setString(1, ddoCode);
                    pstmt.setInt(2, year);
                    rs = pstmt.executeQuery();
                    while (rs.next()) {
                        so = new SelectOption();
                        so.setValue(rs.getString("AQ_MONTH"));
                        so.setLabel(CalendarCommonMethods.getFullMonthAsString(rs.getInt("AQ_MONTH")));
                        month.add(so);
                    }
                } else {
                    pstmt = con.prepareStatement("SELECT DISTINCT AQ_MONTH FROM BILL_MAST WHERE DDO_CODE=? AND AQ_YEAR=? AND TYPE_OF_BILL=? ORDER BY AQ_MONTH DESC");
                    pstmt.setString(1, ddoCode);
                    pstmt.setInt(2, year);
                    pstmt.setString(3, txtBillType);
                    rs = pstmt.executeQuery();
                    while (rs.next()) {
                        so = new SelectOption();
                        so.setValue(rs.getString("AQ_MONTH"));
                        so.setLabel(CalendarCommonMethods.getFullMonthAsString(rs.getInt("AQ_MONTH")));
                        month.add(so);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return month;
    }

    @Override
    public double getbillbenificiaryGross(int billId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        double grossAmt = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select sum(gross_amt) grossAmt from bill_beneficiary where bill_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, billId);
            rs = pst.executeQuery();
            if (rs.next()) {
                grossAmt = rs.getDouble("grossAmt");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return grossAmt;
    }

    @Override
    public List getAllFixedCpfEmployeesInBill(int billNo, String billType) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int cntFixedPranEmp = 0;
        List npsEmplist = new ArrayList();
        BillBrowserbean bbean = null;
        try {
            con = this.dataSource.getConnection();
            if (billType.equals("PAY")) {
                ps = con.prepareStatement("select emp_mast.emp_id from\n"
                        + "aq_mast inner join emp_mast on aq_mast.emp_code=emp_mast.emp_id \n"
                        + "inner join update_ad_info on aq_mast.emp_code=update_ad_info.updation_ref_code\n"
                        + "where  bill_no=? and emp_mast.acct_type='PRAN' and \n"
                        + "if_gpf_assumed='N' and bt_id='57740' and fixedvalue > 0");
                ps.setInt(1, billNo);

            } else if (billType.contains("ARREAR")) {
                ps = con.prepareStatement("select emp_mast.emp_id from\n"
                        + "arr_mast inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id \n"
                        + "inner join update_ad_info on arr_mast.emp_id=update_ad_info.updation_ref_code\n"
                        + "where  bill_no=? and emp_mast.acct_type='PRAN' and \n"
                        + "if_gpf_assumed='N' and bt_id='57740' and fixedvalue > 0");
                ps.setInt(1, billNo);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                bbean = new BillBrowserbean();
                bbean.setBillEmpid(rs.getString("emp_id"));
                npsEmplist.add(bbean);
            }

        } catch (Exception e) {
            e.printStackTrace();;
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return npsEmplist;

    }

    @Override
    public BillDetail unlockBillForError(BillDetail billDetail, String ipaddress, String dcLoginId) {

        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pst1 = null;
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("update bill_mast set bill_status_id=4 where bill_status_id=3 and bill_no=?");
            ps.setInt(1, Integer.parseInt(billDetail.getBillnumber()));
            ps.executeUpdate();

            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            String Cdate = ft.format(dNow);

            String sqlString = dcLoginId + " has unlocked bill no " + billDetail.getBillnumber() + " on " + new Date();
            pst1 = con.prepareStatement("INSERT INTO monitor_emp_log (login_by,login_date,emp_id,sql_text_query,IP_ADDRESS) values(?,?,?,?,?)");
            pst1.setString(1, dcLoginId);
            pst1.setTimestamp(2, new Timestamp(new Date().getTime()));
            pst1.setString(3, billDetail.getBillnumber());
            pst1.setString(4, sqlString);
            pst1.setString(5, ipaddress);
            pst1.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, pst1);
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public String[] getSignedSinglePageSalaryPDFPath(int billno, int reportrefslno) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String[] signedpdfpathandfile = new String[2];
        try {
            con = this.dataSource.getConnection();

            String sql = "select signed_pdf_path,signed_pdf_file from esign_log where bill_no=? and report_ref_slno=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, billno);
            pst.setInt(2, reportrefslno);
            rs = pst.executeQuery();
            if (rs.next()) {
                signedpdfpathandfile[0] = rs.getString("signed_pdf_path");
                signedpdfpathandfile[1] = rs.getString("signed_pdf_file");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return signedpdfpathandfile;
    }

    @Override
    public String getESignedFileStatus(int billno) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String esignfileName = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select signed_pdf_file from esign_log where bill_no=? "
                    + "and (signed_pdf_path is not null or signed_pdf_path<>'') and signed_pdf_file like '%_signed%' and report_ref_slno=2");
            ps.setInt(1, billno);
            rs = ps.executeQuery();
            if (rs.next()) {
                esignfileName = rs.getString("signed_pdf_file");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return esignfileName;
    }

    @Override
    public String getESignedFileStatusForArrearBill(int billno) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String esignfileName = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select signed_pdf_file from esign_log where bill_no=? "
                    + "and (signed_pdf_path is not null or signed_pdf_path<>'') and signed_pdf_file like '%_signed%' ");
            ps.setInt(1, billno);
            rs = ps.executeQuery();
            if (rs.next()) {
                esignfileName = rs.getString("signed_pdf_file");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return esignfileName;
    }

    @Override
    public String getBillStatus(BillDetail billDetail) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String billStatus = null;

        //int billno = Integer.parseInt(billDetail.getBillNo());
        try {
            if (billDetail.getBillnumber() != null && !billDetail.getBillnumber().equals("")) {
                con = dataSource.getConnection();
                pstmt = con.prepareStatement("SELECT * FROM paybill_task WHERE bill_id=?");
                pstmt.setInt(1, Integer.parseInt(billDetail.getBillnumber()));
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    billStatus = rs.getString("bill_id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billStatus;
    }

    @Override
    public BillDetail getStatusBillInfo(String billnumber) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Statement stmt = null;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        BillDetail billDetail = null;
        ArrayList billDetailsList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            String sql1 = "SELECT g_office.off_name,g_office.ddo_code,aq_month,aq_year,bill_id,priority,bill_type,process_date FROM paybill_task LEFT JOIN g_office ON paybill_task.off_code=g_office.off_code WHERE bill_id= '" + billnumber + "'";
            ps = con.prepareStatement(sql1);
            rs = ps.executeQuery();
            if (rs.next()) {
                billDetail = new BillDetail();

                billDetail.setOfficename(rs.getString("off_name"));
                billDetail.setDdoccode(rs.getString("ddo_code"));
                billDetail.setFromMonth(rs.getInt("aq_month"));
                billDetail.setFromYear(rs.getInt("aq_year"));
                billDetail.setBillid(rs.getString("bill_id"));
                billDetail.setPriority(rs.getInt("priority"));
                billDetail.setBillType(rs.getString("bill_type"));

                billDetail.setBillDate(df.format(rs.getTimestamp("process_date").getTime()));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billDetail;

    }

    @Override
    public int getDDORecoveryList(int billno) {
        AllowDeductDetails allowdeduct = null;
        ArrayList ddorecoveryList = new ArrayList();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        int ddoRecoveryAmt = 0;
        try {
            con = dataSource.getConnection();
            /*pst = con.prepareStatement("SELECT AD_CODE,SUM(AD_AMT) AD_AMT FROM AQ_DTLS INNER JOIN  (SELECT AQ_MAST.AQSL_NO FROM AQ_MAST WHERE  AQ_MAST.BILL_NO=?)AQ_MAST ON AQ_DTLS.AQSL_NO = AQ_MAST.AQSL_NO "
             + "WHERE (SCHEDULE ='PVTL' OR SCHEDULE ='PVTD') AND AD_AMT>0 GROUP BY AQ_DTLS.AD_CODE,BT_ID,NOW_DEDN ORDER BY AQ_DTLS.AD_CODE");*/
            pst = con.prepareStatement("SELECT SUM(DDO_RECOVERY)AD_AMT FROM ARR_MAST WHERE BILL_NO=?  AND DDO_RECOVERY IS NOT NULL");
            pst.setInt(1, billno);
            //pst.setInt(2, aqYear);
            //pst.setInt(3, aqMonth);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getString("AD_AMT") != null && !rs.getString("AD_AMT").equals("")) {
                    allowdeduct = new AllowDeductDetails();
                    allowdeduct.setAdname("DDORECOVERY");
                    allowdeduct.setAdamount(rs.getString("AD_AMT"));
                    ddoRecoveryAmt = Integer.parseInt(allowdeduct.getAdamount());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ddoRecoveryAmt;
    }

    @Override
    public int getNewBillYearDHE(String offCode, String typeOfBill) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        int newYearDHE = 0;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select max(aq_year) maxyear,count(*) cnt from BILL_MAST WHERE "
                    + "P_OFF_CODE=(SELECT P_OFF_CODE FROM G_OFFICE WHERE OFF_CODE=?) and type_of_bill LIKE '%" + typeOfBill + "%' ");
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    newYearDHE = rs.getInt("maxyear");
                } else {
                    newYearDHE = Calendar.getInstance().get(Calendar.YEAR);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return newYearDHE;
    }

    @Override
    public int getNewBillMonthDHE(String offCode, int year, String typeOfBill) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        int newMonthDHE = 0;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT MAX(AQ_MONTH) maxmonth FROM BILL_MAST WHERE "
                    + "P_OFF_CODE=(SELECT P_OFF_CODE FROM G_OFFICE WHERE OFF_CODE=?) AND AQ_YEAR=? and TYPE_OF_BILL=?");
            pstmt.setString(1, offCode);
            pstmt.setInt(2, year);
            pstmt.setString(3, typeOfBill);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getString("maxmonth") == null) {
                    newMonthDHE = -1;
                } else {
                    newMonthDHE = rs.getInt("maxmonth");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return newMonthDHE;
    }

    @Override
    public boolean getbillVerificationStatus(String offcode, int billno, int aqMonth, int aqYear) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        boolean billVerified = false;

        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("SELECT is_bill_verified FROM AQ_MAST WHERE OFF_CODE=? AND AQ_MONTH=? AND AQ_YEAR=? AND BILL_NO=?");
            pst.setString(1, offcode);
            pst.setInt(2, aqMonth);
            pst.setInt(3, aqYear);
            pst.setInt(2, billno);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("is_bill_verified").equals("Y") && rs.getString("is_bill_verified") != null) {
                    billVerified = true;
                } else {
                    billVerified = false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billVerified;
    }

    @Override
    public boolean updatebillVerificationStatus(String offcode, int billno, int aqMonth, int aqYear) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;

        boolean isBillVerified = false;
        int retVal = 0;

        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("UPDATE AQ_MAST set is_bill_verified='Y' WHERE OFF_CODE=? AND AQ_MONTH=? AND AQ_YEAR=? AND BILL_NO=?");
            pst.setString(1, offcode);
            pst.setInt(2, aqMonth);
            pst.setInt(3, aqYear);
            pst.setInt(4, billno);
            retVal = pst.executeUpdate();
            if (retVal > 0) {
                isBillVerified = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isBillVerified;
    }

    @Override
    public int getBillFrontPageSlNo(String billtype) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int slno = 0;

        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("select * from g_schedule where schedule_desc='BILL FRONT PAGE' and bill_type=?");
            pst.setString(1, billtype);
            rs = pst.executeQuery();
            if (rs.next()) {
                slno = rs.getInt("sl_no");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return slno;
    }

    @Override
    public List getSalaryDetails(String empId, String fiscalYear) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        List paymentList = new ArrayList();
        String finYear1 = null;
        String finYear2 = null;
        try {
            if (fiscalYear != null) {
                String str[] = fiscalYear.split("-");
                finYear1 = str[0];
                finYear2 = str[1];
            }
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("select t1.*,t2.* from \n"
                    + " (select emp_code,cur_basic,bill_no,aqsl_no,vch_no,vch_date,aq_month,aq_year,type_of_bill,bill_desc,gross_amt,ded_amt,pvt_ded_amt,netpay from \n"
                    + " (select emp_code,cur_basic,bm.bill_no,aqsl_no,bm.vch_no,bm.vch_date,bm.aq_month,bm.aq_year,bm.type_of_bill,bill_desc,\n"
                    + "  aqmast.gross_amt,aqmast.ded_amt,aqmast.pvt_ded_amt ,(aqmast.gross_amt-aqmast.ded_amt)netpay from \n"
                    + " (Select * from aq_mast where emp_code='" + empId + "' and ((aq_month>=3 and aq_year='" + finYear1 + "') or (aq_month<3 and aq_year='" + finYear2 + "')))aqmast \n"
                    + " inner join bill_mast bm on aqmast.bill_no=bm.bill_no where bill_status_id=7)a1 \n"
                    + " union \n"
                    + " (select emp_id emp_code,cur_basic,bm.bill_no,aqsl_no,bm.vch_no,bm.vch_date,bm.aq_month,bm.aq_year,bm.type_of_bill,bill_desc, \n"
                    + "  aqmast.gross_amt,aqmast.ded_amt,aqmast.pvt_ded_amt,arrear_pay netpay  from \n"
                    + " (Select * from arr_mast where emp_id='" + empId + "' and ((p_month>=3 and p_year='" + finYear1 + "') or (p_month<3 and p_year='" + finYear2 + "')))aqmast \n"
                    + " inner join bill_mast bm on aqmast.bill_no=bm.bill_no where bill_status_id=7)  order by type_of_bill desc, aq_month asc)t1 \n"
                    + " left outer join \n"
                    + " (select increment_reason,not_id,pay,emp_id,extract(month from wef)wef_mon,extract(year from wef)wef_yr from emp_pay_record where not_type in ('PAYFIXATION','PAYREVISION') \n"
                    + " and (increment_reason like '%ACP%' or increment_reason like '%TBA%') order by wef desc)t2 \n"
                    + " on t1.emp_code=t2.emp_id and t1.cur_basic=t2.pay  where netpay>0 order by vch_date ");
            rs = pst.executeQuery();
            while (rs.next()) {
                BillBean bb = new BillBean();
                bb.setAqslno(rs.getString("aqsl_no"));
                bb.setBillmonthNm(CommonFunctions.getMonthAsString(rs.getInt("aq_month")).concat(" / ").concat(rs.getString("aq_year")));
                bb.setBillMonth(rs.getInt("aq_month"));
                bb.setBillYear(rs.getInt("aq_year"));
                bb.setBillno(rs.getString("bill_no"));
                bb.setBillGroupDesc(rs.getString("bill_desc"));
                bb.setVoucherno(rs.getString("vch_no"));
                bb.setVoucherdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("vch_date")));
                bb.setBilltype(rs.getString("type_of_bill"));
                if (rs.getString("cur_basic") != null && !rs.getString("cur_basic").equals("")) {
                    bb.setCurbasic(rs.getInt("cur_basic"));
                }
                if (rs.getString("gross_amt") != null && !rs.getString("gross_amt").equals("")) {
                    bb.setBillGross(rs.getInt("gross_amt"));
                }
                if (rs.getString("ded_amt") != null && !rs.getString("ded_amt").equals("")) {
                    bb.setBillDed(rs.getInt("ded_amt") + rs.getInt("pvt_ded_amt"));
                }

                if (rs.getString("netpay") != null && !rs.getString("netpay").equals("")) {
                    bb.setBillNet(rs.getInt("netpay"));
                }

                /*if (rs.getString("pay") != null && !rs.getString("pay").equals("")) {
                 bb.setIncrBasic(rs.getInt("pay"));
                 bb.setHasIncrement("Y");
                 bb.setIncrementReason(rs.getString("increment_reason"));
                 }*/
                if (rs.getString("pay") != null && !rs.getString("pay").equals("")) {
                    if ((rs.getString("wef_mon").equals(rs.getString("aq_month"))) && (rs.getString("wef_yr").equals(rs.getString("aq_year"))) && (rs.getInt("pay") == rs.getInt("cur_basic"))) {
                        bb.setHasIncrement("Y");
                        bb.setIncrBasic(rs.getInt("pay"));
                        bb.setIncrementReason(rs.getString("increment_reason"));
                        System.out.println("Has Increment:" + bb.getVoucherno());
                    }
                } else {
                    bb.setHasIncrement("N");
                }
                paymentList.add(bb);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return paymentList;
    }

    @Override
    public List getBillYear(String empId) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        List salaryYearList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select aq_year from\n"
                    + "(select distinct aq_year from aq_mast where emp_code=? order by aq_year desc)a \n"
                    + "union\n"
                    + "select distinct p_year aq_year from arr_mast where emp_id=? and p_year > 0 order by aq_year desc");
            pst.setString(1, empId);
            pst.setString(2, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                BillBean bb = new BillBean();
                bb.setBillYear(rs.getInt("aq_year"));
                salaryYearList.add(bb);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return salaryYearList;

    }

    @Override
    public List getprivilegedetails(String offcode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        List salaryYearList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select distinct cur_off_code,spc1,getfullname(emp_id)empname,mobile from  \n"
                    + "  ((select distinct spc spc1  from g_privilege_map where substr(spc,1,13) in  \n"
                    + "  (select OFF_CODE from g_office where IS_DDO='B' AND LVL='20') ) gprivilegemap  \n"
                    + "  inner join emp_mast on gprivilegemap.spc1=emp_mast.cur_spc) \n"
                    + "   where cur_off_code=?");
            pst.setString(1, offcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                AqmastModel aqmast = new AqmastModel();
                if (rs.getString("empname") != null && !rs.getString("empname").equals("")) {
                    aqmast.setAqmastEmpNm(rs.getString("empname"));
                }
                if (rs.getString("mobile") != null && !rs.getString("mobile").equals("")) {
                    aqmast.setAqmastMobile(rs.getString("mobile"));
                }
                System.out.println("priv. details-------->" + aqmast.getAqmastEmpNm() + ":::" + aqmast.getAqmastMobile());
                salaryYearList.add(aqmast);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return salaryYearList;

    }

    @Override
    public ArrayList getArrearBillGroupList(String offCode, String curSpc) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList empBillGrpList = new ArrayList();
        boolean privFound = false;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("select ofcbillgrp.*,npsbillgrp1.* from\n"
                    + "(SELECT BILL_GROUP_ID, DESCRIPTION, PLAN, SECTOR, MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,\n"
                    + "SUB_MINOR_HEAD3,POST_CLASS,DEMAND_NO from\n"
                    + "(SELECT BILL_GROUP_ID, DESCRIPTION, PLAN, SECTOR, MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,\n"
                    + "SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,POST_CLASS,DEMAND_NO from BILL_GROUP_MASTER\n"
                    + "WHERE OFF_CODE=? AND (IS_DELETED IS  NULL OR IS_DELETED='N'))BILL_GROUP_MASTER\n"
                    + "LEFT OUTER JOIN\n"
                    + "(SELECT BILL_GRP_ID FROM BILL_GROUP_PRIVILAGE WHERE SPC=?) BILL_GROUP_PRIVILAGE\n"
                    + "ON BILL_GROUP_PRIVILAGE.BILL_GRP_ID = BILL_GROUP_MASTER.BILL_GROUP_ID::BIGINT\n"
                    + "ORDER BY DESCRIPTION)ofcbillgrp\n"
                    + "left outer join(select npsbillgrp.BILL_GROUP_ID,count(*)noteligible from\n"
                    + "(select bsm.bill_group_id,spc,emp_id,acct_type,gpf_no,if_gpf_assumed,stop_pay_nps,is_regular from\n"
                    + "(SELECT * FROM BILL_SECTION_MAPPING)bsm\n"
                    + "inner join G_SECTION on bsm.section_id=g_section.section_id\n"
                    + "inner join SECTION_POST_MAPPING spm on spm.section_id= g_section.section_id\n"
                    + "inner join (select * from emp_mast where\n"
                    + "--(((is_regular IN ('Y','C') and acct_type='PRAN' and if_gpf_assumed='Y' and\n"
                    + "(((is_regular='Y' and if_gpf_assumed='Y'  AND acct_type not in ('GIA','EPF') and \n"
                    + "((if_retired is null or if_retired ='') and (if_reengaged is null or if_reengaged ='')))))\n"
                    + "OR (is_regular ='Y' AND acct_type not in ('GIA','EPF') AND ((extract(month from dos))-(extract(month from dob))<-1 OR (extract(year from dos)-extract(year from dob))<59 \n"
                    + "OR ((extract(year from dos)-extract(year from dob))=59 and ((extract(month from dos))-(extract(month from dob)))!=11 ) is null))\n"
                    + "OR (is_regular in ('D','B'))\n"
                    + "--OR (is_regular='N' and (if_retired not in ('Y') and if_reengaged not in ('Y')))\n"
                    + "--OR (is_regular='N' and ((if_retired is null or if_retired ='') and (if_reengaged is null or if_reengaged ='')))\n"
                    + "--OR (is_regular='A' and usertype='ADHOC')\n"
                    + ")empmast on (empmast.cur_spc=spm.spc or empmast.emp_id=spm.spc))npsbillgrp\n"
                    + "group by npsbillgrp.BILL_GROUP_ID)npsbillgrp1\n"
                    + "ON ofcbillgrp.bill_group_id=npsbillgrp1.bill_group_id\n"
                    + "ORDER BY DESCRIPTION");

            pstmt.setString(1, offCode);
            pstmt.setString(2, curSpc);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                BillAttr billattr = new BillAttr();
                billattr.setBillgroupId(rs.getString("BILL_GROUP_ID"));
                billattr.setBillDesc(rs.getString("DESCRIPTION"));
                billattr.setChartofAcc(StringUtils.defaultString(rs.getString("DEMAND_NO")) + "-" + StringUtils.defaultString(rs.getString("MAJOR_HEAD")) + "-" + StringUtils.defaultString(rs.getString("SUB_MAJOR_HEAD")) + "-" + StringUtils.defaultString(rs.getString("MINOR_HEAD")) + "-" + StringUtils.defaultString(rs.getString("SUB_MINOR_HEAD1")) + "-" + StringUtils.defaultString(rs.getString("SUB_MINOR_HEAD2")) + "-" + StringUtils.defaultString(rs.getString("PLAN")) + "-" + StringUtils.defaultString(rs.getString("SUB_MINOR_HEAD3")) + "-" + StringUtils.defaultString(rs.getString("SECTOR")));
                //billattr.setNodummypran(rs.getString("dummypran"));
                billattr.setNoteligible(rs.getString("noteligible"));
                privFound = true;
                empBillGrpList.add(billattr);
            }
            if (privFound == false) {
                pstmt = con.prepareStatement("SELECT BILL_GROUP_ID, DESCRIPTION, PLAN, SECTOR, MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,POST_CLASS,DEMAND_NO from BILL_GROUP_MASTER "
                        + "WHERE OFF_CODE=? AND (IS_DELETED IS  NULL OR IS_DELETED='N') ORDER BY DESCRIPTION");
                pstmt.setString(1, offCode);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    BillAttr billattr = new BillAttr();
                    billattr.setBillgroupId(rs.getString("BILL_GROUP_ID"));
                    billattr.setBillDesc(rs.getString("DESCRIPTION"));
                    billattr.setChartofAcc(StringUtils.defaultString(rs.getString("DEMAND_NO")) + "-" + StringUtils.defaultString(rs.getString("MAJOR_HEAD")) + "-" + StringUtils.defaultString(rs.getString("SUB_MAJOR_HEAD")) + "-" + StringUtils.defaultString(rs.getString("MINOR_HEAD")) + "-" + StringUtils.defaultString(rs.getString("SUB_MINOR_HEAD1")) + "-" + StringUtils.defaultString(rs.getString("SUB_MINOR_HEAD2")) + "-" + StringUtils.defaultString(rs.getString("PLAN")) + "-" + StringUtils.defaultString(rs.getString("SUB_MINOR_HEAD3")) + "-" + StringUtils.defaultString(rs.getString("SECTOR")));
                    empBillGrpList.add(billattr);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empBillGrpList;
    }

    @Override
    public ArrayList getArrearPayBillList(int year, int month, String offCode, String billType, String spc) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<BillBean> billList = new ArrayList<>();
        try {
            //if (billType != null && billType.equalsIgnoreCase("PAY")) {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT BILL_GRP_ID FROM BILL_GROUP_PRIVILAGE WHERE SPC=?");
            pstmt.setString(1, spc);
            rs = pstmt.executeQuery();
            boolean separateBillMapped = false;
            if (rs.next()) {
                separateBillMapped = true;
                pstmt = con.prepareStatement(" SELECT BILL_NO, BILL_DESC, BILL_DATE, BILL_MAST.BILL_TYPE,TYPE_OF_BILL, BILL_GROUP_DESC DESCRIPTION, BILL_MAST.BILL_STATUS_ID, ONLINE_BILL_SUBMISSION, PAYBILL_TASK.BILL_ID IS_BILL_PREPARED, BILL_MAST.BILL_GROUP_ID, arrear_percent, (select count(*) from bill_beneficiary where bill_id=BILL_MAST.bill_no) noof_emp  FROM ( "
                        + " SELECT BILL_NO,BILL_DESC,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_GROUP_DESC,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION, IS_BILL_PREPARED, BILL_GROUP_ID, arrear_percent  FROM ( "
                        + " SELECT BILL_NO,BILL_DESC,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_GROUP_DESC,BILL_STATUS_ID,OFF_CODE, IS_BILL_PREPARED, BILL_GROUP_ID ,arrear_percent FROM ( "
                        + " SELECT BILL_GRP_ID FROM BILL_GROUP_PRIVILAGE WHERE SPC=? ) BILL_GROUP_PRIVILAGE "
                        + " LEFT OUTER JOIN ( "
                        + " SELECT BILL_NO,BILL_DESC,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_GROUP_DESC,BILL_STATUS_ID,OFF_CODE, IS_BILL_PREPARED, BILL_GROUP_ID, arrear_percent FROM BILL_MAST WHERE OFF_CODE=? AND AQ_MONTH=? AND AQ_YEAR=? AND TYPE_OF_BILL=? ORDER BY BILL_GROUP_DESC)BILL_MAST "
                        + " ON BILL_GROUP_PRIVILAGE.BILL_GRP_ID = BILL_MAST.BILL_GROUP_ID "
                        + " ) BILL_MAST "
                        + " LEFT OUTER JOIN G_OFFICE ON BILL_MAST.OFF_CODE=G_OFFICE.OFF_CODE ORDER BY BILL_DESC) BILL_MAST "
                        + " LEFT OUTER JOIN PAYBILL_TASK ON BILL_MAST.BILL_NO=PAYBILL_TASK.BILL_ID order by BILL_NO");
                pstmt.setString(1, spc);
                pstmt.setString(2, offCode);
                pstmt.setInt(3, month);
                pstmt.setInt(4, year);
                pstmt.setString(5, billType);

            } else {
                pstmt = con.prepareStatement("select t1.*,noteligible from\n"
                        + "(SELECT BILL_NO,BILL_DATE,BILL_MAST.BILL_TYPE,BILL_MAST.TYPE_OF_BILL,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION , \n"
                        + "PAYBILL_TASK.BILL_ID IS_BILL_PREPARED,arrear_percent, BILL_MAST.BILL_GROUP_ID,\n"
                        + "DESCRIPTION, BILL_DESC,IS_DELETED, (select count(*) from bill_beneficiary where bill_id=BILL_MAST.bill_no) noof_emp   FROM (\n"
                        + "SELECT BILL_NO,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION , IS_BILL_PREPARED,arrear_percent, BILL_GROUP_ID,DESCRIPTION, BILL_DESC,IS_DELETED FROM (\n"
                        + "SELECT BILL_NO,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION , IS_BILL_PREPARED,arrear_percent, BILL_GROUP_ID,DESCRIPTION, BILL_DESC,IS_DELETED FROM (\n"
                        + "SELECT BILL_NO,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION , IS_BILL_PREPARED,arrear_percent, BILL_GROUP_MASTER.BILL_GROUP_ID,DESCRIPTION, BILL_DESC,IS_DELETED FROM (\n"
                        + "SELECT BILL_GROUP_ID, DESCRIPTION,IS_DELETED FROM BILL_GROUP_MASTER WHERE OFF_CODE=?) BILL_GROUP_MASTER\n"
                        + "LEFT OUTER JOIN \n"
                        + "(SELECT BILL_NO,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_STATUS_ID,ONLINE_BILL_SUBMISSION , IS_BILL_PREPARED, BILL_GROUP_ID, BILL_DESC, arrear_percent FROM (\n"
                        + "SELECT BILL_NO,BILL_DATE,BILL_TYPE,TYPE_OF_BILL,BILL_STATUS_ID,IS_BILL_PREPARED, BILL_GROUP_ID,OFF_CODE, BILL_DESC, arrear_percent FROM BILL_MAST WHERE OFF_CODE=?\n"
                        + "AND AQ_MONTH=? AND AQ_YEAR=? AND TYPE_OF_BILL=?) BILL_MAST\n"
                        + "INNER JOIN G_OFFICE ON BILL_MAST.OFF_CODE=G_OFFICE.OFF_CODE) BILL_MAST\n"
                        + "ON BILL_GROUP_MASTER.BILL_GROUP_ID=BILL_MAST.BILL_GROUP_ID  WHERE\n"
                        + "(IS_DELETED='N' OR IS_DELETED IS NULL) OR (IS_DELETED='Y' AND IS_BILL_PREPARED ='Y') ORDER BY DESCRIPTION) BILL_MAST ) BILL_MAST ) BILL_MAST\n"
                        + "LEFT OUTER JOIN PAYBILL_TASK ON BILL_MAST.BILL_NO=PAYBILL_TASK.BILL_ID  order by BILL_NO)t1\n"
                        + "left outer join \n"
                        + "(select npsbillgrp.BILL_GROUP_ID,count(*)noteligible from\n"
                        + "(select bsm.bill_group_id,spc,emp_id,acct_type,gpf_no,if_gpf_assumed,stop_pay_nps,is_regular from\n"
                        + "(SELECT * FROM BILL_SECTION_MAPPING)bsm\n"
                        + "inner join G_SECTION on bsm.section_id=g_section.section_id\n"
                        + "inner join SECTION_POST_MAPPING spm on spm.section_id= g_section.section_id\n"
                        + "inner join (select * from emp_mast where\n"
                        + "--(((is_regular IN ('Y','C') and acct_type='PRAN' and if_gpf_assumed='Y' and\n"
                        + "(((is_regular='Y' and if_gpf_assumed='Y' AND acct_type not in ('GIA','EPF') and \n"
                        + "((if_retired is null or if_retired ='') and (if_reengaged is null or if_reengaged ='')))))\n"
                        + "OR (is_regular ='Y' AND acct_type not in ('GIA','EPF') AND ((extract(month from dos))-(extract(month from dob))<-1 OR (extract(year from dos)-extract(year from dob))<59 \n"
                        + "OR ((extract(year from dos)-extract(year from dob))=59 and ((extract(month from dos))-(extract(month from dob)))!=11 ) is null))\n"
                        + "OR (is_regular in ('D','B'))\n"
                        + "--OR (is_regular='N' and (if_retired not in ('Y') and if_reengaged not in ('Y')))\n"
                        + "--OR (is_regular='N' and ((if_retired is null or if_retired ='') and (if_reengaged is null or if_reengaged ='')))\n"
                        + "--OR (is_regular='A' and usertype='ADHOC')\n"
                        + ")empmast on (empmast.cur_spc=spm.spc or empmast.emp_id=spm.spc))npsbillgrp\n"
                        + "group by npsbillgrp.BILL_GROUP_ID)npsbillgrp1\n"
                        + "ON t1.bill_group_id=npsbillgrp1.bill_group_id\n"
                        + "ORDER BY DESCRIPTION");

                pstmt.setString(1, offCode);
                pstmt.setString(2, offCode);
                pstmt.setInt(3, month);
                pstmt.setInt(4, year);
                pstmt.setString(5, billType);
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                BillBean bb = new BillBean();
                bb.setBillgroupId(rs.getString("BILL_GROUP_ID"));
                bb.setBillno(rs.getString("BILL_NO"));
                bb.setBilldesc(rs.getString("BILL_DESC"));
                bb.setBilldate(CommonFunctions.getFormattedOutputDate1(rs.getDate("BILL_DATE")));
                bb.setBilltype(rs.getString("TYPE_OF_BILL"));
                bb.setBillGroupDesc(rs.getString("DESCRIPTION"));
                if (rs.getInt("BILL_STATUS_ID") == 0) {
                    bb.setShowLink("");
                } else if (rs.getInt("BILL_STATUS_ID") == 2 || rs.getInt("BILL_STATUS_ID") == 4 || rs.getInt("BILL_STATUS_ID") == 8) {
                    bb.setShowLink("Y");
                } else if (rs.getInt("BILL_STATUS_ID") > 2 && rs.getInt("BILL_STATUS_ID") != 4 && rs.getInt("BILL_STATUS_ID") != 8) {
                    bb.setShowLink("N");
                } else {
                    bb.setShowLink("");
                }
                bb.setLockBill(rs.getInt("BILL_STATUS_ID"));
                bb.setOnlinebillapproved(rs.getString("ONLINE_BILL_SUBMISSION"));
                bb.setNooofEmployee(rs.getInt("noof_emp"));
                if (rs.getString("IS_BILL_PREPARED") != null && !rs.getString("IS_BILL_PREPARED").equals("")) {
                    bb.setIsbillPrepared("N");
                } else {
                    bb.setIsbillPrepared("Y");
                }
                bb.setPercentageArraer(rs.getInt("arrear_percent") + "");

                if (bb.getBillno() != null && !bb.getBillno().equals("")) {
                    bb.setFtcount(getFailedTransactionCount(Integer.parseInt(bb.getBillno())));
                } else {
                    bb.setFtcount(0);
                }
                if (bb.getBillno() != null && !bb.getBillno().equals("")) {
                    bb.setCheckESignStatus(checkArrearEsign(offCode, bb.getBillno()));
                }
                bb.setFrontpageslno(getBillFrontPageSlNo(bb.getBilltype()) + "");
                //bb.setNodummypran(rs.getString("dummypran"));
                bb.setNoteligible(rs.getString("noteligible"));
                bb.setSelectedBilltype(billType);
                billList.add(bb);
            }
            //}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billList;
    }

    @Override
    public List getempListNotForArrear(String billGrpId) {
        List empListNotForArrear = new ArrayList();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select spc,emp_id,emp_type,dob,dos,(extract(year from dos))-(extract(year from dob))syear,\n"
                    + "(extract(month from dos))-(extract(month from dob))smonth,\n"
                    + " getfullname(emp_id)empname,\n"
                    + " acct_type,gpf_no,if_gpf_assumed,\n"
                    + " stop_pay_nps,empmast.is_regular,if_retired,if_reengaged from\n"
                    + " (SELECT * FROM BILL_SECTION_MAPPING where bill_group_id=?)bsm\n"
                    + " inner join G_SECTION on bsm.section_id=g_section.section_id\n"
                    + " inner join SECTION_POST_MAPPING spm on spm.section_id= g_section.section_id\n"
                    + " inner join (select * from emp_mast where\n"
                    + "--(((is_regular IN ('Y','C') and acct_type='PRAN' and if_gpf_assumed='Y' and\\n\"\n"
                    + "(((is_regular='Y' and if_gpf_assumed='Y' AND acct_type not in ('GIA','EPF') and\n"
                    + "((if_retired is null or if_retired ='') and (if_reengaged is null or if_reengaged ='')))))\n"
                    + "OR (is_regular ='Y' AND acct_type not in ('GIA','EPF') AND ((extract(month from dos))-(extract(month from dob))<-1 OR (extract(year from dos)-extract(year from dob))<59\n"
                    + "OR ((extract(year from dos)-extract(year from dob))=59 and ((extract(month from dos))-(extract(month from dob)))!=11 ) is null))\n"
                    + "OR (is_regular in ('D','B'))\n"
                    + "--OR (is_regular='N' and (if_retired not in ('Y') and if_reengaged not in ('Y')))\\n\"\n"
                    + "--OR (is_regular='N' and ((if_retired is null or if_retired ='') and (if_reengaged is null or if_reengaged ='')))\\n\"\n"
                    + "--OR (is_regular='A' and usertype='ADHOC')\\n\"\n"
                    + ")empmast on  (empmast.cur_spc=spm.spc or empmast.emp_id=spm.spc)\n"
                    + "left outer join g_emp_type on empmast.is_regular=g_emp_type.is_regular\n"
                    + " ");
            pst.setBigDecimal(1, new BigDecimal(billGrpId));
            rs = pst.executeQuery();
            while (rs.next()) {
                Employee empnotForArr = new Employee();
                //System.out.println("empname:" + rs.getString("empname"));
                empnotForArr.setEmpName(rs.getString("empname"));
                empnotForArr.setEmpid(rs.getString("emp_id"));
                empnotForArr.setGpfno(rs.getString("gpf_no"));
                empnotForArr.setAccttype(rs.getString("acct_type"));
                if (rs.getString("emp_type") != null && !rs.getString("emp_type").equals("")) {
                    empnotForArr.setEmpType(rs.getString("emp_type"));
                } else {
                    empnotForArr.setEmpType("Not Available");
                }
                if (empnotForArr.getAccttype() != null && empnotForArr.getAccttype().equals("PRAN")) {
                    if (rs.getString("if_gpf_assumed").equals("Y")) {
                        empnotForArr.setEmpNonPran("DUMMY PRAN");

                    } else if (rs.getString("if_gpf_assumed").equals("N")) {
                        empnotForArr.setEmpNonPran("VALID PRAN");
                    }
                } else if (!empnotForArr.getAccttype().equals("PRAN")) {
                    empnotForArr.setEmpNonPran(" ");
                }
                /* if (rs.getString("stop_pay_nps") != null && rs.getString("stop_pay_nps").equals("N")) {
                 empnotForArr.setStopPayNPS("STOP PRAN DEDUCTION");
                 } else if (rs.getString("stop_pay_nps").equals("Y")) {
                 empnotForArr.setStopPayNPS("CONTINUE PRAN DEDUCTION");
                 }*/
                if (rs.getString("dob") != null && !rs.getString("dob").equals("")) {
                    empnotForArr.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                } else {
                    empnotForArr.setDob("DATE OF BIRTH<BR/>IS BLANK");
                }
                if (rs.getString("dos") != null && !rs.getString("dos").equals("")) {
                    empnotForArr.setDor(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                } else {
                    empnotForArr.setDor("DATE OF RETIREMENT<BR/>IS BLANK");
                }
                /*if (rs.getInt(rs.getInt("syear")) < 59) {
                 empnotForArr.setServiceGap("SERVICE PERIOD IS LESS BETWEEN DOS AND DOB");
                 }*/

                empListNotForArrear.add(empnotForArr);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empListNotForArrear;

    }

    @Override
    public String checkNewBillGenerationStatus(String billGrpId, int aqMonth, int aqYear) {
        String billgeneratedId = null;
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Calendar cal = Calendar.getInstance();
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select bill_status_id,bill_no from bill_mast where \n"
                    + "bill_group_id=? and aq_month=? and aq_year=? \n"
                    + "and (bill_status_id is null or bill_status_id not in (2,3,5,7))");
            ps.setBigDecimal(1, BigDecimal.valueOf(Double.parseDouble(billGrpId)));
            ps.setInt(2, aqMonth);
            ps.setInt(3, aqYear);
            rs = ps.executeQuery();
            if (rs.next()) {
                billgeneratedId = rs.getString("bill_no");
            } else {
                billgeneratedId = "N";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billgeneratedId;
    }

    @Override
    public void deleteOfficeDHE(String billNo, String offcode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("Delete from aq_mast where bill_no=? and off_code=?");
            pstmt.setInt(1, Integer.parseInt(billNo));
            pstmt.setString(2, offcode);
            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getMismatchGPFData(int billno, String typeOfBill) {

        Connection con = null;

        PreparedStatement emplistpst = null;
        ResultSet rs = null;

        PreparedStatement regularpst = null;
        PreparedStatement arrearpst = null;

        int aqmonth = 0;
        int aqyear = 0;

        int finyear1 = 0;
        int finyear2 = 0;

        int empTotalGPFAmt = 0;

        List<AqmastModel> emplist = new ArrayList();

        List mismatchGPFMEmpList = new ArrayList();

        AqmastModel aqempdata = new AqmastModel();

        List<AqmastModel> newlist = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select aq_month, aq_year, emp_id,benf_name,gpf_number from bill_beneficiary"
                    + " inner join bill_mast on bill_beneficiary.bill_id=bill_mast.bill_no"
                    + " where bill_id = ?";
            emplistpst = con.prepareStatement(sql);
            emplistpst.setInt(1, billno);
            rs = emplistpst.executeQuery();
            while (rs.next()) {
                aqempdata = new AqmastModel();
                aqempdata.setEmpCode(rs.getString("emp_id"));
                aqempdata.setEmpName(rs.getString("benf_name"));
                aqempdata.setGpfAccNo(rs.getString("gpf_number"));
                aqyear = rs.getInt("aq_year");
                aqmonth = rs.getInt("aq_month");

                emplist.add(aqempdata);
            }

            String regularsql = "SELECT AD_AMT FROM AQ_DTLS"
                    + " INNER JOIN AQ_MAST ON AQ_DTLS.AQSL_NO=AQ_MAST.AQSL_NO"
                    + " INNER JOIN BILL_MAST ON AQ_MAST.BILL_NO=BILL_MAST.BILL_NO"
                    + " WHERE AQ_DTLS.EMP_CODE=? AND AQ_DTLS.AQ_YEAR=? AND AQ_DTLS.AQ_MONTH=? AND AD_CODE='GPF' AND AD_AMT>0";
            regularpst = con.prepareStatement(regularsql);

            /*sql = "SELECT AQ_MONTH,AQ_YEAR FROM BILL_MAST WHERE BILL_NO=?";
             pst = con.prepareStatement(sql);
             pst.setInt(1, Integer.parseInt(billno));
             rs = pst.executeQuery();
             if (rs.next()) {
             aqmonth = rs.getInt("AQ_MONTH");
             aqyear = rs.getInt("AQ_YEAR");
             }*/
            //DataBaseFunctions.closeSqlObjects(rs, regularpst);
            if (typeOfBill != null && typeOfBill.contains("ARREAR")) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);

                int gpfamt = 0;

                String arrearsql = "SELECT cpf_head FROM arr_mast"
                        + " inner join bill_mast on arr_mast.bill_no=bill_mast.bill_no"
                        + " WHERE EMP_ID=? AND P_YEAR=? AND P_MONTH=? AND cpf_head>0";
                arrearpst = con.prepareStatement(arrearsql);

                if (emplist != null && emplist.size() > 0) {
                    aqempdata = null;
                    for (int i = 0; i < emplist.size(); i++) {
                        aqempdata = (AqmastModel) emplist.get(i);
                        gpfamt = 0;
                        if (aqmonth > 2) {
                            finyear1 = aqyear;
                            finyear2 = finyear1 + 1;
                            //System.out.println("finyear1 is: "+finyear1+" and finyear2 is: "+finyear2);
                            for (int j = 3; j <= 12; j++) {
                                regularpst.setString(1, aqempdata.getEmpCode());
                                regularpst.setInt(2, finyear1);
                                regularpst.setInt(3, j);
                                rs = regularpst.executeQuery();
                                if (rs.next()) {
                                    gpfamt = gpfamt + rs.getInt("AD_AMT");
                                    if (aqempdata.getEmpCode() != null && aqempdata.getEmpCode().equals("63001318")) {
                                        //System.out.println("amount is: "+rs.getInt("AD_AMT"));
                                    }
                                }
                            }

                            for (int j = 0; j <= 2; j++) {
                                regularpst.setString(1, aqempdata.getEmpCode());
                                regularpst.setInt(2, finyear2);
                                regularpst.setInt(3, j);
                                rs = regularpst.executeQuery();
                                if (rs.next()) {
                                    gpfamt = gpfamt + rs.getInt("AD_AMT");
                                    if (aqempdata.getEmpCode() != null && aqempdata.getEmpCode().equals("63001318")) {
                                        //System.out.println("amount is: "+rs.getInt("AD_AMT"));
                                    }
                                }
                            }

                        } else if (aqmonth <= 2) {
                            finyear1 = aqyear;
                            finyear2 = finyear1 + 1;
                            for (int j = 0; j <= 2; j++) {
                                regularpst.setString(1, aqempdata.getEmpCode());
                                regularpst.setInt(2, finyear2);
                                regularpst.setInt(3, j);
                                rs = regularpst.executeQuery();
                                if (rs.next()) {
                                    gpfamt = gpfamt + rs.getInt("AD_AMT");
                                    if (aqempdata.getEmpCode() != null && aqempdata.getEmpCode().equals("63001318")) {
                                        //System.out.println("amount is: "+rs.getInt("AD_AMT"));
                                    }
                                }
                            }

                            for (int j = 3; j <= 12; j++) {
                                regularpst.setString(1, aqempdata.getEmpCode());
                                regularpst.setInt(2, finyear1);
                                regularpst.setInt(3, j);
                                rs = regularpst.executeQuery();
                                if (rs.next()) {
                                    gpfamt = gpfamt + rs.getInt("AD_AMT");
                                    if (aqempdata.getEmpCode() != null && aqempdata.getEmpCode().equals("63001318")) {
                                        //System.out.println("amount is: "+rs.getInt("AD_AMT"));
                                    }
                                }
                            }
                        }

                        if (month > 2) {
                            finyear1 = year;
                            finyear2 = finyear1 + 1;
                            //System.out.println("Arrear finyear1 is: "+finyear1+" and finyear2 is: "+finyear2);
                            for (int j = 3; j <= 12; j++) {
                                arrearpst.setString(1, aqempdata.getEmpCode());
                                arrearpst.setInt(2, finyear1);
                                arrearpst.setInt(3, j);
                                rs = arrearpst.executeQuery();
                                if (rs.next()) {
                                    gpfamt = gpfamt + rs.getInt("cpf_head");
                                    if (aqempdata.getEmpCode() != null && aqempdata.getEmpCode().equals("63001318")) {
                                        //System.out.println("amount is: "+rs.getInt("AD_AMT"));
                                    }
                                }
                            }

                            for (int j = 0; j <= 2; j++) {
                                arrearpst.setString(1, aqempdata.getEmpCode());
                                arrearpst.setInt(2, finyear2);
                                arrearpst.setInt(3, j);
                                rs = arrearpst.executeQuery();
                                if (rs.next()) {
                                    gpfamt = gpfamt + rs.getInt("cpf_head");
                                    if (aqempdata.getEmpCode() != null && aqempdata.getEmpCode().equals("63001318")) {
                                        //System.out.println("amount is: "+rs.getInt("AD_AMT"));
                                    }
                                }
                            }

                        } else if (month <= 2) {
                            finyear1 = year - 1;
                            finyear2 = year;
                            for (int j = 0; j <= 2; j++) {
                                arrearpst.setString(1, aqempdata.getEmpCode());
                                arrearpst.setInt(2, finyear2);
                                arrearpst.setInt(3, j);
                                rs = arrearpst.executeQuery();
                                if (rs.next()) {
                                    gpfamt = gpfamt + rs.getInt("cpf_head");
                                    if (aqempdata.getEmpCode() != null && aqempdata.getEmpCode().equals("63001318")) {
                                        //System.out.println("amount is: "+rs.getInt("AD_AMT"));
                                    }
                                }
                            }

                            for (int j = 3; j <= 12; j++) {
                                arrearpst.setString(1, aqempdata.getEmpCode());
                                arrearpst.setInt(2, finyear1);
                                arrearpst.setInt(3, j);
                                rs = arrearpst.executeQuery();
                                if (rs.next()) {
                                    gpfamt = gpfamt + rs.getInt("cpf_head");
                                    if (aqempdata.getEmpCode() != null && aqempdata.getEmpCode().equals("63001318")) {
                                        //System.out.println("amount is: "+rs.getInt("AD_AMT"));
                                    }
                                }
                            }
                        }
                        if (aqempdata.getEmpCode() != null && aqempdata.getEmpCode().equals("63001318")) {
                            //System.out.println("EMP ID is: "+aqempdata.getEmpCode()+" and gpfamt is: "+gpfamt);
                        }
                        if (gpfamt > 500000) {
                            AqmastModel errorempdata = new AqmastModel();
                            errorempdata.setEmpCode(aqempdata.getEmpCode());
                            errorempdata.setGpfAccNo(aqempdata.getGpfAccNo());
                            errorempdata.setEmpName(aqempdata.getEmpName());
                            if (errorempdata.getEmpCode() != null && !isEmployeeExist(errorempdata.getEmpCode(), mismatchGPFMEmpList)) {
                                mismatchGPFMEmpList.add(errorempdata);
                            }
                        }
                    }
                }
            } else {

                Calendar cal = Calendar.getInstance();
                aqyear = cal.get(Calendar.YEAR);
                aqmonth = cal.get(Calendar.MONTH);

                if (emplist != null && emplist.size() > 0) {
                    aqempdata = null;
                    int gpfamt = 0;
                    for (int i = 0; i < emplist.size(); i++) {
                        aqempdata = (AqmastModel) emplist.get(i);
                        gpfamt = 0;
                        if (aqmonth > 2) {
                            finyear1 = aqyear;
                            finyear2 = finyear1 + 1;

                            for (int j = 3; j <= 12; j++) {
                                regularpst.setString(1, aqempdata.getEmpCode());
                                regularpst.setInt(2, finyear1);
                                regularpst.setInt(3, j);
                                rs = regularpst.executeQuery();
                                if (rs.next()) {
                                    gpfamt = gpfamt + rs.getInt("AD_AMT");
                                }
                            }

                            for (int j = 0; j <= 2; j++) {
                                regularpst.setString(1, aqempdata.getEmpCode());
                                regularpst.setInt(2, finyear2);
                                regularpst.setInt(3, j);
                                rs = regularpst.executeQuery();
                                if (rs.next()) {
                                    gpfamt = gpfamt + rs.getInt("AD_AMT");
                                }
                            }

                        } else if (aqmonth <= 2) {
                            finyear1 = aqyear;
                            finyear2 = finyear1 - 1;
                            for (int j = 0; j <= 2; j++) {
                                regularpst.setString(1, aqempdata.getEmpCode());
                                regularpst.setInt(2, finyear1);
                                regularpst.setInt(3, j);
                                rs = regularpst.executeQuery();
                                if (rs.next()) {
                                    gpfamt = gpfamt + rs.getInt("AD_AMT");
                                }
                            }

                            for (int j = 3; j <= 12; j++) {
                                regularpst.setString(1, aqempdata.getEmpCode());
                                regularpst.setInt(2, finyear2);
                                regularpst.setInt(3, j);
                                rs = regularpst.executeQuery();
                                if (rs.next()) {
                                    gpfamt = gpfamt + rs.getInt("AD_AMT");
                                }
                            }
                        }

                        if (gpfamt > 500000) {
                            AqmastModel errorempdata = new AqmastModel();
                            errorempdata.setEmpCode(aqempdata.getEmpCode());
                            errorempdata.setGpfAccNo(aqempdata.getGpfAccNo());
                            errorempdata.setEmpName(aqempdata.getEmpName());
                            mismatchGPFMEmpList.add(errorempdata);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, emplistpst);
            DataBaseFunctions.closeSqlObjects(arrearpst, regularpst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return mismatchGPFMEmpList;
    }

    private ArrayList<AqmastModel> removeDuplicates(List datalist) {

        ArrayList newList = new ArrayList();

        for (Object element : datalist) {

            if (!newList.contains(element)) {
                newList.add(element.toString());
            }
        }
        return newList;
    }

    private boolean isEmployeeExist(String empid, List empList) {
        boolean duplicate = false;
        AqmastModel aqmodel = null;
        for (int i = 0; i < empList.size(); i++) {
            aqmodel = (AqmastModel) empList.get(i);
            if (aqmodel.getEmpCode() != null && aqmodel.getEmpCode().equals(empid)) {
                duplicate = true;
            }
        }
        return duplicate;
    }
}
