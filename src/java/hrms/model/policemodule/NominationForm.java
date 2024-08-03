/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.policemodule;

import java.io.Serializable;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Surendra
 */
public class NominationForm implements Serializable {

    private String nominationMasterId = "";
    private String nominationDetailId = "";

    private String nominationFormId = "";
    private String officeCode = "";
    private String officeName = "";
    private String category = "";
    private String empId = "";
    private String gpfNo = "";
    private String fullname = "";
    private String fathersname = "";
    private String dob = "";
    private String qualification = "";
    private String homeDistrict = "";
    private String homedistrictName = "";
    private String postingUnintDoj = "";
    private String doeGov = "";
    private String jodInspector = "";
    private String dateForDISTraining = "";
    private String postingPlace = "";
    private String presentRank = "";

    private String yearinService = "";
    private String monthrinService = "";
    private String daysinService = "";

    private String periodParticularsTraining = "";

    private String officeOrderNo = "";
    private String passingTrainingdate = "";
    private String discDetails = "";
    private String rewardGSMark = "";
    private String rewardGSMarkPrior = "";
    private String rewardGSMarkDuring = "";
    private String rewardGSMarkFrom = "";

    private String rewardMoneyOher = "";
    private String rewardMoneyOherPrior = "";
    private String rewardMoneyOherDuring = "";
    private String rewardMoneyOherFrom = "";

    private String rewardMedals = "";
    private String rewardMedalsPrior = "";
    private String rewardMedalsDuring = "";
    private String rewardMedalsFrom = "";

    private String punishmentMajor = "";
    private String punishmentMajorPrior = "";
    private String punishmentMajorDuring = "";
    private String punishmentMajorFrom = "";

    private String punishmentMinor = "";
    private String punishmentMinorPrior = "";
    private String punishmentMinorDuring = "";
    private String punishmentMinorFrom = "";

    private String powerofDecesion = "";
    private String physicalFitness = "";
    private String remarksofCdmo = "";
    private String physicalFitnessDocumentStatus = "";

    private MultipartFile fitnessDocument = null;
    private String originalFileNameFitnessDocument = "";
    private String diskFilenameFitnessDocument = "";

    private String mentalFitness = "";
    private String honestyIntegrity = "";
    private String adverseIfany = "";
    private String dpcifany = "";

    private MultipartFile discDocument = null;
    private String originalFileName = "";
    private String diskFilename = "";

    private MultipartFile discCCROll = null;
    private String originalFileNameCCROll = "";
    private String diskFilenameCCROll = "";

    private MultipartFile documentServingCopy = null;
    private String originalFileNameServing = "";
    private String diskFilenameServing = "";

    private MultipartFile serviceBookCopy = null;
    private String originalFileNameSB = "";
    private String diskFilenameSB = "";

    private MultipartFile punishmentCopy = null;
    private String originalFileNamePunishment = "";
    private String diskFilenamePunishment = "";

    private String dateofServing = "";
    private String dateofServingifAny = "";
    private String remarksNominatingProfessional = "";
    private String remarksNominationGeneral = "";
    private String remarkRecommendation = "";
    private String recommendStatus = "";

    private String remarkRecommendationDG;

    private String sltpostName = "";
    private String sltNominationForPost = "";

    private String nominationtype = "";

    private String accomplishDetail = "";
    private String declarationAccept = "";

    private MultipartFile accomplishCopy = null;
    private String originalFileNameAccomplish = "";
    private String diskFilenameAccomplish = "";

    private MultipartFile casteCertificate = null;
    private String originalFileNamecasteCertificate = "";
    private String diskFilenamecasteCertificate = "";

    private String viewOfRecommendingAuthority;
    private String remarksOfCSB;
    private String cadreName;
    private String servingChargeOfCaseDate;

    private String rewardMedalId;
    private String remarkRecommendationByRange;

    private String gender;
    private String initialRank;
    private String appointmentMode;
    private String isAnyPromotionearlier;
    private String promotionalRankName;
    private String doeInpresentRankDist;
    private String presentRankDistName;
    private String isCompletedRegulartenYears;
    private String isContempletedAsOnDate;
    private String doeinpromotional;
    private String currentDesignation;

    private MultipartFile contempletedDocument = null;
    private String originalFileNameContempletedDocument = "";
    private String diskFilenameContempletedDocument = "";

    private String postingOrDeputationInOtherDistrict;
    private String postingOrDeputationInOtherDistrictFromDate;
    private String postingOrDeputationInOtherDistrictToDate;
    private int postingOrDeputationInOtherDistrictId;

    private List differentDistrictandEstablishmentList;
    private List differentDisciplinaryProceedingList;

    private MultipartFile relevantServiceBookCopy = null;
    private String originalFileNameRelevantSB = "";
    private String diskFileNameRelevantSB = "";
    
    private MultipartFile MajorPunishmentForGRDDocument = null;
    private String originalFileNameMajorPunishmentForGRD = "";
    private String diskFileNameMajorPunishmentForGRD = "";

    private MultipartFile MinorPunishmentForGRDDocument = null;
    private String originalFileNameMinorPunishmentForGRD = "";
    private String diskFileNameMinorPunishmentForGRD = "";
    
    private String recommendStatusDistrict;
    private String viewOfrecommendStatusDistrict;
    
    private MultipartFile willingnessCertificateDocument = null;
    private String originalFileNamewillingnessCertificateForSrclerk = "";
    private String diskFileNamewillingnessCertificateForSrclerk = "";
    
    private String isPassPrelimilaryExam;
    private MultipartFile prelimilaryExamDocument = null;
    private String originalFileNameprelimilaryExamDocument = "";
    private String diskFileNameprelimilaryExamDocument = "";
    
    private String powerOfDecissionMaking;
    private String adverseDetail;
    
    //private String[] disciplinaryproceedingDetail;
    //private MultipartFile[] disciplinaryproceedingfile = null;
    private String disciplinaryproceedingDetail;
    private MultipartFile disciplinaryproceedingfile = null;
    private String originalFileNamefordisciplinaryProceeding = "";
    private String diskFileNamefordisciplinaryProceeding = "";
    private String fileTypefordisciplinaryProceeding = "";
    
    private String proceedingDetail;
    private int proceedingDetailId;
    private byte[] filecontent = null;
    private String contentType;
    
    private String passingPlaceForConstableTraining;
    private String passingYearFromForConstableTraining = "";
    private String passingYearToForConstableTraining = "";
    
    private String orderNopassingForConstableTraining;
    private String orderDatepassingForConstableTraining = "";
    
    private String fiscalyear;
    private String punishmentDetail;
    private String rankJoiningovservice;
    
    private String ccrollsDetail;
    private String criminalcasePresentStatusifAny;
    
    private String presentCriminalStatusDetail;
    private MultipartFile presentCriminalStatusDetailfile = null;
    private String originalFileNameforpresentCriminalStatus = "";
    private String diskFileNameforpresentCriminalStatus = "";
    private String fileTypeforpresentCriminalStatusDetail = "";
    private String dateforpresentCriminalStatus = "";
    
    private String propertyStatementSubmittedifAny;
    private String dateofPropertySubmittedByOfficer;
    private String dateofPropertySubmittedByHRMS;
    private String orderNoForDISTraining;
    private String servingChargeDetail;
    
    private int totalpunishment1;
    private int totalpunishment2;
    private int totalpunishment3;
    
    private String currentRankName;
    private String promoteToRankName;
    private String isActive;
    private int rankMapId;
    
    private String yearinServiceFromPresentRank;
    private String monthrinServiceFromPresentRank;
    private String daysinServiceFromPresentRank;
    private String deptExamPassifany;
    private String gradeSerialNo;
    
    private String havePassedTrainingAsi;
    private String orderNoForPassedTrainingAsi;
    private String dateOfPassedTrainingAsi;
    private String battalionWorkExperienceifany;
    
    

    public String getNominationMasterId() {
        return nominationMasterId;
    }

    public void setNominationMasterId(String nominationMasterId) {
        this.nominationMasterId = nominationMasterId;
    }

    public String getNominationDetailId() {
        return nominationDetailId;
    }

    public void setNominationDetailId(String nominationDetailId) {
        this.nominationDetailId = nominationDetailId;
    }

    public String getNominationFormId() {
        return nominationFormId;
    }

    public void setNominationFormId(String nominationFormId) {
        this.nominationFormId = nominationFormId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getGpfNo() {
        return gpfNo;
    }

    public void setGpfNo(String gpfNo) {
        this.gpfNo = gpfNo;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFathersname() {
        return fathersname;
    }

    public void setFathersname(String fathersname) {
        this.fathersname = fathersname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getHomeDistrict() {
        return homeDistrict;
    }

    public void setHomeDistrict(String homeDistrict) {
        this.homeDistrict = homeDistrict;
    }

    public String getPostingUnintDoj() {
        return postingUnintDoj;
    }

    public void setPostingUnintDoj(String postingUnintDoj) {
        this.postingUnintDoj = postingUnintDoj;
    }

    public String getDoeGov() {
        return doeGov;
    }

    public void setDoeGov(String doeGov) {
        this.doeGov = doeGov;
    }

    public String getJodInspector() {
        return jodInspector;
    }

    public void setJodInspector(String jodInspector) {
        this.jodInspector = jodInspector;
    }

    public String getDateForDISTraining() {
        return dateForDISTraining;
    }

    public void setDateForDISTraining(String dateForDISTraining) {
        this.dateForDISTraining = dateForDISTraining;
    }

    public String getPeriodParticularsTraining() {
        return periodParticularsTraining;
    }

    public void setPeriodParticularsTraining(String periodParticularsTraining) {
        this.periodParticularsTraining = periodParticularsTraining;
    }

    public String getRewardGSMark() {
        return rewardGSMark;
    }

    public void setRewardGSMark(String rewardGSMark) {
        this.rewardGSMark = rewardGSMark;
    }

    public String getRewardGSMarkPrior() {
        return rewardGSMarkPrior;
    }

    public void setRewardGSMarkPrior(String rewardGSMarkPrior) {
        this.rewardGSMarkPrior = rewardGSMarkPrior;
    }

    public String getRewardGSMarkDuring() {
        return rewardGSMarkDuring;
    }

    public void setRewardGSMarkDuring(String rewardGSMarkDuring) {
        this.rewardGSMarkDuring = rewardGSMarkDuring;
    }

    public String getRewardGSMarkFrom() {
        return rewardGSMarkFrom;
    }

    public void setRewardGSMarkFrom(String rewardGSMarkFrom) {
        this.rewardGSMarkFrom = rewardGSMarkFrom;
    }

    public String getRewardMoneyOher() {
        return rewardMoneyOher;
    }

    public void setRewardMoneyOher(String rewardMoneyOher) {
        this.rewardMoneyOher = rewardMoneyOher;
    }

    public String getRewardMoneyOherPrior() {
        return rewardMoneyOherPrior;
    }

    public void setRewardMoneyOherPrior(String rewardMoneyOherPrior) {
        this.rewardMoneyOherPrior = rewardMoneyOherPrior;
    }

    public String getRewardMoneyOherDuring() {
        return rewardMoneyOherDuring;
    }

    public void setRewardMoneyOherDuring(String rewardMoneyOherDuring) {
        this.rewardMoneyOherDuring = rewardMoneyOherDuring;
    }

    public String getRewardMoneyOherFrom() {
        return rewardMoneyOherFrom;
    }

    public void setRewardMoneyOherFrom(String rewardMoneyOherFrom) {
        this.rewardMoneyOherFrom = rewardMoneyOherFrom;
    }

    public String getRewardMedals() {
        return rewardMedals;
    }

    public void setRewardMedals(String rewardMedals) {
        this.rewardMedals = rewardMedals;
    }

    public String getRewardMedalsPrior() {
        return rewardMedalsPrior;
    }

    public void setRewardMedalsPrior(String rewardMedalsPrior) {
        this.rewardMedalsPrior = rewardMedalsPrior;
    }

    public String getRewardMedalsDuring() {
        return rewardMedalsDuring;
    }

    public void setRewardMedalsDuring(String rewardMedalsDuring) {
        this.rewardMedalsDuring = rewardMedalsDuring;
    }

    public String getRewardMedalsFrom() {
        return rewardMedalsFrom;
    }

    public void setRewardMedalsFrom(String rewardMedalsFrom) {
        this.rewardMedalsFrom = rewardMedalsFrom;
    }

    public String getPunishmentMajor() {
        return punishmentMajor;
    }

    public void setPunishmentMajor(String punishmentMajor) {
        this.punishmentMajor = punishmentMajor;
    }

    public String getPunishmentMajorPrior() {
        return punishmentMajorPrior;
    }

    public void setPunishmentMajorPrior(String punishmentMajorPrior) {
        this.punishmentMajorPrior = punishmentMajorPrior;
    }

    public String getPunishmentMajorDuring() {
        return punishmentMajorDuring;
    }

    public void setPunishmentMajorDuring(String punishmentMajorDuring) {
        this.punishmentMajorDuring = punishmentMajorDuring;
    }

    public String getPunishmentMajorFrom() {
        return punishmentMajorFrom;
    }

    public void setPunishmentMajorFrom(String punishmentMajorFrom) {
        this.punishmentMajorFrom = punishmentMajorFrom;
    }

    public String getPunishmentMinor() {
        return punishmentMinor;
    }

    public void setPunishmentMinor(String punishmentMinor) {
        this.punishmentMinor = punishmentMinor;
    }

    public String getPunishmentMinorPrior() {
        return punishmentMinorPrior;
    }

    public void setPunishmentMinorPrior(String punishmentMinorPrior) {
        this.punishmentMinorPrior = punishmentMinorPrior;
    }

    public String getPunishmentMinorDuring() {
        return punishmentMinorDuring;
    }

    public void setPunishmentMinorDuring(String punishmentMinorDuring) {
        this.punishmentMinorDuring = punishmentMinorDuring;
    }

    public String getPunishmentMinorFrom() {
        return punishmentMinorFrom;
    }

    public void setPunishmentMinorFrom(String punishmentMinorFrom) {
        this.punishmentMinorFrom = punishmentMinorFrom;
    }

    public String getPowerofDecesion() {
        return powerofDecesion;
    }

    public void setPowerofDecesion(String powerofDecesion) {
        this.powerofDecesion = powerofDecesion;
    }

    public String getPhysicalFitness() {
        return physicalFitness;
    }

    public void setPhysicalFitness(String physicalFitness) {
        this.physicalFitness = physicalFitness;
    }

    public String getMentalFitness() {
        return mentalFitness;
    }

    public void setMentalFitness(String mentalFitness) {
        this.mentalFitness = mentalFitness;
    }

    public String getHonestyIntegrity() {
        return honestyIntegrity;
    }

    public void setHonestyIntegrity(String honestyIntegrity) {
        this.honestyIntegrity = honestyIntegrity;
    }

    public String getAdverseIfany() {
        return adverseIfany;
    }

    public void setAdverseIfany(String adverseIfany) {
        this.adverseIfany = adverseIfany;
    }

    public String getDpcifany() {
        return dpcifany;
    }

    public void setDpcifany(String dpcifany) {
        this.dpcifany = dpcifany;
    }

    public String getDateofServing() {
        return dateofServing;
    }

    public void setDateofServing(String dateofServing) {
        this.dateofServing = dateofServing;
    }

    public String getRemarksNominatingProfessional() {
        return remarksNominatingProfessional;
    }

    public void setRemarksNominatingProfessional(String remarksNominatingProfessional) {
        this.remarksNominatingProfessional = remarksNominatingProfessional;
    }

    public String getRemarksNominationGeneral() {
        return remarksNominationGeneral;
    }

    public void setRemarksNominationGeneral(String remarksNominationGeneral) {
        this.remarksNominationGeneral = remarksNominationGeneral;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public MultipartFile getDiscDocument() {
        return discDocument;
    }

    public void setDiscDocument(MultipartFile discDocument) {
        this.discDocument = discDocument;
    }

    public String getHomedistrictName() {
        return homedistrictName;
    }

    public void setHomedistrictName(String homedistrictName) {
        this.homedistrictName = homedistrictName;
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

    public String getDateofServingifAny() {
        return dateofServingifAny;
    }

    public void setDateofServingifAny(String dateofServingifAny) {
        this.dateofServingifAny = dateofServingifAny;
    }

    public String getRemarkRecommendation() {
        return remarkRecommendation;
    }

    public void setRemarkRecommendation(String remarkRecommendation) {
        this.remarkRecommendation = remarkRecommendation;
    }

    public String getRemarkRecommendationDG() {
        return remarkRecommendationDG;
    }

    public void setRemarkRecommendationDG(String remarkRecommendationDG) {
        this.remarkRecommendationDG = remarkRecommendationDG;
    }

    public String getYearinService() {
        return yearinService;
    }

    public void setYearinService(String yearinService) {
        this.yearinService = yearinService;
    }

    public String getMonthrinService() {
        return monthrinService;
    }

    public void setMonthrinService(String monthrinService) {
        this.monthrinService = monthrinService;
    }

    public String getDaysinService() {
        return daysinService;
    }

    public void setDaysinService(String daysinService) {
        this.daysinService = daysinService;
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

    public MultipartFile getDocumentServingCopy() {
        return documentServingCopy;
    }

    public void setDocumentServingCopy(MultipartFile documentServingCopy) {
        this.documentServingCopy = documentServingCopy;
    }

    public String getOriginalFileNameServing() {
        return originalFileNameServing;
    }

    public void setOriginalFileNameServing(String originalFileNameServing) {
        this.originalFileNameServing = originalFileNameServing;
    }

    public String getDiskFilenameServing() {
        return diskFilenameServing;
    }

    public void setDiskFilenameServing(String diskFilenameServing) {
        this.diskFilenameServing = diskFilenameServing;
    }

    public String getRecommendStatus() {
        return recommendStatus;
    }

    public void setRecommendStatus(String recommendStatus) {
        this.recommendStatus = recommendStatus;
    }

    public String getSltpostName() {
        return sltpostName;
    }

    public void setSltpostName(String sltpostName) {
        this.sltpostName = sltpostName;
    }

    public String getSltNominationForPost() {
        return sltNominationForPost;
    }

    public void setSltNominationForPost(String sltNominationForPost) {
        this.sltNominationForPost = sltNominationForPost;
    }

    public String getNominationtype() {
        return nominationtype;
    }

    public void setNominationtype(String nominationtype) {
        this.nominationtype = nominationtype;
    }

    public String getAccomplishDetail() {
        return accomplishDetail;
    }

    public void setAccomplishDetail(String accomplishDetail) {
        this.accomplishDetail = accomplishDetail;
    }

    public MultipartFile getAccomplishCopy() {
        return accomplishCopy;
    }

    public void setAccomplishCopy(MultipartFile accomplishCopy) {
        this.accomplishCopy = accomplishCopy;
    }

    public String getOriginalFileNameAccomplish() {
        return originalFileNameAccomplish;
    }

    public void setOriginalFileNameAccomplish(String originalFileNameAccomplish) {
        this.originalFileNameAccomplish = originalFileNameAccomplish;
    }

    public String getDiskFilenameAccomplish() {
        return diskFilenameAccomplish;
    }

    public void setDiskFilenameAccomplish(String diskFilenameAccomplish) {
        this.diskFilenameAccomplish = diskFilenameAccomplish;
    }

    public String getPhysicalFitnessDocumentStatus() {
        return physicalFitnessDocumentStatus;
    }

    public void setPhysicalFitnessDocumentStatus(String physicalFitnessDocumentStatus) {
        this.physicalFitnessDocumentStatus = physicalFitnessDocumentStatus;
    }

    public MultipartFile getFitnessDocument() {
        return fitnessDocument;
    }

    public void setFitnessDocument(MultipartFile fitnessDocument) {
        this.fitnessDocument = fitnessDocument;
    }

    public String getOriginalFileNameFitnessDocument() {
        return originalFileNameFitnessDocument;
    }

    public void setOriginalFileNameFitnessDocument(String originalFileNameFitnessDocument) {
        this.originalFileNameFitnessDocument = originalFileNameFitnessDocument;
    }

    public String getDiskFilenameFitnessDocument() {
        return diskFilenameFitnessDocument;
    }

    public void setDiskFilenameFitnessDocument(String diskFilenameFitnessDocument) {
        this.diskFilenameFitnessDocument = diskFilenameFitnessDocument;
    }

    public String getDeclarationAccept() {
        return declarationAccept;
    }

    public void setDeclarationAccept(String declarationAccept) {
        this.declarationAccept = declarationAccept;
    }

    public String getPostingPlace() {
        return postingPlace;
    }

    public void setPostingPlace(String postingPlace) {
        this.postingPlace = postingPlace;
    }

    public String getPresentRank() {
        return presentRank;
    }

    public void setPresentRank(String presentRank) {
        this.presentRank = presentRank;
    }

    public String getOfficeOrderNo() {
        return officeOrderNo;
    }

    public void setOfficeOrderNo(String officeOrderNo) {
        this.officeOrderNo = officeOrderNo;
    }

    public String getPassingTrainingdate() {
        return passingTrainingdate;
    }

    public void setPassingTrainingdate(String passingTrainingdate) {
        this.passingTrainingdate = passingTrainingdate;
    }

    public String getDiscDetails() {
        return discDetails;
    }

    public void setDiscDetails(String discDetails) {
        this.discDetails = discDetails;
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

    public MultipartFile getPunishmentCopy() {
        return punishmentCopy;
    }

    public void setPunishmentCopy(MultipartFile punishmentCopy) {
        this.punishmentCopy = punishmentCopy;
    }

    public String getOriginalFileNamePunishment() {
        return originalFileNamePunishment;
    }

    public void setOriginalFileNamePunishment(String originalFileNamePunishment) {
        this.originalFileNamePunishment = originalFileNamePunishment;
    }

    public String getDiskFilenamePunishment() {
        return diskFilenamePunishment;
    }

    public void setDiskFilenamePunishment(String diskFilenamePunishment) {
        this.diskFilenamePunishment = diskFilenamePunishment;
    }

    public String getRemarksofCdmo() {
        return remarksofCdmo;
    }

    public void setRemarksofCdmo(String remarksofCdmo) {
        this.remarksofCdmo = remarksofCdmo;
    }

    public MultipartFile getCasteCertificate() {
        return casteCertificate;
    }

    public void setCasteCertificate(MultipartFile casteCertificate) {
        this.casteCertificate = casteCertificate;
    }

    public String getOriginalFileNamecasteCertificate() {
        return originalFileNamecasteCertificate;
    }

    public void setOriginalFileNamecasteCertificate(String originalFileNamecasteCertificate) {
        this.originalFileNamecasteCertificate = originalFileNamecasteCertificate;
    }

    public String getDiskFilenamecasteCertificate() {
        return diskFilenamecasteCertificate;
    }

    public void setDiskFilenamecasteCertificate(String diskFilenamecasteCertificate) {
        this.diskFilenamecasteCertificate = diskFilenamecasteCertificate;
    }

    public String getViewOfRecommendingAuthority() {
        return viewOfRecommendingAuthority;
    }

    public void setViewOfRecommendingAuthority(String viewOfRecommendingAuthority) {
        this.viewOfRecommendingAuthority = viewOfRecommendingAuthority;
    }

    public String getRemarksOfCSB() {
        return remarksOfCSB;
    }

    public void setRemarksOfCSB(String remarksOfCSB) {
        this.remarksOfCSB = remarksOfCSB;
    }

    public String getCadreName() {
        return cadreName;
    }

    public void setCadreName(String cadreName) {
        this.cadreName = cadreName;
    }

    public String getServingChargeOfCaseDate() {
        return servingChargeOfCaseDate;
    }

    public void setServingChargeOfCaseDate(String servingChargeOfCaseDate) {
        this.servingChargeOfCaseDate = servingChargeOfCaseDate;
    }

    public String getRewardMedalId() {
        return rewardMedalId;
    }

    public void setRewardMedalId(String rewardMedalId) {
        this.rewardMedalId = rewardMedalId;
    }

    public String getRemarkRecommendationByRange() {
        return remarkRecommendationByRange;
    }

    public void setRemarkRecommendationByRange(String remarkRecommendationByRange) {
        this.remarkRecommendationByRange = remarkRecommendationByRange;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getInitialRank() {
        return initialRank;
    }

    public void setInitialRank(String initialRank) {
        this.initialRank = initialRank;
    }

    public String getAppointmentMode() {
        return appointmentMode;
    }

    public void setAppointmentMode(String appointmentMode) {
        this.appointmentMode = appointmentMode;
    }

    public String getIsAnyPromotionearlier() {
        return isAnyPromotionearlier;
    }

    public void setIsAnyPromotionearlier(String isAnyPromotionearlier) {
        this.isAnyPromotionearlier = isAnyPromotionearlier;
    }

    public String getPromotionalRankName() {
        return promotionalRankName;
    }

    public void setPromotionalRankName(String promotionalRankName) {
        this.promotionalRankName = promotionalRankName;
    }

    public String getDoeInpresentRankDist() {
        return doeInpresentRankDist;
    }

    public void setDoeInpresentRankDist(String doeInpresentRankDist) {
        this.doeInpresentRankDist = doeInpresentRankDist;
    }

    public String getPresentRankDistName() {
        return presentRankDistName;
    }

    public void setPresentRankDistName(String presentRankDistName) {
        this.presentRankDistName = presentRankDistName;
    }

    public String getIsCompletedRegulartenYears() {
        return isCompletedRegulartenYears;
    }

    public void setIsCompletedRegulartenYears(String isCompletedRegular10Years) {
        this.isCompletedRegulartenYears = isCompletedRegular10Years;
    }

    public String getIsContempletedAsOnDate() {
        return isContempletedAsOnDate;
    }

    public void setIsContempletedAsOnDate(String isContempletedAsOnDate) {
        this.isContempletedAsOnDate = isContempletedAsOnDate;
    }

    public String getDoeinpromotional() {
        return doeinpromotional;
    }

    public void setDoeinpromotional(String doeinpromotional) {
        this.doeinpromotional = doeinpromotional;
    }

    public String getCurrentDesignation() {
        return currentDesignation;
    }

    public void setCurrentDesignation(String currentDesignation) {
        this.currentDesignation = currentDesignation;
    }

    public MultipartFile getContempletedDocument() {
        return contempletedDocument;
    }

    public void setContempletedDocument(MultipartFile contempletedDocument) {
        this.contempletedDocument = contempletedDocument;
    }

    

    public String getOriginalFileNameContempletedDocument() {
        return originalFileNameContempletedDocument;
    }

    public void setOriginalFileNameContempletedDocument(String originalFileNameContempletedDocument) {
        this.originalFileNameContempletedDocument = originalFileNameContempletedDocument;
    }

    public String getDiskFilenameContempletedDocument() {
        return diskFilenameContempletedDocument;
    }

    public void setDiskFilenameContempletedDocument(String diskFilenameContempletedDocument) {
        this.diskFilenameContempletedDocument = diskFilenameContempletedDocument;
    }

    public String getPostingOrDeputationInOtherDistrict() {
        return postingOrDeputationInOtherDistrict;
    }

    public void setPostingOrDeputationInOtherDistrict(String postingOrDeputationInOtherDistrict) {
        this.postingOrDeputationInOtherDistrict = postingOrDeputationInOtherDistrict;
    }

    public String getPostingOrDeputationInOtherDistrictFromDate() {
        return postingOrDeputationInOtherDistrictFromDate;
    }

    public void setPostingOrDeputationInOtherDistrictFromDate(String postingOrDeputationInOtherDistrictFromDate) {
        this.postingOrDeputationInOtherDistrictFromDate = postingOrDeputationInOtherDistrictFromDate;
    }

    public String getPostingOrDeputationInOtherDistrictToDate() {
        return postingOrDeputationInOtherDistrictToDate;
    }

    public void setPostingOrDeputationInOtherDistrictToDate(String postingOrDeputationInOtherDistrictToDate) {
        this.postingOrDeputationInOtherDistrictToDate = postingOrDeputationInOtherDistrictToDate;
    }

    public int getPostingOrDeputationInOtherDistrictId() {
        return postingOrDeputationInOtherDistrictId;
    }

    public void setPostingOrDeputationInOtherDistrictId(int postingOrDeputationInOtherDistrictId) {
        this.postingOrDeputationInOtherDistrictId = postingOrDeputationInOtherDistrictId;
    }

    public List getDifferentDistrictandEstablishmentList() {
        return differentDistrictandEstablishmentList;
    }

    public void setDifferentDistrictandEstablishmentList(List differentDistrictandEstablishmentList) {
        this.differentDistrictandEstablishmentList = differentDistrictandEstablishmentList;
    }

    public List getDifferentDisciplinaryProceedingList() {
        return differentDisciplinaryProceedingList;
    }

    public void setDifferentDisciplinaryProceedingList(List differentDisciplinaryProceedingList) {
        this.differentDisciplinaryProceedingList = differentDisciplinaryProceedingList;
    }

    public MultipartFile getRelevantServiceBookCopy() {
        return relevantServiceBookCopy;
    }

    public void setRelevantServiceBookCopy(MultipartFile relevantServiceBookCopy) {
        this.relevantServiceBookCopy = relevantServiceBookCopy;
    }

    public String getOriginalFileNameRelevantSB() {
        return originalFileNameRelevantSB;
    }

    public void setOriginalFileNameRelevantSB(String originalFileNameRelevantSB) {
        this.originalFileNameRelevantSB = originalFileNameRelevantSB;
    }

    public String getDiskFileNameRelevantSB() {
        return diskFileNameRelevantSB;
    }

    public void setDiskFileNameRelevantSB(String diskFileNameRelevantSB) {
        this.diskFileNameRelevantSB = diskFileNameRelevantSB;
    }

    public MultipartFile getMajorPunishmentForGRDDocument() {
        return MajorPunishmentForGRDDocument;
    }

    public void setMajorPunishmentForGRDDocument(MultipartFile MajorPunishmentForGRDDocument) {
        this.MajorPunishmentForGRDDocument = MajorPunishmentForGRDDocument;
    }

    public String getOriginalFileNameMajorPunishmentForGRD() {
        return originalFileNameMajorPunishmentForGRD;
    }

    public void setOriginalFileNameMajorPunishmentForGRD(String originalFileNameMajorPunishmentForGRD) {
        this.originalFileNameMajorPunishmentForGRD = originalFileNameMajorPunishmentForGRD;
    }

    public String getDiskFileNameMajorPunishmentForGRD() {
        return diskFileNameMajorPunishmentForGRD;
    }

    public void setDiskFileNameMajorPunishmentForGRD(String diskFileNameMajorPunishmentForGRD) {
        this.diskFileNameMajorPunishmentForGRD = diskFileNameMajorPunishmentForGRD;
    }

    public MultipartFile getMinorPunishmentForGRDDocument() {
        return MinorPunishmentForGRDDocument;
    }

    public void setMinorPunishmentForGRDDocument(MultipartFile MinorPunishmentForGRDDocument) {
        this.MinorPunishmentForGRDDocument = MinorPunishmentForGRDDocument;
    }

    public String getOriginalFileNameMinorPunishmentForGRD() {
        return originalFileNameMinorPunishmentForGRD;
    }

    public void setOriginalFileNameMinorPunishmentForGRD(String originalFileNameMinorPunishmentForGRD) {
        this.originalFileNameMinorPunishmentForGRD = originalFileNameMinorPunishmentForGRD;
    }

    public String getDiskFileNameMinorPunishmentForGRD() {
        return diskFileNameMinorPunishmentForGRD;
    }

    public void setDiskFileNameMinorPunishmentForGRD(String diskFileNameMinorPunishmentForGRD) {
        this.diskFileNameMinorPunishmentForGRD = diskFileNameMinorPunishmentForGRD;
    }

    public String getRecommendStatusDistrict() {
        return recommendStatusDistrict;
    }

    public void setRecommendStatusDistrict(String recommendStatusDistrict) {
        this.recommendStatusDistrict = recommendStatusDistrict;
    }

    public String getViewOfrecommendStatusDistrict() {
        return viewOfrecommendStatusDistrict;
    }

    public void setViewOfrecommendStatusDistrict(String viewOfrecommendStatusDistrict) {
        this.viewOfrecommendStatusDistrict = viewOfrecommendStatusDistrict;
    }
    

    public MultipartFile getWillingnessCertificateDocument() {
        return willingnessCertificateDocument;
    }

    public void setWillingnessCertificateDocument(MultipartFile willingnessCertificateDocument) {
        this.willingnessCertificateDocument = willingnessCertificateDocument;
    }

    public String getOriginalFileNamewillingnessCertificateForSrclerk() {
        return originalFileNamewillingnessCertificateForSrclerk;
    }

    public void setOriginalFileNamewillingnessCertificateForSrclerk(String originalFileNamewillingnessCertificateForSrclerk) {
        this.originalFileNamewillingnessCertificateForSrclerk = originalFileNamewillingnessCertificateForSrclerk;
    }

    public String getDiskFileNamewillingnessCertificateForSrclerk() {
        return diskFileNamewillingnessCertificateForSrclerk;
    }

    public void setDiskFileNamewillingnessCertificateForSrclerk(String diskFileNamewillingnessCertificateForSrclerk) {
        this.diskFileNamewillingnessCertificateForSrclerk = diskFileNamewillingnessCertificateForSrclerk;
    }

    public String getIsPassPrelimilaryExam() {
        return isPassPrelimilaryExam;
    }

    public void setIsPassPrelimilaryExam(String isPassPrelimilaryExam) {
        this.isPassPrelimilaryExam = isPassPrelimilaryExam;
    }

    public MultipartFile getPrelimilaryExamDocument() {
        return prelimilaryExamDocument;
    }

    public void setPrelimilaryExamDocument(MultipartFile prelimilaryExamDocument) {
        this.prelimilaryExamDocument = prelimilaryExamDocument;
    }

    public String getOriginalFileNameprelimilaryExamDocument() {
        return originalFileNameprelimilaryExamDocument;
    }

    public void setOriginalFileNameprelimilaryExamDocument(String originalFileNameprelimilaryExamDocument) {
        this.originalFileNameprelimilaryExamDocument = originalFileNameprelimilaryExamDocument;
    }

    public String getDiskFileNameprelimilaryExamDocument() {
        return diskFileNameprelimilaryExamDocument;
    }

    public void setDiskFileNameprelimilaryExamDocument(String diskFileNameprelimilaryExamDocument) {
        this.diskFileNameprelimilaryExamDocument = diskFileNameprelimilaryExamDocument;
    }

    public String getPowerOfDecissionMaking() {
        return powerOfDecissionMaking;
    }

    public void setPowerOfDecissionMaking(String powerOfDecissionMaking) {
        this.powerOfDecissionMaking = powerOfDecissionMaking;
    }

    public String getAdverseDetail() {
        return adverseDetail;
    }

    public void setAdverseDetail(String adverseDetail) {
        this.adverseDetail = adverseDetail;
    }

    public String getDisciplinaryproceedingDetail() {
        return disciplinaryproceedingDetail;
    }

    public void setDisciplinaryproceedingDetail(String disciplinaryproceedingDetail) {
        this.disciplinaryproceedingDetail = disciplinaryproceedingDetail;
    }

    public MultipartFile getDisciplinaryproceedingfile() {
        return disciplinaryproceedingfile;
    }

    public void setDisciplinaryproceedingfile(MultipartFile disciplinaryproceedingfile) {
        this.disciplinaryproceedingfile = disciplinaryproceedingfile;
    }

    /*public String[] getDisciplinaryproceedingDetail() {
        return disciplinaryproceedingDetail;
    }

    public void setDisciplinaryproceedingDetail(String[] disciplinaryproceedingDetail) {
        this.disciplinaryproceedingDetail = disciplinaryproceedingDetail;
    }

    public MultipartFile[] getDisciplinaryproceedingfile() {
        return disciplinaryproceedingfile;
    }

    public void setDisciplinaryproceedingfile(MultipartFile[] disciplinaryproceedingfile) {
        this.disciplinaryproceedingfile = disciplinaryproceedingfile;
    } */

    public String getOriginalFileNamefordisciplinaryProceeding() {
        return originalFileNamefordisciplinaryProceeding;
    }

    public void setOriginalFileNamefordisciplinaryProceeding(String originalFileNamefordisciplinaryProceeding) {
        this.originalFileNamefordisciplinaryProceeding = originalFileNamefordisciplinaryProceeding;
    }

    public String getDiskFileNamefordisciplinaryProceeding() {
        return diskFileNamefordisciplinaryProceeding;
    }

    public void setDiskFileNamefordisciplinaryProceeding(String diskFileNamefordisciplinaryProceeding) {
        this.diskFileNamefordisciplinaryProceeding = diskFileNamefordisciplinaryProceeding;
    }

    public String getFileTypefordisciplinaryProceeding() {
        return fileTypefordisciplinaryProceeding;
    }

    public void setFileTypefordisciplinaryProceeding(String fileTypefordisciplinaryProceeding) {
        this.fileTypefordisciplinaryProceeding = fileTypefordisciplinaryProceeding;
    }

    public String getProceedingDetail() {
        return proceedingDetail;
    }

    public void setProceedingDetail(String proceedingDetail) {
        this.proceedingDetail = proceedingDetail;
    }

    public int getProceedingDetailId() {
        return proceedingDetailId;
    }

    public void setProceedingDetailId(int proceedingDetailId) {
        this.proceedingDetailId = proceedingDetailId;
    }

    public byte[] getFilecontent() {
        return filecontent;
    }

    public void setFilecontent(byte[] filecontent) {
        this.filecontent = filecontent;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFiscalyear() {
        return fiscalyear;
    }

    public void setFiscalyear(String fiscalyear) {
        this.fiscalyear = fiscalyear;
    }

    public String getPassingPlaceForConstableTraining() {
        return passingPlaceForConstableTraining;
    }

    public void setPassingPlaceForConstableTraining(String passingPlaceForConstableTraining) {
        this.passingPlaceForConstableTraining = passingPlaceForConstableTraining;
    }

    public String getPassingYearFromForConstableTraining() {
        return passingYearFromForConstableTraining;
    }

    public void setPassingYearFromForConstableTraining(String passingYearFromForConstableTraining) {
        this.passingYearFromForConstableTraining = passingYearFromForConstableTraining;
    }

    public String getPassingYearToForConstableTraining() {
        return passingYearToForConstableTraining;
    }

    public void setPassingYearToForConstableTraining(String passingYearToForConstableTraining) {
        this.passingYearToForConstableTraining = passingYearToForConstableTraining;
    }

    public String getOrderNopassingForConstableTraining() {
        return orderNopassingForConstableTraining;
    }

    public void setOrderNopassingForConstableTraining(String orderNopassingForConstableTraining) {
        this.orderNopassingForConstableTraining = orderNopassingForConstableTraining;
    }

    public String getOrderDatepassingForConstableTraining() {
        return orderDatepassingForConstableTraining;
    }

    public void setOrderDatepassingForConstableTraining(String orderDatepassingForConstableTraining) {
        this.orderDatepassingForConstableTraining = orderDatepassingForConstableTraining;
    }

    public String getPunishmentDetail() {
        return punishmentDetail;
    }

    public void setPunishmentDetail(String punishmentDetail) {
        this.punishmentDetail = punishmentDetail;
    }

    public String getRankJoiningovservice() {
        return rankJoiningovservice;
    }

    public void setRankJoiningovservice(String rankJoiningovservice) {
        this.rankJoiningovservice = rankJoiningovservice;
    }

    public String getCcrollsDetail() {
        return ccrollsDetail;
    }

    public void setCcrollsDetail(String ccrollsDetail) {
        this.ccrollsDetail = ccrollsDetail;
    }

    public String getCriminalcasePresentStatusifAny() {
        return criminalcasePresentStatusifAny;
    }

    public void setCriminalcasePresentStatusifAny(String criminalcasePresentStatusifAny) {
        this.criminalcasePresentStatusifAny = criminalcasePresentStatusifAny;
    }

    public String getPresentCriminalStatusDetail() {
        return presentCriminalStatusDetail;
    }

    public void setPresentCriminalStatusDetail(String presentCriminalStatusDetail) {
        this.presentCriminalStatusDetail = presentCriminalStatusDetail;
    }

    public MultipartFile getPresentCriminalStatusDetailfile() {
        return presentCriminalStatusDetailfile;
    }

    public void setPresentCriminalStatusDetailfile(MultipartFile presentCriminalStatusDetailfile) {
        this.presentCriminalStatusDetailfile = presentCriminalStatusDetailfile;
    }

    public String getOriginalFileNameforpresentCriminalStatus() {
        return originalFileNameforpresentCriminalStatus;
    }

    public void setOriginalFileNameforpresentCriminalStatus(String originalFileNameforpresentCriminalStatus) {
        this.originalFileNameforpresentCriminalStatus = originalFileNameforpresentCriminalStatus;
    }

    public String getDiskFileNameforpresentCriminalStatus() {
        return diskFileNameforpresentCriminalStatus;
    }

    public void setDiskFileNameforpresentCriminalStatus(String diskFileNameforpresentCriminalStatus) {
        this.diskFileNameforpresentCriminalStatus = diskFileNameforpresentCriminalStatus;
    }

    public String getFileTypeforpresentCriminalStatusDetail() {
        return fileTypeforpresentCriminalStatusDetail;
    }

    public void setFileTypeforpresentCriminalStatusDetail(String fileTypeforpresentCriminalStatusDetail) {
        this.fileTypeforpresentCriminalStatusDetail = fileTypeforpresentCriminalStatusDetail;
    }

    public String getDateforpresentCriminalStatus() {
        return dateforpresentCriminalStatus;
    }

    public void setDateforpresentCriminalStatus(String dateforpresentCriminalStatus) {
        this.dateforpresentCriminalStatus = dateforpresentCriminalStatus;
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

    public String getOrderNoForDISTraining() {
        return orderNoForDISTraining;
    }

    public void setOrderNoForDISTraining(String orderNoForDISTraining) {
        this.orderNoForDISTraining = orderNoForDISTraining;
    }

    public String getServingChargeDetail() {
        return servingChargeDetail;
    }

    public void setServingChargeDetail(String servingChargeDetail) {
        this.servingChargeDetail = servingChargeDetail;
    }

    public int getTotalpunishment1() {
        return totalpunishment1;
    }

    public void setTotalpunishment1(int totalpunishment1) {
        this.totalpunishment1 = totalpunishment1;
    }

    public int getTotalpunishment2() {
        return totalpunishment2;
    }

    public void setTotalpunishment2(int totalpunishment2) {
        this.totalpunishment2 = totalpunishment2;
    }

    public int getTotalpunishment3() {
        return totalpunishment3;
    }

    public void setTotalpunishment3(int totalpunishment3) {
        this.totalpunishment3 = totalpunishment3;
    }

    public String getCurrentRankName() {
        return currentRankName;
    }

    public void setCurrentRankName(String currentRankName) {
        this.currentRankName = currentRankName;
    }

    public String getPromoteToRankName() {
        return promoteToRankName;
    }

    public void setPromoteToRankName(String promoteToRankName) {
        this.promoteToRankName = promoteToRankName;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public int getRankMapId() {
        return rankMapId;
    }

    public void setRankMapId(int rankMapId) {
        this.rankMapId = rankMapId;
    }

    public String getYearinServiceFromPresentRank() {
        return yearinServiceFromPresentRank;
    }

    public void setYearinServiceFromPresentRank(String yearinServiceFromPresentRank) {
        this.yearinServiceFromPresentRank = yearinServiceFromPresentRank;
    }

    public String getMonthrinServiceFromPresentRank() {
        return monthrinServiceFromPresentRank;
    }

    public void setMonthrinServiceFromPresentRank(String monthrinServiceFromPresentRank) {
        this.monthrinServiceFromPresentRank = monthrinServiceFromPresentRank;
    }

    public String getDaysinServiceFromPresentRank() {
        return daysinServiceFromPresentRank;
    }

    public void setDaysinServiceFromPresentRank(String daysinServiceFromPresentRank) {
        this.daysinServiceFromPresentRank = daysinServiceFromPresentRank;
    }

    public String getDeptExamPassifany() {
        return deptExamPassifany;
    }

    public void setDeptExamPassifany(String deptExamPassifany) {
        this.deptExamPassifany = deptExamPassifany;
    }

    public String getGradeSerialNo() {
        return gradeSerialNo;
    }

    public void setGradeSerialNo(String gradeSerialNo) {
        this.gradeSerialNo = gradeSerialNo;
    }

    public String getHavePassedTrainingAsi() {
        return havePassedTrainingAsi;
    }

    public void setHavePassedTrainingAsi(String havePassedTrainingAsi) {
        this.havePassedTrainingAsi = havePassedTrainingAsi;
    }

    public String getOrderNoForPassedTrainingAsi() {
        return orderNoForPassedTrainingAsi;
    }

    public void setOrderNoForPassedTrainingAsi(String orderNoForPassedTrainingAsi) {
        this.orderNoForPassedTrainingAsi = orderNoForPassedTrainingAsi;
    }

    public String getDateOfPassedTrainingAsi() {
        return dateOfPassedTrainingAsi;
    }

    public void setDateOfPassedTrainingAsi(String dateOfPassedTrainingAsi) {
        this.dateOfPassedTrainingAsi = dateOfPassedTrainingAsi;
    }

    public String getBattalionWorkExperienceifany() {
        return battalionWorkExperienceifany;
    }

    public void setBattalionWorkExperienceifany(String battalionWorkExperienceifany) {
        this.battalionWorkExperienceifany = battalionWorkExperienceifany;
    }

    

    
    

}
