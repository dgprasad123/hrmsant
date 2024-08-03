/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.schedule;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.Numtowordconvertion;
import hrms.dao.esignBill.EsignBillDAO;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduePDFEsignDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.schedule.BillContributionRepotBean;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
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
public class BillContributionController implements ServletContextAware {

    @Autowired
    public ScheduleDAO comonScheduleDao;

    @Autowired
    public PayBillDMPDAO paybillDmpDao;
    
     @Autowired
    EsignBillDAO esignBillDao;
     
     @Autowired
    ScheduePDFEsignDAO schedulepdfEsign;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "BillContributionHTML", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView BillContributionHTML(@RequestParam("annexure") String annexure, @RequestParam("billNo") String billNo) {

        ModelAndView mav = null;
        List empDataList = null;
        String cpfTotal = "";
        String gcpfTotal = "";
        String cpfTotalFig = "";
        String gcpfTotalFig = "";
        String gTotal = "";
        BillContributionRepotBean billContBean = new BillContributionRepotBean();

        double arrearAmtTotal = 0;
        try {
            mav = new ModelAndView();

            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            billContBean = comonScheduleDao.getBillContributionRepotScheduleHeaderDetails(annexure, billNo);
            empDataList = comonScheduleDao.getBillContributionRepotScheduleEmpList(annexure, billNo, crb.getAqyear(), crb.getAqmonth());

            BillContributionRepotBean obj = null;
            if (empDataList != null && empDataList.size() > 0) {
                obj = new BillContributionRepotBean();
                for (int i = 0; i < empDataList.size(); i++) {
                    obj = (BillContributionRepotBean) empDataList.get(i);

                    cpfTotal = obj.getTotCpf();
                    gcpfTotal = obj.getTotGcpf();
                    gTotal = obj.getGrandTotal();
                    cpfTotalFig = Numtowordconvertion.convertNumber((int) Integer.parseInt(cpfTotal));
                    gcpfTotalFig = Numtowordconvertion.convertNumber((int) Integer.parseInt(gcpfTotal));

                    if (obj.getArrearAmt() != null && !obj.getArrearAmt().equals("")) {
                        arrearAmtTotal = arrearAmtTotal + Integer.parseInt(obj.getArrearAmt());
                    }
                }
            }
            mav.addObject("NpsEmpList", empDataList);
            mav.addObject("NPSHeader", billContBean);

            mav.addObject("TotCpf", cpfTotal);
            mav.addObject("TotCpfFig", cpfTotalFig);
            mav.addObject("TotGcpf", gcpfTotal);
            mav.addObject("TotGcpfFig", gcpfTotalFig);
            mav.addObject("GrandTot", gTotal);

            if (annexure.equalsIgnoreCase("annexure1")) {
                double gcpfTotalA1 = Integer.parseInt(cpfTotal) + arrearAmtTotal;
                mav.addObject("TotGcpf", gcpfTotalA1);
                String gcpfTotalFigA1 = Numtowordconvertion.convertNumber((int) gcpfTotalA1);
                mav.addObject("TotGcpfFig", gcpfTotalFigA1);
                mav.setViewName("/payroll/schedule/BillContributionA1"); //annexure1

            } else if (annexure.equalsIgnoreCase("annexure2")) {
                mav.setViewName("/payroll/schedule/BillContributionA2"); //annexure2 

            } else if (annexure.equalsIgnoreCase("annexure3")) {
                double cpfTotalA3 = Integer.parseInt(cpfTotal) + arrearAmtTotal;
                mav.addObject("TotCpf", cpfTotalA3);
                String cpfTotalFigA3 = Numtowordconvertion.convertNumber((int) cpfTotalA3);
                mav.addObject("TotCpfFig", cpfTotalFigA3);
                mav.setViewName("/payroll/schedule/BillContributionA3"); //annexure3                
            } else if (annexure.equalsIgnoreCase("annexure4")) {
                double cpfTotalA4 = Integer.parseInt(cpfTotal) + arrearAmtTotal;
                mav.addObject("TotCpf", cpfTotalA4);
                String cpfTotalFigA = Numtowordconvertion.convertNumber((int) cpfTotalA4);
                mav.addObject("TotCpfFig", cpfTotalFigA);
                mav.setViewName("/payroll/schedule/BillContributionA4"); //annexure4 
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "BillContributionPDF", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView BillContributionPDF(@RequestParam("annexure") String annexure, @RequestParam("billNo") String billNo) {

        ModelAndView mav = null;
        List empDataList = null;
        String cpfTotal = "";
        String gcpfTotal = "";
        String cpfTotalFig = "";
        String gcpfTotalFig = "";
        String gTotal = "";
        BillContributionRepotBean billContBean = new BillContributionRepotBean();
        try {

            mav = new ModelAndView("billContributionPDFView");
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            billContBean = comonScheduleDao.getBillContributionRepotScheduleHeaderDetails(annexure, billNo);
            empDataList = comonScheduleDao.getBillContributionRepotScheduleEmpList(annexure, billNo, crb.getAqyear(), crb.getAqmonth());

            BillContributionRepotBean obj = null;
            if (empDataList != null && empDataList.size() > 0) {
                obj = new BillContributionRepotBean();
                for (int i = 0; i < empDataList.size(); i++) {
                    obj = (BillContributionRepotBean) empDataList.get(i);

                    cpfTotal = obj.getTotCpf();
                    gcpfTotal = obj.getTotGcpf();
                    gTotal = obj.getGrandTotal();
                    cpfTotalFig = Numtowordconvertion.convertNumber((int) Integer.parseInt(cpfTotal));
                    gcpfTotalFig = Numtowordconvertion.convertNumber((int) Integer.parseInt(gcpfTotal));
                }
            }
            mav.addObject("NpsEmpList", empDataList);
            mav.addObject("NPSHeader", billContBean);
            mav.addObject("annexure", annexure);
            mav.addObject("TotCpf", cpfTotal);
            mav.addObject("TotCpfFig", cpfTotalFig);
            mav.addObject("TotGcpf", gcpfTotal);
            mav.addObject("TotGcpfFig", gcpfTotalFig);
            mav.addObject("GrandTot", gTotal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "BillContributionPDFDownload", method = {RequestMethod.POST, RequestMethod.GET})
    public void BillContributionPDFDownload(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("SelectedEmpOffice") String selectedEmpOffice,
            HttpServletResponse response, @RequestParam("billNo") String billNo, @RequestParam("annexure") String annexure, @RequestParam("slNo") String slNo) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        String fname = "";
        try {

            if (annexure.equalsIgnoreCase("annexure1")) {
                fname = "ANNEXURE-I";
            } else if (annexure.equalsIgnoreCase("annexure2")) {
                fname = "ANNEXURE-II";
            } else if (annexure.equalsIgnoreCase("annexure3")) {
                fname = "ANNEXURE-III";
            } else if (annexure.equalsIgnoreCase("annexure4")) {
                fname = "ANNEXURE-IV";
            }

            response.setHeader("Content-Disposition", "attachment; filename=" + fname + "_" + billNo + ".pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);

            //String allowEsign = comonScheduleDao.allowedOfficeEsign(lub.getLoginoffcode());
            
            String allowEsign = comonScheduleDao.allowedOfficeEsign(selectedEmpOffice);
            String[] rowArray = allowEsign.split("\\|");
            String allowEsignStatus = rowArray[0];
            String createpathPDF = "";
            String unsignedPdfFilename = "";
            if (allowEsignStatus.equals("Y")) {
                createpathPDF = esignBillDao.GeneratePDFSignedPath(lub.getLoginoffcode(), crb.getAqyear(), crb.getAqmonth(), billNo);
                unsignedPdfFilename = fname + "_" + billNo + "_unsigned.pdf";
                PdfWriter.getInstance(document, new FileOutputStream(createpathPDF + File.separator + unsignedPdfFilename));
                document.open();
            }else {
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=" + fname + "_" + billNo + ".pdf");
                PdfWriter.getInstance(document, response.getOutputStream());
                document.open();
            }            
            schedulepdfEsign.BillContributionPDFDownload(document, billNo, annexure, crb.getAqmonth(), crb.getAqyear());
            
            /*if (billNo != null && !billNo.equals("") && allowEsignStatus.equals("Y")) {
                esignBillDao.savepdflog(unsignedPdfFilename, createpathPDF, slNo, billNo, crb.getAqmonth(), crb.getAqyear(), lub.getLoginempid(), lub.getLoginoffcode());
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
