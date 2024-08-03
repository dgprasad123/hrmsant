/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.Bank;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author lenovo pc
 */
public class BankDAOImpl implements BankDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getBankList() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        Bank bank=null;
        ArrayList bankList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT BANK_NAME,BANK_CODE FROM G_BANK ORDER BY BANK_NAME");
            while (rs.next()) {
                bank = new Bank();
                bank.setBankname(rs.getString("BANK_NAME"));
                bank.setBankcode(rs.getString("BANK_CODE"));
                bankList.add(bank);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return bankList;

    }

    @Override
    public void addNewBank(Bank bank) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String bankCode = bank.getMdlBnkCode();
        String bankName = bank.getMdlBnkName();
        String maxBCode = null;
        try {
            con = this.dataSource.getConnection();
            stmt = con.createStatement();
            ps = con.prepareStatement("select cast(cast(max(bank_code) as int) +1 as text) maxBnkCode from g_bank;");
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("maxBnkCode").length() == 2) {
                    maxBCode = "0".concat(rs.getString("maxBnkCode"));
                } else {
                    maxBCode = rs.getString("maxBnkCode");
                }
            }
            ps = con.prepareStatement("INSERT INTO G_BANK(BANK_CODE,BANK_NAME,IS_DELETED)VALUES(?,?,?)");
            ps.setString(1, maxBCode);
            ps.setString(2, bankName.toUpperCase());
            ps.setString(3, "N");
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,stmt);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);           
            
        }
    }

    @Override
    public String getBankName(String bankcode) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String bankName = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT * FROM G_BANK WHERE BANK_CODE=?");
            ps.setString(1, bankcode);
            rs = ps.executeQuery();
            if (rs.next()) {
                bankName = rs.getString("BANK_NAME");
            }

        } catch (Exception e) {
            e.printStackTrace();;
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
            
        }
        return bankName;
    }
}
