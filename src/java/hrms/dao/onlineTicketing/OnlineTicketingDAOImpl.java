/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.onlineTicketing;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.common.FileAttribute;
import hrms.model.onlineTicketing.OnlineTicketing;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Surendra
 */
public class OnlineTicketingDAOImpl implements OnlineTicketingDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addTicket(OnlineTicketing ticket, String spc) {

        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        PreparedStatement pst = null;
        String createdDateTime = "";
        String overDueDateTime = "";

        try {
            con = dataSource.getConnection();
            /*pst = con.prepareStatement("select * from user_details where linkid='59001114'");
             rs=pst.executeQuery();
             if(){
                
             }
             */
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
            createdDateTime = dateFormat.format(cal.getTime());
            cal.add(Calendar.DAY_OF_YEAR, 1);
            overDueDateTime = dateFormat.format(cal.getTime());
            int tktId = CommonFunctions.getMaxCode(con, "ticket", "ticket_id");
            ps = con.prepareStatement("INSERT INTO ticket (ticket_id,user_id,created_date_time,message,closed_date_time,"
                    + "status,over_due_date_time,duration_for_reply,reopen_date_time,assigned_to_user_id,topic_id,dept_code,dist_code,emp_id ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");

            ps.setInt(1, tktId);
            ps.setString(2, ticket.getUsername());
            ps.setTimestamp(3, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            ps.setString(4, ticket.getMessage());
            ps.setTimestamp(5, null);
            ps.setString(6, ticket.getStatus());
            ps.setTimestamp(7, new Timestamp(dateFormat.parse(overDueDateTime).getTime()));
            ps.setDouble(8, 0);
            ps.setTimestamp(9, null);
            ps.setString(10, ticket.getAssignedToUserId());
            ps.setInt(11, Integer.parseInt(ticket.getTopicId()));
            ps.setString(12, ticket.getDeptCode());
            ps.setString(13, ticket.getDistCode());
            ps.setString(14, ticket.getUserId());
            ps.execute();

            int attacId = 0;
            if (ticket.getOfileName() != null && !ticket.getOfileName().equals("")) {
                attacId = CommonFunctions.getMaxCode(con, "ticket_attachment", "attachment_id");
                pst = con.prepareStatement("INSERT INTO ticket_attachment (attachment_id,o_file_name,d_file_name,ref_id,ref_type,file_path,file_type) VALUES (?,?,?,?,?,?,?)");
                pst.setInt(1, attacId);
                pst.setString(2, ticket.getOfileName());
                pst.setString(3, ticket.getDfileName());
                pst.setInt(4, tktId);
                pst.setString(5, ticket.getRefType());
                pst.setString(6, ticket.getFilePath());
                pst.setString(7, ticket.getFileType());
                pst.execute();
            }
            int logId = CommonFunctions.getMaxCode(con, "ticket_log", "log_id");
            String userType = "HRMS Employee";

            pst = con.prepareStatement("select count(*) cnt from g_privilege_map where role_id='05' and spc=?");
            pst.setString(1, spc);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    userType = "HRMS DDO";
                }
            }

            ps = con.prepareStatement("INSERT INTO ticket_log (log_id,ticket_id,attachment_id,entery_date_time,message,"
                    + "user_type,user_name,status_log ) VALUES (?,?,?,?,?,?,?,?) ");
            ps.setInt(1, logId);
            ps.setInt(2, tktId);
            ps.setInt(3, attacId);
            ps.setTimestamp(4, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            ps.setString(5, ticket.getMessage());
            ps.setString(6, userType);
            ps.setString(7, ticket.getUsername());
            ps.setString(8, "Open");
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(ps, pst);
        }
    }

    @Override
    public List getTicketList(String userId, String fdate, String tdate) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        OnlineTicketing ticket = null;
        SimpleDateFormat sdf = new SimpleDateFormat("EEEEE MMMMM yyyy hh:mm:ss a");

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT ticket_id,user_id,created_date_time,message,closed_date_time, "
                    + "status,over_due_date_time,duration_for_reply,reopen_date_time,assigned_to_user_id,topic,ticket.topic_id,off_en FROM ticket  "
                    + "LEFT OUTER JOIN user_details userdet ON ticket.user_id=userdet.username "
                    + "LEFT OUTER JOIN emp_mast ON userdet.linkid=emp_mast.emp_id "
                    + "LEFT OUTER JOIN g_office off ON emp_mast.cur_off_code=off.off_code LEFT OUTER JOIN g_ticket_topic on g_ticket_topic.topic_id=ticket.topic_id  WHERE user_id=? AND (ticket.created_date_time BETWEEN ? AND ?) ORDER BY ticket.ticket_id desc");

            ps.setString(1, userId);
            ps.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fdate).getTime()));
            ps.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy H:m:s").parse(tdate + " 23:59:59").getTime()));
            rs = ps.executeQuery();
            while (rs.next()) {
                ticket = new OnlineTicketing();
                ticket.setTopicName(rs.getString("topic"));
                ticket.setUserId(rs.getString("user_id"));
                String tickId = rs.getInt("ticket_id") + "";
                String FormattickId = String.format("%5s", tickId).replace(' ', '0');
                ticket.setFticketid(FormattickId);
                ticket.setTicketId(rs.getInt("ticket_id"));
                if (rs.getTimestamp("created_date_time") != null) {
                    ticket.setCreatedDateTimeString(rs.getString("created_date_time"));
                    ticket.setCreatedDateTime(new Date(rs.getTimestamp("created_date_time").getTime()));
                } else {
                    ticket.setCreatedDateTime(null);
                }

                ticket.setMessage(rs.getString("message"));

                if (rs.getTimestamp("closed_date_time") != null) {
                    ticket.setClosedDateTime(new Date(rs.getTimestamp("closed_date_time").getTime()));
                } else {
                    ticket.setClosedDateTime(null);
                }
                String Status = rs.getString("status");
                if (Status == "" || Status == null) {
                    ticket.setStatus("Open");
                } else {
                    ticket.setStatus(rs.getString("status"));
                }

                if (rs.getTimestamp("over_due_date_time") != null) {
                    ticket.setOverDueDateTime(new Date(rs.getTimestamp("over_due_date_time").getTime()));
                } else {
                    ticket.setOverDueDateTime(null);
                }
                ticket.setDurationForReply(rs.getDouble("duration_for_reply"));
                if (rs.getTimestamp("reopen_date_time") != null) {
                    ticket.setReopenDateTime(new Date(rs.getTimestamp("reopen_date_time").getTime()));
                } else {
                    ticket.setReopenDateTime(null);
                }
                ticket.setAssignedToUserId(rs.getString("assigned_to_user_id"));
                ticket.setTopicId(rs.getInt("topic_id") + "");
                ticket.setOffname(rs.getString("off_en"));
                li.add(ticket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public OnlineTicketing editTicket(int ticketId) {
        Connection con = null;

        try {

            con = dataSource.getConnection();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return null;
    }

    @Override
    public void closeTicket(int ticketId) {
        Connection con = null;
        PreparedStatement ps = null;
        String closedDate = "";
        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
            closedDate = dateFormat.format(cal.getTime());

            ps = con.prepareStatement("UPDATE ticket SET closed_date_time=? WHERE ticket_id=?");
            ps.setTimestamp(1, new Timestamp(dateFormat.parse(closedDate).getTime()));
            ps.setInt(2, ticketId);
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getTicketListDC(String userId, String loginId, String fdate, String tdate) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        List li = new ArrayList();
        OnlineTicketing ticket = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        try {
            con = dataSource.getConnection();

            if (loginId.equals("dcsecretariate") || loginId.equals("dcsecretariate2")) {
                //in query off.off_code is added for showing office code//
                ps = con.prepareStatement("SELECT i_read,off.off_code,ticket_id,user_id,created_date_time,message,closed_date_time, "
                        + "status,over_due_date_time,state_id,duration_for_reply,reopen_date_time,assigned_to_user_id,topic,ticket.topic_id,off_en,off.ddo_code"
                        + ",ticket.dept_code,ticket.dist_code,off.ddo_code FROM ticket  "
                        + "LEFT OUTER JOIN user_details userdet ON ticket.user_id=userdet.username "
                        + "LEFT OUTER JOIN emp_mast ON userdet.linkid=emp_mast.emp_id "
                        + "LEFT OUTER JOIN g_office off ON emp_mast.cur_off_code=off.off_code  LEFT OUTER JOIN g_ticket_topic on"
                        + " g_ticket_topic.topic_id=ticket.topic_id  WHERE (ticket.created_date_time BETWEEN ? AND ?)"
                        + " AND ((emp_mast.cur_off_code LIKE 'OLS%' AND (emp_mast.cur_off_code LIKE '%0000' OR emp_mast.cur_off_code LIKE '%OLSWAT0010001%' ) )"
                        + " OR ticket.dist_code='2161') AND  ((ticket.status != 'Closed' AND ticket.status != 'Resolved' ) OR  status IS  NULL )"
                        + " ORDER BY CASE WHEN ticket.status = 'Reopened' THEN 1 WHEN ticket.status is NULL THEN 2 WHEN"
                        + " ticket.status = 'Data Insufficient' THEN 3  WHEN ticket.status = 'Open' THEN 4 END, ticket.ticket_id DESC");
                ps.setTimestamp(1, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fdate).getTime()));
                ps.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy H:m:s").parse(tdate + " 23:59:59").getTime()));

            } else {

                ps = con.prepareStatement("SELECT i_read,off.off_code,ticket_id,user_id,created_date_time,message,closed_date_time, "
                        + "status,over_due_date_time,state_id,duration_for_reply,reopen_date_time,assigned_to_user_id,topic,ticket.topic_id,off_en,ticket.dept_code,ticket.dist_code,off.ddo_code FROM ticket  "
                        + "LEFT OUTER JOIN user_details userdet ON ticket.user_id=userdet.username "
                        + "LEFT OUTER JOIN emp_mast ON userdet.linkid=emp_mast.emp_id "
                        // + " LEFT OUTER JOIN g_spc ON emp_mast.cur_spc=g_spc.spc"
                        //  + " LEFT  OUTER JOIN g_post ON g_spc.gpc=g_post.post"
                        + " LEFT OUTER JOIN g_office off ON emp_mast.cur_off_code=off.off_code"
                        + " LEFT OUTER JOIN g_ticket_topic on g_ticket_topic.topic_id=ticket.topic_id  WHERE ticket.dist_code=? AND (ticket.created_date_time BETWEEN ? AND ?) AND ((ticket.status != 'Closed' AND ticket.status != 'Resolved' ) OR  status IS  NULL ) ORDER BY CASE WHEN ticket.status = 'Reopened' THEN 1 WHEN ticket.status is NULL THEN 2 WHEN ticket.status = 'Data Insufficient' THEN 3  WHEN ticket.status = 'Open' THEN 4   END");

                ps.setString(1, userId);
                ps.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fdate).getTime()));
                ps.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy H:m:s").parse(tdate + " 23:59:59").getTime()));
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                ticket = new OnlineTicketing();
                ticket.setTopicName(rs.getString("topic"));
                ticket.setUserId(rs.getString("user_id"));
                String tickId = rs.getInt("ticket_id") + "";
                String FormattickId = String.format("%5s", tickId).replace(' ', '0');
                ticket.setFticketid(FormattickId);
                ticket.setTicketId(rs.getInt("ticket_id"));
                // String dTicketid=CommonFunctions.encrypt(rs.getInt("ticket_id")+"");
                if (rs.getTimestamp("created_date_time") != null) {
                    ticket.setCreatedDateTime(rs.getDate("created_date_time"));
                    ticket.setCreatedDateTimeString(rs.getString("created_date_time"));
                } else {
                    ticket.setCreatedDateTime(null);
                }

                ticket.setMessage(rs.getString("message"));

                if (rs.getTimestamp("closed_date_time") != null) {
                    ticket.setClosedDateTime(new Date(rs.getTimestamp("closed_date_time").getTime()));
                } else {
                    ticket.setClosedDateTime(null);
                }
                String Status = rs.getString("status");
                if (Status == "" || Status == null) {
                    ticket.setStatus("Open");
                } else {
                    ticket.setStatus(rs.getString("status"));
                }
                ticket.setIread(rs.getString("i_read"));
                ticket.setDdoCode(rs.getString("ddo_code"));
                if (rs.getTimestamp("over_due_date_time") != null) {
                    ticket.setOverDueDateTime(new Date(rs.getTimestamp("over_due_date_time").getTime()));
                } else {
                    ticket.setOverDueDateTime(null);
                }
                ticket.setDurationForReply(rs.getDouble("duration_for_reply"));
                if (rs.getTimestamp("reopen_date_time") != null) {
                    ticket.setReopenDateTime(new Date(rs.getTimestamp("reopen_date_time").getTime()));
                } else {
                    ticket.setReopenDateTime(null);
                }
                ticket.setAssignedToUserId(rs.getString("state_id"));
                ticket.setTopicId(rs.getString("topic_id"));
                String encyId = CommonFunctions.encodedTxt(tickId);
                ticket.setEncticketid(encyId);
                String off_en = rs.getString("off_en");
                ps1 = con.prepareStatement("SELECT * FROM g_district WHERE dist_code=? ");
                ps1.setString(1, rs.getString("dist_code"));

                rs1 = ps1.executeQuery();
                while (rs1.next()) {
                    ticket.setDistCode(rs1.getString("dist_name"));
                    if (rs.getString("dept_code") == null) {
                        String DistName = rs1.getString("dist_name");
                        off_en = "District Coordinator " + DistName;
                        // off_en="GENERAL ADMINISTRATION AND PUBLIC GRIEVANCE DEPARTMENT,GOVERNMENT OF ODISHA";
                    }
                }
                //show office name with office code//
                ticket.setOffname(off_en + "(" + StringUtils.defaultString(rs.getString("off_code")) + ")" + "(DDO Code:" + rs.getString("ddo_code") + ")");
                ticket.setDeptCode(rs.getString("dept_code"));
                // ticket.setUsername(rs.getString("post"));
                li.add(ticket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public List getTicketListDDO(String empId, String loginId, String fdate, String tdate, String offCode) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        List li = new ArrayList();
        OnlineTicketing ticket = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        try {
            con = dataSource.getConnection();

            ps = con.prepareStatement("SELECT i_read,off.off_code,ticket_id,user_id,created_date_time,message,closed_date_time, "
                    + "status,over_due_date_time,state_id,duration_for_reply,reopen_date_time,assigned_to_user_id,topic,ticket.topic_id,off_en,ticket.dept_code,ticket.dist_code,off.ddo_code FROM ticket  "
                    + "LEFT OUTER JOIN user_details userdet ON ticket.user_id=userdet.username "
                    + "LEFT OUTER JOIN emp_mast ON userdet.linkid=emp_mast.emp_id "
                    + " LEFT OUTER JOIN g_office off ON emp_mast.cur_off_code=off.off_code"
                    + " LEFT OUTER JOIN g_ticket_topic on g_ticket_topic.topic_id=ticket.topic_id WHERE emp_mast.cur_off_code=? AND (ticket.created_date_time BETWEEN ? AND ?) AND ((ticket.status != 'Closed' AND ticket.status != 'Resolved' ) OR  status IS  NULL ) ORDER BY CASE WHEN ticket.status = 'Reopened' THEN 1 WHEN ticket.status is NULL THEN 2 WHEN ticket.status = 'Data Insufficient' THEN 3  WHEN ticket.status = 'Open' THEN 4   END");

            ps.setString(1, offCode);
            ps.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fdate).getTime()));
            ps.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy H:m:s").parse(tdate + " 23:59:59").getTime()));
            rs = ps.executeQuery();
            while (rs.next()) {
                ticket = new OnlineTicketing();
                ticket.setTopicName(rs.getString("topic"));
                ticket.setUserId(rs.getString("user_id"));
                String tickId = rs.getInt("ticket_id") + "";
                String FormattickId = String.format("%5s", tickId).replace(' ', '0');
                ticket.setFticketid(FormattickId);
                ticket.setTicketId(rs.getInt("ticket_id"));
                // String dTicketid=CommonFunctions.encrypt(rs.getInt("ticket_id")+"");
                if (rs.getTimestamp("created_date_time") != null) {
                    ticket.setCreatedDateTime(rs.getDate("created_date_time"));
                    ticket.setCreatedDateTimeString(rs.getString("created_date_time"));
                } else {
                    ticket.setCreatedDateTime(null);
                }

                ticket.setMessage(rs.getString("message"));

                if (rs.getTimestamp("closed_date_time") != null) {
                    ticket.setClosedDateTime(new Date(rs.getTimestamp("closed_date_time").getTime()));
                } else {
                    ticket.setClosedDateTime(null);
                }
                String Status = rs.getString("status");
                if (Status == "" || Status == null) {
                    ticket.setStatus("Open");
                } else {
                    ticket.setStatus(rs.getString("status"));
                }
                ticket.setIread(rs.getString("i_read"));
                ticket.setDdoCode(rs.getString("ddo_code"));
                if (rs.getTimestamp("over_due_date_time") != null) {
                    ticket.setOverDueDateTime(new Date(rs.getTimestamp("over_due_date_time").getTime()));
                } else {
                    ticket.setOverDueDateTime(null);
                }
                ticket.setDurationForReply(rs.getDouble("duration_for_reply"));
                if (rs.getTimestamp("reopen_date_time") != null) {
                    ticket.setReopenDateTime(new Date(rs.getTimestamp("reopen_date_time").getTime()));
                } else {
                    ticket.setReopenDateTime(null);
                }
                ticket.setAssignedToUserId(rs.getString("state_id"));
                ticket.setTopicId(rs.getString("topic_id"));
                String encyId = CommonFunctions.encodedTxt(tickId);
                ticket.setEncticketid(encyId);
                String off_en = rs.getString("off_en");
                ps1 = con.prepareStatement("SELECT * FROM g_district WHERE dist_code=? ");
                ps1.setString(1, rs.getString("dist_code"));

                rs1 = ps1.executeQuery();
                while (rs1.next()) {
                    ticket.setDistCode(rs1.getString("dist_name"));
                    if (rs.getString("dept_code") == null) {
                        String DistName = rs1.getString("dist_name");
                        off_en = "District Coordinator " + DistName;
                        // off_en="GENERAL ADMINISTRATION AND PUBLIC GRIEVANCE DEPARTMENT,GOVERNMENT OF ODISHA";
                    }
                }
                //show office name with office code//
                ticket.setOffname(off_en + "(" + StringUtils.defaultString(rs.getString("off_code")) + ")" + "(DDO Code:" + rs.getString("ddo_code") + ")");
                ticket.setDeptCode(rs.getString("dept_code"));
                // ticket.setUsername(rs.getString("post"));
                li.add(ticket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public List getTicketResolvedListDC(String userId, String loginId, String fdate, String tdate) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        List li = new ArrayList();
        OnlineTicketing ticket = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        try {
            con = dataSource.getConnection();

            if (loginId.equals("dcsecretariate") || loginId.equals("dcsecretariate2")) {
                //in query off.off_code is added for showing office code//
                ps = con.prepareStatement("SELECT ticket_log.login_id,off.off_code,ticket.ticket_id,user_id,created_date_time,ticket.message,closed_date_time, "
                        + "status,over_due_date_time,state_id,duration_for_reply,reopen_date_time,assigned_to_user_id,topic,ticket.topic_id,off_en,ticket.dept_code,ticket.dist_code,off.ddo_code FROM ticket  "
                        + "INNER JOIN ticket_log ON ticket.ticket_id=ticket_log.ticket_id AND  ((ticket_log.status_log = 'Closed' OR  ticket_log.status_log = 'Resolved' )  ) "
                        + "LEFT OUTER JOIN user_details userdet ON ticket.user_id=userdet.username "
                        + "LEFT OUTER JOIN emp_mast ON userdet.linkid=emp_mast.emp_id "
                        + "LEFT OUTER JOIN g_office off ON emp_mast.cur_off_code=off.off_code  LEFT OUTER JOIN g_ticket_topic on g_ticket_topic.topic_id=ticket.topic_id  WHERE (ticket.created_date_time BETWEEN ? AND ?) AND ((emp_mast.cur_off_code LIKE 'OLS%' AND (emp_mast.cur_off_code LIKE '%0000' OR emp_mast.cur_off_code LIKE '%OLSWAT0010001%' ) ) OR ticket.dist_code='2161') AND  ((ticket.status != 'Closed' AND ticket.status != 'Resolved' )  ) , ticket.ticket_id DESC");
                ps.setTimestamp(1, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fdate).getTime()));
                ps.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy H:m:s").parse(tdate + " 23:59:59").getTime()));

            } else {

                ps = con.prepareStatement("SELECT ticket_log.login_id,off.off_code,ticket.ticket_id,user_id,created_date_time,ticket.message,closed_date_time, "
                        + "status,over_due_date_time,state_id,duration_for_reply,reopen_date_time,assigned_to_user_id,topic,ticket.topic_id,off_en,ticket.dept_code,ticket.dist_code,off.ddo_code FROM ticket  "
                        + "INNER JOIN ticket_log ON ticket.ticket_id=ticket_log.ticket_id AND  ((ticket_log.status_log = 'Closed' OR  ticket_log.status_log = 'Resolved' )  ) "
                        + "LEFT OUTER JOIN user_details userdet ON ticket.user_id=userdet.username "
                        + "LEFT OUTER JOIN emp_mast ON userdet.linkid=emp_mast.emp_id "
                        // + " LEFT OUTER JOIN g_spc ON emp_mast.cur_spc=g_spc.spc"
                        //  + " LEFT  OUTER JOIN g_post ON g_spc.gpc=g_post.post"
                        + " LEFT OUTER JOIN g_office off ON emp_mast.cur_off_code=off.off_code"
                        + " LEFT OUTER JOIN g_ticket_topic on g_ticket_topic.topic_id=ticket.topic_id  WHERE ticket.dist_code=? AND (ticket.created_date_time BETWEEN ? AND ?)  AND ((ticket.status = 'Closed' OR  ticket.status = 'Resolved' )  ) ");

                ps.setString(1, userId);
                ps.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fdate).getTime()));
                ps.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy H:m:s").parse(tdate + " 23:59:59").getTime()));

            }

            rs = ps.executeQuery();
            while (rs.next()) {
                ticket = new OnlineTicketing();
                ticket.setSolvedBy(rs.getString("login_id"));
                ticket.setTopicName(rs.getString("topic"));
                ticket.setUserId(rs.getString("user_id"));
                String tickId = rs.getInt("ticket_id") + "";
                String FormattickId = String.format("%5s", tickId).replace(' ', '0');
                ticket.setFticketid(FormattickId);
                ticket.setTicketId(rs.getInt("ticket_id"));
                if (rs.getTimestamp("created_date_time") != null) {
                    ticket.setCreatedDateTime(new Date(rs.getTimestamp("created_date_time").getTime()));
                    ticket.setCreatedDateTimeString(sdf.format(rs.getTimestamp("created_date_time").getTime()));
                } else {
                    ticket.setCreatedDateTime(null);
                }

                ticket.setMessage(rs.getString("message"));

                if (rs.getTimestamp("closed_date_time") != null) {
                    ticket.setClosedDateTime(new Date(rs.getTimestamp("closed_date_time").getTime()));
                } else {
                    ticket.setClosedDateTime(null);
                }
                String Status = rs.getString("status");
                if (Status == "" || Status == null) {
                    ticket.setStatus("Open");
                } else {
                    ticket.setStatus(rs.getString("status"));
                }

                if (rs.getTimestamp("over_due_date_time") != null) {
                    ticket.setOverDueDateTime(new Date(rs.getTimestamp("over_due_date_time").getTime()));
                } else {
                    ticket.setOverDueDateTime(null);
                }
                ticket.setDurationForReply(rs.getDouble("duration_for_reply"));
                if (rs.getTimestamp("reopen_date_time") != null) {
                    ticket.setReopenDateTime(new Date(rs.getTimestamp("reopen_date_time").getTime()));
                } else {
                    ticket.setReopenDateTime(null);
                }
                ticket.setAssignedToUserId(rs.getString("state_id"));
                ticket.setTopicId(rs.getString("topic_id"));
                String off_en = rs.getString("off_en");
                ps1 = con.prepareStatement("SELECT * FROM g_district WHERE dist_code=? ");
                ps1.setString(1, rs.getString("dist_code"));

                rs1 = ps1.executeQuery();
                while (rs1.next()) {
                    ticket.setDistCode(rs1.getString("dist_name"));
                    if (rs.getString("dept_code") == null) {
                        String DistName = rs1.getString("dist_name");
                        off_en = "District Coordinator " + DistName;
                        // off_en="GENERAL ADMINISTRATION AND PUBLIC GRIEVANCE DEPARTMENT,GOVERNMENT OF ODISHA";
                    }
                }
                //show office name with office code//
                ticket.setOffname(off_en + "(" + StringUtils.defaultString(rs.getString("off_code")) + ")" + "(DDO Code:" + rs.getString("ddo_code") + ")");
                ticket.setDeptCode(rs.getString("dept_code"));
                // ticket.setUsername(rs.getString("post"));
                li.add(ticket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public List ticketActionDc(String userId, String encticketid, String loginId) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        List li = new ArrayList();
        String deyticketid = CommonFunctions.decodedTxt(encticketid);
        int ticketId = Integer.parseInt(deyticketid);
        OnlineTicketing ticket = null;
        try {
            con = dataSource.getConnection();

            ps = con.prepareStatement("UPDATE ticket SET i_read=? WHERE ticket_id=? AND dist_code=?");
            ps.setString(1, "Y");
            ps.setInt(2, ticketId);
            ps.setString(3, userId);
            ps.executeUpdate();

            ps1 = con.prepareStatement("SELECT a.* FROM ticket_log a ,ticket b  WHERE a.ticket_id=b.ticket_id AND  a.ticket_id=? AND b.dist_code=? ORDER BY log_id DESC ");
            ps1.setInt(1, ticketId);
            ps1.setString(2, userId);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                ticket = new OnlineTicketing();
                if (rs1.getTimestamp("entery_date_time") != null) {
                    ticket.setCreatedDateTime(new Date(rs1.getTimestamp("entery_date_time").getTime()));
                } else {
                    ticket.setCreatedDateTime(null);
                }

                ticket.setMessage(rs1.getString("message"));
                ticket.setUsername(rs1.getString("user_name"));
                String userType = "";
                if (rs1.getString("login_id") != null && !rs1.getString("login_id").equals("")) {
                    userType = rs1.getString("user_type") + "-" + rs1.getString("login_id").toUpperCase();
                } else {
                    userType = rs1.getString("user_type");
                }

                ticket.setUserType(userType);
                ticket.setAttachmentId(rs1.getInt("attachment_id"));
                ticket.setSolvedBy(rs1.getString("login_id"));
                if (loginId.equals("dcsecretariate") || loginId.equals("dcsecretariate2")) {
                    ps = con.prepareStatement("SELECT ticket_id,created_date_time,user_id,ticket.emp_id,created_date_time,message,closed_date_time,concat(f_name,' ',m_name,' ',l_name) as name, "
                            + "status,over_due_date_time,duration_for_reply,ticket.dept_code,reopen_date_time,assigned_to_user_id,topic,ticket.topic_id,off_en FROM ticket  "
                            + "LEFT OUTER JOIN user_details userdet ON ticket.user_id=userdet.username "
                            + "LEFT OUTER JOIN emp_mast ON userdet.linkid=emp_mast.emp_id "
                            + "LEFT OUTER JOIN g_office off ON emp_mast.cur_off_code=off.off_code LEFT OUTER JOIN g_ticket_topic on g_ticket_topic.topic_id=ticket.topic_id  WHERE  ticket_id=?");
                    // ps.setString(1, userId);
                    ps.setInt(1, ticketId);

                } else {
                    ps = con.prepareStatement("SELECT ticket_id,created_date_time,user_id,ticket.emp_id,created_date_time,message,closed_date_time,concat(f_name,' ',m_name,' ',l_name) as name, "
                            + "status,over_due_date_time,duration_for_reply,ticket.dept_code,reopen_date_time,assigned_to_user_id,topic,ticket.topic_id,off_en FROM ticket  "
                            + "LEFT OUTER JOIN user_details userdet ON ticket.user_id=userdet.username "
                            + "LEFT OUTER JOIN emp_mast ON userdet.linkid=emp_mast.emp_id "
                            + "LEFT OUTER JOIN g_office off ON emp_mast.cur_off_code=off.off_code LEFT OUTER JOIN g_ticket_topic on g_ticket_topic.topic_id=ticket.topic_id  WHERE ticket.dist_code=? AND ticket_id=?");
                    ;
                    ps.setString(1, userId);
                    ps.setInt(2, ticketId);
                }
                rs = ps.executeQuery();
                while (rs.next()) {

                    String topic = rs.getString("topic");
                    String tickId = rs.getInt("ticket_id") + "";
                    String FormattickId = String.format("%5s", tickId).replace(' ', '0');
                    String off_en = rs.getString("off_en");
                    ticket.setDeptCode(rs.getString("dept_code"));
                    //  ticket.setCreatedDateTime(rs.getDate("created_date_time"));
                    ticket.setUserId(rs.getString("emp_id"));
                    ticket.setStatus(rs.getString("status"));
                    ps = con.prepareStatement("SELECT * FROM ticket_attachment WHERE ref_id=?");
                    ps.setInt(1, ticketId);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        ticket.setOfileName(rs.getString("o_file_name"));
                        ticket.setFilePath(rs.getString("file_path"));
                        ticket.setFileType(rs.getString("file_type"));
                        ticket.setDfileName(rs.getString("d_file_name"));
                    }

                    ticket.setFticketid(FormattickId);
                    ticket.setTopicName(topic);
                    ticket.setTicketId(rs1.getInt("ticket_id"));
                    ticket.setOffname(off_en);
                }

                li.add(ticket);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void SaveonlineticketDc(OnlineTicketing ticket) {

        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        PreparedStatement pst = null;
        String createdDateTime = "";
        String overDueDateTime = "";
        //String deyticketid = CommonFunctions.decodedTxt(ticket.getEncticketid());
        // int ticketId = Integer.parseInt(deyticketid);

        String deyticketid = null;
        int ticketId = 0;
        try {
            con = this.dataSource.getConnection();

            ticketId = ticket.getTicketId();
            // System.out.println("ticket.getLoginId()===" + ticket.getLoginId());
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
            createdDateTime = dateFormat.format(cal.getTime());
            cal.add(Calendar.DAY_OF_YEAR, 1);
            overDueDateTime = dateFormat.format(cal.getTime());
            int tktId = ticketId;

            ps = con.prepareStatement("UPDATE ticket SET status=? WHERE ticket_id=?");
            ps.setString(1, ticket.getStatus());
            // ps.setInt(2, ticket.getTicketId());
            ps.setInt(2, ticketId);
            if (ticket.getLoginId() != null && !ticket.getLoginId().equals("")) {
                ps.executeUpdate();
            }

            int attacId = 0;
            if (ticket.getOfileName() != null && !ticket.getOfileName().equals("")) {
                attacId = CommonFunctions.getMaxCode(con, "ticket_attachment", "attachment_id");
                pst = con.prepareStatement("INSERT INTO ticket_attachment (attachment_id,o_file_name,d_file_name,ref_id,ref_type,file_path,file_type) VALUES (?,?,?,?,?,?,?)");
                pst.setInt(1, attacId);
                pst.setString(2, ticket.getOfileName());
                pst.setString(3, ticket.getDfileName());
                pst.setInt(4, tktId);
                pst.setString(5, ticket.getRefType());
                pst.setString(6, ticket.getFilePath());
                pst.setString(7, ticket.getFileType());
                pst.execute();
            }
            int logId = CommonFunctions.getMaxCode(con, "ticket_log", "log_id");
            ps = con.prepareStatement("INSERT INTO ticket_log (log_id,ticket_id,attachment_id,entery_date_time,message,"
                    + "user_type,user_name,login_id,status_log ) VALUES (?,?,?,?,?,?,?,?,?) ");
            ps.setInt(1, logId);
            ps.setInt(2, tktId);
            ps.setInt(3, attacId);
            ps.setTimestamp(4, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            ps.setString(5, ticket.getMessage());
            ps.setString(6, ticket.getUserType());
            ps.setString(7, ticket.getUsername());
            ps.setString(8, ticket.getLoginId());
            ps.setString(9, ticket.getStatus());
            if (ticket.getLoginId() != null && !ticket.getLoginId().equals("")) {
                // if (ticket.getUserType() != null && ticket.getUserType().equals("HRMS Staff(District Team)")) {
                ps.execute();
                // }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(ps, pst);
        }
    }

    @Override
    public List ticketEmpActionDc(String userId, int ticketId) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        List li = new ArrayList();
        OnlineTicketing ticket = null;
        try {
            con = dataSource.getConnection();
            ps1 = con.prepareStatement("SELECT * FROM ticket_log WHERE ticket_id=? ORDER BY log_id DESC ");
            ps1.setInt(1, ticketId);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                ticket = new OnlineTicketing();
                if (rs1.getTimestamp("entery_date_time") != null) {
                    ticket.setCreatedDateTime(new Date(rs1.getTimestamp("entery_date_time").getTime()));
                } else {
                    ticket.setCreatedDateTime(null);
                }

                ticket.setMessage(rs1.getString("message"));
                ticket.setUsername(rs1.getString("user_name"));
                ticket.setUserType(rs1.getString("user_type"));
                ticket.setAttachmentId(rs1.getInt("attachment_id"));
                ps = con.prepareStatement("SELECT ticket_id,user_id,created_date_time,message,closed_date_time,concat(f_name,' ',m_name,' ',l_name) as name, "
                        + "status,over_due_date_time,duration_for_reply,reopen_date_time,assigned_to_user_id,topic,ticket.topic_id,off_en FROM ticket  "
                        + "LEFT OUTER JOIN user_details userdet ON ticket.user_id=userdet.username "
                        + "LEFT OUTER JOIN emp_mast ON userdet.linkid=emp_mast.emp_id "
                        + "LEFT OUTER JOIN g_office off ON emp_mast.cur_off_code=off.off_code LEFT OUTER JOIN g_ticket_topic on g_ticket_topic.topic_id=ticket.topic_id  WHERE ticket.user_id=? AND ticket_id=?");
                ps.setString(1, userId);
                ps.setInt(2, ticketId);
                rs = ps.executeQuery();
                while (rs.next()) {

                    String topic = rs.getString("topic");
                    String tickId = rs.getInt("ticket_id") + "";
                    String FormattickId = String.format("%5s", tickId).replace(' ', '0');
                    String off_en = rs.getString("off_en");
                    ticket.setStatus(rs.getString("status"));
                    ps = con.prepareStatement("SELECT * FROM ticket_attachment WHERE ref_id=?");
                    ps.setInt(1, ticketId);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        ticket.setOfileName(rs.getString("o_file_name"));
                        ticket.setFilePath(rs.getString("file_path"));
                        ticket.setFileType(rs.getString("file_type"));
                        ticket.setDfileName(rs.getString("d_file_name"));
                    }

                    ticket.setFticketid(FormattickId);
                    ticket.setTopicName(topic);
                    ticket.setTicketId(rs1.getInt("ticket_id"));
                    ticket.setOffname(off_en);
                }

                li.add(ticket);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public FileAttribute downloadTicket(String filepath, int attid) throws Exception {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        File f = null;
        FileAttribute fa = new FileAttribute();
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT * FROM ticket_attachment WHERE attachment_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, attid);
            rs = pst.executeQuery();
            if (rs.next()) {
                String dirpath = filepath + "/" + rs.getString("d_file_name");
                f = new File(dirpath);
                if (f.exists()) {
                    fa.setDiskFileName(rs.getString("d_file_name"));
                    fa.setOriginalFileName(rs.getString("o_file_name"));
                    fa.setFileType(rs.getString("file_type"));
                    fa.setUploadAttachment(f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fa;
    }

    @Override
    public List onlineticketStatelist(String fdate, String tdate) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        List li = new ArrayList();
        OnlineTicketing ticket = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT ticket_id,user_id,created_date_time,message,closed_date_time, "
                    + "status,over_due_date_time,state_id,duration_for_reply,reopen_date_time,assigned_to_user_id,topic,ticket.topic_id,off_en,off.ddo_code,ticket.dist_code,ticket.dept_code FROM ticket  "
                    + "LEFT OUTER JOIN user_details userdet ON ticket.user_id=userdet.username "
                    + "LEFT OUTER JOIN emp_mast ON userdet.linkid=emp_mast.emp_id "
                    + "LEFT OUTER JOIN g_office off ON emp_mast.cur_off_code=off.off_code LEFT OUTER JOIN g_ticket_topic on g_ticket_topic.topic_id=ticket.topic_id "
                    + " WHERE (created_date_time BETWEEN ? AND ?) AND status=? ORDER BY topic_id");
            ps.setTimestamp(1, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fdate).getTime()));
            ps.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy H:m:s").parse(tdate + " 23:59:59").getTime()));
            ps.setString(3, "Inprogress");
            rs = ps.executeQuery();
            while (rs.next()) {
                ticket = new OnlineTicketing();
                ticket.setTopicName(rs.getString("topic"));
                ticket.setUserId(rs.getString("user_id"));
                String tickId = rs.getInt("ticket_id") + "";
                String FormattickId = String.format("%5s", tickId).replace(' ', '0');
                ticket.setFticketid(FormattickId);
                ticket.setTicketId(rs.getInt("ticket_id"));
                if (rs.getTimestamp("created_date_time") != null) {
                    ticket.setCreatedDateTime(rs.getDate("created_date_time"));
                    ticket.setCreatedDateTimeString(rs.getString("created_date_time"));
                } else {
                    ticket.setCreatedDateTime(null);
                }

                ticket.setMessage(rs.getString("message"));

                String Status = rs.getString("status");
                if (Status == "" || Status == null) {
                    ticket.setStatus("Open");
                } else {
                    ticket.setStatus(rs.getString("status"));
                }
                ticket.setAssignedToUserId(rs.getString("state_id"));
                ticket.setDdoCode(rs.getString("ddo_code"));
                ticket.setTopicId(rs.getInt("topic_id") + "");
                String off_en = rs.getString("off_en");

                ticket.setDeptCode(rs.getString("dept_code"));

                ps1 = con.prepareStatement("SELECT * FROM g_district WHERE dist_code=? ");
                ps1.setString(1, rs.getString("dist_code"));

                rs1 = ps1.executeQuery();
                while (rs1.next()) {
                    ticket.setDistCode(rs1.getString("dist_name"));
                    if (rs.getString("dept_code") == null) {
                        String DistName = rs1.getString("dist_name");
                        off_en = "District Coordinator " + DistName;
                        // off_en="GENERAL ADMINISTRATION AND PUBLIC GRIEVANCE DEPARTMENT,GOVERNMENT OF ODISHA";
                    }
                }
                ticket.setOffname(off_en);

                li.add(ticket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List ticketActionState(int ticketId) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        List li = new ArrayList();
        OnlineTicketing ticket = null;
        try {
            con = dataSource.getConnection();
            ps1 = con.prepareStatement("SELECT * FROM ticket_log WHERE ticket_id=? ORDER BY log_id DESC ");
            ps1.setInt(1, ticketId);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                ticket = new OnlineTicketing();
                if (rs1.getTimestamp("entery_date_time") != null) {
                    ticket.setCreatedDateTime(new Date(rs1.getTimestamp("entery_date_time").getTime()));
                } else {
                    ticket.setCreatedDateTime(null);
                }

                ticket.setMessage(rs1.getString("message"));
                ticket.setUsername(rs1.getString("user_name"));
                ticket.setUserType(rs1.getString("user_type"));
                ticket.setAttachmentId(rs1.getInt("attachment_id"));
                ps = con.prepareStatement("SELECT ticket_id,user_id,ticket.emp_id,created_date_time,message,closed_date_time,concat(f_name,' ',m_name,' ',l_name) as name, "
                        + "status,over_due_date_time,state_id,duration_for_reply,reopen_date_time,assigned_to_user_id,topic,ticket.topic_id,off_en FROM ticket  "
                        + "LEFT OUTER JOIN user_details userdet ON ticket.user_id=userdet.username "
                        + "LEFT OUTER JOIN emp_mast ON userdet.linkid=emp_mast.emp_id "
                        + "LEFT OUTER JOIN g_office off ON emp_mast.cur_off_code=off.off_code LEFT OUTER JOIN g_ticket_topic on g_ticket_topic.topic_id=ticket.topic_id  WHERE ticket.status=? AND ticket_id=?");
                ps.setString(1, "Inprogress");
                ps.setInt(2, ticketId);
                rs = ps.executeQuery();
                while (rs.next()) {

                    String topic = rs.getString("topic");
                    String tickId = rs.getInt("ticket_id") + "";
                    String FormattickId = String.format("%5s", tickId).replace(' ', '0');
                    String off_en = rs.getString("off_en");
                    ticket.setStatus(rs.getString("status"));
                    ticket.setUserId(rs.getString("emp_id"));
                    ticket.setAssignedToUserId(rs.getString("state_id"));
                    ps = con.prepareStatement("SELECT * FROM ticket_attachment WHERE ref_id=?");
                    ps.setInt(1, ticketId);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        ticket.setOfileName(rs.getString("o_file_name"));
                        ticket.setFilePath(rs.getString("file_path"));
                        ticket.setFileType(rs.getString("file_type"));
                        ticket.setDfileName(rs.getString("d_file_name"));
                    }

                    ticket.setFticketid(FormattickId);
                    ticket.setTopicName(topic);
                    ticket.setTicketId(rs1.getInt("ticket_id"));
                    ticket.setOffname(off_en);
                }

                li.add(ticket);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void SaveonlineticketState(OnlineTicketing ticket) {

        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        PreparedStatement pst = null;
        String createdDateTime = "";
        String overDueDateTime = "";

        try {
            con = dataSource.getConnection();

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
            createdDateTime = dateFormat.format(cal.getTime());
            cal.add(Calendar.DAY_OF_YEAR, 1);
            overDueDateTime = dateFormat.format(cal.getTime());
            int tktId = ticket.getTicketId();

            ps = con.prepareStatement("UPDATE ticket SET status=?,state_id=? WHERE ticket_id=?");
            ps.setString(1, ticket.getStatus());
            ps.setString(2, ticket.getUsername());
            ps.setInt(3, ticket.getTicketId());
            ps.executeUpdate();
            int attacId = 0;
            if (ticket.getOfileName() != null && !ticket.getOfileName().equals("")) {
                attacId = CommonFunctions.getMaxCode(con, "ticket_attachment", "attachment_id");
                pst = con.prepareStatement("INSERT INTO ticket_attachment (attachment_id,o_file_name,d_file_name,ref_id,ref_type,file_path,file_type) VALUES (?,?,?,?,?,?,?)");
                pst.setInt(1, attacId);
                pst.setString(2, ticket.getOfileName());
                pst.setString(3, ticket.getDfileName());
                pst.setInt(4, tktId);
                pst.setString(5, ticket.getRefType());
                pst.setString(6, ticket.getFilePath());
                pst.setString(7, ticket.getFileType());
                pst.execute();
            }
            int logId = CommonFunctions.getMaxCode(con, "ticket_log", "log_id");
            ps = con.prepareStatement("INSERT INTO ticket_log (log_id,ticket_id,attachment_id,entery_date_time,message,"
                    + "user_type,user_name,login_id,status_log ) VALUES (?,?,?,?,?,?,?,?,?) ");
            ps.setInt(1, logId);
            ps.setInt(2, tktId);
            ps.setInt(3, attacId);
            ps.setTimestamp(4, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            ps.setString(5, ticket.getMessage());
            ps.setString(6, ticket.getUserType());
            ps.setString(7, ticket.getUsername());
            ps.setString(8, ticket.getLoginId());
            ps.setString(9, ticket.getStatus());
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(ps, pst);

        }
    }

    @Override
    public void SaveonlineticketByDc(OnlineTicketing ticket) {

        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        PreparedStatement pst = null;
        String createdDateTime = "";
        String overDueDateTime = "";

        try {
            con = dataSource.getConnection();
            /*pst = con.prepareStatement("select * from user_details where linkid='59001114'");
             rs=pst.executeQuery();
             if(){
                
             }
             */
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
            createdDateTime = dateFormat.format(cal.getTime());
            cal.add(Calendar.DAY_OF_YEAR, 1);
            overDueDateTime = dateFormat.format(cal.getTime());
            int tktId = CommonFunctions.getMaxCode(con, "ticket", "ticket_id");
            ps = con.prepareStatement("INSERT INTO ticket (ticket_id,user_id,created_date_time,message,closed_date_time,"
                    + "status,over_due_date_time,duration_for_reply,reopen_date_time,assigned_to_user_id,topic_id,dist_code) VALUES (?,?,?,?,?,?,?,?,?,?,?,?) ");

            ps.setInt(1, tktId);
            ps.setString(2, ticket.getUserId());
            ps.setTimestamp(3, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            ps.setString(4, ticket.getMessage());
            ps.setTimestamp(5, null);
            if (ticket.getForward() != null && ticket.getForward().equals("Self Closed")) {
                ps.setString(6, "Open");
            } else {
                ps.setString(6, "Inprogress");
            }
            ps.setTimestamp(7, new Timestamp(dateFormat.parse(overDueDateTime).getTime()));
            ps.setDouble(8, 0);

            ps.setTimestamp(9, null);
            ps.setString(10, ticket.getAssignedToUserId());
            ps.setInt(11, Integer.parseInt(ticket.getTopicId()));
            ps.setString(12, ticket.getDistCode());
            ps.execute();

            int attacId = 0;
            if (ticket.getOfileName() != null && !ticket.getOfileName().equals("")) {
                attacId = CommonFunctions.getMaxCode(con, "ticket_attachment", "attachment_id");
                pst = con.prepareStatement("INSERT INTO ticket_attachment (attachment_id,o_file_name,d_file_name,ref_id,ref_type,file_path,file_type) VALUES (?,?,?,?,?,?,?)");
                pst.setInt(1, attacId);
                pst.setString(2, ticket.getOfileName());
                pst.setString(3, ticket.getDfileName());
                pst.setInt(4, tktId);
                pst.setString(5, ticket.getRefType());
                pst.setString(6, ticket.getFilePath());
                pst.setString(7, ticket.getFileType());
                pst.execute();
            }
            int logId = CommonFunctions.getMaxCode(con, "ticket_log", "log_id");
            ps = con.prepareStatement("INSERT INTO ticket_log (log_id,ticket_id,attachment_id,entery_date_time,message,"
                    + "user_type,user_name,status_log ) VALUES (?,?,?,?,?,?,?,?) ");
            ps.setInt(1, logId);
            ps.setInt(2, tktId);
            ps.setInt(3, attacId);
            ps.setTimestamp(4, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            ps.setString(5, ticket.getMessage());
            ps.setString(6, "HRMS Staff(DC)");
            ps.setString(7, ticket.getUsername());
            ps.setString(8, "Inprogress");
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(ps, pst);
        }
    }

    @Override
    public void SaveonlineticketByDDO(OnlineTicketing ticket) {

        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        PreparedStatement pst = null;
        String createdDateTime = "";
        String overDueDateTime = "";

        try {
            con = dataSource.getConnection();
            /*pst = con.prepareStatement("select * from user_details where linkid='59001114'");
             rs=pst.executeQuery();
             if(){
                
             }
             */
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
            createdDateTime = dateFormat.format(cal.getTime());
            cal.add(Calendar.DAY_OF_YEAR, 1);
            overDueDateTime = dateFormat.format(cal.getTime());
            int tktId = CommonFunctions.getMaxCode(con, "ticket", "ticket_id");
            ps = con.prepareStatement("INSERT INTO ticket (ticket_id,user_id,created_date_time,message,closed_date_time,"
                    + "status,over_due_date_time,duration_for_reply,reopen_date_time,assigned_to_user_id,topic_id,dist_code) VALUES (?,?,?,?,?,?,?,?,?,?,?,?) ");

            ps.setInt(1, tktId);
            ps.setString(2, ticket.getUserId());
            ps.setTimestamp(3, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            ps.setString(4, ticket.getMessage());
            ps.setTimestamp(5, null);
            if (ticket.getForward() != null && ticket.getForward().equals("Self Closed")) {
                ps.setString(6, "Open");
            } else {
                ps.setString(6, "Inprogress");
            }
            ps.setTimestamp(7, new Timestamp(dateFormat.parse(overDueDateTime).getTime()));
            ps.setDouble(8, 0);

            ps.setTimestamp(9, null);
            ps.setString(10, ticket.getAssignedToUserId());
            ps.setInt(11, Integer.parseInt(ticket.getTopicId()));
            ps.setString(12, ticket.getDistCode());
            ps.execute();

            int attacId = 0;
            if (ticket.getOfileName() != null && !ticket.getOfileName().equals("")) {
                attacId = CommonFunctions.getMaxCode(con, "ticket_attachment", "attachment_id");
                pst = con.prepareStatement("INSERT INTO ticket_attachment (attachment_id,o_file_name,d_file_name,ref_id,ref_type,file_path,file_type) VALUES (?,?,?,?,?,?,?)");
                pst.setInt(1, attacId);
                pst.setString(2, ticket.getOfileName());
                pst.setString(3, ticket.getDfileName());
                pst.setInt(4, tktId);
                pst.setString(5, ticket.getRefType());
                pst.setString(6, ticket.getFilePath());
                pst.setString(7, ticket.getFileType());
                pst.execute();
            }
            int logId = CommonFunctions.getMaxCode(con, "ticket_log", "log_id");
            ps = con.prepareStatement("INSERT INTO ticket_log (log_id,ticket_id,attachment_id,entery_date_time,message,"
                    + "user_type,user_name,status_log ) VALUES (?,?,?,?,?,?,?,?) ");
            ps.setInt(1, logId);
            ps.setInt(2, tktId);
            ps.setInt(3, attacId);
            ps.setTimestamp(4, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            ps.setString(5, ticket.getMessage());
            ps.setString(6, "HRMS Staff(DC)");
            ps.setString(7, ticket.getUsername());
            ps.setString(8, "Inprogress");
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(ps, pst);
        }
    }

    @Override
    public void ticketTakeOver(int ticketId, String loginId) {

        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        PreparedStatement pst = null;
        String createdDateTime = "";
        String overDueDateTime = "";

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("UPDATE ticket SET state_id=? WHERE ticket_id=? AND state_id IS NULL ");
            ps.setString(1, loginId);
            ps.setInt(2, ticketId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(ps, pst);
        }
    }

    @Override
    public List reportOnlineTicketStatelist(OnlineTicketing ticket) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        List li = new ArrayList();
        String month = ticket.getMonth();
        String year = ticket.getYear();
        String dist = ticket.getSltdistName();
        try {
            con = dataSource.getConnection();
            if ((dist != null && !dist.equals("")) && (year != null && !year.equals("")) && (month != null && !month.equals(""))) {
                ps = con.prepareStatement("SELECT ticket_id,user_id,created_date_time,message,closed_date_time,(current_date::date-created_date_time::date)pendingdays,state_id,ticket.dist_code,dept_code,\n"
                        + "status,over_due_date_time,duration_for_reply,reopen_date_time,assigned_to_user_id,topic,ticket.topic_id,off_en FROM ticket\n"
                        + "LEFT OUTER JOIN user_details userdet ON ticket.user_id=userdet.username\n"
                        + "LEFT OUTER JOIN emp_mast ON userdet.linkid=emp_mast.emp_id\n"
                        + "LEFT OUTER JOIN g_office off ON emp_mast.cur_off_code=off.off_code LEFT OUTER JOIN g_ticket_topic on g_ticket_topic.topic_id=ticket.topic_id  WHERE\n"
                        + "date_part('year',created_date_time)=? and date_part('month',created_date_time)=? and ticket.dist_code=? and (ticket.status = 'Open' or ticket.status='Reopened' or ticket.status='Inprogress' or status is NULL ) order by created_date_time");
                ps.setInt(1, Integer.parseInt(year));
                ps.setInt(2, Integer.parseInt(month));
                ps.setString(3, dist);
                rs = ps.executeQuery();
                while (rs.next()) {
                    ticket = new OnlineTicketing();
                    ticket.setTopicName(rs.getString("topic"));
                    ticket.setUserId(rs.getString("user_id"));
                    String tickId = rs.getInt("ticket_id") + "";
                    String FormattickId = String.format("%5s", tickId).replace(' ', '0');
                    ticket.setFticketid(FormattickId);
                    ticket.setTicketId(rs.getInt("ticket_id"));
                    if (rs.getTimestamp("created_date_time") != null) {
                        ticket.setCreatedDateTime(new Date(rs.getTimestamp("created_date_time").getTime()));
                    } else {
                        ticket.setCreatedDateTime(null);
                    }

                    ticket.setMessage(rs.getString("message"));
                    ticket.setPendingdays(rs.getString("pendingdays") + " Days");

                    String Status = rs.getString("status");
                    if (Status == "" || Status == null) {
                        ticket.setStatus("Open");
                    } else {
                        ticket.setStatus(rs.getString("status"));
                    }
                    ticket.setAssignedToUserId(rs.getString("state_id"));
                    ticket.setTopicId(rs.getInt("topic_id") + "");
                    String off_en = rs.getString("off_en");

                    ticket.setDeptCode(rs.getString("dept_code"));

                    ps1 = con.prepareStatement("SELECT * FROM g_district WHERE dist_code=? ");
                    ps1.setString(1, rs.getString("dist_code"));

                    rs1 = ps1.executeQuery();
                    while (rs1.next()) {
                        ticket.setDistCode(rs1.getString("dist_name"));
                        if (rs.getString("dept_code") == null) {
                            String DistName = rs1.getString("dist_name");
                            off_en = "District Coordinator " + DistName;
                            // off_en="GENERAL ADMINISTRATION AND PUBLIC GRIEVANCE DEPARTMENT,GOVERNMENT OF ODISHA";
                        }
                    }
                    ticket.setOffname(off_en);

                    li.add(ticket);
                }
            } else if ((month != null && !month.equals("")) && (year != null && !year.equals(""))) {
                ps = con.prepareStatement("SELECT ticket_id,user_id,created_date_time,message,closed_date_time,state_id,(current_date::date-created_date_time::date)pendingdays,ticket.dist_code,dept_code,\n"
                        + "status,over_due_date_time,duration_for_reply,reopen_date_time,assigned_to_user_id,topic,ticket.topic_id,off_en FROM ticket\n"
                        + "LEFT OUTER JOIN user_details userdet ON ticket.user_id=userdet.username\n"
                        + "LEFT OUTER JOIN emp_mast ON userdet.linkid=emp_mast.emp_id\n"
                        + "LEFT OUTER JOIN g_office off ON emp_mast.cur_off_code=off.off_code LEFT OUTER JOIN g_ticket_topic on g_ticket_topic.topic_id=ticket.topic_id  WHERE\n"
                        + "date_part('year',created_date_time)=? and date_part('month',created_date_time)=? and (ticket.status = 'Open' or ticket.status='Reopened' or ticket.status='Inprogress' or status is NULL ) order by created_date_time");
                ps.setInt(1, Integer.parseInt(year));
                ps.setInt(2, Integer.parseInt(month));
                rs = ps.executeQuery();
                while (rs.next()) {
                    ticket = new OnlineTicketing();
                    ticket.setTopicName(rs.getString("topic"));
                    ticket.setUserId(rs.getString("user_id"));
                    String tickId = rs.getInt("ticket_id") + "";
                    String FormattickId = String.format("%5s", tickId).replace(' ', '0');
                    ticket.setFticketid(FormattickId);
                    ticket.setTicketId(rs.getInt("ticket_id"));
                    if (rs.getTimestamp("created_date_time") != null) {
                        ticket.setCreatedDateTime(new Date(rs.getTimestamp("created_date_time").getTime()));
                    } else {
                        ticket.setCreatedDateTime(null);
                    }

                    ticket.setMessage(rs.getString("message"));
                    ticket.setPendingdays(rs.getString("pendingdays") + " Days");

                    String Status = rs.getString("status");
                    if (Status == "" || Status == null) {
                        ticket.setStatus("Open");
                    } else {
                        ticket.setStatus(rs.getString("status"));
                    }
                    ticket.setAssignedToUserId(rs.getString("state_id"));
                    ticket.setTopicId(rs.getInt("topic_id") + "");
                    String off_en = rs.getString("off_en");

                    ticket.setDeptCode(rs.getString("dept_code"));

                    ps1 = con.prepareStatement("SELECT * FROM g_district WHERE dist_code=? ");
                    ps1.setString(1, rs.getString("dist_code"));

                    rs1 = ps1.executeQuery();
                    while (rs1.next()) {
                        ticket.setDistCode(rs1.getString("dist_name"));
                        if (rs.getString("dept_code") == null) {
                            String DistName = rs1.getString("dist_name");
                            off_en = "District Coordinator " + DistName;
                            // off_en="GENERAL ADMINISTRATION AND PUBLIC GRIEVANCE DEPARTMENT,GOVERNMENT OF ODISHA";
                        }
                    }
                    ticket.setOffname(off_en);

                    li.add(ticket);
                }
            } else if ((dist != null && !dist.equals("")) && (year != null && !year.equals(""))) {
                ps = con.prepareStatement("SELECT ticket_id,user_id,created_date_time,message,closed_date_time,state_id,(current_date::date-created_date_time::date)pendingdays,ticket.dist_code,dept_code,\n"
                        + "status,over_due_date_time,duration_for_reply,reopen_date_time,assigned_to_user_id,topic,ticket.topic_id,off_en FROM ticket\n"
                        + "LEFT OUTER JOIN user_details userdet ON ticket.user_id=userdet.username\n"
                        + "LEFT OUTER JOIN emp_mast ON userdet.linkid=emp_mast.emp_id\n"
                        + "LEFT OUTER JOIN g_office off ON emp_mast.cur_off_code=off.off_code LEFT OUTER JOIN g_ticket_topic on g_ticket_topic.topic_id=ticket.topic_id  WHERE\n"
                        + "date_part('year',created_date_time)=?  and ticket.dist_code=? and (ticket.status = 'Open' or ticket.status='Reopened' or ticket.status='Inprogress' or status is NULL ) order by created_date_time");
                ps.setInt(1, Integer.parseInt(year));
                ps.setString(2, dist);
                rs = ps.executeQuery();
                while (rs.next()) {
                    ticket = new OnlineTicketing();
                    ticket.setTopicName(rs.getString("topic"));
                    ticket.setUserId(rs.getString("user_id"));
                    String tickId = rs.getInt("ticket_id") + "";
                    String FormattickId = String.format("%5s", tickId).replace(' ', '0');
                    ticket.setFticketid(FormattickId);
                    ticket.setTicketId(rs.getInt("ticket_id"));
                    if (rs.getTimestamp("created_date_time") != null) {
                        ticket.setCreatedDateTime(new Date(rs.getTimestamp("created_date_time").getTime()));
                    } else {
                        ticket.setCreatedDateTime(null);
                    }

                    ticket.setMessage(rs.getString("message"));
                    ticket.setPendingdays(rs.getString("pendingdays") + " Days");

                    String Status = rs.getString("status");
                    if (Status == "" || Status == null) {
                        ticket.setStatus("Open");
                    } else {
                        ticket.setStatus(rs.getString("status"));
                    }
                    ticket.setAssignedToUserId(rs.getString("state_id"));
                    ticket.setTopicId(rs.getInt("topic_id") + "");
                    String off_en = rs.getString("off_en");

                    ticket.setDeptCode(rs.getString("dept_code"));

                    ps1 = con.prepareStatement("SELECT * FROM g_district WHERE dist_code=? ");
                    ps1.setString(1, rs.getString("dist_code"));

                    rs1 = ps1.executeQuery();
                    while (rs1.next()) {
                        ticket.setDistCode(rs1.getString("dist_name"));
                        if (rs.getString("dept_code") == null) {
                            String DistName = rs1.getString("dist_name");
                            off_en = "District Coordinator " + DistName;
                            // off_en="GENERAL ADMINISTRATION AND PUBLIC GRIEVANCE DEPARTMENT,GOVERNMENT OF ODISHA";
                        }
                    }
                    ticket.setOffname(off_en);

                    li.add(ticket);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getOnlineTicketDataReport(String logindistcode, OnlineTicketing ticket) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List onlineticketreportlist = new ArrayList();

        OnlineTicketing oticketbean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select dist_name,state_id,off_en,g_office.ddo_code,topic,ticket.* from ticket"
                    + " inner join g_ticket_topic on ticket.topic_id=g_ticket_topic.topic_id"
                    + " left outer join g_office on ticket.dept_code=g_office.off_code"
                    + " left outer join g_district on ticket.dist_code=g_district.dist_code"
                    + " where ticket.dist_code=? and extract(year from created_date_time)=? and extract(month from created_date_time)=? order by created_date_time desc";
            pst = con.prepareStatement(sql);
            pst.setString(1, logindistcode);
            pst.setInt(2, Integer.parseInt(ticket.getYear()));
            pst.setInt(3, Integer.parseInt(ticket.getMonth()));
            rs = pst.executeQuery();
            while (rs.next()) {
                oticketbean = new OnlineTicketing();
                oticketbean.setTicketId(rs.getInt("ticket_id"));
                oticketbean.setCreatedDateTimeString(CommonFunctions.getFormattedOutputDate1(rs.getDate("created_date_time")));
                oticketbean.setTopicName(rs.getString("topic"));
                oticketbean.setMessage(rs.getString("message"));
                oticketbean.setOffname(rs.getString("off_en"));
                if (rs.getString("state_id") != null) {
                    oticketbean.setStatus(rs.getString("status") + "(State team)");
                } else {
                    oticketbean.setStatus(rs.getString("status"));
                }

                oticketbean.setDdoCode(rs.getString("ddo_code"));
                onlineticketreportlist.add(oticketbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return onlineticketreportlist;
    }

    @Override
    public List misreportforDC(int month, int year) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        List li = new ArrayList();
        OnlineTicketing ticket = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select username,linkid,dist_name from user_details LEFT OUTER JOIN  g_district ON  linkid=dist_code WHERE  usertype='D' and username like 'dc%' ORDER BY dist_name");
            rs = ps.executeQuery();
            String linkid = "";
            String username = "";
            String totaldisposed = "0";
            String totalsent = "0";
            String totalTicket = "0";
            while (rs.next()) {

                ticket = new OnlineTicketing();
                ticket.setDistCode(rs.getString("dist_name"));
                ticket.setUsername(rs.getString("username"));
                username = rs.getString("username");

                linkid = rs.getString("linkid");
                ps1 = con.prepareStatement("SELECT count(*) as totalTicket FROM ticket WHERE dist_code=? AND EXTRACT(MONTH FROM created_date_time) =? AND  EXTRACT(YEAR FROM created_date_time) =? ");
                ps1.setString(1, linkid);
                ps1.setInt(2, month);
                ps1.setInt(3, year);
                //  System.out.println("month="+month+"year="+year);
                rs1 = ps1.executeQuery();

                while (rs1.next()) {
                    totalTicket = rs1.getString("totalTicket");
                }
                ticket.setTotalticketReceived(Integer.parseInt(totalTicket));

                // ps2 = con.prepareStatement("select count(*) as totaldisposed from (SELECT ticket_log.ticket_id FROM ticket_log INNER JOIN ticket ON ticket_log.ticket_id=ticket.ticket_id WHERE  EXTRACT(MONTH FROM created_date_time) =? AND  EXTRACT(YEAR FROM created_date_time) =? AND login_id=? AND (ticket.status=? OR ticket.status=?)  AND state_id IS NULL GROUP BY ticket_log.ticket_id ) temp ");
                //  ps2 = con.prepareStatement("select count(*) as totaldisposed from (SELECT ticket_log.ticket_id FROM ticket_log INNER JOIN ticket ON ticket_log.ticket_id=ticket.ticket_id WHERE  EXTRACT(MONTH FROM entery_date_time) =? AND  EXTRACT(YEAR FROM entery_date_time) =? AND login_id=? AND (ticket_log.status_log=? OR ticket_log.status_log=? )  AND user_type='HRMS Staff(District Team)' GROUP BY ticket_log.ticket_id ) temp ");
                ps2 = con.prepareStatement("select count(*) as totaldisposed from (SELECT ticket_log.ticket_id FROM ticket_log INNER JOIN ticket ON ticket_log.ticket_id=ticket.ticket_id WHERE  EXTRACT(MONTH FROM entery_date_time) =? AND  EXTRACT(YEAR FROM entery_date_time) =? AND login_id=? AND (ticket_log.status_log=? OR ticket_log.status_log=? )  AND user_type='HRMS Staff(District Team)' GROUP BY ticket_log.ticket_id ) temp ");
                ps2.setInt(1, month);
                ps2.setInt(2, year);
                ps2.setString(3, username);
                ps2.setString(4, "Resolved");
                ps2.setString(5, "Closed");

                rs2 = ps2.executeQuery();

                while (rs2.next()) {
                    totaldisposed = rs2.getString("totaldisposed");
                }

                ticket.setTotalticketdisposed(Integer.parseInt(totaldisposed));

                ps1 = con.prepareStatement("select count(*) as totalsent from (SELECT ticket_log.ticket_id FROM ticket_log INNER JOIN ticket ON ticket_log.ticket_id=ticket.ticket_id WHERE  EXTRACT(MONTH FROM entery_date_time) =? AND  EXTRACT(YEAR FROM entery_date_time) =? AND login_id=?   AND (ticket_log.status_log='Inprogress') GROUP BY ticket_log.ticket_id ) as temp1 ");
                ps1.setInt(1, month);
                ps1.setInt(2, year);
                ps1.setString(3, username);
                rs1 = ps1.executeQuery();

                while (rs1.next()) {
                    totalsent = rs1.getString("totalsent");
                }
                ticket.setTotalticketdisposed(Integer.parseInt(totaldisposed));
                ticket.setTotalticketsent(Integer.parseInt(totalsent));

                li.add(ticket);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(rs1, ps1);
            DataBaseFunctions.closeSqlObjects(rs2, ps2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List GetStateMISReport(int month, int year) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        List li = new ArrayList();
        OnlineTicketing ticket = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select username,linkid,full_name from user_details "
                    + "inner join user_master on user_details.linkid=user_master.user_id::text WHERE "
                    + "usertype='A' AND enable=1 AND username LIKE '%support%' ORDER BY username");
            rs = ps.executeQuery();
            String linkid = "";
            String username = "";
            String fullname = "";
            String totaldisposed = "0";
            String totalsent = "0";
            String totalTicket = "0";
            while (rs.next()) {
                ticket = new OnlineTicketing();
                ticket.setUsername(rs.getString("username"));
                ticket.setFullName(rs.getString("full_name"));
                username = rs.getString("username");
                fullname = rs.getString("full_name");
                linkid = rs.getString("linkid");

                ps2 = con.prepareStatement("select count(*) as totaldisposed from (SELECT ticket_log.ticket_id FROM ticket_log INNER JOIN ticket ON ticket_log.ticket_id=ticket.ticket_id WHERE  EXTRACT(MONTH FROM entery_date_time) =? AND  EXTRACT(YEAR FROM entery_date_time) =? AND login_id=? AND (ticket_log.status_log=? OR ticket_log.status_log=?)  AND state_id IS NOT NULL GROUP BY ticket_log.ticket_id ) temp ");
                ps2.setInt(1, month);
                ps2.setInt(2, year);
                ps2.setString(3, username);
                ps2.setString(4, "Resolved");
                ps2.setString(5, "Closed");
                rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    totaldisposed = rs2.getString("totaldisposed");
                }
                ticket.setTotalticketdisposed(Integer.parseInt(totaldisposed));
                ps1 = con.prepareStatement("select count(*) as totalsent from (SELECT ticket_log.ticket_id FROM ticket_log INNER JOIN ticket ON ticket_log.ticket_id=ticket.ticket_id WHERE  EXTRACT(MONTH FROM entery_date_time) =? AND  EXTRACT(YEAR FROM entery_date_time) =? AND login_id=? AND ticket_log.status_log=?   AND state_id IS NOT  NULL GROUP BY ticket_log.ticket_id ) as temp1 ");
                ps1.setInt(1, month);
                ps1.setInt(2, year);
                ps1.setString(3, username);
                ps1.setString(4, "Open");
                rs1 = ps1.executeQuery();

                while (rs1.next()) {
                    totalsent = rs1.getString("totalsent");
                }
                ticket.setTotalticketdisposed(Integer.parseInt(totaldisposed));
                ticket.setTotalticketsent(Integer.parseInt(totalsent));

                li.add(ticket);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(rs1, ps1);
            DataBaseFunctions.closeSqlObjects(rs2, ps2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List viewActionDetails(String userId, int ticketId, String loginId) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        List li = new ArrayList();
        OnlineTicketing ticket = null;
        try {
            con = dataSource.getConnection();

            ps1 = con.prepareStatement("SELECT a.* FROM ticket_log a ,ticket b  WHERE a.ticket_id=b.ticket_id AND  a.ticket_id=? AND b.dist_code=? ORDER BY log_id DESC ");
            ps1.setInt(1, ticketId);
            ps1.setString(2, userId);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                ticket = new OnlineTicketing();
                if (rs1.getTimestamp("entery_date_time") != null) {
                    ticket.setCreatedDateTime(new Date(rs1.getTimestamp("entery_date_time").getTime()));
                } else {
                    ticket.setCreatedDateTime(null);
                }

                ticket.setMessage(rs1.getString("message"));
                ticket.setUsername(rs1.getString("user_name"));
                ticket.setUserType(rs1.getString("user_type"));
                ticket.setAttachmentId(rs1.getInt("attachment_id"));
                if (loginId.equals("dcsecretariate") || loginId.equals("dcsecretariate2")) {
                    ps = con.prepareStatement("SELECT ticket_id,created_date_time,user_id,ticket.emp_id,created_date_time,message,closed_date_time,concat(f_name,' ',m_name,' ',l_name) as name, "
                            + "status,over_due_date_time,duration_for_reply,ticket.dept_code,reopen_date_time,assigned_to_user_id,topic,ticket.topic_id,off_en FROM ticket  "
                            + "LEFT OUTER JOIN user_details userdet ON ticket.user_id=userdet.username "
                            + "LEFT OUTER JOIN emp_mast ON userdet.linkid=emp_mast.emp_id "
                            + "LEFT OUTER JOIN g_office off ON emp_mast.cur_off_code=off.off_code LEFT OUTER JOIN g_ticket_topic on g_ticket_topic.topic_id=ticket.topic_id  WHERE  ticket_id=?");
                    // ps.setString(1, userId);
                    ps.setInt(1, ticketId);

                } else {
                    ps = con.prepareStatement("SELECT ticket_id,created_date_time,user_id,ticket.emp_id,created_date_time,message,closed_date_time,concat(f_name,' ',m_name,' ',l_name) as name, "
                            + "status,over_due_date_time,duration_for_reply,ticket.dept_code,reopen_date_time,assigned_to_user_id,topic,ticket.topic_id,off_en FROM ticket  "
                            + "LEFT OUTER JOIN user_details userdet ON ticket.user_id=userdet.username "
                            + "LEFT OUTER JOIN emp_mast ON userdet.linkid=emp_mast.emp_id "
                            + "LEFT OUTER JOIN g_office off ON emp_mast.cur_off_code=off.off_code LEFT OUTER JOIN g_ticket_topic on g_ticket_topic.topic_id=ticket.topic_id  WHERE ticket.dist_code=? AND ticket_id=?");
                    ;
                    ps.setString(1, userId);
                    ps.setInt(2, ticketId);
                }
                rs = ps.executeQuery();
                while (rs.next()) {

                    String topic = rs.getString("topic");
                    String tickId = rs.getInt("ticket_id") + "";
                    String FormattickId = String.format("%5s", tickId).replace(' ', '0');
                    String off_en = rs.getString("off_en");
                    ticket.setDeptCode(rs.getString("dept_code"));
                    //  ticket.setCreatedDateTime(rs.getDate("created_date_time"));
                    ticket.setUserId(rs.getString("emp_id"));
                    ticket.setStatus(rs.getString("status"));
                    ps = con.prepareStatement("SELECT * FROM ticket_attachment WHERE ref_id=?");
                    ps.setInt(1, ticketId);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        ticket.setOfileName(rs.getString("o_file_name"));
                        ticket.setFilePath(rs.getString("file_path"));
                        ticket.setFileType(rs.getString("file_type"));
                        ticket.setDfileName(rs.getString("d_file_name"));
                    }

                    ticket.setFticketid(FormattickId);
                    ticket.setTopicName(topic);
                    ticket.setTicketId(rs1.getInt("ticket_id"));
                    ticket.setOffname(off_en);
                }

                li.add(ticket);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getknowledgebase(String topicId, String subtopic) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        OnlineTicketing ticket = null;
        SimpleDateFormat sdf = new SimpleDateFormat("EEEEE MMMMM yyyy hh:mm:ss a");
        int InttopicId = Integer.parseInt(topicId);
        int Intsubtopic = Integer.parseInt(subtopic);
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT * FROM g_ticket_knowledgebase  WHERE topic_id=? AND subtopic_id=?");
            ps.setInt(1, InttopicId);
            ps.setInt(2, Intsubtopic);
            rs = ps.executeQuery();
            while (rs.next()) {
                ticket = new OnlineTicketing();
                ticket.setTopicName(rs.getString("topic"));
                ticket.setDescription(rs.getString("description"));

                li.add(ticket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getDCPendingTicketlist() {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        List li = new ArrayList();
        OnlineTicketing ticket = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select username,linkid,dist_name from user_details LEFT OUTER JOIN  g_district ON  linkid=dist_code WHERE  usertype='D' and username like 'dc%' ORDER BY dist_name");
            rs = ps.executeQuery();
            String linkid = "";
            String username = "";

            while (rs.next()) {

                ticket = new OnlineTicketing();
                ticket.setDistCode(rs.getString("dist_name"));
                ticket.setUsername(rs.getString("username"));
                username = rs.getString("username");
                linkid = rs.getString("linkid");
                if (username.equals("dcsecretariate") || username.equals("dcsecretariate2")) {

                    ps1 = con.prepareStatement("select  (SELECT count(*) "
                            + "FROM ticket WHERE dept_code LIKE 'OLS%' AND dist_code='2117'  AND  (dept_code LIKE '%0000' OR dept_code LIKE '%OLSWAT0010001%')  AND status is NULL AND DATE_PART('day',NOW()-created_date_time) <=1 "
                            + "AND created_date_time >= '2022-01-01'  AND created_date_time <=  now() GROUP BY dist_code) as lessthan24Hours, "
                            + "(SELECT count(*) "
                            + "FROM ticket WHERE dept_code LIKE 'OLS%' AND dist_code='2117' AND  (dept_code LIKE '%0000' OR dept_code LIKE '%OLSWAT0010001%')  AND status is NULL AND (DATE_PART('day',NOW()-created_date_time) <=2 AND DATE_PART('day',NOW()-created_date_time) > 1 ) "
                            + "AND created_date_time >= '2022-01-01'  AND created_date_time <=  now() GROUP BY dist_code) as lessthan48Hours, "
                            + "(SELECT count(*) "
                            + "FROM ticket WHERE dept_code LIKE 'OLS%' AND dist_code='2117' AND  (dept_code LIKE '%0000' OR dept_code LIKE '%OLSWAT0010001%')  AND status is NULL AND (DATE_PART('day',NOW()-created_date_time) <=7 AND DATE_PART('day',NOW()-created_date_time) > 2 ) "
                            + "AND created_date_time >= '2022-01-01'  AND created_date_time <=  now() GROUP BY dist_code) as lessthan7days, "
                            + "\n"
                            + "(SELECT count(*) "
                            + "FROM ticket WHERE dept_code LIKE 'OLS%' AND dist_code='2117' AND  (dept_code LIKE '%0000' OR dept_code LIKE '%OLSWAT0010001%')  AND status is NULL AND (DATE_PART('day',NOW()-created_date_time) <=30 AND  DATE_PART('day',NOW()-created_date_time) > 7) "
                            + "AND created_date_time >= '2022-01-01'  AND created_date_time <=  now() GROUP BY dist_code) as lessthan30days, "
                            + "(SELECT count(*) "
                            + "FROM ticket WHERE dept_code LIKE 'OLS%' AND dist_code='2117'  AND  (dept_code LIKE '%0000' OR dept_code LIKE '%OLSWAT0010001%')   AND status is NULL AND DATE_PART('day',NOW()-created_date_time) > 30 "
                            + "AND created_date_time >= '2022-01-01'  AND created_date_time <=  now() GROUP BY dist_code) as morethan30days ");
                } else if (username.equals("dcsuresh") || username.equals("dcalok")) {

                    ps1 = con.prepareStatement("select  (SELECT count(*) "
                            + "FROM ticket WHERE dept_code LIKE 'BBS%' AND dist_code='2117'  AND status is NULL AND DATE_PART('day',NOW()-created_date_time) <=1 "
                            + "AND created_date_time >= '2022-01-01'  AND created_date_time <=  now() GROUP BY dist_code) as lessthan24Hours, "
                            + "(SELECT count(*) "
                            + "FROM ticket WHERE dept_code LIKE 'BBS%' AND dist_code='2117'  AND status is NULL AND (DATE_PART('day',NOW()-created_date_time) <=2 AND DATE_PART('day',NOW()-created_date_time) > 1 ) "
                            + "AND created_date_time >= '2022-01-01'  AND created_date_time <=  now() GROUP BY dist_code) as lessthan48Hours, "
                            + "(SELECT count(*) "
                            + "FROM ticket WHERE dept_code LIKE 'BBS%' AND dist_code='2117'  AND status is NULL AND (DATE_PART('day',NOW()-created_date_time) <=7 AND DATE_PART('day',NOW()-created_date_time) > 2 ) "
                            + "AND created_date_time >= '2022-01-01'  AND created_date_time <=  now() GROUP BY dist_code) as lessthan7days, "
                            + "\n"
                            + "(SELECT count(*) "
                            + "FROM ticket WHERE dept_code LIKE 'BBS%' AND dist_code='2117' AND status is NULL AND (DATE_PART('day',NOW()-created_date_time) <=30 AND  DATE_PART('day',NOW()-created_date_time) > 7) "
                            + "AND created_date_time >= '2022-01-01'  AND created_date_time <=  now() GROUP BY dist_code) as lessthan30days, "
                            + "(SELECT count(*) "
                            + "FROM ticket WHERE dept_code LIKE 'BBS%' AND dist_code='2117' AND status is NULL AND DATE_PART('day',NOW()-created_date_time) > 30 "
                            + "AND created_date_time >= '2022-01-01'  AND created_date_time <=  now() GROUP BY dist_code) as morethan30days ");
                } else {
                    ps1 = con.prepareStatement("select  (SELECT count(*) "
                            + "FROM ticket WHERE dist_code=? AND status is NULL AND DATE_PART('day',NOW()-created_date_time) <=1 "
                            + "AND created_date_time >= '2022-01-01'  AND created_date_time <=  now() GROUP BY dist_code) as lessthan24Hours, "
                            + "(SELECT count(*) "
                            + "FROM ticket WHERE dist_code=? AND status is NULL AND (DATE_PART('day',NOW()-created_date_time) <=2 AND DATE_PART('day',NOW()-created_date_time) > 1 ) "
                            + "AND created_date_time >= '2022-01-01'  AND created_date_time <=  now() GROUP BY dist_code) as lessthan48Hours, "
                            + "(SELECT count(*) "
                            + "FROM ticket WHERE dist_code=? AND status is NULL AND (DATE_PART('day',NOW()-created_date_time) <=7 AND DATE_PART('day',NOW()-created_date_time) > 2 ) "
                            + "AND created_date_time >= '2022-01-01'  AND created_date_time <=  now() GROUP BY dist_code) as lessthan7days, "
                            + "\n"
                            + "(SELECT count(*) "
                            + "FROM ticket WHERE dist_code=? AND status is NULL AND (DATE_PART('day',NOW()-created_date_time) <=30 AND  DATE_PART('day',NOW()-created_date_time) > 7) "
                            + "AND created_date_time >= '2022-01-01'  AND created_date_time <=  now() GROUP BY dist_code) as lessthan30days, "
                            + "(SELECT count(*) "
                            + "FROM ticket WHERE dist_code=? AND status is NULL AND DATE_PART('day',NOW()-created_date_time) > 30 "
                            + "AND created_date_time >= '2022-01-01'  AND created_date_time <=  now() GROUP BY dist_code) as morethan30days ");
                    ps1.setString(1, linkid);
                    ps1.setString(2, linkid);
                    ps1.setString(3, linkid);
                    ps1.setString(4, linkid);
                    ps1.setString(5, linkid);
                }

                rs1 = ps1.executeQuery();
                String totaldisposed = "0";
                String lessthan24Hours = "0";
                String lessthan48Hours = "0";
                String lessthan7days = "0";
                String lessthan30days = "0";
                String morethan30days = "0";
                while (rs1.next()) {
                    if (rs1.getString("lessthan24Hours") != null) {
                        lessthan24Hours = rs1.getString("lessthan24Hours");
                    }
                    if (rs1.getString("lessthan48Hours") != null) {
                        lessthan48Hours = rs1.getString("lessthan48Hours");
                    }
                    if (rs1.getString("lessthan7days") != null) {
                        lessthan7days = rs1.getString("lessthan7days");
                    }
                    if (rs1.getString("lessthan30days") != null) {
                        lessthan30days = rs1.getString("lessthan30days");
                    }
                    if (rs1.getString("morethan30days") != null) {
                        morethan30days = rs1.getString("morethan30days");
                    }

                }
                ticket.setLessthan24Hours(Integer.parseInt(lessthan24Hours));
                ticket.setLessthan48Hours(Integer.parseInt(lessthan48Hours));
                ticket.setLessthan7days(Integer.parseInt(lessthan7days));
                ticket.setLessthan30days(Integer.parseInt(lessthan30days));
                ticket.setMorethan30days(Integer.parseInt(morethan30days));

                li.add(ticket);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(rs1, ps1);
            DataBaseFunctions.closeSqlObjects(rs2, ps2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getTicketListByEmployee(String userId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        OnlineTicketing ticket = null;
        SimpleDateFormat sdf = new SimpleDateFormat("EEEEE MMMMM yyyy hh:mm:ss a");
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT ticket_id,user_id,created_date_time,message,closed_date_time, "
                    + "status,over_due_date_time,duration_for_reply,reopen_date_time,assigned_to_user_id,topic,ticket.topic_id,off_en FROM ticket  "
                    + "LEFT OUTER JOIN user_details userdet ON ticket.user_id=userdet.username "
                    + "LEFT OUTER JOIN emp_mast ON userdet.linkid=emp_mast.emp_id "
                    + "LEFT OUTER JOIN g_office off ON emp_mast.cur_off_code=off.off_code LEFT OUTER JOIN g_ticket_topic on g_ticket_topic.topic_id=ticket.topic_id  WHERE user_id=? ORDER BY ticket.ticket_id desc");

            ps.setString(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                ticket = new OnlineTicketing();
                ticket.setTopicName(rs.getString("topic"));
                ticket.setUserId(rs.getString("user_id"));
                String tickId = rs.getInt("ticket_id") + "";
                String FormattickId = String.format("%5s", tickId).replace(' ', '0');
                ticket.setFticketid(FormattickId);
                ticket.setTicketId(rs.getInt("ticket_id"));
                if (rs.getTimestamp("created_date_time") != null) {
                    ticket.setCreatedDateTimeString(rs.getString("created_date_time"));
                    ticket.setCreatedDateTime(new Date(rs.getTimestamp("created_date_time").getTime()));
                } else {
                    ticket.setCreatedDateTime(null);
                }

                ticket.setMessage(rs.getString("message"));

                if (rs.getTimestamp("closed_date_time") != null) {
                    ticket.setClosedDateTime(new Date(rs.getTimestamp("closed_date_time").getTime()));
                } else {
                    ticket.setClosedDateTime(null);
                }
                String Status = rs.getString("status");
                if (Status == "" || Status == null) {
                    ticket.setStatus("Open");
                } else {
                    ticket.setStatus(rs.getString("status"));
                }

                if (rs.getTimestamp("over_due_date_time") != null) {
                    ticket.setOverDueDateTime(new Date(rs.getTimestamp("over_due_date_time").getTime()));
                } else {
                    ticket.setOverDueDateTime(null);
                }
                ticket.setDurationForReply(rs.getDouble("duration_for_reply"));
                if (rs.getTimestamp("reopen_date_time") != null) {
                    ticket.setReopenDateTime(new Date(rs.getTimestamp("reopen_date_time").getTime()));
                } else {
                    ticket.setReopenDateTime(null);
                }
                ticket.setAssignedToUserId(rs.getString("assigned_to_user_id"));
                ticket.setTopicId(rs.getInt("topic_id") + "");
                ticket.setOffname(rs.getString("off_en"));
                li.add(ticket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;

    }

    @Override
    public List getTicketListByOffice(String offCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        OnlineTicketing ticket = null;
        SimpleDateFormat sdf = new SimpleDateFormat("EEEEE MMMMM yyyy hh:mm:ss a");
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT ticket_id,user_id,created_date_time,message,closed_date_time,\n"
                    + "status,over_due_date_time,duration_for_reply,reopen_date_time,assigned_to_user_id,topic,ticket.topic_id,off_en FROM ticket\n"
                    + "LEFT OUTER JOIN user_details userdet ON ticket.user_id=userdet.username\n"
                    + "LEFT OUTER JOIN emp_mast ON ticket.emp_id=emp_mast.emp_id\n"
                    + "LEFT OUTER JOIN g_office off ON emp_mast.cur_off_code=off.off_code LEFT OUTER JOIN g_ticket_topic on g_ticket_topic.topic_id=ticket.topic_id \n"
                    + "WHERE cur_off_code=? ORDER BY ticket.ticket_id desc");

            ps.setString(1, offCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                ticket = new OnlineTicketing();
                ticket.setTopicName(rs.getString("topic"));
                ticket.setUserId(rs.getString("user_id"));
                String tickId = rs.getInt("ticket_id") + "";
                String FormattickId = String.format("%5s", tickId).replace(' ', '0');
                ticket.setFticketid(FormattickId);
                ticket.setTicketId(rs.getInt("ticket_id"));
                if (rs.getTimestamp("created_date_time") != null) {
                    ticket.setCreatedDateTimeString(rs.getString("created_date_time"));
                    ticket.setCreatedDateTime(new Date(rs.getTimestamp("created_date_time").getTime()));
                } else {
                    ticket.setCreatedDateTime(null);
                }

                ticket.setMessage(rs.getString("message"));

                if (rs.getTimestamp("closed_date_time") != null) {
                    ticket.setClosedDateTime(new Date(rs.getTimestamp("closed_date_time").getTime()));
                } else {
                    ticket.setClosedDateTime(null);
                }
                String Status = rs.getString("status");
                if (Status == "" || Status == null) {
                    ticket.setStatus("Open");
                } else {
                    ticket.setStatus(rs.getString("status"));
                }

                if (rs.getTimestamp("over_due_date_time") != null) {
                    ticket.setOverDueDateTime(new Date(rs.getTimestamp("over_due_date_time").getTime()));
                } else {
                    ticket.setOverDueDateTime(null);
                }
                ticket.setDurationForReply(rs.getDouble("duration_for_reply"));
                if (rs.getTimestamp("reopen_date_time") != null) {
                    ticket.setReopenDateTime(new Date(rs.getTimestamp("reopen_date_time").getTime()));
                } else {
                    ticket.setReopenDateTime(null);
                }
                ticket.setAssignedToUserId(rs.getString("assigned_to_user_id"));
                ticket.setTopicId(rs.getInt("topic_id") + "");
                ticket.setOffname(rs.getString("off_en"));
                li.add(ticket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public String getOfficeEstInformation(String spc) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String isEstablishmentSection = "N";
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select count(*) from g_privilege_map where spc=? and role_id='05'");
            ps.setString(1, spc);
            rs = ps.executeQuery();
            while(rs.next())
            {
                if (rs.getInt("count") > 0) {
                    isEstablishmentSection = "Y";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps, con);
        }
        return isEstablishmentSection;
    }
}
