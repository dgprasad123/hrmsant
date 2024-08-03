/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.login.LoginDAO;
import hrms.dao.privilege.PrivilegeManagementDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.UserExpertise;
import hrms.model.master.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
@SessionAttributes("LoginUserBean")
public class UserPrivilegeMapController {

    @Autowired
    public LoginDAO loginDao;
    @Autowired
    public PrivilegeManagementDAO privilegeManagementDAO;

    @RequestMapping(value = "userprivilegemap")
    public ModelAndView userprivilegemapdetail(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("module") Module module) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/master/UserPrivilegemap");
        return mav;
    }

    @RequestMapping(value = "userprivilegemap", method = {RequestMethod.POST}, params = {"action=Search"})
    public ModelAndView searchuserprivilegemap(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("module") Module module) {
        ModelAndView mav = new ModelAndView();
        Module[] privileges = privilegeManagementDAO.getUserPrivileageList(module.getUsername());
        mav.addObject("Privileges", privileges);
        mav.setViewName("/master/UserPrivilegemap");
        return mav;
    }

    @RequestMapping(value = "userprivilegemap", method = {RequestMethod.POST}, params = {"action=Assign Privilege"})
    public ModelAndView getAdminPrivileageList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("module") Module module) {
        ModelAndView mav = new ModelAndView();
        Module[] privileges = privilegeManagementDAO.getPrivileageList("U");
        mav.addObject("Privileges", privileges);
        mav.setViewName("/master/UserPrivilegeList");
        return mav;
    }
    
    @RequestMapping(value = "userprivilegemap", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView backAdminPrivileageList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("module") Module module) {
        ModelAndView mav = new ModelAndView();
         Module[] privileges = privilegeManagementDAO.getUserPrivileageList(module.getUsername());
        mav.addObject("Privileges", privileges);
        mav.setViewName("/master/UserPrivilegemap");
        return mav;
    }
     @RequestMapping(value = "userprivilegemap", method = {RequestMethod.POST}, params = {"action=Assign"})
    public ModelAndView AssignAdminPrivileageList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("modname")String[] modname, @RequestParam("username")String username,@ModelAttribute("module") Module module) {
        ModelAndView mav = new ModelAndView();
        
        privilegeManagementDAO.assignPrivilegeUserNameSpecific(modname, username);
        mav.setViewName("/master/UserPrivilegemap");
        return mav;
    }



    @RequestMapping(value = "userprivilegemap", params = {"action=Revoke"})
    public ModelAndView Revokeuserprivilegemap(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("module") Module module) {
        //ModelAndView mav = new ModelAndView();
        ModelAndView mav = new ModelAndView("/master/UserPrivilegemap", "module", module);
        
        privilegeManagementDAO.revokeUserPrivilege(module.getPrivmapid());
        Module[] privileges = privilegeManagementDAO.getUserPrivileageList(module.getUsername());
        mav.addObject("Privileges", privileges);
        //mav.setViewName("/master/UserPrivilegemap");
        // mav.setViewName("redirect:userprivilegemap.htm");
        return mav;
    }

}
