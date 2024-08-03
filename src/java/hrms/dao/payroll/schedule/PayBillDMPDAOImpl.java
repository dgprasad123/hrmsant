/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.schedule;

import hrms.common.AqFunctionalities;
import hrms.common.CommonFunctions;
import hrms.common.DMPUtil;
import hrms.common.DataBaseFunctions;
import hrms.model.common.CommonReportParamBean;
import hrms.model.master.GQtrPool;
import hrms.model.payroll.billbrowser.BillConfigObj;
import hrms.model.payroll.schedule.ADDetailsHealperBean;
import hrms.model.payroll.schedule.AqreportHelperBean;
import hrms.model.payroll.schedule.PrivateDeduction;
import hrms.model.payroll.schedule.Schedule;
import hrms.model.payroll.schedule.SectionWiseAqBean;
import hrms.model.payroll.schedule.WrrScheduleBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Manas
 */
public class PayBillDMPDAOImpl implements PayBillDMPDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    int characterPerLine = 215;
    String secondLine = "";
    String secondLineDesg = "";
    String secondLineOrdno = "";
    int totdedLine = 0;
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

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
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col15", "VE", "P") + AqFunctionalities.getColGrandTotal(originalList, "col15", "VE", "I") + AqFunctionalities.getColGrandTotal(originalList, "col15", "BICA", "P") + AqFunctionalities.getColGrandTotal(originalList, "col15", "BICA", "I") + "", 13) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col16", "PA", null) + AqFunctionalities.getColGrandTotal(originalList, "col16", "MAL", null) + AqFunctionalities.getColGrandTotal(originalList, "col16", "TRDA", null) + "", 11) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col17", 0, null) + AqFunctionalities.getColGrandTotal(originalList, "col17", 1, null) + AqFunctionalities.getColGrandTotal(originalList, "col17", 2, null) + "", 10) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col18", "OR", null) + AqFunctionalities.getColGrandTotal(originalList, "col18", "GISA", "P") + AqFunctionalities.getColGrandTotal(originalList, "col18", "GIS", null) + AqFunctionalities.getColGrandTotal(originalList, "col18", "CMPA", "P") + "", 11) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col19", "TOTDEN", null) + "", 8) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col20", "NETPAY", null) + "", 8));
        dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
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
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col14", "MCA", "P") + "", 16) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col15", "VE", "P") + "", 13) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col16", "PA", null) + "", 11) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col17", 0, null) + "", 10) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col18", "OR", null) + "", 11) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col19", "TOTDEN", null) + "", 8) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col20", "NETPAY", null) + "", 8));

        /*2ND ROW*/
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.rightPad("(Column wise)", 20) + " "
                + StringUtils.leftPad(StringUtils.clean(AqFunctionalities.getColTotal(originalList, end, "col3", "SP", null) + ""), 6) + " "
                + StringUtils.leftPad("", 6) + " " + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col5", "IR", null) + "", 6) + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col6", 1, null) + "", 6) + " "
                + //StringUtils.leftPad(AqFunctionalities.getColTotal(originalList,end,"col6",1,null)+"",5)+" "+
                StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col7", 1, null) + "", 5) + " "
                + StringUtils.leftPad("", 8) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col9", "PLI", null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col10", "GA", null) + "", 12) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col11", "IT", null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col12", "WRR", null) + "", 9) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col13", "HBA", "I") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col14", "MCA", "I") + "", 16) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col15", "VE", "I") + "", 13) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col16", "MAL", null) + "", 11)
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col17", 1, null) + "", 11) + " "
                + //StringUtils.leftPad("",10)+" "+
                StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col18", "GISA", "P") + "", 11));
        /*3RD ROW*/

        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.leftPad("", 20) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col3", "GP", null) + "", 6) + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col6", 2, null) + "", 20) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col7", 2, null) + "", 5) + " "
                + StringUtils.leftPad("", 8) + " " + StringUtils.leftPad("", 6) + " " + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col10", "GPDD", null) + "", 12) + " "
                + StringUtils.leftPad("", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col12", "SWR", null) + "", 9) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col13", "SHBA", "P") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col14", "MOPA", "P") + "", 16) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col15", "BICA", "P") + "", 13) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col16", "TRDA", null) + "", 11)
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col17", 2, null) + "", 11) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col18", "GIS", null) + "", 11));
        /*4TH ROW*/
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.leftPad("", 20) + " " + StringUtils.leftPad("", 20) + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col6", 3, null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col7", 3, null) + "", 5) + " "
                + StringUtils.leftPad("", 8) + " " + StringUtils.leftPad("", 6) + " " + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col10", "GPIR", null) + "", 12) + " "
                + StringUtils.leftPad("", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col12", "HC", null) + "", 9) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col13", "SHBA", "I") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col14", "MOPA", "I") + "", 16) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col15", "BICA", "I") + "", 13) + " "
                + StringUtils.leftPad("", 11)
                + StringUtils.leftPad("", 11) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col18", "CMPA", "P") + "", 11));
        /*5TH ROW*/

        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.leftPad("", 20) + " " + StringUtils.leftPad("", 20) + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col6", 4, null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col7", 4, null) + "", 5) + " "
                + StringUtils.leftPad("", 8) + " " + StringUtils.leftPad("", 6) + " " + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col10", "CPF", null) + "", 12) + " "
                + StringUtils.leftPad("", 6));
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.leftPad("", 20) + " " + StringUtils.leftPad("", 20) + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col6", 5, null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col7", 5, null) + "", 5));

        //if()
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.leftPad("", 20) + " " + StringUtils.leftPad("", 20) + StringUtils.leftPad("", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col7", 6, null) + "", 5) + " "
                + StringUtils.leftPad("", 8) + " " + StringUtils.leftPad("", 6) + " " + StringUtils.leftPad("", 12) + " "
                + StringUtils.leftPad("", 6));
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.leftPad("", 20) + " " + StringUtils.leftPad("", 20) + StringUtils.leftPad("", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColTotal(originalList, end, "col7", 7, null) + "", 5));

        /*dmpUtil.writeToFile(StringUtils.rightPad("",4)+StringUtils.leftPad("",20)+ " "+StringUtils.leftPad("",20)+StringUtils.leftPad("",6)+" "+
         StringUtils.leftPad(AqFunctionalities.getColTotal(originalList,end,"col7",7,null)+"",5)+" "+
         StringUtils.leftPad("",8)+" "+StringUtils.leftPad("",6)+" "+StringUtils.leftPad("",12)+" "+
         StringUtils.leftPad("",6));
         dmpUtil.writeToFile(StringUtils.rightPad("",4)+StringUtils.leftPad("",20)+ " "+StringUtils.leftPad("",20)+StringUtils.leftPad("",6)+" "+
         StringUtils.leftPad("",5));*/
        dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

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
        if (col6Desc != null) {
            StringUtils.leftPad(StringUtils.defaultString(col6Desc[1]), 46);
        }
        if (col7Desc != null && col6Desc != null) {

            dmpUtil.writeToFile("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            dmpUtil.writeToFile(" SL  NAME/               BASIC      DP    DA  " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[0]), 8) + StringUtils.rightPad(StringUtils.defaultString(col7Desc[0]), 6) + " GROSS    LIC/ GPF/EPF/CPF  P.TAX  QTR DEDN           HBAP             MCAP       CAR ADV     PAY ADV       FEST     OTHER      TOTAL  NET PAY REMARKS");
            dmpUtil.writeToFile("     ORD.NO/DT         SPL.PAY            IR  " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[1]), 8) + StringUtils.rightPad(StringUtils.defaultString(col7Desc[1]), 6) + "          PLI  GPF RECOVER  I.TAX WATER TAX        INT HBA          INT MCA       INT CAR     MED ADV      TA ADV   RECOVERY     DEDN           A/C NO");
            dmpUtil.writeToFile("     DESG/                  GP                " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[2]), 8) + StringUtils.rightPad(StringUtils.defaultString(col7Desc[2]), 6) + "                      GPDD        & SWR TAX        SPL.HBAP         MOP ADVP       BI CYCL   TRADE ADV     NPS ARR  GIS ADV");
            dmpUtil.writeToFile("     PAY SCALE                                " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[3]), 8) + StringUtils.rightPad(StringUtils.defaultString(col7Desc[3]), 6) + "                      GPIR         HIRE CHG     INT SPL HBA         INT MOPA      INT CYCL   OVER DRAWL    E.P       AIS GIS");
            dmpUtil.writeToFile("     GPF NO/PRAN                              " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[4]), 8) + StringUtils.rightPad(StringUtils.defaultString(col7Desc[4]), 6) + "                                                                                                           AUDR     COMP. ADV");
            dmpUtil.writeToFile("                                              " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[5]), 8) + StringUtils.rightPad(StringUtils.defaultString(col7Desc[5]), 6) + "                                                                                      ");
            dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            dmpUtil.writeToFile("(1) (2)                    (3)     (4)   (5)   (6)   (7)      (8)      (9)       (10)    (11)       (12)         (13)            (14)           (15)        (16)       (17)       (18)     (19)       (20)      (21)");
            dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        } else if (col7Desc != null) {
            dmpUtil.writeToFile("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            dmpUtil.writeToFile(" SL  NAME/               BASIC      DP    DA   HRA  " + StringUtils.rightPad(StringUtils.defaultString(col7Desc[0]), 8) + " GROSS     LIC/ GPF/EPF/CPF  P.TAX  QTR DEDN           HBAP             MCAP       CAR ADV     PAY ADV      FEST    OTHER       TOTAL  NET PAY REMARKS");
            dmpUtil.writeToFile("     ORD.NO/DT         SPL.PAY            IR        " + StringUtils.rightPad(StringUtils.defaultString(col7Desc[1]), 8) + "           PLI  GPF RECOVER  I.TAX WATER TAX        INT HBA          INT MCA       INT CAR     MED ADV     TA ADV   RECOVERY     DEDN           A/C NO");
            dmpUtil.writeToFile("     DESG/                  GP                      " + StringUtils.rightPad(StringUtils.defaultString(col7Desc[2]), 8) + "                      GPDD        & SWR TAX        SPL.HBAP         MOP ADVP       BI CYCL   TRADE ADV     NPS ARR  GIS ADV");
            dmpUtil.writeToFile("     PAY SCALE                                      " + StringUtils.rightPad(StringUtils.defaultString(col7Desc[3]), 8) + "                      GPIR         HIRE CHG     INT SPL HBA         INT MOPA      INT CYCL  OVER DRAWL     E.P     AIS GIS");
            dmpUtil.writeToFile("     GPF NO/PRAN                                    " + StringUtils.rightPad(StringUtils.defaultString(col7Desc[4]), 8) + "                                                                                                           AUDR     COMP. ADV");
            dmpUtil.writeToFile("                                                    " + StringUtils.rightPad(StringUtils.defaultString(col7Desc[5]), 8) + "                                                                                                                             ");
            dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            dmpUtil.writeToFile("(1) (2)                    (3)     (4)   (5)   (6)   (7)      (8)      (9)       (10)    (11)       (12)         (13)            (14)           (15)        (16)       (17)       (18)     (19)       (20)      (21)");
            dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        } else if (col6Desc != null) {
            dmpUtil.writeToFile("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            dmpUtil.writeToFile(" SL  NAME/               BASIC      DP    DA  " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[0]), 8) + "  IA     GROSS     LIC/ GPF/EPF/CPF  P.TAX  QTR DEDN           HBAP             MCAP       CAR ADV     PAY ADV       FEST      OTHER    TOTAL  NET PAY      REMARKS");
            dmpUtil.writeToFile("     ORD.NO/DT         SPL.PAY            IR  " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[1]), 8) + "  WA                PLI GPF RECOVER  I.TAX WATER TAX        INT HBA          INT MCA       INT CAR     MED ADV     TA ADV   RECOVERY     DEDN                A/C NO");
            dmpUtil.writeToFile("     DESG/                  GP                " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[2]), 8) + "  CA                           GPDD        & SWR TAX       SPL.HBAP         MOP ADVP       BI CYCL   TRADE ADV     NPS ARR   GIS ADV");
            dmpUtil.writeToFile("     PAY SCALE                                " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[3]), 8) + "  OTA                          GPIR         HIRE CHG    INT SPL HBA         INT MOPA      INT CYCL  OVER DRAWL      E.P     AIS GIS");
            dmpUtil.writeToFile("     GPF NO/PRAN                              " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[4]), 8) + "  DEP.AL                                                                                                            AUDR    COMP. ADV");
            dmpUtil.writeToFile("                                              " + StringUtils.rightPad(StringUtils.defaultString(col6Desc[5]), 8) + "                                                                                                                                      ");
            dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            dmpUtil.writeToFile("(1) (2)                    (3)   (4)     (5)   (6)    (7)      (8)      (9)       (10)    (11)       (12)         (13)            (14)           (15)        (16)       (17)       (18)     (19)       (20)        (21)");
            dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        } else {
            dmpUtil.writeToFile("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            dmpUtil.writeToFile(" SL  NAME/               BASIC      DP    DA   HRA    IA     GROSS     LIC/ GPF/EPF/CPF  P.TAX  QTR DEDN           HBAP             MCAP       CAR ADV     PAY ADV       FEST      OTHER    TOTAL  NET PAY      REMARKS");
            dmpUtil.writeToFile("     ORD.NO/DT         SPL.PAY            IR          WA                PLI GPF RECOVER  I.TAX WATER TAX        INT HBA          INT MCA       INT CAR     MED ADV     TA ADV   RECOVERY     DEDN                A/C NO");
            dmpUtil.writeToFile("     DESG/                  GP                        CA                           GPDD        & SWR TAX       SPL.HBAP         MOP ADVP       BI CYCL   TRADE ADV     NPS ARR  GIS ADV");
            dmpUtil.writeToFile("     PAY SCALE                                        OTA                          GPIR         HIRE CHG    INT SPL HBA         INT MOPA      INT CYCL  OVER DRAWL     E.P     AIS GIS");
            dmpUtil.writeToFile("     GPF NO/PRAN                                      DEP.AL                                                                                                           AUDR     COMP. ADV");
            dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            dmpUtil.writeToFile("(1) (2)                    (3)   (4)     (5)   (6)    (7)      (8)      (9)       (10)    (11)       (12)         (13)            (14)           (15)        (16)       (17)       (18)     (19)       (20)        (21)");
            dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        }

    }

    public static int getColFinalTotal(Object objBill, int end, String col) {
        ArrayList arrBill = (ArrayList) objBill;
        AqreportHelperBean aqbean = new AqreportHelperBean();
        ArrayList column = null;
        String subCol = col.substring(3);
        int val = Integer.parseInt(subCol);
        int total = 0;
        ADDetailsHealperBean addbean = null;

        for (int i = 0; i <= end && i < arrBill.size(); i++) {
            aqbean = (AqreportHelperBean) arrBill.get(i);
            if (arrBill.get(i) != null) {
                switch (val) {
                    case 3:
                        column = (ArrayList) aqbean.getCol3();
                        break;
                    case 4:
                        column = (ArrayList) aqbean.getCol4();
                        break;
                    case 5:
                        column = (ArrayList) aqbean.getCol5();
                        break;
                    case 6:
                        column = (ArrayList) aqbean.getCol6();
                        break;
                    case 7:
                        column = (ArrayList) aqbean.getCol7();
                        break;
                    case 8:
                        column = (ArrayList) aqbean.getCol8();
                        break;
                    case 9:
                        column = (ArrayList) aqbean.getCol9();
                        break;
                    case 10:
                        column = (ArrayList) aqbean.getCol10();
                        break;
                    case 11:
                        column = (ArrayList) aqbean.getCol11();
                        break;
                    case 12:
                        column = (ArrayList) aqbean.getCol12();
                        break;
                    case 13:
                        column = (ArrayList) aqbean.getCol13();
                        break;
                    case 14:
                        column = (ArrayList) aqbean.getCol14();
                        break;
                    case 15:
                        column = (ArrayList) aqbean.getCol15();
                        break;
                    case 16:
                        column = (ArrayList) aqbean.getCol16();
                        break;
                    case 17:
                        column = (ArrayList) aqbean.getCol17();
                        break;
                    case 18:
                        column = (ArrayList) aqbean.getCol18();
                        break;
                    case 19:
                        column = (ArrayList) aqbean.getCol19();
                        break;
                    case 20:
                        column = (ArrayList) aqbean.getCol20();
                        break;

                }
            }
            if (val == 3) {
                int basic = 0;
                if (aqbean.getBasic() != null) {
                    basic = Integer.parseInt(aqbean.getBasic());
                }
                total = total + basic;

            }

            for (int j = 0; j < column.size(); j++) {
                addbean = (ADDetailsHealperBean) column.get(j);
                if (val == 19) {
                    if (addbean.getAdcode().equals("TOTDEN") || addbean.getAdcode().equals("NETPAY")) {
                        total = total + addbean.getAdamt();
                    }
                } else if (val == 20) {
                    if (addbean.getAdcode().equals("NETBALANCE") || addbean.getAdcode().equals("NETPAY")) {
                        total = total + addbean.getAdamt();
                    }
                } else {
                    total = total + addbean.getAdamt();
                }
            }
        }

        return total;
    }

    public static int getColFinalTotal(Object objBill, String col) {
        ArrayList arrBill = (ArrayList) objBill;
        AqreportHelperBean aqbean = new AqreportHelperBean();
        ArrayList column = null;
        String subCol = col.substring(3);
        int val = Integer.parseInt(subCol);
        int total = 0;
        ADDetailsHealperBean addbean = null;

        for (int i = 0; i < arrBill.size(); i++) {
            aqbean = (AqreportHelperBean) arrBill.get(i);
            if (arrBill.get(i) != null) {
                switch (val) {
                    case 3:
                        column = (ArrayList) aqbean.getCol3();
                        break;
                    case 4:
                        column = (ArrayList) aqbean.getCol4();
                        break;
                    case 5:
                        column = (ArrayList) aqbean.getCol5();
                        break;
                    case 6:
                        column = (ArrayList) aqbean.getCol6();
                        break;
                    case 7:
                        column = (ArrayList) aqbean.getCol7();
                        break;
                    case 8:
                        column = (ArrayList) aqbean.getCol8();
                        break;
                    case 9:
                        column = (ArrayList) aqbean.getCol9();
                        break;
                    case 10:
                        column = (ArrayList) aqbean.getCol10();
                        break;
                    case 11:
                        column = (ArrayList) aqbean.getCol11();
                        break;
                    case 12:
                        column = (ArrayList) aqbean.getCol12();
                        break;
                    case 13:
                        column = (ArrayList) aqbean.getCol13();
                        break;
                    case 14:
                        column = (ArrayList) aqbean.getCol14();
                        break;
                    case 15:
                        column = (ArrayList) aqbean.getCol15();
                        break;
                    case 16:
                        column = (ArrayList) aqbean.getCol16();
                        break;
                    case 17:
                        column = (ArrayList) aqbean.getCol17();
                        break;
                    case 18:
                        column = (ArrayList) aqbean.getCol18();
                        break;
                    case 19:
                        column = (ArrayList) aqbean.getCol19();
                        break;
                    case 20:
                        column = (ArrayList) aqbean.getCol20();
                        break;

                }
            }
            if (val == 3) {
                int basic = 0;
                if (aqbean.getBasic() != null) {
                    basic = Integer.parseInt(aqbean.getBasic());
                }
                total = total + basic;
            }

            for (int j = 0; j < column.size(); j++) {
                addbean = (ADDetailsHealperBean) column.get(j);

                total = total + addbean.getAdamt();

            }
        }

        return total;
    }

    public static int getColFinalTotal(Object objBill, String col, String adcode, String nowdedn) {
        ArrayList arrBill = (ArrayList) objBill;
        AqreportHelperBean aqbean = new AqreportHelperBean();
        ArrayList column = null;
        String subCol = col.substring(3);
        int val = Integer.parseInt(subCol);
        int total = 0;
        ADDetailsHealperBean addbean = null;

        for (int i = 0; i < arrBill.size(); i++) {
            aqbean = (AqreportHelperBean) arrBill.get(i);
            if (arrBill.get(i) != null) {
                switch (val) {
                    case 3:
                        column = (ArrayList) aqbean.getCol3();
                        break;
                    case 4:
                        column = (ArrayList) aqbean.getCol4();
                        break;
                    case 5:
                        column = (ArrayList) aqbean.getCol5();
                        break;
                    case 6:
                        column = (ArrayList) aqbean.getCol6();
                        break;
                    case 7:
                        column = (ArrayList) aqbean.getCol7();
                        break;
                    case 8:
                        column = (ArrayList) aqbean.getCol8();
                        break;
                    case 9:
                        column = (ArrayList) aqbean.getCol9();
                        break;
                    case 10:
                        column = (ArrayList) aqbean.getCol10();
                        break;
                    case 11:
                        column = (ArrayList) aqbean.getCol11();
                        break;
                    case 12:
                        column = (ArrayList) aqbean.getCol12();
                        break;
                    case 13:
                        column = (ArrayList) aqbean.getCol13();
                        break;
                    case 14:
                        column = (ArrayList) aqbean.getCol14();
                        break;
                    case 15:
                        column = (ArrayList) aqbean.getCol15();
                        break;
                    case 16:
                        column = (ArrayList) aqbean.getCol16();
                        break;
                    case 17:
                        column = (ArrayList) aqbean.getCol17();
                        break;
                    case 18:
                        column = (ArrayList) aqbean.getCol18();
                        break;
                    case 19:
                        column = (ArrayList) aqbean.getCol19();
                        break;
                    case 20:
                        column = (ArrayList) aqbean.getCol20();
                        break;

                }
            }
            if (val == 3) {
                int basic = 0;
                if (aqbean.getBasic() != null) {
                    basic = Integer.parseInt(aqbean.getBasic());
                }
                total = total + basic;
            }

            for (int j = 0; j < column.size(); j++) {
                addbean = (ADDetailsHealperBean) column.get(j);
                if (addbean.getAdcode().equalsIgnoreCase(adcode)) {
                    if (nowdedn == null) {
                        total = total + addbean.getAdamt();
                    } else if (addbean.getNowdedn() != null && addbean.getNowdedn().equalsIgnoreCase(nowdedn)) {
                        total = total + addbean.getAdamt();
                    }
                }

            }
        }

        return total;
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

    private void printPageGrandTotal(DMPUtil dmpUtil, ArrayList originalList, int totalRecord) throws Exception {

        /*1ST ROW*/
        String fstRowCaption = "";

        fstRowCaption = StringUtils.rightPad("UNIT TOTAL :", 20);

        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + fstRowCaption + " "
                + StringUtils.leftPad(StringUtils.clean(AqFunctionalities.getColGrandTotal(originalList, "col3", "BASIC", null) + ""), 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col4", "DP", null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col5", "DA", null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col6", 0, null) + "", 5) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col7", 0, null) + "", 5) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col8", "GROSS PAY", null) + "", 8) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col9", "LIC", null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col10", "GPF", null) + "", 12) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col11", "PT", null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col12", "HRR", null) + "", 9) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col13", "HBA", "P") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col14", "MCA", "P") + "", 16) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col15", "VE", "P") + "", 13) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col16", "PA", null) + "", 11) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col17", 0, null) + "", 10) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col18", "OR", null) + "", 11) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col19", "TOTDEN", null) + "", 8) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col20", "NETPAY", null) + "", 8));

        /*2ND ROW*/
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.rightPad("(Column wise)", 20) + " "
                + StringUtils.leftPad(StringUtils.clean(AqFunctionalities.getColGrandTotal(originalList, "col3", "SP", null) + ""), 6) + " "
                + StringUtils.leftPad("", 6) + " " + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col5", "IR", null) + "", 6) + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col6", 1, null) + "", 6) + " "
                + //StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList,"col6",1,null)+"",0)+" "+
                StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col7", 1, null) + "", 5) + " "
                + StringUtils.leftPad("", 8) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col9", "PLI", null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col10", "GA", null) + "", 12) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col11", "IT", null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col12", "WRR", null) + "", 9) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col13", "HBA", "I") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col14", "MCA", "I") + "", 16) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col15", "VE", "I") + "", 13) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col16", "MAL", null) + "", 11) + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col17", 1, null) + "", 11) + " "
                + StringUtils.leftPad("", 10) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col18", "GISA", "P") + "", 11));
        /*3RD ROW*/
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.rightPad(totalRecord + "", 20) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col3", "GP", null) + "", 6) + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col6", 2, null) + "", 20) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col7", 2, null) + "", 5) + " "
                + StringUtils.leftPad("", 8) + " " + StringUtils.leftPad("", 6) + " " + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col10", "GPDD", null) + "", 12) + " "
                + StringUtils.leftPad("", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col12", "SWR", null) + "", 9) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col13", "SHBA", "P") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col14", "MOPA", "P") + "", 16) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col15", "BICA", "P") + "", 13) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col16", "TRDA", null) + "", 11) + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col17", 2, null) + "", 11) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col18", "CMPA", "P") + "", 11)
        );
        /*4TH ROW*/
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.leftPad("", 20) + " " + StringUtils.leftPad("", 20) + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col6", 3, null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col7", 3, null) + "", 5) + " "
                + StringUtils.leftPad("", 8) + " " + StringUtils.leftPad("", 6) + " " + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col10", "GPIR", null) + "", 12) + " "
                + StringUtils.leftPad("", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col12", "HC", null) + "", 9) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col13", "SHBA", "I") + "", 14) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col14", "MOPA", "I") + "", 16) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col15", "BICA", "I") + "", 13));
        /*5TH ROW*/

        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.leftPad("", 20) + " " + StringUtils.leftPad("", 20) + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col6", 4, null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col7", 4, null) + "", 5) + " "
                + StringUtils.leftPad("", 8) + " " + StringUtils.leftPad("", 6) + " " + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col10", "CPF", null) + "", 12) + " "
                + StringUtils.leftPad("", 6));
        /*6TH ROW*/
        dmpUtil.writeToFile(StringUtils.rightPad("", 4) + StringUtils.leftPad("", 20) + " " + StringUtils.leftPad("", 20) + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col6", 5, null) + "", 6) + " "
                + StringUtils.leftPad(AqFunctionalities.getColGrandTotal(originalList, "col7", 5, null) + "", 5));
        dmpUtil.writeToFile("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    public HashMap getPayAbstract(ArrayList aqlist, Connection con, String billno, int aqmonth, int aqyear) {
        HashMap payAbstract = new HashMap();
        int pay = 0;
        int da = 0;
        int hra = 0;
        int oa = 0;
        int dp = 0;
        AqreportHelperBean aqbean = null;
        ArrayList column3 = null;
        ArrayList column4 = null;
        ArrayList column5 = null;
        ArrayList column6 = null;
        ArrayList column7 = null;
        ADDetailsHealperBean addbean = null;
        Statement stamt = null;
        ResultSet res = null;
        try {
            String aqDTLS = "AQ_DTLS";

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(aqmonth, aqyear);
            stamt = con.createStatement();
            res = stamt.executeQuery("SELECT SUM(ad_amt)hra_amt FROM (select ad_amt,SCHEDULE from " + aqDTLS + " aq_dtls inner join (select aqsl_no from aq_mast where bill_no=" + billno + " AND aq_month=" + aqmonth + " AND aq_year=" + aqyear + " )aq_mast "
                    + "on aq_dtls.aqsl_no = aq_mast.aqsl_no) where SCHEDULE='HRA'");
            if (res.next()) {
                hra = res.getInt("hra_amt");
            }
            stamt = con.createStatement();
            res = stamt.executeQuery("SELECT SUM(ad_amt)dp_amt FROM (select ad_amt,ad_code from " + aqDTLS + " aq_dtls inner join (select aqsl_no from aq_mast where bill_no=" + billno + " AND aq_month=" + aqmonth + " AND aq_year=" + aqyear + " )aq_mast "
                    + "on aq_dtls.aqsl_no = aq_mast.aqsl_no) where ad_code='DP'");
            if (res.next()) {
                dp = res.getInt("dp_amt");
            }
            stamt = con.createStatement();
            res = stamt.executeQuery("SELECT SUM(ad_amt)da_amt FROM (select ad_amt,ad_code from " + aqDTLS + " aq_dtls inner join (select aqsl_no from aq_mast where bill_no=" + billno + " AND aq_month=" + aqmonth + " AND aq_year=" + aqyear + " )aq_mast "
                    + "on aq_dtls.aqsl_no = aq_mast.aqsl_no) where ad_code='DA'");
            if (res.next()) {
                da = res.getInt("da_amt");
            }
            stamt = con.createStatement();
            res = stamt.executeQuery("SELECT SUM(ad_amt)oa_amt FROM (select ad_amt,SCHEDULE,AD_TYPE,ad_code from " + aqDTLS + " aq_dtls inner join (select aqsl_no from aq_mast where bill_no=" + billno + " AND aq_month=" + aqmonth + " AND aq_year=" + aqyear + " )aq_mast "
                    + "on aq_dtls.aqsl_no = aq_mast.aqsl_no) where SCHEDULE='OA' AND AD_TYPE='A' AND ad_code != 'SP'");
            if (res.next()) {
                oa = res.getInt("oa_amt");
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            try {
                if (stamt != null) {
                    stamt.close();
                    stamt = null;
                }
                if (res != null) {
                    res.close();
                    res = null;
                }
            } catch (SQLException sqe) {
                sqe.printStackTrace();
            }
        }
        for (int i = 0; i < aqlist.size(); i++) {
            SectionWiseAqBean sectionwiseAq = (SectionWiseAqBean) aqlist.get(i);
            ArrayList arrBill = sectionwiseAq.getAqlistSectionWise();
            for (int k = 0; k < arrBill.size(); k++) {
                if (arrBill.get(k) != null) {
                    aqbean = (AqreportHelperBean) arrBill.get(k);
                    if (aqlist.get(i) != null) {
                        column3 = (ArrayList) aqbean.getCol3();
                        pay = pay + Integer.parseInt(aqbean.getBasic());
                        for (int j = 0; j < column3.size(); j++) {
                            addbean = (ADDetailsHealperBean) column3.get(j);
                            pay = pay + addbean.getAdamt();
                        }

                    }
                }
            }
        }
        payAbstract.put("pay", pay + "");
        payAbstract.put("dp", dp + "");
        payAbstract.put("da", da + "");
        payAbstract.put("hra", hra + "");
        payAbstract.put("oa", oa + "");

        return payAbstract;
    }

    private ArrayList getScheduleListWithADCode(Connection con, String billNo) throws Exception {

        //Statement st = null;
        ResultSet rs = null;
        ArrayList al = new ArrayList();
        Schedule sc = null;
        Statement st = null;
        try {
            String aqDTLS = "AQ_DTLS";
            aqDTLS = getAqDtlsTableName(billNo);
            st = con.createStatement();
            rs = st.executeQuery(" SELECT AQ_DTLS.SCHEDULE,NOW_DEDN,AD_AMT,BT_ID "
                    + "FROM (SELECT AD_CODE,SCHEDULE,DED_TYPE,NOW_DEDN,SUM(AD_AMT) AD_AMT,BT_ID FROM " + aqDTLS + " AQ_DTLS INNER JOIN  ("
                    + "SELECT AQ_MAST.AQSL_NO FROM AQ_MAST WHERE  AQ_MAST.BILL_NO='" + billNo + "')AQ_MAST "
                    + " ON AQ_DTLS.AQSL_NO = AQ_MAST.AQSL_NO WHERE AD_TYPE='D' and SCHEDULE != 'PVTL' and schedule != 'PVTD'"
                    + " GROUP BY AQ_DTLS.SCHEDULE,DED_TYPE,NOW_DEDN,AD_CODE,BT_ID ORDER BY AQ_DTLS.SCHEDULE)AQ_DTLS WHERE AD_AMT >0");
            //Schedule gpfsc = new Schedule();
            //gpfsc.setScheduleName(StringUtils.defaultString("GPF"));
            //al.add(gpfsc);
            while (rs.next()) {
                String schedule = rs.getString("SCHEDULE");
                /* if(schedule.equals("GA") || schedule.equals("GPF")){
                 Schedule gpfsctemp = (Schedule)al.get(0);
                 gpfsctemp.setObjectHead(StringUtils.defaultString(rs.getString("BT_ID")));
                 gpfsctemp.addSchAmount(rs.getInt("AD_AMT"));
                 }else*/
                if (schedule.equals("GPF")) {
                    sc = new Schedule();
                    sc.setScheduleName("GPF");
                    sc.setObjectHead(StringUtils.defaultString(rs.getString("BT_ID")));
                    sc.setSchAmount(rs.getString("AD_AMT"));
                } else if (schedule.equals("GA")) {
                    sc = new Schedule();
                    sc.setScheduleName("GA");
                    sc.setObjectHead(StringUtils.defaultString(rs.getString("BT_ID")));
                    sc.setSchAmount(rs.getString("AD_AMT"));
                } else if (schedule.equals("TPF") || schedule.equals("TPFGA")) {
                    sc = new Schedule();
                    sc.setScheduleName("TPF");
                    sc.setObjectHead(StringUtils.defaultString(rs.getString("BT_ID")));
                    sc.addSchAmount(rs.getInt("AD_AMT"));
                } else if (schedule.equals("CPF") || schedule.equals("NPSL")) {
                    sc = new Schedule();
                    sc.setScheduleName("CPF");
                    sc.setObjectHead(StringUtils.defaultString(rs.getString("BT_ID")));
                    sc.addSchAmount(rs.getInt("AD_AMT"));
                } else {
                    sc = new Schedule();
                    sc.setObjectHead(StringUtils.defaultString(rs.getString("BT_ID")));
                    if (rs.getString("NOW_DEDN") != null && !rs.getString("NOW_DEDN").equals("")) {
                        sc.setScheduleName(rs.getString("SCHEDULE") + " (" + rs.getString("NOW_DEDN") + ")");
                    } else {
                        sc.setScheduleName(rs.getString("SCHEDULE"));
                    }
                    sc.setSchAmount(rs.getString("AD_AMT"));
                    //al.add(sc);
                }
                al.add(sc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public HashMap getPayAbstract(ArrayList aqlist, String billno, int aqmonth, int aqyear) {
        HashMap payAbstract = new HashMap();
        int pay = 0;
        int da = 0;
        int hra = 0;
        int oa = 0;
        int dp = 0;
        AqreportHelperBean aqbean = null;
        ArrayList column3 = null;
        ArrayList column4 = null;
        ArrayList column5 = null;
        ArrayList column6 = null;
        ArrayList column7 = null;
        ADDetailsHealperBean addbean = null;
        Connection con = null;
        Statement stamt = null;
        ResultSet res = null;
        try {
            String aqDTLS = "AQ_DTLS";
            aqDTLS = getAqDtlsTableName(billno);

            con = dataSource.getConnection();
            stamt = con.createStatement();
            res = stamt.executeQuery("SELECT SUM(ad_amt)hra_amt FROM (select ad_amt,SCHEDULE from " + aqDTLS + " aq_dtls inner join (select aqsl_no from aq_mast where bill_no=" + billno + " AND aq_month=" + aqmonth + " AND aq_year=" + aqyear + " )aq_mast "
                    + "on aq_dtls.aqsl_no = aq_mast.aqsl_no) TAB where SCHEDULE='HRA'");
            if (res.next()) {
                hra = res.getInt("hra_amt");
            }
            stamt = con.createStatement();
            res = stamt.executeQuery("SELECT SUM(ad_amt)dp_amt FROM (select ad_amt,ad_code from " + aqDTLS + " aq_dtls inner join (select aqsl_no from aq_mast where bill_no=" + billno + " AND aq_month=" + aqmonth + " AND aq_year=" + aqyear + " )aq_mast "
                    + "on aq_dtls.aqsl_no = aq_mast.aqsl_no) TAB where ad_code='DP'");
            if (res.next()) {
                dp = res.getInt("dp_amt");
            }
            stamt = con.createStatement();
            res = stamt.executeQuery("SELECT SUM(ad_amt)da_amt FROM (select ad_amt,ad_code from " + aqDTLS + " aq_dtls inner join (select aqsl_no from aq_mast where bill_no=" + billno + " AND aq_month=" + aqmonth + " AND aq_year=" + aqyear + " )aq_mast "
                    + "on aq_dtls.aqsl_no = aq_mast.aqsl_no) TAB where ad_code='DA'");
            if (res.next()) {
                da = res.getInt("da_amt");
            }
            stamt = con.createStatement();
            res = stamt.executeQuery("SELECT SUM(ad_amt)oa_amt FROM (select ad_amt,SCHEDULE,AD_TYPE,ad_code from " + aqDTLS + " aq_dtls inner join (select aqsl_no from aq_mast where bill_no=" + billno + " AND aq_month=" + aqmonth + " AND aq_year=" + aqyear + " )aq_mast "
                    + "on aq_dtls.aqsl_no = aq_mast.aqsl_no) TAB where SCHEDULE='OA' AND AD_TYPE='A' AND ad_code != 'SP'");
            if (res.next()) {
                oa = res.getInt("oa_amt");
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        for (int i = 0; i < aqlist.size(); i++) {
            SectionWiseAqBean sectionwiseAq = (SectionWiseAqBean) aqlist.get(i);
            ArrayList arrBill = sectionwiseAq.getAqlistSectionWise();
            for (int k = 0; k < arrBill.size(); k++) {
                if (arrBill.get(k) != null) {
                    aqbean = (AqreportHelperBean) arrBill.get(k);
                    if (aqlist.get(i) != null) {
                        column3 = (ArrayList) aqbean.getCol3();
                        pay = pay + Integer.parseInt(aqbean.getBasic());
                        for (int j = 0; j < column3.size(); j++) {
                            addbean = (ADDetailsHealperBean) column3.get(j);
                            pay = pay + addbean.getAdamt();
                        }

                    }
                }
            }
        }
        payAbstract.put("pay", pay + "");
        payAbstract.put("dp", dp + "");
        payAbstract.put("da", da + "");
        payAbstract.put("hra", hra + "");
        payAbstract.put("oa", oa + "");

        return payAbstract;
    }

    @Override
    public ArrayList getSectionWiseBillDtls(String billno, int mon, int year, String formatType, BillConfigObj billConfigObj, String empType) {
        String section = "";

        int secslno = 0;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        SectionWiseAqBean objBill = null;
        ArrayList a1 = new ArrayList();
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            String sqlqryforsection = "SELECT SEC_SL_NO,SECTION,EMP_TYPE FROM AQ_MAST WHERE  BILL_NO='" + billno + "' AND AQ_MONTH=" + mon + " AND AQ_YEAR=" + year + "  GROUP BY SECTION,SEC_SL_NO,EMP_TYPE ORDER BY SEC_SL_NO";
            rs = st.executeQuery(sqlqryforsection);
            while (rs.next()) {
                objBill = null;
                section = rs.getString("SECTION");
                secslno = rs.getInt("SEC_SL_NO");
                empType = rs.getString("EMP_TYPE");
                if (empType != null && !empType.equals("")) {
                    if (formatType.equals("f2") && (empType.equals("R") || empType.equals("S"))) {
                        objBill = getAqBillDetailsF2(billno, mon, year, section, secslno, billConfigObj);
                    } else if (formatType.equals("f1") && (empType.equals("R") || empType.equals("S"))) {
                        objBill = getAqBillDetails(billno, mon, year, section, secslno, billConfigObj);
                    } else if (formatType.equals("f1") && (empType.equals("C") || empType.equals("S"))) {
                        objBill = getAqBillDetailsContractual(billno, mon, year, section, secslno, billConfigObj);
                    }
                    objBill.setSectionname(section);
                    a1.add(objBill);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return a1;
    }

    @Override
    public BillConfigObj getBillConfig(String billno) {
        BillConfigObj objBill = new BillConfigObj();
        ResultSet rs = null;
        Statement st = null;
        String col6Desc[] = null;
        String col7Desc[] = null;
        String col16Desc[] = null;
        String col17Desc[] = null;
        String col18Desc[] = null;
        String offCode = "";
        String billgroupId = "";
        Connection con = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("select off_code, bill_group_id from bill_mast where bill_no='" + billno + "'");
            if (rs.next()) {
                offCode = rs.getString("off_code");
                billgroupId = rs.getString("bill_group_id");
            }

            DataBaseFunctions.closeSqlObjects(rs, st);
            st = con.createStatement();
            rs = st.executeQuery("SELECT CONFIGURED_LVL,show_in_aquitance FROM BILL_GROUP_MASTER WHERE BILL_GROUP_ID='" + billgroupId + "'");
            if (rs.next()) {
                String level = rs.getString("CONFIGURED_LVL");
                objBill.setCol6List(getColConfiguredDtata(offCode, billgroupId, 6, level));
                objBill.setCol7List(getColConfiguredDtata(offCode, billgroupId, 7, level));
                objBill.setCol16List(getColConfiguredDtata(offCode, billgroupId, 16, level));
                objBill.setCol17List(getColConfiguredDtata(offCode, billgroupId, 17, level));
                objBill.setCol18List(getColConfiguredDtata(offCode, billgroupId, 18, level));
                objBill.setNotToPrintInAquitance(rs.getString("show_in_aquitance"));
            }
            DataBaseFunctions.closeSqlObjects(rs, st);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return objBill;
    }

    @Override
    public String[] getColConfiguredDtata(String offCode, String billgroupId, int colNumber, String configLevel) {
        ResultSet rs = null;
        Statement st = null;
        String colDesc[] = null;
        int coli = 0;
        String sql = "";
        Connection con = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();

            if (configLevel != null && !configLevel.equals("")) {
                if (configLevel.equalsIgnoreCase("B")) {
                    sql = "SELECT AD_CODE_NAME,ROW_NUMBER FROM (SELECT AD_CODE,ROW_NUMBER FROM BILL_CONFIGURATION WHERE OFF_CODE='" + offCode + "' AND BILL_GROUP_ID=" + billgroupId + " AND COL_NUMBER=" + colNumber + ") BILL_CONFIGURATION "
                            + "INNER JOIN G_AD_LIST ON "
                            + "BILL_CONFIGURATION.AD_CODE=G_AD_LIST.AD_CODE ORDER BY ROW_NUMBER";
                } else {
                    sql = "SELECT AD_CODE_NAME,ROW_NUMBER FROM (SELECT AD_CODE,ROW_NUMBER FROM BILL_CONFIGURATION WHERE OFF_CODE='" + offCode + "' AND COL_NUMBER=" + colNumber + " ) BILL_CONFIGURATION "
                            + "INNER JOIN G_AD_LIST ON "
                            + "BILL_CONFIGURATION.AD_CODE=G_AD_LIST.AD_CODE ORDER BY ROW_NUMBER";
                }
            }
            if (sql != null && !sql.equals("")) {
                rs = st.executeQuery(sql);

                while (rs.next()) {
                    if (coli == 0) {
                        colDesc = new String[8];
                    }
                    colDesc[coli] = rs.getString("AD_CODE_NAME");
                    coli++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return colDesc;
    }

    @Override
    public SectionWiseAqBean getAqBillDetailsF2(String billno, int mon, int year, String section, int secslno, BillConfigObj billConfigObj) {
        Connection con = null;
        Statement st = null;
        Statement st1 = null;
        Statement st2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        AqreportHelperBean aqbean = null;
        ArrayList aqlist = new ArrayList();
        int colNo = 0;
        section = StringUtils.replace(section, "'", "''");
        SectionWiseAqBean secWiseAqBean = new SectionWiseAqBean();
        String sqlQuery = "";
        try {
            con = dataSource.getConnection();

            String aqDTLS = "AQ_DTLS";
            aqDTLS = getAqDtlsTableName(billno);

            st = con.createStatement();
            st1 = con.createStatement();
            st2 = con.createStatement();

            st = con.createStatement();
            if (billConfigObj.getNotToPrintInAquitance() != null && billConfigObj.getNotToPrintInAquitance().equals("N")) {
                sqlQuery = "SELECT * FROM (SELECT AQSL_NO,EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,SPC_ORD_NO,SPC_ORD_DATE,GPF_ACC_NO,ACCT_TYPE,EMP_CODE,post_sl_no,SEC_SL_NO "
                        + "FROM AQ_MAST WHERE AQ_MONTH=" + mon + " AND AQ_YEAR=" + year + " AND BILL_NO=" + billno + " AND SECTION='" + section + "' AND SEC_SL_NO=" + secslno + " AND DEP_CODE != '11' AND EMP_CODE IS NOT NULL) AQ_MAST "
                        + "LEFT OUTER JOIN (SELECT EMP_ID,BRASS_NO,CUR_CADRE_CODE,IF_GPF_ASSUMED FROM EMP_MAST)EMP_MAST ON AQ_MAST.EMP_CODE = EMP_MAST.EMP_ID "
                        + "LEFT OUTER JOIN (SELECT CADRE_CODE,CADRE_ABBR FROM G_CADRE)G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE order by POST_SL_NO";
            } else {
                sqlQuery = "SELECT * FROM (SELECT AQSL_NO,EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,SPC_ORD_NO,SPC_ORD_DATE,GPF_ACC_NO,ACCT_TYPE,EMP_CODE,post_sl_no,SEC_SL_NO "
                        + "FROM AQ_MAST WHERE AQ_MONTH=" + mon + " AND AQ_YEAR=" + year + " AND BILL_NO=" + billno + " AND SECTION='" + section + "' AND SEC_SL_NO=" + secslno + " AND DEP_CODE != '11') AQ_MAST "
                        + "LEFT OUTER JOIN (SELECT EMP_ID,BRASS_NO,CUR_CADRE_CODE,IF_GPF_ASSUMED FROM EMP_MAST)EMP_MAST ON AQ_MAST.EMP_CODE = EMP_MAST.EMP_ID "
                        + "LEFT OUTER JOIN (SELECT CADRE_CODE,CADRE_ABBR FROM G_CADRE)G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE order by POST_SL_NO";
            }
            rs = st.executeQuery(sqlQuery);
            int i = 0;
            while (rs.next()) {
                i++;
                aqbean = new AqreportHelperBean();
                aqbean.setSlno("" + i);
                if (rs.getString("EMP_NAME") != null && !rs.getString("EMP_NAME").equals("")) {
                    if (rs.getString("BRASS_NO") != null && !rs.getString("BRASS_NO").equals("")) {
                        aqbean.setEmpname(rs.getString("BRASS_NO") + " " + rs.getString("EMP_NAME"));
                    } else {
                        aqbean.setEmpname(rs.getString("EMP_NAME"));
                    }
                } else {
                    aqbean.setEmpname("VACANT");
                }

                aqbean.setCadreabbr(rs.getString("CADRE_ABBR"));
                aqbean.setDesg(rs.getString("CUR_DESG"));
                aqbean.setPayscale(rs.getString("PAY_SCALE"));
                aqbean.setAccNo(rs.getString("BANK_ACC_NO"));
                aqbean.setAcctype(rs.getString("ACCT_TYPE"));
                aqbean.setBasic(rs.getString("CUR_BASIC"));
                if (rs.getString("SPC_ORD_NO") != null && !rs.getString("SPC_ORD_NO").equals("")) {
                    aqbean.setOrdno(rs.getString("SPC_ORD_NO"));
                } else {
                    aqbean.setOrdno("");
                }
                aqbean.setOrddate(CommonFunctions.getFormattedOutputDate1(rs.getDate("SPC_ORD_DATE")));
                aqbean.setGpfacct(rs.getString("GPF_ACC_NO"));
                if (rs.getString("IF_GPF_ASSUMED") != null && !rs.getString("IF_GPF_ASSUMED").equals("")) {
                    if (rs.getString("IF_GPF_ASSUMED").equalsIgnoreCase("Y")) {
                        aqbean.setGpfacct("");
                    }
                } else {
                    aqbean.setGpfacct("");
                }
                aqbean.setGpfNoAssumed(rs.getString("IF_GPF_ASSUMED"));
                String aqslno = rs.getString("AQSL_NO");
                String queryClump = "select * from (select cnt,REP_COL,tmep4.AD_CODE,AD_AMT,SCHEDULE,tmep4.NOW_DEDN,AD_TYPE,sl_no,REF_DESC from(select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO='" + rs.getString("AQSL_NO") + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt = 1)tmep4 "
                        + "left outer join (SELECT AD_CODE,REF_DESC,NOW_DEDN FROM " + aqDTLS + " WHERE AQSL_NO='" + rs.getString("AQSL_NO") + "') temp2 on "
                        + "tmep4.ad_code = temp2.ad_code and tmep4.now_dedn = temp2.now_dedn "
                        + "union "
                        + "select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no,'' ::text from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO='" + rs.getString("AQSL_NO") + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt > 1 ) as TAB order by SL_NO ";

                rs1 = st1.executeQuery(queryClump);

                while (rs1.next()) {
                    colNo = rs1.getInt("REP_COL");
                    String refdesc = "";
                    String adcode = "";
                    String nowdedn = "";

                    switch (colNo) {
                        case 3:
                            aqbean.setCol3(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 4:
                            aqbean.setCol4(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 5:
                            aqbean.setCol5(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 6:
                            aqbean.setCol6(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol6List());
                            break;
                        case 7:
                            aqbean.setCol7(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol7List());
                            break;
                        case 9:
                            aqbean.setCol9(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 10:
                            if ((aqbean.getAcctype()).equals("GPF") && (rs1.getString("AD_CODE").equals("GPF") || rs1.getString("AD_CODE").equals("GA") || rs1.getString("AD_CODE").equals("GPDD") || rs1.getString("AD_CODE").equals("GPIR"))) {
                                aqbean.setCol10(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            } else if ((aqbean.getAcctype()).equals("PRAN") && rs1.getString("AD_CODE").equals("CPF")) {
                                aqbean.setCol10(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            } else if ((aqbean.getAcctype()).equals("TPF") && rs1.getString("AD_CODE").equals("TPF")) {
                                aqbean.setCol10(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            }
                            break;
                        case 11:
                            aqbean.setCol11(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 12:
                            aqbean.setCol12(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 13:
                            refdesc = rs1.getString("REF_DESC");
                            adcode = rs1.getString("AD_CODE");
                            nowdedn = rs1.getString("NOW_DEDN");
                            if ((refdesc == null || refdesc.equals("")) && rs1.getInt("AD_AMT") > 0) {
                                refdesc = getRefDesc(adcode, nowdedn, aqslno, billno);
                                aqbean.setCol13(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), refdesc);
                            } else {
                                aqbean.setCol13(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            }
                            break;
                        case 14:
                            aqbean.setCol14(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 15:
                            aqbean.setCol15(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 16:
                            aqbean.setCol16(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol16List());
                            break;
                        case 17:
                            aqbean.setCol17(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol17List());
                            break;
                        case 18:
                            aqbean.setCol18(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol18List());
                            break;
                    }
                }
                int privatededuction = getPrivateDeduction(rs.getString("AQSL_NO"), billno);
                int privateloan = getPrivateLoan(rs.getString("AQSL_NO"), billno);
                int grosspay = getGrossPay(rs.getString("AQSL_NO"), billno);
                int totaldedn = getTotalDedn(rs.getString("AQSL_NO"), billno);
                aqbean.setCol8("GROSS PAY", getGrossPay(rs.getString("AQSL_NO"), billno), null, null, null);
                aqbean.setCol19("TOTDEN", getTotalDedn(rs.getString("AQSL_NO"), billno), null, null, null);
                aqbean.setCol19("NETPAY", grosspay - totaldedn, null, null, null);

                aqbean.setCol20("PVTDED", privatededuction, null, null, null);
                aqbean.setCol20("BANKLOAN", privateloan, null, null, null);
                aqbean.setCol20("NETBALANCE", grosspay - (totaldedn + privatededuction + privateloan), null, null, null);

                aqlist.add(aqbean);
                Collections.sort(aqbean.getCol6());
                Collections.sort(aqbean.getCol7());
                Collections.sort(aqbean.getCol16());
                Collections.sort(aqbean.getCol17());
                Collections.sort(aqbean.getCol18());

            }
            secWiseAqBean.setAqlistSectionWise(aqlist);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(rs2, st2);
            DataBaseFunctions.closeSqlObjects(rs1, st1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return secWiseAqBean;
    }

    @Override
    public String getADCodeHead(String billNo, String ad, int aqMonth, int aqYear) {

        Statement st = null;
        ResultSet rs = null;
        String head = "";
        Connection con = null;
        try {
            con = dataSource.getConnection();
            String aqDTLS = "AQ_DTLS";
            aqDTLS = getAqDtlsTableName(billNo);
            st = con.createStatement();
            String sql = "Select BT_ID from( (Select AQ_MAST.AQSL_NO from AQ_MAST where AQ_MAST.BILL_NO = '" + billNo + "' AND AQ_MAST.aq_month=" + aqMonth + " AND AQ_MAST.aq_year=" + aqYear + ")"
                    + " AQ_MAST inner join"
                    + " (Select AQSL_NO,AD_AMT,BT_ID from " + aqDTLS + " where  AD_CODE ='" + ad + "') AQ_DTLS"
                    + " on AQ_MAST.AQSL_NO = AQ_DTLS.AQSL_NO ) GROUP BY BT_ID";
            rs = st.executeQuery(sql);
            if (rs.next()) {
                head = rs.getString("BT_ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return head;
    }

    @Override
    public int getGPFAmount(String billId, String btId, int aqMonth, int aqYear) {
        ResultSet rs = null;
        Statement st = null;
        int sumOfAmt = 0;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            String aqDTLS = "AQ_DTLS";
            aqDTLS = getAqDtlsTableName(billId);
            rs = st.executeQuery("SELECT SUM(AD_AMT) amt FROM (SELECT AD_AMT,AD_CODE,BT_ID FROM(SELECT AQSL_NO FROM AQ_MAST WHERE BILL_NO=" + billId + " and aq_year=" + aqYear + " and aq_month=" + aqMonth + ")AQ_MAST "
                    + "INNER JOIN " + aqDTLS + " ON AQ_MAST.AQSL_NO = " + aqDTLS + ".AQSL_NO) T1 WHERE (AD_CODE='GPF' OR AD_CODE='GA') AND BT_ID='" + btId + "'");
            if (rs.next()) {
                sumOfAmt = rs.getInt("amt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return sumOfAmt;
    }

    public SectionWiseAqBean getAqBillDetails(String billno, int mon, int year, String section, int secslno, BillConfigObj billConfigObj) {
        Connection con = null;
        Statement st = null;
        PreparedStatement pstamt = null;
        Statement st1 = null;
        Statement st2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        AqreportHelperBean aqbean = null;
        ArrayList aqlist = new ArrayList();
        int colNo = 0;
        section = StringUtils.replace(section, "'", "''");
        SectionWiseAqBean objBill = new SectionWiseAqBean();
        int col7sum = 0;
        String sql = "";

        String aqDTLS = "AQ_DTLS";
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            st1 = con.createStatement();
            st2 = con.createStatement();

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(mon, year);

            if (billConfigObj.getNotToPrintInAquitance() != null && billConfigObj.getNotToPrintInAquitance().equals("N")) {
                sql = "SELECT * FROM (SELECT AQSL_NO,EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,SPC_ORD_NO,SPC_ORD_DATE,GPF_ACC_NO,ACCT_TYPE,EMP_CODE,post_sl_no,SEC_SL_NO "
                        + "FROM AQ_MAST WHERE AQ_MONTH='" + mon + "' AND AQ_YEAR='" + year + "' AND BILL_NO='" + billno + "' AND SECTION='" + section + "' AND SEC_SL_NO='" + secslno + "' AND DEP_CODE != '11' AND EMP_CODE IS NOT NULL) AQ_MAST "
                        + "LEFT OUTER JOIN (SELECT EMP_ID,BRASS_NO,CUR_CADRE_CODE,IF_GPF_ASSUMED FROM EMP_MAST)EMP_MAST ON AQ_MAST.EMP_CODE = EMP_MAST.EMP_ID "
                        + "LEFT OUTER JOIN (SELECT CADRE_CODE,CADRE_ABBR FROM G_CADRE)G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE order by POST_SL_NO";
            } else {
                sql = "SELECT * FROM (SELECT AQSL_NO,EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,SPC_ORD_NO,SPC_ORD_DATE,GPF_ACC_NO,ACCT_TYPE,EMP_CODE,post_sl_no,SEC_SL_NO "
                        + "FROM AQ_MAST WHERE AQ_MONTH='" + mon + "' AND AQ_YEAR='" + year + "' AND BILL_NO='" + billno + "' AND SECTION='" + section + "' AND SEC_SL_NO='" + secslno + "' AND DEP_CODE != '11') AQ_MAST "
                        + "LEFT OUTER JOIN (SELECT EMP_ID,BRASS_NO,CUR_CADRE_CODE,IF_GPF_ASSUMED FROM EMP_MAST)EMP_MAST ON AQ_MAST.EMP_CODE = EMP_MAST.EMP_ID "
                        + "LEFT OUTER JOIN (SELECT CADRE_CODE,CADRE_ABBR FROM G_CADRE)G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE order by POST_SL_NO";
            }
            pstamt = con.prepareStatement(sql);

            rs = pstamt.executeQuery();
            int i = 0;

            while (rs.next()) {
                i++;
                aqbean = new AqreportHelperBean();
                aqbean.setSlno("" + i);
                if (rs.getString("EMP_NAME") != null && !rs.getString("EMP_NAME").equals("")) {
                    if (rs.getString("BRASS_NO") != null && !rs.getString("BRASS_NO").equals("")) {
                        aqbean.setEmpname(rs.getString("BRASS_NO") + " " + rs.getString("EMP_NAME"));
                    } else {
                        aqbean.setEmpname(rs.getString("EMP_NAME"));
                    }
                } else {
                    aqbean.setEmpname("VACANT");
                }
                aqbean.setCadreabbr(rs.getString("CADRE_ABBR"));
                aqbean.setDesg(rs.getString("CUR_DESG"));
                aqbean.setPayscale(rs.getString("PAY_SCALE"));
                aqbean.setAccNo(rs.getString("BANK_ACC_NO"));
                aqbean.setAcctype(rs.getString("ACCT_TYPE"));
                aqbean.setBasic(rs.getString("CUR_BASIC"));
                if (rs.getString("SPC_ORD_NO") != null && !rs.getString("SPC_ORD_NO").equals("")) {
                    aqbean.setOrdno(rs.getString("SPC_ORD_NO"));
                } else {
                    aqbean.setOrdno("");
                }
                aqbean.setOrddate(CommonFunctions.getFormattedOutputDate1(rs.getDate("SPC_ORD_DATE")));
                aqbean.setGpfacct(rs.getString("GPF_ACC_NO"));
                if (rs.getString("IF_GPF_ASSUMED") != null && !rs.getString("IF_GPF_ASSUMED").equals("")) {
                    if (rs.getString("IF_GPF_ASSUMED").equalsIgnoreCase("Y")) {
                        aqbean.setGpfacct("");
                    }
                } else {
                    aqbean.setGpfacct("");
                }
                aqbean.setGpfNoAssumed(rs.getString("IF_GPF_ASSUMED"));
                String aqslno = rs.getString("AQSL_NO");
                String queryClump = "select * from (select cnt,REP_COL,tmep4.AD_CODE,AD_AMT,SCHEDULE,tmep4.NOW_DEDN,AD_TYPE,sl_no,REF_DESC from(select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt = 1)tmep4 "
                        + "left outer join (SELECT AD_CODE,REF_DESC,NOW_DEDN FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp2 on "
                        + "tmep4.ad_code = temp2.ad_code and tmep4.now_dedn = temp2.now_dedn "
                        + "union "
                        + "select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no,'' ::text from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt > 1 ) as TAB order by SL_NO ";

                rs1 = st1.executeQuery(queryClump);

                while (rs1.next()) {
                    colNo = rs1.getInt("REP_COL");
                    String refdesc = "";
                    String adcode = "";
                    String nowdedn = "";
                    switch (colNo) {
                        case 3:
                            aqbean.setCol3(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 4:
                            aqbean.setCol4(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 5:
                            aqbean.setCol5(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 6:

                            aqbean.setCol6(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol6List());
                            break;
                        case 7:

                            aqbean.setCol7(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol7List());
                            break;
                        case 9:
                            aqbean.setCol9(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 10:
                            if ((aqbean.getAcctype()).equals("GPF") && (rs1.getString("AD_CODE").equals("GPF") || rs1.getString("AD_CODE").equals("GA") || rs1.getString("AD_CODE").equals("GPDD") || rs1.getString("AD_CODE").equals("GPIR"))) {
                                aqbean.setCol10(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            } else if ((aqbean.getAcctype()).equals("PRAN") && rs1.getString("AD_CODE").equals("CPF")) {
                                aqbean.setCol10(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            } else if ((aqbean.getAcctype()).equals("TPF") && (rs1.getString("AD_CODE").equals("TPF") || rs1.getString("AD_CODE").equals("TPFGA"))) {
                                aqbean.setCol10(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            }
                            break;
                        case 11:
                            aqbean.setCol11(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 12:
                            aqbean.setCol12(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 13:
                            refdesc = rs1.getString("REF_DESC");
                            adcode = rs1.getString("AD_CODE");
                            nowdedn = rs1.getString("NOW_DEDN");
                            if ((refdesc == null || refdesc.equals("")) && rs1.getInt("AD_AMT") > 0) {
                                refdesc = getRefDesc(adcode, nowdedn, aqslno, billno);
                                aqbean.setCol13(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), refdesc);
                            } else {
                                aqbean.setCol13(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            }
                            break;
                        case 14:
                            refdesc = rs1.getString("REF_DESC");
                            adcode = rs1.getString("AD_CODE");
                            nowdedn = rs1.getString("NOW_DEDN");
                            if ((refdesc == null || refdesc.equals("")) && rs1.getInt("AD_AMT") > 0) {
                                refdesc = getRefDesc(adcode, nowdedn, aqslno, billno);
                                aqbean.setCol14(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), refdesc);
                            } else {
                                aqbean.setCol14(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            }
                            break;
                        case 15:
                            aqbean.setCol15(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 16:
                            aqbean.setCol16(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol16List());
                            break;
                        case 17:
                            if (aqslno.equals("21910000008647_2_2015_2")) {
                            }
                            aqbean.setCol17(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol17List());
                            break;
                        case 18:
                            aqbean.setCol18(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol18List());
                            break;
                    }
                }
                aqbean.setCol8("GROSS PAY", getGrossPay(rs.getString("AQSL_NO"), billno), null, null, null);
                aqbean.setCol19("TOTDEN", getTotalDedn(rs.getString("AQSL_NO"), billno), null, null, null);
                aqbean.setCol20("NETPAY", getGrossPay(rs.getString("AQSL_NO"), billno) - getTotalDedn(rs.getString("AQSL_NO"), billno), null, null, null);
                aqlist.add(aqbean);
                Collections.sort(aqbean.getCol6());
                Collections.sort(aqbean.getCol7());
                Collections.sort(aqbean.getCol16());
                Collections.sort(aqbean.getCol17());
                Collections.sort(aqbean.getCol18());
            }

            objBill.setAqlistSectionWise(aqlist);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(rs2, st2);
            DataBaseFunctions.closeSqlObjects(rs1, st1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return objBill;
    }

    @Override
    public SectionWiseAqBean getAqBillDetailsContractual(String billno, int mon, int year, String section, int secslno, BillConfigObj billConfigObj) {
        Connection con = null;
        Statement st = null;
        Statement st1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String aqslno = null;
        int colNo = 0;
        PreparedStatement pstamt = null;
        AqreportHelperBean aqbean = null;
        ArrayList aqlist = new ArrayList();
        SectionWiseAqBean objBill = new SectionWiseAqBean();
        try {
            String aqDTLS = "AQ_DTLS";
            aqDTLS = getAqDtlsTableName(billno);
            con = dataSource.getConnection();
            st = con.createStatement();
            pstamt = con.prepareStatement("SELECT * FROM (SELECT AQSL_NO,EMP_NAME,CUR_DESG,PAY_SCALE,BANK_ACC_NO,CUR_BASIC,SPC_ORD_NO,SPC_ORD_DATE,GPF_ACC_NO,ACCT_TYPE,EMP_CODE,post_sl_no,SEC_SL_NO "
                    + "FROM AQ_MAST WHERE AQ_MONTH=? AND AQ_YEAR=? AND BILL_NO=? AND SECTION=? AND SEC_SL_NO=? AND DEP_CODE != '11') AQ_MAST "
                    + "LEFT OUTER JOIN (SELECT EMP_ID,CUR_CADRE_CODE,IF_GPF_ASSUMED FROM EMP_MAST)EMP_MAST ON AQ_MAST.EMP_CODE = EMP_MAST.EMP_ID "
                    + "LEFT OUTER JOIN (SELECT CADRE_CODE,CADRE_ABBR FROM G_CADRE)G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE order by POST_SL_NO");

            pstamt.setInt(1, mon);
            pstamt.setInt(2, year);
            pstamt.setInt(3, Integer.parseInt(billno));
            pstamt.setString(4, section);
            pstamt.setInt(5, secslno);
            rs = pstamt.executeQuery();
            int i = 0;
            while (rs.next()) {
                st1 = con.createStatement();
                i++;

                aqbean = new AqreportHelperBean();
                aqbean.setSlno("" + i);
                if (rs.getString("EMP_NAME") != null && !rs.getString("EMP_NAME").equals("")) {
                    aqbean.setEmpname(rs.getString("EMP_NAME"));
                } else {
                    aqbean.setEmpname("VACANT");
                }
                aqbean.setCadreabbr(rs.getString("CADRE_ABBR"));
                aqbean.setDesg(rs.getString("CUR_DESG"));
                aqbean.setPayscale(rs.getString("PAY_SCALE"));
                aqbean.setAccNo(rs.getString("BANK_ACC_NO"));
                aqbean.setAcctype(rs.getString("ACCT_TYPE"));
                aqbean.setBasic(rs.getString("CUR_BASIC"));
                aqslno = rs.getString("AQSL_NO");
                String queryClump = "select * from (select cnt,REP_COL,tmep4.AD_CODE,AD_AMT,SCHEDULE,tmep4.NOW_DEDN,AD_TYPE,sl_no,REF_DESC from(select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt = 1)tmep4 "
                        + "left outer join (SELECT AD_CODE,REF_DESC,NOW_DEDN FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp2 on "
                        + "tmep4.ad_code = temp2.ad_code and tmep4.now_dedn = temp2.now_dedn "
                        + "union "
                        + "select * from (SELECT count(*) cnt,REP_COL,AD_CODE,SUM(AD_AMT)AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no,''::TEXT from "
                        + "(SELECT REP_COL,AD_CODE,AD_AMT,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "') temp group by REP_COL,AD_CODE,SCHEDULE,NOW_DEDN,AD_TYPE,sl_no )temp1 where cnt > 1 ) final_tab order by SL_NO ";
                rs1 = st1.executeQuery(queryClump);
                while (rs1.next()) {
                    colNo = rs1.getInt("REP_COL");
                    String refdesc = "";
                    String adcode = "";
                    String nowdedn = "";
                    switch (colNo) {
                        case 3:
                            aqbean.setCol3(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        //                            case 4:
                        //                                aqbean.setCol4(rs1.getString("AD_CODE"),rs1.getInt("AD_AMT"),rs1.getString("SCHEDULE"),rs1.getString("NOW_DEDN"),rs1.getString("REF_DESC"));
                        //                                break;
                        case 5:
                            aqbean.setCol5(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 6:
                            aqbean.setCol6(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol6List());
                            break;
                        case 7:
                            aqbean.setCol7(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol7List());
                            break;
                        case 9:
                            aqbean.setCol9(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 10:
                            if ((aqbean.getAcctype()).equals("GPF") && (rs1.getString("AD_CODE").equals("GPF") || rs1.getString("AD_CODE").equals("GA") || rs1.getString("AD_CODE").equals("GPDD") || rs1.getString("AD_CODE").equals("GPIR"))) {
                                aqbean.setCol10(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            } else if ((aqbean.getAcctype()).equals("PRAN") && rs1.getString("AD_CODE").equals("CPF")) {
                                aqbean.setCol10(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            } else if ((aqbean.getAcctype()).equals("TPF") && (rs1.getString("AD_CODE").equals("TPF") || rs1.getString("AD_CODE").equals("TPFGA"))) {
                                aqbean.setCol10(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            }
                            break;
                        case 11:
                            aqbean.setCol11(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 12:
                            aqbean.setCol12(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 13:
                            refdesc = rs1.getString("REF_DESC");
                            adcode = rs1.getString("AD_CODE");
                            nowdedn = rs1.getString("NOW_DEDN");
                            if ((refdesc == null || refdesc.equals("")) && rs1.getInt("AD_AMT") > 0) {
                                refdesc = getRefDesc(adcode, nowdedn, aqslno, billno);
                                aqbean.setCol13(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), refdesc);
                            } else {
                                aqbean.setCol13(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            }
                            break;
                        case 14:
                            refdesc = rs1.getString("REF_DESC");
                            adcode = rs1.getString("AD_CODE");
                            nowdedn = rs1.getString("NOW_DEDN");
                            if ((refdesc == null || refdesc.equals("")) && rs1.getInt("AD_AMT") > 0) {
                                refdesc = getRefDesc(adcode, nowdedn, aqslno, billno);
                                aqbean.setCol14(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), refdesc);
                            } else {
                                aqbean.setCol14(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            }
                            break;
                        case 15:
                            aqbean.setCol15(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"));
                            break;
                        case 16:
                            aqbean.setCol16(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol16List());
                            break;
                        case 17:
                            aqbean.setCol17(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol17List());
                            break;
                        case 18:
                            aqbean.setCol18(rs1.getString("AD_CODE"), rs1.getInt("AD_AMT"), rs1.getString("SCHEDULE"), rs1.getString("NOW_DEDN"), rs1.getString("REF_DESC"), billConfigObj.getCol18List());
                            break;
                    }
                }
                aqbean.setCol8("GROSS PAY", getGrossPay(rs.getString("AQSL_NO"), billno), null, null, null);
                aqbean.setCol19("TOTDEN", getTotalDedn(rs.getString("AQSL_NO"), billno), null, null, null);
                aqbean.setCol20("NETPAY", getGrossPay(rs.getString("AQSL_NO"), billno) - getTotalDedn(rs.getString("AQSL_NO"), billno), null, null, null);
                aqlist.add(aqbean);
                Collections.sort(aqbean.getCol6());
                Collections.sort(aqbean.getCol7());
                Collections.sort(aqbean.getCol16());
                Collections.sort(aqbean.getCol17());
                Collections.sort(aqbean.getCol18());
            }
            objBill.setAqlistSectionWise(aqlist);
            objBill.setAqlistSectionWise(aqlist);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return objBill;
    }

    public int getGrossPay(String slno, String billno) {
        Connection con = null;
        Statement st = null;
        Statement st1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        int total = 0;

        try {
            String aqDTLS = "AQ_DTLS";
            aqDTLS = getAqDtlsTableName(billno);
            con = dataSource.getConnection();
            st = con.createStatement();
            st1 = con.createStatement();
            rs = st.executeQuery("SELECT CUR_BASIC FROM AQ_MAST WHERE AQSL_NO='" + slno + "'");
            if (rs.next()) {
                total = rs.getInt("CUR_BASIC");
                rs1 = st1.executeQuery("SELECT SUM(AD_AMT) AD_AMT FROM " + aqDTLS + " WHERE AQSL_NO='" + slno + "' AND AD_TYPE='A' ");
                if (rs1.next()) {
                    total = total + rs1.getInt("AD_AMT");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(rs1, st1);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return total;
    }

    public int getTotalDedn(String slno, String billno) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        Statement st1 = null;
        ResultSet rs1 = null;
        int total = 0;

        try {
            con = dataSource.getConnection();
            String aqDTLS = "AQ_DTLS";
            aqDTLS = getAqDtlsTableName(billno);
            st = con.createStatement();
            st1 = con.createStatement();
            //rs = st.executeQuery("SELECT AD_AMT FROM AQ_DTLS WHERE AQSL_NO='"+slno+"' AND (AD_CODE='LIC' OR AD_CODE='OTH' OR AD_CODE='GISA' OR AD_CODE='PLI' OR AD_CODE='GPF' OR AD_CODE='PT' OR AD_CODE='IT' OR AD_CODE='WT' OR AD_CODE='HC' OR AD_CODE='HRR' OR AD_CODE='WRR' OR AD_CODE='HBA' OR AD_CODE='SPL HBA' OR AD_CODE='CAR' OR AD_CODE='BI' OR AD_CODE='PAY' OR AD_CODE='FA' OR AD_CODE='OR' OR AD_CODE='MED' OR AD_CODE='GIS' OR AD_CODE='TR' OR (AD_CODE='MCA' AND NOW_DEDN='P') OR (AD_CODE='GA' AND NOW_DEDN='P') OR (AD_CODE='HBA' AND NOW_DEDN='I') OR (AD_CODE='SPL HBA' AND NOW_DEDN='I') OR (AD_CODE='MCA' AND NOW_DEDN='I') OR (AD_CODE='MOPA' AND NOW_DEDN='P') OR (AD_CODE='MOPA' AND NOW_DEDN='I') OR (AD_CODE='CAR' AND NOW_DEDN='I') OR (AD_CODE='BI' AND NOW_DEDN='I'))");    
            // rs = st.executeQuery("SELECT SUM(AD_AMT) AD_AMT FROM AQ_DTLS WHERE AQSL_NO='"+slno+"' AND AD_TYPE='D'");  
            rs = st.executeQuery("SELECT SUM(AD_AMT) AD_AMT FROM " + aqDTLS + " WHERE AQSL_NO='" + slno + "' AND AD_TYPE='D' AND SCHEDULE !='PVTL' AND SCHEDULE !='PVTD'");
            if (rs.next()) {
                total = rs.getInt("AD_AMT");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(rs1, st1);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return total;
    }

    public int getPrivateDeduction(String aqslno, String billno) {
        Connection con = null;
        Statement st = null;
        Statement st1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        int total = 0;

        try {
            con = dataSource.getConnection();
            String aqDTLS = "AQ_DTLS";
            aqDTLS = getAqDtlsTableName(billno);
            st = con.createStatement();
            st1 = con.createStatement();
            rs1 = st1.executeQuery("SELECT SUM(AD_AMT) AD_AMT FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "' AND SCHEDULE='PVTD' ");
            if (rs1.next()) {
                total = total + rs1.getInt("AD_AMT");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(rs1, st1);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return total;
    }

    public Vector getPrivateDeductionLoan(String aqslno, String billno) {
        Connection con = null;
        Statement st = null;
        Statement st1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        int total = 0;
        Vector privateDeduction = new Vector();
        try {
            con = dataSource.getConnection();
            String aqDTLS = "AQ_DTLS";
            aqDTLS = getAqDtlsTableName(billno);
            st = con.createStatement();
            st1 = con.createStatement();
            rs1 = st1.executeQuery("SELECT T2.ACC_NO,AD_AMT,IFSC_CODE,MICR_CODE FROM(SELECT T1.ACC_NO,AD_AMT,BRANCH_CODE FROM (SELECT ACC_NO,AD_AMT AD_AMT,AD_REF_ID FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "' AND (SCHEDULE='PVTD' OR SCHEDULE='PVTL') AND ACC_NO IS NOT NULL)T1 "
                    + "INNER JOIN EMP_LOAN_SANC ON T1.AD_REF_ID = EMP_LOAN_SANC.LOANID)T2 "
                    + "INNER JOIN G_BRANCH ON T2.BRANCH_CODE = G_BRANCH.BRANCH_CODE");
            while (rs1.next()) {
                PrivateDeduction pvd = new PrivateDeduction();
                pvd.setLoanaccno(rs1.getString("ACC_NO"));
                pvd.setAcctype("Loan (Cash Credit)");
                pvd.setBankifsccode(rs1.getString("IFSC_CODE"));
                pvd.setBankmicrcode(rs1.getString("MICR_CODE"));
                pvd.setLoanamt(rs1.getInt("AD_AMT"));
                privateDeduction.add(pvd);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(rs1, st1);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return privateDeduction;
    }

    public int getPrivateDeductionLoan(String aqslno, boolean isTransferToDDOAccount, String billno) {
        Connection con = null;
        Statement st = null;
        Statement st1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        int total = 0;

        try {
            con = dataSource.getConnection();
            String aqDTLS = "AQ_DTLS";
            aqDTLS = getAqDtlsTableName(billno);
            st = con.createStatement();
            st1 = con.createStatement();
            if (isTransferToDDOAccount == true) {
                rs1 = st1.executeQuery("SELECT SUM(AD_AMT) AD_AMT FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "' AND (SCHEDULE='PVTD' OR SCHEDULE='PVTL') AND ACC_NO IS NULL");
            } else {
                rs1 = st1.executeQuery("SELECT SUM(AD_AMT) AD_AMT FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "' AND (SCHEDULE='PVTD' OR SCHEDULE='PVTL') AND ACC_NO IS NOT NULL");
            }
            if (rs1.next()) {
                total = total + rs1.getInt("AD_AMT");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(rs1, st1);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return total;
    }

    public int getPrivateDeductionLoanForEmp(String aqslno, String billno) {
        Connection con = null;
        Statement st = null;
        Statement st1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        int total = 0;

        try {
            con = dataSource.getConnection();
            String aqDTLS = "AQ_DTLS";
            aqDTLS = getAqDtlsTableName(billno);
            st = con.createStatement();
            st1 = con.createStatement();

            rs1 = st1.executeQuery("SELECT SUM(AD_AMT) AD_AMT FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "' AND (SCHEDULE='PVTD' OR SCHEDULE='PVTL') AND AD_AMT>0");

            if (rs1.next()) {
                total = total + rs1.getInt("AD_AMT");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(rs1, st1);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return total;
    }

    public int getPrivateLoan(String aqslno, String billno) {
        Connection con = null;
        Statement st = null;
        Statement st1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        int total = 0;

        try {
            con = dataSource.getConnection();
            String aqDTLS = "AQ_DTLS";
            aqDTLS = getAqDtlsTableName(billno);
            st = con.createStatement();
            st1 = con.createStatement();
            rs1 = st1.executeQuery("SELECT SUM(AD_AMT) AD_AMT FROM " + aqDTLS + " WHERE AQSL_NO='" + aqslno + "' AND SCHEDULE='PVTL' ");
            if (rs1.next()) {
                total = total + rs1.getInt("AD_AMT");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(rs1, st1);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return total;
    }

    public String getRefDesc(String adCode, String nowdedn, String aqSlNo, String billno) {
        Connection con = null;
        String refdesc = null;
        Statement stamt = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();
            String aqDTLS = "AQ_DTLS";
            aqDTLS = getAqDtlsTableName(billno);
            stamt = con.createStatement();
            res = stamt.executeQuery("SELECT REF_DESC FROM " + aqDTLS + " WHERE AQSL_NO='" + aqSlNo + "' AND NOW_DEDN='" + nowdedn + "' AND AD_CODE='" + adCode + "' AND AD_AMT > 0");
            int i = 0;
            while (res.next()) {
                i++;
                refdesc = res.getString("REF_DESC");
            }
            if (i > 1) {
                refdesc = null;
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, stamt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return refdesc;
    }

    @Override
    public CommonReportParamBean getCommonReportParameter(String billNo) {
        CommonReportParamBean commonBean = new CommonReportParamBean();
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        String acctType = "GPF";

        try {
            con = dataSource.getConnection();

            String crbQry = "SELECT ARRAY_TO_STRING(ARRAY[INITIALS, EMP_MAST.F_NAME, EMP_MAST.M_NAME, EMP_MAST.L_NAME], ' ') DDONAME,g_office.ddo_code,ben_ref_no,SUFFIX,DESCRIPTION,BILL_DESC,BILL_DATE,AQ_YEAR,AQ_MONTH,G_OFFICE.OFF_EN,DIST_NAME,STATE_NAME, G_POST.POST,"
                    + "G_OFFICE.OFF_EN,G_OFFICE.DDO_REG_NO,G_OFFICE.DTO_REG_NO,G_DEPARTMENT.DEPARTMENT_NAME,TAN_NO,FA_BTID,PA_CODE,BILL_NO.OFF_CODE,"
                    + "BILL_NO.TR_CODE,BILL_NO.BILL_GROUP_ID,TR_NAME,BILL_NO.bill_type, type_of_bill, bill_status_id,gross_amt,ded_amt,pvt_ded_amt, vch_no, vch_date, FROM_MONTH, FROM_YEAR, TO_MONTH, TO_YEAR,required_reports,BILL_NO.arrear_percent FROM ( "
                    + "SELECT BILL_GROUP_ID,OFF_CODE,BILL_DESC,AQ_YEAR,AQ_MONTH,BILL_DATE,TR_CODE,bill_type, type_of_bill, bill_status_id,"
                    + "gross_amt,ded_amt,pvt_ded_amt, vch_no, vch_date, FROM_MONTH, FROM_YEAR, TO_MONTH, TO_YEAR,required_reports,ben_ref_no,"
                    + " arrear_percent FROM BILL_MAST WHERE BILL_NO=?) BILL_NO  "
                    + "INNER JOIN G_OFFICE ON BILL_NO.OFF_CODE = G_OFFICE.OFF_CODE "
                    + "LEFT OUTER JOIN G_POST ON G_OFFICE.DDO_POST=G_POST.POST_CODE "
                    + "LEFT OUTER JOIN G_DEPARTMENT ON G_OFFICE.DEPARTMENT_CODE =G_DEPARTMENT.DEPARTMENT_CODE "
                    + "LEFT OUTER JOIN G_DISTRICT ON G_OFFICE.DIST_CODE=G_DISTRICT.DIST_CODE "
                    + "LEFT OUTER JOIN G_STATE ON G_OFFICE.STATE_CODE=G_STATE.STATE_CODE "
                    + "LEFT OUTER JOIN BILL_GROUP_MASTER ON BILL_NO.BILL_GROUP_ID=BILL_GROUP_MASTER.BILL_GROUP_ID "
                    + "LEFT OUTER JOIN G_TREASURY ON BILL_NO.TR_CODE=G_TREASURY.TR_CODE"
                    + " LEFT OUTER JOIN EMP_MAST ON G_OFFICE.DDO_HRMSID=EMP_MAST.EMP_ID";
            st = con.prepareStatement(crbQry);
            st.setInt(1, Integer.parseInt(billNo));
            rs = st.executeQuery();
            if (rs.next()) {
                commonBean.setDdocode(rs.getString("ddo_code"));
                commonBean.setSuffix(rs.getString("SUFFIX"));
                commonBean.setBillgroupDesc(rs.getString("DESCRIPTION"));
                commonBean.setBillNo(billNo);
                commonBean.setBilldesc(rs.getString("BILL_DESC"));
                commonBean.setBillgroupid(rs.getBigDecimal("BILL_GROUP_ID"));
                commonBean.setBilldate(CommonFunctions.getFormattedOutputDate1(rs.getDate("BILL_DATE")));
                commonBean.setAqyear(rs.getInt("AQ_YEAR"));
                commonBean.setAqmonth(rs.getInt("AQ_MONTH"));
                commonBean.setAqMonthAsName(CommonFunctions.getMonthAsString(commonBean.getAqmonth()));

                commonBean.setDdoname(rs.getString("POST"));
                commonBean.setOfficename(rs.getString("OFF_EN"));
                commonBean.setDeptname(rs.getString("DEPARTMENT_NAME"));
                commonBean.setDistrict(rs.getString("DIST_NAME"));
                commonBean.setStatename(rs.getString("STATE_NAME"));
                commonBean.setDdoregno(rs.getString("DDO_REG_NO"));
                commonBean.setDtoregno(rs.getString("DTO_REG_NO"));
                commonBean.setOfficeen(rs.getString("OFF_EN"));
                commonBean.setTanno(rs.getString("TAN_NO"));
                commonBean.setPacode(rs.getString("PA_CODE"));
                commonBean.setOffcode(rs.getString("OFF_CODE"));
                if (rs.getString("FA_BTID") != null && !rs.getString("FA_BTID").equals("")) {
                    commonBean.setFabtid(rs.getString("FA_BTID"));
                } else {
                    commonBean.setFabtid("55032");
                }
                commonBean.setTreasuryname(rs.getString("TR_NAME"));
                commonBean.setTypeofBill(rs.getString("type_of_bill"));
                commonBean.setBillType(rs.getString("bill_type"));
                commonBean.setBillStatusId(rs.getInt("bill_status_id"));

                commonBean.setBillGrossAmt(rs.getInt("gross_amt"));
                commonBean.setBillDedAmt(rs.getInt("ded_amt"));
                commonBean.setBillPvtDedAmt(rs.getInt("pvt_ded_amt"));
                commonBean.setFromMonth(rs.getInt("FROM_MONTH"));
                commonBean.setFromYear(rs.getInt("FROM_YEAR"));
                commonBean.setToMonth(rs.getInt("TO_MONTH"));
                commonBean.setToYear(rs.getInt("TO_YEAR"));
                commonBean.setRequiredReports(rs.getString("required_reports"));

                if (rs.getString("vch_no") != null && !rs.getString("vch_no").equals("")) {
                    String vchNo1 = "";
                    String vchNo = rs.getString("vch_no");
                    if (vchNo.length() > 4) {
                        vchNo1 = vchNo.substring(4);
                    }
                    commonBean.setVchNo(vchNo1);
                } else {
                    commonBean.setVchNo("");
                }

                if (rs.getString("vch_date") != null && !rs.getString("vch_date").equals("")) {
                    commonBean.setVchDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("vch_date")));
                } else {
                    commonBean.setVchDate("");
                }

                commonBean.setBenRefNo(rs.getString("ben_ref_no"));
                commonBean.setPercentageArraer(rs.getInt("arrear_percent"));

                pst1 = con.prepareStatement("select acct_type from arr_mast where bill_no=? group by acct_type order by acct_type");
                pst1.setInt(1, Integer.parseInt(billNo));
                rs1 = pst1.executeQuery();
                if (rs1.next()) {
                    acctType = rs1.getString("ACCT_TYPE");
                }
                if (commonBean.getPercentageArraer() == 20 && acctType.equals("PRAN")) {
                    commonBean.setPercentageArraer(15);
                }
                commonBean.setDdoName(rs.getString("DDONAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return commonBean;
    }

    @Override
    public CommonReportParamBean getCommonReportParameterDHE(String billNo) {
        CommonReportParamBean commonBean = new CommonReportParamBean();
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        String acctType = "GPF";

        try {
            con = dataSource.getConnection();

            String crbQry = "SELECT g_office.ddo_code,ben_ref_no,SUFFIX,DESCRIPTION,BILL_DESC,BILL_DATE,AQ_YEAR,AQ_MONTH,G_OFFICE.OFF_EN,DIST_NAME,STATE_NAME, G_POST.POST,\n"
                    + " G_OFFICE.OFF_EN,G_OFFICE.DDO_REG_NO,G_OFFICE.DTO_REG_NO,G_DEPARTMENT.DEPARTMENT_NAME,TAN_NO,FA_BTID,PA_CODE,BILL_NO.P_OFF_CODE,\n"
                    + " BILL_NO.TR_CODE,BILL_NO.BILL_GROUP_ID,TR_NAME,BILL_NO.bill_type, type_of_bill, bill_status_id,gross_amt,ded_amt,pvt_ded_amt, vch_no, vch_date, FROM_MONTH, \n"
                    + " FROM_YEAR, TO_MONTH, TO_YEAR,required_reports,BILL_NO.arrear_percent FROM ( \n"
                    + " SELECT BILL_GROUP_ID,P_OFF_CODE,BILL_DESC,AQ_YEAR,AQ_MONTH,BILL_DATE,TR_CODE,bill_type, type_of_bill, bill_status_id,\n"
                    + " gross_amt,ded_amt,pvt_ded_amt, vch_no, vch_date, FROM_MONTH, FROM_YEAR, TO_MONTH, TO_YEAR,required_reports,ben_ref_no,\n"
                    + "  arrear_percent FROM BILL_MAST WHERE BILL_NO=?) BILL_NO \n"
                    + " INNER JOIN G_OFFICE ON BILL_NO.P_OFF_CODE = G_OFFICE.OFF_CODE\n"
                    + " LEFT OUTER JOIN G_POST ON G_OFFICE.DDO_POST=G_POST.POST_CODE\n"
                    + " LEFT OUTER JOIN G_DEPARTMENT ON G_OFFICE.DEPARTMENT_CODE =G_DEPARTMENT.DEPARTMENT_CODE\n"
                    + " LEFT OUTER JOIN G_DISTRICT ON G_OFFICE.DIST_CODE=G_DISTRICT.DIST_CODE\n"
                    + " LEFT OUTER JOIN G_STATE ON G_OFFICE.STATE_CODE=G_STATE.STATE_CODE\n"
                    + " LEFT OUTER JOIN BILL_GROUP_MASTER ON BILL_NO.BILL_GROUP_ID=BILL_GROUP_MASTER.BILL_GROUP_ID\n"
                    + " LEFT OUTER JOIN G_TREASURY ON BILL_NO.TR_CODE=G_TREASURY.TR_CODE";
            st = con.prepareStatement(crbQry);
            st.setInt(1, Integer.parseInt(billNo));
            rs = st.executeQuery();
            if (rs.next()) {
                commonBean.setDdocode(rs.getString("ddo_code"));
                commonBean.setSuffix(rs.getString("SUFFIX"));
                commonBean.setBillgroupDesc(rs.getString("DESCRIPTION"));
                commonBean.setBillNo(billNo);
                commonBean.setBilldesc(rs.getString("BILL_DESC"));
                commonBean.setBillgroupid(rs.getBigDecimal("BILL_GROUP_ID"));
                commonBean.setBilldate(CommonFunctions.getFormattedOutputDate1(rs.getDate("BILL_DATE")));
                commonBean.setAqyear(rs.getInt("AQ_YEAR"));
                commonBean.setAqmonth(rs.getInt("AQ_MONTH"));
                commonBean.setAqMonthAsName(CommonFunctions.getMonthAsString(commonBean.getAqmonth()));

                commonBean.setDdoname(rs.getString("POST"));
                commonBean.setOfficename(rs.getString("OFF_EN"));
                commonBean.setDeptname(rs.getString("DEPARTMENT_NAME"));
                commonBean.setDistrict(rs.getString("DIST_NAME"));
                commonBean.setStatename(rs.getString("STATE_NAME"));
                commonBean.setDdoregno(rs.getString("DDO_REG_NO"));
                commonBean.setDtoregno(rs.getString("DTO_REG_NO"));
                commonBean.setOfficeen(rs.getString("OFF_EN"));
                commonBean.setTanno(rs.getString("TAN_NO"));
                commonBean.setPacode(rs.getString("PA_CODE"));
                commonBean.setOffcode(rs.getString("P_OFF_CODE"));
                if (rs.getString("FA_BTID") != null && !rs.getString("FA_BTID").equals("")) {
                    commonBean.setFabtid(rs.getString("FA_BTID"));
                } else {
                    commonBean.setFabtid("55032");
                }
                commonBean.setTreasuryname(rs.getString("TR_NAME"));
                commonBean.setTypeofBill(rs.getString("type_of_bill"));
                commonBean.setBillType(rs.getString("bill_type"));
                commonBean.setBillStatusId(rs.getInt("bill_status_id"));

                commonBean.setBillGrossAmt(rs.getInt("gross_amt"));
                commonBean.setBillDedAmt(rs.getInt("ded_amt"));
                commonBean.setBillPvtDedAmt(rs.getInt("pvt_ded_amt"));
                commonBean.setFromMonth(rs.getInt("FROM_MONTH"));
                commonBean.setFromYear(rs.getInt("FROM_YEAR"));
                commonBean.setToMonth(rs.getInt("TO_MONTH"));
                commonBean.setToYear(rs.getInt("TO_YEAR"));
                commonBean.setRequiredReports(rs.getString("required_reports"));

                if (rs.getString("vch_no") != null && !rs.getString("vch_no").equals("")) {
                    String vchNo1 = "";
                    String vchNo = rs.getString("vch_no");
                    if (vchNo.length() > 4) {
                        vchNo1 = vchNo.substring(4);
                    }
                    commonBean.setVchNo(vchNo1);
                } else {
                    commonBean.setVchNo("");
                }

                if (rs.getString("vch_date") != null && !rs.getString("vch_date").equals("")) {
                    commonBean.setVchDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("vch_date")));
                } else {
                    commonBean.setVchDate("");
                }

                commonBean.setBenRefNo(rs.getString("ben_ref_no"));
                commonBean.setPercentageArraer(rs.getInt("arrear_percent"));

                pst1 = con.prepareStatement("select acct_type from arr_mast where bill_no=? group by acct_type order by acct_type");
                pst1.setInt(1, Integer.parseInt(billNo));
                rs1 = pst1.executeQuery();
                if (rs1.next()) {
                    acctType = rs1.getString("ACCT_TYPE");
                }
                if (commonBean.getPercentageArraer() == 20 && acctType.equals("PRAN")) {
                    commonBean.setPercentageArraer(15);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return commonBean;
    }

    public CommonReportParamBean getBillDetails(String billNo) {
        Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        CommonReportParamBean crb = null;
        String folderPath = null;
        //String billdate = null;
        try {
            con = dataSource.getConnection();
            rs = st.executeQuery("SELECT AQ_MONTH,AQ_YEAR,BILL_DESC,BILL_DATE FROM BILL_MAST WHERE BILL_NO='" + billNo + "'");
            if (rs.next()) {
                crb = new CommonReportParamBean();
                crb.setAqyear(Integer.parseInt(rs.getString("AQ_YEAR")));
                crb.setAqmonth(Integer.parseInt(rs.getString("AQ_MONTH")));
                crb.setBilldesc(rs.getString("BILL_DESC"));
                crb.setBilldate(CommonFunctions.getFormattedOutputDate1(rs.getDate("BILL_DATE")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return crb;
    }

    @Override
    public String getBtidSwrWrr(String billNo) {
        Connection con = null;
        ResultSet resultset = null;
        Statement st = null;
        String wrrbtid = "";
        String swrbtid = "";
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            resultset = st.executeQuery("SELECT * FROM G_AD_LIST WHERE AD_CODE_NAME='WRR' OR AD_CODE_NAME='SWR'");
            while (resultset.next()) {
                if (resultset.getString("AD_CODE_NAME").equals("WRR")) {
                    wrrbtid = resultset.getString("BT_ID");
                } else if (resultset.getString("AD_CODE_NAME").equals("SWR")) {
                    swrbtid = resultset.getString("BT_ID");
                }
            }
            swrbtid = wrrbtid + "@" + swrbtid;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(resultset, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return wrrbtid;
    }

    @Override
    public List getWRRScheduleEmployeeList(String billno, String schedule, String qtr_pool_id, int month, int year) {

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        WrrScheduleBean wrrBean = null;
        ArrayList wrrList = new ArrayList();

        try {
            con = dataSource.getConnection();
            String aqDTLS = "AQ_DTLS";
            aqDTLS = getAqDtlsTableName(billno);
            stmt = con.createStatement();
            String sql = "select GPF_ACC_NO,EMP_QTR_ALLOT.QUARTER_NO,EMP_QTR_ALLOT.ADDRESS,AQ_MAST.EMP_NAME,AQ_MAST.EMP_CODE,AQ_MAST.CUR_DESG,AQ_DTLS.ACC_NO,AQ_DTLS.REF_DESC,AQ_DTLS.AD_AMT,"
                    + "POST_SL_NO,consumer_no from ( select GPF_ACC_NO,aq_mast.EMP_CODE,aq_mast.EMP_NAME,aq_mast.CUR_DESG,aq_mast.AQSL_NO,POST_SL_NO from AQ_MAST where BILL_NO='" + billno + "' and AQ_MONTH=" + month + " AND "
                    + " AQ_YEAR=" + year + " ORDER BY POST_SL_NO) AQ_MAST INNER JOIN (select AQSL_NO,SCHEDULE,AD_AMT,AD_TYPE,ACC_NO,REF_DESC from " + aqDTLS + " where AQ_MONTH=" + month + " AND "
                    + " AQ_YEAR=" + year + " AND SCHEDULE='" + schedule + "' AND AD_TYPE='D' AND AD_AMT >0) AQ_DTLS on AQ_DTLS.AQSL_NO=AQ_MAST.AQSL_NO inner join "
                    + "(select quarter_no,EMP_ID,address,consumer_no from emp_qtr_allot WHERE Q_ID=" + qtr_pool_id + " AND (if_surrendered IS NULL OR if_surrendered != 'Y'))emp_qtr_allot on emp_qtr_allot.EMP_ID=AQ_MAST.EMP_CODE "
                    + "ORDER BY POST_SL_NO ";

            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                wrrBean = new WrrScheduleBean();
                wrrBean.setConsumerno(rs.getString("consumer_no"));
                wrrBean.setQuarterNo(rs.getString("QUARTER_NO"));
                wrrBean.setAddress(rs.getString("ADDRESS"));
                wrrBean.setGpfNo(rs.getString("GPF_ACC_NO"));
                wrrBean.setEmpname(rs.getString("EMP_NAME"));
                wrrBean.setEmpdesg(rs.getString("CUR_DESG"));
                wrrBean.setRecMonth(rs.getString("REF_DESC"));
                wrrBean.setAmount(rs.getString("AD_AMT"));
                wrrList.add(wrrBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return wrrList;
    }

    @Override
    public List getQtrPool() {
        Connection con = null;
        ResultSet resultset = null;
        Statement st = null;
        String wrrbtid = "";
        String swrbtid = "";
        GQtrPool gqtr = new GQtrPool();
        List li = new ArrayList();
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            resultset = st.executeQuery("SELECT BT_ID,Q_ID,POOL_NAME,DEMAND_NO_STRING  FROM G_QTR_POOL ");
            while (resultset.next()) {
                gqtr = new GQtrPool();
                gqtr.setBtid(resultset.getString("BT_ID"));
                gqtr.setQid(resultset.getString("Q_ID"));
                gqtr.setPoolname(resultset.getString("POOL_NAME"));
                gqtr.setDemandAsString(resultset.getString("DEMAND_NO_STRING"));
                li.add(gqtr);
            }
            swrbtid = wrrbtid + "@" + swrbtid;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(resultset, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    public String getAqDtlsTableName(String billNo) {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        String aqDTLS = "AQ_DTLS";
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery("SELECT aq_month,aq_year FROM BILL_MAST WHERE bill_no=" + billNo);
            int aqMonth = 0;
            int aqYear = 0;
            if (res.next()) {
                aqMonth = res.getInt("aq_month");
                aqYear = res.getInt("aq_year");
            }

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(aqMonth, aqYear);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aqDTLS;
    }

}
