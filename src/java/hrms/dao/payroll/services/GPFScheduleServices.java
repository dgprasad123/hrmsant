/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author Surendra
 */
@Service
public class GPFScheduleServices {

    int characterPerLine = 215;
    int rowPerPage = 62;
    public ScheduleDAO comonScheduleDao;

    public void setComonScheduleDao(ScheduleDAO comonScheduleDao) {
        this.comonScheduleDao = comonScheduleDao;
    }
    
    

    private void printHeader(DMPUtil dmpUtil, CommonReportParamBean crb, String billdesc, String billdate, int pageNo) throws Exception {
        String offname = crb.getOfficeen();
        String deptname = crb.getDeptname();
        String distname = crb.getDistrict();
        String statename = crb.getStatename();
        dmpUtil.writeToFile(StringUtils.center("SCHEDULE-A STATE HEAD QUARTERS   FORM NO-58        BILL NO : " + billdesc + " BILL DT : " + billdate, characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("PAY BILL FOR " + StringUtils.upperCase(offname), characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("MONTHLY PAY BILL FOR " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        dmpUtil.writeToFile(" STATE - " + StringUtils.rightPad(StringUtils.defaultString(statename), 32) + "          DISTRICT- " + StringUtils.rightPad(StringUtils.defaultString(distname), 30) + StringUtils.rightPad("", characterPerLine - 99) + "PAGE : " + pageNo);
        dmpUtil.writeToFile("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        dmpUtil.writeToFile(" SL  GPF      GPF        NAME                            DESIGNATION                BASIC     PAY-                 DATE         DATE        MONTHLY   PERIOD       PERIOD       REFUND OF   INSTALLMENT  OTHER      TOTAL      REMARKS");
        dmpUtil.writeToFile("     SERIES   ACCOUNT                                                               GP        SCALE                OF           OF          SUB.      FROM         TO           WITHDRAWL   NUMBER       DEPOSITS   REALISED          ");
        dmpUtil.writeToFile("              NUMBER                                                                                               BIRTH        SUPERANN.                                                                                             ");
        dmpUtil.writeToFile("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        dmpUtil.writeToFile("(1)  (2)      (3)        (4)                                (5)                      (6)       (7)                  (8)          (9)         (10)     (11)          (12)        (13)         (14)          (15)       (16)       (17)");
        dmpUtil.writeToFile("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        //aqlist = gpfFunc.getGpfType(gpfScheduleBean.getBillNo(),con);
    }

    private void printReportHeader(DMPUtil dmpUtil, CommonReportParamBean crb, String billdesc, String billdate, int pageNo) throws Exception {
        String offname = crb.getOfficeen();
        String deptname = crb.getDeptname();
        String distname = crb.getDistrict();
        String statename = crb.getStatename();
        dmpUtil.writeToFile(StringUtils.center("SCHEDULE-A STATE HEAD QUARTERS   FORM NO-58        BILL NO : " + billdesc + " BILL DT : " + billdate, characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("PAY BILL FOR " + StringUtils.upperCase(offname), characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("MONTHLY PAY BILL FOR " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        dmpUtil.writeToFile(" STATE - " + StringUtils.rightPad(StringUtils.defaultString(statename), 32) + "          DISTRICT- " + StringUtils.rightPad(StringUtils.defaultString(distname), 30) + StringUtils.rightPad("", characterPerLine - 99) + "PAGE : " + pageNo);
    }

    public void printCarryForward(DMPUtil dmpUtil, int pagetotal, int pageNo, boolean isLastPage) throws Exception {
        if (isLastPage == true) {
            dmpUtil.writeToFile("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            dmpUtil.writeToFile(" TOTAL :                                          " + StringUtils.leftPad(pagetotal + "", 10));
        } else {
            dmpUtil.writeToFile(" CARRIED FROM PREVIOUS PAGE :                     " + StringUtils.leftPad(pagetotal + "", 10));
        }
        dmpUtil.writeToFile("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    public void printPageTotal(DMPUtil dmpUtil, double pagetotal, int pageNo, boolean isLastPage) {
        dmpUtil.writeToFile("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        dmpUtil.writeToFile("Page " + pageNo + " Total " + StringUtils.leftPad(pagetotal + "", 209));
        dmpUtil.writeToFile("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

    }

    public void printCategoryTotal(DMPUtil dmpUtil, double pagetotal, int pageNo, boolean isLastPage) {
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        dmpUtil.writeToFile(" Total " + StringUtils.leftPad(pagetotal + "", 214));
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

    }

    public void write(String billno, String folderPath, String fileSeparator, CommonReportParamBean crb) throws Exception {
        int year = 0;
        int month = 0;
        String billdesc = "";
        String billdate = "";
        String fileName = "GPFSCHEDULE.txt";
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
            
            
            
            List gpflist = comonScheduleDao.getGPFScheduleTypeList(billno,month,year) ;
            
            List abstractlist = comonScheduleDao.getGPFScheduleAbstractList(billno,month,year);
            
            String pFrom = "01/" + StringUtils.leftPad((crb.getAqmonth() + 1) + "", 2, "0") + "/" + crb.getAqyear();
            if (crb.getAqyear() >0) {
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
                    printHeader(dmpUtil, crb, billdesc, billdate, pageNo);
                }
                for (int j = 0; j < empGpfList.size(); j++) {

                    if (recordCount != 0 && recordCount % 25 == 0) {
                        printPageTotal(dmpUtil, pageTotal, pageNo - 1, false);
                        pageNo++;
                        dmpUtil.writeToFile((char) 12);
                        dmpUtil.writeToFile((char) 27);
                        dmpUtil.writeToFile((char) 64);
                        dmpUtil.writeToFile((char) 15);
                        printHeader(dmpUtil, crb, billdesc, billdate, pageNo);
                    }
                    recordCount++;
                    GPFScheduleBean empSchBean = (GPFScheduleBean) empGpfList.get(j);

                    if (empSchBean.getAccountNo()  != null && !empSchBean.getAccountNo().equals("")) {
                        acctNo = empSchBean.getAccountNo();
                        acctNo = acctNo.substring(gpfTypeBean.getGpfType().length());
                    } else {
                        acctNo = "N/A";
                    }
                    String name[] = wrapText(empSchBean.getEmpName(), 32);
                    String designation[] = wrapText(empSchBean.getDesignation(), 27);
                    String payscale[] = wrapText(empSchBean.getScaleOfPay(), 15);
                    dmpUtil.writeToFile(StringUtils.leftPad(StringUtils.defaultString(recordCount + "") + "", 2) + "  "
                            + StringUtils.rightPad(StringUtils.defaultString(gpfTypeBean.getGpfType()), 8) + " "
                            + StringUtils.rightPad(StringUtils.defaultString(acctNo), 9) + " "
                            + StringUtils.rightPad(StringUtils.defaultString(name[0]), 32) + " "
                            + StringUtils.rightPad(StringUtils.defaultString(designation[0]), 27) + " "
                            + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getBasicPay()), 10) + " "
                            + StringUtils.rightPad(StringUtils.defaultString(payscale[0]), 20) + " "
                            + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getDob()), 11) + " "
                            + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getDor()), 11) + " "
                            + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getMonthlySub() + "") + "", 8) + " "
                            + StringUtils.rightPad(pFrom, 11) + " "
                            + StringUtils.rightPad(StringUtils.defaultString(pTo), 17)
                            + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getTowardsLoan() + "") + "", 11) + " "
                            + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getNoOfInstalment()), 12) + " "
                            + StringUtils.rightPad(StringUtils.defaultString(empSchBean.getOtherDeposits() + "") + "", 10) + " " + StringUtils.rightPad(empSchBean.getTotalReleased() + "", 7));
                    pageTotal = pageTotal + empSchBean.getTotalReleased();

                    if (name.length > 1 && designation.length > 1) {
                        String desig = "";
                        if (designation.length > 2) {
                            desig = designation[1] + " " + designation[2];
                        } else {
                            desig = designation[1];
                        }
                        if (payscale.length > 1) {
                            dmpUtil.writeToFile(StringUtils.rightPad("", 23) + StringUtils.rightPad(name[1], 32) + StringUtils.rightPad(desig, 28) + StringUtils.rightPad(empSchBean.getGradePay(), 11) + StringUtils.rightPad(StringUtils.defaultString(payscale[1]), 11));
                        } else {
                            dmpUtil.writeToFile(StringUtils.rightPad("", 23) + StringUtils.rightPad(name[1], 32) + StringUtils.rightPad(desig, 28) + StringUtils.rightPad(empSchBean.getGradePay(), 61));
                        }
                        //dmpUtil.writeToFile("");
                    } else if (name.length > 1) {
                        if (payscale.length > 1) {
                            dmpUtil.writeToFile(StringUtils.rightPad("", 23) + StringUtils.rightPad(name[1], 54) + StringUtils.rightPad(empSchBean.getGradePay(), 11) + StringUtils.rightPad(StringUtils.defaultString(payscale[1]), 11));
                        } else {
                            dmpUtil.writeToFile(StringUtils.rightPad("", 23) + StringUtils.rightPad(name[1], 54) + StringUtils.rightPad(empSchBean.getGradePay(), 61));
                        }
                        //dmpUtil.writeToFile("");
                    } else if (designation.length > 1) {
                        if (payscale.length > 1) {
                            dmpUtil.writeToFile(StringUtils.rightPad("", 56) + StringUtils.rightPad(designation[1], 28) + StringUtils.rightPad(empSchBean.getGradePay(), 11) + StringUtils.rightPad(StringUtils.defaultString(payscale[1]), 11));
                        } else {
                            dmpUtil.writeToFile(StringUtils.rightPad("", 56) + StringUtils.rightPad(designation[1], 28) + StringUtils.rightPad(empSchBean.getGradePay(), 61));
                        }
                        //dmpUtil.writeToFile("");
                    } else {
                        if (payscale.length > 1) {
                            dmpUtil.writeToFile(StringUtils.leftPad(StringUtils.defaultString(empSchBean.getGradePay()), 88) + "       " + (StringUtils.defaultString(payscale[1])));
                        } else {
                            dmpUtil.writeToFile(StringUtils.leftPad(StringUtils.defaultString(empSchBean.getGradePay()), 88));
                        }
                        //dmpUtil.writeToFile("");
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
