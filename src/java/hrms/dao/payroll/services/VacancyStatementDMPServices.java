package hrms.dao.payroll.services;

import hrms.common.DMPUtil;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.VacancyStatementScheduleBean;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class VacancyStatementDMPServices {

    public static final int characterPerLine = 87;
    public static final int lineperpage = 62;

    String firstLineDesg = "";
    String secondLineDesg = "";

    public ScheduleDAO comonScheduleDao;

    public void setComonScheduleDao(ScheduleDAO comonScheduleDao) {
        this.comonScheduleDao = comonScheduleDao;
    }

    private void printHeader(DMPUtil dmpUtil, CommonReportParamBean crb, String billdesc, String billdate, int pageNo) throws Exception {

        String deptname = crb.getOfficeen();
        dmpUtil.writeToFile(StringUtils.center(deptname + "         Page : " + pageNo, characterPerLine, " "));

        dmpUtil.writeToFile(StringUtils.center("Form No. O.T.C. 23", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("ABSENTEE STATEMENT", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("[ See Subsidiary Rule 223 ]", characterPerLine, " "));
        dmpUtil.writeToFile((char) 27);
        dmpUtil.writeToFile((char) 69);
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.rightPad("Bill No : " + StringUtils.defaultString(billdesc) + ((char) 27) + ((char) 70), 35));
        dmpUtil.writeToFile("------------------------------------------------------------------------------------");
        dmpUtil.writeToFile("SlNo.  Designation                                 SCALE OF PAY         NO. OF POST ");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------");
        dmpUtil.writeToFile(" 1         2                                           3                    4       ");
        dmpUtil.writeToFile("------------------------------------------------------------------------------------");
    }

    public boolean write(String billno, String folderPath, String fileSeparator, CommonReportParamBean crb) throws Exception {

        boolean dataFound = false;
        boolean createFile = false;

        String fileName = "VACANCYSTMT.txt";
        int pageNo = 0;

        int year = 0;
        int month = 0;
        String billdesc = "";
        String billdate = "";

        int slno = 0;
        int cnt = 0;
        int total = 0;

        try {

            year = crb.getAqyear();
            month = crb.getAqmonth();
            billdesc = crb.getBilldesc();
            folderPath = folderPath + fileSeparator;
            billdate = crb.getBilldate();

            DMPUtil dmpUtil = null;

            List<VacancyStatementScheduleBean> emplist = comonScheduleDao.getVacancyStmtScheduleEmpList(billno);

            for (VacancyStatementScheduleBean vstmtBean : emplist) {

                if (createFile == false) {
                    dmpUtil = new DMPUtil(folderPath, fileName);
                    createFile = true;
                }
                if (vstmtBean.getDesignation() != null && !vstmtBean.getDesignation().equals("")) {
                    dataFound = true;
                    String lines[] = wrapText(vstmtBean.getDesignation(), 39);
                    if (lines != null) {
                        if (lines.length > 1) {
                            firstLineDesg = lines[0];
                            secondLineDesg = lines[1];
                        } else {
                            firstLineDesg = lines[0];
                        }

                    }
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
                    dmpUtil.writeToFile(StringUtils.rightPad(" " + slno, 7) + StringUtils.rightPad(StringUtils.defaultString(firstLineDesg), 44) + StringUtils.rightPad(StringUtils.defaultString(vstmtBean.getPayscale()), 25) + StringUtils.defaultString(vstmtBean.getPostno()+""));
                    total = total + vstmtBean.getPostno();
                    cnt++;

                    if (secondLineDesg != null && !secondLineDesg.equals("")) {
                        dmpUtil.writeToFile(StringUtils.rightPad("", 7) + StringUtils.rightPad(secondLineDesg, 44));
                        secondLineDesg = "";
                    }

                    if (slno % 10 == 0) {
                        cnt = 0;
                        dmpUtil.writeToFile("");
                        dmpUtil.writeToFile("------------------------------------------------------------------------------------");
                        dmpUtil.writeToFile(StringUtils.rightPad(" *  Total * :", 76) + StringUtils.defaultString("" + total));
                        dmpUtil.writeToFile("------------------------------------------------------------------------------------");

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
                    dmpUtil.writeToFile("------------------------------------------------------------------------------------");
                    dmpUtil.writeToFile(StringUtils.rightPad(" *  Total * :", 76) + StringUtils.defaultString("" + total));
                    dmpUtil.writeToFile("------------------------------------------------------------------------------------");

                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile(StringUtils.rightPad("", 60) + crb.getDdoname());
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

    private String[] wrapText(String text, int len) {
        // return empty array for null text
        if (text == null) {
            return new String[]{};
        }

        // return text if len is zero or less
        if (len <= 0) {
            return new String[]{text};
        }

        // return text if less than length
        if (text.length() <= len) {
            return new String[]{text};
        }

        char[] chars = text.toCharArray();
        Vector lines = new Vector();
        StringBuffer line = new StringBuffer();
        StringBuffer word = new StringBuffer();

        for (int i = 0; i < chars.length; i++) {
            word.append(chars[i]);

            if (chars[i] == ' ') {
                if ((line.length() + word.length()) > len) {
                    lines.add(line.toString());
                    line.delete(0, line.length());
                }

                line.append(word);
                word.delete(0, word.length());
            }
        }

        // handle any extra chars in current word
        if (word.length() > 0) {
            if ((line.length() + word.length()) > len) {
                lines.add(line.toString());
                line.delete(0, line.length());
            }
            line.append(word);
        }

        // handle extra line
        if (line.length() > 0) {
            lines.add(line.toString());
        }

        String[] ret = new String[lines.size()];
        int c = 0; // counter
        for (Enumeration e = lines.elements(); e.hasMoreElements(); c++) {
            ret[c] = (String) e.nextElement();
        }

        return ret;
    }
}
