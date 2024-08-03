package hrms.controller.SingleBill;

import hrms.SelectOption;
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
import java.util.ArrayList;
import java.util.Calendar;
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

@Controller
@SessionAttributes("LoginUserBean")
public class SinglePayBillAGController {
    
    @Autowired
    public SinglePayBillDAO singlePayDao;

    @Autowired
    PayBillDMPDAO paybillDmpDao;

    @Autowired
    BillBrowserDAO billBrowserDao;

    @Autowired
    ScheduleDAO comonScheduleDao;
    
    @RequestMapping(value = "getAGWiseBillDetails")
    public ModelAndView getAGWiseBillDetails(@ModelAttribute("singleFormBean") SinglePayBillForm singleBean) {

        ModelAndView mav = null;
        ArrayList yearlist = new ArrayList();
        try {
            mav = new ModelAndView("/SingleBill/SingleBillAGWise", "singleFormBean", singleBean);

            List agwisetrlist = singlePayDao.getAGWiseTreasuryCodeList();

            SelectOption so = null;
            int curyear = 0;
            Calendar cal = Calendar.getInstance();
            curyear = cal.get(Calendar.YEAR);
            for (int i = curyear; i > curyear - 3; i--) {
                so = new SelectOption();
                so.setLabel(i + "");
                so.setValue(i + "");
                yearlist.add(so);
            }

            mav.addObject("agwisetrlist", agwisetrlist);
            mav.addObject("yearlist", yearlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "getAGWiseBillDetails",params="btnSubmit=Submit")
    public ModelAndView getAGWiseBillDetailsList(@ModelAttribute("singleFormBean") SinglePayBillForm singleBean) {

        ModelAndView mav = null;
        
        ArrayList yearlist = new ArrayList();
        List billList = null;
        try {
            mav = new ModelAndView("/SingleBill/SingleBillAGWise", "singleFormBean", singleBean);
            
            List agwisetrlist = singlePayDao.getAGWiseTreasuryCodeList();
            List majorheadlist = billBrowserDao.getMajorHeadListTreasuryWise(singleBean.getTreasury(), Integer.parseInt(singleBean.getSltYear()), Integer.parseInt(singleBean.getSltMonth()));

            SelectOption so = null;
            int curyear = 0;
            Calendar cal = Calendar.getInstance();
            curyear = cal.get(Calendar.YEAR);
            for (int i = curyear; i > curyear - 3; i--) {
                so = new SelectOption();
                so.setLabel(i + "");
                so.setValue(i + "");
                yearlist.add(so);
            }
            
            billList = singlePayDao.getAGWiseBillList(singleBean.getTreasury(), singleBean.getMajorhead(), singleBean.getSltYear(), singleBean.getSltMonth());
            
            mav.addObject("BillDataList", billList);
            
            mav.addObject("agwisetrlist", agwisetrlist);
            mav.addObject("majorheadlist", majorheadlist);
            mav.addObject("yearlist", yearlist);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
      return mav;
    }
    
    @RequestMapping(value = "singleBillReportAction")
    public String singleBillReportAction(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result,
            HttpServletResponse response, @RequestParam Map<String, String> requestParams) {

        String path = "/payroll/PayBillReport";
        List reportList = null;
        String billNo = "";
        String billType = "";

        if (requestParams.get("billNo") != null && requestParams.get("billType") != null) {
            billNo = requestParams.get("billNo");
            billType = requestParams.get("billType");
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);

            reportList = comonScheduleDao.getDisplayReportList(billNo, billType, crb.getRequiredReports(),crb.getAqyear(),crb.getAqmonth());
            
        }
        model.addAttribute("reportList", reportList);
        return path;
    }
}
