/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.AGPension;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author lenovo
 */
public class PensionNOCBean {

    private String authorityType;
    private String name;
    private String gpfNo;
    private String doj;
    private String post;
    private String postCode;
    private String hrmsid;
    private String dos;
    private String nocfor;
    private int nocId;
    private String appraiseeName;
    private String appraiseeId;
    private String appraiseeSpc;
    private String appraiseePost;
    private String alreadyAdded;

    private String dob;

    private String vNocStatus;
    private String cNocStatus;
    private String vNocFileName;
    private String cNocFileName;

    private String offcode;

    private String mobile;
    private String ddoCode;
    private String gpfno;
    private String address;
    private String fatherName;

    private String offname;
    private String departmentName;
    private String vigilanceNocReason;
    private byte[] filecontent = null;
    private String vigilancediskFileName;
    private String vigilanceoriginalFilename;
    private String getvigilanceContentType;
    private MultipartFile uploadDocumentvigilance;
    private String cbNocReason;
    private String cbdiskFileName;
    private String cboriginalFilename;

    private String cbranchUpload;
    private String vigilanceUpload;

    private String nocRequest;

    private String cadreValue;
    private String cadreName;
    private int noofdays;
    private String doe;
    private String relation;
    private String dateFrom;
    private String dateTo;

    public String getAuthorityType() {
        return authorityType;
    }

    public void setAuthorityType(String authorityType) {
        this.authorityType = authorityType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGpfNo() {
        return gpfNo;
    }

    public void setGpfNo(String gpfNo) {
        this.gpfNo = gpfNo;
    }

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
    
    

    public String getHrmsid() {
        return hrmsid;
    }

    public void setHrmsid(String hrmsid) {
        this.hrmsid = hrmsid;
    }

    public String getDos() {
        return dos;
    }

    public void setDos(String dos) {
        this.dos = dos;
    }

    public String getNocfor() {
        return nocfor;
    }

    public void setNocfor(String nocfor) {
        this.nocfor = nocfor;
    }

    public String getOffcode() {
        return offcode;
    }

    public void setOffcode(String offcode) {
        this.offcode = offcode;
    }

    public int getNocId() {
        return nocId;
    }

    public void setNocId(int nocId) {
        this.nocId = nocId;
    }

    public String getAppraiseeName() {
        return appraiseeName;
    }

    public void setAppraiseeName(String appraiseeName) {
        this.appraiseeName = appraiseeName;
    }

    public String getAppraiseeId() {
        return appraiseeId;
    }

    public void setAppraiseeId(String appraiseeId) {
        this.appraiseeId = appraiseeId;
    }

    public String getAppraiseeSpc() {
        return appraiseeSpc;
    }

    public void setAppraiseeSpc(String appraiseeSpc) {
        this.appraiseeSpc = appraiseeSpc;
    }

    public String getAppraiseePost() {
        return appraiseePost;
    }

    public void setAppraiseePost(String appraiseePost) {
        this.appraiseePost = appraiseePost;
    }

    public String getAlreadyAdded() {
        return alreadyAdded;
    }

    public void setAlreadyAdded(String alreadyAdded) {
        this.alreadyAdded = alreadyAdded;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getvNocStatus() {
        return vNocStatus;
    }

    public void setvNocStatus(String vNocStatus) {
        this.vNocStatus = vNocStatus;
    }

    public String getcNocStatus() {
        return cNocStatus;
    }

    public void setcNocStatus(String cNocStatus) {
        this.cNocStatus = cNocStatus;
    }

    public String getvNocFileName() {
        return vNocFileName;
    }

    public void setvNocFileName(String vNocFileName) {
        this.vNocFileName = vNocFileName;
    }

    public String getcNocFileName() {
        return cNocFileName;
    }

    public void setcNocFileName(String cNocFileName) {
        this.cNocFileName = cNocFileName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDdoCode() {
        return ddoCode;
    }

    public void setDdoCode(String ddoCode) {
        this.ddoCode = ddoCode;
    }

    public String getGpfno() {
        return gpfno;
    }

    public void setGpfno(String gpfno) {
        this.gpfno = gpfno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOffname() {
        return offname;
    }

    public void setOffname(String offname) {
        this.offname = offname;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getVigilanceNocReason() {
        return vigilanceNocReason;
    }

    public void setVigilanceNocReason(String vigilanceNocReason) {
        this.vigilanceNocReason = vigilanceNocReason;
    }

    public byte[] getFilecontent() {
        return filecontent;
    }

    public void setFilecontent(byte[] filecontent) {
        this.filecontent = filecontent;
    }

    public String getVigilancediskFileName() {
        return vigilancediskFileName;
    }

    public void setVigilancediskFileName(String vigilancediskFileName) {
        this.vigilancediskFileName = vigilancediskFileName;
    }

    public String getVigilanceoriginalFilename() {
        return vigilanceoriginalFilename;
    }

    public void setVigilanceoriginalFilename(String vigilanceoriginalFilename) {
        this.vigilanceoriginalFilename = vigilanceoriginalFilename;
    }

    public String getGetvigilanceContentType() {
        return getvigilanceContentType;
    }

    public void setGetvigilanceContentType(String getvigilanceContentType) {
        this.getvigilanceContentType = getvigilanceContentType;
    }

    public MultipartFile getUploadDocumentvigilance() {
        return uploadDocumentvigilance;
    }

    public void setUploadDocumentvigilance(MultipartFile uploadDocumentvigilance) {
        this.uploadDocumentvigilance = uploadDocumentvigilance;
    }

    public String getCbNocReason() {
        return cbNocReason;
    }

    public void setCbNocReason(String cbNocReason) {
        this.cbNocReason = cbNocReason;
    }

    public String getCbdiskFileName() {
        return cbdiskFileName;
    }

    public void setCbdiskFileName(String cbdiskFileName) {
        this.cbdiskFileName = cbdiskFileName;
    }

    public String getCboriginalFilename() {
        return cboriginalFilename;
    }

    public void setCboriginalFilename(String cboriginalFilename) {
        this.cboriginalFilename = cboriginalFilename;
    }

    public String getNocRequest() {
        return nocRequest;
    }

    public void setNocRequest(String nocRequest) {
        this.nocRequest = nocRequest;
    }

    public String getCbranchUpload() {
        return cbranchUpload;
    }

    public void setCbranchUpload(String cbranchUpload) {
        this.cbranchUpload = cbranchUpload;
    }

    public String getVigilanceUpload() {
        return vigilanceUpload;
    }

    public void setVigilanceUpload(String vigilanceUpload) {
        this.vigilanceUpload = vigilanceUpload;
    }

    public String getCadreValue() {
        return cadreValue;
    }

    public void setCadreValue(String cadreValue) {
        this.cadreValue = cadreValue;
    }

    public String getCadreName() {
        return cadreName;
    }

    public void setCadreName(String cadreName) {
        this.cadreName = cadreName;
    }

    public int getNoofdays() {
        return noofdays;
    }

    public void setNoofdays(int noofdays) {
        this.noofdays = noofdays;
    }

    public String getDoe() {
        return doe;
    }

    public void setDoe(String doe) {
        this.doe = doe;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }
    
}
