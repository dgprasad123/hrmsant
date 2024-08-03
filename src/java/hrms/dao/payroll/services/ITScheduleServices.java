/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.services;

import hrms.common.CommonFunctions;
import hrms.common.DMPUtil;
import hrms.common.Numtowordconvertion;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.ItScheduleBean;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author Surendra
 */
@Service
public class ITScheduleServices {

    public static final int characterPerLine = 87;
    public static final int lineperpage = 62;

    public PayBillDMPDAO paybillDmpDao;

    public ScheduleDAO comonScheduleDao;

    public void setPaybillDmpDao(PayBillDMPDAO paybillDmpDao) {
        this.paybillDmpDao = paybillDmpDao;
    }

    public void setComonScheduleDao(ScheduleDAO comonScheduleDao) {
        this.comonScheduleDao = comonScheduleDao;
    }

    private void printHeader(DMPUtil dmpUtil, CommonReportParamBean crb, String billdesc, String billdate, int pageNo) throws Exception {
        String deptname = crb.getOfficeen();
        dmpUtil.writeToFile(StringUtils.center(deptname + "         Page : " + pageNo, characterPerLine, " "));

        dmpUtil.writeToFile(StringUtils.center("Schedule of recovery of INCOME TAX DEDUCTION ", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("MEMORANDUM INDICATING THE AMOUNTS CREDITABLE ", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("TO CENTRAL AS SHOWN BELOW", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center(" for the month of :   " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        dmpUtil.writeToFile((char) 27);
        dmpUtil.writeToFile((char) 69);
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.rightPad("Bill No : " + billdesc + ((char) 27) + ((char) 70), 35) + "TAN NO : " + StringUtils.defaultString(crb.getTanno()));
        dmpUtil.writeToFile("8658-INCOME TAX DEDUCTION");
        dmpUtil.writeToFile("BT SL NO-7112");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------");
        dmpUtil.writeToFile("Sl   Name & Designation                  PAN NO         Gross             Deduction");
        dmpUtil.writeToFile("No.       of employee                                   Salary");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------");
        dmpUtil.writeToFile(" 1         2                                3              4                 5 ");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------");
    }

    public boolean write(String billno, String folderPath, String fileSeparator, CommonReportParamBean crb) throws Exception {
        String fileName = "ITSCHEDULE.txt";
        int year = 0;
        int month = 0;
        String billdesc = "";
        String billdate = "";
        boolean dataFound = false;
        boolean createFile = false;
        try {
            year = crb.getAqyear();
            month = crb.getAqmonth();
            billdesc = crb.getBilldesc();
            folderPath = folderPath + fileSeparator;
            billdate = crb.getBilldate();

            DMPUtil dmpUtil = null;

            int pageNo = 0;
            int basic = 0;
            int slno = 0;
            int total = 0;
            String gross = "";
            String adamt = "";
            int cnt = 0;
            String idno = "";

            List<ItScheduleBean> itList = comonScheduleDao.getITScheduleEmployeeList(billno, "IT",month,year);

            for (ItScheduleBean its : itList) {
                if (createFile == false) {
                    dmpUtil = new DMPUtil(folderPath, fileName);
                    createFile = true;
                }
                if (its.getEmpname() != null && !its.getEmpname().equals("")) {
                    dataFound = true;
                    gross = its.getEmpBasicSal();
                    adamt = its.getEmpDedutAmount();
                    total = total + Integer.parseInt(adamt);
                    idno = its.getEmpPanNo();

                    if (slno == 0 || slno % 10 == 0) {
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
                        printHeader(dmpUtil, crb, billdesc, billdate, pageNo);
                    }
                    slno++;
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile(StringUtils.rightPad("" + slno, 4) + StringUtils.rightPad(its.getEmpname(), 35) + StringUtils.leftPad(idno, 8) + StringUtils.leftPad("" + gross, 12) + StringUtils.leftPad(adamt, 19));
                    dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.rightPad(its.getEmpdesg(), characterPerLine));
                    cnt++;
                    if (slno % 10 == 0) {
                        cnt = 0;
                        dmpUtil.writeToFile("");
                        dmpUtil.writeToFile("------------------------------------------------------------------------------------");
                        dmpUtil.writeToFile("* Grand Total * :                                                           " + total);
                        dmpUtil.writeToFile("------------------------------------------------------------------------------------");
                        if (total > 0) {
                            dmpUtil.writeToFile(StringUtils.leftPad("In Words(RUPEES " + StringUtils.upperCase(Numtowordconvertion.convertNumber(total) + ")"), characterPerLine));
                            dmpUtil.writeToFile(StringUtils.leftPad("ONLY", characterPerLine));
                        }

                        dmpUtil.writeToFile("");
                        dmpUtil.writeToFile(StringUtils.leftPad("Designation of the Drawing Officer:", characterPerLine));
                        dmpUtil.writeToFile(StringUtils.leftPad(StringUtils.defaultString(crb.getDdoname()), characterPerLine));
                        dmpUtil.writeToFile("");
                        dmpUtil.writeToFile("");
                        dmpUtil.writeToFile("");
                        dmpUtil.writeToFile("");

                    }
                }
            }
            if (createFile == true) {
                if (cnt > 0) {
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("------------------------------------------------------------------------------------");
                    dmpUtil.writeToFile("* Grand Total * :                                                           " + total);
                    dmpUtil.writeToFile("------------------------------------------------------------------------------------");
                    if (total > 0) {
                        dmpUtil.writeToFile(StringUtils.leftPad("", 30) + "In Words(RUPEES " + StringUtils.upperCase(Numtowordconvertion.convertNumber(total) + ")"));
                        dmpUtil.writeToFile(StringUtils.leftPad("", 30) + "ONLY");
                    }
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile(StringUtils.leftPad("Designation of the Drawing Officer:", characterPerLine));
                    dmpUtil.writeToFile(StringUtils.leftPad(StringUtils.defaultString(crb.getDdoname()), characterPerLine));
                    dmpUtil.writeToFile("");
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

}
