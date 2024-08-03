/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.empeducation;

import hrms.model.empeducation.EmployeeEducation;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface EmployeeEducationDAO {
    
    public int insertEmployeeEducationData(EmployeeEducation edu);
    
    public int updateEmployeeEducationData(EmployeeEducation edu);
    
    public int deleteEmployeeEducationData(String empId, String qualification);
    
    public EmployeeEducation editEmployeeEducationData(String eduId,String qualification);
    
    public List findAllEmployeeEducation(String empId);
}
