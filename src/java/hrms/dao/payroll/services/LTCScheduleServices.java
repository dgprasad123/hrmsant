package hrms.dao.payroll.services;

import hrms.common.CommonFunctions;
import hrms.common.DMPUtil;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.LTCScheduleBean;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class LTCScheduleServices {

    public static final int characterPerLine = 87;
    public static final int lineperpage = 62;
    
    public ScheduleDAO comonScheduleDao;

    public void setComonScheduleDao(ScheduleDAO comonScheduleDao) {
        this.comonScheduleDao = comonScheduleDao;
    }
    
    private void printHeader(DMPUtil dmpUtil, CommonReportParamBean crb, String billdesc, String billdate, int pageNo, String chartAcc) throws Exception {

        String deptname = crb.getOfficeen();

        dmpUtil.writeToFile(StringUtils.center("         Page : " + pageNo, characterPerLine, " "));
        dmpUtil.writeToFile(" ");

        dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY FOR LTC ADVANCE", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("FOR THE MONTH OF " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("********", characterPerLine, " "));

        dmpUtil.writeToFile(StringUtils.rightPad("Name of the Department:", 30) + StringUtils.defaultString(crb.getDeptname()));
        dmpUtil.writeToFile(StringUtils.rightPad("Office Code:", 30) + StringUtils.defaultString(crb.getOfficeen() + "(" + crb.getOffcode() + ")"));
        dmpUtil.writeToFile(StringUtils.rightPad("Designation of DDO:", 30) + StringUtils.defaultString(crb.getDdoname()));
        dmpUtil.writeToFile(StringUtils.rightPad("Name of the Treasury:", 30) + StringUtils.defaultString(crb.getTreasuryname()));
        dmpUtil.writeToFile(StringUtils.rightPad("Bill No/Bill Date:", 30) + StringUtils.defaultString(crb.getBilldesc()) + "/" + StringUtils.defaultString(crb.getBilldate()));
        dmpUtil.writeToFile(StringUtils.rightPad("Head of Account:", 30) + StringUtils.defaultString(chartAcc));

        dmpUtil.writeToFile(StringUtils.repeat("-", 147));
        dmpUtil.writeToFile(" ");

        dmpUtil.writeToFile("  Sl No.  Name & Designation                       T.V. No    A/c No    Original     No of Instl    Instl   Recovery up    Balance       Remarks");
        dmpUtil.writeToFile("            of employee                                                  Amount       of Recovery    Amt    to the month   Outstanding");
        dmpUtil.writeToFile(StringUtils.repeat("-", 147));
        dmpUtil.writeToFile("    1           2                                     3          4         5              6           7          8             9           10");
        dmpUtil.writeToFile(StringUtils.repeat("-", 147));
    }

    private void printPageFooter(DMPUtil dmpUtil, CommonReportParamBean crb, int pageTotal) {

        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.repeat("-", characterPerLine));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("* Page Total * :" + StringUtils.leftPad(pageTotal + "", 65));
        dmpUtil.writeToFile(StringUtils.leftPad("In Words (Rupees " + StringUtils.upperCase(CommonFunctions.convertNumber(pageTotal) + " ) Only"), 81));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.defaultString(crb.getDdoname()));
    }

    public boolean write(String billno, String folderPath, String fileSeparator, CommonReportParamBean crb) throws Exception {

        String fileName = "LTCSCHEDULE.txt";

        String chartAcc = "";

        boolean dataFound = false;
        boolean createFile = false;

        int year = 0;
        int month = 0;
        String billdesc = "";
        String billdate = "";
        try {
            year = crb.getAqyear();
            month = crb.getAqmonth();
            billdesc = crb.getBilldesc();
            folderPath = folderPath + fileSeparator;
            billdate = crb.getBilldate();

            DMPUtil dmpUtil = null;

            chartAcc = comonScheduleDao.getChartOfAccount(billno);

            int pageNo = 0;

            int slno = 0;
            int total = 0;
            int cnt = 0;
            int bal = 0;
            int payBillMonth = 0;
            int payBillYear = 0;

            List<LTCScheduleBean> empList = comonScheduleDao.getLTCScheduleEmpList(billno, year, month);

            for (LTCScheduleBean ltcbean : empList) {

                if (ltcbean.getEmpName() != null && !ltcbean.getEmpName().equals("")) {
                    if (createFile == false) {
                        dmpUtil = new DMPUtil(folderPath, fileName);
                        createFile = true;
                    }
                    dataFound = true;

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
                        printHeader(dmpUtil, crb, billdesc, billdate, pageNo, chartAcc);
                    }
                    total = total + ltcbean.getAdAmt();
                    bal = ltcbean.getPrincipalAmt() - ltcbean.getTotRecAmt();
                    slno++;
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("    " + StringUtils.rightPad(slno + "", 5) + StringUtils.rightPad(ltcbean.getEmpName(), 45) + StringUtils.rightPad(StringUtils.defaultString(ltcbean.getVchNo()), 7) + StringUtils.rightPad(StringUtils.defaultString(ltcbean.getAccNo()), 12) + StringUtils.rightPad(StringUtils.defaultString(ltcbean.getPrincipalAmt()+""), 13) + StringUtils.rightPad(StringUtils.defaultString(ltcbean.getRefDesc()), 14) + StringUtils.rightPad(StringUtils.defaultString(ltcbean.getAdAmt()+""), 9) + StringUtils.rightPad(StringUtils.defaultString(ltcbean.getTotRecAmt()+""), 14) + StringUtils.rightPad(bal + "", 14) + StringUtils.defaultString(ltcbean.getGpfNo()));
                    dmpUtil.writeToFile("    " + StringUtils.rightPad("", 5) + StringUtils.rightPad(ltcbean.getEmpDesg(), characterPerLine));
                    cnt++;
                }
            }

            if (createFile == true) {
                if (cnt > 0) {
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile(StringUtils.repeat("-", 147));
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile(StringUtils.rightPad("* Grand Total * ", 101) + total);
                    dmpUtil.writeToFile(StringUtils.leftPad("In Words (Rupees " + StringUtils.upperCase(CommonFunctions.convertNumber(total) + " ) Only"), 81));
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
                    /*dmpUtil.writeToFile(StringUtils.leftPad("Designation of the Drawing Officer: ",characterPerLine));      
                     dmpUtil.writeToFile(StringUtils.leftPad(StringUtils.defaultString(crb.getDdoname()),characterPerLine));*/
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
