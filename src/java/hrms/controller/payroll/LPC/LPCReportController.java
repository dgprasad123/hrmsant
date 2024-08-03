/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.LPC;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.payroll.LPC.LPCReportDAO;
import hrms.dao.payroll.payslip.PaySlipDAO;
import hrms.model.common.FileAttribute;
import hrms.model.login.Users;
import hrms.model.payroll.payslip.PaySlipListBean;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lenovo
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class LPCReportController {

    @Autowired
    LPCReportDAO LPCReportDAO;
    @Autowired
    SubStantivePostDAO subStantivePostDao;
    @Autowired
    PaySlipDAO PaySlipDao;
    @Autowired
    private ServletContext servletContext;

    @RequestMapping(value = "downloadLPCReport", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView downloadLPCReport(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("payslipform") PaySlipListBean pblpc, @RequestParam("year") int year, @RequestParam("month") int month, @RequestParam("SelectedMonthYear") String SelectedMonthYear, @RequestParam("txtRlvDt") String txtRlvDt, @RequestParam("sltRlvTime") String sltRlvTime, @RequestParam("rlvId") String rlvId) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        PaySlipListBean pbean = new PaySlipListBean();
        ModelAndView mav = null;
        try {
            String lpcPath = servletContext.getInitParameter("lpcPath");
            //month = month - 1;
            String deptCode = lub.getDepcode();
            
            String departmentName = subStantivePostDao.getDeptName(deptCode);
            String lId = lub.getEmpId();

            List payslipDetails = PaySlipDao.getPaySlip(lub.getEmpId(), year, month);
            
            for (int i = 0; i < payslipDetails.size(); i++) {
                pbean = (PaySlipListBean) payslipDetails.get(i);
            }
            String fname = "LPC_" + rlvId + ".pdf";
            File innerfile = new File(lpcPath + "/" + fname);
            // response.setHeader("Content-Disposition", "attachment; filename=" + fname);
            PdfWriter writer = null;
            writer = PdfWriter.getInstance(document, new FileOutputStream(innerfile));
            // PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            LPCReportDAO.downloadLPCReport(document, lub, month, year, SelectedMonthYear, txtRlvDt, sltRlvTime, pbean, fname, rlvId,pblpc);
            return new ModelAndView("redirect:/RelieveList.htm");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;

    }

    @RequestMapping(value = "generateLPC")
    public String generateLPC(@ModelAttribute("SelectedEmpObj") Users lub, ModelMap model, @RequestParam("rlvId") String rlvId) {
        LPCReportDAO.generateLPC(lub.getEmpId());
        model.put("empId", lub.getEmpId());
        model.put("rlvId", rlvId);
        return "/payroll/LPC/generateLPC";
    }

    @RequestMapping(value = "downlaodLPCFile")
    public void downlaodLPCFile(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users lub, ModelMap model, @RequestParam("rlvId") String rlvId) {
        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;
        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("lpcPath");
            fa = LPCReportDAO.downlaodLPCFile(rlvId, filepath,lub);
            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
