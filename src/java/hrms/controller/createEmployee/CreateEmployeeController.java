/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.createEmployee;

import hrms.dao.createEmployee.CreateEmployeeDAO;
import hrms.model.createEmployee.CreateEmployee;
import hrms.model.login.LoginUserBean;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lenovo
 */
@Controller
@SessionAttributes("LoginUserBean")

public class CreateEmployeeController {

    @Autowired
    CreateEmployeeDAO createEmployeeDao;

    @RequestMapping(value = "CreateNewEmployeeAuthenticateAadhaar")
    public String CreateNewEmployeeAuthenticateAadhaar(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam Map<String, String> requestParams) {//

        String path = "/createEmployee/CreateNewEmployeeAuthenticateAadhaar";

        return path;
    }

    @RequestMapping(value = "createNewEmployee")
    public ModelAndView createNewEmployee(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam Map<String, String> requestParams) {//
        ModelAndView mav = new ModelAndView();
        String path = "/createEmployee/CreateEmployee";
        String offcode = "";
        if (requestParams.get("offCode") != null && !requestParams.get("offCode").equals("")) {
            offcode = requestParams.get("offCode");
        } else {
            offcode = lub.getLoginoffcode();
        }
        if ((lub.getLoginOffLevel() != null && lub.getLoginOffLevel().equals("20")) && (lub.getLoginIsDDOType() != null && lub.getLoginIsDDOType().equals("B"))) {
            mav.addObject("ddoCatg", lub.getLoginIsDDOType());
        } else {
            mav.addObject("ddoCatg", lub.getLoginIsDDOType());
        }
        String isduplicate = requestParams.get("isduplicate");
        String username = requestParams.get("username");
        String password = requestParams.get("password");
        String aadhaarno = requestParams.get("aadhaarno");
        mav.addObject("username", username);
        mav.addObject("isduplicate", isduplicate);
        mav.addObject("password", password);
        mav.addObject("aadhaarno", aadhaarno);

        ArrayList gpfList = createEmployeeDao.getGpfType();
        mav.addObject("gpfList", gpfList);
        mav.addObject("offcode", offcode);
        ArrayList empTitleList = createEmployeeDao.empTitleType();
        mav.addObject("empTitleList", empTitleList);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "saveEmployeeData")
    public ModelAndView saveEmployeeData(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("CreateEmployeeform") CreateEmployee cebean, BindingResult result, HttpServletResponse response) {//

        String isDuplicateGPF = createEmployeeDao.saveEmployeeData(cebean);
        String isduplicate = "";
        String username = "";
        String password = "";

        if (isDuplicateGPF.equals("Y")) {
            isduplicate = "Y";

        } else if (isDuplicateGPF.equals("A")) {
            isduplicate = "A";

        } else if (isDuplicateGPF.equals("M")) {
            isduplicate = "M";
        } else {
            StringTokenizer stringTokenizer = new StringTokenizer(isDuplicateGPF, "|");
            while (stringTokenizer.hasMoreTokens()) {
                isduplicate = stringTokenizer.nextToken().trim();
                username = stringTokenizer.nextToken().trim();
                password = stringTokenizer.nextToken().trim();

            }
        }
        ModelAndView mav = new ModelAndView();

        //mav.setViewName(path);
        return new ModelAndView(
                "redirect:/createNewEmployee.htm?isduplicate=" + isduplicate + "&username=&password=");
    }

    @ResponseBody
    @RequestMapping(value = "generateNewAcctNo")
    public void generateNewAcctNo(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("acctType") String acctType) {
        PrintWriter out = null;
        try {
            response.setContentType("application/json");
            String status = createEmployeeDao.generateAccountNo(acctType);
            out = response.getWriter();
            JSONObject obj = new JSONObject();
            obj.append("status", status);
            out.write(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

}
