/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.ws;

import hrms.dao.EmpQuarterAllotment.EmpQuarterAllotDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.EmpQuarterAllotment.EmpQuarterBean;
import hrms.model.payroll.QuaterRent;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Manas Jena
 */
@WebService
public class EquaterWS {

    ScheduleDAO scheduleDAO;
    EmpQuarterAllotDAO empQuarterAllotDAO;

    @WebMethod(exclude = true)
    public void setScheduleDAO(ScheduleDAO scheduleDAO) {
        this.scheduleDAO = scheduleDAO;
    }
    
    @WebMethod(exclude = true)
    public void setEmpQuarterAllotDAO(EmpQuarterAllotDAO empQuarterAllotDAO) {
        this.empQuarterAllotDAO = empQuarterAllotDAO;
    }

    @WebMethod(operationName = "getRentData")
    public QuaterRent[] getRentData(@WebParam(name = "month") int month, @WebParam(name = "year") int year) {
        return scheduleDAO.getRentData(month, year);
    }

    @WebMethod(operationName = "saveQuaterAllotmentData")
    public boolean saveQuaterAllotmentData(@XmlElement(required=true) @WebParam(name = "empId") String newempId, 
            @XmlElement(required=true) @WebParam(name = "qrtrunit") String qrtrunit, 
            @XmlElement(required=true) @WebParam(name = "qrtrtype") String qrtrtype, 
            @XmlElement(required=true) @WebParam(name = "quarterNo") String quarterNo) {
        return empQuarterAllotDAO.updateEmpQuarterAllotment(newempId, qrtrunit, qrtrtype, quarterNo);
    }
}
