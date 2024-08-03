package hrms.dao.payroll.services;

import hrms.common.DMPUtil;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import java.sql.SQLException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class DACertificateServices {

    public static final int characterPerLine = 87;
    public static final int lineperpage = 62;

    public ScheduleDAO comonScheduleDao;

    public void setComonScheduleDao(ScheduleDAO comonScheduleDao) {
        this.comonScheduleDao = comonScheduleDao;
    }

    public boolean write(String billno, String folderPath, String fileSeparator, CommonReportParamBean crb) throws Exception {

        String fileName = "DACERTIFICATE.txt";
        
        try {
            
            DMPUtil dmpUtil = new DMPUtil(folderPath, fileName);

            dmpUtil.writeToFile(StringUtils.leftPad("1. Certified that all those for whom dearness Allowance has been charged in this Bill are eligible to draw the same", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("under the terms & conditions laid down in the Finance Department Office Menorandum No.43937/F.,dated 29-11-1992,No.", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("35864/F., dated 14-08-1992., No.31204/F., dated 17-07-1993, No. 49950/F., dated 27-11-1993, No. 21720/F., dated", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("16-06-1994 , No. 37371., dated 1-11-1994, No.23058/F.,dated 2-06-1995, No. 41732/F.,dated 25-10-1995, No.23986/F.,", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("dated20-05-1996,No.46402/F.,dated 5-11-96, No.27177/F.,dated 21-061997, No.50461/F.,dated 20-12-1997, No.5744/GA.,", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("dated 4-12-1997,No.33929/F.,dated 12-08-1998,No.52561/F.,dated 23-12-1998,No.39597/F.,dated 26-09-2000,No.31902/F.,", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("dated 21-05-2001,No.33417/F.,dated 15-07-2002,No.8335/F.,dated 28-02-2003,No.43255/F.,dated 6-10-2003,No.7226/F.,", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("dated 21-02-2004 and No.45455/F.,dated 16-10-2004,No.22481/F.,dated 06-05-2005,No.39341/F.,dated15-09-2006,No.55805/F,", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("dated 29-12-2008,No.23134/F dated 14-03-2010.,No.31434/F dated 8-10-2013,No.13884/F dated 26-4-2014.,", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("No.29187/F dated 15-10-2014 and No.12410/F dated 20-04-2015", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("", 10));

            dmpUtil.writeToFile(StringUtils.leftPad("2.  Certified that all those for whom Interim Relief has been charged in this Bill are eligible to draw the same under", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("under the terms & conditions laid down in the Finance Department Office Menorandum No.22835/F.,dated 31-5-1995, Office", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("Memorandum No. 8647/F., dated 2-03-1996 and Office Menorandum No.36427/F., dated 08-09-1997.", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("", 10));

            dmpUtil.writeToFile(StringUtils.leftPad("3.  Certified that in respect of the Employee whose Pay and Allowance have been drawn in this Bill, instalment of all ", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("short-term advance due for  recovery from them have been recovered by  deduction  and  that  the  same have been duly ", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("exhibited in the register maintanied for the purpose.", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("", 10));

            dmpUtil.writeToFile(StringUtils.leftPad("4.  Certified that the Government Servent whose Pay and Allowance are being drawn under this Bill has made the deposit", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("as required under the Group Insurance Scheme as per the Resolution No.19043/F., dated 15-04-1976 of the Government in", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("the Finance Department. Recovery of the advance has been shown in this bill in respect of those who have availed the", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("advance from government to make such deposit. ", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("", 10));

            dmpUtil.writeToFile(StringUtils.leftPad("5. Certified that the money drawn in shape of Cash/Draft through the bills presented during the previous months has been", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("distrubuted except the money drawn in A.C. Bills and the amount now proposed for withdrwal in this bill in shape of the", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("Cash/ Bank Draft shall be distributed within a period of therr days from the date of actual drawl from the Bank/Treasury.", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("", 10));

            dmpUtil.writeToFile(StringUtils.leftPad("6.  Certified that the loan account number in respect of House Building Advance, Car/Scooter/Motor Cycle/moped Advance", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("etc. has not yet been communicated by Accountant General, Odisha to this office as a result the same has not yet been", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("indicated in the relevant recovery schedules and the loan account number would be quoted as and when the same is", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("communicated by Accountant General, Odisha.", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("", 10));

            dmpUtil.writeToFile(StringUtils.leftPad("7.  All the money drawn in Cash/Bank Draft upto the period 31-032001 have been fully disbursed and no amoun is laying", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("undisbursed with me.", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("", 10));

            dmpUtil.writeToFile(StringUtils.leftPad("8.  Certified that in pursuance to Finance Department Office, memorandum No.31271/F., dated 16th July,1999 no amount", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("has been drawn against abolition of base level posts", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("", 10));

            dmpUtil.writeToFile(StringUtils.leftPad("9.  Certify that no base level vacant post has been filled up without prior approval of the Finance Department", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("", 10));

            dmpUtil.writeToFile(StringUtils.leftPad("10. Certify that the GPF Account nos have been allotted in favour of the employees whose pay have been drawn in", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("this bill A.G., Odisha, has been requested to allot New Account nos vide F.D.L No.19012/F., dated 02-04-2008 in ", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("respect of those employees who have been allotted A/C Nos.", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("", 10));

            dmpUtil.writeToFile(StringUtils.leftPad("11. G.I.S premium has been deposited according to the Basic Pay of all the Employees and a discrepancy if any", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("found later in I shall be held responsible.", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("", 10));

            dmpUtil.writeToFile(StringUtils.leftPad("12. Certified the annual certificate on proper maintenance of Service Books for the preceding financial year has", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("been sent to the controlling officer F.D and D.T.I.,(O),Bhubaneswar vide F.D letter No._ _ _ _ _ _Date:_ _ _ _ _. ", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("", 10));

            dmpUtil.writeToFile(StringUtils.leftPad("13. Certify that all those from whose Income Tax has not been deducted at source from salary are not liable to pay", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("Income Tax during the Financial Year2005-06.", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("    Further certified that the annual certificate of verification of services with local records in respect of all", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("the incumbents whose pay is drawn in this bill has been complited.", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("", 10));

            dmpUtil.writeToFile(StringUtils.leftPad("14. Certified that the certificate prescribed by Govt. have been obtained from the Govt. servents for whom House", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("Rent Allowance has been drawn in this bill has been complited.", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("", 10));

            dmpUtil.writeToFile(StringUtils.leftPad("15. Certified that where no deduction has been made in this bill on account of Subscription to the GPF from any", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("person from whom pay has been drawn in the bill, the application for admission to the GPF scheme sent to A.G vide", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("F.D letter No_ _ _ _ _ _ _ date_ _ _ _ _ _ _ not received back with account number allotted.", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("", 10));
            dmpUtil.writeToFile(StringUtils.leftPad("(DRAWING AND DISBURSING OFFICER)", 95));
            dmpUtil.writeToFile((char) 12);
            dmpUtil.writeToFile((char) 26);
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
      return true;  
    }
}
