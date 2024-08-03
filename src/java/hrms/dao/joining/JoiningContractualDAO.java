package hrms.dao.joining;

import hrms.model.joining.JoiningContractualForm;
import java.util.List;

public interface JoiningContractualDAO {
    
    public List getJoiningContractualList(String empid, String offcode);

    public JoiningContractualForm getJoiningContractualData(String primaryTrId,String jid);

    public void saveJoiningContractual(JoiningContractualForm jform,String loginempid);
    
    public void updateJoiningContractual(JoiningContractualForm jform);
    
    public void deleteJoiningContractual(JoiningContractualForm jform);
    
}
