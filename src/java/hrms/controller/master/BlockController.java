/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.BlockDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.StateDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Block;
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
public class BlockController {

    @Autowired
    BlockDAO blockDAO;
    
    @Autowired
    DistrictDAO districtDAO;
    
    @Autowired
    StateDAO stateDAO;

    @ResponseBody
    @RequestMapping(value = "getDistrictWiseBlockList", method = RequestMethod.POST)
    public void getDistrictWiseBlockList(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam("distcode") String distcode) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList blockList = blockDAO.getBlockList(distcode);
        JSONArray json = new JSONArray(blockList);
        out = response.getWriter();
        out.write(json.toString());
    }
    @RequestMapping(value = "BlockList")
    public ModelAndView districtList(@ModelAttribute("block") Block  block) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("master/BlockList"); 
        
        List districtList= districtDAO.getDistrictList(block.getStateCode());
        mv.addObject("districtList", districtList);
        
        List blockList= blockDAO.getBlockList(block.getDistCode());
        mv.addObject("blockList", blockList);
        
        mv.addObject("stateList", stateDAO.getStateList());
        return mv;
    }
    
    @RequestMapping(value = "getBlockDetail")
    public ModelAndView getBlockDetail(ModelMap model,@ModelAttribute("block") Block  block){
        ModelAndView mv = new ModelAndView();
        block = blockDAO.editBlock(block);
        mv = new ModelAndView("/master/AddNewBlock","block", block); 
        block.setHidstateCode(block.getStateCode());
        block.setHiddistCode(block.getDistCode());
        mv.addObject("stateList", stateDAO.getStateList());
        mv.addObject("districtList", districtDAO.getDistrictList(block.getStateCode()));
        mv.addObject("blockList", blockDAO.getBlockList(block.getDistCode()));
        return mv;       
    }

    @RequestMapping(value = "saveBlock",method = {RequestMethod.GET, RequestMethod.POST}, params = {"action=Save Block"})
     public String saveNewBlock(ModelMap model,@ModelAttribute("block") Block  block){        
        //System.out.println("controller c code:"+ cadre.getCadreCode()); 
        if (block.getBlockCode()!= null && !block.getBlockCode().equals("")){
            blockDAO.updateNewBlock(block);
        }else{
            blockDAO.saveNewBlock(block);
        }    
        return "redirect:/BlockList.htm";
    }
}
