/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.DistrictWiseEsignDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.DistrictWiseEsignBean;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Surendra
 */
@SessionAttributes({"LoginUserBean"})

@Controller
public class DistrictWiseEsignController {

    @Autowired
    public DistrictWiseEsignDAO districtWiseEsignDAO;

    @RequestMapping(value = "DistrictWiseEsignList")
    public ModelAndView DistrictWiseEsignList(@ModelAttribute("LoginUserBean") LoginUserBean lub, ModelMap model, HttpServletResponse response) {
        String path = "/master/DistrictWiseEsignList";
        ModelAndView mav = new ModelAndView();
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        mav.addObject("selectmonth", month);
        mav.addObject("selectyear", year);
        ArrayList esignList = districtWiseEsignDAO.getEsignList(lub.getLogindistrictcode(), month, year);

        mav.addObject("esignList", esignList);
        // return "/misreport/DeptWisePayBillReport";
        mav.setViewName(path);
        return mav;

    }

    @RequestMapping(value = "getDistrictWiseEsignList", method = RequestMethod.GET)
    public ModelAndView getDistWisePayBillReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, ModelMap model, HttpServletResponse response, @RequestParam("year") int year, @RequestParam("month") int month) {
        ModelAndView mv = new ModelAndView();

        Calendar now = Calendar.getInstance();

        mv.addObject("selectmonth", month);
        mv.addObject("selectyear", year);
        ArrayList esignList = districtWiseEsignDAO.getEsignList(lub.getLogindistrictcode(), month, year);

        mv.addObject("esignList", esignList);
        mv.setViewName("master/DistrictWiseEsignList");
        return mv;
    }

    @RequestMapping(value = "deleteEsignBill", method = RequestMethod.GET)
    public ModelAndView DeleteEsignBill(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("districtWiseEsignBean") DistrictWiseEsignBean districtWiseEsignBean) {
        ModelAndView mvc = new ModelAndView();

        mvc.setViewName("/master/DeleteEsignBill");
        return mvc;
    }

    @RequestMapping(value = "searchEsignBill", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView serachEsignBill(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("districtWiseEsignBean") DistrictWiseEsignBean districtWiseEsignBean) {
        ModelAndView mvc = new ModelAndView();
        List esignBillList = new ArrayList();
        esignBillList = districtWiseEsignDAO.searchEsignbillDelete(districtWiseEsignBean);
        mvc.addObject("esignBillListItem", esignBillList);
        mvc.addObject("districtWiseEsignBean", districtWiseEsignBean);
        mvc.setViewName("/master/DeleteEsignBill");

        return mvc;
    }

    @RequestMapping(value = "deleteEsignbill.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView deleteEsignBillDetails(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("billId") String billId,
            @RequestParam("esignLogId") String esignLogId,
            @ModelAttribute("districtWiseEsignBean") DistrictWiseEsignBean districtWiseEsignBean) {
        ModelAndView mav = new ModelAndView();
        //String newpdfFileName = null;
        //String signedfilepath = null;
        String logindistcode = null;

        if (lub.getLoginusername() != null && lub.getLoginusername().contains("hrmsupport")) {
            logindistcode = "2117";
        } else {
            logindistcode = lub.getLogindistrictcode();
        }

        districtWiseEsignBean = districtWiseEsignDAO.getEsignbill(Integer.parseInt(billId), Integer.parseInt(esignLogId));

        if (logindistcode != null && logindistcode.equals(districtWiseEsignBean.getDistcode())) {
            /* if ((districtWiseEsignBean.getSigned_pdf_path() != null && !districtWiseEsignBean.getSigned_pdf_path().equals(""))
             && (districtWiseEsignBean.getSigned_pdf_file() != null && !districtWiseEsignBean.getSigned_pdf_file().equals(""))) {
             System.out.println("renamed");
             signedfilepath = districtWiseEsignBean.getSigned_pdf_path();
             newpdfFileName = districtWiseEsignDAO.esignFileRename(districtWiseEsignBean, signedfilepath);
             if (newpdfFileName != null && !newpdfFileName.equals("")) {
             int resultQry = districtWiseEsignDAO.updateEsigndetails(districtWiseEsignBean, newpdfFileName);
             System.out.println("resultQry:" + resultQry);
             if (resultQry > 0) {
             //districtWiseEsignDAO.deleteEsignBill(Integer.parseInt(billId), Integer.parseInt(esignLogId));
             }
             }
             } else {*/           
            
            districtWiseEsignDAO.deleteEsignBill(Integer.parseInt(billId), Integer.parseInt(esignLogId));
            mav.setViewName("/master/DeleteEsignBill");
        } else {
            mav.setViewName("/under_const");
        }
        return mav;
    }

}
