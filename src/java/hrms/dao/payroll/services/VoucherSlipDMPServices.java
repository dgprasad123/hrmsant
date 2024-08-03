package hrms.dao.payroll.services;

import hrms.common.DMPUtil;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.VoucherSlipBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class VoucherSlipDMPServices {

    public static final int characterPerLine = 87;
    public static final int lineperpage = 62;

    public ScheduleDAO comonScheduleDao;

    public void setComonScheduleDao(ScheduleDAO comonScheduleDao) {
        this.comonScheduleDao = comonScheduleDao;
    }

    private String allignString(String qtraddr) throws Exception {
        String firstStr = "";
        if (qtraddr.length() > 35) {
            firstStr = qtraddr.substring(0, 35);
        } else {
            firstStr = qtraddr;
        }
        return firstStr;
    }

    private String allignString1(String ofName) throws Exception {
        String firstStr = "";
        if (ofName.length() > 45) {
            firstStr = ofName.substring(0, 44);
        } else {
            firstStr = ofName;
        }
        return firstStr;
    }

    private void printHeader(DMPUtil dmpUtil) throws Exception {
        dmpUtil.writeToFile(StringUtils.center("VOUCHER SLIP", characterPerLine, " "));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.center("FORM O.G.F.R.", characterPerLine, " "));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.center("(See Rule 318)", characterPerLine, " "));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.center("(To be returned in original by the Treasury Officer)", characterPerLine, " "));
    }

    private void printBody1(DMPUtil dmpUtil, CommonReportParamBean crb, String trOfficerName, String trName, String trName1) throws Exception {

        dmpUtil.writeToFile("                                               |                                        ");
        dmpUtil.writeToFile("                                               | (To be filled in the Treasury)         ");
        dmpUtil.writeToFile("                                               |                                        ");
        dmpUtil.writeToFile("                                               |                                        ");
        dmpUtil.writeToFile(" To                                            |  To                                    ");
        dmpUtil.writeToFile(" The " + StringUtils.rightPad(trOfficerName, 42) + "|" + "  The " + StringUtils.defaultString(crb.getDdoname()) + "              ");
        dmpUtil.writeToFile(" " + StringUtils.rightPad(trName, 46) + "|");
        dmpUtil.writeToFile(" " + StringUtils.rightPad(trName1, 46) + "|");
        dmpUtil.writeToFile("                                               |                                        ");
        dmpUtil.writeToFile(" Please furnish the Treasury Voucher           |  Returned with Treasury Voucher        ");
        dmpUtil.writeToFile(" no and date of the Bill sent herewith         |  no.and date as noted below:-          ");
        dmpUtil.writeToFile(" for encashment.                               |                                        ");
        dmpUtil.writeToFile("                                               |                                        ");
        dmpUtil.writeToFile("                                               |                                        ");
        dmpUtil.writeToFile("                                               |                                        ");
        dmpUtil.writeToFile(" " + StringUtils.leftPad(StringUtils.defaultString(crb.getDdoname()), 46) + "|");
        dmpUtil.writeToFile("                             (Drawing Officer) |  Signature ..........................  ");
        dmpUtil.writeToFile("                                               |                      Treasury Officer  ");
        dmpUtil.writeToFile("                                               |                                        ");
        dmpUtil.writeToFile(StringUtils.center("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", characterPerLine, " "));

    }

    private void printBody2(DMPUtil dmpUtil, String year, String month, String billdesc, double billGrossAmt, double billNetAmt, String offName, String offNamefinal) throws Exception {

        dmpUtil.writeToFile("                                               |                                           ");
        dmpUtil.writeToFile(" Bill No:- " + StringUtils.rightPad(billdesc + "", 36) + "|" + " Amount Paid ........................");
        dmpUtil.writeToFile("                                               |                                           ");
        dmpUtil.writeToFile(" Bill Particulars:-                            | T.V. No ............................      ");
        dmpUtil.writeToFile("                                               |                                           ");
        dmpUtil.writeToFile(" Monthly Paybill of                            | Date ...............................      ");
        dmpUtil.writeToFile(" " + StringUtils.rightPad(offName + "", 46) + "|");
        dmpUtil.writeToFile("" + StringUtils.rightPad(offNamefinal + " for " + month + "/" + year + "", 47) + "|");
        dmpUtil.writeToFile("                                               |                                           ");
        dmpUtil.writeToFile(" Gross     : Rs." + StringUtils.rightPad(billGrossAmt + "", 31) + "|");
        dmpUtil.writeToFile("               --------------------------      |                                           ");
        dmpUtil.writeToFile(" Net       : Rs." + StringUtils.rightPad(billNetAmt + "", 31) + "|");
        dmpUtil.writeToFile("               --------------------------      |                                           ");
        dmpUtil.writeToFile("                                               |                                           ");
        dmpUtil.writeToFile("                                               |                                           ");
        dmpUtil.writeToFile("                                               |                                           ");
        dmpUtil.writeToFile("           Signature of Accountant             |               Signature of Treasury       ");
        dmpUtil.writeToFile("                                               |                   Accountant              ");
        dmpUtil.writeToFile("                                               |                                           ");
        dmpUtil.writeToFile("                                               |                                           ");
        dmpUtil.writeToFile(StringUtils.center("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", characterPerLine, " "));

    }

    public boolean write(String billno, String folderPath, String fileSeparator, CommonReportParamBean crb) throws Exception {

        String fileName = "VOUCHERSLIP.txt";

        String dmdNo = "";
        String majorHead = "";
        String minorHeadDesc = "";
        String minorHead = "";
        String smHead1Desc = "";
        String pType = "";
        String smHead2Desc = "";
        String smHead3 = "";

        int year = 0;
        int month = 0;
        String billdesc = "";
        String billdate = "";

        double billGrossAmt = 0.0;
        double billDeductionAmt = 0.0;
        double billNetAmt = 0.0;
        String trCode = "";
        try {
            year = crb.getAqyear();
            month = crb.getAqmonth();
            billdesc = crb.getBilldesc();
            folderPath = folderPath + fileSeparator;
            billdate = crb.getBilldate();

            DMPUtil dmpUtil = new DMPUtil(folderPath, fileName);

            VoucherSlipBean voucherBean = comonScheduleDao.getVoucherSlipScheduleDetails(billno, year, month);

            printHeader(dmpUtil);

            dmdNo = voucherBean.getDemandno();

            majorHead = voucherBean.getMajorhead();

            minorHeadDesc = voucherBean.getMinorheaddesc();

            minorHead = voucherBean.getMinorhead();

            smHead1Desc = voucherBean.getSubminorhead1desc();

            pType = voucherBean.getPostType();

            smHead2Desc = voucherBean.getSubminorhead2desc();

            smHead3 = voucherBean.getSubminorhead3();

            trCode = voucherBean.getTreasuryCode();

            dmpUtil.writeToFile("");
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.center("Demand No:-" + dmdNo + "-" + majorHead + "-" + minorHeadDesc, characterPerLine, " "));
            dmpUtil.writeToFile(StringUtils.center("-" + minorHead + "-" + smHead1Desc + "-" + pType + "-" + smHead2Desc + "-" + smHead3, characterPerLine, " "));

            dmpUtil.writeToFile(StringUtils.center("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", characterPerLine, " "));

            year = Integer.parseInt(voucherBean.getYear());

            month = Integer.parseInt(voucherBean.getMonth());

            billdesc = voucherBean.getBillDesc();

            billGrossAmt = Double.valueOf(voucherBean.getGrossAmount() + "").longValue();
            billNetAmt = Double.valueOf(voucherBean.getNetAmount() + "").longValue();
            // CODE FOR BODY1
            String trName = "";
            String secondStr = "";

            String demoTR = voucherBean.getTreasuryName();
            trName = allignString(demoTR);
            String trName1 = demoTR;
            secondStr = trName1.substring(trName.length(), trName1.length());

            String trOfficerName = "TREASURY OFFICER";
            printBody1(dmpUtil, crb, trOfficerName, trName, secondStr);

            String offName = allignString1(crb.getOfficename());
            String offName1 = crb.getOfficename();
            String offNamefinal = offName1.substring(offName.length(), offName1.length());

            printBody2(dmpUtil, year + "", (month + 1) + "", billdesc, billGrossAmt, billNetAmt, offName, offNamefinal);
            dmpUtil.writeToFile((char) 12);
            dmpUtil.writeToFile((char) 26);

        } catch (Exception e) {
            e.printStackTrace();
        }
      return true;  
    }
}