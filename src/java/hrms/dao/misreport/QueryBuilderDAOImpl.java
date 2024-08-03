/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.misreport;

import hrms.common.DataBaseFunctions;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Manas
 */
public class QueryBuilderDAOImpl implements QueryBuilderDAO {

    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    @Override
    public List getResultSet(String sql) throws Exception{
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet resultSet = null;
        ArrayList datalist = new ArrayList();
        try {
            con = this.repodataSource.getConnection();
            pst = con.prepareStatement(sql);
            resultSet = pst.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            
            List data = new ArrayList();
            for (int i = 1; i <= columnsNumber; i++) {
                data.add(rsmd.getColumnName(i));
            }
            datalist.add(data);
            while (resultSet.next()) {
                data = new ArrayList();
                for (int i = 1; i <= columnsNumber; i++) {
                    data.add(resultSet.getString(i));
                }
                datalist.add(data);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DataBaseFunctions.closeSqlObjects(resultSet, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return datalist;
    }

}
