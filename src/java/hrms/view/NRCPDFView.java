/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.view;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.CommonFunctions;
import hrms.model.parmast.ParApplyForm;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.AbstractView;

/**
 *
 * @author manisha
 */
public class NRCPDFView extends AbstractView {

    public NRCPDFView() {
        setContentType("application/pdf");
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest hsr, HttpServletResponse response) throws Exception {
        ParApplyForm paf = (ParApplyForm) model.get("parApplyForm");
        String reviewedPARFilePath = (String) model.get("reviewedPARFilePath");;
        OutputStream out = response.getOutputStream();
        //response.setHeader("Content-Disposition", "attachment; filename=PerformanceApprisal_" + paf.getParId() + ".pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + paf.getEmpName() + "(" + paf.getPrdFrmDate()  + " " + "To" + " " + paf.getPrdToDate() + ")" + "-" + paf.getParId() + ".pdf");
        try {
            byte[] filebyte = FileUtils.readFileToByteArray(new File(reviewedPARFilePath + paf.getFiscalYear() + CommonFunctions.getResourcePath() + paf.getParId() + ".pdf"));
            out.write(filebyte);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();

        }
    }

}
