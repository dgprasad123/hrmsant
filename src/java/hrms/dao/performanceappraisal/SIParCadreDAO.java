/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.performanceappraisal;

import hrms.model.parmast.SIParCadreBean;
import java.util.List;

/**
 *
 * @author hp
 */
public interface SIParCadreDAO {
    
    public List getSIParDetailsEmpIdWise(String searchCriteria,String searchString);
    
    public String updateSIParDetailsEmpIdWise(String loginHrmsId, SIParCadreBean siParCadre);
    
    public SIParCadreBean getSIParDetailsByEmpId(String hrmsId);
}
