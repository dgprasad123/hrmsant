/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.policemodule;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Surendra
 */
public class AwardMedalListForm {

    private String awardMedalTypeId = "";
    private String awardMedalName = "";
    private String awardYear = "";

    private String rewardMedalId = "";
    private String empId = "";
    private String gpfNo = "";
    private String fullname = "";
    private String fathername = "";
    private String gender = "";

    private String initialAppointYear = "";
    private String initialAppointRank = "";
    private String initialAppointCadre = "";

    private String designation = "";
    private String datePresentRank = "";

    private String awardMedalYear = "";
    private String dob = "";
    private int ageInYear = 0;
    private int ageInMonth = 0;

    private String doa = "";

    private String presentPosting = "";
    private String postingAddress = "";

    private MultipartFile serviceBookCopy = null;
    private String originalFileNameSB = "";
    private String diskFilenameSB = "";

    private MultipartFile discDocument = null;
    private String originalFileName = "";
    private String diskFilename = "";
    private String discDetails = "";
    private String dpcifany = "";

    private MultipartFile discCCROll = null;
    private String originalFileNameCCROll = "";
    private String diskFilenameCCROll = "";

    private MultipartFile srDocument = null;
    private String originalFileNamesrDocument = "";
    private String diskFilenamesrDocument = "";

    private MultipartFile nonSrDocument = null;
    private String originalFileNamenonSrDocument = "";
    private String diskFilenamenonSrDocument = "";

    private MultipartFile certificateDoc = null;
    private String originalFileNamecertificateDoc = "";
    private String diskFilenamecertificateDoc = "";

    private String srDocumentDetails = "";
    private String nonSrDocumentDetails = "";

    private String offCode = "";
    private String offName = "";
    private String sbAttachment = "";
    private String docPresentRank = "";
    private String dojDistEst = "";
    private String moneyReward = "";
    private String commendation = "";
    private String gsMark = "";
    private String appreciation = "";
    private String aar = "";
    private String otherReward = "";

    private String punishmentMajor = "";
    private String punishmentMinor = "";
    private String punishmentDetails = "";
    private String punishmentYears = "";
    private String punishmentMajorDetails = "";
    private String punishmentMinorDetails = "";
    
    private String punishmentMajorpreeciding = "";
    private String punishmentMinorpreeciding = "";
    private String punishmentMajorpreecidingDetails = "";
    private String punishmentMinorpreecidingDetails = "";

    private String pendingEnquiryDetails = "";

    private String prevAwardCmb = "";
    private String awardMedalPreviousYear = "";
    private String awardMedalRank = "";
    private String awardMedalPostingPlace = "";
    private String proceedingAttachment = "";

    private String courtCaseYear = "";
    private String courtCaseDetails = "";
    private String courtCaseStatus = "";

    private String briefNote = "";
    private String ccrAttachment = "";

    private String acrYear1 = "";
    private String acrYear1Grading = "";
    private String acrYear2 = "";
    private String acrYear2Grading = "";
    private String acrYear3 = "";
    private String acrYear3Grading = "";
    private String acrYear4 = "";
    private String acrYear4Grading = "";
    private String acrYear5 = "";
    private String acrYear5Grading = "";

    private String recommendStatusofDist = "";
    private String recommendStatusofRange = "";
    private String reasonFornotRecommend = "";
    private String furtherInfoByRange = "";
    private String completedStatus = "";
    private String submittedRangeOff = "";
    private String submittedOn = "";
    private String rangeSubmittedOn = "";
    private String submitted2DgOffice = "";
    private String sltActionName = "";

    private String sltCategory;
    private String initialAppointDate;
    private String initialAppointDate1;
    private String initialAppointService;
    private String initialAppointCategory;
    private String differentRank;
    private String differentDate;

    private String totalpoliceyears;
    private String totalpolicemonths;
    private String totalpolicedays;
    private String sltDeputation;

    private String deputationDate;

    private String rewardsTotalNo;
    private String rewardsAmt;
    private String cashAwardsNo;
    private String goodServiceEntries;
    private String anyOtherRewards;
    private String meritoriousYear;
    private String sltOccasion;
    private String approvMeritoriousServiceAwarded;
    private String penaltydetails;
    private String penaltyyear;
    private String penaltyOrderNo;
    private String penaltyOrderDate;
    
    private String minorpenaltydetails;
    private String minorpenaltyyear;
    private String minorpenaltyOrderNo;
    private String minorpenaltyOrderDate;
    
    private String medicalCategory;
    private String ifenquirypending;
    private String enquirypending;

    private String dpcyear;
    private String dpcnatureallegation;
    private String dpcpresentstatus;

    private String recommendeeGPFNo;

    private String email;
    private String mobile;
    private String briefdescription;

    private String totaldifferentrankyears;
    private String totaldifferentrankmonths;
    private String totaldifferentrankdays;

    private MultipartFile integrityCertificateDoc;
    private String originalFileNameintegrityCertificateDoc = "";
    private String diskFilenameintegrityCertificateDoc = "";

    private MultipartFile medicalCertificateDoc;
    private String originalFileNamemedicalcertificateDoc = "";
    private String diskFilenamemedicalcertificateDoc = "";

    private String presentPostingPlace;
    private String presentPostingPIN;
    private String presentPostingDate;

    private String firstName;
    private String middleName;
    private String lastName;

    private String fatherfirstname;
    private String fathermiddlename;
    private String fatherlastname;

    private String courtCasePendingYear;
    private String courtCasePendingDetails;
    private String courtCasePendingStatus;

    private String acrGrading1;
    private String acrGrading2;
    private String acrGrading3;
    private String acrGrading4;
    private String acrGrading5;
    private String acrGrading6;
    private String acrGrading7;
    private String acrGrading8;
    private String acrGrading9;
    private String acrGrading10;
    private String acrGrading11;
    private String acrGrading12;
    private String acrGrading13;
    private String acrGrading14;

    private String acrGradingOS;
    private String acrGradingVS;
    private String acrGradingGood;
    private String acrGradingAvg;
    private String acrGradingNic;
    private String acrGradingAdv;
    private String acrGradingMs;
    private String acrGradingNa;

    private String presidentcommendation;
    private String presidentappreciation;

    private MultipartFile punishmentDoc;
    private String originalFileNamepunishmentDoc = "";
    private String diskFilenamepunishmentDoc = "";

    private MultipartFile enquiryDoc;
    private String originalFileNameenquiryDoc = "";
    private String diskFilenameenquiryDoc = "";

    private MultipartFile dpcDoc;
    private String originalFileNamedpcDoc = "";
    private String diskFilenamedpcDoc = "";

    private MultipartFile courtCaseDoc;
    private String originalFileNamecourtCaseDoc = "";
    private String diskFilenamecourtCaseDoc = "";

    private List differentRankList;

    private String cashAwardsAmt;
    private String presidentcommendationAmt;
    private String presidentappreciationAmt;
    private String goodServiceEntriesAmt;
    private String anyOtherRewardsDesc;

    private MultipartFile acrGrading1Doc;
    private String originalFileNameAcrGrading1Doc = "";
    private String diskFilenameAcrGrading1Doc = "";

    private MultipartFile acrGrading2Doc;
    private String originalFileNameAcrGrading2Doc = "";
    private String diskFilenameAcrGrading2Doc = "";

    private MultipartFile acrGrading3Doc;
    private String originalFileNameAcrGrading3Doc = "";
    private String diskFilenameAcrGrading3Doc = "";

    private MultipartFile acrGrading4Doc;
    private String originalFileNameAcrGrading4Doc = "";
    private String diskFilenameAcrGrading4Doc = "";

    private MultipartFile acrGrading5Doc;
    private String originalFileNameAcrGrading5Doc = "";
    private String diskFilenameAcrGrading5Doc = "";

    private MultipartFile acrGrading6Doc;
    private String originalFileNameAcrGrading6Doc = "";
    private String diskFilenameAcrGrading6Doc = "";

    private MultipartFile acrGrading7Doc;
    private String originalFileNameAcrGrading7Doc = "";
    private String diskFilenameAcrGrading7Doc = "";

    private MultipartFile acrGrading8Doc;
    private String originalFileNameAcrGrading8Doc = "";
    private String diskFilenameAcrGrading8Doc = "";

    private MultipartFile acrGrading9Doc;
    private String originalFileNameAcrGrading9Doc = "";
    private String diskFilenameAcrGrading9Doc = "";

    private MultipartFile acrGrading10Doc;
    private String originalFileNameAcrGrading10Doc = "";
    private String diskFilenameAcrGrading10Doc = "";

    private MultipartFile acrGrading11Doc;
    private String originalFileNameAcrGrading11Doc = "";
    private String diskFilenameAcrGrading11Doc = "";

    private MultipartFile acrGrading12Doc;
    private String originalFileNameAcrGrading12Doc = "";
    private String diskFilenameAcrGrading12Doc = "";

    private MultipartFile acrGrading13Doc;
    private String originalFileNameAcrGrading13Doc = "";
    private String diskFilenameAcrGrading13Doc = "";

    private String sltAwardOccasion;

    private MultipartFile discCCRollone = null;
    private String originalFileNameCCRollone = "";
    private String diskFilenameCCRollone = "";

    private MultipartFile discCCRolltwo = null;
    private String originalFileNameCCRolltwo = "";
    private String diskFilenameCCRolltwo = "";

    private MultipartFile discCCRollthree = null;
    private String originalFileNameCCRollthree = "";
    private String diskFilenameCCRollthree = "";

    private String ccrolloneremarks;
    private String ccrolltworemarks;
    private String ccrollthreeremarks;

    private String srCaseId;
    private String srCaseNo;
    private String regDate;
    private String frDate;
    private String dtlsTeamUse;
    private String noOfExhibits;
    private String convictionDtls;
    private String crimeProceedAttach = null;

    private String invstCaseId;
    private String psName;
    private String invstCaseNo;
    private String invstDateReg;
    private String invstCsFr;
    private String invstFinalSubDate;
    private String invstSrNonsr;
    private String invstBriefCase;
    private String invstInnovMethods;
    private String invstScientificAids;
    private String invstScientificEvd;
    private String invstPromptness;
    private String invstAttachConfis;
    private String invstChallenges;
    private String invstConvcDtls;
    private MultipartFile judgementCopy;
    private String originalJudgementCopy = "";
    private String diskJudgementCopy = "";
    private MultipartFile medicalCategoryDoc = null;
    private String originalFileNameMedicalCategoryDoc = "";
    private String diskFilenameMedicalCategoryDoc = "";

    private String nonSrcaseId;
    private String nonSrcaseType;
    private String noOfNonSrcaseInvst2018;
    private String noFinalizedInThirtydays2018;
    private String noFinalizedAfterThirtydays2018;
    private String noStillPending2018;

    private String noOfNonSrcaseInvst2019;
    private String noFinalizedInThirtydays2019;
    private String noFinalizedAfterThirtydays2019;
    private String noStillPending2019;

    private String noOfNonSrcaseInvst2020;
    private String noFinalizedInThirtydays2020;
    private String noFinalizedAfterThirtydays2020;
    private String noStillPending2020;

    private String noOfSrcaseInvst2018;
    private String noOfSrcaseInvst2019;
    private String noOfSrcaseInvst2020;

    private String noSrFinalizedInThirtydays2018;
    private String noSrFinalizedInThirtydays2019;
    private String noSrFinalizedInThirtydays2020;

    private String noSrFinalizedAfterThirtydays2018;
    private String noSrFinalizedAfterThirtydays2019;
    private String noSrFinalizedAfterThirtydays2020;

    private String noSrStillPending2018;
    private String noSrStillPending2019;
    private String noSrStillPending2020;

    private String approvGovernorMedalAwarded;
    private String NotapprovGovernorMedalAwarded;
    private String ifdisciplinaryProceedingpending;
    private String disciplinaryProceedingpending;
    private String otherInformationofRange;
    private String presentPostingRankandDesignation;
    private String totalGovernoryears;
    private String totalGovernormonths;
    private String totalGovernordays;

    private MultipartFile performaBDoc;
    private String originalFileNameperformaBDoc = "";
    private String diskFilenameperformaBDoc = "";

    private MultipartFile performaCDoc;
    private String originalFileNameperformaCDoc = "";
    private String diskFilenameperformaCDoc = "";

    private String punishmentmajor1;
    private String punishmentminor1;
    private String punishmentmajyear1;
    private String punishmentminyear1;
    private String punishmentmajor2;
    private String punishmentminor2;
    private String punishmentmajyear2;
    private String punishmentminyear2;
    private String punishmentmajor3;
    private String punishmentminor3;
    private String punishmentmajyear3;
    private String punishmentminyear3;
    private String punishmentmajor4;
    private String punishmentminor4;
    private String punishmentmajyear4;
    private String punishmentminyear4;
    private String punishmentmajor5;
    private String punishmentminor5;
    private String punishmentmajyear5;
    private String punishmentminyear5;
    private String courtcaseifany;
    private String criminalcaseifany;
    private String annexure2Details;
    private String chargesheetedifany;
    private String annexure3Details;
    private String pendingEnquiryDetails1;

    private MultipartFile proceedingDocument = null;
    private String originalFileNameProccdingDocument = "";
    private String diskFilenameProccdingDocument = "";
    private String meetingProceeding = "";
    private String proceedingDetails = "";
    
    private String selScientificTool;
    private String selInvstInnovMethods;
    private String selInvstAttachConfis;
    private String selInvstScientificEvd;
    
    private String otherInformationFromDistrictDgps;
    private String diskFilenameMinorPunishment;
    private String originalFileNameMajorPunishment;
    private MultipartFile MajorPunishmentDocument;
    private String diskFilenameMajorPunishment;
    private String originalFileNameMinorPunishment;
    private MultipartFile MinorPunishmentDocument;
    
    
    private String diskFilenameMinorPunishmentpreeciding;
    private String originalFileNameMajorPunishmentpreeciding;
    private MultipartFile majorPunishmentpreecidingDocument;
    private String diskFilenameMajorPunishmentpreeciding;
    private String originalFileNameMinorPunishmentpreeciding;
    private MultipartFile minorPunishmentpreecidingDocument;
    
    private String deputationDetail;
    
    private String ifMajorPunishment;
    private String ifMinorPunishment;
    private String penaltydetailsMinor;
    private String penaltydetailsMojor;
    
    private String religious;
    private String category;
    private String isPhotoAvailable;
    private String ccrRefenrence;
    
    private String propertyStatementSubmittedifAny;
    private String dateofPropertySubmittedByOfficer;
    private String dateofPropertySubmittedByHRMS;
    private String punishmentMajordgp;
     private String punishmentMinordgp;
     
     private String country;
     private String nationality;
     private String state;
     private String acrGradingDetail;
     private String acrCopyifAny;
    
    

    public String getSelScientificTool() {
        return selScientificTool;
    }

    public void setSelScientificTool(String selScientificTool) {
        this.selScientificTool = selScientificTool;
    }

    public String getSelInvstInnovMethods() {
        return selInvstInnovMethods;
    }

    public void setSelInvstInnovMethods(String selInvstInnovMethods) {
        this.selInvstInnovMethods = selInvstInnovMethods;
    }

    public String getSelInvstAttachConfis() {
        return selInvstAttachConfis;
    }

    public void setSelInvstAttachConfis(String selInvstAttachConfis) {
        this.selInvstAttachConfis = selInvstAttachConfis;
    }

    public String getSelInvstScientificEvd() {
        return selInvstScientificEvd;
    }

    public void setSelInvstScientificEvd(String selInvstScientificEvd) {
        this.selInvstScientificEvd = selInvstScientificEvd;
    }
    
    

    public String getNoOfSrcaseInvst2018() {
        return noOfSrcaseInvst2018;
    }

    public void setNoOfSrcaseInvst2018(String noOfSrcaseInvst2018) {
        this.noOfSrcaseInvst2018 = noOfSrcaseInvst2018;
    }

    public String getNoOfSrcaseInvst2019() {
        return noOfSrcaseInvst2019;
    }

    public void setNoOfSrcaseInvst2019(String noOfSrcaseInvst2019) {
        this.noOfSrcaseInvst2019 = noOfSrcaseInvst2019;
    }

    public String getNoOfSrcaseInvst2020() {
        return noOfSrcaseInvst2020;
    }

    public void setNoOfSrcaseInvst2020(String noOfSrcaseInvst2020) {
        this.noOfSrcaseInvst2020 = noOfSrcaseInvst2020;
    }

    public String getNoSrFinalizedInThirtydays2018() {
        return noSrFinalizedInThirtydays2018;
    }

    public void setNoSrFinalizedInThirtydays2018(String noSrFinalizedInThirtydays2018) {
        this.noSrFinalizedInThirtydays2018 = noSrFinalizedInThirtydays2018;
    }

    public String getNoSrFinalizedInThirtydays2019() {
        return noSrFinalizedInThirtydays2019;
    }

    public void setNoSrFinalizedInThirtydays2019(String noSrFinalizedInThirtydays2019) {
        this.noSrFinalizedInThirtydays2019 = noSrFinalizedInThirtydays2019;
    }

    public String getNoSrFinalizedInThirtydays2020() {
        return noSrFinalizedInThirtydays2020;
    }

    public void setNoSrFinalizedInThirtydays2020(String noSrFinalizedInThirtydays2020) {
        this.noSrFinalizedInThirtydays2020 = noSrFinalizedInThirtydays2020;
    }

    public String getNoSrFinalizedAfterThirtydays2018() {
        return noSrFinalizedAfterThirtydays2018;
    }

    public void setNoSrFinalizedAfterThirtydays2018(String noSrFinalizedAfterThirtydays2018) {
        this.noSrFinalizedAfterThirtydays2018 = noSrFinalizedAfterThirtydays2018;
    }

    public String getNoSrFinalizedAfterThirtydays2019() {
        return noSrFinalizedAfterThirtydays2019;
    }

    public void setNoSrFinalizedAfterThirtydays2019(String noSrFinalizedAfterThirtydays2019) {
        this.noSrFinalizedAfterThirtydays2019 = noSrFinalizedAfterThirtydays2019;
    }

    public String getNoSrFinalizedAfterThirtydays2020() {
        return noSrFinalizedAfterThirtydays2020;
    }

    public void setNoSrFinalizedAfterThirtydays2020(String noSrFinalizedAfterThirtydays2020) {
        this.noSrFinalizedAfterThirtydays2020 = noSrFinalizedAfterThirtydays2020;
    }

    public String getNoSrStillPending2018() {
        return noSrStillPending2018;
    }

    public void setNoSrStillPending2018(String noSrStillPending2018) {
        this.noSrStillPending2018 = noSrStillPending2018;
    }

    public String getNoSrStillPending2019() {
        return noSrStillPending2019;
    }

    public void setNoSrStillPending2019(String noSrStillPending2019) {
        this.noSrStillPending2019 = noSrStillPending2019;
    }

    public String getNoSrStillPending2020() {
        return noSrStillPending2020;
    }

    public void setNoSrStillPending2020(String noSrStillPending2020) {
        this.noSrStillPending2020 = noSrStillPending2020;
    }
    
    

    public String getProceedingDetails() {
        return proceedingDetails;
    }

    public void setProceedingDetails(String proceedingDetails) {
        this.proceedingDetails = proceedingDetails;
    }

    public MultipartFile getProceedingDocument() {
        return proceedingDocument;
    }

    public void setProceedingDocument(MultipartFile proceedingDocument) {
        this.proceedingDocument = proceedingDocument;
    }

    public String getOriginalFileNameProccdingDocument() {
        return originalFileNameProccdingDocument;
    }

    public void setOriginalFileNameProccdingDocument(String originalFileNameProccdingDocument) {
        this.originalFileNameProccdingDocument = originalFileNameProccdingDocument;
    }

    public String getDiskFilenameProccdingDocument() {
        return diskFilenameProccdingDocument;
    }

    public void setDiskFilenameProccdingDocument(String diskFilenameProccdingDocument) {
        this.diskFilenameProccdingDocument = diskFilenameProccdingDocument;
    }

    public String getMeetingProceeding() {
        return meetingProceeding;
    }

    public void setMeetingProceeding(String meetingProceeding) {
        this.meetingProceeding = meetingProceeding;
    }

    public String getApprovGovernorMedalAwarded() {
        return approvGovernorMedalAwarded;
    }

    public void setApprovGovernorMedalAwarded(String approvGovernorMedalAwarded) {
        this.approvGovernorMedalAwarded = approvGovernorMedalAwarded;
    }

    public String getNotapprovGovernorMedalAwarded() {
        return NotapprovGovernorMedalAwarded;
    }

    public void setNotapprovGovernorMedalAwarded(String NotapprovGovernorMedalAwarded) {
        this.NotapprovGovernorMedalAwarded = NotapprovGovernorMedalAwarded;
    }

    public String getIfdisciplinaryProceedingpending() {
        return ifdisciplinaryProceedingpending;
    }

    public void setIfdisciplinaryProceedingpending(String ifdisciplinaryProceedingpending) {
        this.ifdisciplinaryProceedingpending = ifdisciplinaryProceedingpending;
    }

    public String getDisciplinaryProceedingpending() {
        return disciplinaryProceedingpending;
    }

    public void setDisciplinaryProceedingpending(String disciplinaryProceedingpending) {
        this.disciplinaryProceedingpending = disciplinaryProceedingpending;
    }

    public String getOtherInformationofRange() {
        return otherInformationofRange;
    }

    public void setOtherInformationofRange(String otherInformationofRange) {
        this.otherInformationofRange = otherInformationofRange;
    }

    public String getPresentPostingRankandDesignation() {
        return presentPostingRankandDesignation;
    }

    public void setPresentPostingRankandDesignation(String presentPostingRankandDesignation) {
        this.presentPostingRankandDesignation = presentPostingRankandDesignation;
    }

    public String getTotalGovernoryears() {
        return totalGovernoryears;
    }

    public void setTotalGovernoryears(String totalGovernoryears) {
        this.totalGovernoryears = totalGovernoryears;
    }

    public String getTotalGovernormonths() {
        return totalGovernormonths;
    }

    public void setTotalGovernormonths(String totalGovernormonths) {
        this.totalGovernormonths = totalGovernormonths;
    }

    public String getTotalGovernordays() {
        return totalGovernordays;
    }

    public void setTotalGovernordays(String totalGovernordays) {
        this.totalGovernordays = totalGovernordays;
    }

    public MultipartFile getPerformaBDoc() {
        return performaBDoc;
    }

    public void setPerformaBDoc(MultipartFile performaBDoc) {
        this.performaBDoc = performaBDoc;
    }

    public String getOriginalFileNameperformaBDoc() {
        return originalFileNameperformaBDoc;
    }

    public void setOriginalFileNameperformaBDoc(String originalFileNameperformaBDoc) {
        this.originalFileNameperformaBDoc = originalFileNameperformaBDoc;
    }

    public String getDiskFilenameperformaBDoc() {
        return diskFilenameperformaBDoc;
    }

    public void setDiskFilenameperformaBDoc(String diskFilenameperformaBDoc) {
        this.diskFilenameperformaBDoc = diskFilenameperformaBDoc;
    }

    public MultipartFile getPerformaCDoc() {
        return performaCDoc;
    }

    public void setPerformaCDoc(MultipartFile performaCDoc) {
        this.performaCDoc = performaCDoc;
    }

    public String getOriginalFileNameperformaCDoc() {
        return originalFileNameperformaCDoc;
    }

    public void setOriginalFileNameperformaCDoc(String originalFileNameperformaCDoc) {
        this.originalFileNameperformaCDoc = originalFileNameperformaCDoc;
    }

    public String getDiskFilenameperformaCDoc() {
        return diskFilenameperformaCDoc;
    }

    public void setDiskFilenameperformaCDoc(String diskFilenameperformaCDoc) {
        this.diskFilenameperformaCDoc = diskFilenameperformaCDoc;
    }

    public String getPendingEnquiryDetails1() {
        return pendingEnquiryDetails1;
    }

    public void setPendingEnquiryDetails1(String pendingEnquiryDetails1) {
        this.pendingEnquiryDetails1 = pendingEnquiryDetails1;
    }

    public String getCourtcaseifany() {
        return courtcaseifany;
    }

    public void setCourtcaseifany(String courtcaseifany) {
        this.courtcaseifany = courtcaseifany;
    }

    public String getNonSrcaseId() {
        return nonSrcaseId;
    }

    public void setNonSrcaseId(String nonSrcaseId) {
        this.nonSrcaseId = nonSrcaseId;
    }

    public String getNonSrcaseType() {
        return nonSrcaseType;
    }

    public void setNonSrcaseType(String nonSrcaseType) {
        this.nonSrcaseType = nonSrcaseType;
    }

    public String getNoOfNonSrcaseInvst2018() {
        return noOfNonSrcaseInvst2018;
    }

    public void setNoOfNonSrcaseInvst2018(String noOfNonSrcaseInvst2018) {
        this.noOfNonSrcaseInvst2018 = noOfNonSrcaseInvst2018;
    }

    public String getNoFinalizedInThirtydays2018() {
        return noFinalizedInThirtydays2018;
    }

    public void setNoFinalizedInThirtydays2018(String noFinalizedInThirtydays2018) {
        this.noFinalizedInThirtydays2018 = noFinalizedInThirtydays2018;
    }

    public String getNoFinalizedAfterThirtydays2018() {
        return noFinalizedAfterThirtydays2018;
    }

    public void setNoFinalizedAfterThirtydays2018(String noFinalizedAfterThirtydays2018) {
        this.noFinalizedAfterThirtydays2018 = noFinalizedAfterThirtydays2018;
    }

    public String getNoStillPending2018() {
        return noStillPending2018;
    }

    public void setNoStillPending2018(String noStillPending2018) {
        this.noStillPending2018 = noStillPending2018;
    }

    public String getNoOfNonSrcaseInvst2019() {
        return noOfNonSrcaseInvst2019;
    }

    public void setNoOfNonSrcaseInvst2019(String noOfNonSrcaseInvst2019) {
        this.noOfNonSrcaseInvst2019 = noOfNonSrcaseInvst2019;
    }

    public String getNoFinalizedInThirtydays2019() {
        return noFinalizedInThirtydays2019;
    }

    public void setNoFinalizedInThirtydays2019(String noFinalizedInThirtydays2019) {
        this.noFinalizedInThirtydays2019 = noFinalizedInThirtydays2019;
    }

    public String getNoFinalizedAfterThirtydays2019() {
        return noFinalizedAfterThirtydays2019;
    }

    public void setNoFinalizedAfterThirtydays2019(String noFinalizedAfterThirtydays2019) {
        this.noFinalizedAfterThirtydays2019 = noFinalizedAfterThirtydays2019;
    }

    public String getNoStillPending2019() {
        return noStillPending2019;
    }

    public void setNoStillPending2019(String noStillPending2019) {
        this.noStillPending2019 = noStillPending2019;
    }

    public String getNoOfNonSrcaseInvst2020() {
        return noOfNonSrcaseInvst2020;
    }

    public void setNoOfNonSrcaseInvst2020(String noOfNonSrcaseInvst2020) {
        this.noOfNonSrcaseInvst2020 = noOfNonSrcaseInvst2020;
    }

    public String getNoFinalizedInThirtydays2020() {
        return noFinalizedInThirtydays2020;
    }

    public void setNoFinalizedInThirtydays2020(String noFinalizedInThirtydays2020) {
        this.noFinalizedInThirtydays2020 = noFinalizedInThirtydays2020;
    }

    public String getNoFinalizedAfterThirtydays2020() {
        return noFinalizedAfterThirtydays2020;
    }

    public void setNoFinalizedAfterThirtydays2020(String noFinalizedAfterThirtydays2020) {
        this.noFinalizedAfterThirtydays2020 = noFinalizedAfterThirtydays2020;
    }

    public String getNoStillPending2020() {
        return noStillPending2020;
    }

    public void setNoStillPending2020(String noStillPending2020) {
        this.noStillPending2020 = noStillPending2020;
    }

    public String getAwardMedalName() {
        return awardMedalName;
    }

    public void setAwardMedalName(String awardMedalName) {
        this.awardMedalName = awardMedalName;
    }

    public String getAwardYear() {
        return awardYear;
    }

    public void setAwardYear(String awardYear) {
        this.awardYear = awardYear;
    }

    public String getRewardMedalId() {
        return rewardMedalId;
    }

    public void setRewardMedalId(String rewardMedalId) {
        this.rewardMedalId = rewardMedalId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getAwardMedalYear() {
        return awardMedalYear;
    }

    public void setAwardMedalYear(String awardMedalYear) {
        this.awardMedalYear = awardMedalYear;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
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

    public String getSbAttachment() {
        return sbAttachment;
    }

    public void setSbAttachment(String sbAttachment) {
        this.sbAttachment = sbAttachment;
    }

    public String getDocPresentRank() {
        return docPresentRank;
    }

    public void setDocPresentRank(String docPresentRank) {
        this.docPresentRank = docPresentRank;
    }

    public String getDojDistEst() {
        return dojDistEst;
    }

    public void setDojDistEst(String dojDistEst) {
        this.dojDistEst = dojDistEst;
    }

    public String getMoneyReward() {
        return moneyReward;
    }

    public void setMoneyReward(String moneyReward) {
        this.moneyReward = moneyReward;
    }

    public String getCommendation() {
        return commendation;
    }

    public void setCommendation(String commendation) {
        this.commendation = commendation;
    }

    public String getGsMark() {
        return gsMark;
    }

    public void setGsMark(String gsMark) {
        this.gsMark = gsMark;
    }

    public String getAppreciation() {
        return appreciation;
    }

    public void setAppreciation(String appreciation) {
        this.appreciation = appreciation;
    }

    public String getAar() {
        return aar;
    }

    public void setAar(String aar) {
        this.aar = aar;
    }

    public String getPunishmentMajor() {
        return punishmentMajor;
    }

    public void setPunishmentMajor(String punishmentMajor) {
        this.punishmentMajor = punishmentMajor;
    }

    public String getPunishmentMinor() {
        return punishmentMinor;
    }

    public void setPunishmentMinor(String punishmentMinor) {
        this.punishmentMinor = punishmentMinor;
    }

    public String getAwardMedalPreviousYear() {
        return awardMedalPreviousYear;
    }

    public void setAwardMedalPreviousYear(String awardMedalPreviousYear) {
        this.awardMedalPreviousYear = awardMedalPreviousYear;
    }

    public String getAwardMedalRank() {
        return awardMedalRank;
    }

    public void setAwardMedalRank(String awardMedalRank) {
        this.awardMedalRank = awardMedalRank;
    }

    public String getAwardMedalPostingPlace() {
        return awardMedalPostingPlace;
    }

    public void setAwardMedalPostingPlace(String awardMedalPostingPlace) {
        this.awardMedalPostingPlace = awardMedalPostingPlace;
    }

    public String getProceedingAttachment() {
        return proceedingAttachment;
    }

    public void setProceedingAttachment(String proceedingAttachment) {
        this.proceedingAttachment = proceedingAttachment;
    }

    public String getBriefNote() {
        return briefNote;
    }

    public void setBriefNote(String briefNote) {
        this.briefNote = briefNote;
    }

    public String getCcrAttachment() {
        return ccrAttachment;
    }

    public void setCcrAttachment(String ccrAttachment) {
        this.ccrAttachment = ccrAttachment;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGpfNo() {
        return gpfNo;
    }

    public void setGpfNo(String gpfNo) {
        this.gpfNo = gpfNo;
    }

    public String getAwardMedalTypeId() {
        return awardMedalTypeId;
    }

    public void setAwardMedalTypeId(String awardMedalTypeId) {
        this.awardMedalTypeId = awardMedalTypeId;
    }

    public MultipartFile getServiceBookCopy() {
        return serviceBookCopy;
    }

    public void setServiceBookCopy(MultipartFile serviceBookCopy) {
        this.serviceBookCopy = serviceBookCopy;
    }

    public String getOriginalFileNameSB() {
        return originalFileNameSB;
    }

    public void setOriginalFileNameSB(String originalFileNameSB) {
        this.originalFileNameSB = originalFileNameSB;
    }

    public String getDiskFilenameSB() {
        return diskFilenameSB;
    }

    public void setDiskFilenameSB(String diskFilenameSB) {
        this.diskFilenameSB = diskFilenameSB;
    }

    public MultipartFile getDiscDocument() {
        return discDocument;
    }

    public void setDiscDocument(MultipartFile discDocument) {
        this.discDocument = discDocument;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getDiskFilename() {
        return diskFilename;
    }

    public void setDiskFilename(String diskFilename) {
        this.diskFilename = diskFilename;
    }

    public MultipartFile getDiscCCROll() {
        return discCCROll;
    }

    public void setDiscCCROll(MultipartFile discCCROll) {
        this.discCCROll = discCCROll;
    }

    public String getOriginalFileNameCCROll() {
        return originalFileNameCCROll;
    }

    public void setOriginalFileNameCCROll(String originalFileNameCCROll) {
        this.originalFileNameCCROll = originalFileNameCCROll;
    }

    public String getDiskFilenameCCROll() {
        return diskFilenameCCROll;
    }

    public void setDiskFilenameCCROll(String diskFilenameCCROll) {
        this.diskFilenameCCROll = diskFilenameCCROll;
    }

    public String getPrevAwardCmb() {
        return prevAwardCmb;
    }

    public void setPrevAwardCmb(String prevAwardCmb) {
        this.prevAwardCmb = prevAwardCmb;
    }

    public String getDiscDetails() {
        return discDetails;
    }

    public void setDiscDetails(String discDetails) {
        this.discDetails = discDetails;
    }

    public String getDpcifany() {
        return dpcifany;
    }

    public void setDpcifany(String dpcifany) {
        this.dpcifany = dpcifany;
    }

    public String getRecommendStatusofDist() {
        return recommendStatusofDist;
    }

    public void setRecommendStatusofDist(String recommendStatusofDist) {
        this.recommendStatusofDist = recommendStatusofDist;
    }

    public String getRecommendStatusofRange() {
        return recommendStatusofRange;
    }

    public void setRecommendStatusofRange(String recommendStatusofRange) {
        this.recommendStatusofRange = recommendStatusofRange;
    }

    public String getReasonFornotRecommend() {
        return reasonFornotRecommend;
    }

    public void setReasonFornotRecommend(String reasonFornotRecommend) {
        this.reasonFornotRecommend = reasonFornotRecommend;
    }

    public String getCompletedStatus() {
        return completedStatus;
    }

    public void setCompletedStatus(String completedStatus) {
        this.completedStatus = completedStatus;
    }

    public String getSubmittedRangeOff() {
        return submittedRangeOff;
    }

    public void setSubmittedRangeOff(String submittedRangeOff) {
        this.submittedRangeOff = submittedRangeOff;
    }

    public String getSubmittedOn() {
        return submittedOn;
    }

    public void setSubmittedOn(String submittedOn) {
        this.submittedOn = submittedOn;
    }

    public String getRangeSubmittedOn() {
        return rangeSubmittedOn;
    }

    public void setRangeSubmittedOn(String rangeSubmittedOn) {
        this.rangeSubmittedOn = rangeSubmittedOn;
    }

    public String getSubmitted2DgOffice() {
        return submitted2DgOffice;
    }

    public void setSubmitted2DgOffice(String submitted2DgOffice) {
        this.submitted2DgOffice = submitted2DgOffice;
    }

    public String getSltActionName() {
        return sltActionName;
    }

    public void setSltActionName(String sltActionName) {
        this.sltActionName = sltActionName;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getInitialAppointYear() {
        return initialAppointYear;
    }

    public void setInitialAppointYear(String initialAppointYear) {
        this.initialAppointYear = initialAppointYear;
    }

    public String getInitialAppointRank() {
        return initialAppointRank;
    }

    public void setInitialAppointRank(String initialAppointRank) {
        this.initialAppointRank = initialAppointRank;
    }

    public String getInitialAppointCadre() {
        return initialAppointCadre;
    }

    public void setInitialAppointCadre(String initialAppointCadre) {
        this.initialAppointCadre = initialAppointCadre;
    }

    public String getDatePresentRank() {
        return datePresentRank;
    }

    public void setDatePresentRank(String datePresentRank) {
        this.datePresentRank = datePresentRank;
    }

    public int getAgeInYear() {
        return ageInYear;
    }

    public void setAgeInYear(int ageInYear) {
        this.ageInYear = ageInYear;
    }

    public int getAgeInMonth() {
        return ageInMonth;
    }

    public void setAgeInMonth(int ageInMonth) {
        this.ageInMonth = ageInMonth;
    }

    public String getPresentPosting() {
        return presentPosting;
    }

    public void setPresentPosting(String presentPosting) {
        this.presentPosting = presentPosting;
    }

    public String getPostingAddress() {
        return postingAddress;
    }

    public void setPostingAddress(String postingAddress) {
        this.postingAddress = postingAddress;
    }

    public String getOtherReward() {
        return otherReward;
    }

    public void setOtherReward(String otherReward) {
        this.otherReward = otherReward;
    }

    public String getPunishmentDetails() {
        return punishmentDetails;
    }

    public void setPunishmentDetails(String punishmentDetails) {
        this.punishmentDetails = punishmentDetails;
    }

    public String getPunishmentYears() {
        return punishmentYears;
    }

    public void setPunishmentYears(String punishmentYears) {
        this.punishmentYears = punishmentYears;
    }

    public String getPunishmentMajorDetails() {
        return punishmentMajorDetails;
    }

    public void setPunishmentMajorDetails(String punishmentMajorDetails) {
        this.punishmentMajorDetails = punishmentMajorDetails;
    }

    public String getPunishmentMinorDetails() {
        return punishmentMinorDetails;
    }

    public void setPunishmentMinorDetails(String punishmentMinorDetails) {
        this.punishmentMinorDetails = punishmentMinorDetails;
    }

    public String getPunishmentMajorpreeciding() {
        return punishmentMajorpreeciding;
    }

    public void setPunishmentMajorpreeciding(String punishmentMajorpreeciding) {
        this.punishmentMajorpreeciding = punishmentMajorpreeciding;
    }

    public String getPunishmentMinorpreeciding() {
        return punishmentMinorpreeciding;
    }

    public void setPunishmentMinorpreeciding(String punishmentMinorpreeciding) {
        this.punishmentMinorpreeciding = punishmentMinorpreeciding;
    }

    public String getPunishmentMajorpreecidingDetails() {
        return punishmentMajorpreecidingDetails;
    }

    public void setPunishmentMajorpreecidingDetails(String punishmentMajorpreecidingDetails) {
        this.punishmentMajorpreecidingDetails = punishmentMajorpreecidingDetails;
    }

    public String getPunishmentMinorpreecidingDetails() {
        return punishmentMinorpreecidingDetails;
    }

    public void setPunishmentMinorpreecidingDetails(String punishmentMinorpreecidingDetails) {
        this.punishmentMinorpreecidingDetails = punishmentMinorpreecidingDetails;
    }
    

    public String getPendingEnquiryDetails() {
        return pendingEnquiryDetails;
    }

    public void setPendingEnquiryDetails(String pendingEnquiryDetails) {
        this.pendingEnquiryDetails = pendingEnquiryDetails;
    }

    public String getCourtCaseYear() {
        return courtCaseYear;
    }

    public void setCourtCaseYear(String courtCaseYear) {
        this.courtCaseYear = courtCaseYear;
    }

    public String getCourtCaseDetails() {
        return courtCaseDetails;
    }

    public void setCourtCaseDetails(String courtCaseDetails) {
        this.courtCaseDetails = courtCaseDetails;
    }

    public String getCourtCaseStatus() {
        return courtCaseStatus;
    }

    public void setCourtCaseStatus(String courtCaseStatus) {
        this.courtCaseStatus = courtCaseStatus;
    }

    public String getAcrYear1() {
        return acrYear1;
    }

    public void setAcrYear1(String acrYear1) {
        this.acrYear1 = acrYear1;
    }

    public String getAcrYear1Grading() {
        return acrYear1Grading;
    }

    public void setAcrYear1Grading(String acrYear1Grading) {
        this.acrYear1Grading = acrYear1Grading;
    }

    public String getAcrYear2() {
        return acrYear2;
    }

    public void setAcrYear2(String acrYear2) {
        this.acrYear2 = acrYear2;
    }

    public String getAcrYear2Grading() {
        return acrYear2Grading;
    }

    public void setAcrYear2Grading(String acrYear2Grading) {
        this.acrYear2Grading = acrYear2Grading;
    }

    public String getAcrYear3() {
        return acrYear3;
    }

    public void setAcrYear3(String acrYear3) {
        this.acrYear3 = acrYear3;
    }

    public String getAcrYear3Grading() {
        return acrYear3Grading;
    }

    public void setAcrYear3Grading(String acrYear3Grading) {
        this.acrYear3Grading = acrYear3Grading;
    }

    public String getAcrYear4() {
        return acrYear4;
    }

    public void setAcrYear4(String acrYear4) {
        this.acrYear4 = acrYear4;
    }

    public String getAcrYear4Grading() {
        return acrYear4Grading;
    }

    public void setAcrYear4Grading(String acrYear4Grading) {
        this.acrYear4Grading = acrYear4Grading;
    }

    public String getAcrYear5() {
        return acrYear5;
    }

    public void setAcrYear5(String acrYear5) {
        this.acrYear5 = acrYear5;
    }

    public String getAcrYear5Grading() {
        return acrYear5Grading;
    }

    public void setAcrYear5Grading(String acrYear5Grading) {
        this.acrYear5Grading = acrYear5Grading;
    }

    public String getFurtherInfoByRange() {
        return furtherInfoByRange;
    }

    public void setFurtherInfoByRange(String furtherInfoByRange) {
        this.furtherInfoByRange = furtherInfoByRange;
    }

    public MultipartFile getSrDocument() {
        return srDocument;
    }

    public void setSrDocument(MultipartFile srDocument) {
        this.srDocument = srDocument;
    }

    public String getOriginalFileNamesrDocument() {
        return originalFileNamesrDocument;
    }

    public void setOriginalFileNamesrDocument(String originalFileNamesrDocument) {
        this.originalFileNamesrDocument = originalFileNamesrDocument;
    }

    public String getDiskFilenamesrDocument() {
        return diskFilenamesrDocument;
    }

    public void setDiskFilenamesrDocument(String diskFilenamesrDocument) {
        this.diskFilenamesrDocument = diskFilenamesrDocument;
    }

    public MultipartFile getNonSrDocument() {
        return nonSrDocument;
    }

    public void setNonSrDocument(MultipartFile nonSrDocument) {
        this.nonSrDocument = nonSrDocument;
    }

    public String getOriginalFileNamenonSrDocument() {
        return originalFileNamenonSrDocument;
    }

    public void setOriginalFileNamenonSrDocument(String originalFileNamenonSrDocument) {
        this.originalFileNamenonSrDocument = originalFileNamenonSrDocument;
    }

    public String getDiskFilenamenonSrDocument() {
        return diskFilenamenonSrDocument;
    }

    public void setDiskFilenamenonSrDocument(String diskFilenamenonSrDocument) {
        this.diskFilenamenonSrDocument = diskFilenamenonSrDocument;
    }

    public MultipartFile getCertificateDoc() {
        return certificateDoc;
    }

    public void setCertificateDoc(MultipartFile certificateDoc) {
        this.certificateDoc = certificateDoc;
    }

    public String getOriginalFileNamecertificateDoc() {
        return originalFileNamecertificateDoc;
    }

    public void setOriginalFileNamecertificateDoc(String originalFileNamecertificateDoc) {
        this.originalFileNamecertificateDoc = originalFileNamecertificateDoc;
    }

    public String getDiskFilenamecertificateDoc() {
        return diskFilenamecertificateDoc;
    }

    public void setDiskFilenamecertificateDoc(String diskFilenamecertificateDoc) {
        this.diskFilenamecertificateDoc = diskFilenamecertificateDoc;
    }

    public String getSrDocumentDetails() {
        return srDocumentDetails;
    }

    public void setSrDocumentDetails(String srDocumentDetails) {
        this.srDocumentDetails = srDocumentDetails;
    }

    public String getNonSrDocumentDetails() {
        return nonSrDocumentDetails;
    }

    public void setNonSrDocumentDetails(String nonSrDocumentDetails) {
        this.nonSrDocumentDetails = nonSrDocumentDetails;
    }

    public String getSltCategory() {
        return sltCategory;
    }

    public void setSltCategory(String sltCategory) {
        this.sltCategory = sltCategory;
    }

    public String getInitialAppointDate() {
        return initialAppointDate;
    }

    public void setInitialAppointDate(String initialAppointDate) {
        this.initialAppointDate = initialAppointDate;
    }

    public String getInitialAppointDate1() {
        return initialAppointDate1;
    }

    public void setInitialAppointDate1(String initialAppointDate1) {
        this.initialAppointDate1 = initialAppointDate1;
    }

    public String getInitialAppointService() {
        return initialAppointService;
    }

    public void setInitialAppointService(String initialAppointService) {
        this.initialAppointService = initialAppointService;
    }

    public String getInitialAppointCategory() {
        return initialAppointCategory;
    }

    public void setInitialAppointCategory(String initialAppointCategory) {
        this.initialAppointCategory = initialAppointCategory;
    }

    public String getDifferentRank() {
        return differentRank;
    }

    public void setDifferentRank(String differentRank) {
        this.differentRank = differentRank;
    }

    public String getDifferentDate() {
        return differentDate;
    }

    public void setDifferentDate(String differentDate) {
        this.differentDate = differentDate;
    }

    public String getTotalpoliceyears() {
        return totalpoliceyears;
    }

    public void setTotalpoliceyears(String totalpoliceyears) {
        this.totalpoliceyears = totalpoliceyears;
    }

    public String getTotalpolicemonths() {
        return totalpolicemonths;
    }

    public void setTotalpolicemonths(String totalpolicemonths) {
        this.totalpolicemonths = totalpolicemonths;
    }

    public String getTotalpolicedays() {
        return totalpolicedays;
    }

    public void setTotalpolicedays(String totalpolicedays) {
        this.totalpolicedays = totalpolicedays;
    }

    public String getSltDeputation() {
        return sltDeputation;
    }

    public void setSltDeputation(String sltDeputation) {
        this.sltDeputation = sltDeputation;
    }

    public String getDeputationDate() {
        return deputationDate;
    }

    public void setDeputationDate(String deputationDate) {
        this.deputationDate = deputationDate;
    }

    public String getRewardsTotalNo() {
        return rewardsTotalNo;
    }

    public void setRewardsTotalNo(String rewardsTotalNo) {
        this.rewardsTotalNo = rewardsTotalNo;
    }

    public String getRewardsAmt() {
        return rewardsAmt;
    }

    public void setRewardsAmt(String rewardsAmt) {
        this.rewardsAmt = rewardsAmt;
    }

    public String getCashAwardsNo() {
        return cashAwardsNo;
    }

    public void setCashAwardsNo(String cashAwardsNo) {
        this.cashAwardsNo = cashAwardsNo;
    }

    public String getGoodServiceEntries() {
        return goodServiceEntries;
    }

    public void setGoodServiceEntries(String goodServiceEntries) {
        this.goodServiceEntries = goodServiceEntries;
    }

    public String getAnyOtherRewards() {
        return anyOtherRewards;
    }

    public void setAnyOtherRewards(String anyOtherRewards) {
        this.anyOtherRewards = anyOtherRewards;
    }

    public String getMeritoriousYear() {
        return meritoriousYear;
    }

    public void setMeritoriousYear(String meritoriousYear) {
        this.meritoriousYear = meritoriousYear;
    }

    public String getSltOccasion() {
        return sltOccasion;
    }

    public void setSltOccasion(String sltOccasion) {
        this.sltOccasion = sltOccasion;
    }

    public String getApprovMeritoriousServiceAwarded() {
        return approvMeritoriousServiceAwarded;
    }

    public void setApprovMeritoriousServiceAwarded(String approvMeritoriousServiceAwarded) {
        this.approvMeritoriousServiceAwarded = approvMeritoriousServiceAwarded;
    }
    

    public String getPenaltydetails() {
        return penaltydetails;
    }

    public void setPenaltydetails(String penaltydetails) {
        this.penaltydetails = penaltydetails;
    }

    public String getPenaltyyear() {
        return penaltyyear;
    }

    public void setPenaltyyear(String penaltyyear) {
        this.penaltyyear = penaltyyear;
    }

    public String getPenaltyOrderNo() {
        return penaltyOrderNo;
    }

    public void setPenaltyOrderNo(String penaltyOrderNo) {
        this.penaltyOrderNo = penaltyOrderNo;
    }

    public String getPenaltyOrderDate() {
        return penaltyOrderDate;
    }

    public void setPenaltyOrderDate(String penaltyOrderDate) {
        this.penaltyOrderDate = penaltyOrderDate;
    }

    public String getMinorpenaltydetails() {
        return minorpenaltydetails;
    }

    public void setMinorpenaltydetails(String minorpenaltydetails) {
        this.minorpenaltydetails = minorpenaltydetails;
    }

    public String getMinorpenaltyyear() {
        return minorpenaltyyear;
    }

    public void setMinorpenaltyyear(String minorpenaltyyear) {
        this.minorpenaltyyear = minorpenaltyyear;
    }

    public String getMinorpenaltyOrderNo() {
        return minorpenaltyOrderNo;
    }

    public void setMinorpenaltyOrderNo(String minorpenaltyOrderNo) {
        this.minorpenaltyOrderNo = minorpenaltyOrderNo;
    }

    public String getMinorpenaltyOrderDate() {
        return minorpenaltyOrderDate;
    }

    public void setMinorpenaltyOrderDate(String minorpenaltyOrderDate) {
        this.minorpenaltyOrderDate = minorpenaltyOrderDate;
    }
    

    public String getMedicalCategory() {
        return medicalCategory;
    }

    public void setMedicalCategory(String medicalCategory) {
        this.medicalCategory = medicalCategory;
    }

    public String getEnquirypending() {
        return enquirypending;
    }

    public void setEnquirypending(String enquirypending) {
        this.enquirypending = enquirypending;
    }

    public String getDpcyear() {
        return dpcyear;
    }

    public void setDpcyear(String dpcyear) {
        this.dpcyear = dpcyear;
    }

    public String getDpcnatureallegation() {
        return dpcnatureallegation;
    }

    public void setDpcnatureallegation(String dpcnatureallegation) {
        this.dpcnatureallegation = dpcnatureallegation;
    }

    public String getDpcpresentstatus() {
        return dpcpresentstatus;
    }

    public void setDpcpresentstatus(String dpcpresentstatus) {
        this.dpcpresentstatus = dpcpresentstatus;
    }

    public String getRecommendeeGPFNo() {
        return recommendeeGPFNo;
    }

    public void setRecommendeeGPFNo(String recommendeeGPFNo) {
        this.recommendeeGPFNo = recommendeeGPFNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBriefdescription() {
        return briefdescription;
    }

    public void setBriefdescription(String briefdescription) {
        this.briefdescription = briefdescription;
    }

    public String getTotaldifferentrankyears() {
        return totaldifferentrankyears;
    }

    public void setTotaldifferentrankyears(String totaldifferentrankyears) {
        this.totaldifferentrankyears = totaldifferentrankyears;
    }

    public String getTotaldifferentrankmonths() {
        return totaldifferentrankmonths;
    }

    public void setTotaldifferentrankmonths(String totaldifferentrankmonths) {
        this.totaldifferentrankmonths = totaldifferentrankmonths;
    }

    public String getTotaldifferentrankdays() {
        return totaldifferentrankdays;
    }

    public void setTotaldifferentrankdays(String totaldifferentrankdays) {
        this.totaldifferentrankdays = totaldifferentrankdays;
    }

    public String getOriginalFileNameintegrityCertificateDoc() {
        return originalFileNameintegrityCertificateDoc;
    }

    public void setOriginalFileNameintegrityCertificateDoc(String originalFileNameintegrityCertificateDoc) {
        this.originalFileNameintegrityCertificateDoc = originalFileNameintegrityCertificateDoc;
    }

    public String getDiskFilenameintegrityCertificateDoc() {
        return diskFilenameintegrityCertificateDoc;
    }

    public void setDiskFilenameintegrityCertificateDoc(String diskFilenameintegrityCertificateDoc) {
        this.diskFilenameintegrityCertificateDoc = diskFilenameintegrityCertificateDoc;
    }

    public String getOriginalFileNamemedicalcertificateDoc() {
        return originalFileNamemedicalcertificateDoc;
    }

    public void setOriginalFileNamemedicalcertificateDoc(String originalFileNamemedicalcertificateDoc) {
        this.originalFileNamemedicalcertificateDoc = originalFileNamemedicalcertificateDoc;
    }

    public String getDiskFilenamemedicalcertificateDoc() {
        return diskFilenamemedicalcertificateDoc;
    }

    public void setDiskFilenamemedicalcertificateDoc(String diskFilenamemedicalcertificateDoc) {
        this.diskFilenamemedicalcertificateDoc = diskFilenamemedicalcertificateDoc;
    }

    public String getPresentPostingPlace() {
        return presentPostingPlace;
    }

    public void setPresentPostingPlace(String presentPostingPlace) {
        this.presentPostingPlace = presentPostingPlace;
    }

    public String getPresentPostingPIN() {
        return presentPostingPIN;
    }

    public void setPresentPostingPIN(String presentPostingPIN) {
        this.presentPostingPIN = presentPostingPIN;
    }

    public String getPresentPostingDate() {
        return presentPostingDate;
    }

    public void setPresentPostingDate(String presentPostingDate) {
        this.presentPostingDate = presentPostingDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFatherfirstname() {
        return fatherfirstname;
    }

    public void setFatherfirstname(String fatherfirstname) {
        this.fatherfirstname = fatherfirstname;
    }

    public String getFathermiddlename() {
        return fathermiddlename;
    }

    public void setFathermiddlename(String fathermiddlename) {
        this.fathermiddlename = fathermiddlename;
    }

    public String getFatherlastname() {
        return fatherlastname;
    }

    public void setFatherlastname(String fatherlastname) {
        this.fatherlastname = fatherlastname;
    }

    public String getCourtCasePendingYear() {
        return courtCasePendingYear;
    }

    public void setCourtCasePendingYear(String courtCasePendingYear) {
        this.courtCasePendingYear = courtCasePendingYear;
    }

    public String getCourtCasePendingDetails() {
        return courtCasePendingDetails;
    }

    public void setCourtCasePendingDetails(String courtCasePendingDetails) {
        this.courtCasePendingDetails = courtCasePendingDetails;
    }

    public String getCourtCasePendingStatus() {
        return courtCasePendingStatus;
    }

    public void setCourtCasePendingStatus(String courtCasePendingStatus) {
        this.courtCasePendingStatus = courtCasePendingStatus;
    }

    public String getAcrGrading1() {
        return acrGrading1;
    }

    public void setAcrGrading1(String acrGrading1) {
        this.acrGrading1 = acrGrading1;
    }

    public String getAcrGrading2() {
        return acrGrading2;
    }

    public void setAcrGrading2(String acrGrading2) {
        this.acrGrading2 = acrGrading2;
    }

    public String getAcrGrading3() {
        return acrGrading3;
    }

    public void setAcrGrading3(String acrGrading3) {
        this.acrGrading3 = acrGrading3;
    }

    public String getAcrGrading4() {
        return acrGrading4;
    }

    public void setAcrGrading4(String acrGrading4) {
        this.acrGrading4 = acrGrading4;
    }

    public String getAcrGrading5() {
        return acrGrading5;
    }

    public void setAcrGrading5(String acrGrading5) {
        this.acrGrading5 = acrGrading5;
    }

    public String getAcrGrading6() {
        return acrGrading6;
    }

    public void setAcrGrading6(String acrGrading6) {
        this.acrGrading6 = acrGrading6;
    }

    public String getAcrGrading7() {
        return acrGrading7;
    }

    public void setAcrGrading7(String acrGrading7) {
        this.acrGrading7 = acrGrading7;
    }

    public String getAcrGrading8() {
        return acrGrading8;
    }

    public void setAcrGrading8(String acrGrading8) {
        this.acrGrading8 = acrGrading8;
    }

    public String getAcrGrading9() {
        return acrGrading9;
    }

    public void setAcrGrading9(String acrGrading9) {
        this.acrGrading9 = acrGrading9;
    }

    public String getAcrGrading10() {
        return acrGrading10;
    }

    public void setAcrGrading10(String acrGrading10) {
        this.acrGrading10 = acrGrading10;
    }

    public String getAcrGrading11() {
        return acrGrading11;
    }

    public void setAcrGrading11(String acrGrading11) {
        this.acrGrading11 = acrGrading11;
    }

    public String getAcrGrading12() {
        return acrGrading12;
    }

    public void setAcrGrading12(String acrGrading12) {
        this.acrGrading12 = acrGrading12;
    }

    public String getAcrGrading13() {
        return acrGrading13;
    }

    public void setAcrGrading13(String acrGrading13) {
        this.acrGrading13 = acrGrading13;
    }

    public String getAcrGrading14() {
        return acrGrading14;
    }

    public void setAcrGrading14(String acrGrading14) {
        this.acrGrading14 = acrGrading14;
    }

    public String getAcrGradingOS() {
        return acrGradingOS;
    }

    public void setAcrGradingOS(String acrGradingOS) {
        this.acrGradingOS = acrGradingOS;
    }

    public String getAcrGradingVS() {
        return acrGradingVS;
    }

    public void setAcrGradingVS(String acrGradingVS) {
        this.acrGradingVS = acrGradingVS;
    }

    public String getAcrGradingGood() {
        return acrGradingGood;
    }

    public void setAcrGradingGood(String acrGradingGood) {
        this.acrGradingGood = acrGradingGood;
    }

    public String getAcrGradingAvg() {
        return acrGradingAvg;
    }

    public void setAcrGradingAvg(String acrGradingAvg) {
        this.acrGradingAvg = acrGradingAvg;
    }

    public String getAcrGradingNic() {
        return acrGradingNic;
    }

    public void setAcrGradingNic(String acrGradingNic) {
        this.acrGradingNic = acrGradingNic;
    }

    public String getAcrGradingAdv() {
        return acrGradingAdv;
    }

    public void setAcrGradingAdv(String acrGradingAdv) {
        this.acrGradingAdv = acrGradingAdv;
    }

    public String getAcrGradingMs() {
        return acrGradingMs;
    }

    public void setAcrGradingMs(String acrGradingMs) {
        this.acrGradingMs = acrGradingMs;
    }

    public String getAcrGradingNa() {
        return acrGradingNa;
    }

    public void setAcrGradingNa(String acrGradingNa) {
        this.acrGradingNa = acrGradingNa;
    }

    public MultipartFile getIntegrityCertificateDoc() {
        return integrityCertificateDoc;
    }

    public void setIntegrityCertificateDoc(MultipartFile integrityCertificateDoc) {
        this.integrityCertificateDoc = integrityCertificateDoc;
    }

    public MultipartFile getMedicalCertificateDoc() {
        return medicalCertificateDoc;
    }

    public void setMedicalCertificateDoc(MultipartFile medicalCertificateDoc) {
        this.medicalCertificateDoc = medicalCertificateDoc;
    }

    public String getPresidentcommendation() {
        return presidentcommendation;
    }

    public void setPresidentcommendation(String presidentcommendation) {
        this.presidentcommendation = presidentcommendation;
    }

    public String getPresidentappreciation() {
        return presidentappreciation;
    }

    public void setPresidentappreciation(String presidentappreciation) {
        this.presidentappreciation = presidentappreciation;
    }

    public MultipartFile getPunishmentDoc() {
        return punishmentDoc;
    }

    public void setPunishmentDoc(MultipartFile punishmentDoc) {
        this.punishmentDoc = punishmentDoc;
    }

    public String getOriginalFileNamepunishmentDoc() {
        return originalFileNamepunishmentDoc;
    }

    public void setOriginalFileNamepunishmentDoc(String originalFileNamepunishmentDoc) {
        this.originalFileNamepunishmentDoc = originalFileNamepunishmentDoc;
    }

    public String getDiskFilenamepunishmentDoc() {
        return diskFilenamepunishmentDoc;
    }

    public void setDiskFilenamepunishmentDoc(String diskFilenamepunishmentDoc) {
        this.diskFilenamepunishmentDoc = diskFilenamepunishmentDoc;
    }

    public MultipartFile getEnquiryDoc() {
        return enquiryDoc;
    }

    public void setEnquiryDoc(MultipartFile enquiryDoc) {
        this.enquiryDoc = enquiryDoc;
    }

    public String getOriginalFileNameenquiryDoc() {
        return originalFileNameenquiryDoc;
    }

    public void setOriginalFileNameenquiryDoc(String originalFileNameenquiryDoc) {
        this.originalFileNameenquiryDoc = originalFileNameenquiryDoc;
    }

    public String getDiskFilenameenquiryDoc() {
        return diskFilenameenquiryDoc;
    }

    public void setDiskFilenameenquiryDoc(String diskFilenameenquiryDoc) {
        this.diskFilenameenquiryDoc = diskFilenameenquiryDoc;
    }

    public MultipartFile getDpcDoc() {
        return dpcDoc;
    }

    public void setDpcDoc(MultipartFile dpcDoc) {
        this.dpcDoc = dpcDoc;
    }

    public String getOriginalFileNamedpcDoc() {
        return originalFileNamedpcDoc;
    }

    public void setOriginalFileNamedpcDoc(String originalFileNamedpcDoc) {
        this.originalFileNamedpcDoc = originalFileNamedpcDoc;
    }

    public String getDiskFilenamedpcDoc() {
        return diskFilenamedpcDoc;
    }

    public void setDiskFilenamedpcDoc(String diskFilenamedpcDoc) {
        this.diskFilenamedpcDoc = diskFilenamedpcDoc;
    }

    public MultipartFile getCourtCaseDoc() {
        return courtCaseDoc;
    }

    public void setCourtCaseDoc(MultipartFile courtCaseDoc) {
        this.courtCaseDoc = courtCaseDoc;
    }

    public String getOriginalFileNamecourtCaseDoc() {
        return originalFileNamecourtCaseDoc;
    }

    public void setOriginalFileNamecourtCaseDoc(String originalFileNamecourtCaseDoc) {
        this.originalFileNamecourtCaseDoc = originalFileNamecourtCaseDoc;
    }

    public String getDiskFilenamecourtCaseDoc() {
        return diskFilenamecourtCaseDoc;
    }

    public void setDiskFilenamecourtCaseDoc(String diskFilenamecourtCaseDoc) {
        this.diskFilenamecourtCaseDoc = diskFilenamecourtCaseDoc;
    }

    public List getDifferentRankList() {
        return differentRankList;
    }

    public void setDifferentRankList(List differentRankList) {
        this.differentRankList = differentRankList;
    }

    public String getIfenquirypending() {
        return ifenquirypending;
    }

    public void setIfenquirypending(String ifenquirypending) {
        this.ifenquirypending = ifenquirypending;
    }

    public String getCashAwardsAmt() {
        return cashAwardsAmt;
    }

    public void setCashAwardsAmt(String cashAwardsAmt) {
        this.cashAwardsAmt = cashAwardsAmt;
    }

    public String getPresidentcommendationAmt() {
        return presidentcommendationAmt;
    }

    public void setPresidentcommendationAmt(String presidentcommendationAmt) {
        this.presidentcommendationAmt = presidentcommendationAmt;
    }

    public String getPresidentappreciationAmt() {
        return presidentappreciationAmt;
    }

    public void setPresidentappreciationAmt(String presidentappreciationAmt) {
        this.presidentappreciationAmt = presidentappreciationAmt;
    }

    public String getGoodServiceEntriesAmt() {
        return goodServiceEntriesAmt;
    }

    public void setGoodServiceEntriesAmt(String goodServiceEntriesAmt) {
        this.goodServiceEntriesAmt = goodServiceEntriesAmt;
    }

    public String getAnyOtherRewardsDesc() {
        return anyOtherRewardsDesc;
    }

    public void setAnyOtherRewardsDesc(String anyOtherRewardsDesc) {
        this.anyOtherRewardsDesc = anyOtherRewardsDesc;
    }

    public MultipartFile getAcrGrading1Doc() {
        return acrGrading1Doc;
    }

    public void setAcrGrading1Doc(MultipartFile acrGrading1Doc) {
        this.acrGrading1Doc = acrGrading1Doc;
    }

    public String getOriginalFileNameAcrGrading1Doc() {
        return originalFileNameAcrGrading1Doc;
    }

    public void setOriginalFileNameAcrGrading1Doc(String originalFileNameAcrGrading1Doc) {
        this.originalFileNameAcrGrading1Doc = originalFileNameAcrGrading1Doc;
    }

    public String getDiskFilenameAcrGrading1Doc() {
        return diskFilenameAcrGrading1Doc;
    }

    public void setDiskFilenameAcrGrading1Doc(String diskFilenameAcrGrading1Doc) {
        this.diskFilenameAcrGrading1Doc = diskFilenameAcrGrading1Doc;
    }

    public MultipartFile getAcrGrading2Doc() {
        return acrGrading2Doc;
    }

    public void setAcrGrading2Doc(MultipartFile acrGrading2Doc) {
        this.acrGrading2Doc = acrGrading2Doc;
    }

    public String getOriginalFileNameAcrGrading2Doc() {
        return originalFileNameAcrGrading2Doc;
    }

    public void setOriginalFileNameAcrGrading2Doc(String originalFileNameAcrGrading2Doc) {
        this.originalFileNameAcrGrading2Doc = originalFileNameAcrGrading2Doc;
    }

    public String getDiskFilenameAcrGrading2Doc() {
        return diskFilenameAcrGrading2Doc;
    }

    public void setDiskFilenameAcrGrading2Doc(String diskFilenameAcrGrading2Doc) {
        this.diskFilenameAcrGrading2Doc = diskFilenameAcrGrading2Doc;
    }

    public MultipartFile getAcrGrading3Doc() {
        return acrGrading3Doc;
    }

    public void setAcrGrading3Doc(MultipartFile acrGrading3Doc) {
        this.acrGrading3Doc = acrGrading3Doc;
    }

    public String getOriginalFileNameAcrGrading3Doc() {
        return originalFileNameAcrGrading3Doc;
    }

    public void setOriginalFileNameAcrGrading3Doc(String originalFileNameAcrGrading3Doc) {
        this.originalFileNameAcrGrading3Doc = originalFileNameAcrGrading3Doc;
    }

    public String getDiskFilenameAcrGrading3Doc() {
        return diskFilenameAcrGrading3Doc;
    }

    public void setDiskFilenameAcrGrading3Doc(String diskFilenameAcrGrading3Doc) {
        this.diskFilenameAcrGrading3Doc = diskFilenameAcrGrading3Doc;
    }

    public MultipartFile getAcrGrading4Doc() {
        return acrGrading4Doc;
    }

    public void setAcrGrading4Doc(MultipartFile acrGrading4Doc) {
        this.acrGrading4Doc = acrGrading4Doc;
    }

    public String getOriginalFileNameAcrGrading4Doc() {
        return originalFileNameAcrGrading4Doc;
    }

    public void setOriginalFileNameAcrGrading4Doc(String originalFileNameAcrGrading4Doc) {
        this.originalFileNameAcrGrading4Doc = originalFileNameAcrGrading4Doc;
    }

    public String getDiskFilenameAcrGrading4Doc() {
        return diskFilenameAcrGrading4Doc;
    }

    public void setDiskFilenameAcrGrading4Doc(String diskFilenameAcrGrading4Doc) {
        this.diskFilenameAcrGrading4Doc = diskFilenameAcrGrading4Doc;
    }

    public MultipartFile getAcrGrading5Doc() {
        return acrGrading5Doc;
    }

    public void setAcrGrading5Doc(MultipartFile acrGrading5Doc) {
        this.acrGrading5Doc = acrGrading5Doc;
    }

    public String getOriginalFileNameAcrGrading5Doc() {
        return originalFileNameAcrGrading5Doc;
    }

    public void setOriginalFileNameAcrGrading5Doc(String originalFileNameAcrGrading5Doc) {
        this.originalFileNameAcrGrading5Doc = originalFileNameAcrGrading5Doc;
    }

    public String getDiskFilenameAcrGrading5Doc() {
        return diskFilenameAcrGrading5Doc;
    }

    public void setDiskFilenameAcrGrading5Doc(String diskFilenameAcrGrading5Doc) {
        this.diskFilenameAcrGrading5Doc = diskFilenameAcrGrading5Doc;
    }

    public MultipartFile getAcrGrading6Doc() {
        return acrGrading6Doc;
    }

    public void setAcrGrading6Doc(MultipartFile acrGrading6Doc) {
        this.acrGrading6Doc = acrGrading6Doc;
    }

    public String getOriginalFileNameAcrGrading6Doc() {
        return originalFileNameAcrGrading6Doc;
    }

    public void setOriginalFileNameAcrGrading6Doc(String originalFileNameAcrGrading6Doc) {
        this.originalFileNameAcrGrading6Doc = originalFileNameAcrGrading6Doc;
    }

    public String getDiskFilenameAcrGrading6Doc() {
        return diskFilenameAcrGrading6Doc;
    }

    public void setDiskFilenameAcrGrading6Doc(String diskFilenameAcrGrading6Doc) {
        this.diskFilenameAcrGrading6Doc = diskFilenameAcrGrading6Doc;
    }

    public MultipartFile getAcrGrading7Doc() {
        return acrGrading7Doc;
    }

    public void setAcrGrading7Doc(MultipartFile acrGrading7Doc) {
        this.acrGrading7Doc = acrGrading7Doc;
    }

    public String getOriginalFileNameAcrGrading7Doc() {
        return originalFileNameAcrGrading7Doc;
    }

    public void setOriginalFileNameAcrGrading7Doc(String originalFileNameAcrGrading7Doc) {
        this.originalFileNameAcrGrading7Doc = originalFileNameAcrGrading7Doc;
    }

    public String getDiskFilenameAcrGrading7Doc() {
        return diskFilenameAcrGrading7Doc;
    }

    public void setDiskFilenameAcrGrading7Doc(String diskFilenameAcrGrading7Doc) {
        this.diskFilenameAcrGrading7Doc = diskFilenameAcrGrading7Doc;
    }

    public MultipartFile getAcrGrading8Doc() {
        return acrGrading8Doc;
    }

    public void setAcrGrading8Doc(MultipartFile acrGrading8Doc) {
        this.acrGrading8Doc = acrGrading8Doc;
    }

    public String getOriginalFileNameAcrGrading8Doc() {
        return originalFileNameAcrGrading8Doc;
    }

    public void setOriginalFileNameAcrGrading8Doc(String originalFileNameAcrGrading8Doc) {
        this.originalFileNameAcrGrading8Doc = originalFileNameAcrGrading8Doc;
    }

    public String getDiskFilenameAcrGrading8Doc() {
        return diskFilenameAcrGrading8Doc;
    }

    public void setDiskFilenameAcrGrading8Doc(String diskFilenameAcrGrading8Doc) {
        this.diskFilenameAcrGrading8Doc = diskFilenameAcrGrading8Doc;
    }

    public MultipartFile getAcrGrading9Doc() {
        return acrGrading9Doc;
    }

    public void setAcrGrading9Doc(MultipartFile acrGrading9Doc) {
        this.acrGrading9Doc = acrGrading9Doc;
    }

    public String getOriginalFileNameAcrGrading9Doc() {
        return originalFileNameAcrGrading9Doc;
    }

    public void setOriginalFileNameAcrGrading9Doc(String originalFileNameAcrGrading9Doc) {
        this.originalFileNameAcrGrading9Doc = originalFileNameAcrGrading9Doc;
    }

    public String getDiskFilenameAcrGrading9Doc() {
        return diskFilenameAcrGrading9Doc;
    }

    public void setDiskFilenameAcrGrading9Doc(String diskFilenameAcrGrading9Doc) {
        this.diskFilenameAcrGrading9Doc = diskFilenameAcrGrading9Doc;
    }

    public MultipartFile getAcrGrading10Doc() {
        return acrGrading10Doc;
    }

    public void setAcrGrading10Doc(MultipartFile acrGrading10Doc) {
        this.acrGrading10Doc = acrGrading10Doc;
    }

    public String getOriginalFileNameAcrGrading10Doc() {
        return originalFileNameAcrGrading10Doc;
    }

    public void setOriginalFileNameAcrGrading10Doc(String originalFileNameAcrGrading10Doc) {
        this.originalFileNameAcrGrading10Doc = originalFileNameAcrGrading10Doc;
    }

    public String getDiskFilenameAcrGrading10Doc() {
        return diskFilenameAcrGrading10Doc;
    }

    public void setDiskFilenameAcrGrading10Doc(String diskFilenameAcrGrading10Doc) {
        this.diskFilenameAcrGrading10Doc = diskFilenameAcrGrading10Doc;
    }

    public MultipartFile getAcrGrading11Doc() {
        return acrGrading11Doc;
    }

    public void setAcrGrading11Doc(MultipartFile acrGrading11Doc) {
        this.acrGrading11Doc = acrGrading11Doc;
    }

    public String getOriginalFileNameAcrGrading11Doc() {
        return originalFileNameAcrGrading11Doc;
    }

    public void setOriginalFileNameAcrGrading11Doc(String originalFileNameAcrGrading11Doc) {
        this.originalFileNameAcrGrading11Doc = originalFileNameAcrGrading11Doc;
    }

    public String getDiskFilenameAcrGrading11Doc() {
        return diskFilenameAcrGrading11Doc;
    }

    public void setDiskFilenameAcrGrading11Doc(String diskFilenameAcrGrading11Doc) {
        this.diskFilenameAcrGrading11Doc = diskFilenameAcrGrading11Doc;
    }

    public MultipartFile getAcrGrading12Doc() {
        return acrGrading12Doc;
    }

    public void setAcrGrading12Doc(MultipartFile acrGrading12Doc) {
        this.acrGrading12Doc = acrGrading12Doc;
    }

    public String getOriginalFileNameAcrGrading12Doc() {
        return originalFileNameAcrGrading12Doc;
    }

    public void setOriginalFileNameAcrGrading12Doc(String originalFileNameAcrGrading12Doc) {
        this.originalFileNameAcrGrading12Doc = originalFileNameAcrGrading12Doc;
    }

    public String getDiskFilenameAcrGrading12Doc() {
        return diskFilenameAcrGrading12Doc;
    }

    public void setDiskFilenameAcrGrading12Doc(String diskFilenameAcrGrading12Doc) {
        this.diskFilenameAcrGrading12Doc = diskFilenameAcrGrading12Doc;
    }

    public MultipartFile getAcrGrading13Doc() {
        return acrGrading13Doc;
    }

    public void setAcrGrading13Doc(MultipartFile acrGrading13Doc) {
        this.acrGrading13Doc = acrGrading13Doc;
    }

    public String getOriginalFileNameAcrGrading13Doc() {
        return originalFileNameAcrGrading13Doc;
    }

    public void setOriginalFileNameAcrGrading13Doc(String originalFileNameAcrGrading13Doc) {
        this.originalFileNameAcrGrading13Doc = originalFileNameAcrGrading13Doc;
    }

    public String getDiskFilenameAcrGrading13Doc() {
        return diskFilenameAcrGrading13Doc;
    }

    public void setDiskFilenameAcrGrading13Doc(String diskFilenameAcrGrading13Doc) {
        this.diskFilenameAcrGrading13Doc = diskFilenameAcrGrading13Doc;
    }

    public String getSltAwardOccasion() {
        return sltAwardOccasion;
    }

    public void setSltAwardOccasion(String sltAwardOccasion) {
        this.sltAwardOccasion = sltAwardOccasion;
    }

    public MultipartFile getDiscCCRollone() {
        return discCCRollone;
    }

    public void setDiscCCRollone(MultipartFile discCCRollone) {
        this.discCCRollone = discCCRollone;
    }

    public String getOriginalFileNameCCRollone() {
        return originalFileNameCCRollone;
    }

    public void setOriginalFileNameCCRollone(String originalFileNameCCRollone) {
        this.originalFileNameCCRollone = originalFileNameCCRollone;
    }

    public String getDiskFilenameCCRollone() {
        return diskFilenameCCRollone;
    }

    public void setDiskFilenameCCRollone(String diskFilenameCCRollone) {
        this.diskFilenameCCRollone = diskFilenameCCRollone;
    }

    public MultipartFile getDiscCCRolltwo() {
        return discCCRolltwo;
    }

    public void setDiscCCRolltwo(MultipartFile discCCRolltwo) {
        this.discCCRolltwo = discCCRolltwo;
    }

    public String getOriginalFileNameCCRolltwo() {
        return originalFileNameCCRolltwo;
    }

    public void setOriginalFileNameCCRolltwo(String originalFileNameCCRolltwo) {
        this.originalFileNameCCRolltwo = originalFileNameCCRolltwo;
    }

    public String getDiskFilenameCCRolltwo() {
        return diskFilenameCCRolltwo;
    }

    public void setDiskFilenameCCRolltwo(String diskFilenameCCRolltwo) {
        this.diskFilenameCCRolltwo = diskFilenameCCRolltwo;
    }

    public MultipartFile getDiscCCRollthree() {
        return discCCRollthree;
    }

    public void setDiscCCRollthree(MultipartFile discCCRollthree) {
        this.discCCRollthree = discCCRollthree;
    }

    public String getOriginalFileNameCCRollthree() {
        return originalFileNameCCRollthree;
    }

    public void setOriginalFileNameCCRollthree(String originalFileNameCCRollthree) {
        this.originalFileNameCCRollthree = originalFileNameCCRollthree;
    }

    public String getDiskFilenameCCRollthree() {
        return diskFilenameCCRollthree;
    }

    public void setDiskFilenameCCRollthree(String diskFilenameCCRollthree) {
        this.diskFilenameCCRollthree = diskFilenameCCRollthree;
    }

    public String getCcrolloneremarks() {
        return ccrolloneremarks;
    }

    public void setCcrolloneremarks(String ccrolloneremarks) {
        this.ccrolloneremarks = ccrolloneremarks;
    }

    public String getCcrolltworemarks() {
        return ccrolltworemarks;
    }

    public void setCcrolltworemarks(String ccrolltworemarks) {
        this.ccrolltworemarks = ccrolltworemarks;
    }

    public String getCcrollthreeremarks() {
        return ccrollthreeremarks;
    }

    public void setCcrollthreeremarks(String ccrollthreeremarks) {
        this.ccrollthreeremarks = ccrollthreeremarks;
    }

    public String getSrCaseNo() {
        return srCaseNo;
    }

    public void setSrCaseNo(String srCaseNo) {
        this.srCaseNo = srCaseNo;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getFrDate() {
        return frDate;
    }

    public void setFrDate(String frDate) {
        this.frDate = frDate;
    }

    public String getDtlsTeamUse() {
        return dtlsTeamUse;
    }

    public void setDtlsTeamUse(String dtlsTeamUse) {
        this.dtlsTeamUse = dtlsTeamUse;
    }

    public String getNoOfExhibits() {
        return noOfExhibits;
    }

    public void setNoOfExhibits(String noOfExhibits) {
        this.noOfExhibits = noOfExhibits;
    }

    public String getConvictionDtls() {
        return convictionDtls;
    }

    public void setConvictionDtls(String convictionDtls) {
        this.convictionDtls = convictionDtls;
    }

    public String getCrimeProceedAttach() {
        return crimeProceedAttach;
    }

    public void setCrimeProceedAttach(String crimeProceedAttach) {
        this.crimeProceedAttach = crimeProceedAttach;
    }

    public String getSrCaseId() {
        return srCaseId;
    }

    public void setSrCaseId(String srCaseId) {
        this.srCaseId = srCaseId;
    }

    public String getPsName() {
        return psName;
    }

    public void setPsName(String psName) {
        this.psName = psName;
    }

    public String getInvstCaseNo() {
        return invstCaseNo;
    }

    public void setInvstCaseNo(String invstCaseNo) {
        this.invstCaseNo = invstCaseNo;
    }

    public String getInvstDateReg() {
        return invstDateReg;
    }

    public void setInvstDateReg(String invstDateReg) {
        this.invstDateReg = invstDateReg;
    }

    public String getInvstCsFr() {
        return invstCsFr;
    }

    public void setInvstCsFr(String invstCsFr) {
        this.invstCsFr = invstCsFr;
    }

    public String getInvstFinalSubDate() {
        return invstFinalSubDate;
    }

    public void setInvstFinalSubDate(String invstFinalSubDate) {
        this.invstFinalSubDate = invstFinalSubDate;
    }

    public String getInvstSrNonsr() {
        return invstSrNonsr;
    }

    public void setInvstSrNonsr(String invstSrNonsr) {
        this.invstSrNonsr = invstSrNonsr;
    }

    public String getInvstBriefCase() {
        return invstBriefCase;
    }

    public void setInvstBriefCase(String invstBriefCase) {
        this.invstBriefCase = invstBriefCase;
    }

    public String getInvstInnovMethods() {
        return invstInnovMethods;
    }

    public void setInvstInnovMethods(String invstInnovMethods) {
        this.invstInnovMethods = invstInnovMethods;
    }

    public String getInvstScientificAids() {
        return invstScientificAids;
    }

    public void setInvstScientificAids(String invstScientificAids) {
        this.invstScientificAids = invstScientificAids;
    }

    public String getInvstScientificEvd() {
        return invstScientificEvd;
    }

    public void setInvstScientificEvd(String invstScientificEvd) {
        this.invstScientificEvd = invstScientificEvd;
    }

    public String getInvstPromptness() {
        return invstPromptness;
    }

    public void setInvstPromptness(String invstPromptness) {
        this.invstPromptness = invstPromptness;
    }

    public String getInvstAttachConfis() {
        return invstAttachConfis;
    }

    public void setInvstAttachConfis(String invstAttachConfis) {
        this.invstAttachConfis = invstAttachConfis;
    }

    public String getInvstChallenges() {
        return invstChallenges;
    }

    public void setInvstChallenges(String invstChallenges) {
        this.invstChallenges = invstChallenges;
    }

    public String getInvstConvcDtls() {
        return invstConvcDtls;
    }

    public void setInvstConvcDtls(String invstConvcDtls) {
        this.invstConvcDtls = invstConvcDtls;
    }

    public MultipartFile getJudgementCopy() {
        return judgementCopy;
    }

    public void setJudgementCopy(MultipartFile judgementCopy) {
        this.judgementCopy = judgementCopy;
    }

    public String getOriginalJudgementCopy() {
        return originalJudgementCopy;
    }

    public void setOriginalJudgementCopy(String originalJudgementCopy) {
        this.originalJudgementCopy = originalJudgementCopy;
    }

    public String getDiskJudgementCopy() {
        return diskJudgementCopy;
    }

    public void setDiskJudgementCopy(String diskJudgementCopy) {
        this.diskJudgementCopy = diskJudgementCopy;
    }

    public String getInvstCaseId() {
        return invstCaseId;
    }

    public void setInvstCaseId(String invstCaseId) {
        this.invstCaseId = invstCaseId;
    }

    public MultipartFile getMedicalCategoryDoc() {
        return medicalCategoryDoc;
    }

    public void setMedicalCategoryDoc(MultipartFile medicalCategoryDoc) {
        this.medicalCategoryDoc = medicalCategoryDoc;
    }

    public String getOriginalFileNameMedicalCategoryDoc() {
        return originalFileNameMedicalCategoryDoc;
    }

    public void setOriginalFileNameMedicalCategoryDoc(String originalFileNameMedicalCategoryDoc) {
        this.originalFileNameMedicalCategoryDoc = originalFileNameMedicalCategoryDoc;
    }

    public String getDiskFilenameMedicalCategoryDoc() {
        return diskFilenameMedicalCategoryDoc;
    }

    public void setDiskFilenameMedicalCategoryDoc(String diskFilenameMedicalCategoryDoc) {
        this.diskFilenameMedicalCategoryDoc = diskFilenameMedicalCategoryDoc;
    }

    public String getPunishmentmajor1() {
        return punishmentmajor1;
    }

    public void setPunishmentmajor1(String punishmentmajor1) {
        this.punishmentmajor1 = punishmentmajor1;
    }

    public String getPunishmentminor1() {
        return punishmentminor1;
    }

    public void setPunishmentminor1(String punishmentminor1) {
        this.punishmentminor1 = punishmentminor1;
    }

    public String getPunishmentmajyear1() {
        return punishmentmajyear1;
    }

    public void setPunishmentmajyear1(String punishmentmajyear1) {
        this.punishmentmajyear1 = punishmentmajyear1;
    }

    public String getPunishmentminyear1() {
        return punishmentminyear1;
    }

    public void setPunishmentminyear1(String punishmentminyear1) {
        this.punishmentminyear1 = punishmentminyear1;
    }

    public String getPunishmentmajor2() {
        return punishmentmajor2;
    }

    public void setPunishmentmajor2(String punishmentmajor2) {
        this.punishmentmajor2 = punishmentmajor2;
    }

    public String getPunishmentminor2() {
        return punishmentminor2;
    }

    public void setPunishmentminor2(String punishmentminor2) {
        this.punishmentminor2 = punishmentminor2;
    }

    public String getPunishmentmajyear2() {
        return punishmentmajyear2;
    }

    public void setPunishmentmajyear2(String punishmentmajyear2) {
        this.punishmentmajyear2 = punishmentmajyear2;
    }

    public String getPunishmentminyear2() {
        return punishmentminyear2;
    }

    public void setPunishmentminyear2(String punishmentminyear2) {
        this.punishmentminyear2 = punishmentminyear2;
    }

    public String getPunishmentmajor3() {
        return punishmentmajor3;
    }

    public void setPunishmentmajor3(String punishmentmajor3) {
        this.punishmentmajor3 = punishmentmajor3;
    }

    public String getPunishmentminor3() {
        return punishmentminor3;
    }

    public void setPunishmentminor3(String punishmentminor3) {
        this.punishmentminor3 = punishmentminor3;
    }

    public String getPunishmentmajyear3() {
        return punishmentmajyear3;
    }

    public void setPunishmentmajyear3(String punishmentmajyear3) {
        this.punishmentmajyear3 = punishmentmajyear3;
    }

    public String getPunishmentminyear3() {
        return punishmentminyear3;
    }

    public void setPunishmentminyear3(String punishmentminyear3) {
        this.punishmentminyear3 = punishmentminyear3;
    }

    public String getPunishmentmajor4() {
        return punishmentmajor4;
    }

    public void setPunishmentmajor4(String punishmentmajor4) {
        this.punishmentmajor4 = punishmentmajor4;
    }

    public String getPunishmentminor4() {
        return punishmentminor4;
    }

    public void setPunishmentminor4(String punishmentminor4) {
        this.punishmentminor4 = punishmentminor4;
    }

    public String getPunishmentmajyear4() {
        return punishmentmajyear4;
    }

    public void setPunishmentmajyear4(String punishmentmajyear4) {
        this.punishmentmajyear4 = punishmentmajyear4;
    }

    public String getPunishmentminyear4() {
        return punishmentminyear4;
    }

    public void setPunishmentminyear4(String punishmentminyear4) {
        this.punishmentminyear4 = punishmentminyear4;
    }

    public String getPunishmentmajor5() {
        return punishmentmajor5;
    }

    public void setPunishmentmajor5(String punishmentmajor5) {
        this.punishmentmajor5 = punishmentmajor5;
    }

    public String getPunishmentminor5() {
        return punishmentminor5;
    }

    public void setPunishmentminor5(String punishmentminor5) {
        this.punishmentminor5 = punishmentminor5;
    }

    public String getPunishmentmajyear5() {
        return punishmentmajyear5;
    }

    public void setPunishmentmajyear5(String punishmentmajyear5) {
        this.punishmentmajyear5 = punishmentmajyear5;
    }

    public String getPunishmentminyear5() {
        return punishmentminyear5;
    }

    public void setPunishmentminyear5(String punishmentminyear5) {
        this.punishmentminyear5 = punishmentminyear5;
    }

    public String getCriminalcaseifany() {
        return criminalcaseifany;
    }

    public void setCriminalcaseifany(String criminalcaseifany) {
        this.criminalcaseifany = criminalcaseifany;
    }

    public String getAnnexure2Details() {
        return annexure2Details;
    }

    public void setAnnexure2Details(String annexure2Details) {
        this.annexure2Details = annexure2Details;
    }

    public String getChargesheetedifany() {
        return chargesheetedifany;
    }

    public void setChargesheetedifany(String chargesheetedifany) {
        this.chargesheetedifany = chargesheetedifany;
    }

    public String getAnnexure3Details() {
        return annexure3Details;
    }

    public void setAnnexure3Details(String annexure3Details) {
        this.annexure3Details = annexure3Details;
    }

    public String getOtherInformationFromDistrictDgps() {
        return otherInformationFromDistrictDgps;
    }

    public void setOtherInformationFromDistrictDgps(String otherInformationFromDistrictDgps) {
        this.otherInformationFromDistrictDgps = otherInformationFromDistrictDgps;
    }

    public MultipartFile getMajorPunishmentDocument() {
        return MajorPunishmentDocument;
    }

    public void setMajorPunishmentDocument(MultipartFile MajorPunishmentDocument) {
        this.MajorPunishmentDocument = MajorPunishmentDocument;
    }

    public String getOriginalFileNameMajorPunishment() {
        return originalFileNameMajorPunishment;
    }

    public void setOriginalFileNameMajorPunishment(String originalFileNameMajorPunishment) {
        this.originalFileNameMajorPunishment = originalFileNameMajorPunishment;
    }

    public String getDiskFilenameMajorPunishment() {
        return diskFilenameMajorPunishment;
    }

    public void setDiskFilenameMajorPunishment(String diskFilenameMajorPunishment) {
        this.diskFilenameMajorPunishment = diskFilenameMajorPunishment;
    }

    public MultipartFile getMinorPunishmentDocument() {
        return MinorPunishmentDocument;
    }

    public void setMinorPunishmentDocument(MultipartFile MinorPunishmentDocument) {
        this.MinorPunishmentDocument = MinorPunishmentDocument;
    }

    public String getDiskFilenameMinorPunishmentpreeciding() {
        return diskFilenameMinorPunishmentpreeciding;
    }

    public void setDiskFilenameMinorPunishmentpreeciding(String diskFilenameMinorPunishmentpreeciding) {
        this.diskFilenameMinorPunishmentpreeciding = diskFilenameMinorPunishmentpreeciding;
    }

    public String getOriginalFileNameMajorPunishmentpreeciding() {
        return originalFileNameMajorPunishmentpreeciding;
    }

    public void setOriginalFileNameMajorPunishmentpreeciding(String originalFileNameMajorPunishmentpreeciding) {
        this.originalFileNameMajorPunishmentpreeciding = originalFileNameMajorPunishmentpreeciding;
    }

    public MultipartFile getMajorPunishmentpreecidingDocument() {
        return majorPunishmentpreecidingDocument;
    }

    public void setMajorPunishmentpreecidingDocument(MultipartFile majorPunishmentpreecidingDocument) {
        this.majorPunishmentpreecidingDocument = majorPunishmentpreecidingDocument;
    }

    public String getDiskFilenameMajorPunishmentpreeciding() {
        return diskFilenameMajorPunishmentpreeciding;
    }

    public void setDiskFilenameMajorPunishmentpreeciding(String diskFilenameMajorPunishmentpreeciding) {
        this.diskFilenameMajorPunishmentpreeciding = diskFilenameMajorPunishmentpreeciding;
    }

    public String getOriginalFileNameMinorPunishmentpreeciding() {
        return originalFileNameMinorPunishmentpreeciding;
    }

    public void setOriginalFileNameMinorPunishmentpreeciding(String originalFileNameMinorPunishmentpreeciding) {
        this.originalFileNameMinorPunishmentpreeciding = originalFileNameMinorPunishmentpreeciding;
    }

    public MultipartFile getMinorPunishmentpreecidingDocument() {
        return minorPunishmentpreecidingDocument;
    }

    public void setMinorPunishmentpreecidingDocument(MultipartFile minorPunishmentpreecidingDocument) {
        this.minorPunishmentpreecidingDocument = minorPunishmentpreecidingDocument;
    }

    public String getOriginalFileNameMinorPunishment() {
        return originalFileNameMinorPunishment;
    }

    public void setOriginalFileNameMinorPunishment(String originalFileNameMinorPunishment) {
        this.originalFileNameMinorPunishment = originalFileNameMinorPunishment;
    }

    public String getDiskFilenameMinorPunishment() {
        return diskFilenameMinorPunishment;
    }

    public void setDiskFilenameMinorPunishment(String diskFilenameMinorPunishment) {
        this.diskFilenameMinorPunishment = diskFilenameMinorPunishment;
    }

    public String getDeputationDetail() {
        return deputationDetail;
    }

    public void setDeputationDetail(String deputationDetail) {
        this.deputationDetail = deputationDetail;
    }

    public String getIfMajorPunishment() {
        return ifMajorPunishment;
    }

    public void setIfMajorPunishment(String ifMajorPunishment) {
        this.ifMajorPunishment = ifMajorPunishment;
    }

    public String getIfMinorPunishment() {
        return ifMinorPunishment;
    }

    public void setIfMinorPunishment(String ifMinorPunishment) {
        this.ifMinorPunishment = ifMinorPunishment;
    }

    public String getPenaltydetailsMinor() {
        return penaltydetailsMinor;
    }

    public void setPenaltydetailsMinor(String penaltydetailsMinor) {
        this.penaltydetailsMinor = penaltydetailsMinor;
    }

    public String getPenaltydetailsMojor() {
        return penaltydetailsMojor;
    }

    public void setPenaltydetailsMojor(String penaltydetailsMojor) {
        this.penaltydetailsMojor = penaltydetailsMojor;
    }

    public String getReligious() {
        return religious;
    }

    public void setReligious(String religious) {
        this.religious = religious;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIsPhotoAvailable() {
        return isPhotoAvailable;
    }

    public void setIsPhotoAvailable(String isPhotoAvailable) {
        this.isPhotoAvailable = isPhotoAvailable;
    }

    public String getCcrRefenrence() {
        return ccrRefenrence;
    }

    public void setCcrRefenrence(String ccrRefenrence) {
        this.ccrRefenrence = ccrRefenrence;
    }

    public String getPropertyStatementSubmittedifAny() {
        return propertyStatementSubmittedifAny;
    }

    public void setPropertyStatementSubmittedifAny(String propertyStatementSubmittedifAny) {
        this.propertyStatementSubmittedifAny = propertyStatementSubmittedifAny;
    }

    public String getDateofPropertySubmittedByOfficer() {
        return dateofPropertySubmittedByOfficer;
    }

    public void setDateofPropertySubmittedByOfficer(String dateofPropertySubmittedByOfficer) {
        this.dateofPropertySubmittedByOfficer = dateofPropertySubmittedByOfficer;
    }

    public String getDateofPropertySubmittedByHRMS() {
        return dateofPropertySubmittedByHRMS;
    }

    public void setDateofPropertySubmittedByHRMS(String dateofPropertySubmittedByHRMS) {
        this.dateofPropertySubmittedByHRMS = dateofPropertySubmittedByHRMS;
    }

    public String getPunishmentMajordgp() {
        return punishmentMajordgp;
    }

    public void setPunishmentMajordgp(String punishmentMajordgp) {
        this.punishmentMajordgp = punishmentMajordgp;
    }

    public String getPunishmentMinordgp() {
        return punishmentMinordgp;
    }

    public void setPunishmentMinordgp(String punishmentMinordgp) {
        this.punishmentMinordgp = punishmentMinordgp;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAcrGradingDetail() {
        return acrGradingDetail;
    }

    public void setAcrGradingDetail(String acrGradingDetail) {
        this.acrGradingDetail = acrGradingDetail;
    }

    public String getAcrCopyifAny() {
        return acrCopyifAny;
    }

    public void setAcrCopyifAny(String acrCopyifAny) {
        this.acrCopyifAny = acrCopyifAny;
    }

   
   

}
