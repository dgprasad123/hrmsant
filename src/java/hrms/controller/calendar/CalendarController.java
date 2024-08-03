/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.controller.calendar;

import hrms.dao.calendar.CalendarDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Durga
 */
@Controller
@SessionAttributes("LoginUserBean")
public class CalendarController {
    @Autowired
    public CalendarDAO calendarDAO;
    
    @ResponseBody
    @RequestMapping(value = "getHolidayList", method = RequestMethod.GET)
    public void isCalendarHoliday(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List li = calendarDAO.getHolidayList();
            JSONArray json = new JSONArray(li);
            out = response.getWriter();
            out.write(json.toString());        
            } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
    
    @RequestMapping(value = "myCalendar", method = RequestMethod.GET)
    public ModelAndView myCalendar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("calendar/EmployeeCalendar");        
        List calendarList = calendarDAO.getEmployeeCalendar("");
        mv.addObject("calendarList", calendarList);
        return mv;
    }
}
