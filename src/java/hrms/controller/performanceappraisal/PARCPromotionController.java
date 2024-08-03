/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.performanceappraisal;

import hrms.common.CommonFunctions;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.fiscalyear.FiscalYearDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.performanceappraisal.PARAdminDAO;
import hrms.dao.performanceappraisal.PARCPromotionDAO;
import hrms.dao.preferauthority.SactionedAuthorityDAO;
import hrms.dao.task.TaskDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Cadre;
import hrms.model.master.Module;
import hrms.model.parmast.GroupCAdminAdminSearchCriteria;
import hrms.model.parmast.GroupCCustodianCommunication;
import hrms.model.parmast.GroupCEmployee;
import hrms.model.parmast.GroupCInitiatedbean;
import hrms.model.parmast.GroupCSearchResult;
import hrms.model.parmast.ParApplyForm;
import hrms.model.parmast.ParAssignPrivilage;
import hrms.model.preferauthority.WorkflowAuthority;
import hrms.model.task.TaskListHelperBean;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
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
 * @author manisha
 */
@Controller
@SessionAttributes({"LoginUserBean", "Privileges", "SelectedEmpObj"})
public class PARCPromotionController {

    /* @Autowired
     PARAdminDAO parAdminDAO; */
    @Autowired
    PARCPromotionDAO parCPromotionDAO;

    @Autowired
    DepartmentDAO departmentDAO;

    @Autowired
    public SactionedAuthorityDAO sactionedAuthorityDao;

    @Autowired
    TaskDAO taskDAO;

    @Autowired
    FiscalYearDAO fiscalDAO;

    @Autowired
    EmployeeDAO employeeDAO;

    @Autowired
    public DistrictDAO districtDAO;

    /*This Code is used to see the final Report of Group C Employee after complete process*/
    @RequestMapping(value = "parCPromotionReport.htm", method = RequestMethod.GET)
    public ModelAndView parCPromotionReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCInitiatedbean") GroupCInitiatedbean groupCInitiatedbean) {
        ModelAndView mv = new ModelAndView();
        if (lub.getLoginPostGrptype().equals("A") || lub.getLoginPostGrptype().equals("B")) {
            ArrayList promotionReportList = parCPromotionDAO.getPromotionReportList(lub.getLoginempid());
            List fiscyear = fiscalDAO.getFiscalYearListForGroupCPromotion();
            int taskid = parCPromotionDAO.gettaskId(groupCInitiatedbean.getGroupCpromotionId());
            String[] revertreason = parCPromotionDAO.getRevertReasonOfGroupCPAR(groupCInitiatedbean.getGroupCpromotionId(), taskid);
            mv.addObject("promotionReportList", promotionReportList);
            mv.addObject("fiscyear", fiscyear);
            mv.addObject("authorityType", revertreason[0]);
            mv.addObject("revertRemaeks", revertreason[1]);
            mv.addObject("authorityName", revertreason[2]);
            mv.setViewName("/parCpromotion/ParCPromotionFinalReport");
        } else {
            mv.setViewName("/errorpage/error404");
        }
        return mv;
    }
   
    @RequestMapping(value = "GetFiscalYearListForGroupCJSON", method = RequestMethod.POST)
    public @ResponseBody
    void getFiscalYearListJSON(HttpServletResponse response) {
        response.setContentType("application/json");
        JSONArray json = null;
        PrintWriter out = null;
        try {
            List fiscalyearlist = fiscalDAO.getFiscalYearListForGroupCPromotion();
            json = new JSONArray(fiscalyearlist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    /*This Code is used to create new Group C Employee Report*/
    @RequestMapping(value = "parCPromotionReport.htm", method = RequestMethod.POST, params = {"action=Create"})
    public ModelAndView createParCPromotionReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCInitiatedbean") GroupCInitiatedbean groupCInitiatedbean) {
        ModelAndView mv = new ModelAndView();
        groupCInitiatedbean.setReportingempId(lub.getLoginempid());
        groupCInitiatedbean.setReportingspc(lub.getLoginspc());
        List fiscyear = fiscalDAO.getFiscalYearListForGroupCPromotion();
        //mav.addObject("custdianDetail", custdianDetail);
        mv.addObject("fiscyear", fiscyear);
        parCPromotionDAO.createParCPromotionReport(groupCInitiatedbean);
        return new ModelAndView("redirect:/parCPromotionReport.htm");
    }

    /*This Code is used to go back to the final Report*/
    @RequestMapping(value = "parCPromotionReport.htm", params = {"action=Back"})
    public ModelAndView backparCPromotionReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCInitiatedbean") GroupCInitiatedbean groupCInitiatedbean) {
        return new ModelAndView("redirect:/parCPromotionReport.htm");
    }

    /*This Code is used to get all the selected Group C Employee Report*/
    @RequestMapping(value = "showGroupCEmployee", method = {RequestMethod.GET})
    public ModelAndView showGroupCEmployee(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCInitiatedbean") GroupCEmployee groupCEmployee) {
        ModelAndView mv = new ModelAndView();
        List groupcemplist = parCPromotionDAO.getGroupCEmployeeList(lub.getLoginoffcode(), groupCEmployee.getGroupCpromotionId());
        mv.addObject("groupcemplist", groupcemplist);
        mv.addObject("fiscalyear", groupCEmployee.getFiscalyear());
        mv.setViewName("/parCpromotion/PARCemplist");
        return mv;
    }

    @RequestMapping(value = "EditSelectedEmpDetail.htm", method = RequestMethod.GET)
    public ModelAndView editSelectedEmpDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mav = new ModelAndView();
        ArrayList empList = parCPromotionDAO.getSelectedgroupCEmpList(groupCEmployee.getGroupCpromotionId());
        mav.addObject("empList", empList);
        mav.setViewName("/parCpromotion/DetailReportOfPARCemplist");
        return mav;
    }

    @RequestMapping(value = "removenewparCPromotionReport.htm", method = RequestMethod.GET)
    public ModelAndView removenewparCPromotionReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCInitiatedbean") GroupCInitiatedbean groupCInitiatedbean) {
        ModelAndView mv = new ModelAndView();
        groupCInitiatedbean.setGroupCpromotionId(groupCInitiatedbean.getGroupCpromotionId());
        parCPromotionDAO.deletereviewedEmpDetails(groupCInitiatedbean);
        groupCInitiatedbean.setReportingempId(lub.getLoginempid());
        ArrayList promotionReportList = parCPromotionDAO.getPromotionReportList(lub.getLoginempid());
        mv.addObject("promotionReportList", promotionReportList);
        mv.setViewName("/parCpromotion/ParCPromotionFinalReport");
        return mv;
    }

    @RequestMapping(value = "parCPromotionReport.htm", params = {"action=Get Selected Employee List"}, method = RequestMethod.POST)
    public ModelAndView submitparCPromotionReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee, @ModelAttribute("groupCInitiatedbean") GroupCInitiatedbean groupCInitiatedbean) {
        ModelAndView mav = new ModelAndView();
        ArrayList empList = parCPromotionDAO.getSelectedgroupCEmpList(groupCEmployee.getGroupCpromotionId());
        groupCEmployee.setMode("getemplist");
        // mav.setViewName("/parCpromotion/DetailReportOfPARCemplist");
        mav = new ModelAndView("/parCpromotion/DetailReportOfPARCemplist", "groupCEmployee", groupCEmployee);
        mav.addObject("empList", empList);
        return mav;
    }

    @RequestMapping(value = "parCPromotionReport.htm", params = {"action=Other Office"}, method = RequestMethod.POST)
    public ModelAndView otherOfficeparCPromotionReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee, @ModelAttribute("groupCInitiatedbean") GroupCInitiatedbean groupCInitiatedbean) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("departmentList", departmentDAO.getDepartmentList());
        mv.setViewName("/parCpromotion/SearchEmployeeFromOtherOffice");
        return mv;
    }

    @RequestMapping(value = "searchEmployeeList.htm", params = {"action=Get Employee"}, method = RequestMethod.POST)
    public ModelAndView searchEmployeeList(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm, @RequestParam("deptCode") String deptCode, @RequestParam("offCode") String offCode, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee, @ModelAttribute("groupCInitiatedbean") GroupCInitiatedbean groupCInitiatedbean) throws IOException, JSONException {
        ModelAndView mav = new ModelAndView();
        mav.addObject("departmentList", departmentDAO.getDepartmentList());
        //mav.addObject("empList", employeeDAO.getOfficeWiseEmployeeList(offCode));
        mav.addObject("empList", parCPromotionDAO.getGroupCEmployeeListFromOtherOffice(offCode, groupCEmployee.getGroupCpromotionId()));
        mav.addObject("deptCode", lub.getLogindeptcode());
        mav.setViewName("/parCpromotion/SearchEmployeeFromOtherOffice");
        return mav;
    }

    @RequestMapping(value = "searchEmployeeList.htm", params = {"action=Get Selected Employee List"}, method = RequestMethod.POST)
    public ModelAndView getSelectedEmployeeList(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm, @RequestParam("deptCode") String deptCode, @RequestParam("offCode") String offCode, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) throws IOException, JSONException {
        ModelAndView mav = new ModelAndView();
        ArrayList empList = parCPromotionDAO.getSelectedgroupCEmpList(groupCEmployee.getGroupCpromotionId());
        groupCEmployee.setMode("getemplist");
        // mav.setViewName("/parCpromotion/DetailReportOfPARCemplist");
        mav = new ModelAndView("/parCpromotion/DetailReportOfPARCemplist", "groupCEmployee", groupCEmployee);
        mav.addObject("empList", empList);
        return mav;
    }

    @RequestMapping(value = "searchEmployeeList.htm", params = {"action=Back"}, method = RequestMethod.POST)
    public ModelAndView BackSelectedEmployeeList(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm, @RequestParam("deptCode") String deptCode, @RequestParam("offCode") String offCode, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) throws IOException, JSONException {
        return new ModelAndView("redirect:/showGroupCEmployee.htm");
    }

    @RequestMapping(value = "parCPromotionReport.htm", params = {"action=Get Fit For Promotion List"}, method = RequestMethod.POST)
    public ModelAndView GetFitForPromotionListparCPromotionReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mv = new ModelAndView();
        ArrayList fitForPronotionempList = parCPromotionDAO.getSelectedFitForPronotionCEmpList(lub.getLoginempid());
        mv.addObject("fitForPronotionempList", fitForPronotionempList);
        mv.setViewName("/parCpromotion/FitForPromotionEmployeeList");
        return mv;
    }

    @RequestMapping(value = "parCPromotionReport.htm", params = {"action=Forward"}, method = RequestMethod.POST)
    public ModelAndView forwardparCPromotionReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mv = new ModelAndView();
        boolean isReortingSaveAllDate = parCPromotionDAO.isReortingSaveAllDate(lub.getLoginoffcode(), groupCEmployee.getGroupCpromotionId());
        List authoritylist = parCPromotionDAO.gethigherAuthorityList(lub.getLogindeptcode(), lub.getLoginoffcode());
        mv.addObject("isReortingSaveAllDate", isReortingSaveAllDate);
        mv.addObject("authoritylist", authoritylist);
        mv.addObject("departmentList", departmentDAO.getDepartmentList());
        mv.setViewName("/parCpromotion/higherAuthorityList");
        return mv;
    }

    @RequestMapping(value = "parCPromotionReport.htm", params = {"action=Revert"}, method = RequestMethod.POST)
    public ModelAndView RevertforwardparCPromotionReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mv = new ModelAndView();

        mv.setViewName("/parCpromotion/RevertGroupCPAR");
        return mv;
    }

    @RequestMapping(value = "RevertGroupCPAR.htm", params = {"action=Submit"}, method = RequestMethod.POST)
    public ModelAndView SaveRevertparCPromotionReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mv = new ModelAndView();
        String isReverted = "N";
        isReverted = parCPromotionDAO.revertGroupCPARByAuthority(lub.getLoginempid(), groupCEmployee);
        mv.addObject("revertstatus", isReverted);
        mv.setViewName("/parCpromotion/RevertGroupCPAR");
        return mv;
    }

    @RequestMapping(value = "parCForwardReport.htm", params = {"action=Search Authority"}, method = RequestMethod.POST)
    public ModelAndView SearchAuthorityparCPromotionReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) {
        ModelAndView mv = new ModelAndView("/par/SearchAuthority");
        mv.addObject("departmentList", departmentDAO.getDepartmentPARList());
        mv.setViewName("/parCpromotion/SearchAuthority");
        return mv;
    }

    @RequestMapping(value = "parCForwardReport.htm", params = {"action=Revert"}, method = RequestMethod.POST)
    public ModelAndView RevertforwardparCPromotionReportByAccepting(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/parCpromotion/RevertGroupCPAR");
        return mv;
    }

    @RequestMapping(value = "searchAuthortityList.htm", params = {"action=Get Authority"}, method = RequestMethod.POST)
    public ModelAndView searchAuthortityList(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm, @RequestParam("deptCode") String deptCode, @RequestParam("offCode") String offCode, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) throws IOException, JSONException {
        ModelAndView mv = new ModelAndView();
        List authoritylist = parCPromotionDAO.gethigherAuthorityList(deptCode, offCode);
        mv.addObject("departmentList", departmentDAO.getDepartmentList());
        mv.addObject("authoritylist", authoritylist);
        mv.setViewName("/parCpromotion/SearchAuthority");
        return mv;
    }

    /*@RequestMapping(value = "searchAuthortityList.htm", params = {"action=Submit"}, method = RequestMethod.POST)
     public ModelAndView SubmitsearchAuthortityList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
     ModelAndView mv = new ModelAndView();
     String temp = groupCEmployee.getReportingempId();

     String[] tempArray = temp.split("-");
     groupCEmployee.setReportingempId(lub.getLoginempid());
     groupCEmployee.setReportingspc(lub.getLoginspc());
     groupCEmployee.setReviewingempId(tempArray[0]);
     groupCEmployee.setReviewingspc(tempArray[1]);
     //parAdminDAO.forwardToAdverseApprisee(groupCEmployee);
     parCPromotionDAO.savehigherAuthorityForwardRemark(groupCEmployee);
     mv.setViewName("/parCpromotion/SubmitRemark");
     return mv;
     } */
    @RequestMapping(value = "searchAuthortityList.htm", params = {"action=Submit"}, method = RequestMethod.POST)
    public ModelAndView SubmitsearchAuthortityList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mv = new ModelAndView();
        String temp = groupCEmployee.getReportingempId();

        if (groupCEmployee.getRemarkauthoritytype().equals("REVIEWING")) {
            String[] tempArray = temp.split("-");
            groupCEmployee.setAcceptingempId(tempArray[0]);
            if (tempArray[0] != null && tempArray[0].length() == 8) {
                groupCEmployee.setAcceptingspc(tempArray[1]);
            } else if (tempArray[0] != null && tempArray[0].length() == 2) {
                groupCEmployee.setAcceptingspc(null);
            }
            parCPromotionDAO.forwardReviewingPromotionList(groupCEmployee);
        } else if (groupCEmployee.getRemarkauthoritytype().equals("ACCEPTING")) {
            parCPromotionDAO.submitAcceptingingPromotionList(groupCEmployee);
            mv.setViewName("/parCpromotion/AcceptingSubmitRemark");
        } else {
            String[] tempArray = temp.split("-");
            groupCEmployee.setReportingempId(lub.getLoginempid());
            groupCEmployee.setReportingspc(lub.getLoginspc());
            groupCEmployee.setReviewingempId(tempArray[0]);
            if (tempArray[0] != null && tempArray[0].length() == 8) {
                groupCEmployee.setReviewingspc(tempArray[1]);
            } else if (tempArray[0] != null && tempArray[0].length() == 2) {
                groupCEmployee.setReviewingspc(null);
            }
            //parAdminDAO.forwardToAdverseApprisee(groupCEmployee);
            parCPromotionDAO.savehigherAuthorityForwardRemark(groupCEmployee);
        }
        //forwardReviewingPromotionList
        mv.setViewName("/parCpromotion/SubmitRemark");
        return mv;
    }

    /*  @RequestMapping(value = "preferedAuthortityList.htm")
     public ModelAndView preferedAuthortityListView(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) throws IOException, JSONException {
     ModelAndView mv = new ModelAndView("/par/PreferedAuthortityView");
     String appriseSPC = parAdminDAO.getAppriseSPCOfPar(parApplyForm.getParId());
     List sanctionedauthoritylist = sactionedAuthorityDao.getSanctionedAuthorityList(appriseSPC, 3, parApplyForm.getFiscalYear(), lub.getLoginempid());
     mv.addObject("appriseSPC", appriseSPC);
     mv.addObject("sanctionedauthoritylist", sanctionedauthoritylist);
     return mv;
     } */
    @RequestMapping(value = "searchAuthortityList.htm", params = {"action=Save"}, method = RequestMethod.POST)
    public ModelAndView saveAuthortityList(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("postcode") String postcode, @ModelAttribute("WorkflowAuthority") WorkflowAuthority wa, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) throws IOException, JSONException {
        ModelAndView mv = new ModelAndView();
        String[] vpreferedpostcode = postcode.split(",");
        sactionedAuthorityDao.addAuthoritySPC(vpreferedpostcode, wa.getSpc(), 3, wa.getHidDeptCode(), wa.getHidOffCode());
        mv.setViewName("/parCpromotion/higherAuthorityList");
        return mv;
    }

    @RequestMapping(value = "ViewpreferedAuthortityList.htm")
    public ModelAndView ViewpreferedAuthortityList(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) throws IOException, JSONException {
        ModelAndView mv = new ModelAndView("/par/PreferedAuthortityList");
        String appriseSPC = parCPromotionDAO.getAppriseSPCOfPar(parApplyForm.getParId());
        List sanctionedauthoritylist = sactionedAuthorityDao.getSanctionedAuthorityList(appriseSPC, 3, parApplyForm.getFiscalYear(), lub.getLoginempid());
        mv.addObject("appriseSPC", appriseSPC);
        mv.addObject("sanctionedauthoritylist", sanctionedauthoritylist);
        return mv;
    }

    @RequestMapping(value = "parCForwardReport.htm", params = {"action=Submit"}, method = RequestMethod.POST)
    public ModelAndView SubmitparCPromotionReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mv = new ModelAndView();
        String temp = groupCEmployee.getReportingempId();

        if (groupCEmployee.getRemarkauthoritytype().equals("REVIEWING")) {
            String[] tempArray = temp.split("-");
            groupCEmployee.setAcceptingempId(tempArray[0]);
            if (tempArray[0] != null && tempArray[0].length() == 8) {
                groupCEmployee.setAcceptingspc(tempArray[1]);
            } else if (tempArray[0] != null && tempArray[0].length() == 2) {
                groupCEmployee.setAcceptingspc(null);
            }
            parCPromotionDAO.forwardReviewingPromotionList(groupCEmployee);
        } else if (groupCEmployee.getRemarkauthoritytype().equals("ACCEPTING")) {
            parCPromotionDAO.submitAcceptingingPromotionList(groupCEmployee);
            mv.setViewName("/parCpromotion/AcceptingSubmitRemark");
        } else {
            String[] tempArray = temp.split("-");
            groupCEmployee.setReportingempId(lub.getLoginempid());
            groupCEmployee.setReportingspc(lub.getLoginspc());
            groupCEmployee.setReviewingempId(tempArray[0]);
            if (tempArray[0] != null && tempArray[0].length() == 8) {
                groupCEmployee.setReviewingspc(tempArray[1]);
            } else if (tempArray[0] != null && tempArray[0].length() == 2) {
                groupCEmployee.setReviewingspc(null);
            }

            //parAdminDAO.forwardToAdverseApprisee(groupCEmployee);
            parCPromotionDAO.savehigherAuthorityForwardRemark(groupCEmployee);
        }
        //forwardReviewingPromotionList
        mv.setViewName("/parCpromotion/SubmitRemark");
        return mv;
    }

    @RequestMapping(value = "parCForwardReport.htm", params = {"action=Back"})
    public ModelAndView backparCPromotion(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mav = new ModelAndView();
        ArrayList empList = parCPromotionDAO.getSelectedgroupCEmpList(groupCEmployee.getGroupCpromotionId());
        groupCEmployee.setMode("getemplist");
        mav = new ModelAndView("/parCpromotion/DetailReportOfPARCemplist", "groupCEmployee", groupCEmployee);
        mav.addObject("empList", empList);
        return mav;
    }

    /*This Code is use while authority wants to add Employee with date
     @ResponseBody
     @RequestMapping(value = "addgroupcEmpList.htm")
     public void addgroupcEmpList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee, HttpServletResponse response, ModelMap model) throws IOException, JSONException {
     response.setContentType("application/json");
     groupCEmployee.setReportingempId(lub.getLoginempid());
     groupCEmployee.setReportingspc(lub.getLoginspc());
     String parcfrmdt = "";
     String parctodt = "";
     if (groupCEmployee.getAssessmentTypeReporting().equals("fullPeriod")) {
     parcfrmdt = "01-APR-2022";
     parctodt = "31-MAR-2023";
     System.out.println("parcfrmdt:::" + parcfrmdt + "parctodt" + parctodt);
     } else {
     parcfrmdt = groupCEmployee.getPeriodFromReporting();
     parctodt = groupCEmployee.getPeriodToReporting();
     System.out.println("parcfrmdt:" + parcfrmdt + "parctodt:" + parctodt);
     }
     JSONObject obj = new JSONObject();
     boolean isDuplicatePeriod = parCPromotionDAO.isDuplicatePARCPeriod(groupCEmployee.getReviewedempId(), parcfrmdt, parctodt, groupCEmployee.getFiscalyear(), groupCEmployee.getHidpromotionId());
     System.out.println("isDuplicatePeriod" + isDuplicatePeriod);
     if (isDuplicatePeriod == false) {
     parCPromotionDAO.savegroupCEmpList(groupCEmployee);
     obj.append("msg", "Saved Successfully");
     } else {
     obj.append("msg", "Duplicate Period");
     }

     PrintWriter out = response.getWriter();
     out.write(obj.toString());
     out.flush();
     } */
    /*This Code is use while authority wants to add Employee with date*/
    @ResponseBody
    @RequestMapping(value = "addgroupcEmpList.htm")
    public void addgroupcEmpList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee, HttpServletResponse response, ModelMap model) throws IOException, JSONException {
        response.setContentType("application/json");
        groupCEmployee.setReportingempId(lub.getLoginempid());
        groupCEmployee.setReportingspc(lub.getLoginspc());
        parCPromotionDAO.savegroupCEmpList(groupCEmployee);
        JSONObject obj = new JSONObject();
        obj.append("msg", "Y");
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();
    }

    /*This Code is use while authority choose remarks fitforpromotion fit for promotion*/
    @ResponseBody
    @RequestMapping(value = "fitforpromotion.htm")
    public void fitforpromotion(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee, HttpServletResponse response, ModelMap model) throws IOException, JSONException {
        response.setContentType("application/json");
        groupCEmployee.setReportingempId(lub.getLoginempid());
        groupCEmployee.setReportingspc(lub.getLoginspc());
        groupCEmployee.setIsfitforShoulderingResponsibilityReporting("Y");
        parCPromotionDAO.updateGroupCpromotionRemark(groupCEmployee);
        JSONObject obj = new JSONObject();
        obj.append("msg", "Y");
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();
    }

    /*This code is use while any Authority choose on Not Fit For Promotion button*/
    @RequestMapping(value = "remarkOfNotFitForPromotionReport.htm", method = RequestMethod.GET)
    public ModelAndView remarkOfNotFitForPromotionReportReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/parCpromotion/NotFitForPromotionRemark");
        return mv;
    }

    /*This code is use while any Authority choose on Not Fit For Promotion button and give remarks and click on save button*/
    @RequestMapping(value = "remarkOfNotFitForPromotionReport", method = {RequestMethod.POST}, params = {"action=Save"})
    public ModelAndView savegroupCRemarks(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mv = new ModelAndView();
        groupCEmployee.setReportingempId(lub.getLoginempid());
        groupCEmployee.setReportingspc(lub.getLoginspc());
        groupCEmployee.setAcceptingempId(lub.getLoginempid());
        groupCEmployee.setAcceptingspc(lub.getLoginspc());
        groupCEmployee.setReviewingempId(lub.getLoginempid());
        groupCEmployee.setReviewingspc(lub.getLoginspc());
        GroupCInitiatedbean groupCInitiatedbean = parCPromotionDAO.getAuthorityDetail(groupCEmployee.getGroupCpromotionId());
        mv.addObject("groupCInitiatedbean", groupCInitiatedbean);
        if (groupCEmployee.getRemarkauthoritytype().equals("REVIEWING")) {
            if (groupCEmployee.getIsfitforShoulderingResponsibilityReviewing().equals("Y")) {
                parCPromotionDAO.reviewingRemarkFitForPromotion(groupCEmployee);
                mv.setViewName("/parCpromotion/ReportingForwardedreportOfGroupCemp");
            } else if (groupCEmployee.getIsfitforShoulderingResponsibilityReviewing().equals("N")) {
                parCPromotionDAO.reviewingRemarkNotFitForPromotion(groupCEmployee);
                mv.setViewName("/parCpromotion/ReportingForwardedreportOfGroupCemp");

            }

        } else if (groupCEmployee.getRemarkauthoritytype().equals("ACCEPTING")) {
            if (groupCEmployee.getIsfitforShoulderingResponsibilityAccepting().equals("Y")) {
                parCPromotionDAO.acceptingRemarkFitForPromotion(groupCEmployee);
                mv.setViewName("/parCpromotion/ReviewingForwardedreportOfGroupCemp");
            } else if (groupCEmployee.getIsfitforShoulderingResponsibilityAccepting().equals("N")) {
                parCPromotionDAO.acceptingRemarkNotFitForPromotion(groupCEmployee);
                mv.setViewName("/parCpromotion/ReviewingForwardedreportOfGroupCemp");
            }
        } else {
            parCPromotionDAO.saveremarksForGroupC(groupCEmployee);
            mv.setViewName("/parCpromotion/DetailReportOfPARCemplist");
        }
        ArrayList empList = parCPromotionDAO.getSelectedgroupCEmpList(groupCEmployee.getGroupCpromotionId());
        boolean isReviewingGivesAllRemark = parCPromotionDAO.isReviewingGivesAllRemark(lub.getLoginoffcode(), groupCEmployee.getGroupCpromotionId());
        boolean isAcceptingGivesAllRemark = parCPromotionDAO.isAcceptingGivesAllRemark(lub.getLoginoffcode(), groupCEmployee.getGroupCpromotionId());
        boolean isReortingSaveAllDate = parCPromotionDAO.isReortingSaveAllDate(lub.getLoginoffcode(), groupCEmployee.getGroupCpromotionId());
        mv.addObject("isReviewingGivesAllRemark", isReviewingGivesAllRemark);
        mv.addObject("isAcceptingGivesAllRemark", isAcceptingGivesAllRemark);
        mv.addObject("isReortingSaveAllDate", isReortingSaveAllDate);
        mv.addObject("empList", empList);

        return mv;
    }

    /*This code is used while any of the Authority wants to go back to Not Fit For promotion option*/
    @RequestMapping(value = "remarkOfNotFitForPromotionReport", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView backgroupCRemarks(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mv = new ModelAndView();
        ArrayList empList = parCPromotionDAO.getSelectedgroupCEmpList(groupCEmployee.getGroupCpromotionId());
        mv.addObject("empList", empList);

        if (groupCEmployee.getRemarkauthoritytype().equals("REVIEWING")) {
            boolean isReviewingGivesAllRemark = parCPromotionDAO.isReviewingGivesAllRemark(lub.getLoginoffcode(), groupCEmployee.getGroupCpromotionId());
            mv.addObject("isReviewingGivesAllRemark", isReviewingGivesAllRemark);
            mv.setViewName("/parCpromotion/ReportingForwardedreportOfGroupCemp");
        } else if (groupCEmployee.getRemarkauthoritytype().equals("ACCEPTING")) {
            boolean isAcceptingGivesAllRemark = parCPromotionDAO.isAcceptingGivesAllRemark(lub.getLoginoffcode(), groupCEmployee.getGroupCpromotionId());
            mv.addObject("isAcceptingGivesAllRemark", isAcceptingGivesAllRemark);
            mv.setViewName("/parCpromotion/ReviewingForwardedreportOfGroupCemp");
        } else {
            mv.setViewName("/parCpromotion/DetailReportOfPARCemplist");
        }

        return mv;
    }

    /*This code is used while the Authority wants to Edit the Remark*/
    @RequestMapping(value = "editremarkOfNotFitForPromotionReport", method = {RequestMethod.GET})
    public ModelAndView editremarkOfNotFitForPromotionReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mav = new ModelAndView();

        groupCEmployee.setReviewingempId(lub.getLoginempid());
        groupCEmployee.setReviewingspc(lub.getLoginspc());
        groupCEmployee.setAcceptingempId(lub.getLoginempid());
        groupCEmployee.setAcceptingspc(lub.getLoginspc());
        GroupCEmployee groupCEmployeedetail = parCPromotionDAO.getremarksForGroupC(groupCEmployee);
        mav.addObject("groupCEmployee", groupCEmployeedetail);
        mav.setViewName("/parCpromotion/NotFitForPromotionRemark");
        return mav;
    }

    /*This code is used while the Authority wants to delete the Remark of Not Fit For Promotion*/
    @RequestMapping(value = "deleteremarkOfNotFitForPromotionReport", method = {RequestMethod.GET})
    public ModelAndView deleteremarkOfNotFitForPromotionReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mav = new ModelAndView();
        parCPromotionDAO.deleteremarksForGroupC(groupCEmployee);
        ArrayList empList = parCPromotionDAO.getSelectedgroupCEmpList(groupCEmployee.getGroupCpromotionId());
        mav.addObject("empList", empList);
        mav.setViewName("/parCpromotion/DetailReportOfPARCemplist");
        return mav;
    }

    /*This code is used while the Authority wants to Remove any user for reviewed while he/she choose that wrongly*/
    @RequestMapping(value = "removeselectedEmployeeDetail.htm", method = RequestMethod.GET)
    public ModelAndView removeselectedEmployeeDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mv = new ModelAndView();
        groupCEmployee.setPromotionId(groupCEmployee.getPromotionId());
        parCPromotionDAO.deleteselectedEmployeeList(groupCEmployee);
        ArrayList empList = parCPromotionDAO.getSelectedgroupCEmpList(groupCEmployee.getGroupCpromotionId());
        mv.addObject("empList", empList);
        mv.setViewName("/parCpromotion/DetailReportOfPARCemplist");
        return mv;
    }

    /*This code is call in the vtask List while any of the 3 Authority sends there Remarks*/
    @RequestMapping(value = "ReportingForwardedreportOfGroupCemp.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView forwardNotFitForPromotionDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("taskId") int taskId, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mv = new ModelAndView();
        TaskListHelperBean taskListHelperBean = taskDAO.getTaskDetails(taskId);
        int groupcpromotionid = parCPromotionDAO.getGroupCpromotionId(taskId);
        groupCEmployee.setGroupCpromotionId(groupcpromotionid);
        ArrayList empList = parCPromotionDAO.getSelectedgroupCEmpList(groupcpromotionid);
        GroupCInitiatedbean groupCInitiatedbean = parCPromotionDAO.getAuthorityDetail(groupcpromotionid);
        //GroupCInitiatedbean groupCInitiatedbean = parCPromotionDAO.getAuthorityDetail(groupcpromotionid);
        ArrayList promotionReportList = parCPromotionDAO.getPromotionReportList(lub.getLoginempid());
        mv.addObject("empList", empList);
        mv.addObject("groupCInitiatedbean", groupCInitiatedbean);
        mv.addObject("promotionReportList", promotionReportList);
        //mv.addObject("groupCInitiatedbean", groupCInitiatedbean);
        if (taskListHelperBean.getStatusId() == 91) {
            groupCEmployee.setRemarkauthoritytype("ACCEPTING");
            boolean isAcceptingGivesAllRemark = parCPromotionDAO.isAcceptingGivesAllRemark(lub.getLoginoffcode(), groupCEmployee.getGroupCpromotionId());
            mv.addObject("isAcceptingGivesAllRemark", isAcceptingGivesAllRemark);
            mv.setViewName("/parCpromotion/ReviewingForwardedreportOfGroupCemp");
        } else if (taskListHelperBean.getStatusId() == 84) {
            groupCEmployee.setRemarkauthoritytype("REVIEWING");
            boolean isReviewingGivesAllRemark = parCPromotionDAO.isReviewingGivesAllRemark(lub.getLoginoffcode(), groupCEmployee.getGroupCpromotionId());
            //GroupCInitiatedbean groupCInitiatedbean = parCPromotionDAO.getAuthorityDetail(groupcpromotionid);
            mv.addObject("isReviewingGivesAllRemark", isReviewingGivesAllRemark);
            //mv.addObject("groupCInitiatedbean", groupCInitiatedbean);
            mv.setViewName("/parCpromotion/ReportingForwardedreportOfGroupCemp");
        } else if (taskListHelperBean.getStatusId() == 93) {
            mv.setViewName("/parCpromotion/appraiseeRemarksDetail");

        } else if (taskListHelperBean.getStatusId() == 115) {
            groupCEmployee.setRemarkauthoritytype("REVIEWING");
            String statusId = parCPromotionDAO.getstatusId(groupCEmployee.getGroupCpromotionId());
            int taskid = parCPromotionDAO.gettaskId(groupCEmployee.getGroupCpromotionId());
            String[] revertreason = parCPromotionDAO.getRevertReasonOfGroupCPAR(groupCEmployee.getGroupCpromotionId(), taskid);

            mv.addObject("statusId", statusId);
            mv.addObject("authorityType", revertreason[0]);
            mv.addObject("revertRemaeks", revertreason[1]);
            mv.addObject("authorityName", revertreason[2]);
            mv.setViewName("/parCpromotion/ReportingForwardedreportOfGroupCemp");
        }

        return mv;
    }
    /*This code is used to view all detail after final completion of the process*/

    @RequestMapping(value = "ViewSelectedEmpDetail.htm")
    public ModelAndView ViewSelectedEmpDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mav = new ModelAndView();
        ArrayList empList = parCPromotionDAO.getSelectedgroupCEmpList(groupCEmployee.getGroupCpromotionId());
        String isPendingAtEmpId = parCPromotionDAO.getisPendingAtEmpId(groupCEmployee.getGroupCpromotionId());
        String statusId = parCPromotionDAO.getstatusId(groupCEmployee.getGroupCpromotionId());
        int taskId = parCPromotionDAO.gettaskIdFromGroupCPromotionId(groupCEmployee.getGroupCpromotionId());
        boolean isReortingSaveAllDate = parCPromotionDAO.isReortingSaveAllDate(lub.getLoginoffcode(), groupCEmployee.getGroupCpromotionId());
        groupCEmployee.setMode("getemplist");
        mav = new ModelAndView("/parCpromotion/DetailReportOfPARCemplist", "groupCEmployee", groupCEmployee);
        mav.addObject("empList", empList);
        mav.addObject("isPendingAtEmpId", isPendingAtEmpId);
        mav.addObject("statusId", statusId);
        mav.addObject("taskId", taskId);
        mav.addObject("isReortingSaveAllDate", isReortingSaveAllDate);
        return mav;
    }

    @RequestMapping(value = "ViewSelectedEmpDetail.htm", method = {RequestMethod.POST}, params = {"action=Download"})
    public ModelAndView downloadGroupStatementReportForReportingPdf(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mv = new ModelAndView();
        ArrayList empList = parCPromotionDAO.getSelectedgroupCEmpList(groupCEmployee.getGroupCpromotionId());
        //String isPendingAtEmpId = parCPromotionDAO.getisPendingAtEmpId(groupCEmployee.getGroupCpromotionId());
        //groupCEmployee.setMode("getemplist");
        //mv = new ModelAndView("/parCpromotion/DetailReportOfPARCemplist", "groupCEmployee", groupCEmployee);
        mv.addObject("empList", empList);
        //mv.addObject("isPendingAtEmpId", isPendingAtEmpId);
        mv.setViewName("groupCEmployeeForReportingPdf");
        return mv;
    }

    @RequestMapping(value = "viewgroupCPromotionReport.htm")
    public ModelAndView viewgroupCPromotionReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mav = new ModelAndView();
        ArrayList empList = parCPromotionDAO.getSelectedgroupCEmpList(groupCEmployee.getGroupCpromotionId());
        groupCEmployee.setMode("getemplist");
        mav = new ModelAndView("/parCpromotion/ViewReportOfGroupCEmployee", "groupCEmployee", groupCEmployee);
        mav.addObject("empList", empList);
        return mav;
    }

    @RequestMapping(value = "remarkOfNotFitForPromotionReviewingReport.htm", method = RequestMethod.GET)
    public ModelAndView remarkOfNotFitForPromotionReviewingReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mv = new ModelAndView();
        groupCEmployee.setIsfitforShoulderingResponsibilityReviewing("N");
        groupCEmployee.setRemarkauthoritytype("REVIEWING");
        groupCEmployee.setReportingempId(lub.getLoginempid());
        groupCEmployee.setReportingspc(lub.getLoginspc());
        groupCEmployee.setAcceptingempId(lub.getLoginempid());
        groupCEmployee.setAcceptingspc(lub.getLoginspc());
        groupCEmployee.setReviewingempId(lub.getLoginempid());
        groupCEmployee.setReviewingspc(lub.getLoginspc());
        boolean isRemarkCompulsory = parCPromotionDAO.isRemarksCompulsoryReviewing(groupCEmployee.getGroupCpromotionId(), "N");

        if (isRemarkCompulsory) {
            mv.setViewName("/parCpromotion/NotFitForPromotionRemark");
        } else {
            parCPromotionDAO.reviewingRemarkFitForPromotion(groupCEmployee);
            ArrayList empList = parCPromotionDAO.getSelectedgroupCEmpList(groupCEmployee.getGroupCpromotionId());
            GroupCInitiatedbean groupCInitiatedbean = parCPromotionDAO.getAuthorityDetail(groupCEmployee.getGroupCpromotionId());
            boolean isReviewingGivesAllRemark = parCPromotionDAO.isReviewingGivesAllRemark(lub.getLoginoffcode(), groupCEmployee.getGroupCpromotionId());
            boolean isAcceptingGivesAllRemark = parCPromotionDAO.isAcceptingGivesAllRemark(lub.getLoginoffcode(), groupCEmployee.getGroupCpromotionId());
            mv.addObject("isReviewingGivesAllRemark", isReviewingGivesAllRemark);
            mv.addObject("isAcceptingGivesAllRemark", isAcceptingGivesAllRemark);
            mv.addObject("empList", empList);
            mv.addObject("groupCInitiatedbean", groupCInitiatedbean);
            mv.setViewName("/parCpromotion/ReportingForwardedreportOfGroupCemp");
        }

        mv.setViewName("/parCpromotion/NotFitForPromotionRemark");
        return mv;
    }


    /*This code is used while the Reviewing Authority Click on fit for promotion Button*/
    @RequestMapping(value = "remarkOfFitForPromotionReviewingReport.htm", method = RequestMethod.GET)
    public ModelAndView remarkOfFitForPromotionReviewingReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mv = new ModelAndView();
        groupCEmployee.setIsfitforShoulderingResponsibilityReviewing("Y");
        groupCEmployee.setRemarkauthoritytype("REVIEWING");
        groupCEmployee.setReportingempId(lub.getLoginempid());
        groupCEmployee.setReportingspc(lub.getLoginspc());
        groupCEmployee.setAcceptingempId(lub.getLoginempid());
        groupCEmployee.setAcceptingspc(lub.getLoginspc());
        groupCEmployee.setReviewingempId(lub.getLoginempid());
        groupCEmployee.setReviewingspc(lub.getLoginspc());
        boolean isRemarkCompulsory = parCPromotionDAO.isRemarksCompulsoryReviewing(groupCEmployee.getPromotionId(), "Y");
        if (isRemarkCompulsory) {
            mv.setViewName("/parCpromotion/NotFitForPromotionRemark");
        } else {
            parCPromotionDAO.reviewingRemarkFitForPromotion(groupCEmployee);
            ArrayList empList = parCPromotionDAO.getSelectedgroupCEmpList(groupCEmployee.getGroupCpromotionId());
            GroupCInitiatedbean groupCInitiatedbean = parCPromotionDAO.getAuthorityDetail(groupCEmployee.getGroupCpromotionId());
            boolean isReviewingGivesAllRemark = parCPromotionDAO.isReviewingGivesAllRemark(lub.getLoginoffcode(), groupCEmployee.getGroupCpromotionId());
            boolean isAcceptingGivesAllRemark = parCPromotionDAO.isAcceptingGivesAllRemark(lub.getLoginoffcode(), groupCEmployee.getGroupCpromotionId());
            mv.addObject("isReviewingGivesAllRemark", isReviewingGivesAllRemark);
            mv.addObject("isAcceptingGivesAllRemark", isAcceptingGivesAllRemark);
            mv.addObject("empList", empList);
            mv.addObject("groupCInitiatedbean", groupCInitiatedbean);
            mv.setViewName("/parCpromotion/ReportingForwardedreportOfGroupCemp");
        }

        return mv;
    }

    @RequestMapping(value = "reviewingRemarkFitForPromotion", method = {RequestMethod.GET})
    public ModelAndView reviewingRemarkFitForPromotion(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mav = new ModelAndView();
        groupCEmployee.setReviewingempId(lub.getLoginempid());
        groupCEmployee.setReviewingspc(lub.getLoginspc());
        parCPromotionDAO.reviewingRemarkFitForPromotion(groupCEmployee);
        ArrayList empList = parCPromotionDAO.getSelectedgroupCEmpList(groupCEmployee.getGroupCpromotionId());
        GroupCInitiatedbean groupCInitiatedbean = parCPromotionDAO.getAuthorityDetail(groupCEmployee.getGroupCpromotionId());
        mav.addObject("empList", empList);
        mav.addObject("groupCInitiatedbean", groupCInitiatedbean);
        mav.setViewName("/parCpromotion/ReportingForwardedreportOfGroupCemp");
        return mav;
    }

    /*This code is used while the Accepting Authority Click on Not fit for promotion Button*/
    @RequestMapping(value = "remarkOfNotFitForPromotionAcceptingReport.htm", method = RequestMethod.GET)
    public ModelAndView remarkOfNotFitForPromotionAcceptingReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mv = new ModelAndView();
        groupCEmployee.setIsfitforShoulderingResponsibilityAccepting("N");
        groupCEmployee.setRemarkauthoritytype("ACCEPTING");
        groupCEmployee.setReportingempId(lub.getLoginempid());
        groupCEmployee.setReportingspc(lub.getLoginspc());
        groupCEmployee.setAcceptingempId(lub.getLoginempid());
        groupCEmployee.setAcceptingspc(lub.getLoginspc());
        groupCEmployee.setReviewingempId(lub.getLoginempid());
        groupCEmployee.setReviewingspc(lub.getLoginspc());
        boolean isRemarkCompulsoryAccepting = parCPromotionDAO.isRemarksCompulsoryAccepting(groupCEmployee.getPromotionId(), "N");
        if (isRemarkCompulsoryAccepting) {
            mv.setViewName("/parCpromotion/NotFitForPromotionRemark");
        } else {
            parCPromotionDAO.acceptingRemarkFitForPromotion(groupCEmployee);
            ArrayList empList = parCPromotionDAO.getSelectedgroupCEmpList(groupCEmployee.getGroupCpromotionId());
            GroupCInitiatedbean groupCInitiatedbean = parCPromotionDAO.getAuthorityDetail(groupCEmployee.getGroupCpromotionId());
            boolean isReviewingGivesAllRemark = parCPromotionDAO.isReviewingGivesAllRemark(lub.getLoginoffcode(), groupCEmployee.getGroupCpromotionId());
            boolean isAcceptingGivesAllRemark = parCPromotionDAO.isAcceptingGivesAllRemark(lub.getLoginoffcode(), groupCEmployee.getGroupCpromotionId());
            mv.addObject("isReviewingGivesAllRemark", isReviewingGivesAllRemark);
            mv.addObject("isAcceptingGivesAllRemark", isAcceptingGivesAllRemark);
            mv.addObject("empList", empList);
            mv.addObject("groupCInitiatedbean", groupCInitiatedbean);
            mv.setViewName("/parCpromotion/ReviewingForwardedreportOfGroupCemp");
            //mv.setViewName("/parCpromotion/NotFitForPromotionRemark");
        }
        return mv;
    }

    /*This code is used while the Accepting Authority Click on Not fit for promotion Button*/
    @RequestMapping(value = "remarkOfFitForPromotionAcceptingReport.htm", method = RequestMethod.GET)
    public ModelAndView remarkOfFitForPromotionAcceptingReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mv = new ModelAndView();
        groupCEmployee.setIsfitforShoulderingResponsibilityAccepting("Y");
        groupCEmployee.setRemarkauthoritytype("ACCEPTING");
        groupCEmployee.setReportingempId(lub.getLoginempid());
        groupCEmployee.setReportingspc(lub.getLoginspc());
        groupCEmployee.setAcceptingempId(lub.getLoginempid());
        groupCEmployee.setAcceptingspc(lub.getLoginspc());
        groupCEmployee.setReviewingempId(lub.getLoginempid());
        groupCEmployee.setReviewingspc(lub.getLoginspc());
        boolean isRemarkCompulsoryAccepting = parCPromotionDAO.isRemarksCompulsoryAccepting(groupCEmployee.getPromotionId(), "Y");
        if (isRemarkCompulsoryAccepting) {
            mv.setViewName("/parCpromotion/NotFitForPromotionRemark");
        } else {
            parCPromotionDAO.acceptingRemarkFitForPromotion(groupCEmployee);
            ArrayList empList = parCPromotionDAO.getSelectedgroupCEmpList(groupCEmployee.getGroupCpromotionId());
            GroupCInitiatedbean groupCInitiatedbean = parCPromotionDAO.getAuthorityDetail(groupCEmployee.getGroupCpromotionId());
            boolean isReviewingGivesAllRemark = parCPromotionDAO.isReviewingGivesAllRemark(lub.getLoginoffcode(), groupCEmployee.getGroupCpromotionId());
            boolean isAcceptingGivesAllRemark = parCPromotionDAO.isAcceptingGivesAllRemark(lub.getLoginoffcode(), groupCEmployee.getGroupCpromotionId());
            mv.addObject("isReviewingGivesAllRemark", isReviewingGivesAllRemark);
            mv.addObject("isAcceptingGivesAllRemark", isAcceptingGivesAllRemark);
            mv.addObject("empList", empList);
            mv.addObject("groupCInitiatedbean", groupCInitiatedbean);
            mv.setViewName("/parCpromotion/ReviewingForwardedreportOfGroupCemp");
        }
        return mv;
    }

    @RequestMapping(value = "acceptingRemarkFitForPromotion", method = {RequestMethod.GET})
    public ModelAndView acceptingRemarkFitForPromotion(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mav = new ModelAndView();
        groupCEmployee.setAcceptingempId(lub.getLoginempid());
        groupCEmployee.setAcceptingspc(lub.getLoginspc());
        parCPromotionDAO.acceptingRemarkFitForPromotion(groupCEmployee);
        ArrayList empList = parCPromotionDAO.getSelectedgroupCEmpList(groupCEmployee.getGroupCpromotionId());
        GroupCInitiatedbean groupCInitiatedbean = parCPromotionDAO.getAuthorityDetail(groupCEmployee.getGroupCpromotionId());
        mav.addObject("empList", empList);
        mav.addObject("groupCInitiatedbean", groupCInitiatedbean);
        mav.setViewName("/parCpromotion/ReviewingForwardedreportOfGroupCemp");
        return mav;
    }

    /*This code is Custodian click on group c Custodian */
    @RequestMapping(value = "groupCCustdianReport.htm", method = RequestMethod.GET)
    public ModelAndView groupCCustdianReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee, @ModelAttribute("groupCCustodianCommunication") GroupCCustodianCommunication groupCCustodianCommunication) {
        ModelAndView mav = new ModelAndView();
        //ArrayList custdianDetail = parAdminDAO.getgroupCCustdianDetail();
        List fiscyear = fiscalDAO.getFiscalYearList();
        //mav.addObject("custdianDetail", custdianDetail);
        mav.addObject("fiscyear", fiscyear);
        mav.setViewName("/parCpromotion/GroupCCustdianReport");
        return mav;
    }

    @RequestMapping(value = "groupCCustdianReport.htm", method = RequestMethod.POST, params = {"action=Download"})
    public ModelAndView groupCCustdianReportPDF(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee, @ModelAttribute("groupCCustodianCommunication") GroupCCustodianCommunication groupCCustodianCommunication) {
        ModelAndView mv = new ModelAndView();
        ParAssignPrivilage pap = parCPromotionDAO.getAssignPrivilage(lub.getLoginspc());
        ArrayList groupCEmpList = null;
        pap.setDistCode(groupCEmployee.getDistCode());
        pap.setOffCode(groupCEmployee.getOffcode());
        if (pap != null) {
            groupCEmpList = parCPromotionDAO.getgroupCCustdianDetail(pap, groupCEmployee.getFiscalyear());
            mv.addObject("groupCEmpList", groupCEmpList);

        }
        //ArrayList acceptingremarksdetail = parCPromotionDAO.getAcceptingFinalRemarks(groupCEmployee.getFiscalyear());
        //mv.addObject("acceptingremarksdetail", acceptingremarksdetail);
        mv.setViewName("groupCCustodianReportPdfView");
        return mv;
    }

    @RequestMapping(value = "groupCCustdianReport.htm", method = RequestMethod.POST, params = {"action=Back"})
    public ModelAndView backgroupCCustdianReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee, @ModelAttribute("groupCCustodianCommunication") GroupCCustodianCommunication groupCCustodianCommunication) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:groupCCustdianReport.htm");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "getSearchEmpListForGroupC.htm", method = RequestMethod.POST)
    public void getSearchEmpListForGroupC(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            HttpServletResponse response, @RequestParam("fiscalyear") String fiscalyear,
            @RequestParam("distCode") String distCode, @RequestParam("offCode") String offCode) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ParAssignPrivilage pap = parCPromotionDAO.getAssignPrivilage(lub.getLoginspc());
        pap.setDistCode(distCode);
        pap.setOffCode(offCode);
        ArrayList groupCEmpList = null;
        if (pap != null) {
            groupCEmpList = parCPromotionDAO.getgroupCCustdianDetail(pap, fiscalyear);

        }

        JSONObject obj = new JSONObject();
        obj.append("groupCEmpList", groupCEmpList);
        out = response.getWriter();
        out.write(obj.toString());
    }

    //@RequestMapping(value = "groupCCustdianReport.htm", params = {"action=List For Fit For Promotion and Not Fit For Promotion"}, method = RequestMethod.POST)
    @RequestMapping(value = "groupCCustdianReportForFitForPromotionandNotFitForPromotion.htm", method = RequestMethod.GET)
    public ModelAndView groupCCustdianReportForFitForPromotionandNotFitForPromotion(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mav = new ModelAndView();
        ParAssignPrivilage pap = parCPromotionDAO.getAssignPrivilage(lub.getLoginspc());
        ArrayList groupCEmpList = null;
        if (pap != null) {
            groupCEmpList = parCPromotionDAO.getgroupCCustdianDetail(pap, groupCEmployee.getFiscalyear());
            mav.addObject("groupCEmpList", groupCEmpList);
        }
        //ArrayList acceptingremarksdetail = parCPromotionDAO.getAcceptingFinalRemarks(groupCEmployee.getFiscalyear());
        //mav.addObject("acceptingremarksdetail", acceptingremarksdetail);
        mav.setViewName("/parCpromotion/CustodianFinalReportForFitForPromotionandNotFitForPromotion");
        return mav;
    }

    /*This code is used while the Custodian click on Communication Button of groupCCustdianReport*/
    @RequestMapping(value = "groupCCustdianCommunicationDetail.htm", method = RequestMethod.GET)
    public ModelAndView groupCCustdianCommunicationDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCCustodianCommunication") GroupCCustodianCommunication groupCCustodianCommunication, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mav = new ModelAndView();
        groupCCustodianCommunication = parCPromotionDAO.getAppraiseeForGroupC(groupCCustodianCommunication.getPromotionId());
        List custodianremarkList = parCPromotionDAO.getremarksOfCustodianForGroupC(groupCCustodianCommunication.getPromotionId());
        mav.addObject("groupCCustodianCommunication", groupCCustodianCommunication);
        mav.addObject("custodianremarkList", custodianremarkList);
        mav.setViewName("/parCpromotion/CustodianAdverseCommunicationWithAppraisee");
        return mav;
    }

    /*This code is used while the Custodian click on Communication Button of groupCCustdianReport and give there Remarks and submit that*/
    @RequestMapping(value = "groupCCustdianCommunicationDetail.htm", params = {"action=Submit"}, method = RequestMethod.POST)
    public ModelAndView savegroupCCustdianCommunicationDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCCustodianCommunication") GroupCCustodianCommunication groupCCustodianCommunication) {
        ModelAndView mav = new ModelAndView();
        if (groupCCustodianCommunication.getTaskId() == 0) {
            groupCCustodianCommunication.setFromempId(lub.getLoginempid());
            groupCCustodianCommunication.setFromspc(lub.getLoginspc());
        } else {
            TaskListHelperBean taskListHelperBean = taskDAO.getTaskDetails(groupCCustodianCommunication.getTaskId());
            groupCCustodianCommunication.setToempId(taskListHelperBean.getAppEmpCode());
            groupCCustodianCommunication.setTospc(taskListHelperBean.getAppSpc());
            groupCCustodianCommunication.setFromempId(lub.getLoginempid());
            groupCCustodianCommunication.setFromspc(lub.getLoginspc());
        }

        parCPromotionDAO.savecustodianremarksForGroupC(groupCCustodianCommunication);
        List custodianremarkList = parCPromotionDAO.getremarksOfCustodianForGroupC(groupCCustodianCommunication.getPromotionId());
        //groupCCustodianCommunication = parAdminDAO.getAppraiseeForGroupC(groupCCustodianCommunication.getPromotionId());
        mav.addObject("custodianremarkList", custodianremarkList);
        //  mav.addObject("groupCCustodianCommunication", groupCCustodianCommunication);
        if (groupCCustodianCommunication.getTaskId() == 0) {

            mav.setViewName("/parCpromotion/GroupCCustodianCommunicationDetail");
        } else {
            mav.setViewName("/parCpromotion/AuthorityRemarkSuccess");
        }
        return mav;
    }

    /*This code is used while the Custodian wants to go back to the  groupCCustdianReport page*/
    @RequestMapping(value = "groupCCustdianCommunicationDetail.htm", params = {"action=Back"}, method = RequestMethod.POST)
    public ModelAndView backgroupCCustdianCommunicationDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCCustodianCommunication") GroupCCustodianCommunication groupCCustodianCommunication, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mav = new ModelAndView();
        ParAssignPrivilage pap = parCPromotionDAO.getAssignPrivilage(lub.getLoginspc());
        ArrayList custdianDetail = parCPromotionDAO.getgroupCCustdianDetail(pap, groupCEmployee.getFiscalyear());
        mav.addObject("custdianDetail", custdianDetail);
        mav.setViewName("/parCpromotion/GroupCCustdianReport");
        return mav;
    }

    /*This code is used while the Custodian Click on Submit Button on Remarks and while that is insert in to the task List*/
    @RequestMapping(value = "CustodianForwardedreportOfGroupCemp.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView CustodianForwardedreportOfGroupCemp(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCCustodianCommunication") GroupCCustodianCommunication groupCCustodianCommunication) {
        ModelAndView mv = new ModelAndView();
        TaskListHelperBean taskListHelperBean = taskDAO.getTaskDetails(groupCCustodianCommunication.getTaskId());
        List custodianremarkList = parCPromotionDAO.getremarksOfCustodianForGroupC(taskListHelperBean.getReferenceId());
        groupCCustodianCommunication.setPromotionId(taskListHelperBean.getReferenceId());
        mv.addObject("custodianremarkList", custodianremarkList);
        mv.addObject("taskListHelperBean", taskListHelperBean);
        if (taskListHelperBean.getStatusId() == 96) {
            mv.setViewName("/parCpromotion/GroupCCustodianCommunicationDetail");
        }
        return mv;
    }

    @RequestMapping(value = "downloadAttachmentOfGroupC")
    public void downloadAttachmentOfGroupC(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCCustodianCommunication") GroupCCustodianCommunication groupCCustodianCommunication, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        groupCCustodianCommunication = parCPromotionDAO.getAttachedFile(groupCCustodianCommunication.getCommunicationId());
        response.setContentType(groupCCustodianCommunication.getGetContentType());
        response.setHeader("Content-Disposition", "attachment;filename=" + groupCCustodianCommunication.getOriginalFilename());
        OutputStream out = response.getOutputStream();
        out.write(groupCCustodianCommunication.getFilecontent());
        out.flush();
    }

    @RequestMapping(value = "downloadAttachmentOfGroupCForNotFit")
    public void downloadAttachmentOfGroupCForNotFit(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        groupCEmployee = parCPromotionDAO.getAttachedFileforNotFit(groupCEmployee.getPromotionId());
        response.setContentType(groupCEmployee.getGetContentType());
        response.setHeader("Content-Disposition", "attachment;filename=" + groupCEmployee.getOriginalFilename());
        OutputStream out = response.getOutputStream();
        out.write(groupCEmployee.getFilecontent());
        out.flush();
    }

    @RequestMapping(value = "saveFromAndToDateForReporting")
    public void saveFromAndToDateForReportingg(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee, HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView();
        parCPromotionDAO.updateFromAndToDateForReporting(groupCEmployee);
        ArrayList empList = parCPromotionDAO.getSelectedgroupCEmpList(groupCEmployee.getGroupCpromotionId());
        groupCEmployee.setMode("getemplist");
        mav = new ModelAndView("/parCpromotion/DetailReportOfPARCemplist", "groupCEmployee", groupCEmployee);
        mav.addObject("empList", empList);
        mav.setViewName("redirect:ViewSelectedEmpDetail.htm");
    }

    /*This Code is used to update From And To Date For Reporting Group C Employee Report*/
    /* @RequestMapping(value = "parCPromotionReport.htm", method = RequestMethod.POST, params = {"action=Save"})
     public ModelAndView saveFromAndToDateForReporting(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCInitiatedbean") GroupCInitiatedbean groupCInitiatedbean) {
     ModelAndView mav = new ModelAndView();
     parCPromotionDAO.updateFromAndToDateForReporting(groupCInitiatedbean);
     System.out.println("************");
     mav.setViewName("redirect:ViewSelectedEmpDetail.htm");
     return mav;
     }*/
    @RequestMapping(value = "GroupCEmployeeForReportingPDF.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView GroupCEmployeeForReportingPDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mv = new ModelAndView();
        ArrayList empList = parCPromotionDAO.getSelectedgroupCEmpList(groupCEmployee.getGroupCpromotionId());
        //GroupCInitiatedbean groupCInitiatedbean = parCPromotionDAO.getReportingAuthorityDetail();
        // String isPendingAtEmpId = parCPromotionDAO.getisPendingAtEmpId(groupCEmployee.getGroupCpromotionId());
        mv.addObject("empList", empList);
        //mv.addObject("groupCEmployee", groupCEmployee);
        mv.setViewName("groupCEmployeeForReportingPdf");
        return mv;
    }

    @RequestMapping(value = "GroupCEmployeeForAppraiseePDF.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView GroupCEmployeeForAppraiseePdf(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mv = new ModelAndView();
        ArrayList empList = parCPromotionDAO.getSelectedgroupCEmpList(groupCEmployee.getGroupCpromotionId());
        //GroupCInitiatedbean groupCInitiatedbean = parCPromotionDAO.getReportingAuthorityDetail();
        // String isPendingAtEmpId = parCPromotionDAO.getisPendingAtEmpId(groupCEmployee.getGroupCpromotionId());
        mv.addObject("empList", empList);
        //mv.addObject("groupCEmployee", groupCEmployee);
        mv.setViewName("groupCEmployeeForAppraiseePdf");
        return mv;
    }

    @RequestMapping(value = "RevertReasonForGroupCPAR", method = RequestMethod.GET)
    public ModelAndView RevertReason(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("groupcPromotionid") int groupcPromotionid) {
        ModelAndView mav = null;
        try {
            mav = new ModelAndView();
            int taskid = parCPromotionDAO.gettaskId(groupcPromotionid);
            String[] revertreason = parCPromotionDAO.getRevertReasonOfGroupCPAR(groupcPromotionid, taskid);
            mav.addObject("authorityType", revertreason[0]);
            mav.addObject("revertRemaeks", revertreason[1]);
            mav.addObject("authorityName", revertreason[2]);
            mav.setViewName("parCpromotion/RevertReasonForGroupCPAR");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }

    @RequestMapping(value = "groupCPrivilizationList.htm")
    public ModelAndView groupCPrivilizationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("cadre") Cadre cadre, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee, @ModelAttribute("Privileges") Module[] privileges) {
        ModelAndView mv = new ModelAndView();
        //List cadrelist = cadreDAO.getCadreList(cadre.getDeptCode());
        //mv.addObject("cadrelist", cadrelist);
        if (CommonFunctions.hasPrivileage(privileges, "sendMessageForPARList.htm") == true) {
            mv.setViewName("/parCpromotion/ParCAuthorizationList");
            List distlist = districtDAO.getDistrictList();
            mv.addObject("departmentList", departmentDAO.getDepartmentList());
            mv.addObject("distlist", distlist);
        } else {
            mv.setViewName("/under_const");
        }
        return mv;

    }

    /*@RequestMapping(value = "groupCPrivilizationList.htm")
     public ModelAndView groupCPrivilizationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("cadre") Cadre cadre, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
     ModelAndView mv = new ModelAndView("/parCpromotion/ParCAuthorizationList");
     //List cadrelist = cadreDAO.getCadreList(cadre.getDeptCode());
     //mv.addObject("cadrelist", cadrelist);
     List distlist = districtDAO.getDistrictList();
     mv.addObject("departmentList", departmentDAO.getDepartmentList());
     mv.addObject("distlist", distlist);
     return mv;

     }*/
    @RequestMapping(value = "groupCPrivilizationList.htm", params = {"action=Get Districtwise Privilege List"}, method = RequestMethod.POST)
    public ModelAndView getOfficeWisePrivilizationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mav = new ModelAndView();
        ArrayList distOrOffwiseprivilegedList = parCPromotionDAO.getAssignPrivilegedListDistandOffWise("D");
        mav.addObject("distOrOffwiseprivilegedList", distOrOffwiseprivilegedList);
        mav.setViewName("/parCpromotion/ParCAuthorizationList");
        return mav;
    }

    @RequestMapping(value = "groupCPrivilizationList.htm", params = {"action=Get Officewise Privilege List"}, method = RequestMethod.POST)
    public ModelAndView getdistrictWisePrivilizationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mav = new ModelAndView();
        ParAssignPrivilage pap = parCPromotionDAO.getAssignPrivilage(lub.getLoginspc());
        ArrayList distOrOffwiseprivilegedList = parCPromotionDAO.getAssignPrivilegedListDistandOffWise("O");
        mav.addObject("distOrOffwiseprivilegedList", distOrOffwiseprivilegedList);
        mav.setViewName("/parCpromotion/ParCAuthorizationList");
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "assignPrivilegeForGroupC.htm")
    public void assignCadrePrivilage(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ModelAndView mv = new ModelAndView();
        String msg = parCPromotionDAO.saveAssignPrivilege(groupCEmployee);
        JSONObject obj = new JSONObject();
        obj.append("msg", msg);
        out = response.getWriter();
        out.write(obj.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getassignPrivilegeForGroupCDetail.htm")
    public void getassignPrivilegeForGroupCDetail(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList privilegedList = parCPromotionDAO.getAssignPrivilegedList(groupCEmployee.getSpc());
        JSONArray json = new JSONArray(privilegedList);
        out = response.getWriter();
        out.write(json.toString());
        out.flush();
        out.close();
    }

    @ResponseBody
    @RequestMapping(value = "removeGroupCPrivilage.htm")
    public void removeGroupCPrivilage(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        parCPromotionDAO.deleteGroupCPrivilage(groupCEmployee.getSpc());
        JSONArray json = new JSONArray();
        out = response.getWriter();
        out.write(json.toString());
        out.flush();
        out.close();
    }

    @RequestMapping(value = "removeGroupCPrivilagedistOrOffwise.htm")
    public ModelAndView removeGroupCPrivilagedistOrOffwise(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupCEmployee") GroupCEmployee groupCEmployee) {
        ModelAndView mv = new ModelAndView("/parCpromotion/ParCAuthorizationList");
        parCPromotionDAO.deleteGroupCPrivilage(groupCEmployee.getSpc());
        return mv;
    }

    @RequestMapping(value = "viewGroupCAdmin.htm", method = RequestMethod.GET)
    public ModelAndView viewGroupCAdmin(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("Privileges") Module[] privileges) {
        ModelAndView mv = new ModelAndView();
        if (CommonFunctions.hasPrivileage(privileges, "viewGroupCAdmin.htm") == true) {
            mv.setViewName("/parCpromotion/GroupCAdmin");
            List fiscyear = fiscalDAO.getFiscalYearList();
            mv.addObject("fiscyear", fiscyear);
        } else {
            mv.setViewName("/under_const");
        }

        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "getSearchGroupCDetailList.htm", method = RequestMethod.POST)
    public void getSearchGroupCDetailList(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("groupCAdminAdminSearchCriteria") GroupCAdminAdminSearchCriteria groupCAdminAdminSearchCriteria) throws IOException, JSONException {

        response.setContentType("application/json");
        PrintWriter out = null;
        GroupCSearchResult groupCEmplist = new GroupCSearchResult();
        groupCEmplist = parCPromotionDAO.getGroupCEmpList(groupCAdminAdminSearchCriteria);

        JSONObject obj = new JSONObject(groupCEmplist);
        out = response.getWriter();
        out.write(obj.toString());
    }

    @RequestMapping(value = "getGroupCStatusListJSON", method = RequestMethod.POST)
    public @ResponseBody
    void getGroupCStatusListJSON(HttpServletResponse response) {
        response.setContentType("application/json");
        JSONArray json = null;
        PrintWriter out = null;
        try {
            List groupCStatusList = parCPromotionDAO.getGroupCStatusList();
            json = new JSONArray(groupCStatusList);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "GetOfficePrivelegeListJSON", method = RequestMethod.POST)
    public @ResponseBody
    void getOfficePrivelegeListJSON(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) {
        response.setContentType("application/json");
        JSONArray json = null;
        PrintWriter out = null;
        try {
            List officeList = parCPromotionDAO.getOfficePriveligedList(lub.getLoginspc());
            json = new JSONArray(officeList);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "GetDistrictPriveligeListJSON", method = RequestMethod.POST)
    public @ResponseBody
    void getDistrictPriveligeListJSON(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) {
        response.setContentType("application/json");
        JSONArray json = null;
        PrintWriter out = null;
        try {
            List districtlist = parCPromotionDAO.getDistrictPriveligedList(lub.getLoginspc());
            json = new JSONArray(districtlist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getFromDateToDateJSON")
    public void getFromDateToDateJSON(HttpServletResponse response, @RequestParam("promotionId") int promotionId) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            GroupCEmployee groupCEmployee = parCPromotionDAO.getFromDateToDateDetail(promotionId);
            out = response.getWriter();
            JSONObject obj = new JSONObject(groupCEmployee);
            //obj.append("groupCEmployee", groupCEmployee);            
            out.write(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getFromDateToDatebyEmpIdJSON")
    public void getFromDateToDatebyEmpIdJSON(HttpServletResponse response, @RequestParam("reviewedEmpId") String reviewedEmpId, @RequestParam("fiscalYear") String fiscalYear) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            GroupCEmployee groupCEmployee = parCPromotionDAO.getFromDateToDateByEmpId(reviewedEmpId, fiscalYear);
            out = response.getWriter();
            JSONObject obj = new JSONObject(groupCEmployee);
            out.write(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

}
