/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.leavesanction;

import hrms.dao.leave.LeaveDAO;
import hrms.model.leave.Leave;
import hrms.model.login.Users;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
 
/**
 *
 * @author Surendra
 */

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class LeaveSanctionController {
    
    @Autowired
    LeaveDAO leaveDao;
    
    @RequestMapping(value = "LeaveSanctionControllerList")
    public ModelAndView LeaveSanctionControllerListPage(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("leave") Leave leave) {

        List leavelist = null;
        
        ModelAndView mav = null;
        try{
            System.out.println("EMP ID inside Joining List is: "+lub.getEmpId());
            
            leavelist = leaveDao.getLeaveList(leave.getEmpId(), leave.getTollid());
            
            mav = new ModelAndView("/leavesanction/LeaveSanction", "leave", leave);
            mav.addObject("leaveList", leavelist);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return mav;
    }
    
    
    @RequestMapping(value = "LeaveSanctionControllerDataNew")
    public ModelAndView LeaveSanctionControllerDataNew(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("leave") Leave leave) {

        List leavelist = null;
        
        ModelAndView mav = null;
        try{
            System.out.println("EMP ID inside Joining List is: "+lub.getEmpId());
            
            leavelist = leaveDao.getLeaveList(leave.getEmpId(), leave.getTollid());
            
            mav = new ModelAndView("/leavesanction/LeaveSanctionData", "leave", leave);
            mav.addObject("leaveList", leavelist);
          //  System.out.println("hghghg");
        }catch(Exception e){
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "saveLeaveSanctionControllerData", params = "action=Save")
    public ModelAndView saveLeaveSanctionControllerData(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("leave") Leave leave) {

        List leavelist = null;
        
        ModelAndView mav = null;
        try{
            System.out.println("EMP ID inside  List is: "+lub.getEmpId());
            
            leavelist = leaveDao.getLeaveList(leave.getEmpId(), leave.getTollid());
            
            mav = new ModelAndView("redirect:/LeaveSanctionControllerList.htm");
            mav.addObject("leaveList", leavelist);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "saveLeaveSanctionControllerData", params = "action=Back")
    public ModelAndView backLeaveSanctionControllerData(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("leave") Leave leave) {

        List leavelist = null;
        
        ModelAndView mav = null;
        try{
            System.out.println("EMP ID inside Joining List is: "+lub.getEmpId());
            
            leavelist = leaveDao.getLeaveList(leave.getEmpId(), leave.getTollid());
            
            mav = new ModelAndView("redirect:/LeaveSanctionControllerList.htm");
            mav.addObject("leaveList", leavelist);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return mav;
    }
}
