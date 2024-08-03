package hrms.dao.performanceappraisal;

import hrms.common.DataBaseFunctions;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class MaxPARAbsenteeIDDAOImpl implements MaxPARAbsenteeIDDAO{
    
    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private int mCode = 0;
    
    @Override
    public int getMaxPARAbsenteeId() {
        Statement st = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            if (mCode > 0) {
                mCode = mCode + 1;
            } else {
                con = this.dataSource.getConnection();
                st = con.createStatement();

                rs = st.executeQuery("SELECT MAX(PABID) MCODE FROM PAR_ABSENTEE");
                if (rs.next()) {
                    mCode = rs.getInt("MCODE") + 1;
                }else{
                    mCode = 1;
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
