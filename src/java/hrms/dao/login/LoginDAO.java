/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.login;

import hrms.model.lamembers.LAMembers;
import hrms.model.login.AdminUsers;
import hrms.model.login.UserDetails;
import hrms.model.login.UserExpertise;
import hrms.model.login.Users;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Surendra
 */
public interface LoginDAO {

    public Users getEmployeeProfileInfo(String hrmsid);

    public LAMembers getMiniSterialProfileInfo(String lmid);

    public UserDetails checkLogin(String userid, String pwd);

    public UserDetails checkWSLogin(String userid, String pwd);

    public AdminUsers getPoliceOfficesLoginDetails(String userid);

    public AdminUsers getAdminUsersProfileInfo(String userid);

    public UserDetails requestPassword(Map<String, String> params);

    public Users getInstituteInfo(String hrmsId);

    public void updateCadreStatus(String empId, String cadreStat, String subCadreStat);

    public AdminUsers getDeptUsersProfileInfo(String userid);

    public UserExpertise getUserInfo(String hrmsid);

    public boolean countExpertise(String empid);

    public boolean getEligibility(String postCode);

    public String getCurrentTrainingProgram();

    public boolean getFeedbackEligibility(String empId);

    public String decryptPassword(String encrypted);

    public List getLocalFundAuditUsers(AdminUsers adminUsers);

    public boolean ifEmpisDDO(String empId);

    public String generateOTP(String mobile);

    public int resetForgotPassword(String linkid, String newpassword, String confirmpassword, String mobile);

    public String getUserName(String linkid, String mobile);

    public boolean getSMSGatewayStatus();

    public String getRegisteredMobileno(String username);

    public String validateLoginOTP(String mobOtp, String sessionOtp);

    public UserDetails checkOTPLogin(String userid);

    public String getUsernameUsingMobile(String mobile);

    public boolean checkMobileno(String mobile);

    public boolean checkUsername(String username);

    public void saveLoginLog(String empid, String username, String loginIp, String loginType, String offCode, String spc, String usertype, String privHrmsid);

    public boolean checkUserprivLog(String IP, String spc);
    

}
