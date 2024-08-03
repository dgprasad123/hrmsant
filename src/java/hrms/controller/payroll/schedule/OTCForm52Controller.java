/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.schedule;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.dao.esignBill.EsignBillDAO;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.schedule.OtcFormBean;
import java.io.File;
import java.io.FileOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean","SelectedEmpOffice"})
public class OTCForm52Controller implements ServletContextAware {

    @Autowired
    public ScheduleDAO comonScheduleDao;
    
    @Autowired
    public PayBillDMPDAO paybillDmpDao;
    
    @Autowired
    EsignBillDAO esignBillDao;
    
    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "OTCForm52HTML", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView OTCForm52HTML(@RequestParam("billNo") String billNo) {

        ModelAndView mav = null;
        OtcFormBean otcBean = new OtcFormBean();
        try {
            mav = new ModelAndView();
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            otcBean = comonScheduleDao.getOTCForm52ScheduleDetails(billNo,crb.getAqyear(), crb.getAqmonth());
            mav.addObject("OTC52Header", otcBean);
            mav.setViewName("/payroll/schedule/OTC52");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "ArrOTCForm52HTML", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView ArrOTCForm52HTML(@RequestParam("billNo") int billNo) {

        ModelAndView mav = null;
        OtcFormBean otcBean = new OtcFormBean();
        try {
            mav = new ModelAndView();
            otcBean = comonScheduleDao.getArrOTCForm52ScheduleDetails(billNo);
            mav.addObject("OTC52Header", otcBean);
            mav.setViewName("/payroll/schedule/OTC52");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "OTCForm52PDF", method = {RequestMethod.POST, RequestMethod.GET})
    public void OTCForm52Pdf(HttpServletResponse response, @RequestParam("billNo") String billNo, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice,@RequestParam("slNo") String slNo) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            response.setHeader("Content-Disposition", "attachment; filename=OTC52Schedule_" + billNo + ".pdf"); // OTCForm52
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            OtcFormBean otcBean = comonScheduleDao.getOTCForm52ScheduleDetails(billNo,crb.getAqyear(), crb.getAqmonth());
           PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            /************** Save Unsigned PDF Details ******************************/                        
            //String allowEsign = comonScheduleDao.allowedOfficeEsign(lub.getLoginoffcode());
            String allowEsign = comonScheduleDao.allowedOfficeEsign(selectedEmpOffice);
            
            String[] rowArray = allowEsign.split("\\|");
            String allowEsignStatus = rowArray[0];
            String createpathPDF = "";
            String unsignedPdfFilename = "";
          /* if (allowEsignStatus.equals("Y")) {
             createpathPDF = esignBillDao.GeneratePDFSignedPath(lub.getLoginoffcode(), crb.getAqyear(), crb.getAqmonth(), billNo);
             unsignedPdfFilename = "OTC52Schedule_" + billNo + "_unsigned.pdf";
             PdfWriter.getInstance(document, new FileOutputStream(createpathPDF + File.separator + unsignedPdfFilename));
             document.open();
             }
            if (billNo != null && !billNo.equals("") && allowEsignStatus.equals("Y")) {
                 esignBillDao.savepdflog(unsignedPdfFilename, createpathPDF, slNo,billNo, crb.getAqmonth(), crb.getAqyear(), lub.getLoginempid(), lub.getLoginoffcode());
             }*/
            /*********************************************************************************************/          

            comonScheduleDao.OtcForm52SchedulePDF(document, billNo, otcBean);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}
