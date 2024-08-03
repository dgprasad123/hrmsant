
package hrms.dao.repayment;

import hrms.model.repayment.LoanRepayment;
import java.util.List;

public interface RepaymentDAO {

    public List getLoanRepaymentList(String empId);

    public void saveLoanRepayment(LoanRepayment loanrelease, int notId);

    public void updateLoanRepayment(LoanRepayment loanrelease);

    public LoanRepayment getLoanRepaymentData(String repid, int notid);

    public void deleteLoanRepayment(String repid, int notid);
}
