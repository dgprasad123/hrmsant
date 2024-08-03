package hrms.dao.termination;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.termination.Termination;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class TerminationDAOImpl implements TerminationDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int insertPayrevisionData(Termination term) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            String mcode = CommonFunctions.getMaxCode("EMP_NOTIFICATION", "NOT_ID", con);
            pst = con.prepareStatement("INSERT INTO EMP_NOTIFICATION(NOT_ID,NOT_TYPE,EMP_ID,DOE,TOE,IF_ASSUMED,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,NOTE,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH,ACS,ASCS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, mcode);
            pst.setString(2, "TERMINATION");
            pst.setString(3, term.getEmpid());
            pst.setString(4, term.getDoe());
            pst.setString(5, term.getToe());
            pst.setString(6, term.getIfAssumed());
            pst.setString(7, term.getOrdno());
            pst.setString(8, term.getOrdDate());
            pst.setString(9, term.getSltDept());
            pst.setString(10, term.getSltOffice());
            pst.setString(11, term.getSltAuth());
            pst.setString(12, term.getNote());
            pst.setString(13, term.getIfVisible());
            pst.setString(14, term.getEntDept());
            pst.setString(15, term.getEntOffice());
            pst.setString(16, term.getEntAuth());
            pst.setString(17, term.getCadreStatus());
            pst.setString(18, term.getSubStatus());
            n = pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public int updateTerminationData(Termination term) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE  EMP_NOTIFICATION SET NOT_TYPE=?,EMP_ID=?,DOE=?,TOE=?,IF_ASSUMED=?,ORDNO=?,ORDDT=?,DEPT_CODE=?,OFF_CODE=?,AUTH,NOTE=?,IF_VISIBLE=?,ENT_DEPT=?,ENT_OFF=?,ENT_AUTH=?,ACS=?,ASCS=? WHERE NOT_ID=?");
            pst.setString(1, "TERMINATION");
            pst.setString(2, term.getEmpid());
            pst.setString(3, term.getDoe());
            pst.setString(4, term.getToe());
            pst.setString(5, term.getIfAssumed());
            pst.setString(6, term.getOrdno());
            pst.setString(7, term.getOrdDate());
            pst.setString(8, term.getSltDept());
            pst.setString(9, term.getSltOffice());
            pst.setString(10, term.getSltAuth());
            pst.setString(11, term.getNote());
            pst.setString(12, term.getIfVisible());
            pst.setString(13, term.getEntDept());
            pst.setString(14, term.getEntOffice());
            pst.setString(15, term.getEntAuth());
            pst.setString(16, term.getCadreStatus());
            pst.setString(17, term.getSubStatus());
            pst.setString(18, term.getNotId());
            n = pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public int deleteTermination(String terminationId) {
        int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            
            if(terminationId != null && !terminationId.equals("")){
                pst = con.prepareStatement("DELETE FROM EMP_NOTIFICATION WHERE NOT_ID=?");
                pst.setInt(1, Integer.parseInt(terminationId));
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public Termination editTermination(String terminationId) {
        Termination term = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT NOT_TYPE,EMP_ID,DOE,TOE,IF_ASSUMED,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,NOTE,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH,ACS,ASCS FROM EMP_NOTIFICATION WHERE NOT_ID=?");
            pst.setString(1, terminationId);
            rs = pst.executeQuery();
            if (rs.next()) {
                term = new Termination();
                term.setNotType("TERMINATION");
                term.setEmpid(rs.getString("EMP_ID"));
                term.setDoe(rs.getString("DOE"));
                term.setToe(rs.getString("TOE"));
                term.setIfAssumed(rs.getString("IF_ASSUMED"));
                term.setOrdno(rs.getString("ORDNO"));
                term.setOrdDate(rs.getString("ORDDT"));
                term.setSltDept(rs.getString("DEPT_CODE"));
                term.setSltOffice(rs.getString("OFF_CODE"));
                term.setSltAuth(rs.getString("AUTH"));
                term.setNote(rs.getString("NOTE"));
                term.setIfVisible(rs.getString("IF_VISIBLE"));
                term.setEntDept(rs.getString("ENT_DEPT"));
                term.setEntOffice(rs.getString("ENT_OFF"));
                term.setEntAuth(rs.getString("ENT_AUTH"));
                term.setCadreStatus(rs.getString("ACS"));
                term.setSubStatus(rs.getString("ASCS"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return term;
    }

    @Override
    public Termination getEmployeeTerminationData(String empId) {
        Termination termination = new Termination();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            termination.setNotType("GOO");
            termination.setRetauthtyp("GOO");
            termination.setRetired("Y");
            pst = con.prepareStatement("SELECT ER.dos,ER.doe, ER.RET_ID,ER.RE_DEPT, ER.RE_OFF, ER.RE_SPC, ER.not_id"
                    + ",ord_no,ord_date, ER.AUTH_DEPT, ER.AUTH_OFF, ER.AUTH, ER.NOTE"
                    + " FROM emp_ret_res ER"
                    + " WHERE ER.EMP_ID=? AND ER.ret_type = 'TR'");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            if (rs.next()) {
                termination.setCurspc(rs.getString("RE_SPC"));
                termination.setRetid(rs.getString("RET_ID"));
                termination.setRetiredfromdeptCode(rs.getString("RE_DEPT"));
                termination.setRetiredfromofficeCode(rs.getString("RE_OFF"));
                termination.setRetiredfromspc(rs.getString("RE_SPC"));
                termination.setAuthDeptCode(rs.getString("AUTH_DEPT"));
                termination.setAuthOfficeCode(rs.getString("AUTH_OFF"));
                termination.setAuthSpc(rs.getString("AUTH"));
                termination.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORD_DATE")));
                termination.setOrdno(rs.getString("ORD_NO"));
                termination.setDuedate(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                termination.setNote(rs.getString("NOTE"));
            }
            if (termination.getRetid() == null) {
                pst = con.prepareStatement("SELECT CUR_SPC,dept_code,off_code FROM EMP_MAST INNER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC WHERE EMP_ID=?");
                pst.setString(1, empId);
                rs = pst.executeQuery();
                if (rs.next()) {
                    termination.setRetiredfromdeptCode(rs.getString("dept_code"));
                    termination.setRetiredfromofficeCode(rs.getString("off_code"));
                    termination.setRetiredfromspc(rs.getString("CUR_SPC"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
        return termination;
    }

    @Override
    public void saveEmployeeTermination(Termination termination) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("INSERT INTO emp_ret_res (emp_id, doe, TOE, IF_ASSUMED, ret_type, dos, retired, ORD_NO, ORD_DATE, AUTH_DEPT, AUTH_OFF, AUTH, RE_DEPT, RE_OFF, RE_SPC, NOTE, ent_dept, ent_off, ent_auth) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            
            pst.setString(1, termination.getRetiredEmpid());
            pst.setDate(2, new java.sql.Date(new Date().getTime()));
            pst.setString(3, termination.getToe());
            pst.setString(4, termination.getIfAssumed());
            pst.setString(5, "TR");
            if(termination.getDuedate() != null && !termination.getDuedate().equals("")){
                pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(termination.getDuedate()).getTime()));
            }else{
                pst.setTimestamp(6, null);
            }
            pst.setString(7, termination.getRetired());
            pst.setString(8, termination.getOrdno().toUpperCase());
            pst.setDate(9, new java.sql.Date(CommonFunctions.getDateFromString(termination.getOrdDate(), "dd-MMM-yyyy").getTime()));
            pst.setString(10, termination.getAuthDeptCode());
            pst.setString(11, termination.getAuthOfficeCode());
            pst.setString(12, termination.getAuthSpc());

            pst.setString(13, termination.getRetiredfromdeptCode());
            pst.setString(14, termination.getRetiredfromofficeCode());
            pst.setString(15, termination.getRetiredfromspc());
            pst.setString(16, termination.getNote());

            pst.setString(17, termination.getEntDeptCode());
            pst.setString(18, termination.getEntOfficeCode());
            pst.setString(19, termination.getEntAuthCode());
            pst.executeUpdate();
            
            if(termination.getRdTransaction() != null && termination.getRdTransaction().equals("C")){
                pst = con.prepareStatement("UPDATE EMP_MAST SET dep_code='00', CUR_SPC=null WHERE EMP_ID=?");
                pst.setString(1, termination.getRetiredEmpid());
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
    public void deleteEmployeeTermination(Termination termi) {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String terminatedspc = "";
        
        try{
            con = this.dataSource.getConnection();
            
            String sql = "select RE_SPC,ret_type from emp_ret_res where emp_id=? and ret_type='TR'";
            pst = con.prepareStatement(sql);
            pst.setString(1,termi.getRetiredEmpid());
            rs = pst.executeQuery();
            if(rs.next()){
                terminatedspc = rs.getString("RE_SPC");
            }
            
            DataBaseFunctions.closeSqlObjects(rs,pst);
            
            if(!terminatedspc.equals("")){
                sql = "delete from emp_ret_res where emp_id=? and ret_type='TR'";
                pst = con.prepareStatement(sql);
                pst.setString(1,termi.getRetiredEmpid());
                int retval = pst.executeUpdate();
                
                DataBaseFunctions.closeSqlObjects(rs,pst);
                
                if(retval > 0){
                    sql = "update emp_mast set cur_spc=?,dep_code='02' where emp_id=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1,terminatedspc);
                    pst.setString(2,termi.getRetiredEmpid());
                    pst.executeUpdate();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}
