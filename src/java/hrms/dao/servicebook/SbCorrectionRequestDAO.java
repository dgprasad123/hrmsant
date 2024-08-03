package hrms.dao.servicebook;

import hrms.model.servicebook.SbCorrectionRequestModel;
import java.util.List;

public interface SbCorrectionRequestDAO {
    
    public List getServiceBookModuleNameList(String empHrmsId);
    public List getServiceBookModuleDataList(String empHrmsId, String moduleName,String loginoffcode);
    public SbCorrectionRequestModel getServiceBookModuleData(String empHrmsId, String moduleName, String noteId);
    
    public int saveRequestedServiceBookLanguage(SbCorrectionRequestModel sbmodel);
    
    public List getDDOEmployeeWiseServiceBookCorrectionData(String empid);
    
    public String getRequestedServiceBookLanguage(String empid,String nottype,String notid);
    
    public void rejectRequestedServiceBookLanguage(String empid,String nottype,String notid,String loginid);
    
    public void updateRequestedServiceBookFlag(String empid,String nottype,String notid,String correctionid,String loginid);
    
    public void updateRequestedServiceBookLanguage(int correctionid,String sblangrequested);
    
    public void submitRequestedServiceBookLanguage(String empid,int notid,String nottype);
    
    public void deleteRequestedServiceBookLanguage(String empid,String nottype,String notid);
    
    public List getRequestedEmployeeListSBCorrection(String offcode);
}
