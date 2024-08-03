/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.empinfo;

import java.util.List;

/**
 *
 * @author manisha
 */
public class EmployeeSearchResult {
    private List employeeList;
    private int totalEmpFound;

    public List getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List employeeList) {
        this.employeeList = employeeList;
    }

    public int getTotalEmpFound() {
        return totalEmpFound;
    }

    public void setTotalEmpFound(int totalEmpFound) {
        this.totalEmpFound = totalEmpFound;
    }
    
    
    
}
