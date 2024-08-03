/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.misreport;

import hrms.dao.misreport.PostSanctionStrengthDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.onlineTicketing.OnlineTicketing;
import hrms.thread.misreport.FinanceDepartmentDataThread;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.ModelAndView;

/**
 *
 * @author DurgaPrasad
 */
@Controller
@SessionAttributes("LoginUserBean")
public class PostSanctionStrengthReport {

    @Autowired
    FinanceDepartmentDataThread financeDepartmentDataThread;

    @Autowired
    PostSanctionStrengthDAO postSanctionStrengthDAO;

    @RequestMapping(value = "generatePostSanctionStrength.htm", method = RequestMethod.GET)
    public ModelAndView generatePostSanctionStrengh(@ModelAttribute("LoginUserBean") LoginUserBean lub, Map<String, Object> model) {
        postSanctionStrengthDAO.generatePostSanctionStrengh();
        ModelAndView mv = new ModelAndView("redirect:/viewPostSanctionStrength.htm");
        return mv;
    }

    @RequestMapping(value = "viewPostSanctionStrength.htm", method = RequestMethod.GET)
    public String viewPostSanctionStrengh(@ModelAttribute("LoginUserBean") LoginUserBean lub, Map<String, Object> model) {
        model.put("gaReportlist", postSanctionStrengthDAO.viewPostSanctionStrengh());
        return "misreport/ViewPostSanctionStrength";
    }

    @RequestMapping(value = "generateFDEmpData.htm", method = RequestMethod.GET)
    public String generateFDEmpData(@ModelAttribute("LoginUserBean") LoginUserBean lub, Map<String, Object> model) {
        financeDepartmentDataThread.run();
        return "misreport/viewFDEmpData";
    }

    @RequestMapping(value = "generateCenusData.htm", method = RequestMethod.GET)
    public String generateCenusData(@ModelAttribute("LoginUserBean") LoginUserBean lub, Map<String, Object> model) {
        postSanctionStrengthDAO.generateCenusData();
        return "misreport/viewFDEmpData";
    }

}
