package hrms.dao.payroll.services;

import hrms.common.CommonFunctions;
import hrms.common.DMPUtil;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.ItScheduleBean;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class AISGISScheduleServices {

    public static final int characterPerLine = 87;
    public static final int lineperpage = 62;

    public ScheduleDAO comonScheduleDao;

    public void setComonScheduleDao(ScheduleDAO comonScheduleDao) {
        this.comonScheduleDao = comonScheduleDao;
    }

    private void printHeader(DMPUtil dmpUtil, CommonReportParamBean crb, String billdesc, String billdate, int pageNo) throws Exception {
        String officename = crb.getOfficeen();
        dmpUtil.writeToFile(StringUtils.center(officename + "      Page : " + pageNo, characterPerLine, " "));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile((char) 27);
        dmpUtil.writeToFile((char) 69);
        dmpUtil.writeToFile(StringUtils.rightPad("Bill No : " + billdesc + ((char) 27) + ((char) 70), 35));
        dmpUtil.writeToFile("Schedule of recovery of G.I.S. advance of A.I.S. Officers for the ");
        dmpUtil.writeToFile("month of :" + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear());
        dmpUtil.writeToFile("Recovery Schedule towards subscription of G.I.S. Scheme of All India");
        dmpUtil.writeToFile("Service Officers of SCIENCE AND TECHNOLOGY DEPARTMENT as per letter No. S-");
        dmpUtil.writeToFile("11013/2/81/TA/3845 of Ministry of Finance, Government of India read with");
        dmpUtil.writeToFile("General Administration Department Office Memorandum No. 19870/Gen., dated");
        dmpUtil.writeToFile("21.8.82.");
        dmpUtil.writeToFile("Head of Account:    8658-Suspense account 123-A.I.S. Officers Group");
        dmpUtil.writeToFile("                    Insurance scheme - Payment to the Central Government of");
        dmpUtil.writeToFile("                    the subscription in respect of A.I.S. Officers Group");
        dmpUtil.writeToFile("                    Insurance Scheme.");
        dmpUtil.writeToFile("-------------------------------------------------------------------------");
        dmpUtil.writeToFile("Sl    Name & Designation               Amount of         Remarks");
        dmpUtil.writeToFile("No.   of employee                   Subscription");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("--------------------------------------------------------------------------");
        dmpUtil.writeToFile("1         2                                  3              4");
        dmpUtil.writeToFile("-------------------------------------------------------------------------");
    }

    public boolean write(String billno, String folderPath, String fileSeparator, CommonReportParamBean crb) throws Exception {
        
        String fileName = "AISGISSCHEDULE.txt";
        
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
            
            List<ItScheduleBean> cgegislist = comonScheduleDao.getITScheduleEmployeeList(billno, "CGEGIS", crb.getAqmonth(),crb.getAqyear());
            
            for (ItScheduleBean cgegis : cgegislist) {
                if (cgegis.getEmpname() != null && !cgegis.getEmpname().equals("")) {
                    if (createFile == false) {
                        dmpUtil = new DMPUtil(folderPath, fileName);
                        createFile = true;
                    }
                    dataFound = true;
                    adamt = cgegis.getEmpDedutAmount();
                    total = total + Integer.parseInt(adamt);
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
                    dmpUtil.writeToFile(StringUtils.rightPad("" + slno, 5) + StringUtils.rightPad(cgegis.getEmpname(), 35) + StringUtils.leftPad(adamt, 9));
                    dmpUtil.writeToFile(StringUtils.rightPad("", 5) + StringUtils.rightPad(cgegis.getEmpdesg(), characterPerLine));

                    cnt++;
                    if (slno % 10 == 0) {
                        cnt = 0;
                        dmpUtil.writeToFile("");
                        dmpUtil.writeToFile("-------------------------------------------------------------------------");
                        dmpUtil.writeToFile(StringUtils.rightPad("* Page Total * : ", 40) + total);
                        dmpUtil.writeToFile("-------------------------------------------------------------------------");
                        if (total > 0) {
                            dmpUtil.writeToFile(StringUtils.leftPad("", 30) + "In Words(RUPEES " + StringUtils.upperCase(CommonFunctions.convertNumber(total) + ")"));
                            dmpUtil.writeToFile(StringUtils.leftPad("", 30) + "ONLY");
                        }
                        dmpUtil.writeToFile("");
                        dmpUtil.writeToFile("");
                        dmpUtil.writeToFile(StringUtils.rightPad("", 60) + crb.getDdoname());
                    }
                }
            }
            if (createFile == true) {
                if (cnt > 0) {
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("-------------------------------------------------------------------------");
                    dmpUtil.writeToFile("* Grand Total * :                             " + total);
                    dmpUtil.writeToFile("-------------------------------------------------------------------------");
                    if (total > 0) {
                        dmpUtil.writeToFile(StringUtils.leftPad("", 30) + "In Words(RUPEES " + StringUtils.upperCase(CommonFunctions.convertNumber(total) + ")"));
                        dmpUtil.writeToFile(StringUtils.leftPad("", 30) + "ONLY");
                    }
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile(StringUtils.rightPad("", 60) + crb.getDdoname());

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
