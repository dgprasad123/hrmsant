/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.schedule;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.FooterPageEvent;
import hrms.common.Numtowordconvertion;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.ExcessPayBean;
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
public class ExcessPayController implements ServletContextAware {

    @Autowired
    public ScheduleDAO comonScheduleDao;

    @Autowired
    public PayBillDMPDAO paybillDmpDao;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "ExcessPayHTML", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView ExcessPayHTML(@RequestParam("billNo") String billNo) {

        ModelAndView mav = null;
        List empList = null;
        ExcessPayBean excessBean = new ExcessPayBean();
        String totalGross = "";
        String totalTax = "";
        String totalTaxFig = "";
        try {
            mav = new ModelAndView();
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            excessBean = comonScheduleDao.getExcessPayScheduleHeaderDetails(billNo);
            empList = comonScheduleDao.getExcessPayScheduleEmpDetails(billNo, crb.getAqyear(), crb.getAqmonth());

            ExcessPayBean obj = null;
            if (empList != null && empList.size() > 0) {
                obj = new ExcessPayBean();
                for (int i = 0; i < empList.size(); i++) {
                    obj = (ExcessPayBean) empList.get(i);
                    totalGross = obj.getTotalGross();
                    totalTax = obj.getTotalTax();
                    totalTaxFig = Numtowordconvertion.convertNumber(Integer.parseInt(totalTax));
                }
            }

            mav.addObject("ExcessHeader", excessBean);
            mav.addObject("ExcessList", empList);

            mav.addObject("GrossTot", totalGross);
            mav.addObject("TaxTot", totalTax);
            mav.addObject("TaxTotFig", totalTaxFig);
            mav.setViewName("/payroll/schedule/ExcessPay");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "ExcessPayPdf", method = {RequestMethod.POST, RequestMethod.GET})
    public void ExcessPayPDF(@RequestParam("billNo") String billNo, HttpServletResponse response) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        ModelAndView mav = null;
        List empList = null;
        ExcessPayBean excessBean = new ExcessPayBean();
        String totalGross = "";
        String totalTax = "";
        String totalTaxFig = "";
        try {
            response.setHeader("Content-Disposition", "attachment; filename=ExcessPaySchedulePdf_" + billNo + ".pdf");
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            FooterPageEvent event = new FooterPageEvent();
            Rectangle rect = new Rectangle(150, 30, 550, 800);
            writer.setBoxSize("art", rect);
            writer.setPageEvent(event);
            document.open();
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            excessBean = comonScheduleDao.getExcessPayScheduleHeaderDetails(billNo);
            empList = comonScheduleDao.getExcessPayScheduleEmpDetails(billNo, crb.getAqyear(), crb.getAqmonth());

            /*ExcessPayBean obj = null;
            if (empList != null && empList.size() > 0) {
                obj = new ExcessPayBean();
                for (int i = 0; i < empList.size(); i++) {
                    obj = (ExcessPayBean) empList.get(i);
                    totalGross = obj.getTotalGross();
                    totalTax = obj.getTotalTax();
                    totalTaxFig = Numtowordconvertion.convertNumber(Integer.parseInt(totalTax));
                }
            }*/
            comonScheduleDao.excesspaySchedulePdf(writer, document, billNo, excessBean, empList);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

}
