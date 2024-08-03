/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.report.officewisepostgroup;

import hrms.common.DataBaseFunctions;
import hrms.model.report.officewisepostgroup.OfficeWisePostGroup;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author lenovo pc
 */
public class OfficeWisePostGroupImpl implements OfficeWisePostGroupDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getOfficeWisePostGroupList(String offCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList offList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT CADRE_NAME,TAB4.POST_CODE,TAB4.POST,SAN_STR,CUR_STR,TAB4.POST_GROUP_TYPE FROM "
                    + "( SELECT CADRE_CODE,TAB3.POST_CODE,TAB3.POST,SAN_STR,TAB3.POST_GROUP_TYPE FROM( SELECT POST_CODE,SAN_STR,POST,POST_GROUP_TYPE FROM "
                    + "(SELECT GPC,count(*) SAN_STR FROM(SELECT SPC FROM (SELECT SECTION_ID FROM(SELECT BILL_GROUP_ID "
                    + "FROM BILL_GROUP_MASTER WHERE OFF_CODE=?)BILLGROUPMAST "
                    + "INNER JOIN BILL_SECTION_MAPPING ON BILL_SECTION_MAPPING.BILL_GROUP_ID=BILLGROUPMAST.BILL_GROUP_ID)TAB "
                    + "INNER JOIN SECTION_POST_MAPPING ON SECTION_POST_MAPPING.SECTION_ID=TAB.SECTION_ID)TAB1 "
                    + "LEFT OUTER JOIN G_SPC ON  G_SPC.SPC=TAB1.SPC  GROUP BY GPC )TAB2 INNER JOIN G_POST ON G_POST.POST_CODE=TAB2.GPC)TAB3 "
                    + "LEFT OUTER JOIN G_CADRE2POST ON G_CADRE2POST.POST_CODE=TAB3.POST_CODE)TAB4 "
                    + "LEFT OUTER JOIN G_CADRE ON  G_CADRE.CADRE_CODE=TAB4.CADRE_CODE LEFT OUTER JOIN "
                    + "(SELECT GPC,COUNT(*) CUR_STR FROM(SELECT SPC,GPC FROM (SELECT EMP_ID,CUR_SPC FROM EMP_MAST WHERE CUR_OFF_CODE=?)EMP_MAST "
                    + "INNER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC)G_SPC1 INNER JOIN G_POST ON G_SPC1.GPC = G_POST.POST_CODE GROUP BY GPC) CURRENT_STR "
                    + "ON TAB4.POST_CODE = CURRENT_STR.GPC ORDER BY POST");
            pstmt.setString(1, offCode);
            pstmt.setString(2, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                OfficeWisePostGroup officeWisePostGroup = new OfficeWisePostGroup();
                officeWisePostGroup.setCadretopost(rs.getString("CADRE_NAME"));
                officeWisePostGroup.setPostgrtype(rs.getString("POST_GROUP_TYPE"));
                officeWisePostGroup.setPost(rs.getString("POST"));
                officeWisePostGroup.setSancstrength(rs.getInt("SAN_STR"));
                officeWisePostGroup.setCurstrength(rs.getInt("CUR_STR"));
                officeWisePostGroup.setVacancy(rs.getInt("SAN_STR") - rs.getInt("CUR_STR"));
                offList.add(officeWisePostGroup);
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return offList;
    }
}
