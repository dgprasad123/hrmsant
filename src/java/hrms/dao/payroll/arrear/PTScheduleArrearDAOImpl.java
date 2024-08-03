/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.arrear;

import hrms.common.CalendarCommonMethods;
import hrms.common.DataBaseFunctions;
import hrms.common.PayrollCommonFunction;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.PtScheduleBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author tushar
 */
public class PTScheduleArrearDAOImpl implements PTScheduleArrearDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getPTScheduleEmployeeList(String billno, int aqMonth, int aqYear) {
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        Statement stmt1 = null;
        ResultSet rs1 = null;
        PtScheduleBean ptBean = null;
        ArrayList ptSchldList = new ArrayList();
        int carryForwardTax = 0;
        int totalGross = 0;
        int basicSal = 0;
        String empCode = "";
        int sno = 1;
        String aqDtlsTbl = "";
        int totalAllowance = 0;
        try {
            con = dataSource.getConnection();

            String ptQuery = "select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,CUR_DESG,AQSL_NO,PT,emp_mast.emp_id,GPF_NO,cur_basic,arrear_pay"
                    + " from arr_mast inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id"
                    + " where bill_no=? and pt > 0";
            pst = con.prepareStatement(ptQuery);
            pst.setInt(1,Integer.parseInt(billno));
            rs = pst.executeQuery();
            while (rs.next()) {

                ptBean = new PtScheduleBean();

                ptBean.setSlno(sno);
                ptBean.setEmpname(rs.getString("EMP_NAME"));
                ptBean.setEmpdesg(rs.getString("CUR_DESG"));

                if (rs.getString("PT") != null && !rs.getString("PT").equals("")) {
                    ptBean.setEmpTaxOnProffesion(rs.getString("PT"));
                } else {
                    ptBean.setEmpTaxOnProffesion("0");
                }
                carryForwardTax = carryForwardTax + rs.getInt("PT");

                ptBean.setTotalTax(carryForwardTax + "");
                basicSal = rs.getInt("CUR_BASIC");
                ptBean.setBasicSal(basicSal);
                totalAllowance = rs.getInt("arrear_pay");

                String gross = basicSal + totalAllowance + "";
                totalGross = totalGross + Integer.parseInt(gross);
                //ptBean.setTotalGross(totalGross + "");
                ptBean.setTotalGross(totalAllowance + "");
                //ptBean.setEmpGrossSal(gross);
                ptBean.setEmpGrossSal(totalAllowance + "");

                ptSchldList.add(ptBean);
                sno++;

                if (sno % 13 == 0) {
                    ptBean.setPagebreakPT("<input type=\"button\" name=\"pagebreak1\" style=\"page-break-before: always;width: 0;height: 0\"/>");
                    ptBean.setPageHeaderPT(reportPageHeader(con, "PT", null, billno, "") + "");
                } else {
                    ptBean.setPagebreakPT("");
                    ptBean.setPageHeaderPT("");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(rs1, stmt1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ptSchldList;
    }

    @Override
    public PtScheduleBean getPTScheduleHeaderDetails(String billno) {

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PtScheduleBean ptBean = new PtScheduleBean();
        ArrayList ptHeaderList = new ArrayList();
        PayrollCommonFunction prcf = new PayrollCommonFunction();

        try {
            con = dataSource.getConnection();

            CommonReportParamBean bean = prcf.getCommonReportParameter(con, billno);
            ptBean.setPTScheduleDtls(getPTScheduleEmployeeList(billno, bean.getAqmonth(), bean.getAqyear()));

            ptBean.setDeptName(bean.getDeptname());
            ptBean.setOfficeName(bean.getOfficename());
            ptBean.setDdoDegn(bean.getDdoname());
            ptBean.setBillDesc(bean.getBilldesc());
            ptBean.setMonthYear(prcf.getMonthAndYear(con, billno));
            ptBean.setBillNo(billno);

            ptHeaderList.add(ptBean);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ptBean;

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
                    + "<td width=\"5%\" class=\"tblHeader\" style=\"text-align:center;\">Sl No</td>"
                    + "<td width=\"25%\" class=\"tblHeader\" style=\"text-align:center;\">Employee Name</td>"
                    + "<td width=\"35%\" class=\"tblHeader\" style=\"text-align:center;\">Designation</td>"
                    + "<td width=\"15%\" class=\"tblHeader\" style=\"text-align:center;\">Gross Salary</td>"
                    + "<td width=\"10%\" class=\"tblHeader\" style=\"text-align:center;\">Tax on Profession</td>"
                    + "<td width=\"10%\" class=\"tblHeader\" style=\"text-align:center;\">Remark</td>"
                    + "</tr>");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
        }
        return header;
    }

}
