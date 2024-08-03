/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.arrear;

import hrms.model.payroll.schedule.GPFScheduleBean;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DurgaPrasad
 */
public interface TPFArrearDAO {
    public GPFScheduleBean getGPFScheduleHeaderDetails(String billno);

    public List getGPFScheduleTypeList(String billno, int aqmonth, int aqyear);

    public List getGPFScheduleAbstractList(String billno, int aqmonth, int aqyear);
    
    public ArrayList getEmpGpfDetails(String billNo) throws Exception;
    
    public List getTPFScheduleAbstractList(String billno);
}
