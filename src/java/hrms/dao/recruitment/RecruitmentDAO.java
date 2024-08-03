
package hrms.dao.recruitment;

import hrms.model.recruitment.RecruitmentModel;
import java.util.List;


public interface RecruitmentDAO {
    
    public void insertRecruitmentData(RecruitmentModel recruit, int notId);
    
    public void updateRecruitmentData(RecruitmentModel recruit);
    
    public void deleteRecruitment(RecruitmentModel recruit);
    
    public RecruitmentModel editRecruitment(String empid, int notId);
    
    public List findAllRecruitment(String empId);
    
}
