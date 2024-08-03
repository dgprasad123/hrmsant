package hrms.dao.payroll.services;

import hrms.common.CommonFunctions;
import hrms.common.DMPUtil;
import hrms.dao.payroll.payslip.PaySlipDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.payslip.ADDetails;
import hrms.model.payroll.payslip.PaySlipDetailBean;
import hrms.model.payroll.payslip.PaySlipListBean;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class PaySlipIIDMPServices {

    public static final int characterPerLine = 87;
    public static final int lineperpage = 32;

    private PaySlipDAO payslipDao;

    public void setPayslipDao(PaySlipDAO payslipDao) {
        this.payslipDao = payslipDao;
    }

    private void printHeader(DMPUtil dmpUtil, CommonReportParamBean crb, int pageNo) throws Exception {
        String deptname = crb.getOfficeen();
        dmpUtil.writeToFile((char) 27);
        dmpUtil.writeToFile((char) 69);
        dmpUtil.writeToFile(StringUtils.center(crb.getOfficename() + ((char) 27) + ((char) 70), characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.rightPad("Payslip for the month of : " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + " ' " + crb.getAqyear() + "'" + "   Bill No:" + crb.getBilldesc(), characterPerLine, " "));
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
    }

    public void write(String billno, String folderPath, String fileSeparator, CommonReportParamBean crb) throws Exception {

        String fileName = "PAYSLIPF2.txt";

        int year = 0;
        int month = 0;
        String billdesc = "";
        String billdate = "";
        String vchno = "";

        double billGrossAmt = 0;
        double billDeductionAmt = 0;
        double billNetAmt = 0;

        ADDetails[] allowanceListArr = null;
        List allowanceList = null;
        Iterator allowanceIterator = null;

        ADDetails[] deductionListArr = null;
        List deductionList = null;
        Iterator deductionIterator = null;

        List privateDeductionList = null;
        Iterator privateDeductionIterator = null;
        
        List loanList = null;
        Iterator loanIterator = null;

        try {
            year = crb.getAqyear();
            month = crb.getAqmonth();
            billdesc = crb.getBilldesc();
            folderPath = folderPath + fileSeparator;
            billdate = crb.getBilldate();

            PaySlipDetailBean billAmtDetail = payslipDao.billAmt(billno, year, month);

            vchno = StringUtils.defaultString(billAmtDetail.getVchno());
            if (vchno != null && !vchno.equals("")) {
                if (vchno.length() > 4) {
                    vchno = vchno.substring(0, 4) + "-" + vchno.substring(4);
                }
            }
            if (billAmtDetail.getVchdate() != null && !billAmtDetail.getVchdate().equals("")) {
                vchno = vchno + " dtd " + billAmtDetail.getVchdate();
            }

            billGrossAmt = billAmtDetail.getBillGrossAmt();
            billDeductionAmt = billAmtDetail.getBillDeductionAmt();
            billNetAmt = billAmtDetail.getBillNetAmt();

            List<PaySlipListBean> emplist = payslipDao.getBillWiseEmployeeList(billno, year, month);
            DMPUtil dmpUtil = new DMPUtil(folderPath, fileName);
            //PayrollCommonFunction pcf = new PayrollCommonFunction();
            int slno = 0;
            int pageNo = 0;
            int basic = 0;

            for (PaySlipListBean list : emplist) {
                int lineNo = 0;
                if (list.getEmpName() != null && !list.getEmpName().equals("")) {
                    if (list.getBasic() != null && !list.getBasic().equals("")) {
                        basic = Integer.parseInt(list.getBasic());
                    }
                    if (slno == 0 || slno % 2 == 0) {
                        pageNo++;
                        if (pageNo == 1) {
                            dmpUtil.writeToFile((char) 32);
                            dmpUtil.writeToFile((char) 32);
                            dmpUtil.writeToFile((char) 32);
                            dmpUtil.writeToFile((char) 27);
                            dmpUtil.writeToFile((char) 64);
                            dmpUtil.writeToFile((char) 32);
                            dmpUtil.writeToFile((char) 32);
                            dmpUtil.writeToFile((char) 18);
                            dmpUtil.writeToFile((char) 27);
                            dmpUtil.writeToFile((char) 120);
                            dmpUtil.writeToFile((char) 48);
                        } else {
                            dmpUtil.writeToFile((char) 12);
                            dmpUtil.writeToFile((char) 32);
                            dmpUtil.writeToFile((char) 32);
                            dmpUtil.writeToFile((char) 32);
                            dmpUtil.writeToFile((char) 27);
                            dmpUtil.writeToFile((char) 64);
                            dmpUtil.writeToFile((char) 32);
                            dmpUtil.writeToFile((char) 32);
                            dmpUtil.writeToFile((char) 18);
                            dmpUtil.writeToFile((char) 27);
                            dmpUtil.writeToFile((char) 120);
                            dmpUtil.writeToFile((char) 48);
                        }
                    }
                    dmpUtil.writeToFile("");
                    printHeader(dmpUtil, crb, pageNo);
                    lineNo = lineNo + 3;
                    dmpUtil.writeToFile("Name         :" + StringUtils.rightPad(list.getEmpName(), 41) + "HRMS ID   : " + list.getEmpId());
                    dmpUtil.writeToFile("Designation  :" + StringUtils.rightPad(list.getCurDesg(), 41) + "GPF A/C   : " + list.getGpfNo());
                    dmpUtil.writeToFile("PAN No       :" + StringUtils.rightPad(StringUtils.defaultString(list.getPanNo()), 41) + "Days Attn. : " + list.getPayDay());
                    dmpUtil.writeToFile("Bank A/c No  :" + StringUtils.rightPad(StringUtils.defaultString(list.getBankaccno()), 41) + "DOS       : " + list.getDos());
                    dmpUtil.writeToFile("Brass No     :" + StringUtils.rightPad(StringUtils.defaultString(list.getBrassNo()), 41));
                    dmpUtil.writeToFile("----------------------------------------------------------------------------------------");
                    dmpUtil.writeToFile("Earnings...           ! Deductions...                             ! Pvt. Ded....");
                    lineNo = lineNo + 5;
                    
                    
                    String aqslNo = list.getAqslno();
                    
                    allowanceListArr = payslipDao.getAllowanceDeductionList(aqslNo, "A", year, month);
                    allowanceList = Arrays.asList(allowanceListArr);
                    deductionListArr = payslipDao.getAllowanceDeductionList(aqslNo, "D", year, month);
                    deductionList = Arrays.asList(deductionListArr);
                    loanList = payslipDao.getLoanList(aqslNo, year, month);
                    privateDeductionList = payslipDao.getPrivateDedeuctionListForPaySlip(aqslNo, year, month);
                    
                    if (allowanceList != null && allowanceList.size() > 0) {
                        allowanceIterator = allowanceList.iterator();
                    }
                    if (deductionList != null && deductionList.size() > 0) {
                        deductionIterator = deductionList.iterator();
                    }
                    if (loanList != null && loanList.size() > 0) {
                        loanIterator = loanList.iterator();
                    }
                    if (privateDeductionList != null && privateDeductionList.size() > 0) {
                        privateDeductionIterator = privateDeductionList.iterator();
                    }
                    
                    String temp = "";
                    
                    boolean dataAvailable = false;
                    boolean deductionavl = false;
                    int grossPay = 0;
                    int deduction = 0;
                    int pvtdedn = 0;
                    int j = 0;
                    for (int i = 0; i < 18; i++) {
                        String row = "";
                        dataAvailable = false;
                        deductionavl = false;
                        /*ALLOWANCES*/
                        if (i == 0) {
                            temp = StringUtils.defaultString("Basic");
                            if (temp.length() > 13) {
                                temp = StringUtils.left(temp, 13);
                            }
                            grossPay = grossPay + basic;
                            row = row + StringUtils.rightPad(temp, 13) + ":" + StringUtils.leftPad(basic + "", 7) + " ! ";
                            dataAvailable = true;
                        } else {
                            if (allowanceIterator != null && allowanceIterator.hasNext()) {
                                ADDetails adDetails = (ADDetails) allowanceIterator.next();
                                temp = StringUtils.defaultString(adDetails.getAdCodeDesc());
                                grossPay = grossPay + adDetails.getAdAmt();
                                row = row + StringUtils.rightPad(temp, 13) + ":" + StringUtils.leftPad(adDetails.getAdAmt()+"", 7) + " ! ";
                                dataAvailable = true;
                            }
                        }
                        if (row.equals("")) {
                            row = StringUtils.repeat(" ", 21) + " ! ";
                        }
                        /*DEDUCTIONS*/
                        temp = "";
                        if (deductionIterator != null && deductionIterator.hasNext()) {
                            ADDetails adDetails = (ADDetails) deductionIterator.next();
                            temp = StringUtils.defaultString(adDetails.getAdCodeDesc());
                            if (temp.length() > 13) {
                                temp = StringUtils.left(temp, 13);
                            }
                            String licnoStr = adDetails.getAccNumber();
                            int repeatLen = 0;
                            if (licnoStr != null && !licnoStr.equals("")) {
                                repeatLen = 21 - licnoStr.length();
                            } else {
                                repeatLen = 21;
                            }
                            deduction = deduction + adDetails.getAdAmt();
                            row = row + StringUtils.rightPad(temp, 13) + ":" + StringUtils.leftPad(StringUtils.defaultString(adDetails.getAdAmt()+""), 6) + " " + StringUtils.defaultString(adDetails.getAccNumber()) + StringUtils.repeat(" ", repeatLen) + "!";
                            dataAvailable = true;
                            deductionavl = true;
                        } else {
                            j++;
                            /*LOANS*/
                            if (j <= 2) {
                                if (j == 0) {
                                    row = row + StringUtils.repeat("-", 42);
                                } else if (j == 1) {
                                    row = row + "Description    Amount   Instl. Balance    !";
                                } else if (j == 2) {
                                    row = row + StringUtils.repeat("-", 42) + "!";
                                }
                                dataAvailable = true;
                                deductionavl = true;
                            } else {
                                if (loanIterator != null && loanIterator.hasNext()) {
                                    ADDetails adDetails = (ADDetails) loanIterator.next();
                                    temp = StringUtils.defaultString(adDetails.getAdCode());
                                    deduction = deduction + adDetails.getAdAmt();
                                    int balanceAmt = 0;
                                    if (adDetails.getNowDedn() != null && adDetails.getNowDedn().equals("P")) {
                                        balanceAmt = adDetails.getPrincipalAmt() - adDetails.getTotRecAmt();
                                        temp = temp + "(P)";
                                    } else if (adDetails.getNowDedn() != null && adDetails.getNowDedn().equals("I")) {
                                        balanceAmt = adDetails.getInterestAmt() - adDetails.getTotRecAmt();
                                        temp = temp + "(I)";
                                    }
                                    row = row + StringUtils.rightPad(temp, 13) + ":" + StringUtils.leftPad(StringUtils.defaultString(adDetails.getAdAmt()+""), 6) + StringUtils.leftPad(StringUtils.defaultString(adDetails.getRefDesc()), 9) + StringUtils.leftPad(balanceAmt + "", 9) + "    !";
                                    dataAvailable = true;
                                    deductionavl = true;
                                }
                            }

                        }
                        /*Private Deductions*/
                        if (privateDeductionIterator != null && privateDeductionIterator.hasNext()) {
                            ADDetails adDetails = (ADDetails) privateDeductionIterator.next();
                            temp = StringUtils.defaultString(adDetails.getAdCodeDesc());
                            pvtdedn = pvtdedn + adDetails.getAdAmt();
                            if (temp.length() > 13) {
                                temp = StringUtils.left(temp, 13);
                            }
                            if (deductionavl == true) {
                                row = row + StringUtils.rightPad(temp, 13) + ":" + StringUtils.leftPad(StringUtils.defaultString(adDetails.getAdAmt()+""), 6);
                            } else {
                                row = row + StringUtils.repeat(" ", 42) + "!" + StringUtils.rightPad(temp, 13) + ":" + StringUtils.leftPad(StringUtils.defaultString(adDetails.getAdAmt()+""), 6);
                            }
                            dataAvailable = true;
                        }
                        if (dataAvailable == true) {
                            dmpUtil.writeToFile(row);
                            lineNo = lineNo + 1;
                        }
                    }

                    int netPay = grossPay - (deduction + pvtdedn);
                    if (netPay < 0) {
                        netPay = Math.abs(netPay);
                    }
                    dmpUtil.writeToFile("----------------------------------------------------------------------------------------");
                    dmpUtil.writeToFile(StringUtils.rightPad("Gross Pay", 13) + ":" + StringUtils.rightPad(" ", 2) + StringUtils.rightPad(grossPay + "", 8) + StringUtils.rightPad("Total Dedn.", 12) + ":" + StringUtils.rightPad(" ", 2) + StringUtils.rightPad(deduction + "", 8) + StringUtils.rightPad("Pvt Dedn.", 12) + ":" + StringUtils.rightPad(" ", 2) + StringUtils.rightPad(pvtdedn + "", 9) + StringUtils.rightPad("Net Pay", 10) + ":" + StringUtils.rightPad(" ", 2) + StringUtils.rightPad(netPay + "", 9));
                    dmpUtil.writeToFile(StringUtils.leftPad("(Rupees " + StringUtils.upperCase(CommonFunctions.convertNumber(netPay) + " ) Only"), 78));
                    dmpUtil.writeToFile("----------------------------------------------------------------------------------------");
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile(StringUtils.leftPad("Authorised Signatory/Bill Assistant", 78));
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("N.B YOU MAY ALSO VIEW THIS PAYSLIP BY USING HRMS ODISHA USING FOLLOWING URL");
                    dmpUtil.writeToFile("(http://hrmsorissa.gov.in)");
                    lineNo = lineNo + 8;
                    slno++;

                    if (slno % 2 == 0) {
                        dmpUtil.writeToFile("");
                    } else {
                        int requiredBlankLine = (lineperpage - lineNo) - 1;
                        for (int k = 1; k < requiredBlankLine; k++) {
                            dmpUtil.writeToFile("");
                        }
                        dmpUtil.writeToFile("------------Cut Here---------------Cut Here--------------------Cut Here---------");
                        dmpUtil.writeToFile("");
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
}
