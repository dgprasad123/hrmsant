/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.schedule;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.CommonFunctions;
import hrms.common.Numtowordconvertion;
import hrms.dao.payroll.aqmast.AqmastDAO;
import hrms.dao.payroll.billbrowser.AqReportDAOImpl;
import hrms.dao.payroll.billbrowser.BillBrowserDAO;
import hrms.dao.payroll.schedule.BillFrontpageDAO;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduleDAOImpl;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.billbrowser.AllowDeductDetails;
import hrms.model.payroll.billbrowser.BillChartOfAccount;
import hrms.model.payroll.billbrowser.BillObjectHead;
import hrms.model.payroll.schedule.OtcPlanForm40Bean;
import hrms.model.payroll.schedule.Schedule;
import hrms.model.payroll.schedule.ScheduleHelper;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class OTCForm40Controller implements ServletContextAware {

    @Autowired
    public ScheduleDAOImpl comonScheduleDao;

    @Autowired
    public PayBillDMPDAO paybillDmpDao;

    @Autowired
    BillFrontpageDAO billFrontPageDmpDao;

    @Autowired
    AqmastDAO aqMastDao;

    @Autowired
    BillBrowserDAO billBrowserDao;

    @Autowired
    AqReportDAOImpl aqReportDAO;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "OTCForm40HTML", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView OTCForm40HTML(@RequestParam("billNo") String billNo) {

        ModelAndView mav = null;
        List empList = null;
        List alowanceList = null;
        List newAlowanceList = new ArrayList();
        List deductList = null;
        String pageBreak = null;
        try {
            mav = new ModelAndView();

            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            empList = comonScheduleDao.getOTCForm40ScheduleEmpList(billNo, crb.getAqyear(), crb.getAqmonth());

            ScheduleHelper obj = null;
            if (empList != null && empList.size() > 0) {
                obj = new ScheduleHelper();
                for (int i = 0; i < empList.size(); i++) {
                    obj = (ScheduleHelper) empList.get(i);

                    alowanceList = obj.getAllowanceList();
                    deductList = obj.getDeductionList();
                    pageBreak = obj.getPagebreakOTC();
                }
            }
            int totgp = 0;
            OtcPlanForm40Bean otc40Obj = null;
            for (int i = 0; i < alowanceList.size(); i++) {
                otc40Obj = (OtcPlanForm40Bean) alowanceList.get(i);
                String adCode = otc40Obj.getAdCode();

                if (adCode.equals("GP")) {
                    totgp = totgp + otc40Obj.getAdAmt();
                } else if (!adCode.equals("GP")) {
                    OtcPlanForm40Bean otcObj = new OtcPlanForm40Bean();

                    otcObj.setAdAmt(otc40Obj.getAdAmt());
                    otcObj.setBtId(otc40Obj.getBtId());
                    otcObj.setNowDedn(otc40Obj.getNowDedn());
                    otcObj.setAdCode(otc40Obj.getAdCode());
                    newAlowanceList.add(otcObj);
                }
            }

            OtcPlanForm40Bean otcBean = comonScheduleDao.getOTCForm40ScheduleDetails(billNo, crb.getAqyear(), crb.getAqmonth(), totgp, crb);
            String payHead = billFrontPageDmpDao.getPayHead(Integer.parseInt(billNo));

            mav.addObject("Otc40Header", otcBean);
            mav.addObject("PayHead", payHead);
            mav.addObject("AlowanceList", newAlowanceList);
            mav.addObject("DeductList", deductList);
            mav.addObject("pBreak", pageBreak);
            mav.setViewName("/payroll/schedule/OTC40");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "ArrOTCForm40HTML")
    public String ArrOTCForm40HTML(ModelMap model,@RequestParam("billNo") String billNo) {

        double payamtTot = 0;
        double cpfAmt = 0.0;
        double itAmt = 0.0;
        double ptAmt = 0.0;
        double deductAmt = 0;
        double netAmt = 0;

        String payAmt = "";
        String daAmt = "";
        String hraAmt = "";
        String oaAmt = "";
        
        String gross = "";
        String netTotal = "";
        String netTotalUnder = "";
        
        String monthName1 = "";
        String monthName2 = "";
        int year1;
        int year2;
        
        try {
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);

            monthName1 = CommonFunctions.getMonthAsString(crb.getFromMonth() - 1);
            year1 = crb.getFromYear();
            monthName2 = CommonFunctions.getMonthAsString(crb.getToMonth() - 1);
            year2 = crb.getToYear();

            if (crb.getTypeofBill() != null && crb.getTypeofBill().equals("ARREAR")) {

            } else if (crb.getTypeofBill() != null && crb.getTypeofBill().equalsIgnoreCase("OTHER_ARREAR")) {

                OtcPlanForm40Bean otcBean = comonScheduleDao.getArrOTCForm40ScheduleDetails(billNo, crb.getAqyear(), crb.getAqmonth(), crb);

                List allowanceList = billBrowserDao.getAllowanceListForArrear(Integer.parseInt(billNo), crb.getAqmonth(), crb.getAqyear());
                for (int i = 0; i < allowanceList.size(); i++) {
                    AllowDeductDetails all = (AllowDeductDetails) allowanceList.get(i);

                    if (all.getObjecthead() != null && !all.getObjecthead().equals("")) {
                        payamtTot = payamtTot + Double.valueOf(all.getAdamount());
                        if (all.getObjecthead().equals("136")) {
                            if (all.getAdamount() != null && !all.getAdamount().equals("")) {
                                if (payAmt != null && !payAmt.equals("")) {
                                    payAmt = (Integer.parseInt(payAmt) + Integer.parseInt(all.getAdamount())) + "";
                                } else {
                                    payAmt = Integer.parseInt(all.getAdamount()) + "";
                                }
                            }
                        } else if (all.getObjecthead().equals("523")) {
                            oaAmt = all.getAdamount();
                        } else if (all.getObjecthead().equals("156")) {
                            daAmt = all.getAdamount();
                        } else if (all.getObjecthead().equals("403")) {
                            hraAmt = all.getAdamount();
                        }
                    }
                }
                
                Schedule obj = aqReportDAO.getArrScheduleListWithADCode(billNo);
                if (obj.getCpfAmt() != null && !obj.getCpfAmt().equals("")) {
                    cpfAmt = Double.parseDouble(obj.getCpfAmt());
                }
                if (obj.getPtAmt() != null && !obj.getPtAmt().equals("")) {
                    ptAmt = Double.parseDouble(obj.getPtAmt());
                }
                if (obj.getItAmt() != null && !obj.getItAmt().equals("")) {
                    itAmt = Double.parseDouble(obj.getItAmt());
                }
                
                deductAmt = cpfAmt + ptAmt + itAmt;
                
                gross = Double.valueOf(payamtTot + "").longValue() + "";
                
                netAmt = payamtTot - deductAmt;
                netTotal = Double.valueOf(netAmt + "").longValue() + "";
                
                model.addAttribute("Otc40Header", otcBean);
                
                model.addAttribute("payAmt", payAmt);
                model.addAttribute("daAmt", daAmt);
                model.addAttribute("hraAmt", hraAmt);
                model.addAttribute("oaAmt", oaAmt);
            
                model.addAttribute("grossTotal", gross);
                model.addAttribute("grandTotinWord", Numtowordconvertion.convertNumber(new Double(payamtTot).intValue()).toUpperCase());
                
                model.addAttribute("deductTot", deductAmt);
                
                model.addAttribute("cpfAmt", obj.getCpfAmt());
                model.addAttribute("ptAmt", obj.getPtAmt());
                model.addAttribute("itAmt", obj.getItAmt());
                model.addAttribute("itObjHead", obj.getItObjHead());
                model.addAttribute("cpfObjHead", obj.getCpfObjHead());
                model.addAttribute("ptObjHead", obj.getPtObjHead());

                model.addAttribute("totNetAmt", netTotal);
                
                netTotalUnder = Double.valueOf((netAmt + 1) + "").longValue() + "";
                model.addAttribute("totNetAmtUnder", netTotalUnder);
                model.addAttribute("totNetAmtUnderInWord", Numtowordconvertion.convertNumber(new Double(netTotalUnder).intValue()).toUpperCase());
            }
            model.addAttribute("billtype", crb.getTypeofBill());
            model.addAttribute("billtypeRegGrant", crb.getBillType());
            
            model.addAttribute("monthName1", monthName1);
            model.addAttribute("year1", year1);
            model.addAttribute("monthName2", monthName2);
            model.addAttribute("year2", year2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/payroll/arrear/ArrOTC40";
    }

    @RequestMapping(value = "OTCForm40PDF", method = {RequestMethod.POST, RequestMethod.GET})
    public void OTCForm40Pdf(HttpServletResponse response, @RequestParam("billNo") String billNo) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        List alowanceList = null;
        List deductList = null;
        List newAlowanceList = new ArrayList();
        try {
            response.setHeader("Content-Disposition", "attachment; filename=OTC40Schedule_" + billNo + ".pdf"); // OTC Form 40
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            List empList = comonScheduleDao.getOTCForm40ScheduleEmpList(billNo, crb.getAqyear(), crb.getAqmonth());

            ScheduleHelper obj = null;
            if (empList != null && empList.size() > 0) {
                obj = new ScheduleHelper();
                for (int i = 0; i < empList.size(); i++) {
                    obj = (ScheduleHelper) empList.get(i);

                    alowanceList = obj.getAllowanceList();
                    deductList = obj.getDeductionList();
                }
            }
            int totgp = 0;
            OtcPlanForm40Bean otc40Obj = null;
            for (int i = 0; i < alowanceList.size(); i++) {
                otc40Obj = (OtcPlanForm40Bean) alowanceList.get(i);
                String adCode = otc40Obj.getAdCode();

                if (adCode.equals("GP")) {
                    totgp = totgp + otc40Obj.getAdAmt();
                } else if (!adCode.equals("GP")) {
                    OtcPlanForm40Bean otcObj = new OtcPlanForm40Bean();

                    otcObj.setAdAmt(otc40Obj.getAdAmt());
                    otcObj.setBtId(otc40Obj.getBtId());
                    otcObj.setNowDedn(otc40Obj.getNowDedn());
                    otcObj.setAdCode(otc40Obj.getAdCode());
                    newAlowanceList.add(otcObj);
                }
            }

            OtcPlanForm40Bean otcBean = comonScheduleDao.getOTCForm40ScheduleDetails(billNo, crb.getAqyear(), crb.getAqmonth(), totgp, crb);
            String payHead = billFrontPageDmpDao.getPayHead(Integer.parseInt(billNo));

            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            comonScheduleDao.OtcForm40SchedulePDF(document, billNo, otcBean, payHead, newAlowanceList, deductList);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}
