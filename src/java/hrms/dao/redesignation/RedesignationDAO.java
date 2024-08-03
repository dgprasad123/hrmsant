package hrms.dao.redesignation;

import hrms.model.redesignation.Redesignation;
import java.util.List;

public interface RedesignationDAO {
    
    public void insertRedesignationData(Redesignation redesig, int notid);
    
    public void updateRedesignationData(Redesignation redesig);
    
    public void deleteRedesignation(String redesignationId);
    
    public Redesignation editRedesignation(String empId, int notId);
    
    public List findAllRedesignation(String empId);
    
    public Redesignation findRetirementStatus(String gpfno);
    
    public int saveRedesignation(Redesignation redesig,String loginoffcode);
}
