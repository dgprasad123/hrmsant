/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.misreport;

import hrms.dao.employee.EmployeeDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Office;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas Jena
 *
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpOffice"})
public class OfficeWiseReport {

    @Autowired
    EmployeeDAO employeeDAO;

    @RequestMapping(value = "officewiseEmployee")
    public ModelAndView officewiseEmployee(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice) {
        ModelAndView mav = new ModelAndView();
        String path = "misreport/OfficeWiseEmployee";
        ArrayList employees = employeeDAO.getOfficeWiseBillGroupWiseEmployeeList(selectedEmpOffice);
        ArrayList contEmployees = employeeDAO.getOfficeWiseBillGroupWiseContractualEmployeeList(selectedEmpOffice);
        mav.addObject("contEmployees", contEmployees);
        mav.addObject("employees", employees);
        mav.setViewName(path);
        return mav;
    }
    @RequestMapping(value = "granteeofficewiseEmployee")
    public ModelAndView granteeofficewiseEmployee(ModelMap model, HttpServletRequest request, @RequestParam Map<String, String> map) {
        ModelAndView mav = new ModelAndView();
        String path = "report/GranteeOfficeWiseEmployee";
        ArrayList employees = employeeDAO.getOfficeWiseBillGroupWiseEmployeeList(map.get("offCode"));
        ArrayList contEmployees = employeeDAO.getOfficeWiseBillGroupWiseContractualEmployeeList(map.get("offCode"));
        mav.addObject("contEmployees", contEmployees);
        mav.addObject("employees", employees);
        mav.setViewName(path);
        return mav;
    }

}
