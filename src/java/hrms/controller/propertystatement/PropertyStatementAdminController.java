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
import hrms.dao.performanceappraisal.PARAdminDAO;
import hrms.dao.performanceappraisal.PARBrowserDAO;
import hrms.dao.propertystatement.PropertyStatementAdminDAO;
import hrms.dao.propertystatement.PropertyStatementDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.master.Module;
import hrms.model.propertystatement.PropertyAdminSearchCriteria;
import hrms.model.propertystatement.PropertySearchResult;
import hrms.model.propertystatement.PropertyStatement;
import hrms.model.propertystatement.PropertyStatementAdminBean;
import hrms.model.propertystatement.PropertyStatementStatusBean;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
 * @author Manisha
 */
@Controller
@SessionAttributes({"LoginUserBean", "users", "Privileges"})
public class PropertyStatementAdminController implements ServletContextAware{
   private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    
    @Autowired
    PropertyStatementAdminDAO propertyStatementAdminDAO;

    @Autowired
    public FiscalYearDAO fiscalDAO;

    @Autowired
    PARAdminDAO parAdminDAO;

    @Autowired
    public PARBrowserDAO parbrowserDao;
    
    @Autowired
    PropertyStatementDAO propertyStatementDAO;

    @RequestMapping(value = "viewPropertyStatementadmin.htm", method = RequestMethod.GET)
    public ModelAndView viewPropertyStatementadmin(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("Privileges") Module[] privileges) {
        ModelAndView mv = new ModelAndView();
        if (CommonFunctions.hasPrivileage(privileges, "viewPropertyStatementadmin.htm")) {
            //List fiscyear = fiscalDAO.getFiscalYearList();
            //mv.addObject("fiscyear", fiscyear);
            mv.setViewName("/propertystatement/PropertyStatementAdmin");
        } else {
            mv.setViewName("/under_const");
        }
        return mv;
    }

    @RequestMapping(value = "propertyStatementadmin.htm", method = RequestMethod.GET)
    public ModelAndView viewPropertyStatementAdmin(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("users") Users users) {
        ModelAndView mv = new ModelAndView("/under_const");
        List fiscyear = fiscalDAO.getFiscalYearList();
        mv.addObject("fiscyear", fiscyear);
        if (users.getHaspropertyadminAuthorization() != null && users.getHaspropertyadminAuthorization().equals("Y")) {
            mv = new ModelAndView("/propertystatement/PropertyStatementAdminPage");
        }
        return mv;
    }

    @RequestMapping(value = "propertyStatementListForDDO.htm", method = RequestMethod.GET)
    public ModelAndView propertyStatementListForDDO(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("users") Users users) {
        ModelAndView mv = new ModelAndView();
        ArrayList officeListDDO = propertyStatementAdminDAO.getOfficeListForDDO(lub.getLoginempid());
        mv.addObject("officeListDDO", officeListDDO);
        mv.setViewName("/propertystatement/PropertyStatementReportForDDO");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "getSearchPROPERTYList.htm")
    public void getSearchPROPERTYList(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("propertyAdminSearchCriteria") PropertyAdminSearchCriteria propertyAdminSearchCriteria) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        PropertyStatementAdminBean propertyadmin = new PropertyStatementAdminBean();
        PropertySearchResult propertySearchResult = propertyStatementAdminDAO.getPropertyStatement(propertyAdminSearchCriteria.getFiscalyear(), propertyAdminSearchCriteria.getSearchCriteria(), propertyAdminSearchCriteria.getSearchString(), propertyAdminSearchCriteria.getRows(), propertyAdminSearchCriteria.getPage());
        //mv.addObject("propertyList", propertyList);
        propertyadmin.setIsClosed(propertyadmin.getIsClosed());
        JSONObject obj = new JSONObject(propertySearchResult);
        out = response.getWriter();
        out.write(obj.toString());
        out.close();
        out.flush();
    }

    @ResponseBody
    @RequestMapping(value = "getSearchPROPERTYListForDDO.htm")
    public void getSearchPROPERTYListForDDO(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("propertyAdminSearchCriteria") PropertyAdminSearchCriteria propertyAdminSearchCriteria) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        PropertyStatementAdminBean propertyadmin = new PropertyStatementAdminBean();
        PropertySearchResult propertySearchResult = propertyStatementAdminDAO.getPropertyStatementForDDO(propertyAdminSearchCriteria.getFiscalyear(), propertyAdminSearchCriteria.getOffcode(), propertyAdminSearchCriteria.getSearchCriteria(), propertyAdminSearchCriteria.getSearchString(), propertyAdminSearchCriteria.getRows(), propertyAdminSearchCriteria.getPage());
        propertyadmin.setIsClosed(propertyadmin.getIsClosed());
        JSONObject obj = new JSONObject(propertySearchResult);
        out = response.getWriter();
        out.write(obj.toString());
        out.close();
        out.flush();
    }
    
    @RequestMapping(value = "viewPropertyStatementPDFForDDO.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public void viewPropertyStatementPDFForDDO(HttpServletResponse response, @RequestParam("yearlyPropId") BigDecimal yearlyPropId) {
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
            propertyStatementDAO.viewPropertyStatementPDF(propertyStatement, movablePropertyDetailList, immovablePropertyDetailList, document,qrCodePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }


    /*@ResponseBody
     @RequestMapping(value = "updatePROPERTYList.htm")
     public void updatePROPERTYList(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("propertyStatementAdminBean") PropertyStatementAdminBean propertyStatementAdminBean) throws IOException, JSONException {
     response.setContentType("application/json");
     PrintWriter out = null;
     ModelAndView mv = new ModelAndView("/propertystatement/PropertyStatementAdmin");
     String msg = propertyStatementAdminDAO.unlockPropertyStatement(lub.getLoginempid(),propertyStatementAdminBean);
     JSONObject obj = new JSONObject();
     obj.append("msg", msg);
     out = response.getWriter();
     out.write(obj.toString());
     out.close();
     out.flush();
     } */
    @RequestMapping(value = "groupwisePropertyStatementReport.htm")
    public ModelAndView groupwisePropertyStatementReport(@ModelAttribute("propertyStatementAdminBean") PropertyStatementAdminBean propertyStatementAdminBean) {
        ModelAndView mv = new ModelAndView();
        List propertyStatusList = propertyStatementAdminDAO.getGroupWisePropertyStatus(propertyStatementAdminBean.getFiscalyear());
        mv.addObject("propertyStatusList", propertyStatusList);
        mv.setViewName("/propertystatement/GroupWisePropertyReport");
        return mv;
    }

    @RequestMapping(value = "groupwisePropertyStatementReport.htm", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView BackgroupwisePropertyStatementReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("Privileges") Module[] privileges) {
        ModelAndView mav = new ModelAndView();

        ModelAndView mv = new ModelAndView();
        if (CommonFunctions.hasPrivileage(privileges, "viewPropertyStatementadmin.htm")) {
            List fiscyear = fiscalDAO.getFiscalYearList();
            mv.addObject("fiscyear", fiscyear);
            mv.setViewName("/propertystatement/PropertyStatementAdmin");
        } else {
            mv.setViewName("/under_const");
        }
        return mv;
    }

    @RequestMapping(value = "viewPropertyStatementForAuthority.htm", method = RequestMethod.GET)
    public ModelAndView viewPARStatementForAuthority(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        List fiscyear = fiscalDAO.getFiscalYearList();
        mv.addObject("fiscyear", fiscyear);
        mv.setViewName("/propertystatement/PropertyStatementStatusReportForAuthority");
        return mv;
    }

    @RequestMapping(value = "groupwisePropertyStatementReportForAuthority.htm")
    public ModelAndView groupwisePropertyStatementReportForAuthority(@ModelAttribute("propertyStatementAdminBean") PropertyStatementAdminBean propertyStatementAdminBean) {
        ModelAndView mv = new ModelAndView();
        List propertyStatusList = propertyStatementAdminDAO.getGroupWisePropertyStatus(propertyStatementAdminBean.getFiscalyear());
        mv.addObject("propertyStatusList", propertyStatusList);
        mv.addObject("fiscalyear", propertyStatementAdminBean.getFiscalyear());
        mv.setViewName("/propertystatement/GroupwisePropertyStatementReportForAuthority");
        return mv;
    }

    @RequestMapping(value = "groupwisePropertyStatementReportForAuthority.htm", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView BackgroupwisePropertyStatementReportForAuthority(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("Privileges") Module[] privileges) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:viewPropertyStatementForAuthority.htm");
        return mav;

    }

    @RequestMapping(value = "propertyStatementStatusManageByPARAdmin", method = RequestMethod.GET)
    public ModelAndView propertyStatementStatusManageByPARAdmin(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("propertyStatementStatusBean") PropertyStatementStatusBean propertyStatementStatusBean, @ModelAttribute("Privileges") Module[] privileges) {
        ModelAndView mv = new ModelAndView();
        if (CommonFunctions.hasPrivileage(privileges, "viewPARAdmin.htm") == true) {
            mv.setViewName("/propertystatement/PropertyStatusChangeDetail");
            List fiscalyearlistcy = fiscalDAO.getFiscalYearListCYWise();
            mv.addObject("fiscalyearlistcy", fiscalyearlistcy);
        } else {
            mv.setViewName("/under_const");
        }
        return mv;

    }

    @RequestMapping(value = "propertyStatementStatusManageByPARAdmin.htm", params = {"action=Save"}, method = RequestMethod.POST)
    public ModelAndView saveparStatusManageByPARAdmin(@ModelAttribute("propertyStatementStatusBean") PropertyStatementStatusBean propertyStatementStatusBean) {
        ModelAndView mv = new ModelAndView();
        propertyStatementAdminDAO.savePropertyStatementtatusDetailByPARAdmin(propertyStatementStatusBean);
        ArrayList propertyStatusDetail = propertyStatementAdminDAO.getPropertyStatementtatusDetailByPARAdmin();
        mv.addObject("propertyStatusDetail", propertyStatusDetail);
        mv.setViewName("/propertystatement/PropertyStatusChangeList");
        return mv;
    }

    @RequestMapping(value = "propertyStatementStatusManageByPARAdmin.htm", params = {"action=Get Change List"}, method = RequestMethod.POST)
    public ModelAndView getlistpropertyStatementStatusManageByPARAdmin(@ModelAttribute("propertyStatementStatusBean") PropertyStatementStatusBean propertyStatementStatusBean) {
        ModelAndView mv = new ModelAndView();
        ArrayList propertyStatusDetail = propertyStatementAdminDAO.getPropertyStatementtatusDetailByPARAdmin();
        mv.addObject("propertyStatusDetail", propertyStatusDetail);
        mv.setViewName("/propertystatement/PropertyStatusChangeList");
        return mv;
    }

    @RequestMapping(value = "downloadFileForChangePropertyStatus")
    public void downloadFileForChangePropertyStatus(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("propertyStatementStatusBean") PropertyStatementStatusBean propertyStatementStatusBean, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        propertyStatementStatusBean = propertyStatementAdminDAO.getAttachedFileforChangePropertyStatusByPARAdmin(propertyStatementStatusBean.getLogId());
        response.setContentType(propertyStatementStatusBean.getFileTypeForpropertyStatus());
        response.setHeader("Content-Disposition", "attachment;filename=" + propertyStatementStatusBean.getOriginalFileNameForpropertyStatus());
        OutputStream out = response.getOutputStream();
        out.write(propertyStatementStatusBean.getFilecontent());
        out.flush();
    }

    @RequestMapping(value = "propertyStatementStatusManageByPARAdmin.htm", params = {"action=Back"}, method = RequestMethod.POST)
    public ModelAndView BackpropertyStatementStatusManageByPARAdmin(@ModelAttribute("propertyStatementStatusBean") PropertyStatementStatusBean propertyStatementStatusBean) {
        ModelAndView mv = new ModelAndView("/propertystatement/PropertyStatusChangeDetail");
        List fiscalyearlistcy = fiscalDAO.getFiscalYearListCYWise();
        mv.addObject("fiscalyearlistcy", fiscalyearlistcy);
        return mv;
    }

}
