package hrms.dao.miscellaneous;

import hrms.model.miscellaneous.ScStRecruitment;
import hrms.model.miscellaneous.Rascheme;
import hrms.model.miscellaneous.Bleo;
import hrms.model.miscellaneous.CommissionCourtCase;
import hrms.model.miscellaneous.CommissionPending;
import hrms.model.miscellaneous.Training;
import hrms.model.miscellaneous.RecruitmentDrive;
import hrms.model.miscellaneous.SelectedCandidates;
import hrms.model.miscellaneous.SelectedCandidatesCategory;
import hrms.model.miscellaneous.VacantPost;
import java.io.OutputStream;

import java.util.List;
import jxl.write.WritableWorkbook;

public interface DeptDataEntryDAO {

    public int saveScStRecruitment(ScStRecruitment appoint);

    public int updateRecruitmentData(ScStRecruitment appoint);

    public int deleteRecruitment(String recruitmentId);

    public ScStRecruitment editRecruitment(String recruitmentId);

    public List getScStRecruitmentList(String deptId);

    public int saveRascheme(Rascheme raScheme);

    public int updateAppointmentStatusData(Rascheme appointStatus);

    public int deleteAppointmentStatus(String raSchemeId);

    public Rascheme editAppointmentStatus(String raSchemeId);

    public List getRaschemeList(String deptId);

    public int saveBleo(Bleo empStrength, String deptId);

    public int updateEmpStrengthData(Bleo empStrength);

    public int deleteEmpStrength(String recruitEoId);

    public Bleo editEmpStrength(String recruitEoId);

    public List getBleoList(String deptId);

    public int saveTraining(Training empTraining);

    public int updateEmpTrainingData(Training empTraining);

    public int deleteEmpTraining(String trainingId);

    public Training editEmpTraining(String trainingId);

    public List getTrainingList(String deptId);

    public int saveDeptvacantpost(VacantPost empStrength);

    public int updateVacantPostData(VacantPost empStrength);

    public int deleteVacantPost(String vacantPostId);

    public VacantPost editVacantPost(String vacantPostId);

    public List getVacantPost(String deptId, String month, String year);

    public int saveRecruitDriveData(RecruitmentDrive recruitDrive);

    public int updateRecruitDriveData(RecruitmentDrive recruitDrive);

    public int deleteRecruitDrive(String recruitDriveId);

    public RecruitmentDrive editRecruitDrive(String recruitDriveId);

    public List getRecruitDriveList(String deptId);

    public List getDEInstituteList();

    /**
     * ************ Manoj **************
     */
    public void saveCandidateCategory(SelectedCandidatesCategory scc, String empId);

    public List selectedCandiateCategoryList(String empId);

    public void saveSelectedCandidates(SelectedCandidates sc);

    public List selectedCandiatesList(String categoryId);

    public void deleteCandidate(int candidateId, int categoryId);

    public SelectedCandidatesCategory getcCategoryDetail(String categoryId);

    public void saveCommissionPending(CommissionPending cp, String empId);

    public CommissionPending editcommissionpending(String taskid);

    public void updateCommissionPending(CommissionPending cp, String empId);

    public List commissionPendingList(String empId);

    public void saveCommissionCourtcaseDetails(CommissionCourtCase cp, String empId);

    public List commissioncourtcaseListDetails(String empId);

    public CommissionCourtCase editCommissionCourtCase(String taskid);

    public void updateCommissionCourtCase(CommissionCourtCase cp, String empId);

    /**
     * ************************************************** MIS Panel
     * ***************
     */
    public List getmisVacantPost(String month, String year);

    public List getmisRAScheme(String month, String year);

    public List getmisScStRecruitment(String month, String year);

    public List misRecruitDriveList(String month, String year);

    public List misTrainingList(String month, String year);

    public List misBleoList(String deptId);

    public List miscommissionPendingList(String month, String year);

    public List miscommissioncourtcaseListDetails(String month, String year);

    public List summarygetmisRAScheme(String month, String year);

    public List summarygetmisVacantPost(String month, String year);

    public List summaryvacantgroupwise(String month, String year, String deptid);

    public List summaryvacantpostwise(String month, String year, String deptid, String gname);

    public void updateMonthlyVacantPost(String deptid, String month, String year);

    public List summaryvacantdeptgroupwise(String month, String year, String deptid);

    public List summaryvacantdeptpostwise(String month, String year, String deptid, String gname);

    public List entryDetailsList(String month, String year, String deptid);

    public List misSplReport(String month, String year);

    public List totalsummarygetmisRAScheme();

    public List totalsummaryvacantgroupwise(String month, String year);
    
    public void DownlaodAadhaarDetails(OutputStream out, String fileName, WritableWorkbook workbook);
}
