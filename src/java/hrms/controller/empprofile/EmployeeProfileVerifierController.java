/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.empprofile;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.FooterPageEvent;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.performanceappraisal.PARBrowserDAOImpl;
import hrms.model.employee.Address;
import hrms.model.employee.Education;
import hrms.model.employee.Employee;
import hrms.model.employee.EmployeeLanguage;
import hrms.model.employee.FamilyRelation;
import hrms.model.employee.IdentityInfo;
import hrms.model.employee.employeeFamilyHelperBean;
import hrms.model.login.LoginUserBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Surendra
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class EmployeeProfileVerifierController implements ServletContextAware {

    @Autowired
    public EmployeeDAO employeeDAO;

    @Autowired
    public PARBrowserDAOImpl parbrowserDao;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext sc) {
        this.context = sc;
    }

    @RequestMapping(value = "showProfileReportController", method = RequestMethod.GET)
    public ModelAndView showProfileReport(@ModelAttribute("Employee") Employee emp, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String path = "/employee/EmployeeProfileDataReport";

        ModelAndView mav = new ModelAndView(path, "command", emp);
        List li = employeeDAO.getEmployeeProfileForVerificationDataList(lub.getLoginoffcode());
        mav.addObject("EmpList", li);
        return mav;
    }

    @RequestMapping(value = "viewProfileVerificationController", method = RequestMethod.GET)
    public ModelAndView viewProfileVerificationController(@ModelAttribute("Employee") Employee emp, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, @RequestParam Map<String, String> param, HttpServletResponse response) {
        ModelAndView mav = null;
        String path = "/employee/EmployeeProfileDetailPage";
        String isAadharVerified = "N"; 

        try {
            emp.setEmpid(param.get("empId"));
            mav = new ModelAndView(path, "command", emp);
            Employee empdata = employeeDAO.getEmployeeProfile(param.get("empId"));

            List identityInfoList = employeeDAO.getIdentityList(emp.getEmpid());
            for (int i = 0; i < identityInfoList.size(); i++) {
                IdentityInfo idInfo = (IdentityInfo) identityInfoList.get(i);
                if (idInfo.getIdentityDocType() == "AADHAAR" && idInfo.getIsVerified() == "Y") {
                    isAadharVerified = "Y";
                }
            }
            mav.addObject("IdList", identityInfoList);

            EmployeeLanguage[] languageinfo = employeeDAO.getLanguageKnown(param.get("empId"));
            List<EmployeeLanguage> languagelist = new ArrayList<EmployeeLanguage>(Arrays.asList(languageinfo));

            Address permAddress = employeeDAO.getPermanentAddressForAcknowledgement(emp.getEmpid());
            int hasPermAddress = employeeDAO.getAddressCount(emp.getEmpid(), "PERMANENT");
            int hasPresentAddress = employeeDAO.getAddressCount(emp.getEmpid(), "PRESENT");

            FamilyRelation[] familyinfo = employeeDAO.getEmployeeFamily(param.get("empId"));
            List<FamilyRelation> familylist = new ArrayList<FamilyRelation>(Arrays.asList(familyinfo));

            Education[] educationinfo = employeeDAO.getEmployeeEducation(param.get("empId"));
            List<Education> educationlist = new ArrayList<Education>(Arrays.asList(educationinfo));

            mav.addObject("emp", empdata);
            // mav.addObject("IdList", identityInfoList);
            mav.addObject("isAadharVerified", isAadharVerified);
            mav.addObject("languageList", languagelist);

            mav.addObject("address", permAddress);
            mav.addObject("hasPermAddress", hasPermAddress);
            mav.addObject("hasPresentAddress", hasPresentAddress);

            mav.addObject("familyList", familylist);
            mav.addObject("educationList", educationlist);

            mav.addObject("verifierName", parbrowserDao.getEmployeeName(lub.getLoginempid()));
            mav.addObject("employeeName", parbrowserDao.getEmployeeName(param.get("empId")));

            Employee empprofilecompletedstatus = employeeDAO.getProfileVerifiedStatus(param.get("empId"));
            mav.addObject("empprofilecompletedstatus", empprofilecompletedstatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveProfileVerification", params = "btnSave=Approve")
    public ModelAndView ApproveProfileVerification(@ModelAttribute("Employee") Employee emp, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        try {
            employeeDAO.saveProfileVerificationData(emp, lub.getLoginempid(), "Approve");
            employeeDAO.profileCompletedLog(emp.getEmpid(), lub.getLoginempid(), lub.getLoginempid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/showProfileReportController.htm");
    }

    @RequestMapping(value = "saveProfileVerification", params = "btnSave=Decline")
    public ModelAndView DeclineProfileVerification(@ModelAttribute("Employee") Employee emp, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        try {
            employeeDAO.saveProfileVerificationData(emp, lub.getLoginempid(), "Decline");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/showProfileReportController.htm");
    }

    @RequestMapping(value = "downloadEmployeeDetails")
    public void downloadEmployeeDetails(HttpServletResponse response, @RequestParam(value = "empId", required = false) String empId, @ModelAttribute("Employee") Employee emp, @ModelAttribute("LoginUserBean") LoginUserBean lub) throws DocumentException {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        final Font headingRule = FontFactory.getFont("Arial", 15, Font.BOLD);

        try {
            String photoPath = context.getInitParameter("PhotoPath");

            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            FooterPageEvent event = new FooterPageEvent();
            Rectangle rect = new Rectangle(150, 30, 550, 800);
            writer.setBoxSize("art", rect);
            writer.setPageEvent(event);
            document.open();

            employeeDAO.downloadEmployeeDetailsPDF(writer, document, empId, photoPath);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "empProfileDetailsNew")
    public ModelAndView empProfileDetailsNewPage(@ModelAttribute("Employee") Employee emp, @RequestParam Map<String, String> param) {
        Employee empdata = null;
        List familyData = null;
        try {
            empdata = employeeDAO.getEmployeeProfileDetails(param.get("empId"));
            familyData = employeeDAO.getFamilyDetails(param.get("empId"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String path = "/employee/EmployeeProfileDetailPageNew";
        ModelAndView mav = new ModelAndView();
        mav.addObject("EmpData", empdata);
        mav.addObject("FamilyData", familyData);
        mav.setViewName(path);
        return mav;
    }

    /*@RequestMapping(value = "viewProfileVerificationControllerDC")
     public ModelAndView viewProfileVerificationControllerDC(@RequestParam Map<String, String> param) {

     String path = "/employee/EmployeeProfileDetailPageDC";

     ModelAndView mav = new ModelAndView();

     Employee empdata = employeeDAO.getEmployeeProfileForDC(param.get("empId"));

     IdentityInfo[] idinfo = employeeDAO.getEmployeeIdInformation(param.get("empId"));
     List<IdentityInfo> idList = new ArrayList<IdentityInfo>(Arrays.asList(idinfo));

     String identityData = "";
     String identityVerificationData = "";
     if (idList != null && idList.size() > 0) {
     for (int i = 0; i < idList.size(); i++) {
     IdentityInfo idinfo1 = (IdentityInfo) idList.get(i);
     identityData += idinfo1.getIdentityDesc();
     }
     }
     if (!identityData.contains("AADHAAR")) {
     identityVerificationData = "AADHAAR needs to be updated";
     }
     mav.addObject("identityVerificationData", identityVerificationData);

     EmployeeLanguage[] languageinfo = employeeDAO.getLanguageKnown(param.get("empId"));
     List<EmployeeLanguage> languagelist = new ArrayList<EmployeeLanguage>(Arrays.asList(languageinfo));

     Address[] addressinfo = employeeDAO.getAddress(param.get("empId"));
     List<Address> addresslist = new ArrayList<Address>(Arrays.asList(addressinfo));

     String permanentAddressData = "";
     String permanentAddressVerificationData = "";
     String presentAddressData = "";
     String presentAddressVerificationData = "";
     if (addresslist != null && addresslist.size() > 0) {
     for (int i = 0; i < addresslist.size(); i++) {
     Address addressinfo1 = (Address) addresslist.get(i);

     if (addressinfo1.getAddressType().equals("PERMANENT")) {
     if (addressinfo1.getAddress() != null && !addressinfo1.getAddress().equals("")) {
     permanentAddressData += "_ADDRESS";
     }
     if (addressinfo1.getBlockCode() != null && !addressinfo1.getBlockCode().equals("")) {
     permanentAddressData += "_BLOCK";
     }
     if (addressinfo1.getPin() != null && !addressinfo1.getPin().equals("")) {
     permanentAddressData += "_PIN";
     }
     } else if (addressinfo1.getAddressType().equals("PRESENT")) {
     if (addressinfo1.getAddress() != null && !addressinfo1.getAddress().equals("")) {
     presentAddressData += "_ADDRESS";
     }
     if (addressinfo1.getBlockCode() != null && !addressinfo1.getBlockCode().equals("")) {
     presentAddressData += "_BLOCK";
     }
     if (addressinfo1.getPin() != null && !addressinfo1.getPin().equals("")) {
     presentAddressData += "_PIN";
     }
     }
     }
     }

     if (!permanentAddressData.contains("ADDRESS") && !permanentAddressData.contains("BLOCK") && !permanentAddressData.contains("PIN")) {
     permanentAddressVerificationData = "WHOLE";
     } else {
     if (!permanentAddressData.contains("ADDRESS")) {
     if (permanentAddressVerificationData == null || permanentAddressVerificationData.equals("")) {
     permanentAddressVerificationData = "(ADDRESS)";
     } else {
     permanentAddressVerificationData = permanentAddressVerificationData + ",(ADDRESS)";
     }
     }
     if (!permanentAddressData.contains("BLOCK")) {
     if (permanentAddressVerificationData == null || permanentAddressVerificationData.equals("")) {
     permanentAddressVerificationData = "BLOCK";
     } else {
     permanentAddressVerificationData = permanentAddressVerificationData + ",BLOCK";
     }
     }
     if (!permanentAddressData.contains("PIN")) {
     if (permanentAddressVerificationData == null || permanentAddressVerificationData.equals("")) {
     permanentAddressVerificationData = "PIN";
     } else {
     permanentAddressVerificationData = permanentAddressVerificationData + ",PIN";
     }
     }
     }
     mav.addObject("permanentAddressVerificationData", permanentAddressVerificationData);

     if (!presentAddressData.contains("ADDRESS") && !presentAddressData.contains("BLOCK") && !presentAddressData.contains("PIN")) {
     presentAddressVerificationData = "WHOLE";
     } else {
     if (!presentAddressData.contains("ADDRESS")) {
     if (presentAddressVerificationData != null && !presentAddressVerificationData.equals("")) {
     presentAddressVerificationData = presentAddressVerificationData + ",(ADDRESS)";
     } else {
     presentAddressVerificationData = "(ADDRESS)";
     }
     }
     if (!presentAddressData.contains("BLOCK")) {
     if (presentAddressVerificationData != null && !presentAddressVerificationData.equals("")) {
     presentAddressVerificationData = presentAddressVerificationData + ",BLOCK";
     } else {
     presentAddressVerificationData = "BLOCK";
     }
     }
     if (!presentAddressData.contains("PIN")) {
     if (presentAddressVerificationData != null && !presentAddressVerificationData.equals("")) {
     presentAddressVerificationData = presentAddressVerificationData + ",PIN";
     } else {
     presentAddressVerificationData = "PIN";
     }
     }
     }
     mav.addObject("presentAddressVerificationData", presentAddressVerificationData);

     FamilyRelation[] familyinfo = employeeDAO.getEmployeeFamily(param.get("empId"));
     List<FamilyRelation> familylist = new ArrayList<FamilyRelation>(Arrays.asList(familyinfo));

     String familyData = "";
     String familyVerificationData = "";
     if (familylist != null && familylist.size() > 0) {
     for (int i = 0; i < familylist.size(); i++) {
     FamilyRelation familyinfo1 = (FamilyRelation) familylist.get(i);
     familyData += familyinfo1.getRelation();
     }
     }
     if (!familyData.contains("FATHER") && !familyData.contains("HUSBAND")) {
     familyVerificationData = "Either FATHER or HUSBAND needs to be updated";
     }
     mav.addObject("familyVerificationData", familyVerificationData);

     Education[] educationinfo = employeeDAO.getEmployeeEducation(param.get("empId"));
     List<Education> educationlist = new ArrayList<Education>(Arrays.asList(educationinfo));

     mav.addObject("EmpData", empdata);
     mav.addObject("IdList", idList);
     mav.addObject("languageList", languagelist);
     mav.addObject("addressList", addresslist);
     mav.addObject("familyList", familylist);
     mav.addObject("educationList", educationlist);

     mav.setViewName(path);
     return mav;
     }*/
    @RequestMapping(value = "viewProfileVerificationControllerDC")
    public ModelAndView viewProfileVerificationControllerDC(@RequestParam Map<String, String> param) {

        String path = "/employee/EmployeeProfileDetailPageDC";

        ModelAndView mv = new ModelAndView();

        Employee emp = new Employee();
        String empid = param.get("empId");

        emp.setEmpid(empid);
        mv.addObject("emp", emp);
        emp = employeeDAO.getEmployeeProfile(empid);
        mv = new ModelAndView("/employee/EmployeeProfileDetailPageDC", "emp", emp);
        String isAadharVerified = "N";
        //Address[] empAddress = employeeDAO.getAddress(lub.getLoginempid());
        Address permAddress = employeeDAO.getPermanentAddressForAcknowledgement(empid);
        int hasPermAddress = employeeDAO.getAddressCount(empid, "PERMANENT");
        int hasPresentAddress = employeeDAO.getAddressCount(empid, "PRESENT");
        
        mv.addObject("address", permAddress);
        mv.addObject("hasPermAddress", hasPermAddress);
        mv.addObject("hasPresentAddress", hasPresentAddress);
        List identityInfoList = employeeDAO.getIdentityList(empid);
        for (int i = 0; i < identityInfoList.size(); i++) {
            IdentityInfo idInfo = (IdentityInfo) identityInfoList.get(i);
            if (idInfo.getIdentityDocType() == "AADHAAR" && idInfo.getIsVerified() == "Y") {
                isAadharVerified = "Y";
            }
        }
        mv.addObject("identityInfoList", identityInfoList);
        mv.addObject("isAadharVerified", isAadharVerified);
        Education[] educationList = employeeDAO.getEmployeeEducation(empid);
        mv.addObject("educationList", educationList);
        FamilyRelation[] familyRelationList = employeeDAO.getEmployeeFamily(empid);
        mv.addObject("familyRelationList", familyRelationList);

        EmployeeLanguage[] emplanguageList = employeeDAO.getLanguageKnown(empid);
        mv.addObject("emplanguageList", emplanguageList);

        Employee empprofilecompletedstatus = employeeDAO.getProfileCompletedStatus(empid);
        mv.addObject("empprofilecompletedstatus", empprofilecompletedstatus);
        return mv;
    }

    @RequestMapping(value = "SaveAndAcknowledgeEmployeeProfileVerification", params = "btnSave=Approve")
    public ModelAndView SaveAndAcknowledgeEmployeeProfileVerification(@ModelAttribute("Employee") Employee emp, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        try {
            employeeDAO.saveProfileVerificationData(emp, lub.getLoginempid(), "Approve");
            employeeDAO.profileCompletedLog(lub.getLoginempid(), lub.getLoginempid(), lub.getLoginempid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/showProfileReportController.htm");
    }

    @RequestMapping(value = "SaveAndAcknowledgeEmployeeProfileVerification", params = "btnSave=Decline")
    public ModelAndView declineAndAcknowledgeEmployeeProfileVerification(@ModelAttribute("Employee") Employee emp, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        try {
            employeeDAO.saveProfileVerificationData(emp, lub.getLoginempid(), "Decline");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/showProfileReportController.htm");
    }
}
