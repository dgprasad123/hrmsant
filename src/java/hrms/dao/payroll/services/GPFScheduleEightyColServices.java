package hrms.dao.payroll.services;

import hrms.common.CommonFunctions;
import hrms.common.DMPUtil;
import hrms.common.Numtowordconvertion;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.GPFScheduleBean;
import hrms.model.payroll.schedule.ScheduleHelper;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class GPFScheduleEightyColServices {

    int characterPerLine = 87;
    int rowPerPage = 62;
    public ScheduleDAO comonScheduleDao;

    public void setComonScheduleDao(ScheduleDAO comonScheduleDao) {
        this.comonScheduleDao = comonScheduleDao;
    }

    private void printforPageNoOne(DMPUtil dmpUtil, CommonReportParamBean crb, String billdesc, String billdate, int pageNo) throws Exception {
        String offname = crb.getOfficeen();
        String deptname = crb.getDeptname();
        String distname = crb.getDistrict();
        String statename = crb.getStatename();
        dmpUtil.writeToFile(StringUtils.leftPad("Page : " + pageNo, characterPerLine));
        dmpUtil.writeToFile(StringUtils.center(StringUtils.upperCase(offname), characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("SCHEDULE L-III NO 191", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("FORM NO. O T C - 77", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.leftPad("(SEE RULE - 666)", 50) + StringUtils.leftPad("8009-State Provident Fund", 30));
        dmpUtil.writeToFile("");

        dmpUtil.writeToFile((char) 27);
        dmpUtil.writeToFile((char) 69);
        dmpUtil.writeToFile(StringUtils.rightPad("Bill No : " + billdesc + ((char) 27) + ((char) 70), 0) + StringUtils.leftPad("-01-Civil/100/General Provident Fund", 45));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.rightPad("SCHEDULES OF GENERAL PROVIDENT FUND DEDUCTIONS OF", 0) + StringUtils.leftPad("TV NO.____________________", 31));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.rightPad("Deductions made from the salary for" + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), 0) + StringUtils.leftPad("Date :____________________", 37));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.leftPad("1.Arrange the A/C Nos in serial order. Accounts Nos may be written very clearly", 13));
        dmpUtil.writeToFile(StringUtils.leftPad(".Separate Schedules should be prepared  for each group.", 13));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.leftPad("2.The names of the subscribers should be written in full.", 13));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.leftPad("3.If interest is paid on advance, mention it in remarks column.", 13));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.leftPad("4.Figures in columns 3,4,5 and 7 should be rounded to whole rupees.", 13));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.leftPad("5.Use similar form, if names are few. But donot write subscribers name ", 13));
        dmpUtil.writeToFile(StringUtils.leftPad("and account numbers very close to each other.", 13));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.leftPad("6.The total of schedules also should be written both in figures and words.", 13));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.leftPad("7.This form should not be used for transactions of General Provident Fund for ", 13));
        dmpUtil.writeToFile(StringUtils.leftPad("which form No. O.T.C. 76 has been provided.", 13));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.leftPad("8.In Col. 1 quote account number unfailingly. The guide letters e.g. I.C.S.  ", 13));
        dmpUtil.writeToFile(StringUtils.leftPad("(ICS Provident Fund) etc. should be invariably  prefixed to Account Nos.", 13));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.leftPad("9.In the remarks column write description against every new name such as  ", 13));
        dmpUtil.writeToFile(StringUtils.leftPad(" 'New Subscriber' came on transfer from Office District resumed subscription.", 13));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.leftPad("10.Separate schedule should be prepared in respect of persons whose account ", 13));
        dmpUtil.writeToFile(StringUtils.leftPad("  are kept by different Accountant General.", 13));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.leftPad(" Office of the " + StringUtils.upperCase(offname), 13));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.center("Name of the Accounts officer who maintains these accounts ", 13));
        dmpUtil.writeToFile(StringUtils.leftPad(" (See Instruction): A.G(A&E) Orissa, Bhubaneswar ", 13));
        dmpUtil.writeToFile("");

    }

    private void printHeader(DMPUtil dmpUtil, CommonReportParamBean crb, String billdesc, String billdate, String pfcode, int pageNo) throws Exception {
        String offname = crb.getOfficeen();
        String deptname = crb.getDeptname();
        String distname = crb.getDistrict();
        String statename = crb.getStatename();
        dmpUtil.writeToFile(StringUtils.leftPad("Page : " + pageNo, characterPerLine));
        dmpUtil.writeToFile(StringUtils.center(StringUtils.upperCase(offname), characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("SCHEDULE L-III NO 191", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("FORM NO. O T C - 77", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.leftPad("(SEE RULE - 666)", 50) + StringUtils.leftPad("8009-State Provident Fund", 30));
        dmpUtil.writeToFile("");

        dmpUtil.writeToFile((char) 27);
        dmpUtil.writeToFile((char) 69);
        dmpUtil.writeToFile(StringUtils.rightPad("Bill No : " + billdesc + ((char) 27) + ((char) 70), 0) + StringUtils.leftPad("-01-Civil/100/General Provident Fund", 45));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.rightPad("SCHEDULES OF GENERAL PROVIDENT FUND DEDUCTIONS OF", 0) + StringUtils.leftPad("TV NO.____________________", 31));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.rightPad("Deductions made from the salary for" + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), 0) + StringUtils.leftPad("Date :____________________", 37));
        dmpUtil.writeToFile("");

        dmpUtil.writeToFile(" STATE - " + StringUtils.rightPad(StringUtils.defaultString(statename), 32) + "          DISTRICT- " + StringUtils.rightPad(StringUtils.defaultString(distname), 30));
        dmpUtil.writeToFile("PF CODE- " + pfcode);
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
        dmpUtil.writeToFile("SL GPF NO.    NAME/                  BASIC/ MONTHLY REFUNDS  INST.  TOTAL    REMARKS");
        dmpUtil.writeToFile("NO D.O.E     DESIGNATION             GP/     SUB.   OF WITH- NUMBER REALISED (DOB/");
        dmpUtil.writeToFile("                                     PAY          DRAWALS                  SUPERANN");
        dmpUtil.writeToFile("                                     SCALE                                         ");
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
        dmpUtil.writeToFile("     (1)      (2)                    (3)      (4)    (5)      (6)     (7)       (8) ");
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
    }

    private void printReportHeader(DMPUtil dmpUtil, CommonReportParamBean crb, String billdesc, String billdate, int pageNo) throws Exception {
        String offname = crb.getOfficeen();
        dmpUtil.writeToFile(StringUtils.leftPad("Page : " + pageNo, characterPerLine));
        dmpUtil.writeToFile(StringUtils.center(StringUtils.upperCase(offname), characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("SCHEDULE L-III NO 191", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("FORM NO. O T C - 77", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.leftPad("(SEE RULE - 666)", 50) + StringUtils.leftPad("8009-State Provident Fund", 30));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile((char) 27);
        dmpUtil.writeToFile((char) 69);
        dmpUtil.writeToFile(StringUtils.rightPad("Bill No : " + billdesc + ((char) 27) + ((char) 70), 0) + StringUtils.leftPad("-01-Civil/100/General Provident Fund", 45));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.rightPad("SCHEDULES OF GENERAL PROVIDENT FUND DEDUCTIONS OF", 0) + StringUtils.leftPad("TV NO.____________________", 31));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.rightPad("Deductions made from the salary for" + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), 0) + StringUtils.leftPad("Date :____________________", 37));
    }

    public void printCarryForward(DMPUtil dmpUtil, int pagetotal, int pageNo, boolean isLastPage) throws Exception {
        if (isLastPage == true) {
            dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
            dmpUtil.writeToFile(" TOTAL :                                          " + StringUtils.leftPad(pagetotal + "", 10));
        } else {
            dmpUtil.writeToFile(" CARRIED FROM PREVIOUS PAGE :                     " + StringUtils.leftPad(pagetotal + "", 10));
        }
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
    }

    public void printPageTotal(DMPUtil dmpUtil, double pagetotal, int pageNo, boolean isLastPage) {
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
        dmpUtil.writeToFile("Page " + pageNo + " Total " + StringUtils.leftPad(pagetotal + "", 66));
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");

    }

    public void printCategoryTotal(DMPUtil dmpUtil, double pagetotal, int pageNo, boolean isLastPage) {
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
        dmpUtil.writeToFile(" Total " + StringUtils.leftPad(pagetotal + "", 66));
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");

    }

    public void write(String billno, String folderPath, String fileSeparator, CommonReportParamBean crb) throws Exception {
        int year = 0;
        int month = 0;
        String billdesc = "";
        String billdate = "";
        String fileName = "GPFSCHEDULE_80COL.txt";
        int noofdays = 0;;
        int recordCount = 0;
        try {
            year = crb.getAqyear();
            month = crb.getAqmonth();
            billdesc = crb.getBilldesc();
            folderPath = folderPath + fileSeparator;
            billdate = crb.getBilldate();

            DMPUtil dmpUtil = new DMPUtil(folderPath, fileName);

            //PrintWriter out = response.getWriter();
            int pageNo = 1;
            boolean isHeaderPrinted = false;
            boolean isPageTotalPrinted = false;

            int rowCount = 0;

            List gpflist = comonScheduleDao.getGPFScheduleTypeList(billno, month, year);

            List abstractlist = comonScheduleDao.getGPFScheduleAbstractList(billno, month, year);

            String pFrom = "01/" + StringUtils.leftPad((crb.getAqmonth() + 1) + "", 2, "0") + "/" + crb.getAqyear();
            if (crb.getAqyear() > 0) {
                noofdays = CommonFunctions.getDaysInMonth(1, crb.getAqmonth(), crb.getAqyear());
            }
            String pTo = noofdays + "/" + StringUtils.leftPad((crb.getAqmonth() + 1) + "", 2, "0") + "/" + crb.getAqyear();

            String acctNo = "";
            String gpfType = "";
            double pageTotal = 0.0;
            dmpUtil.writeToFile((char) 27);
            dmpUtil.writeToFile((char) 64);
            dmpUtil.writeToFile((char) 15);
            //dmpUtil.writeToFile((char)12);
            for (int i = 0; i < gpflist.size(); i++) {
                ScheduleHelper gpfTypeBean = (ScheduleHelper) gpflist.get(i);

                ArrayList empGpfList = gpfTypeBean.getHelperList();
                pageTotal = 0.0;
                if (empGpfList.size() > 0) {

                    if (pageNo == 1) {
                        printforPageNoOne(dmpUtil, crb, billdesc, billdate, pageNo);
                        printHeader(dmpUtil, crb, billdesc, billdate, gpfTypeBean.getGpfType(), pageNo);
                    }
                    if (pageNo != 1) {
                        printHeader(dmpUtil, crb, billdesc, billdate, gpfTypeBean.getGpfType(), pageNo);
                    }

                    //printHeader(dmpUtil, crb, billdesc, billdate, pageNo);
                }
                for (int j = 0; j < empGpfList.size(); j++) {

                    if (recordCount != 0 && recordCount % 25 == 0) {
                        printPageTotal(dmpUtil, pageTotal, pageNo - 1, false);
                        pageNo++;
                        dmpUtil.writeToFile((char) 12);
                        dmpUtil.writeToFile((char) 27);
                        dmpUtil.writeToFile((char) 64);
                        dmpUtil.writeToFile((char) 15);
                        printHeader(dmpUtil, crb, billdesc, billdate, gpfTypeBean.getGpfType(), pageNo);
                    }
                    recordCount++;
                    GPFScheduleBean empSchBean = (GPFScheduleBean) empGpfList.get(j);

                    if (empSchBean.getAccountNo() != null && !empSchBean.getAccountNo().equals("")) {
                        acctNo = empSchBean.getAccountNo();
                        acctNo = acctNo.substring(gpfTypeBean.getGpfType().length());
                    } else {
                        acctNo = "N/A";
                    }
                    String name[] = wrapText(empSchBean.getEmpName(), 23);
                    String designation[] = wrapText(empSchBean.getDesignation(), 23);
                    String payscale[] = wrapText(empSchBean.getScaleOfPay(), 15);
                    dmpUtil.writeToFile(StringUtils.rightPad(StringUtils.defaultString(recordCount + ""), 3)
                            + StringUtils.rightPad(StringUtils.defaultString(acctNo), 12)
                            + StringUtils.rightPad(StringUtils.defaultString(name[0]), 23) + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getBasicPay()), 10) + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getMonthlySub() + ""), 7)
                            + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getTowardsLoan() + ""), 7) + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getNoOfInstalment() + ""), 6) + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getTotalReleased() + ""), 8)
                            + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getDob() + ""), 8));
                    pageTotal = pageTotal + empSchBean.getTotalReleased();

                    if (name.length > 1 && designation.length > 1) {
                        String desig = "";
                        if (designation.length > 2) {
                            desig = designation[1] + " " + designation[2];
                        } else {
                            desig = designation[1];
                        }

                        dmpUtil.writeToFile(StringUtils.rightPad("", 3) + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getDateOfEntry()), 12) + StringUtils.rightPad(StringUtils.defaultString(name[1]), 23)
                                + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getGradePay()), 10) + StringUtils.leftPad("", 5) + StringUtils.leftPad(StringUtils.defaultString(empSchBean.getDor()), 25));
                        dmpUtil.writeToFile(StringUtils.rightPad("", 15) + StringUtils.rightPad(designation[0], 23) + empSchBean.getScaleOfPay());
                        dmpUtil.writeToFile(StringUtils.rightPad("", 15) + StringUtils.rightPad(designation[1], 23));
                        dmpUtil.writeToFile("");
                    } else if (name.length > 1) {
                        dmpUtil.writeToFile(StringUtils.rightPad("", 3) + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getDateOfEntry()), 12) + StringUtils.rightPad(StringUtils.defaultString(name[1]), 23)
                                + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getGradePay()), 10) + StringUtils.leftPad("", 5) + StringUtils.leftPad(StringUtils.defaultString(empSchBean.getDor()), 25));
                        dmpUtil.writeToFile(StringUtils.rightPad("", 15) + StringUtils.rightPad("", 23) + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getScaleOfPay()), 38));
                        dmpUtil.writeToFile("");
                    } else if (designation.length > 1) {
                        dmpUtil.writeToFile(StringUtils.rightPad(StringUtils.defaultString(""), 3) + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getDateOfEntry()), 12) + StringUtils.rightPad(StringUtils.defaultString(designation[0]), 23)
                                + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getGradePay()), 10) + StringUtils.leftPad("", 5) + StringUtils.leftPad(StringUtils.defaultString(empSchBean.getDor()), 33));
                        dmpUtil.writeToFile(StringUtils.rightPad("", 15) + StringUtils.rightPad(StringUtils.defaultString(designation[1]), 23) + empSchBean.getScaleOfPay());
                        dmpUtil.writeToFile("");
                    } else if (name.length == 1 && designation.length == 1) {
                        dmpUtil.writeToFile(StringUtils.rightPad("", 3) + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getDateOfEntry()), 12) + StringUtils.rightPad(StringUtils.defaultString(designation[0]), 23)
                                + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getGradePay()), 10) + StringUtils.leftPad("", 5) + StringUtils.leftPad(empSchBean.getDor(), 33));
                        dmpUtil.writeToFile(StringUtils.rightPad("", 15) + StringUtils.rightPad("", 23) + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getScaleOfPay()), 38));
                        dmpUtil.writeToFile("");
                    } else {
                        dmpUtil.writeToFile("");
                        dmpUtil.writeToFile("");
                    }

                }
                if (empGpfList.size() > 0) {
                    printCategoryTotal(dmpUtil, pageTotal, pageNo, false);
                    pageNo++;
                }
                /*Page Break in each GPF Series*/
                dmpUtil.writeToFile((char) 12);
                dmpUtil.writeToFile((char) 27);
                dmpUtil.writeToFile((char) 64);
                dmpUtil.writeToFile((char) 15);
                /*Page Break in each GPF Series*/
            }

            printReportHeader(dmpUtil, crb, billdesc, billdate, pageNo);
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.rightPad("", 38) + "GPF ABSTRACT");
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.rightPad("", 30) + "PF CODE            TOTAL AMOUNT");
            dmpUtil.writeToFile(StringUtils.rightPad("", 30) + "-------------------------------");
            int totalGPFDeduction = 0;
            for (int j = 0; j < abstractlist.size(); j++) {
                GPFScheduleBean gpfTypeBean = (GPFScheduleBean) abstractlist.get(j);
                totalGPFDeduction = totalGPFDeduction + Integer.parseInt(gpfTypeBean.getTotalAmount());
                dmpUtil.writeToFile(StringUtils.rightPad("", 31) + StringUtils.rightPad(gpfTypeBean.getGpfType(), 11) + StringUtils.leftPad(gpfTypeBean.getTotalAmount(), 18));
            }
            dmpUtil.writeToFile(StringUtils.rightPad("", 30) + "-------------------------------");
            dmpUtil.writeToFile(StringUtils.rightPad("", 30) + " GRAND TOTAL" + StringUtils.leftPad(totalGPFDeduction + "", 18));
            dmpUtil.writeToFile(StringUtils.rightPad("", 30) + StringUtils.upperCase("(" + Numtowordconvertion.convertNumber(totalGPFDeduction) + ") Only"));
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.rightPad("Certified that all the particulars of recoveries have been corrected/verified as per the  instructions", 4));
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.rightPad("issued in respect of preparation of GPF Schedule as per FD Memo No. TRB-64/2000/ 27044/F Dt. 23.6.2000", 4));
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.leftPad(StringUtils.defaultString(crb.getDdoname()), 82));

            dmpUtil.writeToFile((char) 12);
            dmpUtil.writeToFile((char) 26);
        } catch (Exception sqe) {
            sqe.printStackTrace();
        } finally {

        }
    }

    private static String[] wrapText(String text, int len) {

        // return empty array for null text
        if (text == null) {
            return new String[]{" "};
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
