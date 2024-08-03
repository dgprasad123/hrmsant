/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.PayInServiceRecord;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.PayInServiceRecord.PayInServiceRecordForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Madhusmita
 */
public class PayInServiceRecordDAOImpl implements PayInServiceRecordDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getPayInServiceRecordList(String empid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList servicepaylist = new ArrayList();
        PayInServiceRecordForm pisr = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select * from emp_sr_pay where emp_id=?");
            ps.setString(1, empid);
            rs = ps.executeQuery();
            while (rs.next()) {
                pisr = new PayInServiceRecordForm();
                pisr.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                pisr.setWefdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
                pisr.setPay(rs.getDouble("pay"));
                pisr.setDa(rs.getDouble("da"));
                pisr.setAda(rs.getDouble("ada"));
                pisr.setTotPay(rs.getDouble("tot_pay"));
                pisr.setPayScale(rs.getString("pay_scale"));
                pisr.setLeaveSal(rs.getDouble("leave_sal"));
                pisr.setSrpId(Integer.toString(rs.getInt("srp_id")));
                pisr.setNote(rs.getString("remark"));
                servicepaylist.add(pisr);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return servicepaylist;

    }

    @Override
    public void savePISRData(String empid, PayInServiceRecordForm pisr) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String maxSrpId = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        double totPay = 0;

        try {
            con = this.dataSource.getConnection();

            Date currentDate = new Date();

            ps = con.prepareStatement("select max(srp_id):: integer + 1 maxSrpId from emp_sr_pay");
            rs = ps.executeQuery();
            if (rs.next()) {
                maxSrpId = rs.getString("maxSrpId");
            }

            ps = con.prepareStatement("Insert into emp_sr_pay(emp_id,wef,pay,pay_scale,s_pay,p_pay,oth_pay,oth_desc,da,ada,leave_sal,tot_pay,srp_id,doe,ent_dept,ent_off,ent_auth,remark)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, empid);
            ps.setTimestamp(2, new Timestamp(sdf.parse(pisr.getWefdate()).getTime()));
            ps.setDouble(3, pisr.getPay());
            ps.setString(4, pisr.getSltPayScale());
            ps.setDouble(5, pisr.getSpecialPay());
            ps.setDouble(6, pisr.getPersonalPay());
            ps.setDouble(7, pisr.getOthEmoulment());
            if (pisr.getOthPayDescr() != null && !pisr.getOthPayDescr().equals("")) {
                ps.setString(8, pisr.getOthPayDescr().toUpperCase().trim().toString());
            } else {
                ps.setString(8, null);
            }
            ps.setDouble(9, pisr.getDa());
            ps.setDouble(10, pisr.getAda());
            ps.setDouble(11, pisr.getLeaveSal());
            totPay = pisr.getPay() + pisr.getSpecialPay() + pisr.getPersonalPay() + pisr.getDa()
                    + pisr.getAda() + pisr.getLeaveSal() + pisr.getOthEmoulment();
            ps.setDouble(12, totPay);
            ps.setInt(13, Integer.parseInt(maxSrpId));
            ps.setTimestamp(14, new Timestamp(currentDate.getTime()));
            ps.setString(15, pisr.getEntDept());
            ps.setString(16, pisr.getEntOfc());
            ps.setString(17, pisr.getEntSpc());
            ps.setString(18, pisr.getNote());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public PayInServiceRecordForm getPayInServiceRecordData(String empid, String srpid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        ArrayList arlist = null;
        PayInServiceRecordForm pisr = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select * from emp_sr_pay where emp_id=? and srp_id=?");
            pst.setString(1, empid);
            pst.setInt(2, Integer.parseInt(srpid));
            rs = pst.executeQuery();
            if (rs.next()) {
                pisr = new PayInServiceRecordForm();
                pisr.setWefdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
                pisr.setPay((rs.getDouble("pay")));
                pisr.setSltPayScale(rs.getString("pay_scale"));
                pisr.setSpecialPay((rs.getDouble("s_pay")));
                pisr.setPersonalPay((rs.getDouble("p_pay")));
                pisr.setOthPayDescr(rs.getString("oth_desc"));
                pisr.setOthEmoulment((rs.getDouble("oth_pay")));
                pisr.setDa((rs.getDouble("da")));
                pisr.setAda((rs.getDouble("ada")));
                pisr.setLeaveSal((rs.getDouble("leave_sal")));
                pisr.setSltPayScale(rs.getString("pay_scale"));
                pisr.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                pisr.setNote(rs.getString("remark"));
                pisr.setTotPay((rs.getDouble("tot_pay")));
                pisr.setHidSrpId(srpid);
            }

        } catch (Exception e) {
            e.printStackTrace();;
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pisr;
    }

    @Override
    public void modifyPISRData(PayInServiceRecordForm pisr, String srpid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        double totalPay = 0;

        try {
            con = this.dataSource.getConnection();
            Date modifiedDate = new Date();
            ps = con.prepareStatement("Update emp_sr_pay set wef=?,pay=?,pay_scale=?,s_pay=?,p_pay=?,oth_pay=?,oth_desc=?,da=?,ada=?,leave_sal=?,tot_pay=?,doe=?,ent_dept=?,"
                    + "ent_off=?,ent_auth=?,remark=? where emp_id=? and srp_id=?");
            ps.setTimestamp(1, new Timestamp(sdf.parse(pisr.getWefdate()).getTime()));
            ps.setDouble(2, pisr.getPay());
            ps.setString(3, pisr.getSltPayScale());
            ps.setDouble(4, pisr.getSpecialPay());
            ps.setDouble(5, pisr.getPersonalPay());
            ps.setDouble(6, pisr.getOthEmoulment());
            if (pisr.getOthPayDescr() != null && !pisr.getOthPayDescr().equals("")) {
                ps.setString(7, pisr.getOthPayDescr().toUpperCase().trim().toString());
            } else {
                ps.setString(7, null);
            }
            ps.setDouble(8, pisr.getDa());
            ps.setDouble(9, pisr.getAda());
            ps.setDouble(10, pisr.getLeaveSal());

            totalPay = pisr.getPay() + pisr.getSpecialPay() + pisr.getPersonalPay() + pisr.getDa()
                    + pisr.getAda() + pisr.getLeaveSal() + pisr.getOthEmoulment();
            ps.setDouble(11, totalPay);
            ps.setTimestamp(12, new Timestamp(modifiedDate.getTime()));
            ps.setString(13, pisr.getEntDept());
            ps.setString(14, pisr.getEntOfc());
            ps.setString(15, pisr.getEntSpc());
            ps.setString(16, pisr.getNote());
            ps.setString(17, pisr.getEmpid());
            ps.setInt(18, Integer.parseInt(srpid));

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void deletePISRData(String empid, String srpid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            System.out.println("empid:srpid:" + empid + ";" + srpid);
            ps = con.prepareStatement("Delete from emp_sr_pay where emp_id=? and srp_id=?");
            ps.setString(1, empid);
            ps.setInt(2, Integer.parseInt(srpid));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public List getpayInServiceData(String empid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList DAList = new ArrayList();
        PayInServiceRecordForm pisr = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select * from emp_sr_pay where emp_id=? order by wef");
            ps.setString(1, empid);
            rs = ps.executeQuery();
            while (rs.next()) {
                pisr = new PayInServiceRecordForm();
                pisr.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                pisr.setWefdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
                pisr.setPay(rs.getDouble("pay"));
                pisr.setDa(rs.getDouble("da"));
                pisr.setAda(rs.getDouble("ada"));
                pisr.setTotPay(rs.getDouble("tot_pay"));
                pisr.setPayScale(rs.getString("pay_scale"));
                pisr.setLeaveSal(rs.getDouble("leave_sal"));
                pisr.setSrpId(Integer.toString(rs.getInt("srp_id")));
                pisr.setPersonalPay(rs.getDouble("p_pay"));
                pisr.setSpecialPay(rs.getDouble("s_pay"));
                pisr.setOthEmoulment(rs.getDouble("oth_pay"));
                pisr.setNote(rs.getString("remark"));
                DAList.add(pisr);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return DAList;

    }

}
