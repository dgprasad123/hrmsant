/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.redeployment;

import hrms.dao.reemployment.RedesignationContractualDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.reemployment.RedesignationContractualBean;
import java.util.List;
import java.util.Map;
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
 * @author Surendra
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class RedesignationcontractualController {

    @Autowired
    public RedesignationContractualDAO redesignationContractualDAO;

    @RequestMapping(value = "AddnewdesignationContractual")
    public ModelAndView AddnewdesignationContractual(ModelMap model, @ModelAttribute(value = "SelectedEmpObj") Users selectedEmpObj,
            @ModelAttribute("redesgContractualForm") RedesignationContractualBean redesgContform, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = null;
        try {
            redesgContform.setEmpid(selectedEmpObj.getEmpId());
            redesgContform = redesignationContractualDAO.getTransferContractualList(selectedEmpObj.getEmpId());
            mav = new ModelAndView("reemployment/RedesignationContractual", "redesgContform", redesgContform);
            String msg = requestParams.get("msg");
            mav.addObject("msg", msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveEmployeeCurrentpostContractual")
    public String saveEmployeeCpost(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("redesgContractualForm") RedesignationContractualBean redesgContform) {

        try {
            redesignationContractualDAO.saveCurrentpostContractual(redesgContform, lub.getLoginempid(), lub.getLoginusername());
                                //redesignationContractualDAO.updateCurrentpostContractual(redesgContform, lub.getLoginempid());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/AddnewdesignationContractual.htm?msg=Update Success";
    }

}
