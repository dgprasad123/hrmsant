/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.annualBudget;

import com.fasterxml.jackson.databind.ObjectMapper;
import hrms.common.CommonFunctions;
import hrms.dao.annualBudget.AnnualBudgetDAO;
import hrms.model.annualBudget.Annexure1a;
import hrms.model.annualBudget.Annexure1b;
import hrms.model.annualBudget.Annexure1c;
import hrms.model.annualBudget.Annexure2;
import hrms.model.annualBudget.AnnualBudgetForm;
import hrms.model.annualBudget.AnnualBudgetFormDC;
import hrms.model.annualBudget.BudgetResponseStatus;
import hrms.model.login.LoginUserBean;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONArray;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Surendra
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj", "SelectedEmpOffice"})
public class AnnualBudgetDDOController implements ServletContextAware {

    private ServletContext context;

    @Autowired
    public AnnualBudgetDAO annualBudgetDAO;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "budgetListForDDOController")
    public String budgetListForDDOController(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice) {

        //System.out.println("selectedEmpOffice is: " + selectedEmpOffice);

        String selectedddocode = annualBudgetDAO.getDDOCodeSelectedOfficeWise(selectedEmpOffice);

        List budgetlist = annualBudgetDAO.getAnnualBudgetList(selectedddocode);
        model.addAttribute("budgetlist", budgetlist);

        /*String submitStatus = annualBudgetDAO.checkDDOAnnualBudgetSubmitStatus(selectedddocode, "2021-22");
        model.addAttribute("submitStatus", submitStatus);*/

        return "/annualBudget/AnnualBudgetDDOList";
    }

    @RequestMapping(value = "submitAnnualbudgetData")
    public String submitAnnualbudgetData(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("annualBudgetForm") AnnualBudgetForm annualBudgetForm, @RequestParam("budgetid") String budgetid, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice) {

        JSONObject jsonObject = new JSONObject();
        String jsonString = null;
        try {
            String fileKeyPath = context.getInitParameter("hrmsconfig");

            annualBudgetForm.setFinancialYear("2024-25"); // should dynamic

            JSONArray jsonArray = null;

            String selectedddocode = annualBudgetDAO.getDDOCodeSelectedOfficeWise(selectedEmpOffice);

            List<Annexure1a> annexure1a = annualBudgetDAO.getAnnexure1Adata(selectedddocode, annualBudgetForm.getFinancialYear());
            if (annexure1a != null && annexure1a.size() > 0) {
                jsonArray = new JSONArray(annexure1a);
                jsonObject.put("annexure1a", jsonArray);
            }

            List<Annexure1b> annexure1b = annualBudgetDAO.getAnnexure1Bdata(selectedddocode, annualBudgetForm.getFinancialYear());
            if (annexure1b != null && annexure1b.size() > 0) {
                jsonArray = new JSONArray(annexure1b);
                jsonObject.put("annexure1b", jsonArray);
            }

            List<Annexure1c> annexure1c = annualBudgetDAO.getAnnexure1Cdata(selectedddocode, annualBudgetForm.getFinancialYear());
            if (annexure1c != null && annexure1c.size() > 0) {
                jsonArray = new JSONArray(annexure1c);
                jsonObject.put("annexure1c", jsonArray);
            }

            /*List<Annexure2> annexure2 = annualBudgetDAO.getAnnexure2data(selectedddocode, annualBudgetForm.getFinancialYear());
            if (annexure2 != null && annexure2.size() > 0) {
                jsonArray = new JSONArray(annexure2);
                jsonObject.put("annexure2", jsonArray);
            }*/
            jsonString = annualBudgetDAO.sendBudgetDataToIFMS(jsonObject.toString(), fileKeyPath + "HRMS.key");
            //System.out.println("string=="+jsonString);

            URIBuilder uriBuilder = new URIBuilder();
            //https://uat.odishatreasury.gov.in/bdbillreceivingws/0.1/authenticate
            // www.odishatreasury.gov.in/webservices/submit-annexure
            uriBuilder.setScheme("https").setHost("www.odishatreasury.gov.in").setPath("/webservices/submit-annexure/");

            HttpPost postRequest = new HttpPost(uriBuilder.build());
            postRequest.addHeader("accept", "application/json");

            StringEntity requestEntity = new StringEntity(jsonString);
            requestEntity.setContentType("application/json");
            //DefaultHttpClient httpClient = new DefaultHttpClient();
            CloseableHttpClient httpClient = CommonFunctions.createAcceptSelfSignedCertificateClient();

            //CloseableHttpClient httpClient = HttpClients.createDefault();
            postRequest.setEntity(requestEntity);
            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            ObjectMapper Obj = new ObjectMapper();

            /*BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
             String output = "";
             Boolean keepGoing = true;
             while (keepGoing) {
             String currentLine = br.readLine();

             if (currentLine == null) {
             keepGoing = false;
             } else {
             output += currentLine;
             }
             }
             System.out.println(output);*/
            BudgetResponseStatus budgres = Obj.readValue(response.getEntity().getContent(), BudgetResponseStatus.class);
            if (budgres != null && budgres.getStatus().equalsIgnoreCase("0")) {
            //if (response.getStatusLine().getStatusCode() == 200){
                // data successfully submitted. 
                // so do data base code is here.
                //System.out.println("Submitted successfully" + budgres.getStatus());
                annualBudgetDAO.updateDDOBudgetAfterSubmit(budgetid);
            }

            httpClient.getConnectionManager().shutdown();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/budgetListForDDOController.htm";
    }

    @RequestMapping(value = "CreateNewBudgetData")
    public String CreateNewBudget(Model model, @ModelAttribute("annualBudgetForm") AnnualBudgetForm annualBudgetForm, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice) {

        try {
            List fylist = annualBudgetDAO.getFinancialYearList();
            model.addAttribute("financialYearList", fylist);

            String selectedddocode = annualBudgetDAO.getDDOCodeSelectedOfficeWise(selectedEmpOffice);
             
            String createStatus = annualBudgetDAO.checkDDOAnnualBudgetCreateStatus(selectedddocode, "2024-25");
            model.addAttribute("createStatus", createStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/annualBudget/CreateNewBudgetData";
    }

    @RequestMapping(value = "ProcessBudgetData")
    public String ProcessBudgetData(@ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("annualBudgetForm") AnnualBudgetForm annualBudgetForm, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice) {

        try {
            String selectedddocode = annualBudgetDAO.getDDOCodeSelectedOfficeWise(selectedEmpOffice);
            annualBudgetDAO.processBudgetData(annualBudgetForm.getFinancialYear(), selectedddocode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/budgetListForDDOController.htm";
    }

    @RequestMapping(value = "DDOAnnexureIIAController")
    public String DDOAnnexureIIAController(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("budgetid") String budgetid, @ModelAttribute("annualBudgetForm") AnnualBudgetForm annualBudgetForm) {

        try {
            List annexureIIAdataList = annualBudgetDAO.getAnnexureIIAData(budgetid);
            model.addAttribute("annexureIIAdataList", annexureIIAdataList);
            model.addAttribute("ddoname", lub.getLoginoffname());
            model.addAttribute("budgetid", budgetid);

            String isLocked = annualBudgetDAO.getDDOBudgetLockStatus(budgetid);
            model.addAttribute("isLocked", isLocked);

            model.addAttribute("financialyear", annualBudgetDAO.getDDOBudgetFinancialYear(budgetid));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/annualBudget/AnnualBudgetDDOAnnexureIIA";
    }

    @RequestMapping(value = "DDOAnnexureIIBController")
    public ModelAndView DDOAnnexureIIBController(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("budgetid") String budgetid) {

        List annexureIIBdataList = annualBudgetDAO.getAnnexureIIBData(budgetid);

        String path = "/annualBudget/AnnualBudgetDDOAnnexureIIB";

        ModelAndView mav = new ModelAndView();
        mav.addObject("annexureIIBdataList", annexureIIBdataList);
        mav.addObject("ddoname", lub.getLoginoffname());
        mav.addObject("budgetid", budgetid);

        String isLocked = annualBudgetDAO.getDDOBudgetLockStatus(budgetid);
        mav.addObject("isLocked", isLocked);

        mav.addObject("financialyear", annualBudgetDAO.getDDOBudgetFinancialYear(budgetid));

        mav.setViewName(path);

        return mav;
    }

    @RequestMapping(value = "DDOAnnexureIIIAController")
    public ModelAndView DDOAnnexureIIIAController(ModelMap model, HttpServletResponse response) {

        String path = "/annualBudget/AnnualBudgetDDOAnnexureIIIA";

        ModelAndView mav = new ModelAndView();

        mav.setViewName(path);

        return mav;
    }

    @RequestMapping(value = "DDOAnnexureIIIBController")
    public ModelAndView DDOAnnexureIIIBController(ModelMap model, HttpServletResponse response) {

        String path = "/annualBudget/AnnualBudgetDDOAnnexureIIIB";

        ModelAndView mav = new ModelAndView();

        mav.setViewName(path);

        return mav;
    }

    @RequestMapping(value = "DDOAnnexureIIICController")
    public ModelAndView DDOAnnexureIIICController(ModelMap model, HttpServletResponse response) {

        String path = "/annualBudget/AnnualBudgetDDOAnnexureIIIC";

        ModelAndView mav = new ModelAndView();

        mav.setViewName(path);

        return mav;
    }

    @RequestMapping(value = "LockDDOBudget")
    public String DDOAnnexureIIICController(@RequestParam("budgetid") String budgetid) {

        try {
            annualBudgetDAO.lockDDOBudget(budgetid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/budgetListForDDOController.htm";
    }

    @ResponseBody
    @RequestMapping(value = "saveAnnexureIEmployeeBudgetData")
    public void saveAnnexureIEmployeeBudgetData(@RequestParam Map<String, String> requestParams) {

        try {
            String budgetid = requestParams.get("budgetid");
            String detailid = requestParams.get("detailid");
            String empid = requestParams.get("empid");

            String basicNextYear = requestParams.get("basicNextYear");
            String da156 = requestParams.get("da");
            String hra403 = requestParams.get("hra");
            String arrear = requestParams.get("arrear");
            String oa = requestParams.get("oa");
            String rcm = requestParams.get("rcm");

            annualBudgetDAO.updateAnnexureIEmployeeBudgetData(budgetid, detailid, empid, arrear, oa, rcm, basicNextYear, da156, hra403);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "updateAdditionalAmountData")
    public void updateAdditionalAmountData(@RequestParam Map<String, String> requestParams) {

        try {
            String budgetid = requestParams.get("budgetid");
            String detailid = requestParams.get("detailid");
            String amount = requestParams.get("amount");

            annualBudgetDAO.updateAdditionalAmountData(budgetid, detailid, amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "updateExclusionsData")
    public void updateExclusionsData(@RequestParam Map<String, String> requestParams) {

        try {
            String budgetid = requestParams.get("budgetid");
            String detailid = requestParams.get("detailid");
            String amount = requestParams.get("amount");

            annualBudgetDAO.updateExclusionsData(budgetid, detailid, amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "updateContractualData")
    public void updateContractualData(@RequestParam Map<String, String> requestParams) {

        try {
            String budgetid = requestParams.get("budgetid");
            String detailid = requestParams.get("detailid");

            String incrDcrMenInPositionData = requestParams.get("incrDcrMenInPosition");
            String txtTotalMenInPositionData = requestParams.get("txtTotalMenInPosition");
            String actualExp201920Data = requestParams.get("actualExp201920");
            String actualExp202021Data = requestParams.get("actualExp202021");
            String revisedEstimate202021Data = requestParams.get("revisedEstimate202021");
            String be202122Data = requestParams.get("be202122");

            annualBudgetDAO.updateContractualData(budgetid, detailid, incrDcrMenInPositionData, actualExp201920Data, actualExp202021Data, revisedEstimate202021Data, be202122Data, txtTotalMenInPositionData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "updateBudgetAbstractData")
    public void updateBudgetAbstractData(@RequestParam Map<String, String> requestParams) {

        try {
            String budgetid = requestParams.get("budgetid");
            String detailid = requestParams.get("detailid");
            
            String sanctionedStrength = requestParams.get("sanctionedStrength");
            String currentVacancy = requestParams.get("currentVacancy");
            String anticipatedVacancy = requestParams.get("anticipatedVacancy");
            String vacancytobefilled = requestParams.get("vacancytobefilled");
            String anticipatedMenInPosition = requestParams.get("anticipatedMenInPosition");

            annualBudgetDAO.updateBudgetAbstractData(budgetid, detailid, anticipatedVacancy, vacancytobefilled, anticipatedMenInPosition, currentVacancy,sanctionedStrength);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "deleteDDOAnnualBudget")
    public String deleteDDOAnnualBudget(@RequestParam("budgetid") String budgetid) {

        try {
            annualBudgetDAO.deleteDDOAnnualBudget(budgetid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/budgetListForDDOController.htm";
    }

    @RequestMapping(value = "ReProcessBudgetAnnexureIIAData")
    public String ReProcessBudgetAnnexureIIAData(@ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("annualBudgetForm") AnnualBudgetForm annualBudgetForm, @RequestParam("budgetid") String budgetid, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice) {

        try {
            String selectedddocode = annualBudgetDAO.getDDOCodeSelectedOfficeWise(selectedEmpOffice);
            annualBudgetDAO.ReProcessBudgetAnnexureIIAData(annualBudgetForm.getFinancialYear(), selectedddocode, budgetid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/budgetListForDDOController.htm";
    }

    @RequestMapping(value = "ReProcessBudgetAnnexureIIBData")
    public String ReProcessBudgetAnnexureIIBData(@ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("annualBudgetForm") AnnualBudgetForm annualBudgetForm, @RequestParam("budgetid") String budgetid, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice) {

        try {
            String selectedddocode = annualBudgetDAO.getDDOCodeSelectedOfficeWise(selectedEmpOffice);
            annualBudgetDAO.ReProcessBudgetAnnexureIIBData(annualBudgetForm.getFinancialYear(), selectedddocode, budgetid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/budgetListForDDOController.htm";
    }

    @RequestMapping(value = "UnlockDDOAnnualBudgetPageDC")
    public String UnlockDDOAnnualBudgetPageDC(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("annualBudgetFormDC") AnnualBudgetFormDC annualBudgetFormDC) {

        String path = "/annualBudget/AnnualBudgetListDC";

        try {
            if (annualBudgetFormDC.getDdocode() != null && !annualBudgetFormDC.getDdocode().equals("")) {
                String viewstatus = annualBudgetDAO.getDDOCodeDistrictDC(lub.getLogindistrictcode(), annualBudgetFormDC.getDdocode());
                if (lub.getLoginusertype().equalsIgnoreCase("A") || lub.getLoginusertype().equalsIgnoreCase("S")) {
                    viewstatus = "Y";
                }
                if (viewstatus.equals("Y")) {
                    if (annualBudgetFormDC.getDdocode() != null && !annualBudgetFormDC.getDdocode().equals("")) {
                        List budgetlist = annualBudgetDAO.getDDOAnnualBudgetDataDC(annualBudgetFormDC.getDdocode(), "2024-25");
                        model.addAttribute("budgetlist", budgetlist);

                        path = "/annualBudget/AnnualBudgetListDC";
                    }
                } else {
                    path = "under_const";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    @RequestMapping(value = "UnlockDDOAnnualBudgetDC")
    public String UnlockDDOAnnualBudgetDC(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("annualBudgetFormDC") AnnualBudgetFormDC annualBudgetFormDC
    ) {

        try {
            annualBudgetDAO.unlockDDOBudgetDC(annualBudgetFormDC.getBudgetid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/UnlockDDOAnnualBudgetPageDC.htm?ddocode=" + annualBudgetFormDC.getDdocode();
    }

    @RequestMapping(value = "downloadAnnualBudgetExcel")
    public void downloadAnnualBudgetExcel(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) {

        response.setContentType("application/vnd.ms-excel");

        OutputStream out = null;

        try {
            String budgetid = requestParams.get("budgetid");
            String chartacc = requestParams.get("chartacc");
            String type = requestParams.get("type");

            out = response.getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);

            response.setHeader("Content-Disposition", "attachment; filename=" + type + ".xls");

            annualBudgetDAO.downloadAnnualBudgetDataExcel(workbook, budgetid, chartacc, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "uploadAnnualBudgetExcel")
    public String uploadAnnualBudgetExcel(@ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("annualBudgetForm") AnnualBudgetForm annualBudgetForm, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice) {

        MultipartFile uploadedFile = annualBudgetForm.getUploadedBudgetFile();

        Workbook workbook;
        try {

            String selectedddocode = annualBudgetDAO.getDDOCodeSelectedOfficeWise(selectedEmpOffice);
            if (uploadedFile != null && !uploadedFile.isEmpty()) {
                if (uploadedFile.getOriginalFilename().endsWith("xlsx")) {

                }
                if (uploadedFile.getOriginalFilename().endsWith("xls")) {

                    InputStream inputStream = null;

                    inputStream = uploadedFile.getInputStream();
                    workbook = Workbook.getWorkbook(inputStream);
                    annualBudgetDAO.uploadAnnualBudgetDataExcel(workbook, annualBudgetForm.getBudgetid(), annualBudgetForm.getUploadDedType(), selectedddocode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/DDOAnnexureIIAController.htm?budgetid=" + annualBudgetForm.getBudgetid();
    }
}
