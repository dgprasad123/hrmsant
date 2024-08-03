
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.SBMonitoring;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.Rent.QuarterBean;
import hrms.model.employee.EmployeeCadre;
import hrms.model.login.Users;
import hrms.model.master.SubstantivePost;
import hrms.model.report.superannuationProjectionReport.SuperannuationProjectionReportBean;
import hrms.model.servicebook.esbListBean;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 *
 * @author Manoj PC
 */
public class SBMonitoringDAOImpl implements SBMonitoringDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    @Override
    public List getSBDeptWiseList(String fromDate, String toDate) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List li = new ArrayList();

        try {
            con = this.repodataSource.getConnection();

            ps = con.prepareStatement("select EN.dept_code, D.department_name, count(distinct off_code) AS total_office, count(distinct emp_id) AS total_emp, count(*) AS total_entry FROM emp_notification EN INNER JOIN g_department D ON EN.dept_code = D.department_code \n"
                    + "WHERE doe BETWEEN ? AND ?"
                    + "GROUP BY EN.dept_code, D.department_name ORDER BY D.department_name");
            ps.setTimestamp(1, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate).getTime()));
            ps.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy H:m:s").parse(toDate + " 23:59:59").getTime()));
            rs = ps.executeQuery();
            while (rs.next()) {
                esbListBean eBean = new esbListBean();
                eBean.setDepartmentId(rs.getString("dept_code"));
                eBean.setDepartmentName(rs.getString("department_name"));
                eBean.setTotalOffice(rs.getString("total_office"));
                eBean.setTotalEmp(rs.getString("total_emp"));
                eBean.setTotalEntry(rs.getString("total_entry"));
                li.add(eBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getSBDeptWiseRetireList(String year, String deptCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List li = new ArrayList();

        try {
            con = this.repodataSource.getConnection();
            if (deptCode.equals("11")) {
                ps = con.prepareStatement("select D.department_code, D.department_name, (SELECT COUNT(*) FROM g_office WHERE department_code = D.department_code AND (is_ddo='Y' and off_status='F')) AS total_office"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN g_office GO ON EM.cur_off_code = GO.off_code WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND (is_ddo='Y' and off_status='F') and dos > '2022-12-31' and  (if_retired is null OR if_retired = 'N') AND GO.department_code = D.department_code) AS total_emp"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN g_office GO ON EM.cur_off_code = GO.off_code WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND (is_ddo='Y' and off_status='F') AND GO.department_code = D.department_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' and  (if_retired is null OR if_retired = 'N') ) AS num_retiring"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN g_office GO ON EM.cur_off_code = GO.off_code WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND (is_ddo='Y' and off_status='F') AND GO.department_code = D.department_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' AND is_sb_update_completed = 'Y' and  (if_retired is null OR if_retired = 'N') ) AS num_sb_completed"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN g_office GO ON EM.cur_off_code = GO.off_code WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND (is_ddo='Y' and off_status='F') AND GO.department_code = D.department_code AND dos > '2023-12-31' AND is_sb_update_completed = 'Y' and  (if_retired is null OR if_retired = 'N') ) AS num_sb_completed_after"
                        + " from g_department D WHERE D.if_active = 'Y' ORDER BY D.department_name");
            } else {
                ps = con.prepareStatement("select D.department_code, D.department_name, (SELECT COUNT(*) FROM g_office WHERE department_code = D.department_code AND (is_ddo='Y' and off_status='F')) AS total_office"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN g_office GO ON EM.cur_off_code = GO.off_code WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND (is_ddo='Y' and off_status='F') and dos > '2022-12-31' and  (if_retired is null OR if_retired = 'N') AND GO.department_code = D.department_code) AS total_emp"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN g_office GO ON EM.cur_off_code = GO.off_code WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND (is_ddo='Y' and off_status='F') AND GO.department_code = D.department_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' and  (if_retired is null OR if_retired = 'N') ) AS num_retiring"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN g_office GO ON EM.cur_off_code = GO.off_code WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND (is_ddo='Y' and off_status='F') AND GO.department_code = D.department_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' AND is_sb_update_completed = 'Y' and  (if_retired is null OR if_retired = 'N') ) AS num_sb_completed"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN g_office GO ON EM.cur_off_code = GO.off_code WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND (is_ddo='Y' and off_status='F') AND GO.department_code = D.department_code AND dos > '2023-12-31' AND is_sb_update_completed = 'Y' and  (if_retired is null OR if_retired = 'N') ) AS num_sb_completed_after"
                        + " from g_department D WHERE D.if_active = 'Y' AND D.department_code = '" + deptCode + "' ORDER BY D.department_name");

            }
            rs = ps.executeQuery();
            while (rs.next()) {
                esbListBean eBean = new esbListBean();
                eBean.setDepartmentId(rs.getString("department_code"));
                eBean.setDepartmentName(rs.getString("department_name"));
                eBean.setTotalOffice(rs.getString("total_office"));
                eBean.setTotalEmp(rs.getString("total_emp"));
                eBean.setRetiredEmp(rs.getString("num_retiring"));
                eBean.setSbCompleted(rs.getString("num_sb_completed"));

                eBean.setSbCompletedAfter(rs.getString("num_sb_completed_after"));
                li.add(eBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getSBDistrictWiseRetireList(String year, String deptCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List li = new ArrayList();

        try {
            con = this.repodataSource.getConnection();
            if (deptCode.equals("11")) {
                ps = con.prepareStatement("SELECT T1.DIST_NAME,T1.DIST_CODE,T2.TOTAL_OFFICE,T1.TOTAL_EMP,T1.NUM_RETIRING,T1.NUM_SB_COMPLETED,T1.NUM_SB_COMPLETED_AFTER FROM \n"
                        + "(SELECT D.DIST_NAME, D.DIST_CODE \n"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.CUR_OFF_CODE = GO.OFF_CODE WHERE GO.DIST_CODE = D.DIST_CODE AND (IS_DDO='Y' AND OFF_STATUS='F') AND (IS_REGULAR = 'Y' OR IS_REGULAR = 'C' OR IS_REGULAR = 'G' OR IS_REGULAR = 'W') AND DEP_CODE NOT IN('00', '08', '09', '10', '13', '14','06') AND DOS > '2022-12-31' AND  (IF_RETIRED IS NULL OR IF_RETIRED = 'N')) AS TOTAL_EMP\n"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.CUR_OFF_CODE = GO.OFF_CODE WHERE (IS_REGULAR = 'Y' OR IS_REGULAR = 'C' OR IS_REGULAR = 'G' OR IS_REGULAR = 'W') AND DEP_CODE NOT IN('00', '08', '09', '10', '13', '14','06') AND (IS_DDO='Y' AND OFF_STATUS='F') AND GO.DIST_CODE = D.DIST_CODE AND DOS BETWEEN '2023-01-01' AND '2023-12-31' AND  (IF_RETIRED IS NULL OR IF_RETIRED = 'N') ) AS NUM_RETIRING\n"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.CUR_OFF_CODE = GO.OFF_CODE WHERE (IS_REGULAR = 'Y' OR IS_REGULAR = 'C' OR IS_REGULAR = 'G' OR IS_REGULAR = 'W') AND DEP_CODE NOT IN('00', '08', '09', '10', '13', '14','06') AND (IS_DDO='Y' AND OFF_STATUS='F') AND GO.DIST_CODE = D.DIST_CODE AND DOS BETWEEN '2023-01-01' AND '2023-12-31' AND IS_SB_UPDATE_COMPLETED = 'Y' AND  (IF_RETIRED IS NULL OR IF_RETIRED = 'N') ) AS NUM_SB_COMPLETED\n"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.CUR_OFF_CODE = GO.OFF_CODE WHERE (IS_REGULAR = 'Y' OR IS_REGULAR = 'C' OR IS_REGULAR = 'G' OR IS_REGULAR = 'W') AND DEP_CODE NOT IN('00', '08', '09', '10', '13', '14','06') AND (IS_DDO='Y' AND OFF_STATUS='F') AND GO.DIST_CODE = D.DIST_CODE AND DOS > '2023-12-31' AND IS_SB_UPDATE_COMPLETED = 'Y' AND  (IF_RETIRED IS NULL OR IF_RETIRED = 'N') ) AS NUM_SB_COMPLETED_AFTER\n"
                        + "FROM G_DISTRICT D WHERE D.STATE_CODE = '21' AND D.DIST_CODE NOT IN('2133', '2134', '2132','2135','2100','2131','2136','2137','2138','2139','2140') ORDER BY D.DIST_NAME)T1\n"
                        + "INNER JOIN \n"
                        + "(SELECT GD.DIST_CODE,COUNT(*) TOTAL_OFFICE FROM\n"
                        + "(SELECT OFF_CODE,DIST_CODE FROM \n"
                        + "(SELECT * FROM G_OFFICE WHERE (IS_DDO='Y' AND OFF_STATUS='F')) GO\n"
                        + "INNER JOIN (SELECT * FROM G_DEPARTMENT WHERE IF_ACTIVE = 'Y')GD\n"
                        + "ON GO.DEPARTMENT_CODE=GD.DEPARTMENT_CODE)D\n"
                        + "INNER JOIN (SELECT * FROM G_DISTRICT WHERE STATE_CODE='21') GD ON GD.DIST_CODE=D.DIST_CODE GROUP BY GD.DIST_CODE)T2\n"
                        + "ON T1.DIST_CODE=T2.DIST_CODE order by T1.DIST_NAME");
            } else {
                ps = con.prepareStatement("SELECT T1.DIST_NAME,T1.DIST_CODE,T2.TOTAL_OFFICE,T1.TOTAL_EMP,T1.NUM_RETIRING,T1.NUM_SB_COMPLETED,T1.NUM_SB_COMPLETED_AFTER FROM \n"
                        + "(SELECT D.DIST_NAME, D.DIST_CODE\n"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.CUR_OFF_CODE = GO.OFF_CODE WHERE GO.DIST_CODE = D.DIST_CODE AND DEPARTMENT_CODE = '" + deptCode + "' AND (IS_DDO='Y' AND OFF_STATUS='F') AND (IS_REGULAR = 'Y' OR IS_REGULAR = 'C' OR IS_REGULAR = 'G' OR IS_REGULAR = 'W') AND DEP_CODE NOT IN('00', '08', '09', '10', '13', '14','06') AND DOS > '2022-12-31' AND  (IF_RETIRED IS NULL OR IF_RETIRED = 'N')) AS TOTAL_EMP\n"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.CUR_OFF_CODE = GO.OFF_CODE WHERE (IS_REGULAR = 'Y' OR IS_REGULAR = 'C' OR IS_REGULAR = 'G' OR IS_REGULAR = 'W') AND DEP_CODE NOT IN('00', '08', '09', '10', '13', '14','06')AND (IS_DDO='Y' AND OFF_STATUS='F') AND GO.DIST_CODE = D.DIST_CODE AND DEPARTMENT_CODE = '" + deptCode + "' AND DOS BETWEEN '2023-01-01' AND '2023-12-31' AND  (IF_RETIRED IS NULL OR IF_RETIRED = 'N') ) AS NUM_RETIRING\n"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.CUR_OFF_CODE = GO.OFF_CODE WHERE (IS_REGULAR = 'Y' OR IS_REGULAR = 'C' OR IS_REGULAR = 'G' OR IS_REGULAR = 'W') AND DEP_CODE NOT IN('00', '08', '09', '10', '13', '14','06')AND (IS_DDO='Y' AND OFF_STATUS='F') AND GO.DIST_CODE = D.DIST_CODE AND DEPARTMENT_CODE = '" + deptCode + "' AND DOS BETWEEN '2023-01-01' AND '2023-12-31' AND IS_SB_UPDATE_COMPLETED = 'Y' AND  (IF_RETIRED IS NULL OR IF_RETIRED = 'N') ) AS NUM_SB_COMPLETED\n"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.CUR_OFF_CODE = GO.OFF_CODE WHERE (IS_REGULAR = 'Y' OR IS_REGULAR = 'C' OR IS_REGULAR = 'G' OR IS_REGULAR = 'W') AND DEP_CODE NOT IN('00', '08', '09', '10', '13', '14','06')AND (IS_DDO='Y' AND OFF_STATUS='F') AND GO.DIST_CODE = D.DIST_CODE AND DEPARTMENT_CODE = '" + deptCode + "' AND DOS > '2023-12-31' AND IS_SB_UPDATE_COMPLETED = 'Y' AND  (IF_RETIRED IS NULL OR IF_RETIRED = 'N') ) AS NUM_SB_COMPLETED_AFTER\n"
                        + "FROM G_DISTRICT D WHERE D.STATE_CODE = '21' AND D.DIST_CODE NOT IN('2133', '2134', '2132','2135','2100','2131','2136','2137','2138','2139','2140')  ORDER BY D.DIST_NAME)T1\n"
                        + "INNER JOIN \n"
                        + "(SELECT GD.DIST_CODE,COUNT(*) TOTAL_OFFICE FROM\n"
                        + "(SELECT OFF_CODE,DIST_CODE FROM \n"
                        + "(SELECT * FROM G_OFFICE WHERE (IS_DDO='Y' AND OFF_STATUS='F') AND DEPARTMENT_CODE = '" + deptCode + "' ) GO\n"
                        + "INNER JOIN (SELECT * FROM G_DEPARTMENT WHERE IF_ACTIVE = 'Y')GD\n"
                        + "ON GO.DEPARTMENT_CODE=GD.DEPARTMENT_CODE)D\n"
                        + "INNER JOIN (SELECT * FROM G_DISTRICT WHERE STATE_CODE='21') GD ON GD.DIST_CODE=D.DIST_CODE GROUP BY GD.DIST_CODE)T2\n"
                        + "ON T1.DIST_CODE=T2.DIST_CODE ORDER BY T1.DIST_NAME");
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                esbListBean eBean = new esbListBean();
                eBean.setDistrictId(rs.getString("dist_code"));
                eBean.setDistrictName(rs.getString("dist_name"));
                eBean.setTotalOffice(rs.getString("total_office"));
                eBean.setTotalEmp(rs.getString("total_emp"));
                eBean.setRetiredEmp(rs.getString("num_retiring"));
                eBean.setSbCompleted(rs.getString("num_sb_completed"));
                eBean.setSbCompletedAfter(rs.getString("num_sb_completed_after"));
                li.add(eBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getSBOfficeWiseList(String fromDate, String toDate, String deptcode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List li = new ArrayList();

        try {
            con = this.repodataSource.getConnection();

            ps = con.prepareStatement("select D.off_code, D.off_en, count(distinct emp_id) AS total_emp, count(*) AS total_entry FROM emp_notification EN "
                    + " INNER JOIN g_office D ON EN.off_code = D.off_code"
                    + " WHERE (doe BETWEEN ? AND ?) AND dept_code = ?"
                    + " GROUP BY D.off_code, D.off_name ORDER BY D.off_name");
            ps.setTimestamp(1, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate).getTime()));
            ps.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy H:m:s").parse(toDate + " 23:59:59").getTime()));
            ps.setString(3, deptcode);
            rs = ps.executeQuery();
            while (rs.next()) {
                esbListBean eBean = new esbListBean();
                eBean.setOffCode(rs.getString("off_code"));
                eBean.setOfficeName(rs.getString("off_en"));
                eBean.setTotalEmp(rs.getString("total_emp"));
                eBean.setRetiredEmp(rs.getString("num_retiring"));
                eBean.setSbCompleted(rs.getString("num_sb_completed"));
                eBean.setSbNotCompleted(rs.getString("num_sb_not_completed"));
                li.add(eBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getSBOfficeWiseRetireList(String deptcode, String year) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List li = new ArrayList();

        try {
            con = this.repodataSource.getConnection();

            ps = con.prepareStatement("SELECT GO.off_en, GO.off_code, (SELECT COUNT(*) FROM EMP_MAST EM WHERE cur_off_code = GO.off_code AND (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') and dos > '2022-12-31' and  (if_retired is null OR if_retired = 'N')) AS total_emp"
                    + ", (SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GO.off_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' and  (if_retired is null OR if_retired = 'N') ) AS num_retiring"
                    + ", (SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GO.off_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' AND is_sb_update_completed = 'Y' and  (if_retired is null OR if_retired = 'N') ) AS num_sb_completed"
                    + ", (SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GO.off_code AND  dos > '2023-12-31' AND is_sb_update_completed = 'Y' and  (if_retired is null OR if_retired = 'N') ) AS num_sb_completed_after"
                    + " FROM g_office GO WHERE GO.department_code = ? AND (is_ddo='Y' and off_status='F' OR DDO_CODE IN ('ANGWAT017','BLGWAT011','CTCWAT007','DKLWAT004','JPRWAT005','JYRWAT005','KJRWAT009','KJRWAT010','KLDWAT015','MBJWAT013','MKGWAT009','MKGWAT008','NPRWAT008','SBPWAT021')) ORDER BY GO.off_en");
            ps.setString(1, deptcode);
            rs = ps.executeQuery();
            while (rs.next()) {
                esbListBean eBean = new esbListBean();
                eBean.setOffCode(rs.getString("off_code"));
                eBean.setOfficeName(rs.getString("off_en"));
                eBean.setTotalEmp(rs.getString("total_emp"));
                eBean.setRetiredEmp(rs.getString("num_retiring"));
                eBean.setSbCompleted(rs.getString("num_sb_completed"));
                eBean.setSbCompletedAfter(rs.getString("num_sb_completed_after"));
                eBean.setDepartmentId(deptcode);
                /*int sbNotUpdatedBefore=Integer.parseInt(rs.getString("total_emp"))-Integer.parseInt(rs.getString("num_sb_completed"));
                 eBean.setSbNotCompleted(Integer.toString(sbNotUpdatedBefore));
                 int sbNotUpdatedAfter=Integer.parseInt(rs.getString("total_emp"))-Integer.parseInt(rs.getString("num_sb_completed_after"));
                 eBean.setSbNotCompletedAfter(Integer.toString(sbNotUpdatedAfter));
                 int grandTotalSBUpdated=Integer.parseInt(eBean.getSbCompleted())+Integer.parseInt(eBean.getSbCompletedAfter());
                 eBean.setGrandTotalSBUpdated(grandTotalSBUpdated);
                
                 int grandTotalSBNotUpdated=Integer.parseInt(eBean.getSbNotCompleted())+Integer.parseInt(eBean.getSbNotCompletedAfter());
                 eBean.setGrandTotalSBNotUpdated(grandTotalSBNotUpdated);*/
                li.add(eBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getSBOfficeWiseDetailList(String deptcode, String year) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List li = new ArrayList();

        try {
            con = this.repodataSource.getConnection();

            ps = con.prepareStatement("SELECT GO.off_en, GO.off_code, (SELECT COUNT(*) FROM EMP_MAST EM WHERE cur_off_code = GO.off_code AND (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') and dos > '2022-12-31' and  (if_retired is null OR if_retired = 'N')) AS total_emp"
                    + ", (SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GO.off_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' and  (if_retired is null OR if_retired = 'N') ) AS num_retiring"
                    + ", (SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GO.off_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' and  (if_retired is null OR if_retired = 'N') AND is_sb_update_completed = 'Y' ) AS num_sb_completed"
                    + ", (SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GO.off_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' and  (if_retired is null OR if_retired = 'N') AND post_grp_type = 'A') AS gp_a_completed"
                    + ", (SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GO.off_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' and  (if_retired is null OR if_retired = 'N') AND post_grp_type = 'B') AS gp_b_completed"
                    + ", (SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GO.off_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' and  (if_retired is null OR if_retired = 'N') AND post_grp_type = 'C') AS gp_c_completed"
                    + ", (SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GO.off_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' and  (if_retired is null OR if_retired = 'N') AND post_grp_type = 'D') AS gp_d_completed"
                    + ", (SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GO.off_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' and  (if_retired is null OR if_retired = 'N') and (select count(*) from emp_notification where EM.emp_id = emp_notification.emp_id and is_validated='N')=0 and "
                    + "(select count(*) from emp_join where EM.emp_id = emp_join.emp_id and is_validated='N')=0 "
                    + "and (select count(*) from emp_train where EM.emp_id = emp_train.emp_id and is_validated='N')=0 "
                    + "and (select count(*) from EMP_SV where EM.emp_id = EMP_SV.emp_id and is_validated='N')=0) AS num_validated "
                    + " FROM g_office GO WHERE GO.department_code = ? AND (is_ddo='Y' and off_status='F') ORDER BY GO.off_en");

            ps.setString(1, deptcode);
            rs = ps.executeQuery();
            while (rs.next()) {
                esbListBean eBean = new esbListBean();
                eBean.setOffCode(rs.getString("off_code"));
                eBean.setOfficeName(rs.getString("off_en"));
                eBean.setTotalEmp(rs.getString("total_emp"));
                eBean.setRetiredEmp(rs.getString("num_retiring"));
                eBean.setSbCompleted(rs.getString("num_sb_completed"));
                eBean.setGroupATotal(rs.getString("gp_a_completed"));
                eBean.setGroupBTotal(rs.getString("gp_b_completed"));
                eBean.setGroupCTotal(rs.getString("gp_c_completed"));
                eBean.setGroupDTotal(rs.getString("gp_d_completed"));
                eBean.setNumValidated(rs.getString("num_validated"));
                li.add(eBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getCadreWiseRetireList(String year) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List li = new ArrayList();

        try {
            con = this.repodataSource.getConnection();

            ps = con.prepareStatement("SELECT 'OAS' AS off_en, '' AS off_code, (SELECT COUNT(*) FROM EMP_MAST EM WHERE cur_cadre_code IN('1103', '2668')) AS total_emp"
                    + ", (SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06')  AND dos BETWEEN '2023-01-01' AND '2023-12-31' AND cur_cadre_code IN('1103', '2668')) AS num_retiring"
                    + ", (SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06')  AND dos BETWEEN '2023-01-01' AND '2023-12-31' AND cur_cadre_code IN('1103', '2668') AND is_sb_update_completed = 'Y' ) AS num_sb_completed"
                    + ", (SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06')  AND  dos > '2023-12-31' AND is_sb_update_completed = 'Y' AND cur_cadre_code IN('1103', '2668')) AS num_sb_completed_after"
                    + "");
            rs = ps.executeQuery();
            while (rs.next()) {
                esbListBean eBean = new esbListBean();
                eBean.setOffCode(rs.getString("off_code"));
                eBean.setOfficeName(rs.getString("off_en"));
                eBean.setTotalEmp(rs.getString("total_emp"));
                eBean.setRetiredEmp(rs.getString("num_retiring"));
                eBean.setSbCompleted(rs.getString("num_sb_completed"));
                eBean.setSbCompletedAfter(rs.getString("num_sb_completed_after"));
                li.add(eBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getSBDistOfficeWiseRetireList(String distCode, String year, String deptCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List li = new ArrayList();

        try {
            con = this.repodataSource.getConnection();
            if (!deptCode.equals("11")) {
                ps = con.prepareStatement("SELECT T1.* FROM \n"
                        + "(SELECT GOA.off_en, GOA.off_code,GOA.DEPARTMENT_CODE, (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.cur_off_code = GO.off_code WHERE cur_off_code = GOA.off_code AND \n"
                        + "GO.department_code = '" + deptCode + "' and (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') \n"
                        + "and dos > '2022-12-31' and  (if_retired is null OR if_retired = 'N')) AS total_emp\n"
                        + ",(SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.cur_off_code = GO.off_code  WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') \n"
                        + " AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GOA.off_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' AND \n"
                        + " GO.department_code = '" + deptCode + "' and  (if_retired is null OR if_retired = 'N') ) AS num_retiring\n"
                        + ",(SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.cur_off_code = GO.off_code  WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') \n"
                        + "AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GOA.off_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' AND is_sb_update_completed = 'Y' \n"
                        + "AND GO.department_code = '" + deptCode + "' and  (if_retired is null OR if_retired = 'N') ) AS num_sb_completed\n"
                        + ",(SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.cur_off_code = GO.off_code  WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') \n"
                        + "AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GOA.off_code AND  dos > '2023-12-31' AND is_sb_update_completed = 'Y' AND GO.department_code = '" + deptCode + "' and  \n"
                        + "(if_retired is null OR if_retired = 'N') ) AS num_sb_completed_after\n"
                        + " FROM g_office GOA WHERE GOA.dist_code = ? AND GOA.department_code = '" + deptCode + "' AND ((is_ddo='Y' and off_status='F') OR \n"
                        + "DDO_CODE IN ('ANGWAT017','BLGWAT011','CTCWAT007','DKLWAT004','JPRWAT005','JYRWAT005','KJRWAT009','KJRWAT010','KLDWAT015','MBJWAT013','MKGWAT009','MKGWAT008','NPRWAT008','SBPWAT021')) )T1\n"
                        + "INNER JOIN  G_DEPARTMENT GD ON T1.DEPARTMENT_CODE=GD.DEPARTMENT_CODE\n"
                        + "WHERE GD.IF_ACTIVE='Y' ORDER BY T1.off_en,GD.DEPARTMENT_CODE");
            } else {
                ps = con.prepareStatement("SELECT T1.* FROM\n"
                        + "(SELECT GO.off_en, GO.off_code,GO.DEPARTMENT_CODE, (SELECT COUNT(*) FROM EMP_MAST EM WHERE cur_off_code = GO.off_code and (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06')and dos > '2022-12-31' and  (if_retired is null OR if_retired = 'N')) AS total_emp\n"
                        + ",(SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GO.off_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' and  (if_retired is null OR if_retired = 'N') ) AS num_retiring\n"
                        + ",(SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GO.off_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' AND is_sb_update_completed = 'Y' and  (if_retired is null OR if_retired = 'N') ) \n"
                        + "AS num_sb_completed\n"
                        + ",(SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') \n"
                        + "AND cur_off_code = GO.off_code AND  dos > '2023-12-31' AND is_sb_update_completed = 'Y' and  (if_retired is null OR if_retired = 'N') ) AS num_sb_completed_after\n"
                        + "FROM g_office GO WHERE GO.dist_code =? AND ((is_ddo='Y' and off_status='F') OR \n"
                        + "DDO_CODE IN ('ANGWAT017','BLGWAT011','CTCWAT007','DKLWAT004','JPRWAT005','JYRWAT005','KJRWAT009','KJRWAT010','KLDWAT015','MBJWAT013','MKGWAT009','MKGWAT008','NPRWAT008','SBPWAT021')))T1\n"
                        + "INNER JOIN G_DEPARTMENT GD ON T1.DEPARTMENT_CODE=GD.DEPARTMENT_CODE\n"
                        + "WHERE GD.IF_ACTIVE='Y' ORDER BY T1.off_en,GD.DEPARTMENT_CODE");

            }
            ps.setString(1, distCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                esbListBean eBean = new esbListBean();
                eBean.setOffCode(rs.getString("off_code"));
                eBean.setOfficeName(rs.getString("off_en"));
                eBean.setTotalEmp(rs.getString("total_emp"));
                eBean.setRetiredEmp(rs.getString("num_retiring"));
                eBean.setSbCompleted(rs.getString("num_sb_completed"));
                eBean.setSbCompletedAfter(rs.getString("num_sb_completed_after"));
                li.add(eBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getSBDCWiseRetireList(String distCode, String year) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List li = new ArrayList();

        try {
            con = this.repodataSource.getConnection();
            ps = con.prepareStatement("SELECT GO.off_en, GO.off_code, (SELECT COUNT(*) FROM EMP_MAST EM WHERE cur_off_code = GO.off_code AND (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06')  and dos > '2022-12-31' and  (if_retired is null OR if_retired = 'N')) AS total_emp"
                    + ", (SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GO.off_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' and  (if_retired is null OR if_retired = 'N') ) AS num_retiring"
                    + ", (SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GO.off_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' and  (if_retired is null OR if_retired = 'N') AND is_sb_update_completed = 'Y' ) AS num_sb_completed"
                    + ", (SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GO.off_code AND  dos > '2023-12-31' and  (if_retired is null OR if_retired = 'N') AND is_sb_update_completed = 'Y' ) AS num_sb_completed_after"
                    + " FROM g_office GO WHERE GO.dist_code = ? AND ((is_ddo='Y' and off_status='F') OR DDO_CODE IN ('ANGWAT017','BLGWAT011','CTCWAT007','DKLWAT004','JPRWAT005','JYRWAT005','KJRWAT009','KJRWAT010','KLDWAT015','MBJWAT013','MKGWAT009','MKGWAT008','NPRWAT008','SBPWAT021')) ORDER BY GO.off_en");

            ps.setString(1, distCode);
            rs = ps.executeQuery();
            System.out.println("test");
            while (rs.next()) {
                esbListBean eBean = new esbListBean();
                eBean.setOffCode(rs.getString("off_code"));
                eBean.setOfficeName(rs.getString("off_en"));
                eBean.setTotalEmp(rs.getString("total_emp"));
                eBean.setRetiredEmp(rs.getString("num_retiring"));
                eBean.setSbCompleted(rs.getString("num_sb_completed"));
                eBean.setSbCompletedAfter(rs.getString("num_sb_completed_after"));
                li.add(eBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getSBEmpWiseList(String fromDate, String toDate, String offCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List li = new ArrayList();

        try {
            con = this.repodataSource.getConnection();

            ps = con.prepareStatement("select EN.emp_id, ARRAY_TO_STRING(ARRAY[EM.INITIALS, EM.F_NAME, EM.M_NAME, EM.L_NAME], ' ') EMPNAME"
                    + ", GP.POST AS EMP_POST, count(*) AS total_entry FROM emp_notification EN"
                    + " LEFT OUTER JOIN EMP_MAST EM ON EM.emp_id = EN.emp_id LEFT OUTER JOIN G_SPC GS ON GS.spc = EM.cur_spc"
                    + "	LEFT OUTER JOIN g_post GP ON GP.post_code = GS.gpc"
                    + " WHERE doe BETWEEN ? AND ? AND EN.off_code = ?"
                    + " GROUP BY EN.emp_id, EM.INITIALS, EM.F_NAME, EM.M_NAME, EM.L_NAME,GP.POST ORDER BY emp_id");
            ps.setTimestamp(1, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate).getTime()));
            ps.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy H:m:s").parse(toDate + " 23:59:59").getTime()));
            ps.setString(3, offCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                esbListBean eBean = new esbListBean();
                eBean.setEmployeeName(rs.getString("EMPNAME"));
                eBean.setPostName(rs.getString("EMP_POST"));
                eBean.setTotalEntry(rs.getString("total_entry"));
                li.add(eBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getSBEmpWiseRetireList(String offCode, String year) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List li = new ArrayList();
        String sbCompleted = "No";
        try {
            con = this.repodataSource.getConnection();

            ps = con.prepareStatement("select EM.emp_id, ARRAY_TO_STRING(ARRAY[EM.INITIALS, EM.F_NAME, EM.M_NAME, EM.L_NAME], ' ') EMPNAME"
                    + ", GP.POST AS EMP_POST, is_sb_update_completed FROM EMP_MAST EM "
                    + " LEFT OUTER JOIN G_SPC GS ON GS.spc = EM.cur_spc"
                    + " LEFT OUTER JOIN g_post GP ON GP.post_code = GS.gpc\n"
                    + " WHERE dos BETWEEN '2023-01-01' AND '2023-12-31' AND EM.cur_off_code = ?"
                    + " ORDER BY ARRAY_TO_STRING(ARRAY[EM.INITIALS, EM.F_NAME, EM.M_NAME, EM.L_NAME], ' ')");
            ps.setString(1, offCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("is_sb_update_completed").equals("Y")) {
                    sbCompleted = "Yes";
                }
                esbListBean eBean = new esbListBean();
                eBean.setEmployeeName(rs.getString("EMPNAME"));
                eBean.setPostName(rs.getString("EMP_POST"));
                eBean.setIsSbCompleted(sbCompleted);
                li.add(eBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getSBOffWiseRetireList(String offCode, String year, String strType) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List li = new ArrayList();
        String sbCompleted = "No";
        try {
            con = this.repodataSource.getConnection();
            if (strType.equals("current")) {
                ps = con.prepareStatement("select EM.emp_id, ARRAY_TO_STRING(ARRAY[EM.INITIALS, EM.F_NAME, EM.M_NAME, EM.L_NAME], ' ') EMPNAME"
                        + ", GP.POST AS EMP_POST, is_sb_update_completed FROM EMP_MAST EM "
                        + " LEFT OUTER JOIN G_SPC GS ON GS.spc = EM.cur_spc"
                        + " LEFT OUTER JOIN g_post GP ON GP.post_code = GS.gpc\n"
                        + " WHERE dos BETWEEN '2023-01-01' AND '2023-12-31' "
                        + " AND EM.cur_off_code = ? AND is_sb_update_completed = 'Y' AND (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06')"
                        + " ORDER BY ARRAY_TO_STRING(ARRAY[EM.INITIALS, EM.F_NAME, EM.M_NAME, EM.L_NAME], ' ')");
            } else {
                ps = con.prepareStatement("select EM.emp_id, ARRAY_TO_STRING(ARRAY[EM.INITIALS, EM.F_NAME, EM.M_NAME, EM.L_NAME], ' ') EMPNAME"
                        + ", GP.POST AS EMP_POST, is_sb_update_completed FROM EMP_MAST EM "
                        + " LEFT OUTER JOIN G_SPC GS ON GS.spc = EM.cur_spc"
                        + " LEFT OUTER JOIN g_post GP ON GP.post_code = GS.gpc\n"
                        + " WHERE dos > '2023-12-31' "
                        + " AND EM.cur_off_code = ? AND is_sb_update_completed = 'Y' AND (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06')"
                        + " ORDER BY ARRAY_TO_STRING(ARRAY[EM.INITIALS, EM.F_NAME, EM.M_NAME, EM.L_NAME], ' ')");
            }
            ps.setString(1, offCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                sbCompleted = "No";
                if (rs.getString("is_sb_update_completed").equals("Y")) {
                    sbCompleted = "Yes";
                }
                esbListBean eBean = new esbListBean();
                eBean.setEmployeeName(rs.getString("EMPNAME"));
                eBean.setPostName(rs.getString("EMP_POST"));
                eBean.setIsSbCompleted(sbCompleted);
                li.add(eBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getSBDeptWiseRetireList(String offCode, String year, String strType) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List li = new ArrayList();
        String sbCompleted = "No";
        try {
            con = this.repodataSource.getConnection();
            if (strType.equals("current")) {
                ps = con.prepareStatement("select EM.emp_id, ARRAY_TO_STRING(ARRAY[EM.INITIALS, EM.F_NAME, EM.M_NAME, EM.L_NAME], ' ') EMPNAME"
                        + ", GP.POST AS EMP_POST, is_sb_update_completed FROM EMP_MAST EM "
                        + " LEFT OUTER JOIN G_SPC GS ON GS.spc = EM.cur_spc"
                        + " LEFT OUTER JOIN g_post GP ON GP.post_code = GS.gpc\n"
                        + " LEFT OUTER JOIN G_OFFICE GO ON EM.cur_off_code = GO.off_code"
                        + " WHERE dos BETWEEN '2023-01-01' AND '2023-12-31' "
                        + " AND EM.cur_off_code = ? AND is_sb_update_completed = 'Y' AND (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06')"
                        + " ORDER BY ARRAY_TO_STRING(ARRAY[EM.INITIALS, EM.F_NAME, EM.M_NAME, EM.L_NAME], ' ')");
            } else {
                ps = con.prepareStatement("select EM.emp_id, ARRAY_TO_STRING(ARRAY[EM.INITIALS, EM.F_NAME, EM.M_NAME, EM.L_NAME], ' ') EMPNAME"
                        + ", GP.POST AS EMP_POST, is_sb_update_completed FROM EMP_MAST EM "
                        + " LEFT OUTER JOIN G_SPC GS ON GS.spc = EM.cur_spc"
                        + " LEFT OUTER JOIN g_post GP ON GP.post_code = GS.gpc\n"
                        + " LEFT OUTER JOIN G_OFFICE GO ON EM.cur_off_code = GO.off_code"
                        + " WHERE dos > '2023-12-31' "
                        + " AND EM.cur_off_code = ? AND is_sb_update_completed = 'Y' AND (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06')"
                        + " ORDER BY ARRAY_TO_STRING(ARRAY[EM.INITIALS, EM.F_NAME, EM.M_NAME, EM.L_NAME], ' ')");
            }
            ps.setString(1, offCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("is_sb_update_completed").equals("Y")) {
                    sbCompleted = "Yes";
                }
                esbListBean eBean = new esbListBean();
                eBean.setEmployeeName(rs.getString("EMPNAME"));
                eBean.setPostName(rs.getString("EMP_POST"));
                eBean.setIsSbCompleted(sbCompleted);
                li.add(eBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getDOSEmpList(String offCode) {
        Connection con = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        List emplist = new ArrayList();
        try {
            con = this.repodataSource.getConnection();
            ps2 = con.prepareStatement("SELECT EMP_ID,DOS,GPF_NO,POST,DATE_OF_NINCR, EXTRACT(YEAR FROM EMP.dos)::text AS dos_year, is_sb_update_completed, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,F_NAME,PAY_DATE FROM EMP_MAST EMP "
                    + "   LEFT OUTER JOIN G_SPC GSPC ON EMP.CUR_SPC=GSPC.SPC "
                    + "   LEFT OUTER JOIN G_POST GPOST ON GSPC.GPC=GPOST.POST_CODE WHERE CUR_OFF_CODE IN(" + offCode + ") AND DOS > '2022-12-31'"
                    + "   AND (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'W' OR is_regular = 'G') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06')"
                    + "   ORDER BY DOS");

            /*System.out.println("SELECT EMP_ID,GPF_NO,POST,DATE_OF_NINCR, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,F_NAME,PAY_DATE FROM EMP_MAST EMP "
             + "   INNER JOIN G_SPC GSPC ON EMP.CUR_SPC=GSPC.SPC "
             + "   INNER JOIN G_POST GPOST ON GSPC.GPC=GPOST.POST_CODE AND CUR_OFF_CODE=? "
             + "   AND DATE_OF_NINCR < '"+year+"-"+(month+1)+"-01'  or DATE_OF_NINCR is null) ORDER BY F_NAME");*/
            //System.out.println("Year:"+year+" Month:"+month);
            //  ps2.setString(1, offCode);
            //ps2.setInt(2, year);
            rs = ps2.executeQuery();
            Users emp = null;
            SubstantivePost sup = null;
            while (rs.next()) {
                emp = new Users();
                emp.setEmpId(rs.getString("EMP_ID"));
                emp.setFullName(rs.getString("EMPNAME"));
                emp.setGpfno(rs.getString("GPF_NO"));
                emp.setDos(rs.getDate("DOS"));
                sup = new SubstantivePost();
                sup.setSpn(rs.getString("POST"));
                emp.setSubstantivePost(sup);
                emp.setHasPrivilages(rs.getString("is_sb_update_completed"));
                emp.setIfmsCode(rs.getString("dos_year"));
                emplist.add(emp);
                //System.out.println("Year:"+rs.getString("dos_year"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public void updateSBUpdateStatus(String empId, String sbStatus) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            String str = "UPDATE EMP_MAST SET is_sb_update_completed = '" + sbStatus + "'"
                    + " WHERE emp_id = ?";
            pst = con.prepareStatement(str);
            pst.setString(1, empId);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getSBOffWiseAllRetireList(String offCode, String year, String strType) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List li = new ArrayList();
        String sbCompleted = "No";
        try {
            con = this.repodataSource.getConnection();
            if (strType.equals("current")) {
                ps = con.prepareStatement("select EM.emp_id, ARRAY_TO_STRING(ARRAY[EM.INITIALS, EM.F_NAME, EM.M_NAME, EM.L_NAME], ' ') EMPNAME"
                        + ", GP.POST AS EMP_POST, is_sb_update_completed, dos, emp_id FROM EMP_MAST EM "
                        + " LEFT OUTER JOIN G_SPC GS ON GS.spc = EM.cur_spc"
                        + " LEFT OUTER JOIN g_post GP ON GP.post_code = GS.gpc\n"
                        + " WHERE dos BETWEEN '2023-01-01' AND '2023-12-31' "
                        + " AND EM.cur_off_code = ? AND (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06')"
                        + " ORDER BY ARRAY_TO_STRING(ARRAY[EM.INITIALS, EM.F_NAME, EM.M_NAME, EM.L_NAME], ' ')");

            } else if (strType.equals("total")) {
                ps = con.prepareStatement("select EM.emp_id, ARRAY_TO_STRING(ARRAY[EM.INITIALS, EM.F_NAME, EM.M_NAME, EM.L_NAME], ' ') EMPNAME"
                        + ", GP.POST AS EMP_POST, is_sb_update_completed, dos, emp_id FROM EMP_MAST EM "
                        + " LEFT OUTER JOIN G_SPC GS ON GS.spc = EM.cur_spc"
                        + " LEFT OUTER JOIN g_post GP ON GP.post_code = GS.gpc\n"
                        + " WHERE  "
                        + " EM.cur_off_code = ? AND (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06')  and dos > '2022-12-31' and  (if_retired is null OR if_retired = 'N')"
                        + " ORDER BY ARRAY_TO_STRING(ARRAY[EM.INITIALS, EM.F_NAME, EM.M_NAME, EM.L_NAME], ' ')");

            } else {
                ps = con.prepareStatement("select EM.emp_id, ARRAY_TO_STRING(ARRAY[EM.INITIALS, EM.F_NAME, EM.M_NAME, EM.L_NAME], ' ') EMPNAME"
                        + ", GP.POST AS EMP_POST, is_sb_update_completed, dos, emp_id FROM EMP_MAST EM "
                        + " LEFT OUTER JOIN G_SPC GS ON GS.spc = EM.cur_spc"
                        + " LEFT OUTER JOIN g_post GP ON GP.post_code = GS.gpc\n"
                        + " WHERE dos > '2023-12-31' "
                        + " AND EM.cur_off_code = ? AND (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06')"
                        + " ORDER BY ARRAY_TO_STRING(ARRAY[EM.INITIALS, EM.F_NAME, EM.M_NAME, EM.L_NAME], ' ')");
            }
            ps.setString(1, offCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                sbCompleted = "No";
                if (rs.getString("is_sb_update_completed").equals("Y")) {
                    sbCompleted = "Yes";
                }
                esbListBean eBean = new esbListBean();
                eBean.setEmployeeName(rs.getString("EMPNAME"));
                eBean.setPostName(rs.getString("EMP_POST"));
                eBean.setIsSbCompleted(sbCompleted);
                eBean.setHrmsId(rs.getString("emp_id"));
                eBean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("dos")));
                li.add(eBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public String getServiceBookValidationStatus(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String validationcomplete = "Y";
        try {
            con = this.repodataSource.getConnection();

            /*String sql = "select * from" +
             " (select emp_id,(select count(*) from emp_notification where if_visible='Y' AND is_validated='N' and (not_type<>'REINSTATEMENT' and not_type<>'SUSPENSION')and emp_id=EMP_MAST.emp_id)notification," +
             " (select count(*) from emp_relieve where if_visible='Y' and is_validated='N' and emp_id=EMP_MAST.emp_id)relieve," +
             " (select count(*) from emp_join where if_visible='Y' and is_validated='N' and emp_id=EMP_MAST.emp_id)joining," +
             " (select count(*) from emp_train where is_validated='N' and emp_id=EMP_MAST.emp_id)training," +
             " (select count(*) from emp_exam where is_validated='N' and emp_id=EMP_MAST.emp_id)exam," +
             " (select count(*) from emp_sv where is_validated='N' and emp_id=EMP_MAST.emp_id)sv," +
             " (select count(*) from EMP_REINSTATEMENT where is_validated='N' and emp_id=EMP_MAST.emp_id)reinstatement," +
             " (select count(*) from EMP_SUSPENSION where IF_VISIBLE='Y' and is_validated='N' and emp_id=EMP_MAST.emp_id)suspension," +
             " (select count(*) from EMP_PERMISSION where is_validated='N' and emp_id=EMP_MAST.emp_id)permissiondata," +
             " (select count(*) from EMP_QUALIFICATION where is_validated='N' and emp_id=EMP_MAST.emp_id)qualification," +
             " (select count(*) from EMP_MISC where is_validated='N' and emp_id=EMP_MAST.emp_id)misc" +
             " from EMP_MAST where emp_id=?)temp1";*/
            /*String sql = "select * from" +
             " (select emp_id,(select count(*) from emp_notification where if_visible='Y' AND is_validated='N' and" +
             " (NOT_TYPE!='CHNG_STRUCTURE' AND NOT_TYPE!='LEAVE' AND NOT_TYPE!='RESULT' AND NOT_TYPE!='COMPLIANCE' AND NOT_TYPE!='SC_NOTICE' AND NOT_TYPE!='INCREMENT' and NOT_TYPE!='LOAN_SANC' and not_type<>'REINSTATEMENT' and not_type<>'SUSPENSION') and emp_id=EMP_MAST.emp_id and (sb_description is not null and sb_description<>''))notification," +
             " (select count(*) from emp_incr inner join emp_notification on EMP_INCR.NOT_ID = emp_notification.NOT_ID where if_visible='Y' and is_validated='N' AND PRID IS NULL and EMP_INCR.emp_id=EMP_MAST.emp_id and emp_notification.emp_id=EMP_MAST.emp_id)incr," +
             " (select count(*) from emp_notification where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=EMP_MAST.emp_id AND LSOT_ID='01') AND EMP_NOTIFICATION.EMP_ID=EMP_MAST.emp_id and if_visible='Y' and is_validated='N' and STATUS = 'SANCTIONED AND AVAILED')leave_sanc," +
             " (select count(*) from emp_notification where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=EMP_MAST.emp_id AND LSOT_ID='02') AND EMP_NOTIFICATION.EMP_ID=EMP_MAST.emp_id and if_visible='Y' and is_validated='N' and (sb_description is not null and sb_description<>''))leave_surrender," +
             " (select count(*) from emp_notification where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=EMP_MAST.emp_id AND LSOT_ID!='01' AND LSOT_ID!='02') AND EMP_NOTIFICATION.EMP_ID=EMP_MAST.emp_id and if_visible='Y' and is_validated='N')other_leave," +
             " (select count(*) from emp_loan_sanc inner join EMP_NOTIFICATION on emp_loan_sanc.NOT_ID = emp_notification.NOT_ID where emp_loan_sanc.EMP_ID=EMP_MAST.emp_id and emp_notification.EMP_ID=EMP_MAST.emp_id and if_visible='Y' and is_validated='N' and (emp_loan_sanc.loan_tp='BICA' or emp_loan_sanc.loan_tp='CMPA' or emp_loan_sanc.loan_tp='MOPA' or emp_loan_sanc.loan_tp='MCA' or emp_loan_sanc.loan_tp='VE') and (sb_description is not null and sb_description<>''))loan," +
             " (select count(*) from emp_relieve where if_visible='Y' and not_id is not null and is_validated='N' AND NOT_TYPE!='CHNG_STRUCTURE' AND NOT_TYPE!='SUSPENSION' and emp_id=EMP_MAST.emp_id)relieve," +
             " (select count(*) from emp_join where if_visible='Y' and not_id is not null and is_validated='N' AND NOT_TYPE!='CHNG_STRUCTURE' AND NOT_TYPE!='ADDITIONAL_CHARGE' and emp_id=EMP_MAST.emp_id)joining," +
             " (select count(*) from emp_train where if_visible='Y' and is_validated='N' and emp_id=EMP_MAST.emp_id)training," +
             " (select count(*) from emp_exam where if_visible='Y' and is_validated='N' and emp_id=EMP_MAST.emp_id)exam," +
             " (select count(*) from emp_sv where is_validated='N' and emp_id=EMP_MAST.emp_id)sv," +
             " (select count(*) from EMP_REINSTATEMENT where is_validated='N' and not_id is not null and (sb_description is not null and sb_description<>'') and emp_id=EMP_MAST.emp_id)reinstatement," +
             " (select count(*) from EMP_SUSPENSION where IF_VISIBLE='Y' and is_validated='N' and not_id is not null and (sb_description is not null and sb_description<>'') and emp_id=EMP_MAST.emp_id and (HQ_FIX IS NULL OR HQ_FIX = ''))suspension," +
             " (select count(*) from EMP_PERMISSION where IF_VISIBLE='Y' and is_validated='N' and emp_id=EMP_MAST.emp_id)permissiondata" + 
             " from EMP_MAST where emp_id=?)temp1";*/
            String sql = "select * from"
                    + " (select emp_id,(select count(*) from emp_notification where if_visible='Y' AND is_validated='N' and"
                    + " (NOT_TYPE!='CHNG_STRUCTURE' AND NOT_TYPE!='LEAVE' AND NOT_TYPE!='RESULT' AND NOT_TYPE!='COMPLIANCE' AND NOT_TYPE!='SC_NOTICE' AND NOT_TYPE!='INCREMENT' and NOT_TYPE!='LOAN_SANC' and not_type<>'REINSTATEMENT' and not_type<>'SUSPENSION') and emp_id=EMP_MAST.emp_id and (sb_description is not null and sb_description<>''))notification,"
                    + " (select count(*) from emp_incr inner join emp_notification on EMP_INCR.NOT_ID = emp_notification.NOT_ID where if_visible='Y' and is_validated='N' AND PRID IS NULL and EMP_INCR.emp_id=EMP_MAST.emp_id and emp_notification.emp_id=EMP_MAST.emp_id)incr,"
                    + " (select count(*) from emp_notification where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=EMP_MAST.emp_id AND LSOT_ID='01') AND EMP_NOTIFICATION.EMP_ID=EMP_MAST.emp_id and if_visible='Y' and is_validated='N' and STATUS = 'SANCTIONED AND AVAILED')leave_sanc,"
                    + " (select count(*) from emp_notification where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=EMP_MAST.emp_id AND LSOT_ID='02') AND EMP_NOTIFICATION.EMP_ID=EMP_MAST.emp_id and if_visible='Y' and is_validated='N' and (sb_description is not null and sb_description<>''))leave_surrender,"
                    + " (select count(*) from emp_notification where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=EMP_MAST.emp_id AND LSOT_ID!='01' AND LSOT_ID!='02') AND EMP_NOTIFICATION.EMP_ID=EMP_MAST.emp_id and if_visible='Y' and is_validated='N')other_leave,"
                    + " (select count(*) from emp_loan_sanc inner join EMP_NOTIFICATION on emp_loan_sanc.NOT_ID = emp_notification.NOT_ID where emp_loan_sanc.EMP_ID=EMP_MAST.emp_id and emp_notification.EMP_ID=EMP_MAST.emp_id and if_visible='Y' and is_validated='N' and (emp_loan_sanc.loan_tp='BICA' or emp_loan_sanc.loan_tp='CMPA' or emp_loan_sanc.loan_tp='MOPA' or emp_loan_sanc.loan_tp='MCA' or emp_loan_sanc.loan_tp='VE') and (sb_description is not null and sb_description<>''))loan,"
                    + " (SELECT count(*)"
                    + " FROM EMP_NOTIFICATION NOTI"
                    + " LEFT OUTER JOIN (SELECT * FROM EMP_RELIEVE WHERE EMP_ID=EMP_MAST.emp_id AND (JOIN_ID IS NULL OR (JOIN_ID::TEXT NOT IN"
                    + " (SELECT JOIN_ID::TEXT FROM EMP_JOIN WHERE EMP_ID=EMP_MAST.emp_id AND IF_AD_CHARGE='Y'))))EMP_RELIEVE ON NOTI.NOT_ID=EMP_RELIEVE.NOT_ID"
                    + " WHERE EMP_RELIEVE.EMP_ID=EMP_MAST.emp_id and (NOTI.not_type ='REHABILITATION' or"
                    + " NOTI.not_type='ABSORPTION' or NOTI.not_type='REDEPLOYMENT' or NOTI.not_type='VALIDATION' or NOTI.not_type='LT_TRAINING' or"
                    + " NOTI.not_type='DEPUTATION' or NOTI.not_type ='TRANSFER' or NOTI.not_type ='PROMOTION' or NOTI.not_type ='REDESIGNATION' or"
                    + " NOTI.not_type ='RESIGNATION' or NOTI.not_type ='DECEASED' or NOTI.not_type ='RETIREMENT' or NOTI.not_type='ALLOT_CADRE' or (NOTI.not_type='LEAVE'"
                    + " AND NOTI.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_ID=EMP_MAST.emp_id AND IF_LONGTERM='Y')))"
                    + " and EMP_RELIEVE.IF_VISIBLE='Y' AND EMP_RELIEVE.emp_id=EMP_MAST.emp_id AND EMP_RELIEVE.is_validated='N' and EMP_RELIEVE.NOT_TYPE!='CHNG_STRUCTURE' AND EMP_RELIEVE.NOT_TYPE!='SUSPENSION' and EMP_RELIEVE.NOT_ID is not null)relieve,"
                    + " (SELECT count(*)"
                    + " FROM emp_notification"
                    + " left outer join emp_relieve reli on emp_notification.emp_id=reli.emp_id and"
                    + " emp_notification.not_id=reli.not_id and reli.join_id not in (Select join_id from emp_join where EMP_ID=EMP_MAST.emp_id AND if_ad_charge='Y')"
                    + " left outer join emp_join joi on emp_notification.emp_id=joi.emp_id and emp_notification.not_id=joi.not_id"
                    + " left outer join g_spc on joi.ent_auth=g_spc.spc where joi.IF_VISIBLE='Y' AND joi.is_validated='N' AND joi.emp_id=EMP_MAST.emp_id AND joi.NOT_TYPE!='CHNG_STRUCTURE' AND joi.NOT_TYPE!='ADDITIONAL_CHARGE' and"
                    + " (emp_notification.not_type='FIRST_APPOINTMENT' or emp_notification.not_type='REHABILITATION' or emp_notification.not_type='ABSORPTION' or emp_notification.not_type='REDEPLOYMENT' or"
                    + " emp_notification.not_type='VALIDATION' or emp_notification.not_type='LT_TRAINING' or emp_notification.not_type='DEPUTATION' or emp_notification.not_type='POSTING' or emp_notification.not_type='TRANSFER'"
                    + " or emp_notification.not_type='PROMOTION' or emp_notification.not_type='ADDITIONAL_CHARGE' or emp_notification.not_type='ALLOT_CADRE' or emp_notification.not_type='REDESIGNATION' or"
                    + " emp_notification.not_type='CHNG_STRUCTURE'))joining,"
                    + " (select count(*) from emp_train where if_visible='Y' and is_validated='N' and emp_id=EMP_MAST.emp_id)training,"
                    + " (select count(*) from emp_exam where if_visible='Y' and is_validated='N' and emp_id=EMP_MAST.emp_id)exam,"
                    + " (select count(*) from emp_sv where is_validated='N' and emp_id=EMP_MAST.emp_id)sv,"
                    + " (select count(*) from EMP_REINSTATEMENT where is_validated='N' and not_id is not null and (sb_description is not null and sb_description<>'') and emp_id=EMP_MAST.emp_id)reinstatement,"
                    + " (select count(*) from EMP_SUSPENSION where IF_VISIBLE='Y' and is_validated='N' and not_id is not null and (sb_description is not null and sb_description<>'') and emp_id=EMP_MAST.emp_id and (HQ_FIX IS NULL OR HQ_FIX = ''))suspension,"
                    + " (select count(*) from EMP_PERMISSION where IF_VISIBLE='Y' and is_validated='N' and emp_id=EMP_MAST.emp_id)permissiondata"
                    + " from EMP_MAST where emp_id=?)temp1";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("notification") > 0) {
                    validationcomplete = "N";
                }
                if (rs.getInt("incr") > 0) {
                    validationcomplete = "N";
                }
                if (rs.getInt("leave_sanc") > 0) {
                    validationcomplete = "N";
                }
                if (rs.getInt("leave_surrender") > 0) {
                    validationcomplete = "N";
                }
                if (rs.getInt("other_leave") > 0) {
                    validationcomplete = "N";
                }
                if (rs.getInt("loan") > 0) {
                    validationcomplete = "N";
                }
                if (rs.getInt("relieve") > 0) {
                    validationcomplete = "N";
                }
                if (rs.getInt("joining") > 0) {
                    validationcomplete = "N";
                }
                if (rs.getInt("training") > 0) {
                    validationcomplete = "N";
                }
                if (rs.getInt("exam") > 0) {
                    validationcomplete = "N";
                }
                if (rs.getInt("sv") > 0) {
                    validationcomplete = "N";
                }
                if (rs.getInt("reinstatement") > 0) {
                    validationcomplete = "N";
                }
                if (rs.getInt("suspension") > 0) {
                    validationcomplete = "N";
                }
                /*if(rs.getInt("hqfixation") > 0){
                 validationcomplete = "N";
                 }*/
                if (rs.getInt("permissiondata") > 0) {
                    validationcomplete = "N";
                }
                /*if(rs.getInt("qualification") > 0){
                 validationcomplete = "N";
                 }*/
                /*if(rs.getInt("misc") > 0){
                 validationcomplete = "N";
                 }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return validationcomplete;
    }

    @Override
    public String getOffCode(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String offCode = "";
        String offCodes = "";
        try {
            con = this.repodataSource.getConnection();

            String sql = "select * from g_office WHERE ddo_hrmsid = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                offCode = rs.getString("off_code");
                if (offCodes == "") {
                    offCodes += "'" + offCode;
                } else {
                    offCodes += "', '" + offCode;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return offCodes + "'";
    }

    @Override
    public void downloadDeptWiseSBUpdatedReport(OutputStream out, String fileName, WritableWorkbook workbook) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int row = 0;
        int rowCnt = 0;
        int allGrandTotalSBUpdated = 0;
        int allGrandTotalSBNotUpdated = 0;
        int allTotalEmployees = 0;

        try {
            con = this.dataSource.getConnection();
            WritableSheet sheet = workbook.createSheet("DeptWiseSBUpdated", 0);
            int heightInPoints = 44 * 20;
            //Cells cells = worksheet.getCells();

            WritableFont headformat1 = new WritableFont(WritableFont.ARIAL, 15, WritableFont.BOLD);
            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 13, WritableFont.BOLD);
            WritableFont titleFont4 = new WritableFont(WritableFont.ARIAL, 10);
            WritableCellFormat titleformat4 = new WritableCellFormat(titleFont4);
            titleformat4.setAlignment(Alignment.CENTRE);
            titleformat4.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat4.setVerticalAlignment(VerticalAlignment.CENTRE);
            titleformat4.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat4.setWrap(true);

            WritableCellFormat titleformat5 = new WritableCellFormat(titleFont4);
            titleformat5.setAlignment(Alignment.LEFT);
            titleformat5.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat5.setVerticalAlignment(VerticalAlignment.CENTRE);
            titleformat5.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat5.setWrap(true);

            WritableCellFormat headcell1 = new WritableCellFormat(headformat1);
            headcell1.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell1.setAlignment(Alignment.CENTRE);
            headcell1.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setBorder(Border.ALL, BorderLineStyle.THIN);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setWrap(true);

            WritableFont innerformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);

            WritableCellFormat innercell = new WritableCellFormat(innerformat);

            jxl.write.Number num = null;

            sheet.mergeCells(0, 0, 11, 0);
            sheet.setRowView(0, heightInPoints);
            Label label = new Label(0, 0, "Deptwise Service Book Updation Status Report", headcell1);//column,row
            sheet.addCell(label);

            row = row + 1;
            sheet.setColumnView(0, 10);
            label = new Label(0, row, "Sl No\n(1)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(1, 45);
            label = new Label(1, row, "Department Name\n(2)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(2, 35);
            label = new Label(2, row, "Total Office\n(3)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(3, 35);
            label = new Label(3, row, "Total Employees\n(4)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(4, 35);
            label = new Label(4, row, "Total Employees retiring on or before 31st December 2023\n(5)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(5, 35);
            label = new Label(5, row, "SB Update Completed before 31st December 2023\n(6)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(6, 35);
            label = new Label(6, row, "SB Update Not Completed before 31st December 2023\n(7)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(7, 35);
            label = new Label(7, row, "Total Employees retiring after 31st December 2023\n(8)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(8, 35);
            label = new Label(8, row, "SB Update Completed retiring after 31st December 2023\n(9)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(9, 35);
            label = new Label(9, row, "SB Update Not Completed retiring after 31st December 2023\n(10)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(10, 35);
            label = new Label(10, row, "Total SB Update Completed\n(Col.6+Col.9)\n(11)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(11, 35);
            label = new Label(11, row, "Total SB Update Not Completed\n(Col.7+Col.10)\n(12)", headcell);
            sheet.addCell(label);

            ps = con.prepareStatement("select D.department_code, D.department_name, (SELECT COUNT(*) FROM g_office WHERE department_code = D.department_code AND (is_ddo='Y' and off_status='F')) AS total_office, \n"
                    + "(SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN g_office GO ON EM.cur_off_code = GO.off_code WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') \n"
                    + " AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND (is_ddo='Y' and off_status='F') and dos > '2022-12-31' and  (if_retired is null OR if_retired = 'N') \n"
                    + " AND GO.department_code = D.department_code) AS total_emp,\n"
                    + "(SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN g_office GO ON EM.cur_off_code = GO.off_code WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') \n"
                    + " AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND (is_ddo='Y' and off_status='F') AND GO.department_code = D.department_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' and  \n"
                    + " (if_retired is null OR if_retired = 'N') ) AS num_retiring,\n"
                    + " (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN g_office GO ON EM.cur_off_code = GO.off_code WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') \n"
                    + " AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND (is_ddo='Y' and off_status='F') AND GO.department_code = D.department_code AND \n"
                    + " dos BETWEEN '2023-01-01' AND '2023-12-31' AND is_sb_update_completed = 'Y' and  (if_retired is null OR if_retired = 'N') ) AS num_sb_completed,\n"
                    + "(SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN g_office GO ON EM.cur_off_code = GO.off_code WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') \n"
                    + " AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND (is_ddo='Y' and off_status='F') AND GO.department_code = D.department_code AND dos > '2023-12-31' \n"
                    + " AND is_sb_update_completed = 'Y' and  (if_retired is null OR if_retired = 'N') ) AS num_sb_completed_after\n"
                    + "from g_department D WHERE D.if_active = 'Y'  ORDER BY D.department_name");
            rs = ps.executeQuery();
            while (rs.next()) {
                row = row + 1;
                rowCnt = rowCnt + 1;

                sheet.setColumnView(0, 10);

                label = new Label(0, row, Integer.toString(rowCnt), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(1, 45);
                label = new Label(1, row, rs.getString("department_name"), titleformat5);
                sheet.addCell(label);

                sheet.setColumnView(2, 15);
                label = new Label(2, row, rs.getString("total_office"), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(3, 20);
                label = new Label(3, row, rs.getString("total_emp"), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(4, 15);
                label = new Label(4, row, rs.getString("num_retiring"), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(5, 20);
                label = new Label(5, row, rs.getString("num_sb_completed"), titleformat4);
                sheet.addCell(label);

                int sbNotcompletedBefore = Integer.parseInt(rs.getString("num_retiring")) - Integer.parseInt(rs.getString("num_sb_completed"));
                sheet.setColumnView(6, 15);
                label = new Label(6, row, Integer.toString(sbNotcompletedBefore), titleformat4);
                sheet.addCell(label);

                int totemplretiringafter31stDec21 = Integer.parseInt(rs.getString("total_emp")) - Integer.parseInt(rs.getString("num_retiring"));
                sheet.setColumnView(7, 20);
                label = new Label(7, row, Integer.toString(totemplretiringafter31stDec21), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(8, 15);
                label = new Label(8, row, rs.getString("num_sb_completed_after"), titleformat4);
                sheet.addCell(label);
                int grandTotalSBCompleted = Integer.parseInt(rs.getString("num_sb_completed")) + Integer.parseInt(rs.getString("num_sb_completed_after"));

                int totalSbUpdateCompletedAfter31stDec21 = totemplretiringafter31stDec21 - Integer.parseInt(rs.getString("num_sb_completed_after"));
                sheet.setColumnView(9, 20);
                label = new Label(9, row, Integer.toString(totalSbUpdateCompletedAfter31stDec21), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(10, 25);
                label = new Label(10, row, Integer.toString(grandTotalSBCompleted), titleformat4);
                sheet.addCell(label);

                int grandTotalSbNotCompleted = sbNotcompletedBefore + totalSbUpdateCompletedAfter31stDec21;
                sheet.setColumnView(11, 25);
                label = new Label(11, row, Integer.toString(grandTotalSbNotCompleted), titleformat4);
                sheet.addCell(label);

                allGrandTotalSBUpdated = allGrandTotalSBUpdated + grandTotalSBCompleted;
                allGrandTotalSBNotUpdated = allGrandTotalSBNotUpdated + grandTotalSbNotCompleted;
                allTotalEmployees = allTotalEmployees + Integer.parseInt(rs.getString("total_emp"));

            }

            sheet.mergeCells(0, row + 1, 2, row + 1);

            sheet.setRowView(0, heightInPoints);
            Label label1 = new Label(0, row + 1, "Grand Total", headcell1);//column,row
            sheet.addCell(label1);

            Label label2 = new Label(3, row + 1, Integer.toString(allTotalEmployees), headcell1);//column,row
            sheet.addCell(label2);

            //sheet.unmergeCells(3, row + 1, 9, row + 1);
            for (int i = 4; i <= 9; i++) {
                Label label5 = new Label(i, row + 1, " ", headcell1);//column,row
                sheet.addCell(label5);
            }

            sheet.setColumnView(10, 15);
            Label label3 = new Label(10, row + 1, Integer.toString(allGrandTotalSBUpdated), headcell1);
            sheet.addCell(label3);

            sheet.setColumnView(11, 15);
            Label label4 = new Label(11, row + 1, Integer.toString(allGrandTotalSBNotUpdated), headcell1);
            sheet.addCell(label4);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);

        }
    }

    @Override
    public void downloadOfficeWiseSBUpdatedReport(String deptCode, OutputStream out, String fileName, WritableWorkbook workbook) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int row = 0;
        int rowCnt = 0;
        int allGrandTotalSBUpdated = 0;
        int allGrandTotalSBNotUpdated = 0;
        int allTotalEmployees = 0;

        try {
            con = this.dataSource.getConnection();
            WritableSheet sheet = workbook.createSheet("DeptWiseSBUpdated", 0);
            int heightInPoints = 44 * 20;
            //Cells cells = worksheet.getCells();

            WritableFont headformat1 = new WritableFont(WritableFont.ARIAL, 15, WritableFont.BOLD);
            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 13, WritableFont.BOLD);
            WritableFont titleFont4 = new WritableFont(WritableFont.ARIAL, 10);
            WritableCellFormat titleformat4 = new WritableCellFormat(titleFont4);
            titleformat4.setAlignment(Alignment.CENTRE);
            titleformat4.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat4.setVerticalAlignment(VerticalAlignment.CENTRE);
            titleformat4.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat4.setWrap(true);

            WritableCellFormat titleformat5 = new WritableCellFormat(titleFont4);
            titleformat5.setAlignment(Alignment.LEFT);
            titleformat5.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat5.setVerticalAlignment(VerticalAlignment.CENTRE);
            titleformat5.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat5.setWrap(true);

            WritableCellFormat headcell1 = new WritableCellFormat(headformat1);
            headcell1.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell1.setAlignment(Alignment.CENTRE);
            headcell1.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setBorder(Border.ALL, BorderLineStyle.THIN);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setWrap(true);

            WritableFont innerformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);

            WritableCellFormat innercell = new WritableCellFormat(innerformat);

            jxl.write.Number num = null;

            sheet.mergeCells(0, 0, 10, 0);
            sheet.setRowView(0, heightInPoints);
            Label label = new Label(0, 0, "OfficeWise Service Book Updation Status Report", headcell1);//column,row
            sheet.addCell(label);

            row = row + 1;
            sheet.setColumnView(0, 10);
            label = new Label(0, row, "Sl No\n(1)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(1, 45);
            label = new Label(1, row, "Office Name\n(2)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(2, 35);
            label = new Label(2, row, "Total Employees\n(3)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(3, 35);
            label = new Label(3, row, "Total Employees retiring on or before 31st December 2023\n(4)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(4, 35);
            label = new Label(4, row, "SB Update Completed before 31st December 2023\n(5)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(5, 35);
            label = new Label(5, row, "SB Update Not Completed before 31st December 2023\n(6)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(6, 35);
            label = new Label(6, row, "Total Employees retiring after 31st December 2023\n(7)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(7, 35);
            label = new Label(7, row, "SB Update Completed retiring after 31st December 2023\n(8)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(8, 35);
            label = new Label(8, row, "SB Update Not Completed retiring after 31st December 2023\n(9)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(9, 35);
            label = new Label(9, row, "Total SB Update Completed\n(Col.5+Col.8)\n(10)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(10, 35);
            label = new Label(10, row, "Total SB Update Not Completed\n(Col.6+Col.9)\n(11)", headcell);
            sheet.addCell(label);

            ps = con.prepareStatement("SELECT GO.off_en, GO.off_code, (SELECT COUNT(*) FROM EMP_MAST EM WHERE cur_off_code = GO.off_code AND (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') and dos > '2022-12-31' and  (if_retired is null OR if_retired = 'N')) AS total_emp\n"
                    + ", (SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GO.off_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' and  (if_retired is null OR if_retired = 'N') ) AS num_retiring\n"
                    + ", (SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GO.off_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' AND is_sb_update_completed = 'Y' and  \n"
                    + "(if_retired is null OR if_retired = 'N') ) AS num_sb_completed\n"
                    + ", (SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code\n"
                    + "NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GO.off_code AND  dos > '2023-12-31' AND is_sb_update_completed = 'Y' \n"
                    + "and  (if_retired is null OR if_retired = 'N') ) AS num_sb_completed_after\n"
                    + "FROM g_office GO WHERE GO.department_code =? AND (is_ddo='Y' and off_status='F' OR \n"
                    + "DDO_CODE IN ('ANGWAT017','BLGWAT011','CTCWAT007','DKLWAT004','JPRWAT005','JYRWAT005','KJRWAT009','KJRWAT010','KLDWAT015','MBJWAT013','MKGWAT009','MKGWAT008','NPRWAT008','SBPWAT021'))\n"
                    + "ORDER BY GO.off_en");
            ps.setString(1, deptCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                row = row + 1;
                rowCnt = rowCnt + 1;

                sheet.setColumnView(0, 10);

                label = new Label(0, row, Integer.toString(rowCnt), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(1, 45);
                label = new Label(1, row, rs.getString("off_en").concat(" (").concat(rs.getString("off_code")).concat(")"), titleformat5);
                sheet.addCell(label);

                sheet.setColumnView(2, 20);
                label = new Label(2, row, rs.getString("total_emp"), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(3, 15);
                label = new Label(3, row, rs.getString("num_retiring"), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(4, 20);
                label = new Label(4, row, rs.getString("num_sb_completed"), titleformat4);
                sheet.addCell(label);

                int sbNotcompletedBefore = Integer.parseInt(rs.getString("num_retiring")) - Integer.parseInt(rs.getString("num_sb_completed"));
                sheet.setColumnView(5, 15);
                label = new Label(5, row, Integer.toString(sbNotcompletedBefore), titleformat4);
                sheet.addCell(label);

                int totemplretiringafter31stDec21 = Integer.parseInt(rs.getString("total_emp")) - Integer.parseInt(rs.getString("num_retiring"));
                sheet.setColumnView(6, 20);
                label = new Label(6, row, Integer.toString(totemplretiringafter31stDec21), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(7, 15);
                label = new Label(7, row, rs.getString("num_sb_completed_after"), titleformat4);
                sheet.addCell(label);
                int grandTotalSBCompleted = Integer.parseInt(rs.getString("num_sb_completed")) + Integer.parseInt(rs.getString("num_sb_completed_after"));

                int totalSbUpdateCompletedAfter31stDec21 = totemplretiringafter31stDec21 - Integer.parseInt(rs.getString("num_sb_completed_after"));
                sheet.setColumnView(8, 20);
                label = new Label(8, row, Integer.toString(totalSbUpdateCompletedAfter31stDec21), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(9, 25);
                label = new Label(9, row, Integer.toString(grandTotalSBCompleted), titleformat4);
                sheet.addCell(label);

                int grandTotalSbNotCompleted = sbNotcompletedBefore + totalSbUpdateCompletedAfter31stDec21;
                sheet.setColumnView(10, 25);
                label = new Label(10, row, Integer.toString(grandTotalSbNotCompleted), titleformat4);
                sheet.addCell(label);

                allGrandTotalSBUpdated = allGrandTotalSBUpdated + grandTotalSBCompleted;
                allGrandTotalSBNotUpdated = allGrandTotalSBNotUpdated + grandTotalSbNotCompleted;
                allTotalEmployees = allTotalEmployees + Integer.parseInt(rs.getString("total_emp"));

            }

            sheet.mergeCells(0, row + 1, 1, row + 1);
            sheet.setRowView(0, heightInPoints);
            Label label1 = new Label(0, row + 1, "Grand Total", headcell1);//column,row
            sheet.addCell(label1);

            sheet.setColumnView(2, 15);
            Label label2 = new Label(2, row + 1, Integer.toString(allTotalEmployees), headcell1);//column,row
            sheet.addCell(label2);

            for (int i = 3; i < 9; i++) {
                Label label5 = new Label(i, row + 1, " ", headcell1);//column,row
                sheet.addCell(label5);
            }

            sheet.setColumnView(9, 15);
            Label label3 = new Label(9, row + 1, Integer.toString(allGrandTotalSBUpdated), headcell1);
            sheet.addCell(label3);

            sheet.setColumnView(10, 15);
            Label label4 = new Label(10, row + 1, Integer.toString(allGrandTotalSBNotUpdated), headcell1);
            sheet.addCell(label4);
            //sheet.mergeCells(row, row, row, row)

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);

        }
    }

    @Override
    public void downloadeDistrcitWiseSBUpdatedReport(String deptcode, OutputStream out, String fileName, WritableWorkbook workbook) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int row = 0;
        int rowCnt = 0;
        int allGrandTotalSBUpdated = 0;
        int allGrandTotalSBNotUpdated = 0;
        int allTotalEmployees = 0;

        try {
            con = this.dataSource.getConnection();
            WritableSheet sheet = workbook.createSheet("DistWiseSBUpdated", 0);
            int heightInPoints = 44 * 20;
            //Cells cells = worksheet.getCells();

            WritableFont headformat1 = new WritableFont(WritableFont.ARIAL, 15, WritableFont.BOLD);
            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 13, WritableFont.BOLD);
            WritableFont titleFont4 = new WritableFont(WritableFont.ARIAL, 10);
            WritableCellFormat titleformat4 = new WritableCellFormat(titleFont4);
            titleformat4.setAlignment(Alignment.CENTRE);
            titleformat4.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat4.setVerticalAlignment(VerticalAlignment.CENTRE);
            titleformat4.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat4.setWrap(true);

            WritableCellFormat titleformat5 = new WritableCellFormat(titleFont4);
            titleformat5.setAlignment(Alignment.LEFT);
            titleformat5.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat5.setVerticalAlignment(VerticalAlignment.CENTRE);
            titleformat5.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat5.setWrap(true);

            WritableCellFormat headcell1 = new WritableCellFormat(headformat1);
            headcell1.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell1.setAlignment(Alignment.CENTRE);
            headcell1.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setBorder(Border.ALL, BorderLineStyle.THIN);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setWrap(true);

            WritableFont innerformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);

            WritableCellFormat innercell = new WritableCellFormat(innerformat);

            jxl.write.Number num = null;

            sheet.mergeCells(0, 0, 11, 0);
            sheet.setRowView(0, heightInPoints);
            Label label = new Label(0, 0, "Districtwise Service Book Updation Status Report", headcell1);//column,row
            sheet.addCell(label);

            row = row + 1;
            sheet.setColumnView(0, 10);
            label = new Label(0, row, "Sl No\n(1)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(1, 35);
            label = new Label(1, row, "Distrcit Name\n(2)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(2, 35);
            label = new Label(2, row, "Total Office\n(3)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(3, 35);
            label = new Label(3, row, "Total Employees\n(4)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(4, 35);
            label = new Label(4, row, "Total Employees retiring on or before 31st December 2023\n(5)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(5, 35);
            label = new Label(5, row, "SB Update Completed before 31st December 2023\n(6)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(6, 35);
            label = new Label(6, row, "SB Update Not Completed before 31st December 2023\n(7)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(7, 35);
            label = new Label(7, row, "Total Employees retiring after 31st December 2023\n(8)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(8, 35);
            label = new Label(8, row, "SB Update Completed retiring after 31st December 2023\n(9)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(9, 35);
            label = new Label(9, row, "SB Update Not Completed retiring after 31st December 2023\n(10)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(10, 40);
            label = new Label(10, row, "Total SB Update Completed\n(Col.6+Col.9)\n(11)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(11, 40);
            label = new Label(11, row, "Total SB Update Not Completed\n(Col.7+Col.10)\n(12)", headcell);
            sheet.addCell(label);

            if (deptcode.equals("11")) {
                ps = con.prepareStatement("SELECT T1.DIST_NAME,T1.DIST_CODE,T2.TOTAL_OFFICE,T1.TOTAL_EMP,T1.NUM_RETIRING,T1.NUM_SB_COMPLETED,T1.NUM_SB_COMPLETED_AFTER FROM \n"
                        + "(SELECT D.DIST_NAME, D.DIST_CODE \n"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.CUR_OFF_CODE = GO.OFF_CODE WHERE GO.DIST_CODE = D.DIST_CODE AND (IS_DDO='Y' AND OFF_STATUS='F') AND (IS_REGULAR = 'Y' OR IS_REGULAR = 'C' OR IS_REGULAR = 'G' OR IS_REGULAR = 'W') AND DEP_CODE NOT IN('00', '08', '09', '10', '13', '14','06') AND DOS > '2022-12-31' AND  (IF_RETIRED IS NULL OR IF_RETIRED = 'N')) AS TOTAL_EMP\n"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.CUR_OFF_CODE = GO.OFF_CODE WHERE (IS_REGULAR = 'Y' OR IS_REGULAR = 'C' OR IS_REGULAR = 'G' OR IS_REGULAR = 'W') AND DEP_CODE NOT IN('00', '08', '09', '10', '13', '14','06') AND (IS_DDO='Y' AND OFF_STATUS='F') AND GO.DIST_CODE = D.DIST_CODE AND DOS BETWEEN '2023-01-01' AND '2023-12-31' AND  (IF_RETIRED IS NULL OR IF_RETIRED = 'N') ) AS NUM_RETIRING\n"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.CUR_OFF_CODE = GO.OFF_CODE WHERE (IS_REGULAR = 'Y' OR IS_REGULAR = 'C' OR IS_REGULAR = 'G' OR IS_REGULAR = 'W') AND DEP_CODE NOT IN('00', '08', '09', '10', '13', '14','06') AND (IS_DDO='Y' AND OFF_STATUS='F') AND GO.DIST_CODE = D.DIST_CODE AND DOS BETWEEN '2023-01-01' AND '2023-12-31' AND IS_SB_UPDATE_COMPLETED = 'Y' AND  (IF_RETIRED IS NULL OR IF_RETIRED = 'N') ) AS NUM_SB_COMPLETED\n"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.CUR_OFF_CODE = GO.OFF_CODE WHERE (IS_REGULAR = 'Y' OR IS_REGULAR = 'C' OR IS_REGULAR = 'G' OR IS_REGULAR = 'W') AND DEP_CODE NOT IN('00', '08', '09', '10', '13', '14','06') AND (IS_DDO='Y' AND OFF_STATUS='F') AND GO.DIST_CODE = D.DIST_CODE AND DOS > '2023-12-31' AND IS_SB_UPDATE_COMPLETED = 'Y' AND  (IF_RETIRED IS NULL OR IF_RETIRED = 'N') ) AS NUM_SB_COMPLETED_AFTER\n"
                        + "FROM G_DISTRICT D WHERE D.STATE_CODE = '21' AND D.DIST_CODE NOT IN('2133', '2134', '2132','2135','2100','2131','2136','2137','2138','2139','2140') ORDER BY D.DIST_NAME)T1\n"
                        + "INNER JOIN \n"
                        + "(SELECT GD.DIST_CODE,COUNT(*) TOTAL_OFFICE FROM\n"
                        + "(SELECT OFF_CODE,DIST_CODE FROM \n"
                        + "(SELECT * FROM G_OFFICE WHERE (IS_DDO='Y' AND OFF_STATUS='F')) GO\n"
                        + "INNER JOIN (SELECT * FROM G_DEPARTMENT WHERE IF_ACTIVE = 'Y')GD\n"
                        + "ON GO.DEPARTMENT_CODE=GD.DEPARTMENT_CODE)D\n"
                        + "INNER JOIN (SELECT * FROM G_DISTRICT WHERE STATE_CODE='21') GD ON GD.DIST_CODE=D.DIST_CODE GROUP BY GD.DIST_CODE)T2\n"
                        + "ON T1.DIST_CODE=T2.DIST_CODE ORDER BY T1.DIST_NAME");
            } else {
                ps = con.prepareStatement("SELECT T1.DIST_NAME,T1.DIST_CODE,T2.TOTAL_OFFICE,T1.TOTAL_EMP,T1.NUM_RETIRING,T1.NUM_SB_COMPLETED,T1.NUM_SB_COMPLETED_AFTER FROM \n"
                        + "(SELECT D.DIST_NAME, D.DIST_CODE\n"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.CUR_OFF_CODE = GO.OFF_CODE WHERE GO.DIST_CODE = D.DIST_CODE AND DEPARTMENT_CODE = '" + deptcode + "' AND (IS_DDO='Y' AND OFF_STATUS='F') AND (IS_REGULAR = 'Y' OR IS_REGULAR = 'C' OR IS_REGULAR = 'G' OR IS_REGULAR = 'W') AND DEP_CODE NOT IN('00', '08', '09', '10', '13', '14','06') AND DOS > '2022-12-31' AND  (IF_RETIRED IS NULL OR IF_RETIRED = 'N')) AS TOTAL_EMP\n"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.CUR_OFF_CODE = GO.OFF_CODE WHERE (IS_REGULAR = 'Y' OR IS_REGULAR = 'C' OR IS_REGULAR = 'G' OR IS_REGULAR = 'W') AND DEP_CODE NOT IN('00', '08', '09', '10', '13', '14','06')AND (IS_DDO='Y' AND OFF_STATUS='F') AND GO.DIST_CODE = D.DIST_CODE AND DEPARTMENT_CODE = '" + deptcode + "' AND DOS BETWEEN '2023-01-01' AND '2023-12-31' AND  (IF_RETIRED IS NULL OR IF_RETIRED = 'N') ) AS NUM_RETIRING\n"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.CUR_OFF_CODE = GO.OFF_CODE WHERE (IS_REGULAR = 'Y' OR IS_REGULAR = 'C' OR IS_REGULAR = 'G' OR IS_REGULAR = 'W') AND DEP_CODE NOT IN('00', '08', '09', '10', '13', '14','06')AND (IS_DDO='Y' AND OFF_STATUS='F') AND GO.DIST_CODE = D.DIST_CODE AND DEPARTMENT_CODE = '" + deptcode + "' AND DOS BETWEEN '2023-01-01' AND '2023-12-31' AND IS_SB_UPDATE_COMPLETED = 'Y' AND  (IF_RETIRED IS NULL OR IF_RETIRED = 'N') ) AS NUM_SB_COMPLETED\n"
                        + ", (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.CUR_OFF_CODE = GO.OFF_CODE WHERE (IS_REGULAR = 'Y' OR IS_REGULAR = 'C' OR IS_REGULAR = 'G' OR IS_REGULAR = 'W') AND DEP_CODE NOT IN('00', '08', '09', '10', '13', '14','06')AND (IS_DDO='Y' AND OFF_STATUS='F') AND GO.DIST_CODE = D.DIST_CODE AND DEPARTMENT_CODE = '" + deptcode + "' AND DOS > '2023-12-31' AND IS_SB_UPDATE_COMPLETED = 'Y' AND  (IF_RETIRED IS NULL OR IF_RETIRED = 'N') ) AS NUM_SB_COMPLETED_AFTER\n"
                        + "FROM G_DISTRICT D WHERE D.STATE_CODE = '21' AND D.DIST_CODE NOT IN('2133', '2134', '2132','2135','2100','2131','2136','2137','2138','2139','2140')  ORDER BY D.DIST_NAME)T1\n"
                        + "INNER JOIN \n"
                        + "(SELECT GD.DIST_CODE,COUNT(*) TOTAL_OFFICE FROM\n"
                        + "(SELECT OFF_CODE,DIST_CODE FROM \n"
                        + "(SELECT * FROM G_OFFICE WHERE (IS_DDO='Y' AND OFF_STATUS='F') AND DEPARTMENT_CODE = '" + deptcode + "' ) GO\n"
                        + "INNER JOIN (SELECT * FROM G_DEPARTMENT WHERE IF_ACTIVE = 'Y')GD\n"
                        + "ON GO.DEPARTMENT_CODE=GD.DEPARTMENT_CODE)D\n"
                        + "INNER JOIN (SELECT * FROM G_DISTRICT WHERE STATE_CODE='21') GD ON GD.DIST_CODE=D.DIST_CODE GROUP BY GD.DIST_CODE)T2\n"
                        + "ON T1.DIST_CODE=T2.DIST_CODE ORDER BY T1.DIST_NAME");
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                row = row + 1;
                rowCnt = rowCnt + 1;

                sheet.setColumnView(0, 10);

                label = new Label(0, row, Integer.toString(rowCnt), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(1, 35);
                label = new Label(1, row, rs.getString("dist_name"), titleformat5);
                sheet.addCell(label);

                sheet.setColumnView(2, 15);
                label = new Label(2, row, rs.getString("total_office"), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(3, 20);
                label = new Label(3, row, rs.getString("total_emp"), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(4, 15);
                label = new Label(4, row, rs.getString("num_retiring"), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(5, 20);
                label = new Label(5, row, rs.getString("num_sb_completed"), titleformat4);
                sheet.addCell(label);

                int sbNotcompletedBefore = Integer.parseInt(rs.getString("num_retiring")) - Integer.parseInt(rs.getString("num_sb_completed"));
                sheet.setColumnView(6, 15);
                label = new Label(6, row, Integer.toString(sbNotcompletedBefore), titleformat4);
                sheet.addCell(label);

                int totemplretiringafter31stDec21 = Integer.parseInt(rs.getString("total_emp")) - Integer.parseInt(rs.getString("num_retiring"));
                sheet.setColumnView(7, 20);
                label = new Label(7, row, Integer.toString(totemplretiringafter31stDec21), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(8, 15);
                label = new Label(8, row, rs.getString("num_sb_completed_after"), titleformat4);
                sheet.addCell(label);
                int grandTotalSBCompleted = Integer.parseInt(rs.getString("num_sb_completed")) + Integer.parseInt(rs.getString("num_sb_completed_after"));

                int totalSbUpdateCompletedAfter31stDec21 = totemplretiringafter31stDec21 - Integer.parseInt(rs.getString("num_sb_completed_after"));
                sheet.setColumnView(9, 20);
                label = new Label(9, row, Integer.toString(totalSbUpdateCompletedAfter31stDec21), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(10, 40);
                label = new Label(10, row, Integer.toString(grandTotalSBCompleted), titleformat4);
                sheet.addCell(label);

                int grandTotalSbNotCompleted = sbNotcompletedBefore + totalSbUpdateCompletedAfter31stDec21;
                sheet.setColumnView(11, 40);
                label = new Label(11, row, Integer.toString(grandTotalSbNotCompleted), titleformat4);
                sheet.addCell(label);

                allGrandTotalSBUpdated = allGrandTotalSBUpdated + grandTotalSBCompleted;
                allGrandTotalSBNotUpdated = allGrandTotalSBNotUpdated + grandTotalSbNotCompleted;
                allTotalEmployees = allTotalEmployees + Integer.parseInt(rs.getString("total_emp"));

            }

            sheet.mergeCells(0, row + 1, 2, row + 1);

            sheet.setRowView(0, heightInPoints);
            Label label1 = new Label(0, row + 1, "Grand Total", headcell1);//column,row
            sheet.addCell(label1);

            Label label2 = new Label(3, row + 1, Integer.toString(allTotalEmployees), headcell1);//column,row
            sheet.addCell(label2);

            //sheet.unmergeCells(3, row + 1, 9, row + 1);
            for (int i = 4; i <= 9; i++) {
                Label label5 = new Label(i, row + 1, " ", headcell1);//column,row
                sheet.addCell(label5);
            }

            sheet.setColumnView(10, 15);
            Label label3 = new Label(10, row + 1, Integer.toString(allGrandTotalSBUpdated), headcell1);
            sheet.addCell(label3);

            sheet.setColumnView(11, 15);
            Label label4 = new Label(11, row + 1, Integer.toString(allGrandTotalSBNotUpdated), headcell1);
            sheet.addCell(label4);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);

        }

    }

    @Override
    public void downloadeSBDistOfficeWiseRetireList(String deptcode, String distName, String distCode, String fileName, WritableWorkbook workbook) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int row = 0;
        int rowCnt = 0;
        int allGrandTotalSBUpdated = 0;
        int allGrandTotalSBNotUpdated = 0;
        int allTotalEmployees = 0;

        try {
            con = this.dataSource.getConnection();
            WritableSheet sheet = workbook.createSheet("DistOfficeWiseSBUpdated", 0);
            int heightInPoints = 44 * 20;
            //Cells cells = worksheet.getCells();

            WritableFont headformat1 = new WritableFont(WritableFont.ARIAL, 15, WritableFont.BOLD);
            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 13, WritableFont.BOLD);
            WritableFont titleFont4 = new WritableFont(WritableFont.ARIAL, 10);
            WritableCellFormat titleformat4 = new WritableCellFormat(titleFont4);
            titleformat4.setAlignment(Alignment.CENTRE);
            titleformat4.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat4.setVerticalAlignment(VerticalAlignment.CENTRE);
            titleformat4.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat4.setWrap(true);

            WritableCellFormat titleformat5 = new WritableCellFormat(titleFont4);
            titleformat5.setAlignment(Alignment.LEFT);
            titleformat5.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat5.setVerticalAlignment(VerticalAlignment.CENTRE);
            titleformat5.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat5.setWrap(true);

            WritableCellFormat headcell1 = new WritableCellFormat(headformat1);
            headcell1.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell1.setAlignment(Alignment.CENTRE);
            headcell1.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setBorder(Border.ALL, BorderLineStyle.THIN);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setWrap(true);

            WritableFont innerformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);

            WritableCellFormat innercell = new WritableCellFormat(innerformat);

            jxl.write.Number num = null;

            sheet.mergeCells(0, 0, 10, 0);
            sheet.setRowView(0, heightInPoints);
            Label label = new Label(0, 0, distName + " District Office Wise Service Book Updation Status Report", headcell1);//column,row
            sheet.addCell(label);

            row = row + 1;
            sheet.setColumnView(0, 10);
            label = new Label(0, row, "Sl No\n(1)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(1, 35);
            label = new Label(1, row, "Office Name\n(2)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(2, 35);
            label = new Label(2, row, "Total Employees\n(3)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(3, 35);
            label = new Label(3, row, "Total Employees retiring on or before 31st December 2023\n(4)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(4, 35);
            label = new Label(4, row, "SB Update Completed before 31st December 2023\n(5)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(5, 35);
            label = new Label(5, row, "SB Update Not Completed before 31st December 2023\n(6)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(6, 35);
            label = new Label(6, row, "Total Employees retiring after 31st December 2023\n(7)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(7, 35);
            label = new Label(7, row, "SB Update Completed retiring after 31st December 2023\n(8)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(8, 35);
            label = new Label(8, row, "SB Update Not Completed retiring after 31st December 2023\n(9)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(9, 40);
            label = new Label(9, row, "Total SB Update Completed\n(Col.5+Col.8)\n(10)", headcell);
            sheet.addCell(label);

            sheet.setColumnView(10, 40);
            label = new Label(10, row, "Total SB Update Not Completed\n(Col.6+Col.9)\n(11)", headcell);
            sheet.addCell(label);

            if (!deptcode.equals("11")) {
                ps = con.prepareStatement("SELECT T1.* FROM \n"
                        + "(SELECT GOA.off_en, GOA.off_code,GOA.DEPARTMENT_CODE, (SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.cur_off_code = GO.off_code WHERE cur_off_code = GOA.off_code AND \n"
                        + "GO.department_code = '" + deptcode + "' and (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06')\n"
                        + "and dos > '2022-12-31' and  (if_retired is null OR if_retired = 'N') and (IS_DDO='Y' AND OFF_STATUS='F')) AS total_emp\n"
                        + ",(SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.cur_off_code = GO.off_code  WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W')\n"
                        + "AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GOA.off_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' AND\n"
                        + "GO.department_code = '" + deptcode + "' and  (if_retired is null OR if_retired = 'N') and (IS_DDO='Y' AND OFF_STATUS='F') ) AS num_retiring\n"
                        + ",(SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.cur_off_code = GO.off_code  WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W')\n"
                        + "AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GOA.off_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' AND is_sb_update_completed = 'Y' \n"
                        + "AND GO.department_code = '" + deptcode + "' and  (if_retired is null OR if_retired = 'N') and (IS_DDO='Y' AND OFF_STATUS='F') ) AS num_sb_completed\n"
                        + ",(SELECT COUNT(*) FROM EMP_MAST EM INNER JOIN G_OFFICE GO ON EM.cur_off_code = GO.off_code  WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W')\n"
                        + "AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND cur_off_code = GOA.off_code AND  dos > '2023-12-31' AND is_sb_update_completed = 'Y' AND \n"
                        + "  GO.department_code = '01' and \n"
                        + "(if_retired is null OR if_retired = 'N') and (IS_DDO='Y' AND OFF_STATUS='F') ) AS num_sb_completed_after\n"
                        + "FROM g_office GOA WHERE GOA.dist_code = ? AND GOA.department_code = '" + deptcode + "' AND ((is_ddo='Y' and off_status='F') OR \n"
                        + "DDO_CODE IN ('ANGWAT017','BLGWAT011','CTCWAT007','DKLWAT004','JPRWAT005','JYRWAT005','KJRWAT009','KJRWAT010','KLDWAT015','MBJWAT013','MKGWAT009','MKGWAT008','NPRWAT008','SBPWAT021')) )T1\n"
                        + "INNER JOIN  G_DEPARTMENT GD ON T1.DEPARTMENT_CODE=GD.DEPARTMENT_CODE\n"
                        + "WHERE GD.IF_ACTIVE='Y' ORDER BY T1.off_en,GD.DEPARTMENT_CODE");
            } else {
                ps = con.prepareStatement("SELECT T1.* FROM\n"
                        + "(SELECT GO.off_en, GO.off_code,GO.DEPARTMENT_CODE, \n"
                        + "  (SELECT COUNT(*) FROM EMP_MAST EM WHERE cur_off_code = GO.off_code and (is_regular = 'Y' OR is_regular = 'C' \n"
                        + "OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06')and dos > '2022-12-31' and  (if_retired is null OR if_retired = 'N') \n"
                        + "and  (IS_DDO='Y' AND OFF_STATUS='F')) AS total_emp\n"
                        + ",(SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') \n"
                        + "AND cur_off_code = GO.off_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' and  (if_retired is null OR if_retired = 'N') ) AS num_retiring\n"
                        + ",(SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06') \n"
                        + "AND cur_off_code = GO.off_code AND dos BETWEEN '2023-01-01' AND '2023-12-31' AND is_sb_update_completed = 'Y' and  (if_retired is null OR if_retired = 'N') ) \n"
                        + "AS num_sb_completed\n"
                        + ",(SELECT COUNT(*) FROM EMP_MAST EM WHERE (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') AND dep_code NOT IN('00', '08', '09', '10', '13', '14','06')\n"
                        + "AND cur_off_code = GO.off_code AND (IS_DDO='Y' AND OFF_STATUS='F') and dos > '2023-12-31' AND is_sb_update_completed = 'Y' and  (if_retired is null OR if_retired = 'N') ) AS num_sb_completed_after\n"
                        + "FROM g_office GO WHERE GO.dist_code = ? AND ((IS_DDO='Y' AND OFF_STATUS='F') OR\n"
                        + "DDO_CODE IN ('ANGWAT017','BLGWAT011','CTCWAT007','DKLWAT004','JPRWAT005','JYRWAT005','KJRWAT009','KJRWAT010','KLDWAT015','MBJWAT013','MKGWAT009','MKGWAT008','NPRWAT008','SBPWAT021')))T1\n"
                        + "INNER JOIN G_DEPARTMENT GD ON T1.DEPARTMENT_CODE=GD.DEPARTMENT_CODE\n"
                        + "WHERE GD.IF_ACTIVE='Y' ORDER BY T1.off_en,GD.DEPARTMENT_CODE");
                ps.setString(1, distCode);

            }

            rs = ps.executeQuery();
            while (rs.next()) {
                row = row + 1;
                rowCnt = rowCnt + 1;

                sheet.setColumnView(0, 10);

                label = new Label(0, row, Integer.toString(rowCnt), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(1, 35);
                label = new Label(1, row, rs.getString("off_en"), titleformat5);
                sheet.addCell(label);

                sheet.setColumnView(2, 20);
                label = new Label(2, row, rs.getString("total_emp"), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(3, 15);
                label = new Label(3, row, rs.getString("num_retiring"), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(4, 20);
                label = new Label(4, row, rs.getString("num_sb_completed"), titleformat4);
                sheet.addCell(label);

                int sbNotcompletedBefore = Integer.parseInt(rs.getString("num_retiring")) - Integer.parseInt(rs.getString("num_sb_completed"));
                sheet.setColumnView(5, 15);
                label = new Label(5, row, Integer.toString(sbNotcompletedBefore), titleformat4);
                sheet.addCell(label);

                int totemplretiringafter31stDec21 = Integer.parseInt(rs.getString("total_emp")) - Integer.parseInt(rs.getString("num_retiring"));
                sheet.setColumnView(6, 20);
                label = new Label(6, row, Integer.toString(totemplretiringafter31stDec21), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(7, 15);
                label = new Label(7, row, rs.getString("num_sb_completed_after"), titleformat4);
                sheet.addCell(label);
                int grandTotalSBCompleted = Integer.parseInt(rs.getString("num_sb_completed")) + Integer.parseInt(rs.getString("num_sb_completed_after"));

                int totalSbUpdateCompletedAfter31stDec21 = totemplretiringafter31stDec21 - Integer.parseInt(rs.getString("num_sb_completed_after"));
                sheet.setColumnView(8, 20);
                label = new Label(8, row, Integer.toString(totalSbUpdateCompletedAfter31stDec21), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(9, 40);
                label = new Label(9, row, Integer.toString(grandTotalSBCompleted), titleformat4);
                sheet.addCell(label);

                int grandTotalSbNotCompleted = sbNotcompletedBefore + totalSbUpdateCompletedAfter31stDec21;
                sheet.setColumnView(10, 40);
                label = new Label(10, row, Integer.toString(grandTotalSbNotCompleted), titleformat4);
                sheet.addCell(label);

                allGrandTotalSBUpdated = allGrandTotalSBUpdated + grandTotalSBCompleted;
                allGrandTotalSBNotUpdated = allGrandTotalSBNotUpdated + grandTotalSbNotCompleted;
                allTotalEmployees = allTotalEmployees + Integer.parseInt(rs.getString("total_emp"));

            }

            sheet.mergeCells(0, row + 1, 1, row + 1);

            sheet.setRowView(0, heightInPoints);
            Label label1 = new Label(0, row + 1, "Grand Total", headcell1);//column,row
            sheet.addCell(label1);

            Label label2 = new Label(2, row + 1, Integer.toString(allTotalEmployees), headcell1);//column,row
            sheet.addCell(label2);

            //sheet.unmergeCells(3, row + 1, 9, row + 1);
            for (int i = 3; i <= 9; i++) {
                Label label5 = new Label(i, row + 1, " ", headcell1);//column,row
                sheet.addCell(label5);
            }

            sheet.setColumnView(9, 15);
            Label label3 = new Label(9, row + 1, Integer.toString(allGrandTotalSBUpdated), headcell1);
            sheet.addCell(label3);

            sheet.setColumnView(10, 15);
            Label label4 = new Label(10, row + 1, Integer.toString(allGrandTotalSBNotUpdated), headcell1);
            sheet.addCell(label4);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);

        }
    }

    @Override
    public List getEmpCadreList(String deptCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        List li = new ArrayList();
        try {
            con = this.repodataSource.getConnection();
            ps = con.prepareStatement("select * FROM temp_emp_cadre_report WHERE department_code = ? AND cadre_code != 'N/A'");
            ps.setString(1, deptCode);
            //System.out.println("Department Code: "+deptCode);
            rs = ps.executeQuery();
            ps1 = con.prepareStatement("select * FROM temp_emp_cadre_report WHERE department_code = ? LIMIT 1");
            ps1.setString(1, deptCode);
            rs1 = ps1.executeQuery();
            String departmentName = "";
            while(rs1.next())
            {
                departmentName = rs1.getString("department_name");
            }
            int numEmployees = 0;
           Map<String, Integer> details = new HashMap<>();
            EmployeeCadre ec1 = new EmployeeCadre();
            ec1.setDepartmentName(departmentName);
            ec1.setCadreCode("-001");
           // li.add(ec1);
            int cadreTotal = 0;
            List li1 = new ArrayList();
            boolean flag = false;
            while (rs.next()) {
                EmployeeCadre ec = new EmployeeCadre();
                ec.setCadreName(rs.getString("cadre_name"));
                ec.setCadreCode(rs.getString("cadre_code"));
                
               cadreTotal = 0;
                    ec.setL1(rs.getString("level1") + "");
                    ec.setL2(rs.getString("level2") + "");
                    ec.setL3(rs.getString("level3") + "");
                    ec.setL4(rs.getString("level4") + "");
                    ec.setL5(rs.getString("level5") + "");
                    ec.setL6(rs.getString("level6") + "");
                    ec.setL7(rs.getString("level7") + "");
                    ec.setL8(rs.getString("level8") + "");
                    ec.setL9(rs.getString("level9") + "");
                    ec.setL10(rs.getString("level10") + "");
                    ec.setL11(rs.getString("level11") + "");
                    ec.setL12(rs.getString("level12") + "");
                    ec.setL13(rs.getString("level13") + "");
                    ec.setL14(rs.getString("level14") + "");
                    ec.setL15(rs.getString("level15") + "");
                    ec.setL16(rs.getString("level16") + "");
                    ec.setL17(rs.getString("level17") + "");
                    for(int p = 1; p <= 17; p++)
                    cadreTotal+= rs.getInt("level"+p);
                    ec.setTotal(cadreTotal);

                    //System.out.println("Val: "+details.get("level"+10));
                    li.add(ec);

                
            }

            ps1 = con.prepareStatement("select * FROM temp_emp_cadre_report WHERE department_code = ? AND cadre_code = 'N/A'");
            ps1.setString(1, deptCode);
            numEmployees = 0;
            rs1 = ps1.executeQuery();
            cadreTotal = 0;
            while(rs1.next()) {
                            EmployeeCadre ec = new EmployeeCadre();
            ec.setCadreName("No Cadre");
            ec.setCadreCode("N/A");
                cadreTotal = 0;
                    ec.setL1(rs1.getString("level1") + "");
                    ec.setL2(rs1.getString("level2") + "");
                    ec.setL3(rs1.getString("level3") + "");
                    ec.setL4(rs1.getString("level4") + "");
                    ec.setL5(rs1.getString("level5") + "");
                    ec.setL6(rs1.getString("level6") + "");
                    ec.setL7(rs1.getString("level7") + "");
                    ec.setL8(rs1.getString("level8") + "");
                    ec.setL9(rs1.getString("level9") + "");
                    ec.setL10(rs1.getString("level10") + "");
                    ec.setL11(rs1.getString("level11") + "");
                    ec.setL12(rs1.getString("level12") + "");
                    ec.setL13(rs1.getString("level13") + "");
                    ec.setL14(rs1.getString("level14") + "");
                    ec.setL15(rs1.getString("level15") + "");
                    ec.setL16(rs1.getString("level16") + "");
                    ec.setL17(rs1.getString("level17") + "");
                    for(int p = 1; p <= 17; p++)
                    cadreTotal+= rs1.getInt("level"+p);
                    ec.setTotal(cadreTotal);

                    //System.out.println("Val: "+details.get("level"+10));

            //System.out.println("Val: "+details.get("level"+10));
            li.add(ec);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

}
