package hrms.dao.payroll.aqmast;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.employee.PayComponent;
import hrms.model.payroll.aqmast.AqMastBean;
import hrms.model.payroll.aqmast.AqmastModel;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

public class AqmastDAOImpl implements AqmastDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String saveAqmastdata(AqmastModel aqmast) {
        Connection con = null;
        PreparedStatement pstmt = null;
        String aqslNo = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        try {
            con = dataSource.getConnection();
            aqslNo = aqmast.getAqGroup() + "_" + aqmast.getPayMonth() + "_" + aqmast.getPayYear() + "_" + aqmast.getSlno();
            pstmt = con.prepareStatement("insert into aq_mast(aqsl_no,aq_group,aq_group_desc ,p_month , p_year , pay_type ,aq_date ,aq_month ,aq_year ,aq_type ,ref_ord ,ref_date ,aq_day ,pay_day , "
                    + "major_head ,major_head_desc ,sub_major_head ,sub_major_head_desc ,minor_head ,minor_head_desc ,sub_minor_head1,sub_minor_head1_desc , "
                    + "sub_minor_head2 ,sub_minor_head2_desc ,sub_minor_head3 ,sub_minor_head3_desc ,plan ,sector ,alt_unit ,off_code ,off_ddo ,sec_sl_no , "
                    + " section ,post_sl_no ,cur_desg ,cur_grade ,cur_level ,gazetted ,pay_scale ,mon_basic ,emp_code ,emp_name ,gpf_type ,gpf_acc_no,cur_basic,bill_no, "
                    + " bill_date ,bank_name,branch_name ,bank_acc_no ,default_bank ,remark ,spc_ord_no ,spc_ord_date,acct_type ,cur_spc ,emp_type ,dep_code) values(?,?,?,?,?,?,?,?,?,?,?,"
                    + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pstmt.setString(1, aqslNo);
            pstmt.setBigDecimal(2, aqmast.getAqGroup());
            pstmt.setString(3, aqmast.getAqGroupDesc());
            pstmt.setInt(4, aqmast.getPayMonth());
            pstmt.setInt(5, aqmast.getPayYear());
            pstmt.setString(6, aqmast.getPayType());
            pstmt.setDate(7, new java.sql.Date(aqmast.getAqDate().getTime()));
            pstmt.setInt(8, aqmast.getAqMonth());
            pstmt.setInt(9, aqmast.getAqYear());
            pstmt.setString(10, aqmast.getAqType());
            pstmt.setString(11, aqmast.getRefOrder());
            if (aqmast.getRefDate() != null) {
                pstmt.setDate(12, new java.sql.Date(aqmast.getRefDate().getTime()));
            } else {
                pstmt.setDate(12, null);
            }
            pstmt.setInt(13, aqmast.getAqDay());
            pstmt.setInt(14, aqmast.getPayDay());
            pstmt.setString(15, aqmast.getMajorHead());
            pstmt.setString(16, aqmast.getMajorHeadDesc());
            pstmt.setString(17, aqmast.getSubMajorHead());
            pstmt.setString(18, aqmast.getSubMajorHeadDesc());
            pstmt.setString(19, aqmast.getMinorHead());
            pstmt.setString(20, aqmast.getMinorHeadDesc());
            pstmt.setString(21, aqmast.getSubMinorHead1());
            pstmt.setString(22, aqmast.getSubMinorHeadDesc1());
            pstmt.setString(23, aqmast.getSubMinorHead2());
            pstmt.setString(24, aqmast.getSubMinorHeadDesc2());
            pstmt.setString(25, aqmast.getSubMinorHead3());
            pstmt.setString(26, aqmast.getSubMinorHeadDesc3());
            pstmt.setString(27, aqmast.getPlan());
            pstmt.setString(28, aqmast.getSector());
            pstmt.setString(29, aqmast.getAltUnit());
            pstmt.setString(30, aqmast.getOffCode());
            pstmt.setString(31, aqmast.getOffDdo());
            pstmt.setInt(32, aqmast.getSecSlNo());
            pstmt.setString(33, aqmast.getSection());
            pstmt.setInt(34, aqmast.getPostSlNo());
            pstmt.setString(35, aqmast.getCurDesg());
            pstmt.setString(36, aqmast.getCurGrade());
            pstmt.setString(37, aqmast.getCurLevel());
            pstmt.setString(38, aqmast.getGazetted());
            pstmt.setString(39, aqmast.getPayScale());
            pstmt.setLong(40, aqmast.getMonBasic());
            pstmt.setString(41, aqmast.getEmpCode());
            pstmt.setString(42, aqmast.getEmpName());
            pstmt.setString(43, aqmast.getGpfType());
            pstmt.setString(44, aqmast.getGpfAccNo());
            pstmt.setLong(45, aqmast.getCurBasic());
            pstmt.setInt(46, aqmast.getBillNo());
            if (aqmast.getBillDate() != null) {
                pstmt.setDate(47, new java.sql.Date(aqmast.getBillDate().getTime()));
            } else {
                pstmt.setDate(47, null);
            }
            pstmt.setString(48, aqmast.getBankName());
            pstmt.setString(49, aqmast.getBranchName());
            pstmt.setString(50, aqmast.getBankAccNo());
            pstmt.setInt(51, aqmast.getDefaultBank());
            pstmt.setString(52, aqmast.getRemark());
            pstmt.setString(53, aqmast.getSpcOrdNo());
            if (aqmast.getSpcOrdDate() != null) {
                pstmt.setDate(54, new java.sql.Date(aqmast.getSpcOrdDate().getTime()));
            } else {
                pstmt.setDate(54, null);
            }
            pstmt.setString(55, aqmast.getAcctType());
            pstmt.setString(56, aqmast.getCurSpc());
            pstmt.setString(57, aqmast.getEmpType());
            pstmt.setString(58, StringUtils.defaultIfEmpty(aqmast.getDeptCode(), "02"));
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aqslNo;
    }

    @Override
    public void updateAqmastdata(AqmastModel aqmast) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE AQ_MAST SET cur_basic=?, mon_basic=?, pay_day=?, bank_acc_no=?, ifsc_code=?, acct_type=?, gpf_acc_no=?, pay_scale=?, dep_code=?, pay_commission=?,gpf_type=? WHERE aqsl_no=?");
            pstmt.setLong(1, aqmast.getCurBasic());
            pstmt.setLong(2, aqmast.getMonBasic());
            pstmt.setInt(3, aqmast.getPayDay());
            pstmt.setString(4, aqmast.getBankAccNo());
            pstmt.setString(5, aqmast.getIfscCode());
            pstmt.setString(6, aqmast.getAcctType());
            pstmt.setString(7, aqmast.getGpfAccNo());
            pstmt.setString(8, aqmast.getPayScale());
            pstmt.setString(9, aqmast.getDeptCode());
            if (aqmast.getPayCommission() != null && !aqmast.getPayCommission().equals("")) {
                pstmt.setInt(10, Integer.parseInt(aqmast.getPayCommission()));
            } else {
                pstmt.setInt(10, 0);
            }
            pstmt.setString(11, aqmast.getGpfType());
            pstmt.setString(12, aqmast.getAqSlNo());

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public AqmastModel getAqmastDetail(String aqslno) {
        AqmastModel aqmast = new AqmastModel();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select g_cadre.cadre_type,aqsl_no,aq_group,aq_mast.aq_group_desc ,p_month , p_year , pay_type ,aq_date ,aq_mast.aq_month ,aq_mast.aq_year ,aq_type ,ref_ord ,ref_date ,aq_day ,pay_day , "
                    + " aq_mast.major_head ,aq_mast.major_head_desc ,aq_mast.sub_major_head ,aq_mast.sub_major_head_desc ,aq_mast.minor_head ,aq_mast.minor_head_desc ,aq_mast.sub_minor_head1,aq_mast.sub_minor_head1_desc , "
                    + " aq_mast.sub_minor_head2 ,aq_mast.sub_minor_head2_desc ,aq_mast.sub_minor_head3 ,aq_mast.sub_minor_head3_desc ,aq_mast.plan ,aq_mast.sector ,alt_unit ,aq_mast.off_code ,aq_mast.off_ddo ,sec_sl_no , "
                    + " section ,post_sl_no ,cur_desg ,cur_grade ,cur_level ,gazetted ,pay_scale ,mon_basic ,emp_code ,emp_name ,aq_mast.gpf_type ,gpf_no,cur_basic,aq_mast.bill_no, "
                    + " aq_mast.bill_date ,bank_name,branch_name ,aq_mast.bank_acc_no ,default_bank ,remark ,spc_ord_no ,spc_ord_date,emp_mast.acct_type ,aq_mast.cur_spc ,aq_mast.emp_type ,aq_mast.dep_code, demand_no, if_maintenance_deduct from  aq_mast "
                    + " inner join bill_mast on aq_mast.bill_no=bill_mast.bill_no  "
                    + " inner join emp_mast on aq_mast.emp_code=emp_mast.emp_id "
                    + " left outer join g_cadre on emp_mast.cur_cadre_code=g_cadre.cadre_code "
                    + " where aqsl_no=?");
            pstmt.setString(1, aqslno);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                //aqmast = new AqmastModel();
                aqmast.setAqSlNo(aqslno);
                aqmast.setAqGroup(rs.getBigDecimal("aq_group"));
                aqmast.setAqGroupDesc(rs.getString("aq_group_desc"));
                aqmast.setPayMonth(rs.getInt("p_month"));
                aqmast.setPayYear(rs.getInt("p_year"));
                aqmast.setPayType(rs.getString("pay_type"));
                aqmast.setAqDate(rs.getDate("aq_date"));
                aqmast.setAqMonth(rs.getInt("aq_month"));
                aqmast.setAqYear(rs.getInt("aq_year"));
                aqmast.setAqType(rs.getString("aq_type"));
                aqmast.setRefOrder(rs.getString("ref_ord"));
                aqmast.setRefDate(rs.getDate("ref_date"));
                aqmast.setAqDay(rs.getInt("aq_day"));
                aqmast.setPayDay(rs.getInt("pay_day"));
                aqmast.setMajorHead(rs.getString("major_head"));
                aqmast.setMajorHeadDesc(rs.getString("major_head_desc"));
                aqmast.setSubMajorHead(rs.getString("sub_major_head"));
                aqmast.setSubMajorHeadDesc(rs.getString("sub_major_head_desc"));
                aqmast.setMinorHead(rs.getString("minor_head"));
                aqmast.setMinorHeadDesc(rs.getString("minor_head_desc"));
                aqmast.setSubMinorHead1(rs.getString("sub_minor_head1"));
                aqmast.setSubMinorHeadDesc1(rs.getString("sub_minor_head1_desc"));
                aqmast.setSubMinorHead2(rs.getString("sub_minor_head2"));
                aqmast.setSubMinorHeadDesc2(rs.getString("sub_minor_head2_desc"));
                aqmast.setSubMinorHead3(rs.getString("sub_minor_head3"));
                aqmast.setSubMinorHeadDesc3(rs.getString("sub_minor_head3_desc"));
                aqmast.setPlan(rs.getString("plan"));
                aqmast.setSector(rs.getString("sector"));
                aqmast.setAltUnit(rs.getString("alt_unit"));
                aqmast.setOffCode(rs.getString("off_code"));
                aqmast.setOffDdo(rs.getString("off_ddo"));
                aqmast.setSecSlNo(rs.getInt("sec_sl_no"));
                aqmast.setSection(rs.getString("section"));
                aqmast.setPostSlNo(rs.getInt("post_sl_no"));
                aqmast.setCurDesg(rs.getString("cur_desg"));
                aqmast.setCadreType(rs.getString("cadre_type"));
                aqmast.setCurGrade(rs.getString("cur_grade"));
                aqmast.setCurLevel(rs.getString("cur_level"));
                aqmast.setGazetted(rs.getString("gazetted"));
                aqmast.setPayScale(rs.getString("pay_scale"));
                aqmast.setMonBasic(rs.getInt("mon_basic"));
                aqmast.setEmpCode(rs.getString("emp_code"));
                aqmast.setGpfType(rs.getString("gpf_type"));
                aqmast.setGpfAccNo(rs.getString("gpf_no"));
                aqmast.setCurBasic(rs.getInt("cur_basic"));
                aqmast.setBillNo(rs.getInt("bill_no"));
                aqmast.setBillDate(rs.getDate("bill_date"));
                aqmast.setBankName(rs.getString("bank_name"));
                aqmast.setBranchName(rs.getString("branch_name"));
                aqmast.setBankAccNo(rs.getString("bank_acc_no"));
                aqmast.setDefaultBank(rs.getInt("default_bank"));
                aqmast.setRemark(rs.getString("remark"));
                aqmast.setSpcOrdNo(rs.getString("spc_ord_no"));
                aqmast.setSpcOrdDate(rs.getDate("spc_ord_date"));
                aqmast.setAcctType(rs.getString("acct_type"));
                aqmast.setCurSpc(rs.getString("cur_spc"));
                aqmast.setEmpType(rs.getString("emp_type"));
                aqmast.setDeptCode(rs.getString("dep_code"));
                aqmast.setDemandNumber(rs.getString("demand_no"));
                aqmast.setIfMaintenanceDeduct(rs.getString("if_maintenance_deduct"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aqmast;
    }

    @Override
    public int deleteAqmastData(String aqslno) {
        int n = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("delete from aq_mast where aqsl_no=?");
            pstmt.setString(1, aqslno);
            n = pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public List getAqmastList(int billno) {
        List aqList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        AqMastBean aqmBean = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select aqsl_no,aq_group,aq_group_desc ,p_month , p_year , pay_type ,aq_date ,aq_month ,aq_year ,aq_type ,ref_ord ,ref_date ,aq_day ,pay_day , "
                    + "major_head ,major_head_desc ,sub_major_head ,sub_major_head_desc ,minor_head ,minor_head_desc ,sub_minor_head1,sub_minor_head1_desc , "
                    + "sub_minor_head2 ,sub_minor_head2_desc ,sub_minor_head3 ,sub_minor_head3_desc ,plan ,sector ,alt_unit ,off_code ,off_ddo ,sec_sl_no , "
                    + " section ,post_sl_no ,cur_desg ,cur_grade ,cur_level ,gazetted ,pay_scale ,mon_basic ,emp_code ,emp_name ,gpf_type ,gpf_acc_no,cur_basic,bill_no, "
                    + " bill_date ,bank_name,branch_name ,bank_acc_no ,default_bank ,remark ,spc_ord_no ,spc_ord_date,acct_type ,cur_spc ,emp_type ,dep_code from "
                    + "  aq_mast where bill_no=?");
            pstmt.setInt(1, billno);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                aqmBean = new AqMastBean();
                aqmBean.setAqSlNo(rs.getString("aqsl_no"));
                aqmBean.setAqGroup(rs.getInt("aq_group"));
                aqmBean.setAqGroupDesc(rs.getString("aq_group_desc"));
                aqmBean.setPayMonth(rs.getInt("p_month"));
                aqmBean.setPayYear(rs.getInt("p_year"));
                aqmBean.setPayType(rs.getString("pay_type"));
                aqmBean.setAqDate(CommonFunctions.getFormattedOutputDate3(rs.getDate("aq_date")));
                aqmBean.setAqMonth(rs.getInt("aq_month"));
                aqmBean.setAqYear(rs.getInt("aq_year"));
                aqmBean.setAqType(rs.getString("aq_type"));
                aqmBean.setRefOrder(rs.getString("ref_ord"));
                aqmBean.setRefDate(CommonFunctions.getFormattedOutputDate3(rs.getDate("ref_date")));
                aqmBean.setAqDay(rs.getInt("aq_day"));
                aqmBean.setPayDay(rs.getInt("pay_day"));
                aqmBean.setMajorHead(rs.getString("major_head"));
                aqmBean.setMajorHeadDesc(rs.getString("major_head_desc"));
                aqmBean.setSubMajorHead(rs.getString("sub_major_head"));
                aqmBean.setSubMajorHeadDesc(rs.getString("sub_major_head_desc"));
                aqmBean.setMinorHead(rs.getString("minor_head"));
                aqmBean.setMinorHeadDesc(rs.getString("minor_head_desc"));
                aqmBean.setSubMinorHead1(rs.getString("sub_minor_head1"));
                aqmBean.setSubMinorHeadDesc1(rs.getString("sub_minor_head1_desc"));
                aqmBean.setSubMinorHead2(rs.getString("sub_minor_head2"));
                aqmBean.setSubMinorHeadDesc2(rs.getString("sub_minor_head2_desc"));
                aqmBean.setSubMinorHead3(rs.getString("sub_minor_head3"));
                aqmBean.setSubMinorHeadDesc3(rs.getString("sub_minor_head3_desc"));
                aqmBean.setPlan(rs.getString("plan"));
                aqmBean.setSector(rs.getString("sector"));
                aqmBean.setAltUnit(rs.getString("alt_unit"));
                aqmBean.setOffCode(rs.getString("off_code"));
                aqmBean.setOffDdo(rs.getString("off_ddo"));
                aqmBean.setSecSlNo(rs.getInt("sec_sl_no"));
                aqmBean.setSection(rs.getString("section"));
                aqmBean.setPostSlNo(rs.getInt("post_sl_no"));
                aqmBean.setCurDesg(rs.getString("cur_desg"));
                aqmBean.setCurGrade(rs.getString("cur_grade"));
                aqmBean.setCurLevel(rs.getString("cur_level"));
                aqmBean.setGazetted(rs.getString("gazetted"));
                aqmBean.setPayScale(rs.getString("pay_scale"));
                aqmBean.setMonBasic(rs.getInt("mon_basic"));
                aqmBean.setEmpCode(rs.getString("emp_name"));
                aqmBean.setGpfType(rs.getString("gpf_type"));
                aqmBean.setGpfAccNo(rs.getString("gpf_acc_no"));
                aqmBean.setCurBasic(rs.getInt("cur_basic"));
                aqmBean.setBillNo(rs.getInt("bill_no"));
                aqmBean.setBillDate(CommonFunctions.getFormattedOutputDate3(rs.getDate("bill_date")));
                aqmBean.setBankName(rs.getString("bank_name"));
                aqmBean.setBranchName(rs.getString("branch_name"));
                aqmBean.setBankAccNo(rs.getString("bank_acc_no"));
                aqmBean.setDefaultBank(rs.getInt("default_bank"));
                aqmBean.setRemark(rs.getString("remark"));
                aqmBean.setSpcOrdNo(rs.getString("spc_ord_no"));
                aqmBean.setSpcOrdDate(CommonFunctions.getFormattedOutputDate3(rs.getDate("spc_ord_date")));
                aqmBean.setAcctType(rs.getString("acct_type"));
                aqmBean.setCurSpc(rs.getString("cur_spc"));
                aqmBean.setEmpType(rs.getString("emp_type"));
                aqmBean.setDeptCode(rs.getString("dep_code"));
                aqList.add(aqmBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aqList;
    }

    @Override
    public PayComponent getEmployeePayComponent(Date startDate, Date endDate, int daysInMonth) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PayComponent paycomp = new PayComponent();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return paycomp;
    }

    @Override
    public String getEmployeeType(int billNo, int aqMonth, int aqYear) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String empType = "";
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select emp_type from aq_mast where bill_no = ? and p_month = ? and p_year=? group by emp_type");
            pstmt.setInt(1, billNo);
            pstmt.setInt(2, aqMonth);
            pstmt.setInt(3, aqYear);
            rs = pstmt.executeQuery();
            if (rs.next()) {

                empType = rs.getString("emp_type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empType;
    }

    @Override
    public void addMaintenanceData(AqmastModel aqMastModel) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pst = null;
        PreparedStatement pst2 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        int netAmt = 0;
        int benfAmt = 0;
        int totAmt = 0;
        try {
            con = dataSource.getConnection();

            pst2 = con.prepareStatement("delete from other_beneficiary where ref_emp_id=? and bill_ID=? ");
            pst2.setString(1, aqMastModel.getEmpCode());
            pst2.setInt(2, aqMastModel.getBillNo());
            pst2.executeUpdate();

            pst2 = con.prepareStatement("insert into other_beneficiary (bill_id, ref_emp_id, beneficiary_name, aq_month, aq_year, acct_type, acct_no ,ifsc_code, bank_name, branch_name, net_amt, ref_off_code, aqsl_no, rel_slno) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");

            pst = con.prepareStatement("select getempgrosstotal('" + aqMastModel.getAqSlNo() + "') gross, getempdedtotal('" + aqMastModel.getAqSlNo() + "') dedn, getemppvtdedtotal('" + aqMastModel.getAqSlNo() + "') pvtdedn ");
            rs2 = pst.executeQuery();
            if (rs2.next()) {
                netAmt = rs2.getInt("gross") - rs2.getInt("dedn");
            }

            pstmt = con.prepareStatement("SELECT BANK_ACC_NO, G_BRANCH.BANK_CODE, G_BRANCH.BRANCH_CODE, G_BRANCH.IFSC_CODE, AMOUNT_TYPE, FIXED_AMOUNT, FORMULA, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, SL_NO  FROM EMP_RELATION "
                    + " INNER JOIN G_BRANCH ON EMP_RELATION.BRANCH_CODE=G_BRANCH.BRANCH_CODE "
                    + " WHERE EMP_ID=? AND AMOUNT_TYPE IS NOT NULL");

            pstmt.setString(1, aqMastModel.getEmpCode());
            rs1 = pstmt.executeQuery();
            while (rs1.next()) {
                if (rs1.getString("AMOUNT_TYPE").equals("1")) {
                    int percentage = Integer.parseInt(rs1.getString("FORMULA"));
                    if (percentage == 10) {
                        benfAmt = new Long(Math.round(netAmt * 0.10)).intValue();
                    } else if (percentage == 15) {
                        benfAmt = new Long(Math.round(netAmt * 0.15)).intValue();
                    } else if (percentage == 20) {
                        benfAmt = new Long(Math.round(netAmt * 0.20)).intValue();
                    } else if (percentage == 25) {
                        benfAmt = new Long(Math.round(netAmt * 0.25)).intValue();
                    } else if (percentage == 30) {
                        benfAmt = new Long(Math.round(netAmt * 0.30)).intValue();
                    } else if (percentage == 35) {
                        benfAmt = new Long(Math.round(netAmt * 0.35)).intValue();
                    } else if (percentage == 40) {
                        benfAmt = new Long(Math.round(netAmt * 0.40)).intValue();
                    } else if (percentage == 45) {
                        benfAmt = new Long(Math.round(netAmt * 0.45)).intValue();
                    } else if (percentage == 50) {
                        benfAmt = new Long(Math.round(netAmt * 0.50)).intValue();
                    }

                } else if (rs1.getString("AMOUNT_TYPE").equals("0")) {
                    benfAmt = rs1.getInt("FIXED_AMOUNT");
                }

                totAmt = totAmt + benfAmt;

                pst2.setInt(1, aqMastModel.getBillNo());
                pst2.setString(2, aqMastModel.getEmpCode());
                pst2.setString(3, rs1.getString("EMPNAME"));
                pst2.setInt(4, aqMastModel.getAqMonth());
                pst2.setInt(5, aqMastModel.getAqYear());
                pst2.setString(6, "S");
                pst2.setString(7, rs1.getString("BANK_ACC_NO"));
                pst2.setString(8, rs1.getString("ifsc_code"));
                pst2.setString(9, rs1.getString("bank_code"));
                pst2.setString(10, rs1.getString("BRANCH_CODE"));
                pst2.setInt(11, benfAmt);
                pst2.setString(12, aqMastModel.getOffCode());
                pst2.setString(13, aqMastModel.getAqSlNo());
                pst2.setInt(14, rs1.getInt("SL_NO"));
                if (totAmt <= netAmt) {
                    pst2.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public boolean stopNpsDeduction(String empId, int billMonth, int billYear) {
        boolean hastobeStop = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String str = "";

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select dos  from emp_mast where emp_id =? ");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                str = CommonFunctions.getFormattedOutputDate1(rs.getDate("dos"));
            }
            if (str != null) {
                Date dt = new Date(str);

                Calendar billcal1 = Calendar.getInstance();
                billcal1.set(Calendar.YEAR, billYear);
                billcal1.set(Calendar.MONTH, billMonth);

                Calendar dosCal = Calendar.getInstance();
                dosCal.setTime(dt);

                int diffYear = dosCal.get(Calendar.YEAR) - billcal1.get(Calendar.YEAR);
                int diffMonth = diffYear * 12 + dosCal.get(Calendar.MONTH) - billcal1.get(Calendar.MONTH);

                if (diffMonth == 0) {
                    hastobeStop = true;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return hastobeStop;
    }

    @Override
    public boolean stopGpfDeduction(String empId, int billMonth, int billYear) {
        boolean hastobeStop = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String str = "";
        String cadretype = "";
        try {
            con = dataSource.getConnection();
            //pstmt = con.prepareStatement("select dos  from emp_mast where emp_id =? ");
            pstmt = con.prepareStatement("select dos,cur_cadre_code,cadre_type from emp_mast"
                    + " left outer join g_cadre on emp_mast.cur_cadre_code=g_cadre.cadre_code"
                    + " where emp_id=?");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                str = CommonFunctions.getFormattedOutputDate1(rs.getDate("dos"));
                cadretype = rs.getString("cadre_type");
            }
            if (str != null) {
                Date dt = new Date(str);

                Calendar billcal1 = Calendar.getInstance();
                billcal1.set(Calendar.YEAR, billYear);
                billcal1.set(Calendar.MONTH, billMonth);

                Calendar dosCal = Calendar.getInstance();
                dosCal.setTime(dt);

                int diffYear = dosCal.get(Calendar.YEAR) - billcal1.get(Calendar.YEAR);
                int diffMonth = diffYear * 12 + dosCal.get(Calendar.MONTH) - billcal1.get(Calendar.MONTH);

                /*addition of code for stop gpf foR AIS employee*/
                if (cadretype != null && cadretype.equals("AIS")) {
                    if (diffMonth <= 2) {
                        hastobeStop = true;
                    }
                } else if (diffMonth <= 3) {
                    hastobeStop = true;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return hastobeStop;
    }

    @Override
    public boolean stopSalaryForPayHeldUp(String empId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean stopSalary = false;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select * from emp_pay_heldup where emp_id=? and to_date is null ");
            ps.setString(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {
                stopSalary = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return stopSalary;
    }

    @Override
    public boolean stopSalaryForRetirement(String empId, int billMonth, int billYear) {

        boolean stoppayforretirement = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String str = "";

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select dos from emp_mast where emp_id=?");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                str = CommonFunctions.getFormattedOutputDate1(rs.getDate("dos"));
            }
            if (str != null) {
                Date dt = new Date(str);

                Calendar billcal1 = Calendar.getInstance();
                billcal1.set(Calendar.YEAR, billYear);
                billcal1.set(Calendar.MONTH, billMonth);

                Calendar dosCal = Calendar.getInstance();
                dosCal.setTime(dt);

                int diffYear = dosCal.get(Calendar.YEAR) - billcal1.get(Calendar.YEAR);
                int diffMonth = diffYear * 12 + dosCal.get(Calendar.MONTH) - billcal1.get(Calendar.MONTH);
                System.out.println("diffMonth is: " + diffMonth);
                if (diffMonth < 0) {
                    stoppayforretirement = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return stoppayforretirement;
    }

    @Override
    public List getBillgroupwiseOfficeMapped(String billgroupid, int billMonth, int billYear) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        List offcMappedList = new ArrayList();
        AqmastModel aqmast = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select paybill_task.task_id,t3.*,bill_mast.bill_no billmast_billno from\n"
                    + "(select bill_group_id,t1.off_code,t1.distname,t1.off_en,sum(noofEmp)cntemp,is_bill_verified,bill_no,getbillgrossamtDHE(bill_no,t1.off_code)Gross,\n"
                    + "getbilldedamtDHE(bill_no,t1.off_code)Ded,(getbillgrossamtDHE(bill_no,t1.off_code)-getbilldedamtDHE(bill_no,t1.off_code))Net from\n"
                    + "(select t2.*,aqm.noofEmp::integer,aqm.is_bill_verified,aqm.bill_no from\n"
                    + "(select distinct bill_group_id,g_office.off_code,off_en,getdistname(g_office.dist_code)distname from\n"
                    + "(select * from bill_section_mapping where bill_group_id=?)bsm\n"
                    + "inner join g_section on bsm.section_id=g_section.section_id\n"
                    + "inner join g_office on g_section.off_code=g_office.off_code\n"
                    + "inner join (select section_id,count(*)cntemp from section_post_mapping group by section_id)spm\n"
                    + "on spm.section_id=bsm.section_id)t2 \n"
                    + "left outer join (SELECT bill_no,off_code,AQ_GROUP,is_bill_verified,COUNT(*) noofEmp\n"
                    + "FROM AQ_MAST WHERE AQ_MONTH=? AND AQ_YEAR=? AND AQ_GROUP=? \n"
                    + "GROUP BY off_code,AQ_GROUP,is_bill_verified,bill_no)aqm\n"
                    + "on aqm.AQ_GROUP=t2.bill_group_id AND aqm.OFF_CODE=t2.OFF_CODE\n"
                    + "order by is_bill_verified desc)t1 \n"
                    + "group by t1.off_code,off_en,is_bill_verified,bill_no,distname,bill_group_id)t3\n"
                    + "left outer join paybill_task on paybill_task.bill_group_id=t3.bill_group_id and paybill_task.off_code=t3.off_code and \n"
                    + "paybill_task.aq_month=? and paybill_task.aq_year=?\n"
                    + "left outer join bill_mast on bill_mast.bill_group_id=t3.bill_group_id and bill_mast.aq_month=? and bill_mast.aq_year=?\n"
                    + "order by distname,off_code");
            pstmt.setBigDecimal(1, BigDecimal.valueOf(Double.parseDouble(billgroupid)));
            pstmt.setInt(2, billMonth);
            pstmt.setInt(3, billYear);
            pstmt.setBigDecimal(4, BigDecimal.valueOf(Double.parseDouble(billgroupid)));
            pstmt.setInt(5, billMonth);
            pstmt.setInt(6, billYear);
            pstmt.setInt(7, billMonth);
            pstmt.setInt(8, billYear);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                aqmast = new AqmastModel();
                aqmast.setBillNo(rs.getInt("billmast_billno"));
                aqmast.setOffCode(rs.getString("off_code"));
                aqmast.setOffDdo(rs.getString("off_en"));
                aqmast.setNoempMappedinbill(rs.getInt("cntemp"));
                if (rs.getString("is_bill_verified") != null && rs.getString("is_bill_verified").equals("Y")) {
                    aqmast.setIsbillVerified("Processed And Verified");
                } else if (rs.getString("is_bill_verified") != null && rs.getString("is_bill_verified").equals("N")) {
                    aqmast.setIsbillVerified("Processed But Not Verified");
                } else if (rs.getString("is_bill_verified") == null || rs.getString("is_bill_verified").equals("")) {
                    aqmast.setIsbillVerified("Not Processed");
                }
                aqmast.setOffDist(rs.getString("distname"));
                aqmast.setGrossAmt(rs.getInt("Gross"));
                aqmast.setDedAmt(rs.getInt("Ded"));
                aqmast.setNetAmt(rs.getInt("Net"));
                aqmast.setBillgroupId(billgroupid);
                aqmast.setTaskid(rs.getString("task_id"));

                offcMappedList.add(aqmast);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return offcMappedList;

    }
    
     @Override
    public boolean stopShowingDummyPranForRetiredEmp(String empId, int billMonth, int billYear) {
        boolean hasNotobeStop = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String str = "";
        int diffMonth=0;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select dos  from emp_mast where emp_id =? ");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                str = CommonFunctions.getFormattedOutputDate1(rs.getDate("dos"));
            }
            if (str != null) {
                Date dt = new Date(str);

                Calendar billcal1 = Calendar.getInstance();
                billcal1.set(Calendar.YEAR, billYear);
                billcal1.set(Calendar.MONTH, billMonth);

                Calendar dosCal = Calendar.getInstance();
                dosCal.setTime(dt);

                //int diffYear = dosCal.get(Calendar.YEAR) - billcal1.get(Calendar.YEAR);
                //int diffMonth = diffYear * 12 + dosCal.get(Calendar.MONTH) - billcal1.get(Calendar.MONTH);
                if ((dosCal.get(Calendar.YEAR) == billcal1.get(Calendar.YEAR)) && ((dosCal.get(Calendar.MONTH)-1) == billcal1.get(Calendar.MONTH))) {
                    diffMonth = 1;
                } 

                if (diffMonth == 1) {
                    hasNotobeStop = true;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return hasNotobeStop;
    }

}
