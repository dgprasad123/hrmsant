/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.ltc;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.CommonFunctions;
import hrms.common.HeaderFooterPageEvent;
import hrms.common.Message;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.login.LoginDAOImpl;
import hrms.dao.ltc.LTCDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.ProcessStatusDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.MaxNotificationIdDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.LTC.LTCBean;
import hrms.model.LTC.sLTCBean;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manoj PC
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class LTCController {

    @Autowired
    public LoginDAOImpl loginDAO;

    @Autowired
    public LTCDAO LTCDAO;

    @Autowired
    public DepartmentDAO departmentDao;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public SubStantivePostDAO substantivePostDAO;

    @Autowired
    public NotificationDAO NotificationDAO;

    @Autowired
    public MaxNotificationIdDAO maxnotiidDao;

    @Autowired
    ProcessStatusDAO processStatusDAO;

    @RequestMapping(value = "ApplyEmpLTC", method = RequestMethod.GET)
    public ModelAndView ApplyEmpLTC(ModelMap model, @ModelAttribute("LTCBean") LTCBean lBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {

        ModelAndView mav = new ModelAndView();
        mav.addObject("empName", lub.getLoginname());
        mav.addObject("designation", lub.getLoginspn());
        List li = LTCDAO.getLeaveTypes();
        mav.addObject("leaveTypes", li);
        mav.setViewName("LTC/AddEmpLTC");

        return mav;
    }

    @RequestMapping(value = "BasicInfo", method = RequestMethod.GET)
    public ModelAndView BasicInfo(ModelMap model, @ModelAttribute("LTCBean") LTCBean lBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam Map<String, String> requestParams) {

        String ltcId = requestParams.get("id");
        if (!(ltcId != null && !ltcId.equals(""))) {
            ltcId = "0";
        }
        int iLtcId = Integer.parseInt(ltcId);

        if (iLtcId > 0) {
            lBean = LTCDAO.getLtcDetail(ltcId);
        }
        ModelAndView mav = new ModelAndView("/LTC/BasicInfo", "LTCBean", lBean);
        //System.out.println("LTCID:"+ltcId);
        mav.addObject("empName", lub.getLoginname());
        mav.addObject("designation", lub.getLoginspn());
        List li = LTCDAO.getLeaveTypes();
        mav.addObject("leaveTypes", li);
        mav.addObject("ltcId", ltcId);
        mav.addObject("lBean", lBean);

        return mav;
    }

    @RequestMapping(value = "FamilyMembers", method = RequestMethod.GET)
    public ModelAndView FamilyMembers(ModelMap model, @ModelAttribute("LTCBean") LTCBean lBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = new ModelAndView();
        mav.addObject("empName", lub.getLoginname());
        mav.addObject("designation", lub.getLoginspn());
        String ltcId = requestParams.get("id");
        List li = LTCDAO.getLeaveTypes();
        List fList = LTCDAO.getFamilyMembers(ltcId);
        List relationList = LTCDAO.getRelations();
        mav.addObject("leaveTypes", li);
        mav.addObject("fList", fList);
        mav.addObject("ltcId", ltcId);
        mav.addObject("relationList", relationList);
        mav.setViewName("LTC/FamilyMembers");

        return mav;
    }

    @RequestMapping(value = "ReimbursementDetails", method = RequestMethod.GET)
    public ModelAndView ReimbursementDetails(ModelMap model, @ModelAttribute("LTCBean") LTCBean lBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam Map<String, String> requestParams) {

        String ltcId = requestParams.get("id");
        if (!(ltcId != null && !ltcId.equals(""))) {
            ltcId = "0";
        }
        int iLtcId = Integer.parseInt(ltcId);

        if (iLtcId > 0) {
            lBean = LTCDAO.getLtcDetail(ltcId);
        }
        ModelAndView mav = new ModelAndView("/LTC/ReimbursementDetails", "LTCBean", lBean);
        mav.addObject("empName", lub.getLoginname());
        mav.addObject("designation", lub.getLoginspn());
        List li = LTCDAO.getLeaveTypes();
        mav.addObject("leaveTypes", li);
        mav.addObject("ltcId", ltcId);

        return mav;
    }

    @RequestMapping(value = "LTCReportingAuthority", method = RequestMethod.GET)
    public ModelAndView LTCReportingAuthority(ModelMap model, @ModelAttribute("LTCBean") LTCBean lBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam Map<String, String> requestParams) {

        String ltcId = requestParams.get("id");
        if (!(ltcId != null && !ltcId.equals(""))) {
            ltcId = "0";
        }
        int iLtcId = Integer.parseInt(ltcId);
        String rauthorityName = "";
        String rSpc = "";
        if (iLtcId > 0) {
            lBean = LTCDAO.getLtcDetail(ltcId);
            rSpc = lBean.getrAuthoritySpc();
        }
        if ((rSpc != null && !rSpc.equals(""))) {
            rauthorityName = LTCDAO.getAuthorityName(rSpc);
        }
        ModelAndView mav = new ModelAndView("/LTC/LTCReportingAuthority", "LTCBean", lBean);
        mav.addObject("empName", lub.getLoginname());
        mav.addObject("designation", lub.getLoginspn());
        List li = LTCDAO.getLeaveTypes();
        mav.addObject("leaveTypes", li);
        mav.addObject("authorityName", rauthorityName);
        mav.addObject("ltcId", ltcId);

        return mav;
    }

    @RequestMapping(value = "LTCIssuingAuthority", method = RequestMethod.GET)
    public ModelAndView LTCIssuingAuthority(ModelMap model, @ModelAttribute("LTCBean") LTCBean lBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam Map<String, String> requestParams) {

        String ltcId = requestParams.get("id");
        if (!(ltcId != null && !ltcId.equals(""))) {
            ltcId = "0";
        }
        int iLtcId = Integer.parseInt(ltcId);
        String iauthorityName = "";
        String iSpc = "";
        if (iLtcId > 0) {
            lBean = LTCDAO.getLtcDetail(ltcId);
            iSpc = lBean.getiAuthoritySpc();
        }

        if ((iSpc != null && !iSpc.equals(""))) {
            iauthorityName = LTCDAO.getAuthorityName(iSpc);
        }

        ModelAndView mav = new ModelAndView("/LTC/LTCIssuingAuthority", "LTCBean", lBean);
        mav.addObject("empName", lub.getLoginname());
        mav.addObject("designation", lub.getLoginspn());
        List li = LTCDAO.getLeaveTypes();
        mav.addObject("leaveTypes", li);
        mav.addObject("authorityName", iauthorityName);
        mav.addObject("ltcId", ltcId);

        return mav;
    }

    @RequestMapping(value = "ValidateLTC", method = RequestMethod.GET)
    public ModelAndView ValidateLTC(ModelMap model, @ModelAttribute("LTCBean") LTCBean lBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam Map<String, String> requestParams) {

        String ltcId = requestParams.get("id");
        LTCBean ltBean = null;
        if (!(ltcId != null && !ltcId.equals(""))) {
            ltcId = "0";
            ltBean = new LTCBean();
        }
        int iLtcId = Integer.parseInt(ltcId);

        if (iLtcId > 0) {
            lBean = LTCDAO.getLtcDetail(ltcId);
            ltBean = lBean;
        }
        ModelAndView mav = new ModelAndView("/LTC/ValidateLTC", "LTCBean", lBean);
        mav.addObject("empName", lub.getLoginname());
        mav.addObject("designation", lub.getLoginspn());
        List li = LTCDAO.getLeaveTypes();
        mav.addObject("leaveTypes", li);
        mav.addObject("ltcId", ltcId);
        mav.addObject("ltcvBean", ltBean);

        return mav;
    }

    @RequestMapping(value = "SaveEmpLTC", method = {RequestMethod.GET, RequestMethod.POST})
    public void SaveEmpLTC(@ModelAttribute("LTCBean") LTCBean lBean,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        try {
            LTCDAO.saveEmpLTCData(lBean, lub.getLoginempid(), lub.getLoginspc());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @RequestMapping(value = "SubmitLTC", method = {RequestMethod.GET, RequestMethod.POST})
    public String SubmitLTC(@ModelAttribute("LTCBean") LTCBean lBean,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        try {
            String ltcId = lBean.getLtcId();
            lBean = LTCDAO.getLtcDetail(ltcId);
            String iSpc = lBean.getiAuthoritySpc();
            LTCDAO.submitLTC(Integer.parseInt(ltcId), lub.getLoginempid(), lub.getLoginspc(), iSpc);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        String path = "redirect:/EmpLTCList.htm";
        return path;
    }

    @RequestMapping(value = "SaveBasicInfo", method = {RequestMethod.GET, RequestMethod.POST})
    public String SaveBasicInfo(@ModelAttribute("LTCBean") LTCBean lBean,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {

        String path = "";
        int ltcID = 0;
        try {
            if ((lBean.getLtcId() != null && !lBean.getLtcId().equals("")) && Integer.parseInt(lBean.getLtcId()) > 0) {
                LTCDAO.updateBasicInfo(lBean, lBean.getLtcId());
                ltcID = Integer.parseInt(lBean.getLtcId());
            } else {
                ltcID = LTCDAO.saveBasicInfo(lBean, lub.getLoginempid(), lub.getLoginspc());
            }

            path = "redirect:/FamilyMembers.htm?id=" + ltcID;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return path;
    }

    @RequestMapping(value = "SaveFamilyMembers", method = {RequestMethod.GET, RequestMethod.POST})
    public String SaveFamilyMembers(@ModelAttribute("LTCBean") LTCBean lBean,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String path = "";
        try {
            LTCDAO.saveFamilyMember(lBean, lub.getLoginempid());
            path = "redirect:/FamilyMembers.htm?id=" + lBean.getLtcId();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return path;
    }

    @RequestMapping(value = "SaveRDetails", method = {RequestMethod.GET, RequestMethod.POST})
    public String SaveRDetails(@ModelAttribute("LTCBean") LTCBean lBean,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String path = "";
        try {
            LTCDAO.saveRDetails(lBean, lub.getLoginempid());
            path = "redirect:/LTCReportingAuthority.htm?id=" + lBean.getLtcId();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return path;
    }

    @RequestMapping(value = "SaveIssuingAuthority", method = {RequestMethod.GET, RequestMethod.POST})
    public String SaveIssuingAuthority(@ModelAttribute("LTCBean") LTCBean lBean,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String path = "";
        try {
            LTCDAO.saveIssuingAuthority(lBean, lub.getLoginempid());
            path = "redirect:/ValidateLTC.htm?id=" + lBean.getLtcId();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return path;
    }

    @RequestMapping(value = "SaveReportingAuthority", method = {RequestMethod.GET, RequestMethod.POST})
    public String SaveReportingAuthority(@ModelAttribute("LTCBean") LTCBean lBean,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String path = "";
        try {
            LTCDAO.saveReportingAuthority(lBean, lub.getLoginempid());
            path = "redirect:/LTCIssuingAuthority.htm?id=" + lBean.getLtcId();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return path;
    }

    @RequestMapping(value = "EmpLTCList", method = RequestMethod.GET)
    public ModelAndView EmpLTCList(ModelMap model, @ModelAttribute("LTCBean") LTCBean lBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {

        ModelAndView mav = new ModelAndView();
        mav.addObject("empName", lub.getLoginname());
        mav.addObject("designation", lub.getLoginspn());
        List li = LTCDAO.getEmpLTCList(lub.getLoginempid());
        int total = LTCDAO.getLTCCount(lub.getLoginempid());
        mav.addObject("ltcList", li);
        mav.addObject("total", total);
        mav.setViewName("LTC/EmpLTCList");
        return mav;
    }

    @RequestMapping(value = "ViewLTCDetail", method = RequestMethod.GET)
    public ModelAndView ViewLTCDetail(ModelMap model, @ModelAttribute("LTCBean") LTCBean lBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = new ModelAndView();
        mav.addObject("empName", lub.getLoginname());
        mav.addObject("designation", lub.getLoginspn());
        String ltcId = requestParams.get("id");
        lBean = LTCDAO.getLtcDetail(ltcId);
        mav.addObject("ltBean", lBean);
        mav.setViewName("LTC/ViewLTCDetail");

        return mav;
    }

    @RequestMapping(value = "viewLTCProposalTask", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView viewLTCProposalTask(@ModelAttribute("LTCBean") LTCBean lBean,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("taskId") int taskId) {
        String path = "LTC/ViewLTCProposalTask";

        ModelAndView mav = new ModelAndView();
        try {
            int ltcId = LTCDAO.getProposalMasterId(taskId);
            int statusId = LTCDAO.getTaskStatus(taskId);

            lBean = LTCDAO.getLtcDetail(ltcId + "");
            mav.addObject("ltBean", lBean);
            mav.setViewName(path);
            mav.addObject("statusId", statusId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;

    }

    @ResponseBody
    @RequestMapping(value = "getLTCStatusJSON")
    public void getLTCStatusJSON(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            List ltcStatusList = LTCDAO.getLTCStatus();
            json = new JSONArray(ltcStatusList);
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
    @RequestMapping(value = "saveLTCAction")
    public void saveTrainingApprove(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, ModelMap model, @ModelAttribute("LTCBean") LTCBean lBean) throws IOException {

        response.setContentType("application/json");
        PrintWriter out = null;

        Message msg = null;
        try {
            msg = LTCDAO.saveLTCAction(Integer.parseInt(lBean.getTaskId()), Integer.parseInt(lBean.getTaskStatus()), lub.getLoginempid(), lub.getLoginspc(), lBean.getTaskAuthority(), lBean.getNote());

            JSONObject job = new JSONObject(msg);
            out = response.getWriter();
            out.write(job.toString());
            //System.out.println("TaskId");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "SaveVerification")
    public void saveVerification(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, ModelMap model, @ModelAttribute("LTCBean") LTCBean lBean) throws IOException {

        response.setContentType("application/json");
        PrintWriter out = null;

        Message msg = null;
        try {
            msg = LTCDAO.saveVerification(Integer.parseInt(lBean.getTaskId()), lub.getLoginempid(), lub.getLoginspc(), lBean);

            JSONObject job = new JSONObject(msg);
            out = response.getWriter();
            out.write(job.toString());
            System.out.println("TaskId");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "SaveOrderInfo")
    public void saveOrderInfo(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, ModelMap model, @ModelAttribute("LTCBean") LTCBean lBean) throws IOException {

        response.setContentType("application/json");
        PrintWriter out = null;

        Message msg = null;
        try {
            msg = LTCDAO.saveOrderInfo(Integer.parseInt(lBean.getTaskId()), lBean.getOrderDate(), lBean.getOrderNo(), Integer.parseInt(lBean.getLtcId()));

            JSONObject job = new JSONObject(msg);
            out = response.getWriter();
            out.write(job.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "GenerateLTCOrder", method = RequestMethod.GET)
    public void generateLTCOrder(ModelMap model, @ModelAttribute("LTCBean") LTCBean lBean,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("ltcId") int ltcId) {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=LTC.pdf");
        Document document = new Document(PageSize.A4);
        final Font titlefontSmall = FontFactory.getFont("Arial", 15, Font.BOLD);
        final Font headingRule = FontFactory.getFont("Arial", 15, Font.BOLD);
        final Font bottomRule = FontFactory.getFont("Arial", 12, Font.BOLD);
        String offname = "";

        try {
            LTCBean ltBean = LTCDAO.getLtcDetail(ltcId + "");

            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            Rectangle rect = new Rectangle(150, 30, 550, 800);
            writer.setBoxSize("art", rect);
            writer.setPageEvent(event);
            document.open();

            Paragraph rule = new Paragraph("Government of Odisha", headingRule);
            rule.setAlignment(Element.ALIGN_CENTER);
            document.add(rule);

            Paragraph heading = new Paragraph(ltBean.getOfficeName() + "\n*******", titlefontSmall);
            heading.setAlignment(Element.ALIGN_CENTER);
            document.add(heading);

            Paragraph order = new Paragraph("OFFICE ORDER\nBhubaneswar, dated " + ltBean.getOrderDate(), titlefontSmall);
            order.setAlignment(Element.ALIGN_CENTER);
            document.add(order);

            String pointStr = "\nNo. " + ltBean.getOrderNo() + ", " + ltBean.getEmpName() + ", " + ltBean.getDesignation() + " of " + ltBean.getOfficeName() + " is allowed to visit " + ltBean.getVisitPlace() + ", " + ltBean.getVisitState() + " on L.T.C. by availing " + ltBean.getNumDays() + " days " + ltBean.getLeaveType() + " with headquarter leaving permission from " + ltBean.getFromDate() + " to " + ltBean.getToDate() + " along with his " + CommonFunctions.convertNumber(Integer.parseInt(ltBean.getNumFamilyMembers())) + " family members.";
            Paragraph point1 = new Paragraph(pointStr);
            document.add(point1);

            String pointStr2 = "\nThe advance to " + ltBean.getEmpName() + ", " + ltBean.getDesignation() + " shall be paid subject to availability of funds under the head of L.T.C.";

            Paragraph point2 = new Paragraph(pointStr2);
            document.add(point2);

            Paragraph blank = new Paragraph("\n\n\n\n" + ltBean.getApprovingPost() + " to Government", bottomRule);
            blank.setAlignment(Element.ALIGN_RIGHT);
            document.add(blank);

            Paragraph blank1 = new Paragraph("Memo No._____________________                     Dt.________________", bottomRule);
            document.add(blank1);

            Paragraph blank2 = new Paragraph("\nCopy forwarded to " + ltBean.getEmpName() + ", " + ltBean.getDesignation() + ", " + ltBean.getOfficeName() + ", Account Section for information and necessary action.");
            document.add(blank2);

            Paragraph blank3 = new Paragraph("\n\n\n\n" + ltBean.getApprovingPost() + " to Government", bottomRule);
            blank3.setAlignment(Element.ALIGN_RIGHT);
            document.add(blank3);

            document.newPage();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

    }

    public void onEndPage(PdfWriter writer, Document document) {
        Font ffont = new Font(Font.FontFamily.UNDEFINED, 5, Font.ITALIC);
        PdfContentByte cb = writer.getDirectContent();
        Phrase footer = new Phrase("this is a footer", ffont);

        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                footer,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 10, 0);
    }

    @RequestMapping(value = "newLTCEntry")
    public ModelAndView newLTCEntry(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("sLTCBean") sLTCBean lBean) {

        ModelAndView mav = null;
        try {
            //List depuList = deputationDAO.getDeputationList(lub.getEmpId());
            Calendar cal = Calendar.getInstance();
            int currentYear = cal.get(Calendar.YEAR);
            mav = new ModelAndView("/LTC/AddsEmpLTC", "sLTCBean", lBean);

            if (lBean.getRadpostingauthtype() == null || lBean.getRadpostingauthtype().equals("")) {
                lBean.setRadpostingauthtype("GOO");
            }

            List deptlist = departmentDao.getDepartmentList();
            List fieldofflist = offDAO.getFieldOffList(lub.getOffcode());
            List otherOrgfflist = offDAO.getOtherOrganisationList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("fieldofflist", fieldofflist);
            mav.addObject("currentYear", currentYear);
            mav.addObject("otherOrgfflist", otherOrgfflist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "editLTCEntry")
    public ModelAndView editLTCEntry(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("sLTCBean") sLTCBean lBean, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = null;
        try {
            String empId = lub.getEmpId();
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            sLTCBean slBean = LTCDAO.getSLTCDetail(empId, notificationId);

            if (slBean.getRadpostingauthtype() == null || slBean.getRadpostingauthtype().equals("")) {
                slBean.setRadpostingauthtype("GOO");
            }

            Calendar cal = Calendar.getInstance();
            int currentYear = cal.get(Calendar.YEAR);
            mav = new ModelAndView("/LTC/AddsEmpLTC", "sLTCBean", slBean);

            List deptlist = departmentDao.getDepartmentList();
            List fieldofflist = offDAO.getFieldOffList(lub.getOffcode());
            List otherOrgfflist = offDAO.getOtherOrganisationList();

            mav.addObject("deptlist", deptlist);
            mav.addObject("fieldofflist", fieldofflist);
            mav.addObject("currentYear", currentYear);

            mav.addObject("otherOrgfflist", otherOrgfflist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "savesLTCEntry", params = "action=Back to LTC List")
    public ModelAndView BackToGDeputationListPage(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("sLTCBean") sLTCBean lBean) {

        ModelAndView mav = null;
        try {
            mav = new ModelAndView("redirect:/LTCEntryList.htm", "sLTCBean", lBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "savesLTCEntry", params = "action=Save LTC")
    public ModelAndView savesLTCEntry(@ModelAttribute("LoginUserBean") LoginUserBean loginbean, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("sLTCBean") sLTCBean lBean) {

        ModelAndView mav = null;

        try {
            //deputationForm.setEmpid(lub.getEmpId());

            // System.out.println("Else");
            lBean.setEmpid(selectedEmpObj.getEmpId());
            if (lBean.getLtcId() != null && !lBean.getLtcId().equals("")) {
                LTCDAO.updateLTCEntry(lBean);
            } else {
                LTCDAO.saveLTCEntry((lBean.getHidNotId() + ""), lBean, loginbean.getLogindeptcode(), loginbean.getLoginoffcode(), loginbean.getLogingpc());
            }

                // transferForm.setSltPostedFieldOff(deputationForm.getSltFieldOffice());
            //  transferDao.saveTransfer(transferForm, notid,loginbean.getLoginempid());
            mav = new ModelAndView("redirect:/LTCEntryList.htm", "sLTCBean", lBean);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "UpdateLTCEntry", params = "action=Back to LTC List")
    public ModelAndView UpdateLTCEntry(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("sLTCBean") sLTCBean lBean) {

        ModelAndView mav = null;
        try {
            mav = new ModelAndView("redirect:/LTCEntryList.htm", "sLTCBean", lBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "UpdateLTCEntry", params = "action=Update LTC")
    public ModelAndView updateLTCEntry(@ModelAttribute("LoginUserBean") LoginUserBean loginbean, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("sLTCBean") sLTCBean lBean) {

        ModelAndView mav = null;

        try {
            lBean.setEmpid(selectedEmpObj.getEmpId());
            LTCDAO.updateLTCEntry(lBean);

            mav = new ModelAndView("redirect:/LTCEntryList.htm", "sLTCBean", lBean);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "LTCEntryList")
    public ModelAndView LTCEntryList(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("slBean") sLTCBean lBean) {
        ModelAndView mav = null;
        try {
            List ltcList = LTCDAO.getLTCEntryList(lub.getEmpId());

            mav = new ModelAndView("/LTC/LTCEntryList", "sLTCBean", lBean);
            mav.addObject("ltcList", ltcList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
}
