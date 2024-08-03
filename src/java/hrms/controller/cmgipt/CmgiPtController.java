package hrms.controller.cmgipt;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.dao.cmgipt.CmgiPtDAO;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.model.common.CommonReportParamBean;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CmgiPtController {
    
    @Autowired
    public CmgiPtDAO cmgiptDAO;
    
    @Autowired
    public PayBillDMPDAO paybillDmpDao;
    
    @RequestMapping(value = "CmgiPtPDF")
    public void CmgiPtPDF(HttpServletResponse response,@RequestParam("billNo") String billNo){
        
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        
        try{
            response.setHeader("Content-Disposition", "attachment; filename=CMGI_PT_Schedule.pdf");
            
            PdfWriter.getInstance(document, response.getOutputStream());
            
            document.open();
            
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            
            cmgiptDAO.ptPDF(document,crb,billNo);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            document.close();
        }
    }
    
}
