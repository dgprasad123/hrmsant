package hrms.dao.allotmentToCadre;

import hrms.model.allotmentToCadre.AllotmentToCadreForm;
import java.util.List;

public interface AllotmentToCadreDAO {
    
    public List getAllotmentToCadreList(String empid);
    
    public void saveAllotmentToCadre(AllotmentToCadreForm allotmentToCadre, int notId);
    
    public void updateAllotmentToCadre(AllotmentToCadreForm allotmentToCadre);
    
    public AllotmentToCadreForm getEmpAllotmentToCadreData(AllotmentToCadreForm allotmentToCadre, int notId);
    
}
