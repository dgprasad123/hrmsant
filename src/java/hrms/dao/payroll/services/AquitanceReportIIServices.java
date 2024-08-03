/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.services;

import hrms.common.AqFunctionalities;
import hrms.common.CommonFunctions;
import hrms.common.DMPUtil;
import hrms.common.Numtowordconvertion;
import hrms.dao.payroll.schedule.BillFrontpageDAO;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.billbrowser.BillConfigObj;
import hrms.model.payroll.schedule.ADDetailsHealperBean;
import hrms.model.payroll.schedule.AqreportHelperBean;
import hrms.model.payroll.schedule.Schedule;
import hrms.model.payroll.schedule.SectionWiseAqBean;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class AquitanceReportIIServices {

    public PayBillDMPDAO paybillDmpDao;

    public BillFrontpageDAO billFrontPageDmpDao;

    public void setPaybillDmpDao(PayBillDMPDAO paybillDmpDao) {
        this.paybillDmpDao = paybillDmpDao;
    }

    public void setBillFrontPageDmpDao(BillFrontpageDAO billFrontPageDmpDao) {
        this.billFrontPageDmpDao = billFrontPageDmpDao;
    }

    int characterPerLine = 215;
    String secondLine = "";
    String secondLineDesg = "";
    String secondLineOrdno = "";
    int totdedLine = 0;

    private String printName(String fullname) {
        String nameLength[] = wrapText(fullname, 20);
        if (nameLength.length > 1) {
            secondLine = nameLength[1];
            secondLine = StringUtils.rightPad(secondLine, 21, " ");
            fullname = nameLength[0];
        } else {
            fullname = nameLength[0];
        }
        return StringUtils.rightPad(fullname, 20, " ");

    }

    private String printOrdno(String ordno) {
        //String ordnotemp[] = wrapText(ordno,20);
        String tempOrdno[] = wrapText(ordno, 18);
        if (tempOrdno.length > 1) {
            if (ordno.length() > 17) {
                ordno = tempOrdno[0];
                secondLineOrdno = tempOrdno[1];
            } else {
                ordno = tempOrdno[0];;
            }
        }
        return StringUtils.rightPad(ordno, 21);
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

    private String printDesignation(String designation) {
        secondLineDesg = "";
        //designation="";
        String desiglength[] = wrapText(designation, 32);
        if (desiglength.length > 1) {
            secondLineDesg = desiglength[1];
            designation = desiglength[0];
        } else {
            designation = desiglength[0];
        }

        return StringUtils.rightPad(designation, 33, " ");
    }

    private void printHeader(DMPUtil dmpUtil, CommonReportParamBean crb, String billdesc, String billdate, int pageNo, BillConfigObj billConfig) throws Exception {

        String col7Desc[] = null;
        String col6Desc[] = null;
        col6Desc = billConfig.getCol6List();
        col7Desc = billConfig.getCol7List();

        String offname = crb.getOfficeen();
        String deptname = crb.getDeptname();
        String distname = crb.getDistrict();
        String statename = crb.getStatename();
        int length = StringUtils.defaultString(billdesc).length() + 32;
        dmpUtil.writeToFile(StringUtils.repeat(" ", 83) + StringUtils.rightPad("SCHEDULE-A STATE HEAD QUARTERS FORM NO-58", 41) + StringUtils.leftPad(" " + ((char) 27) + ((char) 69) + "BILL NO : " + StringUtils.defaultString(billdesc) + ((char) 27) + ((char) 70) + " BILL DT : " + billdate, characterPerLine - 133));
        dmpUtil.writeToFile(StringUtils.center("PAY BILL FOR " + ((char) 27) + ((char) 69) + StringUtils.upperCase(offname) + ((char) 27) + ((char) 70), characterPerLine, " "));
        dmpUtil.writeToFile(StringUtils.center("MONTHLY PAY BILL FOR " + ((char) 27) + ((char) 69) + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear() + ((char) 27) + ((char) 70), characterPerLine, " "));
        dmpUtil.writeToFile(" STATE - " + StringUtils.rightPad(StringUtils.defaultString(statename), 32) + "          DISTRICT- " + StringUtils.rightPad(StringUtils.defaultString(distname), 30) + StringUtils.rightPad("", characterPerLine - 99) + "PAGE : " + pageNo);
        if (col7Desc != null && col6Desc != null) {
            dmpUtil.writeToFile("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            dmpUtil.writeToFile(" SL  NAME/               BASIC      DP    DA  " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[0]), 8) + StringUtils.rightPad(StringUtils.defaultString(col7Desc[0]), 6) + " GROSS     LIC/ GPF/EPF/CPF  P.TAX  QTR DEDN           HBAP       MCAP       CAR ADV    PAY ADV    FEST    OTHER     TOTAL    PVTDED      REMARKS");
            dmpUtil.writeToFile("     ORD.NO/DT         SPL.PAY            IR  " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[1]), 8) + StringUtils.rightPad(StringUtils.defaultString(col7Desc[1]), 6) + "            PLI GPF RECOVER  I.TAX WATER TAX        INT HBA      INT MCA     INT CAR    MED ADV    TA ADV  RECOVERY   DEDN    BANK LOAN    A/C NO");
            dmpUtil.writeToFile("     DESG/                  GP                " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[2]), 8) + StringUtils.rightPad(StringUtils.defaultString(col7Desc[2]), 6) + "                       GPDD        & SWR TAX       SPL.HBAP      MOP ADVP     BI CYCL   TRADE ADV  NPS ARR GIS ADV   NET-PAY  NET BALANCE");
            dmpUtil.writeToFile("     PAY SCALE                                " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[3]), 8) + StringUtils.rightPad(StringUtils.defaultString(col7Desc[3]), 6) + "                       GPIR         HIRE CHG    INT SPL HBA      INT MOPA     INT CYCL             E.P     AIS GIS");
            dmpUtil.writeToFile("     GPF NO/PRAN                              " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[4]), 8) + StringUtils.rightPad(StringUtils.defaultString(col7Desc[4]), 6) + "                                                                                                   AUDR    COMP. ADV");
            dmpUtil.writeToFile("                                              " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[5]), 8) + StringUtils.rightPad(StringUtils.defaultString(col7Desc[5]), 6) + "                                                                                      ");
            dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            dmpUtil.writeToFile("(1) (2)                    (3)     (4)   (5)   (6)   (7)      (8)      (9)       (10)    (11)         (12)         (13)         (14)     (15)        (16)      (17)    (18)       (19)       (20)       (21)");
            dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        } else if (col7Desc != null) {
            dmpUtil.writeToFile("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            dmpUtil.writeToFile(" SL  NAME/               BASIC      DP    DA   HRA  " + StringUtils.rightPad(StringUtils.defaultString(col7Desc[0]), 8) + " GROSS     LIC/ GPF/EPF/CPF  P.TAX  QTR DEDN           HBAP       MCAP       CAR ADV    PAY ADV    FEST    OTHER     TOTAL    PVTDED      REMARKS");
            dmpUtil.writeToFile("     ORD.NO/DT         SPL.PAY            IR        " + StringUtils.rightPad(StringUtils.defaultString(col7Desc[1]), 8) + "           PLI GPF RECOVER  I.TAX WATER TAX        INT HBA      INT MCA     INT CAR    MED ADV    TA ADV   RECOVERY   DEDN    BANK LOAN    A/C NO");
            dmpUtil.writeToFile("     DESG/                  GP                      " + StringUtils.rightPad(StringUtils.defaultString(col7Desc[2]), 8) + "                       GPDD        & SWR TAX       SPL.HBAP      MOP ADVP     BI CYCL   TRADE ADV  NPS ARR GIS ADV   NET-PAY  NET BALANCE");
            dmpUtil.writeToFile("     PAY SCALE                                      " + StringUtils.rightPad(StringUtils.defaultString(col7Desc[3]), 8) + "                       GPIR         HIRE CHG    INT SPL HBA      INT MOPA     INT CYCL              E.P    AIS GIS");
            dmpUtil.writeToFile("     GPF NO/PRAN                                    " + StringUtils.rightPad(StringUtils.defaultString(col7Desc[4]), 8) + "                                                                                                   AUDR    COMP. ADV");
            dmpUtil.writeToFile("                                                    " + StringUtils.rightPad(StringUtils.defaultString(col7Desc[5]), 8) + "                                                                                      ");
            dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            dmpUtil.writeToFile("(1) (2)                    (3)     (4)   (5)   (6)   (7)      (8)      (9)       (10)    (11)         (12)         (13)         (14)        (15)        (16)      (17)    (18)   (19)       (20)       (21)");
            dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        } else if (col6Desc != null) {
            dmpUtil.writeToFile("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            dmpUtil.writeToFile(" SL  NAME/               BASIC      DP    DA  " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[0]), 8) + "  IA     GROSS     LIC/ GPF/EPF/CPF  P.TAX  QTR DEDN           HBAP             MCAP       CAR ADV     PAY ADV    FEST    OTHER     TOTAL   PVTDED      REMARKS");
            dmpUtil.writeToFile("     ORD.NO/DT         SPL.PAY            IR  " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[1]), 8) + "  WA               PLI GPF RECOVER  I.TAX WATER TAX        INT HBA          INT MCA       INT CAR     MED ADV    TA ADV  RECOVERY   DEDN    BANK LOAN    A/C NO");
            dmpUtil.writeToFile("     DESG/                  GP                " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[2]), 8) + "  CA                           GPDD        & SWR TAX       SPL.HBAP         MOP ADVP       BI CYCL   TRADE ADV   NPS ARR GIS ADV    NET-PAY  NET BALANCE");
            dmpUtil.writeToFile("     PAY SCALE                                " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[3]), 8) + "  OTA                          GPIR         HIRE CHG    INT SPL HBA         INT MOPA      INT CYCL                E.P    AIS GIS");
            dmpUtil.writeToFile("     GPF NO/PRAN                              " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[4]), 8) + "  DEP.AL                                                                                                          AUDR    COMP. ADV");
            dmpUtil.writeToFile("                                              " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[5]), 8) + "                                                                                                  ");
            dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            dmpUtil.writeToFile("(1) (2)                    (3)   (4)     (5)   (6)    (7)      (8)      (9)       (10)    (11)       (12)         (13)            (14)           (15)        (16)       (17)       (18)     (19)       (20)      (21)");
            dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        } else {
            dmpUtil.writeToFile("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            dmpUtil.writeToFile(" SL  NAME/               BASIC      DP    DA   HRA    IA     GROSS     LIC/ GPF/EPF/CPF  P.TAX  QTR DEDN           HBAP             MCAP       CAR ADV     PAY ADV    FEST    OTHER     TOTAL   PVTDED      REMARKS");
            dmpUtil.writeToFile("     ORD.NO/DT         SPL.PAY            IR          WA                PLI GPF RECOVER  I.TAX WATER TAX        INT HBA          INT MCA       INT CAR     MED ADV    TA ADV  RECOVERY   DEDN    BANK LOAN    A/C NO");
            dmpUtil.writeToFile("     DESG/                  GP                        CA                           GPDD        & SWR TAX       SPL.HBAP         MOP ADVP       BI CYCL   TRADE ADV    NPS ARR GIS ADV    NET-PAY  NET BALANCE");
            dmpUtil.writeToFile("     PAY SCALE                                        OTA                          GPIR         HIRE CHG    INT SPL HBA         INT MOPA      INT CYCL                E.P    AIS GIS");
            dmpUtil.writeToFile("     GPF NO/PRAN                                      DEP.AL                                                                                                          AUDR    COMP. ADV");
            dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            dmpUtil.writeToFile("(1) (2)                    (3)   (4)     (5)   (6)    (7)      (8)      (9)       (10)    (11)       (12)         (13)            (14)           (15)        (16)       (17)       (18)     (19)       (20)      (21)");
            dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        }

    }

    private void printPageTotal(DMPUtil dmpUtil, ArrayList originalList, int pageno, int count, boolean isLastPage) throws Exception {
        int end = 0;
        //if(pageno == 1){
        //end = (pageno) * 5; 
        //}else{
        //end = ((pageno) * 5)+1;
        //}
        end = (count * 5) - 1;
        totdedLine = end;
        /*1ST ROW*/
        String fstRowCaption = "";
        if (isLastPage == true) {
            fstRowCaption = StringUtils.rightPad("CARRIED FROM PAGE " + (pageno - 1) + ":", 20);
        } else {
            fstRowCaption = StringUtils.rightPad("PAGE " + pageno + " TOTAL :", 20);
        }
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + fstRowCaption + " "
                + StringUtils.leftPad(StringUtils.clean(AqFunctionalities.getColTotal(originalList, end, "col3", "BASIC", null) + ""), 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col4", "DP", null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col5", "DA", null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col6", 0, null) + "", 5) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col7", 0, null) + "", 5) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col8", "GROSS PAY", null) + "", 8) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col9", "LIC", null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col10", "GPF", null) + "", 12) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col11", "PT", null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col12", "HRR", null) + "", 9) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col13", "HBA", "P") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col14", "MCA", "P") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col15", "VE", "P") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col16", "PA", null) + "", 11) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col17", 0, null) + "", 10) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col18", "OR", null) + "", 11) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col19", "TOTDEN", null) + "", 8) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col20", "PVTDED", null) + "", 8));
        /*2ND ROW*/
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.rightPad("(Column wise)", 20) + " "
                + StringUtils.leftPad(StringUtils.clean(AqFunctionalities.getColTotal(originalList, end, "col3", "SP", null) + ""), 6) + " "
                + StringUtils.leftPad("", 6) + " " + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col5", "IR", null) + "", 6) + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col6", 1, null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col7", 1, null) + "", 5) + " "
                + StringUtils.leftPad("", 8) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col9", "PLI", null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col10", "GA", null) + "", 12) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col11", "IT", null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col12", "WRR", null) + "", 9) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col13", "HBA", "I") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col14", "MCA", "I") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col15", "VE", "I") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col16", "MAL", null) + "", 11) + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col17", 1, null) + "", 11) + " "
                + //StringUtils.leftPad("",10)+" "+
                StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col18", "GISA", "P") + "", 11) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col19", "NETPAY", null) + "", 8)
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col20", "BANKLOAN", null) + "", 8));
        /*3RD ROW*/
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.leftPad("", 20) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col3", "GP", null) + "", 6) + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col6", 2, null) + "", 20) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col7", 2, null) + "", 5) + " "
                + StringUtils.leftPad("", 8) + " " + StringUtils.leftPad("", 6) + " " + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col10", "GPDD", null) + "", 12) + " "
                + StringUtils.leftPad("", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col12", "SWR", null) + "", 9) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col13", "SHBA", "P") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col14", "MOPA", "P") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col15", "BICA", "P") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col16", "TRDA", null) + "", 11) + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col17", 2, null) + "", 11) + " "
                + //StringUtils.leftPad("",10)+" "+
                StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col18", "GIS", null) + "", 11)
                + StringUtils.leftPad("", 10)
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col20", "NETBALANCE", null) + "", 8));
        /*4TH ROW*/
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.leftPad("", 20) + " " + StringUtils.leftPad("", 20) + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col6", 3, null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col7", 3, null) + "", 5) + " "
                + StringUtils.leftPad("", 8) + " " + StringUtils.leftPad("", 6) + " " + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col10", "GPIR", null) + "", 12) + " "
                + StringUtils.leftPad("", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col12", "HC", null) + "", 9) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col13", "SHBA", "I") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col14", "MOPA", "I") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col15", "BICA", "I") + "", 14));
        /*5TH ROW*/
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.leftPad("", 20) + " " + StringUtils.leftPad("", 20) + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col6", 4, null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col7", 4, null) + "", 5) + " "
                + StringUtils.leftPad("", 8) + " " + StringUtils.leftPad("", 6) + " " + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col10", "CPF", null) + "", 12) + " "
                + StringUtils.leftPad("", 6));
        /*6TH ROW*/
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.leftPad("", 20) + " " + StringUtils.leftPad("", 20) + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col6", 5, null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col7", 5, null) + "", 5));

        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.leftPad("", 20) + " " + StringUtils.leftPad("", 20) + StringUtils.leftPad("", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col7", 6, null) + "", 5) + " "
                + StringUtils.leftPad("", 8) + " " + StringUtils.leftPad("", 6) + " " + StringUtils.leftPad("", 12) + " "
                + StringUtils.leftPad("", 6));
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.leftPad("", 20) + " " + StringUtils.leftPad("", 20) + StringUtils.leftPad("", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col7", 7, null) + "", 5));

        dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    private void grandTotal(DMPUtil dmpUtil, ArrayList originalList, int pageno) throws Exception {
        String fstRowCaption = "";
        fstRowCaption = StringUtils.rightPad("GRAND TOTAL :", 20);

        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + fstRowCaption + " "
                + StringUtils.leftPad(StringUtils.clean(AqFunctionalities.getColGrandTotal(originalList, "col3", "BASIC", null) + AqFunctionalities.getColGrandTotal(originalList, "col3", "SP", null) + AqFunctionalities.getColGrandTotal(originalList, "col3", "GP", null) + ""), 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col4", "DP", null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col5", "DA", null) + AqFunctionalities.getColGrandTotal(originalList, "col5", "IR", null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col6", 0, null) + AqFunctionalities.getColGrandTotal(originalList, "col6", 1, null) + AqFunctionalities.getColGrandTotal(originalList, "col6", 2, null) + AqFunctionalities.getColGrandTotal(originalList, "col6", 3, null) + AqFunctionalities.getColGrandTotal(originalList, "col6", 4, null) + "", 5) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col7", 0, null) + AqFunctionalities.getColGrandTotal(originalList, "col7", 1, null) + AqFunctionalities.getColGrandTotal(originalList, "col7", 2, null) + AqFunctionalities.getColGrandTotal(originalList, "col7", 3, null) + AqFunctionalities.getColGrandTotal(originalList, "col7", 4, null) + "", 5) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col8", "GROSS PAY", null) + "", 8) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col9", "LIC", null) + AqFunctionalities.getColGrandTotal(originalList, "col9", "PLI", null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col10", "GPF", null) + AqFunctionalities.getColGrandTotal(originalList, "col10", "GA", null) + AqFunctionalities.getColGrandTotal(originalList, "col10", "GPDD", null) + AqFunctionalities.getColGrandTotal(originalList, "col10", "GPIR", null) + AqFunctionalities.getColGrandTotal(originalList, "col10", "CPF", null) + "", 12) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col11", "PT", null) + AqFunctionalities.getColGrandTotal(originalList, "col11", "IT", null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col12", "HRR", null) + AqFunctionalities.getColGrandTotal(originalList, "col12", "WRR", null) + AqFunctionalities.getColGrandTotal(originalList, "col12", "SWR", null) + AqFunctionalities.getColGrandTotal(originalList, "col12", "HC", null) + "", 9) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col13", "HBA", "P") + AqFunctionalities.getColGrandTotal(originalList, "col13", "HBA", "I") + AqFunctionalities.getColGrandTotal(originalList, "col13", "SHBA", "P") + AqFunctionalities.getColGrandTotal(originalList, "col13", "SHBA", "I") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col14", "MCA", "P") + AqFunctionalities.getColGrandTotal(originalList, "col14", "MCA", "I") + AqFunctionalities.getColGrandTotal(originalList, "col14", "MOPA", "P") + AqFunctionalities.getColGrandTotal(originalList, "col14", "MOPA", "I") + "", 16) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col15", "VE", "P") + AqFunctionalities.getColGrandTotal(originalList, "col15", "VE", "I") + AqFunctionalities.getColGrandTotal(originalList, "col15", "BICA", "P") + AqFunctionalities.getColGrandTotal(originalList, "col15", "BICA", "I") + "", 12) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col16", "PA", null) + AqFunctionalities.getColGrandTotal(originalList, "col16", "MAL", null) + AqFunctionalities.getColGrandTotal(originalList, "col16", "TRDA", null) + "", 11) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col17", 0, null) + AqFunctionalities.getColGrandTotal(originalList, "col17", 1, null) + AqFunctionalities.getColGrandTotal(originalList, "col17", 2, null) + "", 10) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col18", "OR", null) + AqFunctionalities.getColGrandTotal(originalList, "col18", "GISA", "P") + AqFunctionalities.getColGrandTotal(originalList, "col18", "GIS", null) + AqFunctionalities.getColGrandTotal(originalList, "col18", "CMPA", "P") + "", 11) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col19", "NETPAY", null) + AqFunctionalities.getColGrandTotal(originalList, "col19", "TOTDEN", null) + "", 8) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col20", "PVTDED", null) + AqFunctionalities.getColGrandTotal(originalList, "col20", "BANKLOAN", null) + AqFunctionalities.getColGrandTotal(originalList, "col20", "NETBALANCE", null) + "", 8));
        dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    private void printPageGrandTotal(DMPUtil dmpUtil, ArrayList originalList, int totalRecord) throws Exception {

        String fstRowCaption = "";

        fstRowCaption = StringUtils.rightPad("UNIT TOTAL :", 20);
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + fstRowCaption + " "
                + StringUtils.leftPad(StringUtils.clean(AqFunctionalities.getColGrandTotal(originalList, "col3", "BASIC", null) + ""), 6) + " "
                + StringUtils.leftPad(StringUtils.defaultString(AqFunctionalities.getColGrandTotal(originalList, "col4", "DP", null) + "") + "", 6) + " "
                + StringUtils.leftPad(StringUtils.defaultString(AqFunctionalities.getColGrandTotal(originalList, "col5", "DA", null) + "") + "", 6) + " "
                + StringUtils.leftPad(StringUtils.defaultString(AqFunctionalities.getColGrandTotal(originalList, "col6", 0, null) + "") + "", 5) + " "
                + StringUtils.leftPad(StringUtils.defaultString(AqFunctionalities.getColGrandTotal(originalList, "col7", 0, null) + "") + "", 5) + " "
                + StringUtils.leftPad(StringUtils.defaultString(AqFunctionalities.getColGrandTotal(originalList, "col8", "GROSS PAY", null) + "") + "", 8) + " "
                + StringUtils.leftPad(StringUtils.defaultString(AqFunctionalities.getColGrandTotal(originalList, "col9", "LIC", null) + "") + "", 6) + " "
                + StringUtils.leftPad(StringUtils.defaultString(AqFunctionalities.getColGrandTotal(originalList, "col10", "GPF", null) + "") + "", 12) + " "
                + StringUtils.leftPad(StringUtils.defaultString(AqFunctionalities.getColGrandTotal(originalList, "col11", "PT", null) + "") + "", 6) + " "
                + StringUtils.leftPad(StringUtils.defaultString(AqFunctionalities.getColGrandTotal(originalList, "col12", "HRR", null) + "") + "", 9) + " "
                + StringUtils.leftPad(StringUtils.defaultString(AqFunctionalities.getColGrandTotal(originalList, "col13", "HBA", "P") + "") + "", 14) + " "
                + StringUtils.leftPad(StringUtils.defaultString(AqFunctionalities.getColGrandTotal(originalList, "col14", "MCA", "P") + "") + "", 16) + " "
                + StringUtils.leftPad(StringUtils.defaultString(AqFunctionalities.getColGrandTotal(originalList, "col15", "VE", "P") + "") + "", 12) + " "
                + StringUtils.leftPad(StringUtils.defaultString(AqFunctionalities.getColGrandTotal(originalList, "col16", "PA", null) + "") + "", 11) + " "
                + StringUtils.leftPad(StringUtils.defaultString(AqFunctionalities.getColGrandTotal(originalList, "col17", 0, null) + "") + "", 10) + " "
                + StringUtils.leftPad(StringUtils.defaultString(AqFunctionalities.getColGrandTotal(originalList, "col18", "OR", null) + "") + "", 11) + " "
                + StringUtils.leftPad(StringUtils.defaultString(AqFunctionalities.getColGrandTotal(originalList, "col19", "TOTDEN", null) + "") + "", 8) + " "
                + StringUtils.leftPad(StringUtils.defaultString(AqFunctionalities.getColGrandTotal(originalList, "col20", "NETBALANCE", null) + "") + "", 8));
        /*2ND ROW*/
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.rightPad("(Column wise)", 20) + " "
                + StringUtils.leftPad(StringUtils.clean(AqFunctionalities.getColGrandTotal(originalList, "col3", "SP", null) + ""), 6) + " "
                + StringUtils.leftPad("", 6) + " " + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col5", "IR", null) + "", 6) + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col6", 1, null) + "", 6) + " "
                + //StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList,"col6",1,null)+"",5)+" "+
                StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col7", 1, null) + "", 5) + " "
                + StringUtils.leftPad("", 8) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col9", "PLI", null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col10", "GA", null) + "", 12) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col11", "IT", null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col12", "WRR", null) + "", 9) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col13", "HBA", "I") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col14", "MCA", "I") + "", 16) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col15", "VE", "I") + "", 12) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col16", "MAL", null) + "", 11) + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col17", 1, null) + "", 11) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col18", "GISA", "P") + "", 11) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col19", "NETPAY", null) + "", 8));
        /*3RD ROW*/
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.leftPad("", 20) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col3", "GP", null) + "", 6) + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col6", 2, null) + "", 20) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col7", 2, null) + "", 5) + " "
                + StringUtils.leftPad("", 8) + " " + StringUtils.leftPad("", 6) + " " + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col10", "GPDD", null) + "", 12) + " "
                + StringUtils.leftPad("", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col12", "SWR", null) + "", 9) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col13", "SHBA", "P") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col14", "MOPA", "P") + "", 16) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col15", "BICA", "P") + "", 12) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col16", "TRDA", null) + "", 11) + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col17", 2, null) + "", 11) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col18", "GIS", null) + "", 11));
        /*4TH ROW*/
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.leftPad("", 20) + " " + StringUtils.leftPad("", 20) + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col6", 3, null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col7", 3, null) + "", 5) + " "
                + StringUtils.leftPad("", 8) + " " + StringUtils.leftPad("", 6) + " " + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col10", "GPIR", null) + "", 12) + " "
                + StringUtils.leftPad("", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col12", "HC", null) + "", 9) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col13", "SHBA", "I") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col14", "MOPA", "I") + "", 16) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col15", "BICA", "I") + "", 12));
        /*5TH ROW*/
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.leftPad("", 20) + " " + StringUtils.leftPad("", 20) + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col6", 4, null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col7", 4, null) + "", 5) + " "
                + StringUtils.leftPad("", 8) + " " + StringUtils.leftPad("", 6) + " " + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col10", "CPF", null) + "", 12) + " "
                + StringUtils.leftPad("", 6));
        /*5TH ROW*/
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.leftPad("", 20) + " " + StringUtils.leftPad("", 20) + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col6", 5, null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col7", 5, null) + "", 5));
        dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    public Schedule getScheduleData(ArrayList ScheduleList, String scheduleName) {
        Schedule returnSchedule = new Schedule();
        for (int i = 0; i < ScheduleList.size(); i++) {
            Schedule tempSchedule = (Schedule) ScheduleList.get(i);
            if (tempSchedule.getScheduleName().equals(scheduleName)) {
                returnSchedule = tempSchedule;
                break;
            }
        }
        return returnSchedule;
    }

    public void printCarryForward(DMPUtil dmpUtil, ArrayList aqlist, int pageNo, int count, boolean isLastPage) throws Exception {
        int end = 0;
        //if(pageNo == 1){
        //end = (pageNo-1) * 6; 
        //}else{
        end = (count * 5) - 1;
        //}

        String fstRowCaption = "";
        if (isLastPage == true) {
            fstRowCaption = StringUtils.rightPad("GRAND TOTAL :", 25);
        } else {
            fstRowCaption = StringUtils.rightPad(" CARRIED FROM PAGE " + (pageNo - 1), 25);
        }
        dmpUtil.writeToFile(fstRowCaption
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col3") + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col4") + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col5") + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col6") + "", 5) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col7") + "", 5) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col8") + "", 8) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col9") + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col10") + "", 12) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col11") + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col12") + "", 9) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col13") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col14") + "", 16) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col15") + "", 13) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col16") + "", 11) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col17") + "", 10) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col18") + "", 11) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col19") + "", 8) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col20") + "", 8)
        );
        dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

    }

    public void printPageTotaling(DMPUtil dmpUtil, ArrayList aqlist, int pageNo, boolean isLastPage) throws Exception {
        int end = 0;
        //if(pageNo == 1){
        end = (pageNo * 5) - 1;
        //}else{
        // end = ((pageNo) * 6)-1;
        //}
        String fstRowCaption = "";
        fstRowCaption = StringUtils.rightPad(" ", 25);
        dmpUtil.writeToFile(fstRowCaption
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col3") + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col4") + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col5") + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col6") + "", 5) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col7") + "", 5) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col8") + "", 8) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col9") + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col10") + "", 12) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col11") + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col12") + "", 9) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col13") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col14") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col15") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col16") + "", 11) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col17") + "", 10) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col18") + "", 11) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col19") + "", 8) + " "
                + StringUtils.leftPad(AqFunctionalities.getColFinalTotal(aqlist, end, "col20") + "", 8)
        );
        dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    public File write(String billno, String folderPath, String fileSeparator, CommonReportParamBean crb) throws Exception {
        ArrayList aqlist = new ArrayList();

        int year = crb.getAqyear();
        int month = crb.getAqmonth();
        String billdesc = crb.getBilldesc();
        String billdate = crb.getBilldate();
        String fileName = "ACQTFII.txt";
        String originalFileName = "ACQTFII.txt";
        File dmp = null;
        SectionWiseAqBean sectionwiseAq = null;
        boolean createFile = false;
        int ctrForLast = 0;

        try {
            //billdate=CommonFunctions.getFormattedOutputDate(res.getDate("BILL_DATE"));   
            folderPath = folderPath+fileSeparator;
            BillConfigObj billConfig = paybillDmpDao.getBillConfig(billno);
            aqlist = paybillDmpDao.getSectionWiseBillDtls(billno, month, year, "f2", billConfig, "R");

            DMPUtil dmpUtil = null;

            //PrintWriter out = response.getWriter();
            int pageNo = 0;
            boolean isHeaderPrinted = false;
            boolean isPageTotalPrinted = false;
            ArrayList al = new ArrayList();
            int totaq = 0;
            
            for (int ia = 0; ia < aqlist.size(); ia++) {
                if (createFile == false) {
                    dmpUtil = new DMPUtil(folderPath, fileName);
                    createFile = true;
                    dmpUtil.writeToFile((char) 27);
                    dmpUtil.writeToFile((char) 64);
                    dmpUtil.writeToFile((char) 15);
                }
                sectionwiseAq = (SectionWiseAqBean) aqlist.get(ia);
                al = sectionwiseAq.getAqlistSectionWise();

                int recordCount = 0;
                int cnt = 0;
                for (int i = 0; i < al.size(); i++) {
                    recordCount++;
                    totaq++;
                    //Printing Header Section for first time
                    if (pageNo == 0) {
                        pageNo++;
                        printHeader(dmpUtil, crb, billdesc, billdate, pageNo, billConfig);
                    }
                    AqreportHelperBean aqbean = (AqreportHelperBean) al.get(i);
                    String row1 = "";
                    String row2 = "";
                    String row3 = "";
                    String row4 = "";
                    String row5 = "";
                    String row6 = "";
                    String row7 = "";
                    String row8 = "";
                    ArrayList column3 = aqbean.getCol3();
                    ArrayList column4 = aqbean.getCol4();
                    ArrayList column5 = aqbean.getCol5();
                    ArrayList column6 = aqbean.getCol6();
                    ArrayList column7 = aqbean.getCol7();
                    ArrayList column8 = aqbean.getCol8();
                    ArrayList column9 = aqbean.getCol9();
                    ArrayList column10 = aqbean.getCol10();
                    ArrayList column11 = aqbean.getCol11();
                    ArrayList column12 = aqbean.getCol12();
                    ArrayList column13 = aqbean.getCol13();
                    ArrayList column14 = aqbean.getCol14();
                    ArrayList column15 = aqbean.getCol15();
                    ArrayList column16 = aqbean.getCol16();
                    ArrayList column17 = aqbean.getCol17();
                    ArrayList column18 = aqbean.getCol18();
                    ArrayList column19 = aqbean.getCol19();
                    ArrayList column20 = aqbean.getCol20();
                    int gpfsum = 0;
                    for (int k = 0; k <= 7; k++) {
                        if (k > 0 && column3.size() > k - 1) {
                            ADDetailsHealperBean adbean = (ADDetailsHealperBean) column3.get(k - 1);
                            if (k == 1) {
                                row2 = StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 6);
                            }
                            if (k == 2) {
                                row3 = StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 6);
                                //row3 = StringUtils.clean(adbean.getAdamt()+"");
                            }
                            if (k == 3) {
                                row4 = StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 6);
                            }
                        } else {
                            if (k == 1) {
                                row2 = StringUtils.leftPad(" ", 6);
                            }
                            if (k == 2) {
                                row3 = StringUtils.leftPad(" ", 6);
                            }
                        }
                        if (column4.size() > k) {
                            ADDetailsHealperBean adbean = (ADDetailsHealperBean) column4.get(k);
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 6);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 6);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 6);
                            }
                        } else {
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(" ", 6);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(" ", 6);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(" ", 6);
                            }
                        }
                        if (column5.size() > k) {
                            ADDetailsHealperBean adbean = (ADDetailsHealperBean) column5.get(k);
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 6);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 6);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 6);
                            }
                        } else {
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(" ", 6);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(" ", 6);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(" ", 6);
                            }
                        }
                        if (column6.size() > k) {
                            ADDetailsHealperBean adbean = (ADDetailsHealperBean) column6.get(k);
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 5);
                            } else if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 5);
                            } else if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 5);
                            } else if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 12);
                            } else if (k == 4) {
                                row5 = row5 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 5);
                            } else if (k == 5) {
                                row6 = row6 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 26);
                            }
                        } else {
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(" ", 5);
                            } else if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(" ", 5);
                            } else if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(" ", 5);
                            } else if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(" ", 12);
                            } else if (k == 4) {
                                row5 = row5 + " " + StringUtils.leftPad(" ", 5);
                            } else if (k == 5) {
                                row6 = row6 + " " + StringUtils.leftPad(" ", 25);
                            }
                        }
                        if (column7.size() > k) {
                            ADDetailsHealperBean adbean = (ADDetailsHealperBean) column7.get(k);
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 5);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 5);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 5);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 5);
                            }
                            if (k == 4) {
                                row5 = row5 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 5);
                            }
                            if (k == 5) {
                                row6 = row6 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 6);
                            }
                            if (k == 6) {
                                row7 = row7 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 5);
                            }
                            if (k == 7) {
                                row8 = row8 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 5);
                            }
                        } else {
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(" ", 5);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(" ", 5);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(" ", 5);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(" ", 5);
                            }
                            if (k == 4) {
                                row5 = row5 + " " + StringUtils.leftPad(" ", 5);
                            }
                            if (k == 5) {
                                row6 = row6 + " " + StringUtils.leftPad(" ", 5);
                            }
                            if (k == 6) {
                                row7 = row7 + " " + StringUtils.leftPad(" ", 5);
                            }
                            if (k == 7) {
                                row8 = row8 + " " + StringUtils.leftPad(" ", 5);
                            }
                        }
                        if (column8.size() > k) {
                            ADDetailsHealperBean adbean = (ADDetailsHealperBean) column8.get(k);
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 8);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 8);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 8);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 8);
                            }
                        } else {
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(" ", 8);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(" ", 8);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(" ", 8);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(" ", 8);
                            }
                        }
                        if (column9.size() > k) {
                            ADDetailsHealperBean adbean = (ADDetailsHealperBean) column9.get(k);
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 6);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 6);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 6);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 6);
                            }
                        } else {
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(" ", 6);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(" ", 6);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(" ", 6);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(" ", 6);
                            }
                        }
                        if (column10.size() > k) {
                            ADDetailsHealperBean adbean = (ADDetailsHealperBean) column10.get(k);

                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 12);
                                gpfsum = adbean.getAdamt();
                            } else if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 12);
                                gpfsum = gpfsum + adbean.getAdamt();
                            } else if (k == 2) {
                                gpfsum = gpfsum + adbean.getAdamt();
                                row3 = row3 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 12);
                            } else if (k == 3) {
                                gpfsum = gpfsum + adbean.getAdamt();
                                row4 = row4 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 12);
                            }
                            int temp = k + 2;
                        } else {
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(" ", 12);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(" ", 12);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(" ", 12);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(" ", 12);
                            }
                            if (k == 4) {
                                if (gpfsum == 0) {
                                    row5 = row5 + " " + StringUtils.leftPad(" ", 12);
                                } else {
                                    row5 = row5 + StringUtils.leftPad(" ", 23) + StringUtils.leftPad("", 6, "-") + StringUtils.leftPad("", 86);
                                }
                            }
                            if (k == 5) {
                                if (gpfsum == 0) {
                                    row6 = row6 + " " + StringUtils.leftPad(" ", 12);
                                } else {
                                    row6 = row6 + StringUtils.leftPad(" ", 17) + StringUtils.leftPad(gpfsum + "", 12);
                                }
                            }
                        }
                        if (column11.size() > k) {
                            ADDetailsHealperBean adbean = (ADDetailsHealperBean) column11.get(k);
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 6);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 6);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 6);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 6);
                            }
                        } else {
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(" ", 6);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(" ", 6);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(" ", 6);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(" ", 6);
                            }
                        }
                        if (column12.size() > k) {
                            ADDetailsHealperBean adbean = (ADDetailsHealperBean) column12.get(k);
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 9);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 9);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 9);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(StringUtils.clean(adbean.getAdamt() + ""), 9);
                            }
                        } else {
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(" ", 9);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(" ", 9);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(" ", 9);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(" ", 9);
                            }
                        }
                        if (column13.size() > k) {
                            ADDetailsHealperBean adbean = (ADDetailsHealperBean) column13.get(k);
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 14);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 14);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 14);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 14);
                            }
                        } else {
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(" ", 14);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(" ", 14);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(" ", 14);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(" ", 14);
                            }
                        }
                        if (column14.size() > k) {
                            ADDetailsHealperBean adbean = (ADDetailsHealperBean) column14.get(k);
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 14);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 14);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 14);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 14);
                            }
                        } else {
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(" ", 14);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(" ", 14);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(" ", 14);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(" ", 14);
                            }
                        }
                        if (column15.size() > k) {
                            ADDetailsHealperBean adbean = (ADDetailsHealperBean) column15.get(k);
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 14);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 14);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 14);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 14);
                            }
                        } else {
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(" ", 14);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(" ", 14);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(" ", 14);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(" ", 14);
                            }
                        }
                        if (column16.size() > k) {
                            ADDetailsHealperBean adbean = (ADDetailsHealperBean) column16.get(k);
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 11);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 11);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 11);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 11);
                            }
                        } else {
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(" ", 11);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(" ", 11);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(" ", 11);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(" ", 11);
                            }
                        }
                        if (column17.size() > k) {
                            ADDetailsHealperBean adbean = (ADDetailsHealperBean) column17.get(k);
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 10);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 10);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 10);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 10);
                            }
                            if (k == 4) {
                                row5 = row5 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 10);
                            }
                        } else {
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(" ", 10);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(" ", 10);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(" ", 10);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(" ", 10);
                            }
                            if (k == 4) {
                                row5 = row5 + " " + StringUtils.leftPad(" ", 10);
                            }
                        }
                        if (column18.size() > k) {
                            ADDetailsHealperBean adbean = (ADDetailsHealperBean) column18.get(k);
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 11);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 11);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 11);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 11);
                            }
                            if (k == 4) {
                                row5 = row5 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 10);
                            }
                        } else {
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(" ", 11);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(" ", 11);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(" ", 11);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(" ", 11);
                            }
                            if (k == 4) {
                                row5 = row5 + " " + StringUtils.leftPad(" ", 11);
                            }
                        }
                        if (column19.size() > k) {
                            ADDetailsHealperBean adbean = (ADDetailsHealperBean) column19.get(k);
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 8);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 8);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 8);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 8);
                            }
                        } else {
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(" ", 8);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(" ", 8);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(" ", 8);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(" ", 8);
                            }
                        }
                        if (column20.size() > k) {
                            ADDetailsHealperBean adbean = (ADDetailsHealperBean) column20.get(k);
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 8);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 8);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 8);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(adbean.getRefdesc() + StringUtils.clean(adbean.getAdamt() + ""), 8);
                            }
                        } else {
                            if (k == 0) {
                                row1 = row1 + " " + StringUtils.leftPad(" ", 8);
                            }
                            if (k == 1) {
                                row2 = row2 + " " + StringUtils.leftPad(" ", 8);
                            }
                            if (k == 2) {
                                row3 = row3 + " " + StringUtils.leftPad(" ", 8);
                            }
                            if (k == 3) {
                                row4 = row4 + " " + StringUtils.leftPad(" ", 8);
                            }
                        }
                    }
                    String empname = aqbean.getEmpname();
                    if (aqbean.getCadreabbr() != null && !aqbean.getCadreabbr().equals("")) {
                        empname = empname + " ," + aqbean.getCadreabbr();
                    }
                    String ordno = "";
                    if (aqbean.getOrdno() != null && !aqbean.getOrdno().equals("")) {
                        if (aqbean.getOrddate() != null && !aqbean.getOrddate().equals("")) {
                            ordno = StringUtils.defaultString(aqbean.getOrdno() + ", " + aqbean.getOrddate());
                        } else {
                            ordno = StringUtils.defaultString(aqbean.getOrdno());
                        }
                        ordno = StringUtils.rightPad(ordno, 21);
                    } else {
                        ordno = StringUtils.rightPad("", 21);
                        //secondLine = StringUtils.rightPad(StringUtils.defaultString(secondLine),20," ");
                    }
                    row1 = " " + StringUtils.rightPad((i + 1) + "", 3) + printName(empname) + " " + StringUtils.leftPad(StringUtils.clean(aqbean.getBasic()), 6) + row1;
                    if (secondLine != null && !secondLine.equals("")) {
                        row2 = StringUtils.rightPad("", 4) + secondLine + "" + row2 + StringUtils.leftPad(StringUtils.defaultString(aqbean.getAccNo()) + "", 12);
                        row3 = StringUtils.rightPad("", 4) + printOrdno(ordno) + row3;
                        if (secondLineOrdno.trim() != null && !secondLineOrdno.trim().equals("")) {
                            row4 = StringUtils.rightPad("", 4) + StringUtils.rightPad(secondLineOrdno, 33, " ") + " " + row4;
                            row5 = StringUtils.rightPad("", 4) + printDesignation(aqbean.getDesg()) + " " + StringUtils.rightPad(" ", 7) + row5;
                            row6 = StringUtils.rightPad("", 4) + StringUtils.rightPad(StringUtils.defaultString(aqbean.getPayscale()), 20) + row6;
                            row7 = StringUtils.rightPad("", 4) + StringUtils.rightPad(StringUtils.defaultString(aqbean.getGpfacct()), 47) + row7;
                            row8 = StringUtils.rightPad("", 6) + StringUtils.rightPad("", 45) + row8;
                        } else {
                            row4 = StringUtils.rightPad("", 4) + printDesignation(aqbean.getDesg()) + " " + row4;
                            row5 = StringUtils.rightPad("", 4) + StringUtils.rightPad(StringUtils.defaultString(aqbean.getPayscale()), 20) + " " + StringUtils.rightPad(" ", 20) + row5;
                            row6 = StringUtils.rightPad("", 4) + StringUtils.rightPad(StringUtils.defaultString(aqbean.getGpfacct()), 20) + row6;
                            row7 = StringUtils.rightPad("", 4) + StringUtils.rightPad("", 45) + row7;
                            row8 = StringUtils.rightPad("", 6) + StringUtils.rightPad("", 45) + row8;
                        }
                        secondLine = "";
                    } else {
                        row2 = StringUtils.rightPad("", 4) + StringUtils.rightPad(" ", 21) + row2 + StringUtils.leftPad(StringUtils.defaultString(aqbean.getAccNo()) + "", 12);
                        row3 = StringUtils.rightPad("", 4) + printOrdno(ordno) + row3;
                        if (secondLineOrdno.trim() != null && !secondLineOrdno.trim().equals("")) {
                            row4 = StringUtils.rightPad("", 4) + StringUtils.rightPad(secondLineOrdno, 33, " ") + " " + row4;
                            row5 = StringUtils.rightPad("", 4) + printDesignation(aqbean.getDesg()) + " " + StringUtils.rightPad(" ", 7) + row5;
                            row6 = StringUtils.rightPad("", 4) + StringUtils.rightPad(StringUtils.defaultString(aqbean.getPayscale()), 20) + row6;
                            row7 = StringUtils.rightPad("", 4) + StringUtils.rightPad(StringUtils.defaultString(aqbean.getGpfacct()), 47) + row7;
                            row8 = StringUtils.rightPad("", 6) + StringUtils.rightPad("", 45) + row8;
                        } else {
                            row4 = StringUtils.rightPad("", 4) + printDesignation(aqbean.getDesg()) + " " + row4;
                            row5 = StringUtils.rightPad("", 4) + StringUtils.rightPad(StringUtils.defaultString(aqbean.getPayscale()), 20) + " " + StringUtils.rightPad(" ", 20) + row5;
                            row6 = StringUtils.rightPad("", 4) + StringUtils.rightPad(StringUtils.defaultString(aqbean.getGpfacct()), 20) + row6;
                            row7 = StringUtils.rightPad("", 6) + StringUtils.rightPad("", 45) + row7;
                            row8 = StringUtils.rightPad("", 6) + StringUtils.rightPad("", 45) + row8;
                        }
                    }

                    dmpUtil.writeToFile(row1);
                    dmpUtil.writeToFile(row2);
                    dmpUtil.writeToFile(row3);
                    dmpUtil.writeToFile(row4);
                    dmpUtil.writeToFile(row5);
                    dmpUtil.writeToFile(row6);
                    //row7 = row7.replaceAll("\\s", "");
                    //row8 = row8.replaceAll("\\s", "");
                    if (row7 != null && !row7.trim().equals("") && !row7.trim().equals("0")) {
                        dmpUtil.writeToFile(row7);
                    }
                    if (row8 != null && !row8.trim().equals("") && !row8.trim().equals("0")) {
                        dmpUtil.writeToFile(row8);
                    }
                    dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

                    if (recordCount != 1 && recordCount % 5 == 0) {
                        cnt++;
                        ctrForLast = cnt;
                        printPageTotal(dmpUtil, al, pageNo, cnt, false);
                        printPageTotaling(dmpUtil, al, cnt, false);
                        pageNo++;
                        dmpUtil.writeToFile((char) 12);
                        dmpUtil.writeToFile((char) 27);
                        dmpUtil.writeToFile((char) 64);
                        dmpUtil.writeToFile((char) 15);
                        printHeader(dmpUtil, crb, billdesc, billdate, pageNo, billConfig);
                        if (al.size() > 5 && pageNo != 1) {
                            printCarryForward(dmpUtil, al, pageNo, cnt, false);
                        }
                        isPageTotalPrinted = true;
                        isHeaderPrinted = false;
                        if (isPageTotalPrinted == false) {

                            printPageTotal(dmpUtil, al, pageNo, cnt, false);
                            printPageTotaling(dmpUtil, al, pageNo, false);
                            isPageTotalPrinted = true;
                            isHeaderPrinted = false;
                        }

                    } else if (recordCount == al.size() && recordCount % 5 != 0) {
                        cnt++;
                        ctrForLast = cnt;
                        printPageTotal(dmpUtil, al, pageNo, cnt, false);
                        printPageTotaling(dmpUtil, al, cnt, false);

                        pageNo++;
                        dmpUtil.writeToFile((char) 12);
                        dmpUtil.writeToFile((char) 27);
                        dmpUtil.writeToFile((char) 64);
                        dmpUtil.writeToFile((char) 15);
                        printHeader(dmpUtil, crb, billdesc, billdate, pageNo, billConfig);
                        if (al.size() > 6 && pageNo != 1 && recordCount != al.size()) {
                            printCarryForward(dmpUtil, al, pageNo, cnt, false);
                        }

                        isPageTotalPrinted = true;
                        isHeaderPrinted = false;
                        if (isPageTotalPrinted == false) {
                            printPageTotal(dmpUtil, al, pageNo, cnt, false);
                            printPageTotaling(dmpUtil, al, pageNo, false);
                            isPageTotalPrinted = true;
                            isHeaderPrinted = false;
                        }
                    }

                }
            }
            printPageGrandTotal(dmpUtil, aqlist, pageNo + 1);
            grandTotal(dmpUtil, aqlist, pageNo + 1);
            //printCarryForward(dmpUtil,al,pageNo+1,ctrForLast,true);
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile("");

            dmpUtil.writeToFile(StringUtils.leftPad("RUPEES " + StringUtils.upperCase(Numtowordconvertion.convertNumber(AqFunctionalities.getColGrandTotal(aqlist, "col20", "PVTDED", null) + AqFunctionalities.getColGrandTotal(aqlist, "col20", "BANKLOAN", null) + AqFunctionalities.getColGrandTotal(aqlist, "col20", "NETBALANCE", null))) + " ONLY", characterPerLine));
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(StringUtils.leftPad(StringUtils.defaultString(crb.getDdoname()), characterPerLine, " "));
            HashMap payAbstract = paybillDmpDao.getPayAbstract(aqlist, billno,crb.getAqmonth(),crb.getAqyear());
            dmpUtil.writeToFile("Pay   = " + StringUtils.leftPad(payAbstract.get("pay").toString(), 7));
            dmpUtil.writeToFile("DP    = " + StringUtils.leftPad(payAbstract.get("dp").toString(), 7));
            dmpUtil.writeToFile("DA    = " + StringUtils.leftPad(payAbstract.get("da").toString(), 7));
            dmpUtil.writeToFile("HRA   = " + StringUtils.leftPad(payAbstract.get("hra").toString(), 7));
            dmpUtil.writeToFile("OA    = " + StringUtils.leftPad(payAbstract.get("oa").toString(), 7));
            dmpUtil.writeToFile("---------------");
            dmpUtil.writeToFile("Total = " + StringUtils.leftPad((Integer.parseInt(payAbstract.get("pay").toString()) + Integer.parseInt(payAbstract.get("dp").toString()) + Integer.parseInt(payAbstract.get("da").toString()) + Integer.parseInt(payAbstract.get("hra").toString()) + Integer.parseInt(payAbstract.get("oa").toString())) + "", 7));
            dmpUtil.writeToFile("---------------");
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile("");
            String leftspace = "                                                     ";
            dmpUtil.writeToFile(leftspace + "LIC    7100(55832)= Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col9", "LIC", null) + "", 9) + "FESTIVAL ADV  " + crb.getFabtid() + "=Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col17", "FA", null) + "", 9));
            dmpUtil.writeToFile(leftspace + "HR            3321= Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col12", "HRR", null) + "", 9) + "GEN HBA ADV   8678(55521)=Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col13", "HBA", "P") + "", 9));
            dmpUtil.writeToFile(leftspace + "WRR           3320= Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col12", "WRR", null) + "", 9) + "CAR ADV           =Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col15", "VE", "P") + "", 9));
            dmpUtil.writeToFile(leftspace + "SWR               = Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col12", "SWR", null) + "", 9) + "EX. PAY            =Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col17", 2, null) + "", 9));
            dmpUtil.writeToFile(leftspace + "HUDCO  7049(55522)= Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col13", "SHBA", "P") + "", 9) + "MC/MOPED ADV  8679(55525)=Rs" + StringUtils.rightPad((AqFunctionalities.getColGrandTotal(aqlist, "col14", "MOPA", "P") + AqFunctionalities.getColGrandTotal(aqlist, "col14", "MCA", "P")) + "", 9));
            dmpUtil.writeToFile(leftspace + "INT. ON HUDCO 3206= Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col13", "SHBA", "I") + "", 9) + "BI-CYCLS ADV  3036(57633)=Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col14", "BI", "P") + "", 9));
            dmpUtil.writeToFile(leftspace + "PROF. TAX     3043= Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col11", "PT", null) + "", 9) + "GIS ADV       8680(57639)=Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col18", "GISA", "P") + "", 9));
            dmpUtil.writeToFile(leftspace + "INC. TAX 7112(58816)= Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col11", "IT", null) + "", 9) + "GPF           8690(55545)=Rs" + StringUtils.rightPad((AqFunctionalities.getColGrandTotal(aqlist, "col10", "GPF", null) + AqFunctionalities.getColGrandTotal(aqlist, "col10", "GA", "P")) + "", 9));
            dmpUtil.writeToFile(leftspace + "MISC          3279= Rs" + StringUtils.rightPad(0 + "", 9) + "INT MC/Moped  7322=Rs" + StringUtils.rightPad((AqFunctionalities.getColGrandTotal(aqlist, "col14", "MOPA", "I") + AqFunctionalities.getColGrandTotal(aqlist, "col14", "MCA", "I")) + "", 9));
            dmpUtil.writeToFile(leftspace + "HICHG         8685= Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col12", "HC", null) + "", 9) + "INT HBA       7321=Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col13", "HBA", "I") + "", 9));
            dmpUtil.writeToFile(leftspace + "AISGIS 8693(58829)= Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col18", "GIS", null) + "", 9) + "INT CAR ADV       =Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col15", "VE", "I") + "", 9));
            dmpUtil.writeToFile(leftspace + "GPDD              = Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col10", "GPDD", null) + "", 9) + "GPIR              =Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col10", "GPIR", null) + "", 9));
            dmpUtil.writeToFile(leftspace + "CPF    9871(57740)= Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col10", "CPF", null) + "", 9) + "PAY ADV           =Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col16", "PA", null) + "", 9));
            dmpUtil.writeToFile(leftspace + "TLIC          7129= Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col9", "TLIC", null) + "", 9) + "COMPUTER ADV. 30015(57635)= Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col18", "CMPA", "P") + "", 9));
            dmpUtil.writeToFile(leftspace + "TPF    7058(55550)= Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col10", "TPF", null) + "", 9) + "TPF ADV        7058(55550)=Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col10", "TPFGA", null) + "", 9));
            dmpUtil.writeToFile(leftspace + "NPS ARR.          = Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col17", "NPSL", null) + "", 9) + "COMPUTER ADV.(I) 7324 = Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col18", "CMPA", "I") + "", 9));
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile("");
            dmpUtil.writeToFile(leftspace + StringUtils.repeat("-", 59));
            dmpUtil.writeToFile(leftspace + StringUtils.rightPad(" ", 44) + "Total=Rs" + StringUtils.rightPad(AqFunctionalities.getColGrandTotal(aqlist, "col19", "TOTDEN", null) + "", 8));
            dmpUtil.writeToFile((char) 12);
            dmpUtil.writeToFile((char) 26);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
        return dmp;
    }

}
