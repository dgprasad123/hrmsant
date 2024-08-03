/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.createEmployee;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.changepassword.ChangePasswordDAO;
import hrms.model.createEmployee.CreateEmployee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author lenovo
 */
public class CreateEmployeeDAOImpl implements CreateEmployeeDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ArrayList getGpfType() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList gpfList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM g_gpf_type");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("gpf_type"));
                so.setLabel(rs.getString("gpf_type"));
                gpfList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gpfList;
    }

    public ArrayList empTitleType() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList empTitleList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM g_title");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("title"));
                so.setLabel(rs.getString("title"));
                empTitleList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empTitleList;
    }

    @Override
    public String saveEmployeeData(CreateEmployee ceBean) {
        Connection conn = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        Statement stmt = null;
        int result = 0;
        int result1 = 0;
        int result2 = 0;
        String gpfdetails = "";
        String isDuplicateGPF = "N";
        String isDuplicateAadhar = "N";
        String isDuplicateMobile = "N";
        String returnString = null;
        String depCode = "";
        Calendar cal = Calendar.getInstance();
         DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());
        try {
            conn = dataSource.getConnection();

            String acctype = ceBean.getAccountType();
            String maxCode = CommonFunctions.getEmpID(conn);
            if (acctype.equals("GPF")) {
                gpfdetails = ceBean.getGpfType() + ceBean.getGptNumber();
            } else if (acctype.equals("TPF")) {
                gpfdetails = ceBean.getTpfType() + ceBean.getGptNumber();
            } else if (acctype.equals("GIA")) {
                //gpfdetails = ceBean.getOthNumber();
                gpfdetails = "GIA00".concat(maxCode);
            } else if (acctype.equals("BGT")) {
                gpfdetails = "BGT00".concat(maxCode);
            } else {
                gpfdetails = ceBean.getGptNumber();

            }
            String empType = ceBean.getEmpType();
            if (empType.equals("N") || empType.equals("D")) {
                depCode = "02";
            } else {
                depCode = "03";
            }
            if (acctype.equals("GPF") || acctype.equals("TPF") || acctype.equals("PRAN")) {
                String selectQry = "SELECT GPF_NO FROM EMP_MAST WHERE GPF_NO=?";
                pstmt = conn.prepareStatement(selectQry);
                pstmt.setString(1, gpfdetails);

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    isDuplicateGPF = "Y";
                }
            } else {
                isDuplicateGPF = "N";
            }

            returnString = isDuplicateGPF;

            String aadharNo = ceBean.getAadharNo();

            String selectQry1 = "SELECT ID_NO FROM EMP_ID_DOC WHERE ID_DESCRIPTION='AADHAAR' AND ID_NO=?";
            pstmt1 = conn.prepareStatement(selectQry1);
            pstmt1.setString(1, aadharNo);
            rs1 = pstmt1.executeQuery();
            if (rs1.next()) {
                isDuplicateAadhar = "A";

                //pstmt1.setString(1, ceBean.getDupAadhar());
            }
            String selectQry2 = "SELECT MOBILE FROM EMP_MAST WHERE MOBILE=?";
            pstmt2 = conn.prepareStatement(selectQry2);
            pstmt2.setString(1, ceBean.getMobile());
            rs2 = pstmt2.executeQuery();
            if (rs2.next()) {
                isDuplicateMobile = "M";

            }
            String assumed = "N";
            String fname = ceBean.getFirstnmae();
            String lname = ceBean.getLname();

            
            String dob = ceBean.getDob();
            String dob4digit = dob.substring(0, 4);
            String username = fname.replaceAll("\\s", "") + "." + lname.replaceAll("\\s", "") + dob4digit;
            username = username.toLowerCase();

            String password = lname.replaceAll("\\s", "") + "." + dob4digit;
            password = password.toLowerCase();

            if (isDuplicateGPF.equals("N") && isDuplicateAadhar.equals("N") && isDuplicateMobile.equals("N")) {
                if (ceBean.getAssumed() != null && !ceBean.getAssumed().equals("")) {
                    assumed = "Y";
                }

                String insertQry = "INSERT INTO emp_mast (emp_id,gpf_no,acct_type,is_regular,initials,f_name,m_name,l_name,gender,cur_off_code,dob,doe_gov,if_gpf_assumed,post_nomenclature,mobile, cur_basic_salary,gp,dep_code, post_grp_type,pay_commission,employee_creation_date) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pstmt = conn.prepareStatement(insertQry);
                pstmt.setString(1, maxCode);
                pstmt.setString(2, gpfdetails);
                pstmt.setString(3, acctype);
                pstmt.setString(4, ceBean.getEmpType());
                pstmt.setString(5, ceBean.getTitle());
                pstmt.setString(6, ceBean.getFirstnmae().toUpperCase());
                pstmt.setString(7, ceBean.getMname());
                pstmt.setString(8, ceBean.getLname().toUpperCase());
                pstmt.setString(9, ceBean.getGender());
                pstmt.setString(10, ceBean.getOffCode());
                pstmt.setTimestamp(11, new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(ceBean.getDob()).getTime()));
                pstmt.setTimestamp(12, new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(ceBean.getDoj()).getTime()));
                pstmt.setString(13, "Y");
                pstmt.setString(14, ceBean.getPost());
                pstmt.setString(15, ceBean.getMobile());
                pstmt.setInt(16, ceBean.getBasicpay());
                pstmt.setInt(17, ceBean.getGp());
                pstmt.setString(18, depCode);
                pstmt.setString(19, ceBean.getPostGroup());
                if (ceBean.getSltPayCommission() != null && !ceBean.getSltPayCommission().equals("")) {
                    pstmt.setInt(20, Integer.parseInt(ceBean.getSltPayCommission()));
                } else {
                    pstmt.setInt(20, 0);
                }
                pstmt.setTimestamp(21, new Timestamp(System.currentTimeMillis()));


                result = pstmt.executeUpdate();

                if (result > 0) {
                    String insertReal = "INSERT INTO emp_relation (emp_id,relation,initials,f_name,m_name,l_name,sl_no,GENDER,DOB,MOBILE) "
                            + "VALUES (?,?,?,?,?,?,?,?,?,?)";
                    pstmt = conn.prepareStatement(insertReal);
                    pstmt.setString(1, maxCode);

                    if (ceBean.getRelation().equals("FATHER")) {
                        pstmt.setString(2, "FATHER");
                    } else {
                        pstmt.setString(2, "HUSBAND"); // CASE FOR HUSBAND
                    }
                    pstmt.setString(3, ceBean.getrTitle());
                    pstmt.setString(4, ceBean.getrFirstnmae().toUpperCase());
                    pstmt.setString(5, ceBean.getrMname());
                    pstmt.setString(6, ceBean.getrLname().toLowerCase());
                    pstmt.setDouble(7, 1);
                    pstmt.setString(8, ceBean.getGender());
                    if (ceBean.getDob() != null) {
                        pstmt.setTimestamp(9, new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(ceBean.getDob()).getTime()));
                    } else {
                        pstmt.setTimestamp(9, null);
                    }
                    pstmt.setString(10, ceBean.getMobile());

                    result1 = pstmt.executeUpdate();
                    if (result1 > 0) {
                        String insertAadhar = "INSERT INTO EMP_ID_DOC(EMP_ID,ID_DESCRIPTION,ID_NO,IS_VERIFIED)VALUES(?,?,?,?)";
                        pstmt = conn.prepareStatement(insertAadhar);
                        pstmt.setString(1, maxCode);
                        pstmt.setString(2, "AADHAAR");
                        pstmt.setString(3, ceBean.getAadharNo());
                        pstmt.setString(4, "Y");
                        result2 = pstmt.executeUpdate();
                    }
                    if (result2 > 0) {
                        String usersalt = CommonFunctions.getNewSalt();
                        String encryptedPassword = CommonFunctions.getEncryptedPassword(password, usersalt);
                        
                        String insertdetails = "INSERT INTO user_details (username,password,accountnonexpired,accountnonlocked,enable,credentialsnonexpired,usertype,linkid,is_encrypted,encrypted_pwd,salt) "
                                + "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
                        pstmt = conn.prepareStatement(insertdetails);
                        pstmt.setString(1, maxCode);
                        pstmt.setString(2, password);
                        pstmt.setInt(3, 1);
                        pstmt.setInt(4, 1);
                        pstmt.setInt(5, 1);
                        pstmt.setInt(6, 1);
                        pstmt.setString(7, "G");
                        pstmt.setString(8, maxCode);
                        pstmt.setString(9, "Y");
                        pstmt.setString(10, encryptedPassword);
                        pstmt.setString(11, usersalt);
                        result = pstmt.executeUpdate();
                        returnString = returnString + "|" + maxCode + "|" + password;
                    }

                }
            } else if (isDuplicateAadhar.equals("A")) {
                returnString = "A";

            } else if (isDuplicateMobile.equals("M")) {
                returnString = "M";

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(stmt);
            DataBaseFunctions.closeSqlObjects(pstmt, rs, conn);
        }
        return returnString;
    }

    @Override
    public String generateAccountNo(String acctType) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String generatedAcctNo = null;
        int newMaxAcctNo = 0;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select cast (MAX(SUBSTR(GPF_NO,4,7)) as integer)gpfNo from emp_mast where acct_type=? AND SUBSTR(GPF_NO,1,3)=?");
            ps.setString(1, acctType);
            ps.setString(2, acctType);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("gpfNo") != null && rs.getInt("gpfNo") >= 0) {
                    newMaxAcctNo = rs.getInt("gpfNo") + 1;
                } else {
                    newMaxAcctNo = 1000001;
                }
                if (acctType != null && !acctType.equals("")) {

                    if (acctType.equals("GIA")) {
                        generatedAcctNo = "GIA".concat(Integer.toString(newMaxAcctNo));
                    } else if (acctType.equals("BGT")) {

                        if (newMaxAcctNo > 0) {
                            generatedAcctNo = "BGT".concat(Integer.toString(newMaxAcctNo));
                        } else {
                            generatedAcctNo = "BGT5000001";
                        }

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return generatedAcctNo;
    }

}
