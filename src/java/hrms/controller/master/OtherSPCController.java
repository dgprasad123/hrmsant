/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OtherSPCDAO;
import hrms.model.master.OtherSPC;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lenovo
 */
@Controller
public class OtherSPCController {

    @Autowired
    OtherSPCDAO OtherSPC;
    
    @Autowired
    public DepartmentDAO deptDAO;
    
    @RequestMapping(value = "addOtherSPC")
    public ModelAndView addOtherSPC(@ModelAttribute("OtherSPC") OtherSPC ospc) {
       // ModelAndView mv = new ModelAndView();
       // mv.setViewName("/master/addOtherSPC");
         ModelAndView mv = new ModelAndView("/master/addOtherSPC","OtherSPC", ospc );
        return mv;
    }
    
    @RequestMapping(value = "getOtherSPCList")
    public ModelAndView getOtherSPCList(@ModelAttribute("OtherSPC") OtherSPC ospc) {
        ModelAndView mv = new ModelAndView("/master/addOtherSPC","OtherSPC", ospc );
        
        List deptlist = deptDAO.getDepartmentList();
        mv.addObject("deptlist", deptlist);
        
        mv.addObject("Office", ospc);     
        mv.addObject("spcList", OtherSPC.getOtherSPCList(ospc.getOffType()));
        
        return mv;
    }
    @RequestMapping(value = "saveOtherSPCList")
    public ModelAndView saveOtherSPCList(ModelMap model, HttpServletResponse response, @ModelAttribute("OtherSPC") OtherSPC ospc) {
       
        OtherSPC.saveOtherSPCList(ospc);		
        ModelAndView mv = new ModelAndView("redirect:/getOtherSPCList.htm?offType="+ospc.getHiddenoffType());
         return mv;
    }

}
