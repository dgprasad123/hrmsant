/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.controller.SingleBill;

import hrms.dao.SingleBill.SinglePayBillDAO;
import hrms.dao.payroll.billbrowser.BillBrowserDAO;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.SingleBill.SinglePayBillForm;
import hrms.model.common.CommonReportParamBean;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Treasury;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
 * @author prashant
 */
@Controller
@SessionAttributes({"TreasuryDtls", "LoginUserBean","SelectedEmpOffice"})
public class SinglePayBillController implements ServletContextAware{
    
    @Autowired
    public SinglePayBillDAO singlePayDao;
    
    @Autowired
    PayBillDMPDAO paybillDmpDao;
    
    @Autowired
    BillBrowserDAO billBrowserDao;
    
    @Autowired
    ScheduleDAO comonScheduleDao;
    
    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }
    
    @ResponseBody
    @RequestMapping(value = "getBillYearDdoWise", method = RequestMethod.POST)
    public void getBillYearDdoWise(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, 
            @ModelAttribute("singleFormBean") SinglePayBillForm singleBean, HttpServletResponse response) throws IOException {
        
        response.setContentType("application/json");
        PrintWriter out = null;
        List billYears = billBrowserDao.getSinglePageBillPrepareYear(singleBean.getDdoName());
        JSONArray json = new JSONArray(billYears);
        out = response.getWriter();
        out.write(json.toString());
    }
    
    @ResponseBody
    @RequestMapping(value = "getSBillMonthYearWise", method = RequestMethod.POST)
    public void getBillMonthYearWise(HttpServletRequest request, @ModelAttribute("singleFormBean") SinglePayBillForm singleBean, 
            @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        
        List billMonths = null;
        response.setContentType("application/json");
        PrintWriter out = null;
        if(singleBean.getSltYear() != null && !singleBean.getSltYear().equals("")){
            billMonths = billBrowserDao.getSinglePageMonthFromSelectedYear(singleBean.getDdoName(), Integer.parseInt(singleBean.getSltYear()) ,singleBean.getBillType());
        }
        
        JSONArray json = new JSONArray(billMonths);
        out = response.getWriter();
        out.write(json.toString());
    }
    
    @RequestMapping(value = "showDdoDetails", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView LoadDdoDetailsData(@ModelAttribute("TreasuryDtls") Treasury trBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("singleFormBean") SinglePayBillForm singleBean) {

        ModelAndView mav = null;
        List ddoDataList = null;
        List billList = null;
        List billYears = null;
        List billMonths = null;
        
        try {
            mav = new ModelAndView();
            if(lub.getLoginusertype().equals("D")){
                ddoDataList = singlePayDao.getOnlineBillSubmittedDDOs(lub.getLogindistrictcode(),lub.getLoginusertype());
            }else{
                ddoDataList = singlePayDao.getOnlineBillSubmittedDDOs(trBean.getTreasuryCode(),lub.getLoginusertype());
            }
            
            billYears = billBrowserDao.getSinglePageBillPrepareYear(singleBean.getDdoName());
            
            if(singleBean.getBillType() != null && singleBean.getDdoName() != null && singleBean.getSltYear() != null && singleBean.getSltMonth() != null){

                billMonths = billBrowserDao.getSinglePageMonthFromSelectedYear(singleBean.getDdoName(), Integer.parseInt(singleBean.getSltYear()) ,singleBean.getBillType());
                billList = singlePayDao.getDdoWiseBillList(singleBean.getBillType(), singleBean.getDdoName(), singleBean.getSltYear(), singleBean.getSltMonth(),lub.getLoginusertype(),singleBean.getArrearType());
            }
            
            mav.addObject("BillDataList", billList);
            mav.addObject("DDOList", ddoDataList);
            mav.addObject("YearList", billYears);
            mav.addObject("MonthList", billMonths);
            mav.setViewName("/SingleBill/SinglePaySlip");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "singleBillReportAction", method = RequestMethod.GET)
    public String singleBillReportAction(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, 
            HttpServletResponse response, @RequestParam Map<String,String> requestParams) {
        
        String path = "/payroll/SinglePayBillReport";
        List reportList = null;
        String billNo = "";
        String billType = "";
        
        if(requestParams.get("billNo") != null && requestParams.get("billType") != null){   
            billNo = requestParams.get("billNo");
            billType = requestParams.get("billType");
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            
            reportList = comonScheduleDao.getDisplayReportList(billNo, billType, crb.getRequiredReports(),crb.getAqyear(),crb.getAqmonth());
            
            //String allowEsign = comonScheduleDao.allowedOfficeEsign(crb.getOffcode());
            
            String allowEsign = comonScheduleDao.allowedOfficeEsign(lub.getLoginoffcode());
            
            String[] rowArray = allowEsign.split("\\|");
            String allowEsignStatus = rowArray[0];
            model.addAttribute("allowEsign", "Y");
        }
        model.addAttribute("reportList", reportList);
        return path;
    }
    
    
}
