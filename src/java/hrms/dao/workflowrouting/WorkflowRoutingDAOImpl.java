/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.workflowrouting;

import hrms.common.DataBaseFunctions;
import hrms.model.employee.EmployeeBasicProfile;
import hrms.model.workflowrouting.WorkflowRouting;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Surendra
 */
public class WorkflowRoutingDAOImpl implements WorkflowRoutingDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getmappedPostList(String postcode, String processId) {
        List li = new ArrayList();
        WorkflowRouting wr = new WorkflowRouting();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select workflow_routing_id, process_attribute1, getofficeen(reporting_office_code) offName, office_code, gpc, post, process_id, reporting_gpc, level_id "
                    + "from g_workflow_routing a, g_post b where a.reporting_gpc=b.post_code and  gpc=? and process_id=? order by post");
            ps.setString(1, postcode);
            ps.setInt(2, Integer.parseInt(processId));
            rs = ps.executeQuery();
            while (rs.next()) {
                wr = new WorkflowRouting();
                wr.setWorkflowRoutingId(rs.getInt("workflow_routing_id"));
                wr.setPostName(rs.getString("post")+", "+rs.getString("offName"));
                li.add(wr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getPostListAuthorityWise(String offcode, String postcode, String processId) {
        List li = new ArrayList();
        WorkflowRouting wr = new WorkflowRouting();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select DISTINCT post_code,post from ( \n"
                    + " select post_code,post from g_post where  isauthority='Y' and post_code not in ( \n"
                    + " select reporting_gpc from g_workflow_routing where gpc=? and process_id=?) ) tab \n"
                    + " left outer join g_spc on tab.post_code=g_spc.gpc where off_code=?  order by post");
            
            
            ps.setString(1, postcode);
            ps.setInt(2, Integer.parseInt(processId));
            ps.setString(3, offcode);
            rs = ps.executeQuery();
            while (rs.next()) {
                wr = new WorkflowRouting();
                wr.setReportingGpc(rs.getString("post_code") + "");
                wr.setPostName(rs.getString("post"));
                li.add(wr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void addHierarchy(WorkflowRouting wr) {

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("insert into g_workflow_routing (office_code, gpc, process_id, reporting_gpc, reporting_office_code) values(?,?,?,?,?) ");
            ps.setString(1, wr.getLoginOffcode());
            ps.setString(2, wr.getGpc());
            ps.setInt(3, Integer.parseInt(wr.getProcessId()));
            ps.setString(4, wr.getReportingGpc());
            ps.setString(5, wr.getOfficeCode());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void removeHierarchy(WorkflowRouting wr) {

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("delete from g_workflow_routing where  workflow_routing_id=? ");
            ps.setInt(1, wr.getWorkflowRoutingId());
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
    @Override
    public List<EmployeeBasicProfile> getWorkFlowRoutingList(int processId, String postcode, String officecode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        List<EmployeeBasicProfile> empList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT SPC,EMP_ID,F_NAME, M_NAME, L_NAME, SPN FROM G_WORKFLOW_ROUTING "
                    + "INNER JOIN G_SPC ON G_SPC.GPC = G_WORKFLOW_ROUTING.REPORTING_GPC AND  G_SPC.OFF_CODE = G_WORKFLOW_ROUTING.REPORTING_OFFICE_CODE "
                    + "INNER JOIN EMP_MAST ON G_SPC.SPC = EMP_MAST.CUR_SPC "
                    + "WHERE PROCESS_ID = ? AND G_WORKFLOW_ROUTING.GPC = ? AND G_WORKFLOW_ROUTING.OFFICE_CODE = ? AND DEP_CODE = '02' AND IS_REGULAR = 'Y'");
            pstmt.setInt(1, processId);
            pstmt.setString(2, postcode);
            pstmt.setString(3, officecode);
            res = pstmt.executeQuery();
            while(res.next()){
                EmployeeBasicProfile empBasicProfile = new EmployeeBasicProfile();
                empBasicProfile.setEmpid(res.getString("EMP_ID"));
                empBasicProfile.setSpn(res.getString("SPN"));
                empBasicProfile.setSpc(res.getString("SPC"));
                empBasicProfile.setFname(res.getString("F_NAME"));
                empBasicProfile.setMname(res.getString("M_NAME"));
                empBasicProfile.setLname(res.getString("L_NAME"));
                empList.add(empBasicProfile);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt, con);
        }
        return empList;
    }
    
}
