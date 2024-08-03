/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.controller.empprofile;

import hrms.dao.employee.SearchEmployeeDAO;
import hrms.model.employee.SearchEmployeeDetails;
import hrms.model.login.LoginUserBean;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class SearchEmployeeController {
    
    @Autowired
    public SearchEmployeeDAO SearchEmployeeDAO;
    
    @RequestMapping(value = "loadSearchEmployeeDetailsPage.htm")
    public String loadSearchEmployeeDetailsPage(Model model, @ModelAttribute("searchEmployeeDetails") SearchEmployeeDetails searchEmployeeDetails) {
        try {

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "/employee/searchEmployeeDetails";
    }
       
    @RequestMapping(value = "SearchEmployeeDetails.htm", params = "btnSearch=Search",  method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView searchEmployeeDetails(Model model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("SearchEmployeeDetails") SearchEmployeeDetails searchEmployeeDetails) {
        ModelAndView mv = new ModelAndView();
        List employeeList = SearchEmployeeDAO.getEmployees(searchEmployeeDetails.getFirstName(),
                searchEmployeeDetails.getLastName(), searchEmployeeDetails.getDob(), searchEmployeeDetails.getDesignation(), searchEmployeeDetails.getDepartmentName(), searchEmployeeDetails.getFatherName());

        List departmentList = SearchEmployeeDAO.getDepartment();
        mv.addObject("employeeList", employeeList);
        mv.addObject("departmentList", departmentList);
        mv.setViewName("/employee/searchEmployeeDetails");
        return mv;
    }
    
}
