/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.StateDAO;
import hrms.model.master.State;
import java.io.PrintWriter;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas Jena
 */
@Controller
public class StateMasterController {
    @Autowired
    StateDAO stateDAO;
    
    @ResponseBody
    @RequestMapping(value = "getStateListJSON", method = RequestMethod.GET)
    public void getstatelist(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try{
            List statelist = stateDAO.getStateList();
            json = new JSONArray(statelist);
            out = response.getWriter();
            out.write(json.toString());
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            out.flush();
            out.close();
        }       
    }
    
    @RequestMapping(value = "StateList")
    public ModelAndView stateList(@ModelAttribute("state") State  state) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("master/StateList"); 
        List statelist= stateDAO.getStateList();
        mv.addObject("statelist", statelist);
        return mv;
    }
    @RequestMapping(value = "getStateDetail")
    public ModelAndView getStateDetail(ModelMap model,@ModelAttribute("state") State  state){
        ModelAndView mv = new ModelAndView();
        state = stateDAO.editState(state);
        mv = new ModelAndView("/master/AddNewState","state", state);      
        return mv;       
    }
    
    @RequestMapping(value = "saveState",method = {RequestMethod.GET, RequestMethod.POST}, params = {"action=Save State"})
     public String saveNewState(ModelMap model,@ModelAttribute("state") State  state){        
        
        if (state.getStatecode() != null && !state.getStatecode().equals("")){
            stateDAO.updateNewState(state);
        }else{
            stateDAO.saveNewState(state);
        }    
        return "redirect:/StateList.htm";
    }
}
