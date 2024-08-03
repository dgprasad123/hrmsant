package hrms.dao.payroll.arrear;

import hrms.common.CalendarCommonMethods;
import hrms.common.DataBaseFunctions;
import hrms.common.Numtowordconvertion;
import hrms.common.PayrollCommonFunction;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.BankAcountScheduleBean;
import hrms.model.payroll.schedule.CommonScheduleMethods;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

public class BankAccountScheduleArrearDAOImpl implements BankAccountScheduleArrearDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public BankAcountScheduleBean getBankAcountScheduleHeaderDetails(String billno) {

        Connection con = null;
        BankAcountScheduleBean bankAcountBean = new BankAcountScheduleBean();
        try {
            con = this.dataSource.getConnection();

            PayrollCommonFunction prcf = new PayrollCommonFunction();
            CommonReportParamBean bean = prcf.getCommonReportParameter(con, billno);

            bankAcountBean.setBilldesc(bean.getBilldesc());
            bankAcountBean.setDdoDesg(bean.getDdoname());
            bankAcountBean.setOffName(bean.getOfficename());
            bankAcountBean.setMonth(CalendarCommonMethods.getFullMonthAsString(bean.getAqmonth()));
            bankAcountBean.setYear(bean.getAqyear() + "");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return bankAcountBean;
    }

    @Override
    public List getBankAcountScheduleEmpList(String billno, int year, int month,String typeOfBill) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList bankAccScheduleList = new ArrayList();
        BankAcountScheduleBean bankAcountBean = null;

        int pagetotal = 0;
        int pagetotalddo = 0;
        int slno = 0;
        int netCfTot = 0;
        String aqDTLS = "AQ_DTLS";
        try {
            con = this.dataSource.getConnection();

            String bankQuery = "select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,gpf_no,CUR_DESG,AQSL_NO,arrear_pay,full_arrear_pay,inctax,cpf_head,PT,emp_mast.bank_acc_no,g_bank.bank_name from arr_mast"
                    + " inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id"
                    + " LEFT OUTER JOIN G_BANK ON EMP_MAST.BANK_CODE=G_BANK.BANK_CODE"
                    + " where bill_no=?";
            pst = con.prepareStatement(bankQuery);
            pst.setInt(1, Integer.parseInt(billno));
            rs = pst.executeQuery();
            while (rs.next()) {
                bankAcountBean = new BankAcountScheduleBean();
                slno++;
                bankAcountBean.setSlno(slno);
                bankAcountBean.setDesignation(rs.getString("CUR_DESG"));
                bankAcountBean.setAccountNo(rs.getString("bank_acc_no"));
                bankAcountBean.setBankName(rs.getString("BANK_NAME"));
                bankAcountBean.setEmpname(rs.getString("EMP_NAME"));
                bankAcountBean.setGpfNo(rs.getString("gpf_no"));
                
                int arrearGross = 0;
                if (typeOfBill.equalsIgnoreCase("ARREAR_6") || typeOfBill.equalsIgnoreCase("ARREAR")) {
                    arrearGross = rs.getInt("arrear_pay");
                }else{
                    arrearGross = rs.getInt("full_arrear_pay");
                }
                int itAmt = rs.getInt("inctax");
                int gpfAmt = rs.getInt("cpf_head");
                int pt = rs.getInt("PT");

                int dedAmt = itAmt + gpfAmt + pt;

                int netPay = arrearGross - dedAmt;

                bankAcountBean.setNetAmount(netPay + "");
                netCfTot = netCfTot + netPay;
                bankAcountBean.setNetCfTot(String.valueOf(netCfTot));

                //bankAcountBean.setCarryForward(pagetotal + "");
                //bankAcountBean.setCarryForwardDDO(pagetotalddo + "");
                bankAccScheduleList.add(bankAcountBean);

                if (slno % 13 == 0) {
                    bankAcountBean.setCarryForward(reportCarryForward(netCfTot)+"");
                    bankAcountBean.setPagebreakBS("<input type=\"button\" name=\"pagebreak1\" style=\"page-break-before: always;width: 0;height: 0\"/>");
                    bankAcountBean.setPageHeaderBS(reportPageHeader(con, "BS", null, billno, "") + "");
                } else {
                    bankAcountBean.setCarryForward("");
                    bankAcountBean.setPagebreakBS("");
                    bankAcountBean.setPageHeaderBS("");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return bankAccScheduleList;

    }

    @Override
    public List getBankNameScheduleList(String billno, int year, int month) {
        Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;
        ArrayList bankNameScheduleList = new ArrayList();
        BankAcountScheduleBean bankAcountBean = null;
        String totamt = "0";
        String aqDTLS = "AQ_DTLS";

        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();

            String bankQry = "SELECT BANK_NAME from AQ_MAST WHERE BILL_NO='" + billno + "' and DEFAULT_BANK= 1 OR DEFAULT_BANK = 0 GROUP BY BANK_NAME";
            rs = stmt.executeQuery(bankQry);
            while (rs.next()) {
                bankAcountBean = new BankAcountScheduleBean();

                if (rs.getString("BANK_NAME") != null && !rs.getString("BANK_NAME").equals("")) {
                    bankAcountBean.setBankName(rs.getString("BANK_NAME"));
                    //totamt = new CommonScheduleMethods().getEmpSalDetails(con, rs.getString("BANK_NAME"), billno, true, aqDTLS, aqYear, aqMonth);
                } else {
                    bankAcountBean.setBankName("DDO Current");
                    //totamt = new CommonScheduleMethods().getEmpSalDetails(con, null, billno, false, aqDTLS, aqYear, aqMonth);
                }
                bankAcountBean.setNetAmt(totamt);
                bankNameScheduleList.add(bankAcountBean);
            }
            //String arr[] = new CommonScheduleMethods().getTotalAmount(con, billno, aqDTLS, aqYear, aqMonth);
            //bankAcountBean.setNetAmtNumbers(arr[0]);
            bankAcountBean.setAmountInWords(Numtowordconvertion.convertNumber((int) Double.parseDouble(bankAcountBean.getNetAmtNumbers())));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return bankNameScheduleList;
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
                    + "<td width=\"3%\" class=\"tblHeader\" style=\"text-align:center;\">Sl No</td>"
                    + "<td width=\"18%\" class=\"tblHeader\" style=\"text-align:center;\">SB A/C No. /  <br /> Name of the Bank</td>"
                    + "<td width=\"30%\" class=\"tblHeader\" style=\"text-align:center;\">Name/<br /> Designation</td>"
                    + "<td width=\"12%\" class=\"tblHeader\" style=\"text-align:center;\">(PF No) <br /> Net Amount <br /> (in Rs)</td>"
                    + "</tr>");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
        }
        return header;
    }
    
    private StringBuffer reportCarryForward(int pagetotal) {

        StringBuffer carryforward = null;

        try {

            carryforward = new StringBuffer("<tr style=\"height:30px\">"
                    + "<td colspan=\"4\" style=\"text-align:right;\" class=\"txtf\">"
                    + "Carry Forward  " + pagetotal + ""
                    + "</td>"
                    + "</tr>");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return carryforward;
    }
}
