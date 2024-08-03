/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.performanceappraisal;

import com.itextpdf.text.Document;
import hrms.model.login.LoginUserBean;
import hrms.model.parmast.DepartmentPromotionBean;
import hrms.model.parmast.DepartmentPromotionDetail;
import hrms.model.parmast.GroupCAdminAdminSearchCriteria;
import hrms.model.parmast.GroupCCustodianCommunication;
import hrms.model.parmast.GroupCEmployee;
import hrms.model.parmast.GroupCInitiatedbean;
import hrms.model.parmast.GroupCSearchResult;
import hrms.model.parmast.ParAdverseCommunicationDetail;
import hrms.model.parmast.ParApplyForm;
import hrms.model.parmast.ParAssignPrivilage;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manisha
 */
public interface PARCPromotionDAO {

    public List getGroupCEmployeeList(String offCode, int groupcPromotionid);

    public void savegroupCEmpList(GroupCEmployee groupCEmployee);

    public String getAppriseSPCOfPar(int parId);

    public void updateGroupCpromotionRemark(GroupCEmployee groupCEmployee);

    public ArrayList getSelectedgroupCEmpList(int groupcPromotionId);

    public ArrayList getSelectedFitForPronotionCEmpList(String reviewingEmpId);

    public void saveremarksForGroupC(GroupCEmployee groupCEmployee);

    public String getisPendingAtEmpId(int groupcPromotionid);

    public int gettaskIdFromGroupCPromotionId(int groupcPromotionid);

    public GroupCEmployee getremarksForGroupC(GroupCEmployee groupCEmployee);

    public void deleteremarksForGroupC(GroupCEmployee groupCEmployee);

    public void reviewingRemarkFitForPromotion(GroupCEmployee groupCEmployee);

    public boolean isReviewingGivesAllRemark(String offCode, int groupcPromotionid);

    public boolean isReortingSaveAllDate(String offCode, int groupcPromotionid);

    public boolean isReviewingSaveAllDate(String offCode, int groupcPromotionid);

    public boolean isAcceptingGivesAllRemark(String offCode, int groupcPromotionid);

    public void reviewingRemarkNotFitForPromotion(GroupCEmployee groupCEmployee);

    public ArrayList gethigherAuthorityList(String deptcode,String offCode);

    public void forwardToAdverseApprisee(GroupCEmployee groupCEmployee);

    public int savehigherAuthorityForwardRemark(GroupCEmployee groupCEmployee);

    public void deleteselectedEmployeeList(GroupCEmployee groupCEmployee);

    public ArrayList getSelectedNotFitForPronotionCEmpList(int groupCpromotionId);

    public int getGroupCpromotionId(int taskId);

    public void createParCPromotionReport(GroupCInitiatedbean groupCInitiatedbean);

    public ArrayList getPromotionReportList(String reportingempId);

    public void deletereviewedEmpDetails(GroupCInitiatedbean groupCInitiatedbean);

    public void updateGroupCpromotionReviewingRemark(GroupCEmployee groupCEmployee);

    public void forwardReviewingPromotionList(GroupCEmployee groupCEmployee);

    public void submitAcceptingingPromotionList(GroupCEmployee groupCEmployee);

    public void acceptingRemarkFitForPromotion(GroupCEmployee groupCEmployee);

    public void updateGroupCpromotionAcceptingRemark(GroupCEmployee groupCEmployee);

    public void acceptingRemarkNotFitForPromotion(GroupCEmployee groupCEmployee);

    public ArrayList getgroupCCustdianDetail(ParAssignPrivilage pap, String fiscalyear);

    public ArrayList getReportingAuthorityDetail(int groupcPromotionId);

    public ArrayList getAcceptingFinalRemarks(String fiscalyear);

    public void savecustodianremarksForGroupC(GroupCCustodianCommunication groupCCustodianCommunication);

    public ArrayList getremarksOfCustodianForGroupC(int promotionId);

    public void saveAcceptingRemarksForReview(ParApplyForm parApplyForm);

    public void savecustodianremarksAdversePAR(ParAdverseCommunicationDetail parAdverseCommunicationDetail);

    public void savecustodianremarksAdversePARToAppraisee(ParAdverseCommunicationDetail parAdverseCommunicationDetail);

    public void saveparAdverseCommunicationReply(ParAdverseCommunicationDetail parAdverseCommunicationDetail);

    public ArrayList getcustodianremarksAdversePAR(int parId);

    public ParAdverseCommunicationDetail getCommunicationDetails(int communicationId);

    public GroupCCustodianCommunication getAppraiseeForGroupC(int promotionId);

    public GroupCCustodianCommunication getAttachedFile(int communication_id);

    public GroupCEmployee getAttachedFileforNotFit(int promotionId);

    public void getAppraiseForAdversePAR(ParAdverseCommunicationDetail parAdverseCommunicationDetail);

    public ParAdverseCommunicationDetail getAttachedFileforAdversePAR(int parId);

    public List getGroupCEmployeeListFromOtherOffice(String offCode, int groupcPromotionid);

    public boolean isRemarksCompulsoryReviewing(int groupcPromotionid, String remarkofReviewing);

    public boolean isRemarksCompulsoryAccepting(int groupcPromotionid, String remarkofAccepting);

    public void updateFromAndToDateForReporting(GroupCEmployee groupCEmployee);

    public String revertGroupCPARByAuthority(String loginempid, GroupCEmployee groupCEmployee);

    public String[] getRevertReasonOfGroupCPAR(int groupcPromotionid, int taskId);

    public int gettaskId(int groupcPromotionid);

    public String getstatusId(int groupcPromotionid);

    public ParAssignPrivilage getAssignPrivilage(String spc);

    public String saveAssignPrivilege(GroupCEmployee groupCEmployee);

    public ArrayList getAssignPrivilegedList(String spc);

    public ArrayList getAssignPrivilegedListDistandOffWise(String mode);

    public void deleteGroupCPrivilage(String spc);

    public GroupCSearchResult getGroupCEmpList(GroupCAdminAdminSearchCriteria groupCAdminAdminSearchCriteria);

    public List getGroupCStatusList();

    public GroupCInitiatedbean getAuthorityDetail(int groupcPromotionid);

    public List getOfficePriveligedList(String spc);

    public List getDistrictPriveligedList(String spc);

    public GroupCEmployee getFromDateToDateDetail(int promotionId);

    public boolean isDuplicatePARCPeriod(String reviewedEmpId, String parcfrmdt, String parctodt, String fiscalyear, String promotionId);

    public GroupCEmployee getFromDateToDateByEmpId(String reviewingEmpId, String fiscalyear);
    
    public String getAssignedDepartmentForGroupC(String lmid,String offcode);
}
