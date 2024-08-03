package hrms.dao.payroll.arrear;

import hrms.model.payroll.schedule.BillContributionRepotBean;
import java.util.List;

public interface NPSArrearDAO {
    
    public BillContributionRepotBean getBillContributionRepotScheduleHeaderDetails(String annexure, String billno);

    public List getBillContributionRepotScheduleEmpList(String annexure, String billno, int year, int month, String typeofbill);
    
}
