package hrms.dao.payroll.arrear;

import hrms.model.payroll.arrear.ITScheduleArrearBean;
import hrms.model.payroll.schedule.ItScheduleBean;
import java.util.List;

public interface ITScheduleArrearDAO {
    
    public List getITScheduleEmployeeList(String billno, int aqMonth, int aqYear);

    public ItScheduleBean getITScheduleHeaderDetails(String billno, String schedule);
    
}
