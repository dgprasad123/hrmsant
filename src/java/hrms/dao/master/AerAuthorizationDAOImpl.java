/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.AerAuthorization.AerAuthorizationBean;
import hrms.model.AerAuthorization.AerAuthorizationInnerBean;
import hrms.model.discProceeding.IoBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author manisha
 */
public class AerAuthorizationDAOImpl implements AerAuthorizationDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public AerAuthorizationBean getProcessAuthorization(String financialyear, String offCode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        AerAuthorizationBean aerAuthorizationBean = new AerAuthorizationBean();
        try {
            con = dataSource.getConnection();
            /*pst = con.prepareStatement("select sl_no,process_id,role_type,process_authorization.SPC,SPN,hrms_id,is_locked,SPN,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME from process_authorization "
             + "INNER JOIN EMP_MAST ON  process_authorization.hrms_id = EMP_MAST.emp_id "
             + "INNER JOIN G_SPC ON process_authorization.SPC = G_SPC.SPC "
             + "where financial_year=? AND process_authorization.off_code=?");
             pst.setString(1, financialyear);
             pst.setString(2, offCode);
             rs = pst.executeQuery();*/
            pst = con.prepareStatement("select (SELECT SPC FROM EMP_JOIN WHERE EMP_ID=process_authorization.HRMS_ID AND IF_AD_CHARGE='Y' order by join_date desc limit 1)ADDITIONALSPC,cur_spc,process_authorization.SPC,sl_no,process_authorization.process_id,role_type,process_authorization.SPC,SPN,hrms_id,is_locked,SPN,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME from process_authorization"
                    + " INNER JOIN EMP_MAST ON process_authorization.hrms_id = EMP_MAST.emp_id"
                    + " INNER JOIN g_workflow_process ON process_authorization.process_id= g_workflow_process.process_id"
                    + " INNER JOIN G_SPC ON process_authorization.SPC = G_SPC.SPC"
                    //+ " where process_authorization.off_code=?");
                    + " where EMP_MAST.cur_off_code=?");
            pst.setString(1, offCode);
            rs = pst.executeQuery();
            aerAuthorizationBean.setFinancialyear(financialyear);
            while (rs.next()) {
                if (rs.getString("role_type").equalsIgnoreCase("OPERATOR") && rs.getString("process_id").equals("13")) {
                    aerAuthorizationBean.setOperator(rs.getString("hrms_id") + "-" + rs.getString("SPC"));
                    if (rs.getString("cur_spc") != null && rs.getString("cur_spc").equals(rs.getString("SPC"))) {
                        aerAuthorizationBean.setOperatorname(rs.getString("EMP_NAME") + ", " + rs.getString("SPN"));
                    } else {
                        aerAuthorizationBean.setOperatorname("");
                    }
                    if(rs.getString("ADDITIONALSPC") != null && rs.getString("ADDITIONALSPC").equals(rs.getString("SPC"))){
                        aerAuthorizationBean.setOperatorname(rs.getString("EMP_NAME") + ", " + rs.getString("SPN"));
                    }
                } else if (rs.getString("role_type").equalsIgnoreCase("APPROVER") && rs.getString("process_id").equals("13")) {
                    aerAuthorizationBean.setApprover(rs.getString("hrms_id") + "-" + rs.getString("SPC"));
                    if (rs.getString("cur_spc") != null && rs.getString("cur_spc").equals(rs.getString("SPC"))) {
                        aerAuthorizationBean.setApprovername(rs.getString("EMP_NAME") + ", " + rs.getString("SPN"));
                    } else {
                        aerAuthorizationBean.setApprovername("");
                    }
                } else if (rs.getString("role_type").equalsIgnoreCase("REVIEWER") && rs.getString("process_id").equals("13")) {
                    /*aerAuthorizationBean.setReviewer(rs.getString("hrms_id") + "-" + rs.getString("SPC"));
                     if (rs.getString("cur_spc") != null && rs.getString("cur_spc").equals(rs.getString("SPC"))) {
                     aerAuthorizationBean.setReviewername(rs.getString("EMP_NAME") + ", " + rs.getString("SPN"));
                     } else {
                     aerAuthorizationBean.setReviewername("");
                     }*/
                    aerAuthorizationBean.setReviewerlist(getAuthorityList(offCode, rs.getString("role_type")));
                } else if (rs.getString("role_type").equalsIgnoreCase("VERIFIER") && rs.getString("process_id").equals("13")) {
                    /*aerAuthorizationBean.setVerifier(rs.getString("hrms_id") + "-" + rs.getString("SPC"));
                     if (rs.getString("cur_spc") != null && rs.getString("cur_spc").equals(rs.getString("SPC"))) {
                     aerAuthorizationBean.setVerifiername(rs.getString("EMP_NAME") + ", " + rs.getString("SPN"));
                     } else {
                     aerAuthorizationBean.setVerifiername("");
                     }*/
                    aerAuthorizationBean.setVerifierlist(getAuthorityList(offCode, rs.getString("role_type")));
                } else if (rs.getString("role_type").equalsIgnoreCase("ACCEPTOR") && rs.getString("process_id").equals("13")) {
                    aerAuthorizationBean.setAcceptor(rs.getString("hrms_id") + "-" + rs.getString("SPC"));
                    if (rs.getString("cur_spc") != null && rs.getString("cur_spc").equals(rs.getString("SPC"))) {
                        aerAuthorizationBean.setAcceptorname(rs.getString("EMP_NAME") + ", " + rs.getString("SPN"));
                    } else {
                        aerAuthorizationBean.setAcceptorname("");
                    }
                } else if (rs.getString("role_type").equalsIgnoreCase("DREVIEWER") && rs.getString("process_id").equals("13")) {
                    /*aerAuthorizationBean.setDreviewer(rs.getString("hrms_id") + "-" + rs.getString("SPC"));
                     if (rs.getString("cur_spc") != null && rs.getString("cur_spc").equals(rs.getString("SPC"))) {
                     aerAuthorizationBean.setDreviewername(rs.getString("EMP_NAME") + ", " + rs.getString("SPN"));
                     } else {
                     aerAuthorizationBean.setDreviewername("");
                     }*/
                    aerAuthorizationBean.setDreviewerlist(getAuthorityList(offCode, rs.getString("role_type")));
                } else if (rs.getString("role_type").equalsIgnoreCase("DVERIFIER") && rs.getString("process_id").equals("13")) {
                    /*aerAuthorizationBean.setDverifier(rs.getString("hrms_id") + "-" + rs.getString("SPC"));
                     if (rs.getString("cur_spc") != null && rs.getString("cur_spc").equals(rs.getString("SPC"))) {
                     aerAuthorizationBean.setDverifiername(rs.getString("EMP_NAME") + ", " + rs.getString("SPN"));
                     } else {
                     aerAuthorizationBean.setDverifiername("");
                     }*/
                    aerAuthorizationBean.setDverifierlist(getAuthorityList(offCode, rs.getString("role_type")));
                } else if (rs.getString("role_type").equalsIgnoreCase("APPROVER") && rs.getString("process_id").equals("14")) {
                    aerAuthorizationBean.setProfileApprover(rs.getString("hrms_id") + "-" + rs.getString("SPC"));
                    if (rs.getString("cur_spc") != null && rs.getString("cur_spc").equals(rs.getString("SPC"))) {
                        aerAuthorizationBean.setProfileApprovername(rs.getString("EMP_NAME") + ", " + rs.getString("SPN"));
                    } else {
                        aerAuthorizationBean.setProfileApprovername("");
                    }
                } else if (rs.getString("role_type").equalsIgnoreCase("SERVICE BOOK VALIDATOR") && rs.getString("process_id").equals("22")) {
                    /*aerAuthorizationBean.setServiceBookValidator(rs.getString("hrms_id") + "-" + rs.getString("SPC"));
                     if (rs.getString("cur_spc") != null && rs.getString("cur_spc").equals(rs.getString("SPC"))) {
                     aerAuthorizationBean.setValidatorName(rs.getString("EMP_NAME") + ", " + rs.getString("SPN"));
                     } else {
                     aerAuthorizationBean.setValidatorName("");
                     }*/
                    aerAuthorizationBean.setValidatorlist(getAuthorityList(offCode, rs.getString("role_type")));
                } else if (rs.getString("role_type").equalsIgnoreCase("SERVICE BOOK ENTRY") && rs.getString("process_id").equals("23")) {
                    /* aerAuthorizationBean.setServiceBookentry(rs.getString("hrms_id") + "-" + rs.getString("SPC"));
                     if (rs.getString("cur_spc") != null && rs.getString("cur_spc").equals(rs.getString("SPC"))) {
                     aerAuthorizationBean.setServiceentryName(rs.getString("EMP_NAME") + ", " + rs.getString("SPN"));
                     } else {
                     aerAuthorizationBean.setServiceentryName("");
                     }*/
                    aerAuthorizationBean.setServiceBookentrylist(getAuthorityList(offCode, rs.getString("role_type")));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aerAuthorizationBean;
    }

    private ArrayList getAuthorityList(String offcode, String roletype) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList authlist = new ArrayList();

        AerAuthorizationInnerBean authinnerbean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select cur_spc,process_authorization.SPC,sl_no,process_authorization.process_id,role_type,process_authorization.SPC,SPN,hrms_id,is_locked,SPN,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME from process_authorization"
                    + " INNER JOIN EMP_MAST ON  process_authorization.hrms_id = EMP_MAST.emp_id"
                    + " INNER JOIN g_workflow_process ON process_authorization.process_id= g_workflow_process.process_id"
                    + " INNER JOIN G_SPC ON process_authorization.SPC = G_SPC.SPC"
                    + " where process_authorization.off_code=? and role_type=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            pst.setString(2, roletype);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getString("role_type").equalsIgnoreCase("REVIEWER") && rs.getString("process_id").equals("13")) {
                    authinnerbean = new AerAuthorizationInnerBean();
                    authinnerbean.setReviewer(rs.getString("hrms_id") + "-" + rs.getString("SPC"));
                    if (rs.getString("cur_spc") != null && rs.getString("cur_spc").equals(rs.getString("SPC"))) {
                        authinnerbean.setReviewername(rs.getString("EMP_NAME") + ", " + rs.getString("SPN"));
                    } else {
                        authinnerbean.setReviewername("");
                    }
                } else if (rs.getString("role_type").equalsIgnoreCase("VERIFIER") && rs.getString("process_id").equals("13")) {
                    authinnerbean = new AerAuthorizationInnerBean();
                    authinnerbean.setVerifier(rs.getString("hrms_id") + "-" + rs.getString("SPC"));
                    if (rs.getString("cur_spc") != null && rs.getString("cur_spc").equals(rs.getString("SPC"))) {
                        authinnerbean.setVerifiername(rs.getString("EMP_NAME") + ", " + rs.getString("SPN"));
                    } else {
                        authinnerbean.setVerifiername("");
                    }
                } else if (rs.getString("role_type").equalsIgnoreCase("DREVIEWER") && rs.getString("process_id").equals("13")) {
                    authinnerbean = new AerAuthorizationInnerBean();
                    authinnerbean.setDreviewer(rs.getString("hrms_id") + "-" + rs.getString("SPC"));
                    if (rs.getString("cur_spc") != null && rs.getString("cur_spc").equals(rs.getString("SPC"))) {
                        authinnerbean.setDreviewername(rs.getString("EMP_NAME") + ", " + rs.getString("SPN"));
                    } else {
                        authinnerbean.setDreviewername("");
                    }
                } else if (rs.getString("role_type").equalsIgnoreCase("DVERIFIER") && rs.getString("process_id").equals("13")) {
                    authinnerbean = new AerAuthorizationInnerBean();
                    authinnerbean.setDverifier(rs.getString("hrms_id") + "-" + rs.getString("SPC"));
                    if (rs.getString("cur_spc") != null && rs.getString("cur_spc").equals(rs.getString("SPC"))) {
                        authinnerbean.setDverifiername(rs.getString("EMP_NAME") + ", " + rs.getString("SPN"));
                    } else {
                        authinnerbean.setDverifiername("");
                    }
                } else if (rs.getString("role_type").equalsIgnoreCase("SERVICE BOOK VALIDATOR") && rs.getString("process_id").equals("22")) {
                    authinnerbean = new AerAuthorizationInnerBean();
                    authinnerbean.setServiceBookValidator(rs.getString("hrms_id") + "-" + rs.getString("SPC"));
                    if (rs.getString("cur_spc") != null && rs.getString("cur_spc").equals(rs.getString("SPC"))) {
                        authinnerbean.setValidatorName(rs.getString("EMP_NAME") + ", " + rs.getString("SPN"));
                    } else {
                        authinnerbean.setValidatorName("");
                    }
                } else if (rs.getString("role_type").equalsIgnoreCase("SERVICE BOOK ENTRY") && rs.getString("process_id").equals("23")) {
                    authinnerbean = new AerAuthorizationInnerBean();
                    authinnerbean.setServiceBookentry(rs.getString("hrms_id") + "-" + rs.getString("SPC"));
                    if (rs.getString("cur_spc") != null && rs.getString("cur_spc").equals(rs.getString("SPC"))) {
                        authinnerbean.setServiceentryName(rs.getString("EMP_NAME") + ", " + rs.getString("SPN"));
                    } else {
                        authinnerbean.setServiceentryName("");
                    }
                }

                authlist.add(authinnerbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return authlist;
    }

    @Override
    public ArrayList getProcessAuthorizationList(String financialyear, String offCode) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean flag = false;
        ArrayList aerauthorizationList = new ArrayList();
        try {
            con = dataSource.getConnection();
            /*pst = con.prepareStatement("select sl_no,process_name,role_type,hrms_id,is_locked,SPN,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME from process_authorization "
             + "INNER JOIN EMP_MAST ON  process_authorization.hrms_id = EMP_MAST.emp_id "
             + "INNER JOIN g_workflow_process ON process_authorization.process_id= g_workflow_process.process_id "
             + "INNER JOIN G_SPC ON process_authorization.SPC = G_SPC.SPC "
             + "where financial_year=? and process_authorization.off_code=?");
             pst.setString(1, financialyear);
             pst.setString(2, offCode);*/
            pst = con.prepareStatement("select (SELECT SPC FROM EMP_JOIN WHERE EMP_ID=process_authorization.HRMS_ID AND IF_AD_CHARGE='Y' AND IF_AD_CHARGE='Y' order by join_date desc limit 1)ADDITIONALSPC,process_authorization.SPC,emp_mast.cur_spc,sl_no,process_name,role_type,hrms_id,is_locked,SPN,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME from process_authorization "
                    + "INNER JOIN EMP_MAST ON process_authorization.hrms_id = EMP_MAST.emp_id "
                    + "INNER JOIN g_workflow_process ON process_authorization.process_id= g_workflow_process.process_id "
                    + "INNER JOIN G_SPC ON process_authorization.SPC = G_SPC.SPC "
                    + "where process_authorization.off_code=?");
            pst.setString(1, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                AerAuthorizationBean aerAuthorizationBean = new AerAuthorizationBean();
                aerAuthorizationBean.setSiNo(rs.getInt("sl_no"));
                aerAuthorizationBean.setProcessName(rs.getString("process_name"));
                aerAuthorizationBean.setRoletype(rs.getString("role_type"));
                aerAuthorizationBean.setHrmsid(rs.getString("hrms_id"));
                if (rs.getString("cur_spc") != null && !rs.getString("cur_spc").equals("")) {
                    if (rs.getString("cur_spc").equals(rs.getString("SPC"))) {
                        aerAuthorizationBean.setEmpname(rs.getString("EMP_NAME"));
                    } else {
                        aerAuthorizationBean.setEmpname("");
                    }
                }
                if (rs.getString("ADDITIONALSPC") != null && !rs.getString("ADDITIONALSPC").equals("")) {
                    aerAuthorizationBean.setEmpname(rs.getString("EMP_NAME"));
                }

                aerAuthorizationBean.setSpn(rs.getString("SPN"));
                aerAuthorizationBean.setIslocked(rs.getString("is_locked"));
                aerauthorizationList.add(aerAuthorizationBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aerauthorizationList;

    }

    @Override
    public void saveAerauthorisationAcceptorDetail(AerAuthorizationBean aerAuthorizationBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        //int sl_no = 0;
        try {
            con = dataSource.getConnection();
            //sl_no = CommonFunctions.getMaxCode(con, "process_authorization", "sl_no");
            String approver = aerAuthorizationBean.getAcceptor();//000001-ryrtyry
            String[] tApprover = approver.split("-");
            String rule15MemoQry = "INSERT INTO process_authorization (financial_year,process_id,role_type,hrms_id,spc,authority_type,off_code ) VALUES (?,?,?,?,?,?,?)";
            pstmt = con.prepareStatement(rule15MemoQry);
            //pstmt.setInt(1, sl_no);
            pstmt.setString(1, aerAuthorizationBean.getFinancialyear());
            pstmt.setInt(2, 13);
            pstmt.setString(3, "ACCEPTOR");
            pstmt.setString(4, tApprover[0]);
            pstmt.setString(5, tApprover[1]);
            pstmt.setString(6, aerAuthorizationBean.getAuthoritytype());
            pstmt.setString(7, aerAuthorizationBean.getOffCode());
            pstmt.executeUpdate();

            String[] dreviewerArr = aerAuthorizationBean.getDreviewer().split(",");
            if (dreviewerArr != null && dreviewerArr.length > 0) {
                for (int i = 0; i < dreviewerArr.length; i++) {
                    String dreviewer = dreviewerArr[i];
                    //sl_no = CommonFunctions.getMaxCode(con, "process_authorization", "sl_no");
                    //000001-ryrtyry
                    String[] tdreviewer = dreviewer.split("-");
                    rule15MemoQry = "INSERT INTO process_authorization (financial_year,process_id,role_type,hrms_id,spc,authority_type,off_code ) VALUES (?,?,?,?,?,?,?)";
                    pstmt = con.prepareStatement(rule15MemoQry);
                    //pstmt.setInt(1, sl_no);
                    pstmt.setString(1, aerAuthorizationBean.getFinancialyear());
                    pstmt.setInt(2, 13);
                    pstmt.setString(3, "DREVIEWER");
                    pstmt.setString(4, tdreviewer[0]);
                    pstmt.setString(5, tdreviewer[1]);
                    pstmt.setString(6, aerAuthorizationBean.getAuthoritytype());
                    pstmt.setString(7, aerAuthorizationBean.getOffCode());
                    pstmt.executeUpdate();
                }
            }

            String[] dverifierArr = aerAuthorizationBean.getDverifier().split(",");
            if (dverifierArr != null && dverifierArr.length > 0) {
                for (int i = 0; i < dverifierArr.length; i++) {
                    //sl_no = CommonFunctions.getMaxCode(con, "process_authorization", "sl_no");
                    String dverifier = dverifierArr[i];//000001-ryrtyry
                    String[] tDverifier = dverifier.split("-");
                    rule15MemoQry = "INSERT INTO process_authorization (financial_year,process_id,role_type,hrms_id,spc,authority_type,off_code ) VALUES (?,?,?,?,?,?,?)";
                    pstmt = con.prepareStatement(rule15MemoQry);
                    //pstmt.setInt(1, sl_no);
                    pstmt.setString(1, aerAuthorizationBean.getFinancialyear());
                    pstmt.setInt(2, 13);
                    pstmt.setString(3, "DVERIFIER");
                    pstmt.setString(4, tDverifier[0]);
                    pstmt.setString(5, tDverifier[1]);
                    pstmt.setString(6, aerAuthorizationBean.getAuthoritytype());
                    pstmt.setString(7, aerAuthorizationBean.getOffCode());
                    pstmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateAerauthorisationAcceptorDetail(AerAuthorizationBean aerAuthorizationBean) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int res = 0;
        String doCurSpc = null;
        int mcode = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "DELETE FROM process_authorization WHERE off_code=? and role_type='ACCEPTOR' and process_id=13";
            pst = con.prepareStatement(sql);
            pst.setString(1, aerAuthorizationBean.getOffCode());
            pst.executeUpdate();

            sql = "DELETE FROM process_authorization WHERE off_code=? and role_type='DREVIEWER' and process_id=13";
            pst = con.prepareStatement(sql);
            pst.setString(1, aerAuthorizationBean.getOffCode());
            pst.executeUpdate();

            sql = "DELETE FROM process_authorization WHERE off_code=? and role_type='DVERIFIER' and process_id=13";
            pst = con.prepareStatement(sql);
            pst.setString(1, aerAuthorizationBean.getOffCode());
            pst.executeUpdate();
            /*String approver = aerAuthorizationBean.getAcceptor();//000001-ryrtyry
             String[] tApprover = approver.split("-");

             String rule15MemoQry = "UPDATE process_authorization set hrms_id=?,spc=? WHERE off_code=? and role_type='ACCEPTOR' and process_id=13 AND is_locked='N'";
             pstmt = con.prepareStatement(rule15MemoQry);
             pstmt.setString(1, tApprover[0]);
             pstmt.setString(2, tApprover[1]);
             //pstmt.setString(3, aerAuthorizationBean.getFinancialyear());
             pstmt.setString(3, aerAuthorizationBean.getOffCode());
             pstmt.executeUpdate();

             String dreviewer = aerAuthorizationBean.getDreviewer();//000001-ryrtyry
             String[] tdreviewer = dreviewer.split("-");
             rule15MemoQry = "UPDATE process_authorization set hrms_id=?,spc=? WHERE off_code=? and role_type='DREVIEWER' and process_id=13 AND is_locked='N'";
             pstmt = con.prepareStatement(rule15MemoQry);
             pstmt.setString(1, tdreviewer[0]);
             pstmt.setString(2, tdreviewer[1]);
             //pstmt.setString(3, aerAuthorizationBean.getFinancialyear());
             pstmt.setString(3, aerAuthorizationBean.getOffCode());
             int updates = pstmt.executeUpdate();
             if (updates == 0) {
             int sl_no = CommonFunctions.getMaxCode(con, "process_authorization", "sl_no");
             dreviewer = aerAuthorizationBean.getDreviewer();//000001-ryrtyry
             tdreviewer = dreviewer.split("-");
             rule15MemoQry = "INSERT INTO process_authorization (sl_no,financial_year,process_id,role_type,hrms_id,spc,authority_type,off_code ) VALUES (?,?,?,?,?,?,?,?)";
             pstmt = con.prepareStatement(rule15MemoQry);
             pstmt.setInt(1, sl_no);
             pstmt.setString(2, aerAuthorizationBean.getFinancialyear());
             pstmt.setInt(3, 13);
             pstmt.setString(4, "DREVIEWER");
             pstmt.setString(5, tdreviewer[0]);
             pstmt.setString(6, tdreviewer[1]);
             pstmt.setString(7, aerAuthorizationBean.getAuthoritytype());
             pstmt.setString(8, aerAuthorizationBean.getOffCode());
             pstmt.executeUpdate();
             }

             String dverifier = aerAuthorizationBean.getDverifier();//000001-ryrtyry
             String[] tDverifier = dverifier.split("-");
             rule15MemoQry = "UPDATE process_authorization set hrms_id=?,spc=? WHERE off_code=? and role_type='DVERIFIER' and process_id=13 AND is_locked='N'";
             pstmt = con.prepareStatement(rule15MemoQry);
             pstmt.setString(1, tDverifier[0]);
             pstmt.setString(2, tDverifier[1]);
             //pstmt.setString(3, aerAuthorizationBean.getFinancialyear());
             pstmt.setString(3, aerAuthorizationBean.getOffCode());
             updates = pstmt.executeUpdate();
             if (updates == 0) {
             int sl_no = CommonFunctions.getMaxCode(con, "process_authorization", "sl_no");
             dverifier = aerAuthorizationBean.getDverifier();//000001-ryrtyry
             tDverifier = dverifier.split("-");
             rule15MemoQry = "INSERT INTO process_authorization (sl_no,financial_year,process_id,role_type,hrms_id,spc,authority_type,off_code ) VALUES (?,?,?,?,?,?,?,?)";
             pstmt = con.prepareStatement(rule15MemoQry);
             pstmt.setInt(1, sl_no);
             pstmt.setString(2, aerAuthorizationBean.getFinancialyear());
             pstmt.setInt(3, 13);
             pstmt.setString(4, "DVERIFIER");
             pstmt.setString(5, tDverifier[0]);
             pstmt.setString(6, tDverifier[1]);
             pstmt.setString(7, aerAuthorizationBean.getAuthoritytype());
             pstmt.setString(8, aerAuthorizationBean.getOffCode());
             pstmt.executeUpdate();
             }*/
        } catch (Exception e) {
            res = 0;
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void saveAerauthorisationReviewerDetail(AerAuthorizationBean aerAuthorizationBean) {

        Connection con = null;

        PreparedStatement delpst = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        //int sl_no = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "DELETE FROM process_authorization WHERE off_code=? and role_type='REVIEWER' and process_id=13";
            delpst = con.prepareStatement(sql);
            delpst.setString(1, aerAuthorizationBean.getOffCode());
            delpst.executeUpdate();

            sql = "DELETE FROM process_authorization WHERE off_code=? and role_type='VERIFIER' and process_id=13";
            delpst = con.prepareStatement(sql);
            delpst.setString(1, aerAuthorizationBean.getOffCode());
            delpst.executeUpdate();

            String[] reviewerArr = aerAuthorizationBean.getReviewer().split(",");
            if (reviewerArr != null && reviewerArr.length > 0) {
                for (int i = 0; i < reviewerArr.length; i++) {
                    String reviewer = reviewerArr[i];//000001-ryrtyry

                    //sl_no = CommonFunctions.getMaxCode(con, "process_authorization", "sl_no");
                    String[] tReviewer = reviewer.split("-");

                    String isDuplicate = isAuthorityDuplicate(aerAuthorizationBean.getOffCode(), "REVIEWER", tReviewer[0]);

                    String rule15MemoQry = "INSERT INTO process_authorization (financial_year,process_id,role_type,hrms_id,spc,authority_type,off_code ) VALUES (?,?,?,?,?,?,?)";
                    pstmt = con.prepareStatement(rule15MemoQry);
                    //pstmt.setInt(1, sl_no);
                    pstmt.setString(1, aerAuthorizationBean.getFinancialyear());
                    pstmt.setInt(2, aerAuthorizationBean.getProcessid());
                    pstmt.setString(3, "REVIEWER");
                    pstmt.setString(4, tReviewer[0]);
                    pstmt.setString(5, tReviewer[1]);
                    pstmt.setString(6, aerAuthorizationBean.getAuthoritytype());
                    pstmt.setString(7, aerAuthorizationBean.getOffCode());
                    if (isDuplicate.equals("N")) {
                        pstmt.executeUpdate();
                    }
                }
            }

            String[] verifierArr = aerAuthorizationBean.getVerifier().split(",");
            if (verifierArr != null && verifierArr.length > 0) {
                for (int i = 0; i < verifierArr.length; i++) {
                    String verifier = verifierArr[i];//000001-ryrtyry

                    String[] tVerifier = verifier.split("-");

                    String isDuplicate = isAuthorityDuplicate(aerAuthorizationBean.getOffCode(), "VERIFIER", tVerifier[0]);

                    //sl_no = CommonFunctions.getMaxCode(con, "process_authorization", "sl_no");
                    String rule15MemoQry = "INSERT INTO process_authorization (financial_year,process_id,role_type,hrms_id,spc ,authority_type,off_code ) VALUES (?,?,?,?,?,?,?)";
                    pstmt = con.prepareStatement(rule15MemoQry);
                    //pstmt.setInt(1, sl_no);
                    pstmt.setString(1, aerAuthorizationBean.getFinancialyear());
                    pstmt.setInt(2, aerAuthorizationBean.getProcessid());
                    pstmt.setString(3, "VERIFIER");
                    pstmt.setString(4, tVerifier[0]);
                    pstmt.setString(5, tVerifier[1]);
                    pstmt.setString(6, aerAuthorizationBean.getAuthoritytype());
                    pstmt.setString(7, aerAuthorizationBean.getOffCode());
                    if (isDuplicate.equals("N")) {
                        pstmt.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    private String isAuthorityDuplicate(String offcode, String roletype, String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isPresent = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from process_authorization where off_code=? and process_id=13 and role_type=? and hrms_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            pst.setString(2, roletype);
            pst.setString(3, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                isPresent = "Y";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isPresent;
    }

    @Override
    public void updateAerauthorisationReviewerDetail(AerAuthorizationBean aerAuthorizationBean) {

        Connection con = null;

        PreparedStatement pst = null;

        ResultSet rs = null;
        int res = 0;
        String doCurSpc = null;
        int mcode = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "DELETE FROM process_authorization WHERE off_code=? and role_type='REVIEWER' and process_id=13";
            pst = con.prepareStatement(sql);
            pst.setString(1, aerAuthorizationBean.getOffCode());
            pst.executeUpdate();

            sql = "DELETE FROM process_authorization WHERE off_code=? and role_type='VERIFIER' and process_id=13";
            pst = con.prepareStatement(sql);
            pst.setString(1, aerAuthorizationBean.getOffCode());
            pst.executeUpdate();

            /*String reviewer = aerAuthorizationBean.getReviewer();//000001-ryrtyry
             String[] tReviewer = reviewer.split("-");

             String verifier = aerAuthorizationBean.getVerifier();//000001-ryrtyry
             String[] tVerifier = verifier.split("-");

             String rule15MemoQry = "UPDATE process_authorization set hrms_id=?,spc=? WHERE off_code=? and role_type='REVIEWER' and process_id=13";
             pstmt = con.prepareStatement(rule15MemoQry);
             pstmt.setString(1, tReviewer[0]);
             pstmt.setString(2, tReviewer[1]);
             //pstmt.setString(3, aerAuthorizationBean.getFinancialyear());
             pstmt.setString(3, aerAuthorizationBean.getOffCode());
             pstmt.executeUpdate();

             //rule15MemoQry = "UPDATE process_authorization set hrms_id=?,spc=? WHERE financial_year=? and off_code=? and role_type='VERIFIER' and process_id=13";
             rule15MemoQry = "UPDATE process_authorization set hrms_id=?,spc=? WHERE off_code=? and role_type='VERIFIER' and process_id=13";
             pstmt = con.prepareStatement(rule15MemoQry);
             pstmt.setString(1, tVerifier[0]);
             pstmt.setString(2, tVerifier[1]);
             //pstmt.setString(3, aerAuthorizationBean.getFinancialyear());
             pstmt.setString(3, aerAuthorizationBean.getOffCode());
             pstmt.executeUpdate();*/
        } catch (Exception e) {
            res = 0;
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void saveAerauthorisationDetail(AerAuthorizationBean aerAuthorizationBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        //int sl_no = 0;
        try {
            con = dataSource.getConnection();
            //sl_no = CommonFunctions.getMaxCode(con, "process_authorization", "sl_no");
            String operator = aerAuthorizationBean.getOperator();//000001-ryrtyry
            String[] tOperator = operator.split("-");
            String rule15MemoQry = "INSERT INTO process_authorization (financial_year,process_id,role_type,hrms_id,spc,authority_type,off_code ) VALUES (?,?,?,?,?,?,?)";
            pstmt = con.prepareStatement(rule15MemoQry);
            //pstmt.setInt(1, sl_no);
            pstmt.setString(1, aerAuthorizationBean.getFinancialyear());
            pstmt.setInt(2, 13);
            pstmt.setString(3, "OPERATOR");
            pstmt.setString(4, tOperator[0]);
            pstmt.setString(5, tOperator[1]);
            pstmt.setString(6, aerAuthorizationBean.getAuthoritytype());
            pstmt.setString(7, aerAuthorizationBean.getOffCode());
            pstmt.executeUpdate();

            String approver = aerAuthorizationBean.getApprover();//000001-ryrtyry
            String[] tApprover = approver.split("-");
            //sl_no = CommonFunctions.getMaxCode(con, "process_authorization", "sl_no");
            rule15MemoQry = "INSERT INTO process_authorization (financial_year,process_id,role_type,hrms_id,spc ,authority_type,off_code ) VALUES (?,?,?,?,?,?,?)";
            pstmt = con.prepareStatement(rule15MemoQry);
            //pstmt.setInt(1, sl_no);
            pstmt.setString(1, aerAuthorizationBean.getFinancialyear());
            pstmt.setInt(2, 13);
            pstmt.setString(3, "APPROVER");
            pstmt.setString(4, tApprover[0]);
            pstmt.setString(5, tApprover[1]);
            pstmt.setString(6, aerAuthorizationBean.getAuthoritytype());
            pstmt.setString(7, aerAuthorizationBean.getOffCode());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveEmpApproverAuth(AerAuthorizationBean aerAuthorizationBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        //int sl_no = 0;
        try {
            java.sql.Timestamp currentdate = new java.sql.Timestamp(new java.util.Date().getTime());
            con = dataSource.getConnection();
            //sl_no = CommonFunctions.getMaxCode(con, "process_authorization", "sl_no");
            String profileApprover = aerAuthorizationBean.getProfileApprover();//000001-ryrtyry
            String[] tApprover = profileApprover.split("-");
            String rule15MemoQry = "INSERT INTO process_authorization (financial_year,process_id,role_type,hrms_id,spc,authority_type,off_code,entered_by_hrmsid,date_of_entry ) VALUES (?,?,?,?,?,?,?,?,?)";
            pstmt = con.prepareStatement(rule15MemoQry);
            //pstmt.setInt(1, sl_no);
            pstmt.setString(1, aerAuthorizationBean.getFinancialyear());
            pstmt.setInt(2, aerAuthorizationBean.getProcessid());
            pstmt.setString(3, "APPROVER");
            pstmt.setString(4, tApprover[0]);
            pstmt.setString(5, tApprover[1]);
            pstmt.setString(6, aerAuthorizationBean.getAuthoritytype());
            pstmt.setString(7, aerAuthorizationBean.getOffCode());
            pstmt.setString(8, aerAuthorizationBean.getEnteredByhrmsid());
            pstmt.setTimestamp(9, currentdate);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateAerauthorisationDetail(AerAuthorizationBean aerAuthorizationBean) {
        Connection con = null;
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        int res = 0;
        String doCurSpc = null;
        int mcode = 0;
        try {
            con = dataSource.getConnection();
            String operator = aerAuthorizationBean.getOperator();//000001-ryrtyry
            String[] tOperator = operator.split("-");

            String approver = aerAuthorizationBean.getApprover();//000001-ryrtyry
            String[] tApprover = approver.split("-");

            //String rule15MemoQry = "UPDATE process_authorization set hrms_id=?,spc=? WHERE financial_year=? and off_code=? and role_type='OPERATOR' and process_id=13";
            String rule15MemoQry = "UPDATE process_authorization set hrms_id=?,spc=? WHERE off_code=? and role_type='OPERATOR' and process_id=13";
            pstmt = con.prepareStatement(rule15MemoQry);
            pstmt.setString(1, tOperator[0]);
            pstmt.setString(2, tOperator[1]);
            //pstmt.setString(3, aerAuthorizationBean.getFinancialyear());
            pstmt.setString(3, aerAuthorizationBean.getOffCode());
            pstmt.executeUpdate();

            //rule15MemoQry = "UPDATE process_authorization set hrms_id=?,spc=? WHERE financial_year=? and off_code=? and role_type='APPROVER' and process_id=13";
            rule15MemoQry = "UPDATE process_authorization set hrms_id=?,spc=? WHERE off_code=? and role_type='APPROVER' and process_id=13";
            pstmt = con.prepareStatement(rule15MemoQry);
            pstmt.setString(1, tApprover[0]);
            pstmt.setString(2, tApprover[1]);
            //pstmt.setString(3, aerAuthorizationBean.getFinancialyear());
            pstmt.setString(3, aerAuthorizationBean.getOffCode());
            pstmt.executeUpdate();

        } catch (Exception e) {
            res = 0;
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void updateEmpApproverAuth(AerAuthorizationBean aerAuthorizationBean) {
        Connection con = null;
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        int res = 0;
        String doCurSpc = null;
        int mcode = 0;
        try {
            con = dataSource.getConnection();

            String profileApprover = aerAuthorizationBean.getProfileApprover();
            String[] tApprover = profileApprover.split("-");

            pstmt = con.prepareStatement("UPDATE process_authorization set hrms_id=?,spc=? WHERE off_code=? and role_type='APPROVER' and process_id=14");
            pstmt.setString(1, tApprover[0]);
            pstmt.setString(2, tApprover[1]);
            //pstmt.setString(3, aerAuthorizationBean.getFinancialyear());
            pstmt.setString(3, aerAuthorizationBean.getOffCode());
            pstmt.executeUpdate();

        } catch (Exception e) {
            res = 0;
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void saveValidatorServiceBookAuth(AerAuthorizationBean aerAuthorizationBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        //int sl_no = 0;
        try {
            java.sql.Timestamp currentdate = new java.sql.Timestamp(new java.util.Date().getTime());
            con = dataSource.getConnection();
            //sl_no = CommonFunctions.getMaxCode(con, "process_authorization", "sl_no");

            String[] validatorArr = aerAuthorizationBean.getServiceBookValidator().split(",");
            if (validatorArr != null && validatorArr.length > 0) {
                for (int i = 0; i < validatorArr.length; i++) {
                    String validator = validatorArr[i];//000001-ryrtyry

                    //sl_no = CommonFunctions.getMaxCode(con, "process_authorization", "sl_no");
                    String[] tvalidator = validator.split("-");

                    String isDuplicate = isAuthorityDuplicate(aerAuthorizationBean.getOffCode(), "SERVICE BOOK VALIDATOR", tvalidator[0]);

                    String rule15MemoQry = "INSERT INTO process_authorization (financial_year,process_id,role_type,hrms_id,spc,authority_type,off_code,entered_by_hrmsid,date_of_entry ) VALUES (?,?,?,?,?,?,?,?,?)";
                    pstmt = con.prepareStatement(rule15MemoQry);
                    //pstmt.setInt(1, sl_no);
                    pstmt.setString(1, aerAuthorizationBean.getFinancialyear());
                    pstmt.setInt(2, aerAuthorizationBean.getProcessid());
                    pstmt.setString(3, "SERVICE BOOK VALIDATOR");
                    pstmt.setString(4, tvalidator[0]);
                    pstmt.setString(5, tvalidator[1]);
                    pstmt.setString(6, aerAuthorizationBean.getAuthoritytype());
                    pstmt.setString(7, aerAuthorizationBean.getOffCode());
                    pstmt.setString(8, aerAuthorizationBean.getEnteredByhrmsid());
                    pstmt.setTimestamp(9, currentdate);
                    if (isDuplicate.equals("N")) {
                        pstmt.executeUpdate();
                    }
                }
            }

            /* String profileApprover = aerAuthorizationBean.getServiceBookValidator();//000001-ryrtyry
             String[] tApprover = profileApprover.split("-");
             String rule15MemoQry = "INSERT INTO process_authorization (financial_year,process_id,role_type,hrms_id,spc,authority_type,off_code,entered_by_hrmsid,date_of_entry ) VALUES (?,?,?,?,?,?,?,?,?)";
             pstmt = con.prepareStatement(rule15MemoQry);
             //pstmt.setInt(1, sl_no);
             pstmt.setString(1, aerAuthorizationBean.getFinancialyear());
             pstmt.setInt(2, aerAuthorizationBean.getProcessid());
             pstmt.setString(3, "SERVICE BOOK VALIDATOR");
             pstmt.setString(4, tApprover[0]);
             pstmt.setString(5, tApprover[1]);
             pstmt.setString(6, aerAuthorizationBean.getAuthoritytype());
             pstmt.setString(7, aerAuthorizationBean.getOffCode());
             pstmt.setString(8, aerAuthorizationBean.getEnteredByhrmsid());
             pstmt.setTimestamp(9, currentdate);
             pstmt.executeUpdate();*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateValidatorServiceBookAuth(AerAuthorizationBean aerAuthorizationBean) {
        Connection con = null;
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        int res = 0;
        String doCurSpc = null;
        int mcode = 0;
        try {
            con = dataSource.getConnection();

            String sql = "DELETE FROM process_authorization WHERE off_code=? and role_type='SERVICE BOOK VALIDATOR' and process_id=22";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, aerAuthorizationBean.getOffCode());
            pstmt.executeUpdate();

            /*String profileApprover = aerAuthorizationBean.getServiceBookValidator();
             String[] tApprover = profileApprover.split("-");

             pstmt = con.prepareStatement("UPDATE process_authorization set hrms_id=?,spc=? WHERE off_code=? and role_type='SERVICE BOOK VALIDATOR' and process_id=22");
             pstmt.setString(1, tApprover[0]);
             pstmt.setString(2, tApprover[1]);
             //pstmt.setString(3, aerAuthorizationBean.getFinancialyear());
             pstmt.setString(3, aerAuthorizationBean.getOffCode());
             pstmt.executeUpdate();*/
        } catch (Exception e) {
            res = 0;
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void saveentryServiceBookAuth(AerAuthorizationBean aerAuthorizationBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        //code for insert into g_previlage_map
        PreparedStatement selectpst = null;
        ResultSet selectrs = null;

        PreparedStatement insertpst = null;

        PreparedStatement maxIdpst = null;
        ResultSet maxIdrs = null;

        PreparedStatement deletepst = null;

        int mCode = 1;

        String status = "Y";

        //end
        try {
            java.sql.Timestamp currentdate = new java.sql.Timestamp(new java.util.Date().getTime());
            con = dataSource.getConnection();

            String[] sbentryArr = aerAuthorizationBean.getServiceBookentry().split(",");
            if (sbentryArr != null && sbentryArr.length > 0) {
                for (int i = 0; i < sbentryArr.length; i++) {
                    String sbentry = sbentryArr[i];//000001-ryrtyry               
                    String[] sbenrtysplit = sbentry.split("-");

                    String isDuplicate = isAuthorityDuplicate(aerAuthorizationBean.getOffCode(), "SERVICE BOOK ENTRY", sbenrtysplit[0]);

                    String rule15MemoQry = "INSERT INTO process_authorization (financial_year,process_id,role_type,hrms_id,spc,authority_type,off_code,entered_by_hrmsid,date_of_entry ) VALUES (?,?,?,?,?,?,?,?,?)";
                    pstmt = con.prepareStatement(rule15MemoQry);
                    pstmt.setString(1, aerAuthorizationBean.getFinancialyear());
                    pstmt.setInt(2, aerAuthorizationBean.getProcessid());
                    pstmt.setString(3, "SERVICE BOOK ENTRY");
                    pstmt.setString(4, sbenrtysplit[0]);
                    pstmt.setString(5, sbenrtysplit[1]);
                    pstmt.setString(6, aerAuthorizationBean.getAuthoritytype());
                    pstmt.setString(7, aerAuthorizationBean.getOffCode());
                    pstmt.setString(8, aerAuthorizationBean.getEnteredByhrmsid());
                    pstmt.setTimestamp(9, currentdate);
                    if (isDuplicate.equals("N")) {
                        pstmt.executeUpdate();
                    }
                    //code for insert into g_previlage_map
                    String deletesql = "delete from g_privilege_map where spc=? and role_id='05'";
                    deletepst = con.prepareStatement(deletesql);

                    String selectsql = "select * from module_assign2_group_abstract where abstract_group_id in (1,2,3)";
                    selectpst = con.prepareStatement(selectsql);

                    String insertsql = "insert into g_privilege_map(priv_map_id,spc,role_id,mod_grp_id,mod_id) values(?,?,?,?,?)";
                    insertpst = con.prepareStatement(insertsql);

                    String maxIdsql = "select max(priv_map_id) priv_map_id from g_privilege_map";
                    maxIdpst = con.prepareStatement(maxIdsql);

                    deletepst.setString(1, sbenrtysplit[1]);
                    deletepst.executeUpdate();

                    int abstractId = 2;
                    //selectpst.setInt(1, abstractId);
                    selectrs = selectpst.executeQuery();
                    while (selectrs.next()) {
                        maxIdrs = maxIdpst.executeQuery();
                        if (maxIdrs.next()) {
                            if (maxIdrs != null) {
                                mCode = maxIdrs.getInt("priv_map_id") + 1;
                            }
                        }

                        String modGrpId = selectrs.getString("mod_grp_id");
                        int modId = selectrs.getInt("mod_id");

                        insertpst.setInt(1, mCode);
                        insertpst.setString(2, sbenrtysplit[1]);
                        insertpst.setString(3, "05");
                        insertpst.setString(4, modGrpId);
                        insertpst.setInt(5, modId);
                        insertpst.executeUpdate();

                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(selectrs, selectpst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateValidatorServiceBookentry(AerAuthorizationBean aerAuthorizationBean) {
        Connection con = null;
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        int res = 0;
        String doCurSpc = null;
        int mcode = 0;
        try {
            con = dataSource.getConnection();

            String sql = "DELETE FROM process_authorization WHERE off_code=? and role_type='SERVICE BOOK ENTRY' and process_id=23";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, aerAuthorizationBean.getOffCode());
            pstmt.executeUpdate();


            /* String profileApprover = aerAuthorizationBean.getServiceBookentry();
             String[] tApprover = profileApprover.split("-");

             pstmt = con.prepareStatement("UPDATE process_authorization set hrms_id=?,spc=? WHERE off_code=? and role_type='SERVICE BOOK ENTRY' and process_id=23");
             pstmt.setString(1, tApprover[0]);
             pstmt.setString(2, tApprover[1]);
             //pstmt.setString(3, aerAuthorizationBean.getFinancialyear());
             pstmt.setString(3, aerAuthorizationBean.getOffCode());
             pstmt.executeUpdate(); */
        } catch (Exception e) {
            res = 0;
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

    }
}
