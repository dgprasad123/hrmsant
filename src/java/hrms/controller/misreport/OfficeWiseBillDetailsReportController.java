/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.controller.misreport;


import hrms.common.CommonFunctions;
import hrms.dao.payroll.billmast.BillMastDAO;
import java.util.ArrayList;
import java.util.Calendar;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class OfficeWiseBillDetailsReportController {
    
    @Autowired
    BillMastDAO billMastDAO;

   @RequestMapping(value = "DownloadOfficeWiseBillDetails")
    public ModelAndView getOfficeWiseBillStatus(ModelMap model, HttpServletResponse response, @RequestParam("year") String year, @RequestParam("month") String month, @RequestParam("ocode") String ocode)
    {
        String path = "/misreport/DownloadOfficeWiseBillDetails";
        ModelAndView mav = new ModelAndView();
        mav.addObject("selectmonth", month);
        mav.addObject("selectyear", year);
        String dYear = CommonFunctions.decodedTxt(year);
        String dMonth = CommonFunctions.decodedTxt(month);
        String dOcode = CommonFunctions.decodedTxt(ocode);
        ArrayList billListDetails = billMastDAO.getOfficeWiseBillStatus(Integer.parseInt(dMonth), Integer.parseInt(dYear), dOcode);
        mav.addObject("billListDetails", billListDetails);
        mav.setViewName(path);    
        return mav;        
    }
    
}
