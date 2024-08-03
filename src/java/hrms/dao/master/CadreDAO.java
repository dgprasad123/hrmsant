package hrms.dao.master;

import hrms.model.cadre.Cadre;
//import hrms.model.master.Cadre;
import java.util.List;

public interface CadreDAO {

    public List getCadreList(String deptcode);

    public List getCadreListfOrPAR(String deptcode);

    public List getGradeList(String cadreCode);

    public List getGradeListDetails(String cadreCode);

    public List getCadreLevelList();

    public List getDescription(String cdrLevel);

    public void saveCadreData(Cadre cadreform);

    public void updateCadreData(Cadre cadreform);

    public void deleteCadreData(Cadre cadreform);

    public String getCadreName(String cadrecode);

    public Cadre editCadre(Cadre cadre);

    public List getEmployeeList(String cadreCode);

    public void saveNewCadre(Cadre cadre);

    public void updateNewCadre(Cadre cadre);

    public List getPostListCadreCodeAndDeptCodeWise(String deptCode,String cadreCode);
    
    public void saveCadreDataSBCorrection(Cadre cadreform);
    
    public void updateCadreDataSBCorrection(Cadre cadreform);
    
    public void deleteCadreDataSBCorrection(Cadre cadreform);
    
    public String getCadreGradeName(String gradecode);
}
