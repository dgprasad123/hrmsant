package hrms.dao.loansanction;

import hrms.model.common.FileAttribute;
import hrms.model.loan.GroupLoanBean;
import hrms.model.loan.GroupLoanForm;
import hrms.model.loan.Loan;
import hrms.model.payroll.aqdtls.AqDtlsModel;
import hrms.model.payroll.aqmast.AqmastModel;
import java.util.ArrayList;
import java.util.List;

public interface LoanSancDAO {

    public List getLoanSancList(String empId);

    public void saveLoanDetail(Loan loanForm, int notId);

    public Loan editLoanData(String loanId);

    public void removeLoanData(String loanId, int notId);

    public void updateLoanDetail(Loan loanForm, String filepath);

    public List getPrincipalLoanListForBill(String empId, int payday, String depcode);

    public List getInterestLoanListForBill(String empId, int payday, String depcode);

    public List getLoanDeductionDeailEmpWise(String empId, String loanType);  

    public String getFABTId(String offCode);

    public String getGPFBTId(String gpfType);

    public AqDtlsModel[] getAqDtlsModelFromPLoanList(List principalLoanList, AqmastModel aqmast);

    public AqDtlsModel[] getAqDtlsModelFromILoanList(List interestLoanList, AqmastModel aqmast);

    public ArrayList getMonthWiseInstlment(String empCode, String loanId);

    public List EmployeeWiseLoanList(String empId);

    public ArrayList getSectionList(String offCode);

    public ArrayList getLoanList();

    public ArrayList getEmpList(String offCode, String billname, String loanname);

    public void saveEmpData(String insertdata);

    public void startStopLoandata(String loanId, String status);

    public Loan editAGInterestLoanData(String empid, String adrefid, Loan loanForm);

    public Loan getLoanInterestIntimationData(String loanid);

    public String getLoanTP(String loanid);

    public String getLoanName(String loantp);

    public int updateLoanSanctionSBData(Loan lfb);

    public int getAGInterestDetailedReportEmployeeCount(String offcode);

    public FileAttribute getAttachedDocument(String filePath, String docName, String loanid);

    public int deleteLoanAttachment(String loanid, String filepath, FileAttribute fa);
    
    public String SaveLoanDetailSBCorrection(Loan loanForm, int notId, int sbcorrectionid);
    
    public Loan editLoanDataSBCorrectionDDO(String loanId, int sbcorrectionid);
    
    public void approveLoansanctionDataSBCorrection(Loan loanForm,String entrydeptcode,String entryoffcode,String entryspc,String loginuserid);
}
