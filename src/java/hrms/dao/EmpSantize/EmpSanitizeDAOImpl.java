/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.EmpSantize;

import hrms.common.DataBaseFunctions;
import hrms.model.EmpSanitize.EmpSanitize;
import hrms.model.EmpSanitize.EmpType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Hp
 */
public class EmpSanitizeDAOImpl implements EmpSanitizeDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getEmployee1List(String offcode) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        //Employee employee = null;
        List list = new ArrayList();
        try {
            //You have to intialize the objects like con, pst and rs
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("SELECT emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,mobile,is_active,employee_type,spn from emp_mast left outer join g_spc on emp_mast.cur_spc = g_spc.spc where cur_off_code =? order by f_name");
            pst.setString(1, offcode);

            rs = pst.executeQuery();

            while (rs.next()) {
                EmpSanitize sanitize = new EmpSanitize();
                //Now you have to set the values from database

                sanitize.setEmpFullName(rs.getString("EMP_NAME"));
                sanitize.setDesignation(rs.getString("spn"));
                sanitize.setMobile(rs.getString("mobile"));
                sanitize.setIsActive(rs.getString("is_active"));
                sanitize.setEmpId(rs.getString("emp_id"));
                sanitize.setEmployeeType(rs.getString("employee_type"));

                list.add(sanitize);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public List getEmployeeType() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        //Employee employee = null;
        List list = new ArrayList();
        try {
            //You have to intialize the objects like con, pst and rs
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("select * from g_emp_type ");

            rs = pst.executeQuery();

            while (rs.next()) {
                EmpType type = new EmpType();
                //Now you have to set the values from database

                type.setEmpType(rs.getString("emp_type"));
                type.setIsregular(rs.getString("is_regular"));
                type.setTypeid(rs.getString("emp_type_id"));

                list.add(type);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public void saveEmployeeSanitize(String empid,String employeetype) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "UPDATE emp_mast SET  employee_type = ? WHERE emp_id = ?";
            pst = con.prepareStatement(sql);
             pst.setInt(1, Integer.parseInt(employeetype));
             pst.setString(2,empid);
            pst.executeUpdate();

            

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
     @Override
    public void changeEmployeeStatus(String empid,String status) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "UPDATE emp_mast SET  is_active = ?  WHERE emp_id = ?";
            pst = con.prepareStatement(sql);
             pst.setString(1,status);;
             pst.setString(2,empid);
            pst.executeUpdate();

            

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}
