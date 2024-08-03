/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.budgetproposal;

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
 * @author Surendra
 */
public class BudgetProposoalDAOImpl implements BudgetProposoalDAO  {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    
    @Override
    public List getFinancialYearList() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        List fylist = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            String sql = "select fy from financial_year order by fy desc";
            pst = con.prepareStatement(sql);
            
            rs = pst.executeQuery();
            while (rs.next()) {
                SelectOption so=new SelectOption();
                so.setLabel(rs.getString("fy"));
                so.setValue(rs.getString("fy"));
                fylist.add(so);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fylist;
    }
    
    
    @Override
    public List getBillList(String offCode, String fy){
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List list =new ArrayList();
        List fylist = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            String sql = "SELECT * FROM BILL_GROUP_MASTER WHERE OFF_CODE='OLSGAD0010000' AND IS_VERIFIED='Y' AND BILL_TYPE='42' ";
            pst = con.prepareStatement(sql);
            
            rs = pst.executeQuery();
            while (rs.next()) {
                SelectOption so=new SelectOption();
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        
        return list;
    }
    
}
