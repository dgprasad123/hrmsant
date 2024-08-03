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
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author lenovo pc
 */
public class BranchDAOImpl implements BranchDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getBranchList(String bankcode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList branchList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT * FROM G_BRANCH WHERE BANK_CODE=? AND STATE_CODE='21' ORDER BY BRANCH_NAME");
            ps.setString(1, bankcode);
            rs = ps.executeQuery();
            while (rs.next()) {
                Bank bank = new Bank();
                bank.setBranchcode(rs.getString("BRANCH_CODE"));
                bank.setBranchname(rs.getString("BRANCH_NAME")+"-"+rs.getString("IFSC_CODE"));
                bank.setIfsccode(rs.getString("IFSC_CODE"));
                bank.setMicrcode(rs.getString("MICR_CODE"));
                branchList.add(bank);
            }

        } catch (Exception e) {
            e.printStackTrace();;
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return branchList;
    }

    @Override
    public Bank getBranch(String branchcode) {
        Connection con = null;
        ResultSet rs = null;
        Bank bank = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT * FROM G_BRANCH WHERE BRANCH_CODE=?");
            ps.setString(1, branchcode);
            rs = ps.executeQuery();
            if (rs.next()) {
                bank = new Bank();
                bank.setBranchcode(branchcode);
                bank.setMdlbranchname(rs.getString("BRANCH_NAME"));
                bank.setMdlifsccode(rs.getString("IFSC_CODE"));
                bank.setMdlmicrcode(rs.getString("MICR_CODE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return bank;
    }

    @Override
    public void addNewBranch(Bank bank, String bankcode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Bank gbank = new Bank();
        String newBranchCode = null;
        String branchName = bank.getMdlbranchname();
        String ifscCode = bank.getMdlifsccode();
        String micrCode = bank.getMdlmicrcode();
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("UPDATE g_branch SET STATE_CODE='21', IF_SHOW='Y', IS_DELETED='N' WHERE IFSC_CODE=?");
            ps.setString(1, StringUtils.defaultString(ifscCode).toUpperCase());
            ps.executeUpdate();
            
            ps = con.prepareStatement("select max(branch_code)maxBCode,bank_code from g_branch where length(branch_code)=8 and bank_code=? group by bank_code ");
            ps.setString(1, bankcode);
            rs = ps.executeQuery();

            if (rs.next()) {

                if (rs.getString("maxBCode").length() < 8) {
                    ps = con.prepareStatement("select substring(bank_code,2,2) subst_bank_code from g_bank where bank_code=?");
                    ps.setString(1, bankcode);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        gbank = new Bank();
                        newBranchCode = (rs.getString("subst_bank_code").concat("00000") + 1);
                    }

                } else {
                    ps = con.prepareStatement("select cast((substring(bank_code,2,2)||max(substring(branch_code,3,6))) as int)+1 maxBranchCode from g_branch where state_code='21' "
                            + "and bank_code=? and length(branch_code)=8 group by bank_code");
                    ps.setString(1, bankcode);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        gbank = new Bank();
                        gbank.setMaxbranchcode(rs.getString("maxBranchCode"));

                        if (rs.getString("maxBranchCode").length() == 7) {
                            newBranchCode = ("0".concat(rs.getString("maxBranchCode")));
                        } else {
                            newBranchCode = rs.getString("maxBranchCode");
                        }
                    }
                }

            } else {
                ps = con.prepareStatement("select substring(bank_code,2,2) subst_bank_code from g_bank where bank_code=?");
                ps.setString(1, bankcode);
                rs = ps.executeQuery();
                if (rs.next()) {
                    gbank = new Bank();
                    newBranchCode = (rs.getString("subst_bank_code").concat("00000") + 1);
                }
            }

            ps = con.prepareStatement("INSERT INTO G_BRANCH(BRANCH_CODE,BRANCH_NAME,BANK_CODE,IFSC_CODE,MICR_CODE,STATE_CODE,DIST_CODE,IF_SHOW,IS_DELETED)VALUES(?,?,?,?,?,?,?,?,?)");
            ps.setString(1, newBranchCode);
            ps.setString(2, StringUtils.defaultString(branchName).toUpperCase());
            ps.setString(3, bankcode);
            ps.setString(4, StringUtils.defaultString(ifscCode).toUpperCase());
            ps.setString(5, StringUtils.defaultString(micrCode).toUpperCase());
            ps.setString(6, "21");
            ps.setString(7, "");
            ps.setString(8, "Y");
            ps.setString(9, "N");
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);

        }
    }

    @Override
    public void updateBranch(Bank bank) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("UPDATE G_BRANCH SET BRANCH_NAME=?,IFSC_CODE=?,MICR_CODE=?,IF_SHOW='Y' WHERE BRANCH_CODE=?");
            ps.setString(1, bank.getMdlbranchname());
            ps.setString(2, bank.getMdlifsccode());
            ps.setString(3, bank.getMdlmicrcode());
            ps.setString(4, bank.getHidBranchCode());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);

        }
    }

}
