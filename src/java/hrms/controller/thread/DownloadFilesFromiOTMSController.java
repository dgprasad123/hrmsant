package hrms.controller.thread;

import hrms.model.login.LoginUserBean;
import hrms.thread.paybill.CallableDownloadFiles;
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
public class DownloadFilesFromiOTMSController {
    
    @Autowired
    CallableDownloadFiles callableDownloadFiles;
    
    @RequestMapping(value = "runDownloadFilesFromiOTMSThread", method = RequestMethod.GET)
    public ModelAndView runDownloadFilesFromiOTMSThread(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException{        
        if(callableDownloadFiles.getThreadStatus() == 0){
            Thread t = new Thread(callableDownloadFiles);
            t.start();
        }
        ModelAndView mv = new ModelAndView("redirect:/getThreadServiceList.htm");                
        return mv;
    }
    
}
