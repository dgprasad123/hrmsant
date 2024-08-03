/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.master;

import hrms.SelectOption;
import hrms.common.DataBaseFunctions;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Durga
 */
public class ProcessStatusDAOImpl implements ProcessStatusDAO{
    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public List getProcessStatusList() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        SelectOption so = null;
        List li = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT STATUS_ID,STATUS_NAME FROM G_PROCESS_STATUS WHERE PROCESS_ID=4");
            rs = pst.executeQuery();

            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("STATUS_NAME"));
                so.setValue(rs.getString("STATUS_ID"));
                li.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }
    
}
