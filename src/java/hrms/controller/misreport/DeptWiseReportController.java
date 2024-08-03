/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.misreport;

import hrms.dao.master.DepartmentDAO;
import hrms.dao.misreport.DeptWiseReportDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Department;
import hrms.model.master.Office;
import hrms.model.misreport.DeptWiseReportForm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
 * @author Madhusmita
 */
@Controller
@SessionAttributes("LoginUserBean")
public class DeptWiseReportController {

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public DeptWiseReportDAO deptReportDao;

    @RequestMapping(value = "DeptWiseNoofEmployees", method = RequestMethod.GET)
    public ModelAndView DeptWiseTypeOfEmployees(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams,
            @ModelAttribute("DeptWiseReportForm") DeptWiseReportForm deptReportForm) {
        ModelAndView mv = new ModelAndView("misreport/DeptWiseNoOfEmployeesReport", "DeptWiseReportForm", deptReportForm);

        List allDeptList = null;
        String dept_code = null;
        List noOfempDeptWise = null;
        //List totNoOfempDeptWise = null;

        try {

            Calendar c = Calendar.getInstance();
            int currentYear = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            String year = requestParams.get("year");
            if (year == null || year.equals("")) {
                year = currentYear + "";
            }
            List totNoOfempDeptWise = new ArrayList();
            
            //System.out.println("month:"+month);

            totNoOfempDeptWise = deptReportDao.getemployeeCountDeptWise(allDeptList, currentYear, month);

            mv.addObject("deptwiseEmplist", totNoOfempDeptWise);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }
}
