/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.discProceeding;

import javax.mail.Multipart;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manisha
 */
public class CourtCaseBean {
    private String caseNo;
    private String caseName;
    private String fiscalyear;
    private MultipartFile courtDocumentForDP;
    private String courtDocumentorgFileName;
    private String courtDocumentdiskFileName;
    private String courtDocumentFileType;
    private byte[] Filecontent;
    private int daId;
    private int caseId;

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getFiscalyear() {
        return fiscalyear;
    }

    public void setFiscalyear(String fiscalyear) {
        this.fiscalyear = fiscalyear;
    }

    public MultipartFile getCourtDocumentForDP() {
        return courtDocumentForDP;
    }

    public void setCourtDocumentForDP(MultipartFile courtDocumentForDP) {
        this.courtDocumentForDP = courtDocumentForDP;
    }

   

    public String getCourtDocumentorgFileName() {
        return courtDocumentorgFileName;
    }

    public void setCourtDocumentorgFileName(String courtDocumentorgFileName) {
        this.courtDocumentorgFileName = courtDocumentorgFileName;
    }

    public String getCourtDocumentdiskFileName() {
        return courtDocumentdiskFileName;
    }

    public void setCourtDocumentdiskFileName(String courtDocumentdiskFileName) {
        this.courtDocumentdiskFileName = courtDocumentdiskFileName;
    }

    public String getCourtDocumentFileType() {
        return courtDocumentFileType;
    }

    public void setCourtDocumentFileType(String courtDocumentFileType) {
        this.courtDocumentFileType = courtDocumentFileType;
    }

    public byte[] getFilecontent() {
        return Filecontent;
    }

    public void setFilecontent(byte[] Filecontent) {
        this.Filecontent = Filecontent;
    }

    public int getDaId() {
        return daId;
    }

    public void setDaId(int daId) {
        this.daId = daId;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

}
