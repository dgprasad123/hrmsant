/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.SubjectIdentityDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.SubjectIdentityBean;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller

public class SubjectIdentityController {

    @Autowired
    SubjectIdentityDAO subjectIdentityDAO ;
            
    @RequestMapping(value = "SubjectIdentityList", method = RequestMethod.GET)
    public ModelAndView SubjectIdentityList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("subjectIdentityBean") SubjectIdentityBean subjectIdentityBean) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("master/SubjectIdentityList");
        List sList = subjectIdentityDAO.getsubjectIdentityList();
        mv.addObject("sList", sList);
        return mv;
    }
    
     @RequestMapping(value = "SubjectIdentityList", method = {RequestMethod.POST}, params = {"action=Add New Subject"})
    public ModelAndView createNewSubjectList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("subjectIdentityBean") SubjectIdentityBean subjectIdentityBean) {
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("/master/AddSubjectIdentity");
        return mvc;
    }
    
     @RequestMapping(value = "SubjectIdentityList", method = {RequestMethod.POST}, params = {"action=Save"})
    public ModelAndView SaveSubjectList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("subjectIdentityBean") SubjectIdentityBean subjectIdentityBean) {
        ModelAndView mv = new ModelAndView();
        subjectIdentityDAO.saveSubjectDetails(subjectIdentityBean);
        mv.setViewName("redirect:SubjectIdentityList.htm");
        return mv;

    }
    
      @RequestMapping(value = "editSubjectList", method = {RequestMethod.GET})
    public ModelAndView editSubjectList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("subjectIdentityBean") SubjectIdentityBean subjectIdentityBean) {
        ModelAndView mav = new ModelAndView();
        subjectIdentityBean = subjectIdentityDAO.editSubject(subjectIdentityBean.getSubjectsl());
        mav.setViewName("/master/AddSubjectIdentity");
        mav.addObject("subjectIdentityBean", subjectIdentityBean);
        return mav;
    }
    
     @RequestMapping(value = "SubjectIdentityList", method = {RequestMethod.POST}, params = {"action=Update"})
    public ModelAndView UpdateSubjectList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("subjectIdentityBean") SubjectIdentityBean subjectIdentityBean) {
        ModelAndView mv = new ModelAndView();
        subjectIdentityDAO.updateSubjectDetails(subjectIdentityBean);
        mv.setViewName("redirect:SubjectIdentityList.htm");
        return mv;

    }
    
    @RequestMapping(value = "deleteSubject.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView deleteQualification(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("subjectIdentityBean")  SubjectIdentityBean subjectIdentityBean) {
        ModelAndView mav = new ModelAndView();
        subjectIdentityDAO.deleteDegree(subjectIdentityBean.getSubjectsl());

        mav.setViewName("redirect:SubjectIdentityList.htm");

        return mav;

    }
}
