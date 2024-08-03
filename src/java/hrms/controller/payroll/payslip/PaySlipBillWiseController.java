/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.payslip;

import java.util.List;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import javax.servlet.http.HttpServletResponse;
import hrms.dao.payroll.payslip.PaySlipDAOImpl;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.model.common.CommonReportParamBean;
import javax.servlet.ServletContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletContextAware;

/**
 *
 * @author prashant
 */
@Controller
public class PaySlipBillWiseController implements ServletContextAware {

    @Autowired
    public PaySlipDAOImpl payslipDao;

    @Autowired
    public PayBillDMPDAO paybillDmpDao;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "PaySlipBillWisePDF", method = RequestMethod.GET)
    public void PaySlipBillWisePDF(HttpServletResponse response, @RequestParam("billNo") int billNo) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            response.setHeader("Content-Disposition", "attachment; filename=PaySlipBillWise_" + billNo + ".pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo + "");
            List dataList = payslipDao.getPayBillData(billNo, crb.getAqmonth(), crb.getAqyear());
            payslipDao.getPaySlipBillWise(document, dataList, billNo, crb, "");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}
