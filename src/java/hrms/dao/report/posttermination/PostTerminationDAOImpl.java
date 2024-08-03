/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.report.posttermination;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.report.annualestablishmentreport.AnnualEstablishment;
import hrms.model.report.annualestablishmentreport.AnnualEstablishmentReportPTDeptList;
import hrms.model.report.annualestablishmentreport.AnnualEstablishmentReportPostTerminatonBean;
import hrms.model.report.annualestablishmentreport.AnnualEstablishmentReportPostTerminatonForm;
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

/**
 *
 * @author lenovo
 */
public class PostTerminationDAOImpl implements PostTerminationDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    @Override
    public ArrayList getScheduleIIPostTerminationList(String offcode, String spc, String fy) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList datalist = new ArrayList();
        AnnualEstablishmentReportPostTerminatonBean aerptbean = null;
        try {
            con = this.dataSource.getConnection();

            //String sql = "select * from post_termination_master_co where co_off_code=? and fy=?";
            String sql = "SELECT post_termination_master_co.* FROM process_authorization"
                    + " inner join post_termination_master_co on process_authorization.off_code=post_termination_master_co.co_off_code"
                    + " WHERE spc=? and fy=? AND process_id='13' AND role_type='VERIFIER'";
            pst = con.prepareStatement(sql);
            pst.setString(1, spc);
            pst.setString(2, fy);
            rs = pst.executeQuery();
            while (rs.next()) {
                aerptbean = new AnnualEstablishmentReportPostTerminatonBean();
                aerptbean.setProposalId(rs.getString("post_termination_co_proposal_id"));
                aerptbean.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("submitted_on")));

                if (rs.getString("is_ao_approved") == null || rs.getString("is_ao_approved").equalsIgnoreCase("")) {
                    aerptbean.setStatus("PENDING AT AO");
                } else if (rs.getString("is_ao_approved").equalsIgnoreCase("Y")) {
                    aerptbean.setStatus("<strong style='color:green'>Approved By AO</strong>");
                } else if (rs.getString("is_ao_approved").equalsIgnoreCase("N")) {
                    aerptbean.setStatus("<strong style='color:red'>Declined</strong>");
                }
                aerptbean.setRevertReason(rs.getString("revert_reason"));
                aerptbean.setNoOfPost(rs.getString("no_ter_status"));
                aerptbean.setNotermPost(rs.getString("no_ter_status"));
                aerptbean.setIsAoApproved(rs.getString("is_ao_approved"));
                datalist.add(aerptbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return datalist;
    }

    @Override
    public ArrayList viewPostTerminationScheduleII(int proposalId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList datalist = new ArrayList();

        AnnualEstablishmentReportPostTerminatonBean aerptbean = null;
        try {
            con = this.dataSource.getConnection();
            String sql = "select postname,pay_scale,go_number,go_date,no_of_post,post_terminated_date,post_remarks from post_termination_master_co"
                    + " inner join post_termination_transaction_co on post_termination_master_co.post_termination_co_proposal_id=post_termination_transaction_co.post_termination_co_proposal_id"
                    + " where post_termination_master_co.post_termination_co_proposal_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, proposalId);
            rs = pst.executeQuery();
            while (rs.next()) {
                aerptbean = new AnnualEstablishmentReportPostTerminatonBean();
                aerptbean.setPostname(rs.getString("postname"));
                aerptbean.setPayscale(rs.getString("pay_scale"));
                aerptbean.setGoNumber(rs.getString("go_number"));
                aerptbean.setGoDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("go_date")));
                aerptbean.setNoOfPost(rs.getString("no_of_post"));
                aerptbean.setTerminatedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("post_terminated_date")));
                aerptbean.setPostRemarks(rs.getString("post_remarks"));
                datalist.add(aerptbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return datalist;
    }

    @Override
    public void downloadPDFScheduleIIA(Document document, int propsalId, String offCode, String fy) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String[] an = {"A", "B", "C", "D"};
        try {
            con = this.dataSource.getConnection();

            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 5, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            PdfPTable table = null;
            PdfPTable innertable = null;
            PdfPCell cell = null;
            PdfPCell innercell = null;

            table = new PdfPTable(1);
            table.setWidths(new int[]{2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase(offCode, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Financial Year: " + fy, new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SCHEDULE II-A", new Font(Font.FontFamily.TIMES_ROMAN, 6)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(Relating to Head of the Department)", new Font(Font.FontFamily.TIMES_ROMAN, 6)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            table.addCell(cell);
            document.add(table);

            PdfPTable table1 = new PdfPTable(8);
            table1.setWidths(new int[]{5, 12, 5, 5, 5, 5, 5, 10});
            table1.setWidthPercentage(100);

            PdfPCell datacell;

            datacell = new PdfPCell(new Phrase("SL No ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Description of Posts ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Pay Scale ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("GO No.", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("GO Date ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("No. of Posts to be terminated", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Date from which posts(s) to be terminated", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Remarks", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            String sql = "select postname,pay_scale,go_number,go_date,no_of_post,post_terminated_date,post_remarks from post_termination_master_co"
                    + " inner join post_termination_transaction_co on post_termination_master_co.post_termination_co_proposal_id=post_termination_transaction_co.post_termination_co_proposal_id"
                    + " where post_termination_master_co.post_termination_co_proposal_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, propsalId);
            rs = pst.executeQuery();
            int rows = 1;
            while (rs.next()) {
                datacell = new PdfPCell(new Phrase(rows + "", dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(rs.getString("pay_scale"), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(rs.getString("go_number"), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(CommonFunctions.getFormattedOutputDate1(rs.getDate("go_date")), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(rs.getString("no_of_post"), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(CommonFunctions.getFormattedOutputDate1(rs.getDate("post_terminated_date")), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(rs.getString("post_remarks"), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                rows = rows + 1;
            }

            document.add(table1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public ArrayList getScheduleIIPostTerminationPostList(String offcode, String spc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList scheduleIIPostlist = new ArrayList();

        AnnualEstablishmentReportPostTerminatonBean aerptbean = null;
        try {
            con = this.dataSource.getConnection();

            /*String sql = "select postname,pay_scale_6th_pay,pay_scale_7th_pay,gp,post_group,sum(sanction_strength) sanction_strength,sum(menin_position) menin_position,sum(vacancy_position) vacancy_position,report_id from annual_establish_report_at_co where \n"
             + " off_code=? and aer_part_type in ('PART-A','PART-B') group by postname,pay_scale_6th_pay,pay_scale_7th_pay,gp,post_group,report_id order by post_group,postname";*/
            String sql = "select postname,pay_scale_6th_pay,pay_scale_7th_pay,gp,post_group,sum(sanction_strength) sanction_strength,sum(menin_position) menin_position,sum(vacancy_position) vacancy_position,"
                    + " report_id from aer_report_submit_at_co"
                    + " inner join annual_establish_report_at_co on aer_report_submit_at_co.co_aer_id=annual_establish_report_at_co.co_aer_id where verifier_spc=? and"
                    + " aer_part_type in ('PART-A','PART-B') group by postname,pay_scale_6th_pay,pay_scale_7th_pay,gp,post_group,report_id order by post_group,postname";
            pst = con.prepareStatement(sql);
            pst.setString(1, spc);
            rs = pst.executeQuery();
            while (rs.next()) {
                aerptbean = new AnnualEstablishmentReportPostTerminatonBean();
                aerptbean.setReportid(rs.getString("report_id"));
                aerptbean.setGroup(rs.getString("post_group"));
                aerptbean.setPostname(rs.getString("postname"));
                aerptbean.setPayscale(rs.getString("pay_scale_6th_pay"));
                aerptbean.setGp(rs.getString("gp"));
                aerptbean.setLevel_7thpay(rs.getString("pay_scale_7th_pay"));
                aerptbean.setSancStrength(rs.getString("sanction_strength"));
                aerptbean.setMenInPosition(rs.getString("menin_position"));
                aerptbean.setVacancy(rs.getString("vacancy_position"));
                //aerptbean.setNoOfPost(rs.getString("cnt"));
                scheduleIIPostlist.add(aerptbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return scheduleIIPostlist;
    }

    @Override
    public void saveTerminationNoDataSchedule2(String fy, String offcode, String empid, String empSpc, String aoOffCode, String editPropsalId) {
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        Connection con = null;
        int post_termination_co_id = 0;
        String startTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        startTime = dateFormat.format(cal.getTime());

        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            if (editPropsalId != null && !editPropsalId.equals("")) {
                post_termination_co_id = Integer.parseInt(editPropsalId);
                // con = dataSource.getConnection();
                pst = con.prepareStatement("DELETE FROM post_termination_transaction_co WHERE post_termination_co_proposal_id =?");
                pst.setInt(1, post_termination_co_id);
                pst.executeUpdate();

                pst1 = con.prepareStatement("UPDATE post_termination_master_co  SET ao_off_code=?,is_ao_approved=?,no_ter_status=?,revert_reason=?   WHERE post_termination_co_proposal_id =?");
                pst1.setString(1, aoOffCode);
                pst1.setString(2, "");
                pst1.setString(3, "Y");
                pst1.setString(4, "");
                pst1.setInt(5, post_termination_co_id);
                pst1.executeUpdate();

            } else {

                int masterCo = CommonFunctions.getMaxCodeInteger("post_termination_master_co", "post_termination_co_proposal_id", con);
                String sql = "insert into post_termination_master_co(co_off_code,fy,submitted_on,is_hod_submitted,hod_emp_id,hod_spc,ao_off_code,no_ter_status,post_termination_co_proposal_id) values(?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, offcode);
                pst.setString(2, fy);
                pst.setTimestamp(3, new Timestamp(new Date().getTime()));
                pst.setString(4, "Y");
                pst.setString(5, empid);
                pst.setString(6, empSpc);
                pst.setString(7, aoOffCode);
                pst.setString(8, "Y");
                pst.setInt(9, masterCo);
                pst.executeUpdate();

                rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    post_termination_co_id = rs.getInt(1);
                }
            }
            int mcode = CommonFunctions.getMaxCodeInteger("post_termination_transaction_co", "post_termination_co_transaction_id", con);
            String sql1 = "insert into post_termination_transaction_co(postname,go_number,go_date,pay_scale,no_of_post,post_terminated_date,post_termination_co_proposal_id,post_remarks,post_termination_co_transaction_id) values(?,?,?,?,?,?,?,?,?)";
            pst2 = con.prepareStatement(sql1);
            pst2.setString(1, "No Post Termination");
            pst2.setString(2, "NA");
            pst2.setTimestamp(3, new Timestamp(new Date().getTime()));
            pst2.setString(4, "0");
            pst2.setInt(5, 0);
            pst2.setTimestamp(6, new Timestamp(new Date().getTime()));
            pst2.setInt(7, post_termination_co_id);
            pst2.setString(8, "No Post Termination");
            pst2.setInt(9, mcode);
            pst2.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst2);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getScheduleIIPostTerminationCheckedList(String offCode, AnnualEstablishmentReportPostTerminatonForm ae) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList checkedPostList = new ArrayList();

        AnnualEstablishmentReportPostTerminatonBean aerptbean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select report_id,postname,pay_scale_6th_pay from annual_establish_report_at_co where report_id=? order by postname";
            pst = con.prepareStatement(sql);

            if (ae.getChkTerminatedPost() != null && !ae.getChkTerminatedPost().equals("")) {
                String[] postnameArr = ae.getChkTerminatedPost().split(",");

                if (postnameArr.length > 0) {
                    for (int i = 0; i < postnameArr.length; i++) {
                        int reportid = Integer.parseInt(postnameArr[i]);
                        pst.setInt(1, reportid);
                        rs = pst.executeQuery();
                        if (rs.next()) {
                            aerptbean = new AnnualEstablishmentReportPostTerminatonBean();
                            aerptbean.setPostname(rs.getString("postname"));
                            aerptbean.setPayscale(rs.getString("pay_scale_6th_pay"));
                            checkedPostList.add(aerptbean);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return checkedPostList;
    }

    @Override
    public void insertPostTerminationAtCOData(String coOffCode, String coempid, String coSpc, String aoOffCode, AnnualEstablishmentReportPostTerminatonForm ae) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;

        int post_termination_co_id = 0;
        try {
            con = this.dataSource.getConnection();
            if (ae.getEditPropsalId() != null && !ae.getEditPropsalId().equals("")) {
                post_termination_co_id = Integer.parseInt(ae.getEditPropsalId());
                // con = dataSource.getConnection();
                pst = con.prepareStatement("DELETE FROM post_termination_transaction_co WHERE post_termination_co_proposal_id =?");
                pst.setInt(1, post_termination_co_id);
                pst.executeUpdate();

                pst1 = con.prepareStatement("UPDATE post_termination_master_co  SET ao_off_code=?,is_ao_approved=?,no_ter_status=?,revert_reason=?   WHERE post_termination_co_proposal_id =?");
                pst1.setString(1, aoOffCode);
                pst1.setString(2, "");
                pst1.setString(3, "");
                pst1.setString(4, "");
                pst1.setInt(5, post_termination_co_id);
                pst1.executeUpdate();

            } else {
                int masterCo = CommonFunctions.getMaxCodeInteger("post_termination_master_co", "post_termination_co_proposal_id", con);
                String sql = "insert into post_termination_master_co(co_off_code,fy,submitted_on,is_hod_submitted,hod_emp_id,hod_spc,ao_off_code,post_termination_co_proposal_id,no_ter_status) values(?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, coOffCode);
                pst.setString(2, ae.getFinancialYear());
                pst.setTimestamp(3, new Timestamp(new Date().getTime()));
                pst.setString(4, "Y");
                pst.setString(5, coempid);
                pst.setString(6, coSpc);
                pst.setString(7, aoOffCode);
                pst.setInt(8, masterCo);
                pst.setString(9, "");
                pst.executeUpdate();

                rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    post_termination_co_id = rs.getInt(1);
                }

                DataBaseFunctions.closeSqlObjects(rs, pst);
            }
            if (post_termination_co_id > 0) {
                String sql = "insert into post_termination_transaction_co(postname,go_number,go_date,pay_scale,no_of_post,post_terminated_date,post_termination_co_proposal_id,post_remarks,post_termination_co_transaction_id) values(?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql);
                if (ae.getHidPostName() != null && !ae.getHidPostName().equals("")) {
                    String[] terminatedPostArr = ae.getHidPostName().split(",");

                    String[] payScaleArr = ae.getHidPayScale().split(",");
                    String[] goNoArr = ae.getGoNo().split(",");
                    String[] dateOfSanctionArr = ae.getDateOfSanction().split(",");
                    String[] noOfPostArr = ae.getNoOfPost().split(",");
                    String[] dateOfTermination = ae.getDateOfTermination().split(",");
                    String[] remarks = ae.getRemarks().split(",");

                    for (int i = 0; i < terminatedPostArr.length; i++) {
                        int mcode = CommonFunctions.getMaxCodeInteger("post_termination_transaction_co", "post_termination_co_transaction_id", con);
                        pst.setString(1, terminatedPostArr[i]);
                        if (goNoArr.length > 0 && goNoArr[i] != null && !goNoArr[i].equals("NA")) {
                            pst.setString(2, goNoArr[i]);
                        } else {
                            pst.setString(2, null);
                        }

                        if (dateOfSanctionArr.length > 0 && dateOfSanctionArr[i] != null && !dateOfSanctionArr[i].equals("") && !dateOfSanctionArr[i].equals("NA")) {
                            pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(dateOfSanctionArr[i]).getTime()));
                        } else {
                            pst.setTimestamp(3, null);
                        }
                        if (payScaleArr.length > 0 && payScaleArr[i] != null) {
                            pst.setString(4, payScaleArr[i]);
                        } else {
                            pst.setString(4, null);
                        }
                        if (noOfPostArr.length > 0 && noOfPostArr[i] != null && !noOfPostArr[i].equals("")) {
                            pst.setInt(5, Integer.parseInt(noOfPostArr[i]));
                        } else {
                            pst.setInt(5, 0);
                        }
                        if (dateOfTermination.length > 0 && dateOfTermination[i] != null && !dateOfTermination[i].equals("")) {
                            pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(dateOfTermination[i]).getTime()));
                        } else {
                            pst.setTimestamp(6, null);
                        }
                        pst.setInt(7, post_termination_co_id);
                        if (remarks.length > 0 && remarks[i] != null) {
                            pst.setString(8, remarks[i]);
                        } else {
                            pst.setString(8, null);
                        }
                        pst.setInt(9, mcode);
                        pst.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(rs, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList coWiseSubmittedPostTerminationList(String fy) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList deptlist = new ArrayList();
        ArrayList colist = new ArrayList();

        AnnualEstablishmentReportPTDeptList aerptdeptlist = null;
        AnnualEstablishmentReportPostTerminatonBean aerptbean = null;

        String deptname = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select department_name,off_en,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,post,submitted_on from post_termination_master_co"
                    + " inner join g_spc on post_termination_master_co.hod_spc=g_spc.spc"
                    + " inner join g_post on g_spc.gpc=g_post.post_code"
                    + " inner join g_office on post_termination_master_co.co_off_code=g_office.off_code"
                    + " inner join g_department on g_office.department_code=g_department.department_code"
                    + " inner join emp_mast on post_termination_master_co.hod_emp_id=emp_mast.emp_id"
                    + " where fy=? order by department_name,off_en";
            pst = con.prepareStatement(sql);
            pst.setString(1, fy);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (deptname.equals("")) {
                    deptname = rs.getString("department_name");
                    aerptdeptlist = new AnnualEstablishmentReportPTDeptList();
                    aerptdeptlist.setDeptName(deptname);
                } else if (!deptname.equals(rs.getString("department_name"))) {
                    aerptdeptlist.setOfflist(colist);
                    deptlist.add(aerptdeptlist);
                    colist = new ArrayList();
                    deptname = rs.getString("department_name");
                    aerptdeptlist = new AnnualEstablishmentReportPTDeptList();
                    aerptdeptlist.setDeptName(deptname);
                }
                aerptbean = new AnnualEstablishmentReportPostTerminatonBean();
                aerptbean.setOffName(rs.getString("off_en"));
                aerptbean.setEmpName(rs.getString("EMPNAME"));
                aerptbean.setPost(rs.getString("post"));
                aerptbean.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("submitted_on")));
                colist.add(aerptbean);
            }
            if (deptlist != null && deptlist.size() == 0) {
                aerptdeptlist.setOfflist(colist);
                deptlist.add(aerptdeptlist);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deptlist;
    }

    @Override
    public ArrayList aoViewSubmittedPostTerminationList(String offCode, String fy) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList colist = new ArrayList();

        AnnualEstablishmentReportPostTerminatonBean aerptbean = null;

        String deptname = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select department_name,off_en,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,post,submitted_on,post_termination_co_proposal_id,is_ao_approved from post_termination_master_co"
                    + "  inner join g_spc on post_termination_master_co.hod_spc=g_spc.spc"
                    + "  inner join g_post on g_spc.gpc=g_post.post_code"
                    + "  inner join g_office on post_termination_master_co.co_off_code=g_office.off_code"
                    + "  inner join g_department on g_office.department_code=g_department.department_code"
                    + "  inner join emp_mast on post_termination_master_co.hod_emp_id=emp_mast.emp_id"
                    + "  where fy=? and post_termination_master_co.ao_off_code=? order by department_name,off_en";
            pst = con.prepareStatement(sql);
            pst.setString(1, fy);
            pst.setString(2, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                aerptbean = new AnnualEstablishmentReportPostTerminatonBean();
                aerptbean.setOffName(rs.getString("off_en"));
                aerptbean.setEmpName(rs.getString("EMPNAME"));
                aerptbean.setPost(rs.getString("post"));
                aerptbean.setIsAoApproved(rs.getString("is_ao_approved"));
                aerptbean.setProposalId(rs.getString("post_termination_co_proposal_id"));
                aerptbean.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("submitted_on")));
                colist.add(aerptbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return colist;
    }

    @Override
    public List getTerminationPostSchedule3(String fyear, String offCode) {

        List li = new ArrayList();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        String stm = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        String gpcArray = "";
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement(" SELECT a.submitted_on,a.fy,b.post_termination_proposal_ao_id,a.revert_reason,a.fd_status,a.no_ter_status FROM post_termination_master_ao a INNER join post_termination_transaction_ao b ON a.post_termination_proposal_ao_id=b.post_termination_proposal_ao_id inner join g_spc on a.ao_spc=g_spc.spc WHERE fy=? AND a.ao_off_code=? group by b.post_termination_proposal_ao_id,a.submitted_on,a.fy,a.fd_status,a.revert_reason,a.no_ter_status  ");

            pst.setString(1, fyear);
            pst.setString(2, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                AnnualEstablishment ae = new AnnualEstablishment();
                ae.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("submitted_on")));
                ae.setFy(rs.getString("fy"));
                if (rs.getString("fd_status") == null) {
                    ae.setStatus("PENDING AT Finance Department");
                } else if (rs.getString("fd_status").equalsIgnoreCase("Approved")) {
                    ae.setStatus("Approved");
                } else if (rs.getString("fd_status").equalsIgnoreCase("Rejected")) {
                    ae.setStatus("Rejected");

                } else if (rs.getString("fd_status").equalsIgnoreCase("Reverted Back")) {
                    ae.setStatus("Rejected");

                }
                ae.setRevertReason(rs.getString("revert_reason"));
                ae.setTermId(rs.getInt("post_termination_proposal_ao_id"));
                ae.setNotermPOst(rs.getString("no_ter_status"));

                li.add(ae);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getAERPOstDetailsSchedule3(String offCode) {

        List li = new ArrayList();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        String stm = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        boolean viewStatus = true;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select postname,pay_scale_6th_pay,pay_scale_7th_pay,gp,post_group,sum(sanction_strength) as ss,sum(menin_position) as pp,sum(vacancy_position) as vacancy,report_id from annual_establish_report_at_ao where off_code=? and aer_part_type in ('PART-A','PART-B') AND postname is NOT NULL GROUP BY postname,pay_scale_6th_pay,pay_scale_7th_pay,gp,post_group,report_id ORDER BY post_group,postname");
            pst.setString(1, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                AnnualEstablishment ae = new AnnualEstablishment();
                ae.setGroup(rs.getString("post_group"));
                ae.setPostname(rs.getString("postname"));
                ae.setScaleofPay(rs.getString("pay_scale_6th_pay"));
                ae.setGp(rs.getString("GP"));
                ae.setScaleofPay7th(rs.getString("pay_scale_7th_pay"));
                ae.setSanctionedStrength(rs.getInt("ss"));
                ae.setMeninPosition(rs.getInt("pp"));
                ae.setVacancyPosition(rs.getInt("vacancy"));
                ae.setReportId(rs.getInt("report_id"));
                li.add(ae);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void saveTerminationNoDataSchedule3(String fy, String offcode, String empid, String empSpc) {
        PreparedStatement pst = null;
        PreparedStatement pst2 = null;
        Connection con = null;
        int prposalId = 0;
        String startTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        startTime = dateFormat.format(cal.getTime());

        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            String sql = "INSERT INTO post_termination_master_ao(ao_off_code,status,fy,submitted_on,is_ao_submitted, ao_emp_id, ao_spc, fd_dept_code,no_ter_status ) values(?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, offcode);
            pst.setString(2, "Submitted to FD");
            pst.setString(3, fy);
            pst.setTimestamp(4, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(5, "Y");
            pst.setString(6, empid);
            pst.setString(7, empSpc);
            pst.setString(8, "OLSFIN0010000");
            pst.setString(9, "Y");
            pst.execute();

            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                prposalId = rs.getInt(1);
            }
            pst2 = con.prepareStatement("INSERT INTO post_termination_transaction_ao (post_name,gp,post_remarks, go_number, go_date, pay_scale, no_of_post, post_terminated_date, post_termination_proposal_ao_id) VALUES(?,?,?,?,?,?,?,?,?)");
            pst2.setString(1, "NO POST");
            pst2.setString(2, "0");
            pst2.setString(3, "No Post Termination");
            pst2.setString(4, "NA");
            pst2.setTimestamp(5, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst2.setString(6, "0");
            pst2.setInt(7, 0);
            pst2.setTimestamp(8, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst2.setInt(9, prposalId);
            pst2.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst2);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getAERCheckedPOstDetailsSchedule3(String fyear, String offCode, String getChkReportId) {

        List li = new ArrayList();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        String stm = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        String reportArray = "";
        try {
            reportArray = getChkReportId.replaceAll(",", "','");

            con = dataSource.getConnection();
            pst = con.prepareStatement("select postname,pay_scale_6th_pay,pay_scale_7th_pay,gp,post_group,sum(sanction_strength) as ss,sum(menin_position) as pp,sum(vacancy_position) as vacancy,report_id from annual_establish_report_at_ao where off_code=? and aer_part_type in ('PART-A','PART-B') AND report_id IN ('" + reportArray + "') AND postname is NOT NULL GROUP BY postname,pay_scale_6th_pay,pay_scale_7th_pay,gp,post_group,report_id ORDER BY post_group,postname");

            pst.setString(1, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                AnnualEstablishment ae = new AnnualEstablishment();
                ae.setGroup(rs.getString("post_group"));
                ae.setPostname(rs.getString("postname"));
                ae.setScaleofPay(rs.getString("pay_scale_6th_pay"));
                ae.setGp(rs.getString("GP"));
                ae.setScaleofPay7th(rs.getString("pay_scale_7th_pay"));
                ae.setSanctionedStrength(rs.getInt("ss"));
                ae.setMeninPosition(rs.getInt("pp"));
                ae.setVacancyPosition(rs.getInt("vacancy"));
                ae.setReportId(rs.getInt("report_id"));
                li.add(ae);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void saveTerminationDataschedule3(AnnualEstablishment bean) {
        PreparedStatement pst = null;
        PreparedStatement pst2 = null;
        Connection con = null;
        int prposalId = 0;
        String startTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        startTime = dateFormat.format(cal.getTime());

        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            String sql = "INSERT INTO post_termination_master_ao(ao_off_code,status,fy,submitted_on,is_ao_submitted, ao_emp_id, ao_spc, fd_dept_code ) values(?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, bean.getOffCode());
            pst.setString(2, "Submitted to FD");
            pst.setString(3, bean.getFinancialYear());
            pst.setTimestamp(4, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(5, "Y");
            pst.setString(6, bean.getEmpId());
            pst.setString(7, bean.getEmpPost());
            pst.setString(8, "OLSFIN0010000");
            pst.execute();

            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                prposalId = rs.getInt(1);
            }

            String postArray[] = bean.getHiddenpostName().split(",");
            String gpArray[] = bean.getHiddengp().split(",");
            String payscale[] = bean.getHiddePayScale().split(",");
            String remarksArray[] = bean.getRemarks().split(",");
            String gonoArray[] = bean.getGoNo().split(",");
            String goDateArray[] = bean.getGoDate().split(",");
            String noOfPostArray[] = bean.getPostTerminated().split(",");
            String ordDateArray[] = bean.getDateTerminated().split(",");
            String godateArray[] = bean.getGoDate().split(",");

            for (int i = 0; i < postArray.length; i++) {
                pst2 = con.prepareStatement("INSERT INTO post_termination_transaction_ao (post_name,gp,post_remarks, go_number, go_date, pay_scale, no_of_post, post_terminated_date, post_termination_proposal_ao_id) VALUES(?,?,?,?,?,?,?,?,?)");
                pst2.setString(1, postArray[i]);
                pst2.setString(2, gpArray[i]);
                pst2.setString(3, remarksArray[i]);
//                pst2.setString(4, gonoArray[i]);
                if (gonoArray.length > 0 && gonoArray[i] != null && !gonoArray[i].equals("NA")) {
                    pst2.setString(4, gonoArray[i]);
                } else {
                    pst2.setString(4, "");
                }
                //  pst2.setTimestamp(5, new Timestamp(dateFormat.parse(godateArray[i]).getTime()));

                if (godateArray.length > 0 && !godateArray[i].equals("NA") && godateArray[i] != null && !godateArray[i].equals("")) {
                    pst2.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(godateArray[i]).getTime()));
                } else {
                    pst2.setTimestamp(5, null);
                }

                pst2.setString(6, payscale[i]);
                pst2.setInt(7, Integer.parseInt(noOfPostArray[i]));

                // pst2.setTimestamp(8, new Timestamp(dateFormat.parse(ordDateArray[i]).getTime()));
                if (ordDateArray.length > 0 && ordDateArray[i] != null && !ordDateArray[i].equals("")) {
                    pst2.setTimestamp(8, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(ordDateArray[i]).getTime()));
                } else {
                    pst2.setTimestamp(8, null);
                }
                pst2.setInt(9, prposalId);
                pst2.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst2);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getviewTSchedule3(String fyear, String offCode, int termId) {

        List li = new ArrayList();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        String stm = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT b.* FROM post_termination_master_ao a, post_termination_transaction_ao b WHERE a.post_termination_proposal_ao_id=b.post_termination_proposal_ao_id AND fy=? AND ao_off_code=? AND a.post_termination_proposal_ao_id=?");

            pst.setString(1, fyear);
            pst.setString(2, offCode);
            pst.setInt(3, termId);
            rs = pst.executeQuery();
            while (rs.next()) {
                AnnualEstablishment ae = new AnnualEstablishment();
                ae.setPostname(rs.getString("post_name"));
                ae.setScaleofPay(rs.getString("pay_scale"));
                ae.setGp(rs.getString("gp"));
                ae.setGoNo(rs.getString("go_number"));
                ae.setGoDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("go_date")));
                ae.setRemarks(rs.getString("post_remarks"));
                ae.setDateTerminated(CommonFunctions.getFormattedOutputDate1(rs.getDate("post_terminated_date")));
                ae.setPostTerminated(rs.getInt("no_of_post") + "");
                li.add(ae);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getTerminationPostFD(String fyear) {

        List li = new ArrayList();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        String stm = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        String gpcArray = "";
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT D.department_name,D.demand_no, (SELECT  post_termination_master_ao.ao_off_code FROM g_office "
                    + ", post_termination_master_ao WHERE g_office.off_code=ao_off_code AND D.department_code =g_office.department_code AND fy=? GROUP BY post_termination_master_ao.ao_off_code LIMIT 1 ) FROM g_department D WHERE D.if_active='Y' ORDER BY demand_no ");

            pst.setString(1, fyear);
            rs = pst.executeQuery();
            while (rs.next()) {
                AnnualEstablishment ae = new AnnualEstablishment();
                ae.setDepartmentname(rs.getString("department_name"));
                ae.setOffCode(rs.getString("ao_off_code"));
                ae.setDemandNo(rs.getInt("demand_no"));

                li.add(ae);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List viewDeptWiseTermination(String offCode, String fyear) {

        List li = new ArrayList();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        String stm = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        String gpcArray = "";
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT SUM(no_of_post) as totalPost,pay_scale,post_name,gp FROM post_termination_master_ao a ,post_termination_transaction_ao b WHERE ao_off_code=?  AND fy=? AND a.post_termination_proposal_ao_id=b.post_termination_proposal_ao_id GROUP BY pay_scale,post_name,gp ");
            pst.setString(1, offCode);
            pst.setString(2, fyear);
            rs = pst.executeQuery();
            while (rs.next()) {
                AnnualEstablishment ae = new AnnualEstablishment();

                ae.setPostname(rs.getString("post_name"));
                ae.setScaleofPay(rs.getString("pay_scale"));
                ae.setPostTerminated(rs.getInt("totalPost") + "");
                ae.setGp(rs.getString("gp"));

                li.add(ae);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public ArrayList COWiseDeptPostTerminationStatus() {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs1, rs2, rs3 = null;
        ArrayList outList = new ArrayList();

        String stm = null;
        String stm1 = null;
        String sql2 = null;
        String fyYear = "2020-21";
        try {
            con = repodataSource.getConnection();
            sql2 = "SELECT off_code,off_en,(SELECT count(*) FROM post_termination_master_co WHERE co_off_code=g_office.off_code AND fy=? AND g_office.department_code=?) as cnt FROM g_office WHERE (lvl=? OR lvl=?) AND 	g_office.is_ddo='Y'  AND g_office.department_code=?  order by off_en ";
            stm = ("SELECT count(g_office.*) as  totalDdo ,department_name,g_department.department_code,g_department.demand_no,( select off_en from g_office inner join \n"
                    + "(SELECT ao_off_code FROM post_termination_master_co WHERE  fy=? GROUP BY ao_off_code)temp\n"
                    + "on g_office.off_code=temp.ao_off_code WHERE department_code=g_department.department_code ) is_dept_submitted FROM g_department INNER JOIN g_office "
                    + " ON g_department.department_code=g_office.department_code WHERE 	g_department.if_active='Y' AND 	g_office.is_ddo='Y'  AND (g_office.lvl='02' OR  g_office.lvl='01') GROUP BY  g_department.department_code ORDER BY department_name");
            ps = con.prepareStatement(stm);
            ps.setString(1, "2020-21");
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setDepartmentname(rs1.getString("department_name"));
                aer.setTotalDDO(rs1.getInt("totalDdo"));
                aer.setStatus(rs1.getString("is_dept_submitted"));
                String deptCode = rs1.getString("department_code");
                ArrayList inList = new ArrayList();

                ps1 = con.prepareStatement(sql2);
                ps1.setString(1, "2020-21");
                ps1.setString(2, deptCode);
                ps1.setString(3, "01");
                ps1.setString(4, "02");
                ps1.setString(5, deptCode);
                rs2 = ps1.executeQuery();
                int totalCosubmitted = 0;
                while (rs2.next()) {
                    AnnualEstablishment aer2 = new AnnualEstablishment();
                    aer2.setOffCode(rs2.getString("off_code"));
                    aer2.setOffName(rs2.getString("off_en"));
                    aer2.setTotalDDO(rs2.getInt("cnt"));
                    String offCode = rs2.getString("off_code");

                    stm1 = "SELECT count(*) as coStatus FROM post_termination_master_co WHERE co_off_code=? AND fy=? AND  ao_off_code is NOT NULL  ";
                    ps2 = con.prepareStatement(stm1);
                    ps2.setString(1, offCode);
                    ps2.setString(2, "2020-21");
                    rs3 = ps2.executeQuery();
                    String Status = "N";

                    while (rs3.next()) {
                        if (rs3.getInt("coStatus") > 0) {
                            Status = "Y";
                            totalCosubmitted = totalCosubmitted + 1;
                        }

                    }
                    aer2.setCoStatus(Status);

                    inList.add(aer2);
                }
                aer.setAerWiseDDO(inList);
                aer.setTotal(totalCosubmitted);
                aer.setDeptCode(deptCode);
                outList.add(aer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public void downloadPDFScheduleIIIA(Document document, int termId, String offname, String offcode, String fy) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String[] an = {"A", "B", "C", "D"};
        try {
            con = this.dataSource.getConnection();

            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 5, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            PdfPTable table = null;
            PdfPTable innertable = null;
            PdfPCell cell = null;
            PdfPCell innercell = null;

            table = new PdfPTable(1);
            table.setWidths(new int[]{2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase(offname, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Financial Year: " + fy, new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SCHEDULE III-A", new Font(Font.FontFamily.TIMES_ROMAN, 6)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(Relating to Administrative Department, attached Sub-ordinate Offices, Heads of Department & Sub-ordinate District Offices for the Department as a whole)", new Font(Font.FontFamily.TIMES_ROMAN, 6)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            table.addCell(cell);
            document.add(table);

            PdfPTable table1 = new PdfPTable(9);
            table1.setWidths(new int[]{5, 12, 5, 5, 5, 5, 5, 5, 10});
            table1.setWidthPercentage(100);

            PdfPCell datacell;

            datacell = new PdfPCell(new Phrase("SL No ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Description of Posts ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Pay Scale ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("GP ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("GO No.", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("GO Date ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("No. of Posts to be terminated", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Date from which posts(s) to be terminated", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Remarks", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            pst = con.prepareStatement("SELECT b.* FROM post_termination_master_ao a, post_termination_transaction_ao b WHERE a.post_termination_proposal_ao_id=b.post_termination_proposal_ao_id AND fy=? AND ao_off_code=? AND a.post_termination_proposal_ao_id=?");
            pst.setString(1, fy);
            pst.setString(2, offcode);
            pst.setInt(3, termId);
            rs = pst.executeQuery();
            int rows = 1;
            while (rs.next()) {
                datacell = new PdfPCell(new Phrase(rows + "", dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(rs.getString("post_name"), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(rs.getString("pay_scale"), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(rs.getString("gp"), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(rs.getString("go_number"), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(CommonFunctions.getFormattedOutputDate1(rs.getDate("go_date")), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(rs.getInt("no_of_post") + "", dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(CommonFunctions.getFormattedOutputDate1(rs.getDate("post_terminated_date")), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(rs.getString("post_remarks"), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                rows = rows + 1;
            }

            document.add(table1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public List getlistingTerminationPost(String fyear, String offCode) {

        List li = new ArrayList();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        String stm = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        String gpcArray = "";
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement(" SELECT a.submitted_on,a.fy,b.post_termination_proposal_id,a.is_hod_submitted,a.revert_reason,a.no_ter_status,a.is_co_approved FROM post_termination_master a INNER join post_termination_transaction b ON a.post_termination_proposal_id=b.post_termination_proposal_id inner join g_spc on a.hoo_spc=g_spc.spc WHERE fy=? AND a.off_code=? group by b.post_termination_proposal_id,a.submitted_on,a.fy,a.is_hod_submitted,a.is_co_approved,a.revert_reason,a.no_ter_status  ");

            pst.setString(1, fyear);
            pst.setString(2, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                AnnualEstablishment ae = new AnnualEstablishment();
                ae.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("submitted_on")));
                ae.setFy(rs.getString("fy"));
                if (rs.getString("is_co_approved") == null || rs.getString("is_co_approved").equalsIgnoreCase("")) {
                    ae.setStatus("PENDING AT CO END");
                } else if (rs.getString("is_co_approved").equalsIgnoreCase("Y")) {
                    ae.setStatus("<strong style='color:green'>Approved By CO</strong>");
                } else if (rs.getString("is_co_approved").equalsIgnoreCase("N")) {
                    ae.setStatus("<strong style='color:red'>Declined</strong>");

                }
                ae.setRevertReason(rs.getString("revert_reason"));
                ae.setTermId(rs.getInt("post_termination_proposal_id"));
                ae.setNotermPOst(rs.getString("no_ter_status"));
                li.add(ae);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void saveTerminationData(AnnualEstablishment bean) {
        PreparedStatement pst = null;
        PreparedStatement pst2 = null;
        Connection con = null;
        int prposalId = 0;
        String startTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        startTime = dateFormat.format(cal.getTime());

        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            String sql = "INSERT INTO post_termination_master(off_code,status,fy,submitted_on,is_hoo_submitted, hoo_emp_id, hoo_spc, co_off_code ) values(?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, bean.getOffCode());
            pst.setString(2, "Submitted to Heads of Department");
            pst.setString(3, bean.getFy());
            pst.setTimestamp(4, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(5, "Y");
            pst.setString(6, bean.getEmpId());
            pst.setString(7, bean.getEmpPost());
            pst.setString(8, bean.getHidOffCode());
            pst.execute();

            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                prposalId = rs.getInt(1);
            }
            String gpcarray[] = bean.getHiddenGPC().split(",");
            String remarksArray[] = bean.getRemarks().split(",");
            String gonoArray[] = bean.getGoNo().split(",");
            String goDateArray[] = bean.getGoDate().split(",");
            String noOfPostArray[] = bean.getPostTerminated().split(",");
            String ordDateArray[] = bean.getDateTerminated().split(",");
            String godateArray[] = bean.getGoDate().split(",");
            String payscale[] = bean.getHiddePayScale().split(",");
            String postArray[] = bean.getHiddenpostName().split(",");

            pst2 = con.prepareStatement("INSERT INTO post_termination_transaction (gpc, post_remarks, go_number, go_date, pay_scale, no_of_post, post_terminated_date, post_termination_proposal_id,postname) VALUES(?,?,?,?,?,?,?,?,?)");
            for (int i = 0; i < gpcarray.length; i++) {
                pst2.setString(1, gpcarray[i]);
                pst2.setString(2, remarksArray[i]);

                if (gonoArray.length > 0 && gonoArray[i] != null && !gonoArray[i].equals("NA")) {
                    pst2.setString(3, gonoArray[i]);
                } else {
                    pst2.setString(3, null);
                }

                if (godateArray.length > 0 && godateArray[i] != null && !godateArray[i].equals("") && !godateArray[i].equals("NA")) {
                    pst2.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(godateArray[i]).getTime()));
                } else {
                    pst2.setTimestamp(4, null);
                }
                // pst2.setString(3, gonoArray[i]);
                // pst2.setTimestamp(4, new Timestamp(dateFormat.parse(godateArray[i]).getTime()));

                pst2.setString(5, payscale[i]);
                pst2.setInt(6, Integer.parseInt(noOfPostArray[i]));
                pst2.setTimestamp(7, new Timestamp(dateFormat.parse(ordDateArray[i]).getTime()));
                pst2.setInt(8, prposalId);
                pst2.setString(9, postArray[i]);
                pst2.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst2);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getviewTSchedule1(String fyear, String offCode, int termId) {

        List li = new ArrayList();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        String stm = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT b.* FROM post_termination_master a, post_termination_transaction b WHERE a.post_termination_proposal_id=b.post_termination_proposal_id AND fy=? AND off_code=? AND a.post_termination_proposal_id=?");
            pst.setString(1, fyear);
            pst.setString(2, offCode);
            pst.setInt(3, termId);
            rs = pst.executeQuery();
            while (rs.next()) {
                AnnualEstablishment ae = new AnnualEstablishment();
                ae.setPostname(rs.getString("postname"));
                ae.setScaleofPay(rs.getString("pay_scale"));
                // ae.setGp(rs.getString("gp"));
                ae.setGoNo(rs.getString("go_number"));
                ae.setGoDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("go_date")));
                ae.setRemarks(rs.getString("post_remarks"));
                ae.setDateTerminated(CommonFunctions.getFormattedOutputDate1(rs.getDate("post_terminated_date")));
                ae.setPostTerminated(rs.getInt("no_of_post") + "");
                li.add(ae);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void saveTerminationNoDataSchedule1(String fy, String offcode, String empid, String empSpc) {
        PreparedStatement pst = null;
        PreparedStatement pst2 = null;
        PreparedStatement pst1 = null;
        Connection con = null;
        int prposalId = 0;
        String startTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        startTime = dateFormat.format(cal.getTime());
        String coOffcode = "";

        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            pst1 = con.prepareStatement("SELECT co_off_code FROM ddo_to_co_mapping WHERE ddo_off_code=?");
            pst1.setString(1, offcode);
            rs = pst1.executeQuery();
            while (rs.next()) {
                coOffcode = rs.getString("co_off_code");
            }

            String sql = "INSERT INTO post_termination_master(off_code,status,fy,submitted_on,is_hoo_submitted, hoo_emp_id, hoo_spc, co_off_code,no_ter_status ) values(?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, offcode);
            pst.setString(2, "Submitted to Heads of Department");
            pst.setString(3, fy);
            pst.setTimestamp(4, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(5, "Y");
            pst.setString(6, empid);
            pst.setString(7, empSpc);
            pst.setString(8, coOffcode);
            pst.setString(9, "Y");
            pst.execute();

            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                prposalId = rs.getInt(1);
            }

            pst2 = con.prepareStatement("INSERT INTO post_termination_transaction (gpc, post_remarks, go_number, go_date, pay_scale, no_of_post, post_terminated_date, post_termination_proposal_id,postname) VALUES(?,?,?,?,?,?,?,?,?)");
            pst2.setString(1, "");
            pst2.setString(2, "NO Termination  Post");
            pst2.setString(3, "");
            pst2.setTimestamp(4, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst2.setString(5, "");
            pst2.setInt(6, 0);
            pst2.setTimestamp(7, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst2.setInt(8, prposalId);
            pst2.setString(9, "No Post");
            pst2.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst2, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getAERCheckedPOstDetails(String fyear, String offCode, String checkedGpC) {

        List li = new ArrayList();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        String stm = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        String gpcArray = "";
        try {
            gpcArray = checkedGpC.replaceAll(",", "','");

            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT post, sanction_strength, meninpositionForTR(gpc,pay_scale,post_grp,level_7thpay::TEXT,GP,OFF_CODE) person_in_position,gpc, pay_scale, POST_GRP, level_7thpay, GP,OFF_CODE FROM( SELECT COUNT(*) sanction_strength,GPC,pay_scale,post_grp,level_7thpay, G_SPC.GP,aer2_co_mapping.OFF_CODE FROM aer2_co_mapping inner join bill_section_mapping on aer2_co_mapping"
                    + ".BILL_GROUP_ID = bill_section_mapping.BILL_GROUP_ID"
                    + "  inner join bill_group_master on bill_section_mapping.BILL_GROUP_ID = bill_group_master.BILL_GROUP_ID"
                    + " inner join g_section on bill_section_mapping.section_id = g_section.section_id"
                    + " INNER JOIN SECTION_POST_MAPPING ON BILL_SECTION_MAPPING.SECTION_ID = SECTION_POST_MAPPING.SECTION_ID"
                    + " INNER JOIN G_SPC ON SECTION_POST_MAPPING.SPC = G_SPC.SPC AND G_SPC.GPC IN ('" + gpcArray + "')"
                    + " LEFT OUTER JOIN EMP_MAST ON SECTION_POST_MAPPING"
                    + " .SPC = EMP_MAST.CUR_SPC"
                    + " WHERE aer2_co_mapping.off_code =?"
                    + " and bill_group_master.bill_type = '42' and g_section.bill_type ='REGULAR'"
                    + " AND is_sanctioned = 'Y' "
                    + " group by gpc, pay_scale, post_grp,level_7thpay,G_SPC.GP, aer2_co_mapping.OFF_CODE"
                    + " ) G_SPC INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + " order by post_grp, level_7thpay::Integer desc,pay_scale desc, post ");

            pst.setString(1, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                AnnualEstablishment ae = new AnnualEstablishment();
                ae.setGroup(rs.getString("POST_GRP"));
                ae.setGpc(rs.getString("gpc"));
                ae.setPostname(rs.getString("post"));
                ae.setScaleofPay(rs.getString("pay_scale"));
                ae.setGp(rs.getString("GP"));
                ae.setScaleofPay7th(rs.getString("level_7thpay"));
                ae.setSanctionedStrength(rs.getInt("sanction_strength"));
                ae.setMeninPosition(rs.getInt("person_in_position"));
                ae.setVacancyPosition(ae.getSanctionedStrength() - ae.getMeninPosition());
                li.add(ae);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getAERPOstDetails(String offCode, String fy) {

        List li = new ArrayList();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        String stm = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        boolean viewStatus = true;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT post, sanction_strength, meninpositionForTR(gpc,pay_scale,post_grp,level_7thpay::TEXT,GP,OFF_CODE) person_in_position,gpc, pay_scale, POST_GRP, level_7thpay, GP,OFF_CODE FROM( SELECT COUNT(*) sanction_strength,GPC,pay_scale,post_grp,level_7thpay, G_SPC.GP,aer2_co_mapping.OFF_CODE FROM aer2_co_mapping inner join bill_section_mapping on aer2_co_mapping"
                    + ".BILL_GROUP_ID = bill_section_mapping.BILL_GROUP_ID"
                    + "  inner join bill_group_master on bill_section_mapping.BILL_GROUP_ID = bill_group_master.BILL_GROUP_ID"
                    + " inner join g_section on bill_section_mapping.section_id = g_section.section_id"
                    + " INNER JOIN SECTION_POST_MAPPING ON BILL_SECTION_MAPPING.SECTION_ID = SECTION_POST_MAPPING.SECTION_ID"
                    + " INNER JOIN G_SPC ON SECTION_POST_MAPPING.SPC = G_SPC.SPC"
                    + " LEFT OUTER JOIN EMP_MAST ON SECTION_POST_MAPPING"
                    + " .SPC = EMP_MAST.CUR_SPC"
                    + " WHERE aer2_co_mapping.off_code =?"
                    + "and bill_group_master.bill_type = '42' and g_section.bill_type ='REGULAR'"
                    + "AND is_sanctioned = 'Y' and aer2_co_mapping.fy=?"
                    + "group by gpc, pay_scale, post_grp,level_7thpay,G_SPC.GP, aer2_co_mapping.OFF_CODE"
                    + ") G_SPC INNER JOIN G_POST ON G_SPC"
                    + ".GPC = G_POST.POST_CODE order by post_grp, level_7thpay::Integer desc"
                    + ",pay_scale desc, post");
            pst.setString(1, offCode);
            pst.setString(2, fy);
            rs = pst.executeQuery();
            while (rs.next()) {
                AnnualEstablishment ae = new AnnualEstablishment();
                ae.setGroup(rs.getString("POST_GRP"));
                ae.setGpc(rs.getString("gpc"));
                ae.setPostname(rs.getString("post"));
                ae.setScaleofPay(rs.getString("pay_scale"));
                ae.setGp(rs.getString("GP"));
                ae.setScaleofPay7th(rs.getString("level_7thpay"));
                ae.setSanctionedStrength(rs.getInt("sanction_strength"));
                ae.setMeninPosition(rs.getInt("person_in_position"));
                ae.setVacancyPosition(ae.getSanctionedStrength() - ae.getMeninPosition());
                li.add(ae);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void downloadPDFScheduleIA(Document document, int termId, String offname, String offcode, String fy) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String[] an = {"A", "B", "C", "D"};
        try {
            con = this.dataSource.getConnection();

            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 5, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            PdfPTable table = null;
            PdfPTable innertable = null;
            PdfPCell cell = null;
            PdfPCell innercell = null;

            table = new PdfPTable(1);
            table.setWidths(new int[]{2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase(offname, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Financial Year: " + fy, new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SCHEDULE I-A", new Font(Font.FontFamily.TIMES_ROMAN, 6)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(Relating to Head of the Office)", new Font(Font.FontFamily.TIMES_ROMAN, 6)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            table.addCell(cell);
            document.add(table);

            PdfPTable table1 = new PdfPTable(8);
            table1.setWidths(new int[]{5, 12, 5, 5, 5, 5, 5, 10});
            table1.setWidthPercentage(100);

            PdfPCell datacell;

            datacell = new PdfPCell(new Phrase("SL No ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Description of Posts ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Pay Scale ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("GO No.", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("GO Date ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("No. of Posts to be terminated", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Date from which posts(s) to be terminated", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Remarks", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            //   pst = con.prepareStatement("SELECT b.* FROM post_termination_master_ao a, post_termination_transaction_ao b WHERE a.post_termination_proposal_ao_id=b.post_termination_proposal_ao_id AND fy=? AND ao_off_code=? AND a.post_termination_proposal_ao_id=?");
            pst = con.prepareStatement("SELECT b.* FROM post_termination_master a, post_termination_transaction b WHERE a.post_termination_proposal_id=b.post_termination_proposal_id AND fy=? AND off_code=? AND a.post_termination_proposal_id=?");
            pst.setString(1, fy);
            pst.setString(2, offcode);
            pst.setInt(3, termId);
            rs = pst.executeQuery();
            int rows = 1;
            while (rs.next()) {
                datacell = new PdfPCell(new Phrase(rows + "", dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(rs.getString("pay_scale"), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(rs.getString("go_number"), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(CommonFunctions.getFormattedOutputDate1(rs.getDate("go_date")), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(rs.getInt("no_of_post") + "", dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(CommonFunctions.getFormattedOutputDate1(rs.getDate("post_terminated_date")), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(rs.getString("post_remarks"), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                rows = rows + 1;
            }

            document.add(table1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void approvePostTerminationForCO(int proposalId, String fy) {
        Connection con = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("update post_termination_master_co set is_ao_approved=?  WHERE   post_termination_co_proposal_id=? AND fy=?");
            pst.setString(1, "Y");
            pst.setInt(2, proposalId);
            pst.setString(3, fy);
            pst.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void declinePostTerminationForCO(int proposalId, String fy, String revertReason) {

        Connection con = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("update post_termination_master_co set is_ao_approved=?,revert_reason=?,ao_off_code=?  WHERE   post_termination_co_proposal_id=? AND fy=?");
            pst.setString(1, "N");
            pst.setString(2, revertReason);
            pst.setString(3, "");
            pst.setInt(4, proposalId);
            pst.setString(5, fy);
            pst.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public ArrayList AOWiseSummaryReport(String offCode, String fy) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList colist = new ArrayList();
        AnnualEstablishmentReportPostTerminatonBean aerptbean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT SUM(no_of_post) as noOfPost,postname,pay_scale FROM post_termination_master_co INNER JOIN post_termination_transaction_co"
                    + "  ON post_termination_master_co.post_termination_co_proposal_id=post_termination_transaction_co.post_termination_co_proposal_id WHERE ao_off_code=?  AND fy=? AND is_ao_approved=?"
                    + "  GROUP BY postname,pay_scale ORDER BY postname";
            pst = con.prepareStatement(sql);
            pst.setString(1, offCode);
            pst.setString(2, fy);
            pst.setString(3, "Y");
            rs = pst.executeQuery();

            while (rs.next()) {
                aerptbean = new AnnualEstablishmentReportPostTerminatonBean();
                aerptbean.setPost(rs.getString("postname"));
                aerptbean.setPayscale(rs.getString("pay_scale"));
                aerptbean.setNoOfPost(rs.getString("noOfPost"));
                colist.add(aerptbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return colist;
    }

    @Override
    public void SubmitConsolidatedDataToFD(String offcode, String empid, String spc, String fy) {

        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        String startTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        startTime = dateFormat.format(cal.getTime());
        ArrayList colist = new ArrayList();
        int prposalId = 0;

        String deptname = "";
        try {

            con = dataSource.getConnection();
            String sql_ao = "INSERT INTO post_termination_master_ao(ao_off_code,status,fy,submitted_on,is_ao_submitted, ao_emp_id, ao_spc, fd_dept_code ) values(?,?,?,?,?,?,?,?)";
            pst1 = con.prepareStatement(sql_ao, Statement.RETURN_GENERATED_KEYS);
            pst1.setString(1, offcode);
            pst1.setString(2, "Submitted to FD");
            pst1.setString(3, fy);
            pst1.setTimestamp(4, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst1.setString(5, "Y");
            pst1.setString(6, empid);
            pst1.setString(7, spc);
            pst1.setString(8, "OLSFIN0010000");
            pst1.execute();

            rs1 = pst1.getGeneratedKeys();
            if (rs1.next()) {
                prposalId = rs1.getInt(1);
            }

            String sql = "SELECT SUM(no_of_post) as noOfPost,postname,pay_scale FROM post_termination_master_co INNER JOIN post_termination_transaction_co"
                    + "  ON post_termination_master_co.post_termination_co_proposal_id=post_termination_transaction_co.post_termination_co_proposal_id WHERE ao_off_code=?  AND fy=? AND is_ao_approved=?"
                    + "  GROUP BY postname,pay_scale ORDER BY postname";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            pst.setString(2, fy);
            pst.setString(3, "Y");
            rs = pst.executeQuery();
            while (rs.next()) {
                pst2 = con.prepareStatement("INSERT INTO post_termination_transaction_ao (post_name,post_remarks, pay_scale, no_of_post, post_termination_proposal_ao_id) VALUES(?,?,?,?,?)");
                pst2.setString(1, rs.getString("postname"));
                pst2.setString(2, "NA");
                pst2.setString(3, rs.getString("pay_scale"));
                pst2.setInt(4, rs.getInt("noOfPost"));
                pst2.setInt(5, prposalId);
                pst2.execute();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public String coFDStatus(String offCode, String fy) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList colist = new ArrayList();
        String fdStatus = "N";
        AnnualEstablishmentReportPostTerminatonBean aerptbean = null;
        try {
            con = this.dataSource.getConnection();
            String sql = "SELECT * FROM post_termination_master_ao WHERE ao_off_code=? AND fy=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offCode);
            pst.setString(2, fy);
            rs = pst.executeQuery();

            while (rs.next()) {
                fdStatus = "Y";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fdStatus;
    }

    @Override
    public ArrayList DeptWisePostTerminationReport() {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement cntStatement = null;
        ResultSet rs1, rs2, rs3 = null;
        ArrayList outList = new ArrayList();
        String stm, stm1, stm2 = null;
        String fyYear = "2020-21";
        AnnualEstablishmentReportPostTerminatonBean aer = null;
        try {
            con = repodataSource.getConnection();

            stm = "SELECT department_name,g_department.department_code,demand_no FROM g_department  WHERE	g_department.if_active='Y'  ORDER BY demand_no";
            ps = con.prepareStatement(stm);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                int GroupA = 0;
                int GroupB = 0;
                int GroupC = 0;
                int GroupD = 0;
                int GroupO = 0;
                int GroupR = 0;
                int GroupG = 0;

                int grantidaid = 0;
                int TotalGroup = 0;

                aer = new AnnualEstablishmentReportPostTerminatonBean();
                aer.setDepartmentname(rs1.getString("department_name"));
                aer.setDemandNo(rs1.getInt("demand_no"));
                String deptCode = rs1.getString("department_code");
                aer.setDeptCode(deptCode);

                stm1 = "SELECT SUM(no_of_post) as cnt,post_group,department_name FROM post_termination_master_ao a ,post_termination_transaction_ao b,g_department d, g_office e WHERE d.department_code=e.department_code AND e.off_code=a.ao_off_code	AND no_of_post > 0 AND d.department_code =  ? AND  fy =  ? AND  a.post_termination_proposal_ao_id = b.post_termination_proposal_ao_id GROUP BY post_group,department_name ORDER BY department_name, post_group";
                ps1 = con.prepareStatement(stm1);
                ps1.setString(1, deptCode);
                ps1.setString(2, fyYear);

                rs2 = ps1.executeQuery();
                while (rs2.next()) {
                    if (rs2.getString("post_group") != null && rs2.getString("post_group").equals("A")) {
                        GroupA = rs2.getInt("cnt");
                    }
                    if (rs2.getString("post_group") != null && rs2.getString("post_group").equals("B")) {
                        GroupB = rs2.getInt("cnt");
                    }
                    if (rs2.getString("post_group") != null && rs2.getString("post_group").equals("C")) {
                        GroupC = rs2.getInt("cnt");
                    }
                    if (rs2.getString("post_group") != null && rs2.getString("post_group").equals("D")) {
                        GroupD = rs2.getInt("cnt");
                    }
                    if (rs2.getString("post_group") == null || rs2.getString("post_group").equals("")) {
                        GroupO = rs2.getInt("cnt");
                    }
                    if (rs2.getString("post_group") != null && rs2.getString("post_group").equals("R")) {
                        GroupR = rs2.getInt("cnt");
                    }
                    if (rs2.getString("post_group") != null && rs2.getString("post_group").equals("G")) {
                        GroupG = rs2.getInt("cnt");
                    }
                    GroupO = GroupO + GroupR + GroupG;
                    aer.setTotalACnt(GroupA);
                    aer.setTotalBCnt(GroupB);
                    aer.setTotalCCnt(GroupC);
                    aer.setTotalDCnt(GroupD);
                    aer.setTotalOCnt(GroupO);

                    TotalGroup = GroupA + GroupB + GroupC + GroupD + GroupO;
                    aer.setTotalallCnt(TotalGroup);

                }

                int grandTotal = TotalGroup;
                aer.setGrandTotal(grandTotal);
                outList.add(aer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList COViewPostTerminationList(String offCode, String fy) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList colist = new ArrayList();

        AnnualEstablishmentReportPostTerminatonBean aerptbean = null;

        String deptname = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select department_name,off_en,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,post,submitted_on,post_termination_proposal_id,is_co_approved from post_termination_master"
                    + "  inner join g_spc on post_termination_master.hoo_spc=g_spc.spc"
                    + "  inner join g_post on g_spc.gpc=g_post.post_code"
                    + "  inner join g_office on post_termination_master.off_code=g_office.off_code"
                    + "  inner join g_department on g_office.department_code=g_department.department_code"
                    + "  inner join emp_mast on post_termination_master.hoo_emp_id=emp_mast.emp_id"
                    + "  where fy=? and post_termination_master.co_off_code=? order by department_name,off_en";
            pst = con.prepareStatement(sql);
            pst.setString(1, fy);
            pst.setString(2, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                aerptbean = new AnnualEstablishmentReportPostTerminatonBean();
                aerptbean.setOffName(rs.getString("off_en"));
                aerptbean.setEmpName(rs.getString("EMPNAME"));
                aerptbean.setPost(rs.getString("post"));
                aerptbean.setIsAoApproved(rs.getString("is_co_approved"));
                aerptbean.setProposalId(rs.getString("post_termination_proposal_id"));
                aerptbean.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("submitted_on")));
                colist.add(aerptbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return colist;
    }

    @Override
    public void approvePostTerminationForDDO(int proposalId, String fy) {
        Connection con = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("update post_termination_master set is_co_approved=?  WHERE   post_termination_proposal_id=? AND fy=?");
            pst.setString(1, "Y");
            pst.setInt(2, proposalId);
            pst.setString(3, fy);
            pst.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void declinePostTerminationForDDO(int proposalId, String fy, String revertReason) {

        Connection con = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("update post_termination_master set is_co_approved=?,revert_reason=?,co_off_code=?  WHERE   post_termination_proposal_id=? AND fy=?");
            pst.setString(1, "N");
            pst.setString(2, revertReason);
            pst.setString(3, "");
            pst.setInt(4, proposalId);
            pst.setString(5, fy);
            pst.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void downloadPDFScheduleIAByCO(Document document, int termId, String offname, String offcode, String fy) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String[] an = {"A", "B", "C", "D"};
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("SELECT off_en FROM post_termination_master inner join g_office on post_termination_master.co_off_code=g_office.off_code WHERE  fy=? AND post_termination_proposal_id=?");
            pst.setString(1, fy);
            pst.setInt(2, termId);
            rs = pst.executeQuery();
            while (rs.next()) {
                offname = rs.getString("off_en");
            }

            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 5, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            PdfPTable table = null;
            PdfPTable innertable = null;
            PdfPCell cell = null;
            PdfPCell innercell = null;

            table = new PdfPTable(1);
            table.setWidths(new int[]{2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase(offname, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Financial Year: " + fy, new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SCHEDULE I-A", new Font(Font.FontFamily.TIMES_ROMAN, 6)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(Relating to Head of the Office)", new Font(Font.FontFamily.TIMES_ROMAN, 6)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            table.addCell(cell);
            document.add(table);

            PdfPTable table1 = new PdfPTable(8);
            table1.setWidths(new int[]{5, 12, 5, 5, 5, 5, 5, 10});
            table1.setWidthPercentage(100);

            PdfPCell datacell;

            datacell = new PdfPCell(new Phrase("SL No ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Description of Posts ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Pay Scale ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("GO No.", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("GO Date ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("No. of Posts to be terminated", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Date from which posts(s) to be terminated", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Remarks", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            //   pst = con.prepareStatement("SELECT b.* FROM post_termination_master_ao a, post_termination_transaction_ao b WHERE a.post_termination_proposal_ao_id=b.post_termination_proposal_ao_id AND fy=? AND ao_off_code=? AND a.post_termination_proposal_ao_id=?");
            pst = con.prepareStatement("SELECT b.* FROM post_termination_master a, post_termination_transaction b WHERE a.post_termination_proposal_id=b.post_termination_proposal_id AND fy=? AND co_off_code=? AND a.post_termination_proposal_id=?");
            pst.setString(1, fy);
            pst.setString(2, offcode);
            pst.setInt(3, termId);
            rs = pst.executeQuery();
            int rows = 1;
            while (rs.next()) {
                datacell = new PdfPCell(new Phrase(rows + "", dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(rs.getString("pay_scale"), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(rs.getString("go_number"), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(CommonFunctions.getFormattedOutputDate1(rs.getDate("go_date")), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(rs.getInt("no_of_post") + "", dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(CommonFunctions.getFormattedOutputDate1(rs.getDate("post_terminated_date")), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(rs.getString("post_remarks"), dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                rows = rows + 1;
            }

            document.add(table1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public ArrayList COWiseSummaryReport(String offCode, String fy) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList colist = new ArrayList();
        AnnualEstablishmentReportPostTerminatonBean aerptbean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT SUM(no_of_post) as noOfPost,postname,pay_scale FROM post_termination_master INNER JOIN post_termination_transaction"
                    + "  ON post_termination_master.post_termination_proposal_id=post_termination_transaction.post_termination_proposal_id WHERE co_off_code=?  AND fy=? AND is_co_approved=?"
                    + "  GROUP BY postname,pay_scale ORDER BY postname";
            pst = con.prepareStatement(sql);
            pst.setString(1, offCode);
            pst.setString(2, fy);
            pst.setString(3, "Y");
            rs = pst.executeQuery();

            while (rs.next()) {
                aerptbean = new AnnualEstablishmentReportPostTerminatonBean();
                aerptbean.setPost(rs.getString("postname"));
                aerptbean.setPayscale(rs.getString("pay_scale"));
                aerptbean.setNoOfPost(rs.getString("noOfPost"));
                colist.add(aerptbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return colist;
    }

}
