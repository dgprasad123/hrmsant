/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.conclusionproceedings;

import hrms.common.CommonFunctions;
import hrms.common.CommonMasterDataFunction;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.conclusionproceedings.ConclusionProceedings;
import hrms.model.conclusionproceedings.PunishmentDetails;
import hrms.model.notification.NotificationBean;
import hrms.model.parmast.GroupCEmployee;
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
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Manas
 */
public class ConclusionProceedingsDAOImpl implements ConclusionProceedingsDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public NotificationDAO notificationDao;

    private String uploadPath;
    
    protected ServiceBookLanguageDAO sbDAO;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setNotificationDao(NotificationDAO notificationDao) {
        this.notificationDao = notificationDao;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }
    
    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }
    //private String uploadPath;

    @Override
    public ArrayList getEmpConcProcList(String empId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList concprocList = new ArrayList();
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            pstmt = con.prepareStatement("select EMP_PROCEEDING.EMP_ID,PR_ID,SV_ID,NOT_ID,NOT_TYPE,DOE,DOI,DCR,TOE,IF_ASSUMED,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,ENT_DEPT,ENT_OFF,ENT_AUTH,NOTE,ISCANCELED,LINK_ID,IF_VISIBLE,RULE,"
                    + "init_notord_no,init_notord_dt,punishment_rewarded,scn_original_filename,scn_disk_filename,scn_file_type,is_validated "
                    + "from (SELECT * FROM EMP_PROCEEDING WHERE EMP_ID=?)EMP_PROCEEDING "
                    + "LEFT OUTER JOIN EMP_NOTIFICATION ON EMP_PROCEEDING.INIT_NOT_ID=EMP_NOTIFICATION.NOT_ID  order by DOE desc");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ConclusionProceedings cp = new ConclusionProceedings();
                cp.setEmpid(rs.getString("EMP_ID"));
                cp.setSvid(rs.getString("SV_ID"));
                cp.setCrnotid(rs.getInt("NOT_ID"));
                cp.setConcprocid(rs.getInt("PR_ID"));
                /*if (rs.getDate("DOE") != null && !rs.getDate("DOE").equals("")) {
                 Date svbdt = rs.getDate("DOE");
                 cp.setDoesvbk(CommonFunctions.getFormattedOutputDate1(svbdt));
                 } else {
                 cp.setDoesvbk("");
                 }*/

                if (rs.getString("DOI") != null && !rs.getString("DOI").equals("")) {
                    //cp.setDoesvbk(rs.getString("DOI"));
                    cp.setDoesvbk(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOI")));
                } else {
                    cp.setDoesvbk("");
                }

                cp.setConcprocOrNo(rs.getString("ORDNO"));

                if (rs.getDate("ORDDT") != null && !rs.getDate("ORDDT").equals("")) {
                    Date procdt = rs.getDate("ORDDT");
                    cp.setConcprocOrdate(CommonFunctions.getFormattedOutputDate1(procdt));
                } else {
                    cp.setConcprocOrdate("");
                }

                /*  For fetching name of office of a department according to Verifying Authority   */
                if (rs.getString("auth") != null && !rs.getString("auth").trim().equals("")) {
                    cp.setAuth(CommonMasterDataFunction.getSPN(rs.getString("auth"), stmt));
                } else if (rs.getString("OFF_CODE") != null && !rs.getString("OFF_CODE").trim().equals("")) {
                    cp.setAuth(CommonMasterDataFunction.getOffName(rs.getString("OFF_CODE"), stmt));
                } else if (rs.getString("DEPT_CODE") != null && !rs.getString("DEPT_CODE").trim().equals("")) {
                    cp.setAuth(CommonMasterDataFunction.getDeptName(rs.getString("DEPT_CODE"), stmt));
                }
                /*  For fetching name of office of a department according to Verifying Authority   */

                if (rs.getDate("DOI") != null && !rs.getDate("DOI").equals("")) {
                    Date scnoticedt = rs.getDate("DOI");
                    cp.setDoiscnotice(CommonFunctions.getFormattedOutputDate1(scnoticedt));
                } else {
                    cp.setDoiscnotice("");
                }

                if (rs.getDate("DCR") != null && !rs.getDate("DCR").equals("")) {
                    Date comprcptdt = rs.getDate("DCR");
                    cp.setComprcptdate(CommonFunctions.getFormattedOutputDate1(comprcptdt));
                } else {
                    cp.setComprcptdate("");
                }
                if (rs.getString("RULE") != null && !rs.getString("RULE").equals("")) {
                    cp.setRuleofproc(rs.getString("RULE"));
                } else {
                    cp.setRuleofproc("");
                }
                if (rs.getString("init_notord_no") != null && !rs.getString("init_notord_no").equals("")) {
                    cp.setInitNotOrdNo(rs.getString("init_notord_no"));
                } else {
                    cp.setInitNotOrdNo("");
                }
                if (rs.getString("init_notord_dt") != null && !rs.getString("init_notord_dt").equals("")) {
                    cp.setInitNotOrdDt(rs.getString("init_notord_dt"));
                } else {
                    cp.setInitNotOrdDt("");
                }
                if (rs.getString("punishment_rewarded") != null && !rs.getString("punishment_rewarded").equals("")) {
                    cp.setPunishmentRewarded(rs.getString("punishment_rewarded"));
                } else {
                    cp.setPunishmentRewarded("");
                }

                if (rs.getString("scn_original_filename") != null && !rs.getString("scn_original_filename").equals("")) {
                    cp.setOriginalFilename(rs.getString("scn_original_filename"));
                } else {
                    cp.setOriginalFilename("");
                }
                if (rs.getString("scn_disk_filename") != null && !rs.getString("scn_disk_filename").equals("")) {
                    cp.setDiskFileName(rs.getString("scn_disk_filename"));
                } else {
                    cp.setDiskFileName("");
                }
                if (rs.getString("scn_file_type") != null && !rs.getString("scn_file_type").equals("")) {
                    cp.setGetContentType(rs.getString("scn_file_type"));
                } else {
                    cp.setGetContentType("");
                }
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    cp.setChkNotSBPrint("Y");
                }
                 if (rs.getString("is_validated") != null && rs.getString("is_validated").equals("N")) {
                    cp.setIsValidated("Y");
                }

                concprocList.add(cp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return concprocList;
    }

    @Override
    public ArrayList getEmpConcProcListOfficewise(String offcode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList procListofficewise = new ArrayList();
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            pstmt = con.prepareStatement("SELECT EMP_MAST.EMP_ID, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,POST, PR_ID,SV_ID,NOT_ID,NOT_TYPE,DOE,DOI,DCR,TOE,IF_ASSUMED,ORDNO,ORDDT,AUTH,ENT_DEPT,ENT_OFF,ENT_AUTH,NOTE,ISCANCELED,LINK_ID,IF_VISIBLE,RULE,"
                    + "init_notord_no,init_notord_dt,punishment_rewarded,scn_original_filename,scn_disk_filename,scn_file_type FROM EMP_MAST "
                    + "INNER JOIN EMP_PROCEEDING ON EMP_PROCEEDING.EMP_ID = EMP_MAST.EMP_ID "
                    + "LEFT OUTER JOIN EMP_NOTIFICATION ON EMP_PROCEEDING.INIT_NOT_ID=EMP_NOTIFICATION.NOT_ID "
                    + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC "
                    + "LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE WHERE CUR_OFF_CODE =? order by DOE desc");
            pstmt.setString(1, offcode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ConclusionProceedings cp = new ConclusionProceedings();
                cp.setEmpid(rs.getString("EMP_ID"));
                cp.setFullName(rs.getString("EMP_NAME"));
                cp.setPost(rs.getString("POST"));
                cp.setSvid(rs.getString("SV_ID"));
                cp.setCrnotid(rs.getInt("NOT_ID"));
                cp.setConcprocid(rs.getInt("PR_ID"));
                /*if (rs.getDate("DOE") != null && !rs.getDate("DOE").equals("")) {
                 Date svbdt = rs.getDate("DOE");
                 cp.setDoesvbk(CommonFunctions.getFormattedOutputDate1(svbdt));
                 } else {
                 cp.setDoesvbk("");
                 }*/

                if (rs.getString("DOI") != null && !rs.getString("DOI").equals("")) {
                    //cp.setDoesvbk(rs.getString("DOI"));
                    cp.setDoesvbk(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOI")));
                } else {
                    cp.setDoesvbk("");
                }

                cp.setConcprocOrNo(rs.getString("ORDNO"));

                if (rs.getDate("ORDDT") != null && !rs.getDate("ORDDT").equals("")) {
                    Date procdt = rs.getDate("ORDDT");
                    cp.setConcprocOrdate(CommonFunctions.getFormattedOutputDate1(procdt));
                } else {
                    cp.setConcprocOrdate("");
                }

                /*  For fetching name of office of a department according to Verifying Authority   */
                if (rs.getString("auth") != null && !rs.getString("auth").trim().equals("")) {
                    cp.setAuth(CommonMasterDataFunction.getSPN(rs.getString("auth"), stmt));

                }
                /*  For fetching name of office of a department according to Verifying Authority   */

                if (rs.getDate("DOI") != null && !rs.getDate("DOI").equals("")) {
                    Date scnoticedt = rs.getDate("DOI");
                    cp.setDoiscnotice(CommonFunctions.getFormattedOutputDate1(scnoticedt));
                } else {
                    cp.setDoiscnotice("");
                }

                if (rs.getDate("DCR") != null && !rs.getDate("DCR").equals("")) {
                    Date comprcptdt = rs.getDate("DCR");
                    cp.setComprcptdate(CommonFunctions.getFormattedOutputDate1(comprcptdt));
                } else {
                    cp.setComprcptdate("");
                }
                if (rs.getString("RULE") != null && !rs.getString("RULE").equals("")) {
                    cp.setRuleofproc(rs.getString("RULE"));
                } else {
                    cp.setRuleofproc("");
                }
                if (rs.getString("init_notord_no") != null && !rs.getString("init_notord_no").equals("")) {
                    cp.setInitNotOrdNo(rs.getString("init_notord_no"));
                } else {
                    cp.setInitNotOrdNo("");
                }
                if (rs.getString("init_notord_dt") != null && !rs.getString("init_notord_dt").equals("")) {
                    cp.setInitNotOrdDt(rs.getString("init_notord_dt"));
                } else {
                    cp.setInitNotOrdDt("");
                }
                if (rs.getString("punishment_rewarded") != null && !rs.getString("punishment_rewarded").equals("")) {
                    cp.setPunishmentRewarded(rs.getString("punishment_rewarded"));
                } else {
                    cp.setPunishmentRewarded("");
                }

                if (rs.getString("scn_original_filename") != null && !rs.getString("scn_original_filename").equals("")) {
                    cp.setOriginalFilename(rs.getString("scn_original_filename"));
                } else {
                    cp.setOriginalFilename("");
                }
                if (rs.getString("scn_disk_filename") != null && !rs.getString("scn_disk_filename").equals("")) {
                    cp.setDiskFileName(rs.getString("scn_disk_filename"));
                } else {
                    cp.setDiskFileName("");
                }
                if (rs.getString("scn_file_type") != null && !rs.getString("scn_file_type").equals("")) {
                    cp.setGetContentType(rs.getString("scn_file_type"));
                } else {
                    cp.setGetContentType("");
                }

                procListofficewise.add(cp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return procListofficewise;
    }

    @Override
    public List getConclusionProceedingEmpList(String offCode, String empid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList emplistForConclusionProceeding = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,emp_mast.emp_id,gpf_no,cur_spc,POST from emp_mast "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "LEFT OUTER JOIN (SELECT emp_id FROM emp_proceeding where emp_proceeding.emp_id = ?) AS emp_proceeding ON emp_mast.emp_id = emp_proceeding.emp_id "
                    + "where cur_off_code=? and dep_code='02'  ORDER BY EMPNAME");
            pstmt.setString(1, empid);
            pstmt.setString(2, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ConclusionProceedings proceeding = new ConclusionProceedings();
                proceeding.setEmpNameForProceeding(rs.getString("EMPNAME"));
                proceeding.setEmpid(rs.getString("emp_id"));
                proceeding.setEmpSpcForProceeding(rs.getString("cur_spc"));
                proceeding.setGpfNo(rs.getString("gpf_no"));
                proceeding.setEmpPostForProceeding(rs.getString("POST"));
                /*  if (rs.getString("emp_id") != null && !rs.getString("emp_id").equals("")) {
                 proceeding.setAlreadyAdded("Y");
                 } else {
                 proceeding.setAlreadyAdded("N");
                 } */
                emplistForConclusionProceeding.add(proceeding);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return emplistForConclusionProceeding;
    }

    public List getConclusionProceedingEmpListByGPFNo(String searchby, String gpfno) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList emplistForConclusionProceeding = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,emp_mast.emp_id,gpf_no,cur_spc,POST from emp_mast "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "where gpf_no=?  ORDER BY EMPNAME");
            pstmt.setString(1, gpfno);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ConclusionProceedings proceeding = new ConclusionProceedings();
                proceeding.setEmpNameForProceeding(rs.getString("EMPNAME"));
                proceeding.setEmpid(rs.getString("emp_id"));
                proceeding.setEmpSpcForProceeding(rs.getString("cur_spc"));
                proceeding.setGpfNo(rs.getString("gpf_no"));
                proceeding.setEmpPostForProceeding(rs.getString("POST"));
                /*  if (rs.getString("emp_id") != null && !rs.getString("emp_id").equals("")) {
                 proceeding.setAlreadyAdded("Y");
                 } else {
                 proceeding.setAlreadyAdded("N");
                 } */
                emplistForConclusionProceeding.add(proceeding);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return emplistForConclusionProceeding;
    }

    @Override
    public void saveConclusionProceedingEmpList(ConclusionProceedings conclusionProceedings) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            Long curtime = new Date().getTime();
            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO emp_proceeding(emp_id,cur_spc,doi,init_by_emp_id,init_by_spc) VALUES(?,?,?,?,?)");
            pst.setString(1, conclusionProceedings.getEmpid());
            pst.setString(2, conclusionProceedings.getEmpSpcForProceeding());
            pst.setTimestamp(3, new Timestamp(curtime));
            pst.setString(4, conclusionProceedings.getInitiatedByempId());
            pst.setString(5, conclusionProceedings.getInitiatedByspc());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getSelectedEmpListForConclusionProceeding(String initiatedByempId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList selectedempList = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("Select emp_proceeding.emp_id, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,emp_proceeding.cur_spc,doi,post,init_by_emp_id,init_by_spc from emp_proceeding "
                    + "INNER JOIN emp_mast ON  emp_proceeding.emp_id = emp_mast.emp_id "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE where init_by_emp_id =? ");
            pstmt.setString(1, initiatedByempId);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                ConclusionProceedings conclusionProceedings = new ConclusionProceedings();

                conclusionProceedings.setEmpid(rs.getString("emp_id"));
                conclusionProceedings.setEmpNameForProceeding(rs.getString("EMPNAME"));
                conclusionProceedings.setEmpSpcForProceeding(rs.getString("cur_spc"));
                conclusionProceedings.setInitonDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("doi")));
                conclusionProceedings.setEmpPostForProceeding(rs.getString("POST"));
                conclusionProceedings.setInitiatedByempId(rs.getString("init_by_emp_id"));
                conclusionProceedings.setInitiatedByspc(rs.getString("init_by_spc"));

                selectedempList.add(conclusionProceedings);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return selectedempList;
    }

    @Override
    public ConclusionProceedings getConclusionProceedingData(int concprocid) {
        ConclusionProceedings conclusionProceedings = new ConclusionProceedings();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT emp_proceeding.EMP_ID,IF_INITIATED,INIT_NOT_ID,CUR_SPC,getgpostnamefromspc(CUR_SPC) AS postedPostName,"
                    + "ORDNO,ORDDT,DEPT_CODE,OFF_CODE,rule,AUTH,getgpostnamefromspc(AUTH) AS notauthority, SCN_ISSUED, SCN_NOT_ID, DOI, CR_NOT_ID,DCR,IF_FREE,"
                    + "RES_NOT_ID,scn_original_filename,scn_disk_filename,scn_file_type,punishment_rewarded,punishment_type,narrationfor_freefromcharge FROM emp_proceeding "
                    + "INNER JOIN EMP_NOTIFICATION ON emp_proceeding.INIT_NOT_ID = EMP_NOTIFICATION.NOT_ID "
                    + "WHERE pr_id = ?");
            pstmt.setInt(1, concprocid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                conclusionProceedings.setConcprocid(concprocid);
                conclusionProceedings.setInitNotOrdNo(rs.getString("ORDNO"));
                conclusionProceedings.setInitNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                conclusionProceedings.setInitnotid(rs.getInt("INIT_NOT_ID"));
                conclusionProceedings.setEmpid(rs.getString("EMP_ID"));
                conclusionProceedings.setPostedPostName(rs.getString("postedPostName"));
                conclusionProceedings.setPostedspc(rs.getString("CUR_SPC"));
                conclusionProceedings.setNotdept(rs.getString("DEPT_CODE"));
                conclusionProceedings.setNotoffice(rs.getString("OFF_CODE"));
                conclusionProceedings.setRuleofproc(rs.getString("rule"));
                conclusionProceedings.setNotspc(rs.getString("AUTH"));
                conclusionProceedings.setNotauthority(rs.getString("notauthority"));
                conclusionProceedings.setScnnotid(rs.getInt("SCN_NOT_ID"));
                conclusionProceedings.setShowcausedate(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOI")));
                conclusionProceedings.setCrnotid(rs.getInt("CR_NOT_ID"));
                conclusionProceedings.setFreefromcharges(rs.getString("IF_FREE"));
                conclusionProceedings.setResnotid(rs.getInt("RES_NOT_ID"));
                conclusionProceedings.setReceiprofcompliancedate(CommonFunctions.getFormattedOutputDate1(rs.getDate("DCR")));
                conclusionProceedings.setOriginalFilename(rs.getString("scn_original_filename"));
                conclusionProceedings.setDiskFileName(rs.getString("scn_disk_filename"));
                conclusionProceedings.setGetContentType(rs.getString("scn_file_type"));
                conclusionProceedings.setPunishmentRewarded(rs.getString("punishment_rewarded"));
                conclusionProceedings.setPunishmentType(rs.getString("punishment_type"));
                conclusionProceedings.setNarrationForFreeCharge(rs.getString("narrationfor_freefromcharge"));

            }

            pstmt = con.prepareStatement("SELECT ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,getgpostnamefromspc(AUTH) AS notauthority FROM EMP_NOTIFICATION "
                    + "where NOT_ID=?");
            pstmt.setInt(1, conclusionProceedings.getScnnotid());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                conclusionProceedings.setShowcauseOrdNo(rs.getString("ORDNO"));
                conclusionProceedings.setShowcauseOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                conclusionProceedings.setShowcausenotdept(rs.getString("DEPT_CODE"));
                conclusionProceedings.setShowcausenotoffice(rs.getString("OFF_CODE"));
                conclusionProceedings.setShowcausenotspc(rs.getString("AUTH"));
                conclusionProceedings.setShowcausenotauthority(rs.getString("notauthority"));
            }

            pstmt = con.prepareStatement("SELECT ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,getgpostnamefromspc(AUTH) AS notauthority FROM EMP_NOTIFICATION "
                    + "where NOT_ID=?");
            pstmt.setInt(1, conclusionProceedings.getCrnotid());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                conclusionProceedings.setReceiprofcomplianceOrdNo(rs.getString("ORDNO"));
                conclusionProceedings.setReceiprofcomplianceOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                conclusionProceedings.setReceiprofcompliancenotdept(rs.getString("DEPT_CODE"));
                conclusionProceedings.setReceiprofcompliancenotoffice(rs.getString("OFF_CODE"));
                conclusionProceedings.setReceiprofcompliancenotspc(rs.getString("AUTH"));
                conclusionProceedings.setReceiprofcompliancenotauthority(rs.getString("notauthority"));
            }

            pstmt = con.prepareStatement("SELECT ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,getgpostnamefromspc(AUTH) AS notauthority FROM EMP_NOTIFICATION "
                    + "where NOT_ID=?");
            pstmt.setInt(1, conclusionProceedings.getResnotid());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                conclusionProceedings.setConclusionOrdNo(rs.getString("ORDNO"));
                conclusionProceedings.setConclusionOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                conclusionProceedings.setConclusionnotdept(rs.getString("DEPT_CODE"));
                conclusionProceedings.setConclusionnotoffice(rs.getString("OFF_CODE"));
                conclusionProceedings.setConclusionnotspc(rs.getString("AUTH"));
                conclusionProceedings.setConclusionnotauthority(rs.getString("notauthority"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return conclusionProceedings;
    }

    public ConclusionProceedings getAttachedFile(int concprocid) {
        ConclusionProceedings conclusionProceedings = new ConclusionProceedings();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select scn_original_filename,scn_disk_filename,scn_file_type,scn_file_path from EMP_PROCEEDING where pr_id = ?");
            pst.setInt(1, concprocid);
            res = pst.executeQuery();
            String filepath = null;
            if (res.next()) {
                conclusionProceedings.setOriginalFilename(res.getString("scn_original_filename"));
                conclusionProceedings.setDiskFileName(res.getString("scn_disk_filename"));
                conclusionProceedings.setGetContentType("scn_file_type");
                filepath = res.getString("scn_file_path");
            }
            File f = new File(filepath + File.separator + conclusionProceedings.getDiskFileName());
            conclusionProceedings.setFilecontent(FileUtils.readFileToByteArray(f));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return conclusionProceedings;
    }

    @Override
    public void saveInitiationDetailsForBacklog(ConclusionProceedings conclusionProceedings) {
        Connection con = null;
        PreparedStatement pstmt = null;
        NotificationBean nb = new NotificationBean();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String originalFileName = null;
        String diskfileName = null;
        String contentType = null;
        try {

            con = dataSource.getConnection();
            if (conclusionProceedings.getUploadDocument() != null && !conclusionProceedings.getUploadDocument().isEmpty()) {
                diskfileName = new Date().getTime() + "";
                originalFileName = conclusionProceedings.getUploadDocument().getOriginalFilename();
                contentType = conclusionProceedings.getUploadDocument().getContentType();
                byte[] bytes = conclusionProceedings.getUploadDocument().getBytes();
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

            int notid = notificationDao.insertNotificationData(nb);

            pstmt = con.prepareStatement("INSERT INTO EMP_PROCEEDING(EMP_ID,IF_INITIATED,INIT_NOT_ID,CUR_SPC,doi,rule,scn_original_filename,scn_disk_filename,scn_file_type,scn_file_path,init_notord_no,init_notord_dt,punishment_rewarded) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, conclusionProceedings.getEmpid());
            pstmt.setString(2, "Y");
            pstmt.setInt(3, notid);
            pstmt.setString(4, conclusionProceedings.getPostedspc());
            pstmt.setTimestamp(5, new Timestamp(curtime));
            pstmt.setString(6, conclusionProceedings.getRuleofproc());
            pstmt.setString(7, originalFileName);
            pstmt.setString(8, diskfileName);
            pstmt.setString(9, contentType);
            pstmt.setString(10, this.uploadPath);
            pstmt.setString(11, conclusionProceedings.getInitNotOrdNo());
            pstmt.setString(12, conclusionProceedings.getInitNotOrdDt());
            pstmt.setString(13, conclusionProceedings.getPunishmentRewarded());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            int prId = rs.getInt("pr_id");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveInitiationDetails(ConclusionProceedings conclusionProceedings) {
        Connection con = null;
        PreparedStatement pstmt = null;
        NotificationBean nb = new NotificationBean();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String originalFileName = null;
        String diskfileName = null;
        String contentType = null;
        //int hidnotid = 0;
        int notid = 0;
        try {

            con = dataSource.getConnection();
            if (conclusionProceedings.getEmpid() != null && !conclusionProceedings.getEmpid().equals("")) {
                if (conclusionProceedings.getConcprocid() == 0) {

                    Calendar cal = Calendar.getInstance();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
                    String startTime = dateFormat.format(cal.getTime());
                    Long curtime = new Date().getTime();

                    nb = new NotificationBean();
                    nb.setNottype("INITIATION");
                    nb.setEmpId(conclusionProceedings.getEmpid());
                    nb.setDateofEntry(new Date());
                    nb.setOrdno(conclusionProceedings.getInitNotOrdNo());
                    nb.setOrdDate(sdf.parse(conclusionProceedings.getInitNotOrdDt()));
                    nb.setSancDeptCode(conclusionProceedings.getNotdept());
                    nb.setSancOffCode(conclusionProceedings.getNotoffice());
                    nb.setSancAuthCode(conclusionProceedings.getNotspc());
                    nb.setEntryDeptCode(conclusionProceedings.getNotdept());
                    nb.setEntryOffCode(conclusionProceedings.getNotoffice());
                    nb.setEntryAuthCode(conclusionProceedings.getNotspc());
                    nb.setNote("");
                    notid = notificationDao.insertNotificationData(nb);
                    
                    pstmt = con.prepareStatement("INSERT INTO EMP_PROCEEDING(EMP_ID,IF_INITIATED,INIT_NOT_ID,CUR_SPC,doi,rule) VALUES(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                    pstmt.setString(1, conclusionProceedings.getEmpid());
                    pstmt.setString(2, "Y");
                    pstmt.setInt(3, notid);
                    pstmt.setString(4, conclusionProceedings.getPostedspc());
                    pstmt.setTimestamp(5, new Timestamp(curtime));
                    pstmt.setString(6, conclusionProceedings.getRuleofproc());
                    pstmt.executeUpdate();
                    ResultSet rs = pstmt.getGeneratedKeys();
                    rs.next();
                    int prId = rs.getInt("pr_id");
                } else {
                    nb = new NotificationBean();
                    nb.setNotid(conclusionProceedings.getInitnotid());
                    notid = nb.getNotid();
                    nb.setNottype("INITIATION");
                    nb.setEmpId(conclusionProceedings.getEmpid());
                    nb.setDateofEntry(new Date());
                    nb.setOrdno(conclusionProceedings.getInitNotOrdNo());
                    nb.setOrdDate(sdf.parse(conclusionProceedings.getInitNotOrdDt()));
                    nb.setSancDeptCode(conclusionProceedings.getNotdept());
                    nb.setSancOffCode(conclusionProceedings.getNotoffice());
                    nb.setSancAuthCode(conclusionProceedings.getNotspc());
                    nb.setEntryDeptCode(conclusionProceedings.getNotdept());
                    nb.setEntryOffCode(conclusionProceedings.getNotoffice());
                    nb.setEntryAuthCode(conclusionProceedings.getNotspc());
                    nb.setNote("");
                    notificationDao.modifyNotificationData(nb);
                    pstmt = con.prepareStatement("UPDATE EMP_PROCEEDING SET CUR_SPC=? WHERE pr_id=? AND EMP_ID=?");
                    pstmt.setString(1, conclusionProceedings.getPostedspc());
                    pstmt.setInt(2, conclusionProceedings.getConcprocid());
                    pstmt.setString(3, conclusionProceedings.getEmpid());
                    pstmt.executeUpdate();
                }
                
                /*if (conclusionProceedings.getChkNotSBPrint() != null && conclusionProceedings.getChkNotSBPrint().equals("Y")) {
                    pstmt = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                    pstmt.setInt(1, notid);
                    pstmt.execute();
                } else {*/
                   // String sbLang = sbDAO.getInitProceedingsDetails(conclusionProceedings);
                    String sbLang = sbDAO.getInitProceedingsDetails(conclusionProceedings,conclusionProceedings.getPunishmentRewarded());
                    pstmt = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=? WHERE NOT_ID=? ");
                    pstmt.setString(1, sbLang);
                    pstmt.setInt(2, notid);
                    pstmt.execute();
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
      //return hidnotid;  
    }

    @Override
    public void deleteInitiationDetails(ConclusionProceedings conclusionProceedings) {
        PreparedStatement pst = null;
        Connection con = null;
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM EMP_NOTIFICATION WHERE NOT_ID=? AND NOT_TYPE='INITIATION' ");
            // pst.setString(1, conclusionProceedings.getEmpid());
            pst.setInt(1, conclusionProceedings.getCrnotid());
            pst.executeUpdate();
            pst = con.prepareStatement("DELETE FROM EMP_PROCEEDING WHERE EMP_ID=? AND pr_id=?");
            pst.setString(1, conclusionProceedings.getEmpid());
            pst.setInt(2, conclusionProceedings.getConcprocid());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveShowCauseDetails(ConclusionProceedings conclusionProceedings) {
        Connection con = null;
        PreparedStatement pstmt = null;
        NotificationBean nb = new NotificationBean();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {

            if (conclusionProceedings.getShowcauseOrdNo() != null && !conclusionProceedings.getShowcauseOrdNo().equals("")
                    && conclusionProceedings.getShowcauseOrdDt() != null && !conclusionProceedings.getShowcauseOrdDt().equals("")
                    && conclusionProceedings.getShowcausenotdept() != null && !conclusionProceedings.getShowcausenotdept().equals("")
                    && conclusionProceedings.getShowcausenotoffice() != null && !conclusionProceedings.getShowcausenotoffice().equals("")
                    && conclusionProceedings.getShowcausenotspc() != null && !conclusionProceedings.getShowcausenotspc().equals("")
                    && conclusionProceedings.getShowcausedate() != null && !conclusionProceedings.getShowcausedate().equals("")) {
                con = dataSource.getConnection();
                if (conclusionProceedings.getScnnotid() == 0) {
                    nb = new NotificationBean();
                    nb.setNottype("SC_NOTICE");
                    nb.setEmpId(conclusionProceedings.getEmpid());
                    nb.setDateofEntry(new Date());
                    nb.setOrdno(conclusionProceedings.getShowcauseOrdNo());
                    nb.setOrdDate(sdf.parse(conclusionProceedings.getShowcauseOrdDt()));
                    nb.setSancDeptCode(conclusionProceedings.getShowcausenotdept());
                    nb.setSancOffCode(conclusionProceedings.getShowcausenotoffice());
                    nb.setSancAuthCode(conclusionProceedings.getShowcausenotspc());
                    nb.setEntryDeptCode(conclusionProceedings.getShowcausenotdept());
                    nb.setEntryOffCode(conclusionProceedings.getShowcausenotoffice());
                    nb.setEntryAuthCode(conclusionProceedings.getShowcausenotspc());
                    nb.setNote("");
                    int notid = notificationDao.insertNotificationData(nb);

                    pstmt = con.prepareStatement("UPDATE EMP_PROCEEDING SET SCN_ISSUED='Y',SCN_NOT_ID=?,DOI=? WHERE PR_ID=? AND EMP_ID=?");
                    pstmt.setInt(1, notid);
                    pstmt.setTimestamp(2, new Timestamp(sdf.parse(conclusionProceedings.getShowcausedate()).getTime()));
                    pstmt.setInt(3, conclusionProceedings.getConcprocid());
                    pstmt.setString(4, conclusionProceedings.getEmpid());
                    pstmt.executeUpdate();
                } else {
                    nb = new NotificationBean();
                    nb.setNotid(conclusionProceedings.getScnnotid());
                    nb.setDateofEntry(new Date());
                    nb.setOrdno(conclusionProceedings.getShowcauseOrdNo());
                    nb.setOrdDate(sdf.parse(conclusionProceedings.getShowcauseOrdDt()));
                    nb.setSancDeptCode(conclusionProceedings.getShowcausenotdept());
                    nb.setSancOffCode(conclusionProceedings.getShowcausenotoffice());
                    nb.setSancAuthCode(conclusionProceedings.getShowcausenotspc());
                    nb.setEntryDeptCode(conclusionProceedings.getShowcausenotdept());
                    nb.setEntryOffCode(conclusionProceedings.getShowcausenotoffice());
                    nb.setEntryAuthCode(conclusionProceedings.getShowcausenotspc());
                    nb.setNote("");
                    notificationDao.modifyNotificationData(nb);

                    pstmt = con.prepareStatement("UPDATE EMP_PROCEEDING SET SCN_ISSUED='Y',DOI=? WHERE PR_ID=? AND EMP_ID=?");
                    pstmt.setTimestamp(1, new Timestamp(sdf.parse(conclusionProceedings.getShowcausedate()).getTime()));
                    pstmt.setInt(2, conclusionProceedings.getConcprocid());
                    pstmt.setString(3, conclusionProceedings.getEmpid());
                    pstmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveComplianceDetails(ConclusionProceedings conclusionProceedings) {
        Connection con = null;
        PreparedStatement pstmt = null;
        NotificationBean nb = new NotificationBean();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            if (conclusionProceedings.getReceiprofcomplianceOrdNo() != null && !conclusionProceedings.getReceiprofcomplianceOrdNo().equals("")
                    && conclusionProceedings.getReceiprofcomplianceOrdDt() != null && !conclusionProceedings.getReceiprofcomplianceOrdDt().equals("")
                    && conclusionProceedings.getReceiprofcompliancenotdept() != null && !conclusionProceedings.getReceiprofcompliancenotdept().equals("")
                    && conclusionProceedings.getReceiprofcompliancenotoffice() != null && !conclusionProceedings.getReceiprofcompliancenotoffice().equals("")
                    && conclusionProceedings.getReceiprofcompliancenotspc() != null && !conclusionProceedings.getReceiprofcompliancenotspc().equals("")
                    && conclusionProceedings.getReceiprofcompliancedate() != null && !conclusionProceedings.getReceiprofcompliancedate().equals("")) {
                con = dataSource.getConnection();
                if (conclusionProceedings.getCrnotid() == 0) {
                    nb = new NotificationBean();
                    nb.setNottype("COMPLIANCE");
                    nb.setEmpId(conclusionProceedings.getEmpid());
                    nb.setDateofEntry(new Date());
                    nb.setOrdno(conclusionProceedings.getReceiprofcomplianceOrdNo());
                    nb.setOrdDate(sdf.parse(conclusionProceedings.getReceiprofcomplianceOrdDt()));
                    nb.setSancDeptCode(conclusionProceedings.getReceiprofcompliancenotdept());
                    nb.setSancOffCode(conclusionProceedings.getReceiprofcompliancenotoffice());
                    nb.setSancAuthCode(conclusionProceedings.getReceiprofcompliancenotspc());
                    nb.setEntryDeptCode(conclusionProceedings.getReceiprofcompliancenotdept());
                    nb.setEntryOffCode(conclusionProceedings.getReceiprofcompliancenotoffice());
                    nb.setEntryAuthCode(conclusionProceedings.getReceiprofcompliancenotspc());
                    nb.setNote("");
                    int notid = notificationDao.insertNotificationData(nb);

                    pstmt = con.prepareStatement("UPDATE EMP_PROCEEDING SET IF_CR='Y',CR_NOT_ID=?,DCR=? WHERE PR_ID=? AND EMP_ID=?");
                    pstmt.setInt(1, notid);
                    pstmt.setTimestamp(2, new Timestamp(sdf.parse(conclusionProceedings.getReceiprofcompliancedate()).getTime()));
                    pstmt.setInt(3, conclusionProceedings.getConcprocid());
                    pstmt.setString(4, conclusionProceedings.getEmpid());
                    pstmt.executeUpdate();
                } else {
                    nb = new NotificationBean();
                    nb.setNotid(conclusionProceedings.getCrnotid());
                    nb.setDateofEntry(new Date());
                    nb.setOrdno(conclusionProceedings.getReceiprofcomplianceOrdNo());
                    nb.setOrdDate(sdf.parse(conclusionProceedings.getReceiprofcomplianceOrdDt()));
                    nb.setSancDeptCode(conclusionProceedings.getReceiprofcompliancenotdept());
                    nb.setSancOffCode(conclusionProceedings.getReceiprofcompliancenotoffice());
                    nb.setSancAuthCode(conclusionProceedings.getReceiprofcompliancenotspc());
                    nb.setEntryDeptCode(conclusionProceedings.getReceiprofcompliancenotdept());
                    nb.setEntryOffCode(conclusionProceedings.getReceiprofcompliancenotoffice());
                    nb.setEntryAuthCode(conclusionProceedings.getReceiprofcompliancenotspc());
                    nb.setNote("");
                    notificationDao.modifyNotificationData(nb);

                    pstmt = con.prepareStatement("UPDATE EMP_PROCEEDING SET IF_CR='Y',DCR=? WHERE PR_ID=? AND EMP_ID=?");
                    pstmt.setTimestamp(1, new Timestamp(sdf.parse(conclusionProceedings.getReceiprofcompliancedate()).getTime()));
                    pstmt.setInt(2, conclusionProceedings.getConcprocid());
                    pstmt.setString(3, conclusionProceedings.getEmpid());
                    pstmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveResultDetails(ConclusionProceedings conclusionProceedings) {
        Connection con = null;
        PreparedStatement pstmt = null;
        NotificationBean nb = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String originalFileName = null;
        String diskfileName = null;
        String contentType = null;
        try {
            if (conclusionProceedings.getConclusionOrdNo() != null && !conclusionProceedings.getConclusionOrdNo().equals("")
                    && conclusionProceedings.getConclusionOrdDt() != null && !conclusionProceedings.getConclusionOrdDt().equals("")) {
                con = dataSource.getConnection();

                if (conclusionProceedings.getUploadDocument() != null && !conclusionProceedings.getUploadDocument().isEmpty()) {
                    diskfileName = new Date().getTime() + "";
                    originalFileName = conclusionProceedings.getUploadDocument().getOriginalFilename();
                    contentType = conclusionProceedings.getUploadDocument().getContentType();
                    byte[] bytes = conclusionProceedings.getUploadDocument().getBytes();
                    File dir = new File(this.uploadPath);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File serverFile = new File(dir.getAbsolutePath() + File.separator + diskfileName);
                    FileOutputStream fout = new FileOutputStream(serverFile);
                    fout.write(bytes);
                    fout.close();

                    pstmt = con.prepareStatement("UPDATE EMP_PROCEEDING SET scn_original_filename=?,scn_disk_filename=?,scn_file_type=?,scn_file_path=?,punishment_type=?,narrationfor_freefromcharge=? WHERE PR_ID=? AND EMP_ID=?");
                    pstmt.setString(1, originalFileName);
                    pstmt.setString(2, diskfileName);
                    pstmt.setString(3, contentType);
                    pstmt.setString(4, this.uploadPath);
                    pstmt.setString(5, conclusionProceedings.getPunishmentType());
                    pstmt.setString(6, conclusionProceedings.getNarrationForFreeCharge());
                    pstmt.setInt(7, conclusionProceedings.getConcprocid());
                    pstmt.setString(8, conclusionProceedings.getEmpid());

                    pstmt.executeUpdate();

                }
                if (conclusionProceedings.getResnotid() == 0) {
                    nb = new NotificationBean();
                    nb.setNottype("RESULT");
                    nb.setEmpId(conclusionProceedings.getEmpid());
                    nb.setDateofEntry(new Date());
                    nb.setOrdno(conclusionProceedings.getConclusionOrdNo());
                    nb.setOrdDate(sdf.parse(conclusionProceedings.getConclusionOrdDt()));
                    nb.setSancDeptCode(conclusionProceedings.getConclusionnotdept());
                    nb.setSancOffCode(conclusionProceedings.getConclusionnotoffice());
                    nb.setSancAuthCode(conclusionProceedings.getConclusionnotspc());
                    nb.setEntryDeptCode(conclusionProceedings.getConclusionnotdept());
                    nb.setEntryOffCode(conclusionProceedings.getConclusionnotoffice());
                    nb.setEntryAuthCode(conclusionProceedings.getConclusionnotspc());
                    nb.setNote("");
                    int notid = notificationDao.insertNotificationData(nb);
                    pstmt = con.prepareStatement("UPDATE EMP_PROCEEDING SET censor_type=?,RES_NOT_ID=?,punishment_rewarded=? WHERE PR_ID=? AND EMP_ID=?");
                    pstmt.setString(1, conclusionProceedings.getCensureType());
                    pstmt.setInt(2, notid);
                    // pstmt.setString(3, originalFileName);
                    //pstmt.setString(4, diskfileName);
                    //pstmt.setString(5, contentType);
                    //pstmt.setString(6, this.uploadPath);
                    pstmt.setString(3, conclusionProceedings.getPunishmentRewarded());
                    pstmt.setInt(4, conclusionProceedings.getConcprocid());
                    pstmt.setString(5, conclusionProceedings.getEmpid());
                    pstmt.executeUpdate();
                    
                    String sbLang = sbDAO.getFinalResultProceedingsDetails(conclusionProceedings,conclusionProceedings.getPunishmentRewarded());
                    pstmt = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=? WHERE NOT_ID=? ");
                    pstmt.setString(1, sbLang);
                    pstmt.setInt(2, notid);
                    pstmt.execute();
                } else {
                    nb = new NotificationBean();
                    nb.setNotid(conclusionProceedings.getResnotid());
                    nb.setDateofEntry(new Date());
                    nb.setOrdno(conclusionProceedings.getConclusionOrdNo());
                    nb.setOrdDate(sdf.parse(conclusionProceedings.getConclusionOrdDt()));
                    nb.setSancDeptCode(conclusionProceedings.getConclusionnotdept());
                    nb.setSancOffCode(conclusionProceedings.getConclusionnotoffice());
                    nb.setSancAuthCode(conclusionProceedings.getConclusionnotspc());
                    nb.setEntryDeptCode(conclusionProceedings.getConclusionnotdept());
                    nb.setEntryOffCode(conclusionProceedings.getConclusionnotoffice());
                    nb.setEntryAuthCode(conclusionProceedings.getConclusionnotspc());
                    nb.setNote("");
                    notificationDao.modifyNotificationData(nb);
                    pstmt = con.prepareStatement("UPDATE EMP_PROCEEDING SET censor_type=?,punishment_rewarded=?,punishment_type=?,narrationfor_freefromcharge=? WHERE PR_ID=? AND EMP_ID=?");
                    pstmt.setString(1, conclusionProceedings.getCensureType());
                    //pstmt.setString(2, originalFileName);
                    //pstmt.setString(3, diskfileName);
                    //pstmt.setString(4, contentType);
                    //pstmt.setString(5, this.uploadPath);
                    pstmt.setString(2, conclusionProceedings.getPunishmentRewarded());
                    pstmt.setString(3, conclusionProceedings.getPunishmentType());
                    pstmt.setString(4, conclusionProceedings.getNarrationForFreeCharge());
                    pstmt.setInt(5, conclusionProceedings.getConcprocid());
                    pstmt.setString(6, conclusionProceedings.getEmpid());

                    pstmt.executeUpdate();
                    
                    
                    String sbLang = sbDAO.getFinalResultProceedingsDetails(conclusionProceedings,conclusionProceedings.getPunishmentRewarded());
                    pstmt = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=? WHERE NOT_ID=? ");
                    pstmt.setString(1, sbLang);
                    pstmt.setInt(2, conclusionProceedings.getResnotid());
                    pstmt.execute();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteAttachmentDetail(ConclusionProceedings conclusionProceedings) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE EMP_PROCEEDING SET scn_original_filename=null,scn_disk_filename=null,scn_file_type=null,scn_file_path=null WHERE PR_ID=? AND EMP_ID=?");
            pst.setInt(1, conclusionProceedings.getConcprocid());
            pst.setString(2, conclusionProceedings.getEmpid());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getEmpPunishDetailsData(int concprocid, String pdtype) {
        ArrayList punishdetailsList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select pd_id,pr_id,EMP_PRDETAILS.PENALTY_TP,penalty from EMP_PRDETAILS "
                    + "INNER JOIN G_PENALTY ON EMP_PRDETAILS.PENALTY_TP = G_PENALTY.PENALTY_TP where EMP_PRDETAILS.pr_id= ? and pd_type=? order by penalty_tp");
            pstmt.setInt(1, concprocid);
            pstmt.setString(2, pdtype);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                PunishmentDetails pndt = new PunishmentDetails();
                pndt.setConcprocid(rs.getInt("pr_id"));
                pndt.setPunishmentdetailsid(rs.getInt("pd_id"));
                pndt.setPentype(rs.getString("PENALTY_TP"));
                pndt.setPunishmenttypedesc(rs.getString("penalty"));
                PunishmentDetails tempPndt = getPunishmentTypeDetails(pndt.getPunishmentdetailsid(), pndt.getPentype());
                pndt.setWefdate(tempPndt.getWefdate());
                pndt.setTilldate(tempPndt.getTilldate());
                pndt.setNarration(tempPndt.getNarration());
                punishdetailsList.add(pndt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return punishdetailsList;
    }

    @Override
    public boolean deletePunishmentTypeDetails(int punishmentdetailsid, String penltdesc) {
        boolean isDeleted = true;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            if (penltdesc != null && penltdesc.equalsIgnoreCase("01")) {
                pstmt = con.prepareStatement("DELETE FROM EMP_PEN_FINE WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                pstmt.executeUpdate();
                pstmt = con.prepareStatement("DELETE FROM EMP_PRDETAILS WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                pstmt.executeUpdate();
            } else if (penltdesc != null && penltdesc.equalsIgnoreCase("02")) {
                pstmt = con.prepareStatement("DELETE FROM EMP_PEN_WINC WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                pstmt.executeUpdate();
                pstmt = con.prepareStatement("DELETE FROM EMP_PRDETAILS WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                pstmt.executeUpdate();
            } else if (penltdesc != null && penltdesc.equalsIgnoreCase("03")) {
                pstmt = con.prepareStatement("DELETE FROM EMP_PEN_WPRO WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                pstmt.executeUpdate();
                pstmt = con.prepareStatement("DELETE FROM EMP_PRDETAILS WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                pstmt.executeUpdate();
            } else if (penltdesc != null && penltdesc.equalsIgnoreCase("04")) {
                pstmt = con.prepareStatement("DELETE FROM EMP_PEN_SUSP WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                pstmt.executeUpdate();
                pstmt = con.prepareStatement("DELETE FROM EMP_PRDETAILS WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                pstmt.executeUpdate();
            } else if (penltdesc != null && penltdesc.equalsIgnoreCase("05")) {
                pstmt = con.prepareStatement("DELETE FROM EMP_PEN_REDCT WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                pstmt.executeUpdate();
                pstmt = con.prepareStatement("DELETE FROM EMP_PRDETAILS WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                pstmt.executeUpdate();
            } else if (penltdesc != null && penltdesc.equalsIgnoreCase("06")) {
                pstmt = con.prepareStatement("DELETE FROM EMP_PEN_CRETIRE WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                pstmt.executeUpdate();
                pstmt = con.prepareStatement("DELETE FROM EMP_PRDETAILS WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                pstmt.executeUpdate();
            } else if (penltdesc != null && penltdesc.equalsIgnoreCase("07")) {
                pstmt = con.prepareStatement("DELETE FROM EMP_PEN_REMOVE WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                pstmt.executeUpdate();
                pstmt = con.prepareStatement("DELETE FROM EMP_PRDETAILS WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                pstmt.executeUpdate();
            } else if (penltdesc != null && penltdesc.equalsIgnoreCase("08")) {
                pstmt = con.prepareStatement("DELETE FROM EMP_PEN_DISMISS WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                pstmt.executeUpdate();
                pstmt = con.prepareStatement("DELETE FROM EMP_PRDETAILS WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            isDeleted = false;
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt, con);
        }
        return isDeleted;
    }

    @Override
    public PunishmentDetails getPunishmentTypeDetails(int punishmentdetailsid, String penltdesc) {
        PunishmentDetails pd = new PunishmentDetails();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            if (penltdesc != null && penltdesc.equalsIgnoreCase("01")) {
                pstmt = con.prepareStatement("select *  FROM EMP_PEN_FINE WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    if (rs.getDate("FDATE") != null) {
                        pd.setWefdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("FDATE")));
                    }
                    pd.setFineamount(rs.getInt("FINEAMT"));
                    pd.setRecvtype(rs.getString("RECOVERYTYPE"));
                    pd.setNoinstll(rs.getInt("INSTALLMENTCNT"));
                    pd.setTilldate("");
                    pd.setNarration("");
                }

            } else if (penltdesc != null && penltdesc.equalsIgnoreCase("02")) {
                pstmt = con.prepareStatement("select *  FROM EMP_PEN_WINC WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    if (rs.getDate("FDATE") != null) {
                        pd.setWefdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("FDATE")));
                    }
                    pd.setTilldate("");
                    pd.setNarration("");
                    pd.setScalepay(rs.getString("pay_scale"));
                    pd.setNoofinc(rs.getInt("incno"));
                    pd.setWithincum(rs.getString("cumeff"));
                }

            } else if (penltdesc != null && penltdesc.equalsIgnoreCase("03")) {
                pstmt = con.prepareStatement("select *  FROM EMP_PEN_WPRO WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    pd.setWefdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("FDATE")));
                    pd.setTilldate(CommonFunctions.getFormattedOutputDate1(rs.getDate("TDATE")));
                    pd.setNarration("");
                    pd.setNoofpromotion(rs.getInt("pencnt"));
                    pd.setWithpromcum(rs.getString("cumeff"));
                }

            } else if (penltdesc != null && penltdesc.equalsIgnoreCase("04")) {
                pstmt = con.prepareStatement("select *  FROM EMP_PEN_SUSP WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    pd.setWefdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("FDATE")));
                    pd.setTilldate(CommonFunctions.getFormattedOutputDate1(rs.getDate("TDATE")));
                    pd.setTreatasduty(rs.getString("TREATASDUTY"));
                    pd.setTolId("TOL_ID");
                    pd.setNarration("");
                }

            } else if (penltdesc != null && penltdesc.equalsIgnoreCase("05")) {
                pstmt = con.prepareStatement("select *  FROM EMP_PEN_REDCT WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    pd.setWefdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("FDATE")));
                    pd.setTilldate(CommonFunctions.getFormattedOutputDate1(rs.getDate("TDATE")));
                    pd.setDepCode(rs.getString("DEPT_CODE"));
                    pd.setOffCode(rs.getString("OFF_CODE"));
                    pd.setCadreCode(rs.getString("CADRE_CODE"));
                    pd.setGrade(rs.getString("GRADE"));
                    pd.setPostCode(rs.getString("POST_CODE"));
                    pd.setPayScale(rs.getString("PAY_SCALE"));
                    pd.setEarnInc(rs.getString("EARN_INC"));
                    pd.setNarration("");
                }
            } else if (penltdesc != null && penltdesc.equalsIgnoreCase("06")) {
                pstmt = con.prepareStatement("select *  FROM EMP_PEN_CRETIRE WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    pd.setWefdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOR")));
                    pd.setTilldate("");
                    pd.setNarration("");
                }

            } else if (penltdesc != null && penltdesc.equalsIgnoreCase("07")) {
                pstmt = con.prepareStatement("select *  FROM EMP_PEN_REMOVE WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    pd.setWefdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOR")));
                    pd.setTilldate("");
                    pd.setNarration("");
                    pd.setRemfutemp(rs.getString("DIS_QUAL"));
                }
            } else if (penltdesc != null && penltdesc.equalsIgnoreCase("08")) {
                pstmt = con.prepareStatement("select *  FROM EMP_PEN_DISMISS WHERE PD_ID=?");
                pstmt.setInt(1, punishmentdetailsid);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    pd.setWefdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOD")));
                    pd.setTilldate("");
                    pd.setNarration("");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return pd;
    }

    public int saveEmpPrDetails(int prid, String pentype, String pdtype) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int pdid = 0;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("insert into emp_prdetails (PR_ID, PENALTY_TP,PD_TYPE)values(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, prid);
            pst.setString(2, pentype);
            pst.setString(3, pdtype);
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            rs.next();
            pdid = rs.getInt("pd_id");
        } catch (SQLException esql) {
            esql.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
        return pdid;
    }

    @Override
    public PunishmentDetails savePunishmentFineRecovery(PunishmentDetails pnd) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select count(*) as cnt from emp_pen_fine where pd_id=?");
            pstmt.setInt(1, pnd.getPunishmentdetailsid());
            rs = pstmt.executeQuery();
            int cnt = 0;
            if (rs.next()) {
                cnt = rs.getInt("cnt");
            }
            pnd.setMsg("Y");
            if (pnd.getPunishmentdetailsid() == 0) {
                int pdid = saveEmpPrDetails(pnd.getConcprocid(), "01", pnd.getPdtype());
                pstmt = con.prepareStatement("INSERT into  emp_pen_fine(PD_ID,FDATE, FINEAMT, RECOVERYTYPE, INSTALLMENTCNT) values(?,?,?,?,?)");
                pstmt.setInt(1, pdid);
                pstmt.setTimestamp(2, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                pstmt.setInt(3, pnd.getFineamount());
                pstmt.setString(4, pnd.getRecvtype());
                pstmt.setInt(5, pnd.getNoinstll());
                pstmt.executeUpdate();
            } else {
                if (cnt == 0) {
                    pstmt = con.prepareStatement("INSERT into  emp_pen_fine(PD_ID,FDATE, FINEAMT, RECOVERYTYPE, INSTALLMENTCNT,PD_TYPE) values(?,?,?,?,?)");
                    pstmt.setInt(1, pnd.getPunishmentdetailsid());
                    pstmt.setTimestamp(2, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                    pstmt.setInt(3, pnd.getFineamount());
                    pstmt.setString(4, pnd.getRecvtype());
                    pstmt.setInt(5, pnd.getNoinstll());
                    pstmt.setString(6, pnd.getPdtype());
                    pstmt.executeUpdate();
                } else {
                    pstmt = con.prepareStatement("UPDATE  emp_pen_fine SET FDATE=?, FINEAMT=?, RECOVERYTYPE=?, INSTALLMENTCNT=? WHERE PD_ID=?");
                    pstmt.setTimestamp(1, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                    pstmt.setInt(2, pnd.getFineamount());
                    pstmt.setString(3, pnd.getRecvtype());
                    pstmt.setInt(4, pnd.getNoinstll());
                    pstmt.setInt(5, pnd.getPunishmentdetailsid());
                    pstmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            pnd.setMsg("N");
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return pnd;
    }

    @Override
    public PunishmentDetails savePunishmentWithholdIncrement(PunishmentDetails pnd) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            con = dataSource.getConnection();
            pnd.setMsg("Y");
            if (pnd.getPunishmentdetailsid() == 0) {
                int pdid = saveEmpPrDetails(pnd.getConcprocid(), "02", pnd.getPdtype());
                pstmt = con.prepareStatement("INSERT into  EMP_PEN_WINC(PD_ID,FDATE, pay_scale, incno, cumeff) values(?,?,?,?,?)");
                pstmt.setInt(1, pdid);
                pstmt.setTimestamp(2, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                pstmt.setString(3, pnd.getScalepay());
                pstmt.setInt(4, pnd.getNoofinc());
                pstmt.setString(5, pnd.getWithincum());
                pstmt.executeUpdate();
            } else {
                pstmt = con.prepareStatement("select count(*) as cnt from EMP_PEN_WINC where pd_id=?");
                pstmt.setInt(1, pnd.getPunishmentdetailsid());
                rs = pstmt.executeQuery();
                int cnt = 0;
                if (rs.next()) {
                    cnt = rs.getInt("cnt");
                }
                if (cnt == 0) {
                    pstmt = con.prepareStatement("INSERT into  EMP_PEN_WINC(PD_ID,FDATE, pay_scale, incno, cumeff) values(?,?,?,?,?)");
                    pstmt.setInt(1, pnd.getPunishmentdetailsid());
                    pstmt.setTimestamp(2, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                    pstmt.setString(3, pnd.getScalepay());
                    pstmt.setInt(4, pnd.getNoofinc());
                    pstmt.setString(5, pnd.getWithincum());
                    pstmt.executeUpdate();
                } else {
                    pstmt = con.prepareStatement("UPDATE  EMP_PEN_WINC SET FDATE=?, pay_scale=?, incno=?, cumeff=? WHERE PD_ID=?");
                    pstmt.setTimestamp(1, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                    pstmt.setString(2, pnd.getScalepay());
                    pstmt.setInt(3, pnd.getNoofinc());
                    pstmt.setString(4, pnd.getWithincum());
                    pstmt.setInt(5, pnd.getPunishmentdetailsid());
                    pstmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            pnd.setMsg("N");
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return pnd;
    }

    @Override
    public PunishmentDetails savePunishmentWithholdPromotion(PunishmentDetails pnd) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            con = dataSource.getConnection();
            pnd.setMsg("Y");
            if (pnd.getPunishmentdetailsid() == 0) {
                int pdid = saveEmpPrDetails(pnd.getConcprocid(), "03", pnd.getPdtype());
                pstmt = con.prepareStatement("INSERT into  EMP_PEN_WPRO(PD_ID,FDATE, TDATE, PENCNT, cumeff) values(?,?,?,?,?)");
                pstmt.setInt(1, pdid);
                pstmt.setTimestamp(2, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                pstmt.setTimestamp(3, new Timestamp(sdf.parse(pnd.getTilldate()).getTime()));
                pstmt.setInt(4, pnd.getNoofpromotion());
                pstmt.setString(5, pnd.getWithpromcum());
                pstmt.executeUpdate();
            } else {
                pstmt = con.prepareStatement("select count(*) as cnt from EMP_PEN_WPRO where pd_id=?");
                pstmt.setInt(1, pnd.getPunishmentdetailsid());
                rs = pstmt.executeQuery();
                int cnt = 0;
                if (rs.next()) {
                    cnt = rs.getInt("cnt");
                }
                if (cnt == 0) {
                    pstmt = con.prepareStatement("INSERT INTO  EMP_PEN_WPRO(PD_ID,FDATE, TDATE, PENCNT, cumeff) values(?,?,?,?,?)");
                    pstmt.setInt(1, pnd.getPunishmentdetailsid());
                    pstmt.setTimestamp(2, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                    pstmt.setTimestamp(3, new Timestamp(sdf.parse(pnd.getTilldate()).getTime()));
                    pstmt.setInt(4, pnd.getNoofpromotion());
                    pstmt.setString(5, pnd.getWithpromcum());
                    pstmt.executeUpdate();
                } else {
                    pstmt = con.prepareStatement("UPDATE  EMP_PEN_WPRO SET FDATE=?, TDATE=?, PENCNT=?, cumeff=? WHERE PD_ID=?");
                    pstmt.setTimestamp(1, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                    pstmt.setTimestamp(2, new Timestamp(sdf.parse(pnd.getTilldate()).getTime()));
                    pstmt.setInt(3, pnd.getNoofpromotion());
                    pstmt.setString(4, pnd.getWithpromcum());
                    pstmt.setInt(5, pnd.getPunishmentdetailsid());
                    pstmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            pnd.setMsg("N");
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return pnd;
    }
    /*Save the Punishment for suspension*/

    @Override
    public PunishmentDetails savePunishmentSuspension(PunishmentDetails pnd) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            con = dataSource.getConnection();
            pnd.setMsg("Y");
            if (pnd.getPunishmentdetailsid() == 0) {
                int pdid = saveEmpPrDetails(pnd.getConcprocid(), "04", pnd.getPdtype());
                pstmt = con.prepareStatement("INSERT into EMP_PEN_SUSP(PD_ID, SP_ID, FDATE, TDATE, TREATASDUTY,TOL_ID) values(?,?,?,?,?,?)");
                pstmt.setInt(1, pdid);
                pstmt.setString(2, pnd.getSpId());
                pstmt.setTimestamp(3, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                pstmt.setTimestamp(4, new Timestamp(sdf.parse(pnd.getTilldate()).getTime()));
                pstmt.setString(5, pnd.getTreatasduty());
                pstmt.setString(6, pnd.getTolId());
                pstmt.executeUpdate();
            } else {
                pstmt = con.prepareStatement("select count(*) as cnt from EMP_PEN_SUSP where pd_id=?");
                pstmt.setInt(1, pnd.getPunishmentdetailsid());
                rs = pstmt.executeQuery();
                int cnt = 0;
                if (rs.next()) {
                    cnt = rs.getInt("cnt");
                }
                if (cnt == 0) {
                    pstmt = con.prepareStatement("INSERT INTO  EMP_PEN_SUSP(PD_ID, SP_ID, FDATE, TDATE, TREATASDUTY,TOL_ID) values(?,?,?,?,?,?)");
                    pstmt.setInt(1, pnd.getPunishmentdetailsid());
                    pstmt.setString(2, pnd.getSpId());
                    pstmt.setTimestamp(3, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                    pstmt.setTimestamp(4, new Timestamp(sdf.parse(pnd.getTilldate()).getTime()));
                    pstmt.setString(5, pnd.getTreatasduty());
                    pstmt.setString(6, pnd.getTolId());
                    pstmt.executeUpdate();
                } else {
                    pstmt = con.prepareStatement("update emp_pen_susp set SP_ID=?, FDATE=?, TDATE=?, TREATASDUTY=?,TOL_ID=? where PD_ID=?");
                    pstmt.setString(1, pnd.getSpId());
                    pstmt.setTimestamp(2, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                    pstmt.setTimestamp(3, new Timestamp(sdf.parse(pnd.getTilldate()).getTime()));
                    pstmt.setString(4, pnd.getTreatasduty());
                    pstmt.setString(5, pnd.getTolId());
                    pstmt.setInt(6, pnd.getPunishmentdetailsid());
                    pstmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            pnd.setMsg("N");
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return pnd;
    }

    @Override
    public PunishmentDetails savePunishmentReduction(PunishmentDetails pnd) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            con = dataSource.getConnection();
            pnd.setMsg("Y");
            if (pnd.getPunishmentdetailsid() == 0) {
                int pdid = saveEmpPrDetails(pnd.getConcprocid(), "05", pnd.getPdtype());
                pstmt = con.prepareStatement("insert into emp_pen_redct( PD_ID, FDATE, TDATE, DEPT_CODE, CADRE_CODE, GRADE, POST_CODE, PAY_SCALE, EARN_INC, OFF_CODE) values(?,?,?,?,?,?,?,?,?,?)");
                pstmt.setInt(1, pdid);
                pstmt.setTimestamp(2, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                pstmt.setTimestamp(3, new Timestamp(sdf.parse(pnd.getTilldate()).getTime()));
                pstmt.setString(4, pnd.getDepCode());
                pstmt.setString(5, pnd.getCadreCode());
                pstmt.setString(6, pnd.getGrade());
                pstmt.setString(7, pnd.getPostCode());
                pstmt.setString(8, pnd.getPayScale());
                pstmt.setString(9, pnd.getEarnInc());
                pstmt.setString(10, pnd.getOffCode());
                pstmt.executeUpdate();
            } else {
                pstmt = con.prepareStatement("select count(*) as cnt from emp_pen_redct where pd_id=?");
                pstmt.setInt(1, pnd.getPunishmentdetailsid());
                rs = pstmt.executeQuery();
                int cnt = 0;
                if (rs.next()) {
                    cnt = rs.getInt("cnt");
                }
                if (cnt == 0) {
                    pstmt = con.prepareStatement("insert into emp_pen_redct( PD_ID, FDATE, TDATE, DEPT_CODE, CADRE_CODE, GRADE, POST_CODE, PAY_SCALE, EARN_INC, OFF_CODE) values(?,?,?,?,?,?,?,?,?,?)");
                    pstmt.setInt(1, pnd.getPunishmentdetailsid());
                    pstmt.setTimestamp(2, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                    pstmt.setTimestamp(3, new Timestamp(sdf.parse(pnd.getTilldate()).getTime()));
                    pstmt.setString(4, pnd.getDepCode());
                    pstmt.setString(5, pnd.getCadreCode());
                    pstmt.setString(6, pnd.getGrade());
                    pstmt.setString(7, pnd.getPostCode());
                    pstmt.setString(8, pnd.getPayScale());
                    pstmt.setString(9, pnd.getEarnInc());
                    pstmt.setString(10, pnd.getOffCode());
                    pstmt.executeUpdate();
                } else {
                    pstmt = con.prepareStatement("update emp_pen_redct set FDATE=?, TDATE=?, DEPT_CODE=?, CADRE_CODE=?, GRADE=?, POST_CODE=?, PAY_SCALE=?, EARN_INC=?, OFF_CODE=? where PD_ID=?");
                    pstmt.setTimestamp(1, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                    pstmt.setTimestamp(2, new Timestamp(sdf.parse(pnd.getTilldate()).getTime()));
                    pstmt.setString(3, pnd.getDepCode());
                    pstmt.setString(4, pnd.getCadreCode());
                    pstmt.setString(5, pnd.getGrade());
                    pstmt.setString(6, pnd.getPostCode());
                    pstmt.setString(7, pnd.getPayScale());
                    pstmt.setString(8, pnd.getEarnInc());
                    pstmt.setString(9, pnd.getOffCode());
                    pstmt.setInt(10, pnd.getPunishmentdetailsid());
                    pstmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            pnd.setMsg("N");
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return pnd;
    }

    @Override
    public PunishmentDetails savePunishmentRetirement(PunishmentDetails pnd) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            con = dataSource.getConnection();
            pnd.setMsg("Y");
            if (pnd.getPunishmentdetailsid() == 0) {
                int pdid = saveEmpPrDetails(pnd.getConcprocid(), "06", pnd.getPdtype());
                pstmt = con.prepareStatement("INSERT into  EMP_PEN_CRETIRE(PD_ID,DOR) values(?,?)");
                pstmt.setInt(1, pdid);
                pstmt.setTimestamp(2, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                pstmt.executeUpdate();
            } else {
                pstmt = con.prepareStatement("select count(*) as cnt from EMP_PEN_CRETIRE where pd_id=?");
                pstmt.setInt(1, pnd.getPunishmentdetailsid());
                rs = pstmt.executeQuery();
                int cnt = 0;
                if (rs.next()) {
                    cnt = rs.getInt("cnt");
                }
                if (cnt == 0) {
                    pstmt = con.prepareStatement("INSERT INTO  EMP_PEN_CRETIRE(PD_ID,DOR) values(?,?)");
                    pstmt.setInt(1, pnd.getPunishmentdetailsid());
                    pstmt.setTimestamp(2, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                    pstmt.executeUpdate();
                } else {
                    pstmt = con.prepareStatement("UPDATE  EMP_PEN_CRETIRE SET DOR=? WHERE PD_ID=?");
                    pstmt.setTimestamp(1, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                    pstmt.setInt(2, pnd.getPunishmentdetailsid());
                    pstmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            pnd.setMsg("N");
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return pnd;
    }

    @Override
    public PunishmentDetails savePunishmentRemovalService(PunishmentDetails pnd) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            con = dataSource.getConnection();
            pnd.setMsg("Y");
            if (pnd.getPunishmentdetailsid() == 0) {
                int pdid = saveEmpPrDetails(pnd.getConcprocid(), "07", pnd.getPdtype());
                pstmt = con.prepareStatement("INSERT INTO EMP_PEN_REMOVE(PD_ID,DOR,DIS_QUAL) values(?,?,?)");
                pstmt.setInt(1, pdid);
                pstmt.setTimestamp(2, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                pstmt.setString(3, pnd.getRemfutemp());
                pstmt.executeUpdate();
            } else {
                pstmt = con.prepareStatement("select count(*) as cnt from EMP_PEN_REMOVE where pd_id=?");
                pstmt.setInt(1, pnd.getPunishmentdetailsid());
                rs = pstmt.executeQuery();
                int cnt = 0;
                if (rs.next()) {
                    cnt = rs.getInt("cnt");
                }
                if (cnt == 0) {
                    pstmt = con.prepareStatement("INSERT INTO EMP_PEN_REMOVE(PD_ID,DOR,DIS_QUAL) values(?,?,?)");
                    pstmt.setInt(1, pnd.getPunishmentdetailsid());
                    pstmt.setTimestamp(2, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                    pstmt.setString(3, pnd.getRemfutemp());
                    pstmt.executeUpdate();
                } else {
                    pstmt = con.prepareStatement("UPDATE EMP_PEN_REMOVE SET DOR=?, DIS_QUAL=? WHERE PD_ID=?");
                    pstmt.setTimestamp(1, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                    pstmt.setString(2, pnd.getRemfutemp());
                    pstmt.setInt(3, pnd.getPunishmentdetailsid());
                    pstmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            pnd.setMsg("N");
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return pnd;
    }

    @Override
    public PunishmentDetails savePunishmentDismissalService(PunishmentDetails pnd) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            con = dataSource.getConnection();
            pnd.setMsg("Y");
            if (pnd.getPunishmentdetailsid() == 0) {
                int pdid = saveEmpPrDetails(pnd.getConcprocid(), "08", pnd.getPdtype());
                pstmt = con.prepareStatement("INSERT INTO EMP_PEN_DISMISS(PD_ID,DOD) values(?,?)");
                pstmt.setInt(1, pdid);
                pstmt.setTimestamp(2, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                pstmt.executeUpdate();
            } else {
                pstmt = con.prepareStatement("select COUNT(*) as cnt from EMP_PEN_DISMISS where pd_id=?");
                pstmt.setInt(1, pnd.getPunishmentdetailsid());
                rs = pstmt.executeQuery();
                int cnt = 0;
                if (rs.next()) {
                    cnt = rs.getInt("cnt");
                }
                if (cnt == 0) {
                    pstmt = con.prepareStatement("INSERT INTO EMP_PEN_DISMISS(PD_ID,DOD) values(?,?)");
                    pstmt.setInt(1, pnd.getPunishmentdetailsid());
                    pstmt.setTimestamp(2, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                    pstmt.executeUpdate();
                } else {
                    pstmt = con.prepareStatement("UPDATE EMP_PEN_DISMISS SET DOD=? WHERE PD_ID=?");
                    pstmt.setTimestamp(1, new Timestamp(sdf.parse(pnd.getWefdate()).getTime()));
                    pstmt.setInt(2, pnd.getPunishmentdetailsid());
                    pstmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            pnd.setMsg("N");
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return pnd;
    }
}
