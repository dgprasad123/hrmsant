/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.NpsData;

import hrms.common.CalendarCommonMethods;
import hrms.common.CommonFunctions;
import hrms.dao.Npsdata.npsDataDAO;
import hrms.dao.master.TreasuryDAO;
import hrms.model.NpsData.NpsDataForm;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Madhusmita
 */
@Controller
@SessionAttributes({"LoginUserBean"})
public class NpsDataExcelController {

    @Autowired
    public npsDataDAO npsdateDao;
    @Autowired
    public TreasuryDAO treasuryDao;

    @RequestMapping(value = "npsDataExcelPage.htm")
    public ModelAndView getNpsDataExcel(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("npsDataForm") NpsDataForm npsform) throws IOException {
        ModelAndView mv = null;
        try {

            mv = new ModelAndView("/NpsData/npsdataexcel", "npsDataForm", npsform);
            ArrayList treasuryList = npsdateDao.getTreasuryList();
            mv.addObject("treasurylist", treasuryList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "npsdataexcel.htm")
    public void downloadNPSDataExcel(ModelMap model, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("npsDataForm") NpsDataForm npsform) {
        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;
        try {
            int trmonth = Integer.parseInt(npsform.getSltMonth());
            //String treasury_name=treasuryDao.getTreasuryName(npsform.getTrCode());
            String monthName = CalendarCommonMethods.getMonthAsString(trmonth);            

            String fileName = "nps_" + npsform.getTrCode() + "_" + "billType_" + npsform.getBilltype() + "_" + monthName + "_" + npsform.getSltYear() + ".xls";
            out = new BufferedOutputStream(response.getOutputStream());

            WritableWorkbook workbook = Workbook.createWorkbook(out);
            npsdateDao.downloadNpsData(out, fileName, workbook, npsform);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    @RequestMapping(value = "nonnpsExcelPage.htm")
    public ModelAndView getnonNpsDataExcel(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("nonnpsDataForm") NpsDataForm nonnpsform) throws IOException {
        ModelAndView mv = null;
        try {

            mv = new ModelAndView("/NpsData/nonNpsdataexcelpage", "nonnpsDataForm", nonnpsform);
            ArrayList treasuryList = npsdateDao.getTreasuryList();
            mv.addObject("treasurylist", treasuryList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }
    
    @RequestMapping(value = "nonNpsdataexcelpage.htm")
    public void downloadnonNPSDataExcel(ModelMap model, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("npsDataForm") NpsDataForm nonnpsform) {
        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;
        try {
            int trmonth = Integer.parseInt(nonnpsform.getSltMonth());
            //String treasury_name=treasuryDao.getTreasuryName(npsform.getTrCode());
            String monthName = CalendarCommonMethods.getMonthAsString(trmonth);            

            String fileName = "nonnps_" + nonnpsform.getTrCode() + "_" + "billType_" + nonnpsform.getBilltype() + "_" + monthName + "_" + nonnpsform.getSltYear() + ".xls";
            out = new BufferedOutputStream(response.getOutputStream());

            WritableWorkbook workbook = Workbook.createWorkbook(out);
            npsdateDao.downloadnonNpsData(out, fileName, workbook, nonnpsform);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
