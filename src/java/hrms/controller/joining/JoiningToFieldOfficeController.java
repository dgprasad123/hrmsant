/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.joining;

import hrms.dao.joining.JoiningDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.model.empinfo.EmployeeSearchResult;
import hrms.model.empinfo.SearchEmployee;
import hrms.model.joining.JoiningForm;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Madhusmita
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class JoiningToFieldOfficeController {

    @Autowired
    public JoiningDAO joiningDAO;

    @Autowired
    DistrictDAO districtDAO;

    @Autowired
    OfficeDAO officeDao;

    @RequestMapping(value = "joinfieldOffice")
    public ModelAndView enterJoiningFieldOfficeDetails(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("joiningForm") JoiningForm joiningForm, @RequestParam Map<String, String> requestParams) {
        ModelAndView mav = new ModelAndView();
        joiningForm.setHidempcode(lub.getEmpId());
        mav.addObject("loginempid", joiningForm.getHidempcode());
        String msg = requestParams.get("msg");

        if (lub.getDeptcode()!=null && lub.getDeptcode().equals("14")) {
            mav.addObject("districtList", districtDAO.getDistrictList());
            mav.addObject("msg", msg);
            mav.setViewName("/joining/JoiningToFieldOffice");
        } else {
            mav.setViewName("/under_const");
        }
        return mav;

    }

    @ResponseBody
    @RequestMapping(value = "getPoliceStationListJSON")
    public void getPoliceStationListJSON(HttpServletResponse response, @RequestParam("distcode") String distcode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List policeofficelist = officeDao.getPoliceStations(distcode);

            json = new JSONArray(policeofficelist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    
    @RequestMapping(value = "joinfieldOffice", params = "action=Save")
    public ModelAndView saveFieldOfficeDetails(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("joiningForm") JoiningForm joiningForm) {
        ModelAndView mav = new ModelAndView();
        String msg = null;
        String fieldoffcode = joiningForm.getSltFieldOffice();
        String empId = lub.getEmpId();
        if (fieldoffcode != null && !fieldoffcode.equals("")) {
            joiningDAO.saveFieldOfficeData(empId, fieldoffcode);
            msg = "Field Office Data Saved Successfully";
        }
        mav.setViewName("redirect:/joinfieldOffice.htm?msg=" + msg);
        return mav;

    }

    @ResponseBody
    @RequestMapping(value = "getFieldOfficeDetailsJSON")
    public void getFieldOfficeDetailsJSON(HttpServletResponse response, @RequestParam("empid") String empid) throws IOException, JSONException {
        response.setContentType("application/json");
        JSONObject obj = null;
        try {
            PrintWriter out = null;
            JoiningForm joiningForm = joiningDAO.getFieldOfficeData(empid);
            obj = new JSONObject(joiningForm);
            out = response.getWriter();
            out.write(obj.toString());
            out.close();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
