/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.propertystatement;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.CommonFunctions;
import hrms.dao.fiscalyear.FiscalYearDAO;
import hrms.dao.propertystatement.PropertyStatementDAO;
import hrms.model.discProceeding.DpViewBean;
import hrms.model.discProceeding.ProceedingBean;
import hrms.model.login.LoginUserBean;
import hrms.model.propertystatement.PropertyDetail;
import hrms.model.propertystatement.PropertyStatement;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("LoginUserBean")
public class PropertyStatementController implements ServletContextAware {

    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Autowired
    PropertyStatementDAO propertyStatementDAO;
    @Autowired
    public FiscalYearDAO fiscalDAO;

    @RequestMapping(value = "viewpropertystatement.htm")
    public ModelAndView viewPropertyStatement(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("yearlyPropId") BigDecimal yearlyPropId) {
        PropertyStatement propertyStatement = propertyStatementDAO.getPropertyStmt(yearlyPropId);
        ModelAndView mv = new ModelAndView();
        if (lub.getLoginempid().equals(propertyStatement.getEmpid())) {
            ArrayList movablePropertyDetailList = propertyStatementDAO.getMovablePropertyDetailList(yearlyPropId);
            ArrayList immovablePropertyDetailList = propertyStatementDAO.getImmovablePropertyDetailList(yearlyPropId);
            if (immovablePropertyDetailList.size() == 0) {
                mv.addObject("isImmovableAvailable", 'N');
            }
            if (movablePropertyDetailList.size() == 0) {
                mv.addObject("isMovableAvailable", 'N');
            }
            mv.setViewName("/propertystatement/ViewPropertyStatement");
            mv.addObject("propertyStatement", propertyStatement);
            mv.addObject("movablePropertyDetailList", movablePropertyDetailList);
            mv.addObject("immovablePropertyDetailList", immovablePropertyDetailList);
        } else {
            mv.setViewName("/under_const");
        }
        return mv;
    }

    @RequestMapping(value = "viewpropertystatementpublic.htm")
    public ModelAndView viewPropertyStatementPublic(HttpServletRequest request, @RequestParam("yearlyPropId") String encryptedYearlyPropId) {
        BigDecimal yearlyPropId = new BigDecimal(CommonFunctions.decodedTxt(encryptedYearlyPropId));
        //PropertyStatement propertyStatement = propertyStatementDAO.getPropertyStmt(yearlyPropId);
        PropertyStatement propertyStatement = propertyStatementDAO.getPropertyStmt(yearlyPropId);
        ModelAndView mv = new ModelAndView();

        ArrayList movablePropertyDetailList = propertyStatementDAO.getMovablePropertyDetailList(yearlyPropId);
        ArrayList immovablePropertyDetailList = propertyStatementDAO.getImmovablePropertyDetailList(yearlyPropId);
        mv.setViewName("/propertystatement/ViewPropertyStatement");
        mv.addObject("propertyStatement", propertyStatement);
        mv.addObject("movablePropertyDetailList", movablePropertyDetailList);
        mv.addObject("immovablePropertyDetailList", immovablePropertyDetailList);

        return mv;
    }

    @RequestMapping(value = "viewpropertystatementlist.htm", method = RequestMethod.GET)
    public String viewPropertyStatementList() {
        return "/propertystatement/PropertyStatementList";
    }

    @ResponseBody
    @RequestMapping(value = "getPropertStatementList", method = RequestMethod.POST)
    public void getPropertStatementList(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList propertyStatementList = propertyStatementDAO.getPropertyList(lub.getLoginempid());
        JSONArray json = new JSONArray(propertyStatementList);
        out = response.getWriter();
        out.write(json.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getPropertyStatementDetail", method = RequestMethod.GET)
    public void getPropertyStatementDetail(HttpServletRequest request, @RequestParam("yearlyPropId") BigDecimal yearlyPropId, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        PropertyStatement propertyStatement = propertyStatementDAO.getPropertyStmt(yearlyPropId);
        JSONObject obj = new JSONObject(propertyStatement);
        out = response.getWriter();
        out.write(obj.toString());
    }

    @ResponseBody
    @RequestMapping(value = "savePropertStatement", method = RequestMethod.POST)
    public void savePropertStatement(@ModelAttribute("PropertyStatement") PropertyStatement propertyStatement, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            propertyStatement.setEmpid(lub.getLoginempid());
            propertyStatement.setCurspc(lub.getLoginspc());
            propertyStatement.setCurcadrecode(lub.getLogincadrecode());
            propertyStatement.setLoginByIp(CommonFunctions.getIpAndHost(request));
            boolean isDuplicatePeriod = propertyStatementDAO.isDuplicatePropertyPeriod(propertyStatement);
            boolean iscorrectPeriod = propertyStatementDAO.isCheckPropertyPeriod(propertyStatement.getFiscalyear(), propertyStatement.getFromdate(), propertyStatement.getTodate());
            Map result = new HashMap();
            if (iscorrectPeriod == true) {
                result.put("msg1", "Selected Property Period is Correct");

                if (isDuplicatePeriod == false) {
                    propertyStatementDAO.savePropertyStmt(propertyStatement);
                    result.put("msg", "Sucessfully Saved");
                } else {
                    result.put("msg", "Duplicate Period");
                }
            } else {
                result.put("msg", "Selected Property Period is Not Correct");
            }

            JSONObject jobj = new JSONObject(result);
            out = response.getWriter();
            out.write(jobj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "updatePropertStatement", method = RequestMethod.POST)
    public void updatePropertStatement(@ModelAttribute("PropertyStatement") PropertyStatement propertyStatement, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        propertyStatement.setEmpid(lub.getLoginempid());
        propertyStatement.setCurspc(lub.getLoginspc());
        propertyStatement.setCurcadrecode(lub.getLogincadrecode());
        Map result = new HashMap();
        propertyStatementDAO.updatePropertyStmt(propertyStatement);

        JSONObject jobj = new JSONObject(result);
        out = response.getWriter();
        out.write(jobj.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getPropertyStmt", method = RequestMethod.GET)
    public void getPropertyStmt(ModelMap model, @RequestParam("yearlyPropId") BigDecimal yearlyPropId, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        PropertyStatement propertyStatement = propertyStatementDAO.getPropertyStmt(yearlyPropId);
        JSONObject obj = new JSONObject(propertyStatement);
        out = response.getWriter();
        out.write(obj.toString());
    }

    @ResponseBody
    @RequestMapping(value = "deletePropertyStmt", method = RequestMethod.GET)
    public void deletePropertyStmt(ModelMap model, @RequestParam("yearlyPropId") BigDecimal yearlyPropId, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        boolean isDeleted = propertyStatementDAO.deletePropertyStmt(yearlyPropId, lub.getLoginempid());
        JSONObject obj = new JSONObject();
        obj.append("isDeleted", isDeleted);
        out = response.getWriter();
        out.write(obj.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getMovablePropertyDetailList", method = RequestMethod.POST)
    public void getMovablePropertyDetailList(ModelMap model, @RequestParam("yearlyPropId") BigDecimal yearlyPropId, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList propertyDetailList = propertyStatementDAO.getMovablePropertyDetailList(yearlyPropId);
        JSONArray json = new JSONArray(propertyDetailList);
        out = response.getWriter();
        out.write(json.toString());
    }

    @ResponseBody
    @RequestMapping(value = "submitPropertyStatement", method = RequestMethod.POST)
    public void submitPropertyStatement(ModelMap model, @RequestParam("yearlyPropId") BigDecimal yearlyPropId, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("isImmovableBlank") String isImmovableBlank, @RequestParam("isMovableBlank") String isMovableBlank, HttpServletResponse response,
            HttpServletRequest request, @ModelAttribute("PropertyStatement") PropertyStatement propertyStatement) throws Exception {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONObject obj = new JSONObject();
        boolean isclosed = propertyStatementDAO.isPropertySubmittedDateClosed(yearlyPropId);
        if (isclosed == false) {
            String Ip = CommonFunctions.getIpAndHost(request);
            String qrCodePath = servletContext.getInitParameter("PropertyPath");
            ArrayList propertyDetailList = propertyStatementDAO.getImmovablePropertyDetailList(yearlyPropId);
            if (propertyDetailList.size() == 0) {
                obj.append("isImmovableAvailable", "N");
            }
            ArrayList movablePropertyDetailList = propertyStatementDAO.getMovablePropertyDetailList(yearlyPropId);
            if (movablePropertyDetailList.size() == 0) {
                obj.append("isMovableAvailable", "N");
            }
            if ((propertyDetailList.size() == 0 && isImmovableBlank.equals("Y")) && (movablePropertyDetailList.size() > 0 && isMovableBlank.equals("N"))) {
                boolean isSubmitted = propertyStatementDAO.submitPropertyStatement(yearlyPropId, lub.getLoginempid(), isImmovableBlank, isMovableBlank, qrCodePath, Ip);
                obj.append("isSubmitted", "S");
            } else if ((propertyDetailList.size() == 0 && isImmovableBlank.equals("Y")) && (movablePropertyDetailList.size() == 0 && isMovableBlank.equals("Y"))) {
                boolean isSubmitted = propertyStatementDAO.submitPropertyStatement(yearlyPropId, lub.getLoginempid(), isImmovableBlank, isMovableBlank, qrCodePath, Ip);
                obj.append("isSubmitted", "S");
            } else if ((propertyDetailList.size() > 0 && isImmovableBlank.equals("N")) && (movablePropertyDetailList.size() > 0 && isMovableBlank.equals("N"))) {
                boolean isSubmitted = propertyStatementDAO.submitPropertyStatement(yearlyPropId, lub.getLoginempid(), isImmovableBlank, isMovableBlank, qrCodePath, Ip);
                obj.append("isSubmitted", "S");
            } else if ((propertyDetailList.size() > 0 && isImmovableBlank.equals("N")) && (movablePropertyDetailList.size() == 0 && isMovableBlank.equals("Y"))) {
                boolean isSubmitted = propertyStatementDAO.submitPropertyStatement(yearlyPropId, lub.getLoginempid(), isImmovableBlank, isMovableBlank, qrCodePath, Ip);
                obj.append("isSubmitted", "S");
            }
        } else {
            obj.append("msg", "Property Statement Submission Date For the Financial Year Is Closed");
        }
        out = response.getWriter();
        out.write(obj.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getImmovablePropertyDetailList", method = RequestMethod.POST)
    public void getImmovablePropertyDetailList(ModelMap model, @RequestParam("yearlyPropId") BigDecimal yearlyPropId, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList propertyDetailList = propertyStatementDAO.getImmovablePropertyDetailList(yearlyPropId);
        JSONArray json = new JSONArray(propertyDetailList);
        out = response.getWriter();
        out.write(json.toString());
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping(value = "saveImmovablePropertyDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public void saveImmovablePropertyDetail(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("PropertyDetail") PropertyDetail prtdtl, HttpServletResponse response) throws IOException {
        int mCode = propertyStatementDAO.saveImmovableProperty(prtdtl);
        PrintWriter out = null;
        Map result = new HashMap();
        result.put("mCode", mCode);
        JSONObject job = new JSONObject(result);
        out = response.getWriter();
        out.write(job.toString());
    }

    @RequestMapping(value = "saveMovablePropertyDetail", method = RequestMethod.POST)
    public void saveMovablePropertyDetail(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("PropertyDetail") PropertyDetail prtdtl, HttpServletResponse response) throws IOException {
        int mCode = propertyStatementDAO.saveMovableProperty(prtdtl);

        PrintWriter out = null;
        Map result = new HashMap();
        result.put("mCode", mCode);
        JSONObject job = new JSONObject(result);
        out = response.getWriter();
        out.write(job.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getImmovableProperty", method = RequestMethod.GET)
    public void getImmovableProperty(ModelMap model, @RequestParam("propertyDtlsId") BigDecimal propertyDtlsId, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        PropertyDetail propertyDetail = propertyStatementDAO.getImmovableProperty(propertyDtlsId);
        JSONObject obj = new JSONObject(propertyDetail);
        out = response.getWriter();
        out.write(obj.toString());
    }

    @ResponseBody
    @RequestMapping(value = "deleteImmovableProperty", method = RequestMethod.POST)
    public void deleteImmovableProperty(ModelMap model, @RequestParam("propertyDtlsId") BigDecimal propertyDtlsId, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");
        PrintWriter out = null;
        boolean isDeleted = propertyStatementDAO.deleteImmovableProperty(propertyDtlsId, lub.getLoginempid());
        JSONObject obj = new JSONObject();
        obj.append("isDeleted", isDeleted);
        out = response.getWriter();
        out.write(obj.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getMovableProperty", method = RequestMethod.GET)
    public void getMovableProperty(ModelMap model, @RequestParam("propertyDtlsId") BigDecimal propertyDtlsId, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;

        PropertyDetail propertyDetail = propertyStatementDAO.getMovableProperty(propertyDtlsId);
        JSONObject obj = new JSONObject(propertyDetail);
        out = response.getWriter();
        out.write(obj.toString());
    }

    @ResponseBody
    @RequestMapping(value = "deleteMovableProperty", method = RequestMethod.POST)
    public void deleteMovableProperty(ModelMap model, @RequestParam("propertyDtlsId") BigDecimal propertyDtlsId, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");
        PrintWriter out = null;
        boolean isDeleted = propertyStatementDAO.deleteMovableProperty(propertyDtlsId, lub.getLoginempid());
        JSONObject obj = new JSONObject();
        obj.append("isDeleted", isDeleted);
        out = response.getWriter();
        out.write(obj.toString());
    }

    @RequestMapping(value = "GetPFiscalYearListJSON", method = RequestMethod.POST)
    public @ResponseBody
    String getPFiscalYearListJSON() {

        JSONArray json = null;
        try {
            List fiscalyearlist = fiscalDAO.getPFiscalYearList();
            json = new JSONArray(fiscalyearlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "GetPreviousFiscalYearPropertyStatementDataJSON")
    public String GetPreviousFiscalYearPropertyStatementDataJSON(@ModelAttribute("LoginUserBean") LoginUserBean lub) {

        JSONArray json = null;
        try {
            List fiscalyearlist = propertyStatementDAO.getPreviousFiscalYearPropertyStatementData(lub.getLoginempid());
            json = new JSONArray(fiscalyearlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return json.toString();
    }

    @RequestMapping(value = "PropertyStatementStatus.htm")
    public String propertyStatementStatus(ModelMap model) {

        List propertyStatementStatusList = propertyStatementDAO.getPropertyStatementStatusData();
        model.addAttribute("propertstatementstatus", propertyStatementStatusList);

        return "/propertystatement/PropertyStatementStatusReport";
    }

    @RequestMapping(value = "viewPropertyStatementPDF.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public void viewPropertyStatementPDF(HttpServletResponse response, @RequestParam("yearlyPropId") String encryptedYearlyPropId) {
        BigDecimal yearlyPropId = new BigDecimal(CommonFunctions.decodedTxt(encryptedYearlyPropId));
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        try {
            //String reviewedPARFilePath = context.getInitParameter("ReviewedPARFilePath");
            //String filePath = reviewedPARFilePath + paf.getFiscalYear() + CommonFunctions.getResourcePath();
            String qrCodePath = servletContext.getInitParameter("PropertyPath");
            PropertyStatement propertyStatement = propertyStatementDAO.getPropertyStmt(yearlyPropId);
            ArrayList movablePropertyDetailList = propertyStatementDAO.getMovablePropertyDetailList(yearlyPropId);
            ArrayList immovablePropertyDetailList = propertyStatementDAO.getImmovablePropertyDetailList(yearlyPropId);
            response.setHeader("Content-Disposition", "inline; filename=Property Statement.pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            propertyStatementDAO.viewPropertyStatementPDF(propertyStatement, movablePropertyDetailList, immovablePropertyDetailList, document, qrCodePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "viewPropertyStatementPDFPublic.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public void viewPropertyStatementPDFPublic(HttpServletResponse response, @RequestParam("yearlyPropId") String encryptedYearlyPropId) {
        BigDecimal yearlyPropId = new BigDecimal(CommonFunctions.decodedTxt(encryptedYearlyPropId));
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        try {
            //String reviewedPARFilePath = context.getInitParameter("ReviewedPARFilePath");
            //String filePath = reviewedPARFilePath + paf.getFiscalYear() + CommonFunctions.getResourcePath();
            String qrCodePath = servletContext.getInitParameter("PropertyPath");
            PropertyStatement propertyStatement = propertyStatementDAO.getPropertyStmt(yearlyPropId);
            ArrayList movablePropertyDetailList = propertyStatementDAO.getMovablePropertyDetailList(yearlyPropId);
            ArrayList immovablePropertyDetailList = propertyStatementDAO.getImmovablePropertyDetailList(yearlyPropId);
            response.setHeader("Content-Disposition", "inline; filename=Property Statement.pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            propertyStatementDAO.viewPropertyStatementPDF(propertyStatement, movablePropertyDetailList, immovablePropertyDetailList, document, qrCodePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "viewPropertyStatementindividualLoginPDF.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public void viewPropertyStatementindividualLoginPDF(HttpServletResponse response, @RequestParam("yearlyPropId") BigDecimal yearlyPropId,
            @RequestParam("fiscalyear") String fiscalyear) {
        //BigDecimal yearlyPropId = new BigDecimal(CommonFunctions.decodedTxt(encryptedYearlyPropId));
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        try {
            String qrCodePath = servletContext.getInitParameter("PropertyPath");
            PropertyStatement propertyStatement = propertyStatementDAO.getPropertyStmt(yearlyPropId);
            ArrayList movablePropertyDetailList = propertyStatementDAO.getMovablePropertyDetailList(yearlyPropId);
            ArrayList immovablePropertyDetailList = propertyStatementDAO.getImmovablePropertyDetailList(yearlyPropId);
            response.setHeader("Content-Disposition", "inline; filename=Property Statement.pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            propertyStatement.setFiscalyear(fiscalyear);
            propertyStatementDAO.viewPropertyStatementPDF(propertyStatement, movablePropertyDetailList, immovablePropertyDetailList, document, qrCodePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "GetFiscalYearListCYWiseJSON", method = RequestMethod.POST)
    public @ResponseBody
    void getFiscalYearListCYWiseJSON(HttpServletResponse response) {
        response.setContentType("application/json");
        JSONArray json = null;
        PrintWriter out = null;
        try {
            List fiscalyearlistcy = fiscalDAO.getFiscalYearListCYWise();
            json = new JSONArray(fiscalyearlistcy);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

}
