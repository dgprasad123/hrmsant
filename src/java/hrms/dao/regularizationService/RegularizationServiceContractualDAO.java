package hrms.dao.regularizationService;

import hrms.model.regularizeService.RegularizeServiceContractualForm;

public interface RegularizationServiceContractualDAO {
    
    public RegularizeServiceContractualForm getRegularizationServiceContractualData(RegularizeServiceContractualForm regularizeServiceForm);
    
    public void insertRegularizationContractualData(RegularizeServiceContractualForm regularizeService,String loginempid);
    
    public void updateRegularizationContractualData(RegularizeServiceContractualForm regularizeService,String loginempid);
    
    public void updateEmployeeData(RegularizeServiceContractualForm regularizeService);
    
    public RegularizeServiceContractualForm getRegularizationServiceContractual6YearsData(RegularizeServiceContractualForm regularizeServiceForm);
    
    public void insertRegularizationContractual6YearsData(RegularizeServiceContractualForm regularizeService,String loginempid);
    
    public void updateRegularizationContractual6YearsData(RegularizeServiceContractualForm regularizeService,String loginempid);
    
    public void updateEmployeeDataFor6Years(RegularizeServiceContractualForm regularizeService);
    
    public boolean checkEntryForContractual6YearsToRegular(String empid);
}
