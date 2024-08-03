package hrms.dao.payroll.officewisesecondschedulelist;

import hrms.common.Message;
import hrms.model.payroll.officewisesecondschedulelist.OfficeWiseSecondScheduleForm;
import hrms.model.payroll.officewisesecondschedulelist.OfficeWiseSecondScheduleListBean;
import java.util.List;

public interface OfficeWiseSecondScheduleListDAO {
    
    public List getSectionList(String billGrpId);
    
    public List getSectionWiseEmpList(String sectionId,int rows,int page);
    
    public int getSectionWiseEmpListCount(String sectionId);
    
    public List getEmployeeList(String offCode,String billGrpId,int rows,int page);
    
    public int getEmployeeListCount(String offCode,String billGrpId);
    
    public OfficeWiseSecondScheduleForm getSecondScheduleData(String empid);
    
    public Message saveSecondScheduleData(OfficeWiseSecondScheduleForm oForm);
    
    public String[] getHeaderData(String empid);
	
	public Message saveRevisionAuthData(String chkEmp,String authEmp);
}
