/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.model.master.Bank;
import java.util.ArrayList;

/**
 *
 * @author lenovo pc
 */
public interface BranchDAO {

    public ArrayList getBranchList(String bankcode);

    public Bank getBranch(String branchcode);
    
    public void addNewBranch(Bank bank, String bankcode);
    
    public void updateBranch(Bank bank);
}
