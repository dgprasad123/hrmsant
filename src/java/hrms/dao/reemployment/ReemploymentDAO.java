/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.reemployment;

import hrms.model.reemployment.Reemployment;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface ReemploymentDAO {
    
    public int insertReemploymentData(Reemployment reemployment);
    
    public int updateReemploymentData(Reemployment reemployment);
    
    public int deleteReemployment(String reemploymentId);
    
    public Reemployment editReemployment(String reemploymentId);
    
    public List findAllReemployment(String empId);
    
}
