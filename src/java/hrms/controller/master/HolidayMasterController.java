/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.calendar.CalendarDAO;
import hrms.dao.master.ModuleDAO;
import hrms.model.calendar.HolidayBean;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Module;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas Jena
 */
@Controller
@SessionAttributes("LoginUserBean")
public class HolidayMasterController {
    @Autowired
    CalendarDAO calendarDAO;
    
    @RequestMapping(value = "holidayListview", method = RequestMethod.GET)
    public String getholidayList(){
        String path = "/master/HolidayList";
        return path;
    }
    
    @ResponseBody
    @RequestMapping(value = "holidayListJSON", method = RequestMethod.GET)
    public void getholidayListJSON(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response,@RequestParam("year") Integer year) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List li = calendarDAO.getHolidayList(year);
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
    @ResponseBody
    @RequestMapping(value = "editholiday", method = RequestMethod.GET)
    public void editholiday(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response,@RequestParam("hid") Integer hid) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            HolidayBean hbean = calendarDAO.editHoliday(hid);
            JSONObject jobj = new JSONObject(hbean);            
            out = response.getWriter();
            out.write(jobj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
    @ResponseBody
    @RequestMapping(value = "removeHoliday", method = RequestMethod.GET)
    public void removeHoliday(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response,@RequestParam("hid") Integer hid) {
        
        PrintWriter out = null;
        try {
            int i = calendarDAO.removeHoliday(hid);                 
            out = response.getWriter();
            out.write(i);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
    @ResponseBody
    @RequestMapping(value = "saveHoliday", method = RequestMethod.GET)//holidayName + '&fromdate=' + +'&todate='+
    public void saveHoliday(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response,@RequestParam("holidayName") String holidayName,@RequestParam("holidayType") String holidayType,@RequestParam("fdate") String fdate,@RequestParam("tdate") String tdate) {        
        PrintWriter out = null;
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");                  
            HolidayBean hbean = new HolidayBean();
            hbean.setHolidayName(holidayName);
           
            hbean.setHolidayType(holidayType);
            hbean.setFdate((Date)formatter.parse(fdate));
            hbean.setTdate((Date)formatter.parse(tdate));
            int hid = calendarDAO.addHoliday(hbean);
            out = response.getWriter();
            out.write(hid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
}
