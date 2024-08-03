/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.AGPension;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.CommonFunctions;
import hrms.dao.AGPension.PensionNOCDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.model.AGPension.PensionNOCBean;
import hrms.model.AGPension.SearchApplBean;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Office;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
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

/**
 *
 * @author lenovo
 */
@Controller
@SessionAttributes("LoginUserBean")
public class PensionNOCController implements ServletContextAware {

    @Autowired
    PensionNOCDAO PensionNOC;

    @Autowired
    OfficeDAO officeDao;

    @Autowired
    DepartmentDAO departmentDao;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext sc) {
        this.context = sc;
    }

    /*This Function is call while the DDO will click on Apply For NOC Button*/
    @RequestMapping(value = "pensionerNocList")
    public ModelAndView pensionerNocList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("PensionNOCBean") PensionNOCBean pnoc) {
        ModelAndView mv = new ModelAndView("/AGPension/NOCList", "PensionNOCBean", pnoc);
        mv.addObject("pensionList", PensionNOC.NocList(lub.getLoginoffcode(),lub.getLoginempid()));
        //Office office = officeDao.getOfficeDetails(lub.getLoginoffcode());
        //  System.out.println("officecode"+lub.getLoginoffcode());      

        return mv;
    }

    @RequestMapping(value = "pensionerNocList.htm", params = {"action=Request NOC for Pension"})
    public ModelAndView addNewpensionerNocList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pensionNOCBean") PensionNOCBean pensionNOCBean) {
        ModelAndView mv = new ModelAndView("/AGPension/PensionNOC", "PensionNOCBean", pensionNOCBean);
        mv.addObject("pensionList", PensionNOC.PensionerNocList(lub.getLoginempid()));

        Office office = officeDao.getOfficeDetails(lub.getLoginoffcode());
        //  System.out.println("officecode"+lub.getLoginoffcode());

        List officelistcowise = new ArrayList();
        if (office.getLvl() != null && office.getLvl().equals("01")) {
            officelistcowise = officeDao.getTotalOfficeList(lub.getLogindeptcode());
        } else if (office.getLvl() != null && office.getLvl().equals("02")) {
            officelistcowise = officeDao.getOfficeListCOWise(lub.getLoginoffcode());
        } else if (office.getCategory() != null && office.getCategory().equals("DISTRICT COLLECTORATE")) {
            officelistcowise = officeDao.getDistrictWiseOfficeList(lub.getLogindistrictcode());
        }

        mv.addObject("office", office);
        mv.addObject("officelistcowise", officelistcowise);
        return mv;
    }

    @RequestMapping(value = "PensionOtherOfficeNocList.htm")
    public ModelAndView PensionOtherOfficeNocList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pensionNOCBean") PensionNOCBean pensionNOCBean, @RequestParam("searchOffcode") String searchOffcode) {
        ModelAndView mv = new ModelAndView("/AGPension/PensionNOC", "PensionNOCBean", pensionNOCBean);
        mv.addObject("pensionList", PensionNOC.PensionerNocList(searchOffcode));

        Office office = officeDao.getOfficeDetails(lub.getLoginoffcode());
        //  System.out.println("officecode"+lub.getLoginoffcode());

        List officelistcowise = new ArrayList();
        if (office.getLvl() != null && office.getLvl().equals("01")) {
            officelistcowise = officeDao.getTotalOfficeList(lub.getLogindeptcode());
        } else if (office.getLvl() != null && office.getLvl().equals("02")) {
            officelistcowise = officeDao.getOfficeListCOWise(lub.getLoginoffcode());
        } else if (office.getCategory() != null && office.getCategory().equals("DISTRICT COLLECTORATE")) {
            officelistcowise = officeDao.getDistrictWiseOfficeList(lub.getLogindistrictcode());
        }

        mv.addObject("office", office);
        mv.addObject("officelistcowise", officelistcowise);
        return mv;
    }

    /*This Function is call while the DDO will click onAdd New Employee Button to add New Employee*/
    @RequestMapping(value = "pensionerNocList.htm", params = {"action=Request NOC for Promotion"})
    public ModelAndView addNewPromotioNocList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pensionNOCBean") PensionNOCBean pensionNOCBean) {
        ModelAndView mv = new ModelAndView();
        List emplistforpension = PensionNOC.getEmployeeListForPension(lub.getLoginempid());
        mv.addObject("emplistforpension", emplistforpension);

        Office office = officeDao.getOfficeDetails(lub.getLoginoffcode());
        //  System.out.println("officecode"+lub.getLoginoffcode());

        List officelistcowise = new ArrayList();
        if (office.getLvl() != null && office.getLvl().equals("01")) {
            officelistcowise = officeDao.getTotalOfficeList(lub.getLogindeptcode());
        } else if (office.getLvl() != null && office.getLvl().equals("02")) {
            officelistcowise = officeDao.getOfficeListCOWise(lub.getLoginoffcode());
        } else if (office.getCategory() != null && office.getCategory().equals("DISTRICT COLLECTORATE")) {
            officelistcowise = officeDao.getDistrictWiseOfficeList(lub.getLogindistrictcode());
        }

        mv.addObject("office", office);
        mv.addObject("officelistcowise", officelistcowise);
        mv.setViewName("AGPension/EmpListForPension");
        return mv;
    }

    @RequestMapping(value = "promotionNocList.htm")
    public ModelAndView promotionNocList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pensionNOCBean") PensionNOCBean pensionNOCBean, @RequestParam("searchOffcode") String searchOffcode) {
        ModelAndView mv = new ModelAndView();
        List emplistforpension = PensionNOC.getEmployeeListForPensionfromOtherOffice(searchOffcode);
        mv.addObject("emplistforpension", emplistforpension);
        Office office = officeDao.getOfficeDetails(lub.getLoginoffcode());
        List officelistcowise = new ArrayList();
        if (office.getLvl() != null && office.getLvl().equals("01")) {
            officelistcowise = officeDao.getTotalOfficeList(lub.getLogindeptcode());
        } else if (office.getLvl() != null && office.getLvl().equals("02")) {
            officelistcowise = officeDao.getOfficeListCOWise(lub.getLoginoffcode());
        } else if (office.getCategory() != null && office.getCategory().equals("DISTRICT COLLECTORATE")) {
            officelistcowise = officeDao.getDistrictWiseOfficeList(lub.getLogindistrictcode());
        }

        mv.addObject("office", office);
        mv.addObject("officelistcowise", officelistcowise);
        mv.setViewName("AGPension/EmpListForPension");
        return mv;
    }

    /*This Function is call while the DDO will click on Add  Button after the employee List will come*/
    @ResponseBody
    @RequestMapping(value = "addEmployeeListForPension.htm")
    public void addrecommendationEmployeeList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pensionNOCBean") PensionNOCBean pensionNOCBean, HttpServletResponse response, ModelMap model) throws IOException, JSONException {
        response.setContentType("application/json");
        pensionNOCBean.setOffcode(lub.getLoginoffcode());
        pensionNOCBean.setNocfor("PROMOTION");
        PensionNOC.saveEmployeeListForPension(pensionNOCBean, lub.getLoginempid());
        JSONObject obj = new JSONObject();
        obj.append("msg", "Y");
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();
    }

    /*This Function is call while the DDO will click on Back Button Of Add new Employee Page to go back to the  List page*/
    @RequestMapping(value = "pensionerNocList.htm", params = {"action=Back"})
    public ModelAndView backNewpensionerNocList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("PensionNOCBean") PensionNOCBean pnoc) {
        ModelAndView mv = new ModelAndView("/AGPension/NOCList", "PensionNOCBean", pnoc);
        mv.addObject("pensionList", PensionNOC.NocList(lub.getLoginoffcode(),lub.getLoginempid()));
        Office office = officeDao.getOfficeDetails(lub.getLoginoffcode());
        //  System.out.println("officecode"+lub.getLoginoffcode());

        List officelistcowise = new ArrayList();
        if (office.getLvl() != null && office.getLvl().equals("01")) {
            officelistcowise = officeDao.getTotalOfficeList(lub.getLogindeptcode());
        } else if (office.getLvl() != null && office.getLvl().equals("02")) {
            officelistcowise = officeDao.getOfficeListCOWise(lub.getLoginoffcode());
        } else if (office.getCategory() != null && office.getCategory().equals("DISTRICT COLLECTORATE")) {
            officelistcowise = officeDao.getDistrictWiseOfficeList(lub.getLogindistrictcode());
        }

        mv.addObject("office", office);
        mv.addObject("officelistcowise", officelistcowise);
        return mv;
    }
    /*This Function is call while the DDO will click on request for NOC Button*/

    @RequestMapping(value = "requestForNoc")
    public ModelAndView requestForNoc(HttpServletResponse response, @RequestParam("hrmsid") String hrmsid, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("PensionNOCBean") PensionNOCBean pnoc, @ModelAttribute("pensionNOCBean") PensionNOCBean pensionNOCBean) {
        //PensionNOC.savePensionDetails(lub.getLoginoffcode(), hrmsid);
        pensionNOCBean.setOffcode(lub.getLoginoffcode());
        PensionNOC.requestForNoc(pensionNOCBean, lub.getLoginempid());
        ModelAndView mv = new ModelAndView("redirect:/pensionerNocList.htm");
        return mv;
    }
    /*This Function is call while the Crime branch / Vigilance click on Noc Application  Button*/

    /*  @RequestMapping(value = "VigilancePensionerNocList")
     public ModelAndView VigilancePensionerNocList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("PensionNOCBean") PensionNOCBean pnoc, @ModelAttribute("SearchApplBean") SearchApplBean sab) {

     ModelAndView mv = new ModelAndView("/AGPension/VigilancePensionerNocList", "PensionNOCBean", pnoc);
     String NocStatus = "N";
     mv.addObject("adminStatus", "N");
     mv.addObject("nocStatusType", "Pending");
     if (lub.getLoginusername().equals("cbadmin")) {
     mv.addObject("loginName", "Crime Branch");
     mv.addObject("pensionList", PensionNOC.CBPensionerNocList(NocStatus));
     } else if (lub.getLoginusername().equals("vigilanceadmin")) {
     mv.addObject("postList", PensionNOC.getPostList());
     mv.addObject("departmentList", departmentDao.getDepartmentList());
     mv.addObject("pensionList", PensionNOC.VigilancePensionerNocList(NocStatus));
     mv.addObject("loginName", "Vigilance");
     }
     return mv;
     }*/
    @RequestMapping(value = "VigilancePensionerNocList")
    public ModelAndView VigilancePensionerNocList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("PensionNOCBean") PensionNOCBean pnoc, @ModelAttribute("SearchApplBean") SearchApplBean sab) {
        ModelAndView mv = new ModelAndView("/AGPension/VigilancePensionerNocList", "PensionNOCBean", pnoc);
        String NocStatus = "N";

        mv.addObject("adminStatus", "N");
        mv.addObject("nocStatusType", "Pending");

        LocalDate currentDate = LocalDate.now();
        LocalDate previousMonthDate = currentDate.minusMonths(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = previousMonthDate.format(formatter);

        Date currentdate1 = java.sql.Date.valueOf(currentDate);

        sab.setDate_to(CommonFunctions.getFormattedOutputDateMySQLFormat(currentdate1));
        sab.setDate_from(CommonFunctions.getOneMonthBackDate(sab.getDate_to()));

         System.out.println(CommonFunctions.getFormattedOutputDateMySQLFormat(currentdate1)+"----"+CommonFunctions.getOneMonthBackDate(sab.getDate_to()));
        if (lub.getLoginusername().equals("cbadmin")) {
            mv.addObject("loginName", "Crime Branch");
            //mv.addObject("pensionList", PensionNOC.CBPensionerNocList(NocStatus));
            mv.addObject("pensionList", PensionNOC.CBPensionerNocListByDate(NocStatus, sab.getDate_from(), sab.getDate_to()));
        } else if (lub.getLoginusername().equals("vigilanceadmin")) {
            mv.addObject("postList", PensionNOC.getPostList());
            mv.addObject("departmentList", departmentDao.getDepartmentList());
            //mv.addObject("pensionList", PensionNOC.VigilancePensionerNocList(NocStatus));
            mv.addObject("pensionList", PensionNOC.VigilancePensionerNocListByDate(NocStatus, sab.getDate_from(), sab.getDate_to()));
            mv.addObject("loginName", "Vigilance");
        }
        return mv;
    }

    //////////////////////////////////////
    @RequestMapping(value = "searchApplication")
    public ModelAndView searchApplication(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("PensionNOCBean") PensionNOCBean pnoc, @ModelAttribute("SearchApplBean") SearchApplBean sab) {

        ModelAndView mv = new ModelAndView("/AGPension/VigilancePensionerNocList", "PensionNOCBean", pnoc);
        String NocStatus = "N";
        mv.addObject("adminStatus", "N");
        mv.addObject("nocStatusType", "Pending");

        mv.addObject("postList", PensionNOC.getPostList());
        mv.addObject("departmentList", departmentDao.getDepartmentList());
        mv.addObject("pensionList", PensionNOC.SearchVigilancePensionerNocList(NocStatus, sab));
        mv.addObject("loginName", "Vigilance");
        mv.addObject("SearchApplBean", sab);

        return mv;
    }
    /////////////////////////////////////

    @RequestMapping(value = "CompletedNocList")
    public ModelAndView CompletedNocList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("PensionNOCBean") PensionNOCBean pnoc) {

        ModelAndView mv = new ModelAndView("/AGPension/VigilancePensionerNocList", "PensionNOCBean", pnoc);
        String NocStatus = "Y";
        mv.addObject("nocStatusType", "Completed");
        mv.addObject("adminStatus", "N");
        if (lub.getLoginusername().equals("cbadmin")) {
            mv.addObject("loginName", "Crime Branch");
            mv.addObject("pensionList", PensionNOC.CBPensionerNocList(NocStatus));
        } else if (lub.getLoginusername().equals("vigilanceadmin")) {
            mv.addObject("pensionList", PensionNOC.VigilancePensionerNocList(NocStatus));
            mv.addObject("loginName", "Vigilance");

        }
        return mv;
    }
    /*This Function is call while the Crime branch / Vigilance click on Upload Button*/

    @RequestMapping(value = "VigilanceNocUpload")
    public ModelAndView VigilanceNocUpload(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("PensionNOCBean") PensionNOCBean pnoc) {
        ModelAndView mv = new ModelAndView("/AGPension/VigilanceNocUpload", "PensionNOCBean", pnoc);
        if (lub.getLoginusername().equals("cbadmin")) {
            mv.addObject("loginName", "Crime Branch");
        } else if (lub.getLoginusername().equals("vigilanceadmin")) {
            mv.addObject("loginName", "Vigilance");

        }
        mv.addObject("pensionList", PensionNOC.VigilanceNocUpload(pnoc.getNocId(), pnoc.getHrmsid()));

        return mv;
    }
    /*This Function is call while click on Save button of Noc Upload page to save the document and reason*/

    @ResponseBody
    @RequestMapping(value = "saveVigilanceNOC", method = RequestMethod.POST)
    public void saveVigilanceNOC(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("hrmsid") String hrmsid, @ModelAttribute("PensionNOCBean") PensionNOCBean pnoc, @ModelAttribute("pensionNOCBean") PensionNOCBean pensionNOCBean) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = null;
        pensionNOCBean.setAuthorityType(lub.getLoginusername());
        //System.out.println("=="+pensionNOCBean.getUploadDocumentvigilance().getContentType());
        PensionNOC.savePensionDetails(pensionNOCBean);
        //JSONArray json = new JSONArray(billMonths);
        out = response.getWriter();
        out.write("success");
    }

    /*This Function is call while click on back button of Noc Upload page to go back to the List page*/
    @RequestMapping(value = "saveVigilanceNOC", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView backVigilanceNOC(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("hrmsid") String hrmsid, @ModelAttribute("PensionNOCBean") PensionNOCBean pnoc, @ModelAttribute("pensionNOCBean") PensionNOCBean pensionNOCBean) {
        ModelAndView mv = new ModelAndView("redirect:/VigilancePensionerNocList.htm");
        pensionNOCBean.setAuthorityType(lub.getLoginusername());
        mv.addObject("pensionList", PensionNOC.VigilancePensionerNocList("N"));
        return mv;
    }

    @RequestMapping(value = "CBNocUpload")
    public ModelAndView CBNocUpload(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("PensionNOCBean") PensionNOCBean pnoc) {
        ModelAndView mv = new ModelAndView("/AGPension/CBNocUpload", "PensionNOCBean", pnoc);
        mv.addObject("pensionList", PensionNOC.CBNocUpload(pnoc.getNocId(), pnoc.getHrmsid()));
        return mv;
    }
    /*This Function is call while download the vigilance File*/

    @RequestMapping(value = "downloadAttachmentOfNOC")
    public void downloadAttachmentOfNOC(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pensionNOCBean") PensionNOCBean pensionNOCBean, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        pensionNOCBean = PensionNOC.getAttachedFileforNOC(pensionNOCBean.getNocId());
        response.setContentType(pensionNOCBean.getGetvigilanceContentType());
        response.setHeader("Content-Disposition", "attachment;filename=" + pensionNOCBean.getVigilanceoriginalFilename());
        OutputStream out = response.getOutputStream();
        out.write(pensionNOCBean.getFilecontent());
        out.flush();
    }
    /*This Function is call while download the Crime branch File*/

    @RequestMapping(value = "downloadAttachmentOfNOCForCB")
    public void downloadAttachmentOfNOCForCB(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pensionNOCBean") PensionNOCBean pensionNOCBean, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        pensionNOCBean = PensionNOC.getAttachedFileforCB(pensionNOCBean.getNocId());
        response.setContentType(pensionNOCBean.getGetvigilanceContentType());
        //System.out.println("sdfsd"+pensionNOCBean.getVigilanceoriginalFilename());
        response.setHeader("Content-Disposition", "attachment;filename=" + pensionNOCBean.getCboriginalFilename());

        OutputStream out = response.getOutputStream();
        out.write(pensionNOCBean.getFilecontent());
        out.flush();
    }

    @RequestMapping(value = "GenerateNoc")
    public void GenerateNoc(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("PensionNOCBean") PensionNOCBean pnoc) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            response.setHeader("Content-Disposition", "attachment; filename=CrimeBranch_Noc.pdf");
             String logopath = context.getInitParameter("PhotoPath");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            PensionNOC.GenerateNoc(document, pnoc.getNocId(), pnoc.getHrmsid(),logopath);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "GeneratevNoc")
    public void GeneratevNoc(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("PensionNOCBean") PensionNOCBean pnoc) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            response.setHeader("Content-Disposition", "attachment; filename=Vigilance_Noc.pdf");

            String logopath = context.getInitParameter("PhotoPath");

            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            PensionNOC.GeneratevNoc(document, pnoc.getNocId(), pnoc.getHrmsid(), logopath);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "cadreNocList")
    public ModelAndView cadreNocList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("PensionNOCBean") PensionNOCBean pnoc) {
        ModelAndView mv = new ModelAndView("/AGPension/CadreNocList", "PensionNOCBean", pnoc);
        List cadreList = new ArrayList();
        cadreList = PensionNOC.cadreList(lub.getLoginspc());
        mv.addObject("cadreList", cadreList);
        mv.addObject("pensionList", PensionNOC.CadreNocList(lub.getLoginspc(), lub.getLoginoffcode()));
        return mv;
    }

    @RequestMapping(value = "CadreEmployeeList.htm")
    public ModelAndView CadreEmployeeList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pensionNOCBean") PensionNOCBean pensionNOCBean, @RequestParam("cadreValue") String cadreValue, @RequestParam("nocFor") String nocFor) {
        ModelAndView mv = new ModelAndView();
        List emplistforpension = PensionNOC.getCadreEmployeeListforPromotion(cadreValue, nocFor);
        mv.addObject("emplistforpension", emplistforpension);
        mv.addObject("nocFor", nocFor);
        mv.setViewName("AGPension/CadreEmployeeList");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "addCadreEmployeeListForPension.htm")
    public void addCadreEmployeeListForPension(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pensionNOCBean") PensionNOCBean pensionNOCBean, HttpServletResponse response, ModelMap model) throws IOException, JSONException {
        response.setContentType("application/json");
        pensionNOCBean.setOffcode(lub.getLoginoffcode());
        pensionNOCBean.setNocfor(pensionNOCBean.getNocfor());
        PensionNOC.saveCadreEmployeeListForPension(pensionNOCBean, lub.getLoginempid(), lub.getLoginspc());
        JSONObject obj = new JSONObject();
        obj.append("msg", "Y");
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();
    }

    @RequestMapping(value = "findApplication.htm")
    public ModelAndView findApplicationList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("PensionNOCBean") PensionNOCBean pnoc, @ModelAttribute("SearchApplBean") SearchApplBean sab) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ModelAndView mv = new ModelAndView("/AGPension/VigilancePensionerNocList", "PensionNOCBean", pnoc);
        try {

            String NocStatus = "N";
            mv.addObject("adminStatus", "N");
            mv.addObject("nocStatusType", "Pending");

            Date frmDate = new SimpleDateFormat("dd-MMM-yyyy").parse(pnoc.getDateFrom());
            String fromDate = sdf.format(frmDate);
            Date tDate = new SimpleDateFormat("dd-MMM-yyyy").parse(pnoc.getDateTo());
            String toDate = sdf.format(tDate);

           System.out.println("fromDate:" + fromDate + "toDate:" + toDate);

            if (lub.getLoginusername().equals("cbadmin")) {
                mv.addObject("loginName", "Crime Branch");
                mv.addObject("pensionList", PensionNOC.CBPensionerNocListByDate(NocStatus, fromDate, toDate));
            } else if (lub.getLoginusername().equals("vigilanceadmin")) {
                mv.addObject("postList", PensionNOC.getPostList());
                mv.addObject("departmentList", departmentDao.getDepartmentList());
                mv.addObject("pensionList", PensionNOC.VigilancePensionerNocListByDate(NocStatus, fromDate, toDate));
                mv.addObject("loginName", "Vigilance");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }
}
