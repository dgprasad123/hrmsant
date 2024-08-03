/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.login;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.RandomPasswordGenerator;
import hrms.model.lamembers.LAMembers;
import hrms.model.login.AdminUsers;
import hrms.model.login.UserDetails;
import hrms.model.login.UserExpertise;
import hrms.model.login.Users;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;
import org.apache.commons.lang.RandomStringUtils;

/**
 *
 * @author Surendra
 */
public class LoginDAOImpl implements LoginDAO {

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

    private String pwd_policy = "((?=.*[a-zA-Z]).(?=.*[@#$%]).(?=.*[@#$%]).{6,})";
    private Pattern pattern;
    private Matcher matcher;

    @Override
    public UserDetails checkWSLogin(String username, String pwd) {
        Connection con = null;
        PreparedStatement ps2 = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        UserDetails ud = new UserDetails();

        try {
            con = this.dataSource.getConnection();
            String sql = "SELECT username, password, enable, accountnonexpired, accountnonlocked, credentialsnonexpired, user_details.userid , user_details.usertype , linkid, password, "
                    + "last_login_failed, no_login_failed, last_login, dep_code FROM user_details inner join emp_mast on user_details.linkid = emp_mast.emp_id where username=? and password=?";
            ps2 = con.prepareStatement(sql);
            ps2.setString(1, username);
            ps2.setString(2, pwd);
            rs = ps2.executeQuery();

            if (rs.next()) {
                ud.setPassword(rs.getString("password"));
                ud.setLinkid(rs.getString("linkid"));
                ud.setUsertype(rs.getString("usertype"));
                ud.setAccountnonexpired(rs.getInt("accountnonexpired"));
                ud.setAccountnonlocked(rs.getInt("accountnonlocked"));
                ud.setCredentialsnonexpired(rs.getInt("credentialsnonexpired"));
                ud.setNoofloginfailed(rs.getInt("no_login_failed"));
                ud.setLastloginfailed(rs.getTimestamp("last_login_failed"));
                ud.setLastlogin(rs.getTimestamp("last_login"));
                ud.setDepCode(rs.getString("dep_code"));
                ud.setMessage("S");
            } else {
                ud.setMessage("F");
            }

            if (!ud.getMessage().equals("F")) {
                /*If login is sucessfull then last login is saved in database.*/
                pstmt = con.prepareStatement("UPDATE user_details SET last_login = ?,no_login_failed=0 , last_login_failed=null where linkid=?");
                pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                pstmt.setString(2, ud.getLinkid());
                pstmt.executeUpdate();
            } else {
                /*If login fails. It will calculate the last failed attempt.*/
                if (ud.getLastloginfailed() == null) {
                    ud.setLastloginfailed(new Timestamp(System.currentTimeMillis()));
                }
                long diff = System.currentTimeMillis() - ud.getLastloginfailed().getTime();
                long hour = diff / (1000 * 60 * 60);
                /*If last attempt is within 24 hrs. then it will increase the failed attempt.*/
                if (hour < 24) {
                    if (ud.getNoofloginfailed() == 0) {
                        pstmt = con.prepareStatement("UPDATE user_details SET no_login_failed = ?, last_login_failed=? where linkid=?");
                        pstmt.setInt(1, 1);
                        pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                        pstmt.setString(3, ud.getLinkid());
                        pstmt.executeUpdate();
                    } else if (ud.getNoofloginfailed() > 0 && ud.getNoofloginfailed() < 4) {
                        pstmt = con.prepareStatement("UPDATE user_details SET no_login_failed = ? where linkid=?");
                        pstmt.setInt(1, ud.getNoofloginfailed() + 1);
                        pstmt.setString(2, ud.getLinkid());
                        pstmt.executeUpdate();
                    } else {
                        pstmt = con.prepareStatement("UPDATE user_details SET no_login_failed = ?, accountnonlocked = 0, last_login_failed=? where linkid=?");
                        pstmt.setInt(1, ud.getNoofloginfailed() + 1);
                        pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                        pstmt.setString(3, ud.getLinkid());
                        pstmt.executeUpdate();
                    }
                } else {
                    /*If last attempt is more than 24 hrs. then it will count as 1st attempt.*/
                    pstmt = con.prepareStatement("UPDATE user_details SET no_login_failed = ?, last_login_failed=? where linkid=?");
                    pstmt.setInt(1, 1);
                    pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                    pstmt.setString(3, ud.getLinkid());
                    pstmt.executeUpdate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps2, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return ud;
    }

    @Override
    public UserDetails checkLogin(String username, String pwd) {

        Connection con = null;

        PreparedStatement ps2 = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        UserDetails ud = new UserDetails();
        //pwd=pwd.substring(32);    
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT ENCRYPTED_PWD,force_reset,username, password, enable, accountnonexpired, accountnonlocked, credentialsnonexpired, userid , usertype , linkid, password, last_login_failed, no_login_failed, last_login "
                    + "FROM user_details where username=? and ENCRYPTED_PWD=?";
            ps2 = con.prepareStatement(sql);
            ps2.setString(1, username);
            ps2.setString(2, pwd);
            rs = ps2.executeQuery();
            if (rs.next()) {
                if (rs.getString("ENCRYPTED_PWD").equals(pwd)) {
                    ud.setPassword(rs.getString("password"));
                    ud.setLinkid(rs.getString("linkid"));
                    ud.setUsertype(rs.getString("usertype"));
                    ud.setAccountnonexpired(rs.getInt("accountnonexpired"));
                    ud.setAccountnonlocked(rs.getInt("accountnonlocked"));
                    ud.setCredentialsnonexpired(rs.getInt("credentialsnonexpired"));
                    ud.setNoofloginfailed(rs.getInt("no_login_failed"));
                    ud.setLastloginfailed(rs.getTimestamp("last_login_failed"));
                    ud.setLastlogin(rs.getTimestamp("last_login"));
                    ud.setForceReset(rs.getString("force_reset"));
                }
            } else {
                ud.setLinkid("F");
            }

            if (!ud.getLinkid().equals("F")) {
                /*If login is sucessfull then last login is saved in database.*/
                pstmt = con.prepareStatement("UPDATE user_details SET last_login = ?,no_login_failed=0 , last_login_failed=null where linkid=?");
                pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                pstmt.setString(2, ud.getLinkid());
                pstmt.executeUpdate();
            } else {
                /*If login fails. It will calculate the last failed attempt.*/
                if (ud.getLastloginfailed() == null) {
                    ud.setLastloginfailed(new Timestamp(System.currentTimeMillis()));
                }
                long diff = System.currentTimeMillis() - ud.getLastloginfailed().getTime();
                long hour = diff / (1000 * 60 * 60);
                /*If last attempt is within 24 hrs. then it will increase the failed attempt.*/
                if (hour < 24) {
                    if (ud.getNoofloginfailed() == 0) {
                        pstmt = con.prepareStatement("UPDATE user_details SET no_login_failed = ?, last_login_failed=? where linkid=?");
                        pstmt.setInt(1, 1);
                        pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                        pstmt.setString(3, ud.getLinkid());
                        pstmt.executeUpdate();
                    } else if (ud.getNoofloginfailed() > 0 && ud.getNoofloginfailed() < 4) {
                        pstmt = con.prepareStatement("UPDATE user_details SET no_login_failed = ? where linkid=?");
                        pstmt.setInt(1, ud.getNoofloginfailed() + 1);
                        pstmt.setString(2, ud.getLinkid());
                        pstmt.executeUpdate();
                    } else {
                        pstmt = con.prepareStatement("UPDATE user_details SET no_login_failed = ?, accountnonlocked = 0, last_login_failed=? where linkid=?");
                        pstmt.setInt(1, ud.getNoofloginfailed() + 1);
                        pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                        pstmt.setString(3, ud.getLinkid());
                        pstmt.executeUpdate();
                    }
                } else {
                    /*If last attempt is more than 24 hrs. then it will count as 1st attempt.*/
                    pstmt = con.prepareStatement("UPDATE user_details SET no_login_failed = ?, last_login_failed=? where linkid=?");
                    pstmt.setInt(1, 1);
                    pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                    pstmt.setString(3, ud.getLinkid());
                    pstmt.executeUpdate();
                }
                //ud = null;
            }

        } catch (Exception e) {
            ud.setMessage("Internal Server Error. Please try after sometime");
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps2, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return ud;
    }

    @Override
    public Users getEmployeeProfileInfo(String hrmsid) {
        Users emp = new Users();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String sql = "";
        try {
            con = this.repodataSource.getConnection();
            sql = "SELECT E.is_foreign_body,E.lvl,E.is_ddo, A.dep_code, G.deploy_type,a.post_nomenclature, a.is_regular, A.EMP_ID,ARRAY_TO_STRING(ARRAY[A.INITIALS, A.F_NAME, A.M_NAME, A.L_NAME], ' ') EMPNAME,A.USERID,A.PWD,A.USERTYPE,A.DOE_GOV,A.DOB,A.MOBILE,"
                    + " A.gisno,A.gistype,A.dos,A.doe_gov,A.home_town,A.religion,A.res_cat,A.gender,A.height,A.bl_grp,A.domicile,A.id_mark,A.joindate_of_goo,A.m_status,A.jointime_of_goo,"
                    + " A.toe_gov,A.bl_grp,A.bank_acc_no,A.bank_code,A.branch_code,A.domicile,A.id_mark,A.email_id, "
                    + " A.POST_GRP_TYPE,A.ACCT_TYPE,A.GPF_NO,A.DATE_OF_NINCR,A.GP,A.CUR_SALARY,A.CUR_BASIC_SALARY, "
                    + "	D.CADRE_NAME, D.CADRE_CODE,E.OFF_EN, E.OFF_CODE,E.ddo_code, B.SPC, B.SPN, B.GPC, C.POST,C.POST_CODE,E.DEPARTMENT_CODE,GD.department_name, if_profile_completed,isauthority, ddo_hrmsid, E.dist_code FROM   EMP_MAST A "
                    + "	left outer join G_CADRE D on A.CUR_CADRE_CODE=D.CADRE_CODE "
                    + "	LEFT outer join G_SPC B on A.CUR_SPC=B.SPC "
                    + " left outer join g_deploy_type G ON A.dep_code=G.deploy_code "
                    + "	left outer join g_post C on B.gpc=C.post_code "
                    + " LEFT OUTER JOIN g_department GD ON C.department_code = GD.department_code,  G_OFFICE E "
                    + "	WHERE A.CUR_OFF_CODE=E.OFF_CODE AND A.EMP_ID=? ";
            ps = con.prepareStatement(sql);

            ps.setString(1, hrmsid);

            rs = ps.executeQuery();
            if (rs.next()) {
                emp.setLoginEmpid(rs.getString("usertype"));
                emp.setHrmsEncId(CommonFunctions.encodedTxt(hrmsid));
                emp.setEmpId(hrmsid);
                emp.setFullName(rs.getString("EMPNAME"));
                emp.setDob(rs.getDate("DOB"));
                emp.setDoegov(rs.getDate("DOE_GOV"));
                emp.setMobile(rs.getString("MOBILE"));
                emp.setEmailId(rs.getString("email_id"));
                emp.setGisNo(rs.getString("gisno"));
                emp.setGisType(rs.getString("gistype"));
                emp.setDos(rs.getDate("dos"));
                emp.setJoinDateGoo(rs.getDate("joindate_of_goo"));
                emp.setCategory(rs.getString("res_cat"));
                emp.setReligion(rs.getString("religion"));
                emp.setHeight(rs.getString("height"));
                emp.setHomeTown(rs.getString("home_town"));
                emp.setDomicil(rs.getString("domicile"));
                emp.setGender(rs.getString("gender"));
                emp.setMarital(rs.getString("m_status"));
                emp.setBloodGrp(rs.getString("bl_grp"));
                emp.setToeGov(rs.getString("toe_gov"));
                emp.setJoiningTime(rs.getString("jointime_of_goo"));
                emp.setPostgrp(rs.getString("POST_GRP_TYPE"));
                emp.setAcctType(rs.getString("ACCT_TYPE"));
                emp.setGpfno(rs.getString("GPF_NO"));
                emp.setDateOfnincr(rs.getDate("DATE_OF_NINCR"));
                emp.setGradepay(rs.getInt("GP"));
                emp.setPayscale(rs.getString("CUR_SALARY"));
                emp.setCurBasic(rs.getDouble("CUR_BASIC_SALARY"));
                emp.setCadrecode(rs.getString("CADRE_CODE"));
                emp.setCadrename(rs.getString("CADRE_NAME"));
                emp.setOffname(rs.getString("OFF_EN"));
                emp.setOffcode(rs.getString("OFF_CODE"));
                emp.setOfficeLevel(rs.getString("lvl"));
                emp.setEmpIsDDO(rs.getString("is_ddo"));
                //System.out.println("emp.setOfficeLevel::"+rs.getString("lvl")+"::"+rs.getString("is_ddo"));
                emp.setDistCode(rs.getString("dist_code"));
                emp.setDdoCode(rs.getString("ddo_code"));
                emp.setCurspc(rs.getString("SPC"));

                emp.setSpn(rs.getString("SPN"));
                emp.setGpc(rs.getString("GPC"));
                if (rs.getString("is_regular") != null && !rs.getString("is_regular").equals("")) {
                    if (rs.getString("is_regular").equals("N")) {
                        emp.setPostname(rs.getString("post_nomenclature"));
                    } else {
                        emp.setPostname(rs.getString("post"));
                    }

                } else {
                    emp.setPostname("");
                }
                emp.setIsRegular(rs.getString("is_regular"));
                emp.setDeptcode(rs.getString("DEPARTMENT_CODE"));
                emp.setDepstatus(rs.getString("deploy_type"));
                emp.setDeptName(rs.getString("department_name"));
                emp.setDepcode(rs.getString("dep_code"));
                emp.setUsertype("G");
                emp.setPostCode(rs.getString("POST_CODE"));
                emp.setBankAccNo(rs.getString("bank_acc_no"));
                emp.setBankCode(rs.getString("bank_code"));
                emp.setBranchCode(rs.getString("branch_code"));
                emp.setDomicil(rs.getString("domicile"));
                emp.setIdMark(rs.getString("id_mark"));
                emp.setIfprofileCompleted(rs.getString("if_profile_completed"));
                emp.setIsauthority(rs.getString("isauthority"));
                emp.setIsForeignbodyEmp(rs.getString("is_foreign_body"));
                getAdditionalChargeData(hrmsid, emp);

                if (rs.getString("ddo_hrmsid") != null && !rs.getString("ddo_hrmsid").equals("")) {
                    if (rs.getString("ddo_hrmsid").equals(emp.getEmpId())) {
                        emp.setIsddoLogin("Y");
                    }
                }
            }

            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("SELECT id_no FROM EMP_ID_DOC WHERE EMP_ID=? AND ID_DESCRIPTION='AADHAAR'");
            ps.setString(1, hrmsid);
            rs = ps.executeQuery();
            if (rs.next()) {
                emp.setAadharno(CommonFunctions.maskCardNumber(rs.getString("id_no"), "##xx-xxxx-xx##", 12));
            }

            DataBaseFunctions.closeSqlObjects(rs, ps);
            ps = con.prepareStatement("SELECT if_visited FROM districtcoordinator_visited WHERE empid_asddo=?");
            ps.setString(1, hrmsid);
            rs = ps.executeQuery();
            if (rs.next()) {
                emp.setIfVisited(rs.getString("if_visited"));
            }

            DataBaseFunctions.closeSqlObjects(rs, ps);
            if (emp.getCurspc() != null) {

                ps = con.prepareStatement("SELECT ROLE_ID FROM G_PRIVILEGE_MAP WHERE SPC=? GROUP BY ROLE_ID"
                        + " UNION"
                        + " SELECT '05' ROLE_ID FROM ("
                        + " select AUTH_OFF, SPC from emp_join where EMP_ID=? AND IF_AD_CHARGE='Y'"
                        + " AND JOIN_ID::TEXT NOT IN (SELECT EMP_ID FROM EMP_RELIEVE WHERE EMP_ID=?)) EMP_JOIN"
                        + " INNER JOIN G_OFFICE ON EMP_JOIN.AUTH_OFF=G_OFFICE.OFF_CODE AND G_OFFICE.DDO_HRMSID=? GROUP BY ROLE_ID");
                ps.setString(1, emp.getCurspc());
                ps.setString(2, hrmsid);
                ps.setString(3, hrmsid);
                ps.setString(4, hrmsid);
                rs = ps.executeQuery();
                while (rs.next()) {
                    emp.setHasPrivilages("Y");
                    if (rs.getString("ROLE_ID").equals("01")) {
                        emp.setHasmyCadreTab("Y");
                    } else if (rs.getString("ROLE_ID").equals("02")) {
                        emp.setHasmyDeptTab("Y");
                    } else if (rs.getString("ROLE_ID").equals("03")) {
                        emp.setHasmyDistTab("Y");
                    } else if (rs.getString("ROLE_ID").equals("04")) {
                        emp.setHasmyHodTab("Y");
                    } else if (rs.getString("ROLE_ID").equals("05")) {
                        emp.setHasmyOfficeTab("Y");
                    } else if (rs.getString("ROLE_ID").equals("10")) {
                        emp.setHasparadminTab("Y");
                    } else if (rs.getString("ROLE_ID").equals("11")) {
                        emp.setHaspoliceDGTab("Y");
                    } else if (rs.getString("ROLE_ID").equals("13")) {
                        emp.setHasCommandandAuthPriv("Y");
                    } else if (rs.getString("ROLE_ID").equals("14")) {
                        emp.setHasparreviewingAuthorization("Y");
                    } else if (rs.getString("ROLE_ID").equals("15")) {
                        emp.setHasparcustodianAuthorization("Y");
                    } else if (rs.getString("ROLE_ID").equals("16")) {
                        emp.setHasparviewingAuthorization("Y");
                    } else if (rs.getString("ROLE_ID").equals("17")) {
                        emp.setHaspropertyadminAuthorization("Y");
                    } else if (rs.getString("ROLE_ID").equals("18")) {
                        emp.setHasGroupCcustodianAuthorization("Y");
                    } else if (rs.getString("ROLE_ID").equals("20")) {
                        emp.setIsNodalOfficerSiPar("Y");
                    } else if (rs.getString("ROLE_ID").equals("21")) {
                        emp.setHasOfficeWisePARCustodianAuthorization("Y");
                    }

                }
                DataBaseFunctions.closeSqlObjects(rs, ps);
            }

            /*Having Additional Charge*/
            if (emp.getDepcode() != null && emp.getDepcode().equals("02")) {
                ps = con.prepareStatement("SELECT ROLE_ID,spc FROM "
                        + "(SELECT ROLE_ID,G_PRIVILEGE_MAP.SPC FROM G_PRIVILEGE_MAP "
                        + "INNER JOIN (select SPC from emp_join where emp_id=? AND IF_AD_CHARGE='Y')emp_join ON G_PRIVILEGE_MAP.SPC = emp_join.SPC)G_PRIVILEGE_MAP "
                        + "GROUP BY ROLE_ID,spc ");
                ps.setString(1, hrmsid);
                rs = ps.executeQuery();
                while (rs.next()) {
                    emp.setHasPrivilages("Y");
                    if (rs.getString("ROLE_ID").equals("01")) {
                        emp.setHasmyCadreTab("Y");
                    } else if (rs.getString("ROLE_ID").equals("02")) {
                        emp.setHasmyDeptTab("Y");
                    } else if (rs.getString("ROLE_ID").equals("03")) {
                        emp.setHasmyDistTab("Y");
                    } else if (rs.getString("ROLE_ID").equals("04")) {
                        emp.setHasmyHodTab("Y");
                    } else if (rs.getString("ROLE_ID").equals("05")) {
                        emp.setHasmyOfficeTab("Y");
                    } else if (rs.getString("ROLE_ID").equals("10")) {
                        emp.setHasparadminTab("Y");
                    } else if (rs.getString("ROLE_ID").equals("11")) {
                        emp.setHaspoliceDGTab("Y");
                    } else if (rs.getString("ROLE_ID").equals("13")) {
                        emp.setHasCommandandAuthPriv("Y");
                    } else if (rs.getString("ROLE_ID").equals("14")) {
                        emp.setHasparreviewingAuthorization("Y");
                    } else if (rs.getString("ROLE_ID").equals("15")) {
                        emp.setHasparcustodianAuthorization("Y");
                    } else if (rs.getString("ROLE_ID").equals("16")) {
                        emp.setHasparviewingAuthorization("Y");
                    } else if (rs.getString("ROLE_ID").equals("17")) {
                        emp.setHaspropertyadminAuthorization("Y");
                    };

                }
                DataBaseFunctions.closeSqlObjects(rs, ps);
            }
            /*Having Additional Charge*/

            ps = con.prepareStatement("SELECT revisioning_auth_emp_id FROM pay_revision_option WHERE revisioning_auth_emp_id=?");
            ps.setString(1, hrmsid);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("revisioning_auth_emp_id") != null && !rs.getString("revisioning_auth_emp_id").equals("")) {
                    emp.setHasPayRevisionAuth("Y");
                }
            }

            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("SELECT checking_auth_emp_id FROM pay_revision_option WHERE checking_auth_emp_id=?");
            ps.setString(1, hrmsid);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("checking_auth_emp_id") != null && !rs.getString("checking_auth_emp_id").equals("")) {
                    emp.setHascheckingAuth("Y");
                }
            }

            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("SELECT verifying_auth_emp_id FROM pay_revision_option WHERE verifying_auth_emp_id=?");
            ps.setString(1, hrmsid);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("verifying_auth_emp_id") != null && !rs.getString("verifying_auth_emp_id").equals("")) {
                    emp.setHasverifyingAuth("Y");
                }
            }

            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("SELECT hrms_id FROM process_authorization WHERE spc=? AND process_id='14'");
            ps.setString(1, emp.getCurspc());
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("hrms_id") != null && !rs.getString("hrms_id").equals("")) {
                    emp.setHasProfileAuthorization("Y");
                }
            }

            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("SELECT hrms_id FROM process_authorization WHERE spc=? AND process_id='13' AND role_type='OPERATOR' ");
            ps.setString(1, emp.getCurspc());
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("hrms_id") != null && !rs.getString("hrms_id").equals("")) {
                    emp.setHasAERAuthorization("Y");
                }
            }

            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("SELECT hrms_id FROM process_authorization WHERE spc=? AND process_id='13' AND role_type='OPERATOR' ");
            ps.setString(1, emp.getAdditionalChargespc());
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("hrms_id") != null && !rs.getString("hrms_id").equals("")) {
                    emp.setHasAERAuthorization("Y");
                    emp.setHasAERAuthorizationForAdditionalChargeSPC("Y");
                }
            }

            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("SELECT hrms_id FROM process_authorization WHERE hrms_id=? and spc=? AND process_id='22' ");
            ps.setString(1, emp.getEmpId());
            ps.setString(2, emp.getCurspc());
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("hrms_id") != null && !rs.getString("hrms_id").equals("")) {
                    emp.setHasServiceBookValidateAuthorization("Y");
                }
            }

            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("SELECT hrms_id FROM process_authorization"
                    + " inner join aer_report_submit on process_authorization.off_code=aer_report_submit.off_code"
                    + " WHERE spc=? AND process_id='13' AND role_type='APPROVER' and is_operator_submitted='Y'");
            ps.setString(1, emp.getCurspc());
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("hrms_id") != null && !rs.getString("hrms_id").equals("")) {
                    emp.setHasAERAuthorization("Y");
                }
            }

            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("SELECT * FROM process_authorization "
                    + " inner join aer_report_submit on process_authorization.off_code=aer_report_submit.co_off_code "
                    + " WHERE spc=? AND process_id='13' AND role_type='REVIEWER' and is_approver_submitted='Y' ");
            ps.setString(1, emp.getCurspc());
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("hrms_id") != null && !rs.getString("hrms_id").equals("")) {
                    emp.setHasAerForReviewerAuthorization("Y");
                }
            }

            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("SELECT * FROM process_authorization "
                    + " inner join aer_report_submit on process_authorization.off_code=aer_report_submit.co_off_code "
                    + " WHERE spc=? AND process_id='13' AND role_type='VERIFIER' and is_reviewer_submitted='Y' ");
            ps.setString(1, emp.getCurspc());
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("hrms_id") != null && !rs.getString("hrms_id").equals("")) {
                    emp.setHasAerForReviewerAuthorization("Y");
                }
            }

            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("SELECT * FROM process_authorization "
                    + " WHERE spc=? AND process_id='13' AND (role_type='ACCEPTOR' or role_type='DREVIEWER' or role_type='DVERIFIER')  ");
            //+ " WHERE spc=? AND process_id='13' AND (role_type='ACCEPTOR' or role_type='DREVIEWER' or role_type='DVERIFIER')");
            ps.setString(1, emp.getCurspc());
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("hrms_id") != null && !rs.getString("hrms_id").equals("")) {
                    emp.setHasAerForAcceptorAuthorization("Y");
                }
            }

            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("SELECT distinct(hrms_id) FROM process_authorization"
                    + " inner join aer_report_submit on process_authorization.off_code=aer_report_submit.co_off_code"
                    + " WHERE spc=? AND process_id='13' AND role_type='VERIFIER' and is_reviewer_submitted='Y' and is_verifier_submitted='Y' and co_aer_id > 0 ");
            ps.setString(1, emp.getCurspc());
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("hrms_id") != null && !rs.getString("hrms_id").equals("")) {
                    emp.setHasPostTerminationForVerifier("Y");
                }
            }

            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("select hrms_id from process_authorization"
                    + " inner join aer_report_submit_at_ao on process_authorization.off_code=aer_report_submit_at_ao.ao_off_code"
                    + " WHERE spc=? AND process_id='13' AND role_type='ACCEPTOR'");
            ps.setString(1, emp.getCurspc());
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("hrms_id") != null && !rs.getString("hrms_id").equals("")) {
                    emp.setHasPostTerminationForAcceptor("Y");
                }
            }

            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("select coalesce(count(*), 0) as num_employees FROM emp_mast WHERE emp_id=? and (cur_off_code IN (select g.off_code from g_office g"
                    + " inner join g_district d on g.dist_code=d.dist_code"
                    + " inner join g_treasury t on g.tr_code=t.tr_code"
                    + " inner join g_off_level o on g.lvl=o.lvl_code"
                    + " inner join g_department dt on g.department_code=dt.department_code"
                    + " where (g.tr_code in ('1892','1893','1801','1891','0701','0791','1802')"
                    + " and OFF_STATUS='F'"
                    + " and online_bill_submission='Y' and if_active='Y') or (select count(*) from qmsv2.tbl_registration where hrms_id=?) > 0)) or qms_login_allowed='Y'");
            ps.setString(1, hrmsid);
            ps.setString(2, hrmsid);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("num_employees") > 0) {
                    emp.setHasQMSLink("Y");
                }
            }
            
            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("select coalesce(count(*), 0) as num_employees FROM emp_mast WHERE emp_id=? and (cur_off_code LIKE 'CTCHOM%' or cur_off_code LIKE 'CTSHOM%')");
            ps.setString(1, hrmsid);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("num_employees") > 0) {
                    emp.setHasQMSLink(null);
                    emp.setHasPQMSLink("Y");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, rs, con);
        }
        return emp;
    }

    @Override
    public AdminUsers getPoliceOfficesLoginDetails(String userid) {
        Connection con = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        AdminUsers adm = new AdminUsers();
        String sql = "";
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT USER_ID, FULL_NAME, DISTRICT_CODE, user_off_code, user_category  FROM USER_MASTER WHERE USER_ID=?";
            ps2 = con.prepareStatement(sql);
            ps2.setInt(1, Integer.parseInt(userid));
            rs = ps2.executeQuery();
            if (rs.next()) {
                adm.setEmpId(userid);
                adm.setFullName(rs.getString("FULL_NAME"));
                adm.setDistCode(rs.getString("DISTRICT_CODE"));
                adm.setRefcode(rs.getString("user_off_code"));
                adm.setUsertype(rs.getString("user_category"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps2, rs, con);
        }
        return adm;
    }

    @Override
    public AdminUsers getAdminUsersProfileInfo(String userid) {
        Connection con = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        AdminUsers adm = new AdminUsers();
        String sql = "";
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT USER_ID, FULL_NAME, DISTRICT_CODE FROM USER_MASTER WHERE USER_ID=?";
            ps2 = con.prepareStatement(sql);
            ps2.setInt(1, Integer.parseInt(userid));
            rs = ps2.executeQuery();
            if (rs.next()) {
                adm.setEmpId(userid);
                adm.setFullName(rs.getString("FULL_NAME"));
                adm.setRefcode(rs.getString("DISTRICT_CODE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps2, rs, con);
        }
        return adm;
    }

    public LAMembers getMiniSterialProfileInfo(String lmid) {
        Connection con = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        LAMembers lam = new LAMembers();
        try {
            con = this.dataSource.getConnection();
            String sql = "SELECT LMID,fullname,OFF_AS,OFF_NAME,GETMINISTERSDEPT(LMID) OFF_EN,MOBILE FROM ( "
                    + "SELECT LMID,ARRAY_TO_STRING(ARRAY[INITIALS, FNAME, MNAME,LNAME], ' ') fullname, OFF_AS,MOBILE FROM LA_MEMBERS WHERE LMID::TEXT=?) LA_MEMBERS "
                    + "INNER JOIN G_OFFICIATING ON LA_MEMBERS.OFF_AS=G_OFFICIATING.OFF_ID";
            ps2 = con.prepareStatement(sql);
            ps2.setString(1, lmid);
            rs = ps2.executeQuery();
            if (rs.next()) {
                lam.setEmpId(rs.getInt("lmid"));
                lam.setUsertype("M");
                lam.setFullName(rs.getString("fullname"));
                lam.setMobile(rs.getString("mobile"));
                lam.setDesignation(rs.getString("OFF_NAME"));
                lam.setOffName(rs.getString("OFF_EN"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lam;
    }

    @Override
    public UserDetails requestPassword(Map<String, String> params) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String mobileno = params.get("mobile");
        String dob = params.get("dob");
        String enteredcaptcha = params.get("cap1");
        String originalcaptcha = params.get("cap2");
        int msg = 0;
        String empid = "";
        String otp = "";

        UserDetails udetail = new UserDetails();
        try {
            con = dataSource.getConnection();
            if (!enteredcaptcha.equals(originalcaptcha)) {
                msg = 5;
            } else {
                if (mobileno != null && !mobileno.equals("")) {
                    pstmt = con.prepareStatement("SELECT EMP_ID,MOBILE,DOB,L_NAME,USERID FROM EMP_MAST WHERE MOBILE=?");
                    pstmt.setString(1, mobileno);
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        empid = rs.getString("EMP_ID");
                        udetail.setLinkid(empid);
                        if (rs.getDate("DOB") != null && CommonFunctions.getFormattedOutputDate1(rs.getDate("DOB")).equalsIgnoreCase(dob)) {
                            int smsperday = getSMSCountPerDay(con, mobileno);
                            getUserName(empid, mobileno);
                            if (smsperday < 3) {
                                boolean requestsms = getSMSDelayInMins(con, mobileno);
                                if (requestsms == true) {
                                    /*SMSThread smsthread = new SMSThread(empid, mobileno, "FP");
                                     Thread t1 = new Thread(smsthread);
                                     t1.start();*/
                                    msg = 1;
                                } else {
                                    msg = 6;
                                }
                            } else {
                                msg = 3;
                            }
                        } else {
                            msg = 4;
                        }
                    }
                } else {
                    msg = 7;
                }
            }
            udetail.setMessage(msg + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return udetail;
    }

    public int getSMSCountPerDay(Connection con, String mobile) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        int count = 0;
        try {
            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            startTime = dateFormat.format(cal.getTime());
            String sql = "SELECT count(*) cnt FROM sms_log WHERE sent_on::date = '" + startTime + "' AND MOBILE=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, mobile);
            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
        } catch (Exception e) {
            DataBaseFunctions.closeSqlObjects(rs, pst);
        }
        return count;
    }

    public boolean getSMSDelayInMins(Connection con, String mobile) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean requestSMS = true;
        try {
            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());
            String sql = "select (extract(epoch from '" + startTime + "' - sent_on)/60) diff from sms_log where sent_on::date = '" + startTime + "' and mobile=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, mobile);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getString("diff") != null && !rs.getString("diff").equals("")) {
                    if (rs.getDouble("diff") <= 5) {
                        requestSMS = false;
                    }
                }
            }
        } catch (Exception e) {
            DataBaseFunctions.closeSqlObjects(rs, pst);
        }
        return requestSMS;
    }

    @Override
    public Users getInstituteInfo(String linkid) {
        Connection con = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        Users us = new Users();
        try {
            con = this.dataSource.getConnection();
            String sql = "SELECT institution_code, institution_name FROM user_details"
                    + " UD INNER JOIN g_institutions GI ON UD.linkid = GI.institution_code::text"
                    + " WHERE UD.linkid = '" + linkid + "'";
            ps2 = con.prepareStatement(sql);
            rs = ps2.executeQuery();
            if (rs.next()) {
                us.setEmpId("" + rs.getInt("institution_code"));
                us.setUsertype("I");
                us.setFullName(rs.getString("institution_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return us;
    }

    @Override
    public void updateCadreStatus(String empId, String cadreStat, String subCadreStat) {

        Connection con = null;

        PreparedStatement pst = null;
        int retVal = 0;
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("UPDATE EMP_MAST SET ACS=?,ASCS=? WHERE EMP_ID=?");
            pst.setString(1, cadreStat);
            pst.setString(2, subCadreStat);
            pst.setString(3, empId);
            retVal = pst.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public AdminUsers getDeptUsersProfileInfo(String userid) {
        Connection con = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        AdminUsers adm = new AdminUsers();
        String sql = "";
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT department_name from g_department where department_code=?";
            ps2 = con.prepareStatement(sql);
            ps2.setString(1, userid);
            rs = ps2.executeQuery();
            if (rs.next()) {
                adm.setEmpId(userid);
                adm.setFullName(rs.getString("department_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return adm;
    }

    @Override
    public UserExpertise getUserInfo(String hrmsid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        UserExpertise ueObj = new UserExpertise();
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME,POST,CADRE_GRADE,DEPARTMENT_NAME,CUR_OFF_CODE,MOBILE,EMAIL_ID,OFF_EN,SUFFIX FROM EMP_MAST"
                    + " LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC"
                    + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
                    + " LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE"
                    + " LEFT OUTER JOIN G_DEPARTMENT ON G_OFFICE.DEPARTMENT_CODE=G_DEPARTMENT.DEPARTMENT_CODE WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, hrmsid);
            rs = pst.executeQuery();
            if (rs.next()) {
                ueObj.setName(rs.getString("FULL_NAME"));
                ueObj.setDesignation(rs.getString("POST"));
                ueObj.setGrade(rs.getString("CADRE_GRADE"));
                ueObj.setDeptname(rs.getString("DEPARTMENT_NAME"));
                ueObj.setPostingPlace(rs.getString("SUFFIX"));
                ueObj.setCurofficename(rs.getString("OFF_EN"));
                ueObj.setMobile(rs.getString("MOBILE"));
                ueObj.setEmailid(rs.getString("EMAIL_ID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, rs, con);
        }
        return ueObj;
    }

    @Override
    public boolean countExpertise(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean isEmpPresent = false;
        String curCadreCode = null;
        String sql = null;
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT CUR_CADRE_CODE FROM emp_mast WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                curCadreCode = rs.getString("CUR_CADRE_CODE");
                if (curCadreCode != null && !curCadreCode.equals("")) {
                    if (curCadreCode.equalsIgnoreCase("1101") || curCadreCode.equalsIgnoreCase("1103") || curCadreCode.equalsIgnoreCase("2668") || curCadreCode.equalsIgnoreCase("0723") || curCadreCode.equalsIgnoreCase("1165")) {
                        isEmpPresent = false;
                    } else {
                        isEmpPresent = true;
                    }
                } else {
                    isEmpPresent = true;
                }
            }
            if (!isEmpPresent) {
                sql = "SELECT COUNT(*) CNT FROM g_emp_expertise WHERE EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, empid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    if (rs.getInt("CNT") > 0) {
                        isEmpPresent = true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, rs, con);
        }
        return isEmpPresent;
    }

    @Override
    public String getCurrentTrainingProgram() {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String trainingIds = null;
        String sql = null;
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT training_program_code FROM g_training_programs WHERE is_published = 'Y' LIMIT 1";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                trainingIds = "" + rs.getInt("training_program_code");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, rs, con);
        }
        return trainingIds;
    }

    @Override
    public boolean getEligibility(String postCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean isEligible = false;
        String sql = null;
        String trainingIds = getCurrentTrainingProgram();
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT COUNT(*) CNT FROM g_training_postcodes WHERE training_id=" + trainingIds + " AND post_code = '" + postCode + "'";

            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("CNT") > 0) {
                    isEligible = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, rs, con);
        }
        return isEligible;
    }

    @Override
    public boolean getFeedbackEligibility(String empId) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean isEligible = false;
        String sql = null;
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT COALESCE(COUNT(*),0) CNT FROM g_training_ratings WHERE emp_id='" + empId + "'";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("CNT") > 0) {
                    isEligible = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, rs, con);
        }
        return isEligible;
    }

    @Override
    public String decryptPassword(String encrypted) {
        String dCodeStr = "";
        try {
            SecretKey key = new SecretKeySpec(Base64.getDecoder().decode("u/Gu5posvwDsXUnV5Zaq4g=="), "AES");
            AlgorithmParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode("5D9r9ZVzEYYgha93/aUK2w=="));
            if (encrypted != null && !encrypted.equals("")) {
                byte[] decodeBase64 = Base64.getDecoder().decode(encrypted);
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, key, iv);
                dCodeStr = new String(cipher.doFinal(decodeBase64), "UTF-8");
                // return new String(cipher.doFinal(decodeBase64), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //throw new RuntimeException("This should not happen in production.", e);
        }
        return dCodeStr;
        //return "";
    }

    @Override
    public List getLocalFundAuditUsers(AdminUsers adminUsers) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean flag = false;
        ArrayList audituser = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select user_master.user_id,full_name from user_details "
                    + " inner join user_master on user_details.linkid =CAST ( user_master.user_id AS Text)"
                    + " where usertype='S' and username like 'alf%' ORDER BY full_name ASC");
            rs = pst.executeQuery();
            while (rs.next()) {
                AdminUsers adminUser = new AdminUsers();
                adminUser.setEmpId(rs.getString("user_id"));
                adminUser.setFullName(rs.getString("full_name"));
                audituser.add(adminUser);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return audituser;
    }

    @Override
    public boolean ifEmpisDDO(String empId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean isEligible = false;
        String sql = null;
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT COUNT(*) CNT FROM g_office WHERE ddo_hrmsid='" + empId + "'";

            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("CNT") > 0) {
                    isEligible = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, rs, con);
        }
        return isEligible;
    }

    @Override
    public String generateOTP(String mobile) {

        String otp = "";

        try {
            otp = RandomStringUtils.randomNumeric(6);
            String msg = "";
            //SMSServices smhttp = new SMSServices(mobile, msg, "1407161668636302928");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return otp;
    }

    @Override
    public int resetForgotPassword(String linkid, String newpassword, String confirmpassword, String mobile) {

        Connection con = null;

        PreparedStatement pst = null;

        int status = 0;

        try {
            con = this.dataSource.getConnection();
            if (linkid != null && !linkid.equals("")) {
                if (newpassword != null && confirmpassword != null) {
                    if (newpassword.equals(confirmpassword)) {
                        if (newpassword.length() >= 8) {

                            //updateDBUseridPwd(con,linkid,mobile);
                            pattern = Pattern.compile(pwd_policy);
                            matcher = pattern.matcher(newpassword);
                            if (matcher.matches()) {
                                String newsalt = CommonFunctions.getNewSalt();

                                String encryptedPassword = CommonFunctions.getEncryptedPassword(newpassword, newsalt);

                                String sql = "UPDATE USER_DETAILS SET ENCRYPTED_PWD=?,SALT=?,is_encrypted='Y' WHERE LINKID=? AND USERTYPE='G'";
                                pst = con.prepareStatement(sql);
                                pst.setString(1, encryptedPassword);
                                pst.setString(2, newsalt);
                                pst.setString(3, linkid);
                                int retval = pst.executeUpdate();
                                status = retval;
                            } else {
                                status = 4;
                            }
                        } else {
                            status = 5;
                        }
                    } else {
                        status = 2;
                    }
                } else {
                    status = 3;
                }
            } else {
                status = -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
        return status;
    }

    @Override
    public String getUserName(String linkid, String mobile) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String username = "";

        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT USERNAME FROM USER_DETAILS WHERE LINKID=? AND USERTYPE='G'";
            pst = con.prepareStatement(sql);
            pst.setString(1, linkid);
            rs = pst.executeQuery();
            if (rs.next()) {
                username = rs.getString("USERNAME");
            }
            if (username == null || username.equals("")) {
                username = updateDBUseridPwd(linkid, mobile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return username;
    }

    public String updateDBUseridPwd(String empId, String mobile) {

        Connection conpsql = null;

        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;

        String verifiedUserId = "";
        try {
            conpsql = this.dataSource.getConnection();

            int noOfCAPSAlpha = 1;
            int noOfDigits = 1;
            int noOfSplChars = 0;
            int minLen = 8;
            int maxLen = 8;

            char[] pswd = RandomPasswordGenerator.generatePswd(minLen, maxLen,
                    noOfCAPSAlpha, noOfDigits, noOfSplChars);

            String newpwd = new String(pswd);

            String newUserId = "";
            String usertype = "";

            ps1 = conpsql.prepareStatement("SELECT * FROM USER_DETAILS WHERE LINKID=? and usertype <> 'Q'");
            ps1.setString(1, empId);
            rs = ps1.executeQuery();
            boolean updateEmpMast = false;
            if (rs.next()) {
                if (rs.getString("USERNAME") == null || rs.getString("USERNAME").equals("")) {
                    ps = conpsql.prepareStatement("SELECT USERID,USERTYPE,F_NAME,L_NAME FROM EMP_MAST WHERE EMP_ID=? AND MOBILE=?");
                    ps.setString(1, empId);
                    ps.setString(2, mobile);
                    rs1 = ps.executeQuery();
                    if (rs1.next()) {
                        newUserId = rs1.getString("USERID");
                        if (newUserId != null && !newUserId.equals("")) {
                            if (Character.isDigit(newUserId.charAt(0))) {
                                newUserId = rs1.getString("F_NAME").trim().toLowerCase() + "." + rs1.getString("L_NAME").trim().toLowerCase();
                                updateEmpMast = true;
                            }
                        } else {
                            newUserId = rs1.getString("F_NAME").trim().toLowerCase() + "." + rs1.getString("L_NAME").trim().toLowerCase();
                            updateEmpMast = true;
                        }
                        usertype = rs1.getString("USERTYPE");
                    }
                    verifiedUserId = verifyUserID(conpsql, newUserId);
                    updateUserId(conpsql, empId, verifiedUserId, usertype, updateEmpMast);
                }
            } else {
                //System.out.println("Inside UpdateDB else");
                ps = conpsql.prepareStatement("SELECT USERID,USERTYPE,F_NAME,L_NAME FROM EMP_MAST WHERE EMP_ID=? AND MOBILE=?");
                ps.setString(1, empId);
                ps.setString(2, mobile);
                rs1 = ps.executeQuery();
                if (rs1.next()) {
                    newUserId = rs1.getString("USERID");
                    if (newUserId != null && !newUserId.equals("")) {
                        if (Character.isDigit(newUserId.charAt(0))) {
                            newUserId = rs1.getString("F_NAME").trim().toLowerCase() + "." + rs1.getString("L_NAME").trim().toLowerCase();
                            updateEmpMast = true;
                        }
                    } else {
                        newUserId = rs1.getString("F_NAME").trim().toLowerCase() + "." + rs1.getString("L_NAME").trim().toLowerCase();
                        updateEmpMast = true;
                    }
                    usertype = rs1.getString("USERTYPE");
                }
                //System.out.println("newUserId is: "+newUserId);
                verifiedUserId = verifyUserID(conpsql, newUserId);

                insertUserId(conpsql, empId, verifiedUserId, usertype, updateEmpMast);

            }

            /*ps1 = conpsql.prepareStatement("UPDATE EMP_MAST SET PWD=?,MOBILE=? WHERE EMP_ID=?");
             ps1.setString(1, newpwd);
             ps1.setString(2, mobile);
             ps1.setString(3, empId);
             ps1.executeUpdate();

             ps1 = conpsql.prepareStatement("UPDATE USER_DETAILS SET PASSWORD=?,ACCOUNTNONLOCKED=? WHERE LINKID=?");
             ps1.setString(1, newpwd);
             ps1.setInt(2, 1);
             ps1.setString(3, empId);
             ps1.executeUpdate();*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(rs1, ps1);
        }
        return verifiedUserId;
    }

    private static String verifyUserID(Connection conpsql, String userid) {

        PreparedStatement pst = null;
        ResultSet rs = null;

        String username = "";
        String regex = "[^\\d]+";
        ArrayList numList = new ArrayList();
        int greaterInt = 0;
        try {
            regex = "[^A-Za-z. ]+";
            String[] uidArr = userid.split(regex);

            String sql = "SELECT USERNAME FROM USER_DETAILS WHERE USERNAME LIKE '" + uidArr[0] + "%'";

            pst = conpsql.prepareStatement(sql);
            rs = pst.executeQuery();
            regex = "[^\\d]+";
            String uid = "";
            while (rs.next()) {
                uid = rs.getString("USERNAME");
                String[] str = uid.split(regex);
                if (str.length > 0) {

                    numList.add(str[1]);
                }
            }
            if (numList != null && numList.size() > 0) {
                for (int i = 0; i < numList.size(); i++) {
                    int numVal = Integer.parseInt(numList.get(i) + "");
                    if (numVal > greaterInt) {
                        greaterInt = numVal;
                    }
                }
            }
            if (greaterInt > 0) {
                greaterInt = greaterInt + 1;
            }
            if (uidArr.length > 0) {
                username = uidArr[0];
            } else {
                username = userid;
            }
            if (greaterInt > 0) {
                username = username + greaterInt;
            } else if (uid != null && !uid.equals("")) {
                username = username + 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
        }
        return username;
    }

    public void insertUserId(Connection conpsql, String empId, String newuserid, String usertype, boolean updateEmpMast) {

        PreparedStatement pstInsert = null;

        try {
            //System.out.println("Inside insertUserId");
            pstInsert = conpsql.prepareStatement("INSERT INTO user_details(username, password, enable, accountnonexpired, accountnonlocked,credentialsnonexpired, usertype, linkid) VALUES(?,?,?,?,?,?,?,?)");
            pstInsert.setString(1, newuserid.toLowerCase());
            pstInsert.setString(2, "");
            pstInsert.setInt(3, 1);
            pstInsert.setInt(4, 1);
            pstInsert.setInt(5, 1);
            pstInsert.setInt(6, 1);
            pstInsert.setString(7, "G");
            pstInsert.setString(8, empId);
            pstInsert.executeUpdate();

            pstInsert = conpsql.prepareStatement("UPDATE EMP_MAST SET USERID=? WHERE EMP_ID=?");
            pstInsert.setString(1, newuserid.toLowerCase());
            pstInsert.setString(2, empId);
            pstInsert.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstInsert);
        }
    }

    public void updateUserId(Connection conpsql, String empId, String newuserid, String usertype, boolean updateEmpMast) {

        PreparedStatement pstUpdate = null;

        try {
            pstUpdate = conpsql.prepareStatement("UPDATE user_details SET username=? where linkid=?");
            pstUpdate.setString(1, newuserid.toLowerCase());
            pstUpdate.setString(2, empId);
            pstUpdate.executeUpdate();

            pstUpdate = conpsql.prepareStatement("UPDATE EMP_MAST SET USERID=? WHERE EMP_ID=?");
            pstUpdate.setString(1, newuserid.toLowerCase());
            pstUpdate.setString(2, empId);
            pstUpdate.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstUpdate);
        }
    }

    @Override
    public boolean getSMSGatewayStatus() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean secondSMS = false;
        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT * FROM HRMS_CONFIG WHERE GLOBAL_VAR_NAME='SWITCH_SECOND_SMS'";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("global_var_value") != null && rs.getString("global_var_value").equals("Y")) {
                    secondSMS = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return secondSMS;
    }

    @Override
    public String getRegisteredMobileno(String username) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String mobile = "";
        try {
            con = this.dataSource.getConnection();
            String sql = "select mobile from emp_mast\n"
                    + "inner join user_details on emp_mast.emp_id=user_details.linkid\n"
                    + "where user_details.username=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, username);
            rs = pst.executeQuery();
            if (rs.next()) {
                mobile = rs.getString("mobile");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return mobile;
    }

    @Override
    public String validateLoginOTP(String enteredloginotp, String sessionOtp) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String result = "N";
        try {
            if (enteredloginotp != null && enteredloginotp.equals(sessionOtp)) {
                result = "Y";
            } else if (enteredloginotp != null && !enteredloginotp.equals(sessionOtp)) {
                result = "N";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return result;
    }

    @Override
    public UserDetails checkOTPLogin(String userid) {

        Connection con = null;

        PreparedStatement ps2 = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        UserDetails ud = new UserDetails();
        //pwd=pwd.substring(32);    
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT ENCRYPTED_PWD,force_reset,username, password, enable, accountnonexpired, accountnonlocked, credentialsnonexpired, userid , usertype , linkid, password, last_login_failed, no_login_failed, last_login "
                    + "FROM user_details where username=?";
            ps2 = con.prepareStatement(sql);
            ps2.setString(1, userid);
            rs = ps2.executeQuery();
            if (rs.next()) {
                ud.setPassword(rs.getString("password"));
                ud.setLinkid(rs.getString("linkid"));
                ud.setUsertype(rs.getString("usertype"));
                ud.setAccountnonexpired(rs.getInt("accountnonexpired"));
                ud.setAccountnonlocked(rs.getInt("accountnonlocked"));
                ud.setCredentialsnonexpired(rs.getInt("credentialsnonexpired"));
                ud.setNoofloginfailed(rs.getInt("no_login_failed"));
                ud.setLastloginfailed(rs.getTimestamp("last_login_failed"));
                ud.setLastlogin(rs.getTimestamp("last_login"));
                ud.setForceReset(rs.getString("force_reset"));

            } else {
                ud.setLinkid("F");
            }

            if (!ud.getLinkid().equals("F")) {
                /*If login is sucessfull then last login is saved in database.*/
                pstmt = con.prepareStatement("UPDATE user_details SET last_login = ?,no_login_failed=0 , last_login_failed=null where linkid=?");
                pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                pstmt.setString(2, ud.getLinkid());
                pstmt.executeUpdate();
            } else {
                /*If login fails. It will calculate the last failed attempt.*/
                if (ud.getLastloginfailed() == null) {
                    ud.setLastloginfailed(new Timestamp(System.currentTimeMillis()));
                }
                long diff = System.currentTimeMillis() - ud.getLastloginfailed().getTime();
                long hour = diff / (1000 * 60 * 60);
                /*If last attempt is within 24 hrs. then it will increase the failed attempt.*/
                if (hour < 24) {
                    if (ud.getNoofloginfailed() == 0) {
                        pstmt = con.prepareStatement("UPDATE user_details SET no_login_failed = ?, last_login_failed=? where linkid=?");
                        pstmt.setInt(1, 1);
                        pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                        pstmt.setString(3, ud.getLinkid());
                        pstmt.executeUpdate();
                    } else if (ud.getNoofloginfailed() > 0 && ud.getNoofloginfailed() < 4) {
                        pstmt = con.prepareStatement("UPDATE user_details SET no_login_failed = ? where linkid=?");
                        pstmt.setInt(1, ud.getNoofloginfailed() + 1);
                        pstmt.setString(2, ud.getLinkid());
                        pstmt.executeUpdate();
                    } else {
                        pstmt = con.prepareStatement("UPDATE user_details SET no_login_failed = ?, accountnonlocked = 0, last_login_failed=? where linkid=?");
                        pstmt.setInt(1, ud.getNoofloginfailed() + 1);
                        pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                        pstmt.setString(3, ud.getLinkid());
                        pstmt.executeUpdate();
                    }
                } else {
                    /*If last attempt is more than 24 hrs. then it will count as 1st attempt.*/
                    pstmt = con.prepareStatement("UPDATE user_details SET no_login_failed = ?, last_login_failed=? where linkid=?");
                    pstmt.setInt(1, 1);
                    pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                    pstmt.setString(3, ud.getLinkid());
                    pstmt.executeUpdate();
                }
                //ud = null;
            }

        } catch (Exception e) {
            ud.setMessage("Internal Server Error. Please try after sometime");
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps2, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return ud;
    }

    @Override
    public String getUsernameUsingMobile(String mobile) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String username = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT USERNAME FROM USER_DETAILS INNER JOIN EMP_MAST ON USER_DETAILS.LINKID=EMP_MAST.EMP_ID WHERE MOBILE=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, mobile);
            rs = pst.executeQuery();
            if (rs.next()) {
                username = rs.getString("USERNAME");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return username;
    }

    @Override
    public boolean checkMobileno(String mobileno) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean mobileexist = false;
        try {
            con = this.dataSource.getConnection();

            String sql = "select count(*) cnt from emp_mast where mobile=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, mobileno);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    mobileexist = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return mobileexist;
    }

    @Override
    public boolean checkUsername(String username) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean usernameexist = false;
        try {
            con = this.dataSource.getConnection();

            String sql = "select count(*) cnt from user_details where username=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, username);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    usernameexist = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return usernameexist;
    }

    private void getAdditionalChargeData(String empid, Users emp) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select auth_off,spc,ddo_code,off_en from emp_join"
                    + " inner join g_office on emp_join.auth_off=g_office.off_code"
                    + " where emp_id=? and if_ad_charge='Y' order by join_date desc limit 1";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("auth_off") != null && !rs.getString("auth_off").equals("")) {
                    emp.setAdditionalChargeOffCode(rs.getString("auth_off"));
                }
                if (rs.getString("spc") != null && !rs.getString("spc").equals("")) {
                    emp.setAdditionalChargespc(rs.getString("spc"));
                }
                if (rs.getString("ddo_code") != null && !rs.getString("ddo_code").equals("")) {
                    emp.setAdditionalChargeDDOCode(rs.getString("ddo_code"));
                }
                if (rs.getString("off_en") != null && !rs.getString("off_en").equals("")) {
                    emp.setAdditionalChargeOfficename(rs.getString("off_en"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveLoginLog(String empid, String username, String loginIp, String loginType, String offCode,String spc, String usertype, String privHrmsid) {
        
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("Insert into equarter.login_log(login_emp,login_username,login_ip,login_date,login_browser,login_reason,off_code,login_usertype,priv_spc,priv_hrmsid)"
                    + "values(?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, empid);
            pst.setString(2, username);
            pst.setString(3, loginIp);
            pst.setTimestamp(4, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            pst.setString(5, null);
            pst.setString(6, loginType);
            pst.setString(7, offCode);
            pst.setString(8, usertype);
            pst.setString(9, spc);
            pst.setString(10, privHrmsid);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public boolean checkUserprivLog(String IP, String spc) {
         Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean privfound = false;
        try {
            con = this.dataSource.getConnection();

            String sql = "select count(*)cnt from equarter.login_log where login_ip=? and priv_spc=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, IP);
            pst.setString(2, spc);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    privfound = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return privfound;
    }
}
