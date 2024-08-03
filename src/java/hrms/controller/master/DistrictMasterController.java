/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.DistrictDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.master.StateDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.District;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas Jena
 */
@Controller
@SessionAttributes("LoginUserBean")
public class DistrictMasterController {

    @Autowired
    DistrictDAO districtDAO;
    
    @Autowired
    StateDAO stateDAO;

    @ResponseBody
    @RequestMapping(value = "getDistrictList", method = RequestMethod.POST)
    public void getDistrictList(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList districtList = districtDAO.getDistrictList();
        JSONArray json = new JSONArray(districtList);
        out = response.getWriter();
        out.write(json.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getStateWiseDistrictList")
    public void getStateWiseDistrictList(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam("stateCode") String stateCode) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList districtList = districtDAO.getDistrictList(stateCode);
        JSONArray json = new JSONArray(districtList);
        out = response.getWriter();
        out.write(json.toString());
    }
    
    @RequestMapping(value = "DistrictList")
    public ModelAndView districtList(@ModelAttribute("district") District  district) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("master/DistrictList"); 
        List districtList= districtDAO.getDistrictList(district.getStateCode());
        mv.addObject("districtList", districtList);
        
        mv.addObject("stateList", stateDAO.getStateList());
        return mv;
    }
    @RequestMapping(value = "getDistrictDetail")
    public ModelAndView getDistrictDetail(ModelMap model,@ModelAttribute("district") District  district){
        ModelAndView mv = new ModelAndView();
        district = districtDAO.editDistrict(district);
        mv = new ModelAndView("/master/AddNewDistrict","district", district);    
        district.setHidstateCode(district.getStateCode());       
        mv.addObject("stateList", stateDAO.getStateList());
        return mv;       
    }

    @RequestMapping(value = "saveDistrict",method = {RequestMethod.GET, RequestMethod.POST}, params = {"action=Save District"})
     public String saveNewDistrict(ModelMap model,@ModelAttribute("district") District  district){        
        //System.out.println("controller c code:"+ cadre.getCadreCode()); 
        if (district.getDistCode()!= null && !district.getDistCode().equals("")){
            districtDAO.updateNewDistrict(district);
        }else{
            districtDAO.saveNewDistrict(district);
        }    
        return "redirect:/DistrictList.htm";
    }
}
