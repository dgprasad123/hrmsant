/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.loancorrection;

import hrms.common.DataBaseFunctions;
import hrms.model.employee.Employee;
import hrms.model.loan.LTA;
import hrms.model.loan.LTAGroup;
import hrms.model.loan.Loan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
//import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Madhusmita
 */
public class LTACorrectionDAOImpl implements LTACorrectionDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getEmployeeLoanList(Loan empLoan) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Statement stmt = null;
        int i = 0;
        ArrayList ar = new ArrayList();
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            if (empLoan.getLoanName() != null && !empLoan.getLoanName().equals("")) {
                String sql = "select emp_mast.emp_id, TRIM(ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ')) FULL_NAME, F_NAME, gpf_no from ( \n"
                        + " select emp_id from  emp_loan_aqdtls where ad_code=? group by emp_id) loan \n"
                        + " inner join emp_mast on loan.emp_id=emp_mast.emp_id order by F_NAME";
                ps = con.prepareStatement(sql);
                ps.setString(1, empLoan.getLoanName());
                rs = ps.executeQuery();
                while (rs.next()) {
                    Loan emploan = new Loan();
                    emploan.setEmpName(rs.getString("full_name"));
                    emploan.setEmpid(rs.getString("emp_id"));
                    emploan.setGpfNo(rs.getString("gpf_no"));
                    ar.add(emploan);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ar;
    }

    @Override
    public void updateLoanDetails(Loan l) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;

        try {

            Date date = new Date(l.getOrderdate());
            con = dataSource.getConnection();
            ps = con.prepareStatement("UPDATE emp_loan_aqdtls SET loan_subtype=?, if_verified=? WHERE ad_ref_id=?");
            ps.setString(1, l.getLoanTp());
            ps.setString(2, "Y");
            ps.setString(3, l.getLoanid());
            ps.execute();

            ps2 = con.prepareStatement("SELECT NOT_ID FROM EMP_LOAN_SANC WHERE LOANID=?");
            ps2.setString(1, l.getLoanid());
            rs = ps2.executeQuery();
            if (rs.next()) {
                ps = con.prepareStatement("UPDATE EMP_NOTIFICATION SET ordno=?, orddt=? WHERE not_id=?");
                ps.setString(1, l.getOrderno());
                ps.setTimestamp(2, new Timestamp(date.getTime()));
                ps.setString(3, rs.getString("NOT_ID"));
                ps.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    /*@Override
     public String getLoanList(String empid) {
     Connection con = null;
     PreparedStatement ps = null;
     ResultSet rs = null;
     Statement stmt = null;
     String adamt = null;
     String nowdedn = null;
     String resultTab = null;
     try {
     con = dataSource.getConnection();
     stmt = con.createStatement();
     String sql = "select * from emp_loan_aqdtls where emp_id='" + empid + "'";
     rs = ps.executeQuery();
     while (rs.next()) {
     adamt = rs.getString("ad_amt");
     nowdedn = rs.getString("now_dedn");
     }
     resultTab = "<table>\n"
     + "<tr>\n"
     + " <td> + adamt</td>\n"
     + " <td> + nowdedn </td>\n"
     + "</tr> \n"
     + "</table>";
            

     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(ps);
     DataBaseFunctions.closeSqlObjects(con);
     }
     return null;
     }*/
    @Override
    public ArrayList getLtaList(String finYear, String loanType, String loaneeId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        Statement stmt = null;
        int i = 0;
        ArrayList li = new ArrayList();
        String ddocode="";
        String offname="";
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
            String loanTypeSql = "SELECT DISTINCT GA.loanee_id, GA.loan_type, GL.loan_name, GA.sanction_no"
                    + ", GA.sanction_dt, GA.release_amount FROM g_lt_adv GA "
                    + " left outer join g_loan GL ON GL.loan_tp = GA.loan_type WHERE GA.fin_year = ?"
                    + " AND (GA.loanee_id = ? OR GA.gpf_no = ?)";
                    if(loanType!=null && !loanType.equals(""))
                    {
                        loanTypeSql+= " AND GA.loan_type = ?";
                    }
            ps1 = con.prepareStatement(loanTypeSql);
            ps1.setString(1, finYear);
            ps1.setString(2, loaneeId);
            ps1.setString(3, loaneeId);
            if(loanType!=null && !loanType.equals(""))
            {            
                ps1.setString(4, loanType);
            }
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                loaneeId = rs1.getString("loanee_id");
                LTAGroup lg = new LTAGroup();
                String sql = "select GA.*, off_en, loan_name, TRIM(ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ')) FULL_NAME from  \n"
                        + " g_lt_adv GA \n"
                        + " left outer join emp_mast EM on GA.gpf_no=EM.gpf_no "
                        + " left outer join g_office GO ON go.off_code = concat(GA.ddo_code,'0000')"
                        + " left outer join g_loan GL ON GL.loan_tp = GA.loan_type"
                        + " WHERE GA.fin_year = ?"
                        + " AND GA.loanee_id = ? AND GA.loan_type = ?"
                        + " AND GA.sanction_no = ? AND GA.sanction_dt = ? ORDER BY CASE"
                        + " WHEN acc_month = '4' THEN 1"
                        + "              WHEN acc_month = '5' THEN 2"
                        + "              WHEN acc_month = '6' THEN 3"
                        + "              WHEN acc_month = '7' THEN 4"
                        + "              WHEN acc_month = '8' THEN 5"
                        + "              WHEN acc_month = '9' THEN 6"
                        + "              WHEN acc_month = '10' THEN 7"
                        + "              WHEN acc_month = '11' THEN 8"
                        + "              WHEN acc_month = '12' THEN 9"
                        + "              WHEN acc_month = '1' THEN 10"
                        + "              WHEN acc_month = '2' THEN 11"
                        + "              WHEN acc_month = '3' THEN 12"
                        + "         END;";

                ps = con.prepareStatement(sql);
                ps.setString(1, finYear);
                ps.setString(2, loaneeId);
                ps.setString(3, rs1.getString("loan_type"));
                ps.setString(4, rs1.getString("sanction_no"));
                ps.setString(5, rs1.getString("sanction_dt"));
                rs = ps.executeQuery();
                ArrayList ar = new ArrayList();
                lg.setAmountType(rs1.getString("loan_type"));
                lg.setLoanName(rs1.getString("loan_name"));
                lg.setSanctionDt(rs1.getString("sanction_dt"));
                lg.setSanctionNo(rs1.getString("sanction_no"));
                if(rs1.getString("release_amount")!=null && !rs1.getString("release_amount").equals("")){
                    lg.setReleaseAmount(numberFormat.format(rs1.getInt("release_amount")));
                }
                
                while (rs.next()) {
                    LTA ltList = new LTA();
                    ltList.setEmpName(rs.getString("FULL_NAME"));
                    ltList.setLoaneeId(rs.getString("loanee_id"));
                    ltList.setFinYear(rs.getString("fin_year"));
                    ltList.setDdoCode(rs.getString("ddo_code"));
                    ltList.setLoanType(rs.getString("loan_type"));
                    ltList.setAccMonth(rs.getString("acc_month"));
                    ltList.setCrMonth(rs.getString("cr_month_year"));
                    ltList.setSalMonth(rs.getString("sal_mnth"));
                    
                    ltList.setInstallmentAmount(StringUtils.defaultString(rs.getInt("installment_amt")+"", "0"));
                    ltList.setAmountType(rs.getString("amount_type"));
                    ltList.setAdvOpBal(numberFormat.format(rs.getInt("adv_op_bal")));
                    ltList.setAdvDrawn(numberFormat.format(rs.getInt("adv_drawn")));
                    ltList.setAdvRepd(numberFormat.format(rs.getInt("adv_repd")));
                    ltList.setAdvClBal(numberFormat.format(rs.getInt("adv_cl_bal")));
                    ltList.setReleaseAmount(numberFormat.format(rs.getInt("release_amount")));
                    ltList.setOfficeName(rs.getString("off_en"));
                    ltList.setLoanName(rs.getString("loan_name"));
                    ar.add(ltList);
                    ddocode=ltList.getDdoCode();
                    offname=ltList.getOfficeName();
                }
                lg.setLastDDo(ddocode);
                lg.setLastOfficeName(offname);
                lg.setDataList(ar);
                li.add(lg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }
}
