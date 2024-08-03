/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.AerAuthorization;

import hrms.dao.employee.EmployeeDAO;
import hrms.dao.fiscalyear.FiscalYearDAO;
import hrms.dao.master.AerAuthorizationDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.model.AerAuthorization.AerAuthorizationBean;
import hrms.model.deptwiseaersubmitted.DeptWiseAerSubmitted;
import hrms.model.login.LoginUserBean;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author manisha
 */
@Controller

@SessionAttributes({"LoginUserBean", "SelectedEmpOffice"})
public class AerAuthorizationController {

    @Autowired
    FiscalYearDAO fiscalDAO;
    @Autowired
    EmployeeDAO employeeDAO;
    @Autowired
    DepartmentDAO departmentDAO;
    @Autowired
    SubStantivePostDAO substantivePostDAO;
    @Autowired
    OfficeDAO officeDao;
    @Autowired
    PostDAO postDAO;
    @Autowired
    AerAuthorizationDAO aerAuthorizationDAO;

    @RequestMapping(value = "aerauthorization.htm")
    public ModelAndView aerauthorization(@ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = null;

        List fiscyear = fiscalDAO.getFiscalYearList();
        aerAuthorizationBean.setAuthoritytype("OFFICE");
        mv = new ModelAndView("/deptwiseaersubmitted/AerAuthorization", "aerAuthorizationBean", aerAuthorizationBean);
        mv.addObject("fiscyear", fiscyear);
        return mv;
    }

    @RequestMapping(value = "aerauthorizationlist")
    public ModelAndView searchaerauthorization(@ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        ArrayList aerauthorizationList = aerAuthorizationDAO.getProcessAuthorizationList(aerAuthorizationBean.getFinancialyear(), lub.getLoginoffcode());
        List fiscyear = fiscalDAO.getFiscalYearList();
        mv.addObject("aerauthorizationList", aerauthorizationList);
        mv.addObject("fiscyear", fiscyear);
        mv.setViewName("/deptwiseaersubmitted/AerAuthorization");
        return mv;
    }

    @RequestMapping(value = "aerauthorizationlist", method = {RequestMethod.POST}, params = {"action=Add New"})
    public ModelAndView aerauthorizationDetail(@ModelAttribute("deptWiseAerSubmitted") DeptWiseAerSubmitted deptWiseAerSubmitted, @ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice) {
        ModelAndView mv = new ModelAndView();
        
        mv.addObject("officedetails", officeDao.getOfficeDetails(selectedEmpOffice));
        mv.setViewName("/deptwiseaersubmitted/AerAuthorizationDetail");
        return mv;

    }

    @RequestMapping(value = "aerauthorizationlist", method = {RequestMethod.POST}, params = {"action=Edit"})
    public ModelAndView editaerauthorizationDetail(@ModelAttribute("deptWiseAerSubmitted") DeptWiseAerSubmitted deptWiseAerSubmitted, @ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice) {
        ModelAndView mv = new ModelAndView();
        AerAuthorizationBean AerAuthorizationBean = aerAuthorizationDAO.getProcessAuthorization(aerAuthorizationBean.getFinancialyear(),selectedEmpOffice);
        
        mv.addObject("officedetails", officeDao.getOfficeDetails(selectedEmpOffice));
        mv.addObject("aerAuthorizationBean", AerAuthorizationBean);
        mv.setViewName("/deptwiseaersubmitted/AerAuthorizationDetail");
        return mv;

    }
    /*@RequestMapping(value = "aerauthorizationlist")
     public ModelAndView Searchaerauthorization(@ModelAttribute("deptWiseAerSubmitted") DeptWiseAerSubmitted deptWiseAerSubmitted, @ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
     ModelAndView mv = new ModelAndView();
     
     mv.setViewName("/deptwiseaersubmitted/AerAuthorizationDetail");
     return mv;
     }*/

    // @RequestMapping(value = "aerauthorizationOperator")
    @RequestMapping(value = "searchaerauthorization.htm")
    public ModelAndView search1aerauthorization(@ModelAttribute("deptWiseAerSubmitted") DeptWiseAerSubmitted deptWiseAerSubmitted, @ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();

        mv.addObject("departmentList", departmentDAO.getDepartmentList());
        mv.addObject("officeList", officeDao.getTotalOfficeList(aerAuthorizationBean.getDeptCode()));
        mv.addObject("postList", postDAO.getPostList(aerAuthorizationBean.getDeptCode()));
        mv.addObject("emplist", substantivePostDAO.getEmployeeNameWithSPC(aerAuthorizationBean.getOffCode(), aerAuthorizationBean.getPostCode()));
        mv.addObject("tempPostCode", aerAuthorizationBean.getPostCode());
        mv.setViewName("/deptwiseaersubmitted/officewiseAerList");
        return mv;
    }

    @RequestMapping(value = "aerauthorizationsearch.htm", method = {RequestMethod.POST}, params = {"action=Search"})
    public ModelAndView searchofaerauthorization(@ModelAttribute("deptWiseAerSubmitted") DeptWiseAerSubmitted deptWiseAerSubmitted, @ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("departmentList", departmentDAO.getDepartmentList());
        mv.addObject("officeList", officeDao.getTotalOfficeList(aerAuthorizationBean.getDeptCode()));
        mv.addObject("postList", postDAO.getPostList(aerAuthorizationBean.getDeptCode()));
        mv.addObject("emplist", substantivePostDAO.getEmployeeNameWithSPC(aerAuthorizationBean.getOffCode(), aerAuthorizationBean.getPostCode()));
        mv.addObject("tempPostCode", aerAuthorizationBean.getPostCode());
        mv.setViewName("/deptwiseaersubmitted/officewiseAerList");
        return mv;
    }

    @RequestMapping(value = "aerauthorizationlist.htm", method = {RequestMethod.POST}, params = {"action=Save"})
    public ModelAndView saveAerauthorization(@ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        aerAuthorizationBean.setOffCode(lub.getLoginoffcode());
        aerAuthorizationBean.setAuthoritytype("OFFICE");
        aerAuthorizationDAO.saveAerauthorisationDetail(aerAuthorizationBean);
        ArrayList aerauthorizationList = aerAuthorizationDAO.getProcessAuthorizationList(aerAuthorizationBean.getFinancialyear(), lub.getLoginoffcode());
        List fiscyear = fiscalDAO.getFiscalYearList();
        mv.addObject("aerauthorizationList", aerauthorizationList);
        mv.addObject("fiscyear", fiscyear);
        mv.setViewName("/deptwiseaersubmitted/AerAuthorization");
        return mv;
    }

    @RequestMapping(value = "saveaerauthorizationreviewer.htm", method = {RequestMethod.POST}, params = {"action=Save"})
    public ModelAndView saveAerauthorizationreviewer(@ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        aerAuthorizationBean.setOffCode(lub.getLoginoffcode());
        aerAuthorizationBean.setAuthoritytype("OFFICE");
        aerAuthorizationBean.setProcessid(13);
        aerAuthorizationDAO.saveAerauthorisationReviewerDetail(aerAuthorizationBean);
        ArrayList aerauthorizationList = aerAuthorizationDAO.getProcessAuthorizationList(aerAuthorizationBean.getFinancialyear(), lub.getLoginoffcode());
        List fiscyear = fiscalDAO.getFiscalYearList();
        mv.addObject("aerauthorizationList", aerauthorizationList);
        mv.addObject("fiscyear", fiscyear);
        mv.setViewName("/deptwiseaersubmitted/AerAuthorization");
        return mv;
    }

    @RequestMapping(value = "saveaerauthorizationapprover.htm", method = {RequestMethod.POST}, params = {"action=Save"})
    public ModelAndView saveAerauthorizationapprover(@ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        aerAuthorizationBean.setOffCode(lub.getLoginoffcode());
        aerAuthorizationBean.setAuthoritytype("OFFICE");
        aerAuthorizationBean.setProcessid(13);
        aerAuthorizationDAO.saveAerauthorisationAcceptorDetail(aerAuthorizationBean);
        ArrayList aerauthorizationList = aerAuthorizationDAO.getProcessAuthorizationList(aerAuthorizationBean.getFinancialyear(), lub.getLoginoffcode());
        List fiscyear = fiscalDAO.getFiscalYearList();
        mv.addObject("aerauthorizationList", aerauthorizationList);
        mv.addObject("fiscyear", fiscyear);
        mv.setViewName("/deptwiseaersubmitted/AerAuthorization");
        return mv;
    }

    @RequestMapping(value = "saveaerauthorizationreviewer.htm", method = {RequestMethod.POST}, params = {"action=Update"})
    public ModelAndView updateAerauthorizationreviewer(@ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        aerAuthorizationBean.setOffCode(lub.getLoginoffcode());
        aerAuthorizationBean.setAuthoritytype("OFFICE");
        //aerAuthorizationDAO.updateAerauthorisationReviewerDetail(aerAuthorizationBean);
        aerAuthorizationBean.setProcessid(13);
        aerAuthorizationDAO.saveAerauthorisationReviewerDetail(aerAuthorizationBean);
        ArrayList aerauthorizationList = aerAuthorizationDAO.getProcessAuthorizationList(aerAuthorizationBean.getFinancialyear(), lub.getLoginoffcode());
        List fiscyear = fiscalDAO.getFiscalYearList();
        mv.addObject("aerauthorizationList", aerauthorizationList);
        mv.addObject("fiscyear", fiscyear);
        mv.setViewName("/deptwiseaersubmitted/AerAuthorization");
        return mv;
    }

    @RequestMapping(value = "saveaerauthorizationapprover.htm", method = {RequestMethod.POST}, params = {"action=Update"})
    public ModelAndView updateAerauthorizationapprover(@ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        aerAuthorizationBean.setOffCode(lub.getLoginoffcode());
        aerAuthorizationBean.setAuthoritytype("OFFICE");
        aerAuthorizationDAO.updateAerauthorisationAcceptorDetail(aerAuthorizationBean);
        aerAuthorizationBean.setProcessid(13);
        aerAuthorizationDAO.saveAerauthorisationAcceptorDetail(aerAuthorizationBean);
        ArrayList aerauthorizationList = aerAuthorizationDAO.getProcessAuthorizationList(aerAuthorizationBean.getFinancialyear(), lub.getLoginoffcode());
        List fiscyear = fiscalDAO.getFiscalYearList();
        mv.addObject("aerauthorizationList", aerauthorizationList);
        mv.addObject("fiscyear", fiscyear);
        mv.setViewName("/deptwiseaersubmitted/AerAuthorization");
        return mv;
    }

    @RequestMapping(value = "aerauthorizationlist.htm", method = {RequestMethod.POST}, params = {"action=Update"})
    public ModelAndView updateAerauthorization(@ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        aerAuthorizationBean.setOffCode(lub.getLoginoffcode());
        aerAuthorizationDAO.updateAerauthorisationDetail(aerAuthorizationBean);
        ArrayList aerauthorizationList = aerAuthorizationDAO.getProcessAuthorizationList(aerAuthorizationBean.getFinancialyear(), lub.getLoginoffcode());
        List fiscyear = fiscalDAO.getFiscalYearList();
        aerAuthorizationBean.setAuthoritytype("OFFICE");

        mv.addObject("aerauthorizationList", aerauthorizationList);
        mv.addObject("fiscyear", fiscyear);
        mv.setViewName("/deptwiseaersubmitted/AerAuthorization");
        return mv;
    }

    @RequestMapping(value = "aerauthorizationsearch.htm", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView backofAerauthorization(@ModelAttribute("deptWiseAerSubmitted") DeptWiseAerSubmitted deptWiseAerSubmitted, @ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();

        mv.setViewName("/deptwiseaersubmitted/AerAuthorizationDetail");
        return mv;
    }

    @RequestMapping(value = "saveEmpApproverAuth.htm", method = {RequestMethod.POST}, params = {"action=Save"})
    public ModelAndView saveEmpApproverAuth(@ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        aerAuthorizationBean.setOffCode(lub.getLoginoffcode());
        aerAuthorizationBean.setProcessid(14);
        aerAuthorizationBean.setAuthoritytype("OFFICE");
        aerAuthorizationDAO.saveEmpApproverAuth(aerAuthorizationBean);
        ArrayList aerauthorizationList = aerAuthorizationDAO.getProcessAuthorizationList(aerAuthorizationBean.getFinancialyear(), lub.getLoginoffcode());
        List fiscyear = fiscalDAO.getFiscalYearList();
        mv.addObject("aerauthorizationList", aerauthorizationList);
        mv.addObject("fiscyear", fiscyear);
        mv.setViewName("/deptwiseaersubmitted/AerAuthorization");
        return mv;
    }

    @RequestMapping(value = "saveEmpApproverAuth.htm", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView backEmpApproverAuth(@ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        ArrayList aerauthorizationList = aerAuthorizationDAO.getProcessAuthorizationList(aerAuthorizationBean.getFinancialyear(), lub.getLoginoffcode());
        List fiscyear = fiscalDAO.getFiscalYearList();
        mv.addObject("aerauthorizationList", aerauthorizationList);
        mv.addObject("fiscyear", fiscyear);
        mv.setViewName("/deptwiseaersubmitted/AerAuthorization");
        return mv;
    }

    @RequestMapping(value = "saveEmpApproverAuth.htm", method = {RequestMethod.POST}, params = {"action=Update"})
    public ModelAndView updateEmpApproverAuth(@ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        aerAuthorizationBean.setOffCode(lub.getLoginoffcode());
        aerAuthorizationDAO.updateEmpApproverAuth(aerAuthorizationBean);
        ArrayList aerauthorizationList = aerAuthorizationDAO.getProcessAuthorizationList(aerAuthorizationBean.getFinancialyear(), lub.getLoginoffcode());
        List fiscyear = fiscalDAO.getFiscalYearList();
        aerAuthorizationBean.setAuthoritytype("OFFICE");
        mv.addObject("aerauthorizationList", aerauthorizationList);
        mv.addObject("fiscyear", fiscyear);
        mv.setViewName("/deptwiseaersubmitted/AerAuthorization");
        return mv;
    }
    
    @RequestMapping(value = "saveValidatorServiceBookAuth.htm", method = {RequestMethod.POST}, params = {"action=Save"})
    public ModelAndView saveValidatorServiceBookAuth(@ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        aerAuthorizationBean.setOffCode(lub.getLoginoffcode());
        aerAuthorizationBean.setProcessid(22);
        aerAuthorizationBean.setAuthoritytype("OFFICE");
        aerAuthorizationDAO.saveValidatorServiceBookAuth(aerAuthorizationBean);
        ArrayList aerauthorizationList = aerAuthorizationDAO.getProcessAuthorizationList(aerAuthorizationBean.getFinancialyear(), lub.getLoginoffcode());
        List fiscyear = fiscalDAO.getFiscalYearList();
        mv.addObject("aerauthorizationList", aerauthorizationList);
        mv.addObject("fiscyear", fiscyear);
        mv.setViewName("/deptwiseaersubmitted/AerAuthorization");
        return mv;
    }
    
    @RequestMapping(value = "saveValidatorServiceBookAuth.htm", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView saveValidatorServiceBookAuth(@ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        ArrayList aerauthorizationList = aerAuthorizationDAO.getProcessAuthorizationList(aerAuthorizationBean.getFinancialyear(), lub.getLoginoffcode());
        List fiscyear = fiscalDAO.getFiscalYearList();
        mv.addObject("aerauthorizationList", aerauthorizationList);
        mv.addObject("fiscyear", fiscyear);
        mv.setViewName("/deptwiseaersubmitted/AerAuthorization");
        return mv;
    }

    

    @RequestMapping(value = "saveValidatorServiceBookAuth.htm", method = {RequestMethod.POST}, params = {"action=Update"})
    public ModelAndView updateValidatorServiceBookAuth(@ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        aerAuthorizationBean.setOffCode(lub.getLoginoffcode());
        aerAuthorizationDAO.updateValidatorServiceBookAuth(aerAuthorizationBean);
        
        aerAuthorizationBean.setProcessid(22);
        aerAuthorizationBean.setAuthoritytype("OFFICE");
        aerAuthorizationDAO.saveValidatorServiceBookAuth(aerAuthorizationBean);
        
        ArrayList aerauthorizationList = aerAuthorizationDAO.getProcessAuthorizationList(aerAuthorizationBean.getFinancialyear(), lub.getLoginoffcode());
        List fiscyear = fiscalDAO.getFiscalYearList();       
        aerAuthorizationBean.setAuthoritytype("OFFICE");
        mv.addObject("aerauthorizationList", aerauthorizationList);
        mv.addObject("fiscyear", fiscyear);
        mv.setViewName("/deptwiseaersubmitted/AerAuthorization");
        return mv;
    }
    
    @RequestMapping(value = "saveentryServiceBookAuth.htm", method = {RequestMethod.POST}, params = {"action=Save"})
    public ModelAndView saveentryServiceBookAuth(@ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        aerAuthorizationBean.setOffCode(lub.getLoginoffcode());
        aerAuthorizationBean.setProcessid(23);
        aerAuthorizationBean.setAuthoritytype("OFFICE");
        aerAuthorizationDAO.saveentryServiceBookAuth(aerAuthorizationBean);
        ArrayList aerauthorizationList = aerAuthorizationDAO.getProcessAuthorizationList(aerAuthorizationBean.getFinancialyear(), lub.getLoginoffcode());
        List fiscyear = fiscalDAO.getFiscalYearList();
        mv.addObject("aerauthorizationList", aerauthorizationList);
        mv.addObject("fiscyear", fiscyear);
        mv.setViewName("/deptwiseaersubmitted/AerAuthorization");
        return mv;
    }
    
    @RequestMapping(value = "saveentryServiceBookAuth.htm", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView saveentryServiceBookAuth(@ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        ArrayList aerauthorizationList = aerAuthorizationDAO.getProcessAuthorizationList(aerAuthorizationBean.getFinancialyear(), lub.getLoginoffcode());
        List fiscyear = fiscalDAO.getFiscalYearList();
        mv.addObject("aerauthorizationList", aerauthorizationList);
        mv.addObject("fiscyear", fiscyear);
        mv.setViewName("/deptwiseaersubmitted/AerAuthorization");
        return mv;
    }

    

    @RequestMapping(value = "saveentryServiceBookAuth.htm", method = {RequestMethod.POST}, params = {"action=Update"})
    public ModelAndView updateValidatorServiceBookentry(@ModelAttribute("aerAuthorizationBean") AerAuthorizationBean aerAuthorizationBean, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        aerAuthorizationBean.setOffCode(lub.getLoginoffcode());
        aerAuthorizationDAO.updateValidatorServiceBookentry(aerAuthorizationBean);
        
        aerAuthorizationBean.setProcessid(23);
        aerAuthorizationBean.setAuthoritytype("OFFICE");
        aerAuthorizationDAO.saveentryServiceBookAuth(aerAuthorizationBean);
                    
        ArrayList aerauthorizationList = aerAuthorizationDAO.getProcessAuthorizationList(aerAuthorizationBean.getFinancialyear(), lub.getLoginoffcode());
        List fiscyear = fiscalDAO.getFiscalYearList();
        aerAuthorizationBean.setAuthoritytype("OFFICE");
        mv.addObject("aerauthorizationList", aerauthorizationList);
        mv.addObject("fiscyear", fiscyear);
        mv.setViewName("/deptwiseaersubmitted/AerAuthorization");
        return mv;
    }
}
