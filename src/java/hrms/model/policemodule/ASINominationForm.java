package hrms.model.policemodule;

import org.springframework.web.multipart.MultipartFile;

public class ASINominationForm {

    private String downloadType = "";

    private String nominationMasterId;
    private String nominationDetailId;
    private String nominationFormId;
    private String empId;

    private String officeCode;
    private String officeName;
    private String sltEstablishment;
    private String sltCenter;
    private String examDate="";

    private String fullname;
    private String category;
    private String fathersname;
    private String dob;
    private String qualification;
    private String homeDistrict;
    private String dojAppntdDist;
    private String currentRank;
    private String postingPlace;
    private String postingDate;
    private String dojNewDist;
    private String txtTrainingCompletedDate;
    private String yearinServiceLength;
    private String monthinServiceLength;
    private String daysinServiceLength;
    private String yearinRedeploymentServiceLength;
    private String monthRedeploymentServiceLength;
    private String daysRedeploymentServiceLength;

    private String homedistrictName;
    private String doeGov;

    private String sltRedeploymentJoining;
    private String txtRedeploymentJoiningDate;

    private String annexureARemarks;
    private String annexureBRemarks;

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

    private String adverseIfany="";
    private String dpcifany;
    private MultipartFile discDocument;
    private MultipartFile documentServingCopy;

    private String dateofServingifAny;
    private String dateofServing;

    private MultipartFile nominatedEmployeePhoto;

    private String txtMobile;

    private String photoAttchId;
    private String applicationId;
    private String admitCardRollNo;
    private String whetheravailedReservationCategory = "";

    private String originalFileName = "";
    private String diskfileName = "";
    private String fileType = "";

    private String brassno;
    private int centerCode;
    private String centerName;
    private String qualifiedBySystem;
    private String entryYear;

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getDiskfileName() {
        return diskfileName;
    }

    public void setDiskfileName(String diskfileName) {
        this.diskfileName = diskfileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getDojAppntdDist() {
        return dojAppntdDist;
    }

    public void setDojAppntdDist(String dojAppntdDist) {
        this.dojAppntdDist = dojAppntdDist;
    }

    public String getCurrentRank() {
        return currentRank;
    }

    public void setCurrentRank(String currentRank) {
        this.currentRank = currentRank;
    }

    public String getPostingPlace() {
        return postingPlace;
    }

    public void setPostingPlace(String postingPlace) {
        this.postingPlace = postingPlace;
    }

    public String getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }

    public String getDojNewDist() {
        return dojNewDist;
    }

    public void setDojNewDist(String dojNewDist) {
        this.dojNewDist = dojNewDist;
    }

    public String getTxtTrainingCompletedDate() {
        return txtTrainingCompletedDate;
    }

    public void setTxtTrainingCompletedDate(String txtTrainingCompletedDate) {
        this.txtTrainingCompletedDate = txtTrainingCompletedDate;
    }

    public String getYearinServiceLength() {
        return yearinServiceLength;
    }

    public void setYearinServiceLength(String yearinServiceLength) {
        this.yearinServiceLength = yearinServiceLength;
    }

    public String getMonthinServiceLength() {
        return monthinServiceLength;
    }

    public void setMonthinServiceLength(String monthinServiceLength) {
        this.monthinServiceLength = monthinServiceLength;
    }

    public String getDaysinServiceLength() {
        return daysinServiceLength;
    }

    public void setDaysinServiceLength(String daysinServiceLength) {
        this.daysinServiceLength = daysinServiceLength;
    }

    public String getYearinRedeploymentServiceLength() {
        return yearinRedeploymentServiceLength;
    }

    public void setYearinRedeploymentServiceLength(String yearinRedeploymentServiceLength) {
        this.yearinRedeploymentServiceLength = yearinRedeploymentServiceLength;
    }

    public String getMonthRedeploymentServiceLength() {
        return monthRedeploymentServiceLength;
    }

    public void setMonthRedeploymentServiceLength(String monthRedeploymentServiceLength) {
        this.monthRedeploymentServiceLength = monthRedeploymentServiceLength;
    }

    public String getDaysRedeploymentServiceLength() {
        return daysRedeploymentServiceLength;
    }

    public void setDaysRedeploymentServiceLength(String daysRedeploymentServiceLength) {
        this.daysRedeploymentServiceLength = daysRedeploymentServiceLength;
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

    public String getHomedistrictName() {
        return homedistrictName;
    }

    public void setHomedistrictName(String homedistrictName) {
        this.homedistrictName = homedistrictName;
    }

    public String getDoeGov() {
        return doeGov;
    }

    public void setDoeGov(String doeGov) {
        this.doeGov = doeGov;
    }

    public String getSltRedeploymentJoining() {
        return sltRedeploymentJoining;
    }

    public void setSltRedeploymentJoining(String sltRedeploymentJoining) {
        this.sltRedeploymentJoining = sltRedeploymentJoining;
    }

    public String getTxtRedeploymentJoiningDate() {
        return txtRedeploymentJoiningDate;
    }

    public void setTxtRedeploymentJoiningDate(String txtRedeploymentJoiningDate) {
        this.txtRedeploymentJoiningDate = txtRedeploymentJoiningDate;
    }

    public String getAnnexureARemarks() {
        return annexureARemarks;
    }

    public void setAnnexureARemarks(String annexureARemarks) {
        this.annexureARemarks = annexureARemarks;
    }

    public String getAnnexureBRemarks() {
        return annexureBRemarks;
    }

    public void setAnnexureBRemarks(String annexureBRemarks) {
        this.annexureBRemarks = annexureBRemarks;
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

    public String getDpcifany() {
        return dpcifany;
    }

    public void setDpcifany(String dpcifany) {
        this.dpcifany = dpcifany;
    }

    public String getDateofServingifAny() {
        return dateofServingifAny;
    }

    public void setDateofServingifAny(String dateofServingifAny) {
        this.dateofServingifAny = dateofServingifAny;
    }

    public String getDateofServing() {
        return dateofServing;
    }

    public void setDateofServing(String dateofServing) {
        this.dateofServing = dateofServing;
    }

    public MultipartFile getNominatedEmployeePhoto() {
        return nominatedEmployeePhoto;
    }

    public void setNominatedEmployeePhoto(MultipartFile nominatedEmployeePhoto) {
        this.nominatedEmployeePhoto = nominatedEmployeePhoto;
    }

    public String getTxtMobile() {
        return txtMobile;
    }

    public void setTxtMobile(String txtMobile) {
        this.txtMobile = txtMobile;
    }

    public String getPhotoAttchId() {
        return photoAttchId;
    }

    public void setPhotoAttchId(String photoAttchId) {
        this.photoAttchId = photoAttchId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getAdmitCardRollNo() {
        return admitCardRollNo;
    }

    public void setAdmitCardRollNo(String admitCardRollNo) {
        this.admitCardRollNo = admitCardRollNo;
    }

    public String getWhetheravailedReservationCategory() {
        return whetheravailedReservationCategory;
    }

    public void setWhetheravailedReservationCategory(String whetheravailedReservationCategory) {
        this.whetheravailedReservationCategory = whetheravailedReservationCategory;
    }

    public String getSltEstablishment() {
        return sltEstablishment;
    }

    public void setSltEstablishment(String sltEstablishment) {
        this.sltEstablishment = sltEstablishment;
    }

    public String getSltCenter() {
        return sltCenter;
    }

    public void setSltCenter(String sltCenter) {
        this.sltCenter = sltCenter;
    }

    public String getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(String downloadType) {
        this.downloadType = downloadType;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public String getAdverseIfany() {
        return adverseIfany;
    }

    public void setAdverseIfany(String adverseIfany) {
        this.adverseIfany = adverseIfany;
    }

    public MultipartFile getDiscDocument() {
        return discDocument;
    }

    public void setDiscDocument(MultipartFile discDocument) {
        this.discDocument = discDocument;
    }

    public MultipartFile getDocumentServingCopy() {
        return documentServingCopy;
    }

    public void setDocumentServingCopy(MultipartFile documentServingCopy) {
        this.documentServingCopy = documentServingCopy;
    }

    public String getBrassno() {
        return brassno;
    }

    public void setBrassno(String brassno) {
        this.brassno = brassno;
    }

    public int getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(int centerCode) {
        this.centerCode = centerCode;
    }

    public String getQualifiedBySystem() {
        return qualifiedBySystem;
    }

    public void setQualifiedBySystem(String qualifiedBySystem) {
        this.qualifiedBySystem = qualifiedBySystem;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

   

    public String getEntryYear() {
        return entryYear;
    }

    public void setEntryYear(String entryYear) {
        this.entryYear = entryYear;
    }

    
    
}
