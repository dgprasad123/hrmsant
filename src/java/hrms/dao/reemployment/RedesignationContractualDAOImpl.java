/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.reemployment;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.reemployment.RedesignationContractualBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Surendra
 */
public class RedesignationContractualDAOImpl implements RedesignationContractualDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override

    public RedesignationContractualBean getTransferContractualList(String empid) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List redesignation = new ArrayList();
        RedesignationContractualBean tbean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select emp_id,dep_code,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,post_nomenclature from emp_mast where emp_id=?";

            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                tbean = new RedesignationContractualBean();
                tbean.setEmpid(rs.getString("emp_id"));
                tbean.setEname(rs.getString("EMPNAME"));
                tbean.setContpost(rs.getString("post_nomenclature"));
                tbean.setDepcode(rs.getString("dep_code"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return tbean;
    }

    @Override
    public void saveCurrentpostContractual(RedesignationContractualBean incr, String loginempid, String loginusername) {
        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "insert into contractual_emp_redesignation (emp_id, current_post_nomenclature, new_post_nomenclature, modified_date, modified_by_user, modified_by_hrms_id) values(?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, incr.getEmpid());
            pst.setString(2, incr.getContpost());
            pst.setString(3, incr.getNewContpost().toUpperCase());
            pst.setTimestamp(4, new Timestamp(new Date().getTime()));
            pst.setString(5, loginusername);
            pst.setString(6, loginempid);
            pst.executeUpdate();
            

            if (incr.getDepcode() != null && incr.getDepcode().equals("03")) {
                sql = "update emp_mast set post_nomenclature=?, dep_code='02' where emp_id=?";
            } else {
                sql = "update emp_mast set post_nomenclature=? where emp_id=?";
            }

            pst = con.prepareStatement(sql);
            pst.setString(1, incr.getNewContpost().toUpperCase());
            pst.setString(2, incr.getEmpid());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

}
