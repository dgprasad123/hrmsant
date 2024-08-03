package hrms.controller.payroll.schedule;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.dao.esignBill.EsignBillDAO;
import hrms.dao.payroll.billbrowser.BillBrowserDAO;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.login.LoginUserBean;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("LoginUserBean")
public class BeneficiaryListController {

    @Autowired
    public ScheduleDAO comonScheduleDao;

    @Autowired
    public PayBillDMPDAO paybillDmpDao;

    @Autowired
    BillBrowserDAO billBrowserDao;

    @Autowired
    EsignBillDAO esignBillDao;

    @RequestMapping(value = "BeneficiaryListDownloadPDF")
    public void BeneficiaryListDownloadPDF(HttpServletResponse response, @RequestParam("billNo") String billNo, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("slNo") String slNo) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        boolean isDDODHe = false;
        CommonReportParamBean crb = null;

        PdfWriter writer = null;
        try {
            response.setHeader("Content-Disposition", "attachment; filename=BeneficiaryList_" + billNo + ".pdf");

            writer = PdfWriter.getInstance(document, response.getOutputStream());

            document.open();

            isDDODHe = billBrowserDao.isDDODHE(lub.getLoginoffcode());
            if (isDDODHe) {
                crb = paybillDmpDao.getCommonReportParameterDHE(billNo);
            } else {
                crb = paybillDmpDao.getCommonReportParameter(billNo);
            }

            ArrayList emplist = billBrowserDao.getBeneficiaryList(Integer.parseInt(billNo), crb.getTypeofBill());
            double netAmt = billBrowserDao.getBillGrossAndNet(Integer.parseInt(billNo));
            double grossAmt = billBrowserDao.getbillbenificiaryGross(Integer.parseInt(billNo));
            //crb.setBillGrossAmt((int)grossAmt);
            
           
            

            /**
             * ************ Save Unsigned PDF Details *****************************
             */
            // CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);            
           /* String allowEsign = comonScheduleDao.allowedOfficeEsign(lub.getLoginoffcode());
            String[] rowArray = allowEsign.split("\\|");
            String allowEsignStatus = rowArray[0];
            String createpathPDF = "";
            String unsignedPdfFilename = "";
            if (allowEsignStatus.equals("Y")) {
                createpathPDF = esignBillDao.GeneratePDFSignedPath(lub.getLoginoffcode(), crb.getAqyear(), crb.getAqmonth(), billNo);
                unsignedPdfFilename = "BeneficiaryList_" + billNo + "_unsigned.pdf";
                PdfWriter.getInstance(document, new FileOutputStream(createpathPDF + File.separator + unsignedPdfFilename));
                document.open();
            }
            if (billNo != null && !billNo.equals("") && allowEsignStatus.equals("Y")) {
                esignBillDao.savepdflog(unsignedPdfFilename, createpathPDF, slNo, billNo, crb.getAqmonth(), crb.getAqyear(), lub.getLoginempid(), lub.getLoginoffcode());
            }*/
            /**
             * **********************************************************************
             */

            comonScheduleDao.generateBeneficiaryListPDF(document, crb, billNo, emplist, netAmt);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "BeneficiaryListHTML")
    public String BeneficiaryListHTML(Model model, @RequestParam("billNo") String billNo) {

        try {
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);

            ArrayList emplist = comonScheduleDao.getBillWiseEmployeeList(billNo, crb.getTypeofBill());

            double netAmt = (crb.getBillGrossAmt() - (crb.getBillDedAmt() + crb.getBillPvtDedAmt()));
            model.addAttribute("netAmt", netAmt);

            model.addAttribute("crb", crb);
            model.addAttribute("emplist", emplist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/payroll/schedule/BeneficiaryList";
    }
}
