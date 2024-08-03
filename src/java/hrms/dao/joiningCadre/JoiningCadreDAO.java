package hrms.dao.joiningCadre;

import hrms.model.joiningCadre.JoiningCadreForm;
import java.util.List;

public interface JoiningCadreDAO {
    
    public List getJoiningCadreList(String empid);
    
    public void saveJoiningCadre(JoiningCadreForm joiningCadreForm, int notId);
    
    public void updateJoiningCadre(JoiningCadreForm joiningCadreForm);
    
    public JoiningCadreForm getEmpJoiningCadreData(JoiningCadreForm joiningCadreForm, int notId);
    
}
