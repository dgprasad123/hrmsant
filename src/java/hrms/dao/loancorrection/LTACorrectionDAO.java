/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.loancorrection;

import hrms.model.loan.LTA;
import hrms.model.loan.LTAGroup;
import hrms.model.loan.Loan;
import java.util.ArrayList;

/**
 *
 * @author Madhusmita
 */
public interface LTACorrectionDAO {

    public ArrayList getEmployeeLoanList(Loan empLoan);
    
    public void updateLoanDetails(Loan empLoan);
    
    public ArrayList getLtaList(String finYear, String loanType, String loaneeId);
    

}
