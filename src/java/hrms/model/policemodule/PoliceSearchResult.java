package hrms.model.policemodule;

import java.util.List;

public class PoliceSearchResult {
    
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
