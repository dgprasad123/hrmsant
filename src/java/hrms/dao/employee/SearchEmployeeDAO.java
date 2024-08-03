/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.employee;

import java.util.List;

/**
 *
 * @author hp
 */
public interface SearchEmployeeDAO {
    public List getEmployees(String firstName,String lastName,String dob,String designation, String departmentName, String fatherName);
    public List getDepartment();
}
