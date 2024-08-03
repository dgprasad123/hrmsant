package hrms.dao.reappointment;

import hrms.model.reappointment.ReAppointmentForm;
import java.util.List;

public interface ReAppointmentDAO {
    
    public ReAppointmentForm getEmployeeReAppointmentData(String empId);
    
    public List getReAppointmentList(String empId);
    
    public void saveEmployeeReAppointment(ReAppointmentForm reapp);
    
}
