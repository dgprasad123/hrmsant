/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.miscellaneous;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.empinfo.EmployeeGrievance;
import hrms.model.empinfo.GrievanceDashBoard;
import hrms.model.empinfo.GrievnceCommunication;
import hrms.model.empinfo.SMSGrievance;
import hrms.model.employee.EmployeeBasicProfile;
import hrms.model.master.GrievanceCategory;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manas
 */
public class EmployeeGrievanceDAOImpl implements EmployeeGrievanceDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    protected String uploadPath;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    @Override
    public List getGrievnceCategoryList() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        List categorylist = new ArrayList();
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT category_code, g_category, status FROM g_grievance_category");
            while (rs.next()) {
                GrievanceCategory gCategory = new GrievanceCategory();
                gCategory.setCategoryCode(rs.getString("category_code"));
                gCategory.setCategory(rs.getString("g_category"));
                gCategory.setStatus(rs.getString("status"));
                categorylist.add(gCategory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt, con);
        }
        return categorylist;
    }

    @Override
    public List getEmployeeGrievnceList(String empId) {
        List grievncelist = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT g_id, g_grievance_category.category_code, g_category, grievance, mobile, is_disposed, grievance_time, is_forwarded,IS_REJECTED FROM  emp_grievance_details "
                    + "INNER JOIN g_grievance_category ON emp_grievance_details.category_code = g_grievance_category.category_code WHERE hrmsid = ? ORDER BY g_id DESC");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                EmployeeGrievance empGrievance = new EmployeeGrievance();
                empGrievance.setGid(rs.getInt("g_id"));
                empGrievance.setCategory(rs.getString("g_category"));
                empGrievance.setGrievanceDetail(rs.getString("grievance"));
                empGrievance.setAppmobile(rs.getString("mobile"));
                empGrievance.setIsdisposed(rs.getString("is_disposed"));
                empGrievance.setGrievanceTime(CommonFunctions.getFormattedOutputDate1(rs.getDate("grievance_time")));
                empGrievance.setIsforwarded(rs.getString("is_forwarded"));
                empGrievance.setIsrejected(rs.getString("IS_REJECTED"));
                grievncelist.add(empGrievance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return grievncelist;
    }

    @Override
    public int saveEmployeeGrievnceByAdmin(EmployeeGrievance employeeGrievance) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        int gid = 0;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT CUR_SPC, MOBILE FROM EMP_MAST WHERE EMP_ID=?");
            pstmt.setString(1, employeeGrievance.getHrmsid());
            res = pstmt.executeQuery();
            if (res.next()) {
                employeeGrievance.setAppmobile(res.getString("MOBILE"));
                employeeGrievance.setSpc(res.getString("CUR_SPC"));
            }
            pstmt = con.prepareStatement("INSERT INTO emp_grievance_details (hrmsid,mobile,category_code,office_code,grievance,grievance_time,source) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, employeeGrievance.getHrmsid());
            pstmt.setString(2, employeeGrievance.getAppmobile());
            pstmt.setString(3, employeeGrievance.getCategoryCode());
            pstmt.setString(4, employeeGrievance.getAppoffcode());
            pstmt.setString(5, employeeGrievance.getGrievanceDetail());
            pstmt.setDate(6, new java.sql.Date(new java.util.Date().getTime()));
            pstmt.setString(7, employeeGrievance.getSource());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                gid = rs.getInt(1);
            }

            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            String yearInString = String.valueOf(year);
            String ack_no = "OSAP-" + gid + "/" + yearInString;

            pstmt = con.prepareStatement("UPDATE emp_grievance_details SET current_year=?, ackno=? where g_id = ?");
            pstmt.setString(1, yearInString);
            pstmt.setString(2, ack_no);
            pstmt.setInt(3, gid);
            pstmt.executeUpdate();

            String authCode = employeeGrievance.getAuthCode();
            String[] decodeVal = StringUtils.split(authCode, "-");
            pstmt = con.prepareStatement("INSERT INTO grievance_communications (grievance_id, from_hrms_id, from_spc, to_hrms_id, to_spc, communication_time, remarks, is_pending) VALUES (?,?,?,?,?,?,?,?)");
            pstmt.setInt(1, gid);
            pstmt.setString(2, employeeGrievance.getHrmsid());
            pstmt.setString(3, employeeGrievance.getSpc());
            pstmt.setString(4, decodeVal[0]);
            pstmt.setString(5, decodeVal[1]);
            pstmt.setDate(6, new java.sql.Date(new Date().getTime()));
            pstmt.setString(7, employeeGrievance.getGrievanceDetail());
            pstmt.setInt(8, 1);//1 for pending 0 for not pending
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt, con);
        }
        return gid;
    }

    @Override
    public void updateEmployeeGrievnce(EmployeeGrievance employeeGrievance) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE emp_grievance_details SET grievance = ?,grievance_time = ? WHERE g_id = ?");
            pstmt.setString(1, employeeGrievance.getGrievanceDetail());
            pstmt.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
            pstmt.setInt(3, employeeGrievance.getGid());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt, con);
        }
    }

    @Override
    public int saveEmployeeGrievnce(EmployeeGrievance employeeGrievance) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int gid = 0;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("INSERT INTO emp_grievance_details (hrmsid,mobile,category_code,office_code,grievance,grievance_time,source) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, employeeGrievance.getHrmsid());
            pstmt.setString(2, employeeGrievance.getAppmobile());
            pstmt.setString(3, employeeGrievance.getCategoryCode());
            pstmt.setString(4, employeeGrievance.getAppoffcode());
            pstmt.setString(5, employeeGrievance.getGrievanceDetail());
            pstmt.setDate(6, new java.sql.Date(new java.util.Date().getTime()));
            pstmt.setString(7, employeeGrievance.getSource());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                gid = rs.getInt(1);
            }

            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            String yearInString = String.valueOf(year);
            String ack_no = "OSAP-" + gid + "/" + yearInString;

            pstmt = con.prepareStatement("UPDATE emp_grievance_details SET current_year=?, ackno=? where g_id = ?");
            pstmt.setString(1, yearInString);
            pstmt.setString(2, ack_no);
            pstmt.setInt(3, gid);
            pstmt.executeUpdate();

            String authCode = employeeGrievance.getAuthCode();
            String[] decodeVal = StringUtils.split(authCode, "-");
            pstmt = con.prepareStatement("INSERT INTO grievance_communications (grievance_id, from_hrms_id, from_spc, to_hrms_id, to_spc, communication_time, remarks, is_pending) VALUES (?,?,?,?,?,?,?,?)");
            pstmt.setInt(1, gid);
            pstmt.setString(2, employeeGrievance.getHrmsid());
            pstmt.setString(3, employeeGrievance.getSpc());
            pstmt.setString(4, decodeVal[0]);
            pstmt.setString(5, decodeVal[1]);
            pstmt.setDate(6, new java.sql.Date(new Date().getTime()));
            pstmt.setString(7, employeeGrievance.getGrievanceDetail());
            pstmt.setInt(8, 1);//1 for pending 0 for not pending
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt, con);
        }
        return gid;
    }

    @Override
    public List getAdminGrievnceList(String spc) {
        List grievncelist = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT grievance_id, g_grievance_category.category_code, g_category, grievance, emp_grievance_details.mobile, IS_DISPOSED, IS_REJECTED, grievance_time, "
                    + "IS_FORWARDED, emp_grievance_details.hrmsid, F_NAME, M_NAME, L_NAME  FROM grievance_communications "
                    + "INNER JOIN emp_grievance_details on emp_grievance_details.g_id = grievance_communications.grievance_id "
                    + "INNER JOIN g_grievance_category ON emp_grievance_details.category_code = g_grievance_category.category_code "
                    + "INNER JOIN EMP_MAST ON emp_grievance_details.hrmsid = EMP_MAST.EMP_ID WHERE to_spc = ?");
            pstmt.setString(1, spc);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                EmployeeGrievance empGrievance = new EmployeeGrievance();
                empGrievance.setGid(rs.getInt("grievance_id"));
                empGrievance.setCategory(rs.getString("g_category"));
                empGrievance.setGrievanceDetail(rs.getString("grievance"));
                empGrievance.setAppmobile(rs.getString("mobile"));
                empGrievance.setHrmsid(rs.getString("hrmsid"));
                empGrievance.setFullname(rs.getString("F_NAME") + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + rs.getString("L_NAME"));
                empGrievance.setIsdisposed(rs.getString("IS_DISPOSED"));
                empGrievance.setIsrejected(rs.getString("IS_REJECTED"));
                empGrievance.setGrievanceTime(CommonFunctions.getFormattedOutputDate1(rs.getDate("grievance_time")));
                empGrievance.setIsforwarded(rs.getString("IS_FORWARDED"));
                grievncelist.add(empGrievance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return grievncelist;
    }

    @Override
    public List getAdminGrievnceList(String spc, String categoryCode, String status) {
        List grievncelist = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            String sql = "";
            if (status != null) {
                if (status.equals("P")) {
                    sql = " and IS_DISPOSED = 'N' and IS_REJECTED = 'N' and is_forwarded = 'N' ";
                } else if (status.equals("F")) {
                    sql = " and is_forwarded = 'Y' ";
                } else if (status.equals("R")) {
                    sql = " and IS_REJECTED = 'Y' ";
                } else if (status.equals("D")) {
                    sql = " and IS_DISPOSED = 'Y' ";
                }
            }
            if (categoryCode.equals("0")) {
                pstmt = con.prepareStatement("SELECT grievance_id, g_grievance_category.category_code, g_category, grievance, emp_grievance_details.mobile, IS_DISPOSED, IS_REJECTED, grievance_time, "
                        + "is_forwarded,emp_grievance_details.hrmsid, F_NAME, M_NAME, L_NAME  FROM grievance_communications "
                        + "INNER JOIN emp_grievance_details on emp_grievance_details.g_id = grievance_communications.grievance_id "
                        + "INNER JOIN g_grievance_category ON emp_grievance_details.category_code = g_grievance_category.category_code "
                        + "INNER JOIN EMP_MAST ON emp_grievance_details.hrmsid = EMP_MAST.EMP_ID WHERE to_spc = ?" + sql);
                pstmt.setString(1, spc);
            } else {
                pstmt = con.prepareStatement("SELECT grievance_id, g_grievance_category.category_code, g_category, grievance, emp_grievance_details.mobile, IS_DISPOSED, IS_REJECTED, grievance_time, "
                        + "is_forwarded,emp_grievance_details.hrmsid, F_NAME, M_NAME, L_NAME  FROM grievance_communications "
                        + "INNER JOIN emp_grievance_details on emp_grievance_details.g_id = grievance_communications.grievance_id "
                        + "INNER JOIN g_grievance_category ON emp_grievance_details.category_code = g_grievance_category.category_code "
                        + "INNER JOIN EMP_MAST ON emp_grievance_details.hrmsid = EMP_MAST.EMP_ID WHERE to_spc = ? and emp_grievance_details.category_code = ?" + sql);
                pstmt.setString(1, spc);
                pstmt.setString(2, categoryCode);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                EmployeeGrievance empGrievance = new EmployeeGrievance();
                empGrievance.setGid(rs.getInt("grievance_id"));
                empGrievance.setCategory(rs.getString("g_category"));
                empGrievance.setGrievanceDetail(rs.getString("grievance"));
                empGrievance.setAppmobile(rs.getString("mobile"));
                empGrievance.setHrmsid(rs.getString("hrmsid"));
                empGrievance.setFullname(rs.getString("F_NAME") + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + rs.getString("L_NAME"));
                empGrievance.setIsdisposed(rs.getString("IS_DISPOSED"));
                empGrievance.setIsrejected(rs.getString("IS_REJECTED"));
                empGrievance.setGrievanceTime(CommonFunctions.getFormattedOutputDate1(rs.getDate("grievance_time")));
                empGrievance.setIsforwarded(rs.getString("is_forwarded"));
                grievncelist.add(empGrievance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return grievncelist;
    }

    @Override
    public void saveSMSGrievance(SMSGrievance smsGrievance) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String raw_message = smsGrievance.getWhat();
        try {
            String message = raw_message.substring(5);
            String category_code = message.substring(0, 3);
            message = message.substring(4);
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT emp_id,cur_off_code,gpc FROM emp_mast INNER JOIN g_spc on emp_mast.cur_spc = g_spc.spc WHERE mobile = ?");
            pstmt.setString(1, smsGrievance.getWho());
            rs = pstmt.executeQuery();
            String empId = null;
            String offCode = null;
            String gpc = null;
            EmployeeGrievance employeeGrievance = new EmployeeGrievance();
            if (rs.next()) {
                empId = rs.getString("emp_id");
                offCode = rs.getString("cur_off_code");
                gpc = rs.getString("gpc");
            }

            pstmt = con.prepareStatement("SELECT SPC,EMP_ID,F_NAME, M_NAME, L_NAME, SPN FROM G_WORKFLOW_ROUTING "
                    + "INNER JOIN G_SPC ON G_SPC.GPC = G_WORKFLOW_ROUTING.REPORTING_GPC "
                    + "INNER JOIN EMP_MAST ON G_SPC.SPC = EMP_MAST.CUR_SPC "
                    + "WHERE PROCESS_ID = ? AND G_WORKFLOW_ROUTING.GPC = ? AND OFF_CODE = ? AND DEP_CODE = '02' AND IS_REGULAR = 'Y'");
            pstmt.setInt(1, 12);
            pstmt.setString(2, gpc);
            pstmt.setString(3, offCode);
            rs = pstmt.executeQuery();
            String authCode = "-";
            if (rs.next()) {
                EmployeeBasicProfile empBasicProfile = new EmployeeBasicProfile();
                empBasicProfile.setEmpid(rs.getString("EMP_ID"));
                empBasicProfile.setSpn(rs.getString("SPN"));
                empBasicProfile.setSpc(rs.getString("SPC"));
                empBasicProfile.setFname(rs.getString("F_NAME"));
                empBasicProfile.setMname(rs.getString("M_NAME"));
                empBasicProfile.setLname(rs.getString("L_NAME"));
                authCode = rs.getString("EMP_ID") + "-" + rs.getString("SPC");
            }

            employeeGrievance.setHrmsid(empId);
            employeeGrievance.setAppmobile(smsGrievance.getWho());
            employeeGrievance.setCategoryCode(category_code);
            employeeGrievance.setGrievanceDetail(message);
            employeeGrievance.setAppoffcode(offCode);
            employeeGrievance.setIsforwarded("Y");
            employeeGrievance.setSource("sms");
            employeeGrievance.setAuthCode(authCode);
            saveEmployeeGrievnce(employeeGrievance);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
    }

    @Override
    public void uploadAttachedFile(int gid, MultipartFile file, String refType) {
        String diskFileName = new Date().getTime() + "";
        Connection con = null;
        PreparedStatement pstmt = null;
        if (!file.isEmpty()) {
            try {
                String oFileName = file.getOriginalFilename();
                String fileType = file.getContentType();
                byte[] bytes = file.getBytes();
                File dir = new File(uploadPath + File.separator);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + diskFileName);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
                con = dataSource.getConnection();
                pstmt = con.prepareStatement("INSERT INTO EMPLOYEE_ATTACHMENT (o_file_name, d_file_name, ref_id, ref_type, file_path, file_type) VALUES (?,?,?,?,?,?)");
                pstmt.setString(1, oFileName);
                pstmt.setString(2, oFileName);
                pstmt.setInt(3, gid);
                pstmt.setString(4, refType);//"GRIEVANCE"
                pstmt.setString(5, dir.getAbsolutePath() + File.separator + diskFileName);
                pstmt.setString(6, fileType);
                pstmt.executeUpdate();
            } catch (IOException | SQLException sqe) {
                sqe.printStackTrace();
            } finally {
                DataBaseFunctions.closeSqlObjects(pstmt, con);
            }
        }
    }

    @Override
    public EmployeeGrievance getGrievanceDetail(int gid) {
        EmployeeGrievance employeeGrievance = new EmployeeGrievance();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT ACKNO, GRIEVANCE_TIME, G_CATEGORY, GRIEVANCE, OFF_EN, SOURCE, emp_grievance_details.hrmsid, F_NAME, M_NAME, L_NAME, IS_DISPOSED, "
                    + "IS_REJECTED, IS_FORWARDED, GRIEV_REMARKS FROM emp_grievance_details "
                    + "INNER JOIN EMP_MAST ON emp_grievance_details.hrmsid = EMP_MAST.EMP_ID "
                    + "LEFT OUTER JOIN g_grievance_category ON emp_grievance_details.category_code = g_grievance_category.category_code "
                    + "LEFT OUTER JOIN G_OFFICE ON emp_grievance_details.OFFICE_CODE = G_OFFICE.OFF_CODE WHERE G_ID=?");
            pstmt.setInt(1, gid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                employeeGrievance.setGid(gid);
                employeeGrievance.setHrmsid(rs.getString("hrmsid"));
                employeeGrievance.setFullname(rs.getString("F_NAME") + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + rs.getString("L_NAME"));
                employeeGrievance.setAckno(rs.getString("ACKNO"));
                employeeGrievance.setGrievanceTime(rs.getString("GRIEVANCE_TIME"));
                employeeGrievance.setCategory(rs.getString("G_CATEGORY"));
                employeeGrievance.setOffname(rs.getString("OFF_EN"));
                employeeGrievance.setGrievanceDetail(rs.getString("GRIEVANCE"));
                employeeGrievance.setSource(rs.getString("SOURCE"));
                employeeGrievance.setIsdisposed(rs.getString("IS_DISPOSED"));
                employeeGrievance.setIsrejected(rs.getString("IS_REJECTED"));
                employeeGrievance.setIsforwarded(rs.getString("IS_FORWARDED"));
                employeeGrievance.setRemark(rs.getString("GRIEV_REMARKS"));
            }
            pstmt = con.prepareStatement("SELECT o_file_name,attachment_id FROM EMPLOYEE_ATTACHMENT WHERE REF_TYPE = 'GRIEVANCE' AND REF_ID = ?");
            pstmt.setInt(1, gid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                employeeGrievance.setAttachementName(rs.getString("o_file_name"));
                employeeGrievance.setAttachementId(rs.getString("attachment_id"));
            }
            pstmt = con.prepareStatement("SELECT * FROM grievance_communications left outer join (SELECT REF_ID,o_file_name,attachment_id FROM EMPLOYEE_ATTACHMENT WHERE REF_TYPE = 'GRIEVANCECOMM') EMPLOYEE_ATTACHMENT on grievance_communications.grievance_id = EMPLOYEE_ATTACHMENT.REF_ID  WHERE grievance_id = ? ORDER BY gc_id DESC LIMIT 1");
            pstmt.setInt(1, gid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                employeeGrievance.setFinalRemark(rs.getString("remarks"));
                employeeGrievance.setAttachementName(rs.getString("o_file_name"));
                employeeGrievance.setAttachementId(rs.getString("attachment_id"));
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return employeeGrievance;
    }

    public void getGrievnceCommunicationAttachment(GrievnceCommunication grvComm) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT o_file_name,attachment_id FROM EMPLOYEE_ATTACHMENT WHERE REF_TYPE = 'GRIEVANCECOMM' AND REF_ID = ?");
            pstmt.setInt(1, grvComm.gcid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                grvComm.setAttachementName(rs.getString("o_file_name"));
                grvComm.setAttachementId(rs.getString("attachment_id"));
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
    }

    @Override
    public List getCommunicationList(int gid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List grievnceCommunicationlist = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT GC_ID, getfullname(FROM_HRMS_ID) AS FROMNAME, getfullname(TO_HRMS_ID) AS TONAME, REMARKS, COMMUNICATION_TIME FROM grievance_communications WHERE GRIEVANCE_ID=?");
            pstmt.setInt(1, gid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                GrievnceCommunication grvComm = new GrievnceCommunication();
                grvComm.setGcid(rs.getInt("GC_ID"));
                grvComm.setFromEmployee(rs.getString("FROMNAME"));
                grvComm.setToEmployee(rs.getString("TONAME"));
                grvComm.setCommTime(rs.getString("COMMUNICATION_TIME"));
                grvComm.setRemark(rs.getString("REMARKS"));
                getGrievnceCommunicationAttachment(grvComm);
                grievnceCommunicationlist.add(grvComm);
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return grievnceCommunicationlist;
    }

    @Override
    public void saveGrievanceCommunication(GrievnceCommunication grievnceCommunication) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            if (grievnceCommunication.getActiontaken().equalsIgnoreCase("forward")) {

            } else if (grievnceCommunication.getActiontaken().equalsIgnoreCase("dispose")) {

            } else if (grievnceCommunication.getActiontaken().equalsIgnoreCase("reject")) {

            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
    }

    @Override
    public List getDashBoardDetail(String offcode, String status) {
        List grievncelist = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "";
            if(status != null && status.equalsIgnoreCase("D")){
                sql = " AND is_pending=0 AND is_disposed = 'Y'";
            }else if(status != null && status.equalsIgnoreCase("R")){
                sql = " AND is_pending=0 AND is_rejected = 'Y'";
            }else if(status != null && status.equalsIgnoreCase("P")){
                sql = " AND IS_DISPOSED = 'N' and IS_REJECTED = 'N' and is_forwarded = 'N' ";
            }
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT gc_id,grievance_id, g_grievance_category.category_code, g_category, grievance, emp_grievance_details.mobile, IS_DISPOSED, IS_REJECTED, grievance_time, "
                        + "is_forwarded,emp_grievance_details.hrmsid, F_NAME, M_NAME, L_NAME, getfullname(to_hrms_id) as pendingat,getspn(to_spc) as pendingatspc  FROM grievance_communications "
                        + "INNER JOIN emp_grievance_details on emp_grievance_details.g_id = grievance_communications.grievance_id "
                        + "INNER JOIN g_grievance_category ON emp_grievance_details.category_code = g_grievance_category.category_code "
                        + "INNER JOIN EMP_MAST ON emp_grievance_details.hrmsid = EMP_MAST.EMP_ID WHERE emp_grievance_details.office_code = ?"+sql);
            pstmt.setString(1, offcode);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                EmployeeGrievance empGrievance = new EmployeeGrievance();                
                empGrievance.setGid(rs.getInt("grievance_id"));
                empGrievance.setCategory(rs.getString("g_category"));
                empGrievance.setGrievanceDetail(rs.getString("grievance"));
                empGrievance.setAppmobile(rs.getString("mobile"));
                empGrievance.setHrmsid(rs.getString("hrmsid"));
                empGrievance.setFullname(rs.getString("F_NAME") + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + rs.getString("L_NAME"));
                empGrievance.setIsdisposed(rs.getString("IS_DISPOSED"));
                empGrievance.setIsrejected(rs.getString("IS_REJECTED"));
                empGrievance.setGrievanceTime(CommonFunctions.getFormattedOutputDate1(rs.getDate("grievance_time")));
                empGrievance.setIsforwarded(rs.getString("is_forwarded"));
                empGrievance.setPendingat(rs.getString("pendingat")+", "+rs.getString("pendingatspc"));
                grievncelist.add(empGrievance);
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return grievncelist;
    }

    @Override
    public GrievanceDashBoard getDashBoardDetail(String offcode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        GrievanceDashBoard gdashboard = new GrievanceDashBoard();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT COUNT(*) AS total_griev FROM emp_grievance_details WHERE office_code = ?");
            pstmt.setString(1, offcode);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                gdashboard.setTotalGrievance(rs.getInt("total_griev"));
            }

            pstmt = con.prepareStatement("SELECT COUNT(*) AS total_griev FROM emp_grievance_details WHERE office_code = ? AND is_disposed = 'Y'");
            pstmt.setString(1, offcode);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                gdashboard.setDisposedGrievance(rs.getInt("total_griev"));
            }

            pstmt = con.prepareStatement("SELECT COUNT(*) AS total_griev FROM emp_grievance_details WHERE office_code = ? AND is_rejected = 'Y'");
            pstmt.setString(1, offcode);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                gdashboard.setRejectedGrievance(rs.getInt("total_griev"));
            }

        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return gdashboard;
    }

    public void getDashBoardDetail(GrievanceDashBoard gdashboard) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT COUNT(*) AS total_griev FROM emp_grievance_details WHERE office_code = ?");
            pstmt.setString(1, gdashboard.getOffcode());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                gdashboard.setTotalGrievance(rs.getInt("total_griev"));
            }

            pstmt = con.prepareStatement("SELECT COUNT(*) AS total_griev FROM emp_grievance_details WHERE office_code = ? AND is_disposed = 'Y'");
            pstmt.setString(1, gdashboard.getOffcode());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                gdashboard.setDisposedGrievance(rs.getInt("total_griev"));
            }

            pstmt = con.prepareStatement("SELECT COUNT(*) AS total_griev FROM emp_grievance_details WHERE office_code = ? AND is_rejected = 'Y'");
            pstmt.setString(1, gdashboard.getOffcode());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                gdashboard.setRejectedGrievance(rs.getInt("total_griev"));
            }

        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
    }

    @Override
    public List getBattalionwiseDashBoardDetail() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List battalionwiseDashBoardDetail = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT OFF_CODE,OFF_EN FROM g_office WHERE LVL='32'");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                GrievanceDashBoard gdashboard = new GrievanceDashBoard();
                gdashboard.setBattalionName(rs.getString("OFF_EN"));
                gdashboard.setOffcode(rs.getString("OFF_CODE"));
                getDashBoardDetail(gdashboard);
                battalionwiseDashBoardDetail.add(gdashboard);
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return battalionwiseDashBoardDetail;
    }

    @Override
    public int disposeGrievance(GrievnceCommunication grievnceCommunication) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int gcommid = 0;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE GRIEVANCE_COMMUNICATIONS SET IS_PENDING = 0 where grievance_id = ?");
            pstmt.setInt(1, grievnceCommunication.getGid());
            pstmt.executeUpdate();

            String authCode = grievnceCommunication.getToEmployee();
            String[] decodeVal = StringUtils.split(authCode, "-");
            pstmt = con.prepareStatement("INSERT INTO grievance_communications (grievance_id, from_hrms_id, from_spc, to_hrms_id, to_spc, communication_time, remarks, is_pending) VALUES (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, grievnceCommunication.getGid());
            pstmt.setString(2, grievnceCommunication.getFromEmployee());
            pstmt.setString(3, grievnceCommunication.getFromEmployeeSPC());
            pstmt.setString(4, decodeVal[0]);
            pstmt.setString(5, decodeVal[1]);
            pstmt.setDate(6, new java.sql.Date(new Date().getTime()));
            pstmt.setString(7, grievnceCommunication.getRemark());
            pstmt.setInt(8, 1);//1 for pending 0 for not pending
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                gcommid = rs.getInt(1);
            }

            pstmt = con.prepareStatement("UPDATE EMP_GRIEVANCE_DETAILS SET GRIEV_REMARKS = ? , is_disposed = 'Y', disposed_on = ? WHERE G_ID = ?");
            pstmt.setString(1, grievnceCommunication.getRemark());
            pstmt.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
            pstmt.setInt(3, grievnceCommunication.getGid());
            pstmt.executeUpdate();
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return gcommid;
    }

    @Override
    public int rejectGrievance(GrievnceCommunication grievnceCommunication) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int gcommid = 0;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE GRIEVANCE_COMMUNICATIONS SET IS_PENDING = 0 where grievance_id = ?");
            pstmt.setInt(1, grievnceCommunication.getGid());
            pstmt.executeUpdate();

            String authCode = grievnceCommunication.getToEmployee();
            String[] decodeVal = StringUtils.split(authCode, "-");
            pstmt = con.prepareStatement("INSERT INTO grievance_communications (grievance_id, from_hrms_id, from_spc, to_hrms_id, to_spc, communication_time, remarks, is_pending) VALUES (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, grievnceCommunication.getGid());
            pstmt.setString(2, grievnceCommunication.getFromEmployee());
            pstmt.setString(3, grievnceCommunication.getFromEmployeeSPC());
            pstmt.setString(4, decodeVal[0]);
            pstmt.setString(5, decodeVal[1]);
            pstmt.setDate(6, new java.sql.Date(new Date().getTime()));
            pstmt.setString(7, grievnceCommunication.getRemark());
            pstmt.setInt(8, 1);//1 for pending 0 for not pending
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                gcommid = rs.getInt(1);
            }

            pstmt = con.prepareStatement("UPDATE EMP_GRIEVANCE_DETAILS SET GRIEV_REMARKS = ? , is_rejected = 'Y', disposed_on = ? WHERE G_ID = ?");
            pstmt.setString(1, grievnceCommunication.getRemark());
            pstmt.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
            pstmt.setInt(3, grievnceCommunication.getGid());
            pstmt.executeUpdate();
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return gcommid;
    }

    @Override
    public int forwardGrievance(GrievnceCommunication grievnceCommunication) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int gcommid = 0;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE GRIEVANCE_COMMUNICATIONS SET IS_PENDING = 0 where grievance_id = ?");
            pstmt.setInt(1, grievnceCommunication.getGid());
            pstmt.executeUpdate();

            String authCode = grievnceCommunication.getToEmployee();
            String[] decodeVal = StringUtils.split(authCode, "-");
            pstmt = con.prepareStatement("INSERT INTO grievance_communications (grievance_id, from_hrms_id, from_spc, to_hrms_id, to_spc, communication_time, remarks, is_pending) VALUES (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, grievnceCommunication.getGid());
            pstmt.setString(2, grievnceCommunication.getFromEmployee());
            pstmt.setString(3, grievnceCommunication.getFromEmployeeSPC());
            pstmt.setString(4, decodeVal[0]);
            pstmt.setString(5, decodeVal[1]);
            pstmt.setDate(6, new java.sql.Date(new Date().getTime()));
            pstmt.setString(7, grievnceCommunication.getRemark());
            pstmt.setInt(8, 1);//1 for pending 0 for not pending
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                gcommid = rs.getInt(1);
            }

            pstmt = con.prepareStatement("UPDATE EMP_GRIEVANCE_DETAILS SET GRIEV_REMARKS = ? , is_forwarded = 'Y', forwarded_on = ? WHERE G_ID = ?");
            pstmt.setString(1, grievnceCommunication.getRemark());
            pstmt.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
            pstmt.setInt(3, grievnceCommunication.getGid());
            pstmt.executeUpdate();
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return gcommid;
    }
}
