package hrms.dao.loanrelease;

import hrms.model.loanrelease.LoanRelease;
import java.util.List;

public interface LoanreleaseDAO {

    public List getLoanreleaseList(String empId);

    public void saveLoanrelease(LoanRelease loanrelease, int notId);

    public void updateLoanrelease(LoanRelease loanrelease);

    public LoanRelease getLoanReleaseData(String repid, int notid);

    public void deleteLoanRelease(String repid, int notid);
}
