package hrms.dao.confirmationofservice;

import hrms.model.confirmationofservice.ConfirmationOfServiceForm;
import java.util.List;

public interface ConfirmationOfServiceDAO {
    
    public List getConfirmationOfServiceList(String empid);
    
    public List getAllotDescList(String notType);
    
    public void saveConfirmationOfService(ConfirmationOfServiceForm confirmationOfServiceForm, int notId,String loginempid,String sblanguage);
    
    public void updateConfirmationOfService(ConfirmationOfServiceForm confirmationOfServiceForm,String loginempid);
    
    public ConfirmationOfServiceForm getConfirmationOfServiceData(ConfirmationOfServiceForm confirmationOfServiceForm, int notId);
    
}
