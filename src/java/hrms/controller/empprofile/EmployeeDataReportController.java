/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.controller.empprofile;

import hrms.dao.employee.EmployeeDAO;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class EmployeeDataReportController {
    @Autowired
    public EmployeeDAO employeeDAO;
    
    @RequestMapping(value = "EmployeeDataReport.htm", method = {RequestMethod.GET})
    public ModelAndView EmployeeDataReport(){
        ModelAndView mv = new ModelAndView();
        ArrayList employeeList = employeeDAO.getOfficeWiseEmployeeList("OLSGAD0010000");
        mv.addObject("employeeList", employeeList);
        mv.setViewName("misreport/EmployeeDataReport");
        return mv;
    }
    
    @RequestMapping(value = "DeptWiseProfileUpdate")
    public ModelAndView DeptWiseProfileUpdate(ModelMap model, HttpServletResponse response) {
        String path = "/misreport/DeptWiseProfileUpdate";
        ModelAndView mav = new ModelAndView();
        ArrayList ProfileDetails = employeeDAO.DeptWiseProfileUpdate();
        mav.addObject("ProfileDetails", ProfileDetails);
        mav.setViewName(path);
        return mav;

    }
    
    @RequestMapping(value = "DDOWiseprofileUpdate")
    public ModelAndView DDOWiseprofileUpdate(ModelMap model, HttpServletResponse responseh, @RequestParam("dcode") String dcode) {
        String path = "/misreport/DDOWiseProfileUpdate";
        ModelAndView mav = new ModelAndView();
        ArrayList DDOProfileDetails = employeeDAO.DDOWiseprofileUpdate(dcode);
        mav.addObject("DDOProfileDetails", DDOProfileDetails);
        mav.setViewName(path);
        return mav;

    }
    
    @RequestMapping(value = "EmpwiseprofileUpdate")
    public ModelAndView EmpwiseprofileUpdate(ModelMap model, HttpServletResponse responseh, @RequestParam("ddoCode") String ddoCode) {
        String path = "/misreport/EmpwiseprofileUpdate";
        ModelAndView mav = new ModelAndView();
        ArrayList EmpProfileDetails = employeeDAO.EmpwiseprofileUpdate(ddoCode);
        mav.addObject("EmpProfileDetails", EmpProfileDetails);
        mav.setViewName(path);
        return mav;

    }
}
