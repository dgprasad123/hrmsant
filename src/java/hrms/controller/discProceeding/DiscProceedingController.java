/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.discProceeding;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.FileDownload;
import hrms.dao.conclusionproceedings.ConclusionProceedingsDAO;
import hrms.dao.discProceeding.DiscProceedingDAO;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.fiscalyear.FiscalYearDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.task.TaskDAO;
import hrms.model.conclusionproceedings.ConclusionProceedings;
import hrms.model.discProceeding.CourtCaseBean;
import hrms.model.discProceeding.DefenceBean;
import hrms.model.discProceeding.DiscChargeBean;
import hrms.model.discProceeding.DiscWitnessBean;
import hrms.model.discProceeding.DispatchDetailsBean;
import hrms.model.discProceeding.DpViewBean;
import hrms.model.discProceeding.EnquiryBean;
import hrms.model.discProceeding.FinalOrder;
import hrms.model.discProceeding.ForwardDiscChargeBean;
import hrms.model.discProceeding.IoBean;
import hrms.model.discProceeding.IoReportBean;
import hrms.model.discProceeding.NoticeBean;
import hrms.model.discProceeding.ProceedingBean;
import hrms.model.discProceeding.SubmitProceedingBean;
import hrms.model.employee.Employee;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import hrms.model.redesignation.Redesignation;
import hrms.model.task.TaskListHelperBean;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller

@SessionAttributes({"LoginUserBean"})
public class DiscProceedingController {

    @Autowired
    public DiscProceedingDAO discProcedDAO;
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
    TaskDAO taskDAO;
    @Autowired
    public DepartmentDAO deptDAO;
    @Autowired
    ConclusionProceedingsDAO conclusionProceedingsDAO;

    @Autowired
    FiscalYearDAO fiscalDAO;
    /*
     When User Click on Disciplinary Proceeding the method calls and load all the proceedings initited by him
     */

    @RequestMapping(value = "DiscProcedingList.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView getDiscProcedingList(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("SelectedEmpObj") Users selectedEmpObj) {
        ModelAndView mav = new ModelAndView("/discProceeding/discProcedFinalList");
        List dpEmpList = discProcedDAO.getDiscProcedingFinalList(lub.getLoginempid(), 1, 10);
        //List procListofficewise = conclusionProceedingsDAO.getEmpConcProcListOfficewise(lub.getLoginoffcode());
        mav.addObject("dpEmpList", dpEmpList);
        //mav.addObject("procListofficewise", procListofficewise);
        return mav;
    }

    /* When user click New Disciplinary Proceeding the mehod calls and show the Rule List*/
    @RequestMapping(value = "chooseRuleForDepartmentalProceeding", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView chooseRuleForDepartmentalProceeding(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pbean") ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView("/discProceeding/chooseRuleForDepartmentalProceeding");
        return mav;
    }
    /* When user click New Disciplinary Proceeding the mehod calls and show the employee list*/

    @RequestMapping(value = "DiscProceding1", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView getEmployeeList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pbean") ProceedingBean pbean) {
        ModelAndView mav = null;
        String path = null;
        try {

            //path = "/discProceeding/OfficeEmployeeList";
            path = "/discProceeding/empListForInitiiatingdiscproced";
            pbean.setOffCode(lub.getLoginoffcode());
            pbean.setOffName(lub.getLoginoffname());
            mav = new ModelAndView(path, "pbean", pbean);
            mav.addObject("departmentList", departmentDAO.getDepartmentList());
            mav.addObject("empList", employeeDAO.getOfficeWiseEmployeeListForDp(lub.getLoginoffcode()));
            mav.addObject("deptCode", lub.getLogindeptcode());
            // mav.addObject("rule", pbean.getRule());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "getotherOffice", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView getotherOffice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ProceedingBean") ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/discProceeding/otherOffice");
        return mav;
    }

    @RequestMapping(value = "viewRule15DiscProceding", method = {RequestMethod.GET})
    public ModelAndView viewRule15DiscProceding(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ProceedingBean") ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView();
        //discProcedDAO.viewRule15DiscProceeding(lub.getLoginempid());
        mav.setViewName("/discProceeding/viewRule15DiscProceding");
        return mav;
    }

    @RequestMapping(value = "editRule15ProceedingForOngoingDP", method = {RequestMethod.POST}, params = {"action=Add Delinquent Officer"})
    public ModelAndView addMoreDelinquentOfficerforOngoingDP(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ProceedingBean") ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView();
        pbean.setOffCode(lub.getLoginoffcode());
        pbean.setOffName(lub.getLoginoffname());
        mav = new ModelAndView("/discProceeding/addDelinquentOfficerFromOtherOffice", "pbean", pbean);
        mav.addObject("departmentList", departmentDAO.getDepartmentList());
        mav.addObject("empList", employeeDAO.getOfficeWiseEmployeeListForDp(lub.getLoginoffcode()));
        mav.addObject("deptCode", lub.getLogindeptcode());
        return mav;
    }

    @RequestMapping(value = "editRule15ProceedingForOngoingDPRule15", method = {RequestMethod.GET})
    public ModelAndView editRule15ProceedingForOngoingDPRule15(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pbean") ProceedingBean pbean, @ModelAttribute("emp") Employee emp,
            @ModelAttribute("defencebean") DefenceBean defencebean, @ModelAttribute("ioBean") IoBean ioBean) {
        ModelAndView mav = new ModelAndView();
        DpViewBean dpviewBean = discProcedDAO.getDpdetail(pbean.getDaId());
        Employee[] delinquentofficerList = discProcedDAO.getDelinquentOfficerForOngoing(pbean.getDaId());
        List deptlist = deptDAO.getDepartmentList();
        pbean = discProcedDAO.getDiscProceedingData(pbean.getDaId());
        pbean.setDadid(Integer.parseInt(delinquentofficerList[0].getRefid()));
        //defencebean = discProcedDAO.getDefenceStatementByDOOngoingDP(Integer.parseInt(pbean.getDadid()));
        List disccharge = discProcedDAO.getDiscChargeList(pbean.getDaId());
        //ioBean = discProcedDAO.getIoDetailsOngoingDP(pbean.getDaId());
        List punishdetailsListProposed = conclusionProceedingsDAO.getEmpPunishDetailsData(pbean.getDaId(), "P");
        List punishdetailsListFinal = conclusionProceedingsDAO.getEmpPunishDetailsData(pbean.getDaId(), "F");
        mav.addObject("articleOfChargeList", disccharge);
        mav.addObject("iseditable", discProcedDAO.isEditable(pbean.getDaId()));
        mav.addObject("dpviewBean", dpviewBean);
        mav.addObject("delinquentofficerList", delinquentofficerList);
        mav.addObject("deptlist", deptlist);
        mav.addObject("departmentList", deptlist);
        mav.addObject("pbean", pbean);
        mav.addObject("punishdetailsListProposed", punishdetailsListProposed);
        mav.addObject("punishdetailsListFinal", punishdetailsListFinal);

        mav.setViewName("/discProceeding/SelectedDelinquentOfficerListForDisciplinaryProceedingRule15");
        return mav;
    }

    @RequestMapping(value = "editRule15ProceedingForOngoingDPRule15.htm", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Save"})
    public ModelAndView saveRule15ProceedingForOngoingDPRule15(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pbean") ProceedingBean pbean, @ModelAttribute("emp") Employee emp,
            @ModelAttribute("defencebean") DefenceBean defencebean, @ModelAttribute("ioBean") IoBean ioBean) {
        ModelAndView mav = new ModelAndView();

        if (pbean.getApprovalByAuthority() != null && pbean.getApprovalByAuthority().equals("AlreadyApproveByDA")) {
            discProcedDAO.saveMemorandumDetails(pbean);
            for (int i = 0; i < pbean.getDelinquent().length; i++) {
                String[] temp = pbean.getDelinquent()[i].split("-");
                ConclusionProceedings conclusionProceedings = new ConclusionProceedings();
                conclusionProceedings.setEmpid(temp[0]);
                conclusionProceedings.setInitNotOrdNo(pbean.getMemoNo());
                conclusionProceedings.setInitNotOrdDt(pbean.getMemoDate());
                conclusionProceedings.setNotdept(pbean.getDadept());
                conclusionProceedings.setNotoffice(pbean.getDaoffice());
                conclusionProceedings.setNotspc(pbean.getDaspc());
                conclusionProceedings.setRuleofproc(pbean.getRule());
                conclusionProceedings.setPostedspc(temp[1]);
                conclusionProceedingsDAO.saveInitiationDetails(conclusionProceedings);
            }

        }

        if (pbean.getShowcause() != null && pbean.getShowcause().equals("AlreadyServed")) {
            discProcedDAO.saveFirstShowCauseDetails(pbean);
        } else if (pbean.getDefenceremarkByDO() == null && pbean.getIsIoReportSubmitted() == null && pbean.getServeDelinquentOnIoRemarks() == null && pbean.getPunishmentProposedOnRepresentationOfDoOnIoReport() == null && pbean.getHasSendNoticetoDO() != null && pbean.getHasSendNoticetoDO().equals("Y") && pbean.getHasIoAppointed().equals("")) {
            discProcedDAO.saveDefenceStatementByDelinquentOfficerOngoingDP(defencebean);
        }
        if (pbean.getDefenceremarkByDO() != null && pbean.getDefenceremarkByDO().equals("Inquiry")) {
            discProcedDAO.saveIoDetailsOngoingDP(ioBean);
        }
        if (pbean.getDefenceremarkByDO() != null && pbean.getDefenceremarkByDO().equals("MinorPenalty")) {
            discProcedDAO.saveConsultationDetailsOnRepresentationofdoOnIoreport(pbean);
            discProcedDAO.saveConcurranceDetailsOnRepresentationofdoOnIoreport(pbean);
            discProcedDAO.saveFinalOrderDetailsOnRepresentationofdoOnIoreport(pbean);
        }
        if (pbean.getDefenceremarkByDO() != null && pbean.getDefenceremarkByDO().equals("Exoneration")) {
            discProcedDAO.saveExanaurationFinalOrderDetailsOnRepresentationofdoOnIoreport(pbean);
        }
        if (pbean.getIsIoReportSubmitted() != null && pbean.getIsIoReportSubmitted().equals("IoReportSubmit")) {
            discProcedDAO.saveIoRemarksDetailOngoingDP(ioBean);
        }
        if (pbean.getServeDelinquentOnIoRemarks() != null && pbean.getServeDelinquentOnIoRemarks().equals("AlreadyServedDelinquentOnIoRemarks")) {
            discProcedDAO.saveSecondShowCauseDetails(pbean);
        }
        if (pbean.getProposedpunishmentOnRepresentationOfDOOnpunishment() == null && pbean.getHasSendSecondNotice().equals("Y") && pbean.getHasDoRepresentOnsecondshowCause() != null && !pbean.getHasDoRepresentOnsecondshowCause().equals('Y') && pbean.getThirdNoticeToDelinquentPunishmentProposed() == null && pbean.getHasSendthirdshowCause().equals("") && pbean.getProposedpunishmentOnRepresentationOfDOOnpunishment() == null) {
            discProcedDAO.saveRepresentationOfDOOnSecondShowCauseDetails(pbean);
        }
        if (pbean.getPunishmentProposedOnRepresentationOfDoOnIoReport() != null && pbean.getPunishmentProposedOnRepresentationOfDoOnIoReport().equals("punishmentOnRepresentationOfDOOnIoReport")) {
            discProcedDAO.saveConsultationDetailsOnRepresentationofdoOnIoreport(pbean);
            discProcedDAO.saveConcurranceDetailsOnRepresentationofdoOnIoreport(pbean);
            discProcedDAO.saveFinalOrderDetailsOnRepresentationofdoOnIoreport(pbean);
        } else if (pbean.getPunishmentProposedOnRepresentationOfDoOnIoReport() != null && pbean.getPunishmentProposedOnRepresentationOfDoOnIoReport().equals("exanarutionOnRepresentationOfDOOnIoReport")) {
            discProcedDAO.saveExanaurationFinalOrderDetailsOnRepresentationofdoOnIoreport(pbean);
        }
        /*
         */
        if (pbean.getThirdNoticeToDelinquentPunishmentProposed() != null && pbean.getThirdNoticeToDelinquentPunishmentProposed().equals("AlreadyServedDelinquentthirdNotice")) {
            discProcedDAO.saveThirdShowCauseDetails(pbean);
            //discProcedDAO.saveThirdShowReplyByDelinquentOfficer(pbean);
        }
        if (pbean.getHasSendthirdshowCause() != null && pbean.getHasSendthirdshowCause().equals("Y")) {
            discProcedDAO.saveThirdShowReplyByDelinquentOfficer(pbean);
        }
        if (pbean.getProposedpunishmentOnRepresentationOfDOOnpunishment() != null && pbean.getProposedpunishmentOnRepresentationOfDOOnpunishment().equals("proposedpunishmentOnRepresentationOfDOOpunishment")) {
            discProcedDAO.saveConsultationDetailsOnRepresentationofdoOnIoreport(pbean);
            discProcedDAO.saveConcurranceDetailsOnRepresentationofdoOnIoreport(pbean);
            discProcedDAO.saveFinalOrderDetailsOnRepresentationofdoOnIoreport(pbean);
        } else if (pbean.getProposedpunishmentOnRepresentationOfDOOnpunishment() != null && pbean.getProposedpunishmentOnRepresentationOfDOOnpunishment().equals("exanarutionOnRepresentationOfDOOpunishment")) {
            discProcedDAO.saveExanaurationFinalOrderDetailsOnRepresentationofdoOnIoreport(pbean);
        }
        mav.setViewName("redirect:editRule15ProceedingForOngoingDPRule15.htm?daId=" + pbean.getDaId());
        return mav;
    }

    @RequestMapping(value = "downloadMemorandumAttachment")
    public void downloadMemorandumAttachment(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pbean") ProceedingBean pbean, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        pbean = discProcedDAO.getAttachedFileForMemorandumDetails(pbean.getDaId());
        response.setContentType(pbean.getMemorandumcontentType());
        response.setHeader("Content-Disposition", "attachment;filename=" + pbean.getMemorandumoriginalFileName());
        OutputStream out = response.getOutputStream();
        out.write(pbean.getFilecontent());
        out.flush();
    }

    @RequestMapping(value = "downloadFirstShowCauseAttachment")
    public void downloadFirstShowCauseAttachment(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pbean") ProceedingBean pbean, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        pbean = discProcedDAO.getAttachedFileForFirstShowCauseDetails(pbean.getDaId());
        response.setContentType(pbean.getFirstshowcausecontentType());
        response.setHeader("Content-Disposition", "attachment;filename=" + pbean.getFirstshowcauseoriginalFileName());
        OutputStream out = response.getOutputStream();
        out.write(pbean.getFilecontent());
        out.flush();
    }

    @RequestMapping(value = "downloadDefenceStatementByDelinquentOfficerAttachment")
    public void downloadDefenceStatementByDelinquentOfficerAttachment(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("defencebean") DefenceBean defencebean, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        defencebean = discProcedDAO.getAttachedFileForDefenceStatementByDelinquentOfficerDetails(defencebean.getDadid());
        response.setContentType(defencebean.getDefenceByDOcontentType());
        response.setHeader("Content-Disposition", "attachment;filename=" + defencebean.getDefenceByDOoriginalFileName());
        OutputStream out = response.getOutputStream();
        out.write(defencebean.getFilecontent());
        out.flush();
    }

    @RequestMapping(value = "downloadIoRemarksDetailOngoingDPAttachment")
    public void downloadIoRemarksDetailOngoingDPAttachment(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ioBean") IoBean ioBean, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        ioBean = discProcedDAO.getAttachedFileForIoRemarksDetailOngoingDPDetails(ioBean.getDaId());
        response.setContentType(ioBean.getRemarksByIOcontentType());
        response.setHeader("Content-Disposition", "attachment;filename=" + ioBean.getRemarksByIOoriginalFileName());
        OutputStream out = response.getOutputStream();
        out.write(ioBean.getFilecontent());
        out.flush();
    }

    @RequestMapping(value = "downloadSecondShowCauseDetailsAttachment")
    public void downloadSecondShowCauseDetailsAttachment(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pbean") ProceedingBean pbean, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        pbean = discProcedDAO.getAttachedFileForSecondShowCauseDetails(pbean.getDaId());
        response.setContentType(pbean.getNoticetoDOOnIoRemarkfiletype());
        response.setHeader("Content-Disposition", "attachment;filename=" + pbean.getNoticetoDOOnIoRemarkoriginalFileName());
        OutputStream out = response.getOutputStream();
        out.write(pbean.getFilecontent());
        out.flush();
    }

    @RequestMapping(value = "downloadAttachedFileForRepresentationOfDOOnSecondShowCause")
    public void downloadAttachedFileForRepresentationOfDOOnSecondShowCause(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pbean") ProceedingBean pbean, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        pbean = discProcedDAO.getAttachedFileForRepresentationOfDOOnSecondShowCause(pbean.getDadid());
        response.setContentType(pbean.getSecondshowcausecontentTypeondoRepresentation());
        response.setHeader("Content-Disposition", "attachment;filename=" + pbean.getSecondshowcauseoriginalFileNameondoRepresentation());
        OutputStream out = response.getOutputStream();
        out.write(pbean.getFilecontent());
        out.flush();
    }

    @RequestMapping(value = "downloadAttachedFileForThirdShowCauseDetails")
    public void downloadAttachedFileForThirdShowCauseDetails(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pbean") ProceedingBean pbean, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        pbean = discProcedDAO.getAttachedFileForThirdShowCauseDetails(pbean.getDaId());
        response.setContentType(pbean.getThirdNoticetoDOOnForPunishmentFileType());
        response.setHeader("Content-Disposition", "attachment;filename=" + pbean.getThirdNoticetoDOOnForPunishmentorgFileName());
        OutputStream out = response.getOutputStream();
        out.write(pbean.getFilecontent());
        out.flush();
    }

    @RequestMapping(value = "downloadAttachedFileForThirdShowReplyByDelinquentOfficer")
    public void downloadAttachedFileForThirdShowReplyByDelinquentOfficer(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pbean") ProceedingBean pbean, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        pbean = discProcedDAO.getAttachedFileForThirdShowReplyByDelinquentOfficer(pbean.getDadid());
        response.setContentType(pbean.getThirdshowcausefiletypeOnRepresentationOfDoOnIoReport());
        response.setHeader("Content-Disposition", "attachment;filename=" + pbean.getThirdshowcausediskfilenameOnRepresentationOfDoOnIoReport());
        OutputStream out = response.getOutputStream();
        out.write(pbean.getFilecontent());
        out.flush();
    }

    @RequestMapping(value = "editRule15ProceedingForOngoingDPRule15.htm", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Add New"})
    public ModelAndView addNewProceedingForOngoingDParticleofcharge(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("daId") int daId) {
        ModelAndView mav = new ModelAndView();
        DiscChargeBean chargeBean = new DiscChargeBean();
        chargeBean.setDaId(daId);
        List disccharge = discProcedDAO.getDiscChargeList(daId);
        mav.addObject("articleOfChargeList", disccharge);
        mav.addObject("chargebean", chargeBean);
        mav.setViewName("/discProceeding/articleOfCharge");
        return mav;
    }

    @RequestMapping(value = "editRule15ProceedingForOngoingDPRule15", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Back"})
    public ModelAndView backeditProceedingForOngoingDPRule15(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ProceedingBean") ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView("/discProceeding/discProcedFinalList");
        List dpEmpList = discProcedDAO.getDiscProcedingFinalList(lub.getLoginempid(), 1, 10);
        mav.addObject("dpEmpList", dpEmpList);
        return mav;
    }

    @RequestMapping(value = "caseDiaryForRule15ProceedingForOngoingDPRule15", method = {RequestMethod.GET})
    public ModelAndView caseDiaryForRule15ProceedingForOngoingDPRule15(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ProceedingBean") ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView();
        DpViewBean dpviewBean = discProcedDAO.getDpdetail(pbean.getDaId());
        Employee[] delinquentofficerList = discProcedDAO.getDelinquentOfficerForOngoing(pbean.getDaId());
        List deptlist = deptDAO.getDepartmentList();
        pbean = discProcedDAO.getDiscProceedingData(pbean.getDaId());
        pbean.setDadid(Integer.parseInt(delinquentofficerList[0].getRefid()));
        List disccharge = discProcedDAO.getDiscChargeList(pbean.getDaId());
        mav.addObject("articleOfChargeList", disccharge);
        mav.addObject("iseditable", discProcedDAO.isEditable(pbean.getDaId()));
        mav.addObject("dpviewBean", dpviewBean);
        mav.addObject("delinquentofficerList", delinquentofficerList);
        mav.addObject("deptlist", deptlist);
        mav.addObject("pbean", pbean);
        mav.setViewName("/discProceeding/CaseDiaryForRule15Proceeding");
        return mav;
    }

    @RequestMapping(value = "editRule16ProceedingForOngoingDP", method = {RequestMethod.GET})
    public ModelAndView editRule16ProceedingForOngoingDP(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ProceedingBean") ProceedingBean pbean, @ModelAttribute("emp") Employee emp) {
        ModelAndView mav = new ModelAndView();
        DpViewBean dpviewBean = discProcedDAO.getDpdetail(pbean.getDaId());
        Employee[] delinquentofficerList = discProcedDAO.getDelinquentOfficerForOngoing(pbean.getDaId());
        //ArrayList delinquentOfficer = discProcedDAO.getDelinquentOfficer(pbean.getDaId());
        List deptlist = deptDAO.getDepartmentList();
        mav.addObject("iseditable", discProcedDAO.isEditable(pbean.getDaId()));
        mav.addObject("dpviewBean", dpviewBean);
        mav.addObject("delinquentofficerList", delinquentofficerList);
        //mav.addObject("delinquentOfficer", delinquentOfficer);
        mav.addObject("deptlist", deptlist);
        mav.setViewName("/discProceeding/SelectedDelinquentOfficerListForRule16ongoingDP");
        return mav;
    }

    @RequestMapping(value = "editRule16ProceedingForOngoingDP", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Back"})
    public ModelAndView backeditRule16ProceedingForOngoingDP(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ProceedingBean") ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView("/discProceeding/discProcedFinalList");
        List dpEmpList = discProcedDAO.getDiscProcedingFinalList(lub.getLoginempid(), 1, 10);
        mav.addObject("dpEmpList", dpEmpList);
        return mav;
    }

    /*when the user click on EDIT Button of the page employee list the Add delinquent officer.jsp page will diplay*/
    @RequestMapping(value = "editRule15Proceeding", method = {RequestMethod.GET}, params = {"action=Edit"})
    public ModelAndView editRule15Proceeding(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ProceedingBean") ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView();
        DpViewBean dpviewBean = discProcedDAO.getDpdetail(pbean.getDaId());
        ArrayList delinquentOfficer = discProcedDAO.getDelinquentOfficer(pbean.getDaId());
        mav.addObject("iseditable", discProcedDAO.isEditable(pbean.getDaId()));
        mav.addObject("dpviewBean", dpviewBean);
        mav.addObject("delinquentOfficer", delinquentOfficer);
        mav.setViewName("/discProceeding/selectedDelinquentOfficerListForDiscProceed");
        return mav;
    }

    @RequestMapping(value = "courtCaseForDP", method = {RequestMethod.GET})
    public ModelAndView CourtCaseDpRule15Proceeding(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("courtCaseBean") CourtCaseBean courtCaseBean, @ModelAttribute("ProceedingBean") ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView();
        ArrayList courtcaseList = discProcedDAO.getCourtCaseDetail(courtCaseBean.getDaId());
        DpViewBean dpviewBean = discProcedDAO.getDpdetail(pbean.getDaId());
        mav.addObject("courtcaseList", courtcaseList);
        List fiscyear = fiscalDAO.getFiscalYearList();
        mav.addObject("fiscyear", fiscyear);
        mav.setViewName("/discProceeding/CourtInterventionForDP");
        return mav;
    }

    @RequestMapping(value = "courtCaseForDP", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Save"})
    public ModelAndView savecourtCaseForDP(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("courtCaseBean") CourtCaseBean courtCaseBean, final RedirectAttributes redirectAttributes, @ModelAttribute("ProceedingBean") ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView();
        discProcedDAO.saveCourtCaseDetail(courtCaseBean);
        ArrayList courtcaseList = discProcedDAO.getCourtCaseDetail(courtCaseBean.getDaId());
        mav.addObject("courtcaseList", courtcaseList);
        mav.setViewName("/discProceeding/CourtInterventionForDP");
        return mav;
    }

    @RequestMapping(value = "downloadcourtCaseForDP")
    public void downloadcourtCaseForDP(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("courtCaseBean") CourtCaseBean courtCaseBean, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        courtCaseBean = discProcedDAO.getAttachedFileForCourtCase(courtCaseBean.getCaseId());
        response.setContentType(courtCaseBean.getCourtDocumentFileType());
        response.setHeader("Content-Disposition", "attachment;filename=" + courtCaseBean.getCourtDocumentorgFileName());
        OutputStream out = response.getOutputStream();
        out.write(courtCaseBean.getFilecontent());
        out.flush();
    }

    @RequestMapping(value = "courtCaseForDP", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView backcourtCaseForDP(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("courtCaseBean") CourtCaseBean courtCaseBean, final RedirectAttributes redirectAttributes, @ModelAttribute("ProceedingBean") ProceedingBean pbean, @ModelAttribute("emp") Employee emp) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:DiscProcedingList.htm");
        return mav;
    }

    @RequestMapping(value = "editRule15Proceeding", method = {RequestMethod.GET}, params = {"action=DeleteDP"})
    public ModelAndView deleteRule15Proceeding(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ProceedingBean") ProceedingBean pbean, @ModelAttribute("emp") Employee emp) {
        ModelAndView mav = new ModelAndView();
        Employee[] delinquentofficerList = discProcedDAO.getDelinquentOfficerForOngoing(pbean.getDaId());
        pbean.setDadid(Integer.parseInt(delinquentofficerList[0].getRefid()));
        discProcedDAO.deleteRule15Proceeding(pbean);
        mav.setViewName("redirect:DiscProcedingList.htm");
        return mav;
    }
    /* when the user click on article of charge button of the page (add delinquent officer) it will loads all the data and show the articleofchargelist.jsp page */

    @RequestMapping(value = "editRule15Proceeding.htm", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Article of Charge"})
    public ModelAndView getDiscchargeList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("daId") int daId) {
        ModelAndView mav = new ModelAndView();
        DiscChargeBean chargeBean = new DiscChargeBean();
        chargeBean.setDaId(daId);
        //chargeBean.setDacid(dacid);
        //List disccharge = discProcedDAO.getDiscChargeList(daId);
        List disccharge = discProcedDAO.getDiscChargeList(daId);
        mav.addObject("articleOfChargeList", disccharge);
        mav.addObject("chargebean", chargeBean);
        mav.setViewName("/discProceeding/articleOfChargeList");
        return mav;
    }

    @RequestMapping(value = "editRule15Proceeding", method = {RequestMethod.POST}, params = {"action=Add Delinquent Officer"})
    public ModelAndView addMoreDelinquentOfficer(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ProceedingBean") ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView();
        pbean.setOffCode(lub.getLoginoffcode());
        pbean.setOffName(lub.getLoginoffname());
        mav = new ModelAndView("/discProceeding/addDelinquentOfficerFromOtherOffice", "pbean", pbean);
        mav.addObject("departmentList", departmentDAO.getDepartmentList());
        mav.addObject("empList", employeeDAO.getOfficeWiseEmployeeListForDp(lub.getLoginoffcode()));
        mav.addObject("deptCode", lub.getLogindeptcode());
        return mav;
    }
    /* When the User click on the back button of the(Article of charge List) page  the discproceedinglist.jsp page will display  */

    @RequestMapping(value = "editRule15Proceeding", method = {RequestMethod.POST}, params = {"action=Back"})
    public String backToDiscProceedList(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        return "redirect:DiscProcedingList.htm";
    }

    @RequestMapping(value = "editRule15Proceeding", method = {RequestMethod.POST}, params = {"action=Delete"})
    public ModelAndView deleteDelinquentOfficer(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ProceedingBean") ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView();

        discProcedDAO.deleteDelinquentOfficer(pbean);
        ArrayList delinquentOfficer = discProcedDAO.getDelinquentOfficer(pbean.getDaId());
        mav.addObject("delinquentOfficer", delinquentOfficer);
        mav.setViewName("/discProceeding/selectedDelinquentOfficerListForDiscProceed");
        return mav;
    }

    /*When user click on Add Delinquent Officer this will show the list of employee for adding*/

    /*When User Click on the save button of employee list (Add Delinquent Officer)*/
    @RequestMapping(value = "addDelinquentOfficer", method = {RequestMethod.POST}, params = {"action=Add"})
    public ModelAndView addDelinquentOfficerFromOtherOffice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ProceedingBean") ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView();
        discProcedDAO.saveDelinquentOfficer(pbean);
        ArrayList delinquentOfficer = discProcedDAO.getDelinquentOfficer(pbean.getDaId());

        mav.addObject("delinquentOfficer", delinquentOfficer);
        mav.setViewName("/discProceeding/selectedDelinquentOfficerListForDiscProceed");
        return mav;
    }

    @RequestMapping(value = "addDelinquentOfficer", method = {RequestMethod.POST}, params = {"action=Get Employee"})
    public ModelAndView getDelinquentOfficerFromOtherOffice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ProceedingBean") ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView("/discProceeding/addDelinquentOfficerFromOtherOffice", "pbean", pbean);
        mav.addObject("departmentList", departmentDAO.getDepartmentList());
        mav.addObject("empList", employeeDAO.getOfficeWiseEmployeeListForDp(pbean.getOffCode()));
        mav.addObject("deptCode", lub.getLogindeptcode());
        return mav;

    }
    /*When User Click on the save button of employee list (Add Delinquent Officer)*/

    @RequestMapping(value = "addDelinquentOfficer", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView backaddDelinquentOfficer(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ProceedingBean") ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView();
        ArrayList delinquentOfficer = discProcedDAO.getDelinquentOfficer(pbean.getDaId());
        mav.addObject("delinquentOfficer", delinquentOfficer);
        mav.setViewName("/discProceeding/selectedDelinquentOfficerListForDiscProceed");
        return mav;
    }
    /*
     @RequestMapping(value = "discproced.htm", method = {RequestMethod.POST, RequestMethod.GET})
     public ModelAndView getDiscproced(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
     ModelAndView mav = new ModelAndView("/discProceeding/discproced");

     return mav;
     }
     */

    /*  */
    @RequestMapping(value = "rule15Controller", method = {RequestMethod.POST}, params = {"action=Get Employee"})
    public ModelAndView getEmployeeController(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pbean") ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView("/discProceeding/empListForInitiiatingdiscproced", "pbean", pbean);
        mav.addObject("departmentList", departmentDAO.getDepartmentList());
        mav.addObject("empList", employeeDAO.getOfficeWiseEmployeeListForDp(pbean.getOffCode()));
        mav.addObject("deptCode", lub.getLogindeptcode());
        return mav;
    }

    @RequestMapping(value = "rule15Controller", method = {RequestMethod.POST}, params = {"action=Start"})
    public ModelAndView rule15Controller(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute() ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView();
        pbean.setInitHrmsId(lub.getLoginempid());
        pbean.setInitSpc(lub.getLoginspc());
        int daId = discProcedDAO.saveRule15MemoDetails(pbean);
        pbean.setDaId(daId);

        discProcedDAO.saveDelinquentOfficer(pbean);
        ProceedingBean proceedingbean = discProcedDAO.getRule15MemoDetails(lub.getLoginoffcode(), pbean.getDelinquent());
        mav.addObject("proceedingbean", proceedingbean);
        mav.addObject("pbean", pbean);
        List dpEmpList = discProcedDAO.getDiscProcedingFinalList(lub.getLoginempid(), 1, 10);
        mav.addObject("dpEmpList", dpEmpList);
        mav.setViewName("/discProceeding/discProcedFinalList");
        /* } else {
         mav.setViewName("redirect:/editRule15Proceeding.htm?action=Edit&daId=" + daId);
         } */
        return mav;
    }

    @RequestMapping(value = "rule15Controller", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView backrule15Controller(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute() ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView("/discProceeding/discProcedFinalList");
        List dpEmpList = discProcedDAO.getDiscProcedingFinalList(lub.getLoginempid(), 1, 10);
        mav.addObject("dpEmpList", dpEmpList);
        return mav;
    }

    @RequestMapping(value = "rule15Controller", method = {RequestMethod.GET}, params = {"action=Approval Order"})
    public ModelAndView putApprovalOrderController(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute() ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView();
        pbean.setInitHrmsId(lub.getLoginempid());
        pbean.setInitSpc(lub.getLoginspc());
        ArrayList delinquentOfficer = discProcedDAO.getDelinquentOfficer(pbean.getDaId());
        String[] delinquents = new String[delinquentOfficer.size()];
        for (int i = 0; i < delinquentOfficer.size(); i++) {
            Employee emp = (Employee) delinquentOfficer.get(i);
            delinquents[i] = emp.getEmpid();
        }
        pbean.setDelinquent(delinquents);
        ProceedingBean proceedingbean = discProcedDAO.getRule15MemoDetails(lub.getLoginoffcode(), pbean.getDelinquent());
        mav.addObject("proceedingbean", proceedingbean);
        mav.addObject("pbean", pbean);
        mav.setViewName("/discProceeding/rule15Memo");
        return mav;
    }
    /* When the user click on witness option of the page(Article of chargeList)    */

    @RequestMapping(value = "saveAddWitness", method = {RequestMethod.POST}, params = {"action=Save"})
    public ModelAndView saveAddWitness(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("witnessbean") DiscWitnessBean witnessbean, final RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        discProcedDAO.saveAddWitness(witnessbean);
        mav.setViewName("redirect:/employeeWitnessList.htm");
        redirectAttributes.addAttribute("action", "witnessList");
        redirectAttributes.addAttribute("dacid", witnessbean.getDacid());
        redirectAttributes.addAttribute("daId", witnessbean.getDaId());
        return mav;
    }
    /* When the user click on the Back button ther WitnessList page will display*/

    @RequestMapping(value = "saveAddWitness", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView backtoWitness(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("witnessbean") DiscWitnessBean witnessbean) {
        ModelAndView mav = new ModelAndView();

        List witnessList = discProcedDAO.getWitnessList(witnessbean.getDacid());
        mav.addObject("witnessList", witnessList);
        mav.addObject("witnessbean", witnessbean);
        mav.setViewName("/discProceeding/witnessList");
        return mav;
    }

    @RequestMapping(value = "saveAddWitness", method = {RequestMethod.POST}, params = {"action=Get Employee"})
    public ModelAndView saveAddWitnessgetEmployeeController(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pbean") ProceedingBean pbean, @ModelAttribute("witnessbean") DiscWitnessBean witnessbean) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("departmentList", departmentDAO.getDepartmentList());
        mav.addObject("empList", employeeDAO.getOfficeWiseEmployeeListForDp(pbean.getOffCode()));
        mav.addObject("deptCode", lub.getLogindeptcode());
        mav.setViewName("/discProceeding/employeeListForWitness");
        return mav;
    }

    /* When the user click on Edit option of the page(Article of chargeList) Article of charge.jsp page will display where the user input the data and save it */
    @RequestMapping(value = "saveDisccharge", method = {RequestMethod.POST}, params = {"action=Save"})
    public ModelAndView saveDisccharge(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("chargebean") DiscChargeBean chargeBean, @ModelAttribute("pbean") ProceedingBean pbean,
            @ModelAttribute("emp") Employee emp, @RequestParam(value = "openfrom", required = false) String openfrom) {
        ModelAndView mav = new ModelAndView();
        //int dacwid = discProcedDAO.saveDiscCharge(chargeBean);
        String msg = discProcedDAO.saveDiscCharge(chargeBean);
//        DpViewBean dpviewBean = discProcedDAO.getDpdetail(pbean.getDaId());
//        emp = discProcedDAO.getDelinquentOfficerForOngoing(pbean.getDaId());
//        List deptlist = deptDAO.getDepartmentList();
//        pbean = discProcedDAO.getDiscProceedingData(pbean.getDaId());
//        List disccharge = discProcedDAO.getDiscChargeList(pbean.getDaId());
//        mav.addObject("articleOfChargeList", disccharge);
//        mav.addObject("iseditable", discProcedDAO.isEditable(pbean.getDaId()));
//        mav.addObject("dpviewBean", dpviewBean);
//        mav.addObject("emp", emp);
//        mav.addObject("deptlist", deptlist);
//        mav.addObject("pbean", pbean);
        if (openfrom != null && openfrom.equals("T")) {
            mav.setViewName("/discProceeding/annexure3");
        } else {
            mav.setViewName("redirect:editRule15ProceedingForOngoingDPRule15.htm?daId=" + chargeBean.getDaId());
        }
        return mav;
    }

    @RequestMapping(value = "downloadFileForArticleOfCharge", method = {RequestMethod.GET})
    public void downloadFileForArticleOfCharge(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("chargeBean") DiscChargeBean chargeBean, HttpServletResponse response) throws IOException {
        FileDownload fileDownloadData = discProcedDAO.getFileDownloadData(chargeBean.getDacid(), chargeBean.getDocumentTypeName());
        response.setContentType(fileDownloadData.getFiletype());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileDownloadData.getOriginalfilename());
        OutputStream out = response.getOutputStream();
        out.write(fileDownloadData.getFilecontent());
        out.flush();
        out.close();
    }
    /* When the user click on Edit option of the page(Article of chargeList) Article of charge.jsp page will display from where the user delete  */

    @RequestMapping(value = "saveDisccharge", method = {RequestMethod.POST}, params = {"action=Delete"})
    public ModelAndView deleteDiscCharge(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("chargebean") DiscChargeBean chargebean, @ModelAttribute("pbean") ProceedingBean pbean, @ModelAttribute("emp") Employee emp) {
        ModelAndView mav = new ModelAndView();
        discProcedDAO.deleteDiscCharge(chargebean.getDacid());
        DpViewBean dpviewBean = discProcedDAO.getDpdetail(pbean.getDaId());
        Employee[] delinquentofficerList = discProcedDAO.getDelinquentOfficerForOngoing(pbean.getDaId());
        List deptlist = deptDAO.getDepartmentList();
        pbean = discProcedDAO.getDiscProceedingData(pbean.getDaId());
        List disccharge = discProcedDAO.getDiscChargeList(pbean.getDaId());
        mav.addObject("articleOfChargeList", disccharge);
        mav.addObject("iseditable", discProcedDAO.isEditable(pbean.getDaId()));
        mav.addObject("dpviewBean", dpviewBean);
        mav.addObject("delinquentofficerList", delinquentofficerList);
        mav.addObject("deptlist", deptlist);
        mav.addObject("pbean", pbean);
        mav.setViewName("/discProceeding/SelectedDelinquentOfficerListForDisciplinaryProceedingRule15");
        return mav;
    }
    /* When the user click on Edit option of the page(Article of chargeList) Article of charge.jsp page will display from where the user choose the cancel button and again it will return to the page Article of charge List.jsp  */

    @RequestMapping(value = "saveDisccharge", method = {RequestMethod.POST}, params = {"action=Cancel"})
    public ModelAndView cancelDiscCharge(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("emp") Employee emp, @ModelAttribute("pbean") ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView();
        DpViewBean dpviewBean = discProcedDAO.getDpdetail(pbean.getDaId());
        Employee[] delinquentofficerList = discProcedDAO.getDelinquentOfficerForOngoing(pbean.getDaId());
        List deptlist = deptDAO.getDepartmentList();
        pbean = discProcedDAO.getDiscProceedingData(pbean.getDaId());
        List disccharge = discProcedDAO.getDiscChargeList(pbean.getDaId());
        mav.addObject("articleOfChargeList", disccharge);
        mav.addObject("iseditable", discProcedDAO.isEditable(pbean.getDaId()));
        mav.addObject("dpviewBean", dpviewBean);
        mav.addObject("delinquentofficerList", delinquentofficerList);
        mav.addObject("deptlist", deptlist);
        mav.addObject("pbean", pbean);
        mav.setViewName("/discProceeding/SelectedDelinquentOfficerListForDisciplinaryProceedingRule15");
        return mav;
    }
    /* When the user click on AddNew Button of the page(ArticleofchargeList) it will fill all the data and save it */

    @RequestMapping(value = "addNewDisccharge", method = {RequestMethod.POST}, params = {"action=Add New"})
    public ModelAndView AddNew(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("chargebean") DiscChargeBean chargebean) {
        ModelAndView mav = new ModelAndView();
        List disccharge = discProcedDAO.getDiscChargeList(chargebean.getDaId());
        mav.addObject("articleOfChargeList", disccharge);
        mav.setViewName("/discProceeding/articleOfCharge");

        return mav;
    }
    /* When the user click on Back Button of the page(ArticleofchargeList) it will return back to the delinquentOfficerList page */

    @RequestMapping(value = "addNewDisccharge", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView Back(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ProceedingBean") ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView();

        DpViewBean dpviewBean = discProcedDAO.getDpdetail(pbean.getDaId());
        ArrayList delinquentOfficer = discProcedDAO.getDelinquentOfficer(pbean.getDaId());
        mav.addObject("iseditable", discProcedDAO.isEditable(pbean.getDaId()));
        mav.addObject("dpviewBean", dpviewBean);
        mav.addObject("delinquentOfficer", delinquentOfficer);
        mav.setViewName("/discProceeding/selectedDelinquentOfficerListForDiscProceed");
        return mav;
    }
    /* */

    @RequestMapping(value = "editNewDisccharge", method = {RequestMethod.GET})
    public ModelAndView editNewDisccharge(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("chargebean") DiscChargeBean chargebean, @ModelAttribute("pbean") ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/discProceeding/articleOfCharge");
        mav.addObject("chargebean", discProcedDAO.getChargeDetails(pbean.getDaId()));
        return mav;
    }
    /* When the user Click on the  witness button of the page ArticleofChargeList the page witnessList will show*/

    @RequestMapping(value = "employeeWitnessList.htm", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=witnessList"})
    public ModelAndView getWitnessList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("witnessbean") DiscWitnessBean witnessbean) {
        ModelAndView mav = new ModelAndView();
        List witnessList = discProcedDAO.getWitnessList(witnessbean.getDacid());
        List witnessOtherList = discProcedDAO.getOtherWitnessList(witnessbean.getDacid());
        mav.addObject("witnessList", witnessList);
        mav.addObject("witnessOtherList", witnessOtherList);
        mav.setViewName("/discProceeding/witnessList");

        return mav;
    }
    /* When the user click on the Add button of the page WitnessList it will add all the data and show in the employeelist.jsp page */

    @RequestMapping(value = "employeeWitnessList", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Add Witness (HRMS)"})
    public ModelAndView getEmployeeWitnessList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("witnessbean") DiscWitnessBean witnessbean) {
        ModelAndView mav = new ModelAndView();
        List empList = employeeDAO.getOfficeWiseEmployeeListForDp(lub.getLoginoffcode());
        mav.addObject("empList", empList);
        mav.addObject("witnessbean", witnessbean);
        mav.addObject("departmentList", departmentDAO.getDepartmentList());
        mav.setViewName("/discProceeding/employeeListForWitness");

        return mav;

    }

    @RequestMapping(value = "employeeWitnessList", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Add Witness (NonHRMS)"})
    public ModelAndView viewotherhrmswitness(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("witnessbean") DiscWitnessBean witnessbean) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/discProceeding/otherhrmsWitness");
        return mav;

    }

    @RequestMapping(value = "employeeWitnessList", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Back"})
    public ModelAndView backtoWitnessList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pbean") ProceedingBean pbean, @ModelAttribute("emp") Employee emp) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:editRule15ProceedingForOngoingDPRule15.htm?daId=" + pbean.getDaId());

        return mav;
    }
    /* When the user click on the Delete button of the page WitnessList it will delete the data */

    @RequestMapping(value = "employeeWitnessList", method = {RequestMethod.POST}, params = {"action=Delete"})
    public ModelAndView deleteWitnessList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("witnessbean") DiscWitnessBean witnessbean, final RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        discProcedDAO.deleteWitnessList(witnessbean.getSelecteddacwid());
        mav.setViewName("redirect:/employeeWitnessList.htm");
        redirectAttributes.addAttribute("action", "witnessList");
        redirectAttributes.addAttribute("dacid", witnessbean.getDacid());
        redirectAttributes.addAttribute("daId", witnessbean.getDaId());
        return mav;
    }
    /* Here the discproceedList page will dispaly */

    @RequestMapping(value = "DiscProcedingList1.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView getDiscProcedingList1(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView("/discProceeding/discProcedFinalList");

        return mav;
    }
    /*Use to Update the page of Discproceeding List*/

    @RequestMapping(value = "saveRule15Memo.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView saveRule15Memo(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("proceedingbean") ProceedingBean proceedingbean) {
        ModelAndView mav = new ModelAndView("/discProceeding/discProcedFinalList");
        discProcedDAO.updateRule15MemoDetails(proceedingbean);
        mav.setViewName("redirect:DiscProcedingList.htm");
        return mav;
    }

    @RequestMapping(value = "discApprovedAction.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView viewDiscproceeding(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pbean") ProceedingBean proceedingbean, @ModelAttribute("DiscWitnessBean") DiscWitnessBean discWitnessBean) {
        ModelAndView mav = new ModelAndView();
        TaskListHelperBean taskListHelperBean = taskDAO.getTaskDetails(proceedingbean.getTaskId());
        if (taskListHelperBean.getStatusId() == 83) {
            //mav.setViewName("redirect:/discApprovedAction.htm?taskId=" + proceedingbean.getTaskId());
            mav.setViewName("/discProceeding/DepartmentProceedingDeclined");
        } else if (taskListHelperBean.getStatusId() == 82) {
            // mav.setViewName("/discProceeding/discFinalNoticePDF.htm");
            mav.setViewName("redirect:/discFinalNoticePDF.htm?taskId=" + proceedingbean.getTaskId());
        } else if (taskListHelperBean.getStatusId() == 81) {
            mav.setViewName("redirect:/viewNotice2Reply.htm?taskId=" + proceedingbean.getTaskId());
        } else if (taskListHelperBean.getStatusId() == 80) {
            mav.setViewName("redirect:/viewNotice1Reply.htm?taskId=" + proceedingbean.getTaskId());
        } else if (taskListHelperBean.getStatusId() == 79) {
            mav.setViewName("redirect:/viewNotice2.htm?taskId=" + proceedingbean.getTaskId());
        } else if (taskListHelperBean.getStatusId() == 78) {
            mav.setViewName("redirect:/viewNotice.htm?taskId=" + proceedingbean.getTaskId());
        } else if (taskListHelperBean.getStatusId() == 77) {
            mav.setViewName("redirect:/finalIoReport.htm?taskId=" + proceedingbean.getTaskId());
            //mav.setViewName("/discProceeding/ioReportView");
        } else if (taskListHelperBean.getStatusId() == 76) {
            mav.setViewName("redirect:/refeaingIoReport.htm?taskId=" + proceedingbean.getTaskId());
        } else if (taskListHelperBean.getStatusId() == 75) {
            mav = new ModelAndView("redirect:/viewIoReport.htm?taskId=" + proceedingbean.getTaskId());
        } else if (taskListHelperBean.getStatusId() == 74) {
            mav = new ModelAndView("redirect:/viewIo.htm?taskId=" + proceedingbean.getTaskId());
        } else if (taskListHelperBean.getStatusId() == 70) {//Status for defence statement
            DefenceBean defencebean = discProcedDAO.getDefenceDetailFromTaskId(proceedingbean.getTaskId());
            DpViewBean dpviewbean = discProcedDAO.viewRule15DiscProceeding(defencebean.getDaId());
            List discchargelist = discProcedDAO.getDiscChargeListWithWitness(defencebean.getDaId());
            //List disccharge = discProcedDAO.getDiscChargeList(daid);
            mav.addObject("discchargelist", discchargelist);
            mav.addObject("dpviewbean", dpviewbean);
            mav.addObject("defencebean", defencebean);
            // mav.addObject("articleOfChargeList", disccharge);
            mav.setViewName("/discProceeding/DefenceStatmentByDelinquentOfficer");
        } else if (taskListHelperBean.getStatusId() == 71) {//Status for "REVIEW STATEMENT"             
            int daid = discProcedDAO.getDaidAndDefIdFromTaskid(proceedingbean.getTaskId());
            TaskListHelperBean taskDtls = taskDAO.getTaskDetails(proceedingbean.getTaskId());
            Employee[] delinquentOfficerDtls = discProcedDAO.getDelinquentOfficerForOngoing(daid);
            DefenceBean defencebean = discProcedDAO.getDefenceDetailFromDadid(Integer.parseInt(delinquentOfficerDtls[0].getRefid()));
            //DefenceBean defencebean = discProcedDAO.getDefenceDetailFromDaid(daid);
            List discchargelist = discProcedDAO.getDiscChargeList(daid);
            //List disccharge = discProcedDAO.getDiscChargeList(daid);

            DiscChargeBean discChargeBean = new DiscChargeBean();
            discChargeBean.setDaId(daid);
            //mav.addObject("delinquentOfficerDtls", delinquentOfficerDtls);
            //mav.addObject("defencebean", defencebean);
            mav.addObject("discChargeBean", discChargeBean);
            mav.addObject("discchargelist", discchargelist);
            mav.addObject("pbean", proceedingbean);
            mav.addObject("taskdtls", taskDAO.getTaskDetails(proceedingbean.getTaskId()));
            //mav.addObject("articleOfChargeList", disccharge);
            mav.setViewName("/discProceeding/defenceStatementOfDelinquentOfficer");
        } else {
            int daid = discProcedDAO.getDaidFromTaskid(proceedingbean.getTaskId());
            if (daid == 0) {
                daid = discProcedDAO.getRefidFromTaskid(proceedingbean.getTaskId());
            }

            DpViewBean dpviewbean = discProcedDAO.viewRule15DiscProceeding(lub.getLoginoffcode(), daid + "", proceedingbean.getTaskId());
            proceedingbean.setDaId(daid);
            mav.addObject("dpviewbean", dpviewbean);
            mav.addObject("taskId", proceedingbean.getTaskId());
            discProcedDAO.getDiscChargeList(daid);
            if (dpviewbean.getIsapprovedbyauthority() != null && dpviewbean.getIsapprovedbyauthority().equals("Y")) {
                mav.setViewName("redirect:/discMemoPDF.htm?taskId=" + proceedingbean.getTaskId());
            } else if (dpviewbean.getIsapprovedbyauthority() == null || dpviewbean.getIsapprovedbyauthority().equals("N")) {
                mav.setViewName("/discProceeding/discApprovedAction");
            }
        }
        return mav;
    }

    /*Use to Edit the page of Disciplinary Authority after of discapproved.jsp page*/
    @RequestMapping(value = "discApprovedAction.htm", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Edit"})
    public ModelAndView editDiscApprove(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pbean") ProceedingBean proceedingbean, @RequestParam("taskId") int taskId) {

        ModelAndView mav = new ModelAndView();
        int daid = discProcedDAO.getDaidFromTaskid(taskId);
        proceedingbean.setDaId(daid);
        ArrayList delinquentOfficer = discProcedDAO.getDelinquentOfficer(proceedingbean.getDaId());

        mav.addObject("delinquentOfficer", delinquentOfficer);
        mav.setViewName("/discProceeding/selectedDelinquentOfficerListForDiscProceed");
        mav.addObject("ProceedingBean", proceedingbean);
        return mav;
    }
    /*After the Disciplinary Authority click on the Approved Button of discapproved.jsp page */

    @RequestMapping(value = "discApprovedAction.htm", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Approved"})
    public ModelAndView Discproceeding(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pbean") ProceedingBean proceedingbean, @RequestParam("taskId") int taskId) {
        //int daid = discProcedDAO.getDaidFromTaskid(proceedingbean.getTaskId());

        DpViewBean viewbean = discProcedDAO.viewRule15DiscProceeding(lub.getLoginoffcode(), proceedingbean.getDaId() + "", proceedingbean.getTaskId());
        ModelAndView mv = new ModelAndView();
        if (viewbean.getIsapprovedbyauthority() != null && viewbean.getIsapprovedbyauthority().equals("Y")) {
            mv.setViewName("redirect:/discMemoPDF.htm?taskId=" + proceedingbean.getTaskId());
        } else {
            viewbean.setMemoNo(proceedingbean.getMemoNo());
            viewbean.setMemoDate(proceedingbean.getMemoDate());
            viewbean.setTaskId(taskId);
            discProcedDAO.sendFYI(viewbean);
            mv.addObject("dpviewbean", viewbean);
            mv.setViewName("redirect:/discMemoPDF.htm?taskId=" + proceedingbean.getTaskId());
            //mv.setViewName("/discProceeding/discApprovedAction");
        }
        return mv;
    }

    @RequestMapping(value = "discApprovedAction.htm", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Declined"})
    public ModelAndView Discproceedingdeclined(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pbean") ProceedingBean proceedingbean, @RequestParam("taskId") int taskId) {
        //int daid = discProcedDAO.getDaidFromTaskid(proceedingbean.getTaskId());

        DpViewBean viewbean = discProcedDAO.viewRule15DiscProceeding(lub.getLoginoffcode(), proceedingbean.getDaId() + "", proceedingbean.getTaskId());
        ModelAndView mv = new ModelAndView();
        //if (viewbean.getIsapprovedbyauthority() != null && viewbean.getIsapprovedbyauthority().equals("N")) {
        // mv.setViewName("redirect:/discMemoPDF.htm?taskId=" + proceedingbean.getTaskId());
        //} else {
        viewbean.setTaskId(taskId);
        discProcedDAO.sendDeclined(viewbean);
        mv.addObject("dpviewbean", viewbean);
        mv.setViewName("/discProceeding/DepartmentProceedingDeclined");

        // }
        return mv;
    }
    /**/

    @RequestMapping(value = "viewIo", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView viewIo(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("IoBean") IoBean ioBean) {
        ModelAndView mav = new ModelAndView();
        int daid = discProcedDAO.getRefidFromTaskid(ioBean.getTaskId());
        List delinquentOfficerList = discProcedDAO.getDelinquentOfficer(daid);
        DefenceBean defencebean = discProcedDAO.getDefenceDetailFromDaid(daid);
        //List discchargelist = discProcedDAO.getDiscChargeList(daid);
        List discchargelist = discProcedDAO.getDiscChargeListWithWitness(daid);
        List disccharge = discProcedDAO.getDiscChargeList(daid);
        DiscChargeBean discChargeBean = new DiscChargeBean();
        discChargeBean.setDaId(daid);
        mav.addObject("delinquentOfficerList", delinquentOfficerList);
        mav.addObject("defencebean", defencebean);
        mav.addObject("discChargeBean", discChargeBean);
        mav.addObject("articleOfChargeList", disccharge);
        mav.addObject("dpviewbean", discProcedDAO.getDpdetail(daid));
        mav.addObject("discchargelist", discchargelist);
        mav.addObject("taskdtls", taskDAO.getTaskDetails(ioBean.getTaskId()));
        mav.setViewName("/discProceeding/AppointmentOfIoandMo");
        return mav;
    }

    @RequestMapping(value = "viewIo", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Get Employee"})
    public ModelAndView otherOfficeEmployeeList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("IoBean") IoBean ioBean) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("departmentList", departmentDAO.getDepartmentList());
        mav.addObject("empList", employeeDAO.getOfficeWiseEmployeeList(ioBean.getOffCode()));
        mav.setViewName("/discProceeding/IoAndMoList");
        return mav;
    }

    @RequestMapping(value = "viewIo", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Add IO"})
    public ModelAndView addIo(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("IoBean") IoBean ioBean) {
        ModelAndView mav = new ModelAndView();
        ioBean.setOfficertype("IO");
        ioBean.setOffCode(lub.getLoginoffcode());
        ioBean.setOffName(lub.getLoginoffname());
        mav.addObject("departmentList", departmentDAO.getDepartmentList());
        mav.addObject("empList", employeeDAO.getOfficeWiseEmployeeList(lub.getLoginoffcode()));
        mav.addObject("deptCode", lub.getLogindeptcode());
        mav.setViewName("/discProceeding/IoAndMoList");
        return mav;
    }

    @RequestMapping(value = "viewIo", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Add MO"})
    public ModelAndView addMo(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("IoBean") IoBean ioBean) {
        ModelAndView mav = new ModelAndView();
        ioBean.setOfficertype("MO");
        ioBean.setOffCode(lub.getLoginoffcode());
        ioBean.setOffName(lub.getLoginoffname());
        mav.addObject("departmentList", departmentDAO.getDepartmentList());
        mav.addObject("empList", employeeDAO.getOfficeWiseEmployeeList(lub.getLoginoffcode()));
        mav.addObject("deptCode", lub.getLogindeptcode());
        mav.setViewName("/discProceeding/IoAndMoList");
        return mav;
    }

    @RequestMapping(value = "viewIo", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Send"})
    public ModelAndView saveIo(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("IoBean") IoBean ioBean, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        ArrayList investingOfficer = new ArrayList();
        if (session.getAttribute("investingOfficer") != null) {
            investingOfficer = (ArrayList) session.getAttribute("investingOfficer");

        }
        String[] doio = new String[investingOfficer.size()];
        for (int i = 0; i < investingOfficer.size(); i++) {
            IoBean ciobean = (IoBean) investingOfficer.get(i);
            doio[i] = ciobean.getIoEmpHrmsId() + "-" + ciobean.getIoEmpSPC() + "-" + ciobean.getOfficertype();
        }
        ioBean.setIoAppointhrmsId(lub.getLoginempid());
        ioBean.setIoAppointingspc(lub.getLoginspc());
        ioBean.setDoio(doio);
        discProcedDAO.saveIo(ioBean);
        taskDAO.closeTask(ioBean.getTaskId());
        mav.setViewName("/discProceeding/ioreportSubmit");
        return mav;
    }

    @RequestMapping(value = "viewIo", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Add"})
    public ModelAndView AddIo(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("IoBean") IoBean ioBean, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        String[] selectedIo = ioBean.getDoio();
        ArrayList investingOfficerList = discProcedDAO.getInvestingOfficer(ioBean.getDaId());
        if (selectedIo != null) {
            ArrayList investingOfficer = new ArrayList();
            if (session.getAttribute("investingOfficer") != null) {
                investingOfficer = (ArrayList) session.getAttribute("investingOfficer");
            }

            if (investingOfficerList.size() == 0) {
                for (int i = 0; i < selectedIo.length; i++) {
                    Employee employee = employeeDAO.getEmployeeProfile(selectedIo[i]);
                    IoBean ciobean = new IoBean();
                    ciobean.setIoEmpHrmsId(employee.getEmpid());
                    ciobean.setIoEmpSPC(employee.getSpc());
                    ciobean.setIoEmpName(employee.getEmpName());
                    ciobean.setIoEmpCurDegn(employee.getSpn());
                    ciobean.setOfficertype(ioBean.getOfficertype());
                    investingOfficer.add(ciobean);
                }
            } else {
                investingOfficer.addAll(investingOfficerList);
                for (int i = 0; i < selectedIo.length; i++) {
                    Employee employee = employeeDAO.getEmployeeProfile(selectedIo[i]);
                    IoBean ciobean = new IoBean();
                    ciobean.setIoEmpHrmsId(employee.getEmpid());
                    ciobean.setIoEmpSPC(employee.getSpc());
                    ciobean.setIoEmpName(employee.getEmpName());
                    ciobean.setIoEmpCurDegn(employee.getSpn());
                    ciobean.setOfficertype(ioBean.getOfficertype());
                    investingOfficer.add(ciobean);
                }
            }
            session.setAttribute("investingOfficer", investingOfficer);
        }

        int daid = discProcedDAO.getRefidFromTaskid(ioBean.getTaskId());
        List delinquentOfficerList = discProcedDAO.getDelinquentOfficer(daid);
        DefenceBean defencebean = discProcedDAO.getDefenceDetailFromDaid(daid);
        List discchargelist = discProcedDAO.getDiscChargeList(daid);
        List disccharge = discProcedDAO.getDiscChargeList(daid);
        DiscChargeBean discChargeBean = new DiscChargeBean();
        discChargeBean.setDaId(daid);
        mav.addObject("taskdtls", taskDAO.getTaskDetails(ioBean.getTaskId()));
        mav.addObject("delinquentOfficerList", delinquentOfficerList);
        mav.addObject("defencebean", defencebean);
        mav.addObject("discChargeBean", discChargeBean);
        mav.addObject("discchargelist", discchargelist);
        mav.setViewName("/discProceeding/AppointmentOfIoandMo");
        mav.addObject("articleOfChargeList", disccharge);
        mav.addObject("dpviewbean", discProcedDAO.getDpdetail(daid));

        return mav;
    }

    @RequestMapping(value = "viewIo", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Back"})
    public ModelAndView backaddIo(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ProceedingBean") ProceedingBean pbean) {
        ModelAndView mav = new ModelAndView();
        DpViewBean dpviewBean = discProcedDAO.getDpdetail(pbean.getDaId());
        ArrayList delinquentOfficer = discProcedDAO.getDelinquentOfficer(pbean.getDaId());
        List deptlist = deptDAO.getDepartmentList();
        mav.addObject("iseditable", discProcedDAO.isEditable(pbean.getDaId()));
        mav.addObject("dpviewBean", dpviewBean);
        mav.addObject("delinquentOfficer", delinquentOfficer);
        mav.addObject("deptlist", deptlist);
        mav.setViewName("/discProceeding/SelectedDelinquentOfficerListForongoingDP");
        return mav;
    }

    @RequestMapping(value = "viewenquiry", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView viewenquiry(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("enquiryBean") EnquiryBean enquiryBean) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/discProceeding/enquiryReport");
        return mav;
    }

    @RequestMapping(value = "saveenquiry", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView saveenquiry(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("enquiryBean") EnquiryBean enquiryBean) {
        ModelAndView mav = new ModelAndView();
        int dacid = discProcedDAO.saveEnquiryReport(enquiryBean);
        if (!enquiryBean.getUploadDocument().isEmpty()) {
            employeeDAO.uploadAttachedFile(dacid, "ENQUIRYSTMT", enquiryBean.getUploadDocument());
        }
        mav.setViewName("/discProceeding/enquiryReport");
        return mav;
    }

    @RequestMapping(value = "viewdefence", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView viewdefence(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("defencebean") DefenceBean defencebean) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("taskdtls", taskDAO.getTaskDetails(defencebean.getTaskId())); //IN CONTROLLER
        mav.setViewName("/discProceeding/DefenceStatmentByDelinquentOfficer");
        return mav;
    }

    @RequestMapping(value = "savedefenceStatementByDo", method = {RequestMethod.POST})
    public ModelAndView savedefence(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("defencebean") DefenceBean defencebean) {
        ModelAndView mav = new ModelAndView();
        defencebean.setDefhrmsid(lub.getLoginempid());
        defencebean.setDefspc(lub.getLoginspc());
        discProcedDAO.saveDefenceStatementByDelinquentOfficer(defencebean);
        taskDAO.closeTask(defencebean.getTaskId());
        /* if (!defencebean.getUploadDocument().isEmpty()) {
         employeeDAO.uploadAttachedFile(defencebean.getDefid(), "DEFENCESTMT", defencebean.getUploadDocument());
         } */
        mav.setViewName("/discProceeding/submitdefencestatement of Do");
        return mav;
    }
    /*the dispatchList button of articleofChargeList page*/

    @RequestMapping(value = "dispatchList", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView dispatchList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("DispatchDetailsBean") DispatchDetailsBean dispatchDetailsBean) {
        ModelAndView mav = new ModelAndView();
        List dispatchList = discProcedDAO.getDispatchList(dispatchDetailsBean.getComTypeReference(), dispatchDetailsBean.getComType());
        mav.addObject("dispatchList", dispatchList);
        mav.setViewName("/discProceeding/dispatchList");

        return mav;
    }
    /*Use to download the attachment of dispatch details  */

    @RequestMapping(value = "downloadDispatchAttachment")
    public void downloadEmployeeAttachment(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("DispatchDetailsBean") DispatchDetailsBean dispatchDetailsBean, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        dispatchDetailsBean = discProcedDAO.getAttachedDispatchFile(dispatchDetailsBean.getDdid());
        response.setContentType(dispatchDetailsBean.getFiletype());
        response.setHeader("Content-Disposition", "attachment;filename=" + dispatchDetailsBean.getEmsoFileName());
        OutputStream out = response.getOutputStream();
        out.write(dispatchDetailsBean.getFilecontent());
        out.flush();
        out.close();
    }
    /*Use to delete the attachment of dispatch details  */

    @RequestMapping(value = "removeDispatchAttachment", method = {RequestMethod.POST, RequestMethod.GET})
    public String removeDispatchAttachment(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("DispatchDetailsBean") DispatchDetailsBean dispatchDetailsBean, final RedirectAttributes redirectAttributes) {
        dispatchDetailsBean = discProcedDAO.removeDispatchFile(dispatchDetailsBean.getDdid());
        redirectAttributes.addAttribute("comType", dispatchDetailsBean.getComType());
        redirectAttributes.addAttribute("comTypeReference", dispatchDetailsBean.getComTypeReference());
        redirectAttributes.addAttribute("daId", dispatchDetailsBean.getDaId());
        return "redirect:/dispatchList.htm";
    }
    /*Use to save the dispatch details  */

    @RequestMapping(value = "savedispatchdetails", method = {RequestMethod.POST, RequestMethod.GET})
    public String savedispatchdetails(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("DispatchDetailsBean") DispatchDetailsBean dispatchDetailsBean, final RedirectAttributes redirectAttributes) {
        discProcedDAO.saveDispatchDetails(dispatchDetailsBean);
        redirectAttributes.addAttribute("comType", dispatchDetailsBean.getComType());
        redirectAttributes.addAttribute("comTypeReference", dispatchDetailsBean.getComTypeReference());
        redirectAttributes.addAttribute("daId", dispatchDetailsBean.getDaId());
        return "redirect:/dispatchList.htm";
    }
    /*Use to add new  dispatch   */

    @RequestMapping(value = "dispatchList", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Add New Dispatch"})
    public ModelAndView viewdispatchdetails(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("DispatchDetailsBean") DispatchDetailsBean dispatchDetailsBean) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/discProceeding/dispatchDetails");
        return mav;
    }
    /*Use to Back from DispatchList page   */

    @RequestMapping(value = "dispatchList", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Back"})
    public String Backdispatchdetails(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("DispatchDetailsBean") DispatchDetailsBean dispatchDetailsBean, final RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("action", "Edit");
        redirectAttributes.addAttribute("daId", dispatchDetailsBean.getDaId());
        return "redirect:/editRule15Proceeding.htm";
    }
    /*Use to save otherhrmswitness of articleofcharge page   */

    @RequestMapping(value = "saveotherhrmswitness", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Save"})
    public ModelAndView saveotherhrmswitness(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("witnessbean") DiscWitnessBean witnessbean, final RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        discProcedDAO.saveOtherHrmsWitnessList(witnessbean);
        mav.setViewName("redirect:/employeeWitnessList.htm");
        redirectAttributes.addAttribute("action", "witnessList");
        redirectAttributes.addAttribute("dacid", witnessbean.getDacid());
        redirectAttributes.addAttribute("daId", witnessbean.getDaId());
        return mav;
    }
    /*Use to Back from  otherhrmswitness of articleofcharge page   */

    @RequestMapping(value = "saveotherhrmswitness", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Back"})
    public ModelAndView backotherhrmswitness(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("chargebean") DiscChargeBean chargebean, @ModelAttribute("DiscWitnessBean") DiscWitnessBean discWitnessBean) {
        ModelAndView mav = new ModelAndView();
        //discProcedDAO.saveOtherHrmsWitnessList(discWitnessBean);
        //mav.addObject("discWitnessBean",discWitnessBean );
        mav.addObject("chargebean", chargebean);
        List disccharge = discProcedDAO.getDiscChargeList(chargebean.getDaId());
        mav.addObject("articleOfChargeList", disccharge);
        mav.setViewName("/discProceeding/articleOfChargeList");

        return mav;
    }
    /*Use to view the defence Statement of delinquent officer to initiating Authority*/

    @RequestMapping(value = "viewdefenceStatementOfDelinquentOfficer")
    public ModelAndView viewdefenceStatementOfDelinquentOfficer(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("discChargeBean") DiscChargeBean discChargeBean, @RequestParam("taskId") int dadid, @RequestParam("dadid") int taskId) {
        ModelAndView mav = new ModelAndView();
        int daid = discProcedDAO.getDaidFromTaskid(taskId);
        List discchargelist = discProcedDAO.getDiscChargeList(daid);
        List delinquentOfficerList = discProcedDAO.getDelinquentOfficer(daid);

        DefenceBean defencebean = discProcedDAO.getDefenceDetailFromDadid(dadid);
        mav.addObject("defencebean", defencebean);
        mav.addObject("delinquentOfficerList", delinquentOfficerList);
        mav.addObject("discchargelist", discchargelist);
        mav.addObject("taskdtls", taskDAO.getTaskDetails(discChargeBean.getTaskId()));
        mav.setViewName("/discProceeding/defenceStatementOfDelinquentOfficer");

        return mav;
    }
    /*Use to Edit the defence Statement of delinquent officer to initiating Authority*/

    @RequestMapping(value = "editDefenceStatementReafing", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView editDefenceStatementReafing(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("discChargeBean") DiscChargeBean discChargeBean) {
        ModelAndView mav = new ModelAndView();
        DiscChargeBean tdiscChargeBean = discProcedDAO.getChargeDetails(discChargeBean.getDaId());
        tdiscChargeBean.setDacid(discChargeBean.getDacid());
        tdiscChargeBean.setTaskId(discChargeBean.getTaskId());
        Employee delinquentOfficerDtls = discProcedDAO.getDelinquentOfficerDtl(discChargeBean.getDadid());
        DefenceBean defencebean = discProcedDAO.getDefenceDetailFromDadid(discChargeBean.getDadid());
        mav.addObject("discChargeBean", tdiscChargeBean);
        mav.addObject("delinquentOfficerDtls", delinquentOfficerDtls);
        mav.addObject("defencebean", defencebean);
        mav.setViewName("/discProceeding/editDefenceStatementReafing");

        return mav;
    }
    /*Use to Update the defence Statement of delinquent officer to initiating Authority*/

    @RequestMapping(value = "updateDefenceStatementReafing", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView updateDefenceStatementReafing(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("discChargeBean") DiscChargeBean discChargeBean) {
        ModelAndView mav = new ModelAndView();
        //int daid = discProcedDAO.getDaidFromTaskid(taskId);

        //mav.addObject("dpviewbean", discProcedDAO.viewRule15DiscProceeding(lub.getLoginoffcode(), daid + "", taskId));
        //mav.addObject("taskId", taskId); 
        discProcedDAO.updatedefenceStatementRefeaing(discChargeBean);
        //discProcedDAO.saveOtherHrmsWitnessList(discWitnessBean);
        //mav.addObject("discWitnessBean",discWitnessBean );
        //mav.addObject("DiscWitnessBean", discWitnessBean);
        //mav.addObject("pbean", proceedingbean);
        /*List discchargelist = discProcedDAO.getDiscChargeList(discChargeBean.getDaId());
         //List delinquentOfficerList = discProcedDAO.getDelinquentOfficer(discChargeBean.getDaId());
         //mav.addObject("delinquentOfficerList", delinquentOfficerList);
         //mav.addObject("discchargelist", discchargelist);
         //mav.setViewName("/discProceeding/defenceStatementRefeaing");*/
        mav.setViewName("redirect:/discApprovedAction.htm?taskId=" + discChargeBean.getTaskId());
        return mav;
    }
    /*Use to Back from the defence Statement of delinquent officer to initiating Authority*/

    @RequestMapping(value = "updateDefenceStatementReafing", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Back"})
    public ModelAndView backDefenceStatementReafing(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("discChargeBean") DiscChargeBean discChargeBean, @RequestParam("taskId") int taskId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/discApprovedAction.htm?taskId=" + discChargeBean.getTaskId());
        return mav;
    }
    /*Use to Submit  the defence Statement of delinquent officer to Disciplinary Authority done by  initiating Authority*/

    @RequestMapping(value = "viewdefenceStatementOfDelinquentOfficer", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=submit"})
    public ModelAndView submitDefenceStatementReafing(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("discChargeBean") DiscChargeBean discChargeBean, @ModelAttribute("defencebean") DefenceBean defencebean) {
        ModelAndView mav = new ModelAndView();
        discProcedDAO.updatedefenceStatementRefeaing(discChargeBean);
        List discchargelist = discProcedDAO.getDiscChargeList(discChargeBean.getDaId());
        List delinquentOfficerList = discProcedDAO.getDelinquentOfficer(discChargeBean.getDaId());
        mav.addObject("delinquentOfficerList", delinquentOfficerList);
        mav.addObject("discchargelist", discchargelist);
        discProcedDAO.submitDefenceStatementRefeaing(defencebean);
        taskDAO.closeTask(discChargeBean.getTaskId());

        mav.setViewName("/discProceeding/defenceStatementRefeaingofDo");

        return mav;
    }

    @RequestMapping(value = "refeaingIoReport")
    public ModelAndView refeaingIoReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ioReportBean") IoReportBean ioReportBean) {
        ModelAndView mav = new ModelAndView();
        int daioid = discProcedDAO.getRefidFromTaskid(ioReportBean.getTaskId());

        ArrayList investingreport = discProcedDAO.getIoReportList(daioid);
        //discProcedDAO.updateIoRemark(ioReportBean);
        mav.addObject("taskdtls", taskDAO.getTaskDetails(ioReportBean.getTaskId()));
        mav.addObject("investingreport", investingreport);
        mav.setViewName("/discProceeding/ioReportRefeaing");
        return mav;
    }

    @RequestMapping(value = "finalIoReport")
    public ModelAndView finalIoReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ioReportBean") IoReportBean ioReportBean) {
        ModelAndView mav = new ModelAndView();
        int daioid = discProcedDAO.getRefidFromTaskid(ioReportBean.getTaskId());
        DpViewBean dpViewBean = discProcedDAO.getDpdetail(daioid);

        IoReportBean ioReportdetails = discProcedDAO.getIODetails(daioid);
        ioReportdetails.setTaskId(ioReportBean.getTaskId());

        mav.addObject("dpdetails", discProcedDAO.getDpdetail(ioReportdetails.getDaId()));
        mav.addObject("ioReportBean", ioReportdetails);
        mav.addObject("dpViewBean", dpViewBean);
        //ArrayList investingreport = discProcedDAO.getIoReportList(daioid);
        //discProcedDAO.updateIoRemark(ioReportBean);
        //mav.addObject("investingreport", investingreport);
        mav.setViewName("/discProceeding/ioReportView");

        return mav;
    }

    @RequestMapping(value = "viewfinalIoReport", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView viewfinalIoReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ioReportBean") IoReportBean ioReportBean) {
        ModelAndView mav = new ModelAndView();

        mav.setViewName("/discProceeding/ioRemarkofInitingAuthority");

        return mav;
    }

    @RequestMapping(value = "viewfinalIoReport", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Save remark"})
    public ModelAndView SaveRemark(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ioReportBean") IoReportBean ioReportBean) {
        ModelAndView mav = new ModelAndView();
        discProcedDAO.updateIoRemark(ioReportBean);
        mav.setViewName("redirect:/refeaingIoReport.htm.htm?taskId=" + ioReportBean.getTaskId());

        return mav;
    }

    @RequestMapping(value = "viewIoReport")
    public ModelAndView viewIoReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ioReportBean") IoReportBean ioReportBean) {
        ModelAndView mav = new ModelAndView();
        int daioid = discProcedDAO.getRefidFromTaskid(ioReportBean.getTaskId());
        IoReportBean tiobean = discProcedDAO.getIODetails(daioid);
        int daid = tiobean.getDaId();
        List delinquentOfficerList = discProcedDAO.getDelinquentOfficer(daid);
        DefenceBean defencebean = discProcedDAO.getDefenceDetailFromDaid(daid);
        List discchargelist = discProcedDAO.getDiscChargeList(daid);

        List disccharge = discProcedDAO.getDiscChargeList(daid);

        DiscChargeBean discChargeBean = new DiscChargeBean();
        discChargeBean.setDaId(daid);
        mav.addObject("taskdtls", taskDAO.getTaskDetails(ioReportBean.getTaskId()));
        mav.addObject("delinquentOfficerList", delinquentOfficerList);
        mav.addObject("defencebean", defencebean);
        mav.addObject("discChargeBean", discChargeBean);
        mav.addObject("discchargelist", discchargelist);
        mav.addObject("articleOfChargeList", disccharge);
        mav.addObject("dpviewbean", discProcedDAO.getDpdetail(daid));

        ioReportBean.setDaioid(daioid);
        ArrayList investingreport = discProcedDAO.getIoReportList(ioReportBean.getDaioid());
        mav.addObject("investingreport", investingreport);

        //mav.addObject("taskdtls", taskDAO.getTaskDetails(ioReportBean.getTaskId()));
        mav.setViewName("/discProceeding/ioReportBy");
        return mav;
    }

    @RequestMapping(value = "viewIoReport", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Submit Remark"})
    public ModelAndView SubmitRemark(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ioReportBean") IoReportBean ioReportBean) {
        ModelAndView mav = new ModelAndView();
        int refid = discProcedDAO.getRefidFromTaskid(ioReportBean.getTaskId());

        ioReportBean.setDaioid(refid);
        //ArrayList investingreport = discProcedDAO.getIoReportList(ioReportBean.getDaioid());
        //mav.addObject("investingreport", investingreport);

        //discProcedDAO.updateIoRemark(ioReportBean);
        discProcedDAO.submitIOReportBriefing(ioReportBean);
        taskDAO.closeTask(ioReportBean.getTaskId());

        mav.setViewName("redirect:/viewIoReport.htm?taskId=" + ioReportBean.getTaskId());
        return mav;
    }

    @RequestMapping(value = "viewIoReport", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Add Exihibits"})
    public ModelAndView AddExihibits(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ioReportBean") IoReportBean ioReportBean) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/discProceeding/ioReportDetails");
        return mav;
    }

    @RequestMapping(value = "viewIoReport", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Submit"})
    public ModelAndView SubmitExihibits(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ioReportBean") IoReportBean ioReportBean) {
        ModelAndView mav = new ModelAndView();

        taskDAO.closeTask(ioReportBean.getTaskId());
        discProcedDAO.submitIoReportDetails(ioReportBean);
        mav.setViewName("/discProceeding/submitIoreportDetails");
        return mav;
    }

    @RequestMapping(value = "viewIoReport", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Save"})
    public ModelAndView SaveIoReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ioReportBean") IoReportBean ioReportBean) {
        ModelAndView mav = new ModelAndView();
        int ioremarkid = discProcedDAO.saveIoReportDetails(ioReportBean);

        if (!ioReportBean.getIoDocument().isEmpty()) {
            employeeDAO.uploadAttachedFile(ioremarkid, "IOSTMT", ioReportBean.getIoDocument());
        }
        mav.setViewName("redirect:/viewIoReport.htm?taskId=" + ioReportBean.getTaskId());
        return mav;
    }
    /*When the Initiating Authority click on Back Button of  */

    @RequestMapping(value = "viewIoReport", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Back"})
    public ModelAndView BackIoReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ioReportBean") IoReportBean ioReportBean, @RequestParam("taskId") int taskId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/viewIoReport.htm?taskId=" + ioReportBean.getTaskId());
        return mav;
    }
    /*When Initiating Authority sends the Whole detail report including IO Report*/

    @RequestMapping(value = "FinalReport", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView FinalReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("daId") int daId, @ModelAttribute("noticeBean") NoticeBean noticeBean) {
        ModelAndView mav = new ModelAndView();

        //int daid = discProcedDAO.getDaidAndDefIdFromTaskid(proceedingbean.getTaskId());
        List delinquentOfficerList = discProcedDAO.getDelinquentOfficer(daId);
        DefenceBean defencebean = discProcedDAO.getDefenceDetailFromDaid(daId);
        List discchargelist = discProcedDAO.getDiscChargeListWithWitness(daId);
        List disccharge = discProcedDAO.getDiscChargeList(daId);
        ArrayList investingreport = discProcedDAO.getIoDetailList();
        ArrayList investingreports = discProcedDAO.getIoDetailListfromdaid(daId);
        List noticeBeanList = discProcedDAO.getReplyNotice1(daId);

        mav.addObject("discchargelist", discchargelist);
        mav.addObject("delinquentOfficerList", delinquentOfficerList);
        mav.addObject("defencebean", defencebean);
        mav.addObject("articleOfChargeList", disccharge);
        //ArrayList investingreport = discProcedDAO.getIoReportList(daioid);
        mav.addObject("dpviewbean", discProcedDAO.getDpdetail(daId));
        mav.addObject("investingreport", investingreport);
        mav.addObject("investingreports", investingreports);
        mav.setViewName("/discProceeding/Report");
        mav.addObject("noticeBeanList", noticeBeanList);
        return mav;

    }

    @RequestMapping(value = "FinalReport", params = {"action=Final Order"})
    public ModelAndView FinalReport1(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("noticeBean") NoticeBean noticeBean) {
        ModelAndView mav = new ModelAndView();

        List penalty = discProcedDAO.getPenaltyList(noticeBean);
        DpViewBean viewbean = discProcedDAO.viewRule15DiscProceeding(lub.getLoginoffcode(), noticeBean.getDaId() + "", noticeBean.getTaskId());
        ArrayList investingOfficerList = discProcedDAO.getInvestingOfficer(noticeBean.getDaId());
        mav.addObject("penalty", penalty);
        mav.addObject("dpviewbean", viewbean);
        mav.addObject("viewbean", viewbean);

        mav.addObject("investingOfficerList", investingOfficerList);
        // session.setAttribute("investingOfficer", investingOfficer);
        mav.setViewName("/discProceeding/finalOrder");

        return mav;

    }

    @RequestMapping(value = "FinalReport", params = {"action=Add Penalty"})
    public ModelAndView Penaltyadd(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("noticeBean") NoticeBean noticeBean) {
        ModelAndView mav = new ModelAndView();
        List penalty = discProcedDAO.getPenaltyList(noticeBean);
        mav.addObject("penalty", penalty);
        mav.setViewName("/discProceeding/penaltyAdd");
        return mav;

    }

    @RequestMapping(value = "FinalReport", params = {"action=Approve Order"})
    public ModelAndView ApproveOrder(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("noticeBean") NoticeBean noticeBean, @ModelAttribute("finalOrder") FinalOrder finalOrder) {
        ModelAndView mav = new ModelAndView();
        discProcedDAO.sendFinalOrder(finalOrder);

        mav.setViewName("/discProceeding/Approve Oreder Succes");

        return mav;

    }

    @RequestMapping(value = "FinalReport", params = {"action=Submit"})
    public ModelAndView submitPenalty(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("noticeBean") NoticeBean noticeBean) {
        ModelAndView mav = new ModelAndView();
        DpViewBean viewbean = discProcedDAO.viewRule15DiscProceeding(lub.getLoginoffcode(), noticeBean.getDaId() + "", noticeBean.getTaskId());
        ArrayList investingOfficerList = discProcedDAO.getInvestingOfficer(noticeBean.getDaId());
        discProcedDAO.savePenalty(noticeBean);
        List penalty = discProcedDAO.getPenaltyList(noticeBean);
        mav.addObject("viewbean", viewbean);
        mav.addObject("dpviewbean", viewbean);
        mav.addObject("investingOfficerList", investingOfficerList);
        mav.addObject("penalty", penalty);
        mav.setViewName("/discProceeding/finalOrder");
        return mav;

    }
    /*When the Disciplinary Authority View Notice1 of Delinquent Officer*/

    @RequestMapping(value = "viewNotice", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView viewNotice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("noticeBean") NoticeBean noticeBean) {
        ModelAndView mav = new ModelAndView();
        int daId = discProcedDAO.getRefidFromTaskid(noticeBean.getTaskId());
        noticeBean.setDaId(daId);

        DpViewBean viewbean = discProcedDAO.viewRule15DiscProceeding(lub.getLoginoffcode(), noticeBean.getDaId() + "", noticeBean.getTaskId());
        ArrayList investingOfficerList = discProcedDAO.getInvestingOfficer(noticeBean.getDaId());
        mav.addObject("dpviewbean", viewbean);
        mav.addObject("viewbean", viewbean);
        mav.addObject("investingOfficerList", investingOfficerList);
        mav.addObject("taskdtls", taskDAO.getTaskDetails(noticeBean.getTaskId()));
        mav.setViewName("/discProceeding/Notice1");
        return mav;
    }
    /*When the Disciplinary Authority send Notice1 to Delinquent Officer*/

    @RequestMapping(value = "sendNotice", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Send Notice-1"})
    public ModelAndView sendNotice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("discChargeBean") DiscChargeBean discChargeBean, @ModelAttribute("noticeBean") NoticeBean noticeBean) {
        ModelAndView mav = new ModelAndView();

        discProcedDAO.sendNotice1(noticeBean);
        mav.setViewName("/discProceeding/Notice1Sucess");
        return mav;
    }
    /*When the Disciplinary Authority View Notice2 of Delinquent Officer*/

    @RequestMapping(value = "viewNotice2", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView viewNotice2(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("discChargeBean") DiscChargeBean discChargeBean) {
        ModelAndView mav = new ModelAndView();

        int daId = discProcedDAO.getRefidFromTaskid(discChargeBean.getTaskId());
        discChargeBean.setDaId(daId);
        DpViewBean viewbean = discProcedDAO.viewRule15DiscProceeding(lub.getLoginoffcode(), discChargeBean.getDaId() + "", discChargeBean.getTaskId());
        ArrayList investingOfficerList = discProcedDAO.getInvestingOfficer(discChargeBean.getDaId());
        mav.addObject("taskdtls", taskDAO.getTaskDetails(discChargeBean.getTaskId()));
        mav.addObject("dpviewbean", viewbean);
        mav.addObject("viewbean", viewbean);
        mav.addObject("investingOfficerList", investingOfficerList);
        mav.setViewName("/discProceeding/Notice2");

        return mav;
    }
    /*When the Disciplinary Authority send Notice2 to Delinquent Officer*/

    @RequestMapping(value = "sendNotice", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Send Notice-2"})
    public ModelAndView sendNotice2(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("discChargeBean") DiscChargeBean discChargeBean, @ModelAttribute("noticeBean") NoticeBean noticeBean) {
        ModelAndView mav = new ModelAndView();
        discProcedDAO.sendNotice2(noticeBean);

        mav.setViewName("/discProceeding/Notice2Sucess");

        return mav;
    }
    /*When the Delinquent oficer reply to the Notice1 of disciplinary Authority*/

    @RequestMapping(value = "sendNotice", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Submit Reply"})
    public ModelAndView saveNotice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("discChargeBean") DiscChargeBean discChargeBean, @ModelAttribute("noticeBean") NoticeBean noticeBean) {
        ModelAndView mav = new ModelAndView();
        noticeBean.setEmpHrmsId(lub.getLoginempid());
        discProcedDAO.saveReplyNotice1(noticeBean);
        discProcedDAO.sendFYI(noticeBean);
        taskDAO.closeTask(noticeBean.getTaskId());

        mav.setViewName("/discProceeding/replynotice1Sucess");
        return mav;
    }

    @RequestMapping(value = "viewNotice1Reply", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView viewNotice1Reply(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("noticeBean") NoticeBean noticeBean) {
        ModelAndView mav = new ModelAndView();
        int dadId = discProcedDAO.getRefidFromTaskid(noticeBean.getTaskId());
        noticeBean.setDadid(dadId);
        List noticeBeanList = discProcedDAO.getReplyNotice1(noticeBean.getDadid());
        mav.addObject("noticeBeanList", noticeBeanList);
        mav.setViewName("/discProceeding/Notice1Reply");
        return mav;
    }

    @RequestMapping(value = "viewNotice2Reply", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView viewNotice2Reply(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("noticeBean") NoticeBean noticeBean) {
        ModelAndView mav = new ModelAndView();
        int dadId = discProcedDAO.getRefidFromTaskid(noticeBean.getTaskId());
        noticeBean.setDadid(dadId);
        List noticeBeanList = discProcedDAO.getReplyNotice2(noticeBean.getDadid());
        mav.addObject("noticeBeanList", noticeBeanList);
        mav.setViewName("/discProceeding/Notice2Reply");
        return mav;
    }

    /*When the Delinquent oficer reply to the Notice2 of disciplinary Authority*/
    @RequestMapping(value = "sendNotice", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Submit Reply of Notice2"})
    public ModelAndView saveNotice2(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("discChargeBean") DiscChargeBean discChargeBean, @ModelAttribute("noticeBean") NoticeBean noticeBean) {
        ModelAndView mav = new ModelAndView();
        noticeBean.setEmpHrmsId(lub.getLoginempid());
        discProcedDAO.saveReplyNotice2(noticeBean);
        discProcedDAO.sendFYI2(noticeBean);
        taskDAO.closeTask(noticeBean.getTaskId());

        mav.setViewName("/discProceeding/replynotice2Success");

        return mav;
    }

    /*@RequestMapping(value = "replynotice1", method = {RequestMethod.POST, RequestMethod.GET})
     public ModelAndView replynotice1(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("discChargeBean") DiscChargeBean discChargeBean, @ModelAttribute("noticeBean") NoticeBean noticeBean) {
     ModelAndView mav = new ModelAndView();
     discProcedDAO.saveReplyNotice1(noticeBean);

     mav.setViewName("/discProceeding/Notice1");

     return mav;
     }*/
    /*Use to make the pdf of discapproved.jsp page when the higher Authority clicks on the Approved Button*/
    @RequestMapping(value = "discMemoPDF.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public void DiscMemoPDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pbean") ProceedingBean proceedingbean) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {

            int daid = discProcedDAO.getDaidFromTaskid(proceedingbean.getTaskId());
            if (daid == 0) {
                daid = discProcedDAO.getRefidFromTaskid(proceedingbean.getTaskId());
            }

            DpViewBean viewbean = discProcedDAO.viewRule15DiscProceeding(lub.getLoginoffcode(), daid + "", proceedingbean.getTaskId());

            response.setHeader("Content-Disposition", "inline; filename=Memorandum.pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            //viewbean.setDaId(daid);            
            discProcedDAO.discApprovedPDF(document, viewbean);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "discFinalNoticePDF.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public void discFinalNoticePDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pbean") ProceedingBean proceedingbean, @ModelAttribute("noticeBean") NoticeBean noticeBean) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            if (proceedingbean.getDaId() == 0) {
                proceedingbean.setDaId(discProcedDAO.getRefidFromTaskid(proceedingbean.getTaskId()));
            }

            DpViewBean viewbean = discProcedDAO.viewRule15DiscProceeding(lub.getLoginoffcode(), proceedingbean.getDaId() + "", proceedingbean.getTaskId());

            response.setHeader("Content-Disposition", "inline; filename=discFinalNotice.pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            //viewbean.setDaId(daid);  
            discProcedDAO.discFinalNoticePDF(document, viewbean);
            //discProcedDAO.discApprovedPDF(document, viewbean);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    /**
     * **************************************************
     ***********************New Proceeding ***************
     * **************************************************
     */

    /*view Controller Forward to Disciplinary Authority For Approval*/
    @RequestMapping(value = "forwardRule15DiscProceding")
    public ModelAndView forwardRule15DiscProceding(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ForwardDiscChargeBean") ForwardDiscChargeBean chargebean) {
        ModelAndView mav = new ModelAndView();

        mav.addObject("ForwardDiscChargeBean", new ForwardDiscChargeBean());
        mav.addObject("departmentList", departmentDAO.getDepartmentList());
        mav.addObject("officeList", officeDao.getTotalOfficeList(chargebean.getDeptCode()));
        mav.addObject("postList", postDAO.getPostList(chargebean.getDeptCode()));
        chargebean.setProcessId(11);
        mav.addObject("emplist", substantivePostDAO.getEmployeeNameWithSPC(chargebean.getOffCode(), chargebean.getPostCode()));
        mav.addObject("ForwardDiscChargeBean", chargebean);
        mav.addObject("tempPostCode", chargebean.getPostCode());
        mav.setViewName("/discProceeding/forwardRule15DiscProceding");

        return mav;
    }
    /*Forward to Disciplinary Authority For Approval*/

    @RequestMapping(value = "forwardRule15DiscProceding", method = {RequestMethod.POST}, params = {"action=Submit"})
    public ModelAndView initiatedDepartmentproceeding(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SubmitProceedingBean") SubmitProceedingBean proceedingbean) {
        ModelAndView mav = new ModelAndView();
        proceedingbean.setInitiatedBy(lub.getLoginempid());
        proceedingbean.setInitiatedSpc(lub.getLoginspc());
        discProcedDAO.saveinitiatedDepartmentproceeding(proceedingbean);
        mav.setViewName("/discProceeding/SubmitDepartmentProceeding");
        return mav;
    }

    @RequestMapping(value = "forwardRule15DiscProceding", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView backRule15DiscProceding(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ForwardDiscChargeBean") ForwardDiscChargeBean chargebean) {
        ModelAndView mav = new ModelAndView("/discProceeding/discProcedFinalList");
        List dpEmpList = discProcedDAO.getDiscProcedingFinalList(lub.getLoginempid(), 1, 10);
        mav.addObject("dpEmpList", dpEmpList);
        return mav;
    }

    /*Use to back from Annexure1,annexure2,annexure2,annexure2 page of Higher Disciplinary Authority*/
    @RequestMapping(value = "discApprovedAction.htm", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Back"})
    public ModelAndView backannextureIcharge(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pbean") ProceedingBean proceedingbean) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/discApprovedAction.htm?taskId=" + proceedingbean.getTaskId());
        return mav;
    }

    /*Annexture3 page of Disciplinary Authority*/
    @RequestMapping(value = "discApprovedAction.htm", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Article Of Charge"})
    public ModelAndView annextureIIIcharge(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pbean") DiscChargeBean chargebean) {
        ModelAndView mav = new ModelAndView();
        int daid = discProcedDAO.getDaidFromTaskid(chargebean.getTaskId());
        List disccharge = discProcedDAO.getDiscChargeList(daid);
        mav.addObject("articleOfChargeList", disccharge);
        mav.setViewName("/discProceeding/annexure3");
        return mav;
    }

}
