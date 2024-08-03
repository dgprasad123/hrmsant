package hrms.dao.policemodule;

import com.itextpdf.text.Document;
import hrms.model.common.FileAttribute;
import hrms.model.policemodule.ASINominationForm;
import hrms.model.policemodule.EmployeeDetailsForRank;
import java.util.List;
import jxl.write.WritableWorkbook;

public interface ASINominationDAO {
    
    public List getASINominationList(String offCode);
    
    public List getRankListForASI(String offCode);
    
    public List getEmployeeListRankWiseForASI(String postName, String offCode);
    
    public void createASINomination(String empId, String currentPost, String nominationPost, String offCode, String loginUserName);
    
    public EmployeeDetailsForRank getASINominationCreatedData(String offCode, int nominationMasterId);
    
    public List getCreatedASINominationList(String offCode, int nominationMasterId);
    
    public List getEmployeeListRankWiseExcludedAlreadyMapped(String offCode, int nominationMasterId);
    
    public void addNewEmployee(String empId, int nominationMaxId,String currentpost);
    
    public ASINominationForm getEmployeeASINominationData(int nominationMasterId, int nominationDetailId);
    
    public ASINominationForm getEmployeeBeforeNominationData(int nominationMasterId, int nominationDetailId);
    
    public void saveEmployeeASINominationData(ASINominationForm nform,String filepath);
    
    public void updateEmployeeNominationData(ASINominationForm nform,String filepath);
    
    public void getASINominationAnnexureAData(String nominationmasterid,WritableWorkbook workbook,String createdofficename);
    
    public void getASINominationAnnexureBData(String nominationmasterid,WritableWorkbook workbook,String createdofficename);
    
    public void submitASINominationForm2RangeOffice(String fieldOffice, String rangeOffice, String nominationId);
    
    public ASINominationForm getCreatedOfficeName(ASINominationForm asiform);
    
    public List getASINominationEmployeeList(int nominationMasterId);
    
    public void getASICheckListData(WritableWorkbook workbook, String establishmentName, String photoPath, String downloadType) throws Exception;
    
    public List getAllEstablishmentName();
    
    public List getAllCenterList();
    
    public List getCenterPriviligeList(String entryYear);
    
    public void deleteCenterPriviligeListOfficeWise(ASINominationForm nfm);
    
    public List getAllQualifiedDistrictList();
    
    public String saveAssignCenter(ASINominationForm nominationForm);
    
    public String getCenterName(String centerCode);
    
    public String checkClosingDateForASInomination(int year, int month, int date, int time);
    
    public List downloadASIAdmitCard(String offCode, String filepath);
    
    public List getCenterWiseCandidateList(String centerCode, String filepath);
    
    public List getQualifiedListinASIExam(String offcode, String filepath);
    
    public List getCandidateListApplyForASIExam();
    
    public void updateQualifiedListinASIExam();
    
    public String generateHallTicketForASIExam();
    
    
}
