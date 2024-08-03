package hrms.dao.payroll.services;

import hrms.common.CommonFunctions;
import hrms.common.DMPUtil;
import hrms.common.Numtowordconvertion;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.ExcessPayBean;
import hrms.model.payroll.schedule.HCScheduleBean;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class HCScheduleServices {

    public static final int characterPerLine = 87;
    public static final int lineperpage = 62;

    public ScheduleDAO comonScheduleDao;

    public void setComonScheduleDao(ScheduleDAO comonScheduleDao) {
        this.comonScheduleDao = comonScheduleDao;
    }

    private void printHeader(DMPUtil dmpUtil, CommonReportParamBean crb, String billdesc, String billdate, int pageNo) throws Exception {
        String officename = crb.getOfficeen();
        dmpUtil.writeToFile(StringUtils.center("      Page : " + pageNo, characterPerLine, " "));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile((char) 27);
        dmpUtil.writeToFile((char) 69);
        dmpUtil.writeToFile(StringUtils.rightPad("Bill No : " + billdesc + ((char) 27) + ((char) 70), characterPerLine, " "));
        dmpUtil.writeToFile("0075 - Misc. General Services - 800 - Other Receipts - 9913310");
        dmpUtil.writeToFile("Recovery of Conveyance Charges form Officers using Govt. Vehicles");
        dmpUtil.writeToFile("FOR THE Month of  " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear());
        dmpUtil.writeToFile("FROM THE PAY BILLS OF " + officename);
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("-------------------------------------------------------------------------");
        dmpUtil.writeToFile(" Sl    Name & Designation                             Amount");
        dmpUtil.writeToFile(" No.   of employee                                  deducted");
        dmpUtil.writeToFile("                                                   ( in Rs.)");
        dmpUtil.writeToFile("--------------------------------------------------------------------------");
        dmpUtil.writeToFile(" 1         2                                             3");
        dmpUtil.writeToFile("--------------------------------------------------------------------------");
    }

    public boolean write(String billno, String folderPath, String fileSeparator, CommonReportParamBean crb) throws Exception {

        String fileName = "HCSCHEDULE.txt";

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
            int slno = 0;
            int total = 0;
            String adamt = "";
            int cnt = 0;
            String idno = "";

            List<HCScheduleBean> empList = comonScheduleDao.getHCScheduleEmpList(billno, year, month);

            for (HCScheduleBean hc : empList) {

                if (hc.getEmpName() != null && !hc.getEmpName().equals("")) {
                    if (createFile == false) {
                        dmpUtil = new DMPUtil(folderPath, fileName);
                        createFile = true;
                    }
                    dataFound = true;

                    adamt = hc.getAmt();
                    total = total + Integer.parseInt(adamt);
                    idno = hc.getIdNumber();

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
                    dmpUtil.writeToFile(StringUtils.rightPad("" + slno, 5) + StringUtils.rightPad(hc.getEmpName(), 45) + StringUtils.leftPad(adamt, 9));
                    dmpUtil.writeToFile(StringUtils.rightPad("", 5) + StringUtils.rightPad(hc.getDesg(), characterPerLine));

                    cnt++;
                    if (slno % 10 == 0) {
                        cnt = 0;
                        dmpUtil.writeToFile("");
                        dmpUtil.writeToFile("-------------------------------------------------------------------------");
                        dmpUtil.writeToFile(StringUtils.rightPad("* Page Total * : ", 40) + total);
                        dmpUtil.writeToFile("-------------------------------------------------------------------------");
                        if (total > 0) {
                            dmpUtil.writeToFile(StringUtils.leftPad("", 30) + "In Words(RUPEES " + StringUtils.upperCase(Numtowordconvertion.convertNumber(total) + ")"));
                            dmpUtil.writeToFile(StringUtils.leftPad("", 30) + "ONLY");
                        }
                        dmpUtil.writeToFile("");
                        dmpUtil.writeToFile("");
                        dmpUtil.writeToFile(StringUtils.rightPad("", 60) + crb.getDdoname());
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
                    dmpUtil.writeToFile("-------------------------------------------------------------------------");
                    dmpUtil.writeToFile("* Grand Total * :                                      " + total);
                    dmpUtil.writeToFile("-------------------------------------------------------------------------");
                    if (total > 0) {
                        dmpUtil.writeToFile(StringUtils.leftPad("", 30) + "In Words(RUPEES " + StringUtils.upperCase(Numtowordconvertion.convertNumber(total) + ")"));
                        dmpUtil.writeToFile(StringUtils.leftPad("", 30) + "ONLY");
                    }
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile(StringUtils.rightPad("", 60) + crb.getDdoname());
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("");
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
