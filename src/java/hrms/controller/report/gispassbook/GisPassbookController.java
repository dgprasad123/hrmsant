/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.report.gispassbook;

import hrms.common.CommonFunctions;
import hrms.dao.report.gispassbook.GisPassbookDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lenovo
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class GisPassbookController {

    @Autowired
    public GisPassbookDAO gispassbookDao;
    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @RequestMapping(value = "getGisPassBookList")
    public ModelAndView getGisPassBookList(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        String path = "/report/gispassbook/GisPassbookList";
        
        String empId = "";

        empId = lub.getLoginempid();

        ArrayList gispassbookDetails = gispassbookDao.getgisPassbookList(empId);
        ModelAndView mav = new ModelAndView();
        mav.addObject("gisList", gispassbookDetails);
        mav.addObject("EmpId", empId);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "getGisPassBookListForClickedEmployee")
    public ModelAndView getGisPassBookListForClickedEmployee(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, HttpServletResponse response) throws IOException {
        String path = "/report/gispassbook/GisPassbookList";

        String empId = "";

        empId = selectedEmpObj.getEmpId();

        ArrayList gispassbookDetails = gispassbookDao.getgisPassbookList(empId);
        List empDetails=gispassbookDao.getgisEmployeeDetaills(empId);
        ModelAndView mav = new ModelAndView();
        mav.addObject("gisList", gispassbookDetails);
        mav.addObject("empInfo", empDetails);
        mav.addObject("EmpId", empId);
        
        mav.setViewName(path);
        return mav;
    }
     @RequestMapping(value = "EmployeePostList", method = RequestMethod.GET)
    public ModelAndView EmployeePostList(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        String path = "/report/gispassbook/EmployeePostList";   
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int prevMonth=month-1; 
         int lastMonth=month-2;
         int searchYear;
         if(month==0 ){
             lastMonth=11;
             searchYear=year-1;
         } else {
             lastMonth=lastMonth;
             searchYear=year;
         }
         
       
        String monthstring=CommonFunctions.getMonthAsString(prevMonth);
        ArrayList employeePostDetails = gispassbookDao.EmployeePostList(lub.getLoginoffcode(),lastMonth,searchYear);        
        ModelAndView mav = new ModelAndView();
        mav.addObject("empdetails", employeePostDetails);
         mav.addObject("monthString", monthstring);
          mav.addObject("yearString", year);
        mav.setViewName(path);
        return mav;
    }

}
