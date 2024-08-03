/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.common;

import hrms.model.parmast.ParForceForwardBean;
import hrms.model.parmast.ParMaster;
import hrms.model.parmast.ReportingHelperBean;
import hrms.model.task.TaskListHelperBean;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;

/**
 *
 * @author Manisha
 */
public class ParSubmissionofTwoThousandTwentyOneTwentyTwo {

    public static void main(String[] args) throws WriteException, IOException, BiffException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        PreparedStatement pstmt4 = null;
        PreparedStatement pstmt5 = null;
        PreparedStatement pstmt6 = null;

        ResultSet res = null;
        ResultSet res2 = null;
        ResultSet res4 = null;
        ResultSet res5 = null;

        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://192.168.1.19/hrmis", "hrmis2", "cmgi");
            //con = DriverManager.getConnection("jdbc:postgresql://172.16.1.16/hrmis", "hrmis2", "cmgi");

            pstmt = con.prepareStatement("select spc,NRC_SUBMITTED_ON,par_master.emp_id from par_master where parid=421686");
            res = pstmt.executeQuery();

            if (res.next()) {
                ParMaster parmaster = new ParMaster();
                parmaster.setAppraiseePost(res.getString("spc"));
                parmaster.setSubmittedOn(res.getString("NRC_SUBMITTED_ON"));
                parmaster.setEmpid(res.getString("emp_id"));
            }

            pstmt1 = con.prepareStatement("update par_reporting_tran set is_completed=null where par_id=421686");
            pstmt1.executeUpdate();

            pstmt2 = con.prepareStatement("select reporting_emp_id,reporting_cur_spc,prptid,* from par_reporting_tran where par_id=421686 and (todate::DATE - fromdate::DATE) > 120 order by hierarchy_no");
            res2 = pstmt2.executeQuery();
            if (res2.next()) {
                ReportingHelperBean rhb = new ReportingHelperBean();
                rhb.setReportingempid(res2.getString("reporting_emp_id"));
                rhb.setReportingauthName(res2.getString("reporting_cur_spc"));
                rhb.setPrtid(res2.getInt("prptid"));
            }

            pstmt3 = con.prepareStatement("update par_master set ref_id_of_table=?,par_status=6 where parid=421686");
            pstmt3.setInt(1, res2.getInt("prptid"));
            pstmt3.executeUpdate();

            pstmt4 = con.prepareStatement("select max (task_id) + 1 as task_id from task_master");
            res4 = pstmt4.executeQuery();
            if (res4.next()) {
                TaskListHelperBean thb = new TaskListHelperBean();
                thb.setTaskId(res4.getInt("task_id"));
            }

            pstmt5 = con.prepareStatement("insert into task_master(process_id,initiated_by,initiated_on,status_id,pending_at,note,apply_to,initiated_spc,pending_spc,apply_to_spc,is_seen,is_completed, "
                    + "completed_on,ref_id,ref_description,task_id,task_par_type) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pstmt5.setInt(1, 3);
            pstmt5.setString(2, res.getString("emp_id"));
            pstmt5.setDate(3, res.getDate("NRC_SUBMITTED_ON"));
            pstmt5.setInt(4, 6);
            pstmt5.setString(5, res2.getString("reporting_emp_id"));
            pstmt5.setString(6, null);
            pstmt5.setString(7, res2.getString("reporting_emp_id"));
            pstmt5.setString(8, res.getString("spc"));
            pstmt5.setString(9, res2.getString("reporting_cur_spc"));
            pstmt5.setString(10, res2.getString("reporting_cur_spc"));
            pstmt5.setString(11, null);
            pstmt5.setString(12, "N");
            pstmt5.setDate(13, null);
            pstmt5.setInt(14, 0);
            pstmt5.setString(15, null);
            pstmt5.setInt(16, res4.getInt("task_id"));
            pstmt5.setString(17, null);
            pstmt5.executeUpdate();

            pstmt6 = con.prepareStatement("update par_master set task_id=? where parid=421686");
            pstmt6.setInt(1, res4.getInt("task_id"));
            pstmt6.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //DataBaseFunctions.closeSqlObjects(pstmt, pstmt1,pstmt3,pstmt4,pstmt5,pstmt6);
            //DataBaseFunctions.closeSqlObjects(res, res2,res4);
            //DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(res, pstmt, con);
        }
    }

}
