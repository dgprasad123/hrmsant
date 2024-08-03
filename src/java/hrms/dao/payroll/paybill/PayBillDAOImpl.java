/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.paybill;

import hrms.common.AqFunctionalities;
import hrms.common.DataBaseFunctions;
import hrms.model.employee.PayComponent;
import hrms.model.payroll.aqdtls.AqDtlsModel;
import hrms.model.payroll.aqmast.AqmastModel;
import hrms.model.payroll.billbrowser.SectionDtlSPCWiseEmp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Manas
 */
public class PayBillDAOImpl implements PayBillDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public PayComponent getEmployeePayComponent(SectionDtlSPCWiseEmp sdswe, Date startDate, Date endDate, int daysInMonth) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PayComponent paycomp = null;
        try {
            paycomp = new PayComponent();
            paycomp.setBasic(sdswe.getCurBasicSalary());
            paycomp.setGp(sdswe.getGp());
            con = dataSource.getConnection();

            int basicinEmpMast = 0;
            int gpinEmpMast = 0;
            int payCommission = 6;
            
            pstmt = con.prepareStatement("SELECT CUR_BASIC_SALARY,GP,pay_commission FROM EMP_MAST WHERE EMP_ID=?");
            pstmt.setString(1, sdswe.getEmpid());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                basicinEmpMast = rs.getInt("CUR_BASIC_SALARY");
                gpinEmpMast = rs.getInt("GP");
                payCommission = rs.getInt("pay_commission");
            }
            
            if (payCommission == 5) {
                pstmt = con.prepareStatement("select fixedvalue from UPDATE_AD_INFO where REF_ad_CODE='71' and fixedvalue >0 and updation_ref_code=? ");
                pstmt.setString(1, sdswe.getEmpid());
                rs1 = pstmt.executeQuery();
                if (rs1.next()) {
                    paycomp.setDp(rs1.getInt("fixedvalue"));
                } else {
                    paycomp.setDp(0);
                }
                paycomp.setBasic(basicinEmpMast);

                paycomp.setIspayrevised("N");
                paycomp.setPaycommission(payCommission);
            } else if (payCommission == 6) {
                paycomp.setBasic(basicinEmpMast);
                paycomp.setGp(gpinEmpMast);
                paycomp.setIspayrevised("N");
                paycomp.setPaycommission(payCommission);
            } else if (payCommission == 7) {
                paycomp.setBasic(basicinEmpMast);
                paycomp.setGp(0);
                paycomp.setIspayrevised("Y");
                paycomp.setPaycommission(payCommission);
            } else {
                paycomp.setBasic(basicinEmpMast);
                paycomp.setGp(gpinEmpMast);
                paycomp.setIspayrevised("N");
                paycomp.setPaycommission(6);
            }

            /*

             String sDate1 = "01/01/2016";
             Date payRevDate = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);

             pstmt = con.prepareStatement("SELECT CUR_BASIC_SALARY,GP FROM EMP_MAST WHERE EMP_ID=?");
             pstmt.setString(1, sdswe.getEmpid());
             rs = pstmt.executeQuery();

             if (rs.next()) {
             basicinEmpMast = rs.getInt("CUR_BASIC_SALARY");
             gpinEmpMast = rs.getInt("GP");
             }
            

             pstmt = con.prepareStatement("SELECT MAX(REVISED_BASIC) AS v_temp_basic FROM emp_pay_revised_increment_2016 WHERE EMP_ID=?");
             pstmt.setString(1, sdswe.getEmpid());
             rs = pstmt.executeQuery();
             if (rs.next()) {
             v_temp_basic = rs.getInt("v_temp_basic");
             }
             pstmt = con.prepareStatement("SELECT IS_APPROVED_CHECKING_AUTH,payrev_fitted_amount FROM PAY_REVISION_OPTION WHERE EMP_ID=?");
             pstmt.setString(1, sdswe.getEmpid());
             rs = pstmt.executeQuery();
             if (rs.next()) {
             String isapproved = rs.getString("IS_APPROVED_CHECKING_AUTH");
             if (isapproved != null && isapproved.equalsIgnoreCase("Y")) {
             String revisedBasic = rs.getString("payrev_fitted_amount");
             if (isStringInt(revisedBasic)) {
             paycomp.setBasic(Integer.parseInt(revisedBasic));
             if (Integer.parseInt(revisedBasic) < v_temp_basic) {
             paycomp.setBasic(v_temp_basic);
             }
             if (basicinEmpMast > paycomp.getBasic()) {
             paycomp.setBasic(basicinEmpMast);
             }
             paycomp.setGp(0);
             paycomp.setIspayrevised("Y");
             } else {
             paycomp.setIspayrevised("N");
             }
             } else {
             paycomp.setIspayrevised("N");
             }
             } else {
             paycomp.setBasic(basicinEmpMast);
             paycomp.setGp(gpinEmpMast);
             paycomp.setIspayrevised("N");
             }
             Date payDate = null;
             int prevBasicSalary = 0;
             int prevGP = 0;

             payDate = sdswe.getPayDate();

             prevBasicSalary = sdswe.getPrevBasicSalary();
             prevGP = sdswe.getPrevGp();

             if (paycomp.getIspayrevised() == null || paycomp.getIspayrevised().equalsIgnoreCase("N")) {
             if (sdswe.getDoegov() != null && sdswe.getDoegov().compareTo(payRevDate) > 0) {
             paycomp.setBasic(basicinEmpMast);
             paycomp.setGp(gpinEmpMast);
             paycomp.setIspayrevised("Y");
             }
             }
            
             */
            /**
             * **************
             */
            /*
             if (sdswe.getDoegov() != null && sdswe.getDoegov().compareTo(payRevDate) > 0) {                
             paycomp.setBasic(basicinEmpMast);
             paycomp.setGp(gpinEmpMast);
             paycomp.setIspayrevised("Y");
             } else {
             pstmt = con.prepareStatement("SELECT MAX(REVISED_BASIC) AS v_temp_basic FROM emp_pay_revised_increment_2016 WHERE EMP_ID=?");
             pstmt.setString(1, sdswe.getEmpid());
             rs = pstmt.executeQuery();
             if (rs.next()) {
             v_temp_basic = rs.getInt("v_temp_basic");
             }
             pstmt = con.prepareStatement("SELECT IS_APPROVED_CHECKING_AUTH,payrev_fitted_amount FROM PAY_REVISION_OPTION WHERE EMP_ID=?");
             pstmt.setString(1, sdswe.getEmpid());
             rs = pstmt.executeQuery();
             if (rs.next()) {
             String isapproved = rs.getString("IS_APPROVED_CHECKING_AUTH");
             if (isapproved != null && isapproved.equalsIgnoreCase("Y")) {
             String revisedBasic = rs.getString("payrev_fitted_amount");
             if (isStringInt(revisedBasic)) {
             paycomp.setBasic(Integer.parseInt(revisedBasic));
             if (Integer.parseInt(revisedBasic) < v_temp_basic) {
             paycomp.setBasic(v_temp_basic);
             }
             if (basicinEmpMast > paycomp.getBasic()) {
             paycomp.setBasic(basicinEmpMast);
             }
             paycomp.setGp(0);
             paycomp.setIspayrevised("Y");
             } else {
             paycomp.setIspayrevised("N");
             }
             } else {
             paycomp.setIspayrevised("N");
             }
             } else {
             paycomp.setBasic(basicinEmpMast);
             paycomp.setGp(gpinEmpMast);
             paycomp.setIspayrevised("N");
             }
             Date payDate = null;
             int prevBasicSalary = 0;
             int prevGP = 0;

             payDate = sdswe.getPayDate();

             prevBasicSalary = sdswe.getPrevBasicSalary();
             prevGP = sdswe.getPrevGp();
             }
             */

            /*Calculate Is Employee get his increment in this month*/
            /*
             if (payDate != null && payDate.compareTo(startDate) > 0 && payDate.compareTo(endDate) < 0) {
             long diff = payDate.getTime() - startDate.getTime();
             int prevPayDays = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
             if (prevBasicSalary != 0 && paycomp.getBasic() != prevBasicSalary) {
             int tPrevBasic = (prevBasicSalary / daysInMonth) * prevPayDays;
             int tCurBasic = ((paycomp.getBasic() / daysInMonth) * (daysInMonth - prevPayDays)) + tPrevBasic;
             paycomp.setBasic(tCurBasic);
             }
             if (prevGP != 0 && paycomp.getGp() != prevGP) {
             int tPrevGp = (prevGP / daysInMonth) * prevPayDays;
             int tCurGp = ((paycomp.getGp() / daysInMonth) * (daysInMonth - prevPayDays)) + tPrevGp;
             paycomp.setGp(tCurGp);
             }
             }*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(rs1, pstmt1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return paycomp;
    }

    @Override
    public HashMap getPayWorkDays(SectionDtlSPCWiseEmp sdswe, int month, int year) {
        HashMap<String, Integer> payworkday = new HashMap<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT DAYS_WORKED FROM EMP_ATTENDANCE WHERE EMP_ID=? AND AT_MONTH=? AND AT_YEAR=?");
            pstmt.setString(1, sdswe.getEmpid());
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            rs = pstmt.executeQuery();
            int v_workday = 0;
            if (rs.next()) {
                v_workday = rs.getInt("DAYS_WORKED");
            }
            payworkday.put("workday", v_workday);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return payworkday;
    }

    @Override
    public int getPayDaysContractual(SectionDtlSPCWiseEmp sdswe, int month, int year, int daysInMonth) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int v_payday = daysInMonth;
        if (sdswe.getJobtypeid() == 1) {
            v_payday = 0;
            try {
                con = dataSource.getConnection();
                pstmt = con.prepareStatement("SELECT DAYS_WORKED FROM EMP_ATTENDANCE WHERE EMP_ID=? AND AT_MONTH=? AND AT_YEAR=?");
                pstmt.setString(1, sdswe.getEmpid());
                pstmt.setInt(2, month);
                pstmt.setInt(3, year);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    v_payday = rs.getInt("DAYS_WORKED");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                DataBaseFunctions.closeSqlObjects(rs, pstmt);
                DataBaseFunctions.closeSqlObjects(con);
            }
        }
        return v_payday;
    }

    @Override
    public HashMap getPayWorkDays(SectionDtlSPCWiseEmp sdswe, Date monstartDate, Date monendDate, int v_aqday) {
        HashMap<String, Integer> payworkday = new HashMap<>();
        int v_payday = 0;
        int v_workday = 0;
        payworkday.put("payday", v_payday);
        payworkday.put("workday", v_workday);
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            /*Get month and year from date*/
            Calendar tcal = Calendar.getInstance();
            tcal.setTime(monstartDate);
            int year = tcal.get(Calendar.YEAR);
            int month = tcal.get(Calendar.MONTH);
            /*Get month and year from date*/

            int jday = 0;
            int v_rday = 0;
            int totdays = 0;
            int v_fday = 0;
            int v_jday = 0;
            int v_tday = 0;
            int v_fday1 = 0;
            int v_tday1 = 0;
            Calendar cal = Calendar.getInstance();
            Date v_ifjoinedthismonth = null;
            String v_jtime = null;
            Date v_ifrelievedthismonth = null;
            String v_rtime = null;

            long diff = monendDate.getTime() - monstartDate.getTime();
            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            totdays = (int) days + 1;
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT JOIN_DATE, JOIN_TIME FROM EMP_JOIN WHERE SPC=? AND  EMP_ID=? AND date_part('year', JOIN_DATE) = ? and date_part('month', JOIN_DATE)=?");
            pstmt.setString(1, sdswe.getSpc());
            pstmt.setString(2, sdswe.getEmpid());
            pstmt.setInt(3, year);
            pstmt.setInt(4, month + 1);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getDate("JOIN_DATE") != null) {
                    v_ifjoinedthismonth = rs.getDate("JOIN_DATE");
                    v_jtime = rs.getString("JOIN_TIME");
                    cal.setTime(v_ifjoinedthismonth);
                    v_jday = cal.get(Calendar.DAY_OF_MONTH);
                }
            }
            pstmt = con.prepareStatement("SELECT RLV_DATE,RLV_TIME FROM EMP_RELIEVE WHERE SPC = ? AND EMP_ID=? AND date_part('year', RLV_DATE) = ? and date_part('month', RLV_DATE)=?");
            pstmt.setString(1, sdswe.getSpc());
            pstmt.setString(2, sdswe.getEmpid());
            pstmt.setInt(3, year);
            pstmt.setInt(4, month);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getDate("RLV_DATE") != null) {
                    v_ifrelievedthismonth = rs.getDate("RLV_DATE");
                    v_rtime = rs.getString("RLV_TIME");
                    cal.setTime(v_ifrelievedthismonth);
                    v_rday = cal.get(Calendar.DAY_OF_MONTH);
                }
            }
            /**
             * ***********************IF JOINED THIS MONTH AND RELIEVED THIS
             * MONTH FROM SAME POST**************
             */
            if (v_ifjoinedthismonth != null && v_ifrelievedthismonth != null) {
                if (v_rday > v_jday) {
                    v_fday = v_jday;
                    if (v_rtime.equalsIgnoreCase("FN")) {
                        totdays = v_rday - v_jday;
                        v_tday = v_rday - 1;
                    } else {
                        totdays = v_rday - v_jday + 1;
                        v_tday = v_rday;
                    }
                } else {
                    v_fday = 1;
                    v_fday1 = v_jday;
                    v_tday1 = v_aqday;
                    if (v_rtime.equalsIgnoreCase("FN")) {
                        totdays = (v_aqday - v_jday) + v_rday;
                        v_tday = v_rday - 1;
                    } else {
                        totdays = (v_aqday - v_jday) + 1 + v_rday;
                        v_tday = v_rday;
                    }
                }
            }
            /**
             * *IF JOINED THIS MONTH AND RELIEVED THIS MONTH FROM SAME POST**
             */
            /**
             * *IF JOINED THIS MONTH AND NOT RELIEVED THIS MONTH FROM SAME
             * POST*
             */
            if (v_ifjoinedthismonth != null && v_ifrelievedthismonth == null) {
                totdays = (v_aqday - v_jday) + 1;
                v_fday = v_jday;
                v_tday = v_aqday;
            }

            /**
             * IF JOINED THIS MONTH AND NOT RELIEVED THIS MONTH FROM SAME POST*
             */
            /**
             * ***********************IF RELIEVED THIS MONTH AND NOT JOINED
             * THIS MONTH IN SAME POST**************
             */
            if (v_ifjoinedthismonth == null && v_ifrelievedthismonth != null) {
                v_fday = 1;
                if (v_rtime.equalsIgnoreCase("FN")) {
                    totdays = v_rday - 1;
                    v_tday = v_rday - 1;
                } else {
                    totdays = v_rday;
                    v_tday = v_rday;
                }
            }
            /**
             * ****IF RELIEVED THIS MONTH AND NOT JOINED THIS MONTH IN SAME
             * POST***
             */
            /**
             * NOT JOINED THIS MONTH AND NOT RELIEVED THIS MONTH
             */
            if (v_ifjoinedthismonth == null && v_ifrelievedthismonth != null) {
                totdays = v_aqday;
                v_fday = 1;
                v_tday = v_aqday;
            }

            /**
             * NOT JOINED THIS MONTH AND NOT RELIEVED THIS MONTH
             */
            /**
             * CALCULATING TOTAL DAYS ABSENT*
             */
            int v_totalAbsent = 0;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strmonstartDate = simpleDateFormat.format(monstartDate);
            String strmonendDate = simpleDateFormat.format(monendDate);
            pstmt = con.prepareStatement("SELECT ABS_FROM,ABS_TO FROM EMP_ABSENTEE where EMP_ID = ? and (ABS_FROM BETWEEN to_date(?, 'YYYY-MM-DD') AND to_date(?, 'YYYY-MM-DD') or ABS_TO BETWEEN to_date(?, 'YYYY-MM-DD') AND to_date(?, 'YYYY-MM-DD'))");
            pstmt.setString(1, sdswe.getEmpid());
            pstmt.setString(2, strmonstartDate);
            pstmt.setString(3, strmonendDate);
            pstmt.setString(4, strmonstartDate);
            pstmt.setString(5, strmonendDate);
            rs = pstmt.executeQuery();
            Date tmonstartDate = new SimpleDateFormat("yyyy-MM-dd").parse(strmonstartDate);
            Date tmonendDate = new SimpleDateFormat("yyyy-MM-dd").parse(strmonendDate);
            while (rs.next()) {
                Date absfrom = rs.getDate("ABS_FROM");
                Date absto = rs.getDate("ABS_TO");
                if (absfrom.compareTo(tmonstartDate) >= 0 && absto.compareTo(tmonendDate) <= 0) {//Absent from and to with in this month
                    diff = absto.getTime() - absfrom.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    v_totalAbsent = v_totalAbsent + (int) days;
                    v_totalAbsent++;
                } else if (absfrom.compareTo(tmonstartDate) < 0 && absto.compareTo(tmonendDate) <= 0) {//absent from beftore this month but absent to in this month
                    diff = absto.getTime() - monstartDate.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    v_totalAbsent = v_totalAbsent + (int) days;
                } else if (absfrom.compareTo(tmonstartDate) >= 0 && absto.compareTo(tmonendDate) > 0) {//absent from in this month but absent to after this month
                    diff = monendDate.getTime() - absfrom.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);
                    v_totalAbsent = v_totalAbsent + (int) days;
                } else if (absfrom.compareTo(tmonstartDate) < 0 && absto.compareTo(tmonendDate) > 0) {//absent from beftore this month but absent to after this month
                    diff = monstartDate.getTime() - monendDate.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);
                    v_totalAbsent = v_totalAbsent + (int) days;
                }
            }
            /**
             * CALCULATING TOTAL DAYS ABSENT*
             */
            /**
             * CALCULATING TOTAL DAYS SANCTIONED*
             */

            int v_sanctionavailed = 0;
            /*
             pstmt = con.prepareStatement("SELECT FDATE,TDATE FROM(SELECT FDATE,TDATE FROM EMP_LEAVE WHERE EMP_ID = ? and IF_LONGTERM != 'Y' and AND LSOT_ID='01' and (ABS_FROM BETWEEN ? AND ? or ABS_TO BETWEEN ? AND ?))EMP_LEAVE INNER JOIN "
             + "(SELECT * FROM EMP_NOTIFICATION WHERE EMP_ID = ? AND NOT_TYPE='LEAVE' AND EMP_NOTIFICATION.LINK_ID IS NULL AND STATUS='SANCTIONED AND AVAILED') EMP_NOTIFICATION ON EMP_LEAVE.NOT_ID=EMP_NOTIFICATION.NOT_ID)");
             pstmt.setString(1, sdswe.getEmpid());
             pstmt.setDate(2, new java.sql.Date(monstartDate.getTime()));
             pstmt.setDate(3, new java.sql.Date(monendDate.getTime()));
             pstmt.setDate(4, new java.sql.Date(monstartDate.getTime()));
             pstmt.setDate(5, new java.sql.Date(monendDate.getTime()));
             pstmt.setString(6, sdswe.getEmpid());
             rs = pstmt.executeQuery();
             while (rs.next()) {
             Date sancfrom = rs.getDate("FDATE");
             Date sancto = rs.getDate("TDATE");
             if (sancfrom.compareTo(monstartDate) >= 0 && sancto.compareTo(monendDate) <= 0) {//Absent from and to with in this month
             long diff = sancto.getTime() - sancfrom.getTime();
             long days = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);
             v_sanctionavailed = v_sanctionavailed + (int) days;
             } else if (sancfrom.compareTo(monstartDate) < 0 && sancto.compareTo(monendDate) <= 0) {//absent from beftore this month but absent to in this month
             long diff = sancto.getTime() - monstartDate.getTime();
             long days = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);
             v_sanctionavailed = v_sanctionavailed + (int) days;
             } else if (sancfrom.compareTo(monstartDate) >= 0 && sancto.compareTo(monendDate) > 0) {//absent from in this month but absent to after this month
             long diff = monendDate.getTime() - sancfrom.getTime();
             long days = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);
             v_sanctionavailed = v_sanctionavailed + (int) days;
             } else if (sancfrom.compareTo(monstartDate) < 0 && sancto.compareTo(monendDate) > 0) {//absent from beftore this month but absent to after this month
             long diff = monstartDate.getTime() - monendDate.getTime();
             long days = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);
             v_sanctionavailed = v_sanctionavailed + (int) days;
             }
             }*/
            /**
             * CALCULATING TOTAL DAYS SANCTIONED*
             */
            /* CALCULATING JT TIME PERIOD */
            int v_jtavailed = 0;
            pstmt = con.prepareStatement("SELECT JOIN_TIME_FROM,JOIN_TIME_TO FROM EMP_LEAVE WHERE EMP_ID = ? AND (JOIN_TIME_FROM <= ? AND JOIN_TIME_TO >= ?)");
            pstmt.setString(1, sdswe.getEmpid());
            pstmt.setDate(2, new java.sql.Date(monstartDate.getTime()));
            pstmt.setDate(3, new java.sql.Date(monendDate.getTime()));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Date joiningfrom = rs.getDate("FDATE");
                Date joiningto = rs.getDate("TDATE");
                if (joiningfrom.compareTo(monstartDate) >= 0 && joiningto.compareTo(monendDate) <= 0) {//Absent from and to with in this month
                    diff = joiningto.getTime() - joiningfrom.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);
                    v_jtavailed = v_jtavailed + (int) days;
                } else if (joiningfrom.compareTo(monstartDate) < 0 && joiningto.compareTo(monendDate) <= 0) {//absent from beftore this month but absent to in this month
                    diff = joiningto.getTime() - monstartDate.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);
                    v_jtavailed = v_jtavailed + (int) days;
                } else if (joiningfrom.compareTo(monstartDate) >= 0 && joiningto.compareTo(monendDate) > 0) {//absent from in this month but absent to after this month
                    diff = monendDate.getTime() - joiningfrom.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);
                    v_jtavailed = v_jtavailed + (int) days;
                } else if (joiningfrom.compareTo(monstartDate) < 0 && joiningto.compareTo(monendDate) > 0) {//absent from beftore this month but absent to after this month
                    diff = monstartDate.getTime() - monendDate.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);
                    v_jtavailed = v_jtavailed + (int) days;
                }
            }
            /* CALCULATING JT TIME PERIOD */
            v_payday = totdays - (v_totalAbsent - v_sanctionavailed);
            v_workday = totdays - v_totalAbsent;
            if (v_payday < 0) {
                v_payday = 0;
            }
            if (v_workday < 0) {
                v_workday = 0;
            }
            /*payworkday.put("payday", v_payday);
             payworkday.put("workday", v_workday);
             */
            payworkday.put("payday", v_payday);
            payworkday.put("workday", v_workday);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return payworkday;
    }

    public HashMap getPayWorkDays(AqmastModel aqMastModel, Date monstartDate, Date monendDate, int v_aqday) {
        HashMap<String, Integer> payworkday = new HashMap<String, Integer>();
        int v_payday = 0;
        int v_workday = 0;
        payworkday.put("payday", v_payday);
        payworkday.put("workday", v_workday);
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            /*Get month and year from date*/
            Calendar tcal = Calendar.getInstance();
            tcal.setTime(monstartDate);
            int year = tcal.get(Calendar.YEAR);
            int month = tcal.get(Calendar.MONTH);
            /*Get month and year from date*/

            int jday = 0;
            int v_rday = 0;
            int totdays = 0;
            int v_fday = 0;
            int v_jday = 0;
            int v_tday = 0;
            int v_fday1 = 0;
            int v_tday1 = 0;
            Calendar cal = Calendar.getInstance();
            Date v_ifjoinedthismonth = null;
            String v_jtime = null;
            Date v_ifrelievedthismonth = null;
            String v_rtime = null;

            long diff = monendDate.getTime() - monstartDate.getTime();
            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            totdays = (int) days + 1;
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT JOIN_DATE, JOIN_TIME FROM EMP_JOIN WHERE SPC=? AND  EMP_ID=? AND date_part('year', JOIN_DATE) = ? and date_part('month', JOIN_DATE)=?");
            pstmt.setString(1, aqMastModel.getCurSpc());
            pstmt.setString(2, aqMastModel.getEmpCode());
            pstmt.setInt(3, year);
            pstmt.setInt(4, month + 1);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getDate("JOIN_DATE") != null) {
                    v_ifjoinedthismonth = rs.getDate("JOIN_DATE");
                    v_jtime = rs.getString("JOIN_TIME");
                    cal.setTime(v_ifjoinedthismonth);
                    v_jday = cal.get(Calendar.DAY_OF_MONTH);
                }
            }
            pstmt = con.prepareStatement("SELECT RLV_DATE,RLV_TIME FROM EMP_RELIEVE WHERE SPC = ? AND EMP_ID=? AND date_part('year', RLV_DATE) = ? and date_part('month', RLV_DATE)=?");
            pstmt.setString(1, aqMastModel.getCurSpc());
            pstmt.setString(2, aqMastModel.getEmpCode());
            pstmt.setInt(3, year);
            pstmt.setInt(4, month + 1);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getDate("RLV_DATE") != null) {
                    v_ifrelievedthismonth = rs.getDate("RLV_DATE");
                    v_rtime = rs.getString("RLV_TIME");
                    cal.setTime(v_ifrelievedthismonth);
                    v_rday = cal.get(Calendar.DAY_OF_MONTH);
                }
            }
            /**
             * ***********************IF JOINED THIS MONTH AND RELIEVED THIS
             * MONTH FROM SAME POST**************
             */
            if (v_ifjoinedthismonth != null && v_ifrelievedthismonth != null) {
                if (v_rday > v_jday) {
                    v_fday = v_jday;
                    if (v_rtime.equalsIgnoreCase("FN")) {
                        totdays = v_rday - v_jday;
                        v_tday = v_rday - 1;
                    } else {
                        totdays = v_rday - v_jday + 1;
                        v_tday = v_rday;
                    }
                } else {
                    v_fday = 1;
                    v_fday1 = v_jday;
                    v_tday1 = v_aqday;
                    if (v_rtime.equalsIgnoreCase("FN")) {
                        totdays = (v_aqday - v_jday) + v_rday;
                        v_tday = v_rday - 1;
                    } else {
                        totdays = (v_aqday - v_jday) + 1 + v_rday;
                        v_tday = v_rday;
                    }
                }
            }
            /**
             * *IF JOINED THIS MONTH AND RELIEVED THIS MONTH FROM SAME POST**
             */
            /**
             * *IF JOINED THIS MONTH AND NOT RELIEVED THIS MONTH FROM SAME
             * POST*
             */
            if (v_ifjoinedthismonth != null && v_ifrelievedthismonth == null) {
                if (v_jtime.equalsIgnoreCase("FN")) {
                    totdays = (v_aqday - v_jday) + 1;
                    v_fday = v_jday;
                    v_tday = v_aqday;
                } else {
                    totdays = (v_aqday - v_jday);
                    v_fday = v_jday;
                    v_tday = v_aqday;
                }

            }

            /**
             * IF JOINED THIS MONTH AND NOT RELIEVED THIS MONTH FROM SAME POST*
             */
            /**
             * ***********************IF RELIEVED THIS MONTH AND NOT JOINED
             * THIS MONTH IN SAME POST**************
             */
            if (v_ifjoinedthismonth == null && v_ifrelievedthismonth != null) {
                v_fday = 1;
                if (v_rtime.equalsIgnoreCase("FN")) {
                    totdays = v_rday - 1;
                    v_tday = v_rday - 1;
                    v_rday = v_tday;
                } else {
                    totdays = v_rday;
                    v_tday = v_rday;
                }
            }
            /**
             * ****IF RELIEVED THIS MONTH AND NOT JOINED THIS MONTH IN SAME
             * POST***
             */
            /**
             * NOT JOINED THIS MONTH AND NOT RELIEVED THIS MONTH
             */
            if (v_ifjoinedthismonth == null && v_ifrelievedthismonth == null) {
                totdays = v_aqday;
                v_fday = 1;
                v_tday = v_aqday;
            }

            /**
             * NOT JOINED THIS MONTH AND NOT RELIEVED THIS MONTH
             */
            /**
             * CALCULATING TOTAL DAYS ABSENT*
             */
            int v_totalAbsent = 0;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strmonstartDate = simpleDateFormat.format(monstartDate);
            String strmonendDate = simpleDateFormat.format(monendDate);
            pstmt = con.prepareStatement("SELECT ABS_FROM,ABS_TO FROM EMP_ABSENTEE where EMP_ID = ? and (ABS_FROM BETWEEN to_date(?, 'YYYY-MM-DD') AND to_date(?, 'YYYY-MM-DD') or ABS_TO BETWEEN to_date(?, 'YYYY-MM-DD') AND to_date(?, 'YYYY-MM-DD'))");
            pstmt.setString(1, aqMastModel.getEmpCode());
            pstmt.setString(2, strmonstartDate);
            pstmt.setString(3, strmonendDate);
            pstmt.setString(4, strmonstartDate);
            pstmt.setString(5, strmonendDate);
            rs = pstmt.executeQuery();
            Date tmonstartDate = new SimpleDateFormat("yyyy-MM-dd").parse(strmonstartDate);
            Date tmonendDate = new SimpleDateFormat("yyyy-MM-dd").parse(strmonendDate);
            while (rs.next()) {
                Date absfrom = rs.getDate("ABS_FROM");
                Date absto = rs.getDate("ABS_TO");
                if (absfrom.compareTo(tmonstartDate) >= 0 && absto.compareTo(tmonendDate) <= 0) {//Absent from and to with in this month
                    diff = absto.getTime() - absfrom.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    v_totalAbsent = v_totalAbsent + (int) days;
                    v_totalAbsent++;
                } else if (absfrom.compareTo(tmonstartDate) < 0 && absto.compareTo(tmonendDate) <= 0) {//absent from beftore this month but absent to in this month
                    diff = absto.getTime() - monstartDate.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    v_totalAbsent = v_totalAbsent + (int) days;
                } else if (absfrom.compareTo(tmonstartDate) >= 0 && absto.compareTo(tmonendDate) > 0) {//absent from in this month but absent to after this month
                    diff = monendDate.getTime() - absfrom.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);
                    v_totalAbsent = v_totalAbsent + (int) days;
                } else if (absfrom.compareTo(tmonstartDate) < 0 && absto.compareTo(tmonendDate) > 0) {//absent from beftore this month but absent to after this month
                    diff = monstartDate.getTime() - monendDate.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);
                    v_totalAbsent = v_totalAbsent + (int) days;
                }
            }

            /**
             * CALCULATING TOTAL DAYS ABSENT*
             */
            /**
             * CALCULATING TOTAL DAYS SANCTIONED*
             */
            int v_sanctionavailed = 0;

            pstmt = con.prepareStatement(" SELECT FDATE,TDATE FROM(SELECT FDATE,TDATE , not_id FROM EMP_LEAVE  "
                    + " WHERE EMP_ID = ? and IF_LONGTERM != 'Y'  AND LSOT_ID='01' and (fdate BETWEEN ? AND ? or tdate BETWEEN ? AND ?)) as EMP_LEAVE \n"
                    + " INNER JOIN (SELECT * FROM EMP_NOTIFICATION WHERE EMP_ID = ? AND NOT_TYPE='LEAVE'  "
                    + " AND EMP_NOTIFICATION.LINK_ID IS NULL "
                    + " AND STATUS='SANCTIONED AND AVAILED') as EMP_NOTIFICATION  "
                    + " ON EMP_LEAVE.NOT_ID=EMP_NOTIFICATION.NOT_ID");
            pstmt.setString(1, aqMastModel.getEmpCode());
            pstmt.setDate(2, new java.sql.Date(monstartDate.getTime()));
            pstmt.setDate(3, new java.sql.Date(monendDate.getTime()));
            pstmt.setDate(4, new java.sql.Date(monstartDate.getTime()));
            pstmt.setDate(5, new java.sql.Date(monendDate.getTime()));
            pstmt.setString(6, aqMastModel.getEmpCode());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Date sancfrom = rs.getDate("FDATE");
                Date sancto = rs.getDate("TDATE");

                if (sancfrom.compareTo(tmonstartDate) >= 0 && sancto.compareTo(tmonendDate) <= 0) {//Absent from and to with in this month
                    long diff2 = sancto.getTime() - sancfrom.getTime();
                    long days2 = (diff2 / (1000 * 60 * 60 * 24)) + 1;
                    v_sanctionavailed = v_sanctionavailed + (int) days2;
                } else if (sancfrom.compareTo(monstartDate) < 0 && sancto.compareTo(tmonendDate) <= 0) {//absent from beftore this month but absent to in this month
                    long diff3 = sancto.getTime() - monstartDate.getTime();
                    long days3 = (diff3 / (1000 * 60 * 60 * 24)) + 1;
                    v_sanctionavailed = v_sanctionavailed + (int) days3;
                } else if (sancfrom.compareTo(monstartDate) >= 0 && sancto.compareTo(tmonendDate) > 0) {//absent from in this month but absent to after this month
                    long diff4 = monendDate.getTime() - sancfrom.getTime();
                    long days4 = (diff4 / (1000 * 60 * 60 * 24)) + 1;
                    v_sanctionavailed = v_sanctionavailed + (int) days4;
                } else if (sancfrom.compareTo(monstartDate) < 0 && sancto.compareTo(tmonendDate) > 0) {//absent from beftore this month but absent to after this month
                    long diff5 = monstartDate.getTime() - monendDate.getTime();
                    long days5 = (diff5 / (1000 * 60 * 60 * 24)) + 1;
                    v_sanctionavailed = v_sanctionavailed + (int) days5;
                }
            }
            /**
             * CALCULATING TOTAL DAYS SANCTIONED*
             */
            /* CALCULATING JT TIME PERIOD */
            int v_jtavailed = 0;
            pstmt = con.prepareStatement("SELECT JOIN_TIME_FROM,JOIN_TIME_TO FROM EMP_LEAVE WHERE EMP_ID = ? AND (JOIN_TIME_FROM <= ? AND JOIN_TIME_TO >= ?)");
            pstmt.setString(1, aqMastModel.getEmpCode());
            pstmt.setDate(2, new java.sql.Date(monstartDate.getTime()));
            pstmt.setDate(3, new java.sql.Date(monendDate.getTime()));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Date joiningfrom = rs.getDate("JOIN_TIME_FROM");
                Date joiningto = rs.getDate("JOIN_TIME_TO");
                if (joiningfrom.compareTo(monstartDate) >= 0 && joiningto.compareTo(monendDate) <= 0) {//Absent from and to with in this month
                    diff = joiningto.getTime() - joiningfrom.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);
                    v_jtavailed = v_jtavailed + (int) days;
                } else if (joiningfrom.compareTo(monstartDate) < 0 && joiningto.compareTo(monendDate) <= 0) {//absent from beftore this month but absent to in this month
                    diff = joiningto.getTime() - monstartDate.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);
                    v_jtavailed = v_jtavailed + (int) days;
                } else if (joiningfrom.compareTo(monstartDate) >= 0 && joiningto.compareTo(monendDate) > 0) {//absent from in this month but absent to after this month
                    diff = monendDate.getTime() - joiningfrom.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);
                    v_jtavailed = v_jtavailed + (int) days;
                } else if (joiningfrom.compareTo(monstartDate) < 0 && joiningto.compareTo(monendDate) > 0) {//absent from beftore this month but absent to after this month
                    diff = monstartDate.getTime() - monendDate.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);
                    v_jtavailed = v_jtavailed + (int) days;
                }
            }
            /* CALCULATING JT TIME PERIOD */

            if (v_rday > 0) {
                v_payday = v_rday - (v_totalAbsent - v_sanctionavailed);
            } else {
                v_payday = totdays - (v_totalAbsent - v_sanctionavailed);
            }

            v_workday = totdays - v_totalAbsent;
            if (v_payday < 0) {
                v_payday = 0;
            }
            if (v_workday < 0) {
                v_workday = 0;
            }
            /*payworkday.put("payday", v_payday);
             payworkday.put("workday", v_workday);
             */
            System.out.println(v_workday + "==v_payday==" + v_payday);
            payworkday.put("payday", v_payday);
            payworkday.put("workday", v_workday);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return payworkday;
    }

    @Override
    public AqDtlsModel[] getAqDtlsModelFromPT(AqmastModel aqmast, int monbasic, int curbasic) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        ArrayList<AqDtlsModel> list = new ArrayList<>();

        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,BT_ID FROM G_AD_LIST WHERE AD_TYPE='D' AND SCHEDULE = 'PT'");
            res = pstmt.executeQuery();
            if (res.next()) {
                double annualGross = 0;
                System.out.println("aqmastgetGrossAmt" + aqmast.getGrossAmt());
                System.out.println("curbasic" + aqmast.getCurBasic());
                System.out.println("monbasic" + aqmast.getMonBasic());
                if (curbasic < monbasic) {
                    annualGross = ((aqmast.getGrossAmt() - curbasic) + monbasic) * 12;
                } else {
                    annualGross = aqmast.getGrossAmt() * 12;
                }
                int adamt = 0;
                if (annualGross <= 160000) {
                    adamt = 0;
                } else if (annualGross >= 160001 && annualGross <= 300000) {
                    adamt = 125;
                } else if (annualGross > 300000) {
                    if (aqmast.getAqMonth() + 1 != 2) {
                        adamt = 200;
                    } else {
                        adamt = 300;
                    }
                }
                AqDtlsModel aqModel = new AqDtlsModel();
                aqModel.setAqGroup(aqmast.getAqGroup());
                aqModel.setAqSlNo(aqmast.getAqSlNo());
                aqModel.setDdoOff("");
                aqModel.setEmpCode(aqmast.getEmpCode());
                aqModel.setPayMon(aqmast.getPayMonth());
                aqModel.setPayYear(aqmast.getPayYear());
                aqModel.setAqDate(aqmast.getAqDate());
                aqModel.setAqMonth(aqmast.getAqMonth());
                aqModel.setAqYear(aqmast.getAqYear());
                aqModel.setAqType(aqmast.getAqType());
                aqModel.setRefOrderNo(aqmast.getRefOrder());
                aqModel.setRefOrderDate(aqmast.getRefDate());
                aqModel.setSlNo(res.getInt("ROW_NO"));
                aqModel.setAdCode(res.getString("AD_CODE_NAME"));
                aqModel.setAdDesc(res.getString("AD_DESC"));
                aqModel.setAdType(res.getString("AD_TYPE"));
                aqModel.setAltUnit(res.getString("ALT_UNIT"));
                aqModel.setDedType(res.getString("DED_TYPE"));
                aqModel.setAdAmt(adamt);
                aqModel.setAccNo(null);
                aqModel.setRefDesc(null);
                aqModel.setRefCount(0);
                aqModel.setSchedule(res.getString("SCHEDULE"));
                aqModel.setNowDedn(null);
                aqModel.setTotRecAmt(0);
                aqModel.setRepCol(res.getInt("REP_COL"));
                aqModel.setAdRefId(null);
                aqModel.setBtId(res.getString("BT_ID"));
                aqModel.setInstalCount(1);
                list.add(aqModel);
            }
        } catch (SQLException exe) {
            exe.printStackTrace();
        } finally {
            try {
                pstmt.close();
                con.close();
            } catch (SQLException exe) {
                exe.printStackTrace();
            }
        }
        AqDtlsModel aqDtlsModels[] = list.toArray(new AqDtlsModel[list.size()]);
        return aqDtlsModels;
    }

    public boolean isStringInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public AqDtlsModel[] getAqDtlsModelFromCPF(PayComponent payComponent, AqmastModel aqmast) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        ResultSet res2 = null;
        ArrayList<AqDtlsModel> list = new ArrayList<>();
        try {
            con = dataSource.getConnection();
            String aqdtlstbl = AqFunctionalities.getAQBillDtlsTable(aqmast.getAqMonth(), aqmast.getAqYear());
            int da = getDAAmount(aqmast.getAqSlNo(),aqdtlstbl);
            pstmt = con.prepareStatement("SELECT FIXEDVALUE FROM UPDATE_AD_INFO INNER JOIN EMP_MAST ON UPDATE_AD_INFO.UPDATION_REF_CODE=EMP_MAST.EMP_ID WHERE WHERE_UPDATED = 'E' AND UPDATION_REF_CODE = ? AND REF_AD_CODE='76' AND EMP_MAST.IF_GPF_ASSUMED='Y'");
            pstmt.setString(1, aqmast.getEmpCode());
            res = pstmt.executeQuery();
            int fixedcpf = 0;
            int govtcontribution = 0;
            String refdesc = null;
            if (res.next()) {
                fixedcpf = res.getInt("FIXEDVALUE");
            } else {
                if((aqmast.getAqMonth() >= 9 && aqmast.getAqYear() == 2021) || aqmast.getAqYear() > 2021){
                    fixedcpf = new Long(Math.round((payComponent.getBasic() + payComponent.getGp() + da) * 0.1)).intValue();
                    govtcontribution = new Long(Math.round((payComponent.getBasic() + payComponent.getGp() + da) * 0.14)).intValue();
                    refdesc = "14%";
                }else{
                    fixedcpf = new Long(Math.round((payComponent.getBasic() + payComponent.getGp() + da) * 0.1)).intValue();
                    refdesc = "10%";
                }
            }
            
            pstmt = con.prepareStatement("SELECT AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,BT_ID FROM G_AD_LIST WHERE AD_TYPE='D' AND SCHEDULE = 'CPF'");
            res = pstmt.executeQuery();
            if (res.next()) {

                AqDtlsModel aqModel = new AqDtlsModel();
                aqModel.setAqGroup(aqmast.getAqGroup());
                aqModel.setAqSlNo(aqmast.getAqSlNo());
                aqModel.setDdoOff("");
                aqModel.setEmpCode(aqmast.getEmpCode());
                aqModel.setPayMon(aqmast.getPayMonth());
                aqModel.setPayYear(aqmast.getPayYear());
                aqModel.setAqDate(aqmast.getAqDate());
                aqModel.setAqMonth(aqmast.getAqMonth());
                aqModel.setAqYear(aqmast.getAqYear());
                aqModel.setAqType(aqmast.getAqType());
                aqModel.setRefOrderNo(aqmast.getRefOrder());
                aqModel.setRefOrderDate(aqmast.getRefDate());
                aqModel.setSlNo(res.getInt("ROW_NO"));
                aqModel.setAdCode(res.getString("AD_CODE_NAME"));
                aqModel.setAdDesc(res.getString("AD_DESC"));
                aqModel.setAdType(res.getString("AD_TYPE"));
                aqModel.setAltUnit(res.getString("ALT_UNIT"));
                aqModel.setDedType(res.getString("DED_TYPE"));
                aqModel.setAdAmt(fixedcpf);
                aqModel.setAccNo(null);
                aqModel.setRefDesc(refdesc);
                aqModel.setRefCount(0);
                aqModel.setSchedule(res.getString("SCHEDULE"));
                aqModel.setNowDedn(null);
                aqModel.setTotRecAmt(govtcontribution);
                aqModel.setRepCol(res.getInt("REP_COL"));
                aqModel.setAdRefId(null);
                aqModel.setBtId(res.getString("BT_ID"));
                aqModel.setInstalCount(1);
                list.add(aqModel);
            }
        } catch (SQLException exe) {
            exe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        AqDtlsModel aqDtlsModels[] = list.toArray(new AqDtlsModel[list.size()]);
        return aqDtlsModels;
    }

    public AqDtlsModel[] getAqDtlsModelFromGPF_TPF(PayComponent payComponent, AqmastModel aqmast) {
        Connection con = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet res = null;
        ResultSet res2 = null;
        ArrayList<AqDtlsModel> list = new ArrayList<>();
        String adCode = "";
        String schedule = "";
        try {
            con = dataSource.getConnection();
            
            String aqdtlstbl = AqFunctionalities.getAQBillDtlsTable(aqmast.getAqMonth(), aqmast.getAqYear());
            int da = getDAAmount(aqmast.getAqSlNo(),aqdtlstbl);

            if (aqmast.getAcctType().equalsIgnoreCase("GPF")) {
                adCode = "54";
                schedule = "GPF";
            } else if (aqmast.getAcctType().equalsIgnoreCase("TPF")) {
                adCode = "123";
                schedule = "TPF";
            }
            System.out.println("===");
            pstmt = con.prepareStatement("SELECT FIXEDVALUE FROM UPDATE_AD_INFO WHERE WHERE_UPDATED = 'E' AND UPDATION_REF_CODE = ? AND REF_AD_CODE=? ");
            pstmt.setString(1, aqmast.getEmpCode());
            pstmt.setString(2, adCode);
            res = pstmt.executeQuery();
            int fixedcpf = 0;
            if (res.next()) {
                fixedcpf = res.getInt("FIXEDVALUE");
            } else {
                if (aqmast.getAcctType().equalsIgnoreCase("GPF")) {
                    fixedcpf = new Long(Math.round(payComponent.getBasic() * 0.1)).intValue();
                } else if (aqmast.getAcctType().equalsIgnoreCase("TPF")) {
                    fixedcpf = new Long(Math.round((payComponent.getBasic() + payComponent.getGp()) * 0.1)).intValue();

                }

            }
            pstmt = con.prepareStatement("SELECT AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,BT_ID FROM G_AD_LIST WHERE AD_TYPE='D' AND SCHEDULE = ? and ad_code=? ");
            pstmt.setString(1, schedule);
            pstmt.setString(2, adCode);
            res = pstmt.executeQuery();
            if (res.next()) {

                AqDtlsModel aqModel = new AqDtlsModel();
                aqModel.setAqGroup(aqmast.getAqGroup());
                aqModel.setAqSlNo(aqmast.getAqSlNo());
                aqModel.setDdoOff("");
                aqModel.setEmpCode(aqmast.getEmpCode());
                aqModel.setPayMon(aqmast.getPayMonth());
                aqModel.setPayYear(aqmast.getPayYear());
                aqModel.setAqDate(aqmast.getAqDate());
                aqModel.setAqMonth(aqmast.getAqMonth());
                aqModel.setAqYear(aqmast.getAqYear());
                aqModel.setAqType(aqmast.getAqType());
                aqModel.setRefOrderNo(aqmast.getRefOrder());
                aqModel.setRefOrderDate(aqmast.getRefDate());
                aqModel.setSlNo(res.getInt("ROW_NO"));
                aqModel.setAdCode(res.getString("AD_CODE_NAME"));
                aqModel.setAdDesc(res.getString("AD_DESC"));
                aqModel.setAdType(res.getString("AD_TYPE"));
                aqModel.setAltUnit(res.getString("ALT_UNIT"));
                aqModel.setDedType(res.getString("DED_TYPE"));
                aqModel.setAdAmt(fixedcpf);
                aqModel.setAccNo(null);
                aqModel.setRefDesc(null);
                aqModel.setRefCount(0);
                aqModel.setSchedule(res.getString("SCHEDULE"));
                aqModel.setNowDedn(null);
                aqModel.setTotRecAmt(0);
                aqModel.setRepCol(res.getInt("REP_COL"));
                aqModel.setAdRefId(null);
                aqModel.setBtId(getGPFBTId(aqmast.getGpfType()));
                aqModel.setInstalCount(1);
                list.add(aqModel);
            }
        } catch (SQLException exe) {
            exe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        AqDtlsModel aqDtlsModels[] = list.toArray(new AqDtlsModel[list.size()]);
        return aqDtlsModels;
    }

    public AqDtlsModel[] getAqDtlsModelFromEPF(PayComponent payComponent, AqmastModel aqmast) {
        Connection con = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet res = null;
        ResultSet res2 = null;
        ArrayList<AqDtlsModel> list = new ArrayList<>();
        String adCode = "";
        String schedule = "";
        try {
            con = dataSource.getConnection();            
            
            String aqdtlstbl = AqFunctionalities.getAQBillDtlsTable(aqmast.getAqMonth(), aqmast.getAqYear());
            int da = getDAAmount(aqmast.getAqSlNo(),aqdtlstbl);
            adCode = "173";
            schedule = "EPFDED";

            pstmt = con.prepareStatement("SELECT FIXEDVALUE FROM UPDATE_AD_INFO WHERE WHERE_UPDATED = 'E' AND UPDATION_REF_CODE = ? AND REF_AD_CODE=? ");
            pstmt.setString(1, aqmast.getEmpCode());
            pstmt.setString(2, adCode);
            res = pstmt.executeQuery();
            int fixedcpf = 0;
            if (res.next()) {
                fixedcpf = res.getInt("FIXEDVALUE");
            } else {
                if (aqmast.getAcctType().equalsIgnoreCase("EPF")) {
                    fixedcpf = new Long(Math.round(15000 * 0.12)).intValue();
                }
            }
            pstmt = con.prepareStatement("SELECT AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,BT_ID FROM G_AD_LIST WHERE AD_TYPE='D' AND ad_code_name='EPFDED'");
            res = pstmt.executeQuery();
            if (res.next()) {

                AqDtlsModel aqModel = new AqDtlsModel();
                aqModel.setAqGroup(aqmast.getAqGroup());
                aqModel.setAqSlNo(aqmast.getAqSlNo());
                aqModel.setDdoOff("");
                aqModel.setEmpCode(aqmast.getEmpCode());
                aqModel.setPayMon(aqmast.getPayMonth());
                aqModel.setPayYear(aqmast.getPayYear());
                aqModel.setAqDate(aqmast.getAqDate());
                aqModel.setAqMonth(aqmast.getAqMonth());
                aqModel.setAqYear(aqmast.getAqYear());
                aqModel.setAqType(aqmast.getAqType());
                aqModel.setRefOrderNo(aqmast.getRefOrder());
                aqModel.setRefOrderDate(aqmast.getRefDate());
                aqModel.setSlNo(res.getInt("ROW_NO"));
                aqModel.setAdCode(res.getString("AD_CODE_NAME"));
                aqModel.setAdDesc(res.getString("AD_DESC"));
                aqModel.setAdType(res.getString("AD_TYPE"));
                aqModel.setAltUnit(res.getString("ALT_UNIT"));
                aqModel.setDedType(res.getString("DED_TYPE"));
                aqModel.setAdAmt(fixedcpf);
                aqModel.setAccNo(null);
                aqModel.setRefDesc(null);
                aqModel.setRefCount(0);
                aqModel.setSchedule(res.getString("SCHEDULE"));
                aqModel.setNowDedn(null);
                aqModel.setTotRecAmt(0);
                aqModel.setRepCol(res.getInt("REP_COL"));
                aqModel.setAdRefId(null);
                aqModel.setBtId(res.getString("BT_ID"));
                aqModel.setInstalCount(1);
                list.add(aqModel);
            }
        } catch (SQLException exe) {
            exe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        AqDtlsModel aqDtlsModels[] = list.toArray(new AqDtlsModel[list.size()]);
        return aqDtlsModels;
    }

    public int getDAAmount(String aqslno,String aqdtlstbl) {
        int da = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select ad_amt from "+aqdtlstbl+" where aqsl_no=? and ad_code='DA'");
            pstmt.setString(1, aqslno);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                da = rs.getInt("ad_amt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return da;
    }

    public String getGPFBTId(String gpfSeries) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String gpfbtid = "";
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT BT_ID FROM G_GPF_TYPE WHERE GPF_TYPE = ?");
            pstmt.setString(1, gpfSeries);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                gpfbtid = rs.getString("BT_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gpfbtid;
    }
}
