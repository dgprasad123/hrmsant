/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.policemodule;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manisha
 */
public class NominationDifferentDisciplinaryProceedingList {
    private int proceedingdetailid;
    private String proceedingDetail;
    private MultipartFile disciplinaryproceedingCopy = null;
    private String originalFilename = "";
    private String diskFileNamedisciplinaryProceeding = "";
    private byte[] filecontent = null;
    private String getContentType;

    public int getProceedingdetailid() {
        return proceedingdetailid;
    }

    public void setProceedingdetailid(int proceedingdetailid) {
        this.proceedingdetailid = proceedingdetailid;
    }

    public String getProceedingDetail() {
        return proceedingDetail;
    }

    public void setProceedingDetail(String proceedingDetail) {
        this.proceedingDetail = proceedingDetail;
    }

    public MultipartFile getDisciplinaryproceedingCopy() {
        return disciplinaryproceedingCopy;
    }

    public void setDisciplinaryproceedingCopy(MultipartFile disciplinaryproceedingCopy) {
        this.disciplinaryproceedingCopy = disciplinaryproceedingCopy;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    

    public String getDiskFileNamedisciplinaryProceeding() {
        return diskFileNamedisciplinaryProceeding;
    }

    public void setDiskFileNamedisciplinaryProceeding(String diskFileNamedisciplinaryProceeding) {
        this.diskFileNamedisciplinaryProceeding = diskFileNamedisciplinaryProceeding;
    }

    public byte[] getFilecontent() {
        return filecontent;
    }

    public void setFilecontent(byte[] filecontent) {
        this.filecontent = filecontent;
    }

    public String getGetContentType() {
        return getContentType;
    }

    public void setGetContentType(String getContentType) {
        this.getContentType = getContentType;
    }

 }
