/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.billbrowser;

import hrms.common.AqFunctionalities;
import hrms.common.CommonFunctions;
import hrms.dao.empqtrallotment.EmpQtrAllotmentDAO;
import hrms.dao.lic.LicDAO;
import hrms.dao.loansanction.LoanSancDAO;
import hrms.dao.payroll.allowancededcution.AllowanceDeductionDAO;
import hrms.dao.payroll.aqdtls.AqDtlsDAO;
import hrms.dao.payroll.aqmast.AqmastDAO;
import hrms.dao.payroll.arrear.ArrmastDAO;
import hrms.dao.payroll.billbrowser.AqReportDAO;
import hrms.dao.payroll.billbrowser.BillBrowserDAO;
import hrms.dao.payroll.billbrowser.BillGroupDAO;
import hrms.dao.payroll.billbrowser.SectionDefinationDAO;
import hrms.dao.payroll.billmast.BillMastDAO;
import hrms.dao.payroll.paybill.PayBillDAO;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.model.billvouchingTreasury.BillDetail;
import hrms.model.common.CommonReportParamBean;
import hrms.model.employee.PayComponent;
import hrms.model.employee.QuaterAllotment;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.aqdtls.AqDtlsModel;
import hrms.model.payroll.aqmast.AqmastModel;
import hrms.model.payroll.arrear.ArrAqDtlsModel;
import hrms.model.payroll.arrear.ArrAqMastModel;
import hrms.model.payroll.arrear.PayRevisionOption;
import hrms.model.payroll.billbrowser.AcquaintanceBean;
import hrms.model.payroll.billbrowser.SectionDtlSPCWiseEmp;
import hrms.model.payroll.billmast.BillMastModel;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas Jena
 */
@Controller
@SessionAttributes("LoginUserBean")
public class AquitanceBrowserController {

    @Autowired
    AqReportDAO aqReportDao;

    @Autowired
    public PayBillDMPDAO paybillDmpDao;

    @Autowired
    public ArrmastDAO arrmastDAO;
    @Autowired
    BillGroupDAO billGroupDAO;
    @Autowired
    SectionDefinationDAO sectionDefinationDAO;
    @Autowired
    AqmastDAO aqmastDAO;
    @Autowired
    BillMastDAO billMastDAO;
    @Autowired
    AqDtlsDAO aqDtlsDAO;
    @Autowired
    AllowanceDeductionDAO allowanceDeductionDAO;
    @Autowired
    LoanSancDAO loanSancDAO;
    @Autowired
    LicDAO licDAO;
    @Autowired
    EmpQtrAllotmentDAO empQtrAllotmentDAO;
    @Autowired
    PayBillDAO payBillDAO;

    @Autowired
    BillBrowserDAO billBrowserDao;

    @RequestMapping(value = "browseAquitance", method = RequestMethod.GET)
    public ModelAndView browseAquitance(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("billNo") String billNo, HttpServletResponse response) {//
        ModelAndView mv = new ModelAndView("/payroll/BrowseAquitance");
        boolean isCollegeDhe = false;
         List aquitanceList=new ArrayList();
        BillDetail bill = new BillDetail();

        String stopSingleProcess = "Y";
        /* if(bill.getBillyear()<2020 && bill.getTypeofBillString().equals("PAY")){
         stopSingleProcess="Y";
         }
         */
        //--Start DHE---
        isCollegeDhe = billBrowserDao.isCollegeUnderDHE(lub.getLoginoffcode());
        if (isCollegeDhe) {            
            bill = billBrowserDao.getBillDetailsDHE(Integer.parseInt(billNo), lub.getLoginoffcode());
            aquitanceList = aqReportDao.getAcquaintanceDHE(billNo,lub.getLoginoffcode());           
            
        }//--End--
        else {
            bill = billBrowserDao.getBillDetails(Integer.parseInt(billNo));
            aquitanceList = aqReportDao.getAcquaintance(billNo);
        }

        if (bill.getBillStatusId() < 2 || bill.getBillStatusId() == 4 || bill.getBillStatusId() == 6 || bill.getBillStatusId() == 8) {
            stopSingleProcess = "N";
        } else {
            stopSingleProcess = "Y";
        }

        mv.addObject("aquitanceList", aquitanceList);
        mv.addObject("billNo", billNo);
        mv.addObject("billstatus", bill.getBillStatusId());
        mv.addObject("stopSingleProcess", stopSingleProcess);
        return mv;
    }
//BrowseAquitanceDataView

    @RequestMapping(value = "browseAquitanceDataView", method = RequestMethod.GET)
    public ModelAndView browseAquitanceDataView(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("aqslno") String aqslno, @RequestParam("billNo") String billNo, HttpServletResponse response) {
        int basic = 0;
        int totded = 0;
        int totall = 0;
        double gross = 0;
        double net = 0;
        double totAllowance = 0.0;
        double totDeduction = 0.0;
        ModelAndView mv = new ModelAndView("/payroll/BrowseAquitanceDataView");

        String tableName = aqReportDao.getAqDtlsTableName(billNo);
        AcquaintanceBean aqReportBean = aqReportDao.getAqMastDtl(aqslno);
        ArrayList deductionobjList = aqReportDao.getAcquaintanceDtlDed(aqslno, tableName);
        ArrayList allowanceObjList = aqReportDao.getAcquaintanceDtlAll(aqslno, tableName);
        totAllowance = aqReportDao.getTotalAllowance(aqslno, tableName);
        totDeduction = aqReportDao.getTotalDeduction(aqslno, tableName);
        gross = totAllowance + aqReportBean.getCurbasic();
        net = gross - totDeduction;
        mv.addObject("totAll", totAllowance);
        mv.addObject("totDed", totDeduction);
        mv.addObject("gross", gross);
        mv.addObject("net", net);
        mv.addObject("aqSlNo", aqslno);
        mv.addObject("billNo", billNo);
        mv.addObject("aqReportBean", aqReportBean);
        mv.addObject("deductionobjList", deductionobjList);
        mv.addObject("allowanceObjList", allowanceObjList);
        return mv;
    }

    @RequestMapping(value = "browseAquitanceData", method = RequestMethod.GET)
    public ModelAndView browseAquitanceData(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("aqslno") String aqslno, @RequestParam("billNo") String billNo, HttpServletResponse response) {
        int basic = 0;
        int totded = 0;
        int totall = 0;
        double gross = 0;
        double net = 0;
        double totAllowance = 0.0;
        double totDeduction = 0.0;
        ModelAndView mv = new ModelAndView("/payroll/BrowseAquitanceData");
        boolean iasBillFound = billBrowserDao.verifySalaryBillofIAS(Integer.parseInt(billNo));
        String iasBill = "N";
        if (iasBillFound) {
            iasBill = "Y";
        } else {
            iasBill = "N";
        }

        String tableName = aqReportDao.getAqDtlsTableName(billNo);
        AcquaintanceBean aqReportBean = aqReportDao.getAqMastDtl(aqslno);
        ArrayList deductionobjList = aqReportDao.getAcquaintanceDtlDed(aqslno, tableName);
        ArrayList allowanceObjList = aqReportDao.getAcquaintanceDtlAll(aqslno, tableName);
        totAllowance = aqReportDao.getTotalAllowance(aqslno, tableName);
        totDeduction = aqReportDao.getTotalDeduction(aqslno, tableName);
        gross = totAllowance + aqReportBean.getCurbasic();
        net = gross - totDeduction;
        mv.addObject("totAll", totAllowance);
        mv.addObject("totDed", totDeduction);
        mv.addObject("gross", gross);
        mv.addObject("net", net);
        mv.addObject("aqSlNo", aqslno);
        mv.addObject("billNo", billNo);
        mv.addObject("aqReportBean", aqReportBean);
        mv.addObject("deductionobjList", deductionobjList);
        mv.addObject("allowanceObjList", allowanceObjList);
        mv.addObject("checkAsIASBill", iasBill);
        return mv;
    }

    @RequestMapping(value = "backToBillListPage")
    public ModelAndView backToBrowseAquitanceArr(ModelMap model, @ModelAttribute("ArrAqDtlsModel") ArrAqDtlsModel arrDtlsBean, @RequestParam("billNo") int billNo, HttpServletResponse response) {

        ModelAndView mv = new ModelAndView("redirect:/browseAquitanceArr.htm?billNo=" + billNo);

        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "reprocessPayAqMast")
    public void reprocessPayAqMast(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("aqslno") String aqslno, @RequestParam("billNo") int billNo, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        BillMastModel billMastModel = billMastDAO.getBillMastDetails(billNo);
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.DAY_OF_MONTH, 1);
        myCalendar.set(Calendar.MONTH, billMastModel.getAqMonth());
        myCalendar.set(Calendar.YEAR, billMastModel.getAqYear());
        Date startDate = myCalendar.getTime();
        myCalendar.set(Calendar.DAY_OF_MONTH, myCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        int daysInMonth = myCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date endDate = myCalendar.getTime();
        String configLvl = billGroupDAO.getConfigurationLvl(new BigDecimal(billMastModel.getBillGroupId()));
        AqmastModel aqMastModel = aqmastDAO.getAqmastDetail(aqslno);
        HashMap<String, Integer> hmap = new HashMap<String, Integer>();
        try {
            out = response.getWriter();
            /*Aquitance Process for Regular Employee*/
            if (aqMastModel.getEmpType().equals("R")) {
                String aqDTLSTable = AqFunctionalities.getAQBillDtlsTable(billMastModel.getAqMonth(), billMastModel.getAqYear());
                aqDtlsDAO.deleteAqdtls(aqslno, aqDTLSTable);
                SectionDtlSPCWiseEmp sdswe = sectionDefinationDAO.getSPCEmpSection(aqMastModel.getEmpCode());
                String gpfSeries = CommonFunctions.getGPFSeries(sdswe.getGpfaccno());
                HashMap<String, Integer> payworkday = payBillDAO.getPayWorkDays(aqMastModel, startDate, endDate, daysInMonth);
                PayComponent payComponent = payBillDAO.getEmployeePayComponent(sdswe, startDate, endDate, daysInMonth);

                boolean payHeldUp = aqmastDAO.stopSalaryForPayHeldUp(aqMastModel.getEmpCode());
                //boolean stopSalaryForRetirement = aqmastDAO.stopNpsDeduction(aqMastModel.getEmpCode(), aqMastModel.getAqMonth(), aqMastModel.getAqYear());
                if (payHeldUp == false) {

                    int monBasic = payComponent.getBasic();

                    aqMastModel.setPayDay(payworkday.get("payday"));
                    int curBasic = 0;
                    if (payworkday.get("payday") == daysInMonth) {
                        curBasic = monBasic;
                    } else {
                        double value = (((double) monBasic) / daysInMonth) * payworkday.get("payday");
                        Long temp = Math.round(value);
                        curBasic = temp.intValue();
                    }
                    if (aqMastModel.getDeptCode() != null && aqMastModel.getDeptCode().equals("05")) {
                        //curBasic = Math.round(Float.parseFloat(monBasic + "") / 2);
                        curBasic = Math.round(Float.parseFloat(curBasic + "") / 2);
                    }
                    aqMastModel.setGpfType(gpfSeries);
                    aqMastModel.setBankAccNo(sdswe.getBankaccno());
                    aqMastModel.setIfscCode(sdswe.getIfscCode());
                    aqMastModel.setGrossAmt(curBasic);
                    aqMastModel.setCurBasic(curBasic);
                    aqMastModel.setMonBasic(monBasic);
                    aqMastModel.setPayScale(sdswe.getPayscale());
                    aqMastModel.setDeptCode(sdswe.getDepcode());
                    aqMastModel.setPayCommission(payComponent.getPaycommission() + "");
                    aqmastDAO.updateAqmastdata(aqMastModel);

                    payComponent.setBasic(curBasic);
                    /*Allowance List*/
                    ArrayList allowanceList = allowanceDeductionDAO.getAllowanceList(sdswe, configLvl, aqMastModel.getAqGroup(), billMastModel.getOffCode(), daysInMonth, payworkday.get("payday"), payworkday.get("workday"), 0, payComponent, billMastModel.getBillType(), aqMastModel.getDeptCode(), aqMastModel.getAqMonth(), aqMastModel.getAqYear());

                    AqDtlsModel[] AqDtlsModelFromAllowanceList = allowanceDeductionDAO.getAqDtlsModelFromAllowanceList(allowanceList, aqMastModel, payComponent, aqMastModel.getEmpType(), aqMastModel.getAqMonth());

                    aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromAllowanceList, false, aqDTLSTable);

                    System.out.println("aqMastModelgross" + aqMastModel.getGrossAmt());

                    /*Allowance List*/
                    ArrayList deductionList = allowanceDeductionDAO.getDeductionList(sdswe, configLvl, aqMastModel, billMastModel, daysInMonth, payworkday.get("payday"), payworkday.get("payday"), 0, payComponent, gpfSeries);
                    List principalLoanList = loanSancDAO.getPrincipalLoanListForBill(sdswe.getEmpid(), aqMastModel.getPayDay(), sdswe.getDepcode());

                    List interestLoanList = loanSancDAO.getInterestLoanListForBill(sdswe.getEmpid(), aqMastModel.getPayDay(), sdswe.getDepcode());
                    List licList = licDAO.getLicList(sdswe.getEmpid());
                    QuaterAllotment[] qtrallotmentList = empQtrAllotmentDAO.getQuaterAllotmentDetail(sdswe.getEmpid(),curBasic,payComponent.getGp());

                    AqDtlsModel[] AqDtlsModelFromDeductionList = allowanceDeductionDAO.getAqDtlsModelFromDeductionList(deductionList, aqMastModel, payComponent);
                    AqDtlsModel[] AqDtlsModelFromPLoanList = loanSancDAO.getAqDtlsModelFromPLoanList(principalLoanList, aqMastModel);
                    AqDtlsModel[] AqDtlsModelFromILoanList = loanSancDAO.getAqDtlsModelFromILoanList(interestLoanList, aqMastModel);
                    AqDtlsModel[] AqDtlsModelFromLICList = licDAO.getAqDtlsModelFromLICList(licList, aqMastModel);
                    AqDtlsModel[] AqDtlsModelFromQTRList = empQtrAllotmentDAO.getAqDtlsModelFromQtrAllotment(qtrallotmentList, aqMastModel);
                    AqDtlsModel[] AqDtlsModelFromPT = payBillDAO.getAqDtlsModelFromPT(aqMastModel, monBasic, curBasic);
                    AqDtlsModel[] AqDtlsModelFromCPF = null;

                    boolean stopMonthlySubscrip = false;
                    if (aqMastModel.getAcctType() != null && !aqMastModel.getAcctType().equals("") && (aqMastModel.getAcctType().equalsIgnoreCase("GPF") || aqMastModel.getAcctType().equalsIgnoreCase("TPF"))) {
                        stopMonthlySubscrip = aqmastDAO.stopGpfDeduction(aqMastModel.getEmpCode(), aqMastModel.getAqMonth(), aqMastModel.getAqYear());
                    } else if (aqMastModel.getAcctType() != null && !aqMastModel.getAcctType().equals("") && aqMastModel.getAcctType().equalsIgnoreCase("PRAN")) {
                        stopMonthlySubscrip = aqmastDAO.stopNpsDeduction(aqMastModel.getEmpCode(), aqMastModel.getAqMonth(), aqMastModel.getAqYear());
                    }

                    if (sdswe.getAcctype().equals("PRAN") && !sdswe.getAccountAssume().equals("Y")) {
                        AqDtlsModelFromCPF = payBillDAO.getAqDtlsModelFromCPF(payComponent, aqMastModel);
                    } else if ((sdswe.getAcctype().equals("GPF") || sdswe.getAcctype().equals("TPF")) && !sdswe.getAccountAssume().equals("Y")) {
                        if (payworkday.get("payday") < 15 || (aqMastModel.getDeptCode() != null && aqMastModel.getDeptCode().equals("05"))) {
                            stopMonthlySubscrip = true;
                        }
                        AqDtlsModelFromCPF = payBillDAO.getAqDtlsModelFromGPF_TPF(payComponent, aqMastModel);
                    } else if (sdswe.getAcctype().equals("EPF") && (!lub.getLoginoffcode().equals("OLSGAD0010001") && !lub.getLoginoffcode().equals("OLSEDN0010003") && !lub.getLoginoffcode().equals("OLSIND0010002"))) {
                        AqDtlsModelFromCPF = payBillDAO.getAqDtlsModelFromEPF(payComponent, aqMastModel);
                    }

                    aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromDeductionList, stopMonthlySubscrip, aqDTLSTable);
                    aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromILoanList, stopMonthlySubscrip, aqDTLSTable);
                    aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromPLoanList, stopMonthlySubscrip, aqDTLSTable);
                    aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromLICList, stopMonthlySubscrip, aqDTLSTable);
                    aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromQTRList, stopMonthlySubscrip, aqDTLSTable);
                    aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromPT, stopMonthlySubscrip, aqDTLSTable);
                    if (sdswe.getAcctype().equals("PRAN") || sdswe.getAcctype().equals("GPF") || sdswe.getAcctype().equals("TPF") || sdswe.getAcctype().equals("EPF")) {
                        aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromCPF, stopMonthlySubscrip, aqDTLSTable);
                    }

                }

                hmap.put("processed", 1);
                JSONObject jsonobj = new JSONObject(hmap);
                out.write(jsonobj.toString());
                /*Aquitance Process for Contractual Six Year Employee*/
            } else if (aqMastModel.getEmpType().equals("S")) {
                String aqDTLSTable = AqFunctionalities.getAQBillDtlsTable(billMastModel.getAqMonth(), billMastModel.getAqYear());
                aqDtlsDAO.deleteAqdtls(aqslno, aqDTLSTable);
                SectionDtlSPCWiseEmp sdswe = sectionDefinationDAO.getSPCEmpSection(aqMastModel.getEmpCode());
                String gpfSeries = CommonFunctions.getGPFSeries(sdswe.getGpfaccno());
                PayComponent payComponent = new PayComponent();
                HashMap<String, Integer> payworkday = payBillDAO.getPayWorkDays(sdswe, startDate, endDate, daysInMonth);
                int monBasic = sdswe.getCurBasicSalary();
                aqMastModel.setPayDay(payworkday.get("payday"));
                int curBasic = 0;
                if (payworkday.get("payday") == daysInMonth) {
                    curBasic = monBasic;
                } else {
                    double value = (((double) monBasic) / daysInMonth) * payworkday.get("payday");
                    Long temp = Math.round(value);
                    curBasic = temp.intValue();
                }
                aqMastModel.setGrossAmt(curBasic);
                aqMastModel.setCurBasic(curBasic);
                aqMastModel.setMonBasic(monBasic);
                aqMastModel.setBankAccNo(sdswe.getBankaccno());
                aqMastModel.setIfscCode(sdswe.getIfscCode());
                aqmastDAO.updateAqmastdata(aqMastModel);
                payComponent.setBasic(curBasic);
                ArrayList allowanceList = allowanceDeductionDAO.getAllowanceList(sdswe.getSpc(), sdswe.getEmpid(), configLvl, aqMastModel.getAqGroup(), billMastModel.getOffCode(), daysInMonth, payworkday.get("payday"), payworkday.get("payday"), 0, payComponent, billMastModel.getBillType(), aqMastModel.getDeptCode());

                ArrayList deductionList = allowanceDeductionDAO.getDeductionList(sdswe.getSpc(), sdswe.getEmpid(), configLvl, aqMastModel.getAqGroup(), billMastModel.getOffCode(), daysInMonth, payworkday.get("payday"), payworkday.get("payday"), 0, payComponent, billMastModel.getBillType(), aqMastModel.getDeptCode(), gpfSeries);
                List principalLoanList = loanSancDAO.getPrincipalLoanListForBill(sdswe.getEmpid(), aqMastModel.getPayDay(), sdswe.getDepcode());
                List interestLoanList = loanSancDAO.getInterestLoanListForBill(sdswe.getEmpid(), aqMastModel.getPayDay(), sdswe.getDepcode());
                List licList = licDAO.getLicList(sdswe.getEmpid());
                QuaterAllotment[] qtrallotmentList = empQtrAllotmentDAO.getQuaterAllotmentDetail(sdswe.getEmpid(),curBasic,payComponent.getGp());
                AqDtlsModel[] AqDtlsModelFromAllowanceList = allowanceDeductionDAO.getAqDtlsModelForContractualFromAllowanceList(allowanceList, aqMastModel, payComponent, aqMastModel.getEmpType(), aqMastModel.getAqMonth());

                AqDtlsModel[] AqDtlsModelFromDeductionList = allowanceDeductionDAO.getAqDtlsModelFromDeductionList(deductionList, aqMastModel, payComponent);
                AqDtlsModel[] AqDtlsModelFromPLoanList = loanSancDAO.getAqDtlsModelFromPLoanList(principalLoanList, aqMastModel);
                AqDtlsModel[] AqDtlsModelFromILoanList = loanSancDAO.getAqDtlsModelFromILoanList(interestLoanList, aqMastModel);
                AqDtlsModel[] AqDtlsModelFromLICList = licDAO.getAqDtlsModelFromLICList(licList, aqMastModel);
                AqDtlsModel[] AqDtlsModelFromQTRList = empQtrAllotmentDAO.getAqDtlsModelFromQtrAllotment(qtrallotmentList, aqMastModel);
                AqDtlsModel[] AqDtlsModelFromPT = payBillDAO.getAqDtlsModelFromPT(aqMastModel, monBasic, curBasic);
                AqDtlsModel[] AqDtlsModelFromCPF = null;
                if (sdswe.getAcctype().equals("PRAN") && !sdswe.getAccountAssume().equals("Y")) {
                    AqDtlsModelFromCPF = payBillDAO.getAqDtlsModelFromCPF(payComponent, aqMastModel);
                } else if (sdswe.getAcctype().equals("EPF")) {
                    AqDtlsModelFromCPF = payBillDAO.getAqDtlsModelFromEPF(payComponent, aqMastModel);
                }

                aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromAllowanceList, false, aqDTLSTable);
                aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromDeductionList, false, aqDTLSTable);
                aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromILoanList, false, aqDTLSTable);
                aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromPLoanList, false, aqDTLSTable);
                aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromLICList, false, aqDTLSTable);
                aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromQTRList, false, aqDTLSTable);
                aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromPT, false, aqDTLSTable);
                if (sdswe.getAcctype().equals("PRAN") || sdswe.getAcctype().equals("EPF")) {
                    aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromCPF, false, aqDTLSTable);
                }

                hmap.put("processed", 1);
                JSONObject jsonobj = new JSONObject(hmap);
                out.write(jsonobj.toString());
            } else if (aqMastModel.getEmpType().equals("C")) {
                String aqDTLSTable = AqFunctionalities.getAQBillDtlsTable(billMastModel.getAqMonth(), billMastModel.getAqYear());
                aqDtlsDAO.deleteAqdtls(aqslno, aqDTLSTable);
                SectionDtlSPCWiseEmp sdswe = sectionDefinationDAO.getSPCWiseContEmp(aqMastModel.getEmpCode());
                HashMap<String, Integer> payworkday = payBillDAO.getPayWorkDays(aqMastModel, startDate, endDate, daysInMonth);
                PayComponent payComponent = new PayComponent();
                int monBasic = sdswe.getCurBasicSalary();
                aqMastModel.setPayDay(payworkday.get("payday"));
                int curBasic = 0;
                if (payworkday.get("payday") == daysInMonth) {
                    curBasic = monBasic;
                } else {
                    double value = (((double) monBasic) / daysInMonth) * payworkday.get("payday");
                    Long temp = Math.round(value);
                    curBasic = temp.intValue();
                }
                payComponent.setBasic(curBasic);
                aqMastModel.setBankAccNo(sdswe.getBankaccno());
                aqMastModel.setIfscCode(sdswe.getIfscCode());
                aqMastModel.setGrossAmt(curBasic);
                aqMastModel.setCurBasic(curBasic);
                aqMastModel.setMonBasic(monBasic);
                aqmastDAO.updateAqmastdata(aqMastModel);
                int payday = 0;
                if (sdswe.getJobtypeid() == 1) {
                    payday = payBillDAO.getPayDaysContractual(sdswe, billMastModel.getAqMonth() + 1, billMastModel.getAqYear(), daysInMonth);
                } else {
                    payday = payworkday.get("payday");
                }

                ArrayList allowanceList = allowanceDeductionDAO.getAllowanceList(sdswe, billMastModel.getOffCode(), daysInMonth, payday, payday, 0, payComponent);
                ArrayList deductionList = allowanceDeductionDAO.getDeductionList(sdswe.getSpc(), sdswe.getEmpid(), null, aqMastModel.getAqGroup(), billMastModel.getOffCode(), daysInMonth, payday, payday, 0, payComponent, billMastModel.getBillType(), sdswe.getDepcode(), "");
                List principalLoanList = loanSancDAO.getPrincipalLoanListForBill(sdswe.getEmpid(), aqMastModel.getPayDay(), sdswe.getDepcode());
                AqDtlsModel[] AqDtlsModelFromPLoanList = loanSancDAO.getAqDtlsModelFromPLoanList(principalLoanList, aqMastModel);
                AqDtlsModel[] AqDtlsModelFromAllowanceList = allowanceDeductionDAO.getAqDtlsModelForContractualFromAllowanceList(allowanceList, aqMastModel, payComponent, aqMastModel.getEmpType(), aqMastModel.getAqMonth());

                List licList = licDAO.getLicList(sdswe.getEmpid());

                AqDtlsModel[] AqDtlsModelFromLICList = licDAO.getAqDtlsModelFromLICList(licList, aqMastModel);
                AqDtlsModel[] AqDtlsModelFromDeductionList = allowanceDeductionDAO.getAqDtlsModelFromDeductionList(deductionList, aqMastModel, payComponent);
                AqDtlsModel[] AqDtlsModelFromPT = payBillDAO.getAqDtlsModelFromPT(aqMastModel, monBasic, curBasic);
                QuaterAllotment[] qtrallotmentList = empQtrAllotmentDAO.getQuaterAllotmentDetail(sdswe.getEmpid(),curBasic,payComponent.getGp());
                AqDtlsModel[] AqDtlsModelFromQTRList = empQtrAllotmentDAO.getAqDtlsModelFromQtrAllotment(qtrallotmentList, aqMastModel);

                aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromAllowanceList, false, aqDTLSTable);
                aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromPLoanList, false, aqDTLSTable);
                aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromDeductionList, false, aqDTLSTable);
                aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromPT, false, aqDTLSTable);
                aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromQTRList, false, aqDTLSTable);
                aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromLICList, false, aqDTLSTable);
                AqDtlsModel[] AqDtlsModelFromCPF = null;
                if (sdswe.getAcctype().equals("PRAN") && !sdswe.getAccountAssume().equals("Y")) {
                    AqDtlsModelFromCPF = payBillDAO.getAqDtlsModelFromCPF(payComponent, aqMastModel);
                    aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromCPF, false, aqDTLSTable);
                } else if (sdswe.getAcctype().equals("EPF") && lub.getLoginoffcode() != null && (!lub.getLoginoffcode().equals("OLSGAD0010001") && !lub.getLoginoffcode().equals("OLSEDN0010003") && !lub.getLoginoffcode().equals("OLSIND0010002"))) {
                    AqDtlsModelFromCPF = payBillDAO.getAqDtlsModelFromEPF(payComponent, aqMastModel);
                    aqDtlsDAO.saveAqdtlsData(AqDtlsModelFromCPF, false, aqDTLSTable);
                }

                hmap.put("processed", 1);
                JSONObject jsonobj = new JSONObject(hmap);
                out.write(jsonobj.toString());
            }
            //---------------------------Single Reprocess For Non Govt. Aided Employees----------------------------//
            
            else if (aqMastModel.getEmpType().equals("D")) {
                String aqDTLSTable = AqFunctionalities.getAQBillDtlsTable(billMastModel.getAqMonth(), billMastModel.getAqYear());
                aqDtlsDAO.deleteAqdtls(aqslno, aqDTLSTable);
                SectionDtlSPCWiseEmp sdswe = sectionDefinationDAO.getSPCEmpSection(aqMastModel.getEmpCode());
                String gpfSeries = CommonFunctions.getGPFSeries(sdswe.getGpfaccno());
                HashMap<String, Integer> payworkday = payBillDAO.getPayWorkDays(aqMastModel, startDate, endDate, daysInMonth);
                PayComponent payComponent = payBillDAO.getEmployeePayComponent(sdswe, startDate, endDate, daysInMonth);

                boolean payHeldUp = aqmastDAO.stopSalaryForPayHeldUp(aqMastModel.getEmpCode());
                //boolean stopSalaryForRetirement = aqmastDAO.stopNpsDeduction(aqMastModel.getEmpCode(), aqMastModel.getAqMonth(), aqMastModel.getAqYear());
                if (payHeldUp == false) {

                    int monBasic = payComponent.getBasic();

                    aqMastModel.setPayDay(payworkday.get("payday"));
                    int curBasic = 0;
                    if (payworkday.get("payday") == daysInMonth) {
                        curBasic = monBasic;
                    } else {
                        double value = (((double) monBasic) / daysInMonth) * payworkday.get("payday");
                        Long temp = Math.round(value);
                        curBasic = temp.intValue();
                    }
                    if (aqMastModel.getDeptCode() != null && aqMastModel.getDeptCode().equals("05")) {
                        curBasic = Math.round(Float.parseFloat(monBasic + "") / 2);
                    }
                    aqMastModel.setGpfType(gpfSeries);
                    aqMastModel.setBankAccNo(sdswe.getBankaccno());
                    aqMastModel.setIfscCode(sdswe.getIfscCode());
                    aqMastModel.setGrossAmt(curBasic);
                    aqMastModel.setCurBasic(curBasic);
                    aqMastModel.setMonBasic(monBasic);
                    aqMastModel.setPayScale(sdswe.getPayscale());
                    aqMastModel.setDeptCode(sdswe.getDepcode());
                    aqMastModel.setPayCommission(payComponent.getPaycommission() + "");
                    aqmastDAO.updateAqmastdata(aqMastModel);

                    payComponent.setBasic(curBasic);
                    /*Allowance List*/
                    ArrayList allowanceList = allowanceDeductionDAO.getAllowanceList(sdswe, configLvl, aqMastModel.getAqGroup(), billMastModel.getOffCode(), daysInMonth, payworkday.get("payday"), payworkday.get("workday"), 0, payComponent, billMastModel.getBillType(), aqMastModel.getDeptCode(), aqMastModel.getAqMonth(), aqMastModel.getAqYear());

                    AqDtlsModel[] AqDtlsModelFromAllowanceList = allowanceDeductionDAO.getAqDtlsModelFromAllowanceList(allowanceList, aqMastModel, payComponent, aqMastModel.getEmpType(), aqMastModel.getAqMonth());

                    aqDtlsDAO.saveAqdtlsDataDHE(AqDtlsModelFromAllowanceList, false, aqDTLSTable);

                    System.out.println("aqMastModelgross Aided" + aqMastModel.getGrossAmt());

                    /*Allowance List*/
                    ArrayList deductionList = allowanceDeductionDAO.getDeductionListDHE(sdswe, configLvl, aqMastModel, billMastModel, daysInMonth, payworkday.get("payday"), payworkday.get("payday"), 0, payComponent, gpfSeries);
                    List principalLoanList = loanSancDAO.getPrincipalLoanListForBill(sdswe.getEmpid(), aqMastModel.getPayDay(), sdswe.getDepcode());

                    List interestLoanList = loanSancDAO.getInterestLoanListForBill(sdswe.getEmpid(), aqMastModel.getPayDay(), sdswe.getDepcode());
                    List licList = licDAO.getLicList(sdswe.getEmpid());
                    QuaterAllotment[] qtrallotmentList = empQtrAllotmentDAO.getQuaterAllotmentDetail(sdswe.getEmpid(),curBasic,payComponent.getGp());

                    AqDtlsModel[] AqDtlsModelFromDeductionList = allowanceDeductionDAO.getAqDtlsModelFromDeductionList(deductionList, aqMastModel, payComponent);
                    AqDtlsModel[] AqDtlsModelFromPLoanList = loanSancDAO.getAqDtlsModelFromPLoanList(principalLoanList, aqMastModel);
                    AqDtlsModel[] AqDtlsModelFromILoanList = loanSancDAO.getAqDtlsModelFromILoanList(interestLoanList, aqMastModel);
                    AqDtlsModel[] AqDtlsModelFromLICList = licDAO.getAqDtlsModelFromLICList(licList, aqMastModel);
                    AqDtlsModel[] AqDtlsModelFromQTRList = empQtrAllotmentDAO.getAqDtlsModelFromQtrAllotment(qtrallotmentList, aqMastModel);
                    AqDtlsModel[] AqDtlsModelFromPT = payBillDAO.getAqDtlsModelFromPT(aqMastModel, monBasic, curBasic);
                    AqDtlsModel[] AqDtlsModelFromCPF = null;

                    boolean stopMonthlySubscrip = false;
                    if (aqMastModel.getAcctType() != null && !aqMastModel.getAcctType().equals("") && (aqMastModel.getAcctType().equalsIgnoreCase("GPF") || aqMastModel.getAcctType().equalsIgnoreCase("TPF"))) {
                        stopMonthlySubscrip = aqmastDAO.stopGpfDeduction(aqMastModel.getEmpCode(), aqMastModel.getAqMonth(), aqMastModel.getAqYear());
                    } else if (aqMastModel.getAcctType() != null && !aqMastModel.getAcctType().equals("") && aqMastModel.getAcctType().equalsIgnoreCase("PRAN")) {
                        stopMonthlySubscrip = aqmastDAO.stopNpsDeduction(aqMastModel.getEmpCode(), aqMastModel.getAqMonth(), aqMastModel.getAqYear());
                    }

                    if (sdswe.getAcctype().equals("PRAN") && !sdswe.getAccountAssume().equals("Y")) {
                        AqDtlsModelFromCPF = payBillDAO.getAqDtlsModelFromCPF(payComponent, aqMastModel);
                    } else if ((sdswe.getAcctype().equals("GPF") || sdswe.getAcctype().equals("TPF")) && !sdswe.getAccountAssume().equals("Y")) {
                        if (payworkday.get("payday") < 15 || (aqMastModel.getDeptCode() != null && aqMastModel.getDeptCode().equals("05"))) {
                            stopMonthlySubscrip = true;
                        }
                        AqDtlsModelFromCPF = payBillDAO.getAqDtlsModelFromGPF_TPF(payComponent, aqMastModel);
                    } else if (sdswe.getAcctype().equals("EPF") && (!lub.getLoginoffcode().equals("OLSGAD0010001") && !lub.getLoginoffcode().equals("OLSEDN0010003") && !lub.getLoginoffcode().equals("OLSIND0010002"))) {
                        AqDtlsModelFromCPF = payBillDAO.getAqDtlsModelFromEPF(payComponent, aqMastModel);
                      
                    }

                    aqDtlsDAO.saveAqdtlsDataDHE(AqDtlsModelFromDeductionList, stopMonthlySubscrip, aqDTLSTable);
                    aqDtlsDAO.saveAqdtlsDataDHE(AqDtlsModelFromILoanList, stopMonthlySubscrip, aqDTLSTable);
                    aqDtlsDAO.saveAqdtlsDataDHE(AqDtlsModelFromPLoanList, stopMonthlySubscrip, aqDTLSTable);
                    aqDtlsDAO.saveAqdtlsDataDHE(AqDtlsModelFromLICList, stopMonthlySubscrip, aqDTLSTable);
                    aqDtlsDAO.saveAqdtlsDataDHE(AqDtlsModelFromQTRList, stopMonthlySubscrip, aqDTLSTable);
                    aqDtlsDAO.saveAqdtlsDataDHE(AqDtlsModelFromPT, stopMonthlySubscrip, aqDTLSTable);
                    if (sdswe.getAcctype().equals("PRAN") || sdswe.getAcctype().equals("GPF") || sdswe.getAcctype().equals("TPF") || sdswe.getAcctype().equals("EPF")) {
                        aqDtlsDAO.saveAqdtlsDataDHE(AqDtlsModelFromCPF, stopMonthlySubscrip, aqDTLSTable);
                    }

                }

                hmap.put("processed", 1);
                JSONObject jsonobj = new JSONObject(hmap);
                out.write(jsonobj.toString());
            }
       
            
        } catch (Exception e) {
            hmap.put("processed", 0);
            JSONObject jsonobj = new JSONObject(hmap);
            out.write(jsonobj.toString());
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "reprocessArrAqDtls")
    public void reprocessArrAqDtls(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam(value = "aqslno", required = false) String aqslno, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        HashMap<String, Integer> hmap = new HashMap<String, Integer>();
        try {
            out = response.getWriter();
            hmap.put("processed", 1);
            JSONObject jsonobj = new JSONObject(hmap);
            out.write(jsonobj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "reprocessArrAqMast")
    public void reprocessArrAqMast(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam(value = "aqslno", required = false) String aqslno,
            @RequestParam("billNo") int billNo, @RequestParam(value = "empCode", required = false) String empCode,
            HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        HashMap<String, Integer> hmap = new HashMap<String, Integer>();
        int status = 1;
        try {
            out = response.getWriter();
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo + "");
            if (crb.getBillStatusId() == 5 || crb.getBillStatusId() == 7 || crb.getBillStatusId() == 3) {

            } else if (crb.getBillStatusId() < 2 || crb.getBillStatusId() == 4 || crb.getBillStatusId() == 8) {
                if (crb.getTypeofBill().equalsIgnoreCase("ARREAR")) {
                    arrmastDAO.reprocessArrAqMast(billNo, aqslno);
                } else if (crb.getTypeofBill().equalsIgnoreCase("ARREAR_6")) {
                    arrmastDAO.reprocess6ArrAqMast(billNo, aqslno);
                } else if (crb.getTypeofBill().equalsIgnoreCase("LEAVE_ARREAR")) {
                    arrmastDAO.reprocessLeaveArrAqMast(crb, empCode);
                } else if (crb.getTypeofBill().equalsIgnoreCase("ARREAR_NPS")) {
                    arrmastDAO.reprocessNPSArrAqMast(billNo, aqslno);
                } else if (crb.getTypeofBill().equalsIgnoreCase("ARREAR_J")) {
                    status = arrmastDAO.reprocessJudiciaryArrAqMast(billNo, aqslno);
                } else if (crb.getTypeofBill().equalsIgnoreCase("ARREAR_6_J")) {
                    status = arrmastDAO.reprocessJudiciaryArr6AqMast(billNo, aqslno);
                } else {
                    arrmastDAO.reprocessOtherArrAqMast(billNo, aqslno);
                }
                hmap.put("processed", status);
                JSONObject jsonobj = new JSONObject(hmap);
                out.write(jsonobj.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "giveFullArrear")
    public void giveFullArrear(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("aqslno") String aqslno, @RequestParam("billNo") int billNo, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        HashMap<String, Integer> hmap = new HashMap<String, Integer>();
        try {
            out = response.getWriter();
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo + "");
            if (crb.getBillStatusId() == 5 || crb.getBillStatusId() == 7 || crb.getBillStatusId() == 3) {

            } else if (crb.getBillStatusId() < 2 || crb.getBillStatusId() == 4 || crb.getBillStatusId() == 8) {
                arrmastDAO.giveFullArrear(aqslno);

                hmap.put("processed", 1);
                JSONObject jsonobj = new JSONObject(hmap);
                out.write(jsonobj.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "browseAquitanceArr", method = RequestMethod.GET)
    public ModelAndView browseAquitanceArr(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("billNo") int billNo, HttpServletResponse response) {//

        String path = "/payroll/arrear/BrowseArrAquitance";
        ModelAndView mv = new ModelAndView();
        CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo + "");

        if (crb.getTypeofBill().equalsIgnoreCase("ARREAR")) {
            List arrAqList = arrmastDAO.getArrearAcquaintance(billNo);
            path = "/payroll/arrear/BrowseArrAquitance";
            mv = new ModelAndView(path);
            mv.addObject("AqArrList", arrAqList);
        } else if (crb.getTypeofBill().equalsIgnoreCase("OTHER_ARREAR")) {
            List arrAqList = arrmastDAO.getArrearAcquaintance(billNo);
            path = "/payroll/arrear/BrowseOtherArrAquitance";
            mv = new ModelAndView(path);
            mv.addObject("AqArrList", arrAqList);
        } else if (crb.getTypeofBill().equalsIgnoreCase("LEAVE_ARREAR")) {
            List arrAqList = arrmastDAO.getLeaveArrearAcquaintance(billNo);
            path = "/payroll/arrear/BrowseLeaveArrAquitance";
            mv = new ModelAndView(path);
            mv.addObject("AqArrList", arrAqList);
        } else if (crb.getTypeofBill().equalsIgnoreCase("HELDUP_ARREAR")) {
            List arrAqList = arrmastDAO.getLeaveArrearAcquaintance(billNo);
            path = "/payroll/arrear/BrowseLeaveArrAquitance";
            mv = new ModelAndView(path);
            mv.addObject("AqArrList", arrAqList);
        } else if (crb.getTypeofBill().equalsIgnoreCase("ARREAR_NPS")) {
            List arrAqList = arrmastDAO.getNPSArrearAcquaintance(billNo);
            path = "/payroll/arrear/BrowseNPSArrAquitance";
            mv = new ModelAndView(path);
            mv.addObject("AqArrList", arrAqList);
        } else if (crb.getTypeofBill().equalsIgnoreCase("ARREAR_J") || crb.getTypeofBill().equalsIgnoreCase("ARREAR_6_J")) {
            List arrAqList = arrmastDAO.getJudiciaryArrearAquitance(billNo,crb);
            path = "/payroll/arrear/BrowseJudiciaryArrAquitance";
            mv = new ModelAndView(path);
            mv.addObject("AqArrList", arrAqList);
        } else {
            List arrAqList = arrmastDAO.getArrearAcquaintance(billNo);
            path = "/payroll/arrear/BrowseOtherArrAquitance";
            mv = new ModelAndView(path);
            mv.addObject("AqArrList", arrAqList);
        }

        mv.addObject("BillSts", crb.getBillStatusId());
        mv.addObject("arrearPercent", crb.getPercentageArraer());

        mv.addObject("billNo", billNo);

        return mv;
    }

    @RequestMapping(value = "browseAquitanceArrReport", method = RequestMethod.GET)
    public ModelAndView browseAquitanceArrReport(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("billNo") int billNo, HttpServletResponse response) {//

        ModelAndView mv = new ModelAndView("/payroll/arrear/BrowseArrAquitanceReport");
        List arrAqList = arrmastDAO.getArrearAcquaintance(billNo);

        CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo + "");

        mv.addObject("AqArrList", arrAqList);
        mv.addObject("billNo", billNo);
        mv.addObject("typeofbill", crb.getTypeofBill());

        return mv;
    }

    @RequestMapping(value = "browseArrAqDataReport", method = RequestMethod.GET)
    public ModelAndView browseArrAquitanceDataReport(ModelMap model, @ModelAttribute("ArrAqDtlsModel") ArrAqDtlsModel arrMastBean, @RequestParam("aqslno") String aqslno, @RequestParam("billNo") int billNo, HttpServletResponse response) {

        ModelAndView mv = new ModelAndView("/payroll/arrear/BrowseArrAAquitanceDataReport");

        CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo + "");
        List headerDataList = arrmastDAO.getArrearAqHeaderData(billNo);
        ArrAqMastModel arrAqMastBean = arrmastDAO.getArrearAcquaintanceData(aqslno);

        String acctType = arrmastDAO.getAcctType(billNo, arrAqMastBean.getEmpCode());

        mv.addObject("OffName", crb.getOfficename());
        mv.addObject("DeptName", crb.getDeptname());
        mv.addObject("HeaderDataList", headerDataList);
        mv.addObject("arrAqMastBean", arrAqMastBean);
        mv.addObject("acctType", acctType);

        return mv;
    }
    
     @RequestMapping(value = "browseArrAq25DataReport", method = RequestMethod.GET)
    public ModelAndView browseArrAquitance25DataReport(ModelMap model, @ModelAttribute("ArrAqDtlsModel") ArrAqDtlsModel arrMastBean, @RequestParam("aqslno") String aqslno, @RequestParam("billNo") int billNo, HttpServletResponse response) {

        ModelAndView mv = new ModelAndView("/payroll/arrear/BrowseArrAAquitanceDataReport");
        CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo + "");
        
        List headerDataList = arrmastDAO.getArrearAqHeaderData(billNo);
         //System.out.println(headerDataList);
        ArrAqMastModel arrAqMastBean = arrmastDAO.getArrearAcquaintanceData(aqslno);

        String acctType = arrmastDAO.getAcctType(billNo, arrAqMastBean.getEmpCode());
      
        mv.addObject("OffName", crb.getOfficename());
        mv.addObject("DeptName", crb.getDeptname());
        mv.addObject("HeaderDataList", headerDataList);
        mv.addObject("arrAqMastBean", arrAqMastBean);
        mv.addObject("acctType", acctType);

        return mv;
    }

    @RequestMapping(value = "browseJudiciaryArrAqDataReport", method = RequestMethod.GET)
    public ModelAndView browseJudiciaryArrAqDataReport(ModelMap model, @ModelAttribute("ArrAqDtlsModel") ArrAqDtlsModel arrMastBean, @RequestParam("aqslno") String aqslno, @RequestParam("billNo") int billNo, HttpServletResponse response) {

        ModelAndView mv = new ModelAndView("/payroll/arrear/BrowseJudiciaryArrAquitanceDataReport");

        CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo + "");
        List headerDataList = arrmastDAO.getArrearAqHeaderData(billNo);
        if (crb.getTypeofBill() != null && crb.getTypeofBill().equals("ARREAR_6_J")) {
            ArrAqMastModel arrAqMastBean = arrmastDAO.getJudiciaryArrear6AcquaintanceData(aqslno);
            String acctType = arrmastDAO.getAcctType(billNo, arrAqMastBean.getEmpCode());
            mv.addObject("arrAqMastBean", arrAqMastBean);
            mv.addObject("acctType", acctType);
        } else {
            ArrAqMastModel arrAqMastBean = arrmastDAO.getJudiciaryArrearAcquaintanceData(aqslno);
            String acctType = arrmastDAO.getAcctType(billNo, arrAqMastBean.getEmpCode());
            mv.addObject("arrAqMastBean", arrAqMastBean);
            mv.addObject("acctType", acctType);
        }

        mv.addObject("OffName", crb.getOfficename());
        mv.addObject("DeptName", crb.getDeptname());
        mv.addObject("HeaderDataList", headerDataList);

        return mv;
    }

    @RequestMapping(value = "browse6ArrAqDataReport", method = RequestMethod.GET)
    public ModelAndView browse6ArrAquitanceDataReport(ModelMap model, @ModelAttribute("ArrAqDtlsModel") ArrAqDtlsModel arrMastBean, @RequestParam("aqslno") String aqslno, @RequestParam("billNo") int billNo, HttpServletResponse response) {

        ModelAndView mv = new ModelAndView();
        
        String path = "/payroll/arrear/Browse6ArrAAquitanceDataReport";
        
        CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo + "");
        List headerDataList = arrmastDAO.getArrearAqHeaderData(billNo);
        
        if(crb.getTypeofBill() != null && crb.getTypeofBill().equals("ARREAR_6_J")){
            ArrAqMastModel arrAqMastBean = arrmastDAO.getJudiciaryArrear6AcquaintanceData(aqslno);
            mv.addObject("arrAqMastBean", arrAqMastBean);
            path = "/payroll/arrear/BrowseJudiciary6ArrAquitanceDataReport";
        }else{
            ArrAqMastModel arrAqMastBean = arrmastDAO.getArrearAcquaintanceData(aqslno);
            mv.addObject("arrAqMastBean", arrAqMastBean);
        }
        mv.addObject("OffName", crb.getOfficename());
        mv.addObject("DeptName", crb.getDeptname());
        mv.addObject("HeaderDataList", headerDataList);
        mv.setViewName(path);
        
        return mv;
    }

    @RequestMapping(value = "browseFullArrAqDataReport", method = RequestMethod.GET)
    public ModelAndView browseFullArrAqDataReport(ModelMap model, @ModelAttribute("ArrAqDtlsModel") ArrAqDtlsModel arrMastBean, @RequestParam("aqslno") String aqslno, @RequestParam("billNo") int billNo, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();

        CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo + "");
        if (crb.getTypeofBill().equals("ARREAR")) {
            mv.setViewName("/payroll/arrear/BrowseFullArrAAquitanceDataReport");
            ArrAqMastModel arrAqMastBean = arrmastDAO.getArrearAcquaintanceData(aqslno);
            mv.addObject("arrAqMastBean", arrAqMastBean);

            String accType = arrmastDAO.getAcctType(billNo, arrAqMastBean.getEmpCode());
            mv.addObject("accType", accType);
        } else if (crb.getTypeofBill().equals("ARREAR_NPS")) {
            mv.setViewName("/payroll/arrear/BrowseNPSArrAquitanceDataReport");
            ArrAqMastModel arrAqMastBean = arrmastDAO.getNPSArrearAquitanceData(aqslno);
            mv.addObject("arrAqMastBean", arrAqMastBean);

            String accType = arrmastDAO.getAcctType(billNo, arrAqMastBean.getEmpCode());
            mv.addObject("accType", accType);
        } else {
            mv.setViewName("/payroll/arrear/BrowseOtherArrAAquitanceDataReport");
            ArrAqMastModel arrAqMastBean = arrmastDAO.getOtherArrearAcquaintanceData(aqslno);
            mv.addObject("arrAqMastBean", arrAqMastBean);

            String accType = arrmastDAO.getAcctType(billNo, arrAqMastBean.getEmpCode());
            mv.addObject("accType", accType);
        }
        List headerDataList = arrmastDAO.getArrearAqHeaderData(billNo);
        mv.addObject("OffName", crb.getOfficename());
        mv.addObject("DeptName", crb.getDeptname());
        mv.addObject("HeaderDataList", headerDataList);

        return mv;
    }

    @RequestMapping(value = "browseArrAqData", method = RequestMethod.GET)
    public ModelAndView browseArrAquitanceData(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ArrAqDtlsModel") ArrAqDtlsModel arrMastBean,
            @RequestParam(value = "aqslno", required = false) String aqslno,
            @RequestParam(value = "billNo", required = true) int billNo,
            @RequestParam(value = "empCode", required = false) String empCode,
            HttpServletResponse response) {

        arrMastBean.setAqslno(aqslno);
        ModelAndView mv = new ModelAndView("/payroll/arrear/BrowseArrAAquitanceData", "command", arrMastBean);

        CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo + "");
        if (crb.getTypeofBill().equals("ARREAR")) {
            mv.setViewName("/payroll/arrear/BrowseArrAAquitanceData");
            ArrAqMastModel arrAqMastBean = arrmastDAO.getArrearAcquaintanceData(aqslno);
            String acctType = arrmastDAO.getAcctType(billNo, arrAqMastBean.getEmpCode());

            mv.addObject("arrAqMastBean", arrAqMastBean);
            mv.addObject("acctType", acctType);
        } else if (crb.getTypeofBill().equals("ARREAR_6")) {
            mv.setViewName("/payroll/arrear/BrowseArr6AquitanceData");
            ArrAqMastModel arrAqMastBean = arrmastDAO.get6ArrearAcquaintanceData(aqslno);
            mv.addObject("arrAqMastBean", arrAqMastBean);
            mv.addObject("usertype", lub.getLoginusertype());
        } else if (crb.getTypeofBill().equals("LEAVE_ARREAR")) {
            List<ArrAqMastModel> arrAqMastModels = arrmastDAO.getArrearAcquaintanceData(billNo, empCode);
            mv.addObject("arrAqMastModels", arrAqMastModels);
            mv.setViewName("/payroll/arrear/BrowseLeaveArrAquitanceData");
        } else if (crb.getTypeofBill().equals("HELDUP_ARREAR")) {
            List<ArrAqMastModel> arrAqMastModels = arrmastDAO.getArrearAcquaintanceData(billNo, empCode);
            mv.addObject("arrAqMastModels", arrAqMastModels);
            mv.setViewName("/payroll/arrear/BrowseLeaveArrAquitanceData");
        } else if (crb.getTypeofBill().equals("ARREAR_NPS")) {
            mv.setViewName("/payroll/arrear/BrowseNPSArrAquitanceData");
            ArrAqMastModel arrAqMastBean = arrmastDAO.getNPSArrearAquitanceData(aqslno);
            String acctType = arrmastDAO.getAcctType(billNo, arrAqMastBean.getEmpCode());

            mv.addObject("arrAqMastBean", arrAqMastBean);
            mv.addObject("acctType", acctType);
        } else if (crb.getTypeofBill().equals("ARREAR_J")) {
            ArrAqMastModel arrAqMastBean = arrmastDAO.getJudiciaryArrearAcquaintanceData(aqslno);
            String acctType = arrmastDAO.getAcctType(billNo, arrAqMastBean.getEmpCode());
            boolean isIRHeadPresent = arrmastDAO.isIRHeadPresent(aqslno);

            mv.addObject("isIRHeadPresent", isIRHeadPresent);
            mv.addObject("arrAqMastBean", arrAqMastBean);
            mv.addObject("acctType", acctType);

            mv.setViewName("/payroll/arrear/BrowseJudiciaryArrAquitanceData");
        } else if (crb.getTypeofBill().equals("ARREAR_6_J")) {
            ArrAqMastModel arrAqMastBean = arrmastDAO.getJudiciaryArrear6AcquaintanceData(aqslno);
            String acctType = arrmastDAO.getAcctType(billNo, arrAqMastBean.getEmpCode());
            boolean isIRHeadPresent = arrmastDAO.isIRHeadPresent(aqslno);

            mv.addObject("isIRHeadPresent", isIRHeadPresent);
            mv.addObject("arrAqMastBean", arrAqMastBean);
            mv.addObject("acctType", acctType);

            mv.setViewName("/payroll/arrear/BrowseJudiciaryArrAquitanceData");
        } else {
            ArrAqMastModel arrAqMastBean = arrmastDAO.getOtherArrearAcquaintanceData(aqslno);
            
            boolean isIRHeadPresent = arrmastDAO.isIRHeadPresent(aqslno);
            String acctType = arrmastDAO.getAcctType(billNo, arrAqMastBean.getEmpCode());

            mv.addObject("isIRHeadPresent", isIRHeadPresent);
            mv.addObject("arrAqMastBean", arrAqMastBean);
            mv.addObject("acctType", acctType);

            mv.setViewName("/payroll/arrear/BrowseOtherArrAAquitanceData");
        }
        List headerDataList = arrmastDAO.getArrearAqHeaderData(billNo);

        mv.addObject("OffName", crb.getOfficename());
        mv.addObject("DeptName", crb.getDeptname());
        mv.addObject("HeaderDataList", headerDataList);

        mv.addObject("BillSts", crb.getBillStatusId());

        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "addAllowanceToArrear", method = RequestMethod.GET)
    public void addAllowanceToArrear(ModelMap model,
            @RequestParam(value = "aqslno", required = true) String aqslno,
            @RequestParam(value = "adType", required = true) String adType,
            HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = null;
        arrmastDAO.addAllowanceToArrear(aqslno + "", adType + "");
        out = response.getWriter();
        out.write("Completed");
        out.flush();
        out.close();
    }

    @ResponseBody
    @RequestMapping(value = "editArrData")
    public void GetArrDtlsDataJSON(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("billNo") int billNo,
            @RequestParam("aqslno") String aqSlno, @RequestParam("pmonth") int pMonth, @RequestParam("pyear") int pYear, @RequestParam("calcuniqueno") int calcuniqueno, @RequestParam("autoincrid") int autoincrid) {

        response.setContentType("application/json");
        PrintWriter out = null;

        try {
            out = response.getWriter();
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo + "");

            if (crb.getBillStatusId() == 5 || crb.getBillStatusId() == 7 || crb.getBillStatusId() == 3) {

            } else if (crb.getBillStatusId() < 2 || crb.getBillStatusId() == 4 || crb.getBillStatusId() == 8) {
                JSONObject json = null;
                if (crb.getTypeofBill().equals("ARREAR")) {
                    ArrAqDtlsModel arrAqDtlsBean = arrmastDAO.getArrearAcquaintanceDataList(aqSlno, pMonth, pYear, calcuniqueno);//getOtherArrearAcquaintanceDataList
                    json = new JSONObject(arrAqDtlsBean);
                } else if (crb.getTypeofBill().equals("ARREAR_J") || crb.getTypeofBill().equals("ARREAR_6_J")) {
                    ArrAqDtlsModel arrAqDtlsBean = arrmastDAO.getJudiciaryArrearAquitanceDataList(aqSlno, pMonth, pYear, calcuniqueno);//getOtherArrearAcquaintanceDataList
                    json = new JSONObject(arrAqDtlsBean);
                } else if (crb.getTypeofBill().equals("ARREAR_6")) {
                    ArrAqDtlsModel arrAqDtlsBean = arrmastDAO.getArrear6AcquaintanceDataList(aqSlno, pMonth, pYear, calcuniqueno, autoincrid);
                    json = new JSONObject(arrAqDtlsBean);
                } else {
                    ArrAqDtlsModel arrAqDtlsBean = arrmastDAO.getOtherArrearAcquaintanceDataList(aqSlno, pMonth, pYear, calcuniqueno);
                    json = new JSONObject(arrAqDtlsBean);
                }

                out.write(json.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "deleteArrAqDtls")
    public void deleteArrAqDtls(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("ArrAqDtlsModel") ArrAqDtlsModel arrDtlsBean, @ModelAttribute("billNo") int billNo) {

        response.setContentType("application/json");
        PrintWriter out = null;
        int deleted = 0;
        try {
            out = response.getWriter();
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo + "");
            if (crb.getBillStatusId() == 5 || crb.getBillStatusId() == 7 || crb.getBillStatusId() == 3) {

            } else if (crb.getBillStatusId() < 2 || crb.getBillStatusId() == 4 || crb.getBillStatusId() == 8) {

                deleted = arrmastDAO.deleteArrDtlsData(arrDtlsBean);

                HashMap<String, Integer> hmap = new HashMap<String, Integer>();
                hmap.put("deleted", deleted);
                JSONObject jsonobj = new JSONObject(hmap);
                out.write(jsonobj.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "deleteMassArrAqDtls")
    public ModelAndView deleteMassArrAqDtls(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("ArrAqDtlsModel") ArrAqDtlsModel arrDtlsBean, @ModelAttribute("billNo") int billNo, @RequestParam("aqSlNoArr") String aqSlNoArr) {

        try {
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo + "");
            if (crb.getBillStatusId() == 5 || crb.getBillStatusId() == 7 || crb.getBillStatusId() == 3) {

            } else if (crb.getBillStatusId() < 2 || crb.getBillStatusId() == 4 || crb.getBillStatusId() == 8) {
                if (aqSlNoArr != null && !aqSlNoArr.equals("")) {
                    String[] tempaqSlNoArr = aqSlNoArr.split(",");
                    if (tempaqSlNoArr.length > 0) {
                        for (int i = 0; i < tempaqSlNoArr.length; i++) {
                            String[] inneraqSlNoArr = tempaqSlNoArr[i].split("@");

                            arrDtlsBean.setAqslno(inneraqSlNoArr[0]);
                            arrDtlsBean.setPayMonth(Integer.parseInt(inneraqSlNoArr[1]));
                            arrDtlsBean.setPayYear(Integer.parseInt(inneraqSlNoArr[2]));
                            arrDtlsBean.setCalcuniqueno(Integer.parseInt(inneraqSlNoArr[3]));

                            arrmastDAO.deleteArrDtlsData(arrDtlsBean);
                        }

                    }
                } else {
                    //deleted = arrmastDAO.deleteArrDtlsData(arrDtlsBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return new ModelAndView("redirect:/browseArrAqData.htm?aqslno=" + arrDtlsBean.getAqslno() + "&billNo=" + billNo);
    }

    @ResponseBody
    @RequestMapping(value = "updateArrDtls")
    public void UpdateArrDtlsData(HttpServletResponse response, @ModelAttribute() ArrAqDtlsModel arrDtlsBean) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            int updSts = arrmastDAO.updateArrDtlsData(arrDtlsBean);
            ArrAqDtlsModel arrAqDtlsBean = arrmastDAO.getArrearAcquaintanceDataList(arrDtlsBean.getAqslno(), arrDtlsBean.getPayMonth(), arrDtlsBean.getPayYear(), arrDtlsBean.getCalcuniqueno());
            JSONObject json = new JSONObject(arrAqDtlsBean);
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
    @RequestMapping(value = "updateArr6Dtls")
    public void updateArr6Dtls(HttpServletResponse response, @ModelAttribute() ArrAqDtlsModel arrDtlsBean) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            int updSts = arrmastDAO.updateArr6DtlsData(arrDtlsBean);
            ArrAqDtlsModel arrAqDtlsBean = arrmastDAO.getArrear6AcquaintanceDataList(arrDtlsBean.getAqslno(), arrDtlsBean.getPayMonth(), arrDtlsBean.getPayYear(), arrDtlsBean.getCalcuniqueno(), arrDtlsBean.getAutoincrid());
            JSONObject json = new JSONObject(arrAqDtlsBean);
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
    @RequestMapping(value = "UpdateOtherArrDtlsData")
    public void UpdateOtherArrDtlsData(HttpServletResponse response, @ModelAttribute() ArrAqDtlsModel arrDtlsBean) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            int updSts = arrmastDAO.updateOtherArrDtlsData(arrDtlsBean);
            ArrAqDtlsModel arrAqDtlsBean = arrmastDAO.getOtherArrearAcquaintanceDataList(arrDtlsBean.getAqslno(), arrDtlsBean.getPayMonth(), arrDtlsBean.getPayYear(), arrDtlsBean.getCalcuniqueno());
            JSONObject json = new JSONObject(arrAqDtlsBean);
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
    @RequestMapping(value = "searchEmpForArrMast")
    public void searchEmpForArrMast(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("searchemp") String searchemp) {
        response.setContentType("application/json");
        PrintWriter out = null;
        PayRevisionOption payRevisionOption = arrmastDAO.searchEmployee(searchemp);
        try {
            JSONObject json = new JSONObject(payRevisionOption);
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
    @RequestMapping(value = "addEmployeeToBill")
    public void addEmployeeToBill(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("arrAqMastModel") ArrAqMastModel arrAqMastModel) {
        PayRevisionOption payRevisionOption = arrmastDAO.searchEmployee(arrAqMastModel.getEmpCode());
        String aqslNo = "";
        if (payRevisionOption.getMsgcode() == 0) {
            aqslNo = arrmastDAO.addEmployeeToBill(arrAqMastModel);
        } else if (payRevisionOption.getMsgcode() == 2) {
            arrmastDAO.insertIntoPayRevisionOption(arrAqMastModel.getInputChoiceDate(), arrAqMastModel.getPayrevisionbasic(), arrAqMastModel.getEmpCode());
            aqslNo = arrmastDAO.addEmployeeToBill(arrAqMastModel);
        }
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            HashMap<String, String> hmap = new HashMap<String, String>();
            hmap.put("aqslNo", aqslNo);
            JSONObject jsonobj = new JSONObject(hmap);
            out = response.getWriter();
            out.write(jsonobj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "saveArrData")
    public void SaveArrDtlsData(HttpServletResponse response, @ModelAttribute() ArrAqDtlsModel arrDtlsBean) {

        response.setContentType("application/json");
        PrintWriter out = null;

        try {

            ArrAqDtlsModel[] arrAqDtlsModels = new ArrAqDtlsModel[3];

            ArrAqDtlsModel arrAqDtlsModel = new ArrAqDtlsModel();

            arrAqDtlsModel.setAqslno(arrDtlsBean.getAqslno());
            arrAqDtlsModel.setPayMonth(arrDtlsBean.getPayMonth());
            arrAqDtlsModel.setPayYear(arrDtlsBean.getPayYear());
            arrAqDtlsModel.setAdType("PAY");
            arrAqDtlsModel.setDueAmt(arrDtlsBean.getDuePayAmt());
            arrAqDtlsModel.setDrawnAMt(arrDtlsBean.getDrawnPayAmt());
            arrAqDtlsModel.setDrawnBillNo(arrDtlsBean.getDrawnBillNo());
            arrAqDtlsModel.setRefaqslno("0");
            arrAqDtlsModels[0] = arrAqDtlsModel;

            arrAqDtlsModel = new ArrAqDtlsModel();
            arrAqDtlsModel.setAqslno(arrDtlsBean.getAqslno());
            arrAqDtlsModel.setPayMonth(arrDtlsBean.getPayMonth());
            arrAqDtlsModel.setPayYear(arrDtlsBean.getPayYear());
            arrAqDtlsModel.setAdType("GP");
            arrAqDtlsModel.setDueAmt(arrDtlsBean.getDueGpAmt());
            arrAqDtlsModel.setDrawnAMt(arrDtlsBean.getDrawnGpAmt());
            arrAqDtlsModel.setDrawnBillNo(arrDtlsBean.getDrawnBillNo());
            arrAqDtlsModel.setRefaqslno("0");
            arrAqDtlsModels[1] = arrAqDtlsModel;

            arrAqDtlsModel = new ArrAqDtlsModel();
            arrAqDtlsModel.setAqslno(arrDtlsBean.getAqslno());
            arrAqDtlsModel.setPayMonth(arrDtlsBean.getPayMonth());
            arrAqDtlsModel.setPayYear(arrDtlsBean.getPayYear());
            arrAqDtlsModel.setAdType("DA");
            arrAqDtlsModel.setDueAmt(arrDtlsBean.getDueDaAmt());
            arrAqDtlsModel.setDrawnAMt(arrDtlsBean.getDrawnDaAmt());
            arrAqDtlsModel.setDrawnBillNo(arrDtlsBean.getDrawnBillNo());
            arrAqDtlsModel.setRefaqslno("0");
            arrAqDtlsModels[2] = arrAqDtlsModel;

            int calcUniqueNo = arrmastDAO.getCalcUniqueNo(arrDtlsBean.getAqslno());
            arrmastDAO.saveArrdtlsdata(arrAqDtlsModels, calcUniqueNo);

            JSONObject json = new JSONObject('Y');
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
    @RequestMapping(value = "saveOtherArrData")
    public void SaveOtherArrData(HttpServletResponse response, @ModelAttribute() ArrAqDtlsModel arrDtlsBean) {

        response.setContentType("application/json");
        PrintWriter out = null;

        try {

            ArrAqDtlsModel[] arrAqDtlsModels = new ArrAqDtlsModel[5];

            ArrAqDtlsModel arrAqDtlsModel = new ArrAqDtlsModel();

            arrAqDtlsModel.setAqslno(arrDtlsBean.getAqslno());
            arrAqDtlsModel.setPayMonth(arrDtlsBean.getPayMonth());
            arrAqDtlsModel.setPayYear(arrDtlsBean.getPayYear());
            arrAqDtlsModel.setAdType("PAY");
            arrAqDtlsModel.setDueAmt(arrDtlsBean.getDuePayAmt());
            arrAqDtlsModel.setDrawnAMt(arrDtlsBean.getDrawnPayAmt());
            arrAqDtlsModel.setDrawnBillNo(arrDtlsBean.getDrawnBillNo());
            arrAqDtlsModel.setRefaqslno("0");
            arrAqDtlsModels[0] = arrAqDtlsModel;

            arrAqDtlsModel = new ArrAqDtlsModel();
            arrAqDtlsModel.setAqslno(arrDtlsBean.getAqslno());
            arrAqDtlsModel.setPayMonth(arrDtlsBean.getPayMonth());
            arrAqDtlsModel.setPayYear(arrDtlsBean.getPayYear());
            arrAqDtlsModel.setAdType("GP");
            arrAqDtlsModel.setDueAmt(arrDtlsBean.getDueGpAmt());
            arrAqDtlsModel.setDrawnAMt(arrDtlsBean.getDrawnGpAmt());
            arrAqDtlsModel.setDrawnBillNo(arrDtlsBean.getDrawnBillNo());
            arrAqDtlsModel.setRefaqslno("0");
            arrAqDtlsModels[1] = arrAqDtlsModel;

            arrAqDtlsModel = new ArrAqDtlsModel();
            arrAqDtlsModel.setAqslno(arrDtlsBean.getAqslno());
            arrAqDtlsModel.setPayMonth(arrDtlsBean.getPayMonth());
            arrAqDtlsModel.setPayYear(arrDtlsBean.getPayYear());
            arrAqDtlsModel.setAdType("DA");
            arrAqDtlsModel.setDueAmt(arrDtlsBean.getDueDaAmt());
            arrAqDtlsModel.setDrawnAMt(arrDtlsBean.getDrawnDaAmt());
            arrAqDtlsModel.setDrawnBillNo(arrDtlsBean.getDrawnBillNo());
            arrAqDtlsModel.setRefaqslno("0");
            arrAqDtlsModels[2] = arrAqDtlsModel;

            arrAqDtlsModel = new ArrAqDtlsModel();
            arrAqDtlsModel.setAqslno(arrDtlsBean.getAqslno());
            arrAqDtlsModel.setPayMonth(arrDtlsBean.getPayMonth());
            arrAqDtlsModel.setPayYear(arrDtlsBean.getPayYear());
            arrAqDtlsModel.setAdType("HRA");
            arrAqDtlsModel.setDueAmt(arrDtlsBean.getDueHraAmt());
            arrAqDtlsModel.setDrawnAMt(arrDtlsBean.getDrawnHraAmt());
            arrAqDtlsModel.setDrawnBillNo(arrDtlsBean.getDrawnBillNo());
            arrAqDtlsModel.setRefaqslno("0");
            arrAqDtlsModels[3] = arrAqDtlsModel;

            arrAqDtlsModel = new ArrAqDtlsModel();
            arrAqDtlsModel.setAqslno(arrDtlsBean.getAqslno());
            arrAqDtlsModel.setPayMonth(arrDtlsBean.getPayMonth());
            arrAqDtlsModel.setPayYear(arrDtlsBean.getPayYear());
            arrAqDtlsModel.setAdType("OA");
            arrAqDtlsModel.setDueAmt(arrDtlsBean.getDueOaAmt());
            arrAqDtlsModel.setDrawnAMt(arrDtlsBean.getDrawnOaAmt());
            arrAqDtlsModel.setDrawnBillNo(arrDtlsBean.getDrawnBillNo());
            arrAqDtlsModel.setRefaqslno("0");
            arrAqDtlsModels[4] = arrAqDtlsModel;

            int calcUniqueNo = arrmastDAO.getCalcUniqueNo(arrDtlsBean.getAqslno());
            arrmastDAO.saveArrdtlsdata(arrAqDtlsModels, calcUniqueNo);

            JSONObject json = new JSONObject('Y');
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
    @RequestMapping(value = "saveJudiciaryArrData")
    public void SaveJudiciaryArrData(HttpServletResponse response, @ModelAttribute() ArrAqDtlsModel arrDtlsBean) {

        response.setContentType("application/json");
        PrintWriter out = null;

        try {

            ArrAqDtlsModel[] arrAqDtlsModels = new ArrAqDtlsModel[4];

            ArrAqDtlsModel arrAqDtlsModel = new ArrAqDtlsModel();

            arrAqDtlsModel.setAqslno(arrDtlsBean.getAqslno());
            arrAqDtlsModel.setPayMonth(arrDtlsBean.getPayMonth());
            arrAqDtlsModel.setPayYear(arrDtlsBean.getPayYear());
            arrAqDtlsModel.setAdType("PAY");
            arrAqDtlsModel.setDueAmt(arrDtlsBean.getDuePayAmt());
            arrAqDtlsModel.setDrawnAMt(arrDtlsBean.getDrawnPayAmt());
            arrAqDtlsModel.setDrawnBillNo(arrDtlsBean.getDrawnBillNo());
            arrAqDtlsModel.setRefaqslno("0");
            arrAqDtlsModels[0] = arrAqDtlsModel;

            arrAqDtlsModel = new ArrAqDtlsModel();
            arrAqDtlsModel.setAqslno(arrDtlsBean.getAqslno());
            arrAqDtlsModel.setPayMonth(arrDtlsBean.getPayMonth());
            arrAqDtlsModel.setPayYear(arrDtlsBean.getPayYear());
            arrAqDtlsModel.setAdType("GP");
            arrAqDtlsModel.setDueAmt(arrDtlsBean.getDueGpAmt());
            arrAqDtlsModel.setDrawnAMt(arrDtlsBean.getDrawnGpAmt());
            arrAqDtlsModel.setDrawnBillNo(arrDtlsBean.getDrawnBillNo());
            arrAqDtlsModel.setRefaqslno("0");
            arrAqDtlsModels[1] = arrAqDtlsModel;

            arrAqDtlsModel = new ArrAqDtlsModel();
            arrAqDtlsModel.setAqslno(arrDtlsBean.getAqslno());
            arrAqDtlsModel.setPayMonth(arrDtlsBean.getPayMonth());
            arrAqDtlsModel.setPayYear(arrDtlsBean.getPayYear());
            arrAqDtlsModel.setAdType("DA");
            arrAqDtlsModel.setDueAmt(arrDtlsBean.getDueDaAmt());
            arrAqDtlsModel.setDrawnAMt(arrDtlsBean.getDrawnDaAmt());
            arrAqDtlsModel.setDrawnBillNo(arrDtlsBean.getDrawnBillNo());
            arrAqDtlsModel.setRefaqslno("0");
            arrAqDtlsModels[2] = arrAqDtlsModel;

            arrAqDtlsModel = new ArrAqDtlsModel();
            arrAqDtlsModel.setAqslno(arrDtlsBean.getAqslno());
            arrAqDtlsModel.setPayMonth(arrDtlsBean.getPayMonth());
            arrAqDtlsModel.setPayYear(arrDtlsBean.getPayYear());
            arrAqDtlsModel.setAdType("IR");
            arrAqDtlsModel.setDueAmt(arrDtlsBean.getDueIrAmt());
            arrAqDtlsModel.setDrawnAMt(arrDtlsBean.getDrawnIrAmt());
            arrAqDtlsModel.setDrawnBillNo(arrDtlsBean.getDrawnBillNo());
            arrAqDtlsModel.setRefaqslno("0");
            arrAqDtlsModels[3] = arrAqDtlsModel;

            int calcUniqueNo = arrmastDAO.getCalcUniqueNo(arrDtlsBean.getAqslno());
            arrmastDAO.saveArrdtlsdata(arrAqDtlsModels, calcUniqueNo);

            JSONObject json = new JSONObject('Y');
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
    @RequestMapping(value = "stoppay")
    public ModelAndView stopPay(@RequestParam("aqslno") String aqslno, @RequestParam("billNo") String billNo) {
        ModelAndView mv = null;
        try {
            BillDetail bill = billBrowserDao.getBillDetails(Integer.parseInt(billNo));
            if ((bill.getBillStatusId() < 2) || (bill.getBillStatusId() == 4) || (bill.getBillStatusId() == 8)) {
                aqReportDao.stopPay(aqslno);
            }
            mv = new ModelAndView("redirect:/browseAquitance.htm?billNo=" + billNo);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mv;
    }

    @RequestMapping(value = "downloadFullArrDataReportExcel")
    public void downloadFullArrDataReportExcel(HttpServletResponse response, @RequestParam("billNo") int billNo) {

        try {
            Workbook workbook = new XSSFWorkbook();

            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo + "");

            arrmastDAO.downloadFullArrDataReportExcel(workbook, billNo + "", crb.getTypeofBill());

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=\"ArrearData_" + billNo + ".xlsx\"");

            workbook.write(response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
