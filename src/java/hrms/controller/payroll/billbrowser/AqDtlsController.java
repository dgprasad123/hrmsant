/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.billbrowser;

import hrms.dao.payroll.aqdtls.AqDtlsDAO;
import hrms.dao.payroll.billbrowser.AqReportDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.billbrowser.AcquaintanceBean;
import hrms.model.payroll.billbrowser.AqDtlsDedBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("LoginUserBean")
public class AqDtlsController {

    @Autowired
    AqDtlsDAO aqDtlsDAO;
    @Autowired
    AqReportDAO aqReportDao;

    @RequestMapping(value = "getAqDtlsDedAction.htm", method = RequestMethod.GET)
    public ModelAndView getAqDtlsDedData(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("aqslNo") String aqslNo, @RequestParam("adCode") String adCode, @RequestParam("nowDedn") String nowdedn, @RequestParam("dedType") String dedType, @RequestParam("adrefId") String adrefId, @RequestParam("adType") String adType, @RequestParam("billNo") String billNo) {//

        AqDtlsDedBean aqDtlsDed = new AqDtlsDedBean();
        String tableName = aqReportDao.getAqDtlsTableName(billNo);

        aqDtlsDed = aqDtlsDAO.getAqDetailsDed(tableName,aqslNo, adCode, nowdedn, adrefId);
        //aqDtlsDed.setBillNo(billNo);
        aqDtlsDed.setAdCode(adCode);
        aqDtlsDed.setNowDedn(nowdedn);
        aqDtlsDed.setAqslNo(aqslNo);
        aqDtlsDed.setDedType(dedType);
        aqDtlsDed.setAdRefId(adrefId);
        aqDtlsDed.setTotRecAmt(aqDtlsDed.getTotRecAmt());
        aqDtlsDed.setAdType(adType);
        aqDtlsDed.setBillNo(billNo);
        ModelAndView mv = new ModelAndView("/payroll/AqDtlsDedAmt", "command", aqDtlsDed);
        return mv;
    }

    @RequestMapping(value = "getAqDtlsAllAction.htm", method = RequestMethod.GET)
    public ModelAndView getAqDtlsAllData(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("aqslNo") String aqslNo, @RequestParam("adCode") String adCode, @RequestParam("adType") String adType, @RequestParam("billNo") String billNo) {//

        AqDtlsDedBean aqDtlsDed = new AqDtlsDedBean();
        String tableName = aqReportDao.getAqDtlsTableName(billNo);

        aqDtlsDed = aqDtlsDAO.getAqDetailsAllowance(tableName,aqslNo, adCode);
        aqDtlsDed.setAdCode(adCode);
        aqDtlsDed.setAqslNo(aqslNo);
        aqDtlsDed.setAdType(adType);
        aqDtlsDed.setBillNo(billNo);
        ModelAndView mv = new ModelAndView("/payroll/AqDtlsDedAmt", "command", aqDtlsDed);
        return mv;
    }

    @RequestMapping(value = "browserAqData.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView updateAqdtls(@ModelAttribute("AqDtlsDedBean") AqDtlsDedBean aqDtlsDedBean) {
        double gross = 0;
        double net = 0;
        double totAllowance = 0.0;
        double totDeduction = 0.0;
        String updateMessage = "Please enter the Loan details in Loan/LIC Module";
       String tableName = aqReportDao.getAqDtlsTableName(aqDtlsDedBean.getBillNo());
        int n = aqDtlsDAO.updateAqDtlsData(tableName,aqDtlsDedBean);
        ModelAndView mv = new ModelAndView("/payroll/BrowseAquitanceData");
        // ModelAndView mv = new ModelAndView("redirect:/browseAquitanceData.htm?aqslno="+aqDtlsDedBean.getAqslNo()+"&billNo=" + aqDtlsDedBean.getBillNo());
        
        AcquaintanceBean aqReportBean = aqReportDao.getAqMastDtl(aqDtlsDedBean.getAqslNo());
        ArrayList deductionobjList = aqReportDao.getAcquaintanceDtlDed(aqDtlsDedBean.getAqslNo(), tableName);
        ArrayList allowanceObjList = aqReportDao.getAcquaintanceDtlAll(aqDtlsDedBean.getAqslNo(), tableName);
        totAllowance = aqReportDao.getTotalAllowance(aqDtlsDedBean.getAqslNo(),tableName);
        totDeduction = aqReportDao.getTotalDeduction(aqDtlsDedBean.getAqslNo(),tableName);
        gross = totAllowance + aqReportBean.getCurbasic();
        net = gross - totDeduction;
        mv.addObject("totAll", totAllowance);
        mv.addObject("totDed", totDeduction);
        mv.addObject("gross", gross);
        mv.addObject("net", net);
        mv.addObject("aqSlNo", aqDtlsDedBean.getAqslNo());
        mv.addObject("billNo", aqDtlsDedBean.getBillNo());
        mv.addObject("aqReportBean", aqReportBean);
        mv.addObject("deductionobjList", deductionobjList);
        mv.addObject("allowanceObjList", allowanceObjList);
        if (n == 0) {
            mv.addObject("updateMessage", updateMessage);
        }
        return mv;
    }

    @RequestMapping(value = "getAquitanceBasicJSON.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public void getAquitanceBasic(HttpServletResponse response, @RequestParam("aqslNo") String aqslNo) {
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            int basic = aqReportDao.getAquitanceBasic(aqslNo);
            json.put("basic", basic);
            out = response.getWriter();
            out.write(json.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "saveAquitanceBasicJSON")
    public void saveAquitanceBasicJSON(HttpServletResponse response, @RequestParam("aqslNo") String aqslNo, @RequestParam("aqbasic") int aqbasic) {
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            aqReportDao.saveAquitanceBasic(aqslNo, aqbasic);
            json.put("msg", "Saved");
            out = response.getWriter();
            out.write(json.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "deleteBrowseAquitanceData.htm", method = RequestMethod.GET)
    public ModelAndView deleteBrowseAquitanceData(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("aqslno") String aqslno, @RequestParam("billNo") String billNo, HttpServletResponse response) {
        ModelAndView mv = null;
        try {
            aqDtlsDAO.deleteBrowserAqData(aqslno, lub.getLoginoffcode());
            mv = new ModelAndView("redirect:/browseAquitance.htm?billNo=" + billNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "backToBrowserAquitance.htm", method = RequestMethod.GET)
    public ModelAndView backToBrowserAquitance(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("aqslno") String aqslno, @RequestParam("billNo") String billNo, HttpServletResponse response) {
        ModelAndView mv = null;
        try {

            mv = new ModelAndView("redirect:/browseAquitance.htm?billNo=" + billNo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }
}
