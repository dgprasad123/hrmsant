/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.PropertyMaster;
import hrms.model.master.PropertyOwnerType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Manas Jena
 */
public class PropertyMasterDAOImpl implements PropertyMasterDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getPropertyMasterList(int propertyTypeId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List propertyMasterList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT * FROM PROPERTY_MASTER WHERE PROPERTY_TYPE_ID=?");
            pst.setInt(1, propertyTypeId);
            rs = pst.executeQuery();
            while(rs.next()){
                PropertyMaster pm = new PropertyMaster();
                pm.setPropertyId(rs.getInt("property_id"));
                pm.setPropertyName(rs.getString("property_name"));
                propertyMasterList.add(pm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return propertyMasterList;
    }
    public List getPropertyOwnerList(){
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List propertyOwnerTypeList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT * FROM PROPERTY_OWNER");
            rs = pst.executeQuery();
            while(rs.next()){
                PropertyOwnerType pot = new PropertyOwnerType();
                pot.setOwnerTypeId(rs.getInt("OWNER_TYPE_ID"));
                pot.setOwnerTypeName(rs.getString("OWNER_TYPE_NAME"));
                propertyOwnerTypeList.add(pot);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return propertyOwnerTypeList;
    }
}
