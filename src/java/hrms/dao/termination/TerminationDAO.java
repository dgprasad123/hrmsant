/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.termination;

import hrms.model.termination.Termination;

/**
 *
 * @author Surendra
 */
public interface TerminationDAO {

    public int insertPayrevisionData(Termination term);

    public int updateTerminationData(Termination term);

    public int deleteTermination(String terminationId);

    public Termination editTermination(String terminationId);

    public Termination getEmployeeTerminationData(String empId);

    public void saveEmployeeTermination(Termination termi);
    
    public void deleteEmployeeTermination(Termination termi);

}
