package hrms.dao.payroll.managePvtDeduction;

import hrms.model.payroll.managePvtDeduction.ManagePvtDeductionBean;
import hrms.model.payroll.managePvtDeduction.ManagePvtDeductionForm;
import java.util.ArrayList;

public interface ManagePvtDeductionDAO {
    
    public ArrayList getCurrentDDOData(String billNo);
    
    public ArrayList getAddedAccountData(String billNo);
    
    public void saveManagePvtDeductionData(ManagePvtDeductionForm mpdform);
    
    public ManagePvtDeductionForm editManagePvtDedData(ManagePvtDeductionForm mpdform,int pvtid);
    
    public int getDDOPvtDednAmount(int billNo);
    
    public int getAddedAccountDataAmount(int billNo);
    
    public int getBillNo(int pvtid);
    
    public void deleteManagePvtDeductionData(int pvtid);
    
    public String getCurrentBillMonthYear(int billno);
    
}
