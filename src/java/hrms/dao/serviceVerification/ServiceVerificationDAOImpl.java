/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.serviceVerification;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.serviceVerification.ServiceVerification;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Surendra
 */
public class ServiceVerificationDAOImpl implements ServiceVerificationDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected MaxServiceVerificatioIdDAOImpl maxServiceVerificatioIdDAO;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setMaxServiceVerificatioIdDAO(MaxServiceVerificatioIdDAOImpl maxServiceVerificatioIdDAO) {
        this.maxServiceVerificatioIdDAO = maxServiceVerificatioIdDAO;
    }

    @Override
    public List findAllSvData(String empId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {

            con = this.dataSource.getConnection();

            ps = con.prepareStatement("select is_validated,SV_ID,EMP_ID, DOE, FDATE, FTIME, TDATE, TTIME, AUTH_DEPT, AUTH_OFF, AUTH, SPN, NOTE,verified_on from EMP_SV "
                    + " LEFT OUTER JOIN G_SPC ON EMP_SV.AUTH=G_SPC.SPC  where emp_id=? ORDER BY FDATE,TDATE");
            ps.setString(1, empId);
            rs = ps.executeQuery();

            while (rs.next()) {

                ServiceVerification svb = new ServiceVerification();

                svb.setTxtempid(rs.getString("emp_id"));
                svb.setTxtsvid(rs.getString("sv_id"));
                if (rs.getString("verified_on") != null && !rs.getString("verified_on").trim().equals("")) {
                    svb.setTxtdoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("verified_on")));
                } else if (rs.getString("doe") != null && !rs.getString("doe").trim().equals("")) {
                    svb.setTxtdoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                }

                if (rs.getString("fdate") != null && !rs.getString("fdate").trim().equals("")) {
                    svb.setTxtfdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("fdate")));
                } else {
                    svb.setTxtfdate(null);
                }

                if (rs.getString("tdate") != null && !rs.getString("tdate").trim().equals("")) {
                    svb.setTxttdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("tdate")));
                } else {
                    svb.setTxttdate(null);
                }

                //Code to get the difference in days
                svb.setAuthName(rs.getString("SPN"));
                svb.setHidAuthSpc(rs.getString("AUTH"));
                svb.setHidAuthOffice(rs.getString("AUTH_OFF"));
                svb.setHidAuthDept(rs.getString("AUTH_DEPT"));

                svb.setSltftime(rs.getString("FTIME"));
                svb.setSltttime(rs.getString("TTIME"));
                svb.setNotes(rs.getString("note"));
                svb.setIsValidated(rs.getString("is_validated"));

                int qualifyingdays = getQualifyingDays(rs.getString("fdate"), rs.getString("tdate"));

                svb.setqDays(qualifyingdays);
                li.add(svb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public void addSvData(ServiceVerification sv, String authSpn) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String sbLang = "";
        try {
            Date outputDate = null;

            con = this.dataSource.getConnection();

            /*DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            
             Calendar calobj = Calendar.getInstance();
             Date sysdate = calobj.getTime();
            
             Date toDate = df.parse(sv.getTxttdate());
            
             String verifiedon = df.format(calobj.getTime());
             if(sysdate.compareTo(toDate) > 0){
             verifiedon = sv.getTxtVerifiedOn();
             }*/
            ps = con.prepareStatement("INSERT INTO EMP_SV (emp_id, doe, if_assumed, fdate, ftime, tdate, ttime, auth_dept, auth_off, auth , note,organization_type,verified_on) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) ", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, sv.getTxtempid());
            ps.setTimestamp(2, new Timestamp(new Date().getTime()));
            ps.setString(3, "N");
            if (sv.getTxtfdate() != null && !sv.getTxtfdate().equals("")) {
                outputDate = dateFormat.parse(sv.getTxtfdate());
                ps.setTimestamp(4, new Timestamp(outputDate.getTime()));
            } else {
                ps.setTimestamp(4, null);
            }
            ps.setString(5, sv.getSltftime());
            if (sv.getTxttdate() != null && !sv.getTxttdate().equals("")) {
                outputDate = dateFormat.parse(sv.getTxttdate());
                ps.setTimestamp(6, new Timestamp(outputDate.getTime()));
            } else {
                ps.setTimestamp(6, null);
            }
            ps.setString(7, sv.getSltttime());
            ps.setString(8, sv.getHidAuthDept());
            ps.setString(9, sv.getHidAuthOffice());
            ps.setString(10, sv.getHidAuthSpc());
            ps.setString(11, sv.getNotes());
            ps.setString(12, sv.getRadpostingauthtype());
            ps.setTimestamp(13, new Timestamp(dateFormat.parse(sv.getTxtVerifiedOn()).getTime()));
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            rs.next();
            int svId = rs.getInt("sv_id");

            DataBaseFunctions.closeSqlObjects(rs, ps);

            updateSVID(sv.getTxtempid(), svId + "", sv.getTxtfdate(), sv.getTxttdate());

            sbLang = "SERVICES VERIFIED ON " + sv.getTxtVerifiedOn() + " FROM " + sv.getTxtfdate() + " TO " + sv.getTxttdate() + " WITH REFERENCE \n"
                    + "TO PAY ACQUITTANCE ROLL/ ESTABLISHMENT PAY BILLS AND OTHER CONNECTED PAPERS AND FOUND CORRECT BY " + authSpn;
            ps = con.prepareStatement("UPDATE EMP_SV SET SB_DESCRIPTION = ? WHERE SV_ID=? ");
            ps.setString(1, sbLang);
            ps.setInt(2, svId);
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void modifySvData(ServiceVerification sv, String authSpn) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String sbLang = "";
        try {
            Date outputDate = null;

            con = this.dataSource.getConnection();

            ps = con.prepareStatement(" UPDATE  EMP_SV SET  fdate=?, ftime=?, tdate=?, ttime=?, auth_dept=?, auth_off=?, auth=?, note=?,organization_type=?,verified_on=? WHERE sv_id=? ");
            if (sv.getTxtfdate() != null && !sv.getTxtfdate().equals("")) {
                outputDate = dateFormat.parse(sv.getTxtfdate());
                ps.setTimestamp(1, new Timestamp(outputDate.getTime()));
            } else {
                ps.setTimestamp(1, null);
            }
            ps.setString(2, sv.getSltftime());
            if (sv.getTxttdate() != null && !sv.getTxttdate().equals("")) {
                outputDate = dateFormat.parse(sv.getTxttdate());
                ps.setTimestamp(3, new Timestamp(outputDate.getTime()));
            } else {
                ps.setTimestamp(3, null);
            }
            ps.setString(4, sv.getSltttime());
            ps.setString(5, sv.getHidAuthDept());
            ps.setString(6, sv.getHidAuthOffice());
            ps.setString(7, sv.getHidAuthSpc());
            ps.setString(8, sv.getNotes());
            ps.setString(9, sv.getRadpostingauthtype());
            ps.setTimestamp(10, new Timestamp(dateFormat.parse(sv.getTxtVerifiedOn()).getTime()));
            ps.setInt(11, Integer.parseInt(sv.getTxtsvid()));
            ps.execute();

            DataBaseFunctions.closeSqlObjects(ps);

            sbLang = "SERVICES VERIFIED ON " + sv.getTxtVerifiedOn() + " FROM " + sv.getTxtfdate() + " TO " + sv.getTxttdate() + " WITH REFERENCE \n"
                    + "TO PAY ACQUITTANCE ROLL/ ESTABLISHMENT PAY BILLS AND OTHER CONNECTED PAPERS AND FOUND CORRECT BY " + authSpn;
            ps = con.prepareStatement("UPDATE EMP_SV SET SB_DESCRIPTION = ? WHERE SV_ID=? ");
            ps.setString(1, sbLang);
            ps.setInt(2, Integer.parseInt(sv.getTxtsvid()));
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void removeSvData(String svId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ServiceVerification svb = new ServiceVerification();
        try {

            con = this.dataSource.getConnection();
            ps = con.prepareStatement("DELETE from emp_sv  WHERE sv_id=? ");
            ps.setInt(1, Integer.parseInt(svId));
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ServiceVerification getServiceVerificationData(String svId, String empId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ServiceVerification svb = new ServiceVerification();
        try {

            con = this.dataSource.getConnection();
            if (svId != null && !svId.equals("")) {
                ps = con.prepareStatement("select SV_ID, EMP_ID, DOE, FDATE, FTIME, TDATE, TTIME, AUTH_DEPT, AUTH_OFF, AUTH, SPN, NOTE,organization_type,verified_on from EMP_SV "
                        + " LEFT OUTER JOIN G_SPC ON EMP_SV.AUTH=G_SPC.SPC  where emp_id=? and SV_ID=? order by FDATE");

                ps.setString(1, empId);
                ps.setInt(2, Integer.parseInt(svId));
                rs = ps.executeQuery();

                if (rs.next()) {

                    svb.setTxtempid(rs.getString("emp_id"));
                    svb.setTxtsvid(rs.getInt("sv_id") + "");
                    if (rs.getString("doe") != null && !rs.getString("doe").trim().equals("")) {
                        svb.setTxtdoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                    }

                    if (rs.getString("fdate") != null && !rs.getString("fdate").trim().equals("")) {
                        svb.setTxtfdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("fdate")));
                    } else {
                        svb.setTxtfdate(null);
                    }

                    if (rs.getString("tdate") != null && !rs.getString("tdate").trim().equals("")) {
                        svb.setTxttdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("tdate")));
                    } else {
                        svb.setTxttdate(null);
                    }
                    if (rs.getString("organization_type") != null && rs.getString("organization_type").equals("GOI")) {
                        svb.setRadpostingauthtype(rs.getString("organization_type"));
                        svb.setHidOthSpc(rs.getString("AUTH"));
                        svb.setAuthName(getOtherSpn(rs.getString("AUTH")));
                    } else {
                        svb.setRadpostingauthtype("GOO");
                        svb.setHidAuthSpc(rs.getString("AUTH"));
                        svb.setSltSpc(rs.getString("AUTH"));
                        svb.setAuthName(rs.getString("SPN"));
                    }

                    svb.setHidAuthOffice(rs.getString("AUTH_OFF"));
                    svb.setSltOffice(rs.getString("AUTH_OFF"));
                    svb.setHidAuthDept(rs.getString("AUTH_DEPT"));
                    svb.setSltDept(rs.getString("AUTH_DEPT"));

                    svb.setSltftime(rs.getString("FTIME"));
                    svb.setSltttime(rs.getString("TTIME"));
                    svb.setNotes(rs.getString("note"));

                    svb.setTxtVerifiedOn(CommonFunctions.getFormattedOutputDate1(rs.getDate("verified_on")));
                    if (svb.getTxtVerifiedOn() == null || svb.getTxtVerifiedOn().equals("")) {
                        svb.setTxtVerifiedOn(svb.getTxtdoe());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return svb;
    }

    @Override
    public boolean chkDuplicate(String frmdate, String todate, String svid, String empid) throws Exception {
        boolean ret = false;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            if (frmdate != null && todate != null) {

                Date fromDt = new SimpleDateFormat("dd-MMM-yyyy").parse(frmdate);
                Date toDt = new SimpleDateFormat("dd-MMM-yyyy").parse(todate);

                if (svid != null && !svid.equals("")) {
                    pst = con.prepareStatement("select fdate from emp_sv where fdate::date=? and tdate::date=? and emp_id=? and sv_id != ?");
                    pst.setDate(1, new java.sql.Date(fromDt.getTime()));
                    pst.setDate(2, new java.sql.Date(toDt.getTime()));
                    pst.setString(3, empid);
                    pst.setInt(4, Integer.parseInt(svid));
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        ret = true;
                    }
                } else {
                    pst = con.prepareStatement("select fdate from emp_sv where fdate::date=? and tdate::date=? and emp_id=?");
                    pst.setDate(1, new java.sql.Date(fromDt.getTime()));
                    pst.setDate(2, new java.sql.Date(toDt.getTime()));
                    pst.setString(3, empid);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        ret = true;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ret;
    }

    public String getOtherSpn(String othSpc) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        String spn = "";
        try {
            con = this.dataSource.getConnection();

            if (othSpc != null && !othSpc.equals("")) {
                pst = con.prepareStatement("SELECT oth_spc, off_en FROM g_oth_spc WHERE other_spc_id=? and is_active='Y'");
                pst.setInt(1, Integer.parseInt(othSpc));
                rs = pst.executeQuery();
                if (rs.next()) {
                    spn = rs.getString("off_en");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spn;
    }

    @Override
    public void updateSVID(String empid, String svid, String frmDate, String toDate) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("UPDATE EMP_SR_PAY SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, svid);
            pst.setString(2, empid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE EMP_SERVICERECORD SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, svid);
            pst.setString(2, empid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, svid);
            pst.setString(2, empid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE EMP_EXAM SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, svid);
            pst.setString(2, empid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE EMP_QUALIFICATION SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, svid);
            pst.setString(2, empid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE EMP_RET_RES SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, svid);
            pst.setString(2, empid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE EMP_JOIN SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, svid);
            pst.setString(2, empid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE EMP_SUSPENSION SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, svid);
            pst.setString(2, empid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE EMP_REINSTATEMENT SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, svid);
            pst.setString(2, empid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE EMP_RELIEVE SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, svid);
            pst.setString(2, empid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE EMP_PERMISSION SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, svid);
            pst.setString(2, empid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateNullSVID(String empid, String frmDate, String toDate) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("UPDATE EMP_SR_PAY SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, null);
            pst.setString(2, empid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE EMP_SERVICERECORD SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, null);
            pst.setString(2, empid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, null);
            pst.setString(2, empid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE EMP_EXAM SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, null);
            pst.setString(2, empid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE EMP_QUALIFICATION SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, null);
            pst.setString(2, empid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE EMP_RET_RES SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, null);
            pst.setString(2, empid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE EMP_JOIN SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, null);
            pst.setString(2, empid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE EMP_SUSPENSION SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, null);
            pst.setString(2, empid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE EMP_REINSTATEMENT SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, null);
            pst.setString(2, empid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE EMP_RELIEVE SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, null);
            pst.setString(2, empid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE EMP_PERMISSION SET SV_ID=? WHERE EMP_ID=? AND DOE>='" + frmDate + "'::DATE AND DOE<='" + toDate + "'::DATE");
            pst.setString(1, null);
            pst.setString(2, empid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public int getQualifyingDays(String fdate, String tdate) {
        long diff = 0;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date firstDate = sdf.parse(fdate);
            Date secondDate = sdf.parse(tdate);

            long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
            diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            System.out.println("diff is: " + diff);
            //assertEquals(6, diff);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int) diff;
    }

    private String formatDate(Date input) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = "";

        try {
            formattedDate = sdf.format(input);
        } catch (Exception exp) {
            formattedDate = "";
        }
        return formattedDate;
    }

    @Override
    public String getEmployeeDateOfEntry(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String doegov = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT DOE_GOV FROM EMP_MAST WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                doegov = rs.getString("DOE_GOV");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return doegov;
    }

    public String getEmployeeDateOfRetirement(String empid) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String dos = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT DOS FROM EMP_MAST WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                dos = rs.getString("DOS");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return dos;
    }

    @Override
    public ServiceVerification getServiceVerificationDataSBCorrectionDDO(String svId, String empId,int correctionid) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ServiceVerification svb = new ServiceVerification();
        try {

            con = this.dataSource.getConnection();

            ps = con.prepareStatement("select SV_ID, EMP_ID, DOE, FDATE, FTIME, TDATE, TTIME, AUTH_DEPT, AUTH_OFF, AUTH, SPN, NOTE,organization_type,verified_on from EMP_SV_SB_CORRECTION EMP_SV"
                    + " LEFT OUTER JOIN G_SPC ON EMP_SV.AUTH=G_SPC.SPC where emp_id=? and SV_ID=? AND REF_CORRECTION_ID=? order by FDATE");

            ps.setString(1, empId);
            ps.setInt(2, Integer.parseInt(svId));
            ps.setInt(3, correctionid);
            rs = ps.executeQuery();
            if (rs.next()) {

                svb.setTxtempid(rs.getString("emp_id"));
                svb.setTxtsvid(rs.getInt("sv_id") + "");
                if (rs.getString("doe") != null && !rs.getString("doe").trim().equals("")) {
                    svb.setTxtdoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                }

                if (rs.getString("fdate") != null && !rs.getString("fdate").trim().equals("")) {
                    svb.setTxtfdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("fdate")));
                } else {
                    svb.setTxtfdate(null);
                }

                if (rs.getString("tdate") != null && !rs.getString("tdate").trim().equals("")) {
                    svb.setTxttdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("tdate")));
                } else {
                    svb.setTxttdate(null);
                }
                if (rs.getString("organization_type") != null && rs.getString("organization_type").equals("GOI")) {
                    svb.setRadpostingauthtype(rs.getString("organization_type"));
                    svb.setHidOthSpc(rs.getString("AUTH"));
                    svb.setAuthName(getOtherSpn(rs.getString("AUTH")));
                } else {
                    svb.setRadpostingauthtype("GOO");
                    svb.setHidAuthSpc(rs.getString("AUTH"));
                    svb.setSltSpc(rs.getString("AUTH"));
                    svb.setAuthName(rs.getString("SPN"));
                }

                svb.setHidAuthOffice(rs.getString("AUTH_OFF"));
                svb.setSltOffice(rs.getString("AUTH_OFF"));
                svb.setHidAuthDept(rs.getString("AUTH_DEPT"));
                svb.setSltDept(rs.getString("AUTH_DEPT"));

                svb.setSltftime(rs.getString("FTIME"));
                svb.setSltttime(rs.getString("TTIME"));
                svb.setNotes(rs.getString("note"));

                svb.setTxtVerifiedOn(CommonFunctions.getFormattedOutputDate1(rs.getDate("verified_on")));
                if (svb.getTxtVerifiedOn() == null || svb.getTxtVerifiedOn().equals("")) {
                    svb.setTxtVerifiedOn(svb.getTxtdoe());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return svb;
    }

    @Override
    public String addSvDataSBCorrection(ServiceVerification sv, String authSpn) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String sbLang = "";
        try {
            Date outputDate = null;

            con = this.dataSource.getConnection();

            ps = con.prepareStatement("DELETE FROM EMP_SV_SB_CORRECTION WHERE emp_id=? AND SV_ID=?");
            ps.setString(1, sv.getTxtempid());
            if(sv.getTxtsvid() != null && !sv.getTxtsvid().equals("")){
                ps.setInt(2, Integer.parseInt(sv.getTxtsvid()));
            }else{
                ps.setInt(2, 0);
            }
            ps.executeUpdate();

            ps = con.prepareStatement("INSERT INTO EMP_SV_SB_CORRECTION (emp_id, doe, if_assumed, fdate, ftime, tdate, ttime, auth_dept, auth_off, auth , note,organization_type,verified_on,ref_correction_id,sv_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, sv.getTxtempid());
            ps.setTimestamp(2, new Timestamp(new Date().getTime()));
            ps.setString(3, "N");
            if (sv.getTxtfdate() != null && !sv.getTxtfdate().equals("")) {
                outputDate = dateFormat.parse(sv.getTxtfdate());
                ps.setTimestamp(4, new Timestamp(outputDate.getTime()));
            } else {
                ps.setTimestamp(4, null);
            }
            ps.setString(5, sv.getSltftime());
            if (sv.getTxttdate() != null && !sv.getTxttdate().equals("")) {
                outputDate = dateFormat.parse(sv.getTxttdate());
                ps.setTimestamp(6, new Timestamp(outputDate.getTime()));
            } else {
                ps.setTimestamp(6, null);
            }
            ps.setString(7, sv.getSltttime());
            ps.setString(8, sv.getHidAuthDept());
            ps.setString(9, sv.getHidAuthOffice());
            ps.setString(10, sv.getHidAuthSpc());
            ps.setString(11, sv.getNotes());
            ps.setString(12, sv.getRadpostingauthtype());
            if(sv.getTxtVerifiedOn() != null && !sv.getTxtVerifiedOn().equals("")){
                ps.setTimestamp(13, new Timestamp(dateFormat.parse(sv.getTxtVerifiedOn()).getTime()));
            }else{
                ps.setTimestamp(13, null);
            }
            ps.setInt(14, Integer.parseInt(sv.getCorrectionid()));
            if(sv.getTxtsvid() != null && !sv.getTxtsvid().equals("")){
                ps.setInt(15, Integer.parseInt(sv.getTxtsvid()));
            }else{
                ps.setInt(15, 0);
            }
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            rs.next();
            int logid = rs.getInt("log_id");

            DataBaseFunctions.closeSqlObjects(rs, ps);

            sbLang = "SERVICES VERIFIED ON " + sv.getTxtVerifiedOn() + " FROM " + sv.getTxtfdate() + " TO " + sv.getTxttdate() + " WITH REFERENCE \n"
                    + "TO PAY ACQUITTANCE ROLL/ ESTABLISHMENT PAY BILLS AND OTHER CONNECTED PAPERS AND FOUND CORRECT BY " + authSpn;
            ps = con.prepareStatement("UPDATE EMP_SV_SB_CORRECTION SET SB_DESCRIPTION = ? WHERE log_id=? ");
            ps.setString(1, sbLang);
            ps.setInt(2, logid);
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return sbLang;
    }

    @Override
    public void approveServiceVerificationSBCorrection(ServiceVerification sv, String authSpn, String entrydeptcode, String entryoffcode, String entryspc, String loginuserid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String sbLang = "";
        try {
            con = this.dataSource.getConnection();

            Date outputDate = null;

            if (sv.getEntrytypeSBCorrection() != null && sv.getEntrytypeSBCorrection().equals("NEW")) {
                pst = con.prepareStatement("INSERT INTO EMP_SV (emp_id, doe, if_assumed, fdate, ftime, tdate, ttime, auth_dept, auth_off, auth , note,organization_type,verified_on) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) ", Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, sv.getTxtempid());
                pst.setTimestamp(2, new Timestamp(new Date().getTime()));
                pst.setString(3, "N");
                if (sv.getTxtfdate() != null && !sv.getTxtfdate().equals("")) {
                    outputDate = dateFormat.parse(sv.getTxtfdate());
                    pst.setTimestamp(4, new Timestamp(outputDate.getTime()));
                } else {
                    pst.setTimestamp(4, null);
                }
                pst.setString(5, sv.getSltftime());
                if (sv.getTxttdate() != null && !sv.getTxttdate().equals("")) {
                    outputDate = dateFormat.parse(sv.getTxttdate());
                    pst.setTimestamp(6, new Timestamp(outputDate.getTime()));
                } else {
                    pst.setTimestamp(6, null);
                }
                pst.setString(7, sv.getSltttime());
                pst.setString(8, sv.getHidAuthDept());
                pst.setString(9, sv.getHidAuthOffice());
                pst.setString(10, sv.getHidAuthSpc());
                pst.setString(11, sv.getNotes());
                pst.setString(12, sv.getRadpostingauthtype());
                pst.setTimestamp(13, new Timestamp(dateFormat.parse(sv.getTxtVerifiedOn()).getTime()));
                pst.executeUpdate();

                rs = pst.getGeneratedKeys();
                rs.next();
                int svid = rs.getInt("sv_id");

                DataBaseFunctions.closeSqlObjects(rs, pst);

                sbLang = "SERVICES VERIFIED ON " + sv.getTxtVerifiedOn() + " FROM " + sv.getTxtfdate() + " TO " + sv.getTxttdate() + " WITH REFERENCE \n"
                        + "TO PAY ACQUITTANCE ROLL/ ESTABLISHMENT PAY BILLS AND OTHER CONNECTED PAPERS AND FOUND CORRECT BY " + authSpn;
                pst = con.prepareStatement("UPDATE EMP_SV SET SB_DESCRIPTION = ? WHERE sv_id=?");
                pst.setString(1, sbLang);
                pst.setInt(2, svid);
                pst.execute();
            } else {
                pst = con.prepareStatement("UPDATE EMP_SV_SB_CORRECTION SET if_assumed=?, fdate=?, ftime=?, tdate=?, ttime=?, auth_dept=?, auth_off=?, auth=? , note=?,organization_type=?,verified_on=?"
                        + " WHERE SV_ID=?");
                pst.setString(1, "N");
                if (sv.getTxtfdate() != null && !sv.getTxtfdate().equals("")) {
                    outputDate = dateFormat.parse(sv.getTxtfdate());
                    pst.setTimestamp(2, new Timestamp(outputDate.getTime()));
                } else {
                    pst.setTimestamp(2, null);
                }
                pst.setString(3, sv.getSltftime());
                if (sv.getTxttdate() != null && !sv.getTxttdate().equals("")) {
                    outputDate = dateFormat.parse(sv.getTxttdate());
                    pst.setTimestamp(4, new Timestamp(outputDate.getTime()));
                } else {
                    pst.setTimestamp(4, null);
                }
                pst.setString(5, sv.getSltttime());
                pst.setString(6, sv.getHidAuthDept());
                pst.setString(7, sv.getHidAuthOffice());
                pst.setString(8, sv.getHidAuthSpc());
                pst.setString(9, sv.getNotes());
                pst.setString(10, sv.getRadpostingauthtype());
                pst.setTimestamp(11, new Timestamp(dateFormat.parse(sv.getTxtVerifiedOn()).getTime()));
                pst.setInt(12, Integer.parseInt(sv.getTxtsvid()));
                pst.executeUpdate();

                pst = con.prepareStatement("UPDATE EMP_SV SET if_assumed=EMP_SV_SB_CORRECTION.if_assumed, fdate=EMP_SV_SB_CORRECTION.fdate, ftime=EMP_SV_SB_CORRECTION.ftime, tdate=EMP_SV_SB_CORRECTION.tdate, ttime=EMP_SV_SB_CORRECTION.ttime, auth_dept=EMP_SV_SB_CORRECTION.auth_dept, auth_off=EMP_SV_SB_CORRECTION.auth_off, auth=EMP_SV_SB_CORRECTION.auth , note=EMP_SV_SB_CORRECTION.note,organization_type=EMP_SV_SB_CORRECTION.organization_type,verified_on=EMP_SV_SB_CORRECTION.verified_on FROM EMP_SV_SB_CORRECTION"
                        + " WHERE EMP_SV.SV_ID=EMP_SV_SB_CORRECTION.SV_ID AND EMP_SV_SB_CORRECTION.SV_ID=?");
                pst.setInt(1, Integer.parseInt(sv.getTxtsvid()));
                pst.executeUpdate();

                sbLang = "SERVICES VERIFIED ON " + sv.getTxtVerifiedOn() + " FROM " + sv.getTxtfdate() + " TO " + sv.getTxttdate() + " WITH REFERENCE \n"
                        + "TO PAY ACQUITTANCE ROLL/ ESTABLISHMENT PAY BILLS AND OTHER CONNECTED PAPERS AND FOUND CORRECT BY " + authSpn;
                pst = con.prepareStatement("UPDATE EMP_SV SET SB_DESCRIPTION = ? WHERE sv_id=?");
                pst.setString(1, sbLang);
                pst.setInt(2, Integer.parseInt(sv.getTxtsvid()));
                pst.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}
