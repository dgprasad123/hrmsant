/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.notification;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("singleton")
public class MaxNotificationIdDAOImpl implements MaxNotificationIdDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private String notid;
    private String mCode;

    @Override
    public String getMaxNotId() {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String fixedId = "21910000000000";
        try {

            if (notid != null) {
                notid = CommonFunctions.getNextString(notid);
            } else {

                con = this.dataSource.getConnection();
                st = con.createStatement();
                rs = st.executeQuery("SELECT max(cast (not_id as BIGINT ))+1 NCODE FROM emp_notification");
                if (rs.next()) {
                    notid = rs.getString("NCODE");
                    if (notid != null) {
                        if (fixedId.compareTo(notid) > 0) {
                            notid = "21910000000001";
                        }
                    } 

                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        
        return notid;
    }

    @Override
    public String getMaxCode(String tblname, String fieldname) {
        Statement st = null;
        ResultSet rs = null;
        String fixedId = "21910000000000";
        Connection con = null;
        try {
            if (mCode != null) {
                mCode = CommonFunctions.getNextString(mCode);
            } else {
                con = this.dataSource.getConnection();
                st = con.createStatement();

                rs = st.executeQuery("SELECT MAX(cast ( "+fieldname+" as BIGINT )) MCODE FROM " + tblname);
                if (rs.next()) {
                    mCode = rs.getString("MCODE");

                    if (mCode != null) {
                        // modified by Surendra for keep track of HRMS 2.0 transaction //
                        if (fixedId.compareTo(mCode) > 0) {
                            mCode = "21910000000001";
                        } else{
                            mCode = CommonFunctions.getNextString(mCode);
                        }
                    } 

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
            
        }
        return mCode;
    }

}
