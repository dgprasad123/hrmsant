/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.leaveapply;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveapply.MaxLeaveIdDAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("singleton")
public class MaxLeaveIdDAOImpl implements MaxLeaveIdDAO{

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private String mCode;

    @Override
    public String getMaxLeaveId() {
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

                rs = st.executeQuery("SELECT MAX(cast ( LEAVEID as BIGINT )) MCODE FROM EMP_LEAVE");
                if (rs.next()) {
                    mCode = rs.getString("MCODE");

                    if (mCode != null) {
                        // modified by Surendra for keep track of HRMS 2.0 transaction //
                        if (fixedId.compareTo(mCode) > 0) {
                            mCode = "21910000000001";
                        } else {
                            mCode = CommonFunctions.getNextString(mCode);
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return mCode;
    }
}
