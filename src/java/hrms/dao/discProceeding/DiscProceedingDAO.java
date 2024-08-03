/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.discProceeding;

import com.itextpdf.text.Document;
import hrms.common.FileDownload;
import hrms.model.discProceeding.CourtCaseBean;
import hrms.model.discProceeding.DefenceBean;
import hrms.model.discProceeding.DiscChargeBean;
import hrms.model.discProceeding.DiscWitnessBean;
import hrms.model.discProceeding.DispatchDetailsBean;
import hrms.model.discProceeding.DpViewBean;
import hrms.model.discProceeding.EnquiryBean;
import hrms.model.discProceeding.FinalOrder;
import hrms.model.discProceeding.IoBean;
import hrms.model.discProceeding.IoReportBean;
import hrms.model.discProceeding.NoticeBean;
import hrms.model.discProceeding.ProceedingBean;
import hrms.model.discProceeding.Rule15ChargeBean;
import hrms.model.discProceeding.SubmitProceedingBean;
import hrms.model.employee.Employee;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface DiscProceedingDAO {

    public List getDiscProcedingFinalList(String empId, int page, int rows);

    public int getDiscProceedTotalCount(String empId);

    public void saveinitiatedDepartmentproceeding(SubmitProceedingBean ProceedingBean);

    public List getPostWithEmpList(String offCode);

    public List getOffWiseEmpWitnessList(String offCode, String hrmsId, int dacId, String mode);

    public void saveCourtCaseDetail(CourtCaseBean courtCasebean);

    public void saveDelinquentOfficer(ProceedingBean dpBean);

    public void saveMemorandumDetails(ProceedingBean dpBean);

    public void saveFirstShowCauseDetails(ProceedingBean dpBean);

    public void saveDefenceStatementByDelinquentOfficerOngoingDP(DefenceBean defencebean);

    public void saveIoDetailsOngoingDP(IoBean ioBean);

    public void saveIoRemarksDetailOngoingDP(IoBean ioBean);

    public void saveSecondShowCauseDetails(ProceedingBean dpBean);

    public void saveRepresentationOfDOOnSecondShowCauseDetails(ProceedingBean dpBean);

    public void saveConsultationDetailsOnRepresentationofdoOnIoreport(ProceedingBean dpBean);

    public void saveConcurranceDetailsOnRepresentationofdoOnIoreport(ProceedingBean dpBean);

    public void saveFinalOrderDetailsOnRepresentationofdoOnIoreport(ProceedingBean dpBean);

    public void saveExanaurationFinalOrderDetailsOnRepresentationofdoOnIoreport(ProceedingBean dpBean);

    public void saveThirdShowCauseDetails(ProceedingBean pbean);

    public void saveThirdShowReplyByDelinquentOfficer(ProceedingBean pbean);

    public ProceedingBean getDiscProceedingData(int daId);

    public ArrayList getCourtCaseDetail(int daId);

    public CourtCaseBean getAttachedFileForCourtCase(int caseId);

    public ProceedingBean getAttachedFileForMemorandumDetails(int daId);

    public ProceedingBean getAttachedFileForFirstShowCauseDetails(int daId);

    public DefenceBean getAttachedFileForDefenceStatementByDelinquentOfficerDetails(int dadid);

    public IoBean getAttachedFileForIoRemarksDetailOngoingDPDetails(int daid);

    public ProceedingBean getAttachedFileForSecondShowCauseDetails(int daid);

    public ProceedingBean getAttachedFileForRepresentationOfDOOnSecondShowCause(int dadid);

    public ProceedingBean getAttachedFileForThirdShowCauseDetails(int daId);

    public ProceedingBean getAttachedFileForThirdShowReplyByDelinquentOfficer(int dadid);

    public DefenceBean getDefenceStatementByDOOngoingDP(int dadid);

    public IoBean getIoDetailsOngoingDP(int daid);

    public IoBean getIoRemarksDetailOngoingDP(int ioremarkId);

    public void deleteDelinquentOfficer(ProceedingBean dpBean);

    public List getDiscChargeList(int daId);

    public List getDiscChargeListWithWitness(int daId);

    public void saveAddWitness(DiscWitnessBean WitnessBean);

    public List getWitnessList(int dacid);

    public List getWitnessListfromdaid(int daId);

    public List getOtherWitnessList(int dacid);

    public DpViewBean getDpdetail(int daId);

    public void deleteWitnessList(int[] selecteddacwid);

    public String saveDiscCharge(DiscChargeBean chargeBean);

    public FileDownload getFileDownloadData(int dacid, String documentTypeName);

    public void deleteDiscCharge(int dacid);

    public ArrayList getInvestingOfficer(int daId);

    public Employee getDelinquentOfficerDtl(int daid, String empId);

    public Employee getDelinquentOfficerDtl(int dadid);

    public Employee[] getDelinquentOfficerForOngoing(int daId);

    public ArrayList getDelinquentOfficer(int daId);

    public int saveRule15MemoDetails(ProceedingBean dpBean);

    public void updateRule15MemoDetails(ProceedingBean dpBean);

    public int getMaxDaId();

    public ProceedingBean getRule15MemoDetails(String offCode, String[] empids);

    public ArrayList getEmpComboDtls(String offCode, String hrmsid);

    public int saveRule15AddCharges(Rule15ChargeBean chargeBean, String filePath);

    public DpViewBean viewRule15DiscProceeding(String offCode, String daId, int taskId);

    public DpViewBean viewRule15DiscProceeding(int daId);

    public int saveRule15AddWitness(Rule15ChargeBean chargeBean);

    public int deleteChargeAndWitness(int dacId);

    public int deleteWitnessOnly(int dacId);

    public Rule15ChargeBean EditRule15ChargeData(Rule15ChargeBean chargeBean, int dacId);

    public int updateRule15Charges(Rule15ChargeBean chargeBean);

    public Rule15ChargeBean EditRule15WitnessData(Rule15ChargeBean chargeBean, int dacId);

    public int rule15UpdateWitness(Rule15ChargeBean chargeBean);

    public DiscChargeBean getChargeDetails(int daId);

    public List getDPForwadrEmpList(String offCode);

    public List getPostWiseEmpList(String postCode);

    public int saveRule15ForwardDP(String authEmpId, String authEmpSpc, Rule15ChargeBean chargeBean);

    public void deleteRule15Proceeding(ProceedingBean pbean);

    public int getDaidFromTaskid(int taskid);

    public int getDaidFromDaioid(int daioid);

    public int getRefidFromTaskid(int taskid);

    public int getDaidAndDefIdFromTaskid(int taskid);

    public int sendFYI(DpViewBean viewbean);

    public void discApprovedPDF(Document document, DpViewBean viewbean);

    public String discProceedingActionUrl(int taskId, String empId);

    public void saveIo(IoBean ioBean);

    public int saveEnquiryReport(EnquiryBean enquirybean);

    public void submitDefenceStatementRefeaing(DefenceBean defencebean);

    public void saveDefenceStatementByDelinquentOfficer(DefenceBean defencebean);

    public void sendNotice1(NoticeBean noticeBean);

    public void saveReplyNotice1(NoticeBean noticeBean);

    public void saveReplyNotice2(NoticeBean noticeBean);

    public List getReplyNotice1(int dadid);

    public List getReplyNotice2(int dadid);

    public void sendNotice2(NoticeBean noticeBean);

    public void sendFYI(NoticeBean noticeBean);

    public void sendFYI2(NoticeBean noticeBean);

    public void discFinalNoticePDF(Document document, DpViewBean viewbean);

    public void sendFinalOrder(FinalOrder finalOrder);

    public int savePenalty(NoticeBean noticeBean);

    public List getPenaltyList(NoticeBean noticeBean);

    public DefenceBean getDefenceDetailFromDadid(int dadid);

    public DefenceBean getDefenceDetailFromDaid(int daId);

    public DefenceBean getDefenceDetailFromTaskId(int taskId);

    public void uploadDefenceStatementAttachedFile(int messageId, String refType, MultipartFile file);

    public void uploadIOAttachedFile(int messageId, String refType, MultipartFile file);

    public int saveIoReportDetails(IoReportBean ioReportBean);

    public IoReportBean getIODetails(int daioid);

    public ArrayList getIoReportList(int daioid);

    public ArrayList getIoDetailList();

    public ArrayList getIoDetailListfromdaid(int daId);

    public void submitIoReportDetails(IoReportBean ioReportBean);

    public void saveDispatchDetails(DispatchDetailsBean dispatchdetailsbean);

    public DispatchDetailsBean removeDispatchFile(int ddid);

    public DispatchDetailsBean getAttachedDispatchFile(int ddid);

    public List getDispatchList(int comTypeRef, String comType);

    public void saveOtherHrmsWitnessList(DiscWitnessBean discWitnessBean);

    public void updatedefenceStatementRefeaing(DiscChargeBean discChargeBean);

    public void updateIoRemark(IoReportBean ioReportBean);

    public void submitIOReportBriefing(IoReportBean ioReportBean);

    public boolean isEditable(int daid);

    public int sendDeclined(DpViewBean viewbean);

}
