/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.DistrictDAO;
import hrms.dao.master.PoliceStationDAO;
import hrms.dao.master.StateDAO;
import hrms.dao.master.VillageDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Village;
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
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas
 */
@Controller
public class VillageMasterController {

    @Autowired
    public VillageDAO villageDAO;
    
    @Autowired
    DistrictDAO districtDAO;
    
    @Autowired
    StateDAO stateDAO;
    
     @Autowired
    public PoliceStationDAO policeStationDAO;

    @ResponseBody
    @RequestMapping(value = "getDistrictWisePsWiseVillageList", method = RequestMethod.POST)
    public void getDistrictWisePsWiseVillageList(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam("distcode") String distcode, @RequestParam("pscode") String pscode) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList villageList = villageDAO.getVillageList(distcode, pscode);
        JSONArray json = new JSONArray(villageList);
        out = response.getWriter();
        out.write(json.toString());
    }
    
    @RequestMapping(value = "VillageList")
    public ModelAndView postoffList(@ModelAttribute("village") Village  village) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("master/VillageList"); 
        
        List districtList= districtDAO.getDistrictList(village.getStateCode());
        mv.addObject("districtList", districtList);
        
        List poStList=policeStationDAO.getPoliceStationList(village.getDistCode());
        mv.addObject("poStList", poStList);
        
        List villageList= villageDAO.getVillageList(village.getDistCode(),village.getPsCode());
        mv.addObject("villageList", villageList);
        
        mv.addObject("stateList", stateDAO.getStateList());
        return mv;
    }
    
    @RequestMapping(value = "getVillageDetail")
    public ModelAndView getVillageDetail(ModelMap model,@ModelAttribute("village") Village  village){
        ModelAndView mv = new ModelAndView();
        village = villageDAO.editVillage(village);    
        mv = new ModelAndView("/master/AddNewVillageList","village", village);   
        village.setHidstateCode(village.getStateCode());
        village.setHiddistCode(village.getDistCode());
        village.setHidpsCode(village.getPsCode());
 
        mv.addObject("stateList", stateDAO.getStateList());
        mv.addObject("districtList", districtDAO.getDistrictList(village.getStateCode()));   
        mv.addObject("poStList", policeStationDAO.getPoliceStationList(village.getDistCode())); 
        return mv;       
    }

    @RequestMapping(value = "saveVillage",method = {RequestMethod.GET, RequestMethod.POST}, params = {"action=Save Village"})
     public String saveVillage(ModelMap model,@ModelAttribute("village") Village  village){               
        if (village.getVillageCode()!= null && !village.getVillageCode().equals("")){
            villageDAO.updateVillage(village);
        }else{
            villageDAO.saveNewVillage(village);
        }    
        return "redirect:/VillageList.htm";
    }
}
