/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.controller.thread;

import hrms.model.login.LoginUserBean;
import hrms.thread.paybill.CallableArrearPayBill;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas
 */
@Controller
public class ArrearBillThreadController {
    @Autowired
    public CallableArrearPayBill callableArrearPayBill;
    
    @RequestMapping(value = "runArrearBillThread", method = RequestMethod.GET)
    public ModelAndView runArrearBillThread(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response)throws IOException{        
        if(callableArrearPayBill.getThreadStatus() == 0){
            Thread t = new Thread(callableArrearPayBill);
            t.start();
        }
        ModelAndView mv = new ModelAndView("redirect:/getThreadServiceList.htm");                
        return mv;
    }
}
