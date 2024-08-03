/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.thread;

import hrms.common.LogMessage;
import hrms.model.login.Users;
import hrms.model.payroll.QuaterRentBean;
import hrms.thread.equater.RentRecoveryThread;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas Jena
 */
@Controller
public class RentRecoveryThreadController {
    @Autowired
    public RentRecoveryThread rentRecoveryThread;
    
    @RequestMapping(value = "replicateRentData", method = RequestMethod.POST)
    public ModelAndView replicateRentData(ModelMap model, @ModelAttribute("quaterRentBeanForm") QuaterRentBean qrbean,BindingResult result, HttpServletResponse response){
        LogMessage.setMessage("XML Creation called");
        if(rentRecoveryThread.getThreadStatus() == 0){
            LogMessage.setMessage("Inside the thread");
            LogMessage.setMessage("bbbean.getSltMonth():"+qrbean.getSltMonth()+"bbbean.getSltYear()"+qrbean.getSltYear()+" District"+qrbean.getDistcode());
            rentRecoveryThread.setThreadValue(qrbean.getSltMonth(),qrbean.getSltYear(),qrbean.getDistcode());
            Thread t = new Thread(rentRecoveryThread);
            t.start();
        }        
        
        ModelAndView mav = new ModelAndView();        
        mav.setViewName("payroll/RentScheduleForeQuater");
        return mav;
                
    }
    
    @RequestMapping(value = "showReplicateRentData", method = RequestMethod.GET)
    public ModelAndView showReplicateRentData(ModelMap model, @ModelAttribute("loginForm") Users loginForm,BindingResult result, HttpServletResponse response){
        ModelAndView mav = new ModelAndView();
        
        mav.setViewName("payroll/RentScheduleForeQuater");
        return mav;
    }
}
