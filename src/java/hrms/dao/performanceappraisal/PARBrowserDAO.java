package hrms.dao.performanceappraisal;

import com.itextpdf.text.Document;
import hrms.SelectOption;
import hrms.model.common.FileAttribute;
import hrms.model.parmast.AcceptingHelperBean;
import hrms.model.parmast.InitiateOtherPARForm;
import hrms.model.parmast.PARTemplateBean;
import hrms.model.parmast.ParAbsenteeBean;
import hrms.model.parmast.ParAchievement;
import hrms.model.parmast.ParDetail;
import hrms.model.parmast.ParMaster;
import hrms.model.parmast.ParOtherDetails;
import hrms.model.parmast.ParSubmitForm;
import hrms.model.parmast.ReportingHelperBean;
import hrms.model.parmast.ReviewingHelperBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface PARBrowserDAO {

    public List getPARList(String fiscalyear, String empid);

    public List getPARListForSI(String fiscalyear, String empid);

    public List getPARList(String empid);

    public int savePAR(int pageno, ParMaster parmaster, ParOtherDetails parOtherDetails, String filepath);

    public List getAbsenteeList(String empid, int parid);

    public void saveAbsentee(ParAbsenteeBean parabsentee);

    public List getAchievementList(String empid, int parid);

    public void saveAchievement(String empid, ParAchievement parachievement, String filepath);

    public ParMaster getAppraiseInfo(String empid, int parid);

    public ParOtherDetails getOtherDetails(String empid, int parid);

    public List getAbsenceCauseList();

    public List getLeaveTypeList();

    public List getTrainingypeList();

    public String sendPar(ParSubmitForm pf) throws Exception;

    public int getmaxhierachy(int parid, int parstatus);

    public ParAbsenteeBean getAbsenteeInfo(String empid, int pabid);

    public ParAchievement getAchievementInfo(String empid, int pacid);

    public void deleteAbsentee(String empid, int pabid);

    public void deleteAchievement(String empid, int pacid, String fiscalyear, String filepath);

    public int deleteAchievementAttachment(String empid, int pacid, int attid, String fiscalyear, String filepath);

    public boolean isDuplicatePARPeriod(String empid, String parfrmdt, String partodt, String fiscalyear, String parid);
    
    public boolean isDuplicatePARPeriodForInitiateOtherPAR(String empid, String parfrmdt, String partodt, String fiscalyear, String parId);

    public ParDetail getPARDetails(String empid, int parid, int taskid, String authType);

    public int getTaskid(String empid, int parid);
    
    public int getPrptidReporting(String reportingempid, int parid);
    
    public int getPrvtidReviewing(String reviewingempid, int parid);
    
    public int getPactidAccepting(String acceptingempid, int parid);

    //public Font getDesired_PDF_Font(int fontsize,boolean isBold,boolean isUnderline) throws Exception;
    public List getPARGradeList();

    public void saveAndForwardPAR(ParDetail pdetail, String empid, String forwardbtn, String gradingPath);

    public void viewPDFfunc(Document document, ParDetail paf, String empid, String filepath) throws Exception;

    public int getParid(String empid, int taskid);

    public int saveAcceptingAuthRemarksFromCombo(String empid, int parid, int taskid, String remarks);

    public List getNRCReasonList();
    
    public List getNRCReasonListForMoreThan4Month();

    public String getNRCAttachedFileName(String empid, int parid);

    public ParDetail getNRCDetails(String empid, int parid);

    public void viewNRCPDFfunc(Document document, ParDetail paf, String empid);

    public FileAttribute downloadachievementattachment(String filepath, int attid, String fiscalyear) throws Exception;

    public FileAttribute downloadNRCAttachment(int parid, String fiscalyear, String filepath) throws Exception;

    public String revertPAR(String loginempid, ParDetail parDetail);

    public ParSubmitForm getAuthorityInfo(int parid);

    public String[] getRevertReason(int parid, int taskid);

    public String isFiscalYearClosed(String fyear);

    public String isFiscalYearClosed4Si(String fyear);

    public String isAuthRemarksClosed(String fyear);

    public void deleteNRC(int parid, String empId);

    public int deletePAR(String parid, String empid);

    public int deletePARForReportingRevert(String parid, String empid);

    public String isAchievementDataPresent(int parid);

    public String isOtherDetailsPresent(int parid);

    public boolean isPARReverted(int parid, String empid);

    public List getPARReport(String fiscalyear);

    public boolean isAuthorizedtoDownloadPAR(String loggedinEmpid, int parid);

    public ArrayList getEmployeeListforInitiateOtherPAR(String offcode, String fiscalYear);

    /* Written Previously For update with same parid that Appraisee Created but not submitted
     public int saveInitiateOtherPARRemarks(String loginid, String loginspc,InitiateOtherPARForm initiateOtherPARForm); 
     */
    public int saveInitiateOtherPARRemarks(String loginid, String loginspc, InitiateOtherPARForm initiateOtherPARForm);

    public String submitInitiateOtherPar(String loginid, String loginspc, InitiateOtherPARForm initiateOtherPARForm) throws Exception;

    public String getEmployeeName(String empid);

    public String isInitiateOtherDuplicatePAR(InitiateOtherPARForm initiateOtherPARForm);

    public SelectOption getPartialPARPeriod(String empid, String fiscalyear);

    public void downloadPARDetailPDF(Document document, String empid, ParDetail paf, String filePath);

    public boolean isPAREditableByAppraisee(int parid);
    
    public boolean isSubmitByAppraiseeBeforeDate(int parid);

    public List getPARTemplateList(String templateHeading, String hrmsId);

    public void saveCustomForAuthNote(PARTemplateBean parTemplateBean);

    public HashMap getOtherDetailsOfAllFinancialYear(String empid, String fieldName);

    public ArrayList getAchievementDetailsOfAllFinancialYear(String fiscalyear, String empid);

    public void savePreviousYearAchievement(int pacid, int hidparid);

    public void savePreviousYearAchievementList(int prevparid, int parid);

    public HashMap getPreviousPARDetail(String empid);

    public void savePreviousyrOtherDetails(int prevparid, int parid);

    public ArrayList getloginWiseSavedTemplateList(String templateHeading, String empid);

    public void deleteloginWiseSavedTemplateList(int templateId);

    public ParSubmitForm getForceforwardLogDetails();
    
    public void generateQRCodeForPreviousYearPAR(int parId, String qrCodePathPAR);
    
   // public FileAttribute DownloadGradingAttchmentReporting(int parid, String fiscalyear,int prptid, String gradingPath) throws Exception;
    
    public ReportingHelperBean DownloadGradingAttchmentReporting(int parId,int pactid, String gradingPath);
    
    public ReviewingHelperBean DownloadGradingAttchmentReviewing(int parId,int prvtid, String gradingPath);
    
    public AcceptingHelperBean DownloadGradingAttchmentAccepting(int parId, int pactid, String gradingPath); 
    
    //public ReportingHelperBean getDocumentForReporting(String gradingfilePath, int parId, int prptid);
    
    public void deleteGradingAttachmentReporting(ReportingHelperBean reportingHelperBean);
    
    public void deleteGradingAttachmentReviewing(ReviewingHelperBean reviewingHelperBean);
    
    public void deleteGradingAttachmentAccepting(AcceptingHelperBean acceptingHelperBean);
    
   public boolean isCheckParPeriod(String parfrmdt, String partodt, String fiscalyear);
   
   public boolean isParCreatedDateClosed(String empId, String fiscalyear);

}
