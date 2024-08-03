package hrms.dao.fiscalyear;

import hrms.common.DataBaseFunctions;
import hrms.model.fiscalyear.FiscalYear;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class FiscalYearDAOImpl implements FiscalYearDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String getDefaultFiscalYear() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String defaultFiscalYear = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT FY FROM FINANCIAL_YEAR WHERE IS_CLOSED='N'";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                defaultFiscalYear = rs.getString("FY");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return defaultFiscalYear;
    }

    @Override
    public List getFiscalYearList() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List<FiscalYear> fiscalyearlist = new ArrayList();
        FiscalYear fiscalyr = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT FY FROM FINANCIAL_YEAR ORDER BY FY DESC";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                fiscalyr = new FiscalYear();
                fiscalyr.setFy(rs.getString("FY"));
                fiscalyearlist.add(fiscalyr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fiscalyearlist;
    }
    
    public List getFiscalYearListForGroupCPromotion() {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List<FiscalYear> fiscalyearlist = new ArrayList();
        FiscalYear fiscalyr = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT FY FROM FINANCIAL_YEAR where groupcpromotion_active='Y' ORDER BY FY DESC";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                fiscalyr = new FiscalYear();
                fiscalyr.setFy(rs.getString("FY"));
                fiscalyearlist.add(fiscalyr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fiscalyearlist;
    }

    @Override
    public List getFiscalYearListForPoliceNomination() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        List<FiscalYear> fiscalyearlist = new ArrayList();
        FiscalYear fiscalyr = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT FY FROM police_nomination_dpc_year where is_active = 'Y' ORDER BY FY DESC";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                fiscalyr = new FiscalYear();
                fiscalyr.setFy(rs.getString("FY"));
                fiscalyearlist.add(fiscalyr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fiscalyearlist;
    }

    @Override
    public List getAllFiscalYearListForPoliceNomination() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        List<FiscalYear> fiscalyearlist = new ArrayList();
        FiscalYear fiscalyr = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT FY FROM police_nomination_dpc_year ORDER BY FY DESC";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                fiscalyr = new FiscalYear();
                fiscalyr.setFy(rs.getString("FY"));
                fiscalyearlist.add(fiscalyr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fiscalyearlist;
    }

    @Override
    public List getFiscalYearListCYWise() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List<FiscalYear> fiscalyearlistcy = new ArrayList();
        FiscalYear fiscalyr = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT CY FROM FINANCIAL_YEAR ORDER BY CY DESC";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                fiscalyr = new FiscalYear();
                fiscalyr.setFy(rs.getString("CY"));
                fiscalyearlistcy.add(fiscalyr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fiscalyearlistcy;
    }

    @Override
    public List getPFiscalYearList() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<FiscalYear> fiscalyearlist = new ArrayList();
        FiscalYear fiscalyr = null;
        try {
            con = this.dataSource.getConnection();
            String sql = "SELECT CY FROM FINANCIAL_YEAR WHERE property_active='Y' ORDER BY CY DESC";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                fiscalyr = new FiscalYear();
                fiscalyr.setFy(rs.getString("CY"));
                fiscalyearlist.add(fiscalyr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fiscalyearlist;
    }

    @Override
    public List getisReviewedFiscalYearList() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<FiscalYear> fiscalyearlist = new ArrayList();
        FiscalYear fiscalyr = null;
        try {
            con = this.dataSource.getConnection();
            String sql = "SELECT FY FROM FINANCIAL_YEAR WHERE isreviewed_year='Y' ORDER BY FY DESC";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                fiscalyr = new FiscalYear();
                fiscalyr.setFy(rs.getString("FY"));
                fiscalyearlist.add(fiscalyr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fiscalyearlist;
    }

    @Override
    public List getFiscalYearListForCadrewiseForceforward() {
        Connection con = null;

        PreparedStatement pst;
        ResultSet rs;

        List<FiscalYear> fiscalyearforforceforwardlist = new ArrayList();
        FiscalYear fiscalyr;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT FY FROM FINANCIAL_YEAR ORDER BY FY DESC";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                fiscalyr = new FiscalYear();
                fiscalyr.setFy(rs.getString("FY"));
                fiscalyearforforceforwardlist.add(fiscalyr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fiscalyearforforceforwardlist;
    }
}
