/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.LoanType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author lenovo pc
 */
public class LoanTypeDAOImpl implements LoanTypeDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getLoanTypeList() {
        Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        ArrayList loanTypeList = new ArrayList();
        LoanType loanType=new LoanType();
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT LOAN_TP,LOAN_NAME,HAVE_INT FROM G_LOAN  ORDER BY LOAN_NAME");
            while(rs.next()){
                loanType=new LoanType();
                loanType.setLoanType(rs.getString("LOAN_TP"));
                loanType.setLoanName(rs.getString("LOAN_NAME"));
                loanType.setHaveInt(rs.getString("HAVE_INT"));
                loanTypeList.add(loanType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanTypeList;
    }
    @Override
    public LoanType getLoanTypeDetails(String loanType) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        LoanType loanTypedetails = new LoanType();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT LOAN_TP,LOAN_NAME FROM G_LOAN WHERE LOAN_TP=?");
            pstmt.setString(1, loanType);
            rs = pstmt.executeQuery();
            while(rs.next()){               
                loanTypedetails.setLoanType(rs.getString("LOAN_TP"));
                loanTypedetails.setLoanName(rs.getString("LOAN_NAME"));
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanTypedetails;
    }

}
