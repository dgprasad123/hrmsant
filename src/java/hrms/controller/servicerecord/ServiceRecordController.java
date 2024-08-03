package hrms.controller.servicerecord;

import hrms.dao.master.LeaveTypeDAO;
import hrms.dao.servicerecord.ServiceRecordDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.servicerecord.ServiceRecordForm;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class ServiceRecordController {
    
    @Autowired
    public ServiceRecordDAO servicerecordDAO;
    
    @Autowired
    public LeaveTypeDAO leaveTypeDAO;
    
    @RequestMapping(value = "ServiceRecordList")
    public String ServiceRecordList(Model model,@ModelAttribute("SelectedEmpObj") Users u,@ModelAttribute("serviceRecordForm") ServiceRecordForm serviceRecordForm){
        
        ArrayList servicerecordlist = servicerecordDAO.getServiceRecordList(u.getEmpId());
        model.addAttribute("servicerecordlist", servicerecordlist);
        
        return "/servicerecord/ServiceRecordList";
    }
    
    @RequestMapping(value = "NewServiceRecord")
    public String NewServiceRecord(Model model,@ModelAttribute("serviceRecordForm") ServiceRecordForm serviceRecordForm){
        
        ArrayList leavetpyeList = leaveTypeDAO.getLeaveTypeList();
        model.addAttribute("leavetpyeList", leavetpyeList);
        
        return "/servicerecord/NewServiceRecord";
    }
    
    @RequestMapping(value = "SaveServiceRecord")
    public String SaveServiceRecord(@ModelAttribute("SelectedEmpObj") Users u,@ModelAttribute("serviceRecordForm") ServiceRecordForm serviceRecordForm){
        servicerecordDAO.saveServiceRecord(serviceRecordForm, u.getEmpId());
        return "redirect:/ServiceRecordList.htm";
    }
    
    @RequestMapping(value = "editServiceRecord")
    public ModelAndView EditServiceRecord(@ModelAttribute("SelectedEmpObj") Users u,@RequestParam("srid") int srid){
        
        ModelAndView mav = null;
        
        ServiceRecordForm serviceRecordForm = servicerecordDAO.editServiceRecord(u.getEmpId(),srid);
        
        mav = new ModelAndView("/servicerecord/NewServiceRecord","serviceRecordForm",serviceRecordForm);
        
        ArrayList leavetpyeList = leaveTypeDAO.getLeaveTypeList();
        mav.addObject("leavetpyeList", leavetpyeList);
        
        return mav;
    }
    @RequestMapping(value = "deleteServiceRecorddata.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView deleteServiceRecord(Model model,@ModelAttribute("SelectedEmpObj") Users u,@RequestParam("srid") int srid ,@ModelAttribute("serviceRecordForm") ServiceRecordForm serviceRecordForm) {
        ModelAndView mv = null;
        servicerecordDAO.deleteServiceRecord(srid);
        ArrayList servicerecordlist = servicerecordDAO.getServiceRecordList(u.getEmpId());              
        mv = new ModelAndView("/servicerecord/ServiceRecordList","servicerecordlist",serviceRecordForm);
        mv.addObject("servicerecordlist", servicerecordlist);  
        return mv;
    }
    
}
