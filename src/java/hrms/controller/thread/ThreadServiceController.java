/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.controller.thread;

import hrms.dao.thread.ThreadServiceDAO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas
 */
@Controller
public class ThreadServiceController {
    @Autowired
    ThreadServiceDAO threadServiceDAO;
    
    @RequestMapping(value = "getThreadServiceList")
    public ModelAndView getThreadServiceList(){
        ModelAndView mv = new ModelAndView();
        List serviceThreadList = threadServiceDAO.getServiceThreadList();
        mv.addObject("serviceThreadList", serviceThreadList);
        mv.setViewName("master/ServiceList");
        return mv;
    }
}
