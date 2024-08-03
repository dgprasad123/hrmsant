/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.budgetproposal;

import java.util.List;

/**
 *
 * @author Surendra
 */


public interface BudgetProposoalDAO {
    
    public List getFinancialYearList();
    
    public List getBillList(String offCode, String fy);
    
}
