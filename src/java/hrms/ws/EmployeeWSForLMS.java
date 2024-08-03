/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.ws;

import hrms.dao.employee.EmployeeDAO;
import hrms.model.employee.Employee;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author Manas
 */
@WebService
public class EmployeeWSForLMS {
    EmployeeDAO employeeDAO;
    
    @WebMethod(exclude = true)
    public void setEmployeeDAO(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }
    
    @WebMethod(operationName = "getOfficeWiseEmployee")
    public Employee[] getOfficeWiseEmployee(@WebParam(name = "offCode") String offCode) {
        List<Employee> employeelist = employeeDAO.getOfficeWiseEmployeeList(offCode);
        Employee employees[] = employeelist.toArray(new Employee[employeelist.size()]);
        return employees;
    }
    
    @WebMethod(operationName = "getEmployeeProfile")
    public Employee getEmployeeProfile(@WebParam(name = "empid") String empid) {
        return employeeDAO.getEmployeeProfile(empid);
    }
    
}
