package hrms.controller.unlockuser;

import hrms.dao.privilege.PrivilegeManagementDAO;
import hrms.dao.unlockuser.UnlockuserDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UnlockUserController {

    @Autowired
    UnlockuserDAO unlockuserDAO;
    @Autowired
    PrivilegeManagementDAO privilegeManagementDAO;

    @RequestMapping(value = "unlockuserpage")
    public String unlockuserpage() {

        return "/unlockuser/UnlockUser";

    }

    @RequestMapping(value = "unlockuser")
    public String unlockuser(Model model, @RequestParam("sltEmpidGpf") String sltEmpidGpf, @RequestParam("txtEmpidGpf") String txtEmpidGpf) {

        int status = 0;
        try {
            status = unlockuserDAO.unlockuser(sltEmpidGpf, txtEmpidGpf);
            model.addAttribute("status", status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/unlockuser/UnlockUser";
    }

    @RequestMapping(value = "userlistview")
    public ModelAndView userprivilegemapdetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("users") Users users) {
        ModelAndView mav = new ModelAndView();
        List usertypes = privilegeManagementDAO.getUserType();

        mav.addObject("usertypes", usertypes);
        mav.setViewName("/unlockuser/userDetails");
        return mav;
    }

    @RequestMapping(value = "userlistview", method = {RequestMethod.POST}, params = {"action=Search"})
    public ModelAndView searchempprivilegemap(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("users") Users users) {
        ModelAndView mav = new ModelAndView();
        List usertypes = privilegeManagementDAO.getUserType();
        List userList = privilegeManagementDAO.getUserList(users.getUsertype());

        mav.addObject("usertypes", usertypes);
        mav.addObject("userList", userList);
        mav.setViewName("/unlockuser/userDetails");
        return mav;
    }

    @RequestMapping(value = "userlistview", method = {RequestMethod.POST}, params = {"action=Add New"})
    public ModelAndView Newempprivilegemap(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("users") Users users) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/unlockuser/userList");
        return mav;
    }

    @RequestMapping(value = "empprivilegemap", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView Backempprivilegemap(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("users") Users users) {
        ModelAndView mav = new ModelAndView();
        List usertypes = privilegeManagementDAO.getUserType();

        mav.addObject("usertypes", usertypes);
        mav.setViewName("/unlockuser/userDetails");
        return mav;
    }

    @RequestMapping(value = "empprivilegemap", method = {RequestMethod.POST}, params = {"action=Save"})
    public ModelAndView Saveempprivilegemap(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("users") Users users) {
        ModelAndView mav = new ModelAndView();
        privilegeManagementDAO.saveUserDetail(users);
        mav.addObject("msg", "user name is already exists");
        mav.setViewName("/unlockuser/userDetails");
        return mav;
    }
}
