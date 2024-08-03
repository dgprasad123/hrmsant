package hrms.controller.deptwiseaersubmitted;

import hrms.dao.deptwiseaersubmitted.DeptWiseAerSubmittedDAO;
import hrms.dao.fiscalyear.FiscalYearDAO;
import hrms.dao.master.OfficeDAO;
import hrms.model.deptwiseaersubmitted.DeptWiseAerSubmitted;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Office;
import hrms.model.report.substantivepost.SubstantivePost;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpOffice"})
public class DeptWiseAerSubmittedController {

    @Autowired
    FiscalYearDAO fiscalDAO;
    @Autowired
    public OfficeDAO offDAO;
    @Autowired
    DeptWiseAerSubmittedDAO deptWiseAerSubmittedDAO;

    @RequestMapping(value = "deptwiseaersubmitted.htm")
    public ModelAndView offList(@ModelAttribute("deptWiseAerSubmitted") DeptWiseAerSubmitted deptWiseAerSubmitted, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = null;
        Office office = offDAO.getOfficeDetails(lub.getLoginoffcode());
        List fiscyear = fiscalDAO.getFiscalYearList();
        mv = new ModelAndView("/deptwiseaersubmitted/DeptWiseAerSubmittedReport", "deptwiseaersubmitted", deptWiseAerSubmitted);
        mv.addObject("offname", office.getOffEn());
        mv.addObject("fiscyear", fiscyear);
        return mv;
    }

    @RequestMapping(value = "deptwiseaersubmitted.htm", params = "search")
    public ModelAndView aerOffList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("deptWiseAerSubmitted") DeptWiseAerSubmitted deptWiseAerSubmitted, ModelMap model) {
        ModelAndView mv = null;
        Office office = offDAO.getOfficeDetails(lub.getLoginoffcode());
        List fiscyear = fiscalDAO.getFiscalYearList();
        List aeroffList = deptWiseAerSubmittedDAO.getAerOffList(lub.getLogindeptcode(),deptWiseAerSubmitted.getFiscalYear());
        mv = new ModelAndView("/deptwiseaersubmitted/DeptWiseAerSubmittedReport", "deptwiseaersubmitted", deptWiseAerSubmitted);
        mv.addObject("aersubmittedofflist", aeroffList);
         mv.addObject("fiscyear", fiscyear);
        return mv;
    }

}
