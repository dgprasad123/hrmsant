/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.CadreDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.model.cadre.Cadre;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manisha
 */
@Controller
public class DeptAndCadreWisePostController {

    @Autowired
    CadreDAO cadreDAO;
    @Autowired
    DepartmentDAO departmentDao;

    @RequestMapping(value = "deptandcadreWisePostList")
    public ModelAndView deptandcadreWisePostList(@ModelAttribute("cadre") Cadre cadre) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("master/deptandCaderWisePostList");
        List cadrelist = cadreDAO.getCadreListfOrPAR(cadre.getDeptCode());
        List postlist = cadreDAO.getPostListCadreCodeAndDeptCodeWise(cadre.getDeptCode(), cadre.getCadreCode());
        mv.addObject("cadrelist", cadrelist);
        mv.addObject("postlist", postlist);
        mv.addObject("departmentList", departmentDao.getDepartmentList());
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "getCadreListDeptWiseJSON")
    public void getCadreListDeptWiseJSON(HttpServletResponse response, @ModelAttribute("cadre") Cadre cadre) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            List cadrelist = cadreDAO.getCadreListfOrPAR(cadre.getDeptCode());
            json = new JSONArray(cadrelist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

}
