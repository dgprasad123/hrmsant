/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.controller.master;

import hrms.dao.master.DdoDetailsDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.DdoDetailsBean;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Surendra
 */
@SessionAttributes({"LoginUserBean"})

@Controller
public class DdoDetailsController {
    @Autowired
    DdoDetailsDAO ddoDetailsDAO;
    
    
     @RequestMapping(value = "DdoList", method = RequestMethod.GET)
    public ModelAndView DdoList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("DdoDetailsBean") DdoDetailsBean ddoDetailsBean) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("master/DdoDetailsList");
        List ddoList = ddoDetailsDAO.getDdoList(lub.getLogindistrictcode());
        mv.addObject("ddoList", ddoList);
        return mv;
    }
    
}
