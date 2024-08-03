/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.LeaveUpdateBalance;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import static hrms.dao.leaveapply.LeaveApplyDAOImpl.getMaxCreditLimitofLeave;
import hrms.model.updateleave.UpdateLeave;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class LeaveUpdateDAOImpl implements LeaveUpdateDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ArrayList getPostWiseEmpList(String postCode, String offCode, String year, String month) {

        double totCrLeave = 0;
        double totdebitCount = 0;
        double finalleaveCnt = 0;
        double leaveavail = 0;

        Connection con = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        PreparedStatement pstmt4 = null;
        ArrayList empList = new ArrayList();
        //HashMap<String,Object> hm=new HashMap<String,Object>();
        UpdateLeave updateLeave = null;

        PreparedStatement pstmtLeaveCr = null;
        double maxnoofEl = 0;
        ResultSet leaveCrRs = null;
        ResultSet leaveCrRs1 = null;
        PreparedStatement pstmtLeaveCr1 = null;
        PreparedStatement pstexist = null;
        ResultSet rsexist = null;

        /* Statement st = null;
         Statement st2 = null;
         Statement st4 = null;
         ResultSet leaveCrRs = null;
         ResultSet leaveCrRs2 = null;
         ResultSet leaveCrRs4 = null;*/
        try {
            con = dataSource.getConnection();
            /* st = con.createStatement();
             st2 = con.createStatement();
             st4 = con.createStatement();*/

            pstmt1 = con.prepareStatement("select emp_id from emp_mast "
                    + "	LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC "
                    + "	LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "	WHERE CUR_OFF_CODE=? AND DEP_CODE='02' AND GPC=?");
            pstmt1.setString(1, offCode);
            pstmt1.setString(2, postCode);
            rs1 = pstmt1.executeQuery();
            while (rs1.next()) {
                pstmt2 = con.prepareStatement("select emp_id from emp_leave_balance where emp_id=? and year=? and month=?");
                pstmt2.setString(1, rs1.getString("emp_id"));
                pstmt2.setString(2, year);
                pstmt2.setString(3, month);
                rs2 = pstmt2.executeQuery();
                if (rs2.next()) {

                } else {
                    pstexist = con.prepareStatement("select balance,available,tol_id from emp_leave_balance where emp_id=? and year=? and month=? and tol_id=?");
                    pstexist.setString(1, rs1.getString("emp_id"));
                    pstexist.setString(2, String.valueOf(Integer.parseInt(year) - 1));
                    if (Integer.parseInt(month) - 1 == 0) {
                        pstexist.setString(3, "12");
                    } else {
                        pstexist.setString(3, String.valueOf(Integer.parseInt(month) - 1));
                    }
                    pstexist.setString(4, "CL");
                    rsexist = pstexist.executeQuery();
                    if (rsexist.next()) {
                        pstmt3 = con.prepareStatement("insert into emp_leave_balance (emp_id,tol_id,balance,available,year,month) values(?,?,?,?,?,?)");
                        pstmt3.setString(1, rs1.getString("emp_id"));
                        pstmt3.setString(2, "CL");
                        pstmt3.setDouble(3, rsexist.getDouble("balance"));
                        if (Integer.parseInt(month) - 1 == 0) {
                            pstmt3.setDouble(4, rsexist.getDouble("balance"));
                        } else {
                            pstmt3.setDouble(4, rsexist.getDouble("available"));
                        }
                        pstmt3.setString(5, year);
                        pstmt3.setString(6, month);
                        pstmt3.executeUpdate();
                    }

                    pstmtLeaveCr = con.prepareStatement("SELECT SUM (CR_COUNT) AS total FROM EMP_LEAVE_CR WHERE EMP_ID=? AND (TOL_ID='EL' or TOL_ID='AEL') AND CR_TYPE='G' group by CR_TYPE");
                    pstmtLeaveCr.setString(1, rs1.getString("emp_id"));
                    leaveCrRs = pstmtLeaveCr.executeQuery();
                    if (leaveCrRs.next()) {
                        totCrLeave = leaveCrRs.getDouble("total");
                    }

                    pstmtLeaveCr1 = con.prepareStatement("select sum((tdate::date - fdate::date))+count(*) totdebit from(select fdate,tdate from hw_emp_leave where emp_id=? and tol_id='EL'                                                         "
                            + "union "
                            + "select fdate,tdate from emp_leave where emp_id=? and tol_id='EL')tab ");
                    pstmtLeaveCr1.setString(1, rs1.getString("emp_id"));
                    pstmtLeaveCr1.setString(2, rs1.getString("emp_id"));
                    leaveCrRs1 = pstmtLeaveCr1.executeQuery();
                    if (leaveCrRs1.next()) {
                        totdebitCount = leaveCrRs1.getDouble("totdebit");
                    }
                    finalleaveCnt = totCrLeave - totdebitCount;
                    if (finalleaveCnt >= 300) {
                        leaveavail = 300;
                    } else {
                        leaveavail = finalleaveCnt;
                    }

                    pstmt4 = con.prepareStatement("insert into emp_leave_balance (emp_id,tol_id,balance,available,year,month) values(?,?,?,?,?,?)");
                    pstmt4.setString(1, rs1.getString("emp_id"));
                    pstmt4.setString(2, "EL");
                    pstmt4.setDouble(3, leaveavail);
                    if (leaveavail > 0) {
                        pstmt4.setDouble(4, leaveavail + 15);
                    } else {
                        pstmt4.setDouble(4, 0);
                    }
                    pstmt4.setString(5, year);
                    pstmt4.setString(6, month);
                    pstmt4.executeUpdate();
                    /* }*/
                }
            }

            pstmt = con.prepareStatement("SELECT emp_leave_balance.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,"
                    + "	max(balance) FILTER (WHERE tol_id = 'CL') AS CL_BALANCE, "
                    + "	max(balance) FILTER (WHERE tol_id = 'EL') AS EL_BALANCE, "
                    + "	max(available) FILTER (WHERE tol_id = 'CL') AS CL_AVAILABLE, "
                    + "	max(available) FILTER (WHERE tol_id = 'EL') AS EL_AVAILABLE "
                    + "	FROM emp_leave_balance"
                    + "	inner join EMP_MAST on EMP_MAST.emp_id=emp_leave_balance.emp_id  "
                    + "	LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC "
                    + "	LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "	WHERE CUR_OFF_CODE=? AND DEP_CODE='02' AND GPC=? and year=? and month=?"
                    + "	GROUP BY emp_leave_balance.emp_id,EMPNAME order by EMPNAME");

            pstmt.setString(1, offCode);
            pstmt.setString(2, postCode);
            pstmt.setString(3, year);
            pstmt.setString(4, month);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                updateLeave = new UpdateLeave();
                updateLeave.setEmpid(rs.getString("EMP_ID"));
                updateLeave.setEmpName(rs.getString("EMPNAME"));
                updateLeave.setTotalCl(rs.getString("CL_BALANCE"));
                updateLeave.setTotalClAvail(rs.getString("CL_AVAILABLE"));
                updateLeave.setTotalEl(rs.getString("EL_BALANCE"));
                updateLeave.setTotalElAvail(rs.getString("EL_AVAILABLE"));
                //hm.put(employee.getEmpid(), employee);
                empList.add(updateLeave);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            /* DataBaseFunctions.closeSqlObjects(leaveCrRs, st);
             DataBaseFunctions.closeSqlObjects(leaveCrRs2, st2);
             DataBaseFunctions.closeSqlObjects(leaveCrRs4, st4);
             */
            DataBaseFunctions.closeSqlObjects(leaveCrRs, pstmtLeaveCr);
            DataBaseFunctions.closeSqlObjects(leaveCrRs1, pstmtLeaveCr1);
            DataBaseFunctions.closeSqlObjects(rsexist, pstexist);
            DataBaseFunctions.closeSqlObjects(rs1, pstmt1);
            DataBaseFunctions.closeSqlObjects(rs2, pstmt2);
            DataBaseFunctions.closeSqlObjects(pstmt3);
            DataBaseFunctions.closeSqlObjects(pstmt4);
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empList;
    }

    @Override
    public UpdateLeave getLeaveBalance(String empId, String curYear, String tolId, String curMonth) {
        UpdateLeave updateLeaveBal = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        PreparedStatement pst1 = null;
        ResultSet rs1 = null;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("select * from emp_leave_balance where emp_id=? and year=? and tol_id=? and month=?");
            pst.setString(1, empId);
            pst.setString(2, curYear);
            pst.setString(3, tolId);
            pst.setString(4, curMonth);
            rs = pst.executeQuery();
            if (rs.next()) {
                updateLeaveBal = new UpdateLeave();
                updateLeaveBal.setYear(rs.getString("year"));
                updateLeaveBal.setTotalCl(rs.getString("balance"));
                updateLeaveBal.setTotalClAvail(rs.getString("available"));
                updateLeaveBal.setMonth(rs.getString("month"));
            } else {
                updateLeaveBal = new UpdateLeave();
                updateLeaveBal.setYear(curYear);
                updateLeaveBal.setTotalCl("0");
                updateLeaveBal.setTotalClAvail("0");
                updateLeaveBal.setMonth(curMonth);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return updateLeaveBal;
    }

    @Override
    public void updateLeaveBalance(UpdateLeave updateLeave) {
        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select * from emp_leave_balance where emp_id=? and year=? and tol_id=? and month=?");
            pst.setString(1, updateLeave.getEmpid());
            pst.setString(2, updateLeave.getYear());
            pst.setString(3, updateLeave.getTolId());
            pst.setString(4, updateLeave.getMonth());
            rs = pst.executeQuery();
            if (rs.next()) {
                pst = con.prepareStatement("update emp_leave_balance set balance=?,available=? where emp_id=? and year=? and tol_id=? and month=?");
                pst.setDouble(1, Double.parseDouble(updateLeave.getTotalCl()));
                pst.setDouble(2, Double.parseDouble(updateLeave.getTotalClAvail()));
                pst.setString(3, updateLeave.getEmpid());
                pst.setString(4, updateLeave.getYear());
                pst.setString(5, updateLeave.getTolId());
                pst.setString(6, updateLeave.getMonth());
                pst.executeUpdate();
            } else {
                pst = con.prepareStatement("insert into emp_leave_balance (emp_id,tol_id,year,balance,available,month) values (?,?,?,?,?,?)");
                pst.setString(1, updateLeave.getEmpid());
                pst.setString(2, updateLeave.getTolId());
                pst.setString(3, updateLeave.getYear());
                if (updateLeave.getTotalCl() != null && !updateLeave.getTotalCl().equals("")) {
                    pst.setDouble(4, Double.parseDouble(updateLeave.getTotalCl()));
                } else {
                    pst.setDouble(4, 0);
                }
                if (updateLeave.getTotalClAvail() != null && !updateLeave.getTotalClAvail().equals("")) {
                    pst.setDouble(5, Double.parseDouble(updateLeave.getTotalClAvail()));
                } else {
                    pst.setDouble(5, 0);
                }
                pst.setString(6, updateLeave.getMonth());
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getLeaveStatusReport(String offCode, String curYear, String curMon, String curDate) {
        Connection con = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        ArrayList empList = new ArrayList();
        //HashMap<String,Object> hm=new HashMap<String,Object>();
        UpdateLeave updateLeave = null;
        int totalFulldayAbsent = 0;
        int totnoOfHalfdayAbsent = 0;
        double finalAbsent = 0.0;
        int totalSaturday = 0;
        int satholidays = 0;
        double totBalanceAsOnDate;
        String insertsql = "";
        PreparedStatement insertpst = null;
        PreparedStatement selpstmst = null;
        String selEmpFromBal = null;
        ResultSet empBalResult = null;
        PreparedStatement selPst = null;
        String updateEmpFromBal = "";
        ResultSet lastdateResult = null;
        PreparedStatement lastDatepst = null;
        Date lastDateStr = null;
        String lastDateMonthYrStr = "";
        String absentStr = "";
        String currentMon = "";
        double totHalfDayAbsent = 0.0;
        int prevMon = 0;
        String selSql = "";
        ResultSet selRs = null;
        String updatesql = "";
        String updateEmpId = "";
        PreparedStatement selEmpId = null;
        ResultSet rsEmpId = null;
        float threeDaysCount = 0.1f;
        float threeDaysCountOnePrece = 0.1f;

        try {
            con = dataSource.getConnection();
            // lastDateMonthYrStr=curYear+curMon;

            if ((curYear != null && !curYear.equals("")) && (curMon != null && !curMon.equals(""))) {
                pstmt = con.prepareStatement("select * from(select tab3.user_id,tab3.EMP_ID,tab3.empname,CL_BALANCE,CL_AVAILABLE,EL_BALANCE,EL_AVAILABLE,sum(tab3.lateinpunch) latepunch from     "
                        + "(select  tab2.user_id,empmast.EMP_ID,initcap(tab2.name) as empname ,CL_BALANCE,CL_AVAILABLE,EL_BALANCE,EL_AVAILABLE,lateinpunch  "
                        + " from(SELECT EMP_MAST.EMP_ID,employment_type_id,    "
                        + "max(balance) FILTER (WHERE tol_id = 'CL') AS CL_BALANCE,max(available) FILTER (WHERE tol_id = 'CL') AS CL_AVAILABLE,    "
                        + "max(balance) FILTER (WHERE tol_id = 'EL') AS EL_BALANCE,max(available) FILTER (WHERE tol_id = 'EL') AS EL_AVAILABLE    "
                        + "FROM emp_leave_balance    "
                        + "inner join EMP_MAST on EMP_MAST.emp_id=emp_leave_balance.emp_id     "
                        + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC WHERE CUR_OFF_CODE='OLSGAD0010001' AND DEP_CODE='02' and year=? and month=?    "
                        + "GROUP BY EMP_MAST.emp_id,employment_type_id )empmast    "
                        + "right outer join ( select  emp_attendance_cmgi.user_id,emp_attendance_cmgi.name,tab1.no_of_lateinpunch as lateinpunch from emp_attendance_cmgi     "
                        + "left outer join ( select count(*) no_of_lateinpunch,    "
                        + "user_id,emp_id,tab.name from( select count(*),user_id,name from emp_attendance_cmgi where    "
                        + "extract(year from attendance_date) = ? and    "
                        + "extract(month from attendance_date) = ?    "
                        + "group by INPUNCH_DATE::date,attendance_date,user_id,name having cast(MIN(INPUNCH_DATE) as time) > '10:30:00' and cast(MIN(INPUNCH_DATE) as time) < '13:00:00'    "
                        + "ORDER BY attendance_date) tab left outer join emp_mast on emp_mast.employment_type_id=tab.user_id group by user_id,emp_id,tab.name)tab1 on    "
                        + "emp_attendance_cmgi.user_id=tab1.user_id  where "
                        + "extract(year from      attendance_date) = ? and  "
                        + "extract(month from attendance_date) = ? group by emp_attendance_cmgi.user_id,emp_attendance_cmgi.name,tab1.no_of_lateinpunch,lateinpunch)tab2 on      "
                        + " tab2.user_id=empmast.employment_type_id group by tab2.user_id,empmast.EMP_ID,empname,CL_BALANCE,CL_AVAILABLE,EL_BALANCE,EL_AVAILABLE,lateinpunch)tab3   "
                        + " group by tab3.user_id,tab3.EMP_ID,tab3.empname,CL_BALANCE,CL_AVAILABLE,EL_BALANCE,EL_AVAILABLE)tab7  "
                        + "left outer join     "
                        + " (select tab.user_id,initcap(name) as empname,   count(*)missing_outpunch_count  from(select MIN(INPUNCH_DATE) AS INPUNCH_DATE,    "
                        + " CASE    "
                        + "    WHEN (select count(*) FROM emp_attendance_cmgi where user_id = EA.user_id and attendance_date = EA.attendance_date)>1 THEN MAX(INPUNCH_DATE)    "
                        + "    ELSE null    "
                        + "  END    "
                        + "  AS OUTPUNCH_DATE, cast(MIN(INPUNCH_DATE) as time),  count(*) missingoutpunch, name, user_id from emp_attendance_cmgi EA    "
                        + " where  extract(year from attendance_date) = ?   "
                        + " and extract(month from attendance_date) = ?     "
                        + " group by attendance_date,user_id, name    "
                        + " ORDER BY attendance_date)tab where tab.missingoutpunch=1 group by tab.user_id,empname)tab4 on tab7.user_id=tab4.user_id");

                // pstmt.setString(1, "OLSGAD0010001");
                //  System.out.println("======"+curYear+"====="+curMon);
                // if (curYear.equals("2023") && curMon.equals("01")) {
                pstmt.setString(1, curYear);
                pstmt.setString(2, curMon);
                pstmt.setDouble(3, Double.parseDouble(curYear));
                pstmt.setDouble(4, Double.parseDouble(curMon));
                pstmt.setDouble(5, Double.parseDouble(curYear));
                pstmt.setDouble(6, Double.parseDouble(curMon));
                pstmt.setDouble(7, Double.parseDouble(curYear));
                pstmt.setDouble(8, Double.parseDouble(curMon));

                rs = pstmt.executeQuery();
                while (rs.next()) {

                    updateLeave = new UpdateLeave();
                    updateLeave.setEmpid(rs.getString("EMP_ID"));
                    updateLeave.setBiometricId(rs.getString("user_id"));
                    updateLeave.setEmpName(rs.getString("empname"));
                    updateLeave.setTotalCl(rs.getString("CL_BALANCE"));
                    updateLeave.setTotalEl(rs.getString("EL_BALANCE"));
                    updateLeave.setTotalElAvail(rs.getString("EL_AVAILABLE"));

                    if (rs.getString("latepunch") != null) {
                        updateLeave.setTotLateInpunch(rs.getString("latepunch"));
                        threeDaysCount = (Float) Float.parseFloat(rs.getString("latepunch")) / 3;
                        threeDaysCountOnePrece = (Float) Float.parseFloat(String.format("%.1f", threeDaysCount));
                        if (threeDaysCountOnePrece == 1.0F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + threeDaysCountOnePrece);
                        } else if (threeDaysCountOnePrece == 2.0F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + threeDaysCountOnePrece);
                        } else if (threeDaysCountOnePrece == 3.0F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + threeDaysCountOnePrece);
                        } else if (threeDaysCountOnePrece == 4.0F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + threeDaysCountOnePrece);
                        } else if (threeDaysCountOnePrece == 5.0F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + threeDaysCountOnePrece);
                        } else if (threeDaysCountOnePrece == 6.0F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + threeDaysCountOnePrece);
                        } else if (threeDaysCountOnePrece == 7.0F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + threeDaysCountOnePrece);
                        } else if (threeDaysCountOnePrece == 8.0F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + threeDaysCountOnePrece);
                        } else if (threeDaysCountOnePrece == 9.0F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + threeDaysCountOnePrece);
                        } else if (threeDaysCountOnePrece == 10.0F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + threeDaysCountOnePrece);
                        } else if (threeDaysCountOnePrece >= 0.5F && threeDaysCountOnePrece < 1.0F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + 0.5);
                        } else if (threeDaysCountOnePrece >= 1.5F && threeDaysCountOnePrece < 2.0F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + 1.5);
                        } else if (threeDaysCountOnePrece >= 2.5F && threeDaysCountOnePrece < 3.0F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + 2.5);
                        } else if (threeDaysCountOnePrece >= 3.5F && threeDaysCountOnePrece < 4.0F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + 3.5);
                        } else if (threeDaysCountOnePrece >= 4.5F && threeDaysCountOnePrece < 5.0F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + 4.5);
                        } else if (threeDaysCountOnePrece >= 5.5F && threeDaysCountOnePrece < 6.0F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + 5.5);
                        } else if (threeDaysCountOnePrece >= 6.5F && threeDaysCountOnePrece < 7.0F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + 6.5);
                        } else if (threeDaysCountOnePrece >= 7.5F && threeDaysCountOnePrece < 8.0F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + 7.5);
                        } else if (threeDaysCountOnePrece > 1.0F && threeDaysCountOnePrece < 1.5F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + 1.0);
                        } else if (threeDaysCountOnePrece > 2.0F && threeDaysCountOnePrece < 2.5F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + 2.0);
                        } else if (threeDaysCountOnePrece > 3.0F && threeDaysCountOnePrece < 3.5F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + 3.5);
                        } else if (threeDaysCountOnePrece > 4.0F && threeDaysCountOnePrece < 4.5F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + 4.0);
                        } else if (threeDaysCountOnePrece > 5.0F && threeDaysCountOnePrece < 5.5F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + 5.0);
                        } else if (threeDaysCountOnePrece > 6.0F && threeDaysCountOnePrece < 6.5F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + 6.0);
                        } else if (threeDaysCountOnePrece > 7.0F && threeDaysCountOnePrece < 7.5F) {
                            updateLeave.setTotDaysForThreeDaysLate("" + 7.0);
                        }
                    } else {
                        updateLeave.setTotDaysForThreeDaysLate("0");
                    }
                    if (rs.getString("missing_outpunch_count") == null) {
                        updateLeave.setTotMissingOutPunch("0");
                    } else {
                        updateLeave.setTotMissingOutPunch(rs.getString("missing_outpunch_count"));

                    }

                    lastDateMonthYrStr = "SELECT (date_trunc('MONTH', ('" + curYear + curMon + "'||'01')::date) + INTERVAL '1 MONTH - 1 day')::DATE as monthlastdate, "
                            + "to_char(current_date, 'MM') as current_month ,to_char(current_date, 'yyyy-MM-dd') as currentdate";
                    lastDatepst = con.prepareStatement(lastDateMonthYrStr);
                    lastdateResult = lastDatepst.executeQuery();
                    if (lastdateResult.next()) {
                        currentMon = lastdateResult.getString("current_month");
                        if (!currentMon.equals(curMon)) {
                            lastDateStr = lastdateResult.getDate("monthlastdate");
                        } else {
                            lastDateStr = lastdateResult.getDate("currentdate");
                        }
                    }

                    absentStr = "select count(*),(select count(t1.*)fulldayabsent from(select * from(select tab5.*  from(select ABSENT,trim(to_char(ABSENT, 'day')) weekday from(select * from(   "
                            + "select (generate_series(cast(date_trunc('month', '" + lastDateStr + "'::date) as date), "
                            + "cast(date_trunc('day', '" + lastDateStr + "'::date) as date), '1 day'::interval))::date as ABSENT)tab   where tab.ABSENT not in   "
                            + "(SELECT CAST(INPUNCH_DATE as DATE) Check_Date   "
                            + "FROM emp_attendance_cmgi where  extract(year from attendance_date) = ? "
                            + " and extract(month from attendance_date) = ? and user_id=?  "
                            + "GROUP BY user_id,name, CAST(INPUNCH_DATE as DATE), to_char(INPUNCH_DATE, 'day') ))tab2 where tab2.ABSENT not in  "
                            + "(select CAST(fdate as DATE) from g_holiday where extract(year from fdate)=?))tab5 where  "
                            + "tab5.weekday!='sunday' )tab6  where tab6.ABSENT not in "
                            + "(select tab.sat::date from((SELECT  y,(CONCAT(y, '-" + curMon + "-01')::date +CONCAT(6 - EXTRACT(DOW FROM CONCAT(y,  '-" + curMon + "-01')::date) + 7, "
                            + "' days')::interval) sat FROM generate_series(2024, 2024) s(y) ORDER BY y DESC) UNION ALL "
                            + "(SELECT  y,(CONCAT(y,  '-" + curMon + "-01')::date + CONCAT(6 - EXTRACT(DOW FROM CONCAT(y,  '-" + curMon + "-01')::date) +21, "
                            + "' days')::interval)sat FROM generate_series(2024, 2024) s(y) ORDER BY y DESC))tab))t1), "
                            + "(select count(*)half_days_absent from (SELECT CAST(MAX(INPUNCH_DATE) - MIN(INPUNCH_DATE) AS Time) as working_hour "
                            + "FROM emp_attendance_cmgi where  extract(year from attendance_date) =?  "
                            + "and extract(month from attendance_date) =? and user_id=? "
                            + "GROUP BY user_id,name, CAST(INPUNCH_DATE as DATE)  "
                            + "order by name DESC, user_id ASC)tab8 where tab8.working_hour<='6:00:00' and tab8.working_hour>'00:00:20')";
                    pstmt1 = con.prepareStatement(absentStr);
                    pstmt1.setDouble(1, Double.parseDouble(curYear));
                    pstmt1.setDouble(2, Double.parseDouble(curMon));
                    pstmt1.setInt(3, rs.getInt("user_id"));
                    pstmt1.setDouble(4, Double.parseDouble(curYear));
                    pstmt1.setDouble(5, Double.parseDouble(curYear));
                    pstmt1.setDouble(6, Double.parseDouble(curMon));
                    pstmt1.setInt(7, rs.getInt("user_id"));

                    rs1 = pstmt1.executeQuery();

                    if (rs1.next()) {
                        totalFulldayAbsent = rs1.getInt("fulldayabsent");
                        totnoOfHalfdayAbsent = rs1.getInt("half_days_absent");

                    }
                    totHalfDayAbsent = totnoOfHalfdayAbsent * 0.5;
                    // finalAbsent = totalFulldayAbsent;
                    // System.out.println("===" + rs.getString("EMP_ID") + "==totalFulldayAbsent===" + totalFulldayAbsent + "====totalSaturday===" + totnoOfHalfdayAbsent);

                    finalAbsent = totalFulldayAbsent + totHalfDayAbsent;

                    updateLeave.setTotAbsentCurMonth(finalAbsent + "");
                    if (curMon.equals("01") && curYear.equals("2024")) {
                        if (rs.getString("user_id").equals("199201")) {
                            finalAbsent = 1;
                        } else if (rs.getString("user_id").equals("199202")) {
                            finalAbsent = 10;
                        } else if (rs.getString("user_id").equals("199203")) {
                            finalAbsent = 3.5;
                        } else if (rs.getString("user_id").equals("199204")) {
                            finalAbsent = 1;
                        } else if (rs.getString("user_id").equals("199205")) {
                            finalAbsent = 1;
                        } else if (rs.getString("user_id").equals("199206")) {
                            finalAbsent = 1;
                        } else if (rs.getString("user_id").equals("199207")) {
                            finalAbsent = 2;
                        } else if (rs.getString("user_id").equals("199208")) {
                            finalAbsent = 2;
                        } else if (rs.getString("user_id").equals("199209")) {
                            finalAbsent = 0.5;
                        } else if (rs.getString("user_id").equals("199210")) {
                            finalAbsent = 0;
                        } else if (rs.getString("user_id").equals("199211")) {
                            finalAbsent = 2;
                        } else if (rs.getString("user_id").equals("199213")) {
                            finalAbsent = 1.5;
                        } else if (rs.getString("user_id").equals("199214")) {
                            finalAbsent = 0;
                        } else if (rs.getString("user_id").equals("199215")) {
                            finalAbsent = 0.5;
                        } else if (rs.getString("user_id").equals("199216")) {
                            finalAbsent = 0.5;
                        } else if (rs.getString("user_id").equals("199217")) {
                            finalAbsent = 2;
                        } else if (rs.getString("user_id").equals("199218")) {
                            finalAbsent = 0;
                        } else if (rs.getString("user_id").equals("199219")) {
                            finalAbsent = 1;
                        } else if (rs.getString("user_id").equals("199220")) {
                            finalAbsent = 2.5;
                        } else if (rs.getString("user_id").equals("199221")) {
                            finalAbsent = 1;
                        } else if (rs.getString("user_id").equals("199222")) {
                            finalAbsent = 1;
                        } else if (rs.getString("user_id").equals("199223")) {
                            finalAbsent = 1;
                        } else if (rs.getString("user_id").equals("199224")) {
                            finalAbsent = 0;
                        } else if (rs.getString("user_id").equals("199225")) {
                            finalAbsent = 0;
                        } else if (rs.getString("user_id").equals("199226")) {
                            finalAbsent = 2;
                        } else if (rs.getString("user_id").equals("199228")) {
                            finalAbsent = 0.5;
                        }
                    }
                    if (curMon.equals("02") && curYear.equals("2024")) {
                        if (rs.getString("user_id").equals("199206")) {
                            finalAbsent = 2;
                        }
                    }
                    if (rs.getString("CL_AVAILABLE") != null) {

                        totBalanceAsOnDate = Double.parseDouble(rs.getString("CL_AVAILABLE")) - finalAbsent;
                    } else {
                        totBalanceAsOnDate = 0;
                    }
                    System.out.println("------totBalanceAsOnDate-------" + totBalanceAsOnDate + "----------------" + rs.getString("user_id") + "==========" + rs.getString("EMP_ID"));
                    updateLeave.setTotBalanceAsOnDate(totBalanceAsOnDate + "");

                    System.out.println("===curYear====" + curYear);

                    System.out.println("=========curMon==========" + curMon);
                    selEmpFromBal = "select count(*) cnt from emp_leave_balance where biometric_user_id=? and tol_id=? and year=? and month=?";
                    selpstmst = con.prepareStatement(selEmpFromBal);
                    selpstmst.setInt(1, Integer.parseInt(rs.getString("user_id")));
                    selpstmst.setString(2, "CL");
                    selpstmst.setString(3, curYear);

                    // selpstmst.setString(4, "03");
                    selpstmst.setString(4, curMon);

                    empBalResult = selpstmst.executeQuery();
                    if (empBalResult.next()) {

                        if (empBalResult.getInt("cnt") > 0) {

                            if (curMon.equals("01") && curYear.equals("2024")) {

                                updatesql = "update emp_leave_balance set balance_as_ondate=?, balance=?,available=? where emp_id=? and tol_id=? and year=? and month=?";

                                insertpst = con.prepareStatement(updatesql);

                                // System.out.println(rs.getString("EMP_ID") + "=====" + curYear + "===month====" + curMon + "==available====" + selRs.getString("available"));
                               
                                insertpst.setDouble(1, totBalanceAsOnDate);
                                 insertpst.setInt(2, 15);
                                insertpst.setDouble(3, 15);
                                System.out.println("============" + rs.getString("EMP_ID") + "============" + Integer.parseInt(rs.getString("user_id")));
                                insertpst.setString(4, rs.getString("EMP_ID"));
                                insertpst.setString(5, "CL");
                                insertpst.setString(6, curYear);
                                //insertpst.setString(5, "01");
                                insertpst.setString(7, curMon);
                                
                                insertpst.executeUpdate();

                            } else {

                                selSql = "select balance_as_ondate from emp_leave_balance where  biometric_user_id=? and tol_id=? and year=? and month=?";
                                selPst = con.prepareStatement(selSql);
                                //System.out.println("===========******************======="+rs.getString("EMP_ID")+"^^^^^"+"0" + (Integer.parseInt(curMon) - 1));
                                selPst.setInt(1, Integer.parseInt(rs.getString("user_id")));
                                selPst.setString(2, "CL");
                                selPst.setString(3, curYear);

                                if ((Integer.parseInt(curMon) - 1) < 10) {
                                    selPst.setString(4, "0" + (Integer.parseInt(curMon) - 1));
                                } else {
                                    selPst.setString(4, String.valueOf((Integer.parseInt(curMon) - 1)));
                                }
                                //selPst.setString(4, "10");
                                // selPst.setString(4, "01");
                                selRs = selPst.executeQuery();
                                if (selRs.next()) {

                                    updatesql = "update emp_leave_balance set balance=?,available=?,balance_as_ondate=? where emp_id=? and tol_id=? and year=? and month=?";

                                    insertpst = con.prepareStatement(updatesql);
                                     insertpst.setInt(1, 15);
                                    // System.out.println(rs.getString("EMP_ID") + "=====" + curYear + "===month====" + curMon + "==available====" + selRs.getString("available"));
                                    if (selRs.getString("balance_as_ondate") != null && !selRs.getString("balance_as_ondate").equals("")) {
                                        insertpst.setDouble(2, Double.parseDouble(selRs.getString("balance_as_ondate")));
                                    } else {
                                        insertpst.setDouble(2, 0);
                                    }
                                    insertpst.setDouble(3, totBalanceAsOnDate);
                                    System.out.println("============" + rs.getString("EMP_ID") + "============" + Integer.parseInt(rs.getString("user_id")));
                                    insertpst.setString(4, rs.getString("EMP_ID"));
                                    insertpst.setString(5, "CL");
                                    insertpst.setString(6, curYear);
                                    //insertpst.setString(5, "01");
                                    insertpst.setString(7, curMon);
                                    insertpst.executeUpdate();

                                }
                            }
//                        else {
//                            // System.out.println("==totBalanceAsOnDate===="+totBalanceAsOnDate);
//                            // System.out.println("======" + rs.getString("EMP_ID") + "======" + curYear);
//
//                            if (curMon.equals("01") && curYear.equals("2024")) {
//                                updateEmpId = "select emp_id from emp_mast where employment_type_id=?";
//                                selEmpId = con.prepareStatement(updateEmpId);
//                                selEmpId.setInt(1, Integer.parseInt(rs.getString("user_id")));
//
//                                rsEmpId = selEmpId.executeQuery();
//                                if (rsEmpId.next()) {
//                                    insertsql = "insert into emp_leave_balance(year,emp_id,tol_id,balance,available,month,balance_as_ondate,biometric_user_id) values(?,?,?,?,?,?,?,?)";
//                                    insertpst = con.prepareStatement(insertsql);
//                                    insertpst.setString(1, curYear);
//                                    if (rsEmpId.getString("emp_id") != null && !rsEmpId.getString("emp_id").equals("")) {
//                                        insertpst.setString(2, rsEmpId.getString("emp_id"));
//                                    } else {
//                                        insertpst.setString(2, null);
//                                    }
//                                    insertpst.setString(3, "CL");
//                                    insertpst.setInt(4, 25);
//
//                                   
//                                    insertpst.setDouble(5, 25);
//
//                                  
//                                    insertpst.setString(6, curMon);
//                                    insertpst.setDouble(7, totBalanceAsOnDate);
//                                    insertpst.setDouble(8, Integer.parseInt(rs.getString("user_id")));
//                                    insertpst.executeUpdate();
//
//                                }
//                            } /*else {
//                             updateEmpId = "select emp_id from emp_mast where employment_type_id=?";
//                             selEmpId = con.prepareStatement(updateEmpId);
//                             selEmpId.setInt(1, Integer.parseInt(rs.getString("user_id")));
//                             rsEmpId = selEmpId.executeQuery();
//                             if (rsEmpId.next()) {
//                             insertsql = "insert into emp_leave_balance(year,emp_id,tol_id,balance,available,month,biometric_user_id) values(?,?,?,?,?,?,?)";
//                             insertpst = con.prepareStatement(insertsql);
//                             insertpst.setString(1, curYear);
//                             if (rsEmpId.getString("emp_id") != null && !rsEmpId.getString("emp_id").equals("")) {
//                             insertpst.setString(2, rsEmpId.getString("emp_id"));
//                             } else {
//                             insertpst.setString(2, null);
//                             }
//                             insertpst.setString(3, "CL");
//                             insertpst.setInt(4, 25);
//
//                             insertpst.setDouble(5, totBalanceAsOnDate);
//                             //  insertpst.setDouble(5, 25);
//
//                             // insertpst.setDouble(5, 25);
//                             // insertpst.setString(6, "12");
//                             // insertpst.setString(6, "03");
//                             insertpst.setString(6, curMon);
//                             insertpst.setDouble(7, Integer.parseInt(rs.getString("user_id")));
//                             // insertpst.setDouble(7, (25 - finalAbsent));
//                             insertpst.executeUpdate();
//
//                             }
//                             }*/
//
                        }else{
                             updateEmpId = "select emp_id from emp_mast where employment_type_id=?";
                             selEmpId = con.prepareStatement(updateEmpId);
                             selEmpId.setInt(1, Integer.parseInt(rs.getString("user_id")));
                             rsEmpId = selEmpId.executeQuery();
                             if (rsEmpId.next()) {
                             insertsql = "insert into emp_leave_balance(year,emp_id,tol_id,balance,available,month,biometric_user_id) values(?,?,?,?,?,?,?)";
                             insertpst = con.prepareStatement(insertsql);
                             insertpst.setString(1, curYear);
                             if (rsEmpId.getString("emp_id") != null && !rsEmpId.getString("emp_id").equals("")) {
                             insertpst.setString(2, rsEmpId.getString("emp_id"));
                             } else {
                             insertpst.setString(2, null);
                             }
                             insertpst.setString(3, "CL");
                             insertpst.setInt(4, 15);

                             insertpst.setDouble(5, totBalanceAsOnDate);
                             //  insertpst.setDouble(5, 25);

                             // insertpst.setDouble(5, 25);
                             // insertpst.setString(6, "12");
                             // insertpst.setString(6, "03");
                             insertpst.setString(6, curMon);
                             insertpst.setDouble(7, Integer.parseInt(rs.getString("user_id")));
                             // insertpst.setDouble(7, (25 - finalAbsent));
                             insertpst.executeUpdate();
                             }
                        }
                    }

                    updateLeave.setTotalClAvail(rs.getString("CL_AVAILABLE"));
                    empList.add(updateLeave);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(lastdateResult, lastDatepst);
            DataBaseFunctions.closeSqlObjects(empBalResult, insertpst);
            DataBaseFunctions.closeSqlObjects(rs1, pstmt1);
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(selRs, selPst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empList;
    }

    @Override
    public ArrayList getMonthlyWorkingHour(String year, String month, String userId
    ) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        UpdateLeave updateLeave = null;
        ArrayList workingHourList = new ArrayList();
        try {
            con = dataSource.getConnection();
            if (year != null && !year.equals("") && month != null && !month.equals("")) {
                pstmt = con.prepareStatement("SELECT user_id, name, CAST(INPUNCH_DATE as DATE) Check_Date,   "
                        + "to_char(INPUNCH_DATE, 'day') days,  "
                        + "       MIN(CAST(INPUNCH_DATE as Time)) TimeIN,   "
                        + "       MAX(Cast(OUTPUNCH_DATE as Time)) TimeOUT,  "
                        + "       CAST(MAX(INPUNCH_DATE) - MIN(INPUNCH_DATE) AS Time) As hour  "
                        + "FROM emp_attendance_cmgi where  extract(year from attendance_date) =?  "
                        + "and extract(month from attendance_date) = ? and user_id=?  "
                        + "GROUP BY user_id,name, CAST(INPUNCH_DATE as DATE), to_char(INPUNCH_DATE, 'day')  "
                        + "order by name DESC, check_date ASC, user_id ASC");
                pstmt.setDouble(1, Double.parseDouble(year));
                pstmt.setDouble(2, Double.parseDouble(month));
                pstmt.setInt(3, Integer.parseInt(userId));
            } else {
                pstmt = con.prepareStatement("SELECT user_id, name, CAST(INPUNCH_DATE as DATE) Check_Date,   "
                        + "to_char(INPUNCH_DATE, 'day') days,  "
                        + "       MIN(CAST(INPUNCH_DATE as Time)) TimeIN,   "
                        + "       MAX(Cast(OUTPUNCH_DATE as Time)) TimeOUT,  "
                        + "       CAST(MAX(INPUNCH_DATE) - MIN(INPUNCH_DATE) AS Time) As hour  "
                        + "FROM emp_attendance_cmgi where  extract(year from attendance_date) =  (SELECT EXTRACT(YEAR FROM cast(date_trunc('month', current_date) as date)))   "
                        + "and extract(month from attendance_date) = (SELECT EXTRACT(MONTH FROM cast(date_trunc('month', current_date) as date))) and user_id=?  "
                        + "GROUP BY user_id,name, CAST(INPUNCH_DATE as DATE), to_char(INPUNCH_DATE, 'day')  "
                        + "order by name DESC, check_date ASC, user_id ASC");
                pstmt.setInt(1, Integer.parseInt(userId));
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                updateLeave = new UpdateLeave();
                updateLeave.setWorkingDate(rs.getString("Check_Date"));
                updateLeave.setWorkingDay(rs.getString("days"));
                if (rs.getString("TimeIN").equals(rs.getString("TimeOUT"))) {
                    updateLeave.setInpunchTime(rs.getString("TimeIN"));
                    updateLeave.setOutpunchTime("");
                } else {
                    updateLeave.setInpunchTime(rs.getString("TimeIN"));
                    updateLeave.setOutpunchTime(rs.getString("TimeOUT"));
                }
                updateLeave.setDailyWorkingHour(rs.getString("hour"));
                workingHourList.add(updateLeave);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return workingHourList;
    }

    @Override
    public ArrayList getEmpWiseLeaveList(String empId, String year, String month
    ) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        UpdateLeave updateLeave = null;
        ArrayList leaveList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT DISTINCT G_LEAVE.TOL_ID,TOL,balance,available FROM G_LEAVE "
                    + "left outer join (select tol_id,balance,available from emp_leave_balance "
                    + "where emp_id=? and year=? and month=?)tab on "
                    + "tab.tol_id=G_LEAVE.tol_id WHERE IF_NOT_APP != 'Y' ORDER BY TOL_ID");
            pstmt.setString(1, empId);
            pstmt.setString(2, year);
            pstmt.setString(3, month);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                updateLeave = new UpdateLeave();
                updateLeave.setTolId(rs.getString("TOL_ID"));
                updateLeave.setLeaveType(rs.getString("TOL"));
                updateLeave.setTotalBalance(rs.getString("balance"));
                updateLeave.setTotalAvailable(rs.getString("available"));
                leaveList.add(updateLeave);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return leaveList;
    }
}
