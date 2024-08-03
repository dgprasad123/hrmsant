/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.employee;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.employee.SearchEmployeeDetails;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class SearchEmployeeDAOImpl implements SearchEmployeeDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getEmployees(String firstName, String lastName, String dob, String designation, String departmentName, String fatherName) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        SearchEmployeeDetails searchEmployee = new SearchEmployeeDetails();
        List empDataList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            String sql = "SELECT EMP.*, post,g_spc.spn DESIGNATION,OFF_EN OFFICENAME,DEPARTMENT_NAME,er.f_name,er.l_name,"
                    + "array_to_string(array[er.initials,er.f_name,er.m_name,er.l_name],' ')fathersname FROM "
                    + "(SELECT emp_mast.emp_id,cur_spc,substr(cur_spc,14,6)gpc,cur_off_code,emp_mast.f_name,"
                    + "emp_mast.l_name,array_to_string(array[emp_mast.initials,emp_mast.f_name,emp_mast.m_name,emp_mast.l_name],' ')empname,"
                    + " emp_mast.dob FROM emp_mast)EMP"
                    + " LEFT OUTER JOIN g_spc ON EMP.cur_spc = g_spc.spc"
                    + " LEFT OUTER JOIN g_post on emp.gpc=g_post.post_code"
                    + " LEFT OUTER JOIN g_office ON EMP.cur_off_code = g_office.off_code"
                    + " LEFT OUTER JOIN g_department ON g_office.department_code = g_department.department_code "
                    + "LEFT OUTER JOIN (SELECT * FROM emp_relation WHERE relation='FATHER') er on emp.emp_id = er.emp_id "
                    + "where 1 = 1";
            if (dob != null && !dob.isEmpty()) {
                sql += " AND EMP.dob = '" + dob + "'";
            }
            if (firstName != null && !firstName.isEmpty()) {
                sql += " AND EMP.f_name like '%" + firstName + "%'";
            }
            if (lastName != null && !lastName.isEmpty()) {
                sql += " AND EMP.l_name like '%" + lastName + "%'";
            }
            if (designation != null && !designation.isEmpty()) {
                sql += " AND post like '%" + designation + "%'";
            }
            if (departmentName != null && !departmentName.isEmpty()) {
                sql += " AND DEPARTMENT_NAME like '%" + departmentName + "%'";
            }
            if (fatherName != null && !fatherName.isEmpty()) {
                sql += " AND (er.f_name like '%" + fatherName + "%' OR er.l_name LIKE '%" + fatherName + "%')";
            }

            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                searchEmployee = new SearchEmployeeDetails();
                searchEmployee.setEmpId(rs.getString("emp_id"));
                searchEmployee.setFirstName(rs.getString("f_name"));
                searchEmployee.setLastName(rs.getString("l_name"));
                searchEmployee.setDob(CommonFunctions.getFormattedOutputDate6(rs.getDate("DOB")));
                searchEmployee.setDesignation(rs.getString("DESIGNATION"));
                searchEmployee.setDepartmentName(rs.getString("department_name"));
                searchEmployee.setFatherName(rs.getString("fathersname"));
                empDataList.add(searchEmployee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empDataList;
    }

    @Override
    public List getDepartment() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        SearchEmployeeDetails searchEmployee = new SearchEmployeeDetails();
        List departmentList = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            String sql = "SELECT department_code, department_name FROM g_department WHERE if_active = 'Y' ORDER BY department_name";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String departmentName = rs.getString("department_name");

                departmentList.add(departmentName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return departmentList;
    }

}
