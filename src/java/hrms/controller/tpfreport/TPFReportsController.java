package hrms.controller.tpfreport;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.Numtowordconvertion;
import hrms.dao.master.TreasuryDAO;
import hrms.dao.tpfreport.TPFReportsDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.tpfreport.TPFReportBean;
import hrms.model.tpfreport.TPFReportForm;
import hrms.model.tpfreport.TPFReportList;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("LoginUserBean")
public class TPFReportsController {

    @Autowired
    public TPFReportsDAO tpfreportDAO;

    @Autowired
    TreasuryDAO treasuryDao;

    @Autowired
    private ServletContext servletContext;

    @RequestMapping(value = "TreasuryReportTPF")
    public ModelAndView tpfreportpage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("offCode") String offCode) {
        ModelAndView mav = new ModelAndView();

        mav.addObject("parentTreasuryList", treasuryDao.getParentTreasuryList());

        mav.setViewName("/tpfreport/TPFReport");
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "treasuryDDOListJSON")
    public void GetTreasuryDDOListJSON(HttpServletResponse response, @RequestParam("parentTrCode") String trcode, @RequestParam("subTrCode") String subTrCode) {

        response.setContentType("application/json");
        PrintWriter out = null;

        List ddolist = null;
        try {
            ddolist = tpfreportDAO.getDDOList(trcode, subTrCode);

            JSONArray json = new JSONArray(ddolist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "treasuryGetBillAmtJSON")
    public void GetTreasuryGETBillAmtJSON(HttpServletResponse response, @RequestParam("ddocode") String ddocode, @RequestParam("year") int year, @RequestParam("month") int month) {

        response.setContentType("application/json");
        PrintWriter out = null;

        List amtlist = null;
        try {
            amtlist = tpfreportDAO.getBillAmt(ddocode, month, year);

            JSONArray json = new JSONArray(amtlist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "treasuryGetYearListJSON")
    public void GetTreasuryGetYearListJSON(HttpServletResponse response) {

        response.setContentType("application/json");
        PrintWriter out = null;

        List yearlist = null;
        try {
            yearlist = tpfreportDAO.getYearList();

            JSONArray json = new JSONArray(yearlist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "TPFSchedulePaymentReport")
    public String TPFSchedulePaymentReport(Model model, @RequestParam("billNo") String billNo) {

        int totalAmt = 0;

        TPFReportBean tbean = null;
        TPFReportList tlist = null;
        try {
            tbean = tpfreportDAO.getBillDtls(billNo);

            model.addAttribute("billData", tbean);

            List paymentList = tpfreportDAO.getPaymentList(billNo);
            Iterator itr = paymentList.iterator();
            if (itr.hasNext()) {
                tlist = (TPFReportList) itr.next();
                totalAmt = totalAmt + tlist.getAmount();
            }

            model.addAttribute("paymentList", paymentList);
            model.addAttribute("totalAmt", totalAmt);
            model.addAttribute("totalAmtinWords", Numtowordconvertion.convertNumber(totalAmt));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/tpfreport/TPFPaymentReport";
    }

    @RequestMapping(value = "SearchTPFReportByChallanNo")
    public String searchTPFReportByChallanNo(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") TPFReportForm tpfForm) {

        model.addAttribute("parentTreasuryList", treasuryDao.getParentTreasuryList());

        return "/tpfreport/TPFReportByChallanNo";

    }

    @RequestMapping(value = "TPFReportByChallanNoList")
    public ModelAndView TPFReportByChallanNoList(@ModelAttribute("command") TPFReportForm tpfForm) {

        ModelAndView mav = new ModelAndView();

        mav.addObject("parentTreasuryList", treasuryDao.getParentTreasuryList());

        ArrayList subTreasuryList = treasuryDao.getSubTreasuryList(tpfForm.getSltParentTreasury());
        mav.addObject("subTreasuryList", subTreasuryList);

        ArrayList tpfList = tpfreportDAO.getTPFListFromChallanNo(tpfForm.getTxtChallanNo(), tpfForm.getTxtChallanDate(), tpfForm.getSltParentTreasury(), tpfForm.getSltSubTreasury());
        mav.addObject("tpfList", tpfList);

        double grandTotal = 0;

        if (tpfList != null && tpfList.size() > 0) {
            TPFReportBean tpfbean = null;
            for (int i = 0; i < tpfList.size(); i++) {
                tpfbean = (TPFReportBean) tpfList.get(i);
                grandTotal = grandTotal + tpfbean.getTotalReleased();
            }
        }
        mav.addObject("totalAmount", grandTotal);

        mav.addObject("officeName", tpfreportDAO.getOfficeNameFromChallanNo(tpfForm.getTxtChallanNo(), tpfForm.getTxtChallanDate(), tpfForm.getSltParentTreasury(), tpfForm.getSltSubTreasury()));

        mav.setViewName("/tpfreport/TPFReportByChallanNo");

        return mav;
    }

    @RequestMapping(value = "DownloadTPFPDFCOAPage")
    public ModelAndView downloadTPFPDFCOAPage(@ModelAttribute("command") TPFReportForm tpfForm) {

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
            mav.setViewName("/tpfreport/DownloadTPFPDFCOA");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "DownloadTPFPDFCOA")
    public ModelAndView downloadTPFPDFCOA(HttpServletResponse response, @ModelAttribute("command") TPFReportForm tpfForm) {

        ModelAndView mav = new ModelAndView();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        FileInputStream fis = null;
        BufferedInputStream bis = null;

        byte bytes[] = new byte[2048];

        String zipFilename = tpfForm.getSltTrCode() + "_" + tpfForm.getSltYear() + "_" + CommonFunctions.getMonthAsString(Integer.parseInt(tpfForm.getSltMonth())) + ".zip";

        try {
            String tpffilepath = servletContext.getInitParameter("TPFSchedulePDFPath");

            File srcfiles = new File(tpffilepath + tpfForm.getSltYear() + "/" + CommonFunctions.getMonthAsString(Integer.parseInt(tpfForm.getSltMonth())) + "/" + tpfForm.getSltTrCode());
            //System.out.println(tpffilepath + tpfForm.getSltYear() + "\\" + CommonFunctions.getMonthAsString(Integer.parseInt(tpfForm.getSltMonth())) + "\\" + tpfForm.getSltTrCode());
            File[] files = srcfiles.listFiles();
            for (int i = 0; i < files.length; i++) {

                fis = new FileInputStream(files[i]);
                bis = new BufferedInputStream(fis);
                zos.putNextEntry(new ZipEntry(files[i].getName()));
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

            OutputStream out = response.getOutputStream();
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFilename + "\"");
            out.write(baos.toByteArray());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        mav.setViewName("/tpfreport/DownloadTPFPDFCOA");
        return mav;
    }
}
