/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.resignation;

import hrms.model.resignation.Resignation;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface ResignationDAO {

    public Resignation getEmployeeResignationData(String empId);

    public void saveEmployeeResignation(Resignation resig);

    public void deleteEmployeeResignation(Resignation resig);

    public List getResginationList(String empId);

    public Resignation editResignationData(String notId);

    public void updateEmployeeResignation(Resignation resig);
    
    public void deleteResignation(Resignation resig);
    
    public Resignation getEmployeeResignationDataForNewEntry(String empId);
    
}
