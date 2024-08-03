package hrms.controller.payroll.schedule;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.dao.payroll.schedule.ScheduleDAOImpl;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DACertificateController {
    
    @Autowired
    public ScheduleDAOImpl comonScheduleDao;
    
    @RequestMapping(value = "DACertificatePDF")
    public void thirdschedulePDF(HttpServletResponse response, @RequestParam("billNo") String billNo) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        PdfWriter writer = null;
        try {

            response.setHeader("Content-Disposition", "attachment; filename=DACertificate_" + billNo + ".pdf");

            writer = PdfWriter.getInstance(document, response.getOutputStream());

            document.open();

            comonScheduleDao.downloadDACertificatePDF(document);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
    @RequestMapping(value = "DaCertificateHtml")
    public ModelAndView DaCertificateHTML(@RequestParam("billNo") int billNo) {

        ModelAndView mav = null;
        try {
            mav = new ModelAndView();
            mav.setViewName("/payroll/schedule/DaCertificate"); // DA Certificate

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

}
