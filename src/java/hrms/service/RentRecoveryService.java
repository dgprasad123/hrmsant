/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.service;

import hrms.common.AqFunctionalities;
import hrms.common.DataBaseFunctions;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.stereotype.Service;

/**
 *
 * @author Manas Jena
 */
@Service
public class RentRecoveryService {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    

    public void replicateRentInfo(int month, int year, String distcode) {
        
        Connection pgcon = null;
        PreparedStatement ps = null;
        PreparedStatement ps3 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        
        String tblname = AqFunctionalities.getAQBillDtlsTable(month,year);
        try {
            String sql = "SELECT  B.BILL_NO,B.AQ_MONTH AQMTH,B.AQ_YEAR AQYR,EMP.EMP_ID,GPF_ACC_NO,B.DDO_CODE,QUARTER_RENT,WATER_RENT,SEWERAGE_RENT,B.BILL_DATE,B.VCH_NO,B.VCH_DATE,EMP.DOS,QTR.CONSUMER_NO FROM "
                    + "BILL_MAST B "
                    + "INNER JOIN AQ_MAST AQ ON B.BILL_NO=AQ.BILL_NO "
                    + "INNER JOIN G_OFFICE GOFF ON B.OFF_CODE=GOFF.OFF_CODE "
                    + "INNER JOIN EMP_QTR_ALLOT QTR ON AQ.EMP_CODE=QTR.EMP_ID "
                    + "INNER JOIN EMP_MAST EMP ON AQ.EMP_CODE=EMP.EMP_ID "
                    + "WHERE  B.AQ_MONTH=? AND B.AQ_YEAR=? AND AQ.EMP_CODE IS NOT NULL "
                    + "AND GOFF.DIST_CODE=? AND GOFF.OFF_STATUS='F' "
                    + "AND (IF_SURRENDERED='N' OR IF_SURRENDERED IS NULL) AND QUARTER_RENT >0 ORDER BY EMP_NAME";

            pgcon = dataSource.getConnection();
            ps = pgcon.prepareStatement(sql);
            ps.setInt(1, month);
            ps.setInt(2, year);
            ps.setString(3, distcode);
            rs = ps.executeQuery();
            pgcon = dataSource.getConnection();
            ps2 = pgcon.prepareStatement("INSERT INTO HRMS_EQUATER_DATA ( AQ_MONTH, AQ_YEAR, HRMSID, GPFNO, DDOCODE, HRA, WTAX, SWTAX, DT_REALN, TVNO, TVDATE, DOS, CONSUMERNO,BILL_NO)  VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
            ps3 = pgcon.prepareStatement("SELECT AD_CODE, AD_AMT FROM(SELECT * FROM AQ_MAST WHERE BILL_NO=? AND EMP_CODE=?)AQ_MAST INNER JOIN "+tblname+" AQ_DTLS ON AQ_MAST.AQSL_NO = AQ_DTLS.AQSL_NO "
                    + "WHERE AD_CODE = 'HRR' OR AD_CODE = 'WRR' OR AD_CODE = 'SWR'");
            
            while (rs.next()) {
                int hrr = 0;
                int wrr = 0;
                int swg = 0;
                ps3.setInt(1, rs.getInt("BILL_NO"));
                ps3.setString(2, rs.getString("EMP_ID"));
                rs2 = ps3.executeQuery();
                while(rs2.next()){
                    String adCode = rs2.getString("AD_CODE");
                    if(adCode.equalsIgnoreCase("HRR")){
                        hrr = rs2.getInt("AD_AMT");
                    }else if(adCode.equalsIgnoreCase("WRR")){
                        wrr = rs2.getInt("AD_AMT");
                    }else if(adCode.equalsIgnoreCase("SWR")){
                        swg = rs2.getInt("AD_AMT");
                    }
                }
                ps2.setInt(1, month);
                ps2.setInt(2, year);
                ps2.setString(3, rs.getString("EMP_ID"));
                ps2.setString(4, rs.getString("GPF_ACC_NO"));
                ps2.setString(5, rs.getString("DDO_CODE"));
                ps2.setInt(6, hrr);
                ps2.setInt(7, wrr);
                ps2.setInt(8, swg);
                ps2.setTimestamp(9, rs.getTimestamp("BILL_DATE"));
                ps2.setString(10, rs.getString("VCH_NO"));
                ps2.setTimestamp(11, rs.getTimestamp("VCH_DATE"));
                ps2.setTimestamp(12, rs.getTimestamp("DOS"));
                ps2.setString(13, rs.getString("CONSUMER_NO"));
                ps2.setInt(14, rs.getInt("BILL_NO"));
                ps2.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pgcon);
        }
    }
}
