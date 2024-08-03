package hrms.common;

import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.WrrScheduleBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class PayrollCommonFunction {

    public CommonReportParamBean getCommonReportParameter(Connection con, String billNo) throws Exception {

        CommonReportParamBean commonBean = new CommonReportParamBean();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = con.createStatement();
            if (billNo != null && !billNo.equals("")) {
                String crbQry = "SELECT SUFFIX,DESCRIPTION,BILL_DESC,BILL_DATE,AQ_YEAR,AQ_MONTH,G_OFFICE.OFF_EN,DIST_NAME,STATE_NAME, G_POST.POST,"
                        + "G_OFFICE.OFF_EN,G_OFFICE.DDO_REG_NO,G_OFFICE.DTO_REG_NO,G_DEPARTMENT.DEPARTMENT_NAME,TAN_NO,FA_BTID,PA_CODE,BILL_NO.OFF_CODE,"
                        + "BILL_NO.TR_CODE,TR_NAME,BILL_NO.bill_type, type_of_bill, bill_status_id,gross_amt,ded_amt,pvt_ded_amt, vch_no, vch_date FROM ( "
                        + "SELECT BILL_GROUP_ID,OFF_CODE,BILL_DESC,AQ_YEAR,AQ_MONTH,BILL_DATE,TR_CODE,bill_type, type_of_bill, bill_status_id,"
                        + "gross_amt,ded_amt,pvt_ded_amt, vch_no, vch_date FROM BILL_MAST WHERE BILL_NO='" + billNo + "') BILL_NO  "
                        + "INNER JOIN G_OFFICE ON BILL_NO.OFF_CODE = G_OFFICE.OFF_CODE "
                        + "LEFT OUTER JOIN G_POST ON G_OFFICE.DDO_POST=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN G_DEPARTMENT ON G_OFFICE.DEPARTMENT_CODE =G_DEPARTMENT.DEPARTMENT_CODE "
                        + "LEFT OUTER JOIN G_DISTRICT ON G_OFFICE.DIST_CODE=G_DISTRICT.DIST_CODE "
                        + "LEFT OUTER JOIN G_STATE ON G_OFFICE.STATE_CODE=G_STATE.STATE_CODE "
                        + "LEFT OUTER JOIN BILL_GROUP_MASTER ON BILL_NO.BILL_GROUP_ID=BILL_GROUP_MASTER.BILL_GROUP_ID "
                        + "LEFT OUTER JOIN G_TREASURY ON BILL_NO.TR_CODE=G_TREASURY.TR_CODE";

                rs = st.executeQuery(crbQry);
                if (rs.next()) {
                    commonBean.setSuffix(rs.getString("SUFFIX"));
                    commonBean.setBillgroupDesc(rs.getString("DESCRIPTION"));
                    commonBean.setBilldesc(rs.getString("BILL_DESC"));
                    commonBean.setBilldate(CommonFunctions.getFormattedOutputDate1(rs.getDate("BILL_DATE")));
                    commonBean.setAqyear(rs.getInt("AQ_YEAR"));
                    commonBean.setAqmonth(rs.getInt("AQ_MONTH"));
                    commonBean.setAqMonthAsName(CommonFunctions.getMonthAsString(commonBean.getAqmonth()));

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
                    if (rs.getString("FA_BTID") != null && !rs.getString("FA_BTID").equals("")) {
                        commonBean.setFabtid(rs.getString("FA_BTID"));
                    } else {
                        commonBean.setFabtid("55032");
                    }
                    commonBean.setTreasuryname(rs.getString("TR_NAME"));
                    commonBean.setTypeofBill(rs.getString("type_of_bill"));
                    commonBean.setBillType(rs.getString("bill_type"));
                    commonBean.setBillStatusId(rs.getInt("bill_status_id"));

                    commonBean.setBillGrossAmt(rs.getInt("gross_amt"));
                    commonBean.setBillDedAmt(rs.getInt("ded_amt"));
                    commonBean.setBillPvtDedAmt(rs.getInt("pvt_ded_amt"));

                    if (rs.getString("vch_no") != null && !rs.getString("vch_no").equals("")) {
                        String vchNo1 = "";
                        String vchNo = rs.getString("vch_no");
                        if (vchNo.length() > 4) {
                            vchNo1 = vchNo.substring(4);
                        }
                        commonBean.setVchNo(vchNo1);
                    } else {
                        commonBean.setVchNo("");
                    }

                    if (rs.getString("vch_date") != null && !rs.getString("vch_date").equals("")) {
                        commonBean.setVchDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("vch_date")));
                    } else {
                        commonBean.setVchDate("");
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
        }
        return commonBean;
    }

    public String getMonthAndYear(Connection con, String billNo) throws Exception {

        Statement st2 = null;
        ResultSet rs2 = null;
        String query1 = "";

        String yearVal = "";
        int monthVal = 0;
        String monthStr = "";
        String result = "";
        try {
            st2 = con.createStatement();
            query1 = "SELECT AQ_MONTH,AQ_YEAR FROM BILL_MAST WHERE BILL_NO='" + billNo + "'";
            rs2 = st2.executeQuery(query1);
            if (rs2.next()) {
                yearVal = rs2.getString("AQ_YEAR");
                monthVal = rs2.getInt("AQ_MONTH");
                monthStr = CalendarCommonMethods.getFullMonthAsString(monthVal);
                result = monthStr + "-" + yearVal;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs2, st2);
        }
        return result;
    }

    public String getReportName(Connection con, String schedule) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String repName = "";
        try {
            st = con.createStatement();
            String schduleQry = "Select G_SCHEDULE.SCHEDULE_DESC from G_SCHEDULE where G_SCHEDULE.SCHEDULE='" + schedule + "'";
            rs = st.executeQuery(schduleQry);
            if (rs.next()) {
                repName = rs.getString("SCHEDULE_DESC");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
        }
        return repName;
    }

    public String getDemandName(String schedule, Connection con) throws Exception {

        PreparedStatement pst = null;
        ResultSet rs = null;
        String repName = "";
        try {
            pst = con.prepareStatement("Select G_SCHEDULE.DEMAND_NO from G_SCHEDULE where G_SCHEDULE.SCHEDULE=?");
            pst.setString(1, schedule);
            rs = pst.executeQuery();
            if (rs.next()) {
                repName = rs.getString("DEMAND_NO");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
        }
        return repName;
    }

    public void getDate(Connection con, String billno, WrrScheduleBean wrrbean) throws Exception {
        ResultSet rs = null;
        ResultSet rs1 = null;
        Statement st = null;
        Statement st1 = null;

        try {
            st = con.createStatement();
            st1 = con.createStatement();
            rs = st.executeQuery("SELECT AQ_MONTH,AQ_YEAR FROM BILL_MAST WHERE BILL_NO='" + billno + "'");
            while (rs.next()) {
                wrrbean.setTxtMonth(CalendarCommonMethods.getFullMonthAsString(rs.getInt("AQ_MONTH")));
                wrrbean.setTxtYear(rs.getString("AQ_YEAR"));
            }
            rs1 = st1.executeQuery("select POST from BILL_MAST inner join G_POST on BILL_MAST.OFF_DDO=G_POST.POST_CODE where BILL_MAST.BILL_NO='" + billno + "'");
            while (rs1.next()) {
                wrrbean.setDdoDegn(rs1.getString("POST"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(rs1, st1);
        }
    }
}
