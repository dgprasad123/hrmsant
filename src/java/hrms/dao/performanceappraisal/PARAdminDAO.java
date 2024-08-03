/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.performanceappraisal;

import com.itextpdf.text.Document;
import hrms.model.empinfo.SMSGrievance;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.parmast.DepartmentPromotionBean;
import hrms.model.parmast.DepartmentPromotionDetail;
import hrms.model.parmast.PARMessageCommunication;
import hrms.model.parmast.PARSearchResult;
import hrms.model.parmast.PARViewDetailLogBean;
import hrms.model.parmast.ParAdminProperties;
import hrms.model.parmast.ParAdminSearchCriteria;
import hrms.model.parmast.ParAdverseCommunicationDetail;
import hrms.model.parmast.ParApplyForm;
import hrms.model.parmast.ParAssignPrivilage;
import hrms.model.parmast.ParDeleteDetailBean;
import hrms.model.parmast.ParDetail;
import hrms.model.parmast.ParForceForwardBean;
import hrms.model.parmast.ParNrcAttachment;
import hrms.model.parmast.ParStatusBean;
import hrms.model.parmast.ParUnReviewedDetailBean;
import hrms.model.parmast.Parauthorityhelperbean;
import hrms.model.parmast.UploadPreviousPAR;
import java.util.ArrayList;
import java.util.List;
import jxl.write.WritableWorkbook;

/**
 *
 * @author DurgaPrasad
 */
public interface PARAdminDAO {

    public PARSearchResult getPARList(ParAdminSearchCriteria parAdminSearchCriteria);

    public PARSearchResult getOfficeSpecificPARList(ParAdminSearchCriteria parAdminSearchCriteria);

    public PARSearchResult getOfficeCadreSpecificPARList(ParAdminSearchCriteria parAdminSearchCriteria);

    public boolean hasCadrePrivilege(String privilegedSpc);

    public String[] getAssignedAdditionalPost(String empId);

    public PARSearchResult getPARList(String privilegedSpc, String cadrecode, String fiscalyear, String searchCriteria, String searchString, String searchParStatus, String searchCriteria1, int noofrows, int page);

    public PARSearchResult getReviewedParList(ParAdminSearchCriteria parAdminSearchCriteria);

    public ArrayList getPendingPARListAtAuthorityEnd(String privilegedSpc, String cadrecode, String fiscalyear, String searchCriteria, String searchString, String searchParStatus);

    public ArrayList getPARDetails(String fiscalyear, String empId);

    public ParApplyForm getviewParDetail(int parId, int taskId);

    public String revertPARByPARAdmin(String loginempid, ParDetail parDetail);

    public String getAppriseSPCOfPar(int parId);

    public ParApplyForm getviewParDetail(int parId);

    public ParApplyForm getParBriefDetail(int parId);

    public String getLoginempid(String empid);

    public ArrayList getAbsenteeList(int parId);

    public ArrayList getAchievementList(int parId);

    public ArrayList getReportingData(ParApplyForm paf);

    public ArrayList getParAuthority(String authtype, int parId, int taskId);

    public String getAuthorityType(String empId, String spc);

    public String isPendingAcceptingAuthority(String empid, int taskid);

    public String isPendingReviewingAuthority(String empid, int taskid);

    public String isPendingReportingAuthority(String empid, int taskid);

    public String getGradingName(int gradeid);

    public ArrayList getReviewingData(ParApplyForm paf);

    public ArrayList getAcceptingData(ParApplyForm paf);

    public String getHasAdminPriviliged(LoginUserBean lub);

    public ArrayList getCadreListForCustdian(String spc);

    public ParAssignPrivilage getAssignPrivilage(String spc);

    public void UpdateCadre(ParAdminProperties parAdminProperties);

    public void SaveMapPost(ParApplyForm paf);

    public String parReviewRemarks(ParApplyForm paf);

    public void parPdfReview(ParApplyForm paf, Document document);

    public void markParAsReviewed(String reviewedByEmpId, String reviewedBySPC, int parId);

    public void saveUnReviewedPARDetail(ParUnReviewedDetailBean parUnReviewedDetailBean);

    public void saveDeletePARDetail(ParDeleteDetailBean parDeleteDetailBean);

    public ParApplyForm AdverseCommunication(int parId, int taskId);

    public List getAdverseParList();

    public ParApplyForm getNRCDetails(int parId);

    public void DownloadNRCPDF(ParApplyForm paf, Document document);

    public ParNrcAttachment DownloadNRCAttchment(String filepath, int parId);

    public void NRCAccepted(ParApplyForm paf, Document document);
    
    public String saveAssignCadre(ParApplyForm parApplyForm);

    public String saveAssignOfficewisePrivilige(ParApplyForm parApplyForm);

    public ArrayList getAssignPrivilegedList(String cadrecode);

    //public void deleteCadrePrivilage(String cadrecode, String spc, String postGroup);
    
    public void deleteCadrePrivilage(ParApplyForm parApplyForm);

    public ArrayList getOfficewiseAssignPrivilegedList(String offCode);

    public void deleteOfficewisePrivilage(String offCode, String spc, String postGroup);

    public ArrayList getCadreList(String spc);

    public void saveDPCData(DepartmentPromotionBean departmentPromotionBean);

    public DepartmentPromotionBean getDPCData(int dpcId);

    public ArrayList getDPCData(String createdByEmpId);

    public ArrayList getEmployeeListCadrewise(int dpcId);

    public void deleteDPCData(int dpcId);

    public void savecadrewisenrcStatementDetailReport(DepartmentPromotionDetail departmentPromotionDetail);

    public void deletecadrewisenrcStatementDetailReport(DepartmentPromotionDetail departmentPromotionDetail);

    public ArrayList getcadrewisenrcStatementDetailReport(int dpcId);

    public List getParStatusList();

    public void saveAcceptingRemarksForReview(ParApplyForm parApplyForm);

    public void savecustodianremarksAdversePAR(ParAdverseCommunicationDetail parAdverseCommunicationDetail);

    public void savecustodianremarksAdversePARToAppraisee(ParAdverseCommunicationDetail parAdverseCommunicationDetail);

    public void saveparAdverseCommunicationReply(ParAdverseCommunicationDetail parAdverseCommunicationDetail);

    public ArrayList getcustodianremarksAdversePAR(int parId);

    public ParAdverseCommunicationDetail getCommunicationDetails(int communicationId);

    public void markParAsAdverse(int parId);

    public void getAppraiseForAdversePAR(ParAdverseCommunicationDetail parAdverseCommunicationDetail);

    public ParAdverseCommunicationDetail getAttachedFileforAdversePAR(int parId, int communicationinId);

    public Parauthorityhelperbean getParAuthorityForAdverse(String authoritytype, int authid);

    public ArrayList getTotalAdverseCommunicationListForCustdian();

    public List getGroupWiseParStatus(String fiscalyear);

    public void saveEmployeeListForUploadPAR(UploadPreviousPAR uploadPreviousPAR);

    public List getPreviousYearPARUpload(String previousyrParUploadedbyempId);

    public UploadPreviousPAR getAttachedFileOfPreviousYearPAR(int parid);

    public Users getEmployeeProfileInfo(String hrmsid);

    public String isAppraiseeSubmittedPAR(String empId, String fiscalYear);

    public void saveForceForwardDetail(ParForceForwardBean parForceForwardBean);

    public String forceForwardCadrewise(ParForceForwardBean parForceForwardBean);

    public String forceForwardAll(ParForceForwardBean parForceForwardBean);

    //public String forceForwardFromReviewingtoAccepting(ParForceForwardBean parForceForwardBean);
    public String forceForward(ParApplyForm parApplyForm);

    public ArrayList getForceForwardDetailListCadrewise();

    public ArrayList getForceForwardDetailListALLCadre();

    public void getForceForwardError();

    public ArrayList getForceForwardDetailListForAuthority(String fiscalYear, String reportingEmpId);

    public void getcheckForceForwardErrorFromReviewingtoAccepting();

    public void savePARStatusDetailByPARAdmin(ParStatusBean parStatusBean);

    public ArrayList getPARStatusDetailListByPARAdmin();

    public ParStatusBean getAttachedFileforChangePARStatus(int logId);

    public ArrayList getAppraiseDetailListForMessageCommunication(String fiscalYear);

    public ArrayList getPARStatusDetailListForMessageCommunication(String fiscalyear);

    public void saveSendMessageCommunicationToAppraisee(PARMessageCommunication pARMessageCommunication);

    public ArrayList<SMSGrievance> getMessageList();

    public ArrayList getAssignPrivilegedListDistWiseForPolice();

    public void deletePrivilegedListDistWiseForPolice(String spc, String postgrp);

    public ArrayList getPendingPARReportByAdmin(ParAdminSearchCriteria parAdminSearchCriteria);

    public String hasPARCreated(String empid, String fiscalYear);

    public void updateReviewingCompletedPAR();

    public void updateMoreThenOneReviewingCompletedPAR();

    public void updateMoreThenOneReviewingCompletedNRCCasePAR();

    public void downloadExcelForPARReportAtViewer(WritableWorkbook workbook, String currentPost, String cadreCode, String searchCriteria, String searchString, String fiscalyear, String parStatus);

    public ArrayList getPARCadreChangeDetailList(String fiscalYear);
    
    public List getOfficeWiseParListForCustodian(String fiscalYear, String custodianOffCode, String custodianDistCode, String searchCriteria, String searchString);

    public List getofficeWiseParDetails(String hrmsId, String fiscalYear, String encParId);

//Code for SI PAR 
    public List getAssignedPrivilegeListDistWiseForPoliceSiPar();

    public String saveAssignOfficewisePrivilige4SiPar(ParApplyForm parApplyForm);

    public List getOfficeWiseAssignedPrivilegeList4SiPar(String offCode);

    public List getAllSiPARFyWiseList(ParAdminSearchCriteria parAdminSearchCriteria);

    public List getOfficeSpecificSiPARList(ParAdminSearchCriteria parAdminSearchCriteria);

    public List getSiParDetails(String hrmsId, String fiscalYear, String encParId);

    public String checkParViewPrivilege(String spc);

    public ParApplyForm viewTotalSiParDetail(int parId, int taskId);

    public void deleteOfficeWiseSiPrivilage(String offCode, String spc, String postGroup);

    public void deletePrivilegedListDistWiseForPoliceSi(String spc, String postgrp);

    //public List getSiParFyWiseCustodianList(String fiscalYear, String custodianOffCode, String custodianDistCode);
    public List getSiParFyWiseCustodianList(String fiscalYear, String custodianOffCode, String custodianDistCode, String searchCriteria, String searchString);

    public void transferPARListForForceForwardFromReportingToReviewingToLog(ParForceForwardBean parForceForwardBean);

    public void transferPARListForForceForwardFromReviewingToAcceptingToLog(ParForceForwardBean parForceForwardBean);

    public void getAppraiseForAdverseSiPAR(ParAdverseCommunicationDetail parAdverseCommunicationDetail);

    public ArrayList getcustodianremarksAdverseSiPAR(int parId);

    public void savecustodianremarksAdversePARToAppraiseeSiPAR(ParAdverseCommunicationDetail parAdverseCommunicationDetail);

    public void savecustodianremarksAdversesiPAR(ParAdverseCommunicationDetail parAdverseCommunicationDetail);

    public void saveparAdverseCommunicationReplySiPAR(ParAdverseCommunicationDetail parAdverseCommunicationDetail);

    public ParAdverseCommunicationDetail getCommunicationDetailsSiPAR(int communicationId);

    public List getSiEqivAllOfficerDataList(String custodianOffCode);
    
    public ParAdverseCommunicationDetail getSiParAdverseRemarkIntimationPdfData(String custEmpId, int parid);
    
    public List getSiPARNotInitiatedOfficerList(String custodianOffCode,String fiscalyear);
    
    public void saveAdverseRemarksDetailAtCustodianSIPAR(ParAdverseCommunicationDetail parAdverseCommunicationDetail);
    
    public List getAdverseRemarksDetailListAtCustodianSIPAR(int parid);
    
    public ParAdverseCommunicationDetail getAdverseRemarksDetailAtCustodianSIPAR(int parid);
    
    public void getAppraiseDetailForAdverseAtCustodianSiPAR(ParAdverseCommunicationDetail parAdverseCommunicationDetail);
        
    public void UpdatePARViewDetailLog(PARViewDetailLogBean pARViewDetailLogBean);
    
    public ArrayList getPARViewDetailLogList(String fiscalyear);
}
