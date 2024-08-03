/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.report.annualestablishment;

import com.itextpdf.text.Document;
import hrms.model.employee.EmployeeBasicProfile;

import hrms.model.report.annualestablishmentreport.AerListBox;
import hrms.model.report.annualestablishmentreport.AnnualEstablishment;
import hrms.model.report.annualestablishmentreport.AnnualEstablishmentReportPostTerminatonForm;
import hrms.model.workflowrouting.WorkflowRouting;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jxl.write.WritableWorkbook;

/**
 *
 * @author Surendra
 */
public interface AnnuaiEstablishmentReportDAO {

    public List getAerReportListFinancialYearWise(String offCode, String financialYear, String roleType, String loginSpc);

    public List getAerReportListFinancialYearWiseForCOLevel(String offCode, String financialYear, String roleType, String loginSpc);

    public void saveAERSanctionedPostData(String offCode, AnnualEstablishment bean, String ddocode);

    public boolean duplicateCreateAER(String offCode, String financialYear);

    public AnnualEstablishment geteditAerdata(int aerid);

    public String getCoType(String offCode, String financialYear);

    public AnnualEstablishment submittedforCurrentFinancialYear(String offcode, AnnualEstablishment ae);

    public List<AnnualEstablishment> getAnnualEstablistmentReportListFromAuthLogin(String taskId);

    public AerListBox getAnnualEstablistmentReportList(String offcode, int aerid, int isSingle);

    public void addAEReportDataPartA(int aerId, String postGroup, String cadreCode);

    public void addAEReportDataPartB(int aerId, String postGroup, String cadreCode);

    public void addAEReportDataPartC(int aerId);

    public void updateGspcForAERReport(String offcode);

    public List<AnnualEstablishment> getSubmittedReportList(String offcode, String fy, int aerId);

    public List getAerReportList(String offcode, int aerId);

    public Map<String, String> getAuthorityList(String offcode);

    public void addAERMaster(AnnualEstablishment ae, String empId, int aerId);

    public void submitEstablishmentReportAsOperator(int aerId, String offCode, String empId, String spc);

    public void approvedAER(int taskId);

    public String getAERStatus(int taskId);

    public void revertAER(int taskId, String serverfilePath, String fileName, String revertReason);

    public List getScheduleIIData(int aerId, String offcode, int issingleCo);

    public List departmentWiseAerStatus(String finYear);

    public List aerSubmittedOfficeList(String deptCode, String finYear);

    public List getFinancialYearList();

    public List getAERAuthorityList(String offCode);

    public int getisSingleCo(int aerId);

    public String getAERRevertReason(String aerid);

    public List aerApprovedOfficeList(String deptCode, String finYear);

    public String getAerFileName(String aerId);

    public List getProformaGIAEmployeeData(String offcode, String postgrptype);

    public int getGIACount(String offCode);

    public List getPostListAuthorityWise(String offcode, String postcode, String processId);

    public List getmappedPostList(String postcode, String processId);

    public void addHierarchy(WorkflowRouting wr);

    public void removeHierarchy(WorkflowRouting wr);

    public void createOtherEst(AnnualEstablishment bean);

    public AnnualEstablishment[] getOtherEst(int aerid, String PartType);

    public int deleteOtherEst(int aerOtherId);

    public AnnualEstablishment geOtherEstDetals(int aerOtherId);

    public void saveOtherEst(AnnualEstablishment bean);

    public List getGroupAlistBeforeSubmission(String partType, String offCode, String postGroup, int aerId, String cadreCode);

    public void deleteAerData(int aerId, String OffCode);

    public void deleteAerReportData(int aerId, String ddocode);

    public boolean getAerSubmittedStatus(int aerId);

    public int updatePostInformation(String offcode, String gpc, String scaleOfPay6th, String scaleOfPay7th, String postgroup, String hidPostgroup, String level, String gp);

    public int updatePartCPostInformation(String offcode, String gpc, String scaleOfPay6th, String postgroup, String level, String gp);

    public String checkOperator(String offcode, String spc);

    public String checkApprover(String offcode, String spc);

    public String checkReviewer(String offcode, String spc);

    public String checkVerifier(String offcode, String spc);

    public List getTotalGranteeOfficeListWithAerdata(String poffcode, int aerId);

    public void approveAER(int aerid, String empid, String spc, String offcode, String selectedOffCode, String roleType);

    public void saveAerGiaData(AnnualEstablishment bean);

    public AnnualEstablishment getGiaEstDetals(int aerOtherId, String partType, int aerid);

    public void updateAerGiaData(AnnualEstablishment ae);

    public void deleteGiaEst(int aerOtherId, String partType, int aerid);

    public void DownloadaerPDFReport(Document document, String aerId, String offCode);

    public void downloadaerPDFReportScheduleII(Document document, String aerId, String offCode);

    public void declineAER(int aerid, String empid, String spc, String offcode, String revertreason, String roleType);

    public AnnualEstablishment[] getGiaEst(int aerId, String offCode, String partType);

    public boolean privewDataInserted(int aerId);

    public String getRemarks(int aerid, String gpc);

    public void updateRemarks(int aerid, String gpc, String remarks);

    public List getAllGrantinAidOfficesByAerId(int aerId);

    public List getAerViewForCO(String fy, String spc, String coOffCode, String postgrp, String cadreType, String partTYpe, String roletype);

    public void addAEReportDataOther(String offcode, int aerId, String type);

    public ArrayList DeptWiseAERStatus(String fiscalyear);

    public ArrayList DDOWiseAERStatus(String dcode);

    public ArrayList DistWiseAERReport(String fy);

    public ArrayList DistWiseOfficeAERReport(String distcode);

    public ArrayList getAERProcessUserNameList(int aerid);

    public AnnualEstablishment[] getAerViewOtherPartForCO(String fy, String spc, String partTYpe, String roletype);

    public String getAERSubmittedOfficeName(int aerid);

    public ArrayList COWiseAERStatus();

    public ArrayList getAERBillGroupNameList(int aerid);

    public void deleteAERBillGroupNameList(int aerid, BigDecimal billgroupid);

    public int submitAerReportAsVerifier(String empId, String spc, String cooffCode, String aoOffCode, String fy);

    public void updateAerCOAerId(String empId, String spc, String cooffCode, String fy, int coAerId);

    public void insertConsolidatedDataForAOPartABFORGIAandC(int coAerId, String fy, String spc, String cooffCode, String cadreType, String postgrp, String partTYpe);

    public void insertConsolidatedDataForAOPartBDE(String fy, String spc, String partType, int coAerId, String cooffCode);

    public String getcoOffCodeasVerifier(String empId, String fy);

    public String getAoOffCode(String deptCode);

    public List getAerReportListFinancialYearWiseForAOLevel(String offCode, String financialYear, String roleType, String loginSpc);

    public String getSubmitStatusForReviewer(String empid, String spc, String fy, String cooffcode);

    public int getCoAerid(String empid, String spc, String fy, String cooffcode);

    public String getRoleTypeForReviewerAndVerifier(String empid, String spc, String fy, String cooffcode);

    public ArrayList TreasuryWiseAERStatus(String fiscalyear);

    public ArrayList TreasuryWiseOfficeAERReport(String trCode);

    public String getRoleTypeForReviewerAndVerifierOfAO(String empid, String spc, String fy, String cooffcode);

    public int getSchedule2AerId(String loginSpc, String empId, String fy);

    public List getAerViewForAO(String fy, String spc, String aoOffCode, String postgrp, String cadreType, String partTYpe, String roletype);

    public int submitAerReportAsAcceptor(String empId, String spc, String aoOffCode, String fy);

    public void insertConsolidatedDataAfterAcceptorSubmit(int coAerId, String fy, String spc, String cooffCode, String cadreType, String postgrp, String partTYpe);

    public void downloadPDFReportScheduleIII(Document document, String aerId, String offCode);

    public String getSubmitStatusForAcceptor(String empid, String spc, String fy, String cooffcode);

    public int getSchedule3AerId(String loginSpc, String empId, String fy);

    public void updateAerAOAerId(String empId, String spc, String aooffCode, String fy, int aoAerId);

    public ArrayList DeptWiseGroupAERStatus();

    public String checkAODReviewer(String offcode, String spc);

    public String checkAODVerifier(String offcode, String spc);

    public String checkAOAcceptor(String offcode, String spc);

    public ArrayList getAERFlowList(int aerid);

    public int updatePartAPostInformationAtReviewerLevel(int aerid, String gpc, String scaleOfPay6th, String postgroup, String hidPostgroup, String level, String gp);

    public ArrayList DeptWiseGroupAERSan();

    public ArrayList COWiseDeptAERStatus(String fy);

    public void insertConsolidatedDataAfterAcceptorSubmitForBDE(int aoAerId, String fy, String spc, String aooffCode, String postgrp, String partTYpe);

    public ArrayList DeptWiseGroupAERSanCoWise(String fyear);

    public ArrayList DeptWiseGroupAERMIPCoWise(String fyear);

    public ArrayList DeptWiseGroupAERVacancyCoWise(String fyear);

    public ArrayList viewCODeptWise(String dcode, String fy);

    public ArrayList viewCODeptWiseMIP(String dcode, String fy);

    public ArrayList viewCODeptWiseVacancy(String dcode, String fy);

    public ArrayList getDDOtoCOMappingList(String offcode);

    public void changeCadre(int aerid, String gpc, String cadreType);

    public void insertGranteeOfficeData(int aerid, String offcode);

    public int getCOAuthorityApprovedOperatorList(String loginoffcode, String fy, String roletype, String loginspc);

    public String getAERSubmittedCOOfficeName(int coAerid);

    public String getAERSubmittedAOOfficeName(int aoAerid);

    public ArrayList COWiseDDONotSubmited(String dcode);

    public AnnualEstablishment[] getAerViewOtherPartForAO(String fy, String spc, String partTYpe, String roletype);

    public List getVerifierCOOfficeList(String empid, String spc, String fy);

    public List getAerReportListFinancialYearWiseForCOLevelForMultipleCOOffice(String cooffCode, String financialYear, String roleType, String loginSpc);

    public int getSchedule2AerIdForMultipleCOOffice(String loginSpc, String empId, String fy, String cooffcode);

    public List getAerViewForCOMultiple(String fy, String spc, String coOffCode, String postgrp, String cadreType, String partTYpe, String roletype);

    public AnnualEstablishment[] getAerViewOtherPartForCOMultiple(String fy, String spc, String partTYpe, String roletype, String cooffcode);

    public void insertConsolidatedDataForAOPartABFORGIAandCMultipleCO(int coAerId, String fy, String spc, String cooffCode, String cadreType, String postgrp, String partTYpe);

    public void insertConsolidatedDataForAOPartBDEMultipleCO(String fy, String spc, String partType, int coAerId, String cooffCode);

    public List getAcceptorAOOfficeList(String empid, String spc, String fy);

    public String getAcceptorAssignedOffice(String empid, String spc);

    public List getAerViewForMultipleAO(String fy, String spc, String aoOffCode, String postgrp, String cadreType, String partTYpe, String roletype);

    public AnnualEstablishment[] getAerViewOtherPartForMultipleAO(String fy, String spc, String partTYpe, String roletype, String aooffcode);

    public AnnualEstablishment[] getAerViewOtherPartForGIAMultipleAO(String fy, String spc, String partTYpe, String roletype, String aooffcode);

    public void insertConsolidatedDataAfterAcceptorSubmitMultipleAO(int aoAerId, String fy, String spc, String aooffCode, String cadreType, String postgrp, String partTYpe);

    public void insertConsolidatedDataAfterAcceptorSubmitForBDEMultipleAO(int aoAerId, String fy, String spc, String aooffCode, String postgrp, String partTYpe);

    public List getAerReportListFinancialYearWiseForMultipleAOLevel(String aoffCode, String financialYear, String roleType, String loginSpc);

    public int getSchedule3AerIdMultipleAO(String loginSpc, String empId, String fy, String aoffCode);

    public void downloadAERSanctionedStrengthExcel(WritableWorkbook workbook, ArrayList aerdetails, String fyear);

    public void downloadAERMenInPositionExcel(WritableWorkbook workbook, ArrayList aerdetails, String fyear);

    public void downloadAERVacancyExcel(WritableWorkbook workbook, ArrayList aerdetails, String fyear);

    public void disApproveAER(int aerid, String empid, String spc, String offcode, String selectedOffCode, String roleType);

    public List getAERListStatus(String offcode, String fy);

    public List getAerScheduleIListForAOLevel(int coaerid);

    public String getRoleTypeForReviewerAndVerifierOfAODisapprove(String empid, String spc, String fy, String cooffcode);

    public ArrayList viewDDODeptWiseVacancy(String offcode, String coaerid);

    public ArrayList viewDDODeptWiseVacancyPostList(String postgroup, String parttype, String aerid);

    public ArrayList viewDDODeptWiseSancStrength(String offcode, String coaerid);

    public ArrayList viewDDODeptWiseSancStrengthPostList(String postgroup, String parttype, String aerid);

    public ArrayList viewDDODeptWiseMIP(String offcode, String coaerid);

    public ArrayList viewDDODeptWiseMIPPostList(String postgroup, String parttype, String aerid);
    ///---GenericPostWiseStrength For DDO Office---

    public List getGenericPostList(String offcode);

    public void downloadExcelGenericPostWiseStrength(OutputStream out, WritableWorkbook workbook, String fileName, String offCode);

    public List gpcWiseEmployeeMapped(String offCode, String gpc);

    public List getMultipleEmpInOneSPC(String gpc, String offCode);

        /////----GenericPostWiseStrength For CO Office -------
    public List getGenericPostListForCO(String offcode, String offLvl);

    public void downloadExcelGenericPostWiseStrengthForCO(OutputStream out, WritableWorkbook workbook, String fileName, String offCode, String offLvl);

    public List gpcWiseEmployeeMappedForCO(String offCode, String gpc, String offLvl);

    public List getMultipleEmpInOneSPCForCO(String gpc, String offCode, String offLvl);

    public String getAERRevertReasonVerifier(String aerid);

    public List viewMappedSectionAgainstGPC(String offCode, String postcode);
    
    public ArrayList DistWiseOfficeAERReportDC(String fy,String distcode);

}
