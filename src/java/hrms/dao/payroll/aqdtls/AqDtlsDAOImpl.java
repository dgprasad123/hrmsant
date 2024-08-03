package hrms.dao.payroll.aqdtls;

import hrms.common.DataBaseFunctions;
import hrms.model.payroll.aqdtls.AqDtlsModel;
import hrms.model.payroll.billbrowser.AqDtlsDedBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class AqDtlsDAOImpl implements AqDtlsDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int saveAqdtlsData(AqDtlsModel[] aqdtlsList, boolean stopSubscription, String aqtableName) {
        int n = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pst2 = null;
        try {
            con = dataSource.getConnection();

            pst2 = con.prepareStatement("update aq_mast set da_rate=? where aqsl_no=? ");

            pstmt = con.prepareStatement("insert into " + aqtableName + " (aq_group ,aqsl_no ,off_ddo,emp_code ,p_month ,p_year,aq_date ,aq_month ,aq_year ,aq_type ,ref_ord ,ref_date,sl_no,ad_code,ad_desc,ad_type, "
                    + "alt_unit,ded_type,ad_amt ,acc_no ,ref_desc ,ref_count,schedule ,now_dedn ,tot_rec_amt ,rep_col ,ad_ref_id ,bt_id,instalment_count,ad_amt_gc) values(?,?,?,?,?,?,?,?,?,?,?,"
                    + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            if (aqdtlsList != null) {
                for (int cnt = 0; cnt < aqdtlsList.length; cnt++) {
                    AqDtlsModel aqdtls = aqdtlsList[cnt];
                    pstmt.setBigDecimal(1, aqdtls.getAqGroup());
                    pstmt.setString(2, aqdtls.getAqSlNo());
                    pstmt.setString(3, aqdtls.getDdoOff());
                    pstmt.setString(4, aqdtls.getEmpCode());
                    pstmt.setInt(5, aqdtls.getPayMon());
                    pstmt.setInt(6, aqdtls.getPayYear());
                    if (aqdtls.getAqDate() != null) {
                        pstmt.setDate(7, new java.sql.Date(aqdtls.getAqDate().getTime()));
                    } else {
                        pstmt.setDate(7, null);
                    }
                    pstmt.setInt(8, aqdtls.getAqMonth());
                    pstmt.setInt(9, aqdtls.getAqYear());
                    pstmt.setString(10, aqdtls.getAqType());
                    pstmt.setString(11, aqdtls.getRefOrderNo());
                    if (aqdtls.getRefOrderDate() != null) {
                        pstmt.setDate(12, new java.sql.Date(aqdtls.getRefOrderDate().getTime()));
                    } else {
                        pstmt.setDate(12, null);
                    }
                    pstmt.setInt(13, aqdtls.getSlNo());
                    pstmt.setString(14, aqdtls.getAdCode());
                    pstmt.setString(15, aqdtls.getAdDesc());
                    pstmt.setString(16, aqdtls.getAdType());
                    pstmt.setString(17, aqdtls.getAltUnit());
                    pstmt.setString(18, aqdtls.getDedType());
                    if (aqdtls.getAdCode() != null && (aqdtls.getAdCode().equals("GPF") || aqdtls.getAdCode().equals("TPF"))) {

                        if (stopSubscription) {
                            pstmt.setLong(19, 0);
                        } else {
                            pstmt.setLong(19, aqdtls.getAdAmt());
                        }

                    } else if (aqdtls.getAdCode() != null && aqdtls.getAdCode().equals("CPF")) {
                        if (stopSubscription) {
                            pstmt.setLong(19, 0);
                        } else {
                            pstmt.setLong(19, aqdtls.getAdAmt());
                        }

                    } else {
                        pstmt.setLong(19, aqdtls.getAdAmt());
                    }
                    pstmt.setString(20, aqdtls.getAccNo());
                    pstmt.setString(21, aqdtls.getRefDesc());
                    pstmt.setInt(22, aqdtls.getRefCount());
                    pstmt.setString(23, aqdtls.getSchedule());
                    pstmt.setString(24, aqdtls.getNowDedn());
                    if (aqdtls.getAdCode() != null && aqdtls.getAdCode().equals("CPF")) {
                        if (stopSubscription) {
                            pstmt.setInt(25, 0);
                        } else {
                            pstmt.setInt(25, aqdtls.getTotRecAmt());
                        }
                    } else {
                        pstmt.setInt(25, aqdtls.getTotRecAmt());
                    }
                    pstmt.setInt(26, aqdtls.getRepCol());
                    pstmt.setString(27, aqdtls.getAdRefId());
                    pstmt.setString(28, aqdtls.getBtId());
                    pstmt.setInt(29, aqdtls.getInstalCount());
                    if (aqdtls.getAdAmtGc() != null && !aqdtls.getAdAmtGc().equals("")) {
                        pstmt.setInt(30, (int) Double.parseDouble(aqdtls.getAdAmtGc()));
                    } else {
                        pstmt.setInt(30, 0);
                    }
                    if (aqdtls.getAdType() != null && !aqdtls.getAdType().equals("") && aqdtls.getAdType().equalsIgnoreCase("D")) {
                        if (aqdtls.getAdAmt() > 0) {
                            n = pstmt.executeUpdate();
                        }

                    } else if (aqdtls.getAdType() != null && !aqdtls.getAdType().equals("") && aqdtls.getAdType().equalsIgnoreCase("A")) {

                        if (aqdtls.getBtId() != null && !aqdtls.getBtId().equals("") && (aqdtls.getBtId().equalsIgnoreCase("523") || aqdtls.getBtId().equalsIgnoreCase("000"))) {
                            if (aqdtls.getAdAmt() > 0) {

                                n = pstmt.executeUpdate();
                            }
                        } else {
                            n = pstmt.executeUpdate();
                        }

                    } else {
                        n = pstmt.executeUpdate();
                    }

                    if (aqdtls.getAdCode() != null && (aqdtls.getAdCode().equals("DA"))) {
                        pst2.setString(1, aqdtls.getDaformula());
                        pst2.setString(2, aqdtls.getAqSlNo());
                        pst2.execute();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public int deleteAqdtls(String aqslno, String aqDTLSTable) {
        int n = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("delete from " + aqDTLSTable + " where aqsl_no=?");
            pstmt.setString(1, aqslno);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public List getAqdtlsList(String aqslno) {
        List aqDtlsList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        AqDtlsModel aqdtls = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select aq_group ,aqsl_no ,off_ddo,emp_code ,p_month ,p_year,aq_date ,aq_month ,aq_year ,aq_type ,ref_ord ,ref_date,sl_no,ad_code,ad_desc,ad_type, "
                    + "alt_unit,ded_type,ad_amt ,acc_no ,ref_desc ,ref_count,schedule ,now_dedn ,tot_rec_amt ,rep_col ,ad_ref_id ,bt_id,instalment_count from aq_dtls where aqsl_no=?");
            pstmt.setString(1, aqslno);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                aqdtls = new AqDtlsModel();
                aqdtls.setAqGroup(rs.getBigDecimal("aq_group"));
                aqdtls.setDdoOff(rs.getString("off_ddo"));
                aqdtls.setEmpCode(rs.getString("emp_code"));
                aqdtls.setPayMon(rs.getInt("p_month"));
                aqdtls.setPayYear(rs.getInt("p_year"));
                aqdtls.setAqDate(rs.getDate("aq_date"));
                aqdtls.setAqMonth(rs.getInt("aq_month"));
                aqdtls.setAqYear(rs.getInt("aq_year"));
                aqdtls.setAqType(rs.getString("aq_type"));
                aqdtls.setRefOrderNo(rs.getString("ref_ord"));
                aqdtls.setRefOrderDate(rs.getDate("ref_date"));
                aqdtls.setSlNo(rs.getInt("sl_no"));
                aqdtls.setAdCode(rs.getString("ad_code"));
                aqdtls.setAdDesc(rs.getString("ad_desc"));
                aqdtls.setAdType(rs.getString("ad_type"));
                aqdtls.setAltUnit(rs.getString("alt_unit"));
                aqdtls.setDedType(rs.getString("ded_type"));
                aqdtls.setAdAmt(rs.getInt("ad_amt"));
                aqdtls.setAccNo(rs.getString("acc_no"));
                aqdtls.setRefDesc(rs.getString("ref_desc"));
                aqdtls.setRefCount(rs.getInt("ref_count"));
                aqdtls.setSchedule(rs.getString("schedule"));
                aqdtls.setNowDedn(rs.getString("now_dedn"));
                aqdtls.setTotRecAmt(rs.getInt("tot_rec_amt"));
                aqdtls.setRepCol(rs.getInt("rep_col"));
                aqdtls.setAdRefId(rs.getString("ad_ref_id"));
                aqdtls.setBtId(rs.getString("bt_id"));
                aqdtls.setInstalCount(rs.getInt("instalment_count"));
                aqDtlsList.add(aqdtls);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aqDtlsList;
    }

    @Override
    public AqDtlsModel getAqdtlsData(String aqslno) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int updateAqDtlsData(String tableName, AqDtlsDedBean aqDtlsDedBean) {
        Connection con = null;
        PreparedStatement ps = null;
        int n = 0;
        try {

            con = dataSource.getConnection();
            if (aqDtlsDedBean.getAdType() != null && aqDtlsDedBean.getAdType().equals("D")) {
                if ((aqDtlsDedBean.getAdRefId() != null) && !aqDtlsDedBean.getAdRefId().equals("")) {
                    if (aqDtlsDedBean.getDedType() != null && aqDtlsDedBean.getDedType().equals("L")) {
                        if (aqDtlsDedBean.getNowDedn() != null && !aqDtlsDedBean.getNowDedn().equals("")) {
                            ps = con.prepareStatement("UPDATE " + tableName + " SET ad_amt=?,instalment_count=?,ref_desc=?,tot_rec_amt=?,ref_count=? WHERE aqsl_no=? and ad_code=? and now_dedn=?  and ad_ref_id=? and ad_type=? ");
                            ps.setDouble(1, Double.parseDouble(aqDtlsDedBean.getDedAmt()));
                            if (aqDtlsDedBean.getDedInstalCount() != null && !aqDtlsDedBean.getDedInstalCount().equals("")) {
                                ps.setDouble(2, Double.parseDouble(aqDtlsDedBean.getDedInstalCount()));
                            } else {
                                ps.setInt(2, 0);
                            }
                            ps.setString(3, aqDtlsDedBean.getRefDesc());
                            if (aqDtlsDedBean.getTotRecAmt() != null && !aqDtlsDedBean.getTotRecAmt().equals("")) {
                                ps.setDouble(4, Double.parseDouble(aqDtlsDedBean.getTotRecAmt()));
                            } else {
                                ps.setInt(4, 0);
                            }
                            if (aqDtlsDedBean.getDedInstalNo() != null && !aqDtlsDedBean.getDedInstalNo().equals("")) {
                                ps.setInt(5, Integer.parseInt(aqDtlsDedBean.getDedInstalNo()));
                            } else {
                                ps.setInt(5, 0);
                            }
                            ps.setString(6, aqDtlsDedBean.getAqslNo());
                            ps.setString(7, aqDtlsDedBean.getAdCode());
                            ps.setString(8, aqDtlsDedBean.getNowDedn());
                            ps.setString(9, aqDtlsDedBean.getAdRefId());
                            ps.setString(10, aqDtlsDedBean.getAdType());
                            n = ps.executeUpdate();
                        } else {
                            ps = con.prepareStatement("UPDATE " + tableName + " SET ad_amt=?,instalment_count=?,ref_desc=?,tot_rec_amt=?,ref_count=?,acc_no=? WHERE aqsl_no=? and ad_code=?  and ad_ref_id=? and ad_type=? ");
                            ps.setDouble(1, Double.parseDouble(aqDtlsDedBean.getDedAmt()));
                            if (aqDtlsDedBean.getDedInstalCount() != null && !aqDtlsDedBean.getDedInstalCount().equals("")) {
                                ps.setDouble(2, Double.parseDouble(aqDtlsDedBean.getDedInstalCount()));
                            } else {
                                ps.setInt(2, 0);
                            }
                            ps.setString(3, aqDtlsDedBean.getRefDesc());
                            if (aqDtlsDedBean.getTotRecAmt() != null && !aqDtlsDedBean.getTotRecAmt().equals("")) {
                                ps.setDouble(4, Double.parseDouble(aqDtlsDedBean.getTotRecAmt()));
                            } else {
                                ps.setInt(4, 0);
                            }
                            if (aqDtlsDedBean.getDedInstalNo() != null && !aqDtlsDedBean.getDedInstalNo().equals("")) {
                                ps.setInt(5, Integer.parseInt(aqDtlsDedBean.getDedInstalNo()));
                            } else {
                                ps.setInt(5, 0);
                            }
                            ps.setString(6, aqDtlsDedBean.getPolicyNo());
                            ps.setString(7, aqDtlsDedBean.getAqslNo());
                            ps.setString(8, aqDtlsDedBean.getAdCode());
                            ps.setString(9, aqDtlsDedBean.getAdRefId());
                            ps.setString(10, aqDtlsDedBean.getAdType());
                            n = ps.executeUpdate();
                        }

                    } else {
                        ps = con.prepareStatement("UPDATE " + tableName + " SET ad_amt=?,instalment_count=?,ref_desc=?,tot_rec_amt=?,ref_count=?,acc_no=? WHERE aqsl_no=? and ad_code=? and ad_ref_id=? and ad_type=? ");
                        ps.setDouble(1, Double.parseDouble(aqDtlsDedBean.getDedAmt()));
                        if (aqDtlsDedBean.getDedInstalCount() != null && !aqDtlsDedBean.getDedInstalCount().equals("")) {
                            ps.setDouble(2, Double.parseDouble(aqDtlsDedBean.getDedInstalCount()));
                        } else {
                            ps.setInt(2, 0);
                        }
                        ps.setString(3, aqDtlsDedBean.getRefDesc());
                        if (aqDtlsDedBean.getTotRecAmt() != null && !aqDtlsDedBean.getTotRecAmt().equals("")) {
                            ps.setDouble(4, Double.parseDouble(aqDtlsDedBean.getTotRecAmt()));
                        } else {
                            ps.setInt(4, 0);
                        }
                        if (aqDtlsDedBean.getDedInstalNo() != null && !aqDtlsDedBean.getDedInstalNo().equals("")) {
                            ps.setInt(5, Integer.parseInt(aqDtlsDedBean.getDedInstalNo()));
                        } else {
                            ps.setInt(5, 0);
                        }

                        ps.setString(6, aqDtlsDedBean.getPolicyNo());
                        ps.setString(7, aqDtlsDedBean.getAqslNo());
                        ps.setString(8, aqDtlsDedBean.getAdCode());
                        ps.setString(9, aqDtlsDedBean.getAdRefId());
                        ps.setString(10, aqDtlsDedBean.getAdType());
                        n = ps.executeUpdate();

                    }
                } else {
//                    if (aqDtlsDedBean.getNowDedn() != null && aqDtlsDedBean.getNowDedn().equals("P")) {
//                        ps = con.prepareStatement("UPDATE AQ_DTLS SET ad_amt=?,instalment_count=?,ref_desc=?,tot_rec_amt=?,ref_count=? WHERE aqsl_no=? and ad_code=? and ad_type=? and now_dedn='P'");
//                    }
//                    if (aqDtlsDedBean.getNowDedn() != null && aqDtlsDedBean.getNowDedn().equals("I")) {
//                        ps = con.prepareStatement("UPDATE AQ_DTLS SET ad_amt=?,instalment_count=?,ref_desc=?,tot_rec_amt=?,ref_count=? WHERE aqsl_no=? and ad_code=? and ad_type=? and now_dedn='I'");
//                    }
                    if (!aqDtlsDedBean.getNowDedn().equals("P") && !aqDtlsDedBean.getNowDedn().equals("I")) {
                        ps = con.prepareStatement("UPDATE " + tableName + " SET ad_amt=?,instalment_count=?,ref_desc=?,tot_rec_amt=?,ref_count=? WHERE aqsl_no=? and ad_code=? and ad_type=?");

                        if (aqDtlsDedBean.getDedAmt() != null && !aqDtlsDedBean.getDedAmt().equals("")) {
                            ps.setDouble(1, Double.parseDouble(aqDtlsDedBean.getDedAmt()));
                        } else {
                            ps.setInt(1, 0);
                        }

                        if (aqDtlsDedBean.getDedInstalCount() != null && !aqDtlsDedBean.getDedInstalCount().equals("")) {
                            ps.setDouble(2, Double.parseDouble(aqDtlsDedBean.getDedInstalCount()));
                        } else {
                            ps.setInt(2, 0);
                        }
                        ps.setString(3, aqDtlsDedBean.getRefDesc());
                        if (aqDtlsDedBean.getTotRecAmt() != null && !aqDtlsDedBean.getTotRecAmt().equals("")) {
                            ps.setDouble(4, Double.parseDouble(aqDtlsDedBean.getTotRecAmt()));
                        } else {
                            ps.setInt(4, 0);
                        }
                        if (aqDtlsDedBean.getDedInstalNo() != null && !aqDtlsDedBean.getDedInstalNo().equals("")) {
                            ps.setInt(5, Integer.parseInt(aqDtlsDedBean.getDedInstalNo()));

                        } else {
                            ps.setInt(5, 0);
                        }

                        ps.setString(6, aqDtlsDedBean.getAqslNo());
                        ps.setString(7, aqDtlsDedBean.getAdCode());
                        ps.setString(8, aqDtlsDedBean.getAdType());
                        n = ps.executeUpdate();

                    }
                }
            } else {
                ps = con.prepareStatement("UPDATE " + tableName + " SET ad_amt=? WHERE aqsl_no=? and ad_code=? and ad_type=?");
                if (aqDtlsDedBean.getDedAmt() != null && !aqDtlsDedBean.getDedAmt().equals("")) {
                    ps.setDouble(1, Double.parseDouble(aqDtlsDedBean.getDedAmt()));
                } else {
                    ps.setDouble(1, 0);
                }
                ps.setString(2, aqDtlsDedBean.getAqslNo());
                ps.setString(3, aqDtlsDedBean.getAdCode());
                ps.setString(4, aqDtlsDedBean.getAdType());
                n = ps.executeUpdate();

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public AqDtlsDedBean getAqDetailsAllowance(String tableName, String aqslNo, String adCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        AqDtlsDedBean aqDtlsBean = null;
        String sql = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select ad_amt from " + tableName + "  WHERE aqsl_no=? and ad_code=? and ad_type='A'");
            ps.setString(1, aqslNo);
            ps.setString(2, adCode);
            rs = ps.executeQuery();
            if (rs.next()) {
                aqDtlsBean = new AqDtlsDedBean();
                aqDtlsBean.setDedAmt(rs.getString("ad_amt"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aqDtlsBean;
    }

    public AqDtlsDedBean getAqDetailsDed(String tableName, String aqslNo, String adCode, String nowdedn, String adRefId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        AqDtlsDedBean aqDtlsBean = null;
        String sql = null;
        try {
            con = dataSource.getConnection();
            if (adRefId != null && !adRefId.equals("")) {
                if (nowdedn != null && !nowdedn.equals("")) {
                    ps = con.prepareStatement("select ad_amt,instalment_count,ref_desc,tot_rec_amt,ref_count,acc_no from " + tableName + "  WHERE aqsl_no=? and ad_code=? and now_dedn=? and ad_ref_id=? and ad_type='D'");
                    ps.setString(1, aqslNo);
                    ps.setString(2, adCode);
                    ps.setString(3, nowdedn);
                    ps.setString(4, adRefId);
                } else {

                    ps = con.prepareStatement("select ad_amt,instalment_count,ref_desc,tot_rec_amt,ref_count,acc_no from " + tableName + "  WHERE aqsl_no=? and ad_code=? and ad_ref_id=? and ad_type='D'");
                    ps.setString(1, aqslNo);
                    ps.setString(2, adCode);
                    ps.setString(3, adRefId);
                }

            } else {
                ps = con.prepareStatement("select ad_amt,instalment_count,ref_desc,tot_rec_amt,ref_count,acc_no from " + tableName + "  WHERE aqsl_no=? and ad_code=? and ad_type='D'");
                ps.setString(1, aqslNo);
                ps.setString(2, adCode);
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                aqDtlsBean = new AqDtlsDedBean();
                aqDtlsBean.setDedAmt(rs.getString("ad_amt"));
                aqDtlsBean.setDedInstalNo(rs.getString("ref_count"));
                aqDtlsBean.setRefDesc(rs.getString("ref_desc"));
                aqDtlsBean.setTotRecAmt(rs.getString("tot_rec_amt"));
                aqDtlsBean.setDedInstalCount(rs.getString("instalment_count"));
                aqDtlsBean.setPolicyNo(rs.getString("acc_no"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aqDtlsBean;
    }

    @Override
    public void deleteBrowserAqData(String aqslno, String offCode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("delete from aq_mast where aqsl_no=? and off_code=?");
            pst.setString(1, aqslno);
            pst.setString(2, offCode);
            pst.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public int saveAqdtlsDataDHE(AqDtlsModel[] aqdtlsList, boolean stopSubscription, String aqtableName) {
        int n = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pst2 = null;
        try {
            con = dataSource.getConnection();

            pst2 = con.prepareStatement("update aq_mast set da_rate=? where aqsl_no=? ");

            pstmt = con.prepareStatement("insert into " + aqtableName + " (aq_group ,aqsl_no ,off_ddo,emp_code ,p_month ,p_year,aq_date ,aq_month ,aq_year ,aq_type ,ref_ord ,ref_date,sl_no,ad_code,ad_desc,ad_type, "
                    + "alt_unit,ded_type,ad_amt ,acc_no ,ref_desc ,ref_count,schedule ,now_dedn ,tot_rec_amt ,rep_col ,ad_ref_id ,bt_id,instalment_count,ad_amt_gc) values(?,?,?,?,?,?,?,?,?,?,?,"
                    + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            if (aqdtlsList != null) {
                for (int cnt = 0; cnt < aqdtlsList.length; cnt++) {
                    AqDtlsModel aqdtls = aqdtlsList[cnt];
                    pstmt.setBigDecimal(1, aqdtls.getAqGroup());
                    pstmt.setString(2, aqdtls.getAqSlNo());
                    pstmt.setString(3, aqdtls.getDdoOff());
                    pstmt.setString(4, aqdtls.getEmpCode());
                    pstmt.setInt(5, aqdtls.getPayMon());
                    pstmt.setInt(6, aqdtls.getPayYear());
                    if (aqdtls.getAqDate() != null) {
                        pstmt.setDate(7, new java.sql.Date(aqdtls.getAqDate().getTime()));
                    } else {
                        pstmt.setDate(7, null);
                    }
                    pstmt.setInt(8, aqdtls.getAqMonth());
                    pstmt.setInt(9, aqdtls.getAqYear());
                    pstmt.setString(10, aqdtls.getAqType());
                    pstmt.setString(11, aqdtls.getRefOrderNo());
                    if (aqdtls.getRefOrderDate() != null) {
                        pstmt.setDate(12, new java.sql.Date(aqdtls.getRefOrderDate().getTime()));
                    } else {
                        pstmt.setDate(12, null);
                    }
                    pstmt.setInt(13, aqdtls.getSlNo());

                    pstmt.setString(14, aqdtls.getAdCode());
                    pstmt.setString(15, aqdtls.getAdDesc());
                    pstmt.setString(16, aqdtls.getAdType());
                    pstmt.setString(17, aqdtls.getAltUnit());
                    pstmt.setString(18, aqdtls.getDedType());
                    if (aqdtls.getAdCode() != null && (aqdtls.getAdCode().equals("GPF") || aqdtls.getAdCode().equals("TPF"))) {

                        if (stopSubscription) {
                            pstmt.setLong(19, 0);
                        } else {
                            pstmt.setLong(19, aqdtls.getAdAmt());
                        }

                    } else if (aqdtls.getAdCode() != null && aqdtls.getAdCode().equals("CPF")) {
                        if (stopSubscription) {
                            pstmt.setLong(19, 0);
                        } else {
                            pstmt.setLong(19, aqdtls.getAdAmt());
                        }

                    } else if (aqdtls.getAdCode().equals("EPF") || aqdtls.getAdCode().equals("EPFDED")) {
                        System.out.println("ADCOde:"+aqdtls.getAdCode());
                        if (stopSubscription) {
                            pstmt.setLong(19, 0);
                        } else {
                            pstmt.setLong(19, 0);
                        }
                    } else {
                        pstmt.setLong(19, aqdtls.getAdAmt());
                    }
                    pstmt.setString(20, aqdtls.getAccNo());
                    pstmt.setString(21, aqdtls.getRefDesc());
                    pstmt.setInt(22, aqdtls.getRefCount());
                    pstmt.setString(23, aqdtls.getSchedule());
                    pstmt.setString(24, aqdtls.getNowDedn());
                    if (aqdtls.getAdCode() != null && aqdtls.getAdCode().equals("CPF")) {
                        if (stopSubscription) {
                            pstmt.setInt(25, 0);
                        } else {
                            pstmt.setInt(25, aqdtls.getTotRecAmt());
                        }
                    } else {
                        pstmt.setInt(25, aqdtls.getTotRecAmt());
                    }
                    pstmt.setInt(26, aqdtls.getRepCol());
                    pstmt.setString(27, aqdtls.getAdRefId());
                    pstmt.setString(28, aqdtls.getBtId());
                    pstmt.setInt(29, aqdtls.getInstalCount());
                    if (aqdtls.getAdAmtGc() != null && !aqdtls.getAdAmtGc().equals("")) {
                        pstmt.setInt(30, (int) Double.parseDouble(aqdtls.getAdAmtGc()));
                    } else {
                        pstmt.setInt(30, 0);
                    }
                    if (aqdtls.getAdType() != null && !aqdtls.getAdType().equals("") && aqdtls.getAdType().equalsIgnoreCase("D")) {
                        if (aqdtls.getAdAmt() > 0) {
                            n = pstmt.executeUpdate();
                        }

                    } else if (aqdtls.getAdType() != null && !aqdtls.getAdType().equals("") && aqdtls.getAdType().equalsIgnoreCase("A")) {

                        if (aqdtls.getBtId() != null && !aqdtls.getBtId().equals("") && (aqdtls.getBtId().equalsIgnoreCase("523") || aqdtls.getBtId().equalsIgnoreCase("000"))) {
                            if (aqdtls.getAdAmt() > 0) {

                                n = pstmt.executeUpdate();
                            }
                        } else {
                            n = pstmt.executeUpdate();
                        }

                    } else {
                        n = pstmt.executeUpdate();
                    }

                    if (aqdtls.getAdCode() != null && (aqdtls.getAdCode().equals("DA"))) {
                        pst2.setString(1, aqdtls.getDaformula());
                        pst2.setString(2, aqdtls.getAqSlNo());
                        pst2.execute();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }
}
