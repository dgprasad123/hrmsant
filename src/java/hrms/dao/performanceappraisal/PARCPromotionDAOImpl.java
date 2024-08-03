/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.performanceappraisal;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.SMSThread;
import hrms.model.parmast.GroupCAdminAdminSearchCriteria;
import hrms.model.parmast.GroupCCustodianCommunication;
import hrms.model.parmast.GroupCEmployee;
import hrms.model.parmast.GroupCInitiatedbean;
import hrms.model.parmast.GroupCSearchResult;
import hrms.model.parmast.GroupCStatus;
import hrms.model.parmast.ParAdverseCommunicationDetail;
import hrms.model.parmast.ParApplyForm;
import hrms.model.parmast.ParAssignPrivilage;
import hrms.model.parmast.Parauthorityhelperbean;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
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
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Manisha
 */
public class PARCPromotionDAOImpl implements PARCPromotionDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    protected DataSource repodataSource;

    private String uploadPath;

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    @Override
    public List getGroupCEmployeeList(String offCode, int groupcPromotionid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList emplist = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,emp_id,cur_spc,POST,gpf_no,reviewed_empid from emp_mast "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "LEFT OUTER JOIN (SELECT reviewed_empid FROM parc_promotion WHERE groupc_promotionid=?) AS parc_promotion ON emp_mast.emp_id = parc_promotion.reviewed_empid "
                    + "where cur_off_code=? and post_grp_type='C' and dep_code='02'  ORDER BY EMPNAME");
            pstmt.setInt(1, groupcPromotionid);
            pstmt.setString(2, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                GroupCEmployee groupCEmployee = new GroupCEmployee();
                groupCEmployee.setReviewedempname(rs.getString("EMPNAME"));
                groupCEmployee.setReviewedempId(rs.getString("emp_id"));
                groupCEmployee.setReviewedspc(rs.getString("cur_spc"));
                groupCEmployee.setReviewedpost(rs.getString("POST"));
                groupCEmployee.setGpfno(rs.getString("gpf_no"));
                if (rs.getString("reviewed_empid") != null && !rs.getString("reviewed_empid").equals("")) {
                    groupCEmployee.setAlreadyAdded("Y");
                } else {
                    groupCEmployee.setAlreadyAdded("N");
                }
                emplist.add(groupCEmployee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return emplist;
    }

    @Override
    public void savegroupCEmpList(GroupCEmployee groupCEmployee) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            Long curtime = new Date().getTime();
            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO parc_promotion(reviewed_empid,reviewed_spc,reporting_ondate,is_fitfor_shouldering_responsibility_reporting,reporting_empid,reporting_spc,groupc_promotionid) VALUES(?,?,?,?,?,?,?)");
            pst.setString(1, groupCEmployee.getReviewedempId());
            pst.setString(2, groupCEmployee.getReviewedspc());
            pst.setTimestamp(3, new Timestamp(curtime));
            pst.setString(4, "Y");
            pst.setString(5, groupCEmployee.getReportingempId());
            pst.setString(6, groupCEmployee.getReportingspc());
            pst.setInt(7, groupCEmployee.getGroupCpromotionId());
            /*  pst.setString(8, groupCEmployee.getAssessmentTypeReporting());

             if (groupCEmployee.getAssessmentTypeReporting().equals("fullPeriod")) {
             pst.setTimestamp(9, new Timestamp(sdf.parse("01-APR-2022").getTime()));
             System.out.println("groupCEmployee.getPeriodFromReporting() ****");
             } else {
             System.out.println("groupCEmployee.getPeriodFromReporting()" + groupCEmployee.getPeriodFromReporting());
             pst.setTimestamp(9, new Timestamp(sdf.parse(groupCEmployee.getPeriodFromReporting()).getTime()));

             }
             if (groupCEmployee.getAssessmentTypeReporting().equals("fullPeriod")) {
             pst.setTimestamp(10, new Timestamp(sdf.parse("31-MAR-2023").getTime()));
             System.out.println(" groupCEmployee.getPeriodToReporting() ***");
             } else {
             System.out.println("groupCEmployee.getPeriodToReporting()" + groupCEmployee.getPeriodToReporting());
             pst.setTimestamp(10, new Timestamp(sdf.parse(groupCEmployee.getPeriodToReporting()).getTime()));
             }*/

            

            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateGroupCpromotionRemark(GroupCEmployee groupCEmployee) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            Long curtime = new Date().getTime();
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE parc_promotion SET reporting_ondate = ?,is_fitfor_shouldering_responsibility_reporting = ? WHERE reporting_empid=? AND reporting_spc = ? AND reviewed_empid = ? AND reviewed_spc = ?");
            pst.setTimestamp(1, new Timestamp(curtime));
            pst.setString(2, groupCEmployee.getIsfitforShoulderingResponsibilityReporting());
            pst.setString(3, groupCEmployee.getReportingempId());
            pst.setString(4, groupCEmployee.getReportingspc());
            pst.setString(5, groupCEmployee.getReviewedempId());
            pst.setString(6, groupCEmployee.getReviewedspc());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getSelectedgroupCEmpList(int groupcPromotionId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList empList = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("Select groupc_promotion.groupc_promotionid,promotion_id,reviewed_empid,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,reviewed_spc,POST,is_fitfor_shouldering_responsibility_reporting,"
                    + "reporting_remarks,get_empname_from_type(pending_at,'G') as pending_at,disk_file_name,org_file_name, is_fitfor_shouldering_responsibility_reviewing, reviewing_remarks, is_fitfor_shouldering_responsibility_accepting, accepting_remarks,"
                    + "get_empname_from_type(parc_promotion.reporting_empid ,'G') as reportingname,getspn(parc_promotion.reporting_spc) as reportingspc,fiscal_year,get_empname_from_type(parc_promotion.reviewing_empid ,'G') as reviewingname,"
                    + "getspn(parc_promotion.reviewing_spc) as reviewingspc,from_date_reporting,to_date_reporting,status_id from parc_promotion "
                    + "INNER JOIN emp_mast ON  parc_promotion.reviewed_empid = emp_mast.emp_id "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "INNER JOIN groupc_promotion ON groupc_promotion.groupc_promotionid = parc_promotion.groupc_promotionid where groupc_promotion.groupc_promotionid = ? ");
            pstmt.setInt(1, groupcPromotionId);

            rs = pstmt.executeQuery();
            while (rs.next()) {

                GroupCEmployee groupCEmployee = new GroupCEmployee();
                groupCEmployee.setMode("");
                groupCEmployee.setGroupCpromotionId(rs.getInt("groupc_promotionid"));
                groupCEmployee.setPromotionId(rs.getInt("promotion_id"));
                groupCEmployee.setReviewedempId(rs.getString("reviewed_empid"));
                groupCEmployee.setReviewedempname(rs.getString("EMP_NAME"));
                groupCEmployee.setReviewedspc(rs.getString("reviewed_spc"));
                groupCEmployee.setReviewedpost(rs.getString("POST"));
                groupCEmployee.setIsfitforShoulderingResponsibilityReporting(rs.getString("is_fitfor_shouldering_responsibility_reporting"));
                groupCEmployee.setReportingRemarks(rs.getString("reporting_remarks"));
                groupCEmployee.setPendingAtEmpId(rs.getString("pending_at"));
                groupCEmployee.setDiskFileName(rs.getString("disk_file_name"));
                groupCEmployee.setOriginalFilename(rs.getString("org_file_name"));
                groupCEmployee.setIsfitforShoulderingResponsibilityReviewing(rs.getString("is_fitfor_shouldering_responsibility_reviewing"));
                groupCEmployee.setReviewingRemarks(rs.getString("reviewing_remarks"));
                groupCEmployee.setIsfitforShoulderingResponsibilityAccepting(rs.getString("is_fitfor_shouldering_responsibility_accepting"));
                groupCEmployee.setAcceptingRemarks(rs.getString("accepting_remarks"));
                groupCEmployee.setReportingempname(rs.getString("reportingname"));
                groupCEmployee.setReportingpost(rs.getString("reportingspc"));
                groupCEmployee.setFiscalyear(rs.getString("fiscal_year"));
                groupCEmployee.setReviewingempname(rs.getString("reviewingname"));
                groupCEmployee.setReviewingpost(rs.getString("reviewingspc"));
                groupCEmployee.setPeriodFromReporting(CommonFunctions.getFormattedOutputDate1(rs.getDate("from_date_reporting")));
                groupCEmployee.setPeriodToReporting(CommonFunctions.getFormattedOutputDate1(rs.getDate("to_date_reporting")));
                groupCEmployee.setStatusId(rs.getInt("status_id"));
                /*if (CommonFunctions.getFormattedOutputDate1(rs.getDate("from_date_reporting")) == null || CommonFunctions.getFormattedOutputDate1(rs.getDate("from_date_reporting")).equals("")) {
                 System.out.println("11111");
                 groupCEmployee.setPeriodFromReporting("01-04-2022");
                 } else {
                 groupCEmployee.setPeriodFromReporting(CommonFunctions.getFormattedOutputDate1(rs.getDate("from_date_reporting")));
                 System.out.println("CommonFunctions.getFormattedOutputDate1(rs.getDate(\"from_date_reporting\"))" + CommonFunctions.getFormattedOutputDate1(rs.getDate("from_date_reporting")));
                 } 
                 if (CommonFunctions.getFormattedOutputDate1(rs.getDate("to_date_reporting")) == null || CommonFunctions.getFormattedOutputDate1(rs.getDate("to_date_reporting")).equals("")) {
                 System.out.println("2222");
                 groupCEmployee.setPeriodToReporting("31-03-2023");
                 } else {
                 groupCEmployee.setPeriodToReporting(CommonFunctions.getFormattedOutputDate1(rs.getDate("to_date_reporting")));
                 System.out.println("CommonFunctions.getFormattedOutputDate1(rs.getDate(\"to_date_reporting\"))" + CommonFunctions.getFormattedOutputDate1(rs.getDate("to_date_reporting")));
                 }*/

                empList.add(groupCEmployee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return empList;
    }

    @Override
    public ArrayList getSelectedFitForPronotionCEmpList(String reviewingEmpId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList fitForPronotionempList = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("Select reviewed_empid,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,reviewed_spc,POST,is_fitfor_shouldering_responsibility_reporting from parc_promotion "
                    + " inner join emp_mast ON  parc_promotion.reviewed_empid = emp_mast.emp_id "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "where reviewing_empid = ? and is_fitfor_shouldering_responsibility_reporting='Y'");
            pstmt.setString(1, reviewingEmpId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                GroupCEmployee groupCEmployee = new GroupCEmployee();
                groupCEmployee.setReviewedempId(rs.getString("reviewed_empid"));
                groupCEmployee.setReviewedempname(rs.getString("EMP_NAME"));
                groupCEmployee.setReviewedspc(rs.getString("reviewed_spc"));
                groupCEmployee.setReviewedpost(rs.getString("POST"));
                groupCEmployee.setIsfitforShoulderingResponsibilityReporting(rs.getString("is_fitfor_shouldering_responsibility_reporting"));
                fitForPronotionempList.add(groupCEmployee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return fitForPronotionempList;
    }

    @Override
    public void saveremarksForGroupC(GroupCEmployee groupCEmployee) {
        String diskfileName = null;
        PreparedStatement pst = null;
        Connection con = null;
        String originalFileName = null;
        String contentType = null;
        try {
            if (!groupCEmployee.getUploadDocument().isEmpty()) {
                diskfileName = new Date().getTime() + "";
                originalFileName = groupCEmployee.getUploadDocument().getOriginalFilename();
                contentType = groupCEmployee.getUploadDocument().getContentType();
                byte[] bytes = groupCEmployee.getUploadDocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + diskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            con = dataSource.getConnection();

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            pst = con.prepareStatement("UPDATE parc_promotion SET reporting_empid=?,reporting_spc=?,reporting_remarks=?,disk_file_name=?,org_file_name=?,file_type=?,file_path=?,is_fitfor_shouldering_responsibility_reporting='N' WHERE promotion_id=?");
            pst.setString(1, groupCEmployee.getReportingempId());
            pst.setString(2, groupCEmployee.getReportingspc());
            pst.setString(3, groupCEmployee.getReportingRemarks());
            pst.setString(4, diskfileName);
            pst.setString(5, originalFileName);
            pst.setString(6, contentType);
            pst.setString(7, this.uploadPath);

            pst.setInt(8, groupCEmployee.getPromotionId());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public GroupCEmployee getremarksForGroupC(GroupCEmployee groupCEmployee) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select reporting_remarks,disk_file_name,org_file_name,is_fitfor_shouldering_responsibility_reporting from parc_promotion where promotion_id=?");
            pst.setInt(1, groupCEmployee.getPromotionId());
            rs = pst.executeQuery();
            if (rs.next()) {
                groupCEmployee.setReportingRemarks(rs.getString("reporting_remarks"));
                groupCEmployee.setDiskFileName(rs.getString("disk_file_name"));
                groupCEmployee.setOriginalFilename(rs.getString("org_file_name"));
                groupCEmployee.setIsfitforShoulderingResponsibilityReporting(rs.getString("is_fitfor_shouldering_responsibility_reporting"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return groupCEmployee;
    }

    @Override
    public void deleteremarksForGroupC(GroupCEmployee groupCEmployee) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("update parc_promotion set is_fitfor_shouldering_responsibility_reporting='Y', reporting_remarks = null, disk_file_name=null,org_file_name=null,file_type=null WHERE promotion_id=?");
            pst.setInt(1, groupCEmployee.getPromotionId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void reviewingRemarkFitForPromotion(GroupCEmployee groupCEmployee) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            con = dataSource.getConnection();
            pst = con.prepareStatement("update parc_promotion set is_fitfor_shouldering_responsibility_reviewing='Y',reviewing_empid=?,reviewing_spc=?,reviewing_ondate=?, reviewing_remarks = ? WHERE promotion_id=?");
            pst.setString(1, groupCEmployee.getReviewingempId());
            pst.setString(2, groupCEmployee.getReviewingspc());
            pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(4, groupCEmployee.getReportingRemarks());
            pst.setInt(5, groupCEmployee.getPromotionId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public boolean isReviewingGivesAllRemark(String offCode, int groupcPromotionid) {
        boolean isReviewingGivesAllRemark = true;
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select count(*) as cnt from parc_promotion where groupc_promotionid=? and is_fitfor_shouldering_responsibility_reviewing is null");
            pst.setInt(1, groupcPromotionid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    isReviewingGivesAllRemark = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return isReviewingGivesAllRemark;
    }

    @Override
    public boolean isReortingSaveAllDate(String offCode, int groupcPromotionid) {
        boolean isReortingSaveAllDate = true;
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select count(*) as cnt from parc_promotion where groupc_promotionid=? and from_date_reporting is null and to_date_reporting is null");
            pst.setInt(1, groupcPromotionid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    isReortingSaveAllDate = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return isReortingSaveAllDate;
    }

    @Override
    public boolean isReviewingSaveAllDate(String offCode, int groupcPromotionid) {
        boolean isReviewingSaveAllDate = true;
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select count(*) as cnt from parc_promotion where groupc_promotionid=? and is_fitfor_shouldering_responsibility_reviewing is null");
            pst.setInt(1, groupcPromotionid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    isReviewingSaveAllDate = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return isReviewingSaveAllDate;
    }

    @Override
    public boolean isAcceptingGivesAllRemark(String offCode, int groupcPromotionid) {
        boolean isAcceptingGivesAllRemark = true;
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select count(*) as cnt from parc_promotion where groupc_promotionid=? and is_fitfor_shouldering_responsibility_accepting is null");
            pst.setInt(1, groupcPromotionid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    isAcceptingGivesAllRemark = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return isAcceptingGivesAllRemark;
    }

    @Override
    public void reviewingRemarkNotFitForPromotion(GroupCEmployee groupCEmployee) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            con = dataSource.getConnection();
            pst = con.prepareStatement("update parc_promotion set is_fitfor_shouldering_responsibility_reviewing='N', reviewing_empid=?,reviewing_spc=?,reviewing_ondate=?, reviewing_remarks = ? WHERE promotion_id=?");
            pst.setString(1, groupCEmployee.getReviewingempId());
            pst.setString(2, groupCEmployee.getReviewingspc());
            pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(4, groupCEmployee.getReportingRemarks());
            pst.setInt(5, groupCEmployee.getPromotionId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList gethigherAuthorityList(String deptcode, String offCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList authoritylist = new ArrayList();
        GroupCEmployee groupCEmployee = null;

        try {
            con = this.dataSource.getConnection();
            if (deptcode != null && deptcode.equalsIgnoreCase("LM")) {
                //pstmt = con.prepareStatement("SELECT LMID,ARRAY_TO_STRING(ARRAY[INITIALS, FNAME, MNAME,LNAME], ' ') EMPNAME,OFF_AS FROM LA_MEMBERS WHERE ACTIVE='Y' AND OFF_AS=?");
                pstmt = con.prepareStatement("SELECT LMID,ARRAY_TO_STRING(ARRAY[emp_mast.INITIALS, FNAME, MNAME,LNAME], ' ') EMPNAME,OFF_AS,USER_DETAILS.userid,linkid FROM LA_MEMBERS "
                        + "left outer join USER_DETAILS on LA_MEMBERS.userid = USER_DETAILS.username "
                        + "left outer join emp_mast on USER_DETAILS.linkid = emp_mast.emp_id "
                        + "WHERE ACTIVE='Y' AND OFF_AS=?");
                pstmt.setString(1, offCode);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    groupCEmployee = new GroupCEmployee();
                    groupCEmployee.setReportingempname(rs.getString("EMPNAME"));
                    groupCEmployee.setReportingpost(getAssignedDepartmentForGroupC(rs.getString("LMID"), offCode));
                    groupCEmployee.setReportingempId(rs.getString("LMID"));
                    //groupCEmployee.setReportingspc(rs.getString("cur_spc"));
                    authoritylist.add(groupCEmployee);
                }
            } else {
                pstmt = con.prepareStatement("select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,emp_id,cur_spc,POST,post_group_type from emp_mast "
                        + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                        + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE where cur_off_code=? and isauthority = 'Y' and dep_code='02'");
                pstmt.setString(1, offCode);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    groupCEmployee = new GroupCEmployee();
                    groupCEmployee.setReportingempname(rs.getString("EMPNAME"));
                    groupCEmployee.setReportingempId(rs.getString("emp_id"));
                    groupCEmployee.setReportingspc(rs.getString("cur_spc"));
                    groupCEmployee.setReportingpost(rs.getString("POST"));
                    groupCEmployee.setGroupTypeOfAuthority(rs.getString("post_group_type"));
                    authoritylist.add(groupCEmployee);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return authoritylist;
    }

    @Override
    public void forwardToAdverseApprisee(GroupCEmployee groupCEmployee) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        Connection con = null;
        try {
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            con = dataSource.getConnection();
            pst1 = con.prepareStatement("INSERT INTO TASK_MASTER(PROCESS_ID, INITIATED_BY, INITIATED_ON, STATUS_ID, PENDING_AT,INITIATED_SPC,PENDING_SPC) Values (?,?,?,?,?,?,?)");
            pst = con.prepareStatement("select * from parc_promotion where groupc_promotionid=? and is_fitfor_shouldering_responsibility_reporting='N'");
            pst.setInt(1, groupCEmployee.getGroupCpromotionId());
            rs = pst.executeQuery();
            while (rs.next()) {
                pst1.setInt(1, 15);//In g_workflow_process for group c 
                pst1.setString(2, groupCEmployee.getReportingempId());
                pst1.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst1.setInt(4, 93);//iN G_PROCESS_STATUS FOR GROUPC FORWARD TO APPRISEE
                pst1.setString(5, rs.getString("reviewed_empid"));
                pst1.setString(6, groupCEmployee.getReportingspc());
                pst1.setString(7, rs.getString("reviewed_spc"));
                pst1.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst1);
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
    }

    @Override
    public int savehigherAuthorityForwardRemark(GroupCEmployee groupCEmployee) {
        int taskid = 0;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        String mobileno = null;

        try {
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            con = dataSource.getConnection();

            pst = con.prepareStatement("SELECT mobile FROM EMP_MAST WHERE emp_id=?");
            pst.setString(1, groupCEmployee.getReviewingempId());
            rs = pst.executeQuery();
            if (rs.next()) {
                groupCEmployee.setMobileNo(rs.getString("mobile"));
                mobileno = groupCEmployee.getMobileNo();
            }
            DataBaseFunctions.closeSqlObjects(rs, pst);

            if (groupCEmployee.getTaskId() == 0) {
                pst = con.prepareStatement("INSERT INTO TASK_MASTER(PROCESS_ID, INITIATED_BY, INITIATED_ON, STATUS_ID, PENDING_AT,INITIATED_SPC,PENDING_SPC) Values (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

                pst.setInt(1, 15);//In g_workflow_process for group c 
                pst.setString(2, groupCEmployee.getReportingempId());
                pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(4, GroupCEmployee.PARC_PROMOTION_SUBMITTED_BY_REPORTING_AUTHORITY);//iN G_PROCESS_STATUS FOR GROUPC FORWARD
                pst.setString(5, groupCEmployee.getReviewingempId());
                pst.setString(6, groupCEmployee.getReportingspc());
                pst.setString(7, groupCEmployee.getReviewingspc());
                pst.executeUpdate();
                rs = pst.getGeneratedKeys();
                rs.next();
                taskid = rs.getInt("task_id");
                DataBaseFunctions.closeSqlObjects(rs, pst);
                pst = con.prepareStatement("update groupc_promotion set task_id=?,pending_at = ?, pending_at_spc=?,STATUS_ID=? where groupc_promotionid = ?");
                pst.setInt(1, taskid);
                pst.setString(2, groupCEmployee.getReviewingempId());
                pst.setString(3, groupCEmployee.getReviewingspc());
                pst.setInt(4, 84);
                pst.setInt(5, groupCEmployee.getGroupCpromotionId());
                pst.executeUpdate();
            } else {
                pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?,PENDING_SPC=?,STATUS_ID=? WHERE TASK_ID=?");
                pst.setString(1, groupCEmployee.getReviewingempId());
                pst.setString(2, groupCEmployee.getReviewingspc());
                pst.setInt(3, GroupCEmployee.PARC_PROMOTION_SUBMITTED_BY_REPORTING_AUTHORITY);
                pst.setInt(4, groupCEmployee.getTaskId());
                pst.executeUpdate();
                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("update groupc_promotion set pending_at = ?, pending_at_spc=? where groupc_promotionid = ?");
                pst.setString(1, groupCEmployee.getReviewingempId());
                pst.setString(2, groupCEmployee.getReviewingspc());
                pst.setInt(3, groupCEmployee.getGroupCpromotionId());
                pst.executeUpdate();
            }

            if (mobileno != null && !mobileno.equals("")) {
                SMSThread smsthread = new SMSThread(groupCEmployee.getReviewingempId(), mobileno, "P");
                Thread t1 = new Thread(smsthread);
                t1.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return taskid;
    }

    @Override
    public void forwardReviewingPromotionList(GroupCEmployee groupCEmployee) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String mobileno = null;
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT mobile FROM EMP_MAST WHERE emp_id=?");
            pst.setString(1, groupCEmployee.getAcceptingempId());
            rs = pst.executeQuery();
            if (rs.next()) {
                groupCEmployee.setMobileNo(rs.getString("mobile"));
                mobileno = groupCEmployee.getMobileNo();
            }
            DataBaseFunctions.closeSqlObjects(rs, pst);

            pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?, PENDING_SPC=?,STATUS_ID=? WHERE TASK_ID=?");
            pst.setString(1, groupCEmployee.getAcceptingempId());
            pst.setString(2, groupCEmployee.getAcceptingspc());
            pst.setInt(3, GroupCEmployee.FORWARDED_TO_ACCEPTING_AUTHORITY);
            pst.setInt(4, groupCEmployee.getTaskId());
            pst.executeUpdate();
            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("update groupc_promotion set pending_at = ?,pending_at_spc=?,status_id=? where groupc_promotionid = ?");
            pst.setString(1, groupCEmployee.getAcceptingempId());
            pst.setString(2, groupCEmployee.getAcceptingspc());
            pst.setInt(3, 91);//Forwarded to Accepting
            pst.setInt(4, groupCEmployee.getGroupCpromotionId());
            pst.executeUpdate();

            if (mobileno != null && !mobileno.equals("")) {
                SMSThread smsthread = new SMSThread(groupCEmployee.getAcceptingempId(), mobileno, "P");
                Thread t1 = new Thread(smsthread);
                t1.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void submitAcceptingingPromotionList(GroupCEmployee groupCEmployee) {
        Connection con = null;
        PreparedStatement pst = null;
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=NULL, PENDING_SPC=NULL,STATUS_ID=95,IS_COMPLETED='Y' WHERE TASK_ID=?");
            pst.setInt(1, groupCEmployee.getTaskId());
            pst.executeUpdate();

            pst = con.prepareStatement("UPDATE groupc_promotion SET pending_at=NULL, pending_at_spc=NULL,STATUS_ID=95 WHERE TASK_ID=?");
            pst.setInt(1, groupCEmployee.getTaskId());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteselectedEmployeeList(GroupCEmployee groupCEmployee) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("delete from parc_promotion where promotion_id=? and groupc_promotionid =?");
            pst.setInt(1, groupCEmployee.getPromotionId());
            pst.setInt(2, groupCEmployee.getGroupCpromotionId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getSelectedNotFitForPronotionCEmpList(int groupCpromotionId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList notfitForPronotionempList = new ArrayList();
        //int res = 0;
        try {
            con = this.dataSource.getConnection();
            //res = getGroupCpromotionId();
            pstmt = con.prepareStatement("Select reviewed_empid,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,reviewed_spc,POST,is_fitfor_shouldering_responsibility_reporting,reporting_remarks,disk_file_name,org_file_name from parc_promotion "
                    + " inner join emp_mast ON  parc_promotion.reviewed_empid = emp_mast.emp_id "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "where groupc_promotionid = ? and is_fitfor_shouldering_responsibility_reporting='N'");

            pstmt.setInt(1, groupCpromotionId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                GroupCEmployee groupCEmployee = new GroupCEmployee();
                groupCEmployee.setReviewedempId(rs.getString("reviewed_empid"));
                groupCEmployee.setReviewedempname(rs.getString("EMP_NAME"));
                groupCEmployee.setReviewedspc(rs.getString("reviewed_spc"));
                groupCEmployee.setReviewedpost(rs.getString("POST"));
                groupCEmployee.setIsfitforShoulderingResponsibilityReporting(rs.getString("is_fitfor_shouldering_responsibility_reporting"));
                groupCEmployee.setReportingRemarks(rs.getString("reporting_remarks"));
                groupCEmployee.setDiskFileName(rs.getString("disk_file_name"));
                groupCEmployee.setOriginalFilename(rs.getString("org_file_name"));
                notfitForPronotionempList.add(groupCEmployee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return notfitForPronotionempList;
    }

    @Override
    public int getGroupCpromotionId(int taskId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int groupc_promotionid = 0;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select groupc_promotionid from groupc_promotion where task_id=?");
            pstmt.setInt(1, taskId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                groupc_promotionid = rs.getInt("groupc_promotionid");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return groupc_promotionid;
    }

    @Override
    public void createParCPromotionReport(GroupCInitiatedbean groupCInitiatedbean) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO groupc_promotion(reporting_empid,reporting_spc, created_ondate, fiscal_year) Values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, groupCInitiatedbean.getReportingempId());
            pst.setString(2, groupCInitiatedbean.getReportingspc());
            pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(4, groupCInitiatedbean.getFiscalyear());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getPromotionReportList(String reportingempId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList promotionReportList = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("Select groupc_promotionid,reporting_empid,groupc_promotion.reporting_spc,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,created_ondate,fiscal_year,task_id,status_id,"
                    + "get_empname_from_type(pending_at,'G') as pending_at,post from groupc_promotion "
                    + " inner join emp_mast ON  groupc_promotion.reporting_empid = emp_mast.emp_id "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE where reporting_empid=? order by groupc_promotionid desc");

            pstmt.setString(1, reportingempId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                GroupCInitiatedbean groupCInitiatedbean = new GroupCInitiatedbean();
                groupCInitiatedbean.setGroupCpromotionId(rs.getInt("groupc_promotionid"));
                groupCInitiatedbean.setReportingempId(rs.getString("reporting_empid"));
                groupCInitiatedbean.setReportingspc(rs.getString("reporting_spc"));
                groupCInitiatedbean.setReportingempname(rs.getString("EMP_NAME"));
                groupCInitiatedbean.setCreatedondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("created_ondate")));
                groupCInitiatedbean.setFiscalyear(rs.getString("fiscal_year"));
                groupCInitiatedbean.setTaskId(rs.getInt("task_id"));
                groupCInitiatedbean.setStatusId(rs.getInt("status_id"));
                groupCInitiatedbean.setPendingAtempId(rs.getString("pending_at"));
                groupCInitiatedbean.setReportingpost(rs.getString("POST"));
                promotionReportList.add(groupCInitiatedbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return promotionReportList;
    }

    @Override
    public void deletereviewedEmpDetails(GroupCInitiatedbean groupCInitiatedbean) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("delete from groupc_promotion where groupc_promotionid =?");
            pst.setInt(1, groupCInitiatedbean.getGroupCpromotionId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void updateGroupCpromotionReviewingRemark(GroupCEmployee groupCEmployee) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            Long curtime = new Date().getTime();
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE parc_promotion SET reviewing_ondate = ?,is_fitfor_shouldering_responsibility_reviewing = ? ,reviewing_empid=?,reviewing_spc = ?,reviewed_empid = ?,reviewed_spc = ? where promotion_id =?");
            pst.setTimestamp(1, new Timestamp(curtime));
            pst.setString(2, groupCEmployee.getIsfitforShoulderingResponsibilityReviewing());
            pst.setString(3, groupCEmployee.getReviewingempId());
            pst.setString(4, groupCEmployee.getReviewingspc());
            pst.setString(5, groupCEmployee.getReviewedempId());
            pst.setString(6, groupCEmployee.getReviewedspc());
            pst.setInt(7, groupCEmployee.getPromotionId());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void acceptingRemarkFitForPromotion(GroupCEmployee groupCEmployee) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            con = dataSource.getConnection();
            pst = con.prepareStatement("update parc_promotion set is_fitfor_shouldering_responsibility_accepting='Y', accepting_empid=?,accepting_spc=?,accepting_ondate=?, accepting_remarks = ? WHERE promotion_id=?");

            pst.setString(1, groupCEmployee.getAcceptingempId());
            pst.setString(2, groupCEmployee.getAcceptingspc());
            pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(4, groupCEmployee.getReportingRemarks());
            pst.setInt(5, groupCEmployee.getPromotionId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void acceptingRemarkNotFitForPromotion(GroupCEmployee groupCEmployee) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            con = dataSource.getConnection();
            pst = con.prepareStatement("update parc_promotion set is_fitfor_shouldering_responsibility_accepting='N', accepting_empid=?,accepting_spc=?,accepting_ondate=?, accepting_remarks = ? WHERE promotion_id=?");
            pst.setString(1, groupCEmployee.getAcceptingempId());
            pst.setString(2, groupCEmployee.getAcceptingspc());
            pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(4, groupCEmployee.getReportingRemarks());
            pst.setInt(5, groupCEmployee.getPromotionId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateGroupCpromotionAcceptingRemark(GroupCEmployee groupCEmployee) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            Long curtime = new Date().getTime();
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE parc_promotion SET accepting_ondate = ?,is_fitfor_shouldering_responsibility_accepting = ? ,accepting_empid=?,accepting_spc = ?,reviewed_empid = ?,reviewed_spc = ? where promotion_id =?");
            pst.setTimestamp(1, new Timestamp(curtime));
            pst.setString(2, groupCEmployee.getIsfitforShoulderingResponsibilityAccepting());
            pst.setString(3, groupCEmployee.getAcceptingempId());
            pst.setString(4, groupCEmployee.getAcceptingspc());
            pst.setString(5, groupCEmployee.getReviewedempId());
            pst.setString(6, groupCEmployee.getReviewedspc());
            pst.setInt(7, groupCEmployee.getPromotionId());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getgroupCCustdianDetail(ParAssignPrivilage pap, String fiscalyear) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList groupCEmpList = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            /* Office codewise Search*/
            if ((((pap.getDistCode() == null && pap.getDistCode().equals("")) && pap.getOffCode() != null || !pap.getOffCode().equals("")))) {
                pstmt = con.prepareStatement("Select fiscal_year,promotion_id,reviewed_empid,get_empname_from_type(parc_promotion.reviewed_empid ,'G') as reviewedempname,getspn(parc_promotion.reviewed_spc)as reviewed_spc, "
                        + "reporting_remarks,disk_file_name,org_file_name,reviewing_remarks,is_fitfor_shouldering_responsibility_reporting,is_fitfor_shouldering_responsibility_reviewing,is_fitfor_shouldering_responsibility_accepting, "
                        + "accepting_remarks,get_empname_from_type(groupc_promotion.reporting_empid ,'G') as reportingname,get_empname_from_type(parc_promotion.reviewing_empid ,'G') as reviewingname,"
                        + "get_empname_from_type(parc_promotion.accepting_empid ,'G') as acceptingname,getspn(groupc_promotion.reporting_spc) as reportingspc,getspn(parc_promotion.reviewing_spc) as reviewingspc,"
                        + "getspn(parc_promotion.accepting_spc) as acceptingspc,reporting_ondate,reviewing_ondate,accepting_ondate from parc_promotion "
                        + "INNER JOIN groupc_promotion ON groupc_promotion.groupc_promotionid = parc_promotion.groupc_promotionid "
                        + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = parc_promotion.reviewed_empid "
                        + "INNER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE = G_OFFICE.OFF_CODE "
                        + "where groupc_promotion.fiscal_year =? and G_OFFICE.OFF_CODE=?");
                pstmt.setString(1, fiscalyear);
                pstmt.setString(2, pap.getOffCode());
                //rs = pstmt.executeQuery();
                /*district code wise Search */
            } else if (((pap.getOffCode() == null || pap.getOffCode().equals(""))) && (pap.getDistCode() != null || !pap.getDistCode().equals(""))) {
                pstmt = con.prepareStatement("Select fiscal_year,promotion_id,reviewed_empid,get_empname_from_type(parc_promotion.reviewed_empid ,'G') as reviewedempname,getspn(parc_promotion.reviewed_spc)as reviewed_spc, "
                        + "reporting_remarks,disk_file_name,org_file_name,reviewing_remarks,is_fitfor_shouldering_responsibility_reporting,is_fitfor_shouldering_responsibility_reviewing,is_fitfor_shouldering_responsibility_accepting, "
                        + "accepting_remarks,get_empname_from_type(groupc_promotion.reporting_empid ,'G') as reportingname,get_empname_from_type(parc_promotion.reviewing_empid ,'G') as reviewingname,"
                        + "get_empname_from_type(parc_promotion.accepting_empid ,'G') as acceptingname,getspn(groupc_promotion.reporting_spc) as reportingspc,getspn(parc_promotion.reviewing_spc) as reviewingspc,"
                        + "getspn(parc_promotion.accepting_spc) as acceptingspc,reporting_ondate,reviewing_ondate,accepting_ondate from parc_promotion "
                        + "INNER JOIN groupc_promotion ON groupc_promotion.groupc_promotionid = parc_promotion.groupc_promotionid "
                        + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = parc_promotion.reviewed_empid "
                        + "INNER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE = G_OFFICE.OFF_CODE "
                        + "where groupc_promotion.fiscal_year =? and dist_code=?");
                pstmt.setString(1, fiscalyear);
                pstmt.setString(2, pap.getDistCode());
            } else {
                pstmt = con.prepareStatement("Select fiscal_year,promotion_id,reviewed_empid,get_empname_from_type(parc_promotion.reviewed_empid ,'G') as reviewedempname,getspn(parc_promotion.reviewed_spc)as reviewed_spc, "
                        + "reporting_remarks,disk_file_name,org_file_name,reviewing_remarks,is_fitfor_shouldering_responsibility_reporting,is_fitfor_shouldering_responsibility_reviewing,is_fitfor_shouldering_responsibility_accepting, "
                        + "accepting_remarks,get_empname_from_type(groupc_promotion.reporting_empid ,'G') as reportingname,get_empname_from_type(parc_promotion.reviewing_empid ,'G') as reviewingname,"
                        + "get_empname_from_type(parc_promotion.accepting_empid ,'G') as acceptingname,getspn(groupc_promotion.reporting_spc) as reportingspc,getspn(parc_promotion.reviewing_spc) as reviewingspc,"
                        + "getspn(parc_promotion.accepting_spc) as acceptingspc,reporting_ondate,reviewing_ondate,accepting_ondate from parc_promotion "
                        + "INNER JOIN groupc_promotion ON groupc_promotion.groupc_promotionid = parc_promotion.groupc_promotionid "
                        + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = parc_promotion.reviewed_empid "
                        + "INNER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE = G_OFFICE.OFF_CODE "
                        + "where groupc_promotion.fiscal_year =? and cadre_code=? and G_OFFICE.OFF_CODE=? and dist_code=?");
                pstmt.setString(1, fiscalyear);
                pstmt.setString(2, pap.getCadreCode());
                pstmt.setString(3, pap.getOffCode());
                pstmt.setString(4, pap.getDistCode());
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {

                GroupCEmployee groupCEmployee = new GroupCEmployee();
                groupCEmployee.setFiscalyear(rs.getString("fiscal_year"));
                groupCEmployee.setPromotionId(rs.getInt("promotion_id"));
                groupCEmployee.setReviewedempId(rs.getString("reviewed_empid"));
                groupCEmployee.setReviewedempname(rs.getString("reviewedempname"));
                groupCEmployee.setReviewedpost(rs.getString("reviewed_spc"));
                groupCEmployee.setReportingRemarks(rs.getString("reporting_remarks"));
                groupCEmployee.setDiskFileName(rs.getString("disk_file_name"));
                groupCEmployee.setOriginalFilename(rs.getString("org_file_name"));
                groupCEmployee.setReviewingRemarks(rs.getString("reviewing_remarks"));
                groupCEmployee.setIsfitforShoulderingResponsibilityReporting(rs.getString("is_fitfor_shouldering_responsibility_reporting"));
                groupCEmployee.setIsfitforShoulderingResponsibilityReviewing(rs.getString("is_fitfor_shouldering_responsibility_reviewing"));
                groupCEmployee.setIsfitforShoulderingResponsibilityAccepting(rs.getString("is_fitfor_shouldering_responsibility_accepting"));
                groupCEmployee.setAcceptingRemarks(rs.getString("accepting_remarks"));
                groupCEmployee.setReportingempname(rs.getString("reportingname"));
                groupCEmployee.setReviewingempname(rs.getString("reviewingname"));
                groupCEmployee.setAcceptingempname(rs.getString("acceptingname"));
                groupCEmployee.setReportingpost(rs.getString("reportingspc"));
                groupCEmployee.setReviewingpost(rs.getString("reviewingspc"));
                groupCEmployee.setAcceptingpost(rs.getString("acceptingspc"));
                groupCEmployee.setReportingondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("reporting_ondate")));
                groupCEmployee.setReviewingondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("reviewing_ondate")));
                groupCEmployee.setAcceptingondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("accepting_ondate")));

                groupCEmpList.add(groupCEmployee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return groupCEmpList;
    }

    @Override
    public ArrayList getReportingAuthorityDetail(int groupcPromotionId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList reportingdetail = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("Select promotion_id,reporting_empid,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,reporting_spc,POST,is_fitfor_shouldering_responsibility_reporting,"
                    + "reporting_remarks,disk_file_name,org_file_name from parc_promotion "
                    + "INNER JOIN emp_mast ON  parc_promotion.reporting_empid = emp_mast.emp_id "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "INNER JOIN groupc_promotion ON groupc_promotion.groupc_promotionid = parc_promotion.groupc_promotionid where groupc_promotion.groupc_promotionid = ? ");
            pstmt.setInt(1, groupcPromotionId);

            rs = pstmt.executeQuery();
            while (rs.next()) {

                GroupCEmployee groupCEmployee = new GroupCEmployee();
                groupCEmployee.setMode("");
                groupCEmployee.setPromotionId(rs.getInt("promotion_id"));
                groupCEmployee.setReportingempId(rs.getString("reporting_empid"));
                groupCEmployee.setReportingempname(rs.getString("EMP_NAME"));
                groupCEmployee.setReportingspc(rs.getString("reporting_spc"));
                groupCEmployee.setReportingpost(rs.getString("POST"));
                groupCEmployee.setIsfitforShoulderingResponsibilityReporting(rs.getString("is_fitfor_shouldering_responsibility_reporting"));
                groupCEmployee.setReportingRemarks(rs.getString("reporting_remarks"));
                groupCEmployee.setDiskFileName(rs.getString("disk_file_name"));
                groupCEmployee.setOriginalFilename(rs.getString("org_file_name"));
                reportingdetail.add(groupCEmployee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return reportingdetail;
    }

    public ArrayList getAcceptingFinalRemarks(String fiscalyear) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList acceptingremarksdetail = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("Select promotion_id,reviewed_empid,get_empname_from_type(parc_promotion.reviewed_empid ,'G') as reviewedempname,getspn(parc_promotion.reviewed_spc)as reviewed_spc,"
                    + "is_fitfor_shouldering_responsibility_accepting,accepting_remarks from parc_promotion "
                    + "INNER JOIN groupc_promotion ON groupc_promotion.groupc_promotionid = parc_promotion.groupc_promotionid where groupc_promotion.fiscal_year =?");
            pstmt.setString(1, fiscalyear);
            rs = pstmt.executeQuery();
            while (rs.next()) {

                GroupCEmployee groupCEmployee = new GroupCEmployee();
                groupCEmployee.setPromotionId(rs.getInt("promotion_id"));
                groupCEmployee.setReviewedempId(rs.getString("reviewed_empid"));
                groupCEmployee.setReviewedempname(rs.getString("reviewedempname"));
                groupCEmployee.setReviewedspc(rs.getString("reviewed_spc"));
                groupCEmployee.setIsfitforShoulderingResponsibilityAccepting(rs.getString("is_fitfor_shouldering_responsibility_accepting"));
                groupCEmployee.setAcceptingRemarks(rs.getString("accepting_remarks"));
                acceptingremarksdetail.add(groupCEmployee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return acceptingremarksdetail;
    }

    @Override
    public void savecustodianremarksForGroupC(GroupCCustodianCommunication groupCCustodianCommunication) {
        String diskfileName = null;
        PreparedStatement pst = null;
        Connection con = null;
        String originalFileName = null;
        String contentType = null;
        String filepath = null;
        ResultSet rs = null;

        try {
            if (!groupCCustodianCommunication.getUploadDocument().isEmpty()) {
                diskfileName = new Date().getTime() + "";
                originalFileName = groupCCustodianCommunication.getUploadDocument().getOriginalFilename();
                contentType = groupCCustodianCommunication.getUploadDocument().getContentType();
                byte[] bytes = groupCCustodianCommunication.getUploadDocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + diskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            con = dataSource.getConnection();
            if (groupCCustodianCommunication.getTaskId() == 0) {
                pst = con.prepareStatement("Select promotion_id,reporting_empid,reporting_spc,reviewing_empid,reviewing_spc,accepting_empid,accepting_spc from parc_promotion");
                //pst.setInt(1, groupCCustodianCommunication.getPromotionId());
                rs = pst.executeQuery();

                if (groupCCustodianCommunication.getAuthoritytype().equals("reporting")) {
                    if (rs.next()) {
                        groupCCustodianCommunication.setToempId(rs.getString("reporting_empid"));
                        groupCCustodianCommunication.setTospc(rs.getString("reporting_spc"));
                    }
                } else if (groupCCustodianCommunication.getAuthoritytype().equals("reviewing")) {
                    if (rs.next()) {
                        groupCCustodianCommunication.setToempId(rs.getString("reviewing_empid"));
                        groupCCustodianCommunication.setTospc(rs.getString("reviewing_spc"));
                    }
                } else if (groupCCustodianCommunication.getAuthoritytype().equals("accepting")) {
                    if (rs.next()) {
                        groupCCustodianCommunication.setToempId(rs.getString("accepting_empid"));
                        groupCCustodianCommunication.setTospc(rs.getString("accepting_spc"));
                    }
                }
            }
            Long curtime = new Date().getTime();

            pst = con.prepareStatement("INSERT INTO parc_promotion_communication(promotion_id,from_emp_id,from_spc,to_emp_id,to_spc,message,disk_file_name,org_file_name,file_type,file_path,communication_on_date) VALUES(?,?,?,?,?,?,?,?,?,?,?)");

            pst.setInt(1, groupCCustodianCommunication.getPromotionId());
            pst.setString(2, groupCCustodianCommunication.getFromempId());
            pst.setString(3, groupCCustodianCommunication.getFromspc());
            pst.setString(4, groupCCustodianCommunication.getToempId());
            pst.setString(5, groupCCustodianCommunication.getTospc());
            pst.setString(6, groupCCustodianCommunication.getMessagedetail());
            pst.setString(7, diskfileName);
            pst.setString(8, originalFileName);
            pst.setString(9, contentType);
            pst.setString(10, this.uploadPath);
            pst.setTimestamp(11, new Timestamp(curtime));

            pst.executeUpdate();
            if (groupCCustodianCommunication.getTaskId() == 0) {

                pst = con.prepareStatement("INSERT INTO TASK_MASTER(PROCESS_ID, INITIATED_BY, INITIATED_ON, STATUS_ID, PENDING_AT,INITIATED_SPC,PENDING_SPC,ref_id) Values (?,?,?,?,?,?,?,?)");
                pst.setInt(1, 17);//In g_workflow_process for group c Custodian
                pst.setString(2, groupCCustodianCommunication.getFromempId());
                pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(4, 96);//IN G_PROCESS_STATUS FOR GROUPC FORWARD
                pst.setString(5, groupCCustodianCommunication.getToempId());
                pst.setString(6, groupCCustodianCommunication.getFromspc());
                pst.setString(7, groupCCustodianCommunication.getTospc());
                pst.setInt(8, groupCCustodianCommunication.getPromotionId());
                pst.executeUpdate();
                DataBaseFunctions.closeSqlObjects(pst);
            } else {
                pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=NULL,PENDING_SPC=NULL WHERE TASK_ID=?");
                pst.setInt(1, groupCCustodianCommunication.getTaskId());
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getremarksOfCustodianForGroupC(int promotionId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList custodianremarkList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select communication_id,get_empname_from_type(from_emp_id ,'G') as from_emp_name,getspn(from_spc) as from_spc,message,org_file_name from parc_promotion_communication where promotion_id=?");
            pst.setInt(1, promotionId);
            rs = pst.executeQuery();
            while (rs.next()) {
                GroupCCustodianCommunication groupCCustodianCommunication = new GroupCCustodianCommunication();
                groupCCustodianCommunication.setCommunicationId(rs.getInt("communication_id"));
                groupCCustodianCommunication.setFromempName(rs.getString("from_emp_name"));
                groupCCustodianCommunication.setFromspc(rs.getString("from_spc"));
                groupCCustodianCommunication.setMessagedetail(rs.getString("message"));
                groupCCustodianCommunication.setOriginalFilename(rs.getString("org_file_name"));
                custodianremarkList.add(groupCCustodianCommunication);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return custodianremarkList;
    }

    public void saveAcceptingRemarksForReview(ParApplyForm parApplyForm) {
        Connection con = null;

        PreparedStatement pstmt = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {

            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT pactid FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=?");
            pstmt.setInt(1, parApplyForm.getParId());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                parApplyForm.setPactid(rs.getInt("pactid"));
            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            pstmt = con.prepareStatement("UPDATE PAR_ACCEPTING_TRAN SET overallgrading=? WHERE PAR_ID=? and pactid=?");
            pstmt.setInt(1, Integer.parseInt(parApplyForm.getSltAcceptingGrading()));
            pstmt.setInt(2, parApplyForm.getParId());
            pstmt.setInt(3, parApplyForm.getPactid());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void savecustodianremarksAdversePAR(ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
        String diskfileName = null;
        PreparedStatement pst = null;
        Connection con = null;
        String originalFileName = null;
        String contentType = null;
        ResultSet rs = null;
        String toempId = null;
        String toSpc = null;
        try {
            con = dataSource.getConnection();
            String tauthoritytype = parAdverseCommunicationDetail.getAuthoritytype();
            //String authoritytype = tauthoritytype.substring(0, tauthoritytype.lastIndexOf("-"));
            //String authid1 = tauthoritytype.substring(tauthoritytype.lastIndexOf("-"));
            String[] authoritytypeArr = tauthoritytype.split("-");
            String authoritytype = authoritytypeArr[0];
            String authid1 = authoritytypeArr[1];

            Parauthorityhelperbean phelper = getParAuthorityForAdverse(authoritytype, Integer.parseInt(authid1));
            toempId = phelper.getAuthorityempid();
            toSpc = phelper.getAuthorityspc();

            if (!parAdverseCommunicationDetail.getUploadDocument().isEmpty()) {
                diskfileName = new Date().getTime() + "";
                originalFileName = parAdverseCommunicationDetail.getUploadDocument().getOriginalFilename();
                contentType = parAdverseCommunicationDetail.getUploadDocument().getContentType();
                byte[] bytes = parAdverseCommunicationDetail.getUploadDocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + diskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            pst = con.prepareStatement("INSERT INTO par_adverse_communication(parid,from_emp_id,from_spc,to_emp_id,to_spc,remarks,disk_file_name,org_file_name,file_type,remarks_on_date,file_path,communication_date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, parAdverseCommunicationDetail.getParId());
            pst.setString(2, parAdverseCommunicationDetail.getFromempId());
            pst.setString(3, parAdverseCommunicationDetail.getFromspc());
            pst.setString(4, toempId);
            pst.setString(5, toSpc);
            pst.setString(6, parAdverseCommunicationDetail.getRemarksdetail());
            pst.setString(7, diskfileName);
            pst.setString(8, originalFileName);
            pst.setString(9, contentType);
            pst.setTimestamp(10, new Timestamp(curtime));
            pst.setString(11, this.uploadPath);
            pst.setTimestamp(12, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.executeUpdate();

            rs = pst.getGeneratedKeys();
            int communicationId = 0;
            if (rs.next()) {
                communicationId = rs.getInt("communication_id");
            }
            DataBaseFunctions.closeSqlObjects(rs, pst);

            if (parAdverseCommunicationDetail.getTaskId() == 0) {
                pst = con.prepareStatement("INSERT INTO TASK_MASTER(PROCESS_ID, INITIATED_BY, INITIATED_ON, STATUS_ID, PENDING_AT,INITIATED_SPC,PENDING_SPC,ref_id) Values (?,?,?,?,?,?,?,?)");
                pst.setInt(1, 18);//In g_workflow_process for REVIEWING ADVERSE
                pst.setString(2, parAdverseCommunicationDetail.getFromempId());
                pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(4, 103);//IN G_PROCESS_STATUS FOR AWAITED REMARKS FROM AUTHORITY
                pst.setString(5, toempId);
                pst.setString(6, parAdverseCommunicationDetail.getFromspc());
                pst.setString(7, toSpc);
                pst.setInt(8, communicationId);
                pst.executeUpdate();
            } else {
                pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=NULL,PENDING_SPC=NULL WHERE TASK_ID=?");
                pst.setInt(1, parAdverseCommunicationDetail.getTaskId());
                pst.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void savecustodianremarksAdversePARToAppraisee(ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
        String diskfileName = null;
        PreparedStatement pst = null;
        Connection con = null;
        String originalFileName = null;
        String contentType = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            if (!parAdverseCommunicationDetail.getUploadDocument().isEmpty()) {
                diskfileName = new Date().getTime() + "";
                originalFileName = parAdverseCommunicationDetail.getUploadDocument().getOriginalFilename();
                contentType = parAdverseCommunicationDetail.getUploadDocument().getContentType();
                byte[] bytes = parAdverseCommunicationDetail.getUploadDocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + diskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            pst = con.prepareStatement("INSERT INTO par_adverse_communication(parid,from_emp_id,from_spc,to_emp_id,to_spc,remarks,disk_file_name,org_file_name,file_type,remarks_on_date,file_path,communication_date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, parAdverseCommunicationDetail.getParId());
            pst.setString(2, parAdverseCommunicationDetail.getFromempId());
            pst.setString(3, parAdverseCommunicationDetail.getFromspc());
            pst.setString(4, parAdverseCommunicationDetail.getToempId());
            pst.setString(5, parAdverseCommunicationDetail.getTospc());
            pst.setString(6, parAdverseCommunicationDetail.getRemarksdetail());
            pst.setString(7, diskfileName);
            pst.setString(8, originalFileName);
            pst.setString(9, contentType);
            pst.setTimestamp(10, new Timestamp(curtime));
            pst.setString(11, this.uploadPath);
            pst.setTimestamp(12, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.executeUpdate();

            rs = pst.getGeneratedKeys();
            int communicationId = 0;
            if (rs.next()) {
                communicationId = rs.getInt("communication_id");
            }
            if (parAdverseCommunicationDetail.getTaskId() == 0) {
                pst = con.prepareStatement("INSERT INTO TASK_MASTER(PROCESS_ID, INITIATED_BY, INITIATED_ON, STATUS_ID, PENDING_AT,INITIATED_SPC,PENDING_SPC,ref_id) Values (?,?,?,?,?,?,?,?)");
                pst.setInt(1, 18);//In g_workflow_process for REVIEWING ADVERSE
                pst.setString(2, parAdverseCommunicationDetail.getFromempId());
                pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(4, 101);//IN G_PROCESS_STATUS FOR REVIEWING PAR IS ADVERSED BY CUSTODIAN
                pst.setString(5, parAdverseCommunicationDetail.getToempId());
                pst.setString(6, parAdverseCommunicationDetail.getFromspc());
                pst.setString(7, parAdverseCommunicationDetail.getTospc());
                pst.setInt(8, communicationId);
                pst.executeUpdate();
            } else {
                pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=NULL,PENDING_SPC=NULL WHERE TASK_ID=?");
                pst.setInt(1, parAdverseCommunicationDetail.getTaskId());
                pst.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void saveparAdverseCommunicationReply(ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
        String diskfileName = null;
        PreparedStatement pst = null;
        Connection con = null;
        String originalFileName = null;
        String contentType = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            if (!parAdverseCommunicationDetail.getUploadDocument().isEmpty()) {
                diskfileName = new Date().getTime() + "";
                originalFileName = parAdverseCommunicationDetail.getUploadDocument().getOriginalFilename();
                contentType = parAdverseCommunicationDetail.getUploadDocument().getContentType();
                byte[] bytes = parAdverseCommunicationDetail.getUploadDocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + diskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            pst = con.prepareStatement("INSERT INTO par_adverse_communication(parid,from_emp_id,from_spc,to_emp_id,to_spc,remarks,disk_file_name,org_file_name,file_type,remarks_on_date,file_path,communication_date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, parAdverseCommunicationDetail.getParId());
            pst.setString(2, parAdverseCommunicationDetail.getFromempId());
            pst.setString(3, parAdverseCommunicationDetail.getFromspc());
            pst.setString(4, parAdverseCommunicationDetail.getToempId());
            pst.setString(5, parAdverseCommunicationDetail.getTospc());
            pst.setString(6, parAdverseCommunicationDetail.getRemarksdetail());
            pst.setString(7, diskfileName);
            pst.setString(8, originalFileName);
            pst.setString(9, contentType);
            pst.setTimestamp(10, new Timestamp(curtime));
            pst.setString(11, this.uploadPath);
            pst.setTimestamp(12, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE PAR_MASTER SET adverse_comm_status_id=102 WHERE parid=?");
            pst.setInt(1, parAdverseCommunicationDetail.getParId());
            pst.executeUpdate();
            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=NULL,PENDING_SPC=NULL,status_id=102 WHERE TASK_ID=?");
            pst.setInt(1, parAdverseCommunicationDetail.getTaskId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getcustodianremarksAdversePAR(int parId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList custodianadverseremarkList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select getspn(par_adverse_communication.from_spc) as from_post,get_empname_from_type(par_adverse_communication.from_emp_id ,'G') as from_empname,"
                    + " from_emp_id,from_spc,org_file_name,remarks,get_empname_from_type(par_adverse_communication.to_emp_id ,'G') as to_empname,"
                    + " getspn(par_adverse_communication.to_spc) as to_post,to_emp_id,to_spc from par_adverse_communication where parid =?");
            pst.setInt(1, parId);
            rs = pst.executeQuery();
            while (rs.next()) {
                ParAdverseCommunicationDetail parAdverseCommunicationDetail = new ParAdverseCommunicationDetail();
                parAdverseCommunicationDetail.setFromPost(rs.getString("from_post"));
                parAdverseCommunicationDetail.setFromempName(rs.getString("from_empname"));
                parAdverseCommunicationDetail.setFromempId(rs.getString("from_emp_id"));
                parAdverseCommunicationDetail.setFromspc(rs.getString("from_spc"));
                parAdverseCommunicationDetail.setOriginalFilename(rs.getString("org_file_name"));
                parAdverseCommunicationDetail.setRemarksdetail(rs.getString("remarks"));
                parAdverseCommunicationDetail.setToempName(rs.getString("to_empname"));
                parAdverseCommunicationDetail.setToPost(rs.getString("to_post"));
                parAdverseCommunicationDetail.setToempId(rs.getString("to_emp_id"));
                parAdverseCommunicationDetail.setTospc(rs.getString("to_spc"));
                custodianadverseremarkList.add(parAdverseCommunicationDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return custodianadverseremarkList;
    }

    @Override
    public ParAdverseCommunicationDetail getCommunicationDetails(int communicationId) {
        ParAdverseCommunicationDetail parAdverseCommunicationDetail = new ParAdverseCommunicationDetail();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select parid,getspn(par_adverse_communication.from_spc) as from_post,get_empname_from_type(par_adverse_communication.from_emp_id ,'G') as from_empname,"
                    + "from_emp_id,from_spc,org_file_name,remarks,get_empname_from_type(par_adverse_communication.to_emp_id ,'G') as to_empname, to_emp_id,to_spc from par_adverse_communication where communication_id =?");
            pst.setInt(1, communicationId);
            rs = pst.executeQuery();
            if (rs.next()) {
                parAdverseCommunicationDetail.setParId(rs.getInt("parid"));
                parAdverseCommunicationDetail.setFromPost(rs.getString("from_post"));
                parAdverseCommunicationDetail.setFromempName(rs.getString("from_empname"));
                parAdverseCommunicationDetail.setFromempId(rs.getString("from_emp_id"));
                parAdverseCommunicationDetail.setFromspc(rs.getString("from_spc"));
                parAdverseCommunicationDetail.setOriginalFilename(rs.getString("org_file_name"));
                parAdverseCommunicationDetail.setRemarksdetail(rs.getString("remarks"));
                parAdverseCommunicationDetail.setToempId(rs.getString("to_emp_id"));
                parAdverseCommunicationDetail.setTospc(rs.getString("to_spc"));
                parAdverseCommunicationDetail.setToempName(rs.getString("to_empname"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parAdverseCommunicationDetail;
    }

    public void markParAsAdverse(int parId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();
            Long curtime = new Date().getTime();
            pstmt = con.prepareStatement("UPDATE PAR_MASTER SET is_adversed=? WHERE PARID=?");
            pstmt.setString(1, "Y");
            pstmt.setInt(2, parId);
            int i = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            DataBaseFunctions.closeSqlObjects(res, pstmt, con);
        }
    }

    @Override
    public void getAppraiseForAdversePAR(ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT par_master.emp_id,par_master.spc,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, post ,cur_spc from par_master \n"
                    + "INNER JOIN emp_mast ON emp_mast.emp_id = par_master.emp_id "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "where parid =? ");
            pstmt.setInt(1, parAdverseCommunicationDetail.getParId());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                parAdverseCommunicationDetail.setToempId(rs.getString("emp_id"));
                parAdverseCommunicationDetail.setTospc(rs.getString("spc"));
                parAdverseCommunicationDetail.setToempName(rs.getString("EMPNAME"));
                parAdverseCommunicationDetail.setToPost(rs.getString("post"));
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public ParAdverseCommunicationDetail getAttachedFileforAdversePAR(int parId) {
        ParAdverseCommunicationDetail parAdverseCommunicationDetail = new ParAdverseCommunicationDetail();
        Connection con = null;
        Statement st = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select org_file_name,disk_file_name,file_type,file_path from par_adverse_communication where parid = ?");
            pst.setInt(1, parId);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                parAdverseCommunicationDetail.setOriginalFilename(rs.getString("org_file_name"));
                parAdverseCommunicationDetail.setDiskFileName(rs.getString("disk_file_name"));
                parAdverseCommunicationDetail.setGetContentType("file_type");
                filepath = rs.getString("file_path");
            }
            File f = new File(filepath + File.separator + parAdverseCommunicationDetail.getDiskFileName());
            parAdverseCommunicationDetail.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return parAdverseCommunicationDetail;
    }

    @Override
    public String getAppriseSPCOfPar(int parId) {
        String apprisespc = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT SPC FROM PAR_MASTER WHERE PARID=?");
            pstmt.setInt(1, parId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                apprisespc = rs.getString("SPC");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return apprisespc;
    }

    public Parauthorityhelperbean getParAuthorityForAdverse(String authoritytype, int authid) {
        Connection con = null;
        PreparedStatement pstamt = null;
        ResultSet resultset = null;
        Parauthorityhelperbean parhelper = null;
        try {
            con = dataSource.getConnection();
            if (authoritytype.equalsIgnoreCase("REPORTING")) {

                pstamt = con.prepareStatement("SELECT REPORTING_EMP_ID,reporting_cur_spc,INITIALS,F_NAME,M_NAME,L_NAME FROM PAR_REPORTING_TRAN "
                        + "INNER JOIN EMP_MAST ON PAR_REPORTING_TRAN.REPORTING_EMP_ID = EMP_MAST.EMP_ID WHERE prptid =?");
                pstamt.setInt(1, authid);
                resultset = pstamt.executeQuery();
                if (resultset.next()) {
                    parhelper = new Parauthorityhelperbean();
                    parhelper.setAuthorityname(StringUtils.defaultString(resultset.getString("INITIALS")) + " " + StringUtils.defaultString(resultset.getString("F_NAME")) + " " + StringUtils.defaultString(resultset.getString("M_NAME")) + " " + StringUtils.defaultString(resultset.getString("L_NAME")));
                    parhelper.setAuthorityspc(resultset.getString("reporting_cur_spc"));
                    parhelper.setAuthorityempid(resultset.getString("REPORTING_EMP_ID"));
                }
            } else if (authoritytype.equalsIgnoreCase("REVIEWING")) {
                pstamt = con.prepareStatement("SELECT REVIEWING_EMP_ID,REVIEWING_CUR_SPC,INITIALS,F_NAME,M_NAME,L_NAME FROM PAR_REVIEWING_TRAN "
                        + "INNER JOIN EMP_MAST ON PAR_REVIEWING_TRAN.REVIEWING_EMP_ID = EMP_MAST.EMP_ID WHERE prvtid=?");
                pstamt.setInt(1, authid);
                resultset = pstamt.executeQuery();
                if (resultset.next()) {
                    parhelper = new Parauthorityhelperbean();
                    parhelper.setAuthorityname(StringUtils.defaultString(resultset.getString("INITIALS")) + " " + StringUtils.defaultString(resultset.getString("F_NAME")) + " " + StringUtils.defaultString(resultset.getString("M_NAME")) + " " + StringUtils.defaultString(resultset.getString("L_NAME")));
                    parhelper.setAuthorityspc(resultset.getString("REVIEWING_CUR_SPC"));
                    parhelper.setAuthorityempid(resultset.getString("REVIEWING_EMP_ID"));
                }
            } else if (authoritytype.equalsIgnoreCase("ACCEPTING")) {
                pstamt = con.prepareStatement("SELECT ACCEPTING_EMP_ID,accepting_cur_spc,INITIALS,F_NAME,M_NAME,L_NAME FROM PAR_ACCEPTING_TRAN "
                        + "INNER JOIN EMP_MAST ON PAR_ACCEPTING_TRAN.ACCEPTING_EMP_ID = EMP_MAST.EMP_ID  WHERE  pactid=?");
                pstamt.setInt(1, authid);
                resultset = pstamt.executeQuery();
                if (resultset.next()) {
                    parhelper = new Parauthorityhelperbean();
                    parhelper.setAuthorityname(StringUtils.defaultString(resultset.getString("INITIALS")) + " " + StringUtils.defaultString(resultset.getString("F_NAME")) + " " + StringUtils.defaultString(resultset.getString("M_NAME")) + " " + StringUtils.defaultString(resultset.getString("L_NAME")));
                    parhelper.setAuthorityspc(resultset.getString("accepting_cur_spc"));
                    parhelper.setAuthorityempid(resultset.getString("ACCEPTING_EMP_ID"));
                }
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(resultset);
            DataBaseFunctions.closeSqlObjects(pstamt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parhelper;
    }

    @Override
    public GroupCCustodianCommunication getAppraiseeForGroupC(int promotionId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        GroupCCustodianCommunication groupCCustodianCommunication = new GroupCCustodianCommunication();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("Select promotion_id,reviewed_empid,get_empname_from_type(parc_promotion.reviewed_empid ,'G') as reviewedempname,getspn(parc_promotion.reviewed_spc)as reviewed_spc "
                    + "from parc_promotion INNER JOIN groupc_promotion ON groupc_promotion.groupc_promotionid = parc_promotion.groupc_promotionid where promotion_id =?");
            pst.setInt(1, promotionId);
            rs = pst.executeQuery();
            if (rs.next()) {
                groupCCustodianCommunication.setReviewedempname(rs.getString("reviewedempname"));
                groupCCustodianCommunication.setReviewedPost(rs.getString("reviewed_spc"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return groupCCustodianCommunication;
    }

    @Override
    public GroupCCustodianCommunication getAttachedFile(int communication_id) {
        GroupCCustodianCommunication groupCCustodianCommunication = new GroupCCustodianCommunication();
        Connection con = null;
        Statement st = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select org_file_name,disk_file_name,file_type,file_path from parc_promotion_communication where communication_id = ?");
            pst.setInt(1, communication_id);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                groupCCustodianCommunication.setOriginalFilename(rs.getString("org_file_name"));
                groupCCustodianCommunication.setDiskFileName(rs.getString("disk_file_name"));
                groupCCustodianCommunication.setGetContentType("file_type");
                filepath = rs.getString("file_path");
            }
            File f = new File(filepath + File.separator + groupCCustodianCommunication.getDiskFileName());
            groupCCustodianCommunication.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return groupCCustodianCommunication;
    }

    public GroupCEmployee getAttachedFileforNotFit(int promotionId) {
        GroupCEmployee groupCEmployee = new GroupCEmployee();
        Connection con = null;
        Statement st = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select org_file_name,disk_file_name,file_type,file_path from parc_promotion where promotion_id = ?");
            pst.setInt(1, promotionId);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                groupCEmployee.setOriginalFilename(rs.getString("org_file_name"));
                groupCEmployee.setDiskFileName(rs.getString("disk_file_name"));
                groupCEmployee.setGetContentType("file_type");
                filepath = rs.getString("file_path");
            }
            File f = new File(filepath + File.separator + groupCEmployee.getDiskFileName());
            groupCEmployee.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return groupCEmployee;
    }

    @Override
    public List getGroupCEmployeeListFromOtherOffice(String offCode, int groupcPromotionid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList emplist = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,emp_id,cur_spc,POST,reviewed_empid from emp_mast "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "LEFT OUTER JOIN (SELECT reviewed_empid FROM parc_promotion WHERE groupc_promotionid=?) AS parc_promotion ON emp_mast.emp_id = parc_promotion.reviewed_empid "
                    + "where cur_off_code=? and post_grp_type='C' and dep_code='02'  ORDER BY EMPNAME");
            pstmt.setInt(1, groupcPromotionid);
            pstmt.setString(2, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                GroupCEmployee groupCEmployee = new GroupCEmployee();
                groupCEmployee.setReviewedempname(rs.getString("EMPNAME"));
                groupCEmployee.setReviewedempId(rs.getString("emp_id"));
                groupCEmployee.setReviewedspc(rs.getString("cur_spc"));
                groupCEmployee.setReviewedpost(rs.getString("POST"));
                if (rs.getString("reviewed_empid") != null && !rs.getString("reviewed_empid").equals("")) {
                    groupCEmployee.setAlreadyAdded("Y");
                } else {
                    groupCEmployee.setAlreadyAdded("N");
                }
                emplist.add(groupCEmployee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return emplist;
    }

    @Override
    public boolean isRemarksCompulsoryReviewing(int groupcPromotionid, String remarkofReviewing) {
        boolean isRemarkCompulsory = true;
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select is_fitfor_shouldering_responsibility_reporting from parc_promotion where promotion_id=?");
            pst.setInt(1, groupcPromotionid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("is_fitfor_shouldering_responsibility_reporting").equals("Y") && remarkofReviewing.equals("Y")) {
                    isRemarkCompulsory = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return isRemarkCompulsory;
    }

    @Override
    public boolean isRemarksCompulsoryAccepting(int groupcPromotionid, String remarkofAccepting) {
        boolean isRemarkCompulsoryAccepting = true;
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select is_fitfor_shouldering_responsibility_reviewing from parc_promotion where promotion_id=?");
            pst.setInt(1, groupcPromotionid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("is_fitfor_shouldering_responsibility_reviewing").equals("Y") && remarkofAccepting.equals("Y")) {
                    isRemarkCompulsoryAccepting = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return isRemarkCompulsoryAccepting;
    }

    public void updateFromAndToDateForReporting(GroupCEmployee groupCEmployee) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            con = dataSource.getConnection();

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());

            pst = con.prepareStatement("update parc_promotion set assessment_type_reporting=?,from_date_reporting=?,to_date_reporting=? where promotion_id=?");
            pst.setString(1, groupCEmployee.getAssessmentTypeReporting());
            pst.setTimestamp(2, new Timestamp(sdf.parse(groupCEmployee.getPeriodFromReporting()).getTime()));
            pst.setTimestamp(3, new Timestamp(sdf.parse(groupCEmployee.getPeriodToReporting()).getTime()));
            pst.setInt(4, groupCEmployee.getPromotionId());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }

    }

    public String getisPendingAtEmpId(int groupcPromotionid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        String isPendingAtEmpId = null;
        String isPendingAtspc = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select pending_at,pending_at_spc  from groupc_promotion where groupc_promotionid=?");
            pst.setInt(1, groupcPromotionid);
            res = pst.executeQuery();
            if (res.next()) {
                isPendingAtEmpId = res.getString("pending_at");
                isPendingAtspc = res.getString("pending_at_spc");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
        return isPendingAtEmpId;
    }

    public int gettaskIdFromGroupCPromotionId(int groupcPromotionid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        int taskid = 0;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select task_id  from groupc_promotion where groupc_promotionid=?");
            pst.setInt(1, groupcPromotionid);
            res = pst.executeQuery();
            if (res.next()) {
                taskid = res.getInt("task_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
        return taskid;
    }

    public String revertGroupCPARByAuthority(String loginempid, GroupCEmployee groupCEmployee) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        String startTime = "";
        String revertAuth = "";

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT reporting_empid,reporting_spc FROM groupc_promotion WHERE groupc_promotionid=?");
            pst.setInt(1, groupCEmployee.getGroupCpromotionId());
            res = pst.executeQuery();
            if (res.next()) {
                groupCEmployee.setReportingempId(res.getString("reporting_empid"));
                groupCEmployee.setReportingspc(res.getString("reporting_spc"));
            }

            pst = con.prepareStatement("SELECT reviewing_empid,reviewing_spc FROM parc_promotion WHERE groupc_promotionid=?");
            pst.setInt(1, groupCEmployee.getGroupCpromotionId());
            res = pst.executeQuery();
            if (res.next()) {
                groupCEmployee.setReviewingempId(res.getString("reviewing_empid"));
                groupCEmployee.setReviewingspc(res.getString("reviewing_spc"));
            }

            pst = con.prepareStatement("SELECT pending_at,pending_at_spc FROM groupc_promotion WHERE groupc_promotionid=?");
            pst.setInt(1, groupCEmployee.getGroupCpromotionId());
            res = pst.executeQuery();
            if (res.next()) {
                groupCEmployee.setAcceptingempId(res.getString("pending_at"));
                groupCEmployee.setAcceptingspc(res.getString("pending_at_spc"));
            }

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());

            if (groupCEmployee.getRemarkauthoritytype().equals("REVIEWING")) {

                pst = con.prepareStatement("INSERT INTO WORKFLOW_LOG(REF_ID,ACTION_TAKEN_BY,FORWARDED_SPC,FORWARD_TO,NOTE,SPC_ONTIME,TASK_ACTION_DATE,TASK_ID,TASK_STATUS_ID,WORKFLOW_TYPE,AUTHORITY_TYPE) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
                pst.setInt(1, groupCEmployee.getGroupCpromotionId());
                pst.setString(2, loginempid);
                pst.setString(3, groupCEmployee.getReportingspc());
                pst.setString(4, groupCEmployee.getReportingempId());
                pst.setString(5, groupCEmployee.getRevertremarks());
                pst.setString(6, "");
                pst.setTimestamp(7, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(8, groupCEmployee.getTaskId());
                pst.setInt(9, GroupCEmployee.PARC_PROMOTION_RETURN_BY_REVIEWING_AUTHORITY);//Revert By Reviewing Authority
                pst.setString(10, "PAR_OF_GROUPC_REVERT");
                pst.setString(11, "REVIEWING AUTHORITY");
                pst.executeUpdate();

                pst = con.prepareStatement("UPDATE TASK_MASTER SET STATUS_ID=?,PENDING_AT=?,APPLY_TO=?,PENDING_SPC=? WHERE TASK_ID=?");
                pst.setInt(1, GroupCEmployee.PARC_PROMOTION_RETURN_BY_REVIEWING_AUTHORITY);//PAR IS RETURNED BY REPORTING AUTHORITY
                pst.setString(2, null);
                pst.setString(3, null);
                pst.setString(4, null);
                pst.setInt(5, groupCEmployee.getTaskId());
                pst.executeUpdate();

                pst = con.prepareStatement("update groupc_promotion set pending_at =null,pending_at_spc=null,status_id=114 where groupc_promotionid = ?");
                pst.setInt(1, groupCEmployee.getGroupCpromotionId());
                pst.executeUpdate();

                pst = con.prepareStatement("update parc_promotion set is_fitfor_shouldering_responsibility_reviewing=null,reviewing_ondate =null,reviewing_remarks=null,reviewing_empid=null,reviewing_spc=null where groupc_promotionid = ?");
                pst.setInt(1, groupCEmployee.getGroupCpromotionId());
                pst.executeUpdate();

                groupCEmployee.setRevertdone("Y");
                revertAuth = "Reviewing";

            } else if (groupCEmployee.getRemarkauthoritytype().equals("ACCEPTING")) {
                pst = con.prepareStatement("INSERT INTO WORKFLOW_LOG(REF_ID,ACTION_TAKEN_BY,FORWARDED_SPC,FORWARD_TO,NOTE,SPC_ONTIME,TASK_ACTION_DATE,TASK_ID,TASK_STATUS_ID,WORKFLOW_TYPE,AUTHORITY_TYPE) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
                pst.setInt(1, groupCEmployee.getGroupCpromotionId());
                pst.setString(2, loginempid);
                pst.setString(3, groupCEmployee.getReviewingspc());
                pst.setString(4, groupCEmployee.getReviewingempId());
                pst.setString(5, groupCEmployee.getRevertremarks());
                pst.setString(6, "");
                pst.setTimestamp(7, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(8, groupCEmployee.getTaskId());
                pst.setInt(9, GroupCEmployee.PARC_PROMOTION_RETURN_BY_ACCEPTING_AUTHORITY);//Revert By Reviewing Authority
                pst.setString(10, "PAR_OF_GROUPC_REVERT");
                pst.setString(11, "ACCEPTING AUTHORITY");
                pst.executeUpdate();

                pst = con.prepareStatement("UPDATE TASK_MASTER SET STATUS_ID=?,PENDING_AT=?,APPLY_TO=?,PENDING_SPC=?,initiated_by=?,initiated_spc=? WHERE TASK_ID=?");
                //pst = con.prepareStatement("INSERT INTO TASK_MASTER (STATUS_ID,PENDING_AT,APPLY_TO,PENDING_SPC,initiated_by,initiated_spc,ref_id) VALUES (?,?,?,?,?,?,?)");
                pst.setInt(1, GroupCEmployee.PARC_PROMOTION_RETURN_BY_ACCEPTING_AUTHORITY);//PAR IS RETURNED BY REPORTING AUTHORITY
                pst.setString(2, groupCEmployee.getReviewingempId());
                pst.setString(3, groupCEmployee.getReviewingempId());
                pst.setString(4, groupCEmployee.getReviewingspc());
                pst.setString(5, groupCEmployee.getAcceptingempId());
                pst.setString(6, groupCEmployee.getAcceptingspc());
                pst.setInt(7, groupCEmployee.getTaskId());
                pst.executeUpdate();

                pst = con.prepareStatement("update groupc_promotion set pending_at =?,pending_at_spc=?,status_id=? where groupc_promotionid = ?");
                pst.setString(1, groupCEmployee.getReviewingempId());
                pst.setString(2, groupCEmployee.getReviewingspc());
                pst.setInt(3, GroupCEmployee.PARC_PROMOTION_RETURN_BY_ACCEPTING_AUTHORITY);
                pst.setInt(4, groupCEmployee.getGroupCpromotionId());
                pst.executeUpdate();

                pst = con.prepareStatement("update parc_promotion set is_fitfor_shouldering_responsibility_accepting=null,accepting_ondate =null,accepting_empid=null,accepting_spc=null,accepting_remarks=null where groupc_promotionid = ?");
                pst.setInt(1, groupCEmployee.getGroupCpromotionId());
                pst.executeUpdate();

                groupCEmployee.setRevertdone("Y");
                revertAuth = "Accepting";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst, con);
        }

        return groupCEmployee.getRevertdone();
    }

    public String[] getRevertReasonOfGroupCPAR(int groupcPromotionid, int taskId) {
        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;

        String[] revertreson = new String[3];
        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT TASK_ID,NOTE,ACTION_TAKEN_BY,AUTHORITY_TYPE,TASK_ACTION_DATE FROM"
                    + " (SELECT TASK_ID,NOTE,ACTION_TAKEN_BY,AUTHORITY_TYPE,TASK_ACTION_DATE FROM WORKfLOW_LOG"
                    + " WHERE REF_ID=? and task_id=?)WORKfLOW_LOG LIMIT 1";
            pst = con.prepareStatement(sql);
            pst.setInt(1, groupcPromotionid);
            pst.setInt(2, taskId);
            //pst.setInt(2, taskid);
            rs = pst.executeQuery();
            if (rs.next()) {
                revertreson[0] = rs.getString("AUTHORITY_TYPE");
                revertreson[1] = rs.getString("NOTE");
                String empId = rs.getString("ACTION_TAKEN_BY");
                if (empId != null) {
                    pst1 = con.prepareStatement("select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME from emp_mast where emp_id=?");
                    pst1.setString(1, empId);
                    rs = pst1.executeQuery();

                    if (rs.next()) {
                        revertreson[2] = rs.getString("FULL_NAME");
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return revertreson;
    }

    public int gettaskId(int groupcPromotionid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int taskid = 0;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select task_id  from groupc_promotion where groupc_promotionid=?");
            pstmt.setInt(1, groupcPromotionid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                taskid = rs.getInt("task_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return taskid;
    }

    public String getstatusId(int groupcPromotionid) {
        String statusId = null;
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select status_id from groupc_promotion where groupc_promotionid=?");
            pst.setInt(1, groupcPromotionid);
            rs = pst.executeQuery();
            if (rs.next()) {

                statusId = rs.getString("status_id");

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return statusId;
    }

    public ParAssignPrivilage getAssignPrivilage(String spc) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ParAssignPrivilage pap = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select * from groupc_authority_admin where spc=?");
            pstmt.setString(1, spc);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                pap = new ParAssignPrivilage();
                pap.setCadreCode(rs.getString("cadre_code"));
                pap.setOffCode(rs.getString("off_code"));
                pap.setDistCode(rs.getString("dist_code"));
                pap.setSpc(rs.getString("spc"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return pap;
    }

    @Override
    public String saveAssignPrivilege(GroupCEmployee groupCEmployee) {
        String msg = "Y";
        int priv_id = 0;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        Connection con = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        try {
            int recordFound = 0;
            int recordFound1 = 0;
            con = dataSource.getConnection();
            if (groupCEmployee.getHidAuthOffCode() != null && !groupCEmployee.getHidAuthOffCode().equals("")) {
                pst = con.prepareStatement("SELECT COUNT(*) AS CNT FROM groupc_authority_admin WHERE SPC=? and off_code=? ");
                pst.setString(1, groupCEmployee.getSpc());
                pst.setString(2, groupCEmployee.getHidAuthOffCode());
                rs = pst.executeQuery();
                if (rs.next()) {
                    recordFound = rs.getInt("CNT");
                }
            } else if (groupCEmployee.getDistCode() != null && !groupCEmployee.getDistCode().equals("")) {
                pst = con.prepareStatement("SELECT COUNT(*) AS CNT FROM groupc_authority_admin WHERE SPC=? and dist_code=? ");
                pst.setString(1, groupCEmployee.getSpc());
                pst.setString(2, groupCEmployee.getDistCode());
                rs = pst.executeQuery();
                if (rs.next()) {
                    recordFound = rs.getInt("CNT");
                }
            }
            if (groupCEmployee.getSpc() != null && !groupCEmployee.getSpc().equals("")) {
                pst1 = con.prepareStatement("SELECT COUNT(*) AS CNT FROM G_PRIVILEGE_MAP WHERE SPC=? and role_id='15' ");
                pst1.setString(1, groupCEmployee.getSpc());
                rs1 = pst1.executeQuery();
                if (rs1.next()) {
                    recordFound1 = rs1.getInt("CNT");
                }
            }

            /*
             pst1 = con.prepareStatement("SELECT off_code,dist_code FROM groupc_authority_admin WHERE SPC=?");
             pst1.setString(1, groupCEmployee.getSpc());
             rs1 = pst1.executeQuery();
             if (rs1.next()) {
             groupCEmployee.setHidAuthOffCode(rs1.getString("off_code"));
             groupCEmployee.setDistCode(rs1.getString("dist_code"));
             }
             */
            if (recordFound == 0) {
                pst = con.prepareStatement("INSERT INTO groupc_authority_admin(SPC,off_code,dist_code) VALUES(?,?,?)");
                pst.setString(1, groupCEmployee.getSpc());
                pst.setString(2, groupCEmployee.getHidAuthOffCode());
                pst.setString(3, groupCEmployee.getDistCode());
                pst.executeUpdate();

                if (recordFound1 == 0) {
                    priv_id = CommonFunctions.getMaxCodeInteger("G_PRIVILEGE_MAP", "PRIV_MAP_ID", con);
                    pst = con.prepareStatement("INSERT INTO G_PRIVILEGE_MAP(PRIV_MAP_ID,SPC,ROLE_ID,MOD_GRP_ID,MOD_ID) VALUES(?,?,?,?,?)");
                    pst.setInt(1, priv_id);
                    pst.setString(2, groupCEmployee.getSpc());
                    pst.setInt(3, 15);
                    pst.setString(4, "038");
                    pst.setInt(5, 214);
                    pst.executeUpdate();
                }
            } else {
                msg = "D";
            }
        } catch (Exception e) {
            msg = "N";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, rs, con);
        }
        return msg;
    }

    @Override
    public ArrayList getAssignPrivilegedList(String spc) {
        Connection con = null;
        ArrayList privilegedList = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,groupc_authority_admin.SPC,SPN FROM groupc_authority_admin "
                    + "INNER JOIN G_SPC ON groupc_authority_admin.SPC = G_SPC.SPC "
                    + "LEFT OUTER JOIN EMP_MAST ON groupc_authority_admin.SPC = EMP_MAST.CUR_SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "WHERE groupc_authority_admin.spc = ?");
            pstmt.setString(1, spc);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                GroupCEmployee groupCEmployee = new GroupCEmployee();
                groupCEmployee.setPriviligeAuhName(rs.getString("EMP_NAME"));
                groupCEmployee.setSpc(rs.getString("SPC"));
                groupCEmployee.setPriviligeAuhDesignation(rs.getString("SPN"));
                privilegedList.add(groupCEmployee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return privilegedList;
    }

    @Override
    public ArrayList getAssignPrivilegedListDistandOffWise(String mode) {
        Connection con = null;
        ArrayList distOrOffwiseprivilegedList = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            if (mode.equals("D")) {
                pstmt = con.prepareStatement("SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,groupc_authority_admin.SPC,SPN,groupc_authority_admin.dist_code,dist_name FROM groupc_authority_admin  "
                        + "INNER JOIN G_SPC ON groupc_authority_admin.SPC = G_SPC.SPC "
                        + "LEFT OUTER JOIN EMP_MAST ON groupc_authority_admin.SPC = EMP_MAST.CUR_SPC "
                        + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                        + "INNER JOIN G_DISTRICT ON groupc_authority_admin.dist_code = G_DISTRICT.dist_code "
                        + "WHERE groupc_authority_admin.dist_code != null or groupc_authority_admin.dist_code != ''");
                rs = pstmt.executeQuery();
            } else {
                pstmt = con.prepareStatement("SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,groupc_authority_admin.SPC,SPN,groupc_authority_admin.off_code,off_en FROM groupc_authority_admin  "
                        + "INNER JOIN G_SPC ON groupc_authority_admin.SPC = G_SPC.SPC "
                        + "LEFT OUTER JOIN EMP_MAST ON groupc_authority_admin.SPC = EMP_MAST.CUR_SPC "
                        + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                        + "INNER JOIN G_OFFICE ON groupc_authority_admin.off_code = G_OFFICE.off_code "
                        + "WHERE groupc_authority_admin.off_code != null or groupc_authority_admin.off_code != ''");
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                GroupCEmployee groupCEmployee = new GroupCEmployee();
                groupCEmployee.setPriviligeAuhName(rs.getString("EMP_NAME"));
                groupCEmployee.setSpc(rs.getString("SPC"));
                groupCEmployee.setPriviligeAuhDesignation(rs.getString("SPN"));
                if (mode.equals("D")) {
                    groupCEmployee.setDistCode(rs.getString("dist_code"));
                    groupCEmployee.setDistName(rs.getString("dist_name"));
                }
                if (mode.equals("O")) {
                    groupCEmployee.setOffcode(rs.getString("off_code"));
                    groupCEmployee.setOffName(rs.getString("off_en"));
                }
                distOrOffwiseprivilegedList.add(groupCEmployee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return distOrOffwiseprivilegedList;
    }

    @Override
    public void deleteGroupCPrivilage(String spc) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM  groupc_authority_admin WHERE SPC=?");
            pst.setString(1, spc);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public GroupCSearchResult getGroupCEmpList(GroupCAdminAdminSearchCriteria groupCAdminAdminSearchCriteria) {
        String fiscalYear = groupCAdminAdminSearchCriteria.getFiscalyear();
        String searchCriteria = groupCAdminAdminSearchCriteria.getSearchCriteria();
        String searchString = groupCAdminAdminSearchCriteria.getSearchString();
        String searchGroupCStatus = groupCAdminAdminSearchCriteria.getSearchGroupCStatus();
        int noofrows = groupCAdminAdminSearchCriteria.getRows();
        int page = groupCAdminAdminSearchCriteria.getPage();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        ArrayList groupCEmplist = new ArrayList();
        GroupCSearchResult groupCSearchResult = new GroupCSearchResult();
        try {
            con = dataSource.getConnection();

            int offSet = noofrows * (page - 1);
            int limit = noofrows;
            String groupCstatus = "";

            if ((searchCriteria == null || searchCriteria.equals("")) && (searchGroupCStatus == null || searchGroupCStatus.equals(""))) {
                pst = con.prepareStatement("SELECT count (*) as cnt from "
                        + "(select status_id,fiscal_year,groupc_promotionid from groupc_promotion)groupc_promotion "
                        + "INNER JOIN (select groupc_promotionid,reviewed_empid,reviewed_spc,reporting_remarks,reviewing_remarks from parc_promotion) parc_promotion on groupc_promotion.groupc_promotionid = parc_promotion.groupc_promotionid "
                        + "INNER JOIN (select EMP_MAST.EMP_ID,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,EMP_MAST.CUR_OFF_CODE,GPF_NO,DOB from EMP_MAST)EMP_MAST ON EMP_MAST.EMP_ID=parc_promotion.reviewed_empid "
                        + "LEFT OUTER JOIN G_PROCESS_STATUS ON G_PROCESS_STATUS.STATUS_ID = groupc_promotion.status_id "
                        + "LEFT OUTER JOIN G_SPC ON parc_promotion.reviewed_spc=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE where fiscal_year =?");
                pst.setString(1, fiscalYear);
                res = pst.executeQuery();
                if (res.next()) {
                    groupCSearchResult.setTotalgroupCEmpFound(res.getInt("CNT"));
                }

                pst = con.prepareStatement("SELECT groupc_promotion.status_id,fiscal_year,groupc_promotion.groupc_promotionid,reviewed_empid,reviewed_spc,EMP_MAST.EMP_ID,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,POST,EMP_MAST.CUR_OFF_CODE,GPF_NO,DOB, "
                        + "STATUS_NAME,reporting_remarks,reviewing_remarks,STATUS_NAME FROM groupc_promotion "
                        + "INNER JOIN parc_promotion on groupc_promotion.groupc_promotionid = parc_promotion.groupc_promotionid "
                        + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID=parc_promotion.reviewed_empid "
                        + "LEFT OUTER JOIN G_PROCESS_STATUS ON G_PROCESS_STATUS.STATUS_ID = groupc_promotion.status_id "
                        + "LEFT OUTER JOIN G_SPC ON parc_promotion.reviewed_spc=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE where fiscal_year =? LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, fiscalYear);
            } else if ((searchCriteria == null || searchCriteria.equals("")) && (searchGroupCStatus != null && !searchGroupCStatus.equals(""))) {
                pst = con.prepareStatement("SELECT groupc_promotion.status_id,fiscal_year,groupc_promotion.groupc_promotionid,reviewed_empid,reviewed_spc,EMP_MAST.EMP_ID,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,POST,EMP_MAST.CUR_OFF_CODE,GPF_NO,DOB,STATUS_NAME, "
                        + "reporting_remarks,reviewing_remarks,STATUS_NAME FROM groupc_promotion "
                        + "INNER JOIN parc_promotion on groupc_promotion.groupc_promotionid = parc_promotion.groupc_promotionid "
                        + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID=parc_promotion.reviewed_empid "
                        + "LEFT OUTER JOIN G_PROCESS_STATUS ON G_PROCESS_STATUS.STATUS_ID = groupc_promotion.status_id "
                        + "LEFT OUTER JOIN G_SPC ON parc_promotion.reviewed_spc=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE where fiscal_year =? and groupc_promotion.status_id=? LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, fiscalYear);
                pst.setString(2, searchGroupCStatus);
            } else if (searchCriteria.equals("empid")) {
                pst = con.prepareStatement("SELECT groupc_promotion.status_id,fiscal_year,groupc_promotion.groupc_promotionid,reviewed_empid,reviewed_spc,EMP_MAST.EMP_ID,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,POST,EMP_MAST.CUR_OFF_CODE,GPF_NO,DOB, "
                        + "reporting_remarks,reviewing_remarks,STATUS_NAME FROM groupc_promotion "
                        + "INNER JOIN parc_promotion on groupc_promotion.groupc_promotionid = parc_promotion.groupc_promotionid "
                        + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID=parc_promotion.reviewed_empid "
                        + "LEFT OUTER JOIN G_PROCESS_STATUS ON G_PROCESS_STATUS.STATUS_ID = groupc_promotion.status_id "
                        + "LEFT OUTER JOIN G_SPC ON parc_promotion.reviewed_spc=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE where fiscal_year =?  and reviewed_empid=?");

                pst.setString(1, fiscalYear);
                pst.setString(2, searchString);
            } else if (searchCriteria.equals("gpfno")) {
                pst = con.prepareStatement("SELECT groupc_promotion.status_id,fiscal_year,groupc_promotion.groupc_promotionid,reviewed_empid,reviewed_spc,EMP_MAST.EMP_ID,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,POST,EMP_MAST.CUR_OFF_CODE,GPF_NO,DOB, "
                        + "reporting_remarks,reviewing_remarks,STATUS_NAME FROM groupc_promotion "
                        + "INNER JOIN parc_promotion on groupc_promotion.groupc_promotionid = parc_promotion.groupc_promotionid "
                        + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID=parc_promotion.reviewed_empid "
                        + "LEFT OUTER JOIN G_PROCESS_STATUS ON G_PROCESS_STATUS.STATUS_ID = groupc_promotion.status_id "
                        + "LEFT OUTER JOIN G_SPC ON parc_promotion.reviewed_spc=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE where fiscal_year =? and GPF_NO=?");
                pst.setString(1, fiscalYear);
                pst.setString(2, searchString);
            } else if (searchCriteria.equals("empname")) {
                pst = con.prepareStatement("SELECT count (*) as cnt from "
                        + "(select status_id,fiscal_year,groupc_promotionid from groupc_promotion)groupc_promotion "
                        + "INNER JOIN (select groupc_promotionid,reviewed_empid,reviewed_spc,reporting_remarks,reviewing_remarks from parc_promotion) parc_promotion on groupc_promotion.groupc_promotionid = parc_promotion.groupc_promotionid "
                        + "INNER JOIN (select EMP_MAST.EMP_ID,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,EMP_MAST.CUR_OFF_CODE,GPF_NO,DOB from EMP_MAST)EMP_MAST ON EMP_MAST.EMP_ID=parc_promotion.reviewed_empid "
                        + "LEFT OUTER JOIN G_PROCESS_STATUS ON G_PROCESS_STATUS.STATUS_ID = groupc_promotion.status_id "
                        + "LEFT OUTER JOIN G_SPC ON parc_promotion.reviewed_spc=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE where fiscal_year =? and F_NAME=?");
                pst.setString(1, fiscalYear);
                pst.setString(2, searchString);
                res = pst.executeQuery();
                if (res.next()) {
                    groupCSearchResult.setTotalgroupCEmpFound(res.getInt("CNT"));
                }

                pst = con.prepareStatement("SELECT groupc_promotion.status_id,fiscal_year,groupc_promotion.groupc_promotionid,reviewed_empid,reviewed_spc,EMP_MAST.EMP_ID,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,POST,EMP_MAST.CUR_OFF_CODE,GPF_NO,DOB, "
                        + "reporting_remarks,reviewing_remarks,STATUS_NAME FROM groupc_promotion "
                        + "INNER JOIN parc_promotion on groupc_promotion.groupc_promotionid = parc_promotion.groupc_promotionid "
                        + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID=parc_promotion.reviewed_empid "
                        + "LEFT OUTER JOIN G_PROCESS_STATUS ON G_PROCESS_STATUS.STATUS_ID = groupc_promotion.status_id "
                        + "LEFT OUTER JOIN G_SPC ON parc_promotion.reviewed_spc=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE where fiscal_year =? and F_NAME=? LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setString(2, searchString);

            } else if (searchCriteria.equals("lastname")) {
                pst = con.prepareStatement("SELECT count (*) as cnt from "
                        + "(select status_id,fiscal_year,groupc_promotionid from groupc_promotion)groupc_promotion "
                        + "INNER JOIN (select groupc_promotionid,reviewed_empid,reviewed_spc,reporting_remarks,reviewing_remarks from parc_promotion) parc_promotion on groupc_promotion.groupc_promotionid = parc_promotion.groupc_promotionid "
                        + "INNER JOIN (select EMP_MAST.EMP_ID,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,EMP_MAST.CUR_OFF_CODE,GPF_NO,DOB from EMP_MAST)EMP_MAST ON EMP_MAST.EMP_ID=parc_promotion.reviewed_empid "
                        + "LEFT OUTER JOIN G_PROCESS_STATUS ON G_PROCESS_STATUS.STATUS_ID = groupc_promotion.status_id "
                        + "LEFT OUTER JOIN G_SPC ON parc_promotion.reviewed_spc=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE where fiscal_year =? and L_NAME=?");
                pst.setString(1, fiscalYear);
                pst.setString(2, searchString);
                res = pst.executeQuery();
                if (res.next()) {
                    groupCSearchResult.setTotalgroupCEmpFound(res.getInt("CNT"));
                }

                pst = con.prepareStatement("SELECT groupc_promotion.status_id,fiscal_year,groupc_promotion.groupc_promotionid,reviewed_empid,reviewed_spc,EMP_MAST.EMP_ID,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,POST,EMP_MAST.CUR_OFF_CODE,GPF_NO,DOB, "
                        + "reporting_remarks,reviewing_remarks,STATUS_NAME FROM groupc_promotion "
                        + "INNER JOIN parc_promotion on groupc_promotion.groupc_promotionid = parc_promotion.groupc_promotionid "
                        + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID=parc_promotion.reviewed_empid "
                        + "LEFT OUTER JOIN G_PROCESS_STATUS ON G_PROCESS_STATUS.STATUS_ID = groupc_promotion.status_id "
                        + "LEFT OUTER JOIN G_SPC ON parc_promotion.reviewed_spc=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE where fiscal_year =? and L_NAME=? LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setString(2, searchString);

            } else if (searchCriteria.equals("dob")) {
                pst = con.prepareStatement("SELECT count (*) as cnt from "
                        + "(select status_id,fiscal_year,groupc_promotionid from groupc_promotion)groupc_promotion "
                        + "INNER JOIN (select groupc_promotionid,reviewed_empid,reviewed_spc,reporting_remarks,reviewing_remarks from parc_promotion) parc_promotion on groupc_promotion.groupc_promotionid = parc_promotion.groupc_promotionid "
                        + "INNER JOIN (select EMP_MAST.EMP_ID,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,EMP_MAST.CUR_OFF_CODE,GPF_NO,DOB from EMP_MAST  WHERE L_NAME=?)EMP_MAST ON EMP_MAST.EMP_ID=parc_promotion.reviewed_empid "
                        + "LEFT OUTER JOIN G_PROCESS_STATUS ON G_PROCESS_STATUS.STATUS_ID = groupc_promotion.status_id "
                        + "LEFT OUTER JOIN G_SPC ON parc_promotion.reviewed_spc=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE where fiscal_year =? and dob= to_date(?,'DD-MM-yyyy')");
                pst.setString(1, fiscalYear);
                pst.setString(2, searchString);
                res = pst.executeQuery();
                if (res.next()) {
                    groupCSearchResult.setTotalgroupCEmpFound(res.getInt("CNT"));
                }

                pst = con.prepareStatement("SELECT groupc_promotion.status_id,fiscal_year,groupc_promotion.groupc_promotionid,reviewed_empid,reviewed_spc,EMP_MAST.EMP_ID,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,POST,EMP_MAST.CUR_OFF_CODE,GPF_NO,DOB, "
                        + "reporting_remarks,reviewing_remarks,STATUS_NAME FROM groupc_promotion "
                        + "INNER JOIN parc_promotion on groupc_promotion.groupc_promotionid = parc_promotion.groupc_promotionid "
                        + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID=parc_promotion.reviewed_empid "
                        + "LEFT OUTER JOIN G_PROCESS_STATUS ON G_PROCESS_STATUS.STATUS_ID = groupc_promotion.status_id "
                        + "LEFT OUTER JOIN G_SPC ON parc_promotion.reviewed_spc=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE where fiscal_year =? and dob= to_date(?,'DD-MM-yyyy') LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setString(2, searchString);

            } else if (searchCriteria.equals("mobileno")) {
                pst = con.prepareStatement("SELECT groupc_promotion.status_id,fiscal_year,groupc_promotion.groupc_promotionid,reviewed_empid,reviewed_spc,EMP_MAST.EMP_ID,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,POST,EMP_MAST.CUR_OFF_CODE,GPF_NO,DOB, "
                        + "reporting_remarks,reviewing_remarks,STATUS_NAME FROM groupc_promotion "
                        + "INNER JOIN parc_promotion on groupc_promotion.groupc_promotionid = parc_promotion.groupc_promotionid "
                        + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID=parc_promotion.reviewed_empid "
                        + "LEFT OUTER JOIN G_PROCESS_STATUS ON G_PROCESS_STATUS.STATUS_ID = groupc_promotion.status_id "
                        + "LEFT OUTER JOIN G_SPC ON parc_promotion.reviewed_spc=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE where fiscal_year =? and MOBILE=?");

                pst.setString(1, fiscalYear);
                pst.setString(2, searchString);
            }
            res = pst.executeQuery();
            int count = 0;
            while (res.next()) {
                count++;
                GroupCEmployee groupCEmployee = new GroupCEmployee();
                groupCEmployee.setReviewedempId(res.getString("EMP_ID"));
                String fname = res.getString("F_NAME").trim();
                String mname = "";
                String lname = "";

                if (res.getString("M_NAME") != null) {
                    mname = res.getString("M_NAME").trim();
                } else {
                    mname = "";
                }
                if (res.getString("L_NAME") != null && !res.getString("L_NAME").equals("")) {
                    lname = res.getString("L_NAME").trim();
                }
                groupCEmployee.setPageNo(((page - 1) * 20 + count) + "");
                groupCEmployee.setReviewedempname(fname + " " + mname + " " + lname);
                groupCEmployee.setDob(CommonFunctions.getFormattedOutputDate6(res.getDate("DOB")));
                groupCEmployee.setGpfno(res.getString("GPF_NO"));
                groupCEmployee.setReviewedEmpCurrentoffice(res.getString("CUR_OFF_CODE"));
                groupCEmployee.setReviewedpost(StringUtils.defaultString(res.getString("POST"), ""));
                groupCEmployee.setMobile(StringUtils.defaultString(res.getString("MOBILE"), ""));
                groupCEmployee.setGroupCpromotionId(res.getInt("groupc_promotionid"));
                groupCEmployee.setGroupCstatus(res.getString("STATUS_NAME"));
                groupCEmployee.setReportingRemarks(res.getString("reporting_remarks"));
                groupCEmployee.setReviewingRemarks(res.getString("reviewing_remarks"));
                groupCEmplist.add(groupCEmployee);
            }

            groupCSearchResult.setGroupCEmplist(groupCEmplist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return groupCSearchResult;
    }

    public List getGroupCStatusList() {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List<GroupCStatus> groupCStatusList = new ArrayList();
        GroupCStatus groupCStatus = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT STATUS_ID,STATUS_NAME FROM G_PROCESS_STATUS WHERE PROCESS_ID=15 ORDER BY STATUS_NAME";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                groupCStatus = new GroupCStatus();
                groupCStatus.setStatusId(rs.getString("STATUS_ID"));
                groupCStatus.setStatusName(rs.getString("STATUS_NAME"));
                groupCStatusList.add(groupCStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return groupCStatusList;
    }

    @Override
    public GroupCInitiatedbean getAuthorityDetail(int groupcPromotionid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        GroupCInitiatedbean groupCInitiatedbean = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("Select groupc_promotion.groupc_promotionid,promotion_id,reviewed_empid,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,POST,"
                    + "get_empname_from_type(groupc_promotion.reporting_empid ,'G') as reportingname,getspn(groupc_promotion.reporting_spc) as reportingspc,fiscal_year,"
                    + "get_empname_from_type(parc_promotion.reviewing_empid ,'G') as reviewingname,getspn(parc_promotion.reviewing_spc) as reviewingspc from parc_promotion "
                    + "INNER JOIN emp_mast ON  parc_promotion.reviewed_empid = emp_mast.emp_id "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "INNER JOIN groupc_promotion ON groupc_promotion.groupc_promotionid = parc_promotion.groupc_promotionid where groupc_promotion.groupc_promotionid =?");
            pst.setInt(1, groupcPromotionid);
            rs = pst.executeQuery();
            if (rs.next()) {
                groupCInitiatedbean = new GroupCInitiatedbean();
                groupCInitiatedbean.setGroupCpromotionId(rs.getInt("groupc_promotionid"));
                groupCInitiatedbean.setReportingempname(rs.getString("reportingname"));
                groupCInitiatedbean.setReportingpost(rs.getString("reportingspc"));
                groupCInitiatedbean.setFiscalyear(rs.getString("fiscal_year"));
                groupCInitiatedbean.setReportingdata(getSelectedgroupCEmpList(groupCInitiatedbean.getPromotionId()));
                groupCInitiatedbean.setReviewingempname(rs.getString("reviewingname"));
                groupCInitiatedbean.setReviewingpost(rs.getString("reviewingspc"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return groupCInitiatedbean;
    }

    @Override
    public List getOfficePriveligedList(String spc) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        List<ParAssignPrivilage> officeList = new ArrayList();
        ParAssignPrivilage pap = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT groupc_authority_admin.off_code,off_en FROM groupc_authority_admin  "
                    + "inner join g_office on groupc_authority_admin.off_code = g_office.off_code "
                    + "where spc=? ORDER BY off_code DESC");
            pst.setString(1, spc);
            rs = pst.executeQuery();
            while (rs.next()) {
                pap = new ParAssignPrivilage();
                pap.setOffCode(rs.getString("off_code"));
                pap.setOffName(rs.getString("off_en"));
                officeList.add(pap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officeList;
    }

    @Override
    public List getDistrictPriveligedList(String spc) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        List<ParAssignPrivilage> districtlist = new ArrayList();
        ParAssignPrivilage pap = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT groupc_authority_admin.dist_code,dist_name FROM groupc_authority_admin  "
                    + "inner join g_district on groupc_authority_admin.dist_code = g_district.dist_code "
                    + "where spc=? ORDER BY off_code DESC");
            pst.setString(1, spc);
            rs = pst.executeQuery();

            while (rs.next()) {
                pap = new ParAssignPrivilage();
                pap.setDistCode(rs.getString("dist_code"));
                pap.setDistName(rs.getString("dist_name"));
                districtlist.add(pap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return districtlist;
    }

    @Override
    public GroupCEmployee getFromDateToDateDetail(int promotionId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        GroupCEmployee groupCEmployee = new GroupCEmployee();
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT from_date_reporting,to_date_reporting,assessment_type_reporting FROM parc_promotion where promotion_id=?");
            pst.setInt(1, promotionId);
            rs = pst.executeQuery();

            if (rs.next()) {
                groupCEmployee.setPeriodFromReporting(CommonFunctions.getFormattedOutputDate1(rs.getDate("from_date_reporting")));
                groupCEmployee.setPeriodToReporting(CommonFunctions.getFormattedOutputDate1(rs.getDate("to_date_reporting")));
                groupCEmployee.setAssessmentTypeReporting(rs.getString("assessment_type_reporting"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return groupCEmployee;
    }

    //parCPromotionDAO.isDuplicatePARCPeriod(groupCEmployee.getReviewedempId(), parcfrmdt, parctodt, groupCEmployee.getFiscalyear(), groupCEmployee.getHidpromotionId());
    @Override
    public boolean isDuplicatePARCPeriod(String reviewedEmpId, String parcfrmdt, String parctodt, String fiscalyear, String promotionId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean duplPeriod = false;
        //boolean status = true;

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String sql = "";
        String dbf1 = "";
        String dbt1 = "";
        try {
            Date fd1 = formatter.parse(parcfrmdt);
            Date td1 = formatter.parse(parctodt);
            con = dataSource.getConnection();

            if (promotionId != null && !promotionId.equals("")) {
                sql = "SELECT from_date_reporting,to_date_reporting FROM parc_promotion "
                        + "inner join groupc_promotion on parc_promotion.groupc_promotionid = groupc_promotion.groupc_promotionid "
                        + "WHERE reviewed_empid=? AND FISCAL_YEAR=? AND promotion_id <> ? ";
                pst = con.prepareStatement(sql);
                pst.setString(1, reviewedEmpId);
                pst.setString(2, fiscalyear);
                pst.setInt(3, Integer.parseInt(promotionId));
            } else {
                sql = "SELECT from_date_reporting,to_date_reporting FROM parc_promotion "
                        + "inner join groupc_promotion on parc_promotion.groupc_promotionid = groupc_promotion.groupc_promotionid "
                        + "WHERE reviewed_empid=? AND FISCAL_YEAR=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, reviewedEmpId);
                pst.setString(2, fiscalyear);
            }
            rs = pst.executeQuery();
            while (rs.next()) {

                dbf1 = CommonFunctions.getFormattedOutputDate1(rs.getDate("from_date_reporting"));
                dbt1 = CommonFunctions.getFormattedOutputDate1(rs.getDate("to_date_reporting"));

                Date frs1 = formatter.parse(dbf1);
                Date trs1 = formatter.parse(dbt1);
                System.out.println("frs1 --" + frs1 + "trs1--" + trs1);
                System.out.println("fd1 --" + fd1 + "td1" + td1);
                System.out.println("fd1.compareTo(frs1) 1111" + fd1.compareTo(frs1));
                System.out.println("fd1.compareTo(trs1) 2222" + fd1.compareTo(trs1));
                System.out.println("td1.compareTo(trs1) 3333" + td1.compareTo(trs1));
                System.out.println("td1.compareTo(frs1) 4444" + td1.compareTo(frs1));

                if (fd1.compareTo(frs1) == 0 || td1.compareTo(trs1) == 0) {
                    System.out.println("Case-1");
                    duplPeriod = true;
                } else if (fd1.compareTo(frs1) < 0 && (td1.compareTo(frs1) > 0 && td1.compareTo(trs1) < 0)) {
                    System.out.println("Case-2");
                    duplPeriod = true;
                } else if (fd1.compareTo(frs1) > 0 && (fd1.compareTo(trs1) < 0 && td1.compareTo(trs1) > 0)) {
                    System.out.println("Case-3");
                    duplPeriod = true;
                } else if (fd1.compareTo(frs1) < 0 && fd1.compareTo(trs1) < 0 && td1.compareTo(frs1) > 0 && td1.compareTo(trs1) > 0) {
                    System.out.println("Case-4");
                    duplPeriod = true;
                } else if (fd1.compareTo(frs1) > 0 && fd1.compareTo(trs1) < 0 && td1.compareTo(frs1) > 0 && td1.compareTo(trs1) < 0) {
                    System.out.println("Case-5");
                    duplPeriod = true;
                } else {
                    duplPeriod = false;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return duplPeriod;
    }

    @Override
    public GroupCEmployee getFromDateToDateByEmpId(String reviewedEmpId, String fiscalyear) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        GroupCEmployee groupCEmployee = new GroupCEmployee();;

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,from_date_reporting,to_date_reporting FROM parc_promotion "
                    + "inner join groupc_promotion on parc_promotion.groupc_promotionid = groupc_promotion.groupc_promotionid "
                    + "INNER JOIN emp_mast ON parc_promotion.reviewed_empid=emp_mast.EMP_ID WHERE reviewed_empid=? AND FISCAL_YEAR=?");
            pstmt.setString(1, reviewedEmpId);
            pstmt.setString(2, fiscalyear);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                groupCEmployee.setPeriodFromReporting(CommonFunctions.getFormattedOutputDate1(rs.getDate("from_date_reporting")));
                groupCEmployee.setPeriodToReporting(CommonFunctions.getFormattedOutputDate1(rs.getDate("to_date_reporting")));
                groupCEmployee.setReviewedempname(rs.getString("EMPNAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return groupCEmployee;
    }

    public String getAssignedDepartmentForGroupC(String lmid, String offcode) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        String desc = "";
        try {
            con = dataSource.getConnection();
            //String sql = "SELECT DEPARTMENT_NAME FROM G_DEPARTMENT WHERE LMID='" + lmid + "' ORDER BY DEPARTMENT_NAME";
            String sql = "SELECT DEPARTMENT_NAME FROM G_DEPARTMENT WHERE LMID='" + lmid + "' "
                    + " UNION"
                    + " SELECT off_name DEPARTMENT_NAME FROM g_officiating WHERE off_id='" + offcode + "' ORDER BY DEPARTMENT_NAME";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                if (desc != null && !desc.equals("")) {
                    desc = desc + ", " + rs.getString("DEPARTMENT_NAME");
                } else {
                    desc = rs.getString("DEPARTMENT_NAME");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return desc;
    }

}
