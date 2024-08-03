/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.discProceeding;

import hrms.common.CommonFunctions;
import hrms.dao.discProceeding.DiscProceedingAdminDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.tab.ServicePanelDAO;
import hrms.model.discProceeding.DPPendingReportBean;
import hrms.model.empinfo.EmployeeSearchResult;
import hrms.model.empinfo.SearchEmployee;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.master.Module;
import hrms.model.parmast.ParAdminSearchCriteria;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas
 */
@Controller

@SessionAttributes({"LoginUserBean", "Privileges", "SelectedEmpObj"})
public class DiscProceedingAdminController {

    @Autowired
    DepartmentDAO departmentDao;
    @Autowired
    DiscProceedingAdminDAO discProceedingAdminDAO;

    @Autowired
    public ServicePanelDAO servicePanelDAO;

    @RequestMapping(value = "viewDPAdmin.htm", method = RequestMethod.GET)
    public ModelAndView viewDPAdmin(@Valid @ModelAttribute("searchEmployee") SearchEmployee empinfo, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("Privileges") Module[] privileges) {
        ModelAndView mv = new ModelAndView();
        if (CommonFunctions.hasPrivileage(privileges, "viewDPAdmin.htm")) {
            mv.addObject("departmentList", departmentDao.getDepartmentList());
            mv.setViewName("/discProceeding/DPAdmin");
        } else {
            mv.setViewName("/under_const");
        }
        return mv;
    }

    @RequestMapping(value = "redirect2DPAInterface.htm", method = RequestMethod.GET)
    public ModelAndView redirect2DPAInterface(@Valid @ModelAttribute("searchEmployee") SearchEmployee empinfo, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("Privileges") Module[] privileges, @RequestParam(value = "empId", required = false) String empId) {
        hrms.model.login.Users selectedEmpObj = new Users();

        selectedEmpObj = servicePanelDAO.getSelectedEmployeeInfo(empId);

        ModelAndView mv = new ModelAndView("redirect:/ConclusionProceedingsList.htm");
        mv.addObject("SelectedEmpObj", selectedEmpObj);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "getSearchEmployeeList.htm", method = RequestMethod.POST)
    public void getSearchEmployeeList(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("searchEmployee") SearchEmployee empinfo, HttpServletResponse response) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        EmployeeSearchResult empSearchResult = discProceedingAdminDAO.locateEmployee(empinfo);
        JSONObject obj = new JSONObject(empSearchResult);
        out = response.getWriter();
        out.write(obj.toString());
    }

    @RequestMapping(value = "pendingDepartmentProceedingReport.htm")
    public ModelAndView pendingDepartmentProceedingReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("dPPendingReportBean") DPPendingReportBean DPPendingReportBean) {
        ModelAndView mv = new ModelAndView("/discProceeding/DPPendingReport");

        return mv;
    }

    @RequestMapping(value = "pendingDepartmentProceedingReport.htm", params = {"action=Departmentwise Dp Report"}, method = RequestMethod.POST)
    public ModelAndView departmentwiseDpReport(@ModelAttribute("dPPendingReportBean") DPPendingReportBean dPPendingReportBean) {
        ModelAndView mv = new ModelAndView();
        ArrayList dpStatusReport = discProceedingAdminDAO.getPendingDisciplinaryProceedingReportByAdmin();
        mv.addObject("dpStatusReport", dpStatusReport);
        mv.setViewName("/discProceeding/DPPendingReportDepartmentWise");
        return mv;
    }

    @RequestMapping(value = "pendingDepartmentProceedingReport.htm", params = {"action=Back"}, method = RequestMethod.POST)
    public ModelAndView backdepartmentwiseDpReport(@ModelAttribute("dPPendingReportBean") DPPendingReportBean dPPendingReportBean) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:pendingDepartmentProceedingReport.htm");
        return mav;
    }

    @RequestMapping(value = "pendingDepartmentProceedingReport.htm", params = {"action=Cadrewise Dp Report"}, method = RequestMethod.POST)
    public ModelAndView cadrewiseDpReport(@ModelAttribute("dPPendingReportBean") DPPendingReportBean dPPendingReportBean) {
        ModelAndView mv = new ModelAndView();
        ArrayList dpStatusReportCadreWise = discProceedingAdminDAO.getPendingDPReportCadrewiseByAdmin();
        mv.addObject("dpStatusReportCadreWise", dpStatusReportCadreWise);
        mv.setViewName("/discProceeding/DPPendingReportCadreWise");
        return mv;
    }

    @RequestMapping(value = "pendingDPdepartmentwiseReportExcel.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView pendingDPdepartmentwiseReportExcel(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("dPPendingReportBean") DPPendingReportBean dPPendingReportBean) {
        ModelAndView mv = new ModelAndView();
        ArrayList dpStatusReportDepartmentWise = discProceedingAdminDAO.getPendingDPReportDepartmentwiseByAdmin();
        mv.addObject("dpStatusReportDepartmentWise", dpStatusReportDepartmentWise);
        mv.setViewName("pendingDepartmentProceedingDeptwiseExcel");
        return mv;
    }
}
