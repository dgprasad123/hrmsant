/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.policemodule;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.ZipFileAttribute;
import hrms.dao.fiscalyear.FiscalYearDAO;
import hrms.dao.master.CategoryDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.policemodule.NominationRollDAO;
import hrms.dao.policemodule.NominationRollPDFDAO;
import hrms.dao.policemodule.NominationRollRecommendCheckListDAO;
import hrms.model.common.FileAttribute;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.policemodule.ASINominationForm;
import hrms.model.policemodule.EmployeeDetailsForRank;
import hrms.model.policemodule.NominationDifferentDisciplinaryProceedingList;
import hrms.model.policemodule.NominationForm;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
 * @author Surendra
 */
@Controller
@SessionAttributes({"LoginUserBean"})
public class NominationRollController implements ServletContextAware {

    @Autowired
    public NominationRollDAO nominationRollDAO;

    @Autowired
    NominationRollRecommendCheckListDAO nominationRollRecommendCheckListDAO;

    @Autowired
    public DistrictDAO districtDAO;

    @Autowired
    public CategoryDAO categoryDAO;

    @Autowired
    public PostDAO postDao;

    @Autowired
    FiscalYearDAO fiscalDAO;

    @Autowired
    NominationRollPDFDAO nominationRollPDFDAO;

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @RequestMapping(value = "nominationrollList")
    public ModelAndView nominationrollList(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {
        ModelAndView mav = new ModelAndView();
        List currentRankList = new ArrayList();
        try {

            currentRankList = nominationRollDAO.getCurrentRankList();
            List fiscyear = fiscalDAO.getFiscalYearListForPoliceNomination();
            List newRanklist = nominationRollDAO.getNominatedRankList(emp.getSltpostName());
            List rangeOffList = nominationRollDAO.getRangeOfficeList(lub.getLoginoffcode());
            mav = new ModelAndView("/policemodule/NominationRoll", "EmpDetNom", emp);
            mav.addObject("currentRankList", currentRankList);
            mav.addObject("fiscyear", fiscyear);
            mav.addObject("newRanklist", newRanklist);
            mav.addObject("rangeOffList", rangeOffList);
            //mav.addObject("rangeOffList",rangeOffList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "nominationrollListForGroupD2JuniorClerk")
    public ModelAndView nominationrollListForGroupD2JuniorClerk(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {
        ModelAndView mav = new ModelAndView();

        List currentRankList = new ArrayList();
        List fiscyear = fiscalDAO.getFiscalYearListForPoliceNomination();
        try {

            currentRankList = nominationRollDAO.getCurrentRankList();
            mav = new ModelAndView("/policemodule/NominationRollForGroupD2JuniorClerk", "EmpDetNom", emp);
            mav.addObject("currentRankList", currentRankList);
            mav.addObject("fiscyear", fiscyear);
            //mav.addObject("rangeOffList",rangeOffList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "getNominationForRankListJSON")
    public String getNominationForRankListJSON(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {
        JSONArray json = null;
        try {
            List newRanklist = nominationRollDAO.getNominatedRankList(emp.getSltpostName());

            json = new JSONArray(newRanklist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getNominationForAllRankListJSON")
    public String getNominationForAllRankListJSON(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {
        JSONArray json = null;
        try {
            List newRanklist = nominationRollDAO.getAllNominatedRankList(emp.getSltpostName());

            json = new JSONArray(newRanklist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @RequestMapping(value = "submitNominationForFieldOffice", params = {"action=Search"})
    public ModelAndView getNominationListRankWise(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp, @RequestParam Map<String, String> requestParams) {
        ModelAndView mav = new ModelAndView();
        List nominationList = new ArrayList();
        List rangeOffList = new ArrayList();
        List currentRankList = new ArrayList();

        try {
            currentRankList = nominationRollDAO.getCurrentRankList();
            List newRanklist = nominationRollDAO.getNominatedRankList(emp.getSltpostName());
            List fiscyear = fiscalDAO.getFiscalYearListForPoliceNomination();
            if (emp.getSltpostName().equals("000000")) {
                nominationList = nominationRollDAO.getNominationListForGroupD(lub.getLoginoffcode(), emp.getSltNominationForPost(), emp.getFiscalyear());
                //mav = new ModelAndView("/policemodule/NominationRollForGroupD2JuniorClerk", "EmpDetNom", emp);
            } else {
                nominationList = nominationRollDAO.getNominationList(lub.getLoginoffcode(), emp.getSltNominationForPost(), emp.getFiscalyear());

            }
            rangeOffList = nominationRollDAO.getRangeOfficeList(lub.getLoginoffcode());
            mav = new ModelAndView("/policemodule/NominationRoll", "EmpDetNom", emp);
            mav.addObject("nominationList", nominationList);
            mav.addObject("rangeOffList", rangeOffList);
            mav.addObject("currentRankList", currentRankList);
            mav.addObject("newRanklist", newRanklist);
            mav.addObject("fiscyear", fiscyear);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "getsearchNominationForFieldOffice.htm", method = RequestMethod.POST)
    public void getsearchNominationForFieldOffice(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp, HttpServletResponse response) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        List nominationList = new ArrayList();

        if (emp.getSltpostName().equals("000000")) {
            nominationList = nominationRollDAO.getNominationListForGroupD(lub.getLoginoffcode(), emp.getSltNominationForPost(), emp.getFiscalyear());
        } else {
            nominationList = nominationRollDAO.getNominationList(lub.getLoginoffcode(), emp.getSltNominationForPost(), emp.getFiscalyear());
        }

        //JSONObject obj = new JSONObject(nominationList);
        JSONArray obj = new JSONArray(nominationList);
        //obj.append("nominationList", nominationList);

        out = response.getWriter();
        out.write(obj.toString());
        out.close();
        out.flush();

    }

    @RequestMapping(value = "searchEmployeeFornomination")
    public ModelAndView searchEmployeeFornomination(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp
    ) {

        ModelAndView mav = new ModelAndView();

        try {

            mav = new ModelAndView("/policemodule/AddEmployeeforNomination", "EmpDetNom", emp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    /*@ResponseBody
     @RequestMapping(value = "getEmployeeListToAddForNomination")
     public void getEmployeeListToAddForNomination(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp
     ) {

     response.setContentType("application/json");
     PrintWriter out = null;
     JSONArray json = null;

     try {
     //HashMap emplist = nominationRollDAO.getEmployeeListGpfNowise(emp.getEmpId());
     json = new JSONArray(emplist);
     out = response.getWriter();
     out.write(json.toString());
     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     out.flush();
     out.close();
     }
     }*/
    @ResponseBody
    @RequestMapping(value = "getEmployeeListToAddForNomination")
    public void getEmployeeListToAddForNomination(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List emplist = nominationRollDAO.getEmployeeListGpfNowise(emp.getEmpId());

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

    @RequestMapping(value = "nominationrollListForRangeOffice")
    public ModelAndView nominationrollListForRangeOffice(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp
    ) {

        ModelAndView mav = new ModelAndView();

        List nominationList = new ArrayList();
        try {
            List newRanklist = nominationRollDAO.getAllNominatedRankList();
            List fiscyear = fiscalDAO.getFiscalYearListForPoliceNomination();
            mav = new ModelAndView("/policemodule/NominationRollForRangeOffice", "EmpDetNom", emp);
            mav.addObject("newRanklist", newRanklist);
            mav.addObject("fiscyear", fiscyear);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "viewnominationFormControllerForRangeOffice", params = {"action=Search"})
    public ModelAndView loadNominationListForRangeOffice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp
    ) {

        ModelAndView mav = new ModelAndView();
        String filepath = servletContext.getInitParameter("policeDocPath");
        List nominationList = new ArrayList();
        List fiscyear = fiscalDAO.getFiscalYearListForPoliceNomination();
        try {
            if (emp.getSltNominationForPost() != null && !emp.getSltNominationForPost().equals("")) {
                nominationList = nominationRollDAO.getSubmittedNominationList(lub.getLoginoffcode(), emp.getSltpostName(), emp.getSltNominationForPost(), emp.getFiscalyear());
            }

            List newRanklist = nominationRollDAO.getAllNominatedRankList();

            mav = new ModelAndView("/policemodule/NominationRollForRangeOffice", "EmpDetNom", emp);
            mav.addObject("nominationList", nominationList);
            mav.addObject("newRanklist", newRanklist);
            mav.addObject("fiscyear", fiscyear);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "submitNominationForFieldOffice", params = {"action=New Nomination"})
    public ModelAndView createNominationroll(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        ModelAndView mav = new ModelAndView();
        List empList = new ArrayList();

        try {

            List currentRankList = nominationRollDAO.getCurrentRankList(emp.getSltpostName());
            List newRanklist = nominationRollDAO.getNominatedRankList(emp.getSltpostName());
            empList = nominationRollDAO.getEmployeeListRankWise(emp.getSltpostName(), lub.getLoginoffcode());
            mav = new ModelAndView("/policemodule/NominationRollDetailPage", "EmpDetNom", emp);
            mav.addObject("currentRankList", currentRankList);
            mav.addObject("newRanklist", newRanklist);
            mav.addObject("empList", empList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "getselectedEmployeeForNomination.htm", method = RequestMethod.POST)
    public void getselectedEmployeeForNomination(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp, HttpServletResponse response) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;

        List empList = null;
        if (emp.getNominationMasterId() == null || emp.getNominationMasterId().equals("")) {
            empList = nominationRollDAO.getEmployeeListRankWise(emp.getSltpostName(), lub.getLoginoffcode());
        } else {
            empList = nominationRollDAO.getCreatedNominationList(lub.getLoginoffcode(), emp.getSltpostName(), Integer.parseInt(emp.getNominationMasterId()));
        }
        JSONArray obj = new JSONArray(empList);
        out = response.getWriter();
        out.write(obj.toString());
        out.close();
        out.flush();

    }

    @RequestMapping(value = "createNominationroll", params = {"action=Get Employee"})
    public ModelAndView selectNominationEmployee(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp
    ) {

        ModelAndView mav = new ModelAndView();

        List empList = new ArrayList();
        try {
            List currentRankList = nominationRollDAO.getCurrentRankList(emp.getSltpostName());
            List newRanklist = nominationRollDAO.getNominatedRankList(emp.getSltpostName());
            empList = nominationRollDAO.getEmployeeListRankWise(emp.getSltpostName(), lub.getLoginoffcode());

            mav = new ModelAndView("/policemodule/NominationRollDetailPage", "EmpDetNom", emp);
            mav.addObject("currentRankList", currentRankList);
            mav.addObject("newRanklist", newRanklist);
            mav.addObject("empList", empList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "createNominationroll", params = {"action=Add"})
    public ModelAndView addselectedNominationEmployee(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp
    ) {

        ModelAndView mav = new ModelAndView();

        List empList = new ArrayList();
        try {

            nominationRollDAO.addSearchEmployee2List(emp.getGpfno(), lub.getLoginoffcode(), lub.getLoginoffname(), emp.getSltpostName(), emp.getSltNominationForPost(), emp.getFiscalyear());
            empList = nominationRollDAO.getEmployeeListRankWise(emp.getSltpostName(), lub.getLoginoffcode());
            List currentRankList = nominationRollDAO.getCurrentRankList(emp.getSltpostName());
            List newRanklist = nominationRollDAO.getNominatedRankList(emp.getSltpostName());
            mav = new ModelAndView("/policemodule/NominationRollDetailPage", "EmpDetNom", emp);
            mav.addObject("currentRankList", currentRankList);
            mav.addObject("newRanklist", newRanklist);
            mav.addObject("empList", empList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "addNominationroll")
    public void addgroupcEmpList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp, HttpServletResponse response, ModelMap model) throws IOException, JSONException {
        response.setContentType("application/json");
        emp.setSubmittedByOffice(lub.getLoginoffname());
        emp.setSubmittedByOfficeCode(lub.getLoginoffcode());
        List empList = new ArrayList();
        nominationRollDAO.addSearchEmployee2List(emp.getGpfno(), lub.getLoginoffcode(), lub.getLoginoffname(), emp.getSltpostName(), emp.getSltNominationForPost(), emp.getFiscalyear());
        empList = nominationRollDAO.getEmployeeListRankWise(emp.getSltpostName(), lub.getLoginoffcode());
        List currentRankList = nominationRollDAO.getCurrentRankList(emp.getSltpostName());
        List newRanklist = nominationRollDAO.getNominatedRankList(emp.getSltpostName());
        JSONObject obj = new JSONObject();
        obj.append("empList", empList);
        obj.append("currentRankList", currentRankList);
        obj.append("newRanklist", newRanklist);
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();
    }

    @RequestMapping(value = "nominationEmployeeListforAdd")
    public ModelAndView getnominationEmployeeList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp, @RequestParam Map<String, String> requestParams
    ) {

        ModelAndView mav = new ModelAndView();

        List empList = new ArrayList();
        try {

            empList = nominationRollDAO.getEmployeeListRankWiseExcludedAlreadyMapped(emp.getSltpostName(), lub.getLoginoffcode(), Integer.parseInt(emp.getNominationMasterId()));
            String postName = postDao.getPostName(requestParams.get("sltpostName"));
            String nominationForPost = postDao.getPostName(requestParams.get("sltNominationForPost"));
            mav = new ModelAndView("/policemodule/NominationEmployeeList", "EmpDetNom", emp);
            mav.addObject("nominationForPost", nominationForPost);
            mav.addObject("postName", postName);
            mav.addObject("empList", empList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "createNominationroll", params = {"action=Delete Nomination"})
    public ModelAndView deleteNominationroll(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp
    ) {

        ModelAndView mav = new ModelAndView();

        try {

            nominationRollDAO.deleteNomination(lub.getLoginoffcode(), Integer.parseInt(emp.getNominationMasterId()));

            mav = new ModelAndView("redirect:/nominationrollList.htm");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "createNominationroll", params = {"action=Create Nomination"})
    public ModelAndView createNominationEmployee(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        ModelAndView mav = new ModelAndView();

        try {

            nominationRollDAO.createNomination(emp.getGpfno(), emp.getSltpostName(), emp.getSltNominationForPost(), lub.getLoginoffcode(), lub.getLoginuserid(), emp.getFiscalyear());
            mav = new ModelAndView("redirect:/nominationrollList.htm?sltpostName=" + emp.getSltpostName() + "&sltNominationForPost=" + emp.getSltNominationForPost() + "&fiscalyear=" + emp.getFiscalyear());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "createNominationroll", params = {"action=Add Employee"})
    public ModelAndView addEmployeetoList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp
    ) {

        ModelAndView mav = new ModelAndView();

        try {

            nominationRollDAO.addNewEmployee(emp.getGpfno(), Integer.parseInt(emp.getNominationMasterId()), emp.getSltpostName());

            mav = new ModelAndView("redirect:/editNominationroll.htm?nominationMasterId=" + emp.getNominationMasterId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "deleteEmployeeFromNominationList")
    public ModelAndView deleteEmployeeFromList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp
    ) {

        ModelAndView mav = new ModelAndView();

        try {

            nominationRollDAO.deleteEmployee(Integer.parseInt(emp.getNominationMasterId()), Integer.parseInt(emp.getNominationDetailId()));

            mav = new ModelAndView("redirect:/editNominationroll.htm?nominationMasterId=" + emp.getNominationMasterId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "downloadNominationFormController")
    public void downloadNominationForm(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam Map<String, String> requestParams, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp
    ) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        String currentPostName = "";
        String promotePostname = "";
        int nominationMasterId = Integer.parseInt(requestParams.get("nominationMasterId"));
        int nominationDetailId = Integer.parseInt(requestParams.get("nominationDetailId"));
        try {
            String filename = "NOMINATION_" + lub.getLoginname() + ".pdf";
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            String currentRankName = nominationRollDAO.getCurrentRankCodeFormDetailTable(nominationMasterId, nominationDetailId);

            if (currentRankName.equals("140070")) {
                currentPostName = "CONSTABLES/LNK/HAV/CI HAV";
                promotePostname = "ASSISTANT SUB INSPECTOR OF POLICE";
                nominationRollPDFDAO.downloadNominationFormForConstableHavtoASI(document, Integer.parseInt(requestParams.get("nominationMasterId")), Integer.parseInt(requestParams.get("nominationDetailId")), currentPostName, promotePostname);
            } else if (currentRankName.equals("140858")) {
                currentPostName = "ASSISTANT SUB INSPECTOR OF POLICE";
                promotePostname = "SUB-INSPECTOR OF POLICE";
                nominationRollPDFDAO.downloadNominationFormforASI2SubInspector(document, Integer.parseInt(requestParams.get("nominationMasterId")), Integer.parseInt(requestParams.get("nominationDetailId")), currentRankName, promotePostname);
            } else if (currentRankName.equals("140293")) {
                currentPostName = "SUB-INSPECTOR OF POLICE";
                promotePostname = "INSPECTOR OF POLICE";
                nominationRollPDFDAO.downloadNominationFormforSI2Inspector(document, Integer.parseInt(requestParams.get("nominationMasterId")), Integer.parseInt(requestParams.get("nominationDetailId")), currentRankName, promotePostname);

            } else if (currentRankName.equals("140599")) {
                currentPostName = "INSPECTOR OF POLICE";
                promotePostname = "DEPUTY SUPERINTENDENT OF POLICE";
                nominationRollPDFDAO.downloadNominationFormforInspector2DSP(document, Integer.parseInt(requestParams.get("nominationMasterId")), Integer.parseInt(requestParams.get("nominationDetailId")), currentRankName, promotePostname);
            } else if (currentRankName.equals("140161")) {
                currentPostName = "JUNIOR CLERK";
                promotePostname = "SENIOR CLERK";
                nominationRollPDFDAO.downloadNominationFormForJuniorClerkToSeniorClerk(document, nominationMasterId, nominationDetailId, currentRankName, promotePostname);
            } else if (currentRankName.equals("140124")) {
                currentPostName = "HAVILDAR";
                promotePostname = "HAVILDAR MAJOR";
                nominationRollPDFDAO.downloadNominationFormforHavildar2HavildarMajor(document, Integer.parseInt(requestParams.get("nominationMasterId")), Integer.parseInt(requestParams.get("nominationDetailId")), currentRankName, promotePostname);
            } else if (currentRankName.equals("141191")) {
                currentPostName = "SUB INSPECTOR OF POLICE(ARMED)";
                promotePostname = "INSPECTOR OF POLICE(ARMED)";
                nominationRollPDFDAO.downloadNominationFormforSIArmedtoInspectorArmed(document, Integer.parseInt(requestParams.get("nominationMasterId")), Integer.parseInt(requestParams.get("nominationDetailId")), currentRankName, promotePostname);
            } else if (currentRankName.equals("140871")) {
                currentPostName = "INSPECTOR OF POLICE(ARMED)";
                promotePostname = "ASSISTANT COMMANDANT";
                nominationRollPDFDAO.downloadNominationFormforInspectorArmed2AssistantCommandant(document, Integer.parseInt(requestParams.get("nominationMasterId")), Integer.parseInt(requestParams.get("nominationDetailId")), currentRankName, promotePostname);
            } else if (currentRankName.equals("140126")) {
                currentPostName = "HAVILDAR MAJOR / ASSISTANT SUB INSPECTOR OF POLICE(ARMED)";
                promotePostname = "SUB INSPECTOR OF POLICE(ARMED)";
                nominationRollPDFDAO.downloadNominationFormforASIArmed2SubInspectorArmed(document, Integer.parseInt(requestParams.get("nominationMasterId")), Integer.parseInt(requestParams.get("nominationDetailId")), currentRankName, promotePostname);
            }
            document.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @RequestMapping(value = "downloadNominationFormByRecommend")
    public void downloadNominationFormByRecommend(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            HttpServletResponse response, @RequestParam Map<String, String> requestParams,
            @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        String currentPostName = "";
        String promotePostname = "";
        int nominationMasterId = Integer.parseInt(requestParams.get("nominationMasterId"));
        int nominationDetailId = Integer.parseInt(requestParams.get("nominationDetailId"));
        try {

            response.setHeader("Content-Disposition", "attachment; filename=NOMINATION_" + lub.getLoginname() + ".pdf");
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

            NominationForm nfm = nominationRollDAO.getEmployeeNominationData(nominationMasterId, nominationDetailId);

            document.open();

            String currentRankName = nominationRollDAO.getCurrentRankCodeFormDetailTable(nominationMasterId, nominationDetailId);
            writer.setPageEvent(new PoliceDocumentHeaderFooter(nfm.getFullname()));

            if (currentRankName.equals("140070")) {
                currentPostName = "CONSTABLES/LNK/HAV/CI HAV";
                promotePostname = "ASSISTANT SUB INSPECTOR OF POLICE";

            } else if (currentRankName.equals("140858")) {
                currentPostName = "ASSISTANT SUB INSPECTOR OF POLICE";
                promotePostname = "SUB-INSPECTOR OF POLICE";

            } else if (currentRankName.equals("140599")) {
                currentPostName = "INSPECTOR OF POLICE";
                promotePostname = "DEPUTY SUPERINTENDENT OF POLICE";

            } else if (currentRankName.equals("140161")) {
                currentPostName = "JUNIOR CLERK";
                promotePostname = "SENIOR CLERK";

            } else if (currentRankName.equals("140293")) {
                currentPostName = "SUB INSPECTOR OF POLICE";
                promotePostname = "INSPECTOR OF POLICE";

            } else if (currentRankName.equals("140124")) {
                currentPostName = "HAVILDAR";
                promotePostname = "HAVILDAR MAJOR";

            } else if (currentRankName.equals("141191")) {
                currentPostName = "SUB INSPECTOR OF POLICE(ARMED)";
                promotePostname = "INSPECTOR OF POLICE(ARMED)";

            } else if (currentRankName.equals("140126")) {
                currentPostName = "ASI(ARMED)/HAVILDAR MAJOR";
                promotePostname = "SUB INSPECTOR OF POLICE(ARMED)";

            } else if (currentRankName.equals("140871")) {
                currentPostName = "INSPECTOR OF POLICE(ARMED)";
                promotePostname = "ASSISTANT COMMANDANT";

            }
            if (currentRankName.equals("140293")) {
                nominationRollPDFDAO.downloadNominationFormByRecommendSI2Inspector(document, Integer.parseInt(requestParams.get("nominationMasterId")), Integer.parseInt(requestParams.get("nominationDetailId")), currentPostName, promotePostname);
            } else if (currentRankName.equals("140070")) {
                nominationRollPDFDAO.downloadNominationFormByRecommendHavildarCons2ASI(document, Integer.parseInt(requestParams.get("nominationMasterId")), Integer.parseInt(requestParams.get("nominationDetailId")), currentPostName, promotePostname);
            } else if (currentRankName.equals("140124")) {
                nominationRollPDFDAO.downloadNominationFormByRecommendHavildar2MajorHavildar(document, Integer.parseInt(requestParams.get("nominationMasterId")), Integer.parseInt(requestParams.get("nominationDetailId")), currentPostName, promotePostname);
            } else if (currentRankName.equals("140858")) {
                nominationRollPDFDAO.downloadNominationFormByRecommendASI2SI(document, Integer.parseInt(requestParams.get("nominationMasterId")), Integer.parseInt(requestParams.get("nominationDetailId")), currentPostName, promotePostname);
            } else if (currentRankName.equals("140599")) {
                nominationRollPDFDAO.downloadNominationFormByRecommendInspector2DSP(document, Integer.parseInt(requestParams.get("nominationMasterId")), Integer.parseInt(requestParams.get("nominationDetailId")), currentPostName, promotePostname);
            } else if (currentRankName.equals("141191")) {
                nominationRollPDFDAO.downloadNominationFormByRecommendSIArmed2InspectorArmed(document, Integer.parseInt(requestParams.get("nominationMasterId")), Integer.parseInt(requestParams.get("nominationDetailId")), currentPostName, promotePostname);
            } else if (currentRankName.equals("140871")) {
                nominationRollPDFDAO.downloadNominationFormByRecommendInspectorArmed2AssistantCommandant(document, Integer.parseInt(requestParams.get("nominationMasterId")), Integer.parseInt(requestParams.get("nominationDetailId")), currentPostName, promotePostname);
            } else if (currentRankName.equals("140126")) {
                nominationRollPDFDAO.downloadNominationFormByRecommendAsiArmedtoSiArmed(document, Integer.parseInt(requestParams.get("nominationMasterId")), Integer.parseInt(requestParams.get("nominationDetailId")), currentPostName, promotePostname);
            } else if (currentRankName.equals("140161")) {
                nominationRollPDFDAO.downloadNominationFormByRecommendJuniorClerktoSeniorClerk(document, Integer.parseInt(requestParams.get("nominationMasterId")), Integer.parseInt(requestParams.get("nominationDetailId")), currentPostName, promotePostname);
            } else {
                nominationRollPDFDAO.downloadNominationFormByRecommend(document, Integer.parseInt(requestParams.get("nominationMasterId")), Integer.parseInt(requestParams.get("nominationDetailId")), currentPostName, promotePostname);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "downloadAllAttachment")
    public void downloadAttachmentInZipFormat(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam Map<String, String> requestParams
    ) {
        response.setContentType("application/zip");

        try {
            String filePath = servletContext.getInitParameter("policeDocPath");
            NominationForm nfm = nominationRollDAO.getEmployeeNominationData(Integer.parseInt(requestParams.get("nominationMasterId")), Integer.parseInt(requestParams.get("nominationDetailId")));
            File directory = new File(filePath);
            ZipFileAttribute zipObj = nominationRollDAO.getAttachmentFiles(nfm.getNominationFormId());
            List<String> al = zipObj.getDiskFileName();
            String zipFilename = nfm.getFullname() + ".zip";
            String files[] = al.toArray(new String[al.size()]);

            byte[] zip = zipFiles(directory, files, zipObj.getOrgGFileName());
            OutputStream out = response.getOutputStream();
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFilename + "\"");
            out.write(zip);
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    private byte[] zipFiles(File directory, String[] files, Map orgFile) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        byte bytes[] = new byte[2048];
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i];
            FileInputStream fis = new FileInputStream(directory.getPath() + "/" + fileName);
            BufferedInputStream bis = new BufferedInputStream(fis);
            zos.putNextEntry(new ZipEntry(orgFile.get(fileName) + ""));
            int bytesRead;
            while ((bytesRead = bis.read(bytes)) != -1) {
                zos.write(bytes, 0, bytesRead);
            }
            zos.closeEntry();
            bis.close();
            fis.close();
        }
        zos.flush();
        baos.flush();
        zos.close();
        baos.close();
        return baos.toByteArray();
    }

    @RequestMapping(value = "downloadAnnextureAForDSPRankController")
    public void downloadAnnextureAForDSPRankController(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam Map<String, String> requestParams, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {

            response.setHeader("Content-Disposition", "attachment; filename=ANNEXTURE-A_ " + lub.getLoginname() + ".pdf");
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            nominationRollPDFDAO.downloadAnnextureAForDSPRank(document, Integer.parseInt(emp.getNominationMasterId()));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "editNominationroll")
    public ModelAndView editNominationroll(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {
        ModelAndView mav = new ModelAndView();
        List nominationList = new ArrayList();
        try {
            emp = nominationRollDAO.getNominationCreatedData(lub.getLoginoffcode(), Integer.parseInt(emp.getNominationMasterId()));
            nominationList = nominationRollDAO.getCreatedNominationList(lub.getLoginoffcode(), emp.getSltpostName(), Integer.parseInt(emp.getNominationMasterId()));
            List currentRankList = nominationRollDAO.getCurrentRankList(emp.getSltpostName());
            List newRanklist = nominationRollDAO.getNominatedRankList(emp.getSltpostName());
            List fiscyear = fiscalDAO.getFiscalYearListForPoliceNomination();
            mav = new ModelAndView("/policemodule/NominationRollDetailPage", "EmpDetNom", emp);
            mav.addObject("empList", nominationList);
            mav.addObject("currentRankList", currentRankList);
            mav.addObject("newRanklist", newRanklist);
            mav.addObject("fiscyear", fiscyear);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "viewNominationrollList")
    public ModelAndView viewNominationrollList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        ModelAndView mav = new ModelAndView();

        List nominationList = new ArrayList();
        try {
            emp = nominationRollDAO.getNominationCreatedData(lub.getLoginoffcode(), Integer.parseInt(emp.getNominationMasterId()));

            nominationList = nominationRollDAO.getSubmittedNominationList(lub.getLoginoffcode(), emp.getSltpostName(), Integer.parseInt(emp.getNominationMasterId()));

            mav = new ModelAndView("/policemodule/NominationRollDetailListViewPage", "EmpDetNom", emp);
            mav.addObject("empList", nominationList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "viewNominationrollForRangeOffice")
    public ModelAndView viewNominationrollForRangeOffice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = new ModelAndView();

        List nominationList = new ArrayList();

        try {

            nominationList = nominationRollDAO.getNominatedEmployeeList(Integer.parseInt(requestParams.get("nominationMasterId")));
            mav = new ModelAndView("/policemodule/ViewProposedNominatioListByRangeOffice", "EmpDetNom", emp);
            mav.addObject("empList", nominationList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "showNominationrollForRangeOffice")
    public ModelAndView showNominationrollForRangeOffice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = new ModelAndView();

        List nominationList = new ArrayList();

        try {

            //emp = nominationRollDAO.getNominationCreatedData(lub.getLoginoffcode(), Integer.parseInt(requestParams.get("nominationMasterId")));
            nominationList = nominationRollDAO.getNominatedEmployeeList(Integer.parseInt(requestParams.get("nominationMasterId")));

            mav = new ModelAndView("/policemodule/ShowProposedNominatioListByRangeOffice", "EmpDetNom", emp);
            mav.addObject("empList", nominationList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "nominationFormController")
    public ModelAndView nominationFormController(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") NominationForm form) {

        ModelAndView mav = new ModelAndView();
        try {
            if (form.getSltNominationForPost().equalsIgnoreCase("140293")) {
                NominationForm nfm = nominationRollDAO.getEmployeeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()));
                if (nfm == null) {
                    nfm = nominationRollDAO.getEmployeeBeforeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()), form.getSltpostName());
                }
                mav = new ModelAndView("/policemodule/ASI2SINominationForm", "nominationForm", nfm);
                /*SUBINSPECTOR TO INSPECTOR NOMINATION*/
            } else if (form.getSltNominationForPost().equalsIgnoreCase("140599")) {
                NominationForm nfm = nominationRollDAO.getEmployeeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()));
                if (nfm == null) {
                    nfm = nominationRollDAO.getEmployeeBeforeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()), form.getSltpostName());
                }
                mav = new ModelAndView("/policemodule/SI2InspectorPoliceNominationForm", "nominationForm", nfm);
            } else if (form.getSltNominationForPost().equalsIgnoreCase("141191")) {
                NominationForm nfm = nominationRollDAO.getEmployeeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()));
                if (nfm == null) {
                    nfm = nominationRollDAO.getEmployeeBeforeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()), form.getSltpostName());
                }

                mav = new ModelAndView("/policemodule/Havildar2SIPoliceNominationForm", "nominationForm", nfm);
            } else if (form.getSltNominationForPost().equalsIgnoreCase("140871")) {
                NominationForm nfm = nominationRollDAO.getEmployeeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()));
                if (nfm == null) {
                    nfm = nominationRollDAO.getEmployeeBeforeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()), form.getSltpostName());
                }

                mav = new ModelAndView("/policemodule/SubInspector2InspectorPoliceNominationForm", "nominationForm", nfm);
            } else if (form.getSltNominationForPost().equalsIgnoreCase("140161")) {
                NominationForm nfm = nominationRollDAO.getEmployeeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()));
                if (nfm == null) {
                    nfm = nominationRollDAO.getEmployeeBeforeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()), form.getSltpostName());
                }

                mav = new ModelAndView("/policemodule/GroupD2JuniorClerkNominationForm", "nominationForm", nfm);
            } else if (form.getSltNominationForPost().equalsIgnoreCase("140255")) {
                NominationForm nfm = nominationRollDAO.getEmployeeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()));
                if (nfm == null) {
                    nfm = nominationRollDAO.getEmployeeBeforeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()), form.getSltpostName());
                }

                mav = new ModelAndView("/policemodule/JuniorClerk2SeniorClerkNominationForm", "nominationForm", nfm);
                /*CONSTABLES/LNK/HAV/CI HAV TO ASI NOMINATION*/
            } else if (form.getSltNominationForPost().equalsIgnoreCase("140858")) {
                NominationForm nfm = nominationRollDAO.getEmployeeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()));
                if (nfm == null) {
                    nfm = nominationRollDAO.getEmployeeBeforeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()), form.getSltpostName());
                }
                mav = new ModelAndView("/policemodule/Havildaraonstable2ASINominationForm", "nominationForm", nfm);
            } else if (form.getSltNominationForPost().equalsIgnoreCase("140126")) {
                NominationForm nfm = nominationRollDAO.getEmployeeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()));
                if (nfm == null) {
                    nfm = nominationRollDAO.getEmployeeBeforeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()), form.getSltpostName());
                }
                mav = new ModelAndView("/policemodule/Havildar2MajorHavildarNominationForm", "nominationForm", nfm);
            } else if (form.getSltNominationForPost().equalsIgnoreCase("140022")) {
                NominationForm nfm = nominationRollDAO.getEmployeeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()));
                if (nfm == null) {
                    nfm = nominationRollDAO.getEmployeeBeforeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()), form.getSltpostName());
                }
                mav = new ModelAndView("/policemodule/InspectorArmed2AssistantCommandantNominationForm", "nominationForm", nfm);
            } else if (form.getSltNominationForPost().equalsIgnoreCase("140090")) {
                NominationForm nfm = nominationRollDAO.getEmployeeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()));
                if (nfm == null) {
                    nfm = nominationRollDAO.getEmployeeBeforeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()), form.getSltpostName());
                }
                mav = new ModelAndView("/policemodule/Inspector2DSPNominationForm", "nominationForm", nfm);
            } else if (form.getSltNominationForPost().equalsIgnoreCase("140251")) {
                NominationForm nfm = nominationRollDAO.getEmployeeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()));
                if (nfm == null) {
                    nfm = nominationRollDAO.getEmployeeBeforeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()), form.getSltpostName());
                }
                mav = new ModelAndView("/policemodule/Aso2SoNominationForm", "nominationForm", nfm);
            } else {
                NominationForm nfm = nominationRollDAO.getEmployeeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()));
                if (nfm == null) {
                    nfm = nominationRollDAO.getEmployeeBeforeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()), form.getSltpostName());
                }

                mav = new ModelAndView("/policemodule/GroupD2JuniorClerkNominationForm", "nominationForm", nfm);
            }

            mav.addObject("districtList", districtDAO.getPoliceDistrictList());
            mav.addObject("categoryList", categoryDAO.getCategoryList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "downloadServiceBookCopy")
    public void downloadServiceBookCopy(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = nominationRollDAO.getDpcDocument(filepath, "SB", nominationDetailId);

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
    @RequestMapping(value = "downloadPunishmentDocument")
    public void downloadPunishmentDocument(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = nominationRollDAO.getDpcDocument(filepath, "PUNISH", nominationDetailId);

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
    @RequestMapping(value = "downloadCCroll")
    public void downloadCCroll(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = nominationRollDAO.getDpcDocument(filepath, "CCROLL", nominationDetailId);

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
    @RequestMapping(value = "downloadDPC")
    public void downloadDPC(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = nominationRollDAO.getDpcDocument(filepath, "DPC", nominationDetailId);

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
    @RequestMapping(value = "downloadServing")
    public void downloadServing(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = nominationRollDAO.getDpcDocument(filepath, "SERV_CHRG", nominationDetailId);

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
    @RequestMapping(value = "downloadFitnessDocument")
    public void downloadFitnessDocument(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = nominationRollDAO.getDpcDocument(filepath, "FITNESS", nominationDetailId);

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
    @RequestMapping(value = "downloadFirstPageServiceBookCopy")
    public void downloadFirstPageServiceBookCopy(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = nominationRollDAO.getDpcDocument(filepath, "FPSB", nominationDetailId);

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
    @RequestMapping(value = "downloadMajorPunishmentForGroupD")
    public void downloadMajorPunishmentForGroupD(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = nominationRollDAO.getDpcDocument(filepath, "MAP", nominationDetailId);

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
    @RequestMapping(value = "downloadMinorPunishmentForGroupD")
    public void downloadMinorPunishmentForGroupD(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = nominationRollDAO.getDpcDocument(filepath, "MIP", nominationDetailId);

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
    @RequestMapping(value = "downloadAccomplishmentCopy")
    public void downloadAccomplishmentCopy(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = nominationRollDAO.getDpcDocument(filepath, "ACCOMP", nominationDetailId);

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
    @RequestMapping(value = "downloadCasteCertificateDocument")
    public void downloadCasteCertificateDocument(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = nominationRollDAO.getDpcDocument(filepath, "CASTE", nominationDetailId);

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
    @RequestMapping(value = "downloadCriminalCasePresentStatusDocument")
    public void downloadCriminalCasePresentStatusDocument(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = nominationRollDAO.getDpcDocument(filepath, "PRESENT_CRIMINAL_STATUS", nominationDetailId);

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
    @RequestMapping(value = "downloadWillingnessCertificateForGroupD")
    public void downloadWillingnessCertificateForGroupD(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = nominationRollDAO.getDpcDocument(filepath, "WILLINGNESS", nominationDetailId);

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
    @RequestMapping(value = "downloadPreliminaryExam")
    public void downloadPreliminaryExam(HttpServletResponse response, @RequestParam("attachId") int nominationDetailId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = nominationRollDAO.getDpcDocument(filepath, "PRELIMEXAM", nominationDetailId);

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

    @RequestMapping(value = "recomendationFormController")
    public ModelAndView recomendationFormController(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") NominationForm form) {

        ModelAndView mav = new ModelAndView();
        try {
            NominationForm nfm = nominationRollDAO.getEmployeeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()));
            if (nfm.getSltNominationForPost().equalsIgnoreCase("140293")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/RecommendNominationFormForASI2SIandSI2INS", "nominationForm", nfm);
                }
                /*Recommend Nomination Form ForSI2Inspector*/
            } else if (nfm.getSltNominationForPost().equalsIgnoreCase("140599")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/RecommendNominationFormForSI2Inspector", "nominationForm", nfm);
                }
            } else if (nfm.getSltNominationForPost().equalsIgnoreCase("141191")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/RecommendNominationFormForHavildar2SIPolice", "nominationForm", nfm);
                }
            } else if (nfm.getSltNominationForPost().equalsIgnoreCase("140871")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/RecommendNominationFormForSIArmed2InspectorArmed", "nominationForm", nfm);
                }
            } else if (nfm.getSltNominationForPost().equalsIgnoreCase("140161")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/RecommendNominationFormForgroupD2juniorClerk", "nominationForm", nfm);
                }
            } else if (nfm.getSltNominationForPost().equalsIgnoreCase("140255")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/RecommendNominationFormFojuniorClerk2SeniorClerk", "nominationForm", nfm);
                }
            } else if (nfm.getSltNominationForPost().equalsIgnoreCase("140858")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/RecommendNominationFormForHavildarConstable2ASI", "nominationForm", nfm);
                }
            } else if (nfm.getSltNominationForPost().equalsIgnoreCase("140126")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/RecommendNominationFormForHavildar2MajorHavildar", "nominationForm", nfm);
                }
            } else if (nfm.getSltNominationForPost().equalsIgnoreCase("140022")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/RecommendNominationFormForInspectorArmed2AssCommadant", "nominationForm", nfm);
                }
            } else if (nfm.getSltNominationForPost().equalsIgnoreCase("140090")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/RecommendNominationFormForInspector2DSP", "nominationForm", nfm);
                }
            } else if (nfm.getSltNominationForPost().equalsIgnoreCase("140251")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/RecommendNominationFormForAsoToSo", "nominationForm", nfm);
                }
            } else {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/RecommendNominationForm", "nominationForm", nfm);
                } else {
                    mav = new ModelAndView("/policemodule/RecommendNominationForm", "nominationForm", form);
                }
            }

            mav.addObject("districtList", districtDAO.getDistrictList("21"));
            mav.addObject("categoryList", categoryDAO.getCategoryList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "EmployeeNominationViewController")
    public ModelAndView EmployeeNominationViewController(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") NominationForm form) {

        ModelAndView mav = new ModelAndView();
        try {
            NominationForm nfm = nominationRollDAO.getEmployeeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()));
            if (nfm.getSltNominationForPost().equalsIgnoreCase("140293")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewForASI2SIorSI2INSDDO", "nominationForm", nfm);
                } else {
                    mav = new ModelAndView("/policemodule/NominationViewForASI2SIorSI2INSDDO", "nominationForm", form);
                }
            } else if (nfm.getSltNominationForPost().equalsIgnoreCase("140599")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewForSI2Inspector", "nominationForm", nfm);
                } else {
                    mav = new ModelAndView("/policemodule/NominationViewForSI2Inspector", "nominationForm", form);
                }
            } else if (nfm.getSltNominationForPost().equalsIgnoreCase("141191")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewForHavildar2SIPolice", "nominationForm", nfm);
                }
            } else if (nfm.getSltNominationForPost().equalsIgnoreCase("140871")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewForSIArmedr2InspectorPoliceArmed", "nominationForm", nfm);
                }
            } else if (nfm.getSltNominationForPost().equalsIgnoreCase("140161")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewForGroupDtoJuniorClerk", "nominationForm", nfm);
                }
            } else if (nfm.getSltNominationForPost().equalsIgnoreCase("140255")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewForJuniorClerktoSeniorClerk", "nominationForm", nfm);
                }
            } else if (nfm.getSltNominationForPost().equalsIgnoreCase("140858")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewForHavildarconstable2ASI", "nominationForm", nfm);
                }
            } else if (nfm.getSltNominationForPost().equalsIgnoreCase("140126")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewForHavildar2MajorHavildar", "nominationForm", nfm);
                }
            } else if (nfm.getSltNominationForPost().equalsIgnoreCase("140022")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewForInspectorArmed2AssistantCommandant", "nominationForm", nfm);
                }
            } else if (nfm.getSltNominationForPost().equalsIgnoreCase("140090")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewForInspectortoDSP", "nominationForm", nfm);
                }
            } else if (nfm.getSltNominationForPost().equalsIgnoreCase("140090")) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewForAsoToSo", "nominationForm", nfm);
                }
            } else {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/EmployeeNominationViewForDDO", "nominationForm", nfm);
                } else {
                    mav = new ModelAndView("/policemodule/EmployeeNominationViewForDDO", "nominationForm", form);
                }
            }

            mav.addObject("districtList", districtDAO.getDistrictList());
            mav.addObject("categoryList", categoryDAO.getCategoryList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "EmployeeNominationViewControllerForRange")
    public ModelAndView EmployeeNominationViewControllerForRange(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") NominationForm form) {

        ModelAndView mav = new ModelAndView();
        try {
            NominationForm nfm = nominationRollDAO.getEmployeeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()));
            if (nfm.getSltNominationForPost() != null && !nfm.getSltNominationForPost().equals("") && (nfm.getSltNominationForPost().equalsIgnoreCase("140293"))) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewForASI2SIorSI2INSRange", "nominationForm", nfm);
                } else {
                    mav = new ModelAndView("/policemodule/NominationViewForASI2SIorSI2INSRange", "nominationForm", form);
                }
            }
            if (nfm.getSltNominationForPost() != null && !nfm.getSltNominationForPost().equals("") && (nfm.getSltNominationForPost().equalsIgnoreCase("140599"))) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewForSI2InspectorRange", "nominationForm", nfm);
                } else {
                    mav = new ModelAndView("/policemodule/NominationViewForSI2InspectorRange", "nominationForm", form);
                }
            } else if (nfm.getSltNominationForPost() != null && !nfm.getSltNominationForPost().equals("") && (nfm.getSltNominationForPost().equalsIgnoreCase("140255"))) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewForJuniorClerktoSeniorClerk", "nominationForm", nfm);
                } else {
                    mav = new ModelAndView("/policemodule/NominationViewForJuniorClerktoSeniorClerk", "nominationForm", form);
                }
            } else if (nfm.getSltNominationForPost() != null && !nfm.getSltNominationForPost().equals("") && (nfm.getSltNominationForPost().equalsIgnoreCase("140161"))) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewRecommendSideForGroupDtoJuniorClerk", "nominationForm", nfm);
                } else {
                    mav = new ModelAndView("/policemodule/NominationViewRecommendSideForGroupDtoJuniorClerk", "nominationForm", form);
                }
            } else if (nfm.getSltNominationForPost() != null && !nfm.getSltNominationForPost().equals("") && (nfm.getSltNominationForPost().equalsIgnoreCase("141191"))) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewRecommendedSideForASIArmed2SI", "nominationForm", nfm);
                } else {
                    mav = new ModelAndView("/policemodule/NominationViewRecommendedSideForASIArmed2SI", "nominationForm", form);
                }
            } else if (nfm.getSltNominationForPost() != null && !nfm.getSltNominationForPost().equals("") && (nfm.getSltNominationForPost().equalsIgnoreCase("140090"))) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewRecommendedSideForInspector2DSP", "nominationForm", nfm);
                } else {
                    mav = new ModelAndView("/policemodule/NominationViewRecommendedSideForInspector2DSP", "nominationForm", form);
                }
            } else if (nfm.getSltNominationForPost() != null && !nfm.getSltNominationForPost().equals("") && (nfm.getSltNominationForPost().equalsIgnoreCase("140858"))) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewRecommendedSideForHavildarconstable2ASI", "nominationForm", nfm);
                } else {
                    mav = new ModelAndView("/policemodule/NominationViewRecommendedSideForHavildarconstable2ASI", "nominationForm", form);
                }
            } else if (nfm.getSltNominationForPost() != null && !nfm.getSltNominationForPost().equals("") && (nfm.getSltNominationForPost().equalsIgnoreCase("140126"))) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewRecommendedSideForHavildar2MajohHAV", "nominationForm", nfm);
                } else {
                    mav = new ModelAndView("/policemodule/NominationViewRecommendedSideForHavildar2MajohHAV", "nominationForm", form);
                }
            } else if (nfm.getSltNominationForPost() != null && !nfm.getSltNominationForPost().equals("") && (nfm.getSltNominationForPost().equalsIgnoreCase("140022"))) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewRecommendedSideForInspectorArmed2AsstCommandant", "nominationForm", nfm);
                } else {
                    mav = new ModelAndView("/policemodule/NominationViewRecommendedSideForInspectorArmed2AsstCommandant", "nominationForm", form);
                }
            } else if (nfm.getSltNominationForPost() != null && !nfm.getSltNominationForPost().equals("") && (nfm.getSltNominationForPost().equalsIgnoreCase("140022"))) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewRecommendedSideForAsoToSo", "nominationForm", nfm);
                } else {
                    mav = new ModelAndView("/policemodule/NominationViewRecommendedSideForAsoToSo", "nominationForm", form);
                }
            } else if (nfm.getSltNominationForPost() != null && !nfm.getSltNominationForPost().equals("") && (nfm.getSltNominationForPost().equalsIgnoreCase("140871"))) {
                if (nfm != null) {
                    mav = new ModelAndView("/policemodule/NominationViewRecommendedSideForSIArmed2InspectorArmed", "nominationForm", nfm);
                } else {
                    mav = new ModelAndView("/policemodule/NominationViewRecommendedSideForSIArmed2InspectorArmed", "nominationForm", form);
                }
            }

            mav.addObject("districtList", districtDAO.getDistrictList());
            mav.addObject("categoryList", categoryDAO.getCategoryList());
            mav.addObject("username", lub.getLoginuserid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "savenominationFormController")
    public ModelAndView savenominationFormController(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") NominationForm form, BindingResult result) {

        ModelAndView mav = new ModelAndView();
        String filepath = servletContext.getInitParameter("policeDocPath");
        try {
            if (form.getNominationFormId() != null && !form.getNominationFormId().equals("")) {
                nominationRollDAO.updateEmployeeNominationData(form, filepath);
            } else {
                nominationRollDAO.saveEmployeeNominationData(form, filepath);
            }
            mav = new ModelAndView("redirect:/editNominationroll.htm?nominationMasterId=" + form.getNominationMasterId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveRecommedNominationFormController")
    public ModelAndView saveRecommedNominationFormController(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") NominationForm form) {

        ModelAndView mav = new ModelAndView();

        try {
            if (form.getNominationFormId() != null && !form.getNominationFormId().equals("")) {
                nominationRollDAO.updateEmployeeNominationRecomendationData(form);
            }
            mav = new ModelAndView("redirect:/viewNominationrollForRangeOffice.htm?nominationMasterId=" + form.getNominationMasterId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "viewnominationFormControllerForRangeOffice", params = {"action=Submit"})
    public ModelAndView loadNominationListForRangeOfficeWithList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        ModelAndView mav = new ModelAndView();
        String filepath = servletContext.getInitParameter("policeDocPath");

        try {

            nominationRollDAO.submitNominationForm2DGOffice(lub.getLoginoffcode(), emp.getSltRangeOffice(), emp.getNominationId());
            mav = new ModelAndView("redirect:/nominationrollListForRangeOffice.htm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "submitNominationForFieldOffice", params = {"action=Submit"})
    public ModelAndView submitNominationForm(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp, @RequestParam Map<String, String> requestParams) {
        ModelAndView mav = new ModelAndView();
        try {

            nominationRollDAO.submitNominationForm2RangeOffice(lub.getLoginoffcode(), emp.getSltRangeOffice(), requestParams.get("nominationId"));
            mav = new ModelAndView("redirect:/nominationrollList.htm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "NominatedList")
    public String NominatedList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        try {
            List nominatedlist = nominationRollDAO.getNominatedList(lub.getLoginoffcode());

            model.addAttribute("nominatedlist", nominatedlist);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/policemodule/NominatedList";
    }

    @RequestMapping(value = "cmMedalDistWiseNominationList.htm")
    public String cmMedalDistWiseNomination(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        try {
            List distlist = districtDAO.getPoliceDistrictList();

            model.addAttribute("districtlist", distlist);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/policemodule/award/DistrictWiseNominatedList";
    }

    @RequestMapping(value = "NominatedListDetailView")
    public String NominatedListDetailView(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        try {
            List nominateddetailviewlist = nominationRollDAO.getNominatedListDetailView(emp.getNominationMasterId());

            model.addAttribute("nominateddetailviewlist", nominateddetailviewlist);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/policemodule/NominatedListDetailView";
    }

    @RequestMapping(value = "NominatedEmployeeDetailView")
    public String NominatedEmployeeDetailView(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") ASINominationForm nfm) {

        try {
            ASINominationForm nform = nominationRollDAO.getNominatedEmployeeDetailData(nfm.getNominationMasterId(), nfm.getNominationDetailId());

            model.addAttribute("nominationForm", nform);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "policemodule/NominatedEmployeeDetailView";
    }

    @RequestMapping(value = "viewNominationrollForDGOffice")
    public ModelAndView viewNominationrollForDGOffice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = new ModelAndView();

        List nominationList = new ArrayList();

        try {

            //emp = nominationRollDAO.getNominationCreatedData(lub.getLoginoffcode(), Integer.parseInt(requestParams.get("nominationMasterId")));
            nominationList = nominationRollDAO.getNominatedEmployeeList(Integer.parseInt(requestParams.get("nominationMasterId")));

            mav = new ModelAndView("/policemodule/ViewProposedNominatioListByDGOffice", "EmpDetNom", emp);
            mav.addObject("empList", nominationList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "remarksFormController")
    public ModelAndView remarksFormController(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") NominationForm form) {

        ModelAndView mav = new ModelAndView();
        try {
            NominationForm nfm = nominationRollDAO.getEmployeeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()));
            if (nfm != null) {
                mav = new ModelAndView("/policemodule/RemarksNominationForm", "nominationForm", nfm);
            }

            mav.addObject("districtList", districtDAO.getDistrictList("21"));
            mav.addObject("categoryList", categoryDAO.getCategoryList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveRecommedNominationFormControllerDG")
    public ModelAndView saveRecommedNominationFormControllerDG(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") NominationForm form) {

        ModelAndView mav = new ModelAndView();

        try {
            if (form.getNominationFormId() != null && !form.getNominationFormId().equals("")) {
                nominationRollDAO.updateEmployeeNominationRecomendationDataDG(form);
            }
            mav = new ModelAndView("redirect:/viewNominationrollForDGOffice.htm?nominationMasterId=" + form.getNominationMasterId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "RecommendViewPageForDGOffice")
    public ModelAndView getRecommendViewPageForDGOffice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {
        ModelAndView mav = new ModelAndView();
        List currentRankList = new ArrayList();
        try {

            currentRankList = nominationRollDAO.getCurrentRankListForDgBoard();
            List newRanklist = nominationRollDAO.getAllNominatedRankList(emp.getSltpostName());
            List fiscyear = fiscalDAO.getAllFiscalYearListForPoliceNomination();
            mav = new ModelAndView("/policemodule/RecommendPageforDGPolice", "EmpDetNom", emp);
            mav.addObject("currentRankList", currentRankList);
            mav.addObject("newRanklist", newRanklist);
            mav.addObject("fiscyear", fiscyear);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "RecommendViewPageForDGOfficeForGroupD")
    public ModelAndView RecommendViewPageForDGOfficeForGroupD(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {
        ModelAndView mav = new ModelAndView();
        List currentRankList = new ArrayList();
        try {

            currentRankList = nominationRollDAO.getCurrentRankList();
            List newRanklist = nominationRollDAO.getNominatedRankList(emp.getSltpostName());
            List fiscyear = fiscalDAO.getAllFiscalYearListForPoliceNomination();
            mav = new ModelAndView("/policemodule/RecommendPageforDGPoliceForGroupD", "EmpDetNom", emp);
            mav.addObject("currentRankList", currentRankList);
            mav.addObject("newRanklist", newRanklist);
            mav.addObject("fiscyear", fiscyear);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "RedirectActionViewPageForDGOffice")
    public ModelAndView getRedirectActionViewPageForDGOffice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {
        ModelAndView mav = new ModelAndView();
        String path = "";
        try {
            if (emp.getSltActionName().equalsIgnoreCase("1")) {
                path = "redirect:/getNominationCompletedList.htm?sltpostName=" + emp.getSltpostName() + "&sltNominationForPost=" + emp.getSltNominationForPost() + "&fiscalyear=" + emp.getFiscalyear();
            } else if (emp.getSltActionName().equalsIgnoreCase("2")) {
                path = "redirect:/getRecomendationCompletedList.htm?sltpostName=" + emp.getSltpostName() + "&sltNominationForPost=" + emp.getSltNominationForPost() + "&fiscalyear=" + emp.getFiscalyear();
            } else if (emp.getSltActionName().equalsIgnoreCase("3")) {
                path = "redirect:/getRecomendationNotCompletedByRange.htm?sltpostName=" + emp.getSltpostName() + "&sltNominationForPost=" + emp.getSltNominationForPost() + "&fiscalyear=" + emp.getFiscalyear();
            } else if (emp.getSltActionName().equalsIgnoreCase("4")) {
                path = "redirect:/downloadExcelFormatNominationCheckList.htm?sltpostName=" + emp.getSltpostName() + "&sltNominationForPost=" + emp.getSltNominationForPost() + "&cadreName=" + emp.getCadreName() + "&fiscalyear=" + emp.getFiscalyear();
            }
            mav = new ModelAndView(path);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "checkStatusForPoliceNomination")
    public void checkStatusForPoliceMedal(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("currentPost") String currentPost, @RequestParam("nominationPost") String nominationPost) {
        response.setContentType("text/html");
        PrintWriter out = null;
        try {
            String msg = nominationRollDAO.CheckStatusOfPoliceNomination(currentPost, nominationPost);
            out = response.getWriter();
            out.write(msg + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "activateStatusForPoliceNomination")
    public void activateStatusForPoliceNomination(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("currentPost") String currentPost, @RequestParam("nominationPost") String nominationPost) throws IOException, JSONException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        try {
            String msg = nominationRollDAO.activeStatusOfPoliceNomination(currentPost, nominationPost);
            out.write(msg + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "deActivateStatusForPoliceNomination")
    public void deActivateStatusForPoliceNomination(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("currentPost") String currentPost, @RequestParam("nominationPost") String nominationPost) throws IOException, JSONException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        try {
            String msg = nominationRollDAO.deActiveStatusOfPoliceNomination(currentPost, nominationPost);
            out.write(msg + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "getNominationCompletedList")
    public ModelAndView getNominationCompletedList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {
        ModelAndView mav = new ModelAndView();

        try {
            HashMap li = nominationRollDAO.getNominationCompletedList(emp.getSltpostName(), emp.getSltNominationForPost(), emp.getFiscalyear());
            mav = new ModelAndView("/policemodule/NominationCompletedReport", "EmpDetNom", emp);
            mav.addObject("listData", li);
            mav.addObject("nominationPost", nominationRollDAO.getPromoteRankname(emp.getSltNominationForPost()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "getRecomendationCompletedList")
    public ModelAndView getRecomendationCompletedList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {
        ModelAndView mav = new ModelAndView();

        try {

            List li = nominationRollDAO.getRecommendationCompletedList(emp.getSltpostName(), emp.getSltNominationForPost(), emp.getFiscalyear());
            mav = new ModelAndView("/policemodule/RecommendationCompletedList", "EmpDetNom", emp);
            mav.addObject("listData", li);
            mav.addObject("nominationPost", nominationRollDAO.getPromoteRankname(emp.getSltNominationForPost()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "getRecomendationNotCompletedByRange")
    public ModelAndView getRecomendationNotCompletedByRange(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {
        ModelAndView mav = new ModelAndView();

        try {

            List li = nominationRollDAO.getRecomendationPendingList(emp.getSltpostName(), emp.getSltNominationForPost(), emp.getFiscalyear());
            mav = new ModelAndView("/policemodule/RecommendationPendingReport", "EmpDetNom", emp);
            mav.addObject("listData", li);
            mav.addObject("nominationPost", nominationRollDAO.getPromoteRankname(emp.getSltNominationForPost()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "downloadExcelFormatNominationCheckList")
    public void downloadExcelFormatNominationCheckList(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        response.setContentType("application/vnd.ms-excel");

        OutputStream out = null;

        try {

            out = response.getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);

            response.setHeader("Content-Disposition", "attachment; filename=ServiceParticular.xls");
            if (emp.getSltNominationForPost() != null && !emp.getSltNominationForPost().equals("") && (emp.getSltNominationForPost().equalsIgnoreCase("140599"))) {
                nominationRollRecommendCheckListDAO.downloadNominationCheckListForSI2Inspector(workbook, emp.getSltpostName(), emp.getSltNominationForPost(), emp.getCadreName(), emp.getFiscalyear());
            } else if (emp.getSltNominationForPost() != null && !emp.getSltNominationForPost().equals("") && (emp.getSltNominationForPost().equalsIgnoreCase("140126"))) {
                nominationRollRecommendCheckListDAO.downloadNominationCheckListForHavildar2HavildarMajor(workbook, emp.getSltpostName(), emp.getSltNominationForPost(), emp.getCadreName(), emp.getFiscalyear());
            } else if (emp.getSltNominationForPost() != null && !emp.getSltNominationForPost().equals("") && (emp.getSltNominationForPost().equalsIgnoreCase("140090"))) {
                nominationRollRecommendCheckListDAO.downloadNominationCheckListForInspector2DSP(workbook, emp.getSltpostName(), emp.getSltNominationForPost(), emp.getCadreName(), emp.getFiscalyear());
            } else if (emp.getSltNominationForPost() != null && !emp.getSltNominationForPost().equals("") && (emp.getSltNominationForPost().equalsIgnoreCase("140293"))) {
                nominationRollRecommendCheckListDAO.downloadNominationCheckListForASI2SI(workbook, emp.getSltpostName(), emp.getSltNominationForPost(), emp.getCadreName(), emp.getFiscalyear());
            } else if (emp.getSltNominationForPost() != null && !emp.getSltNominationForPost().equals("") && (emp.getSltNominationForPost().equalsIgnoreCase("140022"))) {
                nominationRollRecommendCheckListDAO.downloadNominationCheckListForInspectorArmed2AssistantCommandant(workbook, emp.getSltpostName(), emp.getSltNominationForPost(), emp.getCadreName(), emp.getFiscalyear());
            } else if ((emp.getSltNominationForPost() != null && !emp.getSltNominationForPost().equals("") && emp.getCadreName() != null || !emp.getCadreName().equals("")) && (emp.getSltNominationForPost().equalsIgnoreCase("140871") && emp.getCadreName().equals("1453"))) {
                nominationRollRecommendCheckListDAO.downloadNominationCheckListForSIArmed2InspectorArmedDistrictCadre(workbook, emp.getSltpostName(), emp.getSltNominationForPost(), emp.getCadreName(), emp.getFiscalyear());
            } else if ((emp.getSltNominationForPost() != null && !emp.getSltNominationForPost().equals("") && emp.getCadreName() != null || !emp.getCadreName().equals("")) && (emp.getSltNominationForPost().equalsIgnoreCase("140871") && emp.getCadreName().equals("1493"))) {
                nominationRollRecommendCheckListDAO.downloadNominationCheckListForSIArmed2InspectorArmedBatalionCadreOdiaCoy(workbook, emp.getSltpostName(), emp.getSltNominationForPost(), emp.getCadreName(), emp.getFiscalyear());
            } else if ((emp.getSltNominationForPost() != null && !emp.getSltNominationForPost().equals("") && emp.getCadreName() != null || !emp.getCadreName().equals("")) && (emp.getSltNominationForPost().equalsIgnoreCase("140871") && emp.getCadreName().equals("1494"))) {
                nominationRollRecommendCheckListDAO.downloadNominationCheckListForSIArmed2InspectorArmedBatalionCadreGurkhaCoy(workbook, emp.getSltpostName(), emp.getSltNominationForPost(), emp.getCadreName(), emp.getFiscalyear());
            } else if ((emp.getSltNominationForPost() != null && !emp.getSltNominationForPost().equals("") && emp.getCadreName() != null || !emp.getCadreName().equals("")) && (emp.getSltNominationForPost().equalsIgnoreCase("140871") && emp.getCadreName().equals("1495"))) {
                nominationRollRecommendCheckListDAO.downloadNominationCheckListForSIArmed2InspectorArmedGeneralCadre(workbook, emp.getSltpostName(), emp.getSltNominationForPost(), emp.getCadreName(), emp.getFiscalyear());
            } else if ((emp.getSltNominationForPost() != null && !emp.getSltNominationForPost().equals("") && emp.getCadreName() != null || !emp.getCadreName().equals("")) && (emp.getSltNominationForPost().equalsIgnoreCase("141191") && emp.getCadreName().equals("1453"))) {
                nominationRollRecommendCheckListDAO.downloadNominationCheckListForASIArmed2SubInspectorArmedDistrictCadre(workbook, emp.getSltpostName(), emp.getSltNominationForPost(), emp.getCadreName(), emp.getFiscalyear());
            } else if ((emp.getSltNominationForPost() != null && !emp.getSltNominationForPost().equals("") && emp.getCadreName() != null || !emp.getCadreName().equals("")) && (emp.getSltNominationForPost().equalsIgnoreCase("141191") && emp.getCadreName().equals("1494"))) {
                nominationRollRecommendCheckListDAO.downloadNominationCheckListForASIArmed2SubInspectorArmedBatalionCadreGurkhaCoy(workbook, emp.getSltpostName(), emp.getSltNominationForPost(), emp.getCadreName(), emp.getFiscalyear());
            } else if ((emp.getSltNominationForPost() != null && !emp.getSltNominationForPost().equals("") && emp.getCadreName() != null || !emp.getCadreName().equals("")) && (emp.getSltNominationForPost().equalsIgnoreCase("141191") && emp.getCadreName().equals("1493"))) {
                nominationRollRecommendCheckListDAO.downloadNominationCheckListForASIArmed2SubInspectorArmedBatalionCadreOdiaCoy(workbook, emp.getSltpostName(), emp.getSltNominationForPost(), emp.getCadreName(), emp.getFiscalyear());
            } else if ((emp.getSltNominationForPost() != null && !emp.getSltNominationForPost().equals("") && emp.getCadreName() != null || !emp.getCadreName().equals("")) && (emp.getSltNominationForPost().equalsIgnoreCase("140858"))) {
                nominationRollRecommendCheckListDAO.downloadNominationCheckListForConstableHavtoASI(workbook, emp.getSltpostName(), emp.getSltNominationForPost(), emp.getCadreName(), emp.getFiscalyear());
            } else if (emp.getSltNominationForPost() != null && !emp.getSltNominationForPost().equals("") && (emp.getSltNominationForPost().equalsIgnoreCase("140255"))) {
                nominationRollRecommendCheckListDAO.downloadNominationCheckListForJuniorClerk2SeniorClerk(workbook, emp.getSltpostName(), emp.getSltNominationForPost(), emp.getCadreName(), emp.getFiscalyear());
            } else if (emp.getSltNominationForPost() != null && !emp.getSltNominationForPost().equals("") && (emp.getSltNominationForPost().equalsIgnoreCase("140161"))) {
                nominationRollRecommendCheckListDAO.downloadNominationCheckListForGroupD2JuniorClerk(workbook, emp.getSltpostName(), emp.getSltNominationForPost(), emp.getCadreName(), emp.getFiscalyear());
            } else {
                nominationRollRecommendCheckListDAO.downloadNominationCheckList(workbook, emp.getSltpostName(), emp.getSltNominationForPost(), emp.getCadreName(), emp.getFiscalyear());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "PoliceNominationDuplicateDataPage")
    public String PoliceNominationDuplicateDataPage(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        String path = "";

        try {
            if (lub.getLoginusertype() != null && (lub.getLoginusertype().equals("H") || lub.getLoginusertype().equals("D") || lub.getLoginusertype().equals("A") || lub.getLoginusertype().equals("S"))) {
                if (emp.getGpfno() != null && !emp.getGpfno().equals("")) {
                    nominationRollDAO.getEnteredGPFNoDuplicateData(emp.getGpfno(), emp);
                    model.addAttribute("gpfno", emp.getGpfno());
                    model.addAttribute("empname", emp.getEmpName());
                }
                path = "/policemodule/PoliceNominationDuplicateData";
            } else {
                path = "under_const";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    @RequestMapping(value = "PoliceNominationDuplicateDataDelete")
    public String PoliceNominationDuplicateDataDelete(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        String path = "";

        try {
            if (lub.getLoginusertype() != null && (lub.getLoginusertype().equals("H") || lub.getLoginusertype().equals("D") || lub.getLoginusertype().equals("A") || lub.getLoginusertype().equals("S"))) {
                if (emp.getGpfno() != null && !emp.getGpfno().equals("")) {
                    String isDeleted = nominationRollDAO.deletePoliceNominationDuplicateData(emp.getGpfno());
                    model.addAttribute("isDeleted", isDeleted);
                }
                path = "/policemodule/PoliceNominationDuplicateData";
            } else {
                path = "under_const";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    @RequestMapping(value = "downloadDistrictWiseNominationListAnnexureI")
    public ModelAndView downloadDistrictWiseNominationListAnnexureI(HttpServletResponse response, @RequestParam("distCode") String distCode) {
        ModelAndView mv = new ModelAndView("districtWiseMedalNomination");
        String[] headers = new String[]{"Sl No", "Name and Designation", "2014-15", "2015-16", "2016-17", "2017-18", "2018-19", "Total", "Years of service as on 2020", "Punishments (If Any) during last five years", "Details of Pending Court cases/Vig Cases/ Dept. Proceedings", "Remarks"};
        List data = nominationRollDAO.getDistrictWiseRecomendationListAnnextureI(distCode);
        mv.addObject("headers", headers);
        mv.addObject("data", data);
        return mv;
    }

    @RequestMapping(value = "downloadDistrictWiseNominationListAnnexureIIA")
    public ModelAndView downloadDistrictWiseNominationListAnnexureIIA(HttpServletResponse response, @RequestParam("distCode") String distCode) {
        ModelAndView mv = new ModelAndView("districtWiseMedalNomination");
        String[] headers = new String[]{"Sl No", "Name and Designation", "PS Name", "Case No. with sections of law", "Date of registration", "CS or FR", "Date of submission of final form", "SR or Non SR", "Brief of the case", "Innovative methods/ specific professional investigative skill used", "Use of scientific aids and forensic tools in investigation",
            " Arraigning of scientific evidence", "Promptness in filing charge sheet", "Attachment and confiscation of crime proceeds(if applicable)", "Skills displayed in overcoming challenges in investigation", "Conviction details"};
        List data = nominationRollDAO.getDistrictWiseRecomendationListAnnextureIIA(distCode);
        mv.addObject("headers", headers);
        mv.addObject("data", data);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "deletePoliceNominationAttachment")
    public void deletePoliceMedalAttachment(HttpServletResponse response, @RequestParam("attchId") int attchId, @RequestParam("doctype") String doctype) {

        response.setContentType("application/json");

        FileAttribute fa = null;

        JSONObject obj = null;
        try {
            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = nominationRollDAO.getDocument(filepath, doctype, attchId);

            int deletestatus = nominationRollDAO.deleteNominationAttachment(attchId, doctype, filepath, fa);

            obj = new JSONObject();
            obj.append("deletestatus", deletestatus);
            PrintWriter out = response.getWriter();
            out.write(obj.toString());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "distWiseEmpNominationList")
    public ModelAndView getDistWiseEmpNominationList(HttpServletResponse response, @RequestParam("distCode") String distCode, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {
        ModelAndView mv = new ModelAndView("/policemodule/award/DistrictWiseEmpNominationList", "AwardMedalListForm", emp);
        List data = nominationRollDAO.getDistrictWiseEmpNomination(distCode);
        mv.addObject("data", data);
        return mv;
    }

    @RequestMapping(value = "downloadAttachmentOfDPforASI2SI")
    public void downloadAttachmentOfDPofASI2SI(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationDifferentDisciplinaryProceedingList") NominationDifferentDisciplinaryProceedingList nominationDifferentDisciplinaryProceedingList, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        nominationDifferentDisciplinaryProceedingList = nominationRollDAO.getAttachedFileOfDPforASI2SI(nominationDifferentDisciplinaryProceedingList.getProceedingdetailid());
        response.setContentType(nominationDifferentDisciplinaryProceedingList.getGetContentType());
        response.setHeader("Content-Disposition", "attachment;filename=" + nominationDifferentDisciplinaryProceedingList.getOriginalFilename() + ".pdf");
        OutputStream out = response.getOutputStream();
        out.write(nominationDifferentDisciplinaryProceedingList.getFilecontent());
        out.flush();
    }

    @RequestMapping(value = "downloadAttachmentOfDPforSI2Inspector")
    public void downloadAttachmentOfDPforSI2Inspector(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nform") NominationForm nform, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        nform = nominationRollDAO.getAttachedFileOfDPforSI2Inspector(nform.getProceedingDetailId());
        response.setContentType(nform.getContentType());
        response.setHeader("Content-Disposition", "attachment;filename=" + nform.getOriginalFileNamefordisciplinaryProceeding() + ".pdf");
        OutputStream out = response.getOutputStream();
        out.write(nform.getFilecontent());
        out.flush();
    }

    @ResponseBody
    @RequestMapping(value = "removedpDetailforASI2SI.htm")
    public void removedpDetailforASI2SI(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("nominationDifferentDisciplinaryProceedingList") NominationDifferentDisciplinaryProceedingList nominationDifferentDisciplinaryProceedingList) throws Exception {
        response.setContentType("application/json");
        nominationRollDAO.deletedpDetailforASI2SI(nominationDifferentDisciplinaryProceedingList);
        JSONObject obj = null;
        String deletestatus = "Y";
        obj = new JSONObject();
        obj.append("deletestatus", deletestatus);
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();
    }

    @ResponseBody
    @RequestMapping(value = "removedpDetailforSI2Inspector.htm")
    public void removedpDetailforSI2Inspector(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("nform") NominationForm nform) throws Exception {
        response.setContentType("application/json");
        nominationRollDAO.deleteAttachedFileOfDPforSI2Inspector(nform);
        JSONObject obj = null;
        String deletestatus = "Y";
        obj = new JSONObject();
        obj.append("deletestatus", deletestatus);
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();
    }

    @RequestMapping(value = "RevertNominationFromRange")
    public String RevertNominationFromRange(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nform") NominationForm nform) {

        try {
            if (nform.getNominationMasterId() != null && !nform.getNominationMasterId().equals("")) {
                nominationRollDAO.revertNominationDataByRangeOffice(nform);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/viewnominationFormControllerForRangeOffice.htm?action=Search";
    }

    @RequestMapping(value = "RevertNominationFromDGOffice")
    public String RevertNominationFromDGOffice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nform") NominationForm nform) {

        try {
            if (nform.getNominationMasterId() != null && !nform.getNominationMasterId().equals("")) {
                nominationRollDAO.revertNominationDataByDGOffice(nform);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/getRecomendationCompletedList.htm?sltpostName=" + nform.getSltpostName() + "&sltNominationForPost=" + nform.getSltNominationForPost();
    }

    @RequestMapping(value = "manageNominationStatus.htm", method = RequestMethod.GET)
    public ModelAndView manageNominationStatus(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nform") NominationForm nform) {
        ModelAndView mav = new ModelAndView();
        mav = new ModelAndView("/policemodule/manageNominationStatus");
        ArrayList allRanklist = nominationRollDAO.getAllRankListForNomination();
        mav.addObject("rank", allRanklist);
        return mav;
    }
}
