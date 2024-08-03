package hrms.dao.payroll.services;

import hrms.common.CommonFunctions;
import hrms.common.DMPUtil;
import hrms.dao.payroll.payslip.PaySlipDAO;
import hrms.dao.payroll.payslip.PaySlipDAOImpl;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.payslip.ADDetails;
import hrms.model.payroll.payslip.PaySlipDetailBean;
import hrms.model.payroll.payslip.PaySlipListBean;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;


public class PaySlipIDMPServices {

    public static final int characterPerLine = 87;
    public static final int lineperpage = 32;

    private PaySlipDAO payslipDao;

    public PaySlipDAO getPayslipDao() {
        return payslipDao;
    }

    public void setPayslipDao(PaySlipDAO payslipDao) {
        this.payslipDao = payslipDao;
    }

    private void printHeader(DMPUtil dmpUtil, CommonReportParamBean crb, String billdesc, String billdate, int pageNo) throws Exception {
        String deptname = crb.getOfficeen();

        dmpUtil.writeToFile((char) 27);
        dmpUtil.writeToFile((char) 69);
        dmpUtil.writeToFile(StringUtils.center(crb.getOfficename() + ((char) 27) + ((char) 70), characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.rightPad("Payslip for the month of : " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + " ' " + crb.getAqyear() + "'", characterPerLine, " "));
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
    }

    public void write(String billno, String folderPath, String fileSeparator, CommonReportParamBean crb) throws Exception {

        String fileName = "PAYSLIP.txt";

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

        List loanList = null;
        Iterator loanIterator = null;

        String daRate = "";

        //String prev_da_date = "";
        String prev_da_rate = "";

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

            //DA Verification Start
            /*SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
            Date prevdadate = null;
            historystmt = con.createStatement();
            historyrs = historystmt.executeQuery("SELECT WEF_FROM,AD_FORMULA FROM G_AD_LIST_HISTORY WHERE AD_CODE_NAME='DA'");
            if (historyrs.next()) {
                //prev_da_date = historyrs.getString("WEF_FROM");
                prevdadate = sdf.parse(CommonFunctions.getFormattedOutputDate(historyrs.getDate("WEF_FROM")));
                prev_da_rate = historyrs.getString("AD_FORMULA");
            }

            String billDateString = "25-" + CommonFunctions.getMonthAsString(Integer.parseInt(month)) + "-" + year;
            String new_da_date = "15-OCT-2014";
            Date curbillDate = sdf.parse(billDateString);
            Date newdadate = sdf.parse(new_da_date);
            if (curbillDate.compareTo(newdadate) < 0) {
                if (curbillDate.compareTo(prevdadate) > 0) {
                    daRate = prev_da_rate;
                }
            } else {
                st = con.createStatement();
                rs = st.executeQuery("SELECT AD_FORMULA FROM G_AD_LIST WHERE AD_CODE_NAME='DA' ");
                if (rs.next()) {
                    daRate = rs.getString("AD_FORMULA");
                }
            }*/
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
                    String payCommission = "";
                    if(list.getPayCommission() != null && !list.getPayCommission().equals("")){
                        if(list.getPayCommission().equals("6")){
                            payCommission = "Scale of Pay ";
                        }else if(list.getPayCommission().equals("7")){
                            payCommission = "Pay Matrix   ";
                        }
                    }else {
                        payCommission = "Scale of Pay ";
                    }
                    daRate = list.getDaformula();
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
                    printHeader(dmpUtil, crb, billdesc, billdate, pageNo);
                    lineNo = lineNo + 3;
                    dmpUtil.writeToFile("HRMS ID      :" + StringUtils.rightPad(list.getEmpId(), 41) + "Bill No    : " + billdesc);
                    dmpUtil.writeToFile("Name         :" + StringUtils.rightPad(list.getEmpName(), 41) + "Bill Gross : " + billGrossAmt);
                    dmpUtil.writeToFile("Designation  :" + StringUtils.rightPad(list.getCurDesg(), 41) + "Bill Net   : " + billNetAmt);
                    dmpUtil.writeToFile("GPF A/C      :" + StringUtils.rightPad(list.getGpfNo(), 41) + "TV No & Dt : " + vchno);
                    dmpUtil.writeToFile("PAN No       :" + StringUtils.rightPad(StringUtils.defaultString(list.getPanNo()), 41) + "Days Attn. : " + list.getPayDay());
                    dmpUtil.writeToFile(payCommission+":" + StringUtils.rightPad(StringUtils.defaultString(list.getPayScale()), 41) + "DA Rate    : " + daRate);
                    dmpUtil.writeToFile("Bank A/c No  :" + StringUtils.rightPad(StringUtils.defaultString(list.getBankaccno()), 41) + "Brass No   : " + StringUtils.defaultString(list.getBrassNo()));
                    dmpUtil.writeToFile("----------------------------------------------------------------------------------------");
                    dmpUtil.writeToFile("Earnings...           ! Deductions...        !   Loan/Advance Recovery...");
                    lineNo = lineNo + 8;

                    String aqslNo = list.getAqslno();

                    allowanceListArr = payslipDao.getAllowanceDeductionList(aqslNo, "A", year, month);
                    allowanceList = Arrays.asList(allowanceListArr);
                    deductionListArr = payslipDao.getAllowanceDeductionList(aqslNo, "D", year, month);
                    deductionList = Arrays.asList(deductionListArr);
                    loanList = payslipDao.getLoanList(aqslNo, year, month);

                    if (allowanceList != null && allowanceList.size() > 0) {
                        allowanceIterator = allowanceList.iterator();
                    }
                    if (deductionList != null && deductionList.size() > 0) {
                        deductionIterator = deductionList.iterator();
                    }
                    if (loanList != null && loanList.size() > 0) {
                        loanIterator = loanList.iterator();
                    }

                    String temp = "";
                    boolean dataAvailable = false;
                    int grossPay = 0;
                    int deduction = 0;
                    for (int i = 0; i < 18; i++) {
                        String row = "";
                        dataAvailable = false;
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
                                temp = StringUtils.defaultString(adDetails.getAdCode());
                                grossPay = grossPay + adDetails.getAdAmt();
                                //row = row + StringUtils.rightPad(temp, 13) + ":" + StringUtils.leftPad(adDetails.getAdDesc(), 7) + " ! ";
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
                            //temp = StringUtils.defaultString(adDetails.getAdCode());
                            temp = StringUtils.defaultString(adDetails.getAdCodeDesc());
                            if (temp.length() > 13) {
                                temp = StringUtils.left(temp, 13);
                            }
                            deduction = deduction + adDetails.getAdAmt();
                            //row = row + StringUtils.rightPad(temp, 13) + ":" + StringUtils.leftPad(StringUtils.defaultString(adDetails.getAdDesc()), 6) + " !";
                            row = row + StringUtils.rightPad(temp, 13) + ":" + StringUtils.leftPad(StringUtils.defaultString(adDetails.getAdAmt()+""), 6) + " !";
                            dataAvailable = true;
                        }
                        if (temp.equals("")) {
                            row = row + StringUtils.repeat(" ", 20) + " !";
                        }
                        /*LOANS*/
                        if (i <= 2) {
                            if (i == 0) {
                                row = row + StringUtils.repeat("-", 42);
                            } else if (i == 1) {
                                row = row + "Description    Amount   Instl. Balance";
                            } else if (i == 2) {
                                row = row + StringUtils.repeat("-", 42);
                            }
                            dataAvailable = true;
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
                                row = row + StringUtils.rightPad(temp, 13) + ":" + StringUtils.leftPad(StringUtils.defaultString(adDetails.getAdAmt() + ""), 6) + StringUtils.leftPad(StringUtils.defaultString(adDetails.getRefDesc()), 9) + StringUtils.leftPad(balanceAmt + "", 9);
                                dataAvailable = true;
                            }
                        }
                        if (dataAvailable == true) {
                            dmpUtil.writeToFile(row);
                            lineNo = lineNo + 1;
                        }
                    }
                    int netPay = grossPay - deduction;
                    if (netPay < 0) {
                        netPay = Math.abs(netPay);
                    }
                    dmpUtil.writeToFile("----------------------------------------------------------------------------------------");
                    dmpUtil.writeToFile(StringUtils.rightPad("Gross Pay", 13) + ":" + StringUtils.rightPad(" ", 2) + StringUtils.rightPad(grossPay + "", 9) + StringUtils.rightPad("Total Dedn.", 12) + ":" + StringUtils.rightPad(" ", 2) + StringUtils.rightPad(deduction + "", 9) + StringUtils.rightPad("Net Pay", 10) + ":" + StringUtils.rightPad(" ", 2) + StringUtils.rightPad(netPay + "", 9));
                    dmpUtil.writeToFile(StringUtils.leftPad("(Rupees " + StringUtils.upperCase(CommonFunctions.convertNumber(netPay) + " ) Only"), 78));
                    dmpUtil.writeToFile("----------------------------------------------------------------------------------------");
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile(StringUtils.leftPad("Authorised Signatory/Bill Assistant", 78));
                    dmpUtil.writeToFile("");
                    //dmpUtil.writeToFile("N.B YOU MAY ALSO VIEW THIS PAYSLIP BY USING HRMS ODISHA USING FOLLOWING URL");
                    //dmpUtil.writeToFile("WISH YOU HAPPY NEW YEAR 2014");
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
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {

        }
    }

}
