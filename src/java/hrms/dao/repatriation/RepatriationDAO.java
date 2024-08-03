package hrms.dao.repatriation;

import hrms.model.repatriation.RepatriationForm;
import java.util.List;

public interface RepatriationDAO {
    
    public List getRepatriationList(String empid);
    
    public List getAllotDescList(String notType);
    
    public void saveRepatriation(RepatriationForm repatriationForm, int notId);
    
    public void updateRepatriation(RepatriationForm repatriationForm);
    
    public RepatriationForm getEmpRepatriationData(RepatriationForm repatriationForm, int notId);
}
