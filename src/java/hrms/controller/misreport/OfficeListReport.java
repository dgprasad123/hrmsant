/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.misreport;

import hrms.dao.misreport.OfficeListDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.misreport.OfficeReport;
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
public class OfficeListReport {
    @Autowired
    OfficeListDAO officelistDao;
    @RequestMapping(value = "officelist.htm")
    public ModelAndView officeList(@ModelAttribute("officelist") OfficeReport officelist, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = null;
        ArrayList offList = officelistDao.getOfficeList(lub.getLoginoffcode());
        mv = new ModelAndView("/misreport/OfficeListReport", "officelist", officelist);
        mv.addObject("offList", offList);
        return mv;
    }
}
