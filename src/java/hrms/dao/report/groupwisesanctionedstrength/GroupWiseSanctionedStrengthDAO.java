package hrms.dao.report.groupwisesanctionedstrength;

import java.util.List;

public interface GroupWiseSanctionedStrengthDAO {
    
    public List getDepartmentWiseData();
    
    public List getOfficeWiseData(String deptCode);
    
}
