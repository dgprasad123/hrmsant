package hrms.controller.joining;

import hrms.dao.joining.JoiningContractualDAO;
import hrms.model.joining.JoiningContractualForm;
import hrms.model.joining.JoiningForm;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class JoiningContractualController {

    @Autowired
    public JoiningContractualDAO joiningDAO;

    @RequestMapping(value = "JoiningContractualList")
    public ModelAndView JoiningContractualListPage(@ModelAttribute("SelectedEmpObj") Users selectedObj,@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("jForm") JoiningContractualForm jForm) {

        List joininglist = null;

        ModelAndView mav = null;
        try {
            jForm.setHidempid(selectedObj.getEmpId());
            joininglist = joiningDAO.getJoiningContractualList(jForm.getHidempid(), lub.getLoginoffcode());

            mav = new ModelAndView("/joining/JoiningListContractual", "jForm", jForm);
            mav.addObject("joininglist", joininglist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "enterJoiningContractualData")
    public ModelAndView enterJoiningContractualData(@ModelAttribute("SelectedEmpObj") Users selectedObj, @RequestParam("primaryTrId") String primaryTrId, @RequestParam("jId") String jId) throws IOException {

        ModelAndView mav = null;

        JoiningContractualForm joinform = new JoiningContractualForm();
        try {

            joinform = joiningDAO.getJoiningContractualData(primaryTrId, jId);
            joinform.setHidempid(selectedObj.getEmpId());
            mav = new ModelAndView("/joining/AddJoiningContractual", "jForm", joinform);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveEmployeeJoiningContractual", params = "action=Join")
    public String saveEmployeeJoining(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("jForm") JoiningContractualForm joinform) {

        try {
            joiningDAO.saveJoiningContractual(joinform, lub.getLoginempid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/JoiningContractualList.htm";
    }
    
    @RequestMapping(value = "saveEmployeeJoiningContractual", params = "action=Update")
    public String updateEmployeeJoining(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("jForm") JoiningContractualForm joinform) {

        try {
            joiningDAO.updateJoiningContractual(joinform);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/JoiningContractualList.htm";
    }
    
    @RequestMapping(value = "saveEmployeeJoiningContractual", params = "action=Back")
    public String backJoiningListPage(@ModelAttribute("jForm") JoiningForm joinform) {
        return "redirect:/JoiningContractualList.htm";
    }
}
