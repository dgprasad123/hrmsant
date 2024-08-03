package hrms.controller.AGPension;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.controller.servicebook.ServiceBookHeaderFooter;
import hrms.dao.AGPension.AGPensionDAO;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.servicebook.ServiceBookDAO;
import hrms.model.AGPension.AGPensionBean;
import hrms.model.AGPension.AGPensionForm;
import hrms.model.employee.Address;
import hrms.model.employee.Education;
import hrms.model.employee.Employee;
import hrms.model.employee.FamilyRelation;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("SelectedEmpObj")
public class AGPensionController {

    @Autowired
    public AGPensionDAO agpensionDAO;
    
    @Autowired
    EmployeeDAO employeeDAO;
    
     @Autowired
    ServiceBookDAO serviceBookDao;

    @RequestMapping(value = "SearchGPFNoPage")
    public String SearchGPFNoPage(@ModelAttribute("agPensionForm") AGPensionForm agpform) {
        return "/AGPension/SearchGPFNo";
    }

    @RequestMapping(value = "SearchGPFNo", params = {"btnSubmit=Search"})
    public ModelAndView SearchGPFNo(Model model, @ModelAttribute("agPensionForm") AGPensionForm agpform) {
        ModelAndView mav = new ModelAndView();
        try {
            AGPensionBean agpbean = agpensionDAO.getGPFNoDetails(agpform.getTxtGPFNo());
            if (agpbean != null && agpbean.getEmpid() != null && !agpbean.getEmpid().equals("")) {
                mav.addObject("empdata", agpbean);
                
                Users selectedEmpObj = new Users();
                selectedEmpObj.setEmpId(agpbean.getEmpid());
                selectedEmpObj.setGpfno(agpform.getTxtGPFNo());
                selectedEmpObj.setFullName(agpbean.getEmpname());
                mav.addObject("SelectedEmpObj", selectedEmpObj);
            }
            mav.setViewName("/AGPension/SearchGPFNo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "viewAnnexure")
    public ModelAndView viewAnnexure(Model model, @ModelAttribute("agPensionForm") AGPensionForm agpform,@RequestParam("empid") String empid) {
        ModelAndView mav = new ModelAndView();

        try {

            mav.addObject("empidnew",empid);
            mav.setViewName("/AGPension/viewAnnexure");
            
            Date todaysDate = new Date();
            DateFormat df3 = new SimpleDateFormat("dd-MMM-yyyy");
            String str3 = df3.format(todaysDate);
            
            Employee employeeProfile = employeeDAO.getEmployeeProfile(empid);
            Education[] educations = employeeDAO.getEmployeeEducation(empid, str3);
            FamilyRelation[] familyRel = employeeDAO.getEmployeeFamily(empid);
            Address[] address = employeeDAO.getAddress(empid);
            List identity = employeeDAO.getIdentityList(empid);
             
            
            String fatherDetails = "";

            List familyList = Arrays.asList(familyRel);
            if (familyList != null && familyList.size() > 0) {
                FamilyRelation familyRelation = null;
                for (int i = 0; i < familyList.size(); i++) {
                    familyRelation = (FamilyRelation) familyList.get(i);
                    if (familyRelation.getRelation() != null && familyRelation.getRelation().equals("FATHER")) {
                        fatherDetails = StringUtils.defaultString(familyRelation.getInitials()) + " " + StringUtils.defaultString(familyRelation.getFname()) + " " + StringUtils.defaultString(familyRelation.getMname()) + " " + StringUtils.defaultString(familyRelation.getLname());
                    }
                }
            }

            mav.addObject("employeeProfile", employeeProfile);
            mav.addObject("educations", educations);
            mav.addObject("fatherDetails", fatherDetails);
            mav.addObject("familyRel", familyRel);
            mav.addObject("address", address);
            mav.addObject("identity", identity);
            
            
            
            List annexureAList = serviceBookDao.getServiceBookAnnexureAData(empid);
            mav.addObject("annexureAList", annexureAList);
            
            List annexureBList = serviceBookDao.getServiceBookAnnexureBData(empid);
            mav.addObject("annexureBList", annexureBList);
            
            List annexureDList = serviceBookDao.getServiceBookAnnexureDData(empid);
            mav.addObject("annexureDList", annexureDList);
            
            List annexureEList = serviceBookDao.getServiceBookAnnexureEData(empid);
             mav.addObject("annexureEList", annexureEList);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;

    }
    
        
        
        
        
}
