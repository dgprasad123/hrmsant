/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.Language;


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
public class LanguageDAOImpl implements LanguageDAO{
     @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
      public ArrayList getLanguageList(){
          Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList langList = new ArrayList();
        Language language = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT LANGUAGE FROM G_LANGUAGE ORDER BY LANGUAGE");
            rs = st.executeQuery();
            while (rs.next()) {
                language = new Language();
                language.setLangCode(rs.getString("LANGUAGE"));
                language.setLangName(rs.getString("LANGUAGE"));
                langList.add(language);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return langList;
    }
      
}
