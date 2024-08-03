
package hrms.dao.redeployment;

import hrms.model.redeployment.Redeployment;
import java.util.List;
public interface RedeploymentDAO {
    
    public int insertRedeploymentData(Redeployment redeploy, int notId);
    
    public int updateRedeploymentData(Redeployment redeploy);
    
    public int deleteRedeployment(String redeploymentId);
    
    public Redeployment editRedeployment(int notId, String empId);
    
    public List findAllRedeployment(String empId);
}
