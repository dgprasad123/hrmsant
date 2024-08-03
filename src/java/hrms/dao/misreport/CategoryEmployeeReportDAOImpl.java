/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.misreport;

import hrms.common.DataBaseFunctions;
import hrms.model.misreport.EmpCategoryBean;
import hrms.model.misreport.OfficeWiseEmpStatusBean;
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
public class CategoryEmployeeReportDAOImpl implements CategoryEmployeeReportDAO {
    
    @Resource(name = "dataSource")
    protected DataSource dataSource;
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public OfficeWiseEmpStatusBean getOfficeWiseEmployeeCategoryData(String offCode) {
        OfficeWiseEmpStatusBean owesb = new OfficeWiseEmpStatusBean();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int totalgenmale = 0;
        int totalgenfemale = 0;
        int totalscmale = 0;
        int totalscfemale = 0;
        int totalstmale = 0;
        int totalstfemale = 0;
        int totalobcmale = 0;
        int totalobcfemale = 0;
        int totalsebcmale = 0;
        int totalsebcfemale = 0;
        int totaluncatmale = 0;
        int totaluncatfemale = 0;
        int totaluncatothers = 0;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM EMP_CATEGORY_REPORT WHERE OFF_CODE=?");
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                owesb.setOffcode(rs.getString("OFF_CODE"));
                owesb.setOffen(rs.getString("OFF_NAME"));
                int genmalecnt = rs.getInt("GENERAL_MALE");
                totalgenmale = totalgenmale + genmalecnt;
                owesb.setGeneralmale(rs.getInt("GENERAL_MALE"));
                
                int genfemalecnt = rs.getInt("GENERAL_FEMALE");
                totalgenfemale = totalgenfemale + genfemalecnt;
                owesb.setGeneralfemale(rs.getInt("GENERAL_FEMALE"));
                
                int scmalecnt = rs.getInt("SC_MALE");
                totalscmale = totalscmale + scmalecnt;
                owesb.setScmale(rs.getInt("SC_MALE"));
                
                int scfemalecnt = rs.getInt("SC_FEMALE");
                totalscfemale = totalscfemale + scfemalecnt;
                owesb.setScfemale(rs.getInt("SC_FEMALE"));
                
                int stmalecnt = rs.getInt("ST_MALE");
                totalstmale = totalstmale + stmalecnt;
                owesb.setStmale(rs.getInt("ST_MALE"));
                
                int stfemalecnt = rs.getInt("ST_FEMALE");
                totalstfemale = totalstfemale + stfemalecnt;
                owesb.setStfemale(rs.getInt("ST_FEMALE"));
                
                int obcmalecnt = rs.getInt("OBC_MALE");
                totalobcmale = totalobcmale + obcmalecnt;
                owesb.setObcmale(rs.getInt("OBC_MALE"));
                
                int obcfemalecnt = rs.getInt("OBC_FEMALE");
                totalobcfemale = totalobcfemale + obcfemalecnt;
                owesb.setObcfemale(rs.getInt("OBC_FEMALE"));
                
                int sebcmalecnt = rs.getInt("SEBC_MALE");
                totalsebcmale = totalsebcmale + sebcmalecnt;
                owesb.setSebcmale(rs.getInt("SEBC_MALE"));
                
                int sebcfemalecnt = rs.getInt("SEBC_FEMALE");
                totalsebcfemale = totalsebcfemale + sebcfemalecnt;
                owesb.setSebcfemale(rs.getInt("SEBC_FEMALE"));
                
                int uncatmalecnt = rs.getInt("UNCAT_MALE");
                totaluncatmale = totaluncatmale + uncatmalecnt;
                owesb.setUncatmale(rs.getInt("UNCAT_MALE"));
                
                int uncatfemalecnt = rs.getInt("UNCAT_FEMALE");
                totaluncatfemale = totaluncatfemale + uncatfemalecnt;
                owesb.setUncatfemale(rs.getInt("UNCAT_FEMALE"));
                
                int uncatothcnt = rs.getInt("UNCAT_OTHERS");
                totaluncatothers = totaluncatothers + uncatothcnt;
                owesb.setUcatnogender(rs.getInt("UNCAT_OTHERS"));
            }
        } catch (SQLException exe) {
            exe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        
        return owesb;
    }
    
    @Override
    public List getEmpCatStatusList(String offCode, String category, String gender) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List catlist = new ArrayList();
        String sql = "";
        try {
            con = this.dataSource.getConnection();
            if (category != null && gender != null) {
                pstmt = con.prepareStatement("SELECT EMP_ID,GPF_NO,NAME,POST,CUR_BASIC_SALARY FROM(SELECT NAME,SPC,EMP_ID,GPF_NO,GPC,CUR_BASIC_SALARY FROM(SELECT CUR_SPC,EMP_ID,GPF_NO,CUR_BASIC_SALARY"
                        + ",coalesce(TRIM(INITIALS),null,'',TRIM(INITIALS)||' ')||coalesce(TRIM(F_NAME),null,'',TRIM(F_NAME)||' ')||"
                        + "coalesce(TRIM(M_NAME),null,'',TRIM(M_NAME)||' ')||coalesce(TRIM(L_NAME),null,'',TRIM(L_NAME)) AS NAME FROM EMP_MAST "
                        + "WHERE CUR_OFF_CODE=? AND CATEGORY=? AND GENDER=? AND DEP_CODE='02')TAB "
                        + "LEFT OUTER JOIN G_SPC ON G_SPC.SPC=TAB.CUR_SPC)TAB1 LEFT OUTER JOIN G_POST ON G_POST.POST_CODE=TAB1.GPC WHERE CUR_BASIC_SALARY IS NOT NULL ORDER BY CUR_BASIC_SALARY DESC");
                pstmt.setString(1, offCode);
                pstmt.setString(2, category);
                pstmt.setString(3, gender);
            }
            if (gender != null && category == null) {
                sql = "SELECT EMP_ID,GPF_NO,NAME,POST,CUR_BASIC_SALARY FROM(SELECT NAME,SPC,EMP_ID,GPF_NO,GPC,CUR_BASIC_SALARY FROM(SELECT CUR_SPC,EMP_ID,GPF_NO,CUR_BASIC_SALARY"
                        + ",coalesce(TRIM(INITIALS),null,'',TRIM(INITIALS)||' ')||coalesce(TRIM(F_NAME),null,'',TRIM(F_NAME)||' ')||"
                        + "coalesce(TRIM(M_NAME),null,'',TRIM(M_NAME)||' ')||coalesce(TRIM(L_NAME),null,'',TRIM(L_NAME)) AS NAME FROM EMP_MAST "
                        + "WHERE CUR_OFF_CODE=? AND CATEGORY is null AND GENDER=? AND DEP_CODE='02')TAB "
                        + "LEFT OUTER JOIN G_SPC ON G_SPC.SPC=TAB.CUR_SPC)TAB1 LEFT OUTER JOIN G_POST ON G_POST.POST_CODE=TAB1.GPC WHERE CUR_BASIC_SALARY IS NOT NULL ORDER BY CUR_BASIC_SALARY DESC";
                pstmt.setString(1, offCode);
                pstmt.setString(2, gender);
            }
            if (category == null && gender == null) {
                sql = "SELECT EMP_ID,GPF_NO,NAME,POST,CUR_BASIC_SALARY FROM(SELECT NAME,SPC,EMP_ID,GPF_NO,GPC,CUR_BASIC_SALARY FROM(SELECT CUR_SPC,EMP_ID,GPF_NO,CUR_BASIC_SALARY"
                        + ",coalesce(TRIM(INITIALS),null,'',TRIM(INITIALS)||' ')||coalesce(TRIM(F_NAME),null,'',TRIM(F_NAME)||' ')||"
                        + "coalesce(TRIM(M_NAME),null,'',TRIM(M_NAME)||' ')||coalesce(TRIM(L_NAME),null,'',TRIM(L_NAME)) AS NAME FROM EMP_MAST "
                        + "WHERE CUR_OFF_CODE=? AND GENDER is null AND DEP_CODE='02')TAB "
                        + "LEFT OUTER JOIN G_SPC ON G_SPC.SPC=TAB.CUR_SPC)TAB1 LEFT OUTER JOIN G_POST ON G_POST.POST_CODE=TAB1.GPC WHERE CUR_BASIC_SALARY IS NOT NULL ORDER BY CUR_BASIC_SALARY DESC";
                pstmt.setString(1, offCode);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                EmpCategoryBean ecb = new EmpCategoryBean();
                ecb.setEmpid(rs.getString("EMP_ID"));
                ecb.setGpfno(rs.getString("GPF_NO"));
                ecb.setName(rs.getString("NAME"));
                if (rs.getString("post") != null && !rs.getString("post").equals("")) {
                    ecb.setPost(rs.getString("post"));
                }
                catlist.add(ecb);
            }
        } catch (SQLException exe) {
            exe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return catlist;
    }
    
}
