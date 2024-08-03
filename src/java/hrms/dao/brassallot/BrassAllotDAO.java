/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.brassallot;

import hrms.model.brassallot.BrassAllot;
import hrms.model.brassallot.BrassAllotList;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface BrassAllotDAO {
    
    public void insertBrassAllotData(BrassAllotList bsallot,String empId);
    
    public int updateBrassAllotData(BrassAllotList brass, String empId);
    
    public int deleteBrassAllotData(String brassId);
    
    public BrassAllotList editBrassAllotData(String empId, int notId);
    
    public ArrayList findAllBrassAllot(String empId,BrassAllotList bsallot );
    
    
}
