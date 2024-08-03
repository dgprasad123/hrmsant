/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.BlockDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.PoliceStationDAO;
import hrms.dao.master.StateDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.PoliceStation;
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
public class PoliceStationMasterController {

    @Autowired
    public PoliceStationDAO policeStationDAO;
    
    @Autowired
    BlockDAO blockDAO;
    
    @Autowired
    DistrictDAO districtDAO;
    
    @Autowired
    StateDAO stateDAO;
    
    @ResponseBody
    @RequestMapping(value = "getDistrictWisePSList")
    public void getDistrictWisePSList( HttpServletResponse response, @RequestParam("distcode") String distcode) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList poStList = policeStationDAO.getPoliceStationList(distcode);
        JSONArray json = new JSONArray(poStList);
        out = response.getWriter();
        out.write(json.toString());
    }
    
    @RequestMapping(value = "PoliceStationList")
    public ModelAndView districtList(@ModelAttribute("pstation") PoliceStation  pstation) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("master/PoliceStationList"); 
        
        List districtList= districtDAO.getDistrictList(pstation.getStateCode());
        mv.addObject("districtList", districtList);
        
        List psList=policeStationDAO.getPoliceStationList(pstation.getDistCode());
        mv.addObject("psList", psList);
        
        mv.addObject("stateList", stateDAO.getStateList());
        return mv;
    }
    
    @RequestMapping(value = "getPsDetail")
    public ModelAndView getPsDetail(ModelMap model,@ModelAttribute("pstation") PoliceStation  pstation){
        ModelAndView mv = new ModelAndView();
        pstation = policeStationDAO.editPoliceStation(pstation);    
        mv = new ModelAndView("/master/AddNewPoliceStationList","pstation", pstation);  
        pstation.setHidstateCode(pstation.getStateCode());
        pstation.setHiddistCode(pstation.getDistCode());
        mv.addObject("stateList", stateDAO.getStateList());
        mv.addObject("districtList", districtDAO.getDistrictList(pstation.getStateCode()));      
        return mv;       
    }

    @RequestMapping(value = "savePoliceStation",method = {RequestMethod.GET, RequestMethod.POST}, params = {"action=Save PS"})
     public String savePoliceStation(ModelMap model,@ModelAttribute("pstation") PoliceStation  pstation){               
        if (pstation.getPsCode()!= null && !pstation.getPsCode().equals("")){
            policeStationDAO.updateNewPoliceStation(pstation);
        }else{
            policeStationDAO.saveNewPoliceStation(pstation);
        }    
        return "redirect:/PoliceStationList.htm";
    }
}
