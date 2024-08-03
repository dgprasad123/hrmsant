/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.billbrowser;

import hrms.SelectOption;
import hrms.common.HrmsBillConfig;
import hrms.model.billvouchingTreasury.BillDetail;
import hrms.model.master.Office;
import hrms.model.master.OfficeBean;
import hrms.model.payroll.billbrowser.AllowDeductDetails;
import hrms.model.payroll.billbrowser.BillAttr;
import hrms.model.payroll.billbrowser.BillBrowserbean;
import hrms.model.payroll.billbrowser.GetBillStatusBean;
import hrms.model.payroll.billbrowser.GlobalBillStatus;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Manas Jena
 */
public interface BillBrowserDAO {

    public boolean grantUploadBill(String offCode, String loginOffCode);

    public BillBrowserbean getArrearBillPeriod(String offcode, int prepareMonth, int preparedYear);

    public ArrayList getBillPrepareYear(String offCode);

    public ArrayList getSinglePageBillPrepareYear(String ddoCode);

    public ArrayList getSinglePageMonthFromSelectedYear(String ddoCode, int year, String txtBillType);

    public ArrayList getBillPrepareYearDHE(String offCode);

    public ArrayList getBillPrepareYearDDODHE(String offCode);

    public ArrayList getMajorHeadListTreasuryWise(String trcode, int aqyear, int aqmonth);

    public ArrayList getVoucherListTreasuryWise(String trcode, int aqyear, int aqmonth, String majorhead);

    public ArrayList getMonthFromSelectedYear(String offcode, int year, String txtBillType);

    public ArrayList getMonthFromSelectedYearDHE(String offcode, int year, String txtBillType);

    public ArrayList getPayBillList(int year, int month, String offCode, String billType, String spc);

    public ArrayList getArrearPayBillList(int year, int month, String offCode, String billType, String spc);

    public ArrayList getPayBillListDHE(int year, int month, String offCode, String billType, String spc);

    public ArrayList getPayBillListDHEColleges(int year, int month, String offCode, String billType, String spc);

    public BillDetail getBillDetails(int billno);

    public BillDetail getBillDetailsDHE(int billno, String offCode);

    public List getBillDetails(String offcode, int month, int year);

    public List getDdoCodeList(String offCode);

    public String getBillSubminorHead3(int billId);

    public ArrayList getOBJXMLData(int billId, String treasuryCode, double basicPay, String billdate, String typeofbill, String head, int aqMonth, int aqYear);

    public ArrayList getBTXMLData(int billId, String treasuryCode, String billdate, String typeofbill, int aqMonth, int aqYear);

    public ArrayList getGPFXMLData(int billId, String billdate, int monthasNumber, int year, String periodFrom, String periodTo, String typeofbill, int aqMonth, int aqYear) throws Exception;

    public ArrayList getTPFXMLData(int billId, String billdate, int monthasNumber, int year, String periodFrom, String periodTo, String typeofbill, int aqMonth, int aqYear) throws Exception;

    public ArrayList getNPSXMLData(int billId, String billdate, int monthasNumber, int year, String typeofBill, int aqMonth, int aqYear);

    public int getbillsubmissionCount(int billno);

    public void updateBillStatus(int billno, int billStatusId);

    public void updateBillHistory(int billno, String submissionDate, String billgrossNet);

    public ArrayList getPayBillList(int year, int month, String treasuryCode);

    public int getNewBillYear(String offCode, String typeOfBill);

    public int getNewBillMonth(String offCode, int year, String typeOfBill);

    public int getNewBillYearDHE(String offCode, String typeOfBill);

    public int getNewBillMonthDHE(String offCode, int year, String typeOfBill);

    public GlobalBillStatus getBillProcessStatus(String globalVariablename);

    public String getMonthName(int month);

    public void updateBillChartofAccount(int billno, BillBrowserbean bean);

    public ArrayList getBillGroupList(String offCode, String curSpc);

    public ArrayList getArrearBillGroupList(String offCode, String curSpc);

    public ArrayList getsubOfficeBillGroupList(String offCode);

    public BillAttr[] createBillFromBillGroup(int month, int year, String[] billGroupId, String processDate, int priority, String billType, String moffcode);

    public BillAttr[] createBillFromBillGroupDHE(int month, int year, String[] billGroupId, String processDate, int priority, String billType, String moffcode);

    public BillAttr[] createBillForExtraMonthIncentive(int mAqMonth, int mAqYear, String[] billGroupId, String processDt, int priority, String billType, String moffcode);

    public BillAttr[] createBillFromBillGroupForAdvancePay(String fromDate, String toDate, String[] billGroupId, String processDate, int priority, String billType, String moffcode, int percentageArraer);

    public BillAttr[] createBillFromBillGroupForArrear(String fromDate, String toDate, String[] billGroupId, String processDate, int priority, String billType, String moffcode, int percentageArraer);

    public int getBillPriority(String offCode);

    public AllowDeductDetails getPay(int billno, String offCode);

    public String getGrossForArrear(int billno);

    public String getTotalDeductionForArrear(int billno);

    public ArrayList getAllowanceList(int billno, int aqMonth, int aqyear);

    public ArrayList getAllowanceListForArrear(int billno, int aqMonth, int aqyear);

    public ArrayList getDeductionListForArrear(int billno, int aqMonth, int aqYear);

    public ArrayList getDeductionList(int billno, int aqMonth, int aqYear);

    public ArrayList getPvtLoanList(int billno, int aqMonth, int aqYear);

    public void reprocessSingleBill(BillBrowserbean bbbean, Office off);

    public void updateBillData(BillBrowserbean bbbean, String typeOfBill);

    public String getBillChartofAccount(int billno);

    public String verifyBillNoandBenRefNo(String offCode, String billId, int year, int month);

    public GetBillStatusBean getUploadBillStatus(int billId);

    public void changeBillStatus(int billId, int statusId);

    public List getBillData(BillDetail billDetail);

    public List getFortyPercentVoucheredBillData(BillDetail billDetail);

    public void unlockBill(BillDetail billDetail, String dcLoginId, String ipaddress);

    public BillDetail unlockBillToResubmit(BillDetail billDetail,String ipaddress,String dcloginid);

    public BillDetail unlockBillForError(BillDetail billDetail,String ipaddress,String dcLoginid);

    public void objectBill(BillDetail billDetail);

    public double getBasicAmountBillWise(int billno);

    public boolean aerSubmitted(String offcode, String fy);

    public HrmsBillConfig getHrmsBillRestrictionStatus();

    public boolean isprofileVerifiedBillWise(int billId);

    public boolean isaersubmitted(String offCode, String fy);

    public boolean isaersubmittedAsAO(String offCode, String fy);

    public boolean isaersubmittedAsCO(String offCode, String fy);

    public boolean ispostTerminationDataSubmitted(String offCode, String fy);

    public String changeObjectBtHead(BillBrowserbean bbbean);

    public String changeObjectBtHeadOfArrear(BillBrowserbean bbbean);

    public String changeBtHeadOfArrear(BillBrowserbean bbbean);

    public String changePayHeadOfBill(BillBrowserbean bbbean);

    public int assignNewBillNo(int billNo);

    public boolean checkBillNo(int billNo);

    public int createBillId(String offCode, String bgrId, String processDate, String billType, int aqmonth, int aqyear) throws Exception;

    public int isContainsKey(String moffcode, int aqMonth, int aqYear, String bgId, String processDate, String billType, int billId, String regularOrcontractualBill, int priority) throws Exception;

    public int isContainsKeyForLeftOut(String moffcode, int aqMonth, int aqYear, String bgId, String processDate, String billType, int billId, String regularOrcontractualBill, int priority) throws Exception;

    public int isContainingKeyDHE(String moffcode, int aqMonth, int aqYear, String bgId, String processDate, String billType, int billId, String regularOrcontractualBill, int priority) throws Exception;

    public int getDHEBillDetails(String offCode, String bgrId, String processDate, String billType, int aqmonth, int aqyear) throws Exception;
    /*--isCollegeUnderDHE for Colleges Under DHE---*/

    public boolean isCollegeUnderDHE(String offcode);

    /*--for Self DDO DHE---*/
    public boolean isDDODHE(String offcode);

    public String getScheduleSlNo(String btId);

    public int updateRequiredReportsColumn(String schSlNo, int billNo);

    public String getFixedScheduleSlNo(String billType);

    public void updateBillInfo(BillDetail billDetail);

    public ArrayList getPayBillListForAG(int year, int month, String treasuryCode);

    public BillDetail getEditBillInfo(String billnumber);

    public boolean isbillPrepared(String billId);

    public ArrayList verifyforLockBill(int billId);

    public ArrayList getBeneficiaryList(int billId, String typeOfBill);

    public ArrayList getVoucherListForAG(int year, int month, String parentTrCode);

    public ArrayList getAquitanceVoucherListForAG(int year, int month, String parentTrCode);

    public double getBillGrossAndNet(int billId);

    public double getbillbenificiaryGross(int billId);

    public boolean verifySalaryBillofIAS(int billId);

    public boolean foundFiftyPercentGrossForIASCorrect(String empId, int billId);

    public String verifyUnlockBillDistrict(String logindistcode, String offcode, String billno);

    public ArrayList getFailedTransactionData(int billno);

    public int getFailedTransactionCount(int billNo);

    public SelectOption getBeneficiaryNet(int billNo);

    public List getMismatchBeneficiaryDetailsFromPreviousMonth(int billNo);

    public ArrayList getAllowanceListForComputerTokenReportArrear(int billno, int aqMonth, int aqyear);

    public boolean unlockFortyPercentVoucheredBill(int billNo);

    public boolean updateFortyPercentVoucheredBillStatus(int billNo);

    public ArrayList getVoucherListForCOA(int year, int month, String parentTrCode);

    public List getAllFixedCpfEmployeesInBill(int billNo, String billType);

    public String[] getSignedSinglePageSalaryPDFPath(int billno, int reportrefslno);

    public String getESignedFileStatus(int billno);

    public String getESignedFileStatusForArrearBill(int billno);

    public String getBillStatus(BillDetail billDetail);

    public BillDetail getStatusBillInfo(String billnumber);

    public int getDDORecoveryList(int billno);

    public boolean getbillVerificationStatus(String offcode, int billno, int aqMonth, int aqYear);

    public boolean updatebillVerificationStatus(String offcode, int billno, int aqMonth, int aqYear);

    public List getSalaryDetails(String empId, String fiscalYear);

    public List getBillYear(String empId);

    public List getprivilegedetails(String offcode);

    public List getempListNotForArrear(String billGrpId);

    public String checkNewBillGenerationStatus(String billGrpId,int aqMonth, int aqYear);
    
    public void deleteOfficeDHE(String billNo,String offcode);
    
    public int getBillFrontPageSlNo(String billtype);
    
    public List getMismatchGPFData(int billno,String typeOfBill);

}
