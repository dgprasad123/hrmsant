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
import org.springframework.stereotype.Service;

@Service
public class PIAdvIntRecServices {

    
    private String schedule;
    private String fileName;
    
    public static final int characterPerLine = 87;
    public static final int lineperpage = 62;

    public ScheduleDAO comonScheduleDao;
    public AqReportDAO aqReportDao;
    public PayBillDMPDAO paybillDmpDao;

    public void setComonScheduleDao(ScheduleDAO comonScheduleDao) {
        this.comonScheduleDao = comonScheduleDao;
    }

    public void setAqReportDao(AqReportDAO aqReportDao) {
        this.aqReportDao = aqReportDao;
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
        if (schedule.endsWith("MOPA")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname + " (INTEREST)", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile(StringUtils.rightPad("0049-INTEREST RECEIPTS OF STATE/UNION-TERRITORY GOVERNMENTS ", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.rightPad("800-OTHER RECEIPTS-0060-Interest Receipts-10019-Interest on Loans and advances", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.rightPad("to Govt. Servants-003-Advances for Purchase of Motor Conveyances", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + StringUtils.defaultString(billdesc), characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        } else if (schedule.equalsIgnoreCase("MCA")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname + " (INTEREST)", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile(StringUtils.rightPad("0049-INTEREST RECEIPTS OF STATE/UNION-TERRITORY GOVERNMENTS ", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.rightPad("800-OTHER RECEIPTS-0060-Interest Receipts-10019-Interest on Loans and advances", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.rightPad("to Govt. Servants-003-Advances for Purchase of Motor Conveyances", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + StringUtils.defaultString(billdesc), characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        } else if (schedule.equalsIgnoreCase("FA")) {
            dmpUtil.writeToFile(StringUtils.rightPad("Schedule of recovery of :" + loanname + "", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        } else if (schedule.equalsIgnoreCase("GISA")) {
            dmpUtil.writeToFile(StringUtils.rightPad("Schedule of recovery of : " + loanname + "", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        } else if (schedule.equalsIgnoreCase("GA")) {
            dmpUtil.writeToFile(StringUtils.rightPad("Schedule of recovery of : " + loanname + "", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        } else if (schedule.equalsIgnoreCase("HBA")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname + " (INTEREST)", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile(StringUtils.rightPad("0049-INTEREST RECEIPTS OF STATE/UNION-TERRITORY GOVERNMENTS-800-OTHER", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.rightPad("RECEIPTS-0060-Interest Receipts-10019-Interest on Loans and advances to Govt.", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.rightPad("Servants-076-House Building Advance", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + StringUtils.defaultString(billdesc), characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        } else if (schedule.equalsIgnoreCase("MAL")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + billdesc, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        } else if (schedule.equalsIgnoreCase("SHBA")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname + " (INTEREST)", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile(StringUtils.rightPad("0049-INTEREST RECEIPTS OF STATE/UNION-TERRITORY GOVERNMENTS-800-OTHER", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.rightPad("RECEIPTS-0060-Interest Receipts-10019-Interest on Loans and advances to Govt.", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.rightPad("Servants-230-Special House Building Advance", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + billdesc, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        } else if (schedule.equalsIgnoreCase("BICA")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + billdesc, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        } else if (schedule.equalsIgnoreCase("VE")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + billdesc, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        } else if (schedule.equalsIgnoreCase("OR")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + billdesc, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        } else if (schedule.equalsIgnoreCase("PA")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF " + loanname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + StringUtils.defaultString(billdesc), characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        }
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.rightPad("Advance for the Month : " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------------");
        dmpUtil.writeToFile(" Sl   Name/                Month      Amount   No.of   Amount   Total    Balance  Remarks");
        dmpUtil.writeToFile(" No.  Designation          in which   of       Instal- deduct   recovery out-");
        dmpUtil.writeToFile("                           Original   Original ments   from     to the   standing");
        dmpUtil.writeToFile("                           Advance    Advance          bill     end");
        dmpUtil.writeToFile("                           was Drawn                   against  of the");
        dmpUtil.writeToFile("                                                       interest month");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------------");
        dmpUtil.writeToFile("          1                   2          3       4        5        6         7        8");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------------");
    }

    private void printPageFooter(DMPUtil dmpUtil, CommonReportParamBean crb, int pageTotal) {
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------------");
        dmpUtil.writeToFile(StringUtils.rightPad("* Page Total * :", 16) + StringUtils.leftPad("" + pageTotal, 43));
        dmpUtil.writeToFile("------------------------------------------------------------------------------------------");
        dmpUtil.writeToFile(StringUtils.rightPad("In Words(RUPEES " + StringUtils.upperCase(Numtowordconvertion.convertNumber(pageTotal) + ") ONLY"), characterPerLine, " "));
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

    public boolean write(String billNo, String schedule, String loanname, String folderPath, String fileSeparator,CommonReportParamBean crb) throws Exception {
        String schdesc = "";
        String demandno = "";
        int year = 0;
        int month = 0;
        String billdesc = "";
        String billdate = "";
        int payBillMonth = 0;
        int payBillYear = 0;

        boolean dataFound = false;
        boolean createFile = false;
        Schedule sch = new Schedule();
        try {
            fileName=schedule+"-INT-SCHEDULE.txt";
            sch = aqReportDao.getScheduleList(schedule);
            schdesc = sch.getScheduleDesc();
            demandno = sch.getDemandNo();

            //crb = paybillDmpDao.getCommonReportParameter(billNo);
            year = crb.getAqyear();
            month = crb.getAqmonth();
            billdesc = crb.getBilldesc();
            folderPath = folderPath+fileSeparator;
            billdate = crb.getBilldate();

            DMPUtil dmpUtil = null;
            int pageNo = 0;
            double adamt = 0;
            int slno = 0;
            double amt1 = 0;
            int tot = 0;
            double amt2 = 0;
            int cnt = 0;
            String refdesc = "";
            String plastinstno = "";
            String sqlQry = "";
            String drawingmonth = "";
            String drawingmonth1 = "";
            String namelength[] = null;
            
            List<LoanAdvanceScheduleBean> loanAdvanceScheduleList = comonScheduleDao.getLoanAdvanceScheduleInterestList(schedule,billNo,month,year);
            int j = 1;
            for (LoanAdvanceScheduleBean loanAdvSch : loanAdvanceScheduleList) {
                if (createFile == false) {
                    dmpUtil = new DMPUtil(folderPath, fileName);
                    createFile = true;
                }
                if (loanAdvSch.getEmpNameDesg()!= null && !loanAdvSch.getEmpNameDesg().equals("")) {
                    dataFound = true;
                    adamt = loanAdvSch.getDecutedAmt();
                         
                    amt1 = loanAdvSch.getOriginalAmt();
                    
                    loanAdvSch.getOriginalAmt();
                    amt2 = loanAdvSch.getRecAmt();
                    
                    if (loanAdvSch.getInstalmentRec() != null) {
                        refdesc = loanAdvSch.getInstalmentRec();
                    }
                    if (loanAdvSch.getInstalmentNo() != null) {
                        plastinstno = loanAdvSch.getInstalmentNo();
                    }
                    drawingmonth = "";
                    drawingmonth1 = "";
                    if (loanAdvSch.getDeductionStdate() != null && !loanAdvSch.getDeductionStdate().equals("")) {
                        drawingmonth = loanAdvSch.getDeductionStdate();
                        drawingmonth1 = drawingmonth.substring(3, drawingmonth.length());
                    } else if (loanAdvSch.getVchDate() != null && !loanAdvSch.getVchDate().equals("")) {
                        drawingmonth1 = loanAdvSch.getVchDate();
                    }

                    namelength = wrapText(loanAdvSch.getEmpNameDesg(), 21);

                    double bal = amt1 - amt2;
                    if (bal < 0) {
                        bal = 0;
                    }
                    if (slno == 0 || slno % 6 == 0) {
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
                        printHeader(dmpUtil, crb, billdesc, schedule, loanname, pageNo);
                        if (pageNo > 1) {
                            printCarryForward(dmpUtil, tot, pageNo);
                        }
                    }
                    Double d=adamt;
                    tot = tot + d.intValue();
                    slno++;

                    for (int len = 0; len < 2; len++) {
                        String nameString = "";
                        if (len == 0) {
                            dmpUtil.writeToFile("");
                            dmpUtil.writeToFile(StringUtils.rightPad(" " + slno, 5) + StringUtils.rightPad(namelength[0], 22) + StringUtils.leftPad(drawingmonth1, 8) + StringUtils.leftPad("" + amt1, 8) + StringUtils.leftPad(refdesc, 8) + StringUtils.leftPad("" + adamt, 8) + StringUtils.leftPad("" + amt2, 10) + StringUtils.leftPad("" + bal, 10) + " " + StringUtils.defaultString(loanAdvSch.getGpfNo()));
                            if (namelength.length > 1) {
                                nameString = namelength[1];
                            }
                            dmpUtil.writeToFile(StringUtils.rightPad(" ", 5) + StringUtils.rightPad(StringUtils.defaultString(nameString), 50));
                            nameString = "";
                        } else if (len == 1) {
                            //dmpUtil.writeToFile(StringUtils.rightPad(" ", 5) + StringUtils.rightPad(loanAdvSch.getEmpdesg(), 70));

                        }

                        cnt++;

                    }

                }
            }
            if (createFile == true) {
                if (cnt > 0) {
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("------------------------------------------------------------------------------------------");
                    dmpUtil.writeToFile(StringUtils.rightPad("* Grand Total * :", 16) + StringUtils.leftPad("" + tot, 43));
                    dmpUtil.writeToFile("------------------------------------------------------------------------------------------");
                    dmpUtil.writeToFile(StringUtils.center("In Words(RUPEES " + StringUtils.upperCase(Numtowordconvertion.convertNumber(tot) + ") ONLY"), characterPerLine, " "));
                    dmpUtil.writeToFile("");
                    if (crb.getAqmonth() <= 10) {
                        payBillMonth = crb.getAqmonth() + 1;
                        payBillYear = crb.getAqyear();
                    } else {
                        payBillMonth = 0;
                        payBillYear = crb.getAqyear() + 1;
                    }
                    dmpUtil.writeToFile(StringUtils.rightPad("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + " payable on " + CommonFunctions.getMonthAsString(payBillMonth) + " " + payBillYear, characterPerLine, " "));
                    dmpUtil.writeToFile(StringUtils.rightPad("Desig. of the DDO: " + StringUtils.defaultString(crb.getDdoname()) + ", ", characterPerLine, " "));
                    dmpUtil.writeToFile(crb.getOfficename());
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
