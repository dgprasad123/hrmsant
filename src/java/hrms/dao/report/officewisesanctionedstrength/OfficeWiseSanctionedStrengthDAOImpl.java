package hrms.dao.report.officewisesanctionedstrength;

import hrms.common.DataBaseFunctions;
import hrms.model.payroll.billbrowser.BillGroup;
import hrms.model.report.officewisesanctionedstrength.OfficeWiseSanctionedStrengthBean;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class OfficeWiseSanctionedStrengthDAOImpl implements OfficeWiseSanctionedStrengthDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getSanctionedStrengthList(String offCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List datalist = new ArrayList();

        OfficeWiseSanctionedStrengthBean bean = null;
        int total = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select aer_id,fy,sancnosgrp_a,sancnosgrp_b,sancnosgrp_c,sancnosgrp_d,grant_in_aid, status from aer_report_submit where off_code=? order by fy desc";
            pst = con.prepareStatement(sql);
            pst.setString(1, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                bean = new OfficeWiseSanctionedStrengthBean();
                bean.setAerId(rs.getString("aer_id"));
                bean.setFinancialYear(rs.getString("fy"));
                bean.setGroupAData(rs.getString("sancnosgrp_a"));
                bean.setGroupBData(rs.getString("sancnosgrp_b"));
                bean.setGroupCData(rs.getString("sancnosgrp_c"));
                bean.setGroupDData(rs.getString("sancnosgrp_d"));
                bean.setGrantInAid(rs.getString("grant_in_aid"));
                bean.setStatus(rs.getString("status"));
                total = rs.getInt("sancnosgrp_a") + rs.getInt("sancnosgrp_b") + rs.getInt("sancnosgrp_c") + rs.getInt("sancnosgrp_d") + rs.getInt("grant_in_aid");
                bean.setTotal(total);
                datalist.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return datalist;
    }

    @Override
    public void saveSanctionedPostData(String offCode, OfficeWiseSanctionedStrengthBean bean) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;

        boolean found = false;
        try {
            con = this.dataSource.getConnection();

            if (bean.getAerId() != null && !bean.getAerId().equals("")) {
                String sql = "UPDATE aer_report_submit SET fy=?,sancnosgrp_a=?,sancnosgrp_b=?,sancnosgrp_c=?,sancnosgrp_d=?,grant_in_aid=? WHERE aer_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, bean.getFinancialYear());
                if (bean.getGroupAData() != null && !bean.getGroupAData().equals("")) {
                    pst.setInt(2, Integer.parseInt(bean.getGroupAData()));
                } else {
                    pst.setInt(2, 0);
                }
                if (bean.getGroupBData() != null && !bean.getGroupBData().equals("")) {
                    pst.setInt(3, Integer.parseInt(bean.getGroupBData()));
                } else {
                    pst.setInt(3, 0);
                }
                if (bean.getGroupCData() != null && !bean.getGroupCData().equals("")) {
                    pst.setInt(4, Integer.parseInt(bean.getGroupCData()));
                } else {
                    pst.setInt(4, 0);
                }
                if (bean.getGroupDData() != null && !bean.getGroupDData().equals("")) {
                    pst.setInt(5, Integer.parseInt(bean.getGroupDData()));
                } else {
                    pst.setInt(5, 0);
                }
                if (bean.getGrantInAid() != null && !bean.getGrantInAid().equals("")) {
                    pst.setInt(6, Integer.parseInt(bean.getGrantInAid()));
                } else {
                    pst.setInt(6, 0);
                }
                pst.setInt(7, Integer.parseInt(bean.getAerId()));
                pst.executeUpdate();

                pst2 = con.prepareStatement("DELETE FROM aer2_co_mapping WHERE aer_id=?");
                pst2.setInt(1, Integer.parseInt(bean.getAerId()));
                pst2.execute();

                pst2 = con.prepareStatement("INSERT INTO aer2_co_mapping (bill_group_id, aer_id, off_code, fy) VALUES (?,?,?,?) ");

                boolean dataExist = false;
                for (int i = 0; i < bean.getBillGrpId().length; i++) {
                    /*
                     psRet.setString(1, offCode);
                     psRet.setBigDecimal(2, bean.getBillGrpId()[i]);
                     psRet.setString(3, bean.getFinancialYear());
                     rs2 = psRet.executeQuery();
                     if (rs2.next()) {
                     dataExist = true;
                     }
                     */
                    //if (dataExist == false) {
                    pst2.setBigDecimal(1, bean.getBillGrpId()[i]);
                    pst2.setInt(2, Integer.parseInt(bean.getAerId()));
                    pst2.setString(3, offCode);
                    pst2.setString(4, bean.getFinancialYear());
                    pst2.executeUpdate();
                    //}
                }

            } else {

                String sql = "INSERT INTO aer_report_submit(off_code,controlling_spc,file_name,status,fy,sancnosgrp_a,sancnosgrp_b ,sancnosgrp_c ,sancnosgrp_d,grant_in_aid) values(?,?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, offCode);
                pst.setString(2, "");
                pst.setString(3, "");
                pst.setString(4, "");
                pst.setString(5, bean.getFinancialYear());
                if (bean.getGroupAData() != null && !bean.getGroupAData().equals("")) {
                    pst.setInt(6, Integer.parseInt(bean.getGroupAData()));
                } else {
                    pst.setInt(6, 0);
                }
                if (bean.getGroupBData() != null && !bean.getGroupBData().equals("")) {
                    pst.setInt(7, Integer.parseInt(bean.getGroupBData()));
                } else {
                    pst.setInt(7, 0);
                }
                if (bean.getGroupCData() != null && !bean.getGroupCData().equals("")) {
                    pst.setInt(8, Integer.parseInt(bean.getGroupCData()));
                } else {
                    pst.setInt(8, 0);
                }
                if (bean.getGroupDData() != null && !bean.getGroupDData().equals("")) {
                    pst.setInt(9, Integer.parseInt(bean.getGroupDData()));
                } else {
                    pst.setInt(9, 0);
                }
                if (bean.getGrantInAid() != null && !bean.getGrantInAid().equals("")) {
                    pst.setInt(10, Integer.parseInt(bean.getGrantInAid()));
                } else {
                    pst.setInt(10, 0);
                }
                pst.execute();
                ResultSet insrs = pst.getGeneratedKeys();
                int aerId = 0;
                if (insrs.next()) {
                    aerId = insrs.getInt(1);
                }

                PreparedStatement psRet = con.prepareStatement("SELECT off_code, fy, bill_group_id from  aer2_co_mapping where off_code=? and bill_group_id=? and fy=?");

                pst2 = con.prepareStatement("INSERT INTO aer2_co_mapping (bill_group_id, aer_id, off_code, fy) VALUES (?,?,?,?) ");

                boolean dataExist = false;
                for (int i = 0; i < bean.getBillGrpId().length; i++) {
                    /*
                     psRet.setString(1, offCode);
                     psRet.setBigDecimal(2, bean.getBillGrpId()[i]);
                     psRet.setString(3, bean.getFinancialYear());
                     rs2 = psRet.executeQuery();
                     if (rs2.next()) {
                     dataExist = true;
                     }
                     */
                    //if (dataExist == false) {
                    pst2.setBigDecimal(1, bean.getBillGrpId()[i]);
                    pst2.setInt(2, aerId);
                    pst2.setString(3, offCode);
                    pst2.setString(4, bean.getFinancialYear());
                    pst2.executeUpdate();
                    //}
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String verifyDuplicate(String offCode, String financialYear) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isDuplicate = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT * FROM aer_report_submit WHERE OFF_CODE=? AND FY=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offCode);
            pst.setString(2, financialYear);
            rs = pst.executeQuery();
            if (rs.next()) {
                isDuplicate = "Y";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isDuplicate;
    }

    @Override
    public List getBillgroupListNotInAddNewCase(String ddocode, String fyYear) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        List billGroupList = new ArrayList();

        String subofficecode = "";
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT BILL_GROUP_MASTER.bill_group_id , aer2_co_mapping.bill_group_id,description,demand_no,major_head,major_head_desc,sub_major_head, "
                    + " sub_major_head_desc, minor_head,minor_head_desc, sub_minor_head1, sub_minor_head1_desc, sub_minor_head2, sub_minor_head2_desc, sub_minor_head3 "
                    + " FROM BILL_GROUP_MASTER "
                    + " left outer join (SELECT * FROM aer2_co_mapping WHERE fy=? AND OFF_CODE=?) aer2_co_mapping ON "
                    + " BILL_GROUP_MASTER.bill_group_id = aer2_co_mapping.bill_group_id "
                    + " WHERE BILL_GROUP_MASTER.OFF_CODE=?  and (is_deleted ='N' or is_deleted  is null) "
                    + " AND  aer2_co_mapping.bill_group_id IS NULL");

            String sql = "select * from g_office where ddo_code=? and online_bill_submission='Y'";
            pst1 = con.prepareStatement(sql);
            pst1.setString(1, ddocode);
            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                subofficecode = rs1.getString("off_code");

                /*
                 pstmt = con.prepareStatement("SELECT BILL_GROUP_MASTER.bill_group_id , aer2_co_mapping.bill_group_id,description,demand_no,major_head,major_head_desc,sub_major_head,"
                 + " sub_major_head_desc, minor_head,minor_head_desc, sub_minor_head1, sub_minor_head1_desc, sub_minor_head2, sub_minor_head2_desc, sub_minor_head3 "
                 + " FROM BILL_GROUP_MASTER left outer join (SELECT * FROM aer2_co_mapping WHERE fy=? AND OFF_CODE=?)aer2_co_mapping ON "
                 + " BILL_GROUP_MASTER.bill_group_id = aer2_co_mapping.bill_group_id WHERE BILL_GROUP_MASTER.OFF_CODE=? AND  aer2_co_mapping.bill_group_id IS NULL");
                 */
                pstmt.setString(1, fyYear);
                pstmt.setString(2, subofficecode);
                pstmt.setString(3, subofficecode);

                rs = pstmt.executeQuery();
                int i = 0;
                while (rs.next()) {
                    i++;
                    BillGroup billGroup = new BillGroup();
                    billGroup.setSlno(i);
                    billGroup.setBillgroupid(rs.getBigDecimal("bill_group_id"));
                    billGroup.setBillgroupdesc(rs.getString("description"));
                    billGroup.setDemandNo(rs.getString("demand_no"));
                    billGroup.setMajorHead(rs.getString("major_head"));
                    billGroup.setMajorHeadDesc(rs.getString("major_head_desc"));
                    billGroup.setSubMajorHead(rs.getString("sub_major_head"));
                    billGroup.setSubMajorHeadDesc(rs.getString("sub_major_head_desc"));
                    billGroup.setMinorHead(rs.getString("minor_head"));
                    billGroup.setMinorHeadDesc(rs.getString("minor_head_desc"));
                    billGroup.setSubMinorHead1(rs.getString("sub_minor_head1"));
                    billGroup.setSubMinorHeadDesc1(rs.getString("sub_minor_head1_desc"));
                    billGroup.setSubMinorHead2(rs.getString("sub_minor_head2"));
                    billGroup.setSubMinorHeadDesc2(rs.getString("sub_minor_head2_desc"));
                    billGroup.setSubMinorHead3(rs.getString("sub_minor_head3"));
                    billGroup.setBilltype(subofficecode);
                    billGroupList.add(billGroup);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billGroupList;
    }

    @Override
    public List getBillgroupListNotInEditNewCase(String ddocode, String fyYear, String aerid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        List billGroupList = new ArrayList();

        String subofficecode = "";
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM (SELECT T1.bill_group_id,description,demand_no,major_head,major_head_desc, sub_major_head, "
                    + " sub_major_head_desc, minor_head, minor_head_desc, sub_minor_head1, sub_minor_head1_desc, sub_minor_head2, sub_minor_head2_desc, "
                    + " sub_minor_head3 FROM "
                    + " (SELECT * FROM BILL_GROUP_MASTER WHERE off_code=? and (is_deleted ='N' or is_deleted  is null))T1 "
                    + " ) T2 WHERE "
                    + " bill_group_id not in (select bill_group_id from aer_report_submit aer "
                    + " left outer join aer2_co_mapping co on aer.aer_id=co.aer_id where aer.off_code=? and aer.fy=? and co.aer_id<>?)");

            /*pstmt = con.prepareStatement("SELECT * FROM BILL_GROUP_MASTER WHERE OFF_CODE=?  and (is_deleted ='N' or is_deleted  is null) "
             + " and bill_group_id not in (select bill_group_id from aer_report_submit aer "
             + " left outer join aer2_co_mapping co on aer.aer_id=co.aer_id where aer.off_code=? and aer.fy=? and co.aer_id<>?)");*/
            String sql = "select * from g_office where ddo_code=? and online_bill_submission='Y'";
            pst1 = con.prepareStatement(sql);
            pst1.setString(1, ddocode);
            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                subofficecode = rs1.getString("off_code");

                pstmt.setString(1, subofficecode);
                pstmt.setString(2, subofficecode);
                pstmt.setString(3, fyYear);
                pstmt.setInt(4, Integer.parseInt(aerid));
                rs = pstmt.executeQuery();
                int i = 0;
                while (rs.next()) {
                    i++;
                    BillGroup billGroup = new BillGroup();
                    billGroup.setSlno(i);
                    billGroup.setBillgroupid(rs.getBigDecimal("bill_group_id"));
                    billGroup.setBillgroupdesc(rs.getString("description"));
                    billGroup.setDemandNo(rs.getString("demand_no"));
                    billGroup.setMajorHead(rs.getString("major_head"));
                    billGroup.setMajorHeadDesc(rs.getString("major_head_desc"));
                    billGroup.setSubMajorHead(rs.getString("sub_major_head"));
                    billGroup.setSubMajorHeadDesc(rs.getString("sub_major_head_desc"));
                    billGroup.setMinorHead(rs.getString("minor_head"));
                    billGroup.setMinorHeadDesc(rs.getString("minor_head_desc"));
                    billGroup.setSubMinorHead1(rs.getString("sub_minor_head1"));
                    billGroup.setSubMinorHeadDesc1(rs.getString("sub_minor_head1_desc"));
                    billGroup.setSubMinorHead2(rs.getString("sub_minor_head2"));
                    billGroup.setSubMinorHeadDesc2(rs.getString("sub_minor_head2_desc"));
                    billGroup.setSubMinorHead3(rs.getString("sub_minor_head3"));
                    billGroupList.add(billGroup);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billGroupList;
    }

    @Override
    public List<BigDecimal> getSelectedBillGroup(String aerId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BigDecimal> billGroupList = new ArrayList<BigDecimal>();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select bill_group_id from aer_report_submit aer "
                    + " left outer join aer2_co_mapping co on aer.aer_id=co.aer_id where  aer.aer_id=?");

            pstmt.setInt(1, Integer.parseInt(aerId));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                billGroupList.add(rs.getBigDecimal("bill_group_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return billGroupList;
    }

    @Override
    public OfficeWiseSanctionedStrengthBean getSanctionedStrengthData(OfficeWiseSanctionedStrengthBean bean) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select aer_id,fy,sancnosgrp_a,sancnosgrp_b,sancnosgrp_c,sancnosgrp_d,grant_in_aid from aer_report_submit where aer_id=?";

            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(bean.getAerId()));
            rs = pst.executeQuery();
            if (rs.next()) {
                bean.setFinancialYear(rs.getString("fy"));
                bean.setGroupAData(rs.getString("sancnosgrp_a"));
                bean.setGroupBData(rs.getString("sancnosgrp_b"));
                bean.setGroupCData(rs.getString("sancnosgrp_c"));
                bean.setGroupDData(rs.getString("sancnosgrp_d"));
                bean.setGrantInAid(rs.getString("grant_in_aid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return bean;
    }

    @Override
    public HashMap<String, String> getFinancialYearList() {

        Connection con = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        HashMap<String, String> list = new HashMap<String, String>();
        try {
            con = dataSource.getConnection();

            String sql = "SELECT fy FROM financial_year WHERE active='Y' ORDER BY fy desc";

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();
            while (rs.next()) {
                list.put(rs.getString("fy"), rs.getString("fy"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;

    }

}
