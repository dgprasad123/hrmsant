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
import hrms.dao.esignBill.EsignBillDAO;
import hrms.dao.payroll.billbrowser.AqReportDAOImpl;
import hrms.dao.payroll.billbrowser.BillBrowserDAO;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.billbrowser.AllowDeductDetails;
import hrms.model.payroll.billbrowser.BillChartOfAccount;
import hrms.model.payroll.billbrowser.BillObjectHead;
import hrms.model.payroll.schedule.BillBackPageBean;
import hrms.model.payroll.schedule.Schedule;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpOffice"})
public class BillBackPageController implements ServletContextAware {

    @Autowired
    public ScheduleDAO comonScheduleDao;
    @Autowired
    AqReportDAOImpl AqReportDAO;
    @Autowired
    public PayBillDMPDAO paybillDmpDao;

    @Autowired
    BillBrowserDAO billBrowserDao;

    @Autowired
    EsignBillDAO esignBillDao;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "BillBackPgHTML", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView BillBackPgHTML(ModelMap model, @RequestParam("billNo") String billNo) {
        CommonReportParamBean crb = null;
        BillChartOfAccount billChartOfAccount = null;
        ModelAndView mav = null;
        List empList = null;
        double deductAmt = 0;
        String totPaise = "";
        long total = 0;
        double netAmt = 0;
        double cpfAmt = 0.0;
        double itAmt = 0.0;
        double ptAmt = 0.0;
        String netTotaUnder = "";
        String arrAll = "";
        String netTotalinWordUnder = "";
        String netTotal = "";
        String netTotalinWord = "";
        BillBackPageBean backPageBean = new BillBackPageBean();
        BillBackPageBean obj = null;
        BillObjectHead boha = null;
        Schedule obj1 = null;
        double oaAmt = 0.0;
        double orData = 0.0;
        double drData = 0.0;
        String payhead = "";
        String dahead = "";
        String hrahead = "";
        String oahead = "";
        String acctType = "";
        try {
            mav = new ModelAndView();
            crb = paybillDmpDao.getCommonReportParameter(billNo);

            if (crb.getTypeofBill() != null && (crb.getTypeofBill().equals("ARREAR") || crb.getTypeofBill().equals("ARREAR_6") || crb.getTypeofBill().equals("ARREAR_J") || crb.getTypeofBill().equals("ARREAR_6_J"))) {
                billChartOfAccount = AqReportDAO.getBillChartOfAccount(billNo);
                boha = AqReportDAO.getArrBillObjectHeadAndAmt(billNo,crb.getTypeofBill());

                obj1 = AqReportDAO.getArrScheduleListWithADCode(billNo);
                if (obj1.getCpfAmt() != null && !obj1.getCpfAmt().equals("")) {
                    cpfAmt = Double.parseDouble(obj1.getCpfAmt());

                }
                if (obj1.getPtAmt() != null && !obj1.getPtAmt().equals("")) {
                    ptAmt = Double.parseDouble(obj1.getPtAmt());
                }
                if (obj1.getItAmt() != null && !obj1.getItAmt().equals("")) {
                    itAmt = Double.parseDouble(obj1.getItAmt());
                }

                deductAmt = cpfAmt + ptAmt + itAmt;
                oaAmt = boha.getPayamt();
                arrAll = Double.valueOf(oaAmt + "").longValue() + "";
                String totOaAmt[] = CommonFunctions.getRupessAndPaise(String.valueOf(oaAmt));
                String totDeductAmt[] = CommonFunctions.getRupessAndPaise(String.valueOf(deductAmt));

                netAmt = oaAmt - deductAmt;
                String netAmount[] = CommonFunctions.getRupessAndPaise(String.valueOf(netAmt));
                netTotalinWord = Numtowordconvertion.convertNumber((int) Double.parseDouble(netAmount[0]));
                netTotal = Double.valueOf(netAmt + "").longValue() + "";
                netTotalinWord = Numtowordconvertion.convertNumber(Integer.parseInt(netTotal));
                netTotaUnder = Double.valueOf((netAmt + 1) + "").longValue() + "";
                netTotalinWordUnder = Numtowordconvertion.convertNumber(Integer.parseInt(netTotaUnder));

                model.addAttribute("billobjectheadamt", boha);

                mav.addObject("BPGHeader", backPageBean);
                mav.addObject("BPGList", empList);
                mav.addObject("acctType", acctType);
                model.addAttribute("cpfAmt", obj1.getCpfAmt());
                model.addAttribute("ptAmt", obj1.getPtAmt());
                model.addAttribute("itAmt", obj1.getItAmt());
                mav.addObject("TotDedut", totDeductAmt[0]);
                mav.addObject("NetTotal", netTotal);
                mav.addObject("NetTotalWord", netTotalinWord);
                mav.addObject("NetTotalUnder", netTotaUnder);
                mav.addObject("NetTotalWordUnder", netTotalinWordUnder);
                mav.addObject("payAmt", arrAll);
                mav.setViewName("/payroll/arrear/ArrBillBackPage");

            } else if (crb.getTypeofBill() != null && (crb.getTypeofBill().equals("OTHER_ARREAR") || crb.getTypeofBill().equals("ARREAR_NPS"))) {
                billChartOfAccount = AqReportDAO.getBillChartOfAccount(billNo);
                boha = AqReportDAO.getArrBillObjectHeadAndAmt(billNo,crb.getTypeofBill());

                List liAllowance = billBrowserDao.getAllowanceListForArrear(Integer.parseInt(billNo), crb.getAqmonth(), crb.getAqyear());

                double payamtTot = 0;
                for (int i = 0; i < liAllowance.size(); i++) {
                    AllowDeductDetails all = (AllowDeductDetails) liAllowance.get(i);

                    if (all.getObjecthead() != null && !all.getObjecthead().equals("")) {
                        if (all.getAdamount() != null && !all.getAdamount().equals("")) {
                            payamtTot = payamtTot + Double.valueOf(all.getAdamount());
                        }
                        if (all.getObjecthead().equals("136")) {
                            if (all.getAdamount() != null && !all.getAdamount().equals("")) {
                                if (payhead != null && !payhead.equals("")) {
                                    payhead = (Integer.parseInt(payhead) + Integer.parseInt(all.getAdamount())) + "";
                                } else {
                                    payhead = Integer.parseInt(all.getAdamount()) + "";
                                }

                            }
                        } else if (all.getObjecthead().equals("523")) {
                            oahead = all.getAdamount();
                        } else if (all.getObjecthead().equals("156")) {
                            dahead = all.getAdamount();
                        } else if (all.getObjecthead().equals("403")) {
                            hrahead = all.getAdamount();
                        }
                    }
                }

                obj1 = AqReportDAO.getArrScheduleListWithADCode(billNo);
                if (obj1.getCpfAmt() != null && !obj1.getCpfAmt().equals("")) {
                    cpfAmt = Double.parseDouble(obj1.getCpfAmt());
                    acctType = obj1.getAcctType();
                }
                if (obj1.getPtAmt() != null && !obj1.getPtAmt().equals("")) {
                    ptAmt = Double.parseDouble(obj1.getPtAmt());
                }
                if (obj1.getItAmt() != null && !obj1.getItAmt().equals("")) {
                    itAmt = Double.parseDouble(obj1.getItAmt());
                }
                if (obj1.getOrAmt() != null && !obj1.getOrAmt().equals("")) {
                    orData = Double.parseDouble(obj1.getOrAmt());
                }
                /*if ((obj1.getDrAmt() != null && !obj1.getDrAmt().equals(""))) {
                    drData = Double.parseDouble(obj1.getDrAmt());
                }*/

                deductAmt = cpfAmt + ptAmt + itAmt + orData ;
                oaAmt = payamtTot;
                arrAll = Double.valueOf(oaAmt + "").longValue() + "";
                String totOaAmt[] = CommonFunctions.getRupessAndPaise(String.valueOf(oaAmt));
                String totDeductAmt[] = CommonFunctions.getRupessAndPaise(String.valueOf(deductAmt));

                netAmt = oaAmt - deductAmt;
                String netAmount[] = CommonFunctions.getRupessAndPaise(String.valueOf(netAmt));
                netTotalinWord = Numtowordconvertion.convertNumber((int) Double.parseDouble(netAmount[0]));
                if (crb.getTypeofBill().equals("ARREAR_NPS")) {
                    netTotal = "0";
                    arrAll = Double.valueOf(obj1.getCpfAmt() + "").longValue() + "";
                    netAmt = 0;
                } else {
                    netTotal = Double.valueOf(netAmt + "").longValue() + "";
                }
                netTotalinWord = Numtowordconvertion.convertNumber(Integer.parseInt(netTotal));
                netTotaUnder = Double.valueOf((netAmt + 1) + "").longValue() + "";
                netTotalinWordUnder = Numtowordconvertion.convertNumber(Integer.parseInt(netTotaUnder));

                if (crb.getTypeofBill().equals("ARREAR_NPS")) {
                    netTotaUnder = "1";
                    netTotalinWordUnder = Numtowordconvertion.convertNumber(Integer.parseInt(netTotaUnder));
                }

                model.addAttribute("billobjectheadamt", boha);
                mav.addObject("acctType", acctType);
                mav.addObject("BPGHeader", backPageBean);
                mav.addObject("BPGList", empList);
                model.addAttribute("cpfAmt", obj1.getCpfAmt());
                model.addAttribute("ptAmt", obj1.getPtAmt());
                model.addAttribute("itAmt", obj1.getItAmt());
                model.addAttribute("itObjHead", obj1.getItObjHead());
                model.addAttribute("cpfObjHead", obj1.getCpfObjHead());
                model.addAttribute("ptfObjHead", obj1.getPtObjHead());
                model.addAttribute("orAmt", obj1.getOrAmt());
                model.addAttribute("drAmt", obj1.getDrAmt());
                model.addAttribute("orObjHead", obj1.getOrObjHead());
                //model.addAttribute("drObjHead", obj1.getDrObjHead());
                mav.addObject("TotDedut", totDeductAmt[0]);
                mav.addObject("NetTotal", netTotal);
                mav.addObject("NetTotalWord", netTotalinWord);
                mav.addObject("NetTotalUnder", netTotaUnder);
                mav.addObject("NetTotalWordUnder", netTotalinWordUnder);
                mav.addObject("payAmt", arrAll);
                mav.setViewName("/payroll/arrear/ArrBillBackPage");
            } else {

                backPageBean = comonScheduleDao.getBillBackPgScheduleHeaderDetails(billNo, crb.getAqyear(), crb.getAqmonth());
                empList = comonScheduleDao.getBillBackPgScheduleEmpList(billNo, crb.getAqyear(), crb.getAqmonth());

                if (empList != null && empList.size() > 0) {
                    obj = new BillBackPageBean();
                    for (int i = 0; i < empList.size(); i++) {
                        obj = (BillBackPageBean) empList.get(i);
                        deductAmt = deductAmt + Long.parseLong(obj.getSchAmount());
                    }
                }
                total = Long.parseLong(backPageBean.getTotalPaise());

                netAmt = total - deductAmt;
                netTotal = Double.valueOf(netAmt + "").longValue() + "";
                netTotalinWord = Numtowordconvertion.convertNumber((int) Double.parseDouble(netTotal));

                netTotaUnder = Double.valueOf((netAmt + 1) + "").longValue() + "";
                netTotalinWordUnder = Numtowordconvertion.convertNumber(Integer.parseInt(netTotaUnder));

                mav.addObject("BPGHeader", backPageBean);
                mav.addObject("BPGList", empList);
                mav.addObject("TotDedut", deductAmt);
                mav.addObject("NetTotal", netTotal);
                mav.addObject("NetTotalWord", netTotalinWord);
                mav.addObject("NetTotalUnder", netTotaUnder);
                mav.addObject("NetTotalWordUnder", netTotalinWordUnder);
                mav.setViewName("/payroll/schedule/BillBackPage");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "billBackPagePDF")
    public void BillBackPagePDF(HttpServletResponse response, @RequestParam("billNo") String billNo, @ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, 
            @RequestParam("slNo") String slNo) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            response.setHeader("Content-Disposition", "attachment; filename=BillBackPage_" + billNo + ".pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            BillBackPageBean backPageBean = comonScheduleDao.getBillBackPgScheduleHeaderDetails(billNo, crb.getAqyear(), crb.getAqmonth());
            List empList = comonScheduleDao.getBillBackPgScheduleEmpList(billNo, crb.getAqyear(), crb.getAqmonth());

            //String allowEsign = comonScheduleDao.allowedOfficeEsign(lub.getLoginoffcode());
            String allowEsign = comonScheduleDao.allowedOfficeEsign(selectedEmpOffice);
            String[] rowArray = allowEsign.split("\\|");
            String allowEsignStatus = rowArray[0];
            String createpathPDF = "";
            String unsignedPdfFilename = "";
            if (allowEsignStatus.equals("Y")) {
                createpathPDF = esignBillDao.GeneratePDFSignedPath(lub.getLoginoffcode(), crb.getAqyear(), crb.getAqmonth(), billNo);
                //   System.out.println("createpathPDF==="+createpathPDF);
                unsignedPdfFilename = "BillBackPage_" + billNo + "_unsigned.pdf";
                PdfWriter.getInstance(document, new FileOutputStream(createpathPDF + File.separator + unsignedPdfFilename));
                document.open();
            }
            /*if (billNo != null && !slNo.equals("") && allowEsignStatus.equals("Y")) {
                esignBillDao.savepdflog(unsignedPdfFilename, createpathPDF, slNo, billNo, crb.getAqmonth(), crb.getAqyear(), lub.getLoginempid(), lub.getLoginoffcode());
            }*/
            comonScheduleDao.billBackPagePDF(document, billNo, backPageBean, empList);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}
