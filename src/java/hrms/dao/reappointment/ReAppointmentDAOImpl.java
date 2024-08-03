package hrms.dao.reappointment;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.model.reappointment.ReAppointmentBean;
import hrms.model.reappointment.ReAppointmentForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class ReAppointmentDAOImpl implements ReAppointmentDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected ServiceBookLanguageDAO sbDAO;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    @Override
    public ReAppointmentForm getEmployeeReAppointmentData(String empId) {

        ReAppointmentForm reAppointmentForm = new ReAppointmentForm();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();

            reAppointmentForm.setNotType("GOO");
            pst = con.prepareStatement("SELECT POST,ER.dos,ER.doe, ER.RET_ID,ER.RE_DEPT, ER.RE_OFF, ER.RE_SPC, ER.not_id,ORDNO,ORDDT,EN.DEPT_CODE"
                    + ",EN.NOTE,EN.OFF_CODE,EN.AUTH FROM emp_ret_res ER INNER JOIN emp_notification EN ON ER.not_id = EN.not_id"
                    + " LEFT OUTER JOIN G_SPC ON ER.RE_SPC=G_SPC.SPC"
                    + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
                    + " WHERE ER.EMP_ID=? AND EN.not_type = 'REAPPOINTMENT'");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            if (rs.next()) {
                reAppointmentForm.setCurspc(rs.getString("RE_SPC"));
                reAppointmentForm.setReappointmentid(rs.getString("RET_ID"));
                reAppointmentForm.setRetiredfromdeptCode(rs.getString("RE_DEPT"));
                reAppointmentForm.setRetiredfromofficeCode(rs.getString("RE_OFF"));
                reAppointmentForm.setReappointedFromPost(rs.getString("RE_SPC"));
                reAppointmentForm.setReappointedFromPostName(rs.getString("POST"));
                reAppointmentForm.setAuthDeptCode(rs.getString("DEPT_CODE"));
                reAppointmentForm.setAuthOfficeCode(rs.getString("OFF_CODE"));
                reAppointmentForm.setAuthSpc(rs.getString("AUTH"));
                reAppointmentForm.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                reAppointmentForm.setOrdno(rs.getString("ORDNO"));
                reAppointmentForm.setDuedate(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                reAppointmentForm.setNote(rs.getString("NOTE"));
            }
            if (reAppointmentForm.getReappointmentid() == null) {
                pst = con.prepareStatement("SELECT POST,CUR_SPC,dept_code,off_code FROM EMP_MAST INNER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE WHERE EMP_ID=?");
                pst.setString(1, empId);
                rs = pst.executeQuery();
                if (rs.next()) {
                    reAppointmentForm.setRetiredfromdeptCode(rs.getString("dept_code"));
                    reAppointmentForm.setRetiredfromofficeCode(rs.getString("off_code"));
                    reAppointmentForm.setReappointedFromPost(rs.getString("CUR_SPC"));
                    reAppointmentForm.setReappointedFromPostName(rs.getString("POST"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
        return reAppointmentForm;
    }

    @Override
    public List getReAppointmentList(String empId) {

        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        ReAppointmentForm reapp = null;
        List reappointmentList = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select empnotification.not_id,empnotification.not_type,empnotification.emp_Id,emp_ret_res.doe,ordno,orddt from"
                    + " (select * from emp_notification where not_type='REAPPOINTMENT' and emp_id=?)empnotification"
                    + " inner join emp_ret_res on empnotification.not_id=emp_ret_res.not_id"
                    + " inner join emp_relieve on empnotification.not_id=emp_relieve.not_id");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                reapp = new ReAppointmentForm();
                reapp.setNotId(rs.getString("not_id"));
                reapp.setOrdno(rs.getString("ordno"));
                reapp.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                reappointmentList.add(reapp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return reappointmentList;
    }

    @Override
    public void saveEmployeeReAppointment(ReAppointmentForm reapp) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("INSERT INTO EMP_NOTIFICATION(NOT_TYPE,EMP_ID,DOE,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,NOTE,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, "REAPPOINTMENT");
            pst.setString(2, reapp.getReappointedEmpid());
            pst.setDate(3, new java.sql.Date(new Date().getTime()));
            pst.setString(4, reapp.getOrdno().toUpperCase());
            if (reapp.getOrdDate() != null && !reapp.getOrdDate().equals("")) {
                pst.setDate(5, new java.sql.Date(CommonFunctions.getDateFromString(reapp.getOrdDate(), "dd-MMM-yyyy").getTime()));
            } else {
                pst.setDate(5, null);
            }
            pst.setString(6, reapp.getAuthDeptCode());
            pst.setString(7, reapp.getAuthOfficeCode());
            pst.setString(8, reapp.getAuthSpc());
            pst.setString(9, reapp.getNote());
            pst.setString(10, "N");
            pst.setString(11, reapp.getEntDeptCode());
            pst.setString(12, reapp.getEntOfficeCode());
            pst.setString(13, reapp.getEntAuthCode());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int notId = rs.getInt("NOT_ID");

            pst = con.prepareStatement("INSERT INTO emp_ret_res (emp_id, doe,ret_type, dos, ORD_NO, ORD_DATE, AUTH_DEPT, AUTH_OFF, AUTH, RE_DEPT, RE_OFF, RE_SPC, NOTE, ent_dept, ent_off, ent_auth, not_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, reapp.getReappointedEmpid());
            pst.setDate(2, new java.sql.Date(new Date().getTime()));
            pst.setString(3, reapp.getRegtype());
            if (reapp.getDuedate() != null && !reapp.getDuedate().equals("")) {
                pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(reapp.getDuedate()).getTime()));
            } else {
                pst.setTimestamp(4, null);
            }
            pst.setString(5, reapp.getOrdno().toUpperCase());
            pst.setDate(6, new java.sql.Date(CommonFunctions.getDateFromString(reapp.getOrdDate(), "dd-MMM-yyyy").getTime()));
            pst.setString(7, reapp.getAuthDeptCode());
            pst.setString(8, reapp.getAuthOfficeCode());
            pst.setString(9, reapp.getAuthSpc());
            pst.setString(10, reapp.getRetiredfromdeptCode());
            pst.setString(11, reapp.getRetiredfromofficeCode());
            pst.setString(12, reapp.getReappointedFromPost());
            pst.setString(13, reapp.getNote());
            pst.setString(14, reapp.getEntDeptCode());
            pst.setString(15, reapp.getEntOfficeCode());
            pst.setString(16, reapp.getEntAuthCode());
            pst.setInt(17, notId);
            pst.executeUpdate();

            //String rid = CommonFunctions.getMaxCode("emp_relieve", "RELIEVE_ID", con);
            pst = con.prepareStatement("INSERT INTO emp_relieve(NOT_TYPE, NOT_ID, EMP_ID, DOE, MEMO_NO, MEMO_DATE, RLV_DATE, RLV_TIME, SPC, IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH) values (?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, "REAPPOINTMENT");
            pst.setInt(2, notId);
            pst.setString(3, reapp.getReappointedEmpid());
            pst.setDate(4, new java.sql.Date(new Date().getTime()));
            pst.setString(5, reapp.getOrdno().toUpperCase());
            if (reapp.getOrdDate() != null && !reapp.getOrdDate().equals("")) {
                pst.setDate(6, new java.sql.Date(CommonFunctions.getDateFromString(reapp.getOrdDate(), "dd-MMM-yyyy").getTime()));
            } else {
                pst.setDate(6, null);
            }
            if (reapp.getDuedate() != null && !reapp.getDuedate().equals("")) {
                pst.setDate(7, new java.sql.Date(CommonFunctions.getDateFromString(reapp.getDuedate(), "dd-MMM-yyyy").getTime()));
            } else {
                pst.setDate(7, null);
            }
            pst.setString(8, "AN");
            pst.setString(9, reapp.getReappointedFromPost());
            pst.setString(10, "Y");
            pst.setString(11, reapp.getEntDeptCode());
            pst.setString(12, reapp.getEntOfficeCode());
            pst.setString(13, reapp.getEntAuthCode());
            pst.executeUpdate();

            rs = pst.getGeneratedKeys();
            rs.next();
            int rid = rs.getInt("RELIEVE_ID");

            String sbLanguage = sbDAO.getRelieveDetails(notId, reapp.getReappointedEmpid());
            pst = con.prepareStatement("UPDATE EMP_RELIEVE SET SB_DESCRIPTION=?,if_visible='Y' WHERE RELIEVE_ID=?");
            pst.setString(1, sbLanguage);
            pst.setInt(2, rid);
            pst.executeUpdate();

            if (reapp.getRdTransaction() != null && reapp.getRdTransaction().equals("C")) {
                pst = con.prepareStatement("UPDATE EMP_MAST SET dep_code='15', CUR_SPC=null WHERE EMP_ID=?");
                pst.setString(1, reapp.getReappointedEmpid());
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}
