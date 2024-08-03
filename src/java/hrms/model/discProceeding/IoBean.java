/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.discProceeding;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author manisha
 */
public class IoBean {

    private int taskId;
    private String ioEmpHrmsId;
    private String ioEmpSPC;
    private String ioEmpName;
    private String ioEmpCurDegn;
    private String ioAppointhrmsId;
    private String ioAppointingspc;
    private String doEmpSpc;
    private String gender;
    private String gender1;
    private String gender2;
    private String officeName;
    private String[] doio;
    private String officertype;
    private int daId;
    private String chkempid;
    private String offCode;
    private String offName;
    private String ioAppoinmentOrdNo;
    private String ioAppoinmentOrdDt;
    private MultipartFile remarksByIOdocument;
    private String remarksByIOoriginalFileName;
    private String remarksByIOdiskFileName;
    private String remarksByIOcontentType;
    private byte[] Filecontent;
    private int daioid;
    private String hasIoRemarks;
    private String ioRemarksOrdNo;
    private String ioRemarksOrdDt;
    private String poHrmsId;
    private String poSPC;
    private String apoHrmsId;
    private String apoSPC;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender1() {
        return gender1;
    }

    public void setGender1(String gender1) {
        this.gender1 = gender1;
    }

    public String getGender2() {
        return gender2;
    }

    public void setGender2(String gender2) {
        this.gender2 = gender2;
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

    public int getDaId() {
        return daId;
    }

    public void setDaId(int daId) {
        this.daId = daId;
    }

    public String getChkempid() {
        return chkempid;
    }

    public void setChkempid(String chkempid) {
        this.chkempid = chkempid;
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

    public byte[] getFilecontent() {
        return Filecontent;
    }

    public void setFilecontent(byte[] Filecontent) {
        this.Filecontent = Filecontent;
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

}
