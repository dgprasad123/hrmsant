package hrms.dao.payroll.arrear;

import hrms.model.payroll.schedule.GPFScheduleBean;
import hrms.model.payroll.schedule.ScheduleHelper;
import java.util.ArrayList;
import java.util.List;

public interface GPFArrearDAO {
    
    public GPFScheduleBean getGPFScheduleHeaderDetails(String billno);

    public List getGPFScheduleTypeList(String billno, int aqmonth, int aqyear);

    public List getGPFScheduleAbstractList(String billno, int aqmonth, int aqyear);
    
    public ArrayList getEmpGpfDetails(String billNo) throws Exception;
    
}
