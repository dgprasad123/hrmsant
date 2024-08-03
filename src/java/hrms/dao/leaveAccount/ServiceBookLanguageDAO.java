/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.leaveAccount;

import hrms.model.EmpQuarterAllotment.EmpQuarterBean;
import hrms.model.EnrollmentToInsurance.Enrollmenttoinsurance;
import hrms.model.EquivalentPost.EquivalentPost;
import hrms.model.FTC.sFTCBean;
import hrms.model.LTC.sLTCBean;
import hrms.model.absorption.AbsorptionModel;
import hrms.model.additionalCharge.AdditionalCharge;
import hrms.model.allotmentToCadre.AllotmentToCadreForm;
import hrms.model.allowances.AllowanceModel;
import hrms.model.allowedToOfficiate.AllowedToOfficiateModel;
import hrms.model.brassallot.BrassAllotList;
import hrms.model.cadre.Cadre;
import hrms.model.conclusionproceedings.ConclusionProceedings;
import hrms.model.confirmationofservice.ConfirmationOfServiceForm;
import hrms.model.departmentalExam.DepartmentalExamForm;
import hrms.model.deputation.DeputationDataForm;
import hrms.model.headquarterleaving.HeadQuarterLeaving;
import hrms.model.incrementsanction.IncrementForm;
import hrms.model.joiningCadre.JoiningCadreForm;
import hrms.model.leaveSanction.LeaveSanctionForm;
import hrms.model.loan.Loan;
import hrms.model.loanrelease.LoanRelease;
import hrms.model.notification.NotificationBean;
import hrms.model.payentitlement.PayEntitlementForm;
import hrms.model.payfixation.PayFixation;
import hrms.model.placementofservice.PlacementOfServiceForm;
import hrms.model.posting.PostingForm;
import hrms.model.promotion.PromotionForm;
import hrms.model.punishment.PunishmentBean;
import hrms.model.recruitment.RecruitmentModel;
import hrms.model.redeployment.Redeployment;
import hrms.model.redesignation.Redesignation;
import hrms.model.regularizeService.RegularizeServiceContractualForm;
import hrms.model.regularizeService.RegularizeServiceForm;
import hrms.model.reinstatement.Reinstatement;
import hrms.model.relieveCadre.RelieveCadreForm;
import hrms.model.repatriation.RepatriationForm;
import hrms.model.repayment.LoanRepayment;
import hrms.model.reward.Reward;
import hrms.model.servicecloser.Retirement;
import hrms.model.suspension.Suspension;
import hrms.model.trainingschedule.TrainingSchedule;
import hrms.model.transfer.TransferForm;

/**
 *
 * @author Surendra
 */
public interface ServiceBookLanguageDAO {

    public String getFirstAppointDetails(RecruitmentModel recruitmentModel, int notid, String notType);

    public String getIncrementSanctionDetails(IncrementForm incfb, int notid, String payCommission);

    public String getTransferDetails(TransferForm tcfb, int notid, String notType);

    public NotificationBean getNotificationDetails(int notId, String notType);

    public String getRelieveDetails(int notId, String empid);

    public String getJoinDetails(String joinId, String empid);

    public String getPromotionDetails(PromotionForm promoForm, int notid, String notificationtype);

    public String getLeaveSanctionDetails(int lvid, String empid, String othspc);

    public String getTrainingDetails(TrainingSchedule trinput);

    public String getSuspDetails(Suspension sfb);

    public String getReinstateLanguage(Reinstatement re);

    public String getDeputationAGLanguage(DeputationDataForm deput);

    public String getJoininginCadreDetails(JoiningCadreForm joiningCadreForm, int notid, String notificationtype);

    public String getDeputationDetails(DeputationDataForm deputationForm, String notType, int notId);

    public String generateLTCLanguage(sLTCBean slBean);

    public String getConfirmationOfServiceDetails(ConfirmationOfServiceForm confirmationOfServiceForm, int notid, String notificationtype);

    public String getPayFixationDetails(PayFixation prf, int notid, String payType);

    public String getPostingDetails(PostingForm postingform, int notid, String notType);

    public String getRetirementDetails(Retirement rt);

    public String getRewardDetails(Reward prfb);

    public String getDeptExamDetails(DepartmentalExamForm efb);

    public String getLoanSancDetails(Loan lfb);

    public void createTransactionLog(String userid, String targethrmsid, String modulename, String querystring);

    public String generateFTCLanguage(sFTCBean slBean);

    public String getPlacementOfServiceDetails(PlacementOfServiceForm placementOfServiceForm, int notid, String notificationtype);

    public String getPayEntitlementDetails(PayEntitlementForm payEntitlementForm, int notid, String notificationtype);

    public String getRedesignationDetails(Redesignation redesig, int notid, String notType);

    public String getAllowedToOfficiateDetails(AllowedToOfficiateModel officiation, int notid, String notType);

    public String getMiscllaneousString(NotificationBean nfb, int notid, String notificationtype);

    public String getPunishmentDetails(PunishmentBean pb, int notid, String notType);

    public String getBrassAllotmentDetails(BrassAllotList bsallot);

    public String getAllowancesDeductionsLangDetails(AllowanceModel allowBean);

    public String getRelieveFromCadreDetails(RelieveCadreForm relieveCadreForm, int notid, String notType);

    public String getAllotmentToCadreLangDetails(AllotmentToCadreForm allotmentToCadre, int notid, String notType);

    public String getQtrSurrenderLangDetails(EmpQuarterBean eqBean, String qaID, String empid, String notType);

    public String getRedeploymentLangDetails(Redeployment redeploymentForm, int notid, String notType);

    public String getDetentionOnVacationLangDetails(HeadQuarterLeaving leavingForm, int notid, String notType);

    public String getRepatriationLangDetails(RepatriationForm repatriationForm, int notid, String notType);
    
    public String getInitProceedingsDetails(ConclusionProceedings conclusionProceedingsForm,String proceedingcause);
    
    public String getFinalResultProceedingsDetails(ConclusionProceedings conclusionProceedingsForm,String proceedingcause);
    
    public String getRegularizationLangDetails(RegularizeServiceForm regularizeService,int notid);
    
    public String getReleaseOfLoanLngDetails(LoanRelease loanrelease,int notid);
    
    public String getRepaymentOfLoanLangDetails(LoanRepayment loanrepayment,int notid);
    
    public String getAbsorptionLangDetails(AbsorptionModel absorptionForm, int notid);
    
    public String getAdditionalChargeLangDetails(AdditionalCharge additionChargeForm, int notid);
    
    public String getQtrAllotLangDetails(EmpQuarterBean eqBean, String qaID, String empid, String notType);
    
    public String getCancelLangDetails(NotificationBean nb,int noid,String othspc);
    
    public String getSupersedeLangDetails(int noid,String othspc);
    
    public String getLongTermTrainingDetails(TrainingSchedule training,int notid);
    
    public String getRegularisationServiceContractual6Yrs(String empid);
    
    public String getEnrollmentSbDetails(Enrollmenttoinsurance enrollmentForm, String notType, int notId);
    
    public String getEquivalentSbDetails(EquivalentPost equivalpostForm,String notType, int notId);
    
    public String getPayFixationDetailsSBCorrection(PayFixation prf, int notid, String payType);
    
    public String getSuperanuationFormDetails(int noid);
}
