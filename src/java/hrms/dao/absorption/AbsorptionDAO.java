/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.absorption;

import hrms.model.absorption.AbsorptionModel;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface AbsorptionDAO {
    
    public int insertAbsorptionData(AbsorptionModel absorp, int notId);
    
    public int updateAbsorptionData(AbsorptionModel absorp);
    
    public int deleteAbsorptionData(AbsorptionModel absorptionForm);
    
    public AbsorptionModel editAbsorptionData(int notId,String empId);
    
    public List findAllAbsorption(String empId);
    
    
            
}
