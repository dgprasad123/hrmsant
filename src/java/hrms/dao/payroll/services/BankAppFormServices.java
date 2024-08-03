/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.services;

import hrms.common.DMPUtil;
import hrms.common.DataBaseFunctions;
import hrms.common.Numtowordconvertion;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.BankAcountScheduleBean;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAppFormServices {

    public static final int characterPerLine = 87;
    public static final int lineperpage = 62;

    public ScheduleDAO comonScheduleDao;

    public void setComonScheduleDao(ScheduleDAO comonScheduleDao) {
        this.comonScheduleDao = comonScheduleDao;
    }
    
    private void printHeader(DMPUtil dmpUtil, String billdesc, String billdate, int net) throws Exception {
        dmpUtil.writeToFile(StringUtils.center("FORM NO O.T.C.   82 ", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("(SEE RULES 138(I),256 (I) AND 368 )", characterPerLine, " "));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("        TO ");
        dmpUtil.writeToFile(StringUtils.rightPad("", 10) + StringUtils.rightPad("The CePC & Manager,                    ", characterPerLine));
        dmpUtil.writeToFile(StringUtils.rightPad("", 10) + StringUtils.rightPad("Reserve Bank Of India,                    ", characterPerLine));
        dmpUtil.writeToFile(StringUtils.rightPad("", 10) + StringUtils.rightPad("PAD Bhubaneswar.                    ", characterPerLine));
        dmpUtil.writeToFile(StringUtils.rightPad("", 10) + StringUtils.rightPad("", characterPerLine));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("        Sir,");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.rightPad("", 10) + StringUtils.rightPad("Please pay enclosed Bill No. " + billdesc + " Dated " + billdate, 20));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("         the 2012-2013 for Net Amount of Rs. " + net + "/-(RUPEES ");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.rightPad("", 10) + StringUtils.upperCase(Numtowordconvertion.convertNumber(net) + "  Only)"));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("         in cash/in shape of BD/BC to                      , T.S. . ");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("         DTI,Orissa,BBSR whose signature is attested by me below. ");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.rightPad("", 50) + StringUtils.rightPad("Yours Faithfully", characterPerLine));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("          Date.............................. Signature of Drawing Officer");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("          Attested specimen signature ");
        dmpUtil.writeToFile("             of the messanger ");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("          Signature of the Drawing officer");
        dmpUtil.writeToFile("              with his official seal)");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("          Receving Payment");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("          Date...................");

    }

    public boolean write(String billno, String folderPath, String fileSeparator, CommonReportParamBean crb) throws Exception {

        String fileName = "BANKAPPFORM.txt";
        
        int year = 0;
        int month = 0;
        String billdesc = "";
        String billdate = "";

        int net = 0;
        int gross = 0;
        
        int allowanceAmt = 0;
        int deductionAmt = 0;
        int curbasic = 0;

        try {
            year = crb.getAqyear();
            month = crb.getAqmonth();
            billdesc = crb.getBilldesc();
            folderPath = folderPath + fileSeparator;
            billdate = crb.getBilldate();

            curbasic = comonScheduleDao.getBasicAmount(billno);
            allowanceAmt = comonScheduleDao.getAllowanceAndDeductionAmount(billno,"A",month,year);
            deductionAmt = comonScheduleDao.getAllowanceAndDeductionAmount(billno,"D",month,year);

            gross = curbasic + allowanceAmt;
            net = gross - deductionAmt;
            DMPUtil dmpUtil = new DMPUtil(folderPath, fileName);

            printHeader(dmpUtil, billdesc, billdate, net);
            dmpUtil.writeToFile((char) 12);
            dmpUtil.writeToFile((char) 26);
        } catch (Exception e) {
            e.printStackTrace();
        }
      return true;  
    }
}
