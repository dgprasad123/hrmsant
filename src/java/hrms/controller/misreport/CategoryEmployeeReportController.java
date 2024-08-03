/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.misreport;

import hrms.dao.misreport.CategoryEmployeeReportDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.misreport.OfficeWiseEmpStatusBean;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas
 */
@Controller
@SessionAttributes("LoginUserBean")
public class CategoryEmployeeReportController {

    @Autowired
    CategoryEmployeeReportDAO categoryEmployeeReportDao;

    @RequestMapping(value = "OfficeWiseCategoryEmployeeReport")
    public ModelAndView OfficeWiseCategoryEmployeeReport(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView("misreport/OfficeWiseCategoryEmployeeReportView");
        OfficeWiseEmpStatusBean owesb = categoryEmployeeReportDao.getOfficeWiseEmployeeCategoryData(lub.getLoginoffcode());
        mav.addObject("owesb", owesb);
        return mav;
    }

    @RequestMapping(value = "CategoryEmployeeReport")
    public ModelAndView CategoryEmployeeReport(@RequestParam("offCode") String offCode, @RequestParam("category") String category, @RequestParam("gender") String gender) {
        ModelAndView mav = new ModelAndView("misreport/EmpCategoryStatus");
        List empList = categoryEmployeeReportDao.getEmpCatStatusList(offCode, category, gender);
        mav.addObject("empList", empList);
        return mav;
    }
}
