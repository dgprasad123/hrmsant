package hrms.dao.AgLta;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.SMSServices;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class AgLtaDAOImpl implements AgLtaDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void sendLTASMS() {

        Connection con = null;

        PreparedStatement selectpst = null;
        ResultSet rs = null;

        PreparedStatement updatepst = null;

        int slno = 0;
        try {
            con = this.dataSource.getConnection();

            updatepst = con.prepareStatement("update ag_lta_comm_msg set is_delivered='Y' where emp_id=?");

            String sql = "select emp_id,mobile,message_text from ag_lta_comm_msg where is_delivered='N' and message_group='ALL'";
            selectpst = con.prepareStatement(sql);
            rs = selectpst.executeQuery();
            SMSServices smhttp = null;
            while (rs.next()) {
                slno += 1;
                smhttp = new SMSServices(rs.getString("mobile"), rs.getString("message_text"), "1407161639885487739");

                updatepst.setString(1, rs.getString("emp_id"));
                updatepst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, selectpst);
            DataBaseFunctions.closeSqlObjects(rs, updatepst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void downloadMonhtlySuperannuationDataExcel(WritableWorkbook workbook, int year, int month) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int row = 0;
        try {
            con = this.dataSource.getConnection();

            WritableSheet sheet = workbook.createSheet(year + "_" + month, 0);

            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 15, WritableFont.BOLD);
            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);
            headcell.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableFont headformat2 = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            WritableCellFormat innerheadcell = new WritableCellFormat(headformat2);
            innerheadcell.setAlignment(Alignment.CENTRE);
            innerheadcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            innerheadcell.setWrap(true);
            innerheadcell.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableFont innerformat = new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD);

            WritableCellFormat innercell = new WritableCellFormat(innerformat);
            innercell.setWrap(true);
            innercell.setBorder(Border.ALL, BorderLineStyle.THIN);

            Label label = new Label(0, row, "SUPERANNUATION REPORT FOR " + CommonFunctions.getMonthAsString(month - 1) + "-" + year, innerheadcell);
            sheet.mergeCells(0, row, 14, row);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "HRMS ID", innerheadcell);
            sheet.setColumnView(0, 15);
            sheet.addCell(label);
            label = new Label(1, row, "MOBILE NO", innerheadcell);
            sheet.setColumnView(1, 15);
            sheet.addCell(label);
            label = new Label(2, row, "MAJOR HEAD", innerheadcell);
            sheet.setColumnView(2, 15);
            sheet.addCell(label);
            label = new Label(3, row, "CURRENT\nDDO CODE", innerheadcell);
            sheet.setColumnView(3, 15);
            sheet.addCell(label);
            label = new Label(4, row, "PREVIOUS\nOFF CODE", innerheadcell);
            sheet.setColumnView(4, 15);
            sheet.addCell(label);
            label = new Label(5, row, "GPF NO/PRAN", innerheadcell);
            sheet.setColumnView(5, 18);
            sheet.addCell(label);
            label = new Label(6, row, "GPF\nSERIES", innerheadcell);
            sheet.setColumnView(6, 10);
            sheet.addCell(label);
            label = new Label(7, row, "GPF\nNUMBER", innerheadcell);
            sheet.setColumnView(7, 12);
            sheet.addCell(label);
            label = new Label(8, row, "FIRST NAME", innerheadcell);
            sheet.setColumnView(8, 25);
            sheet.addCell(label);
            label = new Label(9, row, "MIDDLE NAME", innerheadcell);
            sheet.setColumnView(9, 25);
            sheet.addCell(label);
            label = new Label(10, row, "LAST NAME", innerheadcell);
            sheet.setColumnView(10, 25);
            sheet.addCell(label);
            label = new Label(11, row, "DATE OF BIRTH", innerheadcell);
            sheet.setColumnView(11, 20);
            sheet.addCell(label);
            label = new Label(12, row, "DATE OF SUPERANNUATION", innerheadcell);
            sheet.setColumnView(12, 20);
            sheet.addCell(label);
            label = new Label(13, row, "OFFICE", innerheadcell);
            sheet.setColumnView(13, 30);
            sheet.addCell(label);
            label = new Label(14, row, "POST", innerheadcell);
            sheet.setColumnView(14, 20);
            sheet.addCell(label);

            /*String sql = "select gpf_no, replace(replace(regexp_match(gpf_no,'[A-Z]+')::text,'{', ''),'}', '') AS gpfseries"
             + ", replace(replace(regexp_match(gpf_no,'[0-9]+')::text,'{', ''),'}', '') as gpfnum"
             + ", ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULLNAME,"
             + " to_char(DOB, 'dd-MON-yyyy') BIRTHDATE,to_char(DOS, 'dd-MON-yyyy') RETIREMENTDATE,off_en,post"
             + " from emp_mast e"
             + " left outer join g_office on e.cur_off_code=g_office.off_code"
             + " left outer join g_spc on e.cur_spc=g_spc.spc"
             + " left outer join g_post on g_spc.gpc=g_post.post_code"
             + " where extract(year from dos)=? and extract(month from dos)=? AND ACCT_TYPE='GPF' AND IF_GPF_ASSUMED='N'"
             + " and dep_code NOT IN ('00', '08', '09', '10', '13', '14') order by DOS";*/
            String sql = "select"
                    + " e.emp_id,e.mobile,(select bill_mast.major_head from aq_mast inner join bill_mast on aq_mast.bill_no=bill_mast.bill_no where bill_status_id=7 and gpf_acc_no=e.gpf_no and bill_mast.off_code=e.cur_off_code"
                    + " order by bill_mast.aq_year desc,bill_mast.aq_month desc limit 1)major_head,"
                    + " g_office.ddo_code current_ddocode,"
                    + " (select aq_mast.off_code from aq_mast where gpf_acc_no=e.gpf_no order by aq_mast.aq_year desc,aq_mast.aq_month desc limit 1)previous_off_code,gpf_no, replace(replace(regexp_match(gpf_no,'[A-Z]+')::text,'{', ''),'}', '') AS gpfseries"
                    + " , replace(replace(regexp_match(gpf_no,'[0-9]+')::text,'{', ''),'}', '') as gpfnum"
                    + " , F_NAME, M_NAME,L_NAME,"
                    + " to_char(DOB, 'dd-MON-yyyy') BIRTHDATE,to_char(DOS, 'dd-MON-yyyy') RETIREMENTDATE,off_en,post"
                    + " from emp_mast e"
                    + " left outer join g_office on e.cur_off_code=g_office.off_code"
                    + " left outer join g_spc on e.cur_spc=g_spc.spc"
                    + " left outer join g_post on g_spc.gpc=g_post.post_code"
                    + " where extract(year from dos)=? and extract(month from dos)=? AND ACCT_TYPE in ('GPF','PRAN') AND IF_GPF_ASSUMED='N'"
                    + " and dep_code NOT IN ('00', '08', '09', '10', '13', '14') order by DOS";
            pst = con.prepareStatement(sql);
            pst.setInt(1, year);
            pst.setInt(2, month);
            rs = pst.executeQuery();
            while (rs.next()) {

                row = row + 1;

                label = new Label(0, row, rs.getString("emp_id"), innercell);
                sheet.setColumnView(0, 15);
                sheet.addCell(label);
                label = new Label(1, row, rs.getString("mobile"), innercell);
                sheet.setColumnView(1, 15);
                sheet.addCell(label);
                label = new Label(2, row, rs.getString("major_head"), innercell);
                sheet.setColumnView(2, 15);
                sheet.addCell(label);
                label = new Label(3, row, rs.getString("current_ddocode"), innercell);
                sheet.setColumnView(3, 15);
                sheet.addCell(label);
                label = new Label(4, row, rs.getString("previous_off_code"), innercell);
                sheet.setColumnView(4, 15);
                sheet.addCell(label);
                label = new Label(5, row, rs.getString("gpf_no"), innercell);
                sheet.setColumnView(5, 18);
                sheet.addCell(label);
                label = new Label(6, row, rs.getString("gpfseries"), innercell);
                sheet.setColumnView(6, 10);
                sheet.addCell(label);
                label = new Label(7, row, rs.getString("gpfnum"), innercell);
                sheet.setColumnView(7, 12);
                sheet.addCell(label);
                label = new Label(8, row, rs.getString("F_NAME"), innercell);
                sheet.setColumnView(8, 25);
                sheet.addCell(label);
                label = new Label(9, row, rs.getString("M_NAME"), innercell);
                sheet.setColumnView(9, 25);
                sheet.addCell(label);
                label = new Label(10, row, rs.getString("L_NAME"), innercell);
                sheet.setColumnView(10, 25);
                sheet.addCell(label);
                label = new Label(11, row, rs.getString("BIRTHDATE"), innercell);
                sheet.setColumnView(11, 20);
                sheet.addCell(label);
                label = new Label(12, row, rs.getString("RETIREMENTDATE"), innercell);
                sheet.setColumnView(12, 20);
                sheet.addCell(label);
                label = new Label(13, row, rs.getString("off_en"), innercell);
                sheet.setColumnView(13, 30);
                sheet.addCell(label);
                label = new Label(14, row, rs.getString("post"), innercell);
                sheet.setColumnView(14, 20);
                sheet.addCell(label);
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void agLTALoanIntimationSendSMS() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement updatepst = null;

        String loantype = "";
        String amount = "";
        String gpfno = "";
        String remainingBalance = "";
        String smstext = "";
        String smsintimationmonth = "FEB";
        String smsintimationyear = "2024";
        int smsintimationid = 0;
        String mobile = "";
        try {
            con = this.dataSource.getConnection();

            String updatesql = "UPDATE ag_loan_sms_intimation set sms_sent='Y',sms_intimation_date=? where sms_intimation_id=?";
            updatepst = con.prepareStatement(updatesql);

            String sql = "SELECT * FROM ag_loan_sms_intimation where sms_sent='N' and sms_intimation_month=? and sms_intimation_year=? and mobile is not null limit 1000";
            pst = con.prepareStatement(sql);
            pst.setString(1, smsintimationmonth);
            pst.setString(2, smsintimationyear);
            rs = pst.executeQuery();
            while (rs.next()) {
                smsintimationid = rs.getInt("sms_intimation_id");
                loantype = rs.getString("loan_typ");
                amount = rs.getString("cur_month");
                gpfno = rs.getString("gpf_no");
                remainingBalance = rs.getString("remaining_balance");
                mobile = rs.getString("mobile");
                smsintimationmonth = rs.getString("sms_intimation_month");
                smsintimationyear = rs.getString("sms_intimation_year");

                if (getSMSGatewayStatus() == false) {
                    smstext = "AG Odisha confirms credit for " + loantype + " for Rs." + amount + " against your ID No:" + gpfno + " in the account month " + smsintimationmonth + ", Year " + smsintimationyear + ".Remaining Balance Rs." + remainingBalance + ". HRMS Odisha.";
                    new SMSServices(mobile, smstext, "1407166980743336127");
                } else {
                    String send = "CMGHRM";
                    String message = "AG Odisha confirms credit for " + loantype + " for Rs." + amount + " against your ID No:" + gpfno + " in the account month " + smsintimationmonth + ", Year " + smsintimationyear + ".Remaining Balance Rs." + remainingBalance + ". HRMS Odisha.";
                    String username = "20230049";
                    String password = "D9JrBBlH";
                    
                    String data = ""
                            + "uname=" + URLEncoder.encode(username, "UTF-8")
                            + "&pass=" + URLEncoder.encode(password, "UTF-8")
                            + "&dest=" + URLEncoder.encode(mobile, "UTF-8")
                            + "&msg=" + URLEncoder.encode(message, "UTF-8")
                            + "&send=" + URLEncoder.encode(send, "UTF-8")
                            + "";
                    URL url = new URL("http://164.52.195.161/API/SendMsg.aspx?" + data);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    conn.connect();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    StringBuffer buffer = new StringBuffer();
                    while ((line = rd.readLine()) != null) {
                        buffer.append(line).append("\n");
                    }
                    System.out.println(buffer.toString());
                    rd.close();
                    conn.disconnect();
                }
                updatepst.setTimestamp(1, new Timestamp(new Date().getTime()));
                updatepst.setInt(2, smsintimationid);
                updatepst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(updatepst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public boolean getSMSGatewayStatus() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean secondSMS = false;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT * FROM HRMS_CONFIG WHERE GLOBAL_VAR_NAME='SWITCH_SECOND_SMS'";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("global_var_value") != null && rs.getString("global_var_value").equals("Y")) {
                    secondSMS = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return secondSMS;
    }
}
