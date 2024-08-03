package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.Category;
import hrms.model.master.MaritalStatus;
import hrms.model.reservationcategory.ReservationCategoryInformation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class CategoryDAOImpl implements CategoryDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getCategoryList() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList categoryList = new ArrayList();
        Category category = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT CATEGORY FROM G_CATEGORY ORDER BY CATEGORY");
            rs = st.executeQuery();
            while (rs.next()) {
                category = new Category();
                category.setCategoryid(rs.getString("CATEGORY"));
                category.setCategoryName(rs.getString("CATEGORY"));
                categoryList.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return categoryList;
    }

    @Override
    public ArrayList getDisabilityCategorylist() {

        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList categoryList = new ArrayList();
        Category category = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT * FROM G_DISABILITY ORDER BY DISABILITY");
            rs = st.executeQuery();
            while (rs.next()) {
                category = new Category();
                category.setDisabilityCode(rs.getString("DISABILITY"));
                category.setDisabilityName(rs.getString("DISABILITY"));
                categoryList.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return categoryList;
    }
     @Override
    public ArrayList getSpecificCodeList() {

        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList categoryList = new ArrayList();
        Category category = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT * FROM g_ph_code ORDER BY ph_code");
            rs = st.executeQuery();
            while (rs.next()) {
                category = new Category();
                category.setSpecificCode(rs.getString("ph_code"));
                category.setSpecificName(rs.getString("ph_desc"));
                categoryList.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return categoryList;
    }
}
