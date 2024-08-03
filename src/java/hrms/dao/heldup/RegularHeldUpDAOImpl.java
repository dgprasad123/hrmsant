/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.heldup;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.heldup.RegularHeldUpBean;
import hrms.model.payroll.billbrowser.SectionDtlSPCWiseEmp;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Manas
 */
public class RegularHeldUpDAOImpl implements RegularHeldUpDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getyearList(String offcode) {
        ArrayList al = new ArrayList();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int year = 0;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT DISTINCT AQ_YEAR FROM BILL_MAST WHERE OFF_CODE=? ORDER BY AQ_YEAR DESC ");
            pst.setString(1, offcode);
            rs = pst.executeQuery();
            SelectOption so = null;
            if (rs.next()) {
                so = new SelectOption(rs.getString("AQ_YEAR"), rs.getString("AQ_YEAR"));
                al.add(so);
            } else {
                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                so = new SelectOption(year + "", year + "");
                al.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
        return al;
    }

    @Override
    public ArrayList getmonthList() {
        ArrayList al = new ArrayList();
        SelectOption so = null;
        so = new SelectOption("JANUARY", "0");
        al.add(so);
        so = new SelectOption("FEBRUARY", "1");
        al.add(so);
        so = new SelectOption("MARCH", "2");
        al.add(so);
        so = new SelectOption("APRIL", "3");
        al.add(so);
        so = new SelectOption("MAY", "4");
        al.add(so);
        so = new SelectOption("JUNE", "5");
        al.add(so);
        so = new SelectOption("JULY", "6");
        al.add(so);
        so = new SelectOption("AUGUST", "7");
        al.add(so);
        so = new SelectOption("SEPTEMBER", "8");
        al.add(so);
        so = new SelectOption("OCTOBER", "9");
        al.add(so);
        so = new SelectOption("NOVEMBER", "10");
        al.add(so);
        so = new SelectOption("DECEMBER", "11");
        al.add(so);

        return al;
    }

    @Override
    public ArrayList getBillGroupWiseEmployee(BigDecimal billGroupId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList emplist = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT EMP_CODE,GPF_ACC_NO, HELDUP, POST CUR_DESG,SPC,HELDUP_PAY_ID,F_NAME,M_NAME,L_NAME FROM "
                    + "(SELECT EMP_CODE,F_NAME,M_NAME,L_NAME,GPF_ACC_NO, HELDUP,GPC, G_SPC.SPC,HELDUP_PAY_ID FROM "
                    + "(SELECT EMP_MAST.EMP_ID EMP_CODE,F_NAME,M_NAME,L_NAME, GPF_NO GPF_ACC_NO, DECODE(EMP_PAY_HELDUP.EMP_ID,NULL,'N','Y') HELDUP,EMP_MAST.SPC,HELDUP_PAY_ID FROM "
                    + "(SELECT EMP_ID,F_NAME,M_NAME,L_NAME,GPF_NO,SPC FROM "
                    + "(SELECT SPC,BILL_GROUP_ID,POST_SL_NO FROM "
                    + "(SELECT SECTION_ID,BILL_SECTION_MAPPING.BILL_GROUP_ID FROM "
                    + "(SELECT BILL_GROUP_ID FROM BILL_GROUP_MASTER WHERE BILL_GROUP_ID=?) BILL_GROUP_MASTER "
                    + "INNER JOIN BILL_SECTION_MAPPING ON BILL_GROUP_MASTER.BILL_GROUP_ID=BILL_SECTION_MAPPING.BILL_GROUP_ID ) BILL_SECTION_MAPPING "
                    + "INNER JOIN SECTION_POST_MAPPING ON BILL_SECTION_MAPPING.SECTION_ID=SECTION_POST_MAPPING.SECTION_ID ORDER BY POST_SL_NO) SECTION_POST_MAPPING  "
                    + "INNER JOIN EMP_MAST ON  SECTION_POST_MAPPING.SPC=EMP_MAST.CUR_SPC) EMP_MAST "
                    + "LEFT OUTER JOIN (SELECT * FROM EMP_PAY_HELDUP WHERE TO_DATE IS NULL) EMP_PAY_HELDUP ON EMP_MAST.EMP_ID=EMP_PAY_HELDUP.EMP_ID ) EMP_MAST "
                    + "INNER JOIN G_SPC ON EMP_MAST.SPC=G_SPC.SPC) G_SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE ORDER BY HELDUP DESC, TRIM(F_NAME)");
            pstmt.setBigDecimal(1, billGroupId);
            rs = pstmt.executeQuery();
            int i = 1;
            while (rs.next()) {
                RegularHeldUpBean sdswe = new RegularHeldUpBean();
                sdswe.setEmpIdSPC(rs.getString("EMP_CODE") + "@" + rs.getString("SPC"));
                sdswe.setEmpIdSPC(rs.getString("SPC"));
                sdswe.setHrmsId(rs.getString("EMP_CODE"));
                sdswe.setEmpname((StringUtils.defaultString(rs.getString("F_NAME")) + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + StringUtils.defaultString(rs.getString("L_NAME"))).replaceAll("\\s+", " "));
                sdswe.setHeldupStatus(rs.getString("HELDUP"));
                sdswe.setPost(rs.getString("CUR_DESG"));
                sdswe.setGpfNo(rs.getString("GPF_ACC_NO"));
                sdswe.setHeldupId(rs.getBigDecimal("HELDUP_PAY_ID"));
                sdswe.setSlno("" + i++);
                emplist.add(sdswe);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public void helduppay(RegularHeldUpBean regularHeldUpBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean found = false;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT TO_DATE FROM EMP_PAY_HELDUP WHERE EMP_ID=? AND TO_DATE IS NULL");
            pstmt.setString(1, regularHeldUpBean.getHrmsId());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                found = true;
            }
            if (found == false) {
                pstmt = con.prepareStatement("INSERT INTO EMP_PAY_HELDUP(EMP_ID,SPC,OFF_CODE,FROM_DATE,HELDUP_PAY_ID) VALUES(?,?,?,?,?)");
                pstmt.setString(1, regularHeldUpBean.getHrmsId());
                pstmt.setString(2, regularHeldUpBean.getEmpIdSPC());
                pstmt.setString(3, regularHeldUpBean.getOffcode());
                pstmt.setDate(4, new java.sql.Date(CommonFunctions.getDateFromString(regularHeldUpBean.getHeldupdate(), "dd-MMM-yyyy").getTime()));
                pstmt.setInt(5, CommonFunctions.getMaxCode(con, "EMP_PAY_HELDUP", "HELDUP_PAY_ID"));
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void releasepay(RegularHeldUpBean regularHeldUpBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE EMP_PAY_HELDUP SET TO_DATE=? WHERE HELDUP_PAY_ID=?");
            pstmt.setDate(1, new java.sql.Date(CommonFunctions.getDateFromString(regularHeldUpBean.getHeldupdate(), "dd-MMM-yyyy").getTime()));
            pstmt.setBigDecimal(2, regularHeldUpBean.getHeldupId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public RegularHeldUpBean getPayHeldupData(RegularHeldUpBean regularHeldUpBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT FROM_DATE FROM EMP_PAY_HELDUP WHERE EMP_ID = ? AND HELDUP_PAY_ID=?");
            pstmt.setString(1, regularHeldUpBean.getHrmsId());
            pstmt.setBigDecimal(2, regularHeldUpBean.getHeldupId());
            rs = pstmt.executeQuery();
            if(rs.next()){
                regularHeldUpBean.setHeldupdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("FROM_DATE")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return regularHeldUpBean;
    }
}
