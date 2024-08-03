/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.lic;

import hrms.dao.lic.LicDAO;
import hrms.model.lic.Lic;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas Jena
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class LicController {

    @Autowired
    LicDAO licDAO;

    @RequestMapping(value = "employeeLicAction", method = RequestMethod.GET)
    public ModelAndView employeeLicAction(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, BindingResult result, HttpServletResponse response) {//
        ModelAndView mav = new ModelAndView();
        String path = "/lic/EmployeeLIC";
        mav.addObject("EmpId", selectedEmpObj.getEmpId());
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "getEmployeeWiseLICList", method = RequestMethod.GET)
    public ModelAndView getEmployeeWiseLICList(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, HttpServletResponse response) throws IOException {
        String path = "/lic/empLicDetails";
        ArrayList licListDetails = licDAO.getLicList(selectedEmpObj.getEmpId());
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("licList", licListDetails);
        mav.addObject("EmpId", selectedEmpObj.getEmpId());
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "saveEmployeeLicData", method = RequestMethod.POST)
    public ModelAndView saveEmployeeLicData(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("Licform") Lic lic, BindingResult result, HttpServletResponse response) {//
        String path = "/lic/empLicDetails";
        licDAO.saveLicData(lic);
        ModelAndView mav = new ModelAndView();
        mav.addObject("EmpId", lic.getEmpid());
        mav.setViewName(path);
        return new ModelAndView("redirect:/getEmployeeWiseLICList.htm");
    }

    @RequestMapping(value = "editEmployeeLic", method = RequestMethod.GET)
    public ModelAndView editEmployeeLic(ModelMap model, @RequestParam("elId") BigDecimal elId, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, HttpServletResponse response) throws IOException {
        Lic lic = licDAO.editLicData(selectedEmpObj.getEmpId(), elId);
        ModelAndView mav = new ModelAndView();
        String path = "/lic/EditEmployeeLIC";
        mav.addObject("EmpId", selectedEmpObj.getEmpId());
        mav.addObject("licdata", lic);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "updateEmployeeLicData", method = RequestMethod.POST)
    public ModelAndView updateEmployeeLicData(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("Licform") Lic lic, BindingResult result, HttpServletResponse response) {//
        String path = "/lic/empLicDetails";
        licDAO.updateEmployeeLicData(lic);
        ModelAndView mav = new ModelAndView();
        mav.addObject("EmpId", lic.getEmpid());
        mav.setViewName(path);
        return new ModelAndView("redirect:/getEmployeeWiseLICList.htm");
    }

    @RequestMapping(value = "deleteLicData")
    public ModelAndView deleteLicData(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("LicId") String licId, @RequestParam("status") String status) {
        String ocode = lub.getLoginoffcode();
        licDAO.deleteLicData(licId, status);
        return new ModelAndView("redirect:/getEmployeeWiseLICList.htm");

    }

    /**
     * ******************************************* Added By
     * Rasmi*********************************
     */
}
