/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.services;

import hrms.common.CommonFunctions;
import hrms.common.DMPUtil;
import hrms.common.Numtowordconvertion;
import hrms.dao.payroll.schedule.PayBillDMPDAOImpl;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.master.GQtrPool;
import hrms.model.payroll.schedule.WrrScheduleBean;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Surendra
 */
public class HRRScheduleServices {

    public ScheduleDAO comonScheduleDao;

    public PayBillDMPDAOImpl paybillDmpDao;

    public static final int characterPerLine = 87;
    public static final int lineperpage = 62;
    String secondLineAddress = "";
    private String secondStr = "";

    public void setPaybillDmpDao(PayBillDMPDAOImpl paybillDmpDao) {
        this.paybillDmpDao = paybillDmpDao;
    }

    
    
    
    public void setComonScheduleDao(ScheduleDAO comonScheduleDao) {
        this.comonScheduleDao = comonScheduleDao;
    }

    private String allignString(String qtraddr) throws Exception {
        secondStr = qtraddr.substring(12, qtraddr.length());
        return secondStr;
    }

    private void printPageFooter(DMPUtil dmpUtil, CommonReportParamBean crb, int pageTotal, int pageNo) {
        if (pageTotal > 0) {
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.repeat("-", characterPerLine));
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile("* Page Total * : " + StringUtils.leftPad(pageTotal + "", 65));
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.repeat("-", characterPerLine));
            dmpUtil.writeToFile(StringUtils.leftPad("In Words (Rupees " + StringUtils.upperCase(Numtowordconvertion.convertNumber(pageTotal) + " ) Only"), 82));
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile("");
        }
    }

    private void lastHeader(DMPUtil dmpUtil, CommonReportParamBean crb, String billdesc, String schedule, String billdate, int pageNo, String poolname) throws Exception {
        String deptname = crb.getOfficeen();
        dmpUtil.writeToFile(StringUtils.center("         Page : " + pageNo, characterPerLine, " "));

        if (schedule.endsWith("HRR")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF DEDUCTION OF HOUSE RENT", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));
        }
        if (schedule.endsWith("WRR")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF DEDUCTION OF WATER SUPPLY AND SANITATION CHARGE RENT", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));
        }
        if (schedule.endsWith("SWR")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF DEDUCTION OF SWERAGE RENT", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));
        }
    }

    private void printHeader(DMPUtil dmpUtil, CommonReportParamBean crb, String billdesc, String schedule, String billdate, int pageNo, String poolname, String demandno) throws Exception {
        String deptname = crb.getOfficeen();
        dmpUtil.writeToFile(StringUtils.center("         Page : " + pageNo, characterPerLine, " "));

        if (schedule.endsWith("HRR")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF DEDUCTION OF HOUSE RENT", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile(StringUtils.rightPad("Major head to :  0216-HOUSING-01-GOVERNMENT RESIDENTIAL ", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.rightPad("be credited   :  BUILDINGS-106-GENERALPOOL ACCOMODATION", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.rightPad(StringUtils.leftPad(demandno, 49) + poolname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + billdesc, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center(" for the month of :   " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        }
        if (schedule.endsWith("WRR")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF DEDUCTION OF WATER SUPPLY AND SANITATION CHARGE RENT", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile(StringUtils.rightPad("0215-WATER SUPPLY AND SANITATION-01-WATER SUPPLY-103-RECEIPTS FROM", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.rightPad("URBAN WATER SUPPLY SCHEMES-0175-Water Rate / Cess-02171-Water Supply for", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.rightPad("supply of drinking water" + poolname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + billdesc, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center(" for the month of :   " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        }
        if (schedule.endsWith("SWR")) {
            dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF DEDUCTION OF SWERAGE RENT", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("OF " + deptname, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

            dmpUtil.writeToFile(StringUtils.rightPad("0215-WATER SUPPLY AND SANITATION-01-WATER SUPPLY-103-RECEIPTS FROM", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.rightPad("URBAN WATER SUPPLY SCHEMES-0175-Water Rate / Cess-02171-Water Supply for", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.rightPad("supply of drinking water", characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No " + billdesc, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center(" for the month of :   " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        }
        dmpUtil.writeToFile(StringUtils.repeat("-", characterPerLine));
        dmpUtil.writeToFile(" Sl   Name of the incumbent           Quarter Address   Arrear     Current   Amount");
        dmpUtil.writeToFile(" No.  Designation                                                            Deducted");
        dmpUtil.writeToFile(StringUtils.repeat("-", characterPerLine));
        dmpUtil.writeToFile(" 1         2                              3               4           5         6");
        dmpUtil.writeToFile(StringUtils.repeat("-", characterPerLine));

    }

    private void printCarryForward(DMPUtil dmpUtil, int pageTotal, int pageNo) {
        dmpUtil.writeToFile(StringUtils.rightPad("CARRIED FROM PAGE :" + pageNo, 22) + StringUtils.leftPad("" + pageTotal, 54));
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
    }

    public boolean write(String billno, String folderPath, String fileSeparator, String schedule, String fileName, CommonReportParamBean crb) throws Exception {

        ArrayList abstracthrrlist = new ArrayList();
        ArrayList abstractdeductlist = new ArrayList();

        int year = 0;
        int month = 0;
        String billdesc = "";
        String billdate = "";

        String namelength[] = null;
        String desiglength[] = null;
        String qtrlength[] = null;
        int cnt = 0;
        boolean dataFound = false;
        boolean createFile = false;
        try {

            year = crb.getAqyear();
            month = crb.getAqmonth();
            billdesc = crb.getBilldesc();
            folderPath = folderPath + fileSeparator;
            billdate = crb.getBilldate();

            DMPUtil dmpUtil = null;
            int cnt1 = 0;
            int pageNo = 0;
            int recamt = 0;
            int payBillMonth = 0;
            int payBillYear = 0;
            int totaldeduct = 0;
            String poolname = "";
            String demandno = "";
            String wrrbtid = null;
            String swrbtid = null;

            String btidarr = paybillDmpDao.getBtidSwrWrr(billno);
            String str[] = btidarr.split("@");

            if (str != null && str.length > 1) {
                wrrbtid = str[0];
                swrbtid = str[1];
            }

            List<GQtrPool> li = paybillDmpDao.getQtrPool();

            for (GQtrPool qtpool : li) {
                if (createFile == false) {
                    dmpUtil = new DMPUtil(folderPath, fileName);
                    createFile = true;
                }
                int slno = 0;
                totaldeduct = 0;

                String btid = null;
                if (schedule.equals("HRR")) {
                    btid = qtpool.getBtid();
                } else if (schedule.equals("WRR")) {
                    btid = wrrbtid;
                } else {
                    btid = swrbtid;
                }
                String qtr_pool_id = qtpool.getQid();
                poolname = qtpool.getPoolname();
                demandno = qtpool.getDemandAsString();
                abstracthrrlist.add(poolname);

                List<WrrScheduleBean> qtrEmpList = paybillDmpDao.getWRRScheduleEmployeeList(billno, schedule, qtr_pool_id, month, year);

                for (WrrScheduleBean qtrEmp : qtrEmpList) {
                    int temp = 0;

                    String consumerno = "";
                    if (qtrEmp.getEmpname() != null && !qtrEmp.getEmpname().equals("")) {
                        namelength = wrapText(qtrEmp.getEmpname(), 32);
                        desiglength = wrapText(qtrEmp.getEmpdesg(), 32);

                        String qtrno = "";
                        String qtraddr = "";
                        if (qtrEmp.getQuarterNo() != null) {
                            qtrno = qtrEmp.getQuarterNo().toUpperCase();
                        }
                        if (qtrEmp.getAddress() != null) {
                            qtraddr = qtrEmp.getAddress().toUpperCase();
                        }
                        if (qtrEmp.getConsumerno() != null && !qtrEmp.getConsumerno().equals("")) {
                            consumerno = "Consumer No: " + qtrEmp.getConsumerno();
                        }
                        String address = qtrno + ", " + qtraddr;
                        qtrlength = wrapText(address, 16);

                        dataFound = true;
                        recamt = Integer.parseInt(qtrEmp.getAmount());

                        if (slno == 0 || slno % 7 == 0) {
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

                                printPageFooter(dmpUtil, crb, totaldeduct, pageNo);
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
                            cnt1++;
                            printHeader(dmpUtil, crb, billdesc, schedule, billdate, pageNo, poolname, demandno);
                            if (pageNo > 1 && cnt1 > 1) {
                                printCarryForward(dmpUtil, totaldeduct, pageNo - 1);
                            }
                        }

                        totaldeduct = totaldeduct + Integer.parseInt(qtrEmp.getAmount());

                        slno++;
                        for (int len = 0; len < 2; len++) {
                            String nameString = "";
                            String desigString = "";
                            String qtrString = "";
                            if (len == 0) {
                                dmpUtil.writeToFile("");
                                dmpUtil.writeToFile(StringUtils.rightPad(" " + slno, 5) + StringUtils.rightPad(namelength[0], 33) + StringUtils.rightPad(qtrlength[0], 16) + StringUtils.leftPad("" + recamt, 18) + StringUtils.leftPad("" + recamt, 10));
                                qtrString = "";
                                nameString = "";
                                if (namelength.length > 1) {
                                    nameString = namelength[1];
                                }
                                if (qtrlength.length > 1) {
                                    qtrString = qtrlength[1];
                                }

                                dmpUtil.writeToFile(StringUtils.rightPad(" ", 5) + StringUtils.rightPad(nameString, 33) + StringUtils.rightPad(qtrString, 16) + StringUtils.leftPad("", 24));

                            } else if (len == 1) {
                                qtrString = "";
                                if (qtrlength.length > 2) {
                                    qtrString = qtrlength[2];
                                    dmpUtil.writeToFile(StringUtils.rightPad(" ", 5) + StringUtils.rightPad(desiglength[0], 33) + StringUtils.rightPad(qtrString, 16) + StringUtils.leftPad("", 24));
                                } else {
                                    if (schedule != null && schedule.equals("WRR")) {
                                        temp = 1;
                                        dmpUtil.writeToFile(StringUtils.rightPad(" ", 5) + StringUtils.rightPad(desiglength[0], 33) + StringUtils.rightPad(consumerno, 40));
                                    } else {
                                        dmpUtil.writeToFile(StringUtils.rightPad(" ", 5) + StringUtils.rightPad(desiglength[0], 33));
                                    }
                                }

                                desigString = "";
                                qtrString = "";
                                if (desiglength.length > 1) {
                                    desigString = desiglength[1];
                                }

                                if (qtrlength.length > 3) {
                                    qtrString = qtrlength[3];
                                    dmpUtil.writeToFile(StringUtils.rightPad(" ", 5) + StringUtils.rightPad(desigString, 33) + StringUtils.rightPad(qtrString, 16) + StringUtils.leftPad("", 24));
                                } else {
                                    if (temp == 0) {
                                        if (schedule != null && schedule.equals("WRR")) {
                                            temp = 1;
                                            dmpUtil.writeToFile(StringUtils.rightPad(" ", 5) + StringUtils.rightPad(desigString, 33) + StringUtils.rightPad(consumerno, 40));
                                        } else {
                                            dmpUtil.writeToFile(StringUtils.rightPad(" ", 5) + StringUtils.rightPad(desigString, 33));
                                        }
                                    }

                                }

                                if (desiglength.length > 2) {
                                    desigString = desiglength[2];
                                    if (schedule != null && schedule.equals("WRR")) {
                                        temp = 1;
                                        dmpUtil.writeToFile(StringUtils.rightPad(" ", 5) + StringUtils.rightPad(desigString, 33) + StringUtils.rightPad(consumerno, 40));
                                    } else {
                                        dmpUtil.writeToFile(StringUtils.rightPad(" ", 5) + StringUtils.rightPad(desigString, 33));
                                    }

                                } else {
                                    if (schedule != null && schedule.equals("WRR")) {
                                        if (temp == 0) {
                                            if (schedule != null && schedule.equals("WRR")) {
                                                dmpUtil.writeToFile(StringUtils.rightPad(" ", 5) + StringUtils.rightPad(" ", 33) + StringUtils.rightPad(consumerno, 40));
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }

                }
                abstractdeductlist.add(new Integer(totaldeduct));
                if (pageNo > 1) {
                    printPageFooter(dmpUtil, crb, totaldeduct, pageNo);
                   //pageNo ++; 

                }
                cnt1 = 0;
                cnt++;
            }

            /*Page Break in each GPF Series*/
            int totalGPFDeduction = 0;
            if (schedule.endsWith("HRR")) {
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
                lastHeader(dmpUtil, crb, billdesc, schedule, billdate, pageNo + 1, "");
                dmpUtil.writeToFile("");
                dmpUtil.writeToFile("");
                dmpUtil.writeToFile(StringUtils.rightPad("", 48) + "GRAND TOTAL");
                dmpUtil.writeToFile(StringUtils.rightPad("", 48) + "***********");
                dmpUtil.writeToFile("");
                dmpUtil.writeToFile(StringUtils.rightPad("", 30) + "DEPT NAME                              TOTAL AMOUNT");
                dmpUtil.writeToFile(StringUtils.rightPad("", 30) + "---------------------------------------------------");

                for (int j = 0; j < abstracthrrlist.size(); j++) {

                    if (Integer.parseInt(abstractdeductlist.get(j).toString()) > 0) {
                        totalGPFDeduction = totalGPFDeduction + Integer.parseInt(abstractdeductlist.get(j).toString());
                        dmpUtil.writeToFile(StringUtils.rightPad("", 31) + StringUtils.rightPad(abstracthrrlist.get(j).toString(), 33) + StringUtils.leftPad(abstractdeductlist.get(j).toString(), 13));
                    }
                }
                dmpUtil.writeToFile(StringUtils.rightPad("", 30) + "---------------------------------------------------");
                dmpUtil.writeToFile(StringUtils.rightPad("", 73) + totalGPFDeduction);

            }
            if (schedule.endsWith("WRR") && abstracthrrlist.size() > 1) {
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
                lastHeader(dmpUtil, crb, billdesc, schedule, billdate, pageNo + 1, "");
                dmpUtil.writeToFile("");
                dmpUtil.writeToFile("");
                dmpUtil.writeToFile(StringUtils.rightPad("", 48) + "GRAND TOTAL");
                dmpUtil.writeToFile(StringUtils.rightPad("", 48) + "***********");
                dmpUtil.writeToFile("");

                dmpUtil.writeToFile(StringUtils.rightPad("", 30) + "DEPT NAME                              TOTAL AMOUNT");
                dmpUtil.writeToFile(StringUtils.rightPad("", 30) + "---------------------------------------------------");

                for (int j = 0; j < abstracthrrlist.size(); j++) {

                    if (Integer.parseInt(abstractdeductlist.get(j).toString()) > 0) {
                        totalGPFDeduction = totalGPFDeduction + Integer.parseInt(abstractdeductlist.get(j).toString());
                        dmpUtil.writeToFile(StringUtils.rightPad("", 31) + StringUtils.rightPad(abstracthrrlist.get(j).toString(), 33) + StringUtils.leftPad(abstractdeductlist.get(j).toString(), 13));
                    }
                }
                dmpUtil.writeToFile(StringUtils.rightPad("", 30) + "---------------------------------------------------");
                dmpUtil.writeToFile(StringUtils.rightPad("", 73) + totalGPFDeduction);

            }
            dmpUtil.writeToFile((char) 12);
            dmpUtil.writeToFile((char) 26);
        } catch (Exception sqe) {
            sqe.printStackTrace();
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
