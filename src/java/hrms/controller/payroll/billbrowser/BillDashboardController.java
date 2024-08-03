/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.billbrowser;

import hrms.dao.payroll.billmast.BillMastDAOImpl;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manoj PC
 */
@Controller
public class BillDashboardController implements ServletContextAware {

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    @Autowired
    BillMastDAOImpl billMastDAO;

    @RequestMapping(value = "BillStatusDashboard", method = RequestMethod.GET)
    public ModelAndView billBrowserAction(HttpServletResponse response) {//       
        String path = "/payroll/BillStatusDashboard";
        ModelAndView mav = new ModelAndView(path);
        ArrayList li = new ArrayList();
        ArrayList li1 = new ArrayList();
        li = billMastDAO.getBillStatusCount();
        li1 = billMastDAO.getTreasuryBillStatusCount();
        
        mav.addObject("totalUnderProcess", li.get(0));
        mav.addObject("totalUnderProcessLess3", li.get(1));
        mav.addObject("totalUnderProcessLess30", li.get(2));
        mav.addObject("totalUnderProcessGreat30", li.get(3));

        mav.addObject("trTotalUnderProcess", li1.get(0));
        mav.addObject("trTotalUnderProcessLess3", li1.get(1));
        mav.addObject("trTotalUnderProcessLess30", li1.get(2));
        mav.addObject("trTotalUnderProcessGreat30", li1.get(3));
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "BillStatusDashboardDetail", method = RequestMethod.GET)
    public ModelAndView BillStatusDshboardDetail(HttpServletResponse response, @RequestParam Map<String, String> requestParams) {//       
        String path = "/payroll/BillStatusDashboardDetail";
        ModelAndView mav = new ModelAndView(path);
        String type = requestParams.get("type");
        ArrayList li = billMastDAO.getBillStatusDetail(type);
        mav.addObject("billListDetails", li);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "BillTreasuryDashboardDetail", method = RequestMethod.GET)
    public ModelAndView BillTreasuryDashboardDetail(HttpServletResponse response, @RequestParam Map<String, String> requestParams) {//       
        String path = "/payroll/BillTreasuryDashboardDetail";
        ModelAndView mav = new ModelAndView(path);
        String type = requestParams.get("type");
        ArrayList li = billMastDAO.BillTreasuryDashboardDetail(type);
        mav.addObject("billListDetails", li);
        mav.setViewName(path);
        return mav;
    }
}
