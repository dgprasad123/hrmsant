/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.IncomeTax;

import hrms.dao.IncomeTax.IncomeTaxDAO;
import hrms.model.IncomeTax.AnnexureIBean;
import hrms.model.IncomeTax.IT24QBean;
import hrms.model.IncomeTax.IncomeTaxBean;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Treasury;
import hrms.model.IncomeTax.DeducteeBean;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manoj PC
 */
@Controller
@SessionAttributes({"TreasuryDtls", "LoginUserBean"})
public class IncomeTaxController {

    @Autowired
    public IncomeTaxDAO IncomeTaxDAO;

    @RequestMapping(value = "EmployeeWiseIT")
    public ModelAndView getLTAList(ModelMap model, @ModelAttribute("TreasuryDtls") Treasury trBean, @ModelAttribute("EmployeeWiseIT") IncomeTaxBean IT) {
        ModelAndView mv = new ModelAndView();
        List years = IncomeTaxDAO.getYears();
        String year = IT.getSltYear();
        String month = IT.getSltMonth();
        String trCode = trBean.getTreasuryCode();
        if (year != null && !year.equals("")) {
            List li = IncomeTaxDAO.getEmployeeWiseIT(Integer.parseInt(year), Integer.parseInt(month), trCode);
            mv.addObject("ddoList", li);
        }
        mv.addObject("YearList", years);

        String path = "/IncomeTax/EmployeeWiseIT";
        mv.setViewName(path);
        return mv;
    }

    @RequestMapping(value = "DDOEmployeeWiseIT")
    public ModelAndView getDDOEmpWiseIT(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmployeeWiseIT") IncomeTaxBean IT) {
        ModelAndView mv = new ModelAndView();
        List years = IncomeTaxDAO.getYears();
        String year = IT.getSltYear();
        String month = IT.getSltMonth();
        String offCode = lub.getLoginoffcode();
        if (year != null && !year.equals("")) {
            List li = IncomeTaxDAO.getDDOEmployeeWiseIT(Integer.parseInt(year), Integer.parseInt(month), offCode);
            
            mv.addObject("ddoList", li);
        }
        mv.addObject("YearList", years);

        String path = "/IncomeTax/DDOEmployeeWiseIT";
        mv.setViewName(path);
        return mv;
    }

    @RequestMapping(value = "IT24Q")
    public ModelAndView getIT24Q(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams
    ) {
        ModelAndView mv = new ModelAndView();
        String fiscalYear = IncomeTaxDAO.getCurFiscalYear();
        String finYear = requestParams.get("finYear");
        String finQuarter = requestParams.get("finQuarter");
        IT24QBean IB = IncomeTaxDAO.get24Q(lub.getLoginoffcode(), finYear, finQuarter);
        String path = "/IncomeTax/IT24Q";
        mv.addObject("fiscalYear", fiscalYear);
        mv.addObject("IB", IB);
        mv.setViewName(path);
        return mv;
    }

    @RequestMapping(value = "Annexure-I")
    public ModelAndView getAnnexureI(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub
    ) {
        ModelAndView mv = new ModelAndView();
        
        AnnexureIBean AI = IncomeTaxDAO.getAnnexure(lub.getLoginoffcode());
        String path = "/IncomeTax/Annexure-I";
        String fiscalYear = IncomeTaxDAO.getCurFiscalYear();
        mv.setViewName(path);
        mv.addObject("fiscalYear", fiscalYear);
        mv.addObject("ai", AI);
        return mv;
    }

    @RequestMapping(value = "ImportBinExcel")
    public ModelAndView ImportBinExcel(@ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result) throws IOException, BiffException {
        ModelAndView mv = new ModelAndView();
        String path = null;
        Workbook workbook = null;

        try {

            InputStream inputStream = null;
            OutputStream outputStream = null;
            List li = new ArrayList();
            File documentFile = new File("E:\\hrms\\BINFile\\BIN_Report_May.xls");
            inputStream = new FileInputStream(documentFile);
            workbook = Workbook.getWorkbook(inputStream);
            
            IncomeTaxDAO.addExcelRowIntoDB(workbook, li, lub.getLoginoffcode());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {

            if (workbook != null) {
                workbook.close();
            }

        }

        //path = "redirect:/ImportSuccess.htm?month=" + eBean.getAmonth() + "&year=" + eBean.getAyear();
        mv.setViewName("/IncomeTax/ImportBinExcel");
        return mv;

    }

    @RequestMapping(value = "Main24Q")
    public ModelAndView getMain24Q(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams
    ) {
        ModelAndView mv = new ModelAndView();
        String fiscalYear = IncomeTaxDAO.getCurFiscalYear();
        String finYear = requestParams.get("finYear");
        String finQuarter = requestParams.get("finQuarter");
        IT24QBean IB = IncomeTaxDAO.get24Q(lub.getLoginoffcode(), finYear, finQuarter);
        String path = "/IncomeTax/Main24Q";
        mv.addObject("fiscalYear", fiscalYear);
        mv.addObject("IB", IB);
        mv.setViewName(path);
        return mv;
    }

    @RequestMapping(value = "Challan")
    public ModelAndView getChallan(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams
    ) {
        ModelAndView mv = new ModelAndView();
        String fiscalYear = IncomeTaxDAO.getCurFiscalYear();
        String finYear = requestParams.get("finYear");
        String finQuarter = requestParams.get("finQuarter");
        IT24QBean IB = IncomeTaxDAO.getChallanDetails(lub.getLoginoffcode(), finYear, finQuarter, "24Q");
        String path = "/IncomeTax/Challan";
        mv.addObject("fiscalYear", fiscalYear);
        mv.addObject("IB", IB);
        mv.setViewName(path);
        return mv;
    }

    @RequestMapping(value = "Annexure-I-24Q")
    public ModelAndView getAnnexureI24Q(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams
    ) {
        ModelAndView mv = new ModelAndView();
        
        AnnexureIBean AI = IncomeTaxDAO.getAnnexure(lub.getLoginoffcode());
        String path = "/IncomeTax/Annexure-I-24Q";
        String fiscalYear = IncomeTaxDAO.getCurFiscalYear();
        mv.setViewName(path);
        mv.addObject("fiscalYear", fiscalYear);
        mv.addObject("ai", AI);
        return mv;
    }

    @RequestMapping(value = "Challan26Q")
    public ModelAndView Challan26Q(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams
    ) {
        ModelAndView mv = new ModelAndView();
        String fiscalYear = IncomeTaxDAO.getCurFiscalYear();
        String finYear = requestParams.get("finYear");
        String finQuarter = requestParams.get("finQuarter");
        IT24QBean IB = IncomeTaxDAO.getChallanDetails(lub.getLoginoffcode(), finYear, finQuarter, "26Q");
        String path = "/IncomeTax/Challan26Q";
        mv.addObject("fiscalYear", fiscalYear);
        mv.addObject("IB", IB);
        mv.setViewName(path);
        return mv;
    }

    @RequestMapping(value = "Annexure-I-26Q")
    public ModelAndView getAnnexureI26Q(ModelMap model
            , @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams
            , @ModelAttribute("DeducteeBean") DeducteeBean dBean
    ) {
        ModelAndView mv = new ModelAndView();
        String financialYear = requestParams.get("fYear");
        financialYear = "2019-20";
        String quarter = requestParams.get("qtr");
        quarter = "1";
        String month = requestParams.get("month");
        DeducteeBean db = null;
        
        //AnnexureIBean AI = IncomeTaxDAO.getAnnexure(lub.getLoginoffcode());
        int totalTDSAmount = 0;
        String path = "/IncomeTax/Annexure-I-26Q";
        String fiscalYear = IncomeTaxDAO.getCurFiscalYear();
        totalTDSAmount = IncomeTaxDAO.getTotal26QAmount(financialYear, quarter, month, lub.getLoginoffcode());
        ArrayList li = IncomeTaxDAO.get26QList(lub.getLoginoffcode(), financialYear, quarter, month);
        
        int startIndex = li.size()+1;
        int totalAmount = 0;
        for(int i = 0; i < (startIndex-1); i++)
        {
            db = (DeducteeBean)li.get(i);
            totalAmount+= Integer.parseInt(db.getAmount());
        }
        mv.setViewName(path);
        mv.addObject("fiscalYear", fiscalYear);
        mv.addObject("month", month);
        mv.addObject("li", li);
        mv.addObject("financialYear", "2019-20");
        mv.addObject("totalTDSAmount", totalTDSAmount);
        
        mv.addObject("startIdx", startIndex);
        mv.addObject("totalAmount", totalAmount);
        return mv;
    }
@RequestMapping(value = "SaveDeducteeDetail")
    public ModelAndView SaveDeducteeDetail(ModelMap model
            , @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams
            , @ModelAttribute("DeducteeBean") DeducteeBean dBean
    ) {
        ModelAndView mv = null;      
        mv = new ModelAndView("redirect:/Annexure-I-26Q.htm?month="+dBean.getMonth());
        IncomeTaxDAO.SaveDeducteeDetail(dBean, lub.getLoginempid(), lub.getLoginspc(), lub.getLoginoffcode());
        return mv;
    }    
@RequestMapping(value = "DeleteDeductee")
    public ModelAndView DeleteDeductee(ModelMap model
            , @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams
            , @ModelAttribute("DeducteeBean") DeducteeBean dBean
    ) {
        String deducteeId = requestParams.get("deducteeId");
        ModelAndView mv = null;      
        mv = new ModelAndView("redirect:/Annexure-I-26Q.htm?month="+dBean.getMonth());
        IncomeTaxDAO.deleteDeductee(deducteeId);
        return mv;
    }      
}
