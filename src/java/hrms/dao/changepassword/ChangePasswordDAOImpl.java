/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.changepassword;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Surendra
 */
public class ChangePasswordDAOImpl implements ChangePasswordDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    private String pwd_policy = "((?=.*[a-zA-Z]).(?=.*[@#$%]).(?=.*[@#$%]).{6,})";
    private Pattern pattern;
    private Matcher matcher;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int modifyUserPassword(String empId, String usertype, String oldpwd, String newpwd, String loginusername) throws SQLException {
        Connection conpostgres = null;
        PreparedStatement pst = null;
        int modify = 0;
        int modifications = 0;
        
        try {
            conpostgres = this.dataSource.getConnection();
            pattern = Pattern.compile(pwd_policy);
            matcher = pattern.matcher(newpwd);
            if (matcher.matches()) {
                if (validateOldPassword(empId, oldpwd)) {

                    pst = conpostgres.prepareStatement("UPDATE user_details SET ENCRYPTED_PWD=?,SALT=?,force_reset=0 WHERE linkid=?");
                    
                    /*pst.setString(1, newpwd.substring(32)); 
                     pst.setString(2, empId);
                     pst.setString(3, oldpwd.substring(32));
                     modifications = pst.executeUpdate();*/
                    
                    String newsalt = CommonFunctions.getNewSalt();
                    String encryptedNewPassword = CommonFunctions.getEncryptedPassword(newpwd.substring(32), newsalt);
                    String printqry = "UPDATE user_details SET ENCRYPTED_PWD='"+encryptedNewPassword+"',SALT='"+newsalt+"',force_reset=0 WHERE linkid='"+empId+"'";
                    System.out.println("Change password printqry"+printqry);
                    pst.setString(1, encryptedNewPassword);
                    pst.setString(2, newsalt);
                    pst.setString(3, empId);
                    modifications = pst.executeUpdate();

                    if (modifications > 0) {
                        modify = 1;
                    } else {
                        modify = 0;
                    }
                }
            } else {
                modify = 2;//New Password Doesnot 
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, conpostgres);
        }
        return modify;
    }

    @Override
    public String getUserSalt(String username) throws Exception {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String usersalt = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select salt from user_details where username=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, username);
            rs = pst.executeQuery();
            if (rs.next()) {
                usersalt = rs.getString("salt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return usersalt;
    }

    private boolean validateOldPassword(String linkid, String oldpassword) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean validateflag = false;

        try {
            con = this.dataSource.getConnection();

            String sql = "select encrypted_pwd,salt from user_details where linkid=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, linkid);
            rs = pst.executeQuery();
            if (rs.next()) {
                String usersalt = rs.getString("salt");
                String encryptedOldPassword = rs.getString("encrypted_pwd");
                
                String enteredEncryptedOldPassword = CommonFunctions.getEncryptedPassword(oldpassword.substring(32), usersalt);

                if (encryptedOldPassword.equals(enteredEncryptedOldPassword)) {
                    validateflag = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return validateflag;
    }
}
