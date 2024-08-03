/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.allowances;

import hrms.model.allowances.AllowanceModel;
import java.util.List;

/**
 *
 * @author Madhusmita
 */
public interface AllowancesDAO {

    public List getAllowancesList(String emp_id);

    public void saveAllowance(String emp_id, String not_id);

    public List getAllowancesDeductionList();

    public void insertAllowancesData(AllowanceModel allowBean, int notId);

    public int updateAllowancesData(AllowanceModel allowBean, int notId);

    public AllowanceModel editAllowancesData(int notId, String emp_id);

}
