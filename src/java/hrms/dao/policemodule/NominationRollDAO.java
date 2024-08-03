/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.policemodule;

import com.itextpdf.text.Document;
import hrms.common.ZipFileAttribute;
import hrms.model.common.FileAttribute;
import hrms.model.policemodule.ASINominationForm;
import hrms.model.policemodule.EmployeeDetailsForRank;
import hrms.model.policemodule.NominationDifferentDisciplinaryProceedingList;
import hrms.model.policemodule.NominationForm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jxl.write.WritableWorkbook;

/**
 *
 * @author Surendra
 */
public interface NominationRollDAO {

    public String getCurrentRankCodeFormDetailTable(int nominationDetailId, int nominationMasterId);

    public String getRankname(String rankCode);

    public String getPromoteRankname(String rankCode);

    public List getCurrentRankList();
    
    public List getCurrentRankListForDgBoard();
    
    public List getAllCurrentRankList();

    public List getAllNominatedRankList();
    
    public List getNominatedRankList();

    public List getCurrentRankList(String currentRankCode);

    public List getNominatedRankList(String rankCode);
    
    public List getAllNominatedRankList(String rankCode);

    public List getNominationList(String offCode, String nominationForCode, String fiscalyear);

    public List getNominationListForGroupD(String offCode, String nominationForCode, String fiscalyear);

    public List getSubmittedNominationList(String offCode, String currentRankCode, String nominatedrankCode, String fiscalyear);

    public EmployeeDetailsForRank getNominationCreatedData(String offCode, int nominationMasterId);

    public List getCreatedNominationList(String offCode, String currentrankCode, int nominationMasterId);

    public List getSubmittedNominationList(String offCode, String currentrankCode, int nominationMasterId);

    public List getNominatedEmployeeList(int nominationMasterId);

    public List getRankList(String offCode);

   // public HashMap getEmployeeListGpfNowise(String gpfno);
    public List getEmployeeListGpfNowise(String gpfno);

    public List getEmployeeListRankWise(String postName, String offCode);

    public List getEmployeeListRankWiseExcludedAlreadyMapped(String postName, String offCode, int nominationMasterId);

    public void createNomination(String gpfno, String currentPost, String nominationPost, String offCode, String loginUserName, String fiscalyear);

    public void addNewEmployee(String gpfno, int nominationMaxId, String currentPost);

    public void addSearchEmployee2List(String gpfno, String loginOffCode, String loginoffname, String currentPost, String nominatedPost, String fiscalyear);

    public void deleteNomination(String offCode, int nominationMasterId);

    public void deleteEmployee(int nominationMasterId, int nominationDetailId);

    //public void downloadAnnextureAForDSPRank(Document document, int nominationMasterId);

    public NominationForm getEmployeeNominationData(int nominationMasterId, int nominationDetailId);

    public NominationForm getEmployeeBeforeNominationData(int nominationMasterId, int nominationDetailId, String currentRank);

    public void saveEmployeeNominationData(NominationForm nform, String filePath);

    public void updateEmployeeNominationRecomendationData(NominationForm nform);

    public void updateEmployeeNominationData(NominationForm nform, String filePath);

    public List getRangeOfficeList(String loginOffcode);

    public void submitNominationForm2RangeOffice(String fieldOffice, String rangeOffice, String nominationId);

    public void submitNominationForm2DGOffice(String rangeOffice, String dgOffice, String nominationId);

    public FileAttribute getDpcDocument(String filePath, String docName, int attachId);

    public List getNominatedList(String loginoffcode);

    public List getNominatedListDetailView(String nominatedMasterId);

    public ASINominationForm getNominatedEmployeeDetailData(String nomationMasterId, String nominationDetailId);

    public void updateEmployeeNominationRecomendationDataDG(NominationForm nform);

    public HashMap getNominationCompletedList(String currentPost, String nominatedForPost, String fiscalyear);

    public List getRecommendationCompletedList(String currentPost, String nominatedForPost, String fiscalyear);

    public List getRecomendationPendingList(String currentPost, String nominatedForPost, String fiscalyear);

    public ZipFileAttribute getAttachmentFiles(String formId);

    public void getEnteredGPFNoDuplicateData(String gpfno, EmployeeDetailsForRank edr);

    public String deletePoliceNominationDuplicateData(String gpfno);

    public List getDistrictWiseRecomendationListAnnextureI(String distCode);

    public List getDistrictWiseRecomendationListAnnextureIIA(String distCode);

    public FileAttribute getDocument(String filePath, String docName, int attachId);

    public int deleteNominationAttachment(int attchid, String docName, String filepath, FileAttribute fa);

    public List getDistrictWiseEmpNomination(String distCode);

    public NominationDifferentDisciplinaryProceedingList getAttachedFileOfDPforASI2SI(int proceedingdetailid);

    public void deletedpDetailforASI2SI(NominationDifferentDisciplinaryProceedingList nominationDifferentDisciplinaryProceedingList);

    public NominationForm getAttachedFileOfDPforSI2Inspector(int proceedingdetailid);

    public void deleteAttachedFileOfDPforSI2Inspector(NominationForm nform);

    public void revertNominationDataByRangeOffice(NominationForm nform);

    public void revertNominationDataByDGOffice(NominationForm nform);
    
    public ArrayList getAllRankListForNomination();
    
    public String CheckStatusOfPoliceNomination(String currentPost, String nominationPost);
    
    public String activeStatusOfPoliceNomination(String currentPost, String nominationPost);
    
    public String deActiveStatusOfPoliceNomination(String currentPost, String nominationPost);

}
