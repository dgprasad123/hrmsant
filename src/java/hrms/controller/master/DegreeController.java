/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.DegreeeDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Degree;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Surendra
 */
@Controller
public class DegreeController {

    @Autowired
    DegreeeDAO degreeeDAO;

    @RequestMapping(value = "DegreeList", method = RequestMethod.GET)
    public ModelAndView facultyList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("degree") Degree degree) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("master/DegreeList");
        List degreeList = degreeeDAO.getDegreeList();
        mv.addObject("degreeList", degreeList);
        return mv;
    }

    @RequestMapping(value = "DegreeList", method = {RequestMethod.POST}, params = {"action=Add New Degree"})
    public ModelAndView createNewFacultyList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("degree") Degree degree) {
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("/master/AddnewDegree");
        return mvc;
    }

    @RequestMapping(value = "DegreeList", method = {RequestMethod.POST}, params = {"action=Save"})
    public ModelAndView SaveQualificationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("degree") Degree degree) {
        ModelAndView mv = new ModelAndView();
        degreeeDAO.saveUserDetails(degree);
        mv.setViewName("redirect:DegreeList.htm");
        return mv;

    }

    @RequestMapping(value = "editDegreeList", method = {RequestMethod.GET})
    public ModelAndView editQualification(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("degree") Degree degree) {
        ModelAndView mav = new ModelAndView();
        degree = degreeeDAO.editDegree(degree.getDegreesl());
        mav.setViewName("/master/AddnewDegree");
        mav.addObject("degree", degree);
        return mav;
    }

    @RequestMapping(value = "DegreeList", method = {RequestMethod.POST}, params = {"action=Update"})
    public ModelAndView UpdateQualificationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("degree") Degree degree) {
        ModelAndView mv = new ModelAndView();
        degreeeDAO.updateDegreeDetails(degree);
        mv.setViewName("redirect:DegreeList.htm");
        return mv;

    }

    @RequestMapping(value = "deleteDegree.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView deleteQualification(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("degree") Degree degree) {
        ModelAndView mav = new ModelAndView();
        degreeeDAO.deleteDegree(degree.getDegreesl());

        mav.setViewName("redirect:DegreeList.htm");

        return mav;

    }

}
