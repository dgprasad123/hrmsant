/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.servicecloser;

import hrms.model.servicecloser.EmployeeDeceased;
import hrms.model.servicecloser.Retirement;

/**
 *
 * @author Manas
 */
public interface ServiceCloserDAO {

    public EmployeeDeceased getEmployeeDeceasedData(String empId);

    public void saveEmployeeDeceased(EmployeeDeceased employeeDeceased);

    public void deleteEmployeeDeceased(EmployeeDeceased employeeDeceased);

    public void saveEmployeeRetirement(Retirement retire);

    public void updateEmployeeRetirement(Retirement retire);

    public Retirement getEmployeeRetirementData(String empId);

    public void deleteEmployeeRetirement(Retirement retire);

    public void saveSuperannuation(Retirement retire);

    public Retirement getEmployeeSuperannuationData(String empId);
}
