/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.serviceVerification;

import hrms.model.serviceVerification.ServiceVerification;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface ServiceVerificationDAO {
    
    public List findAllSvData(String empId);
    
    public void addSvData(ServiceVerification sv,String authSpn);
    
    public void modifySvData(ServiceVerification sv,String authSpn);
    
    public void removeSvData(String svId);
    
    public ServiceVerification getServiceVerificationData(String svId, String empId);
    
    public boolean chkDuplicate(String frmdate, String todate, String svid, String empid) throws Exception;
    
    public void updateSVID(String empid,String svid,String frmdate,String todate);
    
    public void updateNullSVID(String empid,String frmdate,String todate);
    
    public int getQualifyingDays(String fdate, String tdate);
    
    public String getEmployeeDateOfEntry(String empid);
    
    public String getEmployeeDateOfRetirement(String empid);
    
    public ServiceVerification getServiceVerificationDataSBCorrectionDDO(String svId, String empId,int correctionid);
    
    public String addSvDataSBCorrection(ServiceVerification sv,String authSpn);
    
    public void approveServiceVerificationSBCorrection(ServiceVerification sv,String authSpn,String entrydeptcode,String entryoffcode,String entryspc,String loginuserid);
    
}
