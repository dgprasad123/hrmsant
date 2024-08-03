/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.suspension;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.empinfo.EmployeeInformationDAO;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.model.suspension.Suspension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author lenovo pc
 */
public class SuspensionDAOImpl implements SuspensionDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected EmployeeInformationDAO empinfoDAO;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected ServiceBookLanguageDAO sbDAO;

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public void setEmpinfoDAO(EmployeeInformationDAO empinfoDAO) {
        this.empinfoDAO = empinfoDAO;
    }

    public List findAllSuspension(String empId) {
        List list = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Suspension susList = null;
        list = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select sp_id, sus.not_id, sus.emp_id, sus.doe, sus.ordno, sus.orddt, wefd, susp_allow  FROM emp_suspension sus\n"
                    + " inner join emp_notification noti on sus.not_id = noti.not_id and not_type='SUSPENSION' where sus.emp_id=?");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                susList = new Suspension();
                susList.setSuspensionId(rs.getString("sp_id"));
                susList.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOE")));
                susList.setOrdno(rs.getString("ORDNO"));
                susList.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                susList.setWefdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("wefd")));
                susList.setTxtallowance(rs.getDouble("susp_allow") + "");
                susList.setNottficationType("SUSPENSION");
                list.add(susList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    public int insertSuspensionData(Suspension suspend) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();
            Date outputDate = null;
            
            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());
            
            pst = con.prepareStatement("insert into emp_suspension "
                    + "(emp_id, doe, TOE, IF_ASSUMED, ordno, orddt, auth_dept, auth_off, auth, wefd, weft, SUSP_ALLOW, HQ_OFF_CODE, note, IF_VISIBLE,ent_dept,ent_off,ent_auth,HQ_FIX,ACS, not_id) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, suspend.getEmpid());
            pst.setTimestamp(2, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(3, suspend.getToe());
            pst.setString(4, suspend.getIfAssumed());
            pst.setString(5, suspend.getOrdno());
            if (suspend.getOrdDate() != null && !suspend.getOrdDate().equals("")) {
                pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(suspend.getOrdDate()).getTime()));
            } else {
                pst.setTimestamp(6, null);
            }
            pst.setString(7, suspend.getSltDept());
            pst.setString(8, suspend.getHidTempAuthOffice());
            pst.setString(9, suspend.getHidTempAuthSpc());

            if (suspend.getWefdate() != null && !suspend.getWefdate().equals("")) {
                /*outputDate = dateFormat.parse(suspend.getWefdate());
                pst.setTimestamp(10, new Timestamp(outputDate.getTime()));*/
                pst.setTimestamp(10, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(suspend.getWefdate()).getTime()));
            } else {
                pst.setTimestamp(10, null);
            }
            pst.setString(11, suspend.getSltweftime());
            double d = 0;
            if (suspend.getTxtallowance() != null && !suspend.getTxtallowance().equals("")) {
                d = Double.parseDouble(suspend.getTxtallowance());
                pst.setDouble(12, d);
            } else {
                pst.setDouble(12, d);
            }
            pst.setString(13, suspend.getSlthqoffice());
            pst.setString(14, suspend.getNote());
            pst.setString(15, suspend.getChkNotSBPrint());
            pst.setString(16, suspend.getEntDept());
            pst.setString(17, suspend.getEntOffice());
            pst.setString(18, suspend.getEntAuth());
            pst.setString(19, null);
            pst.setString(20, suspend.getCadreStatus());
            pst.setInt(21, suspend.getNotId());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            rs.next();
            int suspenseionid = rs.getInt("sp_id");

            /*
             * Updating the Service Book Language
             */
            if (suspend.getChkNotSBPrint() != null && suspend.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE emp_suspension SET IF_VISIBLE='N' WHERE NOT_ID=?");
                pst.setInt(1, suspend.getNotId());
                pst.execute();
            } else if (suspend.getChkNotSBPrint() == null || suspend.getChkNotSBPrint().equals("")) {
                String sbLang = sbDAO.getSuspDetails(suspend);
                pst = con.prepareStatement("UPDATE emp_suspension SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                pst.setString(1, sbLang);
                pst.setInt(2, suspend.getNotId());
                pst.execute();
            }

            String serverDate = dateFormat.format(new Date());

            /*if (serverDate != null && !serverDate.trim().equals("")) {
             boolean updatepay = empinfoDAO.isupdatePayOrPostingInfo(suspend.getEmpid(), suspend.getWefdate(), suspend.getOrdDate(), "PAY");
             if (updatepay == true) {
             pst = con.prepareStatement("UPDATE EMP_MAST SET DEP_CODE='05' WHERE EMP_ID=?");
             pst.setString(1, suspend.getEmpid());
             pst.execute();
             }
             }*/
            if (suspend.getRdTransaction() != null && suspend.getRdTransaction().equals("C")) {
                pst = con.prepareStatement("UPDATE EMP_MAST SET DEP_CODE='05' WHERE EMP_ID=?");
                pst.setString(1, suspend.getEmpid());
                pst.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public int updateSuspensionData(Suspension suspend) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        Date outputDate = null;

        try {
            con = this.dataSource.getConnection();

            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            
            pst = con.prepareStatement("UPDATE emp_suspension SET ordno=?, orddt=?, auth_dept=?, auth_off=?, auth=?, wefd=?, weft=?, SUSP_ALLOW=?, HQ_OFF_CODE=?, note=?, IF_VISIBLE=?, ent_dept=?,ent_off=?,ent_auth=?,HQ_FIX=?,ACS=? WHERE sp_id=?");
            pst.setString(1, suspend.getOrdno());
            if (suspend.getOrdDate() != null && !suspend.getOrdDate().equals("")) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(suspend.getOrdDate()).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            pst.setString(3, suspend.getSltDept());
            pst.setString(4, suspend.getHidTempAuthOffice());
            pst.setString(5, suspend.getHidTempAuthSpc());
            if (suspend.getWefdate() != null && !suspend.getWefdate().equals("")) {
                pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(suspend.getWefdate()).getTime()));
            } else {
                pst.setTimestamp(6, null);
            }
            pst.setString(7, suspend.getSltweftime());
            double d = 0;
            if (suspend.getTxtallowance() != null && !suspend.getTxtallowance().equals("")) {
                d = Double.parseDouble(suspend.getTxtallowance());
                pst.setDouble(8, d);
            } else {
                pst.setDouble(8, d);
            }
            pst.setString(9, suspend.getSlthqoffice());
            pst.setString(10, suspend.getNote());
            pst.setString(11, suspend.getIfVisible());
            pst.setString(12, suspend.getEntDept());
            pst.setString(13, suspend.getEntOffice());
            pst.setString(14, suspend.getEntAuth());
            pst.setString(15, null);
            pst.setString(16, suspend.getCadreStatus());
            pst.setInt(17, Integer.parseInt(suspend.getSuspensionId()));
            n = pst.executeUpdate();

            /*
             * Updating the Service Book Language
             */
            if (suspend.getChkNotSBPrint() != null && suspend.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE emp_suspension SET IF_VISIBLE='N' WHERE NOT_ID=?");
                pst.setInt(1, suspend.getNotId());
                pst.execute();
            } else if (suspend.getChkNotSBPrint() == null || suspend.getChkNotSBPrint().equals("")) {
                String sbLang = sbDAO.getSuspDetails(suspend);
                pst = con.prepareStatement("UPDATE emp_suspension SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=? ");
                pst.setString(1, sbLang);
                pst.setInt(2, suspend.getNotId());
                pst.execute();
            }
            
            if (suspend.getRdTransaction() != null && suspend.getRdTransaction().equals("C")) {
                pst = con.prepareStatement("UPDATE EMP_MAST SET DEP_CODE='05' WHERE EMP_ID=?");
                pst.setString(1, suspend.getEmpid());
                pst.execute();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public int deleteSuspension(String suspendId) {
        int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM EMP_SUSPENSION WHERE SP_ID =?");
            pst.setInt(1, Integer.parseInt(suspendId));
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public Suspension editSuspension(String suspendId) {
        Suspension suspend = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT not_id,emp_id, doe, TOE, IF_ASSUMED, ordno, orddt, auth_dept, auth_off, auth, wefd, weft, SUSP_ALLOW, HQ_OFF_CODE, note, IF_VISIBLE,ent_dept,ent_off,ent_auth,HQ_FIX,ACS FROM emp_suspension WHERE sp_id=?");
            pst.setInt(1, Integer.parseInt(suspendId));
            rs = pst.executeQuery();
            if (rs.next()) {
                suspend = new Suspension();
                suspend.setEmpid(rs.getString("EMP_ID"));
                suspend.setDoe(rs.getString("DOE"));
                suspend.setToe(rs.getString("TOE"));
                suspend.setIfAssumed(rs.getString("IF_ASSUMED"));
                suspend.setOrdno(rs.getString("ORDNO"));
                suspend.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                suspend.setSltDept(rs.getString("auth_dept"));
                suspend.setSltOffice(rs.getString("auth_off"));
                suspend.setHidTempAuthOffice(rs.getString("auth_off"));
                suspend.setSpc(rs.getString("AUTH"));
                suspend.setHidTempAuthSpc(rs.getString("AUTH"));
                suspend.setSltAuth(CommonFunctions.getSPN(con, rs.getString("auth")));
                suspend.setWefdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("wefd")));
                suspend.setSltweftime(rs.getString("weft"));
                suspend.setTxtallowance(rs.getString("SUSP_ALLOW"));
                suspend.setSlthqDeaprtment(getDepartmentFromOffice(rs.getString("HQ_OFF_CODE")));
                suspend.setSlthqoffice(rs.getString("HQ_OFF_CODE"));
                suspend.setHidTempHeadQuarterOffice(rs.getString("HQ_OFF_CODE"));
                suspend.setNote(rs.getString("NOTE"));
                if(rs.getString("IF_VISIBLE") != null && rs.getString("IF_VISIBLE").equals("N")){
                    suspend.setChkNotSBPrint("Y");
                }else{
                    suspend.setChkNotSBPrint("");
                }
                suspend.setEntDept(rs.getString("ENT_DEPT"));
                suspend.setEntOffice(rs.getString("ENT_OFF"));
                suspend.setEntAuth(rs.getString("ENT_AUTH"));
                suspend.setCadreStatus(rs.getString("ACS"));
                suspend.setNotId(rs.getInt("not_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return suspend;
    }

    private String getDepartmentFromOffice(String offCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String dept = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select department_code from g_office where off_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                dept = rs.getString("department_code");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return dept;
    }
}
