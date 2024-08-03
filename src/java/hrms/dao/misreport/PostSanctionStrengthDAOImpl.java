/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.misreport;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.misreport.PostSanctionStrength;
import hrms.model.onlineTicketing.OnlineTicketing;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author DurgaPrasad
 */
public class PostSanctionStrengthDAOImpl implements PostSanctionStrengthDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void generatePostSanctionStrengh() {
        Connection con = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet res1 = null;
        ResultSet res2 = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;

        PreparedStatement pst = null;
        String deptName = null;
        String deptCode = null;
        String distName = null;
        String distCode = null;
        int ss_gp_1900 = 0;
        int mp_gp_1900 = 0;
        int ss_gp_2400 = 0;
        int mp_gp_2400 = 0;
        int ss_gp_4200 = 0;
        int mp_gp_4200 = 0;
        int ss_gp_4600 = 0;
        int mp_gp_4600 = 0;

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("TRUNCATE table  log_ga_strength");
            ps.executeUpdate();

            ps = con.prepareStatement("SELECT department_code,department_name FROM g_department WHERE if_active='Y' ORDER BY department_name ");
            rs = ps.executeQuery();
            while (rs.next()) {
                deptName = rs.getString("department_name");
                deptCode = rs.getString("department_code");
                pst = con.prepareStatement("SELECT * FROM g_district WHERE state_code='21'");
                rs1 = pst.executeQuery();
                while (rs1.next()) {
                    distName = rs1.getString("dist_name");
                    distCode = rs1.getString("dist_code");
                    /**
                     * *********************************** 1900 GP
                     * ************************
                     */
                    ps1 = con.prepareStatement("SELECT count(*) as cnt_ss_1900 FROM g_post INNER JOIN  g_spc ON g_spc.gpc=g_post.post_code  INNER JOIN g_office ON g_spc.off_code=g_office.off_code AND g_office.dist_code=?\n"
                            + "LEFT OUTER JOIN section_post_mapping ON g_spc.spc=section_post_mapping.spc \n"
                            + "WHERE  g_post.department_code=? \n"
                            + " AND post LIKE '%JUNIOR CLERK%' AND gp='1900' and (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) AND is_sanctioned='Y'");
                    ps1.setString(1, distCode);
                    ps1.setString(2, deptCode);
                    res1 = ps1.executeQuery();
                    while (res1.next()) {
                        ss_gp_1900 = res1.getInt("cnt_ss_1900");
                    }
                    
                    DataBaseFunctions.closeSqlObjects(res1,ps1);
                    
                    ps2 = con.prepareStatement("SELECT count(*) as cnt_mp_1900  FROM g_post INNER JOIN  g_spc ON g_spc.gpc=g_post.post_code INNER JOIN g_office ON g_spc.off_code=g_office.off_code AND g_office.dist_code=?\n"
                            + "LEFT OUTER JOIN section_post_mapping ON g_spc.spc=section_post_mapping.spc \n"
                            + "INNER JOIN emp_mast ON section_post_mapping.spc =emp_mast.cur_spc\n"
                            + "WHERE   g_post.department_code=? \n"
                            + " AND post LIKE '%JUNIOR CLERK%' AND g_spc.gp='1900' and (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) AND is_sanctioned='Y'");
                    ps2.setString(1, distCode);
                    ps2.setString(2, deptCode);
                    res2 = ps2.executeQuery();
                    while (res2.next()) {
                        mp_gp_1900 = res2.getInt("cnt_mp_1900");
                    }
                    DataBaseFunctions.closeSqlObjects(res2,ps2);
                    /**
                     * *******************************2400 GP
                     * *********************
                     */
                    ps1 = con.prepareStatement("SELECT count(*) as cnt_ss_2400 FROM g_post INNER JOIN  g_spc ON g_spc.gpc=g_post.post_code  INNER JOIN g_office ON g_spc.off_code=g_office.off_code AND g_office.dist_code=?\n"
                            + "LEFT OUTER JOIN section_post_mapping ON g_spc.spc=section_post_mapping.spc \n"
                            + "WHERE  g_post.department_code=? \n"
                            + " AND post LIKE '%JUNIOR CLERK%' AND gp='2400' and (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) AND is_sanctioned='Y'");
                    ps1.setString(1, distCode);
                    ps1.setString(2, deptCode);
                    res1 = ps1.executeQuery();
                    while (res1.next()) {
                        ss_gp_2400 = res1.getInt("cnt_ss_2400");
                    }
                    
                    DataBaseFunctions.closeSqlObjects(res1,ps1);
                    
                    ps2 = con.prepareStatement("SELECT count(*) as cnt_mp_2400  FROM g_post INNER JOIN  g_spc ON g_spc.gpc=g_post.post_code INNER JOIN g_office ON g_spc.off_code=g_office.off_code AND g_office.dist_code=?\n"
                            + "LEFT OUTER JOIN section_post_mapping ON g_spc.spc=section_post_mapping.spc \n"
                            + "INNER JOIN emp_mast ON section_post_mapping.spc =emp_mast.cur_spc\n"
                            + "WHERE   g_post.department_code=? \n"
                            + " AND post LIKE '%JUNIOR CLERK%' AND g_spc.gp='2400' and (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) AND is_sanctioned='Y'");
                    ps2.setString(1, distCode);
                    ps2.setString(2, deptCode);
                    res2 = ps2.executeQuery();
                    while (res2.next()) {
                        mp_gp_2400 = res2.getInt("cnt_mp_2400");
                    }
                    DataBaseFunctions.closeSqlObjects(res2,ps2);
                    /**
                     * ******************************************* 4200 GP
                     * ******************************
                     */
                    ps1 = con.prepareStatement("SELECT count(*) as cnt_ss_4200 FROM g_post INNER JOIN  g_spc ON g_spc.gpc=g_post.post_code  INNER JOIN g_office ON g_spc.off_code=g_office.off_code AND g_office.dist_code=?\n"
                            + "LEFT OUTER JOIN section_post_mapping ON g_spc.spc=section_post_mapping.spc \n"
                            + "WHERE  g_post.department_code=? \n"
                            + " AND post LIKE '%JUNIOR CLERK%' AND gp='4200' and (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) AND is_sanctioned='Y'");
                    ps1.setString(1, distCode);
                    ps1.setString(2, deptCode);
                    res1 = ps1.executeQuery();
                    while (res1.next()) {
                        ss_gp_4200 = res1.getInt("cnt_ss_4200");
                    }
                    
                    DataBaseFunctions.closeSqlObjects(res1,ps1);
                    
                    ps2 = con.prepareStatement("SELECT count(*) as cnt_mp_4200  FROM g_post INNER JOIN  g_spc ON g_spc.gpc=g_post.post_code INNER JOIN g_office ON g_spc.off_code=g_office.off_code AND g_office.dist_code=?\n"
                            + "LEFT OUTER JOIN section_post_mapping ON g_spc.spc=section_post_mapping.spc \n"
                            + "INNER JOIN emp_mast ON section_post_mapping.spc =emp_mast.cur_spc\n"
                            + "WHERE   g_post.department_code=? \n"
                            + " AND post LIKE '%JUNIOR CLERK%' AND g_spc.gp='4200' and (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) AND is_sanctioned='Y'");
                    ps2.setString(1, distCode);
                    ps2.setString(2, deptCode);
                    res2 = ps2.executeQuery();
                    while (res2.next()) {
                        mp_gp_4200 = res2.getInt("cnt_mp_4200");
                    }
                    DataBaseFunctions.closeSqlObjects(res2,ps2);
                    /**
                     * ******************************************* 4600 GP
                     * ******************************
                     */
                    ps1 = con.prepareStatement("SELECT count(*) as cnt_ss_4600 FROM g_post INNER JOIN  g_spc ON g_spc.gpc=g_post.post_code  INNER JOIN g_office ON g_spc.off_code=g_office.off_code AND g_office.dist_code=?\n"
                            + "LEFT OUTER JOIN section_post_mapping ON g_spc.spc=section_post_mapping.spc \n"
                            + "WHERE  g_post.department_code=? \n"
                            + " AND post LIKE '%JUNIOR CLERK%' AND gp='4600' and (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) AND is_sanctioned='Y'");
                    ps1.setString(1, distCode);
                    ps1.setString(2, deptCode);
                    res1 = ps1.executeQuery();
                    while (res1.next()) {
                        ss_gp_4600 = res1.getInt("cnt_ss_4600");
                    }
                    DataBaseFunctions.closeSqlObjects(res1,ps1);
                    
                    ps2 = con.prepareStatement("SELECT count(*) as cnt_mp_4600  FROM g_post INNER JOIN  g_spc ON g_spc.gpc=g_post.post_code INNER JOIN g_office ON g_spc.off_code=g_office.off_code AND g_office.dist_code=?\n"
                            + "LEFT OUTER JOIN section_post_mapping ON g_spc.spc=section_post_mapping.spc \n"
                            + "INNER JOIN emp_mast ON section_post_mapping.spc =emp_mast.cur_spc\n"
                            + "WHERE   g_post.department_code=? \n"
                            + " AND post LIKE '%JUNIOR CLERK%' AND g_spc.gp='4600' and (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) AND is_sanctioned='Y'");
                    ps2.setString(1, distCode);
                    ps2.setString(2, deptCode);
                    res2 = ps2.executeQuery();
                    while (res2.next()) {
                        mp_gp_4600 = res2.getInt("cnt_mp_4600");
                    }
                    DataBaseFunctions.closeSqlObjects(res2,ps2);
                    /**
                     * ******************** Save into database
                     * ***********************
                     */
                    int logId = CommonFunctions.getMaxCode(con, "log_ga_strength", "log_dept_id");
                    ps = con.prepareStatement("INSERT INTO log_ga_strength (log_dept_id,dept_name,dist_name,ss_gp_1900,mp_gp_1900,ss_gp_2400,mp_gp_2400,ss_gp_4200,mp_gp_4200,ss_gp_4600,mp_gp_4600) VALUES (?,?,?,?,?,?,?,?,?,?,?) ");
                    ps.setInt(1, logId);
                    ps.setString(2, deptName);
                    ps.setString(3, distName);
                    ps.setInt(4, ss_gp_1900);
                    ps.setInt(5, mp_gp_1900);
                    ps.setInt(6, ss_gp_2400);
                    ps.setInt(7, mp_gp_2400);
                    ps.setInt(8, ss_gp_4200);
                    ps.setInt(9, mp_gp_4200);
                    ps.setInt(10, ss_gp_4600);
                    ps.setInt(11, mp_gp_4600);
                    ps.execute();

                }

            }

            //  ps.setString(1, loginId);
            //   ps.setInt(2, ticketId);
            //   ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, pst, ps1, ps2);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List viewPostSanctionStrengh() {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        List li = new ArrayList();
        PostSanctionStrength postSanctionStrength = null;
        String deptname = "";
        try {
            con = dataSource.getConnection();

            ps = con.prepareStatement("SELECT * FROM log_ga_strength ORDER BY dept_name,dist_name");
            rs = ps.executeQuery();

            while (rs.next()) {
                postSanctionStrength = new PostSanctionStrength();
                if (deptname == null || deptname.equals("")) {
                    postSanctionStrength.setDeptName(rs.getString("dept_name"));
                    deptname = rs.getString("dept_name");

                } else if (!deptname.equals(rs.getString("dept_name"))) {
                    postSanctionStrength.setDeptName(rs.getString("dept_name"));
                    deptname = rs.getString("dept_name");
                }
                //ticket.setDeptName(rs.getString("dept_name"));
                postSanctionStrength.setDistName(rs.getString("dist_name"));
                postSanctionStrength.setSs_gp_1900(rs.getInt("ss_gp_1900"));
                postSanctionStrength.setMp_gp_1900(rs.getInt("mp_gp_1900"));
                postSanctionStrength.setSs_gp_2400(rs.getInt("ss_gp_2400"));
                postSanctionStrength.setMp_gp_2400(rs.getInt("mp_gp_2400"));
                postSanctionStrength.setSs_gp_4200(rs.getInt("ss_gp_4200"));
                postSanctionStrength.setMp_gp_4200(rs.getInt("mp_gp_4200"));
                postSanctionStrength.setSs_gp_4600(rs.getInt("ss_gp_4600"));
                postSanctionStrength.setMp_gp_4600(rs.getInt("mp_gp_4600"));
                li.add(postSanctionStrength);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
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
            //ps = con.prepareStatement("TRUNCATE table  log_frbm");
            // ps.executeUpdate();

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

    @Override
    public void generateCenusData() {
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
        String category = null;

        int sc_male = 0;
        int sc_female = 0;
        int st_male = 0;
        int st_female = 0;
        int g_male = 0;
        int g_female = 0;
        int vacancy_post = 0;

        int age_18_50 = 0;
        int age_50_60 = 0;
        int age_60 = 0;
        int gross_16k = 0;
        int gross_16k_25k = 0;
        int gross_25k_50k = 0;
        int gross_50k_100k = 0;
        int gross_100k = 0;
        int non_regular = 0;

        String[] grp = {"A", "B", "C", "D"};

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("TRUNCATE table  log_census");
            ps.executeUpdate();
            for (int i = 0; i < grp.length; i++) {
                String grpName = grp[i];
                ps6 = con.prepareStatement("SELECT department_code,department_name FROM g_department WHERE if_active='Y'  ORDER BY department_name ");
                rs6 = ps6.executeQuery();

                while (rs6.next()) {
                    deptName = rs6.getString("department_name");
                    deptCode = rs6.getString("department_code");
                    ps = con.prepareStatement("select * from g_district where state_code='21' order by dist_name ");
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        distName = rs.getString("dist_name");
                        distCode = rs.getString("dist_code");
                        int total_Group_M_General = 0;
                        int total_Group_F_General = 0;
                        int total_Group_M_SC = 0;
                        int total_Group_F_SC = 0;
                        int total_Group_M_ST = 0;
                        int total_Group_F_ST = 0;
                        int total_Group_M_OBC = 0;
                        int total_Group_F_OBC = 0;
                        pst = con.prepareStatement("SELECT count(emp_mast.*) as total_strength,emp_mast.category,emp_mast.gender FROM emp_mast , aq_mast,g_office WHERE  \n"
                                + " emp_mast.emp_id=aq_mast.emp_code AND g_office.off_code=aq_mast.off_code  AND aq_mast.aq_month =? AND aq_mast.aq_year =?  AND aq_mast.dep_code=?  AND g_office.dist_code=? AND emp_mast.post_grp_type =? AND emp_mast.category !='' \n "
                                + " GROUP BY emp_mast.category,emp_mast.gender ORDER BY emp_mast.gender,emp_mast.category ");
                        pst.setInt(1, Integer.parseInt("02"));
                        pst.setInt(2, Integer.parseInt("2016"));
                        pst.setString(3, deptCode);
                        pst.setString(4, distCode);
                        pst.setString(5, grpName);
                        res3 = pst.executeQuery();
                        while (res3.next()) {
                            if (res3.getString("category").equals("GENERAL") && res3.getString("gender").equals("M")) {
                                total_Group_M_General = res3.getInt("total_strength");
                            }
                            if (res3.getString("category").equals("GENERAL") && res3.getString("gender").equals("F")) {
                                total_Group_F_General = res3.getInt("total_strength");
                            }

                            /**
                             * ************************************************************************************
                             */
                            if (res3.getString("category").equals("SC") && res3.getString("gender").equals("M")) {
                                total_Group_M_SC = res3.getInt("total_strength");
                            }
                            if (res3.getString("category").equals("SC") && res3.getString("gender").equals("F")) {
                                total_Group_F_SC = res3.getInt("total_strength");
                            }

                            /**
                             * ************************************************************************************
                             */
                            if (res3.getString("category").equals("ST") && res3.getString("gender").equals("M")) {
                                total_Group_M_ST = res3.getInt("total_strength");
                            }
                            if (res3.getString("category").equals("ST") && res3.getString("gender").equals("F")) {
                                total_Group_F_ST = res3.getInt("total_strength");
                            }

                            /**
                             * ************************************************************************************
                             */
                            if ((res3.getString("category").equals("OBC") || res3.getString("category").equals("SEBC")) && res3.getString("gender").equals("M")) {
                                total_Group_M_OBC = res3.getInt("total_strength");
                            }
                            if ((res3.getString("category").equals("OBC") || res3.getString("category").equals("SEBC"))) {
                                total_Group_F_OBC = res3.getInt("total_strength");
                            }

                        }
                        age_18_50 = 0;
                        ps1 = con.prepareStatement("SELECT count(emp_mast.*) as total_age,post_grp_type FROM emp_mast , aq_mast,g_office WHERE emp_mast.emp_id=aq_mast.emp_code AND g_office.off_code=aq_mast.off_code  \n"
                                + " AND aq_mast.aq_month =? AND aq_mast.aq_year =?  AND aq_mast.dep_code=?  AND g_office.dist_code=?   AND post_grp_type=? AND TO_CHAR(dob :: DATE, 'dd.mm.yyyy')  BETWEEN '01.04.1966' AND '31.03.1998'  \n "
                                + " GROUP BY post_grp_type ");
                        ps1.setInt(1, Integer.parseInt("02"));
                        ps1.setInt(2, Integer.parseInt("2016"));
                        ps1.setString(3, deptCode);
                        ps1.setString(4, distCode);
                        ps1.setString(5, grpName);
                        //  ps1.setString(6, grpName);
                        res1 = ps1.executeQuery();
                        while (res1.next()) {
                            age_18_50 = res1.getInt("total_age");
                        }

                        age_50_60 = 0;
                        ps1 = con.prepareStatement("SELECT count(emp_mast.*) as total_age,post_grp_type FROM emp_mast , aq_mast,g_office WHERE emp_mast.emp_id=aq_mast.emp_code AND g_office.off_code=aq_mast.off_code  \n"
                                + " AND aq_mast.aq_month =? AND aq_mast.aq_year =?  AND aq_mast.dep_code=?  AND g_office.dist_code=?   AND post_grp_type=? AND TO_CHAR(dob :: DATE, 'dd.mm.yyyy')  BETWEEN '01.04.1956' AND '31.03.1966'  \n "
                                + " GROUP BY post_grp_type ");
                        ps1.setInt(1, Integer.parseInt("02"));
                        ps1.setInt(2, Integer.parseInt("2016"));
                        ps1.setString(3, deptCode);
                        ps1.setString(4, distCode);
                        ps1.setString(5, grpName);
                        //  ps1.setString(6, grpName);
                        res1 = ps1.executeQuery();
                        while (res1.next()) {
                            age_50_60 = res1.getInt("total_age");
                        }
                        age_60 = 0;
                        ps1 = con.prepareStatement("SELECT count(emp_mast.*) as total_age,post_grp_type FROM emp_mast , aq_mast,g_office WHERE emp_mast.emp_id=aq_mast.emp_code AND g_office.off_code=aq_mast.off_code  \n"
                                + " AND aq_mast.aq_month =? AND aq_mast.aq_year =?  AND aq_mast.dep_code=?  AND g_office.dist_code=?   AND post_grp_type=? AND TO_CHAR(dob :: DATE, 'dd.mm.yyyy') < '01.04.1956'   \n "
                                + " GROUP BY post_grp_type ");
                        ps1.setInt(1, Integer.parseInt("02"));
                        ps1.setInt(2, Integer.parseInt("2016"));
                        ps1.setString(3, deptCode);
                        ps1.setString(4, distCode);
                        ps1.setString(5, grpName);
                        //  ps1.setString(6, grpName);
                        res1 = ps1.executeQuery();
                        while (res1.next()) {
                            age_60 = res1.getInt("total_age");
                        }

                        /**
                         * ***************************************************
                         * Total Emoluments per month ***********
                         */
                        gross_16k = 0;
                        ps1 = con.prepareStatement("SELECT count(emp_mast.*) as total_emp,post_grp_type FROM emp_mast , aq_mast,g_office WHERE emp_mast.emp_id=aq_mast.emp_code AND g_office.off_code=aq_mast.off_code  \n"
                                + " AND aq_mast.aq_month =? AND aq_mast.aq_year =?  AND aq_mast.dep_code=?  AND g_office.dist_code=?   AND post_grp_type=? AND gross_amt <= 16000   \n "
                                + " GROUP BY post_grp_type ");
                        ps1.setInt(1, Integer.parseInt("02"));
                        ps1.setInt(2, Integer.parseInt("2016"));
                        ps1.setString(3, deptCode);
                        ps1.setString(4, distCode);
                        ps1.setString(5, grpName);
                        //  ps1.setString(6, grpName);
                        res1 = ps1.executeQuery();
                        while (res1.next()) {
                            gross_16k = res1.getInt("total_emp");
                        }
                        gross_16k_25k = 0;
                        ps1 = con.prepareStatement("SELECT count(emp_mast.*) as total_emp,post_grp_type FROM emp_mast , aq_mast,g_office WHERE emp_mast.emp_id=aq_mast.emp_code AND g_office.off_code=aq_mast.off_code  \n"
                                + " AND aq_mast.aq_month =? AND aq_mast.aq_year =?  AND aq_mast.dep_code=?  AND g_office.dist_code=?   AND post_grp_type=? AND (gross_amt BETWEEN 16001 AND 25000 )    \n "
                                + " GROUP BY post_grp_type ");
                        ps1.setInt(1, Integer.parseInt("02"));
                        ps1.setInt(2, Integer.parseInt("2016"));
                        ps1.setString(3, deptCode);
                        ps1.setString(4, distCode);
                        ps1.setString(5, grpName);
                        //  ps1.setString(6, grpName);
                        res1 = ps1.executeQuery();
                        while (res1.next()) {
                            gross_16k_25k = res1.getInt("total_emp");
                        }
                        gross_25k_50k = 0;
                        ps1 = con.prepareStatement("SELECT count(emp_mast.*) as total_emp,post_grp_type FROM emp_mast , aq_mast,g_office WHERE emp_mast.emp_id=aq_mast.emp_code AND g_office.off_code=aq_mast.off_code  \n"
                                + " AND aq_mast.aq_month =? AND aq_mast.aq_year =?  AND aq_mast.dep_code=?  AND g_office.dist_code=?   AND post_grp_type=? AND (gross_amt BETWEEN 25001 AND 50000 )    \n "
                                + " GROUP BY post_grp_type ");
                        ps1.setInt(1, Integer.parseInt("02"));
                        ps1.setInt(2, Integer.parseInt("2016"));
                        ps1.setString(3, deptCode);
                        ps1.setString(4, distCode);
                        ps1.setString(5, grpName);
                        //  ps1.setString(6, grpName);
                        res1 = ps1.executeQuery();
                        while (res1.next()) {
                            gross_25k_50k = res1.getInt("total_emp");
                        }
                        gross_50k_100k = 0;
                        ps1 = con.prepareStatement("SELECT count(emp_mast.*) as total_emp,post_grp_type FROM emp_mast , aq_mast,g_office WHERE emp_mast.emp_id=aq_mast.emp_code AND g_office.off_code=aq_mast.off_code  \n"
                                + " AND aq_mast.aq_month =? AND aq_mast.aq_year =?  AND aq_mast.dep_code=?  AND g_office.dist_code=?   AND post_grp_type=? AND (gross_amt BETWEEN 50001 AND 100000 )    \n "
                                + " GROUP BY post_grp_type ");
                        ps1.setInt(1, Integer.parseInt("02"));
                        ps1.setInt(2, Integer.parseInt("2016"));
                        ps1.setString(3, deptCode);
                        ps1.setString(4, distCode);
                        ps1.setString(5, grpName);
                        //  ps1.setString(6, grpName);
                        res1 = ps1.executeQuery();
                        while (res1.next()) {
                            gross_50k_100k = res1.getInt("total_emp");
                        }

                        gross_100k = 0;
                        ps1 = con.prepareStatement("SELECT count(emp_mast.*) as total_emp,post_grp_type FROM emp_mast , aq_mast,g_office WHERE emp_mast.emp_id=aq_mast.emp_code AND g_office.off_code=aq_mast.off_code  \n"
                                + " AND aq_mast.aq_month =? AND aq_mast.aq_year =?  AND aq_mast.dep_code=?  AND g_office.dist_code=?   AND post_grp_type=? AND (gross_amt  > 100000 )    \n "
                                + " GROUP BY post_grp_type ");
                        ps1.setInt(1, Integer.parseInt("02"));
                        ps1.setInt(2, Integer.parseInt("2016"));
                        ps1.setString(3, deptCode);
                        ps1.setString(4, distCode);
                        ps1.setString(5, grpName);
                        //  ps1.setString(6, grpName);
                        res1 = ps1.executeQuery();
                        while (res1.next()) {
                            gross_100k = res1.getInt("total_emp");
                        }
                        
                        non_regular = 0;
                        ps1 = con.prepareStatement("SELECT count(emp_mast.*) as cnt_non_regular,post_grp_type FROM emp_mast , aq_mast,g_office WHERE emp_mast.emp_id=aq_mast.emp_code AND g_office.off_code=aq_mast.off_code  \n"
                                + " AND aq_mast.aq_month =? AND aq_mast.aq_year =?  AND aq_mast.dep_code=?  AND g_office.dist_code=?   AND post_grp_type=? AND is_regular='N'    \n "
                                + " GROUP BY post_grp_type ");
                        ps1.setInt(1, Integer.parseInt("02"));
                        ps1.setInt(2, Integer.parseInt("2016"));
                        ps1.setString(3, deptCode);
                        ps1.setString(4, distCode);
                        ps1.setString(5, grpName);
                        //  ps1.setString(6, grpName);
                        res1 = ps1.executeQuery();
                        while (res1.next()) {
                            non_regular = res1.getInt("cnt_non_regular");
                        }

                        /**
                         * ***************************************************************************************
                         */
                        int totalRecord = total_Group_M_General + total_Group_F_General + total_Group_M_SC + total_Group_F_SC + total_Group_F_ST + total_Group_M_ST + total_Group_M_OBC + total_Group_F_OBC;
                        int logId = CommonFunctions.getMaxCode(con, "log_census", "log_sensus_id");
                        ps5 = con.prepareStatement("INSERT INTO log_census (log_sensus_id,dept_name,dist_name,category,s_strength,sc_male,sc_female,st_male,st_female,general_male,general_female,obc_male,obc_female,age_18_50,age_50_60,age_60,gross_16k,gross_16k_25k,gross_25k_50k,gross_50k_100k,gross_100k,non_regular) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
                        ps5.setInt(1, logId);
                        ps5.setString(2, deptName);
                        ps5.setString(3, distName);
                        ps5.setString(4, grpName);
                        ps5.setInt(5, totalRecord);
                        ps5.setInt(6, total_Group_M_SC);
                        ps5.setInt(7, total_Group_F_SC);
                        ps5.setInt(8, total_Group_M_ST);
                        ps5.setInt(9, total_Group_F_ST);
                        ps5.setInt(10, total_Group_M_General);
                        ps5.setInt(11, total_Group_F_General);
                        ps5.setInt(12, total_Group_M_OBC);
                        ps5.setInt(13, total_Group_F_OBC);
                        ps5.setInt(14, age_18_50);
                        ps5.setInt(15, age_50_60);
                        ps5.setInt(16, age_60);
                        ps5.setInt(17, gross_16k);
                        ps5.setInt(18, gross_16k_25k);
                        ps5.setInt(19, gross_25k_50k);
                        ps5.setInt(20, gross_50k_100k);
                        ps5.setInt(21, gross_100k);
                         ps5.setInt(22, non_regular);
                        ps5.execute();

                    }

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
