/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.billbrowser;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.master.ChartOfAccount;
import hrms.model.payroll.billbrowser.BillGroup;
import hrms.model.payroll.billbrowser.BillGroupConfig;
import hrms.model.payroll.billbrowser.SectionDefinition;
import hrms.model.payroll.billbrowser.SectionDtlSPCWiseEmp;
import hrms.model.payroll.grouppayfixation.GroupPayFixation;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Manas Jena
 */
public class BillGroupDAOImpl implements BillGroupDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getBillGroupList(String offcode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList billGroupList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM BILL_GROUP_MASTER WHERE OFF_CODE=?  and (is_deleted ='N' or is_deleted  is null) order by description");
            pstmt.setString(1, offcode);
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
                billGroup.setPayhead(rs.getString("pay_head"));
                billGroup.setPlan(rs.getString("plan"));
                billGroup.setSector(rs.getString("sector"));
                billGroup.setShowInAquitance(rs.getString("show_in_aquitance"));
                billGroup.setSubmitToTreasury(rs.getString("submit_to_treasury"));
                billGroupList.add(billGroup);
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
    public ArrayList getActiveBillGroupList(String offcode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList billGroupList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM BILL_GROUP_MASTER WHERE OFF_CODE=? AND (IS_DELETED IS  NULL OR IS_DELETED='N') ORDER BY DESCRIPTION");
            pstmt.setString(1, offcode);
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billGroupList;
    }

    @Override
    public void saveGroupSection(String offCode, BillGroup bg) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            String maxCode = CommonFunctions.getMaxCode("bill_group_master", "bill_group_id", con);
            pst = con.prepareStatement("INSERT INTO bill_group_master(bill_group_id,off_code,description,demand_no,major_head,sub_major_head,minor_head,sub_minor_head1,sub_minor_head2,sub_minor_head3,post_class,is_deleted,plan,sector,bill_type,pay_head) VALUES(?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setBigDecimal(1, new BigDecimal(maxCode));
            //pst.setInt(1, Integer.parseInt(maxCode));
            pst.setString(2, offCode);

            pst.setString(3, bg.getBillgroupdesc());
            pst.setString(4, bg.getDemandNo());
            pst.setString(5, bg.getMajorHead());
            pst.setString(6, bg.getSubMajorHead());

            pst.setString(7, bg.getMinorHead());
            pst.setString(8, bg.getSubMinorHead1());
            pst.setString(9, bg.getSubMinorHead2());
            pst.setString(10, bg.getSubMinorHead3());
            pst.setString(11, bg.getPostclass());
            pst.setString(12, "N");
            pst.setString(13, bg.getPlan());
            pst.setString(14, bg.getSector());
            pst.setString(15, bg.getBilltype());
            pst.setString(16, bg.getPayhead());

            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public int deleteGroupData(String ocode, String groupId) {
        int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM bill_group_master WHERE bill_group_id =" + groupId + " AND off_code ='" + ocode + "'");

            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public BillGroup getBillGroupDetail(String offCode, BigDecimal groupId) {
        BillGroup billGroup = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select * from bill_group_master where bill_group_id =? AND off_code =?");
            pst.setBigDecimal(1, groupId);
            pst.setString(2, offCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                billGroup = new BillGroup();
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
                billGroup.setPlan(rs.getString("plan"));
                billGroup.setSector(rs.getString("sector"));
                billGroup.setBillgroupid(rs.getBigDecimal("bill_group_id"));
                billGroup.setBilltype(rs.getString("bill_type"));
                billGroup.setPostclass(rs.getString("post_class"));
                billGroup.setConfiglvl(rs.getString("CONFIGURED_LVL"));
                billGroup.setPayhead(rs.getString("pay_head"));
                billGroup.setSltCOList(rs.getString("co_code"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billGroup;
    }

    @Override
    public void updateGroupSection(String offCode, BillGroup bg) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE bill_group_master SET description=?,demand_no=?,major_head=?,sub_major_head=?,minor_head=?,sub_minor_head1=?,sub_minor_head2=?,sub_minor_head3=?,post_class=?,is_deleted=?,plan=?,sector=?,bill_type=?,pay_head=?,co_code=? WHERE bill_group_id=? AND off_code=?");
            pst.setString(1, bg.getBillgroupdesc());
            pst.setString(2, bg.getDemandNo());
            pst.setString(3, bg.getMajorHead());
            pst.setString(4, bg.getSubMajorHead());
            pst.setString(5, bg.getMinorHead());
            pst.setString(6, bg.getSubMinorHead1());
            pst.setString(7, bg.getSubMinorHead2());
            pst.setString(8, bg.getSubMinorHead3());
            pst.setString(9, bg.getPostclass());
            pst.setString(10, "N");
            pst.setString(11, bg.getPlan());
            pst.setString(12, bg.getSector());
            pst.setString(13, bg.getBilltype());
            pst.setString(14, bg.getPayhead());
            pst.setString(15, bg.getSltCOList());
            pst.setBigDecimal(16, bg.getBillgroupid());
            pst.setString(17, offCode);

            n = pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public BillGroup getBillGroupDetails(BigDecimal billGroupId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BillGroup billGroup = new BillGroup();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM BILL_GROUP_MASTER WHERE BILL_GROUP_ID=?");
            pstmt.setBigDecimal(1, billGroupId);
            rs = pstmt.executeQuery();
            int i = 0;
            if (rs.next()) {
                billGroup.setOffcode(rs.getString("OFF_CODE"));
                billGroup.setPlan(rs.getString("PLAN"));
                billGroup.setSector(rs.getString("SECTOR"));
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return billGroup;
    }

    @Override
    public ArrayList getPlanStatusList() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList planStatusList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM g_plan_status");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("description"));
                so.setLabel(rs.getString("plan_descpn"));
                planStatusList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return planStatusList;
    }

    @Override
    public ArrayList getSectorList() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList billSectorList = new ArrayList();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM g_sector");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("sector_code"));
                so.setLabel(rs.getString("sector_desc"));
                billSectorList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billSectorList;
    }

    @Override
    public ArrayList getPostClassList() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList billPostClassList = new ArrayList();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM g_post_class");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("post_class"));
                so.setLabel(rs.getString("post_class"));
                billPostClassList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billPostClassList;
    }

    @Override
    public ArrayList getBillTypeList() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList billTypeList = new ArrayList();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM g_bill_type order by bill_desc");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("bill_type"));
                so.setLabel(rs.getString("bill_desc"));
                billTypeList.add(so);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billTypeList;
    }

    @Override
    public ArrayList getDemandList() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList billDemandList = new ArrayList();
        String desc = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT DEMAND_NUMBER,DESCRIPTION FROM (SELECT DISTINCT DEMAND_NUMBER FROM G_CHART_OF_ACCOUNT ORDER BY DEMAND_NUMBER)T1 INNER JOIN G_DEMAND_NO ON  T1.DEMAND_NUMBER=G_DEMAND_NO.DEMAND_NO");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("DEMAND_NUMBER"));
                desc = rs.getString("DESCRIPTION") + "(" + rs.getString("DEMAND_NUMBER") + ")";
                so.setLabel(desc);
                billDemandList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billDemandList;
    }

    @Override
    public ArrayList getMajorHeadList(String demandno) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList majorHeadList = new ArrayList();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT DISTINCT MAJOR_HEAD FROM G_CHART_OF_ACCOUNT WHERE DEMAND_NUMBER=?");
            pstmt.setString(1, demandno);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("MAJOR_HEAD"));
                so.setLabel(rs.getString("MAJOR_HEAD"));
                majorHeadList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return majorHeadList;
    }

    @Override
    public ArrayList getSubMajorHeadList(String demandNo, String majorhead) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList subMajorHeadList = new ArrayList();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT DISTINCT SUBMAJOR_HEAD FROM G_CHART_OF_ACCOUNT WHERE DEMAND_NUMBER=? AND MAJOR_HEAD=?");
            pstmt.setString(1, demandNo);
            pstmt.setString(2, majorhead);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("SUBMAJOR_HEAD"));
                so.setLabel(rs.getString("SUBMAJOR_HEAD"));
                subMajorHeadList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return subMajorHeadList;
    }

    @Override
    public ArrayList getMinorHeadList(String majorHead, String submajorhead) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList minorHeadList = new ArrayList();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT DISTINCT MINOR_HEAD FROM G_CHART_OF_ACCOUNT WHERE SUBMAJOR_HEAD=? AND MAJOR_HEAD=?");
            pstmt.setString(1, submajorhead);
            pstmt.setString(2, majorHead);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("MINOR_HEAD"));
                so.setLabel(rs.getString("MINOR_HEAD"));
                minorHeadList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return minorHeadList;
    }

    @Override
    public ArrayList getSubMinorHeadList(String subMajorHead, String minorhead) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList subMinorHeadList = new ArrayList();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT DISTINCT SUB_HEAD FROM G_CHART_OF_ACCOUNT WHERE MINOR_HEAD=? AND SUBMAJOR_HEAD=?");
            pstmt.setString(1, minorhead);
            pstmt.setString(2, subMajorHead);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("SUB_HEAD"));
                so.setLabel(rs.getString("SUB_HEAD"));
                subMinorHeadList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return subMinorHeadList;
    }

    @Override
    public ArrayList getDetailHeadList(String minorhead, String subhead) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList detailHeadList = new ArrayList();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT DISTINCT DETAIL_HEAD FROM G_CHART_OF_ACCOUNT WHERE SUB_HEAD=? AND MINOR_HEAD=?");
            pstmt.setString(1, subhead);
            pstmt.setString(2, minorhead);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("DETAIL_HEAD"));
                so.setLabel(rs.getString("DETAIL_HEAD"));
                detailHeadList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return detailHeadList;
    }

    @Override
    public ArrayList getChargedVotedList(String detailhead, String subminorhead) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList chargedVotedList = new ArrayList();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT DISTINCT CHARGED_VOTED FROM G_CHART_OF_ACCOUNT WHERE DETAIL_HEAD=? AND SUB_HEAD=?");
            pstmt.setString(1, detailhead);
            pstmt.setString(2, subminorhead);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("CHARGED_VOTED"));
                so.setLabel(rs.getString("CHARGED_VOTED"));
                chargedVotedList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return chargedVotedList;
    }

    @Override
    public ArrayList getObjectHeadList(String detailhead) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList objectHeadList = new ArrayList();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT DISTINCT object_head FROM G_CHART_OF_ACCOUNT WHERE DETAIL_HEAD=?");
            pstmt.setString(1, detailhead);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("object_head"));
                so.setLabel(rs.getString("object_head"));
                objectHeadList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return objectHeadList;
    }

    @Override
    public ArrayList getAvailableSectionList(String offCode) {
        String query = "";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        ArrayList a1 = new ArrayList();
        BillGroup billGroup = null;

        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT * FROM G_OFFICE WHERE P_OFF_CODE=? AND LVL='20' ");
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                query = "SELECT SECTION_ID,SECTION_NAME,GS.OFF_CODE,GO.OFF_EN FROM\n"
                        + "(SELECT * FROM (SELECT * FROM G_SECTION WHERE OFF_CODE=? OR \n"
                        + "(OFF_CODE IN (SELECT OFF_CODE FROM G_OFFICE WHERE LVL='20' AND P_OFF_CODE=?)))G_SECTION\n"
                        + " WHERE SECTION_ID NOT IN\n"
                        + "(SELECT SECTION_ID FROM BILL_SECTION_MAPPING) ORDER BY G_SECTION.OFF_CODE)GS\n"
                        + "INNER JOIN G_OFFICE GO ON GS.OFF_CODE=GO.OFF_CODE ORDER BY GO.OFF_EN  ";
                pstmt = con.prepareStatement(query);
                pstmt.setString(1, offCode);
                pstmt.setString(2, offCode);

                rs = pstmt.executeQuery();

                while (rs.next()) {
                    billGroup = new BillGroup();
                    billGroup.setSectionId(rs.getString("SECTION_ID"));
                    billGroup.setSectionName(rs.getString("SECTION_NAME"));
                    billGroup.setOffName(rs.getString("OFF_EN"));
                    a1.add(billGroup);
                }

            } else {

                query = "SELECT SECTION_ID,SECTION_NAME FROM (SELECT * FROM G_SECTION WHERE OFF_CODE=?)G_SECTION  WHERE SECTION_ID NOT IN  "
                        + "(SELECT SECTION_ID FROM BILL_SECTION_MAPPING) ORDER BY SECTION_NAME ";
                pstmt = con.prepareStatement(query);
                pstmt.setString(1, offCode);

                rs = pstmt.executeQuery();

                while (rs.next()) {
                    billGroup = new BillGroup();
                    billGroup.setSectionId(rs.getString("SECTION_ID"));
                    billGroup.setSectionName(rs.getString("SECTION_NAME"));
                    a1.add(billGroup);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);

        }
        return a1;
    }

    @Override
    public String getBillName(BigDecimal billgrpId) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String secName = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT DESCRIPTION FROM BILL_GROUP_MASTER WHERE BILL_GROUP_ID=?");
            pstmt.setBigDecimal(1, billgrpId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                secName = rs.getString("DESCRIPTION").toUpperCase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return secName;
    }

    @Override
    public ArrayList getAssignedSectionList(BigDecimal bgId, String offCode, String isDDO) {
        String query = "";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        ArrayList a1 = new ArrayList();
        BillGroup billGroup = null;

        try {
            con = dataSource.getConnection();
            if (isDDO != null && isDDO.equals("Y")) {
                query = "SELECT SEC_SL_NO,GS.SECTION_ID,SECTION_NAME,GOFF.OFF_CODE,OFF_EN,LVL FROM\n"
                        + "(SELECT SEC_SL_NO,G_SECTION.SECTION_ID,SECTION_NAME,OFF_CODE FROM (SELECT SECTION_ID,SEC_SL_NO FROM BILL_SECTION_MAPPING \n"
                        + "WHERE BILL_GROUP_ID =?) BILL_SECTION_MAPPING\n"
                        + "LEFT OUTER JOIN  G_SECTION  ON BILL_SECTION_MAPPING.SECTION_ID=G_SECTION.SECTION_ID ORDER BY SEC_SL_NO)GS\n"
                        + "LEFT OUTER JOIN (SELECT * FROM G_OFFICE WHERE LVL='20')GOFF ON GS.OFF_CODE=GOFF.OFF_CODE ORDER BY OFF_EN";
                pstmt = con.prepareStatement(query);
                pstmt.setBigDecimal(1, bgId);
                rs = pstmt.executeQuery();
            } else {
                query = "SELECT SEC_SL_NO,GS.SECTION_ID,SECTION_NAME,GOFF.OFF_CODE,OFF_EN,LVL FROM\n"
                        + "(SELECT SEC_SL_NO,G_SECTION.SECTION_ID,SECTION_NAME,OFF_CODE FROM (SELECT SECTION_ID,SEC_SL_NO FROM BILL_SECTION_MAPPING \n"
                        + "WHERE BILL_GROUP_ID =?) BILL_SECTION_MAPPING\n"
                        + "LEFT OUTER JOIN  G_SECTION  ON BILL_SECTION_MAPPING.SECTION_ID=G_SECTION.SECTION_ID ORDER BY SEC_SL_NO)GS\n"
                        + "LEFT OUTER JOIN (SELECT * FROM G_OFFICE WHERE LVL='20')GOFF ON GS.OFF_CODE=GOFF.OFF_CODE WHERE GS.off_code=? ORDER BY OFF_EN";
                pstmt = con.prepareStatement(query);
                pstmt.setBigDecimal(1, bgId);
                pstmt.setString(2, offCode);
                rs = pstmt.executeQuery();
            }

            while (rs.next()) {
                billGroup = new BillGroup();
                billGroup.setSectionId(rs.getString("SECTION_ID"));
                billGroup.setSectionName(rs.getString("SECTION_NAME").toUpperCase());
                billGroup.setOffName(rs.getString("OFF_EN"));
                billGroup.setOffcode(rs.getString("OFF_CODE"));
                billGroup.setOfflvl(rs.getString("LVL"));
                a1.add(billGroup);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);

        }
        return a1;
    }

    @Override
    public ArrayList getSectionList(String billGroupId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList sectionList = new ArrayList();
        GroupPayFixation groupPayFix = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT SECTION_ID FROM BILL_SECTION_MAPPING WHERE BILL_GROUP_ID=?");

            pstmt.setDouble(1, Double.parseDouble(billGroupId));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                groupPayFix = new GroupPayFixation();
                groupPayFix.setSectionId(rs.getString("SECTION_ID"));
                sectionList.add(groupPayFix);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return sectionList;
    }

    public BigDecimal getMaxId() {
        Statement st = null;
        ResultSet rs = null;
        Connection con = null;
        BigDecimal mCode = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT MAX(cast ( MAP_ID as BIGINT ))+1 MCODE FROM BILL_SECTION_MAPPING");
            if (rs.next()) {
                mCode = rs.getBigDecimal("MCODE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return mCode;
    }

    @Override
    public void mapSection(BigDecimal billgroupid, int sectionId) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("INSERT INTO BILL_SECTION_MAPPING(MAP_ID,BILL_GROUP_ID,SECTION_ID,SEC_SL_NO) VALUES(?,?,?,?) ");

            pstmt.setBigDecimal(1, getMaxId());
            pstmt.setBigDecimal(2, billgroupid);
            pstmt.setInt(3, sectionId);
            pstmt.setInt(4, 4);
            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void removeSection(int sectionId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("DELETE FROM BILL_SECTION_MAPPING WHERE section_id=?");
            pstmt.setInt(1, sectionId);
            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String getConfigurationLvl(BigDecimal billGroupId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String configLvl = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT CONFIGURED_LVL FROM BILL_GROUP_MASTER WHERE BILL_GROUP_ID=?");
            pstmt.setBigDecimal(1, billGroupId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                configLvl = rs.getString("CONFIGURED_LVL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return configLvl;
    }

    @Override
    public BillGroupConfig getBillConfigData(String offCode, BigDecimal billGroupId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BillGroupConfig billGroupConfig = new BillGroupConfig();
        try {
            billGroupConfig.setBillgroupid(billGroupId);
            billGroupConfig.setOfficeCode(offCode);
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT CONFIGURED_LVL,DESCRIPTION FROM BILL_GROUP_MASTER WHERE BILL_GROUP_ID=?");
            pstmt.setBigDecimal(1, billGroupId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                billGroupConfig.setBillgroupdesc(rs.getString("DESCRIPTION"));
                billGroupConfig.setConfiglvl(rs.getString("CONFIGURED_LVL"));
            } else {
                billGroupConfig.setApplyToAllBill("B");
            }

            if (billGroupConfig.getConfiglvl() != null && !billGroupConfig.getConfiglvl().equals("")) {
                if (rs.getString("CONFIGURED_LVL").equalsIgnoreCase("B")) {
                    pstmt = con.prepareStatement("SELECT COL_NUMBER,AD_CODE,BILL_FORMAT FROM BILL_CONFIGURATION WHERE BILL_GROUP_ID=? ORDER BY COL_NUMBER,ROW_NUMBER");
                    pstmt.setBigDecimal(1, billGroupId);
                    rs = pstmt.executeQuery();
                    int ctr = 1;
                    while (rs.next()) {
                        if (rs.getInt("COL_NUMBER") == 6) {
                            if (ctr == 1) {
                                billGroupConfig.setChkCol6Bill(true);
                                billGroupConfig.setBillFormat(rs.getString("BILL_FORMAT"));
                                billGroupConfig.setSltCol6sl1(rs.getString("AD_CODE"));
                            } else if (ctr == 2) {
                                billGroupConfig.setSltCol6sl2(rs.getString("AD_CODE"));
                            } else if (ctr == 3) {
                                billGroupConfig.setSltCol6sl3(rs.getString("AD_CODE"));
                            } else if (ctr == 4) {
                                billGroupConfig.setSltCol6sl4(rs.getString("AD_CODE"));
                            } else if (ctr == 5) {
                                billGroupConfig.setSltCol6sl5(rs.getString("AD_CODE"));
                            } else if (ctr == 6) {
                                billGroupConfig.setSltCol6sl6(rs.getString("AD_CODE"));
                            } else if (ctr == 7) {
                                billGroupConfig.setSltCol6sl7(rs.getString("AD_CODE"));
                            } else if (ctr == 8) {
                                billGroupConfig.setSltCol6sl8(rs.getString("AD_CODE"));
                                ctr = 0;
                            }
                        } else if (rs.getInt("COL_NUMBER") == 7) {
                            if (ctr == 1) {
                                billGroupConfig.setChkCol7Bill(true);
                                billGroupConfig.setBillFormat(rs.getString("BILL_FORMAT"));
                                billGroupConfig.setSltCol7sl1(rs.getString("AD_CODE"));
                            } else if (ctr == 2) {
                                billGroupConfig.setSltCol7sl2(rs.getString("AD_CODE"));
                            } else if (ctr == 3) {
                                billGroupConfig.setSltCol7sl3(rs.getString("AD_CODE"));
                            } else if (ctr == 4) {
                                billGroupConfig.setSltCol7sl4(rs.getString("AD_CODE"));
                            } else if (ctr == 5) {
                                billGroupConfig.setSltCol7sl5(rs.getString("AD_CODE"));
                            } else if (ctr == 6) {
                                billGroupConfig.setSltCol7sl6(rs.getString("AD_CODE"));
                            } else if (ctr == 7) {
                                billGroupConfig.setSltCol7sl7(rs.getString("AD_CODE"));
                            } else if (ctr == 8) {
                                billGroupConfig.setSltCol7sl8(rs.getString("AD_CODE"));
                                ctr = 0;
                            }
                        } else if (rs.getInt("COL_NUMBER") == 16) {

                            if (ctr == 1) {
                                billGroupConfig.setChkCol16Bill(true);
                                billGroupConfig.setBillFormat(rs.getString("BILL_FORMAT"));
                                billGroupConfig.setSltCol16sl1(rs.getString("AD_CODE"));
                            } else if (ctr == 2) {
                                billGroupConfig.setSltCol16sl2(rs.getString("AD_CODE"));
                            } else if (ctr == 3) {
                                billGroupConfig.setSltCol16sl3(rs.getString("AD_CODE"));
                            } else if (ctr == 4) {
                                billGroupConfig.setSltCol16sl4(rs.getString("AD_CODE"));
                            } else if (ctr == 5) {
                                billGroupConfig.setSltCol16sl5(rs.getString("AD_CODE"));
                                ctr = 0;
                            }
                        } else if (rs.getInt("COL_NUMBER") == 17) {
                            if (ctr == 1) {
                                billGroupConfig.setChkCol17Bill(true);
                                billGroupConfig.setBillFormat(rs.getString("BILL_FORMAT"));
                                billGroupConfig.setSltCol17sl1(rs.getString("AD_CODE"));
                            } else if (ctr == 2) {
                                billGroupConfig.setSltCol17sl2(rs.getString("AD_CODE"));
                            } else if (ctr == 3) {
                                billGroupConfig.setSltCol17sl3(rs.getString("AD_CODE"));
                            } else if (ctr == 4) {
                                billGroupConfig.setSltCol17sl4(rs.getString("AD_CODE"));
                            } else if (ctr == 5) {
                                billGroupConfig.setSltCol17sl5(rs.getString("AD_CODE"));
                                ctr = 0;
                            }
                        } else if (rs.getInt("COL_NUMBER") == 18) {
                            if (ctr == 1) {
                                billGroupConfig.setChkCol18Bill(true);
                                billGroupConfig.setBillFormat(rs.getString("BILL_FORMAT"));
                                billGroupConfig.setSltCol18sl1(rs.getString("AD_CODE"));
                            } else if (ctr == 2) {
                                billGroupConfig.setSltCol18sl2(rs.getString("AD_CODE"));
                            } else if (ctr == 3) {
                                billGroupConfig.setSltCol18sl3(rs.getString("AD_CODE"));
                            } else if (ctr == 4) {
                                billGroupConfig.setSltCol18sl4(rs.getString("AD_CODE"));
                            } else if (ctr == 5) {
                                billGroupConfig.setSltCol18sl5(rs.getString("AD_CODE"));
                                ctr = 0;
                            }
                        }

                        ctr++;
                    }
                    billGroupConfig.setApplyToAllBill("B");
                } else if (rs.getString("CONFIGURED_LVL").equalsIgnoreCase("O")) {
                    pstmt = con.prepareStatement("SELECT COL_NUMBER,AD_CODE,BILL_FORMAT FROM BILL_CONFIGURATION WHERE OFF_CODE=? ORDER BY COL_NUMBER,ROW_NUMBER");
                    pstmt.setString(1, offCode);
                    rs = pstmt.executeQuery();
                    int ctr = 1;
                    while (rs.next()) {
                        if (rs.getInt("COL_NUMBER") == 6) {
                            if (ctr == 1) {
                                billGroupConfig.setChkCol6Bill(true);
                                billGroupConfig.setBillFormat(rs.getString("BILL_FORMAT"));
                                billGroupConfig.setSltCol6sl1(rs.getString("AD_CODE"));
                            } else if (ctr == 2) {
                                billGroupConfig.setSltCol6sl2(rs.getString("AD_CODE"));
                            } else if (ctr == 3) {
                                billGroupConfig.setSltCol6sl3(rs.getString("AD_CODE"));
                            } else if (ctr == 4) {
                                billGroupConfig.setSltCol6sl4(rs.getString("AD_CODE"));
                            } else if (ctr == 5) {
                                billGroupConfig.setSltCol6sl5(rs.getString("AD_CODE"));
                            } else if (ctr == 6) {
                                billGroupConfig.setSltCol6sl6(rs.getString("AD_CODE"));
                            } else if (ctr == 7) {
                                billGroupConfig.setSltCol6sl7(rs.getString("AD_CODE"));
                            } else if (ctr == 8) {
                                billGroupConfig.setSltCol6sl8(rs.getString("AD_CODE"));
                                ctr = 0;
                            }
                        } else if (rs.getInt("COL_NUMBER") == 7) {
                            if (ctr == 1) {
                                billGroupConfig.setChkCol7Bill(true);
                                billGroupConfig.setBillFormat(rs.getString("BILL_FORMAT"));
                                billGroupConfig.setSltCol7sl1(rs.getString("AD_CODE"));
                            } else if (ctr == 2) {
                                billGroupConfig.setSltCol7sl2(rs.getString("AD_CODE"));
                            } else if (ctr == 3) {
                                billGroupConfig.setSltCol7sl3(rs.getString("AD_CODE"));
                            } else if (ctr == 4) {
                                billGroupConfig.setSltCol7sl4(rs.getString("AD_CODE"));
                            } else if (ctr == 5) {
                                billGroupConfig.setSltCol7sl5(rs.getString("AD_CODE"));
                            } else if (ctr == 6) {
                                billGroupConfig.setSltCol7sl6(rs.getString("AD_CODE"));
                            } else if (ctr == 7) {
                                billGroupConfig.setSltCol7sl7(rs.getString("AD_CODE"));
                            } else if (ctr == 8) {
                                billGroupConfig.setSltCol7sl8(rs.getString("AD_CODE"));
                                ctr = 0;
                            }
                        } else if (rs.getInt("COL_NUMBER") == 16) {
                            if (ctr == 1) {
                                billGroupConfig.setChkCol16Bill(true);
                                billGroupConfig.setBillFormat(rs.getString("BILL_FORMAT"));
                                billGroupConfig.setSltCol16sl1(rs.getString("AD_CODE"));
                            } else if (ctr == 2) {
                                billGroupConfig.setSltCol16sl2(rs.getString("AD_CODE"));
                            } else if (ctr == 3) {
                                billGroupConfig.setSltCol16sl3(rs.getString("AD_CODE"));
                            } else if (ctr == 4) {
                                billGroupConfig.setSltCol16sl4(rs.getString("AD_CODE"));
                            } else if (ctr == 5) {
                                billGroupConfig.setSltCol16sl5(rs.getString("AD_CODE"));
                                ctr = 0;
                            }
                        } else if (rs.getInt("COL_NUMBER") == 17) {
                            if (ctr == 1) {
                                billGroupConfig.setChkCol17Bill(true);
                                billGroupConfig.setBillFormat(rs.getString("BILL_FORMAT"));
                                billGroupConfig.setSltCol17sl1(rs.getString("AD_CODE"));
                            } else if (ctr == 2) {
                                billGroupConfig.setSltCol17sl2(rs.getString("AD_CODE"));
                            } else if (ctr == 3) {
                                billGroupConfig.setSltCol17sl3(rs.getString("AD_CODE"));
                            } else if (ctr == 4) {
                                billGroupConfig.setSltCol17sl4(rs.getString("AD_CODE"));
                            } else if (ctr == 5) {
                                billGroupConfig.setSltCol17sl5(rs.getString("AD_CODE"));
                                ctr = 0;
                            }
                        } else if (rs.getInt("COL_NUMBER") == 18) {
                            if (ctr == 1) {
                                billGroupConfig.setChkCol18Bill(true);
                                billGroupConfig.setBillFormat(rs.getString("BILL_FORMAT"));
                                billGroupConfig.setSltCol18sl1(rs.getString("AD_CODE"));
                            } else if (ctr == 2) {
                                billGroupConfig.setSltCol18sl2(rs.getString("AD_CODE"));
                            } else if (ctr == 3) {
                                billGroupConfig.setSltCol18sl3(rs.getString("AD_CODE"));
                            } else if (ctr == 4) {
                                billGroupConfig.setSltCol18sl4(rs.getString("AD_CODE"));
                            } else if (ctr == 5) {
                                billGroupConfig.setSltCol18sl5(rs.getString("AD_CODE"));
                                ctr = 0;
                            }
                        }
                        ctr++;
                    }
                    billGroupConfig.setApplyToAllBill("O");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billGroupConfig;
    }

    @Override
    public void saveBillConfigureddata(BillGroupConfig billGroupConfig) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        String configId = "";
        int count = 0;
        try {
            con = dataSource.getConnection();
            if (billGroupConfig.getApplyToAllBill() != null && !billGroupConfig.getApplyToAllBill().equals("")) {
                pstmt = con.prepareStatement("DELETE FROM BILL_CONFIGURATION WHERE OFF_CODE=?");
                pstmt.setString(1, billGroupConfig.getOfficeCode());
                pstmt.execute();

                if (billGroupConfig.isChkCol6Bill()) {
                    pstmt = con.prepareStatement("INSERT INTO BILL_CONFIGURATION(BILL_CONFIG_ID, OFF_CODE, BILL_GROUP_ID, COL_NUMBER, AD_CODE, ROW_NUMBER, BILL_FORMAT) VALUES(?,?,?,?,?,?,?) ");
                    for (int i = 1; i <= 8; i++) {
                        String adCode = "";
                        if (i == 1) {
                            adCode = billGroupConfig.getSltCol6sl1();
                        } else if (i == 2) {
                            adCode = billGroupConfig.getSltCol6sl2();
                        } else if (i == 3) {
                            adCode = billGroupConfig.getSltCol6sl3();
                        } else if (i == 4) {
                            adCode = billGroupConfig.getSltCol6sl4();
                        } else if (i == 5) {
                            adCode = billGroupConfig.getSltCol6sl5();
                        } else if (i == 6) {
                            adCode = billGroupConfig.getSltCol6sl6();
                        } else if (i == 7) {
                            adCode = billGroupConfig.getSltCol6sl7();
                        } else if (i == 8) {
                            adCode = billGroupConfig.getSltCol6sl8();
                        }
                        configId = billGroupConfig.getOfficeCode() + "_" + count;
                        pstmt.setString(1, configId);
                        pstmt.setString(2, billGroupConfig.getOfficeCode());
                        pstmt.setString(3, null);
                        pstmt.setInt(4, 6);
                        pstmt.setString(5, adCode);
                        pstmt.setInt(6, i);
                        pstmt.setString(7, billGroupConfig.getBillFormat());
                        count++;
                        pstmt.execute();
                    }
                }

                if (billGroupConfig.isChkCol7Bill()) {
                    pstmt = con.prepareStatement("INSERT INTO BILL_CONFIGURATION(BILL_CONFIG_ID, OFF_CODE, BILL_GROUP_ID, COL_NUMBER, AD_CODE, ROW_NUMBER, BILL_FORMAT) VALUES(?,?,?,?,?,?,?) ");
                    for (int i = 1; i <= 8; i++) {
                        String adCode = "";
                        if (i == 1) {
                            adCode = billGroupConfig.getSltCol7sl1();
                        } else if (i == 2) {
                            adCode = billGroupConfig.getSltCol7sl2();
                        } else if (i == 3) {
                            adCode = billGroupConfig.getSltCol7sl3();
                        } else if (i == 4) {
                            adCode = billGroupConfig.getSltCol7sl4();
                        } else if (i == 5) {
                            adCode = billGroupConfig.getSltCol7sl5();
                        } else if (i == 6) {
                            adCode = billGroupConfig.getSltCol7sl6();
                        } else if (i == 7) {
                            adCode = billGroupConfig.getSltCol7sl7();
                        } else if (i == 8) {
                            adCode = billGroupConfig.getSltCol7sl8();
                        }

                        configId = billGroupConfig.getOfficeCode() + "_" + count;
                        pstmt.setString(1, configId);
                        pstmt.setString(2, billGroupConfig.getOfficeCode());
                        pstmt.setBigDecimal(3, null);
                        pstmt.setInt(4, 7);
                        pstmt.setString(5, adCode);
                        pstmt.setInt(6, i);
                        pstmt.setString(7, billGroupConfig.getBillFormat());
                        count++;
                        pstmt.execute();
                    }

                }
                if (billGroupConfig.isChkCol16Bill()) {
                    for (int i = 1; i <= 5; i++) {
                        String adCode = "";
                        if (i == 1) {
                            adCode = billGroupConfig.getSltCol16sl1();
                        } else if (i == 2) {
                            adCode = billGroupConfig.getSltCol16sl2();
                        } else if (i == 3) {
                            adCode = billGroupConfig.getSltCol16sl3();
                        } else if (i == 4) {
                            adCode = billGroupConfig.getSltCol16sl4();
                        } else if (i == 5) {
                            adCode = billGroupConfig.getSltCol16sl5();
                        }

                        configId = billGroupConfig.getOfficeCode() + "_" + count;
                        pstmt = con.prepareStatement("INSERT INTO BILL_CONFIGURATION(BILL_CONFIG_ID, OFF_CODE, BILL_GROUP_ID, COL_NUMBER, AD_CODE, ROW_NUMBER, BILL_FORMAT) VALUES(?,?,?,?,?,?,?) ");
                        pstmt.setString(1, configId);
                        pstmt.setString(2, billGroupConfig.getOfficeCode());
                        pstmt.setString(3, null);
                        pstmt.setInt(4, 16);
                        pstmt.setString(5, adCode);
                        pstmt.setInt(6, i);
                        pstmt.setString(7, billGroupConfig.getBillFormat());
                        count++;
                        pstmt.execute();
                    }

                }

                if (billGroupConfig.isChkCol17Bill()) {
                    pstmt = con.prepareStatement("INSERT INTO BILL_CONFIGURATION(BILL_CONFIG_ID, OFF_CODE, BILL_GROUP_ID, COL_NUMBER, AD_CODE, ROW_NUMBER, BILL_FORMAT) VALUES(?,?,?,?,?,?,?) ");
                    for (int i = 1; i <= 5; i++) {
                        String adCode = "";
                        if (i == 1) {
                            adCode = billGroupConfig.getSltCol17sl1();
                        } else if (i == 2) {
                            adCode = billGroupConfig.getSltCol17sl2();
                        } else if (i == 3) {
                            adCode = billGroupConfig.getSltCol17sl3();
                        } else if (i == 4) {
                            adCode = billGroupConfig.getSltCol17sl4();
                        } else if (i == 5) {
                            adCode = billGroupConfig.getSltCol17sl5();
                        }

                        configId = billGroupConfig.getOfficeCode() + "_" + count;
                        pstmt.setString(1, configId);
                        pstmt.setString(2, billGroupConfig.getOfficeCode());
                        pstmt.setString(3, null);
                        pstmt.setInt(4, 17);
                        pstmt.setString(5, adCode);
                        pstmt.setInt(6, i);
                        pstmt.setString(7, billGroupConfig.getBillFormat());
                        count++;
                        pstmt.execute();
                    }
                }

                if (billGroupConfig.isChkCol18Bill()) {
                    for (int i = 1; i <= 5; i++) {
                        String adCode = "";
                        if (i == 1) {
                            adCode = billGroupConfig.getSltCol18sl1();
                        } else if (i == 2) {
                            adCode = billGroupConfig.getSltCol18sl2();
                        } else if (i == 3) {
                            adCode = billGroupConfig.getSltCol18sl3();
                        } else if (i == 4) {
                            adCode = billGroupConfig.getSltCol18sl4();
                        } else if (i == 5) {
                            adCode = billGroupConfig.getSltCol18sl5();
                        }

                        configId = billGroupConfig.getOfficeCode() + "_" + count;
                        pstmt = con.prepareStatement("INSERT INTO BILL_CONFIGURATION(BILL_CONFIG_ID, OFF_CODE, BILL_GROUP_ID, COL_NUMBER, AD_CODE, ROW_NUMBER, BILL_FORMAT) VALUES(?,?,?,?,?,?,?) ");
                        pstmt.setString(1, configId);
                        pstmt.setString(2, billGroupConfig.getOfficeCode());
                        pstmt.setString(3, null);
                        pstmt.setInt(4, 18);
                        pstmt.setString(5, adCode);
                        pstmt.setInt(6, i);
                        pstmt.setString(7, billGroupConfig.getBillFormat());
                        count++;
                        pstmt.execute();
                    }
                }

                pstmt = con.prepareStatement("UPDATE BILL_GROUP_MASTER SET CONFIGURED_LVL='O' WHERE OFF_CODE=?");
                pstmt.setString(1, billGroupConfig.getOfficeCode());
                pstmt.execute();

            } else {
                if ((billGroupConfig.getApplyToAllBill() != null && !billGroupConfig.getApplyToAllBill().equals("")) && billGroupConfig.getApplyToAllBill().equalsIgnoreCase("O")) {
                    pstmt = con.prepareStatement("DELETE FROM BILL_CONFIGURATION WHERE OFF_CODE=?");
                    pstmt.setString(1, billGroupConfig.getOfficeCode());
                    pstmt.execute();

                    pstmt = con.prepareStatement("UPDATE BILL_GROUP_MASTER SET CONFIGURED_LVL=null WHERE BILL_GROUP_ID <> ? AND OFF_CODE=?");
                    pstmt.setBigDecimal(1, billGroupConfig.getBillgroupid());
                    pstmt.setString(2, billGroupConfig.getOfficeCode());
                    pstmt.execute();
                } else {
                    pstmt = con.prepareStatement("DELETE FROM BILL_CONFIGURATION WHERE BILL_GROUP_ID=?");
                    pstmt.setBigDecimal(1, billGroupConfig.getBillgroupid());
                    pstmt.execute();
                }
                pstmt = con.prepareStatement("SELECT COUNT(OFF_CODE) CNT FROM BILL_CONFIGURATION WHERE OFF_CODE=?");
                pstmt.setString(1, billGroupConfig.getOfficeCode());
                res = pstmt.executeQuery();
                if (res.next()) {
                    if (res.getInt("CNT") > 0) {
                        count = res.getInt("CNT");
                    } else {
                        count = 1;
                    }
                }

                if (billGroupConfig.isChkCol6Bill()) {
                    pstmt = con.prepareStatement("INSERT INTO BILL_CONFIGURATION(BILL_CONFIG_ID, OFF_CODE, BILL_GROUP_ID, COL_NUMBER, AD_CODE, ROW_NUMBER, BILL_FORMAT) VALUES(?,?,?,?,?,?,?) ");
                    for (int i = 1; i <= 8; i++) {
                        String adCode = "";
                        if (i == 1) {
                            adCode = billGroupConfig.getSltCol6sl1();
                        } else if (i == 2) {
                            adCode = billGroupConfig.getSltCol6sl2();
                        } else if (i == 3) {
                            adCode = billGroupConfig.getSltCol6sl3();
                        } else if (i == 4) {
                            adCode = billGroupConfig.getSltCol6sl4();
                        } else if (i == 5) {
                            adCode = billGroupConfig.getSltCol6sl5();
                        } else if (i == 6) {
                            adCode = billGroupConfig.getSltCol6sl6();
                        } else if (i == 7) {
                            adCode = billGroupConfig.getSltCol6sl7();
                        } else if (i == 8) {
                            adCode = billGroupConfig.getSltCol6sl8();
                        }

                        configId = billGroupConfig.getOfficeCode() + "_" + count;
                        pstmt.setString(1, configId);
                        pstmt.setString(2, billGroupConfig.getOfficeCode());
                        pstmt.setBigDecimal(3, billGroupConfig.getBillgroupid());
                        pstmt.setInt(4, 6);
                        pstmt.setString(5, adCode);
                        pstmt.setInt(6, i);
                        pstmt.setString(7, billGroupConfig.getBillFormat());
                        count++;
                        pstmt.execute();
                    }

                }
                if (billGroupConfig.isChkCol7Bill()) {
                    pstmt = con.prepareStatement("INSERT INTO BILL_CONFIGURATION(BILL_CONFIG_ID, OFF_CODE, BILL_GROUP_ID, COL_NUMBER, AD_CODE, ROW_NUMBER, BILL_FORMAT) VALUES(?,?,?,?,?,?,?) ");
                    for (int i = 1; i <= 8; i++) {
                        String adCode = "";
                        if (i == 1) {
                            adCode = billGroupConfig.getSltCol7sl1();
                        } else if (i == 2) {
                            adCode = billGroupConfig.getSltCol7sl2();
                        } else if (i == 3) {
                            adCode = billGroupConfig.getSltCol7sl3();
                        } else if (i == 4) {
                            adCode = billGroupConfig.getSltCol7sl4();
                        } else if (i == 5) {
                            adCode = billGroupConfig.getSltCol7sl5();
                        } else if (i == 6) {
                            adCode = billGroupConfig.getSltCol7sl6();
                        } else if (i == 7) {
                            adCode = billGroupConfig.getSltCol7sl7();
                        } else if (i == 8) {
                            adCode = billGroupConfig.getSltCol7sl8();
                        }

                        configId = billGroupConfig.getOfficeCode() + "_" + count;

                        pstmt.setString(1, configId);
                        pstmt.setString(2, billGroupConfig.getOfficeCode());
                        pstmt.setBigDecimal(3, billGroupConfig.getBillgroupid());
                        pstmt.setInt(4, 7);
                        pstmt.setString(5, adCode);
                        pstmt.setInt(6, i);
                        pstmt.setString(7, billGroupConfig.getBillFormat());
                        count++;
                        pstmt.execute();
                    }

                }
                if (billGroupConfig.isChkCol16Bill()) {
                    pstmt = con.prepareStatement("INSERT INTO BILL_CONFIGURATION(BILL_CONFIG_ID, OFF_CODE, BILL_GROUP_ID, COL_NUMBER, AD_CODE, ROW_NUMBER, BILL_FORMAT) VALUES(?,?,?,?,?,?,?) ");
                    for (int i = 1; i <= 5; i++) {
                        String adCode = "";
                        if (i == 1) {
                            adCode = billGroupConfig.getSltCol16sl1();
                        } else if (i == 2) {
                            adCode = billGroupConfig.getSltCol16sl2();
                        } else if (i == 3) {
                            adCode = billGroupConfig.getSltCol16sl3();
                        } else if (i == 4) {
                            adCode = billGroupConfig.getSltCol16sl4();
                        } else if (i == 5) {
                            adCode = billGroupConfig.getSltCol16sl5();
                        }

                        configId = billGroupConfig.getOfficeCode() + "_" + count;
                        pstmt.setString(1, configId);
                        pstmt.setString(2, billGroupConfig.getOfficeCode());
                        pstmt.setBigDecimal(3, billGroupConfig.getBillgroupid());
                        pstmt.setInt(4, 16);
                        pstmt.setString(5, adCode);
                        pstmt.setInt(6, i);
                        pstmt.setString(7, billGroupConfig.getBillFormat());
                        count++;
                        pstmt.execute();
                    }
                }

                if (billGroupConfig.isChkCol17Bill()) {
                    for (int i = 1; i <= 5; i++) {
                        String adCode = "";
                        if (i == 1) {
                            adCode = billGroupConfig.getSltCol17sl1();
                        } else if (i == 2) {
                            adCode = billGroupConfig.getSltCol17sl2();
                        } else if (i == 3) {
                            adCode = billGroupConfig.getSltCol17sl3();
                        } else if (i == 4) {
                            adCode = billGroupConfig.getSltCol17sl4();
                        } else if (i == 5) {
                            adCode = billGroupConfig.getSltCol17sl5();
                        }

                        configId = billGroupConfig.getOfficeCode() + "_" + count;
                        pstmt = con.prepareStatement("INSERT INTO BILL_CONFIGURATION(BILL_CONFIG_ID, OFF_CODE, BILL_GROUP_ID, COL_NUMBER, AD_CODE, ROW_NUMBER, BILL_FORMAT) VALUES(?,?,?,?,?,?,?) ");
                        pstmt.setString(1, configId);
                        pstmt.setString(2, billGroupConfig.getOfficeCode());
                        pstmt.setBigDecimal(3, billGroupConfig.getBillgroupid());
                        pstmt.setInt(4, 17);
                        pstmt.setString(5, adCode);
                        pstmt.setInt(6, i);
                        pstmt.setString(7, billGroupConfig.getBillFormat());
                        count++;
                        pstmt.execute();
                    }
                }

                DataBaseFunctions.closeSqlObjects(pstmt);
                if (billGroupConfig.isChkCol18Bill()) {
                    for (int i = 1; i <= 5; i++) {
                        String adCode = "";
                        if (i == 1) {
                            adCode = billGroupConfig.getSltCol18sl1();
                        } else if (i == 2) {
                            adCode = billGroupConfig.getSltCol18sl2();
                        } else if (i == 3) {
                            adCode = billGroupConfig.getSltCol18sl3();
                        } else if (i == 4) {
                            adCode = billGroupConfig.getSltCol18sl4();
                        } else if (i == 5) {
                            adCode = billGroupConfig.getSltCol18sl5();
                        }

                        configId = billGroupConfig.getOfficeCode() + "_" + count;
                        pstmt = con.prepareStatement("INSERT INTO BILL_CONFIGURATION(BILL_CONFIG_ID, OFF_CODE, BILL_GROUP_ID, COL_NUMBER, AD_CODE, ROW_NUMBER, BILL_FORMAT) VALUES(?,?,?,?,?,?,?) ");
                        pstmt.setString(1, configId);
                        pstmt.setString(2, billGroupConfig.getOfficeCode());
                        pstmt.setBigDecimal(3, billGroupConfig.getBillgroupid());
                        pstmt.setInt(4, 18);
                        pstmt.setString(5, adCode);
                        pstmt.setInt(6, i);
                        pstmt.setString(7, billGroupConfig.getBillFormat());
                        count++;
                        pstmt.execute();
                    }
                }
                DataBaseFunctions.closeSqlObjects(pstmt);
                pstmt = con.prepareStatement("UPDATE BILL_GROUP_MASTER SET CONFIGURED_LVL='B' WHERE BILL_GROUP_ID=?");
                pstmt.setBigDecimal(1, billGroupConfig.getBillgroupid());
                pstmt.execute();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getColList(int colNumber) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList colList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT AD_CODE, AD_DESC FROM G_AD_LIST WHERE REP_COL=? ORDER BY AD_DESC");
            pstmt.setInt(1, colNumber);
            rs = pstmt.executeQuery();
            SelectOption lbean = null;
            while (rs.next()) {
                lbean = new SelectOption(rs.getString("AD_DESC"), rs.getString("AD_CODE"));
                colList.add(lbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return colList;
    }

    @Override
    public ArrayList getBillGroupName(String offcode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList billTypeList = new ArrayList();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM BILL_GROUP_MASTER WHERE OFF_CODE=?  and (is_deleted ='N' or is_deleted  is null)");
            pstmt.setString(1, offcode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("bill_group_id"));
                so.setLabel(rs.getString("description"));
                billTypeList.add(so);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billTypeList;
    }

    @Override
    public ArrayList compareBillDetails(String offCode, BillGroup bg) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList billcompareList = new ArrayList();

        try {
            con = dataSource.getConnection();
            /*pstmt = con.prepareStatement("select * from (SELECT emp_name as empName,emp_code,  ded_amt, gross_amt FROM  aq_mast WHERE aq_group=? AND aq_month=? AND aq_year=?"
                    + " AND  emp_code!='') tab1 left outer join (SELECT emp_name,emp_code, ded_amt TOT_DED2, gross_amt total_gross2 "
                    + "FROM  aq_mast WHERE aq_group=? AND aq_month=? AND aq_year=? AND  emp_code!='') tab2 "
                    + "on tab1.emp_code=tab2.emp_code");*/
            
            pstmt = con.prepareStatement("select * from (SELECT emp_name as empName,emp_code, (select sum(ad_amt) from aq_dtls where ad_type='D' and aqsl_no=aq_mast.aqsl_no) ded_amt, (select sum(ad_amt) from aq_dtls where ad_type='A' and aqsl_no=aq_mast.aqsl_no) gross_amt FROM aq_mast" +
                                        " WHERE aq_group=? AND aq_month=? AND aq_year=? AND emp_code!='') tab1" +
                                        " left outer join (SELECT emp_name,emp_code, (select sum(ad_amt) from aq_dtls where ad_type='D' and aqsl_no=aq_mast.aqsl_no) TOT_DED2, (select sum(ad_amt) from aq_dtls where ad_type='A' and aqsl_no=aq_mast.aqsl_no) total_gross2" +
                                        " FROM aq_mast WHERE aq_group=? AND aq_month=? AND aq_year=? AND emp_code!='') tab2" +
                                        " on tab1.emp_code=tab2.emp_code");
            pstmt.setBigDecimal(1, bg.getBillgroupid());
            pstmt.setInt(2, bg.getFromMonth());
            pstmt.setInt(3, bg.getFromYear());
            pstmt.setBigDecimal(4, bg.getBillgroupid());
            pstmt.setInt(5, bg.getToMonth());
            pstmt.setInt(6, bg.getToYear());
            rs = pstmt.executeQuery();
            int i = 0;
            while (rs.next()) {
                i++;
                BillGroup billGroup = new BillGroup();
                billGroup.setSlno(i);
                billGroup.setEmpName(rs.getString("empName"));
                int netded = rs.getInt("ded_amt");
                int grossamt = rs.getInt("gross_amt");
                int netAmount = grossamt - netded;

                int netded1 = rs.getInt("TOT_DED2");
                int grossamt1 = rs.getInt("total_gross2");
                int netAmount1 = grossamt1 - netded1;

                billGroup.setNetAmount(netAmount);
                billGroup.setNetAmount1(netAmount1);
                billGroup.setGrossAmount(rs.getInt("gross_amt"));
                billGroup.setGrossAmount1(rs.getInt("total_gross2"));

                billcompareList.add(billGroup);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billcompareList;
    }

    @Override
    public ArrayList getCol6and7List() {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        ArrayList colList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT AD_CODE, AD_DESC FROM G_AD_LIST WHERE AD_TYPE='A' AND SHOW_IN_ADLIST = 'Y' ORDER BY AD_DESC");
            rs = pstmt.executeQuery();
            SelectOption lbean = null;
            while (rs.next()) {
                lbean = new SelectOption(rs.getString("AD_DESC"), rs.getString("AD_CODE"));
                colList.add(lbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return colList;
    }

    @Override
    public ArrayList getEmpwiseList(String offcode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList empList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT cur_spc,emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME FROM emp_mast WHERE cur_off_code=? AND cur_spc != ''");
            pstmt.setString(1, offcode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("cur_spc"));
                String empName = rs.getString("EMPNAME") + "(" + rs.getString("emp_id") + ")";
                so.setLabel(empName);
                empList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empList;
    }

    @Override
    public ArrayList empWiseGroupPrivilege(String offCode, String spc) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList billprivList = new ArrayList();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select a.bill_group_id,a.description,b.spc FROM BILL_GROUP_MASTER a LEFT OUTER JOIN bill_group_privilage b ON CAST (a.bill_group_id AS BIGINT) = CAST (b.bill_grp_id AS BIGINT) AND b.spc=? AND b.off_code=? WHERE a.OFF_CODE=?  and (is_deleted ='N' or is_deleted  is null)  ");
            pstmt.setString(1, spc);
            pstmt.setString(2, offCode);
            pstmt.setString(3, offCode);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                BillGroup billGroup = new BillGroup();
                billGroup.setBillgroupid(rs.getBigDecimal("bill_group_id"));
                billGroup.setBillgroupdesc(rs.getString("description"));
                billGroup.setEmpCode(rs.getString("spc"));
                billprivList.add(billGroup);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billprivList;
    }

    @Override
    public void assignBillPrivilege(String billgroupid, String spc, int type, String offcode) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = dataSource.getConnection();
            if (type == 1) {
                String maxCode = CommonFunctions.getMaxCode("bill_group_privilage", "bilgrpprivid", con);
                pstmt = con.prepareStatement("INSERT INTO bill_group_privilage(bill_grp_id,off_code,spc,bilgrpprivid) VALUES(?,?,?,?) ");
                pstmt.setBigDecimal(1, new BigDecimal(billgroupid));
                pstmt.setString(2, offcode);
                pstmt.setString(3, spc);
                pstmt.setBigDecimal(4, new BigDecimal(maxCode));
                pstmt.execute();
            }
            if (type == 0) {
                pstmt = con.prepareStatement("DELETE FROM bill_group_privilage WHERE bill_grp_id=? AND spc=? AND  off_code=?");
                pstmt.setBigDecimal(1, new BigDecimal(billgroupid));
                pstmt.setString(2, spc);
                pstmt.setString(3, offcode);
                pstmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void saveChartofAccount(ChartOfAccount chartOfAccount) {
        int empid = 0;
        boolean flag = false;
        PreparedStatement pst = null;
        Connection con = null;
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO g_chart_of_account( demand_number,major_head ,submajor_head, minor_head, sub_head,detail_head,charged_voted,object_head) VALUES(?,?,?,?,?,?,?,?)");

            pst.setString(1, chartOfAccount.getDemandno());
            pst.setString(2, chartOfAccount.getMajorHead());
            pst.setString(3, chartOfAccount.getSubMajorHead());
            pst.setString(4, chartOfAccount.getMinorHead());
            pst.setString(5, chartOfAccount.getSubHead());
            pst.setString(6, chartOfAccount.getDetailHead());
            pst.setString(7, chartOfAccount.getChargeVoted());

            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public int excludeBillGroupFromAquitance(BigDecimal billgroupid, String status) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int retVal = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "update bill_group_master set show_in_aquitance=? where bill_group_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, status);
            pst.setBigDecimal(2, billgroupid);
            retVal = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;
    }

    @Override
    public int excludeFromTreasury(BigDecimal billgroupid, String status) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int retVal = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "update bill_group_master set submit_to_treasury=? where bill_group_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, status);
            pst.setBigDecimal(2, billgroupid);
            retVal = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;
    }

    @Override
    public List getCOList(String selectedddocode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SelectOption so = null;
        List colist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "select off_en,co_code from ddo_to_co_mapping"
                    + " inner join g_office on ddo_to_co_mapping.co_off_code=g_office.off_code where ddo_to_co_mapping.ddo_off_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, selectedddocode);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("off_en") + " (" + rs.getString("co_code") + ")");
                so.setValue(rs.getString("co_code"));

                if (rs.getString("co_code") != null && !rs.getString("co_code").equals("")) {
                    colist.add(so);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return colist;
    }

    @Override
    public ArrayList getChartOfAccountList(String ddoCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList ChartOfAccountList = new ArrayList();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM g_chart_of_account where ddo_code=? order by demand_number,major_head ASC");
            pstmt.setString(1, ddoCode);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ChartOfAccount chartOfAccount = new ChartOfAccount();
                chartOfAccount.setDemandno(rs.getString("demand_number"));
                chartOfAccount.setMajorHead(rs.getString("major_head"));
                chartOfAccount.setSubMajorHead(rs.getString("submajor_head"));
                chartOfAccount.setMinorHead(rs.getString("minor_head"));
                chartOfAccount.setSubHead(rs.getString("sub_head"));
                chartOfAccount.setDetailHead(rs.getString("detail_head"));
                chartOfAccount.setObjectHead(rs.getString("object_head"));
                chartOfAccount.setChargeVoted(rs.getString("charged_voted"));
                chartOfAccount.setChatId(rs.getString("chart_acc_id"));
                ChartOfAccountList.add(chartOfAccount);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ChartOfAccountList;
    }

    @Override
    public ArrayList getDemandListDdowise(String hiddendeptCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList demandListddowise = new ArrayList();
        String desc = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT  gdmd.demand_no, gdmd.description\n"
                    + "FROM (select * from g_department WHERE department_code=?)g_department \n"
                    + "left outer join G_DEMAND_NO gdmd on g_department.demand_no=gdmd.demand_no::INTEGER");
            pstmt.setString(1, hiddendeptCode);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("demand_no"));
                desc = rs.getString("description") + "(" + rs.getString("demand_no") + ")";
                so.setLabel(desc);
                demandListddowise.add(so);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return demandListddowise;
    }

    @Override
    public ArrayList getNewChargedVotedList() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList newchargedVotedList = new ArrayList();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM g_charged_voted");

            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("CHARGED_VOTED"));
                so.setLabel(rs.getString("CHARGED_VOTED"));
                newchargedVotedList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return newchargedVotedList;
    }

    @Override
    public void saveNewChartofAccount(ChartOfAccount chartOfAccount) {
        PreparedStatement pst = null;
        Connection con = null;
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO g_chart_of_account( demand_number,major_head ,submajor_head, minor_head, sub_head,detail_head,charged_voted,object_head, ddo_code) VALUES(?,?,?,?,?,?,?,?,?)");

            pst.setString(1, chartOfAccount.getDemandno());
            pst.setString(2, chartOfAccount.getMajorHead());
            pst.setString(3, chartOfAccount.getSubMajorHead());
            pst.setString(4, chartOfAccount.getMinorHead());
            pst.setString(5, chartOfAccount.getSubHead());
            pst.setString(6, chartOfAccount.getDetailHead());
            pst.setString(7, chartOfAccount.getChargeVoted());
            pst.setString(8, chartOfAccount.getObjectHead());
            pst.setString(9, chartOfAccount.getHiddenddoCode());

            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void saveallNewChartofAccount(ChartOfAccount chartOfAccount) {
        boolean flag = false;
        PreparedStatement pst = null;
        Connection con = null;
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO g_chart_of_account( demand_number,major_head ,submajor_head, minor_head, sub_head,detail_head,charged_voted,object_head, ddo_code) VALUES(?,?,?,?,?,?,?,?,?)");

            pst.setString(1, chartOfAccount.getHidDemandno());
            pst.setString(2, chartOfAccount.getMajorHead());
            pst.setString(3, chartOfAccount.getSubMajorHead());
            pst.setString(4, chartOfAccount.getMinorHead());
            pst.setString(5, chartOfAccount.getSubHead());
            pst.setString(6, chartOfAccount.getDetailHead());
            pst.setString(7, chartOfAccount.getChargeVoted());
            pst.setString(8, chartOfAccount.getObjectHead());
            pst.setString(9, chartOfAccount.getHiddenddoCode());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public ChartOfAccount editChartOfAccountList(ChartOfAccount chartOfAccount) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT *,gofc.ddo_code,gdept.department_name\n"
                    + "FROM (select * from g_chart_of_account WHERE ddo_code=? and chart_acc_id=?)g_chart_of_account \n"
                    + "left outer join g_office gofc on g_chart_of_account.ddo_code=gofc.ddo_code\n"
                    + "left outer join g_department gdept on gofc.department_code=gdept.department_code");
            pstmt.setString(1, chartOfAccount.getDdoCode());
            pstmt.setInt(2, Integer.parseInt(chartOfAccount.getChatId()));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                chartOfAccount = new ChartOfAccount();
                chartOfAccount.setDemandno(rs.getString("demand_number"));
                chartOfAccount.setMajorHead(rs.getString("major_head"));
                chartOfAccount.setSubMajorHead(rs.getString("submajor_head"));
                chartOfAccount.setMinorHead(rs.getString("minor_head"));
                chartOfAccount.setSubHead(rs.getString("sub_head"));
                chartOfAccount.setDetailHead(rs.getString("detail_head"));
                chartOfAccount.setChargeVoted(rs.getString("charged_voted"));
                chartOfAccount.setObjectHead(rs.getString("object_head"));
                chartOfAccount.setHiddenddoCode(rs.getString("ddo_code"));
                chartOfAccount.setHiddeptName(rs.getString("department_name"));
                chartOfAccount.setChatId(rs.getString("chart_acc_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return chartOfAccount;
    }

    @Override
    public void updateChartofAccount(ChartOfAccount chartOfAccount) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE g_chart_of_account SET demand_number=?, major_head=?, submajor_head=?, minor_head=?, sub_head=?,detail_head=?, charged_voted=?, object_head=? WHERE ddo_code=? and chart_acc_id=?");
            //update

            pst.setString(1, chartOfAccount.getDemandno());
            pst.setString(2, chartOfAccount.getMajorHead());
            pst.setString(3, chartOfAccount.getSubMajorHead());
            pst.setString(4, chartOfAccount.getMinorHead());
            pst.setString(5, chartOfAccount.getSubHead());
            pst.setString(6, chartOfAccount.getDetailHead());
            pst.setString(7, chartOfAccount.getChargeVoted());
            pst.setString(8, chartOfAccount.getObjectHead());
            //where condition
            pst.setString(9, chartOfAccount.getHiddenddoCode());
            pst.setInt(10, Integer.parseInt(chartOfAccount.getChatId()));
            n = pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveMajorHead(ChartOfAccount chartOfAccount) {
        PreparedStatement pst = null;
        Connection con = null;
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO G_major_head(major_head ,description, if_active) VALUES(?,?,?)");

            pst.setString(1, chartOfAccount.getModalmajorHead());
            pst.setString(2, chartOfAccount.getMajorheadDesc());
            pst.setString(3, "Y");
            pst.executeUpdate();

            pst = con.prepareStatement("INSERT INTO g_chart_of_account ( demand_number, major_head, ddo_code) VALUES(?,?,?)");
            pst.setString(1, chartOfAccount.getDemandno());
            pst.setString(2, chartOfAccount.getModalmajorHead());
            pst.setString(3, chartOfAccount.getHiddenddoCode());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveSubMajorHead(ChartOfAccount chartOfAccount) {
        PreparedStatement pst = null;
        Connection con = null;
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO G_sub_major_head(major_head ,submajor_head,description) VALUES(?,?,?)");

            pst.setString(1, chartOfAccount.getMajorHead());
            pst.setString(2, chartOfAccount.getModalsubmajorHead());
            pst.setString(3, chartOfAccount.getSubmajorheadDesc());
            pst.executeUpdate();

            /*   pst = con.prepareStatement("UPDATE g_chart_of_account SET submajor_head=? where demand_number =? and major_head=? and  ddo_code=?");
             pst.setString(1, chartOfAccount.getModalsubmajorHead());
             pst.setString(2, chartOfAccount.getDemandno());
             pst.setString(3, chartOfAccount.getMajorHead());
             pst.setString(4, chartOfAccount.getHiddenddoCode());                       
             pst.executeUpdate(); */
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void saveMinorHead(ChartOfAccount chartOfAccount) {
        PreparedStatement pst = null;
        Connection con = null;
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO G_minor_head(major_head ,submajor_head,minor_head,description) VALUES(?,?,?,?)");

            pst.setString(1, chartOfAccount.getMajorHead());
            pst.setString(2, chartOfAccount.getSubMajorHead());
            pst.setString(3, chartOfAccount.getModalminorHead());
            pst.setString(4, chartOfAccount.getMinorheadDesc());
            pst.executeUpdate();

            /*  pst = con.prepareStatement("UPDATE g_chart_of_account SET minor_head=? where demand_number=? and major_head=? and submajor_head=? and ddo_code=?");
             pst.setString(1, chartOfAccount.getModalminorHead());
             pst.setString(2, chartOfAccount.getDemandno());
             pst.setString(3, chartOfAccount.getMajorHead());
             pst.setString(4, chartOfAccount.getSubMajorHead());
             pst.setString(5, chartOfAccount.getHiddenddoCode());                   
             pst.executeUpdate();       */
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void saveSubHead(ChartOfAccount chartOfAccount) {
        PreparedStatement pst = null;
        Connection con = null;
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO G_sub_head(hoa_level4,description) VALUES(?,?)");

            pst.setString(1, chartOfAccount.getModalsubHead());
            pst.setString(2, chartOfAccount.getSubheadDesc());
            pst.executeUpdate();

            pst = con.prepareStatement("INSERT INTO g_chart_of_account (sub_head,demand_number,major_head,submajor_head,minor_head,ddo_code) VALUES(?,?,?,?,?,?)");
            pst.setString(1, chartOfAccount.getModalsubHead());
            pst.setString(2, chartOfAccount.getDemandno());
            pst.setString(3, chartOfAccount.getMajorHead());
            pst.setString(4, chartOfAccount.getSubMajorHead());
            pst.setString(5, chartOfAccount.getMinorHead());
            pst.setString(6, chartOfAccount.getHiddenddoCode());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void saveDetailHead(ChartOfAccount chartOfAccount) {
        PreparedStatement pst = null;
        Connection con = null;
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO G_detail_head(detail_head,description) VALUES(?,?)");

            pst.setString(1, chartOfAccount.getModaldetailHead());
            pst.setString(2, chartOfAccount.getSubheadDesc());
            pst.executeUpdate();

            pst = con.prepareStatement("INSERT INTO g_chart_of_account (detail_head, demand_number,major_head, submajor_head, minor_head,sub_head, ddo_code) VALUES(?,?,?,?,?,?,?)");
            pst.setString(1, chartOfAccount.getModaldetailHead());
            pst.setString(2, chartOfAccount.getDemandno());
            pst.setString(3, chartOfAccount.getMajorHead());
            pst.setString(4, chartOfAccount.getSubMajorHead());
            pst.setString(5, chartOfAccount.getMinorHead());
            pst.setString(6, chartOfAccount.getSubHead());
            pst.setString(7, chartOfAccount.getHiddenddoCode());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void saveObjectHead(ChartOfAccount chartOfAccount) {
        PreparedStatement pst = null;
        Connection con = null;
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO G_object_head(hoa_level5,hoa_level6,description) VALUES(?,?,?)");

            pst.setString(1, chartOfAccount.getDetailHead());
            pst.setString(2, chartOfAccount.getModalobjectHead());
            pst.setString(3, chartOfAccount.getObjectheadDesc());
            pst.executeUpdate();
            if (chartOfAccount.getModalobjectHead() != null && !chartOfAccount.getModalobjectHead().equals("")) {
                pst = con.prepareStatement("INSERT INTO g_chart_of_account (object_head, demand_number , major_head, submajor_head, minor_head, sub_head, detail_head, ddo_code) VALUES(?,?,?,?,?,?,?,?)");
                pst.setString(1, chartOfAccount.getModalobjectHead());
                pst.setString(2, chartOfAccount.getDemandno());
                pst.setString(3, chartOfAccount.getMajorHead());
                pst.setString(4, chartOfAccount.getSubMajorHead());
                pst.setString(5, chartOfAccount.getMinorHead());
                pst.setString(6, chartOfAccount.getSubHead());
                pst.setString(7, chartOfAccount.getDetailHead());
                pst.setString(8, chartOfAccount.getHiddenddoCode());
                pst.executeUpdate();
            } else {
                pst = con.prepareStatement("UPDATE g_chart_of_account SET object_head=? where demand_number =? and major_head=? and submajor_head=? and minor_head=? and sub_head=? and detail_head=? and ddo_code=?");
                pst.setString(1, chartOfAccount.getModalobjectHead());
                pst.setString(2, chartOfAccount.getDemandno());
                pst.setString(3, chartOfAccount.getMajorHead());
                pst.setString(4, chartOfAccount.getSubMajorHead());
                pst.setString(5, chartOfAccount.getMinorHead());
                pst.setString(6, chartOfAccount.getSubHead());
                pst.setString(7, chartOfAccount.getDetailHead());
                pst.setString(8, chartOfAccount.getHiddenddoCode());
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public ArrayList getSubMajorHeadListmaster(String majorhead) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList subMajorHeadList = new ArrayList();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT DISTINCT submajor_head FROM G_sub_major_head WHERE MAJOR_HEAD=?");
            pstmt.setString(1, majorhead);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("submajor_head"));
                so.setLabel(rs.getString("submajor_head"));
                subMajorHeadList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return subMajorHeadList;
    }

    @Override
    public ArrayList getMinorHeadListmaster(String majorHead, String submajorhead) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList minorHeadList = new ArrayList();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT DISTINCT MINOR_HEAD FROM G_minor_head WHERE SUBMAJOR_HEAD=? AND MAJOR_HEAD=?");
            pstmt.setString(1, submajorhead);
            pstmt.setString(2, majorHead);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("MINOR_HEAD"));
                so.setLabel(rs.getString("MINOR_HEAD"));
                minorHeadList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return minorHeadList;
    }

    @Override
    public ArrayList getPOfficeBillGroupList(String subOffcode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList billGroupList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM BILL_GROUP_MASTER WHERE OFF_CODE=(SELECT P_OFF_CODE FROM G_OFFICE WHERE IS_DDO='B' AND LVL='20' AND OFF_CODE=?) \n"
                    + "and (is_deleted ='N' or is_deleted  is null) order by description");
            pstmt.setString(1, subOffcode);
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
                billGroup.setPayhead(rs.getString("pay_head"));
                billGroup.setPlan(rs.getString("plan"));
                billGroup.setSector(rs.getString("sector"));
                billGroup.setShowInAquitance(rs.getString("show_in_aquitance"));
                billGroup.setSubmitToTreasury(rs.getString("submit_to_treasury"));
                billGroupList.add(billGroup);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billGroupList;
    }

}
