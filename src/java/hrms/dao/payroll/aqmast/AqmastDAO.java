/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.aqmast;

import hrms.model.employee.PayComponent;
import hrms.model.payroll.aqmast.AqmastModel;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Durga
 */
public interface AqmastDAO {

    public String saveAqmastdata(AqmastModel aqmast);

    public void updateAqmastdata(AqmastModel aqmast);

    public AqmastModel getAqmastDetail(String aqslno);

    public int deleteAqmastData(String aqslno);

    public List getAqmastList(int billno);

    public PayComponent getEmployeePayComponent(Date startDate, Date endDate, int daysInMonth);

    public String getEmployeeType(int billNo, int aqMonth, int aqYear);

    public void addMaintenanceData(AqmastModel aqMastModel);

    public boolean stopNpsDeduction(String empId, int billMonth, int billYear);

    public boolean stopGpfDeduction(String empId, int billMonth, int billYear);

    public boolean stopSalaryForPayHeldUp(String empId);

    public boolean stopSalaryForRetirement(String empId, int billMonth, int billYear);

    public List getBillgroupwiseOfficeMapped(String billgroupid, int billMonth, int billYear);
    
     public boolean stopShowingDummyPranForRetiredEmp(String empId, int billMonth, int billYear);

}
