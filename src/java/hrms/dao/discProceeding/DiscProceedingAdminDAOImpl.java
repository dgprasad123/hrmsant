/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.discProceeding;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.discProceeding.DPPendingReportBean;
import hrms.model.empinfo.EmployeeSearchResult;
import hrms.model.empinfo.SearchEmployee;
import hrms.model.employee.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Manas
 */
public class DiscProceedingAdminDAOImpl implements DiscProceedingAdminDAO {

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
    public EmployeeSearchResult locateEmployee(SearchEmployee searchEmployee) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        PreparedStatement countst = null;
        ArrayList employeeList = new ArrayList();
        Employee employee = null;
        String empStatus = null;
        EmployeeSearchResult empSearchResult = new EmployeeSearchResult();
        try {
            con = repodataSource.getConnection();

            int offSet = searchEmployee.getRows() * (searchEmployee.getPage() - 1);
            int limit = searchEmployee.getRows();

            if (searchEmployee.getOffcode() == null || searchEmployee.getOffcode().equals("")) {
                if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("GPFNO")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE gpf_no = ? ");

                    countst.setString(1, StringUtils.upperCase(searchEmployee.getSearchString()));
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE gpf_no = ?  order by f_name LIMIT " + limit + " OFFSET " + offSet);

                    st.setString(1, StringUtils.upperCase(searchEmployee.getSearchString()));
                } else if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("HRMSID")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE emp_id = ? ");
                    countst.setString(1, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE emp_id = ?  order by f_name ");
                    st.setString(1, searchEmployee.getSearchString());
                } else if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("MOBILE")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE MOBILE = ? ");
                    countst.setString(1, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE MOBILE = ? order by f_name");
                    st.setString(1, searchEmployee.getSearchString());
                } else if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("FNAME")) {
                    // if (searchEmployee.getOffcode() == null || searchEmployee.getOffcode().equals("")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE F_name LIKE  ? ");
                    countst.setString(1, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "  WHERE F_name LIKE  ? ORDER BY f_name, m_name,l_name ");
                    st.setString(1, searchEmployee.getSearchString().toUpperCase() + "%");
                }

            } else if (searchEmployee.getOffcode() != null) {
                if (searchEmployee.getCriteria() == null || searchEmployee.getCriteria().equals("")) {
                    countst = con.prepareStatement("SELECT count(*) CNT  FROM emp_mast WHERE cur_off_code = ?");
                    countst.setString(1, searchEmployee.getOffcode());
                    st = con.prepareStatement("SELECT EMP_ID, TRIM(ARRAY_TO_STRING(ARRAY[emp_mast.INITIALS, emp_mast.F_NAME, emp_mast.M_NAME, emp_mast.L_NAME], ' ')) FULL_NAME, F_NAME, POST, MOBILE,post_grp_type,  "
                            + " GPF_NO, DOB, DOS, DOE_GOV, JOINDATE_OF_GOO, (SELECT COUNT(*) FROM EMP_PROCEEDING WHERE EMP_ID=EMP_MAST.EMP_ID) CNT FROM EMP_MAST  "
                            + " LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC"
                            + " INNER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
                            + " WHERE CUR_OFF_CODE=?"
                            + " ORDER BY F_NAME");
                    st.setString(1, searchEmployee.getOffcode());
                    /*if deptname & offcode != null & searchcriteria = GPFNO */
                } else if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("GPFNO")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE cur_off_code = ? and gpf_no = ? ");
                    countst.setString(1, searchEmployee.getOffcode());
                    countst.setString(2, StringUtils.upperCase(searchEmployee.getSearchString()));
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? and gpf_no = ? order by f_name");
                    st.setString(1, searchEmployee.getOffcode());
                    st.setString(2, StringUtils.upperCase(searchEmployee.getSearchString()));
                } else if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("HRMSID") && searchEmployee.getOffcode() != null) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE cur_off_code = ? and emp_id = ? ");
                    countst.setString(1, searchEmployee.getOffcode());
                    countst.setString(2, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? and emp_id = ? order by f_name");
                    st.setString(1, searchEmployee.getOffcode());
                    st.setString(2, searchEmployee.getSearchString());
                } else if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("FNAME") && searchEmployee.getOffcode() != null) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? and F_name LIKE  ? LIMIT " + limit + " OFFSET " + offSet);
                    countst.setString(1, searchEmployee.getOffcode());
                    countst.setString(2, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "  WHERE cur_off_code = ? and F_name LIKE  ? ORDER BY f_name, m_name,l_name");
                    st.setString(1, searchEmployee.getOffcode());
                    st.setString(2, searchEmployee.getSearchString().toUpperCase() + "%");

                } else {
                    countst = con.prepareStatement("SELECT count(*) CNT  FROM emp_mast WHERE cur_off_code = ?");
                    countst.setString(1, searchEmployee.getOffcode());
                    st = con.prepareStatement("SELECT EMP_ID, TRIM(ARRAY_TO_STRING(ARRAY[emp_mast.INITIALS, emp_mast.F_NAME, emp_mast.M_NAME, emp_mast.L_NAME], ' ')) FULL_NAME, F_NAME, POST, MOBILE, "
                            + "GPF_NO, DOB, DOS, DOE_GOV, JOINDATE_OF_GOO, post_grp_type FROM EMP_MAST "
                            + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC "
                            + "INNER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE CUR_OFF_CODE=? "
                            + "ORDER BY F_NAME ");
                    st.setString(1, searchEmployee.getOffcode());

                }
            }

            rs = countst.executeQuery();
            if (rs.next()) {
                empSearchResult.setTotalEmpFound(rs.getInt("CNT"));
            }
            rs = st.executeQuery();
            while (rs.next()) {
                employee = new Employee();
                // employee.setPageNo(((Page - 1) * 20 + count) + "");
                employee.setEmpid(rs.getString("EMP_ID"));
                employee.setEmpName(rs.getString("FULL_NAME"));
                employee.setGpfno(rs.getString("gpf_no"));
                employee.setPost(rs.getString("POST"));
                employee.setPostGrpType(rs.getString("post_grp_type"));
                employee.setMobile(rs.getString("MOBILE"));
                employee.setDob(CommonFunctions.getFormattedOutputDate4(rs.getDate("dob")));
                if (rs.getInt("CNT") > 0) {
                    employee.setDepstatus("Y");
                } else {
                    employee.setDepstatus("N");
                }
                employeeList.add(employee);
            }
            empSearchResult.setEmployeeList(employeeList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empSearchResult;
    }

    @Override
    public ArrayList getPendingDPReportByAdmin() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList dpStatusReport = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select count(*) cnt, department_name from disciplinary_delinquent "
                    + "inner join emp_mast on disciplinary_delinquent.do_hrmsid=emp_mast.emp_id "
                    + "inner join g_office on emp_mast.cur_off_code=g_office.off_code "
                    + "inner join g_department on g_office.department_code=g_department.department_code group by department_name order by department_name asc");
            rs = pst.executeQuery();
            while (rs.next()) {
                DPPendingReportBean dPPendingReportBean = new DPPendingReportBean();
                dPPendingReportBean.setTotalNumberInitiated(rs.getString("cnt"));
                dPPendingReportBean.setDepartmentName(rs.getString("department_name"));
                dpStatusReport.add(dPPendingReportBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return dpStatusReport;
    }

    @Override
    public ArrayList getPendingDisciplinaryProceedingReportByAdmin() {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs1, rs2, rs3 = null;
        ArrayList outList = new ArrayList();

        String stm = null;
        String stm1 = null;
        String sql2 = null;
        try {
            con = repodataSource.getConnection();

            sql2 = ("select count(*) cnt, department_name,off_en,off_code from disciplinary_delinquent "
                    + "inner join emp_mast on disciplinary_delinquent.do_hrmsid=emp_mast.emp_id "
                    + "inner join g_office on emp_mast.cur_off_code=g_office.off_code "
                    + "inner join g_department on g_office.department_code=g_department.department_code "
                    + "where g_department.if_active='Y' and dep_code NOT IN('00', '08', '09', '10', '13', '14','06')  AND (is_ddo='Y' and off_status='F') "
                    + "and (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') and g_office.department_code=? "
                    + "group by department_name,off_en,off_code order by g_department.department_name");


            /*stm = "select count(*) cnt, department_name,g_department.department_code from disciplinary_delinquent "
             + "inner join emp_mast on disciplinary_delinquent.do_hrmsid=emp_mast.emp_id "
             + "inner join g_office on emp_mast.cur_off_code=g_office.off_code "
             + "inner join g_department on g_office.department_code=g_department.department_code "
             + "where g_department.if_active='Y' and dep_code NOT IN('00', '08', '09', '10', '13', '14','06')  AND (is_ddo='Y' and off_status='F') "
             + "and (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') "
             + "group by department_name,g_department.department_code order by department_name asc"; */
            stm = "select cnt, department_name,g_department.department_code from g_department "
                    + "left outer join (select count(*) cnt, g_office.department_code from disciplinary_delinquent "
                    + "inner join emp_mast on disciplinary_delinquent.do_hrmsid=emp_mast.emp_id "
                    + "inner join g_office on emp_mast.cur_off_code=g_office.off_code "
                    + "where dep_code NOT IN('00', '08', '09', '10', '13', '14','06')  AND (is_ddo='Y' and off_status='F') and (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') "
                    + "group by department_code)t1 "
                    + "on g_department.department_code = t1.department_code where g_department.if_active='Y' order by department_name asc";

            ps = con.prepareStatement(stm);
            rs1 = ps.executeQuery();

            while (rs1.next()) {
                DPPendingReportBean dPPendingReportBean = new DPPendingReportBean();
                if (rs1.getString("cnt") == null || rs1.getString("cnt").equals("")) {
                    dPPendingReportBean.setTotalNumberInitiated("0");
                } else {
                    dPPendingReportBean.setTotalNumberInitiated(rs1.getString("cnt"));
                }
                dPPendingReportBean.setDepartmentName(rs1.getString("department_name"));
                String deptCode = rs1.getString("department_code");

                ArrayList inList = new ArrayList();

                ps1 = con.prepareStatement(sql2);
                ps1.setString(1, deptCode);
                rs2 = ps1.executeQuery();
                while (rs2.next()) {
                    DPPendingReportBean dPBean = new DPPendingReportBean();
                    if (rs2.getString("cnt") == null || rs2.getString("cnt").equals("")) {
                        dPPendingReportBean.setTotalNumberInitiated("0");
                    } else {
                        dPBean.setTotalNumberInitiated(rs2.getString("cnt"));
                    }
                    dPBean.setDepartmentName(rs2.getString("department_name"));
                    dPBean.setOfficeName(rs2.getString("off_en"));
                    dPBean.setOfficeCode(rs2.getString("off_code"));
                    inList.add(dPBean);
                }
                dPPendingReportBean.setDeptWiseDP(inList);
                outList.add(dPPendingReportBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList getPendingDPReportDepartmentwiseByAdmin() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList dpStatusReportDepartmentWise = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select count(*) cnt, department_name,off_en from disciplinary_delinquent "
                    + "inner join emp_mast on disciplinary_delinquent.do_hrmsid=emp_mast.emp_id "
                    + "inner join g_office on emp_mast.cur_off_code=g_office.off_code "
                    + "inner join g_department on g_office.department_code=g_department.department_code "
                    + "where dep_code NOT IN('00', '08', '09', '10', '13', '14','06')  AND (is_ddo='Y' and off_status='F') "
                    + "and (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') "
                    + "group by department_name,off_en order by g_department.department_name");
            rs = pst.executeQuery();
            while (rs.next()) {
                DPPendingReportBean dPPendingReportBean = new DPPendingReportBean();
                dPPendingReportBean.setTotalNumberInitiated(rs.getString("cnt"));
                dPPendingReportBean.setDepartmentName(rs.getString("department_name"));
                dPPendingReportBean.setOfficeName(rs.getString("off_en"));
                dpStatusReportDepartmentWise.add(dPPendingReportBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return dpStatusReportDepartmentWise;
    }

    @Override
    public ArrayList getPendingDPReportCadrewiseByAdmin() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList dpStatusReportCadreWise = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select count(*) cnt, g_cadre.cadre_name from disciplinary_delinquent "
                    + "inner join emp_mast on disciplinary_delinquent.do_hrmsid=emp_mast.emp_id "
                    + "left outer join g_cadre on emp_mast.cur_cadre_code=g_cadre.cadre_code "
                    + "inner join g_office on emp_mast.cur_off_code=g_office.off_code "
                    + "inner join g_department on g_office.department_code=g_department.department_code "
                    + "where dep_code NOT IN('00', '08', '09', '10', '13', '14','06') AND (is_ddo='Y' and off_status='F') "
                    + "and (is_regular = 'Y' OR is_regular = 'C' OR is_regular = 'G' OR is_regular = 'W') group by g_cadre.cadre_name order by g_cadre.cadre_name");
            rs = pst.executeQuery();
            while (rs.next()) {
                DPPendingReportBean dPPendingReportBean = new DPPendingReportBean();
                dPPendingReportBean.setTotalNumberInitiated(rs.getString("cnt"));
                dPPendingReportBean.setCadreName(rs.getString("cadre_name"));
                dpStatusReportCadreWise.add(dPPendingReportBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return dpStatusReportCadreWise;
    }
}
