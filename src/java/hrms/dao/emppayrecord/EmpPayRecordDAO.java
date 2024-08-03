package hrms.dao.emppayrecord;

import hrms.model.emppayrecord.EmpPayRecordForm;

public interface EmpPayRecordDAO {
    
    public void saveEmpPayRecordData(EmpPayRecordForm eprform);
    
    public void updateEmpPayRecordData(EmpPayRecordForm eprform);
    
    public void deleteEmpPayRecordData(EmpPayRecordForm eprform);
    
    public void saveEmpPayRecordDataSBCorrection(EmpPayRecordForm eprform);
    
    public void updateEmpPayRecordDataSBCorrection(EmpPayRecordForm eprform);
    
    public void deleteEmpPayRecordDataSBCorrection(EmpPayRecordForm eprform);
}
