package hrms.dao.payroll.arrear;

import hrms.common.CalendarCommonMethods;
import hrms.common.DataBaseFunctions;
import hrms.common.Numtowordconvertion;
import hrms.common.PayrollCommonFunction;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.CommonScheduleMethods;
import hrms.model.payroll.schedule.GPFScheduleBean;
import hrms.model.payroll.schedule.ScheduleHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

public class GPFArrearDAOImpl implements GPFArrearDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public GPFScheduleBean getGPFScheduleHeaderDetails(String billno) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        GPFScheduleBean gpfBean = new GPFScheduleBean();
        int month = 0;
        String year = "";
        
        try {
            con = dataSource.getConnection();

            PayrollCommonFunction prcf = new PayrollCommonFunction();
            CommonReportParamBean bean = prcf.getCommonReportParameter(con, billno);
            
            gpfBean.setOfficeName(bean.getOfficeen());
            gpfBean.setDdoDesg(bean.getDdoname());
            gpfBean.setBillDesc(bean.getBilldesc());
            gpfBean.setBillNo(billno);

            String sql = "SELECT AQ_MONTH, AQ_YEAR FROM BILL_MAST WHERE BILL_NO = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billno));
            rs = pst.executeQuery();
            if (rs.next()) {
                month = rs.getInt("AQ_MONTH");
                gpfBean.setBillMonth(CalendarCommonMethods.getFullMonthAsString(month));
                gpfBean.setBillYear(rs.getString("AQ_YEAR"));
            }
            gpfBean.setPageHeaderTable(reportPageHeader(billno) + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gpfBean;
    }

    @Override
    public List getGPFScheduleTypeList(String billno, int aqmonth, int aqyear) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList gpfTypeList = new ArrayList();
        ScheduleHelper scHelperBean = null;
        int pageno = 0;
        ArrayList al = null;
        try {
            con = dataSource.getConnection();

            int releaseTot = 0;
            String releaseTotFig = null;
            String gpfQry = "SELECT GPF_TYPE FROM AQ_MAST WHERE BILL_NO=? AND GPF_TYPE IS NOT NULL  AND aq_month=? AND aq_year=? GROUP BY GPF_TYPE ORDER BY GPF_TYPE";
            pst = con.prepareStatement(gpfQry);
            pst.setInt(1, Integer.parseInt(billno));
            pst.setInt(2, aqmonth);
            pst.setInt(3, aqyear);
            rs = pst.executeQuery();

            while (rs.next()) {
                pageno += 1;
                releaseTot = 0;
                scHelperBean = new ScheduleHelper();
                String gpfType = rs.getString("GPF_TYPE");
                scHelperBean.setGpfType(gpfType);
                //scHelperBean.setPageheaderparent(reportPageHeader(con, "GPF", gpfType, billno, null) + "");
                //al = getEmpGpfDetails(gpfType, billno, con, scHelperBean);
                scHelperBean.setHelperList(al);

                GPFScheduleBean obj1 = null;
                if (al != null && al.size() > 0) {
                    obj1 = new GPFScheduleBean();
                    for (int i = 0; i < al.size(); i++) {
                        obj1 = (GPFScheduleBean) al.get(i);
                        releaseTot = releaseTot + obj1.getTotalReleased();
                        releaseTotFig = Numtowordconvertion.convertNumber((int) releaseTot);
                    }
                }
                scHelperBean.setReleaseTot(releaseTot);
                scHelperBean.setReleaseTotFig(releaseTotFig);
                scHelperBean.setEmpNo(scHelperBean.getHelperList().size());
                scHelperBean.setPagebreakparent("<input type=\"button\" name=\"pagebreak1\" style=\"page-break-before: always;width: 0;height: 0\"/>");
                gpfTypeList.add(scHelperBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public List getGPFScheduleAbstractList(String billno, int aqmonth, int aqyear) {

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        ArrayList gpfAbstractList = new ArrayList();
        GPFScheduleBean gpfBean = null;
        String aqDtlsTbl = "";

        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();

            String gpfAbstQry1 = "SELECT gpf_type, sum(ad_amt) amt FROM AQ_MAST AM INNER JOIN " + aqDtlsTbl + " AD on AM.aqsl_no = AD.aqsl_no WHERE BILL_NO='" + billno + "' "
                    + " AND AM.aq_month=" + aqmonth + " AND AM.aq_year=" + aqyear + " AND ((AD_CODE='GPF' and DED_TYPE='S') or (AD_CODE='GA' AND DED_TYPE='L') or (AD_CODE='GPDD' AND DED_TYPE='S') or "
                    + " (AD_CODE='GPIR' AND DED_TYPE='S')) group by gpf_type order by gpf_type";

            rs = stmt.executeQuery(gpfAbstQry1);
            while (rs.next()) {
                gpfBean = new GPFScheduleBean();

                gpfBean.setGpfType(rs.getString("GPF_TYPE"));
                gpfBean.setTotalAmount(rs.getString("amt"));

                gpfAbstractList.add(gpfBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gpfAbstractList;
    }

    private StringBuffer reportPageHeader(String billNo) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String billmonth = "";
        String billyear = "";

        String billDesc = "";

        StringBuffer header = null;
        try {
            con = this.dataSource.getConnection();

            PayrollCommonFunction prcf = new PayrollCommonFunction();
            CommonReportParamBean bean = prcf.getCommonReportParameter(con, StringUtils.defaultString(billNo));

            String sql = "SELECT bill_desc FROM BILL_MAST WHERE BILL_NO = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billNo));
            rs = pst.executeQuery();
            if (rs.next()) {
                billDesc = rs.getString("bill_desc");
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            sql = "SELECT AQ_MONTH, AQ_YEAR FROM BILL_MAST WHERE BILL_NO = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billNo));
            rs = pst.executeQuery();
            if (rs.next()) {
                int month = rs.getInt("AQ_MONTH");
                billmonth = CalendarCommonMethods.getFullMonthAsString(month);
                billyear = rs.getString("AQ_YEAR");
            }

            header = new StringBuffer("<div style=\"width:90%;margin: 0 auto;\">"
                    + "<table width=\"100%\" border=\"0\">"
                    + "<tr>"
                    + "<td width=\"100%\" style=\"text-align:center\">"
                    + "<b>GENERAL PROVIDENT FUND</b>"
                    + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"100%\" style=\"text-align:center\">"
                    + "<b>BILL NO:</b>" + StringUtils.defaultString(billDesc) + ""
                    + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"100%\" style=\"text-align:center\">"
                    + ""
                    + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"100%\" style=\"text-align:center\">"
                    + "Demand No-\"8009/_____________________- State/Centre G.P.F Withdrawals\" (Strike out which is not applicable)"
                    + "</td>"
                    + "</tr>"
                    + "</table>"
                    + "</div>");
            header.append("<div style=\"width:99%;margin: 0 auto;\">"
                    + "<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:13px;\">"
                    + "<thead></thead>"
                    + "<tr>"
                    + "<td style=\"text-align:left;\">"
                    + "1. Arrange the A/C Nos in serial order. Accounts Nos may be written very clearly."
                    + "Separate Schedules should be prepared  for each group."
                    + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td style=\"text-align:left;\">2. The names of the subscribers should be written in full.</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td style=\"text-align:left;\">3. If interest is paid on advance, mention it in remarks column.</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td style=\"text-align:left;\">4. Figures in columns 3,4,5 and 7 should be rounded to whole rupees.</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td style=\"text-align:left;\">5. Use similar form, if names are few. But do not write subscribers name and account numbers very close to each other.</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td style=\"text-align:left;\">6. The total of schedules also should be written both in figures and words.</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td style=\"text-align:left;\">7. This form should not be used for transactions of General Provident Fund for which form No. O.T.C. 76 has been provided.</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td style=\"text-align:left;\">8. In Col. 1 quote account number unfailingly. The guide letters e.g. I.C.S. (ICS Provident Fund) etc. should be invariably  prefixed to Account Nos.</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td style=\"text-align:left;\">9. In the remarks column write description against every new name such as 'New Subscriber' came on transfer from Office District resumed subscription.</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td style=\"text-align:left;\">10. Separate schedule should be prepared in respect of persons whose account are kept by different Accountant General.</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>&nbsp;</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td style=\"text-align:center;\"><b>" + bean.getOfficeen() + "</b>"
                    + "</br>DEDUCTION MADE FROM THE SALARY FOR <b>" + billmonth + "</b>&ensp;<b>" + billyear + "</b>"
                    + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>&nbsp;</td>"
                    + "</tr>"
                    + "</table>"
                    + "</div>");
            //header.append(reportTableHeader("GPF"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return header;
    }

    private StringBuffer reportTableHeader(String schedule) {

        StringBuffer tblHeader = null;

        try {
            if (schedule.equals("GPF")) {
                tblHeader = new StringBuffer("<table border=\"0\" width=\"100%\" cellspacing=\"0\" style=\"font-size:11px; font-family:verdana;\">"
                        + "<tr class=\"tblHeader\" height=\"45px\">"
                        + "<td width=\"3%\" class=\"tblHeader\">Sl No</td>"
                        + "<td width=\"15%\" align=\"center\" class=\"tblHeader\">ACCOUNT NO./<br/>DATE OF ENTRY <br/>INTO GOVT. SERVICE</td>"
                        + "<td width=\"25%\" class=\"tblHeader\">NAME OF THE SUBSCRIBER/<br/>DESIGNATION</td>"
                        + "<td width=\"12%\" class=\"tblHeader\">BASIC PAY/ GRADE <br/>PAY / SCALE OF PAY</td>"
                        + "<td width=\"12%\" class=\"tblHeader\">MONTHLY SUBSCRIPTION</td>"
                        + "<td width=\"15%\" class=\"tblHeader\">REFUND OF WITHDRAWLS </br> AMT / NO. OF INST.</td>"
                        + "<td width=\"8%\" class=\"tblHeader\">TOTAL RELEASED</td>"
                        + "<td width=\"15%\" class=\"tblHeader\">REMARKS <br/>D.O.B and D.O.R.</td>"
                        + "</tr>");
                tblHeader.append("<tr class=\"tblHeader\" height=\"20px\">"
                        + "<td style=\"text-align:center;border-bottom:1px solid;\" class=\"printData\">&nbsp;1</td>"
                        + "<td style=\"text-align:center;border-bottom:1px solid;\" class=\"printData\">&nbsp;2</td>"
                        + "<td style=\"text-align:center;border-bottom:1px solid;\" class=\"printData\">&nbsp;3</td>"
                        + "<td style=\"text-align:center;border-bottom:1px solid;\" class=\"printData\">&nbsp;4</td>"
                        + "<td style=\"text-align:center;border-bottom:1px solid;\" class=\"printData\">&nbsp;5</td>"
                        + "<td style=\"text-align:center;border-bottom:1px solid;\" class=\"printData\">&nbsp;6</td>"
                        + "<td style=\"text-align:center;border-bottom:1px solid;\" class=\"printData\">&nbsp;7</td>"
                        + "<td style=\"text-align:center;border-bottom:1px solid;\" class=\"printData\">&nbsp;8</td>"
                        + "</tr>");
            } else if (schedule.equals("WRR")) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tblHeader;
    }

    @Override
    public ArrayList getEmpGpfDetails(String billNo) throws Exception {

        Connection con = null;

        ArrayList empGpfList = new ArrayList();

        PreparedStatement pst = null;
        ResultSet rs = null;

        GPFScheduleBean gpfBean = null;
        int slno = 0;
        int total = 0;
        //int pageno = scHelperBean.getPageno();
        try {
            con = this.dataSource.getConnection();

            String gpfQuery = "select GP,cur_salary,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,CUR_DESG,AQSL_NO,cpf_head,emp_mast.emp_id,GPF_NO,cur_basic"
                    + " from arr_mast inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id"
                    + " where bill_no=? and cpf_head > 0 and emp_mast.acct_type='GPF'";
            pst = con.prepareStatement(gpfQuery);
            pst.setInt(1, Integer.parseInt(billNo));
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getString("EMP_NAME") != null && !rs.getString("EMP_NAME").equals("")) {
                    
                    gpfBean = new GPFScheduleBean();
                    slno++;

                    gpfBean.setSlno(slno);
                    gpfBean.setEmpName(rs.getString("EMP_NAME"));
                    gpfBean.setDesignation(rs.getString("CUR_DESG"));
                    gpfBean.setAccountNo(rs.getString("GPF_NO"));
                    gpfBean.setBasicPay(rs.getString("CUR_BASIC"));
                    gpfBean.setGradePay(rs.getString("GP"));
                    gpfBean.setScaleOfPay(rs.getString("cur_salary"));
                    gpfBean.setTotalReleased(rs.getInt("cpf_head"));

                    total += rs.getInt("cpf_head");

                    if (slno % 8 == 0) {
                        gpfBean.setCarryForward(reportCarryForward(total, "GPF") + "");
                        gpfBean.setPagebreakchild("<input type=\"button\" name=\"pagebreak1\" style=\"page-break-before: always;width: 0;height: 0\"/>");
                        //gpfBean.setPageHeaderTable(reportTableHeader("GPF") + "");
                        gpfBean.setBroughtForward(reportBroughtForward(total, "GPF") + "");
                    } else {
                        gpfBean.setCarryForward("");
                        gpfBean.setPagebreakchild("");
                        //gpfBean.setPageHeaderTable("");
                        gpfBean.setBroughtForward("");
                    }
                    gpfBean.setReleaseTot(total);
                    if (total > 0) {
                        gpfBean.setAmountInWords(Numtowordconvertion.convertNumber((int) total).toUpperCase());
                    }
                    empGpfList.add(gpfBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empGpfList;
    }

    private StringBuffer reportCarryForward(int pagetotal, String schedule) {

        StringBuffer carryforward = null;

        try {

            carryforward = new StringBuffer("<table width=\"100%\" border=\"0\" id=\"tblcarryforward\" style=\"left: 18px;\" style=\"font-size:11px;\" cellpadding=\"0\" cellspacing=\"0\">"
                    + "<tr style=\"height:30px\">"
                    + "<td colspan=\"6\" style=\"text-align:right;\" class=\"txtf\">"
                    + "Carry Forward  " + pagetotal + ""
                    + "</td>"
                    + "</tr>"
                    + "</table>");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return carryforward;
    }

    private StringBuffer reportBroughtForward(int pagetotal, String schedule) {

        StringBuffer broughtforward = null;

        try {
            broughtforward = new StringBuffer("<tr style=\"height:30px\">"
                    + "<td colspan=\"6\" style=\"text-align:right;\" class=\"txtf\">"
                    + "Brought Forward  " + pagetotal + ""
                    + "</td>"
                    + "</tr>");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return broughtforward;
    }
}
