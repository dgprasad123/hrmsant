/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.services;

import hrms.common.CommonFunctions;
import hrms.common.DMPUtil;
import hrms.common.Numtowordconvertion;
import hrms.dao.payroll.billbrowser.AqReportDAO;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.billbrowser.AcquaintanceBean;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author lenovo pc
 */
@Service
public class BankScheduleServices {

    public static final int characterPerLine = 132;
    public static final int lineperpage = 62;
    public AqReportDAO aqReportDAO;
    public PayBillDMPDAO paybillDmpDao;

    public void setAqReportDAO(AqReportDAO aqReportDAO) {
        this.aqReportDAO = aqReportDAO;
    }

    public void setPaybillDmpDao(PayBillDMPDAO paybillDmpDao) {
        this.paybillDmpDao = paybillDmpDao;
    }

    

    //public BillFrontpageDAO billFrontPageDmpDao;
    private void printHeader(DMPUtil dmpUtil, CommonReportParamBean crb, String billdesc, String billdate, int pageNo) throws Exception {
        int payBillMonth = 0;
        int payBillYear = 0;
        String offname = crb.getOfficeen();
        dmpUtil.writeToFile((char) 27);
        dmpUtil.writeToFile((char) 69);
        dmpUtil.writeToFile(StringUtils.center(offname + ((char) 27) + ((char) 70), characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.rightPad("Bill No :" + ((char) 27) + ((char) 69) + crb.getBilldesc() + ((char) 27) + ((char) 70), characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.rightPad("Schedule - Bank Statement", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.rightPad("SCHEDULE OF -  Employee-wise list with SB A/C No.(Name of the Bank)", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.rightPad("               Net Amount, Loan/ Advance Liability Amount", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.rightPad("               Amount Credited to SB A/C of the employee", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.rightPad("               Amount Credited to CA A/C of DDO", characterPerLine, " "));
        if (crb.getAqmonth() <= 10) {
            payBillMonth = crb.getAqmonth() + 1;
            payBillYear = crb.getAqyear();
        } else {
            payBillMonth = 0;
            payBillYear = crb.getAqyear() + 1;
        }
        dmpUtil.writeToFile(StringUtils.rightPad("For " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + " " + crb.getAqyear() + " payable in " + CommonFunctions.getMonthAsString(payBillMonth) + " " + payBillYear + "'", characterPerLine, " "));
        dmpUtil.writeToFile(" ");
        dmpUtil.writeToFile("Name of the DDO  who maintains these accounts");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------------------------------------------------------");
        dmpUtil.writeToFile("Sl  SB A/C No./        Name/                         (PF NO)           Loan/Advance          Amt Credited to      Amt Credited to   ");
        dmpUtil.writeToFile("NO  Name of the        Designation                Net Amount              Liability           SB A/C of  the        CA A/C of the   ");
        dmpUtil.writeToFile("    Bank                                            (in Rs.)          Amount(in Rs)          employee(in Rs)          DDO (in Rs)   ");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------------------------------------------------------");
        dmpUtil.writeToFile(" 1   2                 3                                4                      5                      6                    7        ");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------------------------------------------------------");
        dmpUtil.writeToFile(" ");
    }

    public void printCarryForward(DMPUtil dmpUtil, int pagetotal, int pageNo, boolean isLastPage) throws Exception {
        if (isLastPage == true) {
            dmpUtil.writeToFile("------------------------------------------------------------------------------------------------------------------------------------");
            dmpUtil.writeToFile(" TOTAL :                                          " + StringUtils.leftPad(pagetotal + "", 10));
        } else {
            dmpUtil.writeToFile(" CARRIED FROM PREVIOUS PAGE :                     " + StringUtils.leftPad(pagetotal + "", 10));
        }
        dmpUtil.writeToFile("------------------------------------------------------------------------------------------------------------------------------------");
    }

    public File write(String billNo, String folderPath, String fileSeparator, CommonReportParamBean crb) throws Exception {
        String fileName = "BANKSCHEDULE.txt";
        int year = 0;
        int month = 0;
        String billdesc = "";
        String billdate = "";
        String originalFileName = "BANKSCHEDULE.txt";
        String desigArray[] = null;
        boolean dataFound = false;
        boolean createFile = false;
        File dmp = null;

        try {
            year = crb.getAqyear();
            month = crb.getAqmonth();
            billdesc = crb.getBilldesc();
            folderPath = folderPath+ fileSeparator;
            originalFileName = folderPath + originalFileName;
            billdate = crb.getBilldate();

            ArrayList<AcquaintanceBean> acquaintanceList = aqReportDAO.getAcquaintance(billNo);

            DMPUtil dmpUtil = null;

            int slno = 0;
            int pageNo = 0;
            boolean isHeaderPrinted = false;
            boolean isPageTotalPrinted = false;
            int pagetotal = 0;
            int linePrinted = 0;
            for (AcquaintanceBean aq : acquaintanceList) {
                if (createFile == false) {
                    dmpUtil = new DMPUtil(folderPath, fileName);
                    createFile = true;
                }
                if (aq.getEmpname() != null && !aq.getEmpname().equals("")) {
                    dataFound = true;
                    desigArray = wrapText(aq.getCurdesg(), 25);
                    if (slno == 0 || slno % 14 == 0) {
                        pageNo++;
                        if (pageNo == 1) {
                            dmpUtil.writeToFile((char) 27);
                            dmpUtil.writeToFile((char) 64);
                            dmpUtil.writeToFile((char) 15);
                        } else {
                            dmpUtil.writeToFile((char) 12);
                            dmpUtil.writeToFile((char) 27);
                            dmpUtil.writeToFile((char) 64);
                            dmpUtil.writeToFile((char) 15);
                        }
                        printHeader(dmpUtil, crb, billdesc, billdate, pageNo);
                        linePrinted = linePrinted + 18;
                        if (pageNo > 1) {
                            printCarryForward(dmpUtil, pagetotal, pageNo, false);
                        }
                        isHeaderPrinted = true;
                        isPageTotalPrinted = false;
                    }
                    slno++;
                    int privatededuction = paybillDmpDao.getPrivateDeduction(aq.getAqslno(),billNo);
                    int privateloan = paybillDmpDao.getPrivateLoan(aq.getAqslno(),billNo);
                    int grosspay = paybillDmpDao.getGrossPay(aq.getAqslno(), billNo);
                    int totaldedn = paybillDmpDao.getTotalDedn(aq.getAqslno(), billNo);
                    int netPay = grosspay - totaldedn;
                    pagetotal = pagetotal + netPay;
                    String banckAccNo = aq.getBankaccno();
                    if (banckAccNo == null) {
                        banckAccNo = "";
                    }
                    dmpUtil.writeToFile(StringUtils.rightPad(slno + "", 4, " ") + StringUtils.rightPad(banckAccNo, 17, " ") + StringUtils.rightPad(aq.getEmpname(), 26, " ") + StringUtils.leftPad(aq.getBankaccno(), 13, " "));
                    linePrinted++;
                    dmpUtil.writeToFile(StringUtils.repeat(" ", 4) + StringUtils.repeat(" ", 17) + StringUtils.rightPad(desigArray[0], 26, " ") + StringUtils.leftPad(netPay + "", 13, " ") + StringUtils.leftPad((privatededuction + privateloan) + "", 22) + StringUtils.leftPad((grosspay - (totaldedn + privatededuction + privateloan)) + "", 25) + StringUtils.leftPad(((privatededuction + privateloan)) + "", 22));
                    linePrinted++;
                    if (desigArray.length > 1) {
                        dmpUtil.writeToFile(StringUtils.repeat(" ", 4) + StringUtils.repeat(" ", 17) + StringUtils.rightPad(desigArray[1], 26, " "));
                    } else {
                        dmpUtil.writeToFile("");
                    }
                    linePrinted++;
                    if (slno != 1 && slno % 14 == 0) {
                        dmpUtil.writeToFile("------------------------------------------------------------------------------------------------------------------------------------");
                        linePrinted = 0;
                        dmpUtil.writeToFile("  Page  Total : " + StringUtils.repeat(" ", 30) + StringUtils.leftPad(pagetotal + "", 13, " "));
                        isPageTotalPrinted = true;
                        isHeaderPrinted = false;
                    }
                }
            }
            if (createFile == true) {
                printCarryForward(dmpUtil, pagetotal, pageNo, true);
                dmpUtil.writeToFile("");
                dmpUtil.writeToFile(StringUtils.leftPad("RUPEES " + StringUtils.upperCase(Numtowordconvertion.convertNumber(pagetotal)) + " ONLY", characterPerLine));
                dmpUtil.writeToFile("");
                dmpUtil.writeToFile(StringUtils.leftPad(StringUtils.defaultString(crb.getDdoname()), characterPerLine));
                for (int i = 2; i < (lineperpage - (linePrinted + 5)); i++) {
                    dmpUtil.writeToFile("");
                }
                dmpUtil.writeToFile("------------------------------------------------------------------------------------------------------------------------------------");
                dmpUtil.writeToFile((char) 12);
                dmpUtil.writeToFile((char) 26);
                
            }
            dmp = new File(originalFileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        
        return dmp;
    }

    private String[] wrapText(String text, int len) {
        // return empty array for null text
        if (text == null) {
            return new String[]{};
        }

        // return text if len is zero or less
        if (len <= 0) {
            return new String[]{text};
        }

        // return text if less than length
        if (text.length() <= len) {
            return new String[]{text};
        }

        char[] chars = text.toCharArray();
        Vector lines = new Vector();
        StringBuffer line = new StringBuffer();
        StringBuffer word = new StringBuffer();

        for (int i = 0; i < chars.length; i++) {
            word.append(chars[i]);

            if (chars[i] == ' ') {
                if ((line.length() + word.length()) > len) {
                    lines.add(line.toString());
                    line.delete(0, line.length());
                }

                line.append(word);
                word.delete(0, word.length());
            }
        }

        // handle any extra chars in current word
        if (word.length() > 0) {
            if ((line.length() + word.length()) > len) {
                lines.add(line.toString());
                line.delete(0, line.length());
            }
            line.append(word);
        }

        // handle extra line
        if (line.length() > 0) {
            lines.add(line.toString());
        }

        String[] ret = new String[lines.size()];
        int c = 0; // counter
        for (Enumeration e = lines.elements(); e.hasMoreElements(); c++) {
            ret[c] = (String) e.nextElement();
        }

        return ret;
    }
}
