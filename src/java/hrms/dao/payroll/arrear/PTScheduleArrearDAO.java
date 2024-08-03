/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.payroll.arrear;

import hrms.model.payroll.schedule.PtScheduleBean;
import java.util.List;

/**
 *
 * @author tushar
 */
public interface PTScheduleArrearDAO {
    
    public List getPTScheduleEmployeeList(String billno, int aqMonth, int aqYear);

    public PtScheduleBean getPTScheduleHeaderDetails(String billno);
    
}
