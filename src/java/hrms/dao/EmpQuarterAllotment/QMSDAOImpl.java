/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.EmpQuarterAllotment;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.SMSServices;
import hrms.model.EmpQuarterAllotment.EmpQuarterBean;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 *
 * @author lenovo
 */
public class QMSDAOImpl implements QMSDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    private String uploadPath;
    private String uploadPathqrt;

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public void setUploadPathqrt(String uploadPathqrt) {
        this.uploadPathqrt = uploadPathqrt;
    }

    @Override
    public List OverdueVacateDate() {
        Connection con = null;

        Statement stm = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        List alist = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT consumer_ga.consumer_no as consumerNo,vacation_notice,otype,vacate_date,unit_qt,type_qt,bldgno_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,hrms_id,mobile FROM quarter_tracking INNER JOIN consumer_ga "
                    + "ON quarter_tracking.hrms_id=consumer_ga.hrmsid "
                    + "INNER JOIN emp_mast ON consumer_ga.hrmsid=emp_mast.emp_id "
                    + "WHERE retention_status='Y' AND consumer_ga.vacation_status IS  NULL  AND  vacate_date < NOW() AND hide_show is NULL   ORDER BY   vacate_date  ");
            rs = pstmt.executeQuery();
            EmpQuarterBean eqBean = null;
            while (rs.next()) {
                eqBean = new EmpQuarterBean();
                eqBean.setConsumerNo(rs.getString("consumerNo"));
                eqBean.setEmpId(rs.getString("hrms_id"));
                eqBean.setEmpName(rs.getString("EMPNAME"));
                eqBean.setQuarterNo(rs.getString("bldgno_qt"));
                eqBean.setQrtrtype(rs.getString("type_qt"));
                eqBean.setQrtrunit(rs.getString("unit_qt"));
                eqBean.setMobileno(rs.getString("mobile"));
                eqBean.setVacateStatus(rs.getString("vacation_notice"));
                String dov = CommonFunctions.getFormattedOutputDate1(rs.getDate("vacate_date"));
                eqBean.setDor(dov);
                String ostatus = "";
                String otype = rs.getString("otype");
                if (otype.equals("1") || otype.equals("2")) {
                    ostatus = "Transfer";
                }

                if (otype.equals("3")) {
                    ostatus = "Transfer to NON-KBK";

                }
                if (otype.equals("4")) {
                    ostatus = "Retirement Case";
                }
                if (otype.equals("5")) {
                    ostatus = "Death Case";
                }
                if (otype.equals("6")) {
                    ostatus = "Dismissal Case";
                }
                eqBean.setOccupationTypes(ostatus);
                eqBean.setOccuId(otype);
                alist.add(eqBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return alist;
    }

    @Override
    public void RetentionOverDueNotice(EmpQuarterBean qbean) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String qaddress = "";
        String unitqt = "";
        String typeqt = "";
        String bldgno = "";

        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("UPDATE consumer_ga SET vacation_notice = ?"
                    + " WHERE CONSUMER_NO=? AND hrmsid=?");
            pst.setString(1, "Y");
            pst.setString(2, qbean.getConsumerNo());
            pst.setString(3, qbean.getEmpId());
            pst.executeUpdate();

            String mobile = "";
            pstmt = con.prepareStatement("SELECT mobile FROM emp_mast WHERE  emp_id=?");
            pstmt.setString(1, qbean.getEmpId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                mobile = rs.getString("mobile");
            }
            if (mobile != null && !mobile.equals("")) {
                String msg = "Sir/Madam, Your quarters retention period is over. Kindly vacate the qtrs immediately. RENT OFFICER";
                String msgId = "1407162157505028034";
                String mobileno = mobile;
                //  String mobileno = "9132055693";
                SMSServices smhttp = new SMSServices(mobileno, msg, msgId);
                QMSMessageLog(con, qbean.getEmpId(), msg, mobileno, "", "Retention Over");
            } else {
                String msg = "Sir/Madam, Your quarters retention period is over. Kindly vacate the qtrs immediately. RENT OFFICER";
                QMSMessageLog(con, qbean.getEmpId(), msg, mobile, "", "Retention Over");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void QMSMessageLog(Connection conpsql, String empid, String msg, String mobile, String deliverymsg, String msgtype) {

        PreparedStatement pst = null;

        String startTime = "";
        try {
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());

            pst = conpsql.prepareStatement("INSERT INTO qms_sms_log(EMP_ID,MESSAGE_TEXT,MESSAGE_TYPE,MOBILE,SENT_ON,STATUS) VALUES(?,?,?,?,?,?)");
            pst.setString(1, empid);
            pst.setString(2, msg);
            pst.setString(3, msgtype);
            pst.setString(4, mobile);
            pst.setTimestamp(5, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(6, deliverymsg);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
        }
    }

    @Override
    public List OrderFiveTwoIssued() {
        Connection con = null;

        Statement stm = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        List alist = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT order_five_two_date,eviction_notice,dob,consumer_ga.*,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,otype,opp_display,opp_orderno,b.drop_case_date FROM consumer_ga    "
                    + "INNER JOIN quarter_tracking ON consumer_ga.hrmsid = quarter_tracking.hrms_id   "
                    + "INNER JOIN equarter.case_master b  ON b.emp_id = consumer_ga.hrmsid AND b.consumer_no = consumer_ga.consumer_no AND consumer_ga.estate_five_two=b.case_id    "
                    + "LEFT OUTER JOIN EMP_MAST ON consumer_ga.hrmsid = EMP_MAST.EMP_ID  "
                    + " WHERE  b.order_five_two=?  AND  estate_five_two IS NOT NULL ORDER BY order_five_two_date DESC");
            pstmt.setString(1, "Y");
            rs = pstmt.executeQuery();

            EmpQuarterBean eqBean = null;
            while (rs.next()) {
                eqBean = new EmpQuarterBean();
                eqBean.setConsumerNo(rs.getString("CONSUMER_NO"));
                eqBean.setEmpId(rs.getString("hrmsid"));
                eqBean.setQuarterNo(rs.getString("bldgno_qt"));
                eqBean.setQrtrtype(rs.getString("type_qt"));
                eqBean.setQrtrunit(rs.getString("unit_qt"));
                eqBean.setEmpName(rs.getString("EMPNAME"));
                eqBean.setMobileno(rs.getString("mobile"));
                eqBean.setOrderNumber(rs.getString("opp_orderno"));
                eqBean.setOccupationTypes(rs.getString("otype"));
                if (rs.getString("dob") != null && !rs.getString("dob").equals("")) {
                    eqBean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                } else {
                    eqBean.setDos("");
                }
                String dov = CommonFunctions.getFormattedOutputDate1(rs.getDate("order_five_two_date"));
                eqBean.setDor(dov);
                eqBean.setCasefivetwoOrder(rs.getString("estate_five_two"));
                eqBean.setVacateStatus(rs.getString("eviction_notice"));

                eqBean.setMobileno(rs.getString("mobile"));

                alist.add(eqBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return alist;
    }

    @Override
    public void DownloadFiveTwoOrder(Document document, int caseId) {
        // OppRequisition qbean = new OppRequisition();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String tdate = sdf.format(c.getTime());
        String Rtype = "";
        String orderNo = "";
        String orderDate = "";
        String qaddress = "";
        String unitqt = "";
        String typeqt = "";
        String bldgno = "";
        String otype = "";
        String empname = "";
        String designation = "";
        String fromOffice = "";
        String toOffice = "";
        String frompost = "";
        String topost = "";

        String fromOfficeName = "";
        String toOfficeName = "";
        String frompostName = "";
        String topostName = "";
        String lfeedate = "";

        String oppcaseno = "";
        String oppcasedate = "";
        String show_cause_date = "";

        String empName = "";
        String empId = "";
        String caseno = "";
        String caseInitiatedDate = "";
        try {
            con = this.dataSource.getConnection();

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFontnew = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont1 = new Font(Font.FontFamily.HELVETICA, 9, Font.UNDERLINE, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
            final Font pFont = FontFactory.getFont("Arial", 10, Font.NORMAL);
            final Font bFont = FontFactory.getFont("Arial", 10, Font.BOLD);
            final Font bottomRule = FontFactory.getFont("Arial", 11, Font.NORMAL);

            pstmt = con.prepareStatement("SELECT a.*,b.* FROM quarter_tracking a INNER JOIN equarter.case_master b ON a.consumer_no=b.consumer_no AND a.hrms_id=b.emp_id  WHERE  b.case_id=? LIMIT 1");
            // pstmt.setString(1, consumerNo);
            // pstmt.setString(2, empId);
            pstmt.setInt(1, caseId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                caseno = rs.getString("case_no") + "/" + rs.getString("case_year");
                qaddress = rs.getString("qrt_no");
                unitqt = rs.getString("unit_area");
                typeqt = rs.getString("qrt_type");
                otype = rs.getString("otype");
                fromOffice = rs.getString("office_from_code");
                toOffice = rs.getString("office_to_code");
                frompost = rs.getString("post_from_office");
                topost = rs.getString("post_to_office");
                empId = rs.getString("emp_id");
                if (rs.getDate("show_cause_date") != null) {
                    show_cause_date = CommonFunctions.getFormattedOutputDate1(rs.getDate("show_cause_date"));
                }
                oppcaseno = rs.getString("requisation_no");
                if (rs.getDate("opp_req_date") != null) {
                    oppcasedate = CommonFunctions.getFormattedOutputDate1(rs.getDate("opp_req_date"));
                }
                if (rs.getDate("case_initiated_date") != null) {
                    caseInitiatedDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("case_initiated_date"));
                } else {
                    caseInitiatedDate = oppcasedate;
                }
                if (rs.getDate("order_five_two_date") != null) {
                    tdate = CommonFunctions.getFormattedOutputDate1(rs.getDate("order_five_two_date"));
                }

            }

            String sql = (" SELECT  (SELECT department_name FROM g_office ,  g_department  WHERE    g_office.department_code=g_department.department_code  AND   g_office.off_code=?) AS off_en_from, "
                    + " ( SELECT department_name FROM g_office ,  g_department  WHERE    g_office.department_code=g_department.department_code  AND   g_office.off_code=?) AS off_en_to, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS from_post, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS to_post, "
                    + " ( SELECT dos  FROM emp_mast WHERE emp_id=?) AS emp_dos, "
                    + " ( SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') fullname FROM emp_mast WHERE emp_id=?) AS emp_name     ");
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, fromOffice);
            pstmt.setString(2, toOffice);
            pstmt.setString(3, frompost);
            pstmt.setString(4, topost);
            pstmt.setString(5, empId);
            pstmt.setString(6, empId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                fromOfficeName = rs.getString("off_en_from");
                toOfficeName = rs.getString("off_en_to");
                frompostName = rs.getString("from_post");
                topostName = rs.getString("to_post");
                empName = rs.getString("emp_name");

            }
            String deptName = "";
            String postName = "";
            String wefDate = "";
            if (otype.equals("1") || otype.equals("2")) {
                deptName = toOfficeName;
                postName = topostName;
                Rtype = "Transfer";
            }

            if (otype.equals("4") || otype.equals("5") || otype.equals("6")) {
                deptName = fromOfficeName;
                postName = frompostName;
            }
            if (otype.equals("3")) {
                deptName = toOfficeName;
                postName = topostName;
                Rtype = "Transfer";
            }
            if (otype.equals("4")) {
                Rtype = "Retirement";
            }
            if (otype.equals("5")) {
                Rtype = "Death";
            }
            if (otype.equals("6")) {
                Rtype = "Dismissal";
            }

            //System.out.println("otype="+otype+"  empName=="+empName);
            PdfPTable table = null;
            table = new PdfPTable(1);
            table.setWidths(new int[]{5});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("IN THE COURT OF SHRI S.K.RAYSARDAR,OAS(S), ESTATE OFFICER, BHUBANESWAR ", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("OPP CASE NO- " + caseno + "(Qr), Dated: " + tdate, hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            String msg2 = "\n   Issue of authorisation to the requisitioning officer under Section 5(2) of OPP(E) ACt,1972. Name of the party to be evicated. ";
            Paragraph point2 = new Paragraph(msg2, pFont);
            point2.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point2);

            PdfPTable table2 = null;
            table2 = new PdfPTable(1);
            table2.setWidths(new int[]{5});
            table2.setWidthPercentage(100);

            PdfPCell cell2 = null;

            cell2 = new PdfPCell(new Phrase(" ", hdrTextFont));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("ORDER", hdrTextFont));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);
            document.add(table2);

            cell2 = new PdfPCell(new Phrase(" ", hdrTextFont));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            String msg3 = "\n   That the Rent Officer of General Administration Department is hereby authorised under Section 5(2) of OPP(E) Act, 1972 to evict the person or persons who may be in occupation of Government premises scheduled below and report compilance to the undersigned by a fort night.";

            Paragraph point3 = new Paragraph(msg3, pFont);
            point3.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point3);

            PdfPTable table4 = new PdfPTable(1);
            table4.setWidths(new int[]{5});
            table4.setWidthPercentage(100);
            PdfPCell datace4;

            datace4 = new PdfPCell(new Phrase("\n\n\n" + empName + " ", dataHdrFont));
            datace4.setBorder(Rectangle.NO_BORDER);
            table4.addCell(datace4);

            datace4 = new PdfPCell(new Phrase(postName + " ", dataHdrFont));
            datace4.setBorder(Rectangle.NO_BORDER);
            table4.addCell(datace4);

            datace4 = new PdfPCell(new Phrase(deptName + " DEPARTMENT ", dataHdrFont));
            datace4.setBorder(Rectangle.NO_BORDER);
            table4.addCell(datace4);

            datace4 = new PdfPCell(new Phrase("Qtrs No:" + qaddress + " ", dataHdrFont));
            datace4.setBorder(Rectangle.NO_BORDER);
            table4.addCell(datace4);

            datace4 = new PdfPCell(new Phrase("Type:" + unitqt + " ", dataHdrFont));
            datace4.setBorder(Rectangle.NO_BORDER);
            table4.addCell(datace4);

            datace4 = new PdfPCell(new Phrase("Unit:" + typeqt + " ", dataHdrFont));
            datace4.setBorder(Rectangle.NO_BORDER);
            table4.addCell(datace4);
            datace4 = new PdfPCell(new Phrase("Bhubaneswar/Cuttack" + " ", dataHdrFont));
            datace4.setBorder(Rectangle.NO_BORDER);

            table4.addCell(datace4);
            document.add(table4);

            PdfPTable table3 = null;
            table3 = new PdfPTable(1);
            table3.setWidths(new int[]{5});
            table3.setWidthPercentage(100);

            PdfPCell cell3 = null;

            cell3 = new PdfPCell(new Phrase(" ", hdrTextFont));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell3);

            cell3 = new PdfPCell(new Phrase("SCHEDULE", hdrTextFont));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell3);

            cell3 = new PdfPCell(new Phrase(" ", hdrTextFont));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell3);

            Paragraph blank2 = new Paragraph("\n\n\n Estate Officer, BHUBANESWAR", bottomRule);
            blank2.setAlignment(Element.ALIGN_RIGHT);
            document.add(blank2);

            Paragraph blank = new Paragraph("\n G.A & P.G Department", bottomRule);
            blank.setAlignment(Element.ALIGN_RIGHT);
            document.add(blank);

            Paragraph blank5 = new Paragraph("\n\n *.This is a computer-generated document. No signature is required.", bottomRule);
            blank5.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(blank5);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // DataBaseFunctions.closeSqlObjects(ps, pstmt);
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void viewPDFEvictionNotice(Document document, String empId, String consumerNo, int CaseId) {
        EmpQuarterBean qbean = new EmpQuarterBean();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        PreparedStatement pst = null;
        String noticeDate = "";
        String orderNo = "";
        String orderDate = "";
        String qaddress = "";
        String unitqt = "";
        String typeqt = "";
        String bldgno = "";
        String otype = "";
        String empname = "";
        String designation = "";
        String fromOffice = "";
        String toOffice = "";
        String frompost = "";
        String topost = "";

        String fromOfficeName = "";
        String toOfficeName = "";
        String frompostName = "";
        String topostName = "";
        String lfeedate = "";

        String periodFrom = "";
        String periodto = "";
        String vacateperiod = "";

        String empName = "";
        String dos = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String wefdate = "";
        String wefdate1 = "";
        String wefdate2 = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());
        Long curtime = new Date().getTime();

        try {
            con = this.dataSource.getConnection();

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFontnew = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont1 = new Font(Font.FontFamily.HELVETICA, 9, Font.UNDERLINE, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
            final Font pFont = FontFactory.getFont("Arial", 10, Font.NORMAL);
            final Font bFont = FontFactory.getFont("Arial", 10, Font.BOLD);
            final Font bottomRule = FontFactory.getFont("Arial", 11, Font.NORMAL);

            pstmt = con.prepareStatement("SELECT *,b.order_five_two_date,b.case_no FROM quarter_tracking INNER JOIN equarter.case_master b  ON b.emp_id = quarter_tracking.hrms_id AND b.consumer_no = quarter_tracking.consumer_no"
                    + " WHERE quarter_tracking.consumer_no=? AND quarter_tracking.hrms_id=? AND b.order_five_two=? AND b.case_id=? ORDER BY tracking_id DESC LIMIT 1");
            pstmt.setString(1, consumerNo);
            pstmt.setString(2, empId);
            pstmt.setString(3, "Y");
            pstmt.setInt(4, CaseId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                qaddress = rs.getString("qrt_no");
                unitqt = rs.getString("unit_area");
                typeqt = rs.getString("qrt_type");
                noticeDate = rs.getString("five_two_notice_no");
                orderNo = rs.getString("case_no");
                otype = rs.getString("otype");
                orderDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("order_five_two_date"));
                fromOffice = rs.getString("office_from_code");
                toOffice = rs.getString("office_to_code");
                frompost = rs.getString("post_from_office");
                topost = rs.getString("post_to_office");

                if (rs.getDate("five_two_notice_date") != null) {
                    lfeedate = CommonFunctions.getFormattedOutputDate1(rs.getDate("five_two_notice_date"));
                }

                if (rs.getDate("quarter_allowed_from") != null) {
                    periodFrom = CommonFunctions.getFormattedOutputDate1(rs.getDate("quarter_allowed_from"));

                }
                if (rs.getDate("quarter_allowed_to") != null) {
                    periodto = CommonFunctions.getFormattedOutputDate1(rs.getDate("quarter_allowed_to"));
                    String strDate = sdf.format(rs.getDate("quarter_allowed_to"));
                    c.setTime(sdf.parse(strDate));
                    //Incrementing the date by 1 day
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    String nextdate = sdf.format(c.getTime());
                    Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate);
                    wefdate = CommonFunctions.getFormattedOutputDate1(date1);

                    c.add(Calendar.DAY_OF_MONTH, 60);
                    String nextdate1 = sdf.format(c.getTime());

                    Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate1);
                    wefdate1 = CommonFunctions.getFormattedOutputDate1(date2);

                    c.add(Calendar.DAY_OF_MONTH, 1);
                    String nextdate2 = sdf.format(c.getTime());

                    Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate2);
                    wefdate2 = CommonFunctions.getFormattedOutputDate1(date3);

                }
                if (rs.getDate("vacate_date") != null) {
                    vacateperiod = CommonFunctions.getFormattedOutputDate1(rs.getDate("vacate_date"));
                }

            }
            String sql = (" SELECT  (SELECT department_name FROM g_office ,  g_department  WHERE    g_office.department_code=g_department.department_code  AND   g_office.off_code=?) AS off_en_from, "
                    + " ( SELECT department_name FROM g_office ,  g_department  WHERE    g_office.department_code=g_department.department_code  AND   g_office.off_code=?) AS off_en_to, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS from_post, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS to_post, "
                    + " ( SELECT dos  FROM emp_mast WHERE emp_id=?) AS emp_dos, "
                    + " ( SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') fullname FROM emp_mast WHERE emp_id=?) AS emp_name     ");
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, fromOffice);
            pstmt.setString(2, toOffice);
            pstmt.setString(3, frompost);
            pstmt.setString(4, topost);
            pstmt.setString(5, empId);
            pstmt.setString(6, empId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                fromOfficeName = rs.getString("off_en_from");
                toOfficeName = rs.getString("off_en_to");
                frompostName = rs.getString("from_post");
                topostName = rs.getString("to_post");
                empName = rs.getString("emp_name");
                dos = CommonFunctions.getFormattedOutputDate1(rs.getDate("emp_dos"));

            }
            String deptName = "";
            String postName = "";
            String wefDate = "";
            if (otype.equals("1") || otype.equals("2")) {
                deptName = toOfficeName;
                postName = topostName;
                wefDate = wefdate;

            }

            if (otype.equals("4") || otype.equals("5") || otype.equals("6")) {
                deptName = fromOfficeName;
                postName = frompostName;
                wefDate = wefdate;

            }

            if (otype.equals("3")) {
                deptName = toOfficeName;
                postName = topostName;
                wefDate = wefdate2;

            }

            //System.out.println("otype="+otype+"  empName=="+empName);
            PdfPTable table = null;
            table = new PdfPTable(1);
            table.setWidths(new int[]{5});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("GOVERNMENT OF ODISHA", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("General Administration & Public Grievance(Rent) Department ", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Bhubaneswar ", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            document.add(table);

            table = new PdfPTable(2);
            table.setWidths(new int[]{3, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("\nQMS-" + noticeDate, f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("\nDate. " + lfeedate, f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);
            // if (otype.equals("1")) {
            PdfPTable table1 = new PdfPTable(1);
            table1.setWidths(new int[]{5});
            table1.setWidthPercentage(100);
            PdfPCell datacell;

            datacell = new PdfPCell(new Phrase("\nTo\n", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("      " + empName + ", ", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);
            datacell = new PdfPCell(new Phrase("      " + postName + ", ", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("      " + deptName + " Department,", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("      In Govt.Qrs.No.   " + qaddress + " , " + unitqt + ", " + typeqt + " (area),", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("      Bhubaneswar / Cuttack", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("\nSub:    Vacation of Govt. Quarters. ", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("\nRef-:    This Department office order no. " + orderNo + ", dated " + orderDate, dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("\nSir/Madam ", dataValFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            document.add(table1);

            String msg1 = "\n         I am intimate you that the Goverment Qrs. No.   " + qaddress + " , " + unitqt + ", " + typeqt + " (area), Bhubaneswar/Cuttack allotted in your favour has been cancelled w.e.f " + wefDate
                    + " vide this Department Office Order under reference and you have been occupying it unauthorisedly";
            Paragraph point1 = new Paragraph(msg1, pFont);
            point1.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point1);
            // datacell = new PdfPCell(new Phrase(msg1, dataValFont));
            // datacell.setBorder(Rectangle.NO_BORDER);
            // table1.addCell(datacell);

            String msg2 = "\n         In the meanwhile, the undersigned has been authorised by the Estate Officer, Bhubaneswar U/S 5(2) of OPP Act to evict you from the premises of aforesaid Goverment quarters. ";

            Paragraph point2 = new Paragraph(msg2, pFont);
            point2.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point2);

            String msg3 = "\n        It is, therefore, requested that the said quarters may please be vacated immediately, failing which eviction would be carried out at any time without any further notice. ";

            Paragraph point3 = new Paragraph(msg3, pFont);
            point2.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point3);

            Paragraph blank = new Paragraph("\n\n\n RENT OFFICER\n G.A. & P.G.(Rent) Department", bottomRule);
            blank.setAlignment(Element.ALIGN_RIGHT);
            document.add(blank);

            Paragraph blank5 = new Paragraph("\n *.This is a computer-generated document. No signature is required.", bottomRule);
            blank5.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(blank5);

           // }
            /*PdfPTable table1 = new PdfPTable(2);
             table1.setWidths(new int[]{5, 5});
             table1.setWidthPercentage(100);
             PdfPCell datacell;*/
            //  Users ue = getLoanEmpDetail(rs.getString("issue_letter_login_id"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, pstmt);
            DataBaseFunctions.closeSqlObjects(ps1, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void downloadVacationOccupationExcel(OutputStream out, String fileName, WritableWorkbook workbook, String vstatus, String loginName, String fdate, String tdate) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        try {
            con = this.dataSource.getConnection();

            WritableSheet sheet = workbook.createSheet(fileName, 0);
            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            WritableCellFormat headcell3 = new WritableCellFormat(headformat);
            headcell3.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell3.setWrap(true);

            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);
            headcell.setBorder(Border.ALL, BorderLineStyle.DOUBLE);

            WritableCellFormat innercell = new WritableCellFormat(NumberFormats.INTEGER);
            innercell.setVerticalAlignment(VerticalAlignment.CENTRE);
            innercell.setWrap(true);

            int col = 0;
            int widthInChars = 10;

            Label label = null;
            jxl.write.Number num = null;

            /* label = new Label(0, 0, vstatus + " List", headcell);//column,row
             sheet.mergeCells(0, 0, 10, 0);
             sheet.setColumnView(col, widthInChars);
             sheet.addCell(label);
            
             col++;*/
            label = new Label(0, 0, "Sl No.", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;

            widthInChars = 20;
            sheet.addCell(label);

            label = new Label(1, 0, "DOE", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;

            widthInChars = 30;
            sheet.addCell(label);
            label = new Label(2, 0, "HRMS Id", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;

            widthInChars = 50;
            sheet.addCell(label);
            label = new Label(3, 0, "Employee Name", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;

            widthInChars = 80;
            sheet.addCell(label);
            label = new Label(4, 0, "Post", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            sheet.addCell(label);

            widthInChars = 20;
            label = new Label(5, 0, "Mobile", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            sheet.addCell(label);

            widthInChars = 20;
            label = new Label(6, 0, "Unit/Area", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            sheet.addCell(label);

            widthInChars = 20;
            label = new Label(7, 0, "QRS Type", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            sheet.addCell(label);

            widthInChars = 20;
            label = new Label(8, 0, "QRS NO", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            sheet.addCell(label);

            widthInChars = 20;
            label = new Label(9, 0, "Status", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            sheet.addCell(label);

            widthInChars = 30;
            label = new Label(10, 0, vstatus + " Date", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            sheet.addCell(label);

            int row = 0;
            int slno = 0;

            con = dataSource.getConnection();
            if (loginName == null || loginName.equals("")) {
                pstmt = con.prepareStatement("SELECT consumer_no,entery_id,status, doe, quarter_occupation_vacation.dos, qrs_no, type_qt,unit_area, quarter_occupation_vacation.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,SPN FROM quarter_occupation_vacation  "
                        + "LEFT OUTER JOIN EMP_MAST ON quarter_occupation_vacation.emp_id = EMP_MAST.EMP_ID  "
                        + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC WHERE status=? AND  (quarter_occupation_vacation.doe>=?::DATE and quarter_occupation_vacation.doe<= ?::DATE) ORDER BY  log_id DESC");
                pstmt.setString(1, vstatus);
                pstmt.setString(2, fdate);
                pstmt.setString(3, tdate);
                rs = pstmt.executeQuery();
            }

            if (loginName != null && !loginName.equals("")) {
                String unit_qt = "";
                pstmt1 = con.prepareStatement("SELECT distinct unit_qt from consumer_ga   where unit_ph=? LIMIT 1 ");
                pstmt1.setString(1, loginName);
                rs1 = pstmt1.executeQuery();
                if (rs1.next()) {
                    unit_qt = rs1.getString("unit_qt");
                }
                // System.out.println("loginName1=="+loginName);

                pstmt = con.prepareStatement("SELECT consumer_no,entery_id,status, doe, quarter_occupation_vacation.dos, qrs_no, type_qt,unit_area, quarter_occupation_vacation.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,SPN FROM quarter_occupation_vacation  "
                        + "LEFT OUTER JOIN EMP_MAST ON quarter_occupation_vacation.emp_id = EMP_MAST.EMP_ID  "
                        + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC WHERE status=?  AND (entery_id=? OR unit_area=? ) AND  (quarter_occupation_vacation.doe>=?::DATE and quarter_occupation_vacation.doe<= ?::DATE) ORDER BY  log_id DESC ");
                pstmt.setString(1, vstatus);
                pstmt.setString(2, loginName);
                pstmt.setString(3, unit_qt);
                pstmt.setString(4, fdate);
                pstmt.setString(5, tdate);
                rs = pstmt.executeQuery();
            }
            int pCount = 0;
            while (rs.next()) {
                String dov = "";
                String doe = "";
                if (rs.getString("dos") != null && !rs.getString("dos").equals("")) {
                    dov = CommonFunctions.getFormattedOutputDate1(rs.getDate("dos"));
                }
                if (rs.getString("doe") != null && !rs.getString("doe").equals("")) {
                    doe = CommonFunctions.getFormattedOutputDate1(rs.getDate("doe"));
                }

                pCount++;

                slno += 1;
                row += 1;

                num = new jxl.write.Number(0, row, pCount, innercell);//column,row
                sheet.setColumnView(col, 10);
                sheet.addCell(num);
                col++;
                widthInChars = 20;

                label = new Label(1, row, doe, innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                sheet.addCell(label);
                col++;
                widthInChars = 40;

                label = new Label(2, row, rs.getString("emp_id"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                sheet.addCell(label);
                col++;
                widthInChars = 40;
                label = new Label(3, row, rs.getString("EMPNAME"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                sheet.addCell(label);
                col++;

                widthInChars = 40;
                label = new Label(4, row, rs.getString("SPN"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                sheet.addCell(label);
                col++;

                widthInChars = 40;
                label = new Label(5, row, rs.getString("mobile"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                sheet.addCell(label);
                col++;

                widthInChars = 40;
                label = new Label(6, row, rs.getString("unit_area"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                sheet.addCell(label);
                col++;

                widthInChars = 40;
                label = new Label(7, row, rs.getString("type_qt"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                sheet.addCell(label);
                col++;

                widthInChars = 40;
                label = new Label(8, row, rs.getString("qrs_no"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                sheet.addCell(label);
                col++;

                widthInChars = 40;
                label = new Label(9, row, rs.getString("status"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                sheet.addCell(label);
                col++;

                widthInChars = 40;
                label = new Label(10, row, dov, innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                sheet.addCell(label);
                col++;

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(rs1, pstmt1);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public List DropOPPCaseList() {
        Connection con = null;

        Statement stm = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        List alist = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT dob,consumer_ga.*,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,otype,opp_display,opp_orderno,b.drop_case_date FROM consumer_ga    "
                    + "INNER JOIN quarter_tracking ON consumer_ga.hrmsid = quarter_tracking.hrms_id   "
                    + "INNER JOIN equarter.case_master b  ON b.emp_id = consumer_ga.hrmsid AND b.consumer_no = consumer_ga.consumer_no AND consumer_ga.drop_case_id=b.case_id    "
                    + "LEFT OUTER JOIN EMP_MAST ON consumer_ga.hrmsid = EMP_MAST.EMP_ID  "
                    + " WHERE  drop_case_id IS NOT NULL ");

            rs = pstmt.executeQuery();

            EmpQuarterBean eqBean = null;
            while (rs.next()) {
                eqBean = new EmpQuarterBean();
                eqBean.setConsumerNo(rs.getString("CONSUMER_NO"));
                eqBean.setEmpId(rs.getString("hrmsid"));
                eqBean.setQuarterNo(rs.getString("bldgno_qt"));
                eqBean.setQrtrtype(rs.getString("type_qt"));
                eqBean.setQrtrunit(rs.getString("unit_qt"));
                eqBean.setEmpName(rs.getString("EMPNAME"));
                eqBean.setMobileno(rs.getString("mobile"));
                eqBean.setOrderNumber(rs.getString("opp_orderno"));
                eqBean.setOccupationTypes(rs.getString("otype"));
                if (rs.getString("dob") != null && !rs.getString("dob").equals("")) {
                    eqBean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                } else {
                    eqBean.setDos("");
                }
                String dov = CommonFunctions.getFormattedOutputDate1(rs.getDate("drop_case_date"));
                eqBean.setDor(dov);
                eqBean.setMobileno(rs.getString("mobile"));

                alist.add(eqBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return alist;
    }
    
     @Override
    public void SaveOPPVacationStatus(EmpQuarterBean qbean) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

            con = this.dataSource.getConnection();
            String vacationStatus = qbean.getVacateStatus();
            String vtype = "";

            pst = con.prepareStatement("UPDATE consumer_ga SET vacation_status = ?"
                    + " WHERE CONSUMER_NO=? AND hrmsid=?");
            pst.setString(1, vacationStatus);
            pst.setString(2, qbean.getConsumerNo());
            pst.setString(3, qbean.getEmpId());
            pst.executeUpdate();

            if (vacationStatus.equals("Yes")) {
                vtype = "Intimation to DDO";
                pstmt = con.prepareStatement("UPDATE quarter_tracking SET vacation_opp_qrt_status = ?,intimation_orderno=?,intimation_order_date=?,ddo_office_code=?,ddo_post_name=?,vacate_date=?,quarter_amount=?"
                        + " WHERE CONSUMER_NO=? AND hrms_id=?");
                pstmt.setString(1, vacationStatus);
                pstmt.setString(2, qbean.getOrderNumber());
                if (qbean.getOrderDate() != null && !qbean.getOrderDate().equals("")) {
                    pstmt.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getOrderDate()).getTime()));
                } else {
                    pstmt.setTimestamp(3, null);
                }
                pstmt.setString(4, qbean.getHidooOffCode());
                pstmt.setString(5, qbean.getGenericpostoo());
                if (qbean.getVacationDate() != null && !qbean.getVacationDate().equals("")) {
                    pstmt.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getVacationDate()).getTime()));
                } else {
                    pstmt.setTimestamp(6, null);
                }
                pstmt.setInt(7, qbean.getQrtFee());
                pstmt.setString(8, qbean.getConsumerNo());
                pstmt.setString(9, qbean.getEmpId());
                pstmt.executeUpdate();

                Calendar cal = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                String createdDateTime = dateFormat.format(cal.getTime());
                Long curtime = new Date().getTime();

                String sqllog = "INSERT INTO quarter_office_orders (file_no,file_date,file_type, entry_date,consumer_no,hrmsid,slno) VALUES(?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sqllog);
                int slno = Integer.parseInt(qbean.getOrderNumber());
                pst.setString(1, qbean.getOrderNumber());

                if (qbean.getOrderDate() != null && !qbean.getOrderDate().equals("")) {
                    pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getOrderDate()).getTime()));
                } else {
                    pst.setTimestamp(2, null);
                }
                pst.setString(3, vtype);
                pst.setTimestamp(4, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
                pst.setString(5, qbean.getConsumerNo());
                pst.setString(6, qbean.getEmpId());
                pst.setInt(7, slno);
                pst.executeUpdate();

           }
		   
		   
		   
            

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
            String createdDateTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            String sqllog = "INSERT INTO quarter_office_orders (file_no,file_date,file_type, entry_date,consumer_no,hrmsid,slno) VALUES(?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sqllog);
            int slno = Integer.parseInt(qbean.getOrderNumber());
            pst.setString(1, qbean.getOrderNumber());

            if (qbean.getOrderDate() != null && !qbean.getOrderDate().equals("")) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getOrderDate()).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            pst.setString(3, vtype);
            pst.setTimestamp(4, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            pst.setString(5, qbean.getConsumerNo());
            pst.setString(6, qbean.getEmpId());
            pst.setInt(7, slno);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

}
