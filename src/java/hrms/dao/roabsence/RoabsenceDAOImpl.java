/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.roabsence;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.roabsence.RoabsenceBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class RoabsenceDAOImpl implements RoabsenceDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    protected ServiceBookLanguageDAO sbDAO;
    protected NotificationDAO notificationDao;

    public ServiceBookLanguageDAO getSbDAO() {
        return sbDAO;
    }

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public NotificationDAO getNotificationDao() {
        return notificationDao;
    }

    public void setNotificationDao(NotificationDAO notificationDao) {
        this.notificationDao = notificationDao;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void saveRoabsence(RoabsenceBean pb, int notid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();

            String sql = "INSERT INTO emp_leave (not_id, not_type"
                    + ", fdate, tdate, lsot_id, s_days, emp_id) VALUES(?,?,?,?,?,?, ?)";
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            pst.setInt(1, notid);
            pst.setString(2, "LEAVE");
            pst.setTimestamp(3, new Timestamp(dateFormat.parse(pb.getDurationFrom()).getTime()));
            pst.setTimestamp(4, new Timestamp(dateFormat.parse(pb.getDurationTo()).getTime()));
            pst.setString(5, "03");
            pst.setInt(6, Integer.parseInt(pb.getNoofDays()));
            pst.setString(7, pb.getEmpId());
            pst.executeUpdate();

            int maxLeaveId = 0;
            rs = pst.getGeneratedKeys();
            rs.next();
            maxLeaveId = rs.getInt("leaveid");
            String sbLanguage = sbDAO.getLeaveSanctionDetails(maxLeaveId, pb.getEmpId(), "");
            notificationDao.saveServiceBookLanguage(sbLanguage, notid, "LEAVE");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateRoabsence(RoabsenceBean pb) {
        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = dataSource.getConnection();

            String sql = "UPDATE emp_leave SET not_id = ?"
                    + ", fdate = ?, tdate = ?"
                    + ", s_days = ? WHERE leaveid = ?";
            pst = con.prepareStatement(sql);
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            pst.setInt(1, pb.getNotificationId()); //CommonFunctions.getMaxCode("EMP_TRANSFER", "TR_ID", con));
            pst.setTimestamp(2, new Timestamp(dateFormat.parse(pb.getDurationFrom()).getTime()));
            pst.setTimestamp(3, new Timestamp(dateFormat.parse(pb.getDurationTo()).getTime()));
            pst.setInt(4, Integer.parseInt(pb.getNoofDays()));
            pst.setInt(5, Integer.parseInt(pb.getLeaveId()));
            pst.executeUpdate();
            String sbLanguage = sbDAO.getLeaveSanctionDetails(Integer.parseInt(pb.getLeaveId()), pb.getEmpId(), "");
            notificationDao.saveServiceBookLanguage(sbLanguage, pb.getNotificationId(), "LEAVE");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteRoabsence(RoabsenceBean pb) {

    }

    @Override
    public List getRoabsenceList(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List bisList = new ArrayList();
        RoabsenceBean pbean = null;
        try {
            con = dataSource.getConnection();

            String sql = "SELECT leaveid,fdate,tdate,s_days,EN.doe,EN.not_id,EN.not_type, EN.auth,ordno,orddt,off_en FROM emp_notification EN"
                    + " INNER JOIN emp_leave EL ON EN.not_id = EL.not_id left outer join g_office on EN.off_code=g_office.off_code WHERE EL.emp_id = '" + empid + "' AND lsot_id = '03'";
            System.out.println("Sql:" + sql);
            pst = con.prepareStatement(sql);
            //pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                pbean = new RoabsenceBean();
                pbean.setDateOfEntry(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                pbean.setLeaveId(rs.getInt("leaveid") + "");
                pbean.setNotificationId(rs.getInt("not_id"));
                pbean.setNotificationType(rs.getString("not_type"));
                pbean.setOrderNumber(rs.getString("ordno"));
                pbean.setOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                pbean.setPostedPostName(CommonFunctions.getSPN(con, rs.getString("auth")));
                //pbean.setPunishmentType(rs.getString("punishment"));
                bisList.add(pbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return bisList;
    }

    @Override
    public RoabsenceBean getEmpRoabsenceData(String leaveId) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        RoabsenceBean bisBean = new RoabsenceBean();

        try {
            con = dataSource.getConnection();

            String sql = "SELECT leaveid,fdate,tdate,s_days,EN.*,off_en FROM emp_notification EN"
                    + " INNER JOIN emp_leave EL ON EN.not_id = EL.not_id left outer join g_office on EN.off_code=g_office.off_code WHERE EL.leaveid = ? AND lsot_id = '03'";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(leaveId));
            rs = pst.executeQuery();
            if (rs.next()) {
                bisBean.setLeaveId(rs.getString("leaveid"));
                bisBean.setNotificationId(rs.getInt("not_id"));
                bisBean.setOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                bisBean.setOrderNumber(rs.getString("ordno"));
                bisBean.setHidTempDeptCode(rs.getString("dept_code"));
                bisBean.setHidTempAuthOffCode(rs.getString("off_code"));
                bisBean.setHidTempAuthPost(rs.getString("auth"));
                bisBean.setAuthPostName(CommonFunctions.getSPN(con, rs.getString("auth")));
                bisBean.setHidAuthDeptCode(rs.getString("dept_code"));
                bisBean.setHidAuthOffCode(rs.getString("off_code"));
                bisBean.setAuthSpc(rs.getString("auth"));
                bisBean.setHidPostedDeptCode(rs.getString("dept_code"));
                bisBean.setHidPostedOffCode(rs.getString("off_code"));

                bisBean.setDurationFrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("fdate")));

                bisBean.setDurationTo(CommonFunctions.getFormattedOutputDate1(rs.getDate("tdate")));

                bisBean.setNoofDays(rs.getInt("s_days") + "");
                bisBean.setNote(rs.getString("note"));
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    bisBean.setChkNotSBPrint("Y");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return bisBean;
    }

}
