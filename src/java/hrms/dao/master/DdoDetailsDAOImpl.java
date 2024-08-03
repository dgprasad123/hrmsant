/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.DdoDetailsBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Surendra
 */
public class DdoDetailsDAOImpl implements DdoDetailsDAO{
    
     @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getDdoList(String distCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList qualList = new ArrayList();
        DdoDetailsBean ddoDetailsBean = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("select off_en,g_office.ddo_code,ddo_hrmsid,mobile,ARRAY_TO_STRING(ARRAY[INITIALS, f_name,m_name,l_name], ' ') FULL_NAME,g_post.post from g_office inner join emp_mast ON g_office.ddo_hrmsid=emp_mast.emp_id inner join g_post ON g_office.ddo_post=g_post.post_code WHERE g_office.dist_code=? AND g_office.is_ddo='Y'  ");
           st.setString(1, distCode);
            rs = st.executeQuery();
            while (rs.next()) {
                ddoDetailsBean = new DdoDetailsBean();
                ddoDetailsBean.setOfficename(rs.getString("off_en"));
                ddoDetailsBean.setDdocode(rs.getString("ddo_code"));
                ddoDetailsBean.setDdohrmsid(rs.getString("ddo_hrmsid"));
                ddoDetailsBean.setDdoName(rs.getString("FULL_NAME"));
                ddoDetailsBean.setDdoPhone(rs.getString("mobile"));
                ddoDetailsBean.setDdoPost(rs.getString("post"));
                qualList.add(ddoDetailsBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return qualList;
    }
    
}
