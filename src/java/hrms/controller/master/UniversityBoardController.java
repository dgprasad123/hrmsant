/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.UniversityDAO;
import hrms.model.master.University;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UniversityBoardController {

    @Autowired
    UniversityDAO universityDAO;

    @RequestMapping(value = "UniversityBoardList")
    public ModelAndView universityList(@ModelAttribute("university") University university) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("master/UniversityBoardList");
        List universityList = universityDAO.getUniversityList();
        mv.addObject("universityList", universityList);
        return mv;
    }

    @RequestMapping(value = "getUniversityBoardDetail")
    public ModelAndView getUniversityBoardDetail(ModelMap model, @ModelAttribute("university") University university, @RequestParam Map<String, String> requestParams) {
        ModelAndView mvc = new ModelAndView();
        String boardName = requestParams.get("boardName");
        university = universityDAO.editUniversityBoard(university);
        mvc = new ModelAndView("/master/AddNewUniversityBoard", "university", university);
        mvc.addObject("boardName", boardName);
        return mvc;
    }

    @RequestMapping(value = "saveBoard", method = {RequestMethod.GET, RequestMethod.POST}, params = {"action=Save Board"})
    public ModelAndView saveBoardDetail(ModelMap model, @ModelAttribute("university") University university) {
        ModelAndView mvc = new ModelAndView();
        university = universityDAO.editUniversityBoard(university);
        universityDAO.saveNewBoard(university);
        mvc.setViewName("master/UniversityBoardList");
        List universityList = universityDAO.getUniversityList();
        mvc.addObject("universityList", universityList);
        return mvc;
    }

    @RequestMapping(value = "updateBoard")
    public ModelAndView updateBoardDetail(ModelMap model, @ModelAttribute("university") University university) {
        ModelAndView mvc = new ModelAndView();
        universityDAO.updateNewBoard(university);
        university = universityDAO.editUniversityBoard(university);
        List universityList = universityDAO.getUniversityList();
        mvc.addObject("universityList", universityList);
        mvc.setViewName("master/UniversityBoardList");
        return mvc;
    }

    @RequestMapping(value = "updateBoardDetail")
    public ModelAndView updateBoardDetail(ModelMap model, @ModelAttribute("university") University university, @RequestParam Map<String, String> requestParams) {
        ModelAndView mvc = new ModelAndView();
        String boardName = requestParams.get("boardName");
        university = universityDAO.editUniversityBoard(university);
        mvc = new ModelAndView("/master/UpdateBoard", "university", university);
        universityDAO.updateNewBoard(university);
        mvc.addObject("boardName", boardName);
        return mvc;
    }

    @RequestMapping(value = "deleteBoardDetail")
    public ModelAndView deleteBoardDetail(ModelMap model, @ModelAttribute("university") University university) {
        ModelAndView mvc = new ModelAndView();
        universityDAO.deleteBoardDetail(university);
        List universityList = universityDAO.getUniversityList();
        mvc.addObject("universityList", universityList);
        mvc.setViewName("master/UniversityBoardList");
        return mvc;
    }

}
