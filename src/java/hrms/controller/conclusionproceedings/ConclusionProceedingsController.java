/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.conclusionproceedings;

import hrms.dao.conclusionproceedings.ConclusionProceedingsDAO;
import hrms.dao.discProceeding.DiscProceedingDAO;
import hrms.dao.fiscalyear.FiscalYearDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.LeaveTypeDAO;
import hrms.dao.tab.ServicePanelDAO;
import hrms.model.conclusionproceedings.ConclusionProceedings;
import hrms.model.conclusionproceedings.PunishmentDetails;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.redesignation.Redesignation;
import hrms.model.tab.RollwiseGroupInfoBean;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class ConclusionProceedingsController {

    @Autowired
    ConclusionProceedingsDAO conclusionProceedingsDAO;

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public ServicePanelDAO servicePanelDAO;

    @Autowired
    FiscalYearDAO fiscalDAO;

    @Autowired
    LeaveTypeDAO leaveTypeDAO;

    @RequestMapping(value = "ConclusionProceedingsList")

    public ModelAndView ConclusionProceedingsList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj) {
        ModelAndView mav = null;
        try {
            List procList = conclusionProceedingsDAO.getEmpConcProcList(selectedEmpObj.getEmpId());
            //List procList = conclusionProceedingsDAO.getEmpConcProcList(conclusionProceedingsForm.getEmpid());
            mav = new ModelAndView("/conclusionProceeding/ConclusionProceedingsList");
            mav.addObject("procList", procList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "DisciplinaryProceedingList")
    public ModelAndView DisciplinaryProceedingList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("RollwiseGroupInfoBean") RollwiseGroupInfoBean rgi, @ModelAttribute("conclusionProceedingsForm") ConclusionProceedings conclusionProceedingsForm) {
        ModelAndView mav = new ModelAndView();;
        try {
            Users selectedEmpObj = servicePanelDAO.getSelectedEmployeeInfo(conclusionProceedingsForm.getEmpid());
            String SelectedEmpOffice = selectedEmpObj.getOffcode();
            ArrayList servicesList = servicePanelDAO.getRollWiseGrpInfo("05", "OLSCOP00100000401860001", "Y", true, "ALL");
            rgi.setGrpList(servicesList);
            rgi.setIsAccessible("Yes");
            mav.addObject("SelectedEmpOffice", SelectedEmpOffice);
            mav.addObject("SelectedEmpObj", selectedEmpObj);
            mav.addObject("RollwiseGroupInfoBean", rgi);
            mav.setViewName("redirect:/ConclusionProceedingsList.htm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "ConclusionProceedingsEmpList")
    public ModelAndView ConclusionProceedingsEmpList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("conclusionProceedingsForm") ConclusionProceedings conclusionProceedingsForm) {
        ModelAndView mav = new ModelAndView();
        List fiscyear = fiscalDAO.getFiscalYearList();
        List emplistForConclusionProceeding = null;
        if (conclusionProceedingsForm.getSearchby() != null && conclusionProceedingsForm.getSearchby().equals("O")) {
            if (conclusionProceedingsForm.getOffcode() != null && !conclusionProceedingsForm.getOffcode().equals("")) {
                emplistForConclusionProceeding = conclusionProceedingsDAO.getConclusionProceedingEmpList(conclusionProceedingsForm.getOffcode(), conclusionProceedingsForm.getEmpid());
            } else {
                emplistForConclusionProceeding = conclusionProceedingsDAO.getConclusionProceedingEmpList(lub.getLoginoffcode(), conclusionProceedingsForm.getEmpid());
            }
        } else if (conclusionProceedingsForm.getSearchby() != null && conclusionProceedingsForm.getSearchby().equals("G")) {
            emplistForConclusionProceeding = conclusionProceedingsDAO.getConclusionProceedingEmpListByGPFNo(conclusionProceedingsForm.getSearchby(), conclusionProceedingsForm.getGpfNo());
        } else {
            emplistForConclusionProceeding = conclusionProceedingsDAO.getConclusionProceedingEmpList(lub.getLoginoffcode(), conclusionProceedingsForm.getEmpid());
        }
        mav.addObject("departmentList", deptDAO.getDepartmentList());
        mav.addObject("fiscyear", fiscyear);
        mav.addObject("emplistForConclusionProceeding", emplistForConclusionProceeding);
        mav.setViewName("/conclusionProceeding/EmpListForConclusionProceeding");
        return mav;
    }
    @RequestMapping(value = "ConclusionProceedingsEmpList.htm", params = {"action=Back"}, method = RequestMethod.POST)
    public ModelAndView backConclusionProceedingsEmpList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("conclusionProceedingsForm") ConclusionProceedings conclusionProceedingsForm) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:DiscProcedingList.htm");
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "addConclusionProceedingEmpList.htm")
    public void addConclusionProceedingEmpList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("conclusionProceedingsForm") ConclusionProceedings conclusionProceedingsForm, HttpServletResponse response, ModelMap model) throws IOException, JSONException {
        response.setContentType("application/json");
        conclusionProceedingsForm.setInitiatedByempId(lub.getLoginempid());
        conclusionProceedingsForm.setInitiatedByspc(lub.getLoginspc());
        conclusionProceedingsDAO.saveConclusionProceedingEmpList(conclusionProceedingsForm);
        JSONObject obj = new JSONObject();
        obj.append("msg", "Y");
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();
    }

    @RequestMapping(value = "ConclusionProceedingsEmpList.htm", params = {"action=Get Selected Employee List"}, method = RequestMethod.POST)
    public ModelAndView getConclusionProceedingsEmpList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("conclusionProceedingsForm") ConclusionProceedings conclusionProceedingsForm) {
        ModelAndView mav = new ModelAndView();
        conclusionProceedingsForm.setInitiatedByempId(lub.getLoginempid());
        ArrayList selectedempList = conclusionProceedingsDAO.getSelectedEmpListForConclusionProceeding(conclusionProceedingsForm.getInitiatedByempId());
        mav.setViewName("/conclusionProceeding/DetailReportOfEmpForConclusionProceeding");
        mav.addObject("selectedempList", selectedempList);
        return mav;
    }

    @RequestMapping(value = "conclusionProceedingEdit")
    public ModelAndView ConclusionProceedingEdit(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj,
            @ModelAttribute("conclusionProceedingsForm") ConclusionProceedings conclusionProceedingsForm) {
        ModelAndView mav = new ModelAndView();
        List deptlist = deptDAO.getDepartmentList();
        conclusionProceedingsForm = conclusionProceedingsDAO.getConclusionProceedingData(conclusionProceedingsForm.getConcprocid());
        List punishdetailsList = conclusionProceedingsDAO.getEmpPunishDetailsData(conclusionProceedingsForm.getConcprocid(), "F");
        List leavelist = leaveTypeDAO.getLeaveTypeList();
        mav.addObject("departmentList", deptlist);
        mav.addObject("conclusionProceedingsForm", conclusionProceedingsForm);
        mav.addObject("punishdetailsList", punishdetailsList);
        mav.addObject("leavelist", leavelist);
        mav.setViewName("/conclusionProceeding/ConclusionProceedingsFirstPage");
        return mav;
    }

    @RequestMapping(value = "conclusionProceedingView")
    public ModelAndView ConclusionProceedingView(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj,
            @ModelAttribute("conclusionProceedingsForm") ConclusionProceedings conclusionProceedingsForm) {
        ModelAndView mav = new ModelAndView();
        conclusionProceedingsForm = conclusionProceedingsDAO.getConclusionProceedingData(conclusionProceedingsForm.getConcprocid());
        List punishdetailsList = conclusionProceedingsDAO.getEmpPunishDetailsData(conclusionProceedingsForm.getConcprocid(), "F");
        mav.addObject("conclusionProceedingsForm", conclusionProceedingsForm);
        mav.addObject("punishdetailsList", punishdetailsList);
        mav.setViewName("/conclusionProceeding/ConclusionProceedingsView");
        return mav;
    }

    @RequestMapping(value = "conclusionProceedingView", params = {"action=Back"})
    public ModelAndView backConclusionProceedingsview(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("conclusionProceedingsForm") ConclusionProceedings conclusionProceedingsForm) {
        ModelAndView mav = new ModelAndView();
        List fiscyear = fiscalDAO.getFiscalYearList();
        List emplistForConclusionProceeding = conclusionProceedingsDAO.getConclusionProceedingEmpList(lub.getLoginoffcode(), conclusionProceedingsForm.getEmpid());
        mav.addObject("fiscyear", fiscyear);
        mav.addObject("emplistForConclusionProceeding", emplistForConclusionProceeding);
        mav.setViewName("/conclusionProceeding/EmpListForConclusionProceeding");
        return mav;
    }

    @RequestMapping(value = "deleteInitiationDetail")
    public ModelAndView deleteInitiationDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj,
            @ModelAttribute("conclusionProceedingsForm") ConclusionProceedings conclusionProceedingsForm) {
        ModelAndView mav = new ModelAndView();
        conclusionProceedingsForm.setEmpid(selectedEmpObj.getEmpId());
        conclusionProceedingsDAO.deleteInitiationDetails(conclusionProceedingsForm);
        List procList = conclusionProceedingsDAO.getEmpConcProcList(selectedEmpObj.getEmpId());
        //List procList = conclusionProceedingsDAO.getEmpConcProcList(conclusionProceedingsForm.getEmpid());
        mav = new ModelAndView("/conclusionProceeding/ConclusionProceedingsList");
        mav.addObject("procList", procList);
        return mav;
    }

    @RequestMapping(value = "ConclusionProceedingsFirstPage", params = {"action=Backlog Entry"})
    public ModelAndView backlogEntryconclusionProceedingsFirstPage(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("SelectedEmpObj") Users selectedEmpObj,
            @ModelAttribute("conclusionProceedingsForm") ConclusionProceedings conclusionProceedingsForm) {
        ModelAndView mav = new ModelAndView();
        conclusionProceedingsForm.setEmpid(conclusionProceedingsForm.getEmpid());
        List deptlist = deptDAO.getDepartmentList();
        mav.addObject("deptList", deptlist);
        mav.addObject("departmentList", deptlist);
        mav.setViewName("/conclusionProceeding/ConclusionProceedingsFirstPage");
        return mav;
    }

    @RequestMapping(value = "ConclusionProceedingsFirstPage", params = {"action=Back"})
    public ModelAndView backConclusionProceedingsFirstPage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("conclusionProceedingsForm") ConclusionProceedings conclusionProceedingsForm) {
        ModelAndView mav = new ModelAndView();
        List fiscyear = fiscalDAO.getFiscalYearList();
        List emplistForConclusionProceeding = conclusionProceedingsDAO.getConclusionProceedingEmpList(lub.getLoginoffcode(), conclusionProceedingsForm.getEmpid());
        mav.addObject("fiscyear", fiscyear);
        mav.addObject("emplistForConclusionProceeding", emplistForConclusionProceeding);
        mav.setViewName("/conclusionProceeding/EmpListForConclusionProceeding");
        return mav;
    }

    @RequestMapping(value = "downloadAttachmentOfInitiationProceeding")
    public void downloadAttachmentOfInitiationProceeding(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("conclusionProceedings") ConclusionProceedings conclusionProceedings, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        conclusionProceedings = conclusionProceedingsDAO.getAttachedFile(conclusionProceedings.getConcprocid());
        response.setContentType(conclusionProceedings.getGetContentType());
        response.setHeader("Content-Disposition", "attachment;filename=" + conclusionProceedings.getOriginalFilename());
        OutputStream out = response.getOutputStream();
        out.write(conclusionProceedings.getFilecontent());
        out.flush();
    }

    @RequestMapping(value = "saveConclusionProceedingsData", params = {"action=Save"})
    public ModelAndView saveInitiationProceedingData(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("SelectedEmpObj") Users selectedEmpObj,
            @ModelAttribute("conclusionProceedingsForm") ConclusionProceedings conclusionProceedingsForm) {
        ModelAndView mav = new ModelAndView();
        conclusionProceedingsForm.setEmpid(selectedEmpObj.getEmpId());
        //int hidnotid = conclusionProceedingsDAO.saveInitiationDetails(conclusionProceedingsForm);
        conclusionProceedingsDAO.saveInitiationDetails(conclusionProceedingsForm);
        if (conclusionProceedingsForm.getConcprocid() > 0) {
            //conclusionProceedingsDAO.saveShowCauseDetails(conclusionProceedingsForm);
            //conclusionProceedingsDAO.saveComplianceDetails(conclusionProceedingsForm);
            conclusionProceedingsDAO.saveResultDetails(conclusionProceedingsForm);
        }
        mav.setViewName("redirect:/ConclusionProceedingsList.htm");
        return mav;
    }
    @ResponseBody
    @RequestMapping(value = "deleteAttachmentDetail.htm", method = RequestMethod.POST)
    public void deleteAttachmentDetail(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("conclusionProceedingsForm") ConclusionProceedings conclusionProceedingsForm) throws IOException, JSONException {
        ModelAndView mv = new ModelAndView();
        conclusionProceedingsDAO.deleteAttachmentDetail(conclusionProceedingsForm);

    }

    @RequestMapping(value = "saveConclusionProceedingsData", params = {"action=Back"})
    public ModelAndView backToListPage(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("SelectedEmpObj") Users selectedEmpObj,
            @ModelAttribute("conclusionProceedingsForm") ConclusionProceedings conclusionProceedingsForm) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/ConclusionProceedingsList.htm");
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "savePunishmentDetails", method = RequestMethod.POST)
    public void savePunishmentDetails(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("PunishmentDetails") PunishmentDetails punishmentDetails) {
        try {
            response.setContentType("application/json");

            PrintWriter out = null;
            if (punishmentDetails.getPentype().equalsIgnoreCase("01")) {
                punishmentDetails = conclusionProceedingsDAO.savePunishmentFineRecovery(punishmentDetails);
            } else if (punishmentDetails.getPentype().equalsIgnoreCase("02")) {
                punishmentDetails = conclusionProceedingsDAO.savePunishmentWithholdIncrement(punishmentDetails);
            } else if (punishmentDetails.getPentype().equalsIgnoreCase("03")) {
                conclusionProceedingsDAO.savePunishmentWithholdPromotion(punishmentDetails);
            } else if (punishmentDetails.getPentype().equalsIgnoreCase("04")) {
                conclusionProceedingsDAO.savePunishmentSuspension(punishmentDetails);
            } else if (punishmentDetails.getPentype().equalsIgnoreCase("05")) {
                conclusionProceedingsDAO.savePunishmentReduction(punishmentDetails);
            } else if (punishmentDetails.getPentype().equalsIgnoreCase("06")) {
                punishmentDetails = conclusionProceedingsDAO.savePunishmentRetirement(punishmentDetails);
            } else if (punishmentDetails.getPentype().equalsIgnoreCase("07")) {
                punishmentDetails = conclusionProceedingsDAO.savePunishmentRemovalService(punishmentDetails);
            } else if (punishmentDetails.getPentype().equalsIgnoreCase("08")) {
                punishmentDetails = conclusionProceedingsDAO.savePunishmentDismissalService(punishmentDetails);

            } else if (punishmentDetails.getPentype().equalsIgnoreCase("09")) {
                punishmentDetails = conclusionProceedingsDAO.savePunishmentFineRecovery(punishmentDetails);
            }
            JSONObject json = new JSONObject(punishmentDetails);
            out = response.getWriter();
            out.write(json.toString());
            out.close();
            out.flush();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "deletePunishmentDetails", method = RequestMethod.POST)
    public void deletePunishmentDetails(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("punishmentdetailsid") int punishmentdetailsid, @RequestParam("pentype") String pentype) {
        try {
            response.setContentType("application/json");
            PrintWriter out = null;
            boolean isDeleted = conclusionProceedingsDAO.deletePunishmentTypeDetails(punishmentdetailsid, pentype);
            JSONObject json = new JSONObject();
            if (isDeleted) {
                json.put("msg", "Y");
            } else {
                json.put("msg", "N");
            }
            out = response.getWriter();
            out.write(json.toString());
            out.close();
            out.flush();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "editPunishmentDetails", method = RequestMethod.POST)
    public void editPunishmentDetails(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("punishmentdetailsid") int punishmentdetailsid, @RequestParam("pentype") String pentype) {
        try {
            response.setContentType("application/json");
            PrintWriter out = null;
            PunishmentDetails pd = conclusionProceedingsDAO.getPunishmentTypeDetails(punishmentdetailsid, pentype);
            JSONObject json = new JSONObject(pd);
            out = response.getWriter();
            out.write(json.toString());
            out.close();
            out.flush();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
