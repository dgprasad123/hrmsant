/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.thread;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.LogMessage;
import hrms.dao.AgLta.AgLtaDAO;
import hrms.dao.master.TreasuryDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.payroll.billbrowser.BillBrowserbean;
import hrms.thread.ag.DownloadZIPFilesForAGThread;
import hrms.thread.ag.LTAAQReportPDFThread;
import hrms.thread.ag.LTAPDFThread;
import hrms.thread.ag.LTAThread;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas Jena
 */
@Controller
public class LTAThreadController implements ServletContextAware {

    @Autowired
    public LTAThread ltaThread;

    @Autowired
    public TreasuryDAO treasuryDao;

    @Autowired
    public AgLtaDAO agLTADAO;

    @Autowired
    public LTAPDFThread ltaPDFThread;

    @Autowired
    public LTAAQReportPDFThread ltaAquitancePDFThread;
    
    @Autowired
    public DownloadZIPFilesForAGThread zipFilesThread;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    /*@RequestMapping(value = "createXMLFile")
     public ModelAndView createXMLFile(ModelMap model, @ModelAttribute("billBrowserForm") BillBrowserbean bbbean,BindingResult result, HttpServletResponse response){
     LogMessage.setMessage("XML Creation called");
     if(ltaThread.getThreadStatus() == 0){
     LogMessage.setMessage("Inside the thread");
     LogMessage.setMessage("bbbean.getSltMonth():"+bbbean.getSltMonth()+"bbbean.getSltYear()"+bbbean.getSltYear()+" HBA");
     ltaThread.setThreadValue(bbbean.getSltMonth(),bbbean.getSltYear(),"HBA");
     Thread t = new Thread(ltaThread);
     t.start();
     }        
        
     ModelAndView mav = new ModelAndView();        
     mav.setViewName("payroll/LTAScheduleForAG");
     return mav;
                
     }*/
    @RequestMapping(value = "createXMLFile")
    public ModelAndView createXMLFile(ModelMap model, @ModelAttribute("billBrowserForm") BillBrowserbean bbbean, BindingResult result, HttpServletResponse response) {
        /*
         LogMessage.setMessage("XML Creation called");
         if(ltaThread.getThreadStatus() == 0){
         LogMessage.setMessage("Inside the thread");
         LogMessage.setMessage("bbbean.getSltMonth():"+bbbean.getSltMonth()+"bbbean.getSltYear()"+bbbean.getSltYear()+" HBA");
         ltaThread.setThreadValue(bbbean.getSltMonth(),bbbean.getSltYear(),"HBA");
         Thread t = new Thread(ltaThread);
         t.start();
         }        
         */
        try {
            String filepath = context.getInitParameter("agdownloadPDFPath");
            //String filepath = "D:\\ltaxml\\";

            String fileName = "LTA_" + (bbbean.getSltMonth() + 1) + "_" + bbbean.getSltYear() + ".zip";

            String dirpath = filepath + "LTA/" + fileName;
            
            File f = new File(dirpath);
            if (f.exists()) {
                response.setContentLength((int) f.length());
                response.setContentType("application/zip");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                response.setHeader("Cache-Control", "no-cache");

                byte[] buf = new byte[response.getBufferSize()];
                response.setContentLength((int) f.length());
                
                
                int length;

                BufferedInputStream fileInBuf = null;

                fileInBuf = new BufferedInputStream(new FileInputStream(f));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                while ((length = fileInBuf.read(buf)) > 0) {
                    baos.write(buf, 0, length);
                }

                response.getOutputStream().write(baos.toByteArray());
                response.getOutputStream().flush();
                response.getOutputStream().close();
            }

            /*ModelAndView mav = new ModelAndView();        
             mav.setViewName("payroll/LTAScheduleForAG");
             return mav;*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }
    
    
    @RequestMapping(value = "DownloadFilesForAG")
    public ModelAndView DownloadFilesForAG(ModelMap model, HttpServletResponse response, @RequestParam("year") int year,@RequestParam("month") int month,@RequestParam("type") String type) {
        
        String fileName = "";
        String filepath = "";
        try {
            //filepath = context.getInitParameter("agdownloadPDFPath");
            String monthStr = CommonFunctions.getMonthAsString(month-1);
            if(type.equals("S")){
                fileName = year + "_" + monthStr + ".zip";
                filepath = context.getInitParameter("agdownloadPDFPath") + "Schedule/" + year + "/" + monthStr + "/";
            }else if(type.equals("A")){
                fileName = year + "_" + monthStr + ".zip";
                filepath = context.getInitParameter("agdownloadPDFPath") + "Aquitance/" + year + "/" + monthStr + "/";
            }else if(type.equals("L")){
                fileName = "LTA_" + month + "_" + year + ".zip";
                filepath = context.getInitParameter("agdownloadPDFPath") + "LTA/";
            }else if(type.equals("AIS")){
                fileName = "AIS_" + month + "_" + year + ".zip";
                filepath = context.getInitParameter("agdownloadPDFPath") + "LTA/";
            }
            
            //String fileName = "LTA_" + (6 + 1) + "_" + 2020 + ".zip";

            String dirpath = filepath + fileName;
            System.out.println("DownloadFilesForAG dirpath is: "+dirpath);
            File f = new File(dirpath);
            if (f.exists()) {
                response.setContentLength((int) f.length());
                response.setContentType("application/zip");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                response.setHeader("Cache-Control", "no-cache");

                byte[] buf = new byte[response.getBufferSize()];
                response.setContentLength((int) f.length());
                
                int length;

                BufferedInputStream fileInBuf = null;

                fileInBuf = new BufferedInputStream(new FileInputStream(f));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                while ((length = fileInBuf.read(buf)) > 0) {
                    baos.write(buf, 0, length);
                }

                response.getOutputStream().write(baos.toByteArray());
                response.getOutputStream().flush();
                response.getOutputStream().close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "showXMLFileCreation", method = RequestMethod.GET)
    public ModelAndView showXMLFileCreation(ModelMap model, @ModelAttribute("loginForm") Users loginForm, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();

        mav.setViewName("payroll/LTAScheduleForAG");
        return mav;
    }

    @RequestMapping(value = "viewLogAction", method = RequestMethod.GET)
    public void viewLogAction(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(LogMessage.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "createPDFFile")
    public ModelAndView createPDFFile(ModelMap model, @ModelAttribute("billBrowserForm") BillBrowserbean bbbean, BindingResult result, HttpServletResponse response) {

        try {
            if (ltaThread.getThreadStatus() == 0) {
                LogMessage.setMessage("Inside the thread");
                LogMessage.setMessage("bbbean.getSltMonth():" + bbbean.getSltMonth() + "bbbean.getSltYear()" + bbbean.getSltYear() + " CMPA");
                ltaThread.setThreadValue(0, 2019, "CMPA");
                Thread t = new Thread(ltaThread);
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ModelAndView mav = new ModelAndView();
        mav.setViewName("payroll/LTAScheduleForAG");
        return mav;
    }

    @RequestMapping(value = "DownloadLoanPDFAGPage")
    public ModelAndView downloadLoanPDFAGPage(@ModelAttribute("billBrowserForm") BillBrowserbean bbbean) {

        ModelAndView mav = new ModelAndView();

        SelectOption so = new SelectOption();
        List yearlist = new ArrayList();
        try {

            List trlist = treasuryDao.getAGTreasuryList();
            mav.addObject("trlist", trlist);

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);

            so.setLabel(year + "");
            so.setValue(year + "");
            yearlist.add(so);

            for (int i = 0; i < 4; i++) {
                int tempyear = year - 1;
                year = tempyear;
                so = new SelectOption();
                so.setLabel(year + "");
                so.setValue(year + "");
                yearlist.add(so);
            }
            mav.addObject("yearlist", yearlist);
            mav.setViewName("payroll/DownloadLoanPDFAG");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "DownloadLoanPDFAG")
    public ModelAndView downloadLoanPDFAG(HttpServletResponse response, @ModelAttribute("billBrowserForm") BillBrowserbean bbbean) {

        ModelAndView mav = new ModelAndView();

        FileInputStream inputStream = null;
        OutputStream outStream = null;
        try {
            outStream = response.getOutputStream();

            String filepath = context.getInitParameter("agdownloadPDFPath") + "Schedule/";
            //String fileSeparator = context.getInitParameter("FileSeparator");

            if (bbbean.getSltYear() == 2020) {
                if (bbbean.getSltMonth() < 1) {
                    if (bbbean.getSltLoan().equalsIgnoreCase("AISGIS")) {
                        bbbean.setSltLoan("GIS");
                    }

                }
            }

            if (bbbean.getSltYear() < 2020) {

                if (bbbean.getSltLoan().equalsIgnoreCase("AISGIS")) {
                    bbbean.setSltLoan("GIS");
                }
            }

            //String fileName = bbbean.getSltYear() + "/" + bbbean.getSltTrCode() + "_" + bbbean.getSltLoan() + "_" + (bbbean.getSltMonth() + 1) + "_" + bbbean.getSltYear() + ".pdf";
            String fileName = bbbean.getSltYear() + "/" + CommonFunctions.getMonthAsString(bbbean.getSltMonth()) + "/" + bbbean.getSltTrCode() + "_" + bbbean.getSltLoan() + "_" + (bbbean.getSltMonth() + 1) + "_" + bbbean.getSltYear() + ".pdf";
            String downloadfileName = bbbean.getSltTrCode() + "_" + bbbean.getSltLoan() + "_" + (bbbean.getSltMonth() + 1) + "_" + bbbean.getSltYear() + ".pdf";

            String dirpath = filepath + fileName;
            

            mav.addObject("filepath", filepath);
            mav.addObject("fileName", fileName);
            mav.addObject("dirpath", dirpath);

            File f = new File(dirpath);
            if (f.exists()) {
                response.setContentLength((int) f.length());
                //response.setContentType("application/pdf");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + downloadfileName + "\"");

                byte[] buffer = new byte[4096];
                int bytesRead = -1;

                // write bytes read from the input stream into the output stream
                inputStream = new FileInputStream(f);
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }

                /*String[] files = f.list();
                 byte[] zip = zipFiles(f, files, fileSeparator);
                 String zipFileName = bbbean.getSltYear() + "";// crb.getBillgroupDesc()+"_"+(crb.getAqmonth()+1)+"_"+crb.getAqyear();
                 response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFileName + ".zip\"");
                 outStream.write(zip);
                 outStream.flush();*/
            }
            mav.setViewName("payroll/DownloadLoanPDFAG");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mav;
    }

    @RequestMapping(value = "LTASendMsg")
    public void LTASendMsg() {
        try {
            agLTADAO.sendLTASMS();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "LTAScheduleBulkPDFforAG")
    public ModelAndView LTAScheduleBulkPDFforAG(@ModelAttribute("billBrowserForm") BillBrowserbean bbbean) {

        ModelAndView mav = new ModelAndView();

        SelectOption so = new SelectOption();
        List yearlist = new ArrayList();
        try {

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);

            so.setLabel(year + "");
            so.setValue(year + "");
            yearlist.add(so);

            for (int i = 0; i < 4; i++) {
                int tempyear = year - 1;
                year = tempyear;
                so = new SelectOption();
                so.setLabel(year + "");
                so.setValue(year + "");
                yearlist.add(so);
            }
            mav.addObject("yearlist", yearlist);
            mav.setViewName("payroll/CreateLoanBulkPDFAG");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "createLTABulkPDFforAG")
    public ModelAndView createLTABulkPDFforAG(ModelMap model, @ModelAttribute("billBrowserForm") BillBrowserbean bbbean, BindingResult result, HttpServletResponse response) {

        try {
            if (ltaPDFThread.getThreadStatus() == 0) {
                LogMessage.setMessage("Inside the Bulk PDF thread");
                LogMessage.setMessage("bbbean.getSltMonth(): " + bbbean.getSltMonth() + "bbbean.getSltYear(): " + bbbean.getSltYear() + " and bbbean.getSltLoan(): " + bbbean.getSltLoan());
                ltaPDFThread.setThreadValue(bbbean.getSltMonth(), bbbean.getSltYear(), bbbean.getSltLoan());
                Thread t = new Thread(ltaPDFThread);
                t.start();
            }
            //ltaService.createLTAScheduleBulkPDFFileForAG(bbbean.getSltMonth(), bbbean.getSltYear(), bbbean.getSltLoan());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ModelAndView mav = new ModelAndView("redirect:/LTAScheduleBulkPDFforAG.htm");
        return mav;
    }

    @RequestMapping(value = "LTAAQuitancePDFforAG")
    public ModelAndView LTAAQuitancePDFforAG(@ModelAttribute("billBrowserForm") BillBrowserbean bbbean) {

        ModelAndView mav = new ModelAndView();

        SelectOption so = new SelectOption();
        List yearlist = new ArrayList();
        try {
            List trlist = treasuryDao.getAGTreasuryList();
            mav.addObject("trlist", trlist);

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);

            so.setLabel(year + "");
            so.setValue(year + "");
            yearlist.add(so);

            for (int i = 0; i < 4; i++) {
                int tempyear = year - 1;
                year = tempyear;
                so = new SelectOption();
                so.setLabel(year + "");
                so.setValue(year + "");
                yearlist.add(so);
            }
            mav.addObject("yearlist", yearlist);
            mav.setViewName("payroll/CreateAquitancePDFAG");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "createLTAAquitancePDFforAG")
    public ModelAndView createLTAAquitancePDFforAG(ModelMap model, @ModelAttribute("billBrowserForm") BillBrowserbean bbbean, BindingResult result, HttpServletResponse response) {

        try {
            if (ltaAquitancePDFThread.getThreadStatus() == 0) {
                ltaAquitancePDFThread.setThreadValue(bbbean.getSltMonth(), bbbean.getSltYear(), bbbean.getSltTrCode());
                Thread t = new Thread(ltaAquitancePDFThread);
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ModelAndView mav = new ModelAndView("redirect:/LTAAQuitancePDFforAG.htm");
        return mav;
    }
    
    @RequestMapping(value = "DownloadZIPFilesForAGPage")
    public ModelAndView DownloadZIPFilesForAGPage(@ModelAttribute("billBrowserForm") BillBrowserbean bbbean) {

        ModelAndView mav = new ModelAndView();

        SelectOption so = new SelectOption();
        List yearlist = new ArrayList();
        try {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);

            so.setLabel(year + "");
            so.setValue(year + "");
            yearlist.add(so);

            for (int i = 0; i < 4; i++) {
                int tempyear = year - 1;
                year = tempyear;
                so = new SelectOption();
                so.setLabel(year + "");
                so.setValue(year + "");
                yearlist.add(so);
            }
            mav.addObject("yearlist", yearlist);
            mav.setViewName("payroll/DownloadZIPFilesForAGPage");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "DownloadZIPFilesForAG")
    public ModelAndView DownloadZIPFilesForAG(@ModelAttribute("billBrowserForm") BillBrowserbean bbbean) {

         try {
            if (zipFilesThread.getThreadStatus() == 0) {
                zipFilesThread.setThreadValue(bbbean.getSltMonth(), bbbean.getSltYear(), bbbean.getSltLoan());
                Thread t = new Thread(zipFilesThread);
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ModelAndView mav = new ModelAndView("redirect:/DownloadZIPFilesForAGPage.htm?sltLoan="+bbbean.getSltLoan()+"&sltYear="+bbbean.getSltYear()+"&sltMonth="+bbbean.getSltMonth());
        return mav;
    }
    
    @RequestMapping(value = "DownloadMonthlySuperannuation")
    public String DownMonthlySuperannuation(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("billBrowserForm") BillBrowserbean bbbean) {
        
        SelectOption so = new SelectOption();
        List yearlist = new ArrayList();
        try {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            year = year + 1;

            so.setLabel(year + "");
            so.setValue(year + "");
            yearlist.add(so);

            for (int i = 0; i < 4; i++) {
                int tempyear = year - 1;
                year = tempyear;
                so = new SelectOption();
                so.setLabel(year + "");
                so.setValue(year + "");
                yearlist.add(so);
            }
            model.addAttribute("yearlist", yearlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "/payroll/DownloadMonthlySuperannuationAG";
    }
    
    @RequestMapping(value = "downloadMonthlySuperannuationExcel")
    public void downloadMonthlySuperannuationExcel(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("billBrowserForm") BillBrowserbean bbbean) {

        response.setContentType("application/vnd.ms-excel");

        OutputStream out = null;

        try {
            out = response.getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            
            response.setHeader("Content-Disposition", "attachment; filename=Monthly_Superannuation_" + bbbean.getSltYear() + "_" + bbbean.getSltMonth() + ".xls");
            
            agLTADAO.downloadMonhtlySuperannuationDataExcel(workbook, bbbean.getSltYear(),bbbean.getSltMonth());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @RequestMapping(value = "AGLTALoanIntimationSendSMS")
    public void AGLTALoanIntimationSendSMS() {
        try {
            agLTADAO.agLTALoanIntimationSendSMS();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
