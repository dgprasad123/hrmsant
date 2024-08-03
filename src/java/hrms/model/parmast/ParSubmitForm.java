/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.parmast;

import java.io.Serializable;
import java.util.ArrayList;

public class ParSubmitForm implements Serializable {
    
    private String[] hidReportingEmpId = null;
    private String[] hidReviewingEmpId = null;
    private String[] hidAcceptingEmpId = null;
    
    private String[] hidReportingSpcCode = null;
    private String[] hidReviewingpcCode = null;
    private String[] hidAcceptingSpcCode = null;
    
    private String[] txtReportingAuthFrmDt = null;
    private String[] txtReportingAuthToDt = null;
    
    private String[] txtReviewingAuthFrmDt = null;
    private String[] txtReviewingAuthToDt = null;
    
    private String[] txtAcceptingAuthFrmDt = null;
    private String[] txtAcceptingAuthToDt = null;
    
    private int hidparid = 0;
    private int hidparstatus = 0;
    private int hidtaskid = 0;
    private String hidfiscalyear = null;
    private int maxhierarchyno = 0;
    private String hidparfrmdt = null;
    private String hidpartodt = null;
    private String hidspc = null;
    private String dateOfForceForwardFromReportingToReviewing;
    private String fiscalYear;
    
    private ArrayList reportList = null;
    private ArrayList reviewList = null;
    private ArrayList acceptList = null;

    public String getDateOfForceForwardFromReportingToReviewing() {
        return dateOfForceForwardFromReportingToReviewing;
    }

    public void setDateOfForceForwardFromReportingToReviewing(String dateOfForceForwardFromReportingToReviewing) {
        this.dateOfForceForwardFromReportingToReviewing = dateOfForceForwardFromReportingToReviewing;
    }
    
    public String[] getHidReportingEmpId() {
        return hidReportingEmpId;
    }

    public void setHidReportingEmpId(String[] hidReportingEmpId) {
        this.hidReportingEmpId = hidReportingEmpId;
    }

    public String[] getHidReviewingEmpId() {
        return hidReviewingEmpId;
    }

    public String getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public void setHidReviewingEmpId(String[] hidReviewingEmpId) {
        this.hidReviewingEmpId = hidReviewingEmpId;
    }

    public String[] getHidAcceptingEmpId() {
        return hidAcceptingEmpId;
    }

    public void setHidAcceptingEmpId(String[] hidAcceptingEmpId) {
        this.hidAcceptingEmpId = hidAcceptingEmpId;
    }

    public String[] getHidReportingSpcCode() {
        return hidReportingSpcCode;
    }

    public void setHidReportingSpcCode(String[] hidReportingSpcCode) {
        this.hidReportingSpcCode = hidReportingSpcCode;
    }

    public String[] getHidReviewingpcCode() {
        return hidReviewingpcCode;
    }

    public void setHidReviewingpcCode(String[] hidReviewingpcCode) {
        this.hidReviewingpcCode = hidReviewingpcCode;
    }

    public String[] getHidAcceptingSpcCode() {
        return hidAcceptingSpcCode;
    }

    public void setHidAcceptingSpcCode(String[] hidAcceptingSpcCode) {
        this.hidAcceptingSpcCode = hidAcceptingSpcCode;
    }

    public String[] getTxtReportingAuthFrmDt() {
        return txtReportingAuthFrmDt;
    }

    public void setTxtReportingAuthFrmDt(String[] txtReportingAuthFrmDt) {
        this.txtReportingAuthFrmDt = txtReportingAuthFrmDt;
    }

    public String[] getTxtReportingAuthToDt() {
        return txtReportingAuthToDt;
    }

    public void setTxtReportingAuthToDt(String[] txtReportingAuthToDt) {
        this.txtReportingAuthToDt = txtReportingAuthToDt;
    }

    public String[] getTxtReviewingAuthFrmDt() {
        return txtReviewingAuthFrmDt;
    }

    public void setTxtReviewingAuthFrmDt(String[] txtReviewingAuthFrmDt) {
        this.txtReviewingAuthFrmDt = txtReviewingAuthFrmDt;
    }

    public String[] getTxtReviewingAuthToDt() {
        return txtReviewingAuthToDt;
    }

    public void setTxtReviewingAuthToDt(String[] txtReviewingAuthToDt) {
        this.txtReviewingAuthToDt = txtReviewingAuthToDt;
    }

    public String[] getTxtAcceptingAuthFrmDt() {
        return txtAcceptingAuthFrmDt;
    }

    public void setTxtAcceptingAuthFrmDt(String[] txtAcceptingAuthFrmDt) {
        this.txtAcceptingAuthFrmDt = txtAcceptingAuthFrmDt;
    }

    public String[] getTxtAcceptingAuthToDt() {
        return txtAcceptingAuthToDt;
    }

    public void setTxtAcceptingAuthToDt(String[] txtAcceptingAuthToDt) {
        this.txtAcceptingAuthToDt = txtAcceptingAuthToDt;
    }

    public int getMaxhierarchyno() {
        return maxhierarchyno;
    }

    public void setMaxhierarchyno(int maxhierarchyno) {
        this.maxhierarchyno = maxhierarchyno;
    }

    public int getHidparid() {
        return hidparid;
    }

    public void setHidparid(int hidparid) {
        this.hidparid = hidparid;
    }

    public int getHidparstatus() {
        return hidparstatus;
    }

    public void setHidparstatus(int hidparstatus) {
        this.hidparstatus = hidparstatus;
    }

    public int getHidtaskid() {
        return hidtaskid;
    }

    public void setHidtaskid(int hidtaskid) {
        this.hidtaskid = hidtaskid;
    }

    public String getHidfiscalyear() {
        return hidfiscalyear;
    }

    public void setHidfiscalyear(String hidfiscalyear) {
        this.hidfiscalyear = hidfiscalyear;
    }

    public String getHidparfrmdt() {
        return hidparfrmdt;
    }

    public void setHidparfrmdt(String hidparfrmdt) {
        this.hidparfrmdt = hidparfrmdt;
    }

    public String getHidpartodt() {
        return hidpartodt;
    }

    public void setHidpartodt(String hidpartodt) {
        this.hidpartodt = hidpartodt;
    }

    public String getHidspc() {
        return hidspc;
    }

    public void setHidspc(String hidspc) {
        this.hidspc = hidspc;
    }

    public ArrayList getReportList() {
        return reportList;
    }

    public void setReportList(ArrayList reportList) {
        this.reportList = reportList;
    }

    public ArrayList getReviewList() {
        return reviewList;
    }

    public void setReviewList(ArrayList reviewList) {
        this.reviewList = reviewList;
    }

    public ArrayList getAcceptList() {
        return acceptList;
    }

    public void setAcceptList(ArrayList acceptList) {
        this.acceptList = acceptList;
    }

}
