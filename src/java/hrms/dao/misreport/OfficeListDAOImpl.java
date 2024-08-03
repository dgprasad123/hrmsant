/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.misreport;

import hrms.common.DataBaseFunctions;
import hrms.model.misreport.OfficeReport;
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
public class OfficeListDAOImpl implements OfficeListDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getOfficeList(String offCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList offList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select ddo_code,dist_name,off_en from g_office inner join g_district on g_district.dist_code=g_office.dist_code where p_off_code=?");
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                OfficeReport offReport = new OfficeReport();
                offReport.setDdoCode(rs.getString("ddo_code"));
                offReport.setDistName(rs.getString("dist_name"));
                offReport.setOffName(rs.getString("off_en"));
                offList.add(offReport);
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
