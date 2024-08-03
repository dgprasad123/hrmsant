/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.thread;

import hrms.common.DataBaseFunctions;
import hrms.model.thread.ThreadService;
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
 * @author Manas
 */
public class ThreadServiceDAOImpl implements ThreadServiceDAO{
    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public List getServiceThreadList() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList list = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT SERVICE_ID, SERVICE_NAME, DELAY_TIME, PERIOD_TIME, START_URL, STOP_URL, STATUS  FROM PAYBILL_SERVICE_NAME ORDER BY SERVICE_ID");
            rs = pstmt.executeQuery();
            while(rs.next()){
                ThreadService ts = new ThreadService();
                ts.setServiceId(rs.getInt("SERVICE_ID"));
                ts.setServiceName(rs.getString("SERVICE_NAME"));
                ts.setStartUrl(rs.getString("START_URL"));
                ts.setStatus(rs.getString("STATUS"));
                ts.setStopUrl(rs.getString("STOP_URL"));
                list.add(ts);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }
    
}
