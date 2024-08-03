/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.GradeDAO;
import hrms.model.master.Grade;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author BIBHUTI
 */
@Controller
public class GradeMasterController {

    @Autowired
    GradeDAO gradeDao;
    @Autowired
    DepartmentDAO departmentDao;
    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @RequestMapping(value = "CadreWiseGrade")
    public ModelAndView CadreWiseGrade(@ModelAttribute("grade") Grade grade) {
        ModelAndView mv = new ModelAndView("/master/CadreWiseGrade", "grade", grade);
        //mv.setViewName("master/CadreWiseGrade");
        List cadrelist = gradeDao.getCadreList(grade.getDeptCode());
        mv.addObject("cadrelist", cadrelist);
        mv.addObject("departmentList", departmentDao.getDepartmentList());
        return mv;
    }

    @RequestMapping(value = "CadreWiseGrade.htm", params = {"action=search"})
    public ModelAndView CadreWiseGrade(@ModelAttribute("grade") Grade grade, @RequestParam("cadreCode") String cadreCode) {
        ModelAndView mv = new ModelAndView("/master/CadreWiseGrade", "grade", grade);
        List gradelist = gradeDao.getGradeList(grade.getCadreCode());
        mv.addObject("gradelist", gradelist);
        //mv.setViewName("/master/CadreWiseGrade");       
        mv.addObject("departmentList", departmentDao.getDepartmentList());
        List cadrelist = gradeDao.getCadreList(grade.getDeptCode());
        mv.addObject("cadrelist", cadrelist);
        return mv;
    }   
    
    @RequestMapping(value = "getGradeDetail")
    public ModelAndView getGradeDetail(ModelMap model, @ModelAttribute("grade") Grade grade) {
        ModelAndView mv = new ModelAndView();
        String deptCode = grade.getDeptCode();
        grade.setDeptCode(deptCode);
        String deptname = departmentDao.getDeptName(grade.getDeptCode());
        grade.setDeptName(deptname);

        String cadreCode = grade.getCadreCode();
        grade.setCadreCode(cadreCode);
        String cadreName = gradeDao.getCadreName(grade.getCadreCode());
        grade.setCadreName(cadreName);
        mv = new ModelAndView("/master/AddNewGrade", "grade", grade);
        return mv;
    }

    @RequestMapping(value = "saveGrade")
    public String saveGrade(ModelMap model, @ModelAttribute("grade") Grade grade) {

        if (grade.getCadreGradeCode() != 0) {
            gradeDao.updateGrade(grade);
        } else {
            gradeDao.saveGrade(grade);
        }
        return "redirect:/CadreWiseGrade.htm";
    }

    @RequestMapping(value = "updateGrade")
    public ModelAndView updateGrade(ModelMap model, @ModelAttribute("grade") Grade grade) {
        ModelAndView mv = new ModelAndView();
        grade = gradeDao.getGradeData(grade.getCadreGradeCode());
        String deptCode = grade.getDeptCode();
        grade.setDeptCode(deptCode);
        String deptname = departmentDao.getDeptName(grade.getDeptCode());
        grade.setDeptName(deptname);

        String cadreCode = grade.getCadreCode();
        grade.setCadreCode(cadreCode);
        String cadreName = gradeDao.getCadreName(grade.getCadreCode());
        grade.setCadreName(cadreName);
        mv.addObject("grade", grade);
        mv = new ModelAndView("/master/AddGrade", "grade", grade);
        return mv;
    }

    @RequestMapping(value = "updateNewGrade")
    public String updateNewGrade(ModelMap model, @ModelAttribute("grade") Grade grade) {
        int cadreGradeCode = grade.getCadreGradeCode();
        grade.setCadreGradeCode(cadreGradeCode);
        grade.getDeptCode();
        grade.getCadreCode();
        gradeDao.updateGrade(grade);
        String deptcod = null;
        deptcod = grade.getDeptCode();
        String cadrecod = null;
        cadrecod = grade.getCadreCode();
           //System.out.println("grade.getCadreGradeCode()123"+grade.getCadreGradeCode());

        return "redirect:/CadreWiseGrade.htm?action=search&deptCode="+deptcod+"&cadreCode="+cadrecod;
    }
    @ResponseBody
    @RequestMapping(value = "GradeDetail")
    public void getData(HttpServletResponse response,@RequestParam("grade") String grade) {
        try{
        response.setContentType("application/json");
        String status = gradeDao.getGradeName(grade);
        JSONObject obj = new JSONObject();
        obj.append("status", status);
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();
        }catch(Exception e)
        {}
    }
    
}
