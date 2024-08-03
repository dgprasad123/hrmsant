/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.policemodule;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.SelectOption;
import hrms.dao.master.CategoryDAO;
import hrms.dao.policemodule.AwardMedalDAO;
import hrms.model.common.FileAttribute;
import hrms.model.login.LoginUserBean;
import hrms.model.policemodule.AwardMedalListForm;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
 * @author Surendra
 */
@Controller
@SessionAttributes({"LoginUserBean"})
public class AwardMedalController implements ServletContextAware {

    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Autowired
    AwardMedalDAO awardMedalDAO;

    @Autowired
    public CategoryDAO categoryDAO;

    @RequestMapping(value = "awardormedalList")
    public ModelAndView awardormedalList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        ModelAndView mav = new ModelAndView();

        List awardeeList = new ArrayList();
        List awardTypeList = new ArrayList();
        try {
            awardTypeList = awardMedalDAO.getAwardTypeList();
            if (form.getAwardYear() != null && !form.getAwardYear().equals("")) {
                awardeeList = awardMedalDAO.getAwardeeList(lub.getLoginoffcode(), form.getAwardMedalTypeId(), form.getAwardYear(), form.getSltAwardOccasion());
            }
            mav = new ModelAndView("/policemodule/award/AwardMedalList", "AwardMedalListForm", form);
            mav.addObject("awardeeList", awardeeList);
            mav.addObject("awardType", awardTypeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "searchEmployeeForAward")
    public ModelAndView searchEmployeeForAward(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm emp) {

        ModelAndView mav = new ModelAndView();

        try {

            mav = new ModelAndView("/policemodule/award/AddEmployeeforAward", "AwardMedalListForm", emp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "addSearchEmployeeForAward")
    public ModelAndView addSearchEmployeeForAward(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        ModelAndView mav = new ModelAndView();

        try {
            awardMedalDAO.addEmployeeForAward(form.getGpfNo(), form.getAwardMedalTypeId(), form.getAwardYear(), lub.getLoginoffcode(), form.getSltAwardOccasion());
            mav = new ModelAndView("redirect:/awardormedalList.htm?awardMedalTypeId=" + form.getAwardMedalTypeId() + "&awardYear=" + form.getAwardYear() + "&sltAwardOccasion=" + form.getSltAwardOccasion());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "getEmployeeListToAddForAward")
    public void getEmployeeListToAddForNomination(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm emp) {

        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        List emplist = new ArrayList();
        try {
            emplist = awardMedalDAO.getEmployeeListGpfNowise(emp.getEmpId(), emp.getAwardYear(), emp.getAwardMedalTypeId(), emp.getSltAwardOccasion());

            json = new JSONArray(emplist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "awardormedalFormLoad")
    public ModelAndView awardormedalFormLoad(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        ModelAndView mav = new ModelAndView();
        List invstCaseList = new ArrayList();
        List medalList = new ArrayList();
        List categoryList = new ArrayList();
        //List srCaseList = new ArrayList();
        List nonSrCaseList = new ArrayList();
        try {
            String path = "";
            if (form.getAwardMedalTypeId().equals("07")) {
                path = "/policemodule/award/DgpsDiscsAward";
            } else if (form.getAwardMedalTypeId().equals("14")) {
                path = "/policemodule/award/CmExcellenceInvestigation";
            } else if (form.getAwardMedalTypeId().equals("09")) {
                path = "/policemodule/award/PresidentPoliceAward";
            } else if (form.getAwardMedalTypeId().equals("06")) {
                path = "/policemodule/award/GovernorAward";
            }
            String profilePhotoPath = servletContext.getInitParameter("PhotoPath");
            form = awardMedalDAO.getAwardeeData(form.getRewardMedalId(),profilePhotoPath);
            // srCaseList = awardMedalDAO.getSRCaseList(form.getRewardMedalId());
            invstCaseList = awardMedalDAO.investigatedCaseList(form.getRewardMedalId());
            nonSrCaseList = awardMedalDAO.getNonSrCaseList(form.getRewardMedalId());
            categoryList = categoryDAO.getCategoryList();

            mav = new ModelAndView(path, "AwardMedalListForm", form);
            // mav.addObject("srCaseList", srCaseList);
            mav.addObject("invstCaseList", invstCaseList);
            mav.addObject("nonSrCaseList", nonSrCaseList);
            mav.addObject("categoryList", categoryList);
            int year = Calendar.getInstance().get(Calendar.YEAR);
            SelectOption so = null;
            ArrayList yearlist = new ArrayList();
            for (int i = 1990; i <= year; i++) {
                so = new SelectOption();
                so.setValue(i + "");
                so.setLabel(i + "");
                yearlist.add(so);
            }
            mav.addObject("yearlist", yearlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "awardormedalForm", params = "btn=Save Form", method = RequestMethod.POST)
    public ModelAndView awardormedalSaveForm(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        ModelAndView mav = new ModelAndView();
        String filepath = servletContext.getInitParameter("policeDocPath");
        try {

            awardMedalDAO.updateAwardData(form, filepath);
            mav = new ModelAndView("redirect:/awardormedalList.htm?awardMedalTypeId=" + form.getAwardMedalTypeId() + "&awardYear=" + form.getAwardYear() + "&sltAwardOccasion=" + form.getSltAwardOccasion());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "awardormedalFormForGovernor", params = "btn=Save Form", method = RequestMethod.POST)
    public ModelAndView awardormedalFormForGovernor(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        ModelAndView mav = new ModelAndView();
        String filepath = servletContext.getInitParameter("policeDocPath");
        //if (!filePath.exists()) {
        
        try {
            awardMedalDAO.updateAwardDataForGovernor(form, filepath);
            mav = new ModelAndView("redirect:/awardormedalList.htm?awardMedalTypeId=" + form.getAwardMedalTypeId() + "&awardYear=" + form.getAwardYear() + "&sltAwardOccasion=" + form.getSltAwardOccasion());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "awardormedalFormForGovernor", params = "btn=Delete", method = RequestMethod.POST)
    public ModelAndView awardormedalDeleteFormForGovernor(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {
        ModelAndView mav = new ModelAndView();
        List medalList = new ArrayList();
        try {
            awardMedalDAO.deleteAwardeeDetailsForGovernor(Integer.parseInt(form.getRewardMedalId()));
            mav = new ModelAndView("redirect:/awardormedalList.htm?awardMedalTypeId=" + form.getAwardMedalTypeId() + "&awardYear=" + form.getAwardYear());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "awardormedalFormForPresident", params = "btn=Save Form", method = RequestMethod.POST)
    public ModelAndView awardormedalFormForPresident(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        ModelAndView mav = new ModelAndView();
        String filepath = servletContext.getInitParameter("policeDocPath");
        try {
            awardMedalDAO.updateAwardDataForPresident(form, filepath);
            mav = new ModelAndView("redirect:/awardormedalList.htm?awardMedalTypeId=" + form.getAwardMedalTypeId() + "&awardYear=" + form.getAwardYear() + "&sltAwardOccasion=" + form.getSltAwardOccasion());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "awardormedalFormForPresident", params = "btn=Delete", method = RequestMethod.POST)
    public ModelAndView awardormedalFormForPresidentdelete(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {
        ModelAndView mav = new ModelAndView();
        List medalList = new ArrayList();
        try {
            awardMedalDAO.deleteAwardeeDetailsForGovernor(Integer.parseInt(form.getRewardMedalId()));
            mav = new ModelAndView("redirect:/awardormedalList.htm?awardMedalTypeId=" + form.getAwardMedalTypeId() + "&awardYear=" + form.getAwardYear());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "awardormedalForm", params = "btn=Add", method = RequestMethod.POST)
    public ModelAndView srCaseSaveForm(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        ModelAndView mav = new ModelAndView();
        // List srCaseList = new ArrayList();
        List invstCaseList = new ArrayList();
        List nonSrCaseList = new ArrayList();
        try {
            if (form.getSrCaseId() != null && !form.getSrCaseId().equals("")) {
                awardMedalDAO.updateSRCaseData(form);
            } else {
                awardMedalDAO.saveSRCaseData(form);
            }
            String path = "";
            if (form.getAwardMedalTypeId().equals("07")) {
                path = "/policemodule/award/DgpsDiscsAward";
            } else if (form.getAwardMedalTypeId().equals("14")) {
                path = "/policemodule/award/CmExcellenceInvestigation";
            } else if (form.getAwardMedalTypeId().equals("09")) {
                path = "/policemodule/award/PresidentPoliceAward";
            }
            String profilePhotoPath = servletContext.getInitParameter("PhotoPath");
            form = awardMedalDAO.getAwardeeData(form.getRewardMedalId(),profilePhotoPath);
            // srCaseList = awardMedalDAO.getSRCaseList(form.getRewardMedalId());
            invstCaseList = awardMedalDAO.investigatedCaseList(form.getRewardMedalId());
            nonSrCaseList = awardMedalDAO.getNonSrCaseList(form.getRewardMedalId());
            mav = new ModelAndView(path, "AwardMedalListForm", form);
            // mav.addObject("srCaseList", srCaseList);
            mav.addObject("invstCaseList", invstCaseList);
            mav.addObject("nonSrCaseList", nonSrCaseList);
            mav.addObject("categoryList", categoryDAO.getCategoryList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "awardormedalForm", params = "entercase=Enter Case", method = RequestMethod.POST)
    public ModelAndView investigatedCaseSaveForm(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        ModelAndView mav = new ModelAndView();
        //List srCaseList = new ArrayList();
        List invstCaseList = new ArrayList();
        List nonSrCaseList = new ArrayList();
        String filepath = servletContext.getInitParameter("policeDocPath");
        try {

            if (form.getInvstCaseId() != null && !form.getInvstCaseId().equals("")) {
                awardMedalDAO.updateInvestigatedCaseData(form, filepath);
            } else {
                awardMedalDAO.saveInvestigatedCaseData(form, filepath);
            }

            String path = "";
            if (form.getAwardMedalTypeId().equals("07")) {
                path = "/policemodule/award/DgpsDiscsAward";
            } else if (form.getAwardMedalTypeId().equals("14")) {
                path = "/policemodule/award/CmExcellenceInvestigation";
            } else if (form.getAwardMedalTypeId().equals("09")) {
                path = "/policemodule/award/PresidentPoliceAward";
            }
           String profilePhotoPath = servletContext.getInitParameter("PhotoPath");
            form = awardMedalDAO.getAwardeeData(form.getRewardMedalId(),profilePhotoPath);
            //srCaseList = awardMedalDAO.getSRCaseList(form.getRewardMedalId());
            invstCaseList = awardMedalDAO.investigatedCaseList(form.getRewardMedalId());
            nonSrCaseList = awardMedalDAO.getNonSrCaseList(form.getRewardMedalId());
            mav = new ModelAndView(path, "AwardMedalListForm", form);
            //mav.addObject("srCaseList", srCaseList);
            mav.addObject("invstCaseList", invstCaseList);
            mav.addObject("nonSrCaseList", nonSrCaseList);
            mav.addObject("categoryList", categoryDAO.getCategoryList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "awardormedalForm", params = "enternonsrcase=Add Case", method = RequestMethod.POST)
    public ModelAndView nonSrCaseDtlsSaveForm(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        ModelAndView mav = new ModelAndView();
        //List srCaseList = new ArrayList();
        List invstCaseList = new ArrayList();
        List nonSrCaseList = new ArrayList();
        String filepath = servletContext.getInitParameter("policeDocPath");
        try {

            if (form.getNonSrcaseId() != null && !form.getNonSrcaseId().equals("")) {
                awardMedalDAO.updateNonSrCaseDtlsData(form);
            } else {
                awardMedalDAO.saveNonSrCaseDtlsData(form);
            }

            String path = "";
            if (form.getAwardMedalTypeId().equals("07")) {
                path = "/policemodule/award/DgpsDiscsAward";
            } else if (form.getAwardMedalTypeId().equals("14")) {
                path = "/policemodule/award/CmExcellenceInvestigation";
            } else if (form.getAwardMedalTypeId().equals("09")) {
                path = "/policemodule/award/PresidentPoliceAward";
            }
            String profilePhotoPath = servletContext.getInitParameter("PhotoPath");
            form = awardMedalDAO.getAwardeeData(form.getRewardMedalId(),profilePhotoPath);
            //srCaseList = awardMedalDAO.getSRCaseList(form.getRewardMedalId());
            invstCaseList = awardMedalDAO.investigatedCaseList(form.getRewardMedalId());
            nonSrCaseList = awardMedalDAO.getNonSrCaseList(form.getRewardMedalId());
            mav = new ModelAndView(path, "AwardMedalListForm", form);
            // mav.addObject("srCaseList", srCaseList);
            mav.addObject("invstCaseList", invstCaseList);
            mav.addObject("nonSrCaseList", nonSrCaseList);
            mav.addObject("categoryList", categoryDAO.getCategoryList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "editSRCases.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public void editSRCases(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form, @RequestParam("srCaseId") String srCaseId, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {

            form = awardMedalDAO.getSRCaseData(srCaseId);
            JSONObject job = new JSONObject(form);
            out = response.getWriter();
            out.write(job.toString());

            //mv.setViewName("/leaveAccount/PeriodicLeaveCredit");
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "editNonSRCases.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public void editNonSRCases(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form, @RequestParam("nonSrcaseId") String nonSrcaseId, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {

            form = awardMedalDAO.getNonSRCaseData(nonSrcaseId);
            JSONObject job = new JSONObject(form);
            out = response.getWriter();
            out.write(job.toString());

            //mv.setViewName("/leaveAccount/PeriodicLeaveCredit");
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "editInvestigatedCases.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public void editInvestigatedCases(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form, @RequestParam("invstCaseId") String invstCaseId, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {

            form = awardMedalDAO.getInvestigatedCaseData(invstCaseId);
            JSONObject job = new JSONObject(form);
            out = response.getWriter();
            out.write(job.toString());

            //mv.setViewName("/leaveAccount/PeriodicLeaveCredit");
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "deleteSRForm.htm", method = {RequestMethod.GET, RequestMethod.POST}, params = {"action=Delete"})
    public ModelAndView deleteSRCases(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form, @RequestParam("srCaseId") String srCaseId, @RequestParam("awardMedalTypeId") String awardMedalTypeId, @RequestParam("rewardMedalId") String rewardMedalId, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        //List srCaseList = new ArrayList();
        List invstCaseList = new ArrayList();
        List nonSrCaseList = new ArrayList();
        try {

            awardMedalDAO.deleteSRCaseData(srCaseId);
            String path = "";
            if (awardMedalTypeId.equals("07")) {
                path = "/policemodule/award/DgpsDiscsAward";
            } else if (awardMedalTypeId.equals("14")) {
                path = "/policemodule/award/CmExcellenceInvestigation";
            } else if (awardMedalTypeId.equals("09")) {
                path = "/policemodule/award/PresidentPoliceAward";
            }
            String profilePhotoPath = servletContext.getInitParameter("PhotoPath");
            form = awardMedalDAO.getAwardeeData(rewardMedalId,profilePhotoPath);
            //srCaseList = awardMedalDAO.getSRCaseList(form.getRewardMedalId());
            invstCaseList = awardMedalDAO.investigatedCaseList(form.getRewardMedalId());
            nonSrCaseList = awardMedalDAO.getNonSrCaseList(form.getRewardMedalId());
            mav = new ModelAndView(path, "AwardMedalListForm", form);
            //mav.addObject("srCaseList", srCaseList);
            mav.addObject("invstCaseList", invstCaseList);
            mav.addObject("nonSrCaseList", nonSrCaseList);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return mav;
    }

    @RequestMapping(value = "deleteInvstCase.htm", method = {RequestMethod.GET, RequestMethod.POST}, params = {"action=Delete"})
    public ModelAndView deleteInvstCase(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form, @RequestParam("invstcaseId") String invstcaseId, @RequestParam("awardMedalTypeId") String awardMedalTypeId, @RequestParam("rewardMedalId") String rewardMedalId, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        //List srCaseList = new ArrayList();
        List invstCaseList = new ArrayList();
        List nonSrCaseList = new ArrayList();
        try {

            awardMedalDAO.deleteInvestigatedCaseData(invstcaseId);
            String path = "";
            if (awardMedalTypeId.equals("07")) {
                path = "/policemodule/award/DgpsDiscsAward";
            } else if (awardMedalTypeId.equals("14")) {
                path = "/policemodule/award/CmExcellenceInvestigation";
            } else if (awardMedalTypeId.equals("09")) {
                path = "/policemodule/award/PresidentPoliceAward";
            }
            String profilePhotoPath = servletContext.getInitParameter("PhotoPath");
            form = awardMedalDAO.getAwardeeData(rewardMedalId,profilePhotoPath);
            //srCaseList = awardMedalDAO.getSRCaseList(form.getRewardMedalId());
            invstCaseList = awardMedalDAO.investigatedCaseList(form.getRewardMedalId());
            nonSrCaseList = awardMedalDAO.getNonSrCaseList(form.getRewardMedalId());
            mav = new ModelAndView(path, "AwardMedalListForm", form);
            //mav.addObject("srCaseList", srCaseList);
            mav.addObject("invstCaseList", invstCaseList);
            mav.addObject("nonSrCaseList", nonSrCaseList);
            mav.addObject("categoryList", categoryDAO.getCategoryList());
        } catch (Exception e) {
            e.printStackTrace();

        }
        return mav;
    }

    @RequestMapping(value = "deleteNonSrCaseCase.htm", method = {RequestMethod.GET, RequestMethod.POST}, params = {"action=Delete"})
    public ModelAndView deleteNonSrCaseCase(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form, @RequestParam("nonSrcaseId") String nonSrcaseId, @RequestParam("awardMedalTypeId") String awardMedalTypeId, @RequestParam("rewardMedalId") String rewardMedalId, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        List srCaseList = new ArrayList();
        List invstCaseList = new ArrayList();
        List nonSrCaseList = new ArrayList();
        try {

            awardMedalDAO.deleteNonSrCaseData(nonSrcaseId);
            String path = "";
            if (awardMedalTypeId.equals("07")) {
                path = "/policemodule/award/DgpsDiscsAward";
            } else if (awardMedalTypeId.equals("14")) {
                path = "/policemodule/award/CmExcellenceInvestigation";
            } else if (awardMedalTypeId.equals("09")) {
                path = "/policemodule/award/PresidentPoliceAward";
            }
            String profilePhotoPath = servletContext.getInitParameter("PhotoPath");
            form = awardMedalDAO.getAwardeeData(rewardMedalId,profilePhotoPath);
            srCaseList = awardMedalDAO.getSRCaseList(form.getRewardMedalId());
            invstCaseList = awardMedalDAO.investigatedCaseList(form.getRewardMedalId());
            nonSrCaseList = awardMedalDAO.getNonSrCaseList(form.getRewardMedalId());
            mav = new ModelAndView(path, "AwardMedalListForm", form);
            mav.addObject("srCaseList", srCaseList);
            mav.addObject("invstCaseList", invstCaseList);
            mav.addObject("nonSrCaseList", nonSrCaseList);
            mav.addObject("categoryList", categoryDAO.getCategoryList());
        } catch (Exception e) {
            e.printStackTrace();

        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "downloadJudgementCopy.htm")
    public void downloadJudgementCopy(HttpServletResponse response, @RequestParam("attachId") int docId) {
        FileAttribute fa = null;
        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();
            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getInvstigateCaseDoc(filepath, docId);
            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "awardormedalForm", params = "btn=Delete", method = RequestMethod.POST)
    public ModelAndView awardormedalDeleteForm(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        ModelAndView mav = new ModelAndView();

        List medalList = new ArrayList();
        try {
            awardMedalDAO.deleteAwardeeDetails(Integer.parseInt(form.getRewardMedalId()));
            mav = new ModelAndView("redirect:/awardormedalList.htm?awardMedalTypeId=" + form.getAwardMedalTypeId() + "&awardYear=" + form.getAwardYear());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "downloadServiceBookCopyForAward")
    public void downloadServiceBookCopy(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getDocument(filepath, "SB", nominationDetailId);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "downloadDPCForAward")
    public void downloadDPCForAward(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getDocument(filepath, "DPC", nominationDetailId);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "downloadCCROLLForAward")
    public void downloadCCROLLForAward(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getDocument(filepath, "CCROLL", nominationDetailId);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "downloadSrDocument")
    public void downloadSrDocument(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getDocument(filepath, "SRD", nominationDetailId);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "downloadNonSrDocument")
    public void downloadNonSrDocument(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getDocument(filepath, "NSRD", nominationDetailId);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "downloadProceedingmeetingDoc")
    public void downloadProceedingmeetingDoc(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getDocument(filepath, "PROC", nominationDetailId);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "downloadCertificateDocument")
    public void downloadCertificateDocument(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getDocument(filepath, "CRTR", nominationDetailId);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "downloadIntegrityDocument")
    public void downloadIntegrityDocument(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getDocument(filepath, "ICD", nominationDetailId);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "downloadMedicalDocument")
    public void downloadMedicalDocument(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getDocument(filepath, "MED", nominationDetailId);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "downloadMedalPunishmentDocument")
    public void downloadMedalPunishmentDocument(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getDocument(filepath, "PUN", nominationDetailId);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "downloadEnquiryDocument")
    public void downloadEnquiryDocument(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getDocument(filepath, "ENQ", nominationDetailId);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "downloadDISCPCDocument")
    public void downloadDISCPCDocument(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getDocument(filepath, "DISCPC", nominationDetailId);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "downloadCourtCaseDocument")
    public void downloadCourtCaseDocument(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getDocument(filepath, "COURT", nominationDetailId);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "downloadACRGradingDocument")
    public void downloadACRGradingDocument(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId, @RequestParam("doctype") String doctype) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getDocument(filepath, doctype, nominationDetailId);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "downloadCCROLLMultipleForAward")
    public void downloadCCROLLMultipleForAward(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId, @RequestParam("doctype") String doctype) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getDocument(filepath, doctype, nominationDetailId);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "downloadMedicalCategoryDocument")
    public void downloadMedicalCategoryDocument(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getDocument(filepath, "MEDCAT", nominationDetailId);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "downloadMajorPunishmentForAward")
    public void downloadMajorPunishmentForAward(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getDocument(filepath, "MAJORPUNISH", nominationDetailId);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "downloadMinorPunishmentForAward")
    public void downloadMinorPunishmentForAward(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getDocument(filepath, "MINORPUNISH", nominationDetailId);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "submit2rangeOffice")
    public ModelAndView submit2rangeOffice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        ModelAndView mav = new ModelAndView();
        try {
            awardMedalDAO.submit2Range((Integer.parseInt(form.getRewardMedalId())), lub.getLoginoffcode());
            mav = new ModelAndView("redirect:/awardormedalList.htm?awardMedalTypeId=" + form.getAwardMedalTypeId() + "&awardYear=" + form.getAwardYear());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "awardormedalListForRangeOffice")
    public ModelAndView awardormedalListForRangeOffice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        ModelAndView mav = new ModelAndView();

        List awardeeList = new ArrayList();
        List awardTypeList = new ArrayList();
        try {
            awardTypeList = awardMedalDAO.getAwardTypeList();
            if (form.getAwardYear() != null && !form.getAwardYear().equals("")) {
                awardeeList = awardMedalDAO.getSubmittedAwardListForRange(lub.getLoginoffcode(), form.getAwardMedalTypeId(), form.getAwardYear(), form.getSltAwardOccasion());
            }
            mav = new ModelAndView("/policemodule/award/AwardListSubmitted2Range", "AwardMedalListForm", form);
            mav.addObject("awardeeList", awardeeList);
            mav.addObject("awardType", awardTypeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "RevertAwardMedalFromRange")
    public String RevertNominationFromRange(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        try {
            if (form.getAwardMedalTypeId() != null && !form.getAwardMedalTypeId().equals("")) {
                awardMedalDAO.revertNominationDataByRangeOffice(form);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/awardormedalListForRangeOffice.htm?action=Search";
    }

    @RequestMapping(value = "awardormedalFormLoadForRange")
    public ModelAndView awardormedalFormLoadForRange(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        ModelAndView mav = new ModelAndView();
        List invstCaseList = new ArrayList();
        // List srCaseList = new ArrayList();
        List nonSrCaseList = new ArrayList();

        try {
            String path = "";

            if (form.getAwardMedalTypeId().equals("07")) {
                path = "/policemodule/award/DgpsDiscsAwardForRange";
            } else if (form.getAwardMedalTypeId().equals("14")) {
                path = "/policemodule/award/CmExcellenceInvestigationForRange";
            } else if (form.getAwardMedalTypeId().equals("09")) {
                path = "/policemodule/award/PresidentPoliceAwardForRange";
            } else if (form.getAwardMedalTypeId().equals("06")) {
                path = "/policemodule/award/GovernorPoliceAwardForRange";
            }
            String profilePhotoPath = servletContext.getInitParameter("PhotoPath");
            form = awardMedalDAO.getAwardeeData(form.getRewardMedalId(),profilePhotoPath);
            //srCaseList = awardMedalDAO.getSRCaseList(form.getRewardMedalId());
            invstCaseList = awardMedalDAO.investigatedCaseList(form.getRewardMedalId());
            nonSrCaseList = awardMedalDAO.getNonSrCaseList(form.getRewardMedalId());
            mav = new ModelAndView(path, "AwardMedalListForm", form);
            //mav.addObject("srCaseList", srCaseList);
            mav.addObject("invstCaseList", invstCaseList);
            mav.addObject("nonSrCaseList", nonSrCaseList);
            mav.addObject("categoryList", categoryDAO.getCategoryList());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "recommendAwardByRangeForm", method = RequestMethod.POST)
    public ModelAndView recommendAwardByRangeForm(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        ModelAndView mav = new ModelAndView();
        try {
            awardMedalDAO.updateAwardDataByRange(form);
            mav = new ModelAndView("redirect:/awardormedalListForRangeOffice.htm?awardMedalTypeId=" + form.getAwardMedalTypeId() + "&awardYear=" + form.getAwardYear() + "&sltAwardOccasion=" + form.getSltAwardOccasion());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "monitorAwardListForDgOffice")
    public ModelAndView monitorAwardListForDgOffice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        ModelAndView mav = new ModelAndView();
        try {
            if (lub.getLoginuserid().equalsIgnoreCase("adgcrimebbs")) {
                if (form.getAwardYear() != null && !form.getAwardYear().equals("")) {
                    //awardeeList = awardMedalDAO.getSubmittedAwardListForRange(lub.getLoginoffcode(), form.getAwardMedalTypeId(), form.getAwardYear());

                }
                mav = new ModelAndView("/policemodule/award/MonitorAwardByCrimeBranch", "AwardMedalListForm", form);
            } else {
                if (form.getAwardYear() != null && !form.getAwardYear().equals("")) {
                    //awardeeList = awardMedalDAO.getSubmittedAwardListForRange(lub.getLoginoffcode(), form.getAwardMedalTypeId(), form.getAwardYear());

                }
                mav = new ModelAndView("/policemodule/award/MonitorAwardByDGoffice", "AwardMedalListForm", form);
            }

            //mav.addObject("awardeeList", awardeeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "RedirectActionAwardViewPageForDGOffice")
    public ModelAndView RedirectActionAwardViewPageForDGOffice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {
        ModelAndView mav = new ModelAndView();
        String path = "";
        try {
            if (form.getSltActionName().equalsIgnoreCase("1")) {
                path = "redirect:/getAwardCompletedList.htm?awardMedalTypeId=" + form.getAwardMedalTypeId() + "&awardYear=" + form.getAwardYear() + "&sltAwardOccasion=" + form.getSltAwardOccasion();
            } else if (form.getSltActionName().equalsIgnoreCase("2")) {
                path = "redirect:/getAwardRecomendationCompletedList.htm?awardMedalTypeId=" + form.getAwardMedalTypeId() + "&awardYear=" + form.getAwardYear() + "&sltAwardOccasion=" + form.getSltAwardOccasion();
            } else if (form.getSltActionName().equalsIgnoreCase("3")) {
                path = "redirect:/getAwardRecomendationNotCompletedByRange.htm?awardMedalTypeId=" + form.getAwardMedalTypeId() + "&awardYear=" + form.getAwardYear() + "&sltAwardOccasion=" + form.getSltAwardOccasion();
            } else if (form.getSltActionName().equalsIgnoreCase("4")) {
                path = "redirect:/downloadExcelFormatAwardBroadsheet.htm?awardMedalTypeId=" + form.getAwardMedalTypeId() + "&awardYear=" + form.getAwardYear() + "&sltAwardOccasion=" + form.getSltAwardOccasion();
            }
            mav = new ModelAndView(path);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "getAwardCompletedList")
    public ModelAndView getAwardCompletedList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        ModelAndView mav = new ModelAndView();

        List awardeeList = new ArrayList();
        List districtList = new ArrayList();
        try {
            if (form.getAwardYear() != null && !form.getAwardYear().equals("")) {
                if (form.getOffCode() != null && !form.getOffCode().equals("")) {
                    awardeeList = awardMedalDAO.getAwardeeCompletedList(form.getAwardMedalTypeId(), form.getAwardYear(), form.getOffCode(), form.getSltAwardOccasion());
                } else {
                    awardeeList = awardMedalDAO.getAwardeeCompletedList(form.getAwardMedalTypeId(), form.getAwardYear(), form.getSltAwardOccasion());
                }
                districtList = awardMedalDAO.getDistEstList(form.getAwardMedalTypeId(), form.getAwardYear());
            }

            mav = new ModelAndView("/policemodule/award/AwardCompletedList", "AwardMedalListForm", form);
            mav.addObject("districtList", districtList);
            mav.addObject("awardeeList", awardeeList);
            mav.addObject("categoryList", categoryDAO.getCategoryList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "getAwardRecomendationCompletedList")
    public ModelAndView getAwardRecomendationCompletedList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        ModelAndView mav = new ModelAndView();

        List awardeeList = new ArrayList();
        List rangeList = new ArrayList();
        try {
            if (form.getAwardYear() != null && !form.getAwardYear().equals("")) {
                if (form.getSubmittedRangeOff() != null && !form.getSubmittedRangeOff().equals("")) {
                    awardeeList = awardMedalDAO.getAwardeeCompletedByRangeList(form.getAwardMedalTypeId(), form.getAwardYear(), form.getSubmittedRangeOff(), form.getSltAwardOccasion());
                } else {
                    awardeeList = awardMedalDAO.getAwardeeCompletedByRangeList(form.getAwardMedalTypeId(), form.getAwardYear(), form.getSltAwardOccasion());
                }
                rangeList = awardMedalDAO.getRangeList(form.getAwardMedalTypeId(), form.getAwardYear());
            }

            mav = new ModelAndView("/policemodule/award/AwardCompletedRecomendationCompletedList", "AwardMedalListForm", form);
            mav.addObject("awardeeList", awardeeList);
            mav.addObject("rangeList", rangeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "getAwardRecomendationNotCompletedByRange")
    public ModelAndView getAwardRecomendationNotCompletedByRange(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        ModelAndView mav = new ModelAndView();

        List awardeeList = new ArrayList();
        try {
            if (form.getAwardYear() != null && !form.getAwardYear().equals("")) {
                awardeeList = awardMedalDAO.getAwardeeListNotCompletedByRange(form.getAwardMedalTypeId(), form.getAwardYear(), form.getSltAwardOccasion());
            }
            mav = new ModelAndView("/policemodule/award/RecommendationPendingatRangeLevel", "AwardMedalListForm", form);
            mav.addObject("awardeeList", awardeeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "downloadExcelFormatAwardBroadsheet")
    public void downloadExcelFormatAwardBroadsheet(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        response.setContentType("application/vnd.ms-excel");

        OutputStream out = null;

        try {

            out = response.getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            if (form.getAwardMedalTypeId() != null && form.getAwardMedalTypeId().equals("09")) {
                response.setHeader("Content-Disposition", "attachment; filename=President_Police_Medal_BroadSheet" + form.getAwardYear() + ".xls");
            } else if (form.getAwardMedalTypeId() != null && form.getAwardMedalTypeId().equals("06")) {
                response.setHeader("Content-Disposition", "attachment; filename=Governor_Medal_BroadSheet" + form.getAwardYear() + ".xls");
            } else {
                response.setHeader("Content-Disposition", "attachment; filename=BroadSheet" + form.getAwardYear() + ".xls");
            }

            awardMedalDAO.downloadBroadSheetForAwardMedal(workbook, form.getAwardMedalTypeId(), form.getAwardYear(), form.getSltAwardOccasion());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "awardormedalFormLoadDGP")
    public ModelAndView awardormedalFormLoadDGP(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        ModelAndView mav = new ModelAndView();

        List medalList = new ArrayList();
        try {
            String path = "";

            if (form.getAwardMedalTypeId().equals("07")) {
                path = "/policemodule/award/ViewAwardMedalByDGP";
            } else if (form.getAwardMedalTypeId().equals("14")) {
                path = "/policemodule/award/ViewAwardMedalByCrimeBranch";
            } else if (form.getAwardMedalTypeId().equals("09")) {
                path = "/policemodule/award/PresidentPoliceAwardViewByDGP";
            } else if (form.getAwardMedalTypeId().equals("06")) {
                path = "/policemodule/award/GovernorPoliceAwardForDGP";
            }
            
            String profilePhotoPath = servletContext.getInitParameter("PhotoPath");
            form = awardMedalDAO.getAwardeeData(form.getRewardMedalId(),profilePhotoPath);

            mav = new ModelAndView(path, "AwardMedalListForm", form);
            mav.addObject("categoryList", categoryDAO.getCategoryList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "awardormedalDGPPDF")
    public ModelAndView awardormedalDGPPDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        ModelAndView mv = new ModelAndView();
        String profilePhotoPath = servletContext.getInitParameter("PhotoPath");
        form = awardMedalDAO.getAwardeeData(form.getRewardMedalId(),profilePhotoPath);
        mv.addObject("formdata", form);
        mv.addObject("profilePhotoPath", profilePhotoPath);
        mv.setViewName("awardMedalPDF");
        return mv;
    }

    @RequestMapping(value = "RevertAwardorMedalFromDGP")
    public String RevertAwardorMedalFromDGP(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        try {
            if (form.getRewardMedalId() != null && !form.getRewardMedalId().equals("")) {
                awardMedalDAO.revertAwardDataByDGOffice(form);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/getAwardCompletedList.htm?awardMedalTypeId=" + form.getAwardMedalTypeId() + "&awardYear=" + form.getAwardYear() + "&sltAwardOccasion=" + form.getSltAwardOccasion();
    }

    @ResponseBody
    @RequestMapping(value = "closePoliceMedal")
    public void ClosePoliceMedal(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("closemedalid") String closemedalid) {

        response.setContentType("text/html");
        PrintWriter out = null;

        try {
            int closestatus = awardMedalDAO.closePoliceMedal(closemedalid);

            out = response.getWriter();
            out.write(closestatus + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
    @ResponseBody
    @RequestMapping(value = "checkStatusForPoliceMedal")
    public void checkStatusForPoliceMedal(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("closemedalid") String closemedalid) {

        response.setContentType("text/html");
        PrintWriter out = null;

        try {
            String  newstatus = awardMedalDAO.CheckStatusOfPoliceMedal(closemedalid);

            out = response.getWriter();
            out.write(newstatus + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
    @ResponseBody
    @RequestMapping(value = "activePoliceMedal")
    public void activePoliceMedal(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("closemedalid") String closemedalid) {

        response.setContentType("text/html");
        PrintWriter out = null;

        try {
            int closestatus = awardMedalDAO.activePoliceMedal(closemedalid);

            out = response.getWriter();
            out.write(closestatus + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
     @ResponseBody
    @RequestMapping(value = "deActivePoliceMedal")
    public void deActivePoliceMedal(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("closemedalid") String closemedalid) {

        response.setContentType("text/html");
        PrintWriter out = null;

        try {
            int closestatus = awardMedalDAO.deActivePoliceMedal(closemedalid);

            out = response.getWriter();
            out.write(closestatus + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "deletePoliceMedalAttachment")
    public void deletePoliceMedalAttachment(HttpServletResponse response, @RequestParam("attchId") int attchId, @RequestParam("doctype") String doctype) {

        response.setContentType("application/json");

        FileAttribute fa = null;

        JSONObject obj = null;
        try {
            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getDocument(filepath, doctype, attchId);

            int deletestatus = awardMedalDAO.deleteRewardAttachment(attchId, doctype, filepath, fa);

            obj = new JSONObject();
            obj.append("deletestatus", deletestatus);
            PrintWriter out = response.getWriter();
            out.write(obj.toString());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "awardormedalForm.htm", params = "btn=Download Certificate", method = RequestMethod.POST)
    public void viewCertificate(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AwardMedalListForm") AwardMedalListForm form) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        PdfWriter writer = null;
        try {
            String profilePhotoPath = servletContext.getInitParameter("PhotoPath");
            form = awardMedalDAO.getAwardeeData(form.getRewardMedalId(),profilePhotoPath);

            response.setHeader("Content-Disposition", "attachment; filename=CERTIFICATE_" + form.getFullname() + ".pdf");

            writer = PdfWriter.getInstance(document, response.getOutputStream());

            document.open();

            PdfPTable table = null;
            PdfPCell cell = null;

            Font f1 = new Font();
            f1.setSize(8);
            f1.setFamily("Times New Roman");

            table = new PdfPTable(4);
            table.setWidths(new int[]{2, 2, 2, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("CERTIFICATE BY RECOMMENDING AUTHORITY", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(4);
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("1. Certified that the integrity of Shri./Smt……………………….. Designation…………………..Son/Daughter of Shri………………………Date of birth…………………..recommended for the award of 'Chief Minister’s Medal for Excellence in Investigation' for the year ….. is above suspicion.", new Font(Font.FontFamily.TIMES_ROMAN, 10)));
            cell.setColspan(4);
            cell.setFixedHeight(35);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("2.He/She has not been awarded with any stricture/ adverse comment in any Court of law in a case investigated by him", new Font(Font.FontFamily.TIMES_ROMAN, 10)));
            cell.setColspan(4);
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("3.It is further certified that no judicial or departmental proceedings are being contemplated/pending against him. Similarly, no vigilance case is being contemplated/pending against him", new Font(Font.FontFamily.TIMES_ROMAN, 10)));
            cell.setColspan(4);
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("4.It is also certified that the recommendee has not been given any major Penalty and has not been awarded with more than one minor punishment in last 5 years", new Font(Font.FontFamily.TIMES_ROMAN, 10)));
            cell.setColspan(4);
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("5.It is also certified that the character and antecedents (of the proposed awardee) has been duly verified and nothing adverse is reported against him. No adverse entry exists in the ACR of the officer for last 5 years", new Font(Font.FontFamily.TIMES_ROMAN, 10)));
            cell.setColspan(4);
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("6.It is also certified that he has not been awarded 'Chief Minister’s Medal for Excellence in Investigation' earlier", new Font(Font.FontFamily.TIMES_ROMAN, 10)));
            cell.setColspan(4);
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(50);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Signature..........................", new Font(Font.FontFamily.TIMES_ROMAN, 10)));
            cell.setColspan(4);
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(30);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Name & Designation.........................", new Font(Font.FontFamily.TIMES_ROMAN, 10)));
            cell.setColspan(4);
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(30);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "downloadPerformaBdocument")
    public void downloadPerformaBdocument(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getDocument(filepath, "PerformaB", nominationDetailId);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "downloadPerformaCdocument")
    public void downloadPerformaCdocument(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = awardMedalDAO.getDocument(filepath, "PerformaC", nominationDetailId);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
