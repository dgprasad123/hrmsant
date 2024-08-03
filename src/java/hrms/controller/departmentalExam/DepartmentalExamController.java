package hrms.controller.departmentalExam;

import hrms.dao.departmentalExam.DepartmentalExamDAO;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.model.departmentalExam.DepartmentalExamForm;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
@Controller
public class DepartmentalExamController {

    @Autowired
    DepartmentalExamDAO deptartmentalExamDAO;
    
    @Autowired
    DepartmentDAO deptDAO;
    
    @Autowired
    public DistrictDAO districtDAO;
    
    @Autowired
    OfficeDAO officeDao;
    
    @Autowired
    SubStantivePostDAO subStantivePostDao;
    
    @Autowired
    ServiceBookLanguageDAO sbDAO;
    
    @RequestMapping(value = "DepartmentalExamList")
    public ModelAndView DepartmentalExamList(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("deptExamForm") DepartmentalExamForm deptExamForm) {

        ModelAndView mav = new ModelAndView();

        try {
            List deptexamlist = deptartmentalExamDAO.getDepartmentalExamList(selectedEmpObj.getEmpId());
            mav.addObject("deptexamlist", deptexamlist);
            mav.setViewName("/departmentalExam/DepartmentalExamList");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "NewDepartmentalExam")
    public ModelAndView NewDepartmentalExam(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("deptExamForm") DepartmentalExamForm deptExamForm) {
        
        ModelAndView mav = new ModelAndView();
        try {
            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            
            deptExamForm.setEmpid(selectedEmpObj.getEmpId());
                    
            mav = new ModelAndView("/departmentalExam/DepartmentalExamNew","deptExamForm",deptExamForm);
            mav.addObject("deptlist", deptlist);
            mav.addObject("distlist", distlist);
        }catch(Exception e){
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "SaveDepartmentalExam")
    public String SaveDepartmentalExam(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("deptExamForm") DepartmentalExamForm deptExamForm) {

        try {
            String sblang = sbDAO.getDeptExamDetails(deptExamForm);
            if(deptExamForm.getChkNotSBPrint()!=null && deptExamForm.getChkNotSBPrint().equals("Y")){
              deptExamForm.setIfvisible("N");
            }else{
               deptExamForm.setIfvisible("Y"); 
            }
                
            if(deptExamForm.getExamId() != null && !deptExamForm.getExamId().equals("")){
                deptartmentalExamDAO.updateDepartmentalExam(deptExamForm,sblang);
            }else{
                deptartmentalExamDAO.saveDepartmentalExam(deptExamForm, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLogingpc(),lub.getLoginempid(),sblang);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/DepartmentalExamList.htm";
    }
    
    @RequestMapping(value = "EditDepartmentalExam")
    public ModelAndView EditDepartmentalExam(@RequestParam("empid") String empid,@RequestParam("examid") String examid) {
        
        ModelAndView mav = new ModelAndView();
        try {
            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            
            DepartmentalExamForm deptExamForm = deptartmentalExamDAO.editDepartmentalExam(empid,examid);
            
            List officelist = officeDao.getTotalOfficeList(deptExamForm.getHidAuthDeptCode(), deptExamForm.getHidAuthDistCode());
            List gpclist = subStantivePostDao.getAuthorityGenericPostList(deptExamForm.getHidAuthOffCode());
            
            mav = new ModelAndView("/departmentalExam/DepartmentalExamNew","deptExamForm",deptExamForm);
            mav.addObject("deptlist", deptlist);
            mav.addObject("distlist", distlist);
            mav.addObject("officelist", officelist);
            mav.addObject("gpclist", gpclist);
        }catch(Exception e){
            e.printStackTrace();
        }
        return mav;
    }
}
