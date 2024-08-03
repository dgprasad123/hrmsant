package hrms.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

public class SMSThread1 implements Runnable {

    private String empid = null;
    private String mobile = null;
    private String msgType = null;

    boolean running = true;

    public SMSThread1(String empId, String mobile, String type) {
        this.empid = empId;
        this.mobile = mobile;
        this.msgType = type;
    }

    @Override
    public void run() {
        
        Connection conpsql = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String deliverymsg = "";
        String msg = "";

        
        try {
           

            Class.forName("org.postgresql.Driver");
            conpsql = DriverManager.getConnection("jdbc:postgresql://172.16.1.14:6432/hrmis", "hrmis2", "cmgi");

            if (msgType.equals("L")) {
                msg = "LEAVE PENDING";
                SMSHttpPostClient smhttp = new SMSHttpPostClient(mobile, msg);
                deliverymsg = smhttp.send_sms();
                insertSMSData(conpsql, empid, msg, mobile, deliverymsg, "LEAVE_PENDING");
            }else if (msgType.equals("S")) {
                msg = "LEAVE IS SANCTIONED";
                SMSHttpPostClient smhttp = new SMSHttpPostClient(mobile, msg);
                deliverymsg = smhttp.send_sms();
                insertSMSData(conpsql, empid, msg, mobile, deliverymsg, "LEAVE_SANCTIONED");
            } else if (msgType.equals("FP")) {
                //ctx = ConnectionUtil.getDefaultDirCtx(ipaddress, port, userName, pwd);
                
               

                msg = getMsgforSMS(conpsql, empid);

                SMSHttpPostClient smhttp = new SMSHttpPostClient(mobile, msg);
                deliverymsg = smhttp.send_sms();

                insertSMSData(conpsql, empid, msg, mobile, deliverymsg, "USERID/PASSWORD/FROM_THREAD");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            
            DataBaseFunctions.closeSqlObjects(conpsql);
        }
    }

    public void updateoidandDB(DirContext ctx, Connection conora, Connection conpsql, String empId, String mobile) {

        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        

        ResultSet rs = null;
        ResultSet rs1 = null;
        try {
            String userDN = "cn=" + empId + ",cn=Users,dc=hrmsorissa,dc=gov,dc=in";
            //String newpwd = RandomStringUtils.randomAlphanumeric(5).toLowerCase() + empId;

            //newpwd = newpwd.replaceAll("[^a-zA-Z0-9]+", "");
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

            ps1 = conpsql.prepareStatement("SELECT * FROM USER_DETAILS WHERE LINKID=?");
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
                                newUserId = rs1.getString("F_NAME").trim() + "." + rs1.getString("L_NAME").trim();
                                updateEmpMast = true;
                            }
                        } else {
                            newUserId = rs1.getString("F_NAME").trim() + "." + rs1.getString("L_NAME").trim();
                            updateEmpMast = true;
                        }
                        usertype = rs1.getString("USERTYPE");
                    }
                    String verifiedUserId = verifyUserID(conpsql, newUserId);
                    updateUserId(conora, conpsql, empId, verifiedUserId, ctx, userDN, usertype,updateEmpMast);
                }
            } else {
                ps = conpsql.prepareStatement("SELECT USERID,USERTYPE,F_NAME,L_NAME FROM EMP_MAST WHERE EMP_ID=? AND MOBILE=?");
                ps.setString(1, empId);
                ps.setString(2, mobile);
                rs1 = ps.executeQuery();
                if (rs1.next()) {
                    newUserId = rs1.getString("USERID");
                    if (newUserId != null && !newUserId.equals("")) {
                        if (Character.isDigit(newUserId.charAt(0))) {
                            newUserId = rs1.getString("F_NAME").trim() + "." + rs1.getString("L_NAME").trim();
                            updateEmpMast = true;
                        }
                    } else {
                        newUserId = rs1.getString("F_NAME").trim() + "." + rs1.getString("L_NAME").trim();
                        updateEmpMast = true;
                    }
                    usertype = rs1.getString("USERTYPE");
                }
                
                String verifiedUserId = verifyUserID(conpsql, newUserId);

                insertUserId(conora, conpsql, empId, verifiedUserId, ctx, userDN, usertype,updateEmpMast);

            }

            ps1 = conpsql.prepareStatement("UPDATE EMP_MAST SET PWD=?,MOBILE=? WHERE EMP_ID=?");
            ps1.setString(1, newpwd);
            ps1.setString(2, mobile);
            ps1.setString(3, empId);
            ps1.executeUpdate();
            

            ps1 = conpsql.prepareStatement("UPDATE USER_DETAILS SET PASSWORD=? WHERE LINKID=?");
            ps1.setString(1, newpwd);
            ps1.setString(2, empId);
            ps1.executeUpdate();
            

            ps = conora.prepareStatement("UPDATE EMP_MAST SET PWD=? WHERE EMP_ID=?");
            ps.setString(1, newpwd);
            ps.setString(2, empId);
            ps.executeUpdate();
            

            //modify(ctx, userDN, newpwd);
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void modify(DirContext ctx, String User, String newPassword) throws Exception {
        ModificationItem[] mods = new ModificationItem[1];
        
        
        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userpassword", newPassword));
        ctx.modifyAttributes(User, mods);
    }

    public static void modifyUserId(DirContext ctx, String User, String newUid) throws Exception {
        ModificationItem[] mods = new ModificationItem[1];
        
        
        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("uid", newUid));
        ctx.modifyAttributes(User, mods);
    }

    public static String getMsgforSMS(Connection conpsql, String empid) {

        PreparedStatement pst = null;
        ResultSet rs = null;

        String msg = "";
        try {
            String eol = System.getProperty("line.separator");
            String sql = "SELECT USERNAME,PASSWORD FROM USER_DETAILS WHERE LINKID=?";
            pst = conpsql.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("USERNAME") != null && !rs.getString("USERNAME").equals("")) {
                    msg = "User Id: " + rs.getString("USERNAME") + eol + "Password: " + rs.getString("PASSWORD");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
        }
        return msg;
    }

    public void insertSMSData(Connection conpsql, String empid, String msg, String mobile, String deliverymsg, String msgtype) {

        PreparedStatement pst = null;

        String startTime = "";
        try {
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());

            pst = conpsql.prepareStatement("INSERT INTO SMS_LOG(MSG_ID,EMP_ID,MESSAGE_TEXT,MESSAGE_TYPE,MOBILE,SENT_ON,STATUS) VALUES(?,?,?,?,?,?,?)");
            pst.setInt(1, CommonFunctions.getMaxCode(conpsql, "SMS_LOG", "MSG_ID"));
            pst.setString(2, empid);
            pst.setString(3, msg);
            pst.setString(4, msgtype);
            pst.setString(5, mobile);
            pst.setTimestamp(6, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(7, deliverymsg);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
        }
    }

    private static String verifyUserID(Connection conpsql, String userid) {

        PreparedStatement pst = null;
        ResultSet rs = null;

        String username = "";
        String regex = "[^\\d]+";
        ArrayList numList = new ArrayList();
        int greaterInt = 0;
        try {
            regex = "[^A-Za-z.]+";
            String[] uidArr = userid.split(regex);

            String sql = "SELECT USERNAME FROM USER_DETAILS WHERE USERNAME LIKE '" + uidArr[0] + "%'";
            
            pst = conpsql.prepareStatement(sql);
            rs = pst.executeQuery();
            regex = "[^\\d]+";
            while (rs.next()) {
                String uid = rs.getString("USERNAME");
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
            

            //regex = "[^A-Za-z. ]+";
            //uidArr = userid.split(regex);
            
            if (uidArr.length > 0) {
                username = uidArr[0];
            } else {
                username = userid;
            }
            if (greaterInt > 0) {
                username = username + greaterInt;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs,pst);
        }
        return username;
    }

    public void insertUserId(Connection conora, Connection conpsql, String empId, String newuserid, DirContext ctx, String userDN, String usertype,boolean updateEmpMast) {

        PreparedStatement pstInsert = null;

        try {
            pstInsert = conpsql.prepareStatement("INSERT INTO user_details(username, password, enable, accountnonexpired, accountnonlocked,credentialsnonexpired, usertype, linkid) VALUES(?,?,?,?,?,?,?,?)");
            pstInsert.setString(1, newuserid.toLowerCase());
            pstInsert.setString(2, "");
            pstInsert.setInt(3, 1);
            pstInsert.setInt(4, 1);
            pstInsert.setInt(5, 1);
            pstInsert.setInt(6, 1);
            pstInsert.setString(7, usertype);
            pstInsert.setString(8, empId);
            pstInsert.executeUpdate();
            
            if(updateEmpMast == true){
                pstInsert = conpsql.prepareStatement("UPDATE EMP_MAST SET USERID=? WHERE EMP_ID=?");
                pstInsert.setString(1, newuserid.toLowerCase());
                pstInsert.setString(2, empId);
                pstInsert.executeUpdate();

                pstInsert = conora.prepareStatement("UPDATE EMP_MAST SET USERID=? WHERE EMP_ID=?");
                pstInsert.setString(1, newuserid.toLowerCase());
                pstInsert.setString(2, empId);
                pstInsert.executeUpdate();
                
                //modifyUserId(ctx, userDN, newuserid);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUserId(Connection conora, Connection conpsql, String empId, String newuserid, DirContext ctx, String userDN, String usertype,boolean updateEmpMast) {

        PreparedStatement pstUpdate = null;
        
        try {
            pstUpdate = conpsql.prepareStatement("UPDATE user_details SET username=? where linkid=?");
            pstUpdate.setString(1, newuserid.toLowerCase());
            pstUpdate.setString(2, empId);
            pstUpdate.executeUpdate();
            
            if(updateEmpMast == true){
                pstUpdate = conpsql.prepareStatement("UPDATE EMP_MAST SET USERID=? WHERE EMP_ID=?");
                pstUpdate.setString(1, newuserid.toLowerCase());
                pstUpdate.setString(2, empId);
                pstUpdate.executeUpdate();

                pstUpdate = conora.prepareStatement("UPDATE EMP_MAST SET USERID=? WHERE EMP_ID=?");
                pstUpdate.setString(1, newuserid.toLowerCase());
                pstUpdate.setString(2, empId);
                pstUpdate.executeUpdate();

                //modifyUserId(ctx, userDN, newuserid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
