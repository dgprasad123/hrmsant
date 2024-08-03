/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.suspension;

import hrms.model.suspension.Suspension;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface SuspensionDAO {
    
    public List findAllSuspension(String empId);
    
    public int insertSuspensionData(Suspension suspend);
    
    public int updateSuspensionData(Suspension suspend);
    
    public int deleteSuspension(String suspendId);
    
    public Suspension editSuspension(String suspendId);
}
