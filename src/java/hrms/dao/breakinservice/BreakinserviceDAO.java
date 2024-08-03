/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.breakinservice;

import hrms.model.breakinservice.BreakinserviceBean;
import java.util.List;

/**
 *
 * @author Manoj PC
 */
public interface BreakinserviceDAO {
    public List getBreakinserviceList(String empid);
    public void saveBreakinService(BreakinserviceBean pb, int notid);
    public void updateBreakinService(BreakinserviceBean pb);
    public void deleteBreakinService(BreakinserviceBean pb);
    public BreakinserviceBean getEmpBISData(String leaveId);
    
}
