package hrms.controller.payroll.payslip;

import hrms.dao.payroll.schedule.CreateBatchFile;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.services.PaySlipIDMPServices;
import hrms.dao.payroll.services.PaySlipIIDMPServices;
import hrms.model.common.CommonReportParamBean;
import hrms.model.login.LoginUserBean;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("LoginUserBean")
public class PaySlipDMPController {

    @Autowired
    public PaySlipIDMPServices paySlipIDMPServices;
    
    @Autowired
    public PaySlipIIDMPServices paySlipIIDMPServices;
    
    @Autowired
    public PayBillDMPDAO paybillDmpDao;

    @Autowired
    private ServletContext context;

    @RequestMapping(value = "paySlipDMP")
    public void PaySlipDMP(@ModelAttribute("LoginUserBean") LoginUserBean lub,@RequestParam("billNo") String billNo, HttpServletResponse response) throws IOException {

        FileInputStream inputStream = null;
        OutputStream outStream = null;
        try {
            //http://localhost:8080/HRMSOpenSource/payBillDMP80Column.htm?billNo=40973221

            String folderPath = context.getInitParameter("DmpFilePath");
            String fileSeparator = context.getInitParameter("FileSeparator");

            response.setContentType("text/html");
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", "PaySlip.txt");
            response.setHeader(headerKey, headerValue);
            outStream = response.getOutputStream();
            folderPath = folderPath + lub.getLoginoffcode() + billNo;
            ArrayList printCommand = new ArrayList();
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);

            paySlipIDMPServices.write(billNo, folderPath, fileSeparator, crb);
            printCommand.add("TYPE PAYSLIP.txt > lpt1");
            
            paySlipIIDMPServices.write(billNo, folderPath, fileSeparator, crb);
            //printCommand.add("TYPE PAYSLIP2.txt > lpt1");
            
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            
            CreateBatchFile cbf = new CreateBatchFile(folderPath);
            cbf.write(printCommand,"PAYSLIP");
            
            OutputStream out = response.getOutputStream();

            File directory = new File(folderPath);
            String[] files = directory.list();
            byte[] zip = zipFiles(directory, files, fileSeparator);
            String zipFileName = billNo+"_PAYSLIP";// crb.getBillgroupDesc()+"_"+(crb.getAqmonth()+1)+"_"+crb.getAqyear();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFileName + ".ZIP\"");
            out.write(zip);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            outStream.close();
        }
    }

    private byte[] zipFiles(File directory, String[] files, String fileSeparator) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        byte bytes[] = new byte[2048];
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i];
            FileInputStream fis = new FileInputStream(directory.getPath() + fileSeparator + fileName);
            BufferedInputStream bis = new BufferedInputStream(fis);
            zos.putNextEntry(new ZipEntry(fileName));
            int bytesRead;
            while ((bytesRead = bis.read(bytes)) != -1) {
                zos.write(bytes, 0, bytesRead);
            }
            zos.closeEntry();
            bis.close();
            fis.close();
        }
        zos.flush();
        baos.flush();
        zos.close();
        baos.close();
        return baos.toByteArray();
    }
}
