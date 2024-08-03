/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.controller.payroll.billbrowser.BillBrowserController;
import hrms.dao.master.CensusReportDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Office;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Madhusmita
 */
@Controller
public class CensusReportController implements ServletContextAware {

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Autowired
    OfficeDAO officeDao;

    @Autowired
    CensusReportDAO censusReportDao;
    
    @Autowired
    DistrictDAO districtDAO;

    @RequestMapping("censusReport")
    public ModelAndView getOfficeList(ModelMap model, HttpServletResponse response, @ModelAttribute("censusOffice") Office office) {
        ModelAndView mv = new ModelAndView();

        //List ofclist = officeDao.getDistrictWiseOfficeList("2112");
        mv.addObject("distList", districtDAO.getDistrictList());
        //mv.addObject("officelist", ofclist);
        mv.setViewName("master/CensusReportOfficewise");
        return mv;

    }
     @RequestMapping("distWiseCensusReport.htm")
    public ModelAndView distWiseCensusReport(ModelMap model, HttpServletResponse response, @ModelAttribute("censusOffice") Office office) {
        ModelAndView mv = new ModelAndView();

        List ofclist = officeDao.getDistrictWiseOfficeList(office.getDistCode());
        mv.addObject("distList", districtDAO.getDistrictList());
        mv.addObject("officelist", ofclist);
        mv.addObject("fiyear", office.getFyYear());
        office.setFyYear(office.getFyYear());
        mv.setViewName("master/CensusReportOfficewise");
        return mv;

    }

    @RequestMapping("GenerateCensusReport.htm")
    public void GenerateCensusReport(ModelMap model, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("offCode") String offCode, @RequestParam("ddoCode") String ddoCode,@RequestParam("fiyear")String fiyear) {
        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(response.getOutputStream());
            String fileName = "CencusReport_".concat(ddoCode).concat("_").concat(fiyear) + ".xls";
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            censusReportDao.downloadCensusReportOfficeWise(out, fileName, workbook, offCode,fiyear);

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            //workbook.write();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping("GenerateAllCensusReport.htm")
    public void GenerateAllCensusReport(ModelMap model, HttpServletResponse response) throws IndexOutOfBoundsException, IOException {

        String folderPath = servletContext.getInitParameter("AllCensusReport");
        
        //String folderpath="D:/CensusReport/";
        OutputStream out = null;
        String fileName = null;
        String zipFilename = null;
        String acct_type = null;
        String ddocode = null;
        String officeCode = null;
        File directory = null;
        String fiyear=null;
        try {
            List officeList = officeDao.getDistrictWiseOfficeList("2112");
            Iterator itr = officeList.iterator();
            Office office = null;
            while (itr.hasNext()) {
                office = (Office) itr.next();
                ddocode = office.getDdoCode();
                officeCode = office.getOffCode();
                fileName = "Census_Reports_" + ddocode + ".xls";
                //out = new BufferedOutputStream(response.getOutputStream());
                out = new FileOutputStream(new File(folderPath + fileName));
                WritableWorkbook workbook = Workbook.createWorkbook(out);
                censusReportDao.downloadCensusReportOfficeWise(out, fileName, workbook, officeCode,fiyear);
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
                workbook.close();
            }
            //directory = new File("D:/CensusReport/");
            directory = new File(folderPath);
            String[] files = directory.list();
            zipFilename = "AllCensusReport_Cuttack" + ".zip";
            byte[] zip = zipFilesCensusReport(directory, files);
            OutputStream outst = response.getOutputStream();
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFilename + "\"");
            outst.write(zip);
            outst.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //FileUtils.deleteDirectory(directory);
        }

    }

    private byte[] zipFilesCensusReport(File directory, String[] files) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        byte bytes[] = new byte[2048];
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i];
            FileInputStream fis = new FileInputStream(directory.getPath() + BillBrowserController.FILE_SEPARATOR + fileName);
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
