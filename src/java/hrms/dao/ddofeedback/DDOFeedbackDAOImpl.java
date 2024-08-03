/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.ddofeedback;

import hrms.common.DataBaseFunctions;
import hrms.model.master.GPost;
import hrms.model.visitingstatus.VisitingStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author lenovo pc
 */
public class DDOFeedbackDAOImpl implements DDOFeedbackDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
@Override
    public ArrayList getDDdoFeedbackList() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList visitingList = new ArrayList();
        VisitingStatus vs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select * from(select tab.feedback,tab.dist_code,visited,notvisited,dist_name from(select  count(*)feedback,count(*) filter (where if_visited = 'Y') as visited, "
                    + "count(*) filter (where if_visited = 'N') as notvisited,dist_code "
                    + "from districtcoordinator_visited "
                    + "left outer join g_office on g_office.off_code=districtcoordinator_visited.office_code "
                    + "group by dist_code)tab inner join g_district on g_district.dist_code=tab.dist_code)tab1 inner join "
                    + "(select count(*) totDDO,dist_code  from g_office where  is_ddo='Y' group by dist_code )tab2 on tab2.dist_code=tab1.dist_code order by totDDO desc");

            rs = pstmt.executeQuery();
            while (rs.next()) {
                vs = new VisitingStatus();
                vs.setDistName(rs.getString("dist_name"));
                vs.setTotDDOOfficeVisited(rs.getString("visited"));
                vs.setTotDDOOfficeNotVisited(rs.getString("notvisited"));
                vs.setTotDDOFeedbackGiven(rs.getString("feedback"));
                vs.setTotDDOOffice(rs.getString("totDDO"));
                visitingList.add(vs);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return visitingList;
    }
}

