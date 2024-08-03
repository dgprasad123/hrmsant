/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.billbrowser;

/**
 *
 * @author Manas
 */
public class ReportList {

    private String slNo = null;
    private String reportName = null;
    private String pdfLink = null;
    private String actionPath = null;
    private String hidePrintOption = null;
    private String reportRefSlno = null;
    private String unsignedpdfPath;
    private String signedpdfpath;
    private String esignLogId;

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportName() {
        return reportName;
    }

    public void setActionPath(String actionPath) {
        this.actionPath = actionPath;
    }

    public String getActionPath() {
        return actionPath;
    }

    public void setPdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
    }

    public String getPdfLink() {
        return pdfLink;
    }
    
    public void setHidePrintOption(String hidePrintOption) {
        this.hidePrintOption = hidePrintOption;
    }

    public String getHidePrintOption() {
        return hidePrintOption;
    }

    public String getReportRefSlno() {
        return reportRefSlno;
    }

    public void setReportRefSlno(String reportRefSlno) {
        this.reportRefSlno = reportRefSlno;
    }

    public String getUnsignedpdfPath() {
        return unsignedpdfPath;
    }

    public void setUnsignedpdfPath(String unsignedpdfPath) {
        this.unsignedpdfPath = unsignedpdfPath;
    }

    public String getSignedpdfpath() {
        return signedpdfpath;
    }

    public void setSignedpdfpath(String signedpdfpath) {
        this.signedpdfpath = signedpdfpath;
    }

    public String getEsignLogId() {
        return esignLogId;
    }

    public void setEsignLogId(String esignLogId) {
        this.esignLogId = esignLogId;
    }

}
