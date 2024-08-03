/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.reemployment;

import hrms.model.reemployment.RedesignationContractualBean;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface RedesignationContractualDAO {

    public RedesignationContractualBean getTransferContractualList(String empid);

    public void saveCurrentpostContractual(RedesignationContractualBean incr, String loginempid, String loginusername);

}
