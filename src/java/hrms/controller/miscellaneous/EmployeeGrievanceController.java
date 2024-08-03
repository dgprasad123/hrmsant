/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.miscellaneous;

import hrms.dao.employee.EmployeeDAO;
import hrms.dao.miscellaneous.EmployeeGrievanceDAO;
import hrms.dao.workflowrouting.WorkflowRoutingDAO;
import hrms.model.empinfo.EmployeeGrievance;
import hrms.model.empinfo.GrievnceCommunication;
import hrms.model.empinfo.SMSGrievance;
import hrms.model.login.LoginUserBean;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas
 */
@Controller
@SessionAttributes({"LoginUserBean", "Privileges"})
public class EmployeeGrievanceController {

    @Autowired
    EmployeeGrievanceDAO employeeGrievanceDAO;
    @Autowired
    WorkflowRoutingDAO workflowRoutingDao;
    @Autowired
    EmployeeDAO employeeDAO;

    @RequestMapping(value = "employeeGrievanceList")
    public ModelAndView employeeGrievanceList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        List grievncelist = employeeGrievanceDAO.getEmployeeGrievnceList(lub.getLoginempid());
        mv.addObject("grievncelist", grievncelist);
        mv.setViewName("/grievance/EmployeeGrievanceList");
        return mv;
    }

    @RequestMapping(value = "adminGrievanceDashBoard")
    public ModelAndView adminGrievanceDashBoard(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("dashBoardDetail", employeeGrievanceDAO.getDashBoardDetail(lub.getLoginoffcode()));
        mv.setViewName("/grievance/AdminGrievanceDashBoard");
        return mv;
    }
    @RequestMapping(value = "adminGrievanceDashBoardDtl")
    public ModelAndView adminGrievanceDashBoardDtl(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam(value="status", required = false) String status) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("grievncelist", employeeGrievanceDAO.getDashBoardDetail(lub.getLoginoffcode(),status));
        mv.setViewName("/grievance/AdminGrievanceDashBoardDtl");
        return mv;
    }
    @RequestMapping(value = "adminGrievanceReport")
    public ModelAndView adminGrievanceReport(){
        ModelAndView mv = new ModelAndView();
        mv.addObject("BattalionwiseDashBoardDetail", employeeGrievanceDAO.getBattalionwiseDashBoardDetail());
        mv.setViewName("/grievance/AdminGrievanceReport");
        return mv;
    }
    @RequestMapping(value = "adminGrievanceList")
    public ModelAndView adminGrievanceList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmployeeGrievanceForm") EmployeeGrievance employeeGrievance) {
        ModelAndView mv = new ModelAndView();
        String status = "P";
        String categoryCode = "0";
        if(employeeGrievance.getGrievanceStatus() != null){
            status = employeeGrievance.getGrievanceStatus();
        }
        if(employeeGrievance.getCategoryCode() != null){
            categoryCode = employeeGrievance.getCategoryCode();
        }        
        List grievncelist = employeeGrievanceDAO.getAdminGrievnceList(lub.getLoginspc(),categoryCode,status);
        mv.addObject("grievncelist", grievncelist);
        List categorylist = employeeGrievanceDAO.getGrievnceCategoryList();
        mv.addObject("categorylist", categorylist);
        mv.addObject("EmployeeGrievanceForm", employeeGrievance);
        mv.setViewName("/grievance/AdminGrievanceList");
        return mv;
    }

    @RequestMapping(value = "adminGrievanceDetail")
    public ModelAndView adminGrievanceDetail(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmployeeGrievanceForm") EmployeeGrievance employeeGrievance) {
        ModelAndView mv = new ModelAndView();
        EmployeeGrievance employeeGrievance1 = employeeGrievanceDAO.getGrievanceDetail(employeeGrievance.getGid());        
        mv.addObject("grievanceDetail", employeeGrievance1);
        mv.addObject("communicationList", employeeGrievanceDAO.getCommunicationList(employeeGrievance.getGid()));
        mv.addObject("authlist", workflowRoutingDao.getWorkFlowRoutingList(12, lub.getLogingpc(), lub.getLoginoffcode()));
        GrievnceCommunication GrievnceCommunicationForm = new GrievnceCommunication();
        GrievnceCommunicationForm.setGid(employeeGrievance.getGid());
        mv.addObject("GrievnceCommunicationForm", GrievnceCommunicationForm);
        mv.setViewName("/grievance/AdminEmployeeGrievanceDtls");
        return mv;
    }

    @RequestMapping(value = "adminGrievanceTakeAction")
    public String adminGrievanceTakeAction(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("GrievnceCommunicationForm") GrievnceCommunication grievnceCommunication) {
        ModelAndView mv = new ModelAndView();
        grievnceCommunication.setFromEmployee(lub.getLoginempid());
        grievnceCommunication.setFromEmployeeSPC(lub.getLoginspc());
        if (grievnceCommunication.getActiontaken().equalsIgnoreCase("dispose")) {
            int gcommid = employeeGrievanceDAO.disposeGrievance(grievnceCommunication);
            if (!grievnceCommunication.getGrivAttachment().isEmpty()) {
                employeeGrievanceDAO.uploadAttachedFile(gcommid, grievnceCommunication.getGrivAttachment(), "GRIEVANCECOMM");
            }
        } else if (grievnceCommunication.getActiontaken().equalsIgnoreCase("reject")) {
            int gcommid = employeeGrievanceDAO.rejectGrievance(grievnceCommunication);
            if (!grievnceCommunication.getGrivAttachment().isEmpty()) {
                employeeGrievanceDAO.uploadAttachedFile(gcommid, grievnceCommunication.getGrivAttachment(), "GRIEVANCECOMM");
            }
        } else if (grievnceCommunication.getActiontaken().equalsIgnoreCase("forward")) {
            grievnceCommunication.setFromEmployee(lub.getLoginempid());
            grievnceCommunication.setFromEmployeeSPC(lub.getLoginspc());
            int gcommid = employeeGrievanceDAO.forwardGrievance(grievnceCommunication);
            if (!grievnceCommunication.getGrivAttachment().isEmpty()) {
                employeeGrievanceDAO.uploadAttachedFile(gcommid, grievnceCommunication.getGrivAttachment(), "GRIEVANCECOMM");
            }
        }
        return "redirect:adminGrievanceList.htm";
    }

    @RequestMapping(value = "grievancecommunications")
    public ModelAndView grievancecommunications(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmployeeGrievanceForm") EmployeeGrievance employeeGrievance) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("communicationList", employeeGrievanceDAO.getCommunicationList(employeeGrievance.gid));
        mv.addObject("grievanceDetail", employeeGrievanceDAO.getGrievanceDetail(employeeGrievance.gid));
        mv.setViewName("/grievance/GrievanceCommunications");
        return mv;
    }

    @RequestMapping(value = "adminGrievanceEntry")
    public ModelAndView adminGrievanceEntry(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmployeeGrievanceForm") EmployeeGrievance employeeGrievance) {
        ModelAndView mv = new ModelAndView();
        List categorylist = employeeGrievanceDAO.getGrievnceCategoryList();
        mv.addObject("categorylist", categorylist);
        employeeGrievance = new EmployeeGrievance();
        employeeGrievance.setSource("Web");
        employeeGrievance.setAppoffcode(lub.getLoginoffcode());
        mv.addObject("empList", employeeDAO.getOffWiseEmpList(lub.getLoginoffcode()));
        mv.addObject("authlist", workflowRoutingDao.getWorkFlowRoutingList(12, lub.getLogingpc(), lub.getLoginoffcode()));
        mv.addObject("EmployeeGrievanceForm", employeeGrievance);
        mv.setViewName("/grievance/AdminGrievanceEntry");
        return mv;
    }

    @RequestMapping(value = "employeeGrievanceEntry")
    public ModelAndView employeeGrievanceEntry(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmployeeGrievanceForm") EmployeeGrievance employeeGrievance) {
        ModelAndView mv = new ModelAndView();
        List categorylist = employeeGrievanceDAO.getGrievnceCategoryList();
        mv.addObject("categorylist", categorylist);
        employeeGrievance = new EmployeeGrievance();
        employeeGrievance.setSource("Web");
        employeeGrievance.setHrmsid(lub.getLoginempid());
        employeeGrievance.setAppoffcode(lub.getLoginoffcode());
        employeeGrievance.setAppmobile(lub.getLoginmobile());
        employeeGrievance.setSpc(lub.getLoginspc());        
        mv.addObject("authlist", workflowRoutingDao.getWorkFlowRoutingList(12, lub.getLogingpc(), lub.getLoginoffcode()));
        mv.addObject("EmployeeGrievanceForm", employeeGrievance);
        mv.setViewName("/grievance/EmployeeGrievanceEntry");
        return mv;
    }

    @RequestMapping(value = "saveEmployeeGrievnceByAdmin", params = {"action=Save"})
    public String saveEmployeeGrievnceByAdmin(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmployeeGrievanceForm") EmployeeGrievance employeeGrievance) {
        String authCode = lub.getLoginempid() + "-" + lub.getLoginspc();
        employeeGrievance.setAuthCode(authCode);
        int gid = employeeGrievanceDAO.saveEmployeeGrievnceByAdmin(employeeGrievance);
        if (!employeeGrievance.getAttachment().isEmpty()) {            
            employeeGrievanceDAO.uploadAttachedFile(gid, employeeGrievance.getAttachment(), "GRIEVANCE");
        }
        return "redirect:adminGrievanceList.htm";
    }

    @RequestMapping(value = "saveEmployeeGrievance", params = {"action=Save"})
    public String saveEmployeeGrievance(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmployeeGrievanceForm") EmployeeGrievance employeeGrievance) {
        int gid = 0;
        if(employeeGrievance.getGid() == 0){
            gid = employeeGrievanceDAO.saveEmployeeGrievnce(employeeGrievance);
        }else{
            gid = employeeGrievance.getGid();
            employeeGrievanceDAO.updateEmployeeGrievnce(employeeGrievance);
        }
        if (!employeeGrievance.getAttachment().isEmpty()) {
            employeeGrievanceDAO.uploadAttachedFile(gid, employeeGrievance.getAttachment(), "GRIEVANCE");
        }
        return "redirect:employeeGrievanceList.htm";
    }

    @RequestMapping(value = "saveEmployeeGrievance", params = {"action=Cancel"})
    public String cancelEmployeeGrievance(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmployeeGrievanceForm") EmployeeGrievance employeeGrievance) {
        return "redirect:employeeGrievanceList.htm";
    }

    @RequestMapping(value = "employeeGrievanceDtls")
    public ModelAndView employeeGrievanceDtls(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmployeeGrievanceForm") EmployeeGrievance employeeGrievance) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("grievanceDetail", employeeGrievanceDAO.getGrievanceDetail(employeeGrievance.gid));
        mv.setViewName("/grievance/EmployeeGrievanceDtls");
        return mv;
    }

    @RequestMapping(value = "saveSMSGrievance")
    @ResponseBody
    public String saveSMSGrievance(@ModelAttribute("SMSGrievanceForm") SMSGrievance smsGrievance) {
        employeeGrievanceDAO.saveSMSGrievance(smsGrievance);

        return null;
    }

    @RequestMapping(value = "editEmployeeGrievance")
    public ModelAndView editEmployeeGrievance(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("gid") int gid) {
        ModelAndView mv = new ModelAndView();
        List categorylist = employeeGrievanceDAO.getGrievnceCategoryList();                
        mv.addObject("authlist", workflowRoutingDao.getWorkFlowRoutingList(12, lub.getLogingpc(), lub.getLoginoffcode()));
        mv.addObject("categorylist", categorylist);        
        mv.addObject("EmployeeGrievanceForm", employeeGrievanceDAO.getGrievanceDetail(gid));
        mv.setViewName("/grievance/EmployeeGrievanceEntry");
        return mv;
    }
}
