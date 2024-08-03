/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.discProceeding;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author manisha
 */
public class DiscChargeBean {

    private int dacwid;
    private String memoNo;
    private String memoDate;
    private int daId;
    private int dacid;
    private int dadid;
    private String articleOfCharge;
    private String statementOfImputation;
    private String memoOfEvidence;
    private String uploadFileName;
    private int uploadFileId;
    private MultipartFile uploadDocument;
    private String briefDescriptionOfDocument;
    private String offName;
    private String offHeadDesignation;
    private String remark;
    private List witnessList;
    private int taskId;
    private MultipartFile articlesofChargeDocument;
    private MultipartFile statementOfImputationDocument;
    private MultipartFile memoofEvidenceDocument;
    private MultipartFile descriptionOfDocument;
    private String articlesofChargeoriginalfilename;
    private String statementOfImputationoriginalfilename;
    private String memoofEvidenceoriginalfilename;
    private String descriptionOfDocumentoriginalfilename;
    private String articlesofChargediskFileName;
    private String statementOfImputationdiskFileName;
    private String memoofEvidencediskFileName;
    private String descriptionOfDocumentdiskFileName;
    private String articlesofChargeContentType;
    private String statementOfImputationContentType;
    private String memoofEvidenceContentType;
    private String descriptionOfDocumentContentType;
    private String documentTypeName;

   

    public int getDacwid() {
        return dacwid;
    }

    public void setDacwid(int dacwid) {
        this.dacwid = dacwid;
    }

    public String getMemoNo() {
        return memoNo;
    }

    public void setMemoNo(String memoNo) {
        this.memoNo = memoNo;
    }

    public String getMemoDate() {
        return memoDate;
    }

    public void setMemoDate(String memoDate) {
        this.memoDate = memoDate;
    }

    public int getDaId() {
        return daId;
    }

    public void setDaId(int daId) {
        this.daId = daId;
    }

    public int getDacid() {
        return dacid;
    }

    public void setDacid(int dacid) {
        this.dacid = dacid;
    }

    public int getDadid() {
        return dadid;
    }

    public void setDadid(int dadid) {
        this.dadid = dadid;
    }

    public String getArticleOfCharge() {
        return articleOfCharge;
    }

    public void setArticleOfCharge(String articleOfCharge) {
        this.articleOfCharge = articleOfCharge;
    }

    public String getStatementOfImputation() {
        return statementOfImputation;
    }

    public void setStatementOfImputation(String statementOfImputation) {
        this.statementOfImputation = statementOfImputation;
    }

    public String getMemoOfEvidence() {
        return memoOfEvidence;
    }

    public void setMemoOfEvidence(String memoOfEvidence) {
        this.memoOfEvidence = memoOfEvidence;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public int getUploadFileId() {
        return uploadFileId;
    }

    public void setUploadFileId(int uploadFileId) {
        this.uploadFileId = uploadFileId;
    }

    public MultipartFile getUploadDocument() {
        return uploadDocument;
    }

    public void setUploadDocument(MultipartFile uploadDocument) {
        this.uploadDocument = uploadDocument;
    }

    public String getBriefDescriptionOfDocument() {
        return briefDescriptionOfDocument;
    }

    public void setBriefDescriptionOfDocument(String briefDescriptionOfDocument) {
        this.briefDescriptionOfDocument = briefDescriptionOfDocument;
    }

    public String getOffName() {
        return offName;
    }

    public void setOffName(String offName) {
        this.offName = offName;
    }

    public String getOffHeadDesignation() {
        return offHeadDesignation;
    }

    public void setOffHeadDesignation(String offHeadDesignation) {
        this.offHeadDesignation = offHeadDesignation;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List getWitnessList() {
        return witnessList;
    }

    public void setWitnessList(List witnessList) {
        this.witnessList = witnessList;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public MultipartFile getArticlesofChargeDocument() {
        return articlesofChargeDocument;
    }

    public void setArticlesofChargeDocument(MultipartFile articlesofChargeDocument) {
        this.articlesofChargeDocument = articlesofChargeDocument;
    }

    public MultipartFile getStatementOfImputationDocument() {
        return statementOfImputationDocument;
    }

    public void setStatementOfImputationDocument(MultipartFile statementOfImputationDocument) {
        this.statementOfImputationDocument = statementOfImputationDocument;
    }

    public MultipartFile getMemoofEvidenceDocument() {
        return memoofEvidenceDocument;
    }

    public void setMemoofEvidenceDocument(MultipartFile memoofEvidenceDocument) {
        this.memoofEvidenceDocument = memoofEvidenceDocument;
    }

    public MultipartFile getDescriptionOfDocument() {
        return descriptionOfDocument;
    }

    public void setDescriptionOfDocument(MultipartFile descriptionOfDocument) {
        this.descriptionOfDocument = descriptionOfDocument;
    }

    public String getArticlesofChargeoriginalfilename() {
        return articlesofChargeoriginalfilename;
    }

    public void setArticlesofChargeoriginalfilename(String articlesofChargeoriginalfilename) {
        this.articlesofChargeoriginalfilename = articlesofChargeoriginalfilename;
    }

    public String getStatementOfImputationoriginalfilename() {
        return statementOfImputationoriginalfilename;
    }

    public void setStatementOfImputationoriginalfilename(String statementOfImputationoriginalfilename) {
        this.statementOfImputationoriginalfilename = statementOfImputationoriginalfilename;
    }

    public String getMemoofEvidenceoriginalfilename() {
        return memoofEvidenceoriginalfilename;
    }

    public void setMemoofEvidenceoriginalfilename(String memoofEvidenceoriginalfilename) {
        this.memoofEvidenceoriginalfilename = memoofEvidenceoriginalfilename;
    }

    public String getDescriptionOfDocumentoriginalfilename() {
        return descriptionOfDocumentoriginalfilename;
    }

    public void setDescriptionOfDocumentoriginalfilename(String descriptionOfDocumentoriginalfilename) {
        this.descriptionOfDocumentoriginalfilename = descriptionOfDocumentoriginalfilename;
    }

    public String getArticlesofChargediskFileName() {
        return articlesofChargediskFileName;
    }

    public void setArticlesofChargediskFileName(String articlesofChargediskFileName) {
        this.articlesofChargediskFileName = articlesofChargediskFileName;
    }

    public String getStatementOfImputationdiskFileName() {
        return statementOfImputationdiskFileName;
    }

    public void setStatementOfImputationdiskFileName(String statementOfImputationdiskFileName) {
        this.statementOfImputationdiskFileName = statementOfImputationdiskFileName;
    }

    public String getMemoofEvidencediskFileName() {
        return memoofEvidencediskFileName;
    }

    public void setMemoofEvidencediskFileName(String memoofEvidencediskFileName) {
        this.memoofEvidencediskFileName = memoofEvidencediskFileName;
    }

    public String getDescriptionOfDocumentdiskFileName() {
        return descriptionOfDocumentdiskFileName;
    }

    public void setDescriptionOfDocumentdiskFileName(String descriptionOfDocumentdiskFileName) {
        this.descriptionOfDocumentdiskFileName = descriptionOfDocumentdiskFileName;
    }

    public String getArticlesofChargeContentType() {
        return articlesofChargeContentType;
    }

    public void setArticlesofChargeContentType(String articlesofChargeContentType) {
        this.articlesofChargeContentType = articlesofChargeContentType;
    }

    public String getStatementOfImputationContentType() {
        return statementOfImputationContentType;
    }

    public void setStatementOfImputationContentType(String statementOfImputationContentType) {
        this.statementOfImputationContentType = statementOfImputationContentType;
    }

    public String getMemoofEvidenceContentType() {
        return memoofEvidenceContentType;
    }

    public void setMemoofEvidenceContentType(String memoofEvidenceContentType) {
        this.memoofEvidenceContentType = memoofEvidenceContentType;
    }

    public String getDescriptionOfDocumentContentType() {
        return descriptionOfDocumentContentType;
    }

    public void setDescriptionOfDocumentContentType(String descriptionOfDocumentContentType) {
        this.descriptionOfDocumentContentType = descriptionOfDocumentContentType;
    }

    public String getDocumentTypeName() {
        return documentTypeName;
    }

    public void setDocumentTypeName(String documentTypeName) {
        this.documentTypeName = documentTypeName;
    }

}
