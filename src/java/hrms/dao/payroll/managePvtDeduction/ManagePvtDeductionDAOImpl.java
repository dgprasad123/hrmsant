package hrms.dao.payroll.managePvtDeduction;

import hrms.common.AqFunctionalities;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.payroll.managePvtDeduction.ManagePvtDeductionBean;
import hrms.model.payroll.managePvtDeduction.ManagePvtDeductionForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class ManagePvtDeductionDAOImpl implements ManagePvtDeductionDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getCurrentDDOData(String billNo) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList pvtlist = new ArrayList();
        ManagePvtDeductionBean mpdbean = null;
        int totAmount = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT pvt_deduction_id,BILL_DESC,AQ_YEAR,AQ_MONTH,amount,OFF_EN FROM manage_pvt_deduction"
                    + " INNER JOIN bill_mast ON manage_pvt_deduction.bill_no = bill_mast.bill_no"
                    + " inner join g_office on manage_pvt_deduction.off_code=g_office.off_code"
                    + " WHERE manage_pvt_deduction.BILL_NO=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billNo));
            rs = pst.executeQuery();
            while (rs.next()) {
                mpdbean = new ManagePvtDeductionBean();
                mpdbean.setIsDdo("N");
                mpdbean.setPvtdednId(rs.getInt("pvt_deduction_id"));
                mpdbean.setDdoName(rs.getString("OFF_EN"));
                mpdbean.setBillName(rs.getString("BILL_DESC"));
                mpdbean.setMonth(CommonFunctions.getMonthAsString(rs.getInt("AQ_MONTH")) + "-" + rs.getString("AQ_YEAR"));
                totAmount = totAmount + rs.getInt("amount");
                mpdbean.setAmount(rs.getString("amount"));
                pvtlist.add(mpdbean);
            }

            /*int ucbamt = 0;

            sql = "select sum(ad_amt) ad_amt, aq_mast.off_code from aq_mast "
                    + "                        inner join aq_dtls on aq_mast.aqsl_no=aq_dtls.aqsl_no "
                    + "                        and ad_code='UCB' and ad_amt>0 and aq_mast.bill_no=? GROUP BY aq_mast.off_code ";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billNo));
            rs = pst.executeQuery();
            if (rs.next()) {

                ucbamt = rs.getInt("ad_amt");

            }*/

            sql = "SELECT BILL_DESC,AQ_YEAR,AQ_MONTH,PVT_DED_AMT,G_OFFICE.BANK_CODE,G_OFFICE.BRANCH_CODE,IFSC_CODE,MICR_CODE,DDO_CUR_ACC_NO,OFF_EN FROM BILL_MAST "
                    + "INNER JOIN G_OFFICE ON BILL_MAST.OFF_CODE = G_OFFICE.OFF_CODE "
                    + "LEFT OUTER JOIN G_BRANCH ON G_OFFICE.BRANCH_CODE = G_BRANCH.BRANCH_CODE WHERE BILL_NO=? and PVT_DED_AMT>0";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billNo));
            rs = pst.executeQuery();
            if (rs.next()) {
                mpdbean = new ManagePvtDeductionBean();
                mpdbean.setIsDdo("Y");
                mpdbean.setDdoName(rs.getString("OFF_EN"));
                mpdbean.setBillName(rs.getString("BILL_DESC"));
                mpdbean.setMonth(CommonFunctions.getMonthAsString(rs.getInt("AQ_MONTH")) + "-" + rs.getString("AQ_YEAR"));
                mpdbean.setAmount((rs.getInt("PVT_DED_AMT") - totAmount) + "");
                pvtlist.add(mpdbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pvtlist;
    }

    @Override
    public void saveManagePvtDeductionData(ManagePvtDeductionForm mpdform) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();
            String ifsCode = getIFSCode(mpdform.getSltBranch());
            if (mpdform.getPvtdednid() != null && !mpdform.getPvtdednid().equals("")) {
                String sql = "update manage_pvt_deduction set off_code=?,bank_code=?,branch_code=?,bank_acc_no=?,amount=?,ifs_code=? where pvt_deduction_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, mpdform.getSltDDO());
                pst.setString(2, mpdform.getSltBank());
                pst.setString(3, mpdform.getSltBranch());
                pst.setString(4, mpdform.getBankAccNo());
                if (mpdform.getAmount() != null && !mpdform.getAmount().equals("")) {
                    pst.setInt(5, Integer.parseInt(mpdform.getAmount()));
                } else {
                    pst.setInt(5, 0);
                }
                pst.setString(6, ifsCode);
                pst.setInt(7, Integer.parseInt(mpdform.getPvtdednid()));
                pst.executeUpdate();
            } else {
                String sql = "insert into manage_pvt_deduction(bill_no,off_code,bank_code,branch_code,ifs_code,bank_acc_no,amount) values(?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql);
                pst.setInt(1, mpdform.getBillNo());
                pst.setString(2, mpdform.getSltDDO());
                pst.setString(3, mpdform.getSltBank());
                pst.setString(4, mpdform.getSltBranch());
                pst.setString(5, ifsCode);
                pst.setString(6, mpdform.getBankAccNo());
                if (mpdform.getAmount() != null && !mpdform.getAmount().equals("")) {
                    pst.setInt(7, Integer.parseInt(mpdform.getAmount()));
                } else {
                    pst.setInt(7, 0);
                }
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getAddedAccountData(String billNo) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ManagePvtDeductionBean mpdbean = new ManagePvtDeductionBean();
        ArrayList pvtlist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT pvt_deduction_id,BILL_DESC,AQ_YEAR,AQ_MONTH,amount,OFF_EN FROM manage_pvt_deduction"
                    + " INNER JOIN bill_mast ON manage_pvt_deduction.bill_no = bill_mast.bill_no"
                    + " inner join g_office on manage_pvt_deduction.off_code=g_office.off_code"
                    + " WHERE manage_pvt_deduction.BILL_NO=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billNo));
            rs = pst.executeQuery();
            while (rs.next()) {
                mpdbean.setPvtdednId(rs.getInt("pvt_deduction_id"));
                mpdbean.setDdoName(rs.getString("OFF_EN"));
                mpdbean.setBillName(rs.getString("BILL_DESC"));
                mpdbean.setMonth(CommonFunctions.getMonthAsString(rs.getInt("AQ_MONTH")) + "-" + rs.getString("AQ_YEAR"));
                mpdbean.setAmount(rs.getString("amount"));
                pvtlist.add(mpdbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pvtlist;
    }

    @Override
    public int getAddedAccountDataAmount(int billNo) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int amount = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT sum(amount) amt FROM manage_pvt_deduction WHERE manage_pvt_deduction.BILL_NO=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, billNo);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("amt") != null && !rs.getString("amt").equals("")) {
                    amount = rs.getInt("amt");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return amount;
    }

    @Override
    public ManagePvtDeductionForm editManagePvtDedData(ManagePvtDeductionForm mpdform, int pvtid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select manage_pvt_deduction.*,dist_code from manage_pvt_deduction"
                    + " inner join g_office on manage_pvt_deduction.off_code=g_office.off_code where pvt_deduction_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, pvtid);
            rs = pst.executeQuery();
            if (rs.next()) {
                mpdform.setPvtdednid(pvtid + "");
                mpdform.setSltDistrict(rs.getString("dist_code"));
                mpdform.setSltDDO(rs.getString("off_code"));
                mpdform.setSltBank(rs.getString("bank_code"));
                mpdform.setSltBranch(rs.getString("branch_code"));
                mpdform.setBankAccNo(rs.getString("bank_acc_no"));
                mpdform.setAmount(rs.getString("amount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return mpdform;

    }

    @Override
    public int getDDOPvtDednAmount(int billNo) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int ddoAmount = 0;
        
        int aqYear = 0;
        int aqMonth = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT PVT_DED_AMT FROM BILL_MAST WHERE BILL_NO=? and PVT_DED_AMT>0";
            pst = con.prepareStatement(sql);
            pst.setInt(1, billNo);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("PVT_DED_AMT") != null && !rs.getString("PVT_DED_AMT").equals("")) {
                    ddoAmount = rs.getInt("PVT_DED_AMT");
                }
            }
            
            int ucbamt = 0;
            
            DataBaseFunctions.closeSqlObjects(rs, pst);
            
            sql = "select aq_year,aq_month from bill_mast where BILL_NO=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, billNo);
            rs = pst.executeQuery();
            if (rs.next()) {
                aqYear = rs.getInt("aq_year");
                aqMonth = rs.getInt("aq_month");
            }
            
            String aqDTLSTable = AqFunctionalities.getAQBillDtlsTable(aqMonth, aqYear);
            
            sql = "select sum(ad_amt) ad_amt, aq_mast.off_code from aq_mast"
                    + " inner join "+aqDTLSTable+" aq_dtls on aq_mast.aqsl_no=aq_dtls.aqsl_no"
                    + " and ad_code='UCB' and ad_amt>0 and aq_mast.bill_no=? and aq_mast.aq_year=? and aq_mast.aq_month = ? GROUP BY aq_mast.off_code";
            pst = con.prepareStatement(sql);
            pst.setInt(1,billNo);
            pst.setInt(2,aqYear);
            pst.setInt(3,aqMonth);
            rs = pst.executeQuery();
            if (rs.next()) {
                ucbamt = rs.getInt("ad_amt");
            }
            
            ddoAmount = ddoAmount - ucbamt;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ddoAmount;
    }

    @Override
    public int getBillNo(int pvtid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int billNo = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select bill_no from manage_pvt_deduction where pvt_deduction_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, pvtid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("bill_no") != null && !rs.getString("bill_no").equals("")) {
                    billNo = rs.getInt("bill_no");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billNo;
    }

    @Override
    public void deleteManagePvtDeductionData(int pvtid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "delete from manage_pvt_deduction where pvt_deduction_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, pvtid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String getCurrentBillMonthYear(int billno) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String billmonthyear = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select type_of_bill,aq_year,aq_month from bill_mast where bill_no=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, billno);
            rs = pst.executeQuery();
            if (rs.next()) {
                billmonthyear = rs.getString("type_of_bill") + "-" + rs.getInt("aq_month") + "-" + rs.getInt("aq_year");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billmonthyear;
    }
    
    private String getIFSCode(String branchCode){
        
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String ifsCode = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select ifsc_code from g_branch where branch_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, branchCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                ifsCode = rs.getString("ifsc_code");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ifsCode;
    }
}
