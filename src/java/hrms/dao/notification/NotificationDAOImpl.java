/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.notification;

import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.model.notification.NotificationBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author Surendra
 */
public class NotificationDAOImpl implements NotificationDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    protected MaxNotificationIdDAO maxnotiidDao;
    // protected ServiceBookLanguageDAO sbDAO;

    public MaxNotificationIdDAO getMaxnotiidDao() {
        return maxnotiidDao;
    }

    public void setMaxnotiidDao(MaxNotificationIdDAO maxnotiidDao) {
        this.maxnotiidDao = maxnotiidDao;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
//    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
//        this.sbDAO = sbDAO;
//    }

    @Override
    public int insertNotificationData(NotificationBean nfb) {

        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("INSERT INTO EMP_NOTIFICATION(NOT_TYPE,EMP_ID,DOE,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,NOTE,IF_ASSUMED,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH,ACS,ASCS,organization_type,organization_type_posting,entry_type) "
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            System.out.println("DEPT_CODE is: " + nfb.getSancDeptCode());
            System.out.println("ENT_DEPT is: " + nfb.getEntryDeptCode());
            System.out.println("nfb.getNottype() is: " + nfb.getNottype());
            pst.setString(1, nfb.getNottype());
            pst.setString(2, nfb.getEmpId());
            if (nfb.getDateofEntry() != null) {
                pst.setTimestamp(3, new Timestamp(nfb.getDateofEntry().getTime()));
            } else {
                pst.setTimestamp(3, null);
            }
            pst.setString(4, nfb.getOrdno());
            if (nfb.getOrdDate() != null) {
                pst.setTimestamp(5, new Timestamp(nfb.getOrdDate().getTime()));
            } else {
                pst.setTimestamp(5, null);
            }
            pst.setString(6, nfb.getSancDeptCode());
            pst.setString(7, nfb.getSancOffCode());
            pst.setString(8, nfb.getSancAuthCode());
            pst.setString(9, nfb.getNote());
            pst.setString(10, null);

            if (nfb.getIfVisible() != null && !nfb.getIfVisible().trim().equals("")) {
                pst.setString(11, nfb.getIfVisible());
            } else {
                pst.setString(11, "Y");
            }
            pst.setString(12, nfb.getEntryDeptCode());
            pst.setString(13, nfb.getEntryOffCode());
            pst.setString(14, nfb.getEntryAuthCode());
            pst.setString(15, nfb.getCadreStatus());
            pst.setString(16, nfb.getSubCadreStatus());
            if (nfb.getRadpostingauthtype() != null && !nfb.getRadpostingauthtype().equals("")) {
                pst.setString(17, nfb.getRadpostingauthtype());
            } else {
                pst.setString(17, "GOO");
            }
            if (nfb.getRadntfnauthtype() != null && !nfb.getRadntfnauthtype().equals("")) {
                pst.setString(18, nfb.getRadntfnauthtype());
            } else {
                pst.setString(18, "GOO");
            }
            if (nfb.getEntryType() != null && !nfb.getEntryType().equals("")) {
                pst.setString(19, nfb.getEntryType());
            } else {
                pst.setString(19, null);
            }
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int notId = rs.getInt("NOT_ID");
            nfb.setNotid(notId);

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE emp_notification SET MODIFIED_BY=?,MODIFIED_ON=?,query_string=? where NOT_ID=?");
            pst.setString(1, nfb.getLoginuserid());
            pst.setTimestamp(2, new Timestamp(new Date().getTime()));
            pst.setString(3, sqlString);
            pst.setInt(4, notId);
            pst.executeUpdate();
//            /*
//                * Updating the Service Book Language
//             */
//            String sbLang = sbDAO.getMiscllaneousString(nfb, notId, nfb.getNottype());
//            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =? WHERE NOT_ID=? ");
//            pst.setString(1, sbLang);
//            pst.setInt(2, notId);
//            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return nfb.getNotid();
    }

    @Override
    public int modifyNotificationData(NotificationBean nfb) {

        Connection con = null;
        PreparedStatement pst = null;
        int returnUpdate = 0;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("UPDATE emp_notification set ORDNO=? ,ORDDT=?, DEPT_CODE=?, OFF_CODE=?, AUTH=?, NOTE=?, IF_ASSUMED=?, IF_VISIBLE=?, ENT_DEPT=?, ENT_OFF=?, ENT_AUTH=?, ACS=?, ASCS=?, organization_type=?,organization_type_posting=? WHERE NOT_ID=?");
            pst.setString(1, nfb.getOrdno());
            if (nfb.getOrdDate() != null) {
                pst.setDate(2, new java.sql.Date(nfb.getOrdDate().getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            pst.setString(3, nfb.getSancDeptCode());
            pst.setString(4, nfb.getSancOffCode());
            pst.setString(5, nfb.getSancAuthCode());
            pst.setString(6, nfb.getNote());

            pst.setString(7, "N");
            if (nfb.getIfVisible() != null && !nfb.getIfVisible().trim().equals("")) {
                pst.setString(8, nfb.getIfVisible());
            } else {
                pst.setString(8, "Y");
            }
            pst.setString(9, nfb.getEntryDeptCode());
            pst.setString(10, nfb.getEntryOffCode());
            pst.setString(11, nfb.getEntryAuthCode());
            pst.setString(12, nfb.getCadreStatus());
            pst.setString(13, nfb.getSubCadreStatus());
            if (nfb.getRadpostingauthtype() != null && !nfb.getRadpostingauthtype().equals("")) {
                pst.setString(14, nfb.getRadpostingauthtype());
            } else {
                pst.setString(14, "GOO");
            }
            if (nfb.getRadntfnauthtype() != null && !nfb.getRadntfnauthtype().equals("")) {
                pst.setString(15, nfb.getRadntfnauthtype());
            } else {
                pst.setString(15, "GOO");
            }
            pst.setInt(16, nfb.getNotid());
            returnUpdate = pst.executeUpdate();

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE emp_notification SET MODIFIED_BY=?,MODIFIED_ON=?,query_string=? where NOT_ID=?");
            pst.setString(1, nfb.getLoginuserid());
            pst.setTimestamp(2, new Timestamp(new Date().getTime()));
            pst.setString(3, sqlString);
            pst.setInt(4, nfb.getNotid());
            pst.executeUpdate();

//            /*
//                * Updating the Service Book Language
//             */
//            String sbLang = sbDAO.getMiscllaneousString(nfb, nfb.getNotid(), nfb.getNottype());
//            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =? WHERE NOT_ID=? ");
//            pst.setString(1, sbLang);
//            pst.setInt(2, nfb.getNotid());
//            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return returnUpdate;
    }

    @Override
    public NotificationBean dispalyNotificationData(int notid, String nottype) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
        NotificationBean nfb = new NotificationBean();
        try {
            sql = "SELECT NOT_ID,NOT_TYPE,EMP_ID,DOE,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,NOTE,TOE"
                    + ",IF_ASSUMED,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH,ACS"
                    + ",ASCS,organization_type FROM EMP_NOTIFICATION WHERE NOT_ID=? AND NOT_TYPE=?";
            con = this.dataSource.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, notid);
            ps.setString(2, nottype);
            rs = ps.executeQuery();
            if (rs.next()) {
                nfb.setNotid(notid);
                nfb.setDateofEntry(rs.getDate("DOE"));
                nfb.setDeptCode(rs.getString("DEPT_CODE"));
                nfb.setEmpId(rs.getString("EMP_ID"));
                nfb.setEntryAuthCode(rs.getString("ENT_AUTH"));
                nfb.setEntryDeptCode(rs.getString("ENT_DEPT"));
                nfb.setEntryOffCode(rs.getString("ENT_OFF"));
                nfb.setIfAssumed(rs.getString("IF_ASSUMED"));
                nfb.setIfVisible(rs.getString("IF_VISIBLE"));
                nfb.setNote(rs.getString("NOTE"));
                nfb.setNottype(rs.getString("NOT_TYPE"));
                nfb.setOffCode(rs.getString("OFF_CODE"));
                nfb.setOrdDate(rs.getDate("ORDDT"));
                nfb.setOrdno(rs.getString("ORDNO"));
                nfb.setSancAuthCode(rs.getString("AUTH"));
                nfb.setSancDeptCode(rs.getString("DEPT_CODE"));
                nfb.setSancOffCode(rs.getString("OFF_CODE"));
                nfb.setRadpostingauthtype(rs.getString("organization_type"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return nfb;
    }

    @Override
    public int deleteNotificationData(int notid, String nottype) {

        Connection con = null;
        PreparedStatement ps = null;
        int returnsuccess = 0;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("DELETE FROM EMP_NOTIFICATION WHERE NOT_ID=? AND NOT_TYPE=?");
            ps.setInt(1, notid);
            ps.setString(2, nottype);
            returnsuccess = ps.executeUpdate();
            System.out.println("Deleted Notify");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return returnsuccess;
    }

    @Override
    public String saveServiceBookLanguage(String sbdescription, int notid, String nottype) {

        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        //String sbdescription = "";

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=? WHERE NOT_ID=? AND NOT_TYPE=?");
            ps.setString(1, sbdescription);
            ps.setInt(2, notid);
            ps.setString(3, nottype);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return sbdescription;
    }

    @Override
    public void updateSupersedeNotificationData(int notid, int linkidnotid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "update emp_notification set iscanceled='Y',link_id=? where not_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, linkidnotid);
            pst.setInt(2, notid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateCancellationNotificationData(int notid, int linkidnotid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "update emp_notification set iscanceled='Y',link_id=? where not_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, linkidnotid);
            pst.setInt(2, notid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public int insertNotificationDataSBCorrection(NotificationBean nfb) {

        Connection con = null;

        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("INSERT INTO EMP_NOTIFICATION_LOG(NOT_TYPE,EMP_ID,DOE,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,NOTE,IF_ASSUMED,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH,ACS,ASCS,organization_type,organization_type_posting,entry_type,NOT_ID,REF_CORRECTION_ID) "
                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, nfb.getNottype());
            pst.setString(2, nfb.getEmpId());
            if (nfb.getDateofEntry() != null) {
                pst.setTimestamp(3, new Timestamp(nfb.getDateofEntry().getTime()));
            } else {
                pst.setTimestamp(3, null);
            }
            pst.setString(4, nfb.getOrdno());
            if (nfb.getOrdDate() != null) {
                pst.setTimestamp(5, new Timestamp(nfb.getOrdDate().getTime()));
            } else {
                pst.setTimestamp(5, null);
            }
            pst.setString(6, nfb.getSancDeptCode());
            pst.setString(7, nfb.getSancOffCode());
            pst.setString(8, nfb.getSancAuthCode());
            pst.setString(9, nfb.getNote());
            pst.setString(10, null);
            if (nfb.getIfVisible() != null && !nfb.getIfVisible().trim().equals("")) {
                pst.setString(11, nfb.getIfVisible());
            } else {
                pst.setString(11, "Y");
            }
            pst.setString(12, nfb.getEntryDeptCode());
            pst.setString(13, nfb.getEntryOffCode());
            pst.setString(14, nfb.getEntryAuthCode());
            pst.setString(15, nfb.getCadreStatus());
            pst.setString(16, nfb.getSubCadreStatus());
            if (nfb.getRadpostingauthtype() != null && !nfb.getRadpostingauthtype().equals("")) {
                pst.setString(17, nfb.getRadpostingauthtype());
            } else {
                pst.setString(17, "GOO");
            }
            if (nfb.getRadntfnauthtype() != null && !nfb.getRadntfnauthtype().equals("")) {
                pst.setString(18, nfb.getRadntfnauthtype());
            } else {
                pst.setString(18, "GOO");
            }
            if (nfb.getEntryType() != null && !nfb.getEntryType().equals("")) {
                pst.setString(19, nfb.getEntryType());
            } else {
                pst.setString(19, null);
            }
            pst.setInt(20, nfb.getNotid());
            pst.setInt(21, nfb.getRefcorrectionid());
            pst.executeUpdate();

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE emp_notification_log SET MODIFIED_BY=?,MODIFIED_ON=?,query_string=? where NOT_ID=?");
            pst.setString(1, nfb.getLoginuserid());
            pst.setTimestamp(2, new Timestamp(new Date().getTime()));
            pst.setString(3, sqlString);
            pst.setInt(4, nfb.getNotid());
            pst.executeUpdate();//            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return nfb.getNotid();
    }

    @Override
    public int modifyNotificationDataSBCorrection(NotificationBean nfb) {

        Connection con = null;
        PreparedStatement pst = null;
        int returnUpdate = 0;
        try {
            con = this.dataSource.getConnection();
            
            pst = con.prepareStatement("UPDATE emp_notification_log set ORDNO=? ,ORDDT=?, DEPT_CODE=?, OFF_CODE=?, AUTH=?, NOTE=?, IF_ASSUMED=?, IF_VISIBLE=?, ENT_DEPT=?, ENT_OFF=?, ENT_AUTH=?, ACS=?, ASCS=?, organization_type=?,organization_type_posting=? WHERE NOT_ID=?");
            pst.setString(1, nfb.getOrdno());
            if (nfb.getOrdDate() != null) {
                pst.setDate(2, new java.sql.Date(nfb.getOrdDate().getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            pst.setString(3, nfb.getSancDeptCode());
            pst.setString(4, nfb.getSancOffCode());
            pst.setString(5, nfb.getSancAuthCode());
            pst.setString(6, nfb.getNote());
            pst.setString(7, "N");
            if (nfb.getIfVisible() != null && !nfb.getIfVisible().trim().equals("")) {
                pst.setString(8, nfb.getIfVisible());
            } else {
                pst.setString(8, "Y");
            }
            pst.setString(9, nfb.getEntryDeptCode());
            pst.setString(10, nfb.getEntryOffCode());
            pst.setString(11, nfb.getEntryAuthCode());
            pst.setString(12, nfb.getCadreStatus());
            pst.setString(13, nfb.getSubCadreStatus());
            if (nfb.getRadpostingauthtype() != null && !nfb.getRadpostingauthtype().equals("")) {
                pst.setString(14, nfb.getRadpostingauthtype());
            } else {
                pst.setString(14, "GOO");
            }
            if (nfb.getRadntfnauthtype() != null && !nfb.getRadntfnauthtype().equals("")) {
                pst.setString(15, nfb.getRadntfnauthtype());
            } else {
                pst.setString(15, "GOO");
            }
            pst.setInt(16, nfb.getNotid());
            returnUpdate = pst.executeUpdate();

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE emp_notification_log SET MODIFIED_BY=?,MODIFIED_ON=?,query_string=? where NOT_ID=?");
            pst.setString(1, nfb.getLoginuserid());
            pst.setTimestamp(2, new Timestamp(new Date().getTime()));
            pst.setString(3, sqlString);
            pst.setInt(4, nfb.getNotid());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return returnUpdate;
    }

    @Override
    public int deleteNotificationDataSBCorrection(int notid, String nottype) {
        
        Connection con = null;
        PreparedStatement ps = null;
        int returnsuccess = 0;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("DELETE FROM EMP_NOTIFICATION_LOG WHERE NOT_ID=? AND NOT_TYPE=?");
            ps.setInt(1, notid);
            ps.setString(2, nottype);
            returnsuccess = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return returnsuccess;        
    }
}
