/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.EnrollmentToInsurance;

import hrms.dao.EnrollmentToInsurance.EnrollmenttoinsuranceDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.EnrollmentToInsurance.Enrollmenttoinsurance;
import hrms.model.login.Users;
import hrms.model.master.Office;
import hrms.model.notification.NotificationBean;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Surendra
 */

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class EnrollmentController {
    
    
    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public OfficeDAO offDAO;
    
    @Autowired
    public NotificationDAO notificationDao;
    
    @Autowired
    public SubStantivePostDAO substantivePostDAO;
    
    @Autowired
    public EnrollmenttoinsuranceDAO enrollmentinsuranceDAO;

    @RequestMapping(value = "EnrollmentToInsurance")
    public ModelAndView EnrollmentToInsurance(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("enrollmentForm") Enrollmenttoinsurance enrollmentForm) {

        ModelAndView mav = null;
        
       
        try {  
            List enrollmentList = enrollmentinsuranceDAO.getEnrollmentList(lub.getEmpId());
            mav = new ModelAndView("/EnrollmentToInsurance/EnrollmentToInsurance"); 
             mav.addObject("EnrollmentList", enrollmentList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "addallNewEnrollment", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView addallnewEnrollment(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("enrollmentForm") Enrollmenttoinsurance enrollmentForm) {
        ModelAndView mav = null;
        
        enrollmentForm.setRadpostingauthtype("GOO");
        enrollmentForm.setRadnotifyingauthtype("GOO");
        
        mav = new ModelAndView("/EnrollmentToInsurance/addNewEnrollmentToInsurance");
        List deptlist = deptDAO.getDepartmentList();
        List fieldofflist = offDAO.getFieldOffList(lub.getOffcode());
        List otherOrgfflist = offDAO.getOtherOrganisationList();
        List schemelist = enrollmentinsuranceDAO.getSchemeList();
        mav.addObject("deptlist", deptlist);
        mav.addObject("fieldofflist", fieldofflist);
        mav.addObject("otherOrgfflist", otherOrgfflist);
        mav.addObject("schemelist",schemelist);
        return mav;

    }
    
    @RequestMapping(value = "saveEnrollment",method = {RequestMethod.GET,RequestMethod.POST}, params = {"action=Save Enrollment"})
    public ModelAndView savenewEnrollment(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("enrollmentForm") Enrollmenttoinsurance enrollmentForm) {
        
        ModelAndView mav = null;
        NotificationBean nb = new NotificationBean();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
         try {
            enrollmentForm.setEmpid(lub.getEmpId());

            nb.setNottype("ENROLLMENT");
            nb.setEmpId(enrollmentForm.getEmpid());            
            nb.setDateofEntry(new Date());
            nb.setOrdno(enrollmentForm.getTxtNotOrdNo());
            if (enrollmentForm.getTxtNotOrdDt() != null && !enrollmentForm.getTxtNotOrdDt().equals("")) {
                nb.setOrdDate(sdf.parse(enrollmentForm.getTxtNotOrdDt()));
            }
            nb.setSancDeptCode(enrollmentForm.getHidNotifyingDeptCode());
            nb.setSancOffCode(enrollmentForm.getHidNotifyingOffCode());
            nb.setRadpostingauthtype(enrollmentForm.getRadnotifyingauthtype());
            if(enrollmentForm.getRadnotifyingauthtype() != null && enrollmentForm.getRadnotifyingauthtype().equals("GOI")){
                nb.setSancAuthCode(enrollmentForm.getHidNotifyingOthSpc());
            }else{
                nb.setSancAuthCode(enrollmentForm.getHidNotiSpc());
            }
            nb.setEntryDeptCode(lub.getDeptcode());
            nb.setEntryOffCode(lub.getOffcode());
            nb.setEntryAuthCode(lub.getCurspc()); 
            nb.setNote(enrollmentForm.getNote());   
            nb.setRadntfnauthtype(enrollmentForm.getRadnotifyingauthtype());
            if(enrollmentForm.getChkNotSBPrint() != null && enrollmentForm.getChkNotSBPrint().equals("Y")){
                nb.setIfVisible("N");
            }else{
                nb.setIfVisible("Y");
            }
            if (enrollmentForm.getHidNotId() > 0) {
                nb.setNotid(enrollmentForm.getHidNotId());
                notificationDao.modifyNotificationData(nb);
                enrollmentinsuranceDAO.updateEnrollment(enrollmentForm);     
            } else {               
                int notid = notificationDao.insertNotificationData(nb);        
                enrollmentinsuranceDAO.saveEnrollment(enrollmentForm, notid);
            }
            mav = new ModelAndView("redirect:/EnrollmentToInsurance.htm", "enrollmentForm", enrollmentForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;  
    }
    
    @RequestMapping(value = "editEnrollment")
    public ModelAndView editEnrollment(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedObj,@ModelAttribute("enrollmentForm") Enrollmenttoinsurance enrollmentForm, @RequestParam Map<String, String> requestParams) throws IOException {

        ModelAndView mav = null;
        Enrollmenttoinsurance enroll= null;
        try {
            String empid = selectedObj.getEmpId();
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            enroll = enrollmentinsuranceDAO.getempEnrollmentData(empid, notificationId);
            enroll.setHidNotId(notificationId);
            enroll.setEmpid(empid);
         
            if (enroll.getRadpostingauthtype() == null || enroll.getRadpostingauthtype().equals("")) {
                enroll.setRadpostingauthtype("GOO");
            }
            if (enroll.getRadnotifyingauthtype() == null || enroll.getRadnotifyingauthtype().equals("")) {
                enroll.setRadnotifyingauthtype("GOO");
            }
            mav = new ModelAndView("/EnrollmentToInsurance/addNewEnrollmentToInsurance","enrollmentForm", enroll);

            List deptlist = deptDAO.getDepartmentList();
            List sancOffList = offDAO.getTotalOfficeList(enroll.getHidNotifyingDeptCode());
            List sancPostList = substantivePostDAO.getSanctioningSPCOfficeWiseList(enroll.getHidNotifyingOffCode());
            List otherOrgfflist = offDAO.getOtherOrganisationList();
            List schemelist = enrollmentinsuranceDAO.getSchemeList();
            
            mav.addObject("deptlist", deptlist);
            mav.addObject("sancOfflist", sancOffList);
            mav.addObject("sancPostlist", sancPostList);
            mav.addObject("otherOrgfflist", otherOrgfflist);
            mav.addObject("schemelist",schemelist);    
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "viewEnrollment")
    public ModelAndView viewEnrollment(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedObj,@ModelAttribute("enrollmentForm") Enrollmenttoinsurance enrollmentForm, @RequestParam Map<String, String> requestParams) throws IOException {
    
        ModelAndView mav = new ModelAndView();

        Enrollmenttoinsurance dptnform = null;
        
        try {
            String empid = selectedObj.getEmpId();

            int notificationId = Integer.parseInt(requestParams.get("notId"));

            dptnform = enrollmentinsuranceDAO.getempEnrollmentData(empid, notificationId);
            dptnform.setHidNotId(notificationId);
            dptnform.setEmpid(empid);
            if (dptnform.getRadpostingauthtype() == null || dptnform.getRadpostingauthtype().equals("")) {
                dptnform.setRadpostingauthtype("GOO");
            }
            if (dptnform.getRadnotifyingauthtype()== null || dptnform.getRadnotifyingauthtype().equals("")) {
                dptnform.setRadnotifyingauthtype("GOO");
            }
            
            mav.addObject("dptnform", dptnform);
            
            String notideptname = deptDAO.getDeptName(dptnform.getHidNotifyingDeptCode());
            mav.addObject("notideptname", notideptname);
            
            Office office = offDAO.getOfficeDetails(dptnform.getHidNotifyingOffCode());
            mav.addObject("notioffice", office.getOffEn());
             
            mav.setViewName("/EnrollmentToInsurance/ViewEnrollment");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "saveEnrollment", params = "action=Back")
    public ModelAndView BackToListPage(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("enrollmentForm") Enrollmenttoinsurance enrollmentForm) {

        ModelAndView mav = null;
        try {
            mav = new ModelAndView("redirect:/EnrollmentToInsurance.htm", "enrollmentForm", enrollmentForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
}
