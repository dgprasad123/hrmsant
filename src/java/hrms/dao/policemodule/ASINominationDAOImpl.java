package hrms.dao.policemodule;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.common.FileAttribute;
import hrms.model.policemodule.ASINominationForm;
import hrms.model.policemodule.EmployeeDetailsForRank;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
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
import java.util.Iterator;
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
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.lang.StringUtils;

public class ASINominationDAOImpl implements ASINominationDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getRepodataSource() {
        return repodataSource;
    }

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    @Override
    public List getASINominationList(String offCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List nominationlist = new ArrayList();

        Calendar cal = Calendar.getInstance();
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select entry_year,nomination_master_id, created_on, submitted_to_office, off_en submitted_to_Office_name,  submitted_on, is_submitted, nomination_for_new_rank,"
                    + " post nomination_post, (select count(*) cnt from police_nomination_detail where nomination_master_id= pnm.nomination_master_id and form_completed_status='N') completion_status,"
                    + " (select count(*) cnt from police_nomination_detail where nomination_master_id= pnm.nomination_master_id) nomination_count"
                    + " from police_nomination_master pnm"
                    + " inner join g_post on pnm.nomination_for_new_rank=g_post.post_code"
                    + " left outer join g_office on pnm.submitted_to_office=g_office.off_code"
                    + " where created_office=? and nomination_for_new_rank='140858'"
                    + " and entry_year=?"
                    + " order by created_on desc");
            pst.setString(1, offCode);
            pst.setString(2, cal.get(Calendar.YEAR) + "");
            rs = pst.executeQuery();
            while (rs.next()) {
                EmployeeDetailsForRank edt = new EmployeeDetailsForRank();
                edt.setNominationMasterId(rs.getInt("nomination_master_id") + "");
                edt.setCreatedOn(CommonFunctions.getFormattedOutputDatAndTime(rs.getTimestamp("created_on")));
                edt.setSubmittedOn(CommonFunctions.getFormattedOutputDatAndTime(rs.getTimestamp("submitted_on")));
                edt.setSubmittedToOffice(rs.getString("submitted_to_Office_name"));
                edt.setSltNominationForPost(rs.getString("nomination_post"));
                edt.setSubmittedStatusForFieldOffice(rs.getString("is_submitted"));
                if (rs.getInt("completion_status") > 0) {
                    edt.setFormCompletionStatus("N");
                } else {
                    edt.setFormCompletionStatus("Y");
                }
                edt.setNominatedEmployeeCount(rs.getString("nomination_count"));
                nominationlist.add(edt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return nominationlist;
    }

    @Override
    public List getRankListForASI(String offCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List ranklist = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement(" select gpc, post from g_spc"
                    + " inner join g_post on g_spc.gpc=g_post.post_code"
                    + " where off_code=? group by gpc, post order by post");
            pst.setString(1, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getString("post"));
                so.setValue(rs.getString("gpc"));
                ranklist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ranklist;
    }

    @Override
    public List getEmployeeListRankWiseForASI(String postName, String offCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List emplist = new ArrayList();

        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("select emp_mast.emp_id, emp_mast.gpf_no, cur_spc, gpc, joindate_of_goo, dob, dos,"
                    + " ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') full_name from emp_mast "
                    + " inner join g_spc on emp_mast.cur_spc=g_spc.spc "
                    + " inner join g_post on g_spc.gpc=g_post.post_code "
                    + " left outer join (select distinct emp_id,entry_year from police_nomination_detail) pnd on emp_mast.emp_id=pnd.emp_id "
                    + " where cur_off_code=? and gpc=? and (pnd.emp_id is null or (pnd.emp_id is not null and pnd.entry_year <> '2022')) "
                    + " order by f_name");
            pst.setString(1, offCode);
            pst.setString(2, postName);
            rs = pst.executeQuery();
            while (rs.next()) {
                EmployeeDetailsForRank emp = new EmployeeDetailsForRank();
                emp.setEmpId(rs.getString("emp_id"));
                emp.setGpfno(rs.getString("gpf_no"));
                emp.setEmpName(rs.getString("full_name"));
                emp.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                emp.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                emp.setDoj(CommonFunctions.getFormattedOutputDate1(rs.getDate("joindate_of_goo")));
                emplist.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public void createASINomination(String empId, String currentPost, String nominationPost, String offCode, String loginUserName) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String createdDateTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh.mm aa");
        createdDateTime = dateFormat.format(cal.getTime());

        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("insert into police_nomination_master (created_on, created_by_user, created_office, nomination_for_new_rank,entry_year) values(?,?,?,?,?) ", Statement.RETURN_GENERATED_KEYS);
            pst.setTimestamp(1, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            pst.setString(2, loginUserName);
            pst.setString(3, offCode);
            pst.setString(4, nominationPost);
            pst.setString(5, cal.get(Calendar.YEAR) + "");
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            rs.next();
            int nominationMaxId = rs.getInt("nomination_master_id");

            DataBaseFunctions.closeSqlObjects(rs, pst);

            pst = con.prepareStatement("insert into police_nomination_detail (nomination_master_id,emp_id,form_completed_status,current_rank,entry_year) values (?,?,?,?,?) ");

            String empIdArr[] = empId.split(",");

            for (int i = 0; i < empIdArr.length; i++) {
                pst.setInt(1, nominationMaxId);
                pst.setString(2, empIdArr[i]);
                pst.setString(3, "N");
                pst.setString(4, currentPost);
                pst.setString(5, cal.get(Calendar.YEAR) + "");
                pst.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public EmployeeDetailsForRank getASINominationCreatedData(String offCode, int nominationMasterId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        EmployeeDetailsForRank edt = new EmployeeDetailsForRank();
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("select nomination_master_id, created_on, submitted_to_office,  submitted_on, nomination_for_new_rank, current_rank from police_nomination_master"
                    + " where created_office=? and nomination_master_id=?");
            pst.setString(1, offCode);
            pst.setInt(2, nominationMasterId);
            rs = pst.executeQuery();
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
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return edt;
    }

    @Override
    public List getCreatedASINominationList(String offCode, int nominationMasterId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List creatednominationlist = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select pnd.emp_id, pnd.gpf_no, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') full_name, dob, dos,"
                    + " joindate_of_goo, nomination_detail_id, pnm.nomination_master_id, pnd.form_completed_status,pnd.current_rank,post from police_nomination_master pnm"
                    + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id"
                    + " inner join emp_mast on pnd.emp_id=emp_mast.emp_id"
                    + " left outer join g_post on pnd.current_rank=g_post.post_code"
                    + " where pnm.created_office=? and pnm.nomination_master_id=? order by full_name");
            pst.setString(1, offCode);
            pst.setInt(2, nominationMasterId);
            rs = pst.executeQuery();
            while (rs.next()) {
                EmployeeDetailsForRank emp = new EmployeeDetailsForRank();
                emp.setEmpId(rs.getString("emp_id"));
                emp.setGpfno(rs.getString("gpf_no"));
                emp.setEmpName(rs.getString("full_name"));
                emp.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                emp.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                emp.setDoj(CommonFunctions.getFormattedOutputDate1(rs.getDate("joindate_of_goo")));
                emp.setFormCompletionStatus(rs.getString("form_completed_status"));
                emp.setNominationDetailId(rs.getInt("nomination_detail_id") + "");
                emp.setNominationMasterId(rs.getInt("nomination_master_id") + "");
                emp.setSltpostName(rs.getString("post"));
                creatednominationlist.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return creatednominationlist;
    }

    @Override
    public List getEmployeeListRankWiseExcludedAlreadyMapped(String offCode, int nominationMasterId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List excludedlist = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select emp_id, gpf_no, cur_spc, gpc, joindate_of_goo, dob, dos,"
                    + "	ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') full_name from emp_mast"
                    + "	inner join g_spc on emp_mast.cur_spc=g_spc.spc"
                    + "	inner join g_post on g_spc.gpc=g_post.post_code"
                    + "	where cur_off_code=?"
                    + " and emp_id not in (select emp_id from police_nomination_detail where nomination_master_id=?)"
                    + "	order by f_name");
            pst.setString(1, offCode);
            pst.setInt(2, nominationMasterId);
            rs = pst.executeQuery();
            while (rs.next()) {
                EmployeeDetailsForRank emp = new EmployeeDetailsForRank();
                emp.setEmpId(rs.getString("emp_id"));
                emp.setGpfno(rs.getString("gpf_no"));
                emp.setEmpName(rs.getString("full_name"));
                emp.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                emp.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                emp.setDoj(CommonFunctions.getFormattedOutputDate1(rs.getDate("joindate_of_goo")));
                excludedlist.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return excludedlist;
    }

    @Override
    public void addNewEmployee(String empId, int nominationMaxId, String currentpost) {

        Connection con = null;
        PreparedStatement pst = null;

        Calendar cal = Calendar.getInstance();
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("insert into police_nomination_detail (nomination_master_id,emp_id,form_completed_status,current_rank,entry_year) values (?,?,?,?,?) ");

            String empIdArr[] = empId.split(",");

            for (int i = 0; i < empIdArr.length; i++) {
                pst.setInt(1, nominationMaxId);
                pst.setString(2, empIdArr[i]);
                pst.setString(3, "N");
                pst.setString(4, currentpost);
                pst.setString(5, cal.get(Calendar.YEAR) + "");
                pst.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
    }

    @Override
    public ASINominationForm getEmployeeASINominationData(int nominationMasterId, int nominationDetailId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ASINominationForm nfm = null;

        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select police_nomination_doc_id,nomination_form_id , nomination_master_id , nominationdetailid, officecode, officename, category ,"
                    + " empid , fullname, fathersname , dob , qualification,  homedistrict, dist_name, doegov,"
                    + " appointed_district,present_rank,place_posting,date_of_posting,doj_new_district,date_course_completion,"
                    + " year_service_length,month_service_length,date_service_length,if_redeployment_joining,date_redeployment_joining,"
                    + " year_redeployment_length,month_redeployment_length,date_redeployment_length,mobile, if_availed_reservation,brass_no "
                    + " from police_nomination_form"
                    + " left outer join g_district on police_nomination_form.homedistrict=g_district.dist_code"
                    + " left outer join police_nomination_doc on police_nomination_form.nomination_form_id=police_nomination_doc.data_table_id"
                    + " where nomination_master_id=? and nominationdetailid=?");
            pst.setInt(1, nominationMasterId);
            pst.setInt(2, nominationDetailId);
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
                nfm.setHomedistrictName(rs.getString("dist_name"));

                nfm.setDoeGov(CommonFunctions.getFormattedOutputDate1(rs.getDate("doegov")));
                nfm.setDojAppntdDist(rs.getString("appointed_district"));

                nfm.setCurrentRank(rs.getString("present_rank"));
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

                nfm.setTxtMobile(rs.getString("mobile"));
                nfm.setWhetheravailedReservationCategory(rs.getString("if_availed_reservation"));
                nfm.setPhotoAttchId(rs.getString("police_nomination_doc_id"));

                nfm.setBrassno(rs.getString("brass_no"));
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
    public ASINominationForm getEmployeeBeforeNominationData(int nominationMasterId, int nominationDetailId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        ASINominationForm nfm = null;

        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select pnd.emp_id, emp_mast.gpf_no, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') full_name, "
                    + " joindate_of_goo, dob, dos, off_en, off_code, father_name, doe_gov, res_cat from police_nomination_detail pnd "
                    + " inner join emp_mast on pnd.emp_id=emp_mast.emp_id "
                    + " inner join g_office on emp_mast.cur_off_code=g_office.off_code"
                    + " left outer join (select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') father_name, emp_id from emp_relation where relation='FATHER') emp_relation on emp_mast.emp_id=emp_relation.emp_id "
                    + " where nomination_master_id=? and nomination_detail_id=? ");
            pst.setInt(1, nominationMasterId);
            pst.setInt(2, nominationDetailId);
            rs = pst.executeQuery();
            if (rs.next()) {
                nfm = new ASINominationForm();
                nfm.setEmpId(rs.getString("emp_id"));
                nfm.setFullname(rs.getString("full_name"));
                nfm.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                //nfm.setDoeGov(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe_gov")));
                nfm.setOfficeCode(rs.getString("off_code"));
                nfm.setOfficeName(rs.getString("off_en"));
                nfm.setFathersname(rs.getString("father_name"));
                nfm.setCategory(rs.getString("res_cat"));
                nfm.setNominationDetailId(nominationDetailId + "");
                nfm.setNominationMasterId(nominationMasterId + "");
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
    public void saveEmployeeASINominationData(ASINominationForm nform, String filepath) {

        Connection con = null;

        PreparedStatement pst = null;

        InputStream inputStream = null;
        OutputStream outputStream = null;

        Calendar cal = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

            con = this.dataSource.getConnection();

            pst = con.prepareStatement("insert into police_nomination_form "
                    + " (nomination_master_id , nominationdetailid, officecode, officename, category ,"
                    + "empid , fullname, fathersname , dob , qualification,  homedistrict, doegov,"
                    + "appointed_district,present_rank,place_posting,date_of_posting,doj_new_district,date_course_completion,"
                    + "year_service_length,month_service_length,date_service_length,if_redeployment_joining,date_redeployment_joining,"
                    + "year_redeployment_length,month_redeployment_length,date_redeployment_length,mobile, if_availed_reservation,brass_no,entry_year)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, Integer.parseInt(nform.getNominationMasterId()));
            pst.setInt(2, Integer.parseInt(nform.getNominationDetailId()));
            pst.setString(3, nform.getOfficeCode());
            pst.setString(4, nform.getOfficeName());
            pst.setString(5, nform.getCategory());
            pst.setString(6, nform.getEmpId());
            pst.setString(7, nform.getFullname());
            pst.setString(8, nform.getFathersname());
            if (nform.getDob() != null && !nform.getDob().equals("")) {
                pst.setTimestamp(9, new Timestamp(sdf.parse(nform.getDob()).getTime()));
            } else {
                pst.setTimestamp(9, null);
            }
            pst.setString(10, nform.getQualification());
            pst.setString(11, nform.getHomeDistrict());
            if (nform.getDoeGov() != null && !nform.getDoeGov().equals("")) {
                pst.setTimestamp(12, new Timestamp(sdf.parse(nform.getDoeGov()).getTime()));
            } else {
                pst.setTimestamp(12, null);
            }
            pst.setString(13, nform.getDojAppntdDist());

            pst.setString(14, nform.getCurrentRank());
            pst.setString(15, nform.getPostingPlace());
            if (nform.getPostingDate() != null && !nform.getPostingDate().equals("")) {
                pst.setTimestamp(16, new Timestamp(sdf.parse(nform.getPostingDate()).getTime()));
            } else {
                pst.setTimestamp(16, null);
            }
            if (nform.getDojNewDist() != null && !nform.getDojNewDist().equals("")) {
                pst.setTimestamp(17, new Timestamp(sdf.parse(nform.getDojNewDist()).getTime()));
            } else {
                pst.setTimestamp(17, null);
            }
            if (nform.getTxtTrainingCompletedDate() != null && !nform.getTxtTrainingCompletedDate().equals("")) {
                pst.setTimestamp(18, new Timestamp(sdf.parse(nform.getTxtTrainingCompletedDate()).getTime()));
            } else {
                pst.setTimestamp(18, null);
            }
            pst.setString(19, nform.getYearinServiceLength());
            pst.setString(20, nform.getMonthinServiceLength());
            pst.setString(21, nform.getDaysinServiceLength());
            pst.setString(22, nform.getSltRedeploymentJoining());
            if (nform.getTxtRedeploymentJoiningDate() != null && !nform.getTxtRedeploymentJoiningDate().equals("")) {
                pst.setTimestamp(23, new Timestamp(sdf.parse(nform.getTxtRedeploymentJoiningDate()).getTime()));
            } else {
                pst.setTimestamp(23, null);
            }
            pst.setString(24, nform.getYearinRedeploymentServiceLength());
            pst.setString(25, nform.getMonthRedeploymentServiceLength());
            pst.setString(26, nform.getDaysRedeploymentServiceLength());
            pst.setString(27, nform.getTxtMobile());
            pst.setString(28, nform.getWhetheravailedReservationCategory());
            pst.setString(29, nform.getBrassno());
            pst.setString(30, cal.get(Calendar.YEAR) + "");
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            rs.next();

            int nomination_form_id = rs.getInt("nomination_form_id");

            DataBaseFunctions.closeSqlObjects(rs, pst);

            if (nform.getNominatedEmployeePhoto() != null) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getNominatedEmployeePhoto().getInputStream();
                String diskfilename = System.currentTimeMillis() + "";
                File newFile1 = new File(filepath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id) values(?,?,?,?,?)";
                pst = con.prepareStatement(insFileQry);
                pst.setString(1, nform.getNominatedEmployeePhoto().getContentType());
                pst.setString(2, filepath);
                pst.setString(3, diskfilename);
                pst.setString(4, nform.getNominatedEmployeePhoto().getOriginalFilename());
                pst.setInt(5, nomination_form_id);
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);
            }

            pst = con.prepareStatement("update police_nomination_detail set form_completed_status=? where nomination_detail_id=?");
            pst.setString(1, "Y");
            pst.setInt(2, Integer.parseInt(nform.getNominationDetailId()));
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
    }

    @Override
    public void updateEmployeeNominationData(ASINominationForm nform, String filepath) {

        Connection con = null;

        PreparedStatement pst = null;

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

            con = this.dataSource.getConnection();

            pst = con.prepareStatement("update police_nomination_form"
                    + " set qualification=?,homedistrict=?,doegov=?,"
                    + " appointed_district=?,present_rank=?,place_posting=?,date_of_posting=?,doj_new_district=?,date_course_completion=?,"
                    + " year_service_length=?,month_service_length=?,date_service_length=?,if_redeployment_joining=?,date_redeployment_joining=?,"
                    + " year_redeployment_length=?,month_redeployment_length=?,date_redeployment_length=?,mobile=?,if_availed_reservation=?,brass_no=? where nomination_form_id=?");
            pst.setString(1, nform.getQualification());
            pst.setString(2, nform.getHomeDistrict());
            if (nform.getDoeGov() != null && !nform.getDoeGov().equals("")) {
                pst.setTimestamp(3, new Timestamp(sdf.parse(nform.getDoeGov()).getTime()));
            } else {
                pst.setTimestamp(3, null);
            }
            pst.setString(4, nform.getDojAppntdDist());

            pst.setString(5, nform.getCurrentRank());
            pst.setString(6, nform.getPostingPlace());
            if (nform.getPostingDate() != null && !nform.getPostingDate().equals("")) {
                pst.setTimestamp(7, new Timestamp(sdf.parse(nform.getPostingDate()).getTime()));
            } else {
                pst.setTimestamp(7, null);
            }
            if (nform.getDojNewDist() != null && !nform.getDojNewDist().equals("")) {
                pst.setTimestamp(8, new Timestamp(sdf.parse(nform.getDojNewDist()).getTime()));
            } else {
                pst.setTimestamp(8, null);
            }
            if (nform.getTxtTrainingCompletedDate() != null && !nform.getTxtTrainingCompletedDate().equals("")) {
                pst.setTimestamp(9, new Timestamp(sdf.parse(nform.getTxtTrainingCompletedDate()).getTime()));
            } else {
                pst.setTimestamp(9, null);
            }
            pst.setString(10, nform.getYearinServiceLength());
            pst.setString(11, nform.getMonthinServiceLength());
            pst.setString(12, nform.getDaysinServiceLength());
            pst.setString(13, nform.getSltRedeploymentJoining());
            if (nform.getTxtRedeploymentJoiningDate() != null && !nform.getTxtRedeploymentJoiningDate().equals("")) {
                pst.setTimestamp(14, new Timestamp(sdf.parse(nform.getTxtRedeploymentJoiningDate()).getTime()));
            } else {
                pst.setTimestamp(14, null);
            }
            pst.setString(15, nform.getYearinRedeploymentServiceLength());
            pst.setString(16, nform.getMonthRedeploymentServiceLength());
            pst.setString(17, nform.getDaysRedeploymentServiceLength());
            pst.setString(18, nform.getTxtMobile());
            pst.setString(19, nform.getWhetheravailedReservationCategory());
            pst.setString(20, nform.getBrassno());
            pst.setInt(21, Integer.parseInt(nform.getNominationFormId()));
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            if (nform.getNominatedEmployeePhoto() != null && !nform.getNominatedEmployeePhoto().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = nform.getNominatedEmployeePhoto().getInputStream();
                String diskfilename = System.currentTimeMillis() + "";
                File newFile1 = new File(filepath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                pst = con.prepareStatement("delete from police_nomination_doc where data_table_id=?");
                pst.setInt(1, Integer.parseInt(nform.getNominationFormId()));
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id) values(?,?,?,?,?)";
                pst = con.prepareStatement(insFileQry);
                pst.setString(1, nform.getNominatedEmployeePhoto().getContentType());
                pst.setString(2, filepath);
                pst.setString(3, diskfilename);
                pst.setString(4, nform.getNominatedEmployeePhoto().getOriginalFilename());
                pst.setInt(5, Integer.parseInt(nform.getNominationFormId()));
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);
            }

            pst = con.prepareStatement("update police_nomination_detail set form_completed_status=? where nomination_detail_id=?");
            pst.setString(1, "Y");
            pst.setInt(2, Integer.parseInt(nform.getNominationDetailId()));
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
    }

    @Override
    public void getASINominationAnnexureAData(String nominationmasterid, WritableWorkbook workbook, String createdofficename) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int row = 0;
        try {
            con = this.dataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("Annexure A", 0);

            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);

            WritableFont innerformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);

            WritableCellFormat innercell = new WritableCellFormat(innerformat);
            //innercell.setWrap(true);

            Label label = new Label(0, row, "Information in respect of eligible and willing Constables/ Lance Naiks/ Havildars/ CI Havildars of Distict Cadre", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(0, row, 8, row);

            row = row + 1;

            label = new Label(0, row, "Name of the Police Establishment: " + createdofficename, headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(0, row, 8, row);

            row = row + 1;

            label = new Label(0, row, "SL No", headcell);
            sheet.addCell(label);
            label = new Label(1, row, "Name/ Category", headcell);
            sheet.addCell(label);
            label = new Label(2, row, "Father's Name", headcell);
            sheet.addCell(label);
            label = new Label(3, row, "Present Rank", headcell);
            sheet.addCell(label);
            label = new Label(4, row, "Date of Birth", headcell);
            sheet.addCell(label);
            label = new Label(5, row, "Date of joining\nas Constable\n& District\nin which\nappointed", headcell);
            sheet.addCell(label);
            label = new Label(6, row, "Date of completion\nof Constables\ncourse of\ntraining with\nOrder No. & Date", headcell);
            sheet.addCell(label);
            label = new Label(7, row, "Date of Joining\nin new District\non change of\ncadre in\nterms of\nPCO-342", headcell);
            sheet.addCell(label);
            label = new Label(8, row, "Remarks", headcell);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "1", headcell);
            sheet.addCell(label);
            label = new Label(1, row, "2", headcell);
            sheet.addCell(label);
            label = new Label(2, row, "3", headcell);
            sheet.addCell(label);
            label = new Label(3, row, "4", headcell);
            sheet.addCell(label);
            label = new Label(4, row, "5", headcell);
            sheet.addCell(label);
            label = new Label(5, row, "6", headcell);
            sheet.addCell(label);
            label = new Label(6, row, "7", headcell);
            sheet.addCell(label);
            label = new Label(7, row, "8", headcell);
            sheet.addCell(label);
            label = new Label(8, row, "9", headcell);
            sheet.addCell(label);

            int slno = 0;
            if (nominationmasterid != null && !nominationmasterid.equals("")) {
                String sql = "select fullname,category,fathersname,post,present_rank,dob,doegov,appointed_district,dist_name appointed_district_name,date_of_posting,place_posting,date_course_completion,doj_new_district"
                        + " from police_nomination_detail"
                        + " inner join police_nomination_master on police_nomination_detail.nomination_master_id=police_nomination_master.nomination_master_id"
                        + " inner join police_nomination_form on police_nomination_detail.nomination_detail_id=police_nomination_form.nominationdetailid"
                        + " left outer join g_post on police_nomination_form.present_rank=g_post.post_code"
                        + " left outer join g_district on police_nomination_form.appointed_district=g_district.dist_code"
                        + " where police_nomination_detail.nomination_master_id=? and (if_redeployment_joining is null or if_redeployment_joining='No') order by fullname";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(nominationmasterid));
                rs = pst.executeQuery();
                while (rs.next()) {
                    row = row + 1;
                    slno = slno + 1;

                    label = new Label(0, row, slno + "", innercell);
                    sheet.addCell(label);
                    label = new Label(1, row, rs.getString("fullname") + "/ " + rs.getString("category"), innercell);
                    sheet.addCell(label);
                    label = new Label(2, row, rs.getString("fathersname"), innercell);
                    sheet.addCell(label);
                    label = new Label(3, row, rs.getString("post"), innercell);
                    sheet.addCell(label);
                    label = new Label(4, row, CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")), innercell);
                    sheet.addCell(label);
                    label = new Label(5, row, CommonFunctions.getFormattedOutputDate1(rs.getDate("doegov")) + "/ " + rs.getString("appointed_district_name"), innercell);
                    sheet.addCell(label);
                    label = new Label(6, row, CommonFunctions.getFormattedOutputDate1(rs.getDate("date_course_completion")), innercell);
                    sheet.addCell(label);
                    label = new Label(7, row, CommonFunctions.getFormattedOutputDate1(rs.getDate("doj_new_district")), innercell);
                    sheet.addCell(label);
                    label = new Label(8, row, "", innercell);
                    sheet.addCell(label);
                }
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
    }

    @Override
    public void getASINominationAnnexureBData(String nominationmasterid, WritableWorkbook workbook, String createdofficename) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int row = 0;
        try {
            con = this.dataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("Annexure B", 0);

            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);

            WritableFont innerformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);

            WritableCellFormat innercell = new WritableCellFormat(innerformat);

            Label label = new Label(0, row, "Information in respect of eligible and willing Battalion Personnel appointed as Constable on redeployment basis in Disticts", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(0, row, 6, row);

            row = row + 1;

            label = new Label(0, row, "Name of the Police Establishment: " + createdofficename, headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(0, row, 6, row);

            row = row + 1;

            label = new Label(0, row, "SL No", headcell);
            sheet.addCell(label);
            label = new Label(1, row, "Name/ Category", headcell);
            sheet.addCell(label);
            label = new Label(2, row, "Father's Name", headcell);
            sheet.addCell(label);
            label = new Label(3, row, "Present Rank", headcell);
            sheet.addCell(label);
            label = new Label(4, row, "Date of Birth", headcell);
            sheet.addCell(label);
            label = new Label(5, row, "Date of joining\nas Constable\non re-\ndeployment\nfrom Battalion", headcell);
            sheet.addCell(label);
            label = new Label(6, row, "Remarks", headcell);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "1", headcell);
            sheet.addCell(label);
            label = new Label(1, row, "2", headcell);
            sheet.addCell(label);
            label = new Label(2, row, "3", headcell);
            sheet.addCell(label);
            label = new Label(3, row, "4", headcell);
            sheet.addCell(label);
            label = new Label(4, row, "5", headcell);
            sheet.addCell(label);
            label = new Label(5, row, "6", headcell);
            sheet.addCell(label);
            label = new Label(6, row, "7", headcell);
            sheet.addCell(label);

            int slno = 0;
            if (nominationmasterid != null && !nominationmasterid.equals("")) {
                String sql = "select fullname,category,fathersname,post,present_rank,dob,date_redeployment_joining"
                        + " from police_nomination_detail"
                        + " inner join police_nomination_master on police_nomination_detail.nomination_master_id=police_nomination_master.nomination_master_id"
                        + " inner join police_nomination_form on police_nomination_detail.nomination_detail_id=police_nomination_form.nominationdetailid"
                        + " left outer join g_post on police_nomination_form.present_rank=g_post.post_code"
                        + " where police_nomination_master.nomination_master_id=? and if_redeployment_joining='Yes' order by fullname";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(nominationmasterid));
                rs = pst.executeQuery();
                while (rs.next()) {
                    row = row + 1;
                    slno = slno + 1;

                    label = new Label(0, row, slno + "", innercell);
                    sheet.addCell(label);
                    label = new Label(1, row, rs.getString("fullname") + "/ " + rs.getString("category"), innercell);
                    sheet.addCell(label);
                    label = new Label(2, row, rs.getString("fathersname"), innercell);
                    sheet.addCell(label);
                    label = new Label(3, row, rs.getString("post"), innercell);
                    sheet.addCell(label);
                    label = new Label(4, row, CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")), innercell);
                    sheet.addCell(label);
                    label = new Label(5, row, CommonFunctions.getFormattedOutputDate1(rs.getDate("date_redeployment_joining")), innercell);
                    sheet.addCell(label);
                    label = new Label(6, row, "", innercell);
                    sheet.addCell(label);
                }
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }

    }

    @Override
    public void submitASINominationForm2RangeOffice(String fieldOffice, String rangeOffice, String nominationId) {

        Connection con = null;

        PreparedStatement pst = null;

        String submittedDateTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh.mm aa");
        submittedDateTime = dateFormat.format(cal.getTime());

        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("update police_nomination_master set submitted_to_office=?, submitted_on=?, is_submitted=?,submitted_to_dg_office=? where nomination_master_id=? and created_office=?");
            pst.setString(1, rangeOffice);
            pst.setTimestamp(2, new Timestamp(dateFormat.parse(submittedDateTime).getTime()));
            pst.setString(3, "Y");
            pst.setString(4, rangeOffice);
            pst.setInt(5, Integer.parseInt(nominationId));
            pst.setString(6, fieldOffice);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
    }

    @Override
    public ASINominationForm getCreatedOfficeName(ASINominationForm asiform) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            if (asiform.getNominationMasterId() != null && !asiform.getNominationMasterId().equals("")) {
                String sql = "select off_code,off_en from police_nomination_master"
                        + " inner join g_office on police_nomination_master.created_office=g_office.off_code where nomination_master_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(asiform.getNominationMasterId()));
                rs = pst.executeQuery();
                if (rs.next()) {
                    asiform.setOfficeCode(rs.getString("off_code"));
                    asiform.setOfficeName(rs.getString("off_en"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return asiform;
    }

    @Override
    public List getASINominationEmployeeList(int nominationMasterId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List creatednominationlist = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select pnd.emp_id, pnd.gpf_no, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') full_name, dob, dos,"
                    + " joindate_of_goo, nomination_detail_id, pnm.nomination_master_id, pnd.form_completed_status,pnd.current_rank,post from police_nomination_master pnm"
                    + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id"
                    + " inner join emp_mast on pnd.emp_id=emp_mast.emp_id"
                    + " left outer join g_post on pnd.current_rank=g_post.post_code"
                    + " where pnm.nomination_master_id=? order by full_name");
            pst.setInt(1, nominationMasterId);
            rs = pst.executeQuery();
            while (rs.next()) {
                EmployeeDetailsForRank emp = new EmployeeDetailsForRank();
                emp.setEmpId(rs.getString("emp_id"));
                emp.setGpfno(rs.getString("gpf_no"));
                emp.setEmpName(rs.getString("full_name"));
                emp.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                emp.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                emp.setDoj(CommonFunctions.getFormattedOutputDate1(rs.getDate("joindate_of_goo")));
                emp.setSltpostName(rs.getString("post"));
                creatednominationlist.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return creatednominationlist;
    }

    @Override
    public List getAllEstablishmentName() {
        List al = new ArrayList();
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select police_office, police_off_code  from police_office_list_data order by est_code");
            rs = pst.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getString("police_office"));
                so.setValue(rs.getString("police_off_code"));
                al.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return al;
    }

    @Override
    public List getAllCenterList() {
        List al = new ArrayList();
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select center_code, center_name  from police_exam_center where is_active='Y' order by center_code");
            rs = pst.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getString("center_name"));
                so.setValue(rs.getString("center_code"));
                al.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return al;
    }

    @Override
    public List getCenterPriviligeList(String entryYear) {
        List centerPrivList = new ArrayList();
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select police_nomination_form.center_code,center_name,officecode,off_en  from police_nomination_form "
                    + "inner join police_exam_center on police_nomination_form.center_code = police_exam_center.center_code::SMALLINT "
                    + "inner join g_office on police_nomination_form.officecode = g_office.off_code "
                    + "where  qualified_by_system='Q' and is_active='Y'  and entry_year=? GROUP BY police_nomination_form.center_code,center_name,officecode,off_en");
            pst.setString(1, entryYear);
            rs = pst.executeQuery();
            while (rs.next()) {
                ASINominationForm nfm = new ASINominationForm();
                nfm.setCenterCode(rs.getInt("center_code"));
                nfm.setCenterName(rs.getString("center_name"));
                nfm.setOfficeCode(rs.getString("officecode"));
                nfm.setOfficeName(rs.getString("off_en"));
                centerPrivList.add(nfm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return centerPrivList;
    }

    public void deleteCenterPriviligeListOfficeWise(ASINominationForm nfm) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("update police_nomination_form set center_code=null where qualified_by_system ='Q' and officecode=?");
            pst.setString(1, nfm.getOfficeCode());
            System.out.println("nfm.getOfficeCode()" + nfm.getOfficeCode());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public List getAllQualifiedDistrictList() {
        List ql = new ArrayList();
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select distinct officecode,off_en from police_nomination_form "
                    + "inner join g_office on police_nomination_form.officecode = g_office.off_code "
                    + "where entry_year='2022'  and  qualified_by_system='Q';");
            rs = pst.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getString("off_en"));
                so.setValue(rs.getString("officecode"));
                ql.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return ql;
    }

    public String saveAssignCenter(ASINominationForm nominationForm) {
        String msg = "Y";
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            //if(nominationForm.getQualifiedBySystem() != null ||  !nominationForm.getQualifiedBySystem().equals("") && nominationForm.getQualifiedBySystem().equals("Q")) {
            System.out.println("1111");
            pst = con.prepareStatement("update police_nomination_form set center_code=? where qualified_by_system ='Q' and officecode=?");
            pst.setInt(1, nominationForm.getCenterCode());
            System.out.println("nominationForm.getCenterCode()" + nominationForm.getCenterCode());
            pst.setString(2, nominationForm.getOfficeCode());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
        return msg;
    }

    @Override
    public String getCenterName(String centerCode) {
        Connection con = null;
        String centernmae = "";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select center_code, center_name  from police_exam_center where center_code=? and is_active='Y'");
            pst.setString(1, centerCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                centernmae = rs.getString("center_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return centernmae;
    }

    @Override
    public void getASICheckListData(WritableWorkbook workbook, String establishmentName, String photoPath, String downloadType) throws Exception {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int row = 0;

        Calendar cal = Calendar.getInstance();
        try {
            con = this.dataSource.getConnection();

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

            Label label = new Label(0, row, "ASSISTANT SUB-INSPCTOR OF POLICE CHECK LIST", headcell);//column,row
            sheet.setRowView(row, 30 * 30);
            sheet.addCell(label);
            sheet.mergeCells(0, row, 21, row);

            row = row + 1;

            label = new Label(0, row, "SL No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);
            label = new Label(1, row, "Name of candidate", innerheadcell);
            sheet.setColumnView(1, 50);
            sheet.addCell(label);
            label = new Label(2, row, "Father's Name", innerheadcell);
            sheet.setColumnView(2, 50);
            sheet.addCell(label);
            label = new Label(3, row, "Date of Birth", innerheadcell);
            sheet.setColumnView(3, 20);
            sheet.addCell(label);
            label = new Label(4, row, "Category", innerheadcell);
            sheet.setColumnView(4, 20);
            sheet.addCell(label);
            label = new Label(5, row, "Photo", innerheadcell);
            sheet.setColumnView(5, 20);
            sheet.addCell(label);
            label = new Label(6, row, "Present Rank", innerheadcell);
            sheet.setColumnView(6, 20);
            sheet.addCell(label);
            label = new Label(7, row, "HRMS Rank", innerheadcell);
            sheet.setColumnView(7, 20);
            sheet.addCell(label);
            label = new Label(8, row, "Date of appointment as Constable", innerheadcell);
            sheet.setColumnView(8, 20);
            sheet.addCell(label);
            label = new Label(9, row, "District in which appointed", innerheadcell);
            sheet.setColumnView(9, 20);
            sheet.addCell(label);
            label = new Label(10, row, "Date of completion of Constables cource of training", innerheadcell);
            sheet.setColumnView(10, 20);
            sheet.addCell(label);
            label = new Label(11, row, "No of years completed as on 01.01.21", innerheadcell);
            sheet.setColumnView(11, 10);
            sheet.addCell(label);
            sheet.mergeCells(11, row, 13, row);
            label = new Label(12, row, "", innerheadcell);
            sheet.setColumnView(12, 10);
            sheet.addCell(label);
            label = new Label(13, row, "", innerheadcell);
            sheet.setColumnView(13, 10);
            sheet.addCell(label);
            label = new Label(14, row, "Date of absorption in new district on change of cadre PCO-342", innerheadcell);
            sheet.setColumnView(14, 20);
            sheet.addCell(label);
            label = new Label(15, row, "No of years completed as on 01.01.21", innerheadcell);
            sheet.setColumnView(15, 10);
            sheet.addCell(label);
            sheet.mergeCells(15, row, 17, row);

            label = new Label(16, row, "", innerheadcell);
            sheet.setColumnView(16, 10);
            sheet.addCell(label);
            label = new Label(17, row, "", innerheadcell);
            sheet.setColumnView(17, 10);
            sheet.addCell(label);
            label = new Label(18, row, "Date of Joining as Constable on Redeployment", innerheadcell);
            sheet.setColumnView(18, 20);
            sheet.addCell(label);
            label = new Label(19, row, "No of years completed as on 01.01.21", innerheadcell);
            sheet.setColumnView(19, 10);
            sheet.addCell(label);
            sheet.mergeCells(19, row, 21, row);
            label = new Label(20, row, "", innerheadcell);
            sheet.setColumnView(20, 10);
            sheet.addCell(label);
            label = new Label(21, row, "", innerheadcell);
            sheet.setColumnView(21, 10);
            sheet.addCell(label);
            label = new Label(22, row, "Dist/Estt Code", innerheadcell);
            sheet.setColumnView(22, 15);
            sheet.addCell(label);
            label = new Label(23, row, "Application ID", innerheadcell);
            sheet.setColumnView(23, 15);
            sheet.addCell(label);
            label = new Label(24, row, "Qualified Status", innerheadcell);
            sheet.setColumnView(24, 15);
            sheet.addCell(label);
            row = row + 1;

            label = new Label(0, row, "", innerheadcell);
            sheet.addCell(label);
            label = new Label(1, row, "", innerheadcell);
            sheet.addCell(label);
            label = new Label(2, row, "", innerheadcell);
            sheet.addCell(label);
            label = new Label(3, row, "", innerheadcell);
            sheet.addCell(label);
            label = new Label(4, row, "", innerheadcell);
            sheet.addCell(label);
            label = new Label(5, row, "", innerheadcell);
            sheet.addCell(label);
            label = new Label(6, row, "", innerheadcell);
            sheet.addCell(label);
            label = new Label(7, row, "", innerheadcell);
            sheet.addCell(label);
            label = new Label(8, row, "", innerheadcell);
            sheet.addCell(label);
            label = new Label(9, row, "", innerheadcell);
            sheet.addCell(label);
            label = new Label(10, row, "", innerheadcell);
            sheet.addCell(label);
            label = new Label(11, row, "Years", innerheadcell);
            sheet.addCell(label);
            label = new Label(12, row, "Months", innerheadcell);
            sheet.addCell(label);
            label = new Label(13, row, "Days", innerheadcell);
            sheet.addCell(label);
            label = new Label(14, row, "", innerheadcell);
            sheet.addCell(label);
            label = new Label(15, row, "Years", innerheadcell);
            sheet.addCell(label);
            label = new Label(16, row, "Months", innerheadcell);
            sheet.addCell(label);
            label = new Label(17, row, "Days", innerheadcell);
            sheet.addCell(label);
            label = new Label(18, row, "", innerheadcell);
            sheet.addCell(label);
            label = new Label(19, row, "Years", innerheadcell);
            sheet.addCell(label);
            label = new Label(20, row, "Months", innerheadcell);
            sheet.addCell(label);
            label = new Label(21, row, "Days", innerheadcell);
            sheet.addCell(label);
            label = new Label(22, row, "", innerheadcell);
            sheet.addCell(label);
            label = new Label(23, row, "", innerheadcell);
            sheet.addCell(label);
            sheet.setRowView(row, 20 * 20);
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
            label = new Label(17, row, "18", innerheadcell);
            sheet.addCell(label);
            label = new Label(18, row, "19", innerheadcell);
            sheet.addCell(label);
            label = new Label(19, row, "20", innerheadcell);
            sheet.addCell(label);
            label = new Label(20, row, "21", innerheadcell);
            sheet.addCell(label);
            label = new Label(21, row, "22", innerheadcell);
            sheet.addCell(label);
            label = new Label(22, row, "23", innerheadcell);
            sheet.addCell(label);
            label = new Label(23, row, "24", innerheadcell);
            sheet.addCell(label);
            label = new Label(24, row, "25", innerheadcell);
            sheet.addCell(label);
            sheet.setRowView(row, 20 * 20);
            int slno = 0;

            WritableImage image = null;
            String downloadTypeQuery = "";
            if (downloadType != null && !downloadType.equals("")) {
                if (downloadType.equals("B")) {
                    downloadTypeQuery = "";
                } else if (downloadType.equals("Q")) {
                    downloadTypeQuery = " and qualified_by_system='Q'";
                } else if (downloadType.equals("D")) {
                    downloadTypeQuery = " and qualified_by_system='D'";
                } else if (downloadType.equals("UR")) {
                    downloadTypeQuery = " and if_availed_reservation='Yes'";
                }
            }
            /*
             String sql = "select pnf.nomination_form_id,officename, trim(fullname) fullname, trim(fathersname) fathersname, to_char(dob, 'DD-Mon-YYYY') dob, pnf.category, post present_post, cur_desg, "
             + " to_char(doegov, 'DD-Mon-YYYY') doegov,dist_name appointed_dist_name, to_char(date_of_posting, 'DD-Mon-YYYY') date_of_posting, to_char(date_course_completion, 'DD-Mon-YYYY') date_course_completion,"
             + " year_service_length,month_service_length,date_service_length,"
             + " to_char(doj_new_district, 'DD-Mon-YYYY')  doj_new_district,"
             + " to_char(date_redeployment_joining, 'DD-Mon-YYYY')  date_redeployment_joining,year_redeployment_length,"
             + " month_redeployment_length,date_redeployment_length,est_code, file_name, qualified_by_system "
             + " from police_nomination_master pnm"
             + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id"
             + " inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid"
             + " inner join g_post on pnf.present_rank=g_post.post_code"
             + " inner join g_district on pnf.appointed_district=g_district.dist_code"
             + " inner join g_office on pnm.created_office=g_office.off_code"
             + " inner join police_office_list_data on g_office.ddo_code=police_office_list_data.police_ddo_code"
             + " left outer join police_nomination_doc pdoc on pnf.nomination_form_id= pdoc.data_table_id"
             + " left outer join (select cur_desg, emp_code from aq_mast where aq_year=2020 and aq_month=8 limit 1) aq_mast on pnf.empid=aq_mast.emp_code "
             + " where is_submitted='Y' and submitted_to_dg_office='CTCHOM0050000'"
             + " and nomination_for_new_rank='140858' " + downloadTypeQuery + "  order by est_code, trim(fullname)";

             if (establishmentName != null && !establishmentName.equalsIgnoreCase("") && !establishmentName.equalsIgnoreCase("ALL")) {
             sql = "select pnf.nomination_form_id,officename, trim(fullname) fullname, trim(fathersname) fathersname, to_char(dob, 'DD-Mon-YYYY') dob, pnf.category, post present_post, cur_desg, "
             + " to_char(doegov, 'DD-Mon-YYYY') doegov,dist_name appointed_dist_name, to_char(date_of_posting, 'DD-Mon-YYYY') date_of_posting, to_char(date_course_completion, 'DD-Mon-YYYY') date_course_completion,"
             + " year_service_length,month_service_length,date_service_length,"
             + " to_char(doj_new_district, 'DD-Mon-YYYY')  doj_new_district,"
             + " to_char(date_redeployment_joining, 'DD-Mon-YYYY')  date_redeployment_joining,year_redeployment_length,"
             + " month_redeployment_length,date_redeployment_length,est_code, file_name, qualified_by_system "
             + " from police_nomination_master pnm"
             + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id"
             + " inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid"
             + " inner join g_post on pnf.present_rank=g_post.post_code"
             + " inner join g_office on pnm.created_office=g_office.off_code"
             + " inner join g_district on pnf.appointed_district=g_district.dist_code"
             + " inner join police_office_list_data on g_office.ddo_code=police_office_list_data.police_ddo_code"
             + " left outer join police_nomination_doc pdoc on pnf.nomination_form_id= pdoc.data_table_id"
             + " left outer join (select cur_desg, emp_code from aq_mast where aq_year=2020 and aq_month=8 limit 1) aq_mast on pnf.empid=aq_mast.emp_code "
             + " where is_submitted='Y' and submitted_to_dg_office='CTCHOM0050000' and nomination_for_new_rank='140858' and g_office.off_code='" + establishmentName + "' " + downloadTypeQuery + " order by est_code, trim(fullname)";
             }
             */

            String sql = "select pnf.nomination_form_id,officename, trim(fullname) fullname, trim(fathersname) fathersname, to_char(dob, 'DD-Mon-YYYY') dob, pnf.category, post present_post, cur_desg, "
                    + " to_char(doegov, 'DD-Mon-YYYY') doegov,dist_name appointed_dist_name, to_char(date_of_posting, 'DD-Mon-YYYY') date_of_posting, to_char(date_course_completion, 'DD-Mon-YYYY') date_course_completion,"
                    + " year_service_length,month_service_length,date_service_length,"
                    + " to_char(doj_new_district, 'DD-Mon-YYYY')  doj_new_district,"
                    + " to_char(date_redeployment_joining, 'DD-Mon-YYYY')  date_redeployment_joining,year_redeployment_length,"
                    + " month_redeployment_length,date_redeployment_length,est_code, off_name_abbr, file_name, qualified_by_system "
                    + " from police_nomination_master pnm"
                    + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id"
                    + " inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid"
                    + " inner join g_post on pnf.present_rank=g_post.post_code"
                    + " inner join g_district on pnf.appointed_district=g_district.dist_code"
                    + " inner join g_office on pnm.created_office=g_office.off_code"
                    + " inner join police_office_list_data on g_office.ddo_code=police_office_list_data.police_ddo_code"
                    + " left outer join police_nomination_doc pdoc on pnf.nomination_form_id= pdoc.data_table_id"
                    + " left outer join (select cur_desg, emp_code from aq_mast where aq_year=2021 and aq_month=6 limit 1) aq_mast on pnf.empid=aq_mast.emp_code "
                    + " where is_submitted='Y' and submitted_to_dg_office='CTCHOM0050000'"
                    + " and nomination_for_new_rank='140858' and pnm.entry_year='" + cal.get(Calendar.YEAR) + "' " + downloadTypeQuery + "  order by est_code, trim(fullname)";
            if (establishmentName != null && !establishmentName.equalsIgnoreCase("") && !establishmentName.equalsIgnoreCase("ALL")) {
                sql = "select pnf.nomination_form_id,officename, trim(fullname) fullname, trim(fathersname) fathersname, "
                        + " to_char(dob, 'DD-Mon-YYYY') dob, pnf.category, post present_post, cur_desg,  to_char(doegov, 'DD-Mon-YYYY') doegov,"
                        + " dist_name appointed_dist_name, to_char(date_of_posting, 'DD-Mon-YYYY') date_of_posting, "
                        + " to_char(date_course_completion, 'DD-Mon-YYYY') date_course_completion, year_service_length,month_service_length,\n"
                        + " date_service_length, to_char(doj_new_district, 'DD-Mon-YYYY')  doj_new_district, to_char(date_redeployment_joining, 'DD-Mon-YYYY')  "
                        + " date_redeployment_joining,year_redeployment_length, month_redeployment_length,date_redeployment_length, est_code, off_name_abbr, file_name, "
                        + " qualified_by_system from police_nomination_master pnm"
                        + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                        + " inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                        + " inner join g_post on pnf.present_rank=g_post.post_code"
                        + " inner join g_district on pnf.appointed_district=g_district.dist_code"
                        + " inner join police_office_list_data pold on pnm.created_office=pold.police_off_code\n"
                        + " left outer join police_nomination_doc pdoc on pnf.nomination_form_id= pdoc.data_table_id "
                        + " left outer join (select cur_desg, emp_code from aq_mast where aq_year=2021 and aq_month=6 limit 1) "
                        + " aq_mast on pnf.empid=aq_mast.emp_code "
                        + " where is_submitted='Y' and submitted_to_dg_office='CTCHOM0050000' "
                        + " and nomination_for_new_rank='140858' and pnm.entry_year='" + cal.get(Calendar.YEAR) + "' and created_office='" + establishmentName + "' " + downloadTypeQuery + " and form_completed_status='Y' "
                        + " order by est_code, trim(fullname)";
            }
            System.out.println(sql);
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                row = row + 1;
                slno = slno + 1;

                label = new Label(0, row, slno + "", innercell);
                sheet.addCell(label);
                label = new Label(1, row, rs.getString("fullname"), innercell);
                sheet.addCell(label);
                label = new Label(2, row, rs.getString("fathersname"), innercell);
                sheet.addCell(label);
                label = new Label(3, row, rs.getString("dob"), innercell);
                sheet.addCell(label);
                label = new Label(4, row, rs.getString("category"), innercell);
                sheet.addCell(label);
                /*
                 if (withPhoto.equals("Y")) {
                 String imgName = rs.getString("file_name");
                 if (imgName != null && !imgName.equals("")) {
                 System.out.println(photoPath + imgName);
                 sheet.setRowView(row, 40 * 40);
                 image = new WritableImage(5, row, 1, 1, new File(photoPath + imgName + ".png"));
                 image.setHeight(1.0);
                 image.setWidth(1.0);
                 sheet.addImage(image);
                 }
                 }
                 */
                label = new Label(5, row, "", innercell);
                sheet.addCell(label);
                label = new Label(6, row, rs.getString("present_post"), innercell);
                sheet.addCell(label);
                label = new Label(7, row, rs.getString("cur_desg"), innercell);
                sheet.addCell(label);
                label = new Label(8, row, rs.getString("doegov"), innercell);
                sheet.addCell(label);
                label = new Label(9, row, rs.getString("appointed_dist_name"), innercell);
                sheet.addCell(label);
                label = new Label(10, row, rs.getString("date_course_completion"), innercell);
                sheet.addCell(label);
                label = new Label(11, row, rs.getString("year_service_length"), innercell);
                sheet.addCell(label);
                label = new Label(12, row, rs.getString("month_service_length"), innercell);
                sheet.addCell(label);
                label = new Label(13, row, rs.getString("date_service_length"), innercell);
                sheet.addCell(label);
                label = new Label(14, row, rs.getString("doj_new_district"), innercell);
                sheet.addCell(label);

                int yearlength = 0;
                int monthlength = 0;
                int dayslength = 0;

                if (rs.getString("doj_new_district") != null && !rs.getString("doj_new_district").equals("")) {
                    String dateofAbsorption = rs.getString("doj_new_district");
                    //Converting String to Date
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                    Date date = formatter.parse(dateofAbsorption);
                    //Converting obtained Date object to LocalDate object
                    Instant instant = date.toInstant();
                    ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
                    LocalDate givenDate = zone.toLocalDate();

                    Date date2 = formatter.parse("01-JAN-2021");
                    //Converting obtained Date object to LocalDate object
                    Instant instant2 = date2.toInstant();
                    ZonedDateTime zone2 = instant2.atZone(ZoneId.systemDefault());
                    LocalDate nowDate = zone2.toLocalDate();
                    System.out.println(givenDate + "==" + nowDate);
                    //Calculating the difference between given date to current date.
                    Period period = Period.between(givenDate, nowDate);

                    yearlength = period.getYears();
                    monthlength = period.getMonths();
                    dayslength = period.getDays();
                }
                label = new Label(15, row, yearlength + "", innercell);
                sheet.addCell(label);
                label = new Label(16, row, monthlength + "", innercell);
                sheet.addCell(label);
                label = new Label(17, row, dayslength + "", innercell);
                sheet.addCell(label);
                label = new Label(18, row, rs.getString("date_redeployment_joining"), innercell);
                sheet.addCell(label);
                label = new Label(19, row, rs.getString("year_redeployment_length"), innercell);
                sheet.addCell(label);
                label = new Label(20, row, rs.getString("month_redeployment_length"), innercell);
                sheet.addCell(label);
                label = new Label(21, row, rs.getString("date_redeployment_length"), innercell);
                sheet.addCell(label);
                label = new Label(22, row, rs.getString("off_name_abbr"), innercell);
                sheet.addCell(label);
                label = new Label(23, row, rs.getString("nomination_form_id"), innercell);
                sheet.addCell(label);
                label = new Label(24, row, rs.getString("qualified_by_system"), innercell);
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
    public String checkClosingDateForASInomination(int year, int month, int date, int time) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String closingStatus = "";
        int closingYear = 0;
        int closingMonth = 0;
        int closingDate = 0;
        int closingTime = 0;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement(" select global_var_name, global_var_value, message from hrms_config where global_var_name like 'STOP ASI CLOSING%' ");
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getString("global_var_name").equalsIgnoreCase("STOP ASI CLOSING YEAR")) {
                    closingStatus = rs.getString("message");
                    closingYear = Integer.parseInt(rs.getString("global_var_value"));
                } else if (rs.getString("global_var_name").equalsIgnoreCase("STOP ASI CLOSING MONTH")) {
                    closingMonth = Integer.parseInt(rs.getString("global_var_value"));
                } else if (rs.getString("global_var_name").equalsIgnoreCase("STOP ASI CLOSING DATE")) {
                    closingDate = Integer.parseInt(rs.getString("global_var_value"));
                } else if (rs.getString("global_var_name").equalsIgnoreCase("STOP ASI CLOSING TIME")) {
                    closingTime = Integer.parseInt(rs.getString("global_var_value"));
                }

            }
            if (year <= closingYear && month <= closingMonth && date < closingDate) {
                closingStatus = "";
            } else if (year <= closingYear && month <= closingMonth && date == closingDate && time < closingTime) {
                closingStatus = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return closingStatus;
    }

    @Override
    public List downloadASIAdmitCard(String offCode, String filepath) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();

        Calendar cal = Calendar.getInstance();
        try {
            con = this.dataSource.getConnection();
            if (offCode != null && !offCode.equals("") && !offCode.equalsIgnoreCase("ALL")) {
                ps = con.prepareStatement("select nomination_form_id, officename, category, fullname, fathersname, "
                        + " admit_card_rollno, original_name, file_type, file_name, center_name, pnf.center_code, exam_date "
                        + " from police_nomination_form  pnf "
                        + " left outer join police_nomination_doc pdoc on pnf.nomination_form_id= pdoc.data_table_id  "
                        + " left outer join police_office_list_data plod on pnf.officecode=plod.police_off_code "
                        + " left outer join (select * from police_exam_center where is_active='Y') pec on pnf.center_code=pec.center_code::SMALLINT "
                        + " where qualified_by_system='Q' and officecode=? and pnf.entry_year='" + cal.get(Calendar.YEAR) + "' order by admit_card_rollno");
                ps.setString(1, offCode);
            } else {
                ps = con.prepareStatement("select nomination_form_id, officename, category, fullname, fathersname,  "
                        + " admit_card_rollno, original_name, file_type, file_name, center_name, pnf.center_code, exam_date  "
                        + " from police_nomination_form  pnf "
                        + " left outer join police_nomination_doc pdoc on pnf.nomination_form_id= pdoc.data_table_id  "
                        + " left outer join police_office_list_data plod on pnf.officecode=plod.police_off_code "
                        + " left outer join (select * from police_exam_center where is_active='Y') pec on pnf.center_code=pec.center_code::SMALLINT "
                        + " where qualified_by_system='Q' and pnf.entry_year='" + cal.get(Calendar.YEAR) + "' order by officename, admit_card_rollno");
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                ASINominationForm asf = new ASINominationForm();
                asf.setNominationFormId(rs.getString("nomination_form_id"));
                asf.setFathersname(rs.getString("fathersname"));
                asf.setFullname(rs.getString("fullname"));
                asf.setOfficeName(rs.getString("officename"));
                asf.setCategory(rs.getString("category"));
                asf.setAdmitCardRollNo(rs.getString("admit_card_rollno"));
                asf.setOriginalFileName(rs.getString("original_name"));
                asf.setDiskfileName(rs.getString("file_name"));
                asf.setFileType(rs.getString("file_type"));
                asf.setSltCenter(rs.getString("center_name"));
                asf.setExamDate(rs.getString("exam_date"));
                li.add(asf);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getCenterWiseCandidateList(String centerCode, String filepath) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();

        Calendar cal = Calendar.getInstance();
        try {
            con = this.dataSource.getConnection();
            if (centerCode != null && !centerCode.equals("")) {
                ps = con.prepareStatement("select distinct admit_card_rollno,nomination_form_id, off_name_abbr, category, fullname, fathersname, "
                        + " original_name, file_type, file_name, center_name, pnf.center_code, exam_date "
                        + " from police_nomination_form  pnf "
                        + " left outer join police_nomination_doc pdoc on pnf.nomination_form_id= pdoc.data_table_id  "
                        + " left outer join police_office_list_data plod on pnf.officecode=plod.police_off_code "
                        + " left outer join (select * from police_exam_center where is_active='Y') pec on pnf.center_code=pec.center_code::SMALLINT "
                        + " where qualified_by_system='Q' and pnf.center_code=? and pnf.entry_year='" + cal.get(Calendar.YEAR) + "' order by admit_card_rollno");
                ps.setInt(1, Integer.parseInt(centerCode));

            }

            rs = ps.executeQuery();
            while (rs.next()) {
                ASINominationForm asf = new ASINominationForm();
                asf.setNominationFormId(rs.getString("nomination_form_id"));
                asf.setFathersname(rs.getString("fathersname"));
                asf.setFullname(rs.getString("fullname"));
                asf.setOfficeName(rs.getString("off_name_abbr"));
                asf.setCategory(rs.getString("category"));
                asf.setAdmitCardRollNo(rs.getString("admit_card_rollno"));
                asf.setOriginalFileName(rs.getString("original_name"));
                asf.setDiskfileName(rs.getString("file_name"));
                asf.setFileType(rs.getString("file_type"));
                asf.setSltCenter(rs.getString("center_name"));
                asf.setExamDate(rs.getString("exam_date"));
                li.add(asf);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    public static Date StringToDate(String dob) throws ParseException {
        //Instantiating the SimpleDateFormat class
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        //Parsing the given String to Date object
        Date date = formatter.parse(dob);
        System.out.println("Date object value: " + date);
        return date;
    }

    public static void main2(String sur[]) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://192.168.1.19/hrmis", "hrmis2", "cmgi");
            //Connection con = DriverManager.getConnection("jdbc:postgresql://172.16.1.14:6432/hrmis", "hrmis2", "cmgi");
            generateExcelSheetforCollectingMarks(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateQualifyList(Connection con) {

        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;

        Calendar cal = Calendar.getInstance();
        try {

            ps = con.prepareStatement("select nomination_form_id,  "
                    + "  to_char(date_course_completion, 'DD-Mon-YYYY') date_course_completion,"
                    + " year_service_length,month_service_length,date_service_length,"
                    + " to_char(doj_new_district, 'DD-Mon-YYYY')  doj_new_district,"
                    + " to_char(date_redeployment_joining, 'DD-Mon-YYYY')  date_redeployment_joining,year_redeployment_length,"
                    + " month_redeployment_length,date_redeployment_length, qualified_by_system "
                    + " from police_nomination_form where entry_year='" + cal.get(Calendar.YEAR) + "'");

            rs = ps.executeQuery();

            ps2 = con.prepareStatement("update police_nomination_form set qualified_by_system=? where nomination_form_id=?");

            while (rs.next()) {
                System.out.println("hh");
                //10== date_course_completion    == year_service_length
                //14== doj_new_district          
                //18== date_redeployment_joining == year_redeployment_length
                String qualifiedStatus = "";

                int yearlength = 0;
                int monthlength = 0;
                int dayslength = 0;

                if (rs.getString("doj_new_district") != null && !rs.getString("doj_new_district").equals("")) {
                    String dateofAbsorption = rs.getString("doj_new_district");
                    //Converting String to Date
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                    Date date = formatter.parse(dateofAbsorption);
                    //Converting obtained Date object to LocalDate object
                    Instant instant = date.toInstant();
                    ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
                    LocalDate givenDate = zone.toLocalDate();

                    Date date2 = formatter.parse("01-JAN-2021");
                    //Converting obtained Date object to LocalDate object
                    Instant instant2 = date2.toInstant();
                    ZonedDateTime zone2 = instant2.atZone(ZoneId.systemDefault());
                    LocalDate nowDate = zone2.toLocalDate();
                    System.out.println(givenDate + "==" + nowDate);
                    //Calculating the difference between given date to current date.
                    Period period = Period.between(givenDate, nowDate);

                    yearlength = period.getYears();
                    monthlength = period.getMonths();
                    dayslength = period.getDays();
                }

                if (rs.getString("date_course_completion") != null && !rs.getString("date_course_completion").equals("") && rs.getString("doj_new_district") != null && !rs.getString("doj_new_district").equals("") && rs.getString("date_redeployment_joining") != null && !rs.getString("date_redeployment_joining").equals("")) {
                    System.out.println("formid==" + rs.getInt("nomination_form_id"));
                    if (rs.getInt("year_service_length") >= 7 && yearlength >= 7 && rs.getInt("year_redeployment_length") >= 7) {
                        qualifiedStatus = "Q";
                    }
                } else if (rs.getString("date_course_completion") != null && !rs.getString("date_course_completion").equals("") && rs.getString("date_redeployment_joining") != null && !rs.getString("date_redeployment_joining").equals("")) {
                    System.out.println("formid==" + rs.getInt("nomination_form_id"));
                    if (rs.getInt("year_service_length") >= 7 && rs.getInt("year_redeployment_length") >= 7) {
                        qualifiedStatus = "Q";
                    }
                } else if (rs.getString("date_course_completion") != null && !rs.getString("date_course_completion").equals("") && rs.getString("doj_new_district") != null && !rs.getString("doj_new_district").equals("")) {
                    System.out.println("formid==" + rs.getInt("nomination_form_id"));
                    if (rs.getInt("year_service_length") >= 7 && yearlength >= 7) {
                        qualifiedStatus = "Q";
                    }
                } else if (rs.getString("date_course_completion") != null && !rs.getString("date_course_completion").equals("")) {
                    System.out.println("formid==" + rs.getInt("nomination_form_id"));
                    if (rs.getInt("year_service_length") >= 7) {
                        qualifiedStatus = "Q";
                    }
                }
                ps2.setString(1, qualifiedStatus);
                ps2.setInt(2, rs.getInt("nomination_form_id"));
                ps2.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generateExcelSheetforCollectingMarks(Connection con) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        WritableWorkbook workbook = null;
        int row = 0;

        Calendar cal = Calendar.getInstance();

        try {
            workbook = Workbook.createWorkbook(new File("d:\\GetResult.xls"));
            WritableSheet sheet = workbook.createSheet("Enter Marks", 0);

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

            sheet.setRowView(row, 30 * 30);
            Label label = new Label(0, row, "Information in respect of eligible and willing Constables/ Lance Naiks/ Havildars/ CI Havildars of Distict Cadre", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(0, row, 9, row);

            row = row + 1;

            sheet.setRowView(row, 30 * 30);
            label = new Label(0, row, "Sl No", innerheadcell);
            sheet.addCell(label);

            label = new Label(1, row, "Hall Ticket No", innerheadcell);
            sheet.addCell(label);

            label = new Label(2, row, "Name of the Candidate", innerheadcell);
            sheet.setColumnView(2, 35);
            sheet.addCell(label);

            label = new Label(3, row, "Category", innerheadcell);
            sheet.setColumnView(3, 15);
            sheet.addCell(label);

            label = new Label(4, row, "Father's Name", innerheadcell);
            sheet.setColumnView(4, 35);
            sheet.addCell(label);

            label = new Label(5, row, "Date of Birth", innerheadcell);
            sheet.setColumnView(5, 15);
            sheet.addCell(label);

            label = new Label(6, row, "Date of Appointment", innerheadcell);
            sheet.setColumnView(6, 15);
            sheet.addCell(label);

            label = new Label(7, row, "District/Establishment", innerheadcell);
            sheet.setColumnView(7, 45);
            sheet.addCell(label);

            label = new Label(8, row, "Paper1 Mark", innerheadcell);
            sheet.addCell(label);

            label = new Label(9, row, "Paper2 Mark", innerheadcell);
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

            int i = 1;
            ps = con.prepareStatement(" SELECT admit_card_rollno, fullname, category, fathersname, to_char(dob, 'DD-Mon-YYYY') dob, to_char(doegov, 'DD-Mon-YYYY') doegov, off_name_abbr, p1_marks, p2_marks "
                    + " FROM POLICE_NOMINATION_FORM pnf "
                    + " left outer join police_office_list_data pold on pnf.officecode=pold.police_off_code "
                    + " WHERE QUALIFIED_BY_SYSTEM ='Q' and pnf.entry_year='" + cal.get(Calendar.YEAR) + "'"
                    + " order by admit_card_rollno ");
            rs = ps.executeQuery();
            while (rs.next()) {
                row = row + 1;

                label = new Label(0, row, i + "", innercell);
                sheet.addCell(label);

                label = new Label(1, row, rs.getString("admit_card_rollno"), innercell);
                sheet.addCell(label);

                label = new Label(2, row, rs.getString("fullname"), innercell);
                sheet.addCell(label);

                if (rs.getString("category") != null && !rs.getString("category").equals("")) {
                    if (rs.getString("category").equalsIgnoreCase("ST")) {
                        label = new Label(2, row, rs.getString("category"), innercell);
                        sheet.addCell(label);
                    } else {
                        label = new Label(2, row, "UR", innercell);
                        sheet.addCell(label);
                    }
                } else {
                    label = new Label(2, row, rs.getString("category"), innercell);
                    sheet.addCell(label);
                }
                label = new Label(4, row, rs.getString("fathersname"), innercell);
                sheet.addCell(label);

                label = new Label(5, row, rs.getString("dob"), innercell);
                sheet.addCell(label);

                label = new Label(6, row, rs.getString("doegov"), innercell);
                sheet.addCell(label);

                label = new Label(7, row, rs.getString("off_name_abbr"), innercell);
                sheet.addCell(label);

                label = new Label(8, row, rs.getString("p1_marks"), innercell);
                sheet.addCell(label);

                label = new Label(9, row, rs.getString("p2_marks"), innercell);
                sheet.addCell(label);

                i++;
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public List getQualifiedListinASIExam(String offcode, String filepath) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();

        Calendar cal = Calendar.getInstance();
        try {
            con = this.dataSource.getConnection();
            if (offcode != null && !offcode.equals("")) {
                ps = con.prepareStatement("select nomination_master_id, nominationdetailid, nomination_form_id, admit_card_rollno, category, fullname, fathersname, to_char(dob, 'DD-Mon-YYYY') dob from police_nomination_form "
                        + " where qualified_by_system='Q' and qualified_in_exam='Q' and officecode=? and entry_year='" + cal.get(Calendar.YEAR) + "'"
                        + " order by admit_card_rollno ");
                ps.setString(1, offcode);

                System.out.println("==" + offcode);
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                ASINominationForm asf = new ASINominationForm();
                asf.setNominationDetailId(rs.getInt("nominationdetailid") + "");
                asf.setNominationMasterId(rs.getInt("nomination_master_id") + "");
                asf.setNominationFormId(rs.getString("nomination_form_id"));
                asf.setFathersname(rs.getString("fathersname"));
                asf.setFullname(rs.getString("fullname"));
                asf.setCategory(rs.getString("category"));
                asf.setAdmitCardRollNo(rs.getString("admit_card_rollno"));
                asf.setDob(rs.getString("dob"));
                li.add(asf);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getCandidateListApplyForASIExam() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();

        Calendar cal = Calendar.getInstance();
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') full_name,nomination_form_id, "
                    + " admit_card_rollno,police_nomination_form.CATEGORY,fathersname,to_char(police_nomination_form.dob, 'DD-Mon-YYYY') dob,to_char(police_nomination_form.doegov, 'DD-Mon-YYYY') doegov,officename,  "
                    + " to_char(date_course_completion, 'DD-Mon-YYYY') date_course_completion,"
                    + " year_service_length,month_service_length,date_service_length,"
                    + " to_char(doj_new_district, 'DD-Mon-YYYY')  doj_new_district,"
                    + " to_char(date_redeployment_joining, 'DD-Mon-YYYY')  date_redeployment_joining,year_redeployment_length,"
                    + " month_redeployment_length,date_redeployment_length, qualified_by_system "
                    + " from police_nomination_form "
                    + " inner join emp_mast on police_nomination_form.empid = emp_mast.emp_id where entry_year='2022'");

            rs = ps.executeQuery();
            while (rs.next()) {
                ASINominationForm asf = new ASINominationForm();
                asf.setEmpId(rs.getString("full_name"));
                asf.setNominationFormId(rs.getInt("nomination_form_id") + "");
                asf.setOfficeName(rs.getString("officename"));
                asf.setAdmitCardRollNo(rs.getString("admit_card_rollno"));
                asf.setCategory(rs.getString("CATEGORY"));
                asf.setFathersname(rs.getString("fathersname"));
                asf.setDob(rs.getString("dob"));
                asf.setDoeGov(rs.getString("doegov"));
                asf.setTxtTrainingCompletedDate(rs.getString("date_course_completion"));
                asf.setYearinServiceLength(rs.getString("year_service_length"));
                asf.setMonthinServiceLength(rs.getString("month_service_length"));
                asf.setDaysinServiceLength(rs.getString("date_service_length"));
                asf.setSltRedeploymentJoining(rs.getString("date_redeployment_joining"));
                asf.setYearinRedeploymentServiceLength(rs.getString("year_redeployment_length"));
                asf.setMonthRedeploymentServiceLength(rs.getString("month_redeployment_length"));
                asf.setDaysRedeploymentServiceLength(rs.getString("date_redeployment_length"));
                li.add(asf);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    public static void main(String sur[]) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://192.168.1.19/hrmis", "hrmis2", "cmgi");
            //Connection con = DriverManager.getConnection("jdbc:postgresql://172.16.1.14:6432/hrmis", "hrmis2", "cmgi");
            PreparedStatement ps = null;
            PreparedStatement ps2 = null;
            ResultSet rs = null;

            ps2 = con.prepareStatement("update police_nomination_form set final_appointment_date=? where admit_card_rollno=?");

            ps = con.prepareStatement("select doegov, date_redeployment_joining, admit_card_rollno from police_nomination_form where final_qualified='Y'");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("date_redeployment_joining") != null && !rs.getString("date_redeployment_joining").equals("")) {
                    ps2.setTimestamp(1, rs.getTimestamp("date_redeployment_joining"));
                } else {
                    ps2.setTimestamp(1, rs.getTimestamp("doegov"));
                }
                ps2.setInt(2, rs.getInt("admit_card_rollno"));
                ps2.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateQualifiedListinASIExam() {
        try {
            Connection con = null;
            PreparedStatement ps = null;
            PreparedStatement ps2 = null;
            ResultSet rs = null;
            con = dataSource.getConnection();
            ps = con.prepareStatement("select nomination_form_id,  "
                    + "  to_char(date_course_completion, 'DD-Mon-YYYY') date_course_completion,"
                    + " year_service_length,month_service_length,date_service_length,"
                    + " to_char(doj_new_district, 'DD-Mon-YYYY')  doj_new_district,"
                    + " to_char(date_redeployment_joining, 'DD-Mon-YYYY')  date_redeployment_joining,year_redeployment_length,"
                    + " month_redeployment_length,date_redeployment_length, qualified_by_system "
                    + " from police_nomination_form where entry_year='2022'");

            rs = ps.executeQuery();

            ps2 = con.prepareStatement("update police_nomination_form set qualified_by_system=? where nomination_form_id=? and entry_year='2022'");

            while (rs.next()) {
                //10== date_course_completion    == year_service_length
                //14== doj_new_district          
                //18== date_redeployment_joining == year_redeployment_length
                String qualifiedStatus = "";

                int yearlength = 0;
                int monthlength = 0;
                int dayslength = 0;

                if (rs.getString("doj_new_district") != null && !rs.getString("doj_new_district").equals("")) {
                    String dateofAbsorption = rs.getString("doj_new_district");
                    //Converting String to Date
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                    Date date = formatter.parse(dateofAbsorption);
                    //Converting obtained Date object to LocalDate object
                    Instant instant = date.toInstant();
                    ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
                    LocalDate givenDate = zone.toLocalDate();

                    Date date2 = formatter.parse("01-JAN-2022");
                    //Converting obtained Date object to LocalDate object
                    Instant instant2 = date2.toInstant();
                    ZonedDateTime zone2 = instant2.atZone(ZoneId.systemDefault());
                    LocalDate nowDate = zone2.toLocalDate();
                    //Calculating the difference between given date to current date.
                    Period period = Period.between(givenDate, nowDate);

                    yearlength = period.getYears();
                    monthlength = period.getMonths();
                    dayslength = period.getDays();
                }

                if (rs.getString("date_course_completion") != null && !rs.getString("date_course_completion").equals("") && rs.getString("doj_new_district") != null && !rs.getString("doj_new_district").equals("") && rs.getString("date_redeployment_joining") != null && !rs.getString("date_redeployment_joining").equals("")) {
                    if (rs.getInt("year_service_length") >= 7 && yearlength >= 7 && rs.getInt("year_redeployment_length") >= 7) {
                        qualifiedStatus = "Q";
                    }
                } else if (rs.getString("date_course_completion") != null && !rs.getString("date_course_completion").equals("") && rs.getString("date_redeployment_joining") != null && !rs.getString("date_redeployment_joining").equals("")) {
                    if (rs.getInt("year_service_length") >= 7 && rs.getInt("year_redeployment_length") >= 7) {
                        qualifiedStatus = "Q";
                    }
                } else if (rs.getString("date_course_completion") != null && !rs.getString("date_course_completion").equals("") && rs.getString("doj_new_district") != null && !rs.getString("doj_new_district").equals("")) {
                    if (rs.getInt("year_service_length") >= 7 && yearlength >= 7) {
                        qualifiedStatus = "Q";
                    }
                } else if (rs.getString("date_course_completion") != null && !rs.getString("date_course_completion").equals("")) {
                    if (rs.getInt("year_service_length") >= 7) {
                        qualifiedStatus = "Q";
                    }
                } else if ((rs.getString("date_course_completion") == null || rs.getString("date_course_completion").equals("")) && (rs.getString("doj_new_district") == null || rs.getString("doj_new_district").equals("")) && rs.getString("date_redeployment_joining") != null && !rs.getString("date_redeployment_joining").equals("")) {
                    if (rs.getInt("year_redeployment_length") >= 7) {
                        qualifiedStatus = "Q";
                    }
                }
                ps2.setString(1, qualifiedStatus);
                ps2.setInt(2, rs.getInt("nomination_form_id"));
                ps2.execute();
            }

            ps2 = con.prepareStatement("update police_nomination_form set qualified_by_system='D' where qualified_by_system <>'Q'");
            ps2.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String generateHallTicketForASIExam() {
        String msg = "Y";
        int centerCode = 4;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select officecode,count(*) cnt  from police_nomination_form where  "
                    + "qualified_by_system ='Q' and entry_year='2022' and center_code=? group by officecode order by cnt");
            ps.setInt(1, centerCode);
            rs = ps.executeQuery();

            ArrayList<ArrayList<Integer>> districtoffices = new ArrayList<>();
            int samlest = 0;
            int larget = 0;
            while (rs.next()) {
                String offCode = rs.getString("officecode");
                ArrayList<Integer> nominationids = getNominationList(offCode, con, centerCode);
                if (samlest == 0) {
                    samlest = nominationids.size();
                    larget = nominationids.size();
                } else if (nominationids.size() < samlest) {
                    samlest = nominationids.size();
                } else if (nominationids.size() > larget) {
                    larget = nominationids.size();
                }
                districtoffices.add(nominationids);
            }
            System.out.println("samlest:" + samlest);
            System.out.println("larget:" + larget);
            HashMap<Integer, String> seatingList = new HashMap<>();
            boolean isloopcontinue = true;
            int slno = 1;
            int loopingcnt = 0;
            boolean isallemppty = false;
            while (loopingcnt < 85) {
                for (int i = 0; i < districtoffices.size(); i++) {
                    ArrayList<Integer> nominationids = districtoffices.get(i);
                    String rollnumber = StringUtils.leftPad(slno + "", 3, "0");
                    System.out.println(nominationids.size() + "***" + loopingcnt);
                    if (nominationids.size() > loopingcnt) {
                        System.out.println("loopingcnt:" + loopingcnt);
                        seatingList.put(nominationids.get(loopingcnt), rollnumber);
                        slno++;
                        //nominationids.remove(loopingcnt);
                        isloopcontinue = false;
                    } else {
                        isloopcontinue = true;
                    }
                }

                loopingcnt++;
            }
            System.out.println("seatingList:" + seatingList.size());
            Iterator hmIterator = seatingList.entrySet().iterator();

            ps2 = con.prepareStatement("update police_nomination_form set admit_card_rollno=? where nomination_form_id=?");
            while (hmIterator.hasNext()) {
                Map.Entry mapElement = (Map.Entry) hmIterator.next();
                System.out.println(mapElement.getKey() + "**" + mapElement.getValue());
                String admitCard = centerCode + (String) mapElement.getValue();
                ps2.setInt(1, Integer.parseInt(admitCard));
                ps2.setInt(2, (Integer) mapElement.getKey());
                ps2.executeUpdate();
            }
            //PreparedStatement ps2 = null;
            ps = con.prepareStatement("select nomination_form_id  from police_nomination_form "
                    + " where  qualified_by_system ='Q' and entry_year='2022' and center_code=?");
            ps.setInt(1, centerCode);
            rs = ps.executeQuery();

            ps2 = con.prepareStatement("update police_nomination_form set admit_card_rollno=? where nomination_form_id=?");

            String str = "";
            int i = 1;
            String admitCard = "";

            while (rs.next()) {
                str = i + "";
                String slno1 = StringUtils.leftPad(i + "", 4, "0");
                admitCard = centerCode + slno1;
                ps2.setInt(1, Integer.parseInt(admitCard));
                ps2.setInt(2, rs.getInt("nomination_form_id"));
                ps2.executeUpdate();
                System.out.println(admitCard);
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    private static ArrayList<Integer> getNominationList(String offcode, Connection con, int centerCode) throws SQLException {
        ArrayList<Integer> nominationids = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            ps = con.prepareStatement("select nomination_form_id  from police_nomination_form where  "
                    + "qualified_by_system ='Q' and entry_year='2022' and center_code=? and officecode=?");
            ps.setInt(1, centerCode);
            ps.setString(2, offcode);
            res = ps.executeQuery();
            while (res.next()) {
                nominationids.add(res.getInt("nomination_form_id"));
            }
        } catch (SQLException sqe) {
            res.close();
            ps.close();
        }
        return nominationids;
    }
}
