package hrms.dao.fiscalyear;

import hrms.model.fiscalyear.FiscalYear;
import java.util.List;

public interface FiscalYearDAO {
    
    public String getDefaultFiscalYear();
    
    public List getFiscalYearList();
    
    public List getFiscalYearListForPoliceNomination();
    
     public List getFiscalYearListForGroupCPromotion();
    
    public List getAllFiscalYearListForPoliceNomination();
    
     public List getPFiscalYearList();
     
     public List getisReviewedFiscalYearList();
     
     public List getFiscalYearListCYWise();
     
     public List getFiscalYearListForCadrewiseForceforward();
}
