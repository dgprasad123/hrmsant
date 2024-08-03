/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.arrear;

import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.arrear.ArrAqDtlsModel;
import hrms.model.payroll.arrear.ArrAqMastModel;
import hrms.model.payroll.arrear.OAClaimModel;
import hrms.model.payroll.arrear.PayRevisionOption;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import jxl.write.WritableWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Manas
 */
public interface ArrmastDAO {

    public String saveArrmastdata(ArrAqMastModel arrAqMastModel);

    public String addEmployeeToBill(ArrAqMastModel arrAqMastModel);

    public int getCalcUniqueNo(String aqslno);

    public void saveArrdtlsdata(List arrAqDtlsList, String aqslno);

    public void saveArrdtlsdata(ArrAqDtlsModel[] arrAqDtlsModels, int calc_unique_no);

    public List getArrearBillDtls(int billNo, String aqMonth, String aqYear);

    public List getOtherArrearBillDtls(int billno, String aqMonth, String aqYear);

    public List getDaArrearBillDtls(int billNo, String aqMonth, String aqYear);

    //public List getDaDiffArrearBillDtls(int billNo, String aqMonth, String aqYear);
    public void reprocessArrAqMast(int billNo, String aqslno);

    public void reprocess6ArrAqMast(int billNo, String aqslno);

    public void reprocessOtherArrAqMast(int billNo, String aqslno);
    
    public void addAllowanceToArrear(String aqslno, String adType);

    public void reprocessLeaveArrAqMast(CommonReportParamBean crb, String empCode);

    public void giveFullArrear(String aqslno);

    public List getArrearAcquaintance(int billNo);

    public List getLeaveArrearAcquaintance(int billNo);

    public List getArrearAqHeaderData(int billNo);

    public ArrAqMastModel getArrearAcquaintanceData(String aqSlNo);

    public ArrAqMastModel get6ArrearAcquaintanceData(String aqSlNo);

    public List getArrearAcquaintanceData(int billNo, String empCode);

    public ArrAqMastModel getOtherArrearAcquaintanceData(String aqSlNo);
    
    public boolean isIRHeadPresent(String aqSlNo);

    public List getArrearAcquaintanceDtls(String aqSlNo,int calcno);

    public List getOtherArrearAcquaintanceDtls(String aqSlNo);

    public List get6pArrearAcquaintanceDtls(String aqSlNo,int calcno);

    public ArrAqDtlsModel getArrearAcquaintanceDataList(String aqSlNo, int aqMonth, int aqYear, int calcuniqueno);
    
    public ArrAqDtlsModel getArrear6AcquaintanceDataList(String aqSlNo, int aqMonth, int aqYear, int calcuniqueno, int autoincrid);

    public ArrAqDtlsModel getOtherArrearAcquaintanceDataList(String aqSlNo, int aqMonth, int aqYear, int calcuniqueno);

    public int updateArrAqMastItData(int billNo, String aqSlNo, int incTax);

    public int updateArrMastItData(int billNo, String aqSlNo, int incTax);

    public int updateArrMastCpfData(int billNo, String aqSlNo, int cpfAmt);

    public int updateArrDtlsData(ArrAqDtlsModel arrAqDtlsModel);
    
    public int updateArr6DtlsData(ArrAqDtlsModel arrAqDtlsModel);

    public int updateOtherArrDtlsData(ArrAqDtlsModel arrAqDtlsModel);

    public int deleteArrDtlsData(ArrAqDtlsModel arrDtlsBean);

    public PayRevisionOption searchEmployee(String searchemp);

    public int updateArrMastPtData(int billNo, String aqSlNo, int ptAmt);
    
    public int updateArrMastORData(int billNo, String aqSlNo, int ptAmt);
    
    public int updateArrMastddoRecovryData(int billNo, String aqSlNo, int drAmt);

    public List getArrearAcqEmpDet(String billNo);

    public List getArrearAcqEmpPayDetails(String aqSlNo, int month, int year);

    public void downloadArrearAcqEmpExcel(OutputStream out, String offcode, WritableWorkbook workbook, String billNo) throws Exception;

    public int deleteArrMastData(String aqSlNo, int billNo);

    public ArrAqDtlsModel[] getAqDtlsModelFromAllowanceList(PayRevisionOption po, int month, int year, String empCode, double dapercentage, String aqslno);

    public ArrAqDtlsModel[] getAqDtlsModelFromAllowanceListForContractual(Date choiceDate, int month, int year, String empCode, int dapercentage, String aqslno, int gp);

    public PayRevisionOption getChoiceDate(String empCode);

    public int reCalculateArrMast(int billNo);

    public void insertIntoPayRevisionOption(String inputChoiceDate, int payrevisionbasic, String empid);

    public int[] getPayMatrix(int gp);

    public void downloadFullArrDataReportExcel(Workbook workbook, String billNo, String typeOfBill);

    public List getOaClaimEmployeeList(String billGroupId);

    public List getObjectHeadList();

    public String insertOaClaimData(OAClaimModel claimModel, String offCode);

    public String updateOaClaimData(OAClaimModel claimModel, String offCode);

    public OAClaimModel getOaClaimEmpData(String empId, String bgId);  
    
    public String getAcctType(int billno,String empid);
    
    public List getArrear40VerificationData(String empid);
    
    public void lockArrear40Bill(String checkedBill);
    
    public void deleteArrear40Bill(String billno,String empid);
    
    public ArrAqMastModel getNPSArrearAquitanceData(String aqSlNo);
    
    public void reprocessNPSArrAqMast(int billNo, String aqslno);
    
    public List getNPSArrearAcquaintance(int billNo);
    
    public int reprocessJudiciaryArrAqMast(int billNo, String aqslno);
    
    public int reprocessJudiciaryArr6AqMast(int billNo, String aqslno);
    
    public ArrAqMastModel getJudiciaryArrearAcquaintanceData(String aqSlNo);
    
    public ArrAqDtlsModel[] getAqDtlsModelFromAllowanceListForJudiciaryArrear(PayRevisionOption po, int month, int year, String empCode, double dapercentage, String aqslno);
    
    public ArrAqDtlsModel getJudiciaryArrearAquitanceDataList(String aqSlNo, int aqMonth, int aqYear, int calcuniqueno);
    
    public List getJudiciaryArrearBillDtls(int billno, String aqMonth, String aqYear,String typeofbill);
    
    public ArrAqMastModel getJudiciaryArrear6AcquaintanceData(String aqSlNo);
    
    public List getJudiciaryArrearAquitance(int billNo,CommonReportParamBean crb); 
    
    public List getArrear25VerificationData(String empid);
    
    public void deleteArrear25Bill(String billno,String empid);
    
    
}
