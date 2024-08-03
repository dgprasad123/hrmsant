package hrms.dao.payroll.services;

import hrms.common.CommonFunctions;
import hrms.common.DMPUtil;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.VehicleScheduleBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class VehicleScheduleDMPServices {

    public static final int characterPerLine = 87;
    public static final int lineperpage = 62;

    public ScheduleDAO comonScheduleDao;

    public void setComonScheduleDao(ScheduleDAO comonScheduleDao) {
        this.comonScheduleDao = comonScheduleDao;
    }

    private void printHeader(DMPUtil dmpUtil, CommonReportParamBean crb, String billdesc, String billdate, int pageNo) throws Exception {

        dmpUtil.writeToFile(StringUtils.rightPad("", 50) + "Page No:" + pageNo);
        String deptname = crb.getOfficeen();
        dmpUtil.writeToFile(StringUtils.center("" + deptname, 45));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile((char) 27);
        dmpUtil.writeToFile((char) 69);
        dmpUtil.writeToFile(StringUtils.rightPad("Bill NO/DATE : " + billdesc + "/" + billdate, 52));
        dmpUtil.writeToFile(StringUtils.rightPad("ABSTRACT OF VEHICLE LOAN FOR THE MONTH OF:" + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), 70));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------");

    }

    public boolean write(String billno, String folderPath, String fileSeparator, CommonReportParamBean crb) throws Exception {

        String fileName = "VehicleSchedule.txt";

        String billdesc = "";
        String billdate = "";

        int year = 0;
        int month = 0;
        boolean dataFound = false;
        boolean createFile = false;

        int tot = 0;
        String total1 = "";
        try {

            year = crb.getAqyear();
            month = crb.getAqmonth();
            billdesc = crb.getBilldesc();
            folderPath = folderPath + fileSeparator;
            billdate = crb.getBilldate();

            DMPUtil dmpUtil = null;

            if (createFile == false) {
                dmpUtil = new DMPUtil(folderPath, fileName);
                createFile = true;
            }
            dataFound = true;
            
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
            
            printHeader(dmpUtil, crb, billdesc, billdate, 1);
            
            VehicleScheduleBean vBean = comonScheduleDao.getVehicleScheduleList(billno, year, month);
            
            tot = vBean.getTotMCAPri() + vBean.getTotMopaPri() + vBean.getTotVEPri() + vBean.getTotMCAInt() + vBean.getTotMopaInt() + vBean.getTotVEInt();
            total1 = CommonFunctions.convertNumber(tot).toUpperCase();
            
            dmpUtil.writeToFile(StringUtils.rightPad("", 10) + StringUtils.rightPad("Motor Cycle Loan", 25) + StringUtils.rightPad(":", 10) + StringUtils.leftPad("" + vBean.getTotMCAPri(), 5));
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.rightPad("", 10) + StringUtils.rightPad("Motor Cycle Interest", 25) + StringUtils.rightPad(":", 10) + StringUtils.leftPad("" + vBean.getTotMCAInt(), 5));
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.rightPad("", 10) + StringUtils.rightPad("Moped Loan", 25) + StringUtils.rightPad(":", 10) + StringUtils.leftPad("" + vBean.getTotMopaPri(), 5));
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.rightPad("", 10) + StringUtils.rightPad("Moped Loan Interest", 25) + StringUtils.rightPad(":", 10) + StringUtils.leftPad("" + vBean.getTotMopaInt(), 5));
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.rightPad("", 10) + StringUtils.rightPad("Car Loan", 25) + StringUtils.rightPad(":", 10) + StringUtils.leftPad("" + vBean.getTotVEPri(), 5));
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.rightPad("", 10) + StringUtils.rightPad("Car Loan Interest", 25) + StringUtils.rightPad(":", 10) + StringUtils.leftPad("" + vBean.getTotVEInt(), 5));
            dmpUtil.writeToFile("------------------------------------------------------------------------------------");
            dmpUtil.writeToFile(StringUtils.leftPad("" + tot, 50));
            dmpUtil.writeToFile(StringUtils.leftPad("IN WORDS(RUPEES " + total1, 5) + ") ONLY");
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.leftPad(StringUtils.defaultString(crb.getDdoname()), 65));
            dmpUtil.writeToFile((char) 12);
            dmpUtil.writeToFile((char) 26);
        } catch (Exception sqe) {
            sqe.printStackTrace();
        } finally {
            
        }
        return dataFound;
    }
}
