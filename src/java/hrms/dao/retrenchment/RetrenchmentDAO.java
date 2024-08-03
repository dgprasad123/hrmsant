/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.retrenchment;

import hrms.model.retrenchment.Retrenchment;

/**
 *
 * @author Surendra
 */
public interface RetrenchmentDAO {
    
    public int insertRetrenchmentData(Retrenchment retrench);
    
    public int updateRetrenchmentData(Retrenchment retrench);
    
    public int deleteRetrenchment(String retrenchId);
    
    public Retrenchment editRetrenchment(String retrenchId);
    
}
