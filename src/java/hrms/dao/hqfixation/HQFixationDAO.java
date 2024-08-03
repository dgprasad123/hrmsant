/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.hqfixation;

import hrms.model.suspension.Suspension;
import java.util.List;

/**
 *
 * @author Surendra
 */

public interface HQFixationDAO {
    
    public List findAllHQFixation(String empId);
    
    public int insertHQFixationData(Suspension hqf);
    
    public int updateHQFixationData(Suspension hqf);
    
    public int deleteHQFixation(String hqfId);
    
    public Suspension editHQFixation(String hqfId);
}
