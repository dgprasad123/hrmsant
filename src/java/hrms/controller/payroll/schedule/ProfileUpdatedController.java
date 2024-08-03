/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.schedule;

import hrms.dao.payroll.schedule.ProfileUpdatedDAO;
import hrms.model.employee.Employee;
import hrms.model.login.LoginUserBean;
import java.util.List;
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
 * @author manisha
 */
@Controller
@SessionAttributes({"LoginUserBean"})
public class ProfileUpdatedController {

    @Autowired
    public ProfileUpdatedDAO profileUpdatedDAO;

    @RequestMapping(value = "EmployeeProfileList.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView getEmployeeProfileList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("billNo") String billNo, @ModelAttribute("employee") Employee employee) {
        ModelAndView mav = new ModelAndView("/payroll/ProfileUpdatedEmpList");
       List employeeList = profileUpdatedDAO.getProfileUpdatedEmployeeList(billNo);
        mav.addObject("employeeList", employeeList);
        return mav;
    }
}
