package hrms.dao.annualBudget;

import hrms.model.annualBudget.Annexure1a;
import hrms.model.annualBudget.Annexure1b;
import hrms.model.annualBudget.Annexure1c;
import hrms.model.annualBudget.Annexure2;
import java.util.List;
import jxl.Workbook;
import jxl.write.WritableWorkbook;

public interface AnnualBudgetDAO {
    
    public List getFinancialYearList();
    
    public List getAnnualBudgetList(String ddocode);
    
    public void processBudgetData(String fy,String ddocode);
    
    public List getAnnexureIIAData(String budgetid);
    
    public List getAnnexureIIBData(String budgetid);
    
    public void lockDDOBudget(String budgetid);
    
    public void updateAnnexureIEmployeeBudgetData(String budgetid,String detailid,String empid,String arrear,String oa,String rcm,String basicNextYear,String da, String hra);
    
    public void updateAdditionalAmountData(String budgetid,String detailid,String amount);
    
    public void updateExclusionsData(String budgetid,String detailid,String amount);
    
    public void updateContractualData(String budgetid,String detailid,String incrDcrMenInPositionData,String actualExp201920Data,String actualExp202021Data,String revisedEstimate202021Data,String be202122Data,String txtTotalMenInPositionData);
    
    public String getDDOBudgetLockStatus(String budgetid);
    
    public String getDDOBudgetFinancialYear(String budgetid);
    
    public void updateBudgetAbstractData(String budgetid,String detailid,String anticipatedVacancy,String vacancytobefilled,String anticipatedMenInPosition,String currentVacancy,String sanctionedStrength);
    
    public void updateDDOBudgetAfterSubmit(String budgetid);
    
    public void deleteDDOAnnualBudget(String budgetid);
    
    public void ReProcessBudgetAnnexureIIAData(String fy,String ddocode,String budgetid);
    
    public void ReProcessBudgetAnnexureIIBData(String fy,String ddocode,String budgetid);
    
    public List getDDOAnnualBudgetDataDC(String ddocode,String fy);
    
    public void unlockDDOBudgetDC(String budgetid);
    
    public String getDDOCodeDistrictDC(String logindistcode,String ddocode);
    
    public String checkDDOAnnualBudgetCreateStatus(String ddocode,String fy);
    
    public String checkDDOAnnualBudgetSubmitStatus(String ddocode,String fy);
    
    public void downloadAnnualBudgetDataExcel(WritableWorkbook workbook, String budgetid,String chartacc,String type);
    
    public void uploadAnnualBudgetDataExcel(Workbook workbook, String budgetid,String type,String ddocode);
    
    public String getDDOCodeSelectedOfficeWise(String offcode);
    
    /* For rest service code  */
    public List<Annexure1a> getAnnexure1Adata(String ddoCode, String fy);
    
    public List<Annexure1b> getAnnexure1Bdata(String ddoCode, String fy);
    
    public List<Annexure1c> getAnnexure1Cdata(String ddoCode, String fy);
    
    public List<Annexure2> getAnnexure2data(String ddoCode, String fy);
    
    public String sendBudgetDataToIFMS(String jsonstring, String fileKeyPath);

}
