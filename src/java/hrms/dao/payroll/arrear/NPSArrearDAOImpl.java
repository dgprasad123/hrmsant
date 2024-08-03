package hrms.dao.payroll.arrear;

import hrms.common.CalendarCommonMethods;
import hrms.common.DataBaseFunctions;
import hrms.common.Numtowordconvertion;
import hrms.common.PayrollCommonFunction;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.BillContributionRepotBean;
import hrms.model.payroll.schedule.CommonScheduleMethods;
import hrms.model.payroll.schedule.WrrScheduleBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

public class NPSArrearDAOImpl implements NPSArrearDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public BillContributionRepotBean getBillContributionRepotScheduleHeaderDetails(String annexure, String billno) {

        Connection con = null;

        int billmonth = 0;

        PreparedStatement pst = null;
        ResultSet rs = null;

        BillContributionRepotBean billContBean = new BillContributionRepotBean();
        try {
            con = dataSource.getConnection();

            PayrollCommonFunction prcf = new PayrollCommonFunction();
            CommonReportParamBean bean = prcf.getCommonReportParameter(con, billno);

            if (bean.getBilldesc() != null && !bean.getBilldesc().equals("")) {
                billContBean.setBillDesc(bean.getBilldesc());
            }
            if (bean.getBilldate() != null && !bean.getBilldate().equals("")) {
                billContBean.setBillDate(bean.getBilldate());
            }
            if (bean.getDdoname() != null && !bean.getDdoname().equals("")) {
                billContBean.setDdoName(bean.getDdoname());
            }
            if (bean.getDdoregno() != null && !bean.getDdoregno().equals("")) {
                billContBean.setDdoRegdNo(bean.getDdoregno());
            }
            if (bean.getDtoregno() != null && !bean.getDtoregno().equals("")) {
                billContBean.setDtoRegdNo(bean.getDtoregno());
            }
            billmonth = bean.getAqmonth();
            billContBean.setAqMonth(String.valueOf(billmonth++));
            billContBean.setBillMonth("" + billmonth);
            billContBean.setBillYear("" + bean.getAqyear());
            billContBean.setOffName(bean.getOfficename());
            billContBean.setBillNo(billno);
            billContBean.setAnnexure(annexure);

            DataBaseFunctions.closeSqlObjects(rs, pst);

            String trNameQry = "SELECT TR_NAME FROM (SELECT TR_CODE FROM BILL_MAST WHERE BILL_NO=?) BILL_MAST INNER JOIN G_TREASURY ON "
                    + "BILL_MAST.TR_CODE=G_TREASURY.TR_CODE";
            pst = con.prepareStatement(trNameQry);
            pst.setInt(1, Integer.parseInt(billno));
            rs = pst.executeQuery();
            if (rs.next()) {
                billContBean.setTreasuryName(rs.getString("TR_NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billContBean;
    }

    @Override
    public List getBillContributionRepotScheduleEmpList(String annexure, String billno, int aqYear, int aqMonth, String typeofbill) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList billContScheduleList = new ArrayList();
        BillContributionRepotBean billContBean = null;
        String arrearAmt = null;
        String arrearInst = null;
        int gtotal = 0;
        int slno = 0;
        int cpfPlusGcpf = 0;
        int totCpfPlusGcpf = 0;
        int annexeTotalGcpf = 0;
        int annexeTotalCpf = 0;
        double basic = 0.0;
        double gp = 0.0;
        double da = 0.0;
        double cpf = 0.0;
        double cp = 0.0;
        double gcpf = 0.0;
        double total = 0.0;
        double totalannex3 = 0.0;
        double grandtotal = 0;
        String aqDTLS = "AQ_DTLS";
        int cpfCaryFrd = 0;
        int totCaryFrd = 0;
        double anx3CaryFrd = 0;

        try {
            con = dataSource.getConnection();

            int frommonth = 0;
            int fromyear = 0;
            pst = con.prepareStatement("SELECT from_month,from_year FROM BILL_MAST WHERE BILL_NO=?");
            pst.setInt(1, Integer.parseInt(billno));
            rs = pst.executeQuery();
            if (rs.next()) {
                frommonth = rs.getInt("from_month");
                fromyear = rs.getInt("from_year");
            }

            String query = "select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,CUR_DESG,AQSL_NO,cpf_head,cpf_head_gc,emp_mast.emp_id,GPF_NO,cur_basic,arrear_pay"
                    + " from arr_mast inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id"
                    + " where bill_no=? and cpf_head > 0 and emp_mast.acct_type='PRAN'";
            pst = con.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(billno));
            rs = pst.executeQuery();
            while (rs.next()) {
                billContBean = new BillContributionRepotBean();
                slno++;
                billContBean.setSlno(slno);
                billContBean.setEmpname(rs.getString("EMP_NAME"));

                if (rs.getString("cur_desg") != null && !rs.getString("cur_desg").equals("")) {
                    billContBean.setEmpdesg(rs.getString("cur_desg"));
                }
                billContBean.setGpfNo(rs.getString("GPF_NO"));

                billContBean.setEmpBasicSal(rs.getString("arrear_pay"));
                if (billContBean.getEmpBasicSal() != null && !billContBean.getEmpBasicSal().equals("")) {
                    basic = Integer.parseInt(billContBean.getEmpBasicSal());
                }
                total = basic;
                billContBean.setTotal("" + total);

                //cpfCaryFrd = cpfCaryFrd + Integer.parseInt(billContBean.getTotal());
                //billContBean.setCpfCaryFrd(String.valueOf(cpfCaryFrd));
                arrearAmt = rs.getInt("cpf_head") + "";
                billContBean.setArrearAmt(arrearAmt);
                if(typeofbill.equals("ARREAR_NPS")){
                   //billContBean.setArrearAmt("0");
                }
                if ((frommonth >= 4 && fromyear == 2019) || fromyear >= 2020) {
                    billContBean.setArrearGAmt(rs.getInt("cpf_head_gc") + "");
                    totCaryFrd = totCaryFrd + Integer.parseInt(billContBean.getArrearGAmt());
                }else{
                    billContBean.setArrearGAmt(arrearAmt);
                    totCaryFrd = totCaryFrd + Integer.parseInt(arrearAmt);
                }
                billContBean.setEmpGcpf(arrearAmt + "");
                
                int totalannexure3 = 0;
                if(billContBean.getArrearAmt() != null && !billContBean.getArrearAmt().equals("")){
                    totalannexure3 = totalannexure3 + Integer.parseInt(billContBean.getArrearAmt());
                }
                if(billContBean.getArrearGAmt() != null && !billContBean.getArrearGAmt().equals("")){
                    totalannexure3 = totalannexure3 + Integer.parseInt(billContBean.getArrearGAmt());
                }
                billContBean.setTotalAnnexure3(totalannexure3+"");
                
                billContBean.setTotCaryFrd(String.valueOf(totCaryFrd));

                if (slno % 13 == 0) {
                    billContBean.setPagebreakAnx("<input type=\"button\" name=\"pagebreak1\" style=\"page-break-before: always;width: 0;height: 0\"/>");
                    billContBean.setPageHeaderAnx(reportPageHeader(con, annexure, null, billno, "") + "");
                } else {
                    billContBean.setPagebreakAnx("");
                    billContBean.setPageHeaderAnx("");
                }

                billContScheduleList.add(billContBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billContScheduleList;
    }

    private StringBuffer reportPageHeader(Connection con, String schedule, String gpfType, String billNo, String dednType) {

        PreparedStatement pst = null;
        ResultSet rs = null;

        String billmonth = "";
        String billyear = "";

        StringBuffer header = null;
        try {
            PayrollCommonFunction prcf = new PayrollCommonFunction();
            CommonReportParamBean bean = prcf.getCommonReportParameter(con, StringUtils.defaultString(billNo));

            String sql = "SELECT AQ_MONTH, AQ_YEAR FROM BILL_MAST WHERE BILL_NO = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billNo));
            rs = pst.executeQuery();
            if (rs.next()) {
                int month = rs.getInt("AQ_MONTH");
                billmonth = CalendarCommonMethods.getFullMonthAsString(month);
                billyear = rs.getString("AQ_YEAR");
            }

            header = new StringBuffer("<tr style=\"height:40px;\">"
                    + "<td width=\"4%\" rowspan=\"3\" class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>Sl No</b></td>"
                    + "<td width=\"10%\" rowspan=\"3\" class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>PRAN</b></td>"
                    + "<td width=\"25%\" rowspan=\"3\" class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>Employee Name</b></td>"
                    + "<td width=\"17%\" rowspan=\"3\" class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>Designation</b></td>"
                    + "<td width=\"8%\" rowspan=\"3\" class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>Basic Pay<br> + GP (Rs.)</b></td>"
                    + "<td width=\"5%\" rowspan=\"3\" class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>D.A<br>(Rs.)</b></td>"
                    + "<td width=\"8%\" rowspan=\"3\" class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>Total <br>(Rs.)</b></td>"
                    + "<td width=\"25%\" colspan=\"4\" class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>Employees Contribution</b></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"7%\" rowspan=\"2\" class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>Current (Rs.)</b></td>"
                    + "<td width=\"18%\" colspan=\"3\" class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>Arrears</b></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"5%\" class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>Installment</b></td>"
                    + "<td width=\"6%\" class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>Amount <br>(Rs.)</b></td>"
                    + "<td width=\"7%\" class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>Total <br>(Rs.)</b></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>1</b></td>"
                    + "<td class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>2</b></td>"
                    + "<td class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>3</b></td>"
                    + "<td class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>4</b></td>"
                    + "<td class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>5</b></td>"
                    + "<td class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>6</b></td>"
                    + "<td class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>7</b></td>"
                    + "<td class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>8</b></td>"
                    + "<td class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>9</b></td>"
                    + "<td class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>10</b></td>"
                    + "<td class=\"tblHeader\" style=\"font-family:verdana;font-size:12px;text-align:center;border:1px solid black;\"><b>11</b></td>"
                    + "</tr>");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
        }
        return header;
    }

}
