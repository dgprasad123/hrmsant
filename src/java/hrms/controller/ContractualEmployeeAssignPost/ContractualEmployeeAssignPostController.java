package hrms.controller.ContractualEmployeeAssignPost;

import hrms.dao.ContractualEmployeeAssignPost.ContractualEmployeeAssignPostDAO;
import hrms.model.ContractualEmployeeAssignPost.ContractualEmployeeAssignPostBean;
import hrms.model.login.LoginUserBean;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("LoginUserBean")
public class ContractualEmployeeAssignPostController {
    
    @Autowired
    public ContractualEmployeeAssignPostDAO contractualEmployeeAssignPost;
    
    @RequestMapping(value = "ContractualEmployeeAssignPost")
    public ModelAndView contractualEmployeeAssignPost(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("contractualAssignPost") ContractualEmployeeAssignPostBean cbean){
        
        ModelAndView mav = null;
        try{
            mav = new ModelAndView();            
            List emplist = contractualEmployeeAssignPost.getContractualEmployeeList(lub.getLoginoffcode());
            mav.addObject("emplist", emplist);            
            List postlist = contractualEmployeeAssignPost.getVacantPost(lub.getLoginoffcode());
            mav.addObject("postlist", postlist);            
            mav.setViewName("/ContractualEmployeeAssignPost/ContractualEmployeeAssignPost");
        }catch(Exception e){
            e.printStackTrace();
        }
      return mav;  
    }
    
    @RequestMapping(value = "saveAssignContractualPost")
    public ModelAndView AssignContractualPost(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("contractualAssignPost") ContractualEmployeeAssignPostBean cbean){
        
        ModelAndView mav = null;
        try{
            mav = new ModelAndView();
            
            String isMapped = contractualEmployeeAssignPost.mapVacantPostToEmployee(cbean);
            mav.addObject("isMapped", isMapped);
            
            List emplist = contractualEmployeeAssignPost.getContractualEmployeeList(lub.getLoginoffcode());
            mav.addObject("emplist", emplist);
            
            List postlist = contractualEmployeeAssignPost.getVacantPost(lub.getLoginoffcode());
            mav.addObject("postlist", postlist);
            
            mav.setViewName("/ContractualEmployeeAssignPost/ContractualEmployeeAssignPost");
        }catch(Exception e){
            e.printStackTrace();
        }
      return mav;  
    }
    
    @RequestMapping(value = "openAddNewPost")
    public ModelAndView OpenAddNewPost(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("contractualAssignPost") ContractualEmployeeAssignPostBean cbean){
        
        ModelAndView mav = null;
        try{
            mav = new ModelAndView();
            
            return new ModelAndView("redirect:/GPCWiseSPCList.htm");
        }catch(Exception e){
            e.printStackTrace();
        }
      return mav;  
    }
}
