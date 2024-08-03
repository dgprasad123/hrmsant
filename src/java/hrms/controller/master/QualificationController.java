/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.QualificationMasterDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.QualificationBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Surendra
 */
@Controller
public class QualificationController {

    @Autowired
    QualificationMasterDAO qualificationMasterDAO;

    @RequestMapping(value = "QualificationList", method = RequestMethod.GET)
    public ModelAndView qualificationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("qualificationBean") QualificationBean qualificationBean) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("master/QualificationList");
        List qualificationList = qualificationMasterDAO.getQualificationList();
        mv.addObject("qualificationList", qualificationList);
        return mv;
    }

    @RequestMapping(value = "QualificationList", method = {RequestMethod.POST}, params = {"action=Add New Qualification"})
    public ModelAndView createNewQualificationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("qualificationBean") QualificationBean qualificationBean) {
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("/master/AddNewQualification");
        return mvc;
    }

    @RequestMapping(value = "QualificationList", method = {RequestMethod.POST}, params = {"action=Save"})
    public ModelAndView SaveQualificationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("qualificationBean") QualificationBean qualificationBean) {
        ModelAndView mv = new ModelAndView();
        qualificationMasterDAO.saveUserDetails(qualificationBean);
        mv.setViewName("redirect:QualificationList.htm");
        return mv;

    }

    @RequestMapping(value = "QualificationList", method = {RequestMethod.POST}, params = {"action=Update"})
    public ModelAndView UpdateQualificationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("qualificationBean") QualificationBean qualificationBean) {
        ModelAndView mv = new ModelAndView();
        qualificationMasterDAO.updateUserDetails(qualificationBean);
        mv.setViewName("redirect:QualificationList.htm");
        return mv;

    }

    @RequestMapping(value = "getQualificationList", method = RequestMethod.GET)
    public ModelAndView getQualificationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("qualificationBean") QualificationBean qualificationBean) {
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("/master/AddNewQualification");
        return mvc;
    }

    /*  @RequestMapping(value = "saveQualificationDetail.htm", method = RequestMethod.POST)
     public ModelAndView saveStudentRegistrationDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("qualificationBean") QualificationBean qualificationBean) {
     ModelAndView mv = new ModelAndView();
     qualificationMasterDAO.saveUserDetails(qualificationBean);
     mv.setViewName("master/QualificationList");

     List qualificationList = qualificationMasterDAO.getQualificationList();
     mv.addObject("qualificationList", qualificationList);
     return mv;
     }*/
    @RequestMapping(value = "editQualification", method = {RequestMethod.GET})
    public ModelAndView editQualification(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("qualificationBean") QualificationBean qualificationBean) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/master/AddNewQualification");
        mav.addObject("qualificationBean", qualificationMasterDAO.editQualification(qualificationBean.getQualificationserialNumber()));
        return mav;
    }
    
     @RequestMapping(value = "deleteQualification.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView deleteQualification(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("qualificationBean") QualificationBean qualificationBean) {
        ModelAndView mav = new ModelAndView();
        qualificationMasterDAO.deleteQualification(qualificationBean.getQualificationserialNumber());

                mav.setViewName("redirect:QualificationList.htm");

        return mav;

    }

}
