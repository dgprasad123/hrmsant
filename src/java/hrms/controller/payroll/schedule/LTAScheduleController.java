/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.schedule;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.model.common.CommonReportParamBean;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Surendra
 */
@Controller
public class LTAScheduleController implements ServletContextAware {

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @Autowired
    PayBillDMPDAO paybillDmpDao;

    @RequestMapping(value = "ltaSchduleHTML", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView ltaSchduleHTML() {

        ModelAndView mav = null;
        try {
            mav = new ModelAndView();

            mav.setViewName("/payroll/schedule/LtaScheduleInterface");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "getYearList", method = {RequestMethod.GET, RequestMethod.POST})
    public void getYearList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList yearlist = new ArrayList();
        SelectOption so = null;
        int curyear = 0;
        Calendar cal = Calendar.getInstance();
        curyear = cal.get(Calendar.YEAR);
        for (int i = curyear; i >= 2014; i--) {
            so = new SelectOption();
            so.setLabel(i + "");
            so.setValue(i + "");
            yearlist.add(so);
        }
        JSONArray json = new JSONArray(yearlist);
        out = response.getWriter();
        out.write(json.toString());
    }

    @RequestMapping(value = "RedirectGPFScheduleReport")
    public String RedirectGPFScheduleReport(@RequestParam("billNo") String billNo) {
        
        String path = "";
        
        try {
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            
            if (crb.getTypeofBill().equals("PAY")) {
                path = "redirect:/GPFScheduleHTML.htm?billNo=" + billNo;
            } else if (crb.getTypeofBill().contains("ARREAR")) {
                path = "redirect:/GPFScheduleArrear.htm?billNo=" + billNo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    @RequestMapping(value = "DownloadAGAquitancePDF")
    public void downloadAGAquitancePDF(HttpServletResponse response, @RequestParam("billNo") String billNo, @RequestParam("month") int month, @RequestParam("year") String year, @RequestParam("treasury") String treasury, @RequestParam("majorhead") String majorhead, @RequestParam("vchno") String vchno) {

        ModelAndView mav = new ModelAndView();

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        String fileName = "";
        String downloadfileName = "";
        try {
            outStream = response.getOutputStream();

            String filepath = context.getInitParameter("agdownloadPDFPath") + "/Aquitance/";
            if (year.equals("2019") || (year.equals("2020") && month < 8)) {
                fileName = year + "/" + CommonFunctions.getMonthAsString(month - 1) + "/" + billNo + ".pdf";
                downloadfileName = billNo + ".pdf";
            }else{
                //if ((year.equals("2020") && month >= 8) || year.equals("2021")) {
                vchno = "0" + vchno;
                fileName = year + "/" + CommonFunctions.getMonthAsString(month - 1) + "/" + treasury + "/" + majorhead + "/" + vchno + ".pdf";
                downloadfileName = vchno + ".pdf";
            }

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
        //return mav;
    }
}
