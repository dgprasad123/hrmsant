/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.EmpQuarterAllotment;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manoj PC
 */
public class EmpQuarterBean {

    private int qaId;
    private String notId = null;
    private String notType = null;
    private String empId = null;
    private String newempId = null;
    private String quarterNo = null;
    private String qrtrtype = null;
    private String qrtrunit = null;
    private String qtrbldgno = null;
    private String allotmentDate = null;
    private String possessionDate = null;
    private String quarterRent = null;
    private String address = null;
    private String ifSurrendered = null;
    private String waterRent = null;
    private String qId = null;
    private String sewerageRent = null;
    private String isGetHra = null;
    private String consumerNo = null;
    private String orderNumber = null;
    private String orderDate = null;
    private String empName = null;
    private String designation = null;
    private String gpfNo = null;
    private String mobileno = null;
    private String transferId = null;
    private String offName;
    private String distCode;
    private String distName;
    private String joinDate;
    private String relieveDate;
    private String occupationTypes;
    private String retentionStatus;
    private String vacateNotice;
    private String vacateStatus;
    private String retentionType;
    private String retentionSubType;
    private int trackingId;
    private String officeFromCode;
    private String officeToCode;
    private String officeFromName;
    private String officeToName;

    private String quarterAllowedFromDate;
    private String quarterAllowedToDate;
    private String wefFromDate;
    private String retiremntWefDate;
    private String qrtVacantDate;
    private String nonkbkCancelledDate;
    private String vacateQuartersDate;
    private String vacateQuartersRetirement;
    private String vacateQuartersDeath;
    private String deathOff;
    private String dismissedOff;
    private String hidtfDeptCode;
    private String hidtfDistCode;
    private String hidtfOffCode;
    private String genericpostTF;
    private String hidTTDeptCode;
    private String hidTTDistCode;
    private String hidTTOffCode;
    private String genericpostTT;
    private String hidooDeptCode;
    private String hidooDistCode;
    private String hidooOffCode;
    private String genericpostoo;
    private String transferOn;
    private String reliefDate;
    private String oPPorderNumber;
    private String oPPorderDate;
    private String vacationDate;
    private int qrtFee = 0;
    private String nocStatus;
    private String oppStatus;
    private String evictionNotice;
    private String dos;
    private String splCaseStatus;
    private MultipartFile uploadDocument;
    private String diskFileName;
    private String originalFilename;
    private String extensionFromDate;
    private String extensionToDate;
    private byte[] filecontent = null;
    private String ContentType;
    private String extensionReason;
    private int nocId;
    private int orderFiveOne;
    private int noticeFiveOne;
    private String phdNocReason;
    private byte[] filecontentnoc = null;
    private String phddiskFileName;
    private String phdoriginalFilename;
    private String getphdContentType;
    private MultipartFile uploadDocumentphd;
    private String gedNocReason;
    private String geddiskFileName;
    private String gedoriginalFilename;
    private String gedUpload;
    private String phdUpload;
    private String nocfor;
    private String dor;
    private String phdNocStatus;
    private String gedNocStatus;
    private String phdNocFileName;
    private String gedNocFileName;
    private String waterNocStatus;
    private String waterNocFileName;
    private String waterNocReason;
    private String waterdiskFileName;
    private String wateroriginalFilename;
    private String offcode;
    private String nocRequest;
    private String nocType;
    private String documentSubmission;
    private String dsStatus;
    private String ds_org_file_name;
    private String ds_disk_file_name;
    private String isValidated;
    private String occuId;
    private String section;
    private int opbla;
    private String strmonth;
    private int year;
    private int assessment;
    private int real;
    private int cbalance;
    private String extensionStatus;
    private String oswasFileNo;
    private String licenceFeeType;
    private int oppCaseId;
    private String showCauseReply;
    private String tvd;
    private String opp_display;
    private String electricityNocStatus;
    private String electricitydiskFileName;
    private String electricityoriginalFilename;
    private String electricityNocFileName;
    private String estateAppeal;
    private String chkNotSBPrint;
    private String appealNotice;
    private String empid;
    private String NotToPrintSB;
    private String chkSBPrintIfSurrender;
    private String casefivetwoOrder;

    private String txtperiodTo;
    private String txtperiodFrom;
    private String vstatus;
    private String dob;
    
    private String municipalityTax;
    
    public String getQtrbldgno() {
        return qtrbldgno;
    }

    public void setQtrbldgno(String qtrbldgno) {
        this.qtrbldgno = qtrbldgno;
    }

    public String getVstatus() {
        return vstatus;
    }

    public void setVstatus(String vstatus) {
        this.vstatus = vstatus;
    } 
    
   // private String casefivetwoOrder;
    public int getQaId() {
        return qaId;
    }

    public void setQaId(int qaId) {
        this.qaId = qaId;
    }

    public String getqId() {
        return qId;
    }

    public void setqId(String qId) {
        this.qId = qId;
    }

    public String getNotId() {
        return notId;
    }

    public void setNotId(String notId) {
        this.notId = notId;
    }

    public String getNotType() {
        return notType;
    }

    public void setNotType(String notType) {
        this.notType = notType;
    }

    public String getNewempId() {
        return newempId;
    }

    public void setNewempId(String newempId) {
        this.newempId = newempId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getQuarterNo() {
        return quarterNo;
    }

    public void setQuarterNo(String quarterNo) {
        this.quarterNo = quarterNo;
    }

    public String getQrtrtype() {
        return qrtrtype;
    }

    public void setQrtrtype(String qrtrtype) {
        this.qrtrtype = qrtrtype;
    }

    public String getQrtrunit() {
        return qrtrunit;
    }

    public void setQrtrunit(String qrtrunit) {
        this.qrtrunit = qrtrunit;
    }

    public String getAllotmentDate() {
        return allotmentDate;
    }

    public void setAllotmentDate(String allotmentDate) {
        this.allotmentDate = allotmentDate;
    }

    public String getPossessionDate() {
        return possessionDate;
    }

    public void setPossessionDate(String possessionDate) {
        this.possessionDate = possessionDate;
    }

    public String getQuarterRent() {
        return quarterRent;
    }

    public void setQuarterRent(String quarterRent) {
        this.quarterRent = quarterRent;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIfSurrendered() {
        return ifSurrendered;
    }

    public void setIfSurrendered(String ifSurrendered) {
        this.ifSurrendered = ifSurrendered;
    }

    public String getWaterRent() {
        return waterRent;
    }

    public void setWaterRent(String waterRent) {
        this.waterRent = waterRent;
    }

    public String getSewerageRent() {
        return sewerageRent;
    }

    public void setSewerageRent(String sewerageRent) {
        this.sewerageRent = sewerageRent;
    }

    public String getIsGetHra() {
        return isGetHra;
    }

    public void setIsGetHra(String isGetHra) {
        this.isGetHra = isGetHra;
    }

    public String getConsumerNo() {
        return consumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getGpfNo() {
        return gpfNo;
    }

    public void setGpfNo(String gpfNo) {
        this.gpfNo = gpfNo;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getOffName() {
        return offName;
    }

    public void setOffName(String offName) {
        this.offName = offName;
    }

    public String getDistCode() {
        return distCode;
    }

    public void setDistCode(String distCode) {
        this.distCode = distCode;
    }

    public String getDistName() {
        return distName;
    }

    public void setDistName(String distName) {
        this.distName = distName;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getRelieveDate() {
        return relieveDate;
    }

    public void setRelieveDate(String relieveDate) {
        this.relieveDate = relieveDate;
    }

    public String getOccupationTypes() {
        return occupationTypes;
    }

    public void setOccupationTypes(String occupationTypes) {
        this.occupationTypes = occupationTypes;
    }

    public String getRetentionStatus() {
        return retentionStatus;
    }

    public void setRetentionStatus(String retentionStatus) {
        this.retentionStatus = retentionStatus;
    }

    public int getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(int trackingId) {
        this.trackingId = trackingId;
    }

    public String getRetentionType() {
        return retentionType;
    }

    public void setRetentionType(String retentionType) {
        this.retentionType = retentionType;
    }

    public String getRetentionSubType() {
        return retentionSubType;
    }

    public void setRetentionSubType(String retentionSubType) {
        this.retentionSubType = retentionSubType;
    }

    public String getOfficeFromCode() {
        return officeFromCode;
    }

    public void setOfficeFromCode(String officeFromCode) {
        this.officeFromCode = officeFromCode;
    }

    public String getOfficeToCode() {
        return officeToCode;
    }

    public void setOfficeToCode(String officeToCode) {
        this.officeToCode = officeToCode;
    }

    public String getOfficeFromName() {
        return officeFromName;
    }

    public void setOfficeFromName(String officeFromName) {
        this.officeFromName = officeFromName;
    }

    public String getOfficeToName() {
        return officeToName;
    }

    public void setOfficeToName(String officeToName) {
        this.officeToName = officeToName;
    }

    public String getQuarterAllowedFromDate() {
        return quarterAllowedFromDate;
    }

    public void setQuarterAllowedFromDate(String quarterAllowedFromDate) {
        this.quarterAllowedFromDate = quarterAllowedFromDate;
    }

    public String getQuarterAllowedToDate() {
        return quarterAllowedToDate;
    }

    public void setQuarterAllowedToDate(String quarterAllowedToDate) {
        this.quarterAllowedToDate = quarterAllowedToDate;
    }

    public String getWefFromDate() {
        return wefFromDate;
    }

    public void setWefFromDate(String wefFromDate) {
        this.wefFromDate = wefFromDate;
    }

    public String getRetiremntWefDate() {
        return retiremntWefDate;
    }

    public void setRetiremntWefDate(String retiremntWefDate) {
        this.retiremntWefDate = retiremntWefDate;
    }

    public String getQrtVacantDate() {
        return qrtVacantDate;
    }

    public void setQrtVacantDate(String qrtVacantDate) {
        this.qrtVacantDate = qrtVacantDate;
    }

    public String getNonkbkCancelledDate() {
        return nonkbkCancelledDate;
    }

    public void setNonkbkCancelledDate(String nonkbkCancelledDate) {
        this.nonkbkCancelledDate = nonkbkCancelledDate;
    }

    public String getVacateQuartersDate() {
        return vacateQuartersDate;
    }

    public void setVacateQuartersDate(String vacateQuartersDate) {
        this.vacateQuartersDate = vacateQuartersDate;
    }

    public String getVacateQuartersRetirement() {
        return vacateQuartersRetirement;
    }

    public void setVacateQuartersRetirement(String vacateQuartersRetirement) {
        this.vacateQuartersRetirement = vacateQuartersRetirement;
    }

    public String getDeathOff() {
        return deathOff;
    }

    public void setDeathOff(String deathOff) {
        this.deathOff = deathOff;
    }

    public String getDismissedOff() {
        return dismissedOff;
    }

    public void setDismissedOff(String dismissedOff) {
        this.dismissedOff = dismissedOff;
    }

    public String getVacateQuartersDeath() {
        return vacateQuartersDeath;
    }

    public void setVacateQuartersDeath(String vacateQuartersDeath) {
        this.vacateQuartersDeath = vacateQuartersDeath;
    }

    public String getHidtfDeptCode() {
        return hidtfDeptCode;
    }

    public void setHidtfDeptCode(String hidtfDeptCode) {
        this.hidtfDeptCode = hidtfDeptCode;
    }

    public String getHidtfDistCode() {
        return hidtfDistCode;
    }

    public void setHidtfDistCode(String hidtfDistCode) {
        this.hidtfDistCode = hidtfDistCode;
    }

    public String getHidtfOffCode() {
        return hidtfOffCode;
    }

    public void setHidtfOffCode(String hidtfOffCode) {
        this.hidtfOffCode = hidtfOffCode;
    }

    public String getGenericpostTF() {
        return genericpostTF;
    }

    public void setGenericpostTF(String genericpostTF) {
        this.genericpostTF = genericpostTF;
    }

    public String getHidTTDeptCode() {
        return hidTTDeptCode;
    }

    public void setHidTTDeptCode(String hidTTDeptCode) {
        this.hidTTDeptCode = hidTTDeptCode;
    }

    public String getHidTTDistCode() {
        return hidTTDistCode;
    }

    public void setHidTTDistCode(String hidTTDistCode) {
        this.hidTTDistCode = hidTTDistCode;
    }

    public String getHidTTOffCode() {
        return hidTTOffCode;
    }

    public void setHidTTOffCode(String hidTTOffCode) {
        this.hidTTOffCode = hidTTOffCode;
    }

    public String getGenericpostTT() {
        return genericpostTT;
    }

    public void setGenericpostTT(String genericpostTT) {
        this.genericpostTT = genericpostTT;
    }

    public String getHidooDeptCode() {
        return hidooDeptCode;
    }

    public void setHidooDeptCode(String hidooDeptCode) {
        this.hidooDeptCode = hidooDeptCode;
    }

    public String getHidooDistCode() {
        return hidooDistCode;
    }

    public void setHidooDistCode(String hidooDistCode) {
        this.hidooDistCode = hidooDistCode;
    }

    public String getHidooOffCode() {
        return hidooOffCode;
    }

    public void setHidooOffCode(String hidooOffCode) {
        this.hidooOffCode = hidooOffCode;
    }

    public String getGenericpostoo() {
        return genericpostoo;
    }

    public void setGenericpostoo(String genericpostoo) {
        this.genericpostoo = genericpostoo;
    }

    public String getTransferOn() {
        return transferOn;
    }

    public void setTransferOn(String transferOn) {
        this.transferOn = transferOn;
    }

    public String getReliefDate() {
        return reliefDate;
    }

    public void setReliefDate(String reliefDate) {
        this.reliefDate = reliefDate;
    }

    public String getVacateNotice() {
        return vacateNotice;
    }

    public void setVacateNotice(String vacateNotice) {
        this.vacateNotice = vacateNotice;
    }

    public String getVacateStatus() {
        return vacateStatus;
    }

    public void setVacateStatus(String vacateStatus) {
        this.vacateStatus = vacateStatus;
    }

    public String getoPPorderNumber() {
        return oPPorderNumber;
    }

    public void setoPPorderNumber(String oPPorderNumber) {
        this.oPPorderNumber = oPPorderNumber;
    }

    public String getoPPorderDate() {
        return oPPorderDate;
    }

    public void setoPPorderDate(String oPPorderDate) {
        this.oPPorderDate = oPPorderDate;
    }

    public String getVacationDate() {
        return vacationDate;
    }

    public void setVacationDate(String vacationDate) {
        this.vacationDate = vacationDate;
    }

    public int getQrtFee() {
        return qrtFee;
    }

    public void setQrtFee(int qrtFee) {
        this.qrtFee = qrtFee;
    }

    public String getNocStatus() {
        return nocStatus;
    }

    public void setNocStatus(String nocStatus) {
        this.nocStatus = nocStatus;
    }

    public String getOppStatus() {
        return oppStatus;
    }

    public void setOppStatus(String oppStatus) {
        this.oppStatus = oppStatus;
    }

    public String getEvictionNotice() {
        return evictionNotice;
    }

    public void setEvictionNotice(String evictionNotice) {
        this.evictionNotice = evictionNotice;
    }

    public String getDos() {
        return dos;
    }

    public void setDos(String dos) {
        this.dos = dos;
    }

    public MultipartFile getUploadDocument() {
        return uploadDocument;
    }

    public void setUploadDocument(MultipartFile uploadDocument) {
        this.uploadDocument = uploadDocument;
    }

    public String getDiskFileName() {
        return diskFileName;
    }

    public void setDiskFileName(String diskFileName) {
        this.diskFileName = diskFileName;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String ContentType) {
        this.ContentType = ContentType;
    }

    public byte[] getFilecontent() {
        return filecontent;
    }

    public void setFilecontent(byte[] filecontent) {
        this.filecontent = filecontent;
    }

    public String getSplCaseStatus() {
        return splCaseStatus;
    }

    public void setSplCaseStatus(String splCaseStatus) {
        this.splCaseStatus = splCaseStatus;
    }

    public String getExtensionFromDate() {
        return extensionFromDate;
    }

    public void setExtensionFromDate(String extensionFromDate) {
        this.extensionFromDate = extensionFromDate;
    }

    public String getExtensionToDate() {
        return extensionToDate;
    }

    public void setExtensionToDate(String extensionToDate) {
        this.extensionToDate = extensionToDate;
    }

    public String getExtensionReason() {
        return extensionReason;
    }

    public void setExtensionReason(String extensionReason) {
        this.extensionReason = extensionReason;
    }

    public int getNocId() {
        return nocId;
    }

    public void setNocId(int nocId) {
        this.nocId = nocId;
    }

    public String getPhdNocReason() {
        return phdNocReason;
    }

    public void setPhdNocReason(String phdNocReason) {
        this.phdNocReason = phdNocReason;
    }

    public byte[] getFilecontentnoc() {
        return filecontentnoc;
    }

    public void setFilecontentnoc(byte[] filecontentnoc) {
        this.filecontentnoc = filecontentnoc;
    }

    public String getPhddiskFileName() {
        return phddiskFileName;
    }

    public void setPhddiskFileName(String phddiskFileName) {
        this.phddiskFileName = phddiskFileName;
    }

    public String getPhdoriginalFilename() {
        return phdoriginalFilename;
    }

    public void setPhdoriginalFilename(String phdoriginalFilename) {
        this.phdoriginalFilename = phdoriginalFilename;
    }

    public String getGetphdContentType() {
        return getphdContentType;
    }

    public void setGetphdContentType(String getphdContentType) {
        this.getphdContentType = getphdContentType;
    }

    public MultipartFile getUploadDocumentphd() {
        return uploadDocumentphd;
    }

    public void setUploadDocumentphd(MultipartFile uploadDocumentphd) {
        this.uploadDocumentphd = uploadDocumentphd;
    }

    public String getGedNocReason() {
        return gedNocReason;
    }

    public void setGedNocReason(String gedNocReason) {
        this.gedNocReason = gedNocReason;
    }

    public String getGeddiskFileName() {
        return geddiskFileName;
    }

    public void setGeddiskFileName(String geddiskFileName) {
        this.geddiskFileName = geddiskFileName;
    }

    public String getGedoriginalFilename() {
        return gedoriginalFilename;
    }

    public void setGedoriginalFilename(String gedoriginalFilename) {
        this.gedoriginalFilename = gedoriginalFilename;
    }

    public String getGedUpload() {
        return gedUpload;
    }

    public void setGedUpload(String gedUpload) {
        this.gedUpload = gedUpload;
    }

    public String getPhdUpload() {
        return phdUpload;
    }

    public void setPhdUpload(String phdUpload) {
        this.phdUpload = phdUpload;
    }

    public String getNocfor() {
        return nocfor;
    }

    public void setNocfor(String nocfor) {
        this.nocfor = nocfor;
    }

    public String getDor() {
        return dor;
    }

    public void setDor(String dor) {
        this.dor = dor;
    }

    public String getPhdNocStatus() {
        return phdNocStatus;
    }

    public void setPhdNocStatus(String phdNocStatus) {
        this.phdNocStatus = phdNocStatus;
    }

    public String getGedNocStatus() {
        return gedNocStatus;
    }

    public void setGedNocStatus(String gedNocStatus) {
        this.gedNocStatus = gedNocStatus;
    }

    public String getPhdNocFileName() {
        return phdNocFileName;
    }

    public void setPhdNocFileName(String phdNocFileName) {
        this.phdNocFileName = phdNocFileName;
    }

    public String getGedNocFileName() {
        return gedNocFileName;
    }

    public void setGedNocFileName(String gedNocFileName) {
        this.gedNocFileName = gedNocFileName;
    }

    public String getOffcode() {
        return offcode;
    }

    public void setOffcode(String offcode) {
        this.offcode = offcode;
    }

    public String getNocRequest() {
        return nocRequest;
    }

    public void setNocRequest(String nocRequest) {
        this.nocRequest = nocRequest;
    }

    public String getNocType() {
        return nocType;
    }

    public void setNocType(String nocType) {
        this.nocType = nocType;
    }

    public String getDocumentSubmission() {
        return documentSubmission;
    }

    public void setDocumentSubmission(String documentSubmission) {
        this.documentSubmission = documentSubmission;
    }

    public String getDsStatus() {
        return dsStatus;
    }

    public void setDsStatus(String dsStatus) {
        this.dsStatus = dsStatus;
    }

    public String getDs_org_file_name() {
        return ds_org_file_name;
    }

    public void setDs_org_file_name(String ds_org_file_name) {
        this.ds_org_file_name = ds_org_file_name;
    }

    public String getDs_disk_file_name() {
        return ds_disk_file_name;
    }

    public void setDs_disk_file_name(String ds_disk_file_name) {
        this.ds_disk_file_name = ds_disk_file_name;
    }

    public String getIsValidated() {
        return isValidated;
    }

    public void setIsValidated(String isValidated) {
        this.isValidated = isValidated;
    }

    public String getOccuId() {
        return occuId;
    }

    public void setOccuId(String occuId) {
        this.occuId = occuId;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getOpbla() {
        return opbla;
    }

    public void setOpbla(int opbla) {
        this.opbla = opbla;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getAssessment() {
        return assessment;
    }

    public void setAssessment(int assessment) {
        this.assessment = assessment;
    }

    public int getReal() {
        return real;
    }

    public void setReal(int real) {
        this.real = real;
    }

    public int getCbalance() {
        return cbalance;
    }

    public void setCbalance(int cbalance) {
        this.cbalance = cbalance;
    }

    public String getStrmonth() {
        return strmonth;
    }

    public void setStrmonth(String strmonth) {
        this.strmonth = strmonth;
    }

    public String getExtensionStatus() {
        return extensionStatus;
    }

    public void setExtensionStatus(String extensionStatus) {
        this.extensionStatus = extensionStatus;
    }

    public String getLicenceFeeType() {
        return licenceFeeType;
    }

    public void setLicenceFeeType(String licenceFeeType) {
        this.licenceFeeType = licenceFeeType;
    }

    public String getOswasFileNo() {
        return oswasFileNo;
    }

    public void setOswasFileNo(String oswasFileNo) {
        this.oswasFileNo = oswasFileNo;
    }

    public int getOppCaseId() {
        return oppCaseId;
    }

    public void setOppCaseId(int oppCaseId) {
        this.oppCaseId = oppCaseId;
    }

    public String getShowCauseReply() {
        return showCauseReply;
    }

    public void setShowCauseReply(String showCauseReply) {
        this.showCauseReply = showCauseReply;
    }

    public String getTvd() {
        return tvd;
    }

    public void setTvd(String tvd) {
        this.tvd = tvd;
    }

    public String getOpp_display() {
        return opp_display;
    }

    public void setOpp_display(String opp_display) {
        this.opp_display = opp_display;
    }

    public String getElectricityNocStatus() {
        return electricityNocStatus;
    }

    public void setElectricityNocStatus(String electricityNocStatus) {
        this.electricityNocStatus = electricityNocStatus;
    }

    public String getElectricitydiskFileName() {
        return electricitydiskFileName;
    }

    public void setElectricitydiskFileName(String electricitydiskFileName) {
        this.electricitydiskFileName = electricitydiskFileName;
    }

    public String getElectricityoriginalFilename() {
        return electricityoriginalFilename;
    }

    public void setElectricityoriginalFilename(String electricityoriginalFilename) {
        this.electricityoriginalFilename = electricityoriginalFilename;
    }

    public String getElectricityNocFileName() {
        return electricityNocFileName;
    }

    public void setElectricityNocFileName(String electricityNocFileName) {
        this.electricityNocFileName = electricityNocFileName;
    }

    public int getOrderFiveOne() {
        return orderFiveOne;
    }

    public void setOrderFiveOne(int orderFiveOne) {
        this.orderFiveOne = orderFiveOne;
    }

    public int getNoticeFiveOne() {
        return noticeFiveOne;
    }

    public void setNoticeFiveOne(int noticeFiveOne) {
        this.noticeFiveOne = noticeFiveOne;
    }

    public String getWaterNocStatus() {
        return waterNocStatus;
    }

    public void setWaterNocStatus(String waterNocStatus) {
        this.waterNocStatus = waterNocStatus;
    }

    public String getWaterNocFileName() {
        return waterNocFileName;
    }

    public void setWaterNocFileName(String waterNocFileName) {
        this.waterNocFileName = waterNocFileName;
    }

    public String getWaterNocReason() {
        return waterNocReason;
    }

    public void setWaterNocReason(String waterNocReason) {
        this.waterNocReason = waterNocReason;
    }

    public String getWaterdiskFileName() {
        return waterdiskFileName;
    }

    public void setWaterdiskFileName(String waterdiskFileName) {
        this.waterdiskFileName = waterdiskFileName;
    }

    public String getWateroriginalFilename() {
        return wateroriginalFilename;
    }

    public void setWateroriginalFilename(String wateroriginalFilename) {
        this.wateroriginalFilename = wateroriginalFilename;
    }

    public String getEstateAppeal() {
        return estateAppeal;
    }

    public void setEstateAppeal(String estateAppeal) {
        this.estateAppeal = estateAppeal;
    }

    public String getChkNotSBPrint() {
        return chkNotSBPrint;
    }

    public void setChkNotSBPrint(String chkNotSBPrint) {
        this.chkNotSBPrint = chkNotSBPrint;
    }

    public String getAppealNotice() {
        return appealNotice;
    }

    public void setAppealNotice(String appealNotice) {
        this.appealNotice = appealNotice;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getNotToPrintSB() {
        return NotToPrintSB;
    }

    public void setNotToPrintSB(String NotToPrintSB) {
        this.NotToPrintSB = NotToPrintSB;
    }

    public String getChkSBPrintIfSurrender() {
        return chkSBPrintIfSurrender;
    }

    public void setChkSBPrintIfSurrender(String chkSBPrintIfSurrender) {
        this.chkSBPrintIfSurrender = chkSBPrintIfSurrender;
    }

    public String getCasefivetwoOrder() {
        return casefivetwoOrder;
    }

    public void setCasefivetwoOrder(String casefivetwoOrder) {
        this.casefivetwoOrder = casefivetwoOrder;
    }

    public String getTxtperiodTo() {
        return txtperiodTo;
    }

    public void setTxtperiodTo(String txtperiodTo) {
        this.txtperiodTo = txtperiodTo;
    }

    public String getTxtperiodFrom() {
        return txtperiodFrom;
    }

    public void setTxtperiodFrom(String txtperiodFrom) {
        this.txtperiodFrom = txtperiodFrom;
    }

    public String getMunicipalityTax() {
        return municipalityTax;
    }

    public void setMunicipalityTax(String municipalityTax) {
        this.municipalityTax = municipalityTax;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
