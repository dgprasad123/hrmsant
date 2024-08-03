/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.incrementsanction;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.incrementsanction.ContractualEmployeeIncrementForm;
import hrms.model.joining.JoiningContractualForm;
import hrms.model.joining.JoiningContractualList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Surendra
 */
public class IncrementForSanctionContractualDAOImpl implements IncrementForSanctionContractualDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getIncrementContractualList(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List ilist = new ArrayList();
        ContractualEmployeeIncrementForm incrf = null;
        try {
            con = this.dataSource.getConnection();

            String sql = " select cont_incr_id,emp_id,ordno, orddt, cur_basic, new_basic, off_code,  entry_taken_by, entry_taken_on from emp_contractual_increment where emp_id=? order by orddt desc ";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                incrf = new ContractualEmployeeIncrementForm();
                incrf.setEmpId(rs.getString("emp_id"));
                incrf.setIncrId(rs.getInt("cont_incr_id"));
                incrf.setOrddate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                incrf.setCurBasic(rs.getInt("cur_basic"));
                incrf.setNewBasic(rs.getInt("new_basic"));
                incrf.setOrdno(rs.getString("ordno"));
                incrf.setOffCode(rs.getString("off_code"));

                ilist.add(incrf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ilist;
    }

    @Override
    public ContractualEmployeeIncrementForm getIncrementContractualData(int incrId, String empId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ContractualEmployeeIncrementForm incrf = new ContractualEmployeeIncrementForm();
        try {
            con = this.dataSource.getConnection();

            String sql = "select  cont_incr_id, emp_id,ordno, orddt, cur_basic, new_basic, off_code,  entry_taken_by, entry_taken_on from emp_contractual_increment where cont_incr_id=? and emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, incrId);
            pst.setString(2, empId);
            rs = pst.executeQuery();
            if (rs.next()) {
                incrf = new ContractualEmployeeIncrementForm();
                incrf.setEmpId(rs.getString("emp_id"));
                incrf.setIncrId(rs.getInt("cont_incr_id"));
                incrf.setOrddate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                incrf.setCurBasic(rs.getInt("cur_basic"));
                incrf.setNewBasic(rs.getInt("new_basic"));
                incrf.setOrdno(rs.getString("ordno"));
                incrf.setOffCode(rs.getString("off_code"));
            }

            if (incrf.getIncrId() < 1) {
                pst = con.prepareStatement("select cur_basic_salary from emp_mast where emp_id=?");
                pst.setString(1, empId);
                rs = pst.executeQuery();
                if (rs.next()) {
                    
                    incrf.setCurBasic(rs.getInt("cur_basic_salary"));
                    
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return incrf;
    }

    @Override
    public void saveIncrementContractual(ContractualEmployeeIncrementForm incr, String loginempid) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "insert into emp_contractual_increment (emp_id, ordno, orddt, cur_basic, new_basic, off_code,  entry_taken_by, entry_taken_on) values(?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, incr.getEmpId());

            pst.setString(2, incr.getOrdno());
            if (incr.getOrddate() != null && !incr.getOrddate().equals("")) {
                pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(incr.getOrddate()).getTime()));
            } else {
                pst.setTimestamp(3, null);
            }
            pst.setInt(4, incr.getCurBasic());
            pst.setInt(5, incr.getNewBasic());

            pst.setString(6, incr.getOffCode());
            pst.setString(7, loginempid);
            pst.setTimestamp(8, new Timestamp(new Date().getTime()));
            pst.executeUpdate();

            sql = "update emp_mast set cur_basic_salary=? where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, incr.getNewBasic());
            pst.setString(2, incr.getEmpId());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateIncrementContractual(ContractualEmployeeIncrementForm incr, String loginempid) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "update  emp_contractual_increment set  ordno=?, orddt=?, cur_basic=?, new_basic=?, off_code=?,  entry_taken_by=?, entry_taken_on=? where emp_id=? and cont_incr_id=?";
            pst = con.prepareStatement(sql);

            pst.setString(1, incr.getOrdno());
            if (incr.getOrddate() != null && !incr.getOrddate().equals("")) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(incr.getOrddate()).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            pst.setInt(3, incr.getCurBasic());
            pst.setInt(4, incr.getNewBasic());

            pst.setString(5, incr.getOffCode());
            pst.setString(6, loginempid);
            pst.setTimestamp(7, new Timestamp(new Date().getTime()));

            pst.setString(8, incr.getEmpId());
            pst.setInt(9, incr.getIncrId());

            pst.executeUpdate();

            sql = "update emp_mast set cur_basic_salary=? where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, incr.getNewBasic());
            pst.setString(2, incr.getEmpId());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}
