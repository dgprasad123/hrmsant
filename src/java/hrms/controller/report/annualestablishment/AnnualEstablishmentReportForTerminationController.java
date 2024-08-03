package hrms.controller.report.annualestablishment;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.report.annualestablishment.AnnuaiEstablishmentReportDAO;
import hrms.dao.report.posttermination.PostTerminationDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.report.annualestablishmentreport.AnnualEstablishment;
import hrms.model.report.annualestablishmentreport.AnnualEstablishmentReportPostTerminatonForm;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("LoginUserBean")
public class AnnualEstablishmentReportForTerminationController {

    @Autowired
    AnnuaiEstablishmentReportDAO annuaiEstablishmentDao;

    @Autowired
    PostTerminationDAO postTerminationDAO;

    @Autowired
    public DepartmentDAO deptDAO;

    @RequestMapping(value = "PostTerminationCOScheduleII", params = {"btnPTAer=GetList"})
    public ModelAndView PostTerminationCOScheduleII(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishmentReportPostTerminatonForm ae) {

        ModelAndView mav = new ModelAndView();

        try {
            String fyear = ae.getFinancialYear();
            if (fyear == null || fyear.equals("")) {
                List terminationlist = new ArrayList();
                
                mav.addObject("cntRecord", 1);
                mav.addObject("PartAGrouplist", terminationlist);
            } else {
                ArrayList terminationlist = postTerminationDAO.getScheduleIIPostTerminationList(lub.getLoginoffcode(), lub.getLoginspc(), ae.getFinancialYear());
                mav.addObject("terminationlist", terminationlist);
                mav.addObject("cntRecord", terminationlist.size());

            }
            mav.addObject("financialYear", fyear);
            mav.addObject("officename", lub.getLoginoffname());

            mav.setViewName("/report/aerposttermination/AERScheduleIIPostTerminationList");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "PostTerminationCOScheduleII", params = {"btnPTAer=Create"})
    public ModelAndView PostTerminationCOScheduleIICreate(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishmentReportPostTerminatonForm ae) {

        ModelAndView mav = new ModelAndView();

        try {
            ArrayList scheduleIIPostTerminationList = postTerminationDAO.getScheduleIIPostTerminationPostList(lub.getLoginoffcode(), lub.getLoginspc());
            mav.addObject("scheduleIIPostTerminationList", scheduleIIPostTerminationList);

            mav.addObject("officename", lub.getLoginoffname());

            ArrayList terminationlist = postTerminationDAO.getScheduleIIPostTerminationList(lub.getLoginoffcode(), lub.getLoginspc(), "2020-21");
            mav.addObject("cntRecord", terminationlist.size());
            mav.addObject("financialYear", ae.getFinancialYear());
            
            if (ae.getEditPropsalId() != null && !ae.getEditPropsalId().equals("")) {
                mav.addObject("editPropsalId", ae.getEditPropsalId());

            } else {
                mav.addObject("editPropsalId", "NA");
            }

            mav.setViewName("/report/aerposttermination/AERScheduleIIPostTerminationPostList");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "PostTerminationCOScheduleII", params = {"btnPTAer=Next"})
    public ModelAndView PostTerminationCOScheduleIINext(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishmentReportPostTerminatonForm ae) {

        ModelAndView mav = new ModelAndView();

        try {
            String checkBoxValue = ae.getChkTerminatedPost();
            if (ae.getEditPropsalId() != null && !ae.getEditPropsalId().equals("")) {
                mav.addObject("editPropsalId", ae.getEditPropsalId());

            } else {
                mav.addObject("editPropsalId", "");
            }
            
            if (checkBoxValue.equals("NA")) {
                mav.setViewName("redirect:/PostTerminationNOCOScheduleII.htm?financialYear" + ae.getFinancialYear());
                String fyear = ae.getFinancialYear();
                String aoOffCode = annuaiEstablishmentDao.getAoOffCode(lub.getLogindeptcode());
                postTerminationDAO.saveTerminationNoDataSchedule2(fyear, lub.getLoginoffcode(), lub.getLoginempid(), lub.getLoginspc(), aoOffCode, ae.getEditPropsalId());
                mav.addObject("financialYear", ae.getFinancialYear());
            } else {
                
                ArrayList checkedPostList = postTerminationDAO.getScheduleIIPostTerminationCheckedList(lub.getLoginoffcode(), ae);
                mav.addObject("checkedPostList", checkedPostList);

                mav.addObject("officename", lub.getLoginoffname());
                mav.addObject("financialYear", ae.getFinancialYear());

                mav.setViewName("/report/aerposttermination/AERAddScheduleIIPostTerminationData");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "PostTerminationCOScheduleII", params = {"btnPTAer=Send"})
    public String PostTerminationCOScheduleIISendToAO(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishmentReportPostTerminatonForm ae) {

        try {

            String aoOffCode = annuaiEstablishmentDao.getAoOffCode(lub.getLogindeptcode());

            postTerminationDAO.insertPostTerminationAtCOData(lub.getLoginoffcode(), lub.getLoginempid(), lub.getLoginspc(), aoOffCode, ae);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/PostTerminationCOScheduleII.htm?btnPTAer=GetList&financialYear=" + ae.getFinancialYear();
    }

    @RequestMapping(value = "PostTerminationCOScheduleIIBack")
    public String PostTerminationCOScheduleIIBack(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishmentReportPostTerminatonForm ae) {
        return "redirect:/PostTerminationCOScheduleII.htm?btnPTAer=GetList&financialYear=" + ae.getFinancialYear();
    }

    @RequestMapping(value = "viewPostTerminationScheduleII")
    public ModelAndView ViewPostTerminationScheduleII(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("propsalId") int propsalId, @RequestParam("type") String type, @ModelAttribute("command") AnnualEstablishmentReportPostTerminatonForm ae) {

        ModelAndView mav = new ModelAndView();

        try {
            ArrayList viewlist = postTerminationDAO.viewPostTerminationScheduleII(propsalId);
            mav.addObject("viewlist", viewlist);

            mav.addObject("officename", lub.getLoginoffname());
            mav.addObject("financialYear", ae.getFinancialYear());
            mav.addObject("type", type);

            mav.setViewName("report/aerposttermination/AERViewPostTerminationScheduleII");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "COWiseSubmittedPostTerminationList")
    public ModelAndView COWiseSubmittedPostTerminationList(@ModelAttribute("command") AnnualEstablishmentReportPostTerminatonForm ae) {

        ModelAndView mav = new ModelAndView();

        try {
            if (ae.getFy() != null && !ae.getFy().equals("")) {
                ArrayList postterminationcolist = postTerminationDAO.coWiseSubmittedPostTerminationList(ae.getFy());
                mav.addObject("postterminationcolist", postterminationcolist);
            }

            mav.setViewName("report/aerposttermination/COWiseSubmittedPostTerminationList");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "PostTerminationNOCOScheduleII")
    public ModelAndView PostTerminationNOCOScheduleII(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishmentReportPostTerminatonForm ae) {

        ModelAndView mav = new ModelAndView();

        try {
            ArrayList terminationlist = postTerminationDAO.getScheduleIIPostTerminationList(lub.getLoginoffcode(), lub.getLoginspc(), ae.getFinancialYear());
            mav.addObject("terminationlist", terminationlist);
            mav.addObject("cntRecord", terminationlist.size());

            mav.addObject("officename", lub.getLoginoffname());
            mav.addObject("financialYear", ae.getFinancialYear());

            mav.setViewName("/report/aerposttermination/AERScheduleIIPostTerminationList");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "AOViewPostTerminationList")
    public ModelAndView AOViewPostTerminationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishmentReportPostTerminatonForm ae) {

        ModelAndView mav = new ModelAndView();

        try {
            if (ae.getFy() != null && !ae.getFy().equals("")) {
                ArrayList postterminationcolist = postTerminationDAO.aoViewSubmittedPostTerminationList(lub.getLoginoffcode(), ae.getFy());
                mav.addObject("postterminationcolist", postterminationcolist);
            }
            String fyear = ae.getFy();
            mav.addObject("financialYear", fyear);
            mav.setViewName("report/aerposttermination/AOPostTerminationList");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "downloadPDFScheduleIIA")
    public void downloadPDFScheduleIIA(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("propsalId") int propsalId, @RequestParam("financialYear") String fy, @ModelAttribute("command") AnnualEstablishmentReportPostTerminatonForm ae) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        PdfWriter writer = null;

        try {
            response.setHeader("Content-Disposition", "attachment; filename=SCHEDULEII_A_" + propsalId + ".pdf");
            writer = PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            postTerminationDAO.downloadPDFScheduleIIA(document, propsalId, lub.getLoginoffname(), fy);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "listingTerminationPostSchedule3")
    public ModelAndView listingTerminationPostSchedule3(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("command") AnnualEstablishmentReportPostTerminatonForm ae, @RequestParam Map<String, String> param) {
        ModelAndView mv = new ModelAndView();
        List grpAList = new ArrayList();
        String fyear = ae.getFinancialYear();
        if (fyear == null || fyear.equals("")) {
            
            mv.addObject("cntRecord", 1);
            mv.addObject("PartAGrouplist", grpAList);
        } else {
            
            grpAList = postTerminationDAO.getTerminationPostSchedule3(fyear, lub.getLoginoffcode());
            mv.addObject("cntRecord", grpAList.size());
            mv.addObject("PartAGrouplist", grpAList);
        }
        mv.addObject("financialYear", fyear);
        mv.addObject("OffName", lub.getLoginoffname());

        mv.setViewName("/report/listingTerminationPostSchedule3");
        return mv;
    }

    @RequestMapping(value = "aerTerminationPostSchedule3")
    public ModelAndView aerTerminationPostSchedule3(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {
        ModelAndView mv = new ModelAndView();
        List grpAList = new ArrayList();
        List totalRecord = new ArrayList();
        
        String fyear = ae.getFinancialYear();
        grpAList = postTerminationDAO.getAERPOstDetailsSchedule3(lub.getLoginoffcode());
        
        mv.addObject("PartAGrouplist", grpAList);
        mv.addObject("OffName", lub.getLoginoffname());
        mv.addObject("financialYear", fyear);
        totalRecord = postTerminationDAO.getTerminationPostSchedule3(fyear, lub.getLoginoffcode());
        mv.addObject("cntRecord", totalRecord.size());

        mv.setViewName("/report/aerTerminationPostSchedule3");
        return mv;
    }

    @RequestMapping(value = "displayPostSchedule3")
    public ModelAndView displaySchedule1PostSchedule3(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> param) {
        ModelAndView mv = new ModelAndView();
        List grpAList = new ArrayList();
        String fyear = ae.getFinancialYear();

        String checkBoxValue = ae.getChkReportId();
        if (checkBoxValue.equals("NA")) {
            mv.setViewName("redirect:/listingTerminationPostSchedule3.htm");
            postTerminationDAO.saveTerminationNoDataSchedule3(fyear, lub.getLoginoffcode(), lub.getLoginempid(), lub.getLoginspc());
            mv.addObject("financialYear", fyear);
        } else {

            grpAList = postTerminationDAO.getAERCheckedPOstDetailsSchedule3(fyear, lub.getLoginoffcode(), ae.getChkReportId());
            mv.addObject("PartAGrouplist", grpAList);
            List deptlist = deptDAO.getDepartmentList();
            mv.addObject("deptlist", deptlist);
            mv.addObject("OffName", lub.getLoginoffname());
            mv.addObject("financialYear", fyear);
            mv.setViewName("/report/displayPostSchedule3");
        }
        return mv;
    }

    @RequestMapping(value = "sentSchedule3Post")
    public ModelAndView sentSchedule3Post(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> param) {
        ModelAndView mv = new ModelAndView("redirect:/listingTerminationPostSchedule3.htm");
        List grpAList = new ArrayList();
        String fyear = ae.getFinancialYear();
        ae.setFy(fyear);
        mv.addObject("financialYear", fyear);
        ae.setOffCode(lub.getLoginoffcode());
        ae.setEmpId(lub.getLoginempid());
        ae.setEmpPost(lub.getLoginspc());
        mv.addObject("OffName", lub.getLoginoffname());
        postTerminationDAO.saveTerminationDataschedule3(ae);

        return mv;
    }

    @RequestMapping(value = "viewTerminationPOstSchedule3")
    public ModelAndView viewTerminationPOstSchedule3(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> param) {
        ModelAndView mv = new ModelAndView();
        List grpAList = new ArrayList();
        String fyear = ae.getFinancialYear();
        grpAList = postTerminationDAO.getviewTSchedule3(fyear, lub.getLoginoffcode(), Integer.parseInt(param.get("termId")));
        mv.addObject("PartAGrouplist", grpAList);

        mv.addObject("OffName", lub.getLoginoffname());
        mv.addObject("financialYear", fyear);
        mv.setViewName("/report/viewTerminationPOstSchedule3");
        return mv;
    }

    @RequestMapping(value = "listingTerminationPostFD")
    public ModelAndView listingTerminationPostFD(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> param) {
        ModelAndView mv = new ModelAndView();
        List grpAList = new ArrayList();
        String fyear = ae.getFinancialYear();
        if (fyear == null || fyear.equals("")) {
            fyear = "2020-21";
        }
        grpAList = postTerminationDAO.getTerminationPostFD(fyear);
        mv.addObject("PartAGrouplist", grpAList);
        mv.addObject("financialYear", fyear);
        mv.setViewName("/report/listingTerminationPostFD");
        return mv;
    }

    @RequestMapping(value = "viewDeptWiseTermination")
    public ModelAndView viewDeptWiseTermination(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("command") AnnualEstablishment ae, @RequestParam("offCode") String offCode, @RequestParam("financialYear") String fyear, @RequestParam("departmentname") String departmentname) {
        ModelAndView mv = new ModelAndView();
        List grpAList = new ArrayList();

        grpAList = postTerminationDAO.viewDeptWiseTermination(offCode, fyear);
        mv.addObject("PartAGrouplist", grpAList);
        mv.addObject("financialYear", fyear);
        mv.addObject("departmentname", departmentname);
        mv.setViewName("/report/viewDeptWiseTermination");
        return mv;
    }

    @RequestMapping(value = "COWiseDeptPostTerminationStatus")
    public ModelAndView COWiseDeptPostTerminationStatus(ModelMap model, HttpServletResponse response) {
        String path = "report/aerposttermination/COWiseDeptPostTerminationStatus";
        ModelAndView mav = new ModelAndView();
        ArrayList AERDetails = postTerminationDAO.COWiseDeptPostTerminationStatus();
        mav.addObject("AERDetails", AERDetails);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "downloadPDFScheduleIIIA")
    public void downloadPDFScheduleIIIA(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("termId") int termId, @RequestParam("financialYear") String fy, @ModelAttribute("command") AnnualEstablishmentReportPostTerminatonForm ae) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        PdfWriter writer = null;

        try {
            response.setHeader("Content-Disposition", "attachment; filename=SCHEDULEIII_A_" + termId + ".pdf");
            writer = PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            postTerminationDAO.downloadPDFScheduleIIIA(document, termId, lub.getLoginoffname(), lub.getLoginoffcode(), fy);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "listingTerminationPost")
    public ModelAndView listingTerminationPost(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> param) {
        ModelAndView mv = new ModelAndView();
        List grpAList = new ArrayList();
        String fyear = ae.getFinancialYear();
        mv.addObject("OffName", lub.getLoginoffname());
        if (fyear == null || fyear.equals("")) {
            
            mv.addObject("cntRecord", 1);
            mv.addObject("PartAGrouplist", grpAList);
        } else {
            
            grpAList = postTerminationDAO.getlistingTerminationPost(fyear, lub.getLoginoffcode());
            mv.addObject("cntRecord", grpAList.size());
            mv.addObject("PartAGrouplist", grpAList);
        }

        mv.addObject("financialYear", fyear);
        mv.setViewName("/report/listingTerminationPost");
        return mv;
    }

    @RequestMapping(value = "sentSchedule1Post")
    public ModelAndView sentSchedule1Post(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> param) {
        ModelAndView mv = new ModelAndView("redirect:/listingTerminationPost.htm");
        List grpAList = new ArrayList();
        String fyear = ae.getFinancialYear();
        ae.setFy(fyear);
        ae.setOffCode(lub.getLoginoffcode());
        ae.setEmpId(lub.getLoginempid());
        ae.setEmpPost(lub.getLoginspc());
        mv.addObject("financialYear", fyear);

        
        mv.addObject("OffName", lub.getLoginoffname());
        postTerminationDAO.saveTerminationData(ae);

        return mv;
    }

    @RequestMapping(value = "viewTerminationPOstSchedule1")
    public ModelAndView viewTerminationPOstSchedule1(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> param) {
        ModelAndView mv = new ModelAndView();
        List grpAList = new ArrayList();
        String fyear = ae.getFinancialYear();
        mv.addObject("financialYear", fyear);
        grpAList = postTerminationDAO.getviewTSchedule1(fyear, lub.getLoginoffcode(), Integer.parseInt(param.get("termId")));
        mv.addObject("PartAGrouplist", grpAList);

        mv.addObject("OffName", lub.getLoginoffname());
        mv.setViewName("/report/viewTerminationPOstSchedule1");
        return mv;
    }

    @RequestMapping(value = "displaySchedule1Post")

    public ModelAndView displaySchedule1Post(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> param) {
        ModelAndView mv = new ModelAndView();
        List grpAList = new ArrayList();
        String fyear = ae.getFinancialYear();
        String checkBoxValue = ae.getChkGpc();
        if (checkBoxValue.equals("NA")) {
            mv.setViewName("redirect:/listingTerminationPost.htm");
            postTerminationDAO.saveTerminationNoDataSchedule1(fyear, lub.getLoginoffcode(), lub.getLoginempid(), lub.getLoginspc());
            mv.addObject("financialYear", fyear);
        } else {
            

            grpAList = postTerminationDAO.getAERCheckedPOstDetails(fyear, lub.getLoginoffcode(), ae.getChkGpc());
            mv.addObject("PartAGrouplist", grpAList);
            List deptlist = deptDAO.getDepartmentList();
            mv.addObject("deptlist", deptlist);
            mv.addObject("OffName", lub.getLoginoffname());
            mv.addObject("financialYear", fyear);
            mv.setViewName("/report/displaySchedule1Post");
        }
        return mv;
    }

    @RequestMapping(value = "aerTerminationPost")

    public ModelAndView aerTerminationPost(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {
        ModelAndView mv = new ModelAndView();
        String fyear = ae.getFinancialYear();
        List grpAList = new ArrayList();
        List totalRecord = new ArrayList();
        grpAList = postTerminationDAO.getAERPOstDetails(lub.getLoginoffcode(),fyear);
        
        mv.addObject("PartAGrouplist", grpAList);
        mv.addObject("OffName", lub.getLoginoffname());
        totalRecord = postTerminationDAO.getlistingTerminationPost(fyear, lub.getLoginoffcode());
        mv.addObject("cntRecord", totalRecord.size());
        mv.addObject("financialYear", fyear);
        mv.setViewName("/report/aerTerminationPost");
        return mv;
    }

    @RequestMapping(value = "downloadPDFScheduleIA")
    public void downloadPDFScheduleIA(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("termId") int termId, @RequestParam("financialYear") String fy, @ModelAttribute("command") AnnualEstablishmentReportPostTerminatonForm ae) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        PdfWriter writer = null;

        try {
            response.setHeader("Content-Disposition", "attachment; filename=SCHEDULE_I_A_" + termId + ".pdf");
            writer = PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            postTerminationDAO.downloadPDFScheduleIA(document, termId, lub.getLoginoffname(), lub.getLoginoffcode(), fy);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "AOWiseTerminationStatus", params = {"btnAer=Approve"})
    public String approvePostTerminationForCO(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {
        String path = "redirect:/AOViewPostTerminationList.htm?fy=" + ae.getFinancialYear();
        postTerminationDAO.approvePostTerminationForCO(ae.getProposalId(), ae.getFinancialYear());
        return path;
    }

    @RequestMapping(value = "AOWiseTerminationStatus", params = {"btnAer=Decline"})
    public String declinePostTerminationForCO(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {
        
        String path = "redirect:/AOViewPostTerminationList.htm?fy=" + ae.getFinancialYear();
        postTerminationDAO.declinePostTerminationForCO(ae.getProposalId(), ae.getFinancialYear(), ae.getRevertReason());
        return path;
    }

    @RequestMapping(value = "AOWiseSummaryReport")
    public ModelAndView AOWiseSummaryReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishmentReportPostTerminatonForm ae) {

        ModelAndView mav = new ModelAndView();
        
        try {
            if (ae.getFy() != null && !ae.getFy().equals("")) {
                ArrayList postterminationcolist = postTerminationDAO.AOWiseSummaryReport(lub.getLoginoffcode(), ae.getFy());
                mav.addObject("postterminationcolist", postterminationcolist);
                String FDStatus= postTerminationDAO.coFDStatus(lub.getLoginoffcode(), ae.getFy());
                 mav.addObject("FDStatus", FDStatus);
            }
            String fyear = ae.getFy();
            mav.addObject("financialYear", fyear);
            mav.setViewName("report/aerposttermination/AOWiseSummaryReport");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "SubmitConsolidatedDataToFD")
    public ModelAndView SubmitConsolidatedDataToFD(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> param) {
        ModelAndView mv = new ModelAndView("redirect:/listingTerminationPostSchedule3.htm");
        List grpAList = new ArrayList();
        String fyear = ae.getFinancialYear();
        ae.setFy(fyear);
        mv.addObject("financialYear", fyear);
        ae.setOffCode(lub.getLoginoffcode());
        ae.setEmpId(lub.getLoginempid());
        ae.setEmpPost(lub.getLoginspc());
        mv.addObject("OffName", lub.getLoginoffname());
        postTerminationDAO.SubmitConsolidatedDataToFD(lub.getLoginoffcode(), lub.getLoginempid(), lub.getLoginspc(),fyear);
        return mv;
    }
    
    @RequestMapping(value = "DeptWisePostTerminationReport")
    public ModelAndView DeptWisePostTerminationReport(ModelMap model, HttpServletResponse response) {
        String path = "/misreport/DeptWisePostTerminationReport";
        ModelAndView mav = new ModelAndView();
        ArrayList AERDetails = postTerminationDAO.DeptWisePostTerminationReport();
        mav.addObject("AERDetails", AERDetails);
        mav.setViewName(path);
        return mav;
    }
    @RequestMapping(value = "COViewPostTerminationList")
    public ModelAndView COViewPostTerminationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishmentReportPostTerminatonForm ae) {

        ModelAndView mav = new ModelAndView();

        try {
            if (ae.getFy() != null && !ae.getFy().equals("")) {
                ArrayList postterminationcolist = postTerminationDAO.COViewPostTerminationList(lub.getLoginoffcode(), ae.getFy());
                mav.addObject("postterminationcolist", postterminationcolist);
            }
            String fyear = ae.getFy();
            mav.addObject("financialYear", fyear);
            mav.setViewName("report/aerposttermination/COViewPostTerminationList");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "COWiseTerminationStatus", params = {"btnAer=Approve"})
    public String approvePostTerminationForDDO(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {
        String path = "redirect:/COViewPostTerminationList.htm?fy=" + ae.getFinancialYear();
        postTerminationDAO.approvePostTerminationForDDO(ae.getProposalId(), ae.getFinancialYear());
        return path;
    }

    @RequestMapping(value = "COWiseTerminationStatus", params = {"btnAer=Decline"})
    public String declinePostTerminationForDDO(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {
        
        String path = "redirect:/COViewPostTerminationList.htm?fy=" + ae.getFinancialYear();
        postTerminationDAO.declinePostTerminationForDDO(ae.getProposalId(), ae.getFinancialYear(), ae.getRevertReason());
        return path;
    }
    
    @RequestMapping(value = "downloadPDFScheduleIAByCO")
    public void downloadPDFScheduleIAByCO(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("termId") int termId, @RequestParam("financialYear") String fy, @ModelAttribute("command") AnnualEstablishmentReportPostTerminatonForm ae) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        PdfWriter writer = null;

        try {
            response.setHeader("Content-Disposition", "attachment; filename=SCHEDULE_I_A_" + termId + ".pdf");
            writer = PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            postTerminationDAO.downloadPDFScheduleIAByCO(document, termId, lub.getLoginoffname(), lub.getLoginoffcode(), fy);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
    @RequestMapping(value = "COWiseSummaryReport")
    public ModelAndView COWiseSummaryReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishmentReportPostTerminatonForm ae) {

        ModelAndView mav = new ModelAndView();
        
        try {
            if (ae.getFy() != null && !ae.getFy().equals("")) {
                ArrayList postterminationcolist = postTerminationDAO.COWiseSummaryReport(lub.getLoginoffcode(), ae.getFy());
                mav.addObject("postterminationcolist", postterminationcolist);
               // String FDStatus= postTerminationDAO.coFDStatus(lub.getLoginoffcode(), ae.getFy());
                // mav.addObject("FDStatus", FDStatus);
            }
            String fyear = ae.getFy();
            mav.addObject("financialYear", fyear);
            mav.setViewName("report/aerposttermination/COWiseSummaryReport");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

}
