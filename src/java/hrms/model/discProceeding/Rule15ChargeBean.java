/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.discProceeding;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class Rule15ChargeBean {

    private String rule15Articles;
    private String rule15ChargDtls;
    private String rule15DocumentName;
    private MultipartFile rule15Document;
    private String witnessName;
    private int chargeDaId;
    private int chargeDacId;
    private int hidChargeDacId;
    private int hidChargeDaid;
    private String hidChargeDoHrmsId;
    private int hidWitnesDaid;
    private String hidWitnessDoHrmsId;
    private String hidWitnessIds;
    private int hidWitnessDacId;
    private int chkVal;
    private String hidMode;
    private MultipartFile rule15SubDoc;
    private String hidFowardDaId;
    private String hidPostCode;
    private String hidForwardHrmsId;
    private String hidOffCode2;        
    private String docType;
    private String orgFileName;
    private List documentList;
   

    
            
    public String getRule15Articles() {
        return rule15Articles;
    }

    public void setRule15Articles(String rule15Articles) {
        this.rule15Articles = rule15Articles;
    }

    public String getRule15ChargDtls() {
        return rule15ChargDtls;
    }

    public void setRule15ChargDtls(String rule15ChargDtls) {
        this.rule15ChargDtls = rule15ChargDtls;
    }

    public int getHidChargeDaid() {
        return hidChargeDaid;
    }

    public void setHidChargeDaid(int hidChargeDaid) {
        this.hidChargeDaid = hidChargeDaid;
    }

    public String getHidChargeDoHrmsId() {
        return hidChargeDoHrmsId;
    }

    public void setHidChargeDoHrmsId(String hidChargeDoHrmsId) {
        this.hidChargeDoHrmsId = hidChargeDoHrmsId;
    }

    public int getChargeDaId() {
        return chargeDaId;
    }

    public void setChargeDaId(int chargeDaId) {
        this.chargeDaId = chargeDaId;
    }

    public int getChargeDacId() {
        return chargeDacId;
    }

    public void setChargeDacId(int chargeDacId) {
        this.chargeDacId = chargeDacId;
    }

    public int getHidWitnesDaid() {
        return hidWitnesDaid;
    }

    public void setHidWitnesDaid(int hidWitnesDaid) {
        this.hidWitnesDaid = hidWitnesDaid;
    }

    public String getHidWitnessDoHrmsId() {
        return hidWitnessDoHrmsId;
    }

    public void setHidWitnessDoHrmsId(String hidWitnessDoHrmsId) {
        this.hidWitnessDoHrmsId = hidWitnessDoHrmsId;
    }

    public int getHidWitnessDacId() {
        return hidWitnessDacId;
    }

    public void setHidWitnessDacId(int hidWitnessDacId) {
        this.hidWitnessDacId = hidWitnessDacId;
    }

    public String getHidWitnessIds() {
        return hidWitnessIds;
    }

    public void setHidWitnessIds(String hidWitnessIds) {
        this.hidWitnessIds = hidWitnessIds;
    }

    public String getWitnessName() {
        return witnessName;
    }

    public void setWitnessName(String witnessName) {
        this.witnessName = witnessName;
    }

    public int getHidChargeDacId() {
        return hidChargeDacId;
    }

    public void setHidChargeDacId(int hidChargeDacId) {
        this.hidChargeDacId = hidChargeDacId;
    }

    public int getChkVal() {
        return chkVal;
    }

    public void setChkVal(int chkVal) {
        this.chkVal = chkVal;
    }

    public String getHidMode() {
        return hidMode;
    }

    public void setHidMode(String hidMode) {
        this.hidMode = hidMode;
    }

    public MultipartFile getRule15Document() {
        return rule15Document;
    }

    public void setRule15Document(MultipartFile rule15Document) {
        this.rule15Document = rule15Document;
    }

    public MultipartFile getRule15SubDoc() {
        return rule15SubDoc;
    }

    public void setRule15SubDoc(MultipartFile rule15SubDoc) {
        this.rule15SubDoc = rule15SubDoc;
    }

    public String getRule15DocumentName() {
        return rule15DocumentName;
    }

    public void setRule15DocumentName(String rule15DocumentName) {
        this.rule15DocumentName = rule15DocumentName;
    }

    public String getHidFowardDaId() {
        return hidFowardDaId;
    }

    public void setHidFowardDaId(String hidFowardDaId) {
        this.hidFowardDaId = hidFowardDaId;
    }

    public String getHidPostCode() {
        return hidPostCode;
    }

    public void setHidPostCode(String hidPostCode) {
        this.hidPostCode = hidPostCode;
    }

    public String getHidForwardHrmsId() {
        return hidForwardHrmsId;
    }

    public void setHidForwardHrmsId(String hidForwardHrmsId) {
        this.hidForwardHrmsId = hidForwardHrmsId;
    }

    public String getHidOffCode2() {
        return hidOffCode2;
    }

    public void setHidOffCode2(String hidOffCode2) {
        this.hidOffCode2 = hidOffCode2;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getOrgFileName() {
        return orgFileName;
    }

    public void setOrgFileName(String orgFileName) {
        this.orgFileName = orgFileName;
    }

    public List getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List documentList) {
        this.documentList = documentList;
    }

   
    
}
