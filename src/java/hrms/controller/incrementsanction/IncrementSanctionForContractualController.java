/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.incrementsanction;

import hrms.dao.incrementsanction.IncrementForSanctionContractualDAO;
import hrms.model.incrementsanction.ContractualEmployeeIncrementForm;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Surendra
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class IncrementSanctionForContractualController {

    @Autowired
    public IncrementForSanctionContractualDAO contIncrDAO;

    @RequestMapping(value = "incrementContractualList")
    public ModelAndView incrementContractualList(@ModelAttribute("SelectedEmpObj") Users selectedObj, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams, @ModelAttribute("jForm") ContractualEmployeeIncrementForm jForm) {

        List incrList = null;

        ModelAndView mav = null;
        try {
            jForm.setEmpId(selectedObj.getEmpId());
            jForm.setOffCode(lub.getLoginoffcode());
            incrList = contIncrDAO.getIncrementContractualList(jForm.getEmpId());

            mav = new ModelAndView("/IncrementSanction/IncrementListContractual", "jForm", jForm);
            mav.addObject("incrList", incrList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "editIncrementContractualData")
    public ModelAndView enterIncrementContractualData(@ModelAttribute("SelectedEmpObj") Users selectedObj, @RequestParam Map<String, String> requestParams, @ModelAttribute("jForm") ContractualEmployeeIncrementForm joinform) {

        ModelAndView mav = null;

        try {
            
                joinform = contIncrDAO.getIncrementContractualData(joinform.getIncrId(), selectedObj.getEmpId());
            
            joinform.setEmpId(selectedObj.getEmpId());
            mav = new ModelAndView("/IncrementSanction/AddIncrementContractual", "jForm", joinform);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveEmployeeIncrementContractual")
    public String saveEmployeeIncrement(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("jForm") ContractualEmployeeIncrementForm joinform) {

        try {
            if (joinform.getIncrId() > 0) {
                contIncrDAO.updateIncrementContractual(joinform, lub.getLoginempid());
            } else {
                contIncrDAO.saveIncrementContractual(joinform, lub.getLoginempid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/incrementContractualList.htm";
    }

    

}
