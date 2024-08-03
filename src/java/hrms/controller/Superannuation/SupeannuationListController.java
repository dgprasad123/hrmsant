/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.controller.Superannuation;

import hrms.common.CommonFunctions;
import hrms.dao.Superannuation.SuperannuationDAO;
import hrms.model.SingleBill.SinglePayBillForm;
import hrms.model.Superannuation.SuperannuationList;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Treasury;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author shantanu
 */
@Controller
@SessionAttributes("LoginUserBean")
public class SupeannuationListController implements ServletContextAware{
    
    @Autowired
    SuperannuationDAO supranReportDao;
    
    private ServletContext servletContext;
    
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    
@RequestMapping(value = "superannuationList", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView LoadSuperannuationList(HttpServletRequest request, ModelMap model, @ModelAttribute("TreasuryDtls") Treasury trBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("superannuationList") SuperannuationList sal) throws ParseException {

        ModelAndView mav = null;
        Calendar c = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        int mnth = cal.get(Calendar.MONTH);
        mnth = mnth + 1;
        String month = Integer.toString(mnth);
        System.out.println("Current Month "+month );
        int yr = cal.get(Calendar.YEAR);
        String year = Integer.toString(yr);
        System.out.println("Current Year "+year );
        c.set(Calendar.DAY_OF_MONTH, 1);
        List employeeList = null;
        List billList = null;
        List billYears = null;
        List billMonths = null;
        System.out.println("Get Year: "+ sal.getSupearannuationYear());
        System.out.println("Get Month: "+ sal.getSupearannuationMonth());
        if (sal.getSupearannuationYear() != null && !sal.getSupearannuationYear().equals("")) {
            //Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(sal.getTxtperiodFrom());
            sal.setSupearannuationYear(year);
        } else {
            sal.setSupearannuationYear(year);
        }
        // System.out.println("From Date: "+empQuarterBean.getTxtperiodFrom());
        if (sal.getSupearannuationMonth() != null && !sal.getSupearannuationMonth().equals("")) {
            //Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(sal.getTxtperiodTo());
            sal.getSupearannuationMonth();
        } else {
            
            sal.setSupearannuationMonth(month);
            
        }
        try {
            mav = new ModelAndView();
            System.out.println("Login User Bean: " + lub.getLogindistrictcode());
            System.out.println("From Date"+ sal.getSupearannuationYear());
            System.out.println("To Date"+ sal.getSupearannuationMonth());
            employeeList =  supranReportDao.getSuperannuationList(lub.getLogindistrictcode(), sal.getSupearannuationYear(), sal.getSupearannuationMonth());
            System.out.println("Employee List"+ employeeList);
            mav.addObject("employeeList", employeeList);
            mav.setViewName("/Superannuation/SuperannuationList");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    

}
