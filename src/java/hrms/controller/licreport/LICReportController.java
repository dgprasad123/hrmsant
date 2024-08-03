package hrms.controller.licreport;

import hrms.common.CommonFunctions;
import hrms.dao.licreport.LICReportDAO;
import hrms.dao.master.TreasuryDAO;
import hrms.dao.payroll.billbrowser.BillBrowserDAO;
import hrms.model.licreport.LICDivisionWiseBean;
import hrms.model.licreport.LICTreasuryWiseBean;
import hrms.model.master.Treasury;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

@Controller
public class LICReportController implements ServletContextAware {

    @Autowired
    public LICReportDAO licreportDAO;

    @Autowired
    public BillBrowserDAO billBrowserDao;

    @Autowired
    TreasuryDAO treasuryDao;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "LICReport")
    public String LICReport() {

        return "/licreport/LICReportNavigation";

    }

    @RequestMapping(value = "LICTreasuryWise")
    public String LICTreasuryWise() {

        return "/licreport/LICTreasuryWise";

    }

    @RequestMapping(value = "LICDivisionWise")
    public String LICDivisionWise() {

        return "/licreport/LICDivisionWise";

    }

    /*@RequestMapping(value = "DownloadExcelLICTreasuryWise")
     public void DownloadExcelLICTreasuryWise(HttpServletResponse response, @RequestParam("treasury") String treasury, @RequestParam("year") String year, @RequestParam("month") String month) {

     ArrayList payBillList = billBrowserDao.getPayBillList(Integer.parseInt(year), Integer.parseInt(month), treasury);
     licreportDAO.downloadExcelLICTreasuryWise(response, treasury, payBillList);

     }*/
    @RequestMapping(value = "DownloadExcelLICTreasuryWise")
    public void DownloadExcelLICTreasuryWise(HttpServletResponse response, @RequestParam("treasury") String treasury, @RequestParam("year") String year, @RequestParam("month") String month) {

        FileInputStream inputStream = null;
        OutputStream outStream = null;
        try {
            outStream = response.getOutputStream();

            String filepath = context.getInitParameter("agdownloadPDFPath") + "/LIC/";
            //String fileSeparator = context.getInitParameter("FileSeparator");

            String fileName = year + "/" + CommonFunctions.getMonthAsString(Integer.parseInt(month)) + "/" + "LIC_" + treasury + ".xls";
            String downloadfileName = "LIC_" + treasury + "_" + year + "_" + CommonFunctions.getMonthAsString(Integer.parseInt(month)) + ".xls";

            String dirpath = filepath + fileName;

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
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //inputStream.close();
                outStream.close();
                outStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @ResponseBody
    @RequestMapping(value = "GetDivisionListJSON")
    public String GetDivisionListJSON(HttpServletResponse response) {

        JSONArray json = null;

        try {
            List divisionlist = licreportDAO.getDivisionList();
            json = new JSONArray(divisionlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "GetDivisionYearListJSON")
    public String GetDivisionYearListJSON(HttpServletResponse response) {

        JSONArray json = null;

        try {
            List finlist = licreportDAO.getYearList();
            json = new JSONArray(finlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "GetDivisionWiseTreasuryName")
    public String GetDivisionWiseTreasuryName(@RequestParam("divisionCode") String divisionCode) {

        JSONArray json = null;

        try {
            List treasurylist = licreportDAO.getDivisionWiseTreasuryList(divisionCode);
            json = new JSONArray(treasurylist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    /*@RequestMapping(value = "DownloadExcelLICDivisionWise")
     public void DownloadExcelLICDivisionWise(HttpServletResponse response, @RequestParam("trcode") String treasury, @RequestParam("year") String year, @RequestParam("month") String month) {

     licreportDAO.downloadExcelLICDivisionWise(response, treasury, year, month);

     }*/
    @RequestMapping(value = "DownloadExcelLICDivisionWise")
    public void DownloadExcelLICDivisionWise(HttpServletResponse response, @RequestParam("trcode") String treasury, @RequestParam("year") String year, @RequestParam("month") String month,@RequestParam("division") String division) {

        FileInputStream inputStream = null;
        OutputStream outStream = null;
        try {
            outStream = response.getOutputStream();

            String filepath = context.getInitParameter("agdownloadPDFPath") + "/LIC/";
            //String fileSeparator = context.getInitParameter("FileSeparator");

            String trName = licreportDAO.getTreasuryNameFromDivision(division,treasury);
            trName = trName.replace(" ", "_");
            
            String fileName = year + "/" + CommonFunctions.getMonthAsString(Integer.parseInt(month)) + "/" + "LIC_" + trName + "_DIVISION_WISE.xls";
            String downloadfileName = "LIC_" + trName + "_DIVISION_WISE.xls";
            
            String dirpath = filepath + fileName;

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
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //inputStream.close();
                outStream.close();
                outStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @RequestMapping(value = "LICBulkTreasuryWise")
    public String LICBulkTreasuryWise(Model model, @ModelAttribute("command") LICTreasuryWiseBean licbean) {

        ArrayList treasuryList = treasuryDao.getTreasuryList();

        model.addAttribute("treasuryList", treasuryList);

        return "/licreport/LICBulkTreasuryWise";

    }

    @RequestMapping(value = "CreateBulkLICTreasuryWiseExcel")
    public String createBulkLICExcel(@ModelAttribute("command") LICTreasuryWiseBean licbean) {
        OutputStream out = null;
        try {
            String filepath = context.getInitParameter("agdownloadPDFPath") + "/LIC/";
            
            if (licbean.getSltTreasury() != null && !licbean.getSltTreasury().equals("")) {
                String excelname = "LIC_" + licbean.getSltTreasury();
                excelname = excelname.replace(" ", "_");
                String fileName = excelname + ".xls";

                //String innerFolderName = agfilePath + year + "/" + CommonFunctions.getMonthAsString(month);
                String innerFolderName = filepath + licbean.getSltYear() + "/" + CommonFunctions.getMonthAsString(Integer.parseInt(licbean.getSltMonth())) + "/";
                
                File innerFolder = new File(innerFolderName);
                if (!innerFolder.exists()) {
                    innerFolder.mkdirs();
                }

                out = new FileOutputStream(new File(innerFolder + "/" + fileName));

                ArrayList payBillList = billBrowserDao.getPayBillList(Integer.parseInt(licbean.getSltYear()), Integer.parseInt(licbean.getSltMonth()), licbean.getSltTreasury());
                licreportDAO.createExcelLICTreasuryWise(out, licbean.getSltTreasury(), payBillList);
            } else {
                ArrayList trList = treasuryDao.getTreasuryList();
                Treasury treasury = null;
                
                for (int i = 0; i < trList.size(); i++) {
                    treasury = (Treasury) trList.get(i);

                    licbean.setSltTreasury(treasury.getTreasuryCode());

                    
                    
                    String excelname = "LIC_" + licbean.getSltTreasury();
                    excelname = excelname.replace(" ", "_");
                    String fileName = excelname + ".xls";

                    //String innerFolderName = agfilePath + year + "/" + CommonFunctions.getMonthAsString(month);
                    String innerFolderName = filepath + licbean.getSltYear() + "/" + CommonFunctions.getMonthAsString(Integer.parseInt(licbean.getSltMonth())) + "/";

                    File innerFolder = new File(innerFolderName);
                    if (!innerFolder.exists()) {
                        innerFolder.mkdirs();
                    }

                    out = new FileOutputStream(new File(innerFolder + "/" + fileName));

                    ArrayList payBillList = billBrowserDao.getPayBillList(Integer.parseInt(licbean.getSltYear()), Integer.parseInt(licbean.getSltMonth()), licbean.getSltTreasury());
                    licreportDAO.createExcelLICTreasuryWise(out, licbean.getSltTreasury(), payBillList);

                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/LICBulkTreasuryWise.htm?sltTreasury=" + licbean.getSltTreasury();
    }

    @RequestMapping(value = "LICBulkDivisionWise")
    public String LICBulkDivisionWise(Model model, @ModelAttribute("command") LICDivisionWiseBean licbean) {

        List divisionlist = licreportDAO.getDivisionList();

        model.addAttribute("divisionlist", divisionlist);

        return "/licreport/LICBulkDivisionWise";

    }

    @RequestMapping(value = "CreateBulkLICDivisionWiseExcel")
    public String CreateBulkLICDivisionWiseExcel(@ModelAttribute("command") LICDivisionWiseBean licbean) {
        OutputStream out = null;
        LICDivisionWiseBean licdivisionbean = null;
        try {
            String filepath = context.getInitParameter("agdownloadPDFPath") + "/LIC/";
            
            if (licbean.getSltDivision() != null && !licbean.getSltDivision().equals("")) {

                //String innerFolderName = agfilePath + year + "/" + CommonFunctions.getMonthAsString(month);
                String innerFolderName = filepath + licbean.getSltYear() + "/" + CommonFunctions.getMonthAsString(Integer.parseInt(licbean.getSltMonth())) + "/";

                File innerFolder = new File(innerFolderName);
                if (!innerFolder.exists()) {
                    innerFolder.mkdirs();
                }
                List treasurylist = licreportDAO.getDivisionWiseTreasuryList(licbean.getSltDivision());
                for (int i = 0; i < treasurylist.size(); i++) {
                    licdivisionbean = (LICDivisionWiseBean) treasurylist.get(i);

                    String excelname = "LIC_" + licdivisionbean.getTreasuryName() + "_DIVISION_WISE";
                    excelname = excelname.replace(" ", "_");
                    String fileName = excelname + ".xls";

                    out = new FileOutputStream(new File(innerFolder + "/" + fileName));

                    licreportDAO.createExcelLICDivisionWise(out, licdivisionbean.getTreasuryCode(), licbean.getSltYear(), licbean.getSltMonth());
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/LICBulkDivisionWise.htm?sltDivision=" + licbean.getSltDivision();
    }
}
