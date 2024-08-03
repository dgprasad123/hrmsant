/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.master;

import hrms.model.master.MinisterDetails;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Devi
 */
public interface MinisterDetailsDAO {
    
    public String getMultipleDeptList(int hidlmid);
    
    public List getDeptListCode(int hidlmid);
    
    public List getMinisterDetailsList(String officiatingId);
         
    public String saveMinisterDetails(MinisterDetails mindetailsForm);
    
    public void updateMinisterDetails(MinisterDetails mindetailsForm);
    
    public MinisterDetails editMinisterDetails(MinisterDetails mindetailsForm);
    
    public String getExistingUserid(MinisterDetails mindetailsForm);
}
