package hrms.dao.report.officewisesanctionedstrength;

import hrms.model.report.officewisesanctionedstrength.OfficeWiseSanctionedStrengthBean;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface OfficeWiseSanctionedStrengthDAO {
    
    public List getSanctionedStrengthList(String offCode);
    
    public void saveSanctionedPostData(String offCode,OfficeWiseSanctionedStrengthBean bean);
    
    public String verifyDuplicate(String offCode,String financialYear);
    
    public OfficeWiseSanctionedStrengthBean getSanctionedStrengthData(OfficeWiseSanctionedStrengthBean bean);
    
    public HashMap<String, String> getFinancialYearList();
    
    public List getBillgroupListNotInEditNewCase(String ddocode, String fyYear, String aerid);
    
    public List getBillgroupListNotInAddNewCase(String ddocode, String fyYear);
    
    public List<BigDecimal> getSelectedBillGroup(String aerId);
    
}
