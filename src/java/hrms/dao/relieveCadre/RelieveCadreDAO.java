package hrms.dao.relieveCadre;

import hrms.model.relieveCadre.RelieveCadreForm;
import java.util.List;

public interface RelieveCadreDAO {
    
    public List getRelieveCadreList(String empid);
    
    public void saveRelieveCadre(RelieveCadreForm relieveCadreForm, int notId);
    
    public void updateRelieveCadre(RelieveCadreForm relieveCadreForm);
    
    public RelieveCadreForm getEmpRelieveCadreData(RelieveCadreForm relieveCadreForm, int notId);
}
