/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.tpfreport;

import hrms.dao.master.OfficeDAO;
import hrms.dao.tpfreport.EmpTpfDetailsDAO;
import hrms.model.master.Office;
import hrms.model.tpfreport.TPFReportBean;
import hrms.model.tpfreport.TPFReportList;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;
import static org.joda.time.DateTimeFieldType.year;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Madhusmita
 */
@Controller

public class EmployeeTpfDetailsController {

    @Autowired
    public EmpTpfDetailsDAO empTpfDao;

    @Autowired
    public OfficeDAO officeDao;
    
    @RequestMapping(value = "EmpTpfInfo")
    public ModelAndView viewEmpTpfDetails(@ModelAttribute("empTpfInfo") TPFReportBean tpbean,@ModelAttribute("officeModel") Office office, ModelMap model, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("tpfreport/OfficeWiseEmpTpfList");
        return mv;
    }

    @RequestMapping(value = "EmployeeTpfInfo")
    public ModelAndView employeeTpfDetails(@ModelAttribute("empTpfInfo") TPFReportBean tpbean,@ModelAttribute("officeModel") Office office, ModelMap model, HttpServletResponse response) {
        String path="tpfreport/OfficeWiseEmpTpfList";
        ModelAndView mv = new ModelAndView();
        ArrayList tpfList = empTpfDao.getTpfEmployeeList(tpbean.getMonth(), tpbean.getYear(), tpbean.getDdoCode());        
        model.addAttribute("tpfList", tpfList);
        office=officeDao.getOfficeName(tpbean.getDdoCode());
        model.addAttribute("offNm",office.getOffEn() );
        return new ModelAndView ( path , "officeModel" , office) ;

    }

}
