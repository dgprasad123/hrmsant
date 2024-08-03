/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.loancorrection;

import hrms.dao.employee.EmployeeDAO;
import hrms.dao.loancorrection.LTACorrectionDAO;
import hrms.model.employee.Employee;
import hrms.model.loan.LTA;
import hrms.model.loan.Loan;
import hrms.model.login.LoginUserBean;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Madhusmita
 */
@Controller

@SessionAttributes("LoginUserBean")
public class LTACorrectionController {

    @Autowired
    LTACorrectionDAO ltaDAO;
    
    @Autowired
    EmployeeDAO employeeDAO;

    @RequestMapping(value = "ltaCorrectionList")
    public String getLTAList(@ModelAttribute("LoginUserBean") LoginUserBean lub, ModelMap model, @ModelAttribute("ltaCorrection") Loan emploan) {
        String path = "/payroll/LTACorrection";
        return path;
    }

    @RequestMapping(value = "ltaCorrection")
    public ModelAndView getEmployeeLoanList(@ModelAttribute("LoginUserBean") LoginUserBean lub, ModelMap model, HttpServletResponse response, @ModelAttribute("ltaCorrection") Loan emploan) {
        String path = "/payroll/LTACorrection";
        ModelAndView mav = new ModelAndView();
        ArrayList loanList = ltaDAO.getEmployeeLoanList(emploan);
        mav.addObject("loanList", loanList);
        mav.setViewName(path);
        return mav;

    }

    /*@ResponseBody
     @RequestMapping(value = "ltaCorrection", method = {RequestMethod.GET, RequestMethod.POST})
     public void getCadreList(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("ltaCorrection") Loan loan, @RequestParam("empid") String empid) throws IOException {
     response.setContentType("application/html");
     PrintWriter out = response.getWriter();
     String content = null;
     try {
     content = ltaDAO.getLoanList(loan.getEmpid());
     out.print(content);
     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     out.flush();
     out.close();
     }

     }*/
    @RequestMapping(value = "LTAdvanceList")
    public String getLTAdvance(@ModelAttribute("LoginUserBean") LoginUserBean lub, ModelMap model, @ModelAttribute("ltAdvance") LTA ltAdvance) {
        String path = "/payroll/LTAdvanceList";
        return path;
    }

    @RequestMapping(value = "GetLTAdvanceList")
    public ModelAndView getLTAdvanceList(@ModelAttribute("LoginUserBean") LoginUserBean lub, ModelMap model, @ModelAttribute("ltAdvance") LTA ltAdvance) {
        String path = "/payroll/LTAdvanceList";
        ModelAndView mav = new ModelAndView();
        ArrayList ltaAdvList = ltaDAO.getLtaList(ltAdvance.getFinYear(), ltAdvance.getLoanType(), ltAdvance.getLoaneeId());
        mav.addObject("ltaList", ltaAdvList);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "getEmployeeLTAdvanceList")
    public ModelAndView getEmployeeLTAdvanceList(@ModelAttribute("LoginUserBean") LoginUserBean lub, ModelMap model, @ModelAttribute("ltAdvance") LTA ltAdvance) {
        String path = "/payroll/EmployeeLTAdvanceList";
        ModelAndView mav = new ModelAndView();
        Employee emp=employeeDAO.getEmployeeProfile(lub.getLoginempid());
        String gpfno=emp.getGpfno();
        ArrayList ltaAdvList = ltaDAO.getLtaList(ltAdvance.getFinYear(), ltAdvance.getLoanType(), gpfno);
        mav.addObject("ltaList", ltaAdvList);
        mav.setViewName(path);
        return mav;
    }
}
