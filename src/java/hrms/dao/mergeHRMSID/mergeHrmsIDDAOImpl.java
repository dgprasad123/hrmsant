/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.mergeHRMSID;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.mergeHRMSID.mergeHrmsIDDAO;
import hrms.model.mergeHRMSID.mergeDuplicateHrmsidForm;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Madhusmita
 */
public class mergeHrmsIDDAOImpl implements mergeHrmsIDDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public mergeDuplicateHrmsidForm getEmpData(String srcEmpId, String finalEmpId) {
        Connection con = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ArrayList EmpDetails = new ArrayList();
        ArrayList finalEmpDetails = new ArrayList();
        mergeDuplicateHrmsidForm mergeIDForm = null;

        try {
            con = this.dataSource.getConnection();
            //--------------------------Query For Duplicate HRMSID---------------------------
            mergeIDForm = new mergeDuplicateHrmsidForm();
            ps = con.prepareStatement("select emp_id, array_to_string(array[initials,f_name,m_name,l_name],' ')empNm,gpf_no,acct_type,bank_acc_no,ifsc_code,to_char(dob,'dd-MON-yyyy')dob,\n"
                    + "to_char(dos,'dd-MON-yyyy')dos,off_en,cur_spc,is_regular,post,mobile from (select * from emp_mast where emp_id =?)emp\n"
                    + "left outer join g_post on g_post.post_code=substring(emp.cur_spc,14,6)\n"
                    + "left outer join g_office on g_office.off_code=emp.cur_off_code\n"
                    + "left outer join g_branch on g_branch.branch_code=emp.branch_code");
            ps.setString(1, srcEmpId);
            rs = ps.executeQuery();
            if (rs.next()) {
                mergeIDForm.setSrcEmpId(rs.getString("emp_id"));
                mergeIDForm.setSrcEmpNm(rs.getString("empNm"));
                mergeIDForm.setSrcBankAcctNo(rs.getString("bank_acc_no"));
                mergeIDForm.setSrcIfscCode(rs.getString("ifsc_code"));
                mergeIDForm.setSrcGpfNo(rs.getString("gpf_no"));
                /*if (mergeIDForm.getSrcGpfNo().contains("_DUP")) {
                 mergeIDForm.setMsgCompleted("BOTH HRMSID PREVIOUSLY MERGED");
                 }*/
                mergeIDForm.setSrcDob(rs.getString("dob"));
                mergeIDForm.setSrcDos(rs.getString("dos"));
                mergeIDForm.setSrcEmpType(rs.getString("is_regular"));
                if (rs.getString("is_regular").equals("Y")) {
                    mergeIDForm.setSrcEmpType("REGULAR");
                } else if (rs.getString("is_regular").equals("C")) {
                    mergeIDForm.setSrcEmpType("CONTRACTUAL 6 YR.");
                } else if (rs.getString("is_regular").equals("N")) {
                    mergeIDForm.setSrcEmpType("CONTRACTUAL");
                } else if (rs.getString("is_regular").equals("B")) {
                    mergeIDForm.setSrcEmpType("LEVEL-V(EX CADRE)");
                }else if (rs.getString("is_regular").equals("G")) {
                    mergeIDForm.setSrcEmpType("WAGES");
                }else if (rs.getString("is_regular").equals("W")) {
                    mergeIDForm.setSrcEmpType("WORK CHARGED");
                }
                mergeIDForm.setSrcDesignation(rs.getString("post"));
                mergeIDForm.setSrcOffEn(rs.getString("off_en"));
                mergeIDForm.setSrcMobile(rs.getString("mobile"));

            }

            //--------------------------Query For Original HRMSID---------------------------
            ps1 = con.prepareStatement("select emp_id, array_to_string(array[initials,f_name,m_name,l_name],' ')empNm,gpf_no,acct_type,bank_acc_no,ifsc_code,to_char(dob,'dd-MON-yyyy')dob,\n"
                    + "to_char(dos,'dd-MON-yyyy')dos,off_en,cur_spc,is_regular,post,mobile from (select * from emp_mast where emp_id =?)emp\n"
                    + "left outer join g_post on g_post.post_code=substring(emp.cur_spc,14,6)\n"
                    + "left outer join g_office on g_office.off_code=emp.cur_off_code\n"
                    + "left outer join g_branch on g_branch.branch_code=emp.branch_code");
            ps1.setString(1, finalEmpId);
            rs1 = ps1.executeQuery();
            if (rs1.next()) {
                mergeIDForm.setFinalEmpId(rs1.getString("emp_id"));
                mergeIDForm.setFinalEmpNm(rs1.getString("empNm"));
                mergeIDForm.setFinalBankAcctNo(rs1.getString("bank_acc_no"));
                mergeIDForm.setFinalIfscCode(rs1.getString("ifsc_code"));
                mergeIDForm.setFinalGpfNo(rs1.getString("gpf_no"));
                mergeIDForm.setFinalDob(rs1.getString("dob"));
                mergeIDForm.setFinalDos(rs1.getString("dos"));
                if (rs1.getString("is_regular").equals("Y")) {
                    mergeIDForm.setFinalEmpType("REGULAR");
                } else if (rs1.getString("is_regular").equals("C")) {
                    mergeIDForm.setFinalEmpType("CONTRACTUAL 6 YR.");
                } else if (rs1.getString("is_regular").equals("N")) {
                    mergeIDForm.setFinalEmpType("CONTRACTUAL");
                } else if (rs1.getString("is_regular").equals("B")) {
                    mergeIDForm.setFinalEmpType("LEVEL-V(EX CADRE)");
                }
                else if (rs1.getString("is_regular").equals("G")) {
                    mergeIDForm.setFinalEmpType("WAGES");
                }
                else if (rs1.getString("is_regular").equals("W")) {
                    mergeIDForm.setFinalEmpType("WORK CHARGED");
                }
                mergeIDForm.setFinalDesignation(rs1.getString("post"));
                mergeIDForm.setFinalOffEn(rs1.getString("off_en"));
                mergeIDForm.setFinalMobile(rs1.getString("mobile"));

            }
            if (mergeIDForm.getSrcGpfNo() != null && mergeIDForm.getSrcGpfNo().equals(mergeIDForm.getFinalEmpId() + "_DUP")) {
                mergeIDForm.setMsgCompleted("BOTH HRMSID PREVIOUSLY MERGED");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(rs1, ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return mergeIDForm;
    }

    @Override
    public ArrayList getLastSalary(String empid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList salList = new ArrayList();
        mergeDuplicateHrmsidForm mergeIDForm = null;
        try {
            String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select off_code,aq_month,aq_year,emp_name,emp_code from aq_mast where emp_code=? order by p_year desc, p_month asc");
            ps.setString(1, empid);
            rs = ps.executeQuery();
            while (rs.next()) {

                mergeIDForm = new mergeDuplicateHrmsidForm();
                mergeIDForm.setAqMonth(monthNames[(rs.getInt("aq_month"))]);
                mergeIDForm.setAqYear(rs.getString("aq_year"));
                mergeIDForm.setOffCode(rs.getString("off_code"));
                mergeIDForm.setEmpNm(rs.getString("emp_name"));
                mergeIDForm.setEmpCode(rs.getString("emp_code"));
                salList.add(mergeIDForm);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return salList;
    }

    @Override
    public mergeDuplicateHrmsidForm mergeBothHrmsId(String srcEmpId, String finalEmpId) {
        Connection con = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        PreparedStatement pst = null;
        String message = null;
        mergeDuplicateHrmsidForm mform = null;
        String msg1, msg2 = null;

        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select cur_spc,cur_off_code,is_regular,bank_acc_no from emp_mast where emp_id=?");
            pst.setString(1, finalEmpId);
            rs = pst.executeQuery();

            if (rs.next()) {
                mform = new mergeDuplicateHrmsidForm();
                mform.setFinIsRegular(rs.getString("is_regular"));
                mform.setFinalBankAcctNo(rs.getString("bank_acc_no"));
                mform.setFinCurSpc(rs.getString("cur_spc"));
                /*pst = con.prepareStatement("select max(aq_year) maxSrcYr,count(*) from aq_mast where emp_code=?");
                 pst.setString(1, srcEmpId);
                 rs1 = pst.executeQuery();

                 pst = con.prepareStatement("select max(aq_year),count(*) from aq_mast where emp_code=?");
                 pst.setString(1, finalEmpId);
                 rs2 = pst.executeQuery();*/

                if (mform.getFinIsRegular().equals("Y") || mform.getFinIsRegular().equals("C")) {
                    if ((mform.getFinCurSpc() != null && !mform.getFinCurSpc().equals("")) && (mform.getFinalBankAcctNo() != null && !mform.getFinalBankAcctNo().equals(""))) {
                        CallableStatement callSt = null;

                        callSt = con.prepareCall("{call MERGE_DUPLICATE_HRMSID(?,?)}");
                        callSt.setString(1, srcEmpId);
                        callSt.setString(2, finalEmpId);
                        callSt.execute();
                        //mform.setMsgCompleted(srcEmpId + " EMPLOYEE Merged Sucessfully with  " + finalEmpId);
                        message = srcEmpId + " EMPLOYEE Merged Sucessfully with  " + finalEmpId;

                        //UPDATION OF USERIDPWD IN OID WRT DB
                        //disable employee in postgres
                        //conpostgres = CommonFunctions.getPGSQLConn("hrmis2");
                        //stt1 = conpostgres.createStatement();*/
                        pst = con.prepareStatement("select linkid from user_details where linkid=?");
                        pst.setString(1, srcEmpId);
                        rs = pst.executeQuery();

                        if (rs.next()) {

                            String queryFoUserdl = "update user_details set   password=?,accountnonlocked=?,no_login_failed=5  where LINKID=?";
                            pst = con.prepareStatement(queryFoUserdl);
                            pst.setString(1, ".-$%`");
                            pst.setInt(2, 0);
                            pst.setString(3, srcEmpId);
                            pst.executeUpdate();
                        }

                        message = message + ("<br />" + srcEmpId + "  Duplicate Employee login acct locked.");

                        String queryFoUserd1l = "update emp_mast set   pwd=?,mobile=? ,cur_spc=? ,next_office_code=?,if_profile_completed=?,if_profile_verified=? where emp_id=?";
                        pst = con.prepareStatement(queryFoUserd1l);
                        pst.setString(1, ".-#-`");//pwd
                        pst.setInt(2, 0);//MOBILE
                        pst.setString(3, null);//cur_spc                        
                        pst.setString(4, "");//next_office_code
                        pst.setString(5, "N");//if_profile_completed
                        pst.setString(6, "N");//if_profile_verified
                        pst.setString(7, srcEmpId);
                        pst.executeUpdate();
                        message = message + ("<br />" + srcEmpId + "  Duplicate Employee password, mobile reset.");

                        /*pst = con.prepareStatement("delete from emp_relation where emp_id=?");
                         pst.setString(1, srcEmpId);
                         pst.execute();*/
                        mform.setMsgCompleted(message);
                    } else {
                        if (mform.getFinCurSpc() == null || mform.getFinCurSpc().equals("")) {
                            mform.setMsgForDesination("Designation is Blank In Original HRMS ID. Update Designation Before Merging");
                        }
                        if (mform.getFinalBankAcctNo() == null || mform.getFinalBankAcctNo().equals("")) {
                            mform.setMsgForbank("Bank Account No. is Blank In Original HRMS ID. Update Bank Account No. Before Merging");
                        }

                    }

                } else if (mform.getFinIsRegular().equals("N") || mform.getFinIsRegular().equals("B")||mform.getFinIsRegular().equals("W")||mform.getFinIsRegular().equals("G")) {
                    if (mform.getFinalBankAcctNo() != null && !mform.getFinalBankAcctNo().equals("")) {
                        CallableStatement callSt = null;

                        callSt = con.prepareCall("{call MERGE_DUPLICATE_HRMSID(?,?)}");
                        callSt.setString(1, srcEmpId);
                        callSt.setString(2, finalEmpId);
                        callSt.execute();
                        //mform.setMsgCompleted(srcEmpId + " EMPLOYEE Merged Sucessfully with  " + finalEmpId);
                        message = srcEmpId + " EMPLOYEE Merged Sucessfully with  " + finalEmpId;

                        //UPDATION OF USERIDPWD IN OID WRT DB
                        //disable employee in postgres
                        //conpostgres = CommonFunctions.getPGSQLConn("hrmis2");
                        //stt1 = conpostgres.createStatement();
                        pst = con.prepareStatement("select linkid from user_details where linkid=?");
                        pst.setString(1, srcEmpId);
                        rs = pst.executeQuery();

                        if (rs.next()) {

                            String queryFoUserdl = "update user_details set   password=?,accountnonlocked=?,no_login_failed=5  where LINKID=?";
                            pst = con.prepareStatement(queryFoUserdl);
                            pst.setString(1, ".-$%`");
                            pst.setInt(2, 0);
                            pst.setString(3, srcEmpId);
                            pst.executeUpdate();
                        }

                        //mform.setMsg1(srcEmpId + "  Duplicate Employee login acct locked.");
                        message = message + ("<br />" + srcEmpId + "  Duplicate Employee login acct locked.");

                        String queryFoUserd1l = "update emp_mast set   pwd=?,mobile=? ,cur_spc=? ,next_office_code=?,if_profile_completed=?,if_profile_verified=? where emp_id=?";
                        pst = con.prepareStatement(queryFoUserd1l);
                        pst.setString(1, ".-#-`");//pwd
                        pst.setInt(2, 0);//MOBILE
                        pst.setString(3, null);//cur_spc                        
                        pst.setString(4, "");//next_office_code
                        pst.setString(5, "N");//if_profile_completed
                        pst.setString(6, "N");//if_profile_verified
                        pst.setString(7, srcEmpId);
                        pst.executeUpdate();
                        //mform.setMsg2(srcEmpId + "  Duplicate Employee mast pwd,mobile reset.");
                        message = message + ("<br/>" + srcEmpId + "  Duplicate Employee password, mobile reset.");

                        pst = con.prepareStatement("delete from emp_relation where emp_id=?");
                        pst.setString(1, srcEmpId);
                        pst.execute();
                        mform.setMsgCompleted(message);

                    } else {
                        mform.setMsgForbank("Bank Account No. is Blank In Original HRMS ID. Update Bank Account No. Before Merging");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return mform;
    }
}
