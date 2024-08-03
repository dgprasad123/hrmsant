/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.termination;


import hrms.dao.employee.EmployeeDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.termination.TerminationDAO;
import hrms.model.employee.Employee;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.termination.Termination;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lenovo
 */
@Controller
@SessionAttributes({"Users", "SelectedEmpObj", "LoginUserBean"})
public class TerminationController {

    @Autowired
    public TerminationDAO terminationDAO;
    @Autowired
    EmployeeDAO employeeDAO;
    @Autowired
    DepartmentDAO departmentDAO;
    @Autowired
    OfficeDAO offDAO;
    @Autowired
    SubStantivePostDAO substantivePostDAO;    

    @RequestMapping(value = "Termination")
    public ModelAndView ShowTerminationForm(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj
            , @ModelAttribute("LoginUserBean") LoginUserBean lub) throws Exception {
        ModelAndView mv = new ModelAndView();
        Employee employee = employeeDAO.getEmployeeProfile(selectedEmpObj.getEmpId());
        Termination termination = terminationDAO.getEmployeeTerminationData(selectedEmpObj.getEmpId());
        //resignation.setDuedate(employee.getDor());
        termination.setRetiredEmpid(employee.getEmpid());
        

        mv.addObject("departmentlist", departmentDAO.getDepartmentList());
        mv.addObject("retFromOfficeList", offDAO.getTotalOfficeList(termination.getRetiredfromdeptCode()));
        mv.addObject("retFromSPCList", substantivePostDAO.getOfficeWithSPCList(termination.getRetiredfromofficeCode()));
        mv.addObject("Termination", termination);
        mv.addObject("employee", employee);
        mv.setViewName("/termination/Termination");
        return mv;        
    }
    @RequestMapping(value = "SaveEmployeeTermination", params="action=Save")
    public ModelAndView SaveEmployeeTermination(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("Termination") Termination termination){
        ModelAndView mv = new ModelAndView();
        
        termination.setEntDeptCode(lub.getLogindeptcode());
        termination.setEntOfficeCode(lub.getLoginoffcode());
        termination.setEntAuthCode(lub.getLoginspc());
        termination.setToe("AN");
        terminationDAO.saveEmployeeTermination(termination);
        Employee employee = employeeDAO.getEmployeeProfile(termination.getRetiredEmpid());        
        Termination termination1 = terminationDAO.getEmployeeTerminationData(selectedEmpObj.getEmpId());
        termination1.setDuedate(employee.getDor());
        mv.addObject("departmentlist", departmentDAO.getDepartmentList());
        mv.addObject("retFromOfficeList",offDAO.getTotalOfficeList(termination1.getRetiredfromdeptCode()));
        mv.addObject("retFromSPCList", substantivePostDAO.getOfficeWithSPCList(termination1.getRetiredfromofficeCode()));
        mv.addObject("Termination", termination1);
        mv.addObject("employee", employee);
        mv.setViewName("redirect:/Termination.htm");
        return mv;
    }
    
    @RequestMapping(value = "SaveEmployeeTermination", params="action=Delete")
    public ModelAndView DeleteEmployeeTermination(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("Termination") Termination termination){
        
        ModelAndView mv = new ModelAndView();
        System.out.println("Inside Termination Delete");
        terminationDAO.deleteEmployeeTermination(termination);
        
        mv.setViewName("redirect:/Termination.htm");
        return mv;
    }
    
    @RequestMapping(value = "saveTerminationData")
    public void saveTerminationData(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("terminationForm") Termination terminationForm) throws ParseException {

        response.setContentType("application/json");
        PrintWriter out = null;
        int Terminationdata = 0;

        try {
            terminationForm.setEmpid(selectedEmpObj.getEmpId());
            Terminationdata = terminationDAO.insertPayrevisionData(terminationForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
     @RequestMapping(value = "editTermination")
    public void editTermination(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam Map<String, String> requestParams) throws IOException {
        Termination terminationform = null;
        try {
            String empid = selectedEmpObj.getEmpId();
            String terminationformId = requestParams.get("notId");
            terminationform = terminationDAO.editTermination(terminationformId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
      @RequestMapping(value = "deleteTermination")
    public String deleteTermination(@RequestParam("notId") String notId) {
        int deletestatus = 0;
        deletestatus = terminationDAO.deleteTermination(notId);
        
          return "/termination/Termination";
    }
    
    @RequestMapping(value = "updateTermination")
    public void updateTerminationData(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("terminationForm") Termination terminationForm) throws ParseException {
        int terminationdata = 0;
        try {
            terminationForm.setEmpid(selectedEmpObj.getEmpId());
            terminationdata = terminationDAO.updateTerminationData(terminationForm);
           

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
