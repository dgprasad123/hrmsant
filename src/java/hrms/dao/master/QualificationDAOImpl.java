
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.Qualification;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class QualificationDAOImpl implements QualificationDAO{
     @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getqualificationList(){
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList qualList = new ArrayList();
        Qualification qualification = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT PRINT_SL,QUALIFICATION FROM G_QUALIFICATION ORDER BY QUALIFICATION");
            rs = st.executeQuery();
            while (rs.next()) {
                qualification = new Qualification();
                qualification.setQualCode(rs.getString("PRINT_SL"));
                qualification.setQualification(rs.getString("QUALIFICATION"));
                qualList.add(qualification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return qualList;
    }
     @Override
    public ArrayList getsubjectList(){
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList qualList = new ArrayList();
        Qualification qualification = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT subject_name FROM g_subject ORDER BY subject_name");
            rs = st.executeQuery();
            while (rs.next()) {
                qualification = new Qualification();
                qualification.setQualCode(rs.getString("subject_name").toUpperCase());
                qualification.setQualification(rs.getString("subject_name").toUpperCase());
                qualList.add(qualification);
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
