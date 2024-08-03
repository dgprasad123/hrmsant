package hrms.dao.regularizationService;

import hrms.model.regularizeService.RegularizeServiceForm;
import java.util.List;

public interface RegularizationServiceDAO {
    
    public void insertRegularizationData(RegularizeServiceForm regularizeService, int notId);
    
    public void updateRegularizationData(RegularizeServiceForm regularizeService);
    
    public void deleteRegularizationData(String recruitId);
    
    public RegularizeServiceForm editRegularizationData(String empid, int notId);
    
    public List findAllRegularizationData(String empId);
    
}
