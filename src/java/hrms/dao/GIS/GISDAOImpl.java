package hrms.dao.GIS;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.GIS.GISBean;
import hrms.model.GIS.GISForm;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class GISDAOImpl implements GISDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getGISList(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        GISBean gbean = null;

        List gislist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from EMP_GIS where EMP_ID=? order by ddate desc";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                gbean = new GISBean();
                gbean.setGisid(rs.getString("gis_id"));
                gbean.setInstrumentNo(rs.getString("instru"));
                gbean.setDateOfDeposit(CommonFunctions.getFormattedOutputDate1(rs.getDate("ddate")));
                gbean.setVoucherNo(rs.getString("vchno"));
                gbean.setTreasuryName(rs.getString("tr_code"));
                gbean.setAmount(rs.getString("gamt"));
                gislist.add(gbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gislist;
    }

    @Override
    public void saveGISData(GISForm gisform, String logindeptname, String loginofficename, String loginpost) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            if (gisform.getGisid() != null && !gisform.getGisid().equals("")) {
                String sql = "update EMP_GIS set INSTRU=?,DDATE=?,auth_dept=?,auth_off=?,auth=?,ent_dept=?,ent_off=?,ent_auth=?,VCHNO=?,TR_CODE=?,GAMT=?,scheme_id=? where gis_id=? and emp_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, gisform.getTxtInstrumentNo());
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(gisform.getTxtDepositDate()).getTime()));
                pst.setString(3, gisform.getHidDeptCode());
                pst.setString(4, gisform.getHidOffCode());
                pst.setString(5, gisform.getAuthSpc());
                pst.setString(6, logindeptname);
                pst.setString(7, loginofficename);
                pst.setString(8, loginpost);
                pst.setString(9, gisform.getTxtVoucherNo().toUpperCase());
                pst.setString(10, gisform.getSltTreasuryName());
                if (gisform.getTxtPremiumAmount() != null && !gisform.getTxtPremiumAmount().equals("")) {
                    pst.setDouble(11, Double.parseDouble(gisform.getTxtPremiumAmount()));
                } else {
                    pst.setDouble(11, 0);
                }
                pst.setString(12, gisform.getSltSchemeName());
                pst.setBigDecimal(13, new BigDecimal(gisform.getGisid()));
                pst.setString(14, gisform.getEmpid());
                pst.executeUpdate();
            } else {
                String sql = "insert into EMP_GIS (EMP_ID,INSTRU,DDATE,auth_dept,auth_off,auth,ent_dept,ent_off,ent_auth,VCHNO,TR_CODE,GAMT,DOE,scheme_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql);
                pst.setString(1, gisform.getEmpid());
                pst.setString(2, gisform.getTxtInstrumentNo());
                pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(gisform.getTxtDepositDate()).getTime()));
                pst.setString(4, gisform.getHidDeptCode());
                pst.setString(5, gisform.getHidOffCode());
                pst.setString(6, gisform.getAuthSpc());
                pst.setString(7, logindeptname);
                pst.setString(8, loginofficename);
                pst.setString(9, loginpost);
                pst.setString(10, gisform.getTxtVoucherNo().toUpperCase());
                pst.setString(11, gisform.getSltTreasuryName());
                if (gisform.getTxtPremiumAmount() != null && !gisform.getTxtPremiumAmount().equals("")) {
                    pst.setDouble(12, Double.parseDouble(gisform.getTxtPremiumAmount()));
                } else {
                    pst.setDouble(12, 0);
                }
                pst.setTimestamp(13, new Timestamp(new Date().getTime()));
                pst.setString(14, gisform.getSltSchemeName());
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
    public GISForm editGISData(String empid, String gisid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        GISForm gform = new GISForm();
        try {
            con = this.dataSource.getConnection();

            String sql = "select emp_gis.*,spn,gpc,dist_code from emp_gis\n"
                    + "left outer join g_spc on emp_gis.auth=g_spc.spc \n"
                    + "left outer join g_office ON emp_gis.auth_off=g_office.off_code\n"
                    + "where gis_id=? and emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setBigDecimal(1, new BigDecimal(gisid));
            pst.setString(2, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                gform.setGisid(rs.getString("gis_id"));
                gform.setTxtInstrumentNo(rs.getString("instru"));
                gform.setTxtDepositDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ddate")));
                gform.setHidDeptCode(rs.getString("auth_dept"));
                gform.setHidOffCode(rs.getString("auth_off"));
                gform.setAuthSpc(rs.getString("auth"));
                gform.setAuthorityPostName(rs.getString("spn"));
                gform.setTxtVoucherNo(rs.getString("vchno"));
                gform.setSltSchemeName(rs.getString("scheme_id"));
                gform.setSltTreasuryName(rs.getString("tr_code"));
                gform.setTxtPremiumAmount(rs.getString("gamt"));
                gform.setHidDistCode(rs.getString("dist_code"));
                gform.setHidGPC(rs.getString("gpc"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gform;
    }

    @Override
    public List getSchemeDataList() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SelectOption so = null;
        List schemelist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from g_scheme order by scheme_name";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("scheme_id"));
                so.setLabel(rs.getString("scheme_name"));
                schemelist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return schemelist;
    }
    
    @Override
    public void deleteGISData(String gisid){
        Connection con = null;
        int n = 0;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM emp_gis WHERE  gis_id=?");          
            pst.setBigDecimal(1, new BigDecimal(gisid));
          
            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    
    }
}
