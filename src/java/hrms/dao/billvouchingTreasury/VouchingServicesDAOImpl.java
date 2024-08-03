/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.billvouchingTreasury;

import hrms.common.DataBaseFunctions;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Surendra
 */
public class VouchingServicesDAOImpl implements VouchingServicesDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public double getGrossAmt(int billId, String tabname) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        double gross = 0;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            if (tabname.equals("PAY")) {
                rs = st.executeQuery("select sum(ad_amt) gross from aq_dtls inner join (select * from AQ_MAST where bill_no=" + billId + ")AQ_MAST "
                        + " on AQ_MAST.aqsl_no= aq_dtls.aqsl_no where ad_type = 'A'");
                if (rs.next()) {
                    gross = rs.getDouble("gross");
                }
                rs = st.executeQuery("select SUM(CUR_BASIC) AMT from AQ_MAST where bill_no=" + billId);
                if (rs.next()) {
                    gross = gross + rs.getDouble("AMT");
                }
            } else if (tabname.equalsIgnoreCase("OT")) {
                
                rs = st.executeQuery("select SUM(total) AMT from AQ_MAST_OT where bill_no=" + billId);
                if (rs.next()) {
                    gross = rs.getDouble("AMT");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gross;
    }

    @Override
    public double getdeductAmt(int billId, String tabname,int aqmonth, int aqyear) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        double deduct = 0;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            if (tabname.equals("PAY")) {
                rs = st.executeQuery("SELECT SUM(AD_AMT) AD_AMT FROM (SELECT AQSL_NO FROM AQ_MAST WHERE BILL_NO=" + billId + " and aq_year="+aqyear+" and aq_month="+aqmonth+" )AQ_MAST "
                        + "INNER JOIN (SELECT * FROM AQ_DTLS WHERE AD_TYPE='D'  AND AD_AMT >0 and SCHEDULE != 'PVTL' and SCHEDULE != 'PVTD')AQ_DTLS ON AQ_MAST.AQSL_NO = AQ_DTLS.AQSL_NO ");

                if (rs.next()) {
                    deduct = rs.getDouble("AD_AMT");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deduct;
    }

}
