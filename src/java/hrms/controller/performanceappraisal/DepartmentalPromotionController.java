 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.performanceappraisal;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.HeaderFooterPageEvent;
import hrms.dao.dpcconducted.DPCConductedDAO;
import hrms.dao.fiscalyear.FiscalYearDAO;
import hrms.dao.master.CadreDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.performanceappraisal.PARAdminDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.parmast.DPCConductedForm;
import hrms.model.parmast.DepartmentPromotionBean;
import hrms.model.parmast.DepartmentPromotionDetail;
import hrms.model.parmast.NominationCategoryForm;
import hrms.model.parmast.ParApplyForm;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.json.JSONArray;
import org.json.JSONException;
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
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author manisha
 */
@Controller
@SessionAttributes({"LoginUserBean"})
public class DepartmentalPromotionController {

    @Autowired
    PARAdminDAO parAdminDAO;

    @Autowired
    FiscalYearDAO fiscalDAO;

    @Autowired
    CadreDAO cadreDAO;

    @Autowired
    DPCConductedDAO dpcConductedDAO;
    
    @Autowired
    OfficeDAO offDAO;
    @RequestMapping(value = "dpcStatementDetailReport.htm", method = RequestMethod.GET)
    public ModelAndView dpcStatementDetailReport(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("departmentPromotionBean", new DepartmentPromotionBean());
        ArrayList dpcDataList = parAdminDAO.getDPCData(lub.getLoginempid());
        mv.addObject("dpcDataList", dpcDataList);
        mv.setViewName("/departmentalPromotion/DpcStatementDetailReport");
        return mv;
    }

    @RequestMapping(value = "createdpcStatementDetailReport", method = {RequestMethod.POST}, params = {"action=Create New"})
    public ModelAndView createdpcStatementDetailReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("departmentPromotionBean") DepartmentPromotionBean departmentPromotionBean) {
        ModelAndView mv = new ModelAndView();
        List fiscyear = fiscalDAO.getisReviewedFiscalYearList();
        mv.addObject("fiscyear", fiscyear);

        ArrayList cadreList = parAdminDAO.getCadreList(lub.getLoginspc());

        mv.addObject("cadreList", cadreList);
        mv.setViewName("/departmentalPromotion/CreateDpcStatementDetailReport");
        return mv;
    }

    @RequestMapping(value = "createdpcStatementDetailReport", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Save"})
    public ModelAndView dpcStatementDetailReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("departmentPromotionBean") DepartmentPromotionBean departmentPromotionBean) {
        ModelAndView mav = new ModelAndView();
        departmentPromotionBean.setCreatedByEmpId(lub.getLoginempid());
        parAdminDAO.saveDPCData(departmentPromotionBean);
        ArrayList dpcDataList = parAdminDAO.getDPCData(lub.getLoginempid());
        mav.addObject("dpcDataList", dpcDataList);
        mav.setViewName("/departmentalPromotion/DpcStatementDetailReport");
        return mav;
    }

    @RequestMapping(value = "createdpcStatementDetailReport", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Back"})
    public ModelAndView backdpcStatementDetailReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("departmentPromotionBean") DepartmentPromotionBean departmentPromotionBean) {
        ModelAndView mav = new ModelAndView();
        ArrayList dpcDataList = parAdminDAO.getDPCData(lub.getLoginempid());
        mav.addObject("dpcDataList", dpcDataList);
        mav.setViewName("/departmentalPromotion/DpcStatementDetailReport");
        return mav;
    }

    @RequestMapping(value = "CadrewiseDpcStatementDetailReport.htm")
    public ModelAndView cadrewisenrcStatementDetailReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("departmentPromotionBean") DepartmentPromotionBean departmentPromotionBean) {
        ModelAndView mv = new ModelAndView();
        ArrayList employeeListCadrewise = parAdminDAO.getEmployeeListCadrewise(departmentPromotionBean.getDpcId());
        mv.addObject("employeeListCadrewise", employeeListCadrewise);
        mv.setViewName("/departmentalPromotion/CadrewiseDpcStatementDetailReport");
        return mv;
    }

    @RequestMapping(value = "CadrewiseDpcStatementDetailReport.htm", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Back"})
    public ModelAndView backcadrewisenrcStatementDetailReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("departmentPromotionBean") DepartmentPromotionBean departmentPromotionBean) {
        ModelAndView mv = new ModelAndView();
        ArrayList dpcDataList = parAdminDAO.getDPCData(lub.getLoginempid());
        mv.addObject("dpcDataList", dpcDataList);
        mv.setViewName("/departmentalPromotion/DpcStatementDetailReport");
        return mv;
    }

    @RequestMapping(value = "editdpcStatementDetailReport.htm")
    public ModelAndView editdpcStatementDetailReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("departmentPromotionBean") DepartmentPromotionBean departmentPromotionBean) {
        ModelAndView mv = new ModelAndView();
        List fiscyear = fiscalDAO.getisReviewedFiscalYearList();
        ArrayList cadreList = parAdminDAO.getCadreList(lub.getLoginspc());
        mv.addObject("fiscyear", fiscyear);
        mv.addObject("cadreList", cadreList);
        departmentPromotionBean = parAdminDAO.getDPCData(departmentPromotionBean.getDpcId());
        mv.setViewName("/departmentalPromotion/CreateDpcStatementDetailReport");
        mv.addObject("departmentPromotionBean", departmentPromotionBean);
        return mv;
    }

    @RequestMapping(value = "viewdpcStatement.htm", method = {RequestMethod.POST}, params = {"action=View"})
    public ModelAndView viewdpcStatementDetailReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("departmentPromotionBean") DepartmentPromotionBean departmentPromotionBean) {
        ModelAndView mv = new ModelAndView();
        ArrayList cadrewisenrcReport = parAdminDAO.getcadrewisenrcStatementDetailReport(departmentPromotionBean.getDpcId());
        mv.addObject("cadrewisenrcReport", cadrewisenrcReport);
        mv.setViewName("/departmentalPromotion/DpcStatementFinalReport");
        return mv;
    }
    @RequestMapping(value = "viewdpcStatement.htm", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView BackStatementDetailReportPdf(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("departmentPromotionBean") DepartmentPromotionBean departmentPromotionBean) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:dpcStatementDetailReport.htm");
        return mv;
    }
     @RequestMapping(value = "viewdpcStatementDetailReport.htm")
    public ModelAndView viewdpcStatementReport(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("departmentPromotionBean") DepartmentPromotionBean departmentPromotionBean) {
        ModelAndView mv = new ModelAndView();
        ArrayList cadrewisenrcReport = parAdminDAO.getcadrewisenrcStatementDetailReport(departmentPromotionBean.getDpcId());
        mv.addObject("cadrewisenrcReport", cadrewisenrcReport);
        mv.setViewName("/departmentalPromotion/DpcStatementFinalReport");
        return mv;
    }

    @RequestMapping(value = "viewdpcStatementDetailReport.htm", method = {RequestMethod.POST}, params = {"action=Download As PDF"})
    public ModelAndView downloadStatementDetailReportPdf(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("departmentPromotionBean") DepartmentPromotionBean departmentPromotionBean) {
        ModelAndView mv = new ModelAndView();
        DepartmentPromotionBean dpcBean = parAdminDAO.getDPCData(departmentPromotionBean.getDpcId());
        ArrayList cadrewisenrcReport = parAdminDAO.getcadrewisenrcStatementDetailReport(departmentPromotionBean.getDpcId());
        mv.addObject("dpcBean", dpcBean);
        mv.addObject("cadrewisenrcReport", cadrewisenrcReport);
        mv.setViewName("parStatementReportPdf");
        return mv;
    }

    @RequestMapping(value = "viewdpcStatementDetailReport.htm", method = {RequestMethod.POST}, params = {"action=Download As Excel"})
    public ModelAndView downloadStatementDetailReportExcel( @ModelAttribute("departmentPromotionBean") DepartmentPromotionBean departmentPromotionBean) {
        ModelAndView mv = new ModelAndView();
        DepartmentPromotionBean dpcBean = parAdminDAO.getDPCData(departmentPromotionBean.getDpcId());
        ArrayList cadrewisenrcReport = parAdminDAO.getcadrewisenrcStatementDetailReport(departmentPromotionBean.getDpcId());
        mv.addObject("dpcBean", dpcBean);
        mv.addObject("cadrewisenrcReport", cadrewisenrcReport);
        mv.setViewName("parStatementReportExcel");
        return mv;
    }

    @RequestMapping(value = "viewdpcStatementDetailReport.htm", method = {RequestMethod.POST, RequestMethod.GET}, params = {"action=Back"})
    public ModelAndView backviewdpcStatementDetailReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("departmentPromotionBean") DepartmentPromotionBean departmentPromotionBean) {
        ModelAndView mv = new ModelAndView();
        ArrayList dpcDataList = parAdminDAO.getDPCData(lub.getLoginempid());
        mv.addObject("dpcDataList", dpcDataList);
        mv.setViewName("/departmentalPromotion/DpcStatementDetailReport");
        return mv;
    }

    @RequestMapping(value = "deletedpcStatementDetailReport.htm")
    public ModelAndView deletenrcStatementDetailReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("dpcId") int dpcId) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("departmentPromotionBean", new DepartmentPromotionBean());
        parAdminDAO.deleteDPCData(dpcId);
        ArrayList dpcDataList = parAdminDAO.getDPCData(lub.getLoginempid());
        mv.addObject("dpcDataList", dpcDataList);
        mv.setViewName("/departmentalPromotion/DpcStatementDetailReport");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "addcadrewisenrcStatementDetailReport.htm")
    public void addcadrewisenrcStatementDetailReport(@ModelAttribute("departmentPromotionBean") DepartmentPromotionDetail departmentPromotionDetail, HttpServletResponse response, ModelMap model) throws IOException, JSONException {
        response.setContentType("application/json");
        parAdminDAO.savecadrewisenrcStatementDetailReport(departmentPromotionDetail);
        JSONObject obj = new JSONObject();
        obj.append("msg", "Y");
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();
    }

    @ResponseBody
    @RequestMapping(value = "removecadrewisenrcStatementDetailReport.htm")
    public void removecadrewisenrcStatementDetailReport(@ModelAttribute("departmentPromotionBean") DepartmentPromotionDetail departmentPromotionDetail, HttpServletResponse response, ModelMap model) throws IOException, JSONException {
        response.setContentType("application/json");
        parAdminDAO.deletecadrewisenrcStatementDetailReport(departmentPromotionDetail);
        JSONObject obj = new JSONObject();
        obj.append("msg", "Y");
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();
    }

    @RequestMapping(value = "GetisReviewFiscalYearListJSON", method = RequestMethod.POST)
    public @ResponseBody
    void GetisReviewFiscalYearListJSON(HttpServletResponse response) {
        response.setContentType("application/json");
        JSONArray json = null;
        PrintWriter out = null;
        try {
            List fiscalyearlist = fiscalDAO.getisReviewedFiscalYearList();
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

    @RequestMapping(value = "DPCConductedList.htm")
    public ModelAndView DPCConductedList(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        try {
            DPCConductedForm dpcform = dpcConductedDAO.getDPCconductedList(lub.getLoginoffcode());
            String isSubmitted = dpcConductedDAO.getDpcSubmissionStatus(lub.getLoginoffcode());
            mv.addObject("dpcDetail", dpcform);
            mv.addObject("officename", lub.getLoginoffname());
            mv.addObject("isSubmitted", isSubmitted);
            mv.setViewName("/departmentalPromotion/DpcStatement");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "nominationfinal.htm")
    public ModelAndView nominationfinal(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        try {
            String isSubmitted = dpcConductedDAO.getNominationCategorySubmissionStatus(lub.getLoginoffcode());

            NominationCategoryForm nform = dpcConductedDAO.getNominationCategoryData(lub.getLoginoffcode());

            mv = new ModelAndView("/departmentalPromotion/NominationFinal", "nominationCategoryForm", nform);
            mv.addObject("officename", lub.getLoginoffname());
            mv.addObject("isSubmitted", isSubmitted);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "addDPCConductedPage.htm")
    public ModelAndView addDPCConductedPage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("dpcform") DPCConductedForm dpcform) {
        ModelAndView mv = new ModelAndView();
        try {
            dpcform = dpcConductedDAO.getDPCconductedList(lub.getLoginoffcode());
            mv.addObject("dpcconductedList", dpcform);
            mv.addObject("officename", lub.getLoginoffname());
            mv.setViewName("/departmentalPromotion/DPCConductedEntryPage");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "saveDPCConductedData.htm")
    public String saveDPCConductedData(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("dpcform") DPCConductedForm dpcform) {

        try {
            dpcConductedDAO.saveDPCConductedData(dpcform, lub.getLoginoffcode(), lub.getLoginOffLevel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/DPCConductedList.htm";
    }

    @RequestMapping(value = "saveNominationCategoryData.htm")
    public String saveNominationCategoryData(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationCategoryForm") NominationCategoryForm nominationCategoryForm) {
        dpcConductedDAO.saveNominationCategoryData(nominationCategoryForm, lub.getLoginoffcode(), lub.getLoginOffLevel());
        return "redirect:/nominationfinal.htm";
    }

    @RequestMapping(value = "dpcReport.htm")
    public ModelAndView dpcReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationCategoryForm") NominationCategoryForm nominationCategoryForm) {
        ModelAndView mv = new ModelAndView();
        List dpclist = dpcConductedDAO.dpcMISReport();
        mv.addObject("dpclist", dpclist);
        mv.setViewName("/departmentalPromotion/DPCMISReport");
        return mv;
    }

    @RequestMapping(value = "DownloadNominationCategoryDataPDF")
    public void DownloadNominationCategoryDataPDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,@RequestParam(value = "offcode",required = false)String offcode) {
        
        if (offcode == null || offcode.equals("")) {
            offcode = lub.getLoginoffcode();
        } 
        String fileName = "Nomination_Category_Data_" + offcode;

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        PdfWriter writer = null;
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".pdf");

            HeaderFooterPageEvent event = new HeaderFooterPageEvent();

            writer = PdfWriter.getInstance(document, response.getOutputStream());
            Rectangle rect = new Rectangle(150, 30, 550, 800);
            writer.setBoxSize("art", rect);
            writer.setPageEvent(event);

            document.open();

            NominationCategoryForm nform = dpcConductedDAO.getNominationCategoryData(offcode);
            String offname = offDAO.getOfficeDetails(offcode).getOffEn();
            dpcConductedDAO.downloadNominationCategoryPDF(document, offcode, offname, nform);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "GenerateDPCpdf", method = RequestMethod.GET)
    public void GenerateDPCpdf(ModelMap model, @ModelAttribute("dpcform") DPCConductedForm dpcform,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String offcode = "";
        if (dpcform.getOffCode() != null && !dpcform.getOffCode().equals("")) {
            offcode = dpcform.getOffCode();
        } else {
            offcode = lub.getLoginoffcode();
        }
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=DPCConducted2021-" + offcode + ".pdf");
        Document document = new Document(PageSize.A4.rotate());
        final Font titlefontSmall = FontFactory.getFont("Arial", 15, Font.BOLD);
        final Font headingRule = FontFactory.getFont("Arial", 12, Font.BOLD);
        final Font standardRule = FontFactory.getFont("Arial", 10, Font.NORMAL);
        String offname = offDAO.getOfficeDetails(offcode).getOffEn();

        try {
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            Rectangle rect = new Rectangle(150, 30, 550, 800);
            writer.setBoxSize("art", rect);
            writer.setPageEvent(event);
            document.open();
            dpcform = dpcConductedDAO.getDPCconductedList(offcode);
            PdfPTable table = new PdfPTable(11);
            table.setWidths(new int[]{1, 3, 3, 3, 2, 2, 2, 2, 2, 3, 3});
            table.setWidthPercentage(100);

            PdfPCell cell = null;
            cell = new PdfPCell(new Phrase("DPCs Conducted to fill up vacancies of 2021", titlefontSmall));

            //cell.setBorder(Rectangle.);
            cell.setColspan(11);
            table.addCell(cell);

            cell = null;
            cell = new PdfPCell(new Phrase("Name of the Department/HOD/District: " + offname, headingRule));
            //cell.setBorder(Rectangle.);
            cell.setColspan(11);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Sl No. ", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("No. of DPCs due to be conducted to fill up vacancies of 2021", standardRule));
            cell.setColspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("No. of DPCs conducted to fill up vacancies of 2021", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", standardRule));
            cell.setColspan(5);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("No. of DPCs not conducted", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Reasons thereof", standardRule));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", standardRule));
            cell.setColspan(4);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Group-A", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Group-B", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Group-C", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Group-D", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Total", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", standardRule));
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("1", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("2", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("3", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("4", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("5", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("6", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("7", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("8", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("9", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("10", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("11", standardRule));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("1", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Department Level", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getDlDpcsDue(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getDlDpcs(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getDlGroupA(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getDlGroupB(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getDlGroupC(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getDlGroupD(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getDlTotal(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getDlDpcsNotConducted(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getDlReasonsThereof(), standardRule));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("2", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Heads of Department Level", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getHdDpcsDue(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getHdDpcs(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getHdGroupA(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getHdGroupB(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getHdGroupC(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getHdGroupD(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getHdTotal(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getHdDpcsNotConducted(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getHdReasonsThereof(), standardRule));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("3", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("District Level", standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getDsDpcsDue(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getDsDpcs(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getDsGroupA(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getDsGroupB(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getDsGroupC(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getDsGroupD(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getDsTotal(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getDsDpcsNotConducted(), standardRule));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dpcform.getDsReasonsThereof(), standardRule));
            table.addCell(cell);

            table.addCell(cell);

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}
