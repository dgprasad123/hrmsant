/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.billmast;

import hrms.common.AqFunctionalities;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.employee.ArrEmpBillGroup;
import hrms.model.payroll.billmast.BillMastModel;
import hrms.model.payroll.billmast.BillStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class BillMastDAOImpl implements BillMastDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

        @Override
    public int saveBillMast(BillMastModel bmModel) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Calendar cal = Calendar.getInstance();
        int result = 0;
        int maxId = 0;

        try {
            con = dataSource.getConnection();

            if (bmModel.getBillNo() <= 0) {
                

                String insertQry = "INSERT INTO bill_mast( bill_desc, bill_date, aq_group_desc, aq_month, aq_year, off_ddo, plan, sector,"//9
                        + "major_head, major_head_desc, sub_major_head, sub_major_head_desc, minor_head, minor_head_desc, sub_minor_head1,"//7
                        + "sub_minor_head1_desc, sub_minor_head2, sub_minor_head2_desc, sub_minor_head3, tr_code, tr_officer, vch_no, vch_date, mode,"//9
                        + "bank_code, branch_code,  amt_paid, rec_by, desg, notif_no, gross_amt, ded_amt, pvt_ded_amt, vouchered, off_code,"//12
                        + "bill_group_desc, demand_no, ddo_code, token_no, token_date, bill_status_id, ben_ref_no, bill_group_id, previous_token_no,"//9
                        + "previous_token_date, is_resubmitted, is_bill_prepared, previous_bill_no, bill_type, type_of_bill,reprocess_occurance,is_cleaned"
                        + "ddo_post, ddo_emp_id)"//2
                        + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";//55
                pstmt = con.prepareStatement(insertQry,  Statement.RETURN_GENERATED_KEYS);

                
                pstmt.setString(1, bmModel.getBillDesc());
                pstmt.setTimestamp(2, new Timestamp(bmModel.getBillDate().getTime()));
                pstmt.setString(3, bmModel.getAqGroupDesc());
                pstmt.setInt(4, bmModel.getAqMonth());
                pstmt.setInt(5, bmModel.getAqYear());
                pstmt.setString(6, bmModel.getOffDDO());
                pstmt.setString(7, bmModel.getPlan());
                pstmt.setString(8, bmModel.getSector());
                //bill_no, bill_desc, bill_date, aq_group_desc, aq_month, aq_year, off_ddo, plan, sector,

                pstmt.setString(9, bmModel.getMajorHead());
                pstmt.setString(10, bmModel.getMajorHeadDesc());
                pstmt.setString(11, bmModel.getSubMajorHead());
                pstmt.setString(12, bmModel.getSubMajorHeadDesc());
                pstmt.setString(13, bmModel.getMinorHead());
                pstmt.setString(14, bmModel.getMinorHeadDesc());
                pstmt.setString(15, bmModel.getSubMinorHead1());
                //major_head, major_head_desc, sub_major_head, sub_major_head_desc, minor_head, minor_head_desc, sub_minor_head1,

                pstmt.setString(16, bmModel.getSubMinorHead1Desc());
                pstmt.setString(17, bmModel.getSubMinorHead2());
                pstmt.setString(18, bmModel.getSubMinorHead2Desc());
                pstmt.setString(19, bmModel.getSubMinorHead3());
                pstmt.setString(20, bmModel.getTrCode());
                pstmt.setString(21, bmModel.getTrOfficer());
                pstmt.setString(22, bmModel.getVchNo());
                pstmt.setTimestamp(23, new Timestamp(bmModel.getVchDate().getTime()));
                pstmt.setString(24, bmModel.getMode());
                //sub_minor_head1_desc, sub_minor_head2, sub_minor_head2_desc, sub_minor_head3, tr_code, tr_officer, vch_no, vch_date, mode,

                pstmt.setString(25, bmModel.getBankCode());
                pstmt.setString(26, bmModel.getBranchCode());

                pstmt.setInt(27, bmModel.getAmtPaid());
                pstmt.setString(28, bmModel.getRecBy());
                pstmt.setString(29, bmModel.getDesg());
                pstmt.setString(30, bmModel.getNotifNo());
                pstmt.setInt(31, bmModel.getGrossAmt());
                pstmt.setInt(32, bmModel.getDedAmt());
                pstmt.setInt(33, bmModel.getPvtDedAmt());
                pstmt.setString(34, bmModel.getVouchered());
                pstmt.setString(35, bmModel.getOffCode());
                //bank_code, branch_code, instr_no, amt_paid, rec_by, desg, notif_no, gross_amt, ded_amt, pvt_ded_amt, vouchered, off_code,

                pstmt.setString(36, bmModel.getBillGroupDesc());
                pstmt.setString(37, bmModel.getDemandNo());
                pstmt.setString(38, bmModel.getDdoCode());
                pstmt.setString(39, bmModel.getTokenNo());
                pstmt.setTimestamp(40, new Timestamp(bmModel.getTokenDate().getTime()));
                pstmt.setInt(41, bmModel.getBillStatusId());
                pstmt.setString(42, bmModel.getBenRefNo());
                pstmt.setLong(43, bmModel.getBillGroupId());
                pstmt.setString(44, bmModel.getPreviousTokenNo());
                //bill_group_desc, demand_no, ddo_code, token_no, token_date, bill_status_id, ben_ref_no, bill_group_id, previous_token_no,    

                pstmt.setTimestamp(45, new Timestamp(bmModel.getTokenDate().getTime()));
                pstmt.setString(46, bmModel.getIsResubmitted());
                pstmt.setString(47, bmModel.getIsBillPrepared());
                pstmt.setInt(48, bmModel.getPreviousBillNo());
                pstmt.setString(49, bmModel.getBillType());
                pstmt.setString(50, bmModel.getTypeOfBill());
                pstmt.setInt(51, bmModel.getReprocessOccurance());
                pstmt.setString(52, bmModel.getIsCleaned());
                //previous_token_date, is_resubmitted, is_bill_prepared, previous_bill_no, bill_type, type_of_bill,reprocess_occurance,is_cleaned

                pstmt.setString(53, bmModel.getDdoSpc());
                pstmt.setString(54, bmModel.getDdoEmpId());
                //ddo_spc, ddo_emp_id    
                result = pstmt.executeUpdate();
                rs = pstmt.getGeneratedKeys();
                rs.next();
                maxId = rs.getInt("bill_no");
            } else if (bmModel.getBillNo() > 0) {

                String updQry = "UPDATE bill_mast SET bill_desc=?, bill_date=?, aq_group_desc=?, aq_month=?, aq_year=?, off_ddo=?, plan=?, sector=?,"
                        + "major_head=?, major_head_desc=?, sub_major_head=?, sub_major_head_desc=?, minor_head=?, minor_head_desc=?, sub_minor_head1=?,"
                        + "sub_minor_head1_desc=?, sub_minor_head2=?, sub_minor_head2_desc=?, sub_minor_head3=?, tr_code=?, tr_officer=?, vch_no=?, "
                        + "vch_date=?, mode=?,bank_code=?, branch_code=?, amt_paid=?, rec_by=?, desg=?, notif_no=?, gross_amt=?, ded_amt=?,"
                        + "pvt_ded_amt=?, vouchered=?, off_code=?,bill_group_desc=?, demand_no=?, ddo_code=?, token_no=?, token_date=?, bill_status_id=?,"
                        + "ben_ref_no=?, bill_group_id=?, previous_token_no=?,previous_token_date=?, is_resubmitted=?, is_bill_prepared=?, previous_bill_no=?,"
                        + "bill_type=?, type_of_bill=?,reprocess_occurance=?,is_cleaned=?,ddo_post=?, ddo_emp_id=? WHERE bill_no=?";

                pstmt = con.prepareStatement(updQry);

                pstmt.setString(1, bmModel.getBillDesc());
                pstmt.setTimestamp(2, new Timestamp(bmModel.getBillDate().getTime()));
                pstmt.setString(3, bmModel.getAqGroupDesc());
                pstmt.setInt(4, bmModel.getAqMonth());
                pstmt.setInt(5, bmModel.getAqYear());
                pstmt.setString(6, bmModel.getOffDDO());
                pstmt.setString(7, bmModel.getPlan());
                pstmt.setString(8, bmModel.getSector());
                //bill_no, bill_desc, bill_date, aq_group_desc, aq_month, aq_year, off_ddo, plan, sector,

                pstmt.setString(9, bmModel.getMajorHead());
                pstmt.setString(10, bmModel.getMajorHeadDesc());
                pstmt.setString(11, bmModel.getSubMajorHead());
                pstmt.setString(12, bmModel.getSubMajorHeadDesc());
                pstmt.setString(13, bmModel.getMinorHead());
                pstmt.setString(14, bmModel.getMinorHeadDesc());
                pstmt.setString(15, bmModel.getSubMinorHead1());
                //major_head, major_head_desc, sub_major_head, sub_major_head_desc, minor_head, minor_head_desc, sub_minor_head1,

                pstmt.setString(16, bmModel.getSubMinorHead1Desc());
                pstmt.setString(17, bmModel.getSubMinorHead2());
                pstmt.setString(18, bmModel.getSubMinorHead2Desc());
                pstmt.setString(19, bmModel.getSubMinorHead3());
                pstmt.setString(20, bmModel.getTrCode());
                pstmt.setString(21, bmModel.getTrOfficer());
                pstmt.setString(22, bmModel.getVchNo());
                pstmt.setTimestamp(23, new Timestamp(bmModel.getVchDate().getTime()));
                pstmt.setString(24, bmModel.getMode());
                //sub_minor_head1_desc, sub_minor_head2, sub_minor_head2_desc, sub_minor_head3, tr_code, tr_officer, vch_no, vch_date, mode,

                pstmt.setString(25, bmModel.getBankCode());
                pstmt.setString(26, bmModel.getBranchCode());
                pstmt.setInt(27, bmModel.getAmtPaid());
                pstmt.setString(28, bmModel.getRecBy());
                pstmt.setString(29, bmModel.getDesg());
                pstmt.setString(30, bmModel.getNotifNo());
                pstmt.setInt(31, bmModel.getGrossAmt());
                pstmt.setInt(32, bmModel.getDedAmt());
                pstmt.setInt(33, bmModel.getPvtDedAmt());
                pstmt.setString(34, bmModel.getVouchered());
                pstmt.setString(35, bmModel.getOffCode());
                //bank_code, branch_code, amt_paid, rec_by, desg, notif_no, gross_amt, ded_amt, pvt_ded_amt, vouchered, off_code,

                pstmt.setString(36, bmModel.getBillGroupDesc());
                pstmt.setString(37, bmModel.getDemandNo());
                pstmt.setString(38, bmModel.getDdoCode());
                pstmt.setString(39, bmModel.getTokenNo());
                pstmt.setTimestamp(40, new Timestamp(bmModel.getTokenDate().getTime()));
                pstmt.setInt(41, bmModel.getBillStatusId());
                pstmt.setString(42, bmModel.getBenRefNo());
                pstmt.setLong(43, bmModel.getBillGroupId());
                pstmt.setString(44, bmModel.getPreviousTokenNo());
                //bill_group_desc, demand_no, ddo_code, token_no, token_date, bill_status_id, ben_ref_no, bill_group_id, previous_token_no,    

                pstmt.setTimestamp(45, new Timestamp(bmModel.getTokenDate().getTime()));
                pstmt.setString(46, bmModel.getIsResubmitted());
                pstmt.setString(47, bmModel.getIsBillPrepared());
                pstmt.setInt(48, bmModel.getPreviousBillNo());
                pstmt.setString(49, bmModel.getBillType());
                pstmt.setString(50, bmModel.getTypeOfBill());
                pstmt.setInt(51, bmModel.getReprocessOccurance());
                pstmt.setString(52, bmModel.getIsCleaned());
                //previous_token_date, is_resubmitted, is_bill_prepared, previous_bill_no, bill_type, type_of_bill,reprocess_occurance,is_cleaned

                pstmt.setString(53, bmModel.getDdoSpc());
                pstmt.setString(54, bmModel.getDdoEmpId());
                //ddo_spc=?, ddo_emp_id=?

                pstmt.setInt(55, bmModel.getBillNo());
                result = pstmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return result;
    }

    @Override
    public BillMastModel getBillMastDetails(int billId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BillMastModel bmModel = new BillMastModel();

        try {
            con = this.repodataSource.getConnection();
            String selectQry = "SELECT bill_no, bill_desc, bill_date, aq_group_desc, aq_month, aq_year, off_ddo, plan, sector, major_head, major_head_desc,"
                    + "sub_major_head, sub_major_head_desc, minor_head, minor_head_desc, sub_minor_head1,sub_minor_head1_desc, sub_minor_head2, "
                    + "sub_minor_head2_desc, sub_minor_head3, tr_code, tr_officer, vch_no, vch_date, mode,bank_code, branch_code, amt_paid, "
                    + "rec_by, desg, notif_no, gross_amt, ded_amt, pvt_ded_amt, vouchered, off_code,bill_group_desc, demand_no, ddo_code, token_no, "
                    + "token_date, bill_status_id, ben_ref_no, bill_group_id, previous_token_no,previous_token_date, is_resubmitted, is_bill_prepared, "
                    + "previous_bill_no, bill_type, type_of_bill,reprocess_occurance,is_cleaned,ddo_post, ddo_empid FROM bill_mast WHERE bill_no=?";

            pstmt = con.prepareStatement(selectQry);
            pstmt.setInt(1, billId);
            rs = pstmt.executeQuery();
            while (rs.next()) {

                bmModel.setBillNo(billId);
                bmModel.setBillDesc(rs.getString("bill_desc"));
                bmModel.setBillDate(rs.getDate("bill_date"));
                bmModel.setAqGroupDesc(rs.getString("aq_group_desc"));
                bmModel.setAqMonth(rs.getInt("aq_month"));
                bmModel.setAqYear(rs.getInt("aq_year"));
                bmModel.setOffDDO(rs.getString("off_ddo"));
                bmModel.setPlan(rs.getString("plan"));
                bmModel.setSector(rs.getString("sector"));
                bmModel.setMajorHead(rs.getString("major_head"));
                bmModel.setMajorHeadDesc(rs.getString("major_head_desc"));
                bmModel.setSubMajorHead(rs.getString("sub_major_head"));
                bmModel.setSubMajorHeadDesc(rs.getString("sub_major_head_desc"));
                bmModel.setMinorHead(rs.getString("minor_head"));
                bmModel.setMinorHeadDesc(rs.getString("minor_head_desc"));
                bmModel.setSubMinorHead1(rs.getString("sub_minor_head1"));
                bmModel.setSubMinorHead1Desc(rs.getString("sub_minor_head1_desc"));
                bmModel.setSubMinorHead2(rs.getString("sub_minor_head2"));
                bmModel.setSubMinorHead2Desc(rs.getString("sub_minor_head2_desc"));
                bmModel.setSubMinorHead3(rs.getString("sub_minor_head3"));
                bmModel.setTrCode(rs.getString("tr_code"));
                bmModel.setTrOfficer(rs.getString("tr_officer"));
                bmModel.setVchNo(rs.getString("vch_no"));
                bmModel.setVchDate(rs.getDate("vch_date"));
                bmModel.setMode(rs.getString("mode"));
                bmModel.setBankCode(rs.getString("bank_code"));
                bmModel.setBranchCode(rs.getString("branch_code"));
                bmModel.setAmtPaid(rs.getInt("amt_paid"));
                bmModel.setRecBy(rs.getString("rec_by"));
                bmModel.setDesg(rs.getString("desg"));
                bmModel.setNotifNo(rs.getString("notif_no"));
                bmModel.setGrossAmt(rs.getInt("gross_amt"));
                bmModel.setDedAmt(rs.getInt("ded_amt"));
                bmModel.setPvtDedAmt(rs.getInt("pvt_ded_amt"));
                bmModel.setVouchered(rs.getString("vouchered"));
                bmModel.setOffCode(rs.getString("off_code"));
                bmModel.setBillGroupDesc(rs.getString("bill_group_desc"));
                bmModel.setDemandNo(rs.getString("demand_no"));
                bmModel.setDdoCode(rs.getString("ddo_code"));
                bmModel.setTokenNo(rs.getString("token_no"));
                bmModel.setTokenDate(rs.getDate("token_date"));
                bmModel.setBillStatusId(rs.getInt("bill_status_id"));
                bmModel.setBenRefNo(rs.getString("ben_ref_no"));
                bmModel.setBillGroupId(rs.getLong("bill_group_id"));
                bmModel.setPreviousTokenNo(rs.getString("previous_token_no"));
                bmModel.setTokenDate(rs.getDate("previous_token_date"));
                bmModel.setIsResubmitted(rs.getString("is_resubmitted"));
                bmModel.setIsBillPrepared(rs.getString("is_bill_prepared"));
                bmModel.setPreviousBillNo(rs.getInt("previous_bill_no"));
                bmModel.setBillType(rs.getString("bill_type"));
                bmModel.setTypeOfBill(rs.getString("type_of_bill"));
                bmModel.setReprocessOccurance(rs.getInt("reprocess_occurance"));
                bmModel.setIsCleaned(rs.getString("is_cleaned"));
                bmModel.setDdoSpc(rs.getString("ddo_post"));
                bmModel.setDdoEmpId(rs.getString("ddo_empid"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return bmModel;
    }

    @Override
    public List getBillList(int year, int month, String offcode, String billType, String spc) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Calendar cal = Calendar.getInstance();
        List billList = new ArrayList();
        BillMastModel bmModel = null;

        try {
            con = this.repodataSource.getConnection();
            String selectQry = "SELECT bill_no, bill_desc, bill_date, aq_group_desc, aq_month, aq_year, off_ddo, plan, sector, major_head, major_head_desc,"
                    + "sub_major_head, sub_major_head_desc, minor_head, minor_head_desc, sub_minor_head1,sub_minor_head1_desc, sub_minor_head2, "
                    + "sub_minor_head2_desc, sub_minor_head3, tr_code, tr_officer, vch_no, vch_date, mode,bank_code, branch_code, amt_paid, rec_by,"
                    + " desg, notif_no, gross_amt, ded_amt, pvt_ded_amt, vouchered, off_code,bill_group_desc, demand_no, ddo_code, token_no, "
                    + "token_date, bill_status_id, ben_ref_no, bill_group_id, previous_token_no,previous_token_date, is_resubmitted, is_bill_prepared, "
                    + "previous_bill_no, bill_type, type_of_bill, reprocess_occurance, is_cleaned, ddo_post, ddo_emp_id FROM bill_mast WHERE off_code='" + offcode + "'"
                    + "bill_type='" + billType + "' AND aq_month='" + month + "' AND aq_year='" + year + "' AND ddo_post='" + spc + "'";

            pstmt = con.prepareStatement(selectQry);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                bmModel = new BillMastModel();

                bmModel.setBillNo(rs.getInt("bill_no"));
                bmModel.setBillDesc(rs.getString("bill_desc"));
                bmModel.setBillDate(rs.getDate("bill_date"));
                bmModel.setAqGroupDesc(rs.getString("aq_group_desc"));
                bmModel.setAqMonth(rs.getInt("aq_month"));
                bmModel.setAqYear(rs.getInt("aq_year"));
                bmModel.setOffDDO(rs.getString("off_ddo"));
                bmModel.setPlan(rs.getString("plan"));
                bmModel.setSector(rs.getString("sector"));
                bmModel.setMajorHead(rs.getString("major_head"));
                bmModel.setMajorHeadDesc(rs.getString("major_head_desc"));
                //bill_no, bill_desc, bill_date, aq_group_desc, aq_month, aq_year, off_ddo, plan, sector, major_head, major_head_desc,    

                bmModel.setSubMajorHead(rs.getString("sub_major_head"));
                bmModel.setSubMajorHeadDesc(rs.getString("sub_major_head_desc"));
                bmModel.setMinorHead(rs.getString("minor_head"));
                bmModel.setMinorHeadDesc(rs.getString("minor_head_desc"));
                bmModel.setSubMinorHead1(rs.getString("sub_minor_head1"));
                bmModel.setSubMinorHead1Desc(rs.getString("sub_minor_head1_desc"));
                bmModel.setSubMinorHead2(rs.getString("sub_minor_head2"));
                // sub_major_head, sub_major_head_desc, minor_head, minor_head_desc, sub_minor_head1,sub_minor_head1_desc, sub_minor_head2,     

                bmModel.setSubMinorHead2Desc(rs.getString("sub_minor_head2_desc"));
                bmModel.setSubMinorHead3(rs.getString("sub_minor_head3"));
                bmModel.setTrCode(rs.getString("tr_code"));
                bmModel.setTrOfficer(rs.getString("tr_officer"));
                bmModel.setVchNo(rs.getString("vch_no"));
                bmModel.setVchDate(rs.getDate("vch_date"));
                bmModel.setMode(rs.getString("mode"));

                bmModel.setBankCode(rs.getString("bank_code"));
                bmModel.setBranchCode(rs.getString("branch_code"));
                bmModel.setAmtPaid(rs.getInt("amt_paid"));
                //sub_minor_head2_desc, sub_minor_head3, tr_code, tr_officer, vch_no, vch_date, mode,bank_code, branch_code, amt_paid,

                bmModel.setRecBy(rs.getString("rec_by"));
                bmModel.setDesg(rs.getString("desg"));
                bmModel.setNotifNo(rs.getString("notif_no"));
                bmModel.setGrossAmt(rs.getInt("gross_amt"));
                bmModel.setDedAmt(rs.getInt("ded_amt"));
                bmModel.setPvtDedAmt(rs.getInt("pvt_ded_amt"));
                bmModel.setVouchered(rs.getString("vouchered"));
                bmModel.setOffCode(rs.getString("off_code"));
                bmModel.setBillGroupDesc(rs.getString("bill_group_desc"));
                bmModel.setDemandNo(rs.getString("demand_no"));
                bmModel.setDdoCode(rs.getString("ddo_code"));
                bmModel.setTokenNo(rs.getString("token_no"));
                //rec_by, desg, notif_no, gross_amt, ded_amt, pvt_ded_amt, vouchered, off_code,bill_group_desc, demand_no, ddo_code, token_no,

                bmModel.setTokenDate(rs.getDate("token_date"));
                bmModel.setBillStatusId(rs.getInt("bill_status_id"));
                bmModel.setBenRefNo(rs.getString("ben_ref_no"));
                bmModel.setBillGroupId(rs.getInt("bill_group_id"));
                bmModel.setPreviousTokenNo(rs.getString("previous_token_no"));
                bmModel.setTokenDate(rs.getDate("previous_token_date"));
                bmModel.setIsResubmitted(rs.getString("is_resubmitted"));
                bmModel.setIsBillPrepared(rs.getString("is_bill_prepared"));
                //token_date, bill_status_id, ben_ref_no, bill_group_id, previous_token_no,previous_token_date, is_resubmitted, is_bill_prepared,

                bmModel.setPreviousBillNo(rs.getInt("previous_bill_no"));
                bmModel.setBillType(rs.getString("bill_type"));
                bmModel.setTypeOfBill(rs.getString("type_of_bill"));
                bmModel.setReprocessOccurance(rs.getInt("reprocess_occurance"));
                bmModel.setIsCleaned(rs.getString("is_cleaned"));
                //previous_bill_no, bill_type, type_of_bill,reprocess_occurance,is_cleaned

                bmModel.setDdoSpc(rs.getString("ddo_post"));
                bmModel.setDdoEmpId(rs.getString("ddo_emp_id"));
                //ddo_spc, ddo_emp_id

                billList.add(bmModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billList;
    }

    @Override
    public int deleteBill(int billid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int result = 0;

        try {
            con = dataSource.getConnection();
            String delQry = "DELETE FROM bill_mast WHERE bill_no=?";
            pstmt = con.prepareStatement(delQry);
            pstmt.setInt(1, billid);

            result = pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return result;

    }

    @Override
    public int getBillStatus(int billid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int status = 0;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT BILL_STATUS_ID FROM BILL_MAST WHERE BILL_NO=?");
            pstmt.setInt(1, billid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                status = rs.getInt("BILL_STATUS_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;
    }

    @Override
    public void updateBillStatus(int billid, int billStatusId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE BILL_MAST SET BILL_STATUS_ID=? WHERE BILL_NO=?");
            pstmt.setInt(1, billStatusId);
            pstmt.setInt(2, billid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void markBillAsPrepared(int billid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE BILL_MAST SET IS_BILL_PREPARED = 'Y' WHERE BILL_NO=?");
            pstmt.setInt(1, billid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void updateBillTotaling(int billid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String aqdtlsTblName = "";
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT TYPE_OF_BILL,aq_month,aq_year  FROM BILL_MAST WHERE BILL_NO=?");
            pstmt.setInt(1, billid);
            rs = pstmt.executeQuery();
            String typeofbill = "PAY";
            if (rs.next()) {
                typeofbill = rs.getString("TYPE_OF_BILL");
                aqdtlsTblName = AqFunctionalities.getAQBillDtlsTable(rs.getInt("aq_month"), rs.getInt("aq_year"));
            }
            if (typeofbill.equals("PAY")) {

                pstmt = con.prepareStatement("UPDATE AQ_MAST SET gross_amt = getempgrosstotal(AQSL_NO,'" + aqdtlsTblName + "') WHERE BILL_NO=?");
                pstmt.setInt(1, billid);
                pstmt.executeUpdate();
                pstmt = con.prepareStatement("UPDATE AQ_MAST SET ded_amt = getempdedtotal(AQSL_NO,'" + aqdtlsTblName + "') WHERE BILL_NO=?");
                pstmt.setInt(1, billid);
                pstmt.executeUpdate();
                pstmt = con.prepareStatement("UPDATE AQ_MAST SET pvt_ded_amt = getemppvtdedtotal(AQSL_NO,'" + aqdtlsTblName + "') WHERE BILL_NO=?");
                pstmt.setInt(1, billid);
                pstmt.executeUpdate();
                pstmt = con.prepareStatement("UPDATE BILL_MAST SET gross_amt = (SELECT SUM(gross_amt) FROM AQ_MAST WHERE BILL_NO=?) WHERE BILL_NO=?");
                pstmt.setInt(1, billid);
                pstmt.setInt(2, billid);
                pstmt.executeUpdate();
                pstmt = con.prepareStatement("UPDATE BILL_MAST SET ded_amt = (SELECT SUM(ded_amt) FROM AQ_MAST WHERE BILL_NO=?) WHERE BILL_NO=?");
                pstmt.setInt(1, billid);
                pstmt.setInt(2, billid);
                pstmt.executeUpdate();
                pstmt = con.prepareStatement("UPDATE BILL_MAST SET pvt_ded_amt = (SELECT SUM(pvt_ded_amt) FROM AQ_MAST WHERE BILL_NO=?) WHERE BILL_NO=?");
                pstmt.setInt(1, billid);
                pstmt.setInt(2, billid);
                pstmt.executeUpdate();
            } else if (typeofbill.contains("ARREAR")) {
                pstmt = con.prepareStatement("UPDATE BILL_MAST SET GROSS_AMT = getbilltotgross_arrear(BILL_NO),DED_AMT = getbilltotded_arrear(BILL_NO) WHERE BILL_NO=?");
                pstmt.setInt(1, billid);
                pstmt.execute();

                pstmt = con.prepareStatement("UPDATE arr_mast SET gross_amt = getempgrosstotal_arrear(aqsl_no) WHERE bill_no=?");
                pstmt.setInt(1, billid);
                pstmt.executeUpdate();

                pstmt = con.prepareStatement("UPDATE arr_mast SET ded_amt = getempdedtotal_arrear(aqsl_no) WHERE bill_no=?");
                pstmt.setInt(1, billid);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getDeptWisePayBillStatus(int month, int year) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        List list = null;
        ArrayList outList = new ArrayList();
        ArrEmpBillGroup eList = null;
        List empList = null;
        String stm = null;
        try {
            con = this.repodataSource.getConnection();
            stm = ("SELECT G_DEPARTMENT.DEPARTMENT_CODE,DEPARTMENT_NAME ,TOTAL_DDO,DDO_PREPARED ,BILL_PREPARED  ,DDO_SUBMITTED  ,BILL_SUBMITTED ,TOKEN_PREPARED  FROM  "
                    + "(SELECT DEPARTMENT_CODE,DEPARTMENT_NAME FROM G_DEPARTMENT WHERE IF_ACTIVE='Y' ORDER BY DEPARTMENT_NAME)G_DEPARTMENT LEFT OUTER JOIN  "
                    + "(SELECT DEPARTMENT_CODE,COUNT(*)DDO_SUBMITTED,SUM(F1)BILL_SUBMITTED FROM G_OFFICE INNER JOIN  "
                    + "(SELECT OFF_CODE,COUNT(*) F1 FROM BILL_MAST WHERE AQ_MONTH=? AND AQ_YEAR=? AND BILL_STATUS_ID>2  GROUP BY OFF_CODE)T1  "
                    + "ON G_OFFICE.OFF_CODE = T1.OFF_CODE GROUP BY DEPARTMENT_CODE)T2  "
                    + "ON G_DEPARTMENT.DEPARTMENT_CODE = T2.DEPARTMENT_CODE "
                    + "LEFT OUTER JOIN "
                    + "(SELECT DEPARTMENT_CODE,COUNT(*)DDO_PREPARED,SUM(F1)BILL_PREPARED FROM G_OFFICE INNER JOIN  "
                    + "(SELECT OFF_CODE,COUNT(*) F1 FROM BILL_MAST WHERE AQ_MONTH=? AND AQ_YEAR=?  GROUP BY OFF_CODE)T3  "
                    + "ON G_OFFICE.OFF_CODE = T3.OFF_CODE WHERE  G_OFFICE.is_ddo='Y' GROUP BY DEPARTMENT_CODE)T4  "
                    + "ON G_DEPARTMENT.DEPARTMENT_CODE = T4.DEPARTMENT_CODE              "
                    + "LEFT OUTER JOIN  "
                    + "(SELECT DEPARTMENT_CODE,COUNT(*)DDO_TOKEN,SUM(F1)TOKEN_PREPARED FROM G_OFFICE INNER JOIN  "
                    + "(SELECT OFF_CODE,COUNT(*) F1 FROM BILL_MAST WHERE AQ_MONTH=? AND AQ_YEAR=? AND (BILL_STATUS_ID > 4 AND BILL_STATUS_ID < 8) GROUP BY OFF_CODE)T5  "
                    + "ON G_OFFICE.OFF_CODE = T5.OFF_CODE GROUP BY DEPARTMENT_CODE)T6  "
                    + "ON G_DEPARTMENT.DEPARTMENT_CODE = T6.DEPARTMENT_CODE"
                    + " LEFT OUTER JOIN "
                    + " (SELECT DEPARTMENT_CODE,COUNT(*) TOTAL_DDO FROM G_OFFICE WHERE OFF_STATUS ='F' AND IS_DDO='Y' GROUP BY DEPARTMENT_CODE)T10 ON G_DEPARTMENT.DEPARTMENT_CODE = T10.DEPARTMENT_CODE");
            ps1 = con.prepareStatement(stm);
            ps1.setInt(1, month);
            ps1.setInt(2, year);

            
            ps1.setInt(3, month);
            ps1.setInt(4, year);
            
            
            ps1.setInt(5, month);
            ps1.setInt(6, year);
            
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                BillStatus billstatus = new BillStatus();
                billstatus.setDepartmentname(rs1.getString("department_name"));
                billstatus.setTotalDDO(rs1.getInt("total_ddo"));
                billstatus.setDdoPrepared(rs1.getInt("ddo_prepared"));
                billstatus.setBillPrepared(rs1.getInt("bill_prepared"));
                billstatus.setDdoSubmitted(rs1.getInt("ddo_submitted"));
                billstatus.setBillSubmitted(rs1.getInt("bill_submitted"));
                billstatus.setTokenPrepared(rs1.getInt("token_prepared"));
                billstatus.setDeptCode(rs1.getString("DEPARTMENT_CODE"));
                outList.add(billstatus);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList DistWisePayBillReport(int month, int year, String typeOfBill) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        List list = null;
        ArrayList outList = new ArrayList();
        ArrEmpBillGroup eList = null;
        List empList = null;
        String stm = null;
        int paybilltaskcount = 0;
        try {
            con = this.repodataSource.getConnection();
            /*ps=con.prepareStatement("select count(*) cnt from paybill_task  where AQ_MONTH=" + month + " AND AQ_YEAR=" + year);
             if (rs.next()) {
             paybilltaskcount = rs.getInt("cnt");
             }*/


            stm = ("SELECT G_DISTRICT.DIST_CODE,DIST_NAME,TOTAL_DDO,DDO_PREPARED ,BILL_PREPARED  ,DDO_SUBMITTED  ,BILL_SUBMITTED ,TOKEN_PREPARED, BILL_ERROR  FROM "
                    + "(SELECT DIST_CODE,DIST_NAME FROM G_DISTRICT WHERE STATE_CODE='21' and int_district_id is not null ORDER BY DIST_NAME)G_DISTRICT"
                    + "  LEFT OUTER JOIN "
                    + "(SELECT DIST_CODE,COUNT(*)DDO_SUBMITTED,SUM(F1)BILL_SUBMITTED FROM G_OFFICE INNER JOIN "
                    + "(SELECT OFF_CODE,COUNT(*) F1 FROM BILL_MAST WHERE AQ_MONTH=? AND AQ_YEAR=? and type_of_bill=? AND BILL_STATUS_ID >2 GROUP BY OFF_CODE)T1 "
                    + "ON G_OFFICE.OFF_CODE = T1.OFF_CODE GROUP BY DIST_CODE)T2 "
                    + "ON G_DISTRICT.DIST_CODE = T2.DIST_CODE "
                    + "  LEFT OUTER JOIN "
                    + "(SELECT DIST_CODE,COUNT(*)DDO_ERROR,SUM(F1) BILL_ERROR FROM G_OFFICE INNER JOIN "
                    + "(SELECT OFF_CODE,COUNT(*) F1 FROM BILL_MAST WHERE AQ_MONTH=? AND AQ_YEAR=? and type_of_bill=? AND BILL_STATUS_ID=4 GROUP BY OFF_CODE)T1 "
                    + "ON G_OFFICE.OFF_CODE = T1.OFF_CODE GROUP BY DIST_CODE)T3 "
                    + "ON G_DISTRICT.DIST_CODE = T3.DIST_CODE "
                    + "LEFT OUTER JOIN "
                    + "(SELECT DIST_CODE,COUNT(*)DDO_PREPARED,SUM(F1)BILL_PREPARED FROM G_OFFICE INNER JOIN "
                    + "(SELECT OFF_CODE,COUNT(*) F1 FROM BILL_MAST WHERE AQ_MONTH=? AND AQ_YEAR=? and type_of_bill=?  GROUP BY OFF_CODE)T3 "
                    + "ON G_OFFICE.OFF_CODE = T3.OFF_CODE GROUP BY DIST_CODE)T4 "
                    + "ON G_DISTRICT.DIST_CODE = T4.DIST_CODE "
                    + "LEFT OUTER JOIN "
                    + "(SELECT DIST_CODE,COUNT(*)DDO_TOKEN,SUM(F1)TOKEN_PREPARED FROM G_OFFICE INNER JOIN "
                    + "(SELECT OFF_CODE,COUNT(*) F1 FROM BILL_MAST WHERE AQ_MONTH=? AND AQ_YEAR=? and type_of_bill=? AND (BILL_STATUS_ID > 4 AND BILL_STATUS_ID < 8) GROUP BY OFF_CODE)T5 "
                    + "ON G_OFFICE.OFF_CODE = T5.OFF_CODE GROUP BY DIST_CODE)T6 "
                    + "ON G_DISTRICT.DIST_CODE = T6.DIST_CODE "
                    + "LEFT OUTER JOIN "
                    + "(SELECT DIST_CODE,COUNT(*) TOTAL_DDO FROM G_OFFICE WHERE OFF_STATUS = 'F' AND IS_DDO='Y' GROUP BY DIST_CODE)T10 ON G_DISTRICT.DIST_CODE = T10.DIST_CODE ORDER BY DIST_NAME");
            
            ps1 = con.prepareStatement(stm);
            ps1.setInt(1, month);
            ps1.setInt(2, year);
            ps1.setString(3, typeOfBill);
            
            
            ps1.setInt(4, month);
            ps1.setInt(5, year);
            ps1.setString(6, typeOfBill);
            
           
            ps1.setInt(7, month);
            ps1.setInt(8, year);
            ps1.setString(9, typeOfBill);
            
            ps1.setInt(10, month);
            ps1.setInt(11, year);
            ps1.setString(12, typeOfBill);
            
            rs1 = ps1.executeQuery();
            while (rs1.next()) {

                BillStatus billstatus = new BillStatus();
                billstatus.setDepartmentname(rs1.getString("DIST_NAME"));
                billstatus.setDeptCode(rs1.getString("DIST_CODE"));
                billstatus.setTotalDDO(rs1.getInt("total_ddo"));
                billstatus.setDdoPrepared(rs1.getInt("ddo_prepared"));

                
                
                

                billstatus.setBillPrepared(rs1.getInt("bill_prepared"));
                
                billstatus.setDdoSubmitted(rs1.getInt("ddo_submitted"));
                billstatus.setBillSubmitted(rs1.getInt("bill_submitted"));
                billstatus.setTokenPrepared(rs1.getInt("token_prepared"));
                billstatus.setNoofError(rs1.getInt("BILL_ERROR"));
                
                outList.add(billstatus);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList DistWiseOfficePayBill(int month, int year, String dcode, String typeOfBill) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        List list = null;
        ArrayList outList = new ArrayList();
        ArrEmpBillGroup eList = null;
        List empList = null;
        String stm = null;
        try {
            con = this.repodataSource.getConnection();
            stm = ("SELECT G_OFFICE.OFF_CODE,OFF_EN,G_OFFICE.DDO_CODE,TOTAL_DDO,DDO_PREPARED ,BILL_PREPARED  ,DDO_SUBMITTED  ,BILL_SUBMITTED ,TOKEN_PREPARED \n"
                    + " FROM (SELECT OFF_CODE,OFF_EN,DDO_CODE FROM G_OFFICE WHERE DIST_CODE=? ORDER BY OFF_NAME)G_OFFICE\n"
                    + " LEFT OUTER JOIN (SELECT G_OFFICE.OFF_CODE,COUNT(*)DDO_SUBMITTED,SUM(F1)BILL_SUBMITTED FROM G_OFFICE\n"
                    + " INNER JOIN (SELECT OFF_CODE,COUNT(*) F1 FROM BILL_MAST WHERE AQ_MONTH=? AND AQ_YEAR=? and type_of_bill=? AND BILL_STATUS_ID >2 "
                    + " GROUP BY OFF_CODE)T1 ON G_OFFICE.OFF_CODE = T1.OFF_CODE GROUP BY G_OFFICE.OFF_CODE)T2 ON G_OFFICE.OFF_CODE = T2.OFF_CODE \n"
                    + " LEFT OUTER JOIN (SELECT G_OFFICE.OFF_CODE,COUNT(*)DDO_PREPARED,SUM(F1)BILL_PREPARED FROM G_OFFICE INNER JOIN \n"
                    + " (SELECT OFF_CODE,COUNT(*) F1 FROM BILL_MAST WHERE AQ_MONTH=? AND AQ_YEAR=? and type_of_bill=?  GROUP BY OFF_CODE)T3 ON G_OFFICE.OFF_CODE = T3.OFF_CODE \n"
                    + " GROUP BY G_OFFICE.OFF_CODE)T4 ON G_OFFICE.OFF_CODE = T4.OFF_CODE LEFT OUTER JOIN (SELECT G_OFFICE.OFF_CODE,COUNT(*)DDO_TOKEN,SUM(F1)TOKEN_PREPARED \n"
                    + " FROM G_OFFICE INNER JOIN (SELECT OFF_CODE,COUNT(*) F1 FROM BILL_MAST WHERE AQ_MONTH=? AND AQ_YEAR=? and type_of_bill=? \n"
                    + " AND (BILL_STATUS_ID > 4 AND BILL_STATUS_ID < 8) GROUP BY OFF_CODE)T5 ON G_OFFICE.OFF_CODE = T5.OFF_CODE \n"
                    + " GROUP BY G_OFFICE.OFF_CODE)T6 ON G_OFFICE.OFF_CODE = T6.OFF_CODE LEFT OUTER JOIN (SELECT OFF_CODE,COUNT(*) TOTAL_DDO \n"
                    + " FROM G_OFFICE WHERE OFF_STATUS = 'F' AND IS_DDO='Y' GROUP BY OFF_CODE)T10 ON G_OFFICE.OFF_CODE = T10.OFF_CODE WHERE T10.total_ddo >0 ORDER BY OFF_EN");
            ps1 = con.prepareStatement(stm);
            ps1.setString(1, dcode);
            
            ps1.setInt(2, month);
            ps1.setInt(3, year);
            ps1.setString(4, typeOfBill);
            
            
            ps1.setInt(5, month);
            ps1.setInt(6, year);
            ps1.setString(7, typeOfBill);
            
           
            ps1.setInt(8, month);
            ps1.setInt(9, year);
            ps1.setString(10, typeOfBill);
            rs1 = ps1.executeQuery();
            
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                BillStatus billstatus = new BillStatus();
                billstatus.setDepartmentname(rs1.getString("OFF_EN"));
                billstatus.setDdoCode(rs1.getString("DDO_CODE"));
                billstatus.setTotalDDO(rs1.getInt("total_ddo"));
                billstatus.setDdoPrepared(rs1.getInt("ddo_prepared"));
                billstatus.setBillPrepared(rs1.getInt("bill_prepared"));
                billstatus.setDdoSubmitted(rs1.getInt("ddo_submitted"));
                billstatus.setBillSubmitted(rs1.getInt("bill_submitted"));
                billstatus.setTokenPrepared(rs1.getInt("token_prepared"));
                billstatus.setDeptCode(CommonFunctions.encodedTxt(rs1.getString("OFF_CODE")));
                billstatus.setStrMonth(CommonFunctions.encodedTxt(month+""));
                billstatus.setStrYear(CommonFunctions.encodedTxt(year+""));
                outList.add(billstatus);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList getOfficeWiseBillStatus(int month, int year, String offcode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList bill_List = new ArrayList();
        try {
            con = this.repodataSource.getConnection();

            String sql = ("select bill_no,bill_desc,bill_group_desc,major_head,tr_code,token_no,token_date, vch_no, vch_date from "
                    + "bill_mast where off_code='" + offcode + "' and aq_month='" + month + "' and aq_year='" + year + "' and (bill_status_id=5 or bill_status_id=7) order by vch_date");
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                BillMastModel billmodel = new BillMastModel();
                //billmodel.setBillNo(rs.getInt("bill_no"));
                billmodel.setBillDesc(rs.getString("bill_desc"));
                billmodel.setBillGroupDesc(rs.getString("bill_group_desc"));
                billmodel.setMajorHead(rs.getString("major_head"));
                billmodel.setTrCode(rs.getString("tr_code"));
                billmodel.setTokenNo(rs.getString("token_no"));
                billmodel.setTkndate(CommonFunctions.getFormattedOutputDate1(rs.getDate("token_date")));
                billmodel.setVchNo(rs.getString("vch_no"));
                billmodel.setVdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("vch_date")));
                bill_List.add(billmodel);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return bill_List;
    }

    @Override
    public ArrayList DeptWiseOfficePayBill(int month, int year, String dcode) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        List list = null;
        ArrayList outList = new ArrayList();
        ArrEmpBillGroup eList = null;
        List empList = null;
        String stm = null;
        try {
            con = this.repodataSource.getConnection();
            stm = ("SELECT G_OFFICE.OFF_CODE,G_OFFICE.DDO_CODE,OFF_EN,TOTAL_DDO,DDO_PREPARED ,BILL_PREPARED  ,DDO_SUBMITTED  ,BILL_SUBMITTED ,TOKEN_PREPARED \n"
                    + " FROM (SELECT OFF_CODE,OFF_EN,DDO_CODE FROM G_OFFICE WHERE DEPARTMENT_CODE='" + dcode + "' ORDER BY OFF_NAME)G_OFFICE\n"
                    + " LEFT OUTER JOIN (SELECT G_OFFICE.OFF_CODE,COUNT(*)DDO_SUBMITTED,SUM(F1)BILL_SUBMITTED FROM G_OFFICE\n"
                    + " INNER JOIN (SELECT OFF_CODE,COUNT(*) F1 FROM BILL_MAST WHERE AQ_MONTH='" + month + "' AND AQ_YEAR='" + year + "' AND BILL_STATUS_ID>2 \n"
                    + "GROUP BY OFF_CODE)T1 ON G_OFFICE.OFF_CODE = T1.OFF_CODE GROUP BY G_OFFICE.OFF_CODE)T2 ON G_OFFICE.OFF_CODE = T2.OFF_CODE \n"
                    + "LEFT OUTER JOIN (SELECT G_OFFICE.OFF_CODE,COUNT(*)DDO_PREPARED,SUM(F1)BILL_PREPARED FROM G_OFFICE INNER JOIN \n"
                    + "(SELECT OFF_CODE,COUNT(*) F1 FROM BILL_MAST WHERE AQ_MONTH='" + month + "' AND AQ_YEAR='" + year + "'  GROUP BY OFF_CODE)T3 ON G_OFFICE.OFF_CODE = T3.OFF_CODE \n"
                    + "GROUP BY G_OFFICE.OFF_CODE)T4 ON G_OFFICE.OFF_CODE = T4.OFF_CODE LEFT OUTER JOIN (SELECT G_OFFICE.OFF_CODE,COUNT(*)DDO_TOKEN,SUM(F1)TOKEN_PREPARED \n"
                    + "FROM G_OFFICE INNER JOIN (SELECT OFF_CODE,COUNT(*) F1 FROM BILL_MAST WHERE AQ_MONTH='" + month + "' AND AQ_YEAR='" + year + "' \n"
                    + "AND (BILL_STATUS_ID > 4 AND BILL_STATUS_ID < 8) GROUP BY OFF_CODE)T5 ON G_OFFICE.OFF_CODE = T5.OFF_CODE \n"
                    + "GROUP BY G_OFFICE.OFF_CODE)T6 ON G_OFFICE.OFF_CODE = T6.OFF_CODE LEFT OUTER JOIN (SELECT OFF_CODE,COUNT(*) TOTAL_DDO \n"
                    + "FROM G_OFFICE WHERE OFF_STATUS = 'F' AND IS_DDO='Y' GROUP BY OFF_CODE)T10 ON G_OFFICE.OFF_CODE = T10.OFF_CODE WHERE T10.total_ddo >0 ORDER BY OFF_EN");
            ps1 = con.prepareStatement(stm);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                BillStatus billstatus = new BillStatus();
                billstatus.setDdoCode(rs1.getString("DDO_CODE"));
                billstatus.setDepartmentname(rs1.getString("OFF_EN"));
                billstatus.setTotalDDO(rs1.getInt("total_ddo"));
                billstatus.setDdoPrepared(rs1.getInt("ddo_prepared"));
                billstatus.setBillPrepared(rs1.getInt("bill_prepared"));
                billstatus.setDdoSubmitted(rs1.getInt("ddo_submitted"));
                billstatus.setBillSubmitted(rs1.getInt("bill_submitted"));
                billstatus.setTokenPrepared(rs1.getInt("token_prepared"));
                billstatus.setDeptCode(CommonFunctions.encodedTxt(rs1.getString("OFF_CODE")));
                billstatus.setStrMonth(CommonFunctions.encodedTxt(month+""));
                billstatus.setStrYear(CommonFunctions.encodedTxt(year+""));
                outList.add(billstatus);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList getBillStatusCount() {
        int total = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList li = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select (SELECT COALESCE(COUNT(*),0) AS total_rows FROM paybill_task) as a"
                    + ", (select COALESCE(COUNT(*),0) AS total_rows1 from paybill_task WHERE (now()::date - process_date::date) <= 3) as b"
                    + ", (select COALESCE(COUNT(*),0) AS total_rows2 from paybill_task WHERE (now()::date - process_date::date) > 3 AND (now()::date - process_date::date) <= 30) AS c"
                    + ", (select COALESCE(COUNT(*),0) AS total_rows3 from paybill_task WHERE (now()::date - process_date::date) > 30) AS d");
            rs = ps.executeQuery();
            while (rs.next()) {
                li.add(rs.getInt("a"));
                li.add(rs.getInt("b"));
                li.add(rs.getInt("c"));
                li.add(rs.getInt("d"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public ArrayList getTreasuryBillStatusCount() {
        int total = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList li = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select (select COALESCE(COUNT(distinct bill_no),0) AS total_rows from bill_mast BM INNER JOIN bill_status_history BH ON BM.bill_no = BH.bill_id WHERE bill_status_id = 3) as a"
                    + ", (select COALESCE(COUNT(distinct bill_no),0) AS total_rows1 from bill_mast BM INNER JOIN bill_status_history BH ON BM.bill_no = BH.bill_id WHERE (now()::date - history_date::date) <= 3 AND bill_status_id = 3) as b"
                    + ", (select COALESCE(COUNT(distinct bill_no),0) AS total_rows2 from bill_mast BM INNER JOIN bill_status_history BH ON BM.bill_no = BH.bill_id WHERE (now()::date - history_date::date) > 3 AND (now()::date - history_date::date) <= 30 AND bill_status_id = 3) AS c"
                    + ", (select COALESCE(COUNT(distinct bill_no),0) AS total_rows3 from bill_mast BM INNER JOIN bill_status_history BH ON BM.bill_no = BH.bill_id WHERE (now()::date - history_date::date) > 30 AND bill_status_id = 3) AS d");
            rs = ps.executeQuery();
            while (rs.next()) {
                li.add(rs.getInt("a"));
                li.add(rs.getInt("b"));
                li.add(rs.getInt("c"));
                li.add(rs.getInt("d"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public ArrayList getBillStatusDetail(String type) {
        ArrayList li = new ArrayList();
        Connection con = null;
        PreparedStatement ps1 = null;
        String stm = null;
        ResultSet rs = null;
        String sql = null;
        try {
            con = this.repodataSource.getConnection();
            if (type.equals("1")) {
                sql = "select BM.bill_no,BM.ddo_code,BM.bill_desc,BM.bill_group_desc,BM.major_head,BM.tr_code,BM.token_no,process_date, BM.vch_no, BM.vch_date, BM.aq_month, BM.aq_year, (now()::date - process_date::date) AS date_diff from paybill_task PT"
                        + " INNER JOIN bill_mast BM ON PT.bill_id = BM.bill_no ORDER BY (now()::date - process_date::date) DESC";
            }
            if (type.equals("2")) {
                sql = "select BM.bill_no,BM.ddo_code,BM.bill_desc,BM.bill_group_desc,BM.major_head,BM.tr_code,BM.token_no,process_date, BM.vch_no, BM.vch_date, BM.aq_month, BM.aq_year, (now()::date - process_date::date) AS date_diff from paybill_task PT"
                        + " INNER JOIN bill_mast BM ON PT.bill_id = BM.bill_no WHERE (now()::date - process_date::date) <= 3 ORDER BY (now()::date - process_date::date) DESC";
            }
            if (type.equals("3")) {
                sql = "select BM.bill_no,BM.ddo_code,BM.bill_desc,BM.bill_group_desc,BM.major_head,BM.tr_code,BM.token_no,process_date, BM.vch_no, BM.vch_date, BM.aq_month, BM.aq_year, (now()::date - process_date::date) AS date_diff from paybill_task PT"
                        + " INNER JOIN bill_mast BM ON PT.bill_id = BM.bill_no WHERE ((now()::date - process_date::date) > 3 AND (now()::date - process_date::date) <= 30) ORDER BY (now()::date - process_date::date) DESC";
            }
            if (type.equals("4")) {
                sql = "select BM.bill_no,BM.ddo_code,BM.bill_desc,BM.bill_group_desc,BM.major_head,BM.tr_code,BM.token_no,process_date, BM.vch_no, BM.vch_date, BM.aq_month, BM.aq_year, (now()::date - process_date::date) AS date_diff from paybill_task PT"
                        + " INNER JOIN bill_mast BM ON PT.bill_id = BM.bill_no WHERE (now()::date - process_date::date) > 30 ORDER BY (now()::date - process_date::date) DESC";
            }
            stm = (sql);
            ps1 = con.prepareStatement(stm);
            rs = ps1.executeQuery();
            while (rs.next()) {
                BillMastModel billmodel = new BillMastModel();
                billmodel.setBillNo(rs.getInt("bill_no"));
                billmodel.setBillDesc(rs.getString("bill_desc"));
                billmodel.setBillGroupDesc(rs.getString("bill_group_desc"));
                billmodel.setMajorHead(rs.getString("major_head"));
                billmodel.setTrCode(rs.getString("tr_code"));
                billmodel.setTokenNo(rs.getString("token_no"));
                billmodel.setTkndate(CommonFunctions.getFormattedOutputDate1(rs.getDate("process_date")));
                billmodel.setVchNo(rs.getInt("date_diff") + "");
                billmodel.setVdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("vch_date")));
                billmodel.setDdoCode(rs.getString("ddo_code"));
                li.add(billmodel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public ArrayList BillTreasuryDashboardDetail(String type) {
        ArrayList li = new ArrayList();
        Connection con = null;
        PreparedStatement ps1 = null;
        String stm = null;
        ResultSet rs = null;
        String sql = null;
        try {
            con = this.repodataSource.getConnection();
            if (type.equals("1")) {
                sql = "select BM.bill_no,BM.ddo_code,BM.bill_desc,BM.bill_group_desc,BM.major_head,BM.tr_code,BM.token_no,BH.history_date, BM.vch_no, BM.vch_date, BM.aq_month, BM.aq_year, (now()::date - history_date::date) AS date_diff from bill_mast BM \n"
                        + "INNER JOIN (select distinct bill_no, max(history_date) as history_date from bill_mast BM \n"
                        + "INNER JOIN bill_status_history BH ON BM.bill_no = BH.bill_id WHERE bill_status_id = 3 AND status_id = 3 group by bill_no) BH ON BM.bill_no = BH.bill_no ORDER BY (now()::date - history_date::date) DESC";
            }
            if (type.equals("2")) {
                sql = "select BM.bill_no,BM.ddo_code,BM.bill_desc,BM.bill_group_desc,BM.major_head,BM.tr_code,BM.token_no,BH.history_date, BM.vch_no, BM.vch_date, BM.aq_month, BM.aq_year, (now()::date - history_date::date) AS date_diff from bill_mast BM \n"
                        + "INNER JOIN (select distinct bill_no, max(history_date) as history_date from bill_mast BM \n"
                        + "INNER JOIN bill_status_history BH ON BM.bill_no = BH.bill_id WHERE bill_status_id = 3 AND status_id = 3 group by bill_no) BH ON BM.bill_no = BH.bill_no WHERE (now()::date - history_date::date) <= 3 ORDER BY (now()::date - history_date::date) DESC";                
            }
            if (type.equals("3")) {
                sql = "select BM.bill_no,BM.ddo_code,BM.bill_desc,BM.bill_group_desc,BM.major_head,BM.tr_code,BM.token_no,BH.history_date, BM.vch_no, BM.vch_date, BM.aq_month, BM.aq_year, (now()::date - history_date::date) AS date_diff from bill_mast BM \n"
                        + "INNER JOIN (select distinct bill_no, max(history_date) as history_date from bill_mast BM \n"
                        + "INNER JOIN bill_status_history BH ON BM.bill_no = BH.bill_id WHERE bill_status_id = 3 AND status_id = 3 group by bill_no) BH ON BM.bill_no = BH.bill_no WHERE ((now()::date - history_date::date) > 3 AND (now()::date - history_date::date) <= 30) ORDER BY (now()::date - history_date::date) DESC";                
            }
            if (type.equals("4")) {
                sql = "select BM.bill_no,BM.ddo_code,BM.bill_desc,BM.bill_group_desc,BM.major_head,BM.tr_code,BM.token_no,BH.history_date, BM.vch_no, BM.vch_date, BM.aq_month, BM.aq_year, (now()::date - history_date::date) AS date_diff from bill_mast BM \n"
                        + "INNER JOIN (select distinct bill_no, max(history_date) as history_date from bill_mast BM \n"
                        + "INNER JOIN bill_status_history BH ON BM.bill_no = BH.bill_id WHERE bill_status_id = 3 AND status_id = 3 group by bill_no) BH ON BM.bill_no = BH.bill_no WHERE (now()::date - history_date::date) > 30 ORDER BY (now()::date - history_date::date) DESC";
            }
            stm = (sql);
            ps1 = con.prepareStatement(stm);
            rs = ps1.executeQuery();
            while (rs.next()) {
                BillMastModel billmodel = new BillMastModel();
                billmodel.setBillNo(rs.getInt("bill_no"));
                billmodel.setBillDesc(rs.getString("bill_desc"));
                billmodel.setBillGroupDesc(rs.getString("bill_group_desc"));
                billmodel.setMajorHead(rs.getString("major_head"));
                billmodel.setTrCode(rs.getString("tr_code"));
                billmodel.setTokenNo(rs.getString("token_no"));
                billmodel.setTkndate(CommonFunctions.getFormattedOutputDate1(rs.getDate("history_date")));
                billmodel.setVchNo(rs.getInt("date_diff") + "");
                billmodel.setVdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("vch_date")));
                billmodel.setDdoCode(rs.getString("ddo_code"));
                li.add(billmodel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }
    @Override
    public ArrayList DeptWiseVoucherList(int month, int year, String billType) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        List list = null;
        ArrayList outList = new ArrayList();
        ArrEmpBillGroup eList = null;
        List empList = null;
        String stm = null;
        try {
            con = this.repodataSource.getConnection();
            stm = ("SELECT G_DEPARTMENT.DEPARTMENT_CODE,DEPARTMENT_NAME ,TOTAL_DDO,VOUCHER_PREPARED  FROM \n"
                    + "(SELECT DEPARTMENT_CODE,DEPARTMENT_NAME FROM G_DEPARTMENT WHERE IF_ACTIVE='Y' ORDER BY DEPARTMENT_NAME)G_DEPARTMENT LEFT OUTER JOIN  \n"
                    + "(SELECT DEPARTMENT_CODE,COUNT(*)DDO_TOKEN,SUM(F1)VOUCHER_PREPARED FROM G_OFFICE INNER JOIN \n"
                    + "(SELECT OFF_CODE,COUNT(*) F1 FROM BILL_MAST WHERE extract(month from vch_date)=? AND extract(year from vch_date)=? AND BILL_STATUS_ID=7 AND TYPE_OF_BILL=? and length(VCH_NO) > 4 GROUP BY OFF_CODE)T5 \n"
                    + "ON G_OFFICE.OFF_CODE = T5.OFF_CODE GROUP BY DEPARTMENT_CODE)T6 \n"
                    + "ON G_DEPARTMENT.DEPARTMENT_CODE = T6.DEPARTMENT_CODE\n"
                    + "LEFT OUTER JOIN\n"
                    + "(SELECT DEPARTMENT_CODE,COUNT(*) TOTAL_DDO FROM G_OFFICE WHERE OFF_STATUS ='F' AND IS_DDO='Y' GROUP BY DEPARTMENT_CODE)T10 ON G_DEPARTMENT.DEPARTMENT_CODE = T10.DEPARTMENT_CODE");
            ps1 = con.prepareStatement(stm);
            ps1.setInt(1,(month+1));
            ps1.setInt(2,year);
            ps1.setString(3,billType);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                BillStatus billstatus = new BillStatus();
                billstatus.setDepartmentname(rs1.getString("department_name"));
                billstatus.setTotalDDO(rs1.getInt("total_ddo"));
                billstatus.setVoucherPrepared(rs1.getInt("VOUCHER_PREPARED"));                
                billstatus.setDeptCode(rs1.getString("DEPARTMENT_CODE"));
                outList.add(billstatus);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }
}
