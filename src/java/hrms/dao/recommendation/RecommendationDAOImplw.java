/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.recommendation;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.FileDownload;
import hrms.dao.AGPension.PensionNOCDAO;
import hrms.model.AGPension.PensionNOCBean;
import hrms.model.master.Department;
import hrms.model.master.District;
import hrms.model.parmast.DepartmentPromotionDetail;
import hrms.model.parmast.FiscalYearWiseParData;
import hrms.model.recommendation.DeptDistrictWiseNomination;
import hrms.model.recommendation.RecommendationDetailBean;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 *
 * @author Manisha
 */
public class RecommendationDAOImplw implements RecommendationDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    private String uploadPath;
    private PensionNOCDAO pensionNocDAO;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public void setPensionNocDAO(PensionNOCDAO pensionNocDAO) {
        this.pensionNocDAO = pensionNocDAO;
    }

    @Override
    public RecommendationDetailBean getRecommendationApplicationDetail(int recommendationId) {
        RecommendationDetailBean recommendationDetailBean = new RecommendationDetailBean();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("Select recommendation_id,initiated_by_empid,recommendation_master.initiated_by_spc,"
                    + "ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,post,created_on_date,recommendation_type,"
                    + "task_id,submited_on_date,is_submitted_dept from recommendation_master "
                    + "INNER join emp_mast ON  recommendation_master.initiated_by_empid = emp_mast.emp_id "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE where recommendation_id=? ");
            pstmt.setInt(1, recommendationId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                recommendationDetailBean.setRecommendationId(rs.getInt("recommendation_id"));
                recommendationDetailBean.setCreatedondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("created_on_date")));
                recommendationDetailBean.setInitiatedByempId(rs.getString("initiated_by_empid"));
                recommendationDetailBean.setInitiatedByspc(rs.getString("initiated_by_spc"));
                recommendationDetailBean.setInitiatedByempname(rs.getString("EMP_NAME"));
                recommendationDetailBean.setInitiatedBypost(rs.getString("post"));
                recommendationDetailBean.setRecommenadationType(rs.getString("recommendation_type"));
                recommendationDetailBean.setTaskId(rs.getInt("task_id"));
                recommendationDetailBean.setSubmittedondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("submited_on_date")));
                recommendationDetailBean.setIsSubmittedToDept(rs.getString("is_submitted_dept"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return recommendationDetailBean;
    }

    @Override
    public RecommendationDetailBean getRecommendationApplicationDetailFromTaskId(int taskId) {
        RecommendationDetailBean recommendationDetailBean = new RecommendationDetailBean();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("Select recommendation_id,initiated_by_empid,recommendation_master.initiated_by_spc,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,post,created_on_date,recommendation_type,task_id,submited_on_date,is_submitted_dept from recommendation_master "
                    + "INNER join emp_mast ON  recommendation_master.initiated_by_empid = emp_mast.emp_id "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE where task_id=? ");
            pstmt.setInt(1, taskId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                recommendationDetailBean.setRecommendationId(rs.getInt("recommendation_id"));
                recommendationDetailBean.setCreatedondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("created_on_date")));
                recommendationDetailBean.setInitiatedByempId(rs.getString("initiated_by_empid"));
                recommendationDetailBean.setInitiatedByspc(rs.getString("initiated_by_spc"));
                recommendationDetailBean.setInitiatedByempname(rs.getString("EMP_NAME"));
                recommendationDetailBean.setInitiatedBypost(rs.getString("post"));
                recommendationDetailBean.setRecommenadationType(rs.getString("recommendation_type"));
                recommendationDetailBean.setTaskId(rs.getInt("task_id"));
                recommendationDetailBean.setSubmittedondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("submited_on_date")));
                recommendationDetailBean.setIsSubmittedToDept(rs.getString("is_submitted_dept"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return recommendationDetailBean;
    }

    @Override
    public void submittodepartment(int recommendationId, int taskId, String submitteddeptby) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            con = this.dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            pstmt = con.prepareStatement("SELECT recommended_detail_id,recommended_empid,recommended_off_code,recommendation_type FROM recommendation_details  "
                    + "inner join recommendation_master on recommendation_details.recommendation_id = recommendation_master.recommendation_id "
                    + "WHERE recommendation_details.recommendation_id=? and is_approved='Y'");
            pstmt.setInt(1, recommendationId);
            res = pstmt.executeQuery();
            ArrayList<RecommendationDetailBean> recommendationList = new ArrayList();
            while (res.next()) {
                RecommendationDetailBean recommendationDetailBean = new RecommendationDetailBean();
                recommendationDetailBean.setRecommendeddetailId(res.getInt("recommended_detail_id"));
                recommendationDetailBean.setRecommendedempId(res.getString("recommended_empid"));
                recommendationDetailBean.setRecommendedoffcode(res.getString("recommended_off_code"));
                recommendationDetailBean.setRecommenadationType(res.getString("recommendation_type"));
                recommendationList.add(recommendationDetailBean);
            }

            pstmt = con.prepareStatement("UPDATE recommendation_details SET noc_id=? where recommended_detail_id=?");
            for (int i = 0; i < recommendationList.size(); i++) {
                RecommendationDetailBean recommendationDetailBean = recommendationList.get(i);
                if (!recommendationDetailBean.getRecommenadationType().equals("Premature Retirement")) {
                    PensionNOCBean pensionNOCBean = new PensionNOCBean();
                    pensionNOCBean.setAppraiseeId(recommendationDetailBean.getRecommendedempId());
                    pensionNOCBean.setOffcode(recommendationDetailBean.getRecommendedoffcode());
                    pensionNOCBean.setNocfor(recommendationDetailBean.getRecommenadationType());
                    pensionNOCBean.setNocRequest("Both");
                    int nocId = pensionNocDAO.saveEmployeeListForPension(pensionNOCBean, submitteddeptby);
                    System.out.println("nocId:" + nocId);
                    pstmt.setInt(1, nocId);
                    pstmt.setInt(2, recommendationDetailBean.getRecommendeddetailId());
                    pstmt.executeUpdate();
                }
            }
            pstmt = con.prepareStatement("UPDATE recommendation_master SET is_submitted_dept = 'Y',submitted_on_date_dept=?,submitted_dept_by=? WHERE recommendation_id=?");
            pstmt.setTimestamp(1, new Timestamp(dateFormat.parse(startTime).getTime()));
            pstmt.setString(2, submitteddeptby);
            pstmt.setInt(3, recommendationId);
            pstmt.executeUpdate();

            pstmt = con.prepareStatement("UPDATE TASK_MASTER SET status_id=112 WHERE TASK_ID=?");
            pstmt.setInt(1, taskId);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt, con);
        }
    }

    @Override
    public ArrayList getRecommendationList(String empId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList recommendationList = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("Select noofnomination,recommendation_master.recommendation_id,initiated_by_empid,recommendation_master.initiated_by_spc,ARRAY_TO_STRING(ARRAY[INITIALS, "
                    + "F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,post,created_on_date,recommendation_type,task_id,submited_on_date,is_submitted_dept,"
                    + "department_name,for_year from recommendation_master "
                    + "LEFT OUTER JOIN (select count(*) as noofnomination,recommendation_id from recommendation_details group by recommendation_id) as recommendation_details on recommendation_master.recommendation_id = recommendation_details.recommendation_id "
                    + "INNER JOIN emp_mast ON  recommendation_master.initiated_by_empid = emp_mast.emp_id "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN g_office on recommendation_master.off_code = g_office.off_code "
                    + "INNER JOIN G_DEPARTMENT ON g_office.department_code = G_DEPARTMENT.department_code "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE where initiated_by_empid=? or submitted_dept_by=?");
            pstmt.setString(1, empId);
            pstmt.setString(2, empId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                RecommendationDetailBean recommendationDetailBean = new RecommendationDetailBean();
                recommendationDetailBean.setRecommendationId(rs.getInt("recommendation_id"));
                recommendationDetailBean.setNoofnominations(rs.getInt("noofnomination"));
                recommendationDetailBean.setCreatedondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("created_on_date")));
                recommendationDetailBean.setInitiatedByempId(rs.getString("initiated_by_empid"));
                recommendationDetailBean.setInitiatedByspc(rs.getString("initiated_by_spc"));
                recommendationDetailBean.setInitiatedByempname(rs.getString("EMP_NAME"));
                recommendationDetailBean.setInitiatedBypost(rs.getString("post"));
                recommendationDetailBean.setRecommenadationType(rs.getString("recommendation_type"));
                recommendationDetailBean.setTaskId(rs.getInt("task_id"));
                recommendationDetailBean.setSubmittedondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("submited_on_date")));
                recommendationDetailBean.setIsSubmittedToDept(rs.getString("is_submitted_dept"));
                recommendationDetailBean.setDepartmentName(rs.getString("department_name"));
                recommendationDetailBean.setForyear(rs.getString("for_year"));
                recommendationList.add(recommendationDetailBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return recommendationList;
    }

    @Override
    public ArrayList getOtherRecommendationList(String offCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList otherrecommendationList = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("Select recommendation_id,initiated_by_empid,recommendation_master.initiated_by_spc,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,post,created_on_date,recommendation_master.off_code,recommendation_type,task_id,submited_on_date,ddo_off_code,recommendation_master.off_code from recommendation_master  "
                    + "INNER join emp_mast ON  recommendation_master.initiated_by_empid = emp_mast.emp_id "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN ddo_to_co_mapping  ON recommendation_master.off_code = ddo_to_co_mapping.ddo_off_code "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE where co_off_code=? and recommendation_master.off_code != ?");
            pstmt.setString(1, offCode);
            pstmt.setString(2, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                RecommendationDetailBean recommendationDetailBean = new RecommendationDetailBean();
                recommendationDetailBean.setRecommendationId(rs.getInt("recommendation_id"));
                recommendationDetailBean.setCreatedondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("created_on_date")));
                recommendationDetailBean.setInitiatedByempId(rs.getString("initiated_by_empid"));
                recommendationDetailBean.setInitiatedByspc(rs.getString("initiated_by_spc"));
                recommendationDetailBean.setInitiatedByempname(rs.getString("EMP_NAME"));
                recommendationDetailBean.setInitiatedBypost(rs.getString("post"));
                recommendationDetailBean.setRecommenadationType(rs.getString("recommendation_type"));
                recommendationDetailBean.setTaskId(rs.getInt("task_id"));
                recommendationDetailBean.setSubmittedondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("submited_on_date")));
                recommendationDetailBean.setOffCode(rs.getString("off_code"));
                otherrecommendationList.add(recommendationDetailBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return otherrecommendationList;
    }

    @Override
    public RecommendationDetailBean getRecommendationDetailFromRecommendeddetailId(int recommendationId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        RecommendationDetailBean recommendationDetailBean = new RecommendationDetailBean();

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("Select recommendation_master.recommendation_id,recommended_empid,initiated_by_empid,"
                    + "recommendation_master.initiated_by_spc,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,post,created_on_date,"
                    + "recommendation_type,OFF_EN,remark_by_authorities, exceptional_work, other_activities,authorities_original_filename,"
                    + "exceptional_work_original_filename, other_activities_original_filename,is_approved from recommendation_details "
                    + "INNER JOIN recommendation_master ON  recommendation_details.recommendation_id = recommendation_master.recommendation_id  "
                    + "INNER join emp_mast ON  recommendation_details.recommended_empid = emp_mast.emp_id "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE = G_OFFICE.OFF_CODE "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE where recommendation_master.recommendation_id=? ");
            pstmt.setInt(1, recommendationId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                recommendationDetailBean.setRecommendationId(rs.getInt("recommendation_id"));
                recommendationDetailBean.setCreatedondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("created_on_date")));
                recommendationDetailBean.setInitiatedByempId(rs.getString("initiated_by_empid"));
                recommendationDetailBean.setInitiatedByspc(rs.getString("initiated_by_spc"));
                recommendationDetailBean.setRecommendedempId(rs.getString("recommended_empid"));
                recommendationDetailBean.setRecommendedempname(rs.getString("EMP_NAME"));
                recommendationDetailBean.setRecommendedpost(rs.getString("post"));
                recommendationDetailBean.setRecommenadationType(rs.getString("recommendation_type"));
                recommendationDetailBean.setRecommendedempofficename(rs.getString("OFF_EN"));
                recommendationDetailBean.setRecommendationandCommendation(rs.getString("remark_by_authorities"));
                recommendationDetailBean.setAuthoritiesoriginalfilename(rs.getString("authorities_original_filename"));
                recommendationDetailBean.setEmpExceptionalWork(rs.getString("exceptional_work"));
                recommendationDetailBean.setExceptionalworkoriginalfilename(rs.getString("exceptional_work_original_filename"));
                recommendationDetailBean.setEmpActivitiesandIssue(rs.getString("other_activities"));
                recommendationDetailBean.setOtheractivitiesoriginalfilename(rs.getString("other_activities_original_filename"));
                recommendationDetailBean.setIsApproved(rs.getString("is_approved"));
                recommendationDetailBean.setRecommendeddetailId(recommendationId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return recommendationDetailBean;
    }

    @Override
    public RecommendationDetailBean getRecommendationDetail(int recommendeddetailId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        RecommendationDetailBean recommendationDetailBean = new RecommendationDetailBean();

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("Select recommendation_master.recommendation_id,recommended_empid,initiated_by_empid,"
                    + "recommendation_master.initiated_by_spc,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,post,created_on_date,"
                    + "recommendation_type,OFF_EN,remark_by_authorities, exceptional_work, other_activities,authorities_original_filename,authorities_disk_filename,"
                    + "exceptional_work_disk_filename,exceptional_work_original_filename, other_activities_original_filename,other_activities_disk_filename,"
                    + "reason_for_recommendation,is_approved,submited_on_date,"
                    + "is_submitted_dept,task_id, overall_views, overall_views_original_filename,overall_views_disk_filename,overall_views_file_type,"
                    + "reason_for_recommendation_original_filename, reason_for_recommendation_disk_filename, reason_for_recommendation_file_type "
                    + "from recommendation_details "
                    + "INNER JOIN recommendation_master ON  recommendation_details.recommendation_id = recommendation_master.recommendation_id  "
                    + "INNER join emp_mast ON  recommendation_details.recommended_empid = emp_mast.emp_id "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE = G_OFFICE.OFF_CODE "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE where recommended_detail_id=? ");
            pstmt.setInt(1, recommendeddetailId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                recommendationDetailBean.setUploadedPath(this.uploadPath);
                recommendationDetailBean.setRecommendationId(rs.getInt("recommendation_id"));
                recommendationDetailBean.setCreatedondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("created_on_date")));
                recommendationDetailBean.setInitiatedByempId(rs.getString("initiated_by_empid"));
                recommendationDetailBean.setInitiatedByspc(rs.getString("initiated_by_spc"));
                recommendationDetailBean.setRecommendedempId(rs.getString("recommended_empid"));
                recommendationDetailBean.setRecommendedempname(rs.getString("EMP_NAME"));
                recommendationDetailBean.setRecommendedpost(rs.getString("post"));
                recommendationDetailBean.setRecommenadationType(rs.getString("recommendation_type"));
                recommendationDetailBean.setRecommendedempofficename(rs.getString("OFF_EN"));
                recommendationDetailBean.setRecommendationandCommendation(rs.getString("remark_by_authorities"));
                recommendationDetailBean.setAuthoritiesoriginalfilename(rs.getString("authorities_original_filename"));
                recommendationDetailBean.setAuthoritiesdiskfilename(rs.getString("authorities_disk_filename"));
                recommendationDetailBean.setEmpExceptionalWork(rs.getString("exceptional_work"));
                recommendationDetailBean.setExceptionalworkoriginalfilename(rs.getString("exceptional_work_original_filename"));
                recommendationDetailBean.setExceptionalworkdiskfilename(rs.getString("exceptional_work_disk_filename"));
                recommendationDetailBean.setEmpActivitiesandIssue(rs.getString("other_activities"));
                recommendationDetailBean.setReasonforrecommendation(rs.getString("reason_for_recommendation"));
                recommendationDetailBean.setOtheractivitiesoriginalfilename(rs.getString("other_activities_original_filename"));
                recommendationDetailBean.setOtheractivitiesdiskfilename(rs.getString("other_activities_disk_filename"));
                recommendationDetailBean.setIsApproved(rs.getString("is_approved"));
                recommendationDetailBean.setIsSubmittedToDept(rs.getString("is_submitted_dept"));
                recommendationDetailBean.setSubmittedondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("submited_on_date")));
                recommendationDetailBean.setTaskId(rs.getInt("task_id"));
                recommendationDetailBean.setOverallviews(rs.getString("overall_views"));
                recommendationDetailBean.setOverallviewsoriginalfilename(rs.getString("overall_views_original_filename"));
                recommendationDetailBean.setOverallviewsdiskfilename(rs.getString("overall_views_disk_filename"));
                recommendationDetailBean.setReasonforrecommendationoriginalfilename(rs.getString("reason_for_recommendation_original_filename"));
                recommendationDetailBean.setReasonforrecommendationdiskfilename(rs.getString("reason_for_recommendation_disk_filename"));
                recommendationDetailBean.setRecommendeddetailId(recommendeddetailId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return recommendationDetailBean;
    }

    @Override
    public FileDownload getFileDownloadData(int recommendeddetailId, String documentTypeName) {
        FileDownload fileDownload = new FileDownload();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT authorities_disk_filename, authorities_original_filename, authorities_file_type, "
                    + "exceptional_work_original_filename, exceptional_work_disk_filename, exceptional_work_file_type, other_activities_original_filename, "
                    + "other_activities_disk_filename, other_activities_file_type,overall_views_original_filename, overall_views_disk_filename, "
                    + "overall_views_file_type, reason_for_recommendation_original_filename, reason_for_recommendation_disk_filename, reason_for_recommendation_file_type "
                    + "FROM recommendation_details where recommended_detail_id=?");
            pstmt.setInt(1, recommendeddetailId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (documentTypeName.equals("authorities")) {
                    fileDownload.setOriginalfilename(rs.getString("authorities_original_filename"));
                    fileDownload.setFiletype(rs.getString("authorities_file_type"));
                    File f = new File(this.uploadPath + File.separator + rs.getString("authorities_disk_filename"));
                    fileDownload.setFilecontent(FileUtils.readFileToByteArray(f));
                } else if (documentTypeName.equals("exceptional")) {
                    fileDownload.setOriginalfilename(rs.getString("exceptional_work_original_filename"));
                    fileDownload.setFiletype(rs.getString("exceptional_work_file_type"));
                    File f = new File(this.uploadPath + File.separator + rs.getString("exceptional_work_disk_filename"));
                    fileDownload.setFilecontent(FileUtils.readFileToByteArray(f));
                } else if (documentTypeName.equals("other")) {
                    fileDownload.setOriginalfilename(rs.getString("other_activities_original_filename"));
                    fileDownload.setFiletype(rs.getString("other_activities_file_type"));
                    File f = new File(this.uploadPath + File.separator + rs.getString("other_activities_disk_filename"));
                    fileDownload.setFilecontent(FileUtils.readFileToByteArray(f));
                } else if (documentTypeName.equals("overallviews")) {
                    fileDownload.setOriginalfilename(rs.getString("overall_views_original_filename"));
                    fileDownload.setFiletype(rs.getString("overall_views_file_type"));
                    File f = new File(this.uploadPath + File.separator + rs.getString("overall_views_disk_filename"));
                    fileDownload.setFilecontent(FileUtils.readFileToByteArray(f));
                } else if (documentTypeName.equals("reasonrecommendation")) {
                    fileDownload.setOriginalfilename(rs.getString("reason_for_recommendation_original_filename"));
                    fileDownload.setFiletype(rs.getString("reason_for_recommendation_file_type"));
                    File f = new File(this.uploadPath + File.separator + rs.getString("reason_for_recommendation_disk_filename"));
                    fileDownload.setFilecontent(FileUtils.readFileToByteArray(f));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return fileDownload;
    }

    @Override
    public String saveRecommendedEmployeeDetail(RecommendationDetailBean recommendationDetailBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        String msg = "Sucessfully Saved";
        try {
            con = this.dataSource.getConnection();
            String authorities_disk_filename = null;
            String authoritiesoriginalfilename = null;
            String authorities_file_type = null;
            String exceptionalworkdiskfilename = null;
            String exceptionalworkoriginalfilename = null;
            String exceptionalworkfiletype = null;
            String otheractivitiesdiskfilename = null;
            String otheractivitiesoriginalfilename = null;
            String otheractivitiesfiletype = null;
            if (recommendationDetailBean.getReasonforrecommendationDocument() != null && !recommendationDetailBean.getReasonforrecommendationDocument().isEmpty()) {
                authoritiesoriginalfilename = recommendationDetailBean.getReasonforrecommendationDocument().getOriginalFilename();
                authorities_disk_filename = new Date().getTime() + "";
                authorities_file_type = recommendationDetailBean.getReasonforrecommendationDocument().getContentType();
                byte[] bytes = recommendationDetailBean.getReasonforrecommendationDocument().getBytes();
                File dir = new File(this.uploadPath + File.separator);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + authorities_disk_filename);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
                pstmt = con.prepareStatement("UPDATE recommendation_details SET reason_for_recommendation_disk_filename=?, reason_for_recommendation_original_filename = ?,reason_for_recommendation_file_type=? "
                        + "where recommended_detail_id=?");
                pstmt.setString(1, authorities_disk_filename);
                pstmt.setString(2, authoritiesoriginalfilename);
                pstmt.setString(3, authorities_file_type);
                pstmt.setInt(4, recommendationDetailBean.getRecommendeddetailId());
                pstmt.executeUpdate();
            }
            if (recommendationDetailBean.getRecommendationandCommendationdocument() != null && !recommendationDetailBean.getRecommendationandCommendationdocument().isEmpty()) {
                authoritiesoriginalfilename = recommendationDetailBean.getRecommendationandCommendationdocument().getOriginalFilename();
                authorities_disk_filename = new Date().getTime() + "";
                authorities_file_type = recommendationDetailBean.getRecommendationandCommendationdocument().getContentType();
                byte[] bytes = recommendationDetailBean.getRecommendationandCommendationdocument().getBytes();
                File dir = new File(this.uploadPath + File.separator);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + authorities_disk_filename);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
                pstmt = con.prepareStatement("UPDATE recommendation_details SET authorities_disk_filename=?, authorities_original_filename = ?,authorities_file_type=? "
                        + "where recommended_detail_id=?");
                pstmt.setString(1, authorities_disk_filename);
                pstmt.setString(2, authoritiesoriginalfilename);
                pstmt.setString(3, authorities_file_type);
                pstmt.setInt(4, recommendationDetailBean.getRecommendeddetailId());
                pstmt.executeUpdate();
            }
            if (recommendationDetailBean.getEmpExceptionalWorkdocument() != null && !recommendationDetailBean.getEmpExceptionalWorkdocument().isEmpty()) {
                exceptionalworkoriginalfilename = recommendationDetailBean.getEmpExceptionalWorkdocument().getOriginalFilename();
                exceptionalworkdiskfilename = new Date().getTime() + "";
                exceptionalworkfiletype = recommendationDetailBean.getEmpExceptionalWorkdocument().getContentType();
                byte[] bytes = recommendationDetailBean.getEmpExceptionalWorkdocument().getBytes();
                File dir = new File(this.uploadPath + File.separator);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + exceptionalworkdiskfilename);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                pstmt = con.prepareStatement("UPDATE recommendation_details SET exceptional_work_original_filename=?,exceptional_work_disk_filename=?,exceptional_work_file_type=? "
                        + "where recommended_detail_id=?");
                pstmt.setString(1, exceptionalworkoriginalfilename);
                pstmt.setString(2, exceptionalworkdiskfilename);
                pstmt.setString(3, exceptionalworkfiletype);
                pstmt.setInt(4, recommendationDetailBean.getRecommendeddetailId());
                pstmt.executeUpdate();
            }
            if (recommendationDetailBean.getOtherActivitiesDocument() != null && !recommendationDetailBean.getOtherActivitiesDocument().isEmpty()) {
                otheractivitiesoriginalfilename = recommendationDetailBean.getOtherActivitiesDocument().getOriginalFilename();
                otheractivitiesdiskfilename = new Date().getTime() + "";
                otheractivitiesfiletype = recommendationDetailBean.getOtherActivitiesDocument().getContentType();
                byte[] bytes = recommendationDetailBean.getOtherActivitiesDocument().getBytes();
                File dir = new File(this.uploadPath + File.separator);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + otheractivitiesdiskfilename);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
                pstmt = con.prepareStatement("UPDATE recommendation_details SET other_activities_original_filename=?,other_activities_disk_filename=?,other_activities_file_type=? where recommended_detail_id=?");
                pstmt.setString(1, otheractivitiesoriginalfilename);
                pstmt.setString(2, otheractivitiesdiskfilename);
                pstmt.setString(3, otheractivitiesfiletype);
                pstmt.setInt(4, recommendationDetailBean.getRecommendeddetailId());
                pstmt.executeUpdate();
            }
            pstmt = con.prepareStatement("UPDATE recommendation_details SET remark_by_authorities = ?, exceptional_work = ?, other_activities=?, reason_for_recommendation=?, overall_views = ? "
                    + " where recommended_detail_id=?");
            pstmt.setString(1, recommendationDetailBean.getRecommendationandCommendation());
            pstmt.setString(2, recommendationDetailBean.getEmpExceptionalWork());
            pstmt.setString(3, recommendationDetailBean.getEmpActivitiesandIssue());
            pstmt.setString(4, recommendationDetailBean.getReasonforrecommendation());
            pstmt.setString(5, recommendationDetailBean.getOverallviews());
            pstmt.setInt(6, recommendationDetailBean.getRecommendeddetailId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            msg = "Error Occured";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt, con);
        }
        return msg;
    }

    @Override
    public void saveAuthorityAction(String actionType, RecommendationDetailBean recommendationDetailBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = this.dataSource.getConnection();
            if (actionType.equals("Approve")) {
                if (recommendationDetailBean.getOverallviewsdocument() != null && !recommendationDetailBean.getOverallviewsdocument().isEmpty()) {
                    String Overallviewsdocumentoriginalfilename = null;
                    String Overallviewsdocumentdiskfilename = null;
                    String Overallviewsdocumentfiletype = null;
                    Overallviewsdocumentoriginalfilename = recommendationDetailBean.getOverallviewsdocument().getOriginalFilename();
                    Overallviewsdocumentdiskfilename = new Date().getTime() + "";
                    Overallviewsdocumentfiletype = recommendationDetailBean.getOverallviewsdocument().getContentType();
                    byte[] bytes = recommendationDetailBean.getOverallviewsdocument().getBytes();
                    File dir = new File(this.uploadPath + File.separator);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File serverFile = new File(dir.getAbsolutePath() + File.separator + Overallviewsdocumentdiskfilename);
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(bytes);
                    stream.close();
                    pstmt = con.prepareStatement("UPDATE recommendation_details SET overall_views_original_filename=?,overall_views_disk_filename=?,overall_views_file_type=? where recommended_detail_id=?");
                    pstmt.setString(1, Overallviewsdocumentoriginalfilename);
                    pstmt.setString(2, Overallviewsdocumentdiskfilename);
                    pstmt.setString(3, Overallviewsdocumentfiletype);
                    pstmt.setInt(4, recommendationDetailBean.getRecommendeddetailId());
                    pstmt.executeUpdate();
                }
                pstmt = con.prepareStatement("UPDATE recommendation_details SET is_approved = ?,overall_views=? where recommended_detail_id=?");
                pstmt.setString(1, "Y");
                pstmt.setString(2, recommendationDetailBean.getOverallviews());
                pstmt.setInt(3, recommendationDetailBean.getRecommendeddetailId());
                pstmt.executeUpdate();
            } else if (actionType.equals("Decline")) {
                pstmt = con.prepareStatement("UPDATE recommendation_details SET is_approved = ? where recommended_detail_id=?");
                pstmt.setString(1, "N");
                pstmt.setInt(2, recommendationDetailBean.getRecommendeddetailId());
                pstmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt, con);
        }
    }

    @Override
    public void createRecommendationReport(RecommendationDetailBean recommendationDetailBean) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO recommendation_master(initiated_by_empid,initiated_by_spc, created_on_date,recommendation_type,"
                    + "off_code,for_year) Values (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, recommendationDetailBean.getInitiatedByempId());
            pst.setString(2, recommendationDetailBean.getInitiatedByspc());
            pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(4, recommendationDetailBean.getRecommenadationType());
            pst.setString(5, recommendationDetailBean.getOffCode());
            pst.setString(6, "2021");
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getrecommendationEmployeeList(String offCode, int recommendationId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList recommendationemplist = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,GPF_NO,emp_id,cur_spc,POST,recommended_empid from emp_mast "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "LEFT OUTER JOIN (SELECT recommended_empid,for_year FROM recommendation_master INNER JOIN recommendation_details on recommendation_master.recommendation_id=recommendation_details.recommendation_id "
                    + "WHERE for_year='2021' and (is_approved = 'Y' or is_approved is null))recommendation_details ON emp_mast.emp_id = recommendation_details.recommended_empid "
                    + "where cur_off_code=?  and (dep_code='02' or dep_code='05')  ORDER BY EMPNAME");
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                RecommendationDetailBean recommendationDetailBean = new RecommendationDetailBean();
                recommendationDetailBean.setRecommendationId(recommendationId);
                recommendationDetailBean.setRecommendedempname(rs.getString("EMPNAME"));
                recommendationDetailBean.setRecommendedempGpfNo(rs.getString("GPF_NO"));
                recommendationDetailBean.setRecommendedempId(rs.getString("emp_id"));
                recommendationDetailBean.setRecommendedspc(rs.getString("cur_spc"));
                recommendationDetailBean.setRecommendedpost(rs.getString("POST"));
                if (rs.getString("recommended_empid") != null && !rs.getString("recommended_empid").equals("")) {
                    recommendationDetailBean.setAlreadyAdded("Y");
                } else {
                    recommendationDetailBean.setAlreadyAdded("N");
                }
                recommendationemplist.add(recommendationDetailBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return recommendationemplist;

    }

    @Override
    public void saverecommendationEmployeeList(RecommendationDetailBean recommendationDetailBean) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            Long curtime = new Date().getTime();
            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO recommendation_details(recommended_empid,recommended_spc,recommended_off_code, recommended_dept_code, "
                    + "recommended_post_grp,recommendation_id) VALUES(?,?,?,?,?,?)");
            pst.setString(1, recommendationDetailBean.getRecommendedempId());
            pst.setString(2, recommendationDetailBean.getRecommendedspc());
            pst.setString(3, recommendationDetailBean.getRecommendedoffcode());
            pst.setString(4, recommendationDetailBean.getRecommendeddeptcode());
            pst.setString(5, recommendationDetailBean.getRecommendedpostgrp());
            pst.setInt(6, recommendationDetailBean.getRecommendationId());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public boolean removeRecommendationEmployee(int recommendeddetailId) {
        boolean employeeRemoved = true;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM recommendation_details WHERE recommended_detail_id = ?");
            pst.setInt(1, recommendeddetailId);
            pst.executeUpdate();
        } catch (Exception e) {
            employeeRemoved = false;
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return employeeRemoved;
    }

    @Override
    public List getrecommendationEmployeeListForOtherOffice(String offCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList recommendationemplist = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,emp_id,cur_spc,POST,recommended_empid from emp_mast "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "LEFT OUTER JOIN (SELECT recommended_empid FROM recommendation_details WHERE recommendation_id=?) AS recommendation_details ON emp_mast.emp_id = recommendation_details.recommended_empid "
                    + "where cur_off_code=?  and dep_code='02'  ORDER BY EMPNAME");
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                RecommendationDetailBean recommendationDetailBean = new RecommendationDetailBean();
                recommendationDetailBean.setRecommendedempname(rs.getString("EMPNAME"));
                recommendationDetailBean.setRecommendedempId(rs.getString("emp_id"));
                recommendationDetailBean.setRecommendedspc(rs.getString("cur_spc"));
                recommendationDetailBean.setRecommendedpost(rs.getString("POST"));
                if (rs.getString("recommended_empid") != null && !rs.getString("recommended_empid").equals("")) {
                    recommendationDetailBean.setAlreadyAdded("Y");
                } else {
                    recommendationDetailBean.setAlreadyAdded("N");
                }
                recommendationemplist.add(recommendationDetailBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return recommendationemplist;
    }

    @Override
    public ArrayList getrecommendedEmployeeList(int recommendationId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList recommendedList = new ArrayList();

        try {
            con = this.dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT recommended_detail_id,recommended_empid,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,"
                    + "recommended_spc,POST,emp_id,gpf_no,coalesce(reason_for_recommendation, '') as reason_for_recommendation,DEPARTMENT_NAME,is_submitted_dept FROM recommendation_details "
                    + "INNER JOIN emp_mast ON  recommendation_details.recommended_empid = emp_mast.emp_id  "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "INNER JOIN G_DEPARTMENT ON recommendation_details.recommended_dept_code = G_DEPARTMENT.DEPARTMENT_CODE "
                    + "INNER JOIN recommendation_master on recommendation_master.recommendation_id = recommendation_details.recommendation_id "
                    + "WHERE recommendation_details.recommendation_id=?");

            pstmt.setInt(1, recommendationId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                RecommendationDetailBean recommendationDetailBean = new RecommendationDetailBean();
                recommendationDetailBean.setRecommendationId(recommendationId);
                recommendationDetailBean.setRecommendeddetailId(rs.getInt("recommended_detail_id"));
                recommendationDetailBean.setRecommendedempId((rs.getString("recommended_empid")));
                recommendationDetailBean.setRecommendedempGpfNo(rs.getString("gpf_no"));
                recommendationDetailBean.setRecommendedempname((rs.getString("EMP_NAME")));
                recommendationDetailBean.setRecommendedspc(rs.getString("recommended_spc"));
                recommendationDetailBean.setRecommendedpost(rs.getString("POST"));
                recommendationDetailBean.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
                recommendationDetailBean.setIsSubmittedToDept(rs.getString("is_submitted_dept"));
                if (rs.getString("reason_for_recommendation").equals("")) {
                    recommendationDetailBean.setDataintegrity("N");
                } else {
                    recommendationDetailBean.setDataintegrity("Y");
                }
                System.out.println(recommendationDetailBean.getIsSubmittedToDept());
                recommendedList.add(recommendationDetailBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return recommendedList;
    }

    @Override
    public ArrayList getRecommendationListByDepartment(String departmentCode, String employeeGroup) {
        Connection con = null;
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        ArrayList recommendationList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            System.out.println(employeeGroup + ":departmentCode:" + departmentCode);
            /*Get List of Employee nominated field office and approved by Collector and HOD*/
            if (employeeGroup.equals("All")) {
                pstmt = con.prepareStatement("SELECT recommended_detail_id,recommended_empid,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,"
                        + "recommended_spc,POST,gpf_no,OFF_EN,recommendation_type,getspn(initiated_by_spc) as nominated_by_spn,getfullname(initiated_by_empid) as nominated_by,"
                        + "vigilance_noc_status,cbranch_noc_status,cb_noc_reason,vigilance_noc_reason, vigilance_disk_file_name,cb_disk_file_name,recommendation_details.NOC_ID FROM recommendation_details "
                        + "INNER JOIN recommendation_master ON  recommendation_details.recommendation_id = recommendation_master.recommendation_id  "
                        + "INNER JOIN emp_mast ON  recommendation_details.recommended_empid = emp_mast.emp_id  "
                        + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                        + "INNER JOIN G_OFFICE ON emp_mast.CUR_OFF_CODE = G_OFFICE.OFF_CODE "
                        + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                        + "LEFT OUTER JOIN PENSION_NOC ON recommendation_details.NOC_ID=PENSION_NOC.NOC_ID "
                        + "WHERE recommended_dept_code=? and is_submitted_dept='Y' and (is_approved = 'Y' or is_approved is null)");

                pstmt.setString(1, departmentCode);
            } else {
                pstmt = con.prepareStatement("SELECT recommended_detail_id,recommended_empid,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,"
                        + "recommended_spc,POST,gpf_no,OFF_EN,recommendation_type,getspn(initiated_by_spc) as nominated_by_spn,getfullname(initiated_by_empid) as nominated_by,"
                        + " vigilance_noc_status,cbranch_noc_status,cb_noc_reason,vigilance_noc_reason,vigilance_disk_file_name,cb_disk_file_name,recommendation_details.NOC_ID FROM recommendation_details "
                        + "INNER JOIN recommendation_master ON  recommendation_details.recommendation_id = recommendation_master.recommendation_id  "
                        + "INNER JOIN emp_mast ON  recommendation_details.recommended_empid = emp_mast.emp_id  "
                        + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                        + "INNER JOIN G_OFFICE ON emp_mast.CUR_OFF_CODE = G_OFFICE.OFF_CODE "
                        + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                        + "LEFT OUTER JOIN PENSION_NOC ON recommendation_details.NOC_ID=PENSION_NOC.NOC_ID "
                        + "WHERE recommended_dept_code=? and post_grp_type=? and is_submitted_dept='Y' and (is_approved = 'Y' or is_approved is null)");

                pstmt.setString(1, departmentCode);
                pstmt.setString(2, employeeGroup);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                RecommendationDetailBean recommendationDetailBean = new RecommendationDetailBean();
                recommendationDetailBean.setRecommendeddetailId(rs.getInt("recommended_detail_id"));
                recommendationDetailBean.setRecommendedempId((rs.getString("recommended_empid")));
                recommendationDetailBean.setRecommendedempname((rs.getString("EMP_NAME")));
                recommendationDetailBean.setRecommendedempGpfNo(rs.getString("gpf_no"));
                recommendationDetailBean.setRecommendedempofficename(rs.getString("OFF_EN"));
                recommendationDetailBean.setRecommendedpost(rs.getString("POST"));
                recommendationDetailBean.setRecommenadationType(rs.getString("recommendation_type"));
                recommendationDetailBean.setVigilancediskFileName(rs.getString("vigilance_disk_file_name"));
                recommendationDetailBean.setCbdiskFileName(rs.getString("cb_disk_file_name"));
                recommendationDetailBean.setNocId(rs.getInt("NOC_ID"));
                recommendationDetailBean.setNominatedbyemployee(rs.getString("nominated_by") + "," + rs.getString("nominated_by_spn"));
                recommendationDetailBean.setVigilancenocstatus(rs.getString("vigilance_noc_status"));
                recommendationDetailBean.setCbranchnocstatus(rs.getString("cbranch_noc_status"));
                recommendationDetailBean.setCbnocreason(rs.getString("cb_noc_reason"));
                recommendationDetailBean.setVigilancenocreason(rs.getString("vigilance_noc_reason"));
                recommendationList.add(recommendationDetailBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return recommendationList;
    }

    @Override
    public ArrayList getRecommendationListByGroup(String employeeGroup, String nominationType) {
        Connection con = null;
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        ArrayList recommendationList = new ArrayList();
        try {
            System.out.println("employeeGroup:" + employeeGroup + "  nominationType:" + nominationType);
            con = this.dataSource.getConnection();
            /*Get List of Employee nominated field office and approved by Collector and HOD*/
            if (employeeGroup.equals("All") && nominationType.equals("All")) {
                pstmt = con.prepareStatement("SELECT recommended_detail_id,recommended_empid,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,"
                        + "recommended_spc,POST,gpf_no,OFF_EN,recommendation_type,getspn(initiated_by_spc) as nominated_by_spn,getfullname(initiated_by_empid) as nominated_by,"
                        + "vigilance_noc_status,cbranch_noc_status,cb_noc_reason,vigilance_noc_reason, vigilance_disk_file_name,cb_disk_file_name,recommendation_details.NOC_ID,recommended_post_grp FROM recommendation_details "
                        + "INNER JOIN recommendation_master ON  recommendation_details.recommendation_id = recommendation_master.recommendation_id  "
                        + "INNER JOIN emp_mast ON  recommendation_details.recommended_empid = emp_mast.emp_id  "
                        + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                        + "INNER JOIN G_OFFICE ON emp_mast.CUR_OFF_CODE = G_OFFICE.OFF_CODE "
                        + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                        + "LEFT OUTER JOIN PENSION_NOC ON recommendation_details.NOC_ID=PENSION_NOC.NOC_ID "
                        + "WHERE is_submitted_dept='Y' and (is_approved = 'Y' or is_approved is null)");
            } else if (employeeGroup.equals("All") && !nominationType.equals("All")) {
                pstmt = con.prepareStatement("SELECT recommended_detail_id,recommended_empid,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,"
                        + "recommended_spc,POST,gpf_no,OFF_EN,recommendation_type,getspn(initiated_by_spc) as nominated_by_spn,getfullname(initiated_by_empid) as nominated_by,"
                        + "vigilance_noc_status,cbranch_noc_status,cb_noc_reason,vigilance_noc_reason, vigilance_disk_file_name,cb_disk_file_name,recommendation_details.NOC_ID,recommended_post_grp FROM recommendation_details "
                        + "INNER JOIN recommendation_master ON  recommendation_details.recommendation_id = recommendation_master.recommendation_id  "
                        + "INNER JOIN emp_mast ON  recommendation_details.recommended_empid = emp_mast.emp_id  "
                        + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                        + "INNER JOIN G_OFFICE ON emp_mast.CUR_OFF_CODE = G_OFFICE.OFF_CODE "
                        + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                        + "LEFT OUTER JOIN PENSION_NOC ON recommendation_details.NOC_ID=PENSION_NOC.NOC_ID "
                        + "WHERE and recommendation_type=? and is_submitted_dept='Y' and (is_approved = 'Y' or is_approved is null)");
                pstmt.setString(1, nominationType);
            } else if (!employeeGroup.equals("All") && nominationType.equals("All")) {
                pstmt = con.prepareStatement("SELECT recommended_detail_id,recommended_empid,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,"
                        + "recommended_spc,POST,gpf_no,OFF_EN,recommendation_type,getspn(initiated_by_spc) as nominated_by_spn,getfullname(initiated_by_empid) as nominated_by,"
                        + "vigilance_noc_status,cbranch_noc_status,cb_noc_reason,vigilance_noc_reason, vigilance_disk_file_name,cb_disk_file_name,recommendation_details.NOC_ID,recommended_post_grp FROM recommendation_details "
                        + "INNER JOIN recommendation_master ON  recommendation_details.recommendation_id = recommendation_master.recommendation_id  "
                        + "INNER JOIN emp_mast ON  recommendation_details.recommended_empid = emp_mast.emp_id  "
                        + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                        + "INNER JOIN G_OFFICE ON emp_mast.CUR_OFF_CODE = G_OFFICE.OFF_CODE "
                        + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                        + "LEFT OUTER JOIN PENSION_NOC ON recommendation_details.NOC_ID=PENSION_NOC.NOC_ID "
                        + "WHERE post_grp_type=? and is_submitted_dept='Y' and (is_approved = 'Y' or is_approved is null)");
                pstmt.setString(1, employeeGroup);
            } else if (!employeeGroup.equals("All") && !nominationType.equals("All")) {
                pstmt = con.prepareStatement("SELECT recommended_detail_id,recommended_empid,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,"
                        + "recommended_spc,POST,gpf_no,OFF_EN,recommendation_type,getspn(initiated_by_spc) as nominated_by_spn,getfullname(initiated_by_empid) as nominated_by,"
                        + "vigilance_noc_status,cbranch_noc_status,cb_noc_reason,vigilance_noc_reason, vigilance_disk_file_name,cb_disk_file_name,recommendation_details.NOC_ID,recommended_post_grp FROM recommendation_details "
                        + "INNER JOIN recommendation_master ON  recommendation_details.recommendation_id = recommendation_master.recommendation_id  "
                        + "INNER JOIN emp_mast ON  recommendation_details.recommended_empid = emp_mast.emp_id  "
                        + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                        + "INNER JOIN G_OFFICE ON emp_mast.CUR_OFF_CODE = G_OFFICE.OFF_CODE "
                        + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                        + "LEFT OUTER JOIN PENSION_NOC ON recommendation_details.NOC_ID=PENSION_NOC.NOC_ID "
                        + "WHERE recommendation_type=? and post_grp_type=? and is_submitted_dept='Y' and (is_approved = 'Y' or is_approved is null)");
                pstmt.setString(1, nominationType);
                pstmt.setString(2, employeeGroup);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                RecommendationDetailBean recommendationDetailBean = new RecommendationDetailBean();
                recommendationDetailBean.setRecommendeddetailId(rs.getInt("recommended_detail_id"));
                recommendationDetailBean.setRecommendedempId((rs.getString("recommended_empid")));
                recommendationDetailBean.setRecommendedempname((rs.getString("EMP_NAME")));
                recommendationDetailBean.setRecommendedempGpfNo(rs.getString("gpf_no"));
                recommendationDetailBean.setRecommendedempofficename(rs.getString("OFF_EN"));
                recommendationDetailBean.setRecommendedpost(rs.getString("POST"));
                recommendationDetailBean.setRecommendedpostgrp(rs.getString("recommended_post_grp"));
                recommendationDetailBean.setRecommenadationType(rs.getString("recommendation_type"));
                recommendationDetailBean.setVigilancediskFileName(rs.getString("vigilance_disk_file_name"));
                recommendationDetailBean.setCbdiskFileName(rs.getString("cb_disk_file_name"));
                recommendationDetailBean.setNocId(rs.getInt("NOC_ID"));
                recommendationDetailBean.setNominatedbyemployee(rs.getString("nominated_by") + "," + rs.getString("nominated_by_spn"));
                recommendationDetailBean.setVigilancenocstatus(rs.getString("vigilance_noc_status"));
                recommendationDetailBean.setCbranchnocstatus(rs.getString("cbranch_noc_status"));
                recommendationDetailBean.setCbnocreason(rs.getString("cb_noc_reason"));
                recommendationDetailBean.setVigilancenocreason(rs.getString("vigilance_noc_reason"));
                recommendationList.add(recommendationDetailBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return recommendationList;
    }

    @Override
    public ArrayList getRecommendationListByDepartment(String departmentCode, String employeeGroup, String nominationType) {
        Connection con = null;
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        ArrayList recommendationList = new ArrayList();
        try {
            System.out.println("employeeGroup:" + employeeGroup + "  nominationType:" + nominationType);
            con = this.dataSource.getConnection();
            /*Get List of Employee nominated field office and approved by Collector and HOD*/
            if (employeeGroup.equals("All") && nominationType.equals("All")) {
                pstmt = con.prepareStatement("SELECT recommended_detail_id,recommended_empid,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,"
                        + "recommended_spc,POST,gpf_no,OFF_EN,recommendation_type,getspn(initiated_by_spc) as nominated_by_spn,getfullname(initiated_by_empid) as nominated_by,"
                        + "vigilance_noc_status,cbranch_noc_status,cb_noc_reason,vigilance_noc_reason, vigilance_disk_file_name,cb_disk_file_name,recommendation_details.NOC_ID,recommended_post_grp FROM recommendation_details "
                        + "INNER JOIN recommendation_master ON  recommendation_details.recommendation_id = recommendation_master.recommendation_id  "
                        + "INNER JOIN emp_mast ON  recommendation_details.recommended_empid = emp_mast.emp_id  "
                        + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                        + "INNER JOIN G_OFFICE ON emp_mast.CUR_OFF_CODE = G_OFFICE.OFF_CODE "
                        + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                        + "LEFT OUTER JOIN PENSION_NOC ON recommendation_details.NOC_ID=PENSION_NOC.NOC_ID "
                        + "WHERE recommended_dept_code=? and is_submitted_dept='Y' and (is_approved = 'Y' or is_approved is null)");

                pstmt.setString(1, departmentCode);
            } else if (employeeGroup.equals("All") && !nominationType.equals("All")) {
                pstmt = con.prepareStatement("SELECT recommended_detail_id,recommended_empid,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,"
                        + "recommended_spc,POST,gpf_no,OFF_EN,recommendation_type,getspn(initiated_by_spc) as nominated_by_spn,getfullname(initiated_by_empid) as nominated_by,"
                        + "vigilance_noc_status,cbranch_noc_status,cb_noc_reason,vigilance_noc_reason, vigilance_disk_file_name,cb_disk_file_name,recommendation_details.NOC_ID,recommended_post_grp FROM recommendation_details "
                        + "INNER JOIN recommendation_master ON  recommendation_details.recommendation_id = recommendation_master.recommendation_id  "
                        + "INNER JOIN emp_mast ON  recommendation_details.recommended_empid = emp_mast.emp_id  "
                        + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                        + "INNER JOIN G_OFFICE ON emp_mast.CUR_OFF_CODE = G_OFFICE.OFF_CODE "
                        + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                        + "LEFT OUTER JOIN PENSION_NOC ON recommendation_details.NOC_ID=PENSION_NOC.NOC_ID "
                        + "WHERE recommended_dept_code=? and recommendation_type=? and is_submitted_dept='Y' and (is_approved = 'Y' or is_approved is null)");

                pstmt.setString(1, departmentCode);
                pstmt.setString(2, nominationType);
            } else if (!employeeGroup.equals("All") && nominationType.equals("All")) {
                pstmt = con.prepareStatement("SELECT recommended_detail_id,recommended_empid,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,"
                        + "recommended_spc,POST,gpf_no,OFF_EN,recommendation_type,getspn(initiated_by_spc) as nominated_by_spn,getfullname(initiated_by_empid) as nominated_by,"
                        + "vigilance_noc_status,cbranch_noc_status,cb_noc_reason,vigilance_noc_reason, vigilance_disk_file_name,cb_disk_file_name,recommendation_details.NOC_ID,recommended_post_grp FROM recommendation_details "
                        + "INNER JOIN recommendation_master ON  recommendation_details.recommendation_id = recommendation_master.recommendation_id  "
                        + "INNER JOIN emp_mast ON  recommendation_details.recommended_empid = emp_mast.emp_id  "
                        + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                        + "INNER JOIN G_OFFICE ON emp_mast.CUR_OFF_CODE = G_OFFICE.OFF_CODE "
                        + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                        + "LEFT OUTER JOIN PENSION_NOC ON recommendation_details.NOC_ID=PENSION_NOC.NOC_ID "
                        + "WHERE recommended_dept_code=? and post_grp_type=? and is_submitted_dept='Y' and (is_approved = 'Y' or is_approved is null)");
                pstmt.setString(1, departmentCode);
                pstmt.setString(2, employeeGroup);
            } else if (!employeeGroup.equals("All") && !nominationType.equals("All")) {
                pstmt = con.prepareStatement("SELECT recommended_detail_id,recommended_empid,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,"
                        + "recommended_spc,POST,gpf_no,OFF_EN,recommendation_type,getspn(initiated_by_spc) as nominated_by_spn,getfullname(initiated_by_empid) as nominated_by,"
                        + "vigilance_noc_status,cbranch_noc_status,cb_noc_reason,vigilance_noc_reason, vigilance_disk_file_name,cb_disk_file_name,recommendation_details.NOC_ID,recommended_post_grp FROM recommendation_details "
                        + "INNER JOIN recommendation_master ON  recommendation_details.recommendation_id = recommendation_master.recommendation_id  "
                        + "INNER JOIN emp_mast ON  recommendation_details.recommended_empid = emp_mast.emp_id  "
                        + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                        + "INNER JOIN G_OFFICE ON emp_mast.CUR_OFF_CODE = G_OFFICE.OFF_CODE "
                        + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                        + "LEFT OUTER JOIN PENSION_NOC ON recommendation_details.NOC_ID=PENSION_NOC.NOC_ID "
                        + "WHERE recommended_dept_code=? and recommendation_type=? and post_grp_type=? and is_submitted_dept='Y' and (is_approved = 'Y' or is_approved is null)");

                pstmt.setString(1, departmentCode);
                pstmt.setString(2, nominationType);
                pstmt.setString(3, employeeGroup);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                RecommendationDetailBean recommendationDetailBean = new RecommendationDetailBean();
                recommendationDetailBean.setRecommendeddetailId(rs.getInt("recommended_detail_id"));
                recommendationDetailBean.setRecommendedempId((rs.getString("recommended_empid")));
                recommendationDetailBean.setRecommendedempname((rs.getString("EMP_NAME")));
                recommendationDetailBean.setRecommendedempGpfNo(rs.getString("gpf_no"));
                recommendationDetailBean.setRecommendedempofficename(rs.getString("OFF_EN"));
                recommendationDetailBean.setRecommendedpost(rs.getString("POST"));
                recommendationDetailBean.setRecommendedpostgrp(rs.getString("recommended_post_grp"));
                recommendationDetailBean.setRecommenadationType(rs.getString("recommendation_type"));
                recommendationDetailBean.setVigilancediskFileName(rs.getString("vigilance_disk_file_name"));
                recommendationDetailBean.setCbdiskFileName(rs.getString("cb_disk_file_name"));
                recommendationDetailBean.setNocId(rs.getInt("NOC_ID"));
                recommendationDetailBean.setNominatedbyemployee(rs.getString("nominated_by") + "," + rs.getString("nominated_by_spn"));
                recommendationDetailBean.setVigilancenocstatus(rs.getString("vigilance_noc_status"));
                recommendationDetailBean.setCbranchnocstatus(rs.getString("cbranch_noc_status"));
                recommendationDetailBean.setCbnocreason(rs.getString("cb_noc_reason"));
                recommendationDetailBean.setVigilancenocreason(rs.getString("vigilance_noc_reason"));
                recommendationList.add(recommendationDetailBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return recommendationList;
    }

    @Override
    public ArrayList getRecommendationListDistrictWise(List districtList) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmthod = null;
        PreparedStatement pstmtcollec = null;
        ResultSet rs = null;
        ResultSet rshod = null;
        ResultSet rscollec = null;
        ArrayList recommendationList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT count(*) cnt FROM recommendation_details \n"
                    + " INNER JOIN recommendation_master ON  recommendation_details.recommendation_id = recommendation_master.recommendation_id  \n"
                    + " INNER JOIN emp_mast ON  recommendation_details.recommended_empid = emp_mast.emp_id  \n"
                    + " INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC \n"
                    + " INNER JOIN G_OFFICE ON emp_mast.CUR_OFF_CODE = G_OFFICE.OFF_CODE \n"
                    + " INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE \n"
                    + " WHERE dist_code=? and is_submitted_dept='Y'");
            pstmthod = con.prepareStatement("select recommended_post_grp,count(*) cnt from recommendation_master "
                    + "inner join recommendation_details on recommendation_master.recommendation_id = recommendation_details.recommendation_id "
                    + "inner join g_office on recommendation_details.recommended_off_code = g_office.off_code "
                    + "where submitted_to_off_type = 'H' and dist_code=? group by recommended_post_grp");
            pstmtcollec = con.prepareStatement("select recommended_post_grp, count(*) cnt from recommendation_master "
                    + "inner join recommendation_details on recommendation_master.recommendation_id = recommendation_details.recommendation_id "
                    + "inner join g_office on recommendation_details.recommended_off_code = g_office.off_code "
                    + "where submitted_to_off_type = 'C' and dist_code=? group by recommended_post_grp");
            for (int i = 0; i < districtList.size(); i++) {
                District district = (District) districtList.get(i);
                int deptWiseNoofnomination = 0;
                int hodWiseNoofnominationA = 0;
                int hodWiseNoofnominationB = 0;
                int hodWiseNoofnominationC = 0;
                int hodWiseNoofnominationD = 0;
                int collecWiseNoofnominationA = 0;
                int collecWiseNoofnominationB = 0;
                int collecWiseNoofnominationC = 0;
                int collecWiseNoofnominationD = 0;
                /**/
                pstmt.setString(1, district.getDistCode());
                rs = pstmt.executeQuery();

                pstmthod.setString(1, district.getDistCode());
                rshod = pstmthod.executeQuery();

                pstmtcollec.setString(1, district.getDistCode());
                rscollec = pstmtcollec.executeQuery();
                System.out.println(district.getDistCode());
                if (rs.next()) {
                    deptWiseNoofnomination = rs.getInt("cnt");
                }
                while (rshod.next()) {
                    if (rshod.getString("recommended_post_grp").equals("A")) {
                        hodWiseNoofnominationA = rshod.getInt("cnt");
                    } else if (rshod.getString("recommended_post_grp").equals("B")) {
                        hodWiseNoofnominationB = rshod.getInt("cnt");
                    } else if (rshod.getString("recommended_post_grp").equals("C")) {
                        hodWiseNoofnominationC = rshod.getInt("cnt");
                    } else if (rshod.getString("recommended_post_grp").equals("D")) {
                        hodWiseNoofnominationD = rshod.getInt("cnt");
                    }
                }
                while (rscollec.next()) {
                    if (rscollec.getString("recommended_post_grp").equals("A")) {
                        collecWiseNoofnominationA = rscollec.getInt("cnt");
                    } else if (rscollec.getString("recommended_post_grp").equals("B")) {
                        collecWiseNoofnominationB = rscollec.getInt("cnt");
                    } else if (rscollec.getString("recommended_post_grp").equals("C")) {
                        collecWiseNoofnominationC = rscollec.getInt("cnt");
                    } else if (rscollec.getString("recommended_post_grp").equals("D")) {
                        collecWiseNoofnominationD = rscollec.getInt("cnt");
                    }
                }
                DeptDistrictWiseNomination deptDistrictWiseNomination = new DeptDistrictWiseNomination();
                deptDistrictWiseNomination.setDistCode(district.getDistCode());
                deptDistrictWiseNomination.setDistName(district.getDistName());
                deptDistrictWiseNomination.setReceivedatdepartment(deptWiseNoofnomination);
                deptDistrictWiseNomination.setPendingatcollectorateA(collecWiseNoofnominationA);
                deptDistrictWiseNomination.setPendingatcollectorateB(collecWiseNoofnominationB);
                deptDistrictWiseNomination.setPendingatcollectorateC(collecWiseNoofnominationC);
                deptDistrictWiseNomination.setPendingatcollectorateD(collecWiseNoofnominationD);
                deptDistrictWiseNomination.setPendingathodA(hodWiseNoofnominationA);
                deptDistrictWiseNomination.setPendingathodB(hodWiseNoofnominationB);
                deptDistrictWiseNomination.setPendingathodC(hodWiseNoofnominationC);
                deptDistrictWiseNomination.setPendingathodD(hodWiseNoofnominationD);
                recommendationList.add(deptDistrictWiseNomination);
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rscollec, pstmtcollec);
            DataBaseFunctions.closeSqlObjects(rshod, pstmthod);
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return recommendationList;
    }

    @Override
    public HashMap getRecommendationListNoinamtionTypeWise() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        HashMap recommendationList = new HashMap();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select recommendation_type,recommended_post_grp, count(*) cnt from recommendation_master "
                    + "inner join recommendation_details on recommendation_master.recommendation_id = recommendation_details.recommendation_id "
                    + "inner join g_office on recommendation_details.recommended_off_code = g_office.off_code "
                    + "where submitted_to_off_type = 'C' or submitted_to_off_type = 'H' or is_submitted_dept='Y' group by recommendation_type,recommended_post_grp order by recommendation_type,recommended_post_grp");
            rs = pstmt.executeQuery();
            DeptDistrictWiseNomination deptDistrictWiseNominationAB = new DeptDistrictWiseNomination();
            DeptDistrictWiseNomination deptDistrictWiseNominationWB = new DeptDistrictWiseNomination();
            DeptDistrictWiseNomination deptDistrictWiseNominationIA = new DeptDistrictWiseNomination();
            DeptDistrictWiseNomination deptDistrictWiseNominationPR = new DeptDistrictWiseNomination();
            while (rs.next()) {
                String recommendation_type = rs.getString("recommendation_type");
                String recommended_post_grp = rs.getString("recommended_post_grp");

                if (recommendation_type.equals("across batch promotion")) {
                    if (recommended_post_grp.equals("A")) {
                        deptDistrictWiseNominationAB.setPendingatcollectorateA(rs.getInt("cnt"));
                    } else if (recommended_post_grp.equals("B")) {
                        deptDistrictWiseNominationAB.setPendingatcollectorateB(rs.getInt("cnt"));
                    } else if (recommended_post_grp.equals("C")) {
                        deptDistrictWiseNominationAB.setPendingatcollectorateC(rs.getInt("cnt"));
                    } else if (recommended_post_grp.equals("D")) {
                        deptDistrictWiseNominationAB.setPendingatcollectorateD(rs.getInt("cnt"));
                    }

                } else if (recommendation_type.equals("incentives award")) {
                    if (recommended_post_grp.equals("A")) {
                        deptDistrictWiseNominationIA.setPendingatcollectorateA(rs.getInt("cnt"));
                    } else if (recommended_post_grp.equals("B")) {
                        deptDistrictWiseNominationIA.setPendingatcollectorateB(rs.getInt("cnt"));
                    } else if (recommended_post_grp.equals("C")) {
                        deptDistrictWiseNominationIA.setPendingatcollectorateC(rs.getInt("cnt"));
                    } else if (recommended_post_grp.equals("D")) {
                        deptDistrictWiseNominationIA.setPendingatcollectorateD(rs.getInt("cnt"));
                    }
                } else if (recommendation_type.equals("Premature Retirement")) {
                    if (recommended_post_grp.equals("A")) {
                        deptDistrictWiseNominationPR.setPendingatcollectorateA(rs.getInt("cnt"));
                    } else if (recommended_post_grp.equals("B")) {
                        deptDistrictWiseNominationPR.setPendingatcollectorateB(rs.getInt("cnt"));
                    } else if (recommended_post_grp.equals("C")) {
                        deptDistrictWiseNominationPR.setPendingatcollectorateC(rs.getInt("cnt"));
                    } else if (recommended_post_grp.equals("D")) {
                        deptDistrictWiseNominationPR.setPendingatcollectorateD(rs.getInt("cnt"));
                    }
                } else if (recommendation_type.equals("within batch promotion")) {
                    if (recommended_post_grp.equals("A")) {
                        deptDistrictWiseNominationWB.setPendingatcollectorateA(rs.getInt("cnt"));
                    } else if (recommended_post_grp.equals("B")) {
                        deptDistrictWiseNominationWB.setPendingatcollectorateB(rs.getInt("cnt"));
                    } else if (recommended_post_grp.equals("C")) {
                        deptDistrictWiseNominationWB.setPendingatcollectorateC(rs.getInt("cnt"));
                    } else if (recommended_post_grp.equals("D")) {
                        deptDistrictWiseNominationWB.setPendingatcollectorateD(rs.getInt("cnt"));
                    }
                }
            }
            recommendationList.put("deptDistrictWiseNominationAB", deptDistrictWiseNominationAB);
            recommendationList.put("deptDistrictWiseNominationIA", deptDistrictWiseNominationIA);
            recommendationList.put("deptDistrictWiseNominationPR", deptDistrictWiseNominationPR);
            recommendationList.put("deptDistrictWiseNominationWB", deptDistrictWiseNominationWB);
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return recommendationList;
    }

    @Override
    public ArrayList getRecommendationListDepartmentWise(List deptList) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmthod = null;
        PreparedStatement pstmtcollec = null;
        ResultSet rs = null;
        ResultSet rshod = null;
        ResultSet rscollec = null;
        ArrayList recommendationList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT count(*) cnt FROM recommendation_details \n"
                    + " INNER JOIN recommendation_master ON  recommendation_details.recommendation_id = recommendation_master.recommendation_id  \n"
                    + " INNER JOIN emp_mast ON  recommendation_details.recommended_empid = emp_mast.emp_id  \n"
                    + " INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC \n"
                    + " INNER JOIN G_OFFICE ON emp_mast.CUR_OFF_CODE = G_OFFICE.OFF_CODE \n"
                    + " INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE \n"
                    + " WHERE dept_code=? and is_submitted_dept='Y'");
            pstmthod = con.prepareStatement("select recommended_post_grp,count(*) cnt from recommendation_master "
                    + "inner join recommendation_details on recommendation_master.recommendation_id = recommendation_details.recommendation_id "
                    + "where submitted_to_off_type = 'H' and recommended_dept_code=? group by recommended_post_grp");
            pstmtcollec = con.prepareStatement("select recommended_post_grp, count(*) cnt from recommendation_master "
                    + "inner join recommendation_details on recommendation_master.recommendation_id = recommendation_details.recommendation_id "
                    + "where submitted_to_off_type = 'C' and recommended_dept_code=? group by recommended_post_grp");
            for (int i = 0; i < deptList.size(); i++) {
                Department department = (Department) deptList.get(i);
                int deptWiseNoofnomination = 0;
                int hodWiseNoofnominationA = 0;
                int hodWiseNoofnominationB = 0;
                int hodWiseNoofnominationC = 0;
                int hodWiseNoofnominationD = 0;
                int collecWiseNoofnominationA = 0;
                int collecWiseNoofnominationB = 0;
                int collecWiseNoofnominationC = 0;
                int collecWiseNoofnominationD = 0;
                /**/
                pstmt.setString(1, department.getDeptCode());
                rs = pstmt.executeQuery();

                pstmthod.setString(1, department.getDeptCode());
                rshod = pstmthod.executeQuery();

                pstmtcollec.setString(1, department.getDeptCode());
                rscollec = pstmtcollec.executeQuery();
                System.out.println(department.getDeptCode());
                if (rs.next()) {
                    deptWiseNoofnomination = rs.getInt("cnt");
                }
                while (rshod.next()) {
                    if (rshod.getString("recommended_post_grp").equals("A")) {
                        hodWiseNoofnominationA = rshod.getInt("cnt");
                    } else if (rshod.getString("recommended_post_grp").equals("B")) {
                        hodWiseNoofnominationB = rshod.getInt("cnt");
                    } else if (rshod.getString("recommended_post_grp").equals("C")) {
                        hodWiseNoofnominationC = rshod.getInt("cnt");
                    } else if (rshod.getString("recommended_post_grp").equals("D")) {
                        hodWiseNoofnominationD = rshod.getInt("cnt");
                    }
                }
                while (rscollec.next()) {
                    if (rscollec.getString("recommended_post_grp").equals("A")) {
                        collecWiseNoofnominationA = rscollec.getInt("cnt");
                    } else if (rscollec.getString("recommended_post_grp").equals("B")) {
                        collecWiseNoofnominationB = rscollec.getInt("cnt");
                    } else if (rscollec.getString("recommended_post_grp").equals("C")) {
                        collecWiseNoofnominationC = rscollec.getInt("cnt");
                    } else if (rscollec.getString("recommended_post_grp").equals("D")) {
                        collecWiseNoofnominationD = rscollec.getInt("cnt");
                    }
                }
                DeptDistrictWiseNomination deptDistrictWiseNomination = new DeptDistrictWiseNomination();
                deptDistrictWiseNomination.setDeptCode(department.getDeptCode());
                deptDistrictWiseNomination.setDeptName(department.getDeptName());
                deptDistrictWiseNomination.setReceivedatdepartment(deptWiseNoofnomination);
                deptDistrictWiseNomination.setPendingatcollectorateA(collecWiseNoofnominationA);
                deptDistrictWiseNomination.setPendingatcollectorateB(collecWiseNoofnominationB);
                deptDistrictWiseNomination.setPendingatcollectorateC(collecWiseNoofnominationC);
                deptDistrictWiseNomination.setPendingatcollectorateD(collecWiseNoofnominationD);
                deptDistrictWiseNomination.setPendingathodA(hodWiseNoofnominationA);
                deptDistrictWiseNomination.setPendingathodB(hodWiseNoofnominationB);
                deptDistrictWiseNomination.setPendingathodC(hodWiseNoofnominationC);
                deptDistrictWiseNomination.setPendingathodD(hodWiseNoofnominationD);
                recommendationList.add(deptDistrictWiseNomination);
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rscollec, pstmtcollec);
            DataBaseFunctions.closeSqlObjects(rshod, pstmthod);
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return recommendationList;
    }

    @Override
    public ArrayList getRecommendeList(int recommendationId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList recommendationDetail = new ArrayList();

        try {
            con = this.dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT recommended_empid,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,recommended_spc,POST FROM recommendation_details "
                    + "INNER JOIN emp_mast ON  recommendation_details.recommended_empid = emp_mast.emp_id  "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "WHERE recommendation_id=?");

            pstmt.setInt(1, recommendationId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                RecommendationDetailBean recommendationDetailBean = new RecommendationDetailBean();
                recommendationDetailBean.setRecommendationId(recommendationId);
                recommendationDetailBean.setRecommendedempId((rs.getString("recommended_empid")));
                recommendationDetailBean.setRecommendedempname((rs.getString("EMP_NAME")));
                recommendationDetailBean.setRecommendedspc(rs.getString("recommended_spc"));
                recommendationDetailBean.setRecommendedpost(rs.getString("POST"));
                recommendationDetail.add(recommendationDetailBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return recommendationDetail;
    }

    @Override
    public ArrayList getRecommendedEmployeeList(int taskId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList recommendationDetailList = new ArrayList();

        try {
            con = this.dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT recommendation_master.recommendation_id,recommended_detail_id,recommended_empid,"
                    + "ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,recommended_spc,POST,GPF_NO,is_approved FROM recommendation_details "
                    + "INNER JOIN recommendation_master ON  recommendation_details.recommendation_id = recommendation_master.recommendation_id  "
                    + "INNER JOIN emp_mast ON  recommendation_details.recommended_empid = emp_mast.emp_id  "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "WHERE task_id=?");

            pstmt.setInt(1, taskId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                RecommendationDetailBean recommendationDetailBean = new RecommendationDetailBean();
                recommendationDetailBean.setRecommendationId(rs.getInt("recommendation_id"));
                recommendationDetailBean.setRecommendeddetailId(rs.getInt("recommended_detail_id"));
                recommendationDetailBean.setRecommendedempId((rs.getString("recommended_empid")));
                recommendationDetailBean.setRecommendedempname((rs.getString("EMP_NAME")));
                recommendationDetailBean.setRecommendedspc(rs.getString("recommended_spc"));
                recommendationDetailBean.setRecommendedpost(rs.getString("POST"));
                recommendationDetailBean.setRecommendedempGpfNo(rs.getString("GPF_NO"));
                recommendationDetailBean.setIsApproved(rs.getString("is_approved"));
                recommendationDetailList.add(recommendationDetailBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return recommendationDetailList;
    }

    @Override
    public void submitRecommendationListToDepartment(int recommendationId, String submittedBy, String submittedBySpc) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            con = this.dataSource.getConnection();
            /*pst = con.prepareStatement("UPDATE recommendation_details set is_approved='R' where recommendation_id=?");
             pst.setInt(1, recommendationId);
             pst.executeUpdate();*/
            pst = con.prepareStatement("UPDATE recommendation_master SET submited_on_date=?,is_submitted_dept='Y',"
                    + "submitted_on_date_dept=?,submitted_dept_by=?,submitted_dept_by_spc=? where recommendation_id=?");
            pst.setTimestamp(1, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setTimestamp(2, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(3, submittedBy);
            pst.setString(4, submittedBySpc);
            pst.setInt(5, recommendationId);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
    }

    @Override
    public void submitRecommendationList(int recommendationId, String pendingAt, String pendingSPC) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String submitted_to_off_type = null;
        String submitted_to_off_code = null;
        try {
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            RecommendationDetailBean recommendationDetailBean = getRecommendationDetailFromRecommendeddetailId(recommendationId);
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select g_spc.off_code,lvl,category from g_spc "
                    + "inner join g_office on g_office.off_code = g_spc.off_code "
                    + "where spc=?");
            pst.setString(1, pendingSPC);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("category").equals("DISTRICT COLLECTORATE")) {
                    submitted_to_off_type = "C";
                } else if (rs.getString("lvl").equals("02")) {
                    submitted_to_off_type = "H";
                }
                submitted_to_off_code = rs.getString("off_code");
            }
            pst = con.prepareStatement("INSERT INTO TASK_MASTER(PROCESS_ID, INITIATED_BY, INITIATED_ON, STATUS_ID, PENDING_AT,INITIATED_SPC,PENDING_SPC,ref_id) Values (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, 21);//In g_workflow_process for Recommendation
            pst.setString(2, recommendationDetailBean.getInitiatedByempId());
            pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setInt(4, 111);//IN G_PROCESS_STATUS FOR Recommendation
            pst.setString(5, pendingAt);
            pst.setString(6, recommendationDetailBean.getInitiatedByspc());
            pst.setString(7, pendingSPC);
            pst.setInt(8, recommendationDetailBean.getRecommendationId());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            rs.next();
            int taskId = rs.getInt("TASK_ID");
            System.out.println(taskId + "****" + recommendationDetailBean.getRecommendationId());
            pst = con.prepareStatement("UPDATE recommendation_master SET submited_on_date=?,task_id=?, submitted_to_off_type=?, submitted_to_off_code=?, "
                    + "submitted_to_empid=?, submitted_to_emp_spc=? where recommendation_id=?");
            pst.setTimestamp(1, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setInt(2, taskId);
            pst.setString(3, submitted_to_off_type);
            pst.setString(4, submitted_to_off_code);
            pst.setString(5, pendingAt);
            pst.setString(6, pendingSPC);
            pst.setInt(7, recommendationDetailBean.getRecommendationId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
    }

    @Override
    public String checkRecommendationData(int recommendationId) {
        String dataIntegrity = "Y";
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select count(*) as cnt from recommendation_details where recommendation_details.recommendation_id=? "
                    + "and coalesce(reason_for_recommendation, '') = '' ");
            pst.setInt(1, recommendationId);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    dataIntegrity = "N";
                }
            }
            //select reason_for_recommendation from recommendation_details where recommendation_details.recommendation_id=
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
        return dataIntegrity;
    }

    @Override
    public DepartmentPromotionDetail getPARList(String empId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean flag = false;
        DepartmentPromotionDetail dpcdetail = new DepartmentPromotionDetail();
        try {
            con = dataSource.getConnection();
            int fyearfrom = Integer.parseInt("2015-16".substring(0, 4));//2018-19
            int fyearto = Integer.parseInt("2019-20".substring(0, 4));//2018-19
            String fiscalYearStr = "";

            dpcdetail.setHrmsId(empId);
            ArrayList nrcFiscalYearList = new ArrayList();
            for (int i = fyearfrom; i <= fyearto; i++) {
                String tfiscalYearStr = ((i + 1) + "").substring(2, 4);
                fiscalYearStr = i + "-" + tfiscalYearStr;

                ArrayList yearwisePardata = getYearWiseParData(fiscalYearStr, dpcdetail.getHrmsId());
                //if (yearwisePardata.size() > 0) {
                FiscalYearWiseParData nrcfy = new FiscalYearWiseParData();
                nrcfy.setFy(fiscalYearStr);
                nrcfy.setYearwisedata(yearwisePardata);
                nrcFiscalYearList.add(nrcfy);
                //}

            }
            dpcdetail.setFiscalYearList(nrcFiscalYearList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
        return dpcdetail;
    }

    public ArrayList getYearWiseParData(String fiscalYear, String empId) {
        ArrayList<FiscalYearWiseParData.Parabstractdata> yearwisePardata = new ArrayList();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        FiscalYearWiseParData f = new FiscalYearWiseParData();
        boolean dataPresent = false;
        try {
            Collections.sort(yearwisePardata);
            String fromFiscalYear = fiscalYear.substring(0, 4);
            con = dataSource.getConnection();
            /*NRC Data*/
            pst = con.prepareStatement("select parid,period_from,period_to,par_status,reason,is_reviewed from par_master "
                    + " LEFT OUTER JOIN g_par_nrc_reason ON par_master.nrcreason = g_par_nrc_reason.reason_id "
                    + " where emp_id=? and fiscal_year=? AND par_status='17'");

            pst.setString(1, empId);
            pst.setString(2, fiscalYear);
            rs = pst.executeQuery();
            while (rs.next()) {
                dataPresent = true;
                FiscalYearWiseParData.Parabstractdata parabstractdata = f.new Parabstractdata();
                parabstractdata.setParid(rs.getInt("parid"));
                parabstractdata.setPeriodfrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_FROM")));
                parabstractdata.setPeriodto(CommonFunctions.getFormattedOutputDate2(rs.getDate("PERIOD_TO")));
                parabstractdata.setParstatus(rs.getInt("par_status"));
                parabstractdata.setRemark(rs.getString("reason"));
                parabstractdata.setIsreviewed(rs.getString("is_reviewed"));
                yearwisePardata.add(parabstractdata);
            }
            /*NRC Data*/
            /*PAR Data*/
            pst = con.prepareStatement("select parid,period_from,period_to,par_status,is_reviewed,getpargradename(grade_id) as reporting_grade,getpargradename(overallgrading) as reviewing_grade from par_master "
                    + "LEFT OUTER JOIN (SELECT par_id,grade_id FROM par_reporting_tran WHERE IS_COMPLETED='Y' AND HIERARCHY_NO=1) AS par_reporting_tran on par_master.parid = par_reporting_tran.par_id  "
                    + "LEFT OUTER JOIN (SELECT par_id,overallgrading FROM par_reviewing_tran WHERE IS_COMPLETED='Y' AND HIERARCHY_NO=1) AS par_reviewing_tran on par_master.parid = par_reviewing_tran.par_id "
                    + "where emp_id=? and fiscal_year=? AND par_status != 17");

            pst.setString(1, empId);
            pst.setString(2, fiscalYear);
            rs = pst.executeQuery();

            while (rs.next()) {
                dataPresent = true;
                FiscalYearWiseParData.Parabstractdata parabstractdata = f.new Parabstractdata();
                parabstractdata.setParid(rs.getInt("parid"));
                parabstractdata.setPeriodfrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_FROM")));
                parabstractdata.setPeriodto(CommonFunctions.getFormattedOutputDate2(rs.getDate("PERIOD_TO")));
                parabstractdata.setParstatus(rs.getInt("par_status"));
                parabstractdata.setRemark("");
                parabstractdata.setIsreviewed(rs.getString("is_reviewed"));
                parabstractdata.setGradeName(rs.getString("reporting_grade"));
                if (rs.getString("reviewing_grade") != null && !rs.getString("reviewing_grade").equals("")) {
                    parabstractdata.setGradeName(rs.getString("reviewing_grade"));
                }

                yearwisePardata.add(parabstractdata);
            }
            /*PAR Data*/

            /*Get Period where PAR or NRC is not Present*/
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date fromDate = sdf.parse("01/04/" + fromFiscalYear);
            for (int i = 0; i < yearwisePardata.size(); i++) {
                FiscalYearWiseParData.Parabstractdata parabstractdata = yearwisePardata.get(i);
                Date date1 = CommonFunctions.getDateFromString(parabstractdata.getPeriodfrom(), "dd-MMM-yyyy");
                if (date1.compareTo(fromDate) > 0 && i == 0) {
                    date1 = DateUtils.addDays(date1, -1);
                    /*Calculat no of Days*/
                    long difference = date1.getTime() - fromDate.getTime();
                    float daysBetween = (difference / (1000 * 60 * 60 * 24));
                    FiscalYearWiseParData.Parabstractdata wantingparabstractdata = f.new Parabstractdata();
                    wantingparabstractdata.setParid(0);
                    wantingparabstractdata.setPeriodfrom(CommonFunctions.getFormattedOutputDate1(fromDate));
                    wantingparabstractdata.setPeriodto(CommonFunctions.getFormattedOutputDate2(date1));
                    if (daysBetween <= 120) {
                        wantingparabstractdata.setGrade("NRC");
                    } else {
                        wantingparabstractdata.setGrade("Not Initiated");
                    }
                    wantingparabstractdata.setRemark("");
                    wantingparabstractdata.setIsreviewed("");
                    wantingparabstractdata.setGradeName("");
                    //yearwisePardata.add(wantingparabstractdata);
                } else if (date1.compareTo(fromDate) < 0) {

                } else {

                }
            }
            /*PAR Not Present*/
            if (!dataPresent) {
                FiscalYearWiseParData.Parabstractdata parabstractdata = f.new Parabstractdata();
                parabstractdata.setParid(0);
                String fromdate = "01/04/" + fromFiscalYear;
                String todate = "31/03/" + (Integer.parseInt(fromFiscalYear) + 1);
                parabstractdata.setPeriodfrom(CommonFunctions.getFormattedOutputDate2(CommonFunctions.getDateFromString(fromdate, "dd/MM/yyyy")));
                parabstractdata.setPeriodto(CommonFunctions.getFormattedOutputDate2(CommonFunctions.getDateFromString(todate, "dd/MM/yyyy")));
                parabstractdata.setGrade("Not initiated");
                parabstractdata.setRemark("");
                parabstractdata.setIsreviewed("");
                parabstractdata.setGradeName("");
                yearwisePardata.add(parabstractdata);
            }
            /*PAR Not Present*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
        Collections.sort(yearwisePardata);
        return yearwisePardata;
    }
}
