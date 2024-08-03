package hrms.controller.report.officewisepostgroup;

import hrms.dao.master.OfficeDAO;
import hrms.dao.report.officewisepostgroup.OfficeWisePostGroupDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Office;
import hrms.model.report.officewisepostgroup.OfficeWisePostGroup;
import java.util.ArrayList;
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
public class OfficeWisePostGroupController {

    @Autowired
    OfficeWisePostGroupDAO officeWisePostGroupDao;
    @Autowired
    public OfficeDAO offDAO;

    @RequestMapping(value = "officewisepostgrp.htm")
    public ModelAndView officeList(@ModelAttribute("officewisepostgrplist") OfficeWisePostGroup officewisepostgrplist, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = null;
        Office office = offDAO.getOfficeDetails(lub.getLoginoffcode());
     
        ArrayList officewisepostgrp = officeWisePostGroupDao.getOfficeWisePostGroupList(lub.getLoginoffcode());
        mv = new ModelAndView("/report/OfficeWisePostGroup", "officewisepostgrplist", officewisepostgrplist);
        mv.addObject("officewisepostgrp", officewisepostgrp);
         mv.addObject("offname", office.getOffEn());
        return mv;
    }
}
