/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.FacultyMasterDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.FacultyBean;
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
public class FacultyController {

    @Autowired
    FacultyMasterDAO facultyMasterDAO;

    /**
     *
     * @param lub
     * @param facultyBean
     * @return
     */
    @RequestMapping(value = "FacultyList", method = RequestMethod.GET)
    public ModelAndView facultyList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("facultyBean") FacultyBean facultyBean) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("master/FacultyList");
        List facultyList = facultyMasterDAO.getFacultyList();
        mv.addObject("facultyList", facultyList);
        return mv;
    }

    @RequestMapping(value = "FacultyList", method = {RequestMethod.POST}, params = {"action=Add New Faculty"})
    public ModelAndView createNewFacultyList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("facultyBean") FacultyBean facultyBean) {
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("/master/AddnewFaculty");
        return mvc;
    }

    @RequestMapping(value = "FacultyList", method = {RequestMethod.POST}, params = {"action=Save"})
    public ModelAndView SaveQualificationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("facultyBean") FacultyBean facultyBean) {
        ModelAndView mv = new ModelAndView();
        facultyMasterDAO.saveUserDetails(facultyBean);
        mv.setViewName("redirect:FacultyList.htm");
        return mv;

    }

    @RequestMapping(value = "editFacultyList", method = {RequestMethod.GET})
    public ModelAndView editQualification(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("facultyBean") FacultyBean facultyBean) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/master/AddnewFaculty");
        mav.addObject("facultyBean", facultyMasterDAO.editFaculty(facultyBean.getFacultyserialNumber()));
        return mav;
    }

    @RequestMapping(value = "FacultyList", method = {RequestMethod.POST}, params = {"action=Update"})
    public ModelAndView UpdateQualificationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("facultyBean") FacultyBean facultyBean) {
        ModelAndView mv = new ModelAndView();
        facultyMasterDAO.updateFacultyDetails(facultyBean);
        mv.setViewName("redirect:FacultyList.htm");
        return mv;

    }

    @RequestMapping(value = "deleteFaculty.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView deleteQualification(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("facultyBean") FacultyBean facultyBean) {
        ModelAndView mav = new ModelAndView();
        facultyMasterDAO.deleteFaculty(facultyBean.getFacultyserialNumber());

        mav.setViewName("redirect:FacultyList.htm");

        return mav;

    }
}
