/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.schedule;

import hrms.SelectOption;
import hrms.common.DataBaseFunctions;
import hrms.model.employee.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author manisha
 */
public class ProfileUpdatedDAOIMPL implements ProfileUpdatedDAO {
    
    @Resource(name = "dataSource")
    protected DataSource dataSource;
    private Object dateFormat;
    private String uploadPath;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    @Override
    public List getProfileUpdatedEmployeeList(String billNo) {
         Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean flag = false;
        ArrayList employeeList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select emp_name,cur_desg,if_profile_verified from aq_mast "
                    + "INNER JOIN emp_mast ON emp_mast.emp_id=aq_mast.emp_code where bill_no =?");

            pst.setInt(1, Integer.parseInt(billNo));
            rs = pst.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
               
                employee.setFname(rs.getString("emp_name"));
                employee.setCurDesg(rs.getString("cur_desg"));
                employee.setIfprofileVerified(rs.getString("if_profile_verified"));
                employeeList.add(employee);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return employeeList;
    }


}
