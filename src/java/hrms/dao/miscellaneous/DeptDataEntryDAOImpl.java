package hrms.dao.miscellaneous;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.miscellaneous.ScStRecruitment;
import hrms.model.miscellaneous.Rascheme;
import hrms.model.miscellaneous.RecruitmentDrive;
import hrms.model.miscellaneous.Bleo;
import hrms.model.miscellaneous.CommissionCourtCase;
import hrms.model.miscellaneous.CommissionPending;
import hrms.model.miscellaneous.SelectedCandidates;
import hrms.model.miscellaneous.SelectedCandidatesCategory;
import hrms.model.miscellaneous.Training;
import hrms.model.miscellaneous.VacantPost;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class DeptDataEntryDAOImpl implements DeptDataEntryDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int saveScStRecruitment(ScStRecruitment scstRecruitment) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            String mcode = CommonFunctions.getMaxCode("g_sc_st_recruitment", "id_recruitment", con);
            pst = con.prepareStatement("INSERT INTO g_sc_st_recruitment(id_recruitment,id_department,post_name,st_vacancy,sc_vacancy,req_sent_status,letter_no,letter_date,recruiting_agency,adv_details,exam_details,results_details,appointment_details,st_sponsored,sc_sponsored,remarks,month,year,id_cader,resent,sponsored,post_vacant,adv_status,exam_status,result_status,sponsoring_status) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, mcode);
            pst.setString(2, scstRecruitment.getDeptId());
            pst.setString(3, scstRecruitment.getPostCode());
            pst.setString(4, scstRecruitment.getStVacancy());
            pst.setString(5, scstRecruitment.getScVacancy());
            pst.setString(6, scstRecruitment.getReqSentStatus());
            pst.setString(7, scstRecruitment.getLetterNo());
            pst.setString(8, scstRecruitment.getLetterDate());
            pst.setString(9, scstRecruitment.getRecruitAgency());
            pst.setString(10, scstRecruitment.getAdvmentDetails());
            pst.setString(11, scstRecruitment.getExamDetails());
            pst.setString(12, scstRecruitment.getResultDetails());
            pst.setString(13, scstRecruitment.getAppointmentDetails());
            pst.setString(14, scstRecruitment.getStSponsored());
            pst.setString(15, scstRecruitment.getScSponsored());
            pst.setString(16, scstRecruitment.getRemarks());
            pst.setString(17, scstRecruitment.getMonth());
            pst.setString(18, scstRecruitment.getYear());
            pst.setString(19, scstRecruitment.getCadreCode());
            pst.setString(20, scstRecruitment.getResent());
            pst.setString(21, scstRecruitment.getSponsored());
            pst.setString(22, scstRecruitment.getPostVacant());
            pst.setString(23, scstRecruitment.getAdvStatus());
            pst.setString(24, scstRecruitment.getExamStatus());
            pst.setString(25, scstRecruitment.getResultStatus());
            pst.setString(26, scstRecruitment.getSponsoringStatus());
            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public int updateRecruitmentData(ScStRecruitment appoint) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE  g_sc_st_recruitment SET post_name=?,st_vacancy=?,sc_vacancy=?,req_sent_status=?,letter_no=?,letter_date=?,recruiting_agency=?,adv_details=?,exam_details=?,results_details=?,appointment_details=?,st_sponsored=?,sc_sponsored=?,remarks=?,month=?,year=?,id_cader=?,resent=?,sponsored=?,post_vacant=?,adv_status=?,exam_status=?,result_status=?,sponsoring_status=? WHERE id_recruitment=?");
            pst.setString(1, appoint.getPostCode());
            pst.setString(2, appoint.getStVacancy());
            pst.setString(3, appoint.getScVacancy());
            pst.setString(4, appoint.getReqSentStatus());
            pst.setString(5, appoint.getLetterNo());
            pst.setString(6, appoint.getLetterDate());
            pst.setString(7, appoint.getRecruitAgency());
            pst.setString(8, appoint.getAdvmentDetails());
            pst.setString(9, appoint.getExamDetails());
            pst.setString(10, appoint.getResultDetails());
            pst.setString(11, appoint.getAppointmentDetails());
            pst.setString(12, appoint.getStSponsored());
            pst.setString(13, appoint.getScSponsored());
            pst.setString(14, appoint.getRemarks());
            pst.setString(15, appoint.getMonth());
            pst.setString(16, appoint.getYear());
            pst.setString(17, appoint.getCadreCode());
            pst.setString(18, appoint.getResent());
            pst.setString(19, appoint.getSponsored());
            pst.setString(20, appoint.getPostVacant());
            pst.setString(21, appoint.getAdvStatus());
            pst.setString(22, appoint.getExamStatus());
            pst.setString(23, appoint.getResultStatus());
            pst.setString(24, appoint.getSponsoringStatus());
            pst.setString(25, appoint.getIdRecruitment());
            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public int deleteRecruitment(String recruitmentId) {
        int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM g_sc_st_recruitment WHERE id_recruitment =?");
            pst.setString(1, recruitmentId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public ScStRecruitment editRecruitment(String recruitmentId) {
        ScStRecruitment appoint = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT * FROM g_sc_st_recruitment WHERE id_recruitment=?");
            pst.setString(1, recruitmentId);
            rs = pst.executeQuery();
            if (rs.next()) {
                appoint = new ScStRecruitment();
                appoint.setMonth(rs.getString("month"));
                appoint.setYear(rs.getString("year"));
                appoint.setCadreCode(rs.getString("id_cader"));
                appoint.setResent(rs.getString("resent"));
                appoint.setSponsored(rs.getString("sponsored"));
                appoint.setPostVacant(rs.getString("post_vacant"));
                appoint.setAdvStatus(rs.getString("adv_status"));
                appoint.setExamStatus(rs.getString("exam_status"));
                appoint.setResultStatus(rs.getString("result_status"));
                appoint.setSponsoringStatus(rs.getString("sponsoring_status"));
                appoint.setPostName(rs.getString("post_name"));
                appoint.setStVacancy(rs.getString("st_vacancy"));
                appoint.setScVacancy(rs.getString("sc_vacancy"));
                appoint.setReqSentStatus(rs.getString("req_sent_status"));
                appoint.setLetterNo(rs.getString("letter_no"));
                appoint.setLetterDate(rs.getString("letter_date"));
                appoint.setRecruitAgency(rs.getString("recruiting_agency"));
                appoint.setAdvmentDetails(rs.getString("adv_details"));
                appoint.setExamDetails(rs.getString("exam_details"));
                appoint.setResultDetails(rs.getString("results_details"));
                appoint.setAppointmentDetails(rs.getString("appointment_details"));
                appoint.setStSponsored(rs.getString("st_sponsored"));
                appoint.setScSponsored(rs.getString("sc_sponsored"));
                appoint.setRecruitmentId(rs.getString("remarks"));
                appoint.setIdRecruitment(rs.getString("id_recruitment"));
                appoint.setRemarks(rs.getString("remarks"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return appoint;
    }

    @Override
    public List getScStRecruitmentList(String deptid) {
        List list = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ScStRecruitment appoint = null;
        list = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT a.*,b.post FROM g_sc_st_recruitment a,g_post b WHERE  a.post_name=b.post_code AND a.id_department='" + deptid + "' ORDER BY id_recruitment DESC");
            rs = pst.executeQuery();
            while (rs.next()) {
                appoint = new ScStRecruitment();
                appoint.setMonth(rs.getString("month"));
                appoint.setYear(rs.getString("year"));
                appoint.setPostName(rs.getString("post"));
                appoint.setStVacancy(rs.getString("st_vacancy"));
                appoint.setScVacancy(rs.getString("sc_vacancy"));
                appoint.setReqSentStatus(rs.getString("req_sent_status"));
                appoint.setLetterNo(rs.getString("letter_no"));
                appoint.setLetterDate(rs.getString("letter_date"));
                appoint.setRecruitAgency(rs.getString("recruiting_agency"));
                appoint.setAdvmentDetails(rs.getString("adv_details"));
                appoint.setExamDetails(rs.getString("exam_details"));
                appoint.setResultDetails(rs.getString("results_details"));
                appoint.setAppointmentDetails(rs.getString("appointment_details"));
                appoint.setStSponsored(rs.getString("st_sponsored"));
                appoint.setScSponsored(rs.getString("sc_sponsored"));
                appoint.setRemarks(rs.getString("remarks"));
                appoint.setIdRecruitment(rs.getString("id_recruitment"));
                String cadername = rs.getString("id_cader");
                String month_year = rs.getString("month") + ", " + rs.getString("year");
                if (cadername.equals("0")) {
                    appoint.setCadreCode("All");
                } else {
                    appoint.setCadreCode(rs.getString("id_cader"));
                }
                appoint.setYear(month_year);
                list.add(appoint);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public int saveRascheme(Rascheme raScheme) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            String mcode = CommonFunctions.getMaxCode("g_ra_scheme", "id_ra_scheme", con);
            pst = con.prepareStatement("INSERT INTO g_ra_scheme(id_ra_scheme,id_department,total_case,total_appointment,total_rejected,total_cleared,total_pending,remarks,month,year,lastmonth_pending,instituted_case,disposed_case,disposed_last_month,total_app_lastmonth,total_rej_lastmonth) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, mcode);
            pst.setString(2, raScheme.getDeptId());
            pst.setString(3, raScheme.getTotalCase());
            pst.setString(4, raScheme.getTotalAppointment());
            pst.setString(5, raScheme.getTotalRejected());
            pst.setString(6, raScheme.getTotalCleared());
            pst.setString(7, raScheme.getTotalPending());
            pst.setString(8, raScheme.getRemarks());
            pst.setString(9, raScheme.getMonth());
            pst.setString(10, raScheme.getYear());
            pst.setString(11, raScheme.getLastmonthPending());
            pst.setString(12, raScheme.getInstitutedCase());
            pst.setString(13, raScheme.getDisposedcase());
            pst.setString(14, raScheme.getDisposedlastMonth());
            pst.setString(15, raScheme.getTotalAppoLastmonth());
            pst.setString(16, raScheme.getTotalRejLastmonth());
            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public int updateAppointmentStatusData(Rascheme appointStatus) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE  g_ra_scheme SET id_department=?,total_case=?,total_appointment=?,total_rejected=?,total_cleared=?,total_pending=?,remarks=?,month=?,year=?,lastmonth_pending=?,instituted_case=?,disposed_case=?,disposed_last_month=?,total_app_lastmonth=?,total_rej_lastmonth=? WHERE id_ra_scheme =?");

            pst.setString(1, appointStatus.getDeptId());
            pst.setString(2, appointStatus.getTotalCase());
            pst.setString(3, appointStatus.getTotalAppointment());
            pst.setString(4, appointStatus.getTotalRejected());
            pst.setString(5, appointStatus.getTotalCleared());
            pst.setString(6, appointStatus.getTotalPending());
            pst.setString(7, appointStatus.getRemarks());
            pst.setString(8, appointStatus.getMonth());
            pst.setString(9, appointStatus.getYear());

            pst.setString(10, appointStatus.getLastmonthPending());
            pst.setString(11, appointStatus.getInstitutedCase());
            pst.setString(12, appointStatus.getDisposedcase());
            pst.setString(13, appointStatus.getDisposedlastMonth());
            pst.setString(14, appointStatus.getTotalAppoLastmonth());
            pst.setString(15, appointStatus.getTotalRejLastmonth());
            pst.setString(16, appointStatus.getRaSchemeId());
            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public int deleteAppointmentStatus(String raSchemeId) {
        int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM g_ra_scheme WHERE id_ra_scheme =?");
            pst.setString(1, raSchemeId);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public Rascheme editAppointmentStatus(String raSchemeId) {
        Rascheme appointStatus = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT * FROM g_ra_scheme WHERE id_ra_scheme=?");
            pst.setString(1, raSchemeId);
            rs = pst.executeQuery();
            if (rs.next()) {
                appointStatus = new Rascheme();
                appointStatus.setDeptId(rs.getString("id_department"));
                appointStatus.setTotalCase(rs.getString("total_case"));
                appointStatus.setTotalAppointment(rs.getString("total_appointment"));
                appointStatus.setTotalRejected(rs.getString("total_rejected"));
                appointStatus.setTotalCleared(rs.getString("total_cleared"));
                appointStatus.setTotalPending(rs.getString("total_pending"));
                appointStatus.setRemarks(rs.getString("remarks"));
                appointStatus.setMonth(rs.getString("month"));
                appointStatus.setYear(rs.getString("year"));
                appointStatus.setLastmonthPending(rs.getString("lastmonth_pending"));
                appointStatus.setInstitutedCase(rs.getString("instituted_case"));
                appointStatus.setDisposedcase(rs.getString("disposed_case"));
                appointStatus.setDisposedlastMonth(rs.getString("disposed_last_month"));
                appointStatus.setTotalAppoLastmonth(rs.getString("total_app_lastmonth"));
                appointStatus.setTotalRejLastmonth(rs.getString("total_rej_lastmonth"));
                appointStatus.setRaSchemeId(raSchemeId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return appointStatus;
    }

    @Override
    public List getRaschemeList(String deptId) {
        List list = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Rascheme appointStatus = null;
        list = new ArrayList();

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT * FROM g_ra_scheme WHERE id_department='" + deptId + "' ORDER BY id_ra_scheme DESC");
            rs = pst.executeQuery();
            while (rs.next()) {
                appointStatus = new Rascheme();
                appointStatus.setDeptId(rs.getString("id_department"));
                appointStatus.setTotalCase(rs.getString("total_case"));
                appointStatus.setTotalAppointment(rs.getString("total_appointment"));
                appointStatus.setTotalRejected(rs.getString("total_rejected"));
                appointStatus.setTotalCleared(rs.getString("total_cleared"));
                appointStatus.setTotalPending(rs.getString("total_pending"));
                appointStatus.setRemarks(rs.getString("remarks"));
                appointStatus.setMonth(rs.getString("month"));
                appointStatus.setYear(rs.getString("year"));
                appointStatus.setLastmonthPending(rs.getString("lastmonth_pending"));
                appointStatus.setInstitutedCase(rs.getString("instituted_case"));
                appointStatus.setDisposedcase(rs.getString("disposed_case"));
                appointStatus.setDisposedlastMonth(rs.getString("disposed_last_month"));
                appointStatus.setRaSchemeId(rs.getString("id_ra_scheme"));

                appointStatus.setTotalAppoLastmonth(rs.getString("total_app_lastmonth"));
                appointStatus.setTotalRejLastmonth(rs.getString("total_rej_lastmonth"));

                list.add(appointStatus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public int saveBleo(Bleo empStrength, String deptId) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            String mcode = CommonFunctions.getMaxCode("g_recruitment_block_eo", "id_recruitment_eo", con);
            pst = con.prepareStatement("INSERT INTO g_recruitment_block_eo(id_recruitment_eo,id_department,post_name,group_name,san_strength,pre_vacancy,pre_status,post_adv,filled_uppost,nobal_post,reason_rec,id_cader) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, mcode);
            pst.setString(2, deptId);
            pst.setString(3, empStrength.getPostName());
            pst.setString(4, empStrength.getGroupName());
            pst.setString(5, empStrength.getSanctionStrength());
            pst.setString(6, empStrength.getPreviousVacancy());
            pst.setString(7, empStrength.getPreviousStatus());
            pst.setString(8, empStrength.getPostAdv());
            pst.setString(9, empStrength.getFilledUp());
            pst.setString(10, empStrength.getNoBal());
            pst.setString(11, empStrength.getReasonRec());
            pst.setString(12, empStrength.getCadreCode());
            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public int updateEmpStrengthData(Bleo empStrength) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE  g_recruitment_block_eo SET post_name=?,group_name=?,san_strength=?,pre_vacancy=?,pre_status=?,post_adv=?,filled_uppost=?,nobal_post=?,reason_rec=?,id_cader=? WHERE id_recruitment_eo =?");
            pst.setString(1, empStrength.getPostName());
            pst.setString(2, empStrength.getGroupName());
            pst.setString(3, empStrength.getSanctionStrength());
            pst.setString(4, empStrength.getPreviousVacancy());
            pst.setString(5, empStrength.getPreviousStatus());
            pst.setString(6, empStrength.getPostAdv());
            pst.setString(7, empStrength.getFilledUp());
            pst.setString(8, empStrength.getNoBal());
            pst.setString(9, empStrength.getReasonRec());
            pst.setString(10, empStrength.getCadreCode());
            pst.setString(11, empStrength.getRecruitEoId());
            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public int deleteEmpStrength(String recruitEoId) {
        int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM g_recruitment_block_eo WHERE id_recruitment_eo =?");
            pst.setString(1, recruitEoId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public Bleo editEmpStrength(String recruitEoId) {
        Bleo empStrength = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT * FROM g_recruitment_block_eo WHERE id_recruitment_eo=?");
            pst.setString(1, recruitEoId);
            rs = pst.executeQuery();
            if (rs.next()) {
                empStrength = new Bleo();
                empStrength.setDeptId(rs.getString("id_department"));
                empStrength.setPostName(rs.getString("post_name"));
                empStrength.setGroupName(rs.getString("group_name"));
                empStrength.setSanctionStrength(rs.getString("san_strength"));
                empStrength.setPreviousVacancy(rs.getString("pre_vacancy"));
                empStrength.setPreviousStatus(rs.getString("pre_status"));
                empStrength.setPostAdv(rs.getString("post_adv"));
                empStrength.setFilledUp(rs.getString("filled_uppost"));
                empStrength.setNoBal(rs.getString("nobal_post"));
                empStrength.setNoBal(rs.getString("nobal_post"));
                empStrength.setReasonRec(rs.getString("reason_rec"));
                empStrength.setCadreCode(rs.getString("id_cader"));
                empStrength.setRecruitEoId(recruitEoId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empStrength;
    }

    @Override
    public List getBleoList(String deptId) {
        List list = null;
        Bleo bleo = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        list = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT a.*,b.post FROM g_recruitment_block_eo a ,g_post b  WHERE a.post_name=b.post_code AND  id_department='" + deptId + "' ORDER BY id_recruitment_eo DESC");
            rs = pst.executeQuery();
            while (rs.next()) {
                bleo = new Bleo();
                bleo.setDeptId(rs.getString("id_department"));
                bleo.setPostName(rs.getString("post"));
                bleo.setGroupName(rs.getString("group_name"));
                bleo.setSanctionStrength(rs.getString("san_strength"));
                bleo.setPreviousVacancy(rs.getString("pre_vacancy"));
                bleo.setPreviousStatus(rs.getString("pre_status"));
                bleo.setRecruitEoId(rs.getString("id_recruitment_eo"));
                list.add(bleo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public int saveTraining(Training empTraining) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            String mcode = CommonFunctions.getMaxCode("g_training", "id_traning", con);
            pst = con.prepareStatement("INSERT INTO g_training(id_traning,id_department,post_name,total_appointment,date_appointment,ind_emp_trained,ind_place_training,ind_duration,job_emp_trained,job_place_training,job_duration,total_emp_trained,remarks,id_cader,pinstitute,pfdate,ptdate,jinstitute,jfdate,jtdate,training_type,indtotal_emptrained,month,year) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, mcode);
            pst.setString(2, empTraining.getDeptId());
            pst.setString(3, empTraining.getPostCode());
            pst.setString(4, empTraining.getTotalAppointment());
            pst.setString(5, empTraining.getAppointmentDate());
            pst.setString(6, empTraining.getEmpIndvTrained());
            pst.setString(7, empTraining.getTrainingPlace());
            pst.setString(8, empTraining.getTrainingDuration());
            pst.setString(9, empTraining.getEmpJobTrained());
            pst.setString(10, empTraining.getJobPlaceTraining());
            pst.setString(11, empTraining.getJobDuration());
            pst.setString(12, empTraining.getTotalEmpTrained());
            pst.setString(13, empTraining.getRemarks());
            pst.setString(14, empTraining.getCadreCode());
            pst.setString(15, empTraining.getPinstitute());
            pst.setString(16, empTraining.getPfdate());
            pst.setString(17, empTraining.getPtdate());
            pst.setString(18, empTraining.getJinstitute());
            pst.setString(19, empTraining.getJfdate());
            pst.setString(20, empTraining.getJtdate());
            pst.setString(21, empTraining.getTrainingType());
            pst.setString(22, empTraining.getIndtotalEmpTrained());
            pst.setString(23, empTraining.getMonth());
            pst.setString(24, empTraining.getYear());
            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public int updateEmpTrainingData(Training empTraining) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("update g_training SET post_name=?,total_appointment=?,date_appointment=?,ind_emp_trained=?,ind_place_training=?,ind_duration=?,job_emp_trained=?,job_place_training=?,job_duration=?,total_emp_trained=?,remarks=?,id_cader=?,pinstitute=?,pfdate=?,ptdate=?,jinstitute=?,jfdate=?,jtdate=?,training_type=?,indtotal_emptrained=?,month=?,year=? where id_traning=?");
            pst.setString(1, empTraining.getPostCode());
            pst.setString(2, empTraining.getTotalAppointment());
            pst.setString(3, empTraining.getAppointmentDate());
            pst.setString(4, empTraining.getEmpIndvTrained());
            pst.setString(5, empTraining.getTrainingPlace());
            pst.setString(6, empTraining.getTrainingDuration());
            pst.setString(7, empTraining.getEmpJobTrained());
            pst.setString(8, empTraining.getJobPlaceTraining());
            pst.setString(9, empTraining.getJobDuration());
            pst.setString(10, empTraining.getTotalEmpTrained());
            pst.setString(11, empTraining.getRemarks());
            pst.setString(12, empTraining.getCadreCode());
            pst.setString(13, empTraining.getPinstitute());
            pst.setString(14, empTraining.getPfdate());
            pst.setString(15, empTraining.getPtdate());
            pst.setString(16, empTraining.getJinstitute());
            pst.setString(17, empTraining.getJfdate());
            pst.setString(18, empTraining.getJtdate());
            pst.setString(19, empTraining.getTrainingType());
            pst.setString(20, empTraining.getIndtotalEmpTrained());
            pst.setString(21, empTraining.getMonth());
            pst.setString(22, empTraining.getYear());
            pst.setString(23, empTraining.getTrainingId());
            n = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public int deleteEmpTraining(String trainingId) {
        int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM g_training WHERE id_traning =?");
            pst.setString(1, trainingId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public Training editEmpTraining(String trainingId) {
        Training empTraining = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT * FROM g_training WHERE id_traning=?");
            pst.setString(1, trainingId);
            rs = pst.executeQuery();
            if (rs.next()) {
                empTraining = new Training();
                empTraining.setDeptId(rs.getString("id_department"));
                empTraining.setPostName(rs.getString("post_name"));
                empTraining.setCadreCode(rs.getString("id_cader"));
                empTraining.setTotalAppointment(rs.getString("total_appointment"));
                empTraining.setAppointmentDate(rs.getString("date_appointment"));
                empTraining.setEmpIndvTrained(rs.getString("ind_emp_trained"));
                empTraining.setTrainingPlace(rs.getString("ind_place_training"));
                empTraining.setTrainingDuration(rs.getString("ind_duration"));
                empTraining.setEmpJobTrained(rs.getString("job_emp_trained"));
                empTraining.setJobPlaceTraining(rs.getString("job_place_training"));
                empTraining.setJobDuration(rs.getString("job_duration"));
                empTraining.setTotalEmpTrained(rs.getString("total_emp_trained"));
                empTraining.setRemarks(rs.getString("remarks"));
                empTraining.setPinstitute(rs.getString("pinstitute"));
                empTraining.setPfdate(rs.getString("pfdate"));
                empTraining.setPtdate(rs.getString("ptdate"));
                empTraining.setJinstitute(rs.getString("jinstitute"));
                empTraining.setJfdate(rs.getString("jfdate"));
                empTraining.setJtdate(rs.getString("jtdate"));
                empTraining.setTrainingType(rs.getString("training_type"));
                empTraining.setIndtotalEmpTrained(rs.getString("indtotal_emptrained"));
                empTraining.setTrainingId(rs.getString("id_traning"));
                empTraining.setMonth(rs.getString("month"));
                empTraining.setYear(rs.getString("year"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empTraining;
    }

    @Override
    public List getTrainingList(String deptId) {
        List list = new ArrayList();
        Training empTraining = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT a.*,b.post FROM g_training a,g_post b WHERE  a.post_name=b.post_code AND a.id_department='" + deptId + "' ORDER By id_traning DESC");

            rs = pst.executeQuery();
            while (rs.next()) {
                empTraining = new Training();
                empTraining.setDeptId(rs.getString("id_department"));
                empTraining.setPostName(rs.getString("post"));
                empTraining.setTotalAppointment(rs.getString("total_appointment"));
                empTraining.setAppointmentDate(rs.getString("date_appointment"));
                empTraining.setEmpIndvTrained(rs.getString("ind_emp_trained"));
                empTraining.setTrainingPlace(rs.getString("ind_place_training"));
                empTraining.setTrainingDuration(rs.getString("ind_duration"));
                empTraining.setEmpJobTrained(rs.getString("job_emp_trained"));
                empTraining.setJobPlaceTraining(rs.getString("job_place_training"));
                empTraining.setJobDuration(rs.getString("job_duration"));
                empTraining.setTotalEmpTrained(rs.getString("total_emp_trained"));
                empTraining.setRemarks(rs.getString("remarks"));
                empTraining.setTrainingId(rs.getString("id_traning"));
                empTraining.setMonth(rs.getString("month"));
                empTraining.setYear(rs.getString("year"));
                String cadername = rs.getString("id_cader");
                if (cadername.equals("0")) {
                    empTraining.setCadreCode("All");
                } else {
                    empTraining.setCadreCode(rs.getString("id_cader"));
                }
                list.add(empTraining);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public int saveDeptvacantpost(VacantPost vacantPost) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            String mcode = CommonFunctions.getMaxCode("g_vacant_post_details", "id_vacant_post", con);
            pst = con.prepareStatement("INSERT INTO g_vacant_post_details(id_vacant_post,id_cader,id_post,group_name,month,year,dept_id,sanpost,mainpost,vacancy,post_cleared,base_post,vac_retirment,post_filledup,last_month_base,current_month_base,total_filled_up,post_req_sent,balance_posts,sentreq,dept_post_comm,commission_name,prodept_post_balance,sanpost_pro,mainpost_pro,vacancy_pro,propost_cleared,probacklog,pro_retirment,postfilledup_pro,last_month_pro,current_month_pro,total_filled_up_pro,pro_balance_post,pro_req_sent) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, mcode);
            pst.setString(2, vacantPost.getCadreId());
            pst.setString(3, vacantPost.getPostId());
            pst.setString(4, vacantPost.getGroupName());
            pst.setString(5, vacantPost.getMonth());
            pst.setString(6, vacantPost.getYear());
            pst.setString(7, vacantPost.getDeptId());

            pst.setString(8, vacantPost.getSancPost());
            pst.setString(9, vacantPost.getManinPost());
            pst.setString(10, vacantPost.getVacancy());

            pst.setString(11, vacantPost.getPostCleared());
            pst.setString(12, vacantPost.getBaseLevelPost());
            pst.setString(13, vacantPost.getVacRetirment());

            pst.setString(14, vacantPost.getPostFilledUp());
            pst.setString(15, vacantPost.getLastmonthfilled());
            pst.setString(16, vacantPost.getCurrentmonthfilled());
            pst.setString(17, vacantPost.getTotalpostFilledUp());
            pst.setString(18, vacantPost.getPostRequisitionSent());
            pst.setString(19, vacantPost.getPostsBalance());

            pst.setString(20, vacantPost.getSentreq());
            pst.setString(21, vacantPost.getDeptPostComm());

            pst.setString(22, vacantPost.getCommissionName());

            pst.setString(23, vacantPost.getProDeptPostBalance());
            pst.setString(24, vacantPost.getSancPostPro());
            pst.setString(25, vacantPost.getManinPostPro());
            pst.setString(26, vacantPost.getVacancyPro());
            pst.setString(27, vacantPost.getProPostCleared());
            pst.setString(28, vacantPost.getProbacklog());
            pst.setString(29, vacantPost.getProRetirment());

            pst.setString(30, vacantPost.getPostFilledUppro());
            pst.setString(31, vacantPost.getLastMonthPro());
            pst.setString(32, vacantPost.getCurrentmonthfilledpro());
            pst.setString(33, vacantPost.getTotalpostFilledUppro());
            pst.setString(34, vacantPost.getProPostsBalance());
            pst.setString(35, vacantPost.getPropostRequisitionSent());

            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public int updateVacantPostData(VacantPost vacantPost) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            // pst = con.prepareStatement("UPDATE g_vacant_post_details set id_department=?,id_cader=?,id_post=?,group_name=?,post_cleared=?,base_level_post=?,post_filed_up=?,post_requisition_sent=?,balance_posts=?,post_dept_commissions=?,departmentally_bal_post=? where id_vacant_post=?");
            pst = con.prepareStatement("UPDATE g_vacant_post_details set id_cader=?,id_post=?,group_name=?,month=?,year=?,dept_id=?,sanpost=?,mainpost=?,vacancy=?,post_cleared=?,base_post=?,vac_retirment=?,post_filledup=?,last_month_base=?,current_month_base=?,total_filled_up=?,post_req_sent=?,balance_posts=?,sentreq=?,dept_post_comm=?,commission_name=?,prodept_post_balance=?,sanpost_pro=?,mainpost_pro=?,vacancy_pro=?,propost_cleared=?,probacklog=?,pro_retirment=?,postfilledup_pro=?,last_month_pro=?,current_month_pro=?,total_filled_up_pro=?,pro_balance_post=?,pro_req_sent=? where id_vacant_post=?");
            pst.setString(1, vacantPost.getCadreId());
            pst.setString(2, vacantPost.getPostId());
            pst.setString(3, vacantPost.getGroupName());
            pst.setString(4, vacantPost.getMonth());
            pst.setString(5, vacantPost.getYear());
            pst.setString(6, vacantPost.getDeptId());

            pst.setString(7, vacantPost.getSancPost());
            pst.setString(8, vacantPost.getManinPost());
            pst.setString(9, vacantPost.getVacancy());

            pst.setString(10, vacantPost.getPostCleared());
            pst.setString(11, vacantPost.getBaseLevelPost());
            pst.setString(12, vacantPost.getVacRetirment());

            pst.setString(13, vacantPost.getPostFilledUp());
            pst.setString(14, vacantPost.getLastmonthfilled());
            pst.setString(15, vacantPost.getCurrentmonthfilled());
            pst.setString(16, vacantPost.getTotalpostFilledUp());
            pst.setString(17, vacantPost.getPostRequisitionSent());
            pst.setString(18, vacantPost.getPostsBalance());

            pst.setString(19, vacantPost.getSentreq());
            pst.setString(20, vacantPost.getDeptPostComm());

            pst.setString(21, vacantPost.getCommissionName());

            pst.setString(22, vacantPost.getProDeptPostBalance());
            pst.setString(23, vacantPost.getSancPostPro());
            pst.setString(24, vacantPost.getManinPostPro());
            pst.setString(25, vacantPost.getVacancyPro());
            pst.setString(26, vacantPost.getProPostCleared());
            pst.setString(27, vacantPost.getProbacklog());
            pst.setString(28, vacantPost.getProRetirment());

            pst.setString(29, vacantPost.getPostFilledUppro());
            pst.setString(30, vacantPost.getLastMonthPro());
            pst.setString(31, vacantPost.getCurrentmonthfilledpro());
            pst.setString(32, vacantPost.getTotalpostFilledUppro());
            pst.setString(33, vacantPost.getProPostsBalance());
            pst.setString(34, vacantPost.getPropostRequisitionSent());
            pst.setString(35, vacantPost.getIdVacantPost());
            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public int deleteVacantPost(String vacantPostId) {
        int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM g_vacant_post_details WHERE id_vacant_post =?");
            pst.setString(1, vacantPostId);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public VacantPost editVacantPost(String vacantPostId) {
        VacantPost vacantPost = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select * from g_vacant_post_details where id_vacant_post=?");
            pst.setString(1, vacantPostId);
            rs = pst.executeQuery();
            if (rs.next()) {
                vacantPost = new VacantPost();
                vacantPost.setDeptId(rs.getString("dept_id"));
                vacantPost.setCadreId(rs.getString("id_cader"));
                vacantPost.setPostId(rs.getString("id_post"));
                vacantPost.setGroupName(rs.getString("group_name"));
                vacantPost.setIdVacantPost(rs.getString("id_vacant_post"));
                vacantPost.setMonth(rs.getString("month"));
                vacantPost.setYear(rs.getString("year"));
                vacantPost.setSancPost(rs.getString("sanpost"));
                vacantPost.setManinPost(rs.getString("mainpost"));
                vacantPost.setVacancy(rs.getString("vacancy"));
                vacantPost.setPostCleared(rs.getString("post_cleared"));
                vacantPost.setBaseLevelPost(rs.getString("base_post"));
                vacantPost.setVacRetirment(rs.getString("vac_retirment"));
                vacantPost.setPostFilledUp(rs.getString("post_filledup"));
                vacantPost.setLastmonthfilled(rs.getString("last_month_base"));
                vacantPost.setCurrentmonthfilled(rs.getString("current_month_base"));
                vacantPost.setTotalpostFilledUp(rs.getString("total_filled_up"));
                vacantPost.setPostRequisitionSent(rs.getString("post_req_sent"));
                vacantPost.setPostsBalance(rs.getString("balance_posts"));
                vacantPost.setSentreq(rs.getString("sentreq"));
                vacantPost.setDeptPostComm(rs.getString("dept_post_comm"));
                vacantPost.setCommissionName(rs.getString("commission_name"));
                vacantPost.setProDeptPostBalance(rs.getString("prodept_post_balance"));
                vacantPost.setSancPostPro(rs.getString("sanpost_pro"));
                vacantPost.setManinPostPro(rs.getString("mainpost_pro"));
                vacantPost.setVacancyPro(rs.getString("vacancy_pro"));
                vacantPost.setProPostCleared(rs.getString("propost_cleared"));
                vacantPost.setProbacklog(rs.getString("probacklog"));
                vacantPost.setProRetirment(rs.getString("pro_retirment"));
                vacantPost.setPostFilledUppro(rs.getString("postfilledup_pro"));
                vacantPost.setLastMonthPro(rs.getString("last_month_pro"));
                vacantPost.setCurrentmonthfilledpro(rs.getString("current_month_pro"));
                vacantPost.setTotalpostFilledUppro(rs.getString("total_filled_up_pro"));
                vacantPost.setProPostsBalance(rs.getString("pro_balance_post"));
                vacantPost.setPropostRequisitionSent(rs.getString("pro_req_sent"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return vacantPost;
    }

    @Override
    public List getVacantPost(String deptid, String month, String year) {
        List list = null;
        VacantPost vacantPost = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;

        try {
            list = new ArrayList();
            con = dataSource.getConnection();
            //pst = con.prepareStatement("select a.department_name,a.department_code,SUM(cast(b.post_cleared as integer)) as post_cleared,SUM(cast(b.total_filled_up as integer)) as total_filled_up,SUM(cast(b.base_post as integer)) as base_post,SUM(cast(b.vac_retirment as integer)) as vac_retirment,SUM(cast(b.post_filledup as integer)) as post_filledup,SUM(cast(b.balance_posts as integer)) as balance_posts,SUM(cast(b.pro_req_sent as integer)) as pro_req_sent,SUM(cast(b.dept_post_comm as integer)) as dept_post_comm,SUM(cast(b.prodept_post_balance as integer)) as prodept_post_balance FROM g_department a, g_vacant_post_details b WHERE  month = '" + month + "' AND year = '" + year + "' AND a.department_code = b.dept_id AND  a.if_active = 'Y' GROUP BY  a.department_name,department_code,a.department_code ORDER BY a.department_code ");
            pst = con.prepareStatement("select a.department_name,a.department_code,SUM(cast(COALESCE( NULLIF(post_cleared,'') , '0' ) as integer)) \n"
                    + "as post_cleared,SUM(cast(COALESCE( NULLIF(total_filled_up,'') , '0' ) as integer))  \n"
                    + "as total_filled_up,SUM(cast(COALESCE( NULLIF(base_post,'') , '0' ) as integer)) \n"
                    + "as base_post,SUM(cast(COALESCE( NULLIF(vac_retirment,'') , '0' ) as integer))  \n"
                    + "as vac_retirment,SUM(cast(COALESCE( NULLIF(post_filledup,'') , '0' ) as integer)) \n"
                    + " as post_filledup,SUM(cast(COALESCE( NULLIF(balance_posts,'') , '0' ) as integer))  \n"
                    + " as balance_posts,SUM(cast(COALESCE( NULLIF(sentreq,'') , '0' ) as integer))  \n"
                    + " as pro_req_sent,SUM(cast(COALESCE( NULLIF(dept_post_comm,'') , '0' ) as integer))  \n"
                    + " as dept_post_comm,SUM(cast(COALESCE( NULLIF(prodept_post_balance,'') , '0' ) as integer)) \n"
                    + " as prodept_post_balance FROM g_department a, \n"
                    + " g_vacant_post_details b WHERE  dept_id='" + deptid + "' AND month = '" + month + "' AND year = '" + year + "' AND a.department_code = b.dept_id \n"
                    + " AND  a.if_active = 'Y' GROUP BY  \n"
                    + " a.department_name,department_code ORDER BY a.department_code ");

            rs = pst.executeQuery();
            while (rs.next()) {
                vacantPost = new VacantPost();
                //vacantPost.setVacantPostId(rs.getString("id_vacant_post"));
                vacantPost.setDeptName(rs.getString("department_name"));
                vacantPost.setPostCleared(rs.getString("post_cleared"));
                vacantPost.setBaseLevelPost(rs.getString("base_post"));
                vacantPost.setVacRetirment(rs.getString("vac_retirment"));
                vacantPost.setPostFilledUp(rs.getString("post_filledup"));
                vacantPost.setPropostRequisitionSent(rs.getString("pro_req_sent"));
                vacantPost.setPostsBalance(rs.getString("balance_posts"));
                vacantPost.setDeptPostComm(rs.getString("dept_post_comm"));
                vacantPost.setProDeptPostBalance(rs.getString("prodept_post_balance"));
                vacantPost.setTotalpostFilledUp(rs.getString("total_filled_up"));
                vacantPost.setDeptId(rs.getString("department_code"));
                vacantPost.setMonth(month);
                vacantPost.setYear(year);

                list.add(vacantPost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public int saveRecruitDriveData(RecruitmentDrive recruitDrive) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            String mcode = CommonFunctions.getMaxCode("g_recruitment_drive", "id_recruitment_drive", con);
            pst = con.prepareStatement("INSERT INTO g_recruitment_drive(id_recruitment_drive,id_department,post_name,total_vacancy,req_sent_agency_status,sent_date,sent_letterno,post_req_sent,recruiting_agency,adv_details,exam_details,result_published,remarks,month,year,id_cader) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, mcode);
            pst.setString(2, recruitDrive.getDeptId());
            pst.setString(3, recruitDrive.getPostCode());
            pst.setString(4, recruitDrive.getTotalVacanct());
            pst.setString(5, recruitDrive.getReqSentAgentStatus());
            pst.setString(6, recruitDrive.getSentDate());
            pst.setString(7, recruitDrive.getSentLetterNo());
            pst.setString(8, recruitDrive.getPostReqSent());
            pst.setString(9, recruitDrive.getRecruitAgency());
            pst.setString(10, recruitDrive.getAdvDetails());
            pst.setString(11, recruitDrive.getExamDetails());
            pst.setString(12, recruitDrive.getResultPublished());
            pst.setString(13, recruitDrive.getRemarks());
            pst.setString(14, recruitDrive.getMonth());
            pst.setString(15, recruitDrive.getYear());
            pst.setString(16, recruitDrive.getCadreCode());

            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public int updateRecruitDriveData(RecruitmentDrive recruitDrive) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("update g_recruitment_drive set post_name=?,total_vacancy=?,req_sent_agency_status=?,sent_date=?,sent_letterno=?,post_req_sent=?,recruiting_agency=?,adv_details=?,exam_details=?,result_published=?,remarks=?,month=?,year=?,id_cader=? where id_recruitment_drive=?");
            pst.setString(1, recruitDrive.getPostCode());
            pst.setString(2, recruitDrive.getTotalVacanct());
            pst.setString(3, recruitDrive.getReqSentAgentStatus());
            pst.setString(4, recruitDrive.getSentDate());
            pst.setString(5, recruitDrive.getSentLetterNo());
            pst.setString(6, recruitDrive.getPostReqSent());
            pst.setString(7, recruitDrive.getRecruitAgency());
            pst.setString(8, recruitDrive.getAdvDetails());
            pst.setString(9, recruitDrive.getExamDetails());
            pst.setString(10, recruitDrive.getResultPublished());
            pst.setString(11, recruitDrive.getRemarks());
            pst.setString(12, recruitDrive.getMonth());
            pst.setString(13, recruitDrive.getYear());
            pst.setString(14, recruitDrive.getCadreCode());
            pst.setString(15, recruitDrive.getRecruitDriveId());
            n = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public int deleteRecruitDrive(String recruitDriveId) {
        int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM g_recruitment_drive WHERE id_recruitment_drive =?");
            pst.setString(1, recruitDriveId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public RecruitmentDrive editRecruitDrive(String recruitDriveId) {
        RecruitmentDrive recruitDrive = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select * from g_recruitment_drive where id_recruitment_drive=?");
            pst.setString(1, recruitDriveId);

            rs = pst.executeQuery();
            if (rs.next()) {
                recruitDrive = new RecruitmentDrive();
                recruitDrive.setDeptId(rs.getString("id_department"));
                recruitDrive.setPostName(rs.getString("post_name"));
                recruitDrive.setTotalVacanct(rs.getString("total_vacancy"));
                recruitDrive.setReqSentAgentStatus(rs.getString("req_sent_agency_status"));
                recruitDrive.setSentDate(rs.getString("sent_date"));
                recruitDrive.setSentLetterNo(rs.getString("sent_letterno"));
                recruitDrive.setPostReqSent(rs.getString("post_req_sent"));
                recruitDrive.setRecruitAgency(rs.getString("recruiting_agency"));
                recruitDrive.setAdvDetails(rs.getString("adv_details"));
                recruitDrive.setExamDetails(rs.getString("exam_details"));
                recruitDrive.setResultPublished(rs.getString("result_published"));
                recruitDrive.setRemarks(rs.getString("remarks"));
                recruitDrive.setRecruitDriveId(rs.getString("id_recruitment_drive"));
                recruitDrive.setMonth(rs.getString("month"));
                recruitDrive.setYear(rs.getString("year"));
                recruitDrive.setCadreCode(rs.getString("id_cader"));
                recruitDrive.setRecruitDriveId(rs.getString("id_recruitment_drive"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return recruitDrive;
    }

    @Override
    public List getRecruitDriveList(String deptid) {
        List list = new ArrayList();
        RecruitmentDrive recruitDrive = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select a.*,b.post from g_recruitment_drive a,g_post b  WHERE a.post_name=b.post_code AND a.id_department='" + deptid + "' ORDER BY id_recruitment_drive DESC");

            rs = pst.executeQuery();
            while (rs.next()) {
                recruitDrive = new RecruitmentDrive();
                recruitDrive.setDeptId(rs.getString("id_department"));
                recruitDrive.setPostName(rs.getString("post"));
                recruitDrive.setTotalVacanct(rs.getString("total_vacancy"));
                recruitDrive.setReqSentAgentStatus(rs.getString("req_sent_agency_status"));
                recruitDrive.setSentDate(rs.getString("sent_date"));
                recruitDrive.setSentLetterNo(rs.getString("sent_letterno"));
                recruitDrive.setPostReqSent(rs.getString("post_req_sent"));
                recruitDrive.setRecruitAgency(rs.getString("recruiting_agency"));
                recruitDrive.setAdvDetails(rs.getString("adv_details"));
                recruitDrive.setExamDetails(rs.getString("exam_details"));
                recruitDrive.setResultPublished(rs.getString("result_published"));
                recruitDrive.setRemarks(rs.getString("remarks"));
                recruitDrive.setRecruitDriveId(rs.getString("id_recruitment_drive"));
                String cadername = rs.getString("id_cader");
                String month_year = rs.getString("month") + ", " + rs.getString("year");
                if (cadername.equals("0")) {
                    recruitDrive.setCadreCode("All");
                } else {
                    recruitDrive.setCadreCode(rs.getString("id_cader"));
                }
                recruitDrive.setYear(month_year);

                list.add(recruitDrive);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    //@Override
    public List getDEInstituteList() {
        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;

        List instlist = new ArrayList();
        try {
            con = dataSource.getConnection();
            String sql = "SELECT institution_code,institution_name from g_institutions  order by institution_name";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                SelectOption se = new SelectOption();
                se.setValue(rs.getString("institution_code"));
                se.setLabel(rs.getString("institution_name"));
                instlist.add(se);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return instlist;
    }

    /**
     * ************** manoj ***********************
     */
    @Override
    public void saveCandidateCategory(SelectedCandidatesCategory scc, String empId) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO g_selected_candidate_categories(dept_code,group_name"
                    + ",cadre,post,no_of_candidates, order_number, order_date, owner_id) VALUES(?,?,?,?,?,?,?,?)");
            pst.setString(1, scc.getDepartmentId());
            pst.setString(2, scc.getGroupName());
            pst.setString(3, scc.getCadreId());
            pst.setString(4, scc.getPostId());
            pst.setInt(5, scc.getNoOfSelectedCanddiates());
            pst.setString(6, scc.getOrderNumber());
            pst.setString(7, scc.getOrderDate());
            pst.setString(8, empId);
            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List selectedCandiateCategoryList(String empId) {
        List list = new ArrayList();
        SelectedCandidatesCategory scc = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT GC.*, department_name, cadre_name, GP.post AS post_name FROM g_selected_candidate_categories GC LEFT OUTER JOIN g_department GD ON GC.dept_code = GD.department_code\n"
                    + "LEFT OUTER JOIN g_cadre GCD ON GCD.cadre_code = GC.cadre\n"
                    + "LEFT OUTER JOIN g_post GP ON GP.post_code = GC.post WHERE GC.owner_id = '" + empId + "' ORDER BY category_id DESC");
            rs = pst.executeQuery();

            while (rs.next()) {

                scc = new SelectedCandidatesCategory();

                if (rs.getString("cadre").equals("0")) {
                    scc.setCadreId("All");
                } else {
                    scc.setCadreId(rs.getString("cadre_name"));
                }
                scc.setDepartmentId(rs.getString("department_name"));
                scc.setCategoryId(rs.getInt("category_id"));
                scc.setGroupName(rs.getString("group_name"));
                scc.setPostId(rs.getString("post_name"));
                scc.setNoOfSelectedCanddiates(rs.getInt("no_of_candidates"));
                scc.setOrderDate(rs.getString("order_date"));
                scc.setOrderNumber(rs.getString("order_number"));
                list.add(scc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public void saveSelectedCandidates(SelectedCandidates sc) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO g_selected_candidates(candidate_name,roll_no"
                    + ",gender,category, category_id) VALUES(?,?,?,?,?)");
            pst.setString(1, sc.getCandidateName());
            pst.setString(2, sc.getRollNumber());
            pst.setString(3, sc.getGender());
            pst.setString(4, sc.getCategory());
            pst.setInt(5, sc.getCategoryId());
            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public List selectedCandiatesList(String categoryId) {
        List list = new ArrayList();
        SelectedCandidates scc = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT * FROM g_selected_candidates"
                    + " WHERE category_id = " + categoryId + " ORDER BY candidate_id DESC");
            rs = pst.executeQuery();

            while (rs.next()) {

                scc = new SelectedCandidates();

                scc.setCandidateName(rs.getString("candidate_name"));
                scc.setRollNumber(rs.getString("roll_no"));
                scc.setGender(rs.getString("gender"));
                scc.setCategory(rs.getString("category"));
                scc.setCandidateId(rs.getInt("candidate_id"));
                scc.setCategoryId(rs.getInt("category_id"));
                list.add(scc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public void deleteCandidate(int candidateId, int categoryId) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM g_selected_candidates WHERE candidate_id = ?");
            pst.setInt(1, candidateId);
            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public SelectedCandidatesCategory getcCategoryDetail(String categoryId) {
        SelectedCandidatesCategory scc = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT GC.*, department_name, cadre_name, GP.post AS post_name FROM g_selected_candidate_categories GC LEFT OUTER JOIN g_department GD ON GC.dept_code = GD.department_code\n"
                    + "LEFT OUTER JOIN g_cadre GCD ON GCD.cadre_code = GC.cadre\n"
                    + "LEFT OUTER JOIN g_post GP ON GP.post_code = GC.post WHERE GC.category_id = ?");
            pst.setInt(1, Integer.parseInt(categoryId));
            rs = pst.executeQuery();

            while (rs.next()) {
                scc = new SelectedCandidatesCategory();

                if (rs.getString("cadre").equals("0")) {
                    scc.setCadreId("All");
                } else {
                    scc.setCadreId(rs.getString("cadre_name"));
                }
                scc.setDepartmentId(rs.getString("department_name"));
                scc.setCategoryId(rs.getInt("category_id"));
                scc.setGroupName(rs.getString("group_name"));
                scc.setPostId(rs.getString("post_name"));
                scc.setNoOfSelectedCanddiates(rs.getInt("no_of_candidates"));
                scc.setOrderDate(rs.getString("order_date"));
                scc.setOrderNumber(rs.getString("order_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return scc;
    }

    /**
     * ************************************************* Commission Reports
     * **************
     */
    @Override
    public void saveCommissionPending(CommissionPending cp, String empId) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO g_commission_pending_req(dept_id,post_name"
                    + ",cadre,group_name,noof_van,req_date, adv_no, adv_date, exam_date,written_date,vivavoce_date,final_result_date,sponsoring_date,coursecase_details,remarks,year,month,empid) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, cp.getDepartmentId());
            pst.setString(2, cp.getPostId());
            pst.setString(3, cp.getCadreId());
            pst.setString(4, cp.getGroupName());
            pst.setString(5, cp.getNoofVan());
            pst.setString(6, cp.getReqDate());
            pst.setString(7, cp.getAdvNo());
            pst.setString(8, cp.getAdvDate());
            pst.setString(9, cp.getExamdate());
            pst.setString(10, cp.getWrittendate());
            pst.setString(11, cp.getVivavoceDate());
            pst.setString(12, cp.getFinalResultDate());
            pst.setString(13, cp.getSponsoringDate());
            pst.setString(14, cp.getCoursecaseDetails());
            pst.setString(15, cp.getRemarks());
            pst.setString(16, cp.getYear());
            pst.setString(17, cp.getMonth());

            pst.setString(18, empId);
            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List commissionPendingList(String empId) {
        List list = new ArrayList();
        CommissionPending cpl = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        String vanno = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT GC.*, department_name, cadre_name, GP.post FROM g_commission_pending_req GC LEFT OUTER JOIN g_department GD ON GC.dept_id = GD.department_code\n"
                    + "LEFT OUTER JOIN g_cadre GCD ON GCD.cadre_code = GC.cadre\n"
                    + "LEFT OUTER JOIN g_post GP ON GP.post_code = GC.post_name WHERE GC.empid = '" + empId + "' ORDER BY pending_id DESC");

            rs = pst.executeQuery();

            while (rs.next()) {

                cpl = new CommissionPending();

                if (rs.getString("cadre").equals("0")) {
                    cpl.setCadreId("All");
                } else {
                    cpl.setCadreId(rs.getString("cadre_name"));
                }
                //  vanno=rs.getString("noof_van");
                cpl.setDepartmentId(rs.getString("department_name"));
                cpl.setPostId(rs.getString("post"));
                cpl.setPendingId(rs.getString("pending_id"));
                cpl.setGroupName(rs.getString("group_name"));
                cpl.setNoofVan(rs.getString("noof_van"));
                cpl.setReqDate(rs.getString("req_date"));
                cpl.setAdvNo(rs.getString("adv_no"));
                cpl.setAdvDate(rs.getString("adv_date"));
                cpl.setExamdate(rs.getString("exam_date"));
                cpl.setWrittendate(rs.getString("written_date"));
                cpl.setVivavoceDate(rs.getString("vivavoce_date"));
                cpl.setFinalResultDate(rs.getString("final_result_date"));
                cpl.setSponsoringDate(rs.getString("sponsoring_date"));
                cpl.setCoursecaseDetails(rs.getString("coursecase_details"));
                cpl.setRemarks(rs.getString("remarks"));
                cpl.setYear(rs.getString("year"));
                cpl.setMonth(rs.getString("month"));
                cpl.setDeptId(rs.getString("dept_id"));

                list.add(cpl);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public void saveCommissionCourtcaseDetails(CommissionCourtCase cp, String empId) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO g_commission_court_cases(dept_id,post_name"
                    + ",cadre,group_name,remarks,year,month,empid,courtcase_no,courtcase_date,pwc_date,affedevit_date,interim_order,judgement_date,order_passed,steps_stay,stay_date,final_result_date,sponsoring_date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, cp.getDepartmentId());
            pst.setString(2, cp.getPostId());
            pst.setString(3, cp.getCadreId());
            pst.setString(4, cp.getGroupName());
            pst.setString(5, cp.getRemarks());
            pst.setString(6, cp.getYear());
            pst.setString(7, cp.getMonth());
            pst.setString(8, empId);

            pst.setString(9, cp.getCourtCaseNo());
            pst.setString(10, cp.getCourtCaseDate());
            pst.setString(11, cp.getPwcDate());
            pst.setString(12, cp.getAffedevitdate());
            pst.setString(13, cp.getInterimorder());
            pst.setString(14, cp.getJudgementDate());
            pst.setString(15, cp.getOrderPassed());
            pst.setString(16, cp.getStepsStay());
            pst.setString(17, cp.getStayDate());
            pst.setString(18, cp.getFinalResultDate());
            pst.setString(19, cp.getSponsoringDate());

            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List commissioncourtcaseListDetails(String empId) {

        List list = new ArrayList();
        CommissionCourtCase cpl = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        String vanno = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT GC.*, department_name, cadre_name, GP.post FROM g_commission_court_cases GC LEFT OUTER JOIN g_department GD ON GC.dept_id = GD.department_code\n"
                    + "LEFT OUTER JOIN g_cadre GCD ON GCD.cadre_code = GC.cadre\n"
                    + "LEFT OUTER JOIN g_post GP ON GP.post_code = GC.post_name WHERE GC.empid = '" + empId + "' ORDER BY court_case_id DESC");

            rs = pst.executeQuery();

            while (rs.next()) {

                cpl = new CommissionCourtCase();

                if (rs.getString("cadre").equals("0")) {
                    cpl.setCadreId("All");
                } else {
                    cpl.setCadreId(rs.getString("cadre_name"));
                }
                //  vanno=rs.getString("noof_van");
                cpl.setDepartmentId(rs.getString("department_name"));
                cpl.setPostId(rs.getString("post"));
                cpl.setCourtCaseId(rs.getString("court_case_id"));
                cpl.setGroupName(rs.getString("group_name"));
                cpl.setRemarks(rs.getString("remarks"));
                cpl.setYear(rs.getString("year"));
                cpl.setMonth(rs.getString("month"));
                cpl.setCourtCaseNo(rs.getString("courtcase_no"));
                cpl.setCourtCaseDate(rs.getString("courtcase_date"));
                cpl.setPwcDate(rs.getString("pwc_date"));
                cpl.setAffedevitdate(rs.getString("affedevit_date"));
                cpl.setInterimorder(rs.getString("interim_order"));
                cpl.setJudgementDate(rs.getString("judgement_date"));
                cpl.setOrderPassed(rs.getString("order_passed"));
                cpl.setStepsStay(rs.getString("steps_stay"));
                cpl.setStayDate(rs.getString("stay_date"));
                cpl.setFinalResultDate(rs.getString("final_result_date"));
                cpl.setSponsoringDate(rs.getString("sponsoring_date"));
                cpl.setDeptId(rs.getString("dept_id"));

                list.add(cpl);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;

    }

    public CommissionPending editcommissionpending(String taskid) {
        CommissionPending cp = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT a.*,b.post FROM g_commission_pending_req a,g_post b  WHERE a.post_name=b.post_code AND pending_id=?");
            pst.setInt(1, Integer.parseInt(taskid));
            rs = pst.executeQuery();
            if (rs.next()) {
                cp = new CommissionPending();

                cp.setDepartmentId(rs.getString("dept_id"));
                cp.setPostId(rs.getString("post_name"));
                cp.setCadreId(rs.getString("cadre"));
                cp.setGroupName(rs.getString("group_name"));
                cp.setNoofVan(rs.getString("noof_van"));
                cp.setReqDate(rs.getString("req_date"));
                cp.setAdvNo(rs.getString("adv_no"));
                cp.setAdvDate(rs.getString("adv_date"));
                cp.setExamdate(rs.getString("exam_date"));
                cp.setWrittendate(rs.getString("written_date"));
                cp.setVivavoceDate(rs.getString("vivavoce_date"));
                cp.setFinalResultDate(rs.getString("final_result_date"));
                cp.setSponsoringDate(rs.getString("sponsoring_date"));
                cp.setCoursecaseDetails(rs.getString("coursecase_details"));
                cp.setRemarks(rs.getString("remarks"));
                cp.setYear(rs.getString("year"));
                cp.setMonth(rs.getString("month"));
                cp.setPendingId(taskid);
                cp.setPostName(rs.getString("post"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return cp;
    }

    @Override
    public void updateCommissionPending(CommissionPending cp, String empId) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("update g_commission_pending_req set dept_id=?,post_name=?,cadre=?,group_name=?,noof_van=?,req_date=?,adv_no=?,adv_date=?,exam_date=?,written_date=?,vivavoce_date=?,final_result_date=?,sponsoring_date=?,coursecase_details=?,remarks=?,year=?,month=? where empid=? AND pending_id=?");
            pst.setString(1, cp.getDepartmentId());
            pst.setString(2, cp.getPostId());
            pst.setString(3, cp.getCadreId());
            pst.setString(4, cp.getGroupName());
            pst.setString(5, cp.getNoofVan());
            pst.setString(6, cp.getReqDate());
            pst.setString(7, cp.getAdvNo());
            pst.setString(8, cp.getAdvDate());
            pst.setString(9, cp.getExamdate());
            pst.setString(10, cp.getWrittendate());
            pst.setString(11, cp.getVivavoceDate());
            pst.setString(12, cp.getFinalResultDate());
            pst.setString(13, cp.getSponsoringDate());
            pst.setString(14, cp.getCoursecaseDetails());
            pst.setString(15, cp.getRemarks());
            pst.setString(16, cp.getYear());
            pst.setString(17, cp.getMonth());
            pst.setString(18, empId);
            pst.setInt(19, Integer.parseInt(cp.getPendingId()));

            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public CommissionCourtCase editCommissionCourtCase(String taskid) {
        CommissionCourtCase cc = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT a.*,b.post FROM g_commission_court_cases a,g_post b  WHERE a.post_name=b.post_code AND court_case_id=?");
            pst.setInt(1, Integer.parseInt(taskid));
            rs = pst.executeQuery();
            if (rs.next()) {
                cc = new CommissionCourtCase();

                cc.setDepartmentId(rs.getString("dept_id"));
                cc.setPostId(rs.getString("post_name"));
                cc.setCadreId(rs.getString("cadre"));
                cc.setGroupName(rs.getString("group_name"));
                cc.setRemarks(rs.getString("remarks"));
                cc.setYear(rs.getString("year"));
                cc.setMonth(rs.getString("month"));
                cc.setCourtCaseNo(rs.getString("courtcase_no"));
                cc.setCourtCaseDate(rs.getString("courtcase_date"));
                cc.setPwcDate(rs.getString("pwc_date"));
                cc.setAffedevitdate(rs.getString("affedevit_date"));
                cc.setInterimorder(rs.getString("interim_order"));
                cc.setJudgementDate(rs.getString("judgement_date"));
                cc.setOrderPassed(rs.getString("order_passed"));
                cc.setStepsStay(rs.getString("steps_stay"));
                cc.setStayDate(rs.getString("stay_date"));
                cc.setFinalResultDate(rs.getString("final_result_date"));
                cc.setSponsoringDate(rs.getString("sponsoring_date"));
                cc.setCourtCaseId(taskid);
                cc.setPostName(rs.getString("post"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return cc;
    }

    @Override
    public void updateCommissionCourtCase(CommissionCourtCase cp, String empId) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("update g_commission_court_cases set dept_id=?,post_name=?,cadre=?,group_name=?,remarks=?,year=?,month=?,courtcase_no=?,courtcase_date=?,pwc_date=?,affedevit_date=?,interim_order=?,judgement_date=?,order_passed=?,steps_stay=?,stay_date=?,final_result_date=?,sponsoring_date=? where empid=? AND court_case_id=?");

            pst.setString(1, cp.getDepartmentId());
            pst.setString(2, cp.getPostId());
            pst.setString(3, cp.getCadreId());
            pst.setString(4, cp.getGroupName());

            pst.setString(5, cp.getRemarks());
            pst.setString(6, cp.getYear());
            pst.setString(7, cp.getMonth());

            pst.setString(8, cp.getCourtCaseNo());
            pst.setString(9, cp.getCourtCaseDate());
            pst.setString(10, cp.getPwcDate());
            pst.setString(11, cp.getAffedevitdate());
            pst.setString(12, cp.getInterimorder());
            pst.setString(13, cp.getJudgementDate());
            pst.setString(14, cp.getOrderPassed());
            pst.setString(15, cp.getStepsStay());
            pst.setString(16, cp.getStayDate());
            pst.setString(17, cp.getFinalResultDate());
            pst.setString(18, cp.getSponsoringDate());

            pst.setString(19, empId);
            pst.setInt(20, Integer.parseInt(cp.getCourtCaseId()));

            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    /**
     * **************************** MIS **********************************
     */
    @Override
    public List getmisVacantPost(String month, String year) {
        List list = null;
        VacantPost vacantPost = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;

        try {
            list = new ArrayList();
            con = dataSource.getConnection();
            pst = con.prepareStatement("select a.department_name, (SELECT COUNT(*) FROM g_vacant_post_details WHERE month = '" + month + "' AND year = '" + year + "' AND a.department_code = dept_id) AS num_entries from g_department a WHERE a.if_active = 'Y' AND a.department_code <> '50' ");

            rs = pst.executeQuery();
            while (rs.next()) {
                vacantPost = new VacantPost();
                vacantPost.setVacantPostId(rs.getString("num_entries"));
                vacantPost.setDeptName(rs.getString("department_name"));

                list.add(vacantPost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public List getmisRAScheme(String month, String year) {
        List list = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Rascheme appointStatus = null;
        list = new ArrayList();

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select a.department_name, (SELECT COUNT(*) FROM g_ra_scheme WHERE month = '" + month + "' AND year = '" + year + "' AND a.department_code = id_department) AS num_entries from g_department a WHERE a.if_active = 'Y' AND a.department_code <> '50' ");
            rs = pst.executeQuery();
            while (rs.next()) {
                appointStatus = new Rascheme();
                appointStatus.setRaSchemeId(rs.getString("num_entries"));
                appointStatus.setDeptName(rs.getString("department_name"));

                list.add(appointStatus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public List getmisScStRecruitment(String month, String year) {
        List list = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ScStRecruitment appoint = null;
        list = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select a.department_name, (SELECT COUNT(*) FROM g_sc_st_recruitment WHERE month = '" + month + "' AND year = '" + year + "' AND a.department_code = id_department) AS num_entries from g_department a WHERE a.if_active = 'Y' AND a.department_code <> '50' ");
            rs = pst.executeQuery();
            while (rs.next()) {
                appoint = new ScStRecruitment();
                appoint.setIdRecruitment(rs.getString("num_entries"));
                appoint.setDeptName(rs.getString("department_name"));
                list.add(appoint);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public List misRecruitDriveList(String month, String year) {
        List list = new ArrayList();
        RecruitmentDrive recruitDrive = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select a.department_name, (SELECT COUNT(*) FROM g_recruitment_drive WHERE month = '" + month + "' AND year = '" + year + "' AND a.department_code = id_department) AS num_entries from g_department a WHERE a.if_active = 'Y' AND a.department_code <> '50' ");

            rs = pst.executeQuery();
            while (rs.next()) {
                recruitDrive = new RecruitmentDrive();
                recruitDrive.setRecruitDriveId(rs.getString("num_entries"));
                recruitDrive.setDeptName(rs.getString("department_name"));

                list.add(recruitDrive);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public List misTrainingList(String month, String year) {
        List list = new ArrayList();
        Training empTraining = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select a.department_name, (SELECT COUNT(*) FROM g_training WHERE month = '" + month + "' AND year = '" + year + "' AND a.department_code = id_department) AS num_entries from g_department a WHERE a.if_active = 'Y' ");
            rs = pst.executeQuery();
            while (rs.next()) {
                empTraining = new Training();
                empTraining.setTrainingId(rs.getString("num_entries"));
                empTraining.setDeptName(rs.getString("department_name"));

                list.add(empTraining);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public List misBleoList(String deptId) {
        List list = null;
        Bleo bleo = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        list = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select a.department_name, (SELECT COUNT(*) FROM g_recruitment_block_eo WHERE a.department_code = id_department) AS num_entries from g_department a WHERE a.if_active = 'Y' ");
            rs = pst.executeQuery();
            while (rs.next()) {
                bleo = new Bleo();
                bleo.setRecruitEoId(rs.getString("num_entries"));
                bleo.setDeptId(rs.getString("department_name"));
                list.add(bleo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public List miscommissionPendingList(String month, String year) {
        List list = new ArrayList();
        CommissionPending cpl = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        String vanno = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT GC.*, department_name, cadre_name, GP.post FROM g_commission_pending_req GC LEFT OUTER JOIN g_department GD ON GC.dept_id = GD.department_code\n"
                    + "LEFT OUTER JOIN g_cadre GCD ON GCD.cadre_code = GC.cadre\n"
                    + "LEFT OUTER JOIN g_post GP ON GP.post_code = GC.post_name WHERE GC.month='" + month + "' AND GC.year='" + year + "'  ORDER BY empid");

            rs = pst.executeQuery();

            while (rs.next()) {

                cpl = new CommissionPending();

                if (rs.getString("cadre").equals("0")) {
                    cpl.setCadreId("All");
                } else {
                    cpl.setCadreId(rs.getString("cadre_name"));
                }
                //  vanno=rs.getString("noof_van");
                cpl.setDepartmentId(rs.getString("department_name"));
                cpl.setPostId(rs.getString("post"));
                cpl.setPendingId(rs.getString("pending_id"));
                cpl.setGroupName(rs.getString("group_name"));
                cpl.setNoofVan(rs.getString("noof_van"));
                cpl.setReqDate(rs.getString("req_date"));
                cpl.setAdvNo(rs.getString("adv_no"));
                cpl.setAdvDate(rs.getString("adv_date"));
                cpl.setExamdate(rs.getString("exam_date"));
                cpl.setWrittendate(rs.getString("written_date"));
                cpl.setVivavoceDate(rs.getString("vivavoce_date"));
                cpl.setFinalResultDate(rs.getString("final_result_date"));
                cpl.setSponsoringDate(rs.getString("sponsoring_date"));
                cpl.setCoursecaseDetails(rs.getString("coursecase_details"));
                cpl.setRemarks(rs.getString("remarks"));
                cpl.setYear(rs.getString("year"));
                cpl.setMonth(rs.getString("month"));
                cpl.setDeptId(rs.getString("dept_id"));

                list.add(cpl);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public List miscommissioncourtcaseListDetails(String month, String year) {

        List list = new ArrayList();
        CommissionCourtCase cpl = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        String vanno = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT GC.*, department_name, cadre_name, GP.post FROM g_commission_court_cases GC LEFT OUTER JOIN g_department GD ON GC.dept_id = GD.department_code\n"
                    + "LEFT OUTER JOIN g_cadre GCD ON GCD.cadre_code = GC.cadre\n"
                    + "LEFT OUTER JOIN g_post GP ON GP.post_code = GC.post_name WHERE GC.month='" + month + "' AND GC.year='" + year + "'  ORDER BY empId");

            rs = pst.executeQuery();

            while (rs.next()) {

                cpl = new CommissionCourtCase();

                if (rs.getString("cadre").equals("0")) {
                    cpl.setCadreId("All");
                } else {
                    cpl.setCadreId(rs.getString("cadre_name"));
                }
                //  vanno=rs.getString("noof_van");
                cpl.setDepartmentId(rs.getString("department_name"));
                cpl.setPostId(rs.getString("post"));
                cpl.setCourtCaseId(rs.getString("court_case_id"));
                cpl.setGroupName(rs.getString("group_name"));
                cpl.setRemarks(rs.getString("remarks"));
                cpl.setYear(rs.getString("year"));
                cpl.setMonth(rs.getString("month"));
                cpl.setCourtCaseNo(rs.getString("courtcase_no"));
                cpl.setCourtCaseDate(rs.getString("courtcase_date"));
                cpl.setPwcDate(rs.getString("pwc_date"));
                cpl.setAffedevitdate(rs.getString("affedevit_date"));
                cpl.setInterimorder(rs.getString("interim_order"));
                cpl.setJudgementDate(rs.getString("judgement_date"));
                cpl.setOrderPassed(rs.getString("order_passed"));
                cpl.setStepsStay(rs.getString("steps_stay"));
                cpl.setStayDate(rs.getString("stay_date"));
                cpl.setFinalResultDate(rs.getString("final_result_date"));
                cpl.setSponsoringDate(rs.getString("sponsoring_date"));
                cpl.setDeptId(rs.getString("dept_id"));

                list.add(cpl);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;

    }

    @Override
    public List summarygetmisRAScheme(String month, String year) {
        List list = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Rascheme appointStatus = null;
        list = new ArrayList();

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select a.department_name,b.* FROM g_department a, g_ra_scheme b WHERE  month = '" + month + "' AND year = '" + year + "' AND a.department_code = b.id_department AND  a.if_active = 'Y' ORDER BY a.department_code ");

            rs = pst.executeQuery();
            while (rs.next()) {
                appointStatus = new Rascheme();

                appointStatus.setDeptName(rs.getString("department_name"));
                appointStatus.setDeptId(rs.getString("id_department"));
                appointStatus.setTotalCase(rs.getString("total_case"));
                appointStatus.setTotalAppointment(rs.getString("total_appointment"));
                appointStatus.setTotalRejected(rs.getString("total_rejected"));
                appointStatus.setTotalCleared(rs.getString("total_cleared"));
                appointStatus.setTotalPending(rs.getString("total_pending"));
                appointStatus.setRemarks(rs.getString("remarks"));
                appointStatus.setMonth(rs.getString("month"));
                appointStatus.setYear(rs.getString("year"));
                appointStatus.setRaSchemeId(rs.getString("id_ra_scheme"));
                appointStatus.setTotalAppoLastmonth(rs.getString("total_app_lastmonth"));
                appointStatus.setTotalRejLastmonth(rs.getString("total_rej_lastmonth"));

                list.add(appointStatus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public List summarygetmisVacantPost(String month, String year) {
        List list = null;
        VacantPost vacantPost = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;

        try {
            list = new ArrayList();
            con = dataSource.getConnection();
            //pst = con.prepareStatement("select a.department_name,a.department_code,SUM(cast(b.post_cleared as integer)) as post_cleared,SUM(cast(b.total_filled_up as integer)) as total_filled_up,SUM(cast(b.base_post as integer)) as base_post,SUM(cast(b.vac_retirment as integer)) as vac_retirment,SUM(cast(b.post_filledup as integer)) as post_filledup,SUM(cast(b.balance_posts as integer)) as balance_posts,SUM(cast(b.pro_req_sent as integer)) as pro_req_sent,SUM(cast(b.dept_post_comm as integer)) as dept_post_comm,SUM(cast(b.prodept_post_balance as integer)) as prodept_post_balance FROM g_department a, g_vacant_post_details b WHERE  month = '" + month + "' AND year = '" + year + "' AND a.department_code = b.dept_id AND  a.if_active = 'Y' GROUP BY  a.department_name,department_code,a.department_code ORDER BY a.department_code ");
            pst = con.prepareStatement("select a.department_name,a.department_code,SUM(cast(COALESCE( NULLIF(post_cleared,'') , '0' ) as integer)) \n"
                    + "as post_cleared,SUM(cast(COALESCE( NULLIF(total_filled_up,'') , '0' ) as integer))  \n"
                    + "as total_filled_up,SUM(cast(COALESCE( NULLIF(base_post,'') , '0' ) as integer)) \n"
                    + "as base_post,SUM(cast(COALESCE( NULLIF(vac_retirment,'') , '0' ) as integer))  \n"
                    + "as vac_retirment,SUM(cast(COALESCE( NULLIF(post_filledup,'') , '0' ) as integer)) \n"
                    + " as post_filledup,SUM(cast(COALESCE( NULLIF(balance_posts,'') , '0' ) as integer))  \n"
                    + " as balance_posts,SUM(cast(COALESCE( NULLIF(sentreq,'') , '0' ) as integer))  \n"
                    + " as pro_req_sent,SUM(cast(COALESCE( NULLIF(dept_post_comm,'') , '0' ) as integer))  \n"
                    + " as dept_post_comm,SUM(cast(COALESCE( NULLIF(prodept_post_balance,'') , '0' ) as integer)) \n"
                    + " as prodept_post_balance FROM g_department a, \n"
                    + " g_vacant_post_details b WHERE  month = '" + month + "' AND year = '" + year + "' AND a.department_code = b.dept_id \n"
                    + " AND  a.if_active = 'Y'  GROUP BY  \n"
                    + " a.department_name,department_code ORDER BY a.department_code ");

            rs = pst.executeQuery();
            while (rs.next()) {
                vacantPost = new VacantPost();
                vacantPost.setDeptName(rs.getString("department_name"));
                vacantPost.setPostCleared(rs.getString("post_cleared"));
                vacantPost.setBaseLevelPost(rs.getString("base_post"));
                vacantPost.setVacRetirment(rs.getString("vac_retirment"));
                vacantPost.setPostFilledUp(rs.getString("post_filledup"));
                vacantPost.setPropostRequisitionSent(rs.getString("pro_req_sent"));
                vacantPost.setPostsBalance(rs.getString("balance_posts"));
                vacantPost.setDeptPostComm(rs.getString("dept_post_comm"));
                vacantPost.setProDeptPostBalance(rs.getString("prodept_post_balance"));
                vacantPost.setTotalpostFilledUp(rs.getString("total_filled_up"));
                vacantPost.setDeptId(rs.getString("department_code"));
                vacantPost.setMonth(month);
                vacantPost.setYear(year);

                list.add(vacantPost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public List summaryvacantgroupwise(String month, String year, String deptid) {
        List list = null;
        VacantPost vacantPost = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;

        try {
            list = new ArrayList();
            con = dataSource.getConnection();
            //pst = con.prepareStatement("select b.group_name,SUM(cast(b.post_cleared as integer)) as post_cleared,SUM(cast(b.total_filled_up as integer)) as total_filled_up,SUM(cast(b.base_post as integer)) as base_post,SUM(cast(b.vac_retirment as integer)) as vac_retirment,SUM(cast(b.post_filledup as integer)) as post_filledup,SUM(cast(b.balance_posts as integer)) as balance_posts,SUM(cast(b.pro_req_sent as integer)) as pro_req_sent,SUM(cast(b.dept_post_comm as integer)) as dept_post_comm,SUM(cast(b.prodept_post_balance as integer)) as prodept_post_balance FROM g_department a, g_vacant_post_details b WHERE  month = '" + month + "' AND year = '" + year + "' AND a.department_code = b.dept_id AND  a.if_active = 'Y' GROUP BY  a.department_name,department_code,a.department_code ORDER BY a.department_code ");
            pst = con.prepareStatement("select a.department_name,b.group_name,SUM(cast(COALESCE( NULLIF(post_cleared,'') , '0' ) as integer)) \n"
                    + "as post_cleared,SUM(cast(COALESCE( NULLIF(total_filled_up,'') , '0' ) as integer))  \n"
                    + "as total_filled_up,SUM(cast(COALESCE( NULLIF(base_post,'') , '0' ) as integer)) \n"
                    + "as base_post,SUM(cast(COALESCE( NULLIF(vac_retirment,'') , '0' ) as integer))  \n"
                    + "as vac_retirment,SUM(cast(COALESCE( NULLIF(post_filledup,'') , '0' ) as integer)) \n"
                    + " as post_filledup,SUM(cast(COALESCE( NULLIF(balance_posts,'') , '0' ) as integer))  \n"
                    + " as balance_posts,SUM(cast(COALESCE( NULLIF(mainpost,'') , '0' ) as integer)) as mainpost,SUM(cast(COALESCE( NULLIF(sentreq,'') , '0' ) as integer))  \n"
                    + " as pro_req_sent,SUM(cast(COALESCE( NULLIF(sanpost,'') , '0' ) as integer)) as sanpost,SUM(cast(COALESCE( NULLIF(dept_post_comm,'') , '0' ) as integer))  \n"
                    + " as dept_post_comm,SUM(cast(COALESCE( NULLIF(prodept_post_balance,'') , '0' ) as integer)) \n"
                    + " as prodept_post_balance FROM g_vacant_post_details b,g_department a \n"
                    + " WHERE  month = '" + month + "' AND year = '" + year + "' AND a.department_code = b.dept_id AND  b.dept_id = '" + deptid + "' \n"
                    + " GROUP BY group_name,a.department_name ORDER BY group_name ");

            rs = pst.executeQuery();
            while (rs.next()) {
                vacantPost = new VacantPost();

                vacantPost.setPostCleared(rs.getString("post_cleared"));
                vacantPost.setBaseLevelPost(rs.getString("base_post"));
                vacantPost.setVacRetirment(rs.getString("vac_retirment"));
                vacantPost.setPostFilledUp(rs.getString("post_filledup"));
                vacantPost.setPropostRequisitionSent(rs.getString("pro_req_sent"));
                vacantPost.setPostsBalance(rs.getString("balance_posts"));
                vacantPost.setDeptPostComm(rs.getString("dept_post_comm"));
                vacantPost.setProDeptPostBalance(rs.getString("prodept_post_balance"));
                vacantPost.setTotalpostFilledUp(rs.getString("total_filled_up"));
                vacantPost.setGroupName(rs.getString("group_name"));
                vacantPost.setSancPost(rs.getString("sanpost"));
                vacantPost.setManinPost(rs.getString("mainpost"));
                vacantPost.setMonth(month);
                vacantPost.setYear(year);
                vacantPost.setDeptId(deptid);
                // vacantPost.setDeptName(rs.getString("department_name"));

                list.add(vacantPost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public List summaryvacantpostwise(String month, String year, String deptid, String gname) {
        List list = null;
        VacantPost vacantPost = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;

        try {
            list = new ArrayList();
            con = dataSource.getConnection();
            //pst = con.prepareStatement("select b.group_name,SUM(cast(b.post_cleared as integer)) as post_cleared,SUM(cast(b.total_filled_up as integer)) as total_filled_up,SUM(cast(b.base_post as integer)) as base_post,SUM(cast(b.vac_retirment as integer)) as vac_retirment,SUM(cast(b.post_filledup as integer)) as post_filledup,SUM(cast(b.balance_posts as integer)) as balance_posts,SUM(cast(b.pro_req_sent as integer)) as pro_req_sent,SUM(cast(b.dept_post_comm as integer)) as dept_post_comm,SUM(cast(b.prodept_post_balance as integer)) as prodept_post_balance FROM g_department a, g_vacant_post_details b WHERE  month = '" + month + "' AND year = '" + year + "' AND a.department_code = b.dept_id AND  a.if_active = 'Y' GROUP BY  a.department_name,department_code,a.department_code ORDER BY a.department_code ");
            pst = con.prepareStatement("select a.post,b.group_name,SUM(cast(COALESCE( NULLIF(post_cleared,'') , '0' ) as integer)) \n"
                    + "as post_cleared,SUM(cast(COALESCE( NULLIF(total_filled_up,'') , '0' ) as integer))  \n"
                    + "as total_filled_up,SUM(cast(COALESCE( NULLIF(base_post,'') , '0' ) as integer)) \n"
                    + "as base_post,SUM(cast(COALESCE( NULLIF(vac_retirment,'') , '0' ) as integer))  \n"
                    + "as vac_retirment,SUM(cast(COALESCE( NULLIF(post_filledup,'') , '0' ) as integer)) \n"
                    + " as post_filledup,SUM(cast(COALESCE( NULLIF(balance_posts,'') , '0' ) as integer))  \n"
                    + " as balance_posts,SUM(cast(COALESCE( NULLIF(mainpost,'') , '0' ) as integer)) as mainpost,SUM(cast(COALESCE( NULLIF(sentreq,'') , '0' ) as integer))  \n"
                    + " as pro_req_sent,SUM(cast(COALESCE( NULLIF(sanpost,'') , '0' ) as integer)) as sanpost,SUM(cast(COALESCE( NULLIF(dept_post_comm,'') , '0' ) as integer))  \n"
                    + " as dept_post_comm,SUM(cast(COALESCE( NULLIF(prodept_post_balance,'') , '0' ) as integer)) \n"
                    + " as prodept_post_balance FROM g_vacant_post_details b ,g_post a \n"
                    + " WHERE  b.id_post=a.post_code AND month = '" + month + "' AND year = '" + year + "'  AND b.group_name='" + gname + "' AND  b.dept_id = '" + deptid + "' \n"
                    + " GROUP BY a.post,b.group_name,b.dept_id ORDER BY post ");

            rs = pst.executeQuery();
            while (rs.next()) {
                vacantPost = new VacantPost();

                vacantPost.setPostCleared(rs.getString("post_cleared"));
                vacantPost.setBaseLevelPost(rs.getString("base_post"));
                vacantPost.setVacRetirment(rs.getString("vac_retirment"));
                vacantPost.setPostFilledUp(rs.getString("post_filledup"));
                vacantPost.setPropostRequisitionSent(rs.getString("pro_req_sent"));
                vacantPost.setPostsBalance(rs.getString("balance_posts"));
                vacantPost.setDeptPostComm(rs.getString("dept_post_comm"));
                vacantPost.setProDeptPostBalance(rs.getString("prodept_post_balance"));
                vacantPost.setTotalpostFilledUp(rs.getString("total_filled_up"));
                vacantPost.setGroupName(rs.getString("group_name"));
                vacantPost.setSancPost(rs.getString("sanpost"));
                vacantPost.setManinPost(rs.getString("mainpost"));
                vacantPost.setMonth(month);
                vacantPost.setYear(year);
                vacantPost.setDeptId(deptid);
                vacantPost.setPostId(rs.getString("post"));
                // vacantPost.setDeptName(rs.getString("department_name"));

                list.add(vacantPost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    public void updateMonthlyVacantPost(String deptid, String month, String year) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select count(*) as cnt  FROM g_vacant_post_details  WHERE  month = '" + month + "' AND year = '" + year + "' AND dept_id = '" + deptid + "' ");
            rs = pst.executeQuery();
            int cnt = 0;
            while (rs.next()) {
                cnt = rs.getInt("cnt");
            }
            if (cnt == 0) {
                String Pmonth = "";
                String Pyear = "";
                pst1 = con.prepareStatement("SELECT month,year FROM g_vacant_post_details WHERE dept_id = '" + deptid + "' ORDER BY id_vacant_post DESC LIMIT 1 ");
                rs1 = pst1.executeQuery();
                while (rs1.next()) {
                    Pmonth = rs1.getString("month");
                    Pyear = rs1.getString("year");
                }
                int Intmonth = CommonFunctions.getMonthAsInt(Pmonth);
                int Intyear = Integer.parseInt(Pyear);
                if ((Intyear >= 2020 && Intmonth >= 5) || (Intyear >= 2021 && Intmonth >= 1)) {
                    
                    pst = con.prepareStatement("select *  FROM g_vacant_post_details  WHERE  month = '" + Pmonth + "' AND year = '" + Pyear + "' AND dept_id = '" + deptid + "' ");
                    rs = pst.executeQuery();
                    // String mcode = "";
                    while (rs.next()) {
                        pst = con.prepareStatement("INSERT INTO g_vacant_post_details(id_vacant_post,id_cader,id_post,group_name,month,year,dept_id,sanpost,mainpost,vacancy,post_cleared,base_post,vac_retirment,post_filledup,last_month_base,current_month_base,total_filled_up,post_req_sent,balance_posts,sentreq,dept_post_comm,commission_name,prodept_post_balance,sanpost_pro,mainpost_pro,vacancy_pro,propost_cleared,probacklog,pro_retirment,postfilledup_pro,last_month_pro,current_month_pro,total_filled_up_pro,pro_balance_post,pro_req_sent) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                        String mcode = CommonFunctions.getMaxCode("g_vacant_post_details", "id_vacant_post", con);
                        pst.setString(1, mcode);
                        pst.setString(2, rs.getString("id_cader"));
                        pst.setString(3, rs.getString("id_post"));
                        pst.setString(4, rs.getString("group_name"));
                        pst.setString(5, month);
                        pst.setString(6, year);
                        pst.setString(7, deptid);

                        pst.setString(8, rs.getString("sanpost"));
                        pst.setString(9, rs.getString("mainpost"));
                        pst.setString(10, rs.getString("vacancy"));

                        pst.setString(11, rs.getString("post_cleared"));
                        pst.setString(12, rs.getString("base_post"));
                        pst.setString(13, rs.getString("vac_retirment"));

                        pst.setString(14, rs.getString("post_filledup"));
                        pst.setString(15, rs.getString("last_month_base"));
                        pst.setString(16, rs.getString("current_month_base"));
                        pst.setString(17, rs.getString("total_filled_up"));
                        pst.setString(18, rs.getString("post_req_sent"));
                        pst.setString(19, rs.getString("balance_posts"));

                        pst.setString(20, rs.getString("sentreq"));
                        pst.setString(21, rs.getString("dept_post_comm"));

                        pst.setString(22, rs.getString("commission_name"));

                        pst.setString(23, rs.getString("prodept_post_balance"));
                        pst.setString(24, rs.getString("sanpost_pro"));
                        pst.setString(25, rs.getString("mainpost_pro"));
                        pst.setString(26, rs.getString("vacancy_pro"));
                        pst.setString(27, rs.getString("propost_cleared"));
                        pst.setString(28, rs.getString("probacklog"));
                        pst.setString(29, rs.getString("pro_retirment"));

                        pst.setString(30, rs.getString("postfilledup_pro"));
                        pst.setString(31, rs.getString("last_month_pro"));
                        pst.setString(32, rs.getString("current_month_pro"));
                        pst.setString(33, rs.getString("total_filled_up_pro"));
                        pst.setString(34, rs.getString("pro_balance_post"));
                        pst.setString(35, rs.getString("pro_req_sent"));
                        pst.executeUpdate();

                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public List summaryvacantdeptgroupwise(String month, String year, String deptid) {
        List list = null;
        VacantPost vacantPost = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;

        try {
            list = new ArrayList();
            con = dataSource.getConnection();
            //pst = con.prepareStatement("select b.group_name,SUM(cast(b.post_cleared as integer)) as post_cleared,SUM(cast(b.total_filled_up as integer)) as total_filled_up,SUM(cast(b.base_post as integer)) as base_post,SUM(cast(b.vac_retirment as integer)) as vac_retirment,SUM(cast(b.post_filledup as integer)) as post_filledup,SUM(cast(b.balance_posts as integer)) as balance_posts,SUM(cast(b.pro_req_sent as integer)) as pro_req_sent,SUM(cast(b.dept_post_comm as integer)) as dept_post_comm,SUM(cast(b.prodept_post_balance as integer)) as prodept_post_balance FROM g_department a, g_vacant_post_details b WHERE  month = '" + month + "' AND year = '" + year + "' AND a.department_code = b.dept_id AND  a.if_active = 'Y' GROUP BY  a.department_name,department_code,a.department_code ORDER BY a.department_code ");
            pst = con.prepareStatement("select a.department_name,b.group_name,SUM(cast(COALESCE( NULLIF(post_cleared,'') , '0' ) as integer)) \n"
                    + "as post_cleared,SUM(cast(COALESCE( NULLIF(total_filled_up,'') , '0' ) as integer))  \n"
                    + "as total_filled_up,SUM(cast(COALESCE( NULLIF(base_post,'') , '0' ) as integer)) \n"
                    + "as base_post,SUM(cast(COALESCE( NULLIF(vac_retirment,'') , '0' ) as integer))  \n"
                    + "as vac_retirment,SUM(cast(COALESCE( NULLIF(post_filledup,'') , '0' ) as integer)) \n"
                    + " as post_filledup,SUM(cast(COALESCE( NULLIF(balance_posts,'') , '0' ) as integer))  \n"
                    + " as balance_posts,SUM(cast(COALESCE( NULLIF(mainpost,'') , '0' ) as integer)) as mainpost,SUM(cast(COALESCE( NULLIF(sentreq,'') , '0' ) as integer))  \n"
                    + " as pro_req_sent,SUM(cast(COALESCE( NULLIF(sanpost,'') , '0' ) as integer)) as sanpost,SUM(cast(COALESCE( NULLIF(dept_post_comm,'') , '0' ) as integer))  \n"
                    + " as dept_post_comm,SUM(cast(COALESCE( NULLIF(prodept_post_balance,'') , '0' ) as integer)) \n"
                    + " as prodept_post_balance FROM g_vacant_post_details b,g_department a \n"
                    + " WHERE  month = '" + month + "' AND year = '" + year + "' AND a.department_code = b.dept_id AND  b.dept_id = '" + deptid + "' \n"
                    + " GROUP BY group_name,a.department_name ORDER BY group_name ");

            rs = pst.executeQuery();
            while (rs.next()) {
                vacantPost = new VacantPost();

                vacantPost.setPostCleared(rs.getString("post_cleared"));
                vacantPost.setBaseLevelPost(rs.getString("base_post"));
                vacantPost.setVacRetirment(rs.getString("vac_retirment"));
                vacantPost.setPostFilledUp(rs.getString("post_filledup"));
                vacantPost.setPropostRequisitionSent(rs.getString("pro_req_sent"));
                vacantPost.setPostsBalance(rs.getString("balance_posts"));
                vacantPost.setDeptPostComm(rs.getString("dept_post_comm"));
                vacantPost.setProDeptPostBalance(rs.getString("prodept_post_balance"));
                vacantPost.setTotalpostFilledUp(rs.getString("total_filled_up"));
                vacantPost.setGroupName(rs.getString("group_name"));
                vacantPost.setSancPost(rs.getString("sanpost"));
                vacantPost.setManinPost(rs.getString("mainpost"));
                vacantPost.setMonth(month);
                vacantPost.setYear(year);
                vacantPost.setDeptId(deptid);
                // vacantPost.setDeptName(rs.getString("department_name"));

                list.add(vacantPost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public List summaryvacantdeptpostwise(String month, String year, String deptid, String gname) {
        List list = null;
        VacantPost vacantPost = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;

        try {
            list = new ArrayList();
            con = dataSource.getConnection();
            //pst = con.prepareStatement("select b.group_name,SUM(cast(b.post_cleared as integer)) as post_cleared,SUM(cast(b.total_filled_up as integer)) as total_filled_up,SUM(cast(b.base_post as integer)) as base_post,SUM(cast(b.vac_retirment as integer)) as vac_retirment,SUM(cast(b.post_filledup as integer)) as post_filledup,SUM(cast(b.balance_posts as integer)) as balance_posts,SUM(cast(b.pro_req_sent as integer)) as pro_req_sent,SUM(cast(b.dept_post_comm as integer)) as dept_post_comm,SUM(cast(b.prodept_post_balance as integer)) as prodept_post_balance FROM g_department a, g_vacant_post_details b WHERE  month = '" + month + "' AND year = '" + year + "' AND a.department_code = b.dept_id AND  a.if_active = 'Y' GROUP BY  a.department_name,department_code,a.department_code ORDER BY a.department_code ");
            pst = con.prepareStatement("select a.post,b.group_name,SUM(cast(COALESCE( NULLIF(post_cleared,'') , '0' ) as integer)) \n"
                    + "as post_cleared,SUM(cast(COALESCE( NULLIF(total_filled_up,'') , '0' ) as integer))  \n"
                    + "as total_filled_up,SUM(cast(COALESCE( NULLIF(base_post,'') , '0' ) as integer)) \n"
                    + "as base_post,SUM(cast(COALESCE( NULLIF(vac_retirment,'') , '0' ) as integer))  \n"
                    + "as vac_retirment,SUM(cast(COALESCE( NULLIF(post_filledup,'') , '0' ) as integer)) \n"
                    + " as post_filledup,SUM(cast(COALESCE( NULLIF(balance_posts,'') , '0' ) as integer))  \n"
                    + " as balance_posts,SUM(cast(COALESCE( NULLIF(mainpost,'') , '0' ) as integer)) as mainpost,SUM(cast(COALESCE( NULLIF(sentreq,'') , '0' ) as integer))  \n"
                    + " as pro_req_sent,SUM(cast(COALESCE( NULLIF(sanpost,'') , '0' ) as integer)) as sanpost,SUM(cast(COALESCE( NULLIF(dept_post_comm,'') , '0' ) as integer))  \n"
                    + " as dept_post_comm,SUM(cast(COALESCE( NULLIF(prodept_post_balance,'') , '0' ) as integer)) \n"
                    + " as prodept_post_balance FROM g_vacant_post_details b ,g_post a \n"
                    + " WHERE  b.id_post=a.post_code AND month = '" + month + "' AND year = '" + year + "'  AND b.group_name='" + gname + "' AND  b.dept_id = '" + deptid + "' \n"
                    + " GROUP BY a.post,b.group_name,b.dept_id ORDER BY post ");

            rs = pst.executeQuery();
            while (rs.next()) {
                vacantPost = new VacantPost();

                vacantPost.setPostCleared(rs.getString("post_cleared"));
                vacantPost.setBaseLevelPost(rs.getString("base_post"));
                vacantPost.setVacRetirment(rs.getString("vac_retirment"));
                vacantPost.setPostFilledUp(rs.getString("post_filledup"));
                vacantPost.setPropostRequisitionSent(rs.getString("pro_req_sent"));
                vacantPost.setPostsBalance(rs.getString("balance_posts"));
                vacantPost.setDeptPostComm(rs.getString("dept_post_comm"));
                vacantPost.setProDeptPostBalance(rs.getString("prodept_post_balance"));
                vacantPost.setTotalpostFilledUp(rs.getString("total_filled_up"));
                vacantPost.setGroupName(rs.getString("group_name"));
                vacantPost.setSancPost(rs.getString("sanpost"));
                vacantPost.setManinPost(rs.getString("mainpost"));
                vacantPost.setMonth(month);
                vacantPost.setYear(year);
                vacantPost.setDeptId(deptid);
                vacantPost.setPostId(rs.getString("post"));
                // vacantPost.setDeptName(rs.getString("department_name"));

                list.add(vacantPost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public List entryDetailsList(String month, String year, String deptid) {
        List list = null;
        VacantPost vacantPost = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;

        try {
            list = new ArrayList();
            con = dataSource.getConnection();
            pst = con.prepareStatement("select a.*,b.post from g_vacant_post_details a,g_post b  WHERE a.id_post=b.post_code AND a.dept_id='" + deptid + "' AND a.month='" + month + "' AND a.year='" + year + "'  AND a.id_post <> 'NA'"
                    + " UNION (select a.*,'Not Available' as post from g_vacant_post_details a  WHERE  a.dept_id='" + deptid + "'  AND a.month='" + month + "' AND a.year='" + year + "' AND a.id_post = 'NA') ORDER BY id_vacant_post DESC ");
            rs = pst.executeQuery();
            while (rs.next()) {
                vacantPost = new VacantPost();
                vacantPost.setVacantPostId(rs.getString("id_vacant_post"));
                vacantPost.setDeptId(rs.getString("dept_id"));
                String cadername = rs.getString("id_cader");
                if (cadername.equals("0")) {
                    vacantPost.setCadreId("All");
                } else {
                    vacantPost.setCadreId(rs.getString("id_cader"));
                }
                vacantPost.setPostId(rs.getString("post"));
                vacantPost.setGroupName(rs.getString("group_name"));
                vacantPost.setMonth(rs.getString("month"));
                vacantPost.setYear(rs.getString("year"));
                vacantPost.setSancPost(rs.getString("sanpost"));
                vacantPost.setManinPost(rs.getString("mainpost"));
                vacantPost.setVacancy(rs.getString("vacancy"));
                vacantPost.setPostCleared(rs.getString("post_cleared"));
                vacantPost.setBaseLevelPost(rs.getString("base_post"));
                vacantPost.setVacRetirment(rs.getString("vac_retirment"));
                vacantPost.setPostFilledUp(rs.getString("post_filledup"));
                vacantPost.setLastmonthfilled(rs.getString("last_month_base"));
                vacantPost.setCurrentmonthfilled(rs.getString("current_month_base"));
                vacantPost.setTotalpostFilledUp(rs.getString("total_filled_up"));
                vacantPost.setPostRequisitionSent(rs.getString("post_req_sent"));
                vacantPost.setPostsBalance(rs.getString("balance_posts"));
                vacantPost.setSentreq(rs.getString("sentreq"));
                vacantPost.setDeptPostComm(rs.getString("dept_post_comm"));
                vacantPost.setCommissionName(rs.getString("commission_name"));
                vacantPost.setProDeptPostBalance(rs.getString("prodept_post_balance"));
                vacantPost.setSancPostPro(rs.getString("sanpost_pro"));
                vacantPost.setManinPostPro(rs.getString("mainpost_pro"));
                vacantPost.setVacancyPro(rs.getString("vacancy_pro"));
                vacantPost.setProPostCleared(rs.getString("propost_cleared"));
                vacantPost.setProbacklog(rs.getString("probacklog"));
                vacantPost.setProRetirment(rs.getString("pro_retirment"));
                vacantPost.setPostFilledUppro(rs.getString("postfilledup_pro"));
                vacantPost.setLastMonthPro(rs.getString("last_month_pro"));
                vacantPost.setCurrentmonthfilledpro(rs.getString("current_month_pro"));
                vacantPost.setTotalpostFilledUppro(rs.getString("total_filled_up_pro"));
                vacantPost.setProPostsBalance(rs.getString("pro_balance_post"));
                vacantPost.setPropostRequisitionSent(rs.getString("pro_req_sent"));

                list.add(vacantPost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public List misSplReport(String month, String year) {
        List list = null;
        VacantPost vacantPost = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;

        try {
            list = new ArrayList();
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT  GC.group_name,department_name, cadre_name, GP.post AS post_name,SUM(cast(COALESCE( NULLIF(sanpost,'') , '0' ) as integer)) as b_sanpost,SUM(cast(COALESCE( NULLIF(sanpost_pro,'') , '0' ) as integer)) as p_sanpost,SUM(cast(COALESCE( NULLIF(mainpost,'') , '0' ) as integer)) as b_mainpost,SUM(cast(COALESCE( NULLIF(mainpost_pro,'') , '0' ) as integer)) as p_mainpost,SUM(cast(COALESCE( NULLIF(vacancy,'') , '0' ) as integer)) as b_vacancy,SUM(cast(COALESCE( NULLIF(vacancy_pro,'') , '0' ) as integer)) as p_vacancy FROM g_vacant_post_details GC LEFT OUTER JOIN g_department GD ON GC.dept_id = GD.department_code LEFT OUTER JOIN g_cadre GCD ON GCD.cadre_code = GC.id_cader LEFT OUTER JOIN g_post GP ON GP.post_code = GC.id_post WHERE month='" + month + "' AND year='" + year + "' GROUP BY cadre_name,department_name,post,group_name ORDER BY department_name,group_name ");
            rs = pst.executeQuery();
            while (rs.next()) {
                vacantPost = new VacantPost();
                vacantPost.setDeptName(rs.getString("department_name"));
                vacantPost.setCadreId(rs.getString("cadre_name"));
                vacantPost.setPostId(rs.getString("post_name"));
                vacantPost.setSancPost(rs.getString("b_sanpost"));
                vacantPost.setSancPostPro(rs.getString("p_sanpost"));
                vacantPost.setManinPost(rs.getString("b_mainpost"));
                vacantPost.setManinPostPro(rs.getString("p_mainpost"));
                vacantPost.setVacancy(rs.getString("b_vacancy"));
                vacantPost.setVacancyPro(rs.getString("p_vacancy"));
                vacantPost.setGroupName(rs.getString("group_name"));
                list.add(vacantPost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public List totalsummarygetmisRAScheme() {
        List list = null;
        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        Rascheme appointStatus = null;
        list = new ArrayList();
        String deptcode = null;

        try {
            con = dataSource.getConnection();
            pst1 = con.prepareStatement("SELECT DISTINCT(id_department) as deptcode FROM g_ra_scheme ORDER BY id_department ");
            rs1 = pst1.executeQuery();

            while (rs1.next()) {
                deptcode = rs1.getString("deptcode");
                pst = con.prepareStatement("select a.department_name,b.* FROM g_department a, g_ra_scheme b WHERE  a.department_code = b.id_department AND  a.if_active = 'Y' AND b.id_department='" + deptcode + "'  ORDER BY b.id_ra_scheme DESC LIMIT 1 ");
                rs = pst.executeQuery();
                while (rs.next()) {
                    appointStatus = new Rascheme();

                    appointStatus.setDeptName(rs.getString("department_name"));
                    appointStatus.setDeptId(rs.getString("id_department"));
                    appointStatus.setTotalCase(rs.getString("total_case"));
                    appointStatus.setTotalAppointment(rs.getString("total_appointment"));
                    appointStatus.setTotalRejected(rs.getString("total_rejected"));
                    appointStatus.setTotalCleared(rs.getString("total_cleared"));
                    appointStatus.setTotalPending(rs.getString("total_pending"));
                    appointStatus.setRemarks(rs.getString("remarks"));
                    appointStatus.setMonth(rs.getString("month"));
                    appointStatus.setYear(rs.getString("year"));
                    appointStatus.setRaSchemeId(rs.getString("id_ra_scheme"));
                    appointStatus.setTotalAppoLastmonth(rs.getString("total_app_lastmonth"));
                    appointStatus.setTotalRejLastmonth(rs.getString("total_rej_lastmonth"));

                    list.add(appointStatus);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public List totalsummaryvacantgroupwise(String month, String year) {
        List list = null;
        VacantPost vacantPost = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;

        try {
            list = new ArrayList();
            con = dataSource.getConnection();
            //pst = con.prepareStatement("select a.department_name,b.group_name,SUM(cast(b.post_cleared as integer)) as post_cleared,SUM(cast(b.total_filled_up as integer)) as total_filled_up,SUM(cast(b.base_post as integer)) as base_post,SUM(cast(b.vac_retirment as integer)) as vac_retirment,SUM(cast(b.post_filledup as integer)) as post_filledup,SUM(cast(b.balance_posts as integer)) as balance_posts,SUM(cast(b.pro_req_sent as integer)) as pro_req_sent,SUM(cast(b.dept_post_comm as integer)) as dept_post_comm,SUM(cast(b.prodept_post_balance as integer)) as prodept_post_balance FROM g_department a, g_vacant_post_details b WHERE  month = '" + month + "' AND year = '" + year + "' AND a.department_code = b.dept_id AND  a.if_active = 'Y' GROUP BY  a.department_name,department_code,a.department_code ORDER BY a.department_code ");
            pst = con.prepareStatement("select a.department_name,b.group_name,SUM(cast(COALESCE( NULLIF(post_cleared,'') , '0' ) as integer)) \n"
                    + "as post_cleared,SUM(cast(COALESCE( NULLIF(total_filled_up,'') , '0' ) as integer))  \n"
                    + "as total_filled_up,SUM(cast(COALESCE( NULLIF(base_post,'') , '0' ) as integer)) \n"
                    + "as base_post,SUM(cast(COALESCE( NULLIF(vac_retirment,'') , '0' ) as integer))  \n"
                    + "as vac_retirment,SUM(cast(COALESCE( NULLIF(post_filledup,'') , '0' ) as integer)) \n"
                    + " as post_filledup,SUM(cast(COALESCE( NULLIF(balance_posts,'') , '0' ) as integer))  \n"
                    + " as balance_posts,SUM(cast(COALESCE( NULLIF(mainpost,'') , '0' ) as integer)) as mainpost,SUM(cast(COALESCE( NULLIF(sentreq,'') , '0' ) as integer))  \n"
                    + " as pro_req_sent,SUM(cast(COALESCE( NULLIF(sanpost,'') , '0' ) as integer)) as sanpost,SUM(cast(COALESCE( NULLIF(dept_post_comm,'') , '0' ) as integer))  \n"
                    + " as dept_post_comm,SUM(cast(COALESCE( NULLIF(prodept_post_balance,'') , '0' ) as integer)) \n"
                    + " as prodept_post_balance FROM g_vacant_post_details b,g_department a \n"
                    + " WHERE  month = '" + month + "' AND year = '" + year + "' AND a.department_code = b.dept_id  \n"
                    + " GROUP BY group_name,a.department_name ORDER BY a.department_name,group_name ");

            rs = pst.executeQuery();
            while (rs.next()) {
                vacantPost = new VacantPost();

                vacantPost.setPostCleared(rs.getString("post_cleared"));
                vacantPost.setBaseLevelPost(rs.getString("base_post"));
                vacantPost.setVacRetirment(rs.getString("vac_retirment"));
                vacantPost.setPostFilledUp(rs.getString("post_filledup"));
                vacantPost.setPropostRequisitionSent(rs.getString("pro_req_sent"));
                vacantPost.setPostsBalance(rs.getString("balance_posts"));
                vacantPost.setDeptPostComm(rs.getString("dept_post_comm"));
                vacantPost.setProDeptPostBalance(rs.getString("prodept_post_balance"));
                vacantPost.setTotalpostFilledUp(rs.getString("total_filled_up"));
                vacantPost.setGroupName(rs.getString("group_name"));
                vacantPost.setSancPost(rs.getString("sanpost"));
                vacantPost.setManinPost(rs.getString("mainpost"));
                vacantPost.setMonth(month);
                vacantPost.setYear(year);
                // vacantPost.setDeptId(deptid);
                vacantPost.setDeptName(rs.getString("department_name"));

                list.add(vacantPost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public void DownlaodAadhaarDetails(OutputStream out, String filename, WritableWorkbook workbook) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        List list = null;
        List empList = null;
        String stm = null;
        try {
            con = dataSource.getConnection();

            WritableSheet sheet = workbook.createSheet(filename, 0);
            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            WritableCellFormat headcell3 = new WritableCellFormat(headformat);
            headcell3.setAlignment(Alignment.LEFT);
            headcell3.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell3.setWrap(true);

            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setAlignment(Alignment.LEFT);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);
            headcell.setBorder(Border.ALL, BorderLineStyle.DOUBLE);

            WritableCellFormat innercell = new WritableCellFormat(NumberFormats.INTEGER);
            innercell.setAlignment(Alignment.LEFT);
            innercell.setVerticalAlignment(VerticalAlignment.CENTRE);
            innercell.setWrap(true);

            int col = 0;
            int widthInChars = 40;

            Label label = null;
            jxl.write.Number num = null;

            label = new Label(0, 0, "HRMS ID", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            widthInChars = 40;
            sheet.addCell(label);
            label = new Label(1, 0, "NAME", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            widthInChars = 16;
            sheet.addCell(label);
            label = new Label(2, 0, "AADHAR NO", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            widthInChars = 16;
            sheet.addCell(label);
            label = new Label(3, 0, "GENDER", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            widthInChars = 16;
            sheet.addCell(label);
            label = new Label(4, 0, "DOB", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;

            widthInChars = 40;
            sheet.addCell(label);

            int row = 0;
            int slno = 0;

            stm = "SELECT a.id_no,ARRAY_TO_STRING(ARRAY[F_NAME, M_NAME,L_NAME], ' ') EMPNAME,gender,b.emp_id, to_char(dob,'dd/mm/YYYY') as dob FROM EMP_ID_DOC a,emp_mast b WHERE a.emp_id=b.emp_id AND id_description='AADHAAR' AND (length(id_no) = 12)";
            ps1 = con.prepareStatement(stm);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                slno += 1;
                row += 1;

                label = new Label(0, row, rs1.getString("emp_id"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                col++;
                widthInChars = 40;
                sheet.addCell(label);
                label = new Label(1, row, rs1.getString("EMPNAME"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                col++;
                widthInChars = 16;
                sheet.addCell(label);
                label = new Label(2, row, rs1.getString("id_no"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                col++;
                widthInChars = 25;
                sheet.addCell(label);
                label = new Label(3, row, rs1.getString("gender"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);

                col++;
                widthInChars = 25;
                sheet.addCell(label);
                label = new Label(4, row, rs1.getString("dob"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);

                sheet.addCell(label);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

}
