/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.tpfreport;

import hrms.common.DataBaseFunctions;
import hrms.model.tpfreport.TPFReportBean;
import hrms.model.tpfreport.TPFReportList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Madhusmita
 */
public class EmpTpfDetailsDAOImpl implements EmpTpfDetailsDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getTpfEmployeeList(int month, int year, String offCode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        Statement stmt1 = null;
        Statement stmt2 = null;
        Statement stmt3 = null;
        TPFReportBean tpfbean = null;
        ArrayList arList = new ArrayList();
        try {
            con = this.dataSource.getConnection();            
            String sql1 = "SELECT * FROM (  \n"
                    + " SELECT GPF_ACC_NO,EMP_MAST.DOE_GOV,EMP_MAST.DOB,EMP_MAST.DOS, \n"
                    + " AQ_MAST.EMP_CODE,AQ_MAST.OFF_CODE,AQ_MAST.EMP_NAME, AQ_MAST.CUR_DESG,AQ_MAST.BANK_ACC_NO,AQ_MAST.CUR_BASIC,AQ_MAST.PAY_SCALE, \n"
                    + " AQ_MAST.AQSL_NO,POST_SL_NO from (SELECT EMP_CODE,EMP_NAME,OFF_CODE,CUR_DESG,BANK_ACC_NO,CUR_BASIC,PAY_SCALE,AQSL_NO,POST_SL_NO, \n"
                    + " GPF_ACC_NO from AQ_MAST WHERE OFF_CODE like '" + offCode + "%' and aq_month =  '" + month + "' \n"
                    + " and aq_year = '" + year + "'  AND ACCT_TYPE='TPF') AQ_MAST  \n"
                    + " left outer join EMP_MAST on AQ_MAST.EMP_CODE =EMP_MAST.EMP_ID ) TPF_DETAILS ORDER BY GPF_ACC_NO ";
            pst = con.prepareStatement(sql1);
            rs = pst.executeQuery();
            while (rs.next()) {
                tpfbean = new TPFReportBean();
                tpfbean.setDdoCode(rs.getString("off_code"));
                tpfbean.setEmpname(rs.getString("emp_name"));
                tpfbean.setEmpId(rs.getString("emp_code"));
                tpfbean.setCurDesg(rs.getString("cur_desg"));
                tpfbean.setAqslno(rs.getString("aqsl_no"));
                tpfbean.setGpfNo(rs.getString("gpf_acc_no"));
                if (rs.getString("aqsl_no") != null && !rs.getString("aqsl_no").equals("")) {
                    String empAqslNo = rs.getString("aqsl_no");
                    stmt1 = con.createStatement();
                    String Sql2 = "SELECT AD_AMT   FROM AQ_DTLS WHERE AD_TYPE = 'D' AND AQSL_NO= '" + empAqslNo + "'  AND  DED_TYPE = 'S' and SCHEDULE ='TPF'";
                    rs1 = stmt1.executeQuery(Sql2);
                    if (rs1.next()) {
                        tpfbean.setMonthlySubAmt(rs1.getInt("AD_AMT"));
                    }
                    stmt2 = con.createStatement();
                    String Sql3 = "SELECT AD_AMT REFUND_OF_WITHDRAWAL, REF_DESC   FROM AQ_DTLS WHERE AD_TYPE = 'D'  AND DED_TYPE = 'L' and SCHEDULE='TPFGA'\n"
                            + " AND AQSL_NO = '" + empAqslNo + "'";
                    rs2 = stmt2.executeQuery(Sql3);
                    if (rs2.next()) {
                        tpfbean.setTowardsLoan(rs2.getInt("REFUND_OF_WITHDRAWAL"));
                        tpfbean.setInstCnt(StringUtils.defaultString(rs2.getString("REF_DESC")));
                    }
                    stmt3 = con.createStatement();
                    String Sql4 = "SELECT SUM(AD_AMT)TOTALRELEASED FROM AQ_DTLS WHERE AQSL_NO ='" + empAqslNo + "' AND (AD_CODE='TPF' OR ad_code='TPFGA') GROUP BY AQSL_NO";
                    rs3 = stmt3.executeQuery(Sql4);
                    if (rs3.next()) {
                        tpfbean.setTotalReleased(rs3.getInt("TOTALRELEASED"));
                    }
                }
                arList.add(tpfbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arList;
    }

}
