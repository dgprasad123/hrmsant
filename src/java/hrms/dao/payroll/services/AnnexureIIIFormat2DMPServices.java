/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.services;

import hrms.common.CommonFunctions;
import hrms.common.DMPUtil;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.BillContributionRepotBean;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class AnnexureIIIFormat2DMPServices {

    public static final int characterPerLine = 87;
    public static final int lineperpage = 62;

    String firstLineBilldesc = "";
    String secondLineBilldesc = "";

    String firstLineEmpName = "";
    String secondLineEmpName = "";

    String firstLinePost = "";
    String secondLinePost = "";

    public ScheduleDAO comonScheduleDao;

    public void setComonScheduleDao(ScheduleDAO comonScheduleDao) {
        this.comonScheduleDao = comonScheduleDao;
    }

    private void printHeader(DMPUtil dmpUtil, CommonReportParamBean crb, String billdesc, String billdate, int pageNo) throws Exception {

        String dtoregno = "";
        if (crb.getDdoregno() != null && !crb.getDdoregno().equals("")) {
            dtoregno = "/" + crb.getDdoregno();
        } else {
            dtoregno = "";
        }
        dmpUtil.writeToFile(StringUtils.rightPad("", 40) + "Page No:" + pageNo);
        dmpUtil.writeToFile(StringUtils.center("ANNEXURE- III ", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("FORMAT IN WHICH INFORMATION ON CONTRIBUTION IS REQUIRED TO BE SENT BY TREASURY OFFICER", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("TO THE TRUSTEE BANK,BANK OF INDIA", characterPerLine, " "));
        //dmpUtil.writeToFile(StringUtils.center("(TO BE ATTACHED WITH THE PAY BILL FOR DRAWAL OF GOVT. CONTRIBUTION)",characterPerLine," "));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile((char) 27);
        dmpUtil.writeToFile((char) 69);
        dmpUtil.writeToFile(StringUtils.rightPad("Bill NO/DATE : " + billdesc + "/" + billdate, 50) + StringUtils.rightPad("MONTH/YEAR:" + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), 50));
        //dmpUtil.writeToFile(StringUtils.center("MONTH/YEAR:"+BBScheduler.getMonthAsString(crb.getAqmonth())+"-"+crb.getAqyear(),characterPerLine," "));
        dmpUtil.writeToFile((char) 27);
        dmpUtil.writeToFile((char) 70);
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.rightPad("NAME OF THE DDO/REGISTRATION NO:" + crb.getDdoname(), characterPerLine, " "));
        dmpUtil.writeToFile((char) 27);
        dmpUtil.writeToFile((char) 69);
        dmpUtil.writeToFile("");
        String deptname = crb.getDeptname();
        dmpUtil.writeToFile(StringUtils.rightPad("NAME OF OFFICE AND ADDRESS:" + deptname, 45));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.rightPad("DTO REGISTRATION NO:" + dtoregno, 50));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
        dmpUtil.writeToFile("SL  |  TREASURY OFFICER REGD ");
        dmpUtil.writeToFile("        NO./NAME OF THE GOVT.                |BASIC   | AMOUNT OF|AMOUNT OF|TOTAL");
        dmpUtil.writeToFile("NO  |  SERVANT/DESIGNATION                   |+GP     | EMPLOYEES|GOVT.    | IN");
        dmpUtil.writeToFile("    |  (IN BLOCK LETTER)                     |+DA     | CONTRI-  |CONTRI-  | RS");
        dmpUtil.writeToFile("    |   PRAN                                 |RS      | BUTION   |BUTION   |   ");
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
        dmpUtil.writeToFile("1              2                   3         4         5         6   ");
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
    }

    public boolean write(String billno, String folderPath, String fileSeparator, CommonReportParamBean crb) throws Exception {

        String fileName = "ANNEXURE3(II).txt";

        int tot1 = 0;
        int pageNo = 0;
        int slno = 0;
        int gross = 0;
        String adamt = "";
        int cnt = 0;
        int total = 0;
        int tot = 0;
        int arrearinst = 0;
        int arrearamt = 0;
        int totcontribution = 0;
        int govtcontribution = 0;

        boolean dataFound = false;
        String total1 = "";
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

            List<BillContributionRepotBean> empList = comonScheduleDao.getBillContributionRepotScheduleEmpList("annexure3", billno, year, month);

            for (BillContributionRepotBean billbean : empList) {

                if (createFile == false) {
                    dmpUtil = new DMPUtil(folderPath, fileName);
                    createFile = true;
                }
                if (billbean.getEmpname() != null && !billbean.getEmpname().equals("")) {
                    dataFound = true;
                    String namelines[] = wrapText(billbean.getEmpname(), 26);
                    if (namelines != null) {
                        if (namelines.length > 1) {
                            firstLineEmpName = namelines[0];
                            secondLineEmpName = namelines[1];

                        } else {
                            firstLineEmpName = namelines[0];
                        }

                    }
                }
                String designation = billbean.getEmpdesg();
                if (billbean.getEmpname() != null && !billbean.getEmpname().equals("")) {
                    dataFound = true;
                    if (slno == 0 || slno % 7 == 0) {
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
                    //annexbean = getAnnexureAmt(rs.getString("AQSL_NO"), con);
                    slno++;
                    int cpf = 0;
                    if(billbean.getEmpCpf() != null && !billbean.getEmpCpf().equals("")){
                        Double cpfDouble = Double.parseDouble(billbean.getEmpCpf());
                        cpf = new Double(cpfDouble).intValue();
                    }
                    if(billbean.getArrearAmt() != null && !billbean.getArrearAmt().equals("")){
                        arrearamt = Integer.parseInt(billbean.getArrearAmt());
                    }
                    totcontribution = cpf + arrearamt;
                    govtcontribution = totcontribution;
                    total = cpf + cpf;
                    tot = tot + cpf;

                    //tot1=tot*2;
                    tot1 = tot1 + (totcontribution + govtcontribution);
                    total1 = CommonFunctions.convertNumber(tot1).toUpperCase();
                    dmpUtil.writeToFile("");
                    if (secondLineEmpName != null && !secondLineEmpName.equals("")) {
                        dmpUtil.writeToFile(StringUtils.rightPad("" + slno, 5) + StringUtils.rightPad(firstLineEmpName, 41) + StringUtils.rightPad((billbean.getEmpDearnespay() + billbean.getEmpGradepay()) + "", 10) + StringUtils.rightPad(totcontribution + "", 10) + StringUtils.rightPad(govtcontribution + "", 10) + StringUtils.leftPad("" + (totcontribution + govtcontribution), 5));
                        dmpUtil.writeToFile(StringUtils.rightPad("", 5) + StringUtils.rightPad(secondLineEmpName, 26));
                        dmpUtil.writeToFile(StringUtils.rightPad("", 5) + designation);
                        dmpUtil.writeToFile(StringUtils.rightPad("", 5) + billbean.getGpfNo());
                        secondLineEmpName = "";
                    } else {
                        dmpUtil.writeToFile(StringUtils.rightPad("" + slno, 5) + StringUtils.rightPad(firstLineEmpName, 41) + StringUtils.rightPad((billbean.getEmpDearnespay() + billbean.getEmpGradepay()) + "", 10) + StringUtils.rightPad(totcontribution + "", 10) + StringUtils.rightPad(govtcontribution + "", 10) + StringUtils.leftPad("" + (totcontribution + govtcontribution), 5));
                        dmpUtil.writeToFile(StringUtils.rightPad("", 5) + designation);
                        dmpUtil.writeToFile(StringUtils.rightPad("", 5) + billbean.getGpfNo());
                    }

                    cnt++;
                    if (slno % 7 == 0) {
                        cnt = 0;
                        dmpUtil.writeToFile("");
                        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
                        dmpUtil.writeToFile(StringUtils.rightPad("Grand Total:", 12) + StringUtils.rightPad(" ", 30) + StringUtils.leftPad("" + tot, 12) + StringUtils.rightPad(" ", 11) + StringUtils.leftPad("" + tot, 10) + StringUtils.rightPad(" ", 1) + StringUtils.leftPad("" + tot1, 8));
                        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
                        if (total > 0) {
                            dmpUtil.writeToFile(StringUtils.leftPad("IN WORDS(RUPEES " + total1, 72) + ") ONLY");
                        }
                        dmpUtil.writeToFile("");
                        dmpUtil.writeToFile("");
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
                    dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
                    dmpUtil.writeToFile(StringUtils.rightPad("Grand Total:", 73) + StringUtils.leftPad("" + tot1, 8));
                    dmpUtil.writeToFile("---------------------------------------------------------------------------------------");

                    if (tot > 0) {
                        dmpUtil.writeToFile(StringUtils.leftPad("IN WORDS(RUPEES " + total1, 72) + ") ONLY");

                    }
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile(StringUtils.rightPad("", 45) + "Signature of Treasury officer");
                    dmpUtil.writeToFile(StringUtils.rightPad("", 45) + "(With Seal)");
                    dmpUtil.writeToFile("");
                }
                dmpUtil.writeToFile((char) 12);
                dmpUtil.writeToFile((char) 26);
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
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

            if (chars[i] == '/') {
                if ((line.length() + word.length()) > len) {
                    lines.add(line.toString());
                    line.delete(0, line.length());
                }

                line.append(word);
                word.delete(0, word.length());
            }
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
