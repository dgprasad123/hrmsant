/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.changepassword;

import hrms.common.CommonFunctions;
import hrms.dao.changepassword.ChangePasswordDAOImpl;
import hrms.dao.login.LoginDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Surendra
 */
@Controller
@SessionAttributes("LoginUserBean")
public class ChangePasswordController {

    //@Value("${msg}")
    @Autowired
    public ChangePasswordDAOImpl changepwdDao;

    @Autowired
    public LoginDAO loginDao;

    @RequestMapping(value = "ChangePasswordAction", method = RequestMethod.POST)
    public void showchangepassword(ModelMap model, @ModelAttribute("loginForm") Users loginForm, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result,HttpServletResponse request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        response.setContentType("application/json");
        out = response.getWriter();
        int pwdstatus = 0;
        Map responseResult = new HashMap();
        if (loginForm.getUserPassword() != null && !loginForm.getUserPassword().equals("")) {
            if (loginForm.getNewpassword().equalsIgnoreCase(loginForm.getConfirmpassword())) {

                pwdstatus = changepwdDao.modifyUserPassword(lub.getLoginempid(), lub.getLoginusertype(), loginForm.getUserPassword(), loginForm.getNewpassword(), lub.getLoginuserid());
                if (pwdstatus == 1) {
                    responseResult.put("msg", "Password changed successfully");
                   String logIP = CommonFunctions.getISPIPAddress();
                    //System.out.println("Change PWD ====" + logIP + "usrname==" + loginForm.getUserName() + "linkid==" + lub.getLoginempid() + "lub.getLoginoffcode==" + lub.getLoginoffcode() + lub.getLoginusertype());
                    loginDao.saveLoginLog(lub.getLoginempid(), lub.getLoginuserid(), logIP, "CHANGE PASSWORD", lub.getLoginoffcode(),null,lub.getLoginusertype(),null);

                } else if (pwdstatus == 0) {
                    responseResult.put("msg", "Invalid Password");
                } else if (pwdstatus == 2) {
                    responseResult.put("msg", "Password does not meet the policy");
                }
            } else {
                responseResult.put("msg", "New Password does not Match with Confirm Password");
            }
        }
        JSONObject jobj = new JSONObject(responseResult);
        out = response.getWriter();
        out.write(jobj.toString());
    }

    /*For Admin Login*/
    @RequestMapping(value = "showpasswordchange")
    public ModelAndView showpasswordchange(ModelMap model, @ModelAttribute("loginForm") Users loginForm, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("loginForm", loginForm);

        int pwdstatus = 0;
        Map responseResult = new HashMap();
        if (loginForm.getUserPassword() != null && !loginForm.getUserPassword().equals("")) {
            if (loginForm.getNewpassword().equalsIgnoreCase(loginForm.getConfirmpassword())) {

                pwdstatus = changepwdDao.modifyUserPassword(lub.getLoginempid(), lub.getLoginusertype(), loginForm.getUserPassword(), loginForm.getNewpassword(), lub.getLoginuserid());
                if (pwdstatus == 1) {
                    responseResult.put("msg", "Password changed successfully");
                } else if (pwdstatus == 0) {
                    responseResult.put("msg", "Invalid Password");
                } else if (pwdstatus == 2) {
                    responseResult.put("msg", "Password does not meet the policy");
                }
            } else {
                responseResult.put("msg", "New Password does not Match with Confirm Password");
            }
        }

        mav.addObject("msg", responseResult.get("msg"));
        mav.setViewName("tab/AdminChangePassword");
        return mav;
    }

}
