/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.policemodule;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.ZipFileAttribute;
import hrms.model.common.FileAttribute;
import hrms.model.policemodule.ASINominationForm;
import hrms.model.policemodule.AwardMedalListForm;
import hrms.model.policemodule.EmployeeDetailsForRank;
import hrms.model.policemodule.NominationDifferentDisciplinaryProceedingList;
import hrms.model.policemodule.NominationDifferentDistrictandEstablishmentList;
import hrms.model.policemodule.NominationForm;
import hrms.model.policemodule.PoliceEmployee;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Surendra
 */
public class NominationRollDAOImpl implements NominationRollDAO {

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

    private String uploadPath;

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    @Override
    public String getCurrentRankCodeFormDetailTable(int nominationMasterId, int nominationDetailId) {
        String currentCode = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement(" select current_rank from police_nomination_detail where nomination_master_id=? and nomination_detail_id=? ");
            ps.setInt(1, nominationMasterId);
            ps.setInt(2, nominationDetailId);
            rs = ps.executeQuery();
            if (rs.next()) {
                currentCode = rs.getString("current_rank");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return currentCode;
    }

    @Override
    public String getRankname(String rankCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String rankName = "";

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement(" select rank_code, rank_name from police_rank_map where rank_code=? ");
            ps.setString(1, rankCode);
            rs = ps.executeQuery();
            if (rs.next()) {
                rankName = rs.getString("rank_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return rankName;
    }

    @Override
    public String getPromoteRankname(String rankCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String rankName = "";

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement(" select promot_to_rank_code, promot_to_rank_name from police_rank_map where promot_to_rank_code=? ");
            ps.setString(1, rankCode);
            rs = ps.executeQuery();
            if (rs.next()) {
                rankName = rs.getString("promot_to_rank_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return rankName;
    }

    @Override
    public List getCurrentRankList() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement(" select rank_code, rank_name from police_rank_map where is_active='Y' order by rank_name");

            rs = ps.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getString("rank_name"));
                so.setValue(rs.getString("rank_code"));
                li.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public List getCurrentRankListForDgBoard() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement(" select rank_code, rank_name from police_rank_map where is_visible_dgpolice='Y' order by rank_name");

            rs = ps.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getString("rank_name"));
                so.setValue(rs.getString("rank_code"));
                li.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public List getAllCurrentRankList() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement(" select rank_code, rank_name from police_rank_map order by rank_name");

            rs = ps.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getString("rank_name"));
                so.setValue(rs.getString("rank_code"));
                li.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public List getCurrentRankList(String currentRankCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement(" select rank_code, rank_name from police_rank_map  where is_active='Y' and rank_code=?");
            ps.setString(1, currentRankCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getString("rank_name"));
                so.setValue(rs.getString("rank_code"));
                li.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public List getAllNominatedRankList() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement(" select promot_to_rank_code, promot_to_rank_name from police_rank_map  where is_active='Y' ");
            rs = ps.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getString("promot_to_rank_name"));
                so.setValue(rs.getString("promot_to_rank_code"));
                li.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public List getNominatedRankList() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement(" select promot_to_rank_code, promot_to_rank_name from police_rank_map order by promot_to_rank_name");
            rs = ps.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getString("promot_to_rank_name"));
                so.setValue(rs.getString("promot_to_rank_code"));
                li.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public List getNominatedRankList(String rankCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement(" select promot_to_rank_code, promot_to_rank_name from police_rank_map  where is_active='Y' and rank_code=? ");
            ps.setString(1, rankCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getString("promot_to_rank_name"));
                so.setValue(rs.getString("promot_to_rank_code"));
                li.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public List getAllNominatedRankList(String rankCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement(" select promot_to_rank_code, promot_to_rank_name from police_rank_map  where rank_code=? ");
            ps.setString(1, rankCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getString("promot_to_rank_name"));
                so.setValue(rs.getString("promot_to_rank_code"));
                li.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public List getNominationList(String offCode, String nominationForCode, String fiscalyear) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select nomination_master_id, created_on, submitted_to_office, range_office_name submitted_to_Office_name,  submitted_on,  "
                    + " is_submitted, nomination_for_new_rank, post nomination_post, current_rank, current_post,  "
                    + " (select count(*) cnt from police_nomination_detail where nomination_master_id= tab.nomination_master_id  "
                    + "  and form_completed_status='N') completion_status from (  "
                    + " select nomination_master_id, created_on, submitted_to_office,  submitted_on,  "
                    + "	  nomination_for_new_rank, current_rank, "
                    + " post current_post, is_submitted  from  police_nomination_master pnm  "
                    + " inner join g_post on pnm.current_rank=g_post.post_code  "
                    + " where created_office=? and nomination_for_new_rank=? and is_archived='N' and entry_year=?) tab  "
                    + " inner join g_post on tab.nomination_for_new_rank=g_post.post_code  "
                    + " left outer join police_range_office_list pro on tab.submitted_to_office=pro.range_office_id::TEXT "
                    + " order by created_on desc ");
            ps.setString(1, offCode);
            ps.setString(2, nominationForCode);
            ps.setString(3, fiscalyear);
            rs = ps.executeQuery();
            while (rs.next()) {
                EmployeeDetailsForRank edt = new EmployeeDetailsForRank();
                edt.setNominationMasterId(rs.getInt("nomination_master_id") + "");
                edt.setCreatedOn(CommonFunctions.getFormattedOutputDatAndTime(rs.getTimestamp("created_on")));
                if (rs.getString("submitted_on") != null && !rs.getString("submitted_on").equals("")) {
                    edt.setSubmittedOn(CommonFunctions.getFormattedOutputDatAndTime(rs.getTimestamp("submitted_on")));
                } else {
                    edt.setSubmittedOn("");
                }
                if (rs.getString("submitted_to_Office_name") != null && !rs.getString("submitted_to_Office_name").equals("")) {
                    edt.setSubmittedToOffice(rs.getString("submitted_to_Office_name"));
                } else {
                    edt.setSubmittedToOffice("");
                }
                edt.setSltNominationForPost(rs.getString("nomination_post"));
                edt.setSltpostName(rs.getString("current_post"));
                edt.setSubmittedStatusForFieldOffice(rs.getString("is_submitted"));
                if (rs.getInt("completion_status") > 0) {
                    edt.setFormCompletionStatus("N");
                } else {
                    edt.setFormCompletionStatus("Y");
                }
                li.add(edt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public List getNominationListForGroupD(String offCode, String nominationForCode, String fiscalyear) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select nomination_master_id, created_on, submitted_to_office, range_office_name submitted_to_Office_name,  submitted_on,  "
                    + " is_submitted, nomination_for_new_rank, post nomination_post, current_rank, "
                    + " (select count(*) cnt from police_nomination_detail where nomination_master_id= tab.nomination_master_id  "
                    + "  and form_completed_status='N') completion_status from (  "
                    + " select nomination_master_id, created_on, submitted_to_office,  submitted_on,  "
                    + "	  nomination_for_new_rank, current_rank, "
                    + " is_submitted  from  police_nomination_master pnm  "
                    + " where created_office=? and nomination_for_new_rank=? and is_archived='N' and entry_year=?) tab  "
                    + " inner join g_post on tab.nomination_for_new_rank=g_post.post_code  "
                    + " left outer join police_range_office_list pro on tab.submitted_to_office=pro.range_office_id::TEXT "
                    + " order by created_on desc ");
            ps.setString(1, offCode);
            ps.setString(2, nominationForCode);
            ps.setString(3, fiscalyear);
            rs = ps.executeQuery();
            while (rs.next()) {
                EmployeeDetailsForRank edt = new EmployeeDetailsForRank();
                edt.setNominationMasterId(rs.getInt("nomination_master_id") + "");
                edt.setCreatedOn(CommonFunctions.getFormattedOutputDatAndTime(rs.getTimestamp("created_on")));
                if (rs.getString("submitted_on") != null && !rs.getString("submitted_on").equals("")) {
                    edt.setSubmittedOn(CommonFunctions.getFormattedOutputDatAndTime(rs.getTimestamp("submitted_on")));
                } else {
                    edt.setSubmittedOn("");
                }
                if (rs.getString("submitted_to_Office_name") != null && !rs.getString("submitted_to_Office_name").equals("")) {
                    edt.setSubmittedToOffice(rs.getString("submitted_to_Office_name"));
                } else {
                    edt.setSubmittedToOffice("");
                }
                edt.setSltNominationForPost(rs.getString("nomination_post"));
                edt.setSltpostName("GroupD");
                edt.setSubmittedStatusForFieldOffice(rs.getString("is_submitted"));
                if (rs.getInt("completion_status") > 0) {
                    edt.setFormCompletionStatus("N");
                } else {
                    edt.setFormCompletionStatus("Y");
                }
                li.add(edt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public List getSubmittedNominationList(String offCode, String currentRankCode, String nominatedrankCode, String fiscalyear) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement(" select nomination_master_id, created_on, submitted_on, is_submitted, submitted_to_dg_office, go.off_en, "
                    + " is_submitted_range_office, submitted_range_office_on, cgp.post current_post, ngp.post nomination_post, "
                    + " (select count(*) cnt from police_nomination_detail where nomination_master_id= pnm.nomination_master_id and "
                    + " form_recomendation_status='N') completion_status from police_nomination_master pnm "
                    + " inner join police_range_office_list prol on pnm.submitted_to_office=prol.range_office_id::TEXT "
                    + " inner join g_post cgp on pnm.current_rank=cgp.post_code "
                    + " inner join g_post ngp on pnm.nomination_for_new_rank=ngp.post_code "
                    + " inner join g_office go on pnm.created_office=go.off_code "
                    + " where is_submitted='Y' "
                    + " and nomination_for_new_rank= ? "
                    + " and range_office_code=?  and entry_year=? "
                    + " order by submitted_on desc ");
            ps.setString(1, nominatedrankCode);
            ps.setString(2, offCode);
            ps.setString(3, fiscalyear);
            rs = ps.executeQuery();
            while (rs.next()) {
                EmployeeDetailsForRank edt = new EmployeeDetailsForRank();
                edt.setNominationMasterId(rs.getInt("nomination_master_id") + "");
                edt.setSubmittedOn(CommonFunctions.getFormattedOutputDatAndTime(rs.getTimestamp("submitted_on")));
                edt.setSubmittedByOffice(rs.getString("off_en"));
                edt.setSltNominationForPost(rs.getString("nomination_post"));
                edt.setSltpostName(rs.getString("current_post"));
                edt.setForwardedOn(CommonFunctions.getFormattedOutputDatAndTime(rs.getTimestamp("submitted_range_office_on")));
                edt.setForwardedToOffice(rs.getString("submitted_to_dg_office"));
                edt.setForwardedStatus(rs.getString("is_submitted_range_office"));
                if (rs.getInt("completion_status") > 0) {
                    edt.setFormCompletionStatus("N");
                } else {
                    edt.setFormCompletionStatus("Y");
                }
                li.add(edt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public EmployeeDetailsForRank getNominationCreatedData(String offCode, int nominationMasterId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        EmployeeDetailsForRank edt = new EmployeeDetailsForRank();
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select nomination_master_id, created_on, submitted_to_office,  submitted_on, nomination_for_new_rank, current_rank  from  police_nomination_master "
                    + " where created_office=? and nomination_master_id=?");
            ps.setString(1, offCode);
            ps.setInt(2, nominationMasterId);
            rs = ps.executeQuery();
            if (rs.next()) {

                edt.setNominationMasterId(rs.getInt("nomination_master_id") + "");
                edt.setCreatedOn(CommonFunctions.getFormattedOutputDatAndTime(rs.getDate("created_on")));
                edt.setSubmittedOn(CommonFunctions.getFormattedOutputDatAndTime(rs.getDate("submitted_on")));
                edt.setSubmittedToOffice(rs.getString("submitted_to_office"));
                edt.setSltNominationForPost(rs.getString("nomination_for_new_rank"));
                edt.setSltpostName(rs.getString("current_rank"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return edt;
    }

    @Override
    public List getRankList(String offCode) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement(" select gpc, post from g_spc  "
                    + " inner join g_post on g_spc.gpc=g_post.post_code "
                    + " where off_code=?  "
                    + " group by gpc, post order by post");
            ps.setString(1, offCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getString("post"));
                so.setValue(rs.getString("gpc"));
                li.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    /* @Override
     public List getCreatedNominationList(String offCode, String currentrankCode, int nominationMasterId) {
     Connection con = null;
     PreparedStatement ps = null;
     ResultSet rs = null;

     List li = new ArrayList();

     try {
     con = dataSource.getConnection();

     if (currentrankCode.equals("140070")) {
     System.out.println("get emplist For Havildar post");
     ps = con.prepareStatement("select pnd.emp_id, pnd.gpf_no, fullname full_name, dob, "
     + " nomination_detail_id, pnm.nomination_master_id, pnd.form_completed_status "
     + " from police_nomination_master pnm \n"
     + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
     + " inner join police_nomination_form pne on pnd.emp_id=pne.empid "
     + " where pnm.created_office=? and pnm.nomination_master_id=? "
     + " union "
     + " select pnd.emp_id, pnd.gpf_no, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') full_name, dob, "
     + " nomination_detail_id, pnm.nomination_master_id, pnd.form_completed_status "
     + " from police_nomination_master pnm "
     + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
     + " inner join police_nominated_emp_list pne on pnd.gpf_no=pne.gpf_no "
     + " where pnm.created_office=? and pnm.nomination_master_id=?");
     ps.setString(1, offCode);
     ps.setInt(2, nominationMasterId);
     ps.setString(3, offCode);
     ps.setInt(4, nominationMasterId);
     rs = ps.executeQuery();
     while (rs.next()) {
     EmployeeDetailsForRank emp = new EmployeeDetailsForRank();
     emp.setEmpId(rs.getString("emp_id"));
     emp.setGpfno(rs.getString("gpf_no"));
     emp.setEmpName(rs.getString("full_name"));
     emp.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));

     emp.setFormCompletionStatus(rs.getString("form_completed_status"));
     emp.setNominationDetailId(rs.getInt("nomination_detail_id") + "");
     //emp.setFiscalyear(rs.getInt("entry_year") + "");
     li.add(emp);
     }
     } else {
     ps = con.prepareStatement(" select pnd.emp_id, pnd.gpf_no, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') full_name, dob, dos, "
     + " nomination_detail_id, pnm.nomination_master_id, pnd.form_completed_status,pnm.entry_year from police_nomination_master pnm "
     + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
     + " inner join police_nominated_emp_list pne on pnd.gpf_no=pne.gpf_no  "
     + "  where pnm.created_office=? and pnm.nomination_master_id=?");
     ps.setString(1, offCode);
     ps.setInt(2, nominationMasterId);
     rs = ps.executeQuery();
     while (rs.next()) {
     EmployeeDetailsForRank emp = new EmployeeDetailsForRank();
     emp.setGpfno(rs.getString("gpf_no"));
     emp.setEmpName(rs.getString("full_name"));
     emp.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
     emp.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
     emp.setFormCompletionStatus(rs.getString("form_completed_status"));
     emp.setNominationDetailId(rs.getInt("nomination_detail_id") + "");
     emp.setNominationMasterId(rs.getInt("nomination_master_id") + "");
     emp.setFiscalyear(rs.getString("entry_year"));
     li.add(emp);
     }
     }

     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(con);
     }

     return li;
     } */
    @Override
    public List getCreatedNominationList(String offCode, String currentrankCode, int nominationMasterId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = dataSource.getConnection();

            ps = con.prepareStatement(" select pnd.emp_id, pnd.gpf_no, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') full_name, dob, dos, "
                    + " nomination_detail_id, pnm.nomination_master_id, pnd.form_completed_status,pnm.entry_year from police_nomination_master pnm "
                    + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                    + " inner join police_nominated_emp_list pne on pnd.gpf_no=pne.gpf_no  "
                    + "  where pnm.created_office=? and pnm.nomination_master_id=?");
            ps.setString(1, offCode);
            ps.setInt(2, nominationMasterId);
            rs = ps.executeQuery();
            while (rs.next()) {
                EmployeeDetailsForRank emp = new EmployeeDetailsForRank();
                emp.setEmpId(rs.getString("emp_id"));
                emp.setGpfno(rs.getString("gpf_no"));
                emp.setEmpName(rs.getString("full_name"));
                emp.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                emp.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                emp.setFormCompletionStatus(rs.getString("form_completed_status"));
                emp.setNominationDetailId(rs.getInt("nomination_detail_id") + "");
                emp.setNominationMasterId(rs.getInt("nomination_master_id") + "");
                emp.setFiscalyear(rs.getString("entry_year"));
                li.add(emp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public List getSubmittedNominationList(String offCode, String currentrankCode, int nominationMasterId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = dataSource.getConnection();

            ps = con.prepareStatement(" select pnd.emp_id, pnd.gpf_no, fullname, dob, dor, "
                    + "  nomination_detail_id, pnf.nomination_master_id, pnd.form_completed_status from police_nomination_form pnf "
                    + "  inner join police_nomination_detail pnd on pnf.nominationdetailid=pnd.nomination_detail_id "
                    + "  where pnf.officecode=? and pnf.nomination_master_id=? ");
            ps.setString(1, offCode);
            ps.setInt(2, nominationMasterId);

            rs = ps.executeQuery();
            while (rs.next()) {
                EmployeeDetailsForRank emp = new EmployeeDetailsForRank();

                emp.setEmpId(rs.getString("emp_id"));

                emp.setGpfno(rs.getString("gpf_no"));
                emp.setEmpName(rs.getString("fullname"));
                emp.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                emp.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dor")));
                emp.setFormCompletionStatus(rs.getString("form_completed_status"));
                emp.setNominationDetailId(rs.getInt("nomination_detail_id") + "");
                emp.setNominationMasterId(rs.getInt("nomination_master_id") + "");
                li.add(emp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public List getNominatedEmployeeList(int nominationMasterId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        String nominationFor = "";

        try {
            con = dataSource.getConnection();

            /*
             ps = con.prepareStatement(" select pnd.emp_id, pnd.gpf_no, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') full_name, dob, dos, "
             + " nomination_detail_id, pnm.nomination_master_id, pnd.form_completed_status, form_recomendation_status from police_nomination_master pnm "
             + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
             + " inner join police_nominated_emp_list pnel on pnd.emp_id=pnel.gpf_no"
             + " where pnm.nomination_master_id=? order by pnel.f_name ");
             */
            ps = con.prepareStatement("select pnd.emp_id, pnd.gpf_no, fullname, dob,  "
                    + "         nomination_detail_id, pnm.nomination_master_id, pnd.form_completed_status, form_recomendation_status"
                    + "		 from police_nomination_master pnm "
                    + "           inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id"
                    + "            inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                    + "          where pnm.nomination_master_id=? order by pnf.fullname");

            ps.setInt(1, nominationMasterId);

            rs = ps.executeQuery();
            while (rs.next()) {
                EmployeeDetailsForRank emp = new EmployeeDetailsForRank();
                emp.setEmpId(rs.getString("emp_id"));
                emp.setGpfno(rs.getString("gpf_no"));
                emp.setEmpName(rs.getString("fullname"));
                emp.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                //emp.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                //emp.setDoj(CommonFunctions.getFormattedOutputDate1(rs.getDate("joindate_of_goo")));
                emp.setFormCompletionStatus(rs.getString("form_completed_status"));
                emp.setNominationDetailId(rs.getInt("nomination_detail_id") + "");
                emp.setNominationMasterId(rs.getInt("nomination_master_id") + "");
                emp.setRecommendationStatus(rs.getString("form_recomendation_status"));
                li.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    /*@Override
     public HashMap getEmployeeListGpfNowise(String gpfno) {
     Connection con = null;
     PreparedStatement ps = null;
     PreparedStatement ps1 = null;
     ResultSet rs = null;
     ResultSet rs1 = null;
     HashMap<String, Object> map = new HashMap<>();
     boolean dataFound = false;
     List li = new ArrayList();
     try {
     con = dataSource.getConnection();
     ps = con.prepareStatement("select emp_mast.emp_id, emp_mast.gpf_no, ARRAY_TO_STRING(ARRAY[emp_mast.INITIALS, emp_mast.F_NAME, emp_mast.M_NAME, emp_mast.L_NAME], ' ') full_name, emp_mast.dob, emp_mast.dos, emp_mast.gender, emp_mast.category,  "
     + " (SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') FATHERS_NAME "
     + " FROM EMP_RELATION WHERE RELATION='FATHER' AND EMP_ID=EMP_MAST.EMP_ID limit 1) FATHERS_NAME "
     + " from emp_mast "
     + " left outer join police_nominated_emp_list pnei on emp_mast.gpf_no=pnei.gpf_no "
     + " where emp_mast.gpf_no=? and pnei.gpf_no is null and pnei.entry_year=?");
     ps.setString(1, gpfno);
     rs = ps.executeQuery();
     while (rs.next()) {
     EmployeeDetailsForRank emp = new EmployeeDetailsForRank();
     emp.setEmpId(rs.getString("emp_id"));
     emp.setGpfno(rs.getString("gpf_no"));
     emp.setEmpName(rs.getString("full_name"));
     emp.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
     emp.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
     emp.setFathersName(rs.getString("FATHERS_NAME"));
     li.add(emp);
     dataFound = true;
     }
     map.put("emplist", li);
     if (dataFound == true) {
     map.put("msg", "Y");
     } else if (dataFound != true) {
     int recordFound = 0;
     con = dataSource.getConnection();
     ps1 = con.prepareStatement("select count(*) from emp_mast where gpf_no=?");
     ps1.setString(1, gpfno);

     rs1 = ps1.executeQuery();
     if (rs1.next()) {
     recordFound = rs.getInt("CNT");
     dataFound = true;
     }
     map.put("msg", "N");
     } else {
     map.put("msg", "D");
     }
     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(con);
     }

     return map;
     } */
    @Override
    public List getEmployeeListGpfNowise(String gpfno) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select emp_mast.emp_id, emp_mast.gpf_no, ARRAY_TO_STRING(ARRAY[emp_mast.INITIALS, emp_mast.F_NAME, emp_mast.M_NAME, emp_mast.L_NAME], ' ') full_name, emp_mast.dob, emp_mast.dos, emp_mast.gender, emp_mast.category,  "
                    + " (SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') FATHERS_NAME "
                    + " FROM EMP_RELATION WHERE RELATION='FATHER' AND EMP_ID=EMP_MAST.EMP_ID limit 1) FATHERS_NAME "
                    + " from emp_mast "
                    + " left outer join police_nominated_emp_list pnei on emp_mast.gpf_no=pnei.gpf_no "
                    + " where emp_mast.gpf_no=? and pnei.gpf_no is null");
            ps.setString(1, gpfno);
            rs = ps.executeQuery();
            while (rs.next()) {
                EmployeeDetailsForRank emp = new EmployeeDetailsForRank();
                emp.setEmpId(rs.getString("emp_id"));
                emp.setGpfno(rs.getString("gpf_no"));
                emp.setEmpName(rs.getString("full_name"));
                emp.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                emp.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                emp.setFathersName(rs.getString("FATHERS_NAME"));
                li.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }


    /*@Override
     public List getEmployeeListRankWise(String postName, String offCode) {
     Connection con = null;
     PreparedStatement ps = null;
     ResultSet rs = null;

     List li = new ArrayList();

     try {
     con = dataSource.getConnection();

     if (postName.equals("140070")) {
     ps = con.prepareStatement(" select empid emp_id, fullname full_name, dob, category, fathersname   from police_nomination_form  pnf "
     + " left outer join  "
     + " (select emp_id from police_nomination_detail where (nominated_type='EXM' or nominated_type='NOM'))  pnd on pnf.empid=pnd.emp_id "
     + " where officecode=? and final_qualified ='Y' and pnd.emp_id is null "
     + " union "
     + " SELECT pnel.emp_id, ARRAY_TO_STRING(ARRAY[pnel.INITIALS, pnel.F_NAME,pnel. M_NAME, pnel.L_NAME], ' ') full_name, "
     + " dob, category, fathers_name from police_nominated_emp_list pnel "
     + " left outer join (select emp_id from police_nomination_detail where (nominated_type='EXM' or nominated_type='NOM'))  pnd on pnel.emp_id=pnd.emp_id   \n"
     + " where estd_code=? and current_rank_code=? and pnd.emp_id is null");
     ps.setString(1, offCode);
     ps.setString(2, offCode);
     ps.setString(3, postName);
     rs = ps.executeQuery();
     while (rs.next()) {
     EmployeeDetailsForRank emp = new EmployeeDetailsForRank();
     emp.setEmpId(rs.getString("emp_id"));
     emp.setEmpName(rs.getString("full_name"));
     emp.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
     emp.setFathersName(rs.getString("fathersname"));
     li.add(emp);
     }
     } else {
     ps = con.prepareStatement("SELECT police_nominated_id, pnel.emp_id, pnel.gpf_no, ARRAY_TO_STRING(ARRAY[pnel.INITIALS, pnel.F_NAME,pnel. M_NAME, pnel.L_NAME], ' ') full_name, pnel.dob, pnel.dos, pnel.gender, "
     + " category, fathers_name, pnel.current_rank_code, pnel.current_rank_name, nominated_rank_code, nominated_rank_name, estd_code, estd_name "
     + "	FROM police_nominated_emp_list pnel "
     + " left outer join police_nomination_detail pnd on pnel.gpf_no=pnd.gpf_no "
     + " where estd_code=? and current_rank_code=? and pnd.gpf_no is null order by f_name");
     ps.setString(1, offCode);
     ps.setString(2, postName);
     rs = ps.executeQuery();
     while (rs.next()) {
     EmployeeDetailsForRank emp = new EmployeeDetailsForRank();
     emp.setEmpId(rs.getString("emp_id"));
     emp.setGpfno(rs.getString("gpf_no"));
     emp.setEmpName(rs.getString("full_name"));
     emp.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
     emp.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
     emp.setFathersName(rs.getString("fathers_name"));
     li.add(emp);
     }
     }

     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(con);
     }

     return li;
     } */
    public List getEmployeeListRankWise(String postName, String offCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = dataSource.getConnection();

            ps = con.prepareStatement("SELECT police_nominated_id, pnel.emp_id, pnel.gpf_no, ARRAY_TO_STRING(ARRAY[pnel.INITIALS, pnel.F_NAME,pnel. M_NAME, pnel.L_NAME], ' ') full_name, pnel.dob, pnel.dos, pnel.gender, "
                    + " category, fathers_name, pnel.current_rank_code, pnel.current_rank_name, nominated_rank_code, nominated_rank_name, estd_code, estd_name "
                    + "	FROM police_nominated_emp_list pnel "
                    + " left outer join police_nomination_detail pnd on pnel.gpf_no=pnd.gpf_no "
                    + " where estd_code=? and current_rank_code=? and pnd.gpf_no is null order by f_name");
            ps.setString(1, offCode);
            ps.setString(2, postName);
            rs = ps.executeQuery();
            while (rs.next()) {
                EmployeeDetailsForRank emp = new EmployeeDetailsForRank();
                emp.setEmpId(rs.getString("emp_id"));
                emp.setGpfno(rs.getString("gpf_no"));
                emp.setEmpName(rs.getString("full_name"));
                emp.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                emp.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                emp.setFathersName(rs.getString("fathers_name"));
                li.add(emp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public List getEmployeeListRankWiseExcludedAlreadyMapped(String postName, String offCode, int nominationMasterId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT police_nominated_id, emp_id, gpf_no, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') full_name, dob, dos, gender, "
                    + " category, fathers_name, current_rank_code, current_rank_name, nominated_rank_code, nominated_rank_name, estd_code, estd_name "
                    + "	FROM police_nominated_emp_list where estd_code=? and current_rank_code=? and gpf_no not in (select gpf_no from police_nomination_detail where current_rank=? )  order by f_name");
            ps.setString(1, offCode);
            ps.setString(2, postName);
            ps.setString(3, postName);
            rs = ps.executeQuery();
            while (rs.next()) {
                EmployeeDetailsForRank emp = new EmployeeDetailsForRank();
                emp.setEmpId(rs.getString("emp_id"));
                emp.setGpfno(rs.getString("gpf_no"));
                emp.setEmpName(rs.getString("full_name"));
                emp.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                emp.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                emp.setFathersName(rs.getString("fathers_name"));
                li.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    /*@Override
     public void createNomination(String gpfno, String currentPost, String nominationPost, String offCode, String loginUserName, String fiscalyear) {
     Connection con = null;
     PreparedStatement ps = null;
     PreparedStatement ps2 = null;
     ResultSet rs = null;
     ResultSet rs2 = null;
     String createdDateTime = "";
     Calendar cal = Calendar.getInstance();
     DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh.mm aa");
     createdDateTime = dateFormat.format(cal.getTime());
     String nominatedType = "NOM";
     try {
     con = dataSource.getConnection();
     ps = con.prepareStatement("insert into police_nomination_master (created_on, created_by_user, created_office, nomination_for_new_rank, current_rank,entry_year ) values(?,?,?,?,?,?) ", Statement.RETURN_GENERATED_KEYS);
     ps.setTimestamp(1, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
     ps.setString(2, loginUserName);
     ps.setString(3, offCode);
     ps.setString(4, nominationPost);
     ps.setString(5, currentPost);
     System.out.println("nominationPost for entry in master table" +nominationPost);
     System.out.println("currentPost for entry in master table" +currentPost);
     ps.setString(6, fiscalyear);
     ps.executeUpdate();
     rs = ps.getGeneratedKeys();
     rs.next();
     int nominationMaxId = rs.getInt("nomination_master_id");
     if (currentPost.equals("140070")) {
     System.out.println("create nomination for entry in police_nomination_detail table");
     ps = con.prepareStatement("insert into police_nomination_detail (nomination_master_id,emp_id,form_completed_status, current_rank, nominated_type,exam_year) values (?,?,?,?,?,?) ");

     String gpfnoArr[] = gpfno.split(",");

     ps2 = con.prepareStatement(" select fullname from police_nomination_form where empid=? and final_qualified ='Y' ");

     for (int i = 0; i < gpfnoArr.length; i++) {
     ps2.setString(1, gpfnoArr[i]);
     rs2 = ps2.executeQuery();
     if (rs2.next()) {
     nominatedType = "EXM";
     } else {
     nominatedType = "NOM";
     }

     ps.setInt(1, nominationMaxId);
     ps.setString(2, gpfnoArr[i]);
     ps.setString(3, "N");
     ps.setString(4, currentPost);
     ps.setString(5, nominatedType);
     ps.setString(6, "");
     ps.execute();
     }
     } else {
     System.out.println("create nomination for entry in police_nomination_detail table of all type");
     ps = con.prepareStatement("insert into police_nomination_detail (nomination_master_id,gpf_no,form_completed_status, current_rank, nominated_type,entry_year) values (?,?,?,?,?,?) ");

     String gpfnoArr[] = gpfno.split(",");

     for (int i = 0; i < gpfnoArr.length; i++) {
     ps.setInt(1, nominationMaxId);
     ps.setString(2, gpfnoArr[i]);
     ps.setString(3, "N");
     ps.setString(4, currentPost);
     ps.setString(5, nominatedType);
     ps.execute();
     }
     }

     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(con);
     }
     } */
    @Override
    public void createNomination(String gpfno, String currentPost, String nominationPost, String offCode, String loginUserName, String fiscalyear) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        String createdDateTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh.mm aa");
        createdDateTime = dateFormat.format(cal.getTime());
        String nominatedType = "NOM";
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("insert into police_nomination_master (created_on, created_by_user, created_office, nomination_for_new_rank, current_rank,entry_year ) values(?,?,?,?,?,?) ", Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            ps.setString(2, loginUserName);
            ps.setString(3, offCode);
            ps.setString(4, nominationPost);
            ps.setString(5, currentPost);
            ps.setString(6, fiscalyear);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            rs.next();
            int nominationMaxId = rs.getInt("nomination_master_id");
            ps = con.prepareStatement("insert into police_nomination_detail (nomination_master_id,gpf_no,form_completed_status, current_rank, nominated_type,entry_year) values (?,?,?,?,?,?) ");

            String gpfnoArr[] = gpfno.split(",");

            for (int i = 0; i < gpfnoArr.length; i++) {

                ps.setInt(1, nominationMaxId);
                ps.setString(2, gpfnoArr[i]);
                ps.setString(3, "N");
                ps.setString(4, currentPost);
                ps.setString(5, nominatedType);
                ps.setString(6, fiscalyear);
                ps.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void addSearchEmployee2List(String gpfno, String loginOffCode, String loginOfficename, String currentPost, String nominatedPost, String fiscalyear) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        try {

            con = dataSource.getConnection();
            ps = con.prepareStatement("insert into police_nominated_emp_list (emp_id, gpf_no, initials, f_name, m_name, l_name, dob, dos, gender, "
                    + " category, fathers_name, current_rank_code, current_rank_name, nominated_rank_code, nominated_rank_name,property_submitted_on_hrms, estd_code, estd_name,entry_year) "
                    + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");

            ps2 = con.prepareStatement(" select emp_id, gpf_no, initials, f_name, m_name, l_name, dob, dos, gender, category, "
                    + " (SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') FATHERS_NAME "
                    + " FROM EMP_RELATION WHERE RELATION='FATHER' AND EMP_ID=EMP_MAST.EMP_ID) FATHERS_NAME, "
                    + " (SELECT POST FROM G_POST WHERE POST_CODE=?) CURRENT_RANK_NAME, "
                    + " (SELECT POST FROM G_POST WHERE POST_CODE=?) NOMINATED_RANK_NAME, "
                    + " (SELECT submitted_on FROM property_statement_list WHERE EMP_ID=EMP_MAST.EMP_ID and financial_year='2023' and status_id=1) PROPERTY_SUBMITTED_ON_HRMS "
                    + " from emp_mast  "
                    + " where gpf_no=?");

            ps2.setString(1, currentPost);
            ps2.setString(2, nominatedPost);
            ps2.setString(3, gpfno);
            rs = ps2.executeQuery();
            while (rs.next()) {
                ps.setString(1, rs.getString("emp_id"));
                ps.setString(2, rs.getString("gpf_no"));
                ps.setString(3, rs.getString("initials"));
                ps.setString(4, rs.getString("f_name"));
                ps.setString(5, rs.getString("m_name"));
                ps.setString(6, rs.getString("l_name"));
                ps.setTimestamp(7, rs.getTimestamp("dob"));
                ps.setTimestamp(8, rs.getTimestamp("dos"));
                ps.setString(9, rs.getString("gender"));
                ps.setString(10, rs.getString("category"));
                ps.setString(11, rs.getString("fathers_name"));
                ps.setString(12, currentPost);
                ps.setString(13, rs.getString("CURRENT_RANK_NAME"));
                ps.setString(14, nominatedPost);
                ps.setString(15, rs.getString("NOMINATED_RANK_NAME"));
                ps.setTimestamp(16, rs.getTimestamp("property_submitted_on_hrms"));
                ps.setString(17, loginOffCode);
                ps.setString(18, loginOfficename);
                ps.setString(19, fiscalyear);
                ps.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void addNewEmployee(String gpfno, int nominationMaxId, String currentPost) {
        Connection con = null;
        PreparedStatement ps = null;
        try {

            con = dataSource.getConnection();
            ps = con.prepareStatement("insert into police_nomination_detail (nomination_master_id, gpf_no, form_completed_status, current_rank ) values (?,?,?,?) ");

            String empIdArr[] = gpfno.split(",");

            for (int i = 0; i < empIdArr.length; i++) {
                ps.setInt(1, nominationMaxId);
                ps.setString(2, empIdArr[i]);
                ps.setString(3, "N");
                ps.setString(4, currentPost);
                ps.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteEmployee(int nominationMasterId, int nominationDetailId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean found = false;
        try {
            con = dataSource.getConnection();

            ps = con.prepareStatement("select nomination_master_id from police_nomination_detail where nomination_master_id=? and nomination_detail_id=? ");
            ps.setInt(1, nominationMasterId);
            ps.setInt(2, nominationDetailId);
            rs = ps.executeQuery();
            if (rs.next()) {
                found = true;
            }

            //if (found == false) {
            if (found == true) {

                ps = con.prepareStatement("delete from police_nomination_detail where nomination_master_id=? and nomination_detail_id=?");
                ps.setInt(1, nominationMasterId);
                ps.setInt(2, nominationDetailId);
                ps.executeUpdate();

                ps = con.prepareStatement("delete from police_nomination_form where nomination_master_id=? and nominationdetailid=?");
                ps.setInt(1, nominationMasterId);
                ps.setInt(2, nominationDetailId);
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    /*@Override
     public NominationForm getEmployeeBeforeNominationData(int nominationMasterId, int nominationDetailId, String currentrank) {
     Connection con = null;
     PreparedStatement ps = null;
     ResultSet rs = null;
     NominationForm nfm = null;

     try {
     con = dataSource.getConnection();

     if (currentrank.equals("140070")) {
     ps = con.prepareStatement("SELECT * FROM police_nomination_detail pnd "
     + "  inner join police_nomination_form pnf on pnd.emp_id=pnf.empid "
     + "  where pnd.nomination_master_id=? and pnd.nomination_detail_id=? "
     + "  and nominated_type='EXM' and final_qualified='Y' ");
     ps.setInt(1, nominationMasterId);
     ps.setInt(2, nominationDetailId);
     rs = ps.executeQuery();
     if (rs.next()) {
     nfm = new NominationForm();
     nfm.setSltpostName(rs.getString("current_rank"));
     nfm.setNominationtype("EXM");
     nfm.setOfficeCode(rs.getString("officecode"));
     nfm.setOfficeName(rs.getString("officename"));
     nfm.setCategory(rs.getString("category"));
     nfm.setEmpId(rs.getString("empid"));
     nfm.setFullname(rs.getString("fullname"));
     nfm.setFathersname(rs.getString("fathersname"));
     nfm.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
     nfm.setQualification(rs.getString("qualification"));
     nfm.setHomeDistrict(rs.getString("homedistrict"));
     //nfm.setHomedistrictName(rs.getString("dist_name"));
     nfm.setPostingUnintDoj(CommonFunctions.getFormattedOutputDate1(rs.getDate("postingunintdoj")));
     nfm.setDoeGov(CommonFunctions.getFormattedOutputDate1(rs.getDate("final_appointment_date")));
     nfm.setJodInspector(CommonFunctions.getFormattedOutputDate1(rs.getDate("jodinspector")));

     //String dateofAbsorption = rs.getString("final_appointment_date");
     //Converting String to Date
     SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
     Date date = formatter.parse(nfm.getDoeGov());
     //Converting obtained Date object to LocalDate object
     Instant instant = date.toInstant();
     ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
     LocalDate givenDate = zone.toLocalDate();

     Date date2 = formatter.parse("01-JAN-2020");
     //Converting obtained Date object to LocalDate object
     Instant instant2 = date2.toInstant();
     ZonedDateTime zone2 = instant2.atZone(ZoneId.systemDefault());
     LocalDate nowDate = zone2.toLocalDate();

     //Calculating the difference between given date to current date.
     Period period = Period.between(givenDate, nowDate);

     nfm.setYearinService(period.getYears() + "");
     nfm.setMonthrinService(period.getMonths() + "");
     nfm.setDaysinService(period.getDays() + "");

     nfm.setPeriodParticularsTraining(rs.getString("periodparticularstraining"));

     nfm.setRewardGSMark(rs.getString("rewardgsmark"));
     nfm.setRewardGSMarkPrior(rs.getString("rewardgsmarkprior"));
     nfm.setRewardGSMarkDuring(rs.getString("rewardgsmarkduring"));
     nfm.setRewardGSMarkFrom(rs.getString("rewardgsmarkfrom"));

     nfm.setRewardMoneyOher(rs.getString("rewardmoneyoher"));
     nfm.setRewardMoneyOherPrior(rs.getString("rewardmoneyoherprior"));
     nfm.setRewardMoneyOherDuring(rs.getString("rewardmoneyoherduring"));
     nfm.setRewardMoneyOherFrom(rs.getString("rewardmoneyoherfrom"));

     nfm.setRewardMedals(rs.getString("rewardmedals"));
     nfm.setRewardMedalsPrior(rs.getString("rewardmedalsprior"));
     nfm.setRewardMedalsDuring(rs.getString("rewardmedalsduring"));
     nfm.setRewardMedalsFrom(rs.getString("rewardmedalsfrom"));

     nfm.setPunishmentMajor(rs.getString("punishmentmajor"));
     nfm.setPunishmentMajorPrior(rs.getString("punishmentmajorprior"));
     nfm.setPunishmentMajorDuring(rs.getString("punishmentmajorduring"));
     nfm.setPunishmentMajorFrom(rs.getString("punishmentmajorfrom"));

     nfm.setPunishmentMinor(rs.getString("punishmentminor"));
     nfm.setPunishmentMinorPrior(rs.getString("punishmentminorprior"));
     nfm.setPunishmentMinorDuring(rs.getString("punishmentminorduring"));
     nfm.setPunishmentMinorFrom(rs.getString("punishmentminorfrom"));

     nfm.setPowerofDecesion(rs.getString("powerofdecesion"));
     nfm.setPhysicalFitness(rs.getString("physicalfitness"));
     nfm.setMentalFitness(rs.getString("mentalfitness"));
     nfm.setHonestyIntegrity(rs.getString("honestyintegrity"));
     nfm.setAdverseIfany(rs.getString("adverseifany"));
     nfm.setDpcifany(rs.getString("dpcifany"));

     nfm.setDateofServing(CommonFunctions.getFormattedOutputDate1(rs.getDate("dateofserving")));
     if (rs.getString("dateofserving") != null && !rs.getString("dateofserving").equals("")) {
     nfm.setDateofServingifAny("Y");
     nfm.setDateofServing(CommonFunctions.getFormattedOutputDate1(rs.getDate("dateofserving")));
     }
     nfm.setRemarksNominatingProfessional(rs.getString("remarksnominatingprofessional"));
     nfm.setRemarksNominationGeneral(rs.getString("remarksnominationgeneral"));

     nfm.setRemarkRecommendation(rs.getString("remarks_recommend_authority"));
     nfm.setRecommendStatus(rs.getString("recommend_status"));
     nfm.setNominationDetailId(nominationDetailId + "");
     nfm.setNominationMasterId(nominationMasterId + "");
     } else {
     ps = con.prepareStatement("SELECT police_nominated_id, police_nomination_detail.emp_id, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') full_name, dob, dos, gender, "
     + " category, fathers_name, current_rank_code, current_rank_name, nominated_rank_code, nominated_rank_name, estd_code, estd_name "
     + "	FROM police_nomination_detail  "
     + " inner join  police_nominated_emp_list on  police_nomination_detail.emp_id=police_nominated_emp_list.emp_id "
     + " where nomination_master_id=? and nomination_detail_id=? order by f_name");
     ps.setInt(1, nominationMasterId);
     ps.setInt(2, nominationDetailId);
     rs = ps.executeQuery();
     if (rs.next()) {
     nfm = new NominationForm();
     nfm.setFullname(rs.getString("full_name"));
     nfm.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
     nfm.setOfficeCode(rs.getString("estd_code"));
     nfm.setOfficeName(rs.getString("estd_name"));
     nfm.setFathersname(rs.getString("fathers_name"));
     nfm.setCategory(rs.getString("category"));
     nfm.setSltpostName(rs.getString("current_rank_code"));
     nfm.setNominationtype("NOM");
     nfm.setNominationDetailId(nominationDetailId + "");
     nfm.setNominationMasterId(nominationMasterId + "");
     }
     }
     } else {
     ps = con.prepareStatement("SELECT police_nominated_id, police_nomination_detail.emp_id, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') full_name, dob, dos, gender, "
     + " category, fathers_name, current_rank_code, current_rank_name, nominated_rank_code, nominated_rank_name, estd_code, estd_name "
     + " FROM police_nomination_detail  "
     + " inner join  police_nominated_emp_list on  police_nomination_detail.gpf_no=police_nominated_emp_list.gpf_no "
     + " where nomination_master_id=? and nomination_detail_id=? order by f_name");
     ps.setInt(1, nominationMasterId);
     ps.setInt(2, nominationDetailId);
     rs = ps.executeQuery();
     if (rs.next()) {
     nfm = new NominationForm();
     nfm.setFullname(rs.getString("full_name"));
     nfm.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
     nfm.setOfficeCode(rs.getString("estd_code"));
     nfm.setOfficeName(rs.getString("estd_name"));
     nfm.setFathersname(rs.getString("fathers_name"));
     nfm.setCategory(rs.getString("category"));
     nfm.setNominationDetailId(nominationDetailId + "");
     nfm.setNominationMasterId(nominationMasterId + "");
     nfm.setSltpostName(rs.getString("current_rank_code"));
     nfm.setSltNominationForPost(rs.getString("nominated_rank_code"));
     }
     }
     //office_order_no
     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(con);
     }
     return nfm;
     } */
    public NominationForm getEmployeeBeforeNominationData(int nominationMasterId, int nominationDetailId, String currentrank) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        NominationForm nfm = null;

        try {
            con = dataSource.getConnection();

            ps = con.prepareStatement("SELECT police_nomination_detail.gpf_no,police_nominated_id, emp_mast.emp_id, ARRAY_TO_STRING(ARRAY[police_nominated_emp_list.INITIALS, police_nominated_emp_list.F_NAME, police_nominated_emp_list.M_NAME, police_nominated_emp_list.L_NAME], ' ') full_name, police_nominated_emp_list.dob, police_nominated_emp_list.dos, police_nominated_emp_list.gender, "
                    + "police_nominated_emp_list.category, fathers_name, current_rank_code, current_rank_name, nominated_rank_code, nominated_rank_name, estd_code, estd_name,property_submitted_on_hrms "
                    + "FROM police_nomination_detail  "
                    + "inner join  police_nominated_emp_list on  police_nomination_detail.gpf_no=police_nominated_emp_list.gpf_no "
                    + "inner join emp_mast on police_nomination_detail.gpf_no= emp_mast.gpf_no "
                    + "where nomination_master_id=? and nomination_detail_id=? order by police_nominated_emp_list.f_name");
            ps.setInt(1, nominationMasterId);
            ps.setInt(2, nominationDetailId);
            rs = ps.executeQuery();
            if (rs.next()) {
                nfm = new NominationForm();
                nfm.setGpfNo(rs.getString("gpf_no"));
                nfm.setEmpId(rs.getString("emp_id"));
                nfm.setFullname(rs.getString("full_name"));
                nfm.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                nfm.setOfficeCode(rs.getString("estd_code"));
                nfm.setOfficeName(rs.getString("estd_name"));
                nfm.setFathersname(rs.getString("fathers_name"));
                nfm.setCategory(rs.getString("category"));
                nfm.setNominationDetailId(nominationDetailId + "");
                nfm.setNominationMasterId(nominationMasterId + "");
                nfm.setSltpostName(rs.getString("current_rank_code"));
                nfm.setSltNominationForPost(rs.getString("nominated_rank_code"));
                nfm.setDateofPropertySubmittedByHRMS(CommonFunctions.getFormattedOutputDate1(rs.getDate("property_submitted_on_hrms")));
            }

//office_order_no
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return nfm;
    }

    @Override
    public NominationForm getEmployeeNominationData(int nominationMasterId, int nominationDetailId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        NominationForm nfm = null;

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement(" select pnf.gpfno,pnf.nomination_form_id , pnf.nomination_master_id , nominationdetailid, officecode, officename, category , "
                    + " empid , fullname, fathersname , pnf.dob , qualification,  homedistrict, dist_name, postingunintdoj,  doegov, jodinspector, "
                    + " yearinservicepolicestation, monthrinservicepolicestation , daysinservicepolicestation, periodparticularstraining, rewardgsmark, "
                    + " rewardgsmarkprior, rewardgsmarkduring, rewardgsmarkfrom, rewardmoneyoher, rewardmoneyoherprior, rewardmoneyoherduring, "
                    + " rewardmoneyoherfrom, rewardmedals, rewardmedalsprior, rewardmedalsduring, rewardmedalsfrom, punishmentmajor, punishmentmajorprior, "
                    + " punishmentmajorduring, punishmentmajorfrom, punishmentminor, punishmentminorprior, punishmentminorduring, punishmentminorfrom, "
                    + " powerofdecesion, physicalfitness, mentalfitness, honestyintegrity, adverseifany, dpcifany, dateofserving, "
                    + " remarksnominatingprofessional, remarksnominationgeneral, remarks_recommend_authority, recommend_status, "
                    + " nominated_type, physical_fitness_status, accomplishment_details, pnd.current_rank, declaration_accept, office_order_no, "
                    + " passing_training_date, disc_details, place_posting, nomination_for_post, remarks_of_cdmo,recommending_authority_view,csb_remarks,cadre_code, "
                    + " serving_charge_of_casedate,gender,initial_rank,appoint_mode,is_any_promotion_earlier,date_of_joining_in_promotional_rank, "
                    + " date_of_joining_in_present_rank_district,present_rank_district_name,is_completed_regular_tenyears,is_contempleted_as_ondate,current_designation, "
                    + " promotional_rank_name,district_recommendation_status,dateofserving_if_any_dp,district_recommendation_view,is_pass_prelimilaryexam,powerof_decission_making,adverse_detail, "
                    + " passing_place_constable_training,passing_date_from_constable_training,passing_date_to_constable_training,orderno_passing__constable_training,orderdate_passing__constable_training,punishment_detail,rank_joinin_govservice, "
                    + " criminal_status_detail,date_for_present_criminal_status,criminal_case_present_status_ifany,ccrollsdetail,property_submitted_if_any,property_submitted_on_hrms,property_submitted_on_hrms_byofficer, "
                    + " present_rank,date_of_distraining,orderno_of_distraining,total_punishment1,total_punishment2,total_punishment3,servingchargedetail,yearinservicepresentrank,monthinservicepresentrank,daysinservicepresentrank, "
                    + " deptexampassifany,grade_serial_no,have_passed_training_asi,battalion_work_experienceifany,orderno_passing_training_asi,dateof_passing_training_asi from police_nomination_form pnf "
                    + " left outer join g_district on pnf.homedistrict=g_district.dist_code "
                    + " inner join police_nomination_detail pnd on pnf.nominationdetailid=pnd.nomination_detail_id "
                    + " where pnf.nomination_master_id=? and nominationdetailid=? ");
            ps.setInt(1, nominationMasterId);
            ps.setInt(2, nominationDetailId);
            rs = ps.executeQuery();
            if (rs.next()) {
                nfm = new NominationForm();

                nfm.setNominationtype(rs.getString("nominated_type"));
                nfm.setNominationMasterId(rs.getInt("nomination_master_id") + "");
                nfm.setNominationDetailId(rs.getInt("nominationdetailid") + "");
                nfm.setNominationFormId(rs.getInt("nomination_form_id") + "");
                nfm.setOfficeCode(rs.getString("officecode"));
                nfm.setOfficeName(rs.getString("officename"));
                nfm.setCategory(rs.getString("category"));
                nfm.setEmpId(rs.getString("empid"));
                nfm.setFullname(rs.getString("fullname"));
                nfm.setFathersname(rs.getString("fathersname"));
                nfm.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                nfm.setQualification(rs.getString("qualification"));
                nfm.setHomeDistrict(rs.getString("homedistrict"));
                nfm.setHomedistrictName(rs.getString("dist_name"));
                nfm.setPostingUnintDoj(CommonFunctions.getFormattedOutputDate1(rs.getDate("postingunintdoj")));
                nfm.setDoeGov(CommonFunctions.getFormattedOutputDate1(rs.getDate("doegov")));
                nfm.setJodInspector(CommonFunctions.getFormattedOutputDate1(rs.getDate("jodinspector")));

                nfm.setYearinService(rs.getInt("yearinservicepolicestation") + "");
                nfm.setMonthrinService(rs.getInt("monthrinservicepolicestation") + "");
                nfm.setDaysinService(rs.getInt("daysinservicepolicestation") + "");

                nfm.setPeriodParticularsTraining(rs.getString("periodparticularstraining"));

                nfm.setRewardGSMark(rs.getString("rewardgsmark"));
                nfm.setRewardGSMarkPrior(rs.getString("rewardgsmarkprior"));
                nfm.setRewardGSMarkDuring(rs.getString("rewardgsmarkduring"));
                nfm.setRewardGSMarkFrom(rs.getString("rewardgsmarkfrom"));

                nfm.setRewardMoneyOher(rs.getString("rewardmoneyoher"));
                nfm.setRewardMoneyOherPrior(rs.getString("rewardmoneyoherprior"));
                nfm.setRewardMoneyOherDuring(rs.getString("rewardmoneyoherduring"));
                nfm.setRewardMoneyOherFrom(rs.getString("rewardmoneyoherfrom"));

                nfm.setRewardMedals(rs.getString("rewardmedals"));
                nfm.setRewardMedalsPrior(rs.getString("rewardmedalsprior"));
                nfm.setRewardMedalsDuring(rs.getString("rewardmedalsduring"));
                nfm.setRewardMedalsFrom(rs.getString("rewardmedalsfrom"));

                nfm.setPunishmentMajor(rs.getString("punishmentmajor"));
                nfm.setPunishmentMajorPrior(rs.getString("punishmentmajorprior"));
                nfm.setPunishmentMajorDuring(rs.getString("punishmentmajorduring"));
                nfm.setPunishmentMajorFrom(rs.getString("punishmentmajorfrom"));

                nfm.setPunishmentMinor(rs.getString("punishmentminor"));
                nfm.setPunishmentMinorPrior(rs.getString("punishmentminorprior"));
                nfm.setPunishmentMinorDuring(rs.getString("punishmentminorduring"));
                nfm.setPunishmentMinorFrom(rs.getString("punishmentminorfrom"));

                nfm.setPowerofDecesion(rs.getString("powerofdecesion"));
                nfm.setPhysicalFitness(rs.getString("physicalfitness"));
                nfm.setMentalFitness(rs.getString("mentalfitness"));
                nfm.setHonestyIntegrity(rs.getString("honestyintegrity"));
                nfm.setAdverseIfany(rs.getString("adverseifany"));
                nfm.setDpcifany(rs.getString("dpcifany"));

                nfm.setDateofServing(CommonFunctions.getFormattedOutputDate1(rs.getDate("dateofserving")));
                if (rs.getString("dateofserving") != null && !rs.getString("dateofserving").equals("")) {
                    //nfm.setDateofServingifAny("Y");
                    nfm.setDateofServing(CommonFunctions.getFormattedOutputDate1(rs.getDate("dateofserving")));
                }
                nfm.setRemarksNominatingProfessional(rs.getString("remarksnominatingprofessional"));
                nfm.setRemarksNominationGeneral(rs.getString("remarksnominationgeneral"));

                nfm.setRemarkRecommendation(rs.getString("remarks_recommend_authority"));
                nfm.setRecommendStatus(rs.getString("recommend_status"));
                nfm.setPhysicalFitnessDocumentStatus(rs.getString("physical_fitness_status"));
                nfm.setAccomplishDetail(rs.getString("accomplishment_details"));
                nfm.setSltpostName(rs.getString("current_rank"));

                nfm.setDeclarationAccept(rs.getString("declaration_accept"));
                nfm.setOfficeOrderNo(rs.getString("office_order_no"));
                nfm.setPassingTrainingdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("passing_training_date")));
                nfm.setDiscDetails(rs.getString("disc_details"));
                nfm.setPostingPlace(rs.getString("place_posting"));
                nfm.setSltNominationForPost(rs.getString("nomination_for_post"));
                nfm.setRemarksofCdmo(rs.getString("remarks_of_cdmo"));
                nfm.setViewOfRecommendingAuthority(rs.getString("recommending_authority_view"));
                nfm.setRemarksOfCSB(rs.getString("csb_remarks"));
                nfm.setCadreName(rs.getString("cadre_code"));
                nfm.setServingChargeOfCaseDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("serving_charge_of_casedate")));
                nfm.setGender(rs.getString("gender"));
                nfm.setInitialRank(rs.getString("initial_rank"));
                nfm.setAppointmentMode(rs.getString("appoint_mode"));
                nfm.setIsAnyPromotionearlier(rs.getString("is_any_promotion_earlier"));
                nfm.setDoeinpromotional(CommonFunctions.getFormattedOutputDate1(rs.getDate("date_of_joining_in_promotional_rank")));
                nfm.setDoeInpresentRankDist(CommonFunctions.getFormattedOutputDate1(rs.getDate("date_of_joining_in_present_rank_district")));
                nfm.setPresentRankDistName(rs.getString("present_rank_district_name"));
                nfm.setIsCompletedRegulartenYears(rs.getString("is_completed_regular_tenyears"));
                nfm.setIsContempletedAsOnDate(rs.getString("is_contempleted_as_ondate"));
                nfm.setCurrentDesignation(rs.getString("current_designation"));
                nfm.setPromotionalRankName(rs.getString("promotional_rank_name"));
                nfm.setRecommendStatusDistrict(rs.getString("district_recommendation_status"));
                nfm.setDateofServingifAny(rs.getString("dateofserving_if_any_dp"));
                nfm.setViewOfrecommendStatusDistrict(rs.getString("district_recommendation_view"));
                nfm.setIsPassPrelimilaryExam(rs.getString("is_pass_prelimilaryexam"));
                nfm.setPowerOfDecissionMaking(rs.getString("powerof_decission_making"));
                nfm.setAdverseDetail(rs.getString("adverse_detail"));
                nfm.setPassingPlaceForConstableTraining(rs.getString("passing_place_constable_training"));
                nfm.setPassingYearFromForConstableTraining(CommonFunctions.getFormattedOutputDate1(rs.getDate("passing_date_from_constable_training")));
                nfm.setPassingYearToForConstableTraining(CommonFunctions.getFormattedOutputDate1(rs.getDate("passing_date_to_constable_training")));
                nfm.setOrderNopassingForConstableTraining(rs.getString("orderno_passing__constable_training"));
                nfm.setOrderDatepassingForConstableTraining(CommonFunctions.getFormattedOutputDate1(rs.getDate("orderdate_passing__constable_training")));
                nfm.setPunishmentDetail(rs.getString("punishment_detail"));
                nfm.setRankJoiningovservice(rs.getString("rank_joinin_govservice"));
                nfm.setPresentCriminalStatusDetail(rs.getString("criminal_status_detail"));
                nfm.setDateforpresentCriminalStatus(CommonFunctions.getFormattedOutputDate1(rs.getDate("date_for_present_criminal_status")));
                nfm.setCriminalcasePresentStatusifAny(rs.getString("criminal_case_present_status_ifany"));
                nfm.setCcrollsDetail(rs.getString("ccrollsDetail"));
                nfm.setPropertyStatementSubmittedifAny(rs.getString("property_submitted_if_any"));
                nfm.setDateofPropertySubmittedByHRMS(CommonFunctions.getFormattedOutputDate1(rs.getDate("property_submitted_on_hrms")));
                nfm.setDateofPropertySubmittedByOfficer(CommonFunctions.getFormattedOutputDate1(rs.getDate("property_submitted_on_hrms_byofficer")));
                nfm.setPresentRank(rs.getString("present_rank"));
                nfm.setDateForDISTraining(CommonFunctions.getFormattedOutputDate1(rs.getDate("date_of_distraining")));
                nfm.setOrderNoForDISTraining(rs.getString("orderno_of_distraining"));
                nfm.setTotalpunishment1(rs.getInt("total_punishment1"));
                nfm.setTotalpunishment2(rs.getInt("total_punishment2"));
                nfm.setTotalpunishment3(rs.getInt("total_punishment3"));
                nfm.setServingChargeDetail(rs.getString("servingchargedetail"));
                nfm.setYearinServiceFromPresentRank(rs.getInt("yearinservicepresentrank") + "");
                nfm.setMonthrinServiceFromPresentRank(rs.getInt("monthinservicepresentrank") + "");
                nfm.setDaysinServiceFromPresentRank(rs.getInt("daysinservicepresentrank") + "");

                nfm.setDeptExamPassifany(rs.getString("deptexampassifany"));
                nfm.setGpfNo(rs.getString("gpfno"));
                nfm.setGradeSerialNo(rs.getInt("grade_serial_no") + "");

                nfm.setHavePassedTrainingAsi(rs.getString("have_passed_training_asi"));
                nfm.setBattalionWorkExperienceifany(rs.getString("battalion_work_experienceifany"));
                nfm.setOrderNoForPassedTrainingAsi(rs.getString("orderno_passing_training_asi"));
                nfm.setDateOfPassedTrainingAsi(CommonFunctions.getFormattedOutputDate1(rs.getDate("dateof_passing_training_asi")));
                DataBaseFunctions.closeSqlObjects(rs);
            }
            if (nfm != null) {
                List differentDistrictandEstablishmentList = new ArrayList();
                String sql = "SELECT * FROM police_nomination_form_different_district where nomination_form_id=? order by postingordeputationid";
                ps = con.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(nfm.getNominationFormId()));
                rs = ps.executeQuery();
                while (rs.next()) {
                    NominationDifferentDistrictandEstablishmentList ndde = new NominationDifferentDistrictandEstablishmentList();
                    ndde.setPostingordeputationid(rs.getString("postingordeputationid"));
                    ndde.setDistrictName(rs.getString("posting_deputation_otherdistrict"));
                    ndde.setDistrictFromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("posting_deputation_otherdistrict_fromdate")));
                    ndde.setDistrictToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("posting_deputation_otherdistrict_todate")));

                    differentDistrictandEstablishmentList.add(ndde);
                }

                nfm.setDifferentDistrictandEstablishmentList(differentDistrictandEstablishmentList);

                ps = con.prepareStatement("SELECT * FROM police_nomination_form_different_proceeding where nomination_form_id=? order by proceeding_detail_id");
                ps.setInt(1, Integer.parseInt(nfm.getNominationFormId()));
                rs = ps.executeQuery();
                while (rs.next()) {
                    nfm.setProceedingDetailId(rs.getInt("proceeding_detail_id"));
                    nfm.setProceedingDetail(rs.getString("proceeding_detail"));
                    nfm.setOriginalFileNamefordisciplinaryProceeding(rs.getString("proceeding_org_filename"));
                    nfm.setDiskFileNamefordisciplinaryProceeding(rs.getString("proceeding_disk_filename"));

                }

                /*List differentDisciplinaryProceedingList = new ArrayList();
                 String sql1 = "SELECT * FROM police_nomination_form_different_proceeding where nomination_form_id=? order by proceeding_detail_id";
                 ps = con.prepareStatement(sql1);
                 ps.setInt(1, Integer.parseInt(nfm.getNominationFormId()));
                 rs = ps.executeQuery();
                 while (rs.next()) {
                 NominationDifferentDisciplinaryProceedingList plist = new NominationDifferentDisciplinaryProceedingList();
                 plist.setProceedingdetailid(rs.getInt("proceeding_detail_id"));
                 plist.setProceedingDetail(rs.getString("proceeding_detail"));
                 plist.setOriginalFilename(rs.getString("proceeding_org_filename"));
                 plist.setDiskFileNamedisciplinaryProceeding(rs.getString("proceeding_disk_filename"));
                 differentDisciplinaryProceedingList.add(plist);
                 }

                 nfm.setDifferentDisciplinaryProceedingList(differentDisciplinaryProceedingList); */
                ps = con.prepareStatement("select file_name, original_name, doc_name from police_nomination_doc where data_table_id=?");
                ps.setInt(1, Integer.parseInt(nfm.getNominationFormId()));
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getString("doc_name").equalsIgnoreCase("DPC")) {
                        nfm.setOriginalFileName(rs.getString("original_name"));
                        nfm.setDiskFilename(rs.getString("file_name"));
                    } else if (rs.getString("doc_name").equalsIgnoreCase("CCROLL")) {
                        nfm.setOriginalFileNameCCROll(rs.getString("original_name"));
                        nfm.setDiskFilenameCCROll(rs.getString("file_name"));
                    } else if (rs.getString("doc_name").equalsIgnoreCase("SERV_CHRG")) {
                        nfm.setOriginalFileNameServing(rs.getString("original_name"));
                        nfm.setDiskFilenameServing(rs.getString("file_name"));
                    } else if (rs.getString("doc_name").equalsIgnoreCase("FITNESS")) {
                        nfm.setOriginalFileNameFitnessDocument(rs.getString("original_name"));
                        nfm.setDiskFilenameFitnessDocument(rs.getString("file_name"));
                    } else if (rs.getString("doc_name").equalsIgnoreCase("ACCOMP")) {
                        nfm.setOriginalFileNameAccomplish(rs.getString("original_name"));
                        nfm.setDiskFilenameAccomplish(rs.getString("file_name"));
                    } else if (rs.getString("doc_name").equalsIgnoreCase("SB")) {
                        nfm.setOriginalFileNameSB(rs.getString("original_name"));
                        nfm.setDiskFilenameSB(rs.getString("file_name"));
                    } else if (rs.getString("doc_name").equalsIgnoreCase("FPSB")) {
                        nfm.setOriginalFileNameRelevantSB(rs.getString("original_name"));
                        nfm.setDiskFileNameRelevantSB(rs.getString("file_name"));
                    } else if (rs.getString("doc_name").equalsIgnoreCase("PUNISH")) {
                        nfm.setOriginalFileNamePunishment(rs.getString("original_name"));
                        nfm.setDiskFilenamePunishment(rs.getString("file_name"));
                    } else if (rs.getString("doc_name").equalsIgnoreCase("CASTE")) {
                        nfm.setOriginalFileNamecasteCertificate(rs.getString("original_name"));
                        nfm.setDiskFilenamecasteCertificate(rs.getString("file_name"));
                    } else if (rs.getString("doc_name").equalsIgnoreCase("CONTEMPLET")) {
                        nfm.setOriginalFileNameContempletedDocument(rs.getString("original_name"));
                        nfm.setDiskFilenameContempletedDocument(rs.getString("file_name"));
                    } else if (rs.getString("doc_name").equalsIgnoreCase("MAP")) {
                        nfm.setOriginalFileNameMajorPunishmentForGRD(rs.getString("original_name"));
                        nfm.setDiskFileNameMajorPunishmentForGRD(rs.getString("file_name"));
                    } else if (rs.getString("doc_name").equalsIgnoreCase("MIP")) {
                        nfm.setOriginalFileNameMinorPunishmentForGRD(rs.getString("original_name"));
                        nfm.setDiskFileNameMinorPunishmentForGRD(rs.getString("file_name"));
                    } else if (rs.getString("doc_name").equalsIgnoreCase("WILLINGNESS")) {
                        nfm.setOriginalFileNamewillingnessCertificateForSrclerk(rs.getString("original_name"));
                        nfm.setDiskFileNamewillingnessCertificateForSrclerk(rs.getString("file_name"));
                    } else if (rs.getString("doc_name").equalsIgnoreCase("WILLINGNESS")) {
                        nfm.setOriginalFileNamewillingnessCertificateForSrclerk(rs.getString("original_name"));
                        nfm.setDiskFileNamewillingnessCertificateForSrclerk(rs.getString("file_name"));
                    } else if (rs.getString("doc_name").equalsIgnoreCase("PRELIMEXAM")) {
                        nfm.setOriginalFileNameprelimilaryExamDocument(rs.getString("original_name"));
                        nfm.setDiskFileNameprelimilaryExamDocument(rs.getString("file_name"));
                    } else if (rs.getString("doc_name").equalsIgnoreCase("PRESENT_CRIMINAL_STATUS")) {
                        nfm.setOriginalFileNameforpresentCriminalStatus(rs.getString("original_name"));
                        nfm.setDiskFileNameforpresentCriminalStatus(rs.getString("file_name"));
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return nfm;
    }

    @Override
    public void saveEmployeeNominationData(NominationForm nform, String filePath) {
        Connection con = null;
        PreparedStatement ps = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            con = dataSource.getConnection();
            ps = con.prepareStatement("insert into police_nomination_form "
                    + " (nomination_master_id , nominationdetailid, officecode, officename, category , "
                    + " empid , fullname, fathersname , dob , qualification,  homedistrict, postingunintdoj,  doegov, jodinspector,"
                    + " yearinservicepolicestation, monthrinservicepolicestation , daysinservicepolicestation, periodparticularstraining, rewardgsmark, "
                    + " rewardgsmarkprior, rewardgsmarkduring, rewardgsmarkfrom, rewardmoneyoher, rewardmoneyoherprior, rewardmoneyoherduring, "
                    + " rewardmoneyoherfrom, rewardmedals, rewardmedalsprior, rewardmedalsduring, rewardmedalsfrom, punishmentmajor, punishmentmajorprior, "
                    + " punishmentmajorduring, punishmentmajorfrom, punishmentminor, punishmentminorprior, punishmentminorduring, punishmentminorfrom, "
                    + " powerofdecesion, physicalfitness, physical_fitness_status, mentalfitness, honestyintegrity, adverseifany, dpcifany, dateofserving, "
                    + " remarksnominatingprofessional, remarksnominationgeneral,  accomplishment_details, declaration_accept, office_order_no, "
                    + " passing_training_date, disc_details, place_posting, nomination_for_post, remarks_of_cdmo,recommending_authority_view,csb_remarks, "
                    + " cadre_code,serving_charge_of_casedate,gender,initial_rank,appoint_mode,is_any_promotion_earlier,date_of_joining_in_promotional_rank, "
                    + " date_of_joining_in_present_rank_district,present_rank_district_name,is_completed_regular_tenyears,is_contempleted_as_ondate, "
                    + " current_designation,promotional_rank_name,district_recommendation_status,dateofserving_if_any_dp,district_recommendation_view,is_pass_prelimilaryexam,powerof_decission_making,adverse_detail,"
                    + " passing_place_constable_training,passing_date_from_constable_training,passing_date_to_constable_training,orderno_passing__constable_training,orderdate_passing__constable_training,punishment_detail,rank_joinin_govservice, "
                    + " criminal_status_detail,date_for_present_criminal_status,criminal_case_present_status_ifany,ccrollsdetail,property_submitted_if_any,property_submitted_on_hrms,property_submitted_on_hrms_byofficer,present_rank, "
                    + " date_of_distraining,orderno_of_distraining,total_punishment1,total_punishment2,total_punishment3,servingchargedetail,yearinservicepresentrank, "
                    + " monthinservicepresentrank,daysinservicepresentrank,deptexampassifany,grade_serial_no,gpfno,have_passed_training_asi,battalion_work_experienceifany,dateof_passing_training_asi,orderno_passing_training_asi) "
                    + " values "
                    + " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                    + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                    + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, Integer.parseInt(nform.getNominationMasterId()));
            ps.setInt(2, Integer.parseInt(nform.getNominationDetailId()));
            ps.setString(3, nform.getOfficeCode());
            ps.setString(4, nform.getOfficeName());
            ps.setString(5, nform.getCategory());
            ps.setString(6, nform.getEmpId());
            ps.setString(7, nform.getFullname());
            ps.setString(8, nform.getFathersname());

            if (nform.getDob() != null && !nform.getDob().equals("")) {
                ps.setTimestamp(9, new Timestamp(sdf.parse(nform.getDob()).getTime()));
            } else {
                ps.setTimestamp(9, null);
            }
            ps.setString(10, nform.getQualification());
            ps.setString(11, nform.getHomeDistrict());
            if (nform.getPostingUnintDoj() != null && !nform.getPostingUnintDoj().equals("")) {
                ps.setTimestamp(12, new Timestamp(sdf.parse(nform.getPostingUnintDoj()).getTime()));
            } else {
                ps.setTimestamp(12, null);
            }
            if (nform.getDoeGov() != null && !nform.getDoeGov().equals("")) {
                ps.setTimestamp(13, new Timestamp(sdf.parse(nform.getDoeGov()).getTime()));
            } else {
                ps.setTimestamp(13, null);
            }
            if (nform.getJodInspector() != null && !nform.getJodInspector().equals("")) {
                ps.setTimestamp(14, new Timestamp(sdf.parse(nform.getJodInspector()).getTime()));
            } else {
                ps.setTimestamp(14, null);
            }

            //String dateofAbsorption = rs.getString("final_appointment_date");
            //Converting String to Date
            if (nform.getYearinService() == null || nform.getYearinService().equals("")) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                Date date = formatter.parse(nform.getDoeGov());
                //Converting obtained Date object to LocalDate object
                Instant instant = date.toInstant();
                ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
                LocalDate givenDate = zone.toLocalDate();

                Date date2 = formatter.parse("31-DEC-2023");
                //Converting obtained Date object to LocalDate object
                Instant instant2 = date2.toInstant();
                ZonedDateTime zone2 = instant2.atZone(ZoneId.systemDefault());
                LocalDate nowDate = zone2.toLocalDate();

                //Calculating the difference between given date to current date.
                Period period = Period.between(givenDate, nowDate);
                
                nform.setYearinService(period.getYears() + "");
                nform.setMonthrinService(period.getMonths() + "");
                nform.setDaysinService(period.getDays() + "");
            }

            if (nform.getYearinServiceFromPresentRank() == null || nform.getYearinServiceFromPresentRank().equals("")) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                Date date = formatter.parse(nform.getJodInspector());
                //Converting obtained Date object to LocalDate object
                Instant instant = date.toInstant();
                ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
                LocalDate givenDate = zone.toLocalDate();

                Date date2 = formatter.parse("31-DEC-2023");
                //Converting obtained Date object to LocalDate object
                Instant instant2 = date2.toInstant();
                ZonedDateTime zone2 = instant2.atZone(ZoneId.systemDefault());
                LocalDate nowDate = zone2.toLocalDate();

                //Calculating the difference between given date to current date.
                Period period = Period.between(givenDate, nowDate);
                
                nform.setYearinServiceFromPresentRank(period.getYears() + "");
                nform.setMonthrinServiceFromPresentRank(period.getMonths() + "");
                nform.setDaysinServiceFromPresentRank(period.getDays() + "");
                
            }

            ps.setInt(15, Integer.parseInt(nform.getYearinService()));
            ps.setInt(16, Integer.parseInt(nform.getMonthrinService()));
            ps.setInt(17, Integer.parseInt(nform.getDaysinService()));

            ps.setString(18, nform.getPeriodParticularsTraining());

            ps.setString(19, nform.getRewardGSMark());
            ps.setString(20, nform.getRewardGSMarkPrior());
            ps.setString(21, nform.getRewardGSMarkDuring());
            ps.setString(22, nform.getRewardGSMarkFrom());

            ps.setString(23, nform.getRewardMoneyOher());
            ps.setString(24, nform.getRewardMoneyOherPrior());
            ps.setString(25, nform.getRewardMoneyOherDuring());
            ps.setString(26, nform.getRewardMoneyOherFrom());

            ps.setString(27, nform.getRewardMedals());
            ps.setString(28, nform.getRewardMedalsPrior());
            ps.setString(29, nform.getRewardMedalsDuring());
            ps.setString(30, nform.getRewardMedalsFrom());

            ps.setString(31, nform.getPunishmentMajor());
            ps.setString(32, nform.getPunishmentMajorPrior());
            ps.setString(33, nform.getPunishmentMajorDuring());
            ps.setString(34, nform.getPunishmentMajorFrom());

            ps.setString(35, nform.getPunishmentMinor());
            ps.setString(36, nform.getPunishmentMinorPrior());
            ps.setString(37, nform.getPunishmentMinorDuring());
            ps.setString(38, nform.getPunishmentMinorFrom());

            ps.setString(39, nform.getPowerofDecesion());
            ps.setString(40, nform.getPhysicalFitness());
            ps.setString(41, nform.getPhysicalFitnessDocumentStatus());

            ps.setString(42, nform.getMentalFitness());
            ps.setString(43, nform.getHonestyIntegrity());
            ps.setString(44, nform.getAdverseIfany());
            ps.setString(45, nform.getDpcifany());
            if (nform.getDateofServing() != null && !nform.getDateofServing().equals("")) {
                ps.setTimestamp(46, new Timestamp(sdf.parse(nform.getDateofServing()).getTime()));
            } else {
                ps.setTimestamp(46, null);
            }
            ps.setString(47, nform.getRemarksNominatingProfessional());
            ps.setString(48, nform.getRemarksNominationGeneral());
            ps.setString(49, nform.getAccomplishDetail());
            ps.setString(50, nform.getDeclarationAccept());
            ps.setString(51, nform.getOfficeOrderNo());

            if (nform.getPassingTrainingdate() != null && !nform.getPassingTrainingdate().equals("")) {
                ps.setTimestamp(52, new Timestamp(sdf.parse(nform.getPassingTrainingdate()).getTime()));
            } else {
                ps.setTimestamp(52, null);
            }

            ps.setString(53, nform.getDiscDetails());
            ps.setString(54, nform.getPostingPlace());
            ps.setString(55, nform.getSltNominationForPost());
            ps.setString(56, nform.getRemarksofCdmo());
            ps.setString(57, nform.getViewOfRecommendingAuthority());
            ps.setString(58, nform.getRemarksOfCSB());
            ps.setString(59, nform.getCadreName());
            if (nform.getServingChargeOfCaseDate() != null && !nform.getServingChargeOfCaseDate().equals("")) {
                ps.setTimestamp(60, new Timestamp(sdf.parse(nform.getServingChargeOfCaseDate()).getTime()));
            } else {
                ps.setTimestamp(60, null);
            }
            ps.setString(61, nform.getGender());
            ps.setString(62, nform.getInitialRank());
            ps.setString(63, nform.getAppointmentMode());
            ps.setString(64, nform.getIsAnyPromotionearlier());
            if (nform.getDoeinpromotional() != null && !nform.getDoeinpromotional().equals("")) {
                ps.setTimestamp(65, new Timestamp(sdf.parse(nform.getDoeinpromotional()).getTime()));
            } else {
                ps.setTimestamp(65, null);
            }
            if (nform.getDoeInpresentRankDist() != null && !nform.getDoeInpresentRankDist().equals("")) {
                ps.setTimestamp(66, new Timestamp(sdf.parse(nform.getDoeInpresentRankDist()).getTime()));
            } else {
                ps.setTimestamp(66, null);
            }
            ps.setString(67, nform.getPresentRankDistName());
            ps.setString(68, nform.getIsCompletedRegulartenYears());
            ps.setString(69, nform.getIsContempletedAsOnDate());
            ps.setString(70, nform.getCurrentDesignation());
            ps.setString(71, nform.getPromotionalRankName());
            ps.setString(72, nform.getRecommendStatusDistrict());
            ps.setString(73, nform.getDateofServingifAny());
            ps.setString(74, nform.getViewOfrecommendStatusDistrict());
            ps.setString(75, nform.getIsPassPrelimilaryExam());
            ps.setString(76, nform.getPowerOfDecissionMaking());
            ps.setString(77, nform.getAdverseDetail());
            ps.setString(78, nform.getPassingPlaceForConstableTraining());
            if (nform.getPassingYearFromForConstableTraining() != null && !nform.getPassingYearFromForConstableTraining().equals("")) {
                ps.setTimestamp(79, new Timestamp(sdf.parse(nform.getPassingYearFromForConstableTraining()).getTime()));
            } else {
                ps.setTimestamp(79, null);
            }
            if (nform.getPassingYearToForConstableTraining() != null && !nform.getPassingYearToForConstableTraining().equals("")) {
                ps.setTimestamp(80, new Timestamp(sdf.parse(nform.getPassingYearToForConstableTraining()).getTime()));
            } else {
                ps.setTimestamp(80, null);
            }
            ps.setString(81, nform.getOrderNopassingForConstableTraining());
            if (nform.getOrderDatepassingForConstableTraining() != null && !nform.getOrderDatepassingForConstableTraining().equals("")) {
                ps.setTimestamp(82, new Timestamp(sdf.parse(nform.getOrderDatepassingForConstableTraining()).getTime()));
            } else {
                ps.setTimestamp(82, null);
            }
            ps.setString(83, nform.getPunishmentDetail());
            ps.setString(84, nform.getRankJoiningovservice());
            ps.setString(85, nform.getPresentCriminalStatusDetail());
            if (nform.getDateforpresentCriminalStatus() != null && !nform.getDateforpresentCriminalStatus().equals("")) {
                ps.setTimestamp(86, new Timestamp(sdf.parse(nform.getDateforpresentCriminalStatus()).getTime()));
            } else {
                ps.setTimestamp(86, null);
            }
            ps.setString(87, nform.getCriminalcasePresentStatusifAny());
            ps.setString(88, nform.getCcrollsDetail());
            ps.setString(89, nform.getPropertyStatementSubmittedifAny());
            if (nform.getDateofPropertySubmittedByHRMS() != null && !nform.getDateofPropertySubmittedByHRMS().equals("")) {
                ps.setTimestamp(90, new Timestamp(sdf.parse(nform.getDateofPropertySubmittedByHRMS()).getTime()));
            } else {
                ps.setTimestamp(90, null);
            }
            if (nform.getDateofPropertySubmittedByOfficer() != null && !nform.getDateofPropertySubmittedByOfficer().equals("")) {
                ps.setTimestamp(91, new Timestamp(sdf.parse(nform.getDateofPropertySubmittedByOfficer()).getTime()));
            } else {
                ps.setTimestamp(91, null);
            }
            ps.setString(92, nform.getPresentRank());
            if (nform.getDateForDISTraining() != null && !nform.getDateForDISTraining().equals("")) {
                ps.setTimestamp(93, new Timestamp(sdf.parse(nform.getDateForDISTraining()).getTime()));
            } else {
                ps.setTimestamp(93, null);
            }
            ps.setString(94, nform.getOrderNoForDISTraining());
            ps.setInt(95, nform.getTotalpunishment1());
            ps.setInt(96, nform.getTotalpunishment2());
            ps.setInt(97, nform.getTotalpunishment3());
            ps.setString(98, nform.getServingChargeDetail());
            ps.setInt(99, Integer.parseInt(nform.getYearinServiceFromPresentRank()));
            ps.setInt(100, Integer.parseInt(nform.getMonthrinServiceFromPresentRank()));
            ps.setInt(101, Integer.parseInt(nform.getDaysinServiceFromPresentRank()));
            ps.setString(102, nform.getDeptExamPassifany());
            ps.setString(103, nform.getGradeSerialNo());
            ps.setString(104, nform.getGpfNo());
            ps.setString(105, nform.getHavePassedTrainingAsi());
            ps.setString(106, nform.getBattalionWorkExperienceifany());
            if (nform.getDateOfPassedTrainingAsi() != null && !nform.getDateOfPassedTrainingAsi().equals("")) {
                ps.setTimestamp(107, new Timestamp(sdf.parse(nform.getDateOfPassedTrainingAsi()).getTime()));
            } else {
                ps.setTimestamp(107, null);
            }
            ps.setString(108, nform.getOrderNoForPassedTrainingAsi());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int nomination_form_id = rs.getInt("nomination_form_id");

            if (nform.getServiceBookCopy() != null && !nform.getServiceBookCopy().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getServiceBookCopy().getInputStream();
                String diskfilename = "SB" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getServiceBookCopy().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getServiceBookCopy().getOriginalFilename());
                ps.setInt(5, nomination_form_id);
                ps.setString(6, "SB");
                ps.executeUpdate();

            }
            if (nform.getRelevantServiceBookCopy() != null && !nform.getRelevantServiceBookCopy().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getRelevantServiceBookCopy().getInputStream();
                String diskfilename = "FPSB" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getRelevantServiceBookCopy().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getRelevantServiceBookCopy().getOriginalFilename());
                ps.setInt(5, nomination_form_id);
                ps.setString(6, "FPSB");
                ps.executeUpdate();

            }

            if (nform.getPunishmentCopy() != null && !nform.getPunishmentCopy().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getPunishmentCopy().getInputStream();
                String diskfilename = "PUNISH" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getPunishmentCopy().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getPunishmentCopy().getOriginalFilename());
                ps.setInt(5, nomination_form_id);
                ps.setString(6, "PUNISH");
                ps.executeUpdate();

            }
            if (nform.getMajorPunishmentForGRDDocument() != null && !nform.getMajorPunishmentForGRDDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getMajorPunishmentForGRDDocument().getInputStream();
                String diskfilename = "MAP" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getMajorPunishmentForGRDDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getMajorPunishmentForGRDDocument().getOriginalFilename());
                ps.setInt(5, nomination_form_id);
                ps.setString(6, "MAP");
                ps.executeUpdate();

            }
            if (nform.getMinorPunishmentForGRDDocument() != null && !nform.getMinorPunishmentForGRDDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getMinorPunishmentForGRDDocument().getInputStream();
                String diskfilename = "MIP" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getMinorPunishmentForGRDDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getMinorPunishmentForGRDDocument().getOriginalFilename());
                ps.setInt(5, nomination_form_id);
                ps.setString(6, "MIP");
                ps.executeUpdate();

            }

            if (nform.getDiscDocument() != null && !nform.getDiscDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getDiscDocument().getInputStream();
                String diskfilename = "DPC" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getDiscDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getDiscDocument().getOriginalFilename());
                ps.setInt(5, nomination_form_id);
                ps.setString(6, "DPC");
                ps.executeUpdate();

            }

            if (nform.getDiscCCROll() != null && !nform.getDiscCCROll().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getDiscCCROll().getInputStream();
                String diskfilename = "CCROLL" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getDiscCCROll().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getDiscCCROll().getOriginalFilename());
                ps.setInt(5, nomination_form_id);
                ps.setString(6, "CCROLL");
                ps.executeUpdate();

            }

            if (nform.getDocumentServingCopy() != null && !nform.getDocumentServingCopy().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getDocumentServingCopy().getInputStream();
                String diskfilename = "SERV_CHRG" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getDocumentServingCopy().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getDocumentServingCopy().getOriginalFilename());
                ps.setInt(5, nomination_form_id);
                ps.setString(6, "SERV_CHRG");
                ps.executeUpdate();

            }

            if (nform.getFitnessDocument() != null && !nform.getFitnessDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getFitnessDocument().getInputStream();
                String diskfilename = "FITNESS" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getFitnessDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getFitnessDocument().getOriginalFilename());
                ps.setInt(5, nomination_form_id);
                ps.setString(6, "FITNESS");
                ps.executeUpdate();

            }

            if (nform.getAccomplishCopy() != null && !nform.getAccomplishCopy().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getAccomplishCopy().getInputStream();
                String diskfilename = "ACCOMP" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getAccomplishCopy().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getAccomplishCopy().getOriginalFilename());
                ps.setInt(5, nomination_form_id);
                ps.setString(6, "ACCOMP");
                ps.executeUpdate();

            }

            if (nform.getCasteCertificate() != null && !nform.getCasteCertificate().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getCasteCertificate().getInputStream();
                String diskfilename = "CASTE" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getCasteCertificate().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getCasteCertificate().getOriginalFilename());
                ps.setInt(5, nomination_form_id);
                ps.setString(6, "CASTE");
                ps.executeUpdate();

            }
            if (nform.getContempletedDocument() != null && !nform.getContempletedDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getContempletedDocument().getInputStream();
                String diskfilename = "CONTEMPLET" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getContempletedDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getContempletedDocument().getOriginalFilename());
                ps.setInt(5, nomination_form_id);
                ps.setString(6, "CONTEMPLET");
                ps.executeUpdate();

            }
            if (nform.getWillingnessCertificateDocument() != null && !nform.getWillingnessCertificateDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getWillingnessCertificateDocument().getInputStream();
                String diskfilename = "WILLINGNESS" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getWillingnessCertificateDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getWillingnessCertificateDocument().getOriginalFilename());
                ps.setInt(5, nomination_form_id);
                ps.setString(6, "WILLINGNESS");
                ps.executeUpdate();

            }
            if (nform.getPrelimilaryExamDocument() != null && !nform.getPrelimilaryExamDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getPrelimilaryExamDocument().getInputStream();
                String diskfilename = "PRELIMEXAM" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getPrelimilaryExamDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getPrelimilaryExamDocument().getOriginalFilename());
                ps.setInt(5, nomination_form_id);
                ps.setString(6, "PRELIMEXAM");
                ps.executeUpdate();

            }

            if (nform.getPresentCriminalStatusDetailfile() != null && !nform.getPresentCriminalStatusDetailfile().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getPresentCriminalStatusDetailfile().getInputStream();
                String diskfilename = "PRESENT_CRIMINAL_STATUS" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getPresentCriminalStatusDetailfile().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getPresentCriminalStatusDetailfile().getOriginalFilename());
                ps.setInt(5, nomination_form_id);
                ps.setString(6, "PRESENT_CRIMINAL_STATUS");
                ps.executeUpdate();

            }

            if (nform.getDisciplinaryproceedingfile() != null && !nform.getDisciplinaryproceedingfile().isEmpty()) {
                String diskfileName = null;
                PreparedStatement pst = null;
                String originalFileName = null;
                String contentType = null;

                diskfileName = new Date().getTime() + "";
                originalFileName = nform.getDisciplinaryproceedingfile().getOriginalFilename();
                contentType = nform.getDisciplinaryproceedingfile().getContentType();
                byte[] bytes = nform.getDisciplinaryproceedingfile().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + diskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

                pst = con.prepareStatement("insert into police_nomination_form_different_proceeding(nomination_form_id,proceeding_detail,proceeding_org_filename,proceeding_disk_filename,proceeding_filetype,file_path) values(?,?,?,?,?,?)");
                pst.setInt(1, nomination_form_id);
                pst.setString(2, nform.getProceedingDetail());
                pst.setString(3, originalFileName);
                pst.setString(4, diskfileName);
                pst.setString(5, contentType);
                pst.setString(6, this.uploadPath);
                pst.executeUpdate();

            }

            ps = con.prepareStatement("update police_nomination_detail set form_completed_status=? where nomination_detail_id=?");
            ps.setString(1, "Y");
            ps.setInt(2, Integer.parseInt(nform.getNominationDetailId()));
            ps.executeUpdate();

            if ((nform.getPostingOrDeputationInOtherDistrict() != null && !nform.getPostingOrDeputationInOtherDistrict().equals(""))
                    || (nform.getPostingOrDeputationInOtherDistrictFromDate() != null && !nform.getPostingOrDeputationInOtherDistrictFromDate().equals("")) || (nform.getPostingOrDeputationInOtherDistrictToDate() != null && !nform.getPostingOrDeputationInOtherDistrictToDate().equals(""))) {
                String[] postingOrDeputationInOtherDistrictArr = nform.getPostingOrDeputationInOtherDistrict().split(",");
                String[] postingOrDeputationInOtherDistrictFromDateArr = nform.getPostingOrDeputationInOtherDistrictFromDate().split(",");
                String[] postingOrDeputationInOtherDistrictToDateArr = nform.getPostingOrDeputationInOtherDistrictToDate().split(",");

                String sql = "insert into police_nomination_form_different_district(nomination_form_id,posting_deputation_otherdistrict,posting_deputation_otherdistrict_fromdate,posting_deputation_otherdistrict_todate) values(?,?,?,?)";
                ps = con.prepareStatement(sql);

                for (int i = 0; i < postingOrDeputationInOtherDistrictArr.length; i++) {
                    ps.setInt(1, nomination_form_id);
                    if (postingOrDeputationInOtherDistrictArr[i] != null && !postingOrDeputationInOtherDistrictArr[i].equals("")) {
                        ps.setString(2, postingOrDeputationInOtherDistrictArr[i]);
                    } else {
                        ps.setTimestamp(2, null);
                    }
                    if (postingOrDeputationInOtherDistrictFromDateArr[i] != null && !postingOrDeputationInOtherDistrictFromDateArr[i].equals("")) {
                        ps.setTimestamp(3, new Timestamp(sdf.parse(postingOrDeputationInOtherDistrictFromDateArr[i]).getTime()));
                    } else {
                        ps.setTimestamp(3, null);
                    }
                    if (postingOrDeputationInOtherDistrictToDateArr[i] != null && !postingOrDeputationInOtherDistrictToDateArr[i].equals("")) {
                        ps.setTimestamp(4, new Timestamp(sdf.parse(postingOrDeputationInOtherDistrictToDateArr[i]).getTime()));
                    } else {
                        ps.setInt(4, 0);
                    }

                    ps.execute();
                }
            }

            /* if ((nform.getDisciplinaryproceedingDetail() != null && !nform.getDisciplinaryproceedingDetail().equals(""))
             || (nform.getDisciplinaryproceedingfile() != null && !nform.getDisciplinaryproceedingfile().equals(""))) {
             String[] disciplinaryProceedingDetailArr = nform.getDisciplinaryproceedingDetail();
             MultipartFile[] disciplinaryProceedingorgfileArr = nform.getDisciplinaryproceedingfile();
             String sql = "insert into police_nomination_form_different_proceeding(nomination_form_id,proceeding_detail,proceeding_org_filename,proceeding_disk_filename,proceeding_filetype,file_path) values(?,?,?,?,?,?)";
             ps = con.prepareStatement(sql);
             for (int k = 0; k < disciplinaryProceedingorgfileArr.length; k++) {

             String disciplinaryProceedingdiskfileArr = new Date().getTime() + "";
             String disciplinaryProceedingorgfile = disciplinaryProceedingorgfileArr[k].getOriginalFilename();
             String contentType = disciplinaryProceedingorgfileArr[k].getContentType();
             byte[] bytes = disciplinaryProceedingorgfileArr[k].getBytes();

             //String[] postingOrDeputationInOtherDistrictToDateArr = nform.getPostingOrDeputationInOtherDistrictToDate().split(",");
             File dir = new File(this.uploadPath);
             if (!dir.exists()) {
             dir.mkdirs();
             }
             File serverFile = new File(dir.getAbsolutePath() + File.separator + disciplinaryProceedingdiskfileArr);
             FileOutputStream fout = new FileOutputStream(serverFile);
             fout.write(bytes);
             fout.close();

             //String sql = "update police_nomination_form_different_district set posting_deputation_otherdistrict=?,posting_deputation_otherdistrict_fromdate=?,posting_deputation_otherdistrict_todate=? where nomination_form_id=?";
             //for (int i = 0; i < disciplinaryProceedingDetailArr.length; i++) {
             ps.setInt(1, nomination_form_id);
             if (disciplinaryProceedingDetailArr[k] != null && !disciplinaryProceedingDetailArr[k].equals("")) {
             ps.setString(2, disciplinaryProceedingDetailArr[k]);
             } else {
             ps.setString(2, null);
             }
             if (disciplinaryProceedingorgfileArr != null && !disciplinaryProceedingorgfileArr.equals("")) {
             ps.setString(3, disciplinaryProceedingorgfile);
             } else {
             ps.setString(3, null);
             }
             if (disciplinaryProceedingdiskfileArr != null && !disciplinaryProceedingdiskfileArr.equals("")) {
             ps.setString(4, disciplinaryProceedingdiskfileArr);
             } else {
             ps.setString(4, null);
             }
             if (contentType != null && !contentType.equals("")) {
             ps.setString(5, contentType);
             } else {
             ps.setString(5, null);
             }
             ps.setString(6, this.uploadPath);
             ps.execute();
                    
             }
             } */
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateEmployeeNominationData(NominationForm nform, String filePath) {
        Connection con = null;
        PreparedStatement ps = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            con = dataSource.getConnection();
            ps = con.prepareStatement("update police_nomination_form set "
                    + " nomination_master_id=? , nominationdetailid=?, officecode=?, officename=?, category=? , "
                    + " empid=? , fullname=?, fathersname=? , dob=? , qualification=?,  homedistrict=?, postingunintdoj=?,  doegov=?, jodinspector=?,"
                    + " yearinservicepolicestation=?, monthrinservicepolicestation=? , daysinservicepolicestation=?, periodparticularstraining=?, rewardgsmark=?, "
                    + " rewardgsmarkprior=?, rewardgsmarkduring=?, rewardgsmarkfrom=?, rewardmoneyoher=?, rewardmoneyoherprior=?, rewardmoneyoherduring=?, "
                    + " rewardmoneyoherfrom=?, rewardmedals=?, rewardmedalsprior=?, rewardmedalsduring=?, rewardmedalsfrom=?, punishmentmajor=?, punishmentmajorprior=?, "
                    + " punishmentmajorduring=?, punishmentmajorfrom=?, punishmentminor=?, punishmentminorprior=?, punishmentminorduring=?, punishmentminorfrom=?, "
                    + " powerofdecesion=?, physicalfitness=?, mentalfitness=?, honestyintegrity=?, adverseifany=?, dpcifany=?, dateofserving=?, "
                    + " remarksnominatingprofessional=?, remarksnominationgeneral=?, physical_fitness_status=?, accomplishment_details=?, "
                    + " declaration_accept=?, office_order_no=?, passing_training_date=?, disc_details=?, place_posting=?, nomination_for_post=?, remarks_of_cdmo=?,recommending_authority_view=?,csb_remarks=?,cadre_code=?,serving_charge_of_casedate=?, "
                    + " gender=?,initial_rank=?,appoint_mode=?, "
                    + " is_any_promotion_earlier=?,date_of_joining_in_promotional_rank=?,date_of_joining_in_present_rank_district=?,present_rank_district_name=?,is_completed_regular_tenyears=?,is_contempleted_as_ondate=?,current_designation=?,promotional_rank_name=?, "
                    + " district_recommendation_status=?,dateofserving_if_any_dp=?,district_recommendation_view=?,is_pass_prelimilaryexam=?,powerof_decission_making=?,adverse_detail=?, "
                    + " passing_place_constable_training=?,passing_date_from_constable_training=?,passing_date_to_constable_training=?,orderno_passing__constable_training=?,orderdate_passing__constable_training=?,punishment_detail=?,rank_joinin_govservice=?, "
                    + " criminal_status_detail=?,date_for_present_criminal_status=?,criminal_case_present_status_ifany=?,ccrollsdetail=?,property_submitted_if_any=?,property_submitted_on_hrms=?,property_submitted_on_hrms_byofficer=?, "
                    + " present_rank=?,date_of_distraining=?,orderno_of_distraining=?,total_punishment1=?,total_punishment2=?,total_punishment3=?,servingchargedetail=?,yearinservicepresentrank=?, "
                    + " monthinservicepresentrank=?,daysinservicepresentrank=?,deptexampassifany=?,grade_serial_no=?,gpfno=?,have_passed_training_asi=?,battalion_work_experienceifany=?,orderno_passing_training_asi=?,dateof_passing_training_asi=? where nomination_form_id=? ");

            ps.setInt(1, Integer.parseInt(nform.getNominationMasterId()));
            ps.setInt(2, Integer.parseInt(nform.getNominationDetailId()));
            ps.setString(3, nform.getOfficeCode());
            ps.setString(4, nform.getOfficeName());
            ps.setString(5, nform.getCategory());
            ps.setString(6, nform.getEmpId());
            ps.setString(7, nform.getFullname());
            ps.setString(8, nform.getFathersname());

            if (nform.getDob() != null && !nform.getDob().equals("")) {
                ps.setTimestamp(9, new Timestamp(sdf.parse(nform.getDob()).getTime()));
            } else {
                ps.setTimestamp(9, null);
            }
            ps.setString(10, nform.getQualification());
            ps.setString(11, nform.getHomeDistrict());
            if (nform.getPostingUnintDoj() != null && !nform.getPostingUnintDoj().equals("")) {
                ps.setTimestamp(12, new Timestamp(sdf.parse(nform.getPostingUnintDoj()).getTime()));
            } else {
                ps.setTimestamp(12, null);
            }
            if (nform.getDoeGov() != null && !nform.getDoeGov().equals("")) {
                ps.setTimestamp(13, new Timestamp(sdf.parse(nform.getDoeGov()).getTime()));
            } else {
                ps.setTimestamp(13, null);
            }
            if (nform.getJodInspector() != null && !nform.getJodInspector().equals("")) {
                ps.setTimestamp(14, new Timestamp(sdf.parse(nform.getJodInspector()).getTime()));
            } else {
                ps.setTimestamp(14, null);
            }

            if (nform.getDoeGov() != null && !nform.getDoeGov().equals("")) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                Date date = formatter.parse(nform.getDoeGov());
                //Converting obtained Date object to LocalDate object
                Instant instant = date.toInstant();
                ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
                LocalDate givenDate = zone.toLocalDate();

                Date date2 = formatter.parse("31-DEC-2023");
                //Converting obtained Date object to LocalDate object
                Instant instant2 = date2.toInstant();
                ZonedDateTime zone2 = instant2.atZone(ZoneId.systemDefault());
                LocalDate nowDate = zone2.toLocalDate();

                //Calculating the difference between given date to current date.
                Period period = Period.between(givenDate, nowDate);

                nform.setYearinService(period.getYears() + "");
                nform.setMonthrinService(period.getMonths() + "");
                nform.setDaysinService(period.getDays() + "");
            }

            if (nform.getJodInspector() != null && !nform.getJodInspector().equals("")) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                Date date = formatter.parse(nform.getJodInspector());
                //Converting obtained Date object to LocalDate object
                Instant instant = date.toInstant();
                ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
                LocalDate givenDate = zone.toLocalDate();

                Date date2 = formatter.parse("31-DEC-2023");
                //Converting obtained Date object to LocalDate object
                Instant instant2 = date2.toInstant();
                ZonedDateTime zone2 = instant2.atZone(ZoneId.systemDefault());
                LocalDate nowDate = zone2.toLocalDate();

                //Calculating the difference between given date to current date.
                Period period = Period.between(givenDate, nowDate);

                nform.setYearinServiceFromPresentRank(period.getYears() + "");
                nform.setMonthrinServiceFromPresentRank(period.getMonths() + "");
                nform.setDaysinServiceFromPresentRank(period.getDays() + "");
            }

            ps.setInt(15, Integer.parseInt(nform.getYearinService()));
            ps.setInt(16, Integer.parseInt(nform.getMonthrinService()));
            ps.setInt(17, Integer.parseInt(nform.getDaysinService()));

            ps.setString(18, nform.getPeriodParticularsTraining());
            ps.setString(19, nform.getRewardGSMark());
            ps.setString(20, nform.getRewardGSMarkPrior());
            ps.setString(21, nform.getRewardGSMarkDuring());
            ps.setString(22, nform.getRewardGSMarkFrom());
            ps.setString(23, nform.getRewardMoneyOher());
            ps.setString(24, nform.getRewardMoneyOherPrior());
            ps.setString(25, nform.getRewardMoneyOherDuring());
            ps.setString(26, nform.getRewardMoneyOherFrom());
            ps.setString(27, nform.getRewardMedals());
            ps.setString(28, nform.getRewardMedalsPrior());
            ps.setString(29, nform.getRewardMedalsDuring());
            ps.setString(30, nform.getRewardMedalsFrom());
            ps.setString(31, nform.getPunishmentMajor());
            ps.setString(32, nform.getPunishmentMajorPrior());
            ps.setString(33, nform.getPunishmentMajorDuring());
            ps.setString(34, nform.getPunishmentMajorFrom());
            ps.setString(35, nform.getPunishmentMinor());
            ps.setString(36, nform.getPunishmentMinorPrior());

            ps.setString(37, nform.getPunishmentMinorDuring());
            ps.setString(38, nform.getPunishmentMinorFrom());
            ps.setString(39, nform.getPowerofDecesion());
            ps.setString(40, nform.getPhysicalFitness());
            ps.setString(41, nform.getMentalFitness());
            ps.setString(42, nform.getHonestyIntegrity());
            ps.setString(43, nform.getAdverseIfany());
            ps.setString(44, nform.getDpcifany());
            if (nform.getDateofServing() != null && !nform.getDateofServing().equals("")) {
                ps.setTimestamp(45, new Timestamp(sdf.parse(nform.getDateofServing()).getTime()));
            } else {
                ps.setTimestamp(45, null);
            }
            ps.setString(46, nform.getRemarksNominatingProfessional());
            ps.setString(47, nform.getRemarksNominationGeneral());
            ps.setString(48, nform.getPhysicalFitnessDocumentStatus());
            ps.setString(49, nform.getAccomplishDetail());
            ps.setString(50, nform.getDeclarationAccept());

            ps.setString(51, nform.getOfficeOrderNo());

            if (nform.getPassingTrainingdate() != null && !nform.getPassingTrainingdate().equals("")) {
                ps.setTimestamp(52, new Timestamp(sdf.parse(nform.getPassingTrainingdate()).getTime()));
            } else {
                ps.setTimestamp(52, null);
            }

            ps.setString(53, nform.getDiscDetails());
            ps.setString(54, nform.getPostingPlace());
            ps.setString(55, nform.getSltNominationForPost());
            ps.setString(56, nform.getRemarksofCdmo());
            ps.setString(57, nform.getViewOfRecommendingAuthority());
            ps.setString(58, nform.getRemarksOfCSB());
            ps.setString(59, nform.getCadreName());
            if (nform.getServingChargeOfCaseDate() != null && !nform.getServingChargeOfCaseDate().equals("")) {
                ps.setTimestamp(60, new Timestamp(sdf.parse(nform.getServingChargeOfCaseDate()).getTime()));
            } else {
                ps.setTimestamp(60, null);
            }
            ps.setString(61, nform.getGender());
            ps.setString(62, nform.getInitialRank());
            ps.setString(63, nform.getAppointmentMode());
            ps.setString(64, nform.getIsAnyPromotionearlier());
            if (nform.getDoeinpromotional() != null && !nform.getDoeinpromotional().equals("")) {
                ps.setTimestamp(65, new Timestamp(sdf.parse(nform.getDoeinpromotional()).getTime()));
            } else {
                ps.setTimestamp(65, null);
            }
            if (nform.getDoeInpresentRankDist() != null && !nform.getDoeInpresentRankDist().equals("")) {
                ps.setTimestamp(66, new Timestamp(sdf.parse(nform.getDoeInpresentRankDist()).getTime()));
            } else {
                ps.setTimestamp(66, null);
            }
            ps.setString(67, nform.getPresentRankDistName());
            ps.setString(68, nform.getIsCompletedRegulartenYears());
            ps.setString(69, nform.getIsContempletedAsOnDate());
            ps.setString(70, nform.getCurrentDesignation());
            ps.setString(71, nform.getPromotionalRankName());
            ps.setString(72, nform.getRecommendStatusDistrict());
            ps.setString(73, nform.getDateofServingifAny());
            ps.setString(74, nform.getViewOfrecommendStatusDistrict());
            ps.setString(75, nform.getIsPassPrelimilaryExam());
            ps.setString(76, nform.getPowerOfDecissionMaking());
            ps.setString(77, nform.getAdverseDetail());
            ps.setString(78, nform.getPassingPlaceForConstableTraining());
            if (nform.getPassingYearFromForConstableTraining() != null && !nform.getPassingYearFromForConstableTraining().equals("")) {
                ps.setTimestamp(79, new Timestamp(sdf.parse(nform.getPassingYearFromForConstableTraining()).getTime()));
            } else {
                ps.setTimestamp(79, null);
            }
            if (nform.getPassingYearToForConstableTraining() != null && !nform.getPassingYearToForConstableTraining().equals("")) {
                ps.setTimestamp(80, new Timestamp(sdf.parse(nform.getPassingYearToForConstableTraining()).getTime()));
            } else {
                ps.setTimestamp(80, null);
            }
            ps.setString(81, nform.getOrderNopassingForConstableTraining());
            if (nform.getOrderDatepassingForConstableTraining() != null && !nform.getOrderDatepassingForConstableTraining().equals("")) {
                ps.setTimestamp(82, new Timestamp(sdf.parse(nform.getOrderDatepassingForConstableTraining()).getTime()));
            } else {
                ps.setTimestamp(82, null);
            }
            ps.setString(83, nform.getPunishmentDetail());
            ps.setString(84, nform.getRankJoiningovservice());
            ps.setString(85, nform.getPresentCriminalStatusDetail());
            if (nform.getDateforpresentCriminalStatus() != null && !nform.getDateforpresentCriminalStatus().equals("")) {
                ps.setTimestamp(86, new Timestamp(sdf.parse(nform.getDateforpresentCriminalStatus()).getTime()));
            } else {
                ps.setTimestamp(86, null);
            }
            ps.setString(87, nform.getCriminalcasePresentStatusifAny());
            ps.setString(88, nform.getCcrollsDetail());
            ps.setString(89, nform.getPropertyStatementSubmittedifAny());
            if (nform.getDateofPropertySubmittedByHRMS() != null && !nform.getDateofPropertySubmittedByHRMS().equals("")) {
                ps.setTimestamp(90, new Timestamp(sdf.parse(nform.getDateofPropertySubmittedByHRMS()).getTime()));
            } else {
                ps.setTimestamp(90, null);
            }
            if (nform.getDateofPropertySubmittedByOfficer() != null && !nform.getDateofPropertySubmittedByOfficer().equals("")) {
                ps.setTimestamp(91, new Timestamp(sdf.parse(nform.getDateofPropertySubmittedByOfficer()).getTime()));
            } else {
                ps.setTimestamp(91, null);
            }
            ps.setString(92, nform.getPresentRank());
            if (nform.getDateForDISTraining() != null && !nform.getDateForDISTraining().equals("")) {
                ps.setTimestamp(93, new Timestamp(sdf.parse(nform.getDateForDISTraining()).getTime()));
            } else {
                ps.setTimestamp(93, null);
            }
            ps.setString(94, nform.getOrderNoForDISTraining());
            ps.setInt(95, nform.getTotalpunishment1());
            ps.setInt(96, nform.getTotalpunishment2());
            ps.setInt(97, nform.getTotalpunishment3());
            ps.setString(98, nform.getServingChargeDetail());
            ps.setInt(99, Integer.parseInt(nform.getYearinServiceFromPresentRank()));
            ps.setInt(100, Integer.parseInt(nform.getMonthrinServiceFromPresentRank()));
            ps.setInt(101, Integer.parseInt(nform.getDaysinServiceFromPresentRank()));
            ps.setString(102, nform.getDeptExamPassifany());
            ps.setString(103, nform.getGradeSerialNo());
            ps.setString(104, nform.getGpfNo());
            ps.setString(105, nform.getHavePassedTrainingAsi());
            ps.setString(106, nform.getBattalionWorkExperienceifany());
            ps.setString(107, nform.getOrderNoForPassedTrainingAsi());
            if (nform.getDateOfPassedTrainingAsi() != null && !nform.getDateOfPassedTrainingAsi().equals("")) {
                ps.setTimestamp(108, new Timestamp(sdf.parse(nform.getDateOfPassedTrainingAsi()).getTime()));
            } else {
                ps.setTimestamp(108, null);
            }
            ps.setInt(109, Integer.parseInt(nform.getNominationFormId()));
            ps.execute();

            if (nform.getServiceBookCopy() != null && !nform.getServiceBookCopy().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getServiceBookCopy().getInputStream();
                String diskfilename = "SB" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getServiceBookCopy().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getServiceBookCopy().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(nform.getNominationFormId()));
                ps.setString(6, "SB");
                ps.executeUpdate();

            }
            if (nform.getRelevantServiceBookCopy() != null && !nform.getRelevantServiceBookCopy().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getRelevantServiceBookCopy().getInputStream();
                String diskfilename = "FPSB" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getRelevantServiceBookCopy().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getRelevantServiceBookCopy().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(nform.getNominationFormId()));
                ps.setString(6, "FPSB");
                ps.executeUpdate();

            }

            if (nform.getPunishmentCopy() != null && !nform.getPunishmentCopy().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getPunishmentCopy().getInputStream();
                String diskfilename = "PUNISH" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getPunishmentCopy().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getPunishmentCopy().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(nform.getNominationFormId()));
                ps.setString(6, "PUNISH");
                ps.executeUpdate();

            }

            if (nform.getDiscDocument() != null && !nform.getDiscDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getDiscDocument().getInputStream();
                String diskfilename = "DPC" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getDiscDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getDiscDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(nform.getNominationFormId()));
                ps.setString(6, "DPC");
                ps.executeUpdate();

            }

            if (nform.getDiscCCROll() != null && !nform.getDiscCCROll().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getDiscCCROll().getInputStream();
                String diskfilename = "CCROLL" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getDiscCCROll().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getDiscCCROll().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(nform.getNominationFormId()));
                ps.setString(6, "CCROLL");
                ps.executeUpdate();

            }

            if (nform.getDocumentServingCopy() != null && !nform.getDocumentServingCopy().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getDocumentServingCopy().getInputStream();
                String diskfilename = "SERV_CHRG" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getDocumentServingCopy().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getDocumentServingCopy().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(nform.getNominationFormId()));
                ps.setString(6, "SERV_CHRG");
                ps.executeUpdate();

            }

            if (nform.getFitnessDocument() != null && !nform.getFitnessDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getFitnessDocument().getInputStream();
                String diskfilename = "FITNESS" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getFitnessDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getFitnessDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(nform.getNominationFormId()));
                ps.setString(6, "FITNESS");
                ps.executeUpdate();

            }

            if (nform.getAccomplishCopy() != null && !nform.getAccomplishCopy().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getAccomplishCopy().getInputStream();
                String diskfilename = "ACCOMP" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getAccomplishCopy().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getAccomplishCopy().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(nform.getNominationFormId()));
                ps.setString(6, "ACCOMP");
                ps.executeUpdate();

            }

            if (nform.getCasteCertificate() != null && !nform.getCasteCertificate().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getCasteCertificate().getInputStream();
                String diskfilename = "CASTE" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getCasteCertificate().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getCasteCertificate().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(nform.getNominationFormId()));
                ps.setString(6, "CASTE");
                ps.executeUpdate();

            }

            if (nform.getContempletedDocument() != null && !nform.getContempletedDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getContempletedDocument().getInputStream();
                String diskfilename = "CONTEMPLET" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getContempletedDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getContempletedDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(nform.getNominationFormId()));
                ps.setString(6, "CONTEMPLET");
                ps.executeUpdate();

            }
            if (nform.getMajorPunishmentForGRDDocument() != null && !nform.getMajorPunishmentForGRDDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getMajorPunishmentForGRDDocument().getInputStream();
                String diskfilename = "MAP" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getMajorPunishmentForGRDDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getMajorPunishmentForGRDDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(nform.getNominationFormId()));
                ps.setString(6, "MAP");
                ps.executeUpdate();

            }

            if (nform.getPresentCriminalStatusDetailfile() != null && !nform.getPresentCriminalStatusDetailfile().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getPresentCriminalStatusDetailfile().getInputStream();
                String diskfilename = "PRESENT_CRIMINAL_STATUS" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getPresentCriminalStatusDetailfile().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getPresentCriminalStatusDetailfile().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(nform.getNominationFormId()));
                ps.setString(6, "PRESENT_CRIMINAL_STATUS");
                ps.executeUpdate();

            }

            if (nform.getMinorPunishmentForGRDDocument() != null && !nform.getMinorPunishmentForGRDDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getMinorPunishmentForGRDDocument().getInputStream();
                String diskfilename = "MIP" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getMinorPunishmentForGRDDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getMinorPunishmentForGRDDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(nform.getNominationFormId()));
                ps.setString(6, "MIP");
                ps.executeUpdate();

            }
            if (nform.getWillingnessCertificateDocument() != null && !nform.getWillingnessCertificateDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getWillingnessCertificateDocument().getInputStream();
                String diskfilename = "WILLINGNESS" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getWillingnessCertificateDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getWillingnessCertificateDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(nform.getNominationFormId()));
                ps.setString(6, "WILLINGNESS");
                ps.executeUpdate();

            }
            if (nform.getPrelimilaryExamDocument() != null && !nform.getPrelimilaryExamDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getPrelimilaryExamDocument().getInputStream();
                String diskfilename = "PRELIMEXAM" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, nform.getPrelimilaryExamDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, nform.getPrelimilaryExamDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(nform.getNominationFormId()));
                ps.setString(6, "PRELIMEXAM");
                ps.executeUpdate();

            }

            if (nform.getDisciplinaryproceedingfile() != null && !nform.getDisciplinaryproceedingfile().isEmpty()) {
                String diskfileName = null;
                PreparedStatement pst = null;
                String originalFileName = null;
                String contentType = null;
                ResultSet rs = null;

                diskfileName = new Date().getTime() + "";
                originalFileName = nform.getDisciplinaryproceedingfile().getOriginalFilename();
                contentType = nform.getDisciplinaryproceedingfile().getContentType();
                byte[] bytes = nform.getDisciplinaryproceedingfile().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + diskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

                pst = con.prepareStatement("select * from  police_nomination_form_different_proceeding where nomination_form_id=?");
                pst.setInt(1, Integer.parseInt(nform.getNominationFormId()));
                rs = pst.executeQuery();
                if (rs.next()) {
                    nform.setProceedingDetailId(rs.getInt("proceeding_detail_id"));
                }

                if (nform.getProceedingDetailId() == 0) {
                    pst = con.prepareStatement("insert into police_nomination_form_different_proceeding(nomination_form_id,proceeding_detail,proceeding_org_filename,proceeding_disk_filename,proceeding_filetype,file_path) values(?,?,?,?,?,?)");
                    pst.setInt(1, Integer.parseInt(nform.getNominationFormId()));
                    pst.setString(2, nform.getProceedingDetail());
                    pst.setString(3, originalFileName);
                    pst.setString(4, diskfileName);
                    pst.setString(5, contentType);
                    pst.setString(6, this.uploadPath);
                    pst.executeUpdate();
                } else {
                    pst = con.prepareStatement("update police_nomination_form_different_proceeding set proceeding_detail=?,proceeding_org_filename=?,proceeding_disk_filename=?,proceeding_filetype=?,file_path=? where nomination_form_id=?");
                    pst.setString(1, nform.getProceedingDetail());
                    pst.setString(2, originalFileName);
                    pst.setString(3, diskfileName);
                    pst.setString(4, contentType);
                    pst.setString(5, this.uploadPath);
                    pst.setInt(6, Integer.parseInt(nform.getNominationFormId()));
                    pst.executeUpdate();

                }
            }

            if ((nform.getPostingOrDeputationInOtherDistrict() != null && !nform.getPostingOrDeputationInOtherDistrict().equals(""))
                    || (nform.getPostingOrDeputationInOtherDistrictFromDate() != null && !nform.getPostingOrDeputationInOtherDistrictFromDate().equals("")) || (nform.getPostingOrDeputationInOtherDistrictToDate() != null && !nform.getPostingOrDeputationInOtherDistrictToDate().equals(""))) {
                //if (nform.getPostingOrDeputationInOtherDistrict() != null && !nform.getPostingOrDeputationInOtherDistrict().equals("")) {
                String[] postingOrDeputationInOtherDistrictArr = nform.getPostingOrDeputationInOtherDistrict().split(",");
                String[] postingOrDeputationInOtherDistrictFromDateArr = nform.getPostingOrDeputationInOtherDistrictFromDate().split(",");
                String[] postingOrDeputationInOtherDistrictToDateArr = nform.getPostingOrDeputationInOtherDistrictToDate().split(",");

                ps = con.prepareStatement("DELETE FROM police_nomination_form_different_district WHERE nomination_form_id=?");
                ps.setInt(1, Integer.parseInt(nform.getNominationFormId()));
                ps.execute();
                //String sql = "update police_nomination_form_different_district set posting_deputation_otherdistrict=?,posting_deputation_otherdistrict_fromdate=?,posting_deputation_otherdistrict_todate=? where nomination_form_id=?";
                String sql = "insert into police_nomination_form_different_district(nomination_form_id,posting_deputation_otherdistrict,posting_deputation_otherdistrict_fromdate,posting_deputation_otherdistrict_todate) values(?,?,?,?)";
                ps = con.prepareStatement(sql);

                for (int i = 0; i < postingOrDeputationInOtherDistrictArr.length; i++) {
                    ps.setInt(1, Integer.parseInt(nform.getNominationFormId()));
                    if (postingOrDeputationInOtherDistrictArr[i] != null && !postingOrDeputationInOtherDistrictArr[i].equals("")) {
                        ps.setString(2, postingOrDeputationInOtherDistrictArr[i]);
                    } else {
                        ps.setTimestamp(2, null);
                    }
                    if (postingOrDeputationInOtherDistrictFromDateArr[i] != null && !postingOrDeputationInOtherDistrictFromDateArr[i].equals("")) {
                        ps.setTimestamp(3, new Timestamp(sdf.parse(postingOrDeputationInOtherDistrictFromDateArr[i]).getTime()));
                    } else {
                        ps.setTimestamp(3, null);
                    }
                    if (postingOrDeputationInOtherDistrictToDateArr[i] != null && !postingOrDeputationInOtherDistrictToDateArr[i].equals("")) {
                        ps.setTimestamp(4, new Timestamp(sdf.parse(postingOrDeputationInOtherDistrictToDateArr[i]).getTime()));
                    } else {
                        ps.setTimestamp(4, null);
                    }

                    ps.execute();
                }
            }
            /*if ((nform.getDisciplinaryproceedingDetail() != null && !nform.getDisciplinaryproceedingDetail().equals(""))
             || (nform.getDisciplinaryproceedingfile() != null && !nform.getDisciplinaryproceedingfile().equals(""))) {
             String[] disciplinaryProceedingDetailArr = nform.getDisciplinaryproceedingDetail();
             MultipartFile[] disciplinaryProceedingorgfileArr = nform.getDisciplinaryproceedingfile();

             ps = con.prepareStatement("DELETE FROM police_nomination_form_different_district WHERE nomination_form_id=?");
             ps.setInt(1, Integer.parseInt(nform.getNominationFormId()));
             ps.execute();

             String sql = "insert into police_nomination_form_different_proceeding(nomination_form_id,proceeding_detail,proceeding_org_filename,proceeding_disk_filename,proceeding_filetype,file_path) values(?,?,?,?,?,?)";
             ps = con.prepareStatement(sql);
             for (int k = 0; k < disciplinaryProceedingorgfileArr.length; k++) {

             String disciplinaryProceedingdiskfileArr = new Date().getTime() + "";
             String disciplinaryProceedingorgfile = disciplinaryProceedingorgfileArr[k].getOriginalFilename();
             String contentType = disciplinaryProceedingorgfileArr[k].getContentType();
             byte[] bytes = disciplinaryProceedingorgfileArr[k].getBytes();

             //String[] postingOrDeputationInOtherDistrictToDateArr = nform.getPostingOrDeputationInOtherDistrictToDate().split(",");
             File dir = new File(this.uploadPath);
             if (!dir.exists()) {
             dir.mkdirs();
             }
             File serverFile = new File(dir.getAbsolutePath() + File.separator + disciplinaryProceedingdiskfileArr);
             FileOutputStream fout = new FileOutputStream(serverFile);
             fout.write(bytes);
             fout.close();

             //String sql = "update police_nomination_form_different_district set posting_deputation_otherdistrict=?,posting_deputation_otherdistrict_fromdate=?,posting_deputation_otherdistrict_todate=? where nomination_form_id=?";
             //for (int i = 0; i < disciplinaryProceedingDetailArr.length; i++) {
             ps.setInt(1, Integer.parseInt(nform.getNominationFormId()));
             if (disciplinaryProceedingDetailArr[k] != null && !disciplinaryProceedingDetailArr[k].equals("")) {
             ps.setString(2, disciplinaryProceedingDetailArr[k]);
             } else {
             ps.setString(2, null);
             }
             if (disciplinaryProceedingorgfileArr != null && !disciplinaryProceedingorgfileArr.equals("")) {
             ps.setString(3, disciplinaryProceedingorgfile);
             } else {
             ps.setString(3, null);
             }
             if (disciplinaryProceedingdiskfileArr != null && !disciplinaryProceedingdiskfileArr.equals("")) {
             ps.setString(4, disciplinaryProceedingdiskfileArr);
             } else {
             ps.setString(4, null);
             }
             if (contentType != null && !contentType.equals("")) {
             ps.setString(5, contentType);
             } else {
             ps.setString(5, null);
             }
             ps.setString(6, this.uploadPath);
             ps.execute();
                    
             }
             } */
            /*if ((nform.getDisciplinaryproceedingDetail() != null && !nform.getDisciplinaryproceedingDetail().equals(""))
             || (nform.getDisciplinaryproceedingfile() != null && !nform.getDisciplinaryproceedingfile().equals(""))) {
             String[] disciplinaryProceedingDetailArr = nform.getDisciplinaryproceedingDetail().split(",");
             String disciplinaryProceedingdiskfileArr = new Date().getTime() + "";
             String disciplinaryProceedingorgfileArr = nform.getDisciplinaryproceedingfile().getOriginalFilename();
             String contentType = nform.getDisciplinaryproceedingfile().getContentType();
             byte[] bytes = nform.getDisciplinaryproceedingfile().getBytes();
             //String[] postingOrDeputationInOtherDistrictToDateArr = nform.getPostingOrDeputationInOtherDistrictToDate().split(",");

             ps = con.prepareStatement("DELETE FROM police_nomination_form_different_proceeding WHERE nomination_form_id=?");
             ps.setInt(1, Integer.parseInt(nform.getNominationFormId()));
             ps.execute();

             File dir = new File(this.uploadPath);
             if (!dir.exists()) {
             dir.mkdirs();
             }
             File serverFile = new File(dir.getAbsolutePath() + File.separator + disciplinaryProceedingdiskfileArr);
             FileOutputStream fout = new FileOutputStream(serverFile);
             fout.write(bytes);
             fout.close();

             //String sql = "update police_nomination_form_different_district set posting_deputation_otherdistrict=?,posting_deputation_otherdistrict_fromdate=?,posting_deputation_otherdistrict_todate=? where nomination_form_id=?";
             String sql = "insert into police_nomination_form_different_proceeding(nomination_form_id,proceeding_detail,proceeding_org_filename,proceeding_disk_filename,proceeding_filetype,file_path) values(?,?,?,?,?,?)";
             ps = con.prepareStatement(sql);

             for (int i = 0; i < disciplinaryProceedingDetailArr.length; i++) {
             ps.setInt(1, Integer.parseInt(nform.getNominationFormId()));
             if (disciplinaryProceedingDetailArr[i] != null && !disciplinaryProceedingDetailArr[i].equals("")) {
             ps.setString(2, disciplinaryProceedingDetailArr[i]);
             } else {
             ps.setString(2, null);
             }
             if (disciplinaryProceedingorgfileArr != null && !disciplinaryProceedingorgfileArr.equals("")) {
             ps.setString(3, disciplinaryProceedingorgfileArr);
             } else {
             ps.setString(3, null);
             }
             if (disciplinaryProceedingdiskfileArr != null && !disciplinaryProceedingdiskfileArr.equals("")) {
             ps.setString(4, disciplinaryProceedingdiskfileArr);
             } else {
             ps.setString(4, null);
             }
             if (contentType != null && !contentType.equals("")) {
             ps.setString(5, contentType);
             } else {
             ps.setString(5, null);
             }
             ps.setString(6, this.uploadPath);

             ps.execute();
             }
             }*/

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateEmployeeNominationRecomendationData(NominationForm nform) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("update police_nomination_form set remarks_recommend_authority=?, recommend_status=?,csb_remarks=?  where nomination_form_id=? ");
            ps.setString(1, nform.getRemarkRecommendation());
            ps.setString(2, nform.getRecommendStatus());
            ps.setString(3, nform.getRemarksOfCSB());
            ps.setInt(4, Integer.parseInt(nform.getNominationFormId()));
            ps.executeUpdate();

            ps = con.prepareStatement("update police_nomination_detail set form_recomendation_status=? where nomination_detail_id=? ");
            ps.setString(1, "Y");
            ps.setInt(2, Integer.parseInt(nform.getNominationDetailId()));
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteNomination(String offCode, int nominationMasterId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean submittedstatus = false;

        try {
            con = dataSource.getConnection();

            ps = con.prepareStatement("select * from police_nomination_master where created_office=? and nomination_master_id=? and is_submitted='Y' ");
            ps.setString(1, offCode);
            ps.setInt(2, nominationMasterId);
            rs = ps.executeQuery();
            if (rs.next()) {
                submittedstatus = true;
            }

            if (!submittedstatus) {
                ps = con.prepareStatement("delete from police_nomination_master where created_office=? and nomination_master_id=?");
                ps.setString(1, offCode);
                ps.setInt(2, nominationMasterId);
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }


    @Override
    public FileAttribute getDpcDocument(String filePath, String docName, int attachId) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        File f = null;
        FileAttribute fa = new FileAttribute();
        try {
            con = this.dataSource.getConnection();
            String sql = "";
            if (docName != null && !docName.equals("")) {
                sql = "SELECT * FROM police_nomination_doc WHERE data_table_id=? and doc_name=? ";
                pst = con.prepareStatement(sql);
                pst.setInt(1, attachId);
                pst.setString(2, docName);
            } else {
                sql = "SELECT * FROM police_nomination_doc WHERE data_table_id=? ";
                pst = con.prepareStatement(sql);
                pst.setInt(1, attachId);

            }

            rs = pst.executeQuery();
            if (rs.next()) {
                String dirpath = filePath + rs.getString("file_name");

                f = new File(dirpath);
                if (f.exists()) {
                    fa.setDiskFileName(rs.getString("file_name"));
                    fa.setOriginalFileName(rs.getString("original_name"));
                    fa.setFileType(rs.getString("file_type"));
                    fa.setUploadAttachment(f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fa;
    }

    @Override
    public List getRangeOfficeList(String loginOffCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select pr.range_office_id, range_office_name from police_office_map2_range_office pm "
                    + "inner join police_range_office_list pr on pm.range_office_id=pr.range_office_id "
                    + "inner join police_office_list_data pdata on pm.est_code=pdata.est_code "
                    + "where police_off_code=? ");
            ps.setString(1, loginOffCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getString("range_office_name"));
                so.setValue(rs.getString("range_office_id"));
                li.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void submitNominationForm2RangeOffice(String fieldOffice, String rangeOffice, String nominationId) {
        Connection con = null;
        PreparedStatement ps = null;
        String submittedDateTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh.mm aa");
        submittedDateTime = dateFormat.format(cal.getTime());

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("update police_nomination_master set submitted_to_office=?, submitted_on=?, is_submitted=? where nomination_master_id=? and created_office=?");
            ps.setString(1, rangeOffice);
            ps.setTimestamp(2, new Timestamp(dateFormat.parse(submittedDateTime).getTime()));
            ps.setString(3, "Y");
            ps.setInt(4, Integer.parseInt(nominationId));
            ps.setString(5, fieldOffice);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void submitNominationForm2DGOffice(String rangeOffice, String dgOffice, String nominationId) {
        Connection con = null;
        PreparedStatement ps = null;
        String submittedDateTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh.mm aa");
        submittedDateTime = dateFormat.format(cal.getTime());
        ResultSet rs = null;
        String officeId = "";
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("select range_office_id from police_range_office_list where range_office_code=?");
            ps.setString(1, rangeOffice);
            rs = ps.executeQuery();
            if (rs.next()) {
                officeId = rs.getString("range_office_id");
            }

            ps = con.prepareStatement(" update police_nomination_master set submitted_to_dg_office=?, submitted_range_office_on=?, is_submitted_range_office=? where nomination_master_id=? and submitted_to_office=?");
            ps.setString(1, dgOffice);
            ps.setTimestamp(2, new Timestamp(dateFormat.parse(submittedDateTime).getTime()));
            ps.setString(3, "Y");
            ps.setInt(4, Integer.parseInt(nominationId));
            ps.setString(5, officeId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, con);
        }
    }

    @Override
    public List getNominatedList(String loginoffcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List nominatedlist = new ArrayList();
        EmployeeDetailsForRank edr = null;

        Calendar cal = Calendar.getInstance();
        try {
            con = this.dataSource.getConnection();

            String sql = "select nomination_master_id, created_office, off_en created_office_name,nomination_for_new_rank,post nomination_post,submitted_on"
                    + " from police_nomination_master pnm"
                    + " inner join g_post on pnm.nomination_for_new_rank=g_post.post_code"
                    + " left outer join g_office on pnm.created_office=g_office.off_code"
                    + " where submitted_to_dg_office=? and pnm.entry_year=?"
                    + " order by submitted_on desc";
            pst = con.prepareStatement(sql);
            pst.setString(1, loginoffcode);
            pst.setString(2, cal.get(Calendar.YEAR) + "");
            rs = pst.executeQuery();
            while (rs.next()) {
                edr = new EmployeeDetailsForRank();
                edr.setNominationMasterId(rs.getString("nomination_master_id"));
                edr.setSubmittedByOffice(rs.getString("created_office_name"));
                edr.setSltpostName(rs.getString("nomination_for_new_rank"));
                edr.setSltNominationForPost(rs.getString("nomination_post"));
                edr.setSubmittedOn(CommonFunctions.getFormattedOutputDate1(rs.getDate("submitted_on")));
                nominatedlist.add(edr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return nominatedlist;
    }

    @Override
    public List getNominatedListDetailView(String nominatedMasterId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List nominateddetaillist = new ArrayList();
        EmployeeDetailsForRank edr = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select nomination_detail_id,nomination_master_id,police_nomination_detail.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') full_name,post,dob,dos,joindate_of_goo doj"
                    + " from police_nomination_detail"
                    + " inner join emp_mast on police_nomination_detail.emp_id=emp_mast.emp_id"
                    + " left outer join g_post on police_nomination_detail.current_rank=g_post.post_code"
                    + " where nomination_master_id=? order by f_name";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(nominatedMasterId));
            rs = pst.executeQuery();
            while (rs.next()) {
                edr = new EmployeeDetailsForRank();
                edr.setNominationDetailId(rs.getString("nomination_detail_id"));
                edr.setNominationMasterId(rs.getString("nomination_master_id"));
                edr.setEmpId(rs.getString("emp_id"));
                edr.setEmpName(rs.getString("full_name"));
                edr.setSltpostName(rs.getString("post"));
                edr.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                edr.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                edr.setDoj(CommonFunctions.getFormattedOutputDate1(rs.getDate("doj")));
                nominateddetaillist.add(edr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return nominateddetaillist;
    }

    @Override
    public ASINominationForm getNominatedEmployeeDetailData(String nomationMasterId, String nominationDetailId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ASINominationForm nfm = null;

        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select nomination_form_id , nomination_master_id , nominationdetailid, officecode, officename, category ,"
                    + " empid , fullname, fathersname , dob , qualification,  homedistrict, g_district1.dist_name homedistrictname, doegov,"
                    + " appointed_district,g_district2.dist_name appointed_district_name,present_rank,post present_rank_post_name,place_posting,date_of_posting,doj_new_district,date_course_completion,"
                    + " year_service_length,month_service_length,date_service_length,if_redeployment_joining,date_redeployment_joining,"
                    + " year_redeployment_length,month_redeployment_length,date_redeployment_length,rewardgsmark,"
                    + " rewardgsmarkprior, rewardgsmarkduring, rewardgsmarkfrom, rewardmoneyoher, rewardmoneyoherprior, rewardmoneyoherduring,"
                    + " rewardmoneyoherfrom, rewardmedals, rewardmedalsprior, rewardmedalsduring, rewardmedalsfrom, punishmentmajor, punishmentmajorprior,"
                    + " punishmentmajorduring, punishmentmajorfrom, punishmentminor, punishmentminorprior, punishmentminorduring, punishmentminorfrom,dpcifany,dateofserving,mobile"
                    + " from police_nomination_form"
                    + " left outer join g_district g_district1 on police_nomination_form.homedistrict=g_district1.dist_code"
                    + " left outer join g_district g_district2 on police_nomination_form.appointed_district=g_district2.dist_code"
                    + " left outer join g_post on police_nomination_form.present_rank=g_post.post_code"
                    + " where nomination_master_id=? and nominationdetailid=?");
            pst.setInt(1, Integer.parseInt(nomationMasterId));
            pst.setInt(2, Integer.parseInt(nominationDetailId));
            rs = pst.executeQuery();
            if (rs.next()) {
                nfm = new ASINominationForm();
                nfm.setNominationMasterId(rs.getInt("nomination_master_id") + "");
                nfm.setNominationDetailId(rs.getInt("nominationdetailid") + "");
                nfm.setNominationFormId(rs.getInt("nomination_form_id") + "");
                nfm.setOfficeCode(rs.getString("officecode"));
                nfm.setOfficeName(rs.getString("officename"));
                nfm.setCategory(rs.getString("category"));
                nfm.setEmpId(rs.getString("empid"));
                nfm.setFullname(rs.getString("fullname"));
                nfm.setFathersname(rs.getString("fathersname"));
                nfm.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                nfm.setQualification(rs.getString("qualification"));
                nfm.setHomeDistrict(rs.getString("homedistrict"));
                nfm.setHomedistrictName(rs.getString("homedistrictname"));

                nfm.setDoeGov(CommonFunctions.getFormattedOutputDate1(rs.getDate("doegov")));
                nfm.setDojAppntdDist(rs.getString("appointed_district_name"));
                nfm.setCurrentRank(rs.getString("present_rank_post_name"));
                nfm.setPostingPlace(rs.getString("place_posting"));
                nfm.setPostingDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("date_of_posting")));

                nfm.setDojNewDist(CommonFunctions.getFormattedOutputDate1(rs.getDate("doj_new_district")));

                nfm.setTxtTrainingCompletedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("date_course_completion")));

                nfm.setYearinServiceLength(rs.getString("year_service_length"));
                nfm.setMonthinServiceLength(rs.getString("month_service_length"));
                nfm.setDaysinServiceLength(rs.getString("date_service_length"));

                nfm.setSltRedeploymentJoining(rs.getString("if_redeployment_joining"));
                nfm.setTxtRedeploymentJoiningDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("date_redeployment_joining")));

                nfm.setYearinRedeploymentServiceLength(rs.getString("year_redeployment_length"));
                nfm.setMonthRedeploymentServiceLength(rs.getString("month_redeployment_length"));
                nfm.setDaysRedeploymentServiceLength(rs.getString("date_redeployment_length"));

                nfm.setRewardGSMark(rs.getString("rewardgsmark"));
                nfm.setRewardGSMarkPrior(rs.getString("rewardgsmarkprior"));
                nfm.setRewardGSMarkDuring(rs.getString("rewardgsmarkduring"));
                nfm.setRewardGSMarkFrom(rs.getString("rewardgsmarkfrom"));

                nfm.setRewardMoneyOher(rs.getString("rewardmoneyoher"));
                nfm.setRewardMoneyOherPrior(rs.getString("rewardmoneyoherprior"));
                nfm.setRewardMoneyOherDuring(rs.getString("rewardmoneyoherduring"));
                nfm.setRewardMoneyOherFrom(rs.getString("rewardmoneyoherfrom"));

                nfm.setRewardMedals(rs.getString("rewardmedals"));
                nfm.setRewardMedalsPrior(rs.getString("rewardmedalsprior"));
                nfm.setRewardMedalsDuring(rs.getString("rewardmedalsduring"));
                nfm.setRewardMedalsFrom(rs.getString("rewardmedalsfrom"));

                nfm.setPunishmentMajor(rs.getString("punishmentmajor"));
                nfm.setPunishmentMajorPrior(rs.getString("punishmentmajorprior"));
                nfm.setPunishmentMajorDuring(rs.getString("punishmentmajorduring"));
                nfm.setPunishmentMajorFrom(rs.getString("punishmentmajorfrom"));

                nfm.setPunishmentMinor(rs.getString("punishmentminor"));
                nfm.setPunishmentMinorPrior(rs.getString("punishmentminorprior"));
                nfm.setPunishmentMinorDuring(rs.getString("punishmentminorduring"));
                nfm.setPunishmentMinorFrom(rs.getString("punishmentminorfrom"));

                nfm.setDpcifany(rs.getString("dpcifany"));

                nfm.setDateofServing(CommonFunctions.getFormattedOutputDate1(rs.getDate("dateofserving")));
                if (rs.getString("dateofserving") != null && !rs.getString("dateofserving").equals("")) {
                    nfm.setDateofServingifAny("Y");
                }
                nfm.setTxtMobile(rs.getString("mobile"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return nfm;
    }

    @Override
    public void updateEmployeeNominationRecomendationDataDG(NominationForm nform) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("update police_nomination_form set remarks_dg=? where nomination_form_id=? ");
            ps.setString(1, nform.getRemarkRecommendationDG());
            ps.setInt(2, Integer.parseInt(nform.getNominationFormId()));
            ps.executeUpdate();

            ps = con.prepareStatement("update police_nomination_detail set form_recomendation_status=? where nomination_detail_id=? ");
            ps.setString(1, "Y");
            ps.setInt(2, Integer.parseInt(nform.getNominationDetailId()));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, con);
        }
    }

    /*@Override
     public List getNominationCompletedList(String currentPost, String nominatedForPost, String fiscalyear) {
     Connection con = null;
     PreparedStatement ps = null;
     ResultSet rs = null;
     List li = new ArrayList();
     try {
     con = dataSource.getConnection();
     if (currentPost.equals("140070")) {
     ps = con.prepareStatement("select off_name_abbr, fullname, category, to_char(dob, 'DD-Mon-YYYY') dob, "
     + " to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , to_char(doegov, 'DD-Mon-YYYY') doegov, "
     + " to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector  from police_nomination_master pnm "
     + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
     + " inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
     + " inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
     + " where is_submitted='Y' and nomination_for_new_rank=? and (pnd.nominated_type='NOM' or pnd.nominated_type='EXM') and is_archived='N'"
     + " order by off_name_abbr");
     } else {
     ps = con.prepareStatement("select off_name_abbr, fullname, category, to_char(dob, 'DD-Mon-YYYY') dob, "
     + " to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , to_char(doegov, 'DD-Mon-YYYY') doegov, "
     + " to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector,pnm.entry_year  from police_nomination_master pnm "
     + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
     + " inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
     + " inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
     + " where is_submitted='Y' and nomination_for_new_rank=? and is_archived='N' and pnm.entry_year=?"
     + " order by off_name_abbr");
     }

     ps.setString(1, nominatedForPost);
     ps.setString(2, fiscalyear);
     rs = ps.executeQuery();
     NominationForm nf = new NominationForm();
     while (rs.next()) {
     nf = new NominationForm();
     nf.setOfficeName(rs.getString("off_name_abbr"));
     nf.setFullname(rs.getString("fullname"));
     nf.setCategory(rs.getString("category"));
     nf.setDob(rs.getString("dob"));
     nf.setPostingUnintDoj(rs.getString("postingunintdoj"));
     nf.setDoeGov(rs.getString("doegov"));
     nf.setJodInspector(rs.getString("jodinspector"));
     nf.setFiscalyear(rs.getString("entry_year"));
     li.add(nf);
     }

     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(ps, con);
     }

     return li;
     } */
    @Override
    public HashMap getNominationCompletedList(String currentPost, String nominatedForPost, String fiscalyear) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet res = null;
        HashMap officewiseList = new HashMap();

        try {
            con = repodataSource.getConnection();
            ps = con.prepareStatement("select police_nomination_master.nomination_master_id,off_name_abbr from police_nomination_master "
                    + " inner join police_office_list_data  on police_nomination_master.created_office=police_office_list_data.police_off_code "
                    + " where is_submitted='Y' and nomination_for_new_rank=? "
                    + " and is_archived='N' and entry_year=? group by nomination_master_id,off_name_abbr");
            ps.setString(1, nominatedForPost);
            ps.setString(2, fiscalyear);
            rs = ps.executeQuery();
            pstmt = con.prepareStatement("select pnm.nomination_master_id,off_name_abbr, fullname, category, to_char(dob, 'DD-Mon-YYYY') dob, "
                    + " to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , to_char(doegov, 'DD-Mon-YYYY') doegov, "
                    + " to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector,pnm.entry_year  from police_nomination_master pnm "
                    + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                    + " inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                    + " inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                    + " where pnm.nomination_master_id =? order by off_name_abbr");
            NominationForm nf = null;
            while (rs.next()) {
                int nomination_master_id = rs.getInt("nomination_master_id");
                String off_name_abbr = rs.getString("off_name_abbr");
                pstmt.setInt(1, nomination_master_id);
                res = pstmt.executeQuery();
                List li = new ArrayList();
                while (res.next()) {
                    nf = new NominationForm();
                    nf.setNominationMasterId(res.getString("nomination_master_id"));
                    nf.setOfficeName(res.getString("off_name_abbr"));
                    nf.setFullname(res.getString("fullname"));
                    nf.setCategory(res.getString("category"));
                    nf.setDob(res.getString("dob"));
                    nf.setPostingUnintDoj(res.getString("postingunintdoj"));
                    nf.setDoeGov(res.getString("doegov"));
                    nf.setJodInspector(res.getString("jodinspector"));
                    nf.setFiscalyear(res.getString("entry_year"));
                    li.add(nf);
                }
                officewiseList.put(off_name_abbr, li);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, con);
        }

        return officewiseList;
    }

    @Override
    public List getRecommendationCompletedList(String currentPost, String nominatedForPost, String fiscalyear) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select pnf.nominationdetailid, pnf.nomination_master_id, range_office_name, off_name_abbr, fullname, category, to_char(dob, 'DD-Mon-YYYY') dob, "
                    + " to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , to_char(doegov, 'DD-Mon-YYYY') doegov, "
                    + " to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector, remarksnominationgeneral, recommend_status,pnm.entry_year  from police_nomination_master pnm "
                    + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                    + " inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                    + " inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                    + " inner join police_range_office_list prol on pnm.submitted_to_office=prol.range_office_id::TEXT "
                    + " where is_submitted_range_office='Y' and nomination_for_new_rank=? and is_archived='N' and pnm.entry_year=?"
                    + " order by range_office_name, off_name_abbr");

            ps.setString(1, nominatedForPost);
            ps.setString(2, fiscalyear);
            rs = ps.executeQuery();
            NominationForm nf = new NominationForm();
            while (rs.next()) {
                nf = new NominationForm();
                nf.setNominationDetailId(rs.getInt("nominationdetailid") + "");
                nf.setNominationMasterId(rs.getInt("nomination_master_id") + "");
                nf.setOfficeCode(rs.getString("range_office_name"));
                nf.setOfficeName(rs.getString("off_name_abbr"));
                nf.setFullname(rs.getString("fullname"));
                nf.setCategory(rs.getString("category"));
                nf.setDob(rs.getString("dob"));
                nf.setPostingUnintDoj(rs.getString("postingunintdoj"));
                nf.setDoeGov(rs.getString("doegov"));
                nf.setJodInspector(rs.getString("jodinspector"));
                nf.setRemarksNominationGeneral(rs.getString("remarksnominationgeneral"));
                nf.setRecommendStatus(rs.getString("recommend_status"));
                li.add(nf);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, con);
        }

        return li;
    }

    @Override
    public List getRecomendationPendingList(String currentPost, String nominatedForPost, String fiscalyear) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select range_office_name, off_name_abbr, fullname, category, to_char(dob, 'DD-Mon-YYYY') dob, "
                    + " to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , to_char(doegov, 'DD-Mon-YYYY') doegov, "
                    + "  to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector,pnm.entry_year  from police_nomination_master pnm "
                    + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                    + " inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                    + " inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                    + " inner join police_range_office_list prol on pnm.submitted_to_office=prol.range_office_id::TEXT "
                    + " where is_submitted='Y' and (is_submitted_range_office<>'Y' or is_submitted_range_office is null) and nomination_for_new_rank=? and is_archived='N' and pnm.entry_year=? "
                    + " order by range_office_name, off_name_abbr");

            ps.setString(1, nominatedForPost);
            ps.setString(2, fiscalyear);
            rs = ps.executeQuery();
            NominationForm nf = new NominationForm();
            while (rs.next()) {
                nf = new NominationForm();
                nf.setOfficeCode(rs.getString("range_office_name"));
                nf.setOfficeName(rs.getString("off_name_abbr"));
                nf.setFullname(rs.getString("fullname"));
                nf.setCategory(rs.getString("category"));
                nf.setDob(rs.getString("dob"));
                nf.setPostingUnintDoj(rs.getString("postingunintdoj"));
                nf.setDoeGov(rs.getString("doegov"));
                nf.setJodInspector(rs.getString("jodinspector"));
                nf.setFiscalyear(rs.getString("entry_year"));
                li.add(nf);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, con);
        }

        return li;
    }

    public static void generateExcelSheetforCollectingMarks() {

        PreparedStatement ps = null;
        ResultSet rs = null;
        WritableWorkbook workbook = null;
        int row = 0;
        try {
            workbook = Workbook.createWorkbook(new File("d:\\GetResult.xls"));
            WritableSheet sheet = workbook.createSheet("Check List", 0);

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

            Label label = new Label(0, row, " ", headcell);//column,row
            sheet.setRowView(row, 30 * 30);
            sheet.addCell(label);
            sheet.mergeCells(0, row, 16, row);

            row = row + 1;

            sheet.mergeCells(0, row, 0, row + 2);
            label = new Label(0, row, "SL No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(1, row, 1, row + 2);
            label = new Label(1, row, "Name of the Officer", innerheadcell);
            sheet.setColumnView(1, 50);
            sheet.addCell(label);

            sheet.mergeCells(2, row, 2, row + 2);
            label = new Label(2, row, "Father's Name", innerheadcell);
            sheet.setColumnView(2, 50);
            sheet.addCell(label);

            sheet.mergeCells(3, row, 3, row + 2);
            label = new Label(3, row, "Category", innerheadcell);
            sheet.setColumnView(3, 20);
            sheet.addCell(label);

            sheet.mergeCells(4, row, 4, row + 2);
            label = new Label(4, row, "Date of Birth", innerheadcell);
            sheet.setColumnView(4, 20);
            sheet.addCell(label);

            sheet.mergeCells(5, row, 5, row + 2);
            label = new Label(5, row, "Date of entry into Govt. Service", innerheadcell);
            sheet.setColumnView(5, 20);
            sheet.addCell(label);

            sheet.mergeCells(6, row, 6, row + 2);
            label = new Label(6, row, "Date of appointent in the Present Grade", innerheadcell);
            sheet.setColumnView(6, 20);
            sheet.addCell(label);

            sheet.mergeCells(7, row, 7, row + 2);
            label = new Label(7, row, "Status of disciplinary/Proceeding/Criminal/Vigilance proceeding if any ", innerheadcell);
            sheet.setColumnView(7, 20);
            sheet.addCell(label);

            label = new Label(8, row, "Punishment", innerheadcell);
            sheet.setColumnView(8, 20);
            sheet.addCell(label);
            sheet.mergeCells(8, row, 13, row);

            sheet.mergeCells(14, row, 14, row + 2);
            label = new Label(14, row, "Date of Retirement", innerheadcell);
            sheet.setColumnView(14, 20);
            sheet.addCell(label);

            sheet.mergeCells(15, row, 15, row + 2);
            label = new Label(15, row, "Remarks of Nominating Authority", innerheadcell);
            sheet.setColumnView(15, 20);
            sheet.addCell(label);

            sheet.mergeCells(16, row, 16, row + 2);
            label = new Label(16, row, "Remarks of Recommended Authority", innerheadcell);
            sheet.setColumnView(16, 20);
            sheet.addCell(label);
            row = row + 1;

            label = new Label(8, row, "Prior to 01.01.2015", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(8, row, 9, row);

            label = new Label(10, row, "During last 5 years i.e. w.e.f. 1.1.2015 to 31.12.2019", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(10, row, 11, row);

            label = new Label(12, row, "From 1.1.20 till date of submission of Nomination Rolls", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(12, row, 13, row);

            row = row + 1;

            label = new Label(8, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(9, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(10, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(11, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(12, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(13, row, "Minor", innerheadcell);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "1", innerheadcell);
            sheet.addCell(label);
            label = new Label(1, row, "2", innerheadcell);
            sheet.addCell(label);
            label = new Label(2, row, "3", innerheadcell);
            sheet.addCell(label);
            label = new Label(3, row, "4", innerheadcell);
            sheet.addCell(label);
            label = new Label(4, row, "5", innerheadcell);
            sheet.addCell(label);
            label = new Label(5, row, "6", innerheadcell);
            sheet.addCell(label);
            label = new Label(6, row, "7", innerheadcell);
            sheet.addCell(label);
            label = new Label(7, row, "8", innerheadcell);
            sheet.addCell(label);
            label = new Label(8, row, "9", innerheadcell);
            sheet.addCell(label);
            label = new Label(9, row, "10", innerheadcell);
            sheet.addCell(label);
            label = new Label(10, row, "11", innerheadcell);
            sheet.addCell(label);
            label = new Label(11, row, "12", innerheadcell);
            sheet.addCell(label);
            label = new Label(12, row, "13", innerheadcell);
            sheet.addCell(label);
            label = new Label(13, row, "14", innerheadcell);
            sheet.addCell(label);
            label = new Label(14, row, "15", innerheadcell);
            sheet.addCell(label);
            label = new Label(15, row, "16", innerheadcell);
            sheet.addCell(label);
            label = new Label(16, row, "17", innerheadcell);
            sheet.addCell(label);

            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public ZipFileAttribute getAttachmentFiles(String formId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ZipFileAttribute zipObj = new ZipFileAttribute();
        List<String> li = new ArrayList<String>();
        Map map = new HashMap();
        try {
            con = this.dataSource.getConnection();
            String cwcCountQry = "select file_name, original_name from police_nomination_doc where data_table_id=?";
            pst = con.prepareStatement(cwcCountQry);
            pst.setInt(1, Integer.parseInt(formId));
            rs = pst.executeQuery();
            while (rs.next()) {

                li.add(rs.getString("file_name"));
                map.put(rs.getString("file_name"), rs.getString("original_name"));
            }
            zipObj.setDiskFileName(li);
            zipObj.setOrgGFileName(map);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
        return zipObj;
    }

    public static void main(String sur[]) {
        try {

            generateExcelSheetforCollectingMarks();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getEnteredGPFNoDuplicateData(String gpfno, EmployeeDetailsForRank edr) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String found = "N";

        try {
            con = this.dataSource.getConnection();

            String sql = "select * from police_nominated_emp_list where gpf_no=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, gpfno);
            rs = pst.executeQuery();
            if (rs.next()) {
                edr.setEmpName(StringUtils.defaultString(rs.getString("f_name")) + " " + StringUtils.defaultString(rs.getString("m_name")) + " " + StringUtils.defaultString(rs.getString("l_name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
    }

    @Override
    public String deletePoliceNominationDuplicateData(String gpfno) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement deletepst = null;

        String isDeleted = "N";

        try {
            con = this.dataSource.getConnection();

            String deletesql = "delete from police_nominated_emp_list where gpf_no = ?";
            deletepst = con.prepareStatement(deletesql);
            deletepst.setString(1, gpfno);
            deletepst.executeUpdate();

            deletesql = "delete from police_nomination_form where nominationdetailid=?";
            deletepst = con.prepareStatement(deletesql);

            String sql = "select nomination_detail_id from police_nomination_detail where gpf_no = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, gpfno);
            rs = pst.executeQuery();
            if (rs.next()) {
                int nominationdetailid = rs.getInt("nomination_detail_id");
                deletepst.setInt(1, nominationdetailid);
                deletepst.executeUpdate();
            }

            deletesql = "delete from police_nomination_detail where gpf_no = ?";
            deletepst = con.prepareStatement(deletesql);
            deletepst.setString(1, gpfno);
            deletepst.executeUpdate();

            isDeleted = "Y";
        } catch (Exception e) {
            isDeleted = "N";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
        return isDeleted;
    }

    @Override
    public List getDistrictWiseRecomendationListAnnextureI(String distCode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List recomendationListAnnextureI = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select police_award_form.emp_id,full_name,designation,acr_year1_grading,acr_year2_grading,acr_year3_grading,acr_year4_grading,acr_year5_grading,"
                    + "age_in_year,punishment_details,court_case_charge, "
                    + "dist_name,police_dist_code from police_award_form "
                    + "inner join g_office on police_award_form.off_code = g_office.off_code "
                    + "inner join g_district on g_office.police_dist_code = g_district.dist_code "
                    + "where award_medal_type_id='14' and award_occasion='PFD'  and police_dist_code=?");

            pst.setString(1, distCode);
            rs = pst.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            String acryear1grading = "";
            String acryear2grading = "";
            String acryear3grading = "";
            String acryear4grading = "";
            String acryear5grading = "";
            int slno = 0;
            while (rs.next()) {
                slno++;
                List<String> dataRow = new ArrayList<String>();
                dataRow.add(slno + "");
                dataRow.add(rs.getString("full_name") + ", " + rs.getString("designation"));

                if (rs.getString("acr_year1_grading") != null && rs.getString("acr_year1_grading").equals("O")) {
                    acryear1grading = "Outstanding";
                } else if (rs.getString("acr_year1_grading") != null && rs.getString("acr_year1_grading").equals("V")) {
                    acryear1grading = "Very good";
                } else if (rs.getString("acr_year1_grading") != null && rs.getString("acr_year1_grading").equals("G")) {
                    acryear1grading = "Good";
                } else if (rs.getString("acr_year1_grading") != null && rs.getString("acr_year1_grading").equals("A")) {
                    acryear1grading = "Average";
                } else if (rs.getString("acr_year1_grading") != null && rs.getString("acr_year1_grading").equals("N")) {
                    acryear1grading = "NRC";
                } else {
                    acryear1grading = rs.getString("acr_year1_grading");
                }

                dataRow.add(acryear1grading);
                if (rs.getString("acr_year2_grading") != null && rs.getString("acr_year2_grading").equals("O")) {
                    acryear2grading = "Outstanding";
                } else if (rs.getString("acr_year2_grading") != null && rs.getString("acr_year2_grading").equals("V")) {
                    acryear2grading = "Very good";
                } else if (rs.getString("acr_year2_grading") != null && rs.getString("acr_year2_grading").equals("G")) {
                    acryear2grading = "Good";
                } else if (rs.getString("acr_year2_grading") != null && rs.getString("acr_year2_grading").equals("A")) {
                    acryear2grading = "Average";
                } else if (rs.getString("acr_year2_grading") != null && rs.getString("acr_year2_grading").equals("N")) {
                    acryear2grading = "NRC";
                } else {
                    acryear2grading = rs.getString("acr_year2_grading");
                }
                dataRow.add(acryear2grading);
                if (rs.getString("acr_year3_grading") != null && rs.getString("acr_year3_grading").equals("O")) {
                    acryear3grading = "Outstanding";
                } else if (rs.getString("acr_year3_grading") != null && rs.getString("acr_year3_grading").equals("V")) {
                    acryear3grading = "Very good";
                } else if (rs.getString("acr_year3_grading") != null && rs.getString("acr_year3_grading").equals("G")) {
                    acryear3grading = "Good";
                } else if (rs.getString("acr_year3_grading") != null && rs.getString("acr_year3_grading").equals("A")) {
                    acryear3grading = "Average";
                } else if (rs.getString("acr_year3_grading") != null && rs.getString("acr_year3_grading").equals("N")) {
                    acryear3grading = "NRC";
                } else {
                    acryear3grading = rs.getString("acr_year2_grading");
                }
                dataRow.add(acryear3grading);
                if (rs.getString("acr_year4_grading") != null && rs.getString("acr_year4_grading").equals("O")) {
                    acryear4grading = "Outstanding";
                } else if (rs.getString("acr_year4_grading") != null && rs.getString("acr_year4_grading").equals("V")) {
                    acryear4grading = "Very good";
                } else if (rs.getString("acr_year4_grading") != null && rs.getString("acr_year4_grading").equals("G")) {
                    acryear4grading = "Good";
                } else if (rs.getString("acr_year4_grading") != null && rs.getString("acr_year4_grading").equals("A")) {
                    acryear4grading = "Average";
                } else if (rs.getString("acr_year4_grading") != null && rs.getString("acr_year4_grading").equals("N")) {
                    acryear4grading = "NRC";
                } else {
                    acryear4grading = rs.getString("acr_year4_grading");
                }
                dataRow.add(acryear4grading);
                if (rs.getString("acr_year5_grading") != null && rs.getString("acr_year5_grading").equals("O")) {
                    acryear5grading = "Outstanding";
                } else if (rs.getString("acr_year5_grading") != null && rs.getString("acr_year5_grading").equals("V")) {
                    acryear5grading = "Very good";
                } else if (rs.getString("acr_year5_grading") != null && rs.getString("acr_year5_grading").equals("G")) {
                    acryear5grading = "Good";
                } else if (rs.getString("acr_year5_grading") != null && rs.getString("acr_year5_grading").equals("A")) {
                    acryear5grading = "Average";
                } else if (rs.getString("acr_year5_grading") != null && rs.getString("acr_year5_grading").equals("N")) {
                    acryear5grading = "NRC";
                } else {
                    acryear5grading = rs.getString("acr_year5_grading");
                }
                dataRow.add(acryear5grading);
                dataRow.add("");
                dataRow.add(rs.getString("age_in_year"));
                dataRow.add(rs.getString("punishment_details"));
                dataRow.add(rs.getString("court_case_charge"));
                recomendationListAnnextureI.add(dataRow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
        return recomendationListAnnextureI;
    }

    @Override
    public List getDistrictWiseRecomendationListAnnextureIIA(String distCode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List recomendationListAnnextureIIA = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select police_award_form.emp_id,police_investigation.award_medal_id,full_name,designation, "
                    + "ps_name,date_of_reg,cs_fr,final_submit_date,sr_nonsr,brief_case,innovative_method,tools_used, "
                    + "evidence,chargesheet_filing,attach_crime,investigation_challenges,conv_dtls,case_no, "
                    + "dist_name,police_dist_code from police_award_form  "
                    + "inner join police_investigation on police_investigation.award_medal_id=police_award_form.award_medal_id "
                    + "inner join g_office on police_award_form.off_code = g_office.off_code  "
                    + "inner join g_district on g_office.police_dist_code = g_district.dist_code  "
                    + "where award_medal_type_id='14' and award_occasion='PFD' and police_dist_code=?");
            pst.setString(1, distCode);
            rs = pst.executeQuery();
            int slno = 0;
            while (rs.next()) {
                slno++;
                List<String> dataRow = new ArrayList<String>();
                dataRow.add(slno + "");
                dataRow.add(rs.getString("full_name") + ", " + rs.getString("designation"));
                dataRow.add(rs.getString("ps_name"));
                dataRow.add(rs.getString("case_no"));
                dataRow.add(rs.getString("date_of_reg"));
                dataRow.add(rs.getString("cs_fr"));
                dataRow.add(rs.getString("final_submit_date"));
                dataRow.add(rs.getString("sr_nonsr"));
                dataRow.add(rs.getString("brief_case"));
                dataRow.add(rs.getString("innovative_method"));
                dataRow.add(rs.getString("tools_used"));
                dataRow.add(rs.getString("evidence"));
                dataRow.add(rs.getString("chargesheet_filing"));
                dataRow.add(rs.getString("attach_crime"));
                dataRow.add(rs.getString("investigation_challenges"));
                dataRow.add(rs.getString("conv_dtls"));
                recomendationListAnnextureIIA.add(dataRow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
        return recomendationListAnnextureIIA;
    }

    @Override
    public FileAttribute getDocument(String filePath, String docName, int attachId) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        File f = null;
        FileAttribute fa = new FileAttribute();
        try {
            con = this.dataSource.getConnection();
            String sql = "";
            if (docName != null && !docName.equals("")) {
                sql = "SELECT * FROM police_nomination_doc WHERE data_table_id=? and doc_name=? ";
                pst = con.prepareStatement(sql);
                pst.setInt(1, attachId);
                pst.setString(2, docName);
            } else {
                sql = "SELECT * FROM police_nomination_doc WHERE data_table_id=? ";
                pst = con.prepareStatement(sql);
                pst.setInt(1, attachId);

            }

            rs = pst.executeQuery();
            if (rs.next()) {
                String dirpath = filePath + rs.getString("file_name");

                f = new File(dirpath);
                if (f.exists()) {
                    fa.setDiskFileName(rs.getString("file_name"));
                    fa.setOriginalFileName(rs.getString("original_name"));
                    fa.setFileType(rs.getString("file_type"));
                    fa.setUploadAttachment(f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fa;
    }

    @Override
    public int deleteNominationAttachment(int attchid, String docName, String filepath, FileAttribute fa) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        File f = null;

        int deletestatus = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "DELETE FROM police_nomination_doc WHERE data_table_id=? and doc_name=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, attchid);
            pst.setString(2, docName);
            deletestatus = pst.executeUpdate();

            if (deletestatus > 0) {
                String dirpath = filepath + fa.getDiskFileName();

                f = new File(dirpath);
                if (f.exists()) {
                    boolean isDeleted = f.delete();
                    if (isDeleted == true) {

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deletestatus;
    }

    public List getDistrictWiseEmpNomination(String distCode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List recomendationList = new ArrayList();
        AwardMedalListForm award = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select police_award_form.emp_id,full_name,designation,"
                    + "dist_name,police_dist_code ,award_medal_id,award_medal_type_id,award_occasion from police_award_form "
                    + "inner join g_office on police_award_form.off_code = g_office.off_code "
                    + "inner join g_district on g_office.police_dist_code = g_district.dist_code "
                    + "where award_medal_type_id='14' and award_occasion='PFD' and police_dist_code=?");

            pst.setString(1, distCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                award = new AwardMedalListForm();
                award.setFullname(rs.getString("full_name"));
                award.setRewardMedalId(rs.getString("award_medal_id"));
                award.setAwardMedalTypeId(rs.getString("award_medal_type_id"));
                award.setSltAwardOccasion(rs.getString("award_occasion"));
                recomendationList.add(award);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return recomendationList;
    }

    @Override
    public NominationDifferentDisciplinaryProceedingList getAttachedFileOfDPforASI2SI(int proceedingdetailid) {
        NominationDifferentDisciplinaryProceedingList nominationDifferentDisciplinaryProceedingList = new NominationDifferentDisciplinaryProceedingList();
        Connection con = null;
        Statement st = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select proceeding_org_filename,proceeding_disk_filename,file_path from police_nomination_form_different_proceeding where proceeding_detail_id = ?");
            pst.setInt(1, proceedingdetailid);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                nominationDifferentDisciplinaryProceedingList.setOriginalFilename(rs.getString("proceeding_org_filename"));
                nominationDifferentDisciplinaryProceedingList.setDiskFileNamedisciplinaryProceeding(rs.getString("proceeding_disk_filename"));
                filepath = rs.getString("file_path");
            }
            File f = new File(filepath + File.separator + nominationDifferentDisciplinaryProceedingList.getDiskFileNamedisciplinaryProceeding());
            nominationDifferentDisciplinaryProceedingList.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return nominationDifferentDisciplinaryProceedingList;
    }

    @Override
    public void deletedpDetailforASI2SI(NominationDifferentDisciplinaryProceedingList nominationDifferentDisciplinaryProceedingList) {
        PreparedStatement pst = null;
        ResultSet res = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select file_path,proceeding_org_filename,proceeding_disk_filename from police_nomination_form_different_proceeding where proceeding_detail_id=?");
            pst.setInt(1, nominationDifferentDisciplinaryProceedingList.getProceedingdetailid());
            res = pst.executeQuery();
            if (res.next()) {
                String diskFileName = res.getString("proceeding_disk_filename");
                String file_path = res.getString("file_path");
                File f = new File(file_path + File.separator + diskFileName);
                f.delete();
            }

            pst = con.prepareStatement("delete from police_nomination_form_different_proceeding where proceeding_detail_id=?");
            pst.setInt(1, nominationDifferentDisciplinaryProceedingList.getProceedingdetailid());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public NominationForm getAttachedFileOfDPforSI2Inspector(int proceedingdetailid) {
        NominationForm nform = new NominationForm();
        Connection con = null;
        Statement st = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select proceeding_org_filename,proceeding_disk_filename,file_path from police_nomination_form_different_proceeding where proceeding_detail_id = ?");
            pst.setInt(1, proceedingdetailid);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                nform.setOriginalFileNamefordisciplinaryProceeding(rs.getString("proceeding_org_filename"));
                nform.setDiskFileNamefordisciplinaryProceeding(rs.getString("proceeding_disk_filename"));
                filepath = rs.getString("file_path");
            }
            File f = new File(filepath + File.separator + nform.getDiskFileNamefordisciplinaryProceeding());
            nform.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return nform;
    }

    public void deleteAttachedFileOfDPforSI2Inspector(NominationForm nform) {
        PreparedStatement pst = null;
        ResultSet res = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select file_path,proceeding_org_filename,proceeding_disk_filename from police_nomination_form_different_proceeding where proceeding_detail_id=?");
            pst.setInt(1, nform.getProceedingDetailId());
            res = pst.executeQuery();
            if (res.next()) {
                String diskFileName = res.getString("proceeding_disk_filename");
                String file_path = res.getString("file_path");
                File f = new File(file_path + File.separator + diskFileName);
                f.delete();
            }

            pst = con.prepareStatement("update police_nomination_form_different_proceeding set proceeding_org_filename=null,proceeding_disk_filename=null,file_path=null,proceeding_filetype=null where proceeding_detail_id=?");
            pst.setInt(1, nform.getProceedingDetailId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void revertNominationDataByRangeOffice(NominationForm nform) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("update police_nomination_master set submitted_to_office=null, submitted_on=null,is_submitted=null,is_submitted_range_office='N' where nomination_master_id=?");
            pst.setInt(1, Integer.parseInt(nform.getNominationMasterId()));
            pst.executeUpdate();

            pst = con.prepareStatement("update police_nomination_form set remarks_recommend_authority=null, recommend_status=null, csb_remarks=null where nomination_master_id=? ");
            pst.setInt(1, Integer.parseInt(nform.getNominationMasterId()));
            pst.executeUpdate();

            pst = con.prepareStatement("update police_nomination_detail set form_recomendation_status=? where nomination_master_id=? ");
            pst.setString(1, "N");
            pst.setInt(2, Integer.parseInt(nform.getNominationMasterId()));
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void revertNominationDataByDGOffice(NominationForm nform) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("update police_nomination_master set submitted_to_dg_office=null, submitted_range_office_on=null,is_submitted_range_office=null where nomination_master_id=?");
            pst.setInt(1, Integer.parseInt(nform.getNominationMasterId()));
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getAllRankListForNomination() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList rankList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select * from police_rank_map");
            rs = pst.executeQuery();
            while (rs.next()) {
                NominationForm nominationForm = new NominationForm();
                nominationForm.setCurrentRankName(rs.getString("rank_name"));
                nominationForm.setPromoteToRankName(rs.getString("promot_to_rank_name"));
                nominationForm.setIsActive(rs.getString("is_active"));
                nominationForm.setRankMapId(rs.getInt("rank_map_id"));
                rankList.add(nominationForm);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return rankList;
    }

    @Override
    public String CheckStatusOfPoliceNomination(String currentPost, String nominationPost) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String msg = "";
        try {
            con = this.dataSource.getConnection();

            if (currentPost != null && !currentPost.equals("")) {

                String sql = "select is_active from police_rank_map where rank_code=? and promot_to_rank_code=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, currentPost);
                pst.setString(2, nominationPost);

                rs = pst.executeQuery();
                if (rs.next()) {
                    String currentstatus = rs.getString("is_active");
                    if (currentstatus != null && currentstatus.equals("Y")) {
                        msg = "Nomination Status For Selected rank is Active";
                    } else {
                        msg = "Nomination Status For Selected rank is Not Active";
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    public String activeStatusOfPoliceNomination(String currentPost, String nominationPost) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String msg = "N";
        try {
            con = this.dataSource.getConnection();

            if (currentPost != null && !currentPost.equals("")) {
                String sql = "select is_active from police_rank_map where rank_code=? and promot_to_rank_code=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, currentPost);
                pst.setString(2, nominationPost);
                rs = pst.executeQuery();
                if (rs.next()) {
                    String currentstatus = rs.getString("is_active");
                    if (currentstatus != null && currentstatus.equals("N")) {
                        msg = "Y";
                    }
                    DataBaseFunctions.closeSqlObjects(rs, pst);

                    sql = "UPDATE police_rank_map set is_active=? where rank_code=? AND promot_to_rank_code=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, msg);
                    pst.setString(2, currentPost);
                    pst.setString(3, nominationPost);
                    pst.executeUpdate();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    public String deActiveStatusOfPoliceNomination(String currentPost, String nominationPost) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String msg = "N";
        try {
            con = this.dataSource.getConnection();

            if (currentPost != null && !currentPost.equals("")) {
                String sql = "select is_active from police_rank_map where rank_code=? and promot_to_rank_code=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, currentPost);
                pst.setString(2, nominationPost);
                rs = pst.executeQuery();
                if (rs.next()) {
                    String currentstatus = rs.getString("is_active");
                    if (currentstatus != null && currentstatus.equals("Y")) {
                        msg = "N";
                    }
                    DataBaseFunctions.closeSqlObjects(rs, pst);

                    sql = "UPDATE police_rank_map set is_active=? where rank_code=? AND promot_to_rank_code=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, msg);
                    pst.setString(2, currentPost);
                    pst.setString(3, nominationPost);
                    pst.executeUpdate();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }
}
