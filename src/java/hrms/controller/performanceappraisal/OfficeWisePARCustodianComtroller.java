/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.performanceappraisal;

import hrms.common.CommonFunctions;
import hrms.dao.fiscalyear.FiscalYearDAO;
import hrms.dao.performanceappraisal.PARAdminDAO;
import hrms.dao.performanceappraisal.PARBrowserDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Module;
import hrms.model.parmast.ParAdminProperties;
import hrms.model.parmast.ParAdminSearchCriteria;
import hrms.model.parmast.ParDetail;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manisha
 */
@Controller
@SessionAttributes({"LoginUserBean"})
public class OfficeWisePARCustodianComtroller {

    @Autowired
    FiscalYearDAO fiscalDAO;
    
    @Autowired
    PARAdminDAO parAdminDAO;
    
    @Autowired
    public PARBrowserDAO parbrowserDao;

    @RequestMapping(value = "OfficeWiseParCustodian.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView OfficeWiseParCustodian(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminBean) {
        ModelAndView mv = new ModelAndView();
        //if (lub.getLoginusertype().equals("G")) {
        //if (CommonFunctions.hasPrivileage(privileges, "OfficeWiseParCustodian.htm") == true) {
        List fiscyear = fiscalDAO.getFiscalYearList();
        String fyVal = parAdminBean.getFiscalyear();
        mv.setViewName("/par/PARCustdianOfficeWise");
        mv.addObject("FiscYear", fiscyear);
        mv.addObject("FyYear", fyVal);

        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "getSearchOfficeWiseParList.htm")
    public void getSearchOfficeWiseParList(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            HttpServletResponse response, @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminSearchCriteria) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
            List fiscyear = fiscalDAO.getFiscalYearList();
            String fyVal = parAdminSearchCriteria.getFiscalyear();
            List parList = parAdminDAO.getOfficeWiseParListForCustodian(fyVal, lub.getLoginoffcode(), lub.getLogindistrictcode(), parAdminSearchCriteria.getSearchCriteria(), parAdminSearchCriteria.getSearchString());
            System.out.println("lub.getLoginoffcode() Loginoffcode------" +lub.getLoginoffcode());
            JSONArray obj = new JSONArray(parList);
            out = response.getWriter();
            out.write(obj.toString());
            out.flush();
            out.close();
     }
    @RequestMapping(value = "officeWiseParDataCustodianView.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView officeWiseParDataCustodianView(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("ParAdminProperties") ParAdminProperties ParAdminProperties, @RequestParam("empId") String empId,
            @RequestParam("fiscalyear") String fiscalYear, @RequestParam("encParId") String parIdEnc, @ModelAttribute("parDetail") ParDetail parDetail) {

        ModelAndView mv = new ModelAndView("/par/SiParCustodianView");
        mv.addObject("Fyear", fiscalYear);
        //String parViewPrivilige = parAdminDAO.checkParViewPrivilege(lub.getLoginspc());
        parIdEnc = CommonFunctions.decodedTxt(parIdEnc);
        List pardetail = parAdminDAO.getofficeWiseParDetails(empId, fiscalYear, parIdEnc);
        int taskid = parbrowserDao.getTaskid(lub.getLoginempid(), ParAdminProperties.getParId());
        String[] revertreason = parbrowserDao.getRevertReason(ParAdminProperties.getParId(), taskid);

        mv.addObject("pardetail", pardetail);
        //mv.addObject("PrivType", parViewPrivilige);
        mv.addObject("usertype", lub.getLoginusertype());
        mv.addObject("authorityType", revertreason[0]);
        mv.addObject("revertRemarks", revertreason[1]);
        mv.addObject("authorityName", revertreason[2]);
        mv.setViewName("/par/parCustodianOfficeWiseView"); //ParViewerSiDetails
        return mv;
    }

}
