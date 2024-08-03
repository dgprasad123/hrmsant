package hrms.dao.loansanction;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("singleton")
public class MaxLoanIdDAOImpl implements MaxLoanIdDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String getLoanId() {
        Statement st = null;
        ResultSet rs = null;
        String fixedId = "21910000000000";
        Connection con = null;
        String mCode = "";
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT MAX(cast(loanid as BIGINT))+1 MCODE FROM emp_loan_sanc");
            if (rs.next()) {
                mCode = rs.getString("MCODE");
                if (mCode != null) {
                    // modified by Surendra for keep track of HRMS 2.0 transaction //
                    if (fixedId.compareTo(mCode) > 0) {

                        mCode = "21910000000001";
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

    @Override
    public String checkDuplicateMaxCode(String mCodecheck) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String mCode = "";
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT count(*) cnt FROM emp_loan_sanc where loanid=?");
            pst.setString(1, mCodecheck);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    mCode = getLoanId();
                } else {
                    mCode = mCodecheck;
                }
            } else {
                mCode = mCodecheck;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return mCode;
    }
}
