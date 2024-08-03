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
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.LoanAdvanceScheduleBean;
import hrms.model.payroll.schedule.Schedule;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Surendra
 */
public class PIAdvPriRecScheduleServices {

    public static final int characterPerLine = 87;
    public static final int lineperpage = 62;

    public AqReportDAO aqReportDao;

    public ScheduleDAO comonScheduleDao;

    public PayBillDMPDAO paybillDmpDao;

    public void setAqReportDao(AqReportDAO aqReportDao) {
        this.aqReportDao = aqReportDao;
    }

    public void setComonScheduleDao(ScheduleDAO comonScheduleDao) {
        this.comonScheduleDao = comonScheduleDao;
    }

    public void setPaybillDmpDao(PayBillDMPDAO paybillDmpDao) {
        this.paybillDmpDao = paybillDmpDao;
    }

    private void printHeader(DMPUtil dmpUtil, CommonReportParamBean crb, String billdesc, String schedule, String loanname, int pageNo) throws Exception {
        String deptname = crb.getOfficeen();
        dmpUtil.writeToFile(StringUtils.center("         Page : " + pageNo, characterPerLine, " "));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile((char) 27);
        dmpUtil.writeToFile((char) 69);
        dmpUtil.writeToFile(StringUtils.rightPad("Bill No : " + StringUtils.defaultString(billdesc) + ((char) 27) + ((char) 70), characterPerLine, " "));
        dmpUtil.writeToFile("");
        if (schedule.equalsIgnoreCase("OVDL")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname + " (PRINCIPAL)", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile("");
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + StringUtils.defaultString(billdesc), characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        }
        if (schedule.equalsIgnoreCase("MOPA")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname + " (PRINCIPAL)", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile(StringUtils.rightPad("7610-LOANS TO GOVERNMENT SERVANTS ETC-202-ADVANCES FOR PURCHASE OF MOTOR", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.rightPad("CONVEYANCE-0080-Loans to Govt. servants for purchase of motor", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.rightPad("conveyance-13001-Advance for purchase of Motor Car/Motor Cycle", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + StringUtils.defaultString(billdesc), characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        }
        if (schedule.equalsIgnoreCase("MCA")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname + " (PRINCIPAL)", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile(StringUtils.rightPad("7610-LOANS TO GOVERNMENT SERVANTS ETC-202-ADVANCES FOR PURCHASE OF MOTOR-", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.rightPad("CONVEYANCE-0080-Loans to Govt. servants for purchase of motor", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.rightPad("conveyance-13001-Advance for purchase of Motor Car/Motor Cycle", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + StringUtils.defaultString(billdesc), characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        }
        if (schedule.equalsIgnoreCase("FA")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + StringUtils.defaultString(billdesc), characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        }
        if (schedule.equalsIgnoreCase("GISA")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile(StringUtils.rightPad("7610-LOANS TO GOVERNMENT SERVANTS ETC 800-OTHER ADVANCES", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.rightPad(" 0109- Other Loans to Govt. servants 13004-Group Insurance Scheme", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + StringUtils.defaultString(billdesc), characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        }
        if (schedule.equalsIgnoreCase("GA")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + billdesc, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        }
        if (schedule.equalsIgnoreCase("HBA")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname + " (PRINCIPAL)", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile(StringUtils.rightPad("7610-LOANS TO GOVERNMENT SERVANTS ETC-201-HOUSE BUILDING ADVANCES ", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.rightPad("0079-Loans to Govt. servants for construction of house-13072-Normal House Building Advance", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + StringUtils.defaultString(billdesc), characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        }
        if (schedule.equalsIgnoreCase("MAL")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + billdesc, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        }
        if (schedule.equalsIgnoreCase("SHBA")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname + " (PRINCIPAL)", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile(StringUtils.rightPad("7610-LOANS TO GOVERNMENT SERVANTS ETC-201-HOUSE BUILDING ADVANCES ", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.rightPad("0079-Loans to Govt. servants for construction of house-13073-Special House Building Advance", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + StringUtils.defaultString(billdesc), characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        }
        if (schedule.equalsIgnoreCase("BICA")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + billdesc, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        }
        if (schedule.equalsIgnoreCase("VE")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + StringUtils.defaultString(billdesc), characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        }
        if (schedule.equalsIgnoreCase("OR")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + billdesc, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        }
        if (schedule.equalsIgnoreCase("PA")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + StringUtils.defaultString(billdesc), characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        }
        if (schedule.equalsIgnoreCase("CMPA")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + StringUtils.defaultString(billdesc), characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        }

        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------------");
        dmpUtil.writeToFile(" Sl   Name & Desig-  Month      Amount   No.of   Amount  Total    Balance  Remarks");
        dmpUtil.writeToFile(" No.  nation         in which   of       Instal- deduct  recovery out-");
        dmpUtil.writeToFile("                     Original   Original ments   from    to the   standing");
        dmpUtil.writeToFile("                     Advance    Advance          bill    end");
        dmpUtil.writeToFile("                     was Drawn                   against of the");
        dmpUtil.writeToFile("                                                         princ.  month ");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------------");
        dmpUtil.writeToFile("            1            2         3        4      5       6          7        8");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------------");
    }

    private void printPageFooter(DMPUtil dmpUtil, CommonReportParamBean crb, int pageTotal) {
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------------");
        dmpUtil.writeToFile(StringUtils.rightPad("* Page Total * :", 16) + StringUtils.leftPad("" + pageTotal, 45));
        dmpUtil.writeToFile("------------------------------------------------------------------------------------------");
        dmpUtil.writeToFile(StringUtils.leftPad("In Words(RUPEES " + StringUtils.upperCase(Numtowordconvertion.convertNumber(pageTotal) + ") ONLY"), characterPerLine));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.rightPad("Desig. of the DDO: " + StringUtils.defaultString(crb.getDdoname()), characterPerLine, " "));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
    }

    private void printCarryForward(DMPUtil dmpUtil, int pageTotal, int pageNo) {
        dmpUtil.writeToFile(StringUtils.rightPad("CARRIED FROM PAGE :" + pageNo, 22) + StringUtils.leftPad("" + pageTotal, 38));
        dmpUtil.writeToFile("------------------------------------------------------------------------------------------");
    }

    public boolean write(String billno, String schedule, String loanName, String folderPath, String fileSeparator, CommonReportParamBean crb) throws Exception {
        String schdesc = "";
        String demandno = "";
        int year = 0;
        int month = 0;
        String billdesc = "";
        String billdate = "";
        String fileName = "";
        boolean dataFound = false;
        boolean createFile = false;
        try {
            fileName = schedule + "-PRI-SCHEDULE.txt";
            Schedule sc = aqReportDao.getScheduleList(schedule);
            schdesc = sc.getScheduleDesc();
            demandno = sc.getDemandNo();

            month = crb.getAqmonth();
            year = crb.getAqyear();
            billdesc = crb.getBilldesc();
            folderPath = folderPath + fileSeparator;
            billdate = crb.getBilldate();

            DMPUtil dmpUtil = null;

            int pageNo = 0;
            int adamt = 0;
            int slno = 0;
            double amt1 = 0;
            int tot = 0;
            double amt2 = 0;
            int cnt = 0;
            int payBillMonth = 0;
            int payBillYear = 0;
            String namelength[] = null;
            String refdesc = "";
            //String plastinstno = "";
            String drawingmonth = "";
            String drawingmonth1 = "";
            List<LoanAdvanceScheduleBean> loanlist = comonScheduleDao.getLoanAdvanceSchedulePrincipalList(schedule, billno, month, year);

            int j = 1;
            for (LoanAdvanceScheduleBean loan : loanlist) {
                if (createFile == false) {
                    dmpUtil = new DMPUtil(folderPath, fileName);
                    createFile = true;
                }
                if (loan.getEmpNameDesg() != null && !loan.getEmpNameDesg().equals("")) {
                    dataFound = true;
                    
                    adamt = new Double(loan.getDecutedAmt()).intValue();

                    amt1 = loan.getOriginalAmt();
                    amt2 = loan.getRecAmt();

                    if (loan.getInstalmentRec() != null) {
                        refdesc = loan.getInstalmentRec();
                    }

                    drawingmonth = "";
                    drawingmonth1 = "";
                    if (loan.getDeductionStdate() != null) {
                        drawingmonth = loan.getDeductionStdate();
                        drawingmonth1 = loan.getDrawingmonth();
                    } else if (loan.getVchDate() != null && !loan.getVchDate().equals("")) {
                        drawingmonth1 = loan.getDrawingmonth();
                    }
                    namelength = wrapText(loan.getEmpNameDesg(), 14);
                    double bal = amt1 - amt2;
                    if (bal < 0) {
                        bal = 0;
                    }
                    if (slno == 0 || slno % 8 == 0) {
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
                            printPageFooter(dmpUtil, crb, tot);
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

                        printHeader(dmpUtil, crb, billdesc, schedule, loanName, pageNo);
                        if (pageNo > 1) {
                            printCarryForward(dmpUtil, tot, pageNo);
                        }
                    }
                    tot = tot + new Double(adamt).intValue();
                    slno++;

                    for (int len = 0; len < 2; len++) {
                        String nameString = "";
                        if (len == 0) {
                            dmpUtil.writeToFile("");
                            dmpUtil.writeToFile(StringUtils.rightPad(" " + slno, 5) + StringUtils.rightPad(namelength[0], 15) + StringUtils.rightPad(drawingmonth1, 11) + StringUtils.leftPad("" + amt1, 8) + StringUtils.leftPad(refdesc, 8) + StringUtils.leftPad("" + adamt, 6) + StringUtils.leftPad("" + amt2, 10) + StringUtils.leftPad("" + bal, 10) + " " + loan.getGpfNo());
                            if (namelength.length > 1) {
                                int k = 0;
                                for (j = 1; j < namelength.length; j++) {
                                    k++;
                                    if (j == 1) {
                                        nameString = namelength[k];
                                    } else {
                                        nameString = nameString + " " + namelength[k];
                                    }
                                }
                            }
                            dmpUtil.writeToFile(StringUtils.rightPad(" ", 5) + StringUtils.rightPad(StringUtils.defaultString(nameString), 60));
                            nameString = "";
                        } else if (len == 1) {
                            //dmpUtil.writeToFile(StringUtils.rightPad(" ", 5) + StringUtils.rightPad(loan.getEmpdesg(), 60));

                        }

                        cnt++;

                    }

                }
            }
            if (createFile == true) {
                if (cnt > 0) {
                    
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("------------------------------------------------------------------------------------------");
                    dmpUtil.writeToFile(StringUtils.rightPad("* Grand Total * :", 16) + StringUtils.leftPad("" + tot, 44));
                    dmpUtil.writeToFile("------------------------------------------------------------------------------------------");
                    dmpUtil.writeToFile(StringUtils.leftPad("In Words(RUPEES " + StringUtils.upperCase(Numtowordconvertion.convertNumber(tot) + ") ONLY"), characterPerLine));
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("");
                    if (crb.getAqmonth() <= 10) {
                        payBillMonth = crb.getAqmonth() + 1;
                        payBillYear = year;
                    } else {
                        payBillMonth = 0;
                        payBillYear = year + 1;
                    }
                    dmpUtil.writeToFile(StringUtils.rightPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + " payable on " + CommonFunctions.getMonthAsString(payBillMonth) + " " + payBillYear, characterPerLine, " "));
                    dmpUtil.writeToFile(StringUtils.rightPad("Desig. of the DDO: " + StringUtils.defaultString(crb.getDdoname()), characterPerLine, " "));

                }
                dmpUtil.writeToFile((char) 12);
                dmpUtil.writeToFile((char) 26);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return dataFound;
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
