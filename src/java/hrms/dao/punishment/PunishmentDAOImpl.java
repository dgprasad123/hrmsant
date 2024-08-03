/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.punishment;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.punishment.PunishmentBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Manoj PC
 */
public class PunishmentDAOImpl implements PunishmentDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected MaxAdActionIdDAOImpl maxAdActionIdDAO;
    protected ServiceBookLanguageDAO sbDAO;
    protected NotificationDAO notificationDao;

    public void setNotificationDao(NotificationDAO notificationDao) {
        this.notificationDao = notificationDao;
    }

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public void setMaxAdActionIdDAO(MaxAdActionIdDAOImpl maxAdActionIdDAO) {
        this.maxAdActionIdDAO = maxAdActionIdDAO;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getPunishmentTypes() {
        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;

        List deptlist = new ArrayList();
        try {
            con = dataSource.getConnection();
            String sql = "SELECT punish_id, punishment from g_punishment";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                PunishmentBean pb = new PunishmentBean();
                pb.setPunishId(rs.getString("punish_id"));
                pb.setPunishmentType(rs.getString("punishment"));
                deptlist.add(pb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deptlist;
    }

    @Override
    public void savePunishment(PunishmentBean pb, int notid) {
        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = dataSource.getConnection();

            String sql = "INSERT INTO emp_ad_action (not_id, not_type, punish_id"
                    + ", dur_from, dur_ftime, dur_to, dur_ttime, days, reason,if_pay_heldup,punishment_others) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            pst.setInt(1, notid);
            pst.setString(2, "ADM_ACTION");
            pst.setString(3, pb.getPunishId());
            pst.setTimestamp(4, new Timestamp(dateFormat.parse(pb.getDurationFrom()).getTime()));
            pst.setString(5, pb.getDurationFromTime());
            if (pb.getDurationTo() != null && !pb.getDurationTo().equals("")) {
                pst.setTimestamp(6, new Timestamp(dateFormat.parse(pb.getDurationTo()).getTime()));
            } else {
                pst.setTimestamp(6, null);
            }
            pst.setString(7, pb.getDurationToTime());
            if (pb.getDuration() != null && !pb.getDuration().equals("")) {
                pst.setInt(8, Integer.parseInt(pb.getDuration()));
            } else {
                pst.setInt(8, 0);
            }

            pst.setString(9, pb.getReason());
            pst.setString(10, pb.getPayHeldUp());
            if (pb.getPunishId() != null && pb.getPunishId().equals("23")) {
                pst.setString(11, pb.getOthCategory().toUpperCase());
            } else {
                pst.setString(11, null);
            }
            pst.executeUpdate();
            String sblang = sbDAO.getPunishmentDetails(pb, notid, "ADM_ACTION");
            notificationDao.saveServiceBookLanguage(sblang, notid, "ADM_ACTION");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getPunishmentList(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List punishmentList = new ArrayList();
        PunishmentBean pbean = null;
        try {
            con = dataSource.getConnection();

            String sql = "select acid,emp_notification.doe,emp_notification.not_id,emp_notification.auth,emp_ad_action.not_type,ordno,orddt,off_en, GP.punishment from"
                    + " (select off_code, emp_id,doe,not_id,not_type,ordno,orddt, auth from emp_notification where emp_id=? and not_type='ADM_ACTION')emp_notification"
                    + " left outer join emp_ad_action on emp_notification.not_id=emp_ad_action.not_id"
                    + " left outer join g_office on emp_notification.off_code=g_office.off_code "
                    + " left outer join g_punishment GP ON GP.punish_id = emp_ad_action.punish_id";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                pbean = new PunishmentBean();
                pbean.setDateOfEntry(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                pbean.setAcId(rs.getString("acid"));
                pbean.setNotificationId(rs.getInt("not_id"));
                pbean.setNotificationType(rs.getString("not_type"));
                pbean.setOrderNumber(rs.getString("ordno"));
                pbean.setOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                pbean.setPostedPostName(CommonFunctions.getSPN(con, rs.getString("auth")));
                pbean.setPunishmentType(rs.getString("punishment"));
                punishmentList.add(pbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return punishmentList;
    }

    @Override
    public PunishmentBean getEmpPunishmentData(String acId) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        PunishmentBean pBean = new PunishmentBean();

        try {
            con = dataSource.getConnection();

            String sql = "SELECT EN.*, acid, EA.not_id, punishment, EA.punish_id,dur_from, dur_ftime, dur_to, dur_ttime, days, reason, if_pay_heldup,punishment_others FROM emp_ad_action EA "
                    + " left outer join g_punishment GP ON GP.punish_id = EA.punish_id"
                    + " LEFT OUTER JOIN emp_notification EN ON EA.not_id = EN.not_id"
                    + " WHERE EA.acid=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(acId));
            rs = pst.executeQuery();
            if (rs.next()) {
                pBean.setAcId(rs.getString("acid"));
                pBean.setNotificationId(rs.getInt("not_id"));
                pBean.setOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                pBean.setOrderNumber(rs.getString("ordno"));
                pBean.setHidTempDeptCode(rs.getString("dept_code"));
                pBean.setHidTempAuthOffCode(rs.getString("off_code"));
                pBean.setHidTempAuthPost(rs.getString("auth"));
                pBean.setAuthPostName(CommonFunctions.getSPN(con, rs.getString("auth")));
                pBean.setHidAuthDeptCode(rs.getString("dept_code"));
                pBean.setHidAuthOffCode(rs.getString("off_code"));
                pBean.setAuthSpc(rs.getString("auth"));
                pBean.setHidPostedDeptCode(rs.getString("dept_code"));
                pBean.setHidPostedOffCode(rs.getString("off_code"));
                pBean.setPunishId(rs.getString("punish_id"));
                pBean.setDurationFrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("dur_from")));
                pBean.setDurationFromTime(rs.getString("dur_ftime"));
                pBean.setDurationTo(CommonFunctions.getFormattedOutputDate1(rs.getDate("dur_to")));
                pBean.setDurationToTime(rs.getString("dur_ttime"));
                pBean.setDuration(rs.getInt("days") + "");
                pBean.setNote(rs.getString("note"));
                pBean.setPayHeldUp(rs.getString("if_pay_heldup"));
                pBean.setReason(rs.getString("reason"));
                pBean.setOthCategory(rs.getString("punishment_others"));
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    pBean.setChkNotSBPrint("Y");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pBean;
    }

    @Override
    public void updatePunishment(PunishmentBean pb) {
        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = dataSource.getConnection();

            String sql = "UPDATE emp_ad_action SET not_id = ?, punish_id = ?"
                    + ", dur_from = ?, dur_ftime = ?, dur_to = ?, dur_ttime = ?"
                    + ", days = ?, reason = ?, if_pay_heldup = ?,punishment_others=? WHERE acid = ?";
            pst = con.prepareStatement(sql);
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            pst.setInt(1, pb.getNotificationId()); //CommonFunctions.getMaxCode("EMP_TRANSFER", "TR_ID", con));
            pst.setString(2, pb.getPunishId());
            pst.setTimestamp(3, new Timestamp(dateFormat.parse(pb.getDurationFrom()).getTime()));
            pst.setString(4, pb.getDurationFromTime());
            if (pb.getDurationTo() != null && !pb.getDurationTo().equals("")) {
                pst.setTimestamp(5, new Timestamp(dateFormat.parse(pb.getDurationTo()).getTime()));
            } else {
                pst.setTimestamp(5, null);
            }
            pst.setString(6, pb.getDurationToTime());
            if (pb.getDuration() != null && !pb.getDuration().equals("")) {
                pst.setInt(7, Integer.parseInt(pb.getDuration()));
            } else {
                pst.setInt(7, 0);
            }

            pst.setString(8, pb.getReason());
            pst.setString(9, pb.getPayHeldUp());
            if (pb.getPunishId() != null && pb.getPunishId().equals("23")) {
                pst.setString(10, pb.getOthCategory().toUpperCase());
            } else {
                pst.setString(10, null);
            }
            pst.setInt(11, Integer.parseInt(pb.getAcId()));
            pst.executeUpdate();
            String sblang = sbDAO.getPunishmentDetails(pb, pb.getNotificationId(), "ADM_ACTION");
            notificationDao.saveServiceBookLanguage(sblang, pb.getNotificationId(), "ADM_ACTION");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deletePunishment(PunishmentBean pb) {
        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = dataSource.getConnection();
            String sql = "DELETE FROM EMP_AD_ACTION WHERE ACID = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(pb.getAcId()));
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}
