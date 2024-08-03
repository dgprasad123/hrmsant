package hrms.controller.loanworkflow;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.Ifms;
import hrms.dao.loanworkflow.LoanApplyDAO;
import hrms.model.loanworkflow.IFMSAuthObject;
import hrms.model.loanworkflow.LoanBillAck;
import hrms.model.loanworkflow.LoanForm;
import hrms.model.loanworkflow.LoanGPFForm;
import hrms.model.loanworkflow.LoanHBAForm;
import hrms.model.loanworkflow.LoanTempGPFForm;
import hrms.model.login.LoginUserBean;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.crypto.SecretKey;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lenovo
 */
@Controller
@SessionAttributes("LoginUserBean")
public class LoanController implements ServletContextAware {

    @Autowired
    LoanApplyDAO loanworkflowDao;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "loanapplyController", method = {RequestMethod.POST, RequestMethod.GET})
    public String loanApply(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("LoanForm") LoanForm loanform, ModelMap model) {
       loanform = loanworkflowDao.displayEmpDetails(lub.getLoginempid());
        // loanworkflowDao.saveLoanData(loanform);
        model.put("LoanForm", loanform);
        return "/loanworkflow/loanapply";
    }

    @RequestMapping(value = "saveLoan", method = {RequestMethod.POST, RequestMethod.GET}, params = "Save")
    public String saveLoan(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("LoanForm") LoanForm loanform) {
        String filepath = context.getInitParameter("LoanPath");
        loanworkflowDao.saveLoanData(loanform, lub.getLoginempid(), filepath);
        return "/loanworkflow/loanList";
    }

    /*   @RequestMapping(value = "saveLoan", method = {RequestMethod.POST,RequestMethod.GET},params="Back")
     public String getloanList(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("LoanForm") LoanForm loanform) {
     
     return "/loanworkflow/loanList";
     }*/
    @RequestMapping(value = "ChangePostLoanController", method = RequestMethod.GET)
    public String ChangePost() {

        String path = "/loanworkflow/PostChange";
        return path;
    }

    @RequestMapping(value = "loanList", method = RequestMethod.GET)
    public String LoanListPage(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        model.addAttribute("empid", lub.getLoginempid());
        return "/loanworkflow/loanList";
    }

    @ResponseBody
    @RequestMapping(value = "GetLoanListJSON", method = {RequestMethod.GET, RequestMethod.POST})
    public String getLoanListJSON(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        JSONArray json = null;
        try {
            List loanlist = loanworkflowDao.getLoanList(lub.getLoginempid());
            json = new JSONArray(loanlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @RequestMapping(value = "loanApprove", method = RequestMethod.GET)
    public String LoanListPage(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("taskId") int taskid) {
        LoanForm loan = new LoanForm();
        loan = loanworkflowDao.getLoanDetails(taskid, lub.getLoginempid());

        model.addAttribute("LoanForm", loan);
        return "/loanworkflow/loanapprove";
    }

    @ResponseBody
    @RequestMapping(value = "getPostListLoanworkflowJSON", method = {RequestMethod.GET, RequestMethod.POST})
    public String getPostListLoanworkflowJSON(@RequestParam("offcode") String offcode) {
        JSONArray json = null;
        try {
            List postlist = loanworkflowDao.getPostList(offcode);
            json = new JSONArray(postlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getprocessList", method = {RequestMethod.GET, RequestMethod.POST})
    public String getprocessListJSON(@RequestParam("processid") String processid, @RequestParam("taskidlist") String taskidlist) {
        JSONArray json = null;
        try {
            List postlist = loanworkflowDao.getprocessList(processid, taskidlist);
            json = new JSONArray(postlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getloanSanctionOperator", method = {RequestMethod.GET, RequestMethod.POST})
    public String getloanSanctionOperator() {
        JSONArray json = null;
        try {
            List postlist = loanworkflowDao.getloanSanctionOperator();
            json = new JSONArray(postlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @RequestMapping(value = "saveApproveLoan", method = {RequestMethod.POST, RequestMethod.GET}, params = "Save")
    public String saveApproveLoan(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("LoanForm") LoanForm loanform) {
        loanworkflowDao.saveApproveLoanData(loanform, lub.getLoginempid());
        return "/loanworkflow/loanList";
    }

    @RequestMapping(value = "saveLoansaction", method = {RequestMethod.POST, RequestMethod.GET}, params = "Save")
    public ModelAndView saveLoansaction(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("LoanForm") LoanForm loanform) {
        loanworkflowDao.saveLoansaction(loanform);
        return new ModelAndView("redirect:/SactionOrder.htm?taskid=" + loanform.getTaskid() + "&loanid=" + loanform.getLoanId());
    }

    @RequestMapping(value = "ReapplyLoanAction", method = RequestMethod.GET)
    public String ReapplyLoanAction(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("opt") String option, @RequestParam("loanid") int loanid) {
        LoanForm loan = new LoanForm();
        loan = loanworkflowDao.ReplyLoan(option, lub.getLoginempid(), loanid);

        model.addAttribute("LoanForm", loan);
        return "/loanworkflow/loanreapply";
    }

    @RequestMapping(value = "SactionOrder", method = RequestMethod.GET)
    public String SactionOrder(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("taskid") int taskid, @RequestParam("loanid") int loanid) {
        LoanForm loan = new LoanForm();
        loan = loanworkflowDao.SactionLoanOrder(taskid, lub.getLoginempid(), loanid);

        model.addAttribute("LoanForm", loan);
        Calendar now = Calendar.getInstance();

        int cyear = now.get(Calendar.YEAR);
        int nextYear = cyear + 1;
        String fyear = cyear + " - " + nextYear;
        model.addAttribute("curreyear", fyear);
        return "/loanworkflow/loansaction";
    }

    @RequestMapping(value = "PreviewSactionOrder", method = RequestMethod.GET)
    public String PreviewSactionOrder(Model model, @RequestParam("taskid") int taskid, @RequestParam("loanid") int loanid) {
        LoanForm loan = new LoanForm();
        loan = loanworkflowDao.PreviewSactionOrder(taskid, "00009448", loanid);

        model.addAttribute("LoanForm", loan);
        Calendar now = Calendar.getInstance();

        int cyear = now.get(Calendar.YEAR);
        int nextYear = cyear + 1;
        String fyear = cyear + " - " + nextYear;
        model.addAttribute("curreyear", fyear);
        return "/loanworkflow/previewloansaction";
    }

    @RequestMapping(value = "DownloadLoanAttch", method = RequestMethod.GET)
    public void downloadNRCAttachment(HttpServletResponse response, @RequestParam("lid") String loanid) {

        try {
            String filepath = context.getInitParameter("LoanPath");
            loanworkflowDao.downloadLoanAttachment(response, filepath, loanid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @RequestMapping(value = "savereapplyLoan", method = {RequestMethod.POST, RequestMethod.GET}, params = "Save")
    public String savereapplyLoan(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("LoanForm") LoanForm loanform) {
        String filepath = context.getInitParameter("LoanPath");
        loanworkflowDao.savereapplyLoanData(loanform, lub.getLoginempid(), filepath);
        return "/loanworkflow/loanList";
    }

    @RequestMapping(value = "deleteLoanAttachment")
    public String deleteLoanAttachment(@RequestParam("loanId") int lid) {
        String filepath = context.getInitParameter("LoanPath");
        loanworkflowDao.deleteLoanAttch(lid, filepath);
        return "/loanworkflow/loanList";
    }

    @RequestMapping(value = "loangpfapplyController", method = {RequestMethod.POST, RequestMethod.GET})
    public String loanGPFApply(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("LoanGPFForm") LoanGPFForm loanGPFform, ModelMap model, @RequestParam("type") String type) {
        if (type.equals("1")) {
            model.addAttribute("gpftype", "REFUNDABLE");
        } else {
            model.addAttribute("gpftype", "NON-REFUNDABLE");
        }

        loanGPFform = loanworkflowDao.GPFEmpDetails(lub.getLoginempid());
        model.put("LoanGPFForm", loanGPFform);
        return "/loanworkflow/loangpfapply";
    }

    @RequestMapping(value = "savegpfLoan", method = {RequestMethod.POST, RequestMethod.GET}, params = "Save")
    public String savegpfLoan(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("LoanGPFForm") LoanGPFForm loanGPFform) {
        String filepath = context.getInitParameter("LoangpfPath");
        loanworkflowDao.savegpfLoanData(loanGPFform, lub.getLoginempid(), filepath);
        return "/loanworkflow/loanList";
    }

    @RequestMapping(value = "gpfApprove", method = RequestMethod.GET)
    public String LoangpfListPage(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("taskId") int taskid) {
        LoanGPFForm loan = new LoanGPFForm();
        loan = loanworkflowDao.getgpfLoanDetails(taskid, lub.getLoginempid());

        model.addAttribute("LoanGPFForm", loan);
        return "/loanworkflow/gpfapprove";
    }

    @RequestMapping(value = "savegpfApproveLoan", method = {RequestMethod.POST, RequestMethod.GET}, params = "Save")
    public String savegpfApproveLoan(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("LoanGPFForm") LoanGPFForm LoanGPFForm) {

        loanworkflowDao.savegpfApproveLoanData(LoanGPFForm, lub.getLoginempid());

        return "/loanworkflow/loanList";
    }

    @RequestMapping(value = "ReapplygpfLoan", method = RequestMethod.GET)
    public String ReapplygpfLoan(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("opt") String option, @RequestParam("loanid") int loanid) {
        LoanGPFForm loan = new LoanGPFForm();
        loan = loanworkflowDao.ReplygpfLoan(option, lub.getLoginempid(), loanid);

        model.addAttribute("LoanGPFForm", loan);
        return "/loanworkflow/loangpfreapply";
    }

    @RequestMapping(value = "savegpfreapplyLoan", method = {RequestMethod.POST, RequestMethod.GET}, params = "Save")
    public String savegpfreapplyLoan(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("LoanForm") LoanGPFForm LoanGPFForm) {
        String filepath = context.getInitParameter("LoangpfPath");
        loanworkflowDao.savegpfreapplyLoan(LoanGPFForm, lub.getLoginempid(), filepath);

        return "/loanworkflow/loanList";
    }

    @RequestMapping(value = "SactionGPFOrder", method = RequestMethod.GET)
    public String SactionGPFOrder(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("taskid") int taskid, @RequestParam("loanid") int loanid) {
        LoanGPFForm loan = new LoanGPFForm();
        loan = loanworkflowDao.SactionGPFOrder(taskid, loanid);

        model.addAttribute("LoanGPFForm", loan);

        return "/loanworkflow/loangpfsaction";
    }

    @RequestMapping(value = "saveLoanGPFsaction", method = {RequestMethod.POST, RequestMethod.GET}, params = "Save")
    public ModelAndView saveLoanGPFsaction(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("LoanForm") LoanGPFForm LoanGPFForm) {

        loanworkflowDao.saveLoanGPFsaction(LoanGPFForm);
        return new ModelAndView("redirect:/SactionGPFOrder.htm?taskid=" + LoanGPFForm.getTaskid() + "&loanid=" + LoanGPFForm.getLoanId());
    }

    @RequestMapping(value = "loanhbaapplyController", method = {RequestMethod.POST, RequestMethod.GET})
    public String loanHBAApply(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("LoanHBAForm") LoanHBAForm LoanHBAForm, ModelMap model) {
        LoanHBAForm = loanworkflowDao.HBAEmpDetails(lub.getLoginempid());

        model.put("LoanHBAForm", LoanHBAForm);
        return "/loanworkflow/loanHBAapply";
    }

    @RequestMapping(value = "saveHBALoan", method = {RequestMethod.POST, RequestMethod.GET}, params = "Save")
    public String saveHBALoan(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("LoanHBAForm") LoanHBAForm LoanHBAForm) {
        String filepath = context.getInitParameter("LoanHBAPath");
        loanworkflowDao.saveHBALoanData(LoanHBAForm, lub.getLoginempid(), filepath);
        return "/loanworkflow/loanList";
    }

    @RequestMapping(value = "hbaApprove", method = RequestMethod.GET)
    public String LoanhbaListPage(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("taskId") int taskid) {
        LoanHBAForm loan = new LoanHBAForm();
        loan = loanworkflowDao.gethbaLoanDetails(taskid);

        model.addAttribute("LoanHBAForm", loan);
        return "/loanworkflow/hbaapprove";
    }

    @RequestMapping(value = "DownloadhbaLoanAttch", method = RequestMethod.GET)
    public void downloadNHBAttachment(HttpServletResponse response, @RequestParam("lid") String loanid, @RequestParam("attchment") String attchmentId) {

        try {
            String filepath = context.getInitParameter("LoanHBAPath");
            loanworkflowDao.downloadHBALoanAttachment(response, filepath, loanid, attchmentId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @RequestMapping(value = "savehbaApproveLoan", method = {RequestMethod.POST, RequestMethod.GET}, params = "Save")
    public String savehbaApproveLoan(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("LoanHBAForm") LoanHBAForm LoanHBAForm) {

        loanworkflowDao.savehbaApproveLoanData(LoanHBAForm, lub.getLoginempid());
        return "/loanworkflow/loanList";
    }

    @RequestMapping(value = "ReapplyhbaLoan", method = RequestMethod.GET)
    public String ReapplyhbaLoan(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("opt") String option, @RequestParam("loanid") int loanid) {
        LoanHBAForm loan = new LoanHBAForm();
        loan = loanworkflowDao.ReplyhbaLoan(option, lub.getLoginempid(), loanid);

        model.addAttribute("LoanHBAForm", loan);
        return "/loanworkflow/loanhbareapply";
    }

    @RequestMapping(value = "saveHBAreapplyLoan", method = {RequestMethod.POST, RequestMethod.GET}, params = "Save")
    public String saveHBAreapplyLoan(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("LoanHBAForm") LoanHBAForm LoanHBAForm) {
        String filepath = context.getInitParameter("LoanHBAPath");
        loanworkflowDao.saveHBAreapplyLoan(LoanHBAForm, lub.getLoginempid(), filepath);
        return "/loanworkflow/loanList";
    }

    @RequestMapping(value = "DownloadPDF")
    public void GeneratePDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("taskId") int taskId) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            response.setHeader("Content-Disposition", "attachment; filename=letter.pdf");

            PdfWriter.getInstance(document, response.getOutputStream());
            String loanType = loanworkflowDao.getLoanType(taskId);
            document.open();
            if (loanType.equals("PERSONAL COMPUTER") || loanType.equals("MOPED") || loanType.equals("MOTOR CYCLE") || loanType.equals("MOTOR CAR")) {
                loanworkflowDao.viewPDFfunc(document, taskId, lub.getLoginempid());

            }
            if (loanType.equals("HBA")) {
                loanworkflowDao.viewHBAPDFfunc(document, taskId, lub.getLoginempid());

            }
            if (loanType.equals("GPF-REFUNDABLE") || loanType.equals("GPF-NON-REFUNDABLE")) {
                loanworkflowDao.viewGPFPDFfunc(document, taskId, lub.getLoginempid());

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    // GPF Loan Details    
    @RequestMapping(value = "loantempgpfapplyController", method = {RequestMethod.POST, RequestMethod.GET})
    public String loanTempGPFApply(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("LoanTempGPFForm") LoanTempGPFForm LoanTempGPFForm, ModelMap model) {
        LoanTempGPFForm = loanworkflowDao.TempGPFEmpDetails(lub.getLoginempid());
        model.put("LoanTempGPFForm", LoanTempGPFForm);
        return "/loanworkflow/loantempgpfapply";
    }

    @RequestMapping(value = "savetempgpfLoan", method = {RequestMethod.POST, RequestMethod.GET}, params = "Save")
    public String savetempgpfLoan(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("LoanTempGPFForm") LoanTempGPFForm LoanTempGPFForm) {
        String filepath = context.getInitParameter("LoantempgpfPath");
        loanworkflowDao.savetempgpfLoanData(LoanTempGPFForm, lub.getLoginempid(), filepath);
        return "/loanworkflow/loanList";
    }

    @RequestMapping(value = "tempgpfApprove", method = RequestMethod.GET)
    public String LoantempgpfListPage(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("taskId") int taskid) {
        LoanTempGPFForm loan = new LoanTempGPFForm();
        loan = loanworkflowDao.tempgpfLoanDetails(taskid);

        model.addAttribute("LoanTempGPFForm", loan);
        return "/loanworkflow/tempgpfapprove";
    }

    @RequestMapping(value = "DownloadtempgpfLoanAttch", method = RequestMethod.GET)
    public void DownloadtempgpfLoanAttch(HttpServletResponse response, @RequestParam("lid") String loanid) {

        try {
            String filepath = context.getInitParameter("LoantempgpfPath");
            loanworkflowDao.DownloadtempgpfLoanAttch(response, filepath, loanid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @RequestMapping(value = "savetempgpfApprove", method = {RequestMethod.POST, RequestMethod.GET}, params = "Save")
    public String savetempgpfApprove(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("LoanTempGPFForm") LoanTempGPFForm LoanTempGPFForm) {

        loanworkflowDao.savetempfpfApproveData(LoanTempGPFForm);
        return "/loanworkflow/loanList";
    }

    @RequestMapping(value = "Reapplytempgpf", method = {RequestMethod.POST, RequestMethod.GET})
    public String Reapplytempgpf(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("opt") String option, @RequestParam("loanid") int loanid) {
        LoanTempGPFForm loan = new LoanTempGPFForm();
        loan = loanworkflowDao.Reapplytempgpf(option, lub.getLoginempid(), loanid);

        model.addAttribute("LoanTempGPFForm", loan);
        return "/loanworkflow/tempgpfreapply";
    }

    @RequestMapping(value = "savetempgpfreapply", method = {RequestMethod.POST, RequestMethod.GET}, params = "Save")
    public String savetempgpfreapply(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("LoanGPFForm") LoanTempGPFForm LoanTempGPFForm) {

        String filepath = context.getInitParameter("LoantempgpfPath");
        loanworkflowDao.savetempgpfreapply(LoanTempGPFForm, lub.getLoginempid(), filepath);
        return "/loanworkflow/loanList";
    }

    @RequestMapping(value = "downloadLoanTypeI")
    public void downloadLoanTypeI(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("loanid") int loanid) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            String fileName = "Loan_form_" + loanid + ".pdf";
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();
            loanworkflowDao.downloadLoanTypeI(document, loanid, lub.getLoginempid());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "downloadLoanTypeGPFRefund")
    public void downloadLoanTypeGPFRefund(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("loanid") int loanid) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            String fileName = "Loan_GPF_" + loanid + ".pdf";
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();
            loanworkflowDao.downloadLoanTypeGPFRefund(document, loanid, lub.getLoginempid());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "gpftemporarylPDF.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public void gpftemporarylPDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("loanid") int loanid) {
        ModelAndView mv = new ModelAndView();
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            String filePath = context.getInitParameter("PhotoPath");
            response.setHeader("Content-Disposition", "inline; filename=gpftemporarylPDF.pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            loanworkflowDao.gpftemporarylPDF(document, filePath, loanid);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

    }

    @RequestMapping(value = "gppartfinalPDF.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public void gppartfinalPDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            String filePath = context.getInitParameter("PhotoPath");
            response.setHeader("Content-Disposition", "inline; filename=gppartfinalPDF.pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            loanworkflowDao.gpfpartfinalPDF(document, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

    }

    @RequestMapping(value = "ifmssanctionadvancePDF.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public void ifmssanctionadvancePDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            String filePath = context.getInitParameter("PhotoPath");
            //String filename = empid + ".jpg";
            response.setHeader("Content-Disposition", "inline; filename=ifmssanctionadvancePDF.pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            loanworkflowDao.ifmssanctionadvancePDF(document, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

    }

    @RequestMapping(value = "saveGpfLoansaction", method = {RequestMethod.POST, RequestMethod.GET}, params = "Save")
    public ModelAndView saveGpfLoansaction(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("LoanGPFForm") LoanGPFForm loanform) {
        loanworkflowDao.saveGpfLoansaction(loanform);
        return new ModelAndView("redirect:/SactionGPFOrder.htm?taskid=" + loanform.getTaskid() + "&loanid=" + loanform.getLoanId());
    }

    @RequestMapping(value = "downloadHBALoan")
    public void downloadHBALoan(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("loanid") int loanid) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            String fileName = "HBALoan_form_" + loanid + ".pdf";
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();
            loanworkflowDao.downloadHBALoan(document, loanid, lub.getLoginempid());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "getxmlLoansanction", method = RequestMethod.GET)
    public void getxmlLoansanction(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        try {
            int taskid = 245744;
            int loanid = 233;
            String hrmsid = lub.getLoginempid();
            String folderPath = context.getInitParameter("LoanXML");

            OutputStream out = response.getOutputStream();

            String fnamereturn = loanworkflowDao.getxmlLoansanction(folderPath, taskid, loanid, hrmsid);
            String fname = fnamereturn;
            //File zf = new File(folderPath + fname + ".zip");

            createZip(folderPath + fname, fname + ".xml");

            File f = new File(folderPath, fname + ".xml");

            byte[] ba = java.nio.file.Files.readAllBytes(f.toPath());
            FileInputStream in = new FileInputStream(folderPath + fname + ".zip");
            //ba = IOUtils.toByteArray(in);
            //ba = Base64.encodeBase64(ba);
            //String str = new String(ba);
         /*   SecretKey appKey = Ifms.getAESKey();//256 Unique Key        
            String appkey = Ifms.getAppKey(appKey);  // 256 bit random unique AES 256 symmetric key.
            IFMSAuthObject ifmsAuthObject = Ifms.authorization(appkey); // Call authorization function for  authoriz clientId & clientSecrete
            String sek = ifmsAuthObject.getData().getSek(); //authToken
           // System.out.println("SEK=" + sek);
            String str = Ifms.decrptyBySyymetricKey(sek, appKey.getEncoded());
            System.out.println("str="+str);    
            String encryptedData = Ifms.encryptBySymmetricKey(ba, str); //encrypted String Data 

            LoanBillAck loanBillAck = Ifms.sendData(encryptedData, ifmsAuthObject.getData().getAuthToken());

            System.out.println("*Status*" + loanBillAck.isStatus());
            System.out.println("*Data*" + loanBillAck.getData());
            System.out.println("*REK*" + loanBillAck.getRek());*/
            
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    public void createZip(String dirpath, String filetobeZip) {
        byte[] buffer = new byte[1024];

        try {

            FileOutputStream fos = new FileOutputStream(dirpath + ".zip");
            ZipOutputStream zos = new ZipOutputStream(fos);

            ZipEntry ze = new ZipEntry(filetobeZip);
            zos.putNextEntry(ze);
            FileInputStream in = new FileInputStream(dirpath + ".xml");

            int len;
            while ((len = in.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            in.close();

            zos.closeEntry();

            //remember close it
            zos.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static byte[] convertFile2ByteArray(File crunchifyFile) {
        FileInputStream crunchifyInputStream = null;

        byte[] crunchifyByteStream = new byte[(int) crunchifyFile.length()];

        try {

            crunchifyInputStream = new FileInputStream(crunchifyFile);
            crunchifyInputStream.read(crunchifyByteStream);
            crunchifyInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return crunchifyByteStream;
    }

    @RequestMapping(value = "LoanAuthoritySanctionList", method = RequestMethod.GET)
    public String loanAuthoritySanctionList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        List li = loanworkflowDao.getLoanAuthoritySanctionList(lub.getLoginempid());
        model.addAttribute("sanctionList", li);
        return "/loanworkflow/LoanAuthoritySanctionList";
    }

}
