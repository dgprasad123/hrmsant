/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.reinstatement;

import hrms.model.reinstatement.Reinstatement;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface ReinstatementDAO {
    
    public List findAllReinstatement(String empId);
    
    public int insertReinstatementData(Reinstatement reinstate);
    
    public int updateReinstatementData(Reinstatement reinstate);
    
    public int deleteReinstatement(String reinstateId);
    
    public Reinstatement editReinstatement(String reinstateId);
}
