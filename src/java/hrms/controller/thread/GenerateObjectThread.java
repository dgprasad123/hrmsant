package hrms.controller.thread;

import hrms.model.login.LoginUserBean;
import hrms.thread.paybill.CallableObjectionBill;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GenerateObjectThread {
    
    @Autowired
    public CallableObjectionBill callableObjectionBill;
    
    @RequestMapping(value = "runGenerateObjectionThread", method = RequestMethod.GET)
    public ModelAndView runGenerateObjectionThread(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException{        
        if(callableObjectionBill.getThreadStatus() == 0){
            Thread t = new Thread(callableObjectionBill);
            t.start();
        }
        ModelAndView mv = new ModelAndView("redirect:/getThreadServiceList.htm");                
        return mv;
    }
}
