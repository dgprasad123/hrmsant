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
import hrms.model.payroll.schedule.PrivateLoanScheduleBean;
import hrms.model.payroll.schedule.ScheduleHelper;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author Surendra
 */
@Service
public class PrivateLoanScheduleServices {
    
    public ScheduleDAO comonScheduleDao;

    public static final int characterPerLine = 87;
    public static final int lineperpage = 62;

    public void setComonScheduleDao(ScheduleDAO comonScheduleDao) {
        this.comonScheduleDao = comonScheduleDao;
    }
    
    

    private void printHeader(DMPUtil dmpUtil, CommonReportParamBean crb, String billdesc, String billdate, String ddoacctno, int pageNo) throws Exception {
        String offname = crb.getOfficename();
        dmpUtil.writeToFile(StringUtils.center("         Page : " + pageNo, characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("" + offname, characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("PRIVATE LOAN/DEDUCTION SCHEDULE FOR THE MONTH OF :   " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.rightPad("DEPARTMENT : " + billdesc + ((char) 27) + ((char) 70), 35));
        dmpUtil.writeToFile("------------------------------------------------------------------------------------");
        dmpUtil.writeToFile("Sl   Name & Desig                                  AMOUNT(in Rs.)(PVT LOAN/DED DESCRIPTION)");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------");
        dmpUtil.writeToFile(" 1        2                                                       3        ");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------");
    }

    private void printPageFooter(DMPUtil dmpUtil, CommonReportParamBean crb, int pageTotal) {
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------");
        dmpUtil.writeToFile("* Page Total * :" + StringUtils.leftPad(pageTotal + "", 53));
        dmpUtil.writeToFile("------------------------------------------------------------------------------------");
        dmpUtil.writeToFile(StringUtils.leftPad("In Words (Rupees " + StringUtils.upperCase(Numtowordconvertion.convertNumber(pageTotal) + " ) Only"), 76));
        dmpUtil.writeToFile("");
    }

    private void printCarryForward(DMPUtil dmpUtil, int pageTotal, int pageNo) {
        dmpUtil.writeToFile(StringUtils.rightPad("CARRIED FROM PAGE :" + pageNo, 22) + StringUtils.leftPad("" + pageTotal, 54));
        dmpUtil.writeToFile("-------------------------------------------------------------------------------------");
    }

    public boolean write(String billno, String folderPath, String fileSeparator, CommonReportParamBean crb) throws Exception {
        String fileName = "PVTLOANSCHEDULE.txt";
        int year = 0;
        int month = 0;
        String billdesc = "";
        String billdate = "";
        String ddoacctno = "";
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
            int slno = 0;
            int empcnt = 0;
            int total = 0;
            int cnt = 0;
            String query1 = "";
            String empCode = "";
            
            List<ScheduleHelper> pvtlist=comonScheduleDao.getPrivateLoanScheduleEmpDetails(billno,month,year);
            
            
            
            for (ScheduleHelper pvtsSch : pvtlist) {
                if (createFile == false) {
                    dmpUtil = new DMPUtil(folderPath, fileName);
                    createFile = true;
                }
                empCode = pvtsSch.getEmpcode();
                dataFound = true;
                String aqslno = pvtsSch.getAqslNo();
                
                List<PrivateLoanScheduleBean> amtlist=pvtsSch.getHelperList();
                
                boolean employeePrinted = false;

                for (PrivateLoanScheduleBean loanBean : amtlist) {
                    if (pvtsSch.getEmpname() != null && !pvtsSch.getEmpname().equals("")) {
                        if (slno == 0 || slno % 12 == 0) {
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
                                printPageFooter(dmpUtil, crb, total);
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
                            printHeader(dmpUtil, crb, billdesc, billdate, ddoacctno, pageNo);
                            if (pageNo > 1) {
                                printCarryForward(dmpUtil, total, pageNo - 1);
                            }
                        }
                        total = total + loanBean.getDeductedAmt();
                        slno++;
                        if (employeePrinted == false) {
                            employeePrinted = true;
                            empcnt++;
                            dmpUtil.writeToFile("");
                            dmpUtil.writeToFile(StringUtils.rightPad(" " + empcnt, 4) + StringUtils.rightPad(StringUtils.defaultString(pvtsSch.getEmpname()), 50) + StringUtils.leftPad(loanBean.getDeductedAmt()+"", 15) + " (" + loanBean.getDeductedAmtDesc() + ")");
                            dmpUtil.writeToFile(StringUtils.rightPad(" ", 4) + StringUtils.rightPad(pvtsSch.getEmpdesg(), characterPerLine));
                        } else {
                            dmpUtil.writeToFile(StringUtils.rightPad(" ", 4) + StringUtils.rightPad(" ", 50) + StringUtils.leftPad(loanBean.getDeductedAmt()+"", 15) + " (" + loanBean.getDeductedAmtDesc() + ")");
                        }
                        cnt++;

                    }
                }

            }

            if (createFile == true) {
                if (cnt > 0) {
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("------------------------------------------------------------------------------------");
                    dmpUtil.writeToFile("* Grand Total * :" + StringUtils.leftPad(total + "", 52));
                    dmpUtil.writeToFile("------------------------------------------------------------------------------------");
                    dmpUtil.writeToFile(StringUtils.leftPad("In Words (Rupees " + StringUtils.upperCase(Numtowordconvertion.convertNumber(total) + " ) Only"), 76));
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("* Please pay Rs.________________________by transfer credit to the DDO's Current");
                    dmpUtil.writeToFile("Account No." + ddoacctno + " recoveries of loans from banks and financial institutions ");
                    dmpUtil.writeToFile("in respect of _______________ number of employees");

                }
                dmpUtil.writeToFile((char) 12);
                dmpUtil.writeToFile((char) 26);
            }

        } catch (Exception sqe) {
            sqe.printStackTrace();
        } finally {

        }
        return dataFound;
    }

}
