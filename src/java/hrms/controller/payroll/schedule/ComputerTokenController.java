/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.schedule;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.dao.payroll.billbrowser.AqReportDAOImpl;
import hrms.dao.payroll.billbrowser.BillBrowserDAO;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.billbrowser.AllowDeductDetails;
import hrms.model.payroll.billbrowser.BillObjectHead;
import hrms.model.payroll.schedule.ComputerTokenReportBean;
import hrms.model.payroll.schedule.Schedule;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ComputerTokenController implements ServletContextAware {

    @Autowired
    public ScheduleDAO comonScheduleDao;

    @Autowired
    public PayBillDMPDAO paybillDmpDao;

    @Autowired
    BillBrowserDAO billBrowserDao;

    @Autowired
    AqReportDAOImpl aqReportDao;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "ComputerTokenHTML", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView ComputerTokenHTML(@RequestParam("billNo") String billNo) {

        ModelAndView mav = null;
        ComputerTokenReportBean tokenBean = new ComputerTokenReportBean();
        try {
            mav = new ModelAndView();
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);

            tokenBean = comonScheduleDao.getCompTokenRepotScheduleDetails(billNo, crb.getAqyear(), crb.getAqmonth());

            mav.addObject("CTokenHeader", tokenBean);
            mav.setViewName("/payroll/schedule/ComputerTokenReport");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "ComputerTokenSchedulePdf", method = {RequestMethod.POST, RequestMethod.GET})
    public void ComputerTokenPdf(@RequestParam("billNo") String billNo, HttpServletResponse response) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        ComputerTokenReportBean tokenBean = new ComputerTokenReportBean();
        try {
            response.setHeader("Content-Disposition", "attachment; filename=ComputerTokenSchedulePdf_" + billNo + ".pdf");
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);

            tokenBean = comonScheduleDao.getCompTokenRepotScheduleDetails(billNo, crb.getAqyear(), crb.getAqmonth());
            comonScheduleDao.computerTokenSchedulePdf(writer, document, billNo, tokenBean);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "ArrComputerTokenHTML")
    public ModelAndView ArrComputerTokenHTML(@RequestParam("billNo") String billNo) {

        ModelAndView mav = null;
        ComputerTokenReportBean tokenBean = new ComputerTokenReportBean();

        double payamtTot = 0;
        double cpfAmt = 0.0;
        double itAmt = 0.0;
        double ptAmt = 0.0;
        double deductAmt = 0;
        double netAmt = 0;
        double orData = 0.0;
        double drData = 0.0;

        BillObjectHead boha = null;
        try {
            mav = new ModelAndView();
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);

            tokenBean = comonScheduleDao.getArrCompTokenRepotScheduleDetails(billNo, crb.getAqyear(), crb.getAqmonth());
            if (crb.getTypeofBill() != null && (crb.getTypeofBill().equals("ARREAR") || crb.getTypeofBill().equals("ARREAR_6") || crb.getTypeofBill().equals("ARREAR_J") || crb.getTypeofBill().equals("ARREAR_6_J"))) {
                boha = aqReportDao.getArrBillObjectHeadAndAmt(billNo,crb.getTypeofBill());
                payamtTot = boha.getPayamt();
                String gross = Double.valueOf(payamtTot + "").longValue() + "";
                mav.addObject("gross", gross);
            } else if (crb.getTypeofBill() != null && crb.getTypeofBill().equals("OTHER_ARREAR")) {
                List allowanceList = billBrowserDao.getAllowanceListForComputerTokenReportArrear(Integer.parseInt(billNo), crb.getAqmonth(), crb.getAqyear());

                for (int i = 0; i < allowanceList.size(); i++) {
                    AllowDeductDetails allowdeduct = (AllowDeductDetails) allowanceList.get(i);
                    if (allowdeduct.getAdamount() != null && !allowdeduct.getAdamount().equals("")) {
                        payamtTot = payamtTot + Double.valueOf(allowdeduct.getAdamount());
                    }
                }
                String gross = Double.valueOf(payamtTot + "").longValue() + "";

                mav.addObject("allowanceList", allowanceList);
                mav.addObject("gross", gross);
            }

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
            if (obj.getOrAmt() != null && !obj.getOrAmt().equals("")) {
                orData = Double.parseDouble(obj.getOrAmt());
            }
           /* if (obj.getDrAmt() != null && !obj.getDrAmt().equals("")) {
                drData = Double.parseDouble(obj.getDrAmt());
            }*/
            deductAmt = cpfAmt + ptAmt + itAmt + orData ;

            netAmt = payamtTot - deductAmt;

            mav.addObject("CTokenHeader", tokenBean);
            mav.addObject("deductionObj", obj);
            mav.addObject("netTotal", Double.valueOf(netAmt).longValue());
            mav.addObject("deductAmt", deductAmt);

            mav.addObject("billtype", crb.getTypeofBill());

            mav.setViewName("/payroll/arrear/ArrComputerTokenReport");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

}
