/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.DistrictDAO;
import hrms.dao.master.PoliceStationDAO;
import hrms.dao.master.PostOfficeDAO;
import hrms.dao.master.StateDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.PostOffice;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
 * @author Manas
 */
@Controller
@SessionAttributes({"LoginUserBean"})
public class PostOfficeMasterController {
    @Autowired
    public PostOfficeDAO postOfficeDAO;
    
    @Autowired
    DistrictDAO districtDAO;
    
    @Autowired
    StateDAO stateDAO;
    
    @ResponseBody
    @RequestMapping(value = "getDistrictWisePOList", method = RequestMethod.POST)
    public void getDistrictWisePOList(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam("distcode") String distcode) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList poStList = postOfficeDAO.getPostOfficeList(distcode);
        JSONArray json = new JSONArray(poStList);
        out = response.getWriter();
        out.write(json.toString());
    }
    
    @RequestMapping(value = "PostOfficeList")
    public ModelAndView postoffList(@ModelAttribute("postoff") PostOffice  postoff) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("master/PostOfficeList"); 
        
        List districtList= districtDAO.getDistrictList(postoff.getStateCode());
        mv.addObject("districtList", districtList);
        
        List postOffList= postOfficeDAO.getPostOfficeList(postoff.getDistCode());
        mv.addObject("postOffList", postOffList);
        
        mv.addObject("stateList", stateDAO.getStateList());
        return mv;
    }
    
    @RequestMapping(value = "getPostOffDetail")
    public ModelAndView getPostOffDetail(ModelMap model,@ModelAttribute("postoff") PostOffice  postoff){
        ModelAndView mv = new ModelAndView();
        postoff = postOfficeDAO.editPostOffice(postoff);    
        mv = new ModelAndView("/master/AddNewPostOfficeList","postoff", postoff);  
        postoff.setHidstateCode(postoff.getStateCode());
        postoff.setHiddistCode(postoff.getDistCode());
        mv.addObject("stateList", stateDAO.getStateList());
        mv.addObject("districtList", districtDAO.getDistrictList(postoff.getStateCode()));      
        return mv;       
    }

    @RequestMapping(value = "savePostOffice",method = {RequestMethod.GET, RequestMethod.POST}, params = {"action=Save PO"})
     public String savePostOffice(ModelMap model,@ModelAttribute("postoff") PostOffice  postoff){               
        if (postoff.getPostOfficeCode()!= null && !postoff.getPostOfficeCode().equals("")){
            postOfficeDAO.updatePostOffice(postoff);
        }else{
            postOfficeDAO.saveNewPostOffice(postoff);
        }    
        return "redirect:/PostOfficeList.htm";
    }
}
