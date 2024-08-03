/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.schedule;

import hrms.dao.master.LoanTypeDAO;
import hrms.dao.payroll.schedule.CreateBatchFile;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.services.AISGISScheduleServices;
import hrms.dao.payroll.services.AnnexureIIIFormat2DMPServices;
import hrms.dao.payroll.services.AnnexureIIIDMPServices;
import hrms.dao.payroll.services.AnnexureIIDMPServices;
import hrms.dao.payroll.services.AnnexureIDMPServices;
import hrms.dao.payroll.services.AquitanceReportIIServices;
import hrms.dao.payroll.services.BankAppFormServices;
import hrms.dao.payroll.services.BankScheduleServices;
import hrms.dao.payroll.services.DACertificateServices;
import hrms.dao.payroll.services.ExcessPayScheduleServices;
import hrms.dao.payroll.services.GPFScheduleEightyColServices;
import hrms.dao.payroll.services.GPFScheduleServices;
import hrms.dao.payroll.services.HCScheduleServices;
import hrms.dao.payroll.services.HRRScheduleServices;
import hrms.dao.payroll.services.ITScheduleServices;
import hrms.dao.payroll.services.LICScheduleServices;
import hrms.dao.payroll.services.LTCScheduleServices;
import hrms.dao.payroll.services.PIAdvIntRecServices;
import hrms.dao.payroll.services.PIAdvPriRecScheduleServices;
import hrms.dao.payroll.services.PTScheduleServices;
import hrms.dao.payroll.services.PayRollServices;
import hrms.dao.payroll.services.PrivateLoanScheduleServices;
import hrms.dao.payroll.services.VacancyStatementDMPServices;
import hrms.dao.payroll.services.VehicleScheduleDMPServices;
import hrms.dao.payroll.services.VoucherSlipDMPServices;
import hrms.model.common.CommonReportParamBean;
import hrms.model.master.LoanType;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Manas
 */
@Controller
public class PayBillDMPController {

    @Autowired
    public PayRollServices payrollService;

    @Autowired
    public AquitanceReportIIServices aqReportIIServices;

    @Autowired
    public BankScheduleServices bankScheduleService;

    @Autowired
    public ITScheduleServices itScheduleService;

    @Autowired
    public LoanTypeDAO loanTypeDao;

    @Autowired
    public PIAdvIntRecServices piAdvIntRecServices;

    @Autowired
    public PIAdvPriRecScheduleServices piAdvPriRecServices;

    @Autowired
    public PayBillDMPDAO paybillDmpDao;

    @Autowired
    public PrivateLoanScheduleServices pvtlService;

    @Autowired
    GPFScheduleServices gpfservice;

    @Autowired
    GPFScheduleEightyColServices gpfEightyColService;
    /*
     @Autowired
     VehicleScheduleServices vehicleservices;
     */
    @Autowired
    PTScheduleServices ptservice;

    @Autowired
    public HRRScheduleServices hrrServ;

    @Autowired
    public LICScheduleServices licServ;

    @Autowired
    public BankAppFormServices bankAppFormServ;

    @Autowired
    public DACertificateServices daCertificateServ;

    @Autowired
    public ExcessPayScheduleServices excessPayServ;

    @Autowired
    public HCScheduleServices hcScheduleServ;

    @Autowired
    public AnnexureIDMPServices annexureIServices;

    @Autowired
    public AnnexureIIDMPServices annexureIIServices;

    @Autowired
    public AnnexureIIIDMPServices annexureIIIServices;

    @Autowired
    public AnnexureIIIFormat2DMPServices annexureIIIFormat2Services;

    @Autowired
    public LTCScheduleServices ltcScheduleServices;

    @Autowired
    public AISGISScheduleServices aisgisScheduleServices;

    @Autowired
    public VehicleScheduleDMPServices vehicleScheduleServices;

    @Autowired
    public VacancyStatementDMPServices vacancyStatementServices;

    @Autowired
    public VoucherSlipDMPServices voucherSlipServices;

    @Autowired
    private ServletContext context;

    @RequestMapping(value = "payBillDMP", method = {RequestMethod.POST, RequestMethod.GET})
    public void PayBillDMP80Column(@RequestParam("billNo") String billNo, HttpServletResponse response) throws IOException {

        if (billNo != null && !billNo.equals("")) {

            FileInputStream inputStream = null;
            OutputStream outStream = null;
            try {
                //http://localhost:8080/HRMSOpenSource/payBillDMP80Column.htm?billNo=40973221

                String folderPath = context.getInitParameter("DmpFilePath");
                String fileSeparator = context.getInitParameter("FileSeparator");

                response.setContentType("html/text");
                String headerKey = "Content-Disposition";
                String headerValue = String.format("attachment; filename=\"%s\"", "PayBill.txt");
                response.setHeader(headerKey, headerValue);
                outStream = response.getOutputStream();
                folderPath = folderPath + billNo;
                ArrayList printCommand = new ArrayList();
                CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);

                if (crb.getBillNo() != null && !crb.getBillNo().equals("")) {
                    payrollService.write(billNo, folderPath, fileSeparator, crb);

                    aqReportIIServices.write(billNo, folderPath, fileSeparator, crb);
                    /* for 120 column */
                    gpfservice.write(billNo, folderPath, fileSeparator, crb);

                    /* for 80 column */
                    gpfEightyColService.write(billNo, folderPath, fileSeparator, crb);
                    printCommand.add("TYPE GPFSCHEDULE_80COL.txt > lpt1");
                    /*
                     if(vehicleservices.write(crb,folderPath, fileSeparator,billNo)){
                     printCommand.add("TYPE VEHICLE_SCHEDULE.txt > lpt1");
                     }
                    
                     */
                    if (ptservice.write(billNo, folderPath, fileSeparator, crb)) {
                        printCommand.add("TYPE PTSCHEDULE.txt > lpt1");
                    }
                    if (pvtlService.write(billNo, folderPath, fileSeparator, crb)) {
                        printCommand.add("TYPE PVTLOANSCHEDULE.txt > lpt1");
                    }
                    if (bankScheduleService.write(billNo, folderPath, fileSeparator, crb) != null) {
                        printCommand.add("TYPE BANKSCHEDULE.txt > lpt1");
                    }
                    if (itScheduleService.write(billNo, folderPath, fileSeparator, crb) != false) {
                        printCommand.add("TYPE ITSCHEDULE.txt > lpt1");
                    }
                    if (licServ.write(billNo, folderPath, fileSeparator, crb)) {
                        printCommand.add("TYPE LICSCHEDULE.txt > lpt1");
                    }

                    if (hrrServ.write(billNo, folderPath, fileSeparator, "HRR", "HRRSCHEDULE.txt", crb)) {
                        printCommand.add("TYPE HRRSCHEDULE.txt > lpt1");
                    }
                    if (hrrServ.write(billNo, folderPath, fileSeparator, "WRR", "WRRSCHEDULE.txt", crb)) {
                        printCommand.add("TYPE WRRSCHEDULE.txt > lpt1");
                    }
                    if (hrrServ.write(billNo, folderPath, fileSeparator, "SWR", "SWRSCHEDULE.txt", crb)) {
                        printCommand.add("TYPE SWRSCHEDULE.txt > lpt1");
                    }

                    if (bankAppFormServ.write(billNo, folderPath, fileSeparator, crb)) {
                        printCommand.add("TYPE BANKAPPFORM.txt > lpt1");
                    }
                    if (daCertificateServ.write(billNo, folderPath, fileSeparator, crb)) {
                        printCommand.add("TYPE DACERTIFICATE.txt > lpt1");
                    }
                    if (excessPayServ.write(billNo, folderPath, fileSeparator, crb)) {
                        printCommand.add("TYPE EXCESSPAYSCHEDULE.txt > lpt1");
                    }
                    if (hcScheduleServ.write(billNo, folderPath, fileSeparator, crb)) {
                        printCommand.add("TYPE HCSCHEDULE.txt > lpt1");
                    }
                    if (annexureIServices.write(billNo, folderPath, fileSeparator, crb)) {
                        printCommand.add("TYPE ANNEXURE1.txt > lpt1");
                    }
                    if (annexureIIServices.write(billNo, folderPath, fileSeparator, crb)) {
                        printCommand.add("TYPE ANNEXURE2.txt > lpt1");
                    }
                    if (annexureIIIServices.write(billNo, folderPath, fileSeparator, crb)) {
                        printCommand.add("TYPE ANNEXURE3.txt > lpt1");
                    }
                    if (annexureIIIFormat2Services.write(billNo, folderPath, fileSeparator, crb)) {
                        printCommand.add("TYPE ANNEXURE3(II).txt > lpt1");
                    }
                    if (ltcScheduleServices.write(billNo, folderPath, fileSeparator, crb)) {
                        printCommand.add("TYPE LTCSCHEDULE.txt > lpt1");
                    }

                    if (aisgisScheduleServices.write(billNo, folderPath, fileSeparator, crb)) {
                        printCommand.add("TYPE AISGISSCHEDULE.txt > lpt1");
                    }

                    if (vehicleScheduleServices.write(billNo, folderPath, fileSeparator, crb)) {
                        printCommand.add("TYPE VEHICLE_SCHEDULE.txt > lpt1");
                    }

                    if (vacancyStatementServices.write(billNo, folderPath, fileSeparator, crb)) {
                        printCommand.add("TYPE VACANCYSTMT.txt > lpt1");
                    }

                    if (voucherSlipServices.write(billNo, folderPath, fileSeparator, crb)) {
                        printCommand.add("TYPE VOUCHERSLIP.txt > lpt1");
                    }

                    ArrayList<LoanType> loanList = loanTypeDao.getLoanTypeList();
                    for (LoanType loantp : loanList) {
                        String schedule = loantp.getLoanType();
                        String loanname = loantp.getLoanName();
                        String haveint = loantp.getHaveInt();
                        if (piAdvPriRecServices.write(billNo, schedule, loanname, folderPath, fileSeparator, crb)) {
                            printCommand.add("TYPE " + schedule + "-PRI-SCHEDULE.txt > lpt1");
                        }
                        if (haveint != null && haveint.equalsIgnoreCase("Y")) {
                            if (piAdvIntRecServices.write(billNo, schedule, loanname, folderPath, fileSeparator, crb)) {
                                printCommand.add("TYPE " + schedule + "-INT-SCHEDULE.txt > lpt1");
                            }
                        }
                    }

                    byte[] buffer = new byte[4096];
                    int bytesRead = -1;
                    CreateBatchFile cbf = new CreateBatchFile(folderPath);
                    cbf.write();
                    cbf = new CreateBatchFile(folderPath);
                    cbf.write(printCommand);
                    OutputStream out = response.getOutputStream();

                    File directory = new File(folderPath);
                    String[] files = directory.list();
                    byte[] zip = zipFiles(directory, files, fileSeparator);
                    String zipFileName = billNo + "_80Col";// crb.getBillgroupDesc()+"_"+(crb.getAqmonth()+1)+"_"+crb.getAqyear();
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFileName + ".ZIP\"");
                    out.write(zip);
                    out.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                outStream.close();
            }
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
