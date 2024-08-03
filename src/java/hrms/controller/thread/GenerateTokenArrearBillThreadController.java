package hrms.controller.thread;

import hrms.thread.paybill.CallableTokenArrearBill;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ModelAttribute;
import hrms.model.login.LoginUserBean;
import java.io.IOException;

@Controller
public class GenerateTokenArrearBillThreadController {
    
    @Autowired
    public CallableTokenArrearBill callableTokenArrearBill;
    
    @RequestMapping(value = "runGenerateTokenArrearBillThread", method = RequestMethod.GET)
    public ModelAndView runArrearBillThread(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException{        
        if(callableTokenArrearBill.getThreadStatus() == 0){
            Thread t = new Thread(callableTokenArrearBill);
            t.start();
        }
        ModelAndView mv = new ModelAndView("redirect:/getThreadServiceList.htm");                
        return mv;
    }
    
}
