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
import hrms.model.payroll.arrear.ITScheduleArrearBean;
import hrms.model.payroll.schedule.CommonScheduleMethods;
import hrms.model.payroll.schedule.ItScheduleBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

public class ITScheduleArrearDAOImpl implements ITScheduleArrearDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getITScheduleEmployeeList(String billno, int aqMonth, int aqYear) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ITScheduleArrearBean itBean = null;
        ArrayList itSchList = new ArrayList();
        String empCode = "";
        int sno = 1;
        String aqDtlsTbl = "";
        int carryForward = 0;
        try {
            con = dataSource.getConnection();

            String itQuery = "select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,CUR_DESG,AQSL_NO,INCTAX,id_no,emp_mast.emp_id,arrear_pay from arr_mast"
                    + " inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id"
                    + " left outer join (select id_no,emp_id from emp_id_doc where id_description='PAN')emp_id_doc on emp_mast.emp_id=emp_id_doc.emp_id"
                    + " where bill_no=? and inctax > 0";
            pst = con.prepareStatement(itQuery);
            pst.setInt(1, Integer.parseInt(billno));
            rs = pst.executeQuery();
            while (rs.next()) {
                itBean = new ITScheduleArrearBean();

                itBean.setSlno(sno);
                itBean.setEmpname(rs.getString("EMP_NAME"));
                itBean.setEmpdesg(rs.getString("CUR_DESG"));
                if (rs.getString("arrear_pay") != null && !rs.getString("arrear_pay").equals("")) {
                    itBean.setEmpBasicSal(rs.getString("arrear_pay"));
                } else {
                    itBean.setEmpBasicSal("");
                }
                //itobjclass.setEmpBasicSal(rs.getString("CUR_BASIC"));
                if (rs.getString("INCTAX") != null && !rs.getString("INCTAX").equalsIgnoreCase("")) {
                    int dedAmt = rs.getInt("INCTAX");
                    carryForward = carryForward + dedAmt;
                    itBean.setEmpDedutAmount(dedAmt);
                    itBean.setCarryForward(carryForward);
                } else {
                    itBean.setEmpDedutAmount(0);
                }
                if (rs.getString("ID_NO") != null && !rs.getString("ID_NO").equalsIgnoreCase("")) {
                    itBean.setEmpPanNo(rs.getString("ID_NO"));
                } else {
                    itBean.setEmpPanNo("");
                }
                empCode = rs.getString("emp_id");

                if (sno % 20 == 0) {
                    itBean.setPagebreakIT("<input type=\"button\" name=\"pagebreak1\" style=\"page-break-before: always;width: 0;height: 0\"/>");
                    itBean.setPageHeaderIT(reportPageHeader(con, "IT", null, billno, "") + "");
                } else {
                    itBean.setPagebreakIT("");
                    itBean.setPageHeaderIT("");
                }

                itSchList.add(itBean);
                sno++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return itSchList;
    }

    @Override
    public ItScheduleBean getITScheduleHeaderDetails(String billno, String schedule) {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        ItScheduleBean itBean = new ItScheduleBean();
        
        ArrayList itHeaderList = new ArrayList();
        PayrollCommonFunction prcf = new PayrollCommonFunction();
        String btId = "";

        try {
            con = dataSource.getConnection();

            CommonReportParamBean bean = prcf.getCommonReportParameter(con, billno);

            pst = con.prepareStatement("SELECT BT_ID FROM G_AD_LIST WHERE SCHEDULE=?");
            pst.setString(1,schedule);
            rs = pst.executeQuery();
            if (rs.next()) {
                btId = rs.getString("BT_ID");
            }
            itBean.setBtId(btId); 
            
            itBean.setOfficeName(bean.getOfficename());
            itBean.setMontYear(prcf.getMonthAndYear(con, billno));
            itBean.setScheduleName(prcf.getReportName(con, schedule));
            itBean.setBillNo(bean.getBilldesc());
            itBean.setAqmonth(CalendarCommonMethods.getMonthAsString(bean.getAqmonth()));
            itBean.setAqyear(bean.getAqyear() + "");
            itBean.setTanno(bean.getTanno());
            itBean.setDeptName(bean.getDeptname());
            itBean.setDdoDegn(bean.getDdoname());
            itBean.setBillDesc(bean.getBilldesc());
            itBean.setReportName(prcf.getReportName(con, schedule));

            itHeaderList.add(itBean);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return itBean;
        
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

            header = new StringBuffer("<tr style=\"height: 30px\">\n"
                    + "<th width=\"5%\" style=\"text-align: center;border-bottom:1px solid #000000;border-top:1px solid #000000;border-right:1px solid #000000;border-left:1px solid #000000;\">Sl. No.</th>\n"
                    + "<th width=\"20%\" style=\"text-align: center;border-bottom:1px solid #000000;border-top:1px solid #000000;border-right:1px solid #000000;border-left:1px solid #000000;\">Name and Designation of Employee</th>\n"
                    + "<th width=\"10%\" style=\"text-align: center;border-bottom:1px solid #000000;border-top:1px solid #000000;border-right:1px solid #000000;border-left:1px solid #000000;\">PAN No</th>\n"
                    + "<th width=\"10%\" style=\"text-align: center;border-bottom:1px solid #000000;border-top:1px solid #000000;border-right:1px solid #000000;border-left:1px solid #000000;\">Gross Salary</th>\n"
                    + "<th width=\"10%\" style=\"text-align: center;border-bottom:1px solid #000000;border-top:1px solid #000000;border-right:1px solid #000000;border-left:1px solid #000000;\">Deduction</th>\n"
                    + "</tr>");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
        }
        return header;
    }
}
