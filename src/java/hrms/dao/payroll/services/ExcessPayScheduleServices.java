package hrms.dao.payroll.services;

import hrms.common.CommonFunctions;
import hrms.common.DMPUtil;
import hrms.common.DataBaseFunctions;
import hrms.common.Numtowordconvertion;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.ExcessPayBean;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ExcessPayScheduleServices {

    public static final int characterPerLine = 87;
    public static final int lineperpage = 62;

    public ScheduleDAO comonScheduleDao;

    public void setComonScheduleDao(ScheduleDAO comonScheduleDao) {
        this.comonScheduleDao = comonScheduleDao;
    }

    private void printHeader(DMPUtil dmpUtil, CommonReportParamBean crb, String billdesc, String billdate, int pageNo) throws Exception {

        dmpUtil.writeToFile(StringUtils.leftPad("Page :" + pageNo, 50));
        dmpUtil.writeToFile(StringUtils.center("EXCESS PAY RECOVERY ", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("FOR THE MONTH OF :   " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), characterPerLine, " "));

        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        String deptname = crb.getDeptname();
        String offname = crb.getOfficeen();
        dmpUtil.writeToFile("Name of the Department: " + StringUtils.defaultString(deptname));
        dmpUtil.writeToFile("Name of the Office: " + StringUtils.defaultString(offname));
        dmpUtil.writeToFile("Designation of DDO: " + StringUtils.defaultString(crb.getDdoname()));
        dmpUtil.writeToFile("Bill No: " + StringUtils.defaultString(billdesc));

        //dmpUtil.writeToFile((char)27);
        //dmpUtil.writeToFile((char)69);
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------");
        dmpUtil.writeToFile("Sl   Name & Designation                  Gross         Excess             Remarks");
        dmpUtil.writeToFile("No.       of employee                    Salary        Pay   ");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------");
        dmpUtil.writeToFile(" 1         2                                3             4                  5 ");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------");
    }

    public boolean write(String billno, String folderPath, String fileSeparator, CommonReportParamBean crb) throws Exception {

        String fileName = "EXCESSPAYSCHEDULE.txt";

        int year = 0;
        int month = 0;
        String billdesc = "";
        String billdate = "";

        boolean dataFound = false;
        boolean createFile = false;

        int pageNo = 0;
        int basic = 0;
        int slno = 0;
        int total = 0;
        int gross = 0;
        int grosstotal = 0;
        String adamt = "";
        int cnt = 0;
        try {
            year = crb.getAqyear();
            month = crb.getAqmonth();
            billdesc = crb.getBilldesc();
            folderPath = folderPath + fileSeparator;
            billdate = crb.getBilldate();

            DMPUtil dmpUtil = null;
            
            List<ExcessPayBean> empList = comonScheduleDao.getExcessPayScheduleEmpDetails(billno, year, month);
            
            for(ExcessPayBean epb : empList){
                
                if(epb.getEmpName() != null && !epb.getEmpName().equals("")){
                    if (createFile == false) {
                        dmpUtil = new DMPUtil(folderPath, fileName);
                        createFile = true;
                    }
                    dataFound = true;
                    grosstotal = Integer.parseInt(epb.getTotalGross());
                    total = Integer.parseInt(epb.getTotalTax());
                    //gross = comonScheduleDao.getBasicAmount(billno) + comonScheduleDao.getAllowanceAndDeductionAmount(billno, "A", month, year) + comonScheduleDao.getAllowanceAndDeductionAmount(billno, "D", month, year);
                    
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
                    dmpUtil.writeToFile(StringUtils.rightPad("" + slno, 6) + StringUtils.rightPad(epb.getEmpName(), 35) + StringUtils.rightPad(gross + "", 16) + epb.getEmpTaxOnProffesion());
                    dmpUtil.writeToFile(StringUtils.rightPad("", 6) + StringUtils.rightPad(epb.getEmpDegn(), characterPerLine));
                    cnt++;
                    if (slno % 10 == 0) {
                        cnt = 0;
                        dmpUtil.writeToFile("");
                        dmpUtil.writeToFile("------------------------------------------------------------------------------------");
                        dmpUtil.writeToFile(StringUtils.rightPad("Total for the Month of :" + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), 41) + StringUtils.rightPad(grosstotal + "", 16) + total);
                        dmpUtil.writeToFile("------------------------------------------------------------------------------------");
                        if (total > 0) {
                            dmpUtil.writeToFile(StringUtils.leftPad("In Words(RUPEES " + StringUtils.upperCase(Numtowordconvertion.convertNumber(total) + ")"), characterPerLine));
                            dmpUtil.writeToFile(StringUtils.leftPad("ONLY", characterPerLine));
                        }
                        
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
                    dmpUtil.writeToFile(StringUtils.rightPad("Total for the Month of :" + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), 41) + StringUtils.rightPad(grosstotal + "", 16) + total);
                    dmpUtil.writeToFile("------------------------------------------------------------------------------------");
                    if (total > 0) {
                        dmpUtil.writeToFile(StringUtils.leftPad("", 30) + "In Words(RUPEES " + StringUtils.upperCase(Numtowordconvertion.convertNumber(total) + ")"));
                        dmpUtil.writeToFile(StringUtils.leftPad("", 30) + "ONLY");
                    }
                }
                dmpUtil.writeToFile("");
                dmpUtil.writeToFile("------------------------------------------------------------------------------------");
                dmpUtil.writeToFile("");
                dmpUtil.writeToFile("Signature of D.D.O");
                dmpUtil.writeToFile(StringUtils.defaultString(crb.getDeptname()));
                dmpUtil.writeToFile("");
                dmpUtil.writeToFile("Date: ");
                dmpUtil.writeToFile("");
                dmpUtil.writeToFile("To be filled by the Treasury Officer/Sub Treasury/Special Treasury Officer");
                dmpUtil.writeToFile("a.    T.V. No __________________ and Date __________________ of encashment of Bill");
                dmpUtil.writeToFile("b.    Sl. No. __________________ and Date __________________ of the receipt Schedule in which accounted");
                dmpUtil.writeToFile("by Transfer Credit");

                dmpUtil.writeToFile((char) 12);
                dmpUtil.writeToFile((char) 26);
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
        }
      return dataFound;
    }
}
