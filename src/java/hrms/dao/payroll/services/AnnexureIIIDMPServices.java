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
public class AnnexureIIIDMPServices {

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

        String ddoregno = "";
        if (crb.getDdoregno() != null && !crb.getDdoregno().equals("")) {
            ddoregno = "/" + crb.getDdoregno();
        } else {
            ddoregno = "";
        }
        dmpUtil.writeToFile(StringUtils.rightPad("", 40) + "Page No:" + pageNo);
        dmpUtil.writeToFile(StringUtils.center("ANNEXURE- III ", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("FORMAT OF SCHEDULE FOR EMPLOYEES AND EMPLOYER CONTRIBUTIONS", characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("TOWARDS TIER-I OF THE NEW PENSION SCHEME", characterPerLine, " "));
        //dmpUtil.writeToFile(StringUtils.center("(TO BE ATTACHED WITH THE PAY BILL FOR DRAWAL OF GOVT. CONTRIBUTION)",characterPerLine," "));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile((char) 27);
        dmpUtil.writeToFile((char) 69);
        dmpUtil.writeToFile(StringUtils.rightPad("Bill NO/DATE : " + billdesc + "/" + billdate, 50) + StringUtils.rightPad("MONTH/YEAR:" + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), 50));
        //dmpUtil.writeToFile(StringUtils.center("MONTH/YEAR:"+BBScheduler.getMonthAsString(crb.getAqmonth())+"-"+crb.getAqyear(),characterPerLine," "));
        dmpUtil.writeToFile((char) 27);
        dmpUtil.writeToFile((char) 70);
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.rightPad("NAME OF THE DDO/REGISTRATION NO:" + crb.getDdoname() + ddoregno, characterPerLine, " "));
        dmpUtil.writeToFile((char) 27);
        dmpUtil.writeToFile((char) 69);
        dmpUtil.writeToFile("");
        String offname = crb.getOfficename();
        dmpUtil.writeToFile(StringUtils.rightPad("NAME OF OFFICE AND ADDRESS:" + offname, 45));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.rightPad("DTO REGISTRATION NO:" + crb.getDtoregno(), 50));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
        dmpUtil.writeToFile("SL  |  NAME OF THE GOVT.      |BILL NO     | AMOUNT OF|BILL NO     |AMOUNT OF| TOTAL");
        dmpUtil.writeToFile("NO  |  SERVANT/DESIGNATION    |AND DATE    | EMPLOYEES|AND DATE    |GOVT.    | IN");
        dmpUtil.writeToFile("    |  (IN BLOCK LETTER)      |OF EMPLOYEES| CONTRI-  |OF GOVT.    |CONTRI-  | RS");
        dmpUtil.writeToFile("    |   PRAN                  |CONTRIBUTION| BUTION   |CONTRIBUTION|BUTION   |   ");
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
        dmpUtil.writeToFile("            1                      2            3         4            5        6   ");
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");

    }

    public boolean write(String billno, String folderPath, String fileSeparator, CommonReportParamBean crb) throws Exception {

        String fileName = "ANNEXURE3.txt";

        String total1 = "";
        int pageNo = 0;
        int slno = 0;
        int basic = 0;
        String adamt = "";
        int cnt = 0;
        int total = 0;
        int tot = 0;
        int tot1 = 0;
        String arrearinst = "";
        int arrearamt = 0;
        int totarrearamt = 0;
        int totcontribution = 0;
        int govtcontribution = 0;
        int totempcontribution = 0;

        int gtotcont = 0;

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

            List<BillContributionRepotBean> empList = comonScheduleDao.getBillContributionRepotScheduleEmpList("annexure3", billno, year, month);

            for (BillContributionRepotBean billbean : empList) {

                if (createFile == false) {
                    dmpUtil = new DMPUtil(folderPath, fileName);
                    createFile = true;
                }
                if (billbean.getEmpname() != null && !billbean.getEmpname().equals("")) {
                    String namelines[] = wrapText(billbean.getEmpname(), 35);
                    if (namelines != null) {
                        if (namelines.length > 1) {
                            firstLineEmpName = namelines[0];
                            secondLineEmpName = namelines[1];

                        } else {
                            firstLineEmpName = namelines[0];
                        }

                    }
                }
                if (billbean.getEmpdesg() != null && !billbean.getEmpdesg().equals("")) {
                    String desglines[] = wrapText(billbean.getEmpdesg(), 35);
                    if (desglines != null) {
                        if (desglines.length > 1) {
                            firstLinePost = desglines[0];
                            secondLinePost = desglines[1];
                        } else {
                            firstLinePost = desglines[0];
                        }

                    }
                }

                if (billbean.getEmpBasicSal() != null && !billbean.getEmpBasicSal().equals("")) {
                    basic = Integer.parseInt(billbean.getEmpBasicSal());
                }
                int dp = 0;
                if(billbean.getEmpDearnespay() != null && !billbean.getEmpDearnespay().equals("")){
                    dp = Integer.parseInt(billbean.getEmpDearnespay());
                }
                int gp = 0;
                if(billbean.getEmpGradepay() != null && !billbean.getEmpGradepay().equals("")){
                    gp = Integer.parseInt(billbean.getEmpGradepay());
                }
                total = basic + dp + gp;

                int cpf = 0;
                if(billbean.getEmpCpf() != null && !billbean.getEmpCpf().equals("")){
                    Double cpfDouble = Double.parseDouble(billbean.getEmpCpf());
                    cpf = new Double(cpfDouble).intValue();
                }
                if(billbean.getArrearAmt() != null && !billbean.getArrearAmt().equals("")){
                    arrearamt = Integer.parseInt(billbean.getArrearAmt());
                }
                totarrearamt = totarrearamt + arrearamt;
                arrearinst = billbean.getArrInstalment();

                totcontribution = cpf + arrearamt;
                govtcontribution = totcontribution;
                totempcontribution = totempcontribution + totcontribution;
                gtotcont = gtotcont + govtcontribution;
                total = cpf + totcontribution;
                tot = tot + cpf;
                //tot1=tot1+total;
                tot1 = tot1 + (totcontribution + govtcontribution);
                total1 = CommonFunctions.convertNumber(tot1).toUpperCase();
                if (total > 0) {
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
                        slno++;
                        dmpUtil.writeToFile("");
                        if (secondLineEmpName != null && !secondLineEmpName.equals("")) {
                            dmpUtil.writeToFile(StringUtils.rightPad("" + slno, 5) + StringUtils.rightPad(firstLineEmpName, 26) + StringUtils.rightPad(firstLineBilldesc, 12) + StringUtils.leftPad(totcontribution + "", 10) + StringUtils.rightPad("", 2) + StringUtils.rightPad(firstLineBilldesc, 10) + StringUtils.leftPad(govtcontribution + "", 8) + StringUtils.leftPad("" + (totcontribution + govtcontribution), 9));
                            dmpUtil.writeToFile(StringUtils.rightPad("", 5) + StringUtils.rightPad(secondLineEmpName, 26));
                            secondLineEmpName = "";
                        } else {
                            dmpUtil.writeToFile(StringUtils.rightPad("" + slno, 5) + StringUtils.rightPad(firstLineEmpName, 26) + StringUtils.rightPad(firstLineBilldesc, 12) + StringUtils.leftPad(totcontribution + "", 10) + StringUtils.rightPad("", 2) + StringUtils.rightPad(firstLineBilldesc, 10) + StringUtils.leftPad(govtcontribution + "", 8) + StringUtils.leftPad("" + (totcontribution + govtcontribution), 9));
                        }

                        if (secondLineBilldesc != null && !secondLineBilldesc.equals("") && !secondLinePost.equals("")) {
                            dmpUtil.writeToFile(StringUtils.rightPad("", 5) + StringUtils.rightPad(firstLinePost, 26) + StringUtils.rightPad(secondLineBilldesc, 24) + StringUtils.rightPad(secondLineBilldesc, 10));
                            dmpUtil.writeToFile(StringUtils.rightPad("", 5) + StringUtils.rightPad(secondLinePost, 26));
                            dmpUtil.writeToFile(StringUtils.rightPad("", 5) + StringUtils.rightPad(billbean.getGpfNo(), 26) + StringUtils.rightPad(billdate, 24) + StringUtils.rightPad(billdate, 10));
                            secondLinePost = "";
                            firstLinePost = "";
                        } else if (secondLineBilldesc != null && !secondLineBilldesc.equals("") && secondLinePost.equals("")) {
                            dmpUtil.writeToFile(StringUtils.rightPad("", 5) + StringUtils.rightPad(firstLinePost, 26) + StringUtils.rightPad(secondLineBilldesc, 24) + StringUtils.rightPad(secondLineBilldesc, 10));
                            dmpUtil.writeToFile(StringUtils.rightPad("", 5) + StringUtils.rightPad(billbean.getGpfNo(), 26) + StringUtils.rightPad(billdate, 24) + StringUtils.rightPad(billdate, 15));
                        }

                        cnt++;
                        if (slno % 7 == 0) {
                            cnt = 0;
                            dmpUtil.writeToFile("");
                            dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
                            dmpUtil.writeToFile(StringUtils.rightPad("Grand Total:", 12) + StringUtils.rightPad(" ", 30) + StringUtils.leftPad("" + totempcontribution, 12) + StringUtils.rightPad(" ", 11) + StringUtils.leftPad("" + gtotcont, 10) + StringUtils.rightPad(" ", 1) + StringUtils.leftPad("" + tot1, 8));
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
            }
            if (createFile == true) {
                if (cnt > 0) {
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
                    dmpUtil.writeToFile(StringUtils.rightPad("Grand Total:",12) + StringUtils.rightPad(" ", 30) + StringUtils.leftPad("" + totempcontribution,12) + StringUtils.rightPad(" ", 11) + StringUtils.leftPad("" + gtotcont,10)
                            + StringUtils.rightPad(" ", 1)
                            + StringUtils.leftPad("" + tot1, 8));
                    dmpUtil.writeToFile("---------------------------------------------------------------------------------------");

                    if (tot > 0) {
                        dmpUtil.writeToFile(StringUtils.leftPad("IN WORDS(RUPEES " + total1, 75)+ ") ONLY");

                    }
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile(StringUtils.rightPad("The Basic pay entered in the column 5 of the above statement  ",characterPerLine," "));
                    dmpUtil.writeToFile(StringUtils.rightPad("has been verified with the entries made in the Service Book Pay Bill.",characterPerLine," "));
                    dmpUtil.writeToFile(StringUtils.rightPad("(Rupees ............................................................",characterPerLine," "));
                    dmpUtil.writeToFile(StringUtils.rightPad("..............................)",characterPerLine," "));
                    dmpUtil.writeToFile(StringUtils.rightPad("This is to certify that the employees mentioned above is/are ",characterPerLine," "));
                    dmpUtil.writeToFile(StringUtils.rightPad("appointed in a pensionable establishment request",characterPerLine," "));
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile(StringUtils.rightPad("", 45) + "Signature");
                    dmpUtil.writeToFile(StringUtils.rightPad("", 35) + "Drawing and Disbursing officer");
                    dmpUtil.writeToFile(StringUtils.rightPad("", 35) + "With the Designation and date");
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
