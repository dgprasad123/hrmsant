/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.schedule;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.CommonFunctions;
import hrms.common.Numtowordconvertion;
import hrms.dao.esignBill.EsignBillDAO;
import hrms.dao.payroll.billbrowser.AqReportDAOImpl;
import hrms.dao.payroll.billbrowser.BillBrowserDAO;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.billbrowser.AllowDeductDetails;
import hrms.model.payroll.billbrowser.BillObjectHead;
import hrms.model.payroll.schedule.OtcForm82Bean;
import hrms.model.payroll.schedule.OtcFormBean;
import hrms.model.payroll.schedule.Schedule;
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
public class OTCForm82Controller implements ServletContextAware {

    @Autowired
    public ScheduleDAO comonScheduleDao;

    private ServletContext context;

    @Autowired
    public PayBillDMPDAO paybillDmpDao;

    @Autowired
    BillBrowserDAO billBrowserDao;

    @Autowired
    AqReportDAOImpl aqReportDao;
    
     @Autowired
    EsignBillDAO esignBillDao;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "OTCForm82HTML", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView OTCForm82HTMLController(@RequestParam("billNo") String billNo) {

        ModelAndView mav = null;
        OtcForm82Bean otcBean = new OtcForm82Bean();
        try {
            mav = new ModelAndView();
            otcBean = comonScheduleDao.getOTCForm82ScheduleDetails(billNo);

            mav.addObject("Otc82Header", otcBean);
            mav.setViewName("/payroll/schedule/OTC82");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "ArrOTCForm82HTML")
    public ModelAndView ArrOTCForm52HTML(@RequestParam("billNo") String billNo) {

        ModelAndView mav = null;
        OtcForm82Bean otcBean = new OtcForm82Bean();
        BillObjectHead boha = new BillObjectHead();

        double payamtTot = 0;
        double cpfAmt = 0.0;
        double itAmt = 0.0;
        double ptAmt = 0.0;
        double deductAmt = 0;
        double netAmt = 0;
        String netTotal = "";

        String payhead = "";
        String dahead = "";
        String hrahead = "";
        String oahead = "";

        String monthName1 = "";
        String monthName2 = "";
        int year1;
        int year2;
        try {
            mav = new ModelAndView();

            otcBean = comonScheduleDao.getArrOTCForm82ScheduleDetails(billNo);

            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);

            monthName1 = CommonFunctions.getMonthAsString(crb.getFromMonth() - 1);
            year1 = crb.getFromYear();
            monthName2 = CommonFunctions.getMonthAsString(crb.getToMonth() - 1);
            year2 = crb.getToYear();

            if (crb.getTypeofBill().equalsIgnoreCase("ARREAR_6") || crb.getTypeofBill().equalsIgnoreCase("ARREAR") || crb.getTypeofBill().equalsIgnoreCase("ARREAR_J") || crb.getTypeofBill().equalsIgnoreCase("ARREAR_6_J")) {
                boha = aqReportDao.getArrBillObjectHeadAndAmt(billNo,crb.getTypeofBill());
                payamtTot = boha.getPayamt();
            } else {
                List allowanceList = billBrowserDao.getAllowanceListForArrear(Integer.parseInt(billNo), crb.getAqmonth(), crb.getAqyear());

                for (int i = 0; i < allowanceList.size(); i++) {
                    AllowDeductDetails allowdeduct = (AllowDeductDetails) allowanceList.get(i);
                    if (allowdeduct.getAdamount() != null && !allowdeduct.getAdamount().equals("")) {
                        payamtTot = payamtTot + Double.valueOf(allowdeduct.getAdamount());
                    }

                    if (allowdeduct.getObjecthead() != null && !allowdeduct.getObjecthead().equals("")) {
                        if (allowdeduct.getObjecthead().equals("136")) {
                            if (allowdeduct.getAdamount() != null && !allowdeduct.getAdamount().equals("")) {
                                if (payhead != null && !payhead.equals("")) {
                                    payhead = (Integer.parseInt(payhead) + Integer.parseInt(allowdeduct.getAdamount())) + "";
                                } else {
                                    payhead = Integer.parseInt(allowdeduct.getAdamount()) + "";
                                }

                            }
                        } else if (allowdeduct.getObjecthead().equals("523")) {
                            oahead = allowdeduct.getAdamount();
                        } else if (allowdeduct.getObjecthead().equals("156")) {
                            dahead = allowdeduct.getAdamount();
                        } else if (allowdeduct.getObjecthead().equals("403")) {
                            hrahead = allowdeduct.getAdamount();
                        }
                    }
                }
            }
            String gross = Double.valueOf(payamtTot + "").longValue() + "";

            Schedule obj = aqReportDao.getArrScheduleListWithADCode(billNo);

            if (obj.getCpfAmt() != null && !obj.getCpfAmt().equals("")) {
                cpfAmt = Double.parseDouble(obj.getCpfAmt());
            }
            if (obj.getPtAmt() != null && !obj.getPtAmt().equals("")) {
                ptAmt = Double.parseDouble(obj.getPtAmt());
            }
            if (obj.getItAmt() != null && !obj.getItAmt().equals("")) {
                itAmt = Double.parseDouble(obj.getItAmt());
            }
            deductAmt = cpfAmt + ptAmt + itAmt;

            netAmt = payamtTot - deductAmt;
            netTotal = Double.valueOf(netAmt + "").longValue() + "";
            String netTotalinWord = Numtowordconvertion.convertNumber(Integer.parseInt(new Double(netAmt).intValue() + ""));

            mav.addObject("Otc82Header", otcBean);

            mav.addObject("netPay", netTotal);
            mav.addObject("netPayWord", netTotalinWord);

            mav.addObject("payhead", payhead);
            mav.addObject("oahead", oahead);
            mav.addObject("dahead", dahead);
            mav.addObject("hrahead", hrahead);

            mav.addObject("monthNam1", monthName1);
            mav.addObject("year1", year1);
            mav.addObject("monthName2", monthName2);
            mav.addObject("year2", year2);

            mav.setViewName("/payroll/arrear/ArrOTC82");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "OTCForm82PDF")
    public void OTCForm82SchedulePDF(HttpServletResponse response, @RequestParam("billNo") String billNo, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice,@RequestParam("slNo") String slNo) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {

            response.setHeader("Content-Disposition", "attachment; filename=OTC82Schedule_" + billNo + ".pdf"); // OTCForm82 Schedule
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            OtcForm82Bean otcBean = comonScheduleDao.getOTCForm82ScheduleDetails(billNo);
            
            /************** Save Unsigned PDF Details ******************************/
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);            
            //String allowEsign = comonScheduleDao.allowedOfficeEsign(lub.getLoginoffcode());
            
            String allowEsign = comonScheduleDao.allowedOfficeEsign(selectedEmpOffice);
            
            String[] rowArray = allowEsign.split("\\|");
            String allowEsignStatus = rowArray[0];
            String createpathPDF = "";
            String unsignedPdfFilename = "";
           /*if (allowEsignStatus.equals("Y")) {
             createpathPDF = esignBillDao.GeneratePDFSignedPath(lub.getLoginoffcode(), crb.getAqyear(), crb.getAqmonth(), billNo);
             unsignedPdfFilename = "OTC82Schedule_" + billNo + "_unsigned.pdf";
             PdfWriter.getInstance(document, new FileOutputStream(createpathPDF + File.separator + unsignedPdfFilename));
             document.open();
             }
            if (billNo != null && !billNo.equals("") && allowEsignStatus.equals("Y")) {
             esignBillDao.savepdflog(unsignedPdfFilename, createpathPDF, slNo,billNo, crb.getAqmonth(), crb.getAqyear(), lub.getLoginempid(), lub.getLoginoffcode());
             }*/
           /*************************************************************************/
            
            comonScheduleDao.OTC82SchedulePDF(document, billNo, otcBean);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

}
