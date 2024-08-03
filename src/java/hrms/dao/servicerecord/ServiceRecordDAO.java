package hrms.dao.servicerecord;

import hrms.model.servicerecord.ServiceRecordForm;
import java.util.ArrayList;

public interface ServiceRecordDAO {
    
    public ArrayList getServiceRecordList(String empid);
    
    public void saveServiceRecord(ServiceRecordForm serviceRecordForm,String selectedEmpId);
    
    public ServiceRecordForm editServiceRecord(String empid,int srid);
    
    public void deleteServiceRecord(int srid);
}
