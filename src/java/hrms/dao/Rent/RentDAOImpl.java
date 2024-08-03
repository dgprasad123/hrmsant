/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.Rent;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.GetUserAttribute;
import hrms.model.Rent.ChallanDtls;
import hrms.model.Rent.IfmsTransactionBean;
import hrms.model.Rent.QuarterBean;
import hrms.model.Rent.QuarterList;
import hrms.model.Rent.ScrollData;
import hrms.model.Rent.ScrollMain;
import hrms.model.Rent.WorkflowList;
import hrms.model.employee.Address;
import hrms.model.login.Users;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manoj PC
 */
public class RentDAOImpl implements RentDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    private String pwd_policy = "((?=.*[a-zA-Z]).(?=.*[@#$%]).(?=.*[@#$%]).{6,})";
    private Pattern pattern;
    private Matcher matcher;
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Users getGPFEmpDetail(String gpfNo) {
        Users empBean = new Users();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String sql = null;
        String sql1 = null;
        String postName = "";
        String deptName = "";
        String officeName = "";
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT emp_id, gpf_no, ARRAY_TO_STRING(ARRAY[A.INITIALS, A.F_NAME, A.M_NAME, A.L_NAME], ' ') EMPNAME, mobile, email_id,"
                    + " B.SPC, B.SPN, B.GPC, C.POST,C.POST_CODE, E.off_en, E.off_code"
                    + ",C.DEPARTMENT_CODE,GD.department_name, A.GP FROM EMP_MAST A "
                    + "	LEFT outer join G_SPC B on A.CUR_SPC=B.SPC "
                    + "	left outer join g_post C on B.gpc=C.post_code "
                    + " LEFT OUTER JOIN g_department GD ON C.department_code = GD.department_code, "
                    + "	G_OFFICE E "
                    + "	WHERE A.CUR_OFF_CODE=E.OFF_CODE AND A.EMP_ID=? OR A.GPF_NO = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, gpfNo);
            ps.setString(2, gpfNo);
            rs = ps.executeQuery();
            if (rs.next()) {
                empBean.setFullName(rs.getString("EMPNAME"));
                empBean.setPostname(rs.getString("SPN"));
                empBean.setPostCode(rs.getString("spc"));
                empBean.setDeptName(rs.getString("department_name"));
                empBean.setSpn(rs.getString("POST"));
                empBean.setOffname(rs.getString("off_en"));
                empBean.setOffcode(rs.getString("off_code"));
                empBean.setMobile(rs.getString("mobile"));
                empBean.setLname(rs.getString("email_id"));
                empBean.setEmpId(rs.getString("emp_id"));
                empBean.setGpfno(rs.getString("gpf_no"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empBean;
    }

    @Override
    public Address getRentEmpAddress(String empId) {
        Address addr = new Address();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String sql = null;
        String sql1 = null;
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT ED.*, GD.dist_name, GS.state_name from emp_address ED LEFT OUTER JOIN g_state GS ON ED.state_code = GS.state_code\n"
                    + "	LEFT OUTER JOIN g_district GD ON ED.dist_code = GD.dist_code WHERE ED.emp_id = ? AND ED.address_type = 'PRESENT'";
            ps = con.prepareStatement(sql);
            ps.setString(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {
                addr.setAddress(rs.getString("address"));
                addr.setDistCode(rs.getString("dist_name"));
                addr.setStateCode(rs.getString("state_name"));
                addr.setPin(rs.getString("pin"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return addr;
    }

    @Override
    public Users getRentEmpDetail(String empId) {
        Users empBean = new Users();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String sql = null;
        String sql1 = null;
        String postName = "";
        String deptName = "";
        String officeName = "";
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT ARRAY_TO_STRING(ARRAY[A.INITIALS, A.F_NAME, A.M_NAME, A.L_NAME], ' ') EMPNAME, A.dos, A.mobile, A.email_id"
                    + ", (SELECT ARRAY_TO_STRING(ARRAY[F_NAME, M_NAME, L_NAME], ' ') FROM emp_relation"
                    + " WHERE emp_id = A.emp_id AND relation = 'FATHER' LIMIT 1) AS father_name,"
                    + " B.SPC, B.SPN, B.GPC, C.POST,C.POST_CODE, E.off_en, E.off_code"
                    + ",C.DEPARTMENT_CODE,GD.department_name FROM EMP_MAST A "
                    + "	LEFT outer join G_SPC B on A.CUR_SPC=B.SPC "
                    + "	left outer join g_post C on B.gpc=C.post_code "
                    + " LEFT OUTER JOIN g_department GD ON C.department_code = GD.department_code, "
                    + "	G_OFFICE E "
                    + "	WHERE A.CUR_OFF_CODE=E.OFF_CODE AND A.EMP_ID=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {
                empBean.setFullName(rs.getString("EMPNAME"));
                empBean.setPostname(rs.getString("SPN"));
                empBean.setPostCode(rs.getString("spc"));
                empBean.setDeptName(rs.getString("department_name"));
                empBean.setDepcode(rs.getString("department_code"));
                empBean.setPostname(rs.getString("POST"));
                empBean.setSpn(rs.getString("POST"));
                empBean.setOffname(rs.getString("off_en"));
                empBean.setOffcode(rs.getString("off_code"));
                empBean.setFname(rs.getString("father_name"));
                empBean.setEmpDos(CommonFunctions.getFormattedOutputDate2(rs.getDate("dos")));
                empBean.setMobile(rs.getString("mobile"));
                empBean.setLname(rs.getString("email_id"));
                empBean.setGpc(rs.getString("GPC"));
            }
            sql1 = " SELECT GP.post, GD.department_name, GO.off_en FROM emp_relieve ER "
                    + " INNER JOIN g_spc GS ON ER.spc = GS.spc "
                    + " INNER JOIN g_post GP ON GP.post_code = GS.gpc "
                    + " INNER JOIN g_office GO ON GO.off_code = GS.off_code"
                    + " INNER JOIN g_department GD ON GD.department_code = GP.department_code"
                    + " WHERE ER.not_type = 'RETIREMENT' "
                    + " and ER.EMP_ID = ?";
            //System.out.println("Query:"+sql1+"->"+empId);
            ps1 = con.prepareStatement(sql1);
            ps1.setString(1, empId);
            rs1 = ps1.executeQuery();
            if (rs1.next()) {
                postName = rs1.getString("post");
                deptName = rs1.getString("department_name");
                officeName = rs1.getString("off_en");
            }
            empBean.setDeptName(deptName);
            empBean.setPostname(postName);
            if(!officeName.equals(""))
                empBean.setOffname(officeName);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empBean;
    }

    @Override
    public String getRentOfficer() {
        Users empBean = new Users();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String sql = null;
        String sql1 = null;
        String empId = "";
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT E.emp_id FROM emp_mast E INNER JOIN g_spc GS ON E.cur_spc = GS.spc INNER JOIN g_post GP ON GP.post_code = GS.gpc WHERE GP.post_code = '110251'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                empId = rs.getString("emp_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empId;
    }

    @Override
    public void saveNDCApplication(QuarterBean qtBean, String empId, String empSpc, String dirPath) {
        MultipartFile ccPath = qtBean.getCcFile();

        String originalFileName = null;
        String contentType = null;
        Connection con = null;
        PreparedStatement pst = null;
        String rentOfficer = getRentOfficer();
        String gRecovery = "N";
        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Date initiatedDt = dateFormat.parse(startTime);
            if (qtBean.getGratuityRecovery() != null && !qtBean.getGratuityRecovery().equals("")) {
                gRecovery = "Y";
            }
            //Insert application
            pst = con.prepareStatement("INSERT INTO hw_ndc_applications(emp_id, father_name, retirement_spc, date_of_retirement"
                    + ", retirement_department,pension_sanctioning_authority,mobile,email_id,has_posted_in_ctcbbs, has_quarter_now, has_occupied_ga_quarter"
                    + ", has_cleared_outstanding, create_date, emp_name, tc_designation, tc_office, rent_officer, pending_at,status, recovery_from_dcrg) Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            //pst.setInt(1, mcode);
            pst.setString(1, empId);
            pst.setString(2, qtBean.getFatherName());
            pst.setString(3, qtBean.getDesignation());
            pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qtBean.getDateOfRetirement()).getTime()));
            pst.setString(5, qtBean.getRetirementDept());
            pst.setString(6, qtBean.getSltPost());
            pst.setString(7, qtBean.getMobile());
            pst.setString(8, qtBean.getEmail());
            pst.setString(9, qtBean.getIsTwinCity());
            pst.setString(10, qtBean.getIsGaQtr());
            pst.setString(11, qtBean.getHasOccupied());
            pst.setString(12, qtBean.getHasClearedOutstanding());
            pst.setTimestamp(13, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(14, qtBean.getFullName());
            pst.setString(15, qtBean.getTcdesignation());
            pst.setString(16, qtBean.getTcoffice());
            pst.setString(17, rentOfficer);
            pst.setString(18, rentOfficer);
            pst.setString(19, "Pending for Verification");
            pst.setString(20, gRecovery);
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int applicationID = rs.getInt("application_id");
            if (ccPath != null && !ccPath.isEmpty()) {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                originalFileName = ccPath.getOriginalFilename();
                contentType = ccPath.getContentType();

                long time = System.currentTimeMillis();
                String filename = "certificate_" + empId + "_" + applicationID + "_" + time;

                String dirpath = dirPath + "/";
                File newfile = new File(dirpath);
                if (!newfile.exists()) {
                    newfile.mkdirs();
                }

                outputStream = new FileOutputStream(dirpath + filename);
                int read = 0;
                byte[] bytes = new byte[1024];
                inputStream = ccPath.getInputStream();
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                String str = "UPDATE hw_ndc_applications SET cc_file_path = ?, cc_original_file = ?, cc_content_type = ?"
                        + " WHERE application_id = ?";
                pst = con.prepareStatement(str);
                pst.setString(1, filename);
                pst.setString(2, originalFileName);
                pst.setString(3, contentType);
                pst.setInt(4, applicationID);
                pst.executeUpdate();
            }
            //Now insert the places posted
            String[] arrPlace = qtBean.getPlace().split(",");
            String[] arrArea = qtBean.getArea().split(",");
            String[] arrQuarterType = qtBean.getQuarterType().split(",");
            String[] arrBuildingNo = qtBean.getBuildingNo().split(",");
            String place = "";
            String area = "";
            String quarterType = "";
            String buildingNo = "";
            for (int i = 0; i < arrPlace.length; i++) {
                place = arrPlace[i];
                area = arrArea[i];
                quarterType = arrQuarterType[i];
                buildingNo = arrBuildingNo[i];
                String query = "INSERT INTO hw_ndc_qtr_details(application_id, place, area, quarter_type, building_number) VALUES(?,?,?,?,?)";
                pst = con.prepareStatement(query);
                pst.setInt(1, applicationID);
                pst.setString(2, place);
                pst.setString(3, area);
                pst.setString(4, quarterType);
                pst.setString(5, buildingNo);
                pst.executeUpdate();
            }
            /*//Now insert into the task_master
             pst = con.prepareStatement("INSERT INTO TASK_MASTER(PROCESS_ID, INITIATED_BY, INITIATED_ON, STATUS_ID"
             + ", PENDING_AT,APPLY_TO,INITIATED_SPC,PENDING_SPC) Values (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
             //pst.setInt(1, mcode);
             pst.setInt(1, 4);
             pst.setString(2, empId);
             pst.setTimestamp(3, new Timestamp(initiatedDt.getTime()));
             pst.setInt(4, 15);
             pst.setString(5, GetUserAttribute.getEmpId(con, "OLSGAD00100001102510001"));
             pst.setString(6, GetUserAttribute.getEmpId(con, "OLSGAD00100001102510001"));
             pst.setString(7, empSpc);
             pst.setString(8, "OLSGAD00100001102510001");
             pst.executeUpdate();
             rs = pst.getGeneratedKeys();
             rs.next();
             int taskID = rs.getInt("TASK_ID");
             pst = con.prepareStatement("UPDATE HW_NDC_APPLICATIONS SET TASK_ID=?"
             + " WHERE application_id=?");
             pst.setInt(1, taskID);
             pst.setInt(2, applicationID);
             pst.execute();*/

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveNDCRentApplication(QuarterBean qtBean, String empId, String empSpc) {
        MultipartFile ccPath = qtBean.getCcFile();

        String originalFileName = null;
        String contentType = null;
        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement ps = null;
        ResultSet rs1 = null;
        String rentOfficer = getRentOfficer();
        String gRecovery = "N";
        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Date initiatedDt = dateFormat.parse(startTime);
            if (qtBean.getGratuityRecovery() != null && !qtBean.getGratuityRecovery().equals("")) {
                gRecovery = "Y";
            }
            int ndcFileId = 0;
            //Get the file ID
            ps = con.prepareStatement("SELECT MAX(ndc_file_id) AS ndc_file_id FROM hw_ndc_applications");
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                ndcFileId = rs1.getInt("ndc_file_id");
                ndcFileId = ndcFileId + 1;
            }
            //Insert application
            pst = con.prepareStatement("INSERT INTO hw_ndc_applications(emp_id, father_name, retirement_spc, date_of_retirement"
                    + ", retirement_department,psa_name,mobile,email_id"
                    + ", create_date, emp_name, rent_officer, pending_at,status, quarter_unit"
                    + ", qtr_type, building_no, has_vacated, has_occupied_ga_quarter,ndc_file_id) Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            //pst.setInt(1, mcode);
            //System.out.println("PSA:"+qtBean.getPensionSanctioningAuthority());
            pst.setString(1, empId);
            pst.setString(2, qtBean.getFatherName());
            pst.setString(3, qtBean.getDesignation());
            pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qtBean.getDateOfRetirement()).getTime()));
            pst.setString(5, qtBean.getRetirementDept());
            pst.setString(6, qtBean.getPensionSanctioningAuthority());
            pst.setString(7, qtBean.getMobile());
            pst.setString(8, qtBean.getEmail());
            pst.setTimestamp(9, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(10, qtBean.getFullName());
            pst.setString(11, rentOfficer);
            pst.setString(12, rentOfficer);
            pst.setString(13, "Pending for Verification");
            pst.setString(14, qtBean.getQuarterUnit());
            pst.setString(15, qtBean.getQuarterType());
            pst.setString(16, qtBean.getBuildingNo());
            pst.setString(17, qtBean.getHasVacated());
            pst.setString(18, qtBean.getHasOccupied());
            pst.setInt(19, ndcFileId);
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int applicationID = rs.getInt("application_id");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public QuarterBean getNdcApplicationDetail(String empId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        String sql = "";
        QuarterBean qBean = null;
        String isTwinCity = "No";
        String isGaQtr = "No";
        String hasOccupied = "No";
        String hasClearedOutstanding = "No";
        String gRecovery = "No";
        String hasVacated = "No";
        qBean = new QuarterBean();
        qBean.setIsApplied(false);
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT * FROM hw_ndc_applications WHERE emp_id = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, empId);
            rs = ps.executeQuery();
            while (rs.next()) {
                String authority = "";
                sql = "select getspn(?) as current_post";
                ps1 = con.prepareStatement(sql);
                ps1.setString(1, rs.getString("pension_sanctioning_authority"));
                rs1 = ps1.executeQuery();
                while (rs1.next()) {
                    authority = rs1.getString("current_post");
                }

                qBean.setPensionSanctioningAuthority(rs.getString("psa_name"));
                if (rs.getString("has_posted_in_ctcbbs").equals("Y")) {
                    isTwinCity = "Yes";
                }
                if (rs.getString("has_quarter_now").equals("Y")) {
                    isGaQtr = "Yes";
                }
                if (rs.getString("has_occupied_ga_quarter").equals("Y")) {
                    hasOccupied = "Yes";
                }
                if (rs.getString("has_cleared_outstanding").equals("Y")) {
                    hasClearedOutstanding = "Yes";
                }
                if (rs.getString("recovery_from_dcrg").equals("Y")) {
                    gRecovery = "Yes";
                }
                if (rs.getString("has_vacated").equals("Y")) {
                    hasVacated = "Yes";
                }
                qBean.setApplicationId(rs.getInt("application_id") + "");
                qBean.setEmail(rs.getString("email_id"));
                qBean.setIsTwinCity(isTwinCity);
                qBean.setIsGaQtr(isGaQtr);
                qBean.setHasOccupied(hasOccupied);
                qBean.setHasClearedOutstanding(hasClearedOutstanding);
                qBean.setTcdesignation(rs.getString("tc_designation"));
                qBean.setTcoffice(rs.getString("tc_office"));
                qBean.setApplicationId(rs.getInt("application_id") + "");
                qBean.setRetirementDept(rs.getString("retirement_department"));
                qBean.setRetSPC(rs.getString("retirement_spc"));
                qBean.setApplicationStatus(rs.getString("status"));
                qBean.setHasFinalNDC(rs.getString("has_final_ndc"));
                qBean.setApplicationId(rs.getInt("application_id") + "");
                qBean.setNondcRemark(rs.getString("nondc_remark"));
                qBean.setGratuityRecovery(gRecovery);
                qBean.setQuarterUnit(rs.getString("quarter_unit"));
                qBean.setPlace(rs.getString("qtr_place"));
                qBean.setQuarterType(rs.getString("qtr_type"));
                qBean.setBuildingNo(rs.getString("building_no"));
                qBean.setOutstandingAmount(rs.getString("outstanding_amount"));
                qBean.setRecoveryAmount(rs.getString("recovery_amount"));
                qBean.setNondcRemark(rs.getString("nondc_remark"));
                qBean.setHasVacated(hasVacated);
                qBean.setIsApplied(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return qBean;
    }

    @Override
    public QuarterBean getAuthApplicationDetail(String applicationId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        String sql = "";
        QuarterBean qBean = null;
        String isTwinCity = "No";
        String isGaQtr = "No";
        String hasOccupied = "No";
        String hasClearedOutstanding = "No";
        String hasVerificationReport = "No";
        String jsNeverOccupied = "No";
        String jsVacated = "No";
        String jsPendingDues = "No";
        String jsNotVacated = "No";
        String gRecovery = "No";
        String hasVacated = "No";
        qBean = new QuarterBean();
        qBean.setIsApplied(false);
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT * FROM hw_ndc_applications WHERE application_id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(applicationId));
            rs = ps.executeQuery();
            while (rs.next()) {
                String authority = "";
                sql = "select getspn(?) as current_post";
                ps1 = con.prepareStatement(sql);
                ps1.setString(1, rs.getString("pension_sanctioning_authority"));
                rs1 = ps1.executeQuery();
                while (rs1.next()) {
                    authority = rs1.getString("current_post");
                }

                qBean.setPensionSanctioningAuthority(rs.getString("psa_name"));
                if (rs.getString("has_posted_in_ctcbbs").equals("Y")) {
                    isTwinCity = "Yes";
                }
                if (rs.getString("has_quarter_now").equals("Y")) {
                    isGaQtr = "Yes";
                }
                if (rs.getString("has_occupied_ga_quarter").equals("Y")) {
                    hasOccupied = "Yes";
                }
                if (rs.getString("has_cleared_outstanding").equals("Y")) {
                    hasClearedOutstanding = "Yes";
                }
                if (rs.getString("has_verification_report").equals("Y")) {
                    hasVerificationReport = "Yes";
                }
                if (rs.getString("recovery_from_dcrg").equals("Y")) {
                    gRecovery = "Yes";
                }
                if (rs.getString("js_never_occupied").equals("Y")) {
                    jsNeverOccupied = "Yes";
                }
                if (rs.getString("js_not_vacated").equals("Y")) {
                    jsNotVacated = "Yes";
                }
                if (rs.getString("js_pending_dues").equals("Y")) {
                    jsPendingDues = "Yes";
                }
                if (rs.getString("js_vacated").equals("Y")) {
                    jsVacated = "Yes";
                }
                if (rs.getString("has_vacated").equals("Y")) {
                    hasVacated = "Yes";
                }
                qBean.setEmpId(rs.getString("emp_id"));
                qBean.setApplicationId(rs.getInt("application_id") + "");
                qBean.setFullName(rs.getString("emp_name"));
                qBean.setFatherName(rs.getString("father_name"));
                qBean.setDesignation(rs.getString("retirement_spc"));
                qBean.setDateOfRetirement(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("date_of_retirement")));
                qBean.setRetirementDept(rs.getString("retirement_department"));
                qBean.setMobile(rs.getString("mobile"));
                qBean.setEmail(rs.getString("email_id"));
                qBean.setIsTwinCity(isTwinCity);
                qBean.setIsGaQtr(isGaQtr);
                qBean.setHasOccupied(hasOccupied);
                qBean.setHasClearedOutstanding(hasClearedOutstanding);
                qBean.setTcdesignation(rs.getString("tc_designation"));
                qBean.setTcoffice(rs.getString("tc_office"));
                qBean.setIsForwarded(rs.getString("is_forwarded"));
                qBean.setPendingAt(rs.getString("pending_at"));
                qBean.setRentOfficer(rs.getString("rent_officer"));
                qBean.setApplicationStatus(rs.getString("status"));
                qBean.setSectionName(rs.getString("section_name"));
                qBean.setGratuityRecovery(gRecovery);
                qBean.setHasVerificationReport(hasVerificationReport);
                qBean.setOutstandingAmount(rs.getString("outstanding_amount"));
                if (rs.getString("is_forwarded").equals("Y")) {
                    List li = new ArrayList();
                    sql = "SELECT * FROM ndc_workflow_log WHERE application_id = ? ORDER BY date_created";
                    ps2 = con.prepareStatement(sql);
                    ps2.setInt(1, rs.getInt("application_id"));
                    rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        Users ue = getRentEmpDetail(rs2.getString("from_id"));
                        Users ue1 = getRentEmpDetail(rs2.getString("to_id"));
                        WorkflowList wlist = new WorkflowList();
                        wlist.setFromName(ue.getFullName());
                        wlist.setFromDesignation(ue.getSpn());
                        wlist.setToName(ue1.getFullName());
                        wlist.setToDesignation(ue1.getSpn());
                        wlist.setRemarks(rs2.getString("remarks"));
                        wlist.setGpc(ue.getGpc());
                        wlist.setDateCreated(CommonFunctions.getFormattedOutputDate1(rs2.getTimestamp("date_created")));
                        li.add(wlist);
                    }
                    qBean.setWorkflowLogList(li);
                }

                qBean.setJsNeverOccupied(jsNeverOccupied);
                qBean.setJsNotVacated(jsNotVacated);
                qBean.setJsPendingDues(jsPendingDues);
                qBean.setJsVacated(jsVacated);
                qBean.setRecoveryAmount(rs.getString("recovery_amount"));
                qBean.setIsApplied(true);
                qBean.setQuarterUnit(rs.getString("quarter_unit"));
                qBean.setQuarterType(rs.getString("qtr_type"));
                qBean.setBuildingNo(rs.getString("building_no"));
                qBean.setApplicationId(rs.getString("application_id"));
                qBean.setOutstandingAmount(rs.getString("outstanding_amount"));
                qBean.setHasVacated(hasVacated);
                // System.out.println("Recovery Amount:"+rs.getString("recovery_amount"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return qBean;
    }

    @Override
    public void downloadNDCDocument(HttpServletResponse response, String filePath, int applicationId, String fileType) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        int BUFFER_LENGTH = 4096;

        try {
            OutputStream out = response.getOutputStream();
            con = dataSource.getConnection();

            stmt = con.createStatement();
            String sql = "SELECT cc_file_path, cc_content_type, cc_original_file FROM hw_ndc_applications WHERE application_id = " + applicationId;
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                File f = null;
                String dirpath = filePath + "/" + rs.getString("cc_file_path");
                f = new File(dirpath);
                if (f.exists()) {
                    String originalFilename = rs.getString("cc_original_file");
                    String filetype = rs.getString("cc_content_type");

                    response.setContentLength((int) f.length());
                    FileInputStream is = new FileInputStream(f);

                    response.setContentType(filetype);
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + originalFilename + "\"");

                    byte[] bytes = new byte[BUFFER_LENGTH];
                    int read = 0;
                    while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
                        out.write(bytes, 0, read);
                    }
                    is.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void downloadFinalNDC(HttpServletResponse response, String filePath, int applicationId, String fileType) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        int BUFFER_LENGTH = 4096;

        try {
            OutputStream out = response.getOutputStream();
            con = dataSource.getConnection();

            stmt = con.createStatement();
            String sql = "SELECT ndc_file_path, ndc_content_type, ndc_original_file FROM hw_ndc_applications WHERE application_id = " + applicationId;
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                File f = null;
                String dirpath = filePath + "/" + rs.getString("ndc_file_path");
                f = new File(dirpath);
                if (f.exists()) {
                    String originalFilename = rs.getString("ndc_original_file");
                    String filetype = rs.getString("ndc_content_type");

                    response.setContentLength((int) f.length());
                    FileInputStream is = new FileInputStream(f);

                    response.setContentType(filetype);
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + originalFilename + "\"");

                    byte[] bytes = new byte[BUFFER_LENGTH];
                    int read = 0;
                    while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
                        out.write(bytes, 0, read);
                    }
                    is.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List applicationList(String empId) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List li = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("SELECT * FROM hw_ndc_applications  WHERE (pending_at = ? OR rent_officer = ?) AND (status = 'Pending for Verification' OR status='NDC Generated' OR status = 'Recovery Intimation Generated') AND has_final_ndc = 'N' ORDER BY create_date desc");
            ps.setString(1, empId);
            ps.setString(2, empId);
            rs = ps.executeQuery();
            while (rs.next()) {
                QuarterBean qBean = new QuarterBean();
                qBean.setApplicationId(rs.getInt("application_id") + "");
                qBean.setDateOfRetirement(CommonFunctions.getFormattedOutputDate3(rs.getDate("date_of_retirement")));
                qBean.setDesignation(rs.getString("retirement_spc"));
                qBean.setEmpId(rs.getString("emp_id"));
                qBean.setFatherName(rs.getString("father_name"));
                qBean.setFullName(rs.getString("emp_name"));
                if (rs.getString("has_cleared_outstanding").equals("Y")) {
                    qBean.setHasClearedOutstanding("Yes");
                } else {
                    qBean.setHasClearedOutstanding("No");
                }
                if (rs.getString("has_quarter_now").equals("Y")) {
                    qBean.setIsGaQtr("Yes");
                } else {
                    qBean.setIsGaQtr("No");
                }
                if (rs.getString("has_occupied_ga_quarter").equals("Y")) {
                    qBean.setHasOccupied("Yes");
                } else {
                    qBean.setHasOccupied("No");
                }
                if (rs.getString("has_posted_in_ctcbbs").equals("Y")) {
                    qBean.setIsTwinCity("Yes");
                } else {
                    qBean.setIsTwinCity("No");
                }
                if (rs.getString("has_vacated").equals("Y")) {
                    qBean.setHasVacated("Yes");
                } else {
                    qBean.setHasVacated("No");
                }
                qBean.setMobile(rs.getString("mobile"));
                qBean.setApplicationStatus(rs.getString("status"));
                qBean.setIsNDCGenerated(rs.getString("is_ndc_generated"));
                qBean.setHasFinalNDC(rs.getString("has_final_ndc"));
                qBean.setHasRecoveryIntimation(rs.getString("has_recovery_intimation"));
                qBean.setAddress(rs.getString("qtr_address"));
                qBean.setQuarterUnit(rs.getString("quarter_unit"));
                qBean.setQuarterType(rs.getString("qtr_type"));
                qBean.setBuildingNo(rs.getString("building_no"));
                qBean.setOutstandingAmount(rs.getString("outstanding_amount"));
                qBean.setDateCreated(CommonFunctions.getFormattedOutputDate3(rs.getDate("create_date")));
                qBean.setNondcRemark(rs.getString("nondc_remark"));
                li.add(qBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getAuthList() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        List li = new ArrayList();
        String sectionName = "";
        try {
            con = this.dataSource.getConnection();
            String rentOfficer = getRentOfficer();
            Users ue = getRentEmpDetail(rentOfficer);
            SelectOption so = new SelectOption();
            so.setLabel("RENT OFFICER [" + ue.getFullName() + "]");
            so.setValue(rentOfficer);
            li.add(so);
            ps = con.prepareStatement(" select emp_id, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,cur_spc,g.spn,g.gpc, gp.post, NSL.roman_name from emp_mast e"
                    + " inner join user_details u on e.emp_id=u.linkid "
                    + " INNER JOIN G_SPC g ON e.cur_spc=g.spc INNER JOIN g_post gp ON GP.post_code = g.gpc LEFT OUTER JOIN ndc_section_list NSL ON g.spc = NSL.spc where e.cur_off_code='KRDGAD0010000' AND DEP_CODE='02' AND CUR_SPC IS NOT NULL "
                    + " AND g.gpc IN('110059','110132','110135','110318')"
                    + " order by g.spn;");
            rs = ps.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                if (rs.getString("gpc").equals("110132") && rs.getString("roman_name") != null) {
                    so.setLabel(rs.getString("post") + " [" + rs.getString("EMPNAME") + "] (Section-" + rs.getString("roman_name") + ")");
                } else {
                    so.setLabel(rs.getString("post") + " [" + rs.getString("EMPNAME") + "]");
                }
                so.setValue(rs.getString("emp_id"));
                li.add(so);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void saveNDCAuthority(QuarterBean qtBean, String empId, String gpc) {
        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            String startTime = dateFormat.format(cal.getTime());
            Date initiatedDt = dateFormat.parse(startTime);
            int applicationId = Integer.parseInt(qtBean.getApplicationId());
            //Insert application
            if (gpc.equals("110135") || gpc.equals("110318")) {
                String jsNeverOccupied = "N";
                String jsNotVacated = "N";
                String jsPendingDues = "N";
                String jsVacated = "N";
                if (qtBean.getJsNeverOccupied() != null && qtBean.getJsNeverOccupied().equals("Y")) {
                    jsNeverOccupied = "Y";
                }
                if (qtBean.getJsVacated() != null && qtBean.getJsVacated().equals("Y")) {
                    jsNotVacated = "Y";
                }
                if (qtBean.getJsPendingDues() != null && qtBean.getJsPendingDues().equals("Y")) {
                    jsPendingDues = "Y";
                }
                if (qtBean.getJsNotVacated() != null && qtBean.getJsNotVacated().equals("Y")) {
                    jsVacated = "Y";
                }
                pst = con.prepareStatement("UPDATE hw_ndc_applications SET pending_at = ?, status='Pending for Verification'"
                        + ", is_forwarded = 'Y', has_verification_report = 'Y', js_never_occupied = ?, js_vacated = ?, js_pending_dues = ?, js_not_vacated = ?  WHERE application_id = ?");
                pst.setString(1, qtBean.getForwardingAuthority());
                pst.setString(2, jsNeverOccupied);
                pst.setString(3, jsNotVacated);
                pst.setString(4, jsPendingDues);
                pst.setString(5, jsVacated);
                pst.setInt(6, applicationId);
            } else if (gpc.equals("110132")) {
                Users ue = getRentEmpDetail(empId);
                String spc = ue.getPostCode();
                pst2 = con.prepareStatement("SELECT roman_name FROM ndc_section_list WHERE spc = ?");
                pst2.setString(1, spc);
                rs = pst2.executeQuery();
                String romanName = "";
                while (rs.next()) {
                    romanName = rs.getString("roman_name");
                }
                //System.out.println("Roman Name:"+romanName);
                pst = con.prepareStatement("UPDATE hw_ndc_applications SET pending_at = ?, status='Pending for Verification', is_forwarded = 'Y', section_name = ? WHERE application_id = ?");
                pst.setString(1, qtBean.getForwardingAuthority());
                pst.setString(2, romanName);
                pst.setInt(3, applicationId);
            } else {
                pst = con.prepareStatement("UPDATE hw_ndc_applications SET pending_at = ?, status='Pending for Verification', is_forwarded = 'Y' WHERE application_id = ?");
                pst.setString(1, qtBean.getForwardingAuthority());
                pst.setInt(2, applicationId);
            }
            //pst.setInt(1, mcode);

            pst.executeUpdate();

            //Now Insert into workflow table;
            pst1 = con.prepareStatement("INSERT INTO ndc_workflow_log(application_id, from_id, to_id, remarks, date_created) VALUES(?, ?, ?, ?, ?)");
            pst1.setInt(1, applicationId);
            pst1.setString(2, empId);
            pst1.setString(3, qtBean.getForwardingAuthority());
            pst1.setString(4, qtBean.getRemarks());
            pst1.setTimestamp(5, new Timestamp(initiatedDt.getTime()));
            pst1.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateNDCStatus(QuarterBean qtBean) {
        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            String startTime = dateFormat.format(cal.getTime());
            Date initiatedDt = dateFormat.parse(startTime);
            int applicationId = Integer.parseInt(qtBean.getApplicationId());

            if (qtBean.getApplicationStatus().equals("Not Approved")) {
                pst = con.prepareStatement("UPDATE hw_ndc_applications SET status='Not Approved', final_status = 'Not Approved', issue_type = '', is_ndc_generated = 'N' WHERE application_id = ?");
                pst.setInt(1, applicationId);
                pst.executeUpdate();
            }
            if (qtBean.getApplicationStatus().equals("Approved")) {
                pst = con.prepareStatement("UPDATE hw_ndc_applications SET is_ndc_generated = 'Y', has_recovery_intimation = 'N', status='NDC Generated', final_status = 'Approved', issue_type = ? WHERE application_id = ?");
                pst.setString(1, qtBean.getIssueType());
                pst.setInt(2, applicationId);
                pst.executeUpdate();
            }
            if (qtBean.getIssueType().equals("Recovery Intimation")) {
                pst = con.prepareStatement("UPDATE hw_ndc_applications SET is_ndc_generated = 'N', has_recovery_intimation = 'Y', status='Recovery Intimation Generated', final_status = 'Approved', issue_type = ?, recovery_amount = ? WHERE application_id = ?");
                pst.setString(1, qtBean.getIssueType());
                pst.setString(2, qtBean.getRecoveryAmount());
                pst.setInt(3, applicationId);
                pst.executeUpdate();
            }
            if (qtBean.getIssueType().equals("No-NDC")) {
                pst = con.prepareStatement("UPDATE hw_ndc_applications SET is_ndc_generated = 'N', has_recovery_intimation = 'N', status='No NDC', final_status = 'Approved', issue_type = ?, nondc_remark = ? WHERE application_id = ?");
                pst.setString(1, qtBean.getIssueType());
                pst.setString(2, qtBean.getNondcRemark());
                pst.setInt(3, applicationId);
                pst.executeUpdate();
            }
            String outstandingAmount = "";
            if (qtBean.getOutstandingAmount() != null && !qtBean.getOutstandingAmount().equals("")) {
                outstandingAmount = qtBean.getOutstandingAmount();
            }
            pst = con.prepareStatement("UPDATE hw_ndc_applications SET outstanding_amount = ? WHERE application_id = ?");
            pst.setString(1, outstandingAmount);
            pst.setInt(2, applicationId);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveFinalNDC(QuarterBean qtBean, String dirPath) {
        int applicationID = Integer.parseInt(qtBean.getApplicationId());
        MultipartFile ccPath = qtBean.getNdcFile();
        String originalFileName = null;
        String contentType = null;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Date initiatedDt = dateFormat.parse(startTime);
            if (ccPath != null && !ccPath.isEmpty()) {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                originalFileName = ccPath.getOriginalFilename();
                contentType = ccPath.getContentType();

                long time = System.currentTimeMillis();
                String filename = "final_ndc_" + applicationID + "_" + time;

                String dirpath = dirPath + "/";
                File newfile = new File(dirpath);
                if (!newfile.exists()) {
                    newfile.mkdirs();
                }

                outputStream = new FileOutputStream(dirpath + filename);
                int read = 0;
                byte[] bytes = new byte[1024];
                inputStream = ccPath.getInputStream();
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                String str = "UPDATE hw_ndc_applications SET ndc_file_path = ?, ndc_original_file = ?, ndc_content_type = ?, has_final_ndc = 'Y'"
                        + " WHERE application_id = ?";
                pst = con.prepareStatement(str);
                pst.setString(1, filename);
                pst.setString(2, originalFileName);
                pst.setString(3, contentType);
                pst.setInt(4, applicationID);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void savePSAApplication(QuarterBean qtBean, String empId, String spc) {
        Connection con = null;
        PreparedStatement pst = null;
        String rentOfficer = getRentOfficer();
        String applyEmpId = qtBean.getHrmsId();
        String applyGpfNo = qtBean.getGpfNo();
        if (qtBean.getHrmsId() != null && !qtBean.getHrmsId().equals("")) {
            Users ue = getRentEmpDetail(qtBean.getHrmsId());
            applyGpfNo = ue.getGpfno();
        }
        if (qtBean.getGpfNo() != null && !qtBean.getGpfNo().equals("")) {
            Users ue = getGPFEmpDetail(qtBean.getGpfNo());
            applyEmpId = ue.getEmpId();
        }
        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Date initiatedDt = dateFormat.parse(startTime);
            //Insert application
            pst = con.prepareStatement("INSERT INTO hw_ndc_psa_applications(full_name, gpf_no, hrms_id, mobile, date_created, psa_spc, psa_id) Values (?,?,?,?,?,?,?)");
            //pst.setInt(1, mcode);
            pst.setString(1, qtBean.getFullName());
            pst.setString(2, qtBean.getGpfNo());
            pst.setString(3, qtBean.getHrmsId());
            pst.setString(4, qtBean.getMobile());
            pst.setTimestamp(5, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(6, spc);
            pst.setString(7, empId);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List psaApplicationList(String empId) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List li = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("SELECT * FROM hw_ndc_psa_applications  WHERE psa_id = ?");
            ps.setString(1, empId);
            rs = ps.executeQuery();
            while (rs.next()) {
                QuarterBean qBean = new QuarterBean();
                //Users ue = getRentEmpDetail(rs2.getString("from_id"));
                String designation = "";
                if (!rs.getString("hrms_id").equals("")) {
                    Users ue = getRentEmpDetail(rs.getString("hrms_id"));
                    designation = ue.getPostname();
                }
                if (!rs.getString("gpf_no").equals("")) {
                    Users ue = getGPFEmpDetail(rs.getString("gpf_no"));
                    designation = ue.getPostname();
                }
                qBean.setDateOfRetirement(CommonFunctions.getFormattedOutputDate3(rs.getDate("date_created")));
                qBean.setFullName(rs.getString("full_name"));
                qBean.setHrmsId(rs.getString("hrms_id"));
                qBean.setGpfNo(rs.getString("gpf_no"));
                qBean.setMobile(rs.getString("mobile"));
                qBean.setDesignation(designation);
                li.add(qBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List psaAdminApplicationList() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List li = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("SELECT * FROM hw_ndc_psa_applications ORDER BY application_id DESC");
            rs = ps.executeQuery();
            while (rs.next()) {
                QuarterBean qBean = new QuarterBean();
                //Users ue = getRentEmpDetail(rs2.getString("from_id"));
                String designation = "";
                if (!rs.getString("hrms_id").equals("")) {
                    Users ue = getRentEmpDetail(rs.getString("hrms_id"));
                    designation = ue.getPostname();
                }
                if (!rs.getString("gpf_no").equals("")) {
                    Users ue = getGPFEmpDetail(rs.getString("gpf_no"));
                    designation = ue.getPostname();
                }
                Users ue = getGPFEmpDetail(rs.getString("psa_id"));
                String psa = ue.getSpn();
                qBean.setDateOfRetirement(CommonFunctions.getFormattedOutputDate3(rs.getDate("date_created")));
                qBean.setFullName(rs.getString("full_name"));
                qBean.setHrmsId(rs.getString("hrms_id"));
                qBean.setGpfNo(rs.getString("gpf_no"));
                qBean.setMobile(rs.getString("mobile"));
                qBean.setDesignation(designation);
                qBean.setPensionSanctioningAuthority(psa);
                li.add(qBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    public static byte[] readKeyFromFile(String keyFileName) {
        final File file = new File(keyFileName);
        final byte[] key = new byte[(int) file.length()];

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.read(key); // read file into bytes[]
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return key;
    }

    public static byte[] genHmac(final byte[] data, final byte[] key) throws Exception {
        final Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        final SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA256");
        sha256_HMAC.init(secretKey);
        return sha256_HMAC.doFinal(data);
    }

    public static byte[] encrypt(final byte[] plainText, final byte[] secret) throws Exception {
        final SecretKeySpec secretKey = new SecretKeySpec(secret, "AES");
        final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(plainText);
    }

    public static byte[] decrypt(final byte[] cypherText, final byte[] secret) throws Exception {
        final SecretKeySpec secretKey = new SecretKeySpec(secret, "AES");
        final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(cypherText);
    }

    public static String getBase64String(final byte[] byteArray) throws Exception {
        return Base64.getEncoder().encodeToString(byteArray);
    }

    public static byte[] decodeBase64StringToByte(String stringData) throws Exception {
        return Base64.getDecoder().decode(stringData.getBytes("UTF-8"));
    }

    @Override
    public String getEncryptedText(String empId, String keyFilePath, String returnPath, String recoveryAmount) {
        String encText = "";
        try {
            Random r = new Random(System.currentTimeMillis());
            int randValue = ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
            Users ue = getRentEmpDetail(empId);
            Address addr = getRentEmpAddress(empId);
            final byte[] secretKey = readKeyFromFile(keyFilePath + "GAQ_binary.key");
            //System.out.println("key " + getBase64String(secretKey));
            final String hmacText = "GAQ|EQRT-" + empId + "-" + randValue + "|0216-01-106-0142-02055-000|eQuarter Outstanding Fee|" + recoveryAmount + "||||||||||||||||" + recoveryAmount + "|" + ue.getFullName() + "|" + addr.getAddress() + "||" + addr.getStateCode() + "|" + addr.getDistCode() + "|" + addr.getPin() + "|" + ue.getMobile() + "||||||||" + returnPath;
            //System.out.println("plainText: " + hmacText);
            //System.out.println("hmac " + getBase64String(genHmac(hmacText.getBytes("UTF-8"), secretKey)));
            final String plainText = "GAQ|EQRT-" + empId + "-" + randValue + "|0216-01-106-0142-02055-000|eQuarter Outstanding Fee|" + recoveryAmount + "||||||||||||||||" + recoveryAmount + "|" + ue.getFullName() + "|" + addr.getAddress() + "||" + addr.getStateCode() + "|" + addr.getDistCode() + "|" + addr.getPin() + "|" + ue.getMobile() + "||||||||" + returnPath + "|" + getBase64String(genHmac(hmacText.getBytes("UTF-8"), secretKey));
            encText = getBase64String(encrypt(plainText.getBytes("UTF-8"), secretKey));
            //System.out.println("encryptedText: " + encryptedText);
            //final byte[] decryptedText = decrypt(decodeBase64StringToByte(encryptedText), secretKey);      
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encText;
    }

    @Override
    public String getDecryptedText(String encryptedText, String keyFilePath) {
        String decText = "";
        try {
            Random r = new Random(System.currentTimeMillis());
            int randValue = ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));

            final byte[] secretKey = readKeyFromFile(keyFilePath + "GAQ_binary.key");
            final byte[] decryptedText = decrypt(decodeBase64StringToByte(encryptedText), secretKey);
            decText = "" + new String(decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decText;
    }

    @Override
    public IfmsTransactionBean updateTransaction(String decText, String applicationId, String empId) {
        Connection con = null;
        PreparedStatement pst = null;
        String[] arrTemp2 = decText.split("\\|");
        String[] arrTemp = decText.split("ApplyRentNDC.htm\\|");
        String transactionAmount = arrTemp2[4];
        String str1 = arrTemp[1];
        String[] arrTemp1 = str1.split("\\|");
        String challanRefNo = arrTemp1[0];
        String paymentMode = arrTemp1[1];
        String bankName = arrTemp1[2];
        String bankTransactionId = arrTemp1[3];
        String transactionstatus = arrTemp1[4];
        String transactionMsg = arrTemp1[5];
        String transactionTime = arrTemp1[6];
        String checksum = arrTemp1[7];
        IfmsTransactionBean iBean = new IfmsTransactionBean();
        iBean.setBankName(bankName);
        iBean.setBankTransactionId(bankTransactionId);
        iBean.setChallanRefNo(challanRefNo);
        iBean.setPaymentMode(paymentMode);
        iBean.setTransactionMsg(transactionMsg);
        iBean.setTransactionTime(transactionTime);
        iBean.setTransactionstatus(transactionstatus);
        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Date initiatedDt = dateFormat.parse(startTime);
            //Insert application
            pst = con.prepareStatement("INSERT INTO ifms_payment_transactions(challan_ref_no, payment_mode, bank_name, bank_transaction_id, transaction_status, transaction_msg, transaction_time, checksum, raw_data, application_id, emp_id, transaction_amount) Values (?,?,?,?,?,?,?,?,?,?,?,?)");
            //pst.setInt(1, mcode);
            pst.setString(1, challanRefNo);
            pst.setString(2, paymentMode);
            pst.setString(3, bankName);
            pst.setString(4, bankTransactionId);
            pst.setString(5, transactionstatus);
            pst.setString(6, transactionMsg);
            pst.setString(7, transactionTime);
            pst.setString(8, checksum);
            pst.setString(9, decText);
            pst.setInt(10, Integer.parseInt(applicationId));
            pst.setString(11, empId);
            pst.setInt(12, Integer.parseInt(transactionAmount));
            pst.executeUpdate();

            pst = con.prepareStatement("UPDATE hw_ndc_applications SET transaction_id = ? WHERE application_id = ?");
            pst.setString(1, bankTransactionId);
            pst.setInt(2, Integer.parseInt(applicationId));
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return iBean;
    }

    @Override
    public List getQuarterUnitAreaList() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        List unitAreaList = new ArrayList();
        try {
            con = dataSource.getConnection();
            String sql = "SELECT distinct unit_qt from consumer_ga order by unit_qt";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("unit_qt"));
                so.setLabel(rs.getString("unit_qt"));
                unitAreaList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt, con);
        }
        return unitAreaList;
    }

    @Override
    public List getTypeWiseBuildingNo(String qtrUnit, String qtrType) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List unitAreaList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT distinct bldgno_qt from consumer_ga where unit_qt=? AND type_qt = ? order by bldgno_qt");
            pstmt.setString(1, qtrUnit);
            pstmt.setString(2, qtrType);
            //System.out.println("SELECT distinct bldgno_qt from consumer_ga where unit_qt=? AND type_qt = ? and HRMSID is not null order by bldgno_qt");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("bldgno_qt"));
                so.setLabel(rs.getString("bldgno_qt"));
                //System.out.println("Bld:"+rs.getString("bldgno_qt"));
                unitAreaList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return unitAreaList;
    }

    @Override
    public List getEmpPaymentTransactions(String empId, String applicationId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List transactionList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM ifms_payment_transactions WHERE emp_id = ? AND application_id = ?");
            pstmt.setString(1, empId);
            pstmt.setInt(2, Integer.parseInt(applicationId));
            //System.out.println("SELECT distinct bldgno_qt from consumer_ga where unit_qt=? AND type_qt = ? and HRMSID is not null order by bldgno_qt");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                IfmsTransactionBean iBean = new IfmsTransactionBean();
                iBean.setBankName(rs.getString("bank_name"));
                iBean.setBankTransactionId(rs.getString("bank_transaction_id"));
                iBean.setChallanRefNo(rs.getString("challan_ref_no"));
                iBean.setPaymentMode(rs.getString("payment_mode"));
                iBean.setTransactionMsg(rs.getString("transaction_msg"));
                iBean.setTransactionTime(rs.getString("transaction_time"));
                iBean.setTransactionstatus(rs.getString("transaction_status"));
                iBean.setTransactionAmount(rs.getString("transaction_amount"));
                transactionList.add(iBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return transactionList;
    }

    @Override
    public QuarterBean getAppQuarterDetail(String empId) {
        QuarterBean qBean = new QuarterBean();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        qBean.setQuarterUnit("");
        qBean.setQuarterType("");
        qBean.setBuildingNo("");
        qBean.setAddress("");
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM consumer_ga WHERE hrmsid = ?");
            pstmt.setString(1, empId);
            System.out.println("SELECT * FROM consumer_ga WHERE hrmsid = '" + empId + "'");
            //System.out.println("SELECT distinct bldgno_qt from consumer_ga where unit_qt=? AND type_qt = ? and HRMSID is not null order by bldgno_qt");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                qBean.setQuarterUnit(rs.getString("unit_ph"));
                qBean.setQuarterType(rs.getString("type_ph"));
                qBean.setBuildingNo(rs.getString("bldgno_qt"));
                qBean.setAddress(rs.getString("address1") + ", " + rs.getString("address2") + ", " + rs.getString("address3"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return qBean;
    }

    @Override
    public QuarterBean getLedgerAmount(String empId) {
        QuarterBean qBean = new QuarterBean();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT clsbal FROM equarter.ledgernew LN INNER JOIN equarter.ocupants OC ON LN.ocu_cd = OC.ocu_cd WHERE hrms_id = ? ORDER BY serial_no DESC LIMIT 1");
            pstmt.setString(1, empId);
            //System.out.println("SELECT * FROM consumer_ga WHERE hrmsid = '"+empId+"'");
            //System.out.println("SELECT distinct bldgno_qt from consumer_ga where unit_qt=? AND type_qt = ? and HRMSID is not null order by bldgno_qt");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                qBean.setLedgerAmount(rs.getString("clsbal"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return qBean;
    }

    @Override
    public List disposedApplicationList(String empId) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List li = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("SELECT * FROM hw_ndc_applications  WHERE (pending_at = ? OR rent_officer = ?) AND ((status <> 'Pending for Verification' AND status <> 'NDC Generated' AND status <> 'Recovery Intimation Generated') OR has_final_ndc = 'Y')");
            ps.setString(1, empId);
            ps.setString(2, empId);
            rs = ps.executeQuery();
            while (rs.next()) {
                QuarterBean qBean = new QuarterBean();
                qBean.setApplicationId(rs.getInt("application_id") + "");
                qBean.setDateOfRetirement(CommonFunctions.getFormattedOutputDate3(rs.getDate("date_of_retirement")));
                qBean.setDateCreated(CommonFunctions.getFormattedOutputDate3(rs.getDate("create_date")));
                qBean.setDesignation(rs.getString("retirement_spc"));
                qBean.setEmpId(rs.getString("emp_id"));
                qBean.setFatherName(rs.getString("father_name"));
                qBean.setFullName(rs.getString("emp_name"));
                if (rs.getString("has_cleared_outstanding").equals("Y")) {
                    qBean.setHasClearedOutstanding("Yes");
                } else {
                    qBean.setHasClearedOutstanding("No");
                }
                if (rs.getString("has_quarter_now").equals("Y")) {
                    qBean.setIsGaQtr("Yes");
                } else {
                    qBean.setIsGaQtr("No");
                }
                if (rs.getString("has_occupied_ga_quarter").equals("Y")) {
                    qBean.setHasOccupied("Yes");
                } else {
                    qBean.setHasOccupied("No");
                }
                if (rs.getString("has_posted_in_ctcbbs").equals("Y")) {
                    qBean.setIsTwinCity("Yes");
                } else {
                    qBean.setIsTwinCity("No");
                }
                if (rs.getString("has_vacated").equals("Y")) {
                    qBean.setHasVacated("Yes");
                } else {
                    qBean.setHasVacated("No");
                }
                qBean.setMobile(rs.getString("mobile"));
                qBean.setApplicationStatus(rs.getString("status"));
                qBean.setIsNDCGenerated(rs.getString("is_ndc_generated"));
                qBean.setHasFinalNDC(rs.getString("has_final_ndc"));
                qBean.setHasRecoveryIntimation(rs.getString("has_recovery_intimation"));
                qBean.setAddress(rs.getString("qtr_address"));
                qBean.setQuarterUnit(rs.getString("quarter_unit"));
                qBean.setQuarterType(rs.getString("qtr_type"));
                qBean.setBuildingNo(rs.getString("building_no"));
                qBean.setOutstandingAmount(rs.getString("outstanding_amount"));
                qBean.setDateCreated(CommonFunctions.getFormattedOutputDate3(rs.getDate("create_date")));
                qBean.setNondcRemark(rs.getString("nondc_remark"));
                li.add(qBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    public void saveScrollData(ScrollMain sMain) {

        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Date initiatedDt = dateFormat.parse(startTime);

            List aList = sMain.getScrollData();
            ScrollData sData = new ScrollData();

            ChallanDtls cDetail = new ChallanDtls();
            for (int i = 0; i < aList.size(); i++) {
                sData = (ScrollData) aList.get(i);
                List cList = sData.getChallanDtls();

                cDetail = (ChallanDtls) cList.get(0);
                //Insert application
                pst = con.prepareStatement("INSERT INTO ifms_scroll_data(dept_code,dept_ref_id,challan_ref_id,total_amount"
                        + ",bank_trans_id,bank_trans_status,bank_trans_time,hoa,challan_no,challan_date,challan_amount,import_date) "
                        + "Values (?,?,?,?,?,?,?,?,?,?,?,?)");
                //pst.setInt(1, mcode);
                pst.setString(1, sMain.getDeptCode());
                pst.setString(2, sData.getDepartmentRefId());
                pst.setString(3, sData.getChallanRefId());
                pst.setString(4, sData.getTotalAmt());
                pst.setString(5, sData.getBankTransId());
                pst.setString(6, sData.getBankTransStatus());
                pst.setString(7, sData.getBankTransTime());
                pst.setString(8, cDetail.getHoa());
                pst.setString(9, cDetail.getChallanNo());
                pst.setString(10, cDetail.getChallanDt());
                pst.setString(11, cDetail.getChallanAmt());
                pst.setTimestamp(12, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getUnitAreawiseQuarterTypeRent(String unitArea) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List unitAreaList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT distinct type_qt from consumer_ga where unit_qt=? order by type_qt");
            pstmt.setString(1, unitArea);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("type_qt"));
                so.setLabel(rs.getString("type_qt"));
                unitAreaList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return unitAreaList;
    }

    @Override
    public String getNdcFileId(int applicationId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        int originalId = 0;
        int newNdcId = 0;
        try {
            con = dataSource.getConnection();
            int ndcFileId = 0;
            //Get the file ID
            ps = con.prepareStatement("SELECT MAX(ndc_file_id) AS ndc_file_id FROM hw_ndc_applications");
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                ndcFileId = rs1.getInt("ndc_file_id");
                ndcFileId = ndcFileId + 1;
            }
            ps1 = con.prepareStatement("SELECT ndc_file_id AS ndc_file_id FROM hw_ndc_applications WHERE application_id = ?");
            ps1.setInt(1, applicationId);
            rs = ps1.executeQuery();
            while (rs.next()) {
                originalId = rs.getInt("ndc_file_id");
            }
            //System.out.println("Or appID:"+applicationId+"ID:"+originalId);
            if (originalId == 0) {
                //System.out.println("If");
                ps = con.prepareStatement("UPDATE hw_ndc_applications SET ndc_file_id = ? WHERE application_id = ?");
                ps.setInt(1, ndcFileId);
                ps.setInt(2, applicationId);
                ps.executeUpdate();
                newNdcId = ndcFileId;
            }
            else
            {
                //System.out.println("else");
                newNdcId = originalId;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, ps, con);
            DataBaseFunctions.closeSqlObjects(rs, ps1, con);
        }
        return newNdcId+"";
    }
@Override
    public int modifyNDCPassword(String empId, String oldpwd, String newpwd) {
       Connection conpostgres = null;        
        PreparedStatement pst = null;        
        int modify = 0;
        int modifications = 0;
        System.out.println("Emp ID: "+empId);
        System.out.println("Old: "+oldpwd);
        System.out.println("New: "+newpwd);
        try {            
            conpostgres = this.dataSource.getConnection();            
            pattern = Pattern.compile(pwd_policy);
            matcher = pattern.matcher(newpwd);
            if (matcher.matches()) {
                pst = conpostgres.prepareStatement("UPDATE user_details SET password=? WHERE linkid=? AND password=? and usertype = 'Q'");
                pst.setString(1, newpwd.substring(32)); 
                pst.setString(2, empId);
                pst.setString(3, oldpwd.substring(32));
                modifications = pst.executeUpdate();

                if (modifications > 0) {
                    modify = 1;
                } else {
                    modify = 0;
                }                
            } else {
                modify = 2;//New Password Doesnot 
            }
        } catch (Exception e) {            
            e.printStackTrace();
        } finally {            
            DataBaseFunctions.closeSqlObjects(pst,conpostgres);
        }
        return modify;
    }    
}
