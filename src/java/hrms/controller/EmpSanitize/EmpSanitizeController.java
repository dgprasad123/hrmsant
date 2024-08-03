/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.EmpSanitize;

import hrms.dao.EmpSantize.EmpSanitizeDAO;
import hrms.model.EmpSanitize.EmpSanitize;
import hrms.model.login.LoginUserBean;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Hp
 */
@Controller
@SessionAttributes("LoginUserBean")
public class EmpSanitizeController {

    @Autowired
    public EmpSanitizeDAO empSanitizeDAO;

    @RequestMapping(value = "EmpSanitizeList")
    public ModelAndView EmpSanitizeList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("santizeModel") EmpSanitize santizeModel) {
        List empList = empSanitizeDAO.getEmployee1List(lub.getLoginoffcode());
        List empTypeList = empSanitizeDAO.getEmployeeType();
        ModelAndView mav = new ModelAndView("/EmpSanitize/EmpSanitizeList");
        mav.addObject("emplist", empList);
        mav.addObject("empTypeList", empTypeList);
        return mav;
    }
    
    @ResponseBody
    @RequestMapping(value = "saveEmployeeSanitize")
    public void saveEmployeeSanitize(@RequestParam("empid") String empid,@RequestParam("employeetype") String employeetype) {
        empSanitizeDAO.saveEmployeeSanitize(empid,employeetype);
    }
 @ResponseBody
    @RequestMapping(value = "changeEmployeeStatus")
    public void changeEmployeeStatus(@RequestParam("empid") String empid,@RequestParam("status") String status) {
        empSanitizeDAO.changeEmployeeStatus(empid,status);
    }
}
