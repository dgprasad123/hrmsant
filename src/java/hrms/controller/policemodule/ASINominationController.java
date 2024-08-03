package hrms.controller.policemodule;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.dao.master.CategoryDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.policemodule.ASINominationDAO;
import hrms.dao.policemodule.NominationRollDAO;
import hrms.model.common.FileAttribute;
import hrms.model.login.LoginUserBean;
import hrms.model.policemodule.ASINominationForm;
import hrms.model.policemodule.EmployeeDetailsForRank;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean"})
public class ASINominationController implements ServletContextAware {

    @Autowired
    public ASINominationDAO asiNominationDAO;

    @Autowired
    public DistrictDAO districtDAO;

    @Autowired
    public NominationRollDAO nominationRollDAO;

    @Autowired
    public CategoryDAO categoryDAO;

    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @RequestMapping(value = "asiNominationList")
    public ModelAndView asiNominationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        ModelAndView mav = new ModelAndView();

        List asiNominationList = new ArrayList();
        try {

            asiNominationList = asiNominationDAO.getASINominationList(lub.getLoginoffcode());

            mav = new ModelAndView("/policemodule/ASINominationList", "EmpDetNom", emp);
            mav.addObject("asiNominationList", asiNominationList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "submitASINominationForFieldOffice")
    public ModelAndView submitASINominationForFieldOffice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        ModelAndView mav = new ModelAndView();

        List ranknameList = null;
        try {
            ranknameList = asiNominationDAO.getRankListForASI(lub.getLoginoffcode());

            mav = new ModelAndView("/policemodule/ASINominationDetailPage", "EmpDetNom", emp);

            mav.addObject("ranknameList", ranknameList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "createASINomination", params = {"action=Get Employee"})
    public ModelAndView createASINominationGetEmployee(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        ModelAndView mav = new ModelAndView();

        List ranknameList = null;
        List empList = new ArrayList();
        try {

            empList = asiNominationDAO.getEmployeeListRankWiseForASI(emp.getSltpostName(), lub.getLoginoffcode());

            ranknameList = asiNominationDAO.getRankListForASI(lub.getLoginoffcode());

            mav = new ModelAndView("/policemodule/ASINominationDetailPage", "EmpDetNom", emp);

            mav.addObject("ranknameList", ranknameList);
            mav.addObject("empList", empList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "createASINomination", params = {"action=Create Nomination"})
    public String createASINominationCreateNomination(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {
        String path = "";
        try {
            if (lub.getLoginusertype() != null && lub.getLoginusertype().equalsIgnoreCase("H")) {
                Calendar cal = Calendar.getInstance();
                String closingStatus = asiNominationDAO.checkClosingDateForASInomination(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE), cal.get(Calendar.HOUR_OF_DAY));
                if (closingStatus == null || closingStatus.equals("")) {
                    asiNominationDAO.createASINomination(emp.getEmpId(), emp.getSltpostName(), emp.getSltNominationForPost(), lub.getLoginoffcode(), lub.getLoginuserid());
                    path = "redirect:/asiNominationList.htm";
                } else {
                    path = "under_const";
                }

            } else {
                path = "under_const";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    @RequestMapping(value = "editASINomination")
    public ModelAndView editASINomination(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        ModelAndView mav = new ModelAndView();

        List nominationList = new ArrayList();

        List ranknameList = new ArrayList();

        try {

            emp = asiNominationDAO.getASINominationCreatedData(lub.getLoginoffcode(), Integer.parseInt(emp.getNominationMasterId()));

            nominationList = asiNominationDAO.getCreatedASINominationList(lub.getLoginoffcode(), Integer.parseInt(emp.getNominationMasterId()));

            ranknameList = asiNominationDAO.getRankListForASI(lub.getLoginoffcode());

            mav = new ModelAndView("/policemodule/ASINominationDetailPage", "EmpDetNom", emp);
            mav.addObject("empList", nominationList);
            mav.addObject("ranknameList", ranknameList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "asiNominationEmployeeListToAdd")
    public ModelAndView asiNominationEmployeeListToAdd(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        ModelAndView mav = new ModelAndView();

        List empList = new ArrayList();
        List ranknameList = new ArrayList();
        try {
            System.out.println("Inside Edit Add Employee");
            //empList = asiNominationDAO.getEmployeeListRankWiseExcludedAlreadyMapped(lub.getLoginoffcode(), Integer.parseInt(emp.getNominationMasterId()));

            mav = new ModelAndView("/policemodule/ASINominationEmployeeList", "EmpDetNom", emp);
            //mav.setViewName("/policemodule/ASINominationEmployeeList");
            ranknameList = asiNominationDAO.getRankListForASI(lub.getLoginoffcode());
            mav.addObject("ranknameList", ranknameList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "createASINomination", params = {"action=Add Employee"})
    public String addEmployeetoList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        String path = "";
        try {
            asiNominationDAO.addNewEmployee(emp.getEmpId(), Integer.parseInt(emp.getNominationMasterId()), emp.getSltpostName());

            path = "redirect:/editASINomination.htm?nominationMasterId=" + emp.getNominationMasterId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    @RequestMapping(value = "ASINominationApplicationForm")
    public ModelAndView ASINominationApplicationForm(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("asiNominationForm") ASINominationForm form) {

        ModelAndView mav = new ModelAndView();
        try {
            ASINominationForm nfm = asiNominationDAO.getEmployeeASINominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()));

            if (nfm == null) {
                nfm = asiNominationDAO.getEmployeeBeforeNominationData(Integer.parseInt(form.getNominationMasterId()), Integer.parseInt(form.getNominationDetailId()));
            }

            mav = new ModelAndView("/policemodule/ASIApplicationForm", "asiNominationForm", nfm);
            mav.addObject("districtList", districtDAO.getDistrictList("21"));
            mav.addObject("categoryList", categoryDAO.getCategoryList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveASINominationApplicationForm")
    public ModelAndView saveASINominationApplicationForm(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("asiNominationForm") ASINominationForm form) {

        ModelAndView mav = new ModelAndView();
        String filepath = servletContext.getInitParameter("policeDocPath");
        try {
            Calendar cal = Calendar.getInstance();
            String closingStatus = asiNominationDAO.checkClosingDateForASInomination(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE), cal.get(Calendar.HOUR_OF_DAY));
            if (closingStatus == null || closingStatus.equals("")) {
                if (form.getNominationFormId() != null && !form.getNominationFormId().equals("")) {
                    asiNominationDAO.updateEmployeeNominationData(form, filepath);
                } else {
                    asiNominationDAO.saveEmployeeASINominationData(form, filepath);
                }
                mav = new ModelAndView("redirect:/editASINomination.htm?nominationMasterId=" + form.getNominationMasterId());
            } else {
                mav = new ModelAndView("under_const");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "downloadASINominationAnnexureAExcel")
    public void downloadASINominationAnnexureAExcel(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("asiNominationForm") ASINominationForm form) {

        response.setContentType("application/vnd.ms-excel");

        OutputStream out = null;

        try {
            out = response.getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);

            asiNominationDAO.getCreatedOfficeName(form);

            response.setHeader("Content-Disposition", "attachment; filename=ASI_NOMINATION_ANNEXURE_A_" + form.getOfficeCode() + ".xls");

            asiNominationDAO.getASINominationAnnexureAData(form.getNominationMasterId(), workbook, form.getOfficeName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "downloadASINominationAnnexureBExcel")
    public void downloadASINominationAnnexureBExcel(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("asiNominationForm") ASINominationForm form) {

        response.setContentType("application/vnd.ms-excel");

        OutputStream out = null;

        try {
            out = response.getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);

            asiNominationDAO.getCreatedOfficeName(form);

            response.setHeader("Content-Disposition", "attachment; filename=ASI_NOMINATION_ANNEXURE_B_" + form.getOfficeCode() + ".xls");

            asiNominationDAO.getASINominationAnnexureBData(form.getNominationMasterId(), workbook, form.getOfficeName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "submitASINominationForFieldOffice", params = {"action=Submit"})
    public String submitASINominationForFieldOffice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp, @RequestParam Map<String, String> requestParams) {
        String path = "";
        try {
            Calendar cal = Calendar.getInstance();

            String closingStatus = asiNominationDAO.checkClosingDateForASInomination(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE), cal.get(Calendar.HOUR_OF_DAY));
            if (closingStatus == null || closingStatus.equals("")) {
                asiNominationDAO.submitASINominationForm2RangeOffice(lub.getLoginoffcode(), emp.getSltRangeOffice(), requestParams.get("nominationId"));
                path = "redirect:/asiNominationList.htm";
            } else {
                path = "under_const";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    @ResponseBody
    @RequestMapping(value = "getEmployeeListToAdd")
    public void getEmployeeListToAdd(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List emplist = asiNominationDAO.getEmployeeListRankWiseForASI(emp.getSltpostName(), lub.getLoginoffcode());

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

    @RequestMapping(value = "createASINomination", params = {"action=Delete Nomination"})
    public ModelAndView deleteNominationroll(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        ModelAndView mav = new ModelAndView();

        try {

            nominationRollDAO.deleteNomination(lub.getLoginoffcode(), Integer.parseInt(emp.getNominationMasterId()));

            mav = new ModelAndView("redirect:/asiNominationList.htm");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "deleteEmployeeFromASINominationList")
    public ModelAndView deleteEmployeeFromASINominationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        ModelAndView mav = new ModelAndView();

        try {

            nominationRollDAO.deleteEmployee(Integer.parseInt(emp.getNominationMasterId()), Integer.parseInt(emp.getNominationDetailId()));

            mav = new ModelAndView("redirect:/asiNominationList.htm");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "displayNominatedEmployeeProfilePhoto")
    public void displayprofilephoto(HttpServletResponse response, @RequestParam("nominationformId") String nominationformId) throws IOException {

        int BUFFER_LENGTH = 4096;
        String filePath = servletContext.getInitParameter("policeDocPath");

        if (nominationformId != null && !nominationformId.equals("")) {
            FileAttribute fa = nominationRollDAO.getDpcDocument(filePath, "", Integer.parseInt(nominationformId));

            File file = new File(filePath + fa.getDiskFileName());
            response.setContentType(fa.getFileType());
            if (file.exists()) {
                response.setContentLength((int) file.length());
                FileInputStream is = new FileInputStream(file);
                ServletOutputStream os = response.getOutputStream();
                byte[] bytes = new byte[BUFFER_LENGTH];
                int read = 0;
                while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
                    os.write(bytes, 0, read);
                }
                os.flush();
                is.close();
                os.close();
            }
        }
    }

    @RequestMapping(value = "ASINominatedListDetailView")
    public String ASINominatedListDetailView(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpDetNom") EmployeeDetailsForRank emp) {

        try {
            List nominateddetailviewlist = nominationRollDAO.getNominatedListDetailView(emp.getNominationMasterId());

            model.addAttribute("nominateddetailviewlist", nominateddetailviewlist);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/policemodule/ASINominatedDetailView";
    }

    @RequestMapping(value = "ASINominatedEmployeeDetailView")
    public String NominatedEmployeeDetailView(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") ASINominationForm nfm) {

        try {
            ASINominationForm nform = nominationRollDAO.getNominatedEmployeeDetailData(nfm.getNominationMasterId(), nfm.getNominationDetailId());

            model.addAttribute("nominationForm", nform);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/policemodule/ASINominatedEmployeeDetailView";
    }

    @RequestMapping(value = "viewASINominationEmployeeList")
    public String viewASINominationEmployeeList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") ASINominationForm nfm) {

        try {
            if (nfm.getNominationMasterId() != null && !nfm.getNominationMasterId().equals("")) {
                List employeelist = asiNominationDAO.getASINominationEmployeeList(Integer.parseInt(nfm.getNominationMasterId()));
                model.addAttribute("employeelist", employeelist);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/policemodule/ASINominationEmployeeListInfo";
    }

    @RequestMapping(value = "downloadASICheckListExcel")
    public String downloadASICheckListPage(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") ASINominationForm nfm) {

        try {

            if (lub.getLoginusertype().equalsIgnoreCase("H")) {
                List offList = asiNominationDAO.getAllEstablishmentName();
                model.addAttribute("offList", offList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/policemodule/ASICheckListPage";
    }

    @RequestMapping(value = "downloadASICheckListPage", params = {"action=Generate Qualified List"})
    public ModelAndView candidateListForASIExam(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") ASINominationForm nfm) {
        ModelAndView mav = new ModelAndView();
        List candidateList = asiNominationDAO.getCandidateListApplyForASIExam();
        mav.addObject("candidateList", candidateList);
        mav.setViewName("/policemodule/asiExam/AppliedCandidateListForASIExam");
        return mav;
    }

    
    @ResponseBody
    @RequestMapping(value = "generateHallTicket")
    public void generateHallTicket(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("nominationForm") ASINominationForm nfm)throws IOException, JSONException{
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String msg = asiNominationDAO.generateHallTicketForASIExam();
        JSONObject obj = new JSONObject();
        obj.append("msg", 'Y');
        out.write(obj.toString());
        
    }

    @RequestMapping(value = "generateQualifiedList")
    public String generateQualifiedList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("nominationForm") ASINominationForm nfm) {
        asiNominationDAO.updateQualifiedListinASIExam();
        return "/policemodule/ASICheckListPage";
    }

    @RequestMapping(value = "centreAllotmentForASI")
    public String centreAllotmentForASI(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("nominationForm") ASINominationForm nfm) {
        List centerList = asiNominationDAO.getAllCenterList();
        List districtList = asiNominationDAO.getAllQualifiedDistrictList();
        model.addAttribute("centerList", centerList);
        model.addAttribute("districtList", districtList);
        return "/policemodule/CentreAllotmentForASIExam";
    }

    @ResponseBody
    @RequestMapping(value = "assignCenterPrivilage.htm")
    public void assignCenterPrivilage(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("nominationForm") ASINominationForm nfm) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        String msg = asiNominationDAO.saveAssignCenter(nfm);
        JSONObject obj = new JSONObject();
        obj.append("msg", 'Y');
        out = response.getWriter();
        out.write(obj.toString());
    }

    @RequestMapping(value = "getCenterPrivilageList")
    public ModelAndView getCenterPrivilageList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") ASINominationForm nfm) {
        ModelAndView mav = new ModelAndView();
        List centerPrivList = asiNominationDAO.getCenterPriviligeList(nfm.getEntryYear());
        mav.addObject("centerPrivList", centerPrivList);
        mav.setViewName("/policemodule/asiExam/ASICenterPrivilegeList");
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "getCenterPrivilegeYearWise.htm")
    public void getCenterPrivilegeYearWise(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("nominationForm") ASINominationForm nfm) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONArray json = null;
        try {
            List centerPrivList = asiNominationDAO.getCenterPriviligeList(nfm.getEntryYear());
            json = new JSONArray(centerPrivList);
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
            out.flush();
        }
    }

    @RequestMapping(value = "removeCenterPrivilegeYearWise.htm", method = RequestMethod.GET)
    public ModelAndView removeCenterPrivilegeYearWise(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") ASINominationForm nfm) {
        ModelAndView mv = new ModelAndView();
        asiNominationDAO.deleteCenterPriviligeListOfficeWise(nfm);
        List centerPrivList = asiNominationDAO.getCenterPriviligeList(nfm.getEntryYear());
        mv.addObject("centerPrivList", centerPrivList);
        mv.setViewName("/policemodule/asiExam/ASICenterPrivilegeList");
        return mv;
    }

    @RequestMapping(value = "downloadExcelFormatASICheckList")
    public void downloadExcelFormatASICheckList(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") ASINominationForm nfm) {

        response.setContentType("application/vnd.ms-excel");

        OutputStream out = null;

        try {

            out = response.getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);

            response.setHeader("Content-Disposition", "attachment; filename=ASI_CHECK_LIST.xls");

            asiNominationDAO.getASICheckListData(workbook, nfm.getSltEstablishment(), "", nfm.getDownloadType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "downloadExcelFormatASICheckListWithPhoto")
    public void downloadExcelFormatASICheckListWithPhoto(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") ASINominationForm nfm) {

        response.setContentType("application/vnd.ms-excel");

        OutputStream out = null;
        String filepath = servletContext.getInitParameter("policeDocPath");
        try {

            out = response.getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);

            response.setHeader("Content-Disposition", "attachment; filename=ASI_CHECK_LIST.xls");
            asiNominationDAO.getASICheckListData(workbook, nfm.getSltEstablishment(), filepath, "Y");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "downloadASIAdmitCardPage")
    public String downloadASIAdmitCardPage(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") ASINominationForm nfm) {

        try {

            if (lub.getLoginusertype().equalsIgnoreCase("H")) {
                List offList = asiNominationDAO.getAllEstablishmentName();
                List centerList = asiNominationDAO.getAllCenterList();
                model.addAttribute("offList", offList);
                model.addAttribute("centerList", centerList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/policemodule/ASIAdmitCardPage";
    }

    @RequestMapping(value = "backToCenterAllotmentPage")
    public ModelAndView backToCenterAllotmentPage(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") ASINominationForm nfm) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect: downloadASICheckListExcel.htm");
        return mav;
    }

    @RequestMapping(value = "downloadASIAdmitCardPageInDistrictLogin")
    public String downloadASIAdmitCardPageInDistrictLogin(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") ASINominationForm nfm) {

        try {

            if (lub.getLoginusertype().equalsIgnoreCase("H")) {
                List offList = asiNominationDAO.getAllEstablishmentName();
                List centerList = asiNominationDAO.getAllCenterList();
                model.addAttribute("offList", offList);
                model.addAttribute("centerList", centerList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/policemodule/ASIAdmitCardPageForDistrict";
    }

    @RequestMapping(value = "downloadDistrictWiseHallTicketNumber")
    public String downloadDistrictWiseHallTicketNumber(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") ASINominationForm nfm) {
        String filepath = servletContext.getInitParameter("policeDocPath");
        try {

            if (lub.getLoginusertype().equalsIgnoreCase("H")) {
                List li = asiNominationDAO.downloadASIAdmitCard(nfm.getSltEstablishment(), filepath);
                model.addAttribute("candidateList", li);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/policemodule/ASIDistrictWiseHallTicket";
    }

    @RequestMapping(value = "downloadASIAdmitCard")
    public String downloadASIAdmitCard(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") ASINominationForm nfm) {
        String filepath = servletContext.getInitParameter("policeDocPath");
        try {

            if (lub.getLoginusertype().equalsIgnoreCase("H")) {
                List li = asiNominationDAO.downloadASIAdmitCard(nfm.getSltEstablishment(), filepath);
                model.addAttribute("candidateList", li);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/policemodule/ASIAdmitCardDownload";
    }

    @RequestMapping(value = "downloadCenterWiseCandidateList")
    public String downloadCenterWiseCandidateList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") ASINominationForm nfm) {
        String filepath = servletContext.getInitParameter("policeDocPath");
        try {
            System.out.println("== center code==" + nfm.getSltCenter());
            if (lub.getLoginusertype().equalsIgnoreCase("H")) {
                List li = asiNominationDAO.getCenterWiseCandidateList(nfm.getSltCenter(), filepath);
                String center = asiNominationDAO.getCenterName(nfm.getSltCenter());
                model.addAttribute("candidateList", li);
                model.addAttribute("centerName", center);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/policemodule/ASICenterwiseCandidateList";
    }

    @RequestMapping(value = "downloadASIAdmitCardPDF")
    public void downloadASIAdmitCard(HttpServletResponse response, Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") ASINominationForm nfm) {
        String filepath = servletContext.getInitParameter("policeDocPath");
        try {
            response.setContentType("application/pdf");
            File download = new File(filepath + lub.getLoginoffcode() + ".pdf");
            FileInputStream inst = new FileInputStream(download);

            response.setContentLength((int) download.length());
// "Content-Disposition", "attachment; filename=\""+filename+"\""
            //String headerKey = "Content-Disposition";
            //String headerValue = String.format("attachment; filename=\"%s\"", download.getName());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + download.getName() + "\"");
            OutputStream out = response.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inst.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            inst.close();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "qualifiedListofASIController")
    public String qualifiedListofASIController(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") ASINominationForm nfm) {
        String filepath = servletContext.getInitParameter("policeDocPath");
        try {
            if (lub.getLoginusertype().equalsIgnoreCase("H")) {
                List li = asiNominationDAO.getQualifiedListinASIExam(lub.getLoginoffcode(), filepath);
                model.addAttribute("qualifiedList", li);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/policemodule/ASIExamQualifiedList";
    }

    @RequestMapping(value = "ASINominationProceddingDataForm")
    public String ASINominationProceddingDataForm(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("nominationForm") ASINominationForm nfm) {

        try {
            ASINominationForm nform = nominationRollDAO.getNominatedEmployeeDetailData(nfm.getNominationMasterId(), nfm.getNominationDetailId());

            model.addAttribute("nominationForm", nform);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/policemodule/ASINominatedEmployeeProceedingForm";
    }

}
