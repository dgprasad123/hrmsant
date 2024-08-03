package hrms.controller.GPHeadUpdate;

import hrms.dao.GPHeadUpdate.GPHeadDAO;
import hrms.model.GPHeadUpdate.GPHeadUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GPHeadUpdateController {
    
    @Autowired
    public GPHeadDAO gpHeadDAO;
    
    @RequestMapping(value = "GPHead")
    public String GPHeadPage(@ModelAttribute("gpheadform") GPHeadUpdateForm gpheadform){
        
        return "/GPHeadUpdate/GPHeadUpdate";
    }
    
    @RequestMapping(value = "updateGPHead",params = "get")
    public ModelAndView getGPHeadDetails(@ModelAttribute("gpheadform") GPHeadUpdateForm gpheadform){
        
        ModelAndView mav = null;
        try{
            
            gpheadform = gpHeadDAO.getBillDetails(gpheadform);
            
            mav = new ModelAndView("/GPHeadUpdate/GPHeadUpdate", "gpheadform", gpheadform);
        }catch(Exception e){
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "updateGPHead",params = "update")
    public ModelAndView updateGPHead(@ModelAttribute("gpheadform") GPHeadUpdateForm gpheadform){
        
        ModelAndView mav = null;
        try{
            
            int retVal = gpHeadDAO.updateGPHead(gpheadform);
            
            mav = new ModelAndView("/GPHeadUpdate/GPHeadUpdate", "gpheadform", gpheadform);
            mav.addObject("status", retVal+"");
        }catch(Exception e){
            e.printStackTrace();
        }
        return mav;
    }
}
