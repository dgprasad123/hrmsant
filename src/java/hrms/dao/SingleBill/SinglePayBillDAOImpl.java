/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.SingleBill;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.SingleBill.SinglePayBillForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author prashant
 */
public class SinglePayBillDAOImpl implements SinglePayBillDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getDDODetailsList(String code, String userType) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List ddoList = new ArrayList();
        SelectOption so = null;
        String ddoListQry = "";
        try {
            con = dataSource.getConnection();
            if (code != null && !code.equals("")) {
                if (code.equals("9999")) {
                    ddoListQry = "select off_code, ddo_code from g_office where off_status='F' AND is_ddo='Y' AND ONLINE_BILL_SUBMISSION='Y' group by off_code,ddo_code order by off_code,ddo_code";
                    pstmt = con.prepareStatement(ddoListQry);
                } else {
                    if (userType.equalsIgnoreCase("D")) {
                        ddoListQry = "select off_code, ddo_code from g_office where dist_code=? and is_ddo=? order by ddo_code";
                    } else {
                        ddoListQry = "select off_code, ddo_code from g_office where tr_code=? and is_ddo=? order by ddo_code";
                    }
                    pstmt = con.prepareStatement(ddoListQry);
                    pstmt.setString(1, code);
                    pstmt.setString(2, "Y");
                }
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {

                so = new SelectOption();
                so.setLabel(rs.getString("ddo_code"));
                so.setValue(rs.getString("off_code"));
                ddoList.add(so);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ddoList;
    }

    @Override
    public List getDdoWiseBillList(String billType, String offCode, String aqYear, String aqMonth, String userType, String arrearType) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List billList = new ArrayList();
        SinglePayBillForm spbBean = null;
        String billListQry = "";
        try {
            con = dataSource.getConnection();
            if (userType.equalsIgnoreCase("D")) {
                billListQry = "select bill_no,bill_desc,bill_group_desc,bill_status_id,bill_type,type_of_bill, token_no, token_date, vch_no, vch_date from bill_mast where "
                        + "ddo_code = ? and aq_year = ? and aq_month = ? and type_of_bill = ? order by off_code ";
            } else {
                billListQry = "select bill_no,bill_desc,bill_group_desc,bill_status_id,bill_type,type_of_bill, token_no, token_date, vch_no, vch_date from bill_mast where "
                        + "ddo_code = ? and aq_year = ? and aq_month = ? and type_of_bill = ? and bill_status_id > 3 order by off_code";
            }

            pstmt = con.prepareStatement(billListQry);
            pstmt.setString(1, offCode);
            pstmt.setInt(2, Integer.parseInt(aqYear));
            pstmt.setInt(3, Integer.parseInt(aqMonth));
            if (billType != null && billType.equals("ARREAR")) {
                pstmt.setString(4, arrearType);
            } else {
                pstmt.setString(4, billType);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {

                spbBean = new SinglePayBillForm();
                spbBean.setTokenNo(rs.getString("token_no"));
                spbBean.setTokenDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("token_date")));
                spbBean.setVoucherNo(rs.getString("vch_no"));
                spbBean.setVoucherDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("vch_date")));
                spbBean.setBillNo(rs.getString("bill_no"));
                spbBean.setBillDesc(rs.getString("bill_desc"));
                spbBean.setBillGroupDesc(rs.getString("bill_group_desc"));
                spbBean.setBillStatusId(rs.getString("bill_status_id"));
                spbBean.setBillType(rs.getString("bill_type"));
                spbBean.setTypeOfBill(rs.getString("type_of_bill"));

                billList.add(spbBean);
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
    public List getAGWiseTreasuryCodeList() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List trlist = new ArrayList();
        SelectOption so = null;
        try {
            con = dataSource.getConnection();

            String sql = "select ag_treasury_code from g_treasury where ag_treasury_code is not null group by ag_treasury_code order by ag_treasury_code";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("ag_treasury_code"));
                so.setValue(rs.getString("ag_treasury_code"));
                trlist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return trlist;
    }

    @Override
    public List getAGWiseBillList(String trcode, String majorhead, String aqYear, String aqMonth) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List billList = new ArrayList();
        SinglePayBillForm spbBean = null;
        try {
            con = dataSource.getConnection();

            String billListQry = "select bill_no,bill_desc,bill_group_desc,bill_status_id,bill_type,type_of_bill, vch_no, vch_date from bill_mast where"
                    + " tr_code in (select tr_code from g_treasury where ag_treasury_code=?) and aq_year = ? and aq_month = ? and major_head=? and bill_status_id = 7 order by vch_no::INTEGER";
            pst = con.prepareStatement(billListQry);
            pst.setString(1, trcode);
            pst.setInt(2, Integer.parseInt(aqYear));
            pst.setInt(3, Integer.parseInt(aqMonth));
            pst.setString(4, majorhead);
            rs = pst.executeQuery();
            while (rs.next()) {

                spbBean = new SinglePayBillForm();

                spbBean.setBillNo(rs.getString("bill_no"));
                spbBean.setBillDesc(rs.getString("bill_desc"));
                spbBean.setBillGroupDesc(rs.getString("bill_group_desc"));
                spbBean.setBillStatusId(rs.getString("bill_status_id"));
                spbBean.setBillType(rs.getString("bill_type"));
                spbBean.setTypeOfBill(rs.getString("type_of_bill"));

                spbBean.setVoucherDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("vch_date")));

                spbBean.setVoucherNo(rs.getString("vch_no"));
                billList.add(spbBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billList;
    }

    @Override
    public List getOnlineBillSubmittedDDOs(String distCode, String userType) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List ddoList = new ArrayList();
        SelectOption so = null;
        String ddoListQry = "";
        try {
            con = dataSource.getConnection();
            if (distCode != null && !distCode.equals("")) {
                if (distCode.equals("9999")) {
                    ddoListQry = "select off_code, ddo_code from g_office where off_status='F' AND is_ddo='Y' AND ONLINE_BILL_SUBMISSION='Y' group by off_code, ddo_code order by off_code, ddo_code";
                    pstmt = con.prepareStatement(ddoListQry);
                } else {
                    if (userType.equalsIgnoreCase("D")) {
                        ddoListQry = "select off_code, ddo_code from g_office where dist_code=? and is_ddo=? order by ddo_code";
                    } else {
                        ddoListQry = "select off_code, ddo_code from g_office where tr_code=? and is_ddo=? order by ddo_code";
                    }
                    pstmt = con.prepareStatement(ddoListQry);
                    pstmt.setString(1, distCode);
                    pstmt.setString(2, "Y");
                }
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {

                so = new SelectOption();
                so.setLabel(rs.getString("ddo_code"));
                so.setValue(rs.getString("ddo_code"));
                ddoList.add(so);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ddoList;
    }
}
