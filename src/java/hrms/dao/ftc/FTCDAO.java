/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.ftc;

import hrms.model.FTC.sFTCBean;
import java.util.List;

/**
 *
 * @author DurgaPrasad
 */
public interface FTCDAO {
    
    public List getFTCEntryList(String empId);
    
    public void updateFTCEntry(sFTCBean lBean);
    
    public void saveFTCEntry(String notId, sFTCBean slBean, String deptCode, String offCode, String spc);
    
    public sFTCBean getSFTCDetail(String empId, int notId);
    
}
