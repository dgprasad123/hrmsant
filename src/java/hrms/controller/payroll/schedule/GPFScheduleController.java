/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.schedule;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.FooterPageEvent;
import hrms.common.Numtowordconvertion;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.billbrowser.BillBrowserbean;
import hrms.model.payroll.schedule.GPFScheduleBean;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GPFScheduleController implements ServletContextAware {

    @Autowired
    public ScheduleDAO comonScheduleDao;

    @Autowired
    public PayBillDMPDAO paybillDmpDao;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "GPFScheduleHTML", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView GPFScheduleHTML(@RequestParam("billNo") String billNo) {

        ModelAndView mav = null;
        List gpfTypeList = null;
        List gpfAbstractList = null;
        GPFScheduleBean gpfHeader = null;
        GPFScheduleBean obj2 = null;
        double sal = 0.0;
        double totAmt = 0.0;
        String totalFig = null;

        try {
            mav = new ModelAndView();
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            gpfHeader = comonScheduleDao.getGPFScheduleHeaderDetails(billNo);

            gpfTypeList = comonScheduleDao.getGPFScheduleTypeList(billNo, crb.getAqmonth(), crb.getAqyear());
            gpfAbstractList = comonScheduleDao.getGPFScheduleAbstractList(billNo, crb.getAqmonth(), crb.getAqyear());

            GPFScheduleBean obj = null;
            if (gpfAbstractList != null && gpfAbstractList.size() > 0) {
                for (int i = 0; i < gpfAbstractList.size(); i++) {
                    obj = (GPFScheduleBean) gpfAbstractList.get(i);
                    sal = Double.parseDouble(obj.getTotalAmount());
                    totAmt = totAmt + sal;
                    totalFig = Numtowordconvertion.convertNumber((int) totAmt);
                }
            }

            mav.addObject("BillDesc", crb.getBilldesc());
            mav.addObject("GPFTypeList", gpfTypeList);
            mav.addObject("GPFAbstractList", gpfAbstractList);
            mav.addObject("TotAmt", totAmt);
            mav.addObject("TotFig", totalFig);

            mav.addObject("VchNo", crb.getVchNo());
            mav.addObject("VchDate", crb.getVchDate());

            mav.setViewName("/payroll/schedule/GPFSchedule");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "GPFSchedulePDF")
    public void GPFSchedulePdfController(HttpServletResponse response, @ModelAttribute("BillBrowserbean") BillBrowserbean bbBean, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        List gpfTypeList = null;
        List gpfAbstractList = null;
        GPFScheduleBean gpfHeader = null;
        GPFScheduleBean obj2 = null;
        final Font headingRule = FontFactory.getFont("Arial", 15, Font.BOLD);
        double sal = 0.0;
        double totAmt = 0.0;
        String totalFig = null;
        String billNo = null;
        try {
            response.setHeader("Content-Disposition", "attachment; filename=GPFSchedulePdf_" + bbBean.getBillNo() + ".pdf");

            billNo = bbBean.getBillNo();

            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            FooterPageEvent event = new FooterPageEvent();
            Rectangle rect = new Rectangle(150, 30, 550, 800);
            writer.setBoxSize("art", rect);
            writer.setPageEvent(event);
            document.open();

            /*Paragraph rule = new Paragraph("Government of Odisha", headingRule);
            rule.setAlignment(Element.ALIGN_CENTER);
            document.add(rule);*/

            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            gpfHeader = comonScheduleDao.getGPFScheduleHeaderDetails(billNo);

            gpfTypeList = comonScheduleDao.getGPFScheduleTypeList(billNo, crb.getAqmonth(), crb.getAqyear());
            gpfAbstractList = comonScheduleDao.getGPFScheduleAbstractList(billNo, crb.getAqmonth(), crb.getAqyear());

            GPFScheduleBean obj = null;
            if (gpfAbstractList != null && gpfAbstractList.size() > 0) {
                for (int i = 0; i < gpfAbstractList.size(); i++) {
                    obj = (GPFScheduleBean) gpfAbstractList.get(i);
                    sal = Double.parseDouble(obj.getTotalAmount());
                    totAmt = totAmt + sal;
                    totalFig = Numtowordconvertion.convertNumber((int) totAmt);
                }
            }

            comonScheduleDao.gpfSchedulePdf(writer, document, billNo, gpfTypeList, gpfAbstractList, crb,totalFig,totAmt,gpfHeader);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

}
