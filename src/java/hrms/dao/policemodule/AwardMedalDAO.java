/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.policemodule;

import hrms.common.Message;
import hrms.model.common.FileAttribute;
import hrms.model.policemodule.AwardMedalListForm;
import java.util.List;
import jxl.write.WritableWorkbook;

/**
 *
 * @author Surendra
 */
public interface AwardMedalDAO {

    public List getAwardTypeList();

    public List getEmployeeListGpfNowise(String gpfno, String awardYear, String awardId, String awardOccasion);

    public void addEmployeeForAward(String gpfno, String awardName, String awardYear, String loginoffcode, String awardOccasion);

    public List getAwardeeList(String offcode, String awardMedalId, String awardYear, String awardOccasion);

    public AwardMedalListForm getAwardeeData(String rewardId, String profilePhotoPath);

    public void updateAwardData(AwardMedalListForm award, String filePath);

    public void updateAwardDataForGovernor(AwardMedalListForm award, String filePath);
    
     public void updateAwardDataForPresident(AwardMedalListForm award, String filePath);

    public FileAttribute getDocument(String filePath, String docName, int attachId);

    public void deleteAwardeeDetails(int awardMedalId);

    public void submit2Range(int awardMedalId, String offcode);

    public List getSubmittedAwardListForRange(String offcode, String awardMedalId, String awardYear, String awardoccasion);
    
    public void revertNominationDataByRangeOffice(AwardMedalListForm award);

    public void updateAwardDataByRange(AwardMedalListForm award);

    public List getAwardeeCompletedList(String awardMedalId, String awardYear, String awardoccasion);

    public List getAwardeeCompletedList(String awardMedalId, String awardYear, String offCode, String awardoccasion);

    public List getDistEstList(String awardMedalId, String awardYear); 

    public List getRangeList(String awardMedalId, String awardYear);

    public List getAwardeeCompletedByRangeList(String awardMedalId, String awardYear, String awardoccasion);

    public List getAwardeeCompletedByRangeList(String awardMedalId, String awardYear, String rangeCode, String awardoccasion);

    public List getAwardeeListNotCompletedByRange(String awardMedalId, String awardYear, String awardoccasion);

    public void downloadBroadSheetForAwardMedal(WritableWorkbook workbook, String awardId, String awardYear, String awardoccasion);

    public void revertAwardDataByDGOffice(AwardMedalListForm award);

    public void updateSRCaseData(AwardMedalListForm form);

    public void saveSRCaseData(AwardMedalListForm form);

    public List getSRCaseList(String awardMedalId);

    public AwardMedalListForm getSRCaseData(String srCaseId);

    public void deleteSRCaseData(String srCaseId);

    public void saveInvestigatedCaseData(AwardMedalListForm form, String filePath);

    public List investigatedCaseList(String awardMedalId);

    public AwardMedalListForm getInvestigatedCaseData(String invstCaseId);

    public void updateInvestigatedCaseData(AwardMedalListForm form, String filepath);

    public FileAttribute getInvstigateCaseDoc(String filePath, int attachId);

    public void deleteInvestigatedCaseData(String invstcaseId);

    public int closePoliceMedal(String awardmedaltypeid);
    
    public String CheckStatusOfPoliceMedal(String awardmedaltypeid);
    
    public int activePoliceMedal(String awardmedaltypeid);
    
    public int deActivePoliceMedal(String awardmedaltypeid);

    public int deleteRewardAttachment(int attchid, String docName, String filepath, FileAttribute fa);

    public void saveNonSrCaseDtlsData(AwardMedalListForm form);

    public List getNonSrCaseList(String awardMedalId);

    public AwardMedalListForm getNonSRCaseData(String nonSrcaseId);

    public void updateNonSrCaseDtlsData(AwardMedalListForm form);

    public void deleteNonSrCaseData(String nonSrcaseId);        
    
    public void deleteAwardeeDetailsForGovernor(int awardMedalId);

}