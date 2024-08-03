/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.discProceeding;

import hrms.model.employee.Employee;
import java.util.ArrayList;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author manisha
 */
public class ProceedingBean {

    private String deptName;
    private String rule;
    private int daId;
    private String initHrmsId;
    private String initEmpName;
    private String initSpc;
    private String authHrmsId;
    private String authEmpName;
    private String authSpc;
    private String offCode;
    private String offName;
    private String[] delinquent;
    private ArrayList employees;
    private String memoNo;
    private String memoDate;
    private String dpStatus;
    private String annex1Charge;
    private String gender;
    private int taskId;
    private String mode;
    private String firstshowCauseOrdDt;
    private MultipartFile firstshowcausedocument;
    private String firstshowcauseoriginalFileName;
    private String firstshowcausediskFileName;
    private String firstshowcausecontentType;
    private byte[] Filecontent;
    private String isAuthorityApprove;
    private String hasSendNoticetoDO;
    private int dadid;
    private String hasSendSecondNotice;
    private MultipartFile memorandumdocument;
    private String memorandumoriginalFileName;
    private String memorandumdiskFileName;
    private String memorandumcontentType;
    private String OrdNoForNoticetoDOOnIoRemark;
    private String OrdDtForNoticeOnTODOIoRemark;
    private MultipartFile noticetoDOOnIoRemarkdocument;
    private String noticetoDOOnIoRemarkoriginalFileName;
    private String noticetoDOOnIoRemarkdiskFileName;
    private String noticetoDOOnIoRemarkfiletype;
    private String noticetoDOOnIoRemarkfilepath;

    private String showCauseOrdNo;
    private String showCauseOrdDt;
    private String showCauseNotAuthority;
    private String showCauseIssueDate;
    private String showcausedocument;
    private String doWrittenStatementOrdNo;
    private String doWrittenStatementOrdDt;
    private String writtenStatementAuthority;
    private String writtenStatementReplyDate;
    private String writtenStatementReplyByDO;
    private String writtenStatementByDOdocument;
    private String daReplyStatementOrdNo;
    private String daReplyStatementOrdDt;
    private String writtenStatementreplyAuthority;
    private String writtenStatementreplyAuthorityDt;
    private String writtenStatementReplyGivenByDA;
    private String writtenStatementByDAdocument;
    private String ioAppoinmentDetail;
    private String ioAppoinmentDate;
    private String approvalByAuthority;
    private String showcause;
    private String defenceremarkByDO;
    private String isIoAppointed;
    private String isIoReportSubmitted;
    private String serveDelinquentOnIoRemarks;
    private String dahrmsid;
    private String daspc;
    private String daoffice;
    private String dadept;
    
    private String docType;
    private String filetpe;
    private String orgFileName;
    private int defid;
    private int docid;
    private String defhrmsid;
    private String diskfilename;
    private String defenceRemark;
    private MultipartFile uploadDocument;
    private String defspc;
    private String ioappointhrmsid;
    private String ioappointspc;
    private ArrayList<FileAttachBean> attachments;
    private String writtenStatemenyByDOOnDt;
    private MultipartFile defenceByDOdocument;
    private String defenceByDOoriginalFileName;
    private String defenceByDOdiskFileName;
    private String defenceByDOcontentType;
    private String hasReplyByDelinquentOfficer;

    private String ioEmpHrmsId;
    private String ioEmpSPC;
    private String poHrmsId;
    private String poSPC;
    private String apoHrmsId;
    private String apoSPC;
    private String ioEmpName;
    private String ioEmpCurDegn;
    private String ioAppointhrmsId;
    private String ioAppointingspc;
    private String doEmpSpc;
    private String officeName;
    private String[] doio;
    private String officertype;
    private String chkempid;
    private String ioAppoinmentOrdNo;
    private String ioAppoinmentOrdDt;

    private MultipartFile remarksByIOdocument;
    private String remarksByIOoriginalFileName;
    private String remarksByIOdiskFileName;
    private String remarksByIOcontentType;
    private int daioid;
    private String hasIoRemarks;
    private String hasIoAppointed;
    private int ioRemarksId;
    private String ioRemarksOrdNo;
    private String ioRemarksOrdDt;

    private String secondshowCauseOrdDt;
    private String secondshowCauseOrdNo;
    private String doRepresentationOnsecondshowCauseOrdDt;
    private String doRepresentationOnsecondshowCauseOrdNo;
    private String hasDoRepresentOnsecondshowCause;
    private MultipartFile secondshowcausedocumentondoRepresentation;
    private String secondshowcauseoriginalFileNameondoRepresentation;
    private String secondshowcausediskFileNameondoRepresentation;
    private String secondshowcausecontentTypeondoRepresentation;

    private String punishmentProposedOnRepresentationOfDoOnIoReport;

    private String consultationOrdnoOnRepresentationOfDoOnIoReport;
    private String consultationOrddateOnRepresentationOfDoOnIoReport;
    private String consultationOriginalfilenameOnRepresentationOfDoOnIoReport;
    private String consultationdiskfilenameOnRepresentationOfDoOnIoReport;
    private String consultationfiletypeOnRepresentationOfDoOnIoReport;
    private String consultationfilepathOnRepresentationOfDoOnIoReport;
    private MultipartFile consultationdocumentOnRepresentationOfDoOnIoReport;

    private String concurranceOrdnoOnRepresentationOfDoOnIoReport;
    private String concurranceOrddateOnRepresentationOfDoOnIoReport;
    private String concurranceOriginalfilenameOnRepresentationOfDoOnIoReport;
    private String concurrancediskfilenameOnRepresentationOfDoOnIoReport;
    private String concurrancefiletypeOnRepresentationOfDoOnIoReport;
    private String concurrancefilepathOnRepresentationOfDoOnIoReport;
    private MultipartFile concurrancedocumentOnRepresentationOfDoOnIoReport;

    private String finalOrdnoOnRepresentationOfDoOnIoReport;
    private String finalOrddateOnRepresentationOfDoOnIoReport;
    private String finalOriginalfilenameOnRepresentationOfDoOnIoReport;
    private String finaldiskfilenameOnRepresentationOfDoOnIoReport;
    private String finalfiletypeOnRepresentationOfDoOnIoReport;
    private String finalfilepathOnRepresentationOfDoOnIoReport;
    private MultipartFile finaldocumentOnRepresentationOfDoOnIoReport;

    private String exanaurationfinalOrdnoOnRepresentationOfDoOnIoReport;
    private String exanaurationfinalOrddateOnRepresentationOfDoOnIoReport;
    private String exanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport;
    private String exanaurationfinaldiskfilenameOnRepresentationOfDoOnIoReport;
    private String exanaurationfinalfiletypeOnRepresentationOfDoOnIoReport;
    private String exanaurationfinalfilepathOnRepresentationOfDoOnIoReport;
    private MultipartFile exanaurationfinaldocumentOnRepresentationOfDoOnIoReport;
    private String disciplinaryauthority;
    private String inquiryauthority;
    private String presentingauthority;
    private String additionalpresentingauthority;

    private String thirdshowCauseOrdDt;
    private String hasSendthirdshowCause;
    private String thirdNoticeToDelinquentPunishmentProposed;
    private String OrdNoForthirdNoticetoDOOnForPunishment;
    private String OrdDtForthirdNoticetoDOOnForPunishment;
    private MultipartFile thirdNoticetoDOOnForPunishmentdocument;
    private String thirdNoticetoDOOnForPunishmentorgFileName;
    private String  thirdNoticetoDOOnForPunishmentdiskFileName;
    private String  thirdNoticetoDOOnForPunishmentFileType;
    private String doRepresentationOnThirdshowCauseOrdNo;
    private String doRepresentationOnThirdshowCauseOrdDt;
    private MultipartFile thirdshowcausedocumentondoRepresentation;
    private String hasDoRepresentOnthirdshowCause;
    private String thirdshowcauseOriginalfilenameOnRepresentationOfDoOnIoReport;
    private String thirdshowcausediskfilenameOnRepresentationOfDoOnIoReport;
    private String thirdshowcausefiletypeOnRepresentationOfDoOnIoReport;
    private String thirdshowcausefilepathOnRepresentationOfDoOnIoReport;
    private String thirdshowCauseReplyByDAOrdDt;
    private String thirdshowCauseReplyByDAordNo;
    private String hasReplyByDothirdshowCause;

    private String ProposedpunishmentOnRepresentationOfDOOnpunishment;
    
    private String exanaurationfinalOrdnumberOnopscConcurrance;
    private String exanaurationfinalOrddateOnopscConcurrance;
    
    private String exanaurationfinalOrddateafterPunishment;
    private String exanaurationfinalOrdNumberafterPunishment;
    
    private MultipartFile exanaurationfinaldocumentOnopscConcurrance;
    private String exanaurationfinalOriginalfilenameOnopscConcurrance;
    private String exanaurationfinaldiskfilenameOnopscConcurrance;
    private String exanaurationfinalOriginalfiletypeOnopscConcurrance;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public int getDaId() {
        return daId;
    }

    public void setDaId(int daId) {
        this.daId = daId;
    }

    public String getInitHrmsId() {
        return initHrmsId;
    }

    public void setInitHrmsId(String initHrmsId) {
        this.initHrmsId = initHrmsId;
    }

    public String getInitEmpName() {
        return initEmpName;
    }

    public void setInitEmpName(String initEmpName) {
        this.initEmpName = initEmpName;
    }

    public String getInitSpc() {
        return initSpc;
    }

    public void setInitSpc(String initSpc) {
        this.initSpc = initSpc;
    }

    public String getAuthHrmsId() {
        return authHrmsId;
    }

    public void setAuthHrmsId(String authHrmsId) {
        this.authHrmsId = authHrmsId;
    }

    public String getAuthEmpName() {
        return authEmpName;
    }

    public void setAuthEmpName(String authEmpName) {
        this.authEmpName = authEmpName;
    }

    public String getAuthSpc() {
        return authSpc;
    }

    public void setAuthSpc(String authSpc) {
        this.authSpc = authSpc;
    }

    public String getOffCode() {
        return offCode;
    }

    public void setOffCode(String offCode) {
        this.offCode = offCode;
    }

    public String getOffName() {
        return offName;
    }

    public void setOffName(String offName) {
        this.offName = offName;
    }

    public String[] getDelinquent() {
        return delinquent;
    }

    public void setDelinquent(String[] delinquent) {
        this.delinquent = delinquent;
    }

    public ArrayList getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList employees) {
        this.employees = employees;
    }

    public String getMemoNo() {
        return memoNo;
    }

    public void setMemoNo(String memoNo) {
        this.memoNo = memoNo;
    }

    public String getMemoDate() {
        return memoDate;
    }

    public void setMemoDate(String memoDate) {
        this.memoDate = memoDate;
    }

    public String getDpStatus() {
        return dpStatus;
    }

    public void setDpStatus(String dpStatus) {
        this.dpStatus = dpStatus;
    }

    public String getAnnex1Charge() {
        return annex1Charge;
    }

    public void setAnnex1Charge(String annex1Charge) {
        this.annex1Charge = annex1Charge;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getFirstshowCauseOrdDt() {
        return firstshowCauseOrdDt;
    }

    public void setFirstshowCauseOrdDt(String firstshowCauseOrdDt) {
        this.firstshowCauseOrdDt = firstshowCauseOrdDt;
    }

    public MultipartFile getFirstshowcausedocument() {
        return firstshowcausedocument;
    }

    public void setFirstshowcausedocument(MultipartFile firstshowcausedocument) {
        this.firstshowcausedocument = firstshowcausedocument;
    }

    public String getFirstshowcauseoriginalFileName() {
        return firstshowcauseoriginalFileName;
    }

    public void setFirstshowcauseoriginalFileName(String firstshowcauseoriginalFileName) {
        this.firstshowcauseoriginalFileName = firstshowcauseoriginalFileName;
    }

    public String getFirstshowcausediskFileName() {
        return firstshowcausediskFileName;
    }

    public void setFirstshowcausediskFileName(String firstshowcausediskFileName) {
        this.firstshowcausediskFileName = firstshowcausediskFileName;
    }

    public String getFirstshowcausecontentType() {
        return firstshowcausecontentType;
    }

    public void setFirstshowcausecontentType(String firstshowcausecontentType) {
        this.firstshowcausecontentType = firstshowcausecontentType;
    }

    public byte[] getFilecontent() {
        return Filecontent;
    }

    public void setFilecontent(byte[] Filecontent) {
        this.Filecontent = Filecontent;
    }

    public String getIsAuthorityApprove() {
        return isAuthorityApprove;
    }

    public void setIsAuthorityApprove(String isAuthorityApprove) {
        this.isAuthorityApprove = isAuthorityApprove;
    }

    public String getHasSendNoticetoDO() {
        return hasSendNoticetoDO;
    }

    public void setHasSendNoticetoDO(String hasSendNoticetoDO) {
        this.hasSendNoticetoDO = hasSendNoticetoDO;
    }

    public String getWrittenStatemenyByDOOnDt() {
        return writtenStatemenyByDOOnDt;
    }

    public void setWrittenStatemenyByDOOnDt(String writtenStatemenyByDOOnDt) {
        this.writtenStatemenyByDOOnDt = writtenStatemenyByDOOnDt;
    }

    public int getDadid() {
        return dadid;
    }

    public void setDadid(int dadid) {
        this.dadid = dadid;
    }

    public MultipartFile getMemorandumdocument() {
        return memorandumdocument;
    }

    public void setMemorandumdocument(MultipartFile memorandumdocument) {
        this.memorandumdocument = memorandumdocument;
    }

    public String getMemorandumoriginalFileName() {
        return memorandumoriginalFileName;
    }

    public void setMemorandumoriginalFileName(String memorandumoriginalFileName) {
        this.memorandumoriginalFileName = memorandumoriginalFileName;
    }

    public String getMemorandumdiskFileName() {
        return memorandumdiskFileName;
    }

    public void setMemorandumdiskFileName(String memorandumdiskFileName) {
        this.memorandumdiskFileName = memorandumdiskFileName;
    }

    public String getMemorandumcontentType() {
        return memorandumcontentType;
    }

    public void setMemorandumcontentType(String memorandumcontentType) {
        this.memorandumcontentType = memorandumcontentType;
    }

    public String getOrdNoForNoticetoDOOnIoRemark() {
        return OrdNoForNoticetoDOOnIoRemark;
    }

    public void setOrdNoForNoticetoDOOnIoRemark(String OrdNoForNoticetoDOOnIoRemark) {
        this.OrdNoForNoticetoDOOnIoRemark = OrdNoForNoticetoDOOnIoRemark;
    }

    public String getOrdDtForNoticeOnTODOIoRemark() {
        return OrdDtForNoticeOnTODOIoRemark;
    }

    public void setOrdDtForNoticeOnTODOIoRemark(String OrdDtForNoticeOnTODOIoRemark) {
        this.OrdDtForNoticeOnTODOIoRemark = OrdDtForNoticeOnTODOIoRemark;
    }


    public MultipartFile getNoticetoDOOnIoRemarkdocument() {
        return noticetoDOOnIoRemarkdocument;
    }

    public void setNoticetoDOOnIoRemarkdocument(MultipartFile noticetoDOOnIoRemarkdocument) {
        this.noticetoDOOnIoRemarkdocument = noticetoDOOnIoRemarkdocument;
    }

   

    public String getNoticetoDOOnIoRemarkoriginalFileName() {
        return noticetoDOOnIoRemarkoriginalFileName;
    }

    public void setNoticetoDOOnIoRemarkoriginalFileName(String noticetoDOOnIoRemarkoriginalFileName) {
        this.noticetoDOOnIoRemarkoriginalFileName = noticetoDOOnIoRemarkoriginalFileName;
    }

    public String getNoticetoDOOnIoRemarkdiskFileName() {
        return noticetoDOOnIoRemarkdiskFileName;
    }

    public void setNoticetoDOOnIoRemarkdiskFileName(String noticetoDOOnIoRemarkdiskFileName) {
        this.noticetoDOOnIoRemarkdiskFileName = noticetoDOOnIoRemarkdiskFileName;
    }

    public String getNoticetoDOOnIoRemarkfiletype() {
        return noticetoDOOnIoRemarkfiletype;
    }

    public void setNoticetoDOOnIoRemarkfiletype(String noticetoDOOnIoRemarkfiletype) {
        this.noticetoDOOnIoRemarkfiletype = noticetoDOOnIoRemarkfiletype;
    }

    public String getNoticetoDOOnIoRemarkfilepath() {
        return noticetoDOOnIoRemarkfilepath;
    }

    public void setNoticetoDOOnIoRemarkfilepath(String noticetoDOOnIoRemarkfilepath) {
        this.noticetoDOOnIoRemarkfilepath = noticetoDOOnIoRemarkfilepath;
    }

    public String getSecondshowCauseOrdDt() {
        return secondshowCauseOrdDt;
    }

    public void setSecondshowCauseOrdDt(String secondshowCauseOrdDt) {
        this.secondshowCauseOrdDt = secondshowCauseOrdDt;
    }

    public String getHasSendSecondNotice() {
        return hasSendSecondNotice;
    }

    public void setHasSendSecondNotice(String hasSendSecondNotice) {
        this.hasSendSecondNotice = hasSendSecondNotice;
    }

    public String getShowCauseOrdNo() {
        return showCauseOrdNo;
    }

    public void setShowCauseOrdNo(String showCauseOrdNo) {
        this.showCauseOrdNo = showCauseOrdNo;
    }

    public String getShowCauseOrdDt() {
        return showCauseOrdDt;
    }

    public void setShowCauseOrdDt(String showCauseOrdDt) {
        this.showCauseOrdDt = showCauseOrdDt;
    }

    public String getShowCauseNotAuthority() {
        return showCauseNotAuthority;
    }

    public void setShowCauseNotAuthority(String showCauseNotAuthority) {
        this.showCauseNotAuthority = showCauseNotAuthority;
    }

    public String getShowCauseIssueDate() {
        return showCauseIssueDate;
    }

    public void setShowCauseIssueDate(String showCauseIssueDate) {
        this.showCauseIssueDate = showCauseIssueDate;
    }

    public String getShowcausedocument() {
        return showcausedocument;
    }

    public void setShowcausedocument(String showcausedocument) {
        this.showcausedocument = showcausedocument;
    }

    public String getDoWrittenStatementOrdNo() {
        return doWrittenStatementOrdNo;
    }

    public void setDoWrittenStatementOrdNo(String doWrittenStatementOrdNo) {
        this.doWrittenStatementOrdNo = doWrittenStatementOrdNo;
    }

    public String getDoWrittenStatementOrdDt() {
        return doWrittenStatementOrdDt;
    }

    public void setDoWrittenStatementOrdDt(String doWrittenStatementOrdDt) {
        this.doWrittenStatementOrdDt = doWrittenStatementOrdDt;
    }

    public String getWrittenStatementAuthority() {
        return writtenStatementAuthority;
    }

    public void setWrittenStatementAuthority(String writtenStatementAuthority) {
        this.writtenStatementAuthority = writtenStatementAuthority;
    }

    public String getWrittenStatementReplyDate() {
        return writtenStatementReplyDate;
    }

    public void setWrittenStatementReplyDate(String writtenStatementReplyDate) {
        this.writtenStatementReplyDate = writtenStatementReplyDate;
    }

    public String getWrittenStatementReplyByDO() {
        return writtenStatementReplyByDO;
    }

    public void setWrittenStatementReplyByDO(String writtenStatementReplyByDO) {
        this.writtenStatementReplyByDO = writtenStatementReplyByDO;
    }

    public String getWrittenStatementByDOdocument() {
        return writtenStatementByDOdocument;
    }

    public void setWrittenStatementByDOdocument(String writtenStatementByDOdocument) {
        this.writtenStatementByDOdocument = writtenStatementByDOdocument;
    }

    public String getDaReplyStatementOrdNo() {
        return daReplyStatementOrdNo;
    }

    public void setDaReplyStatementOrdNo(String daReplyStatementOrdNo) {
        this.daReplyStatementOrdNo = daReplyStatementOrdNo;
    }

    public String getDaReplyStatementOrdDt() {
        return daReplyStatementOrdDt;
    }

    public void setDaReplyStatementOrdDt(String daReplyStatementOrdDt) {
        this.daReplyStatementOrdDt = daReplyStatementOrdDt;
    }

    public String getWrittenStatementreplyAuthority() {
        return writtenStatementreplyAuthority;
    }

    public void setWrittenStatementreplyAuthority(String writtenStatementreplyAuthority) {
        this.writtenStatementreplyAuthority = writtenStatementreplyAuthority;
    }

    public String getWrittenStatementreplyAuthorityDt() {
        return writtenStatementreplyAuthorityDt;
    }

    public void setWrittenStatementreplyAuthorityDt(String writtenStatementreplyAuthorityDt) {
        this.writtenStatementreplyAuthorityDt = writtenStatementreplyAuthorityDt;
    }

    public String getWrittenStatementReplyGivenByDA() {
        return writtenStatementReplyGivenByDA;
    }

    public void setWrittenStatementReplyGivenByDA(String writtenStatementReplyGivenByDA) {
        this.writtenStatementReplyGivenByDA = writtenStatementReplyGivenByDA;
    }

    public String getWrittenStatementByDAdocument() {
        return writtenStatementByDAdocument;
    }

    public void setWrittenStatementByDAdocument(String writtenStatementByDAdocument) {
        this.writtenStatementByDAdocument = writtenStatementByDAdocument;
    }

    public String getIoAppoinmentOrdNo() {
        return ioAppoinmentOrdNo;
    }

    public void setIoAppoinmentOrdNo(String ioAppoinmentOrdNo) {
        this.ioAppoinmentOrdNo = ioAppoinmentOrdNo;
    }

    public String getIoAppoinmentOrdDt() {
        return ioAppoinmentOrdDt;
    }

    public void setIoAppoinmentOrdDt(String ioAppoinmentOrdDt) {
        this.ioAppoinmentOrdDt = ioAppoinmentOrdDt;
    }

    public String getIoAppoinmentDetail() {
        return ioAppoinmentDetail;
    }

    public void setIoAppoinmentDetail(String ioAppoinmentDetail) {
        this.ioAppoinmentDetail = ioAppoinmentDetail;
    }

    public String getIoAppoinmentDate() {
        return ioAppoinmentDate;
    }

    public void setIoAppoinmentDate(String ioAppoinmentDate) {
        this.ioAppoinmentDate = ioAppoinmentDate;
    }

    public String getApprovalByAuthority() {
        return approvalByAuthority;
    }

    public void setApprovalByAuthority(String approvalByAuthority) {
        this.approvalByAuthority = approvalByAuthority;
    }

    public String getShowcause() {
        return showcause;
    }

    public void setShowcause(String showcause) {
        this.showcause = showcause;
    }

    public String getDefenceremarkByDO() {
        return defenceremarkByDO;
    }

    public void setDefenceremarkByDO(String defenceremarkByDO) {
        this.defenceremarkByDO = defenceremarkByDO;
    }

    public String getIsIoAppointed() {
        return isIoAppointed;
    }

    public void setIsIoAppointed(String isIoAppointed) {
        this.isIoAppointed = isIoAppointed;
    }

    public String getIsIoReportSubmitted() {
        return isIoReportSubmitted;
    }

    public void setIsIoReportSubmitted(String isIoReportSubmitted) {
        this.isIoReportSubmitted = isIoReportSubmitted;
    }

    public String getServeDelinquentOnIoRemarks() {
        return serveDelinquentOnIoRemarks;
    }

    public void setServeDelinquentOnIoRemarks(String serveDelinquentOnIoRemarks) {
        this.serveDelinquentOnIoRemarks = serveDelinquentOnIoRemarks;
    }

    public String getDahrmsid() {
        return dahrmsid;
    }

    public void setDahrmsid(String dahrmsid) {
        this.dahrmsid = dahrmsid;
    }

    public String getDaspc() {
        return daspc;
    }

    public void setDaspc(String daspc) {
        this.daspc = daspc;
    }

    public String getDaoffice() {
        return daoffice;
    }

    public void setDaoffice(String daoffice) {
        this.daoffice = daoffice;
    }

    public String getDadept() {
        return dadept;
    }

    public void setDadept(String dadept) {
        this.dadept = dadept;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getFiletpe() {
        return filetpe;
    }

    public void setFiletpe(String filetpe) {
        this.filetpe = filetpe;
    }

    public String getOrgFileName() {
        return orgFileName;
    }

    public void setOrgFileName(String orgFileName) {
        this.orgFileName = orgFileName;
    }

    public int getDefid() {
        return defid;
    }

    public void setDefid(int defid) {
        this.defid = defid;
    }

    public int getDocid() {
        return docid;
    }

    public void setDocid(int docid) {
        this.docid = docid;
    }

    public String getDefhrmsid() {
        return defhrmsid;
    }

    public void setDefhrmsid(String defhrmsid) {
        this.defhrmsid = defhrmsid;
    }

    public String getDiskfilename() {
        return diskfilename;
    }

    public void setDiskfilename(String diskfilename) {
        this.diskfilename = diskfilename;
    }

    public String getDefenceRemark() {
        return defenceRemark;
    }

    public void setDefenceRemark(String defenceRemark) {
        this.defenceRemark = defenceRemark;
    }

    public MultipartFile getUploadDocument() {
        return uploadDocument;
    }

    public void setUploadDocument(MultipartFile uploadDocument) {
        this.uploadDocument = uploadDocument;
    }

    public String getDefspc() {
        return defspc;
    }

    public void setDefspc(String defspc) {
        this.defspc = defspc;
    }

    public String getIoappointhrmsid() {
        return ioappointhrmsid;
    }

    public void setIoappointhrmsid(String ioappointhrmsid) {
        this.ioappointhrmsid = ioappointhrmsid;
    }

    public String getIoappointspc() {
        return ioappointspc;
    }

    public void setIoappointspc(String ioappointspc) {
        this.ioappointspc = ioappointspc;
    }

    public ArrayList<FileAttachBean> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<FileAttachBean> attachments) {
        this.attachments = attachments;
    }

    public MultipartFile getDefenceByDOdocument() {
        return defenceByDOdocument;
    }

    public void setDefenceByDOdocument(MultipartFile defenceByDOdocument) {
        this.defenceByDOdocument = defenceByDOdocument;
    }

    public String getDefenceByDOoriginalFileName() {
        return defenceByDOoriginalFileName;
    }

    public void setDefenceByDOoriginalFileName(String defenceByDOoriginalFileName) {
        this.defenceByDOoriginalFileName = defenceByDOoriginalFileName;
    }

    public String getDefenceByDOdiskFileName() {
        return defenceByDOdiskFileName;
    }

    public void setDefenceByDOdiskFileName(String defenceByDOdiskFileName) {
        this.defenceByDOdiskFileName = defenceByDOdiskFileName;
    }

    public String getDefenceByDOcontentType() {
        return defenceByDOcontentType;
    }

    public void setDefenceByDOcontentType(String defenceByDOcontentType) {
        this.defenceByDOcontentType = defenceByDOcontentType;
    }

    public String getHasReplyByDelinquentOfficer() {
        return hasReplyByDelinquentOfficer;
    }

    public void setHasReplyByDelinquentOfficer(String hasReplyByDelinquentOfficer) {
        this.hasReplyByDelinquentOfficer = hasReplyByDelinquentOfficer;
    }

    public String getIoEmpHrmsId() {
        return ioEmpHrmsId;
    }

    public void setIoEmpHrmsId(String ioEmpHrmsId) {
        this.ioEmpHrmsId = ioEmpHrmsId;
    }

    public String getIoEmpSPC() {
        return ioEmpSPC;
    }

    public void setIoEmpSPC(String ioEmpSPC) {
        this.ioEmpSPC = ioEmpSPC;
    }

    public String getPoHrmsId() {
        return poHrmsId;
    }

    public void setPoHrmsId(String poHrmsId) {
        this.poHrmsId = poHrmsId;
    }

    public String getPoSPC() {
        return poSPC;
    }

    public void setPoSPC(String poSPC) {
        this.poSPC = poSPC;
    }

    public String getApoHrmsId() {
        return apoHrmsId;
    }

    public void setApoHrmsId(String apoHrmsId) {
        this.apoHrmsId = apoHrmsId;
    }

    public String getApoSPC() {
        return apoSPC;
    }

    public void setApoSPC(String apoSPC) {
        this.apoSPC = apoSPC;
    }

    public String getIoEmpName() {
        return ioEmpName;
    }

    public void setIoEmpName(String ioEmpName) {
        this.ioEmpName = ioEmpName;
    }

    public String getIoEmpCurDegn() {
        return ioEmpCurDegn;
    }

    public void setIoEmpCurDegn(String ioEmpCurDegn) {
        this.ioEmpCurDegn = ioEmpCurDegn;
    }

    public String getIoAppointhrmsId() {
        return ioAppointhrmsId;
    }

    public void setIoAppointhrmsId(String ioAppointhrmsId) {
        this.ioAppointhrmsId = ioAppointhrmsId;
    }

    public String getIoAppointingspc() {
        return ioAppointingspc;
    }

    public void setIoAppointingspc(String ioAppointingspc) {
        this.ioAppointingspc = ioAppointingspc;
    }

    public String getDoEmpSpc() {
        return doEmpSpc;
    }

    public void setDoEmpSpc(String doEmpSpc) {
        this.doEmpSpc = doEmpSpc;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String[] getDoio() {
        return doio;
    }

    public void setDoio(String[] doio) {
        this.doio = doio;
    }

    public String getOfficertype() {
        return officertype;
    }

    public void setOfficertype(String officertype) {
        this.officertype = officertype;
    }

    public String getChkempid() {
        return chkempid;
    }

    public void setChkempid(String chkempid) {
        this.chkempid = chkempid;
    }

    public MultipartFile getRemarksByIOdocument() {
        return remarksByIOdocument;
    }

    public void setRemarksByIOdocument(MultipartFile remarksByIOdocument) {
        this.remarksByIOdocument = remarksByIOdocument;
    }

    public String getRemarksByIOoriginalFileName() {
        return remarksByIOoriginalFileName;
    }

    public void setRemarksByIOoriginalFileName(String remarksByIOoriginalFileName) {
        this.remarksByIOoriginalFileName = remarksByIOoriginalFileName;
    }

    public String getRemarksByIOdiskFileName() {
        return remarksByIOdiskFileName;
    }

    public void setRemarksByIOdiskFileName(String remarksByIOdiskFileName) {
        this.remarksByIOdiskFileName = remarksByIOdiskFileName;
    }

    public String getRemarksByIOcontentType() {
        return remarksByIOcontentType;
    }

    public void setRemarksByIOcontentType(String remarksByIOcontentType) {
        this.remarksByIOcontentType = remarksByIOcontentType;
    }

    public int getDaioid() {
        return daioid;
    }

    public void setDaioid(int daioid) {
        this.daioid = daioid;
    }

    public String getHasIoRemarks() {
        return hasIoRemarks;
    }

    public void setHasIoRemarks(String hasIoRemarks) {
        this.hasIoRemarks = hasIoRemarks;
    }

    public String getHasIoAppointed() {
        return hasIoAppointed;
    }

    public void setHasIoAppointed(String hasIoAppointed) {
        this.hasIoAppointed = hasIoAppointed;
    }

    public int getIoRemarksId() {
        return ioRemarksId;
    }

    public void setIoRemarksId(int ioRemarksId) {
        this.ioRemarksId = ioRemarksId;
    }

    public String getIoRemarksOrdNo() {
        return ioRemarksOrdNo;
    }

    public void setIoRemarksOrdNo(String ioRemarksOrdNo) {
        this.ioRemarksOrdNo = ioRemarksOrdNo;
    }

    public String getIoRemarksOrdDt() {
        return ioRemarksOrdDt;
    }

    public void setIoRemarksOrdDt(String ioRemarksOrdDt) {
        this.ioRemarksOrdDt = ioRemarksOrdDt;
    }

    public String getSecondshowCauseOrdNo() {
        return secondshowCauseOrdNo;
    }

    public void setSecondshowCauseOrdNo(String secondshowCauseOrdNo) {
        this.secondshowCauseOrdNo = secondshowCauseOrdNo;
    }

    public String getDoRepresentationOnsecondshowCauseOrdDt() {
        return doRepresentationOnsecondshowCauseOrdDt;
    }

    public void setDoRepresentationOnsecondshowCauseOrdDt(String doRepresentationOnsecondshowCauseOrdDt) {
        this.doRepresentationOnsecondshowCauseOrdDt = doRepresentationOnsecondshowCauseOrdDt;
    }

    public String getDoRepresentationOnsecondshowCauseOrdNo() {
        return doRepresentationOnsecondshowCauseOrdNo;
    }

    public void setDoRepresentationOnsecondshowCauseOrdNo(String doRepresentationOnsecondshowCauseOrdNo) {
        this.doRepresentationOnsecondshowCauseOrdNo = doRepresentationOnsecondshowCauseOrdNo;
    }

    public String getHasDoRepresentOnsecondshowCause() {
        return hasDoRepresentOnsecondshowCause;
    }

    public void setHasDoRepresentOnsecondshowCause(String hasDoRepresentOnsecondshowCause) {
        this.hasDoRepresentOnsecondshowCause = hasDoRepresentOnsecondshowCause;
    }

    public MultipartFile getSecondshowcausedocumentondoRepresentation() {
        return secondshowcausedocumentondoRepresentation;
    }

    public void setSecondshowcausedocumentondoRepresentation(MultipartFile secondshowcausedocumentondoRepresentation) {
        this.secondshowcausedocumentondoRepresentation = secondshowcausedocumentondoRepresentation;
    }

    public String getSecondshowcauseoriginalFileNameondoRepresentation() {
        return secondshowcauseoriginalFileNameondoRepresentation;
    }

    public void setSecondshowcauseoriginalFileNameondoRepresentation(String secondshowcauseoriginalFileNameondoRepresentation) {
        this.secondshowcauseoriginalFileNameondoRepresentation = secondshowcauseoriginalFileNameondoRepresentation;
    }

    public String getSecondshowcausediskFileNameondoRepresentation() {
        return secondshowcausediskFileNameondoRepresentation;
    }

    public void setSecondshowcausediskFileNameondoRepresentation(String secondshowcausediskFileNameondoRepresentation) {
        this.secondshowcausediskFileNameondoRepresentation = secondshowcausediskFileNameondoRepresentation;
    }

    public String getSecondshowcausecontentTypeondoRepresentation() {
        return secondshowcausecontentTypeondoRepresentation;
    }

    public void setSecondshowcausecontentTypeondoRepresentation(String secondshowcausecontentTypeondoRepresentation) {
        this.secondshowcausecontentTypeondoRepresentation = secondshowcausecontentTypeondoRepresentation;
    }

    public String getPunishmentProposedOnRepresentationOfDoOnIoReport() {
        return punishmentProposedOnRepresentationOfDoOnIoReport;
    }

    public void setPunishmentProposedOnRepresentationOfDoOnIoReport(String punishmentProposedOnRepresentationOfDoOnIoReport) {
        this.punishmentProposedOnRepresentationOfDoOnIoReport = punishmentProposedOnRepresentationOfDoOnIoReport;
    }

    public String getConsultationOrdnoOnRepresentationOfDoOnIoReport() {
        return consultationOrdnoOnRepresentationOfDoOnIoReport;
    }

    public void setConsultationOrdnoOnRepresentationOfDoOnIoReport(String consultationOrdnoOnRepresentationOfDoOnIoReport) {
        this.consultationOrdnoOnRepresentationOfDoOnIoReport = consultationOrdnoOnRepresentationOfDoOnIoReport;
    }

    public String getConsultationOrddateOnRepresentationOfDoOnIoReport() {
        return consultationOrddateOnRepresentationOfDoOnIoReport;
    }

    public void setConsultationOrddateOnRepresentationOfDoOnIoReport(String consultationOrddateOnRepresentationOfDoOnIoReport) {
        this.consultationOrddateOnRepresentationOfDoOnIoReport = consultationOrddateOnRepresentationOfDoOnIoReport;
    }

    public String getConsultationOriginalfilenameOnRepresentationOfDoOnIoReport() {
        return consultationOriginalfilenameOnRepresentationOfDoOnIoReport;
    }

    public void setConsultationOriginalfilenameOnRepresentationOfDoOnIoReport(String consultationOriginalfilenameOnRepresentationOfDoOnIoReport) {
        this.consultationOriginalfilenameOnRepresentationOfDoOnIoReport = consultationOriginalfilenameOnRepresentationOfDoOnIoReport;
    }

    public String getConsultationdiskfilenameOnRepresentationOfDoOnIoReport() {
        return consultationdiskfilenameOnRepresentationOfDoOnIoReport;
    }

    public void setConsultationdiskfilenameOnRepresentationOfDoOnIoReport(String consultationdiskfilenameOnRepresentationOfDoOnIoReport) {
        this.consultationdiskfilenameOnRepresentationOfDoOnIoReport = consultationdiskfilenameOnRepresentationOfDoOnIoReport;
    }

    public String getConsultationfiletypeOnRepresentationOfDoOnIoReport() {
        return consultationfiletypeOnRepresentationOfDoOnIoReport;
    }

    public void setConsultationfiletypeOnRepresentationOfDoOnIoReport(String consultationfiletypeOnRepresentationOfDoOnIoReport) {
        this.consultationfiletypeOnRepresentationOfDoOnIoReport = consultationfiletypeOnRepresentationOfDoOnIoReport;
    }

    public String getConsultationfilepathOnRepresentationOfDoOnIoReport() {
        return consultationfilepathOnRepresentationOfDoOnIoReport;
    }

    public void setConsultationfilepathOnRepresentationOfDoOnIoReport(String consultationfilepathOnRepresentationOfDoOnIoReport) {
        this.consultationfilepathOnRepresentationOfDoOnIoReport = consultationfilepathOnRepresentationOfDoOnIoReport;
    }

    public MultipartFile getConsultationdocumentOnRepresentationOfDoOnIoReport() {
        return consultationdocumentOnRepresentationOfDoOnIoReport;
    }

    public void setConsultationdocumentOnRepresentationOfDoOnIoReport(MultipartFile consultationdocumentOnRepresentationOfDoOnIoReport) {
        this.consultationdocumentOnRepresentationOfDoOnIoReport = consultationdocumentOnRepresentationOfDoOnIoReport;
    }

    public String getConcurranceOrdnoOnRepresentationOfDoOnIoReport() {
        return concurranceOrdnoOnRepresentationOfDoOnIoReport;
    }

    public void setConcurranceOrdnoOnRepresentationOfDoOnIoReport(String concurranceOrdnoOnRepresentationOfDoOnIoReport) {
        this.concurranceOrdnoOnRepresentationOfDoOnIoReport = concurranceOrdnoOnRepresentationOfDoOnIoReport;
    }

    public String getConcurranceOrddateOnRepresentationOfDoOnIoReport() {
        return concurranceOrddateOnRepresentationOfDoOnIoReport;
    }

    public void setConcurranceOrddateOnRepresentationOfDoOnIoReport(String concurranceOrddateOnRepresentationOfDoOnIoReport) {
        this.concurranceOrddateOnRepresentationOfDoOnIoReport = concurranceOrddateOnRepresentationOfDoOnIoReport;
    }

    public String getConcurranceOriginalfilenameOnRepresentationOfDoOnIoReport() {
        return concurranceOriginalfilenameOnRepresentationOfDoOnIoReport;
    }

    public void setConcurranceOriginalfilenameOnRepresentationOfDoOnIoReport(String concurranceOriginalfilenameOnRepresentationOfDoOnIoReport) {
        this.concurranceOriginalfilenameOnRepresentationOfDoOnIoReport = concurranceOriginalfilenameOnRepresentationOfDoOnIoReport;
    }

    public String getConcurrancediskfilenameOnRepresentationOfDoOnIoReport() {
        return concurrancediskfilenameOnRepresentationOfDoOnIoReport;
    }

    public void setConcurrancediskfilenameOnRepresentationOfDoOnIoReport(String concurrancediskfilenameOnRepresentationOfDoOnIoReport) {
        this.concurrancediskfilenameOnRepresentationOfDoOnIoReport = concurrancediskfilenameOnRepresentationOfDoOnIoReport;
    }

    public String getConcurrancefiletypeOnRepresentationOfDoOnIoReport() {
        return concurrancefiletypeOnRepresentationOfDoOnIoReport;
    }

    public void setConcurrancefiletypeOnRepresentationOfDoOnIoReport(String concurrancefiletypeOnRepresentationOfDoOnIoReport) {
        this.concurrancefiletypeOnRepresentationOfDoOnIoReport = concurrancefiletypeOnRepresentationOfDoOnIoReport;
    }

    public String getConcurrancefilepathOnRepresentationOfDoOnIoReport() {
        return concurrancefilepathOnRepresentationOfDoOnIoReport;
    }

    public void setConcurrancefilepathOnRepresentationOfDoOnIoReport(String concurrancefilepathOnRepresentationOfDoOnIoReport) {
        this.concurrancefilepathOnRepresentationOfDoOnIoReport = concurrancefilepathOnRepresentationOfDoOnIoReport;
    }

    public MultipartFile getConcurrancedocumentOnRepresentationOfDoOnIoReport() {
        return concurrancedocumentOnRepresentationOfDoOnIoReport;
    }

    public void setConcurrancedocumentOnRepresentationOfDoOnIoReport(MultipartFile concurrancedocumentOnRepresentationOfDoOnIoReport) {
        this.concurrancedocumentOnRepresentationOfDoOnIoReport = concurrancedocumentOnRepresentationOfDoOnIoReport;
    }

    public String getFinalOrdnoOnRepresentationOfDoOnIoReport() {
        return finalOrdnoOnRepresentationOfDoOnIoReport;
    }

    public void setFinalOrdnoOnRepresentationOfDoOnIoReport(String finalOrdnoOnRepresentationOfDoOnIoReport) {
        this.finalOrdnoOnRepresentationOfDoOnIoReport = finalOrdnoOnRepresentationOfDoOnIoReport;
    }

    public String getFinalOrddateOnRepresentationOfDoOnIoReport() {
        return finalOrddateOnRepresentationOfDoOnIoReport;
    }

    public void setFinalOrddateOnRepresentationOfDoOnIoReport(String finalOrddateOnRepresentationOfDoOnIoReport) {
        this.finalOrddateOnRepresentationOfDoOnIoReport = finalOrddateOnRepresentationOfDoOnIoReport;
    }

    public String getFinalOriginalfilenameOnRepresentationOfDoOnIoReport() {
        return finalOriginalfilenameOnRepresentationOfDoOnIoReport;
    }

    public void setFinalOriginalfilenameOnRepresentationOfDoOnIoReport(String finalOriginalfilenameOnRepresentationOfDoOnIoReport) {
        this.finalOriginalfilenameOnRepresentationOfDoOnIoReport = finalOriginalfilenameOnRepresentationOfDoOnIoReport;
    }

    public String getFinaldiskfilenameOnRepresentationOfDoOnIoReport() {
        return finaldiskfilenameOnRepresentationOfDoOnIoReport;
    }

    public void setFinaldiskfilenameOnRepresentationOfDoOnIoReport(String finaldiskfilenameOnRepresentationOfDoOnIoReport) {
        this.finaldiskfilenameOnRepresentationOfDoOnIoReport = finaldiskfilenameOnRepresentationOfDoOnIoReport;
    }

    public String getFinalfiletypeOnRepresentationOfDoOnIoReport() {
        return finalfiletypeOnRepresentationOfDoOnIoReport;
    }

    public void setFinalfiletypeOnRepresentationOfDoOnIoReport(String finalfiletypeOnRepresentationOfDoOnIoReport) {
        this.finalfiletypeOnRepresentationOfDoOnIoReport = finalfiletypeOnRepresentationOfDoOnIoReport;
    }

    public String getFinalfilepathOnRepresentationOfDoOnIoReport() {
        return finalfilepathOnRepresentationOfDoOnIoReport;
    }

    public void setFinalfilepathOnRepresentationOfDoOnIoReport(String finalfilepathOnRepresentationOfDoOnIoReport) {
        this.finalfilepathOnRepresentationOfDoOnIoReport = finalfilepathOnRepresentationOfDoOnIoReport;
    }

    public MultipartFile getFinaldocumentOnRepresentationOfDoOnIoReport() {
        return finaldocumentOnRepresentationOfDoOnIoReport;
    }

    public void setFinaldocumentOnRepresentationOfDoOnIoReport(MultipartFile finaldocumentOnRepresentationOfDoOnIoReport) {
        this.finaldocumentOnRepresentationOfDoOnIoReport = finaldocumentOnRepresentationOfDoOnIoReport;
    }

    public String getExanaurationfinalOrdnoOnRepresentationOfDoOnIoReport() {
        return exanaurationfinalOrdnoOnRepresentationOfDoOnIoReport;
    }

    public void setExanaurationfinalOrdnoOnRepresentationOfDoOnIoReport(String exanaurationfinalOrdnoOnRepresentationOfDoOnIoReport) {
        this.exanaurationfinalOrdnoOnRepresentationOfDoOnIoReport = exanaurationfinalOrdnoOnRepresentationOfDoOnIoReport;
    }

    public String getExanaurationfinalOrddateOnRepresentationOfDoOnIoReport() {
        return exanaurationfinalOrddateOnRepresentationOfDoOnIoReport;
    }

    public void setExanaurationfinalOrddateOnRepresentationOfDoOnIoReport(String exanaurationfinalOrddateOnRepresentationOfDoOnIoReport) {
        this.exanaurationfinalOrddateOnRepresentationOfDoOnIoReport = exanaurationfinalOrddateOnRepresentationOfDoOnIoReport;
    }

    public String getExanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport() {
        return exanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport;
    }

    public void setExanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport(String exanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport) {
        this.exanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport = exanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport;
    }

    public String getExanaurationfinaldiskfilenameOnRepresentationOfDoOnIoReport() {
        return exanaurationfinaldiskfilenameOnRepresentationOfDoOnIoReport;
    }

    public void setExanaurationfinaldiskfilenameOnRepresentationOfDoOnIoReport(String exanaurationfinaldiskfilenameOnRepresentationOfDoOnIoReport) {
        this.exanaurationfinaldiskfilenameOnRepresentationOfDoOnIoReport = exanaurationfinaldiskfilenameOnRepresentationOfDoOnIoReport;
    }

    public String getExanaurationfinalfiletypeOnRepresentationOfDoOnIoReport() {
        return exanaurationfinalfiletypeOnRepresentationOfDoOnIoReport;
    }

    public void setExanaurationfinalfiletypeOnRepresentationOfDoOnIoReport(String exanaurationfinalfiletypeOnRepresentationOfDoOnIoReport) {
        this.exanaurationfinalfiletypeOnRepresentationOfDoOnIoReport = exanaurationfinalfiletypeOnRepresentationOfDoOnIoReport;
    }

    public String getExanaurationfinalfilepathOnRepresentationOfDoOnIoReport() {
        return exanaurationfinalfilepathOnRepresentationOfDoOnIoReport;
    }

    public void setExanaurationfinalfilepathOnRepresentationOfDoOnIoReport(String exanaurationfinalfilepathOnRepresentationOfDoOnIoReport) {
        this.exanaurationfinalfilepathOnRepresentationOfDoOnIoReport = exanaurationfinalfilepathOnRepresentationOfDoOnIoReport;
    }

    public MultipartFile getExanaurationfinaldocumentOnRepresentationOfDoOnIoReport() {
        return exanaurationfinaldocumentOnRepresentationOfDoOnIoReport;
    }

    public void setExanaurationfinaldocumentOnRepresentationOfDoOnIoReport(MultipartFile exanaurationfinaldocumentOnRepresentationOfDoOnIoReport) {
        this.exanaurationfinaldocumentOnRepresentationOfDoOnIoReport = exanaurationfinaldocumentOnRepresentationOfDoOnIoReport;
    }

    public String getDisciplinaryauthority() {
        return disciplinaryauthority;
    }

    public void setDisciplinaryauthority(String disciplinaryauthority) {
        this.disciplinaryauthority = disciplinaryauthority;
    }

    public String getInquiryauthority() {
        return inquiryauthority;
    }

    public void setInquiryauthority(String inquiryauthority) {
        this.inquiryauthority = inquiryauthority;
    }

    public String getPresentingauthority() {
        return presentingauthority;
    }

    public void setPresentingauthority(String presentingauthority) {
        this.presentingauthority = presentingauthority;
    }

    public String getAdditionalpresentingauthority() {
        return additionalpresentingauthority;
    }

    public void setAdditionalpresentingauthority(String additionalpresentingauthority) {
        this.additionalpresentingauthority = additionalpresentingauthority;
    }

    public String getThirdshowCauseOrdDt() {
        return thirdshowCauseOrdDt;
    }

    public void setThirdshowCauseOrdDt(String thirdshowCauseOrdDt) {
        this.thirdshowCauseOrdDt = thirdshowCauseOrdDt;
    }

    public String getHasSendthirdshowCause() {
        return hasSendthirdshowCause;
    }

    public void setHasSendthirdshowCause(String hasSendthirdshowCause) {
        this.hasSendthirdshowCause = hasSendthirdshowCause;
    }

    public String getThirdNoticeToDelinquentPunishmentProposed() {
        return thirdNoticeToDelinquentPunishmentProposed;
    }

    public void setThirdNoticeToDelinquentPunishmentProposed(String thirdNoticeToDelinquentPunishmentProposed) {
        this.thirdNoticeToDelinquentPunishmentProposed = thirdNoticeToDelinquentPunishmentProposed;
    }

    public String getOrdNoForthirdNoticetoDOOnForPunishment() {
        return OrdNoForthirdNoticetoDOOnForPunishment;
    }

    public void setOrdNoForthirdNoticetoDOOnForPunishment(String OrdNoForthirdNoticetoDOOnForPunishment) {
        this.OrdNoForthirdNoticetoDOOnForPunishment = OrdNoForthirdNoticetoDOOnForPunishment;
    }

    public String getOrdDtForthirdNoticetoDOOnForPunishment() {
        return OrdDtForthirdNoticetoDOOnForPunishment;
    }

    public void setOrdDtForthirdNoticetoDOOnForPunishment(String OrdDtForthirdNoticetoDOOnForPunishment) {
        this.OrdDtForthirdNoticetoDOOnForPunishment = OrdDtForthirdNoticetoDOOnForPunishment;
    }

    public MultipartFile getThirdNoticetoDOOnForPunishmentdocument() {
        return thirdNoticetoDOOnForPunishmentdocument;
    }

    public void setThirdNoticetoDOOnForPunishmentdocument(MultipartFile thirdNoticetoDOOnForPunishmentdocument) {
        this.thirdNoticetoDOOnForPunishmentdocument = thirdNoticetoDOOnForPunishmentdocument;
    }

    public String getThirdNoticetoDOOnForPunishmentorgFileName() {
        return thirdNoticetoDOOnForPunishmentorgFileName;
    }

    public void setThirdNoticetoDOOnForPunishmentorgFileName(String thirdNoticetoDOOnForPunishmentorgFileName) {
        this.thirdNoticetoDOOnForPunishmentorgFileName = thirdNoticetoDOOnForPunishmentorgFileName;
    }

    public String getThirdNoticetoDOOnForPunishmentdiskFileName() {
        return thirdNoticetoDOOnForPunishmentdiskFileName;
    }

    public void setThirdNoticetoDOOnForPunishmentdiskFileName(String thirdNoticetoDOOnForPunishmentdiskFileName) {
        this.thirdNoticetoDOOnForPunishmentdiskFileName = thirdNoticetoDOOnForPunishmentdiskFileName;
    }

    public String getThirdNoticetoDOOnForPunishmentFileType() {
        return thirdNoticetoDOOnForPunishmentFileType;
    }

    public void setThirdNoticetoDOOnForPunishmentFileType(String thirdNoticetoDOOnForPunishmentFileType) {
        this.thirdNoticetoDOOnForPunishmentFileType = thirdNoticetoDOOnForPunishmentFileType;
    }

    public String getDoRepresentationOnThirdshowCauseOrdNo() {
        return doRepresentationOnThirdshowCauseOrdNo;
    }

    public void setDoRepresentationOnThirdshowCauseOrdNo(String doRepresentationOnThirdshowCauseOrdNo) {
        this.doRepresentationOnThirdshowCauseOrdNo = doRepresentationOnThirdshowCauseOrdNo;
    }

    public String getDoRepresentationOnThirdshowCauseOrdDt() {
        return doRepresentationOnThirdshowCauseOrdDt;
    }

    public void setDoRepresentationOnThirdshowCauseOrdDt(String doRepresentationOnThirdshowCauseOrdDt) {
        this.doRepresentationOnThirdshowCauseOrdDt = doRepresentationOnThirdshowCauseOrdDt;
    }

    public MultipartFile getThirdshowcausedocumentondoRepresentation() {
        return thirdshowcausedocumentondoRepresentation;
    }

    public void setThirdshowcausedocumentondoRepresentation(MultipartFile thirdshowcausedocumentondoRepresentation) {
        this.thirdshowcausedocumentondoRepresentation = thirdshowcausedocumentondoRepresentation;
    }

    public String getHasDoRepresentOnthirdshowCause() {
        return hasDoRepresentOnthirdshowCause;
    }

    public void setHasDoRepresentOnthirdshowCause(String hasDoRepresentOnthirdshowCause) {
        this.hasDoRepresentOnthirdshowCause = hasDoRepresentOnthirdshowCause;
    }

    public String getThirdshowcauseOriginalfilenameOnRepresentationOfDoOnIoReport() {
        return thirdshowcauseOriginalfilenameOnRepresentationOfDoOnIoReport;
    }

    public void setThirdshowcauseOriginalfilenameOnRepresentationOfDoOnIoReport(String thirdshowcauseOriginalfilenameOnRepresentationOfDoOnIoReport) {
        this.thirdshowcauseOriginalfilenameOnRepresentationOfDoOnIoReport = thirdshowcauseOriginalfilenameOnRepresentationOfDoOnIoReport;
    }

    public String getThirdshowcausediskfilenameOnRepresentationOfDoOnIoReport() {
        return thirdshowcausediskfilenameOnRepresentationOfDoOnIoReport;
    }

    public void setThirdshowcausediskfilenameOnRepresentationOfDoOnIoReport(String thirdshowcausediskfilenameOnRepresentationOfDoOnIoReport) {
        this.thirdshowcausediskfilenameOnRepresentationOfDoOnIoReport = thirdshowcausediskfilenameOnRepresentationOfDoOnIoReport;
    }

    public String getThirdshowcausefiletypeOnRepresentationOfDoOnIoReport() {
        return thirdshowcausefiletypeOnRepresentationOfDoOnIoReport;
    }

    public void setThirdshowcausefiletypeOnRepresentationOfDoOnIoReport(String thirdshowcausefiletypeOnRepresentationOfDoOnIoReport) {
        this.thirdshowcausefiletypeOnRepresentationOfDoOnIoReport = thirdshowcausefiletypeOnRepresentationOfDoOnIoReport;
    }

    public String getThirdshowcausefilepathOnRepresentationOfDoOnIoReport() {
        return thirdshowcausefilepathOnRepresentationOfDoOnIoReport;
    }

    public void setThirdshowcausefilepathOnRepresentationOfDoOnIoReport(String thirdshowcausefilepathOnRepresentationOfDoOnIoReport) {
        this.thirdshowcausefilepathOnRepresentationOfDoOnIoReport = thirdshowcausefilepathOnRepresentationOfDoOnIoReport;
    }

    public String getThirdshowCauseReplyByDAOrdDt() {
        return thirdshowCauseReplyByDAOrdDt;
    }

    public void setThirdshowCauseReplyByDAOrdDt(String thirdshowCauseReplyByDAOrdDt) {
        this.thirdshowCauseReplyByDAOrdDt = thirdshowCauseReplyByDAOrdDt;
    }

    public String getThirdshowCauseReplyByDAordNo() {
        return thirdshowCauseReplyByDAordNo;
    }

    public void setThirdshowCauseReplyByDAordNo(String thirdshowCauseReplyByDAordNo) {
        this.thirdshowCauseReplyByDAordNo = thirdshowCauseReplyByDAordNo;
    }

    public String getHasReplyByDothirdshowCause() {
        return hasReplyByDothirdshowCause;
    }

    public void setHasReplyByDothirdshowCause(String hasReplyByDothirdshowCause) {
        this.hasReplyByDothirdshowCause = hasReplyByDothirdshowCause;
    }

    public String getProposedpunishmentOnRepresentationOfDOOnpunishment() {
        return ProposedpunishmentOnRepresentationOfDOOnpunishment;
    }

    public void setProposedpunishmentOnRepresentationOfDOOnpunishment(String ProposedpunishmentOnRepresentationOfDOOnpunishment) {
        this.ProposedpunishmentOnRepresentationOfDOOnpunishment = ProposedpunishmentOnRepresentationOfDOOnpunishment;
    }

    public String getExanaurationfinalOrdnumberOnopscConcurrance() {
        return exanaurationfinalOrdnumberOnopscConcurrance;
    }

    public void setExanaurationfinalOrdnumberOnopscConcurrance(String exanaurationfinalOrdnumberOnopscConcurrance) {
        this.exanaurationfinalOrdnumberOnopscConcurrance = exanaurationfinalOrdnumberOnopscConcurrance;
    }

    public String getExanaurationfinalOrddateOnopscConcurrance() {
        return exanaurationfinalOrddateOnopscConcurrance;
    }

    public void setExanaurationfinalOrddateOnopscConcurrance(String exanaurationfinalOrddateOnopscConcurrance) {
        this.exanaurationfinalOrddateOnopscConcurrance = exanaurationfinalOrddateOnopscConcurrance;
    }

    public String getExanaurationfinalOrddateafterPunishment() {
        return exanaurationfinalOrddateafterPunishment;
    }

    public void setExanaurationfinalOrddateafterPunishment(String exanaurationfinalOrddateafterPunishment) {
        this.exanaurationfinalOrddateafterPunishment = exanaurationfinalOrddateafterPunishment;
    }

    public String getExanaurationfinalOrdNumberafterPunishment() {
        return exanaurationfinalOrdNumberafterPunishment;
    }

    public void setExanaurationfinalOrdNumberafterPunishment(String exanaurationfinalOrdNumberafterPunishment) {
        this.exanaurationfinalOrdNumberafterPunishment = exanaurationfinalOrdNumberafterPunishment;
    }

    public MultipartFile getExanaurationfinaldocumentOnopscConcurrance() {
        return exanaurationfinaldocumentOnopscConcurrance;
    }

    public void setExanaurationfinaldocumentOnopscConcurrance(MultipartFile exanaurationfinaldocumentOnopscConcurrance) {
        this.exanaurationfinaldocumentOnopscConcurrance = exanaurationfinaldocumentOnopscConcurrance;
    }

    public String getExanaurationfinalOriginalfilenameOnopscConcurrance() {
        return exanaurationfinalOriginalfilenameOnopscConcurrance;
    }

    public void setExanaurationfinalOriginalfilenameOnopscConcurrance(String exanaurationfinalOriginalfilenameOnopscConcurrance) {
        this.exanaurationfinalOriginalfilenameOnopscConcurrance = exanaurationfinalOriginalfilenameOnopscConcurrance;
    }

    public String getExanaurationfinaldiskfilenameOnopscConcurrance() {
        return exanaurationfinaldiskfilenameOnopscConcurrance;
    }

    public void setExanaurationfinaldiskfilenameOnopscConcurrance(String exanaurationfinaldiskfilenameOnopscConcurrance) {
        this.exanaurationfinaldiskfilenameOnopscConcurrance = exanaurationfinaldiskfilenameOnopscConcurrance;
    }

    public String getExanaurationfinalOriginalfiletypeOnopscConcurrance() {
        return exanaurationfinalOriginalfiletypeOnopscConcurrance;
    }

    public void setExanaurationfinalOriginalfiletypeOnopscConcurrance(String exanaurationfinalOriginalfiletypeOnopscConcurrance) {
        this.exanaurationfinalOriginalfiletypeOnopscConcurrance = exanaurationfinalOriginalfiletypeOnopscConcurrance;
    }

}
