/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.employee;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.model.EmployeeRegistration.EmployeeRegistrationBean;
import hrms.model.empinfo.EmployeeMessage;
import hrms.model.empinfo.EmployeeSearchResult;
import hrms.model.empinfo.SearchEmployee;
import hrms.model.employee.Address;
import hrms.model.employee.Education;
import hrms.model.employee.Employee;
import hrms.model.employee.EmployeeLanguage;
import hrms.model.employee.EmployeePayProfile;
import hrms.model.employee.FamilyRelation;
import hrms.model.employee.IdentityInfo;
import hrms.model.employee.PensionFamilyList;
import hrms.model.employee.PensionProfile;
import hrms.model.employee.Pensioner;
import hrms.model.employee.PreviousPensionDetails;
import hrms.model.employee.Punishment;
import hrms.model.employee.Reward;
import hrms.model.employee.Training;
import hrms.model.employee.TransferJoining;
import hrms.model.employee.employeeFamilyHelperBean;
import hrms.model.loan.Loan;
import hrms.model.parmast.ParStatusBean;
import hrms.model.payroll.aqmast.AqmastModel;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import jxl.write.WritableWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manas Jena
 */
public interface EmployeeDAO {

    public byte[] getEmployeeFirstPage(String empid);

    public Employee getEmployee(String spc);

    public Education[] getEmployeeEducation(String empid);

    public void saveEmployeeEducation(Education education);

    public void updateEmployeeEducation(Education education);

    public void deleteEmployeeEducation(Education education);

    public Education getEmployeeEducationDtls(int qfnid);

    public Education[] getEmployeeEducation(String empid, String inputdate);

    public FamilyRelation[] getEmployeeFamily(String empid);

    public List getCcrdetail(String empid);

    public List getTrainingdetail(String empid);

    public List getdisclipnary(String empid);

    public List getEmployeeNominee(String empid);

    public String saveEmployeeFamily(FamilyRelation familyRelation);

    public String deleteEmployeeFamily(FamilyRelation familyRelation);

    public String updateEmployeeFamily(FamilyRelation familyRelation);

    public Employee getEmployeeProfile(String empid);

    public Employee getEmployeecurrentProfile(String empid);

    public Employee getEmployeeAbstractReport(String empid);

    public Employee getPayfixationunderMacp(String empid);

    public Employee getPayfixationafterMacp(String empid);

    public Employee getPayrevisionDate(String empid);

    public Employee getPayfixationDni(String empid);

    public Employee getEmployeeServiceParticular(String empid);

    public String getEmployeeImage(String empid, String filePath);

    public Punishment[] getEmployeePunishment(String empid, String inputdate);

    public Reward[] getEmployeeReward(String empid, String inputdate);

    public Training[] getEmployeeTraining(String empid, String inputdate);

    public TransferJoining[] getEmployeeTransferAndJoining(String empid, String input);

    public EmployeeLanguage[] getLanguageKnown(String empid);

    public String[] getUpdatedEmpList(String input, String off_code);

    public String[] getEmpList(String off_code);

    public Pensioner getPensionerDetailsThroughAccNo(String gpfno);

    public Loan[] getEmployeeLoan(String empid);

    public ArrayList getEmpServiceList(String empid);

    public ArrayList promotionList(String empid);

    public ArrayList allPosting(String empid);

    public IdentityInfo[] getEmployeeIdInformation(String empid);

    public Pensioner getPensionerDetailsThroughHRMSID(String empid);

    public ArrayList getOfficeWiseBillGroupWiseEmployeeList(String offCode);

    public ArrayList getOfficeWiseBillGroupWiseContractualEmployeeList(String offCode);

    public ArrayList getOfficeWiseEmployeeList(String offCode);

    public ArrayList getOfficeWiseEmployeeListForDp(String offCode);

    public ArrayList getOfficeWisePostWiseEmployeeList(String offCode, String gpc);

    public ArrayList getDDOEmployeeList(String offCode);

    public EmployeePayProfile getEmployeePayProfile(String empid);

    public EmployeeSearchResult SearchEmployee(SearchEmployee searchEmployee);

    public EmployeeSearchResult SearchEmployeeYearWise(SearchEmployee searchEmployee);

    public EmployeeSearchResult SearchEmployeeAG(SearchEmployee searchEmployee);

    public EmployeeSearchResult SearchEmployeeAGYearWise(SearchEmployee searchEmployee);

    public EmployeeSearchResult LocateEmployee(SearchEmployee searchEmployee);

    public EmployeeSearchResult LocateEmployee1(SearchEmployee searchEmployee);

    public Address[] getAddress(String empid);

    public Address getAddressDetals(String empid, int addressId);

    public void saveAddressData(Address address);

    public List getIdentityList(String empid);

    public int saveEmployeeMessage(EmployeeMessage employeemessage);

    public int saveReplyEmployeeMessage(EmployeeMessage employeemessage);

    public int getSentMessageListcount(String senderid);

    public List getSentMessageList(String senderid, int pgno);

    public List getMessageAttachment(int messageId, String reftype);

    public EmployeeMessage getAttachedFile(int attachmentid);

    public List getEmployeeMessageList(String empid);

    public void uploadMultipleAttachedFile(int messageId, String refType, List<MultipartFile> files);

    public void uploadAttachedFile(int messageId, String refType, MultipartFile file);

    public EmployeeMessage getMessageDetails(int messageId);

    public List getOffWiseEmpList(String offCode);

    public List getDeptWiseEmpList(String deptCode);

    public void saveProfile(Employee emp);

    public void saveLanguageKnown(String empid, EmployeeLanguage language);

    public void updateLanguageKnown(String empid, EmployeeLanguage language);

    public void deleteLanguageKnown(String empid, int slno);

    public EmployeeLanguage editLanguage(String empid, int slno);

    public String getSuperannuationdate(String empId, String retiredYear);

    public void DownlaodOfficeWiseEmployee(OutputStream out, String fileName, WritableWorkbook workbook, int month, int year, String ocode);

    public void updateIdentity(IdentityInfo identity);

    public IdentityInfo saveIdentity(IdentityInfo identity);

    public IdentityInfo getIdentityData(String idDesc, String empId);

    public Employee ResetEmpoyeePassword(String empid, String dcLoginId);

    public Employee EditEmpoyeeData(String empid);

    public List specialSubCategory();

    public String updateEmployeeData(Employee employee, String dcLoginId);

    public FamilyRelation getFamilyRelationData(int slno, String empid);

    public String employeeCommunicationCount(String officeStatus, String empId, String officeCode);

    public List viewCommunicationDetails(String officeStatus, String empId, String officeCode);

    public List viewCommunicationMessage(String officeStatus, String empId, String officeCode, int messageId);

    public EmployeeMessage downloadCommunicationMessage(int messageId);

    public EmployeeMessage getParentMessageDetail(int messageId);

    public Employee getEmpoyeeData(String gpfNo);

    public ArrayList getEmployeePostCode(String empId);

    public void updateEmpRegularToSixYrContractual(String empid);

    public ArrayList getContractualJobCategory();

    public void saveContractualEmpData(SearchEmployee employee);

    public Employee EditEmpoyeeDataAdmin(String empid);

    public String updateEmployeeDataAdmin(Employee employee, String dcLoginId);

    public ArrayList empDepType();

    public ArrayList empPayScale();

    public void updateProfileData(Employee employee, String empId, boolean isCmpleted);

    public void updateDDOProfileData(Employee employee, String empId, boolean isCmpleted);

    public List getEmployeeProfileForVerificationDataList(String offcode);

    public Employee getEmployeeProfileDraftData(String empId);

    public void updateProfileCompletedStatus(String empId);

    public boolean isprofileCompleted(String empId);

    public List getPostingInformation(String empId);

    public void saveAddress(Address empaddress);

    public void saveProfileVerificationData(Employee emp, String loginid, String mode);

    public void deleteIdentity(IdentityInfo identity);

    public ArrayList empPostgrptype();

    public Employee getEmployeeProfileForDC(String empid);

    public Employee getEmployeeProfileDetails(String empid);

    public int deleteProfileAddress(int addressId);

    public int deleteEmployeeAddress(int addressId);

    public boolean checkDuplicateIdentity(String empId, String identityType, String idno);

    public boolean duplicateMobile(String empId, String mobile);

    public String isProfileVerified(String empId);

    public ArrayList DeptWiseProfileUpdate();

    public ArrayList DDOWiseprofileUpdate(String dcode);

    public ArrayList EmpwiseprofileUpdate(String ddoCode);

    public String getAadhaarAndPanData(String empId);

    public void updateIdentityForDC(IdentityInfo identity);

    public PensionProfile getpensionProfileDetails(String empid);

    public String verifyEmpIdfor7thPayCommission(String logindistcode, String empid);

    public int updateEmpIdfor7thPayCommission(String empid, String paycommissionValue);

    public boolean verifyEmployeeDistrict(String logindistcode, String empid);

    public List getbankAccountChangeRequestList(String loginEmpId);

    public List getMobileNumberChangeList(String loginEmpId);

    public String updateBankAccountNumber(AqmastModel emp, String loginEmpId, String filepath);

    public String updateMobileNumber(Employee emp, String loginEmpId);

    public void unlockProfileDDO(String empId, String deptCode);

    public void savePrevpensionDetails(PreviousPensionDetails prevpensionForm);

    public void updatePrevpensionDetails(PreviousPensionDetails prevpensionForm);

    public List getPrevpensionList(String empid);

    public PreviousPensionDetails editPrevPensionDetails(PreviousPensionDetails prevpensionForm);

    public void deletePreviousPensionDetails(String pensionId);

    public Employee ResetEmployeeEncodedPassword(String empid, String dcLoginId);

    public void saveUserDetails(EmployeeRegistrationBean employeeRegistrationBean);

    public ArrayList getUserDetails();

    public List getFamilyDetails(String empid);

    public void downloadEmployeeDetailsPDF(PdfWriter writer, Document document, String empId, String photopath);

    public void saveAdminLog(String modName, String usrname, String usertype, String empid, String msg);

    public String getempDDOchargefficeList(String empid);

    public Address getPermanentAddress(String empid);

    public Address getPermanentAddressForAcknowledgement(String empid);

    public void profileCompletedLog(String empid, String completedby, String verifiedby);

    public Employee getProfileCompletedStatus(String empid);

    public int getAddressCount(String empId, String addressType);

    public void SaveAadharTransaction(String empId, String transactionId, String oldAadhar, String aadharNo);

    public void UpdateAadharTransaction(String empId, String uidToken, String txn, String aadhar);

    public void UpdateAadhar(String empId, String aadhar);

    public int countDuplicateAadhar(String aadhar, String empId);

    public boolean stopGpfDeduction(String empId);

    public Employee getProfileVerifiedStatus(String empid);

    public PensionProfile getPensionEmpDetails(String empid);
    
    public Address getAddressForServiceBook(String empid);
} 
