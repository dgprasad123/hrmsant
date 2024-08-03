/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.parmast;

import java.util.ArrayList;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author manisha
 */
public class PerformanceAppraisalForm {
      private String txtFromDate = null;
    private String txtToDate = null;
    private String txtEmpId = null;
    private String curspc = null;
    private String spcinperiod = null;
    private String txtEmpName = null;
    private String txtDOB = null;
    private String txtServiceDetails = null;
    private String sltGroupOfPost = null;
    private String sltDesignation = null;
    private String sltPostedOffice = null;
    private String sltHeadQuarter = null;
    private String sltYear = null;
    private ArrayList SltFYear = new ArrayList();
    private ArrayList yearList = new ArrayList();
    private String pageno = null;
    private String txtAppraisalSummary = null;
    private String hidFromDate = null;
    private String hidToDate = null;
    private ArrayList parList = null;
    private String hidSpcCode = null;
    private String txtSpecialContribution = null;
    private String txtFactors = null;
    private String txtPlace = null;
    private String txtSubmissionDate = null;
    private String fiscalYear = null;
    private String hidOffCode = null;
    private String txtPFromDate = null;
    private String txtPToDate = null;
    private String sltLeaveOrTraining = null;
    private String sltLeaveType = null;
    private ArrayList parAbsentteArr = null;
    private ArrayList parAchievementArr = null;
    private String hidParId = null;

    private String hidabsId = null;
    private String hidachId = null;
    private String task = null;
    private String target = null;
    private String achievement = null;
    private String percentageofAchievement = null;
    private String nrcreason = null;
    private MultipartFile file;
    
    private String hidpaptid = null;
    
    private String nrcAttachmentname = null;
    private String txtNrcRemarks = null;
    private String submitedonNRC;
    
    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getNrcreason() {
        return nrcreason;
    }

    public void setNrcreason(String nrcreason) {
        this.nrcreason = nrcreason;
    }

    public void setTxtFromDate(String txtFromDate) {
        this.txtFromDate = txtFromDate;
    }

    public String getTxtFromDate() {
        return txtFromDate;
    }

    public void setTxtToDate(String txtToDate) {
        this.txtToDate = txtToDate;
    }

    public String getTxtToDate() {
        return txtToDate;
    }

    public void setTxtEmpId(String txtEmpId) {
        this.txtEmpId = txtEmpId;
    }

    public String getTxtEmpId() {
        return txtEmpId;
    }

    public void setCurspc(String curspc) {
        this.curspc = curspc;
    }

    public String getCurspc() {
        return curspc;
    }

    public void setSpcinperiod(String spcinperiod) {
        this.spcinperiod = spcinperiod;
    }

    public String getSpcinperiod() {
        return spcinperiod;
    }

    public void setTxtEmpName(String txtEmpName) {
        this.txtEmpName = txtEmpName;
    }

    public String getTxtEmpName() {
        return txtEmpName;
    }

    public void setTxtDOB(String txtDOB) {
        this.txtDOB = txtDOB;
    }

    public String getTxtDOB() {
        return txtDOB;
    }

    public void setTxtServiceDetails(String txtServiceDetails) {
        this.txtServiceDetails = txtServiceDetails;
    }

    public String getTxtServiceDetails() {
        return txtServiceDetails;
    }

    public void setSltGroupOfPost(String sltGroupOfPost) {
        this.sltGroupOfPost = sltGroupOfPost;
    }

    public String getSltGroupOfPost() {
        return sltGroupOfPost;
    }

    public void setSltDesignation(String sltDesignation) {
        this.sltDesignation = sltDesignation;
    }

    public String getSltDesignation() {
        return sltDesignation;
    }

    public void setSltPostedOffice(String sltPostedOffice) {
        this.sltPostedOffice = sltPostedOffice;
    }

    public String getSltPostedOffice() {
        return sltPostedOffice;
    }

    public void setSltHeadQuarter(String sltHeadQuarter) {
        this.sltHeadQuarter = sltHeadQuarter;
    }

    public String getSltHeadQuarter() {
        return sltHeadQuarter;
    }

    public void setSltYear(String sltYear) {
        this.sltYear = sltYear;
    }

    public String getSltYear() {
        return sltYear;
    }

    public void setSltFYear(ArrayList sltFYear) {
        this.SltFYear = sltFYear;
    }

    public ArrayList getSltFYear() {
        return SltFYear;
    }

    public void setYearList(ArrayList yearList) {
        this.yearList = yearList;
    }

    public ArrayList getYearList() {
        return yearList;
    }

    public void setPageno(String pageno) {
        this.pageno = pageno;
    }

    public String getPageno() {
        return pageno;
    }

    public void setTxtAppraisalSummary(String txtAppraisalSummary) {
        this.txtAppraisalSummary = txtAppraisalSummary;
    }

    public String getTxtAppraisalSummary() {
        return txtAppraisalSummary;
    }

    public void setHidFromDate(String hidFromDate) {
        this.hidFromDate = hidFromDate;
    }

    public String getHidFromDate() {
        return hidFromDate;
    }

    public void setTxtSpecialContribution(String txtSpecialContribution) {
        this.txtSpecialContribution = txtSpecialContribution;
    }

    public String getTxtSpecialContribution() {
        return txtSpecialContribution;
    }

    public void setTxtFactors(String txtFactors) {
        this.txtFactors = txtFactors;
    }

    public String getTxtFactors() {
        return txtFactors;
    }

    public void setTxtPlace(String txtPlace) {
        this.txtPlace = txtPlace;
    }

    public String getTxtPlace() {
        return txtPlace;
    }

    public void setTxtSubmissionDate(String txtSubmissionDate) {
        this.txtSubmissionDate = txtSubmissionDate;
    }

    public String getTxtSubmissionDate() {
        return txtSubmissionDate;
    }

    public void setTxtPFromDate(String txtPFromDate) {
        this.txtPFromDate = txtPFromDate;
    }

    public String getTxtPFromDate() {
        return txtPFromDate;
    }

    public void setTxtPToDate(String txtPToDate) {
        this.txtPToDate = txtPToDate;
    }

    public String getTxtPToDate() {
        return txtPToDate;
    }

    public void setSltLeaveOrTraining(String sltLeaveOrTraining) {
        this.sltLeaveOrTraining = sltLeaveOrTraining;
    }

    public String getSltLeaveOrTraining() {
        return sltLeaveOrTraining;
    }

    public void setSltLeaveType(String sltLeaveType) {
        this.sltLeaveType = sltLeaveType;
    }

    public String getSltLeaveType() {
        return sltLeaveType;
    }

    public void setParAbsentteArr(ArrayList parAbsentteArr) {
        this.parAbsentteArr = parAbsentteArr;
    }

    public ArrayList getParAbsentteArr() {
        return parAbsentteArr;
    }

    public void setHidParId(String hidParId) {
        this.hidParId = hidParId;
    }

    public String getHidParId() {
        return hidParId;
    }

    public void setParList(ArrayList parList) {
        this.parList = parList;
    }

    public ArrayList getParList() {
        return parList;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTask() {
        return task;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setPercentageofAchievement(String percentageofAchievement) {
        this.percentageofAchievement = percentageofAchievement;
    }

    public String getPercentageofAchievement() {
        return percentageofAchievement;
    }

    public void setParAchievementArr(ArrayList parAchievementArr) {
        this.parAchievementArr = parAchievementArr;
    }

    public ArrayList getParAchievementArr() {
        return parAchievementArr;
    }

    public void setHidToDate(String hidToDate) {
        this.hidToDate = hidToDate;
    }

    public String getHidToDate() {
        return hidToDate;
    }

    public void setHidabsId(String hidabsId) {
        this.hidabsId = hidabsId;
    }

    public String getHidabsId() {
        return hidabsId;
    }

    public void setHidachId(String hidachId) {
        this.hidachId = hidachId;
    }

    public String getHidachId() {
        return hidachId;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getFiscalYear() {
        return fiscalYear;
    }

    public void setHidSpcCode(String hidSpcCode) {
        this.hidSpcCode = hidSpcCode;
    }

    public String getHidSpcCode() {
        return hidSpcCode;
    }

    public void setHidOffCode(String hidOffCode) {
        this.hidOffCode = hidOffCode;
    }

    public String getHidOffCode() {
        return hidOffCode;
    }

    public String getHidpaptid() {
        return hidpaptid;
    }

    public void setHidpaptid(String hidpaptid) {
        this.hidpaptid = hidpaptid;
    }

    public String getNrcAttachmentname() {
        return nrcAttachmentname;
    }

    public void setNrcAttachmentname(String nrcAttachmentname) {
        this.nrcAttachmentname = nrcAttachmentname;
    }

    public String getTxtNrcRemarks() {
        return txtNrcRemarks;
    }

    public void setTxtNrcRemarks(String txtNrcRemarks) {
        this.txtNrcRemarks = txtNrcRemarks;
    }

    public String getSubmitedonNRC() {
        return submitedonNRC;
    }

    public void setSubmitedonNRC(String submitedonNRC) {
        this.submitedonNRC = submitedonNRC;
    }
    
    
}


