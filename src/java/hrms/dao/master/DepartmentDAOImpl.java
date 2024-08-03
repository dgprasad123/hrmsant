/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.SelectOption;
import hrms.common.DataBaseFunctions;
import hrms.model.master.Department;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Durga
 */
public class DepartmentDAOImpl implements DepartmentDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getDepartmentList() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        List deptlist = new ArrayList();
        try {
            con = dataSource.getConnection();
            String sql = "SELECT department_code,department_name from g_department where if_active='Y' order by department_name";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Department department = new Department();
                department.setDeptCode(rs.getString("department_code"));
                department.setDeptName(rs.getString("department_name"));
                deptlist.add(department);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt, con);
        }
        return deptlist;
    }

    @Override
    public List getDepartmentList(String offcode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List deptlist = new ArrayList();
        try {
            String offlvl = "";
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT g_department.DEPARTMENT_CODE,department_name,LVL FROM G_OFFICE "
                    + "INNER JOIN g_department ON G_OFFICE.DEPARTMENT_CODE = g_department.DEPARTMENT_CODE WHERE OFF_CODE=?");
            pstmt.setString(1, offcode);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                offlvl = rs.getString("LVL");
                Department department = new Department();
                department.setDeptCode(rs.getString("department_code"));
                department.setDeptName(rs.getString("department_name"));
                deptlist.add(department);
            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            if (offlvl.equals("03")) {
                pstmt = con.prepareStatement("SELECT department_code,department_name from g_department where if_active='Y' AND DEPARTMENT_CODE='26'");
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    Department department = new Department();
                    department.setDeptCode(rs.getString("department_code"));
                    department.setDeptName(rs.getString("department_name"));
                    deptlist.add(department);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return deptlist;
    }

    @Override
    public String getDeptName(String deptCode) {
        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;

        String deptName = "";
        try {
            con = dataSource.getConnection();
            String sql = "SELECT department_code,department_name from g_department where department_code='" + deptCode + "'";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                deptName = rs.getString("department_name");

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deptName;
    }

    @Override
    public List getDeptList() {
        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;

        List deptlist = new ArrayList();
        try {
            con = dataSource.getConnection();
            String sql = "SELECT department_code,department_name from g_department where if_active='Y' order by department_name";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("department_code"));
                so.setLabel(rs.getString("department_name"));
                deptlist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deptlist;
    }

    @Override
    public List getDepartmentPARList() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        List deptlist = new ArrayList();
        try {
            con = dataSource.getConnection();
            String sql = "SELECT department_code,department_name from g_department where if_active='Y' order by department_name";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Department department = new Department();
                department.setDeptCode(rs.getString("department_code"));
                department.setDeptName(rs.getString("department_name"));
                deptlist.add(department);
            }
            Department department1 = new Department();
            department1.setDeptCode("LM");
            department1.setDeptName("LEGISLATIVE MEMBERS");

            deptlist.add(department1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deptlist;
    }

    @Override
    public List getAllDepartmentLIst() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List allDepartmentList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select department_code,department_name from g_department  order by department_name");
            rs = ps.executeQuery();
            while (rs.next()) {
                Department department = new Department();
                department.setDeptCode(rs.getString("department_code"));
                department.setDeptName(rs.getString("department_name"));
                allDepartmentList.add(department);
            }

        } catch (Exception e) {
            e.printStackTrace();;
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allDepartmentList;
    }

    @Override
    public List getDepartmentList4SiPAR() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        List deptlist = new ArrayList();
        
        try {
            con = dataSource.getConnection();
            String sql = "SELECT department_code,department_name from hrmis2.g_department where (department_code='14' or department_code='11') "
                + "and if_active='Y' order by department_name";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Department department = new Department();
                department.setDeptCode(rs.getString("department_code"));
                department.setDeptName(rs.getString("department_name"));
                deptlist.add(department);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt, con);
        }
        return deptlist;
    }
}
