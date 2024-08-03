/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.EmpSantize;

import hrms.model.EmpSanitize.EmpSanitize;
import java.util.List;


/**
 *
 * @author Hp
 */
public interface EmpSanitizeDAO {

    public List getEmployee1List(String offcode);
    public List getEmployeeType();
    public void saveEmployeeSanitize(String empid,String employeetype);
    public void changeEmployeeStatus(String empid,String status);
}
