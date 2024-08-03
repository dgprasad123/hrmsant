/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payrevision;

import hrms.model.payrevision.Payrevision;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface PayrevisionDAO {
    
    public int insertPayrevisionData(Payrevision payrev);
    
    public int updatePayrevisionData(Payrevision payrev);
    
    public int deletePayrevision(String payrevisionId);
    
    public Payrevision editPayrevision(String payrevisionId);
    
    public List findAllPayrevision(String empId);
    
}
