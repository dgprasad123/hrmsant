package hrms.dao.payfixation;

import hrms.model.notification.NotificationBean;
import hrms.model.payfixation.PayFixation;
import java.util.List;

public interface PayFixationDAO {
    
    public void insertPayFixationData(PayFixation payfix, int notId);
    
    public void updatePayFixationData(PayFixation payfix);
    
    public void deletePayFixation(PayFixation payfix);
    
    public void deleteRetrospectivePayFixationRow(int notId);
    
    public PayFixation editPayFixation(String empid, int notId);
    
    public List findAllPayFixation(String empId,String notType);
    
    public void insertCancelPayFixationData(NotificationBean nb, int notId, String othspc);
    
    public PayFixation editCancelPayFixation(String empid, int notId);
    
    public PayFixation editSupersedePayFixation(String empid, int notId);
    
    public void insertSupersedePayFixationData(PayFixation payfix, int notId);
    
    public void updateSupersedePayFixationData(PayFixation payfix);
    
    public String getEmployeeDateOfEntry(String empid);
    
    public List findAllFinBenefits(String empId);
    
    public void insertFinancialBenefitData(PayFixation payfix, int notId);
    
    public void updateFinancialBenefitData(PayFixation payfix);
    
    public String insertPayFixationDataSBCorrection(PayFixation payfix, int notId, int refcorrectionid);
    
    public PayFixation editPayFixationSBCorrectionDDO(String empid, int notId, int refcorrectionid);
    
    public void approvePayFixationDataSBCorrection(PayFixation payfix,String entrydeptcode,String entryoffcode,String entryspc,String loginuserid);
}
