package hrms.dao.departmentalExam;

import hrms.model.departmentalExam.DepartmentalExamForm;
import java.util.List;

public interface DepartmentalExamDAO {
    
    public List getDepartmentalExamList(String empid);
    
    public void saveDepartmentalExam(DepartmentalExamForm deptExamForm,String logindept,String loginoffice,String loginpost,String loginempid,String sblang);
    
    public void updateDepartmentalExam(DepartmentalExamForm deptExamForm,String sblang);
    
    public DepartmentalExamForm editDepartmentalExam(String empid,String examid);
}
