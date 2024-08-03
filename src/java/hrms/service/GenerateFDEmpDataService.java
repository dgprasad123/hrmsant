/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.service;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.stereotype.Service;

/**
 *
 * @author Manas
 */
@Service
public class GenerateFDEmpDataService {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void generateFDEmpData() {
        Map<String, String> m = new HashMap<String, String>();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        year = 2017;

        if (month >= 2) {
            m.put("2", year + "");
            m.put("3", year + "");
            m.put("4", year + "");
            m.put("5", year + "");
            m.put("6", year + "");
            m.put("7", year + "");
            m.put("8", year + "");
            m.put("9", year + "");
            m.put("10", year + "");
            m.put("11", year + "");
            year++;
            m.put("0", year + "");
            m.put("1", year + "");

        } else {
            m.put("0", year + "");
            m.put("1", year + "");
            year--;
            m.put("3", year + "");
            m.put("4", year + "");
            m.put("5", year + "");
            m.put("6", year + "");
            m.put("7", year + "");
            m.put("8", year + "");
            m.put("9", year + "");
            m.put("10", year + "");
            m.put("11", year + "");

        }

        for (Map.Entry<String, String> entry : m.entrySet()) {
            generateFDEmpData(entry.getKey(), entry.getValue());
        }
    }

    public void generateFDEmpData(String month, String year) {        
        Connection con = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet res1 = null;
        ResultSet res2 = null;
        ResultSet res3 = null;
        ResultSet res4 = null;
        ResultSet res5 = null;
        ResultSet rs6 = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        PreparedStatement pst = null;
        PreparedStatement ps5 = null;
        PreparedStatement ps6 = null;
        String deptName = null;
        String deptCode = null;
        String distName = null;
        String distCode = null;
        String plan = null;
        String major_head = null;
        String demandNo = null;

        int gp_amount = 0;
        int DA_Tamount = 0;
        int DP_Tamount = 0;
        int GP_Tamount = 0;
        int HRA_Tamount = 0;
        int SP_Tamount = 0;
        int OA_Tamount = 0;
        int RCM_Tamount = 0;
        int MENIN_position = 0;
        int Basic_Total = 0;
        String plandesc = null;

        try {
            con = dataSource.getConnection();
            ps6 = con.prepareStatement("SELECT department_code,department_name FROM g_department WHERE if_active='Y'  ORDER BY department_name ");
            rs6 = ps6.executeQuery();
            while (rs6.next()) {
                deptName = rs6.getString("department_name");
                deptCode = rs6.getString("department_code");
                ps1 = con.prepareStatement("SELECT DISTINCT(bill_mast.major_head),bill_mast.demand_no,g_plan_status.plan_descpn,aq_mast.pay_scale,bill_mast.aq_month,\n"
                        + "bill_mast.aq_year,bill_mast.plan FROM bill_mast INNER JOIN g_office ON bill_mast.off_code=g_office.off_code  LEFT JOIN  g_plan_status ON bill_mast.plan=g_plan_status.description  \n"
                        + "LEFT  JOIN aq_mast ON aq_mast.bill_no=bill_mast.bill_no   WHERE   aq_mast.aq_month =? AND aq_mast.aq_year =?  \n"
                        + " AND g_office.department_code=?");
                ps1.setInt(1, Integer.parseInt(month));
                ps1.setInt(2, Integer.parseInt(year));
                ps1.setString(3, deptCode);
                res1 = ps1.executeQuery();
                while (res1.next()) {
                    plan = res1.getString("plan");
                    plandesc = res1.getString("plan_descpn");
                    major_head = res1.getString("major_head");
                    demandNo = res1.getString("demand_no");
                    String PayScale = res1.getString("pay_scale");

                    ps2 = con.prepareStatement("SELECT COALESCE(SUM(a.ad_amt),0) as gpTotal FROM aq_dtls a INNER JOIN  aq_mast b ON  a.aqsl_no=b.aqsl_no  INNER JOIN bill_mast ON bill_mast.bill_no=b.bill_no \n"
                            + "INNER JOIN g_office ON b.off_code=g_office.off_code  \n"
                            + " WHERE  bill_mast.aq_month =? AND bill_mast.aq_year =? AND b.pay_scale=? AND g_office.department_code=?     \n"
                            + "  AND bill_mast.major_head=? AND bill_mast.demand_no=?  AND ad_code=? AND bill_mast.plan=?   AND ad_amt >0  LIMIT 1");

                    ps2.setInt(1, Integer.parseInt(month));
                    ps2.setInt(2, Integer.parseInt(year));
                    ps2.setString(3, PayScale);
                    ps2.setString(4, deptCode);
                    ps2.setString(5, major_head);
                    ps2.setString(6, demandNo);
                    ps2.setString(7, "GP");
                    ps2.setString(8, plan);
                    res2 = ps2.executeQuery();
                    while (res2.next()) {
                        gp_amount = res2.getInt("gpTotal");

                    }
                    pst = con.prepareStatement("SELECT COALESCE(SUM(a.ad_amt),0) as Total_Amount , schedule FROM aq_dtls a INNER JOIN  aq_mast b ON  a.aqsl_no=b.aqsl_no  \n"
                            + "INNER JOIN bill_mast ON bill_mast.bill_no=b.bill_no   \n"
                            + " INNER JOIN g_office ON b.off_code=g_office.off_code    \n"
                            + " WHERE  bill_mast.aq_month =? AND bill_mast.aq_year =?  AND   g_office.department_code=?  AND b.pay_scale=?    AND      \n"
                            + " (schedule='GP' OR  schedule='SP' OR schedule='DP' OR schedule='RCM' OR schedule='DA' OR schedule='HRA' OR schedule='OA') AND bill_mast.major_head=? AND bill_mast.demand_no=?  AND bill_mast.plan=?   GROUP BY schedule ORDER BY schedule");
                    pst.setInt(1, Integer.parseInt(month));
                    pst.setInt(2, Integer.parseInt(year));
                    pst.setString(3, deptCode);
                    pst.setString(4, PayScale);
                    pst.setString(5, major_head);
                    pst.setString(6, demandNo);
                    pst.setString(7, plan);
                    res3 = pst.executeQuery();
                    while (res3.next()) {
                        if (res3.getString("schedule").equals("DA")) {
                            DA_Tamount = res3.getInt("Total_Amount");
                        }
                        if (res3.getString("schedule").equals("GP")) {
                            GP_Tamount = res3.getInt("Total_Amount");
                        }
                        if (res3.getString("schedule").equals("DP")) {
                            DP_Tamount = res3.getInt("Total_Amount");
                        }
                        if (res3.getString("schedule").equals("HRA")) {
                            HRA_Tamount = res3.getInt("Total_Amount");
                        }
                        if (res3.getString("schedule").equals("OA")) {
                            OA_Tamount = res3.getInt("Total_Amount");
                        }
                        if (res3.getString("schedule").equals("RCM")) {
                            RCM_Tamount = res3.getInt("Total_Amount");
                        }
                        if (res3.getString("schedule").equals("SP")) {
                            SP_Tamount = res3.getInt("Total_Amount");
                        }
                    }

                    ps3 = con.prepareStatement("SELECT COUNT(*) MENIN_POSITION FROM bill_mast INNER JOIN aq_mast ON bill_mast.bill_no=aq_mast.bill_no  \n"
                            + "INNER JOIN G_OFFICE ON BILL_MAST.OFF_CODE=G_OFFICE.OFF_CODE    \n"
                            + " WHERE bill_mast.aq_month=?  AND  bill_mast.aq_year=?    \n"
                            + " AND bill_mast.major_head=? AND bill_mast.demand_no=? AND bill_mast.plan=?  AND emp_code is NOT NULL       \n"
                            + " and aq_mast.pay_scale=? AND G_OFFICE.DEPARTMENT_CODE=?");
                    ps3.setInt(1, Integer.parseInt(month));
                    ps3.setInt(2, Integer.parseInt(year));
                    ps3.setString(3, major_head);
                    ps3.setString(4, demandNo);
                    ps3.setString(5, plan);
                    ps3.setString(6, PayScale);
                    ps3.setString(7, deptCode);
                    res3 = ps3.executeQuery();
                    while (res3.next()) {
                        MENIN_position = res3.getInt("MENIN_POSITION");

                    }
                    /**
                     * ******************** BaSic Total
                     * ***************************************
                     */
                    ps5 = con.prepareStatement("SELECT COALESCE(SUM(a.mon_basic),0) as BasicTotal FROM aq_mast a INNER JOIN bill_mast ON bill_mast.bill_no=a.bill_no  INNER JOIN g_office ON a.off_code=g_office.off_code   \n"
                            + "WHERE  bill_mast.aq_month =? AND bill_mast.aq_year =? AND a.pay_scale=? AND g_office.department_code=?   \n"
                            + " AND bill_mast.major_head=? AND bill_mast.demand_no=?   AND bill_mast.plan=? ");

                    ps5.setInt(1, Integer.parseInt(month));
                    ps5.setInt(2, Integer.parseInt(year));
                    ps5.setString(3, PayScale);
                    ps5.setString(4, deptCode);
                    ps5.setString(5, major_head);
                    ps5.setString(6, demandNo);
                    ps5.setString(7, plan);
                    res5 = ps5.executeQuery();
                    while (res5.next()) {
                        Basic_Total = res5.getInt("BasicTotal");

                    }
                    /**
                     * ***********************************************************
                     */

                    int logId = CommonFunctions.getMaxCode(con, "log_frbm", "log_frbm_id");
                    ps = con.prepareStatement("INSERT INTO log_frbm (log_frbm_id,dept_name,plan_name,demand_no,majour_head,scale_pay,grade_pay,men_position,spl_amount,da_amount,dp_amount,gp_amount,hra_amount,rcm_amount,oa_amount,month,year,basic_amount) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
                    ps.setInt(1, logId);
                    ps.setString(2, deptName);
                    ps.setString(3, plandesc);
                    ps.setString(4, demandNo);
                    ps.setString(5, major_head);
                    ps.setString(6, PayScale);
                    ps.setInt(7, gp_amount);
                    ps.setInt(8, MENIN_position);
                    ps.setInt(9, SP_Tamount);
                    ps.setInt(10, DA_Tamount);
                    ps.setInt(11, DP_Tamount);
                    ps.setInt(12, GP_Tamount);
                    ps.setInt(13, HRA_Tamount);
                    ps.setInt(14, RCM_Tamount);
                    ps.setInt(15, OA_Tamount);
                    ps.setString(16, month);
                    ps.setString(17, year);
                    ps.setInt(18, Basic_Total);
                    ps.execute();

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            DataBaseFunctions.closeSqlObjects(ps1, ps2, ps3);
            DataBaseFunctions.closeSqlObjects(ps, pst, ps5);
            DataBaseFunctions.closeSqlObjects(ps6);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}
