/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.billbrowser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.CalendarCommonMethods;
import hrms.common.CommonFunctions;
import hrms.common.Numtowordconvertion;
import hrms.dao.esignBill.EsignBillDAO;
import hrms.dao.payroll.aqmast.AqmastDAO;
import hrms.dao.payroll.billbrowser.AqReportDAO;
import hrms.dao.payroll.billbrowser.BillBrowserDAO;
import hrms.dao.payroll.schedule.BillFrontpageDAO;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.billbrowser.AllowDeductDetails;
import hrms.model.payroll.billbrowser.BillBrowserbean;
import hrms.model.payroll.billbrowser.BillChartOfAccount;
import hrms.model.payroll.billbrowser.BillObjectHead;
import hrms.model.payroll.schedule.GPFScheduleBean;
import hrms.model.payroll.schedule.Schedule;
import hrms.services.SalaryBillService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author Manas Jena
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpOffice"})
public class BillFrontPageController {

    @Autowired
    AqReportDAO aqReportDao;

    @Autowired
    BillFrontpageDAO billFrontPageDmpDao;

    @Autowired
    AqmastDAO aqMastDao;

    @Autowired
    ScheduleDAO comonScheduleDao;

    @Autowired
    BillBrowserDAO billBrowserDao;

    @Autowired
    public PayBillDMPDAO paybillDmpDao;

    @Autowired
    SalaryBillService salaryBillService;

    @Autowired
    EsignBillDAO esignBillDao;

    @RequestMapping(value = "billfrontPageHTML", method = RequestMethod.GET)
    public String billfrontPageHTML(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbBean,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("billNo") String billNo, BindingResult result, HttpServletResponse response) {
        CommonReportParamBean crb = null;
        String path = "";
        BillChartOfAccount billChartOfAccount = null;
        crb = paybillDmpDao.getCommonReportParameter(billNo);
        BillObjectHead boha = new BillObjectHead();
        ArrayList scheduleList = new ArrayList();
        boolean isDDODHe = false;
        List oaList = null;
        double oaAmt = 0.0;
        double deductAmt = 0.0;
        int spAmt = 0;
        int irAmt = 0;
        Schedule obj = null;

        String netTotaUnder = "";
        String amtTobeDisplay = "";
        String payHead = "";
        String totDeductAmt = "";
        String netAmount = "";
        String totOaAmtInString = "";
        String totNetAmtInString = "";
        String monthName = "";
        List oaListinFIFO = new ArrayList();

        String arrAll = "";
        //--For DHE--Start
        isDDODHe = billBrowserDao.isDDODHE(lub.getLoginoffcode());
        //--End--
        if (crb.getTypeofBill().contains("ARREAR")) {
            path = "/payroll/arrear/ArrBillFrontPage";

            double cpfAmt = 0.0;
            double itAmt = 0.0;
            double ptAmt = 0.0;
            double orAmt = 0.0;
            double drAmt = 0.0;

            String monthNam1 = "";
            String monthName2 = "";
            int year1;
            int year2;

            String payAmt = "";
            String daAmt = "";
            String hraAmt = "";
            String oaHeadAmt = "";

            monthNam1 = CommonFunctions.getMonthAsString(crb.getFromMonth() - 1);
            year1 = crb.getFromYear();
            monthName2 = CommonFunctions.getMonthAsString(crb.getToMonth() - 1);
            year2 = crb.getToYear();

            billChartOfAccount = aqReportDao.getBillChartOfAccount(billNo);
            boha = aqReportDao.getArrBillObjectHeadAndAmt(billNo, crb.getTypeofBill());
            payHead = billFrontPageDmpDao.getPayHead(Integer.parseInt(bbBean.getBillNo()));
            if (crb.getTypeofBill().equalsIgnoreCase("ARREAR_6") || crb.getTypeofBill().equalsIgnoreCase("ARREAR") || crb.getTypeofBill().equalsIgnoreCase("ARREAR_NPS") || crb.getTypeofBill().equalsIgnoreCase("ARREAR_J") || crb.getTypeofBill().equalsIgnoreCase("ARREAR_6_J")) {

                if (payHead.equalsIgnoreCase("136")) {
                    payHead = "855";
                } else if (payHead.equalsIgnoreCase("921")) {
                    payHead = "921";
                } else if (payHead.equalsIgnoreCase("000")) {
                    payHead = "000";
                }

                oaAmt = boha.getPayamt();
                arrAll = Double.valueOf(oaAmt + "").longValue() + "";

            } else {

                List liAllowance = billBrowserDao.getAllowanceListForArrear(Integer.parseInt(bbBean.getBillNo()), crb.getAqmonth(), crb.getAqyear());
                double payamtTot = 0;
                for (int i = 0; i < liAllowance.size(); i++) {
                    AllowDeductDetails all = (AllowDeductDetails) liAllowance.get(i);

                    if (all.getObjecthead() != null && !all.getObjecthead().equals("")) {
                        payamtTot = payamtTot + Double.valueOf(all.getAdamount());
                        if (all.getObjecthead().equals("136") || all.getObjecthead().equals("000") || all.getObjecthead().equals("921") || all.getObjecthead().equals("401") || all.getObjecthead().equals("506") || all.getObjecthead().equals("855")) {
                            if (all.getAdamount() != null && !all.getAdamount().equals("")) {
                                if (payAmt != null && !payAmt.equals("")) {
                                    payAmt = (Integer.parseInt(payAmt) + Integer.parseInt(all.getAdamount())) + "";
                                } else {
                                    payAmt = Integer.parseInt(all.getAdamount()) + "";
                                }

                            }

                        } else if (all.getObjecthead().equals("523")) {
                            oaHeadAmt = all.getAdamount();
                        } else if (all.getObjecthead().equals("156")) {
                            daAmt = all.getAdamount();
                        } else if (all.getObjecthead().equals("403")) {
                            hraAmt = all.getAdamount();
                        }
                    }
                }

                Schedule sc2 = new Schedule();
                sc2.setObjectHead(payHead);
                sc2.setScheduleName("- " + "ARREAR PAY");
                sc2.setSchAmount(payAmt);
                if (payAmt != null && !payAmt.equals("") && Integer.parseInt(payAmt) != 0) {
                    oaListinFIFO.add(sc2);
                }

                sc2 = new Schedule();
                sc2.setObjectHead("523");
                sc2.setScheduleName("- " + "OA");
                sc2.setSchAmount(oaHeadAmt);
                if (oaHeadAmt != null && !oaHeadAmt.equals("") && Integer.parseInt(oaHeadAmt) != 0) {
                    oaListinFIFO.add(sc2);
                }

                sc2 = new Schedule();
                sc2.setObjectHead("156");
                sc2.setScheduleName("- " + "DA");
                sc2.setSchAmount(daAmt);

                if (daAmt != null && !daAmt.equals("") && Integer.parseInt(daAmt) != 0) {
                    oaListinFIFO.add(sc2);

                }

                sc2 = new Schedule();
                sc2.setObjectHead("403");
                sc2.setScheduleName("- " + "HRA");
                sc2.setSchAmount(hraAmt);

                if (hraAmt != null && !hraAmt.equals("") && Integer.parseInt(hraAmt) != 0) {
                    oaListinFIFO.add(sc2);
                }

                oaAmt = payamtTot;
            }

            obj = aqReportDao.getArrScheduleListWithADCode(billNo);

            if (obj.getCpfAmt() != null && !obj.getCpfAmt().equals("")) {
                cpfAmt = Double.parseDouble(obj.getCpfAmt());
            }
            if (obj.getPtAmt() != null && !obj.getPtAmt().equals("")) {
                ptAmt = Double.parseDouble(obj.getPtAmt());
            }
            if (obj.getItAmt() != null && !obj.getItAmt().equals("")) {
                itAmt = Double.parseDouble(obj.getItAmt());
            }
            if (obj.getOrAmt() != null && !obj.getOrAmt().equals("")) {
                orAmt = Double.parseDouble(obj.getOrAmt());
            }
            /*if (obj.getDrAmt() != null && !obj.getDrAmt().equals("")) {
             drAmt = Double.parseDouble(obj.getDrAmt());
             }*/

            Schedule sc = new Schedule();
            sc.setObjectHead(obj.getItObjHead());
            sc.setScheduleName("- " + "IT");
            sc.setSchAmount(obj.getItAmt());
            if (obj.getItAmt() != null) {
                scheduleList.add(sc);
            }

            sc = new Schedule();
            sc.setObjectHead(obj.getPtObjHead());
            sc.setScheduleName("- " + "PT");
            sc.setSchAmount(obj.getPtAmt());
            if (obj.getPtAmt() != null) {
                scheduleList.add(sc);
            }

            sc = new Schedule();
            sc.setObjectHead(obj.getOrObjHead());
            sc.setScheduleName("- " + "OR");
            sc.setSchAmount(obj.getOrAmt());
            if (obj.getOrAmt() != null) {
                scheduleList.add(sc);
            }

            /*sc = new Schedule();
             sc.setObjectHead(obj.getDrObjHead());
             sc.setScheduleName("- " + "DR");
             sc.setSchAmount(obj.getDrAmt());
             if (obj.getDrAmt() != null) {
             scheduleList.add(sc);
             }*/
            sc = new Schedule();
            sc.setObjectHead(obj.getCpfObjHead());

            sc.setSchAmount(obj.getCpfAmt());
            if (obj.getCpfAmt() != null) {
                sc.setScheduleName("- " + obj.getAcctType());
                scheduleList.add(sc);
            }

            deductAmt = cpfAmt + ptAmt + itAmt + orAmt;
            totDeductAmt = String.valueOf(deductAmt);// CommonFunctions.getRupessAndPaise(String.valueOf(deductAmt));
            double netAmt = oaAmt - deductAmt;

            if (crb.getTypeofBill().equalsIgnoreCase("ARREAR_NPS")) {
                netAmt = 0;
                oaAmt = Double.parseDouble(obj.getCpfAmt());
            }

            netTotaUnder = Double.valueOf(netAmt + "").longValue() + "";
            netAmount = String.valueOf(netAmt); //CommonFunctions.getRupessAndPaise(String.valueOf(netAmt));
            totOaAmtInString = Double.valueOf(oaAmt + "").longValue() + "";
            totNetAmtInString = Double.valueOf(netAmt + "").longValue() + "";

            model.addAttribute("monthNam1", monthNam1);
            model.addAttribute("year1", year1);
            model.addAttribute("monthName2", monthName2);
            model.addAttribute("year2", year2);

        } else {

            path = "/payroll/FrontPage";
            //--For DHE--Start
            if (isDDODHe) {
                billChartOfAccount = aqReportDao.getBillChartOfAccountDHE(bbBean.getBillNo());
            } //--End--
            else {
                billChartOfAccount = aqReportDao.getBillChartOfAccount(bbBean.getBillNo());
            }

            boha = aqReportDao.getBillObjectHeadAndAmt(bbBean.getBillNo(), crb.getAqyear(), crb.getAqmonth());

            scheduleList = billFrontPageDmpDao.getScheduleListWithADCode(bbBean.getBillNo(), crb.getAqmonth(), crb.getAqyear());

            if (scheduleList != null && scheduleList.size() > 0) {
                for (int i = 0; i < scheduleList.size(); i++) {
                    obj = (Schedule) scheduleList.get(i);
                    double schAmt = Double.parseDouble(obj.getSchAmount());
                    deductAmt = deductAmt + schAmt;
                }
            }

            oaList = aqReportDao.getAllowanceDetails(bbBean.getBillNo(), crb.getAqyear(), crb.getAqmonth());

            Schedule obj1 = null;
            int amt = 0;

            payHead = billFrontPageDmpDao.getPayHead(Integer.parseInt(bbBean.getBillNo()));

            spAmt = billFrontPageDmpDao.getSpecialPayAmount(Integer.parseInt(billNo), crb.getAqmonth(), crb.getAqyear());
            irAmt = billFrontPageDmpDao.getIrAmount(Integer.parseInt(billNo), crb.getAqmonth(), crb.getAqyear());

            int payAmt = aqReportDao.getPayAmt(Integer.parseInt(billNo));

            if (oaList != null && oaList.size() > 0) {
                for (int i = 0; i < oaList.size(); i++) {
                    Schedule sc2 = new Schedule();
                    obj1 = (Schedule) oaList.get(i);

                    if (obj1.getObjectHead() != null && obj1.getObjectHead().equals("136")) {
                        amt = amt + Integer.parseInt(obj1.getSchAmount());

                    }
                    if (obj1.getObjectHead() != null && obj1.getObjectHead().equals("000")) {
                        amt = amt + Integer.parseInt(obj1.getSchAmount());

                    }

                }
            }

            payAmt = payAmt + amt;
            amtTobeDisplay = String.valueOf(payAmt);

            Schedule sc = new Schedule();
            sc.setObjectHead(payHead);
            sc.setScheduleName("- " + "PAY");
            sc.setSchAmount(amtTobeDisplay);
            if (amtTobeDisplay != null) {
                oaListinFIFO.add(sc);
            }

            sc = new Schedule();
            sc.setObjectHead(payHead);
            sc.setScheduleName("- " + "IR");
            sc.setSchAmount(irAmt + "");
            if (irAmt > 0) {
                oaListinFIFO.add(sc);
            }
            sc = new Schedule();
            sc.setObjectHead(payHead);
            sc.setScheduleName("- " + "SP");

            sc.setSchAmount(spAmt + "");
            if (spAmt > 0) {
                oaListinFIFO.add(sc);
            }

            if (oaList != null && oaList.size() > 0) {
                for (int i = 0; i < oaList.size(); i++) {
                    Schedule sc2 = new Schedule();
                    obj1 = (Schedule) oaList.get(i);

                    if (obj1.getObjectHead() != null && obj1.getObjectHead().equals("136")) {
                        //amt = amt + Integer.parseInt(obj1.getSchAmount());

                    }
                    if (obj1.getObjectHead() != null && obj1.getObjectHead().equals("000")) {
                        //amt = amt + Integer.parseInt(obj1.getSchAmount());

                    }

                    if (!obj1.getObjectHead().equals("000") && !obj1.getObjectHead().equals("136")) {
                        sc2.setSchAmount(obj1.getSchAmount());
                        sc2.setObjectHead(obj1.getObjectHead());
                        sc2.setScheduleName(obj1.getScheduleName());
                        oaListinFIFO.add(sc2);
                    }
                    oaAmt = obj1.getAlowanceTotal();

                }
            }

            oaAmt = payAmt + spAmt + irAmt + (oaAmt - amt);

            totDeductAmt = String.valueOf(deductAmt);// CommonFunctions.getRupessAndPaise(String.valueOf(deductAmt));
            double netAmt = oaAmt - deductAmt;

            netTotaUnder = Double.valueOf(netAmt + "").longValue() + "";
            netAmount = String.valueOf(netAmt); //CommonFunctions.getRupessAndPaise(String.valueOf(netAmt));
            totOaAmtInString = Double.valueOf(oaAmt + "").longValue() + "";
            totNetAmtInString = Double.valueOf(netAmt + "").longValue() + "";

            monthName = CalendarCommonMethods.getFullMonthAsString(crb.getAqmonth());
            monthName = monthName + " - " + crb.getAqyear();

        }
        System.out.println("totOaAmtInString::" + totOaAmtInString);

        model.addAttribute("payAmt", arrAll);
        model.addAttribute("payHead", payHead);
        model.addAttribute("TotOaAmt", totOaAmtInString);
        model.addAttribute("billMonth", monthName);
        model.addAttribute("billChartOfAccount", billChartOfAccount);
        model.addAttribute("OAList", oaListinFIFO);

        model.addAttribute("TotDeductAmt", Double.valueOf(totDeductAmt).longValue());
        model.addAttribute("scheduleList", scheduleList);
        model.addAttribute("TotNetAmt", Double.valueOf(netTotaUnder).longValue());
        model.addAttribute("billtype", crb.getTypeofBill());

        return path;
    }

    @RequestMapping(value = "billFrontPagePDF")
    public void BillFrontPagePDF(HttpServletResponse response, @ModelAttribute("BillBrowserbean") BillBrowserbean bbBean, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            response.setHeader("Content-Disposition", "attachment; filename=BillFrontPage_" + bbBean.getBillNo() + ".pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(bbBean.getBillNo());
            BillChartOfAccount billChartOfAccount = aqReportDao.getBillChartOfAccount(bbBean.getBillNo());
            BillObjectHead boha = aqReportDao.getBillObjectHeadAndAmt(bbBean.getBillNo(), crb.getAqyear(), crb.getAqmonth());
            ArrayList scheduleList = billFrontPageDmpDao.getScheduleListWithADCode(bbBean.getBillNo(), crb.getAqmonth(), crb.getAqyear());
            List oaList = aqReportDao.getAllowanceDetails(bbBean.getBillNo(), crb.getAqyear(), crb.getAqmonth());

            int spAmt = billFrontPageDmpDao.getSpecialPayAmount(Integer.parseInt(bbBean.getBillNo()), crb.getAqmonth(), crb.getAqyear());
            int irAmt = billFrontPageDmpDao.getIrAmount(Integer.parseInt(bbBean.getBillNo()), crb.getAqmonth(), crb.getAqyear());
            int payAmt = aqReportDao.getPayAmt(Integer.parseInt(bbBean.getBillNo()));
            String payHead = billFrontPageDmpDao.getPayHead(Integer.parseInt(bbBean.getBillNo()));

            String monthName = CalendarCommonMethods.getFullMonthAsString(crb.getAqmonth());
            monthName = monthName + " - " + crb.getAqyear();

            comonScheduleDao.billFrontPagePDF(document, monthName, bbBean.getBillNo(), billChartOfAccount, boha, scheduleList, oaList, spAmt, irAmt, payAmt, payHead);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "ArrearBillFrontPagePDF")
    public void ArrearBillFrontPagePDF(HttpServletResponse response, @ModelAttribute("BillBrowserbean") BillBrowserbean bbBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        double allowanceAmt = 0.0;
        double deductAmt = 0.0;

        List allowanceList = new ArrayList();
        ArrayList deductionList = new ArrayList();

        double payamtTot = 0;

        double payAmt = 0;
        String daAmt = "";
        String hraAmt = "";
        String oaHeadAmt = "";

        double cpfAmt = 0.0;
        double itAmt = 0.0;
        double ptAmt = 0.0;
        double orAmt = 0.0;

        String allowEsignStatus = "";
        String createpathPDF = "";
        String unsignedPdfFilename = "";
        try {
            //response.setHeader("Content-Disposition", "attachment; filename=ArrearBillFrontPage_" + bbBean.getBillNo() + ".pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(bbBean.getBillNo());

            BillChartOfAccount billChartOfAccount = aqReportDao.getBillChartOfAccount(bbBean.getBillNo());

            BillObjectHead boha = aqReportDao.getArrBillObjectHeadAndAmt(bbBean.getBillNo(), crb.getTypeofBill());

            Schedule deductionobj = aqReportDao.getArrScheduleListWithADCode(bbBean.getBillNo());

            int spAmt = billFrontPageDmpDao.getSpecialPayAmount(Integer.parseInt(bbBean.getBillNo()), crb.getAqmonth(), crb.getAqyear());
            int irAmt = billFrontPageDmpDao.getIrAmount(Integer.parseInt(bbBean.getBillNo()), crb.getAqmonth(), crb.getAqyear());
            //int payAmt = aqReportDao.getPayAmt(Integer.parseInt(bbBean.getBillNo()));
            String payHead = billFrontPageDmpDao.getPayHead(Integer.parseInt(bbBean.getBillNo()));
            if (crb.getTypeofBill().equalsIgnoreCase("ARREAR_6") || crb.getTypeofBill().equalsIgnoreCase("ARREAR") || crb.getTypeofBill().equalsIgnoreCase("ARREAR_NPS") || crb.getTypeofBill().equalsIgnoreCase("ARREAR_J") || crb.getTypeofBill().equalsIgnoreCase("ARREAR_6_J")) {
                if (payHead.equalsIgnoreCase("136") || payHead.equalsIgnoreCase("855")) {
                    payHead = "855";
                } else if (payHead.equalsIgnoreCase("921")) {
                    payHead = "921";
                } else if (payHead.equalsIgnoreCase("000")) {
                    payHead = "000";
                }

                allowanceAmt = boha.getPayamt();
            } else {
                List allowanceTempList = billBrowserDao.getAllowanceListForArrear(Integer.parseInt(bbBean.getBillNo()), crb.getAqmonth(), crb.getAqyear());
                for (int i = 0; i < allowanceTempList.size(); i++) {
                    AllowDeductDetails all = (AllowDeductDetails) allowanceTempList.get(i);
                    payamtTot = payamtTot + Double.valueOf(all.getAdamount());
                    if (all.getObjecthead() != null && !all.getObjecthead().equals("")) {
                        if (all.getObjecthead().equals("136") || all.getObjecthead().equals("000") || all.getObjecthead().equals("921") || all.getObjecthead().equals("401") || all.getObjecthead().equals("506") || all.getObjecthead().equals("855")) {
                            if (all.getAdamount() != null && !all.getAdamount().equals("")) {
                                //if (payAmt > 0) {
                                    payAmt = (payAmt + Integer.parseInt(all.getAdamount()));
                                //} else {
                                    //payAmt = Integer.parseInt(all.getAdamount());
                                //}
                            }
                        } else if (all.getObjecthead().equals("523")) {
                            oaHeadAmt = all.getAdamount();
                        } else if (all.getObjecthead().equals("156")) {
                            daAmt = all.getAdamount();
                        } else if (all.getObjecthead().equals("403")) {
                            hraAmt = all.getAdamount();
                        }
                    }
                }

                Schedule sc2 = new Schedule();
                sc2.setObjectHead(payHead);
                sc2.setScheduleName("- " + "ARREAR PAY");
                sc2.setSchAmount(Double.valueOf(payAmt + "").longValue()+"");
                if (payAmt > 0) {
                    allowanceList.add(sc2);
                }

                sc2 = new Schedule();
                sc2.setObjectHead("523");
                sc2.setScheduleName("- " + "OA");
                sc2.setSchAmount(oaHeadAmt);
                if (oaHeadAmt != null && !oaHeadAmt.equals("") && Integer.parseInt(oaHeadAmt) != 0) {
                    allowanceList.add(sc2);
                }

                sc2 = new Schedule();
                sc2.setObjectHead("156");
                sc2.setScheduleName("- " + "DA");
                sc2.setSchAmount(daAmt);
                if (daAmt != null && !daAmt.equals("") && Integer.parseInt(daAmt) != 0) {
                    allowanceList.add(sc2);
                }

                sc2 = new Schedule();
                sc2.setObjectHead("403");
                sc2.setScheduleName("- " + "HRA");
                sc2.setSchAmount(hraAmt);
                if (hraAmt != null && !hraAmt.equals("") && Integer.parseInt(hraAmt) != 0) {
                    allowanceList.add(sc2);
                }
                allowanceAmt = payamtTot;
            }

            if (deductionobj.getCpfAmt() != null && !deductionobj.getCpfAmt().equals("")) {
                cpfAmt = Double.parseDouble(deductionobj.getCpfAmt());
            }
            if (deductionobj.getPtAmt() != null && !deductionobj.getPtAmt().equals("")) {
                ptAmt = Double.parseDouble(deductionobj.getPtAmt());
            }
            if (deductionobj.getItAmt() != null && !deductionobj.getItAmt().equals("")) {
                itAmt = Double.parseDouble(deductionobj.getItAmt());
            }
            if (deductionobj.getOrAmt() != null && !deductionobj.getOrAmt().equals("")) {
                orAmt = Double.parseDouble(deductionobj.getOrAmt());
            }
            /*if (obj.getDrAmt() != null && !obj.getDrAmt().equals("")) {
             drAmt = Double.parseDouble(obj.getDrAmt());
             }*/

            Schedule sc = new Schedule();
            sc.setObjectHead(deductionobj.getItObjHead());
            sc.setScheduleName("- " + "IT");
            sc.setSchAmount(deductionobj.getItAmt());
            if (deductionobj.getItAmt() != null) {
                deductionList.add(sc);
            }

            sc = new Schedule();
            sc.setObjectHead(deductionobj.getPtObjHead());
            sc.setScheduleName("- " + "PT");
            sc.setSchAmount(deductionobj.getPtAmt());
            if (deductionobj.getPtAmt() != null) {
                deductionList.add(sc);
            }

            sc = new Schedule();
            sc.setObjectHead(deductionobj.getOrObjHead());
            sc.setScheduleName("- " + "OR");
            sc.setSchAmount(deductionobj.getOrAmt());
            if (deductionobj.getOrAmt() != null) {
                deductionList.add(sc);
            }

            /*sc = new Schedule();
             sc.setObjectHead(obj.getDrObjHead());
             sc.setScheduleName("- " + "DR");
             sc.setSchAmount(obj.getDrAmt());
             if (obj.getDrAmt() != null) {
             scheduleList.add(sc);
             }*/
            sc = new Schedule();
            sc.setObjectHead(deductionobj.getCpfObjHead());
            sc.setSchAmount(deductionobj.getCpfAmt());
            if (deductionobj.getCpfAmt() != null) {
                sc.setScheduleName("- " + deductionobj.getAcctType());
                deductionList.add(sc);
            }
            deductAmt = cpfAmt + ptAmt + itAmt + orAmt;
            double netAmt = allowanceAmt - deductAmt;

            String allowEsign = comonScheduleDao.allowedOfficeEsign(selectedEmpOffice);
            String[] rowArray = allowEsign.split("\\|");
            allowEsignStatus = rowArray[0];
            if (allowEsignStatus.equals("Y")) {
                boolean islogcreated = esignBillDao.isEsignLogCreated(crb.getAqmonth(), crb.getAqyear(), bbBean.getBillNo(), bbBean.getSlNo());
                if (islogcreated == false) {
                    createpathPDF = esignBillDao.GeneratePDFSignedPath(selectedEmpOffice, crb.getAqyear(), crb.getAqmonth(), bbBean.getBillNo());
                    unsignedPdfFilename = "ArrearBillFrontPage_" + bbBean.getBillNo() + "_unsigned.pdf";
                    PdfWriter.getInstance(document, new FileOutputStream(createpathPDF + File.separator + unsignedPdfFilename));
                    document.open();
                } else {
                    response.setHeader("Content-Disposition", "attachment; filename=ArrearBillFrontPage_" + bbBean.getBillNo() + ".pdf");
                }
            } else {
                response.setHeader("Content-Disposition", "attachment; filename=ArrearBillFrontPage_" + bbBean.getBillNo() + ".pdf");
            }

            if (bbBean.getSlNo() != null && !bbBean.getSlNo().equals("") && allowEsignStatus.equals("Y")) {
                esignBillDao.savepdflog(unsignedPdfFilename, createpathPDF, bbBean.getSlNo(), bbBean.getBillNo(), crb.getAqmonth(), crb.getAqyear(), lub.getLoginempid(), selectedEmpOffice, crb.getTypeofBill());
            }

            comonScheduleDao.arrearBillFrontPagePDF(document, crb, bbBean.getBillNo(), billChartOfAccount, deductionList, allowanceList, allowanceAmt, deductAmt, (int) netAmt,payHead);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "billFrontPagePDFBKP")
    public void BillFrontPagePDFBKP(HttpServletResponse response, @ModelAttribute("BillBrowserbean") BillBrowserbean bbBean, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            response.setHeader("Content-Disposition", "attachment; filename=BillFrontPage_" + bbBean.getBillNo() + ".pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(bbBean.getBillNo());
            BillChartOfAccount billChartOfAccount = aqReportDao.getBillChartOfAccount(bbBean.getBillNo());
            BillObjectHead boha = aqReportDao.getBillObjectHeadAndAmt(bbBean.getBillNo(), crb.getAqyear(), crb.getAqmonth());
            ArrayList scheduleList = billFrontPageDmpDao.getScheduleListWithADCode(bbBean.getBillNo(), crb.getAqmonth(), crb.getAqyear());
            List oaList = aqReportDao.getAllowanceDetails(bbBean.getBillNo(), crb.getAqyear(), crb.getAqmonth());

            int spAmt = billFrontPageDmpDao.getSpecialPayAmount(Integer.parseInt(bbBean.getBillNo()), crb.getAqmonth(), crb.getAqyear());
            int irAmt = billFrontPageDmpDao.getIrAmount(Integer.parseInt(bbBean.getBillNo()), crb.getAqmonth(), crb.getAqyear());
            int payAmt = aqReportDao.getPayAmt(Integer.parseInt(bbBean.getBillNo()));
            String payHead = billFrontPageDmpDao.getPayHead(Integer.parseInt(bbBean.getBillNo()));

            String monthName = CalendarCommonMethods.getFullMonthAsString(crb.getAqmonth());
            monthName = monthName + " - " + crb.getAqyear();

            comonScheduleDao.billFrontPagePDF(document, monthName, bbBean.getBillNo(), billChartOfAccount, boha, scheduleList, oaList, spAmt, irAmt, payAmt, payHead);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "singleBillfrontPageHTML")
    public String singleBillfrontPageHTML(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbBean,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("billNo") String billNo, BindingResult result, HttpServletResponse response) {

        CommonReportParamBean crb = null;

        BillChartOfAccount billChartOfAccount = null;

        BillObjectHead boha = null;

        List oaList = null;
        ArrayList scheduleList = new ArrayList();
        ArrayList scheduleListTR = new ArrayList();
        Schedule obj = null;

        double deductAmt = 0.0;
        double deductAmtTR = 0.0;

        double oaAmt = 0.0;

        int spAmt = 0;
        int irAmt = 0;

        String totOaAmtInString = "";
        String totNetAmtInString = "";
        String totDeductAmt = "";
        String totDeductAmtTR = "";
        String monthName = "";

        String payHead = "";
        String amtTobeDisplay = "";
        boolean isDDODHe = false;

        String path = "/payroll/SingleBillFrontPage";

        try {
            isDDODHe = billBrowserDao.isDDODHE(lub.getLoginoffcode());
            // --DHE--
            if (isDDODHe) {
                billChartOfAccount = aqReportDao.getBillChartOfAccountDHE(billNo);
            } //--End--
            else {
                billChartOfAccount = aqReportDao.getBillChartOfAccount(billNo);
            }

            //billChartOfAccount = aqReportDao.getBillChartOfAccount(billNo);
            crb = paybillDmpDao.getCommonReportParameter(bbBean.getBillNo());
            boha = aqReportDao.getBillObjectHeadAndAmt(bbBean.getBillNo(), crb.getAqyear(), crb.getAqmonth());

            oaList = aqReportDao.getAllowanceDetails(bbBean.getBillNo(), crb.getAqyear(), crb.getAqmonth());
            Schedule obj1 = null;
            int amt = 0;

            if (oaList != null && oaList.size() > 0) {
                for (int i = 0; i < oaList.size(); i++) {

                    obj1 = (Schedule) oaList.get(i);
                    if (obj1.getObjectHead() != null && obj1.getObjectHead().equals("136")) {
                        amt = amt + Integer.parseInt(obj1.getSchAmount());
                    }
                    if (obj1.getObjectHead() != null && obj1.getObjectHead().equals("000")) {
                        amt = amt + Integer.parseInt(obj1.getSchAmount());
                    }
                    oaAmt = obj1.getAlowanceTotal();
                }
            }

            scheduleList = billFrontPageDmpDao.getScheduleListWithADCode(bbBean.getBillNo(), crb.getAqmonth(), crb.getAqyear());
            scheduleListTR = billFrontPageDmpDao.getScheduleListWithADCodeTR(bbBean.getBillNo(), crb.getAqmonth(), crb.getAqyear());
            if (scheduleList != null && scheduleList.size() > 0) {
                for (int i = 0; i < scheduleList.size(); i++) {
                    obj = (Schedule) scheduleList.get(i);
                    double schAmt = Double.parseDouble(obj.getSchAmount());
                    deductAmt = deductAmt + schAmt;
                }
            }

            if (scheduleListTR != null && scheduleListTR.size() > 0) {
                for (int i = 0; i < scheduleListTR.size(); i++) {
                    obj = (Schedule) scheduleListTR.get(i);
                    double schAmt = Double.parseDouble(obj.getSchAmount());
                    deductAmtTR = deductAmtTR + schAmt;
                }
            }

            spAmt = billFrontPageDmpDao.getSpecialPayAmount(Integer.parseInt(billNo), crb.getAqmonth(), crb.getAqyear());
            irAmt = billFrontPageDmpDao.getIrAmount(Integer.parseInt(billNo), crb.getAqmonth(), crb.getAqyear());

            int payAmt = aqReportDao.getPayAmt(Integer.parseInt(billNo));
            payAmt = payAmt + amt;
            amtTobeDisplay = String.valueOf(payAmt);
            oaAmt = payAmt + spAmt + irAmt + (oaAmt - amt);

            payHead = billFrontPageDmpDao.getPayHead(Integer.parseInt(bbBean.getBillNo()));
            model.addAttribute("payHead", payHead);
            totDeductAmt = String.valueOf(deductAmt);// CommonFunctions.getRupessAndPaise(String.valueOf(deductAmt));
            totDeductAmtTR = String.valueOf(deductAmtTR);
            double netAmt = oaAmt - deductAmt;
            totOaAmtInString = Double.valueOf(oaAmt + "").longValue() + "";
            totNetAmtInString = Double.valueOf(netAmt + "").longValue() + "";

            monthName = CalendarCommonMethods.getFullMonthAsString(crb.getAqmonth());
            monthName = monthName + " - " + crb.getAqyear();

            model.addAttribute("billChartOfAccount", billChartOfAccount);
            model.addAttribute("billMonth", monthName);
            model.addAttribute("scheduleList", scheduleList);
            model.addAttribute("scheduleListTR", scheduleListTR);
            model.addAttribute("OAList", oaList);
            model.addAttribute("PayAmt", amtTobeDisplay);
            model.addAttribute("SpAmt", spAmt);
            model.addAttribute("IrAmt", irAmt);

            model.addAttribute("TotOaAmt", totOaAmtInString);
            model.addAttribute("TotOaAmtPaise", "");
            model.addAttribute("TotDeductAmt", totDeductAmt);
            model.addAttribute("TotDeductAmtTR", deductAmtTR + netAmt);
            model.addAttribute("TotDeductAmtPaise", "");
            model.addAttribute("TotNetAmt", totNetAmtInString);
            model.addAttribute("TotNetAmtPaise", "");
            model.addAttribute("billobjectheadamt", boha);

            model.addAttribute("grpACount", billFrontPageDmpDao.getEmployeeCount(lub.getLoginoffcode(), "A", Integer.parseInt(billNo)));
            model.addAttribute("grpBCount", billFrontPageDmpDao.getEmployeeCount(lub.getLoginoffcode(), "B", Integer.parseInt(billNo)));
            model.addAttribute("grpCCount", billFrontPageDmpDao.getEmployeeCount(lub.getLoginoffcode(), "C", Integer.parseInt(billNo)));
            model.addAttribute("grpDCount", billFrontPageDmpDao.getEmployeeCount(lub.getLoginoffcode(), "D", Integer.parseInt(billNo)));
            model.addAttribute("contractualCount", billFrontPageDmpDao.getContractualEmployeeCount(lub.getLoginoffcode(), "C", Integer.parseInt(billNo)));
            model.addAttribute("consolidatedCount", billFrontPageDmpDao.getContractualEmployeeCount(lub.getLoginoffcode(), "N", Integer.parseInt(billNo)));

            //model.addAttribute("amtToBeneficiary", billFrontPageDmpDao.getAmtCreditedToBeneficiary(Integer.parseInt(billNo), crb.getAqmonth(), crb.getAqyear()));
            model.addAttribute("amtToBeneficiary", Double.valueOf(netAmt - billFrontPageDmpDao.getAmtCreditedToDDO(Integer.parseInt(billNo), crb.getAqmonth(), crb.getAqyear())).longValue() + "");
            model.addAttribute("amtToDDO", billFrontPageDmpDao.getAmtCreditedToDDO(Integer.parseInt(billNo), crb.getAqmonth(), crb.getAqyear()));

            model.addAttribute("secondNetAmt", billFrontPageDmpDao.getAmtCreditedToBeneficiary(Integer.parseInt(billNo), crb.getAqmonth(), crb.getAqyear()) - billFrontPageDmpDao.getAmtCreditedToDDO(Integer.parseInt(billNo), crb.getAqmonth(), crb.getAqyear()));

            double netAmtInWord = netAmt + 1;

            model.addAttribute("totNetAmtInWord", Numtowordconvertion.convertNumber(new Double(netAmt).intValue()).toUpperCase());
            model.addAttribute("totNetAmtUnderInWord", Numtowordconvertion.convertNumber(new Double(netAmtInWord).intValue()).toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    @RequestMapping(value = "singlebillFrontPagePDF")
    public void singlebillFrontPagePDF(HttpServletResponse response, @ModelAttribute("BillBrowserbean") BillBrowserbean bbBean, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice) {

        Document document = new Document(PageSize.A4);
        response.setContentType("application/pdf");
        boolean isddoDheOff = false;
        BillChartOfAccount billChartOfAccount = null;

        try {
            // response.setHeader("Content-Disposition", "attachment; filename=SingleBillFrontPage_" + bbBean.getBillNo() + ".pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            isddoDheOff = billBrowserDao.isDDODHE(selectedEmpOffice);
            if (isddoDheOff) {
                billChartOfAccount = aqReportDao.getBillChartOfAccountDHE(bbBean.getBillNo());
            } else {
                billChartOfAccount = aqReportDao.getBillChartOfAccount(bbBean.getBillNo());
            }

            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(bbBean.getBillNo());
            //BillChartOfAccount billChartOfAccount = aqReportDao.getBillChartOfAccount(bbBean.getBillNo());
            BillObjectHead boha = aqReportDao.getBillObjectHeadAndAmt(bbBean.getBillNo(), crb.getAqyear(), crb.getAqmonth());
            ArrayList scheduleList = billFrontPageDmpDao.getScheduleListWithADCode(bbBean.getBillNo(), crb.getAqmonth(), crb.getAqyear());
            ArrayList scheduleListTR = billFrontPageDmpDao.getScheduleListWithADCodeTR(bbBean.getBillNo(), crb.getAqmonth(), crb.getAqyear());
            List oaList = aqReportDao.getAllowanceDetails(bbBean.getBillNo(), crb.getAqyear(), crb.getAqmonth());

            int spAmt = billFrontPageDmpDao.getSpecialPayAmount(Integer.parseInt(bbBean.getBillNo()), crb.getAqmonth(), crb.getAqyear());
            int irAmt = billFrontPageDmpDao.getIrAmount(Integer.parseInt(bbBean.getBillNo()), crb.getAqmonth(), crb.getAqyear());
            int payAmt = aqReportDao.getPayAmt(Integer.parseInt(bbBean.getBillNo()));
            String payHead = billFrontPageDmpDao.getPayHead(Integer.parseInt(bbBean.getBillNo()));

            String monthName = CalendarCommonMethods.getFullMonthAsString(crb.getAqmonth());
            monthName = monthName + " - " + crb.getAqyear();

            //String allowEsign = comonScheduleDao.allowedOfficeEsign(lub.getLoginoffcode());
            String allowEsign = comonScheduleDao.allowedOfficeEsign(selectedEmpOffice);
            String[] rowArray = allowEsign.split("\\|");
            String allowEsignStatus = rowArray[0];
            String createpathPDF = "";
            String unsignedPdfFilename = "";
            if (allowEsignStatus.equals("Y")) {
                createpathPDF = esignBillDao.GeneratePDFSignedPath(selectedEmpOffice, crb.getAqyear(), crb.getAqmonth(), bbBean.getBillNo());
                unsignedPdfFilename = "SingleBillFrontPage_" + bbBean.getBillNo() + "_unsigned.pdf";
                PdfWriter.getInstance(document, new FileOutputStream(createpathPDF + File.separator + unsignedPdfFilename));
                document.open();

            } else {
                // response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=SingleBillFrontPage_" + bbBean.getBillNo() + ".pdf");
                //  PdfWriter.getInstance(document, response.getOutputStream());
                //  document.open();
            }
            if (bbBean.getSlNo() != null && !bbBean.getSlNo().equals("") && allowEsignStatus.equals("Y")) {
                esignBillDao.savepdflog(unsignedPdfFilename, createpathPDF, bbBean.getSlNo(), bbBean.getBillNo(), crb.getAqmonth(), crb.getAqyear(), lub.getLoginempid(), selectedEmpOffice, crb.getTypeofBill());
            }

            billFrontPageDmpDao.singlebillFrontPagePDF(document, monthName, bbBean.getBillNo(), billChartOfAccount, boha, scheduleList, scheduleListTR, oaList, spAmt, irAmt, payAmt, payHead, selectedEmpOffice, crb.getAqmonth(), crb.getAqyear());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}
